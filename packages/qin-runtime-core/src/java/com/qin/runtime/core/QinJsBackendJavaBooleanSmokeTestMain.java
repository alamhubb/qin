package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaBooleanSmokeTestMain {
    private QinJsBackendJavaBooleanSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(new QinIrConstDeclaration(
                        "truth",
                        new QinIrMemberAccessExpression("Boolean", "TRUE"))),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("const __QinJavaLangBoolean"), "Boolean runtime facade");
        require(generated.contains("const Boolean = __QinJavaLangBoolean;"), "Boolean alias");
        require(generated.contains("const truth = Boolean.TRUE;"), "Boolean.TRUE member access");

        Path root = Files.createTempDirectory("qin-js-backend-java-boolean-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-js-backend-java-boolean\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\ntruth.equals(true) + \":\" + Boolean.FALSE.equals(false)"
                        + " + \":\" + Boolean.TRUE.equals(false);\n",
                "js_backend_java_boolean");
        if (!"true:true:false".equals(result)) {
            throw new IllegalStateException("Expected Boolean TRUE/FALSE equals result, got: " + result);
        }
        System.out.println("QinJsBackendJavaBooleanSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
