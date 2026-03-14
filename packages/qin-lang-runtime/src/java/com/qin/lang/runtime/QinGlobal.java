package com.qin.lang.runtime;

/**
 * Runtime global function built-ins.
 */
public final class QinGlobal {
    private QinGlobal() {
    }

    public static Object parseInt(Object value) {
        return QinNumber.parseInt(value);
    }

    public static Object parseInt(Object value, Object radix) {
        return QinNumber.parseInt(value, radix);
    }

    public static Object parseFloat(Object value) {
        return QinNumber.parseFloat(value);
    }

    public static Object isNaN(Object value) {
        return QinNumber.isNaN(value);
    }

    public static Object isFinite(Object value) {
        return QinNumber.isFinite(value);
    }
}
