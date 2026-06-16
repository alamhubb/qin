package com.qin.lang.backend.js;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.util.List;
import java.util.Set;

public interface QinIrCodeBackend {
    QinIrCodegenOptions options();

    default String fileExtension() {
        return options().fileExtension();
    }

    String compileProgram(QinIrProgram program);

    String compileProgram(QinIrProgram program, Set<String> externallyBoundJavaBinaryNames);

    String compileProgramWithExternalJavaSdk(
            QinIrProgram program,
            Set<String> externallyBoundJavaBinaryNames);

    String compileProgramWithExternalJavaSdk(
            QinIrProgram program,
            Set<String> externallyBoundJavaBinaryNames,
            List<QinIrClassDeclaration> contextClassDeclarations);
}
