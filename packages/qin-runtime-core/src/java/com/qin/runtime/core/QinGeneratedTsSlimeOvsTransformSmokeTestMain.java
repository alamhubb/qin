package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsSlimeOvsTransformSmokeTestMain {
    private QinGeneratedTsSlimeOvsTransformSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { ovsTransformBase, ovsTransformFile, vitePluginOvsTransform, OvsParser } from "ovs-compiler";
                import { SlimeGenerator, __qin_smoke_generate, __qin_smoke_generate_java_style_ast, __qin_smoke_generate_normalized_over_qin_fields } from "slime-generator";

                const source = `
                div(class = "shell") {
                  div(id = "balance-panel") {
                    p(class = "loading-line") { "Loading balance monitor..." }
                  }
                }
                `;

                function valueOfProperty(target, name) {
                  const value = target ? target[name] : null;
                  return typeof value === "function" ? value.call(target) : value;
                }

                function childList(cst) {
                  if (!cst) return [];
                  const children = typeof cst.getChildren === "function" ? cst.getChildren() : cst.children;
                  return children || [];
                }

                function cstSummary(cst, depth = 0) {
                  if (!cst || depth > 3) return null;
                  const children = childList(cst);
                  const name = typeof cst.getName === "function" ? cst.getName() : cst.name;
                  const value = typeof cst.getValue === "function" ? cst.getValue() : cst.value;
                  return {
                    name,
                    value: value == null ? null : String(value),
                    childCount: children.length,
                    children: children.slice(0, 8).map(child => cstSummary(child, depth + 1))
                  };
                }

                function parserState(parser) {
                  const curToken = valueOfProperty(parser, "curToken");
                  return {
                    consumedTokens: valueOfProperty(parser, "currentTokenIndex"),
                    currentIndex: valueOfProperty(parser, "currentIndex"),
                    parsedTokens: parser.parsedTokens ? parser.parsedTokens.length : -1,
                    currentTokenName: curToken ? (typeof curToken.tokenName === "function" ? curToken.tokenName() : curToken.tokenName) : "EOF",
                    currentTokenValue: curToken ? (typeof curToken.value === "function" ? curToken.value() : curToken.tokenValue || curToken.value) : "",
                    programMarked: !!(parser.Program && parser.Program.__isSubhutiRule__),
                    rawProgram: !!parser.__qin_subhuti_raw_Program,
                    statementMarked: !!(parser.Statement && parser.Statement.__isSubhutiRule__),
                    rawStatement: !!parser.__qin_subhuti_raw_Statement,
                    ruleCacheSize: parser.__qin_field_ruleCache && typeof parser.__qin_field_ruleCache.size === "function" ? parser.__qin_field_ruleCache.size() : -1
                  };
                }

                function capture(name, fn) {
                  try {
                    return { name, ok: true, value: fn() };
                  } catch (error) {
                    const cause = error && error.cause ? error.cause : null;
                    return {
                      name,
                      ok: false,
                      error: error && error.message ? error.message : String(error),
                      cause: cause && cause.message ? cause.message : cause ? String(cause) : null,
                      stack: error && error.stack ? String(error.stack).slice(0, 600) : null
                    };
                  }
                }

                function stringIncludes(value, text) {
                  if (typeof value !== "string") return false;
                  return value.includes(text);
                }

                function localTransformBase(code) {
                  const localParser = new OvsParser(code);
                  const localCst = localParser.Program();
                  return {
                    cst: localCst,
                    tokens: localParser.parsedTokens,
                    parserState: parserState(localParser)
                  };
                }

                const parser = new OvsParser(source);
                const cstStage = capture("parser.Program", () => parser.Program());
                const cst = cstStage.ok ? cstStage.value : null;
                const parserAfterProgram = parserState(parser);
                const localStage = capture("localTransformBase", () => localTransformBase(source));
                const local = localStage.ok ? localStage.value : {};
                const baseStage = capture("ovsTransformBase", () => ovsTransformBase(source));
                const base = baseStage.ok ? baseStage.value : {};
                const fileStage = capture("ovsTransformFile", () => ovsTransformFile(source));
                const file = fileStage.ok ? fileStage.value : {};
                const generatorSmokeStage = capture("slimeGeneratorSmoke", () => ({
                  plain: __qin_smoke_generate(),
                  javaStyle: __qin_smoke_generate_java_style_ast(),
                  normalized: __qin_smoke_generate_normalized_over_qin_fields()
                }));
                const generatorStage = capture("slimeGenerator", () => SlimeGenerator.generator(file.ast, file.tokens));
                const generated = generatorStage.ok ? generatorStage.value : null;
                const pluginStage = capture("vitePluginOvsTransform", () => vitePluginOvsTransform(source, { globalStyles: new Set() }));
                const plugin = pluginStage.ok ? pluginStage.value : null;
                ({
                  stages: [cstStage, localStage, baseStage, fileStage, generatorSmokeStage, generatorStage, pluginStage].map(stage => ({
                    name: stage.name,
                    ok: stage.ok,
                    error: stage.error || null,
                    cause: stage.cause || null,
                    stack: stage.stack || null
                  })),
                  cst: cstSummary(cst),
                  parserAfterProgram,
                  localParserState: local.parserState || null,
                  localTokenCount: local.tokens ? local.tokens.length : -1,
                  tokenCount: base.tokens ? base.tokens.length : -1,
                  hasAst: !!base.ast,
                  baseAstType: base.ast ? base.ast.type : null,
                  baseBodyLength: base.ast && base.ast.body ? base.ast.body.length : -1,
                  fileHasAst: !!file.ast,
                  fileAstType: file.ast ? file.ast.type : null,
                  fileBodyLength: file.ast && file.ast.body ? file.ast.body.length : -1,
                  fileFirstType: file.ast && file.ast.body && file.ast.body[0] ? file.ast.body[0].type : null,
                  generatedCodeLength: generated && generated.code ? generated.code.length : 0,
                  generatedCodePreview: generated && generated.code ? generated.code.slice(0, 160) : "",
                  pluginKeys: plugin ? Object.keys(plugin).join(",") : "",
                  codeLength: plugin && plugin.code ? plugin.code.length : 0,
                  codePreview: plugin && plugin.code ? plugin.code.slice(0, 120) : "",
                  hasBalancePanel: stringIncludes(plugin ? plugin.code : null, "balance-panel"),
                  hasLoadingLine: stringIncludes(plugin ? plugin.code : null, "Loading balance monitor")
                });
                """, "generated_ts_slime_ovs_transform_probe");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        Object tokenCount = map.get("tokenCount");
        if (!(tokenCount instanceof Number count) || count.intValue() <= 0
                || !Boolean.TRUE.equals(map.get("hasAst"))
                || !(map.get("codeLength") instanceof Number codeLength)
                || codeLength.intValue() <= 0
                || !Boolean.TRUE.equals(map.get("hasBalancePanel"))
                || !Boolean.TRUE.equals(map.get("hasLoadingLine"))) {
            throw new IllegalStateException("Expected generated TS Slime OVS transform output, got: "
                    + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinGeneratedTsSlimeOvsTransformSmokeTestMain OK " + QinObjectJsonEncoder.toJson(map));
    }
}
