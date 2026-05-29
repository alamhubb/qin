package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsTaggedTemplateSmokeTestMain {
    private QinJsTaggedTemplateSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-tagged-template-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-tagged-template\" }\n", StandardCharsets.UTF_8);
        String source = """
                function tag(strings, value) {
                  return strings[0] + value + strings[1] + "|" + strings.raw[0] + "|" + strings.length;
                }
                ({ value: tag`a${1}b` });
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_tagged_template");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result object, got: " + result);
        }
        Object value = map.get("value");
        if (!"a1b|a|2".equals(value)) {
            throw new IllegalStateException("Unexpected tagged template result: " + value);
        }
        System.out.println("QinJsTaggedTemplateSmokeTestMain OK");
    }
}
