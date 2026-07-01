package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;
import com.qin.parser.QinParserFacade;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendInstanceofSmokeTestMain {
    private QinJavaAstJsBackendInstanceofSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                import com.slime.ast.AstNode;
                class Token implements AstNode {}
                class Probe {
                    boolean token(Object value) {
                        return value instanceof Token;
                    }

                    boolean astNode(Object value) {
                        return value instanceof AstNode;
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("return __qin_instanceof_value instanceof Token;"),
                "class instanceof expression");
        require(generated.contains("__qin_java_implements(__qin_instanceof_value, \"com.slime.ast.AstNode\")"),
                "interface instanceof expression");
        require(!generated.contains("__qin_binary__(\"instanceof\""),
                "no generic binary instanceof for Java type tests");
        new QinParserFacade().parseSource(generated);

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-instanceof-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-instanceof\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst probe = new Probe();"
                        + "const token = new Token();"
                        + "probe.token(token) + \":\" + probe.token(null) + \":\""
                        + " + probe.astNode(token) + \":\" + probe.astNode({});\n",
                "java_ast_js_backend_instanceof");
        if (!"true:false:true:false".equals(result)) {
            throw new IllegalStateException("Expected Java instanceof runtime result, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendInstanceofSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
