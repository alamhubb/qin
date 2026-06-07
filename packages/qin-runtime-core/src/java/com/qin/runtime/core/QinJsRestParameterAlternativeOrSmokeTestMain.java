package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsRestParameterAlternativeOrSmokeTestMain {
    private QinJsRestParameterAlternativeOrSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-rest-parameter-alternative-or-");
        Path source = root.resolve("main.js");
        Files.writeString(source, """
                class Alternative {
                  constructor(alt) {
                    this.alt = alt;
                  }
                  static of(supplier) {
                    return new Alternative(supplier);
                  }
                }
                function Or(alternatives, ...additionalAlternatives) {
                  const normalizedAlternatives = Array.isArray(alternatives)
                    ? alternatives
                    : [alternatives, ...additionalAlternatives];
                  let result = 0;
                  for (const alt of normalizedAlternatives) {
                    result = alt.alt();
                    if (result) return result;
                  }
                  return result;
                }
                Or(Alternative.of(() => 0), Alternative.of(() => 7));
                """, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                source,
                root,
                "com.qin.runtime.generated.RestParameterAlternativeOrSmoke");
        if (!(result instanceof Number number) || number.intValue() != 7) {
            throw new IllegalStateException("Expected rest parameter Alternative Or to return 7, got: " + result);
        }
        System.out.println("QinJsRestParameterAlternativeOrSmokeTestMain OK");
    }
}
