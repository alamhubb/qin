package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsClassFieldInitializationSmokeTestMain {
    private QinJsClassFieldInitializationSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Tool {
                  _parseSuccess = true;
                  _sourceCode = "abc";
                  count = 2;
                  list = [];
                  get parserFail() {
                    return !this._parseSuccess;
                  }
                }
                const tool = new Tool();
                ({
                  parserFail: tool.parserFail,
                  parseSuccess: tool._parseSuccess,
                  sourceCode: tool._sourceCode,
                  count: tool.count,
                  listLength: tool.list.length
                });
                """;
        Path root = Files.createTempDirectory("qin-js-class-field-initialization-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsClassFieldInitializationSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!Boolean.FALSE.equals(map.get("parserFail"))
                || !Boolean.TRUE.equals(map.get("parseSuccess"))
                || !"abc".equals(map.get("sourceCode"))
                || !Double.valueOf(2.0d).equals(map.get("count"))
                || !Integer.valueOf(0).equals(map.get("listLength"))) {
            throw new IllegalStateException("Unexpected class field result: " + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinJsClassFieldInitializationSmokeTestMain OK");
    }
}
