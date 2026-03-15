package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Resolves source paths from convention layout and CLI request.
 */
public final class QinSourceResolver {
    public Path resolveRoot(Path rootDir) {
        if (rootDir != null) {
            return rootDir.toAbsolutePath().normalize();
        }
        return Path.of("").toAbsolutePath().normalize();
    }

    public Path resolveSourceFile(QinBuildRequest request, QinRuntimeProjectLayout layout) {
        if (request.sourceFile() != null) {
            Path file = request.sourceFile();
            if (!file.isAbsolute()) {
                file = layout.root().resolve(file).normalize();
            }
            requireFile(file, "--file");
            return file;
        }

        Path detected = layout.resolveDefaultSource();
        if (detected != null) {
            return detected;
        }

        throw new IllegalArgumentException(
                "No Qin source found. Use --file, or provide one of: shared/main.(js|mjs|ts), shared/shared.(js|mjs|ts), main/main.(js|mjs|ts), app/main.(js|mjs|ts)");
    }

    private void requireFile(Path file, String from) {
        Objects.requireNonNull(file, "file cannot be null");
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            throw new IllegalArgumentException("Missing source file from " + from + ": " + file.toAbsolutePath());
        }
    }
}
