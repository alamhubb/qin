package com.qin.lang.runtime;

import java.io.PrintStream;

/**
 * Minimal console builtin for JVM-emitted ESM stage-1 programs.
 */
public final class JavaEsmConsole {
    private JavaEsmConsole() {
    }

    public static void log(Object value) {
        write(System.out, new Object[] {value});
    }

    public static void log(Object... values) {
        write(System.out, values);
    }

    public static void debug(Object value) {
        write(System.out, new Object[] {value});
    }

    public static void debug(Object... values) {
        write(System.out, values);
    }

    public static void info(Object value) {
        write(System.out, new Object[] {value});
    }

    public static void info(Object... values) {
        write(System.out, values);
    }

    public static void warn(Object value) {
        write(System.err, new Object[] {value});
    }

    public static void warn(Object... values) {
        write(System.err, values);
    }

    public static void error(Object value) {
        write(System.err, new Object[] {value});
    }

    public static void error(Object... values) {
        write(System.err, values);
    }

    private static void write(PrintStream stream, Object[] values) {
        if (values == null || values.length == 0) {
            stream.println();
            return;
        }
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                text.append(' ');
            }
            text.append(format(values[i]));
        }
        stream.println(text);
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
