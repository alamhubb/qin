package com.qin.conformance;

import com.qin.conformance.QinConformanceModels.Report;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CLI entry for Qin conformance runner.
 */
public final class QinConformanceMain {
    private QinConformanceMain() {
    }

    public static void main(String[] args) throws Exception {
        Options options = Options.parse(args);
        Path root = options.root().toAbsolutePath().normalize();

        Path baselineFile = QinConformanceFiles.resolveBaseline(root);
        Path exclusionsFile = QinConformanceFiles.resolveExclusions(root);
        QinConformanceBaseline baseline = QinConformanceIO.loadBaseline(baselineFile);
        QinConformanceExclusions exclusions = QinConformanceIO.loadExclusions(exclusionsFile);

        System.out.println("[Conformance] suite: " + baseline.suite());
        System.out.println("[Conformance] baseline: Chrome " + baseline.chrome().channel()
                + " (frozen " + baseline.chrome().frozenDate() + ")");
        System.out.println("[Conformance] root: " + root);

        Report report = new QinConformanceRunner(root, baseline, exclusions, options.chromeBinaryOverride()).run();

        Path reportDir = QinConformanceFiles.resolveReportDir(root);
        Path reportFile = reportDir.resolve("report-" + timestamp() + ".json");
        QinConformanceIO.writeReport(reportFile, report);

        System.out.println("[Conformance] report: " + reportFile.toAbsolutePath());
        System.out.println("[Conformance] total=" + report.summary().total()
                + ", matched=" + report.summary().matched()
                + ", mismatched=" + report.summary().mismatched()
                + ", excluded=" + report.summary().excluded()
                + ", qinFailed=" + report.summary().qinFailed()
                + ", chromeFailed=" + report.summary().chromeFailed());

        if (report.summary().mismatched() > 0) {
            System.exit(2);
        }
    }

    private static String timestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
    }

    private record Options(Path root, String chromeBinaryOverride) {
        private static Options parse(String[] args) {
            Path root = Path.of("").toAbsolutePath().normalize();
            String chrome = "";
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("--root=")) {
                    root = Path.of(arg.substring("--root=".length()).trim());
                    continue;
                }
                if ("--root".equals(arg) && i + 1 < args.length) {
                    root = Path.of(args[++i].trim());
                    continue;
                }
                if (arg.startsWith("--chrome=")) {
                    chrome = arg.substring("--chrome=".length()).trim();
                    continue;
                }
                if ("--chrome".equals(arg) && i + 1 < args.length) {
                    chrome = args[++i].trim();
                }
            }
            return new Options(root, chrome);
        }
    }
}
