package com.qin.lang.module.policy;

/**
 * Canonical import kind for module policy checks.
 */
public enum QinImportKind {
    JAVA,
    JS,
    LOCAL,
    VIRTUAL,
    UNKNOWN;

    public static QinImportKind fromSpecifier(String specifier) {
        if (specifier == null || specifier.isBlank()) {
            return UNKNOWN;
        }
        if (specifier.startsWith("\0")) {
            return VIRTUAL;
        }
        String normalized = specifier.trim();
        if (normalized.startsWith("virtual:")) {
            return VIRTUAL;
        }
        boolean isScriptExt = normalized.endsWith(".js")
                || normalized.endsWith(".mjs")
                || normalized.endsWith(".ts")
                || normalized.endsWith(".qin");
        boolean isRelative = normalized.startsWith("./") || normalized.startsWith("../");
        if (isRelative && isScriptExt) {
            return LOCAL;
        }
        if (normalized.startsWith("java:")) {
            return JAVA;
        }
        if (normalized.startsWith("js:")
                || isScriptExt) {
            return JS;
        }
        // Treat non-java/non-qin imports as JS side for policy purpose.
        return JS;
    }
}
