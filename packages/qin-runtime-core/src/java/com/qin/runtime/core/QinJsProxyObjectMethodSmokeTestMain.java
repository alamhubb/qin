package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsProxyObjectMethodSmokeTestMain {
    private QinJsProxyObjectMethodSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const proxy = new Proxy({}, {
                  get(_, prop) { return prop; }
                });
                const result = proxy.demo;
                """;
        Path root = Files.createTempDirectory("qin-js-proxy-object-method-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsProxyObjectMethodSmoke");
        if (!"demo".equals(result)) {
            throw new IllegalStateException("Expected proxy get result demo, got: " + result);
        }
        System.out.println("QinJsProxyObjectMethodSmokeTestMain OK");
    }
}
