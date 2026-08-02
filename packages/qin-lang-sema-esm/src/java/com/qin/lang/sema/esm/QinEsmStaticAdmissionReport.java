package com.qin.lang.sema.esm;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Structured report for a package shape rejected by Qin's static JVM subset.
 */
public record QinEsmStaticAdmissionReport(
        String packageName,
        Path packageRoot,
        Path sourceFile,
        String unsupportedShape,
        String staticLoweringReason,
        List<String> approvedChoices) {
    public QinEsmStaticAdmissionReport {
        Objects.requireNonNull(packageName, "packageName cannot be null");
        Objects.requireNonNull(packageRoot, "packageRoot cannot be null");
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        Objects.requireNonNull(unsupportedShape, "unsupportedShape cannot be null");
        Objects.requireNonNull(staticLoweringReason, "staticLoweringReason cannot be null");
        Objects.requireNonNull(approvedChoices, "approvedChoices cannot be null");
        approvedChoices = List.copyOf(approvedChoices);
    }
}
