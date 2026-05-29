package com.qin.lang.runtime;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Java-backed Number builtin subset for Qin.
 */
public final class JavaEsmNumber {
    private static final double MAX_SAFE_INTEGER = 9007199254740991d;
    private static final double MIN_SAFE_INTEGER = -9007199254740991d;

    private JavaEsmNumber() {
    }

    public static Object parseInt(Object value) {
        return JavaEsmGlobal.parseInt(value);
    }

    public static Object parseInt(Object value, Object radix) {
        return JavaEsmGlobal.parseInt(value, radix);
    }

    public static Object parseFloat(Object value) {
        return JavaEsmGlobal.parseFloat(value);
    }

    public static Object isNaN(Object value) {
        return JavaEsmGlobal.isNaN(value);
    }

    public static Object isFinite(Object value) {
        return JavaEsmGlobal.isFinite(value);
    }

    public static Object isInteger(Object value) {
        Double number = asNumber(value);
        return number != null && Double.isFinite(number) && number == Math.rint(number);
    }

    public static Object isSafeInteger(Object value) {
        Double number = asNumber(value);
        return number != null
                && Double.isFinite(number)
                && number == Math.rint(number)
                && number >= MIN_SAFE_INTEGER
                && number <= MAX_SAFE_INTEGER;
    }

    static boolean supports(String methodName) {
        return switch (methodName) {
            case "toFixed", "toString" -> true;
            default -> false;
        };
    }

    static Object staticMemberGet(Object property) {
        return switch (String.valueOf(property)) {
            case "MAX_SAFE_INTEGER" -> MAX_SAFE_INTEGER;
            case "MIN_SAFE_INTEGER" -> MIN_SAFE_INTEGER;
            case "NaN" -> Double.NaN;
            case "POSITIVE_INFINITY" -> Double.POSITIVE_INFINITY;
            case "NEGATIVE_INFINITY" -> Double.NEGATIVE_INFINITY;
            default -> null;
        };
    }

    static Object invoke(Number target, String methodName, Object[] args) {
        return switch (methodName) {
            case "toFixed" -> toFixed(target, args);
            case "toString" -> toString(target, args);
            default -> throw new IllegalArgumentException("Unsupported Number builtin: " + methodName);
        };
    }

    private static Object toString(Number target, Object[] args) {
        if (args.length > 1) {
            throw new IllegalArgumentException("Number.toString expects at most 1 argument");
        }
        int radix = args.length == 0 || args[0] == null ? 10 : toInteger(args[0]);
        if (radix < 2 || radix > 36) {
            throw new IllegalArgumentException("Number.toString radix must be between 2 and 36");
        }
        double value = target.doubleValue();
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return String.valueOf(value);
        }
        if (radix == 10 && value == Math.rint(value)) {
            return Long.toString((long) value);
        }
        if (radix != 10 && value == Math.rint(value)) {
            return Long.toString((long) value, radix);
        }
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }

    private static Object toFixed(Number target, Object[] args) {
        if (args.length > 1) {
            throw new IllegalArgumentException("Number.toFixed expects at most 1 argument");
        }
        int digits = args.length == 0 || args[0] == null ? 0 : toInteger(args[0]);
        if (digits < 0 || digits > 100) {
            throw new IllegalArgumentException("Number.toFixed digits must be between 0 and 100");
        }
        double value = target.doubleValue();
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return String.valueOf(value);
        }
        return BigDecimal.valueOf(value)
                .setScale(digits, RoundingMode.HALF_UP)
                .toPlainString();
    }

    private static int toInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String text) {
            try {
                return (int) Double.parseDouble(text.trim());
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
        return 0;
    }

    private static Double asNumber(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value instanceof String text) {
            try {
                return Double.parseDouble(text.trim());
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }
}
