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
  FunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FunctionDeclaration(params);
    }), "FunctionDeclaration", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_FunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.__qin_field_tokenConsumer.Function();
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, this.functionBindingRule(params), this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await())));
    if (this.isParserFail()) {
      return null;
    }
    this.__qin_field_tokenConsumer.LParen();
    this.FormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.__qin_field_tokenConsumer.LBrace();
    this.FunctionBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  FunctionExpression(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FunctionExpression();
    }), "FunctionExpression", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_FunctionExpression(): any {
    this.__qin_field_tokenConsumer.Function();
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "OptionalFunctionBindingIdentifier", this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, false)));
    this.__qin_field_tokenConsumer.LParen();
    this.FormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.__qin_field_tokenConsumer.LBrace();
    this.FunctionBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  FormalParameters(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FormalParameters_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FormalParameters_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FormalParameters/" + __qin_args.length);
  }
  __qin_overload_FormalParameters_0_0(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FormalParameters_0_0();
    }), "FormalParameters", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameters_0_0(): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FormalParameters", this.functionStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    return null;
  }
  __qin_overload_FormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FormalParameters_1_1(params);
    }), "FormalParameters", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FormalParameters", com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_PARAMS_VARIANT_ID, this.functionStaticRuntime(params));
    return null;
  }
  UniqueFormalParameters(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_UniqueFormalParameters_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_UniqueFormalParameters_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: UniqueFormalParameters/" + __qin_args.length);
  }
  __qin_overload_UniqueFormalParameters_0_0(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_UniqueFormalParameters_0_0();
    }), "UniqueFormalParameters", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_UniqueFormalParameters_0_0(): any {
    this.FormalParameters();
    return null;
  }
  __qin_overload_UniqueFormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_UniqueFormalParameters_1_1(params);
    }), "UniqueFormalParameters", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_UniqueFormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.FormalParameters(params);
    return null;
  }
  FormalParameterList(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FormalParameterList_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FormalParameterList_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FormalParameterList/" + __qin_args.length);
  }
  __qin_overload_FormalParameterList_0_0(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FormalParameterList_0_0();
    }), "FormalParameterList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameterList_0_0(): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FormalParameterList", this.functionStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    return null;
  }
  __qin_overload_FormalParameterList_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FormalParameterList_1_1(params);
    }), "FormalParameterList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameterList_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FormalParameterList", com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_PARAMS_VARIANT_ID, this.functionStaticRuntime(params));
    return null;
  }
  FunctionRestParameter(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FunctionRestParameter_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FunctionRestParameter_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FunctionRestParameter/" + __qin_args.length);
  }
  __qin_overload_FunctionRestParameter_0_0(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FunctionRestParameter_0_0();
    }), "FunctionRestParameter", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FunctionRestParameter_0_0(): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FunctionRestParameter", this.functionStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    return null;
  }
  __qin_overload_FunctionRestParameter_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FunctionRestParameter_1_1(params);
    }), "FunctionRestParameter", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FunctionRestParameter_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FunctionRestParameter", com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_PARAMS_VARIANT_ID, this.functionStaticRuntime(params));
    return null;
  }
  functionStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return new com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime(this, params);
  }
  functionStatementParams(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return new com_slime_parser_base_SlimeJavascriptParserBase$StatementParams((__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params).__qin_yield(), (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params).__qin_await(), true);
  }
  canStartFormalParameter(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartFormalParameter_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartFormalParameter_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartFormalParameter/" + __qin_args.length);
  }
  __qin_overload_canStartFormalParameter_0_0(): any {
    return this.canStartFormalParameter(1.0);
  }
  __qin_overload_canStartFormalParameter_1_1(lookaheadOffset: number): any {
    let token: any = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    return (() => {
      switch (token.tokenName()) {
        case "IdentifierName": {
        }
        case "Yield": {
        }
        case "Await": {
        }
        case "LBrace": {
        }
        case "LBracket": {
          return true;
        }
        default: {
          return false;
        }
      }
      return null;
    })();
  }
  canStartBindingRestElement(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartBindingRestElement_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartBindingRestElement_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartBindingRestElement/" + __qin_args.length);
  }
  __qin_overload_canStartBindingRestElement_0_0(): any {
    return this.canStartBindingRestElement(1.0);
  }
  __qin_overload_canStartBindingRestElement_1_1(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("Ellipsis", this.LA(lookaheadOffset).tokenName()));
  }
  canStartBindingRestProperty(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("Ellipsis", this.LA(lookaheadOffset).tokenName()));
  }
  canStartBindingPattern(lookaheadOffset: number): any {
    return (this.canStartObjectBindingPattern(lookaheadOffset) || this.canStartArrayBindingPattern(lookaheadOffset));
  }
  canStartObjectBindingPattern(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("LBrace", this.LA(lookaheadOffset).tokenName()));
  }
  canStartArrayBindingPattern(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("LBracket", this.LA(lookaheadOffset).tokenName()));
  }
  canStartBindingIdentifier(lookaheadOffset: number): any {
    let token: any = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    return (() => {
      switch (token.tokenName()) {
        case "IdentifierName": {
        }
        case "Yield": {
        }
        case "Await": {
          return true;
        }
        default: {
          return false;
        }
      }
      return null;
    })();
  }
  canStartInitializer(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("Assign", this.LA(lookaheadOffset).tokenName()));
  }
  canStartPropertyNameLocal(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("IdentifierName", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Yield", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Await", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("StringLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("NumericLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBracket", this.tokenNameAt(lookaheadOffset)));
  }
  canStartBindingProperty(lookaheadOffset: number): any {
    return (this.canStartPropertyNameLocal(lookaheadOffset) || this.canStartBindingIdentifier(lookaheadOffset));
  }
  canSelectPropertyNameBranch(lookaheadOffset: number): any {
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
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
  canSelectBindingShorthandProperty(lookaheadOffset: number): any {
    return (this.canStartBindingIdentifier(lookaheadOffset) && !__QinJavaLangString.equals("Colon", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartBindingElisionElement(lookaheadOffset: number): any {
    let offset: any = lookaheadOffset;
    while (__QinJavaLangString.equals("Comma", this.tokenNameAt(offset))) {
      offset++;
    }
    return this.canStartBindingElement(offset);
  }
  canStartBindingElement(lookaheadOffset: number): any {
    return (this.canStartBindingIdentifier(lookaheadOffset) || this.canStartBindingPattern(lookaheadOffset));
  }
  canSelectArrayBindingRestOrElisionOnly(lookaheadOffset: number): any {
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if (__QinJavaLangString.equals("Ellipsis", tokenName)) {
      return true;
    }
    if ((!__QinJavaLangString.equals("Comma", tokenName))) {
      return false;
    }
    let offset: any = lookaheadOffset;
    while (__QinJavaLangString.equals("Comma", this.tokenNameAt(offset))) {
      offset++;
    }
    let afterElision: any = this.tokenNameAt(offset);
    return (__QinJavaLangString.equals("RBracket", afterElision) || __QinJavaLangString.equals("Ellipsis", afterElision));
  }
  canStartAssignmentProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    return (this.canStartPropertyNameLocal(lookaheadOffset) || this.canStartIdentifierReferenceLocal(params, lookaheadOffset));
  }
  canSelectAssignmentShorthandProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    return (this.canStartIdentifierReferenceLocal(params, lookaheadOffset) && !__QinJavaLangString.equals("Colon", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartAssignmentRestProperty(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("Ellipsis", this.LA(lookaheadOffset).tokenName()));
  }
  canStartAssignmentRestElement(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("Ellipsis", this.LA(lookaheadOffset).tokenName()));
  }
  canSelectArrayAssignmentRestOrElisionOnly(lookaheadOffset: number): any {
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if (__QinJavaLangString.equals("Ellipsis", tokenName)) {
      return true;
    }
    if ((!__QinJavaLangString.equals("Comma", tokenName))) {
      return false;
    }
    let offset: any = lookaheadOffset;
    while (__QinJavaLangString.equals("Comma", this.tokenNameAt(offset))) {
      offset++;
    }
    let afterElision: any = this.tokenNameAt(offset);
    return (__QinJavaLangString.equals("RBracket", afterElision) || __QinJavaLangString.equals("Ellipsis", afterElision));
  }
  canStartAssignmentElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    let offset: any = lookaheadOffset;
    while (__QinJavaLangString.equals("Comma", this.tokenNameAt(offset))) {
      offset++;
    }
    return this.canStartAssignmentElement(params, offset);
  }
  canStartAssignmentElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    return this.canStartLeftHandSideExpressionLocal(params, lookaheadOffset);
  }
  canStartIdentifierReferenceLocal(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if (__QinJavaLangString.equals("IdentifierName", tokenName)) {
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
  canStartLeftHandSideExpressionLocal(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    if ((__QinJavaLangString.equals("This", tokenName) || __QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("NullLiteral", tokenName) || __QinJavaLangString.equals("True", tokenName) || __QinJavaLangString.equals("False", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("Function", tokenName) || __QinJavaLangString.equals("Class", tokenName) || __QinJavaLangString.equals("LBracket", tokenName) || __QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("RegularExpressionLiteral", tokenName) || __QinJavaLangString.equals("NoSubstitutionTemplate", tokenName) || __QinJavaLangString.equals("TemplateHead", tokenName) || __QinJavaLangString.equals("LParen", tokenName) || __QinJavaLangString.equals("Super", tokenName) || __QinJavaLangString.equals("Import", tokenName) || __QinJavaLangString.equals("New", tokenName))) {
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
  functionBindingRule(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return (params.isDefault() ? "OptionalFunctionBindingIdentifier" : "RequiredFunctionBindingIdentifier");
  }
  FormalParameter(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FormalParameter_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FormalParameter_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FormalParameter/" + __qin_args.length);
  }
  __qin_overload_FormalParameter_0_0(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FormalParameter_0_0();
    }), "FormalParameter", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameter_0_0(): any {
    this.BindingElement(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    return null;
  }
  __qin_overload_FormalParameter_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FormalParameter_1_1(params);
    }), "FormalParameter", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameter_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.BindingElement(params);
    return null;
  }
  FunctionBody(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FunctionBody_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FunctionBody_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FunctionBody/" + __qin_args.length);
  }
  __qin_overload_FunctionBody_0_0(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FunctionBody_0_0();
    }), "FunctionBody", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FunctionBody_0_0(): any {
    this.FunctionStatementList(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, false));
    return null;
  }
  __qin_overload_FunctionBody_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FunctionBody_1_1(params);
    }), "FunctionBody", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FunctionBody_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.FunctionStatementList(params);
    return null;
  }
  FunctionStatementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FunctionStatementList(params);
    }), "FunctionStatementList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_FunctionStatementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FunctionStatementList", this.functionStaticRuntime(params));
    return null;
  }
  FunctionBodyWithParams(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.FunctionStatementList(params);
    return null;
  }
  GeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_GeneratorDeclaration(params);
    }), "GeneratorDeclaration", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_GeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.__qin_field_tokenConsumer.Function();
    this.__qin_field_tokenConsumer.Asterisk();
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, this.functionBindingRule(params), this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, true, params.__qin_await())));
    if (this.isParserFail()) {
      return null;
    }
    this.__qin_field_tokenConsumer.LParen();
    this.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, false));
    this.__qin_field_tokenConsumer.RParen();
    this.__qin_field_tokenConsumer.LBrace();
    this.GeneratorBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  GeneratorExpression(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_GeneratorExpression();
    }), "GeneratorExpression", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_GeneratorExpression(): any {
    this.__qin_field_tokenConsumer.Function();
    this.__qin_field_tokenConsumer.Asterisk();
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "OptionalFunctionBindingIdentifier", this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, true, false)));
    this.__qin_field_tokenConsumer.LParen();
    this.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, false));
    this.__qin_field_tokenConsumer.RParen();
    this.__qin_field_tokenConsumer.LBrace();
    this.GeneratorBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  GeneratorBody(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_GeneratorBody();
    }), "GeneratorBody", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_GeneratorBody(): any {
    this.FunctionBodyWithParams(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, false));
    return null;
  }
  AsyncFunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncFunctionDeclaration(params);
    }), "AsyncFunctionDeclaration", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncFunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.consumeIdentifierValue("async");
    this.assertNoLineBreak();
    this.__qin_field_tokenConsumer.Function();
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, this.functionBindingRule(params), this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), true)));
    if (this.isParserFail()) {
      return null;
    }
    this.__qin_field_tokenConsumer.LParen();
    this.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, true));
    this.__qin_field_tokenConsumer.RParen();
    this.__qin_field_tokenConsumer.LBrace();
    this.AsyncFunctionBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  AsyncFunctionExpression(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncFunctionExpression();
    }), "AsyncFunctionExpression", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncFunctionExpression(): any {
    this.consumeIdentifierValue("async");
    this.assertNoLineBreak();
    this.__qin_field_tokenConsumer.Function();
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "OptionalFunctionBindingIdentifier", this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, false, true)));
    this.__qin_field_tokenConsumer.LParen();
    this.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, true));
    this.__qin_field_tokenConsumer.RParen();
    this.__qin_field_tokenConsumer.LBrace();
    this.AsyncFunctionBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  AsyncFunctionBody(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncFunctionBody();
    }), "AsyncFunctionBody", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncFunctionBody(): any {
    this.FunctionBodyWithParams(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, true));
    return null;
  }
  AsyncGeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncGeneratorDeclaration(params);
    }), "AsyncGeneratorDeclaration", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncGeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.consumeIdentifierValue("async");
    this.assertNoLineBreak();
    this.__qin_field_tokenConsumer.Function();
    this.__qin_field_tokenConsumer.Asterisk();
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, this.functionBindingRule(params), this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, true, true)));
    if (this.isParserFail()) {
      return null;
    }
    this.__qin_field_tokenConsumer.LParen();
    this.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, true));
    this.__qin_field_tokenConsumer.RParen();
    this.__qin_field_tokenConsumer.LBrace();
    this.AsyncGeneratorBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  AsyncGeneratorExpression(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncGeneratorExpression();
    }), "AsyncGeneratorExpression", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncGeneratorExpression(): any {
    this.consumeIdentifierValue("async");
    this.assertNoLineBreak();
    this.__qin_field_tokenConsumer.Function();
    this.__qin_field_tokenConsumer.Asterisk();
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "OptionalFunctionBindingIdentifier", this.functionStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, true, true)));
    this.__qin_field_tokenConsumer.LParen();
    this.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, true));
    this.__qin_field_tokenConsumer.RParen();
    this.__qin_field_tokenConsumer.LBrace();
    this.AsyncGeneratorBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  AsyncGeneratorBody(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncGeneratorBody();
    }), "AsyncGeneratorBody", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncGeneratorBody(): any {
    this.FunctionBodyWithParams(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, true));
    return null;
  }
  BindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingPattern(params);
    }), "BindingPattern", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingPattern", com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_PARAMS_VARIANT_ID, this.functionStaticRuntime(params));
    return null;
  }
  ObjectBindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ObjectBindingPattern(params);
    }), "ObjectBindingPattern", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ObjectBindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "ObjectBindingPattern", this.functionStaticRuntime(params));
    return null;
  }
  ArrayBindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArrayBindingPattern(params);
    }), "ArrayBindingPattern", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrayBindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "ArrayBindingPattern", this.functionStaticRuntime(params));
    return null;
  }
  BindingRestProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingRestProperty(params);
    }), "BindingRestProperty", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingRestProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.__qin_field_tokenConsumer.Ellipsis();
    this.BindingIdentifier(params);
    return null;
  }
  BindingPropertyList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingPropertyList(params);
    }), "BindingPropertyList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingPropertyList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingPropertyList", this.functionStaticRuntime(params));
    return null;
  }
  BindingProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingProperty(params);
    }), "BindingProperty", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingProperty", this.functionStaticRuntime(params));
    return null;
  }
  SingleNameBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_SingleNameBinding(params);
    }), "SingleNameBinding", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_SingleNameBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "SingleNameBinding", this.functionStaticRuntime(params));
    return null;
  }
  BindingElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingElementList(params);
    }), "BindingElementList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingElementList", this.functionStaticRuntime(params));
    return null;
  }
  BindingElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingElisionElement(params);
    }), "BindingElisionElement", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingElisionElement", this.functionStaticRuntime(params));
    return null;
  }
  BindingElement(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_BindingElement_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_BindingElement_0_1();
    throw new Error("Unsupported Java overload: BindingElement/" + __qin_args.length);
  }
  __qin_overload_BindingElement_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_BindingElement_1_0(params);
    }), "BindingElement", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_BindingElement_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingElement", this.functionStaticRuntime(params));
    return null;
  }
  __qin_overload_BindingElement_0_1(): any {
    this.BindingElement(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    return null;
  }
  BindingRestElement(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_BindingRestElement_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_BindingRestElement_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: BindingRestElement/" + __qin_args.length);
  }
  __qin_overload_BindingRestElement_0_0(): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingRestElement", this.functionStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    return null;
  }
  __qin_overload_BindingRestElement_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_BindingRestElement_1_1(params);
    }), "BindingRestElement", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_BindingRestElement_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingRestElement", this.functionStaticRuntime(params));
    return null;
  }
  AssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentPattern(params);
    }), "AssignmentPattern", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "AssignmentPattern", this.functionStaticRuntime(params));
    return null;
  }
  ObjectAssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ObjectAssignmentPattern(params);
    }), "ObjectAssignmentPattern", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ObjectAssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "ObjectAssignmentPattern", this.functionStaticRuntime(params));
    return null;
  }
  ArrayAssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArrayAssignmentPattern(params);
    }), "ArrayAssignmentPattern", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrayAssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "ArrayAssignmentPattern", this.functionStaticRuntime(params));
    return null;
  }
  AssignmentRestProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentRestProperty(params);
    }), "AssignmentRestProperty", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentRestProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.__qin_field_tokenConsumer.Ellipsis();
    this.DestructuringAssignmentTarget(params);
    return null;
  }
  AssignmentPropertyList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentPropertyList(params);
    }), "AssignmentPropertyList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentPropertyList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "AssignmentPropertyList", this.functionStaticRuntime(params));
    return null;
  }
  AssignmentElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentElementList(params);
    }), "AssignmentElementList", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "AssignmentElementList", this.functionStaticRuntime(params));
    return null;
  }
  AssignmentElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentElisionElement(params);
    }), "AssignmentElisionElement", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "AssignmentElisionElement", this.functionStaticRuntime(params));
    return null;
  }
  AssignmentProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentProperty(params);
    }), "AssignmentProperty", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "AssignmentProperty", this.functionStaticRuntime(params));
    return null;
  }
  AssignmentElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentElement(params);
    }), "AssignmentElement", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.DestructuringAssignmentTarget(params);
    this.OptionalFunctionInitializer(params.withIn(true));
    return null;
  }
  OptionalFunctionInitializer(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "OptionalFunctionInitializer", this.functionStaticRuntime(params));
    return null;
  }
  AssignmentInitializer(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentInitializer(params);
    }), "AssignmentInitializer", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentInitializer(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Initializer(params.withIn(true));
    return null;
  }
  AssignmentRestElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentRestElement(params);
    }), "AssignmentRestElement", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentRestElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.__qin_field_tokenConsumer.Ellipsis();
    this.DestructuringAssignmentTarget(params);
    return null;
  }
  DestructuringAssignmentTarget(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_DestructuringAssignmentTarget(params);
    }), "DestructuringAssignmentTarget", "SlimeFunctionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_DestructuringAssignmentTarget(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.LeftHandSideExpression(params);
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
  runStaticAction(actionId: string): any {
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
  testStaticGate(gateId: string): any {
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    let number: any = null;
    let parameterized: any = ((() => { const __qin_pattern_value = variantKey; return __qin_instanceof__(__qin_pattern_value, __QinJavaLangNumber) && (number = __qin_pattern_value, true); })() && __qin_binary__("==", number.intValue(), com_slime_parser_function_SlimeFunctionStaticGrammar.__qin_field_PARAMS_VARIANT_ID));
    if (__QinJavaLangString.equals("FormalParameterList", ruleName)) {
      if (parameterized) {
        this.__qin_field_parser.FormalParameterList(this.__qin_field_effectiveParams);
      } else {
        this.__qin_field_parser.FormalParameterList();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("FunctionRestParameter", ruleName)) {
      if (parameterized) {
        this.__qin_field_parser.FunctionRestParameter(this.__qin_field_effectiveParams);
      } else {
        this.__qin_field_parser.FunctionRestParameter();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("FormalParameter", ruleName)) {
      if (parameterized) {
        this.__qin_field_parser.FormalParameter(this.__qin_field_effectiveParams);
      } else {
        this.__qin_field_parser.FormalParameter(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingRestElement", ruleName)) {
      if (parameterized) {
        this.__qin_field_parser.BindingRestElement(this.__qin_field_effectiveParams);
      } else {
        this.__qin_field_parser.BindingRestElement();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingPattern", ruleName)) {
      if (parameterized) {
        this.__qin_field_parser.BindingPattern(this.__qin_field_effectiveParams);
      } else {
        this.__qin_field_parser.BindingPattern(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      if (parameterized) {
        this.__qin_field_parser.BindingIdentifier(this.__qin_field_effectiveParams);
      } else {
        this.__qin_field_parser.BindingIdentifier(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ObjectBindingPattern", ruleName)) {
      this.__qin_field_parser.ObjectBindingPattern(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ArrayBindingPattern", ruleName)) {
      this.__qin_field_parser.ArrayBindingPattern(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Initializer", ruleName)) {
      this.__qin_field_parser.Initializer(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentInitializer", ruleName)) {
      this.__qin_field_parser.Initializer(this.__qin_field_effectiveParams.withIn(true));
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Elision", ruleName)) {
      this.__qin_field_parser.Elision();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("OptionalFunctionInitializer", ruleName)) {
      this.__qin_field_parser.OptionalFunctionInitializer(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("SingleNameBinding", ruleName)) {
      this.__qin_field_parser.SingleNameBinding(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingElement", ruleName)) {
      this.__qin_field_parser.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "BindingElement", this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ObjectAssignmentPattern", ruleName)) {
      this.__qin_field_parser.ObjectAssignmentPattern(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ArrayAssignmentPattern", ruleName)) {
      this.__qin_field_parser.ArrayAssignmentPattern(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentPattern", ruleName)) {
      this.__qin_field_parser.AssignmentPattern(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingProperty", ruleName)) {
      this.__qin_field_parser.BindingProperty(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingRestProperty", ruleName)) {
      this.__qin_field_parser.BindingRestProperty(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("PropertyName", ruleName)) {
      this.__qin_field_parser.PropertyName(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingPropertyList", ruleName)) {
      this.__qin_field_parser.BindingPropertyList(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingElisionElement", ruleName)) {
      this.__qin_field_parser.BindingElisionElement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingElementList", ruleName)) {
      this.__qin_field_parser.BindingElementList(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentProperty", ruleName)) {
      this.__qin_field_parser.AssignmentProperty(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("IdentifierReference", ruleName)) {
      this.__qin_field_parser.IdentifierReference(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentRestProperty", ruleName)) {
      this.__qin_field_parser.AssignmentRestProperty(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentRestElement", ruleName)) {
      this.__qin_field_parser.AssignmentRestElement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentPropertyList", ruleName)) {
      this.__qin_field_parser.AssignmentPropertyList(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentElisionElement", ruleName)) {
      this.__qin_field_parser.AssignmentElisionElement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentElement", ruleName)) {
      this.__qin_field_parser.AssignmentElement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentElementList", ruleName)) {
      this.__qin_field_parser.AssignmentElementList(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("FunctionStatementList", ruleName)) {
      this.__qin_field_parser.executeStaticRule(com_slime_parser_function_SlimeFunctionParser.__qin_field_STATIC_FUNCTION_GRAMMAR, "FunctionStatementList", this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("StatementList", ruleName)) {
      this.__qin_field_parser.StatementList(this.__qin_field_parser.functionStatementParams(this.__qin_field_effectiveParams));
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
