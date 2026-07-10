package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrJavaClassLiteralExpression;
import com.qin.lang.ir.QinIrProgram;

/**
 * Proves export class lowers through the same static declaration path as class.
 */
public final class QinDeclarationIrExportClassSmokeTestMain {
    private QinDeclarationIrExportClassSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                export class ExportedProbe {
                  static build(): ExportedProbe {
                    return new ExportedProbe()
                  }
                  value(): number {
                    return 1
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        if (program.classDeclarations().size() != 1) {
            throw new IllegalStateException("Expected one class declaration, got "
                    + program.classDeclarations().size());
        }
        QinIrClassDeclaration declaration = program.classDeclarations().get(0);
        if (!"ExportedProbe".equals(declaration.simpleName())) {
            throw new IllegalStateException("Unexpected class declaration: " + declaration.simpleName());
        }
        if (declaration.methods().stream().noneMatch(method -> "build".equals(method.name()) && method.staticMethod())) {
            throw new IllegalStateException("Missing static build method on exported class declaration");
        }
        QinIrConstDeclaration valueDeclaration = program.declarations().stream()
                .filter(candidate -> "ExportedProbe".equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing exported class value declaration"));
        if (!(valueDeclaration.initializer() instanceof QinIrJavaClassLiteralExpression classLiteral)
                || !"ExportedProbe".equals(classLiteral.binaryName())) {
            throw new IllegalStateException("Expected exported class value to use JVM class literal, got "
                    + valueDeclaration.initializer());
        }

        System.out.println("QinDeclarationIrExportClassSmokeTestMain passed.");
    }
}
