package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsBackendJavaCollectorsSmokeTestMain {
    private QinJsBackendJavaCollectorsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                import java.util.List;
                import java.util.stream.Collectors;

                class CollectorsBox {
                    String run() {
                        List<String> names = List.of("qin", "java", "js");
                        List<String> filtered = names.stream()
                            .filter(name -> name.equals("qin") || name.equals("js"))
                            .collect(Collectors.toList());
                        boolean hasJs = filtered.stream().anyMatch(name -> name.equals("js"));
                        String joined = filtered.stream().collect(Collectors.joining("|"));
                        String first = filtered.stream().findFirst().orElse("none");
                        return filtered.size() + ":" + hasJs + ":" + joined + ":" + first;
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("const Collectors = __QinJavaUtilStreamCollectors;"), "Collectors alias");
        require(generated.contains("class __QinJavaUtilStream"), "Stream runtime");

        Path root = Files.createTempDirectory("qin-js-backend-java-collectors-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-java-collectors\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new CollectorsBox(); box.run();\n",
                "js_backend_java_collectors");
        if (!"2:true:qin|js:qin".equals(result)) {
            throw new IllegalStateException("Expected Collectors result, got: " + result);
        }
        System.out.println("QinJsBackendJavaCollectorsSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
