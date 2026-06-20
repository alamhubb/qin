// Generated package entry by Qin. Source Java entry: com.slime.parser.SlimeParser
import { com_slime_parser_SlimeParser as __QinGeneratedSlimeParserBase } from "./com/slime/parser/SlimeParser.ts";
function __qinToJsArray(value) {
  if (value == null) return [];
  if (Array.isArray(value)) return value;
  if (typeof value.toArray === "function") return value.toArray();
  if (typeof value[Symbol.iterator] === "function") return Array.from(value);
  return [];
}
class SlimeParser extends __QinGeneratedSlimeParserBase {
  constructor(...args) {
    super(...args);
    this.parsedTokens = [];
    this.unparsedTokens = [];
    this.tokenConsumer = super.getTokenConsumer();
    this.__qinSyncJsParserProperties();
  }
  __qinSyncJsParserProperties() {
    this.parsedTokens = __qinToJsArray(super.parsedTokens());
    this.unparsedTokens = __qinToJsArray(super.unparsedTokens());
    this.tokenConsumer = super.getTokenConsumer();
    return null;
  }
  Program(...args) {
    const result = super.Program(...args);
    this.__qinSyncJsParserProperties();
    return result;
  }
  parse(...args) {
    const result = super.parse(...args);
    this.__qinSyncJsParserProperties();
    return result;
  }
}
export { SlimeParser };
export { __QinGeneratedSlimeParserBase as com_slime_parser_SlimeParser };
export default SlimeParser;
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
export {
  SlimeCstToAst,
  SlimeCstToAstUtils,
  registerSlimeCstToAstUtil
} from "./src/SlimeCstToAstUtils.ts";
