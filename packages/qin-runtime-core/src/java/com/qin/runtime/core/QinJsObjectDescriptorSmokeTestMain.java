package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsObjectDescriptorSmokeTestMain {
    private QinJsObjectDescriptorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const getDescs = Object.getOwnPropertyDescriptors;
                const obj = { a: 1 };
                const target = {};
                Object.defineProperties(target, getDescs(obj));
                ({
                  value: Object.getOwnPropertyDescriptor(target, "a").value,
                  enumerable: Object.prototype.propertyIsEnumerable.call(target, "a")
                });
                """;
        Path root = Files.createTempDirectory("qin-js-object-descriptor-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsObjectDescriptorSmoke");
        if (!(result instanceof Map<?, ?> map)
                || !Double.valueOf(1.0d).equals(map.get("value"))
                || !Boolean.TRUE.equals(map.get("enumerable"))) {
            throw new IllegalStateException("Unexpected descriptor result: " + result);
        }
        System.out.println("QinJsObjectDescriptorSmokeTestMain OK");
    }
}
