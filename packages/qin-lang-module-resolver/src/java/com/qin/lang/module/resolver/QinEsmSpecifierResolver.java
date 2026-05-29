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
                || "fs".equals(normalized)
                || "path".equals(normalized)
                || "url".equals(normalized)
                || "util".equals(normalized)
                || "process".equals(normalized)
                || "globalthis".equals(normalized);
    }

    public Path resolveModule(Path importerFile, String specifier) {
        if (importerFile == null || specifier == null || specifier.isBlank()) {
            return null;
        }
        if (specifier.startsWith("java:") || specifier.startsWith("js:") || isHostRuntimeModule(specifier)
                || specifier.startsWith("http://") || specifier.startsWith("https://")) {
            return null;
        }
        if (specifier.startsWith("./") || specifier.startsWith("../")) {
            return resolveRelativeModule(importerFile, specifier);
        }
        if (specifier.startsWith("/")) {
            return resolveAbsoluteModule(importerFile, specifier);
        }
        return resolveBareModule(importerFile, specifier);
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
        Path search = importerFile.getParent();
        while (search != null) {
            Path packageDir = search.resolve("node_modules").resolve(bare.packageName());
            if (Files.isDirectory(packageDir)) {
                Path resolved = resolvePackageEntry(packageDir, bare.subPath());
                if (resolved != null) {
                    return resolved;
                }
                throw new IllegalArgumentException(
                        "Cannot resolve bare module import \"" + specifier + "\" from package "
                                + packageDir.toAbsolutePath());
            }
            search = search.getParent();
        }
        throw new IllegalArgumentException(
                "Cannot resolve bare module import \"" + specifier + "\" from " + importerFile.toAbsolutePath());
    }

    private Path resolvePackageEntry(Path packageDir, String subPath) {
        Path packageJson = packageDir.resolve("package.json");
        if (subPath != null && !subPath.isBlank()) {
            Path target = packageDir.resolve(subPath);
            return resolveAsFile(target);
        }

        if (Files.isRegularFile(packageJson)) {
            try {
                String json = Files.readString(packageJson);
                String entry = readExportsImport(json);
                if (entry == null) {
                    entry = readExportsNodeImportDefaultDot(json);
                }
                if (entry == null) {
                    entry = readExportsImportDefaultFallbackDot(json);
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
                    entry = readExportsNodeImportDefaultRoot(json);
                }
                if (entry == null) {
                    entry = readExportsImportDefaultFallbackRoot(json);
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

        Path sourceExtensionFallback = resolveSourceExtensionFallback(path);
        if (sourceExtensionFallback != null) {
            return sourceExtensionFallback;
        }

        String fileName = path.getFileName() == null ? "" : path.getFileName().toString();
        if (!fileName.contains(".")) {
            Path js = path.resolveSibling(fileName + ".js");
            if (Files.isRegularFile(js)) {
                return js.toAbsolutePath().normalize();
            }
            Path qin = path.resolveSibling(fileName + ".qin");
            if (Files.isRegularFile(qin)) {
                return qin.toAbsolutePath().normalize();
            }
            Path vue = path.resolveSibling(fileName + ".vue");
            if (Files.isRegularFile(vue)) {
                return vue.toAbsolutePath().normalize();
            }
            Path mjs = path.resolveSibling(fileName + ".mjs");
            if (Files.isRegularFile(mjs)) {
                return mjs.toAbsolutePath().normalize();
            }
            Path ts = path.resolveSibling(fileName + ".ts");
            if (Files.isRegularFile(ts)) {
                return ts.toAbsolutePath().normalize();
            }
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

    private Path resolveSourceExtensionFallback(Path path) {
        String fileName = path.getFileName() == null ? "" : path.getFileName().toString();
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
        return fileName.endsWith(".js")
                || fileName.endsWith(".mjs")
                || fileName.endsWith(".ts")
                || fileName.endsWith(".qin")
                || fileName.endsWith(".vue");
    }

    private record BareSpecifier(String packageName, String subPath) {
    }
}
