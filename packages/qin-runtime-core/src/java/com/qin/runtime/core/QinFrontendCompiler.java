package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinSlimeFrontendAdapter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.module.policy.QinImportPolicyChecker;
import com.qin.lang.sema.esm.QinEsmLinkValidator;
import com.qin.lang.sema.esm.QinEsmSemanticAnalyzer;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.nio.file.Path;

/**
 * Frontend compiler from Qin source file to IR.
 */
public final class QinFrontendCompiler {
    private final QinSlimeFrontendAdapter adapter = new QinSlimeFrontendAdapter();
    private final QinModuleLinker moduleLinker = new QinModuleLinker();
    private final QinImportPolicyChecker importPolicyChecker = new QinImportPolicyChecker();
    private final QinEsmSemanticAnalyzer esmSemanticAnalyzer = new QinEsmSemanticAnalyzer();
    private final QinEsmLinkValidator esmLinkValidator = new QinEsmLinkValidator();

    public QinFrontendCompileResult compile(Path sourceFile, Path projectRoot) throws Exception {
        QinLinkedSource linkedSource = moduleLinker.link(sourceFile);
        importPolicyChecker.validate(projectRoot, linkedSource.imports());
        QinEsmSemanticModel semanticModel = esmSemanticAnalyzer.analyze(linkedSource.moduleGraph());
        esmLinkValidator.validate(semanticModel);
        QinIrProgram program = adapter.parseProgram(linkedSource.source());
        return new QinFrontendCompileResult(program, linkedSource, semanticModel);
    }
}
