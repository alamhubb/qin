package com.qin.lang.runtime;

/**
 * Runtime Date built-ins for Qin JS-style Date object.
 */
public final class QinDate {
    private QinDate() {
    }

    public static Object now() {
        return System.currentTimeMillis();
    }
}
