package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsConstructorRegisterSubclassThisSmokeTestMain {
    private QinJsConstructorRegisterSubclassThisSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                let registered = null;
                function register(value) { registered = value; }
                class Base {
                  constructor() { register(this); }
                  read() { return "base"; }
                }
                class Child extends Base {
                  constructor() {
                    super();
                    register(this);
                  }
                  read() { return "child"; }
                }
                new Child();
                const result = registered.read();
                """;
        Path root = Files.createTempDirectory("qin-js-constructor-register-subclass-this-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsConstructorRegisterSubclassThisSmoke");
        if (!"child".equals(result)) {
            throw new IllegalStateException("Expected registered subclass this to dispatch child override, got: " + result);
        }
        System.out.println("QinJsConstructorRegisterSubclassThisSmokeTestMain OK");
    }
}
