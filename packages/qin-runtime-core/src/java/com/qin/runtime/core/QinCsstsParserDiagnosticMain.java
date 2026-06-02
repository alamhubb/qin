package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinCsstsParserDiagnosticMain {
    private QinCsstsParserDiagnosticMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-cssts-parser-diagnostic-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-cssts-parser-diagnostic\" }\n", StandardCharsets.UTF_8);

        String wrapper = """
                import CssTsParser from "cssts-compiler/src/parser/CssTsParser.js";
                import { CssTsCstToAstUtils } from "cssts-compiler/src/factory/CssTsCstToAstUtils.js";
                import { SlimeCstToAstUtils } from "slime-parser";
                import { SlimeTokenType } from "slime-token";
                import { SlimeParser } from "slime-parser";

                const source = "const buttonBase = css { colorRed, fontBold }\\n";
                const parser = new CssTsParser(source);
                const slimeStatementListItemName = SlimeParser.prototype.StatementListItem ? SlimeParser.prototype.StatementListItem.name : null;
                const slimeStatementName = SlimeParser.prototype.Statement ? SlimeParser.prototype.Statement.name : null;
                const slimeDeclarationName = SlimeParser.prototype.Declaration ? SlimeParser.prototype.Declaration.name : null;
                const cssExpressionName = CssTsParser.prototype.CssExpression ? CssTsParser.prototype.CssExpression.name : null;
                const programType = typeof parser.Program;
                const programName = parser.Program ? parser.Program.name : null;
                const programIsRule = parser.Program ? parser.Program.__isSubhutiRule__ : null;
                const parserConstructorName = parser.constructor ? parser.constructor.name : null;
                const parserClassName = parser.className;
                const hasOwnProgram = parser.hasOwnProperty("Program");
                const hasOwnCssExpression = parser.hasOwnProperty("CssExpression");
                const identifierParser = new CssTsParser("abc");
                const identifierCst = identifierParser.IdentifierName();
                const identifierProbeName = identifierCst ? identifierCst.name : null;
                const tokenParser = new CssTsParser("abc");
                const slimeIdentifierName = SlimeTokenType.IdentifierName;
                const slimeTokenTypeType = typeof SlimeTokenType;
                const slimeTokenTypeKeys = Object.keys(SlimeTokenType).slice(0, 5).join(",");
                const tokenConsumerIdentifierType = typeof tokenParser.tokenConsumer.IdentifierName;
                const tokenParserNextTokenBefore = tokenParser.nextToken ? tokenParser.nextToken.tokenName + ":" + tokenParser.nextToken.tokenValue : null;
                const tokenParserCurrentIndexBefore = tokenParser.currentTokenIndex;
                const tokenParserParsedLengthBefore = tokenParser.parsedTokens ? tokenParser.parsedTokens.length : null;
                const tokenCst = tokenParser.tokenConsumer.IdentifierName();
                const tokenProbeName = tokenCst ? tokenCst.name : null;
                const tokenParserNextTokenAfter = tokenParser.nextToken ? tokenParser.nextToken.tokenName + ":" + tokenParser.nextToken.tokenValue : null;
                const tokenParserCurrentIndexAfter = tokenParser.currentTokenIndex;
                const tokenParserParsedLengthAfter = tokenParser.parsedTokens ? tokenParser.parsedTokens.length : null;
                const tokenParserFailAfter = tokenParser.parserFail;
                const directConsumeParser = new CssTsParser("abc");
                const directEntry = directConsumeParser._getOrParseToken(directConsumeParser.getNextTokenInfo(), undefined);
                const directEntryTokenName = directEntry && directEntry.token ? directEntry.token.tokenName : null;
                const directEntryTokenValue = directEntry && directEntry.token ? directEntry.token.tokenValue : null;
                const directEntryHasToken = !!(directEntry && directEntry.token);
                const directIsEofBefore = directConsumeParser.isEof;
                const directParserFailBefore = directConsumeParser.parserFail;
                const directParseSuccessFieldBefore = directConsumeParser._parseSuccess;
                const directSourceCodeBefore = directConsumeParser._sourceCode;
                const directCacheTypeBefore = directConsumeParser._cache ? directConsumeParser._cache.constructor.name : null;
                const directTokenCacheTypeBefore = directConsumeParser._tokenCache ? directConsumeParser._tokenCache.constructor.name : null;
                const directConsumeCst = directConsumeParser._consumeToken("IdentifierName");
                const directConsumeName = directConsumeCst ? directConsumeCst.name : null;
                const directConsumeFailAfter = directConsumeParser.parserFail;
                const directConsumeIndexAfter = directConsumeParser.currentTokenIndex;
                const cst = parser.Program();
                const curCst = parser.curCst;
                function findCstByName(node, name) {
                  if (!node) return null;
                  if (node.name === name) return node;
                  const children = node.children || [];
                  for (const child of children) {
                    const found = findCstByName(child, name);
                    if (found) return found;
                  }
                  return null;
                }
                function findParentOfName(node, name) {
                  if (!node || !node.children) return null;
                  for (const child of node.children) {
                    if (child && child.name === name) return node;
                    const found = findParentOfName(child, name);
                    if (found) return found;
                  }
                  return null;
                }
                const cssExpressionCst = findCstByName(cst, "CssExpression");
                const cssPrimaryParent = findParentOfName(cst, "CssExpression");
                let directPrimaryType = null;
                let directPrimaryMethodName = null;
                let directPrimaryMethodType = typeof CssTsCstToAstUtils.createPrimaryExpressionAst;
                if (CssTsCstToAstUtils.createPrimaryExpressionAst) {
                  directPrimaryMethodName = CssTsCstToAstUtils.createPrimaryExpressionAst.name;
                }
                const directPrimary = cssPrimaryParent ? CssTsCstToAstUtils.createPrimaryExpressionAst(cssPrimaryParent) : null;
                directPrimaryType = directPrimary ? directPrimary.type : null;
                const directSlimePrimary = cssPrimaryParent ? SlimeCstToAstUtils.createPrimaryExpressionAst(cssPrimaryParent) : null;
                const ast = CssTsCstToAstUtils.toFileAst(cst);
                let cstChildren = "";
                if (cst && cst.children) {
                  cstChildren = cst.children.map(child => child.name + ":" + child.value).join("|");
                }
                let astBodyLength = null;
                if (ast && ast.body) {
                  astBodyLength = ast.body.length;
                }
                let parserFailed = null;
                if (parser.parserFail !== undefined) {
                  parserFailed = parser.parserFail;
                }
                let currentIndex = null;
                if (parser.currentTokenIndex !== undefined) {
                  currentIndex = parser.currentTokenIndex;
                }
                let tokenCount = null;
                if (parser.parsedTokens) {
                  tokenCount = parser.parsedTokens.length;
                }
                let cstName = null;
                if (cst) {
                  cstName = cst.name;
                }
                ({
                  programType,
                  slimeStatementListItemName,
                  slimeStatementName,
                  slimeDeclarationName,
                  cssExpressionName,
                  programName,
                  programIsRule,
                  parserConstructorName,
                  parserClassName,
                  hasOwnProgram,
                  hasOwnCssExpression,
                  identifierProbeName,
                  tokenProbeName,
                  slimeIdentifierName,
                  slimeTokenTypeType,
                  slimeTokenTypeKeys,
                  tokenConsumerIdentifierType,
                  tokenParserNextTokenBefore,
                  tokenParserCurrentIndexBefore,
                  tokenParserParsedLengthBefore,
                  tokenParserNextTokenAfter,
                  tokenParserCurrentIndexAfter,
                  tokenParserParsedLengthAfter,
                  tokenParserFailAfter,
                  directEntryHasToken,
                  directEntryTokenName,
                  directEntryTokenValue,
                  directIsEofBefore,
                  directParserFailBefore,
                  directParseSuccessFieldBefore,
                  directSourceCodeBefore,
                  directCacheTypeBefore,
                  directTokenCacheTypeBefore,
                  directConsumeName,
                  directConsumeFailAfter,
                  directConsumeIndexAfter,
                  parserFailed,
                  currentIndex,
                  tokenCount,
                  cstName,
                  curCstName: curCst ? curCst.name : null,
                  cstChildren,
                  cssExpressionCstName: cssExpressionCst ? cssExpressionCst.name : null,
                  cssPrimaryParentName: cssPrimaryParent ? cssPrimaryParent.name : null,
                  directPrimaryMethodType,
                  directPrimaryMethodName,
                  directPrimaryType,
                  directSlimePrimaryType: directSlimePrimary ? directSlimePrimary.type : null,
                  astBodyLength,
                  hasCsstsSyntax: CssTsCstToAstUtils.hasCsstsSyntax,
                  usedAtomsSize: CssTsCstToAstUtils.getUsedAtoms().size,
                  usedAtoms: Array.from(CssTsCstToAstUtils.getUsedAtoms()).join(",")
                });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "cssts_parser_diagnostic");
        System.out.println(QinObjectJsonEncoder.toJson(result));
    }
}

