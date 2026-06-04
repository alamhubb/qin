package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendGuardReturnSmokeTestMain {
    private QinJavaAstJsBackendGuardReturnSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class GuardReturn {
                    boolean choose(boolean blocked) {
                        if (blocked) {
                            return false;
                        }
                        return true;
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        Path root = Files.createTempDirectory("qin-java-ast-js-backend-guard-return-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-guard-return\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst guard = new GuardReturn(); guard.choose(false) + \":\" + guard.choose(true);\n",
                "java_ast_js_backend_guard_return");
        if (!"true:false".equals(result)) {
            throw new IllegalStateException("Expected guard return runtime result, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendGuardReturnSmokeTestMain OK");
    }
}
