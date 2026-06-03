package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsBackendJavaOptionalSmokeTestMain {
    private QinJsBackendJavaOptionalSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                import java.util.Optional;

                class OptionalBox {
                    String run() {
                        Optional<String> full = Optional.of("qin");
                        Optional<String> empty = Optional.empty();
                        return full.isPresent() + ":" + full.get() + ":" + empty.orElse("none");
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("const Optional = __QinJavaUtilOptional;"), "Optional alias");
        require(generated.contains("class __QinJavaUtilOptionalValue"), "Optional runtime");

        Path root = Files.createTempDirectory("qin-js-backend-java-optional-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-java-optional\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new OptionalBox(); box.run();\n",
                "js_backend_java_optional");
        if (!"true:qin:none".equals(result)) {
            throw new IllegalStateException("Expected Optional result, got: " + result);
        }
        System.out.println("QinJsBackendJavaOptionalSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
