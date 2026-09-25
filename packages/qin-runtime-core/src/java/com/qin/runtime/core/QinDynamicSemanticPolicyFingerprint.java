package com.qin.runtime.core;

final class QinDynamicSemanticPolicyFingerprint {
    private QinDynamicSemanticPolicyFingerprint() {
    }

    static String current() {
        return "dynamicSemanticMode=" + System.getProperty("qin.dynamicSemanticMode", "warn")
                + "\n"
                + "dynamicSemanticHardFailures=" + System.getProperty("qin.dynamicSemanticHardFailures", "false")
                + "\n"
                + "dynamicSemanticWarnings=" + System.getProperty("qin.dynamicSemanticWarnings", "true");
    }
}
