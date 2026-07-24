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
  ConditionalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ConditionalExpression(params);
    }), "ConditionalExpression", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ConditionalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.ShortCircuitExpression(params);
    if (this.canStartConditionalTail()) {
      this.__qin_field_tokenConsumer.Question();
      this.AssignmentExpression(params.withIn(true));
      this.__qin_field_tokenConsumer.Colon();
      this.AssignmentExpression(params);
    }
    return null;
  }
  canStartConditionalTail(): any {
    return __QinJavaLangString.equals("Question", this.tokenNameAt(1.0));
  }
  AssignmentExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentExpression(params);
    }), "AssignmentExpression", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.parseAssignmentExpressionBody(params);
    return null;
  }
  parseAssignmentExpressionBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    if (this.canStartArrowFunctionHead()) {
      this.ArrowFunction(params);
      return null;
    }
    if (this.canStartAsyncArrowFunctionHead()) {
      this.AsyncArrowFunction(params);
      return null;
    }
    if (this.canStartYieldExpression(params)) {
      this.YieldExpression(params);
      return null;
    }
    if (this.hasTopLevelAssignmentOperatorAhead()) {
      let leftStartIndex: any = this.getCurrentIndex();
      this.LeftHandSideExpression(params);
      if ((this.isParserFail() || __qin_binary__("==", this.getCurrentIndex(), leftStartIndex))) {
        this.setParseFail();
        return null;
      }
      let operatorStartIndex: any = this.getCurrentIndex();
      this.AssignmentOperatorAny();
      if ((this.isParserFail() || __qin_binary__("==", this.getCurrentIndex(), operatorStartIndex))) {
        this.setParseFail();
        return null;
      }
      this.parseAssignmentExpressionBody(params);
      return null;
    }
    this.ConditionalExpression(params);
    return null;
  }
  AssignmentOperatorAny(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentOperatorAny();
    }), "AssignmentOperatorAny", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AssignmentOperatorAny(): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeAssignmentExpressionParser.__qin_field_STATIC_ASSIGNMENT_GRAMMAR, "AssignmentOperatorAny", this.assignmentStaticRuntime());
    return null;
  }
  AssignmentOperator(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentOperator();
    }), "AssignmentOperator", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AssignmentOperator(): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeAssignmentExpressionParser.__qin_field_STATIC_ASSIGNMENT_GRAMMAR, "AssignmentOperator", this.assignmentStaticRuntime());
    return null;
  }
  assignmentStaticRuntime(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_assignmentStaticRuntime_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_assignmentStaticRuntime_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: assignmentStaticRuntime/" + __qin_args.length);
  }
  __qin_overload_assignmentStaticRuntime_0_0(): any {
    return new com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime(this);
  }
  __qin_overload_assignmentStaticRuntime_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return new com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime(this, params);
  }
  YieldExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_YieldExpression(params);
    }), "YieldExpression", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_YieldExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.__qin_field_tokenConsumer.Yield();
    if (this.lookaheadHasLineBreak()) {
      return null;
    }
    let yieldParams: any = params.withYield(true);
    if (__QinJavaLangString.equals("Asterisk", this.tokenNameAt(1.0))) {
      this.__qin_field_tokenConsumer.Asterisk();
      this.AssignmentExpression(yieldParams);
      return null;
    }
    if (this.canStartAssignmentExpression(yieldParams, 1.0)) {
      this.AssignmentExpression(yieldParams);
    }
    return null;
  }
  ArrowFunction(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArrowFunction(params);
    }), "ArrowFunction", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrowFunction(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.ArrowParameters(params);
    this.assertNoLineBreak();
    this.__qin_field_tokenConsumer.Arrow();
    this.ConciseBody(params);
    return null;
  }
  ArrowParameters(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArrowParameters(params);
    }), "ArrowParameters", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrowParameters(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeAssignmentExpressionParser.__qin_field_STATIC_ASSIGNMENT_GRAMMAR, "ArrowParameters", this.assignmentStaticRuntime(params));
    return null;
  }
  ArrowFormalParameters(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArrowFormalParameters(params);
    }), "ArrowFormalParameters", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrowFormalParameters(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.__qin_field_tokenConsumer.LParen();
    this.UniqueFormalParameters(params);
    this.__qin_field_tokenConsumer.RParen();
    return null;
  }
  AsyncArrowHead(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncArrowHead();
    }), "AsyncArrowHead", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncArrowHead(): any {
    this.consumeIdentifierValue("async");
    this.assertNoLineBreak();
    this.ArrowFormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, true));
    return null;
  }
  ConciseBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ConciseBody(params);
    }), "ConciseBody", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ConciseBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeAssignmentExpressionParser.__qin_field_STATIC_ASSIGNMENT_GRAMMAR, "ConciseBody", this.assignmentStaticRuntime(params.withAwait(false)));
    return null;
  }
  ExpressionBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ExpressionBody(params);
    }), "ExpressionBody", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ExpressionBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.AssignmentExpression(params.withYield(false));
    return null;
  }
  AsyncArrowFunction(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncArrowFunction(params);
    }), "AsyncArrowFunction", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncArrowFunction(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    if (this.canStartBaseAsyncArrowBindingIdentifierForm()) {
      this.consumeIdentifierValue("async");
      this.assertNoLineBreak();
      this.AsyncArrowBindingIdentifier(params);
    } else {
      this.CoverCallExpressionAndAsyncArrowHead(params);
    }
    this.assertNoLineBreak();
    this.__qin_field_tokenConsumer.Arrow();
    this.AsyncConciseBody(params);
    return null;
  }
  canStartBaseAsyncArrowBindingIdentifierForm(): any {
    if ((!this.matchIdentifierValue("async"))) {
      return false;
    }
    let identifierToken: any = this.LA(2.0);
    return (__qin_binary__("!=", identifierToken, null) && !identifierToken.hasLineBreakBefore() && __QinJavaLangString.equals("IdentifierName", identifierToken.tokenName()));
  }
  AsyncArrowBindingIdentifier(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncArrowBindingIdentifier(params);
    }), "AsyncArrowBindingIdentifier", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncArrowBindingIdentifier(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.BindingIdentifier(params);
    return null;
  }
  AsyncConciseBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncConciseBody(params);
    }), "AsyncConciseBody", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncConciseBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    if (__QinJavaLangString.equals("LBrace", this.tokenNameAt(1.0))) {
      this.__qin_field_tokenConsumer.LBrace();
      this.AsyncFunctionBody();
      this.__qin_field_tokenConsumer.RBrace();
      return null;
    }
    this.ExpressionBody(params.withAwait(true));
    return null;
  }
  Expression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Expression(params);
    }), "Expression", "SlimeAssignmentExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Expression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.parseExpressionBody(params);
    return null;
  }
  parseExpressionBody(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.parseAssignmentExpressionBody(params);
    while ((!this.isParserFail() && __QinJavaLangString.equals("Comma", this.tokenNameAt(1.0)))) {
      this.__qin_field_tokenConsumer.Comma();
      this.parseAssignmentExpressionBody(params);
    }
    return null;
  }
  canStartAssignmentExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    let effectiveParams: any = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params);
    if ((__qin_binary__("==", lookaheadOffset, 1.0) && (this.canStartArrowFunctionHead() || this.canStartAsyncArrowFunctionHead()))) {
      return true;
    }
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    if ((effectiveParams.__qin_yield() && __QinJavaLangString.equals("Yield", tokenName))) {
      return true;
    }
    if ((__QinJavaLangString.equals("Delete", tokenName) || __QinJavaLangString.equals("Void", tokenName) || __QinJavaLangString.equals("Typeof", tokenName) || __QinJavaLangString.equals("Plus", tokenName) || __QinJavaLangString.equals("Minus", tokenName) || __QinJavaLangString.equals("BitwiseNot", tokenName) || __QinJavaLangString.equals("LogicalNot", tokenName))) {
      return true;
    }
    if ((effectiveParams.__qin_await() && __QinJavaLangString.equals("Await", tokenName))) {
      return true;
    }
    if ((__QinJavaLangString.equals("This", tokenName) || __QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("NullLiteral", tokenName) || __QinJavaLangString.equals("True", tokenName) || __QinJavaLangString.equals("False", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("Function", tokenName) || __QinJavaLangString.equals("Class", tokenName) || __QinJavaLangString.equals("LBracket", tokenName) || __QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("RegularExpressionLiteral", tokenName) || __QinJavaLangString.equals("NoSubstitutionTemplate", tokenName) || __QinJavaLangString.equals("TemplateHead", tokenName) || __QinJavaLangString.equals("LParen", tokenName) || __QinJavaLangString.equals("Super", tokenName) || __QinJavaLangString.equals("Import", tokenName) || __QinJavaLangString.equals("New", tokenName))) {
      return true;
    }
    return ((__QinJavaLangString.equals("Yield", tokenName) && !effectiveParams.__qin_yield()) || (__QinJavaLangString.equals("Await", tokenName) && !effectiveParams.__qin_await()));
  }
  canStartAssignmentBindingIdentifier(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("IdentifierName", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Yield", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Await", this.tokenNameAt(lookaheadOffset)));
  }
  UniqueFormalParameters(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    throw new Error("Abstract Java method is not implemented: UniqueFormalParameters");
  }
  FunctionBody(): any {
    throw new Error("Abstract Java method is not implemented: FunctionBody");
  }
  AsyncFunctionBody(): any {
    throw new Error("Abstract Java method is not implemented: AsyncFunctionBody");
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
    if (__QinJavaLangString.equals("AssignmentOperator", ruleName)) {
      let tokenName: any = this.__qin_field_parser.tokenNameAt(lookaheadOffset);
      return (__QinJavaLangString.equals("MultiplyAssign", tokenName) || __QinJavaLangString.equals("DivideAssign", tokenName) || __QinJavaLangString.equals("ModuloAssign", tokenName) || __QinJavaLangString.equals("PlusAssign", tokenName) || __QinJavaLangString.equals("MinusAssign", tokenName) || __QinJavaLangString.equals("LeftShiftAssign", tokenName) || __QinJavaLangString.equals("RightShiftAssign", tokenName) || __QinJavaLangString.equals("UnsignedRightShiftAssign", tokenName) || __QinJavaLangString.equals("BitwiseAndAssign", tokenName) || __QinJavaLangString.equals("BitwiseXorAssign", tokenName) || __QinJavaLangString.equals("BitwiseOrAssign", tokenName) || __QinJavaLangString.equals("ExponentiationAssign", tokenName));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported assignment static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__QinJavaLangString.equals("AssignmentOperator", ruleName)) {
      this.__qin_field_parser.executeStaticRule(com_slime_parser_expressions_SlimeAssignmentExpressionParser.__qin_field_STATIC_ASSIGNMENT_GRAMMAR, "AssignmentOperator", this);
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  testStaticGate(gateId: string): any {
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeAssignmentExpressionStaticGrammar.__qin_field_GATE_NOT_LBRACE, gateId)) {
      return (!__QinJavaLangString.equals("LBrace", this.__qin_field_parser.tokenNameAt(1.0)));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported assignment expression static gate: " + gateId));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__QinJavaLangString.equals("AssignmentExpression", ruleName)) {
      this.__qin_field_parser.parseAssignmentExpressionBody(this.__qin_field_params);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      this.__qin_field_parser.BindingIdentifier(this.__qin_field_params);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ArrowFormalParameters", ruleName)) {
      this.__qin_field_parser.ArrowFormalParameters(this.__qin_field_params);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CoverParenthesizedExpressionAndArrowParameterList", ruleName)) {
      this.__qin_field_parser.CoverParenthesizedExpressionAndArrowParameterList(this.__qin_field_params);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("FunctionBody", ruleName)) {
      this.__qin_field_parser.FunctionBody();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ExpressionBody", ruleName)) {
      this.__qin_field_parser.ExpressionBody(this.__qin_field_params.withYield(false));
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
