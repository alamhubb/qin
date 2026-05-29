package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;

/**
 * Smoke test for local Qin class type used as parameter type annotation.
 */
public final class QinDeclarationIrLocalTypeParameterSmokeTestMain {
    private QinDeclarationIrLocalTypeParameterSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                import { RestController as RC, PostMapping as POST, RequestBody as Body } from "java:org.springframework.web.bind.annotation"

                class Payload {
                }

                @RC
                class PostController {
                  @POST("/api/create")
                  create(@Body payload: Payload) {
                    return "created"
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        if (program.classDeclarations().size() != 2) {
            throw new IllegalStateException("Expected two class declarations, got " + program.classDeclarations().size());
        }

        QinIrClassDeclaration controller = program.classDeclarations().stream()
                .filter(candidate -> "PostController".equals(candidate.simpleName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing PostController declaration"));

        QinIrMethodDeclaration method = controller.methods().stream()
                .filter(candidate -> "create".equals(candidate.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing create method"));

        if (method.parameters().size() != 1) {
            throw new IllegalStateException("Expected one parameter, got " + method.parameters().size());
        }

        QinIrParameter parameter = method.parameters().get(0);
        if (!"Payload".equals(parameter.type().binaryName())) {
            throw new IllegalStateException("Unexpected local parameter type: " + parameter.type().binaryName());
        }

        System.out.println("QinDeclarationIrLocalTypeParameterSmokeTestMain passed.");
    }
}
