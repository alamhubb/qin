package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsRegExpSplitCaptureSmokeTestMain {
    private QinJsRegExpSplitCaptureSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                "Count is {{ count }}".split(/(\\{\\{[\\s\\S]*?\\}\\})/g);
                """;
        Path root = Files.createTempDirectory("qin-js-regexp-split-capture-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsRegExpSplitCaptureSmoke");
        if (!(result instanceof List<?> list)
                || list.size() != 3
                || !"Count is ".equals(list.get(0))
                || !"{{ count }}".equals(list.get(1))
                || !"".equals(list.get(2))) {
            throw new IllegalStateException("Unexpected RegExp split capture result: "
                    + QinObjectJsonEncoder.toJson(result));
        }
        System.out.println("QinJsRegExpSplitCaptureSmokeTestMain OK");
    }
}
