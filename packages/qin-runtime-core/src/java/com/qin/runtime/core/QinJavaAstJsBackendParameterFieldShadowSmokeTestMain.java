package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendParameterFieldShadowSmokeTestMain {
    private QinJavaAstJsBackendParameterFieldShadowSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class SourceBox {
                    String sourceCode;

                    SourceBox(String sourceCode) {
                        this.sourceCode = sourceCode;
                    }

                    int lengthOf(String sourceCode) {
                        return sourceCode.length();
                    }

                    int storedLength() {
                        return this.sourceCode.length();
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        if (!generated.contains("sourceCode.length()")) {
            throw new IllegalStateException("Expected parameter sourceCode to stay local in generated JS");
        }
        Path root = Files.createTempDirectory("qin-java-ast-js-backend-parameter-field-shadow-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-parameter-field-shadow\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new SourceBox(\"hello\"); box.lengthOf(\"qin\") + \":\" + box.storedLength();\n",
                "java_ast_js_backend_parameter_field_shadow");
        if (!"3:5".equals(result)) {
            throw new IllegalStateException("Expected parameter/field shadow result 3:5, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendParameterFieldShadowSmokeTestMain OK");
    }
}
