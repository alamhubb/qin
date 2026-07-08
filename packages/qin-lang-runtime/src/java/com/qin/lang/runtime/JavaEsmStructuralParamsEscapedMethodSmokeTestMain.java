package com.qin.lang.runtime;

import java.util.LinkedHashMap;

public final class JavaEsmStructuralParamsEscapedMethodSmokeTestMain {
    private JavaEsmStructuralParamsEscapedMethodSmokeTestMain() {
    }

    public static void main(String[] args) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("In", true);
        params.put("Yield", false);
        params.put("Await", true);

        assertEquals(true, JavaEsmGlobal.__qin_call_method__(params, "__qin_in"), "__qin_in");
        assertEquals(false, JavaEsmGlobal.__qin_call_method__(params, "__qin_yield"), "__qin_yield");
        assertEquals(true, JavaEsmGlobal.__qin_call_method__(params, "__qin_await"), "__qin_await");

        assertEquals(true, JavaEsmGlobal.__qin_call_method__(params, "in"), "in");
        assertEquals(false, JavaEsmGlobal.__qin_call_method__(params, "yield"), "yield");
        assertEquals(true, JavaEsmGlobal.__qin_call_method__(params, "await"), "await");

        System.out.println("JavaEsmStructuralParamsEscapedMethodSmokeTestMain OK");
    }

    private static void assertEquals(Object expected, Object actual, String label) {
        if (!expected.equals(actual)) {
            throw new AssertionError(label + " expected " + expected + ", got " + actual);
        }
    }
}
