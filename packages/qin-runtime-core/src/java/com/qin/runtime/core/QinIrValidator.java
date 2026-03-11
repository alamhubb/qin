package com.qin.runtime.core;

import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrProgram;

import java.util.List;
import java.util.Set;

/**
 * Validates IR rules and policy constraints before backend emission.
 */
public final class QinIrValidator {
    private static final Set<String> ALLOWED_JAVA_MODULE_PREFIXES = Set.of(
            "java.lang",
            "java.util",
            "java.nio.file");

    public void validate(QinIrProgram program) {
        validateProgramNotEmpty(program);
        validateJavaImportPolicy(program.javaImports());
    }

    private void validateProgramNotEmpty(QinIrProgram program) {
        if (program.declarations().isEmpty()
                && program.consoleLogs().isEmpty()
                && program.javaStaticConsoleLogs().isEmpty()) {
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
            boolean allowed = ALLOWED_JAVA_MODULE_PREFIXES.stream()
                    .anyMatch(prefix -> javaModule.equals(prefix) || javaModule.startsWith(prefix + "."));
            if (!allowed) {
                throw new IllegalArgumentException("java import module is not allowed: " + module);
            }
        }
    }
}
