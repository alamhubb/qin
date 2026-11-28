package com.qin.lang.module.resolver;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class QinTsConfigPathResolver {
    private final Map<Path, List<PathMapping>> mappingsByConfig = new LinkedHashMap<>();
    private final Map<Path, Path> nearestConfigByDirectory = new LinkedHashMap<>();

    List<Path> resolveCandidates(Path importerFile, String specifier) {
        Path config = nearestConfig(importerFile.getParent());
        if (config == null) {
            return List.of();
        }

        List<PathMapping> matchingMappings = new ArrayList<>();
        for (PathMapping mapping : mappings(config)) {
            if (mapping.match(specifier) != null) {
                matchingMappings.add(mapping);
            }
        }
        matchingMappings.sort(Comparator
                .comparing(PathMapping::hasWildcard)
                .thenComparing(Comparator.comparingInt(PathMapping::prefixLength).reversed()));

        List<Path> candidates = new ArrayList<>();
        for (PathMapping mapping : matchingMappings) {
            String matchedText = mapping.match(specifier);
            for (String substitution : mapping.substitutions()) {
                String relativePath = mapping.hasWildcard()
                        ? substitution.replace("*", matchedText)
                        : substitution;
                candidates.add(mapping.baseDirectory().resolve(relativePath).toAbsolutePath().normalize());
            }
        }
        return candidates;
    }

    private Path nearestConfig(Path directory) {
        Path original = directory == null ? null : directory.toAbsolutePath().normalize();
        Path current = original;
        Path cached = nearestConfigByDirectory.get(original);
        if (cached != null || nearestConfigByDirectory.containsKey(original)) {
            return cached;
        }
        while (current != null) {
            Path config = current.resolve("tsconfig.json");
            if (Files.isRegularFile(config)) {
                nearestConfigByDirectory.put(original, config);
                return config;
            }
            current = current.getParent();
        }
        nearestConfigByDirectory.put(original, null);
        return null;
    }

    private List<PathMapping> mappings(Path config) {
        Path normalized = config.toAbsolutePath().normalize();
        List<PathMapping> cached = mappingsByConfig.get(normalized);
        if (cached != null) {
            return cached;
        }
        List<PathMapping> loaded = loadConfig(normalized, new LinkedHashSet<>()).mappings();
        mappingsByConfig.put(normalized, loaded);
        return loaded;
    }

    private TsConfigSettings loadConfig(Path config, Set<Path> visiting) {
        Path normalized = config.toAbsolutePath().normalize();
        if (!visiting.add(normalized)) {
            throw new IllegalArgumentException("Circular tsconfig extends chain at " + normalized);
        }

        JsonObject root = readConfig(normalized);
        Map<String, PathMapping> effective = new LinkedHashMap<>();
        Path inheritedBaseUrl = null;
        for (Path parentConfig : extendedConfigs(normalized, root)) {
            TsConfigSettings parent = loadConfig(parentConfig, visiting);
            inheritedBaseUrl = parent.baseUrl();
            for (PathMapping mapping : parent.mappings()) {
                effective.put(mapping.pattern(), mapping);
            }
        }

        JsonObject compilerOptions = objectField(root, "compilerOptions");
        Path baseUrl = inheritedBaseUrl;
        if (compilerOptions != null && compilerOptions.has("baseUrl")) {
            String baseUrlText = stringValue(compilerOptions.get("baseUrl"), normalized, "compilerOptions.baseUrl");
            baseUrl = normalized.getParent().resolve(baseUrlText).toAbsolutePath().normalize();
        }
        if (compilerOptions != null && compilerOptions.has("paths")) {
            effective.clear();
            JsonObject paths = objectValue(compilerOptions.get("paths"), normalized, "compilerOptions.paths");
            Path mappingBaseDirectory = baseUrl == null ? normalized.getParent() : baseUrl;
            for (Map.Entry<String, JsonElement> entry : paths.entrySet()) {
                effective.put(entry.getKey(), new PathMapping(
                        entry.getKey(),
                        stringArray(entry.getValue(), normalized, "compilerOptions.paths." + entry.getKey()),
                        mappingBaseDirectory));
            }
        }

        visiting.remove(normalized);
        return new TsConfigSettings(baseUrl, List.copyOf(effective.values()));
    }

    private List<Path> extendedConfigs(Path config, JsonObject root) {
        JsonElement extendsValue = root.get("extends");
        if (extendsValue == null || extendsValue.isJsonNull()) {
            return List.of();
        }
        if (extendsValue.isJsonArray()) {
            List<Path> parents = new ArrayList<>();
            for (JsonElement item : extendsValue.getAsJsonArray()) {
                parents.add(resolveExtendedConfig(config, stringValue(item, config, "extends")));
            }
            return parents;
        }
        return List.of(resolveExtendedConfig(config, stringValue(extendsValue, config, "extends")));
    }

    private Path resolveExtendedConfig(Path config, String extendsText) {
        Path requested = Path.of(extendsText);
        if (requested.isAbsolute() || extendsText.startsWith("./") || extendsText.startsWith("../")) {
            Path resolved = requested.isAbsolute() ? requested : config.getParent().resolve(requested);
            return resolveConfigFile(resolved);
        }

        int packageNameEnd = extendsText.startsWith("@") ? nthSlash(extendsText, 2) : extendsText.indexOf('/');
        String packageName = packageNameEnd < 0 ? extendsText : extendsText.substring(0, packageNameEnd);
        String subPath = packageNameEnd < 0 ? "" : extendsText.substring(packageNameEnd + 1);
        Path search = config.getParent();
        while (search != null) {
            Path packageDirectory = search.resolve("node_modules").resolve(packageName);
            if (Files.isDirectory(packageDirectory)) {
                return resolveConfigFile(subPath.isBlank() ? packageDirectory.resolve("tsconfig.json")
                        : packageDirectory.resolve(subPath));
            }
            search = search.getParent();
        }
        throw new IllegalArgumentException("Cannot resolve tsconfig extends '" + extendsText + "' from " + config);
    }

    private int nthSlash(String value, int occurrence) {
        int slash = -1;
        for (int index = 0; index < occurrence; index++) {
            slash = value.indexOf('/', slash + 1);
            if (slash < 0) {
                return -1;
            }
        }
        return slash;
    }

    private Path resolveConfigFile(Path requested) {
        Path normalized = requested.toAbsolutePath().normalize();
        if (Files.isDirectory(normalized)) {
            normalized = normalized.resolve("tsconfig.json");
        } else if (!Files.isRegularFile(normalized) && !normalized.toString().endsWith(".json")) {
            normalized = Path.of(normalized + ".json");
        }
        if (!Files.isRegularFile(normalized)) {
            throw new IllegalArgumentException("Cannot find extended tsconfig " + normalized);
        }
        return normalized;
    }

    private JsonObject readConfig(Path config) {
        if (!Files.isRegularFile(config)) {
            throw new IllegalArgumentException("Cannot find tsconfig " + config);
        }
        try {
            String source = Files.readString(config, StandardCharsets.UTF_8);
            JsonElement parsed = JsonParser.parseString(removeTrailingCommas(stripComments(source)));
            if (!parsed.isJsonObject()) {
                throw new IllegalArgumentException("Expected tsconfig object in " + config);
            }
            return parsed.getAsJsonObject();
        } catch (IOException error) {
            throw new IllegalArgumentException("Failed to read tsconfig " + config, error);
        }
    }

    private String stripComments(String source) {
        StringBuilder result = new StringBuilder(source.length());
        boolean inString = false;
        boolean escaped = false;
        boolean lineComment = false;
        boolean blockComment = false;
        for (int index = 0; index < source.length(); index++) {
            char current = source.charAt(index);
            char next = index + 1 < source.length() ? source.charAt(index + 1) : '\0';
            if (lineComment) {
                if (current == '\n' || current == '\r') {
                    lineComment = false;
                    result.append(current);
                } else {
                    result.append(' ');
                }
            } else if (blockComment) {
                if (current == '*' && next == '/') {
                    blockComment = false;
                    result.append("  ");
                    index++;
                } else {
                    result.append(current == '\n' || current == '\r' ? current : ' ');
                }
            } else if (inString) {
                result.append(current);
                if (escaped) {
                    escaped = false;
                } else if (current == '\\') {
                    escaped = true;
                } else if (current == '"') {
                    inString = false;
                }
            } else if (current == '"') {
                inString = true;
                result.append(current);
            } else if (current == '/' && next == '/') {
                lineComment = true;
                result.append("  ");
                index++;
            } else if (current == '/' && next == '*') {
                blockComment = true;
                result.append("  ");
                index++;
            } else {
                result.append(current);
            }
        }
        return result.toString();
    }

    private String removeTrailingCommas(String source) {
        StringBuilder result = new StringBuilder(source.length());
        boolean inString = false;
        boolean escaped = false;
        for (int index = 0; index < source.length(); index++) {
            char current = source.charAt(index);
            if (inString) {
                result.append(current);
                if (escaped) {
                    escaped = false;
                } else if (current == '\\') {
                    escaped = true;
                } else if (current == '"') {
                    inString = false;
                }
            } else if (current == '"') {
                inString = true;
                result.append(current);
            } else if (current == ',') {
                int next = index + 1;
                while (next < source.length() && Character.isWhitespace(source.charAt(next))) {
                    next++;
                }
                if (next == source.length() || (source.charAt(next) != '}' && source.charAt(next) != ']')) {
                    result.append(current);
                }
            } else {
                result.append(current);
            }
        }
        return result.toString();
    }

    private JsonObject objectField(JsonObject object, String field) {
        JsonElement value = object.get(field);
        if (value == null || value.isJsonNull()) {
            return null;
        }
        return objectValue(value, null, field);
    }

    private JsonObject objectValue(JsonElement value, Path config, String field) {
        if (!value.isJsonObject()) {
            throw new IllegalArgumentException("Expected object for " + field + " in " + config);
        }
        return value.getAsJsonObject();
    }

    private List<String> stringArray(JsonElement value, Path config, String field) {
        if (!value.isJsonArray()) {
            throw new IllegalArgumentException("Expected string array for " + field + " in " + config);
        }
        JsonArray array = value.getAsJsonArray();
        List<String> strings = new ArrayList<>();
        for (JsonElement item : array) {
            strings.add(stringValue(item, config, field));
        }
        return List.copyOf(strings);
    }

    private String stringValue(JsonElement value, Path config, String field) {
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString()) {
            throw new IllegalArgumentException("Expected string for " + field + " in " + config);
        }
        return value.getAsString();
    }

    private record PathMapping(String pattern, List<String> substitutions, Path baseDirectory) {
        boolean hasWildcard() {
            return pattern.indexOf('*') >= 0;
        }

        int prefixLength() {
            int wildcard = pattern.indexOf('*');
            return wildcard < 0 ? pattern.length() : wildcard;
        }

        String match(String specifier) {
            int wildcard = pattern.indexOf('*');
            if (wildcard < 0) {
                return pattern.equals(specifier) ? "" : null;
            }
            if (pattern.indexOf('*', wildcard + 1) >= 0) {
                return null;
            }
            String prefix = pattern.substring(0, wildcard);
            String suffix = pattern.substring(wildcard + 1);
            if (!specifier.startsWith(prefix) || !specifier.endsWith(suffix)
                    || specifier.length() < prefix.length() + suffix.length()) {
                return null;
            }
            return specifier.substring(prefix.length(), specifier.length() - suffix.length());
        }
    }

    private record TsConfigSettings(Path baseUrl, List<PathMapping> mappings) {
    }
}
