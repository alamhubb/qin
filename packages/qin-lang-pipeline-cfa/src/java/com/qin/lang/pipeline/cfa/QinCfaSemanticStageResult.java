package com.qin.lang.pipeline.cfa;

import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.util.Objects;

/**
 * Output of module-link + ESM semantic stage.
 */
public record QinCfaSemanticStageResult(
        QinLinkedModuleSource linkedSource,
        QinEsmSemanticModel semanticModel) {
    public QinCfaSemanticStageResult {
        Objects.requireNonNull(linkedSource, "linkedSource cannot be null");
        Objects.requireNonNull(semanticModel, "semanticModel cannot be null");
    }
}
