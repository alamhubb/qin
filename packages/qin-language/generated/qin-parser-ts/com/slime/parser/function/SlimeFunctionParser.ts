import { com_slime_parser_statements_SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser as SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime as StatementRootStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime as StatementLoopStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime as StatementTryStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime as StatementIfStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime as StatementVariableStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime as StatementListStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime as StatementJumpStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime as StatementBranchStaticRuntime } from "../statements/SlimeStatementParser.ts";
import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "../../../subhuti/parser/SubhutiStaticGrammar.ts";
import { com_slime_parser_function_SlimeFunctionStaticGrammar, com_slime_parser_function_SlimeFunctionStaticGrammar as SlimeFunctionStaticGrammar } from "./SlimeFunctionStaticGrammar.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../base/SlimeJavascriptParserBase.ts";
import { com_slime_parser_expressions_SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser as SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime as AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime as AssignmentExpressionStaticRuntime } from "../expressions/SlimeAssignmentExpressionParser.ts";
import { com_slime_parser_expressions_SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser as SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime as BinaryStaticRuntime } from "../expressions/SlimeBinaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser as SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime as UnaryStaticRuntime } from "../expressions/SlimeUnaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser as SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime as PrimaryStaticRuntime } from "../expressions/SlimePrimaryExpressionParser.ts";
import { com_slime_parser_literal_SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser as SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime as LiteralStaticRuntime } from "../literal/SlimeLiteralParser.ts";
import { com_slime_parser_identifier_SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser as SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime as IdentifierStaticRuntime } from "../identifier/SlimeIdentifierParser.ts";
import { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser as SubhutiParser, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime as StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticPrefix, com_subhuti_parser_SubhutiParser$StaticPrefix as StaticPrefix, com_subhuti_parser_SubhutiParser$StaticChoice, com_subhuti_parser_SubhutiParser$StaticChoice as StaticChoice } from "../../../subhuti/parser/SubhutiParser.ts";
import { com_subhuti_parser_SubhutiParserFinal, com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal } from "../../../subhuti/parser/SubhutiParserFinal.ts";
import { com_subhuti_parser_SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators as SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl as StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher as StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext as AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext as AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes as PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates as StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame as ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames as CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys as CurrentTokenKeys } from "../../../subhuti/parser/SubhutiParserCombinators.ts";
import { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore as SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments as StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult as RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode as StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks as StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$CacheWork, com_subhuti_parser_SubhutiParserCore$CacheWork as CacheWork, com_subhuti_parser_SubhutiParserCore$FailureWork, com_subhuti_parser_SubhutiParserCore$FailureWork as FailureWork } from "../../../subhuti/parser/SubhutiParserCore.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations as ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException as SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException as SubhutiFirstTokenUnknownException } from "../../../subhuti/parser/SubhutiParserState.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../../../subhuti/lookahead/SubhutiTokenLookahead.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __QinJavaLangNumber, __qin_java_functional } from "@qin/java-sdk-js";
import { __qin_subhuti_rule_cache_key } from "@qin/java-sdk-js/tooling";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const UnsupportedOperationException = __QinJavaLangUnsupportedOperationException;
class com_slime_parser_function_SlimeFunctionParser extends com_slime_parser_statements_SlimeStatementParser {
  static __qin_field_STATIC_FUNCTION_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_function_SlimeFunctionParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeFunctionParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_function_SlimeFunctionParser_1_0(sourceCode: string): void {
    null;
  }
  FunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_FunctionDeclaration receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_FunctionDeclaration.call(this, params);
    }), "FunctionDeclaration", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_FunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_805: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_805.Function();
    }
    {
      const __qin_typed_receiver_806: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_806.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, this.functionBindingRule(params), this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await())));
    }
    if (this.isParserFail()) {
      return null;
    }
    {
      const __qin_typed_receiver_807: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_807.LParen();
    }
    {
      const __qin_typed_receiver_808: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_808.FormalParameters();
    }
    {
      const __qin_typed_receiver_809: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_809.RParen();
    }
    {
      const __qin_typed_receiver_810: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_810.LBrace();
    }
    {
      const __qin_typed_receiver_811: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_811.FunctionBody();
    }
    {
      const __qin_typed_receiver_812: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_812.RBrace();
    }
    return null;
  }
  FunctionExpression(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_FunctionExpression receiver=this arity=0 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_FunctionExpression.call(this);
    }), "FunctionExpression", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_FunctionExpression(): void {
    {
      const __qin_typed_receiver_813: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_813.Function();
    }
    {
      const __qin_typed_receiver_814: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_814.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "OptionalFunctionBindingIdentifier", this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, false)));
    }
    {
      const __qin_typed_receiver_815: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_815.LParen();
    }
    {
      const __qin_typed_receiver_816: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_816.FormalParameters();
    }
    {
      const __qin_typed_receiver_817: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_817.RParen();
    }
    {
      const __qin_typed_receiver_818: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_818.LBrace();
    }
    {
      const __qin_typed_receiver_819: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_819.FunctionBody();
    }
    {
      const __qin_typed_receiver_820: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_820.RBrace();
    }
    return null;
  }
  FormalParameters(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FormalParameters_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FormalParameters_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FormalParameters/" + __qin_args.length);
  }
  __qin_overload_FormalParameters_0_0(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_FormalParameters_0_0 receiver=this arity=0 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_FormalParameters_0_0.call(this);
    }), "FormalParameters", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameters_0_0(): void {
    {
      const __qin_typed_receiver_821: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_821.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FormalParameters", this.functionStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    }
    return null;
  }
  __qin_overload_FormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_FormalParameters_1_1 receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_FormalParameters_1_1.call(this, params);
    }), "FormalParameters", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_822: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_822.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FormalParameters", com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_PARAMS_VARIANT_ID, this.functionStaticRuntime(params));
    }
    return null;
  }
  UniqueFormalParameters(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_UniqueFormalParameters_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_UniqueFormalParameters_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: UniqueFormalParameters/" + __qin_args.length);
  }
  __qin_overload_UniqueFormalParameters_0_0(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_UniqueFormalParameters_0_0 receiver=this arity=0 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_UniqueFormalParameters_0_0.call(this);
    }), "UniqueFormalParameters", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_UniqueFormalParameters_0_0(): void {
    {
      const __qin_typed_receiver_823: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_823.FormalParameters();
    }
    return null;
  }
  __qin_overload_UniqueFormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_UniqueFormalParameters_1_1 receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_UniqueFormalParameters_1_1.call(this, params);
    }), "UniqueFormalParameters", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_UniqueFormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_824: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_824.FormalParameters(params);
    }
    return null;
  }
  FormalParameterList(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FormalParameterList_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FormalParameterList_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FormalParameterList/" + __qin_args.length);
  }
  __qin_overload_FormalParameterList_0_0(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_FormalParameterList_0_0 receiver=this arity=0 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_FormalParameterList_0_0.call(this);
    }), "FormalParameterList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameterList_0_0(): void {
    {
      const __qin_typed_receiver_825: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_825.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FormalParameterList", this.functionStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    }
    return null;
  }
  __qin_overload_FormalParameterList_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_FormalParameterList_1_1 receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_FormalParameterList_1_1.call(this, params);
    }), "FormalParameterList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameterList_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_826: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_826.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FormalParameterList", com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_PARAMS_VARIANT_ID, this.functionStaticRuntime(params));
    }
    return null;
  }
  FunctionRestParameter(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FunctionRestParameter_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FunctionRestParameter_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FunctionRestParameter/" + __qin_args.length);
  }
  __qin_overload_FunctionRestParameter_0_0(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_FunctionRestParameter_0_0 receiver=this arity=0 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_FunctionRestParameter_0_0.call(this);
    }), "FunctionRestParameter", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FunctionRestParameter_0_0(): void {
    {
      const __qin_typed_receiver_827: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_827.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FunctionRestParameter", this.functionStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    }
    return null;
  }
  __qin_overload_FunctionRestParameter_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_FunctionRestParameter_1_1 receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_FunctionRestParameter_1_1.call(this, params);
    }), "FunctionRestParameter", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FunctionRestParameter_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_828: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_828.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FunctionRestParameter", com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_PARAMS_VARIANT_ID, this.functionStaticRuntime(params));
    }
    return null;
  }
  functionStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime(this, params);
  }
  functionStatementParams(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): com_slime_parser_base_SlimeJavascriptParserBase$StatementParams {
    return new com_slime_parser_base_SlimeJavascriptParserBase$StatementParams((__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params).yield(), (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params).await(), true);
  }
  canStartFormalParameter(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartFormalParameter_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartFormalParameter_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartFormalParameter/" + __qin_args.length);
  }
  __qin_overload_canStartFormalParameter_0_0(): boolean {
    return this.canStartFormalParameter(1.0);
  }
  __qin_overload_canStartFormalParameter_1_1(lookaheadOffset: number): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    return (() => {
      const __switch_discriminant = token.tokenName();
      if (__switch_discriminant === "IdentifierName" || __switch_discriminant === "Yield" || __switch_discriminant === "Await" || __switch_discriminant === "LBrace" || __switch_discriminant === "LBracket") {
        return true;
      }
      else {
        return false;
      }
      return null;
    })();
  }
  canStartBindingRestElement(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartBindingRestElement_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartBindingRestElement_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartBindingRestElement/" + __qin_args.length);
  }
  __qin_overload_canStartBindingRestElement_0_0(): boolean {
    return this.canStartBindingRestElement(1.0);
  }
  __qin_overload_canStartBindingRestElement_1_1(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("Ellipsis", this.LA(lookaheadOffset).tokenName()));
  }
  canStartBindingRestProperty(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("Ellipsis", this.LA(lookaheadOffset).tokenName()));
  }
  canStartBindingPattern(lookaheadOffset: number): boolean {
    return (this.canStartObjectBindingPattern(lookaheadOffset) || this.canStartArrayBindingPattern(lookaheadOffset));
  }
  canStartObjectBindingPattern(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("LBrace", this.LA(lookaheadOffset).tokenName()));
  }
  canStartArrayBindingPattern(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("LBracket", this.LA(lookaheadOffset).tokenName()));
  }
  canStartBindingIdentifier(lookaheadOffset: number): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    return (() => {
      const __switch_discriminant = token.tokenName();
      if (__switch_discriminant === "IdentifierName" || __switch_discriminant === "Yield" || __switch_discriminant === "Await") {
        return true;
      }
      else {
        return false;
      }
      return null;
    })();
  }
  canStartInitializer(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("Assign", this.LA(lookaheadOffset).tokenName()));
  }
  canStartPropertyNameLocal(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("IdentifierName", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Yield", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Await", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("StringLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("NumericLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBracket", this.tokenNameAt(lookaheadOffset)));
  }
  canStartBindingProperty(lookaheadOffset: number): boolean {
    return (this.canStartPropertyNameLocal(lookaheadOffset) || this.canStartBindingIdentifier(lookaheadOffset));
  }
  canSelectPropertyNameBranch(lookaheadOffset: number): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__QinJavaLangString.equals("LBracket", tokenName)) {
      return true;
    }
    if ((__QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName))) {
      return true;
    }
    if ((__QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("Yield", tokenName) || __QinJavaLangString.equals("Await", tokenName))) {
      return __QinJavaLangString.equals("Colon", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0)));
    }
    return false;
  }
  canSelectBindingShorthandProperty(lookaheadOffset: number): boolean {
    return (this.canStartBindingIdentifier(lookaheadOffset) && !__QinJavaLangString.equals("Colon", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartBindingElisionElement(lookaheadOffset: number): boolean {
    let offset: number = lookaheadOffset;
    while (__QinJavaLangString.equals("Comma", this.tokenNameAt(offset))) {
      offset++;
    }
    return this.canStartBindingElement(offset);
  }
  canStartBindingElement(lookaheadOffset: number): boolean {
    return (this.canStartBindingIdentifier(lookaheadOffset) || this.canStartBindingPattern(lookaheadOffset));
  }
  canSelectArrayBindingRestOrElisionOnly(lookaheadOffset: number): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__QinJavaLangString.equals("Ellipsis", tokenName)) {
      return true;
    }
    if ((!__QinJavaLangString.equals("Comma", tokenName))) {
      return false;
    }
    let offset: number = lookaheadOffset;
    while (__QinJavaLangString.equals("Comma", this.tokenNameAt(offset))) {
      offset++;
    }
    let afterElision: string = this.tokenNameAt(offset);
    return (__QinJavaLangString.equals("RBracket", afterElision) || __QinJavaLangString.equals("Ellipsis", afterElision));
  }
  canStartAssignmentProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    return (this.canStartPropertyNameLocal(lookaheadOffset) || this.canStartIdentifierReferenceLocal(params, lookaheadOffset));
  }
  canSelectAssignmentShorthandProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    return (this.canStartIdentifierReferenceLocal(params, lookaheadOffset) && !__QinJavaLangString.equals("Colon", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartAssignmentRestProperty(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("Ellipsis", this.LA(lookaheadOffset).tokenName()));
  }
  canStartAssignmentRestElement(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("Ellipsis", this.LA(lookaheadOffset).tokenName()));
  }
  canSelectArrayAssignmentRestOrElisionOnly(lookaheadOffset: number): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__QinJavaLangString.equals("Ellipsis", tokenName)) {
      return true;
    }
    if ((!__QinJavaLangString.equals("Comma", tokenName))) {
      return false;
    }
    let offset: number = lookaheadOffset;
    while (__QinJavaLangString.equals("Comma", this.tokenNameAt(offset))) {
      offset++;
    }
    let afterElision: string = this.tokenNameAt(offset);
    return (__QinJavaLangString.equals("RBracket", afterElision) || __QinJavaLangString.equals("Ellipsis", afterElision));
  }
  canStartAssignmentElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let offset: number = lookaheadOffset;
    while (__QinJavaLangString.equals("Comma", this.tokenNameAt(offset))) {
      offset++;
    }
    return this.canStartAssignmentElement(params, offset);
  }
  canStartAssignmentElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    return this.canStartLeftHandSideExpressionLocal(params, lookaheadOffset);
  }
  canStartIdentifierReferenceLocal(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__QinJavaLangString.equals("IdentifierName", tokenName)) {
      return true;
    }
    if (__QinJavaLangString.equals("Yield", tokenName)) {
      return (!params.yield());
    }
    if (__QinJavaLangString.equals("Await", tokenName)) {
      return (!params.await());
    }
    return false;
  }
  canStartLeftHandSideExpressionLocal(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    if ((__QinJavaLangString.equals("This", tokenName) || __QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("NullLiteral", tokenName) || __QinJavaLangString.equals("True", tokenName) || __QinJavaLangString.equals("False", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("Function", tokenName) || __QinJavaLangString.equals("Class", tokenName) || __QinJavaLangString.equals("LBracket", tokenName) || __QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("RegularExpressionLiteral", tokenName) || __QinJavaLangString.equals("NoSubstitutionTemplate", tokenName) || __QinJavaLangString.equals("TemplateHead", tokenName) || __QinJavaLangString.equals("LParen", tokenName) || __QinJavaLangString.equals("Super", tokenName) || __QinJavaLangString.equals("Import", tokenName) || __QinJavaLangString.equals("New", tokenName))) {
      return true;
    }
    if (__QinJavaLangString.equals("Yield", tokenName)) {
      return (!params.yield());
    }
    if (__QinJavaLangString.equals("Await", tokenName)) {
      return (!params.await());
    }
    return false;
  }
  functionBindingRule(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): string {
    return (params.isDefault() ? "OptionalFunctionBindingIdentifier" : "RequiredFunctionBindingIdentifier");
  }
  FormalParameter(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FormalParameter_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FormalParameter_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FormalParameter/" + __qin_args.length);
  }
  __qin_overload_FormalParameter_0_0(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_FormalParameter_0_0 receiver=this arity=0 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_FormalParameter_0_0.call(this);
    }), "FormalParameter", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameter_0_0(): void {
    {
      const __qin_typed_receiver_829: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_829.BindingElement(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    }
    return null;
  }
  __qin_overload_FormalParameter_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_FormalParameter_1_1 receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_FormalParameter_1_1.call(this, params);
    }), "FormalParameter", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameter_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_830: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_830.BindingElement(params);
    }
    return null;
  }
  FunctionBody(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FunctionBody_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FunctionBody_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FunctionBody/" + __qin_args.length);
  }
  __qin_overload_FunctionBody_0_0(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_FunctionBody_0_0 receiver=this arity=0 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_FunctionBody_0_0.call(this);
    }), "FunctionBody", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FunctionBody_0_0(): void {
    {
      const __qin_typed_receiver_831: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_831.FunctionStatementList(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, false));
    }
    return null;
  }
  __qin_overload_FunctionBody_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_FunctionBody_1_1 receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_FunctionBody_1_1.call(this, params);
    }), "FunctionBody", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FunctionBody_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_832: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_832.FunctionStatementList(params);
    }
    return null;
  }
  FunctionStatementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_FunctionStatementList receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_FunctionStatementList.call(this, params);
    }), "FunctionStatementList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_FunctionStatementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_833: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_833.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FunctionStatementList", this.functionStaticRuntime(params));
    }
    return null;
  }
  FunctionBodyWithParams(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_834: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_834.FunctionStatementList(params);
    }
    return null;
  }
  GeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_GeneratorDeclaration receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_GeneratorDeclaration.call(this, params);
    }), "GeneratorDeclaration", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_GeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_835: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_835.Function();
    }
    {
      const __qin_typed_receiver_836: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_836.Asterisk();
    }
    {
      const __qin_typed_receiver_837: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_837.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, this.functionBindingRule(params), this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, true, params.await())));
    }
    if (this.isParserFail()) {
      return null;
    }
    {
      const __qin_typed_receiver_838: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_838.LParen();
    }
    {
      const __qin_typed_receiver_839: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_839.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, false));
    }
    {
      const __qin_typed_receiver_840: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_840.RParen();
    }
    {
      const __qin_typed_receiver_841: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_841.LBrace();
    }
    {
      const __qin_typed_receiver_842: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_842.GeneratorBody();
    }
    {
      const __qin_typed_receiver_843: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_843.RBrace();
    }
    return null;
  }
  GeneratorExpression(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_GeneratorExpression receiver=this arity=0 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_GeneratorExpression.call(this);
    }), "GeneratorExpression", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_GeneratorExpression(): void {
    {
      const __qin_typed_receiver_844: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_844.Function();
    }
    {
      const __qin_typed_receiver_845: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_845.Asterisk();
    }
    {
      const __qin_typed_receiver_846: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_846.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "OptionalFunctionBindingIdentifier", this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, true, false)));
    }
    {
      const __qin_typed_receiver_847: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_847.LParen();
    }
    {
      const __qin_typed_receiver_848: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_848.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, false));
    }
    {
      const __qin_typed_receiver_849: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_849.RParen();
    }
    {
      const __qin_typed_receiver_850: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_850.LBrace();
    }
    {
      const __qin_typed_receiver_851: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_851.GeneratorBody();
    }
    {
      const __qin_typed_receiver_852: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_852.RBrace();
    }
    return null;
  }
  GeneratorBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_GeneratorBody receiver=this arity=0 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_GeneratorBody.call(this);
    }), "GeneratorBody", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_GeneratorBody(): void {
    {
      const __qin_typed_receiver_853: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_853.FunctionBodyWithParams(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, false));
    }
    return null;
  }
  AsyncFunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AsyncFunctionDeclaration receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AsyncFunctionDeclaration.call(this, params);
    }), "AsyncFunctionDeclaration", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncFunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_854: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_854.consumeIdentifierValue("async");
    }
    {
      const __qin_typed_receiver_855: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_855.assertNoLineBreak();
    }
    {
      const __qin_typed_receiver_856: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_856.Function();
    }
    {
      const __qin_typed_receiver_857: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_857.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, this.functionBindingRule(params), this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), true)));
    }
    if (this.isParserFail()) {
      return null;
    }
    {
      const __qin_typed_receiver_858: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_858.LParen();
    }
    {
      const __qin_typed_receiver_859: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_859.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, true));
    }
    {
      const __qin_typed_receiver_860: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_860.RParen();
    }
    {
      const __qin_typed_receiver_861: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_861.LBrace();
    }
    {
      const __qin_typed_receiver_862: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_862.AsyncFunctionBody();
    }
    {
      const __qin_typed_receiver_863: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_863.RBrace();
    }
    return null;
  }
  AsyncFunctionExpression(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AsyncFunctionExpression receiver=this arity=0 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AsyncFunctionExpression.call(this);
    }), "AsyncFunctionExpression", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncFunctionExpression(): void {
    {
      const __qin_typed_receiver_864: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_864.consumeIdentifierValue("async");
    }
    {
      const __qin_typed_receiver_865: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_865.assertNoLineBreak();
    }
    {
      const __qin_typed_receiver_866: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_866.Function();
    }
    {
      const __qin_typed_receiver_867: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_867.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "OptionalFunctionBindingIdentifier", this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, false, true)));
    }
    {
      const __qin_typed_receiver_868: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_868.LParen();
    }
    {
      const __qin_typed_receiver_869: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_869.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, true));
    }
    {
      const __qin_typed_receiver_870: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_870.RParen();
    }
    {
      const __qin_typed_receiver_871: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_871.LBrace();
    }
    {
      const __qin_typed_receiver_872: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_872.AsyncFunctionBody();
    }
    {
      const __qin_typed_receiver_873: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_873.RBrace();
    }
    return null;
  }
  AsyncFunctionBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AsyncFunctionBody receiver=this arity=0 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AsyncFunctionBody.call(this);
    }), "AsyncFunctionBody", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncFunctionBody(): void {
    {
      const __qin_typed_receiver_874: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_874.FunctionBodyWithParams(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, true));
    }
    return null;
  }
  AsyncGeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AsyncGeneratorDeclaration receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AsyncGeneratorDeclaration.call(this, params);
    }), "AsyncGeneratorDeclaration", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncGeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_875: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_875.consumeIdentifierValue("async");
    }
    {
      const __qin_typed_receiver_876: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_876.assertNoLineBreak();
    }
    {
      const __qin_typed_receiver_877: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_877.Function();
    }
    {
      const __qin_typed_receiver_878: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_878.Asterisk();
    }
    {
      const __qin_typed_receiver_879: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_879.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, this.functionBindingRule(params), this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, true, true)));
    }
    if (this.isParserFail()) {
      return null;
    }
    {
      const __qin_typed_receiver_880: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_880.LParen();
    }
    {
      const __qin_typed_receiver_881: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_881.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, true));
    }
    {
      const __qin_typed_receiver_882: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_882.RParen();
    }
    {
      const __qin_typed_receiver_883: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_883.LBrace();
    }
    {
      const __qin_typed_receiver_884: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_884.AsyncGeneratorBody();
    }
    {
      const __qin_typed_receiver_885: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_885.RBrace();
    }
    return null;
  }
  AsyncGeneratorExpression(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AsyncGeneratorExpression receiver=this arity=0 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AsyncGeneratorExpression.call(this);
    }), "AsyncGeneratorExpression", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncGeneratorExpression(): void {
    {
      const __qin_typed_receiver_886: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_886.consumeIdentifierValue("async");
    }
    {
      const __qin_typed_receiver_887: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_887.assertNoLineBreak();
    }
    {
      const __qin_typed_receiver_888: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_888.Function();
    }
    {
      const __qin_typed_receiver_889: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_889.Asterisk();
    }
    {
      const __qin_typed_receiver_890: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_890.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "OptionalFunctionBindingIdentifier", this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, true, true)));
    }
    {
      const __qin_typed_receiver_891: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_891.LParen();
    }
    {
      const __qin_typed_receiver_892: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_892.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, true));
    }
    {
      const __qin_typed_receiver_893: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_893.RParen();
    }
    {
      const __qin_typed_receiver_894: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_894.LBrace();
    }
    {
      const __qin_typed_receiver_895: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_895.AsyncGeneratorBody();
    }
    {
      const __qin_typed_receiver_896: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_896.RBrace();
    }
    return null;
  }
  AsyncGeneratorBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AsyncGeneratorBody receiver=this arity=0 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AsyncGeneratorBody.call(this);
    }), "AsyncGeneratorBody", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncGeneratorBody(): void {
    {
      const __qin_typed_receiver_897: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_897.FunctionBodyWithParams(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, true));
    }
    return null;
  }
  BindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_BindingPattern receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_BindingPattern.call(this, params);
    }), "BindingPattern", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_898: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_898.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingPattern", com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_PARAMS_VARIANT_ID, this.functionStaticRuntime(params));
    }
    return null;
  }
  ObjectBindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_ObjectBindingPattern receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_ObjectBindingPattern.call(this, params);
    }), "ObjectBindingPattern", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ObjectBindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_899: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_899.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "ObjectBindingPattern", this.functionStaticRuntime(params));
    }
    return null;
  }
  ArrayBindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_ArrayBindingPattern receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_ArrayBindingPattern.call(this, params);
    }), "ArrayBindingPattern", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrayBindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_900: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_900.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "ArrayBindingPattern", this.functionStaticRuntime(params));
    }
    return null;
  }
  BindingRestProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_BindingRestProperty receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_BindingRestProperty.call(this, params);
    }), "BindingRestProperty", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingRestProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_901: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_901.Ellipsis();
    }
    {
      const __qin_typed_receiver_902: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_902.BindingIdentifier(params);
    }
    return null;
  }
  BindingPropertyList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_BindingPropertyList receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_BindingPropertyList.call(this, params);
    }), "BindingPropertyList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingPropertyList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_903: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_903.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingPropertyList", this.functionStaticRuntime(params));
    }
    return null;
  }
  BindingProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_BindingProperty receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_BindingProperty.call(this, params);
    }), "BindingProperty", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_904: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_904.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingProperty", this.functionStaticRuntime(params));
    }
    return null;
  }
  SingleNameBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_SingleNameBinding receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_SingleNameBinding.call(this, params);
    }), "SingleNameBinding", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_SingleNameBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_905: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_905.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "SingleNameBinding", this.functionStaticRuntime(params));
    }
    return null;
  }
  BindingElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_BindingElementList receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_BindingElementList.call(this, params);
    }), "BindingElementList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_906: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_906.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingElementList", this.functionStaticRuntime(params));
    }
    return null;
  }
  BindingElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_BindingElisionElement receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_BindingElisionElement.call(this, params);
    }), "BindingElisionElement", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_907: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_907.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingElisionElement", this.functionStaticRuntime(params));
    }
    return null;
  }
  BindingElement(...__qin_args: any[]): void {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_BindingElement_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_BindingElement_0_1();
    throw new Error("Unsupported Java overload: BindingElement/" + __qin_args.length);
  }
  __qin_overload_BindingElement_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_BindingElement_1_0 receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_BindingElement_1_0.call(this, params);
    }), "BindingElement", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_BindingElement_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_908: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_908.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingElement", this.functionStaticRuntime(params));
    }
    return null;
  }
  __qin_overload_BindingElement_0_1(): void {
    {
      const __qin_typed_receiver_909: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_909.BindingElement(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    }
    return null;
  }
  BindingRestElement(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_BindingRestElement_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_BindingRestElement_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: BindingRestElement/" + __qin_args.length);
  }
  __qin_overload_BindingRestElement_0_0(): void {
    {
      const __qin_typed_receiver_910: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_910.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingRestElement", this.functionStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    }
    return null;
  }
  __qin_overload_BindingRestElement_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw___qin_overload_BindingRestElement_1_1 receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw___qin_overload_BindingRestElement_1_1.call(this, params);
    }), "BindingRestElement", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_BindingRestElement_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_911: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_911.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingRestElement", this.functionStaticRuntime(params));
    }
    return null;
  }
  AssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AssignmentPattern receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AssignmentPattern.call(this, params);
    }), "AssignmentPattern", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_912: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_912.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "AssignmentPattern", this.functionStaticRuntime(params));
    }
    return null;
  }
  ObjectAssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_ObjectAssignmentPattern receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_ObjectAssignmentPattern.call(this, params);
    }), "ObjectAssignmentPattern", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ObjectAssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_913: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_913.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "ObjectAssignmentPattern", this.functionStaticRuntime(params));
    }
    return null;
  }
  ArrayAssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_ArrayAssignmentPattern receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_ArrayAssignmentPattern.call(this, params);
    }), "ArrayAssignmentPattern", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrayAssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_914: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_914.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "ArrayAssignmentPattern", this.functionStaticRuntime(params));
    }
    return null;
  }
  AssignmentRestProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AssignmentRestProperty receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AssignmentRestProperty.call(this, params);
    }), "AssignmentRestProperty", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentRestProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_915: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_915.Ellipsis();
    }
    {
      const __qin_typed_receiver_916: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_916.DestructuringAssignmentTarget(params);
    }
    return null;
  }
  AssignmentPropertyList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AssignmentPropertyList receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AssignmentPropertyList.call(this, params);
    }), "AssignmentPropertyList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentPropertyList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_917: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_917.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "AssignmentPropertyList", this.functionStaticRuntime(params));
    }
    return null;
  }
  AssignmentElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AssignmentElementList receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AssignmentElementList.call(this, params);
    }), "AssignmentElementList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_918: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_918.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "AssignmentElementList", this.functionStaticRuntime(params));
    }
    return null;
  }
  AssignmentElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AssignmentElisionElement receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AssignmentElisionElement.call(this, params);
    }), "AssignmentElisionElement", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_919: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_919.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "AssignmentElisionElement", this.functionStaticRuntime(params));
    }
    return null;
  }
  AssignmentProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AssignmentProperty receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AssignmentProperty.call(this, params);
    }), "AssignmentProperty", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_920: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_920.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "AssignmentProperty", this.functionStaticRuntime(params));
    }
    return null;
  }
  AssignmentElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AssignmentElement receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AssignmentElement.call(this, params);
    }), "AssignmentElement", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_921: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_921.DestructuringAssignmentTarget(params);
    }
    {
      const __qin_typed_receiver_922: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_922.OptionalFunctionInitializer(params.withIn(true));
    }
    return null;
  }
  OptionalFunctionInitializer(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_923: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_923.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "OptionalFunctionInitializer", this.functionStaticRuntime(params));
    }
    return null;
  }
  AssignmentInitializer(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AssignmentInitializer receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AssignmentInitializer.call(this, params);
    }), "AssignmentInitializer", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentInitializer(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_924: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_924.Initializer(params.withIn(true));
    }
    return null;
  }
  AssignmentRestElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_AssignmentRestElement receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_AssignmentRestElement.call(this, params);
    }), "AssignmentRestElement", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentRestElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_925: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_925.Ellipsis();
    }
    {
      const __qin_typed_receiver_926: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_926.DestructuringAssignmentTarget(params);
    }
    return null;
  }
  DestructuringAssignmentTarget(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.function.SlimeFunctionParser method=__qin_subhuti_raw_DestructuringAssignmentTarget receiver=this arity=1 */ com_slime_parser_function_SlimeFunctionParser.prototype.__qin_subhuti_raw_DestructuringAssignmentTarget.call(this, params);
    }), "DestructuringAssignmentTarget", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_DestructuringAssignmentTarget(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_927: com_slime_parser_function_SlimeFunctionParser = this;
      __qin_typed_receiver_927.LeftHandSideExpression(params);
    }
    return null;
  }
}
const SlimeFunctionParser = com_slime_parser_function_SlimeFunctionParser;
class com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime {
  __qin_field_parser: com_slime_parser_function_SlimeFunctionParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_function_SlimeFunctionParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const params: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime_2_0(parser, params);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeFunctionParser$FunctionStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime_2_0(parser: com_slime_parser_function_SlimeFunctionParser, params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params);
  }
  runStaticAction(actionId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_ACTION_FORMAL_PARAMETERS_LIST_REST, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_ACTION_FORMAL_PARAMETERS_LIST_TRAILING_COMMA, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_ACTION_FORMAL_PARAMETERS_LIST, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_ACTION_FORMAL_PARAMETERS_REST, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_ACTION_FORMAL_PARAMETERS_EMPTY, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_ACTION_FORMAL_PARAMETER_LIST, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_ACTION_FUNCTION_REST_PARAMETER, actionId)) {
      return true;
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported function static action: " + actionId));
  }
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_GATE_REQUIRED_FUNCTION_BINDING_IDENTIFIER, gateId)) {
      return this.__qin_field_parser.canStartBindingIdentifier(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_GATE_PROPERTY_NAME_BRANCH, gateId)) {
      return this.__qin_field_parser.canSelectPropertyNameBranch(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_GATE_BINDING_SHORTHAND_PROPERTY, gateId)) {
      return this.__qin_field_parser.canSelectBindingShorthandProperty(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_GATE_ASSIGNMENT_SHORTHAND_PROPERTY, gateId)) {
      return this.__qin_field_parser.canSelectAssignmentShorthandProperty(this.__qin_field_effectiveParams, 1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_GATE_ARRAY_BINDING_REST_OR_ELISION_ONLY, gateId)) {
      return this.__qin_field_parser.canSelectArrayBindingRestOrElisionOnly(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_GATE_ARRAY_ASSIGNMENT_REST_OR_ELISION_ONLY, gateId)) {
      return this.__qin_field_parser.canSelectArrayAssignmentRestOrElisionOnly(1.0);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported function static gate: " + gateId));
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
    if (__QinJavaLangString.equals("FormalParameterList", ruleName)) {
      return this.__qin_field_parser.canStartFormalParameter(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("FunctionRestParameter", ruleName)) {
      return this.__qin_field_parser.canStartBindingRestElement(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("FormalParameter", ruleName)) {
      return this.__qin_field_parser.canStartFormalParameter(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BindingRestElement", ruleName)) {
      return this.__qin_field_parser.canStartBindingRestElement(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BindingPattern", ruleName)) {
      return this.__qin_field_parser.canStartBindingPattern(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      return this.__qin_field_parser.canStartBindingIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("ObjectBindingPattern", ruleName)) {
      return this.__qin_field_parser.canStartObjectBindingPattern(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("ArrayBindingPattern", ruleName)) {
      return this.__qin_field_parser.canStartArrayBindingPattern(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("Initializer", ruleName)) {
      return this.__qin_field_parser.canStartInitializer(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AssignmentInitializer", ruleName)) {
      return this.__qin_field_parser.canStartInitializer(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("Elision", ruleName)) {
      return __QinJavaLangString.equals("Comma", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("OptionalFunctionInitializer", ruleName)) {
      return true;
    }
    if (__QinJavaLangString.equals("SingleNameBinding", ruleName)) {
      return this.__qin_field_parser.canStartBindingIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BindingElement", ruleName)) {
      return (this.__qin_field_parser.canStartBindingIdentifier(lookaheadOffset) || this.__qin_field_parser.canStartBindingPattern(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("ObjectAssignmentPattern", ruleName)) {
      return this.__qin_field_parser.canStartObjectBindingPattern(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("ArrayAssignmentPattern", ruleName)) {
      return this.__qin_field_parser.canStartArrayBindingPattern(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AssignmentPattern", ruleName)) {
      return (this.__qin_field_parser.canStartObjectBindingPattern(lookaheadOffset) || this.__qin_field_parser.canStartArrayBindingPattern(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("BindingProperty", ruleName)) {
      return this.__qin_field_parser.canStartBindingProperty(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BindingRestProperty", ruleName)) {
      return this.__qin_field_parser.canStartBindingRestProperty(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("PropertyName", ruleName)) {
      return this.__qin_field_parser.canStartPropertyNameLocal(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BindingPropertyList", ruleName)) {
      return this.__qin_field_parser.canStartBindingProperty(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BindingElisionElement", ruleName)) {
      return this.__qin_field_parser.canStartBindingElisionElement(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BindingElementList", ruleName)) {
      return this.__qin_field_parser.canStartBindingElisionElement(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AssignmentProperty", ruleName)) {
      return this.__qin_field_parser.canStartAssignmentProperty(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("IdentifierReference", ruleName)) {
      return this.__qin_field_parser.canStartIdentifierReferenceLocal(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AssignmentPropertyList", ruleName)) {
      return this.__qin_field_parser.canStartAssignmentProperty(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AssignmentRestProperty", ruleName)) {
      return this.__qin_field_parser.canStartAssignmentRestProperty(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AssignmentRestElement", ruleName)) {
      return this.__qin_field_parser.canStartAssignmentRestElement(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AssignmentElisionElement", ruleName)) {
      return this.__qin_field_parser.canStartAssignmentElisionElement(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AssignmentElement", ruleName)) {
      return this.__qin_field_parser.canStartAssignmentElement(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AssignmentElementList", ruleName)) {
      return this.__qin_field_parser.canStartAssignmentElisionElement(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("FunctionStatementList", ruleName)) {
      return true;
    }
    if (__QinJavaLangString.equals("StatementList", ruleName)) {
      return this.__qin_field_parser.canStartStatementListItemAt(lookaheadOffset, this.__qin_field_parser.functionStatementParams(this.__qin_field_effectiveParams));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported function static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    let number: __QinJavaLangNumber = null as any;
    let parameterized: boolean = ((__qin_instanceof__(variantKey, __QinJavaLangNumber) && (number = variantKey, true)) && __qin_binary__("==", number.intValue(), com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_PARAMS_VARIANT_ID));
    if (__QinJavaLangString.equals("FormalParameterList", ruleName)) {
      if (parameterized) {
        {
          const __qin_typed_receiver_928: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
          __qin_typed_receiver_928.FormalParameterList(this.__qin_field_effectiveParams);
        }
      } else {
        {
          const __qin_typed_receiver_929: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
          __qin_typed_receiver_929.FormalParameterList();
        }
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("FunctionRestParameter", ruleName)) {
      if (parameterized) {
        {
          const __qin_typed_receiver_930: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
          __qin_typed_receiver_930.FunctionRestParameter(this.__qin_field_effectiveParams);
        }
      } else {
        {
          const __qin_typed_receiver_931: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
          __qin_typed_receiver_931.FunctionRestParameter();
        }
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("FormalParameter", ruleName)) {
      if (parameterized) {
        {
          const __qin_typed_receiver_932: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
          __qin_typed_receiver_932.FormalParameter(this.__qin_field_effectiveParams);
        }
      } else {
        {
          const __qin_typed_receiver_933: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
          __qin_typed_receiver_933.FormalParameter(this.__qin_field_effectiveParams);
        }
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingRestElement", ruleName)) {
      if (parameterized) {
        {
          const __qin_typed_receiver_934: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
          __qin_typed_receiver_934.BindingRestElement(this.__qin_field_effectiveParams);
        }
      } else {
        {
          const __qin_typed_receiver_935: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
          __qin_typed_receiver_935.BindingRestElement();
        }
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingPattern", ruleName)) {
      if (parameterized) {
        {
          const __qin_typed_receiver_936: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
          __qin_typed_receiver_936.BindingPattern(this.__qin_field_effectiveParams);
        }
      } else {
        {
          const __qin_typed_receiver_937: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
          __qin_typed_receiver_937.BindingPattern(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
        }
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      if (parameterized) {
        {
          const __qin_typed_receiver_938: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
          __qin_typed_receiver_938.BindingIdentifier(this.__qin_field_effectiveParams);
        }
      } else {
        {
          const __qin_typed_receiver_939: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
          __qin_typed_receiver_939.BindingIdentifier(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
        }
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ObjectBindingPattern", ruleName)) {
      {
        const __qin_typed_receiver_940: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_940.ObjectBindingPattern(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ArrayBindingPattern", ruleName)) {
      {
        const __qin_typed_receiver_941: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_941.ArrayBindingPattern(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Initializer", ruleName)) {
      {
        const __qin_typed_receiver_942: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_942.Initializer(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentInitializer", ruleName)) {
      {
        const __qin_typed_receiver_943: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_943.Initializer(this.__qin_field_effectiveParams.withIn(true));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Elision", ruleName)) {
      {
        const __qin_typed_receiver_944: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_944.Elision();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("OptionalFunctionInitializer", ruleName)) {
      {
        const __qin_typed_receiver_945: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_945.OptionalFunctionInitializer(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("SingleNameBinding", ruleName)) {
      {
        const __qin_typed_receiver_946: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_946.SingleNameBinding(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingElement", ruleName)) {
      {
        const __qin_typed_receiver_947: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_947.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingElement", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ObjectAssignmentPattern", ruleName)) {
      {
        const __qin_typed_receiver_948: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_948.ObjectAssignmentPattern(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ArrayAssignmentPattern", ruleName)) {
      {
        const __qin_typed_receiver_949: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_949.ArrayAssignmentPattern(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentPattern", ruleName)) {
      {
        const __qin_typed_receiver_950: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_950.AssignmentPattern(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingProperty", ruleName)) {
      {
        const __qin_typed_receiver_951: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_951.BindingProperty(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingRestProperty", ruleName)) {
      {
        const __qin_typed_receiver_952: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_952.BindingRestProperty(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("PropertyName", ruleName)) {
      {
        const __qin_typed_receiver_953: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_953.PropertyName(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingPropertyList", ruleName)) {
      {
        const __qin_typed_receiver_954: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_954.BindingPropertyList(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingElisionElement", ruleName)) {
      {
        const __qin_typed_receiver_955: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_955.BindingElisionElement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingElementList", ruleName)) {
      {
        const __qin_typed_receiver_956: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_956.BindingElementList(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentProperty", ruleName)) {
      {
        const __qin_typed_receiver_957: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_957.AssignmentProperty(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("IdentifierReference", ruleName)) {
      {
        const __qin_typed_receiver_958: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_958.IdentifierReference(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentRestProperty", ruleName)) {
      {
        const __qin_typed_receiver_959: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_959.AssignmentRestProperty(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentRestElement", ruleName)) {
      {
        const __qin_typed_receiver_960: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_960.AssignmentRestElement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentPropertyList", ruleName)) {
      {
        const __qin_typed_receiver_961: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_961.AssignmentPropertyList(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentElisionElement", ruleName)) {
      {
        const __qin_typed_receiver_962: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_962.AssignmentElisionElement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentElement", ruleName)) {
      {
        const __qin_typed_receiver_963: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_963.AssignmentElement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentElementList", ruleName)) {
      {
        const __qin_typed_receiver_964: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_964.AssignmentElementList(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("FunctionStatementList", ruleName)) {
      {
        const __qin_typed_receiver_965: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_965.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FunctionStatementList", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("StatementList", ruleName)) {
      {
        const __qin_typed_receiver_966: com_slime_parser_function_SlimeFunctionParser = this.__qin_field_parser;
        __qin_typed_receiver_966.StatementList(this.__qin_field_parser.functionStatementParams(this.__qin_field_effectiveParams));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported function static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeFunctionParser$FunctionStaticRuntime = com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime;
com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR = com_slime_parser_function_SlimeFunctionStaticGrammar.grammar();

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_function_SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime };
