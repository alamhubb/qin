package com.qin.runtime.core.db;

public final class QinDbDeleteIdInputSmokeTestMain {
    private QinDbDeleteIdInputSmokeTestMain() {
    }

    public static void main(String[] args) {
        require("7", QinDb.deleteIdText("7", "id"), "raw path id");
        require("8", QinDb.deleteIdText("{\"id\":8}", "id"), "numeric JSON id");
        require("9", QinDb.deleteIdText("{\"userId\":\"9\"}", "userId"), "custom JSON id");
        System.out.println("QinDbDeleteIdInputSmokeTestMain passed.");
    }

    private static void require(String expected, String actual, String label) {
        if (!expected.equals(actual)) {
            throw new IllegalStateException(label + " expected " + expected + ", got " + actual);
        }
    }
}
