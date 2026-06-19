package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsSubhutiRawModuleThisSmokeTestMain {
    private QinJsSubhutiRawModuleThisSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-subhuti-raw-this-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-subhuti-raw-this\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                const moduleRules = {
                  __qin_subhuti_raw_Demo() {
                    const nested = () => this.consume("Hash");
                    return nested();
                  }
                };
                const parser = {
                  consume(token) {
                    return token + ":parser";
                  },
                  run() {
                    return moduleRules.__qin_subhuti_raw_Demo();
                  }
                };
                parser.run();
                """, "js_subhuti_raw_module_this");
        if (!"Hash:parser".equals(result)) {
            throw new IllegalStateException("Expected raw rule to inherit parser this, got: " + result);
        }

        Object classResult = new QinJsPackageRunner().runModuleSource(root, """
                function __qin_java_functional(fn) {
                  if (fn == null || fn.__qinJavaFunctional) return fn;
                  Object.defineProperty(fn, "__qinJavaFunctional", { value: true });
                  fn.get = () => fn();
                  fn.run = () => fn();
                  fn.execute = () => fn();
                  fn.apply = (...args) => fn(...args);
                  return fn;
                }

                class Parser {
                  constructor() {
                    this.trace = "";
                    this.left = 1;
                  }

                  executeRuleWrapper(targetFun) {
                    return targetFun.get();
                  }

                  tryAndRestore(fn) {
                    if (this.left <= 0) {
                      return false;
                    }
                    this.left = this.left - 1;
                    fn.run();
                    return true;
                  }

                  Many(rule) {
                    rule = __qin_java_functional(rule);
                    while (this.tryAndRestore(rule)) {
                    }
                    return null;
                  }

                  consume(token) {
                    this.trace = this.trace + token;
                    return this.trace;
                  }

                  ModuleBody() {
                    return this.executeRuleWrapper(__qin_java_functional(() => {
                      return this.__qin_subhuti_raw_ModuleBody();
                    }));
                  }

                  __qin_subhuti_raw_ModuleBody() {
                    this.ModuleItemList();
                    return this.trace;
                  }

                  ModuleItemList() {
                    return this.executeRuleWrapper(__qin_java_functional(() => {
                      return this.__qin_subhuti_raw_ModuleItemList();
                    }));
                  }

                  __qin_subhuti_raw_ModuleItemList() {
                    this.consume("A");
                    this.Many(__qin_java_functional(() => {
                      this.consume("B");
                      return null;
                    }));
                    return this.trace;
                  }
                }

                class Alternative {
                  constructor(supplier) {
                    this.supplier = supplier;
                  }

                  static of(supplier) {
                    return new Alternative(__qin_java_functional(supplier));
                  }

                  execute() {
                    return this.supplier.get();
                  }
                }

                class OvsLikeParser extends Parser {
                  Or(...alternatives) {
                    return alternatives[0].execute();
                  }

                  Statement() {
                    return this.Or(Alternative.of(() => this.consume("C")));
                  }
                }

                const parser = new OvsLikeParser();
                parser.ModuleBody() + parser.Statement();
                """, "js_subhuti_class_raw_module_this");
        if (!"ABABC".equals(classResult)) {
            throw new IllegalStateException("Expected class raw rule chain ABABC, got: " + classResult);
        }

        Object decoratedResult = new QinJsPackageRunner().runModuleSource(root, """
                function __qin_java_functional(fn) {
                  if (fn == null || fn.__qinJavaFunctional) return fn;
                  Object.defineProperty(fn, "__qinJavaFunctional", { value: true });
                  fn.get = () => fn();
                  fn.run = () => fn();
                  fn.execute = () => fn();
                  fn.apply = (...args) => fn(...args);
                  return fn;
                }

                function SubhutiRule(target, key, descriptor) {
                  const originalMethod = descriptor.value;
                  const wrappedFunction = function (...args) {
                    const receiver = this;
                    const targetFun = () => originalMethod.apply(receiver, args);
                    return receiver.executeRuleWrapper(targetFun, key, receiver.constructor.name, "");
                  };
                  descriptor.value = wrappedFunction;
                  return descriptor;
                }

                class Alternative {
                  constructor(supplier) {
                    this.supplier = supplier;
                  }

                  static of(supplier) {
                    return new Alternative(__qin_java_functional(supplier));
                  }

                  execute() {
                    return this.supplier.get();
                  }
                }

                class Parser {
                  constructor() {
                    this.trace = "";
                  }

                  executeRuleWrapper(targetFun) {
                    return targetFun.get();
                  }

                  Or(...alternatives) {
                    return alternatives[0].execute();
                  }

                  consume(token) {
                    this.trace = this.trace + token;
                    return this.trace;
                  }

                  @SubhutiRule
                  Statement() {
                    return this.Or(Alternative.of(() => this.consume("D")));
                  }
                }

                new Parser().Statement();
                """, "js_subhuti_decorated_alternative_this");
        if (!"D".equals(decoratedResult)) {
            throw new IllegalStateException("Expected decorated alternative D, got: " + decoratedResult);
        }

        Object distDecoratedResult = new QinJsPackageRunner().runModuleSource(root, """
                function __qin_java_functional(fn) {
                  if (fn == null || fn.__qinJavaFunctional) return fn;
                  Object.defineProperty(fn, "__qinJavaFunctional", { value: true });
                  fn.get = () => fn();
                  fn.run = () => fn();
                  fn.execute = () => fn();
                  fn.apply = (...args) => fn(...args);
                  return fn;
                }

                function SubhutiRuleDist(target, key, descriptor) {
                  const originalMethod = descriptor.value;
                  const wrappedFunction = function (...args) {
                    return this.executeRuleWrapper(originalMethod, key, this.constructor.name, ...args);
                  };
                  descriptor.value = wrappedFunction;
                  return descriptor;
                }

                class Alternative {
                  constructor(supplier) {
                    this.supplier = supplier;
                  }

                  static of(supplier) {
                    return new Alternative(__qin_java_functional(supplier));
                  }

                  execute() {
                    return this.supplier.get();
                  }
                }

                class Parser {
                  constructor() {
                    this.trace = "";
                  }

                  executeRuleWrapper(targetFun, ruleName, className, ...args) {
                    return this.executeRuleCore(ruleName, targetFun, ...args);
                  }

                  executeRuleCore(ruleName, targetFun, ...args) {
                    targetFun.apply(this, args);
                    return this.trace;
                  }

                  Or(...alternatives) {
                    return alternatives[0].execute();
                  }

                  consume(token) {
                    this.trace = this.trace + token;
                    return this.trace;
                  }

                  @SubhutiRuleDist
                  Statement() {
                    return this.Or(Alternative.of(() => this.consume("E")));
                  }
                }

                new Parser().Statement();
                """, "js_subhuti_dist_decorated_alternative_this");
        if (!"E".equals(distDecoratedResult)) {
            throw new IllegalStateException("Expected dist decorated alternative E, got: " + distDecoratedResult);
        }

        Object mixedGeneratedSlimeResult = new QinJsPackageRunner().runModuleSource(root, """
                function __qin_java_functional(fn) {
                  if (fn == null || fn.__qinJavaFunctional) return fn;
                  Object.defineProperty(fn, "__qinJavaFunctional", { value: true });
                  fn.get = () => fn();
                  fn.run = () => fn();
                  fn.execute = () => fn();
                  fn.apply = (...args) => fn(...args);
                  return fn;
                }

                function SubhutiRuleDist(target, key, descriptor) {
                  const originalMethod = descriptor.value;
                  const wrappedFunction = function (...args) {
                    return this.executeRuleWrapper(originalMethod, key, this.constructor.name, ...args);
                  };
                  descriptor.value = wrappedFunction;
                  return descriptor;
                }

                class Alternative {
                  constructor(supplier) {
                    this.supplier = supplier;
                  }

                  static of(supplier) {
                    return new Alternative(__qin_java_functional(supplier));
                  }

                  execute() {
                    return this.supplier.get();
                  }
                }

                class GeneratedJavaSlimeBase {
                  constructor() {
                    this.trace = "";
                  }

                  executeRuleWrapper(targetFun, ruleName, className, ...args) {
                    return this.executeRuleCore(ruleName, __qin_java_functional(targetFun), ...args);
                  }

                  executeRuleCore(ruleName, targetFun, ...args) {
                    targetFun.get();
                    return this.trace;
                  }

                  Or(...alternatives) {
                    return alternatives[0].execute();
                  }

                  StatementList() {
                    this.trace = this.trace + "L";
                    return this.trace;
                  }
                }

                class OvsLikeParser extends GeneratedJavaSlimeBase {
                  @SubhutiRuleDist
                  OvsRenderStatement() {
                    return this.StatementList();
                  }

                  @SubhutiRuleDist
                  Statement() {
                    return this.Or(Alternative.of(() => this.OvsRenderStatement()));
                  }
                }

                new OvsLikeParser().Statement();
                """, "js_subhuti_generated_slime_get_receiver");
        if (!"L".equals(mixedGeneratedSlimeResult)) {
            throw new IllegalStateException("Expected mixed generated slime receiver L, got: " + mixedGeneratedSlimeResult);
        }

        Object generatedConsumeAliasResult = new QinJsPackageRunner().runModuleSource(root, """
                function __qin_java_functional(fn) {
                  if (fn == null || fn.__qinJavaFunctional) return fn;
                  Object.defineProperty(fn, "__qinJavaFunctional", { value: true });
                  fn.get = () => fn();
                  fn.run = () => fn();
                  fn.execute = () => fn();
                  fn.apply = (...args) => fn(...args);
                  return fn;
                }

                function SubhutiRuleDist(target, key, descriptor) {
                  const originalMethod = descriptor.value;
                  const wrappedFunction = function (...args) {
                    return this.executeRuleWrapper(originalMethod, key, this.constructor.name, ...args);
                  };
                  descriptor.value = wrappedFunction;
                  return descriptor;
                }

                class GeneratedJavaSlimeBase {
                  constructor() {
                    this.trace = "";
                  }

                  executeRuleWrapper(targetFun, ruleName, className, ...args) {
                    return this.executeRuleCore(ruleName, __qin_java_functional(targetFun), ...args);
                  }

                  executeRuleCore(ruleName, targetFun, ...args) {
                    targetFun.get();
                    return this.trace;
                  }

                  token(tokenName) {
                    this.trace = this.trace + tokenName;
                    return this.trace;
                  }
                }

                class OvsLikeParser extends GeneratedJavaSlimeBase {
                  @SubhutiRuleDist
                  NoRenderBlock() {
                    return this.consume("Hash");
                  }
                }

                new OvsLikeParser().NoRenderBlock();
                """, "js_subhuti_generated_slime_consume_alias");
        if (!"Hash".equals(generatedConsumeAliasResult)) {
            throw new IllegalStateException(
                    "Expected generated slime consume alias Hash, got: " + generatedConsumeAliasResult);
        }

        Object inheritedSuperRawRuleResult = new QinJsPackageRunner().runModuleSource(root, """
                class BaseParser {
                  constructor() {
                    this.trace = "";
                  }

                  __qin_subhuti_raw_Declaration() {
                    this.trace = this.trace + "B";
                    return this.trace;
                  }
                }

                class SlimeParserLike extends BaseParser {
                  __qin_subhuti_raw_Declaration() {
                    this.trace = this.trace + "M";
                    return super.__qin_subhuti_raw_Declaration();
                  }

                  Declaration() {
                    return this.__qin_subhuti_raw_Declaration();
                  }
                }

                class CssTsParserLike extends SlimeParserLike {
                }

                new CssTsParserLike().Declaration();
                """, "js_subhuti_inherited_super_raw_rule");
        if (!"MB".equals(inheritedSuperRawRuleResult)) {
            throw new IllegalStateException(
                    "Expected inherited super raw rule MB, got: " + inheritedSuperRawRuleResult);
        }

        Object inheritedSuperRawRuleSkipsIntermediateResult = new QinJsPackageRunner().runModuleSource(root, """
                class BaseParser {
                  constructor() {
                    this.trace = "";
                  }

                  __qin_subhuti_raw_Declaration() {
                    this.trace = this.trace + "A";
                    return this.trace;
                  }
                }

                class IntermediateParser extends BaseParser {
                }

                class TypeScriptParserLike extends IntermediateParser {
                  __qin_subhuti_raw_Declaration() {
                    this.trace = this.trace + "C";
                    return super.__qin_subhuti_raw_Declaration();
                  }

                  Declaration() {
                    return this.__qin_subhuti_raw_Declaration();
                  }
                }

                class CssTsParserLike extends TypeScriptParserLike {
                }

                new CssTsParserLike().Declaration();
                """, "js_subhuti_inherited_super_raw_rule_skips_intermediate");
        if (!"CA".equals(inheritedSuperRawRuleSkipsIntermediateResult)) {
            throw new IllegalStateException(
                    "Expected inherited super raw rule with intermediate CA, got: "
                            + inheritedSuperRawRuleSkipsIntermediateResult);
        }
        System.out.println("QinJsSubhutiRawModuleThisSmokeTestMain OK");
    }
}
