package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsFunctionalAdapterMethodsSmokeTestMain {
    private QinJsFunctionalAdapterMethodsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                function value(prefix) {
                  return prefix ? prefix + "-ok" : "ok";
                }

                function __qin_java_functional(fn) {
                  if (fn == null || fn.__qinJavaFunctional) return fn;
                  Object.defineProperty(fn, "__qinJavaFunctional", { value: true });
                  fn.get = () => fn();
                  fn.run = () => fn();
                  fn.execute = () => fn();
                  fn.apply = (...args) => fn(...args);
                  return fn;
                }

                const adapted = __qin_java_functional(value);
                const box = {
                  value: "box-ok",
                  run() {
                    return __qin_java_functional(() => this.value).get();
                  }
                };
                class Alternative {
                  constructor(alt) {
                    this.alt = alt;
                  }

                  static of(alt) {
                    return new Alternative(alt);
                  }

                  run() {
                    return this.alt();
                  }
                }
                let bareHit = false;
                const bare = () => {
                  bareHit = true;
                  return "bare-ok";
                };
                const alternative = Alternative.of(() => "alternative-ok");

                ({
                  bareGetValue: bare.get(),
                  bareHit,
                  getValue: adapted.get(),
                  runValue: adapted.run(),
                  executeValue: adapted.execute(),
                  applyValue: adapted.apply("qin"),
                  lexicalThisValue: box.run(),
                  alternativeRunValue: alternative.run()
                });
                """;
        Path root = Files.createTempDirectory("qin-js-functional-adapter-methods-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsFunctionalAdapterMethodsSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        assertValue(map, "bareGetValue", "bare-ok");
        if (!Boolean.TRUE.equals(map.get("bareHit"))) {
            throw new IllegalStateException("Expected bare function get() to call function, got: " + map.get("bareHit"));
        }
        assertValue(map, "getValue", "ok");
        assertValue(map, "runValue", "ok");
        assertValue(map, "executeValue", "ok");
        assertValue(map, "applyValue", "qin-ok");
        assertValue(map, "lexicalThisValue", "box-ok");
        assertValue(map, "alternativeRunValue", "alternative-ok");
        System.out.println("QinJsFunctionalAdapterMethodsSmokeTestMain OK");
    }

    private static void assertValue(Map<?, ?> map, String key, String expected) {
        Object actual = map.get(key);
        if (!expected.equals(actual)) {
            throw new IllegalStateException("Expected " + key + " " + expected + ", got: " + actual);
        }
    }
}
