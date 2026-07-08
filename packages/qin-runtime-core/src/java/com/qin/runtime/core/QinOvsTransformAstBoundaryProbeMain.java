package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsTransformAstBoundaryProbeMain {
    private QinOvsTransformAstBoundaryProbeMain() {
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

                div {
                  "Open"
                }
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { ovsTransformFile } from "ovs-compiler";
                import { SlimeGenerator } from "slime-generator";

                const source = %s;
                const transformed = ovsTransformFile(source);
                const ast = transformed.ast;

                function nodeType(node) {
                  if (!node) return null;
                  if (typeof node.type === "string") return node.type;
                  if (typeof node.type === "function") return node.type();
                  return null;
                }

                function arrayOf(value) {
                  if (!value) return [];
                  if (Array.isArray(value)) return value;
                  if (typeof value.toArray === "function") return value.toArray();
                  return [];
                }

                function bodyOf(node) {
                  if (!node) return null;
                  if (typeof node.body === "function") return node.body();
                  return node.body;
                }

                function findIdentifier(node, name, seen = []) {
                  if (!node || typeof node !== "object") return null;
                  if (seen.indexOf(node) >= 0) return null;
                  seen.push(node);
                  if (nodeType(node) === "Identifier" && node.name === name) return node;
                  for (const key of Object.keys(node)) {
                    if (key === "loc" || key.startsWith("__")) continue;
                    const child = node[key];
                    if (typeof child === "function") continue;
                    if (Array.isArray(child)) {
                      for (const item of child) {
                        const found = findIdentifier(item, name, seen);
                        if (found) return found;
                      }
                    } else {
                      const found = findIdentifier(child, name, seen);
                      if (found) return found;
                    }
                  }
                  return null;
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

                const arrow = findArrowWithParam(ast, "value");
                const arrowBody = bodyOf(arrow);
                const methodProp = findProperty(ast, "onClick");
                const methodValue = methodProp ? methodProp.value : null;
                const methodBody = bodyOf(methodValue);
                const generated = SlimeGenerator.generator(ast, transformed.tokens).code;

                ({
                  arrowType: nodeType(arrow),
                  arrowBodyType: nodeType(arrowBody),
                  arrowBodyLength: arrayOf(arrowBody && arrowBody.body).length,
                  methodValueType: nodeType(methodValue),
                  methodBodyType: nodeType(methodBody),
                  methodBodyLength: arrayOf(methodBody && methodBody.body).length,
                  hasConsoleIdentifierInAst: findIdentifier(ast, "console") != null,
                  generated
                });
                """.formatted(QinJsPackageRunner.renderJsLiteral(source)), "ovs_transform_ast_boundary_probe");

        System.out.println("QinOvsTransformAstBoundaryProbeMain " + QinObjectJsonEncoder.toJson(result));
    }
}
