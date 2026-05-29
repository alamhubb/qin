package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsThrowCatchAcrossCallSmokeTestMain {
    private QinJsThrowCatchAcrossCallSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                function fail() {
                  throw { name: "ParsingError", token: "Const" };
                }
                function wrapper() {
                  try {
                    fail();
                    return "miss";
                  } catch (error) {
                    return error.name + ":" + error.token;
                  }
                }
                const result = wrapper();
                """;
        Path root = Files.createTempDirectory("qin-js-throw-catch-across-call-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsThrowCatchAcrossCallSmoke");
        if (!"ParsingError:Const".equals(result)) {
            throw new IllegalStateException("Expected caught thrown object, got: " + result);
        }
        System.out.println("QinJsThrowCatchAcrossCallSmokeTestMain OK");
    }
}
