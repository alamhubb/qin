import { com_slime_parser_identifier_SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser as SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime as IdentifierStaticRuntime } from "../identifier/SlimeIdentifierParser.ts";
import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "../../../subhuti/parser/SubhutiStaticGrammar.ts";
import { com_subhuti_struct_LexerMode, com_subhuti_struct_LexerMode as LexerMode } from "../../../subhuti/struct/LexerMode.ts";
import { com_slime_parser_literal_SlimeLiteralStaticGrammar, com_slime_parser_literal_SlimeLiteralStaticGrammar as SlimeLiteralStaticGrammar } from "./SlimeLiteralStaticGrammar.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../base/SlimeJavascriptParserBase.ts";
import { com_subhuti_struct_SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken as SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken$Builder } from "../../../subhuti/struct/SubhutiMatchToken.ts";
import { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser as SubhutiParser, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime as StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticPrefix, com_subhuti_parser_SubhutiParser$StaticPrefix as StaticPrefix, com_subhuti_parser_SubhutiParser$StaticChoice, com_subhuti_parser_SubhutiParser$StaticChoice as StaticChoice } from "../../../subhuti/parser/SubhutiParser.ts";
import { com_subhuti_parser_SubhutiParserFinal, com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal } from "../../../subhuti/parser/SubhutiParserFinal.ts";
import { com_subhuti_parser_SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators as SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl as StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher as StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext as AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext as AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes as PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates as StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame as ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames as CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys as CurrentTokenKeys } from "../../../subhuti/parser/SubhutiParserCombinators.ts";
import { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore as SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments as StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult as RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode as StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks as StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$CacheWork, com_subhuti_parser_SubhutiParserCore$CacheWork as CacheWork, com_subhuti_parser_SubhutiParserCore$FailureWork, com_subhuti_parser_SubhutiParserCore$FailureWork as FailureWork } from "../../../subhuti/parser/SubhutiParserCore.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations as ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException as SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException as SubhutiFirstTokenUnknownException } from "../../../subhuti/parser/SubhutiParserState.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../../../subhuti/lookahead/SubhutiTokenLookahead.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __qin_java_functional, __QinJavaUtilList, __QinJavaUtilUnmodifiableList } from "@qin/java-sdk-js";
import { __qin_subhuti_rule_cache_key } from "@qin/java-sdk-js/tooling";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const UnsupportedOperationException = __QinJavaLangUnsupportedOperationException;
class com_slime_parser_literal_SlimeLiteralParser extends com_slime_parser_identifier_SlimeIdentifierParser {
  static __qin_field_STATIC_LITERAL_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_literal_SlimeLiteralParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeLiteralParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_literal_SlimeLiteralParser_1_0(sourceCode: string): void {
    null;
  }
  Literal(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Literal();
    }), "Literal", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_Literal(): any {
    this.executeStaticRule(com_slime_parser_literal_SlimeLiteralParser.__qin_field_STATIC_LITERAL_GRAMMAR, "Literal", this.literalStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    return null;
  }
  BooleanLiteral(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BooleanLiteral();
    }), "BooleanLiteral", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_BooleanLiteral(): any {
    this.executeStaticRule(com_slime_parser_literal_SlimeLiteralParser.__qin_field_STATIC_LITERAL_GRAMMAR, "BooleanLiteral", this.literalStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    return null;
  }
  literalStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return new com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime(this, (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params));
  }
  templateTailTokenNameAt(lookaheadOffset: number): any {
    return (__qin_binary__("==", this.LA(lookaheadOffset, __QinJavaUtilList.of(com_subhuti_struct_LexerMode.__qin_field_TEMPLATE_TAIL)), null) ? null : this.LA(lookaheadOffset, __QinJavaUtilList.of(com_subhuti_struct_LexerMode.__qin_field_TEMPLATE_TAIL)).tokenName());
  }
  NumericLiteral(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_NumericLiteral();
    }), "NumericLiteral", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_NumericLiteral(): any {
    this.__qin_field_tokenConsumer.NumericLiteral();
    return null;
  }
  StringLiteral(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_StringLiteral();
    }), "StringLiteral", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_StringLiteral(): any {
    this.__qin_field_tokenConsumer.StringLiteral();
    return null;
  }
  RegularExpressionLiteral(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_RegularExpressionLiteral();
    }), "RegularExpressionLiteral", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_RegularExpressionLiteral(): any {
    this.__qin_field_tokenConsumer.RegularExpressionLiteral();
    return null;
  }
  consumeRegularExpressionLiteral(): any {
    this.RegularExpressionLiteral();
    return null;
  }
  TemplateLiteral(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_TemplateLiteral_1_0(__qin_args[0]);
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_TemplateLiteral_1_1(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_TemplateLiteral_0_2();
    throw new Error("Unsupported Java overload: TemplateLiteral/" + __qin_args.length);
  }
  __qin_overload_TemplateLiteral_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_TemplateLiteral_1_0(params);
    }), "TemplateLiteral", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_TemplateLiteral_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams): any {
    let effectiveParams: any = (__qin_binary__("==", params, null) ? new com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams() : params);
    this.executeStaticRule(com_slime_parser_literal_SlimeLiteralParser.__qin_field_STATIC_LITERAL_GRAMMAR, "TemplateLiteral", this.literalStaticRuntime(effectiveParams.expressionParams()));
    return null;
  }
  __qin_overload_TemplateLiteral_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_TemplateLiteral_1_1(params);
    }), "TemplateLiteral", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_TemplateLiteral_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.TemplateLiteral(new com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams(params, false));
    return null;
  }
  __qin_overload_TemplateLiteral_0_2(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_TemplateLiteral_0_2();
    }), "TemplateLiteral", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_TemplateLiteral_0_2(): any {
    this.TemplateLiteral(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    return null;
  }
  NoSubstitutionTemplate(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_NoSubstitutionTemplate();
    }), "NoSubstitutionTemplate", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_NoSubstitutionTemplate(): any {
    this.__qin_field_tokenConsumer.NoSubstitutionTemplate();
    return null;
  }
  SubstitutionTemplate(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_SubstitutionTemplate(params);
    }), "SubstitutionTemplate", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_SubstitutionTemplate(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.__qin_field_tokenConsumer.TemplateHead();
    this.Expression(params.withIn(true));
    this.TemplateSpans(params);
    return null;
  }
  TemplateSpans(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TemplateSpans(params);
    }), "TemplateSpans", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_TemplateSpans(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_literal_SlimeLiteralParser.__qin_field_STATIC_LITERAL_GRAMMAR, "TemplateSpans", this.literalStaticRuntime(params));
    return null;
  }
  TemplateMiddleList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TemplateMiddleList(params);
    }), "TemplateMiddleList", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_TemplateMiddleList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_literal_SlimeLiteralParser.__qin_field_STATIC_LITERAL_GRAMMAR, "TemplateMiddleList", this.literalStaticRuntime(params));
    return null;
  }
  TemplateMiddleExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TemplateMiddleExpression(params);
    }), "TemplateMiddleExpression", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_TemplateMiddleExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_literal_SlimeLiteralParser.__qin_field_STATIC_LITERAL_GRAMMAR, "TemplateMiddleExpression", this.literalStaticRuntime(params));
    return null;
  }
  Expression(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_Expression_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_Expression_0_1();
    throw new Error("Unsupported Java overload: Expression/" + __qin_args.length);
  }
  __qin_overload_Expression_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    throw new Error("Abstract Java method is not implemented: Expression");
  }
  __qin_overload_Expression_0_1(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_Expression_0_1();
    }), "Expression", "SlimeLiteralParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_Expression_0_1(): any {
    this.Expression(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    return null;
  }
  canStartTemplateExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    if ((__QinJavaLangString.equals("This", tokenName) || __QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("NullLiteral", tokenName) || __QinJavaLangString.equals("True", tokenName) || __QinJavaLangString.equals("False", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("Function", tokenName) || __QinJavaLangString.equals("Class", tokenName) || __QinJavaLangString.equals("LBracket", tokenName) || __QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("RegularExpressionLiteral", tokenName) || __QinJavaLangString.equals("NoSubstitutionTemplate", tokenName) || __QinJavaLangString.equals("TemplateHead", tokenName) || __QinJavaLangString.equals("LParen", tokenName) || __QinJavaLangString.equals("Super", tokenName) || __QinJavaLangString.equals("Import", tokenName) || __QinJavaLangString.equals("New", tokenName) || __QinJavaLangString.equals("Plus", tokenName) || __QinJavaLangString.equals("Minus", tokenName) || __QinJavaLangString.equals("Tilde", tokenName) || __QinJavaLangString.equals("Exclamation", tokenName) || __QinJavaLangString.equals("Delete", tokenName) || __QinJavaLangString.equals("Void", tokenName) || __QinJavaLangString.equals("Typeof", tokenName))) {
      return true;
    }
    if (__QinJavaLangString.equals("Yield", tokenName)) {
      return (!params.__qin_yield());
    }
    if (__QinJavaLangString.equals("Await", tokenName)) {
      return (!params.__qin_await());
    }
    return false;
  }
}
const SlimeLiteralParser = com_slime_parser_literal_SlimeLiteralParser;
class com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime {
  __qin_field_parser: com_slime_parser_literal_SlimeLiteralParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_literal_SlimeLiteralParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const effectiveParams: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime_2_0(parser, effectiveParams);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeLiteralParser$LiteralStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime_2_0(parser: com_slime_parser_literal_SlimeLiteralParser, effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = effectiveParams;
  }
  runStaticAction(actionId: string): any {
    if (__QinJavaLangString.equals(com_slime_parser_literal_SlimeLiteralStaticGrammar.__qin_field_ACTION_LITERAL_NULL, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_literal_SlimeLiteralStaticGrammar.__qin_field_ACTION_LITERAL_NUMERIC, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_literal_SlimeLiteralStaticGrammar.__qin_field_ACTION_LITERAL_STRING, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_literal_SlimeLiteralStaticGrammar.__qin_field_ACTION_BOOLEAN_TRUE, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_literal_SlimeLiteralStaticGrammar.__qin_field_ACTION_BOOLEAN_FALSE, actionId)) {
      return true;
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported literal static action: " + actionId));
  }
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
    if ((__QinJavaLangString.equals("BooleanLiteral", ruleName) && __qin_binary__("==", variantKey, null))) {
      let tokenName: any = this.__qin_field_parser.tokenNameAt(lookaheadOffset);
      return (__QinJavaLangString.equals("True", tokenName) || __QinJavaLangString.equals("False", tokenName));
    }
    if ((__QinJavaLangString.equals("SubstitutionTemplate", ruleName) && __qin_binary__("==", variantKey, null))) {
      return __QinJavaLangString.equals("TemplateHead", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if ((__QinJavaLangString.equals("NoSubstitutionTemplate", ruleName) && __qin_binary__("==", variantKey, null))) {
      return __QinJavaLangString.equals("NoSubstitutionTemplate", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if ((__QinJavaLangString.equals("TemplateMiddleList", ruleName) && __qin_binary__("==", variantKey, null))) {
      return __QinJavaLangString.equals("TemplateMiddle", this.__qin_field_parser.templateTailTokenNameAt(lookaheadOffset));
    }
    if ((__QinJavaLangString.equals("TemplateMiddleExpression", ruleName) && __qin_binary__("==", variantKey, null))) {
      return __QinJavaLangString.equals("TemplateMiddle", this.__qin_field_parser.templateTailTokenNameAt(lookaheadOffset));
    }
    if ((__QinJavaLangString.equals("Expression", ruleName) && __qin_binary__("==", variantKey, null))) {
      return this.__qin_field_parser.canStartTemplateExpression(this.__qin_field_effectiveParams.withIn(true), lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported literal static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if ((__QinJavaLangString.equals("BooleanLiteral", ruleName) && __qin_binary__("==", variantKey, null))) {
      this.__qin_field_parser.BooleanLiteral();
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__QinJavaLangString.equals("SubstitutionTemplate", ruleName) && __qin_binary__("==", variantKey, null))) {
      this.__qin_field_parser.SubstitutionTemplate(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__QinJavaLangString.equals("NoSubstitutionTemplate", ruleName) && __qin_binary__("==", variantKey, null))) {
      this.__qin_field_parser.NoSubstitutionTemplate();
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__QinJavaLangString.equals("TemplateMiddleList", ruleName) && __qin_binary__("==", variantKey, null))) {
      this.__qin_field_parser.TemplateMiddleList(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__QinJavaLangString.equals("TemplateMiddleExpression", ruleName) && __qin_binary__("==", variantKey, null))) {
      this.__qin_field_parser.TemplateMiddleExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__QinJavaLangString.equals("Expression", ruleName) && __qin_binary__("==", variantKey, null))) {
      this.__qin_field_parser.Expression(this.__qin_field_effectiveParams.withIn(true));
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported literal static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeLiteralParser$LiteralStaticRuntime = com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime;
com_slime_parser_literal_SlimeLiteralParser.__qin_field_STATIC_LITERAL_GRAMMAR = com_slime_parser_literal_SlimeLiteralStaticGrammar.grammar();

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_literal_SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime };
