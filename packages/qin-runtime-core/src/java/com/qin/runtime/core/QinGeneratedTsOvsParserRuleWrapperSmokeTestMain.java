package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsOvsParserRuleWrapperSmokeTestMain {
    private QinGeneratedTsOvsParserRuleWrapperSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { OvsParser } from "ovs-compiler";

                const source = `div(class = "shell") {
                  p(class = "loading-line") { "Loading balance monitor..." }
                }`;

                function cstName(cst) {
                  if (!cst) return "";
                  if (typeof cst.getName === "function") return String(cst.getName());
                  if (typeof cst.name === "function") return String(cst.name());
                  return String(cst.name || cst.__qin_field_name || "");
                }

                function toArray(value) {
                  if (!value) return [];
                  if (Array.isArray(value)) return value;
                  if (value.__items && Array.isArray(value.__items)) return value.__items;
                  if (typeof value.size === "function" && typeof value.get === "function") {
                    const items = [];
                    for (let i = 0; i < value.size(); i++) items.push(value.get(i));
                    return items;
                  }
                  if (typeof value.length === "number") {
                    const items = [];
                    for (let i = 0; i < value.length; i++) items.push(value[i]);
                    return items;
                  }
                  return [];
                }

                function cstChildren(cst) {
                  if (!cst) return [];
                  const children = typeof cst.getChildren === "function"
                    ? cst.getChildren()
                    : (cst.children || cst.__qin_field_children);
                  return toArray(children);
                }

                function hasNode(cst, name) {
                  if (cstName(cst) === name) return true;
                  return cstChildren(cst).some(child => hasNode(child, name));
                }

                const parser = new OvsParser(source);
                const cst = parser.Program();
                ({
                  statementMarked: !!(parser.Statement && parser.Statement.__isSubhutiRule__),
                  ovsRenderMarked: !!(parser.OvsRenderStatement && parser.OvsRenderStatement.__isSubhutiRule__),
                  programName: cstName(cst),
                  hasStatement: hasNode(cst, "Statement"),
                  hasOvsRenderStatement: hasNode(cst, "OvsRenderStatement"),
                  parsedTokens: parser.parsedTokens ? parser.parsedTokens.length : -1
                });
                """, "generated_ts_ovs_parser_rule_wrapper");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        require(Boolean.TRUE.equals(map.get("statementMarked")), "Statement rule was not marked: " + map);
        require(Boolean.TRUE.equals(map.get("ovsRenderMarked")), "OvsRenderStatement rule was not marked: " + map);
        require("Program".equals(map.get("programName")), "Expected Program CST root: " + map);
        require(Boolean.TRUE.equals(map.get("hasStatement")), "Program CST is missing Statement wrapper: " + map);
        require(Boolean.TRUE.equals(map.get("hasOvsRenderStatement")),
                "Program CST is missing OvsRenderStatement wrapper: " + map);
        System.out.println("QinGeneratedTsOvsParserRuleWrapperSmokeTestMain OK "
                + QinObjectJsonEncoder.toJson(map));
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
