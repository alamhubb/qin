import { com_slime_parser_literal_SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser as SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime as LiteralStaticRuntime } from "../literal/SlimeLiteralParser.ts";
import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "../../../subhuti/parser/SubhutiStaticGrammar.ts";
import { com_subhuti_struct_LexerMode, com_subhuti_struct_LexerMode as LexerMode } from "../../../subhuti/struct/LexerMode.ts";
import { com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar, com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar as SlimePrimaryExpressionStaticGrammar } from "./SlimePrimaryExpressionStaticGrammar.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../base/SlimeJavascriptParserBase.ts";
import { com_slime_parser_identifier_SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser as SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime as IdentifierStaticRuntime } from "../identifier/SlimeIdentifierParser.ts";
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
const RuntimeException = __QinJavaLangRuntimeException;
const UnsupportedOperationException = __QinJavaLangUnsupportedOperationException;
class com_slime_parser_expressions_SlimePrimaryExpressionParser extends com_slime_parser_literal_SlimeLiteralParser {
  static __qin_field_STATIC_PRIMARY_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_expressions_SlimePrimaryExpressionParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimePrimaryExpressionParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_expressions_SlimePrimaryExpressionParser_1_0(sourceCode: string): void {
    null;
  }
  PrimaryExpression(...__qin_args: any[]): void {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_PrimaryExpression_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_PrimaryExpression_0_1();
    throw new Error("Unsupported Java overload: PrimaryExpression/" + __qin_args.length);
  }
  __qin_overload_PrimaryExpression_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw___qin_overload_PrimaryExpression_1_0 receiver=this arity=1 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw___qin_overload_PrimaryExpression_1_0.call(this, params);
    }), "PrimaryExpression", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_PrimaryExpression_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_427: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_427.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "PrimaryExpression", this.primaryStaticRuntime(params));
    }
    return null;
  }
  __qin_overload_PrimaryExpression_0_1(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw___qin_overload_PrimaryExpression_0_1 receiver=this arity=0 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw___qin_overload_PrimaryExpression_0_1.call(this);
    }), "PrimaryExpression", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_PrimaryExpression_0_1(): void {
    {
      const __qin_typed_receiver_428: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_428.PrimaryExpression(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    }
    return null;
  }
  ThisExpression(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_ThisExpression receiver=this arity=0 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_ThisExpression.call(this);
    }), "ThisExpression", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ThisExpression(): void {
    {
      const __qin_typed_receiver_429: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_429.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "ThisExpression", this.primaryStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    }
    return null;
  }
  ArrayLiteral(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_ArrayLiteral receiver=this arity=1 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_ArrayLiteral.call(this, params);
    }), "ArrayLiteral", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrayLiteral(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_430: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_430.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "ArrayLiteral", this.primaryStaticRuntime(params));
    }
    return null;
  }
  ElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_ElementList receiver=this arity=1 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_ElementList.call(this, params);
    }), "ElementList", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_431: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_431.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "ElementList", this.primaryStaticRuntime(params));
    }
    return null;
  }
  Elision(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_Elision receiver=this arity=0 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_Elision.call(this);
    }), "Elision", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_Elision(): void {
    {
      const __qin_typed_receiver_432: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_432.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "Elision", this.primaryStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    }
    return null;
  }
  SpreadElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_SpreadElement receiver=this arity=1 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_SpreadElement.call(this, params);
    }), "SpreadElement", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_SpreadElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_433: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_433.Ellipsis();
    }
    {
      const __qin_typed_receiver_434: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_434.AssignmentExpression(params.withIn(true));
    }
    return null;
  }
  ObjectLiteral(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_ObjectLiteral receiver=this arity=1 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_ObjectLiteral.call(this, params);
    }), "ObjectLiteral", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ObjectLiteral(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_435: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_435.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "ObjectLiteral", this.primaryStaticRuntime(params));
    }
    return null;
  }
  PropertyDefinitionList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_PropertyDefinitionList receiver=this arity=1 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_PropertyDefinitionList.call(this, params);
    }), "PropertyDefinitionList", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_PropertyDefinitionList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_436: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_436.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "PropertyDefinitionList", this.primaryStaticRuntime(params));
    }
    return null;
  }
  PropertyDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_PropertyDefinition receiver=this arity=1 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_PropertyDefinition.call(this, params);
    }), "PropertyDefinition", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_PropertyDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_437: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_437.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "PropertyDefinition", this.primaryStaticRuntime(params));
    }
    return null;
  }
  PropertyName(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_PropertyName receiver=this arity=1 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_PropertyName.call(this, params);
    }), "PropertyName", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_PropertyName(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_438: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_438.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "PropertyName", this.primaryStaticRuntime(params));
    }
    return null;
  }
  LiteralPropertyName(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_LiteralPropertyName receiver=this arity=0 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_LiteralPropertyName.call(this);
    }), "LiteralPropertyName", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_LiteralPropertyName(): void {
    {
      const __qin_typed_receiver_439: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_439.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "LiteralPropertyName", this.primaryStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    }
    return null;
  }
  primaryStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime(this, (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params));
  }
  canStartStaticPrimaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("This", this.tokenNameAt(lookaheadOffset)) || this.canStartAsyncGeneratorExpressionAt(lookaheadOffset) || this.canStartAsyncFunctionExpressionAt(lookaheadOffset) || this.canStartIdentifierReference(params, lookaheadOffset) || this.canStartStaticLiteral(lookaheadOffset) || this.canStartGeneratorExpressionAt(lookaheadOffset) || this.canStartFunctionExpressionAt(lookaheadOffset) || __QinJavaLangString.equals("Class", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBracket", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBrace", this.tokenNameAt(lookaheadOffset)) || this.canStartRegularExpressionLiteralAt(lookaheadOffset) || this.canStartStaticTemplateLiteral(lookaheadOffset) || __QinJavaLangString.equals("LParen", this.tokenNameAt(lookaheadOffset)));
  }
  canStartRegularExpressionLiteralAt(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset, __QinJavaUtilList.of(com_subhuti_struct_LexerMode.__qin_field_REGEXP)), null) && __QinJavaLangString.equals("RegularExpressionLiteral", this.LA(lookaheadOffset, __QinJavaUtilList.of(com_subhuti_struct_LexerMode.__qin_field_REGEXP)).tokenName()));
  }
  canStartStaticLiteral(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("NullLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("True", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("False", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("NumericLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("StringLiteral", this.tokenNameAt(lookaheadOffset)));
  }
  canStartStaticTemplateLiteral(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("NoSubstitutionTemplate", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("TemplateHead", this.tokenNameAt(lookaheadOffset)));
  }
  canStartGeneratorExpressionAt(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Function", this.tokenNameAt(lookaheadOffset)) && __QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartFunctionExpressionAt(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Function", this.tokenNameAt(lookaheadOffset)) && !__QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartAsyncGeneratorExpressionAt(lookaheadOffset: number): boolean {
    if (__qin_binary__("==", lookaheadOffset, 1.0)) {
      return this.canStartAsyncGeneratorExpression();
    }
    let functionToken: com_subhuti_struct_SubhutiMatchToken = this.LA(__qin_binary__("+", lookaheadOffset, 1.0));
    return (this.matchIdentifierValue("async", lookaheadOffset) && __qin_binary__("!=", functionToken, null) && !functionToken.hasLineBreakBefore() && __QinJavaLangString.equals("Function", functionToken.tokenName()) && __QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 2.0))));
  }
  canStartAsyncFunctionExpressionAt(lookaheadOffset: number): boolean {
    if (__qin_binary__("==", lookaheadOffset, 1.0)) {
      return this.canStartAsyncFunctionExpression();
    }
    let functionToken: com_subhuti_struct_SubhutiMatchToken = this.LA(__qin_binary__("+", lookaheadOffset, 1.0));
    return (this.matchIdentifierValue("async", lookaheadOffset) && __qin_binary__("!=", functionToken, null) && !functionToken.hasLineBreakBefore() && __QinJavaLangString.equals("Function", functionToken.tokenName()) && !__QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 2.0))));
  }
  canStartArrayElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let offset: number = lookaheadOffset;
    while (__QinJavaLangString.equals("Comma", this.tokenNameAt(offset))) {
      offset++;
    }
    return this.canStartArrayElement(params, offset);
  }
  canStartArrayContinuation(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): boolean {
    if ((!__QinJavaLangString.equals("Comma", this.tokenNameAt(1.0)))) {
      return false;
    }
    let offset: number = 2.0;
    while (__QinJavaLangString.equals("Comma", this.tokenNameAt(offset))) {
      offset++;
    }
    return this.canStartArrayElement(params, offset);
  }
  canStartArrayElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Ellipsis", this.tokenNameAt(lookaheadOffset)) || this.canStartAssignmentExpression(params.withIn(true), lookaheadOffset));
  }
  canStartPropertyDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    if ((__QinJavaLangString.equals("Ellipsis", tokenName) || __QinJavaLangString.equals("LBracket", tokenName) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || this.canStartIdentifierName(lookaheadOffset))) {
      return true;
    }
    return (__QinJavaLangString.equals("Async", tokenName) || __QinJavaLangString.equals("Function", tokenName) || __QinJavaLangString.equals("Asterisk", tokenName) || __QinJavaLangString.equals("Get", tokenName) || __QinJavaLangString.equals("Set", tokenName));
  }
  canStartPropertyAssignment(lookaheadOffset: number): boolean {
    return (__qin_binary__(">", this.propertyNameEndOffset(lookaheadOffset), 0.0) && __QinJavaLangString.equals("Colon", this.tokenNameAt(this.propertyNameEndOffset(lookaheadOffset))));
  }
  canStartCoverInitializedProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    return (this.canStartIdentifierReference(params, lookaheadOffset) && __QinJavaLangString.equals("Assign", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartObjectPropertyMethodDefinition(lookaheadOffset: number): boolean {
    if (__QinJavaLangString.equals("Asterisk", this.tokenNameAt(lookaheadOffset))) {
      return this.hasMethodSuffixAfterPropertyName(__qin_binary__("+", lookaheadOffset, 1.0));
    }
    if ((this.matchIdentifierValue("async", lookaheadOffset) && !this.hasTokenLineBreakBefore(__qin_binary__("+", lookaheadOffset, 1.0)) && __QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))))) {
      return this.hasMethodSuffixAfterPropertyName(__qin_binary__("+", lookaheadOffset, 2.0));
    }
    if ((this.matchIdentifierValue("async", lookaheadOffset) && !this.hasTokenLineBreakBefore(__qin_binary__("+", lookaheadOffset, 1.0)) && this.hasMethodSuffixAfterPropertyName(__qin_binary__("+", lookaheadOffset, 1.0)))) {
      return true;
    }
    if (((this.matchIdentifierValue("get", lookaheadOffset) || this.matchIdentifierValue("set", lookaheadOffset)) && this.hasMethodSuffixAfterPropertyName(__qin_binary__("+", lookaheadOffset, 1.0)))) {
      return true;
    }
    return this.hasMethodSuffixAfterPropertyName(lookaheadOffset);
  }
  hasTokenLineBreakBefore(lookaheadOffset: number): boolean {
    try {
      let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
      return (__qin_binary__("!=", token, null) && token.hasLineBreakBefore());
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      return false;
    }
    return null;
  }
  hasMethodSuffixAfterPropertyName(propertyNameOffset: number): boolean {
    let nextOffset: number = this.propertyNameEndOffset(propertyNameOffset);
    if (__qin_binary__("<", nextOffset, 0.0)) {
      return false;
    }
    let suffix: string = this.tokenNameAt(nextOffset);
    return (__QinJavaLangString.equals("LParen", suffix) || __QinJavaLangString.equals("Less", suffix));
  }
  propertyNameEndOffset(lookaheadOffset: number): number {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    if (__QinJavaLangString.equals("LBracket", tokenName)) {
      return this.computedPropertyNameEndOffset(lookaheadOffset);
    }
    if ((__QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || this.canStartIdentifierName(lookaheadOffset))) {
      return __qin_binary__("+", lookaheadOffset, 1.0);
    }
    return __qin_binary__("-", 0.0, 1.0);
  }
  computedPropertyNameEndOffset(lookaheadOffset: number): number {
    let depth: number = 0.0;
    for (let offset: number = lookaheadOffset; true; offset++) {
      let tokenName: string = this.tokenNameAt(offset);
      if (__qin_binary__("==", tokenName, null)) {
        return __qin_binary__("-", 0.0, 1.0);
      }
      if (__QinJavaLangString.equals("LBracket", tokenName)) {
        depth++;
      } else {
        if (__QinJavaLangString.equals("RBracket", tokenName)) {
          depth--;
          if (__qin_binary__("==", depth, 0.0)) {
            return __qin_binary__("+", offset, 1.0);
          }
        }
      }
    }
    return null;
  }
  ComputedPropertyName(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_ComputedPropertyName receiver=this arity=1 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_ComputedPropertyName.call(this, params);
    }), "ComputedPropertyName", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ComputedPropertyName(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_440: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_440.LBracket();
    }
    {
      const __qin_typed_receiver_441: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_441.AssignmentExpression(params.withIn(true));
    }
    {
      const __qin_typed_receiver_442: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_442.RBracket();
    }
    return null;
  }
  CoverInitializedName(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_CoverInitializedName receiver=this arity=1 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_CoverInitializedName.call(this, params);
    }), "CoverInitializedName", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoverInitializedName(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_443: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_443.IdentifierReference(params);
    }
    {
      const __qin_typed_receiver_444: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_444.Initializer(params);
    }
    return null;
  }
  Initializer(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_Initializer receiver=this arity=1 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_Initializer.call(this, params);
    }), "Initializer", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Initializer(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_445: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_445.Assign();
    }
    {
      const __qin_typed_receiver_446: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_446.AssignmentExpression(params);
    }
    return null;
  }
  ParenthesizedExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_ParenthesizedExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_ParenthesizedExpression.call(this, params);
    }), "ParenthesizedExpression", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ParenthesizedExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_447: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_447.LParen();
    }
    {
      const __qin_typed_receiver_448: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_448.Expression(params.withIn(true));
    }
    {
      const __qin_typed_receiver_449: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_449.RParen();
    }
    return null;
  }
  CoverParenthesizedExpressionAndArrowParameterList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw_CoverParenthesizedExpressionAndArrowParameterList receiver=this arity=1 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw_CoverParenthesizedExpressionAndArrowParameterList.call(this, params);
    }), "CoverParenthesizedExpressionAndArrowParameterList", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoverParenthesizedExpressionAndArrowParameterList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_450: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_450.LParen();
    }
    if (this.isParserFail()) {
      return null;
    }
    if (__QinJavaLangString.equals("RParen", this.tokenNameAt(1.0))) {
      {
        const __qin_typed_receiver_451: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_451.RParen();
      }
      return null;
    }
    if (__QinJavaLangString.equals("Ellipsis", this.tokenNameAt(1.0))) {
      {
        const __qin_typed_receiver_452: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_452.Ellipsis();
      }
      {
        const __qin_typed_receiver_453: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
        __qin_typed_receiver_453.coverParenthesizedBindingTarget(params);
      }
      {
        const __qin_typed_receiver_454: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_454.RParen();
      }
      return null;
    }
    {
      const __qin_typed_receiver_455: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_455.Expression(params.withIn(true));
    }
    if (this.isParserFail()) {
      return null;
    }
    if (__QinJavaLangString.equals("Comma", this.tokenNameAt(1.0))) {
      {
        const __qin_typed_receiver_456: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_456.Comma();
      }
      if (__QinJavaLangString.equals("Ellipsis", this.tokenNameAt(1.0))) {
        {
          const __qin_typed_receiver_457: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
          __qin_typed_receiver_457.Ellipsis();
        }
        {
          const __qin_typed_receiver_458: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
          __qin_typed_receiver_458.coverParenthesizedBindingTarget(params);
        }
      }
    }
    {
      const __qin_typed_receiver_459: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_459.RParen();
    }
    return null;
  }
  coverParenthesizedBindingTarget(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    let tokenName: string = this.tokenNameAt(1.0);
    if ((__QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("LBracket", tokenName))) {
      {
        const __qin_typed_receiver_460: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
        __qin_typed_receiver_460.BindingPattern(params);
      }
      return null;
    }
    {
      const __qin_typed_receiver_461: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_461.BindingIdentifier(params);
    }
    return null;
  }
  AssignmentExpression(...__qin_args: any[]): void {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_AssignmentExpression_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_AssignmentExpression_0_1();
    throw new Error("Unsupported Java overload: AssignmentExpression/" + __qin_args.length);
  }
  __qin_overload_AssignmentExpression_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    throw new Error("Abstract Java method is not implemented: AssignmentExpression");
  }
  __qin_overload_AssignmentExpression_0_1(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimePrimaryExpressionParser method=__qin_subhuti_raw___qin_overload_AssignmentExpression_0_1 receiver=this arity=0 */ com_slime_parser_expressions_SlimePrimaryExpressionParser.prototype.__qin_subhuti_raw___qin_overload_AssignmentExpression_0_1.call(this);
    }), "AssignmentExpression", "SlimePrimaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_AssignmentExpression_0_1(): void {
    {
      const __qin_typed_receiver_462: com_slime_parser_expressions_SlimePrimaryExpressionParser = this;
      __qin_typed_receiver_462.AssignmentExpression(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    }
    return null;
  }
}
const SlimePrimaryExpressionParser = com_slime_parser_expressions_SlimePrimaryExpressionParser;
class com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime {
  __qin_field_parser: com_slime_parser_expressions_SlimePrimaryExpressionParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_expressions_SlimePrimaryExpressionParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const effectiveParams: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime_2_0(parser, effectiveParams);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimePrimaryExpressionParser$PrimaryStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime_2_0(parser: com_slime_parser_expressions_SlimePrimaryExpressionParser, effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = effectiveParams;
  }
  canStartStaticRule(...__qin_args: any[]): boolean {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): boolean {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported primary static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("IdentifierName", ruleName)) {
      return this.__qin_field_parser.canStartIdentifierName(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("PrimaryExpression", ruleName)) {
      return this.__qin_field_parser.canStartStaticPrimaryExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("ThisExpression", ruleName)) {
      return __QinJavaLangString.equals("This", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("AsyncGeneratorExpression", ruleName)) {
      return this.__qin_field_parser.canStartAsyncGeneratorExpressionAt(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AsyncFunctionExpression", ruleName)) {
      return this.__qin_field_parser.canStartAsyncFunctionExpressionAt(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("IdentifierReference", ruleName)) {
      return this.__qin_field_parser.canStartIdentifierReference(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("Literal", ruleName)) {
      return this.__qin_field_parser.canStartStaticLiteral(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("GeneratorExpression", ruleName)) {
      return this.__qin_field_parser.canStartGeneratorExpressionAt(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("FunctionExpression", ruleName)) {
      return this.__qin_field_parser.canStartFunctionExpressionAt(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("ClassExpression", ruleName)) {
      return __QinJavaLangString.equals("Class", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("RegularExpressionLiteral", ruleName)) {
      return this.__qin_field_parser.canStartRegularExpressionLiteralAt(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TemplateLiteral", ruleName)) {
      return this.__qin_field_parser.canStartStaticTemplateLiteral(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("CoverParenthesizedExpressionAndArrowParameterList", ruleName)) {
      return __QinJavaLangString.equals("LParen", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("LiteralPropertyName", ruleName)) {
      return (this.__qin_field_parser.canStartIdentifierName(lookaheadOffset) || __QinJavaLangString.equals("StringLiteral", this.__qin_field_parser.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("NumericLiteral", this.__qin_field_parser.tokenNameAt(lookaheadOffset)));
    }
    if (__QinJavaLangString.equals("PropertyName", ruleName)) {
      return (__QinJavaLangString.equals("LBracket", this.__qin_field_parser.tokenNameAt(lookaheadOffset)) || this.__qin_field_parser.canStartIdentifierName(lookaheadOffset) || __QinJavaLangString.equals("StringLiteral", this.__qin_field_parser.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("NumericLiteral", this.__qin_field_parser.tokenNameAt(lookaheadOffset)));
    }
    if (__QinJavaLangString.equals("StringLiteral", ruleName)) {
      return __QinJavaLangString.equals("StringLiteral", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("NumericLiteral", ruleName)) {
      return __QinJavaLangString.equals("NumericLiteral", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("ComputedPropertyName", ruleName)) {
      return __QinJavaLangString.equals("LBracket", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Elision", ruleName)) {
      return __QinJavaLangString.equals("Comma", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("ArrayLiteral", ruleName)) {
      return __QinJavaLangString.equals("LBracket", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("ElementList", ruleName)) {
      return this.__qin_field_parser.canStartArrayElementList(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("ObjectLiteral", ruleName)) {
      return __QinJavaLangString.equals("LBrace", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("PropertyDefinitionList", ruleName)) {
      return this.__qin_field_parser.canStartPropertyDefinition(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("PropertyDefinition", ruleName)) {
      return this.__qin_field_parser.canStartPropertyDefinition(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("SpreadElement", ruleName)) {
      return __QinJavaLangString.equals("Ellipsis", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("CoverInitializedName", ruleName)) {
      return this.__qin_field_parser.canStartCoverInitializedProperty(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("MethodDefinition", ruleName)) {
      return this.__qin_field_parser.canStartObjectPropertyMethodDefinition(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AssignmentExpression", ruleName)) {
      return this.__qin_field_parser.canStartAssignmentExpression(this.__qin_field_effectiveParams.withIn(true), lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported primary static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar.__qin_field_GATE_ASYNC_GENERATOR_EXPRESSION, gateId)) {
      return this.__qin_field_parser.canStartAsyncGeneratorExpressionAt(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar.__qin_field_GATE_ASYNC_FUNCTION_EXPRESSION, gateId)) {
      return this.__qin_field_parser.canStartAsyncFunctionExpressionAt(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar.__qin_field_GATE_IDENTIFIER_REFERENCE, gateId)) {
      return (!this.__qin_field_parser.canStartAsyncGeneratorExpressionAt(1.0) && !this.__qin_field_parser.canStartAsyncFunctionExpressionAt(1.0) && this.__qin_field_parser.canStartIdentifierReference(this.__qin_field_effectiveParams, 1.0));
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar.__qin_field_GATE_GENERATOR_EXPRESSION, gateId)) {
      return this.__qin_field_parser.canStartGeneratorExpressionAt(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar.__qin_field_GATE_FUNCTION_EXPRESSION, gateId)) {
      return this.__qin_field_parser.canStartFunctionExpressionAt(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar.__qin_field_GATE_ARRAY_ELEMENT_LIST_START, gateId)) {
      return this.__qin_field_parser.canStartArrayElementList(this.__qin_field_effectiveParams, 1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar.__qin_field_GATE_ARRAY_ELISION_ONLY_START, gateId)) {
      return (__QinJavaLangString.equals("Comma", this.__qin_field_parser.tokenNameAt(1.0)) && !this.__qin_field_parser.canStartArrayElementList(this.__qin_field_effectiveParams, 1.0));
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar.__qin_field_GATE_ARRAY_CONTINUATION_START, gateId)) {
      return this.__qin_field_parser.canStartArrayContinuation(this.__qin_field_effectiveParams);
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar.__qin_field_GATE_PROPERTY_ASSIGNMENT, gateId)) {
      return this.__qin_field_parser.canStartPropertyAssignment(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar.__qin_field_GATE_PROPERTY_COVER_INITIALIZED, gateId)) {
      return (!this.__qin_field_parser.canStartPropertyAssignment(1.0) && this.__qin_field_parser.canStartCoverInitializedProperty(this.__qin_field_effectiveParams, 1.0));
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar.__qin_field_GATE_PROPERTY_METHOD, gateId)) {
      return (!this.__qin_field_parser.canStartPropertyAssignment(1.0) && !this.__qin_field_parser.canStartCoverInitializedProperty(this.__qin_field_effectiveParams, 1.0) && this.__qin_field_parser.canStartObjectPropertyMethodDefinition(1.0));
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar.__qin_field_GATE_PROPERTY_SHORTHAND, gateId)) {
      return (!this.__qin_field_parser.canStartPropertyAssignment(1.0) && !this.__qin_field_parser.canStartCoverInitializedProperty(this.__qin_field_effectiveParams, 1.0) && !this.__qin_field_parser.canStartObjectPropertyMethodDefinition(1.0) && this.__qin_field_parser.canStartIdentifierReference(this.__qin_field_effectiveParams, 1.0));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported primary static gate: " + gateId));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported primary static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("IdentifierName", ruleName)) {
      {
        const __qin_typed_receiver_463: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_463.IdentifierName();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("PrimaryExpression", ruleName)) {
      {
        const __qin_typed_receiver_464: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_464.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "PrimaryExpression", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ThisExpression", ruleName)) {
      {
        const __qin_typed_receiver_465: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_465.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "ThisExpression", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AsyncGeneratorExpression", ruleName)) {
      {
        const __qin_typed_receiver_466: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_466.AsyncGeneratorExpression();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AsyncFunctionExpression", ruleName)) {
      {
        const __qin_typed_receiver_467: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_467.AsyncFunctionExpression();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("IdentifierReference", ruleName)) {
      {
        const __qin_typed_receiver_468: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_468.IdentifierReference(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Literal", ruleName)) {
      {
        const __qin_typed_receiver_469: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_469.Literal();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("GeneratorExpression", ruleName)) {
      {
        const __qin_typed_receiver_470: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_470.GeneratorExpression();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("FunctionExpression", ruleName)) {
      {
        const __qin_typed_receiver_471: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_471.FunctionExpression();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ClassExpression", ruleName)) {
      {
        const __qin_typed_receiver_472: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_472.ClassExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("RegularExpressionLiteral", ruleName)) {
      {
        const __qin_typed_receiver_473: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_473.RegularExpressionLiteral();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TemplateLiteral", ruleName)) {
      {
        const __qin_typed_receiver_474: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_474.TemplateLiteral(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CoverParenthesizedExpressionAndArrowParameterList", ruleName)) {
      {
        const __qin_typed_receiver_475: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_475.CoverParenthesizedExpressionAndArrowParameterList(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("LiteralPropertyName", ruleName)) {
      {
        const __qin_typed_receiver_476: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_476.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "LiteralPropertyName", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("StringLiteral", ruleName)) {
      {
        const __qin_typed_receiver_477: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_477.StringLiteral();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("NumericLiteral", ruleName)) {
      {
        const __qin_typed_receiver_478: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_478.NumericLiteral();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ComputedPropertyName", ruleName)) {
      {
        const __qin_typed_receiver_479: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_479.ComputedPropertyName(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Elision", ruleName)) {
      {
        const __qin_typed_receiver_480: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_480.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "Elision", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ArrayLiteral", ruleName)) {
      {
        const __qin_typed_receiver_481: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_481.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "ArrayLiteral", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ElementList", ruleName)) {
      {
        const __qin_typed_receiver_482: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_482.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "ElementList", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ObjectLiteral", ruleName)) {
      {
        const __qin_typed_receiver_483: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_483.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "ObjectLiteral", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("PropertyDefinitionList", ruleName)) {
      {
        const __qin_typed_receiver_484: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_484.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "PropertyDefinitionList", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("PropertyDefinition", ruleName)) {
      {
        const __qin_typed_receiver_485: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_485.PropertyDefinition(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("PropertyName", ruleName)) {
      {
        const __qin_typed_receiver_486: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_486.executeStaticRule(com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR, "PropertyName", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("SpreadElement", ruleName)) {
      {
        const __qin_typed_receiver_487: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_487.SpreadElement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CoverInitializedName", ruleName)) {
      {
        const __qin_typed_receiver_488: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_488.CoverInitializedName(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("MethodDefinition", ruleName)) {
      {
        const __qin_typed_receiver_489: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_489.MethodDefinition(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentExpression", ruleName)) {
      {
        const __qin_typed_receiver_490: com_slime_parser_expressions_SlimePrimaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_490.AssignmentExpression(this.__qin_field_effectiveParams.withIn(true));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported primary static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimePrimaryExpressionParser$PrimaryStaticRuntime = com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime;
com_slime_parser_expressions_SlimePrimaryExpressionParser.__qin_field_STATIC_PRIMARY_GRAMMAR = com_slime_parser_expressions_SlimePrimaryExpressionStaticGrammar.grammar();

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_expressions_SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime };
