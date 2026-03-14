package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.sema.esm.QinEsmLinkValidator;
import com.qin.lang.sema.esm.QinEsmSemanticAnalyzer;
import com.qin.lang.sema.esm.QinEsmSemanticException;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.nio.file.Path;

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

        System.out.println("EsmCycleLiveBindingTestMain passed.");
        System.out.println("cycle modules: " + cycleModel.modules().size());
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
}

