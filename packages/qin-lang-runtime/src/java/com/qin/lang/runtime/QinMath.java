package com.qin.lang.runtime;

/**
 * Runtime Math built-ins for Qin JS-style global Math object.
 */
public final class QinMath {
    private QinMath() {
    }

    public static Object random() {
        return java.lang.Math.random();
    }

    public static Object abs(Object value) {
        return java.lang.Math.abs(asNumber(value));
    }

    public static Object floor(Object value) {
        return java.lang.Math.floor(asNumber(value));
    }

    public static Object ceil(Object value) {
        return java.lang.Math.ceil(asNumber(value));
    }

    public static Object max(Object left, Object right) {
        return java.lang.Math.max(asNumber(left), asNumber(right));
    }

    public static Object min(Object left, Object right) {
        return java.lang.Math.min(asNumber(left), asNumber(right));
    }

    public static Object round(Object value) {
        return java.lang.Math.round(asNumber(value));
    }

    public static Object trunc(Object value) {
        double number = asNumber(value);
        return number < 0 ? java.lang.Math.ceil(number) : java.lang.Math.floor(number);
    }

    public static Object pow(Object left, Object right) {
        return java.lang.Math.pow(asNumber(left), asNumber(right));
    }

    public static Object sqrt(Object value) {
        return java.lang.Math.sqrt(asNumber(value));
    }

    public static Object sin(Object value) {
        return java.lang.Math.sin(asNumber(value));
    }

    public static Object cos(Object value) {
        return java.lang.Math.cos(asNumber(value));
    }

    public static Object tan(Object value) {
        return java.lang.Math.tan(asNumber(value));
    }

    public static Object log(Object value) {
        return java.lang.Math.log(asNumber(value));
    }

    public static Object exp(Object value) {
        return java.lang.Math.exp(asNumber(value));
    }

    private static double asNumber(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        throw new IllegalArgumentException("QJS2006 Math built-in expects number, got: "
                + (value == null ? "null" : value.getClass().getName()));
    }
}
