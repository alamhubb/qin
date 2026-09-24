package com.qin.runtime.core;

import java.nio.file.Path;

public final class QinCsstsParserRuleProbeMain {
    private QinCsstsParserRuleProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();

        String wrapper = """
                import CssTsParser from "cssts-compiler/src/parser/CssTsParser.js";
                import { ExpressionParams, DeclarationParams, StatementParams } from "@qin/generated-qin-parser-ts";

                const moduleSource = "const buttonBase = css { colorRed, fontBold }\\n";
                const exprParams = () => new ExpressionParams(true, false, false);
                const declParams = () => new DeclarationParams(false, true, false);
                const stmtParams = () => new StatementParams(false, true, false);

                function probeParser(name, parser, invoke) {
                  let thrown = null;
                  try {
                    invoke(parser);
                  } catch (error) {
                    thrown = String(error);
                  }
                  return {
                    name,
                    parserFail: parser.parserFail,
                    parseSuccess: parser._parseSuccess,
                    nextToken: parser.nextToken ? parser.nextToken.tokenName + ":" + parser.nextToken.tokenValue : null,
                    curCstName: parser.curCst ? parser.curCst.name : null,
                    thrown
                  };
                }

                ({
                  cssExpression: probeParser("CssExpression", new CssTsParser("css { colorRed, fontBold }"), parser => parser.CssExpression(exprParams())),
                  cssStyleObject: probeParser("CssStyleObject", new CssTsParser("{ colorRed, fontBold }"), parser => parser.CssStyleObject(exprParams())),
                  elementList: probeParser("ElementList", new CssTsParser("colorRed, fontBold"), parser => parser.ElementList(exprParams())),
                  assignmentExpression: probeParser("AssignmentExpression", new CssTsParser("fontBold"), parser => parser.AssignmentExpression(exprParams())),
                  primaryExpression: probeParser("PrimaryExpression", new CssTsParser("css { colorRed, fontBold }"), parser => parser.PrimaryExpression(exprParams())),
                  lexicalDeclaration: probeParser("LexicalDeclaration", new CssTsParser(moduleSource), parser => parser.LexicalDeclaration(exprParams())),
                  declaration: probeParser("Declaration", new CssTsParser(moduleSource), parser => parser.Declaration(declParams())),
                  statementListItem: probeParser("StatementListItem", new CssTsParser(moduleSource), parser => parser.StatementListItem(stmtParams())),
                  program: probeParser("Program", new CssTsParser(moduleSource), parser => parser.Program())
                });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "cssts_parser_rule_probe");
        System.out.println(QinObjectJsonEncoder.toJson(result));
    }
}
