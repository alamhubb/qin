package com.qin.runtime.core;

import java.nio.file.Path;

public final class QinModuleClassJavaSdkSystemSmokeTestMain {
    private QinModuleClassJavaSdkSystemSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: QinModuleClassJavaSdkSystemSmokeTestMain <repro-root>");
        }
        Path root = Path.of(args[0]).toAbsolutePath().normalize();
        Object result = new QinInMemoryJvmRunner().compileAndRunModuleClasses(
                root.resolve("lang").resolve("system.js"),
                root,
                "probe.QinModuleClassJavaSdkSystemSmoke",
                "",
                root.resolve(".qin").resolve("module-class-cache"),
                "");
        if (result == null) {
            throw new IllegalStateException("Expected Java SDK system module to evaluate");
        }
        System.out.println("QinModuleClassJavaSdkSystemSmokeTestMain OK");
    }
}
