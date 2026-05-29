package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsArrayLengthAssignmentSmokeTestMain {
    private QinJsArrayLengthAssignmentSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const values = [1, 2, 3];
                values.length = 1;
                const grown = [];
                grown.length = 2;
                ({
                  firstLength: values.length,
                  firstValue: values[0],
                  secondValue: values[1],
                  grownLength: grown.length
                });
                """;
        Path root = Files.createTempDirectory("qin-js-array-length-assignment-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsArrayLengthAssignmentSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!Integer.valueOf(1).equals(map.get("firstLength"))
                || !Double.valueOf(1.0d).equals(map.get("firstValue"))
                || map.get("secondValue") != null
                || !Integer.valueOf(2).equals(map.get("grownLength"))) {
            throw new IllegalStateException("Unexpected array length assignment result: " + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinJsArrayLengthAssignmentSmokeTestMain OK");
    }
}
