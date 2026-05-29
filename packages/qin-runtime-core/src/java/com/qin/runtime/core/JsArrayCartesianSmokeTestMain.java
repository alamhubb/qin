package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class JsArrayCartesianSmokeTestMain {
    private JsArrayCartesianSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-array-cartesian-");
        Path source = root.resolve("cartesian.js");
        Files.writeString(source, """
                function cartesianProduct(arrays) {
                  return arrays.reduce(
                    (acc, arr) => acc.flatMap(x => arr.map(y => [...x, y])),
                    [[]]
                  );
                }

                const inputs = [[{ value: 'left' }], [{ value: 'right' }]];
                const combinations = cartesianProduct(inputs);
                const { value } = combinations[0][0];
                ({ value, size: combinations.length, firstSize: combinations[0].length });
                """, StandardCharsets.UTF_8);

        Object result = new QinInMemoryJvmRunner().compileAndRun(
                source,
                root,
                "com.qin.runtime.generated.JsArrayCartesianSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result map, got: " + result);
        }
        if (!"left".equals(map.get("value"))) {
            throw new IllegalStateException("Expected value=left, got: " + QinObjectJsonEncoder.toJson(map));
        }
        if (!(map.get("size") instanceof Number size) || size.intValue() != 1) {
            throw new IllegalStateException("Expected one combination, got: " + QinObjectJsonEncoder.toJson(map));
        }
        Object firstSize = map.get("firstSize");
        if (firstSize instanceof Number firstSizeNumber && firstSizeNumber.intValue() == 2) {
            System.out.println("JsArrayCartesianSmokeTestMain passed.");
            return;
        }
        if (firstSize instanceof List<?> list && list.size() == 2) {
            System.out.println("JsArrayCartesianSmokeTestMain passed.");
            return;
        }
        throw new IllegalStateException("Expected first combination size=2, got: " + QinObjectJsonEncoder.toJson(map));
    }
}
