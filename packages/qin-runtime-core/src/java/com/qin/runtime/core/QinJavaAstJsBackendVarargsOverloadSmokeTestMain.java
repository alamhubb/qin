package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendVarargsOverloadSmokeTestMain {
    private QinJavaAstJsBackendVarargsOverloadSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                import com.subhuti.parser.Alternative;
                import java.util.Arrays;
                import java.util.List;
                class VarargsBox {
                    List listValue() { return Arrays.asList("x"); }
                    String pick(List values) { return "list:" + values.size(); }
                    String pick(String... values) { return "varargs:" + Arrays.asList(values).size(); }
                    String forward(String... values) { return pick(values); }
                }

                class FunctionalVarargsBox {
                    String choose(Alternative... alternatives) { return "alternative"; }
                    String choose(Runnable... alternatives) { return "runnable:" + Arrays.asList(alternatives).size(); }
                    String run() { return choose(() -> {}, () -> {}); }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("pick(...__qin_args)"), "overload dispatcher");
        require(generated.contains("choose(...__qin_args)"), "functional varargs overload dispatcher");
        require(generated.contains("__qin_overload_pick_1_"), "overload implementation");
        require(generated.contains("(...values)"), "varargs overload implementation");
        require(generated.contains("pick(...values)"), "varargs parameter forwarding");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-varargs-overload-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-varargs-overload\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst box = new VarargsBox();"
                        + "const functional = new FunctionalVarargsBox();"
                        + "const dynamicFunctional = functional.choose(__qin_java_functional(() => {}), __qin_java_functional(() => {}));"
                        + "box.pick(box.listValue()) + \":\" + box.pick(\"a\", \"b\", \"c\")"
                        + " + \":\" + box.forward(\"l\", \"r\") + \":\" + functional.run() + \":\" + dynamicFunctional;\n",
                "java_ast_js_backend_varargs_overload");
        if (!"list:1:varargs:3:varargs:2:runnable:2:runnable:2".equals(result)) {
            throw new IllegalStateException(
                    "Expected varargs overload result list:1:varargs:3:varargs:2:runnable:2:runnable:2, got: "
                            + result);
        }
        System.out.println("QinJavaAstJsBackendVarargsOverloadSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
