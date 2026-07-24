import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../base/SlimeJavascriptParserBase.ts";
import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "../../../subhuti/parser/SubhutiStaticGrammar.ts";
import { com_slime_parser_identifier_SlimeIdentifierStaticGrammar, com_slime_parser_identifier_SlimeIdentifierStaticGrammar as SlimeIdentifierStaticGrammar } from "./SlimeIdentifierStaticGrammar.ts";
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
class com_slime_parser_identifier_SlimeIdentifierParser extends com_slime_parser_base_SlimeJavascriptParserBase {
  static __qin_field_STATIC_IDENTIFIER_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_identifier_SlimeIdentifierParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeIdentifierParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_identifier_SlimeIdentifierParser_1_0(sourceCode: string): void {
    null;
  }
  IdentifierReference(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_IdentifierReference_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_IdentifierReference_0_1();
    throw new Error("Unsupported Java overload: IdentifierReference/" + __qin_args.length);
  }
  __qin_overload_IdentifierReference_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_IdentifierReference_1_0(params);
    }), "IdentifierReference", "SlimeIdentifierParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_IdentifierReference_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_identifier_SlimeIdentifierParser.__qin_field_STATIC_IDENTIFIER_GRAMMAR, "IdentifierReference", this.identifierStaticRuntime(params));
    return null;
  }
  __qin_overload_IdentifierReference_0_1(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_IdentifierReference_0_1();
    }), "IdentifierReference", "SlimeIdentifierParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_IdentifierReference_0_1(): any {
    this.IdentifierReference(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    return null;
  }
  BindingIdentifier(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_BindingIdentifier_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_BindingIdentifier_0_1();
    throw new Error("Unsupported Java overload: BindingIdentifier/" + __qin_args.length);
  }
  __qin_overload_BindingIdentifier_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_BindingIdentifier_1_0(params);
    }), "BindingIdentifier", "SlimeIdentifierParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_BindingIdentifier_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_identifier_SlimeIdentifierParser.__qin_field_STATIC_IDENTIFIER_GRAMMAR, "BindingIdentifier", this.identifierStaticRuntime(params));
    return null;
  }
  __qin_overload_BindingIdentifier_0_1(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_BindingIdentifier_0_1();
    }), "BindingIdentifier", "SlimeIdentifierParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_BindingIdentifier_0_1(): any {
    this.BindingIdentifier(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    return null;
  }
  LabelIdentifier(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_LabelIdentifier(params);
    }), "LabelIdentifier", "SlimeIdentifierParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LabelIdentifier(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_identifier_SlimeIdentifierParser.__qin_field_STATIC_IDENTIFIER_GRAMMAR, "LabelIdentifier", this.identifierStaticRuntime(params));
    return null;
  }
  Identifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Identifier();
    }), "Identifier", "SlimeIdentifierParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_Identifier(): any {
    if ((!this.canStartIdentifier())) {
      this.setParseFail();
      return null;
    }
    this.__qin_field_tokenConsumer.IdentifierName();
    return null;
  }
  IdentifierName(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_IdentifierName();
    }), "IdentifierName", "SlimeIdentifierParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_IdentifierName(): any {
    this.executeStaticRule(com_slime_parser_identifier_SlimeIdentifierParser.__qin_field_STATIC_IDENTIFIER_GRAMMAR, "IdentifierName", this.identifierStaticRuntime());
    return null;
  }
  identifierStaticRuntime(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_identifierStaticRuntime_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_identifierStaticRuntime_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: identifierStaticRuntime/" + __qin_args.length);
  }
  __qin_overload_identifierStaticRuntime_0_0(): any {
    return this.identifierStaticRuntime(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
  }
  __qin_overload_identifierStaticRuntime_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return new com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime(this, (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params));
  }
  canStartIdentifierReference(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    if (__qin_binary__("!=", lookaheadOffset, 1.0)) {
      return false;
    }
    let token: any = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    return (this.canStartIdentifier() || (!params.__qin_yield() && __QinJavaLangString.equals("Yield", token.tokenName())) || (!params.__qin_await() && __QinJavaLangString.equals("Await", token.tokenName())));
  }
  canStartLabelIdentifier(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): any {
    return this.canStartIdentifierReference(params, lookaheadOffset);
  }
  canStartBindingIdentifier(lookaheadOffset: number): any {
    if (__qin_binary__("!=", lookaheadOffset, 1.0)) {
      return false;
    }
    let token: any = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    return (this.canStartIdentifier() || __QinJavaLangString.equals("Yield", token.tokenName()) || __QinJavaLangString.equals("Await", token.tokenName()));
  }
  canStartPrivateIdentifier(lookaheadOffset: number): any {
    if (__qin_binary__("!=", lookaheadOffset, 1.0)) {
      return false;
    }
    let token: any = this.LA(lookaheadOffset);
    return (__qin_binary__("!=", token, null) && __QinJavaLangString.equals("PrivateIdentifier", token.tokenName()));
  }
  canStartIdentifierName(lookaheadOffset: number): any {
    return this.canStartIdentifierNameToken(lookaheadOffset);
  }
  canStartGenericIdentifierName(): any {
    let token: any = this.curToken();
    if ((__qin_binary__("==", token, null) || !__QinJavaLangString.equals("IdentifierName", token.tokenName()))) {
      return false;
    }
    let value: any = token.value();
    if ((__QinJavaLangString.equals("async", value) || __QinJavaLangString.equals("let", value) || __QinJavaLangString.equals("static", value) || __QinJavaLangString.equals("as", value))) {
      return false;
    }
    return (__qin_binary__("==", value, null) || !__QinJavaLangString.contains(value, "\\u") || this.isValidIdentifierWithEscapes(value));
  }
  PrivateIdentifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_PrivateIdentifier();
    }), "PrivateIdentifier", "SlimeIdentifierParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_PrivateIdentifier(): any {
    this.__qin_field_tokenConsumer.PrivateIdentifier();
    return null;
  }
}
const SlimeIdentifierParser = com_slime_parser_identifier_SlimeIdentifierParser;
class com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime {
  __qin_field_parser: com_slime_parser_identifier_SlimeIdentifierParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_identifier_SlimeIdentifierParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const effectiveParams: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime_2_0(parser, effectiveParams);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeIdentifierParser$IdentifierStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime_2_0(parser: com_slime_parser_identifier_SlimeIdentifierParser, effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = effectiveParams;
  }
  testStaticGate(gateId: string): any {
    if (__QinJavaLangString.equals(com_slime_parser_identifier_SlimeIdentifierStaticGrammar.__qin_field_GATE_IDENTIFIER_NAME_GENERIC, gateId)) {
      return this.__qin_field_parser.canStartGenericIdentifierName();
    }
    if (__QinJavaLangString.equals(com_slime_parser_identifier_SlimeIdentifierStaticGrammar.__qin_field_GATE_CAN_START_IDENTIFIER, gateId)) {
      return this.__qin_field_parser.canStartIdentifier();
    }
    if (__QinJavaLangString.equals(com_slime_parser_identifier_SlimeIdentifierStaticGrammar.__qin_field_GATE_NOT_YIELD_CONTEXT, gateId)) {
      return (!this.__qin_field_effectiveParams.__qin_yield());
    }
    if (__QinJavaLangString.equals(com_slime_parser_identifier_SlimeIdentifierStaticGrammar.__qin_field_GATE_NOT_AWAIT_CONTEXT, gateId)) {
      return (!this.__qin_field_effectiveParams.__qin_await());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported identifier static gate: " + gateId));
  }
  runStaticAction(actionId: string): any {
    if (__QinJavaLangString.equals(com_slime_parser_identifier_SlimeIdentifierStaticGrammar.__qin_field_ACTION_IDENTIFIER_NAME, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_identifier_SlimeIdentifierStaticGrammar.__qin_field_ACTION_IDENTIFIER, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_identifier_SlimeIdentifierStaticGrammar.__qin_field_ACTION_IDENTIFIER_REFERENCE, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_identifier_SlimeIdentifierStaticGrammar.__qin_field_ACTION_BINDING_IDENTIFIER, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_identifier_SlimeIdentifierStaticGrammar.__qin_field_ACTION_LABEL_IDENTIFIER, actionId)) {
      return true;
    }
    if (__QinJavaLangString.equals(com_slime_parser_identifier_SlimeIdentifierStaticGrammar.__qin_field_ACTION_PRIVATE_IDENTIFIER, actionId)) {
      return true;
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported identifier static action: " + actionId));
  }
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartIdentifierStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
    return this.canStartIdentifierStaticRule(ruleName, variantKey, lookaheadOffset);
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    this.invokeIdentifierStaticRule(ruleName, variantKey);
    return (!this.__qin_field_parser.isParserFail());
  }
  canStartIdentifierStaticRule(ruleName: string, variantKey: any, lookaheadOffset: number): any {
    let expressionParams: any = null;
    if ((() => { const __qin_pattern_value = variantKey; return __qin_instanceof__(__qin_pattern_value, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) && (expressionParams = __qin_pattern_value, true); })()) {
      if (__QinJavaLangString.equals("IdentifierReference", ruleName)) {
        return this.__qin_field_parser.canStartIdentifierReference(expressionParams, lookaheadOffset);
      }
      if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
        return this.__qin_field_parser.canStartBindingIdentifier(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("LabelIdentifier", ruleName)) {
        return this.__qin_field_parser.canStartLabelIdentifier(expressionParams, lookaheadOffset);
      }
    }
    if (__qin_binary__("==", variantKey, null)) {
      if (__QinJavaLangString.equals("IdentifierReference", ruleName)) {
        return this.__qin_field_parser.canStartIdentifierReference(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT, lookaheadOffset);
      }
      if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
        return this.__qin_field_parser.canStartBindingIdentifier(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("LabelIdentifier", ruleName)) {
        return this.__qin_field_parser.canStartLabelIdentifier(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT, lookaheadOffset);
      }
      if (__QinJavaLangString.equals("Identifier", ruleName)) {
        return this.__qin_field_parser.canStartIdentifier();
      }
      if (__QinJavaLangString.equals("PrivateIdentifier", ruleName)) {
        return this.__qin_field_parser.canStartPrivateIdentifier(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("IdentifierName", ruleName)) {
        return this.__qin_field_parser.canStartIdentifierName(lookaheadOffset);
      }
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported identifier static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  invokeIdentifierStaticRule(ruleName: string, variantKey: any): any {
    let expressionParams: any = null;
    if ((() => { const __qin_pattern_value = variantKey; return __qin_instanceof__(__qin_pattern_value, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) && (expressionParams = __qin_pattern_value, true); })()) {
      if (__QinJavaLangString.equals("IdentifierReference", ruleName)) {
        this.__qin_field_parser.IdentifierReference(expressionParams);
        return null;
      }
      if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
        this.__qin_field_parser.BindingIdentifier(expressionParams);
        return null;
      }
      if (__QinJavaLangString.equals("LabelIdentifier", ruleName)) {
        this.__qin_field_parser.LabelIdentifier(expressionParams);
        return null;
      }
    }
    if (__qin_binary__("==", variantKey, null)) {
      if (__QinJavaLangString.equals("IdentifierReference", ruleName)) {
        this.__qin_field_parser.IdentifierReference(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
        return null;
      }
      if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
        this.__qin_field_parser.BindingIdentifier(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
        return null;
      }
      if (__QinJavaLangString.equals("LabelIdentifier", ruleName)) {
        this.__qin_field_parser.LabelIdentifier(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
        return null;
      }
      if (__QinJavaLangString.equals("Identifier", ruleName)) {
        this.__qin_field_parser.Identifier();
        return null;
      }
      if (__QinJavaLangString.equals("PrivateIdentifier", ruleName)) {
        this.__qin_field_parser.PrivateIdentifier();
        return null;
      }
      if (__QinJavaLangString.equals("IdentifierName", ruleName)) {
        this.__qin_field_parser.IdentifierName();
        return null;
      }
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported identifier static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeIdentifierParser$IdentifierStaticRuntime = com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime;
com_slime_parser_identifier_SlimeIdentifierParser.__qin_field_STATIC_IDENTIFIER_GRAMMAR = com_slime_parser_identifier_SlimeIdentifierStaticGrammar.grammar();

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_identifier_SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime };
