package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsObjectCreateForOfAssignmentSmokeTestMain {
    private QinJsObjectCreateForOfAssignmentSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-object-create-for-of-assignment-");
        Path source = root.resolve("main.js");
        Files.writeString(source, """
                function makeMap(str) {
                  const map = Object.create(null);
                  for (const key of str.split(",")) map[key] = 1;
                  return (val) => val in map;
                }
                const isKnown = makeMap("defer,async,src");
                const isHtmlAttr = makeMap("href,defer,disabled");
                ({
                  defer: isKnown("defer"),
                  missing: isKnown("missing"),
                  attrDefer: isHtmlAttr("defer"),
                  attrAsync: isHtmlAttr("async")
                });
                """, StandardCharsets.UTF_8);

        Object result = new QinInMemoryJvmRunner().compileAndRun(
                source,
                "com.qin.runtime.generated.JsObjectCreateForOfAssignmentSmoke");
        String json = QinObjectJsonEncoder.toJson(result);
        if (!"{\"defer\":true,\"missing\":false,\"attrDefer\":true,\"attrAsync\":false}".equals(json)) {
            throw new IllegalStateException("Unexpected makeMap result: " + json);
        }
        System.out.println("QinJsObjectCreateForOfAssignmentSmokeTestMain OK");
    }
}
