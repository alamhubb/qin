package com.qin.lang.module.resolver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolves ESM module specifiers for local files and node_modules bare imports.
 */
public final class QinEsmSpecifierResolver {
    private static final Pattern JSON_STRING_FIELD = Pattern.compile(
            "\"%s\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern EXPORTS_IMPORT_DOT = Pattern.compile(
            "\"exports\"\\s*:\\s*\\{[^}]*\"\\.\"\\s*:\\s*\\{[^}]*\"import\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.DOTALL);
    private static final Pattern EXPORTS_DEFAULT_DOT = Pattern.compile(
            "\"exports\"\\s*:\\s*\\{[^}]*\"\\.\"\\s*:\\s*\\{[^}]*\"default\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.DOTALL);
    private static final Pattern EXPORTS_DOT_STRING = Pattern.compile(
            "\"exports\"\\s*:\\s*\\{[^}]*\"\\.\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.DOTALL);
    private static final Pattern EXPORTS_IMPORT_ROOT = Pattern.compile(
            "\"exports\"\\s*:\\s*\\{[^}]*\"import\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.DOTALL);
    private static final Pattern EXPORTS_DEFAULT_ROOT = Pattern.compile(
            "\"exports\"\\s*:\\s*\\{[^}]*\"default\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.DOTALL);
    private static final Pattern EXPORTS_STRING_ROOT = Pattern.compile(
            "\"exports\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.DOTALL);
    private static final Pattern EXPORTS_NODE_IMPORT_DEFAULT_DOT = Pattern.compile(
            "\"exports\"\\s*:\\s*\\{[^}]*\"\\.\"\\s*:\\s*\\{[\\s\\S]*?\"import\"\\s*:\\s*\\{[\\s\\S]*?\"node\"\\s*:\\s*\\{[\\s\\S]*?\"default\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.DOTALL);
    private static final Pattern EXPORTS_NODE_IMPORT_DEFAULT_ROOT = Pattern.compile(
            "\"exports\"\\s*:\\s*\\{[\\s\\S]*?\"import\"\\s*:\\s*\\{[\\s\\S]*?\"node\"\\s*:\\s*\\{[\\s\\S]*?\"default\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.DOTALL);
    private static final Pattern EXPORTS_IMPORT_DEFAULT_FALLBACK_DOT = Pattern.compile(
            "\"exports\"\\s*:\\s*\\{[^}]*\"\\.\"\\s*:\\s*\\{[\\s\\S]*?\"import\"\\s*:\\s*\\{[\\s\\S]*?\"default\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.DOTALL);
    private static final Pattern EXPORTS_IMPORT_DEFAULT_FALLBACK_ROOT = Pattern.compile(
            "\"exports\"\\s*:\\s*\\{[\\s\\S]*?\"import\"\\s*:\\s*\\{[\\s\\S]*?\"default\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.DOTALL);

    public static boolean isHostRuntimeModule(String specifier) {
        if (specifier == null || specifier.isBlank()) {
            return false;
        }
        String normalized = specifier.trim();
        return normalized.startsWith("node:")
                || "assert".equals(normalized)
                || "buffer".equals(normalized)
                || "child_process".equals(normalized)
                || "crypto".equals(normalized)
                || "events".equals(normalized)
                || "vue".equals(normalized)
                || "fs".equals(normalized)
                || "path".equals(normalized)
                || "url".equals(normalized)
                || "util".equals(normalized)
                || "module".equals(normalized)
                || "os".equals(normalized)
                || "perf_hooks".equals(normalized)
                || "process".equals(normalized)
                || "stream".equals(normalized)
                || "string_decoder".equals(normalized)
                || "tty".equals(normalized)
                || "worker_threads".equals(normalized)
                || "zlib".equals(normalized)
                || "globalthis".equals(normalized);
    }

    public static boolean isViteVirtualModule(String specifier) {
        if (specifier == null || specifier.isBlank()) {
            return false;
        }
        if (specifier.startsWith("\0")) {
            return true;
        }
        String normalized = specifier.trim();
        return normalized.startsWith("virtual:");
    }

    public Path resolveModule(Path importerFile, String specifier) {
        if (importerFile == null || specifier == null || specifier.isBlank()) {
            return null;
        }
        if (isViteVirtualModule(specifier)) {
            return null;
        }
        if ("vue".equals(specifier.trim())) {
            if (isQinNpmHostModule(importerFile)) {
                return resolveOptionalBareModule(importerFile, specifier);
            }
            return null;
        }
        if (specifier.startsWith("java:") || specifier.startsWith("js:") || isHostRuntimeModule(specifier)
                || specifier.startsWith("http://") || specifier.startsWith("https://")) {
            return null;
        }
        if (specifier.startsWith("#")) {
            return resolvePackageImport(importerFile, specifier);
        }
        if (specifier.startsWith("./") || specifier.startsWith("../")) {
            return resolveRelativeModule(importerFile, specifier);
        }
        if (specifier.startsWith("/")) {
            return resolveAbsoluteModule(importerFile, specifier);
        }
        return resolveBareModule(importerFile, specifier);
    }

    private Path resolveOptionalBareModule(Path importerFile, String specifier) {
        BareSpecifier bare = parseBareSpecifier(specifier);
        Path search = importerFile.getParent();
        while (search != null) {
            Path packageDir = search.resolve("node_modules").resolve(bare.packageName());
            if (Files.isDirectory(packageDir)) {
                Path resolved = resolvePackageEntry(packageDir, bare.subPath());
                if (resolved != null) {
                    return resolved;
                }
                return null;
            }
            search = search.getParent();
        }
        return null;
    }

    private boolean isQinNpmHostModule(Path importerFile) {
        String path = importerFile.toAbsolutePath().normalize().toString().replace('\\', '/');
        return path.contains("/.qin/runtime/npm-host/");
    }

    public Path resolveRelativeModule(Path importerFile, String specifier) {
        if (importerFile == null || specifier == null || specifier.isBlank()) {
            return null;
        }
        if (!(specifier.startsWith("./") || specifier.startsWith("../"))) {
            return null;
        }

        Path parent = importerFile.getParent();
        if (parent == null) {
            throw new IllegalArgumentException("Importer has no parent directory: " + importerFile.toAbsolutePath());
        }
        Path resolved = parent.resolve(specifier).toAbsolutePath().normalize();
        Path file = resolveAsFile(resolved);
        if (file == null) {
            if (hasDeclarationOnlyModule(resolved)) {
                return null;
            }
            throw new IllegalArgumentException(
                    "Cannot resolve local module import \"" + specifier + "\" from " + importerFile.toAbsolutePath());
        }
        return file;
    }

    private Path resolveAbsoluteModule(Path importerFile, String specifier) {
        Path root = findProjectRoot(importerFile.getParent());
        Path target = root.resolve(specifier.substring(1)).toAbsolutePath().normalize();
        Path file = resolveAsFile(target);
        if (file == null) {
            if (hasDeclarationOnlyModule(target)) {
                return null;
            }
            throw new IllegalArgumentException(
                    "Cannot resolve absolute module import \"" + specifier + "\" from " + importerFile.toAbsolutePath());
        }
        return file;
    }

    private Path resolveBareModule(Path importerFile, String specifier) {
        BareSpecifier bare = parseBareSpecifier(specifier);
        Path selfReference = resolvePackageSelfReference(importerFile, bare);
        if (selfReference != null) {
            return selfReference;
        }
        Path resolved = resolveBareModuleFromSearch(importerFile.getParent(), bare, specifier, false);
        if (resolved != null) {
            return resolved;
        }
        try {
            Path realImporter = importerFile.toRealPath();
            if (!realImporter.equals(importerFile.toAbsolutePath().normalize())) {
                resolved = resolveBareModuleFromSearch(realImporter.getParent(), bare, specifier, false);
                if (resolved != null) {
                    return resolved;
                }
            }
        } catch (Exception ignored) {
            // Fall through to the stable error below.
        }
        throw new IllegalArgumentException(
                "Cannot resolve bare module import \"" + specifier + "\" from " + importerFile.toAbsolutePath());
    }

    private Path resolvePackageSelfReference(Path importerFile, BareSpecifier bare) {
        Path packageDir = findContainingPackageDir(importerFile);
        if (packageDir == null) {
            return null;
        }
        Path packageJson = packageDir.resolve("package.json");
        try {
            String packageName = readPackageName(packageJson);
            if (!bare.packageName().equals(packageName)) {
                return null;
            }
            return resolvePackageEntry(packageDir, bare.subPath());
        } catch (Exception error) {
            throw new IllegalArgumentException("Failed to parse package.json: " + packageJson.toAbsolutePath(), error);
        }
    }

    private Path resolveBareModuleFromSearch(
            Path search,
            BareSpecifier bare,
            String specifier,
            boolean optional) {
        while (search != null) {
            Path qinNpmHostPackageDir = search.resolve(".qin")
                    .resolve("runtime")
                    .resolve("npm-host")
                    .resolve("node_modules")
                    .resolve(bare.packageName());
            if (Files.isDirectory(qinNpmHostPackageDir)) {
                Path resolved = resolvePackageEntry(qinNpmHostPackageDir, bare.subPath());
                if (resolved != null) {
                    return resolved;
                }
                if (optional) {
                    return null;
                }
                throw new IllegalArgumentException(
                        "Cannot resolve bare module import \"" + specifier + "\" from Qin npm host package "
                                + qinNpmHostPackageDir.toAbsolutePath());
            }
            Path packageDir = search.resolve("node_modules").resolve(bare.packageName());
            if (Files.isDirectory(packageDir)) {
                Path resolved = resolvePackageEntry(packageDir, bare.subPath());
                if (resolved != null) {
                    return resolved;
                }
                if (optional) {
                    return null;
                }
                throw new IllegalArgumentException(
                        "Cannot resolve bare module import \"" + specifier + "\" from package "
                                + packageDir.toAbsolutePath());
            }
            search = search.getParent();
        }
        return null;
    }

    private Path resolvePackageImport(Path importerFile, String specifier) {
        Path packageDir = findContainingPackageDir(importerFile);
        if (packageDir == null) {
            throw new IllegalArgumentException(
                    "Cannot resolve package import \"" + specifier + "\" from " + importerFile.toAbsolutePath());
        }
        Path packageJson = packageDir.resolve("package.json");
        try {
            String json = Files.readString(packageJson);
            String entry = readPackageImportsEntry(json, specifier);
            if (entry == null || entry.isBlank()) {
                throw new IllegalArgumentException(
                        "Cannot resolve package import \"" + specifier + "\" from package "
                                + packageDir.toAbsolutePath());
            }
            if (entry.startsWith("./")) {
                Path resolved = resolveAsFile(packageDir.resolve(entry));
                if (resolved != null) {
                    return resolved;
                }
            }
            if (!entry.startsWith(".") && !entry.startsWith("/")) {
                return resolveBareModule(packageJson, entry);
            }
            throw new IllegalArgumentException(
                    "Cannot resolve package import \"" + specifier + "\" target \"" + entry
                            + "\" from package " + packageDir.toAbsolutePath());
        } catch (IllegalArgumentException error) {
            throw error;
        } catch (Exception error) {
            throw new IllegalArgumentException("Failed to parse package.json: " + packageJson.toAbsolutePath(), error);
        }
    }

    private Path findContainingPackageDir(Path importerFile) {
        Path current = importerFile == null ? null : importerFile.toAbsolutePath().normalize();
        if (current != null && !Files.isDirectory(current)) {
            current = current.getParent();
        }
        while (current != null) {
            if (Files.isRegularFile(current.resolve("package.json"))) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    private Path resolvePackageEntry(Path packageDir, String subPath) {
        Path packageJson = packageDir.resolve("package.json");
        if (subPath != null && !subPath.isBlank()) {
            if (Files.isRegularFile(packageJson)) {
                try {
                    String json = Files.readString(packageJson);
                    String entry = readExportsSubpathEntry(json, subPath);
                    if (entry != null && !entry.isBlank()) {
                        Path resolved = resolveAsFile(packageDir.resolve(entry));
                        if (resolved != null) {
                            return resolved;
                        }
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Failed to parse package.json: " + packageJson.toAbsolutePath(), e);
                }
            }
            Path target = packageDir.resolve(subPath);
            return resolveAsFile(target);
        }

        if (Files.isRegularFile(packageJson)) {
            try {
                String json = Files.readString(packageJson);
                String entry = readPackageSpecifierMapEntry(json, ".");
                if (entry == null) {
                    entry = readExportsImport(json);
                }
                if (entry == null) {
                    entry = readExportsImportDefaultFallbackDot(json);
                }
                if (entry == null) {
                    entry = readExportsNodeImportDefaultDot(json);
                }
                if (entry == null) {
                    entry = readExportsDefault(json);
                }
                if (entry == null) {
                    entry = readExportsDotString(json);
                }
                if (entry == null) {
                    entry = readExportsImportRoot(json);
                }
                if (entry == null) {
                    entry = readExportsImportDefaultFallbackRoot(json);
                }
                if (entry == null) {
                    entry = readExportsNodeImportDefaultRoot(json);
                }
                if (entry == null) {
                    entry = readExportsDefaultRoot(json);
                }
                if (entry == null) {
                    entry = readExportsStringRoot(json);
                }
                if (entry == null) {
                    entry = readField(json, "module");
                }
                if (entry == null) {
                    Path esmIndex = resolveAsFile(packageDir.resolve("esm/index.js"));
                    if (esmIndex != null) {
                        return esmIndex;
                    }
                }
                if (entry == null) {
                    entry = readField(json, "main");
                }
                if (entry != null && !entry.isBlank()) {
                    Path resolved = resolveAsFile(packageDir.resolve(entry));
                    if (resolved != null) {
                        return resolved;
                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to parse package.json: " + packageJson.toAbsolutePath(), e);
            }
        }

        return resolveAsFile(packageDir.resolve("index.js"));
    }

    private String readField(String json, String field) {
        Matcher matcher = Pattern.compile(String.format(JSON_STRING_FIELD.pattern(), Pattern.quote(field)))
                .matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String readPackageName(Path packageJson) throws java.io.IOException {
        if (!Files.isRegularFile(packageJson)) {
            return null;
        }
        return readField(Files.readString(packageJson), "name");
    }

    private String readExportsImport(String json) {
        Matcher matcher = EXPORTS_IMPORT_DOT.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String readExportsNodeImportDefaultDot(String json) {
        Matcher matcher = EXPORTS_NODE_IMPORT_DEFAULT_DOT.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String readExportsNodeImportDefaultRoot(String json) {
        Matcher matcher = EXPORTS_NODE_IMPORT_DEFAULT_ROOT.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String readExportsImportDefaultFallbackDot(String json) {
        Matcher matcher = EXPORTS_IMPORT_DEFAULT_FALLBACK_DOT.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String readExportsImportDefaultFallbackRoot(String json) {
        Matcher matcher = EXPORTS_IMPORT_DEFAULT_FALLBACK_ROOT.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String readExportsDefault(String json) {
        Matcher matcher = EXPORTS_DEFAULT_DOT.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String readExportsDotString(String json) {
        Matcher matcher = EXPORTS_DOT_STRING.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String readExportsImportRoot(String json) {
        Matcher matcher = EXPORTS_IMPORT_ROOT.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String readExportsDefaultRoot(String json) {
        Matcher matcher = EXPORTS_DEFAULT_ROOT.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String readExportsStringRoot(String json) {
        Matcher matcher = EXPORTS_STRING_ROOT.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String readExportsSubpathEntry(String json, String subPath) {
        String normalizedSubpath = subPath.replace('\\', '/');
        if (!normalizedSubpath.startsWith("./")) {
            normalizedSubpath = "./" + normalizedSubpath;
        }
        return readPackageSpecifierMapEntry(json, normalizedSubpath);
    }

    private String readPackageImportsEntry(String json, String specifier) {
        return readPackageSpecifierMapEntry(json, specifier);
    }

    private String readPackageSpecifierMapEntry(String json, String specifier) {
        String key = "\"" + specifier + "\"";
        int keyIndex = json.indexOf(key);
        if (keyIndex < 0) {
            return null;
        }
        int colonIndex = json.indexOf(':', keyIndex + key.length());
        if (colonIndex < 0) {
            return null;
        }
        int valueStart = skipWhitespace(json, colonIndex + 1);
        if (valueStart >= json.length()) {
            return null;
        }
        char valueStartChar = json.charAt(valueStart);
        if (valueStartChar == '"') {
            return readJsonStringAt(json, valueStart);
        }
        if (valueStartChar != '{') {
            return null;
        }
        int valueEnd = findMatchingBrace(json, valueStart);
        if (valueEnd < 0) {
            return null;
        }
        String exportObject = json.substring(valueStart, valueEnd + 1);
        String entry = readTopLevelStringField(exportObject, "import");
        if (entry == null) {
            String importObject = readJsonObjectField(exportObject, "import");
            if (importObject != null) {
                entry = readConditionalObjectEntry(importObject);
            }
        }
        if (entry == null) {
            entry = readExportsImportDefaultFallbackRoot(exportObject);
        }
        if (entry == null) {
            entry = readExportsNodeImportDefaultRoot(exportObject);
        }
        if (entry == null) {
            entry = readTopLevelStringField(exportObject, "default");
        }
        return entry;
    }

    private String readConditionalObjectEntry(String jsonObject) {
        String entry = readTopLevelStringField(jsonObject, "default");
        if (entry != null) {
            return entry;
        }
        String nodeObject = readJsonObjectField(jsonObject, "node");
        if (nodeObject != null) {
            entry = readTopLevelStringField(nodeObject, "default");
            if (entry != null) {
                return entry;
            }
        }
        return readTopLevelStringField(jsonObject, "node");
    }

    private String readTopLevelStringField(String jsonObject, String field) {
        int colonIndex = findTopLevelFieldColon(jsonObject, field);
        if (colonIndex < 0) {
            return null;
        }
        int valueStart = skipWhitespace(jsonObject, colonIndex + 1);
        if (valueStart >= jsonObject.length() || jsonObject.charAt(valueStart) != '"') {
            return null;
        }
        return readJsonStringAt(jsonObject, valueStart);
    }

    private String readJsonObjectField(String jsonObject, String field) {
        int colonIndex = findTopLevelFieldColon(jsonObject, field);
        if (colonIndex < 0) {
            return null;
        }
        int valueStart = skipWhitespace(jsonObject, colonIndex + 1);
        if (valueStart >= jsonObject.length() || jsonObject.charAt(valueStart) != '{') {
            return null;
        }
        int valueEnd = findMatchingBrace(jsonObject, valueStart);
        if (valueEnd < 0) {
            return null;
        }
        return jsonObject.substring(valueStart, valueEnd + 1);
    }

    private int findTopLevelFieldColon(String jsonObject, String field) {
        String key = "\"" + field + "\"";
        int depth = 0;
        boolean inString = false;
        boolean escaping = false;
        for (int cursor = 0; cursor < jsonObject.length(); cursor++) {
            char ch = jsonObject.charAt(cursor);
            if (inString) {
                if (escaping) {
                    escaping = false;
                } else if (ch == '\\') {
                    escaping = true;
                } else if (ch == '"') {
                    inString = false;
                }
                continue;
            }
            if (ch == '"') {
                if (depth == 1 && jsonObject.startsWith(key, cursor)) {
                    int colonIndex = jsonObject.indexOf(':', cursor + key.length());
                    if (colonIndex >= 0) {
                        return colonIndex;
                    }
                }
                inString = true;
            } else if (ch == '{') {
                depth++;
            } else if (ch == '}') {
                depth--;
            }
        }
        return -1;
    }

    private int skipWhitespace(String text, int index) {
        int cursor = index;
        while (cursor < text.length() && Character.isWhitespace(text.charAt(cursor))) {
            cursor++;
        }
        return cursor;
    }

    private String readJsonStringAt(String text, int quoteIndex) {
        if (quoteIndex < 0 || quoteIndex >= text.length() || text.charAt(quoteIndex) != '"') {
            return null;
        }
        StringBuilder value = new StringBuilder();
        boolean escaping = false;
        for (int cursor = quoteIndex + 1; cursor < text.length(); cursor++) {
            char ch = text.charAt(cursor);
            if (escaping) {
                value.append(ch);
                escaping = false;
                continue;
            }
            if (ch == '\\') {
                escaping = true;
                continue;
            }
            if (ch == '"') {
                return value.toString();
            }
            value.append(ch);
        }
        return null;
    }

    private int findMatchingBrace(String text, int openIndex) {
        if (openIndex < 0 || openIndex >= text.length() || text.charAt(openIndex) != '{') {
            return -1;
        }
        int depth = 0;
        boolean inString = false;
        boolean escaping = false;
        for (int cursor = openIndex; cursor < text.length(); cursor++) {
            char ch = text.charAt(cursor);
            if (inString) {
                if (escaping) {
                    escaping = false;
                } else if (ch == '\\') {
                    escaping = true;
                } else if (ch == '"') {
                    inString = false;
                }
                continue;
            }
            if (ch == '"') {
                inString = true;
            } else if (ch == '{') {
                depth++;
            } else if (ch == '}') {
                depth--;
                if (depth == 0) {
                    return cursor;
                }
            }
        }
        return -1;
    }

    private BareSpecifier parseBareSpecifier(String specifier) {
        String trimmed = specifier.trim();
        if (trimmed.startsWith("@")) {
            int firstSlash = trimmed.indexOf('/');
            int secondSlash = firstSlash < 0 ? -1 : trimmed.indexOf('/', firstSlash + 1);
            if (firstSlash < 0) {
                throw new IllegalArgumentException("Invalid scoped package specifier: " + specifier);
            }
            if (secondSlash < 0) {
                return new BareSpecifier(trimmed, "");
            }
            return new BareSpecifier(trimmed.substring(0, secondSlash), trimmed.substring(secondSlash + 1));
        }

        int slash = trimmed.indexOf('/');
        if (slash < 0) {
            return new BareSpecifier(trimmed, "");
        }
        return new BareSpecifier(trimmed.substring(0, slash), trimmed.substring(slash + 1));
    }

    private Path findProjectRoot(Path start) {
        Path current = start == null ? Path.of("").toAbsolutePath().normalize() : start.toAbsolutePath().normalize();
        Path last = current;
        while (current != null) {
            last = current;
            current = current.getParent();
        }
        return last;
    }

    private Path resolveAsFile(Path path) {
        if (Files.isRegularFile(path) && isSupportedScriptFile(path)) {
            return path.toAbsolutePath().normalize();
        }

        Path appendedExtensionFallback = resolveByAppendingSourceExtension(path);
        if (appendedExtensionFallback != null) {
            return appendedExtensionFallback;
        }

        Path sourceExtensionFallback = resolveSourceExtensionFallback(path);
        if (sourceExtensionFallback != null) {
            return sourceExtensionFallback;
        }

        if (Files.isDirectory(path)) {
            Path indexJs = path.resolve("index.js");
            if (Files.isRegularFile(indexJs)) {
                return indexJs.toAbsolutePath().normalize();
            }
            Path indexQin = path.resolve("index.qin");
            if (Files.isRegularFile(indexQin)) {
                return indexQin.toAbsolutePath().normalize();
            }
            Path indexVue = path.resolve("index.vue");
            if (Files.isRegularFile(indexVue)) {
                return indexVue.toAbsolutePath().normalize();
            }
            Path indexOvs = path.resolve("index.ovs");
            if (Files.isRegularFile(indexOvs)) {
                return indexOvs.toAbsolutePath().normalize();
            }
            Path indexMjs = path.resolve("index.mjs");
            if (Files.isRegularFile(indexMjs)) {
                return indexMjs.toAbsolutePath().normalize();
            }
            Path indexTs = path.resolve("index.ts");
            if (Files.isRegularFile(indexTs)) {
                return indexTs.toAbsolutePath().normalize();
            }
        }
        return null;
    }

    private Path resolveByAppendingSourceExtension(Path path) {
        String fileName = path.getFileName() == null ? "" : path.getFileName().toString();
        if (fileName.isBlank() || hasSupportedScriptExtension(fileName)) {
            return null;
        }
        String[] extensions = {".js", ".qin", ".vue", ".ovs", ".cssts", ".mjs", ".ts", ".css", ".svg", ".png", ".jpg", ".jpeg", ".gif", ".webp", ".ico", ".avif"};
        for (String extension : extensions) {
            Path candidate = path.resolveSibling(fileName + extension);
            if (Files.isRegularFile(candidate)) {
                return candidate.toAbsolutePath().normalize();
            }
        }
        return null;
    }

    private Path resolveSourceExtensionFallback(Path path) {
        String fileName = path.getFileName() == null ? "" : path.getFileName().toString();
        if (!hasSupportedScriptExtension(fileName)) {
            return null;
        }
        int extensionIndex = fileName.lastIndexOf('.');
        if (extensionIndex <= 0) {
            return null;
        }

        String baseName = fileName.substring(0, extensionIndex);
        Path sibling = path.resolveSibling(baseName);
        Path ts = sibling.resolveSibling(baseName + ".ts");
        if (Files.isRegularFile(ts)) {
            return ts.toAbsolutePath().normalize();
        }
        Path qin = sibling.resolveSibling(baseName + ".qin");
        if (Files.isRegularFile(qin)) {
            return qin.toAbsolutePath().normalize();
        }
        Path js = sibling.resolveSibling(baseName + ".js");
        if (Files.isRegularFile(js)) {
            return js.toAbsolutePath().normalize();
        }
        Path mjs = sibling.resolveSibling(baseName + ".mjs");
        if (Files.isRegularFile(mjs)) {
            return mjs.toAbsolutePath().normalize();
        }
        Path vue = sibling.resolveSibling(baseName + ".vue");
        if (Files.isRegularFile(vue)) {
            return vue.toAbsolutePath().normalize();
        }
        Path ovs = sibling.resolveSibling(baseName + ".ovs");
        if (Files.isRegularFile(ovs)) {
            return ovs.toAbsolutePath().normalize();
        }
        Path cssts = sibling.resolveSibling(baseName + ".cssts");
        if (Files.isRegularFile(cssts)) {
            return cssts.toAbsolutePath().normalize();
        }
        return null;
    }

    private boolean hasDeclarationOnlyModule(Path path) {
        if (path == null) {
            return false;
        }
        String fileName = path.getFileName() == null ? "" : path.getFileName().toString();
        if (Files.isRegularFile(path) && fileName.endsWith(".d.ts")) {
            return true;
        }
        if (!fileName.contains(".")) {
            Path dts = path.resolveSibling(fileName + ".d.ts");
            if (Files.isRegularFile(dts)) {
                return true;
            }
        }
        int extensionIndex = fileName.lastIndexOf('.');
        if (extensionIndex > 0) {
            String baseName = fileName.substring(0, extensionIndex);
            Path dts = path.resolveSibling(baseName + ".d.ts");
            if (Files.isRegularFile(dts)) {
                return true;
            }
        }
        if (Files.isDirectory(path) && Files.isRegularFile(path.resolve("index.d.ts"))) {
            return true;
        }
        return false;
    }

    private boolean isSupportedScriptFile(Path path) {
        String fileName = path.getFileName() == null ? "" : path.getFileName().toString().toLowerCase();
        return hasSupportedScriptExtension(fileName);
    }

    private boolean hasSupportedScriptExtension(String fileName) {
        fileName = fileName == null ? "" : fileName.toLowerCase();
        return fileName.endsWith(".js")
                || fileName.endsWith(".mjs")
                || fileName.endsWith(".ts")
                || fileName.endsWith(".qin")
                || fileName.endsWith(".vue")
                || fileName.endsWith(".ovs")
                || fileName.endsWith(".cssts")
                || fileName.endsWith(".css")
                || fileName.endsWith(".svg")
                || fileName.endsWith(".png")
                || fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")
                || fileName.endsWith(".gif")
                || fileName.endsWith(".webp")
                || fileName.endsWith(".ico")
                || fileName.endsWith(".avif");
    }

    private record BareSpecifier(String packageName, String subPath) {
    }
}
