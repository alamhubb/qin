package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendRecordConstructorSmokeTestMain {
    private QinJavaAstJsBackendRecordConstructorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                record Pair(String left, String right) {
                    String join() {
                        return this.left + ":" + this.right;
                    }
                }
                """);

        QinIrMethodDeclaration constructor = program.classDeclarations().get(0).methods().stream()
                .filter(method -> "constructor".equals(method.name()))
                .findFirst()
                .orElseThrow();
        if (constructor.parameters().size() != 2) {
            throw new IllegalStateException("Expected record canonical constructor arity 2");
        }

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("case 2:"), "record constructor dispatch");
        require(generated.contains("this.__qin_field_left = left;"), "left component assignment");
        require(generated.contains("this.__qin_field_right = right;"), "right component assignment");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-record-constructor-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-record-constructor\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nnew Pair(\"qin\", \"record\").join();\n",
                "java_ast_js_backend_record_constructor");
        if (!"qin:record".equals(result)) {
            throw new IllegalStateException("Expected record constructor result qin:record, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendRecordConstructorSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
