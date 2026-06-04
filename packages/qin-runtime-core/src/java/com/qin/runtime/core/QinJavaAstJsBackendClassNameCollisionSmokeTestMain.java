package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;
import com.slime.java.ast.JavaCstToAst;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJavaAstJsBackendClassNameCollisionSmokeTestMain {
    private QinJavaAstJsBackendClassNameCollisionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerPrograms(List.of(
                JavaCstToAst.parse("""
                        package alpha;
                        class SameName {
                            String value() {
                                return "alpha";
                            }
                        }
                        """),
                JavaCstToAst.parse("""
                        package beta;
                        class SameName {
                            String value() {
                                return "beta";
                            }
                        }
                        """)));

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class alpha_SameName"), "alpha generated class identifier");
        require(generated.contains("class beta_SameName"), "beta generated class identifier");
        require(!generated.contains("class SameName"), "no colliding simple class identifier");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-class-name-collision-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-class-name-collision\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst alphaValue = new alpha_SameName().value();"
                        + " const betaValue = new beta_SameName().value();"
                        + " alphaValue + ':' + betaValue;\n",
                "java_ast_js_backend_class_name_collision");
        if (!"alpha:beta".equals(result)) {
            throw new IllegalStateException("Expected both colliding Java classes to remain addressable, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendClassNameCollisionSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
