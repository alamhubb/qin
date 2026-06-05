package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrFieldDeclaration;
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
        require(generated.contains("equals(other)"), "record default equals");
        require(generated.contains("hashCode()"), "record default hashCode");
        require(generated.contains("toString()"), "record default toString");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-record-constructor-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-record-constructor\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + """

                        const left = new Pair("qin", "record");
                        const right = new Pair("qin", "record");
                        const other = new Pair("qin", "other");
                        if (left.join() !== "qin:record") throw new Error("record join failed");
                        if (!left.equals(right)) throw new Error("record equals failed");
                        if (left.equals(other)) throw new Error("record unequal failed");
                        if (left.hashCode() !== right.hashCode()) throw new Error("record hashCode failed");
                        if (left.toString() !== "Pair[left=qin, right=record]") throw new Error("record toString failed");
                        "record-values-ok";
                        """,
                "java_ast_js_backend_record_constructor");
        if (!"record-values-ok".equals(result)) {
            throw new IllegalStateException("Expected record value semantics result, got: " + result);
        }

        QinIrProgram staticRecordProgram = new QinJavaAstIrLowerer().lowerSource("""
                record Flag() {
                    public static final Flag DEFAULT = new Flag();
                }
                """);
        QinIrFieldDeclaration defaultField = staticRecordProgram.classDeclarations().get(0).fields().stream()
                .filter(field -> "DEFAULT".equals(field.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Expected record static DEFAULT field in IR"));
        require(defaultField.staticField(), "record static field flag");
        String staticRecordGenerated = new QinJsBackend().compileProgram(staticRecordProgram);
        if (!staticRecordGenerated.contains("__qin_field_DEFAULT")) {
            throw new IllegalStateException("Expected record static DEFAULT initializer, generated:\n" + staticRecordGenerated);
        }
        Path staticRoot = Files.createTempDirectory("qin-java-ast-js-backend-record-static-");
        Files.writeString(staticRoot.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-record-static\" };\n",
                StandardCharsets.UTF_8);
        Object staticResult = new QinJsPackageRunner().runModuleSource(
                staticRoot,
                staticRecordGenerated + """

                        if (!(Flag.__qin_field_DEFAULT instanceof Flag)) throw new Error("record static DEFAULT failed");
                        "record-static-ok";
                        """,
                "java_ast_js_backend_record_static");
        if (!"record-static-ok".equals(staticResult)) {
            throw new IllegalStateException("Expected record static result, got: " + staticResult);
        }

        QinIrProgram staticFactoryProgram = new QinJavaAstIrLowerer().lowerSource("""
                class Mode {
                    static Mode create(String name) {
                        return new Mode();
                    }
                    static final Mode DEFAULT = create("");
                }
                """);
        String staticFactoryGenerated = new QinJsBackend().compileProgram(staticFactoryProgram);
        require(staticFactoryGenerated.contains("Mode.create(\"\")"), "static field factory call owner");
        require(!staticFactoryGenerated.contains("this.create(\"\""), "no this receiver for static field factory call");
        Path staticFactoryRoot = Files.createTempDirectory("qin-java-ast-js-backend-static-factory-");
        Files.writeString(staticFactoryRoot.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-static-factory\" };\n",
                StandardCharsets.UTF_8);
        Object staticFactoryResult = new QinJsPackageRunner().runModuleSource(
                staticFactoryRoot,
                staticFactoryGenerated + """

                        if (!(Mode.__qin_field_DEFAULT instanceof Mode)) throw new Error("static factory DEFAULT failed");
                        "static-factory-ok";
                        """,
                "java_ast_js_backend_static_factory");
        if (!"static-factory-ok".equals(staticFactoryResult)) {
            throw new IllegalStateException("Expected static factory result, got: " + staticFactoryResult);
        }

        System.out.println("QinJavaAstJsBackendRecordConstructorSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
