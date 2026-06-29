package com.qin.cli;

import com.qin.core.ConfigLoader;
import com.qin.types.QinConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinCliLanguageLocalDependencyBuildSmokeTestMain {
    private QinCliLanguageLocalDependencyBuildSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Method method = QinCli.class.getDeclaredMethod(
                "ensureLocalLanguageDependenciesBuilt",
                QinConfig.class,
                Path.class,
                boolean.class);
        method.setAccessible(true);

        Path root = Files.createTempDirectory("qin-language-local-deps-smoke");
        Path upstream = root.resolve("upstream");
        Path downstream = root.resolve("downstream");
        Files.createDirectories(upstream.resolve("src"));
        createLocalBinBuildTool(upstream);
        Files.createDirectories(downstream);

        Files.writeString(upstream.resolve("src").resolve("source.ts"), "export const value = 1\n", StandardCharsets.UTF_8);
        Files.writeString(upstream.resolve("qin.config.js"), """
                export default {
                  name: "upstream",
                  version: "1.0.0",
                  scripts: {
                    build: "local-qin-build"
                  },
                  language: {
                    id: "upstream",
                    extension: ".up",
                    parser: "src/source.ts"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(downstream.resolve("qin.config.js"), """
                export default {
                  name: "downstream",
                  version: "1.0.0",
                  dependencies: {
                    upstream: "file:../upstream"
                  },
                  scripts: {
                    test: %s
                  },
                  language: {
                    id: "downstream",
                    extension: ".down",
                    parser: "index.ts"
                  }
                }
                """.formatted(jsString(testScript("../upstream/dist/marker.txt"))), StandardCharsets.UTF_8);

        QinConfig downstreamConfig = new ConfigLoader(downstream.toString()).load();
        method.invoke(null, downstreamConfig, downstream, false);
        require(Files.readString(upstream.resolve("dist").resolve("marker.txt"), StandardCharsets.UTF_8).contains("built"),
                "local file dependency build marker");

        Path cycleA = root.resolve("cycle-a");
        Path cycleB = root.resolve("cycle-b");
        Files.createDirectories(cycleA);
        Files.createDirectories(cycleB);
        Files.writeString(cycleA.resolve("qin.config.js"), cycleConfig("cycle-a", "../cycle-b"), StandardCharsets.UTF_8);
        Files.writeString(cycleB.resolve("qin.config.js"), cycleConfig("cycle-b", "../cycle-a"), StandardCharsets.UTF_8);

        try {
            method.invoke(null, new ConfigLoader(cycleA.toString()).load(), cycleA, true);
            throw new IllegalStateException("Expected circular local Qin dependency failure");
        } catch (InvocationTargetException error) {
            Throwable cause = error.getCause();
            require(cause instanceof IllegalStateException
                            && cause.getMessage().contains("Circular local Qin dependency"),
                    "circular local Qin dependency error");
        }

        System.out.println("QinCliLanguageLocalDependencyBuildSmokeTestMain OK");
    }

    private static void createLocalBinBuildTool(Path root) throws Exception {
        Path bin = root.resolve("node_modules").resolve(".bin");
        Files.createDirectories(bin);
        if (isWindows()) {
            Files.writeString(bin.resolve("local-qin-build.cmd"), """
                    @echo off
                    if not exist dist mkdir dist
                    > dist\\marker.txt echo built:local-bin
                    """, StandardCharsets.UTF_8);
            return;
        }
        Path tool = bin.resolve("local-qin-build");
        Files.writeString(tool, """
                #!/bin/sh
                set -eu
                mkdir -p dist
                printf 'built:local-bin\\n' > dist/marker.txt
                """, StandardCharsets.UTF_8);
        if (!tool.toFile().setExecutable(true)) {
            throw new IllegalStateException("Unable to make local-qin-build executable: " + tool);
        }
    }

    private static String testScript(String marker) {
        if (isWindows()) {
            return "powershell -NoProfile -Command \"if (-not (Test-Path '" + marker
                    + "')) { throw 'missing marker' }\"";
        }
        return "test -f " + marker;
    }

    private static String cycleConfig(String name, String dependencyPath) {
        return """
                export default {
                  name: "%s",
                  version: "1.0.0",
                  dependencies: {
                    other: "file:%s"
                  },
                  scripts: {
                    build: %s
                  },
                  language: {
                    id: "%s",
                    extension: ".cycle",
                    parser: "index.ts"
                  }
                }
                """.formatted(name, dependencyPath, jsString(testScript("qin.config.js")), name);
    }

    private static String jsString(String value) {
        StringBuilder out = new StringBuilder("\"");
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            switch (ch) {
                case '\\' -> out.append("\\\\");
                case '"' -> out.append("\\\"");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> out.append(ch);
            }
        }
        return out.append('"').toString();
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
