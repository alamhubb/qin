package com.qin.conformance;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Path resolver helpers for baseline/exclusion/report files.
 */
public final class QinConformanceFiles {
    private static final String BASELINE_FILE = "conformance-baseline.json";
    private static final String EXCLUSIONS_FILE = "allowed-exclusions.json";

    private QinConformanceFiles() {
    }

    public static Path resolveBaseline(Path root) {
        for (Path candidate : baselineCandidates(root)) {
            if (Files.exists(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Missing conformance baseline file: " + BASELINE_FILE);
    }

    public static Path resolveExclusions(Path root) {
        for (Path candidate : exclusionsCandidates(root)) {
            if (Files.exists(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Missing conformance exclusions file: " + EXCLUSIONS_FILE);
    }

    public static Path resolveReportDir(Path root) {
        return root.resolve(".qin").resolve("conformance").toAbsolutePath().normalize();
    }

    private static List<Path> baselineCandidates(Path root) {
        return List.of(
                root.resolve(BASELINE_FILE),
                root.resolve("packages").resolve("qin-conformance").resolve(BASELINE_FILE),
                root.resolve("qin").resolve("packages").resolve("qin-conformance").resolve(BASELINE_FILE),
                Path.of("").toAbsolutePath().normalize().resolve(BASELINE_FILE),
                Path.of("").toAbsolutePath().normalize().resolve("packages").resolve("qin-conformance").resolve(BASELINE_FILE),
                Path.of("").toAbsolutePath().normalize().resolve("qin").resolve("packages").resolve("qin-conformance").resolve(BASELINE_FILE));
    }

    private static List<Path> exclusionsCandidates(Path root) {
        return List.of(
                root.resolve(EXCLUSIONS_FILE),
                root.resolve("packages").resolve("qin-conformance").resolve(EXCLUSIONS_FILE),
                root.resolve("qin").resolve("packages").resolve("qin-conformance").resolve(EXCLUSIONS_FILE),
                Path.of("").toAbsolutePath().normalize().resolve(EXCLUSIONS_FILE),
                Path.of("").toAbsolutePath().normalize().resolve("packages").resolve("qin-conformance").resolve(EXCLUSIONS_FILE),
                Path.of("").toAbsolutePath().normalize().resolve("qin").resolve("packages").resolve("qin-conformance").resolve(EXCLUSIONS_FILE));
    }
}
