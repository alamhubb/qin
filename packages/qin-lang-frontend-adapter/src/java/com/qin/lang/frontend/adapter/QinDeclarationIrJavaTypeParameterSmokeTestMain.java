package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for java:-imported parameter reference type lowering.
 */
public final class QinDeclarationIrJavaTypeParameterSmokeTestMain {
    private QinDeclarationIrJavaTypeParameterSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                import { RestController as RC, PostMapping as POST, RequestBody as Body } from "java:org.springframework.web.bind.annotation"
                import { Payload } from "java:com.example.dto"

                @RC
                class PostController {
                  @POST("/api/create")
                  create(@Body payload: Payload) {
                    return "created"
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration classDeclaration = program.classDeclarations().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing class declaration"));
        QinIrMethodDeclaration method = classDeclaration.methods().stream()
                .filter(candidate -> "create".equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing create method"));

        if (method.parameters().size() != 1) {
            throw new IllegalStateException("Expected one parameter, got " + method.parameters().size());
        }

        QinIrParameter parameter = method.parameters().get(0);
        if (!"com.example.dto.Payload".equals(parameter.type().binaryName())) {
            throw new IllegalStateException("Unexpected java imported parameter type: " + parameter.type().binaryName());
        }

        System.out.println("QinDeclarationIrJavaTypeParameterSmokeTestMain passed.");
    }
}
