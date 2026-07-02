package com.qin.core;

import com.qin.types.CompileResult;
import com.qin.types.QinConfig;

import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class JavaRunnerCompileMetadataSmokeTestMain {
    private JavaRunnerCompileMetadataSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-java-compile-metadata-");
        Path sourceDir = root.resolve("src");
        Path outputDir = root.resolve("build/classes");
        Files.createDirectories(sourceDir);

        Files.writeString(sourceDir.resolve("Main.java"), """
                public final class Main {
                  public static String value() {
                    return "metadata";
                  }
                }
                """, StandardCharsets.UTF_8);

        writeConfig(root, "17");
        CompileResult first = new JavaRunner(loadConfig(root), "", root.toString()).compile();
        if (!first.isSuccess()) {
            throw new IllegalStateException("Initial compile failed: " + first.getError());
        }
        assertClassMajor(outputDir.resolve("Main.class"), 61);

        writeConfig(root, "21");
        CompileResult second = new JavaRunner(loadConfig(root), "", root.toString()).compile();
        if (!second.isSuccess()) {
            throw new IllegalStateException("Metadata-triggered compile failed: " + second.getError());
        }
        if (second.getCompiledFiles() == 0) {
            throw new IllegalStateException("Compile skipped after java.release changed");
        }
        assertClassMajor(outputDir.resolve("Main.class"), 65);

        System.out.println("JavaRunnerCompileMetadataSmokeTestMain OK");
    }

    private static QinConfig loadConfig(Path root) throws Exception {
        return new ConfigLoader(root.toString()).load();
    }

    private static void writeConfig(Path root, String release) throws Exception {
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "compile-metadata-smoke",
                  version: "0.1.0",
                  entry: "src/Main.java",
                  java: {
                    version: "21",
                    release: "%s",
                    sourceDir: "src",
                    outputDir: "build/classes",
                    encoding: "UTF-8"
                  }
                }
                """.formatted(release), StandardCharsets.UTF_8);
    }

    private static void assertClassMajor(Path classFile, int expectedMajor) throws Exception {
        int actualMajor = readClassMajor(classFile);
        if (actualMajor != expectedMajor) {
            throw new IllegalStateException(
                    "Expected " + classFile + " major " + expectedMajor + " but got " + actualMajor);
        }
    }

    private static int readClassMajor(Path classFile) throws Exception {
        try (InputStream input = Files.newInputStream(classFile);
                DataInputStream data = new DataInputStream(input)) {
            int magic = data.readInt();
            if (magic != 0xCAFEBABE) {
                throw new IllegalStateException("Not a class file: " + classFile);
            }
            data.readUnsignedShort();
            return data.readUnsignedShort();
        }
    }
}
