import { com_subhuti_lookahead_SubhutiTokenConsumer, com_subhuti_lookahead_SubhutiTokenConsumer as SubhutiTokenConsumer } from "../lookahead/SubhutiTokenConsumer.ts";
import { com_subhuti_lookahead_SubhutiTokenMatchParser, com_subhuti_lookahead_SubhutiTokenMatchParser as SubhutiTokenMatchParser } from "../lookahead/SubhutiTokenMatchParser.ts";
import { com_subhuti_lexer_TokenCacheEntry, com_subhuti_lexer_TokenCacheEntry as TokenCacheEntry } from "../lexer/TokenCacheEntry.ts";
import { com_subhuti_cache_SubhutiPackratCacheResult, com_subhuti_cache_SubhutiPackratCacheResult as SubhutiPackratCacheResult } from "../cache/SubhutiPackratCacheResult.ts";
import { com_subhuti_debug_SubhutiTraceDebugger, com_subhuti_debug_SubhutiTraceDebugger as SubhutiTraceDebugger } from "../debug/SubhutiTraceDebugger.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations as ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException as SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException as SubhutiFirstTokenUnknownException } from "./SubhutiParserState.ts";
import { com_subhuti_struct_LexerMode, com_subhuti_struct_LexerMode as LexerMode } from "../struct/LexerMode.ts";
import { com_subhuti_struct_SubhutiCst, com_subhuti_struct_SubhutiCst as SubhutiCst, com_subhuti_struct_SubhutiCst$Builder } from "../struct/SubhutiCst.ts";
import { com_subhuti_parser_SubhutiGastGrammar, com_subhuti_parser_SubhutiGastGrammar as SubhutiGastGrammar, com_subhuti_parser_SubhutiGastGrammar$AlternationDefinition, com_subhuti_parser_SubhutiGastGrammar$AlternationDefinition as AlternationDefinition, com_subhuti_parser_SubhutiGastGrammar$RuleVariantKey, com_subhuti_parser_SubhutiGastGrammar$RuleVariantDefinition, com_subhuti_parser_SubhutiGastGrammar$RuleVariantDefinition as RuleVariantDefinition } from "./SubhutiGastGrammar.ts";
import { com_subhuti_parser_SubhutiStaticGrammarPlan, com_subhuti_parser_SubhutiStaticGrammarPlan as SubhutiStaticGrammarPlan, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionAvailability, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionAvailability as DecisionAvailability, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDispatchBlocker, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDispatchBlocker as StaticDispatchBlocker, com_subhuti_parser_SubhutiStaticGrammarPlan$DynamicCandidateReason, com_subhuti_parser_SubhutiStaticGrammarPlan$DynamicCandidateReason as DynamicCandidateReason, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadKind, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadKind as CompiledLookaheadKind, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkNodeDefinition as CompiledLlkNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkEdgeDefinition as CompiledLlkEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadDefinition as CompiledLookaheadDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$BranchDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$BranchDefinition as BranchDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixDefinition as SharedPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupDefinition as CandidateGroupDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleTokenDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleTokenDefinition as CrossRuleTokenDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleFrontierDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleFrontierDefinition as CrossRuleFrontierDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphNodeDefinition as AdaptiveGraphNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphDefinition as AdaptiveGraphDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionKind, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionKind as FiniteInstructionKind, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteTokenClassDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteTokenClassDefinition as FiniteTokenClassDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteMatchEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteMatchEdgeDefinition as FiniteMatchEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteRuleReferenceDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteRuleReferenceDefinition as FiniteRuleReferenceDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteFrameDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteFrameDefinition as FiniteFrameDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteGateDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteGateDefinition as FiniteGateDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteCallPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteCallPrefixDefinition as FiniteCallPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionDefinition as FiniteInstructionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteDecisionProgramDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteDecisionProgramDefinition as FiniteDecisionProgramDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteProgramAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteProgramAddress as FiniteProgramAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixAddress as SharedPrefixAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupAddress as CandidateGroupAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleAddress as CrossRuleAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionDefinition as DecisionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AnalysisDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AnalysisDefinition as AnalysisDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$InvocationDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$InvocationDefinition as InvocationDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CoverageDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CoverageDefinition as CoverageDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticMetadata, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticMetadata as StaticMetadata, com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence, com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence as Occurrence, com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant, com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant as RuleVariant, com_subhuti_parser_SubhutiStaticGrammarPlan$VariantRecursionAnalysis, com_subhuti_parser_SubhutiStaticGrammarPlan$VariantRecursionAnalysis as VariantRecursionAnalysis, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan as StaticRuleInvocationPlan, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlans, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlans as StaticRuleInvocationPlans, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDecisionPlans, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDecisionPlans as StaticDecisionPlans } from "./SubhutiStaticGrammarPlan.ts";
import { com_subhuti_struct_SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken as SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken$Builder } from "../struct/SubhutiMatchToken.ts";
import { com_subhuti_parser_SubhutiDecisionPlan, com_subhuti_parser_SubhutiDecisionPlan as SubhutiDecisionPlan, com_subhuti_parser_SubhutiDecisionPlan$Availability, com_subhuti_parser_SubhutiDecisionPlan$CandidateGroup, com_subhuti_parser_SubhutiDecisionPlan$Branch } from "./SubhutiDecisionPlan.ts";
import { com_subhuti_parser_SubhutiParserRuntimePlan, com_subhuti_parser_SubhutiParserRuntimePlan as SubhutiParserRuntimePlan, com_subhuti_parser_SubhutiParserRuntimePlan$GastRecognizerAnalysis, com_subhuti_parser_SubhutiParserRuntimePlan$GastRecognizerAnalysis as GastRecognizerAnalysis, com_subhuti_parser_SubhutiParserRuntimePlan$TarjanRuleCycles, com_subhuti_parser_SubhutiParserRuntimePlan$TarjanRuleCycles as TarjanRuleCycles, com_subhuti_parser_SubhutiParserRuntimePlan$Access, com_subhuti_parser_SubhutiParserRuntimePlan$Access as Access, com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal, com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal as DirectTerminal, com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminalSequence, com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminalSequence as DirectTerminalSequence, com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerElement, com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerElement as DirectRecognizerElement, com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerElement$Kind, com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerPlan, com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerPlan as DirectRecognizerPlan, com_subhuti_parser_SubhutiParserRuntimePlan$CoverageReport, com_subhuti_parser_SubhutiParserRuntimePlan$CoverageReport as CoverageReport } from "./SubhutiParserRuntimePlan.ts";
import { com_subhuti_struct_SubhutiSourceLocation, com_subhuti_struct_SubhutiSourceLocation as SubhutiSourceLocation, com_subhuti_struct_SubhutiSourceLocation$Builder } from "../struct/SubhutiSourceLocation.ts";
import { com_subhuti_struct_SubhutiPosition, com_subhuti_struct_SubhutiPosition as SubhutiPosition } from "../struct/SubhutiPosition.ts";
import { com_subhuti_parser_ParseRecordNode, com_subhuti_parser_ParseRecordNode as ParseRecordNode } from "./ParseRecordNode.ts";
import { com_subhuti_parser_SubhutiTokenPrediction, com_subhuti_parser_SubhutiTokenPrediction as SubhutiTokenPrediction } from "./SubhutiTokenPrediction.ts";
import { com_subhuti_parser_SubhutiRuleCacheKey, com_subhuti_parser_SubhutiRuleCacheKey as SubhutiRuleCacheKey } from "./SubhutiRuleCacheKey.ts";
import { com_subhuti_parser_SubhutiBackData, com_subhuti_parser_SubhutiBackData as SubhutiBackData } from "./SubhutiBackData.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../lookahead/SubhutiTokenLookahead.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __QinJavaLangStringBuilder, __QinJavaLangInteger, __QinJavaLangLong, __QinJavaLangEnum, __qin_java_string_hash_code__, __qin_java_identity_hash_code__, __qin_java_value_hash_code__, __qin_java_values_equal__, __qin_java_hash_key__, __qin_java_hash_key_equals__, __QinJavaUtilArrayList, __QinJavaUtilUnmodifiableList, __qin_java_functional, __QinJavaUtilObjects, __QinJavaUtilArrays, __QinJavaUtilSet, __QinJavaUtilUnmodifiableSet, __QinJavaUtilHashMap, __QinJavaUtilUnmodifiableMap, __QinJavaUtilList, __QinJavaUtilStreamCollectors, __QinJavaUtilHashSet, __qin_init_enum_value } from "@qin/java-sdk-js";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const IllegalArgumentException = __QinJavaLangIllegalArgumentException;
const RuntimeException = __QinJavaLangRuntimeException;
const IllegalStateException = __QinJavaLangIllegalStateException;
const StringBuilder = __QinJavaLangStringBuilder;
const Integer = __QinJavaLangInteger;
const Exception = __QinJavaLangException;
const Long = __QinJavaLangLong;
const java_lang_Enum = __QinJavaLangEnum;
class com_subhuti_parser_SubhutiParserCore extends com_subhuti_parser_SubhutiParserState {
  static __qin_field_ADAPTIVE_LOW_YIELD_MEMO_MIN_PUTS: number | null = null as any;
  __qin_field_recognizerPassThroughRules: any = null as any;
  __qin_field_recognizerTerminalLeafRules: any = null as any;
  __qin_field_sourceLookaheadEntries: com_subhuti_lexer_TokenCacheEntry[] | null = null as any;
  __qin_field_sourceLookaheadSize: number | null = null as any;
  __qin_field_sourceLookaheadStartIndex: number | null = null as any;
  __qin_field_sourceLookaheadStartLine: number | null = null as any;
  __qin_field_sourceLookaheadStartColumn: number | null = null as any;
  __qin_field_sourceLookaheadMode: com_subhuti_struct_LexerMode | null = null as any;
  __qin_field_sourceLookaheadInitialized: boolean | null = null as any;
  __qin_field_sourceLookaheadTerminalReached: boolean | null = null as any;
  __qin_field_sourceLookaheadTerminalEntry: com_subhuti_lexer_TokenCacheEntry | null = null as any;
  __qin_field_activeStaticRuleIds: number[] | null = null as any;
  __qin_field_activeStaticVariantIds: number[] | null = null as any;
  __qin_field_activeStaticInvocationIds: number[] | null = null as any;
  __qin_field_activeStaticCursorStamps: number[] | null = null as any;
  __qin_field_activeStaticPreviousSameInvocation: number[] | null = null as any;
  __qin_field_activeStaticArguments: any[] | null = null as any;
  __qin_field_activeStaticModes: com_subhuti_struct_LexerMode[] | null = null as any;
  __qin_field_activeStaticLastTokenNames: string[] | null = null as any;
  __qin_field_activeStaticStartCursorStamps: number[] | null = null as any;
  __qin_field_activeStaticDebugStartTimes: number[] | null = null as any;
  __qin_field_activeStaticRootEntries: boolean[] | null = null as any;
  __qin_field_activeStaticTransparentCstFrames: boolean[] | null = null as any;
  __qin_field_activeStaticSourceBodyFrames: boolean[] | null = null as any;
  __qin_field_activeStaticRuleCsts: com_subhuti_struct_SubhutiCst[] | null = null as any;
  __qin_field_activeStaticInvocationPlans: com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan[] | null = null as any;
  __qin_field_activeStaticInvocationHeads: number[] | null = null as any;
  __qin_field_activeStaticRuleDepth: number | null = null as any;
  __qin_field_staticSourceBodyExecutionDepth: number | null = null as any;
  __qin_field_staticSourceBodyRuleIds: number[] | null = null as any;
  __qin_field_staticSourceBodyVariantIds: number[] | null = null as any;
  __qin_field_staticSourceBodyPassThrough: boolean[] | null = null as any;
  __qin_field_activeStaticFiniteProviderActionResult: any = null as any;
  __qin_field_activeStaticFiniteProviderActionResultReady: boolean | null = null as any;
  __qin_field_furthestStaticFailureIndex: number | null = null as any;
  __qin_field_furthestStaticFailureRules: string | null = null as any;
  __qin_field_furthestStaticFailureToken: string | null = null as any;
  __qin_field_firstStaticFailureIndex: number | null = null as any;
  __qin_field_firstStaticFailureRules: string | null = null as any;
  __qin_field_firstStaticFailureToken: string | null = null as any;
  __qin_field_staticDebugHooks: com_subhuti_parser_SubhutiParserCore$StaticDebugHooks | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] === "function" || (typeof __qin_args[1] === "object" && typeof __qin_args[1].getName === "function" && typeof __qin_args[1].isAssignableFrom === "function")) && (__qin_args[2] === null || Array.isArray(__qin_args[2]) || __qin_args[2] instanceof __QinJavaUtilArrayList || __qin_args[2] instanceof __QinJavaUtilUnmodifiableList)) {
      const sourceCode: any = __qin_args[0];
      const tokenConsumerClass: any = __qin_args[1];
      const tokens: any = __qin_args[2];
      super(sourceCode, tokenConsumerClass, tokens);
      this.__qin_constructor_com_subhuti_parser_SubhutiParserCore_3_0(sourceCode, tokenConsumerClass, tokens);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParserCore/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParserCore_3_0(sourceCode: string, tokenConsumerClass: any, tokens: any): void {
    this.__qin_field_recognizerPassThroughRules = null;
    this.__qin_field_recognizerTerminalLeafRules = null;
    this.__qin_field_sourceLookaheadEntries = [];
    this.__qin_field_sourceLookaheadSize = null;
    this.__qin_field_sourceLookaheadStartIndex = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_sourceLookaheadStartLine = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_sourceLookaheadStartColumn = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_sourceLookaheadMode = null;
    this.__qin_field_sourceLookaheadInitialized = null;
    this.__qin_field_sourceLookaheadTerminalReached = null;
    this.__qin_field_sourceLookaheadTerminalEntry = null;
    this.__qin_field_activeStaticRuleIds = [];
    this.__qin_field_activeStaticVariantIds = [];
    this.__qin_field_activeStaticInvocationIds = [];
    this.__qin_field_activeStaticCursorStamps = [];
    this.__qin_field_activeStaticPreviousSameInvocation = [];
    this.__qin_field_activeStaticArguments = [];
    this.__qin_field_activeStaticModes = [];
    this.__qin_field_activeStaticLastTokenNames = [];
    this.__qin_field_activeStaticStartCursorStamps = [];
    this.__qin_field_activeStaticDebugStartTimes = [];
    this.__qin_field_activeStaticRootEntries = [];
    this.__qin_field_activeStaticTransparentCstFrames = [];
    this.__qin_field_activeStaticSourceBodyFrames = [];
    this.__qin_field_activeStaticRuleCsts = [];
    this.__qin_field_activeStaticInvocationPlans = [];
    this.__qin_field_activeStaticInvocationHeads = [];
    this.__qin_field_activeStaticRuleDepth = null;
    this.__qin_field_staticSourceBodyExecutionDepth = null;
    this.__qin_field_staticSourceBodyRuleIds = [];
    this.__qin_field_staticSourceBodyVariantIds = [];
    this.__qin_field_staticSourceBodyPassThrough = [];
    this.__qin_field_activeStaticFiniteProviderActionResult = null;
    this.__qin_field_activeStaticFiniteProviderActionResultReady = null;
    this.__qin_field_furthestStaticFailureIndex = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_furthestStaticFailureRules = "";
    this.__qin_field_furthestStaticFailureToken = "";
    this.__qin_field_firstStaticFailureIndex = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_firstStaticFailureRules = "";
    this.__qin_field_firstStaticFailureToken = "";
    this.__qin_field_staticDebugHooks = com_subhuti_parser_SubhutiParserCore$StaticDebugHooks.__qin_field_NO_OP;
    null;
  }
  gastGrammar(): any {
    return null;
  }
  staticRuleNamesById(): any {
    return null;
  }
  staticGrammarPlan(): any {
    return null;
  }
  effectiveGastGrammar(): any {
    let staticPlan: any = this.staticGrammarPlan();
    if (__qin_binary__("!=", staticPlan, null)) {
      return (staticPlan.hasGeneratedMetadata() ? null : staticPlan.gastGrammar());
    }
    return this.gastGrammar();
  }
  hasStaticGrammarPlan(): any {
    return __qin_binary__("!=", this.staticGrammarPlan(), null);
  }
  executeStaticRule(ruleId: number, variantId: number, invocationArgument: any, sourceBody: any): any {
    const __qin_functional_sourceBody_3 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_3, null)) {
      throw new __QinJavaLangIllegalArgumentException("static rule body cannot be null");
    }
    if ((!this.beginStaticRule(ruleId, variantId, invocationArgument))) {
      return null;
    }
    let result: any = null;
    try {
      result = __qin_functional_sourceBody_3.get();
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      this.abortStaticRule();
      throw exception;
    }
    this.completeStaticRule();
    return result;
  }
  executeStaticVoidRule(ruleId: number, variantId: number, invocationArgument: any, sourceBody: any): any {
    const __qin_functional_sourceBody_3 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_3, null)) {
      throw new __QinJavaLangIllegalArgumentException("static rule body cannot be null");
    }
    this.executeStaticRule(ruleId, variantId, invocationArgument, __qin_java_functional(() => {
      __qin_functional_sourceBody_3.run();
      return null;
    }));
    return null;
  }
  isExecutingStaticSourceBody(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_isExecutingStaticSourceBody_0_0();
    if (__qin_args.length === 2 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number") return this.__qin_overload_isExecutingStaticSourceBody_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: isExecutingStaticSourceBody/" + __qin_args.length);
  }
  __qin_overload_isExecutingStaticSourceBody_0_0(): any {
    return __qin_binary__(">", this.__qin_field_staticSourceBodyExecutionDepth, 0.0);
  }
  __qin_overload_isExecutingStaticSourceBody_2_1(ruleId: number, variantId: number): any {
    for (let index: any = __qin_binary__("-", this.__qin_field_staticSourceBodyExecutionDepth, 1.0); __qin_binary__(">=", index, 0.0); index--) {
      if ((__qin_binary__("==", this.__qin_field_staticSourceBodyRuleIds[index], ruleId) && __qin_binary__("==", this.__qin_field_staticSourceBodyVariantIds[index], variantId))) {
        return true;
      }
    }
    return false;
  }
  isExecutingStaticSourceBodyPassThrough(ruleId: number, variantId: number): any {
    for (let index: any = __qin_binary__("-", this.__qin_field_staticSourceBodyExecutionDepth, 1.0); __qin_binary__(">=", index, 0.0); index--) {
      if ((__qin_binary__("==", this.__qin_field_staticSourceBodyRuleIds[index], ruleId) && __qin_binary__("==", this.__qin_field_staticSourceBodyVariantIds[index], variantId) && this.__qin_field_staticSourceBodyPassThrough[index])) {
        return true;
      }
    }
    return false;
  }
  executeStaticSourceBody(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "function" || __qin_args[0].__qinJavaFunctional === true)) return this.__qin_overload_executeStaticSourceBody_1_1(__qin_args[0]);
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && (__qin_args[2] === null || typeof __qin_args[2] === "function" || __qin_args[2].__qinJavaFunctional === true)) return this.__qin_overload_executeStaticSourceBody_3_3(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "function" || __qin_args[0].__qinJavaFunctional === true)) return this.__qin_overload_executeStaticSourceBody_1_0(__qin_args[0]);
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && (__qin_args[2] === null || typeof __qin_args[2] === "function" || __qin_args[2].__qinJavaFunctional === true)) return this.__qin_overload_executeStaticSourceBody_3_2(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: executeStaticSourceBody/" + __qin_args.length);
  }
  __qin_overload_executeStaticSourceBody_1_0(sourceBody: any): any {
    const __qin_functional_sourceBody_0 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_0, null)) {
      throw new __QinJavaLangIllegalArgumentException("static source body cannot be null");
    }
    this.executeStaticSourceBody(__qin_java_functional(() => {
      __qin_functional_sourceBody_0.run();
      return null;
    }));
    return null;
  }
  __qin_overload_executeStaticSourceBody_1_1(sourceBody: any): any {
    const __qin_functional_sourceBody_0 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_0, null)) {
      throw new __QinJavaLangIllegalArgumentException("static source body cannot be null");
    }
    this.__qin_field_staticSourceBodyExecutionDepth++;
    try {
      return __qin_functional_sourceBody_0.get();
    } finally {
      this.__qin_field_staticSourceBodyExecutionDepth--;
    }
    return null;
  }
  __qin_overload_executeStaticSourceBody_3_2(ruleId: number, variantId: number, sourceBody: any): any {
    const __qin_functional_sourceBody_2 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_2, null)) {
      throw new __QinJavaLangIllegalArgumentException("static source body cannot be null");
    }
    this.executeStaticSourceBody(ruleId, variantId, __qin_java_functional(() => {
      __qin_functional_sourceBody_2.run();
      return null;
    }));
    return null;
  }
  __qin_overload_executeStaticSourceBody_3_3(ruleId: number, variantId: number, sourceBody: any): any {
    const __qin_functional_sourceBody_2 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_2, null)) {
      throw new __QinJavaLangIllegalArgumentException("static source body cannot be null");
    }
    this.ensureStaticSourceBodyStackCapacity(__qin_binary__("+", this.__qin_field_staticSourceBodyExecutionDepth, 1.0));
    let frame: any = this.__qin_field_staticSourceBodyExecutionDepth++;
    this.__qin_field_staticSourceBodyRuleIds[frame] = ruleId;
    this.__qin_field_staticSourceBodyVariantIds[frame] = variantId;
    this.__qin_field_staticSourceBodyPassThrough[frame] = false;
    try {
      return __qin_functional_sourceBody_2.get();
    } finally {
      this.__qin_field_staticSourceBodyExecutionDepth--;
      this.__qin_field_staticSourceBodyRuleIds[frame] = 0.0;
      this.__qin_field_staticSourceBodyVariantIds[frame] = 0.0;
      this.__qin_field_staticSourceBodyPassThrough[frame] = false;
    }
    return null;
  }
  executeStaticSourceBodyPassThrough(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && (__qin_args[2] === null || typeof __qin_args[2] === "function" || __qin_args[2].__qinJavaFunctional === true)) return this.__qin_overload_executeStaticSourceBodyPassThrough_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && (__qin_args[2] === null || typeof __qin_args[2] === "function" || __qin_args[2].__qinJavaFunctional === true)) return this.__qin_overload_executeStaticSourceBodyPassThrough_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: executeStaticSourceBodyPassThrough/" + __qin_args.length);
  }
  __qin_overload_executeStaticSourceBodyPassThrough_3_0(ruleId: number, variantId: number, sourceBody: any): any {
    const __qin_functional_sourceBody_2 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_2, null)) {
      throw new __QinJavaLangIllegalArgumentException("static source body cannot be null");
    }
    this.executeStaticSourceBodyPassThrough(ruleId, variantId, __qin_java_functional(() => {
      __qin_functional_sourceBody_2.run();
      return null;
    }));
    return null;
  }
  __qin_overload_executeStaticSourceBodyPassThrough_3_1(ruleId: number, variantId: number, sourceBody: any): any {
    const __qin_functional_sourceBody_2 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_2, null)) {
      throw new __QinJavaLangIllegalArgumentException("static source body cannot be null");
    }
    this.ensureStaticSourceBodyStackCapacity(__qin_binary__("+", this.__qin_field_staticSourceBodyExecutionDepth, 1.0));
    let frame: any = this.__qin_field_staticSourceBodyExecutionDepth++;
    this.__qin_field_staticSourceBodyRuleIds[frame] = ruleId;
    this.__qin_field_staticSourceBodyVariantIds[frame] = variantId;
    this.__qin_field_staticSourceBodyPassThrough[frame] = true;
    try {
      return __qin_functional_sourceBody_2.get();
    } finally {
      this.__qin_field_staticSourceBodyExecutionDepth--;
      this.__qin_field_staticSourceBodyRuleIds[frame] = 0.0;
      this.__qin_field_staticSourceBodyVariantIds[frame] = 0.0;
      this.__qin_field_staticSourceBodyPassThrough[frame] = false;
    }
    return null;
  }
  markActiveStaticRuleSourceBodyFrame(): any {
    let frame: any = this.requireActiveStaticRuleFrame("mark source body");
    this.__qin_field_activeStaticSourceBodyFrames[frame] = true;
    return null;
  }
  consumeStaticTerminal(tokenName: string, tokenValue: string, lexerMode: com_subhuti_struct_LexerMode): any {
    return this.consumeDirectTerminal(new com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal(tokenName, tokenValue, (__qin_binary__("==", lexerMode, null) ? com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE : lexerMode)));
  }
  beginStaticRule(ruleId: number, variantId: number, invocationArgument: any): any {
    return this.beginStaticRuleLinked(this.staticExecutionPlan().invocation(ruleId, variantId), invocationArgument, false, true);
  }
  beginStaticSubrule(ruleId: number, variantId: number, invocationArgument: any): any {
    return this.beginStaticRuleLinked(this.staticExecutionPlan().invocation(ruleId, variantId), invocationArgument, false, false);
  }
  beginStaticRuleLinked(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && typeof __qin_args[0] === "number" && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_beginStaticRuleLinked_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 4 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan) || __qin_args[0].__qinJavaRecordClass === com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan.__qinJavaRecordClass) && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "boolean" && typeof __qin_args[3] === "boolean") return this.__qin_overload_beginStaticRuleLinked_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    throw new Error("Unsupported Java overload: beginStaticRuleLinked/" + __qin_args.length);
  }
  __qin_overload_beginStaticRuleLinked_2_0(invocationId: number, invocationArgument: any): any {
    return this.beginStaticRuleLinked(this.staticExecutionPlan().invocation(invocationId), invocationArgument, false, true);
  }
  __qin_overload_beginStaticRuleLinked_4_1(invocationPlan: com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan, invocationArgument: any, transparentCst: boolean, validateRootCompletion: boolean): any {
    let rootEntry: any = __qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0);
    if (rootEntry) {
      this.initTopLevelData();
      if ((!this.__qin_field_preTokenizedDefaultModeInput)) {
        this.useDefaultModeTokenArrayInput();
      }
      this.bindStaticExecutionMode();
    }
    if ((!this.__qin_field_parseSuccess)) {
      return false;
    }
    this.__qin_field_indexedRuleInvocationLookups++;
    if ((!this.enterStaticInvocation(invocationPlan, invocationArgument))) {
      this.__qin_field_staticRuleLoopRejects++;
      this.setParseFail();
      return false;
    }
    let frame: any = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    this.__qin_field_activeStaticInvocationPlans[frame] = invocationPlan;
    this.__qin_field_activeStaticRootEntries[frame] = (rootEntry && validateRootCompletion);
    this.__qin_field_activeStaticTransparentCstFrames[frame] = transparentCst;
    this.__qin_field_activeStaticSourceBodyFrames[frame] = false;
    this.__qin_field_activeStaticStartCursorStamps[frame] = this.cursorStamp();
    this.__qin_field_activeStaticDebugStartTimes[frame] = this.__qin_field_staticDebugHooks.onRuleEnter(invocationPlan.ruleName(), this.currentTokenIndex());
    if (rootEntry) {
      this.__qin_field_staticRootRuleEntries++;
    } else {
      this.__qin_field_staticCoreRuleEntries++;
    }
    this.__qin_field_ruleExecutionDepth++;
    if ((this.__qin_field_buildCst && !transparentCst)) {
      this.__qin_field_ruleCstNodes++;
      let cst: any = com_subhuti_struct_SubhutiCst.builder().name(invocationPlan.ruleName()).build();
      this.__qin_field_cstStack.add(cst);
      this.__qin_field_activeStaticRuleCsts[frame] = cst;
    }
    return true;
  }
  beginStaticHelperLinked(invocationId: number, invocationArgument: any): any {
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      throw new __QinJavaLangIllegalStateException("static grammar helper executed outside a generated rule body");
    }
    return this.beginStaticRuleLinked(this.staticExecutionPlan().invocation(invocationId), invocationArgument, true, true);
  }
  staticHelperInvocationArguments(__qin_arguments: any[]): any {
    return new com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments(__qin_arguments);
  }
  completeStaticRule(): any {
    let frame: any = this.requireActiveStaticRuleFrame("complete");
    let invocationPlan: any = this.__qin_field_activeStaticInvocationPlans[frame];
    try {
      this.rejectNonNullableEmptySuccess(invocationPlan, this.__qin_field_activeStaticStartCursorStamps[frame]);
      if ((!this.__qin_field_activeStaticTransparentCstFrames[frame])) {
        this.completeStaticRuleCst(invocationPlan, this.__qin_field_activeStaticRuleCsts[frame]);
      }
      this.__qin_field_staticDebugHooks.onRuleExit(invocationPlan.ruleName(), this.__qin_field_activeStaticDebugStartTimes[frame]);
      if ((this.__qin_field_activeStaticRootEntries[frame] && this.__qin_field_parseSuccess)) {
        this.__qin_field_staticDebugHooks.onRootComplete(this.__qin_field_rootCst);
        this.validateTopLevelParseComplete();
      }
    } finally {
      this.leaveStaticRuleFrame();
    }
    return null;
  }
  abortStaticRule(): any {
    let frame: any = this.requireActiveStaticRuleFrame("abort");
    let cst: any = this.__qin_field_activeStaticRuleCsts[frame];
    if (__qin_binary__("!=", cst, null)) {
      let top: any = __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_cstStack), 1.0);
      if ((__qin_binary__("<", top, 0.0) || __qin_binary__("!=", ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(this.__qin_field_cstStack, top), cst))) {
        throw new __QinJavaLangIllegalStateException("static rule CST stack is inconsistent during abort");
      }
      this.__qin_field_cstStack.remove(top);
    }
    this.leaveStaticRuleFrame();
    return null;
  }
  bindStaticExecutionMode(): any {
    let recovery: any = this.isErrorRecoveryMode();
    let debug: any = __qin_binary__("!=", this.__qin_field__debugger, null);
    let mode: any = null;
    if (debug) {
      mode = (this.__qin_field_buildCst ? com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_DEBUG_CST : com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_DEBUG_RECOGNIZER);
      this.__qin_field__debugger.resetForNewParse(null);
      this.__qin_field_staticDebugHooks = new com_subhuti_parser_SubhutiParserCore$StaticDebugHooks();
    } else {
      this.__qin_field_staticDebugHooks = com_subhuti_parser_SubhutiParserCore$StaticDebugHooks.__qin_field_NO_OP;
      if (recovery) {
        mode = (this.__qin_field_buildCst ? com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_RECOVERY_CST : com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_RECOVERY_RECOGNIZER);
      } else {
        mode = (this.__qin_field_buildCst ? com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_CST : com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_RECOGNIZER);
      }
    }
    this.__qin_field_staticExecutionModeBindings++;
    this.__qin_field_staticExecutionModeName = mode.name();
    return null;
  }
  enterStaticInvocation(invocationPlan: com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan, invocationArgument: any): any {
    let normalizedArgument: any = (__qin_binary__("==", invocationArgument, null) ? "" : invocationArgument);
    let invocationCursorStamp: any = 0.0;
    let recursiveHeadSlot: any = invocationPlan.recursiveHeadSlot();
    if (invocationPlan.recursive()) {
      this.ensureStaticInvocationHeadCapacity(recursiveHeadSlot);
      invocationCursorStamp = this.cursorStamp();
      for (let frame: any = this.__qin_field_activeStaticInvocationHeads[recursiveHeadSlot]; __qin_binary__(">=", frame, 0.0); frame = this.__qin_field_activeStaticPreviousSameInvocation[frame]) {
        if ((__qin_binary__("==", this.__qin_field_activeStaticCursorStamps[frame], invocationCursorStamp) && __qin_binary__("==", this.__qin_field_activeStaticModes[frame], this.__qin_field_currentMode) && __QinJavaUtilObjects.equals(this.__qin_field_activeStaticArguments[frame], normalizedArgument) && __QinJavaUtilObjects.equals(this.__qin_field_activeStaticLastTokenNames[frame], this.__qin_field_lastTokenName))) {
          return false;
        }
      }
    }
    this.ensureStaticStackCapacity(__qin_binary__("+", this.__qin_field_activeStaticRuleDepth, 1.0));
    let frame: any = this.__qin_field_activeStaticRuleDepth++;
    this.__qin_field_activeStaticRuleIds[frame] = invocationPlan.ruleId();
    this.__qin_field_activeStaticVariantIds[frame] = invocationPlan.variantId();
    this.__qin_field_activeStaticArguments[frame] = normalizedArgument;
    if (invocationPlan.recursive()) {
      this.__qin_field_activeStaticInvocationIds[frame] = recursiveHeadSlot;
      this.__qin_field_activeStaticCursorStamps[frame] = invocationCursorStamp;
      this.__qin_field_activeStaticPreviousSameInvocation[frame] = this.__qin_field_activeStaticInvocationHeads[recursiveHeadSlot];
      this.__qin_field_activeStaticModes[frame] = this.__qin_field_currentMode;
      this.__qin_field_activeStaticLastTokenNames[frame] = this.__qin_field_lastTokenName;
      this.__qin_field_activeStaticInvocationHeads[recursiveHeadSlot] = frame;
    }
    this.__qin_field_staticPrimitiveInvocationEntries++;
    return true;
  }
  requireActiveStaticRuleFrame(operation: string): any {
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      throw new __QinJavaLangIllegalStateException(("cannot " + operation + " a static rule outside a generated rule body"));
    }
    let frame: any = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    if (__qin_binary__("==", this.__qin_field_activeStaticInvocationPlans[frame], null)) {
      throw new __QinJavaLangIllegalStateException(("static rule frame has no invocation plan during " + operation));
    }
    return frame;
  }
  setParseFail(): any {
    if ((__qin_binary__(">", this.__qin_field_activeStaticRuleDepth, 0.0) && __qin_binary__("<", this.__qin_field_firstStaticFailureIndex, 0.0))) {
      this.__qin_field_firstStaticFailureIndex = this.__qin_field_currentIndex;
      this.__qin_field_firstStaticFailureRules = this.activeStaticRuleScopeName();
      this.__qin_field_firstStaticFailureToken = __QinJavaLangString.valueOf(this.LA(1.0));
    }
    if ((__qin_binary__(">", this.__qin_field_activeStaticRuleDepth, 0.0) && __qin_binary__(">=", this.__qin_field_currentIndex, this.__qin_field_furthestStaticFailureIndex))) {
      this.__qin_field_furthestStaticFailureIndex = this.__qin_field_currentIndex;
      let rules: any = new __QinJavaLangStringBuilder();
      for (let frame: any = 0.0; __qin_binary__("<", frame, this.__qin_field_activeStaticRuleDepth); frame++) {
        if (__qin_binary__(">", frame, 0.0)) {
          rules.append(" -> ");
        }
        rules.append(this.staticGrammarPlan().staticRuleInvocationPlan(this.__qin_field_activeStaticRuleIds[frame], this.__qin_field_activeStaticVariantIds[frame]).ruleName());
      }
      this.__qin_field_furthestStaticFailureRules = rules.toString();
      let token: any = this.LA(1.0);
      this.__qin_field_furthestStaticFailureToken = __QinJavaLangString.valueOf(token);
    }
    super.setParseFail();
    return null;
  }
  getFurthestStaticFailureReport(): any {
    return (__qin_binary__("<", this.__qin_field_furthestStaticFailureIndex, 0.0) ? "none" : ("index=" + this.__qin_field_furthestStaticFailureIndex + ", token=" + this.__qin_field_furthestStaticFailureToken + ", rules=" + this.__qin_field_furthestStaticFailureRules));
  }
  getFirstStaticFailureReport(): any {
    return (__qin_binary__("<", this.__qin_field_firstStaticFailureIndex, 0.0) ? "none" : ("index=" + this.__qin_field_firstStaticFailureIndex + ", token=" + this.__qin_field_firstStaticFailureToken + ", rule=" + this.__qin_field_firstStaticFailureRules));
  }
  completeStaticRuleCst(invocationPlan: com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan, cst: com_subhuti_struct_SubhutiCst): any {
    if (__qin_binary__("==", cst, null)) {
      if ((!this.__qin_field_parseSuccess && __qin_binary__("!=", this.__qin_field_errorHandler, null))) {
        this.__qin_field_errorHandler.recordRuleFailure(this.__qin_field_currentPosition, invocationPlan.ruleName());
      }
      return null;
    }
    let top: any = __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_cstStack), 1.0);
    if ((__qin_binary__("<", top, 0.0) || __qin_binary__("!=", ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(this.__qin_field_cstStack, top), cst))) {
      throw new __QinJavaLangIllegalStateException("static rule CST stack is inconsistent during completion");
    }
    this.__qin_field_cstStack.remove(top);
    if (this.__qin_field_parseSuccess) {
      this.setLocation(cst);
      let parent: any = this.getCurCst();
      if (__qin_binary__("==", parent, null)) {
        this.__qin_field_rootCst = cst;
      } else {
        parent.addChild(cst);
      }
    } else {
      if (__qin_binary__("!=", this.__qin_field_errorHandler, null)) {
        this.__qin_field_errorHandler.recordRuleFailure(this.__qin_field_currentPosition, invocationPlan.ruleName());
      }
    }
    return null;
  }
  leaveStaticRuleFrame(): any {
    let frame: any = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    let invocationPlan: any = this.__qin_field_activeStaticInvocationPlans[frame];
    this.__qin_field_activeStaticInvocationPlans[frame] = null;
    this.__qin_field_activeStaticRuleCsts[frame] = null;
    this.__qin_field_activeStaticRootEntries[frame] = false;
    this.__qin_field_activeStaticTransparentCstFrames[frame] = false;
    this.__qin_field_activeStaticSourceBodyFrames[frame] = false;
    this.__qin_field_activeStaticDebugStartTimes[frame] = 0.0;
    this.exitStaticInvocation(invocationPlan.recursive());
    this.__qin_field_ruleExecutionDepth--;
    return null;
  }
  exitStaticInvocation(recursive: boolean): any {
    let frame: any = --this.__qin_field_activeStaticRuleDepth;
    if (recursive) {
      let recursiveHeadSlot: any = this.__qin_field_activeStaticInvocationIds[frame];
      this.__qin_field_activeStaticInvocationHeads[recursiveHeadSlot] = this.__qin_field_activeStaticPreviousSameInvocation[frame];
      this.__qin_field_activeStaticModes[frame] = null;
      this.__qin_field_activeStaticLastTokenNames[frame] = null;
    }
    this.__qin_field_activeStaticArguments[frame] = null;
    this.__qin_field_activeStaticStartCursorStamps[frame] = 0.0;
    return null;
  }
  ensureStaticInvocationHeadCapacity(recursiveHeadSlot: number): any {
    if (__qin_binary__("<", recursiveHeadSlot, this.__qin_field_activeStaticInvocationHeads.length)) {
      return null;
    }
    let oldLength: any = this.__qin_field_activeStaticInvocationHeads.length;
    let nextLength: any = Math.max(16.0, oldLength);
    while (__qin_binary__("<=", nextLength, recursiveHeadSlot)) {
      nextLength *= 2.0;
    }
    this.__qin_field_activeStaticInvocationHeads = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticInvocationHeads, nextLength);
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticInvocationHeads, oldLength, nextLength, __qin_binary__("-", 0.0, 1.0));
    return null;
  }
  ensureStaticStackCapacity(capacity: number): any {
    if (__qin_binary__("<=", capacity, this.__qin_field_activeStaticRuleIds.length)) {
      return null;
    }
    let nextLength: any = __qin_binary__("*", this.__qin_field_activeStaticRuleIds.length, 2.0);
    this.__qin_field_activeStaticRuleIds = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticRuleIds, nextLength);
    this.__qin_field_activeStaticVariantIds = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticVariantIds, nextLength);
    this.__qin_field_activeStaticInvocationIds = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticInvocationIds, nextLength);
    this.__qin_field_activeStaticCursorStamps = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticCursorStamps, nextLength);
    this.__qin_field_activeStaticPreviousSameInvocation = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticPreviousSameInvocation, nextLength);
    this.__qin_field_activeStaticArguments = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticArguments, nextLength);
    this.__qin_field_activeStaticModes = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticModes, nextLength);
    this.__qin_field_activeStaticLastTokenNames = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticLastTokenNames, nextLength);
    this.__qin_field_activeStaticStartCursorStamps = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticStartCursorStamps, nextLength);
    this.__qin_field_activeStaticDebugStartTimes = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticDebugStartTimes, nextLength);
    this.__qin_field_activeStaticRootEntries = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticRootEntries, nextLength);
    this.__qin_field_activeStaticTransparentCstFrames = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticTransparentCstFrames, nextLength);
    this.__qin_field_activeStaticSourceBodyFrames = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticSourceBodyFrames, nextLength);
    this.__qin_field_activeStaticRuleCsts = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticRuleCsts, nextLength);
    this.__qin_field_activeStaticInvocationPlans = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticInvocationPlans, nextLength);
    return null;
  }
  ensureStaticSourceBodyStackCapacity(capacity: number): any {
    if (__qin_binary__("<=", capacity, this.__qin_field_staticSourceBodyRuleIds.length)) {
      return null;
    }
    let nextLength: any = __qin_binary__("*", this.__qin_field_staticSourceBodyRuleIds.length, 2.0);
    while (__qin_binary__("<", nextLength, capacity)) {
      nextLength *= 2.0;
    }
    this.__qin_field_staticSourceBodyRuleIds = __QinJavaUtilArrays.copyOf(this.__qin_field_staticSourceBodyRuleIds, nextLength);
    this.__qin_field_staticSourceBodyVariantIds = __QinJavaUtilArrays.copyOf(this.__qin_field_staticSourceBodyVariantIds, nextLength);
    this.__qin_field_staticSourceBodyPassThrough = __QinJavaUtilArrays.copyOf(this.__qin_field_staticSourceBodyPassThrough, nextLength);
    return null;
  }
  rejectNonNullableEmptySuccess(invocationPlan: com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan, startCursorStamp: number): any {
    if ((this.__qin_field_parseSuccess && !invocationPlan.nullable() && __qin_binary__("==", this.cursorStamp(), startCursorStamp) && !this.__qin_field_activeStaticSourceBodyFrames[__qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0)] && !this.allowStaticNonNullableEmptySuccess(invocationPlan.ruleName()))) {
      this.__qin_field_staticNonNullableEmptyRejects++;
      this.__qin_field_lastStaticNonNullableEmptyRule = invocationPlan.ruleName();
      this.setParseFail();
    }
    return null;
  }
  allowStaticNonNullableEmptySuccess(ruleName: string): any {
    return false;
  }
  requireStaticOccurrence(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && typeof __qin_args[0] === "number" && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_subhuti_parser_SubhutiStaticGrammarPlan$Kind))) return this.__qin_overload_requireStaticOccurrence_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 4 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && typeof __qin_args[2] === "number" && (__qin_args[3] === null || __qin_instanceof__(__qin_args[3], com_subhuti_parser_SubhutiStaticGrammarPlan$Kind))) return this.__qin_overload_requireStaticOccurrence_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    throw new Error("Unsupported Java overload: requireStaticOccurrence/" + __qin_args.length);
  }
  __qin_overload_requireStaticOccurrence_2_0(occurrenceId: number, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): any {
    if (this.staticGrammarPlan().hasGeneratedMetadata()) {
      throw new __QinJavaLangIllegalStateException("generated parsers must use linked decision ids");
    }
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      throw new __QinJavaLangIllegalStateException("indexed grammar occurrence executed outside a static rule body");
    }
    let stackIndex: any = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    let occurrence: any = this.staticExecutionPlan().occurrence(this.__qin_field_activeStaticRuleIds[stackIndex], this.__qin_field_activeStaticVariantIds[stackIndex], occurrenceId);
    if (__qin_binary__("!=", occurrence.kind(), expectedKind)) {
      throw new __QinJavaLangIllegalStateException(("static grammar occurrence kind mismatch: expected " + expectedKind + " but found " + occurrence.kind() + " at " + occurrence.line() + ":" + occurrence.column()));
    }
    return occurrence;
  }
  __qin_overload_requireStaticOccurrence_4_1(ruleId: number, variantId: number, occurrenceId: number, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): any {
    if (this.staticGrammarPlan().hasGeneratedMetadata()) {
      throw new __QinJavaLangIllegalStateException("generated parsers must use linked decision ids");
    }
    let occurrence: any = this.staticExecutionPlan().occurrence(ruleId, variantId, occurrenceId);
    if (__qin_binary__("!=", occurrence.kind(), expectedKind)) {
      throw new __QinJavaLangIllegalStateException(("static grammar occurrence kind mismatch: expected " + expectedKind + " but found " + occurrence.kind() + " at " + occurrence.line() + ":" + occurrence.column()));
    }
    return occurrence;
  }
  staticDecisionPlan(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && typeof __qin_args[0] === "number" && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_subhuti_parser_SubhutiStaticGrammarPlan$Kind))) return this.__qin_overload_staticDecisionPlan_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 4 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && typeof __qin_args[2] === "number" && (__qin_args[3] === null || __qin_instanceof__(__qin_args[3], com_subhuti_parser_SubhutiStaticGrammarPlan$Kind))) return this.__qin_overload_staticDecisionPlan_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    throw new Error("Unsupported Java overload: staticDecisionPlan/" + __qin_args.length);
  }
  __qin_overload_staticDecisionPlan_2_0(occurrenceId: number, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): any {
    this.requireStaticOccurrence(occurrenceId, expectedKind);
    let frame: any = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    return this.staticExecutionPlan().decision(this.__qin_field_activeStaticRuleIds[frame], this.__qin_field_activeStaticVariantIds[frame], occurrenceId, expectedKind);
  }
  __qin_overload_staticDecisionPlan_4_1(ruleId: number, variantId: number, occurrenceId: number, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): any {
    this.requireStaticOccurrence(ruleId, variantId, occurrenceId, expectedKind);
    return this.staticExecutionPlan().decision(ruleId, variantId, occurrenceId, expectedKind);
  }
  linkedDecision(decisionId: number, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): any {
    return this.staticExecutionPlan().decision(decisionId, expectedKind);
  }
  dispatchStaticGate(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && typeof __qin_args[2] === "number") return this.__qin_overload_dispatchStaticGate_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_dispatchStaticGate_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: dispatchStaticGate/" + __qin_args.length);
  }
  __qin_overload_dispatchStaticGate_3_0(ruleId: number, variantId: number, gateId: number): any {
    this.__qin_field_staticGateDispatches++;
    return this.executeStaticGate(ruleId, variantId, gateId);
  }
  __qin_overload_dispatchStaticGate_1_1(gateId: number): any {
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      throw new __QinJavaLangIllegalStateException("static gate executed outside a static rule body");
    }
    let frame: any = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    this.__qin_field_staticGateDispatches++;
    return this.executeStaticGate(this.__qin_field_activeStaticRuleIds[frame], this.__qin_field_activeStaticVariantIds[frame], gateId);
  }
  dispatchStaticGateWithArguments(ruleId: number, variantId: number, gateId: number, invocationArgument: any): any {
    let frame: any = this.enterStaticInvocationArgumentFrame(ruleId, variantId, invocationArgument);
    try {
      this.__qin_field_staticGateDispatches++;
      return this.executeStaticGate(ruleId, variantId, gateId);
    } finally {
      this.leaveStaticInvocationArgumentFrame(frame);
    }
    return null;
  }
  withStaticInvocationArgumentFrame(ruleId: number, variantId: number, invocationArgument: any, body: any): any {
    const __qin_functional_body_3 = __qin_java_functional(body);
    let frame: any = this.enterStaticInvocationArgumentFrame(ruleId, variantId, invocationArgument);
    try {
      return __qin_functional_body_3.get();
    } finally {
      this.leaveStaticInvocationArgumentFrame(frame);
    }
    return null;
  }
  dispatchStaticAction(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && typeof __qin_args[2] === "number") return this.__qin_overload_dispatchStaticAction_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_dispatchStaticAction_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: dispatchStaticAction/" + __qin_args.length);
  }
  __qin_overload_dispatchStaticAction_3_0(ruleId: number, variantId: number, actionId: number): any {
    this.__qin_field_staticActionDispatches++;
    return this.executeStaticAction(ruleId, variantId, actionId);
  }
  __qin_overload_dispatchStaticAction_1_1(actionId: number): any {
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      throw new __QinJavaLangIllegalStateException("static action executed outside a static rule body");
    }
    let frame: any = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    this.__qin_field_staticActionDispatches++;
    return this.executeStaticAction(this.__qin_field_activeStaticRuleIds[frame], this.__qin_field_activeStaticVariantIds[frame], actionId);
  }
  staticDirectActionExecutionEnabled(): any {
    return (!this.isErrorRecoveryMode());
  }
  activeStaticInvocationArgument(): any {
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      throw new __QinJavaLangIllegalStateException("static invocation argument requested outside a static rule body");
    }
    return this.__qin_field_activeStaticArguments[__qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0)];
  }
  activeStaticInvocationArgumentAt(parameterIndex: number): any {
    let __qin_arguments: any = null;
    if (__qin_binary__("<", parameterIndex, 0.0)) {
      throw new __QinJavaLangIllegalArgumentException("static invocation parameter index cannot be negative");
    }
    let invocationArgument: any = this.activeStaticInvocationArgument();
    if ((() => { const __qin_pattern_value = invocationArgument; return __qin_instanceof__(__qin_pattern_value, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments) && (__qin_arguments = __qin_pattern_value, true); })()) {
      return __qin_arguments.get(parameterIndex);
    }
    if (__qin_binary__("!=", parameterIndex, 0.0)) {
      throw new __QinJavaLangIllegalArgumentException(("single-argument static invocation cannot read parameter " + parameterIndex));
    }
    return invocationArgument;
  }
  activeStaticRuleId(): any {
    return this.__qin_field_activeStaticRuleIds[__qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0)];
  }
  activeStaticVariantId(): any {
    return this.__qin_field_activeStaticVariantIds[__qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0)];
  }
  executeStaticAction(ruleId: number, variantId: number, actionId: number): any {
    throw new __QinJavaLangIllegalStateException(("missing generated static action: rule=" + ruleId + ", variant=" + variantId + ", action=" + actionId));
  }
  executeStaticNamedAction(ruleId: number, variantId: number, actionId: number, actionName: string): any {
    throw new __QinJavaLangIllegalStateException(("missing generated static named action: rule=" + ruleId + ", variant=" + variantId + ", action=" + actionId + ", name=" + actionName));
  }
  executeStaticSubrule(ruleId: number, variantId: number, invocationArgument: any): any {
    throw new __QinJavaLangIllegalStateException(("missing generated static subrule: rule=" + ruleId + ", variant=" + variantId));
  }
  setStaticFiniteProviderActionResult(result: any): any {
    this.__qin_field_activeStaticFiniteProviderActionResult = result;
    this.__qin_field_activeStaticFiniteProviderActionResultReady = true;
    return null;
  }
  hasStaticFiniteProviderActionResult(): any {
    return this.__qin_field_activeStaticFiniteProviderActionResultReady;
  }
  consumeStaticFiniteProviderActionResult(): any {
    if ((!this.__qin_field_activeStaticFiniteProviderActionResultReady)) {
      throw new __QinJavaLangIllegalStateException("static finite provider action result was requested before a provider produced it");
    }
    let result: any = this.__qin_field_activeStaticFiniteProviderActionResult;
    this.clearStaticFiniteProviderActionResult();
    return result;
  }
  clearStaticFiniteProviderActionResult(): any {
    this.__qin_field_activeStaticFiniteProviderActionResult = null;
    this.__qin_field_activeStaticFiniteProviderActionResultReady = false;
    return null;
  }
  executeStaticGate(ruleId: number, variantId: number, gateId: number): any {
    throw new __QinJavaLangIllegalStateException(("missing generated static gate: rule=" + ruleId + ", variant=" + variantId + ", gate=" + gateId));
  }
  executeStaticNamedGate(ruleId: number, variantId: number, gateId: number, gateName: string): any {
    throw new __QinJavaLangIllegalStateException(("missing generated static named gate: rule=" + ruleId + ", variant=" + variantId + ", gate=" + gateId + ", name=" + gateName));
  }
  executeStaticFiniteGate(decisionRuleId: number, decisionVariantId: number, occurrenceId: number, stateId: number, gateRuleId: number, gateVariantId: number, gateId: number): any {
    throw new __QinJavaLangIllegalStateException(("missing generated finite gate owner-argument proof: decision=" + decisionRuleId + ":" + decisionVariantId + ":" + occurrenceId + ", state=" + stateId + ", gate=" + gateRuleId + ":" + gateVariantId + "#" + gateId));
  }
  executeStaticSharedPrefix(ruleId: number, variantId: number, occurrenceId: number): any {
    throw new __QinJavaLangIllegalStateException(("missing generated shared-prefix provider: " + ruleId + ":" + variantId + ":" + occurrenceId));
  }
  executeStaticSharedContinuation(ruleId: number, variantId: number, occurrenceId: number, branchIndex: number): any {
    throw new __QinJavaLangIllegalStateException(("missing generated shared-prefix continuation: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + branchIndex));
  }
  executeStaticCandidateGroupPrefix(ruleId: number, variantId: number, occurrenceId: number, groupId: number): any {
    throw new __QinJavaLangIllegalStateException(("missing generated candidate-group prefix: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + groupId));
  }
  executeStaticCandidateGroupContinuation(ruleId: number, variantId: number, occurrenceId: number, groupId: number, branchIndex: number): any {
    throw new __QinJavaLangIllegalStateException(("missing generated candidate-group continuation: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + groupId + ":" + branchIndex));
  }
  executeStaticCrossRulePrefix(ruleId: number, variantId: number, occurrenceId: number, providerId: number): any {
    throw new __QinJavaLangIllegalStateException(("missing generated cross-rule prefix provider: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + providerId));
  }
  executeStaticCrossRuleContinuation(ruleId: number, variantId: number, occurrenceId: number, providerId: number, branchIndex: number): any {
    throw new __QinJavaLangIllegalStateException(("missing generated cross-rule continuation provider: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + providerId + ":" + branchIndex));
  }
  executeStaticFiniteCallPrefix(ruleId: number, variantId: number, occurrenceId: number, stateId: number): any {
    throw new __QinJavaLangIllegalStateException(("missing generated finite-program CALL_PREFIX provider: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + stateId));
  }
  executeStaticFiniteContinuation(ruleId: number, variantId: number, occurrenceId: number, stateId: number, branchIndex: number): any {
    throw new __QinJavaLangIllegalStateException(("missing generated finite-program continuation provider: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + stateId + ":" + branchIndex));
  }
  activeStaticRuleScopeName(): any {
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      return null;
    }
    let frame: any = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    let names: any = this.staticRuleNamesById();
    let ruleId: any = this.__qin_field_activeStaticRuleIds[frame];
    if ((__qin_binary__("!=", names, null) && __qin_binary__(">=", ruleId, 0.0) && __qin_binary__("<", ruleId, names.length))) {
      return names[ruleId];
    }
    return this.staticExecutionPlan().invocation(ruleId, this.__qin_field_activeStaticVariantIds[frame]).ruleName();
  }
  enterStaticInvocationArgumentFrame(ruleId: number, variantId: number, invocationArgument: any): any {
    this.ensureStaticStackCapacity(__qin_binary__("+", this.__qin_field_activeStaticRuleDepth, 1.0));
    let frame: any = this.__qin_field_activeStaticRuleDepth++;
    this.__qin_field_activeStaticRuleIds[frame] = ruleId;
    this.__qin_field_activeStaticVariantIds[frame] = variantId;
    this.__qin_field_activeStaticArguments[frame] = (__qin_binary__("==", invocationArgument, null) ? "" : invocationArgument);
    this.__qin_field_activeStaticInvocationPlans[frame] = this.staticExecutionPlan().invocation(ruleId, variantId);
    this.__qin_field_activeStaticRootEntries[frame] = false;
    this.__qin_field_activeStaticTransparentCstFrames[frame] = true;
    this.__qin_field_activeStaticSourceBodyFrames[frame] = false;
    this.__qin_field_activeStaticStartCursorStamps[frame] = this.cursorStamp();
    this.__qin_field_activeStaticRuleCsts[frame] = null;
    return frame;
  }
  leaveStaticInvocationArgumentFrame(frame: number): any {
    if ((__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0) || __qin_binary__("!=", frame, __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0)))) {
      throw new __QinJavaLangIllegalStateException("static invocation argument frame stack is inconsistent");
    }
    this.__qin_field_activeStaticInvocationPlans[frame] = null;
    this.__qin_field_activeStaticArguments[frame] = null;
    this.__qin_field_activeStaticRuleCsts[frame] = null;
    this.__qin_field_activeStaticRootEntries[frame] = false;
    this.__qin_field_activeStaticTransparentCstFrames[frame] = false;
    this.__qin_field_activeStaticSourceBodyFrames[frame] = false;
    this.__qin_field_activeStaticStartCursorStamps[frame] = 0.0;
    this.__qin_field_activeStaticDebugStartTimes[frame] = 0.0;
    this.__qin_field_activeStaticRuleDepth--;
    return null;
  }
  initTopLevelData(): any {
    super.initTopLevelData();
    this.__qin_field_activeStaticRuleDepth = 0.0;
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticInvocationHeads, __qin_binary__("-", 0.0, 1.0));
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticInvocationPlans, null);
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticRuleCsts, null);
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticRootEntries, false);
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticTransparentCstFrames, false);
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticStartCursorStamps, 0.0);
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticDebugStartTimes, 0.0);
    this.clearStaticFiniteProviderActionResult();
    return null;
  }
  parserRuntimePlan(): any {
    if (this.hasStaticGrammarPlan()) {
      throw new __QinJavaLangIllegalStateException("generated static parsers cannot allocate the handwritten runtime plan");
    }
    if (__qin_binary__("!=", this.__qin_field_parserRuntimePlan, null)) {
      return this.__qin_field_parserRuntimePlan;
    }
    let access: any = com_subhuti_parser_SubhutiParserRuntimePlan.access(this.getClass().getName(), this.effectiveGastGrammar());
    this.__qin_field_parserRuntimePlan = access.plan();
    if (access.built()) {
      this.__qin_field_parserRuntimePlanBuilds++;
    } else {
      this.__qin_field_parserRuntimePlanCacheHits++;
    }
    return this.__qin_field_parserRuntimePlan;
  }
  invalidatePredictionPlansForGrammarChange(): any {
    return null;
  }
  _getOrParseTokenEntry(index: number, line: number, column: number, mode: com_subhuti_struct_LexerMode): any {
    let currentTokenEntry: any = this.cachedCurrentTokenEntry(index, line, column, mode);
    if (__qin_binary__("!=", currentTokenEntry, null)) {
      this.__qin_field_currentTokenEntryCacheHits++;
      return currentTokenEntry;
    }
    let preTokenizedOrdinalEntry: any = this.preTokenizedFastEntryAtCurrentOrdinal(index, mode);
    if (__qin_binary__("!=", preTokenizedOrdinalEntry, null)) {
      this.cacheCurrentTokenEntry(index, line, column, mode, preTokenizedOrdinalEntry);
      return preTokenizedOrdinalEntry;
    }
    let preTokenizedEntry: any = this.preTokenizedEntryAt(index, mode);
    if (__qin_binary__("!=", preTokenizedEntry, null)) {
      this.__qin_field_tokenStreamGets++;
      this.__qin_field_tokenStreamHits++;
      this.cacheCurrentTokenEntry(index, line, column, mode, preTokenizedEntry);
      return preTokenizedEntry;
    }
    let regexpNegativeEntry: any = this.preTokenizedRegexpNegativeEntry(index, mode);
    if (__qin_binary__("!=", regexpNegativeEntry, null)) {
      this.__qin_field_preTokenizedRegexpNegativeHits++;
      return regexpNegativeEntry;
    }
    this.recordPreTokenizedFallbackMode(mode);
    this.__qin_field_tokenCacheGets++;
    let cached: any = this.__qin_field_tokenCache.get(index, mode, this.__qin_field_lastTokenName);
    if (__qin_binary__("!=", cached, null)) {
      this.__qin_field_tokenCacheHits++;
      return cached;
    }
    this.__qin_field_tokenCacheMisses++;
    let entry: any = this.__qin_field_lexer.readTokenAt(this.__qin_field_sourceCode, index, line, column, mode, this.__qin_field_lastTokenName);
    this.__qin_field_tokenCache.put(index, mode, this.__qin_field_lastTokenName, entry);
    this.__qin_field_tokenCachePuts++;
    return entry;
  }
  cachedCurrentTokenEntry(index: number, line: number, column: number, mode: com_subhuti_struct_LexerMode): any {
    if ((!this.canUseCurrentTokenEntryCache(index, line, column, mode))) {
      return null;
    }
    if ((!this.__qin_field_currentTokenEntryCacheSet || __qin_binary__("==", this.__qin_field_currentTokenEntryCacheEntry, null) || __qin_binary__("!=", this.__qin_field_currentTokenEntryCacheCodeIndex, index) || __qin_binary__("!=", this.__qin_field_currentTokenEntryCacheLine, line) || __qin_binary__("!=", this.__qin_field_currentTokenEntryCacheColumn, column) || __qin_binary__("!=", this.__qin_field_currentTokenEntryCacheTokenCursor, this.__qin_field_tokenCursor) || !__QinJavaUtilObjects.equals(this.__qin_field_currentTokenEntryCacheMode, mode) || !__QinJavaUtilObjects.equals(this.__qin_field_currentTokenEntryCacheLastTokenName, this.__qin_field_lastTokenName))) {
      return null;
    }
    return this.__qin_field_currentTokenEntryCacheEntry;
  }
  cacheCurrentTokenEntry(index: number, line: number, column: number, mode: com_subhuti_struct_LexerMode, entry: com_subhuti_lexer_TokenCacheEntry): any {
    if ((__qin_binary__("==", entry, null) || !this.canUseCurrentTokenEntryCache(index, line, column, mode))) {
      return null;
    }
    this.__qin_field_currentTokenEntryCacheSet = true;
    this.__qin_field_currentTokenEntryCacheCodeIndex = index;
    this.__qin_field_currentTokenEntryCacheLine = line;
    this.__qin_field_currentTokenEntryCacheColumn = column;
    this.__qin_field_currentTokenEntryCacheTokenCursor = this.__qin_field_tokenCursor;
    this.__qin_field_currentTokenEntryCacheMode = mode;
    this.__qin_field_currentTokenEntryCacheLastTokenName = this.__qin_field_lastTokenName;
    this.__qin_field_currentTokenEntryCacheEntry = entry;
    return null;
  }
  canUseCurrentTokenEntryCache(index: number, line: number, column: number, mode: com_subhuti_struct_LexerMode): any {
    return (!this.__qin_field_buildCst && !this.isErrorRecoveryMode() && this.__qin_field_preTokenizedDefaultModeInput && __qin_binary__("==", index, this.__qin_field_currentIndex) && __qin_binary__("==", line, this.__qin_field_currentPosition.line()) && __qin_binary__("==", column, this.__qin_field_currentPosition.column()) && (__qin_binary__("==", mode, null) || com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE.equals(mode)));
  }
  _getOrParseToken(index: number, line: number, column: number, mode: com_subhuti_struct_LexerMode): any {
    return (__qin_binary__("!=", this._getOrParseTokenEntry(index, line, column, mode), null) ? this._getOrParseTokenEntry(index, line, column, mode).getToken() : null);
  }
  recordPredictionToken(tokenName: string, tokenValue: string, mode: com_subhuti_struct_LexerMode): any {
    this.__qin_field_firstTokenRecordingTokenCount++;
    return null;
  }
  recordPredictionTokenAndStop(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) return this.__qin_overload_recordPredictionTokenAndStop_1_0(__qin_args[0]);
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] === "string")) return this.__qin_overload_recordPredictionTokenAndStop_2_1(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || __qin_instanceof__(__qin_args[2], com_subhuti_struct_LexerMode))) return this.__qin_overload_recordPredictionTokenAndStop_3_2(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: recordPredictionTokenAndStop/" + __qin_args.length);
  }
  __qin_overload_recordPredictionTokenAndStop_1_0(tokenName: string): any {
    this.recordPredictionTokenAndStop(tokenName, null, this.__qin_field_currentMode);
    return null;
  }
  __qin_overload_recordPredictionTokenAndStop_2_1(tokenName: string, tokenValue: string): any {
    this.recordPredictionTokenAndStop(tokenName, tokenValue, this.__qin_field_currentMode);
    return null;
  }
  __qin_overload_recordPredictionTokenAndStop_3_2(tokenName: string, tokenValue: string, mode: com_subhuti_struct_LexerMode): any {
    if ((!this.__qin_field_firstTokenRecording)) {
      return null;
    }
    if (this.hasStaticGrammarPlan()) {
      throw new __QinJavaLangIllegalStateException("generated static parsers cannot enter runtime token prediction recording");
    }
    this.recordPredictionToken(tokenName, tokenValue, mode);
    if (__qin_binary__(">=", this.__qin_field_firstTokenRecordingTokenCount, this.__qin_field_firstTokenRecordingMaxTokens)) {
      throw new com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException();
    }
    return null;
  }
  recordPredictionUnknownAndStop(): any {
    if (this.__qin_field_firstTokenRecording) {
      throw new com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException();
    }
    return null;
  }
  _consumeToken(tokenName: string, mode: com_subhuti_struct_LexerMode): any {
    if (this.__qin_field_firstTokenRecording) {
      this.recordPredictionTokenAndStop(tokenName, null, mode);
      return com_subhuti_struct_SubhutiCst.builder().name(tokenName).value("").build();
    }
    if ((!this.__qin_field_parseSuccess)) {
      return null;
    }
    let tokenEntry: any = (this.shouldReadTokenDirectlyForRecognizer() ? this.readTokenDirectlyForRecognizer(mode) : this._getOrParseTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), mode));
    let token: any = (__qin_binary__("!=", tokenEntry, null) ? tokenEntry.getToken() : null);
    if ((__qin_binary__("==", token, null) || token.isEof() || !__QinJavaLangString.equals(token.tokenName(), tokenName))) {
      this.setParseFail();
      if (__qin_binary__("!=", this.__qin_field_errorHandler, null)) {
        this.__qin_field_errorHandler.recordTokenMismatch(this.__qin_field_currentPosition, tokenName, token);
      }
      return null;
    }
    let cst: any = this.generateCstByToken(token);
    this.advanceTokenCursor(tokenEntry);
    this.recordParsedTokenForState(token);
    this.recordConsumedToken(token);
    return cst;
  }
  generateCstByToken(token: com_subhuti_struct_SubhutiMatchToken): any {
    if ((!this.__qin_field_buildCst)) {
      return null;
    }
    this.__qin_field_tokenCstNodes++;
    let cst: any = com_subhuti_struct_SubhutiCst.builder().name(token.tokenName()).value(token.value()).location(com_subhuti_struct_SubhutiSourceLocation.of(token.startPosition(), token.endPosition())).build();
    let currentCst: any = this.getCurCst();
    if (__qin_binary__("!=", currentCst, null)) {
      currentCst.addChild(cst);
    }
    return cst;
  }
  consumePartialToken(tokenName: string, tokenValue: string, consumedChars: number): any {
    if ((!this.__qin_field_parseSuccess)) {
      return null;
    }
    if (this.isEof()) {
      this.setParseFail();
      return null;
    }
    let token: any = this._getOrParseToken(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), this.__qin_field_currentMode);
    if ((__qin_binary__("==", token, null) || __qin_binary__("==", tokenValue, null) || __qin_binary__("<=", consumedChars, 0.0) || __qin_binary__("<", token.getLength(), consumedChars))) {
      this.setParseFail();
      return null;
    }
    let start: any = this.__qin_field_currentPosition;
    let end: any = this.__qin_field_currentPosition.advance(tokenValue);
    let partialToken: any = new com_subhuti_struct_SubhutiMatchToken(tokenName, tokenValue, start.line(), start.column(), end.column(), start.index(), token.hasLineBreakBefore());
    let cst: any = null;
    if (this.__qin_field_buildCst) {
      cst = com_subhuti_struct_SubhutiCst.builder().name(tokenName).value(tokenValue).location(com_subhuti_struct_SubhutiSourceLocation.of(tokenName, start, end)).build();
      this.__qin_field_tokenCstNodes++;
      let currentCst: any = this.getCurCst();
      if (__qin_binary__("!=", currentCst, null)) {
        currentCst.addChild(cst);
      }
    }
    this.__qin_field_currentIndex += consumedChars;
    this.__qin_field_currentPosition = this.__qin_field_currentPosition.advance(__QinJavaLangString.substring(this.__qin_field_sourceCode, __qin_binary__("-", this.__qin_field_currentIndex, consumedChars), this.__qin_field_currentIndex));
    this.__qin_field_lastTokenName = tokenName;
    if (this.__qin_field_preTokenizedDefaultModeInput) {
      this.__qin_field_tokenCursor = this.tokenOrdinalAtCodeIndex(this.__qin_field_currentIndex);
    } else {
      if (__qin_binary__("==", consumedChars, token.getLength())) {
        this.__qin_field_tokenCursor++;
      }
    }
    this.recordParsedTokenForState(partialToken);
    this.recordConsumedToken(partialToken);
    this.__qin_field_tokenCache.clear();
    return cst;
  }
  _consumeTokenMatch(tokenName: string, mode: com_subhuti_struct_LexerMode): any {
    if (this.__qin_field_firstTokenRecording) {
      this.recordPredictionTokenAndStop(tokenName, null, mode);
      return new com_subhuti_struct_SubhutiMatchToken(tokenName, "");
    }
    if ((!this.__qin_field_parseSuccess)) {
      return null;
    }
    let tokenEntry: any = (this.shouldReadTokenDirectlyForRecognizer() ? this.readTokenDirectlyForRecognizer(mode) : this._getOrParseTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), mode));
    let token: any = (__qin_binary__("!=", tokenEntry, null) ? tokenEntry.getToken() : null);
    if ((__qin_binary__("==", token, null) || token.isEof() || !__QinJavaLangString.equals(token.tokenName(), tokenName))) {
      this.setParseFail();
      return null;
    }
    this.advanceTokenCursor(tokenEntry);
    this.recordParsedTokenForState(token);
    if (this.isErrorRecoveryMode()) {
      this.recordConsumedToken(token);
    }
    if (this.__qin_field_buildCst) {
      this.generateCstByToken(token);
    }
    return token;
  }
  recordConsumedToken(token: com_subhuti_struct_SubhutiMatchToken): any {
    if ((!this.isErrorRecoveryMode() || __qin_binary__("==", token, null) || ((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(this.__qin_field_parseRecordStack))) {
      return null;
    }
    let tokenStartIndex: any = Math.max(0.0, __qin_binary__("-", this.__qin_field_tokenCursor, 1.0));
    let tokenEndIndex: any = this.__qin_field_tokenCursor;
    let tokenNode: any = new com_subhuti_parser_ParseRecordNode(token.tokenName());
    tokenNode.setToken(token);
    tokenNode.setValue(token.value());
    tokenNode.setStartTokenIndex(tokenStartIndex);
    tokenNode.setEndTokenIndex(tokenEndIndex);
    let currentRecord: any = ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(this.__qin_field_parseRecordStack, __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parseRecordStack), 1.0));
    currentRecord.getChildren().add(tokenNode);
    for (let i: any = __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parseRecordStack), 1.0); __qin_binary__(">=", i, 0.0); i--) {
      let ancestor: any = ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(this.__qin_field_parseRecordStack, i);
      if (__qin_binary__(">", tokenEndIndex, ancestor.getEndTokenIndex())) {
        ancestor.setEndTokenIndex(tokenEndIndex);
      }
    }
    return null;
  }
  shouldReadTokenDirectlyForRecognizer(): any {
    return (!this.__qin_field_buildCst && !this.isErrorRecoveryMode() && this.__qin_field_tokenCache.isEmpty());
  }
  readTokenDirectlyForRecognizer(mode: com_subhuti_struct_LexerMode): any {
    let cached: any = this.cachedCurrentTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), mode);
    if (__qin_binary__("!=", cached, null)) {
      this.__qin_field_currentTokenEntryCacheHits++;
      return cached;
    }
    let preTokenizedEntry: any = this.preTokenizedCurrentEntryWithoutCounters(mode);
    if (__qin_binary__("!=", preTokenizedEntry, null)) {
      this.cacheCurrentTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), mode, preTokenizedEntry);
      return preTokenizedEntry;
    }
    let regexpNegativeEntry: any = this.preTokenizedRegexpNegativeEntry(this.__qin_field_currentIndex, mode);
    if (__qin_binary__("!=", regexpNegativeEntry, null)) {
      this.__qin_field_preTokenizedRegexpNegativeHits++;
      return regexpNegativeEntry;
    }
    return this.__qin_field_lexer.readTokenAt(this.__qin_field_sourceCode, this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), mode, this.__qin_field_lastTokenName);
  }
  preTokenizedFastEntryAtCurrentOrdinal(index: number, mode: com_subhuti_struct_LexerMode): any {
    if (__qin_binary__("!=", index, this.__qin_field_currentIndex)) {
      return null;
    }
    let entry: any = this.preTokenizedEntryAtParsedOrdinal(1.0, mode);
    if (__qin_binary__("!=", entry, null)) {
      this.__qin_field_tokenStreamGets++;
      this.__qin_field_tokenStreamHits++;
    }
    return entry;
  }
  executeRuleWrapper(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "function" || __qin_args[0].__qinJavaFunctional === true) && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string")) return this.__qin_overload_executeRuleWrapper_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 4 && (__qin_args[0] === null || typeof __qin_args[0] === "function" || __qin_args[0].__qinJavaFunctional === true) && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string") && (__qin_args[3] === null || typeof __qin_args[3] !== "undefined")) return this.__qin_overload_executeRuleWrapper_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    if (__qin_args.length === 5 && (__qin_args[0] === null || typeof __qin_args[0] === "function" || __qin_args[0].__qinJavaFunctional === true) && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string") && (__qin_args[3] === null || typeof __qin_args[3] !== "undefined") && typeof __qin_args[4] === "boolean") return this.__qin_overload_executeRuleWrapper_5_2(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    if (__qin_args.length === 6 && (__qin_args[0] === null || typeof __qin_args[0] === "function" || __qin_args[0].__qinJavaFunctional === true) && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string") && (__qin_args[3] === null || typeof __qin_args[3] !== "undefined") && typeof __qin_args[4] === "boolean" && typeof __qin_args[5] === "number") return this.__qin_overload_executeRuleWrapper_6_3(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5]);
    throw new Error("Unsupported Java overload: executeRuleWrapper/" + __qin_args.length);
  }
  __qin_overload_executeRuleWrapper_3_0(targetFun: any, ruleName: string, className: string): any {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    return this.executeRuleWrapper(__qin_functional_targetFun_0, ruleName, className, "");
  }
  __qin_overload_executeRuleWrapper_4_1(targetFun: any, ruleName: string, className: string, cacheKeyExtra: any): any {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    return this.executeRuleWrapper(__qin_functional_targetFun_0, ruleName, className, cacheKeyExtra, true);
  }
  __qin_overload_executeRuleWrapper_5_2(targetFun: any, ruleName: string, className: string, cacheKeyExtra: any, cacheRule: boolean): any {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    return this.executeRuleWrapper(__qin_functional_targetFun_0, ruleName, className, cacheKeyExtra, cacheRule, __qin_binary__("-", 0.0, 1.0));
  }
  __qin_overload_executeRuleWrapper_6_3(targetFun: any, ruleName: string, className: string, cacheKeyExtra: any, cacheRule: boolean, ruleId: number): any {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    if ((!this.__qin_field_parseSuccess)) {
      return null;
    }
    let isTopLevel: any = __qin_binary__("==", this.__qin_field_ruleExecutionDepth, 0.0);
    if (isTopLevel) {
      this.initTopLevelData();
      if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
        this.__qin_field__debugger.resetForNewParse(null);
      }
    }
    if ((!isTopLevel && this.canInlineRecognizerRule(ruleName))) {
      this.__qin_field_ruleWrapperPassThroughSkips++;
      this.incrementRuleProfile(this.__qin_field_ruleWrapperPassThroughCounts, ruleName);
      if (this.isRecognizerTerminalLeafRule(ruleName)) {
        this.__qin_field_ruleWrapperTerminalLeafSkips++;
        this.incrementRuleProfile(this.__qin_field_ruleWrapperTerminalLeafCounts, ruleName);
      } else {
        this.__qin_field_ruleWrapperRuleChainSkips++;
        this.incrementRuleProfile(this.__qin_field_ruleWrapperRuleChainCounts, ruleName);
      }
      this.__qin_field_activeRuleScopeStack.addLast(ruleName);
      if ((!this.__qin_field_coreRuleProfileEnabled)) {
        try {
          return __qin_functional_targetFun_0.get();
        } finally {
          this.__qin_field_activeRuleScopeStack.removeLast();
        }
      }
      this.__qin_field_activeRuleProfileStack.addLast(ruleName);
      try {
        return __qin_functional_targetFun_0.get();
      } finally {
        this.__qin_field_activeRuleProfileStack.removeLast();
        this.__qin_field_activeRuleScopeStack.removeLast();
      }
    }
    this.__qin_field_ruleExecutionDepth++;
    this.__qin_field_ruleWrapperCalls++;
    this.incrementRuleProfile(this.__qin_field_ruleWrapperCallCounts, ruleName);
    this.__qin_field_activeRuleScopeStack.addLast(ruleName);
    let tokenIndex: any = this.currentTokenIndex();
    let invocationCursorStamp: any = this.cursorStamp();
    let memoizeRule: any = (!isTopLevel && this.shouldMemoizeRule(ruleName, cacheRule));
    let key: any = (memoizeRule ? this.ruleCacheKey(ruleName, cacheKeyExtra, invocationCursorStamp, this.__qin_field_currentMode, this.__qin_field_lastTokenName) : null);
    let activeRuleInvocations: any = null;
    if ((__qin_binary__(">=", ruleId, 0.0) && this.__qin_field_indexedRuleInvocationsEnabled)) {
      this.__qin_field_indexedRuleInvocationLookups++;
      if (__qin_binary__(">=", ruleId, this.__qin_field_indexedActiveRuleInvocations.length)) {
        let nextLength: any = this.__qin_field_indexedActiveRuleInvocations.length;
        while (__qin_binary__("<=", nextLength, ruleId)) {
          nextLength *= 2.0;
        }
        this.__qin_field_indexedActiveRuleInvocations = __QinJavaUtilArrays.copyOf(this.__qin_field_indexedActiveRuleInvocations, nextLength);
      }
      activeRuleInvocations = this.__qin_field_indexedActiveRuleInvocations[ruleId];
      if (__qin_binary__("==", activeRuleInvocations, null)) {
        activeRuleInvocations = new com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations();
        this.__qin_field_indexedActiveRuleInvocations[ruleId] = activeRuleInvocations;
      }
    } else {
      activeRuleInvocations = this.__qin_field_activeRuleInvocationsByName.computeIfAbsent(ruleName, __qin_java_functional((ignored) => {
      return new com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations();
    }));
    }
    let ruleInvocationEntered: any = activeRuleInvocations.enter(cacheKeyExtra, invocationCursorStamp, this.__qin_field_currentMode, this.__qin_field_lastTokenName);
    if ((!ruleInvocationEntered)) {
      this.setParseFail();
      this.__qin_field_activeRuleScopeStack.removeLast();
      this.__qin_field_ruleExecutionDepth--;
      return null;
    }
    let startTime: any = 0.0;
    if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
      startTime = this.__qin_field__debugger.onRuleEnter(ruleName, tokenIndex);
    }
    let ruleProfileStackPushed: any = false;
    try {
      if (this.__qin_field_coreRuleProfileEnabled) {
        this.__qin_field_activeRuleProfileStack.addLast(ruleName);
        ruleProfileStackPushed = true;
      }
      if (memoizeRule) {
        let cached: any = this.ruleCache().get(key);
        if (cached.isPresent()) {
          this.__qin_field_ruleCacheHits++;
          this.recordAdaptiveRuleCacheHit(ruleName);
          this.incrementRuleProfile(this.__qin_field_ruleCacheHitCounts, ruleName);
          this.incrementCacheHitTokenSpan(ruleName, cached.get(), tokenIndex);
          if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
            this.__qin_field__debugger.onRuleExit(ruleName, true, startTime);
          }
          if ((this.isErrorRecoveryMode() && (() => { const __qin_instanceof_value = cached.get(); return __qin_instanceof__(__qin_instanceof_value, com_subhuti_cache_SubhutiPackratCacheResult); })())) {
            let cacheResult: any = (cached.get());
            if (__qin_binary__(">", cacheResult.__qin_field_endTokenIndex, tokenIndex)) {
              let recordNode: any = new com_subhuti_parser_ParseRecordNode(ruleName);
              recordNode.setStartTokenIndex(tokenIndex);
              recordNode.setEndTokenIndex(cacheResult.__qin_field_endTokenIndex);
              if ((__qin_binary__("!=", cacheResult.__qin_field_recordNode, null) && __qin_binary__("!=", cacheResult.__qin_field_recordNode.getChildren(), null))) {
                recordNode.setChildren(new __QinJavaUtilArrayList(cacheResult.__qin_field_recordNode.getChildren()));
              }
              let recordParent: any = (((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(this.__qin_field_parseRecordStack) ? null : ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(this.__qin_field_parseRecordStack, __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parseRecordStack), 1.0)));
              if (__qin_binary__("!=", recordParent, null)) {
                recordParent.getChildren().add(recordNode);
              }
              for (let i: any = __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parseRecordStack), 1.0); __qin_binary__(">=", i, 0.0); i--) {
                let ancestor: any = ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(this.__qin_field_parseRecordStack, i);
                if (__qin_binary__(">", cacheResult.__qin_field_endTokenIndex, ancestor.getEndTokenIndex())) {
                  ancestor.setEndTokenIndex(cacheResult.__qin_field_endTokenIndex);
                }
              }
            }
          }
          return this.applyCachedResult(cached.get());
        }
      }
      let startParsedTokenCount: any = this.currentTokenIndex();
      let startStoredTokenCount: any = ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parsedTokens);
      let recordNode: any = null;
      if (this.isErrorRecoveryMode()) {
        recordNode = new com_subhuti_parser_ParseRecordNode(ruleName);
        recordNode.setStartTokenIndex(startParsedTokenCount);
        recordNode.setEndTokenIndex(startParsedTokenCount);
        this.__qin_field_parseRecordStack.add(recordNode);
      }
      let result: any = null;
      let producedCst: any = null;
      if (this.__qin_field_buildCst) {
        let executionResult: any = this.executeRuleCore(ruleName, __qin_functional_targetFun_0);
        result = executionResult.__qin_field_ruleResult;
        producedCst = executionResult.__qin_field_cst;
      } else {
        result = this.executeRuleCoreNoCst(ruleName, __qin_functional_targetFun_0);
      }
      if ((this.isErrorRecoveryMode() && __qin_binary__("!=", recordNode, null))) {
        this.__qin_field_parseRecordStack.remove(__qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parseRecordStack), 1.0));
        if (__qin_binary__(">", recordNode.getEndTokenIndex(), recordNode.getStartTokenIndex())) {
          let recordParent: any = (((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(this.__qin_field_parseRecordStack) ? null : ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(this.__qin_field_parseRecordStack, __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parseRecordStack), 1.0)));
          if (__qin_binary__("!=", recordParent, null)) {
            recordParent.getChildren().add(recordNode);
          }
        }
      }
      if (memoizeRule) {
        let endParsedTokenCount: any = this.currentTokenIndex();
        let finalEndIndex: any = (__qin_binary__("!=", recordNode, null) ? Math.max(recordNode.getEndTokenIndex(), endParsedTokenCount) : endParsedTokenCount);
        let consumedTokens: any = null;
        if ((this.__qin_field_parseSuccess && this.shouldStoreParsedTokens() && __qin_binary__(">", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parsedTokens), startStoredTokenCount))) {
          consumedTokens = new __QinJavaUtilArrayList(((__qin_collection, __qin_from, __qin_to) => new __QinJavaUtilArrayList((Array.isArray(__qin_collection) ? __qin_collection : __qin_collection.toArray()).slice(Number(__qin_from), Number(__qin_to))))(this.__qin_field_parsedTokens, startStoredTokenCount, ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parsedTokens)));
        }
        let cacheResult: any = new com_subhuti_cache_SubhutiPackratCacheResult(finalEndIndex, producedCst, this.__qin_field_parseSuccess, recordNode, consumedTokens, (this.__qin_field_parseSuccess ? this.__qin_field_currentIndex : __qin_binary__("-", 0.0, 1.0)), (this.__qin_field_parseSuccess ? this.__qin_field_currentPosition.line() : __qin_binary__("-", 0.0, 1.0)), (this.__qin_field_parseSuccess ? this.__qin_field_currentPosition.column() : __qin_binary__("-", 0.0, 1.0)), (this.__qin_field_parseSuccess ? this.__qin_field_lastTokenName : null));
        this.__qin_field_ruleCachePuts++;
        this.recordAdaptiveRuleCachePut(ruleName);
        this.incrementRuleProfile(this.__qin_field_ruleCachePutCounts, ruleName);
        this.ruleCache().put(key, cacheResult);
      }
      if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
        this.__qin_field__debugger.onRuleExit(ruleName, false, startTime);
      }
      if ((isTopLevel && this.__qin_field_parseSuccess)) {
        if ((__qin_binary__("!=", this.__qin_field__debugger, null) && __qin_binary__("!=", this.__qin_field_rootCst, null))) {
          this.__qin_field__debugger.setCst(this.__qin_field_rootCst);
          this.__qin_field__debugger.autoOutput();
        }
        this.validateTopLevelParseComplete();
      }
      return result;
    } finally {
      if (ruleProfileStackPushed) {
        this.__qin_field_activeRuleProfileStack.removeLast();
      }
      if (ruleInvocationEntered) {
        activeRuleInvocations.exit();
      }
      this.__qin_field_activeRuleScopeStack.removeLast();
      this.__qin_field_ruleExecutionDepth--;
    }
    return null;
  }
  executeVoidRuleWrapper(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "function" || __qin_args[0].__qinJavaFunctional === true) && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string")) return this.__qin_overload_executeVoidRuleWrapper_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 4 && (__qin_args[0] === null || typeof __qin_args[0] === "function" || __qin_args[0].__qinJavaFunctional === true) && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string") && (__qin_args[3] === null || typeof __qin_args[3] !== "undefined")) return this.__qin_overload_executeVoidRuleWrapper_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    if (__qin_args.length === 5 && (__qin_args[0] === null || typeof __qin_args[0] === "function" || __qin_args[0].__qinJavaFunctional === true) && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string") && (__qin_args[3] === null || typeof __qin_args[3] !== "undefined") && typeof __qin_args[4] === "boolean") return this.__qin_overload_executeVoidRuleWrapper_5_2(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    if (__qin_args.length === 6 && (__qin_args[0] === null || typeof __qin_args[0] === "function" || __qin_args[0].__qinJavaFunctional === true) && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string") && (__qin_args[3] === null || typeof __qin_args[3] !== "undefined") && typeof __qin_args[4] === "boolean" && typeof __qin_args[5] === "number") return this.__qin_overload_executeVoidRuleWrapper_6_3(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5]);
    throw new Error("Unsupported Java overload: executeVoidRuleWrapper/" + __qin_args.length);
  }
  __qin_overload_executeVoidRuleWrapper_3_0(targetFun: any, ruleName: string, className: string): any {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    this.executeVoidRuleWrapper(__qin_functional_targetFun_0, ruleName, className, "", true);
    return null;
  }
  __qin_overload_executeVoidRuleWrapper_4_1(targetFun: any, ruleName: string, className: string, cacheKeyExtra: any): any {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    this.executeVoidRuleWrapper(__qin_functional_targetFun_0, ruleName, className, cacheKeyExtra, true);
    return null;
  }
  __qin_overload_executeVoidRuleWrapper_5_2(targetFun: any, ruleName: string, className: string, cacheKeyExtra: any, cacheRule: boolean): any {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    this.executeVoidRuleWrapper(__qin_functional_targetFun_0, ruleName, className, cacheKeyExtra, cacheRule, __qin_binary__("-", 0.0, 1.0));
    return null;
  }
  __qin_overload_executeVoidRuleWrapper_6_3(targetFun: any, ruleName: string, className: string, cacheKeyExtra: any, cacheRule: boolean, ruleId: number): any {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    if ((!this.__qin_field_firstTokenRecording && this.__qin_field_parseSuccess && this.tryExecuteDirectVoidRecognizerRule(ruleName))) {
      return null;
    }
    this.executeRuleWrapper(__qin_java_functional(() => {
      __qin_functional_targetFun_0.run();
      return null;
    }), ruleName, className, cacheKeyExtra, cacheRule, ruleId);
    return null;
  }
  tryExecuteDirectVoidRecognizerRule(ruleName: string): any {
    if ((this.__qin_field_buildCst || this.isErrorRecoveryMode() || __qin_binary__("!=", this.__qin_field__debugger, null) || __qin_binary__("==", ruleName, null) || __QinJavaLangString.isBlank(ruleName))) {
      return false;
    }
    let gast: any = this.effectiveGastGrammar();
    if ((__qin_binary__("==", gast, null) || __qin_binary__("==", gast.rule(ruleName), null))) {
      return false;
    }
    let runtimePlan: any = this.parserRuntimePlan();
    let recognizerPlan: any = runtimePlan.recognizerDirectRecognizerPlanRule(ruleName);
    if (__qin_binary__("!=", recognizerPlan, null)) {
      this.useDefaultModeTokenArrayInputForTopLevelDirectPlan(recognizerPlan.defaultModeOnly());
      return this.executeDirectVoidRecognizerPlan(ruleName, recognizerPlan);
    }
    let terminalSequence: any = runtimePlan.recognizerDirectTerminalSequenceRule(ruleName);
    if (__qin_binary__("!=", terminalSequence, null)) {
      this.useDefaultModeTokenArrayInputForTopLevelDirectPlan(terminalSequence.defaultModeOnly());
      return this.executeDirectVoidRecognizerTerminalSequence(ruleName, terminalSequence);
    }
    let terminal: any = runtimePlan.recognizerDirectTerminalRule(ruleName);
    if (__qin_binary__("==", terminal, null)) {
      return false;
    }
    this.useDefaultModeTokenArrayInputForTopLevelDirectPlan(terminal.defaultMode());
    return this.executeDirectVoidRecognizerTerminal(ruleName, terminal);
  }
  useDefaultModeTokenArrayInputForTopLevelDirectPlan(defaultModeOnly: boolean): any {
    if ((!defaultModeOnly || this.__qin_field_preTokenizedDefaultModeInput || __qin_binary__("!=", this.__qin_field_ruleExecutionDepth, 0.0) || __qin_binary__("!=", this.__qin_field_currentIndex, 0.0) || __qin_binary__("!=", this.__qin_field_tokenCursor, 0.0) || !this.__qin_field_tokenCache.isEmpty())) {
      return null;
    }
    this.useDefaultModeTokenArrayInput();
    return null;
  }
  executeDirectVoidRecognizerPlan(ruleName: string, recognizerPlan: com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerPlan): any {
    let isTopLevel: any = __qin_binary__("==", this.__qin_field_ruleExecutionDepth, 0.0);
    if (isTopLevel) {
      this.initTopLevelData();
      if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
        this.__qin_field__debugger.resetForNewParse(null);
      }
    }
    this.__qin_field_ruleWrapperDirectRecognizerPlanSkips++;
    this.__qin_field_ruleWrapperPassThroughSkips++;
    this.incrementRuleProfile(this.__qin_field_ruleWrapperPassThroughCounts, ruleName);
    this.executeDirectPlanElements(recognizerPlan);
    if ((isTopLevel && this.__qin_field_parseSuccess)) {
      this.validateTopLevelParseComplete();
    }
    return true;
  }
  executeDirectPlanElements(recognizerPlan: com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerPlan): any {
    for (const element of recognizerPlan.elements()) {
      if (element.alternation()) {
        if ((!this.consumeDirectAlternation(element))) {
          break;
        }
        continue;
      }
      if (element.atLeastOne()) {
        if ((!this.consumeDirectTerminals(element.terminals()))) {
          break;
        }
        while (this.canStartDirectTerminalSequence(element.terminals())) {
          if ((!this.consumeDirectTerminals(element.terminals()))) {
            break;
          }
        }
        if ((!this.__qin_field_parseSuccess)) {
          break;
        }
        continue;
      }
      if (element.repetition()) {
        while (this.canStartDirectTerminalSequence(element.terminals())) {
          if ((!this.consumeDirectTerminals(element.terminals()))) {
            break;
          }
        }
        if ((!this.__qin_field_parseSuccess)) {
          break;
        }
        continue;
      }
      if (element.optional()) {
        if ((!this.canStartDirectTerminalSequence(element.terminals()))) {
          continue;
        }
      }
      if ((!this.consumeDirectTerminals(element.terminals()))) {
        break;
      }
    }
    return null;
  }
  consumeDirectAlternation(element: com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerElement): any {
    if (__qin_binary__("<=", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(element.alternatives()), 2.0)) {
      return this.consumeDirectAlternationLinear(element.alternatives());
    }
    return this.consumeDirectAlternationDispatch(element.alternativesByFirstModeAndKey());
  }
  consumeDirectAlternationLinear(alternatives: any): any {
    if ((__qin_binary__("==", alternatives, null) || ((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(alternatives))) {
      this.setParseFail();
      return false;
    }
    for (const alternative of alternatives) {
      if (this.canStartDirectTerminalSequence(alternative)) {
        return this.consumeDirectTerminals(alternative);
      }
    }
    this.setParseFail();
    this.clearCurrentTokenEntryCache();
    return false;
  }
  consumeDirectAlternationDispatch(alternativesByFirstModeAndKey: any): any {
    if ((__qin_binary__("==", alternativesByFirstModeAndKey, null) || alternativesByFirstModeAndKey.isEmpty())) {
      this.setParseFail();
      return false;
    }
    for (const modeEntry of alternativesByFirstModeAndKey.entrySet()) {
      let mode: any = modeEntry.getKey();
      let firstEntry: any = this.preTokenizedCurrentEntryWithoutCounters(mode);
      if (__qin_binary__("==", firstEntry, null)) {
        firstEntry = this.directCurrentTokenEntry(mode);
      }
      let token: any = (__qin_binary__("!=", firstEntry, null) ? firstEntry.getToken() : null);
      if ((__qin_binary__("==", token, null) || token.isEof())) {
        continue;
      }
      let alternative: any = modeEntry.getValue().get(this.directTokenKey(token));
      if (__qin_binary__("==", alternative, null)) {
        alternative = modeEntry.getValue().get(token.tokenName());
      }
      if (__qin_binary__("!=", alternative, null)) {
        return this.consumeDirectTerminalSequenceFromFirstEntry(alternative, firstEntry);
      }
    }
    this.setParseFail();
    this.clearCurrentTokenEntryCache();
    return false;
  }
  executeDirectVoidRecognizerTerminal(ruleName: string, terminal: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal): any {
    let isTopLevel: any = __qin_binary__("==", this.__qin_field_ruleExecutionDepth, 0.0);
    if (isTopLevel) {
      this.initTopLevelData();
      if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
        this.__qin_field__debugger.resetForNewParse(null);
      }
    }
    this.__qin_field_ruleWrapperDirectTerminalSkips++;
    this.__qin_field_ruleWrapperPassThroughSkips++;
    this.incrementRuleProfile(this.__qin_field_ruleWrapperPassThroughCounts, ruleName);
    this.consumeDirectTerminal(terminal);
    if ((isTopLevel && this.__qin_field_parseSuccess)) {
      this.validateTopLevelParseComplete();
    }
    return true;
  }
  executeDirectVoidRecognizerTerminalSequence(ruleName: string, terminalSequence: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminalSequence): any {
    let isTopLevel: any = __qin_binary__("==", this.__qin_field_ruleExecutionDepth, 0.0);
    if (isTopLevel) {
      this.initTopLevelData();
      if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
        this.__qin_field__debugger.resetForNewParse(null);
      }
    }
    this.__qin_field_ruleWrapperDirectTerminalSkips++;
    this.__qin_field_ruleWrapperDirectTerminalSequenceSkips++;
    this.__qin_field_ruleWrapperPassThroughSkips++;
    this.incrementRuleProfile(this.__qin_field_ruleWrapperPassThroughCounts, ruleName);
    if (this.consumeDirectTerminalSequenceWithPreTokenizedCursor(terminalSequence)) {
      if ((isTopLevel && this.__qin_field_parseSuccess)) {
        this.validateTopLevelParseComplete();
      }
      return true;
    }
    this.consumeDirectTerminals(terminalSequence.terminals());
    if ((isTopLevel && this.__qin_field_parseSuccess)) {
      this.validateTopLevelParseComplete();
    }
    return true;
  }
  consumeDirectTerminals(terminals: any): any {
    if ((__qin_binary__("==", terminals, null) || ((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(terminals))) {
      return true;
    }
    if ((!this.__qin_field_buildCst && this.consumeDirectTerminalListWithPreTokenizedCursor(terminals))) {
      return this.__qin_field_parseSuccess;
    }
    for (const terminal of terminals) {
      if ((!this.consumeDirectTerminal(terminal))) {
        return false;
      }
    }
    return true;
  }
  canStartDirectTerminalSequence(terminals: any): any {
    if ((__qin_binary__("==", terminals, null) || ((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(terminals))) {
      return false;
    }
    let first: any = ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(terminals, 0.0);
    let entry: any = this.preTokenizedCurrentEntryWithoutCounters(first.lexerMode());
    if (__qin_binary__("==", entry, null)) {
      entry = this.directCurrentTokenEntry(first.lexerMode());
    }
    let token: any = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
    return this.tokenMatchesDirectTerminal(token, first);
  }
  consumeDirectTerminal(terminal: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal): any {
    let entry: any = this.directCurrentTokenEntry(terminal.lexerMode());
    let token: any = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
    if ((!this.tokenMatchesDirectTerminal(token, terminal))) {
      this.setParseFail();
      this.clearCurrentTokenEntryCache();
      return false;
    }
    this.advanceTokenCursor(entry);
    this.recordParsedTokenForState(token);
    this.generateCstByToken(token);
    this.clearCurrentTokenEntryCache();
    return true;
  }
  consumeDirectTerminalSequenceFromFirstEntry(terminals: any, firstEntry: com_subhuti_lexer_TokenCacheEntry): any {
    if ((__qin_binary__("==", terminals, null) || ((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(terminals))) {
      return true;
    }
    if ((!this.consumeDirectTerminalEntry(((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(terminals, 0.0), firstEntry))) {
      return false;
    }
    if (__qin_binary__("==", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(terminals), 1.0)) {
      return true;
    }
    if ((!this.__qin_field_buildCst && this.consumeDirectTerminalRangeWithPreTokenizedCursor(terminals, 1.0))) {
      return this.__qin_field_parseSuccess;
    }
    for (let i: any = 1.0; __qin_binary__("<", i, ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(terminals)); i++) {
      if ((!this.consumeDirectTerminal(((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(terminals, i)))) {
        return false;
      }
    }
    return true;
  }
  consumeDirectTerminalEntry(terminal: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal, entry: com_subhuti_lexer_TokenCacheEntry): any {
    let token: any = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
    if ((!this.tokenMatchesDirectTerminal(token, terminal))) {
      this.setParseFail();
      this.clearCurrentTokenEntryCache();
      return false;
    }
    this.advanceTokenCursor(entry);
    this.recordParsedTokenForState(token);
    this.generateCstByToken(token);
    this.clearCurrentTokenEntryCache();
    return true;
  }
  tokenMatchesDirectTerminal(token: com_subhuti_struct_SubhutiMatchToken, terminal: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal): any {
    if ((__qin_binary__("==", token, null) || token.isEof() || !__QinJavaLangString.equals(token.tokenName(), terminal.tokenName()))) {
      return false;
    }
    return (__qin_binary__("==", terminal.tokenValue(), null) || __QinJavaUtilObjects.equals(token.tokenValue(), terminal.tokenValue()));
  }
  directTokenKey(token: com_subhuti_struct_SubhutiMatchToken): any {
    return com_subhuti_parser_SubhutiTokenPrediction.key(token.tokenName(), token.tokenValue());
  }
  preTokenizedCurrentEntryWithoutCounters(mode: com_subhuti_struct_LexerMode): any {
    if ((!this.__qin_field_preTokenizedDefaultModeInput || __qin_binary__("==", this.__qin_field_preTokenizedEntriesByOrdinal, null) || (__qin_binary__("!=", mode, null) && !com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE.equals(mode)))) {
      return null;
    }
    return this.preTokenizedEntryAtParsedOrdinal(1.0, com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE);
  }
  directCurrentTokenEntry(mode: com_subhuti_struct_LexerMode): any {
    return (this.shouldReadTokenDirectlyForRecognizer() ? this.readTokenDirectlyForRecognizer(mode) : this._getOrParseTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), mode));
  }
  consumeDirectTerminalSequenceWithPreTokenizedCursor(terminalSequence: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminalSequence): any {
    return this.consumeDirectTerminalListWithPreTokenizedCursor(terminalSequence.terminals());
  }
  consumeDirectTerminalListWithPreTokenizedCursor(terminals: any): any {
    return this.consumeDirectTerminalRangeWithPreTokenizedCursor(terminals, 0.0);
  }
  consumeDirectTerminalRangeWithPreTokenizedCursor(terminals: any, startIndex: number): any {
    if ((!this.__qin_field_preTokenizedDefaultModeInput || __qin_binary__("==", this.__qin_field_preTokenizedEntriesByOrdinal, null))) {
      return false;
    }
    if ((__qin_binary__("==", terminals, null) || __qin_binary__("<", startIndex, 0.0) || __qin_binary__(">", startIndex, ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(terminals)))) {
      return false;
    }
    for (let i: any = startIndex; __qin_binary__("<", i, ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(terminals)); i++) {
      let terminal: any = ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(terminals, i);
      let mode: any = terminal.lexerMode();
      if ((__qin_binary__("!=", mode, null) && !com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE.equals(mode))) {
        return false;
      }
    }
    for (let i: any = startIndex; __qin_binary__("<", i, ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(terminals)); i++) {
      let entry: any = this.preTokenizedEntryAtParsedOrdinal(1.0, com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE);
      let token: any = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
      let terminal: any = ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(terminals, i);
      if ((!this.tokenMatchesDirectTerminal(token, terminal))) {
        this.setParseFail();
        this.clearCurrentTokenEntryCache();
        return true;
      }
      this.advanceTokenCursor(entry);
      this.recordParsedTokenForState(token);
    }
    this.clearCurrentTokenEntryCache();
    return true;
  }
  validateTopLevelParseComplete(): any {
    if ((!this.__qin_field_parseSuccess || this.isEof())) {
      return null;
    }
    let nextToken: any = this.LA(1.0);
    let tokenInfo: any = (__qin_binary__("!=", nextToken, null) ? __QinJavaLangString.format("\"%s\" (%s) at position %d", nextToken.value(), nextToken.tokenName(), this.__qin_field_currentIndex) : "EOF");
    let errorMessage: any = ("Parser internal error: parsing succeeded but source code remains unconsumed. " + "Next token: " + tokenInfo);
    if (this.isErrorRecoveryMode()) {
      this.recordRecoveryDiagnostic(errorMessage, nextToken);
    } else {
      if ((this.__qin_field_analysisMode && __qin_binary__("!=", this.__qin_field_errorHandler, null))) {
        this.__qin_field_errorHandler.recordError(this.__qin_field_currentPosition, errorMessage, "EOF", tokenInfo);
      } else {
        throw new __QinJavaLangRuntimeException(errorMessage);
      }
    }
    return null;
  }
  ruleCacheKey(ruleName: string, cacheKeyExtra: any, cursorStamp: number, mode: com_subhuti_struct_LexerMode, lastTokenName: string): any {
    this.__qin_field_ruleCacheKeyBuilds++;
    return new com_subhuti_parser_SubhutiRuleCacheKey(ruleName, cacheKeyExtra, cursorStamp, mode, lastTokenName);
  }
  shouldMemoizeRule(ruleName: string, cacheRule: boolean): any {
    if ((!this.__qin_field_enableMemoization || !cacheRule)) {
      return false;
    }
    if (this.isRecognizerPassThroughRule(ruleName)) {
      this.__qin_field_ruleCachePassThroughSkips++;
      return false;
    }
    if (this.isRecognizerTerminalLeafRule(ruleName)) {
      this.__qin_field_ruleCacheTerminalLeafSkips++;
      return false;
    }
    if (this.isRecognizerLowYieldMemoRule(ruleName)) {
      this.__qin_field_ruleCacheLowYieldSkips++;
      return false;
    }
    if (this.isAdaptiveLowYieldMemoRule(ruleName)) {
      this.__qin_field_ruleCacheAdaptiveLowYieldSkips++;
      return false;
    }
    if ((!this.__qin_field_buildCst && !this.isErrorRecoveryMode() && __qin_binary__("==", this.__qin_field_speculativeParseDepth, 0.0))) {
      this.__qin_field_ruleCacheSpeculativeSkips++;
      return false;
    }
    return true;
  }
  canInlineRecognizerRule(ruleName: string): any {
    return (__qin_binary__("==", this.__qin_field__debugger, null) && (this.isRecognizerPassThroughRule(ruleName) || this.isRecognizerTerminalLeafRule(ruleName)));
  }
  isRecognizerPassThroughRule(ruleName: string): any {
    if ((this.__qin_field_buildCst || this.isErrorRecoveryMode())) {
      return false;
    }
    let passThroughRules: any = this.recognizerPassThroughRules();
    return (!((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(passThroughRules) && ((__qin_collection, __qin_value) => Array.isArray(__qin_collection) ? __qin_collection.some((__qin_item) => __qin_java_hash_key_equals__(__qin_item, __qin_value)) : __qin_collection.contains(__qin_value))(passThroughRules, ruleName));
  }
  recognizerPassThroughRules(): any {
    if (__qin_binary__("!=", this.__qin_field_recognizerPassThroughRules, null)) {
      return this.__qin_field_recognizerPassThroughRules;
    }
    this.__qin_field_recognizerPassThroughRules = this.parserRuntimePlan().recognizerPassThroughRules();
    return this.__qin_field_recognizerPassThroughRules;
  }
  isRecognizerTerminalLeafRule(ruleName: string): any {
    if ((this.__qin_field_buildCst || this.isErrorRecoveryMode())) {
      return false;
    }
    let terminalLeafRules: any = this.recognizerTerminalLeafRules();
    return (!((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(terminalLeafRules) && ((__qin_collection, __qin_value) => Array.isArray(__qin_collection) ? __qin_collection.some((__qin_item) => __qin_java_hash_key_equals__(__qin_item, __qin_value)) : __qin_collection.contains(__qin_value))(terminalLeafRules, ruleName));
  }
  recognizerTerminalLeafRules(): any {
    if (__qin_binary__("!=", this.__qin_field_recognizerTerminalLeafRules, null)) {
      return this.__qin_field_recognizerTerminalLeafRules;
    }
    this.__qin_field_recognizerTerminalLeafRules = this.parserRuntimePlan().recognizerTerminalLeafRules();
    return this.__qin_field_recognizerTerminalLeafRules;
  }
  recognizerLowYieldMemoRules(): any {
    return __QinJavaUtilSet.of();
  }
  isRecognizerLowYieldMemoRule(ruleName: string): any {
    if ((this.__qin_field_buildCst || this.isErrorRecoveryMode() || __qin_binary__("==", this.__qin_field_speculativeParseDepth, 0.0))) {
      return false;
    }
    let lowYieldRules: any = this.recognizerLowYieldMemoRules();
    return (!((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(lowYieldRules) && ((__qin_collection, __qin_value) => Array.isArray(__qin_collection) ? __qin_collection.some((__qin_item) => __qin_java_hash_key_equals__(__qin_item, __qin_value)) : __qin_collection.contains(__qin_value))(lowYieldRules, ruleName));
  }
  isAdaptiveLowYieldMemoRule(ruleName: string): any {
    return (!this.__qin_field_buildCst && !this.isErrorRecoveryMode() && __qin_binary__(">", this.__qin_field_speculativeParseDepth, 0.0) && ((__qin_collection, __qin_value) => Array.isArray(__qin_collection) ? __qin_collection.some((__qin_item) => __qin_java_hash_key_equals__(__qin_item, __qin_value)) : __qin_collection.contains(__qin_value))(this.__qin_field_adaptiveLowYieldMemoRules, ruleName));
  }
  recordAdaptiveRuleCacheHit(ruleName: string): any {
    if ((this.__qin_field_buildCst || this.isErrorRecoveryMode() || __qin_binary__("==", ruleName, null))) {
      return null;
    }
    this.__qin_field_adaptiveRuleCacheHitCounts.merge(ruleName, 1.0, ((...__qin_args) => { const __qin_method = __QinJavaLangInteger.sum; if (typeof __qin_method === "function") { return __qin_method.apply(__QinJavaLangInteger, __qin_args); } const __qin_receiver = __qin_args[0]; return __qin_receiver.sum(...__qin_args.slice(1)); }));
    return null;
  }
  recordAdaptiveRuleCachePut(ruleName: string): any {
    if ((this.__qin_field_buildCst || this.isErrorRecoveryMode() || __qin_binary__("==", ruleName, null) || ((__qin_collection, __qin_value) => Array.isArray(__qin_collection) ? __qin_collection.some((__qin_item) => __qin_java_hash_key_equals__(__qin_item, __qin_value)) : __qin_collection.contains(__qin_value))(this.__qin_field_adaptiveLowYieldMemoRules, ruleName))) {
      return null;
    }
    let hits: any = this.__qin_field_adaptiveRuleCacheHitCounts.getOrDefault(ruleName, 0.0);
    if (__qin_binary__(">", hits, 0.0)) {
      return null;
    }
    let puts: any = this.__qin_field_adaptiveRuleCachePutCounts.merge(ruleName, 1.0, ((...__qin_args) => { const __qin_method = __QinJavaLangInteger.sum; if (typeof __qin_method === "function") { return __qin_method.apply(__QinJavaLangInteger, __qin_args); } const __qin_receiver = __qin_args[0]; return __qin_receiver.sum(...__qin_args.slice(1)); }));
    if (__qin_binary__(">=", puts, com_subhuti_parser_SubhutiParserCore.__qin_field_ADAPTIVE_LOW_YIELD_MEMO_MIN_PUTS)) {
      this.__qin_field_adaptiveLowYieldMemoRules.add(ruleName);
    }
    return null;
  }
  executeRuleCoreNoCst(ruleName: string, targetFun: any): any {
    const __qin_functional_targetFun_1 = __qin_java_functional(targetFun);
    this.__qin_field_ruleCoreExecutions++;
    this.incrementRuleProfile(this.__qin_field_ruleCoreExecutionCounts, ruleName);
    let result: any = __qin_functional_targetFun_1.get();
    if (this.__qin_field_parseSuccess) {
      this.incrementRuleProfile(this.__qin_field_ruleCoreSuccessCounts, ruleName);
    } else {
      this.incrementRuleProfile(this.__qin_field_ruleCoreFailureCounts, ruleName);
      if (__qin_binary__("!=", this.__qin_field_errorHandler, null)) {
        this.__qin_field_errorHandler.recordRuleFailure(this.__qin_field_currentPosition, ruleName);
      }
    }
    return result;
  }
  executeRuleCore(ruleName: string, targetFun: any): any {
    const __qin_functional_targetFun_1 = __qin_java_functional(targetFun);
    this.__qin_field_ruleCoreExecutions++;
    this.incrementRuleProfile(this.__qin_field_ruleCoreExecutionCounts, ruleName);
    this.__qin_field_ruleCstNodes++;
    let cst: any = com_subhuti_struct_SubhutiCst.builder().name(ruleName).build();
    this.__qin_field_cstStack.add(cst);
    try {
      let result: any = __qin_functional_targetFun_1.get();
      if (this.__qin_field_parseSuccess) {
        this.incrementRuleProfile(this.__qin_field_ruleCoreSuccessCounts, ruleName);
      } else {
        this.incrementRuleProfile(this.__qin_field_ruleCoreFailureCounts, ruleName);
      }
      let finalCst: any = ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(this.__qin_field_cstStack, __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_cstStack), 1.0));
      this.__qin_field_cstStack.remove(__qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_cstStack), 1.0));
      if (this.__qin_field_parseSuccess) {
        this.setLocation(finalCst);
        let parentCst: any = this.getCurCst();
        if (__qin_binary__("!=", parentCst, null)) {
          parentCst.addChild(finalCst);
        } else {
          this.__qin_field_rootCst = finalCst;
        }
      } else {
        if (__qin_binary__("!=", this.__qin_field_errorHandler, null)) {
          this.__qin_field_errorHandler.recordRuleFailure(this.__qin_field_currentPosition, ruleName);
        }
      }
      return new com_subhuti_parser_SubhutiParserCore$RuleExecutionResult(result, finalCst);
    } catch (e) {
      if (!(e instanceof __QinJavaLangException)) {
        throw e;
      }
      this.__qin_field_cstStack.remove(__qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_cstStack), 1.0));
      throw e;
    }
    return null;
  }
  applyCachedResult(cached: any): any {
    if ((!(() => { const __qin_instanceof_value = cached; return __qin_instanceof__(__qin_instanceof_value, com_subhuti_cache_SubhutiPackratCacheResult); })())) {
      return (cached);
    }
    let cacheResult: any = (cached);
    if ((__qin_binary__("!=", cacheResult.__qin_field_parsedTokens, null) && !((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(cacheResult.__qin_field_parsedTokens))) {
      this.__qin_field_parsedTokens.addAll(cacheResult.__qin_field_parsedTokens);
    }
    this.__qin_field_tokenCursor = cacheResult.__qin_field_endTokenIndex;
    if (__qin_binary__(">=", cacheResult.__qin_field_endCodeIndex, 0.0)) {
      this.__qin_field_currentIndex = cacheResult.__qin_field_endCodeIndex;
      this.__qin_field_currentPosition = com_subhuti_struct_SubhutiPosition.of(cacheResult.__qin_field_endLine, cacheResult.__qin_field_endColumn, cacheResult.__qin_field_endCodeIndex);
      this.__qin_field_lastTokenName = cacheResult.__qin_field_lastTokenName;
    } else {
      if ((__qin_binary__("!=", cacheResult.__qin_field_parsedTokens, null) && !((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(cacheResult.__qin_field_parsedTokens))) {
        let lastToken: any = ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(cacheResult.__qin_field_parsedTokens, __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(cacheResult.__qin_field_parsedTokens), 1.0));
        this.__qin_field_currentIndex = lastToken.endOffset();
        this.__qin_field_currentPosition = lastToken.endPosition();
        this.__qin_field_lastTokenName = lastToken.tokenName();
        this.__qin_field_tokenCursor = (this.__qin_field_preTokenizedDefaultModeInput ? this.tokenOrdinalAtCodeIndex(this.__qin_field_currentIndex) : cacheResult.__qin_field_endTokenIndex);
      }
    }
    this.__qin_field_parseSuccess = cacheResult.__qin_field_parseSuccess;
    if ((this.__qin_field_buildCst && cacheResult.__qin_field_parseSuccess && (() => { const __qin_instanceof_value = cacheResult.__qin_field_cst; return __qin_instanceof__(__qin_instanceof_value, com_subhuti_struct_SubhutiCst); })())) {
      let cachedCst: any = (cacheResult.__qin_field_cst);
      let parentCst: any = (((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(this.__qin_field_cstStack) ? null : ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(this.__qin_field_cstStack, __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_cstStack), 1.0)));
      if (__qin_binary__("!=", parentCst, null)) {
        this.__qin_field_cachedCstAttachCount++;
        parentCst.addChild(cachedCst);
      } else {
        this.__qin_field_cachedCstAttachCount++;
        this.__qin_field_rootCst = cachedCst;
      }
    }
    return (cacheResult.__qin_field_cst);
  }
  throwLoopError(ruleName: string): any {
    if (this.__qin_field_analysisMode) {
      this.setParseFail();
      return null;
    }
    let currentToken: any = this.LA(1.0);
    let tokenContext: any = this.getTokenContext(2.0);
    let cacheStats: any = this.getCacheStats();
    let ruleStack: any = this.getRuleStack();
    let isLeftRecursion: any = this.isDirectLeftRecursion(ruleName, ruleStack);
    let errorType: any = (isLeftRecursion ? "left-recursion" : "or-branch-shadowing");
    let hint: any = "检查规则定义，确保在递归前消费了 token";
    let errorMessage: any = __QinJavaLangString.format("[%s] Rule '%s' detected infinite loop at token[%d]\nHint: %s", errorType, ruleName, this.currentTokenIndex(), hint);
    throw new __QinJavaLangRuntimeException(errorMessage);
  }
  isDirectLeftRecursion(ruleName: string, ruleStack: any): any {
    let ruleCounts: any = new __QinJavaUtilHashMap();
    for (const rule of ruleStack) {
      ruleCounts.put(rule, __qin_binary__("+", ruleCounts.getOrDefault(rule, 0.0), 1.0));
    }
    for (const count of ruleCounts.values()) {
      if (__qin_binary__(">=", count, 2.0)) {
        return true;
      }
    }
    return false;
  }
  handleTopLevelError(ruleName: string, startTokenIndex: number): any {
    if (this.__qin_field_analysisMode) {
      return null;
    }
    let noTokenConsumed: any = __qin_binary__("==", this.currentTokenIndex(), startTokenIndex);
    let found: any = this.LA(1.0);
    let expected: any = (noTokenConsumed ? "valid syntax" : "EOF (end of file)");
    let foundStr: any = (__qin_binary__("!=", found, null) ? found.tokenName() : "EOF");
    let errorMessage: any = __QinJavaLangString.format("Parsing Error at token[%d] line %d:%d: Expected %s, found %s", this.currentTokenIndex(), this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), expected, foundStr);
    let stack: any = this.getRuleStack();
    if (((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(stack)) {
      stack = __QinJavaUtilList.of(ruleName);
    }
    errorMessage += ("\nRule stack: " + __QinJavaLangString.join(" -> ", stack));
    throw new __QinJavaLangRuntimeException(errorMessage);
  }
  getTokenContext(contextSize: number): any {
    return new __QinJavaUtilArrayList(((__qin_collection, __qin_from, __qin_to) => new __QinJavaUtilArrayList((Array.isArray(__qin_collection) ? __qin_collection : __qin_collection.toArray()).slice(Number(__qin_from), Number(__qin_to))))(this.__qin_field_parsedTokens, Math.max(0.0, __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parsedTokens), contextSize)), ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parsedTokens)));
  }
  isEof(): any {
    if ((this.__qin_field_preTokenizedDefaultModeInput && (__qin_binary__("==", this.__qin_field_currentMode, null) || com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE.equals(this.__qin_field_currentMode)))) {
      let entry: any = this.preTokenizedEntryAtParsedOrdinal(1.0, com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE);
      if (__qin_binary__("!=", entry, null)) {
        let token: any = entry.getToken();
        return (__qin_binary__("==", token, null) || token.isEof());
      }
      if (__qin_binary__(">=", this.__qin_field_tokenCursor, this.__qin_field_preTokenizedEntriesByOrdinal.length)) {
        return true;
      }
    }
    if (__qin_binary__(">=", this.__qin_field_currentIndex, __QinJavaLangString.length(this.__qin_field_sourceCode))) {
      return true;
    }
    if (this.__qin_field_firstTokenRecording) {
      throw new com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException();
    }
    let entry: any = this._getOrParseTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), this.__qin_field_currentMode);
    let token: any = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
    return (__qin_binary__("==", token, null) || token.isEof());
  }
  LA(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_LA_1_0(__qin_args[0]);
    if (__qin_args.length === 2 && typeof __qin_args[0] === "number" && (__qin_args[1] === null || Array.isArray(__qin_args[1]) || __qin_args[1] instanceof __QinJavaUtilArrayList || __qin_args[1] instanceof __QinJavaUtilUnmodifiableList)) return this.__qin_overload_LA_2_1(__qin_args[0], (Array.isArray(__qin_args[1]) ? new __QinJavaUtilArrayList(__qin_args[1]) : __qin_args[1]));
    throw new Error("Unsupported Java overload: LA/" + __qin_args.length);
  }
  __qin_overload_LA_1_0(offset: number): any {
    if (this.__qin_field_firstTokenRecording) {
      throw new com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException();
    }
    if (__qin_binary__("<", offset, 1.0)) {
      throw new __QinJavaLangIllegalArgumentException("offset must be >= 1");
    }
    let tokenArrayEntry: any = this.preTokenizedEntryAtParsedOrdinal(offset, this.__qin_field_currentMode);
    if (__qin_binary__("!=", tokenArrayEntry, null)) {
      this.__qin_field_tokenStreamGets++;
      this.__qin_field_tokenStreamHits++;
      return tokenArrayEntry.getToken();
    }
    if ((this.__qin_field_preTokenizedDefaultModeInput && (__qin_binary__("==", this.__qin_field_currentMode, null) || com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE.equals(this.__qin_field_currentMode)) && __qin_binary__(">=", __qin_binary__("-", __qin_binary__("+", this.__qin_field_tokenCursor, offset), 1.0), this.__qin_field_preTokenizedEntriesByOrdinal.length))) {
      return null;
    }
    if ((!this.__qin_field_preTokenizedDefaultModeInput && !this.isErrorRecoveryMode() && !this.__qin_field_lexer.dependsOnPreviousTokenName())) {
      let entry: any = this.sourceLookaheadEntry(offset);
      return (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
    }
    if ((!this.__qin_field_buildCst && !this.isErrorRecoveryMode() && __qin_binary__("==", offset, 1.0))) {
      let preTokenizedEntry: any = this.preTokenizedCurrentEntryWithoutCounters(this.__qin_field_currentMode);
      if (__qin_binary__("!=", preTokenizedEntry, null)) {
        return preTokenizedEntry.getToken();
      }
      let entry: any = this._getOrParseTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), this.__qin_field_currentMode);
      return (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
    }
    let index: any = this.__qin_field_currentIndex;
    let pos: any = this.__qin_field_currentPosition;
    for (let i: any = 0.0; __qin_binary__("<", i, offset); i++) {
      let entry: any = this._getOrParseTokenEntry(index, pos.line(), pos.column(), this.__qin_field_currentMode);
      let token: any = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
      if ((__qin_binary__("==", token, null) || token.isEof())) {
        return token;
      }
      if (__qin_binary__("<", i, __qin_binary__("-", offset, 1.0))) {
        index = entry.getNextCodeIndex();
        pos = com_subhuti_struct_SubhutiPosition.of(entry.getNextLine(), entry.getNextColumn(), entry.getNextCodeIndex());
      } else {
        return token;
      }
    }
    return null;
  }
  __qin_overload_LA_2_1(offset: number, modes: any): any {
    if (this.__qin_field_firstTokenRecording) {
      throw new com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException();
    }
    if (__qin_binary__("<", offset, 1.0)) {
      throw new __QinJavaLangIllegalArgumentException("offset must be >= 1");
    }
    let index: any = this.__qin_field_currentIndex;
    let pos: any = this.__qin_field_currentPosition;
    for (let i: any = 0.0; __qin_binary__("<", i, offset); i++) {
      let entry: any = null;
      let token: any = null;
      for (const mode of modes) {
        entry = this._getOrParseTokenEntry(index, pos.line(), pos.column(), mode);
        token = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
        if (__qin_binary__("!=", token, null)) {
          break;
        }
      }
      if ((__qin_binary__("==", token, null) || token.isEof())) {
        return token;
      }
      if (__qin_binary__("<", i, __qin_binary__("-", offset, 1.0))) {
        index = entry.getNextCodeIndex();
        pos = com_subhuti_struct_SubhutiPosition.of(entry.getNextLine(), entry.getNextColumn(), entry.getNextCodeIndex());
      } else {
        return token;
      }
    }
    return null;
  }
  sourceLookaheadEntry(offset: number): any {
    if ((!this.sourceLookaheadMatchesCurrentState())) {
      this.resetSourceLookahead();
    }
    if (__qin_binary__("<=", offset, this.__qin_field_sourceLookaheadSize)) {
      this.__qin_field_sourceLookaheadCacheHits++;
      return this.__qin_field_sourceLookaheadEntries[__qin_binary__("-", offset, 1.0)];
    }
    if (this.__qin_field_sourceLookaheadTerminalReached) {
      this.__qin_field_sourceLookaheadCacheHits++;
      return this.__qin_field_sourceLookaheadTerminalEntry;
    }
    let index: any = this.__qin_field_currentIndex;
    let line: any = this.__qin_field_currentPosition.line();
    let column: any = this.__qin_field_currentPosition.column();
    if (__qin_binary__(">", this.__qin_field_sourceLookaheadSize, 0.0)) {
      let previous: any = this.__qin_field_sourceLookaheadEntries[__qin_binary__("-", this.__qin_field_sourceLookaheadSize, 1.0)];
      if ((__qin_binary__("==", previous, null) || __qin_binary__("==", previous.getToken(), null) || previous.getToken().isEof())) {
        this.__qin_field_sourceLookaheadTerminalReached = true;
        this.__qin_field_sourceLookaheadTerminalEntry = previous;
        return previous;
      }
      index = previous.getNextCodeIndex();
      line = previous.getNextLine();
      column = previous.getNextColumn();
    }
    while (__qin_binary__("<", this.__qin_field_sourceLookaheadSize, offset)) {
      let entry: any = this._getOrParseTokenEntry(index, line, column, this.__qin_field_currentMode);
      this.ensureSourceLookaheadCapacity(__qin_binary__("+", this.__qin_field_sourceLookaheadSize, 1.0));
      this.__qin_field_sourceLookaheadEntries[this.__qin_field_sourceLookaheadSize++] = entry;
      this.__qin_field_sourceLookaheadCacheFills++;
      let token: any = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
      if ((__qin_binary__("==", token, null) || token.isEof())) {
        this.__qin_field_sourceLookaheadTerminalReached = true;
        this.__qin_field_sourceLookaheadTerminalEntry = entry;
        break;
      }
      index = entry.getNextCodeIndex();
      line = entry.getNextLine();
      column = entry.getNextColumn();
    }
    return (__qin_binary__("<=", offset, this.__qin_field_sourceLookaheadSize) ? this.__qin_field_sourceLookaheadEntries[__qin_binary__("-", offset, 1.0)] : this.__qin_field_sourceLookaheadTerminalEntry);
  }
  sourceLookaheadMatchesCurrentState(): any {
    return (this.__qin_field_sourceLookaheadInitialized && __qin_binary__("==", this.__qin_field_sourceLookaheadStartIndex, this.__qin_field_currentIndex) && __qin_binary__("==", this.__qin_field_sourceLookaheadStartLine, this.__qin_field_currentPosition.line()) && __qin_binary__("==", this.__qin_field_sourceLookaheadStartColumn, this.__qin_field_currentPosition.column()) && __QinJavaUtilObjects.equals(this.__qin_field_sourceLookaheadMode, this.__qin_field_currentMode));
  }
  resetSourceLookahead(): any {
    if (this.__qin_field_sourceLookaheadInitialized) {
      this.__qin_field_sourceLookaheadCacheResets++;
    }
    this.__qin_field_sourceLookaheadInitialized = true;
    this.__qin_field_sourceLookaheadStartIndex = this.__qin_field_currentIndex;
    this.__qin_field_sourceLookaheadStartLine = this.__qin_field_currentPosition.line();
    this.__qin_field_sourceLookaheadStartColumn = this.__qin_field_currentPosition.column();
    this.__qin_field_sourceLookaheadMode = this.__qin_field_currentMode;
    this.__qin_field_sourceLookaheadSize = 0.0;
    this.__qin_field_sourceLookaheadTerminalReached = false;
    this.__qin_field_sourceLookaheadTerminalEntry = null;
    return null;
  }
  ensureSourceLookaheadCapacity(capacity: number): any {
    if (__qin_binary__("<=", capacity, this.__qin_field_sourceLookaheadEntries.length)) {
      return null;
    }
    let nextCapacity: any = Math.max(capacity, __qin_binary__("*", this.__qin_field_sourceLookaheadEntries.length, 2.0));
    this.__qin_field_sourceLookaheadEntries = __QinJavaUtilArrays.copyOf(this.__qin_field_sourceLookaheadEntries, nextCapacity);
    return null;
  }
  cache(enable: boolean): any {
    this.__qin_field_enableMemoization = enable;
    return this;
  }
  debug(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && typeof __qin_args[0] === "boolean") return this.__qin_overload_debug_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_debug_0_1();
    throw new Error("Unsupported Java overload: debug/" + __qin_args.length);
  }
  __qin_overload_debug_1_0(enable: boolean): any {
    this.__qin_field_debugMode = enable;
    if (enable) {
      if (__qin_binary__("==", this.__qin_field__debugger, null)) {
        this.__qin_field__debugger = new com_subhuti_debug_SubhutiTraceDebugger(this.__qin_field_parsedTokens);
      }
    } else {
      this.__qin_field__debugger = null;
    }
    return this;
  }
  __qin_overload_debug_0_1(): any {
    return this.debug(true);
  }
  recordParsedTokenForState(token: com_subhuti_struct_SubhutiMatchToken): any {
    if (this.shouldStoreParsedTokens()) {
      this.__qin_field_parsedTokens.add(token);
    }
    return null;
  }
  cst(enable: boolean): any {
    this.__qin_field_buildCst = enable;
    return this;
  }
  isCstEnabled(): any {
    return this.__qin_field_buildCst;
  }
  setLogFile(filePath: string): any {
    if (__qin_binary__("==", this.__qin_field__debugger, null)) {
      this.debug(true);
    }
    this.__qin_field__debugger.setLogFile(filePath);
    return this;
  }
  getDebugger(): any {
    return this.__qin_field__debugger;
  }
  reset(): any {
    this.initTopLevelData();
    this.__qin_field_tokenCache.clear();
    this.clearRuleCacheIfAllocated();
    return null;
  }
  getCacheStats(): any {
    return this.ruleCacheStatsReport();
  }
  getRuntimePlanReport(): any {
    return (__qin_binary__("==", this.staticGrammarPlan(), null) ? this.parserRuntimePlan().coverageReport().toString() : this.staticGrammarPlan().coverageReport(this.getClass().getName()));
  }
  getStaticDecisionReport(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_getStaticDecisionReport_1_0(__qin_args[0]);
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && typeof __qin_args[2] === "number") return this.__qin_overload_getStaticDecisionReport_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: getStaticDecisionReport/" + __qin_args.length);
  }
  __qin_overload_getStaticDecisionReport_1_0(decisionId: number): any {
    return this.staticGrammarPlan().decisionPlan(decisionId).toString();
  }
  __qin_overload_getStaticDecisionReport_3_1(ruleId: number, variantId: number, occurrenceId: number): any {
    let plan: any = this.staticGrammarPlan();
    for (let decisionId: any = 0.0; __qin_binary__("<", decisionId, plan.decisionCount()); decisionId++) {
      let decision: any = plan.decisionPlan(decisionId);
      if ((__qin_binary__("==", decision.ruleId(), ruleId) && __qin_binary__("==", decision.variantId(), variantId) && __qin_binary__("==", decision.occurrenceId(), occurrenceId))) {
        return ("decisionId=" + decisionId + ", " + decision);
      }
    }
    return "none";
  }
  getStaticDecisionMatchReport(decisionId: number): any {
    let decision: any = this.staticGrammarPlan().decisionPlan(decisionId);
    let depth: any = Math.max(1.0, ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(decision.lexerModes()));
    let tokens: any = [];
    for (let index: any = 0.0; __qin_binary__("<", index, depth); index++) {
      tokens[index] = this.LA(__qin_binary__("+", index, 1.0));
    }
    return (decision + ", tokens=" + __QinJavaUtilArrays.toString(tokens) + ", candidates=" + decision.candidates(tokens, tokens.length) + ", selected=" + decision.select(tokens, tokens.length));
  }
  getLastStaticNoMatchReport(): any {
    return (__qin_binary__("<", this.__qin_field_lastStaticNoMatchDecisionId, 0.0) ? "none" : ("decisionId=" + this.__qin_field_lastStaticNoMatchDecisionId + ", " + this.getStaticDecisionMatchReport(this.__qin_field_lastStaticNoMatchDecisionId)));
  }
  getLastStaticAmbiguousReport(): any {
    return (__qin_binary__("<", this.__qin_field_lastStaticAmbiguousDecisionId, 0.0) ? "none" : ("decisionId=" + this.__qin_field_lastStaticAmbiguousDecisionId + ", " + this.getStaticDecisionMatchReport(this.__qin_field_lastStaticAmbiguousDecisionId)));
  }
  getActiveStaticRuleReport(): any {
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      return "none";
    }
    let report: any = new __QinJavaLangStringBuilder();
    for (let frame: any = 0.0; __qin_binary__("<", frame, this.__qin_field_activeStaticRuleDepth); frame++) {
      if (__qin_binary__(">", frame, 0.0)) {
        report.append(" -> ");
      }
      report.append(this.staticGrammarPlan().staticRuleInvocationPlan(this.__qin_field_activeStaticRuleIds[frame], this.__qin_field_activeStaticVariantIds[frame]).ruleName());
    }
    return report.toString();
  }
  getCoreProfileStats(): any {
    return ("ruleWrapperCalls=" + this.__qin_field_ruleWrapperCalls + ", ruleCoreExecutions=" + this.__qin_field_ruleCoreExecutions + ", ruleCacheKeyBuilds=" + this.__qin_field_ruleCacheKeyBuilds + ", ruleCacheHits=" + this.__qin_field_ruleCacheHits + ", ruleCachePuts=" + this.__qin_field_ruleCachePuts + ", ruleCachePassThroughSkips=" + this.__qin_field_ruleCachePassThroughSkips + ", ruleCacheTerminalLeafSkips=" + this.__qin_field_ruleCacheTerminalLeafSkips + ", ruleCacheSpeculativeSkips=" + this.__qin_field_ruleCacheSpeculativeSkips + ", ruleCacheLowYieldSkips=" + this.__qin_field_ruleCacheLowYieldSkips + ", ruleCacheAdaptiveLowYieldSkips=" + this.__qin_field_ruleCacheAdaptiveLowYieldSkips + ", ruleWrapperPassThroughSkips=" + this.__qin_field_ruleWrapperPassThroughSkips + ", ruleWrapperTerminalLeafSkips=" + this.__qin_field_ruleWrapperTerminalLeafSkips + ", ruleWrapperRuleChainSkips=" + this.__qin_field_ruleWrapperRuleChainSkips + ", ruleWrapperDirectTerminalSkips=" + this.__qin_field_ruleWrapperDirectTerminalSkips + ", ruleWrapperDirectTerminalSequenceSkips=" + this.__qin_field_ruleWrapperDirectTerminalSequenceSkips + ", ruleWrapperDirectRecognizerPlanSkips=" + this.__qin_field_ruleWrapperDirectRecognizerPlanSkips + ", indexedRuleInvocationLookups=" + this.__qin_field_indexedRuleInvocationLookups + ", staticPrimitiveInvocationEntries=" + this.__qin_field_staticPrimitiveInvocationEntries + ", staticRootRuleEntries=" + this.__qin_field_staticRootRuleEntries + ", staticGateDispatches=" + this.__qin_field_staticGateDispatches + ", staticActionDispatches=" + this.__qin_field_staticActionDispatches + ", staticActionRetryRestores=" + this.__qin_field_staticActionRetryRestores + ", staticBranchSelectorDispatches=" + this.__qin_field_staticBranchSelectorDispatches + ", staticBranchSelectorNoMatches=" + this.__qin_field_staticBranchSelectorNoMatches + ", lastStaticNoMatchDecisionId=" + this.__qin_field_lastStaticNoMatchDecisionId + ", lastStaticAmbiguousDecisionId=" + this.__qin_field_lastStaticAmbiguousDecisionId + ", lastStaticSharedPrefixSelection=" + this.__qin_field_lastStaticSharedPrefixSelection + ", lastStaticSharedPrefixNoMatch=" + this.__qin_field_lastStaticSharedPrefixNoMatch + ", staticBranchSelectorAmbiguities=" + this.__qin_field_staticBranchSelectorAmbiguities + ", staticDecisionCandidateExecutions=" + this.__qin_field_staticDecisionCandidateExecutions + ", staticDecisionSkippedBranches=" + this.__qin_field_staticDecisionSkippedBranches + ", staticDecisionGateSkips=" + this.__qin_field_staticDecisionGateSkips + ", staticDecisionStateSaves=" + this.__qin_field_staticDecisionStateSaves + ", staticDecisionStateSaveSkips=" + this.__qin_field_staticDecisionStateSaveSkips + ", staticDecisionDirectManyExecutions=" + this.__qin_field_staticDecisionDirectManyExecutions + ", staticCoreRuleEntries=" + this.__qin_field_staticCoreRuleEntries + ", staticRuleLoopRejects=" + this.__qin_field_staticRuleLoopRejects + ", staticNonNullableEmptyRejects=" + this.__qin_field_staticNonNullableEmptyRejects + ", lastStaticNonNullableEmptyRule=" + this.__qin_field_lastStaticNonNullableEmptyRule + ", staticExecutionModeBindings=" + this.__qin_field_staticExecutionModeBindings + ", staticExecutionMode=" + this.__qin_field_staticExecutionModeName + ", parserRuntimePlanBuilds=" + this.__qin_field_parserRuntimePlanBuilds + ", parserRuntimePlanCacheHits=" + this.__qin_field_parserRuntimePlanCacheHits + ", tokenCacheGets=" + this.__qin_field_tokenCacheGets + ", tokenCacheHits=" + this.__qin_field_tokenCacheHits + ", tokenCacheMisses=" + this.__qin_field_tokenCacheMisses + ", tokenCachePuts=" + this.__qin_field_tokenCachePuts + ", tokenStreamGets=" + this.__qin_field_tokenStreamGets + ", tokenStreamHits=" + this.__qin_field_tokenStreamHits + ", currentTokenEntryCacheHits=" + this.__qin_field_currentTokenEntryCacheHits + ", sourceLookaheadCacheHits=" + this.__qin_field_sourceLookaheadCacheHits + ", sourceLookaheadCacheFills=" + this.__qin_field_sourceLookaheadCacheFills + ", sourceLookaheadCacheResets=" + this.__qin_field_sourceLookaheadCacheResets + ", lexerTokenDefinitions=" + this.__qin_field_lexer.getTokenDefinitionCount() + ", lexerExactFixedTokenDefinitions=" + this.__qin_field_lexer.getExactFixedTokenDefinitionCount() + ", lexerCandidateTokenChecks=" + this.__qin_field_lexer.getCandidateTokenChecks() + ", lexerPatternMatchAttempts=" + this.__qin_field_lexer.getPatternMatchAttempts() + ", preTokenizedRegexpNegativeHits=" + this.__qin_field_preTokenizedRegexpNegativeHits + ", preTokenizedFallbackModes=" + this.formatTopRuleCounts(this.__qin_field_preTokenizedFallbackModeCounts, 8.0) + ", cstOutputEnabled=" + this.__qin_field_buildCst + ", tokenCursor=" + this.__qin_field_tokenCursor + ", parsedTokenListSize=" + ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parsedTokens) + ", tokenCstNodes=" + this.__qin_field_tokenCstNodes + ", ruleCstNodes=" + this.__qin_field_ruleCstNodes + ", cstParentRebuilds=" + this.__qin_field_cstParentRebuilds + ", cachedCstAttachCount=" + this.__qin_field_cachedCstAttachCount);
  }
  getCoreHotRuleStats(limit: number): any {
    if ((!this.__qin_field_coreRuleProfileEnabled)) {
      return "disabled";
    }
    return ("core=" + this.formatTopRuleCounts(this.__qin_field_ruleCoreExecutionCounts, limit) + "; success=" + this.formatTopRuleCounts(this.__qin_field_ruleCoreSuccessCounts, limit) + "; failure=" + this.formatTopRuleCounts(this.__qin_field_ruleCoreFailureCounts, limit) + "; wrappers=" + this.formatTopRuleCounts(this.__qin_field_ruleWrapperCallCounts, limit) + "; passThroughRules=" + this.formatTopRuleCounts(this.__qin_field_ruleWrapperPassThroughCounts, limit) + "; terminalLeafRules=" + this.formatTopRuleCounts(this.__qin_field_ruleWrapperTerminalLeafCounts, limit) + "; ruleChainPassThroughRules=" + this.formatTopRuleCounts(this.__qin_field_ruleWrapperRuleChainCounts, limit) + "; cacheHits=" + this.formatTopRuleCounts(this.__qin_field_ruleCacheHitCounts, limit) + "; cachePuts=" + this.formatTopRuleCounts(this.__qin_field_ruleCachePutCounts, limit) + "; cacheWork=" + this.formatTopCacheWork(limit) + "; cacheHitSpan=" + this.formatTopCacheHitSpans(limit) + "; failureRate=" + this.formatTopFailureRates(limit) + "; orStateSaves=" + this.formatTopRuleCounts(this.__qin_field_orPredictionStateSaveRuleCounts, limit) + "; orStateSaveSkips=" + this.formatTopRuleCounts(this.__qin_field_orPredictionStateSaveSkipRuleCounts, limit) + "; orCandidates=" + this.formatTopOrCandidateCounts(limit));
  }
  getStaticDecisionRetryProfile(limit: number): any {
    return (this.__qin_field_staticDecisionProfileEnabled ? this.formatTopRuleCounts(this.__qin_field_staticDecisionRetryCounts, limit) : "disabled");
  }
  recordStaticDecisionRetry(decision: com_subhuti_parser_SubhutiDecisionPlan): any {
    if ((!this.__qin_field_staticDecisionProfileEnabled || __qin_binary__("==", decision, null))) {
      return null;
    }
    let key: any = ("decision=" + decision.decisionId(this.staticGrammarPlan()) + " address=" + decision.ruleId() + ":" + decision.variantId() + ":" + decision.occurrenceId() + " kind=" + decision.kind());
    this.__qin_field_staticDecisionRetryCounts.merge(key, 1.0, ((...__qin_args) => { const __qin_method = __QinJavaLangLong.sum; if (typeof __qin_method === "function") { return __qin_method.apply(__QinJavaLangLong, __qin_args); } const __qin_receiver = __qin_args[0]; return __qin_receiver.sum(...__qin_args.slice(1)); }));
    return null;
  }
  incrementRuleProfile(counts: any, ruleName: string): any {
    if ((!this.__qin_field_coreRuleProfileEnabled)) {
      return null;
    }
    counts.put(ruleName, __qin_binary__("+", counts.getOrDefault(ruleName, 0.0), 1.0));
    return null;
  }
  incrementCacheHitTokenSpan(ruleName: string, cached: any, startTokenIndex: number): any {
    let cacheResult: any = null;
    if ((!this.__qin_field_coreRuleProfileEnabled || !(() => { const __qin_pattern_value = cached; return __qin_instanceof__(__qin_pattern_value, com_subhuti_cache_SubhutiPackratCacheResult) && (cacheResult = __qin_pattern_value, true); })())) {
      return null;
    }
    let span: any = Math.max(0.0, __qin_binary__("-", cacheResult.__qin_field_endTokenIndex, startTokenIndex));
    this.__qin_field_ruleCacheHitTokenSpanTotals.put(ruleName, __qin_binary__("+", this.__qin_field_ruleCacheHitTokenSpanTotals.getOrDefault(ruleName, 0.0), span));
    return null;
  }
  incrementOrPredictionStateSaveProfile(saved: boolean): any {
    if ((!this.__qin_field_coreRuleProfileEnabled)) {
      return null;
    }
    let ruleName: any = this.activeStaticRuleScopeName();
    if (__qin_binary__("==", ruleName, null)) {
      ruleName = this.__qin_field_activeRuleProfileStack.peekLast();
    }
    if (__qin_binary__("==", ruleName, null)) {
      ruleName = "<top-level-or>";
    }
    this.incrementRuleProfile((saved ? this.__qin_field_orPredictionStateSaveRuleCounts : this.__qin_field_orPredictionStateSaveSkipRuleCounts), ruleName);
    return null;
  }
  formatTopRuleCounts(counts: any, limit: number): any {
    if (counts.isEmpty()) {
      return "[]";
    }
    let safeLimit: any = Math.max(1.0, limit);
    return __QinJavaUtilArrays.stream(counts.entrySet()).sorted(__qin_java_functional((left, right) => {
      let countCompare: any = __QinJavaLangLong.compare(right.getValue(), left.getValue());
      if (__qin_binary__("!=", countCompare, 0.0)) {
        return countCompare;
      }
      return left.getKey().compareTo(right.getKey());
    })).limit(safeLimit).map(__qin_java_functional((entry) => {
      return (entry.getKey() + "=" + entry.getValue());
    })).collect(__QinJavaUtilStreamCollectors.joining(", ", "[", "]"));
  }
  recordPreTokenizedFallbackMode(mode: com_subhuti_struct_LexerMode): any {
    if ((!this.__qin_field_coreRuleProfileEnabled || !this.__qin_field_preTokenizedDefaultModeInput)) {
      return null;
    }
    let normalizedMode: any = (__qin_binary__("==", mode, null) ? com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE : mode);
    let modeName: any = (normalizedMode.isDefault() ? "DEFAULT_MODE" : normalizedMode.toString());
    this.__qin_field_preTokenizedFallbackModeCounts.merge(modeName, 1.0, ((...__qin_args) => { const __qin_method = __QinJavaLangLong.sum; if (typeof __qin_method === "function") { return __qin_method.apply(__QinJavaLangLong, __qin_args); } const __qin_receiver = __qin_args[0]; return __qin_receiver.sum(...__qin_args.slice(1)); }));
    return null;
  }
  preTokenizedRegexpNegativeEntry(index: number, mode: com_subhuti_struct_LexerMode): any {
    if ((!this.__qin_field_preTokenizedDefaultModeInput || !com_subhuti_struct_LexerMode.__qin_field_REGEXP.equals(mode))) {
      return null;
    }
    let defaultEntry: any = this.preTokenizedEntryAt(index, com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE);
    let token: any = (__qin_binary__("!=", defaultEntry, null) ? defaultEntry.getToken() : null);
    let tokenStart: any = (__qin_binary__("!=", token, null) ? token.index() : null);
    if ((__qin_binary__("==", tokenStart, null) || __qin_binary__("<", tokenStart, 0.0) || __qin_binary__(">=", tokenStart, __QinJavaLangString.length(this.__qin_field_sourceCode)))) {
      return null;
    }
    return (__qin_binary__("==", __QinJavaLangString.charAt(this.__qin_field_sourceCode, tokenStart), "/") ? null : defaultEntry);
  }
  formatTopCacheWork(limit: number): any {
    if ((this.__qin_field_ruleCachePutCounts.isEmpty() && this.__qin_field_ruleCacheHitCounts.isEmpty())) {
      return "[]";
    }
    let safeLimit: any = Math.max(1.0, limit);
    let ruleNames: any = new __QinJavaUtilHashSet();
    ruleNames.addAll(this.__qin_field_ruleCachePutCounts.keySet());
    ruleNames.addAll(this.__qin_field_ruleCacheHitCounts.keySet());
    return __QinJavaUtilArrays.stream(ruleNames).map(__qin_java_functional((ruleName) => {
      return new com_subhuti_parser_SubhutiParserCore$CacheWork(ruleName, this.__qin_field_ruleCacheHitCounts.getOrDefault(ruleName, 0.0), this.__qin_field_ruleCachePutCounts.getOrDefault(ruleName, 0.0));
    })).sorted(__qin_java_functional((left, right) => {
      let totalCompare: any = __QinJavaLangLong.compare(right.total(), left.total());
      if (__qin_binary__("!=", totalCompare, 0.0)) {
        return totalCompare;
      }
      return left.ruleName().compareTo(right.ruleName());
    })).limit(safeLimit).map(__qin_java_functional((work) => {
      let hitRate: any = (__qin_binary__("==", work.total(), 0.0) ? 0.0 : __qin_binary__("/", Number(work.hits()), work.total()));
      return (work.ruleName() + "=" + work.total() + "(h" + work.hits() + "/p" + work.puts() + "@" + __QinJavaLangString.format(Locale.__qin_field_ROOT, "%.1f", __qin_binary__("*", hitRate, 100.0)) + "%)");
    })).collect(__QinJavaUtilStreamCollectors.joining(", ", "[", "]"));
  }
  formatTopCacheHitSpans(limit: number): any {
    if (this.__qin_field_ruleCacheHitTokenSpanTotals.isEmpty()) {
      return "[]";
    }
    let safeLimit: any = Math.max(1.0, limit);
    return __QinJavaUtilArrays.stream(this.__qin_field_ruleCacheHitTokenSpanTotals.entrySet()).sorted(__qin_java_functional((left, right) => {
      let totalCompare: any = __QinJavaLangLong.compare(right.getValue(), left.getValue());
      if (__qin_binary__("!=", totalCompare, 0.0)) {
        return totalCompare;
      }
      return left.getKey().compareTo(right.getKey());
    })).limit(safeLimit).map(__qin_java_functional((entry) => {
      let hits: any = this.__qin_field_ruleCacheHitCounts.getOrDefault(entry.getKey(), 0.0);
      let average: any = (__qin_binary__("==", hits, 0.0) ? 0.0 : __qin_binary__("/", Number(entry.getValue()), hits));
      return (entry.getKey() + "=" + entry.getValue() + "/" + hits + "@" + __QinJavaLangString.format(Locale.__qin_field_ROOT, "%.2f", average));
    })).collect(__QinJavaUtilStreamCollectors.joining(", ", "[", "]"));
  }
  formatTopFailureRates(limit: number): any {
    if (this.__qin_field_ruleCoreFailureCounts.isEmpty()) {
      return "[]";
    }
    let safeLimit: any = Math.max(1.0, limit);
    return __QinJavaUtilArrays.stream(this.__qin_field_ruleCoreFailureCounts.keySet()).map(__qin_java_functional((ruleName) => {
      return new com_subhuti_parser_SubhutiParserCore$FailureWork(ruleName, this.__qin_field_ruleCoreSuccessCounts.getOrDefault(ruleName, 0.0), this.__qin_field_ruleCoreFailureCounts.getOrDefault(ruleName, 0.0));
    })).filter(__qin_java_functional((work) => {
      return __qin_binary__(">", work.total(), 0.0);
    })).sorted(__qin_java_functional((left, right) => {
      let failureCompare: any = __QinJavaLangLong.compare(right.failures(), left.failures());
      if (__qin_binary__("!=", failureCompare, 0.0)) {
        return failureCompare;
      }
      return left.ruleName().compareTo(right.ruleName());
    })).limit(safeLimit).map(__qin_java_functional((work) => {
      let failureRate: any = __qin_binary__("/", Number(work.failures()), work.total());
      return (work.ruleName() + "=" + work.failures() + "/" + work.total() + "@" + __QinJavaLangString.format(Locale.__qin_field_ROOT, "%.1f", __qin_binary__("*", failureRate, 100.0)) + "%");
    })).collect(__QinJavaUtilStreamCollectors.joining(", ", "[", "]"));
  }
  formatTopOrCandidateCounts(limit: number): any {
    if (this.__qin_field_orPredictionCandidateRuleCounts.isEmpty()) {
      return "[]";
    }
    let safeLimit: any = Math.max(1.0, limit);
    return __QinJavaUtilArrays.stream(this.__qin_field_orPredictionCandidateRuleTotals.entrySet()).sorted(__qin_java_functional((left, right) => {
      let totalCompare: any = __QinJavaLangLong.compare(right.getValue(), left.getValue());
      if (__qin_binary__("!=", totalCompare, 0.0)) {
        return totalCompare;
      }
      return left.getKey().compareTo(right.getKey());
    })).limit(safeLimit).map(__qin_java_functional((entry) => {
      let calls: any = this.__qin_field_orPredictionCandidateRuleCounts.getOrDefault(entry.getKey(), 0.0);
      let average: any = (__qin_binary__("==", calls, 0.0) ? 0.0 : __qin_binary__("/", Number(entry.getValue()), calls));
      return (entry.getKey() + "=" + entry.getValue() + "/" + calls + "@" + __QinJavaLangString.format(Locale.__qin_field_ROOT, "%.2f", average));
    })).collect(__QinJavaUtilStreamCollectors.joining(", ", "[", "]"));
  }
  tryAndRestore(fn: any): any {
    const __qin_functional_fn_0 = __qin_java_functional(fn);
    if (this.isParserFailOrIsEof()) {
      return false;
    }
    let savedState: any = this.saveState();
    let startIndex: any = this.__qin_field_currentIndex;
    __qin_functional_fn_0.run();
    if ((!this.__qin_field_parseSuccess)) {
      this.recordPartialMatchAndRestore(savedState, startIndex);
      this.setParseSuccess();
      return false;
    }
    return __qin_binary__("!=", this.__qin_field_currentIndex, startIndex);
  }
  recordPartialMatchAndRestore(savedState: com_subhuti_parser_SubhutiBackData, startCodeIndex: number): any {
    this.restoreState(savedState);
    return null;
  }
  isParserFailOrIsEof(): any {
    return (!this.__qin_field_parseSuccess || this.isEof());
  }
  recoverFromParseRecord(root: com_subhuti_parser_ParseRecordNode, maxIndex: number): any {
    if ((__qin_binary__("==", root, null) || ((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(root.getChildren()))) {
      return null;
    }
    let cst: any = com_subhuti_struct_SubhutiCst.builder().name(root.getName()).children(this.parseRecordChildrenToCST(root.getChildren(), maxIndex)).build();
    if ((__qin_binary__("==", cst.getChildren(), null) || ((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(cst.getChildren()))) {
      return null;
    }
    return cst;
  }
  parseRecordChildrenToCST(nodes: any, maxIndex: number): any {
    let groups: any = new __QinJavaUtilHashMap();
    for (const node of nodes) {
      if (__qin_binary__(">", node.getEndTokenIndex(), maxIndex)) {
        continue;
      }
      let key: any = node.getStartTokenIndex();
      groups.computeIfAbsent(key, __qin_java_functional((k) => {
      return new __QinJavaUtilArrayList();
    })).add(node);
    }
    let selectedNodes: any = new __QinJavaUtilArrayList();
    for (const group of groups.values()) {
      let best: any = null;
      for (const node of group) {
        if ((__qin_binary__("==", best, null) || __qin_binary__(">=", node.getEndTokenIndex(), best.getEndTokenIndex()))) {
          best = node;
        }
      }
      if (__qin_binary__("!=", best, null)) {
        selectedNodes.add(best);
      }
    }
    selectedNodes.sort(__qin_java_functional((a, b) => {
      return __QinJavaLangInteger.compare(a.getStartTokenIndex(), b.getStartTokenIndex());
    }));
    let result: any = new __QinJavaUtilArrayList();
    for (const node of selectedNodes) {
      result.add(this.parseRecordNodeToCST(node, maxIndex));
    }
    return result;
  }
  parseRecordNodeToCST(node: com_subhuti_parser_ParseRecordNode, maxIndex: number): any {
    let cstBuilder: any = com_subhuti_struct_SubhutiCst.builder().name(node.getName());
    if (__qin_binary__("!=", node.getToken(), null)) {
      let token: any = node.getToken();
      cstBuilder.value(node.getValue()).location(com_subhuti_struct_SubhutiSourceLocation.of(token.startPosition(), token.endPosition()));
    }
    if ((!((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(node.getChildren()))) {
      let children: any = this.parseRecordChildrenToCST(node.getChildren(), maxIndex);
      if (((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(children)) {
        cstBuilder.children(null);
      } else {
        cstBuilder.children(children);
      }
    }
    return cstBuilder.build();
  }
  getParseRecordMaxEndIndex(root: com_subhuti_parser_ParseRecordNode, maxIndex: number): any {
    let maxEnd: any = (__qin_binary__("<=", root.getEndTokenIndex(), maxIndex) ? root.getEndTokenIndex() : 0.0);
    for (const child of root.getChildren()) {
      let childMax: any = this.getParseRecordMaxEndIndex(child, maxIndex);
      if (__qin_binary__(">", childMax, maxEnd)) {
        maxEnd = childMax;
      }
    }
    return maxEnd;
  }
  createErrorNode(startIndex: number, endIndex: number): any {
    let errorChildren: any = new __QinJavaUtilArrayList();
    for (const token of this.__qin_field_parsedTokens) {
      let tokenStart: any = (__qin_binary__("!=", token.index(), null) ? token.index() : 0.0);
      if ((__qin_binary__(">=", tokenStart, startIndex) && __qin_binary__("<", tokenStart, endIndex))) {
        let tokenNode: any = com_subhuti_struct_SubhutiCst.builder().name(token.tokenName()).value(token.value()).location(com_subhuti_struct_SubhutiSourceLocation.of(token.startPosition(), token.endPosition())).build();
        errorChildren.add(tokenNode);
      }
    }
    let errorNodeBuilder: any = com_subhuti_struct_SubhutiCst.builder().name("ErrorNode").children(errorChildren);
    if ((!((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(errorChildren))) {
      let first: any = ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(errorChildren, 0.0);
      let last: any = ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(errorChildren, __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(errorChildren), 1.0));
      if ((__qin_binary__("!=", first.getLocation(), null) && __qin_binary__("!=", last.getLocation(), null))) {
        errorNodeBuilder.location(com_subhuti_struct_SubhutiSourceLocation.of(first.getLocation().start(), last.getLocation().end()));
      }
    }
    return errorNodeBuilder.build();
  }
}
com_subhuti_parser_SubhutiParserCore.__qin_java_interfaces = ["com.subhuti.lookahead.SubhutiTokenMatchParser"];
const SubhutiParserCore = com_subhuti_parser_SubhutiParserCore;
class com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments {
  __qin_field_values: any[] | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 0 && true) {
      this.__qin_constructor_com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments_0_0();
      return;
    }
    if (__qin_args.length === 1 && true) {
      const values: any = __qin_args[0];
      this.__qin_constructor_com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments_1_1(values);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParserCore$StaticInvocationArguments/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments_0_0(): void {
    this.__qin_field_values = null;
    this.__qin_field_values = (__qin_binary__("==", this.__qin_field_values, null) ? [] : this.__qin_field_values.clone());
  }
  __qin_constructor_com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments_1_1(values: any[]): void {
    this.__qin_field_values = null;
    (() => {
      this.__qin_field_values = values;
      return null;
    })();
  }
  values(): any {
    return this.__qin_field_values.clone();
  }
  get(index: number): any {
    if ((__qin_binary__("<", index, 0.0) || __qin_binary__(">=", index, this.__qin_field_values.length))) {
      throw new __QinJavaLangIllegalArgumentException(("static invocation parameter index is out of bounds: " + index));
    }
    return this.__qin_field_values[index];
  }
  equals(other) {
    if (this === other) return true;
    if (!__qin_instanceof__(other, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments)) return false;
    return __qin_java_values_equal__(this.__qin_field_values, other.__qin_field_values);
  }
  hashCode() {
    let result = 1;
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_values);
    return result;
  }
  toString() {
    return ["SubhutiParserCore$StaticInvocationArguments[", "values=", this.__qin_field_values, "]"].join("");
  }
}
const SubhutiParserCore$StaticInvocationArguments = com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments;
class com_subhuti_parser_SubhutiParserCore$RuleExecutionResult {
  __qin_field_ruleResult: any = null as any;
  __qin_field_cst: com_subhuti_struct_SubhutiCst | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] !== "undefined") && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_subhuti_struct_SubhutiCst))) {
      const ruleResult: any = __qin_args[0];
      const cst: any = __qin_args[1];
      this.__qin_constructor_com_subhuti_parser_SubhutiParserCore$RuleExecutionResult_2_0(ruleResult, cst);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParserCore$RuleExecutionResult/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParserCore$RuleExecutionResult_2_0(ruleResult: any, cst: com_subhuti_struct_SubhutiCst): void {
    this.__qin_field_ruleResult = null;
    this.__qin_field_cst = null;
    this.__qin_field_ruleResult = ruleResult;
    this.__qin_field_cst = cst;
  }
}
const SubhutiParserCore$RuleExecutionResult = com_subhuti_parser_SubhutiParserCore$RuleExecutionResult;
class com_subhuti_parser_SubhutiParserCore$StaticExecutionMode extends java_lang_Enum {
  static __qin_field_RECOGNIZER: com_subhuti_parser_SubhutiParserCore$StaticExecutionMode | null = null as any;
  static __qin_field_CST: com_subhuti_parser_SubhutiParserCore$StaticExecutionMode | null = null as any;
  static __qin_field_RECOVERY_RECOGNIZER: com_subhuti_parser_SubhutiParserCore$StaticExecutionMode | null = null as any;
  static __qin_field_RECOVERY_CST: com_subhuti_parser_SubhutiParserCore$StaticExecutionMode | null = null as any;
  static __qin_field_DEBUG_RECOGNIZER: com_subhuti_parser_SubhutiParserCore$StaticExecutionMode | null = null as any;
  static __qin_field_DEBUG_CST: com_subhuti_parser_SubhutiParserCore$StaticExecutionMode | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length !== 0) {
      throw new Error("Unsupported Java constructor arity: SubhutiParserCore$StaticExecutionMode/" + __qin_args.length);
    }
    super();
  }
}
const SubhutiParserCore$StaticExecutionMode = com_subhuti_parser_SubhutiParserCore$StaticExecutionMode;
class com_subhuti_parser_SubhutiParserCore$StaticDebugHooks {
  static __qin_field_NO_OP: com_subhuti_parser_SubhutiParserCore$StaticDebugHooks | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length !== 0) {
      throw new Error("Unsupported Java constructor arity: SubhutiParserCore$StaticDebugHooks/" + __qin_args.length);
    }
  }
  onRuleEnter(ruleName: string, tokenIndex: number): any {
    throw new Error("Abstract Java method is not implemented: onRuleEnter");
  }
  onRuleExit(ruleName: string, startTime: number): any {
    throw new Error("Abstract Java method is not implemented: onRuleExit");
  }
  onRootComplete(cst: com_subhuti_struct_SubhutiCst): any {
    throw new Error("Abstract Java method is not implemented: onRootComplete");
  }
}
const SubhutiParserCore$StaticDebugHooks = com_subhuti_parser_SubhutiParserCore$StaticDebugHooks;
class com_subhuti_parser_SubhutiParserCore$CacheWork {
  __qin_field_ruleName: string | null = null as any;
  __qin_field_hits: number | null = null as any;
  __qin_field_puts: number | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && typeof __qin_args[1] === "number" && typeof __qin_args[2] === "number") {
      const ruleName: any = __qin_args[0];
      const hits: any = __qin_args[1];
      const puts: any = __qin_args[2];
      this.__qin_constructor_com_subhuti_parser_SubhutiParserCore$CacheWork_3_0(ruleName, hits, puts);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParserCore$CacheWork/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParserCore$CacheWork_3_0(ruleName: string, hits: number, puts: number): void {
    this.__qin_field_ruleName = null;
    this.__qin_field_hits = null;
    this.__qin_field_puts = null;
    (() => {
      this.__qin_field_ruleName = ruleName;
      this.__qin_field_hits = hits;
      this.__qin_field_puts = puts;
      return null;
    })();
  }
  total(): any {
    return __qin_binary__("+", this.__qin_field_hits, this.__qin_field_puts);
  }
  ruleName(): any {
    return this.__qin_field_ruleName;
  }
  hits(): any {
    return this.__qin_field_hits;
  }
  puts(): any {
    return this.__qin_field_puts;
  }
  equals(other) {
    if (this === other) return true;
    if (!__qin_instanceof__(other, com_subhuti_parser_SubhutiParserCore$CacheWork)) return false;
    return __qin_java_values_equal__(this.__qin_field_ruleName, other.__qin_field_ruleName)
      && __qin_java_values_equal__(this.__qin_field_hits, other.__qin_field_hits)
      && __qin_java_values_equal__(this.__qin_field_puts, other.__qin_field_puts);
  }
  hashCode() {
    let result = 1;
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_ruleName);
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_hits);
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_puts);
    return result;
  }
  toString() {
    return ["SubhutiParserCore$CacheWork[", "ruleName=", this.__qin_field_ruleName, ", ", "hits=", this.__qin_field_hits, ", ", "puts=", this.__qin_field_puts, "]"].join("");
  }
}
const SubhutiParserCore$CacheWork = com_subhuti_parser_SubhutiParserCore$CacheWork;
class com_subhuti_parser_SubhutiParserCore$FailureWork {
  __qin_field_ruleName: string | null = null as any;
  __qin_field_successes: number | null = null as any;
  __qin_field_failures: number | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && typeof __qin_args[1] === "number" && typeof __qin_args[2] === "number") {
      const ruleName: any = __qin_args[0];
      const successes: any = __qin_args[1];
      const failures: any = __qin_args[2];
      this.__qin_constructor_com_subhuti_parser_SubhutiParserCore$FailureWork_3_0(ruleName, successes, failures);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParserCore$FailureWork/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParserCore$FailureWork_3_0(ruleName: string, successes: number, failures: number): void {
    this.__qin_field_ruleName = null;
    this.__qin_field_successes = null;
    this.__qin_field_failures = null;
    (() => {
      this.__qin_field_ruleName = ruleName;
      this.__qin_field_successes = successes;
      this.__qin_field_failures = failures;
      return null;
    })();
  }
  total(): any {
    return __qin_binary__("+", this.__qin_field_successes, this.__qin_field_failures);
  }
  ruleName(): any {
    return this.__qin_field_ruleName;
  }
  successes(): any {
    return this.__qin_field_successes;
  }
  failures(): any {
    return this.__qin_field_failures;
  }
  equals(other) {
    if (this === other) return true;
    if (!__qin_instanceof__(other, com_subhuti_parser_SubhutiParserCore$FailureWork)) return false;
    return __qin_java_values_equal__(this.__qin_field_ruleName, other.__qin_field_ruleName)
      && __qin_java_values_equal__(this.__qin_field_successes, other.__qin_field_successes)
      && __qin_java_values_equal__(this.__qin_field_failures, other.__qin_field_failures);
  }
  hashCode() {
    let result = 1;
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_ruleName);
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_successes);
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_failures);
    return result;
  }
  toString() {
    return ["SubhutiParserCore$FailureWork[", "ruleName=", this.__qin_field_ruleName, ", ", "successes=", this.__qin_field_successes, ", ", "failures=", this.__qin_field_failures, "]"].join("");
  }
}
const SubhutiParserCore$FailureWork = com_subhuti_parser_SubhutiParserCore$FailureWork;
com_subhuti_parser_SubhutiParserCore.__qin_field_ADAPTIVE_LOW_YIELD_MEMO_MIN_PUTS = 64.0;
com_subhuti_parser_SubhutiParserCore$StaticDebugHooks.__qin_field_NO_OP = new com_subhuti_parser_SubhutiParserCore$StaticDebugHooks();
com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_RECOGNIZER = __qin_init_enum_value(new com_subhuti_parser_SubhutiParserCore$StaticExecutionMode(), "RECOGNIZER", 0);
com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_CST = __qin_init_enum_value(new com_subhuti_parser_SubhutiParserCore$StaticExecutionMode(), "CST", 1);
com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_RECOVERY_RECOGNIZER = __qin_init_enum_value(new com_subhuti_parser_SubhutiParserCore$StaticExecutionMode(), "RECOVERY_RECOGNIZER", 2);
com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_RECOVERY_CST = __qin_init_enum_value(new com_subhuti_parser_SubhutiParserCore$StaticExecutionMode(), "RECOVERY_CST", 3);
com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_DEBUG_RECOGNIZER = __qin_init_enum_value(new com_subhuti_parser_SubhutiParserCore$StaticExecutionMode(), "DEBUG_RECOGNIZER", 4);
com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_DEBUG_CST = __qin_init_enum_value(new com_subhuti_parser_SubhutiParserCore$StaticExecutionMode(), "DEBUG_CST", 5);

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$CacheWork, com_subhuti_parser_SubhutiParserCore$FailureWork };
