package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendFunctionalOverloadGuardSmokeTestMain {
    private QinJavaAstJsBackendFunctionalOverloadGuardSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                import java.util.function.Supplier;

                class FunctionalOverloadBox {
                    String rule(String label, Supplier supplier) {
                        return label + ":" + supplier.get();
                    }

                    void rule(String label, Runnable runnable) {
                        runnable.run();
                    }

                    String route(String label, Supplier supplier) {
                        return label + ":" + supplier.get();
                    }

                    String route(int code, Runnable runnable) {
                        runnable.run();
                        return "run:" + code;
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(!generated.contains("instanceof Supplier"), "Supplier guard avoids instanceof alias");
        require(!generated.contains("instanceof Runnable"), "Runnable guard avoids instanceof alias");
        require(generated.contains(".__qinJavaFunctional === true"), "functional guard marker");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-functional-overload-guard-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-functional-overload-guard\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nlet marker = \"cold\";"
                        + "const box = new FunctionalOverloadBox();"
                        + "const ruleResult = box.rule(\"rule\", __qin_java_functional(() => \"ok\"));"
                        + "const supplierResult = box.route(\"supplier\", __qin_java_functional(() => \"ok\"));"
                        + "const runnableResult = box.route(7, __qin_java_functional(() => { marker = \"hot\"; }));"
                        + "ruleResult + \":\" + supplierResult + \":\" + runnableResult + \":\" + marker;\n",
                "java_ast_js_backend_functional_overload_guard");
        if (!"rule:ok:supplier:ok:run:7:hot".equals(result)) {
            throw new IllegalStateException("Expected functional overload result rule:ok:supplier:ok:run:7:hot, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendFunctionalOverloadGuardSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
