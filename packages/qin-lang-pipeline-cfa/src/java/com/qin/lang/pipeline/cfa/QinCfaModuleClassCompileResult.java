package com.qin.lang.pipeline.cfa;

import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public record QinCfaModuleClassCompileResult(
        Path projectRoot,
        Path sourceFile,
        QinLinkedModuleSource linkedSource,
        QinEsmSemanticModel semanticModel,
        QinCfaModuleClassFile initializerClass,
        List<QinCfaModuleClassFile> moduleClasses) {
    public QinCfaModuleClassCompileResult {
        Objects.requireNonNull(projectRoot, "projectRoot cannot be null");
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        Objects.requireNonNull(linkedSource, "linkedSource cannot be null");
        Objects.requireNonNull(semanticModel, "semanticModel cannot be null");
        Objects.requireNonNull(moduleClasses, "moduleClasses cannot be null");
        moduleClasses = List.copyOf(moduleClasses);
    }
}
