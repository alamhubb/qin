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
