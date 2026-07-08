package com.qin.lang.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JavaEsmInterpretedJavaListMemberSmokeTestMain {
    private JavaEsmInterpretedJavaListMemberSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Object instance = newInterpretedInstance();
        JavaEsmGlobal.__qin_member_set__(instance, "__items", new java.util.ArrayList<>(List.of("a", "b")));

        assertEquals(2.0d, JavaEsmGlobal.__qin_member_get__(instance, "length"), "length");
        assertEquals("b", JavaEsmGlobal.__qin_member_get__(instance, "1"), "numeric index");
        JavaEsmGlobal.__qin_member_set__(instance, "__qin_field_name", "items");
        assertEquals("items", JavaEsmGlobal.__qin_member_get__(instance, "__qin_field_name"), "plain field");

        System.out.println("JavaEsmInterpretedJavaListMemberSmokeTestMain OK");
    }

    private static Object newInterpretedInstance() throws Exception {
        Class<?> cls = Class.forName("com.qin.lang.runtime.JavaEsmGlobal$InterpretedInstance");
        Constructor<?> constructor = cls.getDeclaredConstructor(Map.class, Map.class);
        constructor.setAccessible(true);
        Object instance = constructor.newInstance(new LinkedHashMap<>(), new LinkedHashMap<>());
        Method putOwnField = cls.getDeclaredMethod("putOwnField", String.class, Object.class);
        putOwnField.setAccessible(true);
        putOwnField.invoke(instance, "__items", new java.util.ArrayList<>());
        return instance;
    }

    private static void assertEquals(Object expected, Object actual, String label) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(label + " expected " + expected + ", got " + actual);
        }
    }
}
