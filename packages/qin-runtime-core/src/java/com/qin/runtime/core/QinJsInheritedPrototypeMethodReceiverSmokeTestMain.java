package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsInheritedPrototypeMethodReceiverSmokeTestMain {
    private QinJsInheritedPrototypeMethodReceiverSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class ParserBase {
                  enter() {
                    return this.step();
                  }

                  step() {
                    return this.value();
                  }
                }

                class ParserImpl extends ParserBase {
                  value() {
                    return "ok";
                  }
                }

                new ParserImpl().enter();
                """;
        Path root = Files.createTempDirectory("qin-js-inherited-prototype-method-receiver-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsInheritedPrototypeMethodReceiverSmoke");
        if (!"ok".equals(result)) {
            throw new IllegalStateException("Expected ok, got: " + result);
        }
        System.out.println("QinJsInheritedPrototypeMethodReceiverSmokeTestMain OK");
    }
}
