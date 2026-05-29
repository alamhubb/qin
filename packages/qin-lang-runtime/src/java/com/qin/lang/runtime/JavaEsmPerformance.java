package com.qin.lang.runtime;

/**
 * Java-backed Performance API subset for Qin.
 */
public final class JavaEsmPerformance {
    private static final long START_NANOS = System.nanoTime();

    private JavaEsmPerformance() {
    }

    public static Object now() {
        return (System.nanoTime() - START_NANOS) / 1_000_000.0d;
    }
}
