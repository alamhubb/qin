package com.qin.parser;

import com.subhuti.struct.SubhutiCst;

/**
 * Focused smoke for generated Java SDK helper sources that combine ordinary
 * member chains, calls, arrow arguments, new expressions, and relational
 * operators. These are standard JavaScript/TypeScript expression shapes used
 * by the generated parser package support code, not grammar-specific
 * workarounds.
 */
public final class QinParserCallMemberChainSmokeTestMain {
    private QinParserCallMemberChainSmokeTestMain() {
    }

    public static void main(String[] args) {
        assertProgramParses("plain member call", 1, 1, 0, """
                function f(value) {
                  const mapped = value.map((item) => Number(item));
                }
                """);
        assertProgramParses("plain member call with binary arrow body", 1, 1, 0, """
                function f(value) {
                  const mapped = value.map((item) => Number(item) & 0xff);
                }
                """);
        assertProgramParses("nested member call argument", 2, 2, 0, """
                function f(value) {
                  return Uint8Array.from(value.map((item) => Number(item)));
                }
                """);
        assertProgramParses("nested member call argument with binary arrow body", 2, 2, 0, """
                function f(value) {
                  return Uint8Array.from(value.map((item) => Number(item) & 0xff));
                }
                """);
        assertProgramParses("new expression member call", 1, 1, 1, """
                function f(value) {
                  return new TextEncoder().encode(value);
                }
                """);
        assertProgramParses("instanceof member expression", 0, 1, 0, """
                function f(value) {
                  return value.buffer instanceof ArrayBuffer;
                }
                """);
        assertProgramParses("new expression with member arguments", 0, 3, 1, """
                function f(value) {
                  return new Uint8Array(value.buffer, value.byteOffset || 0, value.byteLength);
                }
                """);
        assertProgramParses("this member chain call", 1, 1, 0, """
                class Box {
                  clear() {
                    this.__entries.clear();
                  }
                }
                """);
        assertProgramParses("export class this member chain call", 1, 1, 0, """
                export class Box {
                  clear() {
                    this.__entries.clear();
                  }
                }
                """);
        assertProgramParses("getter-name ordinary method before clear", 3, 4, 0, """
                export class Box {
                  get(key) {
                    return this.__entries.has(key) ? this.__entries.get(key) : null;
                  }
                  clear() {
                    this.__entries.clear();
                  }
                }
                """);
        assertProgramParses("size property method before clear", 1, 2, 0, """
                export class Box {
                  size() {
                    return this.__entries.size;
                  }
                  clear() {
                    this.__entries.clear();
                  }
                }
                """);
        assertProgramParses("constructor assignment before clear", 2, 2, 1, """
                export class Box {
                  constructor() {
                    const __QinJsMap = __qin_builtin_constructor__("Map");
                    this.__entries = new __QinJsMap();
                  }
                  clear() {
                    this.__entries.clear();
                  }
                }
                """);
        assertProgramParses("identity hash map prefix before clear", 14, 16, 2, """
                export class Box {
                  constructor(initialEntries) {
                    const __QinJsMap = __qin_builtin_constructor__("Map");
                    this.__entries = new __QinJsMap();
                    if (initialEntries != null) {
                      for (const entry of initialEntries) {
                        this.put(entry[0], entry[1]);
                      }
                    }
                  }
                  put(key, value) {
                    const hadKey = this.__entries.has(key);
                    const previous = hadKey ? this.__entries.get(key) : null;
                    this.__entries.set(key, value);
                    return hadKey ? previous : null;
                  }
                  get(key) {
                    return this.__entries.has(key) ? this.__entries.get(key) : null;
                  }
                  getOrDefault(key, defaultValue) {
                    return this.__entries.has(key) ? this.__entries.get(key) : defaultValue;
                  }
                  putIfAbsent(key, value) {
                    if (!this.__entries.has(key)) {
                      this.__entries.set(key, value);
                      return null;
                    }
                    const previous = this.__entries.get(key);
                    if (previous == null) {
                      this.__entries.set(key, value);
                    }
                    return previous;
                  }
                  values() {
                    return new __QinJavaUtilArrayList(Array.from(this.__entries.values()));
                  }
                  keys() {
                    return new __QinJavaUtilArrayList(Array.from(this.__entries.keys()));
                  }
                  clear() {
                    this.__entries.clear();
                  }
                }
                """);
        assertProgramParses("identity hash map compute before clear", 6, 8, 0, """
                export class Box {
                  computeIfAbsent(key, mappingFunction) {
                    if (!this.__entries.has(key) || this.__entries.get(key) == null) {
                      const value = mappingFunction(key);
                      this.__entries.set(key, value);
                      return value;
                    }
                    return this.__entries.get(key);
                  }
                  clear() {
                    this.__entries.clear();
                  }
                }
                """);
        assertProgramParses("identity hash map merge before clear", 7, 8, 0, """
                export class Box {
                  merge(key, value, remappingFunction) {
                    if (!this.__entries.has(key)) {
                      this.__entries.set(key, value);
                      return value;
                    }
                    const previous = this.__entries.get(key);
                    if (previous == null) {
                      this.__entries.set(key, value);
                      return value;
                    }
                    const nextValue = remappingFunction(previous, value);
                    if (nextValue == null) {
                      this.__entries.delete(key);
                      return null;
                    }
                    this.__entries.set(key, nextValue);
                    return nextValue;
                  }
                  clear() {
                    this.__entries.clear();
                  }
                }
                """);
        assertProgramParses("identity hash map suffix before clear", 5, 7, 0, """
                export class Box {
                  containsKey(key) {
                    return this.__entries.has(key);
                  }
                  remove(key) {
                    if (!this.__entries.has(key)) {
                      return null;
                    }
                    const previous = this.__entries.get(key);
                    this.__entries.delete(key);
                    return previous;
                  }
                  size() {
                    return this.__entries.size;
                  }
                  isEmpty() {
                    return this.__entries.size === 0;
                  }
                  clear() {
                    this.__entries.clear();
                  }
                }
                """);
        assertProgramParses("generated identity hash map member chains", 22, 24, 2, """
                export class __QinJavaUtilIdentityHashMap {
                  constructor(initialEntries) {
                    const __QinJsMap = __qin_builtin_constructor__("Map");
                    this.__entries = new __QinJsMap();
                    if (initialEntries != null) {
                      for (const entry of initialEntries) {
                        this.put(entry[0], entry[1]);
                      }
                    }
                  }
                  put(key, value) {
                    const hadKey = this.__entries.has(key);
                    const previous = hadKey ? this.__entries.get(key) : null;
                    this.__entries.set(key, value);
                    return hadKey ? previous : null;
                  }
                  get(key) {
                    return this.__entries.has(key) ? this.__entries.get(key) : null;
                  }
                  getOrDefault(key, defaultValue) {
                    return this.__entries.has(key) ? this.__entries.get(key) : defaultValue;
                  }
                  putIfAbsent(key, value) {
                    if (!this.__entries.has(key)) {
                      this.__entries.set(key, value);
                      return null;
                    }
                    const previous = this.__entries.get(key);
                    if (previous == null) {
                      this.__entries.set(key, value);
                    }
                    return previous;
                  }
                  values() {
                    return new __QinJavaUtilArrayList(Array.from(this.__entries.values()));
                  }
                  keys() {
                    return new __QinJavaUtilArrayList(Array.from(this.__entries.keys()));
                  }
                  computeIfAbsent(key, mappingFunction) {
                    if (!this.__entries.has(key) || this.__entries.get(key) == null) {
                      const value = mappingFunction(key);
                      this.__entries.set(key, value);
                      return value;
                    }
                    return this.__entries.get(key);
                  }
                  merge(key, value, remappingFunction) {
                    if (!this.__entries.has(key)) {
                      this.__entries.set(key, value);
                      return value;
                    }
                    const previous = this.__entries.get(key);
                    if (previous == null) {
                      this.__entries.set(key, value);
                      return value;
                    }
                    const nextValue = remappingFunction(previous, value);
                    if (nextValue == null) {
                      this.__entries.delete(key);
                      return null;
                    }
                    this.__entries.set(key, nextValue);
                    return nextValue;
                  }
                  containsKey(key) {
                    return this.__entries.has(key);
                  }
                  remove(key) {
                    if (!this.__entries.has(key)) {
                      return null;
                    }
                    const previous = this.__entries.get(key);
                    this.__entries.delete(key);
                    return previous;
                  }
                  size() {
                    return this.__entries.size;
                  }
                  isEmpty() {
                    return this.__entries.size === 0;
                  }
                  clear() {
                    this.__entries.clear();
                  }
                }
                """);
        assertProgramParses("generated streams slice", 6, 8, 2, """
                function coerceBytes(value) {
                  const mapped = value.map((item) => Number(item));
                  const mappedWithAnd = value.map((item) => Number(item) & 0xff);
                  if (Array.isArray(value)) return Uint8Array.from(value.map((item) => Number(item) & 0xff));
                  if (typeof value === "string") return new TextEncoder().encode(value);
                  if (value.buffer instanceof ArrayBuffer) {
                    return new Uint8Array(value.buffer, value.byteOffset || 0, value.byteLength);
                  }
                  return Uint8Array.from([]);
                }
                """);
        System.out.println("QinParserCallMemberChainSmokeTestMain passed.");
    }

    private static void assertProgramParses(
            String label,
            int minCallExpressions,
            int minMemberExpressions,
            int minNewExpressions,
            String source) {
        QinParserFacade facade = new QinParserFacade();
        SubhutiCst cst;
        try {
            cst = facade.createProgramCst(source);
        } catch (RuntimeException error) {
            throw new IllegalStateException("Failed case: " + label, error);
        }
        int callExpressionCount = countByName(cst, "CallExpression");
        int memberExpressionCount = countByName(cst, "MemberExpression");
        int newExpressionCount = countByName(cst, "NewExpression");
        if (callExpressionCount < minCallExpressions) {
            throw new IllegalStateException("Expected chained call expressions for " + label
                    + ", got " + callExpressionCount
                    + "\nCST sample:\n" + boundedTree(cst, 180));
        }
        if (memberExpressionCount < minMemberExpressions) {
            throw new IllegalStateException("Expected chained member expressions for " + label
                    + ", got " + memberExpressionCount
                    + "\nCST sample:\n" + boundedTree(cst, 180));
        }
        if (newExpressionCount < minNewExpressions) {
            throw new IllegalStateException("Expected new expressions for " + label
                    + ", got " + newExpressionCount
                    + "\nCST sample:\n" + boundedTree(cst, 180));
        }
    }

    private static int countByName(SubhutiCst cst, String name) {
        if (cst == null) {
            return 0;
        }
        int count = name.equals(cst.getName()) ? 1 : 0;
        if (cst.getChildren() == null) {
            return count;
        }
        for (SubhutiCst child : cst.getChildren()) {
            count += countByName(child, name);
        }
        return count;
    }

    private static String boundedTree(SubhutiCst cst, int maxLines) {
        if (cst == null) {
            return "<null>";
        }
        String[] lines = cst.toTreeString().split("\\R");
        StringBuilder builder = new StringBuilder();
        int count = Math.min(lines.length, maxLines);
        for (int i = 0; i < count; i++) {
            builder.append(lines[i]).append(System.lineSeparator());
        }
        if (lines.length > maxLines) {
            builder.append("... ").append(lines.length - maxLines).append(" more lines")
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }
}
