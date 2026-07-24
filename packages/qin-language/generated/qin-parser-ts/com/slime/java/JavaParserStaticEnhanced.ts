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
  static create(sourceCode: string): any {
    return new com_slime_java_JavaParserStaticEnhanced(sourceCode);
  }
  additiveExpression(): any {
    if ((!this.beginStaticRuleLinked(19.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(19.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  andExpression(): any {
    if ((!this.beginStaticRuleLinked(20.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(20.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  annotation(): any {
    if ((!this.beginStaticRuleLinked(114.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(114.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  annotationConstantRest(): any {
    if ((!this.beginStaticRuleLinked(115.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(115.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  annotationMethodOrConstantRest(): any {
    if ((!this.beginStaticRuleLinked(116.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(116.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  annotationMethodRest(): any {
    if ((!this.beginStaticRuleLinked(117.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(117.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  annotationTypeBody(): any {
    if ((!this.beginStaticRuleLinked(118.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(118.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  annotationTypeDeclaration(): any {
    if ((!this.beginStaticRuleLinked(85.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(85.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  annotationTypeElementDeclaration(): any {
    if ((!this.beginStaticRuleLinked(119.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(119.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  annotationTypeElementRest(): any {
    if ((!this.beginStaticRuleLinked(120.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(120.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  __qin_arguments(): any {
    if ((!this.beginStaticRuleLinked(121.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(121.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  arrayCreatorDimensions(): any {
    if ((!this.beginStaticRuleLinked(21.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(21.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  arrayInitializer(): any {
    if ((!this.beginStaticRuleLinked(4.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(4.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  assignment(): any {
    if ((!this.beginStaticRuleLinked(22.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(22.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  assignmentExpression(): any {
    if ((!this.beginStaticRuleLinked(23.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(23.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  assignmentOperator(): any {
    if ((!this.beginStaticRuleLinked(24.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(24.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  block(): any {
    if ((!this.beginStaticRuleLinked(47.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(47.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  blockStatement(): any {
    if ((!this.beginStaticRuleLinked(48.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(48.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  breakStatement(): any {
    if ((!this.beginStaticRuleLinked(49.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(49.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  catchClause(): any {
    if ((!this.beginStaticRuleLinked(50.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(50.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  catchType(): any {
    if ((!this.beginStaticRuleLinked(51.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(51.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  classBody(): any {
    if ((!this.beginStaticRuleLinked(86.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(86.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  classBodyDeclaration(): any {
    if ((!this.beginStaticRuleLinked(87.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(87.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  classCreatorRest(): any {
    if ((!this.beginStaticRuleLinked(122.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(122.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  classDeclaration(): any {
    if ((!this.beginStaticRuleLinked(88.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(88.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  classOrInterfaceModifier(): any {
    if ((!this.beginStaticRuleLinked(89.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(89.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  classOrInterfaceType(): any {
    if ((!this.beginStaticRuleLinked(9.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(9.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  compactConstructorDeclaration(): any {
    if ((!this.beginStaticRuleLinked(123.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(123.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  compilationUnit(): any {
    if ((!this.beginStaticRuleLinked(124.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(124.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  conditionalAndExpression(): any {
    if ((!this.beginStaticRuleLinked(25.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(25.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  conditionalExpression(): any {
    if ((!this.beginStaticRuleLinked(26.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(26.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  conditionalOrExpression(): any {
    if ((!this.beginStaticRuleLinked(27.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(27.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  constDeclaration(): any {
    if ((!this.beginStaticRuleLinked(90.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(90.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  constantDeclarator(): any {
    if ((!this.beginStaticRuleLinked(91.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(91.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  constructorDeclaration(): any {
    if ((!this.beginStaticRuleLinked(92.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(92.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  continueStatement(): any {
    if ((!this.beginStaticRuleLinked(52.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(52.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  defaultValue(): any {
    if ((!this.beginStaticRuleLinked(125.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(125.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  doWhileStatement(): any {
    if ((!this.beginStaticRuleLinked(53.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(53.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  elementValue(): any {
    if ((!this.beginStaticRuleLinked(126.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(126.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  elementValueArrayInitializer(): any {
    if ((!this.beginStaticRuleLinked(127.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(127.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  elementValuePair(): any {
    if ((!this.beginStaticRuleLinked(128.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(128.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  elementValuePairs(): any {
    if ((!this.beginStaticRuleLinked(129.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(129.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  enhancedForControl(): any {
    if ((!this.beginStaticRuleLinked(54.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(54.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  enumBodyDeclarations(): any {
    if ((!this.beginStaticRuleLinked(130.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(130.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  enumConstant(): any {
    if ((!this.beginStaticRuleLinked(131.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(131.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  enumConstants(): any {
    if ((!this.beginStaticRuleLinked(132.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(132.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  enumDeclaration(): any {
    if ((!this.beginStaticRuleLinked(93.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(93.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  equalityExpression(): any {
    if ((!this.beginStaticRuleLinked(28.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(28.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  exclusiveOrExpression(): any {
    if ((!this.beginStaticRuleLinked(29.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(29.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  explicitConstructorInvocationStatement(): any {
    if ((!this.beginStaticRuleLinked(55.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(55.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  explicitGenericInvocation(): any {
    if ((!this.beginStaticRuleLinked(133.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(133.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  explicitGenericInvocationSuffix(): any {
    if ((!this.beginStaticRuleLinked(134.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(134.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  expression(): any {
    if ((!this.beginStaticRuleLinked(30.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(30.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  expressionList(): any {
    if ((!this.beginStaticRuleLinked(56.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(56.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  expressionStatement(): any {
    if ((!this.beginStaticRuleLinked(57.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(57.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  fieldDeclaration(): any {
    if ((!this.beginStaticRuleLinked(94.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(94.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  finallyBlock(): any {
    if ((!this.beginStaticRuleLinked(58.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(58.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  floatingPointLiteral(): any {
    if ((!this.beginStaticRuleLinked(5.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(5.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  forControl(): any {
    if ((!this.beginStaticRuleLinked(59.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(59.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  forInit(): any {
    if ((!this.beginStaticRuleLinked(60.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(60.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  forStatement(): any {
    if ((!this.beginStaticRuleLinked(61.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(61.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  forUpdate(): any {
    if ((!this.beginStaticRuleLinked(62.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(62.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  formalParameter(): any {
    if ((!this.beginStaticRuleLinked(135.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(135.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  formalParameterList(): any {
    if ((!this.beginStaticRuleLinked(136.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(136.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  formalParameters(): any {
    if ((!this.beginStaticRuleLinked(137.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(137.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  genericConstructorDeclaration(): any {
    if ((!this.beginStaticRuleLinked(95.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(95.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  genericInterfaceMethodDeclaration(): any {
    if ((!this.beginStaticRuleLinked(96.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(96.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  genericMethodDeclaration(): any {
    if ((!this.beginStaticRuleLinked(97.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(97.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  identifier(): any {
    if ((!this.beginStaticRuleLinked(0.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(0.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  ifStatement(): any {
    if ((!this.beginStaticRuleLinked(63.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(63.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  importDeclaration(): any {
    if ((!this.beginStaticRuleLinked(138.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(138.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  importQualifiedName(): any {
    if ((!this.beginStaticRuleLinked(139.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(139.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  importQualifiedNameSegment(): any {
    if ((!this.beginStaticRuleLinked(140.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(140.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  inclusiveOrExpression(): any {
    if ((!this.beginStaticRuleLinked(31.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(31.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  innerCreator(): any {
    if ((!this.beginStaticRuleLinked(141.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(141.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  integerLiteral(): any {
    if ((!this.beginStaticRuleLinked(6.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(6.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  interfaceBody(): any {
    if ((!this.beginStaticRuleLinked(98.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(98.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  interfaceBodyDeclaration(): any {
    if ((!this.beginStaticRuleLinked(99.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(99.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  interfaceCommonBodyDeclaration(): any {
    if ((!this.beginStaticRuleLinked(100.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(100.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  interfaceDeclaration(): any {
    if ((!this.beginStaticRuleLinked(101.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(101.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  interfaceMemberDeclaration(): any {
    if ((!this.beginStaticRuleLinked(102.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(102.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  interfaceMethodDeclaration(): any {
    if ((!this.beginStaticRuleLinked(103.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(103.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  interfaceModifier(): any {
    if ((!this.beginStaticRuleLinked(104.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(104.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  labeledStatement(): any {
    if ((!this.beginStaticRuleLinked(64.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(64.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  lambdaBody(): any {
    if ((!this.beginStaticRuleLinked(32.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(32.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  lambdaExpression(): any {
    if ((!this.beginStaticRuleLinked(33.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(33.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  lambdaParameters(): any {
    if ((!this.beginStaticRuleLinked(34.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(34.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  lastFormalParameter(): any {
    if ((!this.beginStaticRuleLinked(142.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(142.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  leftHandSide(): any {
    if ((!this.beginStaticRuleLinked(35.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(35.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  literal(): any {
    if ((!this.beginStaticRuleLinked(7.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(7.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  localTypeDeclaration(): any {
    if ((!this.beginStaticRuleLinked(65.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(65.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  localVariableDeclaration(): any {
    if ((!this.beginStaticRuleLinked(66.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(66.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  memberDeclaration(): any {
    if ((!this.beginStaticRuleLinked(105.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(105.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  methodBody(): any {
    if ((!this.beginStaticRuleLinked(106.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(106.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  methodDeclaration(): any {
    if ((!this.beginStaticRuleLinked(107.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(107.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  modifier(): any {
    if ((!this.beginStaticRuleLinked(108.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(108.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  moduleDeclaration(): any {
    if ((!this.beginStaticRuleLinked(143.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(143.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  moduleDirective(): any {
    if ((!this.beginStaticRuleLinked(144.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(144.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  multiplicativeExpression(): any {
    if ((!this.beginStaticRuleLinked(36.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(36.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  nonWildcardTypeArguments(): any {
    if ((!this.beginStaticRuleLinked(145.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(145.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  nonWildcardTypeArgumentsOrDiamond(): any {
    if ((!this.beginStaticRuleLinked(146.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(146.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  packageDeclaration(): any {
    if ((!this.beginStaticRuleLinked(147.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(147.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  parExpression(): any {
    if ((!this.beginStaticRuleLinked(67.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(67.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  pattern(): any {
    if ((!this.beginStaticRuleLinked(148.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(148.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  postfixExpression(): any {
    if ((!this.beginStaticRuleLinked(37.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(37.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  postfixOp(): any {
    if ((!this.beginStaticRuleLinked(38.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(38.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  prefixOp(): any {
    if ((!this.beginStaticRuleLinked(39.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(39.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  primary(): any {
    if ((!this.beginStaticRuleLinked(40.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(40.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  primitiveType(): any {
    if ((!this.beginStaticRuleLinked(10.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(10.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  qualifiedName(): any {
    if ((!this.beginStaticRuleLinked(1.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(1.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  qualifiedNameList(): any {
    if ((!this.beginStaticRuleLinked(2.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(2.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  receiverParameter(): any {
    if ((!this.beginStaticRuleLinked(149.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(149.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  recordBody(): any {
    if ((!this.beginStaticRuleLinked(150.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(150.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  recordComponent(): any {
    if ((!this.beginStaticRuleLinked(151.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(151.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  recordComponentList(): any {
    if ((!this.beginStaticRuleLinked(152.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(152.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  recordDeclaration(): any {
    if ((!this.beginStaticRuleLinked(109.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(109.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  recordHeader(): any {
    if ((!this.beginStaticRuleLinked(153.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(153.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  relationalExpression(): any {
    if ((!this.beginStaticRuleLinked(41.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(41.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  requiresModifier(): any {
    if ((!this.beginStaticRuleLinked(154.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(154.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  resource(): any {
    if ((!this.beginStaticRuleLinked(68.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(68.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  resourceSpecification(): any {
    if ((!this.beginStaticRuleLinked(69.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(69.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  resources(): any {
    if ((!this.beginStaticRuleLinked(70.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(70.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  returnStatement(): any {
    if ((!this.beginStaticRuleLinked(71.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(71.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  selector(): any {
    if ((!this.beginStaticRuleLinked(42.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(42.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  shiftExpression(): any {
    if ((!this.beginStaticRuleLinked(43.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(43.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  shiftOperator(): any {
    if ((!this.beginStaticRuleLinked(44.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(44.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  statement(): any {
    if ((!this.beginStaticRuleLinked(72.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(72.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  statementExpression(): any {
    if ((!this.beginStaticRuleLinked(73.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(73.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  superSuffix(): any {
    if ((!this.beginStaticRuleLinked(155.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(155.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  switchBlockStatementGroup(): any {
    if ((!this.beginStaticRuleLinked(156.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(156.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  switchExpression(): any {
    if ((!this.beginStaticRuleLinked(45.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(45.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  switchLabel(): any {
    if ((!this.beginStaticRuleLinked(157.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(157.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  switchRule(): any {
    if ((!this.beginStaticRuleLinked(74.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(74.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  switchRuleLabel(): any {
    if ((!this.beginStaticRuleLinked(75.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(75.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  switchRuleLabelList(): any {
    if ((!this.beginStaticRuleLinked(76.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(76.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  switchRuleOutcome(): any {
    if ((!this.beginStaticRuleLinked(77.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(77.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  switchStatement(): any {
    if ((!this.beginStaticRuleLinked(78.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(78.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  synchronizedStatement(): any {
    if ((!this.beginStaticRuleLinked(79.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(79.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  throwStatement(): any {
    if ((!this.beginStaticRuleLinked(80.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(80.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  tryHandlerSuffix(): any {
    if ((!this.beginStaticRuleLinked(81.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(81.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  tryStatement(): any {
    if ((!this.beginStaticRuleLinked(82.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(82.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  typeArgument(): any {
    if ((!this.beginStaticRuleLinked(11.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(11.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  typeArguments(): any {
    if ((!this.beginStaticRuleLinked(12.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(12.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  typeBound(): any {
    if ((!this.beginStaticRuleLinked(13.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(13.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  typeDeclaration(): any {
    if ((!this.beginStaticRuleLinked(158.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(158.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  typeIdentifier(): any {
    if ((!this.beginStaticRuleLinked(3.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(3.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  typeList(): any {
    if ((!this.beginStaticRuleLinked(14.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(14.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  typeParameter(): any {
    if ((!this.beginStaticRuleLinked(15.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(15.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  typeParameters(): any {
    if ((!this.beginStaticRuleLinked(16.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(16.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  typeType(): any {
    if ((!this.beginStaticRuleLinked(17.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(17.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  typeTypeOrVoid(): any {
    if ((!this.beginStaticRuleLinked(18.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(18.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  unaryExpression(): any {
    if ((!this.beginStaticRuleLinked(46.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(46.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  variableDeclarator(): any {
    if ((!this.beginStaticRuleLinked(110.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(110.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  variableDeclaratorId(): any {
    if ((!this.beginStaticRuleLinked(111.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(111.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  variableDeclarators(): any {
    if ((!this.beginStaticRuleLinked(112.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(112.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  variableInitializer(): any {
    if ((!this.beginStaticRuleLinked(8.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(8.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  variableModifier(): any {
    if ((!this.beginStaticRuleLinked(113.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(113.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  whileStatement(): any {
    if ((!this.beginStaticRuleLinked(83.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(83.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  yieldStatement(): any {
    if ((!this.beginStaticRuleLinked(84.0, ""))) {
      return null;
    }
    try {
      this.executeStaticRuleBodyLinked(84.0);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return null;
  }
  executeStaticGate(ruleId: number, variantId: number, gateId: number): any {
    switch (ruleId) {
      case 9.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentTypeNameDotDoesNotStartClassLiteralSuffix();
            }
            default: {
            }
          }
        }
      }
      case 17.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentTypeTypeHasEmptyArraySuffix();
            }
            default: {
            }
          }
        }
      }
      case 21.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentArrayCreatorHasSizedDimension();
            }
            case 0.0: {
              return this.currentArrayCreatorHasEmptyDimension();
            }
            default: {
            }
          }
        }
      }
      case 23.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentExpressionLooksLikeAssignment();
            }
            case 0.0: {
              return this.currentExpressionLooksLikeConditionalExpression();
            }
            default: {
            }
          }
        }
      }
      case 30.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentExpressionLooksLikeLambda();
            }
            case 0.0: {
              return this.currentExpressionLooksLikeAssignmentExpression();
            }
            default: {
            }
          }
        }
      }
      case 34.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentLambdaParametersLookLikeSingleIdentifier();
            }
            case 1.0: {
              return this.currentLambdaParametersLookLikeFormalList();
            }
            case 2.0: {
              return this.currentLambdaParametersLookLikeInferredList();
            }
            default: {
            }
          }
        }
      }
      case 37.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentPostfixExpressionLooksLikeMethodReference();
            }
            default: {
            }
          }
        }
      }
      case 40.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 5.0: {
              return this.currentPrimaryLooksLikeThisMethodReference();
            }
            case 6.0: {
              return this.currentPrimaryLooksLikeSuperMethodReference();
            }
            case 1.0: {
              return this.currentPrimaryLooksLikePlainThis();
            }
            case 2.0: {
              return this.currentPrimaryLooksLikePlainSuper();
            }
            case 4.0: {
              return this.currentPrimaryLooksLikeTypeMethodReference();
            }
            case 0.0: {
              return this.currentPrimaryLooksLikeTypeClassLiteral();
            }
            case 3.0: {
              return this.currentPrimaryLooksLikeIdentifierPrimary();
            }
            default: {
            }
          }
        }
      }
      case 41.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentRelationalExpressionHasComparisonOperator();
            }
            case 3.0: {
              return this.currentRelationalExpressionHasInstanceof();
            }
            case 1.0: {
              return this.currentInstanceofLooksLikePattern();
            }
            case 2.0: {
              return this.currentInstanceofLooksLikeTypeOnly();
            }
            default: {
            }
          }
        }
      }
      case 42.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentLooksLikeArguments();
            }
            default: {
            }
          }
        }
      }
      case 43.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentShiftExpressionHasShiftOperator();
            }
            default: {
            }
          }
        }
      }
      case 44.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentShiftOperatorLooksLikeLeftShift();
            }
            case 0.0: {
              return this.currentShiftOperatorLooksLikeRightShift();
            }
            default: {
            }
          }
        }
      }
      case 46.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentUnaryExpressionLooksLikePrefix();
            }
            case 2.0: {
              return this.currentUnaryExpressionLooksLikeCast();
            }
            case 0.0: {
              return this.currentUnaryExpressionLooksLikePostfix();
            }
            default: {
            }
          }
        }
      }
      case 48.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentBlockStatementLooksLikeLocalVariableDeclaration();
            }
            case 2.0: {
              return this.currentBlockStatementLooksLikeLocalTypeDeclaration();
            }
            case 1.0: {
              return this.currentBlockStatementLooksLikeStatement();
            }
            default: {
            }
          }
        }
      }
      case 59.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentForControlLooksLikeEnhancedFor();
            }
            case 0.0: {
              return this.currentForControlLooksLikeBasicFor();
            }
            default: {
            }
          }
        }
      }
      case 60.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentForInitLooksLikeLocalVariableDeclaration();
            }
            case 0.0: {
              return this.currentForInitLooksLikeExpressionList();
            }
            default: {
            }
          }
        }
      }
      case 68.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentResourceLooksLikeDeclaration();
            }
            case 0.0: {
              return this.currentResourceLooksLikeIdentifier();
            }
            default: {
            }
          }
        }
      }
      case 72.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 2.0: {
              return this.currentStatementLooksLikeSwitchStatement();
            }
            case 3.0: {
              return this.currentStatementLooksLikeLabeledStatement();
            }
            case 0.0: {
              return this.currentStatementLooksLikeExplicitConstructorInvocation();
            }
            case 1.0: {
              return this.currentStatementLooksLikeExpressionStatement();
            }
            default: {
            }
          }
        }
      }
      case 75.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentSwitchRuleLabelLooksLikePattern();
            }
            case 0.0: {
              return this.currentSwitchRuleLabelLooksLikeExpression();
            }
            default: {
            }
          }
        }
      }
      case 78.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentSwitchEntryLooksLikeBlockStatementGroup();
            }
            case 2.0: {
              return this.currentSwitchEntryLooksLikeRule();
            }
            case 1.0: {
              return this.currentSwitchEntryLooksLikeTrailingLabel();
            }
            default: {
            }
          }
        }
      }
      case 82.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentTryStatementLooksLikeResourceTry();
            }
            case 0.0: {
              return this.currentTryStatementLooksLikePlainTry();
            }
            default: {
            }
          }
        }
      }
      case 87.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentClassBodyDeclarationLooksLikeEmpty();
            }
            case 1.0: {
              return this.currentClassBodyDeclarationLooksLikeInitializerBlock();
            }
            case 2.0: {
              return this.currentClassBodyDeclarationLooksLikeMember();
            }
            default: {
            }
          }
        }
      }
      case 89.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentClassOrInterfaceModifierLooksLikeAnnotation();
            }
            default: {
            }
          }
        }
      }
      case 102.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 7.0: {
              return this.interfaceMemberDeclarationLooksLikeRecord();
            }
            case 1.0: {
              return this.interfaceMemberDeclarationLooksLikeConst();
            }
            case 3.0: {
              return this.interfaceMemberDeclarationLooksLikeGenericMethod();
            }
            case 4.0: {
              return this.interfaceMemberDeclarationLooksLikeMethod();
            }
            case 5.0: {
              return this.interfaceMemberDeclarationLooksLikeInterface();
            }
            case 6.0: {
              return this.interfaceMemberDeclarationLooksLikeAnnotationType();
            }
            case 0.0: {
              return this.interfaceMemberDeclarationLooksLikeClass();
            }
            case 2.0: {
              return this.interfaceMemberDeclarationLooksLikeEnum();
            }
            default: {
            }
          }
        }
      }
      case 105.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 8.0: {
              return this.memberDeclarationLooksLikeRecord();
            }
            case 0.0: {
              return this.memberDeclarationLooksLikeGenericConstructor();
            }
            case 2.0: {
              return this.memberDeclarationLooksLikeConstructor();
            }
            case 4.0: {
              return this.memberDeclarationLooksLikeGenericMethod();
            }
            case 5.0: {
              return this.memberDeclarationLooksLikeMethod();
            }
            case 6.0: {
              return this.memberDeclarationLooksLikeField();
            }
            case 7.0: {
              return this.memberDeclarationLooksLikeInterface();
            }
            case 9.0: {
              return this.memberDeclarationLooksLikeAnnotationType();
            }
            case 1.0: {
              return this.memberDeclarationLooksLikeClass();
            }
            case 3.0: {
              return this.memberDeclarationLooksLikeEnum();
            }
            default: {
            }
          }
        }
      }
      case 114.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentAnnotationValueLooksLikePairs();
            }
            case 0.0: {
              return this.currentAnnotationValueLooksLikeSingleValue();
            }
            default: {
            }
          }
        }
      }
      case 116.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentAnnotationMethodOrConstantRestLooksLikeMethod();
            }
            case 0.0: {
              return this.currentAnnotationMethodOrConstantRestLooksLikeConstant();
            }
            default: {
            }
          }
        }
      }
      case 126.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentElementValueLooksLikeAnnotation();
            }
            case 2.0: {
              return this.currentElementValueLooksLikeArrayInitializer();
            }
            case 1.0: {
              return this.currentElementValueLooksLikeExpression();
            }
            default: {
            }
          }
        }
      }
      case 136.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentParameterIsNotVarargs();
            }
            case 2.0: {
              return this.commaFollowedByNonVarargsParameter();
            }
            case 1.0: {
              return this.commaFollowedByVarargsParameter();
            }
            case 3.0: {
              return this.currentParameterIsVarargs();
            }
            default: {
            }
          }
        }
      }
      case 137.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentFormalParametersStartFormalList();
            }
            case 0.0: {
              return this.currentFormalParametersStartReceiver();
            }
            default: {
            }
          }
        }
      }
      case 139.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentImportQualifiedNameContinuesWithIdentifierSegment();
            }
            default: {
            }
          }
        }
      }
      case 140.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return (!this.match("DOT") || this.lookahead("MUL", 2.0));
            }
            default: {
            }
          }
        }
      }
      case 150.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 1.0: {
              return this.currentRecordBodyLooksLikeCompactConstructor();
            }
            case 0.0: {
              return this.currentRecordBodyLooksLikeClassBodyDeclaration();
            }
            default: {
            }
          }
        }
      }
      case 158.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          switch (gateId) {
            case 0.0: {
              return this.currentTypeDeclarationLooksLikeClassOrInterfaceModifier();
            }
            default: {
            }
          }
        }
      }
      default: {
      }
    }
    return super.executeStaticGate(ruleId, variantId, gateId);
  }
  executeStaticSubrule(ruleId: number, variantId: number, invocationArgument: any): any {
    switch (ruleId) {
      case 0.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.identifier();
          return null;
        }
      }
      case 1.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.qualifiedName();
          return null;
        }
      }
      case 2.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.qualifiedNameList();
          return null;
        }
      }
      case 3.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.typeIdentifier();
          return null;
        }
      }
      case 4.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.arrayInitializer();
          return null;
        }
      }
      case 5.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.floatingPointLiteral();
          return null;
        }
      }
      case 6.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.integerLiteral();
          return null;
        }
      }
      case 7.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.literal();
          return null;
        }
      }
      case 8.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.variableInitializer();
          return null;
        }
      }
      case 9.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.classOrInterfaceType();
          return null;
        }
      }
      case 10.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.primitiveType();
          return null;
        }
      }
      case 11.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.typeArgument();
          return null;
        }
      }
      case 12.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.typeArguments();
          return null;
        }
      }
      case 13.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.typeBound();
          return null;
        }
      }
      case 14.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.typeList();
          return null;
        }
      }
      case 15.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.typeParameter();
          return null;
        }
      }
      case 16.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.typeParameters();
          return null;
        }
      }
      case 17.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.typeType();
          return null;
        }
      }
      case 18.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.typeTypeOrVoid();
          return null;
        }
      }
      case 19.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.additiveExpression();
          return null;
        }
      }
      case 20.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.andExpression();
          return null;
        }
      }
      case 21.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.arrayCreatorDimensions();
          return null;
        }
      }
      case 22.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.assignment();
          return null;
        }
      }
      case 23.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.assignmentExpression();
          return null;
        }
      }
      case 24.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.assignmentOperator();
          return null;
        }
      }
      case 25.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.conditionalAndExpression();
          return null;
        }
      }
      case 26.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.conditionalExpression();
          return null;
        }
      }
      case 27.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.conditionalOrExpression();
          return null;
        }
      }
      case 28.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.equalityExpression();
          return null;
        }
      }
      case 29.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.exclusiveOrExpression();
          return null;
        }
      }
      case 30.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.expression();
          return null;
        }
      }
      case 31.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.inclusiveOrExpression();
          return null;
        }
      }
      case 32.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.lambdaBody();
          return null;
        }
      }
      case 33.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.lambdaExpression();
          return null;
        }
      }
      case 34.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.lambdaParameters();
          return null;
        }
      }
      case 35.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.leftHandSide();
          return null;
        }
      }
      case 36.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.multiplicativeExpression();
          return null;
        }
      }
      case 37.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.postfixExpression();
          return null;
        }
      }
      case 38.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.postfixOp();
          return null;
        }
      }
      case 39.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.prefixOp();
          return null;
        }
      }
      case 40.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.primary();
          return null;
        }
      }
      case 41.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.relationalExpression();
          return null;
        }
      }
      case 42.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.selector();
          return null;
        }
      }
      case 43.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.shiftExpression();
          return null;
        }
      }
      case 44.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.shiftOperator();
          return null;
        }
      }
      case 45.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.switchExpression();
          return null;
        }
      }
      case 46.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.unaryExpression();
          return null;
        }
      }
      case 47.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.block();
          return null;
        }
      }
      case 48.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.blockStatement();
          return null;
        }
      }
      case 49.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.breakStatement();
          return null;
        }
      }
      case 50.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.catchClause();
          return null;
        }
      }
      case 51.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.catchType();
          return null;
        }
      }
      case 52.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.continueStatement();
          return null;
        }
      }
      case 53.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.doWhileStatement();
          return null;
        }
      }
      case 54.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.enhancedForControl();
          return null;
        }
      }
      case 55.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.explicitConstructorInvocationStatement();
          return null;
        }
      }
      case 56.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.expressionList();
          return null;
        }
      }
      case 57.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.expressionStatement();
          return null;
        }
      }
      case 58.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.finallyBlock();
          return null;
        }
      }
      case 59.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.forControl();
          return null;
        }
      }
      case 60.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.forInit();
          return null;
        }
      }
      case 61.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.forStatement();
          return null;
        }
      }
      case 62.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.forUpdate();
          return null;
        }
      }
      case 63.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.ifStatement();
          return null;
        }
      }
      case 64.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.labeledStatement();
          return null;
        }
      }
      case 65.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.localTypeDeclaration();
          return null;
        }
      }
      case 66.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.localVariableDeclaration();
          return null;
        }
      }
      case 67.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.parExpression();
          return null;
        }
      }
      case 68.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.resource();
          return null;
        }
      }
      case 69.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.resourceSpecification();
          return null;
        }
      }
      case 70.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.resources();
          return null;
        }
      }
      case 71.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.returnStatement();
          return null;
        }
      }
      case 72.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.statement();
          return null;
        }
      }
      case 73.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.statementExpression();
          return null;
        }
      }
      case 74.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.switchRule();
          return null;
        }
      }
      case 75.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.switchRuleLabel();
          return null;
        }
      }
      case 76.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.switchRuleLabelList();
          return null;
        }
      }
      case 77.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.switchRuleOutcome();
          return null;
        }
      }
      case 78.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.switchStatement();
          return null;
        }
      }
      case 79.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.synchronizedStatement();
          return null;
        }
      }
      case 80.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.throwStatement();
          return null;
        }
      }
      case 81.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.tryHandlerSuffix();
          return null;
        }
      }
      case 82.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.tryStatement();
          return null;
        }
      }
      case 83.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.whileStatement();
          return null;
        }
      }
      case 84.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.yieldStatement();
          return null;
        }
      }
      case 85.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.annotationTypeDeclaration();
          return null;
        }
      }
      case 86.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.classBody();
          return null;
        }
      }
      case 87.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.classBodyDeclaration();
          return null;
        }
      }
      case 88.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.classDeclaration();
          return null;
        }
      }
      case 89.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.classOrInterfaceModifier();
          return null;
        }
      }
      case 90.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.constDeclaration();
          return null;
        }
      }
      case 91.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.constantDeclarator();
          return null;
        }
      }
      case 92.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.constructorDeclaration();
          return null;
        }
      }
      case 93.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.enumDeclaration();
          return null;
        }
      }
      case 94.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.fieldDeclaration();
          return null;
        }
      }
      case 95.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.genericConstructorDeclaration();
          return null;
        }
      }
      case 96.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.genericInterfaceMethodDeclaration();
          return null;
        }
      }
      case 97.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.genericMethodDeclaration();
          return null;
        }
      }
      case 98.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.interfaceBody();
          return null;
        }
      }
      case 99.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.interfaceBodyDeclaration();
          return null;
        }
      }
      case 100.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.interfaceCommonBodyDeclaration();
          return null;
        }
      }
      case 101.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.interfaceDeclaration();
          return null;
        }
      }
      case 102.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.interfaceMemberDeclaration();
          return null;
        }
      }
      case 103.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.interfaceMethodDeclaration();
          return null;
        }
      }
      case 104.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.interfaceModifier();
          return null;
        }
      }
      case 105.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.memberDeclaration();
          return null;
        }
      }
      case 106.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.methodBody();
          return null;
        }
      }
      case 107.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.methodDeclaration();
          return null;
        }
      }
      case 108.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.modifier();
          return null;
        }
      }
      case 109.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.recordDeclaration();
          return null;
        }
      }
      case 110.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.variableDeclarator();
          return null;
        }
      }
      case 111.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.variableDeclaratorId();
          return null;
        }
      }
      case 112.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.variableDeclarators();
          return null;
        }
      }
      case 113.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.variableModifier();
          return null;
        }
      }
      case 114.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.annotation();
          return null;
        }
      }
      case 115.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.annotationConstantRest();
          return null;
        }
      }
      case 116.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.annotationMethodOrConstantRest();
          return null;
        }
      }
      case 117.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.annotationMethodRest();
          return null;
        }
      }
      case 118.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.annotationTypeBody();
          return null;
        }
      }
      case 119.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.annotationTypeElementDeclaration();
          return null;
        }
      }
      case 120.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.annotationTypeElementRest();
          return null;
        }
      }
      case 121.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.__qin_arguments();
          return null;
        }
      }
      case 122.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.classCreatorRest();
          return null;
        }
      }
      case 123.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.compactConstructorDeclaration();
          return null;
        }
      }
      case 124.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.compilationUnit();
          return null;
        }
      }
      case 125.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.defaultValue();
          return null;
        }
      }
      case 126.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.elementValue();
          return null;
        }
      }
      case 127.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.elementValueArrayInitializer();
          return null;
        }
      }
      case 128.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.elementValuePair();
          return null;
        }
      }
      case 129.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.elementValuePairs();
          return null;
        }
      }
      case 130.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.enumBodyDeclarations();
          return null;
        }
      }
      case 131.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.enumConstant();
          return null;
        }
      }
      case 132.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.enumConstants();
          return null;
        }
      }
      case 133.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.explicitGenericInvocation();
          return null;
        }
      }
      case 134.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.explicitGenericInvocationSuffix();
          return null;
        }
      }
      case 135.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.formalParameter();
          return null;
        }
      }
      case 136.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.formalParameterList();
          return null;
        }
      }
      case 137.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.formalParameters();
          return null;
        }
      }
      case 138.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.importDeclaration();
          return null;
        }
      }
      case 139.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.importQualifiedName();
          return null;
        }
      }
      case 140.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.importQualifiedNameSegment();
          return null;
        }
      }
      case 141.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.innerCreator();
          return null;
        }
      }
      case 142.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.lastFormalParameter();
          return null;
        }
      }
      case 143.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.moduleDeclaration();
          return null;
        }
      }
      case 144.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.moduleDirective();
          return null;
        }
      }
      case 145.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.nonWildcardTypeArguments();
          return null;
        }
      }
      case 146.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.nonWildcardTypeArgumentsOrDiamond();
          return null;
        }
      }
      case 147.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.packageDeclaration();
          return null;
        }
      }
      case 148.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.pattern();
          return null;
        }
      }
      case 149.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.receiverParameter();
          return null;
        }
      }
      case 150.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.recordBody();
          return null;
        }
      }
      case 151.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.recordComponent();
          return null;
        }
      }
      case 152.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.recordComponentList();
          return null;
        }
      }
      case 153.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.recordHeader();
          return null;
        }
      }
      case 154.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.requiresModifier();
          return null;
        }
      }
      case 155.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.superSuffix();
          return null;
        }
      }
      case 156.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.switchBlockStatementGroup();
          return null;
        }
      }
      case 157.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.switchLabel();
          return null;
        }
      }
      case 158.0: {
        if (__qin_binary__("==", variantId, 0.0)) {
          this.typeDeclaration();
          return null;
        }
      }
      default: {
      }
    }
    super.executeStaticSubrule(ruleId, variantId, invocationArgument);
    return null;
  }
  static subhutiStaticRuleVariants(): any {
    let variants: any = new __QinJavaUtilArrayList(159.0);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_0(variants);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_1(variants);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_2(variants);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_3(variants);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_4(variants);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_5(variants);
    com_slime_java_JavaParserStaticEnhanced.subhutiStaticRuleVariants_6(variants);
    return __QinJavaUtilList.copyOf(variants);
  }
  static subhutiStaticRuleVariants_0(variants: any): any {
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
  static subhutiStaticRuleVariants_1(variants: any): any {
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
  static subhutiStaticRuleVariants_2(variants: any): any {
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
  static subhutiStaticRuleVariants_3(variants: any): any {
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
  static subhutiStaticRuleVariants_4(variants: any): any {
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
  static subhutiStaticRuleVariants_5(variants: any): any {
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
  static subhutiStaticRuleVariants_6(variants: any): any {
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
  staticGrammarPlan(): any {
    return com_slime_java_JavaParserStaticEnhanced.__qin_field_SUBHUTI_STATIC_GRAMMAR_PLAN;
  }
  staticRuleNamesById(): any {
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
