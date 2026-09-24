import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "../../../subhuti/parser/SubhutiStaticGrammar.ts";
import { com_slime_parser_expressions_SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser as SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime as UnaryStaticRuntime } from "./SlimeUnaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimeBinaryExpressionStaticGrammar, com_slime_parser_expressions_SlimeBinaryExpressionStaticGrammar as SlimeBinaryExpressionStaticGrammar } from "./SlimeBinaryExpressionStaticGrammar.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../base/SlimeJavascriptParserBase.ts";
import { com_slime_parser_expressions_SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser as SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime as PrimaryStaticRuntime } from "./SlimePrimaryExpressionParser.ts";
import { com_slime_parser_literal_SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser as SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime as LiteralStaticRuntime } from "../literal/SlimeLiteralParser.ts";
import { com_slime_parser_identifier_SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser as SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime as IdentifierStaticRuntime } from "../identifier/SlimeIdentifierParser.ts";
import { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser as SubhutiParser, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime as StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticPrefix, com_subhuti_parser_SubhutiParser$StaticPrefix as StaticPrefix, com_subhuti_parser_SubhutiParser$StaticChoice, com_subhuti_parser_SubhutiParser$StaticChoice as StaticChoice } from "../../../subhuti/parser/SubhutiParser.ts";
import { com_subhuti_parser_SubhutiParserFinal, com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal } from "../../../subhuti/parser/SubhutiParserFinal.ts";
import { com_subhuti_parser_SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators as SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl as StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher as StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext as AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext as AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes as PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates as StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame as ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames as CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys as CurrentTokenKeys } from "../../../subhuti/parser/SubhutiParserCombinators.ts";
import { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore as SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments as StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult as RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode as StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks as StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$CacheWork, com_subhuti_parser_SubhutiParserCore$CacheWork as CacheWork, com_subhuti_parser_SubhutiParserCore$FailureWork, com_subhuti_parser_SubhutiParserCore$FailureWork as FailureWork } from "../../../subhuti/parser/SubhutiParserCore.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations as ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException as SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException as SubhutiFirstTokenUnknownException } from "../../../subhuti/parser/SubhutiParserState.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../../../subhuti/lookahead/SubhutiTokenLookahead.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __qin_java_functional } from "@qin/java-sdk-js";
import { __qin_subhuti_rule_cache_key } from "@qin/java-sdk-js/tooling";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const UnsupportedOperationException = __QinJavaLangUnsupportedOperationException;
class com_slime_parser_expressions_SlimeBinaryExpressionParser extends com_slime_parser_expressions_SlimeUnaryExpressionParser {
  static __qin_field_STATIC_BINARY_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_UNARY_EXPRESSION_PREFIX_TOKENS: string[] | null = null as any;
  static __qin_field_UNARY_EXPRESSION_AWAIT_PREFIX_TOKENS: string[] | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_expressions_SlimeBinaryExpressionParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeBinaryExpressionParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_expressions_SlimeBinaryExpressionParser_1_0(sourceCode: string): void {
    null;
  }
  ExponentiationExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_ExponentiationExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_ExponentiationExpression.call(this, params);
    }), "ExponentiationExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ExponentiationExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_545: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_545.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "ExponentiationExpression", this.binaryStaticRuntime(params));
    }
    return null;
  }
  static unaryExpressionPrefixTokens(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): string[] {
    return (params.await() ? com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_UNARY_EXPRESSION_AWAIT_PREFIX_TOKENS : com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_UNARY_EXPRESSION_PREFIX_TOKENS);
  }
  MultiplicativeExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_MultiplicativeExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_MultiplicativeExpression.call(this, params);
    }), "MultiplicativeExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_MultiplicativeExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_546: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_546.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "MultiplicativeExpression", this.binaryStaticRuntime(params));
    }
    return null;
  }
  MultiplicativeOperator(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_MultiplicativeOperator receiver=this arity=0 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_MultiplicativeOperator.call(this);
    }), "MultiplicativeOperator", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_MultiplicativeOperator(): void {
    {
      const __qin_typed_receiver_547: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_547.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "MultiplicativeOperator", this.binaryStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    }
    return null;
  }
  binaryStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime(this, (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params));
  }
  canStartExponentiationExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    return (this.canStartBinaryUpdateExpression(params, lookaheadOffset) || this.canStartUnaryExpressionPrefix(params, lookaheadOffset));
  }
  canStartBinaryUnaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    return this.canStartUnaryExpressionPrefix(params, lookaheadOffset);
  }
  canStartBinaryUpdateExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    let tokenName: string = token.tokenName();
    if ((__QinJavaLangString.equals("Increment", tokenName) || __QinJavaLangString.equals("Decrement", tokenName))) {
      return true;
    }
    return this.canStartBinaryLeftHandSideExpression(params, lookaheadOffset);
  }
  canStartUnaryExpressionPrefix(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    let tokenName: string = token.tokenName();
    if ((__QinJavaLangString.equals("Delete", tokenName) || __QinJavaLangString.equals("Void", tokenName) || __QinJavaLangString.equals("Typeof", tokenName) || __QinJavaLangString.equals("Plus", tokenName) || __QinJavaLangString.equals("Minus", tokenName) || __QinJavaLangString.equals("BitwiseNot", tokenName) || __QinJavaLangString.equals("LogicalNot", tokenName))) {
      return true;
    }
    if (__QinJavaLangString.equals("Await", tokenName)) {
      return (__qin_binary__("!=", params, null) && params.await());
    }
    return false;
  }
  canStartBinaryLeftHandSideExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params);
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    let tokenName: string = token.tokenName();
    if ((__QinJavaLangString.equals("This", tokenName) || __QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("NullLiteral", tokenName) || __QinJavaLangString.equals("True", tokenName) || __QinJavaLangString.equals("False", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("Function", tokenName) || __QinJavaLangString.equals("Class", tokenName) || __QinJavaLangString.equals("LBracket", tokenName) || __QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("RegularExpressionLiteral", tokenName) || __QinJavaLangString.equals("NoSubstitutionTemplate", tokenName) || __QinJavaLangString.equals("TemplateHead", tokenName) || __QinJavaLangString.equals("LParen", tokenName) || __QinJavaLangString.equals("Super", tokenName) || __QinJavaLangString.equals("Import", tokenName) || __QinJavaLangString.equals("New", tokenName))) {
      return true;
    }
    if (__QinJavaLangString.equals("Yield", tokenName)) {
      return (!effectiveParams.yield());
    }
    if (__QinJavaLangString.equals("Await", tokenName)) {
      return (!effectiveParams.await());
    }
    return false;
  }
  canStartRelationalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    return (((__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params).in() && this.lookahead("PrivateIdentifier", lookaheadOffset)) || this.canStartExponentiationExpression((__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params), lookaheadOffset));
  }
  canStartShortCircuitExpressionTail(lookaheadOffset: number): boolean {
    return (this.lookahead("LogicalOr", lookaheadOffset) || this.lookahead("NullishCoalescing", lookaheadOffset));
  }
  canStartMultiplicativeOperator(lookaheadOffset: number): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    let tokenName: string = token.tokenName();
    return (__QinJavaLangString.equals("Asterisk", tokenName) || __QinJavaLangString.equals("Slash", tokenName) || __QinJavaLangString.equals("Modulo", tokenName));
  }
  AdditiveExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_AdditiveExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_AdditiveExpression.call(this, params);
    }), "AdditiveExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AdditiveExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_548: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_548.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "AdditiveExpression", this.binaryStaticRuntime(params));
    }
    return null;
  }
  ShiftExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_ShiftExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_ShiftExpression.call(this, params);
    }), "ShiftExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ShiftExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_549: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_549.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "ShiftExpression", this.binaryStaticRuntime(params));
    }
    return null;
  }
  RelationalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_RelationalExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_RelationalExpression.call(this, params);
    }), "RelationalExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_RelationalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_550: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_550.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "RelationalExpression", this.binaryStaticRuntime(params));
    }
    return null;
  }
  EqualityExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_EqualityExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_EqualityExpression.call(this, params);
    }), "EqualityExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_EqualityExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_551: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_551.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "EqualityExpression", this.binaryStaticRuntime(params));
    }
    return null;
  }
  BitwiseANDExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_BitwiseANDExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_BitwiseANDExpression.call(this, params);
    }), "BitwiseANDExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BitwiseANDExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_552: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_552.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "BitwiseANDExpression", this.binaryStaticRuntime(params));
    }
    return null;
  }
  BitwiseXORExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_BitwiseXORExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_BitwiseXORExpression.call(this, params);
    }), "BitwiseXORExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BitwiseXORExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_553: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_553.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "BitwiseXORExpression", this.binaryStaticRuntime(params));
    }
    return null;
  }
  BitwiseORExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_BitwiseORExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_BitwiseORExpression.call(this, params);
    }), "BitwiseORExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BitwiseORExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_554: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_554.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "BitwiseORExpression", this.binaryStaticRuntime(params));
    }
    return null;
  }
  LogicalANDExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_LogicalANDExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_LogicalANDExpression.call(this, params);
    }), "LogicalANDExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LogicalANDExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_555: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_555.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "LogicalANDExpression", this.binaryStaticRuntime(params));
    }
    return null;
  }
  LogicalORExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_LogicalORExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_LogicalORExpression.call(this, params);
    }), "LogicalORExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LogicalORExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_556: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_556.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "LogicalORExpression", this.binaryStaticRuntime(params));
    }
    return null;
  }
  CoalesceExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_CoalesceExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_CoalesceExpression.call(this, params);
    }), "CoalesceExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoalesceExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_557: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_557.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "CoalesceExpression", this.binaryStaticRuntime(params));
    }
    return null;
  }
  CoalesceExpressionHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_CoalesceExpressionHead receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_CoalesceExpressionHead.call(this, params);
    }), "CoalesceExpressionHead", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoalesceExpressionHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_558: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_558.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "CoalesceExpressionHead", this.binaryStaticRuntime(params));
    }
    return null;
  }
  ShortCircuitExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_ShortCircuitExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_ShortCircuitExpression.call(this, params);
    }), "ShortCircuitExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ShortCircuitExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_559: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_559.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "ShortCircuitExpression", this.binaryStaticRuntime(params));
    }
    return null;
  }
  ShortCircuitExpressionTail(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_ShortCircuitExpressionTail receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_ShortCircuitExpressionTail.call(this, params);
    }), "ShortCircuitExpressionTail", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ShortCircuitExpressionTail(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_560: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_560.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "ShortCircuitExpressionTail", this.binaryStaticRuntime(params));
    }
    return null;
  }
  LogicalORExpressionTail(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_LogicalORExpressionTail receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_LogicalORExpressionTail.call(this, params);
    }), "LogicalORExpressionTail", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LogicalORExpressionTail(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_561: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_561.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "LogicalORExpressionTail", this.binaryStaticRuntime(params));
    }
    return null;
  }
  CoalesceExpressionTail(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeBinaryExpressionParser method=__qin_subhuti_raw_CoalesceExpressionTail receiver=this arity=1 */ com_slime_parser_expressions_SlimeBinaryExpressionParser.prototype.__qin_subhuti_raw_CoalesceExpressionTail.call(this, params);
    }), "CoalesceExpressionTail", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoalesceExpressionTail(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_562: com_slime_parser_expressions_SlimeBinaryExpressionParser = this;
      __qin_typed_receiver_562.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "CoalesceExpressionTail", this.binaryStaticRuntime(params));
    }
    return null;
  }
}
const SlimeBinaryExpressionParser = com_slime_parser_expressions_SlimeBinaryExpressionParser;
class com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime {
  __qin_field_parser: com_slime_parser_expressions_SlimeBinaryExpressionParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_expressions_SlimeBinaryExpressionParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const effectiveParams: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime_2_0(parser, effectiveParams);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeBinaryExpressionParser$BinaryStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime_2_0(parser: com_slime_parser_expressions_SlimeBinaryExpressionParser, effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = effectiveParams;
  }
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeBinaryExpressionStaticGrammar.__qin_field_GATE_PRIVATE_IDENTIFIER_IN, gateId)) {
      return (this.__qin_field_effectiveParams.in() && this.__qin_field_parser.lookahead("PrivateIdentifier", 1.0));
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeBinaryExpressionStaticGrammar.__qin_field_GATE_UNARY_EXPRESSION_PREFIX, gateId)) {
      return this.__qin_field_parser.canStartUnaryExpressionPrefix(this.__qin_field_effectiveParams, 1.0);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported binary static gate: " + gateId));
  }
  runStaticAction(actionId: string): boolean {
    throw new __QinJavaLangUnsupportedOperationException(("unsupported binary static action: " + actionId));
  }
  canStartStaticRule(ruleName: string, variantKey: any, lookaheadOffset: number): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported binary static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("UpdateExpression", ruleName)) {
      return this.__qin_field_parser.canStartBinaryUpdateExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("UnaryExpression", ruleName)) {
      return this.__qin_field_parser.canStartBinaryUnaryExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("ExponentiationExpression", ruleName)) {
      return this.__qin_field_parser.canStartExponentiationExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("MultiplicativeOperator", ruleName)) {
      return this.__qin_field_parser.canStartMultiplicativeOperator(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("MultiplicativeExpression", ruleName)) {
      return this.__qin_field_parser.canStartExponentiationExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AdditiveExpression", ruleName)) {
      return this.__qin_field_parser.canStartExponentiationExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("ShiftExpression", ruleName)) {
      return this.__qin_field_parser.canStartExponentiationExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("RelationalExpression", ruleName)) {
      return this.__qin_field_parser.canStartRelationalExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("EqualityExpression", ruleName)) {
      return this.__qin_field_parser.canStartExponentiationExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BitwiseANDExpression", ruleName)) {
      return this.__qin_field_parser.canStartExponentiationExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BitwiseXORExpression", ruleName)) {
      return this.__qin_field_parser.canStartExponentiationExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BitwiseORExpression", ruleName)) {
      return this.__qin_field_parser.canStartExponentiationExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("LogicalANDExpression", ruleName)) {
      return this.__qin_field_parser.canStartExponentiationExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("LogicalORExpression", ruleName)) {
      return this.__qin_field_parser.canStartExponentiationExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("CoalesceExpression", ruleName)) {
      return this.__qin_field_parser.canStartExponentiationExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("CoalesceExpressionHead", ruleName)) {
      return this.__qin_field_parser.canStartExponentiationExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("ShortCircuitExpression", ruleName)) {
      return this.__qin_field_parser.canStartExponentiationExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("ShortCircuitExpressionTail", ruleName)) {
      return this.__qin_field_parser.canStartShortCircuitExpressionTail(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("LogicalORExpressionTail", ruleName)) {
      return this.__qin_field_parser.lookahead("LogicalOr", lookaheadOffset);
    }
    if (__QinJavaLangString.equals("CoalesceExpressionTail", ruleName)) {
      return this.__qin_field_parser.lookahead("NullishCoalescing", lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported binary static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported binary static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("UpdateExpression", ruleName)) {
      {
        const __qin_typed_receiver_563: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_563.UpdateExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("UnaryExpression", ruleName)) {
      {
        const __qin_typed_receiver_564: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_564.UnaryExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ExponentiationExpression", ruleName)) {
      {
        const __qin_typed_receiver_565: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_565.ExponentiationExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("MultiplicativeOperator", ruleName)) {
      {
        const __qin_typed_receiver_566: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_566.MultiplicativeOperator();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("MultiplicativeExpression", ruleName)) {
      {
        const __qin_typed_receiver_567: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_567.MultiplicativeExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AdditiveExpression", ruleName)) {
      {
        const __qin_typed_receiver_568: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_568.AdditiveExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ShiftExpression", ruleName)) {
      {
        const __qin_typed_receiver_569: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_569.ShiftExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("RelationalExpression", ruleName)) {
      {
        const __qin_typed_receiver_570: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_570.RelationalExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("EqualityExpression", ruleName)) {
      {
        const __qin_typed_receiver_571: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_571.EqualityExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BitwiseANDExpression", ruleName)) {
      {
        const __qin_typed_receiver_572: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_572.BitwiseANDExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BitwiseXORExpression", ruleName)) {
      {
        const __qin_typed_receiver_573: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_573.BitwiseXORExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BitwiseORExpression", ruleName)) {
      {
        const __qin_typed_receiver_574: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_574.BitwiseORExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("LogicalANDExpression", ruleName)) {
      {
        const __qin_typed_receiver_575: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_575.LogicalANDExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("LogicalORExpression", ruleName)) {
      {
        const __qin_typed_receiver_576: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_576.LogicalORExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CoalesceExpression", ruleName)) {
      {
        const __qin_typed_receiver_577: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_577.CoalesceExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CoalesceExpressionHead", ruleName)) {
      {
        const __qin_typed_receiver_578: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_578.CoalesceExpressionHead(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ShortCircuitExpression", ruleName)) {
      {
        const __qin_typed_receiver_579: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_579.ShortCircuitExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ShortCircuitExpressionTail", ruleName)) {
      {
        const __qin_typed_receiver_580: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_580.ShortCircuitExpressionTail(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("LogicalORExpressionTail", ruleName)) {
      {
        const __qin_typed_receiver_581: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_581.LogicalORExpressionTail(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CoalesceExpressionTail", ruleName)) {
      {
        const __qin_typed_receiver_582: com_slime_parser_expressions_SlimeBinaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_582.CoalesceExpressionTail(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported binary static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeBinaryExpressionParser$BinaryStaticRuntime = com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime;
com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR = com_slime_parser_expressions_SlimeBinaryExpressionStaticGrammar.grammar();
com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_UNARY_EXPRESSION_PREFIX_TOKENS = ["Delete", "Void", "Typeof", "Plus", "Minus", "BitwiseNot", "LogicalNot"];
com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_UNARY_EXPRESSION_AWAIT_PREFIX_TOKENS = ["Delete", "Void", "Typeof", "Plus", "Minus", "BitwiseNot", "LogicalNot", "Await"];

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_expressions_SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime };
