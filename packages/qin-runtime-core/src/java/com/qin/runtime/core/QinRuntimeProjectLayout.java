package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Convention-based project layout for Qin runtime projects.
 */
public record QinRuntimeProjectLayout(
        Path root,
        Path sharedDir,
        Path appDir,
        Path mainDir,
        Path backendEntry) {

    private static final List<String> BACKEND_ENTRY_CANDIDATES = List.of(
            "main/Main.java",
            "main/main.java",
            "src/Main.java");

    private static final List<String> SOURCE_CANDIDATES = List.of(
            "shared/main.js",
            "shared/shared.js",
            "main/main.js",
            "app/main.js");

    public static QinRuntimeProjectLayout discover(Path rootDir) {
        Path root = rootDir.toAbsolutePath().normalize();
        Path shared = root.resolve("shared");
        Path app = root.resolve("app");
        Path main = root.resolve("main");

        Path backendEntry = null;
        for (String candidate : BACKEND_ENTRY_CANDIDATES) {
            Path file = root.resolve(candidate).normalize();
            if (Files.exists(file) && Files.isRegularFile(file)) {
                backendEntry = file;
                break;
            }
        }
        if (backendEntry == null) {
            backendEntry = root.resolve("main/Main.java").normalize();
        }

        return new QinRuntimeProjectLayout(root, shared, app, main, backendEntry);
    }

    public Path resolveDefaultSource() {
        for (String candidate : SOURCE_CANDIDATES) {
            Path file = root.resolve(candidate).normalize();
            if (Files.exists(file) && Files.isRegularFile(file)) {
                return file;
            }
        }
        return null;
    }
}
