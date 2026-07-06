package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFullstackJavacCacheSmokeTestMain {
    private QinFullstackJavacCacheSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-fullstack-javac-cache-");
        Path mainDir = root.resolve("main/demo");
        Files.createDirectories(mainDir);
        Files.createDirectories(root.resolve("app"));
        Path source = mainDir.resolve("ProfileMain.java");
        writeSource(source, "v1");
        Files.writeString(root.resolve("app/main.js"), """
                export const marker = "javac-cache";
                """, StandardCharsets.UTF_8);

        runBuild(root, source);
        String second = runBuild(root, source);
        requireContains(second, "javac cache hit :: backend Java sources");
        requireRunResult(root, "v1");

        writeSource(source, "v2");
        String third = runBuild(root, source);
        if (third.contains("javac cache hit :: backend Java sources")) {
            throw new IllegalStateException("Changed Java source must invalidate the javac cache:\n" + third);
        }
        requireRunResult(root, "v2");

        System.out.println("QinFullstackJavacCacheSmokeTestMain OK");
    }

    private static String runBuild(Path root, Path backendFile) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream previousOut = System.out;
        try (PrintStream capture = new PrintStream(out, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            QinFullstackMain.main(new String[] {
                    "--root", root.toString(),
                    "--backend-file", backendFile.toString(),
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
                package demo;

                public final class ProfileMain {
                    private ProfileMain() {}

                    public static Object run() {
                        return "%s";
                    }
                }
                """.formatted(value), StandardCharsets.UTF_8);
    }

    private static void requireRunResult(Path root, String expected) throws Exception {
        Path classes = root.resolve("build/fullstack/server-classes");
        try (URLClassLoader loader = new URLClassLoader(new URL[] { classes.toUri().toURL() }, null)) {
            Class<?> type = Class.forName("demo.ProfileMain", true, loader);
            Method run = type.getMethod("run");
            Object result = run.invoke(null);
            if (!expected.equals(result)) {
                throw new IllegalStateException("Expected ProfileMain.run()=" + expected + ", got " + result);
            }
        }
    }

    private static void requireContains(String log, String expected) {
        if (!log.contains(expected)) {
            throw new IllegalStateException("Expected log to contain " + expected + ", got:\n" + log);
        }
    }
}
