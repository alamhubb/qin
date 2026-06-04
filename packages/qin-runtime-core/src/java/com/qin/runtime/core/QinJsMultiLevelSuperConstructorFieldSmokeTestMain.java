package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsMultiLevelSuperConstructorFieldSmokeTestMain {
    private QinJsMultiLevelSuperConstructorFieldSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-multi-super-constructor-field-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-js-multi-super-constructor-field\" };\n",
                StandardCharsets.UTF_8);
        String source = """
                class State {
                  constructor(sourceCode, tokenConsumerClass, tokens) {
                    this.__qin_field_cstStack = { isEmpty() { return true; } };
                    this.__qin_field_parseSuccess = true;
                  }
                }
                class Core extends State {
                  constructor(sourceCode, tokenConsumerClass, tokens) {
                    super(sourceCode, tokenConsumerClass, tokens);
                    return this.__qin_constructor_3(sourceCode, tokenConsumerClass, tokens);
                  }
                  __qin_constructor_3(sourceCode, tokenConsumerClass, tokens) {
                    null;
                  }
                }
                class Parser extends Core {
                  constructor(sourceCode) {
                    super(sourceCode, "consumer", ["token"]);
                    return this.__qin_constructor_1(sourceCode);
                  }
                  __qin_constructor_1(sourceCode) {
                    null;
                  }
                  execute(callback) {
                    return callback();
                  }
                  read() {
                    return this.execute(() => this.__qin_field_cstStack.isEmpty());
                  }
                }
                const parser = new Parser("source");
                parser.read();
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_multi_super_constructor_field");
        if (!Boolean.TRUE.equals(result)) {
            throw new IllegalStateException("Expected inherited constructor field result true, got: " + result);
        }
        String generatedDispatcherSource = """
                class State {
                  constructor(...__qin_args) {
                    switch (__qin_args.length) {
                      case 3: {
                        const sourceCode = __qin_args[0];
                        const tokenConsumerClass = __qin_args[1];
                        const tokens = __qin_args[2];
                        return this.__qin_constructor_3(sourceCode, tokenConsumerClass, tokens);
                      }
                      default: throw new Error("Unsupported Java constructor arity: State/" + __qin_args.length);
                    }
                  }
                  __qin_constructor_3(sourceCode, tokenConsumerClass, tokens) {
                    this.__qin_field_cstStack = { isEmpty() { return true; } };
                    this.__qin_field_parseSuccess = true;
                  }
                }
                class Core extends State {
                  constructor(...__qin_args) {
                    switch (__qin_args.length) {
                      case 3: {
                        const sourceCode = __qin_args[0];
                        const tokenConsumerClass = __qin_args[1];
                        const tokens = __qin_args[2];
                        super(sourceCode, tokenConsumerClass, tokens);
                        return this.__qin_constructor_3(sourceCode, tokenConsumerClass, tokens);
                      }
                      default: throw new Error("Unsupported Java constructor arity: Core/" + __qin_args.length);
                    }
                  }
                  __qin_constructor_3(sourceCode, tokenConsumerClass, tokens) {
                    null;
                  }
                }
                class Parser extends Core {
                  constructor(...__qin_args) {
                    switch (__qin_args.length) {
                      case 1: {
                        const sourceCode = __qin_args[0];
                        super(sourceCode, "consumer", ["token"]);
                        return this.__qin_constructor_1(sourceCode);
                      }
                      default: throw new Error("Unsupported Java constructor arity: Parser/" + __qin_args.length);
                    }
                  }
                  __qin_constructor_1(sourceCode) {
                    null;
                  }
                  execute(callback) {
                    return callback();
                  }
                  read() {
                    return this.execute(() => this.__qin_field_cstStack.isEmpty());
                  }
                }
                const parser = new Parser("source");
                parser.read();
                """;
        Object generatedDispatcherResult = new QinJsPackageRunner().runModuleSource(
                root,
                generatedDispatcherSource,
                "js_multi_super_generated_dispatcher_field");
        if (!Boolean.TRUE.equals(generatedDispatcherResult)) {
            throw new IllegalStateException("Expected generated dispatcher field result true, got: "
                    + generatedDispatcherResult);
        }
        System.out.println("QinJsMultiLevelSuperConstructorFieldSmokeTestMain OK");
    }
}
