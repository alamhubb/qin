package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;

public final class QinJsRuntimeNumberConversionSmokeTestMain {
    private QinJsRuntimeNumberConversionSmokeTestMain() {
    }

    public static void main(String[] args) {
        String longInvalidNumber = "1".repeat(100_000) + "e+";
        long started = System.nanoTime();

        Object plus = JavaEsmGlobal.__qin_binary__("+", longInvalidNumber, 1);
        Object lessThan = JavaEsmGlobal.__qin_binary__("<", longInvalidNumber, 2);

        long elapsedMillis = (System.nanoTime() - started) / 1_000_000L;
        require(String.valueOf(plus).endsWith("e+1"), "invalid numeric string should concatenate for +");
        require(Boolean.FALSE.equals(lessThan), "invalid numeric string should compare as NaN");
        require(elapsedMillis < 2_000L, "number conversion should be linear for long non-numeric strings");

        System.out.println("QinJsRuntimeNumberConversionSmokeTestMain OK");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
