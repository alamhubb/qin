package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Verifies concurrent Qin package wrapper invocations do not race while
 * materializing the shared npm-host node_modules directory.
 */
public final class QinJsPackageRunnerConcurrentMaterializeSmokeTestMain {
    private QinJsPackageRunnerConcurrentMaterializeSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-package-concurrent-");
        Files.writeString(root.resolve("qin.config.json"), """
                {
                  "name": "com.qin.smoke:js-package-concurrent",
                  "version": "0.1.0",
                  "dependencies": {
                    "@vitejs/plugin-vue": "^6.0.7",
                    "vite": "^8.0.13",
                    "@vue/compiler-sfc": "^3.5.34",
                    "vue": "^3.5.34"
                  }
                }
                """, StandardCharsets.UTF_8);

        String wrapper = """
                import vuePlugin from '@vitejs/plugin-vue'
                const plugin = vuePlugin()
                ;({ name: plugin.name, hasTransformHook: !!plugin.transform })
                """;

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startGate = new CountDownLatch(1);
        try {
            List<Future<Object>> futures = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                futures.add(executor.submit(() -> {
                    startGate.await();
                    return new QinJsPackageRunner().runModuleSource(
                            root,
                            wrapper,
                            "concurrent_plugin_vue_materialize");
                }));
            }
            startGate.countDown();
            for (Future<Object> future : futures) {
                Object result = future.get(5, TimeUnit.MINUTES);
                if (!(result instanceof Map<?, ?> map)
                        || !"vite:vue".equals(map.get("name"))
                        || !Boolean.TRUE.equals(map.get("hasTransformHook"))) {
                    throw new IllegalStateException("Unexpected concurrent package runner result: " + result);
                }
            }
        } finally {
            executor.shutdownNow();
        }

        System.out.println("QinJsPackageRunnerConcurrentMaterializeSmokeTestMain OK");
    }
}
