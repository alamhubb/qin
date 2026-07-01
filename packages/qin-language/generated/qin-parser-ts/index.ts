// Generated package entry by Qin. Source Java entry: com.qin.parser.QinParser
import { com_qin_parser_QinParser } from "./com/qin/parser/QinParser.ts";
export default com_qin_parser_QinParser;
export { com_qin_parser_QinParser, com_qin_parser_QinParser as QinParser } from "./com/qin/parser/QinParser.ts";
export { com_slime_parser_SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser as SlimeJavascriptParser } from "./com/slime/parser/SlimeJavascriptParser.ts";
export { com_slime_parser_consumer_SlimeTokenConsumer, com_slime_parser_consumer_SlimeTokenConsumer as SlimeTokenConsumer } from "./com/slime/parser/consumer/SlimeTokenConsumer.ts";
export { com_slime_parser_SlimeJavascriptParser$SourceType, com_slime_parser_SlimeJavascriptParser$SourceType as SlimeJavascriptParserSourceType } from "./com/slime/parser/SlimeJavascriptParser.ts";
export { com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams } from "./com/slime/parser/base/SlimeJavascriptParserBase.ts";
export { com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams } from "./com/slime/parser/base/SlimeJavascriptParserBase.ts";
export { com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams } from "./com/slime/parser/base/SlimeJavascriptParserBase.ts";
export { com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "./com/slime/parser/base/SlimeJavascriptParserBase.ts";
import { com_slime_token_JavaScriptTokens } from "./com/slime/token/JavaScriptTokens.ts";
export { com_slime_token_JavaScriptTokens as JavaScriptTokens };
const __qinSlimeTokens = com_slime_token_JavaScriptTokens.getTokens();
const __qinSlimeTokenEntries: any[] = [];
for (const token of __qinSlimeTokens as any) {
  const tokenName = token.getName();
  __qinSlimeTokenEntries.push([tokenName, token]);
  if (!tokenName.endsWith("Tok")) __qinSlimeTokenEntries.push([tokenName + "Tok", token]);
}
export const SlimeTokensObj = Object.fromEntries(__qinSlimeTokenEntries);
export const SlimeJavascriptTokensObj = SlimeTokensObj;
export const slimeTokens = Object.values(SlimeTokensObj);
const __qinSlimeTokenTypeEntries: any[] = [];
for (const token of __qinSlimeTokens) {
  const tokenName = token.getName();
  __qinSlimeTokenTypeEntries.push([tokenName, tokenName]);
}
export const SlimeJavascriptContextualKeywordTokenTypes = Object.freeze({
  Async: "async",
  Static: "static",
  Let: "let",
  Get: "get",
  Set: "set",
  Of: "of",
  From: "from",
  As: "as",
  Target: "target",
  Meta: "meta"
});
export const SlimeContextualKeywordTokenTypes = SlimeJavascriptContextualKeywordTokenTypes;
export const SlimeTokenType = Object.freeze(Object.assign(Object.fromEntries(__qinSlimeTokenTypeEntries), SlimeJavascriptContextualKeywordTokenTypes));
function __qinReadTokenMember(token: any, methodName: string, fieldName: string): any {
  if (token == null) return null;
  const method = token[methodName];
  if (typeof method === "function") return method.call(token);
  return token[fieldName];
}
export const ReservedWords = new Set(slimeTokens.filter((token: any) => __qinReadTokenMember(token, "isKeyword", "isKeyword")).map((token: any) => __qinReadTokenMember(token, "getValue", "value")).filter(Boolean));
