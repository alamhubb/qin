package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsOxcClassExpressionDecoratorSmokeTestMain {
    private QinJsOxcClassExpressionDecoratorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-oxc-class-expression-decorator-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-oxc-class-expression-decorator\" }\n",
                StandardCharsets.UTF_8);

        String source = """
                function mark(target, key, descriptor) {
                  descriptor.value.marked = true;
                  return descriptor;
                }
                function __decorate(decorators, target, key, desc) {
                  var c = arguments.length,
                    r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc,
                    d;
                  for (var i = decorators.length - 1; i >= 0; i--) {
                    if (d = decorators[i]) {
                      r = d(target, key, r) || r;
                    }
                  }
                  return c > 3 && r && Object.defineProperty(target, key, r), r;
                }
                let Tool = class Tool {
                  value() {
                    return 41;
                  }
                };
                __decorate([mark], Tool.prototype, "value", null);
                ({
                  type: typeof Tool.prototype.value,
                  marked: Tool.prototype.value.marked,
                  result: new Tool().value()
                });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                source,
                "js_oxc_class_expression_decorator");
        if (!(result instanceof Map<?, ?> map)
                || !"function".equals(map.get("type"))
                || !Boolean.TRUE.equals(map.get("marked"))
                || !Double.valueOf(41.0d).equals(map.get("result"))) {
            throw new IllegalStateException("Unexpected OXC class expression decorator result: "
                    + QinObjectJsonEncoder.toJson(result));
        }
        System.out.println("QinJsOxcClassExpressionDecoratorSmokeTestMain OK");
    }
}

