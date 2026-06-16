package com.qin.runtime.core;

import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrProgram;

import java.util.List;

/**
 * Keeps the backend JVM java: import policy open for Qin's Java SlimeParser route.
 */
public final class QinIrValidatorJavaSlimeParserImportSmokeTestMain {
    private QinIrValidatorJavaSlimeParserImportSmokeTestMain() {
    }

    public static void main(String[] args) {
        QinIrProgram program = new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:com.slime.parser",
                        "SlimeParser",
                        "SlimeParser",
                        "com.slime.parser.SlimeParser")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        new QinIrValidator().validate(program, QinBuildTarget.JVM);
        System.out.println("QinIrValidatorJavaSlimeParserImportSmokeTestMain passed.");
    }
}
