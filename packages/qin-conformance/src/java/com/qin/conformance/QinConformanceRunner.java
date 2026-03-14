package com.qin.conformance;

import com.qin.conformance.QinConformanceBaseline.ConformanceCase;
import com.qin.conformance.QinConformanceModels.CaseExecution;
import com.qin.conformance.QinConformanceModels.CaseReport;
import com.qin.conformance.QinConformanceModels.Report;
import com.qin.conformance.QinConformanceModels.Summary;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Executes Qin-vs-Chrome conformance cases.
 */
public final class QinConformanceRunner {
    private final Path root;
    private final QinConformanceBaseline baseline;
    private final QinConformanceExclusions exclusions;
    private final String chromeBinaryOverride;

    public QinConformanceRunner(
            Path root,
            QinConformanceBaseline baseline,
            QinConformanceExclusions exclusions,
            String chromeBinaryOverride) {
        this.root = root.toAbsolutePath().normalize();
        this.baseline = baseline;
        this.exclusions = exclusions;
        this.chromeBinaryOverride = chromeBinaryOverride;
    }

    public Report run() {
        String chromeBinary = QinChromeRunner.resolveChromeBinary(
                baseline.chrome().binaryCandidates(),
                chromeBinaryOverride);

        List<CaseReport> caseReports = new ArrayList<>();
        int matched = 0;
        int mismatched = 0;
        int excluded = 0;
        int qinFailed = 0;
        int chromeFailed = 0;

        int index = 1;
        for (ConformanceCase testCase : baseline.cases()) {
            Path entryFile = resolveEntry(testCase.entry());
            String className = "com.qin.conformance.generated.Case" + index++;

            CaseExecution qin = QinJvmRunner.run(resolveProjectRoot(entryFile), entryFile, className);
            CaseExecution chrome = QinChromeRunner.run(entryFile, chromeBinary);

            boolean sameStatus = qin.status().equals(chrome.status());
            boolean sameError = normalizeError(qin.errorType()).equals(normalizeError(chrome.errorType()));
            boolean match = sameStatus && (qin.status().equals("PASS") || sameError);
            boolean isExcluded = !match && exclusions.isExcluded(testCase.id());
            String exclusionReason = isExcluded ? exclusions.reasonOf(testCase.id()) : "";

            if ("FAIL".equals(qin.status())) {
                qinFailed++;
            }
            if ("FAIL".equals(chrome.status())) {
                chromeFailed++;
            }

            if (match) {
                matched++;
            } else if (isExcluded) {
                excluded++;
            } else {
                mismatched++;
            }

            caseReports.add(new CaseReport(
                    testCase.id(),
                    entryFile.toString(),
                    qin,
                    chrome,
                    match,
                    isExcluded,
                    exclusionReason));
        }

        Summary summary = new Summary(
                caseReports.size(),
                matched,
                mismatched,
                excluded,
                qinFailed,
                chromeFailed);

        return new Report(
                Instant.now().toString(),
                baseline.suite(),
                baseline.chrome().channel(),
                baseline.chrome().frozenDate(),
                chromeBinary,
                root.toString(),
                summary,
                caseReports);
    }

    private Path resolveEntry(String entry) {
        Set<Path> candidates = new LinkedHashSet<>();
        String normalizedEntry = entry.replace("\\", "/");

        candidates.add(root.resolve(normalizedEntry).normalize());
        candidates.add(Path.of("").toAbsolutePath().normalize().resolve(normalizedEntry).normalize());

        if (normalizedEntry.startsWith("qin/")) {
            String withoutQinPrefix = normalizedEntry.substring("qin/".length());
            candidates.add(root.resolve(withoutQinPrefix).normalize());
            candidates.add(Path.of("").toAbsolutePath().normalize().resolve(withoutQinPrefix).normalize());
        }

        Path rootParent = root.getParent();
        if (rootParent != null) {
            candidates.add(rootParent.resolve(normalizedEntry).normalize());
            if (normalizedEntry.startsWith("qin/")) {
                String withoutQinPrefix = normalizedEntry.substring("qin/".length());
                candidates.add(rootParent.resolve(withoutQinPrefix).normalize());
            }
        }

        for (Path candidate : candidates) {
            if (java.nio.file.Files.exists(candidate)) {
                return candidate.toAbsolutePath().normalize();
            }
        }

        throw new IllegalArgumentException("Conformance case entry does not exist: " + entry);
    }

    private Path resolveProjectRoot(Path entry) {
        Path current = entry.getParent();
        while (current != null) {
            if (java.nio.file.Files.isDirectory(current.resolve("main"))
                    && java.nio.file.Files.isDirectory(current.resolve("shared"))) {
                return current;
            }
            if (java.nio.file.Files.exists(current.resolve("qin.config.json"))) {
                return current;
            }
            current = current.getParent();
        }
        return entry.getParent();
    }

    private String normalizeError(String error) {
        if (error == null || error.isBlank()) {
            return "";
        }
        return error.trim().toLowerCase(Locale.ROOT);
    }
}
