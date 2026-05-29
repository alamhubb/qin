package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsObjectPrototypeCallSmokeTestMain {
    private QinJsObjectPrototypeCallSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const has = Object.prototype.hasOwnProperty;
                ({
                  own: has.call({ video: true }, "video"),
                  missing: has.call({ video: true }, "audio"),
                  tag: Object.prototype.toString.call({})
                });
                """;
        Path root = Files.createTempDirectory("qin-js-object-prototype-call-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsObjectPrototypeCallSmoke");
        if (!(result instanceof Map<?, ?> map)
                || !Boolean.TRUE.equals(map.get("own"))
                || !Boolean.FALSE.equals(map.get("missing"))
                || !"[object Object]".equals(map.get("tag"))) {
            throw new IllegalStateException("Unexpected Object.prototype result: " + result);
        }
        System.out.println("QinJsObjectPrototypeCallSmokeTestMain OK");
    }
}
