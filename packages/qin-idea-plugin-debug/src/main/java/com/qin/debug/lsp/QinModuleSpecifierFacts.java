package com.qin.debug.lsp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class QinModuleSpecifierFacts {
    private static final String JAVA_PREFIX = "java:";

    private QinModuleSpecifierFacts() {
    }

    static @Nullable String javaModuleName(@NotNull String moduleSpecifier) {
        return moduleSpecifier.startsWith(JAVA_PREFIX)
                ? moduleSpecifier.substring(JAVA_PREFIX.length()).trim()
                : null;
    }

    static boolean isQinModuleSpecifier(@NotNull String moduleSpecifier) {
        String normalized = normalizePathSeparators(moduleSpecifier);
        return !normalized.startsWith(JAVA_PREFIX)
                && (normalized.startsWith("./") || normalized.startsWith("../"))
                && (normalized.endsWith(".qin") || !normalized.endsWith("/"));
    }

    static @NotNull String normalizePathSeparators(@NotNull String moduleSpecifier) {
        return moduleSpecifier.replace('\\', '/');
    }
}
