package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsTemplateLiteralExpressionSmokeTestMain {
    private QinJsTemplateLiteralExpressionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-template-literal-expression-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-template-literal-expression\" }\n",
                StandardCharsets.UTF_8);

        String source = """
                var B = class {
                  m(pattern, flags, raw) {
                    return {
                      value: raw || `/${pattern}/${flags}`
                    };
                  }
                };
                ({ value: new B().m("abc", "g", null).value });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_template_literal_expression");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result object, got: " + result);
        }
        Object value = map.get("value");
        if (!"/abc/g".equals(value)) {
            throw new IllegalStateException("Unexpected template literal result: " + value);
        }
        System.out.println("QinJsTemplateLiteralExpressionSmokeTestMain OK");
    }
}
