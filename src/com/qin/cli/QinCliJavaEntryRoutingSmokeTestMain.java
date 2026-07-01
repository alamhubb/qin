package com.qin.cli;

import com.qin.core.ConfigLoader;
import com.qin.types.QinConfig;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinCliJavaEntryRoutingSmokeTestMain {
    private QinCliJavaEntryRoutingSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Method method = QinCli.class.getDeclaredMethod("resolveDefaultQinEntry", QinConfig.class);
        method.setAccessible(true);

        String previousCwd = System.getProperty("user.dir");
        Path javaRoot = Files.createTempDirectory("qin-cli-java-entry-");
        Files.createDirectories(javaRoot.resolve("src/server"));
        Files.writeString(javaRoot.resolve("qin.config.js"), """
                export default {
                  name: "java-entry-smoke",
                  entry: "src/server/Main.java",
                  java: { sourceDir: "src/server" }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(javaRoot.resolve("src/server/Main.java"), """
                package server;
                public final class Main {
                  public static void main(String[] args) {
                    System.out.println("hello");
                  }
                }
                """, StandardCharsets.UTF_8);
        QinConfig javaConfig = new ConfigLoader(javaRoot.toString()).load();
        System.setProperty("user.dir", javaRoot.toString());
        try {
            Object javaEntry = method.invoke(null, javaConfig);
            require(javaEntry == null, "root Java entry stays on JavaRunner path");
        } finally {
            System.setProperty("user.dir", previousCwd);
        }

        Path fullstackRoot = Files.createTempDirectory("qin-cli-fullstack-java-entry-");
        Files.createDirectories(fullstackRoot.resolve("main"));
        Files.writeString(fullstackRoot.resolve("qin.config.js"), """
                export default {
                  name: "fullstack-java-entry-smoke",
                  backend: { entry: "main/Main.java" }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(fullstackRoot.resolve("main/Main.java"), """
                package demo;
                public final class Main {
                  public static Object run() {
                    return "hello";
                  }
                }
                """, StandardCharsets.UTF_8);
        QinConfig fullstackConfig = new ConfigLoader(fullstackRoot.toString()).load();
        System.setProperty("user.dir", fullstackRoot.toString());
        try {
            Object fullstackEntry = method.invoke(null, fullstackConfig);
            require("main/Main.java".equals(fullstackEntry), "backend Java entry stays on fullstack Qin runtime path");
        } finally {
            System.setProperty("user.dir", previousCwd);
        }

        System.out.println("QinCliJavaEntryRoutingSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
