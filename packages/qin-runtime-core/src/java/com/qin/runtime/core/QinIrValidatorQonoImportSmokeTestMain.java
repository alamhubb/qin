package com.qin.runtime.core;

import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrProgram;

import java.util.List;

/**
 * Keeps the backend JVM java: import policy open for the standalone Qono package.
 */
public final class QinIrValidatorQonoImportSmokeTestMain {
    private QinIrValidatorQonoImportSmokeTestMain() {
    }

    public static void main(String[] args) {
        QinIrProgram program = new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:com.qin.qono",
                        "Qono",
                        "Qono",
                        "com.qin.qono.Qono")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        new QinIrValidator().validate(program, QinBuildTarget.JVM);
        System.out.println("QinIrValidatorQonoImportSmokeTestMain passed.");
    }
}
