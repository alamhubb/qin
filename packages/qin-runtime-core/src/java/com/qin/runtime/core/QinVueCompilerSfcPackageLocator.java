package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinEsmSpecifierResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Locates the official `@vue/compiler-sfc` package entry that Qin should
 * eventually execute through its own package/module pipeline.
 */
final class QinVueCompilerSfcPackageLocator {
    private static final String PACKAGE_NAME = "@vue/compiler-sfc";
    private static final Pattern JSON_STRING_FIELD = Pattern.compile("\"%s\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern EXPORTS_IMPORT_DOT = Pattern.compile(
            "\"exports\"\\s*:\\s*\\{[^}]*\"\\.\"\\s*:\\s*\\{[^}]*\"import\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.DOTALL);
    private static final Pattern EXPORTS_DEFAULT_DOT = Pattern.compile(
            "\"exports\"\\s*:\\s*\\{[^}]*\"\\.\"\\s*:\\s*\\{[^}]*\"default\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.DOTALL);
    private static final Pattern EXPORTS_DOT_STRING = Pattern.compile(
            "\"exports\"\\s*:\\s*\\{[^}]*\"\\.\"\\s*:\\s*\"([^\"]+)\"",
            Pattern.DOTALL);

    private final QinEsmSpecifierResolver specifierResolver = new QinEsmSpecifierResolver();

    QinVueCompilerSfcPackageLocation locate(Path importerFile) {
        Objects.requireNonNull(importerFile, "importerFile cannot be null");
        QinVueCompilerSfcPackageLocation directLocation = locateOfficialNodeModulesPackage(importerFile);
        if (directLocation.found()) {
            return directLocation;
        }

        Path entry;
        try {
            entry = specifierResolver.resolveModule(importerFile.toAbsolutePath().normalize(), PACKAGE_NAME);
        } catch (IllegalArgumentException ignored) {
            return QinVueCompilerSfcPackageLocation.notFound(PACKAGE_NAME);
        }
        if (entry == null) {
            return QinVueCompilerSfcPackageLocation.notFound(PACKAGE_NAME);
        }
        if (isQinCompilerSfcShim(entry)) {
            return QinVueCompilerSfcPackageLocation.notFound(PACKAGE_NAME);
        }

        Path packageRoot = findPackageRoot(entry);
        Path packageJson = packageRoot == null ? null : packageRoot.resolve("package.json");
        return new QinVueCompilerSfcPackageLocation(
                PACKAGE_NAME,
                entry,
                packageRoot,
                packageJson,
                true);
    }

    private QinVueCompilerSfcPackageLocation locateOfficialNodeModulesPackage(Path importerFile) {
        Path search = importerFile.toAbsolutePath().normalize().getParent();
        while (search != null) {
            Path packageRoot = search.resolve("node_modules")
                    .resolve("@vue")
                    .resolve("compiler-sfc")
                    .normalize();
            Path entry = resolveOfficialPackageEntry(packageRoot);
            if (entry != null) {
                return new QinVueCompilerSfcPackageLocation(
                        PACKAGE_NAME,
                        entry,
                        packageRoot,
                        packageRoot.resolve("package.json"),
                        true);
            }
            search = search.getParent();
        }
        return QinVueCompilerSfcPackageLocation.notFound(PACKAGE_NAME);
    }

    private Path resolveOfficialPackageEntry(Path packageRoot) {
        if (!Files.isDirectory(packageRoot) || isQinCompilerSfcShim(packageRoot)) {
            return null;
        }
        Path packageJson = packageRoot.resolve("package.json");
        if (!Files.isRegularFile(packageJson)) {
            return null;
        }
        try {
            String json = Files.readString(packageJson, StandardCharsets.UTF_8);
            if (json.contains("\"version\"\\s*:\\s*\"0.0.0-qin-shim\"")
                    || json.contains("\"0.0.0-qin-shim\"")) {
                return null;
            }
            String entry = readExportsImport(json);
            if (entry == null) {
                entry = readExportsDefault(json);
            }
            if (entry == null) {
                entry = readExportsDotString(json);
            }
            if (entry == null) {
                entry = readField(json, "module");
            }
            if (entry == null) {
                entry = readField(json, "main");
            }
            if (entry != null && !entry.isBlank()) {
                Path resolved = resolveAsFile(packageRoot.resolve(entry));
                if (resolved != null) {
                    return resolved;
                }
            }
            return resolveAsFile(packageRoot.resolve("index.js"));
        } catch (IOException error) {
            throw new IllegalArgumentException("Failed to parse package.json: " + packageJson.toAbsolutePath(), error);
        }
    }

    private Path resolveAsFile(Path path) {
        Path normalized = path.toAbsolutePath().normalize();
        if (Files.isRegularFile(normalized)) {
            return normalized;
        }
        String fileName = normalized.getFileName() == null ? "" : normalized.getFileName().toString();
        if (!fileName.endsWith(".js") && !fileName.endsWith(".mjs") && !fileName.endsWith(".cjs")) {
            Path js = normalized.resolveSibling(fileName + ".js");
            if (Files.isRegularFile(js)) {
                return js.toAbsolutePath().normalize();
            }
            Path mjs = normalized.resolveSibling(fileName + ".mjs");
            if (Files.isRegularFile(mjs)) {
                return mjs.toAbsolutePath().normalize();
            }
        }
        return null;
    }

    private boolean isQinCompilerSfcShim(Path path) {
        String normalized = path.toAbsolutePath().normalize().toString().replace('\\', '/');
        return normalized.contains("/.qin/runtime/npm-host/node_modules/@vue/compiler-sfc/");
    }

    private String readField(String json, String field) {
        Matcher matcher = Pattern.compile(String.format(JSON_STRING_FIELD.pattern(), Pattern.quote(field)))
                .matcher(json);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String readExportsImport(String json) {
        Matcher matcher = EXPORTS_IMPORT_DOT.matcher(json);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String readExportsDefault(String json) {
        Matcher matcher = EXPORTS_DEFAULT_DOT.matcher(json);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String readExportsDotString(String json) {
        Matcher matcher = EXPORTS_DOT_STRING.matcher(json);
        return matcher.find() ? matcher.group(1) : null;
    }

    private Path findPackageRoot(Path entry) {
        Path current = entry == null ? null : entry.toAbsolutePath().normalize().getParent();
        while (current != null) {
            if (Files.exists(current.resolve("package.json"))) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    record QinVueCompilerSfcPackageLocation(
            String packageName,
            Path entryFile,
            Path packageRoot,
            Path packageJson,
            boolean found) {
        static QinVueCompilerSfcPackageLocation notFound(String packageName) {
            return new QinVueCompilerSfcPackageLocation(packageName, null, null, null, false);
        }
    }
}
