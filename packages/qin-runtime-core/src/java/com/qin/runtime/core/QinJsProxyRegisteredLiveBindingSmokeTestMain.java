package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsProxyRegisteredLiveBindingSmokeTestMain {
    private QinJsProxyRegisteredLiveBindingSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Base {
                  read() { return "base"; }
                }
                class Child extends Base {
                  read() { return "child"; }
                }
                let current = new Base();
                function register(value) {
                  current = value;
                }
                const proxy = new Proxy({}, {
                  get(_, prop) {
                    const val = current[prop];
                    return typeof val === "function" ? val.bind(current) : val;
                  }
                });
                register(new Child());
                const result = proxy.read();
                """;
        Path root = Files.createTempDirectory("qin-js-proxy-registered-live-binding-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsProxyRegisteredLiveBindingSmoke");
        if (!"child".equals(result)) {
            throw new IllegalStateException("Expected proxy live binding child result, got: " + result);
        }
        System.out.println("QinJsProxyRegisteredLiveBindingSmokeTestMain OK");
    }
}
