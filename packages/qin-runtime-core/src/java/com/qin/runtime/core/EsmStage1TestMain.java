package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.sema.esm.QinEsmLinkValidator;
import com.qin.lang.sema.esm.QinEsmModuleSemantic;
import com.qin.lang.sema.esm.QinEsmSemanticAnalyzer;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Stage-1 ESM semantic smoke test.
 */
public final class EsmStage1TestMain {
    private EsmStage1TestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinEsmTestPaths.resolveStage1Root();
        Path entry = root.resolve("main/main.js").normalize();
        if (!Files.exists(entry)) {
            throw new IllegalArgumentException("Missing entry file: " + entry.toAbsolutePath());
        }

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);
        new QinEsmLinkValidator().validate(model);

        QinEsmModuleSemantic entrySemantic = model.modules().get(entry.toAbsolutePath().normalize());
        if (entrySemantic == null) {
            throw new IllegalStateException("Entry module semantic missing.");
        }
        if (entrySemantic.imports().isEmpty()) {
            throw new IllegalStateException("Expected imports in stage1 entry module.");
        }

        System.out.println("EsmStage1TestMain passed.");
        System.out.println("root: " + root.toAbsolutePath());
        System.out.println("modules: " + model.modules().size());
        System.out.println("entry imports: " + entrySemantic.imports().size());
    }
}

