import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "../../../subhuti/parser/SubhutiStaticGrammar.ts";
import { com_slime_parser_expressions_SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser as SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime as PrimaryStaticRuntime } from "./SlimePrimaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar, com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar as SlimeUnaryExpressionStaticGrammar } from "./SlimeUnaryExpressionStaticGrammar.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../base/SlimeJavascriptParserBase.ts";
import { com_subhuti_struct_SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken as SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken$Builder } from "../../../subhuti/struct/SubhutiMatchToken.ts";
import { com_slime_parser_literal_SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser as SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime as LiteralStaticRuntime } from "../literal/SlimeLiteralParser.ts";
import { com_slime_parser_identifier_SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser as SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime as IdentifierStaticRuntime } from "../identifier/SlimeIdentifierParser.ts";
import { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser as SubhutiParser, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime as StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticPrefix, com_subhuti_parser_SubhutiParser$StaticPrefix as StaticPrefix, com_subhuti_parser_SubhutiParser$StaticChoice, com_subhuti_parser_SubhutiParser$StaticChoice as StaticChoice } from "../../../subhuti/parser/SubhutiParser.ts";
import { com_subhuti_parser_SubhutiParserFinal, com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal } from "../../../subhuti/parser/SubhutiParserFinal.ts";
import { com_subhuti_parser_SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators as SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl as StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher as StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext as AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext as AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes as PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates as StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame as ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames as CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys as CurrentTokenKeys } from "../../../subhuti/parser/SubhutiParserCombinators.ts";
import { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore as SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments as StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult as RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode as StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks as StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$CacheWork, com_subhuti_parser_SubhutiParserCore$CacheWork as CacheWork, com_subhuti_parser_SubhutiParserCore$FailureWork, com_subhuti_parser_SubhutiParserCore$FailureWork as FailureWork } from "../../../subhuti/parser/SubhutiParserCore.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations as ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException as SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException as SubhutiFirstTokenUnknownException } from "../../../subhuti/parser/SubhutiParserState.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../../../subhuti/lookahead/SubhutiTokenLookahead.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangInteger, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __qin_java_functional } from "@qin/java-sdk-js";
import { __qin_subhuti_rule_cache_key } from "@qin/java-sdk-js/tooling";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const Integer = __QinJavaLangInteger;
const UnsupportedOperationException = __QinJavaLangUnsupportedOperationException;
const RuntimeException = __QinJavaLangRuntimeException;
class com_slime_parser_expressions_SlimeUnaryExpressionParser extends com_slime_parser_expressions_SlimePrimaryExpressionParser {
  static __qin_field_STATIC_UNARY_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_expressions_SlimeUnaryExpressionParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeUnaryExpressionParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_expressions_SlimeUnaryExpressionParser_1_0(sourceCode: string): void {
    null;
  }
  UpdateExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_UpdateExpression(params);
    }), "UpdateExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_UpdateExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "UpdateExpression", this.unaryStaticRuntime(params));
    return null;
  }
  UnaryExpression(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_UnaryExpression_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_UnaryExpression_0_1();
    throw new Error("Unsupported Java overload: UnaryExpression/" + __qin_args.length);
  }
  __qin_overload_UnaryExpression_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_UnaryExpression_1_0(params);
    }), "UnaryExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_UnaryExpression_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "UnaryExpression", this.unaryStaticRuntime(params));
    return null;
  }
  __qin_overload_UnaryExpression_0_1(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_UnaryExpression_0_1();
    }), "UnaryExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_UnaryExpression_0_1(): any {
    this.UnaryExpression(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    return null;
  }
  UnaryOperator(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_UnaryOperator();
    }), "UnaryOperator", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_UnaryOperator(): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "UnaryOperator", this.unaryStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    return null;
  }
  unaryStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return new com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime(this, (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params));
  }
  isAssignmentExpressionParamsVariant(ruleName: string, variantKey: any): any {
    let integerVariant: any = null;
    return (__QinJavaLangString.equals("AssignmentExpression", ruleName) && (() => { const __qin_pattern_value = variantKey; return __qin_instanceof__(__qin_pattern_value, __QinJavaLangInteger) && (integerVariant = __qin_pattern_value, true); })() && __qin_binary__("==", integerVariant, 3.0));
  }
  callUnaryExternalStaticRule(ruleName: string, variantKey: any, params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    throw new __QinJavaLangUnsupportedOperationException(("unsupported unary static rule call: " + ruleName + "@" + variantKey));
  }
  canStartUnaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    return (this.canStartUpdateExpression(params, lookaheadOffset) || this.canStartUnaryOperator(lookaheadOffset) || this.canStartAwaitExpression(params, lookaheadOffset));
  }
  canStartUpdateExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
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
          return this.canStartLeftHandSideExpression(params, lookaheadOffset);
        }
      }
      return null;
    })();
  }
  canStartAwaitExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    return (params.__qin_await() && __qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("Await", this.LA(lookaheadOffset).tokenName()));
  }
  canStartLeftHandSideExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
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
          return (!params.__qin_yield());
        }
        case "Await": {
          return (!params.__qin_await());
        }
        default: {
          return false;
        }
      }
      return null;
    })();
  }
  canStartMemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    if (__QinJavaLangString.equals("New", tokenName)) {
      return true;
    }
    if (this.canStartPrimaryExpression(params, lookaheadOffset)) {
      return true;
    }
    return (this.canStartSuperProperty(lookaheadOffset) || this.canStartImportMeta(lookaheadOffset));
  }
  canStartPrimaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    return (() => {
      switch (tokenName) {
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
          return true;
        }
        case "Yield": {
          return (!params.__qin_yield());
        }
        case "Await": {
          return (!params.__qin_await());
        }
        default: {
          return false;
        }
      }
      return null;
    })();
  }
  canStartNewExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    if ((__qin_binary__("==", lookaheadOffset, 1.0) && this.canStartOptionalExpression())) {
      return false;
    }
    if ((__qin_binary__("==", lookaheadOffset, 1.0) && this.hasCallExpressionPostfixAhead())) {
      return false;
    }
    if (__QinJavaLangString.equals("New", this.tokenNameAt(lookaheadOffset))) {
      return true;
    }
    return this.canStartMemberExpression(params, lookaheadOffset);
  }
  canStartTSTypeParameterInstantiationStatic(): any {
    return false;
  }
  TSTypeParameterInstantiationStatic(): any {
    this.setParseFail();
    return null;
  }
  canStartTSNonNullExpressionTailStatic(): any {
    return false;
  }
  TSNonNullExpressionTailStatic(): any {
    this.setParseFail();
    return null;
  }
  canStartCallExpression(lookaheadOffset: number): any {
    return (__qin_binary__("==", lookaheadOffset, 1.0) && this.hasCallExpressionPostfixAhead());
  }
  canStartCoverCallExpressionAndAsyncArrowHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    return (__qin_binary__("==", lookaheadOffset, 1.0) && this.hasCallExpressionPostfixAhead() && this.canStartMemberExpression(params, lookaheadOffset) && !(__QinJavaLangString.equals("Import", this.tokenNameAt(lookaheadOffset)) && __QinJavaLangString.equals("LParen", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0)))));
  }
  canStartUnaryOperator(lookaheadOffset: number): any {
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
        default: {
          return false;
        }
      }
      return null;
    })();
  }
  canStartSuperProperty(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("Super", this.tokenNameAt(lookaheadOffset)) && (__QinJavaLangString.equals("LBracket", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) || __QinJavaLangString.equals("Dot", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0)))));
  }
  canStartMetaProperty(lookaheadOffset: number): any {
    return (this.canStartNewTarget(lookaheadOffset) || this.canStartImportMeta(lookaheadOffset));
  }
  canStartNewTarget(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("New", this.tokenNameAt(lookaheadOffset)) && __QinJavaLangString.equals("Dot", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && __qin_binary__("!=", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)), null) && __QinJavaLangString.equals("IdentifierName", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)).tokenName()) && __QinJavaLangString.equals("target", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)).value()));
  }
  canStartImportMeta(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("Import", this.tokenNameAt(lookaheadOffset)) && __QinJavaLangString.equals("Dot", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && __qin_binary__("!=", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)), null) && __QinJavaLangString.equals("IdentifierName", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)).tokenName()) && __QinJavaLangString.equals("meta", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)).value()));
  }
  canStartIdentifierNameStatic(lookaheadOffset: number): any {
    return this.canStartIdentifierNameToken(lookaheadOffset);
  }
  canStartTemplateLiteralAt(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("NoSubstitutionTemplate", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("TemplateHead", this.tokenNameAt(lookaheadOffset)));
  }
  canRecoverIncompleteMemberAccessAt(lookaheadOffset: number): any {
    if ((!this.isErrorRecoveryMode())) {
      return false;
    }
    if ((!__QinJavaLangString.equals("Dot", this.tokenNameAt(lookaheadOffset)))) {
      return false;
    }
    let next: any = this.safeLookahead(__qin_binary__("+", lookaheadOffset, 1.0));
    return (__qin_binary__("==", next, null) || next.isEof() || next.hasLineBreakBefore() || __QinJavaLangString.equals("RBrace", next.tokenName()) || __QinJavaLangString.equals("RParen", next.tokenName()) || __QinJavaLangString.equals("RBracket", next.tokenName()) || __QinJavaLangString.equals("Semicolon", next.tokenName()) || __QinJavaLangString.equals("Comma", next.tokenName()) || __QinJavaLangString.equals("EOF", next.tokenName()));
  }
  canStartDotMemberProperty(): any {
    if ((!__QinJavaLangString.equals("Dot", this.tokenNameAt(1.0)))) {
      return false;
    }
    return (this.canStartIdentifierNameToken(2.0) || __QinJavaLangString.equals("PrivateIdentifier", this.tokenNameAt(2.0)));
  }
  canStartDotIdentifierMemberProperty(): any {
    if ((!__QinJavaLangString.equals("Dot", this.tokenNameAt(1.0)) || !this.canStartIdentifierNameToken(2.0))) {
      return false;
    }
    let property: any = this.safeLookahead(2.0);
    return (!this.isErrorRecoveryMode() || __qin_binary__("==", property, null) || !property.hasLineBreakBefore());
  }
  canStartDotPrivateMemberProperty(): any {
    if ((!__QinJavaLangString.equals("Dot", this.tokenNameAt(1.0)) || !__QinJavaLangString.equals("PrivateIdentifier", this.tokenNameAt(2.0)))) {
      return false;
    }
    let property: any = this.safeLookahead(2.0);
    return (!this.isErrorRecoveryMode() || __qin_binary__("==", property, null) || !property.hasLineBreakBefore());
  }
  canStartArgumentListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("Ellipsis", this.tokenNameAt(lookaheadOffset)) || this.canStartAssignmentExpression(params.withIn(true), lookaheadOffset));
  }
  AwaitExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AwaitExpression(params);
    }), "AwaitExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AwaitExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "AwaitExpression", this.unaryStaticRuntime(params.withAwait(true)));
    return null;
  }
  LeftHandSideExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_LeftHandSideExpression(params);
    }), "LeftHandSideExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LeftHandSideExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "LeftHandSideExpression", this.unaryStaticRuntime(params));
    return null;
  }
  NewExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_NewExpression(params);
    }), "NewExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_NewExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "NewExpression", this.unaryStaticRuntime(params));
    return null;
  }
  MemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_MemberExpression(params);
    }), "MemberExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_MemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "MemberExpression", this.unaryStaticRuntime(params));
    return null;
  }
  SuperProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_SuperProperty(params);
    }), "SuperProperty", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_SuperProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "SuperProperty", this.unaryStaticRuntime(params));
    return null;
  }
  MetaProperty(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_MetaProperty();
    }), "MetaProperty", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_MetaProperty(): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "MetaProperty", this.unaryStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    return null;
  }
  NewTarget(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_NewTarget();
    }), "NewTarget", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_NewTarget(): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "NewTarget", this.unaryStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    return null;
  }
  ImportMeta(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ImportMeta();
    }), "ImportMeta", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportMeta(): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "ImportMeta", this.unaryStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    return null;
  }
  isContextual(value: string): any {
    return (__qin_binary__("!=", this.LA(1.0), null) && __QinJavaLangString.equals(value, this.LA(1.0).value()));
  }
  CallExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CallExpression(params);
    }), "CallExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CallExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "CallExpression", this.unaryStaticRuntime(params));
    return null;
  }
  CoverCallExpressionAndAsyncArrowHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CoverCallExpressionAndAsyncArrowHead(params);
    }), "CoverCallExpressionAndAsyncArrowHead", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoverCallExpressionAndAsyncArrowHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "CoverCallExpressionAndAsyncArrowHead", this.unaryStaticRuntime(params));
    return null;
  }
  CallMemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CallMemberExpression(params);
    }), "CallMemberExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CallMemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "CallMemberExpression", this.unaryStaticRuntime(params));
    return null;
  }
  SuperCall(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_SuperCall(params);
    }), "SuperCall", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_SuperCall(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "SuperCall", this.unaryStaticRuntime(params));
    return null;
  }
  ImportCall(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ImportCall(params);
    }), "ImportCall", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ImportCall(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "ImportCall", this.unaryStaticRuntime(params));
    return null;
  }
  Arguments(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Arguments(params);
    }), "Arguments", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Arguments(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "Arguments", this.unaryStaticRuntime(params));
    return null;
  }
  ArgumentList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArgumentList(params);
    }), "ArgumentList", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArgumentList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "ArgumentList", this.unaryStaticRuntime(params));
    return null;
  }
  ArgumentListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArgumentListItem(params);
    }), "ArgumentListItem", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArgumentListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "ArgumentListItem", this.unaryStaticRuntime(params));
    return null;
  }
  OptionalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalExpression(params);
    }), "OptionalExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_OptionalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "OptionalExpression", this.unaryStaticRuntime(params));
    return null;
  }
  canStartOptionalExpression(): any {
    let offset: any = this.offsetAfterOptionalExpressionHead(1.0);
    if (__qin_binary__("<", offset, 0.0)) {
      return false;
    }
    while (true) {
      let tokenName: any = this.tokenNameAt(offset);
      if (__qin_binary__("==", tokenName, null)) {
        return false;
      }
      if (__QinJavaLangString.equals("QuestionDot", tokenName)) {
        return true;
      }
      if (__QinJavaLangString.equals("Dot", tokenName)) {
        if ((this.canStartIdentifierNameToken(__qin_binary__("+", offset, 1.0)) || __QinJavaLangString.equals("PrivateIdentifier", this.tokenNameAt(__qin_binary__("+", offset, 1.0))))) {
          offset += 2.0;
          continue;
        }
        return false;
      }
      if ((__QinJavaLangString.equals("LBracket", tokenName) || __QinJavaLangString.equals("LParen", tokenName))) {
        let afterGroup: any = this.offsetAfterBalancedGroup(offset);
        if (__qin_binary__("<", afterGroup, 0.0)) {
          return false;
        }
        offset = afterGroup;
        continue;
      }
      if (__QinJavaLangString.equals("Less", tokenName)) {
        let afterTypeArguments: any = this.offsetAfterBalancedTypeArguments(offset);
        if (__qin_binary__("<", afterTypeArguments, 0.0)) {
          return false;
        }
        offset = afterTypeArguments;
        continue;
      }
      if (__QinJavaLangString.equals("LogicalNot", tokenName)) {
        offset++;
        continue;
      }
      if (__QinJavaLangString.equals("NoSubstitutionTemplate", tokenName)) {
        offset++;
        continue;
      }
      if (__QinJavaLangString.equals("TemplateHead", tokenName)) {
        return false;
      }
      return false;
    }
    return null;
  }
  offsetAfterOptionalExpressionHead(offset: number): any {
    let tokenName: any = this.tokenNameAt(offset);
    if (__qin_binary__("==", tokenName, null)) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    if ((__QinJavaLangString.equals("Super", tokenName) || __QinJavaLangString.equals("Import", tokenName))) {
      return __qin_binary__("+", offset, 1.0);
    }
    if (__QinJavaLangString.equals("New", tokenName)) {
      return __qin_binary__("+", offset, 1.0);
    }
    if ((__QinJavaLangString.equals("LParen", tokenName) || __QinJavaLangString.equals("LBracket", tokenName) || __QinJavaLangString.equals("LBrace", tokenName))) {
      return this.offsetAfterBalancedGroup(offset);
    }
    if (this.canStartPrimaryExpression(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT, offset)) {
      return __qin_binary__("+", offset, 1.0);
    }
    return __qin_binary__("-", 0.0, 1.0);
  }
  offsetAfterBalancedGroup(openOffset: number): any {
    let first: any = this.tokenNameAt(openOffset);
    if ((!__QinJavaLangString.equals("LParen", first) && !__QinJavaLangString.equals("LBracket", first) && !__QinJavaLangString.equals("LBrace", first))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    let parenDepth: any = 0.0;
    let bracketDepth: any = 0.0;
    let braceDepth: any = 0.0;
    for (let offset: any = openOffset; ; offset++) {
      let tokenName: any = this.tokenNameAt(offset);
      if (__qin_binary__("==", tokenName, null)) {
        return __qin_binary__("-", 0.0, 1.0);
      }
      switch (tokenName) {
        case "LParen": {
          parenDepth++;
        }
        case "RParen": {
          parenDepth--;
        }
        case "LBracket": {
          bracketDepth++;
        }
        case "RBracket": {
          bracketDepth--;
        }
        case "LBrace": {
          braceDepth++;
        }
        case "RBrace": {
          braceDepth--;
        }
        default: {
        }
      }
      if ((__qin_binary__("<", parenDepth, 0.0) || __qin_binary__("<", bracketDepth, 0.0) || __qin_binary__("<", braceDepth, 0.0))) {
        return __qin_binary__("-", 0.0, 1.0);
      }
      if ((__qin_binary__("==", parenDepth, 0.0) && __qin_binary__("==", bracketDepth, 0.0) && __qin_binary__("==", braceDepth, 0.0))) {
        return __qin_binary__("+", offset, 1.0);
      }
    }
    return null;
  }
  offsetAfterBalancedTypeArguments(lessOffset: number): any {
    if ((!__QinJavaLangString.equals("Less", this.tokenNameAt(lessOffset)))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    let angleDepth: any = 0.0;
    let parenDepth: any = 0.0;
    let bracketDepth: any = 0.0;
    let braceDepth: any = 0.0;
    for (let offset: any = lessOffset; ; offset++) {
      let tokenName: any = this.tokenNameAt(offset);
      if (__qin_binary__("==", tokenName, null)) {
        return __qin_binary__("-", 0.0, 1.0);
      }
      switch (tokenName) {
        case "Less": {
          angleDepth++;
        }
        case "Greater": {
          angleDepth--;
        }
        case "RightShift": {
          angleDepth -= 2.0;
        }
        case "UnsignedRightShift": {
          angleDepth -= 3.0;
        }
        case "LParen": {
          parenDepth++;
        }
        case "RParen": {
          parenDepth--;
        }
        case "LBracket": {
          bracketDepth++;
        }
        case "RBracket": {
          bracketDepth--;
        }
        case "LBrace": {
          braceDepth++;
        }
        case "RBrace": {
          braceDepth--;
        }
        case "GreaterEqual": {
        }
        case "RightShiftAssign": {
        }
        case "UnsignedRightShiftAssign": {
          return __qin_binary__("-", 0.0, 1.0);
        }
        default: {
        }
      }
      if ((__qin_binary__("<", angleDepth, 0.0) || __qin_binary__("<", parenDepth, 0.0) || __qin_binary__("<", bracketDepth, 0.0) || __qin_binary__("<", braceDepth, 0.0))) {
        return __qin_binary__("-", 0.0, 1.0);
      }
      if ((__qin_binary__("==", angleDepth, 0.0) && __qin_binary__("==", parenDepth, 0.0) && __qin_binary__("==", bracketDepth, 0.0) && __qin_binary__("==", braceDepth, 0.0))) {
        return __qin_binary__("+", offset, 1.0);
      }
    }
    return null;
  }
  safeLookahead(offset: number): any {
    try {
      return this.LA(offset);
    } catch (error) {
      if (!(error instanceof __QinJavaLangRuntimeException)) {
        throw error;
      }
      return null;
    }
    return null;
  }
  OptionalChain(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalChain(params);
    }), "OptionalChain", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_OptionalChain(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "OptionalChain", this.unaryStaticRuntime(params));
    return null;
  }
}
const SlimeUnaryExpressionParser = com_slime_parser_expressions_SlimeUnaryExpressionParser;
class com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime {
  __qin_field_parser: com_slime_parser_expressions_SlimeUnaryExpressionParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_expressions_SlimeUnaryExpressionParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const effectiveParams: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime_2_0(parser, effectiveParams);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeUnaryExpressionParser$UnaryStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime_2_0(parser: com_slime_parser_expressions_SlimeUnaryExpressionParser, effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = effectiveParams;
  }
  testStaticGate(gateId: string): any {
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_AWAIT_CONTEXT, gateId)) {
      return this.__qin_field_effectiveParams.__qin_await();
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_NO_LINE_BREAK, gateId)) {
      return (!this.__qin_field_parser.lookaheadHasLineBreak());
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_TEMPLATE_LITERAL, gateId)) {
      return this.__qin_field_parser.canStartTemplateLiteral();
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_DOT_MEMBER_PROPERTY, gateId)) {
      return this.__qin_field_parser.canStartDotMemberProperty();
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_DOT_IDENTIFIER_MEMBER_PROPERTY, gateId)) {
      return this.__qin_field_parser.canStartDotIdentifierMemberProperty();
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_DOT_PRIVATE_MEMBER_PROPERTY, gateId)) {
      return this.__qin_field_parser.canStartDotPrivateMemberProperty();
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_INCOMPLETE_MEMBER_ACCESS, gateId)) {
      return this.__qin_field_parser.canRecoverIncompleteMemberAccess();
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_NOT_NEW_EXPRESSION, gateId)) {
      return (!__QinJavaLangString.equals("New", this.__qin_field_parser.tokenNameAt(1.0)));
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_META_PROPERTY_START, gateId)) {
      return this.__qin_field_parser.canStartMetaProperty(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_OPTIONAL_EXPRESSION_START, gateId)) {
      return this.__qin_field_parser.canStartOptionalExpression();
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_CALL_EXPRESSION_HEAD_BEFORE_OPTIONAL_CHAIN, gateId)) {
      return this.__qin_field_parser.hasCallExpressionPostfixAhead();
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_MEMBER_EXPRESSION_HEAD_BEFORE_OPTIONAL_CHAIN, gateId)) {
      return (this.__qin_field_parser.canStartMemberExpression(this.__qin_field_effectiveParams, 1.0) && !this.__qin_field_parser.hasCallExpressionPostfixAhead());
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_TS_TYPE_PARAMETER_INSTANTIATION, gateId)) {
      return this.__qin_field_parser.canStartTSTypeParameterInstantiationStatic();
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_TS_NON_NULL, gateId)) {
      return this.__qin_field_parser.canStartTSNonNullExpressionTailStatic();
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_ARGUMENT_LIST_START, gateId)) {
      return this.__qin_field_parser.canStartArgumentListItem(this.__qin_field_effectiveParams, 1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_ARGUMENT_LIST_CONTINUATION, gateId)) {
      return (__QinJavaLangString.equals("Comma", this.__qin_field_parser.tokenNameAt(1.0)) && this.__qin_field_parser.canStartArgumentListItem(this.__qin_field_effectiveParams, 2.0));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported unary static gate: " + gateId));
  }
  runStaticAction(actionId: string): any {
    throw new __QinJavaLangUnsupportedOperationException(("unsupported unary static action: " + actionId));
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
    if ((__qin_binary__("!=", variantKey, null) && !this.__qin_field_parser.isAssignmentExpressionParamsVariant(ruleName, variantKey))) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported unary static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("UpdateExpression", ruleName)) {
      return this.__qin_field_parser.canStartUpdateExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("UnaryExpression", ruleName)) {
      return this.__qin_field_parser.canStartUnaryExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AwaitExpression", ruleName)) {
      return this.__qin_field_parser.canStartAwaitExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSTypeAssertion", ruleName)) {
      return __QinJavaLangString.equals("Less", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("LeftHandSideExpression", ruleName)) {
      return this.__qin_field_parser.canStartLeftHandSideExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("OptionalExpression", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && this.__qin_field_parser.canStartOptionalExpression());
    }
    if (__QinJavaLangString.equals("UnaryOperator", ruleName)) {
      return this.__qin_field_parser.canStartUnaryOperator(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("SuperProperty", ruleName)) {
      return this.__qin_field_parser.canStartSuperProperty(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("MetaProperty", ruleName)) {
      return this.__qin_field_parser.canStartMetaProperty(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("NewTarget", ruleName)) {
      return this.__qin_field_parser.canStartNewTarget(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("ImportMeta", ruleName)) {
      return this.__qin_field_parser.canStartImportMeta(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("PrimaryExpression", ruleName)) {
      return this.__qin_field_parser.canStartPrimaryExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("MemberExpression", ruleName)) {
      return this.__qin_field_parser.canStartMemberExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("NewExpression", ruleName)) {
      return this.__qin_field_parser.canStartNewExpression(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("CallExpression", ruleName)) {
      return this.__qin_field_parser.canStartCallExpression(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("CoverCallExpressionAndAsyncArrowHead", ruleName)) {
      return this.__qin_field_parser.canStartCoverCallExpressionAndAsyncArrowHead(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("CallMemberExpression", ruleName)) {
      return this.__qin_field_parser.canStartCoverCallExpressionAndAsyncArrowHead(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("SuperCall", ruleName)) {
      return (__QinJavaLangString.equals("Super", this.__qin_field_parser.tokenNameAt(lookaheadOffset)) && __QinJavaLangString.equals("LParen", this.__qin_field_parser.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
    }
    if (__QinJavaLangString.equals("ImportCall", ruleName)) {
      return (__QinJavaLangString.equals("Import", this.__qin_field_parser.tokenNameAt(lookaheadOffset)) && __QinJavaLangString.equals("LParen", this.__qin_field_parser.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
    }
    if (__QinJavaLangString.equals("Arguments", ruleName)) {
      return __QinJavaLangString.equals("LParen", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("ArgumentList", ruleName)) {
      return this.__qin_field_parser.canStartArgumentListItem(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("ArgumentListItem", ruleName)) {
      return this.__qin_field_parser.canStartArgumentListItem(this.__qin_field_effectiveParams, lookaheadOffset);
    }
    if (__QinJavaLangString.equals("AssignmentExpression", ruleName)) {
      return this.__qin_field_parser.canStartUnaryExpression(this.__qin_field_effectiveParams.withIn(true), lookaheadOffset);
    }
    if (__QinJavaLangString.equals("Expression", ruleName)) {
      return this.__qin_field_parser.canStartUnaryExpression(this.__qin_field_effectiveParams.withIn(true), lookaheadOffset);
    }
    if (__QinJavaLangString.equals("IdentifierName", ruleName)) {
      return this.__qin_field_parser.canStartIdentifierNameStatic(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("PrivateIdentifier", ruleName)) {
      return __QinJavaLangString.equals("PrivateIdentifier", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TemplateLiteral", ruleName)) {
      return this.__qin_field_parser.canStartTemplateLiteralAt(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("IncompleteMemberAccessProperty", ruleName)) {
      return this.__qin_field_parser.canRecoverIncompleteMemberAccessAt(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("OptionalChain", ruleName)) {
      return __QinJavaLangString.equals("QuestionDot", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeParameterInstantiation", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && this.__qin_field_parser.canStartTSTypeParameterInstantiationStatic());
    }
    if (__QinJavaLangString.equals("TSNonNullExpressionTail", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && this.__qin_field_parser.canStartTSNonNullExpressionTailStatic());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported unary static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if ((__qin_binary__("!=", variantKey, null) && !this.__qin_field_parser.isAssignmentExpressionParamsVariant(ruleName, variantKey))) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported unary static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("UpdateExpression", ruleName)) {
      this.__qin_field_parser.UpdateExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("UnaryExpression", ruleName)) {
      this.__qin_field_parser.UnaryExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AwaitExpression", ruleName)) {
      this.__qin_field_parser.AwaitExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("LeftHandSideExpression", ruleName)) {
      this.__qin_field_parser.LeftHandSideExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("OptionalExpression", ruleName)) {
      this.__qin_field_parser.OptionalExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("UnaryOperator", ruleName)) {
      this.__qin_field_parser.UnaryOperator();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("SuperProperty", ruleName)) {
      this.__qin_field_parser.SuperProperty(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("MetaProperty", ruleName)) {
      this.__qin_field_parser.MetaProperty();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("NewTarget", ruleName)) {
      this.__qin_field_parser.NewTarget();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ImportMeta", ruleName)) {
      this.__qin_field_parser.ImportMeta();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("PrimaryExpression", ruleName)) {
      this.__qin_field_parser.PrimaryExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("NewExpression", ruleName)) {
      this.__qin_field_parser.NewExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CallExpression", ruleName)) {
      this.__qin_field_parser.CallExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("MemberExpression", ruleName)) {
      this.__qin_field_parser.MemberExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CoverCallExpressionAndAsyncArrowHead", ruleName)) {
      this.__qin_field_parser.CoverCallExpressionAndAsyncArrowHead(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CallMemberExpression", ruleName)) {
      this.__qin_field_parser.CallMemberExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("SuperCall", ruleName)) {
      this.__qin_field_parser.SuperCall(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ImportCall", ruleName)) {
      this.__qin_field_parser.ImportCall(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Arguments", ruleName)) {
      this.__qin_field_parser.Arguments(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ArgumentList", ruleName)) {
      this.__qin_field_parser.ArgumentList(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ArgumentListItem", ruleName)) {
      this.__qin_field_parser.ArgumentListItem(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentExpression", ruleName)) {
      this.__qin_field_parser.AssignmentExpression(this.__qin_field_effectiveParams.withIn(true));
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Expression", ruleName)) {
      this.__qin_field_parser.Expression(this.__qin_field_effectiveParams.withIn(true));
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("IdentifierName", ruleName)) {
      this.__qin_field_parser.IdentifierName();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("PrivateIdentifier", ruleName)) {
      this.__qin_field_parser.PrivateIdentifier();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TemplateLiteral", ruleName)) {
      this.__qin_field_parser.TemplateLiteral(new com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams(this.__qin_field_effectiveParams, true));
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("IncompleteMemberAccessProperty", ruleName)) {
      this.__qin_field_parser.IncompleteMemberAccessProperty();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("OptionalChain", ruleName)) {
      this.__qin_field_parser.OptionalChain(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeParameterInstantiation", ruleName)) {
      this.__qin_field_parser.TSTypeParameterInstantiationStatic();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSNonNullExpressionTail", ruleName)) {
      this.__qin_field_parser.TSNonNullExpressionTailStatic();
      return (!this.__qin_field_parser.isParserFail());
    }
    return this.__qin_field_parser.callUnaryExternalStaticRule(ruleName, variantKey, this.__qin_field_effectiveParams);
  }
}
com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeUnaryExpressionParser$UnaryStaticRuntime = com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime;
com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR = com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.grammar();

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_expressions_SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime };
