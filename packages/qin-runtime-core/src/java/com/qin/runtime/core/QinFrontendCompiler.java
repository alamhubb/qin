package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinCfaCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaPipeline;
import com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler;

import java.nio.file.Path;

/**
 * Compatibility frontend facade.
 * Internally delegates to qin-lang-pipeline-cfa semantic/IR stages.
 */
public final class QinFrontendCompiler {
    private final QinCfaPipeline cfaPipeline = new QinSlimeCfaCompiler();

    public QinFrontendCompileResult compile(Path sourceFile, Path projectRoot) throws Exception {
        QinCfaCompileResult result = cfaPipeline.compile(
                QinCfaCompileRequest.forAnalysis(sourceFile, projectRoot));
        return new QinFrontendCompileResult(
                result.irBeforeLowering(),
                toLinkedSource(result.linkedSource()),
                result.semanticModel(),
                result.astText());
    }

    private QinLinkedSource toLinkedSource(QinLinkedModuleSource linked) {
        return new QinLinkedSource(
                linked.entryFile(),
                linked.source(),
                linked.modules(),
                linked.imports(),
                linked.moduleGraph());
    }
}
