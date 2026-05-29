package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.List;

/**
 * Parity smoke test between JVM and JS build targets.
 */
public final class EsmBackendParityTestMain {
    private EsmBackendParityTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinEsmTestPaths.resolveStage1Root();
        QinBuildCoordinator coordinator = new QinBuildCoordinator();

        List<TestCase> cases = List.of(
                new TestCase("valid-main", "main/runtime-ok.js", true, null),
                new TestCase("valid-star-same-binding", "main/runtime-star-same-binding.js", true, null),
                new TestCase("valid-namespace-ambiguous", "main/runtime-namespace-ambiguous.js", true, null),
                new TestCase("valid-export-star-as-ns", "main/runtime-export-star-as-ns.js", true, null),
                new TestCase("valid-intermediate-amb-ok", "main/runtime-intermediate-amb-ok.js", true, null),
                new TestCase("valid-chain-reexport", "main/runtime-chain-reexport.js", true, null),
                new TestCase("valid-cycle-runtime-ok", "main/runtime-cycle-ok.js", true, null),
                new TestCase("invalid-ambiguous-export", "main/invalid-ambiguous-export.js", false, "ESM2004"),
                new TestCase("invalid-intermediate-ambiguous-val", "main/invalid-intermediate-ambiguous-val.js", false, "ESM2004"),
                new TestCase("invalid-import-meta", "main/invalid-import-meta.js", true, null),
                new TestCase("invalid-top-level-await", "main/invalid-top-level-await.js", true, null));

        for (TestCase testCase : cases) {
            Outcome jvm = runBuild(coordinator, root, testCase, QinBuildTarget.JVM);
            Outcome js = runBuild(coordinator, root, testCase, QinBuildTarget.JS);
            assertParity(testCase, jvm, js);
        }

        System.out.println("EsmBackendParityTestMain passed.");
        System.out.println("cases: " + cases.size());
    }

    private static Outcome runBuild(
            QinBuildCoordinator coordinator,
            Path root,
            TestCase testCase,
            QinBuildTarget target) {
        Path source = root.resolve(testCase.relativeSource).normalize();
        Path classOut = root.resolve("build/parity/classes/" + target.name().toLowerCase()).normalize();
        Path jsOut = root.resolve("build/parity/js/" + target.name().toLowerCase() + "/app.js").normalize();
        QinBuildRequest request = new QinBuildRequest(
                root,
                source,
                target,
                "com.qin.runtime.generated.parity." + target.name() + "." + sanitize(testCase.name),
                classOut,
                jsOut,
                false);
        try {
            coordinator.build(request);
            if (!testCase.expectSuccess) {
                throw new IllegalStateException("Expected failure but succeeded: " + testCase.name + " [" + target + "]");
            }
            return Outcome.successResult();
        } catch (Exception ex) {
            if (testCase.expectSuccess) {
                throw new IllegalStateException("Expected success but failed: " + testCase.name + " [" + target + "]", ex);
            }
            String code = extractCode(ex.getMessage());
            if (testCase.expectedCode != null && !testCase.expectedCode.equals(code)) {
                throw new IllegalStateException(
                        "Expected code " + testCase.expectedCode + ", got " + code + " [" + target + "]", ex);
            }
            return Outcome.failureResult(code);
        }
    }

    private static void assertParity(TestCase testCase, Outcome jvm, Outcome js) {
        if (jvm.success != js.success) {
            throw new IllegalStateException(
                    "Parity mismatch success flag: " + testCase.name + ", jvm=" + jvm + ", js=" + js);
        }
        if (!jvm.success && !js.success) {
            String left = jvm.errorCode == null ? "" : jvm.errorCode;
            String right = js.errorCode == null ? "" : js.errorCode;
            if (!left.equals(right)) {
                throw new IllegalStateException(
                        "Parity mismatch error code: " + testCase.name + ", jvm=" + left + ", js=" + right);
            }
        }
    }

    private static String sanitize(String text) {
        return text.replaceAll("[^A-Za-z0-9]", "_");
    }

    private static String extractCode(String message) {
        if (message == null || message.isBlank()) {
            return null;
        }
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(ESM\\d{4}|QIN\\d{4})")
                .matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private record TestCase(String name, String relativeSource, boolean expectSuccess, String expectedCode) {
    }

    private record Outcome(boolean success, String errorCode) {
        static Outcome successResult() {
            return new Outcome(true, null);
        }

        static Outcome failureResult(String errorCode) {
            return new Outcome(false, errorCode);
        }
    }
}
