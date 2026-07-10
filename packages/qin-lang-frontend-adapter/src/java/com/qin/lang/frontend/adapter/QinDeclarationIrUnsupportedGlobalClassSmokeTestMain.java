package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrProgram;

/**
 * Proves unsupported JS globals do not enter the static JVM declaration subset.
 */
public final class QinDeclarationIrUnsupportedGlobalClassSmokeTestMain {
    private QinDeclarationIrUnsupportedGlobalClassSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                export class ClockValue {
                  constructor(value) {
                    this.value = value instanceof Date ? value : new Date()
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        if (!program.classDeclarations().isEmpty()) {
            throw new IllegalStateException("Unsupported Date class should not lower to static declaration class");
        }

        System.out.println("QinDeclarationIrUnsupportedGlobalClassSmokeTestMain passed.");
    }
}
