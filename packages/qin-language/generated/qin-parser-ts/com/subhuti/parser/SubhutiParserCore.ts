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
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __QinJavaLangStringBuilder, __QinJavaLangInteger, __QinJavaLangLong, __QinJavaLangEnum, __qin_java_string_hash_code__, __qin_java_identity_hash_code__, __qin_java_value_hash_code__, __qin_java_values_equal__, __qin_java_hash_key__, __qin_java_hash_key_equals__, __QinJavaUtilArrayList, __QinJavaUtilUnmodifiableList, __qin_java_functional, __qin_collection_size__, __qin_collection_get__, __QinJavaUtilObjects, __QinJavaUtilArrays, __qin_collection_is_empty__, __qin_collection_to_array__, __qin_collection_contains__, __QinJavaUtilSet, __QinJavaUtilUnmodifiableSet, __QinJavaUtilHashMap, __QinJavaUtilUnmodifiableMap, __QinJavaUtilList, __QinJavaUtilStreamCollectors, __QinJavaUtilHashSet, __qin_init_enum_value } from "@qin/java-sdk-js";

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
  static __qin_field_ADAPTIVE_LOW_YIELD_MEMO_MIN_PUTS: number | null = 0 as any;
  __qin_field_recognizerPassThroughRules: __QinJavaUtilSet<string> | null = null as any;
  __qin_field_recognizerTerminalLeafRules: __QinJavaUtilSet<string> | null = null as any;
  __qin_field_sourceLookaheadEntries: com_subhuti_lexer_TokenCacheEntry[] | null = null as any;
  __qin_field_sourceLookaheadSize: number | null = 0 as any;
  __qin_field_sourceLookaheadStartIndex: number | null = 0 as any;
  __qin_field_sourceLookaheadStartLine: number | null = 0 as any;
  __qin_field_sourceLookaheadStartColumn: number | null = 0 as any;
  __qin_field_sourceLookaheadMode: com_subhuti_struct_LexerMode | null = null as any;
  __qin_field_sourceLookaheadInitialized: boolean | null = false as any;
  __qin_field_sourceLookaheadTerminalReached: boolean | null = false as any;
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
  __qin_field_activeStaticRuleDepth: number | null = 0 as any;
  __qin_field_interpretedStaticRuleDepth: number | null = 0 as any;
  __qin_field_staticSourceBodyExecutionDepth: number | null = 0 as any;
  __qin_field_staticSourceBodyRuleIds: number[] | null = null as any;
  __qin_field_staticSourceBodyVariantIds: number[] | null = null as any;
  __qin_field_staticSourceBodyPassThrough: boolean[] | null = null as any;
  __qin_field_activeStaticFiniteProviderActionResult: any = null as any;
  __qin_field_activeStaticFiniteProviderActionResultReady: boolean | null = false as any;
  __qin_field_furthestStaticFailureIndex: number | null = 0 as any;
  __qin_field_furthestStaticFailureRules: string | null = null as any;
  __qin_field_furthestStaticFailureToken: string | null = null as any;
  __qin_field_firstStaticFailureIndex: number | null = 0 as any;
  __qin_field_firstStaticFailureRules: string | null = null as any;
  __qin_field_firstStaticFailureToken: string | null = null as any;
  __qin_field_staticDebugHooks: com_subhuti_parser_SubhutiParserCore$StaticDebugHooks | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] === "function" || (typeof __qin_args[1] === "object" && typeof __qin_args[1].getName === "function" && typeof __qin_args[1].isAssignableFrom === "function")) && (__qin_args[2] === null || Array.isArray(__qin_args[2]) || __qin_instanceof__(__qin_args[2], __QinJavaUtilArrayList) || __qin_instanceof__(__qin_args[2], __QinJavaUtilUnmodifiableList))) {
      const sourceCode: any = __qin_args[0];
      const tokenConsumerClass: any = __qin_args[1];
      const tokens: any = __qin_args[2];
      super(sourceCode, tokenConsumerClass, tokens);
      this.__qin_constructor_com_subhuti_parser_SubhutiParserCore_3_0(sourceCode, tokenConsumerClass, tokens);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParserCore/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParserCore_3_0(sourceCode: string, tokenConsumerClass: __QinJavaLangClass, tokens: __QinJavaUtilList<com_subhuti_struct_SubhutiCreateToken>): void {
    this.__qin_field_recognizerPassThroughRules = null;
    this.__qin_field_recognizerTerminalLeafRules = null;
    this.__qin_field_sourceLookaheadEntries = Array.from({ length: 16.0 }, () => null);
    this.__qin_field_sourceLookaheadSize = 0;
    this.__qin_field_sourceLookaheadStartIndex = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_sourceLookaheadStartLine = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_sourceLookaheadStartColumn = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_sourceLookaheadMode = null;
    this.__qin_field_sourceLookaheadInitialized = false;
    this.__qin_field_sourceLookaheadTerminalReached = false;
    this.__qin_field_sourceLookaheadTerminalEntry = null;
    this.__qin_field_activeStaticRuleIds = Array.from({ length: 16.0 }, () => 0);
    this.__qin_field_activeStaticVariantIds = Array.from({ length: 16.0 }, () => 0);
    this.__qin_field_activeStaticInvocationIds = Array.from({ length: 16.0 }, () => 0);
    this.__qin_field_activeStaticCursorStamps = Array.from({ length: 16.0 }, () => 0);
    this.__qin_field_activeStaticPreviousSameInvocation = Array.from({ length: 16.0 }, () => 0);
    this.__qin_field_activeStaticArguments = Array.from({ length: 16.0 }, () => null);
    this.__qin_field_activeStaticModes = Array.from({ length: 16.0 }, () => null);
    this.__qin_field_activeStaticLastTokenNames = Array.from({ length: 16.0 }, () => null);
    this.__qin_field_activeStaticStartCursorStamps = Array.from({ length: 16.0 }, () => 0);
    this.__qin_field_activeStaticDebugStartTimes = Array.from({ length: 16.0 }, () => 0);
    this.__qin_field_activeStaticRootEntries = Array.from({ length: 16.0 }, () => false);
    this.__qin_field_activeStaticTransparentCstFrames = Array.from({ length: 16.0 }, () => false);
    this.__qin_field_activeStaticSourceBodyFrames = Array.from({ length: 16.0 }, () => false);
    this.__qin_field_activeStaticRuleCsts = Array.from({ length: 16.0 }, () => null);
    this.__qin_field_activeStaticInvocationPlans = Array.from({ length: 16.0 }, () => null);
    this.__qin_field_activeStaticInvocationHeads = Array.from({ length: 0.0 }, () => 0);
    this.__qin_field_activeStaticRuleDepth = 0;
    this.__qin_field_interpretedStaticRuleDepth = 0;
    this.__qin_field_staticSourceBodyExecutionDepth = 0;
    this.__qin_field_staticSourceBodyRuleIds = Array.from({ length: 16.0 }, () => 0);
    this.__qin_field_staticSourceBodyVariantIds = Array.from({ length: 16.0 }, () => 0);
    this.__qin_field_staticSourceBodyPassThrough = Array.from({ length: 16.0 }, () => false);
    this.__qin_field_activeStaticFiniteProviderActionResult = null;
    this.__qin_field_activeStaticFiniteProviderActionResultReady = false;
    this.__qin_field_furthestStaticFailureIndex = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_furthestStaticFailureRules = "";
    this.__qin_field_furthestStaticFailureToken = "";
    this.__qin_field_firstStaticFailureIndex = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_firstStaticFailureRules = "";
    this.__qin_field_firstStaticFailureToken = "";
    this.__qin_field_staticDebugHooks = com_subhuti_parser_SubhutiParserCore$StaticDebugHooks.__qin_field_NO_OP;
    null;
  }
  gastGrammar(): com_subhuti_parser_SubhutiGastGrammar {
    return null;
  }
  staticRuleNamesById(): string[] {
    return null;
  }
  staticGrammarPlan(): com_subhuti_parser_SubhutiStaticGrammarPlan {
    return null;
  }
  effectiveGastGrammar(): com_subhuti_parser_SubhutiGastGrammar {
    let staticPlan: com_subhuti_parser_SubhutiStaticGrammarPlan = this.staticGrammarPlan();
    if (__qin_binary__("!=", staticPlan, null)) {
      return (staticPlan.hasGeneratedMetadata() ? null : staticPlan.gastGrammar());
    }
    return this.gastGrammar();
  }
  hasStaticGrammarPlan(): boolean {
    return __qin_binary__("!=", this.staticGrammarPlan(), null);
  }
  executeStaticRule(ruleId: number, variantId: number, invocationArgument: any, sourceBody: QinJavaSupplier): any {
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
      {
        const __qin_typed_receiver_23: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_23.abortStaticRule();
      }
      throw exception;
    }
    {
      const __qin_typed_receiver_24: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_24.completeStaticRule();
    }
    return result;
  }
  executeStaticVoidRule(ruleId: number, variantId: number, invocationArgument: any, sourceBody: QinJavaRunnable): void {
    const __qin_functional_sourceBody_3 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_3, null)) {
      throw new __QinJavaLangIllegalArgumentException("static rule body cannot be null");
    }
    {
      const __qin_typed_receiver_25: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_25.executeStaticRule(ruleId, variantId, invocationArgument, __qin_java_functional(() => {
      __qin_functional_sourceBody_3.run();
      return null;
    }));
    }
    return null;
  }
  isExecutingStaticSourceBody(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_isExecutingStaticSourceBody_0_0();
    if (__qin_args.length === 2 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number") return this.__qin_overload_isExecutingStaticSourceBody_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: isExecutingStaticSourceBody/" + __qin_args.length);
  }
  __qin_overload_isExecutingStaticSourceBody_0_0(): boolean {
    return __qin_binary__(">", this.__qin_field_staticSourceBodyExecutionDepth, 0.0);
  }
  __qin_overload_isExecutingStaticSourceBody_2_1(ruleId: number, variantId: number): boolean {
    for (let index: number = __qin_binary__("-", this.__qin_field_staticSourceBodyExecutionDepth, 1.0); __qin_binary__(">=", index, 0.0); index--) {
      if ((__qin_binary__("==", this.__qin_field_staticSourceBodyRuleIds[index], ruleId) && __qin_binary__("==", this.__qin_field_staticSourceBodyVariantIds[index], variantId))) {
        return true;
      }
    }
    return false;
  }
  isExecutingStaticSourceBodyPassThrough(ruleId: number, variantId: number): boolean {
    for (let index: number = __qin_binary__("-", this.__qin_field_staticSourceBodyExecutionDepth, 1.0); __qin_binary__(">=", index, 0.0); index--) {
      if ((__qin_binary__("==", this.__qin_field_staticSourceBodyRuleIds[index], ruleId) && __qin_binary__("==", this.__qin_field_staticSourceBodyVariantIds[index], variantId) && this.__qin_field_staticSourceBodyPassThrough[index])) {
        return true;
      }
    }
    return false;
  }
  executeStaticSourceBody(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "function")) return this.__qin_overload_executeStaticSourceBody_1_1(__qin_args[0]);
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && (__qin_args[2] === null || typeof __qin_args[2] === "function")) return this.__qin_overload_executeStaticSourceBody_3_3(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "function")) return this.__qin_overload_executeStaticSourceBody_1_0(__qin_args[0]);
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && (__qin_args[2] === null || typeof __qin_args[2] === "function")) return this.__qin_overload_executeStaticSourceBody_3_2(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: executeStaticSourceBody/" + __qin_args.length);
  }
  __qin_overload_executeStaticSourceBody_1_0(sourceBody: QinJavaRunnable): void {
    const __qin_functional_sourceBody_0 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_0, null)) {
      throw new __QinJavaLangIllegalArgumentException("static source body cannot be null");
    }
    {
      const __qin_typed_receiver_26: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_26.executeStaticSourceBody(__qin_java_functional(() => {
      __qin_functional_sourceBody_0.run();
      return null;
    }));
    }
    return null;
  }
  __qin_overload_executeStaticSourceBody_1_1(sourceBody: QinJavaSupplier): any {
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
  __qin_overload_executeStaticSourceBody_3_2(ruleId: number, variantId: number, sourceBody: QinJavaRunnable): void {
    const __qin_functional_sourceBody_2 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_2, null)) {
      throw new __QinJavaLangIllegalArgumentException("static source body cannot be null");
    }
    {
      const __qin_typed_receiver_27: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_27.executeStaticSourceBody(ruleId, variantId, __qin_java_functional(() => {
      __qin_functional_sourceBody_2.run();
      return null;
    }));
    }
    return null;
  }
  __qin_overload_executeStaticSourceBody_3_3(ruleId: number, variantId: number, sourceBody: QinJavaSupplier): any {
    const __qin_functional_sourceBody_2 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_2, null)) {
      throw new __QinJavaLangIllegalArgumentException("static source body cannot be null");
    }
    {
      const __qin_typed_receiver_28: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_28.ensureStaticSourceBodyStackCapacity(__qin_binary__("+", this.__qin_field_staticSourceBodyExecutionDepth, 1.0));
    }
    let frame: number = this.__qin_field_staticSourceBodyExecutionDepth++;
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
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && (__qin_args[2] === null || typeof __qin_args[2] === "function")) return this.__qin_overload_executeStaticSourceBodyPassThrough_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && (__qin_args[2] === null || typeof __qin_args[2] === "function")) return this.__qin_overload_executeStaticSourceBodyPassThrough_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: executeStaticSourceBodyPassThrough/" + __qin_args.length);
  }
  __qin_overload_executeStaticSourceBodyPassThrough_3_0(ruleId: number, variantId: number, sourceBody: QinJavaRunnable): void {
    const __qin_functional_sourceBody_2 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_2, null)) {
      throw new __QinJavaLangIllegalArgumentException("static source body cannot be null");
    }
    {
      const __qin_typed_receiver_29: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_29.executeStaticSourceBodyPassThrough(ruleId, variantId, __qin_java_functional(() => {
      __qin_functional_sourceBody_2.run();
      return null;
    }));
    }
    return null;
  }
  __qin_overload_executeStaticSourceBodyPassThrough_3_1(ruleId: number, variantId: number, sourceBody: QinJavaSupplier): any {
    const __qin_functional_sourceBody_2 = __qin_java_functional(sourceBody);
    if (__qin_binary__("==", __qin_functional_sourceBody_2, null)) {
      throw new __QinJavaLangIllegalArgumentException("static source body cannot be null");
    }
    {
      const __qin_typed_receiver_30: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_30.ensureStaticSourceBodyStackCapacity(__qin_binary__("+", this.__qin_field_staticSourceBodyExecutionDepth, 1.0));
    }
    let frame: number = this.__qin_field_staticSourceBodyExecutionDepth++;
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
  markActiveStaticRuleSourceBodyFrame(): void {
    let frame: number = this.requireActiveStaticRuleFrame("mark source body");
    this.__qin_field_activeStaticSourceBodyFrames[frame] = true;
    return null;
  }
  consumeStaticTerminal(tokenName: string, tokenValue: string, lexerMode: com_subhuti_struct_LexerMode): boolean {
    return this.consumeDirectTerminal(new com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal(tokenName, tokenValue, (__qin_binary__("==", lexerMode, null) ? com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE : lexerMode)));
  }
  beginStaticRule(ruleId: number, variantId: number, invocationArgument: any): boolean {
    return this.beginStaticRuleLinked(this.staticExecutionPlan().invocation(ruleId, variantId), invocationArgument, false, true);
  }
  beginStaticSubrule(ruleId: number, variantId: number, invocationArgument: any): boolean {
    return this.beginStaticRuleLinked(this.staticExecutionPlan().invocation(ruleId, variantId), invocationArgument, false, false);
  }
  beginStaticRuleLinked(...__qin_args: any[]): boolean {
    if (__qin_args.length === 2 && typeof __qin_args[0] === "number" && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_beginStaticRuleLinked_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 4 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan)) && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "boolean" && typeof __qin_args[3] === "boolean") return this.__qin_overload_beginStaticRuleLinked_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    throw new Error("Unsupported Java overload: beginStaticRuleLinked/" + __qin_args.length);
  }
  __qin_overload_beginStaticRuleLinked_2_0(invocationId: number, invocationArgument: any): boolean {
    return this.beginStaticRuleLinked(this.staticExecutionPlan().invocation(invocationId), invocationArgument, false, true);
  }
  __qin_overload_beginStaticRuleLinked_4_1(invocationPlan: com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan, invocationArgument: any, transparentCst: boolean, validateRootCompletion: boolean): boolean {
    let rootEntry: boolean = __qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0);
    if (rootEntry) {
      {
        const __qin_typed_receiver_31: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_31.initTopLevelData();
      }
      if ((!this.__qin_field_preTokenizedDefaultModeInput)) {
        {
          const __qin_typed_receiver_32: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_32.useDefaultModeTokenArrayInput();
        }
      }
      {
        const __qin_typed_receiver_33: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_33.bindStaticExecutionMode();
      }
    }
    if ((!this.__qin_field_parseSuccess)) {
      return false;
    }
    this.__qin_field_indexedRuleInvocationLookups++;
    if ((!this.enterStaticInvocation(invocationPlan, invocationArgument))) {
      this.__qin_field_staticRuleLoopRejects++;
      {
        const __qin_typed_receiver_34: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_34.setParseFail();
      }
      return false;
    }
    let frame: number = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
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
      let cst: com_subhuti_struct_SubhutiCst = com_subhuti_struct_SubhutiCst.builder().name(invocationPlan.ruleName()).build();
      this.__qin_field_cstStack.add(cst);
      this.__qin_field_activeStaticRuleCsts[frame] = cst;
    }
    return true;
  }
  beginStaticHelperLinked(invocationId: number, invocationArgument: any): boolean {
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      throw new __QinJavaLangIllegalStateException("static grammar helper executed outside a generated rule body");
    }
    return this.beginStaticRuleLinked(this.staticExecutionPlan().invocation(invocationId), invocationArgument, true, true);
  }
  staticHelperInvocationArguments(__qin_arguments: any[]): any {
    return new com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments(__qin_arguments);
  }
  completeStaticRule(): void {
    let frame: number = this.requireActiveStaticRuleFrame("complete");
    let invocationPlan: com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan = this.__qin_field_activeStaticInvocationPlans[frame];
    try {
      {
        const __qin_typed_receiver_35: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_35.rejectNonNullableEmptySuccess(invocationPlan, this.__qin_field_activeStaticStartCursorStamps[frame]);
      }
      if ((!this.__qin_field_activeStaticTransparentCstFrames[frame])) {
        {
          const __qin_typed_receiver_36: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_36.completeStaticRuleCst(invocationPlan, this.__qin_field_activeStaticRuleCsts[frame]);
        }
      }
      {
        const __qin_typed_receiver_37: com_subhuti_parser_SubhutiParserCore$StaticDebugHooks = this.__qin_field_staticDebugHooks;
        __qin_typed_receiver_37.onRuleExit(invocationPlan.ruleName(), this.__qin_field_activeStaticDebugStartTimes[frame]);
      }
      if ((this.__qin_field_activeStaticRootEntries[frame] && this.__qin_field_parseSuccess)) {
        {
          const __qin_typed_receiver_38: com_subhuti_parser_SubhutiParserCore$StaticDebugHooks = this.__qin_field_staticDebugHooks;
          __qin_typed_receiver_38.onRootComplete(this.__qin_field_rootCst);
        }
        {
          const __qin_typed_receiver_39: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_39.validateTopLevelParseComplete();
        }
      }
    } finally {
      {
        const __qin_typed_receiver_40: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_40.leaveStaticRuleFrame();
      }
    }
    return null;
  }
  abortStaticRule(): void {
    let frame: number = this.requireActiveStaticRuleFrame("abort");
    let cst: com_subhuti_struct_SubhutiCst = this.__qin_field_activeStaticRuleCsts[frame];
    if (__qin_binary__("!=", cst, null)) {
      let top: number = __qin_binary__("-", __qin_collection_size__(this.__qin_field_cstStack), 1.0);
      if ((__qin_binary__("<", top, 0.0) || __qin_binary__("!=", __qin_collection_get__(this.__qin_field_cstStack, top), cst))) {
        throw new __QinJavaLangIllegalStateException("static rule CST stack is inconsistent during abort");
      }
      this.__qin_field_cstStack.remove(top);
    }
    {
      const __qin_typed_receiver_41: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_41.leaveStaticRuleFrame();
    }
    return null;
  }
  bindStaticExecutionMode(): void {
    let recovery: boolean = this.isErrorRecoveryMode();
    let debug: boolean = __qin_binary__("!=", this.__qin_field__debugger, null);
    let mode: com_subhuti_parser_SubhutiParserCore$StaticExecutionMode = null;
    if (debug) {
      mode = (this.__qin_field_buildCst ? com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_DEBUG_CST : com_subhuti_parser_SubhutiParserCore$StaticExecutionMode.__qin_field_DEBUG_RECOGNIZER);
      {
        const __qin_typed_receiver_42: com_subhuti_debug_SubhutiTraceDebugger = this.__qin_field__debugger;
        __qin_typed_receiver_42.resetForNewParse(null);
      }
      this.__qin_field_staticDebugHooks = new com_subhuti_parser_SubhutiParserCore$StaticDebugHooks(this);
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
  enterStaticInvocation(invocationPlan: com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan, invocationArgument: any): boolean {
    let normalizedArgument: any = (__qin_binary__("==", invocationArgument, null) ? "" : invocationArgument);
    let invocationCursorStamp: number = 0.0;
    let recursiveHeadSlot: number = invocationPlan.recursiveHeadSlot();
    if (invocationPlan.recursive()) {
      {
        const __qin_typed_receiver_43: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_43.ensureStaticInvocationHeadCapacity(recursiveHeadSlot);
      }
      invocationCursorStamp = this.cursorStamp();
      for (let frame: number = this.__qin_field_activeStaticInvocationHeads[recursiveHeadSlot]; __qin_binary__(">=", frame, 0.0); frame = this.__qin_field_activeStaticPreviousSameInvocation[frame]) {
        if ((__qin_binary__("==", this.__qin_field_activeStaticCursorStamps[frame], invocationCursorStamp) && __qin_binary__("==", this.__qin_field_activeStaticModes[frame], this.__qin_field_currentMode) && __QinJavaUtilObjects.equals(this.__qin_field_activeStaticArguments[frame], normalizedArgument) && __QinJavaUtilObjects.equals(this.__qin_field_activeStaticLastTokenNames[frame], this.__qin_field_lastTokenName))) {
          return false;
        }
      }
    }
    {
      const __qin_typed_receiver_44: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_44.ensureStaticStackCapacity(__qin_binary__("+", this.__qin_field_activeStaticRuleDepth, 1.0));
    }
    let frame: number = this.__qin_field_activeStaticRuleDepth++;
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
  requireActiveStaticRuleFrame(operation: string): number {
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      throw new __QinJavaLangIllegalStateException(("cannot " + operation + " a static rule outside a generated rule body"));
    }
    let frame: number = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    if (__qin_binary__("==", this.__qin_field_activeStaticInvocationPlans[frame], null)) {
      throw new __QinJavaLangIllegalStateException(("static rule frame has no invocation plan during " + operation));
    }
    return frame;
  }
  setParseFail(): void {
    if ((__qin_binary__(">", this.__qin_field_activeStaticRuleDepth, 0.0) && __qin_binary__("<", this.__qin_field_firstStaticFailureIndex, 0.0))) {
      this.__qin_field_firstStaticFailureIndex = this.__qin_field_currentIndex;
      this.__qin_field_firstStaticFailureRules = this.activeStaticRuleScopeName();
      this.__qin_field_firstStaticFailureToken = __QinJavaLangString.valueOf(this.LA(1.0));
    }
    if ((__qin_binary__(">", this.__qin_field_activeStaticRuleDepth, 0.0) && __qin_binary__(">=", this.__qin_field_currentIndex, this.__qin_field_furthestStaticFailureIndex))) {
      this.__qin_field_furthestStaticFailureIndex = this.__qin_field_currentIndex;
      let rules: __QinJavaLangStringBuilder = new __QinJavaLangStringBuilder();
      for (let frame: number = 0.0; __qin_binary__("<", frame, this.__qin_field_activeStaticRuleDepth); frame++) {
        if (__qin_binary__(">", frame, 0.0)) {
          rules.append(" -> ");
        }
        rules.append(this.staticGrammarPlan().staticRuleInvocationPlan(this.__qin_field_activeStaticRuleIds[frame], this.__qin_field_activeStaticVariantIds[frame]).ruleName());
      }
      this.__qin_field_furthestStaticFailureRules = rules.toString();
      let token: com_subhuti_struct_SubhutiMatchToken = this.LA(1.0);
      this.__qin_field_furthestStaticFailureToken = __QinJavaLangString.valueOf(token);
    }
    super.setParseFail();
    return null;
  }
  getFurthestStaticFailureReport(): string {
    return (__qin_binary__("<", this.__qin_field_furthestStaticFailureIndex, 0.0) ? "none" : ("index=" + this.__qin_field_furthestStaticFailureIndex + ", token=" + this.__qin_field_furthestStaticFailureToken + ", rules=" + this.__qin_field_furthestStaticFailureRules));
  }
  getFirstStaticFailureReport(): string {
    return (__qin_binary__("<", this.__qin_field_firstStaticFailureIndex, 0.0) ? "none" : ("index=" + this.__qin_field_firstStaticFailureIndex + ", token=" + this.__qin_field_firstStaticFailureToken + ", rule=" + this.__qin_field_firstStaticFailureRules));
  }
  completeStaticRuleCst(invocationPlan: com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan, cst: com_subhuti_struct_SubhutiCst): void {
    if (__qin_binary__("==", cst, null)) {
      if ((!this.__qin_field_parseSuccess && __qin_binary__("!=", this.__qin_field_errorHandler, null))) {
        {
          const __qin_typed_receiver_45: com_subhuti_error_SubhutiErrorHandler = this.__qin_field_errorHandler;
          __qin_typed_receiver_45.recordRuleFailure(this.__qin_field_currentPosition, invocationPlan.ruleName());
        }
      }
      return null;
    }
    let top: number = __qin_binary__("-", __qin_collection_size__(this.__qin_field_cstStack), 1.0);
    if ((__qin_binary__("<", top, 0.0) || __qin_binary__("!=", __qin_collection_get__(this.__qin_field_cstStack, top), cst))) {
      throw new __QinJavaLangIllegalStateException("static rule CST stack is inconsistent during completion");
    }
    this.__qin_field_cstStack.remove(top);
    if (this.__qin_field_parseSuccess) {
      {
        const __qin_typed_receiver_46: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_46.setLocation(cst);
      }
      let parent: com_subhuti_struct_SubhutiCst = this.getCurCst();
      if (__qin_binary__("==", parent, null)) {
        this.__qin_field_rootCst = cst;
      } else {
        {
          const __qin_typed_receiver_47: com_subhuti_struct_SubhutiCst = parent;
          __qin_typed_receiver_47.addChild(cst);
        }
      }
    } else {
      if (__qin_binary__("!=", this.__qin_field_errorHandler, null)) {
        {
          const __qin_typed_receiver_48: com_subhuti_error_SubhutiErrorHandler = this.__qin_field_errorHandler;
          __qin_typed_receiver_48.recordRuleFailure(this.__qin_field_currentPosition, invocationPlan.ruleName());
        }
      }
    }
    return null;
  }
  leaveStaticRuleFrame(): void {
    let frame: number = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    let invocationPlan: com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan = this.__qin_field_activeStaticInvocationPlans[frame];
    this.__qin_field_activeStaticInvocationPlans[frame] = null;
    this.__qin_field_activeStaticRuleCsts[frame] = null;
    this.__qin_field_activeStaticRootEntries[frame] = false;
    this.__qin_field_activeStaticTransparentCstFrames[frame] = false;
    this.__qin_field_activeStaticSourceBodyFrames[frame] = false;
    this.__qin_field_activeStaticDebugStartTimes[frame] = 0.0;
    {
      const __qin_typed_receiver_49: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_49.exitStaticInvocation(invocationPlan.recursive());
    }
    this.__qin_field_ruleExecutionDepth--;
    return null;
  }
  exitStaticInvocation(recursive: boolean): void {
    let frame: number = --this.__qin_field_activeStaticRuleDepth;
    if (recursive) {
      let recursiveHeadSlot: number = this.__qin_field_activeStaticInvocationIds[frame];
      this.__qin_field_activeStaticInvocationHeads[recursiveHeadSlot] = this.__qin_field_activeStaticPreviousSameInvocation[frame];
      this.__qin_field_activeStaticModes[frame] = null;
      this.__qin_field_activeStaticLastTokenNames[frame] = null;
    }
    this.__qin_field_activeStaticArguments[frame] = null;
    this.__qin_field_activeStaticStartCursorStamps[frame] = 0.0;
    return null;
  }
  ensureStaticInvocationHeadCapacity(recursiveHeadSlot: number): void {
    if (__qin_binary__("<", recursiveHeadSlot, this.__qin_field_activeStaticInvocationHeads.length)) {
      return null;
    }
    let oldLength: number = this.__qin_field_activeStaticInvocationHeads.length;
    let nextLength: number = Math.max(16.0, oldLength);
    while (__qin_binary__("<=", nextLength, recursiveHeadSlot)) {
      nextLength *= 2.0;
    }
    this.__qin_field_activeStaticInvocationHeads = __QinJavaUtilArrays.copyOf(this.__qin_field_activeStaticInvocationHeads, nextLength);
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticInvocationHeads, oldLength, nextLength, __qin_binary__("-", 0.0, 1.0));
    return null;
  }
  ensureStaticStackCapacity(capacity: number): void {
    if (__qin_binary__("<=", capacity, this.__qin_field_activeStaticRuleIds.length)) {
      return null;
    }
    let nextLength: number = __qin_binary__("*", this.__qin_field_activeStaticRuleIds.length, 2.0);
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
  ensureStaticSourceBodyStackCapacity(capacity: number): void {
    if (__qin_binary__("<=", capacity, this.__qin_field_staticSourceBodyRuleIds.length)) {
      return null;
    }
    let nextLength: number = __qin_binary__("*", this.__qin_field_staticSourceBodyRuleIds.length, 2.0);
    while (__qin_binary__("<", nextLength, capacity)) {
      nextLength *= 2.0;
    }
    this.__qin_field_staticSourceBodyRuleIds = __QinJavaUtilArrays.copyOf(this.__qin_field_staticSourceBodyRuleIds, nextLength);
    this.__qin_field_staticSourceBodyVariantIds = __QinJavaUtilArrays.copyOf(this.__qin_field_staticSourceBodyVariantIds, nextLength);
    this.__qin_field_staticSourceBodyPassThrough = __QinJavaUtilArrays.copyOf(this.__qin_field_staticSourceBodyPassThrough, nextLength);
    return null;
  }
  rejectNonNullableEmptySuccess(invocationPlan: com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan, startCursorStamp: number): void {
    if ((this.__qin_field_parseSuccess && !invocationPlan.nullable() && __qin_binary__("==", this.cursorStamp(), startCursorStamp) && !this.__qin_field_activeStaticSourceBodyFrames[__qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0)] && !this.allowStaticNonNullableEmptySuccess(invocationPlan.ruleName()))) {
      this.__qin_field_staticNonNullableEmptyRejects++;
      this.__qin_field_lastStaticNonNullableEmptyRule = invocationPlan.ruleName();
      {
        const __qin_typed_receiver_50: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_50.setParseFail();
      }
    }
    return null;
  }
  allowStaticNonNullableEmptySuccess(ruleName: string): boolean {
    return false;
  }
  requireStaticOccurrence(...__qin_args: any[]): com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence {
    if (__qin_args.length === 2 && typeof __qin_args[0] === "number" && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_subhuti_parser_SubhutiStaticGrammarPlan$Kind))) return this.__qin_overload_requireStaticOccurrence_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 4 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && typeof __qin_args[2] === "number" && (__qin_args[3] === null || __qin_instanceof__(__qin_args[3], com_subhuti_parser_SubhutiStaticGrammarPlan$Kind))) return this.__qin_overload_requireStaticOccurrence_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    throw new Error("Unsupported Java overload: requireStaticOccurrence/" + __qin_args.length);
  }
  __qin_overload_requireStaticOccurrence_2_0(occurrenceId: number, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence {
    if (this.staticGrammarPlan().hasGeneratedMetadata()) {
      throw new __QinJavaLangIllegalStateException("generated parsers must use linked decision ids");
    }
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      throw new __QinJavaLangIllegalStateException("indexed grammar occurrence executed outside a static rule body");
    }
    let stackIndex: number = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    let occurrence: com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence = this.staticExecutionPlan().occurrence(this.__qin_field_activeStaticRuleIds[stackIndex], this.__qin_field_activeStaticVariantIds[stackIndex], occurrenceId);
    if (__qin_binary__("!=", occurrence.kind(), expectedKind)) {
      throw new __QinJavaLangIllegalStateException(("static grammar occurrence kind mismatch: expected " + expectedKind + " but found " + occurrence.kind() + " at " + occurrence.line() + ":" + occurrence.column()));
    }
    return occurrence;
  }
  __qin_overload_requireStaticOccurrence_4_1(ruleId: number, variantId: number, occurrenceId: number, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence {
    if (this.staticGrammarPlan().hasGeneratedMetadata()) {
      throw new __QinJavaLangIllegalStateException("generated parsers must use linked decision ids");
    }
    let occurrence: com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence = this.staticExecutionPlan().occurrence(ruleId, variantId, occurrenceId);
    if (__qin_binary__("!=", occurrence.kind(), expectedKind)) {
      throw new __QinJavaLangIllegalStateException(("static grammar occurrence kind mismatch: expected " + expectedKind + " but found " + occurrence.kind() + " at " + occurrence.line() + ":" + occurrence.column()));
    }
    return occurrence;
  }
  staticDecisionPlan(...__qin_args: any[]): com_subhuti_parser_SubhutiDecisionPlan {
    if (__qin_args.length === 2 && typeof __qin_args[0] === "number" && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_subhuti_parser_SubhutiStaticGrammarPlan$Kind))) return this.__qin_overload_staticDecisionPlan_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 4 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && typeof __qin_args[2] === "number" && (__qin_args[3] === null || __qin_instanceof__(__qin_args[3], com_subhuti_parser_SubhutiStaticGrammarPlan$Kind))) return this.__qin_overload_staticDecisionPlan_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    throw new Error("Unsupported Java overload: staticDecisionPlan/" + __qin_args.length);
  }
  __qin_overload_staticDecisionPlan_2_0(occurrenceId: number, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): com_subhuti_parser_SubhutiDecisionPlan {
    {
      const __qin_typed_receiver_51: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_51.requireStaticOccurrence(occurrenceId, expectedKind);
    }
    let frame: number = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    return this.staticExecutionPlan().decision(this.__qin_field_activeStaticRuleIds[frame], this.__qin_field_activeStaticVariantIds[frame], occurrenceId, expectedKind);
  }
  __qin_overload_staticDecisionPlan_4_1(ruleId: number, variantId: number, occurrenceId: number, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): com_subhuti_parser_SubhutiDecisionPlan {
    {
      const __qin_typed_receiver_52: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_52.requireStaticOccurrence(ruleId, variantId, occurrenceId, expectedKind);
    }
    return this.staticExecutionPlan().decision(ruleId, variantId, occurrenceId, expectedKind);
  }
  linkedDecision(decisionId: number, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): com_subhuti_parser_SubhutiDecisionPlan {
    return this.staticExecutionPlan().decision(decisionId, expectedKind);
  }
  dispatchStaticGate(...__qin_args: any[]): boolean {
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && typeof __qin_args[2] === "number") return this.__qin_overload_dispatchStaticGate_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_dispatchStaticGate_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: dispatchStaticGate/" + __qin_args.length);
  }
  __qin_overload_dispatchStaticGate_3_0(ruleId: number, variantId: number, gateId: number): boolean {
    this.__qin_field_staticGateDispatches++;
    return this.executeStaticGate(ruleId, variantId, gateId);
  }
  __qin_overload_dispatchStaticGate_1_1(gateId: number): boolean {
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      throw new __QinJavaLangIllegalStateException("static gate executed outside a static rule body");
    }
    let frame: number = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    this.__qin_field_staticGateDispatches++;
    return this.executeStaticGate(this.__qin_field_activeStaticRuleIds[frame], this.__qin_field_activeStaticVariantIds[frame], gateId);
  }
  dispatchStaticGateWithArguments(ruleId: number, variantId: number, gateId: number, invocationArgument: any): boolean {
    let frame: number = this.enterStaticInvocationArgumentFrame(ruleId, variantId, invocationArgument);
    try {
      this.__qin_field_staticGateDispatches++;
      return this.executeStaticGate(ruleId, variantId, gateId);
    } finally {
      {
        const __qin_typed_receiver_53: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_53.leaveStaticInvocationArgumentFrame(frame);
      }
    }
    return null;
  }
  withStaticInvocationArgumentFrame(ruleId: number, variantId: number, invocationArgument: any, body: QinJavaSupplier): any {
    const __qin_functional_body_3 = __qin_java_functional(body);
    let frame: number = this.enterStaticInvocationArgumentFrame(ruleId, variantId, invocationArgument);
    try {
      return __qin_functional_body_3.get();
    } finally {
      {
        const __qin_typed_receiver_54: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_54.leaveStaticInvocationArgumentFrame(frame);
      }
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
    let frame: number = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    this.__qin_field_staticActionDispatches++;
    return this.executeStaticAction(this.__qin_field_activeStaticRuleIds[frame], this.__qin_field_activeStaticVariantIds[frame], actionId);
  }
  staticDirectActionExecutionEnabled(): boolean {
    return (!this.isErrorRecoveryMode());
  }
  activeStaticInvocationArgument(): any {
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      throw new __QinJavaLangIllegalStateException("static invocation argument requested outside a static rule body");
    }
    return this.__qin_field_activeStaticArguments[__qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0)];
  }
  activeStaticInvocationArgumentAt(parameterIndex: number): any {
    if (__qin_binary__("<", parameterIndex, 0.0)) {
      throw new __QinJavaLangIllegalArgumentException("static invocation parameter index cannot be negative");
    }
    let invocationArgument: any = this.activeStaticInvocationArgument();
    if ((() => { const __qin_instanceof_value = invocationArgument; return __qin_instanceof__(__qin_instanceof_value, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments); })()) {
      let __qin_arguments: com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments = (invocationArgument as com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments);
      return __qin_arguments.get(parameterIndex);
    }
    if (__qin_binary__("!=", parameterIndex, 0.0)) {
      throw new __QinJavaLangIllegalArgumentException(("single-argument static invocation cannot read parameter " + parameterIndex));
    }
    return invocationArgument;
  }
  activeStaticRuleId(): number {
    return this.__qin_field_activeStaticRuleIds[__qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0)];
  }
  activeStaticVariantId(): number {
    return this.__qin_field_activeStaticVariantIds[__qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0)];
  }
  executeStaticAction(ruleId: number, variantId: number, actionId: number): any {
    throw new __QinJavaLangIllegalStateException(("missing generated static action: rule=" + ruleId + ", variant=" + variantId + ", action=" + actionId));
  }
  executeStaticNamedAction(ruleId: number, variantId: number, actionId: number, actionName: string): any {
    throw new __QinJavaLangIllegalStateException(("missing generated static named action: rule=" + ruleId + ", variant=" + variantId + ", action=" + actionId + ", name=" + actionName));
  }
  executeStaticSubrule(ruleId: number, variantId: number, invocationArgument: any): void {
    throw new __QinJavaLangIllegalStateException(("missing generated static subrule: rule=" + ruleId + ", variant=" + variantId));
  }
  setStaticFiniteProviderActionResult(result: any): void {
    this.__qin_field_activeStaticFiniteProviderActionResult = result;
    this.__qin_field_activeStaticFiniteProviderActionResultReady = true;
    return null;
  }
  hasStaticFiniteProviderActionResult(): boolean {
    return this.__qin_field_activeStaticFiniteProviderActionResultReady;
  }
  consumeStaticFiniteProviderActionResult(): any {
    if ((!this.__qin_field_activeStaticFiniteProviderActionResultReady)) {
      throw new __QinJavaLangIllegalStateException("static finite provider action result was requested before a provider produced it");
    }
    let result: any = this.__qin_field_activeStaticFiniteProviderActionResult;
    {
      const __qin_typed_receiver_55: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_55.clearStaticFiniteProviderActionResult();
    }
    return result;
  }
  clearStaticFiniteProviderActionResult(): void {
    this.__qin_field_activeStaticFiniteProviderActionResult = null;
    this.__qin_field_activeStaticFiniteProviderActionResultReady = false;
    return null;
  }
  executeStaticGate(ruleId: number, variantId: number, gateId: number): boolean {
    throw new __QinJavaLangIllegalStateException(("missing generated static gate: rule=" + ruleId + ", variant=" + variantId + ", gate=" + gateId));
  }
  executeStaticNamedGate(ruleId: number, variantId: number, gateId: number, gateName: string): boolean {
    throw new __QinJavaLangIllegalStateException(("missing generated static named gate: rule=" + ruleId + ", variant=" + variantId + ", gate=" + gateId + ", name=" + gateName));
  }
  executeStaticFiniteGate(decisionRuleId: number, decisionVariantId: number, occurrenceId: number, stateId: number, gateRuleId: number, gateVariantId: number, gateId: number): boolean {
    throw new __QinJavaLangIllegalStateException(("missing generated finite gate owner-argument proof: decision=" + decisionRuleId + ":" + decisionVariantId + ":" + occurrenceId + ", state=" + stateId + ", gate=" + gateRuleId + ":" + gateVariantId + "#" + gateId));
  }
  executeStaticSharedPrefix(ruleId: number, variantId: number, occurrenceId: number): void {
    throw new __QinJavaLangIllegalStateException(("missing generated shared-prefix provider: " + ruleId + ":" + variantId + ":" + occurrenceId));
  }
  executeStaticSharedContinuation(ruleId: number, variantId: number, occurrenceId: number, branchIndex: number): void {
    throw new __QinJavaLangIllegalStateException(("missing generated shared-prefix continuation: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + branchIndex));
  }
  executeStaticCandidateGroupPrefix(ruleId: number, variantId: number, occurrenceId: number, groupId: number): void {
    throw new __QinJavaLangIllegalStateException(("missing generated candidate-group prefix: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + groupId));
  }
  executeStaticCandidateGroupContinuation(ruleId: number, variantId: number, occurrenceId: number, groupId: number, branchIndex: number): void {
    throw new __QinJavaLangIllegalStateException(("missing generated candidate-group continuation: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + groupId + ":" + branchIndex));
  }
  executeStaticCrossRulePrefix(ruleId: number, variantId: number, occurrenceId: number, providerId: number): void {
    throw new __QinJavaLangIllegalStateException(("missing generated cross-rule prefix provider: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + providerId));
  }
  executeStaticCrossRuleContinuation(ruleId: number, variantId: number, occurrenceId: number, providerId: number, branchIndex: number): void {
    throw new __QinJavaLangIllegalStateException(("missing generated cross-rule continuation provider: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + providerId + ":" + branchIndex));
  }
  executeStaticFiniteCallPrefix(ruleId: number, variantId: number, occurrenceId: number, stateId: number): number {
    throw new __QinJavaLangIllegalStateException(("missing generated finite-program CALL_PREFIX provider: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + stateId));
  }
  executeStaticFiniteContinuation(ruleId: number, variantId: number, occurrenceId: number, stateId: number, branchIndex: number): void {
    throw new __QinJavaLangIllegalStateException(("missing generated finite-program continuation provider: " + ruleId + ":" + variantId + ":" + occurrenceId + ":" + stateId + ":" + branchIndex));
  }
  activeStaticRuleScopeName(): string {
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      return null;
    }
    let frame: number = __qin_binary__("-", this.__qin_field_activeStaticRuleDepth, 1.0);
    let names: string[] = this.staticRuleNamesById();
    let ruleId: number = this.__qin_field_activeStaticRuleIds[frame];
    if ((__qin_binary__("!=", names, null) && __qin_binary__(">=", ruleId, 0.0) && __qin_binary__("<", ruleId, names.length))) {
      return names[ruleId];
    }
    return this.staticExecutionPlan().invocation(ruleId, this.__qin_field_activeStaticVariantIds[frame]).ruleName();
  }
  enterStaticInvocationArgumentFrame(ruleId: number, variantId: number, invocationArgument: any): number {
    {
      const __qin_typed_receiver_56: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_56.ensureStaticStackCapacity(__qin_binary__("+", this.__qin_field_activeStaticRuleDepth, 1.0));
    }
    let frame: number = this.__qin_field_activeStaticRuleDepth++;
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
  leaveStaticInvocationArgumentFrame(frame: number): void {
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
  enterInterpretedStaticRule(): void {
    this.__qin_field_interpretedStaticRuleDepth++;
    return null;
  }
  leaveInterpretedStaticRule(): void {
    if (__qin_binary__("==", this.__qin_field_interpretedStaticRuleDepth, 0.0)) {
      throw new __QinJavaLangIllegalStateException("interpreted static rule stack is inconsistent");
    }
    this.__qin_field_interpretedStaticRuleDepth--;
    return null;
  }
  hasActiveStaticRuleExecution(): boolean {
    return (__qin_binary__(">", this.__qin_field_activeStaticRuleDepth, 0.0) || __qin_binary__(">", this.__qin_field_interpretedStaticRuleDepth, 0.0));
  }
  initTopLevelData(): void {
    super.initTopLevelData();
    this.__qin_field_activeStaticRuleDepth = 0.0;
    this.__qin_field_interpretedStaticRuleDepth = 0.0;
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticInvocationHeads, __qin_binary__("-", 0.0, 1.0));
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticInvocationPlans, null);
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticRuleCsts, null);
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticRootEntries, false);
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticTransparentCstFrames, false);
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticStartCursorStamps, 0.0);
    __QinJavaUtilArrays.fill(this.__qin_field_activeStaticDebugStartTimes, 0.0);
    {
      const __qin_typed_receiver_57: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_57.clearStaticFiniteProviderActionResult();
    }
    return null;
  }
  parserRuntimePlan(): com_subhuti_parser_SubhutiParserRuntimePlan {
    if (this.hasStaticGrammarPlan()) {
      throw new __QinJavaLangIllegalStateException("generated static parsers cannot allocate the handwritten runtime plan");
    }
    if (__qin_binary__("!=", this.__qin_field_parserRuntimePlan, null)) {
      return this.__qin_field_parserRuntimePlan;
    }
    let access: com_subhuti_parser_SubhutiParserRuntimePlan$Access = com_subhuti_parser_SubhutiParserRuntimePlan.access(this.getClass().getName(), this.effectiveGastGrammar());
    this.__qin_field_parserRuntimePlan = access.plan();
    if (access.built()) {
      this.__qin_field_parserRuntimePlanBuilds++;
    } else {
      this.__qin_field_parserRuntimePlanCacheHits++;
    }
    return this.__qin_field_parserRuntimePlan;
  }
  invalidatePredictionPlansForGrammarChange(): void {
    return null;
  }
  _getOrParseTokenEntry(index: number, line: number, column: number, mode: com_subhuti_struct_LexerMode): com_subhuti_lexer_TokenCacheEntry {
    let currentTokenEntry: com_subhuti_lexer_TokenCacheEntry = this.cachedCurrentTokenEntry(index, line, column, mode);
    if (__qin_binary__("!=", currentTokenEntry, null)) {
      this.__qin_field_currentTokenEntryCacheHits++;
      return currentTokenEntry;
    }
    let preTokenizedOrdinalEntry: com_subhuti_lexer_TokenCacheEntry = this.preTokenizedFastEntryAtCurrentOrdinal(index, mode);
    if (__qin_binary__("!=", preTokenizedOrdinalEntry, null)) {
      {
        const __qin_typed_receiver_58: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_58.cacheCurrentTokenEntry(index, line, column, mode, preTokenizedOrdinalEntry);
      }
      return preTokenizedOrdinalEntry;
    }
    let preTokenizedEntry: com_subhuti_lexer_TokenCacheEntry = this.preTokenizedEntryAt(index, mode);
    if (__qin_binary__("!=", preTokenizedEntry, null)) {
      this.__qin_field_tokenStreamGets++;
      this.__qin_field_tokenStreamHits++;
      {
        const __qin_typed_receiver_59: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_59.cacheCurrentTokenEntry(index, line, column, mode, preTokenizedEntry);
      }
      return preTokenizedEntry;
    }
    let regexpNegativeEntry: com_subhuti_lexer_TokenCacheEntry = this.preTokenizedRegexpNegativeEntry(index, mode);
    if (__qin_binary__("!=", regexpNegativeEntry, null)) {
      this.__qin_field_preTokenizedRegexpNegativeHits++;
      return regexpNegativeEntry;
    }
    {
      const __qin_typed_receiver_60: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_60.recordPreTokenizedFallbackMode(mode);
    }
    this.__qin_field_tokenCacheGets++;
    let cached: com_subhuti_lexer_TokenCacheEntry = this.__qin_field_tokenCache.get(index, mode, this.__qin_field_lastTokenName);
    if (__qin_binary__("!=", cached, null)) {
      this.__qin_field_tokenCacheHits++;
      return cached;
    }
    this.__qin_field_tokenCacheMisses++;
    let entry: com_subhuti_lexer_TokenCacheEntry = this.__qin_field_lexer.readTokenAt(this.__qin_field_sourceCode, index, line, column, mode, this.__qin_field_lastTokenName);
    {
      const __qin_typed_receiver_61: com_subhuti_parser_SubhutiTokenCache = this.__qin_field_tokenCache;
      __qin_typed_receiver_61.put(index, mode, this.__qin_field_lastTokenName, entry);
    }
    this.__qin_field_tokenCachePuts++;
    return entry;
  }
  cachedCurrentTokenEntry(index: number, line: number, column: number, mode: com_subhuti_struct_LexerMode): com_subhuti_lexer_TokenCacheEntry {
    if ((!this.canUseCurrentTokenEntryCache(index, line, column, mode))) {
      return null;
    }
    if ((!this.__qin_field_currentTokenEntryCacheSet || __qin_binary__("==", this.__qin_field_currentTokenEntryCacheEntry, null) || __qin_binary__("!=", this.__qin_field_currentTokenEntryCacheCodeIndex, index) || __qin_binary__("!=", this.__qin_field_currentTokenEntryCacheLine, line) || __qin_binary__("!=", this.__qin_field_currentTokenEntryCacheColumn, column) || __qin_binary__("!=", this.__qin_field_currentTokenEntryCacheTokenCursor, this.__qin_field_tokenCursor) || !__QinJavaUtilObjects.equals(this.__qin_field_currentTokenEntryCacheMode, mode) || !__QinJavaUtilObjects.equals(this.__qin_field_currentTokenEntryCacheLastTokenName, this.__qin_field_lastTokenName))) {
      return null;
    }
    return this.__qin_field_currentTokenEntryCacheEntry;
  }
  cacheCurrentTokenEntry(index: number, line: number, column: number, mode: com_subhuti_struct_LexerMode, entry: com_subhuti_lexer_TokenCacheEntry): void {
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
  canUseCurrentTokenEntryCache(index: number, line: number, column: number, mode: com_subhuti_struct_LexerMode): boolean {
    return (!this.__qin_field_buildCst && !this.isErrorRecoveryMode() && this.__qin_field_preTokenizedDefaultModeInput && __qin_binary__("==", index, this.__qin_field_currentIndex) && __qin_binary__("==", line, this.__qin_field_currentPosition.line()) && __qin_binary__("==", column, this.__qin_field_currentPosition.column()) && (__qin_binary__("==", mode, null) || com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE.equals(mode)));
  }
  _getOrParseToken(index: number, line: number, column: number, mode: com_subhuti_struct_LexerMode): com_subhuti_struct_SubhutiMatchToken {
    return (__qin_binary__("!=", this._getOrParseTokenEntry(index, line, column, mode), null) ? this._getOrParseTokenEntry(index, line, column, mode).getToken() : null);
  }
  recordPredictionToken(tokenName: string, tokenValue: string, mode: com_subhuti_struct_LexerMode): void {
    this.__qin_field_firstTokenRecordingTokenCount++;
    return null;
  }
  recordPredictionTokenAndStop(...__qin_args: any[]): void {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) return this.__qin_overload_recordPredictionTokenAndStop_1_0(__qin_args[0]);
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] === "string")) return this.__qin_overload_recordPredictionTokenAndStop_2_1(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || __qin_instanceof__(__qin_args[2], com_subhuti_struct_LexerMode))) return this.__qin_overload_recordPredictionTokenAndStop_3_2(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: recordPredictionTokenAndStop/" + __qin_args.length);
  }
  __qin_overload_recordPredictionTokenAndStop_1_0(tokenName: string): void {
    {
      const __qin_typed_receiver_62: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_62.recordPredictionTokenAndStop(tokenName, null, this.__qin_field_currentMode);
    }
    return null;
  }
  __qin_overload_recordPredictionTokenAndStop_2_1(tokenName: string, tokenValue: string): void {
    {
      const __qin_typed_receiver_63: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_63.recordPredictionTokenAndStop(tokenName, tokenValue, this.__qin_field_currentMode);
    }
    return null;
  }
  __qin_overload_recordPredictionTokenAndStop_3_2(tokenName: string, tokenValue: string, mode: com_subhuti_struct_LexerMode): void {
    if ((!this.__qin_field_firstTokenRecording)) {
      return null;
    }
    if (this.hasStaticGrammarPlan()) {
      throw new __QinJavaLangIllegalStateException("generated static parsers cannot enter runtime token prediction recording");
    }
    {
      const __qin_typed_receiver_64: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_64.recordPredictionToken(tokenName, tokenValue, mode);
    }
    if (__qin_binary__(">=", this.__qin_field_firstTokenRecordingTokenCount, this.__qin_field_firstTokenRecordingMaxTokens)) {
      throw new com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException();
    }
    return null;
  }
  recordPredictionUnknownAndStop(): void {
    if (this.__qin_field_firstTokenRecording) {
      throw new com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException();
    }
    return null;
  }
  _consumeToken(tokenName: string, mode: com_subhuti_struct_LexerMode): com_subhuti_struct_SubhutiCst {
    if (this.__qin_field_firstTokenRecording) {
      {
        const __qin_typed_receiver_65: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_65.recordPredictionTokenAndStop(tokenName, null, mode);
      }
      return com_subhuti_struct_SubhutiCst.builder().name(tokenName).value("").build();
    }
    if ((!this.__qin_field_parseSuccess)) {
      return null;
    }
    let tokenEntry: com_subhuti_lexer_TokenCacheEntry = (this.shouldReadTokenDirectlyForRecognizer() ? this.readTokenDirectlyForRecognizer(mode) : this._getOrParseTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), mode));
    let token: com_subhuti_struct_SubhutiMatchToken = (__qin_binary__("!=", tokenEntry, null) ? tokenEntry.getToken() : null);
    if ((__qin_binary__("==", token, null) || token.isEof() || !__QinJavaLangString.equals(token.tokenName(), tokenName))) {
      {
        const __qin_typed_receiver_66: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_66.setParseFail();
      }
      if (__qin_binary__("!=", this.__qin_field_errorHandler, null)) {
        {
          const __qin_typed_receiver_67: com_subhuti_error_SubhutiErrorHandler = this.__qin_field_errorHandler;
          __qin_typed_receiver_67.recordTokenMismatch(this.__qin_field_currentPosition, tokenName, token);
        }
      }
      return null;
    }
    let cst: com_subhuti_struct_SubhutiCst = this.generateCstByToken(token);
    {
      const __qin_typed_receiver_68: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_68.advanceTokenCursor(tokenEntry);
    }
    {
      const __qin_typed_receiver_69: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_69.recordParsedTokenForState(token);
    }
    {
      const __qin_typed_receiver_70: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_70.recordConsumedToken(token);
    }
    return cst;
  }
  generateCstByToken(token: com_subhuti_struct_SubhutiMatchToken): com_subhuti_struct_SubhutiCst {
    if ((!this.__qin_field_buildCst)) {
      return null;
    }
    this.__qin_field_tokenCstNodes++;
    let cst: com_subhuti_struct_SubhutiCst = com_subhuti_struct_SubhutiCst.builder().name(token.tokenName()).value(token.value()).location(com_subhuti_struct_SubhutiSourceLocation.of(token.startPosition(), token.endPosition())).build();
    let currentCst: com_subhuti_struct_SubhutiCst = this.getCurCst();
    if (__qin_binary__("!=", currentCst, null)) {
      {
        const __qin_typed_receiver_71: com_subhuti_struct_SubhutiCst = currentCst;
        __qin_typed_receiver_71.addChild(cst);
      }
    }
    return cst;
  }
  consumePartialToken(tokenName: string, tokenValue: string, consumedChars: number): com_subhuti_struct_SubhutiCst {
    if ((!this.__qin_field_parseSuccess)) {
      return null;
    }
    if (this.isEof()) {
      {
        const __qin_typed_receiver_72: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_72.setParseFail();
      }
      return null;
    }
    let token: com_subhuti_struct_SubhutiMatchToken = this._getOrParseToken(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), this.__qin_field_currentMode);
    if ((__qin_binary__("==", token, null) || __qin_binary__("==", tokenValue, null) || __qin_binary__("<=", consumedChars, 0.0) || __qin_binary__("<", token.getLength(), consumedChars))) {
      {
        const __qin_typed_receiver_73: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_73.setParseFail();
      }
      return null;
    }
    let start: com_subhuti_struct_SubhutiPosition = this.__qin_field_currentPosition;
    let end: com_subhuti_struct_SubhutiPosition = this.__qin_field_currentPosition.advance(tokenValue);
    let partialToken: com_subhuti_struct_SubhutiMatchToken = new com_subhuti_struct_SubhutiMatchToken(tokenName, tokenValue, start.line(), start.column(), end.column(), start.index(), token.hasLineBreakBefore());
    let cst: com_subhuti_struct_SubhutiCst = null;
    if (this.__qin_field_buildCst) {
      cst = com_subhuti_struct_SubhutiCst.builder().name(tokenName).value(tokenValue).location(com_subhuti_struct_SubhutiSourceLocation.of(tokenName, start, end)).build();
      this.__qin_field_tokenCstNodes++;
      let currentCst: com_subhuti_struct_SubhutiCst = this.getCurCst();
      if (__qin_binary__("!=", currentCst, null)) {
        {
          const __qin_typed_receiver_74: com_subhuti_struct_SubhutiCst = currentCst;
          __qin_typed_receiver_74.addChild(cst);
        }
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
    {
      const __qin_typed_receiver_75: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_75.recordParsedTokenForState(partialToken);
    }
    {
      const __qin_typed_receiver_76: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_76.recordConsumedToken(partialToken);
    }
    {
      const __qin_typed_receiver_77: com_subhuti_parser_SubhutiTokenCache = this.__qin_field_tokenCache;
      __qin_typed_receiver_77.clear();
    }
    return cst;
  }
  _consumeTokenMatch(tokenName: string, mode: com_subhuti_struct_LexerMode): com_subhuti_struct_SubhutiMatchToken {
    if (this.__qin_field_firstTokenRecording) {
      {
        const __qin_typed_receiver_78: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_78.recordPredictionTokenAndStop(tokenName, null, mode);
      }
      return new com_subhuti_struct_SubhutiMatchToken(tokenName, "");
    }
    if ((!this.__qin_field_parseSuccess)) {
      return null;
    }
    let tokenEntry: com_subhuti_lexer_TokenCacheEntry = (this.shouldReadTokenDirectlyForRecognizer() ? this.readTokenDirectlyForRecognizer(mode) : this._getOrParseTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), mode));
    let token: com_subhuti_struct_SubhutiMatchToken = (__qin_binary__("!=", tokenEntry, null) ? tokenEntry.getToken() : null);
    if ((__qin_binary__("==", token, null) || token.isEof() || !__QinJavaLangString.equals(token.tokenName(), tokenName))) {
      {
        const __qin_typed_receiver_79: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_79.setParseFail();
      }
      return null;
    }
    {
      const __qin_typed_receiver_80: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_80.advanceTokenCursor(tokenEntry);
    }
    {
      const __qin_typed_receiver_81: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_81.recordParsedTokenForState(token);
    }
    if (this.isErrorRecoveryMode()) {
      {
        const __qin_typed_receiver_82: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_82.recordConsumedToken(token);
      }
    }
    if (this.__qin_field_buildCst) {
      {
        const __qin_typed_receiver_83: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_83.generateCstByToken(token);
      }
    }
    return token;
  }
  _lookaheadTokenMatch(offset: number): com_subhuti_struct_SubhutiMatchToken {
    return this.LA(offset);
  }
  _markParseFail(): void {
    {
      const __qin_typed_receiver_84: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_84.setParseFail();
    }
    return null;
  }
  recordConsumedToken(token: com_subhuti_struct_SubhutiMatchToken): void {
    if ((!this.isErrorRecoveryMode() || __qin_binary__("==", token, null) || __qin_collection_is_empty__(this.__qin_field_parseRecordStack))) {
      return null;
    }
    let tokenStartIndex: number = Math.max(0.0, __qin_binary__("-", this.__qin_field_tokenCursor, 1.0));
    let tokenEndIndex: number = this.__qin_field_tokenCursor;
    let tokenNode: com_subhuti_parser_ParseRecordNode = new com_subhuti_parser_ParseRecordNode(token.tokenName());
    {
      const __qin_typed_receiver_85: com_subhuti_parser_ParseRecordNode = tokenNode;
      __qin_typed_receiver_85.setToken(token);
    }
    {
      const __qin_typed_receiver_86: com_subhuti_parser_ParseRecordNode = tokenNode;
      __qin_typed_receiver_86.setValue(token.value());
    }
    {
      const __qin_typed_receiver_87: com_subhuti_parser_ParseRecordNode = tokenNode;
      __qin_typed_receiver_87.setStartTokenIndex(tokenStartIndex);
    }
    {
      const __qin_typed_receiver_88: com_subhuti_parser_ParseRecordNode = tokenNode;
      __qin_typed_receiver_88.setEndTokenIndex(tokenEndIndex);
    }
    let currentRecord: com_subhuti_parser_ParseRecordNode = __qin_collection_get__(this.__qin_field_parseRecordStack, __qin_binary__("-", __qin_collection_size__(this.__qin_field_parseRecordStack), 1.0));
    currentRecord.getChildren().add(tokenNode);
    for (let i: number = __qin_binary__("-", __qin_collection_size__(this.__qin_field_parseRecordStack), 1.0); __qin_binary__(">=", i, 0.0); i--) {
      let ancestor: com_subhuti_parser_ParseRecordNode = __qin_collection_get__(this.__qin_field_parseRecordStack, i);
      if (__qin_binary__(">", tokenEndIndex, ancestor.getEndTokenIndex())) {
        {
          const __qin_typed_receiver_89: com_subhuti_parser_ParseRecordNode = ancestor;
          __qin_typed_receiver_89.setEndTokenIndex(tokenEndIndex);
        }
      }
    }
    return null;
  }
  shouldReadTokenDirectlyForRecognizer(): boolean {
    return (!this.__qin_field_buildCst && !this.isErrorRecoveryMode() && this.__qin_field_tokenCache.isEmpty());
  }
  readTokenDirectlyForRecognizer(mode: com_subhuti_struct_LexerMode): com_subhuti_lexer_TokenCacheEntry {
    let cached: com_subhuti_lexer_TokenCacheEntry = this.cachedCurrentTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), mode);
    if (__qin_binary__("!=", cached, null)) {
      this.__qin_field_currentTokenEntryCacheHits++;
      return cached;
    }
    let preTokenizedEntry: com_subhuti_lexer_TokenCacheEntry = this.preTokenizedCurrentEntryWithoutCounters(mode);
    if (__qin_binary__("!=", preTokenizedEntry, null)) {
      {
        const __qin_typed_receiver_90: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_90.cacheCurrentTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), mode, preTokenizedEntry);
      }
      return preTokenizedEntry;
    }
    let regexpNegativeEntry: com_subhuti_lexer_TokenCacheEntry = this.preTokenizedRegexpNegativeEntry(this.__qin_field_currentIndex, mode);
    if (__qin_binary__("!=", regexpNegativeEntry, null)) {
      this.__qin_field_preTokenizedRegexpNegativeHits++;
      return regexpNegativeEntry;
    }
    return this.__qin_field_lexer.readTokenAt(this.__qin_field_sourceCode, this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), mode, this.__qin_field_lastTokenName);
  }
  preTokenizedFastEntryAtCurrentOrdinal(index: number, mode: com_subhuti_struct_LexerMode): com_subhuti_lexer_TokenCacheEntry {
    if (__qin_binary__("!=", index, this.__qin_field_currentIndex)) {
      return null;
    }
    let entry: com_subhuti_lexer_TokenCacheEntry = this.preTokenizedEntryAtParsedOrdinal(1.0, mode);
    if (__qin_binary__("!=", entry, null)) {
      this.__qin_field_tokenStreamGets++;
      this.__qin_field_tokenStreamHits++;
    }
    return entry;
  }
  executeRuleWrapper(...__qin_args: any[]): any {
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "function") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string")) return this.__qin_overload_executeRuleWrapper_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 4 && (__qin_args[0] === null || typeof __qin_args[0] === "function") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string") && (__qin_args[3] === null || typeof __qin_args[3] !== "undefined")) return this.__qin_overload_executeRuleWrapper_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    if (__qin_args.length === 5 && (__qin_args[0] === null || typeof __qin_args[0] === "function") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string") && (__qin_args[3] === null || typeof __qin_args[3] !== "undefined") && typeof __qin_args[4] === "boolean") return this.__qin_overload_executeRuleWrapper_5_2(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    if (__qin_args.length === 6 && (__qin_args[0] === null || typeof __qin_args[0] === "function") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string") && (__qin_args[3] === null || typeof __qin_args[3] !== "undefined") && typeof __qin_args[4] === "boolean" && typeof __qin_args[5] === "number") return this.__qin_overload_executeRuleWrapper_6_3(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5]);
    throw new Error("Unsupported Java overload: executeRuleWrapper/" + __qin_args.length);
  }
  __qin_overload_executeRuleWrapper_3_0(targetFun: QinJavaSupplier, ruleName: string, className: string): any {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    return this.executeRuleWrapper(__qin_functional_targetFun_0, ruleName, className, "");
  }
  __qin_overload_executeRuleWrapper_4_1(targetFun: QinJavaSupplier, ruleName: string, className: string, cacheKeyExtra: any): any {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    return this.executeRuleWrapper(__qin_functional_targetFun_0, ruleName, className, cacheKeyExtra, true);
  }
  __qin_overload_executeRuleWrapper_5_2(targetFun: QinJavaSupplier, ruleName: string, className: string, cacheKeyExtra: any, cacheRule: boolean): any {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    return this.executeRuleWrapper(__qin_functional_targetFun_0, ruleName, className, cacheKeyExtra, cacheRule, __qin_binary__("-", 0.0, 1.0));
  }
  __qin_overload_executeRuleWrapper_6_3(targetFun: QinJavaSupplier, ruleName: string, className: string, cacheKeyExtra: any, cacheRule: boolean, ruleId: number): any {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    if ((!this.__qin_field_parseSuccess)) {
      return null;
    }
    let isTopLevel: boolean = (__qin_binary__("==", this.__qin_field_ruleExecutionDepth, 0.0) && !this.hasActiveStaticRuleExecution());
    if (isTopLevel) {
      {
        const __qin_typed_receiver_91: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_91.initTopLevelData();
      }
      if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
        {
          const __qin_typed_receiver_92: com_subhuti_debug_SubhutiTraceDebugger = this.__qin_field__debugger;
          __qin_typed_receiver_92.resetForNewParse(null);
        }
      }
    }
    if ((!isTopLevel && this.canInlineRecognizerRule(ruleName))) {
      this.__qin_field_ruleWrapperPassThroughSkips++;
      {
        const __qin_typed_receiver_93: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_93.incrementRuleProfile(this.__qin_field_ruleWrapperPassThroughCounts, ruleName);
      }
      if (this.isRecognizerTerminalLeafRule(ruleName)) {
        this.__qin_field_ruleWrapperTerminalLeafSkips++;
        {
          const __qin_typed_receiver_94: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_94.incrementRuleProfile(this.__qin_field_ruleWrapperTerminalLeafCounts, ruleName);
        }
      } else {
        this.__qin_field_ruleWrapperRuleChainSkips++;
        {
          const __qin_typed_receiver_95: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_95.incrementRuleProfile(this.__qin_field_ruleWrapperRuleChainCounts, ruleName);
        }
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
    {
      const __qin_typed_receiver_96: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_96.incrementRuleProfile(this.__qin_field_ruleWrapperCallCounts, ruleName);
    }
    this.__qin_field_activeRuleScopeStack.addLast(ruleName);
    let tokenIndex: number = this.currentTokenIndex();
    let invocationCursorStamp: number = this.cursorStamp();
    let memoizeRule: boolean = (!isTopLevel && this.shouldMemoizeRule(ruleName, cacheRule));
    let key: any = (memoizeRule ? this.ruleCacheKey(ruleName, cacheKeyExtra, invocationCursorStamp, this.__qin_field_currentMode, this.__qin_field_lastTokenName) : null);
    let activeRuleInvocations: com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations = null;
    if ((__qin_binary__(">=", ruleId, 0.0) && this.__qin_field_indexedRuleInvocationsEnabled)) {
      this.__qin_field_indexedRuleInvocationLookups++;
      if (__qin_binary__(">=", ruleId, this.__qin_field_indexedActiveRuleInvocations.length)) {
        let nextLength: number = this.__qin_field_indexedActiveRuleInvocations.length;
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
    let ruleInvocationEntered: boolean = activeRuleInvocations.enter(cacheKeyExtra, invocationCursorStamp, this.__qin_field_currentMode, this.__qin_field_lastTokenName);
    if ((!ruleInvocationEntered)) {
      {
        const __qin_typed_receiver_97: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_97.setParseFail();
      }
      this.__qin_field_activeRuleScopeStack.removeLast();
      this.__qin_field_ruleExecutionDepth--;
      return null;
    }
    let startTime: number = 0.0;
    if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
      startTime = this.__qin_field__debugger.onRuleEnter(ruleName, tokenIndex);
    }
    let ruleProfileStackPushed: boolean = false;
    try {
      if (this.__qin_field_coreRuleProfileEnabled) {
        this.__qin_field_activeRuleProfileStack.addLast(ruleName);
        ruleProfileStackPushed = true;
      }
      if (memoizeRule) {
        let cached: any = this.ruleCache().get(key);
        if (cached.isPresent()) {
          this.__qin_field_ruleCacheHits++;
          {
            const __qin_typed_receiver_98: com_subhuti_parser_SubhutiParserCore = this;
            __qin_typed_receiver_98.recordAdaptiveRuleCacheHit(ruleName);
          }
          {
            const __qin_typed_receiver_99: com_subhuti_parser_SubhutiParserCore = this;
            __qin_typed_receiver_99.incrementRuleProfile(this.__qin_field_ruleCacheHitCounts, ruleName);
          }
          {
            const __qin_typed_receiver_100: com_subhuti_parser_SubhutiParserCore = this;
            __qin_typed_receiver_100.incrementCacheHitTokenSpan(ruleName, cached.get(), tokenIndex);
          }
          if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
            {
              const __qin_typed_receiver_101: com_subhuti_debug_SubhutiTraceDebugger = this.__qin_field__debugger;
              __qin_typed_receiver_101.onRuleExit(ruleName, true, startTime);
            }
          }
          if ((this.isErrorRecoveryMode() && (() => { const __qin_instanceof_value = cached.get(); return __qin_instanceof__(__qin_instanceof_value, com_subhuti_cache_SubhutiPackratCacheResult); })())) {
            let cacheResult: com_subhuti_cache_SubhutiPackratCacheResult = (cached.get() as com_subhuti_cache_SubhutiPackratCacheResult);
            if (__qin_binary__(">", cacheResult.__qin_field_endTokenIndex, tokenIndex)) {
              let recordNode: com_subhuti_parser_ParseRecordNode = new com_subhuti_parser_ParseRecordNode(ruleName);
              {
                const __qin_typed_receiver_102: com_subhuti_parser_ParseRecordNode = recordNode;
                __qin_typed_receiver_102.setStartTokenIndex(tokenIndex);
              }
              {
                const __qin_typed_receiver_103: com_subhuti_parser_ParseRecordNode = recordNode;
                __qin_typed_receiver_103.setEndTokenIndex(cacheResult.__qin_field_endTokenIndex);
              }
              if ((__qin_binary__("!=", cacheResult.__qin_field_recordNode, null) && __qin_binary__("!=", cacheResult.__qin_field_recordNode.getChildren(), null))) {
                {
                  const __qin_typed_receiver_104: com_subhuti_parser_ParseRecordNode = recordNode;
                  __qin_typed_receiver_104.setChildren(new __QinJavaUtilArrayList(cacheResult.__qin_field_recordNode.getChildren()));
                }
              }
              let recordParent: com_subhuti_parser_ParseRecordNode = (__qin_collection_is_empty__(this.__qin_field_parseRecordStack) ? null : __qin_collection_get__(this.__qin_field_parseRecordStack, __qin_binary__("-", __qin_collection_size__(this.__qin_field_parseRecordStack), 1.0)));
              if (__qin_binary__("!=", recordParent, null)) {
                recordParent.getChildren().add(recordNode);
              }
              for (let i: number = __qin_binary__("-", __qin_collection_size__(this.__qin_field_parseRecordStack), 1.0); __qin_binary__(">=", i, 0.0); i--) {
                let ancestor: com_subhuti_parser_ParseRecordNode = __qin_collection_get__(this.__qin_field_parseRecordStack, i);
                if (__qin_binary__(">", cacheResult.__qin_field_endTokenIndex, ancestor.getEndTokenIndex())) {
                  {
                    const __qin_typed_receiver_105: com_subhuti_parser_ParseRecordNode = ancestor;
                    __qin_typed_receiver_105.setEndTokenIndex(cacheResult.__qin_field_endTokenIndex);
                  }
                }
              }
            }
          }
          return this.applyCachedResult(cached.get());
        }
      }
      let startParsedTokenCount: number = this.currentTokenIndex();
      let startStoredTokenCount: number = __qin_collection_size__(this.__qin_field_parsedTokens);
      let recordNode: com_subhuti_parser_ParseRecordNode = null;
      if (this.isErrorRecoveryMode()) {
        recordNode = new com_subhuti_parser_ParseRecordNode(ruleName);
        {
          const __qin_typed_receiver_106: com_subhuti_parser_ParseRecordNode = recordNode;
          __qin_typed_receiver_106.setStartTokenIndex(startParsedTokenCount);
        }
        {
          const __qin_typed_receiver_107: com_subhuti_parser_ParseRecordNode = recordNode;
          __qin_typed_receiver_107.setEndTokenIndex(startParsedTokenCount);
        }
        this.__qin_field_parseRecordStack.add(recordNode);
      }
      let result: any = null;
      let producedCst: com_subhuti_struct_SubhutiCst = null;
      if (this.__qin_field_buildCst) {
        let executionResult: com_subhuti_parser_SubhutiParserCore$RuleExecutionResult = this.executeRuleCore(ruleName, __qin_functional_targetFun_0);
        result = executionResult.__qin_field_ruleResult;
        producedCst = executionResult.__qin_field_cst;
      } else {
        result = this.executeRuleCoreNoCst(ruleName, __qin_functional_targetFun_0);
      }
      if ((this.isErrorRecoveryMode() && __qin_binary__("!=", recordNode, null))) {
        this.__qin_field_parseRecordStack.remove(__qin_binary__("-", __qin_collection_size__(this.__qin_field_parseRecordStack), 1.0));
        if (__qin_binary__(">", recordNode.getEndTokenIndex(), recordNode.getStartTokenIndex())) {
          let recordParent: com_subhuti_parser_ParseRecordNode = (__qin_collection_is_empty__(this.__qin_field_parseRecordStack) ? null : __qin_collection_get__(this.__qin_field_parseRecordStack, __qin_binary__("-", __qin_collection_size__(this.__qin_field_parseRecordStack), 1.0)));
          if (__qin_binary__("!=", recordParent, null)) {
            recordParent.getChildren().add(recordNode);
          }
        }
      }
      if (memoizeRule) {
        let endParsedTokenCount: number = this.currentTokenIndex();
        let finalEndIndex: number = (__qin_binary__("!=", recordNode, null) ? Math.max(recordNode.getEndTokenIndex(), endParsedTokenCount) : endParsedTokenCount);
        let consumedTokens: __QinJavaUtilList<com_subhuti_struct_SubhutiMatchToken> = null;
        if ((this.__qin_field_parseSuccess && this.shouldStoreParsedTokens() && __qin_binary__(">", __qin_collection_size__(this.__qin_field_parsedTokens), startStoredTokenCount))) {
          consumedTokens = new __QinJavaUtilArrayList(new __QinJavaUtilArrayList(__qin_collection_to_array__(this.__qin_field_parsedTokens).slice(Number(startStoredTokenCount), Number(__qin_collection_size__(this.__qin_field_parsedTokens)))));
        }
        let cacheResult: com_subhuti_cache_SubhutiPackratCacheResult = new com_subhuti_cache_SubhutiPackratCacheResult(finalEndIndex, producedCst, this.__qin_field_parseSuccess, recordNode, consumedTokens, (this.__qin_field_parseSuccess ? this.__qin_field_currentIndex : __qin_binary__("-", 0.0, 1.0)), (this.__qin_field_parseSuccess ? this.__qin_field_currentPosition.line() : __qin_binary__("-", 0.0, 1.0)), (this.__qin_field_parseSuccess ? this.__qin_field_currentPosition.column() : __qin_binary__("-", 0.0, 1.0)), (this.__qin_field_parseSuccess ? this.__qin_field_lastTokenName : null));
        this.__qin_field_ruleCachePuts++;
        {
          const __qin_typed_receiver_108: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_108.recordAdaptiveRuleCachePut(ruleName);
        }
        {
          const __qin_typed_receiver_109: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_109.incrementRuleProfile(this.__qin_field_ruleCachePutCounts, ruleName);
        }
        {
          const __qin_typed_receiver_110: com_subhuti_cache_SubhutiPackratCache = this.ruleCache();
          __qin_typed_receiver_110.put(key, cacheResult);
        }
      }
      if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
        {
          const __qin_typed_receiver_111: com_subhuti_debug_SubhutiTraceDebugger = this.__qin_field__debugger;
          __qin_typed_receiver_111.onRuleExit(ruleName, false, startTime);
        }
      }
      if ((isTopLevel && this.__qin_field_parseSuccess)) {
        if ((__qin_binary__("!=", this.__qin_field__debugger, null) && __qin_binary__("!=", this.__qin_field_rootCst, null))) {
          {
            const __qin_typed_receiver_112: com_subhuti_debug_SubhutiTraceDebugger = this.__qin_field__debugger;
            __qin_typed_receiver_112.setCst(this.__qin_field_rootCst);
          }
          {
            const __qin_typed_receiver_113: com_subhuti_debug_SubhutiTraceDebugger = this.__qin_field__debugger;
            __qin_typed_receiver_113.autoOutput();
          }
        }
        {
          const __qin_typed_receiver_114: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_114.validateTopLevelParseComplete();
        }
      }
      return result;
    } finally {
      if (ruleProfileStackPushed) {
        this.__qin_field_activeRuleProfileStack.removeLast();
      }
      if (ruleInvocationEntered) {
        {
          const __qin_typed_receiver_115: com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations = activeRuleInvocations;
          __qin_typed_receiver_115.exit();
        }
      }
      this.__qin_field_activeRuleScopeStack.removeLast();
      this.__qin_field_ruleExecutionDepth--;
    }
    return null;
  }
  executeVoidRuleWrapper(...__qin_args: any[]): void {
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "function") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string")) return this.__qin_overload_executeVoidRuleWrapper_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 4 && (__qin_args[0] === null || typeof __qin_args[0] === "function") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string") && (__qin_args[3] === null || typeof __qin_args[3] !== "undefined")) return this.__qin_overload_executeVoidRuleWrapper_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    if (__qin_args.length === 5 && (__qin_args[0] === null || typeof __qin_args[0] === "function") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string") && (__qin_args[3] === null || typeof __qin_args[3] !== "undefined") && typeof __qin_args[4] === "boolean") return this.__qin_overload_executeVoidRuleWrapper_5_2(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4]);
    if (__qin_args.length === 6 && (__qin_args[0] === null || typeof __qin_args[0] === "function") && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] === "string") && (__qin_args[3] === null || typeof __qin_args[3] !== "undefined") && typeof __qin_args[4] === "boolean" && typeof __qin_args[5] === "number") return this.__qin_overload_executeVoidRuleWrapper_6_3(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3], __qin_args[4], __qin_args[5]);
    throw new Error("Unsupported Java overload: executeVoidRuleWrapper/" + __qin_args.length);
  }
  __qin_overload_executeVoidRuleWrapper_3_0(targetFun: QinJavaRunnable, ruleName: string, className: string): void {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    {
      const __qin_typed_receiver_116: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_116.executeVoidRuleWrapper(__qin_functional_targetFun_0, ruleName, className, "", true);
    }
    return null;
  }
  __qin_overload_executeVoidRuleWrapper_4_1(targetFun: QinJavaRunnable, ruleName: string, className: string, cacheKeyExtra: any): void {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    {
      const __qin_typed_receiver_117: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_117.executeVoidRuleWrapper(__qin_functional_targetFun_0, ruleName, className, cacheKeyExtra, true);
    }
    return null;
  }
  __qin_overload_executeVoidRuleWrapper_5_2(targetFun: QinJavaRunnable, ruleName: string, className: string, cacheKeyExtra: any, cacheRule: boolean): void {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    {
      const __qin_typed_receiver_118: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_118.executeVoidRuleWrapper(__qin_functional_targetFun_0, ruleName, className, cacheKeyExtra, cacheRule, __qin_binary__("-", 0.0, 1.0));
    }
    return null;
  }
  __qin_overload_executeVoidRuleWrapper_6_3(targetFun: QinJavaRunnable, ruleName: string, className: string, cacheKeyExtra: any, cacheRule: boolean, ruleId: number): void {
    const __qin_functional_targetFun_0 = __qin_java_functional(targetFun);
    if ((!this.__qin_field_firstTokenRecording && this.__qin_field_parseSuccess && this.tryExecuteDirectVoidRecognizerRule(ruleName, className))) {
      return null;
    }
    {
      const __qin_typed_receiver_119: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_119.executeRuleWrapper(__qin_java_functional(() => {
      __qin_functional_targetFun_0.run();
      return null;
    }), ruleName, className, cacheKeyExtra, cacheRule, ruleId);
    }
    return null;
  }
  tryExecuteDirectVoidRecognizerRule(ruleName: string, className: string): boolean {
    if ((this.__qin_field_buildCst || this.isErrorRecoveryMode() || __qin_binary__("!=", this.__qin_field__debugger, null) || __qin_binary__("==", ruleName, null) || __QinJavaLangString.isBlank(ruleName))) {
      return false;
    }
    let gast: com_subhuti_parser_SubhutiGastGrammar = this.effectiveGastGrammar();
    if ((__qin_binary__("==", gast, null) || __qin_binary__("==", gast.rule(ruleName), null))) {
      return false;
    }
    let runtimePlan: com_subhuti_parser_SubhutiParserRuntimePlan = this.parserRuntimePlan();
    let recognizerPlan: com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerPlan = runtimePlan.recognizerDirectRecognizerPlanRule(ruleName);
    if (__qin_binary__("!=", recognizerPlan, null)) {
      {
        const __qin_typed_receiver_120: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_120.useDefaultModeTokenArrayInputForTopLevelDirectPlan(recognizerPlan.defaultModeOnly());
      }
      return this.executeDirectVoidRecognizerPlan(ruleName, className, recognizerPlan);
    }
    let terminalSequence: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminalSequence = runtimePlan.recognizerDirectTerminalSequenceRule(ruleName);
    if (__qin_binary__("!=", terminalSequence, null)) {
      {
        const __qin_typed_receiver_121: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_121.useDefaultModeTokenArrayInputForTopLevelDirectPlan(terminalSequence.defaultModeOnly());
      }
      return this.executeDirectVoidRecognizerTerminalSequence(ruleName, terminalSequence);
    }
    let terminal: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal = runtimePlan.recognizerDirectTerminalRule(ruleName);
    if (__qin_binary__("==", terminal, null)) {
      return false;
    }
    {
      const __qin_typed_receiver_122: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_122.useDefaultModeTokenArrayInputForTopLevelDirectPlan(terminal.defaultMode());
    }
    return this.executeDirectVoidRecognizerTerminal(ruleName, terminal);
  }
  useDefaultModeTokenArrayInputForTopLevelDirectPlan(defaultModeOnly: boolean): void {
    if ((!defaultModeOnly || this.__qin_field_preTokenizedDefaultModeInput || __qin_binary__("!=", this.__qin_field_ruleExecutionDepth, 0.0) || __qin_binary__("!=", this.__qin_field_currentIndex, 0.0) || __qin_binary__("!=", this.__qin_field_tokenCursor, 0.0) || !this.__qin_field_tokenCache.isEmpty())) {
      return null;
    }
    {
      const __qin_typed_receiver_123: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_123.useDefaultModeTokenArrayInput();
    }
    return null;
  }
  executeDirectVoidRecognizerPlan(ruleName: string, className: string, recognizerPlan: com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerPlan): boolean {
    let isTopLevel: boolean = (__qin_binary__("==", this.__qin_field_ruleExecutionDepth, 0.0) && !this.hasActiveStaticRuleExecution() && this.isConcreteRuleOwner(className));
    if (isTopLevel) {
      {
        const __qin_typed_receiver_124: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_124.initTopLevelData();
      }
      if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
        {
          const __qin_typed_receiver_125: com_subhuti_debug_SubhutiTraceDebugger = this.__qin_field__debugger;
          __qin_typed_receiver_125.resetForNewParse(null);
        }
      }
    }
    this.__qin_field_ruleWrapperDirectRecognizerPlanSkips++;
    this.__qin_field_ruleWrapperPassThroughSkips++;
    {
      const __qin_typed_receiver_126: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_126.incrementRuleProfile(this.__qin_field_ruleWrapperPassThroughCounts, ruleName);
    }
    {
      const __qin_typed_receiver_127: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_127.executeDirectPlanElements(recognizerPlan);
    }
    if ((isTopLevel && this.__qin_field_parseSuccess)) {
      {
        const __qin_typed_receiver_128: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_128.validateTopLevelParseComplete();
      }
    }
    return true;
  }
  isConcreteRuleOwner(className: string): boolean {
    if ((__qin_binary__("==", className, null) || __QinJavaLangString.isBlank(className))) {
      return true;
    }
    let concreteClass: __QinJavaLangClass = this.getClass();
    return (__QinJavaLangString.equals(className, concreteClass.getSimpleName()) || __QinJavaLangString.equals(className, concreteClass.getName()));
  }
  executeDirectPlanElements(recognizerPlan: com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerPlan): void {
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
  consumeDirectAlternation(element: com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerElement): boolean {
    if (__qin_binary__("<=", __qin_collection_size__(element.alternatives()), 2.0)) {
      return this.consumeDirectAlternationLinear(element.alternatives());
    }
    return this.consumeDirectAlternationDispatch(element.alternativesByFirstModeAndKey());
  }
  consumeDirectAlternationLinear(alternatives: __QinJavaUtilList<__QinJavaUtilList<com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal>>): boolean {
    if ((__qin_binary__("==", alternatives, null) || __qin_collection_is_empty__(alternatives))) {
      {
        const __qin_typed_receiver_129: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_129.setParseFail();
      }
      return false;
    }
    for (const alternative of alternatives) {
      if (this.canStartDirectTerminalSequence(alternative)) {
        return this.consumeDirectTerminals(alternative);
      }
    }
    {
      const __qin_typed_receiver_130: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_130.setParseFail();
    }
    {
      const __qin_typed_receiver_131: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_131.clearCurrentTokenEntryCache();
    }
    return false;
  }
  consumeDirectAlternationDispatch(alternativesByFirstModeAndKey: __QinJavaUtilHashMap<com_subhuti_struct_LexerMode, __QinJavaUtilHashMap<string, __QinJavaUtilList<com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal>>>): boolean {
    if ((__qin_binary__("==", alternativesByFirstModeAndKey, null) || alternativesByFirstModeAndKey.isEmpty())) {
      {
        const __qin_typed_receiver_132: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_132.setParseFail();
      }
      return false;
    }
    for (const modeEntry of alternativesByFirstModeAndKey.entrySet()) {
      let mode: com_subhuti_struct_LexerMode = modeEntry.getKey();
      let firstEntry: com_subhuti_lexer_TokenCacheEntry = this.preTokenizedCurrentEntryWithoutCounters(mode);
      if (__qin_binary__("==", firstEntry, null)) {
        firstEntry = this.directCurrentTokenEntry(mode);
      }
      let token: com_subhuti_struct_SubhutiMatchToken = (__qin_binary__("!=", firstEntry, null) ? firstEntry.getToken() : null);
      if ((__qin_binary__("==", token, null) || token.isEof())) {
        continue;
      }
      let alternative: __QinJavaUtilList<com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal> = modeEntry.getValue().get(this.directTokenKey(token));
      if (__qin_binary__("==", alternative, null)) {
        alternative = modeEntry.getValue().get(token.tokenName());
      }
      if (__qin_binary__("!=", alternative, null)) {
        return this.consumeDirectTerminalSequenceFromFirstEntry(alternative, firstEntry);
      }
    }
    {
      const __qin_typed_receiver_133: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_133.setParseFail();
    }
    {
      const __qin_typed_receiver_134: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_134.clearCurrentTokenEntryCache();
    }
    return false;
  }
  executeDirectVoidRecognizerTerminal(ruleName: string, terminal: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal): boolean {
    let isTopLevel: boolean = (__qin_binary__("==", this.__qin_field_ruleExecutionDepth, 0.0) && !this.hasActiveStaticRuleExecution());
    if (isTopLevel) {
      {
        const __qin_typed_receiver_135: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_135.initTopLevelData();
      }
      if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
        {
          const __qin_typed_receiver_136: com_subhuti_debug_SubhutiTraceDebugger = this.__qin_field__debugger;
          __qin_typed_receiver_136.resetForNewParse(null);
        }
      }
    }
    this.__qin_field_ruleWrapperDirectTerminalSkips++;
    this.__qin_field_ruleWrapperPassThroughSkips++;
    {
      const __qin_typed_receiver_137: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_137.incrementRuleProfile(this.__qin_field_ruleWrapperPassThroughCounts, ruleName);
    }
    {
      const __qin_typed_receiver_138: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_138.consumeDirectTerminal(terminal);
    }
    if ((isTopLevel && this.__qin_field_parseSuccess)) {
      {
        const __qin_typed_receiver_139: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_139.validateTopLevelParseComplete();
      }
    }
    return true;
  }
  executeDirectVoidRecognizerTerminalSequence(ruleName: string, terminalSequence: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminalSequence): boolean {
    let isTopLevel: boolean = (__qin_binary__("==", this.__qin_field_ruleExecutionDepth, 0.0) && !this.hasActiveStaticRuleExecution());
    if (isTopLevel) {
      {
        const __qin_typed_receiver_140: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_140.initTopLevelData();
      }
      if (__qin_binary__("!=", this.__qin_field__debugger, null)) {
        {
          const __qin_typed_receiver_141: com_subhuti_debug_SubhutiTraceDebugger = this.__qin_field__debugger;
          __qin_typed_receiver_141.resetForNewParse(null);
        }
      }
    }
    this.__qin_field_ruleWrapperDirectTerminalSkips++;
    this.__qin_field_ruleWrapperDirectTerminalSequenceSkips++;
    this.__qin_field_ruleWrapperPassThroughSkips++;
    {
      const __qin_typed_receiver_142: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_142.incrementRuleProfile(this.__qin_field_ruleWrapperPassThroughCounts, ruleName);
    }
    if (this.consumeDirectTerminalSequenceWithPreTokenizedCursor(terminalSequence)) {
      if ((isTopLevel && this.__qin_field_parseSuccess)) {
        {
          const __qin_typed_receiver_143: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_143.validateTopLevelParseComplete();
        }
      }
      return true;
    }
    {
      const __qin_typed_receiver_144: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_144.consumeDirectTerminals(terminalSequence.terminals());
    }
    if ((isTopLevel && this.__qin_field_parseSuccess)) {
      {
        const __qin_typed_receiver_145: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_145.validateTopLevelParseComplete();
      }
    }
    return true;
  }
  consumeDirectTerminals(terminals: __QinJavaUtilList<com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal>): boolean {
    if ((__qin_binary__("==", terminals, null) || __qin_collection_is_empty__(terminals))) {
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
  canStartDirectTerminalSequence(terminals: __QinJavaUtilList<com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal>): boolean {
    if ((__qin_binary__("==", terminals, null) || __qin_collection_is_empty__(terminals))) {
      return false;
    }
    let first: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal = __qin_collection_get__(terminals, 0.0);
    let entry: com_subhuti_lexer_TokenCacheEntry = this.preTokenizedCurrentEntryWithoutCounters(first.lexerMode());
    if (__qin_binary__("==", entry, null)) {
      entry = this.directCurrentTokenEntry(first.lexerMode());
    }
    let token: com_subhuti_struct_SubhutiMatchToken = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
    return this.tokenMatchesDirectTerminal(token, first);
  }
  consumeDirectTerminal(terminal: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal): boolean {
    let entry: com_subhuti_lexer_TokenCacheEntry = this.directCurrentTokenEntry(terminal.lexerMode());
    let token: com_subhuti_struct_SubhutiMatchToken = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
    if ((!this.tokenMatchesDirectTerminal(token, terminal))) {
      {
        const __qin_typed_receiver_146: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_146.setParseFail();
      }
      {
        const __qin_typed_receiver_147: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_147.clearCurrentTokenEntryCache();
      }
      return false;
    }
    {
      const __qin_typed_receiver_148: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_148.advanceTokenCursor(entry);
    }
    {
      const __qin_typed_receiver_149: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_149.recordParsedTokenForState(token);
    }
    {
      const __qin_typed_receiver_150: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_150.generateCstByToken(token);
    }
    {
      const __qin_typed_receiver_151: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_151.clearCurrentTokenEntryCache();
    }
    return true;
  }
  consumeDirectTerminalSequenceFromFirstEntry(terminals: __QinJavaUtilList<com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal>, firstEntry: com_subhuti_lexer_TokenCacheEntry): boolean {
    if ((__qin_binary__("==", terminals, null) || __qin_collection_is_empty__(terminals))) {
      return true;
    }
    if ((!this.consumeDirectTerminalEntry(__qin_collection_get__(terminals, 0.0), firstEntry))) {
      return false;
    }
    if (__qin_binary__("==", __qin_collection_size__(terminals), 1.0)) {
      return true;
    }
    if ((!this.__qin_field_buildCst && this.consumeDirectTerminalRangeWithPreTokenizedCursor(terminals, 1.0))) {
      return this.__qin_field_parseSuccess;
    }
    for (let i: number = 1.0; __qin_binary__("<", i, __qin_collection_size__(terminals)); i++) {
      if ((!this.consumeDirectTerminal(__qin_collection_get__(terminals, i)))) {
        return false;
      }
    }
    return true;
  }
  consumeDirectTerminalEntry(terminal: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal, entry: com_subhuti_lexer_TokenCacheEntry): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
    if ((!this.tokenMatchesDirectTerminal(token, terminal))) {
      {
        const __qin_typed_receiver_152: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_152.setParseFail();
      }
      {
        const __qin_typed_receiver_153: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_153.clearCurrentTokenEntryCache();
      }
      return false;
    }
    {
      const __qin_typed_receiver_154: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_154.advanceTokenCursor(entry);
    }
    {
      const __qin_typed_receiver_155: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_155.recordParsedTokenForState(token);
    }
    {
      const __qin_typed_receiver_156: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_156.generateCstByToken(token);
    }
    {
      const __qin_typed_receiver_157: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_157.clearCurrentTokenEntryCache();
    }
    return true;
  }
  tokenMatchesDirectTerminal(token: com_subhuti_struct_SubhutiMatchToken, terminal: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal): boolean {
    if ((__qin_binary__("==", token, null) || token.isEof() || !__QinJavaLangString.equals(token.tokenName(), terminal.tokenName()))) {
      return false;
    }
    return (__qin_binary__("==", terminal.tokenValue(), null) || __QinJavaUtilObjects.equals(token.tokenValue(), terminal.tokenValue()));
  }
  directTokenKey(token: com_subhuti_struct_SubhutiMatchToken): string {
    return com_subhuti_parser_SubhutiTokenPrediction.key(token.tokenName(), token.tokenValue());
  }
  preTokenizedCurrentEntryWithoutCounters(mode: com_subhuti_struct_LexerMode): com_subhuti_lexer_TokenCacheEntry {
    if ((!this.__qin_field_preTokenizedDefaultModeInput || __qin_binary__("==", this.__qin_field_preTokenizedEntriesByOrdinal, null) || (__qin_binary__("!=", mode, null) && !com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE.equals(mode)))) {
      return null;
    }
    return this.preTokenizedEntryAtParsedOrdinal(1.0, com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE);
  }
  directCurrentTokenEntry(mode: com_subhuti_struct_LexerMode): com_subhuti_lexer_TokenCacheEntry {
    return (this.shouldReadTokenDirectlyForRecognizer() ? this.readTokenDirectlyForRecognizer(mode) : this._getOrParseTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), mode));
  }
  consumeDirectTerminalSequenceWithPreTokenizedCursor(terminalSequence: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminalSequence): boolean {
    return this.consumeDirectTerminalListWithPreTokenizedCursor(terminalSequence.terminals());
  }
  consumeDirectTerminalListWithPreTokenizedCursor(terminals: __QinJavaUtilList<com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal>): boolean {
    return this.consumeDirectTerminalRangeWithPreTokenizedCursor(terminals, 0.0);
  }
  consumeDirectTerminalRangeWithPreTokenizedCursor(terminals: __QinJavaUtilList<com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal>, startIndex: number): boolean {
    if ((!this.__qin_field_preTokenizedDefaultModeInput || __qin_binary__("==", this.__qin_field_preTokenizedEntriesByOrdinal, null))) {
      return false;
    }
    if ((__qin_binary__("==", terminals, null) || __qin_binary__("<", startIndex, 0.0) || __qin_binary__(">", startIndex, __qin_collection_size__(terminals)))) {
      return false;
    }
    for (let i: number = startIndex; __qin_binary__("<", i, __qin_collection_size__(terminals)); i++) {
      let terminal: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal = __qin_collection_get__(terminals, i);
      let mode: com_subhuti_struct_LexerMode = terminal.lexerMode();
      if ((__qin_binary__("!=", mode, null) && !com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE.equals(mode))) {
        return false;
      }
    }
    for (let i: number = startIndex; __qin_binary__("<", i, __qin_collection_size__(terminals)); i++) {
      let entry: com_subhuti_lexer_TokenCacheEntry = this.preTokenizedEntryAtParsedOrdinal(1.0, com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE);
      let token: com_subhuti_struct_SubhutiMatchToken = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
      let terminal: com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal = __qin_collection_get__(terminals, i);
      if ((!this.tokenMatchesDirectTerminal(token, terminal))) {
        {
          const __qin_typed_receiver_158: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_158.setParseFail();
        }
        {
          const __qin_typed_receiver_159: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_159.clearCurrentTokenEntryCache();
        }
        return true;
      }
      {
        const __qin_typed_receiver_160: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_160.advanceTokenCursor(entry);
      }
      {
        const __qin_typed_receiver_161: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_161.recordParsedTokenForState(token);
      }
    }
    {
      const __qin_typed_receiver_162: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_162.clearCurrentTokenEntryCache();
    }
    return true;
  }
  validateTopLevelParseComplete(): void {
    if ((!this.__qin_field_parseSuccess || this.isEof())) {
      return null;
    }
    let nextToken: com_subhuti_struct_SubhutiMatchToken = this.LA(1.0);
    let tokenInfo: string = (__qin_binary__("!=", nextToken, null) ? __QinJavaLangString.format("\"%s\" (%s) at position %d", nextToken.value(), nextToken.tokenName(), this.__qin_field_currentIndex) : "EOF");
    let errorMessage: string = ("Parser internal error: parsing succeeded but source code remains unconsumed. " + "Next token: " + tokenInfo);
    if (this.isErrorRecoveryMode()) {
      {
        const __qin_typed_receiver_163: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_163.recordRecoveryDiagnostic(errorMessage, nextToken);
      }
    } else {
      if ((this.__qin_field_analysisMode && __qin_binary__("!=", this.__qin_field_errorHandler, null))) {
        {
          const __qin_typed_receiver_164: com_subhuti_error_SubhutiErrorHandler = this.__qin_field_errorHandler;
          __qin_typed_receiver_164.recordError(this.__qin_field_currentPosition, errorMessage, "EOF", tokenInfo);
        }
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
  shouldMemoizeRule(ruleName: string, cacheRule: boolean): boolean {
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
  canInlineRecognizerRule(ruleName: string): boolean {
    return (__qin_binary__("==", this.__qin_field__debugger, null) && (this.isRecognizerPassThroughRule(ruleName) || this.isRecognizerTerminalLeafRule(ruleName)));
  }
  isRecognizerPassThroughRule(ruleName: string): boolean {
    if ((this.__qin_field_buildCst || this.isErrorRecoveryMode())) {
      return false;
    }
    let passThroughRules: __QinJavaUtilSet<string> = this.recognizerPassThroughRules();
    return (!__qin_collection_is_empty__(passThroughRules) && __qin_collection_contains__(passThroughRules, ruleName));
  }
  recognizerPassThroughRules(): __QinJavaUtilSet<string> {
    if (__qin_binary__("!=", this.__qin_field_recognizerPassThroughRules, null)) {
      return this.__qin_field_recognizerPassThroughRules;
    }
    this.__qin_field_recognizerPassThroughRules = this.parserRuntimePlan().recognizerPassThroughRules();
    return this.__qin_field_recognizerPassThroughRules;
  }
  isRecognizerTerminalLeafRule(ruleName: string): boolean {
    if ((this.__qin_field_buildCst || this.isErrorRecoveryMode())) {
      return false;
    }
    let terminalLeafRules: __QinJavaUtilSet<string> = this.recognizerTerminalLeafRules();
    return (!__qin_collection_is_empty__(terminalLeafRules) && __qin_collection_contains__(terminalLeafRules, ruleName));
  }
  recognizerTerminalLeafRules(): __QinJavaUtilSet<string> {
    if (__qin_binary__("!=", this.__qin_field_recognizerTerminalLeafRules, null)) {
      return this.__qin_field_recognizerTerminalLeafRules;
    }
    this.__qin_field_recognizerTerminalLeafRules = this.parserRuntimePlan().recognizerTerminalLeafRules();
    return this.__qin_field_recognizerTerminalLeafRules;
  }
  recognizerLowYieldMemoRules(): __QinJavaUtilSet<string> {
    return __QinJavaUtilSet.of();
  }
  isRecognizerLowYieldMemoRule(ruleName: string): boolean {
    if ((this.__qin_field_buildCst || this.isErrorRecoveryMode() || __qin_binary__("==", this.__qin_field_speculativeParseDepth, 0.0))) {
      return false;
    }
    let lowYieldRules: __QinJavaUtilSet<string> = this.recognizerLowYieldMemoRules();
    return (!__qin_collection_is_empty__(lowYieldRules) && __qin_collection_contains__(lowYieldRules, ruleName));
  }
  isAdaptiveLowYieldMemoRule(ruleName: string): boolean {
    return (!this.__qin_field_buildCst && !this.isErrorRecoveryMode() && __qin_binary__(">", this.__qin_field_speculativeParseDepth, 0.0) && __qin_collection_contains__(this.__qin_field_adaptiveLowYieldMemoRules, ruleName));
  }
  recordAdaptiveRuleCacheHit(ruleName: string): void {
    if ((this.__qin_field_buildCst || this.isErrorRecoveryMode() || __qin_binary__("==", ruleName, null))) {
      return null;
    }
    this.__qin_field_adaptiveRuleCacheHitCounts.merge(ruleName, 1.0, ((...__qin_args) => __QinJavaLangInteger.sum(...__qin_args.slice(0, 2))));
    return null;
  }
  recordAdaptiveRuleCachePut(ruleName: string): void {
    if ((this.__qin_field_buildCst || this.isErrorRecoveryMode() || __qin_binary__("==", ruleName, null) || __qin_collection_contains__(this.__qin_field_adaptiveLowYieldMemoRules, ruleName))) {
      return null;
    }
    let hits: number = this.__qin_field_adaptiveRuleCacheHitCounts.getOrDefault(ruleName, 0.0);
    if (__qin_binary__(">", hits, 0.0)) {
      return null;
    }
    let puts: number = this.__qin_field_adaptiveRuleCachePutCounts.merge(ruleName, 1.0, ((...__qin_args) => __QinJavaLangInteger.sum(...__qin_args.slice(0, 2))));
    if (__qin_binary__(">=", puts, com_subhuti_parser_SubhutiParserCore.__qin_field_ADAPTIVE_LOW_YIELD_MEMO_MIN_PUTS)) {
      this.__qin_field_adaptiveLowYieldMemoRules.add(ruleName);
    }
    return null;
  }
  executeRuleCoreNoCst(ruleName: string, targetFun: QinJavaSupplier): any {
    const __qin_functional_targetFun_1 = __qin_java_functional(targetFun);
    this.__qin_field_ruleCoreExecutions++;
    {
      const __qin_typed_receiver_165: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_165.incrementRuleProfile(this.__qin_field_ruleCoreExecutionCounts, ruleName);
    }
    let result: any = __qin_functional_targetFun_1.get();
    if (this.__qin_field_parseSuccess) {
      {
        const __qin_typed_receiver_166: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_166.incrementRuleProfile(this.__qin_field_ruleCoreSuccessCounts, ruleName);
      }
    } else {
      {
        const __qin_typed_receiver_167: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_167.incrementRuleProfile(this.__qin_field_ruleCoreFailureCounts, ruleName);
      }
      if (__qin_binary__("!=", this.__qin_field_errorHandler, null)) {
        {
          const __qin_typed_receiver_168: com_subhuti_error_SubhutiErrorHandler = this.__qin_field_errorHandler;
          __qin_typed_receiver_168.recordRuleFailure(this.__qin_field_currentPosition, ruleName);
        }
      }
    }
    return result;
  }
  executeRuleCore(ruleName: string, targetFun: QinJavaSupplier): com_subhuti_parser_SubhutiParserCore$RuleExecutionResult {
    const __qin_functional_targetFun_1 = __qin_java_functional(targetFun);
    this.__qin_field_ruleCoreExecutions++;
    {
      const __qin_typed_receiver_169: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_169.incrementRuleProfile(this.__qin_field_ruleCoreExecutionCounts, ruleName);
    }
    this.__qin_field_ruleCstNodes++;
    let cst: com_subhuti_struct_SubhutiCst = com_subhuti_struct_SubhutiCst.builder().name(ruleName).build();
    this.__qin_field_cstStack.add(cst);
    try {
      let result: any = __qin_functional_targetFun_1.get();
      if (this.__qin_field_parseSuccess) {
        {
          const __qin_typed_receiver_170: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_170.incrementRuleProfile(this.__qin_field_ruleCoreSuccessCounts, ruleName);
        }
      } else {
        {
          const __qin_typed_receiver_171: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_171.incrementRuleProfile(this.__qin_field_ruleCoreFailureCounts, ruleName);
        }
      }
      let finalCst: com_subhuti_struct_SubhutiCst = __qin_collection_get__(this.__qin_field_cstStack, __qin_binary__("-", __qin_collection_size__(this.__qin_field_cstStack), 1.0));
      this.__qin_field_cstStack.remove(__qin_binary__("-", __qin_collection_size__(this.__qin_field_cstStack), 1.0));
      if (this.__qin_field_parseSuccess) {
        {
          const __qin_typed_receiver_172: com_subhuti_parser_SubhutiParserCore = this;
          __qin_typed_receiver_172.setLocation(finalCst);
        }
        let parentCst: com_subhuti_struct_SubhutiCst = this.getCurCst();
        if (__qin_binary__("!=", parentCst, null)) {
          {
            const __qin_typed_receiver_173: com_subhuti_struct_SubhutiCst = parentCst;
            __qin_typed_receiver_173.addChild(finalCst);
          }
        } else {
          this.__qin_field_rootCst = finalCst;
        }
      } else {
        if (__qin_binary__("!=", this.__qin_field_errorHandler, null)) {
          {
            const __qin_typed_receiver_174: com_subhuti_error_SubhutiErrorHandler = this.__qin_field_errorHandler;
            __qin_typed_receiver_174.recordRuleFailure(this.__qin_field_currentPosition, ruleName);
          }
        }
      }
      return new com_subhuti_parser_SubhutiParserCore$RuleExecutionResult(result, finalCst);
    } catch (e) {
      if (!(e instanceof __QinJavaLangException)) {
        throw e;
      }
      this.__qin_field_cstStack.remove(__qin_binary__("-", __qin_collection_size__(this.__qin_field_cstStack), 1.0));
      throw e;
    }
    return null;
  }
  applyCachedResult(cached: any): any {
    if ((!(() => { const __qin_instanceof_value = cached; return __qin_instanceof__(__qin_instanceof_value, com_subhuti_cache_SubhutiPackratCacheResult); })())) {
      return (cached);
    }
    let cacheResult: com_subhuti_cache_SubhutiPackratCacheResult = (cached as com_subhuti_cache_SubhutiPackratCacheResult);
    if ((__qin_binary__("!=", cacheResult.__qin_field_parsedTokens, null) && !__qin_collection_is_empty__(cacheResult.__qin_field_parsedTokens))) {
      this.__qin_field_parsedTokens.addAll(cacheResult.__qin_field_parsedTokens);
    }
    this.__qin_field_tokenCursor = cacheResult.__qin_field_endTokenIndex;
    if (__qin_binary__(">=", cacheResult.__qin_field_endCodeIndex, 0.0)) {
      this.__qin_field_currentIndex = cacheResult.__qin_field_endCodeIndex;
      this.__qin_field_currentPosition = com_subhuti_struct_SubhutiPosition.of(cacheResult.__qin_field_endLine, cacheResult.__qin_field_endColumn, cacheResult.__qin_field_endCodeIndex);
      this.__qin_field_lastTokenName = cacheResult.__qin_field_lastTokenName;
    } else {
      if ((__qin_binary__("!=", cacheResult.__qin_field_parsedTokens, null) && !__qin_collection_is_empty__(cacheResult.__qin_field_parsedTokens))) {
        let lastToken: com_subhuti_struct_SubhutiMatchToken = __qin_collection_get__(cacheResult.__qin_field_parsedTokens, __qin_binary__("-", __qin_collection_size__(cacheResult.__qin_field_parsedTokens), 1.0));
        this.__qin_field_currentIndex = lastToken.endOffset();
        this.__qin_field_currentPosition = lastToken.endPosition();
        this.__qin_field_lastTokenName = lastToken.tokenName();
        this.__qin_field_tokenCursor = (this.__qin_field_preTokenizedDefaultModeInput ? this.tokenOrdinalAtCodeIndex(this.__qin_field_currentIndex) : cacheResult.__qin_field_endTokenIndex);
      }
    }
    this.__qin_field_parseSuccess = cacheResult.__qin_field_parseSuccess;
    if ((this.__qin_field_buildCst && cacheResult.__qin_field_parseSuccess && (() => { const __qin_instanceof_value = cacheResult.__qin_field_cst; return __qin_instanceof__(__qin_instanceof_value, com_subhuti_struct_SubhutiCst); })())) {
      let cachedCst: com_subhuti_struct_SubhutiCst = (cacheResult.__qin_field_cst as com_subhuti_struct_SubhutiCst);
      let parentCst: com_subhuti_struct_SubhutiCst = (__qin_collection_is_empty__(this.__qin_field_cstStack) ? null : __qin_collection_get__(this.__qin_field_cstStack, __qin_binary__("-", __qin_collection_size__(this.__qin_field_cstStack), 1.0)));
      if (__qin_binary__("!=", parentCst, null)) {
        this.__qin_field_cachedCstAttachCount++;
        {
          const __qin_typed_receiver_175: com_subhuti_struct_SubhutiCst = parentCst;
          __qin_typed_receiver_175.addChild(cachedCst);
        }
      } else {
        this.__qin_field_cachedCstAttachCount++;
        this.__qin_field_rootCst = cachedCst;
      }
    }
    return (cacheResult.__qin_field_cst);
  }
  throwLoopError(ruleName: string): void {
    if (this.__qin_field_analysisMode) {
      {
        const __qin_typed_receiver_176: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_176.setParseFail();
      }
      return null;
    }
    let currentToken: com_subhuti_struct_SubhutiMatchToken = this.LA(1.0);
    let tokenContext: __QinJavaUtilList<com_subhuti_struct_SubhutiMatchToken> = this.getTokenContext(2.0);
    let cacheStats: string = this.getCacheStats();
    let ruleStack: __QinJavaUtilList<string> = this.getRuleStack();
    let isLeftRecursion: boolean = this.isDirectLeftRecursion(ruleName, ruleStack);
    let errorType: string = (isLeftRecursion ? "left-recursion" : "or-branch-shadowing");
    let hint: string = "检查规则定义，确保在递归前消费了 token";
    let errorMessage: string = __QinJavaLangString.format("[%s] Rule '%s' detected infinite loop at token[%d]\nHint: %s", errorType, ruleName, this.currentTokenIndex(), hint);
    throw new __QinJavaLangRuntimeException(errorMessage);
  }
  isDirectLeftRecursion(ruleName: string, ruleStack: __QinJavaUtilList<string>): boolean {
    let ruleCounts: __QinJavaUtilHashMap<string, number | null> = new __QinJavaUtilHashMap();
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
  handleTopLevelError(ruleName: string, startTokenIndex: number): void {
    if (this.__qin_field_analysisMode) {
      return null;
    }
    let noTokenConsumed: boolean = __qin_binary__("==", this.currentTokenIndex(), startTokenIndex);
    let found: com_subhuti_struct_SubhutiMatchToken = this.LA(1.0);
    let expected: string = (noTokenConsumed ? "valid syntax" : "EOF (end of file)");
    let foundStr: string = (__qin_binary__("!=", found, null) ? found.tokenName() : "EOF");
    let errorMessage: string = __QinJavaLangString.format("Parsing Error at token[%d] line %d:%d: Expected %s, found %s", this.currentTokenIndex(), this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), expected, foundStr);
    let stack: __QinJavaUtilList<string> = this.getRuleStack();
    if (__qin_collection_is_empty__(stack)) {
      stack = __QinJavaUtilList.of(ruleName);
    }
    errorMessage += ("\nRule stack: " + __QinJavaLangString.join(" -> ", stack));
    throw new __QinJavaLangRuntimeException(errorMessage);
  }
  getTokenContext(contextSize: number): __QinJavaUtilList<com_subhuti_struct_SubhutiMatchToken> {
    return new __QinJavaUtilArrayList(new __QinJavaUtilArrayList(__qin_collection_to_array__(this.__qin_field_parsedTokens).slice(Number(Math.max(0.0, __qin_binary__("-", __qin_collection_size__(this.__qin_field_parsedTokens), contextSize))), Number(__qin_collection_size__(this.__qin_field_parsedTokens)))));
  }
  isEof(): boolean {
    if ((this.__qin_field_preTokenizedDefaultModeInput && (__qin_binary__("==", this.__qin_field_currentMode, null) || com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE.equals(this.__qin_field_currentMode)))) {
      let entry: com_subhuti_lexer_TokenCacheEntry = this.preTokenizedEntryAtParsedOrdinal(1.0, com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE);
      if (__qin_binary__("!=", entry, null)) {
        let token: com_subhuti_struct_SubhutiMatchToken = entry.getToken();
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
    let entry: com_subhuti_lexer_TokenCacheEntry = this._getOrParseTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), this.__qin_field_currentMode);
    let token: com_subhuti_struct_SubhutiMatchToken = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
    return (__qin_binary__("==", token, null) || token.isEof());
  }
  LA(...__qin_args: any[]): com_subhuti_struct_SubhutiMatchToken {
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_LA_1_0(__qin_args[0]);
    if (__qin_args.length === 2 && typeof __qin_args[0] === "number" && (__qin_args[1] === null || Array.isArray(__qin_args[1]) || __qin_instanceof__(__qin_args[1], __QinJavaUtilArrayList) || __qin_instanceof__(__qin_args[1], __QinJavaUtilUnmodifiableList))) return this.__qin_overload_LA_2_1(__qin_args[0], (Array.isArray(__qin_args[1]) ? new __QinJavaUtilArrayList(__qin_args[1]) : __qin_args[1]));
    throw new Error("Unsupported Java overload: LA/" + __qin_args.length);
  }
  __qin_overload_LA_1_0(offset: number): com_subhuti_struct_SubhutiMatchToken {
    if (this.__qin_field_firstTokenRecording) {
      throw new com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException();
    }
    if (__qin_binary__("<", offset, 1.0)) {
      throw new __QinJavaLangIllegalArgumentException("offset must be >= 1");
    }
    let tokenArrayEntry: com_subhuti_lexer_TokenCacheEntry = this.preTokenizedEntryAtParsedOrdinal(offset, this.__qin_field_currentMode);
    if (__qin_binary__("!=", tokenArrayEntry, null)) {
      this.__qin_field_tokenStreamGets++;
      this.__qin_field_tokenStreamHits++;
      return tokenArrayEntry.getToken();
    }
    if ((this.__qin_field_preTokenizedDefaultModeInput && (__qin_binary__("==", this.__qin_field_currentMode, null) || com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE.equals(this.__qin_field_currentMode)) && __qin_binary__(">=", __qin_binary__("-", __qin_binary__("+", this.__qin_field_tokenCursor, offset), 1.0), this.__qin_field_preTokenizedEntriesByOrdinal.length))) {
      return null;
    }
    if ((!this.__qin_field_preTokenizedDefaultModeInput && !this.isErrorRecoveryMode() && !this.__qin_field_lexer.dependsOnPreviousTokenName())) {
      let entry: com_subhuti_lexer_TokenCacheEntry = this.sourceLookaheadEntry(offset);
      return (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
    }
    if ((!this.__qin_field_buildCst && !this.isErrorRecoveryMode() && __qin_binary__("==", offset, 1.0))) {
      let preTokenizedEntry: com_subhuti_lexer_TokenCacheEntry = this.preTokenizedCurrentEntryWithoutCounters(this.__qin_field_currentMode);
      if (__qin_binary__("!=", preTokenizedEntry, null)) {
        return preTokenizedEntry.getToken();
      }
      let entry: com_subhuti_lexer_TokenCacheEntry = this._getOrParseTokenEntry(this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), this.__qin_field_currentMode);
      return (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
    }
    let index: number = this.__qin_field_currentIndex;
    let pos: com_subhuti_struct_SubhutiPosition = this.__qin_field_currentPosition;
    for (let i: number = 0.0; __qin_binary__("<", i, offset); i++) {
      let entry: com_subhuti_lexer_TokenCacheEntry = this._getOrParseTokenEntry(index, pos.line(), pos.column(), this.__qin_field_currentMode);
      let token: com_subhuti_struct_SubhutiMatchToken = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
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
  __qin_overload_LA_2_1(offset: number, modes: __QinJavaUtilList<com_subhuti_struct_LexerMode>): com_subhuti_struct_SubhutiMatchToken {
    if (this.__qin_field_firstTokenRecording) {
      throw new com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException();
    }
    if (__qin_binary__("<", offset, 1.0)) {
      throw new __QinJavaLangIllegalArgumentException("offset must be >= 1");
    }
    let index: number = this.__qin_field_currentIndex;
    let pos: com_subhuti_struct_SubhutiPosition = this.__qin_field_currentPosition;
    for (let i: number = 0.0; __qin_binary__("<", i, offset); i++) {
      let entry: com_subhuti_lexer_TokenCacheEntry = null;
      let token: com_subhuti_struct_SubhutiMatchToken = null;
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
  sourceLookaheadEntry(offset: number): com_subhuti_lexer_TokenCacheEntry {
    if ((!this.sourceLookaheadMatchesCurrentState())) {
      {
        const __qin_typed_receiver_177: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_177.resetSourceLookahead();
      }
    }
    if (__qin_binary__("<=", offset, this.__qin_field_sourceLookaheadSize)) {
      this.__qin_field_sourceLookaheadCacheHits++;
      return this.__qin_field_sourceLookaheadEntries[__qin_binary__("-", offset, 1.0)];
    }
    if (this.__qin_field_sourceLookaheadTerminalReached) {
      this.__qin_field_sourceLookaheadCacheHits++;
      return this.__qin_field_sourceLookaheadTerminalEntry;
    }
    let index: number = this.__qin_field_currentIndex;
    let line: number = this.__qin_field_currentPosition.line();
    let column: number = this.__qin_field_currentPosition.column();
    if (__qin_binary__(">", this.__qin_field_sourceLookaheadSize, 0.0)) {
      let previous: com_subhuti_lexer_TokenCacheEntry = this.__qin_field_sourceLookaheadEntries[__qin_binary__("-", this.__qin_field_sourceLookaheadSize, 1.0)];
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
      let entry: com_subhuti_lexer_TokenCacheEntry = this._getOrParseTokenEntry(index, line, column, this.__qin_field_currentMode);
      {
        const __qin_typed_receiver_178: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_178.ensureSourceLookaheadCapacity(__qin_binary__("+", this.__qin_field_sourceLookaheadSize, 1.0));
      }
      this.__qin_field_sourceLookaheadEntries[this.__qin_field_sourceLookaheadSize++] = entry;
      this.__qin_field_sourceLookaheadCacheFills++;
      let token: com_subhuti_struct_SubhutiMatchToken = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
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
  sourceLookaheadMatchesCurrentState(): boolean {
    return (this.__qin_field_sourceLookaheadInitialized && __qin_binary__("==", this.__qin_field_sourceLookaheadStartIndex, this.__qin_field_currentIndex) && __qin_binary__("==", this.__qin_field_sourceLookaheadStartLine, this.__qin_field_currentPosition.line()) && __qin_binary__("==", this.__qin_field_sourceLookaheadStartColumn, this.__qin_field_currentPosition.column()) && __QinJavaUtilObjects.equals(this.__qin_field_sourceLookaheadMode, this.__qin_field_currentMode));
  }
  resetSourceLookahead(): void {
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
  ensureSourceLookaheadCapacity(capacity: number): void {
    if (__qin_binary__("<=", capacity, this.__qin_field_sourceLookaheadEntries.length)) {
      return null;
    }
    let nextCapacity: number = Math.max(capacity, __qin_binary__("*", this.__qin_field_sourceLookaheadEntries.length, 2.0));
    this.__qin_field_sourceLookaheadEntries = __QinJavaUtilArrays.copyOf(this.__qin_field_sourceLookaheadEntries, nextCapacity);
    return null;
  }
  cache(enable: boolean): com_subhuti_parser_SubhutiParserCore {
    this.__qin_field_enableMemoization = enable;
    return this;
  }
  debug(...__qin_args: any[]): com_subhuti_parser_SubhutiParserCore {
    if (__qin_args.length === 1 && typeof __qin_args[0] === "boolean") return this.__qin_overload_debug_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_debug_0_1();
    throw new Error("Unsupported Java overload: debug/" + __qin_args.length);
  }
  __qin_overload_debug_1_0(enable: boolean): com_subhuti_parser_SubhutiParserCore {
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
  __qin_overload_debug_0_1(): com_subhuti_parser_SubhutiParserCore {
    return this.debug(true);
  }
  recordParsedTokenForState(token: com_subhuti_struct_SubhutiMatchToken): void {
    if (this.shouldStoreParsedTokens()) {
      this.__qin_field_parsedTokens.add(token);
    }
    return null;
  }
  cst(enable: boolean): com_subhuti_parser_SubhutiParserCore {
    this.__qin_field_buildCst = enable;
    return this;
  }
  isCstEnabled(): boolean {
    return this.__qin_field_buildCst;
  }
  setLogFile(filePath: string): com_subhuti_parser_SubhutiParserCore {
    if (__qin_binary__("==", this.__qin_field__debugger, null)) {
      {
        const __qin_typed_receiver_179: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_179.debug(true);
      }
    }
    {
      const __qin_typed_receiver_180: com_subhuti_debug_SubhutiTraceDebugger = this.__qin_field__debugger;
      __qin_typed_receiver_180.setLogFile(filePath);
    }
    return this;
  }
  getDebugger(): com_subhuti_debug_SubhutiTraceDebugger {
    return this.__qin_field__debugger;
  }
  reset(): void {
    {
      const __qin_typed_receiver_181: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_181.initTopLevelData();
    }
    {
      const __qin_typed_receiver_182: com_subhuti_parser_SubhutiTokenCache = this.__qin_field_tokenCache;
      __qin_typed_receiver_182.clear();
    }
    {
      const __qin_typed_receiver_183: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_183.clearRuleCacheIfAllocated();
    }
    return null;
  }
  getCacheStats(): string {
    return this.ruleCacheStatsReport();
  }
  getRuntimePlanReport(): string {
    return (__qin_binary__("==", this.staticGrammarPlan(), null) ? this.parserRuntimePlan().coverageReport().toString() : this.staticGrammarPlan().coverageReport(this.getClass().getName()));
  }
  getStaticDecisionReport(...__qin_args: any[]): string {
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_getStaticDecisionReport_1_0(__qin_args[0]);
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && typeof __qin_args[2] === "number") return this.__qin_overload_getStaticDecisionReport_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: getStaticDecisionReport/" + __qin_args.length);
  }
  __qin_overload_getStaticDecisionReport_1_0(decisionId: number): string {
    return this.staticGrammarPlan().decisionPlan(decisionId).toString();
  }
  __qin_overload_getStaticDecisionReport_3_1(ruleId: number, variantId: number, occurrenceId: number): string {
    let plan: com_subhuti_parser_SubhutiStaticGrammarPlan = this.staticGrammarPlan();
    for (let decisionId: number = 0.0; __qin_binary__("<", decisionId, plan.decisionCount()); decisionId++) {
      let decision: com_subhuti_parser_SubhutiDecisionPlan = plan.decisionPlan(decisionId);
      if ((__qin_binary__("==", decision.ruleId(), ruleId) && __qin_binary__("==", decision.variantId(), variantId) && __qin_binary__("==", decision.occurrenceId(), occurrenceId))) {
        return ("decisionId=" + decisionId + ", " + decision);
      }
    }
    return "none";
  }
  getStaticDecisionMatchReport(decisionId: number): string {
    let decision: com_subhuti_parser_SubhutiDecisionPlan = this.staticGrammarPlan().decisionPlan(decisionId);
    let depth: number = Math.max(1.0, __qin_collection_size__(decision.lexerModes()));
    let tokens: com_subhuti_struct_SubhutiMatchToken[] = Array.from({ length: depth }, () => null);
    for (let index: number = 0.0; __qin_binary__("<", index, depth); index++) {
      tokens[index] = this.LA(__qin_binary__("+", index, 1.0));
    }
    return (decision + ", tokens=" + __QinJavaUtilArrays.toString(tokens) + ", candidates=" + decision.candidates(tokens, tokens.length) + ", selected=" + decision.select(tokens, tokens.length));
  }
  getLastStaticNoMatchReport(): string {
    return (__qin_binary__("<", this.__qin_field_lastStaticNoMatchDecisionId, 0.0) ? "none" : ("decisionId=" + this.__qin_field_lastStaticNoMatchDecisionId + ", " + this.getStaticDecisionMatchReport(this.__qin_field_lastStaticNoMatchDecisionId)));
  }
  getLastStaticAmbiguousReport(): string {
    return (__qin_binary__("<", this.__qin_field_lastStaticAmbiguousDecisionId, 0.0) ? "none" : ("decisionId=" + this.__qin_field_lastStaticAmbiguousDecisionId + ", " + this.getStaticDecisionMatchReport(this.__qin_field_lastStaticAmbiguousDecisionId)));
  }
  getActiveStaticRuleReport(): string {
    if (__qin_binary__("==", this.__qin_field_activeStaticRuleDepth, 0.0)) {
      return "none";
    }
    let report: __QinJavaLangStringBuilder = new __QinJavaLangStringBuilder();
    for (let frame: number = 0.0; __qin_binary__("<", frame, this.__qin_field_activeStaticRuleDepth); frame++) {
      if (__qin_binary__(">", frame, 0.0)) {
        report.append(" -> ");
      }
      report.append(this.staticGrammarPlan().staticRuleInvocationPlan(this.__qin_field_activeStaticRuleIds[frame], this.__qin_field_activeStaticVariantIds[frame]).ruleName());
    }
    return report.toString();
  }
  getCoreProfileStats(): string {
    return ("ruleWrapperCalls=" + this.__qin_field_ruleWrapperCalls + ", ruleCoreExecutions=" + this.__qin_field_ruleCoreExecutions + ", ruleCacheKeyBuilds=" + this.__qin_field_ruleCacheKeyBuilds + ", ruleCacheHits=" + this.__qin_field_ruleCacheHits + ", ruleCachePuts=" + this.__qin_field_ruleCachePuts + ", ruleCachePassThroughSkips=" + this.__qin_field_ruleCachePassThroughSkips + ", ruleCacheTerminalLeafSkips=" + this.__qin_field_ruleCacheTerminalLeafSkips + ", ruleCacheSpeculativeSkips=" + this.__qin_field_ruleCacheSpeculativeSkips + ", ruleCacheLowYieldSkips=" + this.__qin_field_ruleCacheLowYieldSkips + ", ruleCacheAdaptiveLowYieldSkips=" + this.__qin_field_ruleCacheAdaptiveLowYieldSkips + ", ruleWrapperPassThroughSkips=" + this.__qin_field_ruleWrapperPassThroughSkips + ", ruleWrapperTerminalLeafSkips=" + this.__qin_field_ruleWrapperTerminalLeafSkips + ", ruleWrapperRuleChainSkips=" + this.__qin_field_ruleWrapperRuleChainSkips + ", ruleWrapperDirectTerminalSkips=" + this.__qin_field_ruleWrapperDirectTerminalSkips + ", ruleWrapperDirectTerminalSequenceSkips=" + this.__qin_field_ruleWrapperDirectTerminalSequenceSkips + ", ruleWrapperDirectRecognizerPlanSkips=" + this.__qin_field_ruleWrapperDirectRecognizerPlanSkips + ", indexedRuleInvocationLookups=" + this.__qin_field_indexedRuleInvocationLookups + ", staticPrimitiveInvocationEntries=" + this.__qin_field_staticPrimitiveInvocationEntries + ", staticRootRuleEntries=" + this.__qin_field_staticRootRuleEntries + ", staticGateDispatches=" + this.__qin_field_staticGateDispatches + ", staticActionDispatches=" + this.__qin_field_staticActionDispatches + ", staticActionRetryRestores=" + this.__qin_field_staticActionRetryRestores + ", staticBranchSelectorDispatches=" + this.__qin_field_staticBranchSelectorDispatches + ", staticBranchSelectorNoMatches=" + this.__qin_field_staticBranchSelectorNoMatches + ", lastStaticNoMatchDecisionId=" + this.__qin_field_lastStaticNoMatchDecisionId + ", lastStaticAmbiguousDecisionId=" + this.__qin_field_lastStaticAmbiguousDecisionId + ", lastStaticSharedPrefixSelection=" + this.__qin_field_lastStaticSharedPrefixSelection + ", lastStaticSharedPrefixNoMatch=" + this.__qin_field_lastStaticSharedPrefixNoMatch + ", staticBranchSelectorAmbiguities=" + this.__qin_field_staticBranchSelectorAmbiguities + ", staticDecisionCandidateExecutions=" + this.__qin_field_staticDecisionCandidateExecutions + ", staticDecisionSkippedBranches=" + this.__qin_field_staticDecisionSkippedBranches + ", staticDecisionGateSkips=" + this.__qin_field_staticDecisionGateSkips + ", staticDecisionStateSaves=" + this.__qin_field_staticDecisionStateSaves + ", staticDecisionStateSaveSkips=" + this.__qin_field_staticDecisionStateSaveSkips + ", staticDecisionDirectManyExecutions=" + this.__qin_field_staticDecisionDirectManyExecutions + ", staticCoreRuleEntries=" + this.__qin_field_staticCoreRuleEntries + ", staticRuleLoopRejects=" + this.__qin_field_staticRuleLoopRejects + ", staticNonNullableEmptyRejects=" + this.__qin_field_staticNonNullableEmptyRejects + ", lastStaticNonNullableEmptyRule=" + this.__qin_field_lastStaticNonNullableEmptyRule + ", staticExecutionModeBindings=" + this.__qin_field_staticExecutionModeBindings + ", staticExecutionMode=" + this.__qin_field_staticExecutionModeName + ", parserRuntimePlanBuilds=" + this.__qin_field_parserRuntimePlanBuilds + ", parserRuntimePlanCacheHits=" + this.__qin_field_parserRuntimePlanCacheHits + ", tokenCacheGets=" + this.__qin_field_tokenCacheGets + ", tokenCacheHits=" + this.__qin_field_tokenCacheHits + ", tokenCacheMisses=" + this.__qin_field_tokenCacheMisses + ", tokenCachePuts=" + this.__qin_field_tokenCachePuts + ", tokenStreamGets=" + this.__qin_field_tokenStreamGets + ", tokenStreamHits=" + this.__qin_field_tokenStreamHits + ", currentTokenEntryCacheHits=" + this.__qin_field_currentTokenEntryCacheHits + ", sourceLookaheadCacheHits=" + this.__qin_field_sourceLookaheadCacheHits + ", sourceLookaheadCacheFills=" + this.__qin_field_sourceLookaheadCacheFills + ", sourceLookaheadCacheResets=" + this.__qin_field_sourceLookaheadCacheResets + ", lexerTokenDefinitions=" + this.__qin_field_lexer.getTokenDefinitionCount() + ", lexerExactFixedTokenDefinitions=" + this.__qin_field_lexer.getExactFixedTokenDefinitionCount() + ", lexerCandidateTokenChecks=" + this.__qin_field_lexer.getCandidateTokenChecks() + ", lexerPatternMatchAttempts=" + this.__qin_field_lexer.getPatternMatchAttempts() + ", preTokenizedRegexpNegativeHits=" + this.__qin_field_preTokenizedRegexpNegativeHits + ", preTokenizedFallbackModes=" + this.formatTopRuleCounts(this.__qin_field_preTokenizedFallbackModeCounts, 8.0) + ", cstOutputEnabled=" + this.__qin_field_buildCst + ", tokenCursor=" + this.__qin_field_tokenCursor + ", parsedTokenListSize=" + __qin_collection_size__(this.__qin_field_parsedTokens) + ", tokenCstNodes=" + this.__qin_field_tokenCstNodes + ", ruleCstNodes=" + this.__qin_field_ruleCstNodes + ", cstParentRebuilds=" + this.__qin_field_cstParentRebuilds + ", cachedCstAttachCount=" + this.__qin_field_cachedCstAttachCount);
  }
  getCoreHotRuleStats(limit: number): string {
    if ((!this.__qin_field_coreRuleProfileEnabled)) {
      return "disabled";
    }
    return ("core=" + this.formatTopRuleCounts(this.__qin_field_ruleCoreExecutionCounts, limit) + "; success=" + this.formatTopRuleCounts(this.__qin_field_ruleCoreSuccessCounts, limit) + "; failure=" + this.formatTopRuleCounts(this.__qin_field_ruleCoreFailureCounts, limit) + "; wrappers=" + this.formatTopRuleCounts(this.__qin_field_ruleWrapperCallCounts, limit) + "; passThroughRules=" + this.formatTopRuleCounts(this.__qin_field_ruleWrapperPassThroughCounts, limit) + "; terminalLeafRules=" + this.formatTopRuleCounts(this.__qin_field_ruleWrapperTerminalLeafCounts, limit) + "; ruleChainPassThroughRules=" + this.formatTopRuleCounts(this.__qin_field_ruleWrapperRuleChainCounts, limit) + "; cacheHits=" + this.formatTopRuleCounts(this.__qin_field_ruleCacheHitCounts, limit) + "; cachePuts=" + this.formatTopRuleCounts(this.__qin_field_ruleCachePutCounts, limit) + "; cacheWork=" + this.formatTopCacheWork(limit) + "; cacheHitSpan=" + this.formatTopCacheHitSpans(limit) + "; failureRate=" + this.formatTopFailureRates(limit) + "; orStateSaves=" + this.formatTopRuleCounts(this.__qin_field_orPredictionStateSaveRuleCounts, limit) + "; orStateSaveSkips=" + this.formatTopRuleCounts(this.__qin_field_orPredictionStateSaveSkipRuleCounts, limit) + "; orCandidates=" + this.formatTopOrCandidateCounts(limit));
  }
  getStaticDecisionRetryProfile(limit: number): string {
    return (this.__qin_field_staticDecisionProfileEnabled ? this.formatTopRuleCounts(this.__qin_field_staticDecisionRetryCounts, limit) : "disabled");
  }
  recordStaticDecisionRetry(decision: com_subhuti_parser_SubhutiDecisionPlan): void {
    if ((!this.__qin_field_staticDecisionProfileEnabled || __qin_binary__("==", decision, null))) {
      return null;
    }
    let key: string = ("decision=" + decision.decisionId(this.staticGrammarPlan()) + " address=" + decision.ruleId() + ":" + decision.variantId() + ":" + decision.occurrenceId() + " kind=" + decision.kind());
    this.__qin_field_staticDecisionRetryCounts.merge(key, 1.0, ((...__qin_args) => __QinJavaLangLong.sum(...__qin_args.slice(0, 2))));
    return null;
  }
  incrementRuleProfile(counts: __QinJavaUtilHashMap<string, number | null>, ruleName: string): void {
    if ((!this.__qin_field_coreRuleProfileEnabled)) {
      return null;
    }
    counts.put(ruleName, __qin_binary__("+", counts.getOrDefault(ruleName, 0.0), 1.0));
    return null;
  }
  incrementCacheHitTokenSpan(ruleName: string, cached: any, startTokenIndex: number): void {
    if ((!this.__qin_field_coreRuleProfileEnabled || !(() => { const __qin_instanceof_value = cached; return __qin_instanceof__(__qin_instanceof_value, com_subhuti_cache_SubhutiPackratCacheResult); })())) {
      return null;
    }
    let cacheResult: com_subhuti_cache_SubhutiPackratCacheResult = (cached as com_subhuti_cache_SubhutiPackratCacheResult);
    let span: number = Math.max(0.0, __qin_binary__("-", cacheResult.__qin_field_endTokenIndex, startTokenIndex));
    this.__qin_field_ruleCacheHitTokenSpanTotals.put(ruleName, __qin_binary__("+", this.__qin_field_ruleCacheHitTokenSpanTotals.getOrDefault(ruleName, 0.0), span));
    return null;
  }
  incrementOrPredictionStateSaveProfile(saved: boolean): void {
    if ((!this.__qin_field_coreRuleProfileEnabled)) {
      return null;
    }
    let ruleName: string = this.activeStaticRuleScopeName();
    if (__qin_binary__("==", ruleName, null)) {
      ruleName = this.__qin_field_activeRuleProfileStack.peekLast();
    }
    if (__qin_binary__("==", ruleName, null)) {
      ruleName = "<top-level-or>";
    }
    {
      const __qin_typed_receiver_184: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_184.incrementRuleProfile((saved ? this.__qin_field_orPredictionStateSaveRuleCounts : this.__qin_field_orPredictionStateSaveSkipRuleCounts), ruleName);
    }
    return null;
  }
  formatTopRuleCounts(counts: __QinJavaUtilHashMap<string, number | null>, limit: number): string {
    if (counts.isEmpty()) {
      return "[]";
    }
    let safeLimit: number = Math.max(1.0, limit);
    return __QinJavaUtilArrays.stream(counts.entrySet()).sorted(__qin_java_functional((left, right) => {
      let countCompare: number = __QinJavaLangLong.compare(right.getValue(), left.getValue());
      if (__qin_binary__("!=", countCompare, 0.0)) {
        return countCompare;
      }
      return left.getKey().compareTo(right.getKey());
    })).limit(safeLimit).map(__qin_java_functional((entry) => {
      return (entry.getKey() + "=" + entry.getValue());
    })).collect(__QinJavaUtilStreamCollectors.joining(", ", "[", "]"));
  }
  recordPreTokenizedFallbackMode(mode: com_subhuti_struct_LexerMode): void {
    if ((!this.__qin_field_coreRuleProfileEnabled || !this.__qin_field_preTokenizedDefaultModeInput)) {
      return null;
    }
    let normalizedMode: com_subhuti_struct_LexerMode = (__qin_binary__("==", mode, null) ? com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE : mode);
    let modeName: string = (normalizedMode.isDefault() ? "DEFAULT_MODE" : normalizedMode.toString());
    this.__qin_field_preTokenizedFallbackModeCounts.merge(modeName, 1.0, ((...__qin_args) => __QinJavaLangLong.sum(...__qin_args.slice(0, 2))));
    return null;
  }
  preTokenizedRegexpNegativeEntry(index: number, mode: com_subhuti_struct_LexerMode): com_subhuti_lexer_TokenCacheEntry {
    if ((!this.__qin_field_preTokenizedDefaultModeInput || !com_subhuti_struct_LexerMode.__qin_field_REGEXP.equals(mode))) {
      return null;
    }
    let defaultEntry: com_subhuti_lexer_TokenCacheEntry = this.preTokenizedEntryAt(index, com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE);
    let token: com_subhuti_struct_SubhutiMatchToken = (__qin_binary__("!=", defaultEntry, null) ? defaultEntry.getToken() : null);
    let tokenStart: number | null = (__qin_binary__("!=", token, null) ? token.index() : null);
    if ((__qin_binary__("==", tokenStart, null) || __qin_binary__("<", tokenStart, 0.0) || __qin_binary__(">=", tokenStart, __QinJavaLangString.length(this.__qin_field_sourceCode)))) {
      return null;
    }
    return (__qin_binary__("==", __QinJavaLangString.charAt(this.__qin_field_sourceCode, tokenStart), "/") ? null : defaultEntry);
  }
  formatTopCacheWork(limit: number): string {
    if ((this.__qin_field_ruleCachePutCounts.isEmpty() && this.__qin_field_ruleCacheHitCounts.isEmpty())) {
      return "[]";
    }
    let safeLimit: number = Math.max(1.0, limit);
    let ruleNames: __QinJavaUtilSet<string> = new __QinJavaUtilHashSet();
    ruleNames.addAll(this.__qin_field_ruleCachePutCounts.keySet());
    ruleNames.addAll(this.__qin_field_ruleCacheHitCounts.keySet());
    return __QinJavaUtilArrays.stream(ruleNames).map(__qin_java_functional((ruleName) => {
      return new com_subhuti_parser_SubhutiParserCore$CacheWork(ruleName, this.__qin_field_ruleCacheHitCounts.getOrDefault(ruleName, 0.0), this.__qin_field_ruleCachePutCounts.getOrDefault(ruleName, 0.0));
    })).sorted(__qin_java_functional((left, right) => {
      let totalCompare: number = __QinJavaLangLong.compare(right.total(), left.total());
      if (__qin_binary__("!=", totalCompare, 0.0)) {
        return totalCompare;
      }
      return left.ruleName().compareTo(right.ruleName());
    })).limit(safeLimit).map(__qin_java_functional((work) => {
      let hitRate: number = (__qin_binary__("==", work.total(), 0.0) ? 0.0 : __qin_binary__("/", Number(work.hits()), work.total()));
      return (work.ruleName() + "=" + work.total() + "(h" + work.hits() + "/p" + work.puts() + "@" + __QinJavaLangString.format(Locale.__qin_field_ROOT, "%.1f", __qin_binary__("*", hitRate, 100.0)) + "%)");
    })).collect(__QinJavaUtilStreamCollectors.joining(", ", "[", "]"));
  }
  formatTopCacheHitSpans(limit: number): string {
    if (this.__qin_field_ruleCacheHitTokenSpanTotals.isEmpty()) {
      return "[]";
    }
    let safeLimit: number = Math.max(1.0, limit);
    return __QinJavaUtilArrays.stream(this.__qin_field_ruleCacheHitTokenSpanTotals.entrySet()).sorted(__qin_java_functional((left, right) => {
      let totalCompare: number = __QinJavaLangLong.compare(right.getValue(), left.getValue());
      if (__qin_binary__("!=", totalCompare, 0.0)) {
        return totalCompare;
      }
      return left.getKey().compareTo(right.getKey());
    })).limit(safeLimit).map(__qin_java_functional((entry) => {
      let hits: number = this.__qin_field_ruleCacheHitCounts.getOrDefault(entry.getKey(), 0.0);
      let average: number = (__qin_binary__("==", hits, 0.0) ? 0.0 : __qin_binary__("/", Number(entry.getValue()), hits));
      return (entry.getKey() + "=" + entry.getValue() + "/" + hits + "@" + __QinJavaLangString.format(Locale.__qin_field_ROOT, "%.2f", average));
    })).collect(__QinJavaUtilStreamCollectors.joining(", ", "[", "]"));
  }
  formatTopFailureRates(limit: number): string {
    if (this.__qin_field_ruleCoreFailureCounts.isEmpty()) {
      return "[]";
    }
    let safeLimit: number = Math.max(1.0, limit);
    return __QinJavaUtilArrays.stream(this.__qin_field_ruleCoreFailureCounts.keySet()).map(__qin_java_functional((ruleName) => {
      return new com_subhuti_parser_SubhutiParserCore$FailureWork(ruleName, this.__qin_field_ruleCoreSuccessCounts.getOrDefault(ruleName, 0.0), this.__qin_field_ruleCoreFailureCounts.getOrDefault(ruleName, 0.0));
    })).filter(__qin_java_functional((work) => {
      return __qin_binary__(">", work.total(), 0.0);
    })).sorted(__qin_java_functional((left, right) => {
      let failureCompare: number = __QinJavaLangLong.compare(right.failures(), left.failures());
      if (__qin_binary__("!=", failureCompare, 0.0)) {
        return failureCompare;
      }
      return left.ruleName().compareTo(right.ruleName());
    })).limit(safeLimit).map(__qin_java_functional((work) => {
      let failureRate: number = __qin_binary__("/", Number(work.failures()), work.total());
      return (work.ruleName() + "=" + work.failures() + "/" + work.total() + "@" + __QinJavaLangString.format(Locale.__qin_field_ROOT, "%.1f", __qin_binary__("*", failureRate, 100.0)) + "%");
    })).collect(__QinJavaUtilStreamCollectors.joining(", ", "[", "]"));
  }
  formatTopOrCandidateCounts(limit: number): string {
    if (this.__qin_field_orPredictionCandidateRuleCounts.isEmpty()) {
      return "[]";
    }
    let safeLimit: number = Math.max(1.0, limit);
    return __QinJavaUtilArrays.stream(this.__qin_field_orPredictionCandidateRuleTotals.entrySet()).sorted(__qin_java_functional((left, right) => {
      let totalCompare: number = __QinJavaLangLong.compare(right.getValue(), left.getValue());
      if (__qin_binary__("!=", totalCompare, 0.0)) {
        return totalCompare;
      }
      return left.getKey().compareTo(right.getKey());
    })).limit(safeLimit).map(__qin_java_functional((entry) => {
      let calls: number = this.__qin_field_orPredictionCandidateRuleCounts.getOrDefault(entry.getKey(), 0.0);
      let average: number = (__qin_binary__("==", calls, 0.0) ? 0.0 : __qin_binary__("/", Number(entry.getValue()), calls));
      return (entry.getKey() + "=" + entry.getValue() + "/" + calls + "@" + __QinJavaLangString.format(Locale.__qin_field_ROOT, "%.2f", average));
    })).collect(__QinJavaUtilStreamCollectors.joining(", ", "[", "]"));
  }
  tryAndRestore(fn: QinJavaRunnable): boolean {
    const __qin_functional_fn_0 = __qin_java_functional(fn);
    if (this.isParserFailOrIsEof()) {
      return false;
    }
    let savedState: com_subhuti_parser_SubhutiBackData = this.saveState();
    let startIndex: number = this.__qin_field_currentIndex;
    __qin_functional_fn_0.run();
    if ((!this.__qin_field_parseSuccess)) {
      {
        const __qin_typed_receiver_185: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_185.recordPartialMatchAndRestore(savedState, startIndex);
      }
      {
        const __qin_typed_receiver_186: com_subhuti_parser_SubhutiParserCore = this;
        __qin_typed_receiver_186.setParseSuccess();
      }
      return false;
    }
    return __qin_binary__("!=", this.__qin_field_currentIndex, startIndex);
  }
  recordPartialMatchAndRestore(savedState: com_subhuti_parser_SubhutiBackData, startCodeIndex: number): void {
    {
      const __qin_typed_receiver_187: com_subhuti_parser_SubhutiParserCore = this;
      __qin_typed_receiver_187.restoreState(savedState);
    }
    return null;
  }
  isParserFailOrIsEof(): boolean {
    return (!this.__qin_field_parseSuccess || this.isEof());
  }
  recoverFromParseRecord(root: com_subhuti_parser_ParseRecordNode, maxIndex: number): com_subhuti_struct_SubhutiCst {
    if ((__qin_binary__("==", root, null) || __qin_collection_is_empty__(root.getChildren()))) {
      return null;
    }
    let cst: com_subhuti_struct_SubhutiCst = com_subhuti_struct_SubhutiCst.builder().name(root.getName()).children(this.parseRecordChildrenToCST(root.getChildren(), maxIndex)).build();
    if ((__qin_binary__("==", cst.getChildren(), null) || __qin_collection_is_empty__(cst.getChildren()))) {
      return null;
    }
    return cst;
  }
  parseRecordChildrenToCST(nodes: __QinJavaUtilList<com_subhuti_parser_ParseRecordNode>, maxIndex: number): __QinJavaUtilList<com_subhuti_struct_SubhutiCst> {
    let groups: __QinJavaUtilHashMap<number | null, __QinJavaUtilList<com_subhuti_parser_ParseRecordNode>> = new __QinJavaUtilHashMap();
    for (const node of nodes) {
      if (__qin_binary__(">", node.getEndTokenIndex(), maxIndex)) {
        continue;
      }
      let key: number = node.getStartTokenIndex();
      groups.computeIfAbsent(key, __qin_java_functional((k) => {
      return new __QinJavaUtilArrayList();
    })).add(node);
    }
    let selectedNodes: __QinJavaUtilList<com_subhuti_parser_ParseRecordNode> = new __QinJavaUtilArrayList();
    for (const group of groups.values()) {
      let best: com_subhuti_parser_ParseRecordNode = null;
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
    let result: __QinJavaUtilList<com_subhuti_struct_SubhutiCst> = new __QinJavaUtilArrayList();
    for (const node of selectedNodes) {
      result.add(this.parseRecordNodeToCST(node, maxIndex));
    }
    return result;
  }
  parseRecordNodeToCST(node: com_subhuti_parser_ParseRecordNode, maxIndex: number): com_subhuti_struct_SubhutiCst {
    let cstBuilder: com_subhuti_struct_SubhutiCst$Builder = com_subhuti_struct_SubhutiCst.builder().name(node.getName());
    if (__qin_binary__("!=", node.getToken(), null)) {
      let token: com_subhuti_struct_SubhutiMatchToken = node.getToken();
      {
        const __qin_typed_receiver_188: com_subhuti_struct_SubhutiCst$Builder = cstBuilder.value(node.getValue());
        __qin_typed_receiver_188.location(com_subhuti_struct_SubhutiSourceLocation.of(token.startPosition(), token.endPosition()));
      }
    }
    if ((!__qin_collection_is_empty__(node.getChildren()))) {
      let children: __QinJavaUtilList<com_subhuti_struct_SubhutiCst> = this.parseRecordChildrenToCST(node.getChildren(), maxIndex);
      if (__qin_collection_is_empty__(children)) {
        {
          const __qin_typed_receiver_189: com_subhuti_struct_SubhutiCst$Builder = cstBuilder;
          __qin_typed_receiver_189.children(null);
        }
      } else {
        {
          const __qin_typed_receiver_190: com_subhuti_struct_SubhutiCst$Builder = cstBuilder;
          __qin_typed_receiver_190.children(children);
        }
      }
    }
    return cstBuilder.build();
  }
  getParseRecordMaxEndIndex(root: com_subhuti_parser_ParseRecordNode, maxIndex: number): number {
    let maxEnd: number = (__qin_binary__("<=", root.getEndTokenIndex(), maxIndex) ? root.getEndTokenIndex() : 0.0);
    for (const child of root.getChildren()) {
      let childMax: number = this.getParseRecordMaxEndIndex(child, maxIndex);
      if (__qin_binary__(">", childMax, maxEnd)) {
        maxEnd = childMax;
      }
    }
    return maxEnd;
  }
  createErrorNode(startIndex: number, endIndex: number): com_subhuti_struct_SubhutiCst {
    let errorChildren: __QinJavaUtilList<com_subhuti_struct_SubhutiCst> = new __QinJavaUtilArrayList();
    for (const token of this.__qin_field_parsedTokens) {
      let tokenStart: number = (__qin_binary__("!=", token.index(), null) ? token.index() : 0.0);
      if ((__qin_binary__(">=", tokenStart, startIndex) && __qin_binary__("<", tokenStart, endIndex))) {
        let tokenNode: com_subhuti_struct_SubhutiCst = com_subhuti_struct_SubhutiCst.builder().name(token.tokenName()).value(token.value()).location(com_subhuti_struct_SubhutiSourceLocation.of(token.startPosition(), token.endPosition())).build();
        errorChildren.add(tokenNode);
      }
    }
    let errorNodeBuilder: com_subhuti_struct_SubhutiCst$Builder = com_subhuti_struct_SubhutiCst.builder().name("ErrorNode").children(errorChildren);
    if ((!__qin_collection_is_empty__(errorChildren))) {
      let first: com_subhuti_struct_SubhutiCst = __qin_collection_get__(errorChildren, 0.0);
      let last: com_subhuti_struct_SubhutiCst = __qin_collection_get__(errorChildren, __qin_binary__("-", __qin_collection_size__(errorChildren), 1.0));
      if ((__qin_binary__("!=", first.getLocation(), null) && __qin_binary__("!=", last.getLocation(), null))) {
        {
          const __qin_typed_receiver_191: com_subhuti_struct_SubhutiCst$Builder = errorNodeBuilder;
          __qin_typed_receiver_191.location(com_subhuti_struct_SubhutiSourceLocation.of(first.getLocation().start(), last.getLocation().end()));
        }
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
    if (__qin_args.length === 1 && true) {
      const values: any = __qin_args[0];
      this.__qin_constructor_com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments_1_0(values);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParserCore$StaticInvocationArguments/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments_1_0(values: any[]): void {
    this.__qin_field_values = null;
    values = (__qin_binary__("==", values, null) ? Array.from({ length: 0.0 }, () => null) : values.clone());
    this.__qin_field_values = values;
  }
  values(): any[] {
    return this.__qin_field_values.clone();
  }
  get(index: number): any {
    if ((__qin_binary__("<", index, 0.0) || __qin_binary__(">=", index, this.__qin_field_values.length))) {
      throw new __QinJavaLangIllegalArgumentException(("static invocation parameter index is out of bounds: " + index));
    }
    return this.__qin_field_values[index];
  }
  equals(other: any): boolean {
    if (this === other) return true;
    if (!__qin_instanceof__(other, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments)) return false;
    const __qin_record_other: com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments = other;
    return __qin_java_values_equal__(this.__qin_field_values, __qin_record_other.__qin_field_values);
  }
  hashCode(): number {
    let result = 1;
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_values);
    return result;
  }
  toString(): string {
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
  __qin_field_owner: com_subhuti_parser_SubhutiParserCore | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_subhuti_parser_SubhutiParserCore))) {
      const owner: any = __qin_args[0];
      this.__qin_constructor_com_subhuti_parser_SubhutiParserCore$StaticDebugHooks_1_0(owner);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParserCore$StaticDebugHooks/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParserCore$StaticDebugHooks_1_0(owner: com_subhuti_parser_SubhutiParserCore): void {
    this.__qin_field_owner = null;
    this.__qin_field_owner = owner;
  }
  onRuleEnter(ruleName: string, tokenIndex: number): number {
    return (__qin_binary__("==", (__qin_binary__("==", this.__qin_field_owner, null) ? null : this.__qin_field_owner.getDebugger()), null) ? 0.0 : (__qin_binary__("==", this.__qin_field_owner, null) ? null : this.__qin_field_owner.getDebugger()).onRuleEnter(ruleName, tokenIndex));
  }
  onRuleExit(ruleName: string, startTime: number): void {
    let __qin_debugger: com_subhuti_debug_SubhutiTraceDebugger = (__qin_binary__("==", this.__qin_field_owner, null) ? null : this.__qin_field_owner.getDebugger());
    if (__qin_binary__("!=", __qin_debugger, null)) {
      {
        const __qin_typed_receiver_192: com_subhuti_debug_SubhutiTraceDebugger = __qin_debugger;
        __qin_typed_receiver_192.onRuleExit(ruleName, false, startTime);
      }
    }
    return null;
  }
  onRootComplete(cst: com_subhuti_struct_SubhutiCst): void {
    let __qin_debugger: com_subhuti_debug_SubhutiTraceDebugger = (__qin_binary__("==", this.__qin_field_owner, null) ? null : this.__qin_field_owner.getDebugger());
    if ((__qin_binary__("!=", __qin_debugger, null) && __qin_binary__("!=", cst, null))) {
      {
        const __qin_typed_receiver_193: com_subhuti_debug_SubhutiTraceDebugger = __qin_debugger;
        __qin_typed_receiver_193.setCst(cst);
      }
      {
        const __qin_typed_receiver_194: com_subhuti_debug_SubhutiTraceDebugger = __qin_debugger;
        __qin_typed_receiver_194.autoOutput();
      }
    }
    return null;
  }
}
const SubhutiParserCore$StaticDebugHooks = com_subhuti_parser_SubhutiParserCore$StaticDebugHooks;
class com_subhuti_parser_SubhutiParserCore$CacheWork {
  __qin_field_ruleName: string | null = null as any;
  __qin_field_hits: number | null = 0 as any;
  __qin_field_puts: number | null = 0 as any;
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
    this.__qin_field_hits = 0;
    this.__qin_field_puts = 0;
    (() => {
      this.__qin_field_ruleName = ruleName;
      this.__qin_field_hits = hits;
      this.__qin_field_puts = puts;
      return null;
    })();
  }
  total(): number {
    return __qin_binary__("+", this.__qin_field_hits, this.__qin_field_puts);
  }
  ruleName(): string {
    return this.__qin_field_ruleName;
  }
  hits(): number {
    return this.__qin_field_hits;
  }
  puts(): number {
    return this.__qin_field_puts;
  }
  equals(other: any): boolean {
    if (this === other) return true;
    if (!__qin_instanceof__(other, com_subhuti_parser_SubhutiParserCore$CacheWork)) return false;
    const __qin_record_other: com_subhuti_parser_SubhutiParserCore$CacheWork = other;
    return __qin_java_values_equal__(this.__qin_field_ruleName, __qin_record_other.__qin_field_ruleName)
      && __qin_java_values_equal__(this.__qin_field_hits, __qin_record_other.__qin_field_hits)
      && __qin_java_values_equal__(this.__qin_field_puts, __qin_record_other.__qin_field_puts);
  }
  hashCode(): number {
    let result = 1;
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_ruleName);
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_hits);
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_puts);
    return result;
  }
  toString(): string {
    return ["SubhutiParserCore$CacheWork[", "ruleName=", this.__qin_field_ruleName, ", ", "hits=", this.__qin_field_hits, ", ", "puts=", this.__qin_field_puts, "]"].join("");
  }
}
const SubhutiParserCore$CacheWork = com_subhuti_parser_SubhutiParserCore$CacheWork;
class com_subhuti_parser_SubhutiParserCore$FailureWork {
  __qin_field_ruleName: string | null = null as any;
  __qin_field_successes: number | null = 0 as any;
  __qin_field_failures: number | null = 0 as any;
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
    this.__qin_field_successes = 0;
    this.__qin_field_failures = 0;
    (() => {
      this.__qin_field_ruleName = ruleName;
      this.__qin_field_successes = successes;
      this.__qin_field_failures = failures;
      return null;
    })();
  }
  total(): number {
    return __qin_binary__("+", this.__qin_field_successes, this.__qin_field_failures);
  }
  ruleName(): string {
    return this.__qin_field_ruleName;
  }
  successes(): number {
    return this.__qin_field_successes;
  }
  failures(): number {
    return this.__qin_field_failures;
  }
  equals(other: any): boolean {
    if (this === other) return true;
    if (!__qin_instanceof__(other, com_subhuti_parser_SubhutiParserCore$FailureWork)) return false;
    const __qin_record_other: com_subhuti_parser_SubhutiParserCore$FailureWork = other;
    return __qin_java_values_equal__(this.__qin_field_ruleName, __qin_record_other.__qin_field_ruleName)
      && __qin_java_values_equal__(this.__qin_field_successes, __qin_record_other.__qin_field_successes)
      && __qin_java_values_equal__(this.__qin_field_failures, __qin_record_other.__qin_field_failures);
  }
  hashCode(): number {
    let result = 1;
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_ruleName);
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_successes);
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_failures);
    return result;
  }
  toString(): string {
    return ["SubhutiParserCore$FailureWork[", "ruleName=", this.__qin_field_ruleName, ", ", "successes=", this.__qin_field_successes, ", ", "failures=", this.__qin_field_failures, "]"].join("");
  }
}
const SubhutiParserCore$FailureWork = com_subhuti_parser_SubhutiParserCore$FailureWork;
com_subhuti_parser_SubhutiParserCore.__qin_field_ADAPTIVE_LOW_YIELD_MEMO_MIN_PUTS = 64.0;
com_subhuti_parser_SubhutiParserCore$StaticDebugHooks.__qin_field_NO_OP = new com_subhuti_parser_SubhutiParserCore$StaticDebugHooks(null);
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
