package com.qin.lang.pipeline.cfa;

import com.qin.lang.module.policy.QinImportPolicyChecker;
import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.module.resolver.QinLinkedModuleSourceEmitter;
import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.sema.esm.QinEsmLinkValidator;
import com.qin.lang.sema.esm.QinEsmRuntimeFeatureValidator;
import com.qin.lang.sema.esm.QinEsmSemanticAnalyzer;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Stage 1: module graph/link + semantic checks.
 */
public final class QinCfaSemanticStage {
    private final QinModuleGraphBuilder moduleGraphBuilder;
    private final QinLinkedModuleSourceEmitter linkedSourceEmitter;
    private final QinImportPolicyChecker importPolicyChecker;
    private final QinEsmRuntimeFeatureValidator runtimeFeatureValidator;
    private final QinEsmSemanticAnalyzer semanticAnalyzer;
    private final QinEsmLinkValidator linkValidator;

    public QinCfaSemanticStage() {
        this(new QinModuleGraphBuilder(),
                new QinLinkedModuleSourceEmitter(),
                new QinImportPolicyChecker(),
                new QinEsmRuntimeFeatureValidator(),
                new QinEsmSemanticAnalyzer(),
                new QinEsmLinkValidator());
    }

    public QinCfaSemanticStage(
            QinModuleGraphBuilder moduleGraphBuilder,
            QinLinkedModuleSourceEmitter linkedSourceEmitter,
            QinImportPolicyChecker importPolicyChecker,
            QinEsmRuntimeFeatureValidator runtimeFeatureValidator,
            QinEsmSemanticAnalyzer semanticAnalyzer,
            QinEsmLinkValidator linkValidator) {
        this.moduleGraphBuilder = Objects.requireNonNull(moduleGraphBuilder, "moduleGraphBuilder cannot be null");
        this.linkedSourceEmitter = Objects.requireNonNull(linkedSourceEmitter, "linkedSourceEmitter cannot be null");
        this.importPolicyChecker = Objects.requireNonNull(importPolicyChecker, "importPolicyChecker cannot be null");
        this.runtimeFeatureValidator = Objects.requireNonNull(runtimeFeatureValidator,
                "runtimeFeatureValidator cannot be null");
        this.semanticAnalyzer = Objects.requireNonNull(semanticAnalyzer, "semanticAnalyzer cannot be null");
        this.linkValidator = Objects.requireNonNull(linkValidator, "linkValidator cannot be null");
    }

    public QinCfaSemanticStageResult execute(Path sourceFile, Path projectRoot) throws Exception {
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        Objects.requireNonNull(projectRoot, "projectRoot cannot be null");

        long startNanos = System.nanoTime();
        logPhase("module graph start", startNanos, sourceFile.toString());
        QinModuleGraph moduleGraph = moduleGraphBuilder.build(sourceFile);
        logPhase("module graph done", startNanos, "modules=" + moduleGraph.modules().size());
        logPhase("linked source start", startNanos, sourceFile.toString());
        QinLinkedModuleSource linkedSource = linkedSourceEmitter.emit(moduleGraph);
        logPhase("linked source done", startNanos, "chars=" + linkedSource.source().length());

        logPhase("policy start", startNanos, projectRoot.toString());
        importPolicyChecker.validate(projectRoot, linkedSource.imports());
        logPhase("policy done", startNanos, "imports=" + linkedSource.imports().size());
        logPhase("runtime feature start", startNanos, sourceFile.toString());
        runtimeFeatureValidator.validate(moduleGraph);
        logPhase("runtime feature done", startNanos, sourceFile.toString());

        logPhase("sema start", startNanos, sourceFile.toString());
        QinEsmSemanticModel semanticModel = semanticAnalyzer.analyze(moduleGraph);
        linkValidator.validate(semanticModel);
        logPhase("sema done", startNanos, sourceFile.toString());
        return new QinCfaSemanticStageResult(linkedSource, semanticModel);
    }

    private void logPhase(String phase, long startNanos, String detail) {
        long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000L;
        System.out.println("[QinCfaSemanticStage] " + phase + " +" + elapsedMs + "ms :: " + detail);
    }
}
