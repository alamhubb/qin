package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinEsmSpecifierResolver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Locates the official `@vue/compiler-sfc` package entry that Qin should
 * eventually execute through its own package/module pipeline.
 */
final class QinVueCompilerSfcPackageLocator {
    private static final String PACKAGE_NAME = "@vue/compiler-sfc";

    private final QinEsmSpecifierResolver specifierResolver = new QinEsmSpecifierResolver();

    QinVueCompilerSfcPackageLocation locate(Path importerFile) {
        Objects.requireNonNull(importerFile, "importerFile cannot be null");
        Path entry;
        try {
            entry = specifierResolver.resolveModule(importerFile.toAbsolutePath().normalize(), PACKAGE_NAME);
        } catch (IllegalArgumentException ignored) {
            return QinVueCompilerSfcPackageLocation.notFound(PACKAGE_NAME);
        }
        if (entry == null) {
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
