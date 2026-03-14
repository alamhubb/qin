package com.qin.lang.runtime;

/**
 * Runtime Number built-ins for Qin JS-style Number object.
 */
public final class QinNumber {
    private static final double MAX_SAFE_INTEGER = 9007199254740991d;

    private QinNumber() {
    }

    public static Object parseInt(Object value) {
        return parseInt(value, 10);
    }

    public static Object parseInt(Object value, Object radixValue) {
        String text = asString(value).trim();
        if (text.isEmpty()) {
            return Double.NaN;
        }
        int radix = normalizeRadix(radixValue);

        int start = 0;
        int sign = 1;
        if (text.charAt(0) == '+' || text.charAt(0) == '-') {
            sign = text.charAt(0) == '-' ? -1 : 1;
            start = 1;
        }
        if (start >= text.length()) {
            return Double.NaN;
        }

        if (radix == 16 && text.regionMatches(true, start, "0x", 0, 2)) {
            start += 2;
        }

        int end = start;
        while (end < text.length() && Character.digit(text.charAt(end), radix) >= 0) {
            end++;
        }
        if (end == start) {
            return Double.NaN;
        }

        String digits = text.substring(start, end);
        try {
            long parsed = Long.parseLong(digits, radix);
            double valueNumber = sign * (double) parsed;
            return toCompactNumber(valueNumber);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    public static Object parseFloat(Object value) {
        String text = asString(value).trim();
        if (text.isEmpty()) {
            return Double.NaN;
        }
        try {
            return toCompactNumber(Double.parseDouble(text));
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    public static Object isNaN(Object value) {
        return (value instanceof Number number) && Double.isNaN(number.doubleValue());
    }

    public static Object isFinite(Object value) {
        if (!(value instanceof Number number)) {
            return false;
        }
        double n = number.doubleValue();
        return !Double.isNaN(n) && !Double.isInfinite(n);
    }

    public static Object isInteger(Object value) {
        if (!(value instanceof Number number)) {
            return false;
        }
        double n = number.doubleValue();
        return !Double.isNaN(n) && !Double.isInfinite(n) && Math.floor(n) == n;
    }

    public static Object isSafeInteger(Object value) {
        if (!(Boolean) isInteger(value)) {
            return false;
        }
        double n = ((Number) value).doubleValue();
        return Math.abs(n) <= MAX_SAFE_INTEGER;
    }

    private static int normalizeRadix(Object radixValue) {
        if (!(radixValue instanceof Number number)) {
            return 10;
        }
        int radix = number.intValue();
        if (radix < 2 || radix > 36) {
            return 10;
        }
        return radix;
    }

    private static String asString(Object value) {
        if (value == null) {
            return "null";
        }
        return String.valueOf(value);
    }

    private static Object toCompactNumber(double value) {
        if (!Double.isFinite(value) || Math.floor(value) != value) {
            return value;
        }
        if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
            return (int) value;
        }
        return (long) value;
    }
}
