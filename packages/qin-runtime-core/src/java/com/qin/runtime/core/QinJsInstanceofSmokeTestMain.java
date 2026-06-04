package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsInstanceofSmokeTestMain {
    private QinJsInstanceofSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Box {}
                const box = new Box();
                const result = {
                  error: new Error("plain") instanceof Error,
                  typeAsError: new TypeError("typed") instanceof Error,
                  typeExact: new TypeError("typed") instanceof TypeError,
                  typeNotRange: new TypeError("typed") instanceof RangeError,
                  box: box instanceof Box,
                  nullBox: null instanceof Box,
                  objectBox: {} instanceof Box
                };
                """;
        Path root = Files.createTempDirectory("qin-js-instanceof-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsInstanceofSmoke");
        if (!(result instanceof Map<?, ?> map)
                || !Boolean.TRUE.equals(map.get("error"))
                || !Boolean.TRUE.equals(map.get("typeAsError"))
                || !Boolean.TRUE.equals(map.get("typeExact"))
                || !Boolean.FALSE.equals(map.get("typeNotRange"))
                || !Boolean.TRUE.equals(map.get("box"))
                || !Boolean.FALSE.equals(map.get("nullBox"))
                || !Boolean.FALSE.equals(map.get("objectBox"))) {
            throw new IllegalStateException("Unexpected instanceof result: " + QinObjectJsonEncoder.toJson(result));
        }
        System.out.println("QinJsInstanceofSmokeTestMain OK");
    }
}
