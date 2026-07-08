package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinOvsCstToAstFunctionBodySmokeTestMain {
    private QinOvsCstToAstFunctionBodySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        String source = """
                const normalizeRootUrl = (value) => {
                  const text = String(value || "").trim()
                  if (!text) {
                    return ""
                  }
                  return text
                }

                div(onClick() { console.log(normalizeRootUrl("https://example.test")) }) {
                  "Open"
                }
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import OvsParser from "ovs-compiler/src/parser/OvsParser.ts";
                import { OvsCstToSlimeAstUtils } from "ovs-compiler/src/factory/OvsCstToSlimeAst/OvsCstToSlimeAst.ts";
                import { normalizeGeneratedAst } from "cssts-compiler/src/parser/generated-runtime-adapter.ts";

                const parser = new OvsParser(%s);
                const cst = parser.OvsProgram();
                const program = normalizeGeneratedAst(OvsCstToSlimeAstUtils.toFileAst(cst));

                function arrayOf(value) {
                  if (!value) return [];
                  if (Array.isArray(value)) return value;
                  if (Array.isArray(value.__items)) return value.__items;
                  if (typeof value.size === "function" && typeof value.get === "function") {
                    const out = [];
                    for (let i = 0; i < value.size(); i++) out.push(value.get(i));
                    return out;
                  }
                  if (typeof value.length === "number") {
                    const out = [];
                    for (let i = 0; i < value.length; i++) out.push(value[i]);
                    return out;
                  }
                  return [];
                }

                function nodeType(node) {
                  if (!node) return null;
                  const raw = typeof node.type === "function" ? node.type() : node.type;
                  if (typeof raw === "string") return raw;
                  const enumName = raw && typeof raw.name === "function" ? raw.name() : raw && raw.__qinEnumName;
                  return enumName == null ? null : String(enumName);
                }

                function bodyOf(node) {
                  if (!node) return null;
                  return typeof node.body === "function" ? node.body() : node.body;
                }

                function cstName(node) {
                  if (!node) return null;
                  return typeof node.getName === "function" ? node.getName() : node.name;
                }

                function cstChildren(node) {
                  if (!node) return [];
                  const children = typeof node.getChildren === "function" ? node.getChildren() : node.children;
                  return arrayOf(children);
                }

                function dumpInterestingCst(node, depth = 0, out = []) {
                  if (!node || depth > 14) return out;
                  const name = cstName(node);
                  const interesting = [
                    "ArrowFunction", "ConciseBody", "FunctionBody", "OvsFunctionBody",
                    "MethodDefinition", "StatementList", "StatementListItem",
                    "VariableStatement", "IfStatement", "ReturnStatement", "ExpressionStatement",
                    "OvsPropertyDefinition", "OvsArguments"
                  ];
                  if (interesting.indexOf(name) >= 0) {
                    out.push("  ".repeat(depth) + name + " -> [" + cstChildren(node).map(cstName).join(",") + "]");
                  }
                  for (const child of cstChildren(node)) dumpInterestingCst(child, depth + 1, out);
                  return out;
                }

                function findArrowWithParam(node, paramName, seen = []) {
                  if (!node || typeof node !== "object") return null;
                  if (seen.indexOf(node) >= 0) return null;
                  seen.push(node);
                  if (nodeType(node) === "ArrowFunctionExpression") {
                    const params = arrayOf(node.params);
                    if (params.some(param => param && param.name === paramName)) return node;
                  }
                  for (const key of Object.keys(node)) {
                    if (key === "loc" || key.startsWith("__")) continue;
                    const child = node[key];
                    if (typeof child === "function") continue;
                    if (Array.isArray(child)) {
                      for (const item of child) {
                        const found = findArrowWithParam(item, paramName, seen);
                        if (found) return found;
                      }
                    } else {
                      const found = findArrowWithParam(child, paramName, seen);
                      if (found) return found;
                    }
                  }
                  return null;
                }

                function findProperty(node, keyName, seen = []) {
                  if (!node || typeof node !== "object") return null;
                  if (seen.indexOf(node) >= 0) return null;
                  seen.push(node);
                  if (nodeType(node) === "Property") {
                    const key = node.key;
                    if (key && key.name === keyName) return node;
                  }
                  for (const key of Object.keys(node)) {
                    if (key === "loc" || key.startsWith("__")) continue;
                    const child = node[key];
                    if (typeof child === "function") continue;
                    if (Array.isArray(child)) {
                      for (const item of child) {
                        const found = findProperty(item, keyName, seen);
                        if (found) return found;
                      }
                    } else {
                      const found = findProperty(child, keyName, seen);
                      if (found) return found;
                    }
                  }
                  return null;
                }

                const arrow = findArrowWithParam(program, "value");
                const arrowBody = bodyOf(arrow);
                const methodProp = findProperty(program, "onClick");
                const methodValue = methodProp ? methodProp.value : null;
                const methodBody = bodyOf(methodValue);
                ({
                  afterEof: parser.isEof(),
                  cstDump: dumpInterestingCst(cst).join("\\n"),
                  arrowType: nodeType(arrow),
                  arrowKeys: arrow ? Object.keys(arrow).join(",") : null,
                  arrowBodyType: nodeType(arrowBody),
                  arrowBodyLength: arrayOf(arrowBody && arrowBody.body).length,
                  methodValueType: nodeType(methodValue),
                  methodValueKeys: methodValue ? Object.keys(methodValue).join(",") : null,
                  methodBodyType: nodeType(methodBody),
                  methodBodyLength: arrayOf(methodBody && methodBody.body).length
                });
                """.formatted(QinJsPackageRunner.renderJsLiteral(source)), "ovs_cst_to_ast_function_body_smoke");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        requireNumber(map, "arrowBodyLength", 3);
        requireNumber(map, "methodBodyLength", 1);
        System.out.println("QinOvsCstToAstFunctionBodySmokeTestMain OK");
    }

    private static void requireNumber(Map<?, ?> map, String key, int expected) {
        Object value = map.get(key);
        if (!(value instanceof Number number) || number.intValue() != expected) {
            throw new IllegalStateException("Expected " + key + "=" + expected + ", got: " + map);
        }
    }
}
