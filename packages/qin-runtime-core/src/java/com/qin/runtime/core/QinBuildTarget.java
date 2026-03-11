package com.qin.runtime.core;

/**
 * Build target for Qin runtime compilation.
 */
public enum QinBuildTarget {
    JVM,
    JS,
    BOTH;

    public boolean emitJvm() {
        return this == JVM || this == BOTH;
    }

    public boolean emitJs() {
        return this == JS || this == BOTH;
    }

    public static QinBuildTarget parse(String raw) {
        return switch (raw.toLowerCase()) {
            case "jvm" -> JVM;
            case "js" -> JS;
            case "both" -> BOTH;
            default -> throw new IllegalArgumentException("Unknown --target value: " + raw);
        };
    }
}
