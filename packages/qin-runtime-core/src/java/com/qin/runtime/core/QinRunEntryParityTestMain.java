package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies run-entry parity wiring between qin run convention and Java launcher.
 */
public final class QinRunEntryParityTestMain {
    private QinRunEntryParityTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = resolveFullstackMvpRoot();
        Path config = root.resolve("qin.config.json").normalize();
        Path launcher = root.resolve("launcher/com/qin/demo/FullstackApplication.java").normalize();
        Path qinBackendEntry = root.resolve("main/main.js").normalize();

        requireFile(config, "qin.config.json");
        requireFile(launcher, "launcher");
        requireFile(qinBackendEntry, "main/main.js");

        String configText = Files.readString(config, StandardCharsets.UTF_8);
        String launcherText = Files.readString(launcher, StandardCharsets.UTF_8);
        if (!configText.contains("\"entry\"")) {
            throw new IllegalStateException("qin.config.json missing entry field");
        }
        if (!launcherText.contains("QinRuntimeApi.runFullstack")) {
            throw new IllegalStateException("launcher does not delegate to QinRuntimeApi");
        }

        QinRuntimeProjectLayout layout = QinRuntimeProjectLayout.discover(root);
        if (!Files.exists(layout.mainDir())) {
            throw new IllegalStateException("project layout missing main directory");
        }

        System.out.println("QinRunEntryParityTestMain passed.");
        System.out.println("project: " + root.toAbsolutePath());
        System.out.println("launcher: " + launcher.toAbsolutePath());
        System.out.println("qin backend entry: " + qinBackendEntry.toAbsolutePath());
    }

    private static Path resolveFullstackMvpRoot() {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[] {
                cwd.resolve("packages/qin-runtime-core/examples/fullstack-mvp"),
                cwd.resolve("qin/packages/qin-runtime-core/examples/fullstack-mvp"),
                cwd.resolve("examples/fullstack-mvp")
        };
        for (Path candidate : candidates) {
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Cannot locate examples/fullstack-mvp directory.");
    }

    private static void requireFile(Path file, String label) {
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            throw new IllegalArgumentException("Missing " + label + ": " + file.toAbsolutePath());
        }
    }
}

