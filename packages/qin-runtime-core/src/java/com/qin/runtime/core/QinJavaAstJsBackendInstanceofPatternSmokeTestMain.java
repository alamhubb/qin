package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;
import com.qin.parser.QinParserFacade;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendInstanceofPatternSmokeTestMain {
    private QinJavaAstJsBackendInstanceofPatternSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class MethodKey {
                    boolean same(Object obj) {
                        if (!(obj instanceof MethodKey other)) {
                            return false;
                        }
                        return true;
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains(
                "return __qin_pattern_value instanceof MethodKey && (other = __qin_pattern_value, true);"),
                "valid instanceof pattern assignment expression");
        new QinParserFacade().parseSource(generated);

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-instanceof-pattern-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-instanceof-pattern\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst key = new MethodKey(); key.same(key) + \":\" + key.same(null);\n",
                "java_ast_js_backend_instanceof_pattern");
        if (!"true:false".equals(result)) {
            throw new IllegalStateException("Expected instanceof pattern runtime result, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendInstanceofPatternSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
