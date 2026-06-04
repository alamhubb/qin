package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsTransitiveInheritedMethodSmokeTestMain {
    private QinJsTransitiveInheritedMethodSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class BaseConsumer {
                  setParser(parser) {
                    this.parser = parser;
                    return "set:" + parser;
                  }
                }
                class JavascriptConsumer extends BaseConsumer {
                }
                class SlimeConsumer extends JavascriptConsumer {
                }
                const consumer = new SlimeConsumer();
                consumer.setParser("qin");
                consumer.parser;
                """;
        Path root = Files.createTempDirectory("qin-js-transitive-inherited-method-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsTransitiveInheritedMethodSmoke");
        if (!"qin".equals(result)) {
            throw new IllegalStateException("Expected inherited parser field qin, got: " + result);
        }
        System.out.println("QinJsTransitiveInheritedMethodSmokeTestMain OK");
    }
}
