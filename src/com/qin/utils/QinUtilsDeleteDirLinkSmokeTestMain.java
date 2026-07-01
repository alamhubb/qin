package com.qin.utils;

import com.qin.constants.QinConstants;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinUtilsDeleteDirLinkSmokeTestMain {
    private QinUtilsDeleteDirLinkSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-delete-link-smoke-");
        Path source = Files.createDirectories(root.resolve("source"));
        Files.writeString(source.resolve("marker.txt"), "keep\n", StandardCharsets.UTF_8);
        Path link = root.resolve("link");
        createDirectoryLink(source, link);

        QinUtils.deleteDir(link);

        require(!Files.exists(link), "link removed");
        require(Files.isRegularFile(source.resolve("marker.txt")), "source directory preserved");
        System.out.println("QinUtilsDeleteDirLinkSmokeTestMain OK");
    }

    private static void createDirectoryLink(Path source, Path link) throws Exception {
        try {
            Files.createSymbolicLink(link, source);
            return;
        } catch (Exception error) {
            if (!QinConstants.isWindows()) {
                throw error;
            }
        }
        Process process = new ProcessBuilder(List.of(
                "cmd",
                "/c",
                "mklink",
                "/J",
                link.toString(),
                source.toString()))
                .redirectErrorStream(true)
                .start();
        String output;
        try (InputStream input = process.getInputStream()) {
            output = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("Failed to create test junction: " + output);
        }
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
