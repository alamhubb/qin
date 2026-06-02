package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsArrowRestSpreadSmokeTestMain {
    private QinJsArrowRestSpreadSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                function join(a, b) {
                  return a + ":" + b;
                }
                const forward = (...args) => join(...args);
                const result = forward("left", "right");
                """;
        String ast = new QinFrontendLowerer().parseAst(source);
        if (!ast.contains("\"params\":[{\"param\":{\"type\":\"RestElement\"")) {
            throw new IllegalStateException("Expected arrow rest parameter to parse as RestElement, got AST: " + ast);
        }
        Path root = Files.createTempDirectory("qin-js-arrow-rest-spread-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsArrowRestSpreadSmoke");
        if (!"left:right".equals(result)) {
            throw new IllegalStateException("Expected arrow rest/spread result left:right, got: " + result);
        }
        System.out.println("QinJsArrowRestSpreadSmokeTestMain OK");
    }
}
