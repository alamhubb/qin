package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsDecoratedFunctionalRunReceiverSmokeTestMain {
    private QinJsDecoratedFunctionalRunReceiverSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                function __qin_java_functional(fn) {
                  if (fn == null || fn.__qinJavaFunctional) return fn;
                  Object.defineProperty(fn, "__qinJavaFunctional", { value: true });
                  fn.get = () => fn();
                  fn.run = () => fn();
                  fn.execute = () => fn();
                  fn.apply = (...args) => fn(...args);
                  return fn;
                }

                function SubhutiLikeRule(target, key, descriptor) {
                  const original = descriptor.value;
                  descriptor.value = function (...args) {
                    const targetFun = __qin_java_functional(() => original.apply(this, args));
                    return this.executeRuleWrapper(targetFun, key, this.constructor.name, "decorated");
                  };
                  return descriptor;
                }

                class ParserBase {
                  constructor() {
                    this.trace = "";
                  }

                  executeRuleWrapper(targetFun, ruleName, className, cacheKeyExtra) {
                    return className + ":" + ruleName + ":" + cacheKeyExtra + ":" + targetFun.get();
                  }

                  Or(...alternatives) {
                    for (let i = 0; i < alternatives.length; i = i + 1) {
                      alternatives[i].run();
                    }
                    return this.trace;
                  }
                }

                class AltOnly {
                  constructor(alt) {
                    this.alt = alt;
                  }
                }

                class ParserImpl extends ParserBase {
                  @SubhutiLikeRule
                  Declaration() {
                    this.Or(__qin_java_functional(() => {
                      this.trace = this.trace + "A";
                    }));
                    return this.trace;
                  }
                }

                function BareSubhutiLikeRule(target, key, descriptor) {
                  const original = descriptor.value;
                  descriptor.value = function (...args) {
                    const targetFun = () => original.apply(this, args);
                    return this.executeRuleWrapper(targetFun, key, this.constructor.name, "bare");
                  };
                  return descriptor;
                }

                class BareParserImpl extends ParserBase {
                  @BareSubhutiLikeRule
                  Declaration() {
                    this.Or(new AltOnly(() => {
                      this.trace = this.trace + "B";
                    }));
                    return this.trace;
                  }
                }

                new ParserImpl().Declaration() + "|" + new BareParserImpl().Declaration();
                """;
        Path root = Files.createTempDirectory("qin-js-decorated-functional-run-receiver-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsDecoratedFunctionalRunReceiverSmoke");
        if (!"ParserImpl:Declaration:decorated:A|BareParserImpl:Declaration:bare:B".equals(result)) {
            throw new IllegalStateException(
                    "Expected ParserImpl:Declaration:decorated:A|BareParserImpl:Declaration:bare:B, got: "
                            + result);
        }
        System.out.println("QinJsDecoratedFunctionalRunReceiverSmokeTestMain OK");
    }
}
