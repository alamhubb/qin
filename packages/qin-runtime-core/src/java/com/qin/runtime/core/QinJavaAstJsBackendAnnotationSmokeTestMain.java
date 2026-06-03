package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendAnnotationSmokeTestMain {
    private QinJavaAstJsBackendAnnotationSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                import com.subhuti.parser.SubhutiRule;

                @Deprecated
                class RuleBox {
                    @SubhutiRule
                    int value() {
                        return 1;
                    }
                }
                """);

        QinIrClassDeclaration ruleBox = program.classDeclarations().get(0);
        require(ruleBox.annotations().size() == 1, "class annotation count");
        QinIrAnnotation classAnnotation = ruleBox.annotations().get(0);
        require("java.lang.Deprecated".equals(classAnnotation.ownerBinaryName()), "class annotation owner");

        QinIrMethodDeclaration value = ruleBox.methods().get(0);
        require(value.annotations().size() == 1, "method annotation count");
        QinIrAnnotation methodAnnotation = value.annotations().get(0);
        require("com.subhuti.parser.SubhutiRule".equals(methodAnnotation.ownerBinaryName()),
                "method annotation owner");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class RuleBox"), "generated class");
        require(generated.contains("value()"), "generated method");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-annotation-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-annotation\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new RuleBox(); box.value();\n",
                "java_ast_js_backend_annotation");
        if (!Double.valueOf(1.0).equals(result)) {
            throw new IllegalStateException("Expected annotated parser method result 1.0, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendAnnotationSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
