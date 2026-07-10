package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for Qin-owned parser decorators that lower statically.
 */
public final class QinDeclarationIrQinOwnedDecoratorSmokeTestMain {
    private QinDeclarationIrQinOwnedDecoratorSmokeTestMain() {
    }

    public static void main(String[] args) {
        String source = """
                @Subhuti
                class ParserProbe {
                  @SubhutiRule
                  parse() {
                    return null
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(source);
        QinIrClassDeclaration declaration = program.classDeclarations().stream()
                .filter(candidate -> "ParserProbe".equals(candidate.simpleName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing ParserProbe class declaration"));
        if (declaration.methods().stream().noneMatch(candidate -> "parse".equals(candidate.name()))) {
            throw new IllegalStateException("Missing decorated parse method");
        }

        System.out.println("QinDeclarationIrQinOwnedDecoratorSmokeTestMain passed.");
    }
}
