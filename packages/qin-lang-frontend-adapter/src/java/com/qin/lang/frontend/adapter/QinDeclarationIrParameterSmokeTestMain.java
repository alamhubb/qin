package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for parameter annotation/type lowering into declaration IR.
 */
public final class QinDeclarationIrParameterSmokeTestMain {
    private QinDeclarationIrParameterSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                import { RestController as RC, PostMapping as POST, RequestBody as Body } from "java:org.springframework.web.bind.annotation"

                @RC
                class PostController {
                  @POST("/api/create")
                  create(@Body payload: string) {
                    return payload
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        if (program.classDeclarations().size() != 1) {
            throw new IllegalStateException(
                    "Expected exactly one declaration class, got " + program.classDeclarations().size());
        }

        QinIrClassDeclaration classDeclaration = program.classDeclarations().get(0);
        QinIrMethodDeclaration method = classDeclaration.methods().stream()
                .filter(candidate -> "create".equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing create method"));

        if (method.parameters().size() != 1) {
            throw new IllegalStateException("Expected one parameter, got " + method.parameters().size());
        }

        QinIrParameter parameter = method.parameters().get(0);
        if (!"payload".equals(parameter.name())) {
            throw new IllegalStateException("Unexpected parameter name: " + parameter.name());
        }
        if (!"java.lang.String".equals(parameter.type().binaryName())) {
            throw new IllegalStateException("Unexpected parameter type: " + parameter.type().binaryName());
        }
        if (parameter.annotations().size() != 1) {
            throw new IllegalStateException("Expected one parameter annotation, got " + parameter.annotations().size());
        }
        if (!"org.springframework.web.bind.annotation.RequestBody"
                .equals(parameter.annotations().get(0).ownerBinaryName())) {
            throw new IllegalStateException(
                    "Unexpected parameter annotation: " + parameter.annotations().get(0).ownerBinaryName());
        }

        System.out.println("QinDeclarationIrParameterSmokeTestMain passed.");
    }
}
