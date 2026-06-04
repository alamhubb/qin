package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendNullableReferenceOverloadSmokeTestMain {
    private QinJavaAstJsBackendNullableReferenceOverloadSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class NullableReferenceOverloadBox {
                    String pick(String value) {
                        return value == null ? "string:null" : "string:" + value;
                    }

                    String pick(int value) {
                        return "int:" + value;
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("__qin_args[0] === null || typeof __qin_args[0] === \"string\""),
                "nullable string overload guard");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-nullable-reference-overload-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-nullable-reference-overload\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new NullableReferenceOverloadBox(); box.pick(null) + \":\" + box.pick(3);\n",
                "java_ast_js_backend_nullable_reference_overload");
        if (!"string:null:int:3".equals(result)) {
            throw new IllegalStateException("Expected nullable overload result string:null:int:3, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendNullableReferenceOverloadSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
