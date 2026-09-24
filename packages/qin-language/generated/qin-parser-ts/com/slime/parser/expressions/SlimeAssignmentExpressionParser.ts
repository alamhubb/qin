import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "../../../subhuti/parser/SubhutiStaticGrammar.ts";
import { com_slime_parser_expressions_SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser as SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime as BinaryStaticRuntime } from "./SlimeBinaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimeAssignmentExpressionStaticGrammar, com_slime_parser_expressions_SlimeAssignmentExpressionStaticGrammar as SlimeAssignmentExpressionStaticGrammar } from "./SlimeAssignmentExpressionStaticGrammar.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../base/SlimeJavascriptParserBase.ts";
import { com_slime_parser_expressions_SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser as SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime as UnaryStaticRuntime } from "./SlimeUnaryExpressionParser.ts";
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
class com_slime_parser_expressions_SlimeAssignmentExpressionParser extends com_slime_parser_expressions_SlimeBinaryExpressionParser {
  static __qin_field_STATIC_ASSIGNMENT_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_expressions_SlimeAssignmentExpressionParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeAssignmentExpressionParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_expressions_SlimeAssignmentExpressionParser_1_0(sourceCode: string): void {
    null;
  }
  ConditionalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_ConditionalExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_ConditionalExpression.call(this, params);
    }), "ConditionalExpression", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ConditionalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_583: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_583.ShortCircuitExpression(params);
    }
    if (this.canStartConditionalTail()) {
      {
        const __qin_typed_receiver_584: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_584.Question();
      }
      {
        const __qin_typed_receiver_585: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_585.AssignmentExpression(params.withIn(true));
      }
      {
        const __qin_typed_receiver_586: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_586.Colon();
      }
      {
        const __qin_typed_receiver_587: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_587.AssignmentExpression(params);
      }
    }
    return null;
  }
  canStartConditionalTail(): boolean {
    return __QinJavaLangString.equals("Question", this.tokenNameAt(1.0));
  }
  AssignmentExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_AssignmentExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_AssignmentExpression.call(this, params);
    }), "AssignmentExpression", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_588: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_588.parseAssignmentExpressionBody(params);
    }
    return null;
  }
  parseAssignmentExpressionBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (this.canStartArrowFunctionHead()) {
      {
        const __qin_typed_receiver_589: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_589.ArrowFunction(params);
      }
      return null;
    }
    if (this.canStartAsyncArrowFunctionHead()) {
      {
        const __qin_typed_receiver_590: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_590.AsyncArrowFunction(params);
      }
      return null;
    }
    if (this.canStartYieldExpression(params)) {
      {
        const __qin_typed_receiver_591: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_591.YieldExpression(params);
      }
      return null;
    }
    if (this.hasTopLevelAssignmentOperatorAhead()) {
      let leftStartIndex: number = this.getCurrentIndex();
      {
        const __qin_typed_receiver_592: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_592.LeftHandSideExpression(params);
      }
      if ((this.isParserFail() || __qin_binary__("==", this.getCurrentIndex(), leftStartIndex))) {
        {
          const __qin_typed_receiver_593: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
          __qin_typed_receiver_593.setParseFail();
        }
        return null;
      }
      let operatorStartIndex: number = this.getCurrentIndex();
      {
        const __qin_typed_receiver_594: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_594.AssignmentOperatorAny();
      }
      if ((this.isParserFail() || __qin_binary__("==", this.getCurrentIndex(), operatorStartIndex))) {
        {
          const __qin_typed_receiver_595: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
          __qin_typed_receiver_595.setParseFail();
        }
        return null;
      }
      {
        const __qin_typed_receiver_596: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_596.parseAssignmentExpressionBody(params);
      }
      return null;
    }
    {
      const __qin_typed_receiver_597: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_597.ConditionalExpression(params);
    }
    return null;
  }
  AssignmentOperatorAny(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_AssignmentOperatorAny receiver=this arity=0 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_AssignmentOperatorAny.call(this);
    }), "AssignmentOperatorAny", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AssignmentOperatorAny(): void {
    {
      const __qin_typed_receiver_598: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_598.executeStaticRule(com_slime_parser_expressions_SlimeAssignmentExpressionParser.__qin_field_STATIC_ASSIGNMENT_GRAMMAR, "AssignmentOperatorAny", this.assignmentStaticRuntime());
    }
    return null;
  }
  AssignmentOperator(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_AssignmentOperator receiver=this arity=0 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_AssignmentOperator.call(this);
    }), "AssignmentOperator", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AssignmentOperator(): void {
    {
      const __qin_typed_receiver_599: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_599.executeStaticRule(com_slime_parser_expressions_SlimeAssignmentExpressionParser.__qin_field_STATIC_ASSIGNMENT_GRAMMAR, "AssignmentOperator", this.assignmentStaticRuntime());
    }
    return null;
  }
  assignmentStaticRuntime(...__qin_args: any[]): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    if (__qin_args.length === 0 && true) return this.__qin_overload_assignmentStaticRuntime_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_assignmentStaticRuntime_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: assignmentStaticRuntime/" + __qin_args.length);
  }
  __qin_overload_assignmentStaticRuntime_0_0(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime(this);
  }
  __qin_overload_assignmentStaticRuntime_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime(this, params);
  }
  YieldExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_YieldExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_YieldExpression.call(this, params);
    }), "YieldExpression", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_YieldExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_600: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_600.Yield();
    }
    if (this.lookaheadHasLineBreak()) {
      return null;
    }
    let yieldParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams = params.withYield(true);
    if (__QinJavaLangString.equals("Asterisk", this.tokenNameAt(1.0))) {
      {
        const __qin_typed_receiver_601: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_601.Asterisk();
      }
      {
        const __qin_typed_receiver_602: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_602.AssignmentExpression(yieldParams);
      }
      return null;
    }
    if (this.canStartAssignmentExpression(yieldParams, 1.0)) {
      {
        const __qin_typed_receiver_603: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_603.AssignmentExpression(yieldParams);
      }
    }
    return null;
  }
  ArrowFunction(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_ArrowFunction receiver=this arity=1 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_ArrowFunction.call(this, params);
    }), "ArrowFunction", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrowFunction(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_604: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_604.ArrowParameters(params);
    }
    {
      const __qin_typed_receiver_605: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_605.assertNoLineBreak();
    }
    {
      const __qin_typed_receiver_606: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_606.Arrow();
    }
    {
      const __qin_typed_receiver_607: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_607.ConciseBody(params);
    }
    return null;
  }
  ArrowParameters(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_ArrowParameters receiver=this arity=1 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_ArrowParameters.call(this, params);
    }), "ArrowParameters", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrowParameters(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_608: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_608.executeStaticRule(com_slime_parser_expressions_SlimeAssignmentExpressionParser.__qin_field_STATIC_ASSIGNMENT_GRAMMAR, "ArrowParameters", this.assignmentStaticRuntime(params));
    }
    return null;
  }
  ArrowFormalParameters(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_ArrowFormalParameters receiver=this arity=1 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_ArrowFormalParameters.call(this, params);
    }), "ArrowFormalParameters", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrowFormalParameters(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_609: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_609.LParen();
    }
    {
      const __qin_typed_receiver_610: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_610.UniqueFormalParameters(params);
    }
    {
      const __qin_typed_receiver_611: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_611.RParen();
    }
    return null;
  }
  AsyncArrowHead(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_AsyncArrowHead receiver=this arity=0 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_AsyncArrowHead.call(this);
    }), "AsyncArrowHead", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncArrowHead(): void {
    {
      const __qin_typed_receiver_612: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_612.consumeIdentifierValue("async");
    }
    {
      const __qin_typed_receiver_613: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_613.assertNoLineBreak();
    }
    {
      const __qin_typed_receiver_614: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_614.ArrowFormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, true));
    }
    return null;
  }
  ConciseBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_ConciseBody receiver=this arity=1 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_ConciseBody.call(this, params);
    }), "ConciseBody", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ConciseBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_615: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_615.executeStaticRule(com_slime_parser_expressions_SlimeAssignmentExpressionParser.__qin_field_STATIC_ASSIGNMENT_GRAMMAR, "ConciseBody", this.assignmentStaticRuntime(params.withAwait(false)));
    }
    return null;
  }
  ExpressionBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_ExpressionBody receiver=this arity=1 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_ExpressionBody.call(this, params);
    }), "ExpressionBody", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ExpressionBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_616: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_616.AssignmentExpression(params.withYield(false));
    }
    return null;
  }
  AsyncArrowFunction(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_AsyncArrowFunction receiver=this arity=1 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_AsyncArrowFunction.call(this, params);
    }), "AsyncArrowFunction", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncArrowFunction(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (this.canStartBaseAsyncArrowBindingIdentifierForm()) {
      {
        const __qin_typed_receiver_617: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_617.consumeIdentifierValue("async");
      }
      {
        const __qin_typed_receiver_618: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_618.assertNoLineBreak();
      }
      {
        const __qin_typed_receiver_619: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_619.AsyncArrowBindingIdentifier(params);
      }
    } else {
      {
        const __qin_typed_receiver_620: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_620.CoverCallExpressionAndAsyncArrowHead(params);
      }
    }
    {
      const __qin_typed_receiver_621: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_621.assertNoLineBreak();
    }
    {
      const __qin_typed_receiver_622: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_622.Arrow();
    }
    {
      const __qin_typed_receiver_623: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_623.AsyncConciseBody(params);
    }
    return null;
  }
  canStartBaseAsyncArrowBindingIdentifierForm(): boolean {
    if ((!this.matchIdentifierValue("async"))) {
      return false;
    }
    let identifierToken: com_subhuti_struct_SubhutiMatchToken = this.LA(2.0);
    return (__qin_binary__("!=", identifierToken, null) && !identifierToken.hasLineBreakBefore() && __QinJavaLangString.equals("IdentifierName", identifierToken.tokenName()));
  }
  AsyncArrowBindingIdentifier(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_AsyncArrowBindingIdentifier receiver=this arity=1 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_AsyncArrowBindingIdentifier.call(this, params);
    }), "AsyncArrowBindingIdentifier", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncArrowBindingIdentifier(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_624: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_624.BindingIdentifier(params);
    }
    return null;
  }
  AsyncConciseBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_AsyncConciseBody receiver=this arity=1 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_AsyncConciseBody.call(this, params);
    }), "AsyncConciseBody", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncConciseBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (__QinJavaLangString.equals("LBrace", this.tokenNameAt(1.0))) {
      {
        const __qin_typed_receiver_625: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_625.LBrace();
      }
      {
        const __qin_typed_receiver_626: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_626.AsyncFunctionBody();
      }
      {
        const __qin_typed_receiver_627: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_627.RBrace();
      }
      return null;
    }
    {
      const __qin_typed_receiver_628: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_628.ExpressionBody(params.withAwait(true));
    }
    return null;
  }
  Expression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeAssignmentExpressionParser method=__qin_subhuti_raw_Expression receiver=this arity=1 */ com_slime_parser_expressions_SlimeAssignmentExpressionParser.prototype.__qin_subhuti_raw_Expression.call(this, params);
    }), "Expression", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Expression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_629: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_629.parseExpressionBody(params);
    }
    return null;
  }
  parseExpressionBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_630: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
      __qin_typed_receiver_630.AssignmentExpression(params);
    }
    while ((!this.isParserFail() && __QinJavaLangString.equals("Comma", this.tokenNameAt(1.0)))) {
      {
        const __qin_typed_receiver_631: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_631.Comma();
      }
      {
        const __qin_typed_receiver_632: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this;
        __qin_typed_receiver_632.AssignmentExpression(params);
      }
    }
    return null;
  }
  canStartAssignmentExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params);
    if ((__qin_binary__("==", lookaheadOffset, 1.0) && (this.canStartArrowFunctionHead() || this.canStartAsyncArrowFunctionHead()))) {
      return true;
    }
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    if ((effectiveParams.yield() && __QinJavaLangString.equals("Yield", tokenName))) {
      return true;
    }
    if ((__QinJavaLangString.equals("Delete", tokenName) || __QinJavaLangString.equals("Void", tokenName) || __QinJavaLangString.equals("Typeof", tokenName) || __QinJavaLangString.equals("Plus", tokenName) || __QinJavaLangString.equals("Minus", tokenName) || __QinJavaLangString.equals("Increment", tokenName) || __QinJavaLangString.equals("Decrement", tokenName) || __QinJavaLangString.equals("BitwiseNot", tokenName) || __QinJavaLangString.equals("LogicalNot", tokenName))) {
      return true;
    }
    if ((effectiveParams.await() && __QinJavaLangString.equals("Await", tokenName))) {
      return true;
    }
    if ((__QinJavaLangString.equals("This", tokenName) || __QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("NullLiteral", tokenName) || __QinJavaLangString.equals("True", tokenName) || __QinJavaLangString.equals("False", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("Function", tokenName) || __QinJavaLangString.equals("Class", tokenName) || __QinJavaLangString.equals("LBracket", tokenName) || __QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("RegularExpressionLiteral", tokenName) || __QinJavaLangString.equals("NoSubstitutionTemplate", tokenName) || __QinJavaLangString.equals("TemplateHead", tokenName) || __QinJavaLangString.equals("LParen", tokenName) || __QinJavaLangString.equals("Super", tokenName) || __QinJavaLangString.equals("Import", tokenName) || __QinJavaLangString.equals("New", tokenName))) {
      return true;
    }
    return ((__QinJavaLangString.equals("Yield", tokenName) && !effectiveParams.yield()) || (__QinJavaLangString.equals("Await", tokenName) && !effectiveParams.await()));
  }
  canStartAssignmentBindingIdentifier(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("IdentifierName", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Yield", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Await", this.tokenNameAt(lookaheadOffset)));
  }
}
const SlimeAssignmentExpressionParser = com_slime_parser_expressions_SlimeAssignmentExpressionParser;
class com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime {
  __qin_field_parser: com_slime_parser_expressions_SlimeAssignmentExpressionParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_expressions_SlimeAssignmentExpressionParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime_1_0(parser: com_slime_parser_expressions_SlimeAssignmentExpressionParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__QinJavaLangString.equals("AssignmentOperator", ruleName)) {
      let tokenName: string = this.__qin_field_parser.tokenNameAt(lookaheadOffset);
      return (__QinJavaLangString.equals("MultiplyAssign", tokenName) || __QinJavaLangString.equals("DivideAssign", tokenName) || __QinJavaLangString.equals("ModuloAssign", tokenName) || __QinJavaLangString.equals("PlusAssign", tokenName) || __QinJavaLangString.equals("MinusAssign", tokenName) || __QinJavaLangString.equals("LeftShiftAssign", tokenName) || __QinJavaLangString.equals("RightShiftAssign", tokenName) || __QinJavaLangString.equals("UnsignedRightShiftAssign", tokenName) || __QinJavaLangString.equals("BitwiseAndAssign", tokenName) || __QinJavaLangString.equals("BitwiseXorAssign", tokenName) || __QinJavaLangString.equals("BitwiseOrAssign", tokenName) || __QinJavaLangString.equals("ExponentiationAssign", tokenName));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported assignment static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__QinJavaLangString.equals("AssignmentOperator", ruleName)) {
      {
        const __qin_typed_receiver_633: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_633.executeStaticRule(com_slime_parser_expressions_SlimeAssignmentExpressionParser.__qin_field_STATIC_ASSIGNMENT_GRAMMAR, "AssignmentOperator", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported assignment static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime = com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime;
class com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime {
  __qin_field_parser: com_slime_parser_expressions_SlimeAssignmentExpressionParser | null = null as any;
  __qin_field_params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_expressions_SlimeAssignmentExpressionParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const params: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime_2_0(parser, params);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime_2_0(parser: com_slime_parser_expressions_SlimeAssignmentExpressionParser, params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_params = null;
    this.__qin_field_parser = parser;
    this.__qin_field_params = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params);
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
    if (__QinJavaLangString.equals("AssignmentExpression", ruleName)) {
      return this.__qin_field_parser.canStartAssignmentExpression(this.__qin_field_params, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      return this.__qin_field_parser.canStartAssignmentBindingIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("ArrowFormalParameters", ruleName)) {
      return __QinJavaLangString.equals("LParen", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("CoverParenthesizedExpressionAndArrowParameterList", ruleName)) {
      return __QinJavaLangString.equals("LParen", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("FunctionBody", ruleName)) {
      return true;
    }
    if (__QinJavaLangString.equals("ExpressionBody", ruleName)) {
      return this.__qin_field_parser.canStartAssignmentExpression(this.__qin_field_params.withYield(false), lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported assignment expression static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeAssignmentExpressionStaticGrammar.__qin_field_GATE_NOT_LBRACE, gateId)) {
      return (!__QinJavaLangString.equals("LBrace", this.__qin_field_parser.tokenNameAt(1.0)));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported assignment expression static gate: " + gateId));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__QinJavaLangString.equals("AssignmentExpression", ruleName)) {
      {
        const __qin_typed_receiver_634: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_634.parseAssignmentExpressionBody(this.__qin_field_params);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      {
        const __qin_typed_receiver_635: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_635.BindingIdentifier(this.__qin_field_params);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ArrowFormalParameters", ruleName)) {
      {
        const __qin_typed_receiver_636: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_636.ArrowFormalParameters(this.__qin_field_params);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CoverParenthesizedExpressionAndArrowParameterList", ruleName)) {
      {
        const __qin_typed_receiver_637: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_637.CoverParenthesizedExpressionAndArrowParameterList(this.__qin_field_params);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("FunctionBody", ruleName)) {
      {
        const __qin_typed_receiver_638: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_638.FunctionBody();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ExpressionBody", ruleName)) {
      {
        const __qin_typed_receiver_639: com_slime_parser_expressions_SlimeAssignmentExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_639.ExpressionBody(this.__qin_field_params.withYield(false));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported assignment expression static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime = com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime;
com_slime_parser_expressions_SlimeAssignmentExpressionParser.__qin_field_STATIC_ASSIGNMENT_GRAMMAR = com_slime_parser_expressions_SlimeAssignmentExpressionStaticGrammar.grammar();

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_expressions_SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime };
