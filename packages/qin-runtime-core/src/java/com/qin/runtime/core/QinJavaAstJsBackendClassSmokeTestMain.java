package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendClassSmokeTestMain {
    private QinJavaAstJsBackendClassSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                package com.example;
                import java.util.ArrayList;
                class Person {
                    String name = "qin";
                    String title;
                    ArrayList fresh() { return new ArrayList(); }
                    String display() { return this.name; }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class Person"), "Person JS class");
        require(generated.contains("constructor()"), "Person constructor");
        require(generated.contains("this.name = \"qin\";"), "explicit field initializer");
        require(generated.contains("this.title = null;"), "default field initializer");
        require(generated.contains("fresh()"), "fresh method");
        require(generated.contains("return new ArrayList();"), "ArrayList method return");
        require(generated.contains("return this.name;"), "this property return");
        require(generated.contains("class __QinJavaUtilArrayList"), "ArrayList runtime shim from class method");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-class-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-class\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst person = new Person(); person.display() + \":\" + person.fresh().size();\n",
                "java_ast_js_backend_class");
        if (!"qin:0".equals(result)) {
            throw new IllegalStateException("Expected generated Java AST class field initializer and ArrayList, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendClassSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
