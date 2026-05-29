package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsTypedArrayProtoSmokeTestMain {
    private QinJsTypedArrayProtoSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-typed-array-proto-");
        Path source = root.resolve("main.js");
        Files.writeString(source, """
                const data = new Uint8Array(2);
                data[0] = 260;
                data.__proto__ = {
                  first() {
                    return this.length;
                  }
                };
                ({
                  first: data.first(),
                  length: data.length
                });
                """, StandardCharsets.UTF_8);

        Object result = new QinInMemoryJvmRunner().compileAndRun(
                source,
                "com.qin.runtime.generated.JsTypedArrayProtoSmoke");
        String json = QinObjectJsonEncoder.toJson(result);
        if (!"{\"first\":2,\"length\":2}".equals(json)) {
            throw new IllegalStateException("Unexpected typed array proto result: " + json);
        }
        System.out.println("QinJsTypedArrayProtoSmokeTestMain OK");
    }
}
