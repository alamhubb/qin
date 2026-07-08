package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsOvsObjectLiteralProbeMain {
    private QinGeneratedTsOvsObjectLiteralProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        String source = """
                export const SummaryGrid = () => {
                  return section(class = "summary-grid") {
                    StatCard({ label: "Root URLs", value: String(rows.value.length), description: "Deduplicated endpoints" })
                  }
                }
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { OvsParser, ovsTransformFile } from "ovs-compiler";
                import { normalizeGeneratedAst } from "cssts-compiler";
                import { SlimeCstToAstUtils } from "@qin/generated-qin-parser-ts/SlimeCstToAstBridge";

                const source = %s;

                function readName(cst) {
                  return !cst ? null : typeof cst.getName === "function" ? cst.getName() : cst.name;
                }

                function readValue(cst) {
                  const value = !cst ? null : typeof cst.getValue === "function" ? cst.getValue() : cst.value;
                  return value == null ? null : String(value);
                }

                function toArray(value) {
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

                function children(cst) {
                  if (!cst) return [];
                  return toArray(typeof cst.getChildren === "function" ? cst.getChildren() : cst.children);
                }

                function summarize(cst, depth = 0) {
                  if (!cst || depth > 4) return null;
                  return {
                    name: readName(cst),
                    value: readValue(cst),
                    childCount: children(cst).length,
                    children: children(cst).slice(0, 8).map(child => summarize(child, depth + 1))
                  };
                }

                function collect(cst, name, out = []) {
                  if (!cst) return out;
                  if (readName(cst) === name) out.push(cst);
                  for (const child of children(cst)) collect(child, name, out);
                  return out;
                }

                function astPropertiesLength(ast) {
                  const props = ast && (typeof ast.properties === "function" ? ast.properties() : ast.properties);
                  return toArray(props).length;
                }

                function astType(ast) {
                  if (!ast) return null;
                  const raw = typeof ast.type === "function" ? ast.type() : ast.type;
                  if (typeof raw === "string") return raw;
                  const enumName = raw && typeof raw.name === "function" ? raw.name() : raw && raw.__qinEnumName;
                  return enumName == null ? null : String(enumName);
                }

                const parser = new OvsParser(source);
                const cst = parser.Program();
                const objectLiterals = collect(cst, "ObjectLiteral");
                const ovsArguments = collect(cst, "OvsArguments");
                const argumentsNodes = collect(cst, "Arguments");
                const propertyLists = collect(cst, "PropertyDefinitionList");
                const propertyDefinitions = collect(cst, "PropertyDefinition");
                const objectAsts = objectLiterals.map(node => {
                  try {
                    const ast = SlimeCstToAstUtils.createObjectLiteralAst(node);
                    const normalized = normalizeGeneratedAst(ast);
                    return {
                      ok: true,
                      propertyCount: astPropertiesLength(ast),
                      normalizedPropertyCount: Array.isArray(normalized.properties) ? normalized.properties.length : -1,
                      astType: astType(ast)
                    };
                  } catch (error) {
                    return { ok: false, error: error && error.message ? error.message : String(error) };
                  }
                });
                const file = ovsTransformFile(source);
                const code = file && file.code ? file.code : "";
                ({
                  objectLiteralCount: objectLiterals.length,
                  ovsArgumentsCount: ovsArguments.length,
                  argumentsCount: argumentsNodes.length,
                  firstOvsArguments: summarize(ovsArguments[0]),
                  firstArguments: summarize(argumentsNodes[0]),
                  propertyListCount: propertyLists.length,
                  propertyDefinitionCount: propertyDefinitions.length,
                  firstObjectLiteral: summarize(objectLiterals[0]),
                  firstPropertyDefinition: summarize(propertyDefinitions[0]),
                  objectAsts,
                  hasSummaryGrid: code.includes("SummaryGrid"),
                  hasRootUrls: code.includes("Root URLs"),
                  hasSummaryClass: code.includes("summary-grid"),
                  codePreview: code.slice(0, 240)
                });
                """.formatted(QinJsPackageRunner.renderJsLiteral(source)), "generated_ts_ovs_object_literal_probe");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        System.out.println("QinGeneratedTsOvsObjectLiteralProbeMain " + QinObjectJsonEncoder.toJson(map));
    }
}
