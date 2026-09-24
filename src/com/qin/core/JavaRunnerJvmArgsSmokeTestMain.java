package com.qin.core;

import com.qin.types.QinConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class JavaRunnerJvmArgsSmokeTestMain {
    private JavaRunnerJvmArgsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Method method = JavaRunner.class.getDeclaredMethod("appendDefaultRunJvmArgs", List.class, List.class);
        method.setAccessible(true);

        List<String> defaults = new ArrayList<>();
        method.invoke(newRunner(), defaults, List.of());
        require(defaults.contains("-Xmx1536m"), "default run heap should be 1536m");
        require(defaults.contains("-Xms16m"), "default run args should keep Xms");

        List<String> explicit = new ArrayList<>();
        method.invoke(newRunner(), explicit, List.of("-Xmx512m"));
        require(!explicit.contains("-Xmx1536m"), "explicit Xmx should override default heap");

        Method inheritedMethod = JavaRunner.class.getDeclaredMethod(
                "appendInheritedRunSystemProperties",
                List.class,
                List.class);
        inheritedMethod.setAccessible(true);
        String previousMode = System.getProperty("qin.dynamicSemanticMode");
        try {
            System.setProperty("qin.dynamicSemanticMode", "error");
            List<String> inherited = new ArrayList<>();
            inheritedMethod.invoke(newRunner(), inherited, List.of());
            require(inherited.contains("-Dqin.dynamicSemanticMode=error"),
                    "Qin dynamic semantic mode should be inherited by qin run child JVM");

            List<String> overridden = new ArrayList<>();
            inheritedMethod.invoke(newRunner(), overridden, List.of("-Dqin.dynamicSemanticMode=warn"));
            require(!overridden.contains("-Dqin.dynamicSemanticMode=error"),
                    "Explicit --jvm-args should override inherited Qin dynamic semantic mode");
        } finally {
            if (previousMode == null) {
                System.clearProperty("qin.dynamicSemanticMode");
            } else {
                System.setProperty("qin.dynamicSemanticMode", previousMode);
            }
        }

        System.out.println("JavaRunnerJvmArgsSmokeTestMain OK");
    }

    private static JavaRunner newRunner() {
        return new JavaRunner(new QinConfig("java-runner-jvm-args-smoke", "0.1.0"), "", ".");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
