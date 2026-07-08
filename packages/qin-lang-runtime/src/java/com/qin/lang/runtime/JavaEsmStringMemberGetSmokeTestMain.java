package com.qin.lang.runtime;

public final class JavaEsmStringMemberGetSmokeTestMain {
    private JavaEsmStringMemberGetSmokeTestMain() {
    }

    public static void main(String[] args) {
        assertEquals(3, JavaEsmGlobal.__qin_member_get__("abc", "length"), "length");
        assertEquals("b", JavaEsmGlobal.__qin_member_get__("abc", "1"), "numeric string index");
        assertEquals(null, JavaEsmGlobal.__qin_member_get__("abc", "tokenName"), "plain property");
        assertEquals(null, JavaEsmGlobal.__qin_member_get__("abc", "__qin_field_name"), "generated field probe");

        System.out.println("JavaEsmStringMemberGetSmokeTestMain OK");
    }

    private static void assertEquals(Object expected, Object actual, String label) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(label + " expected " + expected + ", got " + actual);
        }
    }
}
