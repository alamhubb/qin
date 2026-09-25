package com.qin.lang.ir;

/**
 * Generated TypeScript imports can reach both the public facade spelling
 * (__QinJavaLangNumber) and the lowered class spelling (QinJavaLangNumber).
 * Both must resolve to java.lang.Number before JVM emission.
 */
public final class QinJavaSdkAliasSupportNumberSmokeTestMain {
    private QinJavaSdkAliasSupportNumberSmokeTestMain() {
    }

    public static void main(String[] args) {
        assertCanonical("__QinJavaLangNumber", "java.lang.Number");
        assertCanonical("QinJavaLangNumber", "java.lang.Number");
        assertCanonical("QinJavaLangDouble", "java.lang.Double");
        assertCanonical("QinJavaLangLong", "java.lang.Long");
        System.out.println("QinJavaSdkAliasSupportNumberSmokeTestMain OK");
    }

    private static void assertCanonical(String alias, String expected) {
        String actual = QinJavaSdkAliasSupport.canonicalBinaryName(alias);
        if (!expected.equals(actual)) {
            throw new IllegalStateException("Expected " + alias + " -> " + expected + ", got " + actual);
        }
    }
}
