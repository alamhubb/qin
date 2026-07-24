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
  ExponentiationExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ExponentiationExpression(params);
    }), "ExponentiationExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ExponentiationExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "ExponentiationExpression", this.binaryStaticRuntime(params));
    return null;
  }
  static unaryExpressionPrefixTokens(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return (params.__qin_await() ? com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_UNARY_EXPRESSION_AWAIT_PREFIX_TOKENS : com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_UNARY_EXPRESSION_PREFIX_TOKENS);
  }
  MultiplicativeExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_MultiplicativeExpression(params);
    }), "MultiplicativeExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_MultiplicativeExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "MultiplicativeExpression", this.binaryStaticRuntime(params));
    return null;
  }
  MultiplicativeOperator(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_MultiplicativeOperator();
    }), "MultiplicativeOperator", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_MultiplicativeOperator(): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "MultiplicativeOperator", this.binaryStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    return null;
  }
  binaryStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return new com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime(this, (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params));
  }
  canStartExponentiationExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    return (this.canStartBinaryUpdateExpression(params, lookaheadOffset) || this.canStartUnaryExpressionPrefix(params, lookaheadOffset));
  }
  canStartBinaryUnaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    return this.canStartUnaryExpressionPrefix(params, lookaheadOffset);
  }
  canStartBinaryUpdateExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    let token: any = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    return (() => {
      switch (token.tokenName()) {
        case "Increment": {
        }
        case "Decrement": {
          return true;
        }
        default: {
          return this.canStartBinaryLeftHandSideExpression(params, lookaheadOffset);
        }
      }
      return null;
    })();
  }
  canStartUnaryExpressionPrefix(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    let token: any = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    return (() => {
      switch (token.tokenName()) {
        case "Delete": {
        }
        case "Void": {
        }
        case "Typeof": {
        }
        case "Plus": {
        }
        case "Minus": {
        }
        case "BitwiseNot": {
        }
        case "LogicalNot": {
          return true;
        }
        case "Await": {
          return (__qin_binary__("!=", params, null) && params.__qin_await());
        }
        default: {
          return false;
        }
      }
      return null;
    })();
  }
  canStartBinaryLeftHandSideExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    let effectiveParams: any = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params);
    let token: any = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    return (() => {
      switch (token.tokenName()) {
        case "This": {
        }
        case "IdentifierName": {
        }
        case "NullLiteral": {
        }
        case "True": {
        }
        case "False": {
        }
        case "NumericLiteral": {
        }
        case "StringLiteral": {
        }
        case "Function": {
        }
        case "Class": {
        }
        case "LBracket": {
        }
        case "LBrace": {
        }
        case "RegularExpressionLiteral": {
        }
        case "NoSubstitutionTemplate": {
        }
        case "TemplateHead": {
        }
        case "LParen": {
        }
        case "Super": {
        }
        case "Import": {
        }
        case "New": {
          return true;
        }
        case "Yield": {
          return (!effectiveParams.__qin_yield());
        }
        case "Await": {
          return (!effectiveParams.__qin_await());
        }
        default: {
          return false;
        }
      }
      return null;
    })();
  }
  canStartRelationalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    return (((__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params).__qin_in() && this.lookahead("PrivateIdentifier", lookaheadOffset)) || this.canStartExponentiationExpression((__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params), lookaheadOffset));
  }
  canStartShortCircuitExpressionTail(lookaheadOffset: number): any {
    return (this.lookahead("LogicalOr", lookaheadOffset) || this.lookahead("NullishCoalescing", lookaheadOffset));
  }
  canStartMultiplicativeOperator(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && (() => {
      switch (this.LA(lookaheadOffset).tokenName()) {
        case "Asterisk": {
        }
        case "Slash": {
        }
        case "Modulo": {
          return true;
        }
        default: {
          return false;
        }
      }
      return null;
    })());
  }
  AdditiveExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AdditiveExpression(params);
    }), "AdditiveExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AdditiveExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "AdditiveExpression", this.binaryStaticRuntime(params));
    return null;
  }
  ShiftExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ShiftExpression(params);
    }), "ShiftExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ShiftExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "ShiftExpression", this.binaryStaticRuntime(params));
    return null;
  }
  RelationalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_RelationalExpression(params);
    }), "RelationalExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_RelationalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "RelationalExpression", this.binaryStaticRuntime(params));
    return null;
  }
  EqualityExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_EqualityExpression(params);
    }), "EqualityExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_EqualityExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "EqualityExpression", this.binaryStaticRuntime(params));
    return null;
  }
  BitwiseANDExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BitwiseANDExpression(params);
    }), "BitwiseANDExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BitwiseANDExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "BitwiseANDExpression", this.binaryStaticRuntime(params));
    return null;
  }
  BitwiseXORExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BitwiseXORExpression(params);
    }), "BitwiseXORExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BitwiseXORExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "BitwiseXORExpression", this.binaryStaticRuntime(params));
    return null;
  }
  BitwiseORExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BitwiseORExpression(params);
    }), "BitwiseORExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BitwiseORExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "BitwiseORExpression", this.binaryStaticRuntime(params));
    return null;
  }
  LogicalANDExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_LogicalANDExpression(params);
    }), "LogicalANDExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LogicalANDExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "LogicalANDExpression", this.binaryStaticRuntime(params));
    return null;
  }
  LogicalORExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_LogicalORExpression(params);
    }), "LogicalORExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LogicalORExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "LogicalORExpression", this.binaryStaticRuntime(params));
    return null;
  }
  CoalesceExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CoalesceExpression(params);
    }), "CoalesceExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoalesceExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "CoalesceExpression", this.binaryStaticRuntime(params));
    return null;
  }
  CoalesceExpressionHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CoalesceExpressionHead(params);
    }), "CoalesceExpressionHead", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoalesceExpressionHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "CoalesceExpressionHead", this.binaryStaticRuntime(params));
    return null;
  }
  ShortCircuitExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ShortCircuitExpression(params);
    }), "ShortCircuitExpression", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ShortCircuitExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "ShortCircuitExpression", this.binaryStaticRuntime(params));
    return null;
  }
  ShortCircuitExpressionTail(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ShortCircuitExpressionTail(params);
    }), "ShortCircuitExpressionTail", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ShortCircuitExpressionTail(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "ShortCircuitExpressionTail", this.binaryStaticRuntime(params));
    return null;
  }
  LogicalORExpressionTail(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_LogicalORExpressionTail(params);
    }), "LogicalORExpressionTail", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LogicalORExpressionTail(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "LogicalORExpressionTail", this.binaryStaticRuntime(params));
    return null;
  }
  CoalesceExpressionTail(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CoalesceExpressionTail(params);
    }), "CoalesceExpressionTail", "SlimeBinaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoalesceExpressionTail(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeBinaryExpressionParser.__qin_field_STATIC_BINARY_GRAMMAR, "CoalesceExpressionTail", this.binaryStaticRuntime(params));
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
  testStaticGate(gateId: string): any {
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeBinaryExpressionStaticGrammar.__qin_field_GATE_PRIVATE_IDENTIFIER_IN, gateId)) {
      return (this.__qin_field_effectiveParams.__qin_in() && this.__qin_field_parser.lookahead("PrivateIdentifier", 1.0));
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeBinaryExpressionStaticGrammar.__qin_field_GATE_UNARY_EXPRESSION_PREFIX, gateId)) {
      return this.__qin_field_parser.canStartUnaryExpressionPrefix(this.__qin_field_effectiveParams, 1.0);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported binary static gate: " + gateId));
  }
  runStaticAction(actionId: string): any {
    throw new __QinJavaLangUnsupportedOperationException(("unsupported binary static action: " + actionId));
  }
  canStartStaticRule(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported binary static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("UpdateExpression", ruleName)) {
      this.__qin_field_parser.UpdateExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("UnaryExpression", ruleName)) {
      this.__qin_field_parser.UnaryExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ExponentiationExpression", ruleName)) {
      this.__qin_field_parser.ExponentiationExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("MultiplicativeOperator", ruleName)) {
      this.__qin_field_parser.MultiplicativeOperator();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("MultiplicativeExpression", ruleName)) {
      this.__qin_field_parser.MultiplicativeExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AdditiveExpression", ruleName)) {
      this.__qin_field_parser.AdditiveExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ShiftExpression", ruleName)) {
      this.__qin_field_parser.ShiftExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("RelationalExpression", ruleName)) {
      this.__qin_field_parser.RelationalExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("EqualityExpression", ruleName)) {
      this.__qin_field_parser.EqualityExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BitwiseANDExpression", ruleName)) {
      this.__qin_field_parser.BitwiseANDExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BitwiseXORExpression", ruleName)) {
      this.__qin_field_parser.BitwiseXORExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BitwiseORExpression", ruleName)) {
      this.__qin_field_parser.BitwiseORExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("LogicalANDExpression", ruleName)) {
      this.__qin_field_parser.LogicalANDExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("LogicalORExpression", ruleName)) {
      this.__qin_field_parser.LogicalORExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CoalesceExpression", ruleName)) {
      this.__qin_field_parser.CoalesceExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CoalesceExpressionHead", ruleName)) {
      this.__qin_field_parser.CoalesceExpressionHead(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ShortCircuitExpression", ruleName)) {
      this.__qin_field_parser.ShortCircuitExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ShortCircuitExpressionTail", ruleName)) {
      this.__qin_field_parser.ShortCircuitExpressionTail(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("LogicalORExpressionTail", ruleName)) {
      this.__qin_field_parser.LogicalORExpressionTail(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CoalesceExpressionTail", ruleName)) {
      this.__qin_field_parser.CoalesceExpressionTail(this.__qin_field_effectiveParams);
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
