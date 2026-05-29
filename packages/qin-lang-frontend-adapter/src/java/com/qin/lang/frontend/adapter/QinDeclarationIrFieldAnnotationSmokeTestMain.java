package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrAnnotationArgument;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;

/**
 * Smoke test for field decorator lowering into Qin declaration IR.
 */
public final class QinDeclarationIrFieldAnnotationSmokeTestMain {
    private QinDeclarationIrFieldAnnotationSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                import { JsonProperty as JP } from "java:com.example"

                class Payload {
                  @JP("user_name")
                  name: string
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        if (program.classDeclarations().size() != 1) {
            throw new IllegalStateException("Expected one class declaration, got " + program.classDeclarations().size());
        }

        QinIrClassDeclaration payload = program.classDeclarations().get(0);
        if (payload.fields().size() != 1) {
            throw new IllegalStateException("Expected one field, got " + payload.fields().size());
        }

        QinIrFieldDeclaration field = payload.fields().get(0);
        if (field.annotations().size() != 1) {
            throw new IllegalStateException("Expected one field annotation, got " + field.annotations().size());
        }

        QinIrAnnotation annotation = field.annotations().get(0);
        if (!"com.example.JsonProperty".equals(annotation.ownerBinaryName())) {
            throw new IllegalStateException("Unexpected field annotation owner: " + annotation.ownerBinaryName());
        }
        if (annotation.arguments().size() != 1) {
            throw new IllegalStateException("Expected one field annotation argument");
        }

        QinIrAnnotationArgument argument = annotation.arguments().get(0);
        if (!"value".equals(argument.name())) {
            throw new IllegalStateException("Unexpected field annotation argument name: " + argument.name());
        }
        if (!(argument.value() instanceof QinIrStringLiteral stringLiteral)
                || !"user_name".equals(stringLiteral.value())) {
            throw new IllegalStateException("Unexpected field annotation argument value: " + argument.value());
        }

        System.out.println("QinDeclarationIrFieldAnnotationSmokeTestMain passed.");
    }
}
