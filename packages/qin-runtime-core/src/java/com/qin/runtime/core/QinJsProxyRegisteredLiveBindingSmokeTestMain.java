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
        try {
            new QinInMemoryJvmRunner().compileAndRun(
                    sourceFile,
                    root,
                    "com.qin.runtime.generated.JsProxyRegisteredLiveBindingSmoke");
        } catch (Exception ex) {
            String message = ex.getMessage() == null ? "" : ex.getMessage();
            if (!message.contains("QIN_JS_UNSUPPORTED_PROXY")) {
                throw new IllegalStateException("Expected Proxy rejection, got: " + message, ex);
            }
            System.out.println("QinJsProxyRegisteredLiveBindingSmokeTestMain OK");
            return;
        }
        throw new IllegalStateException("Expected Proxy to be rejected by the Qin JVM target");
    }
}
