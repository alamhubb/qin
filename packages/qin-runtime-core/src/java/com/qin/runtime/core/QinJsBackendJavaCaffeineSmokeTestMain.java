package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsBackendJavaCaffeineSmokeTestMain {
    private QinJsBackendJavaCaffeineSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                import com.github.benmanes.caffeine.cache.Cache;
                import com.github.benmanes.caffeine.cache.Caffeine;

                class CacheBox {
                    String run() {
                        Cache<String, String> cache = Caffeine.newBuilder().maximumSize(1).recordStats().build();
                        cache.put("a", "left");
                        cache.put("b", "right");
                        return cache.getIfPresent("a") + ":" + cache.getIfPresent("b") + ":" + cache.estimatedSize();
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("const Caffeine = __QinCaffeine;"), "Caffeine alias");
        require(generated.contains("class __QinCaffeineCache"), "Caffeine cache runtime");

        Path root = Files.createTempDirectory("qin-js-backend-java-caffeine-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-java-caffeine\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new CacheBox(); box.run();\n",
                "js_backend_java_caffeine");
        if (!"null:right:1".equals(result)) {
            throw new IllegalStateException("Expected Caffeine cache result, got: " + result);
        }
        System.out.println("QinJsBackendJavaCaffeineSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
