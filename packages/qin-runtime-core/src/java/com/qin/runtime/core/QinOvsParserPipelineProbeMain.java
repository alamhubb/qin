package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * OVS parser pipeline diagnostic probe.
 *
 * <p>This is intentionally a diagnostic entry point, not a fallback parser. It
 * runs the active OVS toolchain and reports each layer so parser bugs can be
 * bisected quickly: token -> CST -> AST -> emitted ESM.</p>
 */
public final class QinOvsParserPipelineProbeMain {
    private QinOvsParserPipelineProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        boolean assertFunctionBody = false;
        String source = defaultSource();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--assert-function-body".equals(arg)) {
                assertFunctionBody = true;
            } else if ("--file".equals(arg) && i + 1 < args.length) {
                source = Files.readString(Path.of(args[++i]), StandardCharsets.UTF_8);
            } else if ("--source".equals(arg) && i + 1 < args.length) {
                source = args[++i];
            } else if ("--help".equals(arg) || "-h".equals(arg)) {
                System.out.println("""
                        Usage:
                          java ... com.qin.runtime.core.QinOvsParserPipelineProbeMain [--file path] [--source text] [--assert-function-body]

                        Reports active OVS token, CST, AST, and emitted ESM diagnostics.
                        """);
                return;
            } else {
                throw new IllegalArgumentException("Unknown argument: " + arg);
            }
        }

        Map<?, ?> result = probe(source);
        if (assertFunctionBody) {
            requireNumber(result, "arrowBodyLength", 3);
            requireNumber(result, "methodBodyLength", 1);
            Object generated = result.get("generated");
            String code = generated instanceof String text ? text : "";
            if (!code.contains("const text = String")
                    || !code.contains("return text")
                    || !code.contains("console.log(normalizeRootUrl")) {
                throw new IllegalStateException("OVS pipeline emitted code lost function body statements:\n"
                        + QinObjectJsonEncoder.toJson(result, 12000));
            }
        }
        System.out.println(QinObjectJsonEncoder.toJson(result, 24000));
    }

    public static Map<?, ?> probe(String source) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import OvsParser from "ovs-compiler/src/parser/OvsParser.ts";
                import { OvsCstToSlimeAstUtils } from "ovs-compiler/src/factory/OvsCstToSlimeAst/OvsCstToSlimeAst.ts";
                import { normalizeGeneratedAst } from "cssts-compiler/src/parser/generated-runtime-adapter.ts";
                import { SlimeGenerator } from "slime-generator";

                const source = %s;
                const parser = new OvsParser(source);
                const cst = parser.OvsProgram();
                const program = normalizeGeneratedAst(OvsCstToSlimeAstUtils.toFileAst(cst));
                const generated = SlimeGenerator.generator(program, parser.parsedTokens).code;

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
                  if (enumName == null) return null;
                  return String(enumName).split("_").filter(Boolean).map(part => {
                    if (part === "TS") return "TS";
                    const lower = part.toLowerCase();
                    return lower.slice(0, 1).toUpperCase() + lower.slice(1);
                  }).join("");
                }

                function bodyOf(node) {
                  if (!node) return null;
                  return typeof node.body === "function" ? node.body() : node.body;
                }

                function cstName(node) {
                  if (!node) return null;
                  return typeof node.getName === "function" ? node.getName() : node.name;
                }

                function cstValue(node) {
                  if (!node) return "";
                  const value = typeof node.getValue === "function" ? node.getValue() : node.value;
                  return value == null ? "" : String(value);
                }

                function cstChildren(node) {
                  if (!node) return [];
                  const children = typeof node.getChildren === "function" ? node.getChildren() : node.children;
                  return arrayOf(children);
                }

                function indent(depth) {
                  return new Array(depth + 1).join("  ");
                }

                const interestingCst = [
                  "ModuleItem", "StatementListItem", "Statement", "Declaration",
                  "ArrowFunction", "ConciseBody", "FunctionBody", "OvsFunctionBody", "OvsAsyncFunctionBody",
                  "MethodDefinition", "OvsArguments", "OvsPropertyDefinition",
                  "StatementList", "VariableStatement", "IfStatement", "ReturnStatement", "ExpressionStatement",
                  "OvsRenderFunction", "OvsRenderStatement"
                ];

                function dumpInterestingCst(node, depth = 0, out = []) {
                  if (!node || depth > 18) return out;
                  const name = cstName(node);
                  if (interestingCst.indexOf(name) >= 0) {
                    const value = cstValue(node);
                    out.push(indent(depth) + name + (value ? "=" + value : "") + " -> [" + cstChildren(node).map(cstName).join(",") + "]");
                  }
                  for (const child of cstChildren(node)) dumpInterestingCst(child, depth + 1, out);
                  return out;
                }

                function tokenSummary(token) {
                  if (!token) return null;
                  const name = typeof token.tokenName === "function" ? token.tokenName() :
                    typeof token.getTokenName === "function" ? token.getTokenName() : token.tokenName;
                  const value = typeof token.value === "function" ? token.value() :
                    typeof token.getTokenValue === "function" ? token.getTokenValue() : token.value;
                  return { name, value };
                }

                function walk(node, visit, seen = []) {
                  if (!node || typeof node !== "object") return null;
                  if (seen.indexOf(node) >= 0) return null;
                  seen.push(node);
                  const visited = visit(node);
                  if (visited) return visited;
                  for (const key of Object.keys(node)) {
                    if (key === "loc" || key === "location" || key.startsWith("__")) continue;
                    const child = node[key];
                    if (typeof child === "function") continue;
                    if (Array.isArray(child)) {
                      for (const item of child) {
                        const found = walk(item, visit, seen);
                        if (found) return found;
                      }
                    } else {
                      const found = walk(child, visit, seen);
                      if (found) return found;
                    }
                  }
                  return null;
                }

                function findArrowWithParam(node, paramName) {
                  return walk(node, candidate => {
                    if (nodeType(candidate) !== "ArrowFunctionExpression") return null;
                    const params = arrayOf(candidate.params);
                    return params.some(param => param && param.name === paramName) ? candidate : null;
                  });
                }

                function findProperty(node, keyName) {
                  return walk(node, candidate => {
                    if (nodeType(candidate) !== "Property") return null;
                    const key = candidate.key;
                    return key && key.name === keyName ? candidate : null;
                  });
                }

                function summarizeAstNode(node) {
                  if (!node) return null;
                  const body = bodyOf(node);
                  return {
                    type: nodeType(node),
                    keys: Object.keys(node).filter(key => !key.startsWith("__")).join(","),
                    bodyType: nodeType(body),
                    bodyLength: arrayOf(body && body.body).length
                  };
                }

                function summarizeStatement(node) {
                  if (!node) return null;
                  const summary = {
                    type: nodeType(node),
                    keys: Object.keys(node).filter(key => !key.startsWith("__")).join(",")
                  };
                  if (node.declarations) {
                    summary.declarations = arrayOf(node.declarations).map(decl => ({
                      type: nodeType(decl),
                      id: decl && decl.id && decl.id.name,
                      initType: nodeType(decl && decl.init)
                    }));
                  }
                  if (node.argument) summary.argumentType = nodeType(node.argument);
                  if (node.test) summary.testType = nodeType(node.test);
                  if (node.consequent) summary.consequentType = nodeType(node.consequent);
                  if (node.alternate) summary.alternateType = nodeType(node.alternate);
                  if (node.block) summary.blockType = nodeType(node.block);
                  if (node.handler) summary.handlerType = nodeType(node.handler);
                  if (node.finalizer) summary.finalizerType = nodeType(node.finalizer);
                  const nestedBody = arrayOf(node.body);
                  if (nestedBody.length > 0) {
                    summary.bodyStatementTypes = nestedBody.map(item => nodeType(item));
                  } else if (node.body && node.body.body) {
                    summary.bodyStatementTypes = arrayOf(node.body.body).map(item => nodeType(item));
                  }
                  return summary;
                }

                const arrow = findArrowWithParam(program, "value");
                const arrowBody = bodyOf(arrow);
                const methodProp = findProperty(program, "onClick");
                const methodValue = methodProp ? methodProp.value : null;
                const methodBody = bodyOf(methodValue);
                const tokens = arrayOf(parser.parsedTokens).slice(0, 120).map(tokenSummary);
                ({
                  sourceLength: source.length,
                  afterEof: parser.isEof(),
                  currentTokenIndex: parser.currentTokenIndex(),
                  nextToken: tokenSummary(parser.LA(1)),
                  tokenCount: arrayOf(parser.parsedTokens).length,
                  tokens,
                  cstDump: dumpInterestingCst(cst).join("\\n"),
                  astProgramType: nodeType(program),
                  astBodyLength: arrayOf(program && program.body).length,
                  arrow: summarizeAstNode(arrow),
                  arrowBodyType: nodeType(arrowBody),
                  arrowBodyLength: arrayOf(arrowBody && arrowBody.body).length,
                  arrowBodyStatements: arrayOf(arrowBody && arrowBody.body).map(summarizeStatement),
                  methodValue: summarizeAstNode(methodValue),
                  methodBodyType: nodeType(methodBody),
                  methodBodyLength: arrayOf(methodBody && methodBody.body).length,
                  methodBodyStatements: arrayOf(methodBody && methodBody.body).map(summarizeStatement),
                  generated
                });
                """.formatted(QinJsPackageRunner.renderJsLiteral(source)), "ovs_parser_pipeline_probe");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        return map;
    }

    private static String defaultSource() {
        return """
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
    }

    private static void requireNumber(Map<?, ?> map, String key, int expected) {
        Object value = map.get(key);
        if (!(value instanceof Number number) || number.intValue() != expected) {
            throw new IllegalStateException("Expected " + key + "=" + expected + ", got: "
                    + QinObjectJsonEncoder.toJson(map, 12000));
        }
    }
}
