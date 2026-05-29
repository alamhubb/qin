package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for field declaration lowering into Qin declaration IR.
 */
public final class QinDeclarationIrFieldSmokeTestMain {
    private QinDeclarationIrFieldSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class Payload {
                  name: string = "guest"
                  active: boolean = true
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        if (program.classDeclarations().size() != 1) {
            throw new IllegalStateException("Expected one class declaration, got " + program.classDeclarations().size());
        }

        QinIrClassDeclaration payload = program.classDeclarations().get(0);
        if (payload.fields().size() != 2) {
            throw new IllegalStateException("Expected two fields, got " + payload.fields().size());
        }

        requireField(payload, "name", "java.lang.String");
        requireField(payload, "active", null);
        requireInitializer(payload, "name", "guest");
        requireBooleanInitializer(payload, "active", true);

        System.out.println("QinDeclarationIrFieldSmokeTestMain passed.");
    }

    private static void requireField(QinIrClassDeclaration declaration, String name, String expectedBinaryName) {
        QinIrFieldDeclaration field = declaration.fields().stream()
                .filter(candidate -> name.equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing field: " + name));

        String actualBinaryName = field.type().binaryName();
        if (expectedBinaryName == null) {
            if (field.type().kind() != com.qin.lang.ir.QinIrTypeKind.BOOLEAN) {
                throw new IllegalStateException("Expected boolean field type for `" + name + "`, got " + field.type());
            }
            return;
        }
        if (!expectedBinaryName.equals(actualBinaryName)) {
            throw new IllegalStateException("Unexpected field type for `" + name + "`: " + actualBinaryName);
        }
    }

    private static void requireInitializer(QinIrClassDeclaration declaration, String name, String expectedValue) {
        QinIrFieldDeclaration field = declaration.fields().stream()
                .filter(candidate -> name.equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing field: " + name));
        if (!(field.initializer() instanceof com.qin.lang.ir.QinIrStringLiteral stringLiteral)
                || !expectedValue.equals(stringLiteral.value())) {
            throw new IllegalStateException("Unexpected string initializer for `" + name + "`: " + field.initializer());
        }
    }

    private static void requireBooleanInitializer(QinIrClassDeclaration declaration, String name, boolean expectedValue) {
        QinIrFieldDeclaration field = declaration.fields().stream()
                .filter(candidate -> name.equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing field: " + name));
        if (!(field.initializer() instanceof com.qin.lang.ir.QinIrBooleanLiteral booleanLiteral)
                || booleanLiteral.value() != expectedValue) {
            throw new IllegalStateException("Unexpected boolean initializer for `" + name + "`: " + field.initializer());
        }
    }
}
