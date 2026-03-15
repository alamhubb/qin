package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.sema.esm.QinEsmLinkValidator;
import com.qin.lang.sema.esm.QinEsmSemanticAnalyzer;
import com.qin.lang.sema.esm.QinEsmSemanticException;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.nio.file.Path;
import java.util.Map;

/**
 * Cycle and live-binding oriented semantic checks.
 */
public final class EsmCycleLiveBindingTestMain {
    private EsmCycleLiveBindingTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinEsmTestPaths.resolveStage1Root();
        QinModuleGraphBuilder graphBuilder = new QinModuleGraphBuilder();
        QinEsmSemanticAnalyzer analyzer = new QinEsmSemanticAnalyzer();
        QinEsmLinkValidator validator = new QinEsmLinkValidator();

        Path cycleEntry = root.resolve("shared/cycle-a.js").normalize().toAbsolutePath();
        QinModuleGraph cycleGraph = graphBuilder.build(cycleEntry);
        QinEsmSemanticModel cycleModel = analyzer.analyze(cycleGraph);
        validator.validate(cycleModel);

        Path invalidEntry = root.resolve("main/invalid-missing-export.js").normalize().toAbsolutePath();
        expectSemanticError(analyzer, validator, graphBuilder, invalidEntry, "ESM2003");
        verifyRuntimeCycleOk(root);
        verifyRuntimeCycleTdz(root);

        System.out.println("EsmCycleLiveBindingTestMain passed.");
        System.out.println("cycle modules: " + cycleModel.modules().size());
    }

    private static void verifyRuntimeCycleOk(Path root) throws Exception {
        Path source = root.resolve("main/runtime-cycle-ok.js").normalize().toAbsolutePath();
        Object runResult = new QinInMemoryJvmRunner()
                .compileAndRun(source, "com.qin.runtime.generated.EsmCycleRuntimeOk");
        if (!(runResult instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected map run result, got: " + runResult);
        }
        Object age = map.get("age");
        if (!(age instanceof Number number) || number.intValue() != 41) {
            throw new IllegalStateException("Expected result.age == 41, got: " + age);
        }
    }

    private static void verifyRuntimeCycleTdz(Path root) throws Exception {
        Path source = root.resolve("main/invalid-runtime-cycle-tdz.js").normalize().toAbsolutePath();
        try {
            new QinInMemoryJvmRunner()
                    .compileAndRun(source, "com.qin.runtime.generated.EsmCycleRuntimeTdz");
            throw new IllegalStateException("Expected runtime TDZ failure for cycle import access");
        } catch (Exception ex) {
            String text = collectMessages(ex);
            if (!text.contains("ReferenceError: Cannot access export before initialization")) {
                throw new IllegalStateException("Expected TDZ runtime error, got: " + text, ex);
            }
        }
    }

    private static void expectSemanticError(
            QinEsmSemanticAnalyzer analyzer,
            QinEsmLinkValidator validator,
            QinModuleGraphBuilder graphBuilder,
            Path entry,
            String code) throws Exception {
        try {
            QinEsmSemanticModel model = analyzer.analyze(graphBuilder.build(entry));
            validator.validate(model);
        } catch (QinEsmSemanticException ex) {
            boolean matched = ex.diagnostics().stream().anyMatch(d -> code.equals(d.code()));
            if (matched) {
                return;
            }
            throw new IllegalStateException("Expected code " + code + ", got: " + ex.getMessage(), ex);
        }
        throw new IllegalStateException("Expected semantic error " + code + " for " + entry.toAbsolutePath());
    }

    private static String collectMessages(Throwable throwable) {
        StringBuilder out = new StringBuilder();
        Throwable current = throwable;
        while (current != null) {
            if (current.getMessage() != null && !current.getMessage().isBlank()) {
                if (out.length() > 0) {
                    out.append(" | ");
                }
                out.append(current.getMessage());
            }
            current = current.getCause();
        }
        return out.toString();
    }
}
