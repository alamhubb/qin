package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFullstackBuildProfileSmokeTestMain {
    private QinFullstackBuildProfileSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-fullstack-profile-");
        Files.createDirectories(root.resolve("main"));
        Files.createDirectories(root.resolve("app"));
        Files.writeString(root.resolve("main/Main.java"), """
                public final class Main {
                    private Main() {}

                    public static Object run() {
                        return "profile-ok";
                    }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("app/main.js"), """
                export const marker = "profile-frontend";
                """, StandardCharsets.UTF_8);

        String previousProfile = System.getProperty("qin.profile");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream previousOut = System.out;
        try (PrintStream capture = new PrintStream(out, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            QinFullstackMain.main(new String[] {
                    "--root", root.toString(),
                    "--build-only",
                    "--profile"
            });
        } finally {
            System.setOut(previousOut);
            if (previousProfile == null) {
                System.clearProperty("qin.profile");
            } else {
                System.setProperty("qin.profile", previousProfile);
            }
        }

        String log = out.toString(StandardCharsets.UTF_8);
        requireContains(log, "[QinProfile] fullstack-build start");
        requireContains(log, "[QinProfile] fullstack-build build backend");
        requireContains(log, "[QinProfile] fullstack-build create frontend service");
        requireContains(log, "[QinProfile] javac done");
        requireContains(log, "sources=1");
        requireContains(log, "Build only mode finished.");
        System.out.println("QinFullstackBuildProfileSmokeTestMain OK");
    }

    private static void requireContains(String log, String expected) {
        if (!log.contains(expected)) {
            throw new IllegalStateException("Expected profile log to contain " + expected + ", got:\n" + log);
        }
    }
}
