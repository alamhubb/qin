package com.qin.lang.runtime;

public final class JavaEsmClassInstanceMethodSmokeTestMain {
    private JavaEsmClassInstanceMethodSmokeTestMain() {
    }

    public static void main(String[] args) {
        assertEquals(String.class.getName(),
                JavaEsmGlobal.__qin_call_method__(String.class, "getName"),
                "Class.getName");
        assertEquals("String",
                JavaEsmGlobal.__qin_call_method__(String.class, "getSimpleName"),
                "Class.getSimpleName");
        assertEquals(true,
                JavaEsmGlobal.__qin_call_method__(
                        String.class,
                        "isInstance",
                        "probe"),
                "Class.isInstance");

        System.out.println("JavaEsmClassInstanceMethodSmokeTestMain OK");
    }

    private static void assertEquals(Object expected, Object actual, String label) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(label + " expected " + expected + ", got " + actual);
        }
    }
}
