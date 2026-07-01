package com.qin.core;

import com.qin.types.CompileResult;
import com.qin.types.QinConfig;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;

public final class JavaRunnerIncrementalMissingClassSmokeTestMain {
    private JavaRunnerIncrementalMissingClassSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-java-incremental-missing-class-");
        Path sourceDir = root.resolve("src");
        Path outputDir = root.resolve("build/classes");
        Files.createDirectories(sourceDir);
        Files.createDirectories(outputDir);

        Path oldSource = sourceDir.resolve("OldMain.java");
        String oldText = """
                public final class OldMain {
                  public static void main(String[] args) {
                    System.out.println("old");
                  }
                }
                """;
        Files.writeString(oldSource, oldText, StandardCharsets.UTF_8);
        Path newSource = sourceDir.resolve("NewMain.java");
        String newText = """
                public final class NewMain {
                  public static String value() {
                    return "new";
                  }
                }
                """;
        Files.writeString(newSource, newText, StandardCharsets.UTF_8);

        Files.write(outputDir.resolve("OldMain.class"), new byte[] {
                (byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe
        });
        Files.createDirectories(root.resolve(".qin"));
        Files.writeString(root.resolve(".qin/compile-cache.json"), """
                {
                  "files": {
                    "src/OldMain.java": "%s",
                    "src/NewMain.java": "%s"
                  },
                  "lastCompileTime": 1
                }
                """.formatted(md5(oldText), md5(newText)), StandardCharsets.UTF_8);
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "incremental-missing-class-smoke",
                  version: "0.1.0",
                  entry: "src/OldMain.java",
                  java: {
                    sourceDir: "src",
                    outputDir: "build/classes",
                    encoding: "UTF-8"
                  }
                }
                """, StandardCharsets.UTF_8);

        QinConfig config = new ConfigLoader(root.toString()).load();

        CompileResult result = new JavaRunner(config, "", root.toString()).compile();
        if (!result.isSuccess()) {
            throw new IllegalStateException("Incremental compile failed: " + result.getError());
        }
        if (result.getCompiledFiles() == 0) {
            throw new IllegalStateException("Incremental compile skipped despite missing NewMain.class");
        }
        if (!Files.isRegularFile(outputDir.resolve("NewMain.class"))) {
            throw new IllegalStateException("Expected NewMain.class to be generated");
        }

        System.out.println("JavaRunnerIncrementalMissingClassSmokeTestMain OK");
    }

    private static String md5(String value) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
    }
}
