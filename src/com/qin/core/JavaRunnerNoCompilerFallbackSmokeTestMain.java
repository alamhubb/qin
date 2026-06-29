package com.qin.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class JavaRunnerNoCompilerFallbackSmokeTestMain {
    private JavaRunnerNoCompilerFallbackSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path source = Path.of("src", "com", "qin", "core", "JavaRunner.java").toAbsolutePath().normalize();
        if (!Files.isRegularFile(source)) {
            throw new IllegalStateException("JavaRunner source not found: " + source);
        }

        String text = Files.readString(source);
        for (String forbidden : List.of(
                "compileWithExternalJavac",
                "external javac fallback",
                "In-process javac failed")) {
            if (text.contains(forbidden)) {
                throw new IllegalStateException("JavaRunner compile path must not use compiler fallback: " + forbidden);
            }
        }

        System.out.println("JavaRunnerNoCompilerFallbackSmokeTestMain OK");
    }
}
