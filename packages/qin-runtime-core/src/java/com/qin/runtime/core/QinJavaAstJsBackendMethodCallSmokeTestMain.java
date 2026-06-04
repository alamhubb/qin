package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendMethodCallSmokeTestMain {
    private QinJavaAstJsBackendMethodCallSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                import java.lang.StringBuilder;
                import java.util.Objects;
                record BackData(int codeIndex) {}
                class MethodBox {
                    String build() { return new StringBuilder("qin").append("-js").toString(); }
                    String safe(String name) { return Objects.toString(name); }
                    String display() { return "ok"; }
                    String label() { return this.display(); }
                    String alias() { return display(); }
                    static String helper() { return "static"; }
                    String helperAlias() { return helper(); }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class MethodBox"), "MethodBox JS class");
        require(generated.contains("class __QinJavaLangStringBuilder"), "StringBuilder runtime shim");
        require(generated.contains("const Objects = __QinJavaUtilObjects;"), "Objects runtime alias");
        require(generated.contains("return this.display();"), "explicit this call");
        require(generated.contains("return MethodBox.helper();"), "implicit current-class static call");
        require(generated.contains("codeIndex()"), "record component accessor");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-method-call-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-method-call\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst box = new MethodBox();"
                        + "const state = new BackData(7);"
                        + "box.build() + \":\" + box.safe(null) + \":\" + box.label() + \":\" + box.alias()"
                        + " + \":\" + box.helperAlias() + \":\" + state.codeIndex();\n",
                "java_ast_js_backend_method_call");
        if (!"qin-js:null:ok:ok:static:7".equals(result)) {
            throw new IllegalStateException("Expected method call result qin-js:null:ok:ok:static:7, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendMethodCallSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
