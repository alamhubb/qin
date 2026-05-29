package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsTextDecoderSmokeTestMain {
    private QinJsTextDecoderSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const bytes = new Uint8Array(5);
                bytes[0] = 72;
                bytes[1] = 101;
                bytes[2] = 108;
                bytes[3] = 108;
                bytes[4] = 111;
                const text = new TextDecoder().decode(bytes);
                text === "Hello" ? 42 : 0;
                """;
        Path root = Files.createTempDirectory("qin-js-text-decoder-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-text-decoder\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "text_decoder");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42, got: " + result);
        }
        System.out.println("QinJsTextDecoderSmokeTestMain OK");
    }
}
