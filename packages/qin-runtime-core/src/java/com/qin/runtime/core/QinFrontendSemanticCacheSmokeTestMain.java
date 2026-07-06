package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFrontendSemanticCacheSmokeTestMain {
    private QinFrontendSemanticCacheSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-frontend-semantic-cache-");
        Path app = root.resolve("app");
        Files.createDirectories(app);
        Path entry = app.resolve("main.qin");
        Path dependency = app.resolve("label.qin");
        Files.writeString(entry, """
                import { label } from "./label.qin";

                export const appLabel = label;
                """, StandardCharsets.UTF_8);

        writeDependency(dependency, "v1");
        runCreate(root, entry);
        String second = runCreate(root, entry);
        requireContains(second, "frontend semantic cache hit :: modules=2");

        writeDependency(dependency, "v2");
        String third = runCreate(root, entry);
        if (third.contains("frontend semantic cache hit")) {
            throw new IllegalStateException("Changed frontend module source must invalidate semantic cache:\n" + third);
        }

        System.out.println("QinFrontendSemanticCacheSmokeTestMain OK");
    }

    private static String runCreate(Path root, Path entry) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream previousOut = System.out;
        try (PrintStream capture = new PrintStream(out, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            QinFrontendEsmService.create(root, entry);
        } finally {
            System.setOut(previousOut);
        }
        return out.toString(StandardCharsets.UTF_8);
    }

    private static void writeDependency(Path dependency, String value) throws Exception {
        Files.writeString(dependency, """
                export const label = "%s";
                """.formatted(value), StandardCharsets.UTF_8);
    }

    private static void requireContains(String log, String expected) {
        if (!log.contains(expected)) {
            throw new IllegalStateException("Expected log to contain " + expected + ", got:\n" + log);
        }
    }
}
