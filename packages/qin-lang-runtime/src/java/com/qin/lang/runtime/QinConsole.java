package com.qin.lang.runtime;

/**
 * Runtime console built-ins.
 */
public final class QinConsole {
    private QinConsole() {
    }

    public static void log(Object value) {
        System.out.println(value);
    }
}

