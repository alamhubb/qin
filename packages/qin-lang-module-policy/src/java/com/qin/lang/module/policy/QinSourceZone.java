package com.qin.lang.module.policy;

import java.nio.file.Path;

/**
 * Source zone used by Qin module import policy.
 */
public enum QinSourceZone {
    FRONTEND,
    BACKEND,
    SHARED,
    UNKNOWN;

    public static QinSourceZone detect(Path projectRoot, Path sourceFile) {
        if (sourceFile == null) {
            return UNKNOWN;
        }
        Path normalizedSource = sourceFile.toAbsolutePath().normalize();
        if (projectRoot == null) {
            return detectByFirstSegment(normalizedSource);
        }

        Path normalizedRoot = projectRoot.toAbsolutePath().normalize();
        if (!normalizedSource.startsWith(normalizedRoot)) {
            return detectByFirstSegment(normalizedSource);
        }

        Path relative = normalizedRoot.relativize(normalizedSource);
        if (relative.getNameCount() == 0) {
            return UNKNOWN;
        }
        String first = relative.getName(0).toString();
        return switch (first) {
            case "app" -> FRONTEND;
            case "main" -> BACKEND;
            case "shared" -> SHARED;
            default -> UNKNOWN;
        };
    }

    private static QinSourceZone detectByFirstSegment(Path sourceFile) {
        if (sourceFile.getNameCount() == 0) {
            return UNKNOWN;
        }
        String first = sourceFile.getName(0).toString();
        return switch (first) {
            case "app" -> FRONTEND;
            case "main" -> BACKEND;
            case "shared" -> SHARED;
            default -> UNKNOWN;
        };
    }
}
