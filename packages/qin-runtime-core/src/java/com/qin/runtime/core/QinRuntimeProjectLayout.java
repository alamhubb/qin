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
            "main/main.qin",
            "main/Main.qin",
            "main/main.js",
            "main/Main.java",
            "main/main.java",
            "src/Main.java");

    private static final List<String> SOURCE_CANDIDATES = List.of(
            "shared/main.qin",
            "shared/main.js",
            "shared/main.vue",
            "shared/main.ovs",
            "shared/main.mjs",
            "shared/main.ts",
            "shared/shared.qin",
            "shared/shared.js",
            "shared/shared.vue",
            "shared/shared.ovs",
            "shared/shared.mjs",
            "shared/shared.ts",
            "main/main.qin",
            "main/main.js",
            "main/main.vue",
            "main/main.mjs",
            "main/main.ts",
            "app/main.qin",
            "app/main.js",
            "app/main.vue",
            "app/main.ovs",
            "app/main.mjs",
            "app/main.ts",
            "src/main.qin");

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
