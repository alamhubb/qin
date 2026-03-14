package com.qin.runtime.core;

import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrProgram;

import java.util.List;

/**
 * Validates IR rules and policy constraints before backend emission.
 */
public final class QinIrValidator {
    private final QinJdkInteropPolicy jdkInteropPolicy;

    public QinIrValidator() {
        this(new QinJdkInteropPolicy());
    }

    public QinIrValidator(QinJdkInteropPolicy jdkInteropPolicy) {
        this.jdkInteropPolicy = jdkInteropPolicy;
    }

    public void validate(QinIrProgram program, QinBuildTarget target) {
        validateProgramNotEmpty(program);
        validateJavaImportPolicy(program.javaImports());
        validateTargetImportPolicy(program, target);
    }

    private void validateProgramNotEmpty(QinIrProgram program) {
        if (program.declarations().isEmpty()
                && program.consoleLogs().isEmpty()
                && program.javaStaticConsoleLogs().isEmpty()
                && program.javaInstanceMethodCalls().isEmpty()
                && program.javaInstanceConsoleLogs().isEmpty()) {
            throw new IllegalArgumentException("Program contains no supported executable statements");
        }
    }

    private void validateJavaImportPolicy(List<QinIrJavaImport> javaImports) {
        for (QinIrJavaImport javaImport : javaImports) {
            String module = javaImport.moduleName();
            if (!module.startsWith("java:")) {
                throw new IllegalArgumentException("Unsupported import module scheme: " + module);
            }
            String javaModule = module.substring("java:".length());
            if (!jdkInteropPolicy.isModuleAllowed(javaModule)) {
                throw new IllegalArgumentException("java import module is not allowed: " + module);
            }
        }
    }

    private void validateTargetImportPolicy(QinIrProgram program, QinBuildTarget target) {
        if (target.emitJvm() && !program.jsImports().isEmpty()) {
            throw new IllegalArgumentException(
                    "JVM target does not support js imports. Found: " + program.jsImports().get(0).moduleName());
        }
    }
}
