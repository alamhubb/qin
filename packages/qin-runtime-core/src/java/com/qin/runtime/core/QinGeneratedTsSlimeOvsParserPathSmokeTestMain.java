package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsSlimeOvsParserPathSmokeTestMain {
    private QinGeneratedTsSlimeOvsParserPathSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import SlimeParser from "slime-parser/src/language/es2025/SlimeParser.ts";
                import OvsParser from "ovs-compiler/src/parser/OvsParser.ts";
                import {
                  com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as GeneratedDeclarationParams,
                  com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as GeneratedStatementParams
                } from "slime-parser/com/slime/parser/base/SlimeJavascriptParserBase.ts";

                const letSource = "let count = ref(0)";
                const moduleSource = "import { ref } from 'vue'\\nlet count = ref(0)";

                class ProbeOvsParser extends OvsParser {
                  constructor(source) {
                    super(source);
                    this.hitDeclaration = false;
                    this.hitLexicalDeclaration = false;
                    this.hitStatement = false;
                  }

                  Declaration(params = {}) {
                    this.hitDeclaration = true;
                    return super.Declaration(params);
                  }

                  LexicalDeclaration(params = {}) {
                    this.hitLexicalDeclaration = true;
                    return super.LexicalDeclaration(params);
                  }

                  Statement(params = {}) {
                    this.hitStatement = true;
                    return super.Statement(params);
                  }
                }

                function nextName(parser) {
                  const token = parser.LA(1);
                  return token == null ? "EOF" : token.getTokenName();
                }

                function errorText(error) {
                  const parts = [];
                  let current = error;
                  for (let i = 0; i < 14 && current != null; i++) {
                    parts.push(String(current && current.message ? current.message : current));
                    current = current && current.getCause ? current.getCause() : null;
                  }
                  return parts.join(" <- ");
                }

                function probe(ParserClass, source, run) {
                  const parser = new ParserClass(source);
                  let error = null;
                  try {
                    run(parser);
                  } catch (e) {
                    error = errorText(e);
                  }
                  return {
                    next: nextName(parser),
                    error,
                    parsed: parser.parsedTokens ? parser.parsedTokens.length : -1,
                    hitDeclaration: !!parser.hitDeclaration,
                    hitLexicalDeclaration: !!parser.hitLexicalDeclaration,
                    hitStatement: !!parser.hitStatement
                  };
                }

                ({
                  baseProgramLet: probe(SlimeParser, letSource, parser => parser.Program()),
                  baseProgramModule: probe(SlimeParser, moduleSource, parser => parser.Program()),
                  ovsDeclarationLet: probe(ProbeOvsParser, letSource, parser => parser.Declaration({})),
                  ovsDeclarationGeneratedParamsLet: probe(ProbeOvsParser, letSource, parser => parser.Declaration(new GeneratedDeclarationParams(false, true, false))),
                  ovsDeclarationJavaLikeParamsLet: probe(ProbeOvsParser, letSource, parser => parser.Declaration({
                    yield: () => false,
                    await: () => true,
                    isDefault: () => false
                  })),
                  ovsLexicalDeclarationLet: probe(ProbeOvsParser, letSource, parser => parser.LexicalDeclaration({ In: true })),
                  ovsLexicalDeclarationAwaitLet: probe(ProbeOvsParser, letSource, parser => parser.LexicalDeclaration({ In: true, Await: true })),
                  ovsLexicalDeclarationJavaLikeLet: probe(ProbeOvsParser, letSource, parser => parser.LexicalDeclaration({
                    In: true,
                    Await: true,
                    yield: () => false,
                    await: () => true
                  })),
                  ovsStatementListItemLet: probe(ProbeOvsParser, letSource, parser => parser.StatementListItem({ Yield: false, Await: true, Return: false })),
                  ovsStatementListItemGeneratedParamsLet: probe(ProbeOvsParser, letSource, parser => parser.StatementListItem(new GeneratedStatementParams(false, true, false))),
                  ovsModuleItemLet: probe(ProbeOvsParser, letSource, parser => parser.ModuleItem()),
                  ovsProgramLet: probe(ProbeOvsParser, letSource, parser => parser.Program()),
                  ovsProgramModule: probe(ProbeOvsParser, moduleSource, parser => parser.Program())
                });
                """, "generated_ts_slime_ovs_parser_path");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        System.out.println("QinGeneratedTsSlimeOvsParserPathSmokeTestMain "
                + QinObjectJsonEncoder.toJson(map));
        requireEof(map, "baseProgramLet");
        requireEof(map, "baseProgramModule");
        requireEof(map, "ovsDeclarationLet");
        requireEof(map, "ovsDeclarationGeneratedParamsLet");
        requireEof(map, "ovsDeclarationJavaLikeParamsLet");
        requireEof(map, "ovsLexicalDeclarationLet");
        requireEof(map, "ovsLexicalDeclarationAwaitLet");
        requireEof(map, "ovsLexicalDeclarationJavaLikeLet");
        requireEof(map, "ovsStatementListItemLet");
        requireEof(map, "ovsStatementListItemGeneratedParamsLet");
        requireEof(map, "ovsModuleItemLet");
        requireEof(map, "ovsProgramLet");
        requireEof(map, "ovsProgramModule");
    }

    private static void requireEof(Map<?, ?> root, String name) {
        Object value = root.get(name);
        if (!(value instanceof Map<?, ?> probe)) {
            throw new IllegalStateException("Expected probe object for " + name + ", got: " + value);
        }
        if (!"EOF".equals(probe.get("next")) || probe.get("error") != null) {
            throw new IllegalStateException("Expected EOF without error for " + name + ": "
                    + QinObjectJsonEncoder.toJson(probe));
        }
    }
}
