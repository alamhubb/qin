package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsRestApplyWrapperSmokeTestMain {
    private QinJsRestApplyWrapperSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                function wrap(original) {
                  return function (...args) {
                    return original.apply(this, args);
                  };
                }

                class Tool {
                  constructor() {
                    this.hit = false;
                  }

                  value(input) {
                    this.hit = true;
                    return input + 1;
                  }
                }

                const tool = new Tool();
                const wrapped = wrap(tool.value);
                const returned = wrapped.call(tool, 41);
                ({ hit: tool.hit, returned });
                """;
        Path root = Files.createTempDirectory("qin-js-rest-apply-wrapper-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsRestApplyWrapperSmoke");
        if (!(result instanceof Map<?, ?> map)
                || !Boolean.TRUE.equals(map.get("hit"))
                || !Double.valueOf(42.0d).equals(map.get("returned"))) {
            throw new IllegalStateException("Expected rest apply wrapper to invoke original method, got: "
                    + QinObjectJsonEncoder.toJson(result));
        }
        System.out.println("QinJsRestApplyWrapperSmokeTestMain OK");
    }
}
