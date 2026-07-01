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
        validateJavaImportPolicy(program.javaImports());
        // JS imports are now allowed for backend(main/) modules. Target-specific
        // execution constraints are handled by runtime/lowering phases.
    }

    private void validateJavaImportPolicy(List<QinIrJavaImport> javaImports) {
        for (QinIrJavaImport javaImport : javaImports) {
            String module = javaImport.moduleName();
            if (!module.startsWith("java:")) {
                throw new IllegalArgumentException("Unsupported import module scheme: " + module);
            }
            String javaModule = module.substring("java:".length());
            if (!jdkInteropPolicy.isModuleAllowed(javaModule) && !isResolvableJavaImport(javaImport)) {
                throw new IllegalArgumentException("java import module is not allowed: " + module);
            }
        }
    }

    private boolean isResolvableJavaImport(QinIrJavaImport javaImport) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader != null) {
                Class.forName(javaImport.ownerBinaryName(), false, classLoader);
            } else {
                Class.forName(javaImport.ownerBinaryName());
            }
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
}
