package com.qin.lang.runtime;

/**
 * Minimal console builtin for JVM-emitted ESM stage-1 programs.
 */
public final class JavaEsmConsole {
    private JavaEsmConsole() {
    }

    public static void log(Object value) {
        System.out.println(format(value));
    }

    public static void debug(Object value) {
        System.out.println(format(value));
    }

    public static void info(Object value) {
        System.out.println(format(value));
    }

    public static void warn(Object value) {
        System.err.println(format(value));
    }

    public static void error(Object value) {
        System.err.println(format(value));
    }

    private static Object format(Object value) {
        if (value instanceof Double number
                && Double.isFinite(number)
                && number.doubleValue() == Math.rint(number.doubleValue())) {
            long whole = number.longValue();
            if (whole >= Integer.MIN_VALUE && whole <= Integer.MAX_VALUE) {
                return (int) whole;
            }
            return whole;
        }
        return value;
    }
}
