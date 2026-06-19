package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsOverloadDispatcherThisSmokeTestMain {
    private QinJsOverloadDispatcherThisSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-overload-dispatcher-this-");
        Files.writeString(
                root.resolve("qin.config.js"),
                "export default { name: \"qin-js-overload-dispatcher-this\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                """
                function __qin_java_functional(fn) {
                  if (fn == null || fn.__qinJavaFunctional) return fn;
                  Object.defineProperty(fn, "__qinJavaFunctional", { value: true });
                  fn.get = () => fn();
                  fn.run = () => fn();
                  fn.execute = () => fn();
                  fn.apply = (...args) => fn(...args);
                  return fn;
                }

                let __qin_subhuti_next_rule_cache_id = 1;
                const __qin_subhuti_rule_cache_identity_ids = new WeakMap();
                const __qin_subhuti_rule_cache_value_buckets = new Map();
                function __qin_java_values_equal__(left, right) {
                  if (left === right) return true;
                  if (left == null || right == null) return false;
                  if ((typeof left === "object" || typeof left === "function")
                      && typeof left.equals === "function") {
                    return left.equals(right) === true;
                  }
                  return false;
                }
                function __qin_java_value_hash_code__(value) {
                  if (value == null) return 0;
                  if ((typeof value === "object" || typeof value === "function")
                      && typeof value.hashCode === "function") {
                    return value.hashCode();
                  }
                  return 1;
                }
                function __qin_java_hash_key__(key) {
                  return "hash:" + String(__qin_java_value_hash_code__(key));
                }
                function __qin_java_hash_key_equals__(left, right) {
                  return __qin_java_values_equal__(left, right);
                }
                function __qin_subhuti_identity_rule_cache_id(value) {
                  if (!__qin_subhuti_rule_cache_identity_ids.has(value)) {
                    __qin_subhuti_rule_cache_identity_ids.set(value, __qin_subhuti_next_rule_cache_id++);
                  }
                  return __qin_subhuti_rule_cache_identity_ids.get(value);
                }
                function __qin_subhuti_value_rule_cache_id(value) {
                  const hash = __qin_java_hash_key__(value);
                  let bucket = __qin_subhuti_rule_cache_value_buckets.get(hash);
                  if (bucket == null) {
                    bucket = [];
                    __qin_subhuti_rule_cache_value_buckets.set(hash, bucket);
                  }
                  for (const entry of bucket) {
                    if (__qin_java_hash_key_equals__(entry.value, value)) {
                      return entry.id;
                    }
                  }
                  const id = __qin_subhuti_next_rule_cache_id++;
                  bucket.push({ value, id });
                  return id;
                }
                function __qin_subhuti_rule_cache_key(args) {
                  if (args == null || args.length === 0) return "";
                  const format = (value) => {
                    if (value == null) return "null";
                    const type = typeof value;
                    if (type === "string") return value;
                    if (type === "number" || type === "boolean" || type === "bigint") return "" + value;
                    if (Array.isArray(value)) return "[" + value.map(format).join(", ") + "]";
                    if (type === "object" || type === "function") {
                      if (typeof value.hashCode === "function" || typeof value.equals === "function") {
                        return type + "#value:" + __qin_subhuti_value_rule_cache_id(value);
                      }
                      return type + "#identity:" + __qin_subhuti_identity_rule_cache_id(value);
                    }
                    return "" + value;
                  };
                  const parts = [];
                  for (let i = 0; i < args.length; i++) {
                    parts.push(format(args[i]));
                  }
                  return "[" + parts.join(", ") + "]";
                }

                class ParserBase {
                  getClass() {
                    return { getSimpleName() { return "ParserImpl"; } };
                  }

                  executeRuleWrapper(...__qin_args) {
                    if (__qin_args.length === 3
                        && (__qin_args[0] === null || typeof __qin_args[0] === "function" || __qin_args[0].__qinJavaFunctional === true)
                        && (__qin_args[1] === null || typeof __qin_args[1] === "string")
                        && (__qin_args[2] === null || typeof __qin_args[2] === "string")) {
                      return this.__qin_overload_executeRuleWrapper_3_0(...__qin_args);
                    }
                    if (__qin_args.length === 4
                        && (__qin_args[0] === null || typeof __qin_args[0] === "function" || __qin_args[0].__qinJavaFunctional === true)
                        && (__qin_args[1] === null || typeof __qin_args[1] === "string")
                        && (__qin_args[2] === null || typeof __qin_args[2] === "string")
                        && (__qin_args[3] === null || typeof __qin_args[3] === "string")) {
                      return this.__qin_overload_executeRuleWrapper_4_1(...__qin_args);
                    }
                    throw new Error("Unsupported Java overload: executeRuleWrapper/" + __qin_args.length);
                  }

                  __qin_overload_executeRuleWrapper_3_0(targetFun, ruleName, className) {
                    return this.executeRuleWrapper(targetFun, ruleName, className, "");
                  }

                  __qin_overload_executeRuleWrapper_4_1(targetFun, ruleName, className, cacheKeyExtra) {
                    return className + ":" + ruleName + ":" + cacheKeyExtra + ":" + targetFun.get();
                  }

                  parseFinal(rule, ruleName) {
                    return this.executeRuleWrapper(rule, ruleName, this.getClass().getSimpleName(), __qin_subhuti_rule_cache_key(arguments));
                  }

                  parseParam(params) {
                    return this.executeRuleWrapper(
                      __qin_java_functional(() => this.raw()),
                      "Declaration",
                      this.getClass().getSimpleName(),
                      __qin_subhuti_rule_cache_key([params]));
                  }
                }

                class ParamBox {
                  hashCode() {
                    return 42;
                  }

                  equals(other) {
                    return this === other;
                  }
                }

                class ParserImpl extends ParserBase {
                  hashCode() {
                    return 7;
                  }

                  equals(other) {
                    return this === other;
                  }

                  raw() {
                    return "ok";
                  }

                  run() {
                    return this.parseFinal(__qin_java_functional(() => this.raw()), "raw")
                      + "|" + this.parseParam(new ParamBox());
                  }
                }

                new ParserImpl().run();
                """,
                "js_overload_dispatcher_this");
        if (!"ParserImpl:raw:[function#identity:1, raw]:ok|ParserImpl:Declaration:[object#value:2]:ok".equals(result)) {
            throw new IllegalStateException(
                    "Expected ParserImpl:raw:[function#identity:1, raw]:ok|ParserImpl:Declaration:[object#value:2]:ok, got: "
                            + result);
        }
        System.out.println("QinJsOverloadDispatcherThisSmokeTestMain OK");
    }
}
