package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsArrowObjectArrayMethodCallSmokeTestMain {
    private QinJsArrowObjectArrayMethodCallSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class ParserLike {
                  constructor() {
                    this.index = 0;
                  }
                  consume() {
                    this.index = this.index + 1;
                  }
                  or(alternatives) {
                    for (let i = 0; i < alternatives.length; i++) {
                      const alt = alternatives[i];
                      alt.alt();
                      return this.index;
                    }
                    return -1;
                  }
                  run() {
                    return this.or([
                      { alt: () => this.consume() }
                    ]);
                  }
                }

                class NestedParserLike {
                  constructor() {
                    this.index = 0;
                  }
                  consume() {
                    this.index = this.index + 1;
                  }
                  run() {
                    const outer = () => {
                      const alt = { alt: () => this.consume() };
                      alt.alt();
                    };
                    outer();
                    return this.index;
                  }
                }

                class CallbackParserLike {
                  constructor() {
                    this.index = 0;
                  }
                  consume() {
                    this.index = this.index + 1;
                  }
                  many(fn) {
                    fn();
                  }
                  run() {
                    this.many(() => {
                      const alt = { alt: () => this.consume() };
                      alt.alt();
                    });
                    return this.index;
                  }
                }

                class BaseParserLike {
                  consumeInherited() {
                    this.index = this.index + 1;
                  }
                }

                class InheritedCallbackParserLike extends BaseParserLike {
                  constructor() {
                    super();
                    this.index = 0;
                  }
                  many(fn) {
                    fn();
                  }
                  run() {
                    this.many(() => {
                      const alt = { alt: () => this.consumeInherited() };
                      alt.alt();
                    });
                    return this.index;
                  }
                }

                function LegacyRule(target, key, descriptor) {
                  const original = descriptor.value;
                  descriptor.value = function (...args) {
                    return original.apply(this, args);
                  };
                  return descriptor;
                }

                class DecoratedBaseParserLike {
                  @LegacyRule
                  consumeDecoratedInherited() {
                    this.index = this.index + 1;
                  }
                }

                class DecoratedInheritedCallbackParserLike extends DecoratedBaseParserLike {
                  constructor() {
                    super();
                    this.index = 0;
                  }
                  many(fn) {
                    fn();
                  }
                  run() {
                    this.many(() => {
                      const alt = { alt: () => this.consumeDecoratedInherited() };
                      alt.alt();
                    });
                    return this.index;
                  }
                }

                function SubhutiLikeRule(target, key, descriptor) {
                  const original = descriptor.value;
                  descriptor.value = function (...args) {
                    return this.executeRuleWrapper(original, key, this.constructor.name, ...args);
                  };
                  return descriptor;
                }

                class SubhutiLikeBaseParser {
                  executeRuleWrapper(original, ruleName, className, ...args) {
                    return original.apply(this, args);
                  }
                  @SubhutiLikeRule
                  AssignmentExpression(params = {}) {
                    if (params.In !== true) {
                      return;
                    }
                    this.index = this.index + 1;
                  }
                }

                class SubhutiLikeChildParser extends SubhutiLikeBaseParser {
                  constructor() {
                    super();
                    this.index = 0;
                  }
                  Many(fn) {
                    fn();
                  }
                  run() {
                    const params = {};
                    this.Many(() => {
                      const alt = { alt: () => this.AssignmentExpression({ ...params, In: true }) };
                      alt.alt();
                    });
                    return this.index;
                  }
                }

                new ParserLike().run()
                  + new NestedParserLike().run()
                  + new CallbackParserLike().run()
                  + new InheritedCallbackParserLike().run()
                  + new DecoratedInheritedCallbackParserLike().run()
                  + new SubhutiLikeChildParser().run();
                """;
        Path root = Files.createTempDirectory("qin-js-arrow-object-array-method-call-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsArrowObjectArrayMethodCallSmoke");
        if (!Double.valueOf(6.0d).equals(result)) {
            throw new IllegalStateException("Expected object-array arrow method call result 6, got: " + result);
        }
        System.out.println("QinJsArrowObjectArrayMethodCallSmokeTestMain OK");
    }
}
