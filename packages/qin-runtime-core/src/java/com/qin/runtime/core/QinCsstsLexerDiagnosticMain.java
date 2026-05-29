package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinCsstsLexerDiagnosticMain {
    private QinCsstsLexerDiagnosticMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-cssts-lexer-diagnostic-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-cssts-lexer-diagnostic\" }\n", StandardCharsets.UTF_8);

        String wrapper = """
                import CssTsParser from "cssts-compiler/src/parser/CssTsParser.js";
                import { cssTsTokens } from "cssts-compiler/src/parser/CssTsTokenConsumer.js";
                import { SlimeTokensObj } from "slime-parser";
                import { SlimeJavascriptTokenType } from "slime-token";

                const slimeTokenKeys = SlimeTokensObj ? Object.keys(SlimeTokensObj) : null;
                const slimeTokenValues = SlimeTokensObj ? Object.values(SlimeTokensObj) : null;
                const hashbangToken = SlimeTokensObj ? SlimeTokensObj.HashbangComment : null;
                const firstSlimeTokenValue = slimeTokenValues ? slimeTokenValues[0] : null;
                const inspectToken = (token) => token ? ({
                  typeOf: typeof token,
                  keys: Object.keys(token),
                  name: token.name,
                  type: token.type,
                  value: token.value,
                  defaultName: token.default ? token.default.name : null,
                  defaultKeys: token.default ? Object.keys(token.default) : null,
                  constructorName: token.constructor ? token.constructor.name : null
                }) : null;

                let identifierToken = null;
                const identifierLikeTokens = [];
                for (const token of cssTsTokens) {
                  if (token && token.name && String(token.name).includes("Identifier")) {
                    identifierLikeTokens.push({
                      name: token.name,
                      source: token.pattern ? token.pattern.source : null,
                      flags: token.pattern ? token.pattern.flags : null
                    });
                  }
                  if (token && String(token.name) === "IdentifierName") {
                    identifierToken = token;
                    break;
                  }
                }
                const firstTokens = [];
                for (let i = 0; i < cssTsTokens.length && i < 8; i++) {
                  const token = cssTsTokens[i];
                  firstTokens.push(token ? token.name : null);
                }
                const match = identifierToken && identifierToken.pattern ? "css".match(identifierToken.pattern) : null;
                const anchored = identifierToken && identifierToken.pattern
                  ? new RegExp("^(?:" + identifierToken.pattern.source + ")", identifierToken.pattern.flags)
                  : null;
                const anchoredMatch = anchored ? "css { colorRed }".match(anchored) : null;
                ({
                  tokenCount: cssTsTokens.length,
                  firstTokens,
                  slimeTokensType: typeof SlimeTokensObj,
                  slimeTokenKeysLength: slimeTokenKeys ? slimeTokenKeys.length : null,
                  slimeTokenKey0: slimeTokenKeys ? slimeTokenKeys[0] : null,
                  slimeTokenValuesLength: slimeTokenValues ? slimeTokenValues.length : null,
                  slimeTokenValue0Name: slimeTokenValues && slimeTokenValues[0] ? slimeTokenValues[0].name : null,
                  tokenTypeKeysLength: SlimeJavascriptTokenType ? Object.keys(SlimeJavascriptTokenType).length : null,
                  tokenTypeHashbang: SlimeJavascriptTokenType ? SlimeJavascriptTokenType.HashbangComment : null,
                  tokenTypeIdentifierName: SlimeJavascriptTokenType ? SlimeJavascriptTokenType.IdentifierName : null,
                  hashbangToken: inspectToken(hashbangToken),
                  firstSlimeTokenValue: inspectToken(firstSlimeTokenValue),
                  identifierLikeTokens,
                  identifierTokenName: identifierToken ? identifierToken.name : null,
                  identifierPatternSource: identifierToken && identifierToken.pattern ? identifierToken.pattern.source : null,
                  identifierPatternFlags: identifierToken && identifierToken.pattern ? identifierToken.pattern.flags : null,
                  match0: match ? match[0] : null,
                  anchoredSource: anchored ? anchored.source : null,
                  anchoredFlags: anchored ? anchored.flags : null,
                  anchoredMatch0: anchoredMatch ? anchoredMatch[0] : null
                });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "cssts_lexer_diagnostic");
        System.out.println(QinObjectJsonEncoder.toJson(result));
    }
}
