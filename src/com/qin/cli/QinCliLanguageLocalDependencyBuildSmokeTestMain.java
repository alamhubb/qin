package com.qin.cli;

import com.qin.core.ConfigLoader;
import com.qin.types.QinConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

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
        Path marker = upstream.resolve("dist").resolve("marker.txt");
        String firstMarker = Files.readString(marker, StandardCharsets.UTF_8);
        require(firstMarker.contains("built:") && firstMarker.contains("value = 1"),
                "local file dependency build marker from initial source");

        Files.writeString(upstream.resolve("src").resolve("source.ts"), "export const value = 2\n", StandardCharsets.UTF_8);
        method.invoke(null, downstreamConfig, downstream, false);
        String rebuiltMarker = Files.readString(marker, StandardCharsets.UTF_8);
        require(rebuiltMarker.contains("built:") && rebuiltMarker.contains("value = 2") && !rebuiltMarker.contains("value = 1"),
                "local file dependency rebuild marker from changed source");

        Path concurrentRoot = root.resolve("concurrent");
        Path concurrentUpstream = concurrentRoot.resolve("upstream");
        Path downstreamA = concurrentRoot.resolve("downstream-a");
        Path downstreamB = concurrentRoot.resolve("downstream-b");
        Files.createDirectories(concurrentUpstream.resolve("src"));
        Files.createDirectories(downstreamA);
        Files.createDirectories(downstreamB);
        createConcurrentBuildTool(concurrentUpstream);
        Files.writeString(concurrentUpstream.resolve("src").resolve("source.ts"), "export const concurrent = true\n",
                StandardCharsets.UTF_8);
        Files.writeString(concurrentUpstream.resolve("qin.config.js"), """
                export default {
                  name: "concurrent-upstream",
                  version: "1.0.0",
                  scripts: {
                    build: "concurrent-qin-build"
                  },
                  language: {
                    id: "concurrent-upstream",
                    extension: ".cup",
                    parser: "src/source.ts"
                  }
                }
                """, StandardCharsets.UTF_8);
        writeDownstreamConfig(downstreamA, "../upstream");
        writeDownstreamConfig(downstreamB, "../upstream");

        CountDownLatch start = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();
        List<Thread> threads = List.of(
                dependencyBuildThread(method, downstreamA, start, failure),
                dependencyBuildThread(method, downstreamB, start, failure));
        for (Thread thread : threads) {
            thread.start();
        }
        start.countDown();
        for (Thread thread : threads) {
            thread.join();
        }
        if (failure.get() != null) {
            throw new IllegalStateException("Concurrent local Qin dependency build smoke failed", failure.get());
        }
        require(!Files.exists(concurrentUpstream.resolve("dist").resolve("violation.txt")),
                "language local dependency build lock prevents concurrent dist clean/build entry");
        require(Files.isRegularFile(concurrentUpstream.resolve("dist").resolve("marker.txt")),
                "concurrent local dependency build marker");

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

        verifyLanguageScriptDryRunGeneratesParserFirst(root.resolve("generated-language"));

        System.out.println("QinCliLanguageLocalDependencyBuildSmokeTestMain OK");
    }

    private static void verifyLanguageScriptDryRunGeneratesParserFirst(Path languageRoot) throws Exception {
        Path generatedRoot = languageRoot.resolve("generated").resolve("qin-parser-ts");
        Files.createDirectories(generatedRoot);
        Files.createDirectories(languageRoot.resolve("src").resolve("java"));
        Files.writeString(generatedRoot.resolve("qin.config.js"), """
                export default {
                  name: "@qin/generated-qin-parser-ts",
                  version: "0.0.0",
                  type: "library",
                  entry: "./index.ts",
                  generated: {
                    source: "java",
                    entryBinaryName: "com.qin.parser.QinParser",
                    additionalEntryBinaryNames: [
                      "com.slime.parser.cstToAst.SlimeCstToAstUtils",
                      "com.slime.parser.cstToAst.SlimeAstCreateUtils"
                    ]
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(generatedRoot.resolve("package.json"), """
                {
                  "name": "@qin/generated-qin-parser-ts",
                  "version": "0.0.0",
                  "type": "module",
                  "main": "./index.ts",
                  "module": "./index.ts",
                  "qin": {
                    "entryBinaryName": "com.qin.parser.QinParser"
                  },
                  "exports": {
                    ".": "./index.ts",
                    "./SlimeCstToAstUtils": "./com/slime/parser/cstToAst/SlimeCstToAstUtils.ts",
                    "./SlimeAstCreateUtils": "./com/slime/parser/cstToAst/SlimeAstCreateUtils.ts"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(generatedRoot.resolve("index.ts"), """
                export default com_qin_parser_QinParser
                export { com_qin_parser_QinParser as QinParser }
                export { SlimeJavascriptParser }
                """, StandardCharsets.UTF_8);
        Files.writeString(languageRoot.resolve("qin.config.js"), """
                export default {
                  name: "generated-language",
                  version: "1.0.0",
                  scripts: {
                    test: "generated-language-test"
                  },
                  language: {
                    id: "qin",
                    extension: ".qin",
                    parser: "generated/qin-parser-ts"
                  },
                  generated: {
                    source: "java",
                    entryBinaryName: "com.qin.parser.QinParser",
                    additionalEntryBinaryNames: [
                      "com.slime.parser.cstToAst.SlimeCstToAstUtils",
                      "com.slime.parser.cstToAst.SlimeAstCreateUtils"
                    ],
                    sourceRoots: [
                      "src/java"
                    ],
                    outputDir: "generated/qin-parser-ts"
                  },
                  qinLanguage: {
                    sourceExtension: ".qin",
                    serviceExtension: ".ts",
                    parserPackage: "com.qin:qin-parser",
                    generatedParserTarget: "@qin/generated-qin-parser-ts"
                  }
                }
                """, StandardCharsets.UTF_8);

        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        String originalUserDir = System.getProperty("user.dir");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            System.setErr(capture);
            System.setProperty("user.dir", languageRoot.toString());
            Method runLanguageScript = QinCli.class.getDeclaredMethod(
                    "runLanguageScript",
                    String.class,
                    String[].class);
            runLanguageScript.setAccessible(true);
            try {
                runLanguageScript.invoke(null, "test", new String[]{"--dry-run"});
            } catch (InvocationTargetException error) {
                Throwable cause = error.getCause();
                if (cause instanceof Exception exception) {
                    throw exception;
                }
                throw error;
            }
        } finally {
            System.setProperty("user.dir", originalUserDir);
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        String captured = output.toString(StandardCharsets.UTF_8);
        int generateIndex = captured.indexOf("generate java parser com.qin.parser.QinParser");
        int scriptIndex = captured.indexOf("generated-language-test");
        require(generateIndex >= 0,
                "qin language test --dry-run must generate parser before script execution");
        require(scriptIndex >= 0,
                "qin language test --dry-run must still print the configured test script command");
        require(generateIndex < scriptIndex,
                "qin language test --dry-run must generate parser before running scripts");
    }

    private static void createLocalBinBuildTool(Path root) throws Exception {
        Path bin = root.resolve("node_modules").resolve(".bin");
        Files.createDirectories(bin);
        if (isWindows()) {
            Files.writeString(bin.resolve("local-qin-build.cmd"), """
                    @echo off
                    if not exist dist mkdir dist
                    powershell -NoProfile -ExecutionPolicy Bypass -Command "$src = Get-Content -Raw -Encoding UTF8 'src/source.ts'; Set-Content -Encoding UTF8 'dist/marker.txt' ('built:' + $src)"
                    """, StandardCharsets.UTF_8);
            return;
        }
        Path tool = bin.resolve("local-qin-build");
        Files.writeString(tool, """
                #!/bin/sh
                set -eu
                mkdir -p dist
                printf 'built:' > dist/marker.txt
                cat src/source.ts >> dist/marker.txt
                """, StandardCharsets.UTF_8);
        if (!tool.toFile().setExecutable(true)) {
            throw new IllegalStateException("Unable to make local-qin-build executable: " + tool);
        }
    }

    private static void createConcurrentBuildTool(Path root) throws Exception {
        Path bin = root.resolve("node_modules").resolve(".bin");
        Files.createDirectories(bin);
        if (isWindows()) {
            Files.writeString(bin.resolve("concurrent-qin-build.cmd"), """
                    @echo off
                    powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference = 'Stop'; New-Item -ItemType Directory -Force dist | Out-Null; if (Test-Path dist/running.txt) { Set-Content -Encoding UTF8 dist/violation.txt 'overlap'; exit 23 }; Set-Content -Encoding UTF8 dist/running.txt 'running'; Start-Sleep -Milliseconds 700; Remove-Item -Force dist/running.txt; Set-Content -Encoding UTF8 dist/marker.txt 'built'"
                    """, StandardCharsets.UTF_8);
            return;
        }
        Path tool = bin.resolve("concurrent-qin-build");
        Files.writeString(tool, """
                #!/bin/sh
                set -eu
                mkdir -p dist
                if [ -f dist/running.txt ]; then
                  printf overlap > dist/violation.txt
                  exit 23
                fi
                printf running > dist/running.txt
                sleep 1
                rm -f dist/running.txt
                printf built > dist/marker.txt
                """, StandardCharsets.UTF_8);
        if (!tool.toFile().setExecutable(true)) {
            throw new IllegalStateException("Unable to make concurrent-qin-build executable: " + tool);
        }
    }

    private static void writeDownstreamConfig(Path root, String dependencyPath) throws Exception {
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "%s",
                  version: "1.0.0",
                  dependencies: {
                    upstream: "file:%s"
                  },
                  scripts: {
                    test: %s
                  },
                  language: {
                    id: "%s",
                    extension: ".down",
                    parser: "index.ts"
                  }
                }
                """.formatted(root.getFileName(), dependencyPath, jsString(testScript("../upstream/dist/marker.txt")),
                root.getFileName()), StandardCharsets.UTF_8);
    }

    private static Thread dependencyBuildThread(
            Method method,
            Path downstream,
            CountDownLatch start,
            AtomicReference<Throwable> failure) {
        return new Thread(() -> {
            try {
                QinConfig config = new ConfigLoader(downstream.toString()).load();
                start.await();
                method.invoke(null, config, downstream, false);
            } catch (Throwable error) {
                failure.compareAndSet(null, error);
            }
        }, "qin-local-dependency-build-" + downstream.getFileName());
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
