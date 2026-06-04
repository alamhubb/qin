package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsRuntimeClassStaticMethodSmokeTestMain {
    private QinJsRuntimeClassStaticMethodSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-runtime-class-static-method-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-js-runtime-class-static-method\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                class TokenBuilder {
                  static builder() {
                    return {
                      value(text) {
                        return "token:" + text;
                      }
                    };
                  }
                }
                TokenBuilder.builder().value("ok");
                """, "js_runtime_class_static_method");
        if (!"token:ok".equals(result)) {
            throw new IllegalStateException("Expected static class method chain to produce token:ok, got: " + result);
        }
        System.out.println("QinJsRuntimeClassStaticMethodSmokeTestMain OK");
    }
}
