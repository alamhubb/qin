package com.qin.lang.runtime;

/**
 * Java-backed Math builtin subset for Qin.
 */
public final class JavaEsmMath {
    private JavaEsmMath() {
    }

    public static Object random() {
        return Math.random();
    }

    public static Object abs(Object value) {
        return Math.abs(requireNumber(value, "Math.abs"));
    }

    public static Object floor(Object value) {
        return Math.floor(requireNumber(value, "Math.floor"));
    }

    public static Object ceil(Object value) {
        return Math.ceil(requireNumber(value, "Math.ceil"));
    }

    public static Object max(Object left, Object right) {
        return Math.max(requireNumber(left, "Math.max"), requireNumber(right, "Math.max"));
    }

    public static Object max(Object... values) {
        double result = Double.NEGATIVE_INFINITY;
        for (Object value : values) {
            result = Math.max(result, requireNumber(value, "Math.max"));
        }
        return result;
    }

    public static Object min(Object left, Object right) {
        return Math.min(requireNumber(left, "Math.min"), requireNumber(right, "Math.min"));
    }

    public static Object min(Object... values) {
        double result = Double.POSITIVE_INFINITY;
        for (Object value : values) {
            result = Math.min(result, requireNumber(value, "Math.min"));
        }
        return result;
    }

    public static Object round(Object value) {
        return (double) Math.round(requireNumber(value, "Math.round"));
    }

    public static Object trunc(Object value) {
        double number = requireNumber(value, "Math.trunc");
        return number < 0 ? Math.ceil(number) : Math.floor(number);
    }

    public static Object pow(Object base, Object exponent) {
        return Math.pow(requireNumber(base, "Math.pow"), requireNumber(exponent, "Math.pow"));
    }

    public static Object sqrt(Object value) {
        return Math.sqrt(requireNumber(value, "Math.sqrt"));
    }

    public static Object sin(Object value) {
        return Math.sin(requireNumber(value, "Math.sin"));
    }

    public static Object cos(Object value) {
        return Math.cos(requireNumber(value, "Math.cos"));
    }

    public static Object tan(Object value) {
        return Math.tan(requireNumber(value, "Math.tan"));
    }

    public static Object log(Object value) {
        return Math.log(requireNumber(value, "Math.log"));
    }

    public static Object exp(Object value) {
        return Math.exp(requireNumber(value, "Math.exp"));
    }

    private static double requireNumber(Object value, String where) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value instanceof String text) {
            try {
                return Double.parseDouble(text.trim());
            } catch (NumberFormatException ignored) {
                // fall through
            }
        }
        throw new IllegalArgumentException(where + " expects a numeric value, got "
                + (value == null ? "null" : value.getClass().getName() + "(" + value + ")"));
    }
}
