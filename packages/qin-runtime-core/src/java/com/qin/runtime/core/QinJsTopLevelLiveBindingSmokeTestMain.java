package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsTopLevelLiveBindingSmokeTestMain {
    private QinJsTopLevelLiveBindingSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-top-level-live-binding-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-top-level-live-binding\" }\n", StandardCharsets.UTF_8);
        String source = """
                function readLater() {
                  return ++counter;
                }
                let counter = -1;
                ({ value: readLater() });
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_top_level_live_binding");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result object, got: " + result);
        }
        Object value = map.get("value");
        if (!(value instanceof Number number) || number.doubleValue() != 0d) {
            throw new IllegalStateException("Expected value == 0, got: " + value);
        }
        System.out.println("QinJsTopLevelLiveBindingSmokeTestMain OK");
    }
}
