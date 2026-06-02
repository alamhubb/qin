package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinCsstsCompilerSmokeTestMain {
    private QinCsstsCompilerSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-cssts-compiler-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-cssts-compiler-smoke\" }\n", StandardCharsets.UTF_8);

        String source = """
                const buttonBase = css { colorRed, fontBold }
                const buttonHover = css { backgroundColorBlue }
                """;

        try {
            QinCsstsCompiler.QinCsstsCompileResult result = new QinCsstsCompiler().compile(root, source);
            System.out.println("code:");
            System.out.println(result.code());
            System.out.println("hasStyles=" + result.hasStyles());
            System.out.println("css:");
            System.out.println(result.css());
            System.out.println("atom:");
            System.out.println(result.atomModule());
        } catch (Throwable error) {
            System.out.println("QinCsstsCompilerSmokeTestMain failed: " + error);
            Throwable current = error;
            int depth = 0;
            while (current != null && depth < 12) {
                System.out.println("cause[" + depth + "]: " + current.getClass().getName() + ": " + current.getMessage());
                current = current.getCause();
                depth++;
            }
            throw error;
        }
    }
}

