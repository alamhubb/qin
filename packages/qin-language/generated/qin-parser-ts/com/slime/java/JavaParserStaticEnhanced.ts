import { com_slime_java_JavaParser, com_slime_java_JavaParser as JavaParser } from "./JavaParser.ts";
import { com_subhuti_parser_SubhutiStaticGrammarPlan, com_subhuti_parser_SubhutiStaticGrammarPlan as SubhutiStaticGrammarPlan, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionAvailability, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionAvailability as DecisionAvailability, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDispatchBlocker, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDispatchBlocker as StaticDispatchBlocker, com_subhuti_parser_SubhutiStaticGrammarPlan$DynamicCandidateReason, com_subhuti_parser_SubhutiStaticGrammarPlan$DynamicCandidateReason as DynamicCandidateReason, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadKind, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadKind as CompiledLookaheadKind, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkNodeDefinition as CompiledLlkNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkEdgeDefinition as CompiledLlkEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadDefinition as CompiledLookaheadDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$BranchDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$BranchDefinition as BranchDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixDefinition as SharedPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupDefinition as CandidateGroupDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleTokenDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleTokenDefinition as CrossRuleTokenDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleFrontierDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleFrontierDefinition as CrossRuleFrontierDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphNodeDefinition as AdaptiveGraphNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphDefinition as AdaptiveGraphDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionKind, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionKind as FiniteInstructionKind, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteTokenClassDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteTokenClassDefinition as FiniteTokenClassDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteMatchEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteMatchEdgeDefinition as FiniteMatchEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteRuleReferenceDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteRuleReferenceDefinition as FiniteRuleReferenceDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteFrameDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteFrameDefinition as FiniteFrameDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteGateDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteGateDefinition as FiniteGateDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteCallPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteCallPrefixDefinition as FiniteCallPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionDefinition as FiniteInstructionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteDecisionProgramDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteDecisionProgramDefinition as FiniteDecisionProgramDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteProgramAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteProgramAddress as FiniteProgramAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixAddress as SharedPrefixAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupAddress as CandidateGroupAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleAddress as CrossRuleAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionDefinition as DecisionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AnalysisDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AnalysisDefinition as AnalysisDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$InvocationDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$InvocationDefinition as InvocationDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CoverageDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CoverageDefinition as CoverageDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticMetadata, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticMetadata as StaticMetadata, com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence, com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence as Occurrence, com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant, com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant as RuleVariant, com_subhuti_parser_SubhutiStaticGrammarPlan$VariantRecursionAnalysis, com_subhuti_parser_SubhutiStaticGrammarPlan$VariantRecursionAnalysis as VariantRecursionAnalysis, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan as StaticRuleInvocationPlan, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlans, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlans as StaticRuleInvocationPlans, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDecisionPlans, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDecisionPlans as StaticDecisionPlans } from "../../subhuti/parser/SubhutiStaticGrammarPlan.ts";
import { com_slime_java_clazz_JavaClassParser, com_slime_java_clazz_JavaClassParser as JavaClassParser } from "./clazz/JavaClassParser.ts";
import { com_slime_java_statement_JavaStatementParser, com_slime_java_statement_JavaStatementParser as JavaStatementParser } from "./statement/JavaStatementParser.ts";
import { com_slime_java_expression_JavaExpressionParser, com_slime_java_expression_JavaExpressionParser as JavaExpressionParser } from "./expression/JavaExpressionParser.ts";
import { com_slime_java_type_JavaTypeParser, com_slime_java_type_JavaTypeParser as JavaTypeParser } from "./type/JavaTypeParser.ts";
import { com_slime_java_literal_JavaLiteralParser, com_slime_java_literal_JavaLiteralParser as JavaLiteralParser } from "./literal/JavaLiteralParser.ts";
import { com_slime_java_identifier_JavaIdentifierParser, com_slime_java_identifier_JavaIdentifierParser as JavaIdentifierParser } from "./identifier/JavaIdentifierParser.ts";
import { com_slime_java_base_JavaParserBase, com_slime_java_base_JavaParserBase as JavaParserBase } from "./base/JavaParserBase.ts";
import { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser as SubhutiParser, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime as StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticPrefix, com_subhuti_parser_SubhutiParser$StaticPrefix as StaticPrefix, com_subhuti_parser_SubhutiParser$StaticChoice, com_subhuti_parser_SubhutiParser$StaticChoice as StaticChoice } from "../../subhuti/parser/SubhutiParser.ts";
import { com_subhuti_parser_SubhutiParserFinal, com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal } from "../../subhuti/parser/SubhutiParserFinal.ts";
import { com_subhuti_parser_SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators as SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl as StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher as StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext as AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext as AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes as PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates as StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame as ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames as CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys as CurrentTokenKeys } from "../../subhuti/parser/SubhutiParserCombinators.ts";
import { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore as SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments as StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult as RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode as StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks as StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$CacheWork, com_subhuti_parser_SubhutiParserCore$CacheWork as CacheWork, com_subhuti_parser_SubhutiParserCore$FailureWork, com_subhuti_parser_SubhutiParserCore$FailureWork as FailureWork } from "../../subhuti/parser/SubhutiParserCore.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations as ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException as SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException as SubhutiFirstTokenUnknownException } from "../../subhuti/parser/SubhutiParserState.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../../subhuti/lookahead/SubhutiTokenLookahead.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __QinJavaUtilArrayList, __QinJavaUtilUnmodifiableList, __QinJavaUtilList, __QinJavaUtilHashMap, __QinJavaUtilUnmodifiableMap, __QinJavaUtilSet, __QinJavaUtilUnmodifiableSet } from "@qin/java-sdk-js";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const RuntimeException = __QinJavaLangRuntimeException;
class com_slime_java_JavaParserStaticEnhanced extends com_slime_java_JavaParser {
  static __qin_field_SUBHUTI_STATIC_GRAMMAR_PLAN: com_subhuti_parser_SubhutiStaticGrammarPlan | null = null as any;
  static __qin_field_SUBHUTI_RULE_NAMES_BY_ID: string[] | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_java_JavaParserStaticEnhanced_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: JavaParserStaticEnhanced/" + __qin_args.length);
  }
  __qin_constructor_com_slime_java_JavaParserStaticEnhanced_1_0(sourceCode: string): void {
    null;
  }
  static create(sourceCode: string): com_slime_java_JavaParserStaticEnhanced {
    return new com_slime_java_JavaParserStaticEnhanced(sourceCode);
  }
  additiveExpression(): void {
    if ((!this.beginStaticRuleLinked(19.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2348: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2348.executeStaticRuleBodyLinked(19.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2349: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2349.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2350: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2350.completeStaticRule();
    }
    return null;
  }
  andExpression(): void {
    if ((!this.beginStaticRuleLinked(20.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2351: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2351.executeStaticRuleBodyLinked(20.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2352: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2352.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2353: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2353.completeStaticRule();
    }
    return null;
  }
  annotation(): void {
    if ((!this.beginStaticRuleLinked(114.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2354: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2354.executeStaticRuleBodyLinked(114.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2355: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2355.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2356: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2356.completeStaticRule();
    }
    return null;
  }
  annotationConstantRest(): void {
    if ((!this.beginStaticRuleLinked(115.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2357: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2357.executeStaticRuleBodyLinked(115.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2358: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2358.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2359: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2359.completeStaticRule();
    }
    return null;
  }
  annotationMethodOrConstantRest(): void {
    if ((!this.beginStaticRuleLinked(116.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2360: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2360.executeStaticRuleBodyLinked(116.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2361: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2361.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2362: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2362.completeStaticRule();
    }
    return null;
  }
  annotationMethodRest(): void {
    if ((!this.beginStaticRuleLinked(117.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2363: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2363.executeStaticRuleBodyLinked(117.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2364: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2364.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2365: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2365.completeStaticRule();
    }
    return null;
  }
  annotationTypeBody(): void {
    if ((!this.beginStaticRuleLinked(118.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2366: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2366.executeStaticRuleBodyLinked(118.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2367: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2367.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2368: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2368.completeStaticRule();
    }
    return null;
  }
  annotationTypeDeclaration(): void {
    if ((!this.beginStaticRuleLinked(85.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2369: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2369.executeStaticRuleBodyLinked(85.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2370: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2370.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2371: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2371.completeStaticRule();
    }
    return null;
  }
  annotationTypeElementDeclaration(): void {
    if ((!this.beginStaticRuleLinked(119.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2372: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2372.executeStaticRuleBodyLinked(119.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2373: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2373.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2374: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2374.completeStaticRule();
    }
    return null;
  }
  annotationTypeElementRest(): void {
    if ((!this.beginStaticRuleLinked(120.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2375: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2375.executeStaticRuleBodyLinked(120.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2376: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2376.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2377: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2377.completeStaticRule();
    }
    return null;
  }
  arguments(): void {
    if ((!this.beginStaticRuleLinked(121.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2378: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2378.executeStaticRuleBodyLinked(121.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2379: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2379.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2380: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2380.completeStaticRule();
    }
    return null;
  }
  arrayCreatorDimensions(): void {
    if ((!this.beginStaticRuleLinked(21.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2381: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2381.executeStaticRuleBodyLinked(21.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2382: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2382.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2383: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2383.completeStaticRule();
    }
    return null;
  }
  arrayInitializer(): void {
    if ((!this.beginStaticRuleLinked(4.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2384: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2384.executeStaticRuleBodyLinked(4.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2385: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2385.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2386: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2386.completeStaticRule();
    }
    return null;
  }
  assignment(): void {
    if ((!this.beginStaticRuleLinked(22.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2387: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2387.executeStaticRuleBodyLinked(22.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2388: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2388.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2389: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2389.completeStaticRule();
    }
    return null;
  }
  assignmentExpression(): void {
    if ((!this.beginStaticRuleLinked(23.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2390: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2390.executeStaticRuleBodyLinked(23.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2391: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2391.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2392: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2392.completeStaticRule();
    }
    return null;
  }
  assignmentOperator(): void {
    if ((!this.beginStaticRuleLinked(24.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2393: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2393.executeStaticRuleBodyLinked(24.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2394: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2394.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2395: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2395.completeStaticRule();
    }
    return null;
  }
  block(): void {
    if ((!this.beginStaticRuleLinked(47.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2396: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2396.executeStaticRuleBodyLinked(47.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2397: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2397.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2398: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2398.completeStaticRule();
    }
    return null;
  }
  blockStatement(): void {
    if ((!this.beginStaticRuleLinked(48.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2399: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2399.executeStaticRuleBodyLinked(48.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2400: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2400.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2401: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2401.completeStaticRule();
    }
    return null;
  }
  breakStatement(): void {
    if ((!this.beginStaticRuleLinked(49.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2402: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2402.executeStaticRuleBodyLinked(49.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2403: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2403.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2404: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2404.completeStaticRule();
    }
    return null;
  }
  catchClause(): void {
    if ((!this.beginStaticRuleLinked(50.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2405: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2405.executeStaticRuleBodyLinked(50.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2406: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2406.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2407: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2407.completeStaticRule();
    }
    return null;
  }
  catchType(): void {
    if ((!this.beginStaticRuleLinked(51.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2408: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2408.executeStaticRuleBodyLinked(51.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2409: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2409.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2410: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2410.completeStaticRule();
    }
    return null;
  }
  classBody(): void {
    if ((!this.beginStaticRuleLinked(86.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2411: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2411.executeStaticRuleBodyLinked(86.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2412: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2412.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2413: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2413.completeStaticRule();
    }
    return null;
  }
  classBodyDeclaration(): void {
    if ((!this.beginStaticRuleLinked(87.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2414: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2414.executeStaticRuleBodyLinked(87.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2415: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2415.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2416: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2416.completeStaticRule();
    }
    return null;
  }
  classCreatorRest(): void {
    if ((!this.beginStaticRuleLinked(122.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2417: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2417.executeStaticRuleBodyLinked(122.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2418: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2418.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2419: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2419.completeStaticRule();
    }
    return null;
  }
  classDeclaration(): void {
    if ((!this.beginStaticRuleLinked(88.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2420: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2420.executeStaticRuleBodyLinked(88.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2421: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2421.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2422: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2422.completeStaticRule();
    }
    return null;
  }
  classOrInterfaceModifier(): void {
    if ((!this.beginStaticRuleLinked(89.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2423: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2423.executeStaticRuleBodyLinked(89.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2424: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2424.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2425: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2425.completeStaticRule();
    }
    return null;
  }
  classOrInterfaceType(): void {
    if ((!this.beginStaticRuleLinked(9.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2426: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2426.executeStaticRuleBodyLinked(9.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2427: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2427.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2428: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2428.completeStaticRule();
    }
    return null;
  }
  compactConstructorDeclaration(): void {
    if ((!this.beginStaticRuleLinked(123.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2429: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2429.executeStaticRuleBodyLinked(123.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2430: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2430.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2431: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2431.completeStaticRule();
    }
    return null;
  }
  compilationUnit(): void {
    if ((!this.beginStaticRuleLinked(124.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2432: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2432.executeStaticRuleBodyLinked(124.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2433: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2433.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2434: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2434.completeStaticRule();
    }
    return null;
  }
  conditionalAndExpression(): void {
    if ((!this.beginStaticRuleLinked(25.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2435: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2435.executeStaticRuleBodyLinked(25.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2436: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2436.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2437: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2437.completeStaticRule();
    }
    return null;
  }
  conditionalExpression(): void {
    if ((!this.beginStaticRuleLinked(26.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2438: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2438.executeStaticRuleBodyLinked(26.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2439: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2439.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2440: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2440.completeStaticRule();
    }
    return null;
  }
  conditionalOrExpression(): void {
    if ((!this.beginStaticRuleLinked(27.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2441: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2441.executeStaticRuleBodyLinked(27.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2442: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2442.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2443: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2443.completeStaticRule();
    }
    return null;
  }
  constDeclaration(): void {
    if ((!this.beginStaticRuleLinked(90.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2444: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2444.executeStaticRuleBodyLinked(90.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2445: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2445.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2446: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2446.completeStaticRule();
    }
    return null;
  }
  constantDeclarator(): void {
    if ((!this.beginStaticRuleLinked(91.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2447: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2447.executeStaticRuleBodyLinked(91.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2448: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2448.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2449: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2449.completeStaticRule();
    }
    return null;
  }
  constructorDeclaration(): void {
    if ((!this.beginStaticRuleLinked(92.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2450: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2450.executeStaticRuleBodyLinked(92.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2451: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2451.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2452: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2452.completeStaticRule();
    }
    return null;
  }
  continueStatement(): void {
    if ((!this.beginStaticRuleLinked(52.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2453: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2453.executeStaticRuleBodyLinked(52.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2454: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2454.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2455: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2455.completeStaticRule();
    }
    return null;
  }
  defaultValue(): void {
    if ((!this.beginStaticRuleLinked(125.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2456: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2456.executeStaticRuleBodyLinked(125.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2457: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2457.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2458: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2458.completeStaticRule();
    }
    return null;
  }
  doWhileStatement(): void {
    if ((!this.beginStaticRuleLinked(53.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2459: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2459.executeStaticRuleBodyLinked(53.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2460: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2460.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2461: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2461.completeStaticRule();
    }
    return null;
  }
  elementValue(): void {
    if ((!this.beginStaticRuleLinked(126.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2462: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2462.executeStaticRuleBodyLinked(126.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2463: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2463.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2464: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2464.completeStaticRule();
    }
    return null;
  }
  elementValueArrayInitializer(): void {
    if ((!this.beginStaticRuleLinked(127.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2465: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2465.executeStaticRuleBodyLinked(127.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2466: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2466.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2467: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2467.completeStaticRule();
    }
    return null;
  }
  elementValuePair(): void {
    if ((!this.beginStaticRuleLinked(128.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2468: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2468.executeStaticRuleBodyLinked(128.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2469: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2469.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2470: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2470.completeStaticRule();
    }
    return null;
  }
  elementValuePairs(): void {
    if ((!this.beginStaticRuleLinked(129.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2471: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2471.executeStaticRuleBodyLinked(129.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2472: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2472.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2473: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2473.completeStaticRule();
    }
    return null;
  }
  enhancedForControl(): void {
    if ((!this.beginStaticRuleLinked(54.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2474: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2474.executeStaticRuleBodyLinked(54.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2475: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2475.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2476: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2476.completeStaticRule();
    }
    return null;
  }
  enumBodyDeclarations(): void {
    if ((!this.beginStaticRuleLinked(130.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2477: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2477.executeStaticRuleBodyLinked(130.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2478: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2478.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2479: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2479.completeStaticRule();
    }
    return null;
  }
  enumConstant(): void {
    if ((!this.beginStaticRuleLinked(131.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2480: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2480.executeStaticRuleBodyLinked(131.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2481: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2481.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2482: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2482.completeStaticRule();
    }
    return null;
  }
  enumConstants(): void {
    if ((!this.beginStaticRuleLinked(132.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2483: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2483.executeStaticRuleBodyLinked(132.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2484: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2484.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2485: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2485.completeStaticRule();
    }
    return null;
  }
  enumDeclaration(): void {
    if ((!this.beginStaticRuleLinked(93.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2486: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2486.executeStaticRuleBodyLinked(93.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2487: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2487.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2488: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2488.completeStaticRule();
    }
    return null;
  }
  equalityExpression(): void {
    if ((!this.beginStaticRuleLinked(28.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2489: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2489.executeStaticRuleBodyLinked(28.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2490: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2490.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2491: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2491.completeStaticRule();
    }
    return null;
  }
  exclusiveOrExpression(): void {
    if ((!this.beginStaticRuleLinked(29.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2492: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2492.executeStaticRuleBodyLinked(29.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2493: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2493.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2494: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2494.completeStaticRule();
    }
    return null;
  }
  explicitConstructorInvocationStatement(): void {
    if ((!this.beginStaticRuleLinked(55.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2495: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2495.executeStaticRuleBodyLinked(55.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2496: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2496.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2497: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2497.completeStaticRule();
    }
    return null;
  }
  explicitGenericInvocation(): void {
    if ((!this.beginStaticRuleLinked(133.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2498: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2498.executeStaticRuleBodyLinked(133.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2499: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2499.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2500: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2500.completeStaticRule();
    }
    return null;
  }
  explicitGenericInvocationSuffix(): void {
    if ((!this.beginStaticRuleLinked(134.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2501: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2501.executeStaticRuleBodyLinked(134.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2502: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2502.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2503: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2503.completeStaticRule();
    }
    return null;
  }
  expression(): void {
    if ((!this.beginStaticRuleLinked(30.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2504: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2504.executeStaticRuleBodyLinked(30.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2505: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2505.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2506: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2506.completeStaticRule();
    }
    return null;
  }
  expressionList(): void {
    if ((!this.beginStaticRuleLinked(56.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2507: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2507.executeStaticRuleBodyLinked(56.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2508: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2508.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2509: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2509.completeStaticRule();
    }
    return null;
  }
  expressionStatement(): void {
    if ((!this.beginStaticRuleLinked(57.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2510: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2510.executeStaticRuleBodyLinked(57.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2511: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2511.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2512: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2512.completeStaticRule();
    }
    return null;
  }
  fieldDeclaration(): void {
    if ((!this.beginStaticRuleLinked(94.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2513: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2513.executeStaticRuleBodyLinked(94.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2514: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2514.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2515: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2515.completeStaticRule();
    }
    return null;
  }
  finallyBlock(): void {
    if ((!this.beginStaticRuleLinked(58.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2516: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2516.executeStaticRuleBodyLinked(58.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2517: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2517.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2518: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2518.completeStaticRule();
    }
    return null;
  }
  floatingPointLiteral(): void {
    if ((!this.beginStaticRuleLinked(5.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2519: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2519.executeStaticRuleBodyLinked(5.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2520: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2520.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2521: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2521.completeStaticRule();
    }
    return null;
  }
  forControl(): void {
    if ((!this.beginStaticRuleLinked(59.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2522: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2522.executeStaticRuleBodyLinked(59.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2523: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2523.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2524: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2524.completeStaticRule();
    }
    return null;
  }
  forInit(): void {
    if ((!this.beginStaticRuleLinked(60.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2525: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2525.executeStaticRuleBodyLinked(60.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2526: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2526.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2527: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2527.completeStaticRule();
    }
    return null;
  }
  forStatement(): void {
    if ((!this.beginStaticRuleLinked(61.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2528: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2528.executeStaticRuleBodyLinked(61.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2529: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2529.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2530: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2530.completeStaticRule();
    }
    return null;
  }
  forUpdate(): void {
    if ((!this.beginStaticRuleLinked(62.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2531: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2531.executeStaticRuleBodyLinked(62.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2532: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2532.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2533: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2533.completeStaticRule();
    }
    return null;
  }
  formalParameter(): void {
    if ((!this.beginStaticRuleLinked(135.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2534: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2534.executeStaticRuleBodyLinked(135.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2535: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2535.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2536: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2536.completeStaticRule();
    }
    return null;
  }
  formalParameterList(): void {
    if ((!this.beginStaticRuleLinked(136.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2537: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2537.executeStaticRuleBodyLinked(136.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2538: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2538.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2539: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2539.completeStaticRule();
    }
    return null;
  }
  formalParameters(): void {
    if ((!this.beginStaticRuleLinked(137.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2540: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2540.executeStaticRuleBodyLinked(137.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2541: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2541.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2542: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2542.completeStaticRule();
    }
    return null;
  }
  genericConstructorDeclaration(): void {
    if ((!this.beginStaticRuleLinked(95.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2543: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2543.executeStaticRuleBodyLinked(95.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2544: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2544.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2545: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2545.completeStaticRule();
    }
    return null;
  }
  genericInterfaceMethodDeclaration(): void {
    if ((!this.beginStaticRuleLinked(96.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2546: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2546.executeStaticRuleBodyLinked(96.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2547: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2547.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2548: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2548.completeStaticRule();
    }
    return null;
  }
  genericMethodDeclaration(): void {
    if ((!this.beginStaticRuleLinked(97.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2549: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2549.executeStaticRuleBodyLinked(97.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2550: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2550.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2551: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2551.completeStaticRule();
    }
    return null;
  }
  identifier(): void {
    if ((!this.beginStaticRuleLinked(0.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2552: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2552.executeStaticRuleBodyLinked(0.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2553: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2553.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2554: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2554.completeStaticRule();
    }
    return null;
  }
  ifStatement(): void {
    if ((!this.beginStaticRuleLinked(63.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2555: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2555.executeStaticRuleBodyLinked(63.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2556: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2556.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2557: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2557.completeStaticRule();
    }
    return null;
  }
  importDeclaration(): void {
    if ((!this.beginStaticRuleLinked(138.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2558: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2558.executeStaticRuleBodyLinked(138.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2559: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2559.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2560: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2560.completeStaticRule();
    }
    return null;
  }
  importQualifiedName(): void {
    if ((!this.beginStaticRuleLinked(139.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2561: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2561.executeStaticRuleBodyLinked(139.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2562: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2562.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2563: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2563.completeStaticRule();
    }
    return null;
  }
  importQualifiedNameSegment(): void {
    if ((!this.beginStaticRuleLinked(140.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2564: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2564.executeStaticRuleBodyLinked(140.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2565: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2565.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2566: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2566.completeStaticRule();
    }
    return null;
  }
  inclusiveOrExpression(): void {
    if ((!this.beginStaticRuleLinked(31.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2567: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2567.executeStaticRuleBodyLinked(31.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2568: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2568.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2569: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2569.completeStaticRule();
    }
    return null;
  }
  innerCreator(): void {
    if ((!this.beginStaticRuleLinked(141.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2570: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2570.executeStaticRuleBodyLinked(141.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2571: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2571.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2572: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2572.completeStaticRule();
    }
    return null;
  }
  integerLiteral(): void {
    if ((!this.beginStaticRuleLinked(6.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2573: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2573.executeStaticRuleBodyLinked(6.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2574: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2574.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2575: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2575.completeStaticRule();
    }
    return null;
  }
  interfaceBody(): void {
    if ((!this.beginStaticRuleLinked(98.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2576: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2576.executeStaticRuleBodyLinked(98.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2577: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2577.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2578: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2578.completeStaticRule();
    }
    return null;
  }
  interfaceBodyDeclaration(): void {
    if ((!this.beginStaticRuleLinked(99.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2579: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2579.executeStaticRuleBodyLinked(99.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2580: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2580.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2581: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2581.completeStaticRule();
    }
    return null;
  }
  interfaceCommonBodyDeclaration(): void {
    if ((!this.beginStaticRuleLinked(100.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2582: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2582.executeStaticRuleBodyLinked(100.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2583: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2583.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2584: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2584.completeStaticRule();
    }
    return null;
  }
  interfaceDeclaration(): void {
    if ((!this.beginStaticRuleLinked(101.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2585: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2585.executeStaticRuleBodyLinked(101.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2586: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2586.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2587: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2587.completeStaticRule();
    }
    return null;
  }
  interfaceMemberDeclaration(): void {
    if ((!this.beginStaticRuleLinked(102.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2588: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2588.executeStaticRuleBodyLinked(102.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2589: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2589.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2590: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2590.completeStaticRule();
    }
    return null;
  }
  interfaceMethodDeclaration(): void {
    if ((!this.beginStaticRuleLinked(103.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2591: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2591.executeStaticRuleBodyLinked(103.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2592: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2592.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2593: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2593.completeStaticRule();
    }
    return null;
  }
  interfaceModifier(): void {
    if ((!this.beginStaticRuleLinked(104.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2594: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2594.executeStaticRuleBodyLinked(104.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2595: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2595.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2596: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2596.completeStaticRule();
    }
    return null;
  }
  labeledStatement(): void {
    if ((!this.beginStaticRuleLinked(64.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2597: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2597.executeStaticRuleBodyLinked(64.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2598: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2598.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2599: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2599.completeStaticRule();
    }
    return null;
  }
  lambdaBody(): void {
    if ((!this.beginStaticRuleLinked(32.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2600: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2600.executeStaticRuleBodyLinked(32.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2601: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2601.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2602: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2602.completeStaticRule();
    }
    return null;
  }
  lambdaExpression(): void {
    if ((!this.beginStaticRuleLinked(33.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2603: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2603.executeStaticRuleBodyLinked(33.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2604: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2604.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2605: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2605.completeStaticRule();
    }
    return null;
  }
  lambdaParameters(): void {
    if ((!this.beginStaticRuleLinked(34.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2606: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2606.executeStaticRuleBodyLinked(34.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2607: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2607.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2608: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2608.completeStaticRule();
    }
    return null;
  }
  lastFormalParameter(): void {
    if ((!this.beginStaticRuleLinked(142.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2609: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2609.executeStaticRuleBodyLinked(142.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2610: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2610.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2611: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2611.completeStaticRule();
    }
    return null;
  }
  leftHandSide(): void {
    if ((!this.beginStaticRuleLinked(35.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2612: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2612.executeStaticRuleBodyLinked(35.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2613: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2613.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2614: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2614.completeStaticRule();
    }
    return null;
  }
  literal(): void {
    if ((!this.beginStaticRuleLinked(7.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2615: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2615.executeStaticRuleBodyLinked(7.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2616: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2616.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2617: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2617.completeStaticRule();
    }
    return null;
  }
  localTypeDeclaration(): void {
    if ((!this.beginStaticRuleLinked(65.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2618: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2618.executeStaticRuleBodyLinked(65.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2619: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2619.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2620: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2620.completeStaticRule();
    }
    return null;
  }
  localVariableDeclaration(): void {
    if ((!this.beginStaticRuleLinked(66.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2621: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2621.executeStaticRuleBodyLinked(66.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2622: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2622.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2623: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2623.completeStaticRule();
    }
    return null;
  }
  memberDeclaration(): void {
    if ((!this.beginStaticRuleLinked(105.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2624: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2624.executeStaticRuleBodyLinked(105.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2625: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2625.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2626: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2626.completeStaticRule();
    }
    return null;
  }
  methodBody(): void {
    if ((!this.beginStaticRuleLinked(106.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2627: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2627.executeStaticRuleBodyLinked(106.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2628: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2628.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2629: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2629.completeStaticRule();
    }
    return null;
  }
  methodDeclaration(): void {
    if ((!this.beginStaticRuleLinked(107.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2630: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2630.executeStaticRuleBodyLinked(107.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2631: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2631.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2632: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2632.completeStaticRule();
    }
    return null;
  }
  modifier(): void {
    if ((!this.beginStaticRuleLinked(108.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2633: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2633.executeStaticRuleBodyLinked(108.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2634: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2634.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2635: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2635.completeStaticRule();
    }
    return null;
  }
  moduleDeclaration(): void {
    if ((!this.beginStaticRuleLinked(143.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2636: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2636.executeStaticRuleBodyLinked(143.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2637: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2637.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2638: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2638.completeStaticRule();
    }
    return null;
  }
  moduleDirective(): void {
    if ((!this.beginStaticRuleLinked(144.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2639: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2639.executeStaticRuleBodyLinked(144.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2640: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2640.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2641: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2641.completeStaticRule();
    }
    return null;
  }
  multiplicativeExpression(): void {
    if ((!this.beginStaticRuleLinked(36.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2642: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2642.executeStaticRuleBodyLinked(36.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2643: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2643.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2644: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2644.completeStaticRule();
    }
    return null;
  }
  nonWildcardTypeArguments(): void {
    if ((!this.beginStaticRuleLinked(145.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2645: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2645.executeStaticRuleBodyLinked(145.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2646: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2646.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2647: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2647.completeStaticRule();
    }
    return null;
  }
  nonWildcardTypeArgumentsOrDiamond(): void {
    if ((!this.beginStaticRuleLinked(146.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2648: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2648.executeStaticRuleBodyLinked(146.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2649: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2649.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2650: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2650.completeStaticRule();
    }
    return null;
  }
  packageDeclaration(): void {
    if ((!this.beginStaticRuleLinked(147.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2651: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2651.executeStaticRuleBodyLinked(147.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2652: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2652.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2653: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2653.completeStaticRule();
    }
    return null;
  }
  parExpression(): void {
    if ((!this.beginStaticRuleLinked(67.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2654: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2654.executeStaticRuleBodyLinked(67.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2655: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2655.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2656: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2656.completeStaticRule();
    }
    return null;
  }
  pattern(): void {
    if ((!this.beginStaticRuleLinked(148.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2657: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2657.executeStaticRuleBodyLinked(148.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2658: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2658.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2659: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2659.completeStaticRule();
    }
    return null;
  }
  postfixExpression(): void {
    if ((!this.beginStaticRuleLinked(37.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2660: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2660.executeStaticRuleBodyLinked(37.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2661: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2661.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2662: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2662.completeStaticRule();
    }
    return null;
  }
  postfixOp(): void {
    if ((!this.beginStaticRuleLinked(38.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2663: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2663.executeStaticRuleBodyLinked(38.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2664: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2664.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2665: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2665.completeStaticRule();
    }
    return null;
  }
  prefixOp(): void {
    if ((!this.beginStaticRuleLinked(39.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2666: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2666.executeStaticRuleBodyLinked(39.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2667: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2667.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2668: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2668.completeStaticRule();
    }
    return null;
  }
  primary(): void {
    if ((!this.beginStaticRuleLinked(40.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2669: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2669.executeStaticRuleBodyLinked(40.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2670: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2670.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2671: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2671.completeStaticRule();
    }
    return null;
  }
  primitiveType(): void {
    if ((!this.beginStaticRuleLinked(10.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2672: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2672.executeStaticRuleBodyLinked(10.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2673: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2673.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2674: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2674.completeStaticRule();
    }
    return null;
  }
  qualifiedName(): void {
    if ((!this.beginStaticRuleLinked(1.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2675: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2675.executeStaticRuleBodyLinked(1.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2676: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2676.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2677: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2677.completeStaticRule();
    }
    return null;
  }
  qualifiedNameList(): void {
    if ((!this.beginStaticRuleLinked(2.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2678: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2678.executeStaticRuleBodyLinked(2.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2679: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2679.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2680: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2680.completeStaticRule();
    }
    return null;
  }
  receiverParameter(): void {
    if ((!this.beginStaticRuleLinked(149.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2681: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2681.executeStaticRuleBodyLinked(149.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2682: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2682.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2683: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2683.completeStaticRule();
    }
    return null;
  }
  recordBody(): void {
    if ((!this.beginStaticRuleLinked(150.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2684: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2684.executeStaticRuleBodyLinked(150.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2685: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2685.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2686: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2686.completeStaticRule();
    }
    return null;
  }
  recordComponent(): void {
    if ((!this.beginStaticRuleLinked(151.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2687: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2687.executeStaticRuleBodyLinked(151.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2688: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2688.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2689: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2689.completeStaticRule();
    }
    return null;
  }
  recordComponentList(): void {
    if ((!this.beginStaticRuleLinked(152.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2690: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2690.executeStaticRuleBodyLinked(152.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2691: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2691.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2692: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2692.completeStaticRule();
    }
    return null;
  }
  recordDeclaration(): void {
    if ((!this.beginStaticRuleLinked(109.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2693: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2693.executeStaticRuleBodyLinked(109.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2694: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2694.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2695: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2695.completeStaticRule();
    }
    return null;
  }
  recordHeader(): void {
    if ((!this.beginStaticRuleLinked(153.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2696: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2696.executeStaticRuleBodyLinked(153.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2697: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2697.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2698: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2698.completeStaticRule();
    }
    return null;
  }
  relationalExpression(): void {
    if ((!this.beginStaticRuleLinked(41.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2699: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2699.executeStaticRuleBodyLinked(41.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2700: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2700.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2701: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2701.completeStaticRule();
    }
    return null;
  }
  requiresModifier(): void {
    if ((!this.beginStaticRuleLinked(154.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2702: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2702.executeStaticRuleBodyLinked(154.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2703: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2703.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2704: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2704.completeStaticRule();
    }
    return null;
  }
  resource(): void {
    if ((!this.beginStaticRuleLinked(68.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2705: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2705.executeStaticRuleBodyLinked(68.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2706: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2706.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2707: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2707.completeStaticRule();
    }
    return null;
  }
  resourceSpecification(): void {
    if ((!this.beginStaticRuleLinked(69.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2708: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2708.executeStaticRuleBodyLinked(69.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2709: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2709.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2710: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2710.completeStaticRule();
    }
    return null;
  }
  resources(): void {
    if ((!this.beginStaticRuleLinked(70.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2711: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2711.executeStaticRuleBodyLinked(70.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2712: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2712.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2713: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2713.completeStaticRule();
    }
    return null;
  }
  returnStatement(): void {
    if ((!this.beginStaticRuleLinked(71.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2714: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2714.executeStaticRuleBodyLinked(71.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2715: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2715.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2716: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2716.completeStaticRule();
    }
    return null;
  }
  selector(): void {
    if ((!this.beginStaticRuleLinked(42.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2717: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2717.executeStaticRuleBodyLinked(42.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2718: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2718.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2719: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2719.completeStaticRule();
    }
    return null;
  }
  shiftExpression(): void {
    if ((!this.beginStaticRuleLinked(43.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2720: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2720.executeStaticRuleBodyLinked(43.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2721: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2721.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2722: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2722.completeStaticRule();
    }
    return null;
  }
  shiftOperator(): void {
    if ((!this.beginStaticRuleLinked(44.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2723: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2723.executeStaticRuleBodyLinked(44.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2724: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2724.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2725: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2725.completeStaticRule();
    }
    return null;
  }
  statement(): void {
    if ((!this.beginStaticRuleLinked(72.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2726: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2726.executeStaticRuleBodyLinked(72.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2727: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2727.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2728: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2728.completeStaticRule();
    }
    return null;
  }
  statementExpression(): void {
    if ((!this.beginStaticRuleLinked(73.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2729: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2729.executeStaticRuleBodyLinked(73.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2730: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2730.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2731: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2731.completeStaticRule();
    }
    return null;
  }
  superSuffix(): void {
    if ((!this.beginStaticRuleLinked(155.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2732: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2732.executeStaticRuleBodyLinked(155.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2733: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2733.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2734: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2734.completeStaticRule();
    }
    return null;
  }
  switchBlockStatementGroup(): void {
    if ((!this.beginStaticRuleLinked(156.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2735: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2735.executeStaticRuleBodyLinked(156.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2736: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2736.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2737: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2737.completeStaticRule();
    }
    return null;
  }
  switchExpression(): void {
    if ((!this.beginStaticRuleLinked(45.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2738: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2738.executeStaticRuleBodyLinked(45.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2739: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2739.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2740: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2740.completeStaticRule();
    }
    return null;
  }
  switchLabel(): void {
    if ((!this.beginStaticRuleLinked(157.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2741: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2741.executeStaticRuleBodyLinked(157.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2742: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2742.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2743: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2743.completeStaticRule();
    }
    return null;
  }
  switchRule(): void {
    if ((!this.beginStaticRuleLinked(74.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2744: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2744.executeStaticRuleBodyLinked(74.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2745: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2745.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2746: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2746.completeStaticRule();
    }
    return null;
  }
  switchRuleLabel(): void {
    if ((!this.beginStaticRuleLinked(75.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2747: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2747.executeStaticRuleBodyLinked(75.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2748: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2748.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2749: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2749.completeStaticRule();
    }
    return null;
  }
  switchRuleLabelList(): void {
    if ((!this.beginStaticRuleLinked(76.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2750: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2750.executeStaticRuleBodyLinked(76.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2751: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2751.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2752: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2752.completeStaticRule();
    }
    return null;
  }
  switchRuleOutcome(): void {
    if ((!this.beginStaticRuleLinked(77.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2753: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2753.executeStaticRuleBodyLinked(77.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2754: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2754.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2755: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2755.completeStaticRule();
    }
    return null;
  }
  switchStatement(): void {
    if ((!this.beginStaticRuleLinked(78.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2756: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2756.executeStaticRuleBodyLinked(78.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2757: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2757.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2758: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2758.completeStaticRule();
    }
    return null;
  }
  synchronizedStatement(): void {
    if ((!this.beginStaticRuleLinked(79.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2759: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2759.executeStaticRuleBodyLinked(79.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2760: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2760.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2761: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2761.completeStaticRule();
    }
    return null;
  }
  throwStatement(): void {
    if ((!this.beginStaticRuleLinked(80.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2762: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2762.executeStaticRuleBodyLinked(80.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2763: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2763.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2764: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2764.completeStaticRule();
    }
    return null;
  }
  tryHandlerSuffix(): void {
    if ((!this.beginStaticRuleLinked(81.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2765: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2765.executeStaticRuleBodyLinked(81.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2766: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2766.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2767: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2767.completeStaticRule();
    }
    return null;
  }
  tryStatement(): void {
    if ((!this.beginStaticRuleLinked(82.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2768: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2768.executeStaticRuleBodyLinked(82.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2769: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2769.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2770: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2770.completeStaticRule();
    }
    return null;
  }
  typeArgument(): void {
    if ((!this.beginStaticRuleLinked(11.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2771: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2771.executeStaticRuleBodyLinked(11.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2772: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2772.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2773: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2773.completeStaticRule();
    }
    return null;
  }
  typeArguments(): void {
    if ((!this.beginStaticRuleLinked(12.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2774: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2774.executeStaticRuleBodyLinked(12.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2775: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2775.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2776: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2776.completeStaticRule();
    }
    return null;
  }
  typeBound(): void {
    if ((!this.beginStaticRuleLinked(13.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2777: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2777.executeStaticRuleBodyLinked(13.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2778: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2778.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2779: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2779.completeStaticRule();
    }
    return null;
  }
  typeDeclaration(): void {
    if ((!this.beginStaticRuleLinked(158.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2780: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2780.executeStaticRuleBodyLinked(158.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2781: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2781.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2782: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2782.completeStaticRule();
    }
    return null;
  }
  typeIdentifier(): void {
    if ((!this.beginStaticRuleLinked(3.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2783: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2783.executeStaticRuleBodyLinked(3.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2784: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2784.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2785: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2785.completeStaticRule();
    }
    return null;
  }
  typeList(): void {
    if ((!this.beginStaticRuleLinked(14.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2786: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2786.executeStaticRuleBodyLinked(14.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2787: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2787.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2788: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2788.completeStaticRule();
    }
    return null;
  }
  typeParameter(): void {
    if ((!this.beginStaticRuleLinked(15.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2789: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2789.executeStaticRuleBodyLinked(15.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2790: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2790.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2791: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2791.completeStaticRule();
    }
    return null;
  }
  typeParameters(): void {
    if ((!this.beginStaticRuleLinked(16.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2792: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2792.executeStaticRuleBodyLinked(16.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2793: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2793.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2794: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2794.completeStaticRule();
    }
    return null;
  }
  typeType(): void {
    if ((!this.beginStaticRuleLinked(17.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2795: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2795.executeStaticRuleBodyLinked(17.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2796: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2796.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2797: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2797.completeStaticRule();
    }
    return null;
  }
  typeTypeOrVoid(): void {
    if ((!this.beginStaticRuleLinked(18.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2798: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2798.executeStaticRuleBodyLinked(18.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2799: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2799.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2800: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2800.completeStaticRule();
    }
    return null;
  }
  unaryExpression(): void {
    if ((!this.beginStaticRuleLinked(46.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2801: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2801.executeStaticRuleBodyLinked(46.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2802: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2802.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2803: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2803.completeStaticRule();
    }
    return null;
  }
  variableDeclarator(): void {
    if ((!this.beginStaticRuleLinked(110.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2804: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2804.executeStaticRuleBodyLinked(110.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2805: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2805.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2806: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2806.completeStaticRule();
    }
    return null;
  }
  variableDeclaratorId(): void {
    if ((!this.beginStaticRuleLinked(111.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2807: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2807.executeStaticRuleBodyLinked(111.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2808: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2808.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2809: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2809.completeStaticRule();
    }
    return null;
  }
  variableDeclarators(): void {
    if ((!this.beginStaticRuleLinked(112.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2810: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2810.executeStaticRuleBodyLinked(112.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2811: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2811.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2812: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2812.completeStaticRule();
    }
    return null;
  }
  variableInitializer(): void {
    if ((!this.beginStaticRuleLinked(8.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2813: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2813.executeStaticRuleBodyLinked(8.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2814: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2814.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2815: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2815.completeStaticRule();
    }
    return null;
  }
  variableModifier(): void {
    if ((!this.beginStaticRuleLinked(113.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2816: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2816.executeStaticRuleBodyLinked(113.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2817: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2817.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2818: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2818.completeStaticRule();
    }
    return null;
  }
  whileStatement(): void {
    if ((!this.beginStaticRuleLinked(83.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2819: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2819.executeStaticRuleBodyLinked(83.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2820: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2820.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2821: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2821.completeStaticRule();
    }
    return null;
  }
  yieldStatement(): void {
    if ((!this.beginStaticRuleLinked(84.0, ""))) {
      return null;
    }
    try {
      {
        const __qin_typed_receiver_2822: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2822.executeStaticRuleBodyLinked(84.0);
      }
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      {
        const __qin_typed_receiver_2823: com_slime_java_JavaParserStaticEnhanced = this;
        __qin_typed_receiver_2823.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_2824: com_slime_java_JavaParserStaticEnhanced = this;
      __qin_typed_receiver_2824.completeStaticRule();
    }
    return null;
  }
  executeStaticGate(ruleId: number, variantId: number, gateId: number): boolean {
    switch (ruleId) {
      case 9.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentTypeNameDotDoesNotStartClassLiteralSuffix();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 17.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentTypeTypeHasEmptyArraySuffix();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 21.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentArrayCreatorHasSizedDimension();
              break;
            }
            case 0.0: {
              return this.currentArrayCreatorHasEmptyDimension();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 23.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentExpressionLooksLikeAssignment();
              break;
            }
            case 0.0: {
              return this.currentExpressionLooksLikeConditionalExpression();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 30.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentExpressionLooksLikeLambda();
              break;
            }
            case 0.0: {
              return this.currentExpressionLooksLikeAssignmentExpression();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 34.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentLambdaParametersLookLikeSingleIdentifier();
              break;
            }
            case 1.0: {
              return this.currentLambdaParametersLookLikeFormalList();
              break;
            }
            case 2.0: {
              return this.currentLambdaParametersLookLikeInferredList();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 37.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentPostfixExpressionLooksLikeMethodReference();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 40.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 5.0: {
              return this.currentPrimaryLooksLikeThisMethodReference();
              break;
            }
            case 6.0: {
              return this.currentPrimaryLooksLikeSuperMethodReference();
              break;
            }
            case 1.0: {
              return this.currentPrimaryLooksLikePlainThis();
              break;
            }
            case 2.0: {
              return this.currentPrimaryLooksLikePlainSuper();
              break;
            }
            case 4.0: {
              return this.currentPrimaryLooksLikeTypeMethodReference();
              break;
            }
            case 0.0: {
              return this.currentPrimaryLooksLikeTypeClassLiteral();
              break;
            }
            case 3.0: {
              return this.currentPrimaryLooksLikeIdentifierPrimary();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 41.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentRelationalExpressionHasComparisonOperator();
              break;
            }
            case 3.0: {
              return this.currentRelationalExpressionHasInstanceof();
              break;
            }
            case 1.0: {
              return this.currentInstanceofLooksLikePattern();
              break;
            }
            case 2.0: {
              return this.currentInstanceofLooksLikeTypeOnly();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 42.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentLooksLikeArguments();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 43.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentShiftExpressionHasShiftOperator();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 44.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentShiftOperatorLooksLikeLeftShift();
              break;
            }
            case 0.0: {
              return this.currentShiftOperatorLooksLikeRightShift();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 46.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentUnaryExpressionLooksLikePrefix();
              break;
            }
            case 2.0: {
              return this.currentUnaryExpressionLooksLikeCast();
              break;
            }
            case 0.0: {
              return this.currentUnaryExpressionLooksLikePostfix();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 48.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentBlockStatementLooksLikeLocalVariableDeclaration();
              break;
            }
            case 2.0: {
              return this.currentBlockStatementLooksLikeLocalTypeDeclaration();
              break;
            }
            case 1.0: {
              return this.currentBlockStatementLooksLikeStatement();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 59.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentForControlLooksLikeEnhancedFor();
              break;
            }
            case 0.0: {
              return this.currentForControlLooksLikeBasicFor();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 60.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentForInitLooksLikeLocalVariableDeclaration();
              break;
            }
            case 0.0: {
              return this.currentForInitLooksLikeExpressionList();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 68.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentResourceLooksLikeDeclaration();
              break;
            }
            case 0.0: {
              return this.currentResourceLooksLikeIdentifier();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 72.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 2.0: {
              return this.currentStatementLooksLikeSwitchStatement();
              break;
            }
            case 3.0: {
              return this.currentStatementLooksLikeLabeledStatement();
              break;
            }
            case 0.0: {
              return this.currentStatementLooksLikeExplicitConstructorInvocation();
              break;
            }
            case 1.0: {
              return this.currentStatementLooksLikeExpressionStatement();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 75.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentSwitchRuleLabelLooksLikePattern();
              break;
            }
            case 0.0: {
              return this.currentSwitchRuleLabelLooksLikeExpression();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 78.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentSwitchEntryLooksLikeBlockStatementGroup();
              break;
            }
            case 2.0: {
              return this.currentSwitchEntryLooksLikeRule();
              break;
            }
            case 1.0: {
              return this.currentSwitchEntryLooksLikeTrailingLabel();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 82.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentTryStatementLooksLikeResourceTry();
              break;
            }
            case 0.0: {
              return this.currentTryStatementLooksLikePlainTry();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 87.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentClassBodyDeclarationLooksLikeEmpty();
              break;
            }
            case 1.0: {
              return this.currentClassBodyDeclarationLooksLikeInitializerBlock();
              break;
            }
            case 2.0: {
              return this.currentClassBodyDeclarationLooksLikeMember();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 89.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentClassOrInterfaceModifierLooksLikeAnnotation();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 102.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 7.0: {
              return this.interfaceMemberDeclarationLooksLikeRecord();
              break;
            }
            case 1.0: {
              return this.interfaceMemberDeclarationLooksLikeConst();
              break;
            }
            case 3.0: {
              return this.interfaceMemberDeclarationLooksLikeGenericMethod();
              break;
            }
            case 4.0: {
              return this.interfaceMemberDeclarationLooksLikeMethod();
              break;
            }
            case 5.0: {
              return this.interfaceMemberDeclarationLooksLikeInterface();
              break;
            }
            case 6.0: {
              return this.interfaceMemberDeclarationLooksLikeAnnotationType();
              break;
            }
            case 0.0: {
              return this.interfaceMemberDeclarationLooksLikeClass();
              break;
            }
            case 2.0: {
              return this.interfaceMemberDeclarationLooksLikeEnum();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 105.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 8.0: {
              return this.memberDeclarationLooksLikeRecord();
              break;
            }
            case 0.0: {
              return this.memberDeclarationLooksLikeGenericConstructor();
              break;
            }
            case 2.0: {
              return this.memberDeclarationLooksLikeConstructor();
              break;
            }
            case 4.0: {
              return this.memberDeclarationLooksLikeGenericMethod();
              break;
            }
            case 5.0: {
              return this.memberDeclarationLooksLikeMethod();
              break;
            }
            case 6.0: {
              return this.memberDeclarationLooksLikeField();
              break;
            }
            case 7.0: {
              return this.memberDeclarationLooksLikeInterface();
              break;
            }
            case 9.0: {
              return this.memberDeclarationLooksLikeAnnotationType();
              break;
            }
            case 1.0: {
              return this.memberDeclarationLooksLikeClass();
              break;
            }
            case 3.0: {
              return this.memberDeclarationLooksLikeEnum();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 114.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentAnnotationValueLooksLikePairs();
              break;
            }
            case 0.0: {
              return this.currentAnnotationValueLooksLikeSingleValue();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 116.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentAnnotationMethodOrConstantRestLooksLikeMethod();
              break;
            }
            case 0.0: {
              return this.currentAnnotationMethodOrConstantRestLooksLikeConstant();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 126.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentElementValueLooksLikeAnnotation();
              break;
            }
            case 2.0: {
              return this.currentElementValueLooksLikeArrayInitializer();
              break;
            }
            case 1.0: {
              return this.currentElementValueLooksLikeExpression();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 136.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentParameterIsNotVarargs();
              break;
            }
            case 2.0: {
              return this.commaFollowedByNonVarargsParameter();
              break;
            }
            case 1.0: {
              return this.commaFollowedByVarargsParameter();
              break;
            }
            case 3.0: {
              return this.currentParameterIsVarargs();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 137.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentFormalParametersStartFormalList();
              break;
            }
            case 0.0: {
              return this.currentFormalParametersStartReceiver();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 139.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentImportQualifiedNameContinuesWithIdentifierSegment();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 140.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return (!this.match("DOT") || this.lookahead("MUL", 2.0));
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 150.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentRecordBodyLooksLikeCompactConstructor();
              break;
            }
            case 0.0: {
              return this.currentRecordBodyLooksLikeClassBodyDeclaration();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      case 158.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentTypeDeclarationLooksLikeClassOrInterfaceModifier();
              break;
            }
            default: {
              break;
            }
          }
        }
        break;
      }
      default: {
        break;
      }
    }
    return super.executeStaticGate(ruleId, variantId, gateId);
  }
  executeStaticSubrule(ruleId: number, variantId: number, invocationArgument: any): void {
    switch (ruleId) {
      case 0.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2825: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2825.identifier();
          }
          return null;
        }
        break;
      }
      case 1.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2826: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2826.qualifiedName();
          }
          return null;
        }
        break;
      }
      case 2.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2827: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2827.qualifiedNameList();
          }
          return null;
        }
        break;
      }
      case 3.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2828: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2828.typeIdentifier();
          }
          return null;
        }
        break;
      }
      case 4.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2829: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2829.arrayInitializer();
          }
          return null;
        }
        break;
      }
      case 5.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2830: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2830.floatingPointLiteral();
          }
          return null;
        }
        break;
      }
      case 6.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2831: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2831.integerLiteral();
          }
          return null;
        }
        break;
      }
      case 7.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2832: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2832.literal();
          }
          return null;
        }
        break;
      }
      case 8.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2833: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2833.variableInitializer();
          }
          return null;
        }
        break;
      }
      case 9.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2834: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2834.classOrInterfaceType();
          }
          return null;
        }
        break;
      }
      case 10.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2835: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2835.primitiveType();
          }
          return null;
        }
        break;
      }
      case 11.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2836: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2836.typeArgument();
          }
          return null;
        }
        break;
      }
      case 12.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2837: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2837.typeArguments();
          }
          return null;
        }
        break;
      }
      case 13.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2838: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2838.typeBound();
          }
          return null;
        }
        break;
      }
      case 14.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2839: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2839.typeList();
          }
          return null;
        }
        break;
      }
      case 15.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2840: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2840.typeParameter();
          }
          return null;
        }
        break;
      }
      case 16.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2841: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2841.typeParameters();
          }
          return null;
        }
        break;
      }
      case 17.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2842: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2842.typeType();
          }
          return null;
        }
        break;
      }
      case 18.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2843: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2843.typeTypeOrVoid();
          }
          return null;
        }
        break;
      }
      case 19.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2844: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2844.additiveExpression();
          }
          return null;
        }
        break;
      }
      case 20.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2845: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2845.andExpression();
          }
          return null;
        }
        break;
      }
      case 21.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2846: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2846.arrayCreatorDimensions();
          }
          return null;
        }
        break;
      }
      case 22.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2847: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2847.assignment();
          }
          return null;
        }
        break;
      }
      case 23.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2848: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2848.assignmentExpression();
          }
          return null;
        }
        break;
      }
      case 24.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2849: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2849.assignmentOperator();
          }
          return null;
        }
        break;
      }
      case 25.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2850: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2850.conditionalAndExpression();
          }
          return null;
        }
        break;
      }
      case 26.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2851: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2851.conditionalExpression();
          }
          return null;
        }
        break;
      }
      case 27.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2852: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2852.conditionalOrExpression();
          }
          return null;
        }
        break;
      }
      case 28.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2853: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2853.equalityExpression();
          }
          return null;
        }
        break;
      }
      case 29.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2854: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2854.exclusiveOrExpression();
          }
          return null;
        }
        break;
      }
      case 30.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2855: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2855.expression();
          }
          return null;
        }
        break;
      }
      case 31.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2856: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2856.inclusiveOrExpression();
          }
          return null;
        }
        break;
      }
      case 32.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2857: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2857.lambdaBody();
          }
          return null;
        }
        break;
      }
      case 33.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2858: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2858.lambdaExpression();
          }
          return null;
        }
        break;
      }
      case 34.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2859: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2859.lambdaParameters();
          }
          return null;
        }
        break;
      }
      case 35.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2860: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2860.leftHandSide();
          }
          return null;
        }
        break;
      }
      case 36.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2861: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2861.multiplicativeExpression();
          }
          return null;
        }
        break;
      }
      case 37.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2862: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2862.postfixExpression();
          }
          return null;
        }
        break;
      }
      case 38.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2863: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2863.postfixOp();
          }
          return null;
        }
        break;
      }
      case 39.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2864: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2864.prefixOp();
          }
          return null;
        }
        break;
      }
      case 40.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2865: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2865.primary();
          }
          return null;
        }
        break;
      }
      case 41.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2866: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2866.relationalExpression();
          }
          return null;
        }
        break;
      }
      case 42.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2867: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2867.selector();
          }
          return null;
        }
        break;
      }
      case 43.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2868: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2868.shiftExpression();
          }
          return null;
        }
        break;
      }
      case 44.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2869: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2869.shiftOperator();
          }
          return null;
        }
        break;
      }
      case 45.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2870: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2870.switchExpression();
          }
          return null;
        }
        break;
      }
      case 46.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2871: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2871.unaryExpression();
          }
          return null;
        }
        break;
      }
      case 47.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2872: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2872.block();
          }
          return null;
        }
        break;
      }
      case 48.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2873: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2873.blockStatement();
          }
          return null;
        }
        break;
      }
      case 49.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2874: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2874.breakStatement();
          }
          return null;
        }
        break;
      }
      case 50.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2875: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2875.catchClause();
          }
          return null;
        }
        break;
      }
      case 51.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2876: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2876.catchType();
          }
          return null;
        }
        break;
      }
      case 52.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2877: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2877.continueStatement();
          }
          return null;
        }
        break;
      }
      case 53.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2878: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2878.doWhileStatement();
          }
          return null;
        }
        break;
      }
      case 54.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2879: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2879.enhancedForControl();
          }
          return null;
        }
        break;
      }
      case 55.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2880: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2880.explicitConstructorInvocationStatement();
          }
          return null;
        }
        break;
      }
      case 56.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2881: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2881.expressionList();
          }
          return null;
        }
        break;
      }
      case 57.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2882: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2882.expressionStatement();
          }
          return null;
        }
        break;
      }
      case 58.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2883: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2883.finallyBlock();
          }
          return null;
        }
        break;
      }
      case 59.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2884: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2884.forControl();
          }
          return null;
        }
        break;
      }
      case 60.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2885: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2885.forInit();
          }
          return null;
        }
        break;
      }
      case 61.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2886: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2886.forStatement();
          }
          return null;
        }
        break;
      }
      case 62.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2887: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2887.forUpdate();
          }
          return null;
        }
        break;
      }
      case 63.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2888: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2888.ifStatement();
          }
          return null;
        }
        break;
      }
      case 64.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2889: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2889.labeledStatement();
          }
          return null;
        }
        break;
      }
      case 65.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2890: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2890.localTypeDeclaration();
          }
          return null;
        }
        break;
      }
      case 66.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2891: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2891.localVariableDeclaration();
          }
          return null;
        }
        break;
      }
      case 67.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2892: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2892.parExpression();
          }
          return null;
        }
        break;
      }
      case 68.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2893: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2893.resource();
          }
          return null;
        }
        break;
      }
      case 69.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2894: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2894.resourceSpecification();
          }
          return null;
        }
        break;
      }
      case 70.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2895: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2895.resources();
          }
          return null;
        }
        break;
      }
      case 71.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2896: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2896.returnStatement();
          }
          return null;
        }
        break;
      }
      case 72.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2897: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2897.statement();
          }
          return null;
        }
        break;
      }
      case 73.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2898: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2898.statementExpression();
          }
          return null;
        }
        break;
      }
      case 74.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2899: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2899.switchRule();
          }
          return null;
        }
        break;
      }
      case 75.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2900: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2900.switchRuleLabel();
          }
          return null;
        }
        break;
      }
      case 76.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2901: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2901.switchRuleLabelList();
          }
          return null;
        }
        break;
      }
      case 77.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2902: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2902.switchRuleOutcome();
          }
          return null;
        }
        break;
      }
      case 78.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2903: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2903.switchStatement();
          }
          return null;
        }
        break;
      }
      case 79.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2904: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2904.synchronizedStatement();
          }
          return null;
        }
        break;
      }
      case 80.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2905: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2905.throwStatement();
          }
          return null;
        }
        break;
      }
      case 81.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2906: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2906.tryHandlerSuffix();
          }
          return null;
        }
        break;
      }
      case 82.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2907: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2907.tryStatement();
          }
          return null;
        }
        break;
      }
      case 83.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2908: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2908.whileStatement();
          }
          return null;
        }
        break;
      }
      case 84.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2909: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2909.yieldStatement();
          }
          return null;
        }
        break;
      }
      case 85.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2910: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2910.annotationTypeDeclaration();
          }
          return null;
        }
        break;
      }
      case 86.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2911: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2911.classBody();
          }
          return null;
        }
        break;
      }
      case 87.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2912: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2912.classBodyDeclaration();
          }
          return null;
        }
        break;
      }
      case 88.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2913: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2913.classDeclaration();
          }
          return null;
        }
        break;
      }
      case 89.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2914: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2914.classOrInterfaceModifier();
          }
          return null;
        }
        break;
      }
      case 90.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2915: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2915.constDeclaration();
          }
          return null;
        }
        break;
      }
      case 91.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2916: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2916.constantDeclarator();
          }
          return null;
        }
        break;
      }
      case 92.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2917: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2917.constructorDeclaration();
          }
          return null;
        }
        break;
      }
      case 93.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2918: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2918.enumDeclaration();
          }
          return null;
        }
        break;
      }
      case 94.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2919: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2919.fieldDeclaration();
          }
          return null;
        }
        break;
      }
      case 95.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2920: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2920.genericConstructorDeclaration();
          }
          return null;
        }
        break;
      }
      case 96.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2921: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2921.genericInterfaceMethodDeclaration();
          }
          return null;
        }
        break;
      }
      case 97.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2922: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2922.genericMethodDeclaration();
          }
          return null;
        }
        break;
      }
      case 98.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2923: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2923.interfaceBody();
          }
          return null;
        }
        break;
      }
      case 99.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2924: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2924.interfaceBodyDeclaration();
          }
          return null;
        }
        break;
      }
      case 100.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2925: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2925.interfaceCommonBodyDeclaration();
          }
          return null;
        }
        break;
      }
      case 101.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2926: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2926.interfaceDeclaration();
          }
          return null;
        }
        break;
      }
      case 102.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2927: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2927.interfaceMemberDeclaration();
          }
          return null;
        }
        break;
      }
      case 103.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2928: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2928.interfaceMethodDeclaration();
          }
          return null;
        }
        break;
      }
      case 104.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2929: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2929.interfaceModifier();
          }
          return null;
        }
        break;
      }
      case 105.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2930: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2930.memberDeclaration();
          }
          return null;
        }
        break;
      }
      case 106.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2931: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2931.methodBody();
          }
          return null;
        }
        break;
      }
      case 107.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2932: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2932.methodDeclaration();
          }
          return null;
        }
        break;
      }
      case 108.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2933: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2933.modifier();
          }
          return null;
        }
        break;
      }
      case 109.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2934: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2934.recordDeclaration();
          }
          return null;
        }
        break;
      }
      case 110.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2935: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2935.variableDeclarator();
          }
          return null;
        }
        break;
      }
      case 111.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2936: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2936.variableDeclaratorId();
          }
          return null;
        }
        break;
      }
      case 112.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2937: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2937.variableDeclarators();
          }
          return null;
        }
        break;
      }
      case 113.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2938: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2938.variableModifier();
          }
          return null;
        }
        break;
      }
      case 114.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2939: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2939.annotation();
          }
          return null;
        }
        break;
      }
      case 115.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2940: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2940.annotationConstantRest();
          }
          return null;
        }
        break;
      }
      case 116.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2941: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2941.annotationMethodOrConstantRest();
          }
          return null;
        }
        break;
      }
      case 117.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2942: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2942.annotationMethodRest();
          }
          return null;
        }
        break;
      }
      case 118.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2943: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2943.annotationTypeBody();
          }
          return null;
        }
        break;
      }
      case 119.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2944: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2944.annotationTypeElementDeclaration();
          }
          return null;
        }
        break;
      }
      case 120.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2945: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2945.annotationTypeElementRest();
          }
          return null;
        }
        break;
      }
      case 121.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2946: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2946.arguments();
          }
          return null;
        }
        break;
      }
      case 122.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2947: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2947.classCreatorRest();
          }
          return null;
        }
        break;
      }
      case 123.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2948: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2948.compactConstructorDeclaration();
          }
          return null;
        }
        break;
      }
      case 124.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2949: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2949.compilationUnit();
          }
          return null;
        }
        break;
      }
      case 125.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2950: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2950.defaultValue();
          }
          return null;
        }
        break;
      }
      case 126.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2951: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2951.elementValue();
          }
          return null;
        }
        break;
      }
      case 127.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2952: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2952.elementValueArrayInitializer();
          }
          return null;
        }
        break;
      }
      case 128.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2953: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2953.elementValuePair();
          }
          return null;
        }
        break;
      }
      case 129.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2954: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2954.elementValuePairs();
          }
          return null;
        }
        break;
      }
      case 130.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2955: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2955.enumBodyDeclarations();
          }
          return null;
        }
        break;
      }
      case 131.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2956: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2956.enumConstant();
          }
          return null;
        }
        break;
      }
      case 132.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2957: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2957.enumConstants();
          }
          return null;
        }
        break;
      }
      case 133.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2958: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2958.explicitGenericInvocation();
          }
          return null;
        }
        break;
      }
      case 134.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2959: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2959.explicitGenericInvocationSuffix();
          }
          return null;
        }
        break;
      }
      case 135.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2960: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2960.formalParameter();
          }
          return null;
        }
        break;
      }
      case 136.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2961: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2961.formalParameterList();
          }
          return null;
        }
        break;
      }
      case 137.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2962: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2962.formalParameters();
          }
          return null;
        }
        break;
      }
      case 138.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2963: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2963.importDeclaration();
          }
          return null;
        }
        break;
      }
      case 139.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2964: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2964.importQualifiedName();
          }
          return null;
        }
        break;
      }
      case 140.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2965: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2965.importQualifiedNameSegment();
          }
          return null;
        }
        break;
      }
      case 141.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2966: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2966.innerCreator();
          }
          return null;
        }
        break;
      }
      case 142.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2967: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2967.lastFormalParameter();
          }
          return null;
        }
        break;
      }
      case 143.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2968: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2968.moduleDeclaration();
          }
          return null;
        }
        break;
      }
      case 144.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2969: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2969.moduleDirective();
          }
          return null;
        }
        break;
      }
      case 145.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2970: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2970.nonWildcardTypeArguments();
          }
          return null;
        }
        break;
      }
      case 146.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2971: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2971.nonWildcardTypeArgumentsOrDiamond();
          }
          return null;
        }
        break;
      }
      case 147.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2972: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2972.packageDeclaration();
          }
          return null;
        }
        break;
      }
      case 148.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2973: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2973.pattern();
          }
          return null;
        }
        break;
      }
      case 149.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2974: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2974.receiverParameter();
          }
          return null;
        }
        break;
      }
      case 150.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2975: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2975.recordBody();
          }
          return null;
        }
        break;
      }
      case 151.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2976: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2976.recordComponent();
          }
          return null;
        }
        break;
      }
      case 152.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2977: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2977.recordComponentList();
          }
          return null;
        }
        break;
      }
      case 153.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2978: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2978.recordHeader();
          }
          return null;
        }
        break;
      }
      case 154.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2979: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2979.requiresModifier();
          }
          return null;
        }
        break;
      }
      case 155.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2980: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2980.superSuffix();
          }
          return null;
        }
        break;
      }
      case 156.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2981: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2981.switchBlockStatementGroup();
          }
          return null;
        }
        break;
      }
      case 157.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2982: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2982.switchLabel();
          }
          return null;
        }
        break;
      }
      case 158.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          {
            const __qin_typed_receiver_2983: com_slime_java_JavaParserStaticEnhanced = this;
            __qin_typed_receiver_2983.typeDeclaration();
          }
          return null;
        }
        break;
      }
      default: {
        break;
      }
    }
    super.executeStaticSubrule(ruleId, variantId, invocationArgument);
    return null;
  }
  static subhutiStaticRuleVariants(): __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant> {
    let variants: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant> = new __QinJavaUtilArrayList(159.0);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_0(variants);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_1(variants);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_2(variants);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_3(variants);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_4(variants);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_5(variants);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_6(variants);
    return __QinJavaUtilList.copyOf(variants);
  }
  static subhutiStaticRuleVariants_0(variants: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant>): void {
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(0.0, 0.0, "identifier", "identifier@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 35.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 36.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "IDENTIFIER", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 36.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 38.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "MODULE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 38.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 39.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "OPEN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 39.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 40.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "REQUIRES", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 40.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 41.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "EXPORTS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 41.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 42.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "OPENS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 42.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 43.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "TO", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 43.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 44.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 15.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "USES", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 44.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 45.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 17.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "PROVIDES", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 45.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 46.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(20.0, 19.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "WITH", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 46.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(21.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 47.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(22.0, 21.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "TRANSITIVE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 47.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(23.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 49.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(24.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEALED", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 49.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(25.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 50.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(26.0, 25.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "PERMITS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 50.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(27.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 51.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(28.0, 27.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RECORD", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 51.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(29.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 52.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(30.0, 29.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "VAR", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 52.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(31.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 53.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(32.0, 31.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "YIELD", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 53.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(33.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 54.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(34.0, 33.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "WHEN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 54.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(1.0, 0.0, "qualifiedName", "qualifiedName@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 96.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 97.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 97.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DOT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 98.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 99.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(2.0, 0.0, "qualifiedNameList", "qualifiedNameList@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 110.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 111.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 111.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 112.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 113.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(3.0, 0.0, "typeIdentifier", "typeIdentifier@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 69.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 70.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "IDENTIFIER", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 70.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 72.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "MODULE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 72.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 73.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "OPEN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 73.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 74.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "REQUIRES", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 74.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 75.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "EXPORTS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 75.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 76.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "OPENS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 76.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 77.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "TO", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 77.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 78.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 15.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "USES", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 78.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 79.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 17.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "PROVIDES", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 79.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 80.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(20.0, 19.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "WITH", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 80.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(21.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 81.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(22.0, 21.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "TRANSITIVE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 81.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(23.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 83.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(24.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEALED", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 83.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(25.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 84.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(26.0, 25.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "PERMITS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 84.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(27.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 85.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(28.0, 27.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "WHEN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 85.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(4.0, 0.0, "arrayInitializer", "arrayInitializer@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 88.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 89.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 89.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableInitializer", null, 8.0, 0.0, false, false, 90.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 91.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 91.0, 18.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 92.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableInitializer", null, 8.0, 0.0, false, false, 93.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 95.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 95.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 95.0, 26.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 97.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(5.0, 0.0, "floatingPointLiteral", "floatingPointLiteral@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 75.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 76.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "FLOAT_LITERAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 76.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 77.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "HEX_FLOAT_LITERAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 77.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(6.0, 0.0, "integerLiteral", "integerLiteral@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 59.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 60.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DECIMAL_LITERAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 60.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 61.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "HEX_LITERAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 61.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 62.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "OCT_LITERAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 62.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 63.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "BINARY_LITERAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 63.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(7.0, 0.0, "literal", "literal@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 38.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 39.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "integerLiteral", null, 6.0, 0.0, false, false, 39.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 40.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "floatingPointLiteral", null, 5.0, 0.0, false, false, 40.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 41.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "BOOL_LITERAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 41.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 42.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "CHAR_LITERAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 42.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 43.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "STRING_LITERAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 43.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 44.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "TEXT_BLOCK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 44.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 45.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "NULL_LITERAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 45.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(8.0, 0.0, "variableInitializer", "variableInitializer@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 108.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 109.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "arrayInitializer", null, 4.0, 0.0, false, false, 109.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 110.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 110.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(9.0, 0.0, "classOrInterfaceType", "classOrInterfaceType@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeIdentifier", null, 3.0, 0.0, false, false, 99.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 100.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 100.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeArguments", null, 12.0, 0.0, false, false, 100.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 101.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 101.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.type.JavaTypeParser#currentTypeNameDotDoesNotStartClassLiteralSuffix", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 101.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DOT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 102.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeIdentifier", null, 3.0, 0.0, false, false, 103.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 104.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 104.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeArguments", null, 12.0, 0.0, false, false, 104.0, 26.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(6.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(6.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(10.0, 0.0, "primitiveType", "primitiveType@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 80.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 81.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "BOOLEAN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 81.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 82.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "CHAR", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 82.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 83.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "BYTE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 83.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 84.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SHORT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 84.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 85.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "INT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 85.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 86.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LONG", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 86.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 87.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "FLOAT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 87.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 88.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 15.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DOUBLE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 88.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(11.0, 0.0, "typeArgument", "typeArgument@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 185.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 186.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 186.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 187.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 188.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 188.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotation", null, 114.0, 0.0, false, false, 188.0, 28.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "QUESTION", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 189.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 190.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 190.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 191.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 192.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "EXTENDS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 192.0, 31.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 193.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SUPER", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 193.0, 31.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 195.0, 21.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(12.0, 0.0, "typeArguments", "typeArguments@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 166.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 167.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 167.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeArgument", null, 11.0, 0.0, false, false, 168.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 169.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 169.0, 18.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 170.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeArgument", null, 11.0, 0.0, false, false, 171.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "GT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 174.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(13.0, 0.0, "typeBound", "typeBound@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 152.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 153.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 153.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "BITAND", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 154.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 155.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(14.0, 0.0, "typeList", "typeList@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 208.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 209.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 209.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 210.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 211.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(15.0, 0.0, "typeParameter", "typeParameter@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 136.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 136.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotation", null, 114.0, 0.0, false, false, 136.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeIdentifier", null, 3.0, 0.0, false, false, 137.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 138.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 138.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "EXTENDS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 139.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 140.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 140.0, 18.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotation", null, 114.0, 0.0, false, false, 140.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeBound", null, 13.0, 0.0, false, false, 141.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(16.0, 0.0, "typeParameters", "typeParameters@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 120.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeParameter", null, 15.0, 0.0, false, false, 121.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 122.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 122.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 123.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeParameter", null, 15.0, 0.0, false, false, 124.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "GT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 126.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(17.0, 0.0, "typeType", "typeType@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 36.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 36.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotation", null, 114.0, 0.0, false, false, 36.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 37.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 38.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classOrInterfaceType", null, 9.0, 0.0, false, false, 38.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 39.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "primitiveType", null, 10.0, 0.0, false, false, 39.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 41.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 41.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.type.JavaTypeParser#currentTypeTypeHasEmptyArraySuffix", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 41.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 42.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 42.0, 18.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotation", null, 114.0, 0.0, false, false, 42.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 43.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 44.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(10.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(10.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(18.0, 0.0, "typeTypeOrVoid", "typeTypeOrVoid@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 67.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 68.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 68.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 69.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "VOID", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 69.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(19.0, 0.0, "additiveExpression", "additiveExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "multiplicativeExpression", null, 36.0, 0.0, false, false, 748.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 749.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 749.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 750.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 751.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ADD", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 751.0, 23.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 752.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SUB", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 752.0, 23.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "multiplicativeExpression", null, 36.0, 0.0, false, false, 754.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(20.0, 0.0, "andExpression", "andExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "equalityExpression", null, 28.0, 0.0, false, false, 621.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 622.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 622.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "BITAND", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 623.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "equalityExpression", null, 28.0, 0.0, false, false, 624.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(21.0, 0.0, "arrayCreatorDimensions", "arrayCreatorDimensions@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_AT_LEAST_ONE, "AtLeastOne", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1061.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1061.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentArrayCreatorHasSizedDimension", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1061.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1062.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 1063.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1064.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1066.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1066.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentArrayCreatorHasEmptyDimension", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1066.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1067.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1068.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 1.0), __QinJavaUtilHashMap.entry(8.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 8.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(22.0, 0.0, "assignment", "assignment@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "leftHandSide", null, 35.0, 0.0, false, false, 501.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "assignmentOperator", null, 24.0, 0.0, false, false, 502.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 503.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(23.0, 0.0, "assignmentExpression", "assignmentExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 426.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 427.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentExpressionLooksLikeAssignment", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 427.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "assignment", null, 22.0, 0.0, false, false, 427.0, 68.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 428.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentExpressionLooksLikeConditionalExpression", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 428.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "conditionalExpression", null, 26.0, 0.0, false, false, 428.0, 79.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 1.0), __QinJavaUtilHashMap.entry(5.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 5.0)));
    return null;
  }
  static subhutiStaticRuleVariants_1(variants: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant>): void {
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(24.0, 0.0, "assignmentOperator", "assignmentOperator@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 526.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 527.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 527.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 528.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ADD_ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 528.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 529.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SUB_ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 529.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 530.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "MUL_ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 530.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 531.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DIV_ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 531.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 532.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "AND_ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 532.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 533.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "OR_ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 533.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 534.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 15.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "XOR_ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 534.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 535.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 17.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "MOD_ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 535.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 536.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(20.0, 19.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LSHIFT_ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 536.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(21.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 537.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(22.0, 21.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RSHIFT_ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 537.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(23.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 538.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(24.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "URSHIFT_ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 538.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(25.0, 0.0, "conditionalAndExpression", "conditionalAndExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "inclusiveOrExpression", null, 31.0, 0.0, false, false, 579.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 580.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 580.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "AND", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 581.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "inclusiveOrExpression", null, 31.0, 0.0, false, false, 582.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(26.0, 0.0, "conditionalExpression", "conditionalExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "conditionalOrExpression", null, 27.0, 0.0, false, false, 549.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 550.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 550.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "QUESTION", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 551.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 552.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COLON", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 553.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "conditionalExpression", null, 26.0, 0.0, false, false, 554.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(27.0, 0.0, "conditionalOrExpression", "conditionalOrExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "conditionalAndExpression", null, 25.0, 0.0, false, false, 565.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 566.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 566.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "OR", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 567.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "conditionalAndExpression", null, 25.0, 0.0, false, false, 568.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(28.0, 0.0, "equalityExpression", "equalityExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "relationalExpression", null, 41.0, 0.0, false, false, 635.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 636.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 636.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 637.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 638.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "EQUAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 638.0, 23.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 639.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "NOTEQUAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 639.0, 23.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "relationalExpression", null, 41.0, 0.0, false, false, 641.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(29.0, 0.0, "exclusiveOrExpression", "exclusiveOrExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "andExpression", null, 20.0, 0.0, false, false, 607.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 608.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 608.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "CARET", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 609.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "andExpression", null, 20.0, 0.0, false, false, 610.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(30.0, 0.0, "expression", "expression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 40.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 41.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentExpressionLooksLikeLambda", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 41.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "lambdaExpression", null, 33.0, 0.0, false, false, 41.0, 64.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 42.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentExpressionLooksLikeAssignmentExpression", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 42.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "assignmentExpression", null, 23.0, 0.0, false, false, 42.0, 78.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 1.0), __QinJavaUtilHashMap.entry(5.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 5.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(31.0, 0.0, "inclusiveOrExpression", "inclusiveOrExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "exclusiveOrExpression", null, 29.0, 0.0, false, false, 593.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 594.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 594.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "BITOR", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 595.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "exclusiveOrExpression", null, 29.0, 0.0, false, false, 596.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(32.0, 0.0, "lambdaBody", "lambdaBody@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1048.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1049.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 1049.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1050.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "block", null, 47.0, 0.0, false, false, 1050.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(33.0, 0.0, "lambdaExpression", "lambdaExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "lambdaParameters", null, 34.0, 0.0, false, false, 974.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ARROW", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 975.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "lambdaBody", null, 32.0, 0.0, false, false, 976.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(34.0, 0.0, "lambdaParameters", "lambdaParameters@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 988.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 989.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentLambdaParametersLookLikeSingleIdentifier", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 989.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 989.0, 79.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 990.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentLambdaParametersLookLikeFormalList", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 990.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 991.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 992.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 992.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "formalParameterList", null, 136.0, 0.0, false, false, 992.0, 30.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 993.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 995.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentLambdaParametersLookLikeInferredList", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 995.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 996.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 997.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 998.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 15.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 998.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 16.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 999.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 16.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 1000.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1002.0, 17.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 0.0), __QinJavaUtilHashMap.entry(5.0, 1.0), __QinJavaUtilHashMap.entry(12.0, 2.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 5.0, 12.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(35.0, 0.0, "leftHandSide", "leftHandSide@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "postfixExpression", null, 37.0, 0.0, false, false, 516.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(36.0, 0.0, "multiplicativeExpression", "multiplicativeExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "unaryExpression", null, 46.0, 0.0, false, false, 765.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 766.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 766.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 767.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 768.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "MUL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 768.0, 23.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 769.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DIV", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 769.0, 23.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 770.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "MOD", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 770.0, 23.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "unaryExpression", null, 46.0, 0.0, false, false, 772.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(37.0, 0.0, "postfixExpression", "postfixExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "primary", null, 40.0, 0.0, false, false, 900.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 901.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 901.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "selector", null, 42.0, 0.0, false, false, 901.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 902.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 902.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentPostfixExpressionLooksLikeMethodReference", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 902.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COLONCOLON", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 903.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 904.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 906.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 906.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "postfixOp", null, 38.0, 0.0, false, false, 906.0, 20.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(6.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(6.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(38.0, 0.0, "postfixOp", "postfixOp@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 961.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 962.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "INC", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 962.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 963.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DEC", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 963.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(39.0, 0.0, "prefixOp", "prefixOp@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 883.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 884.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "INC", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 884.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 885.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DEC", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 885.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 886.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "BANG", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 886.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 887.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "TILDE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 887.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 888.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ADD", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 888.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 889.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SUB", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 889.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(40.0, 0.0, "primary", "primary@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 118.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 119.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "switchExpression", null, 45.0, 0.0, false, false, 119.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 120.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 121.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 122.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 123.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 125.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentPrimaryLooksLikeThisMethodReference", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 125.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "THIS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 126.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COLONCOLON", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 127.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 128.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 130.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentPrimaryLooksLikeSuperMethodReference", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 130.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SUPER", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 131.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COLONCOLON", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 132.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 133.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 135.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 17.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentPrimaryLooksLikePlainThis", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 135.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 18.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "THIS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 135.0, 64.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(20.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 136.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(21.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentPrimaryLooksLikePlainSuper", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 136.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(22.0, 21.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SUPER", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 136.0, 65.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(23.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 137.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(24.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "NEW", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 138.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(25.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 139.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(26.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 140.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(27.0, 26.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 141.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(28.0, 27.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classCreatorRest", null, 122.0, 0.0, false, false, 141.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(29.0, 26.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 142.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(30.0, 29.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "arrayInitializer", null, 4.0, 0.0, false, false, 142.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(31.0, 26.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 143.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(32.0, 31.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "arrayCreatorDimensions", null, 21.0, 0.0, false, false, 143.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(33.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 146.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(34.0, 33.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "literal", null, 7.0, 0.0, false, false, 146.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(35.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 147.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(36.0, 35.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentPrimaryLooksLikeTypeMethodReference", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 147.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(37.0, 36.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 148.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(38.0, 36.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COLONCOLON", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 149.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(39.0, 36.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 150.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(40.0, 39.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 151.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(41.0, 40.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 151.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(42.0, 39.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 152.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(43.0, 42.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "NEW", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 152.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(44.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 155.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(45.0, 44.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentPrimaryLooksLikeTypeClassLiteral", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 155.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(46.0, 45.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeTypeOrVoid", null, 18.0, 0.0, false, false, 156.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(47.0, 45.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DOT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 157.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(48.0, 45.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "CLASS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 158.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(49.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 160.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(50.0, 49.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentPrimaryLooksLikeIdentifierPrimary", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 160.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(51.0, 50.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 161.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(52.0, 50.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 162.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(53.0, 52.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 162.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(54.0, 53.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "arguments", null, 121.0, 0.0, false, false, 162.0, 30.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(55.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 164.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(56.0, 55.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "nonWildcardTypeArguments", null, 145.0, 0.0, false, false, 165.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(57.0, 55.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 166.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(58.0, 57.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 167.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(59.0, 58.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "explicitGenericInvocationSuffix", null, 134.0, 0.0, false, false, 167.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(60.0, 57.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 168.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(61.0, 60.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "THIS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 169.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(62.0, 60.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "arguments", null, 121.0, 0.0, false, false, 170.0, 25.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(8.0, 5.0), __QinJavaUtilHashMap.entry(13.0, 6.0), __QinJavaUtilHashMap.entry(18.0, 1.0), __QinJavaUtilHashMap.entry(21.0, 2.0), __QinJavaUtilHashMap.entry(36.0, 4.0), __QinJavaUtilHashMap.entry(45.0, 0.0), __QinJavaUtilHashMap.entry(50.0, 3.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(8.0, 13.0, 18.0, 21.0, 36.0, 45.0, 50.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(41.0, 0.0, "relationalExpression", "relationalExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "shiftExpression", null, 43.0, 0.0, false, false, 652.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 653.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 653.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 653.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 654.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentRelationalExpressionHasComparisonOperator", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 654.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 655.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 656.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 656.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 657.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "GT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 657.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 658.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 658.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 659.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "GE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 659.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "shiftExpression", null, 43.0, 0.0, false, false, 661.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 663.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 16.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentRelationalExpressionHasInstanceof", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 663.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 17.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "INSTANCEOF", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 664.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 17.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 665.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(20.0, 19.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 666.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(21.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentInstanceofLooksLikePattern", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 666.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(22.0, 21.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "pattern", null, 148.0, 0.0, false, false, 666.0, 73.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(23.0, 19.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 667.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(24.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentInstanceofLooksLikeTypeOnly", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 667.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(25.0, 24.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 667.0, 74.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(5.0, 0.0), __QinJavaUtilHashMap.entry(17.0, 3.0), __QinJavaUtilHashMap.entry(21.0, 1.0), __QinJavaUtilHashMap.entry(24.0, 2.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(5.0, 17.0, 21.0, 24.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(42.0, 0.0, "selector", "selector@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 925.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 926.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DOT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 927.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 928.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 929.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 930.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 931.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 931.0, 32.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentLooksLikeArguments", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 931.0, 32.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "arguments", null, 121.0, 0.0, false, false, 931.0, 76.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 933.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "explicitGenericInvocation", null, 133.0, 0.0, false, false, 933.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 934.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "THIS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 934.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 935.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "NEW", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 936.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 937.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 16.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 937.0, 32.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 17.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "nonWildcardTypeArguments", null, 145.0, 0.0, false, false, 937.0, 38.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "innerCreator", null, 141.0, 0.0, false, false, 938.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(20.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 940.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(21.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SUPER", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 941.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(22.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "superSuffix", null, 155.0, 0.0, false, false, 942.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(23.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 946.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(24.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 947.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(25.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 948.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(26.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 949.0, 17.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(8.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(8.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(43.0, 0.0, "shiftExpression", "shiftExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "additiveExpression", null, 19.0, 0.0, false, false, 702.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 703.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 703.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentShiftExpressionHasShiftOperator", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 703.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "shiftOperator", null, 44.0, 0.0, false, false, 705.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "additiveExpression", null, 19.0, 0.0, false, false, 706.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(3.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(3.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(44.0, 0.0, "shiftOperator", "shiftOperator@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 720.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 721.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentShiftOperatorLooksLikeLeftShift", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 721.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 722.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 723.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 725.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentShiftOperatorLooksLikeRightShift", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 725.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "GT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 726.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "GT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 727.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 728.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 728.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "GT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 728.0, 30.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 1.0), __QinJavaUtilHashMap.entry(6.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 6.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(45.0, 0.0, "switchExpression", "switchExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SWITCH", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1033.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "parExpression", null, 67.0, 0.0, false, false, 1034.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1035.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_AT_LEAST_ONE, "AtLeastOne", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1036.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1036.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "switchRule", null, 74.0, 0.0, false, false, 1036.0, 26.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1037.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(46.0, 0.0, "unaryExpression", "unaryExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 785.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 786.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentUnaryExpressionLooksLikePrefix", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 786.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "prefixOp", null, 39.0, 0.0, false, false, 787.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "unaryExpression", null, 46.0, 0.0, false, false, 788.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 790.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentUnaryExpressionLooksLikeCast", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 790.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 791.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 792.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 793.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "unaryExpression", null, 46.0, 0.0, false, false, 794.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 796.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.expression.JavaExpressionParser#currentUnaryExpressionLooksLikePostfix", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 796.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "postfixExpression", null, 37.0, 0.0, false, false, 796.0, 70.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 1.0), __QinJavaUtilHashMap.entry(6.0, 2.0), __QinJavaUtilHashMap.entry(12.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 6.0, 12.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(47.0, 0.0, "block", "block@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 38.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 39.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 39.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "blockStatement", null, 48.0, 0.0, false, false, 39.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 40.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    return null;
  }
  static subhutiStaticRuleVariants_2(variants: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant>): void {
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(48.0, 0.0, "blockStatement", "blockStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 52.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 53.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentBlockStatementLooksLikeLocalVariableDeclaration", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 53.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "localVariableDeclaration", null, 66.0, 0.0, false, false, 54.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 55.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 57.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentBlockStatementLooksLikeLocalTypeDeclaration", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 57.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "localTypeDeclaration", null, 65.0, 0.0, false, false, 57.0, 82.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 58.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentBlockStatementLooksLikeStatement", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 58.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "statement", null, 72.0, 0.0, false, false, 58.0, 71.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 0.0), __QinJavaUtilHashMap.entry(6.0, 2.0), __QinJavaUtilHashMap.entry(9.0, 1.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 6.0, 9.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(49.0, 0.0, "breakStatement", "breakStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "BREAK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 747.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 748.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 748.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 748.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 749.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(50.0, 0.0, "catchClause", "catchClause@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "CATCH", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 949.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 950.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 951.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 951.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableModifier", null, 113.0, 0.0, false, false, 951.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "catchType", null, 51.0, 0.0, false, false, 952.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 953.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 954.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "block", null, 47.0, 0.0, false, false, 955.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(51.0, 0.0, "catchType", "catchType@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 965.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 966.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 966.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "BITOR", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 967.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 968.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(52.0, 0.0, "continueStatement", "continueStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "CONTINUE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 757.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 758.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 758.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 758.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 759.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(53.0, 0.0, "doWhileStatement", "doWhileStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DO", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 481.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "statement", null, 72.0, 0.0, false, false, 482.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "WHILE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 483.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "parExpression", null, 67.0, 0.0, false, false, 484.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 485.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(54.0, 0.0, "enhancedForControl", "enhancedForControl@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 886.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 886.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableModifier", null, 113.0, 0.0, false, false, 886.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 887.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 888.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 888.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 889.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "VAR", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 889.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableDeclaratorId", null, 111.0, 0.0, false, false, 891.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COLON", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 892.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 893.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(55.0, 0.0, "explicitConstructorInvocationStatement", "explicitConstructorInvocationStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 431.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 431.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "nonWildcardTypeArguments", null, 145.0, 0.0, false, false, 431.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 432.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 433.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "THIS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 433.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 434.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SUPER", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 434.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "arguments", null, 121.0, 0.0, false, false, 436.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 437.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(56.0, 0.0, "expressionList", "expressionList@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 935.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 936.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 936.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 937.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 938.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(57.0, 0.0, "expressionStatement", "expressionStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "statementExpression", null, 73.0, 0.0, false, false, 777.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 778.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(58.0, 0.0, "finallyBlock", "finallyBlock@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "FINALLY", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 979.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "block", null, 47.0, 0.0, false, false, 980.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(59.0, 0.0, "forControl", "forControl@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 811.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 812.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentForControlLooksLikeEnhancedFor", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 812.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "enhancedForControl", null, 54.0, 0.0, false, false, 812.0, 69.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 813.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentForControlLooksLikeBasicFor", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 813.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 814.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 814.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "forInit", null, 60.0, 0.0, false, false, 814.0, 30.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 815.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 816.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 816.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 816.0, 30.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 817.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 818.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 818.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 15.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "forUpdate", null, 62.0, 0.0, false, false, 818.0, 30.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 1.0), __QinJavaUtilHashMap.entry(5.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 5.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(60.0, 0.0, "forInit", "forInit@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 904.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 905.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentForInitLooksLikeLocalVariableDeclaration", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 905.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "localVariableDeclaration", null, 66.0, 0.0, false, false, 905.0, 79.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 906.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentForInitLooksLikeExpressionList", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 906.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expressionList", null, 56.0, 0.0, false, false, 906.0, 69.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 1.0), __QinJavaUtilHashMap.entry(5.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 5.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(61.0, 0.0, "forStatement", "forStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "FOR", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 459.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 460.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "forControl", null, 59.0, 0.0, false, false, 461.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 462.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "statement", null, 72.0, 0.0, false, false, 463.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(62.0, 0.0, "forUpdate", "forUpdate@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expressionList", null, 56.0, 0.0, false, false, 925.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(63.0, 0.0, "ifStatement", "ifStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "IF", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 445.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "parExpression", null, 67.0, 0.0, false, false, 446.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "statement", null, 72.0, 0.0, false, false, 447.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 448.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 448.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ELSE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 449.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "statement", null, 72.0, 0.0, false, false, 450.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(64.0, 0.0, "labeledStatement", "labeledStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 767.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COLON", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 768.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "statement", null, 72.0, 0.0, false, false, 769.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(65.0, 0.0, "localTypeDeclaration", "localTypeDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 309.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 310.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 311.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 311.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classOrInterfaceModifier", null, 89.0, 0.0, false, false, 311.0, 28.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 312.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 313.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classDeclaration", null, 88.0, 0.0, false, false, 313.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 314.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "interfaceDeclaration", null, 101.0, 0.0, false, false, 314.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 315.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "recordDeclaration", null, 109.0, 0.0, false, false, 315.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 318.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 318.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(66.0, 0.0, "localVariableDeclaration", "localVariableDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 286.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 286.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableModifier", null, 113.0, 0.0, false, false, 286.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 287.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 288.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "VAR", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 289.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 290.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 291.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 292.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 294.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 295.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableDeclarators", null, 112.0, 0.0, false, false, 296.0, 17.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(67.0, 0.0, "parExpression", "parExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 798.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 799.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 800.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(68.0, 0.0, "resource", "resource@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1018.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1019.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentResourceLooksLikeDeclaration", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1019.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1020.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1020.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableModifier", null, 113.0, 0.0, false, false, 1020.0, 28.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1021.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1022.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classOrInterfaceType", null, 9.0, 0.0, false, false, 1022.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1023.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "VAR", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1023.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableDeclaratorId", null, 111.0, 0.0, false, false, 1025.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1026.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 1027.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1029.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentResourceLooksLikeIdentifier", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1029.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 15.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 1029.0, 66.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 1.0), __QinJavaUtilHashMap.entry(15.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 15.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(69.0, 0.0, "resourceSpecification", "resourceSpecification@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 990.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "resources", null, 70.0, 0.0, false, false, 991.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 992.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 992.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 992.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 993.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(70.0, 0.0, "resources", "resources@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "resource", null, 68.0, 0.0, false, false, 1003.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1004.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1004.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1005.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "resource", null, 68.0, 0.0, false, false, 1006.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(71.0, 0.0, "returnStatement", "returnStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RETURN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 717.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 718.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 718.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 718.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 719.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    return null;
  }
  static subhutiStaticRuleVariants_3(variants: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant>): void {
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(72.0, 0.0, "statement", "statement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 345.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 346.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "block", null, 47.0, 0.0, false, false, 346.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 347.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "ifStatement", null, 63.0, 0.0, false, false, 347.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 348.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "forStatement", null, 61.0, 0.0, false, false, 348.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 349.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "whileStatement", null, 83.0, 0.0, false, false, 349.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 350.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "doWhileStatement", null, 53.0, 0.0, false, false, 350.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 351.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "tryStatement", null, 82.0, 0.0, false, false, 351.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 352.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentStatementLooksLikeSwitchStatement", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 352.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "switchStatement", null, 78.0, 0.0, false, false, 352.0, 72.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 353.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 16.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "synchronizedStatement", null, 79.0, 0.0, false, false, 353.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 354.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 18.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "returnStatement", null, 71.0, 0.0, false, false, 354.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(20.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 355.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(21.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "throwStatement", null, 80.0, 0.0, false, false, 355.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(22.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 356.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(23.0, 22.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "yieldStatement", null, 84.0, 0.0, false, false, 356.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(24.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 357.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(25.0, 24.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "breakStatement", null, 49.0, 0.0, false, false, 357.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(26.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 358.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(27.0, 26.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "continueStatement", null, 52.0, 0.0, false, false, 358.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(28.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 359.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(29.0, 28.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 359.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(30.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 360.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(31.0, 30.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentStatementLooksLikeLabeledStatement", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 360.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(32.0, 31.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "labeledStatement", null, 64.0, 0.0, false, false, 360.0, 73.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(33.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 361.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(34.0, 33.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentStatementLooksLikeExplicitConstructorInvocation", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 361.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(35.0, 34.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "explicitConstructorInvocationStatement", null, 55.0, 0.0, false, false, 361.0, 86.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(36.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 362.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(37.0, 36.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentStatementLooksLikeExpressionStatement", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 362.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(38.0, 37.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expressionStatement", null, 57.0, 0.0, false, false, 362.0, 76.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(14.0, 2.0), __QinJavaUtilHashMap.entry(31.0, 3.0), __QinJavaUtilHashMap.entry(34.0, 0.0), __QinJavaUtilHashMap.entry(37.0, 1.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(14.0, 31.0, 34.0, 37.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(73.0, 0.0, "statementExpression", "statementExpression@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 788.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(74.0, 0.0, "switchRule", "switchRule@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 645.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 646.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "CASE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 647.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "switchRuleLabelList", null, 76.0, 0.0, false, false, 648.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ARROW", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 649.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "switchRuleOutcome", null, 77.0, 0.0, false, false, 650.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 652.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DEFAULT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 653.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ARROW", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 654.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "switchRuleOutcome", null, 77.0, 0.0, false, false, 655.0, 17.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(75.0, 0.0, "switchRuleLabel", "switchRuleLabel@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 671.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 672.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentSwitchRuleLabelLooksLikePattern", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 672.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "pattern", null, 148.0, 0.0, false, false, 672.0, 70.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 673.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentSwitchRuleLabelLooksLikeExpression", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 673.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "assignmentExpression", null, 23.0, 0.0, false, false, 673.0, 73.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 1.0), __QinJavaUtilHashMap.entry(5.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 5.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(76.0, 0.0, "switchRuleLabelList", "switchRuleLabelList@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "switchRuleLabel", null, 75.0, 0.0, false, false, 662.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 663.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 663.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 664.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "switchRuleLabel", null, 75.0, 0.0, false, false, 665.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(77.0, 0.0, "switchRuleOutcome", "switchRuleOutcome@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 695.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 696.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "block", null, 47.0, 0.0, false, false, 696.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 697.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "throwStatement", null, 80.0, 0.0, false, false, 697.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 698.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expressionStatement", null, 57.0, 0.0, false, false, 698.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(78.0, 0.0, "switchStatement", "switchStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SWITCH", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 532.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "parExpression", null, 67.0, 0.0, false, false, 533.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 534.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 535.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 535.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 535.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 536.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentSwitchEntryLooksLikeBlockStatementGroup", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 536.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "switchBlockStatementGroup", null, 156.0, 0.0, false, false, 536.0, 78.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 537.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentSwitchEntryLooksLikeRule", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 537.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "switchRule", null, 74.0, 0.0, false, false, 537.0, 63.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 539.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 539.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentSwitchEntryLooksLikeTrailingLabel", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 539.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "switchLabel", null, 157.0, 0.0, false, false, 539.0, 73.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 540.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(7.0, 0.0), __QinJavaUtilHashMap.entry(10.0, 2.0), __QinJavaUtilHashMap.entry(14.0, 1.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(7.0, 10.0, 14.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(79.0, 0.0, "synchronizedStatement", "synchronizedStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SYNCHRONIZED", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 707.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "parExpression", null, 67.0, 0.0, false, false, 708.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "block", null, 47.0, 0.0, false, false, 709.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(80.0, 0.0, "throwStatement", "throwStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "THROW", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 727.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 728.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 729.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(81.0, 0.0, "tryHandlerSuffix", "tryHandlerSuffix@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 518.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 519.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_AT_LEAST_ONE, "AtLeastOne", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 520.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 520.0, 28.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "catchClause", null, 50.0, 0.0, false, false, 520.0, 34.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 521.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 521.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "finallyBlock", null, 58.0, 0.0, false, false, 521.0, 30.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 523.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "finallyBlock", null, 58.0, 0.0, false, false, 523.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(82.0, 0.0, "tryStatement", "tryStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "TRY", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 493.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 494.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 495.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentTryStatementLooksLikeResourceTry", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 495.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "resourceSpecification", null, 69.0, 0.0, false, false, 496.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "block", null, 47.0, 0.0, false, false, 497.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 498.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 498.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "catchClause", null, 50.0, 0.0, false, false, 498.0, 28.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 499.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 499.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "finallyBlock", null, 58.0, 0.0, false, false, 499.0, 30.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 501.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.statement.JavaStatementParser#currentTryStatementLooksLikePlainTry", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 501.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "block", null, 47.0, 0.0, false, false, 502.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "tryHandlerSuffix", null, 81.0, 0.0, false, false, 503.0, 17.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(3.0, 1.0), __QinJavaUtilHashMap.entry(13.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(3.0, 13.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(83.0, 0.0, "whileStatement", "whileStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "WHILE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 471.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "parExpression", null, 67.0, 0.0, false, false, 472.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "statement", null, 72.0, 0.0, false, false, 473.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(84.0, 0.0, "yieldStatement", "yieldStatement@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "YIELD", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 737.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 738.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 739.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(85.0, 0.0, "annotationTypeDeclaration", "annotationTypeDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "AT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 136.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "INTERFACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 137.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeIdentifier", null, 3.0, 0.0, false, false, 138.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotationTypeBody", null, 118.0, 0.0, false, false, 139.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(86.0, 0.0, "classBody", "classBody@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 149.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 150.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 150.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classBodyDeclaration", null, 87.0, 0.0, false, false, 150.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 151.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(87.0, 0.0, "classBodyDeclaration", "classBodyDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 163.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 164.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#currentClassBodyDeclarationLooksLikeEmpty", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 164.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 164.0, 73.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 165.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#currentClassBodyDeclarationLooksLikeInitializerBlock", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 165.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 166.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 166.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "STATIC", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 166.0, 30.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "block", null, 47.0, 0.0, false, false, 167.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 169.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#currentClassBodyDeclarationLooksLikeMember", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 169.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 170.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 170.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "modifier", null, 108.0, 0.0, false, false, 170.0, 28.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "memberDeclaration", null, 105.0, 0.0, false, false, 171.0, 17.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 0.0), __QinJavaUtilHashMap.entry(5.0, 1.0), __QinJavaUtilHashMap.entry(11.0, 2.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 5.0, 11.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(88.0, 0.0, "classDeclaration", "classDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "CLASS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 43.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeIdentifier", null, 3.0, 0.0, false, false, 44.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 45.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 45.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeParameters", null, 16.0, 0.0, false, false, 45.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 46.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 46.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "EXTENDS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 47.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 48.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 50.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 50.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "IMPLEMENTS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 51.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeList", null, 14.0, 0.0, false, false, 52.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 54.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 54.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "PERMITS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 55.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeList", null, 14.0, 0.0, false, false, 56.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classBody", null, 86.0, 0.0, false, false, 58.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(89.0, 0.0, "classOrInterfaceModifier", "classOrInterfaceModifier@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 753.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 754.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#currentClassOrInterfaceModifierLooksLikeAnnotation", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 754.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotation", null, 114.0, 0.0, false, false, 754.0, 82.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 755.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "PUBLIC", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 755.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 756.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "PROTECTED", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 756.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 757.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "PRIVATE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 757.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 758.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "STATIC", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 758.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 759.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ABSTRACT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 759.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 760.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "FINAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 760.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 761.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 16.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "STRICTFP", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 761.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 762.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 18.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEALED", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 762.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(20.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 763.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(21.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "NON_SEALED", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 763.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(90.0, 0.0, "constDeclaration", "constDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 598.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "constantDeclarator", null, 91.0, 0.0, false, false, 599.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 600.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 600.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 601.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "constantDeclarator", null, 91.0, 0.0, false, false, 602.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 604.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(91.0, 0.0, "constantDeclarator", "constantDeclarator@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 614.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 615.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 615.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 616.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 617.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 619.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableInitializer", null, 8.0, 0.0, false, false, 620.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(92.0, 0.0, "constructorDeclaration", "constructorDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 262.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "formalParameters", null, 137.0, 0.0, false, false, 263.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 264.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 264.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "THROWS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 265.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedNameList", null, 2.0, 0.0, false, false, 266.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "block", null, 47.0, 0.0, false, false, 268.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(93.0, 0.0, "enumDeclaration", "enumDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ENUM", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 94.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeIdentifier", null, 3.0, 0.0, false, false, 95.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 96.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 96.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "IMPLEMENTS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 97.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeList", null, 14.0, 0.0, false, false, 98.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 100.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 101.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 101.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "enumConstants", null, 132.0, 0.0, false, false, 101.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 102.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 102.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 102.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 103.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 103.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "enumBodyDeclarations", null, 130.0, 0.0, false, false, 103.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 104.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(94.0, 0.0, "fieldDeclaration", "fieldDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 289.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableDeclarators", null, 112.0, 0.0, false, false, 290.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 291.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(95.0, 0.0, "genericConstructorDeclaration", "genericConstructorDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeParameters", null, 16.0, 0.0, false, false, 278.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "constructorDeclaration", null, 92.0, 0.0, false, false, 279.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    return null;
  }
  static subhutiStaticRuleVariants_4(variants: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant>): void {
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(96.0, 0.0, "genericInterfaceMethodDeclaration", "genericInterfaceMethodDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeParameters", null, 16.0, 0.0, false, false, 640.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "interfaceCommonBodyDeclaration", null, 100.0, 0.0, false, false, 641.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(97.0, 0.0, "genericMethodDeclaration", "genericMethodDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeParameters", null, 16.0, 0.0, false, false, 251.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "methodDeclaration", null, 107.0, 0.0, false, false, 252.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(98.0, 0.0, "interfaceBody", "interfaceBody@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 463.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 464.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 464.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "interfaceBodyDeclaration", null, 99.0, 0.0, false, false, 464.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 465.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(99.0, 0.0, "interfaceBodyDeclaration", "interfaceBodyDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 476.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 477.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 478.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 478.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "interfaceModifier", null, 104.0, 0.0, false, false, 478.0, 28.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "interfaceMemberDeclaration", null, 102.0, 0.0, false, false, 479.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 481.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 481.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(100.0, 0.0, "interfaceCommonBodyDeclaration", "interfaceCommonBodyDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 653.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 653.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotation", null, 114.0, 0.0, false, false, 653.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeTypeOrVoid", null, 18.0, 0.0, false, false, 654.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 655.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "formalParameters", null, 137.0, 0.0, false, false, 656.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 657.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 657.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 658.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 659.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 661.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 661.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "THROWS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 662.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedNameList", null, 2.0, 0.0, false, false, 663.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "methodBody", null, 106.0, 0.0, false, false, 665.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(101.0, 0.0, "interfaceDeclaration", "interfaceDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "INTERFACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 72.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeIdentifier", null, 3.0, 0.0, false, false, 73.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 74.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 74.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeParameters", null, 16.0, 0.0, false, false, 74.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 75.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 75.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "EXTENDS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 76.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeList", null, 14.0, 0.0, false, false, 77.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 79.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 79.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "PERMITS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 80.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeList", null, 14.0, 0.0, false, false, 81.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "interfaceBody", null, 98.0, 0.0, false, false, 83.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(102.0, 0.0, "interfaceMemberDeclaration", "interfaceMemberDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 513.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 514.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#interfaceMemberDeclarationLooksLikeRecord", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 514.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "recordDeclaration", null, 109.0, 0.0, false, false, 514.0, 73.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 515.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#interfaceMemberDeclarationLooksLikeConst", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 515.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "constDeclaration", null, 90.0, 0.0, false, false, 515.0, 72.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 516.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#interfaceMemberDeclarationLooksLikeGenericMethod", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 516.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "genericInterfaceMethodDeclaration", null, 96.0, 0.0, false, false, 516.0, 80.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 517.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#interfaceMemberDeclarationLooksLikeMethod", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 517.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "interfaceMethodDeclaration", null, 103.0, 0.0, false, false, 517.0, 73.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 518.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#interfaceMemberDeclarationLooksLikeInterface", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 518.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "interfaceDeclaration", null, 101.0, 0.0, false, false, 518.0, 76.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 519.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 16.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#interfaceMemberDeclarationLooksLikeAnnotationType", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 519.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 17.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotationTypeDeclaration", null, 85.0, 0.0, false, false, 519.0, 81.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 520.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(20.0, 19.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#interfaceMemberDeclarationLooksLikeClass", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 520.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(21.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classDeclaration", null, 88.0, 0.0, false, false, 520.0, 72.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(22.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 521.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(23.0, 22.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#interfaceMemberDeclarationLooksLikeEnum", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 521.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(24.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "enumDeclaration", null, 93.0, 0.0, false, false, 521.0, 71.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 7.0), __QinJavaUtilHashMap.entry(5.0, 1.0), __QinJavaUtilHashMap.entry(8.0, 3.0), __QinJavaUtilHashMap.entry(11.0, 4.0), __QinJavaUtilHashMap.entry(14.0, 5.0), __QinJavaUtilHashMap.entry(17.0, 6.0), __QinJavaUtilHashMap.entry(20.0, 0.0), __QinJavaUtilHashMap.entry(23.0, 2.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 5.0, 8.0, 11.0, 14.0, 17.0, 20.0, 23.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(103.0, 0.0, "interfaceMethodDeclaration", "interfaceMethodDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "interfaceCommonBodyDeclaration", null, 100.0, 0.0, false, false, 630.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(104.0, 0.0, "interfaceModifier", "interfaceModifier@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 493.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 494.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "modifier", null, 108.0, 0.0, false, false, 494.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 495.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DEFAULT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 495.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(105.0, 0.0, "memberDeclaration", "memberDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 207.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 208.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#memberDeclarationLooksLikeRecord", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 208.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "recordDeclaration", null, 109.0, 0.0, false, false, 208.0, 64.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 209.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#memberDeclarationLooksLikeGenericConstructor", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 209.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "genericConstructorDeclaration", null, 95.0, 0.0, false, false, 209.0, 76.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 210.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#memberDeclarationLooksLikeConstructor", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 210.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "constructorDeclaration", null, 92.0, 0.0, false, false, 210.0, 69.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 211.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#memberDeclarationLooksLikeGenericMethod", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 211.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "genericMethodDeclaration", null, 97.0, 0.0, false, false, 211.0, 71.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 212.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#memberDeclarationLooksLikeMethod", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 212.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "methodDeclaration", null, 107.0, 0.0, false, false, 212.0, 64.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 213.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 16.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#memberDeclarationLooksLikeField", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 213.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 17.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "fieldDeclaration", null, 94.0, 0.0, false, false, 213.0, 63.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 214.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(20.0, 19.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#memberDeclarationLooksLikeInterface", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 214.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(21.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "interfaceDeclaration", null, 101.0, 0.0, false, false, 214.0, 67.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(22.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 215.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(23.0, 22.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#memberDeclarationLooksLikeAnnotationType", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 215.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(24.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotationTypeDeclaration", null, 85.0, 0.0, false, false, 215.0, 72.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(25.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 216.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(26.0, 25.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#memberDeclarationLooksLikeClass", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 216.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(27.0, 26.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classDeclaration", null, 88.0, 0.0, false, false, 216.0, 63.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(28.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 217.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(29.0, 28.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.clazz.JavaClassParser#memberDeclarationLooksLikeEnum", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 217.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(30.0, 29.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "enumDeclaration", null, 93.0, 0.0, false, false, 217.0, 62.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 8.0), __QinJavaUtilHashMap.entry(5.0, 0.0), __QinJavaUtilHashMap.entry(8.0, 2.0), __QinJavaUtilHashMap.entry(11.0, 4.0), __QinJavaUtilHashMap.entry(14.0, 5.0), __QinJavaUtilHashMap.entry(17.0, 6.0), __QinJavaUtilHashMap.entry(20.0, 7.0), __QinJavaUtilHashMap.entry(23.0, 9.0), __QinJavaUtilHashMap.entry(26.0, 1.0), __QinJavaUtilHashMap.entry(29.0, 3.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 5.0, 8.0, 11.0, 14.0, 17.0, 20.0, 23.0, 26.0, 29.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(106.0, 0.0, "methodBody", "methodBody@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 778.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 779.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "block", null, 47.0, 0.0, false, false, 779.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 780.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 780.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(107.0, 0.0, "methodDeclaration", "methodDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeTypeOrVoid", null, 18.0, 0.0, false, false, 230.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 231.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "formalParameters", null, 137.0, 0.0, false, false, 232.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 233.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 233.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 234.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 235.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 237.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 237.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "THROWS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 238.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedNameList", null, 2.0, 0.0, false, false, 239.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "methodBody", null, 106.0, 0.0, false, false, 241.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(108.0, 0.0, "modifier", "modifier@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 734.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 735.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classOrInterfaceModifier", null, 89.0, 0.0, false, false, 735.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 736.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "NATIVE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 736.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 737.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SYNCHRONIZED", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 737.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 738.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "TRANSIENT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 738.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 739.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "VOLATILE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 739.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(109.0, 0.0, "recordDeclaration", "recordDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RECORD", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 118.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeIdentifier", null, 3.0, 0.0, false, false, 119.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 120.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 120.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeParameters", null, 16.0, 0.0, false, false, 120.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "recordHeader", null, 153.0, 0.0, false, false, 121.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 122.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 122.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "IMPLEMENTS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 123.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeList", null, 14.0, 0.0, false, false, 124.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "recordBody", null, 150.0, 0.0, false, false, 126.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(110.0, 0.0, "variableDeclarator", "variableDeclarator@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableDeclaratorId", null, 111.0, 0.0, false, false, 690.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 691.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 691.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 692.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableInitializer", null, 8.0, 0.0, false, false, 693.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(111.0, 0.0, "variableDeclaratorId", "variableDeclaratorId@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 705.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 706.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 706.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 707.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACK", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 708.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(112.0, 0.0, "variableDeclarators", "variableDeclarators@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableDeclarator", null, 110.0, 0.0, false, false, 676.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 677.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 677.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 678.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableDeclarator", null, 110.0, 0.0, false, false, 679.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(113.0, 0.0, "variableModifier", "variableModifier@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 720.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 721.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "FINAL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 721.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 722.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotation", null, 114.0, 0.0, false, false, 722.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(114.0, 0.0, "annotation", "annotation@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "AT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 390.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 391.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 392.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 392.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 393.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 394.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 394.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 394.0, 26.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 395.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentAnnotationValueLooksLikePairs", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 395.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "elementValuePairs", null, 129.0, 0.0, false, false, 395.0, 72.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 396.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentAnnotationValueLooksLikeSingleValue", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 396.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "elementValue", null, 126.0, 0.0, false, false, 396.0, 78.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 398.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(9.0, 1.0), __QinJavaUtilHashMap.entry(12.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(9.0, 12.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(115.0, 0.0, "annotationConstantRest", "annotationConstantRest@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableDeclarators", null, 112.0, 0.0, false, false, 618.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(116.0, 0.0, "annotationMethodOrConstantRest", "annotationMethodOrConstantRest@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 584.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 585.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentAnnotationMethodOrConstantRestLooksLikeMethod", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 585.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotationMethodRest", null, 117.0, 0.0, false, false, 585.0, 84.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 586.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentAnnotationMethodOrConstantRestLooksLikeConstant", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 586.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotationConstantRest", null, 115.0, 0.0, false, false, 586.0, 86.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 1.0), __QinJavaUtilHashMap.entry(5.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 5.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(117.0, 0.0, "annotationMethodRest", "annotationMethodRest@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 605.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 606.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 607.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 608.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 608.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "defaultValue", null, 125.0, 0.0, false, false, 608.0, 22.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(118.0, 0.0, "annotationTypeBody", "annotationTypeBody@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 513.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 514.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 514.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotationTypeElementDeclaration", null, 119.0, 0.0, false, false, 514.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 515.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(119.0, 0.0, "annotationTypeElementDeclaration", "annotationTypeElementDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 526.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 527.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 528.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 528.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "modifier", null, 108.0, 0.0, false, false, 528.0, 28.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotationTypeElementRest", null, 120.0, 0.0, false, false, 529.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 531.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 531.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    return null;
  }
  static subhutiStaticRuleVariants_5(variants: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant>): void {
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(120.0, 0.0, "annotationTypeElementRest", "annotationTypeElementRest@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 547.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 548.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 549.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotationMethodOrConstantRest", null, 116.0, 0.0, false, false, 550.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 551.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 553.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classDeclaration", null, 88.0, 0.0, false, false, 554.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 555.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 555.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 555.0, 30.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 557.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "interfaceDeclaration", null, 101.0, 0.0, false, false, 558.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 559.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 559.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 559.0, 30.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 561.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 15.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "enumDeclaration", null, 93.0, 0.0, false, false, 562.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 15.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 563.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 17.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 563.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 18.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 563.0, 30.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(20.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 565.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(21.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotationTypeDeclaration", null, 85.0, 0.0, false, false, 566.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(22.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 567.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(23.0, 22.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 567.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(24.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 567.0, 30.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(25.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 569.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(26.0, 25.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "recordDeclaration", null, 109.0, 0.0, false, false, 570.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(27.0, 25.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 571.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(28.0, 27.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 571.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(29.0, 28.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 571.0, 30.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(121.0, 0.0, "arguments", "arguments@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 845.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 846.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 846.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expressionList", null, 56.0, 0.0, false, false, 846.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 847.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(122.0, 0.0, "classCreatorRest", "classCreatorRest@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "arguments", null, 121.0, 0.0, false, false, 933.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 934.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 934.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classBody", null, 86.0, 0.0, false, false, 934.0, 22.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(123.0, 0.0, "compactConstructorDeclaration", "compactConstructorDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 771.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 771.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "modifier", null, 108.0, 0.0, false, false, 771.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 772.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "block", null, 47.0, 0.0, false, false, 773.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(124.0, 0.0, "compilationUnit", "compilationUnit@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 47.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 49.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "moduleDeclaration", null, 143.0, 0.0, false, false, 49.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 51.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 52.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 52.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "packageDeclaration", null, 147.0, 0.0, false, false, 52.0, 30.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 53.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 53.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 53.0, 28.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 54.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "importDeclaration", null, 138.0, 0.0, false, false, 54.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 55.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 55.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 57.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 57.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 15.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 57.0, 28.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 16.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 58.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 17.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeDeclaration", null, 158.0, 0.0, false, false, 58.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 16.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 59.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(20.0, 19.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 59.0, 27.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(125.0, 0.0, "defaultValue", "defaultValue@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DEFAULT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 628.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "elementValue", null, 126.0, 0.0, false, false, 629.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(126.0, 0.0, "elementValue", "elementValue@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 466.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 467.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentElementValueLooksLikeAnnotation", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 467.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotation", null, 114.0, 0.0, false, false, 467.0, 70.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 468.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentElementValueLooksLikeArrayInitializer", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 468.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "elementValueArrayInitializer", null, 127.0, 0.0, false, false, 468.0, 76.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 469.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentElementValueLooksLikeExpression", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 469.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 469.0, 70.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 0.0), __QinJavaUtilHashMap.entry(5.0, 2.0), __QinJavaUtilHashMap.entry(8.0, 1.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 5.0, 8.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(127.0, 0.0, "elementValueArrayInitializer", "elementValueArrayInitializer@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 493.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 494.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 494.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "elementValue", null, 126.0, 0.0, false, false, 495.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 496.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 496.0, 18.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 497.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "elementValue", null, 126.0, 0.0, false, false, 498.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 501.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 501.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 501.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 502.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(128.0, 0.0, "elementValuePair", "elementValuePair@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 452.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ASSIGN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 453.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "elementValue", null, 126.0, 0.0, false, false, 454.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(129.0, 0.0, "elementValuePairs", "elementValuePairs@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "elementValuePair", null, 128.0, 0.0, false, false, 438.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 439.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 439.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 440.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "elementValuePair", null, 128.0, 0.0, false, false, 441.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(130.0, 0.0, "enumBodyDeclarations", "enumBodyDeclarations@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 672.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 673.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 673.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classBodyDeclaration", null, 87.0, 0.0, false, false, 673.0, 20.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(131.0, 0.0, "enumConstant", "enumConstant@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 658.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 658.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotation", null, 114.0, 0.0, false, false, 658.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 659.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 660.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 660.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "arguments", null, 121.0, 0.0, false, false, 660.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 661.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 661.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classBody", null, 86.0, 0.0, false, false, 661.0, 22.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(132.0, 0.0, "enumConstants", "enumConstants@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "enumConstant", null, 131.0, 0.0, false, false, 644.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 645.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 645.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 646.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "enumConstant", null, 131.0, 0.0, false, false, 647.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(133.0, 0.0, "explicitGenericInvocation", "explicitGenericInvocation@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "nonWildcardTypeArguments", null, 145.0, 0.0, false, false, 892.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "explicitGenericInvocationSuffix", null, 134.0, 0.0, false, false, 893.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(134.0, 0.0, "explicitGenericInvocationSuffix", "explicitGenericInvocationSuffix@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 872.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 873.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SUPER", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 874.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "superSuffix", null, 155.0, 0.0, false, false, 875.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 877.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 878.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "arguments", null, 121.0, 0.0, false, false, 879.0, 17.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(135.0, 0.0, "formalParameter", "formalParameter@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 359.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 359.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableModifier", null, 113.0, 0.0, false, false, 359.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 360.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableDeclaratorId", null, 111.0, 0.0, false, false, 361.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(136.0, 0.0, "formalParameterList", "formalParameterList@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 213.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 214.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentParameterIsNotVarargs", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 214.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "formalParameter", null, 135.0, 0.0, false, false, 215.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 216.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 216.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#commaFollowedByNonVarargsParameter", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 216.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 217.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "formalParameter", null, 135.0, 0.0, false, false, 218.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 220.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 220.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#commaFollowedByVarargsParameter", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 220.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 221.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "lastFormalParameter", null, 142.0, 0.0, false, false, 222.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 225.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 14.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentParameterIsVarargs", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 225.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 15.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "lastFormalParameter", null, 142.0, 0.0, false, false, 225.0, 57.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 0.0), __QinJavaUtilHashMap.entry(6.0, 2.0), __QinJavaUtilHashMap.entry(11.0, 1.0), __QinJavaUtilHashMap.entry(15.0, 3.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0, 6.0, 11.0, 15.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(137.0, 0.0, "formalParameters", "formalParameters@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 157.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 158.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 158.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 158.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 159.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentFormalParametersStartFormalList", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 159.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "formalParameterList", null, 136.0, 0.0, false, false, 159.0, 70.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 160.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentFormalParametersStartReceiver", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 160.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "receiverParameter", null, 149.0, 0.0, false, false, 161.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 162.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 162.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 163.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "formalParameterList", null, 136.0, 0.0, false, false, 164.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 168.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(5.0, 1.0), __QinJavaUtilHashMap.entry(8.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(5.0, 8.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(138.0, 0.0, "importDeclaration", "importDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "IMPORT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 85.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 86.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 86.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "STATIC", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 86.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "importQualifiedName", null, 139.0, 0.0, false, false, 87.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 88.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 88.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DOT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 89.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "MUL", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 90.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 92.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(139.0, 0.0, "importQualifiedName", "importQualifiedName@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 102.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 103.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 103.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentImportQualifiedNameContinuesWithIdentifierSegment", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 103.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DOT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 104.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 105.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(3.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(3.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(140.0, 0.0, "importQualifiedNameSegment", "importQualifiedNameSegment@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "if", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 115.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "then", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 115.0, 51.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "(!match(\"DOT\") || lookahead(\"MUL\", 2))", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 115.0, 12.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ACTION, "setParseFail", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 116.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ACTION, "return", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 117.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DOT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 119.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 120.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(141.0, 0.0, "innerCreator", "innerCreator@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 904.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 905.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 905.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "nonWildcardTypeArgumentsOrDiamond", null, 146.0, 0.0, false, false, 905.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classCreatorRest", null, 122.0, 0.0, false, false, 906.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(142.0, 0.0, "lastFormalParameter", "lastFormalParameter@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 371.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 371.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableModifier", null, 113.0, 0.0, false, false, 371.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 372.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 373.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 373.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotation", null, 114.0, 0.0, false, false, 373.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "ELLIPSIS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 374.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "variableDeclaratorId", null, 111.0, 0.0, false, false, 375.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(143.0, 0.0, "moduleDeclaration", "moduleDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 968.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 968.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotation", null, 114.0, 0.0, false, false, 968.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 969.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 969.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "OPEN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 969.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "MODULE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 970.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 971.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 972.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 973.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 973.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "moduleDirective", null, 144.0, 0.0, false, false, 973.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 974.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    return null;
  }
  static subhutiStaticRuleVariants_6(variants: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant>): void {
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(144.0, 0.0, "moduleDirective", "moduleDirective@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 988.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 989.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "REQUIRES", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 990.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 991.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 991.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "requiresModifier", null, 154.0, 0.0, false, false, 991.0, 28.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 992.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 993.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 995.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "EXPORTS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 996.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 997.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 998.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 998.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "TO", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 999.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 1000.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(15.0, 12.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1001.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(16.0, 15.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1001.0, 26.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(17.0, 16.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1002.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(18.0, 16.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 1003.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(19.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1006.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(20.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1008.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(21.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "OPENS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1009.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(22.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 1010.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(23.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1011.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(24.0, 23.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1011.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(25.0, 24.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "TO", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1012.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(26.0, 24.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 1013.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(27.0, 24.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1014.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(28.0, 27.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1014.0, 26.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(29.0, 28.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1015.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(30.0, 28.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 1016.0, 25.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(31.0, 20.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1019.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(32.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1021.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(33.0, 32.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "USES", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1022.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(34.0, 32.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 1023.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(35.0, 32.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1024.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(36.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1026.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(37.0, 36.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "PROVIDES", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1027.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(38.0, 36.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 1028.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(39.0, 36.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "WITH", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1029.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(40.0, 36.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 1030.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(41.0, 36.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1031.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(42.0, 41.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1031.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(43.0, 42.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1032.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(44.0, 42.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 1033.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(45.0, 36.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1035.0, 17.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(145.0, 0.0, "nonWildcardTypeArguments", "nonWildcardTypeArguments@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 858.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeList", null, 14.0, 0.0, false, false, 859.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "GT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 860.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(146.0, 0.0, "nonWildcardTypeArgumentsOrDiamond", "nonWildcardTypeArgumentsOrDiamond@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 917.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 918.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 919.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "GT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 920.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 922.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "nonWildcardTypeArguments", null, 145.0, 0.0, false, false, 922.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(147.0, 0.0, "packageDeclaration", "packageDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 72.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 72.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotation", null, 114.0, 0.0, false, false, 72.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "PACKAGE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 73.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "qualifiedName", null, 1.0, 0.0, false, false, 74.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "SEMI", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 75.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(148.0, 0.0, "pattern", "pattern@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 829.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 830.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(149.0, 0.0, "receiverParameter", "receiverParameter@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 196.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 197.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 197.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 198.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DOT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 199.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "THIS", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 201.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(150.0, 0.0, "recordBody", "recordBody@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 726.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 727.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 727.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 727.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 728.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentRecordBodyLooksLikeCompactConstructor", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 728.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "compactConstructorDeclaration", null, 123.0, 0.0, false, false, 728.0, 76.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 729.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentRecordBodyLooksLikeClassBodyDeclaration", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 729.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 8.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classBodyDeclaration", null, 87.0, 0.0, false, false, 729.0, 78.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RBRACE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 731.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(5.0, 1.0), __QinJavaUtilHashMap.entry(8.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(5.0, 8.0)));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(151.0, 0.0, "recordComponent", "recordComponent@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeType", null, 17.0, 0.0, false, false, 714.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 715.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(152.0, 0.0, "recordComponentList", "recordComponentList@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "recordComponent", null, 151.0, 0.0, false, false, 700.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 701.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 701.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COMMA", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 702.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "recordComponent", null, 151.0, 0.0, false, false, 703.0, 13.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(153.0, 0.0, "recordHeader", "recordHeader@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "LPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 688.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 689.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 689.0, 16.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "recordComponentList", null, 152.0, 0.0, false, false, 689.0, 22.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "RPAREN", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 690.0, 9.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(154.0, 0.0, "requiresModifier", "requiresModifier@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1047.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1048.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "TRANSITIVE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1048.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1049.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "STATIC", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 1049.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(155.0, 0.0, "superSuffix", "superSuffix@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 946.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 947.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "arguments", null, 121.0, 0.0, false, false, 947.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 948.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DOT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 949.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 950.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 950.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "typeArguments", null, 12.0, 0.0, false, false, 950.0, 30.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "identifier", null, 0.0, 0.0, false, false, 951.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OPTION, "Option", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 952.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 952.0, 24.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 10.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "arguments", null, 121.0, 0.0, false, false, 952.0, 30.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(156.0, 0.0, "switchBlockStatementGroup", "switchBlockStatementGroup@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_AT_LEAST_ONE, "AtLeastOne", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 788.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 788.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "switchLabel", null, 157.0, 0.0, false, false, 788.0, 26.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_AT_LEAST_ONE, "AtLeastOne", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 789.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 789.0, 20.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "blockStatement", null, 48.0, 0.0, false, false, 789.0, 26.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(157.0, 0.0, "switchLabel", "switchLabel@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 801.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 802.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "CASE", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 803.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 804.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 805.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "pattern", null, 148.0, 0.0, false, false, 805.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 3.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 806.0, 21.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 6.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "expression", null, 30.0, 0.0, false, false, 806.0, 27.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COLON", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 808.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 810.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "DEFAULT", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 811.0, 17.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_CONSUME, "COLON", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 812.0, 17.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of()));
    variants.add(new com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant(158.0, 0.0, "typeDeclaration", "typeDeclaration@0", __QinJavaUtilList.of(new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(0.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_MANY, "Many", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 131.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(1.0, 0.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 131.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(2.0, 1.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_GATE, "com.slime.java.JavaParser#currentTypeDeclarationLooksLikeClassOrInterfaceModifier", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 131.0, 14.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(3.0, 2.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classOrInterfaceModifier", null, 89.0, 0.0, false, false, 131.0, 88.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(4.0, __qin_binary__("-", 0.0, 1.0), com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_OR, "Or", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 132.0, 9.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(5.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 133.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(6.0, 5.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "classDeclaration", null, 88.0, 0.0, false, false, 133.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(7.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 134.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(8.0, 7.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "enumDeclaration", null, 93.0, 0.0, false, false, 134.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(9.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 135.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(10.0, 9.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "interfaceDeclaration", null, 101.0, 0.0, false, false, 135.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(11.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 136.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(12.0, 11.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "annotationTypeDeclaration", null, 85.0, 0.0, false, false, 136.0, 19.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(13.0, 4.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_ALTERNATIVE, "alternative", null, __qin_binary__("-", 0.0, 1.0), __qin_binary__("-", 0.0, 1.0), false, false, 137.0, 13.0), new com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence(14.0, 13.0, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind.__qin_field_SUBRULE, "recordDeclaration", null, 109.0, 0.0, false, false, 137.0, 19.0)), __QinJavaUtilHashMap.of(), __QinJavaUtilSet.of(), __QinJavaUtilHashMap.ofEntries(__QinJavaUtilHashMap.entry(2.0, 0.0)), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(), __QinJavaUtilSet.of(2.0)));
    return null;
  }
  staticGrammarPlan(): com_subhuti_parser_SubhutiStaticGrammarPlan {
    return com_slime_java_JavaParserStaticEnhanced.__qin_field_SUBHUTI_STATIC_GRAMMAR_PLAN;
  }
  staticRuleNamesById(): string[] {
    return com_slime_java_JavaParserStaticEnhanced.__qin_field_SUBHUTI_RULE_NAMES_BY_ID;
  }
}
const JavaParserStaticEnhanced = com_slime_java_JavaParserStaticEnhanced;
com_slime_java_JavaParserStaticEnhanced.__qin_field_SUBHUTI_STATIC_GRAMMAR_PLAN = new com_subhuti_parser_SubhutiStaticGrammarPlan(com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants(), com_subhuti_parser_SubhutiStaticGrammarPlan.decodeMetadata("H4sIAAAAAAAA/+1d938kxbGf1SreHRfIOZlgMNgcyWBjY45sA0fG8dl70upujU46VhJwOOKc7eec35/3/oD3s6zXPdu9U1uqTjM9uxOqPp/SdKiqrk5f9fT0zrz2xqnnkiS5XPD/JhM0L/iEDOwL0mFMJ1RmJlbZUGf8J/mQqkoBklbnC+h2iLQ5dV0AaUeQzDIIX6KuXZB2OMCHRRA+BMJL6noUyR8H4RV1PeZZXhNI99mHYoD9JxYLe/M59TooLsfPggofAenL6irHS1eFD3uWsaiuh9RVjo2jIP+4usrxcCxmu1SZLWOj001hIY0qIBqh0FRQiWm6hFG8CEVFFebpI0IsO4Dm9DhjVGkJxUQUSYwqNWXZeTFtAepmQYkqR1VR+8nkkn9ER1VOwjd0Vt2iN3Q6XsbNHJa9BIRXPMtoCtX6Jk6FC9/EofxL1HWlbFCrEhNjQ8//zlyM5VbHEKbitjxK1rVMoMo2+ePKD5E36YXapnRD5WJffamono+uq519+sFml0RHmQGvOEzJUjImHVwGLo9Ks+VjGZcuRgVTui+FIlGIvG87UnXyKZOojkbHeb4ZbQnxzSjzATSJYQuQRpXlKGsupuoTowpz2aiykAUlqsypovZ1OJlTsSw9YYApQKYtsKYQg0zYZMTbWtEm+ozrBWkxC84r1mAyvldS8QTCDBVi6ClEHcDdJNt913lNIIagHDCUjMbDnJ6+s/YpQp0ggac78zqqQAVkLamkxIVC9hBjlBfpJ376iSLGJP20DmJWYrhSy6q6EuOXeU7L8SLHSopVSTY+IOmtkZn7W7CukMDT77j3aXhi4YcrpknlI+ualFTZJn9c+SHyJr1Q25RuqFxs9qWiekni1nO1s08/2OyaHr514BWHKVlKxqSDy8DlUWm2fCzj0oVytnRfDgCkYHkNZC4Zqk4+ZRLjRB/TiXM0gameFIJomHj51SCWHZpXD5BGFdcj/Q4YfowsTaAiSIKJkaXGLDswlh1AGllWeL3SYuL1CvMYHfLqATqUBSWqLCuz+8nk7yiWVVpSbL87C3lAFL7hn0Nx01SQ6fg3Gjh/0ZE/D8ILFllfkna6TikmG7lOUM+BNNMmzhKVrvIWLXnz6roQYdp2Zw0ddWY0JsDvueLvdlNhKm7Lo2R9dkNx2GYnxFdf275+uXwLsV8lch11CNkN13KV+gkInlA2+zZ9nzqZfPWxVTHeM9TXa7ebGBsawPgnIG2jWKjId3U1Z9mJMWwA0qgy+5+AVOF/fxV8qCNFRxZptO4zrW2MxoRGlkNV+hlIFSZ46PEYphE1EWS8j74wkyCj3+jT/LeG2PJ4KycOhfSxjx3exrHUuQ7s0y8BaCVJIxZv47SFYiMhb+PUlGXnxbQFaPxWQUYVJiuZ0IhRpWUsO51KA6TfSMuo0hbitQqzER2K2AI0/mwNo0rLiB9kM0dDFzQmNKrM84NsplzUuOdLzNFQZYnXKi0jXqswjxEhhg1A+mOec4wqLSHeV2GOhibQFqDxF6MZVVpGvFZhjoYuaExoVOlW+S2BLhmfsss+bOczSdt02K4ouQ7Z5X1jHz645vvGvqKH7JxHgbE9qsxYIFAnJsaGBq1lfrdWw8n2XyIG8ZKoRiw7zCctj11AGl0O841Wy4hRhbksVLk0C/ILanzKL/tXTfgm0eRHHSiGz/yrpuzaodLrwkX9JcbGZXqM8JKIaYJCkJmXRQ1n3dE+Moo0skTff2ZqDoXsszPINJz1WPCQ0XS51uHlC5OVTOjCqNIylp1OpQG6Qgfm3U+hOowsNaYyt4QYWWrEssPKsgtII8siP99uODGyME8TWa7KgvbtFrVoAbHEC2jw6/Kp1+e7XqkfouOy5fLJ5W+IbBF9l62iZdvsxdCbVdiXOoh9KaQsV5+YfPD1DdoygfseuuIwJUvJmHRwGbg8Ks2Wj2VculDOlu7LviCa9k0A8PrYnqgjLsOjzD00Pq7OghLc01cRKgA/gkSPqPQE7qpPI8Rr1egkW/Owuq6o6yGrxqSu5jl17aqrvvVYUNdFdV1S12XCnk95RYnXtRE4GY0ZOV4OecrrMSLHx7waF4tqPCznKD/mIYVr9PjiHf4KUh2OVDGqTAd1Kn2UCo2Ja9U1zuvnmepPoUjGqNJQlp0bIgvoOj2WeK1SUar6eoVRpYKzfBa+AbpeXRlVqkAddK0LMbKUOFNn7Ueov4o0svAR7YoRBJk6AQ2DTHmTtjZfHERj4gbtPy9fmLwIIx6jSktZdj4MA7oxC5Zz3An+6zUd63DpJYn5mInrXztVPrYFZaZ1nMnniIurDXzLNtXPRyc2+1JevZDjSK4+8Okjlw9R3862j47NmHSIMvaQfNAxJOwn5T9hbw/pHkj35QCgC5LH7elqf8onjzLxMaSb9PjgpRyTkyik4aVcC1l2PI4DujkLtuf9SSH5IfImvVDblG6oXOyrLxXV0+E8YySkH2x2K/WuJluaLR/LuHQxOJjSfSkEoELl1dW2jCXvXX3LJKpzaxaUQNlV6vs6LC4q2hnbpm+BBa525pJswf8JdQWvshuH8cjl9Nmnywc3+lC37sOPA7mOR5pJn9Pzpfu2+6zTNMN0qi6utO/rxFsSlXO7DHS1ioSo9B1Q8jT5HTIgj5R/SgbkufLPyoD8ocHHZECeVn9KBuTPGx6TAXmG/T4ZkD9w+agMXCL4XhmQX7d+XAbkR96elgH5XaZTMiDf8HtWBuQsSX9sLV24IAOXa5+v0D5fqX2+Svt8tfb5Gu3ztdrn67TP12ufb9A+36h9vkn7fLP2+Rbt2K2qIZKPqEomt6mGSF04magC033NO5WJ1Eb6w867VCWTuwWfS5QLaWvco1vjXt0ausc06f8bS1EOu/quJG16Nju+KyQYpnSoPJe/vrZ9/XL5FmK/ShTD51JXupS+z0rXd7Xq0vepE1Wmy+cqclF/ibGhEesYbzS2hGKjIG801pRl58W0Beg2PdYYVZhI8llPSmJ0aQnrDsdpME+RRpeuc3cuy5gIMea0kiDmUPejes+FMaclrPsepc2BPE23a1le0TSYXKuRosTIUiOWHWaLF7ELSCPLvGPPuAOGI6NL1Wnae82MLBVl2TnTLAuQRpYVXrNUmEKffk2bGFmmjBaSZu2HyS9Fd2RB/jlzHci29VEFYpCZ/mSu3M+c0Zi4MwtKkJFnmjSYLI5zFlVKQu/02kMMSDOjaQKS3tuV1y5Khy/otREDVMMASo2JLoinL+T18EvTR7Pg+F3hyeg8Zci7wtXwA7FF+4Fyh615/BtsEOsGwp9sInl0dMUQN6XZbCQjP8Zp+D3bLnsuvyjbqlXJsil5k38m2752TO0n5fQ7wmG65HlH+T5pHXBdyam3VMK1LPL9t0L1h9Q7nKNM3HaV+rxEMloqTaRR9pPRu8yNn5eQFbX4Bn8PLttgxZC+5Mslwr/Pv5c9IH84Rzn4d936X8a1fHSh8YSPnsMl79xB8SjEy9QKs+ygfYA9iTpqUEY5gDTm3FmVzboi93u2SUXl23Rd8iF+2Oz6yjSdrL9DKTqpIkzGBKQ5f3EdWIbxl9pUetOZGBsapB7kz0Q0iKaJcLz0qTDLDppWOYA0qjzatKUPTou97KFsuJY8Jvu+Mk2nSi5/8MTBeTCNyifkO5QutmFLbzoTY+MuPUZqcVSrTTN3FmgVo8yZLIdkwbOeXdOcwVUvC40JjTLOr1CQDw4rAjxtAR8GnrCBXrnjTFUDg2mWhcaEBp7FEfDMK7P7SfZ0e17FE3gnRoUqAENtIdPBqbLLLArzvBVULixMHJwquSx9KxQLgi4L/4zFnPdRqUzK71MQLhs2HZ/PGIR8eoLSyVunPOVSFPI5ixC9ulAsv52fm9if0bEklOfywabvUyeTrz62qsRF/cXHku7OgtXYJ2cql6gVVkxw5BVYjThJDrwCpYwXHWiQUbtPC6qY/QT8+mJBJSSuu8AsVIOndfD+qcwndvMoPofivvZ9ZepEee8pK/nULlH3X9pJbBOmUfkqPg/Cc5QutmFLrxMnObYLibGhEW2RX93SMOKDS8zjWT+tcgBpZDnOxyFbTLwHzjxGh7x6gD6mx5XjlBGvV5pMtX3Iz1wOskgqoqvonixYzofimepH/DifGQJGrMf5GmxW+AappVR0i4ZRpSEsO7OILqB71bU7HxtVbKO1zo9VmA6SE1mkUOyRzFwtRmMCfCnW44viB84fgljihT34pB1+FGx6AY2PrE3fVLbJH1d+iLxJL9Q2pRsqF/vqS3k3d0LKsrUzVbavT9qO6Tu1c/CKw5QsJWPSwWXg8qg0Wz6WcelCOVu6L4fiVAC2OU8fEnWfKMNVJhpfkj6hx0ihH8jaDrX4jNq8B2d87Ln88vE9RGZa17x+zdpnHyqqZ9K1jSmq3CKIK+lDNPsOfHWRmrlYjtI36RhWSQfKNaXlIVPdYpVTFuJi33E6FcZluMokqnNfFvRYsSaWFWvHicl6HM6Bq14NzKE8Sg6Od7wyc+knSMfkB7Zv8wWmUWX4ylK2KT2YRtVbp8FDqJQMVYbPytTlNy7L1L4uPV+f8l59qageDmuibOI+M/ngyjcRbF/8f0AfkpS/F5lDK789nZ9MrlZdrwU+sApG9uAri+dwmpbT6TAflD+WScwrY+wTtDGRTtmwcQCuB8ub2o4KUz55lIl/G3RSj6eZPmkJXWG5dF2rVFOea+Xm44PLrq9M04lcnRZlaTCGnnYS58E0Kt+nDMqGLb3pTIyN+9V1rhIvzGrbzJw1xWrvUp4BS8OznjFt4RhtjcbEA3qMVQJZmKpNFBLxyZIWsux4HAf0oB4vfBflkPf1ge+i/IjvooANW3rTmRgbD+tAJdY6bZuZsya+i2KO1tZoTGhkcb5qOEtPvEDGd6lh0zPJuiaE73IidFkUYtvXL2zHJVMniuGzdVkkM/DEgGHT5MEyJh1cBqXn8sGm71Mn3Q62OteBi/pLjI1P6gD/uKjFVARleAuoQSw7NK8eII0q0ZdEdSbb0oTJTo0DmYRYjjAHgcwj6spPr9pCsRGzcajSFpadF9MWoE9nwXJe7WI6gN2xyLhs+Nh12XTp4LyOp5yPr9pWSPlQD/qCl1kh7QLDpvrheNnsS0X1fPJ8+oBa5vr4BW2X9eEGn48gHPjwQpIt1awfblBy5IcbdP2RPZNve6jM/+xPHvT24gAADJb3SD9Qd5jvUSY+nK2B2fUG4nE8DZpQ2HYjZhulNtmQmztK1mXL12ebvEkv1DalGyoX++pLRfVC5Vz++vqj80vZx6f0bWVQejYfcD6WcelCOVu6LwWgXbB8iAzlk6tMojoaHQ9VFh199E1lMzIWu/oSI6NFxqTDyFgLZDzKyJjTHiMjIyMjo6WONUXGx7Jgvbc6Q+y5/OJtTjpeNvtSUT0X1XqLUxp36OAyeIvTIO8hF3uL83F17c7kMF7o0oapehTlibY0FMMO82wYjYknsmCcw3g+QBH6r7morCsd/7umdHxkmkzUsiYGka9cLAo+ReRMaTodhqE8Tm8yJ0ncA4poTGhQKvZSm5izs6itPAhk0m8j+kCaCfKEjOSYutpZKh+mx/K/ThyjzmhMaORZcL7sNYHrIS88clEZ8zl0IwTrhrzUk8lNQbdgUiHyfAn+2rvS8345J3Mw5pzSgUr8VKGtq4pZUSmrmZgjddazpS0co63RmNDIUsrLJzpEmIqb9EyyriULVbbNToivvrZ9/cJ2fOTrQjF81jZMBxAOPC+CYUqWkjHp4DIoPZcPNn2fOuk2tNW5DlzUX2IsPZUFq/UZbdsBg7w2qPSyDhHk8S/0UILPYYG6UAxffR7u52X8cD9ID8VDHu6HlEEeCthHD/dnDWIBYFV0pYYf7muw6/I9YAuJ7wGZo7U1GhMaWRar8radIoPd9mjM9gjNFg/xx6Rnu7/MU05TSNe5lu9chmHfMrG8boe89prCxNh4Wl3n+OWCLaAy0I/f0FNDlh0X2x6gZ/R4q8SNFFO5xKjCPA1UeVaPN/6ZcE57Jr1Q25RuqFzsqy8V1QuVc/nr64/1Lk5m4BkIw6ZZimVMOrgMXB6VZsvHMi5djAamdF8KRaCYCEjV3dQeHqgo6bksKJHxhFLf12FMJzKkzB1SD50iWJq02cUPEUGs443sx9X1MIjjfBg/AsLzhIzcpTuK0nBZ+PEaJQfD2CaUgXlYjvLFJgPDsoxlIg3bOgZ0F4HcEsqXaSsePki5BYdfJr0uSpPxQ4Ru7KsvFdXTYSr/EoO8JNwPlA++vsH2Nf2HkXlyPh2HaSB8RF3nYfo+ekyp7Ohvx+OzHsfBdQ+VYX00up/9ZnlZXbEPMm1JXVewrkhbAHLHQXoXpvtywH+MYHnqP40cK1S/QL8DysSPRj+fBct7gOFadrdx876p5LzBl0KG9NqcUWB29zGgL2TBer/txteWy6c2vOmGssdvuan5W248dHAZM3nLDZSl0n05APCC5T3kYr/l5gU9Pvj5TUso9tKan9/UlGXnxbQF6MUsWNrPuvGqZc6QZ9Mzydr0TWWb/HHlh8ib9EyyHZTmKitULvbVl4rq6bCvLFWmT1/Y7Jr23ubgFYcpWUrGpIPLwOVRabZ8LIPyOlSdtJwt3ZcD8ClY3oWRVN1N7WHxCdJL6hr/Zwm20R57RcA0Wyqy4daqU7JNZjQmNLIs+Gy4JfjRbyDY5NlYY6o3+dwG+mwqMdeY0ZjQoHPEsZxRtyq8pGEyEC9pmDG6vJIFy/2lZV4wCX1exDRb8trJloI5Bi4fZKgJozGhQWZuvoznY4wOzafSUIW5PozGhEaVhcr8FpuRqF5U2lN3aXzWs4U5F6q8qq6dqr3BjykudVC4bPDmIz4VZtlB6lrqTSYaExpsFks5OMjLkeYT3xgxm1BlhW+MmHIR3xgxY1R5U12L/cgBr7qpMIybkIPS9bVtsuW65s0rYtfULlRaEZ9sZVaBbPX2jYeWRX5rD1/hTDGhHcyn9B0zL8F2qLhLHvtrKs+n/FkjE9XGVNxW5xwIKOmtLOj4UcbBM4CTu0X2E4HwALwpDOOmA/OUrq9tky3X1SYbYsdH1+fHBD51KFq/KhG1m0XFfX/s4yrrwA8xEuJHCTAPxikdmz6lhxmnm+So/P3RzzutPwpx2bXVc8ZYiV8gA/HR+WMZh208fjRWLlbmPnTaVMWVVJ2oMXvmsjKz9qGujMaERhXXeejEiioxZ2bR+wybDde9rc2W7/2Qzf+6I1gs/yvz3XWtQ+lqZ6l8mA7zTHZi+VsVjuE7GhNfVFf+GESbqQjCNGZ9w5wfYdCY0Kgy+2ONdf/f31aKjirS6KxnF3MUVKnOmQCKGHGqS1Ndq8gCZz2DmL1R5ZJKo0oeYiSaDlX+Dkg6OWsfms5oTHwpC0pUOaTCKyA8okNC+eCj9hJCjo3kBesQ9ydpa7Gg/oohvZscfIBNbRAvg7DG9KUCPtWVyto4Xog0YRYL6K4QaelXY1QYPyReVlc5HpZmDRazAKcYNgB9WV3n+H3RLaHYy8nKL5uYy0MTaAvQV/RYY1RpMJV5Y8qoUiOWHVaWXUBfVVd+yE0RbxO5qRWoIis6ax+qzGhMfE1dq40qPLurS/w4itmEKkszRZUiqGE72ms7Auw6/hvik0nP51ixSabqFMNfbSMqMkmDMfS0kzgPplH5PmVQNmzpVeeivhJjQyPToUqvd5jiEe/4MkdBE2wLkEaV5h2/YTpIZaypGFVqyLLjYtsDdEaPt5m+6CfkLiTkTgfrm65584rYtbVBiE8hclUmV3vkrQd5pyYTqCucITZZm75jxiXYDhV3yVP+UeVRfvv4XBU21TOP70SzrmbBOB/EYSDMr4vlqLS2AKHv9l+oXUkMhIb0KnOSZAc1bXUuCITFDimGjkxf1Aoln1nvixChm+I+6a68OlAs/3O/a8MmG2LHhG7UjDGhjA0tffzK429VOIbvaEys6QAfbmwZlYIqzPVj2YkxbADSqFLO9zeYqkFlrqsYVWrEssPKsgtIowo/7Keo7nc506BWoIqs6Kx9qDKjMdFX12ofmWYql4qgZytQpS0sOzSvHiCNKvy2QaZ8xG8bZDahSrXfNpiHGKWmQ5Vfq0gnZ+1D0xmNifUsKFFlWXXBfpK9tUrSskqTU3VpAnc6E7HuRGx+IrYwEVucRK+JmP0zXpr0p6K6hjhOU6U67ag6kvZM5dhsqpaYSPOxHVo/7TuV7lO2qx4+8RDZupDuQ3mFb6VTIzWYYDuYIHkPXdNwQr+hbE9N6yUkj/UP6AmdeSVD6ZE+aAhB+bZyTHXStrqWOteBx/4m+T5NtofGx7ksGP+0pA/hk3FUfh7i80LlUxXOC3UseXxeaEocw3c0JgZZcPwOWQVA5nfILtvXeSDmv1qcXIGu2NejnivJ+ST7747jOE3Vy2lHeTdedWGbprJsdvUqFqbZ7OM8StakT6XPgauPLZ/62crzSdMrMb3SDtWrC0n/te/w/cFFVta4TUJXpMsm2WT0TtzQFenifo4VKZGfa0Wq24JKnxZwR2CqfYL00Rj5ZhaMsyJlqiaZVtqxV8WV34lkJlfUxpV9XvuA3lbXLp8bbTDxuVFmEl1i2gWkUWW2r6+rKtV9t2sa1ApUkRWdtQ9VZjQmNrLgeH9BiO0nYJ9jQSUk8J7IHoq4h2+jIvv3pr12bDvW/j21PWPb6oBbQHWkme/fx0aJRN01ULraWSofpsO8hNiKSbKHiDh9PsTXKnGMPkNj4ry68mn3NlMRhGnFWqgtLDs0rx4gjSqLlUYVvtOpLvFnB5gxqmyqa6fSqMJUDsVAa16rNIBlRxbVB7Slxxc/Y2ow8TMm5ijoYbML6IIed4wqLSE+D8M8RoKYtgC9kwWrdeiOt1KmS7EO4JUCMtLwrCdhWziJcAAPjYmhuvIjoSbRNBGaly4VZtlB0yoHkEaVOC9Ash0lCfkpp+sYim/ZJn98jr74ypv0Qm1TuqFysa++VFTPR9fVzj79YLNLoqPMwLMThk0zGMuYdHAZuDwqzZaPZVy6GBFM6b4UikKh6OjTjlSdfMokqqPRccl1Y6cW/hA5vQGzQ4SpuEkvSbKzd9QkcAEvLh/bgjIwD8tRPthkbHmmtrC1l8sfU9mm+vnoxGZfKqqHwy5ZqkyfPqIItqMJdDvwisOUrOI9S56tjD0kH/QTVuwn5T9hbw/pHkj35QAQDZb3bUdo39QeBsY/Yd3JguWAbl4ygWxR/WmBah4fQ8rKI990coJdAc779qWiv9cPKcP0O//Q3/83hTHY7aprd2q7em2afW2g3KgilSswI5gjMBoT76rrbL9hbtP1tW2y5brmzSti19QuVJqrHXxlqko+bZHXZu7P9VI6Nn3HLEuwHSruksf+msqz1c3mc1XYVs+CaCfpvSxYje+W23QZ/Mx2TbImm1UkvQsC4zFsSmLwqyf4kd8qjwx+0/1WuY3y2vKZNb7I4EJenzJC8+pAsfyv0nsLjEignTUhDIWUJjux/K0Kx/AdjYn3s6BEokVVxH4Cv+ewmMFReMhjF8x3WeEDGl0POZsP8jqXmAEIy5rs47wFIs3HL5M9U7k++VUnvQdf9GUz5BLMBkJazyLbhfKuSYnqQy6XKB/UdQHm28ox1cnkq4+tinGhZw7E2NDAxy+vawOVgYJ8OreGLDsutj1AGlWOMKq0gBhVmKeJKicYVVpAjCrM00SVKxlVWkCMKszTRJXrGFVaQIwqzNNAlYt6vPGvnnPay3Ncx9evULnYV18qqhcq5/LX159cz7FcsxTLmHRwGbg8Ks2Wj2VcuhgNTOm+FIpAMRGQqrupPTxQUdIH6sov820iTePpPa+3asCyo8q2D+hbOsAHFB22fNePIevKulEs//mA4hRmetVRCo2Jb2fB8e9E1JCbG/3NoGgc6jhAyWfB7ZqwGAzyylMA4pLHMlSaj17TCdc15gsNMFhZ39Jhy4d5oXLSEZim4yC/Q4VNMk1nVM/CLzJAY0KD1XyjbsbaghZFqZRlUNVYOjhrH6rOMdoIjQmNLEuV/w4Co0UcKqMdp44sstBZz8a6c+w2RGNCI8tK1b6FYCMGmThUxs0gg0wNOYl8A4jGhAaZo6XvJ/s+Sw2x6ZLNs69s8yPvc+o6U5F+sVHQnrItH+a57NjkTGk6HYahPE5vMudp6xxodLyMdw+ZiIGpnkTtqccgBqYacpLEey8QAUzfzYISmPS7BvZ1WFxUtDNucx2bm4h1vG7RlN6BMBU36Zlkbfqmsk3+uPJD5E16obYp3VC52FdfKqqnwzYsnENXqlyfvrD5YToQOQevOEzJUjImHVwGLo9Ks+VjGZculLOl+3IAVgXLY8wkeA/bN7WHxSdI39NjhI+K57Rn0gu1TemGysW++lJRPR9dVzv79IPNLh8Xt6T7UgDaBcv7tiNVJ58yiepodJyv/HNEpjgU+1690qcTmN2IE8sWII0qy40698RkJkYV5rJRReNDsY+iMNWHGFWYy0aVH+ixxp9aCswrYjdkP6yIT7Yyq0A++4Ah+4w+ZbX6CyQ2e7NGORvimeqZx2+iGX6oA7y2ahnFQkVeW9WcZSfGsAFIo0qzfv/WdprmOopRpcIsO2ha5QDSqMKf6mgiTQNdGFVqwLKjyrYP6Ed6/PEdUEuId5eZx0gQ0xagn2TBavyatpMU20CkNiM7KA/LUXk2+YSIm/xw2faVaTrp+kYFqSTJ9ytRrKf9w3kwjcq3TV4sD23Y0pvOycFx/1MdqMTSp02zsgpU6Q1gaXjWM6YtHKOt0Zj4WRa0L3/UIASxrhfY4Df4UW/0c73lz0fHp5wQ30z6NhnfuuUpl6K8bZCnrapIsfyGdkwguYeuOEzJUnq2NxhCGSy3Z7BhKsvnTYku33x8riIX9XcPjQ8Nkq7fYKj5y7tPjSDefWJOWXZeTFuANLKsOJ6Uqc0BRpZGECMLc9nI8nM91ir7q3qbTZ/yKR0qz+Wvr21fv1y+hdivEsXwWdso5ZfulL6tDCwH00w+2PR96kSVGRsJ6oBcxNjQiNX12YrqTKJZpbaiQuy5/OJtqOpTbbahpHGHDi6Dt6Hyc+xtKA2Qh/hYZUuIbxSZU5adF9MWoF/osdZaVKnbeqNq1BhUkZWZtQ91ZTQmNKostBZV2kgxkbQxqNJGlh0Yyw6gX+px5vEgPmFkaRAxsjCXiSy/GofmxkNNiO2bxtCv9bjk9U2LqQgqMQo1iGWH5tUD9Bsd4N95tJBitTf/zqPmHKOt0ZjQyMKvkW8bVRpVmGuNKr/VAUaVlhGjCnPZqNJtxN4K30VNnyqJKtKxWfvQJkZjQqPKykzXKrHRwGQv5EcaRctqK8V+U0/UE162tKJlxfS17ozGxO+yoESZBdVU+zosaSFDGt9QyYgUc2bDXw/g9HkUj1FWN4KdplAl3h1msHXgk8AibT4WoiSjD63PHA2qwGhMaETqtnaPJgRpeI1DUyXvqPLOjphybWI0JjSyHG7EPk0ZxGhip8agis/MmbUPVWU0JjSqHGvteqVtxL9yZB4jQUxbgDSqXMVrFQPxWsVOrUEVWdlZ+1BVRmNCo8p1vFZpCfFahXmMBDFtAdKochejSkuIUYW5bFT5fRasxoeumGZLRV9bxyDTLM71ujo0Jv6gA7x0aRnxDwKYx4gQwwagP+kxNtPN2yIjnNLtEHm2Y7s+8r4+uOz6yjSddH0rcdQX62kncR5Mo/J9yqBs2NKbzsTY+LMeI4XWOq5ZieM+B/5dSOGaxVjfdM2bV8Suzw8bYvhkK7MKZKu3bzy0rA/xjKCuNnSAsjZ9j9mX4HSTnC3fhaw2u1VFQaournAEFIz+1QbqqwpUGMZxOs6HYdfXB0y2TGVSdn2/sJBHLjSM7fr4i3USQ3qeuhRpryoRNU5wGuW7a3yZyPaVCP2VB/I77TAO08F1j7JjAIIOsIm/CmGKY07zCRv/2ae/AnHArqX8mQMxbmNDfM8k58n4KxF/1eODt+BaQrERkbfgasqy82LaAvQ3PdYq+zlBH31T2T43kT7lF7l5NJXj", "qmPV1kM+FMNn8qYUzwR9tc0Om4xJB5dB6bl8sOn71Mnkq4+tKnFRf4mx8fcsyGcdQm406ggm06DGLIkSz5sMWelZ+1o1RmPiH1mQQaaJ5AOGeXdxKGoMyDSRZQc58qO8LQWNCQ0y/B2tNlMRhGFUaRDLDs2rB0ijygqjSoNomjdvjCoVZtlB0yoH0D91YD42qoTsCTPVm5zIIoVmOeqZZ4Ys3ULIYns6Y3qS43oq4/MEyMeeyy8f30NkpnXN69esffahonomXd+njCFj1+aH8VinJjwrKTlK36RjmOXGI5qOeniRqW6xyglFthB5U9uZwrgMV5lEdf6VBcvb3HYt63hp1xwqsqyL9hpq5tkyGhMaZJxnx5NAkHGd7Tal2WzYdHzOJoecbe54yvn4qG2Fnq3uIMZ+ueqE45S9EPnY7EtF9Vzk0/bYlq9PtrPimvfQFYcpWUrGdub6wNnsJAN1fHZ7QkfJkWe7df2RPapOe1CWSvflAKALlveQO1B3qOdRJj4r/m8dcJwV74AhSiNvzKVaUVsmfeq+yHX/lfcerykUq97kPWZRLmKH0tXOUvkwPZb/deIYdUZjQqPPfGW+leSzY5JnRsA1Vh7deadUe0i2R4xvPgU9wZQKlrzgG9SE+B6Sp958Hr0mchLhm09oTPyP6uf9zmiWn1B9q+Nd1W8w3gXxo6p/9jtJmrKYKART+RIDFkFczuslEF8SvAz0pfwKiEtYPATklwUfRuUdQfKXgLj09yiKH0Pyx1H8BLJ/KfLvMhCX9bkc2b8C2bsS5V+F7F0N6ndE8DVI/lpk7zqUfz3y5waUfyMq7yaUfzPKvwXl3wriK4I/AuKSb0P+3Y7id6DxcCeIy/b9KJC/SfBdqLy7QfwSwR9D/t2Dyrt3ZD8tQMY/DvLlePsE0r8Ptd9JNN7vR/oPoPwHUfwhVP7DaDx9Evn7CJCX+o+CuGyvT6H4p5G9x1D/fQbZ/yzQl38eR/59DpX/BNI/her/JJJ/CuU/jdrzGdTezyL950C+xJ/nkb+fR/a/gOr7ArL/IrL/EtI/jfJfRvFXkL1Xkf5rSP51FH8D1f9NZO8t1L5fBPFDgr+E4l9G8l9B/fdVlP81NH/+C5X/ddR+30DxHoqfQfqrKL6GyuuDuMTrdRQ/C+KSz6HyBqj+30Tj/21kbwPV/zzqr03UP1tovl5A+e8gf4Zovm2j8bmD5HdR/7+L2us9FH8fxCW+XUT+fIDsfwvpfxv59x0k/10k/z1gX8p/H+V/iOI/QO3/Q1S/HyH5H6Pyf4Lkf4rkf4b8/zmK/wLFf4n0f4XK+zXK/w0aH79F4+N3YDxcL/i/kf7vUfwPqD5/ROX/CeX/GcX/guT/iuz/DcX/DvyT8v9A9fknsvcv1F7/RvF0zZcO3pHNE4m+hczmxByKd0H8qKpHAtd8IH9Open4vGpnHU/XfEA/XfOBeLrmA/Lpmg+VdwTJXwLiXeUjjB9D8sdR/ASyfyny7zIQT9d8yP4VyN6VKP8qZO9qUL90zYfkr0X2rkP51yN/bkD5N6LybkL5N6P8W1D+rSCervlAXPJtyL/bUfwOUL90zQfi6ZoPyKdrPlTe3SCervmQf/eg8u4d2R//T/w4yE/XfEj/PtR+J4F/6ZoP6T+A8h9E8YdQ+Q8D/XTNh/x9BMinaz4QT9d8KP5pZO8x1H+fQfY/C/Tln8eRf59D5T+B9E+h+j+J5J9C+U+j9nwGtfezSP85kJ+u+ZC/n0f2v4Dq+wKy/yKy/xLSP43yX0bxV5C9V5H+a0j+dRR/A9X/TWTvLdS+XwTxdM2H4l9G8l9B/fdVlP81EE/XfKj8r6P2+waK91D8DNJfRfE1VF4fxNM1H4qfBXHJ51B5A1T/b4L6pms+ZG8D1f886q9N1D9bo3ii4xdQ/jvInyGIp2s+IC//7CD5XRBP13yovd5D8fdBPF3zIX8+QPa/hfS/jfz7DpL/LpL/HrCfrvlQ/oco/gPU/j9E9fsRkv8xKv8nSP6nSP5nyP+fo/gvUPyXSP9XqLxfo/zfgHi65gPxdM03qp/8O1rzIf3fo/gfUH3+iMr/E8r/M4r/Bcn/Fdn/G4r/HfiXrvlQff6J7P0Ltde/UXy85kvp0GCtv7kzWB/003Ge6FkpxuI7u70NmbH2Uu98f1z8aCv/xETmC4PtdBZk68Pk6M7FC/3nJ23PJ3o3/3hvOOxdfH5zsDMQZj4Q+bo2o4cfl69vbPV2BptnX94abO68MNjpD3vpPE9rsDgqQeT0z/aHMHdJsfi7AZKXFYvF27u94aB3ZqMPyh4j18qo7NWN3vb26eHzwvxwvbfaf13UJNFodGjUNheGg/PCwLujTEmHFYtFnKz6E8Ozu+dF7VPFI4qFIszbTvScT9esyYrMPLW1u7mWZhxVLHyXGWkjy/RjipW1l3tD0QU7qg2PK1Y9MM4clXVCsTI5rtmlipWWTD89fHNrMHLkMsVCpre2llb76fcvDPvb24OtzbT1LlcsXOptrqHMKxSLpXDa7U8O+72dreFTA9EGUmg7lbpSsWhi0fyDs5vj1rtKsSgiy0FlXK1YujiWOX1BjABRUpp+jWIht7q1KWuxtdnbeOKAt9cqFj4DOSR0nWLhGBA6PURi1ysWXvXTCbNzEUncoFiU1n9/dWN3W7TtATM3KhZN05/MuUmxUB9smtRvVizUN3rnz6z1Tm2tXUxzblEshswoJ9NMW/5WxeP8bDil+h9RLEb3Rn995znR968JPEnzblMs2vv87sbO4MLGYLVHDJ3bFYuBeWFre2d98D4SuEOxmB9K4HT6Lzu9kbhzNJiFQpb+UcUCBOQ07Q1Hlb1LsRhGw/5Gj+zXuxULk9v9jf6qHDwy/WOKxcTbPjdYx6PvHsVi+KfZeuClQHqvYtGI2+8NdlbPoUb+uGJhe3dTOItsf0KxgMYzG1urb6c69ykW8zVNfG2nt9OXQz5VOalY5oq5luWmafcrFnAlOmT13JMbvd3tEQw8oFg0dZo1BrcHFYtOEuNdAPNuf9LoQ4pFJde23jo32AD50vLDiuVM2DzX21ztrz2zNXxS2BpubaQSn1QsOlsMcjFYBjsie3tnuCt74fnNd7dW0z6btPuIYlHVbGpIpEwb4lHFAr6y3EkDn1IshvD6QAyIjYunxq38acVi4qyPnU0NP6ZYDLF16dxgVN5nFEtjW8PJgj6rWLStyHvjwprITJUeVyz6Y7A+qfM5xen8OyMG5Npk/hOKxZAWTvc2ZH891Rf/v4ZpU6UypxSLeZjKvKn+AWK5JxXLf269IRqjTykW80Ikb+0OV0e+P61Y4I9Of+1Cf1X8y1/NLD+jWNRcC43g41nFYuQP+zu7Q9Q1zykWetsT4/t5xaJbxxlo1nxesei60aR7dXdjNMa/oFhO5XHOC7J1U8UXFEvbk9njUfWiYjEZMpHTuzurW+dHRbykeFzEZL1OKxaNtn1xc/XccGtTLENQ176sWP5HFhLvTea+olgMjJ3hRYm7G/3ha7vrAgTT/FcVy8XI8OKk7muKheX3Dk7U1xWL3IuD/gby6g3F4l9pb3NzayftZGrUvalYIolcTsl/OWnGW4r1OktmAOW0gb+oWNQulcHGv6RY/itHa7UXt9bS9WZq58uKpR0JJdjOVxQLUErze5tjEYX7X1UsliarGRhhO19TLPq7v7l7Hmf/l2LhxrpsUpz/dcViJXC2v9kfDlafNJf1DcXiH7cSzure3zm3dcB6T7FoLKVAy51RLOb/QFsc99qqYmFkIhP33JpisV4Zyz25df781iaSTq32FYuxMJbGMuuKxaJskFX0/Jn+EJd9VvGkJFXVc4rFBB6QA2egWAicJ8v6pmIBL+fTIsYt9bbiVJUqfEOxgNLzsMzzioXisL+6NTyguKlYjNZ3EYar0bqlWLTnQYnnR/1yQTG4FcpkRjcJ7ygW41WL6NZJ84eK5Up9DAJpzrZiudIf5zypptarfXX/sqNYDJJMatRTp4dQOm2XXcVy9Y+kxxbfVSxX/xOwNO6W9xSLlfCkxNMbKbrhxn5f8QGgU/Ljoi8qFkDXm7it+0CxBjF11zNW/JZiMevFP44LvdUdy6z/tmKBMFJ4MFq+viHXHTLxO4oF3K/113tiuf1mb2N39M/ou4pFXn/ke5qXtu33FIu7FJj3BHVz/n3Foj5Q9uXeYJT/YaKPHJ7A+aMG+YFi0ZESJREgjGR+qFg6K2T0YEjzfqRYABTMGyn+WLHoL716fFbDo145poI/USzum4yC4B/pTxWLhhertvO9jcm77Z8pFrMJZY9XCz9XLNF/UmTk+S8USzA6f2FrODEUJf1SsShjJPDKgX2ZXymW0HdQ5LX+2fE6/deKRQMPNkW11ahMPfmNYlGQGLA7zxD1/a1iiW5ba7uTq0hJv0v0J5yPKYGBQDN525cm/rdi8X9kc2vzrcHG2mpvuPb6gT2R3yf6m623mOROD58a9MQ/ltEWxR8S/f3FS8VUert39oBnf1Qs7wp7O6JCo/Hwp0R/Yk3Cbl84Opys8p8T/Z2PQyNcHmPKXxSny1eZI/7TXdja1Gulvyb6ffSXoezxTs7fEv1y6SMjkef6vTVV7t8T/SrX48P+O7uiGbfHKCzpH4l+ldHh7d0LE6u/fyb6rQHXjNafpybuEZ8dbu1eSAX/lejfoR0eCaYL3TTr34k+o3xsB63vJP3PiBf+LxnR/yqWpA/7jpZenzvz8MPr/UdPrj306MmHTq6t3ffJR/u9hx48c9/aoycfFRn9kw8+sPZAf/2hh3sP3v/AQ48+sPrIA4+urz1838O9M2v3rz94//8DX4LhofZIBAA="));
com_slime_java_JavaParserStaticEnhanced.__qin_field_SUBHUTI_RULE_NAMES_BY_ID = ["identifier", "qualifiedName", "qualifiedNameList", "typeIdentifier", "arrayInitializer", "floatingPointLiteral", "integerLiteral", "literal", "variableInitializer", "classOrInterfaceType", "primitiveType", "typeArgument", "typeArguments", "typeBound", "typeList", "typeParameter", "typeParameters", "typeType", "typeTypeOrVoid", "additiveExpression", "andExpression", "arrayCreatorDimensions", "assignment", "assignmentExpression", "assignmentOperator", "conditionalAndExpression", "conditionalExpression", "conditionalOrExpression", "equalityExpression", "exclusiveOrExpression", "expression", "inclusiveOrExpression", "lambdaBody", "lambdaExpression", "lambdaParameters", "leftHandSide", "multiplicativeExpression", "postfixExpression", "postfixOp", "prefixOp", "primary", "relationalExpression", "selector", "shiftExpression", "shiftOperator", "switchExpression", "unaryExpression", "block", "blockStatement", "breakStatement", "catchClause", "catchType", "continueStatement", "doWhileStatement", "enhancedForControl", "explicitConstructorInvocationStatement", "expressionList", "expressionStatement", "finallyBlock", "forControl", "forInit", "forStatement", "forUpdate", "ifStatement", "labeledStatement", "localTypeDeclaration", "localVariableDeclaration", "parExpression", "resource", "resourceSpecification", "resources", "returnStatement", "statement", "statementExpression", "switchRule", "switchRuleLabel", "switchRuleLabelList", "switchRuleOutcome", "switchStatement", "synchronizedStatement", "throwStatement", "tryHandlerSuffix", "tryStatement", "whileStatement", "yieldStatement", "annotationTypeDeclaration", "classBody", "classBodyDeclaration", "classDeclaration", "classOrInterfaceModifier", "constDeclaration", "constantDeclarator", "constructorDeclaration", "enumDeclaration", "fieldDeclaration", "genericConstructorDeclaration", "genericInterfaceMethodDeclaration", "genericMethodDeclaration", "interfaceBody", "interfaceBodyDeclaration", "interfaceCommonBodyDeclaration", "interfaceDeclaration", "interfaceMemberDeclaration", "interfaceMethodDeclaration", "interfaceModifier", "memberDeclaration", "methodBody", "methodDeclaration", "modifier", "recordDeclaration", "variableDeclarator", "variableDeclaratorId", "variableDeclarators", "variableModifier", "annotation", "annotationConstantRest", "annotationMethodOrConstantRest", "annotationMethodRest", "annotationTypeBody", "annotationTypeElementDeclaration", "annotationTypeElementRest", "arguments", "classCreatorRest", "compactConstructorDeclaration", "compilationUnit", "defaultValue", "elementValue", "elementValueArrayInitializer", "elementValuePair", "elementValuePairs", "enumBodyDeclarations", "enumConstant", "enumConstants", "explicitGenericInvocation", "explicitGenericInvocationSuffix", "formalParameter", "formalParameterList", "formalParameters", "importDeclaration", "importQualifiedName", "importQualifiedNameSegment", "innerCreator", "lastFormalParameter", "moduleDeclaration", "moduleDirective", "nonWildcardTypeArguments", "nonWildcardTypeArgumentsOrDiamond", "packageDeclaration", "pattern", "receiverParameter", "recordBody", "recordComponent", "recordComponentList", "recordHeader", "requiresModifier", "superSuffix", "switchBlockStatementGroup", "switchLabel", "typeDeclaration"];

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_java_JavaParserStaticEnhanced };
