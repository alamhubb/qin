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
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangNumber, __QinJavaLangDouble, __qin_java_implements, __QinJavaLangInteger, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __qin_java_functional } from "@qin/java-sdk-js";
import { __qin_subhuti_rule_cache_key } from "@qin/java-sdk-js/tooling";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const Double = __QinJavaLangDouble;
const Integer = __QinJavaLangInteger;
const NumberFormatException = __QinJavaLangNumberFormatException;
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
  UpdateExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_UpdateExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_UpdateExpression.call(this, params);
    }), "UpdateExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_UpdateExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_491: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_491.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "UpdateExpression", this.unaryStaticRuntime(params));
    }
    return null;
  }
  UnaryExpression(...__qin_args: any[]): void {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_UnaryExpression_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_UnaryExpression_0_1();
    throw new Error("Unsupported Java overload: UnaryExpression/" + __qin_args.length);
  }
  __qin_overload_UnaryExpression_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw___qin_overload_UnaryExpression_1_0 receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw___qin_overload_UnaryExpression_1_0.call(this, params);
    }), "UnaryExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_UnaryExpression_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_492: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_492.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "UnaryExpression", this.unaryStaticRuntime(params));
    }
    return null;
  }
  __qin_overload_UnaryExpression_0_1(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw___qin_overload_UnaryExpression_0_1 receiver=this arity=0 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw___qin_overload_UnaryExpression_0_1.call(this);
    }), "UnaryExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_UnaryExpression_0_1(): void {
    {
      const __qin_typed_receiver_493: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_493.UnaryExpression(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    }
    return null;
  }
  UnaryOperator(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_UnaryOperator receiver=this arity=0 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_UnaryOperator.call(this);
    }), "UnaryOperator", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_UnaryOperator(): void {
    {
      const __qin_typed_receiver_494: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_494.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "UnaryOperator", this.unaryStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    }
    return null;
  }
  unaryStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime(this, (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params));
  }
  isAssignmentExpressionParamsVariant(ruleName: string, variantKey: any): boolean {
    return __qin_binary__("!=", this.assignmentExpressionParamsForVariant(ruleName, variantKey, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT), null);
  }
  assignmentExpressionParamsForVariant(ruleName: string, variantKey: any, baseParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams {
    if ((!__QinJavaLangString.equals("AssignmentExpression", ruleName))) {
      return null;
    }
    if (__qin_binary__("==", variantKey, null)) {
      return null;
    }
    if (com_slime_parser_expressions_SlimeUnaryExpressionParser.isIntegralVariant(variantKey, 3.0)) {
      let effectiveBase: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams = (__qin_binary__("==", baseParams, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : baseParams);
      return effectiveBase.withIn(true);
    }
    return null;
  }
  static isIntegralVariant(variantKey: any, expected: number): boolean {
    let number: __QinJavaLangNumber = null as any;
    let text: any = null as any;
    if ((__qin_instanceof__(variantKey, __QinJavaLangNumber) && (number = variantKey, true))) {
      const __qin_pattern_number = number;
      let number: __QinJavaLangNumber = __qin_pattern_number as any;
      return (__qin_binary__("==", __QinJavaLangNumber.intValue(number), expected) && __qin_binary__("==", __QinJavaLangDouble.compare(__QinJavaLangNumber.doubleValue(number), expected), 0.0));
    }
    if ((__qin_java_implements(variantKey, "java.lang.CharSequence") && (text = variantKey, true))) {
      const __qin_pattern_text = text;
      let text: any = __qin_pattern_text as any;
      try {
        return __qin_binary__("==", __QinJavaLangInteger.parseInt(text.toString()), expected);
      } catch (ignored) {
        if (!(ignored instanceof __QinJavaLangNumberFormatException)) {
          throw ignored;
        }
        return false;
      }
    }
    return false;
  }
  callUnaryExternalStaticRule(ruleName: string, variantKey: any, params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): boolean {
    throw new __QinJavaLangUnsupportedOperationException(("unsupported unary static rule call: " + ruleName + "@" + variantKey));
  }
  canStartUnaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    return (this.canStartUpdateExpression(params, lookaheadOffset) || this.canStartUnaryOperator(lookaheadOffset) || this.canStartAwaitExpression(params, lookaheadOffset));
  }
  canStartUpdateExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    return (() => {
      const __switch_discriminant = token.tokenName();
      if (__switch_discriminant === "Increment" || __switch_discriminant === "Decrement") {
        return true;
      }
      else {
        return this.canStartLeftHandSideExpression(params, lookaheadOffset);
      }
      return null;
    })();
  }
  canStartAwaitExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    return (params.await() && __qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("Await", this.LA(lookaheadOffset).tokenName()));
  }
  canStartLeftHandSideExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    return (() => {
      const __switch_discriminant = token.tokenName();
      if (__switch_discriminant === "This" || __switch_discriminant === "IdentifierName" || __switch_discriminant === "NullLiteral" || __switch_discriminant === "True" || __switch_discriminant === "False" || __switch_discriminant === "NumericLiteral" || __switch_discriminant === "StringLiteral" || __switch_discriminant === "Function" || __switch_discriminant === "Class" || __switch_discriminant === "LBracket" || __switch_discriminant === "LBrace" || __switch_discriminant === "RegularExpressionLiteral" || __switch_discriminant === "NoSubstitutionTemplate" || __switch_discriminant === "TemplateHead" || __switch_discriminant === "LParen" || __switch_discriminant === "Super" || __switch_discriminant === "Import" || __switch_discriminant === "New") {
        return true;
      }
      else if (__switch_discriminant === "Yield") {
        return (!params.yield());
      }
      else if (__switch_discriminant === "Await") {
        return (!params.await());
      }
      else {
        return false;
      }
      return null;
    })();
  }
  canStartMemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
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
  canStartPrimaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    return (() => {
      const __switch_discriminant = tokenName;
      if (__switch_discriminant === "This" || __switch_discriminant === "IdentifierName" || __switch_discriminant === "NullLiteral" || __switch_discriminant === "True" || __switch_discriminant === "False" || __switch_discriminant === "NumericLiteral" || __switch_discriminant === "StringLiteral" || __switch_discriminant === "Function" || __switch_discriminant === "Class" || __switch_discriminant === "LBracket" || __switch_discriminant === "LBrace" || __switch_discriminant === "RegularExpressionLiteral" || __switch_discriminant === "NoSubstitutionTemplate" || __switch_discriminant === "TemplateHead" || __switch_discriminant === "LParen") {
        return true;
      }
      else if (__switch_discriminant === "Yield") {
        return (!params.yield());
      }
      else if (__switch_discriminant === "Await") {
        return (!params.await());
      }
      else {
        return false;
      }
      return null;
    })();
  }
  canStartNewExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
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
  canStartTSTypeParameterInstantiationStatic(): boolean {
    return false;
  }
  TSTypeParameterInstantiationStatic(): void {
    {
      const __qin_typed_receiver_495: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_495.setParseFail();
    }
    return null;
  }
  canStartTSNonNullExpressionTailStatic(): boolean {
    return false;
  }
  TSNonNullExpressionTailStatic(): void {
    {
      const __qin_typed_receiver_496: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_496.setParseFail();
    }
    return null;
  }
  canStartCallExpression(lookaheadOffset: number): boolean {
    return (__qin_binary__("==", lookaheadOffset, 1.0) && this.hasCallExpressionPostfixAhead());
  }
  canStartCoverCallExpressionAndAsyncArrowHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    return (__qin_binary__("==", lookaheadOffset, 1.0) && this.hasCallExpressionPostfixAhead() && this.canStartMemberExpression(params, lookaheadOffset) && !(__QinJavaLangString.equals("Import", this.tokenNameAt(lookaheadOffset)) && __QinJavaLangString.equals("LParen", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0)))));
  }
  canStartUnaryOperator(lookaheadOffset: number): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    return (() => {
      const __switch_discriminant = token.tokenName();
      if (__switch_discriminant === "Delete" || __switch_discriminant === "Void" || __switch_discriminant === "Typeof" || __switch_discriminant === "Plus" || __switch_discriminant === "Minus" || __switch_discriminant === "BitwiseNot" || __switch_discriminant === "LogicalNot") {
        return true;
      }
      else {
        return false;
      }
      return null;
    })();
  }
  canStartSuperProperty(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Super", this.tokenNameAt(lookaheadOffset)) && (__QinJavaLangString.equals("LBracket", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) || __QinJavaLangString.equals("Dot", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0)))));
  }
  canStartMetaProperty(lookaheadOffset: number): boolean {
    return (this.canStartNewTarget(lookaheadOffset) || this.canStartImportMeta(lookaheadOffset));
  }
  canStartNewTarget(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("New", this.tokenNameAt(lookaheadOffset)) && __QinJavaLangString.equals("Dot", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && __qin_binary__("!=", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)), null) && __QinJavaLangString.equals("IdentifierName", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)).tokenName()) && __QinJavaLangString.equals("target", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)).value()));
  }
  canStartImportMeta(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Import", this.tokenNameAt(lookaheadOffset)) && __QinJavaLangString.equals("Dot", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && __qin_binary__("!=", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)), null) && __QinJavaLangString.equals("IdentifierName", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)).tokenName()) && __QinJavaLangString.equals("meta", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)).value()));
  }
  canStartIdentifierNameStatic(lookaheadOffset: number): boolean {
    return this.canStartIdentifierNameToken(lookaheadOffset);
  }
  canStartTemplateLiteralAt(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("NoSubstitutionTemplate", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("TemplateHead", this.tokenNameAt(lookaheadOffset)));
  }
  canRecoverIncompleteMemberAccessAt(lookaheadOffset: number): boolean {
    if ((!this.isErrorRecoveryMode())) {
      return false;
    }
    if ((!__QinJavaLangString.equals("Dot", this.tokenNameAt(lookaheadOffset)))) {
      return false;
    }
    let next: com_subhuti_struct_SubhutiMatchToken = this.safeLookahead(__qin_binary__("+", lookaheadOffset, 1.0));
    return (__qin_binary__("==", next, null) || next.isEof() || next.hasLineBreakBefore() || __QinJavaLangString.equals("RBrace", next.tokenName()) || __QinJavaLangString.equals("RParen", next.tokenName()) || __QinJavaLangString.equals("RBracket", next.tokenName()) || __QinJavaLangString.equals("Semicolon", next.tokenName()) || __QinJavaLangString.equals("Comma", next.tokenName()) || __QinJavaLangString.equals("EOF", next.tokenName()));
  }
  canStartDotMemberProperty(): boolean {
    if ((!__QinJavaLangString.equals("Dot", this.tokenNameAt(1.0)))) {
      return false;
    }
    return (this.canStartIdentifierNameToken(2.0) || __QinJavaLangString.equals("PrivateIdentifier", this.tokenNameAt(2.0)));
  }
  canStartDotIdentifierMemberProperty(): boolean {
    if ((!__QinJavaLangString.equals("Dot", this.tokenNameAt(1.0)) || !this.canStartIdentifierNameToken(2.0))) {
      return false;
    }
    let property: com_subhuti_struct_SubhutiMatchToken = this.safeLookahead(2.0);
    return (!this.isErrorRecoveryMode() || __qin_binary__("==", property, null) || !property.hasLineBreakBefore());
  }
  canStartDotPrivateMemberProperty(): boolean {
    if ((!__QinJavaLangString.equals("Dot", this.tokenNameAt(1.0)) || !__QinJavaLangString.equals("PrivateIdentifier", this.tokenNameAt(2.0)))) {
      return false;
    }
    let property: com_subhuti_struct_SubhutiMatchToken = this.safeLookahead(2.0);
    return (!this.isErrorRecoveryMode() || __qin_binary__("==", property, null) || !property.hasLineBreakBefore());
  }
  canStartArgumentListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Ellipsis", this.tokenNameAt(lookaheadOffset)) || this.canStartAssignmentExpression(params.withIn(true), lookaheadOffset));
  }
  AwaitExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_AwaitExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_AwaitExpression.call(this, params);
    }), "AwaitExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AwaitExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_497: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_497.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "AwaitExpression", this.unaryStaticRuntime(params.withAwait(true)));
    }
    return null;
  }
  LeftHandSideExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_LeftHandSideExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_LeftHandSideExpression.call(this, params);
    }), "LeftHandSideExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LeftHandSideExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_498: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_498.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "LeftHandSideExpression", this.unaryStaticRuntime(params));
    }
    return null;
  }
  NewExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_NewExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_NewExpression.call(this, params);
    }), "NewExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_NewExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_499: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_499.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "NewExpression", this.unaryStaticRuntime(params));
    }
    return null;
  }
  MemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_MemberExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_MemberExpression.call(this, params);
    }), "MemberExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_MemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_500: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_500.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "MemberExpression", this.unaryStaticRuntime(params));
    }
    return null;
  }
  SuperProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_SuperProperty receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_SuperProperty.call(this, params);
    }), "SuperProperty", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_SuperProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_501: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_501.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "SuperProperty", this.unaryStaticRuntime(params));
    }
    return null;
  }
  MetaProperty(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_MetaProperty receiver=this arity=0 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_MetaProperty.call(this);
    }), "MetaProperty", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_MetaProperty(): void {
    {
      const __qin_typed_receiver_502: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_502.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "MetaProperty", this.unaryStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    }
    return null;
  }
  NewTarget(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_NewTarget receiver=this arity=0 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_NewTarget.call(this);
    }), "NewTarget", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_NewTarget(): void {
    {
      const __qin_typed_receiver_503: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_503.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "NewTarget", this.unaryStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    }
    return null;
  }
  ImportMeta(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_ImportMeta receiver=this arity=0 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_ImportMeta.call(this);
    }), "ImportMeta", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportMeta(): void {
    {
      const __qin_typed_receiver_504: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_504.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "ImportMeta", this.unaryStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT));
    }
    return null;
  }
  isContextual(value: string): boolean {
    return (__qin_binary__("!=", this.LA(1.0), null) && __QinJavaLangString.equals(value, this.LA(1.0).value()));
  }
  CallExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_CallExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_CallExpression.call(this, params);
    }), "CallExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CallExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_505: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_505.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "CallExpression", this.unaryStaticRuntime(params));
    }
    return null;
  }
  CoverCallExpressionAndAsyncArrowHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_CoverCallExpressionAndAsyncArrowHead receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_CoverCallExpressionAndAsyncArrowHead.call(this, params);
    }), "CoverCallExpressionAndAsyncArrowHead", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoverCallExpressionAndAsyncArrowHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_506: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_506.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "CoverCallExpressionAndAsyncArrowHead", this.unaryStaticRuntime(params));
    }
    return null;
  }
  CallMemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_CallMemberExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_CallMemberExpression.call(this, params);
    }), "CallMemberExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CallMemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_507: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_507.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "CallMemberExpression", this.unaryStaticRuntime(params));
    }
    return null;
  }
  SuperCall(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_SuperCall receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_SuperCall.call(this, params);
    }), "SuperCall", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_SuperCall(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_508: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_508.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "SuperCall", this.unaryStaticRuntime(params));
    }
    return null;
  }
  ImportCall(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_ImportCall receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_ImportCall.call(this, params);
    }), "ImportCall", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ImportCall(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_509: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_509.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "ImportCall", this.unaryStaticRuntime(params));
    }
    return null;
  }
  Arguments(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_Arguments receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_Arguments.call(this, params);
    }), "Arguments", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Arguments(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_510: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_510.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "Arguments", this.unaryStaticRuntime(params));
    }
    return null;
  }
  ArgumentList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_ArgumentList receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_ArgumentList.call(this, params);
    }), "ArgumentList", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArgumentList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_511: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_511.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "ArgumentList", this.unaryStaticRuntime(params));
    }
    return null;
  }
  ArgumentListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_ArgumentListItem receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_ArgumentListItem.call(this, params);
    }), "ArgumentListItem", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArgumentListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_512: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_512.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "ArgumentListItem", this.unaryStaticRuntime(params));
    }
    return null;
  }
  OptionalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_OptionalExpression receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_OptionalExpression.call(this, params);
    }), "OptionalExpression", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_OptionalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_513: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_513.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "OptionalExpression", this.unaryStaticRuntime(params));
    }
    return null;
  }
  canStartOptionalExpression(): boolean {
    let offset: number = this.offsetAfterOptionalExpressionHead(1.0);
    if (__qin_binary__("<", offset, 0.0)) {
      return false;
    }
    while (true) {
      let tokenName: string = this.tokenNameAt(offset);
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
        let afterGroup: number = this.offsetAfterBalancedGroup(offset);
        if (__qin_binary__("<", afterGroup, 0.0)) {
          return false;
        }
        offset = afterGroup;
        continue;
      }
      if (__QinJavaLangString.equals("Less", tokenName)) {
        let afterTypeArguments: number = this.offsetAfterBalancedTypeArguments(offset);
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
  offsetAfterOptionalExpressionHead(offset: number): number {
    let tokenName: string = this.tokenNameAt(offset);
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
  offsetAfterBalancedGroup(openOffset: number): number {
    let first: string = this.tokenNameAt(openOffset);
    if ((!__QinJavaLangString.equals("LParen", first) && !__QinJavaLangString.equals("LBracket", first) && !__QinJavaLangString.equals("LBrace", first))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    let parenDepth: number = 0.0;
    let bracketDepth: number = 0.0;
    let braceDepth: number = 0.0;
    for (let offset: number = openOffset; true; offset++) {
      let tokenName: string = this.tokenNameAt(offset);
      if (__qin_binary__("==", tokenName, null)) {
        return __qin_binary__("-", 0.0, 1.0);
      }
      switch (tokenName) {
        case "LParen": {
          parenDepth++;
          break;
        }
        case "RParen": {
          parenDepth--;
          break;
        }
        case "LBracket": {
          bracketDepth++;
          break;
        }
        case "RBracket": {
          bracketDepth--;
          break;
        }
        case "LBrace": {
          braceDepth++;
          break;
        }
        case "RBrace": {
          braceDepth--;
          break;
        }
        default: {
          break;
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
  offsetAfterBalancedTypeArguments(lessOffset: number): number {
    if ((!__QinJavaLangString.equals("Less", this.tokenNameAt(lessOffset)))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    let angleDepth: number = 0.0;
    let parenDepth: number = 0.0;
    let bracketDepth: number = 0.0;
    let braceDepth: number = 0.0;
    for (let offset: number = lessOffset; true; offset++) {
      let tokenName: string = this.tokenNameAt(offset);
      if (__qin_binary__("==", tokenName, null)) {
        return __qin_binary__("-", 0.0, 1.0);
      }
      switch (tokenName) {
        case "Less": {
          angleDepth++;
          break;
        }
        case "Greater": {
          angleDepth--;
          break;
        }
        case "RightShift": {
          angleDepth -= 2.0;
          break;
        }
        case "UnsignedRightShift": {
          angleDepth -= 3.0;
          break;
        }
        case "LParen": {
          parenDepth++;
          break;
        }
        case "RParen": {
          parenDepth--;
          break;
        }
        case "LBracket": {
          bracketDepth++;
          break;
        }
        case "RBracket": {
          bracketDepth--;
          break;
        }
        case "LBrace": {
          braceDepth++;
          break;
        }
        case "RBrace": {
          braceDepth--;
          break;
        }
        case "GreaterEqual": {
        }
        case "RightShiftAssign": {
        }
        case "UnsignedRightShiftAssign": {
          return __qin_binary__("-", 0.0, 1.0);
          break;
        }
        default: {
          break;
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
  safeLookahead(offset: number): com_subhuti_struct_SubhutiMatchToken {
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
  OptionalChain(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.expressions.SlimeUnaryExpressionParser method=__qin_subhuti_raw_OptionalChain receiver=this arity=1 */ com_slime_parser_expressions_SlimeUnaryExpressionParser.prototype.__qin_subhuti_raw_OptionalChain.call(this, params);
    }), "OptionalChain", "SlimeUnaryExpressionParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_OptionalChain(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_514: com_slime_parser_expressions_SlimeUnaryExpressionParser = this;
      __qin_typed_receiver_514.executeStaticRule(com_slime_parser_expressions_SlimeUnaryExpressionParser.__qin_field_STATIC_UNARY_GRAMMAR, "OptionalChain", this.unaryStaticRuntime(params));
    }
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
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_AWAIT_CONTEXT, gateId)) {
      return this.__qin_field_effectiveParams.await();
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
    if (__QinJavaLangString.equals(com_slime_parser_expressions_SlimeUnaryExpressionStaticGrammar.__qin_field_GATE_CALL_EXPRESSION_START, gateId)) {
      return (this.__qin_field_parser.hasCallExpressionPostfixAhead() && !this.__qin_field_parser.canStartOptionalExpression());
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
  runStaticAction(actionId: string): boolean {
    throw new __QinJavaLangUnsupportedOperationException(("unsupported unary static action: " + actionId));
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
    let assignmentParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams = this.__qin_field_parser.assignmentExpressionParamsForVariant(ruleName, variantKey, this.__qin_field_effectiveParams);
    if ((__qin_binary__("!=", variantKey, null) && __qin_binary__("==", assignmentParams, null))) {
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
      return this.__qin_field_parser.canStartUnaryExpression((__qin_binary__("==", assignmentParams, null) ? this.__qin_field_effectiveParams.withIn(true) : assignmentParams), lookaheadOffset);
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    let assignmentParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams = this.__qin_field_parser.assignmentExpressionParamsForVariant(ruleName, variantKey, this.__qin_field_effectiveParams);
    if ((__qin_binary__("!=", variantKey, null) && __qin_binary__("==", assignmentParams, null))) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported unary static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("UpdateExpression", ruleName)) {
      {
        const __qin_typed_receiver_515: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_515.UpdateExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("UnaryExpression", ruleName)) {
      {
        const __qin_typed_receiver_516: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_516.UnaryExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AwaitExpression", ruleName)) {
      {
        const __qin_typed_receiver_517: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_517.AwaitExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("LeftHandSideExpression", ruleName)) {
      {
        const __qin_typed_receiver_518: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_518.LeftHandSideExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("OptionalExpression", ruleName)) {
      {
        const __qin_typed_receiver_519: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_519.OptionalExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("UnaryOperator", ruleName)) {
      {
        const __qin_typed_receiver_520: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_520.UnaryOperator();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("SuperProperty", ruleName)) {
      {
        const __qin_typed_receiver_521: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_521.SuperProperty(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("MetaProperty", ruleName)) {
      {
        const __qin_typed_receiver_522: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_522.MetaProperty();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("NewTarget", ruleName)) {
      {
        const __qin_typed_receiver_523: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_523.NewTarget();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ImportMeta", ruleName)) {
      {
        const __qin_typed_receiver_524: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_524.ImportMeta();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("PrimaryExpression", ruleName)) {
      {
        const __qin_typed_receiver_525: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_525.PrimaryExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("NewExpression", ruleName)) {
      {
        const __qin_typed_receiver_526: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_526.NewExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CallExpression", ruleName)) {
      {
        const __qin_typed_receiver_527: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_527.CallExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("MemberExpression", ruleName)) {
      {
        const __qin_typed_receiver_528: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_528.MemberExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CoverCallExpressionAndAsyncArrowHead", ruleName)) {
      {
        const __qin_typed_receiver_529: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_529.CoverCallExpressionAndAsyncArrowHead(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CallMemberExpression", ruleName)) {
      {
        const __qin_typed_receiver_530: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_530.CallMemberExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("SuperCall", ruleName)) {
      {
        const __qin_typed_receiver_531: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_531.SuperCall(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ImportCall", ruleName)) {
      {
        const __qin_typed_receiver_532: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_532.ImportCall(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Arguments", ruleName)) {
      {
        const __qin_typed_receiver_533: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_533.Arguments(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ArgumentList", ruleName)) {
      {
        const __qin_typed_receiver_534: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_534.ArgumentList(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ArgumentListItem", ruleName)) {
      {
        const __qin_typed_receiver_535: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_535.ArgumentListItem(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("AssignmentExpression", ruleName)) {
      {
        const __qin_typed_receiver_536: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_536.AssignmentExpression((__qin_binary__("==", assignmentParams, null) ? this.__qin_field_effectiveParams.withIn(true) : assignmentParams));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Expression", ruleName)) {
      {
        const __qin_typed_receiver_537: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_537.Expression(this.__qin_field_effectiveParams.withIn(true));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("IdentifierName", ruleName)) {
      {
        const __qin_typed_receiver_538: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_538.IdentifierName();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("PrivateIdentifier", ruleName)) {
      {
        const __qin_typed_receiver_539: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_539.PrivateIdentifier();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TemplateLiteral", ruleName)) {
      {
        const __qin_typed_receiver_540: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_540.TemplateLiteral(new com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams(this.__qin_field_effectiveParams, true));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("IncompleteMemberAccessProperty", ruleName)) {
      {
        const __qin_typed_receiver_541: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_541.IncompleteMemberAccessProperty();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("OptionalChain", ruleName)) {
      {
        const __qin_typed_receiver_542: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_542.OptionalChain(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeParameterInstantiation", ruleName)) {
      {
        const __qin_typed_receiver_543: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_543.TSTypeParameterInstantiationStatic();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSNonNullExpressionTail", ruleName)) {
      {
        const __qin_typed_receiver_544: com_slime_parser_expressions_SlimeUnaryExpressionParser = this.__qin_field_parser;
        __qin_typed_receiver_544.TSNonNullExpressionTailStatic();
      }
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
