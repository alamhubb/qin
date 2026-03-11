package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinSlimeFrontendAdapter;
import com.qin.lang.ir.QinIrProgram;

import java.nio.file.Path;

/**
 * Frontend compiler from Qin source file to IR.
 */
public final class QinFrontendCompiler {
    private final QinSlimeFrontendAdapter adapter = new QinSlimeFrontendAdapter();
    private final QinModuleLinker moduleLinker = new QinModuleLinker();

    public QinIrProgram compile(Path sourceFile) throws Exception {
        QinLinkedSource linkedSource = moduleLinker.link(sourceFile);
        return adapter.parseProgram(linkedSource.source());
    }
}
