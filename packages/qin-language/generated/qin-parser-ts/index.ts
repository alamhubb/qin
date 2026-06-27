// Generated package entry by Qin. Source Java entry: com.qin.parser.QinParser
import { com_qin_parser_QinParser } from "./com/qin/parser/QinParser.ts";
export default com_qin_parser_QinParser;
export { com_qin_parser_QinParser, com_qin_parser_QinParser as QinParser } from "./com/qin/parser/QinParser.ts";
export { com_slime_parser_SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser as SlimeJavascriptParser } from "./com/slime/parser/SlimeJavascriptParser.ts";
export { com_slime_parser_consumer_SlimeTokenConsumer, com_slime_parser_consumer_SlimeTokenConsumer as SlimeTokenConsumer } from "./com/slime/parser/consumer/SlimeTokenConsumer.ts";
export { com_slime_parser_SlimeJavascriptParser$SourceType, com_slime_parser_SlimeJavascriptParser$SourceType as SlimeJavascriptParserSourceType } from "./com/slime/parser/SlimeJavascriptParser.ts";
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
function __qinReadTokenMember(token: any, methodName: string, fieldName: string): any {
  if (token == null) return null;
  const method = token[methodName];
  if (typeof method === "function") return method.call(token);
  return token[fieldName];
}
export const ReservedWords = new Set(slimeTokens.filter((token: any) => __qinReadTokenMember(token, "isKeyword", "isKeyword")).map((token: any) => __qinReadTokenMember(token, "getValue", "value")).filter(Boolean));
export type ExpressionParams = any;
export type StatementParams = any;
export type DeclarationParams = any;
export type TemplateLiteralParams = any;
