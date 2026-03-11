package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinSlimeFrontendAdapter;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Frontend compiler from Qin source file to IR.
 */
public final class QinFrontendCompiler {
    private final QinSlimeFrontendAdapter adapter = new QinSlimeFrontendAdapter();

    public QinIrProgram compile(Path sourceFile) throws Exception {
        String source = Files.readString(sourceFile, StandardCharsets.UTF_8);
        return adapter.parseProgram(source);
    }
}
