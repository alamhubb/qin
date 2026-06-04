package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendSubhutiSuperSmokeTestMain {
    private QinJavaAstJsBackendSubhutiSuperSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                import com.subhuti.parser.SubhutiRule;
                import java.util.function.Supplier;

                class ParentSubhutiRuleBox {
                    Object executeRuleWrapper(Supplier targetFun, String ruleName, String className, String cacheKey) {
                        return "wrapped:" + className + ":" + targetFun.get();
                    }

                    @SubhutiRule
                    Object Declaration() {
                        return "parent";
                    }
                }

                class ChildSubhutiRuleBox extends ParentSubhutiRuleBox {
                    @Override
                    @SubhutiRule
                    Object Declaration() {
                        return super.Declaration();
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("__qin_subhuti_raw_Declaration"), "Subhuti raw rule method");
        require(generated.contains("super.__qin_subhuti_raw_Declaration()"), "super Subhuti raw call");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-subhuti-super-");
        Files.writeString(
                root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-subhuti-super\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nnew ChildSubhutiRuleBox().Declaration();\n",
                "java_ast_js_backend_subhuti_super");
        if (!"wrapped:ChildSubhutiRuleBox:parent".equals(result)) {
            throw new IllegalStateException("Expected child wrapper plus parent raw result, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendSubhutiSuperSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
