package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFullstackQinBackendModuleCacheSmokeTestMain {
    private QinFullstackQinBackendModuleCacheSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-fullstack-qin-backend-module-cache-");
        Path mainDir = root.resolve("main");
        Files.createDirectories(mainDir);
        Files.createDirectories(root.resolve("app"));
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "qin-backend-module-cache-smoke",
                  backend: { entry: "main/main.qin" },
                  frontend: { entry: "app/main.js", staticDir: "app" }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("app/main.js"), """
                export const marker = "backend-module-cache";
                """, StandardCharsets.UTF_8);
        Path source = mainDir.resolve("main.qin");

        writeSource(source, "v1");
        runBuild(root, source);
        String second = runBuild(root, source);
        requireContains(second, "qin backend module cache hit :: com.qin.runtime.generated.ServerApp");
        requireRunResult(root, "v1");

        writeSource(source, "v2");
        String third = runBuild(root, source);
        if (third.contains("qin backend module cache hit :: com.qin.runtime.generated.ServerApp")) {
            throw new IllegalStateException("Changed Qin backend source must invalidate module cache:\n" + third);
        }
        requireRunResult(root, "v2");

        System.out.println("QinFullstackQinBackendModuleCacheSmokeTestMain OK");
    }

    private static String runBuild(Path root, Path backendFile) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream previousOut = System.out;
        try (PrintStream capture = new PrintStream(out, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            QinFullstackMain.main(new String[] {
                    "--root", root.toString(),
                    "--backend-file", backendFile.toString(),
                    "--frontend-file", root.resolve("app/main.js").toString(),
                    "--static-dir", root.resolve("app").toString(),
                    "--build-only",
                    "--profile"
            });
        } finally {
            System.setOut(previousOut);
        }
        return out.toString(StandardCharsets.UTF_8);
    }

    private static void writeSource(Path source, String value) throws Exception {
        Files.writeString(source, """
                export const marker = "%s";
                marker;
                """.formatted(value), StandardCharsets.UTF_8);
    }

    private static void requireRunResult(Path root, String expected) throws Exception {
        Path classes = root.resolve("build/fullstack/server-classes");
        try (URLClassLoader loader = new URLClassLoader(
                new URL[] { classes.toUri().toURL() },
                QinFullstackQinBackendModuleCacheSmokeTestMain.class.getClassLoader())) {
            Class<?> type = Class.forName("com.qin.runtime.generated.ServerAppFullstackAdapter", true, loader);
            Method run = type.getMethod("run");
            Object result = run.invoke(null);
            if (!expected.equals(result)) {
                throw new IllegalStateException("Expected backend run result " + expected + ", got " + result);
            }
        }
    }

    private static void requireContains(String log, String expected) {
        if (!log.contains(expected)) {
            throw new IllegalStateException("Expected log to contain " + expected + ", got:\n" + log);
        }
    }
}
