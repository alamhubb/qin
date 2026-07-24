import { com_subhuti_cache_SubhutiPackratCache, com_subhuti_cache_SubhutiPackratCache as SubhutiPackratCache } from "../cache/SubhutiPackratCache.ts";
import { com_subhuti_debug_SubhutiTraceDebugger, com_subhuti_debug_SubhutiTraceDebugger as SubhutiTraceDebugger } from "../debug/SubhutiTraceDebugger.ts";
import { com_subhuti_error_SubhutiErrorHandler, com_subhuti_error_SubhutiErrorHandler as SubhutiErrorHandler, com_subhuti_error_SubhutiErrorHandler$ParseError, com_subhuti_error_SubhutiErrorHandler$ParseError as ParseError } from "../error/SubhutiErrorHandler.ts";
import { com_subhuti_lexer_SubhutiLexer, com_subhuti_lexer_SubhutiLexer as SubhutiLexer, com_subhuti_lexer_SubhutiLexer$MatchedTokenInfo, com_subhuti_lexer_SubhutiLexer$MatchedTokenInfo as MatchedTokenInfo, com_subhuti_lexer_SubhutiLexer$LexerException, com_subhuti_lexer_SubhutiLexer$LexerException as LexerException } from "../lexer/SubhutiLexer.ts";
import { com_subhuti_lookahead_SubhutiTokenConsumer, com_subhuti_lookahead_SubhutiTokenConsumer as SubhutiTokenConsumer } from "../lookahead/SubhutiTokenConsumer.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../lookahead/SubhutiTokenLookahead.ts";
import { com_subhuti_struct_SubhutiPosition, com_subhuti_struct_SubhutiPosition as SubhutiPosition } from "../struct/SubhutiPosition.ts";
import { com_subhuti_struct_LexerMode, com_subhuti_struct_LexerMode as LexerMode } from "../struct/LexerMode.ts";
import { com_subhuti_parser_SubhutiTokenCache, com_subhuti_parser_SubhutiTokenCache as SubhutiTokenCache } from "./SubhutiTokenCache.ts";
import { com_subhuti_lexer_TokenCacheEntry, com_subhuti_lexer_TokenCacheEntry as TokenCacheEntry } from "../lexer/TokenCacheEntry.ts";
import { com_subhuti_struct_SubhutiCst, com_subhuti_struct_SubhutiCst as SubhutiCst, com_subhuti_struct_SubhutiCst$Builder } from "../struct/SubhutiCst.ts";
import { com_subhuti_struct_SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken as SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken$Builder } from "../struct/SubhutiMatchToken.ts";
import { com_subhuti_parser_SubhutiParserRuntimePlan, com_subhuti_parser_SubhutiParserRuntimePlan as SubhutiParserRuntimePlan, com_subhuti_parser_SubhutiParserRuntimePlan$GastRecognizerAnalysis, com_subhuti_parser_SubhutiParserRuntimePlan$GastRecognizerAnalysis as GastRecognizerAnalysis, com_subhuti_parser_SubhutiParserRuntimePlan$TarjanRuleCycles, com_subhuti_parser_SubhutiParserRuntimePlan$TarjanRuleCycles as TarjanRuleCycles, com_subhuti_parser_SubhutiParserRuntimePlan$Access, com_subhuti_parser_SubhutiParserRuntimePlan$Access as Access, com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal, com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminal as DirectTerminal, com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminalSequence, com_subhuti_parser_SubhutiParserRuntimePlan$DirectTerminalSequence as DirectTerminalSequence, com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerElement, com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerElement as DirectRecognizerElement, com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerElement$Kind, com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerPlan, com_subhuti_parser_SubhutiParserRuntimePlan$DirectRecognizerPlan as DirectRecognizerPlan, com_subhuti_parser_SubhutiParserRuntimePlan$CoverageReport, com_subhuti_parser_SubhutiParserRuntimePlan$CoverageReport as CoverageReport } from "./SubhutiParserRuntimePlan.ts";
import { com_subhuti_parser_SubhutiStaticExecutionPlan, com_subhuti_parser_SubhutiStaticExecutionPlan as SubhutiStaticExecutionPlan } from "./SubhutiStaticExecutionPlan.ts";
import { com_subhuti_parser_ParseRecordNode, com_subhuti_parser_ParseRecordNode as ParseRecordNode } from "./ParseRecordNode.ts";
import { com_subhuti_parser_SubhutiStaticGrammarPlan, com_subhuti_parser_SubhutiStaticGrammarPlan as SubhutiStaticGrammarPlan, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionAvailability, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionAvailability as DecisionAvailability, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDispatchBlocker, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDispatchBlocker as StaticDispatchBlocker, com_subhuti_parser_SubhutiStaticGrammarPlan$DynamicCandidateReason, com_subhuti_parser_SubhutiStaticGrammarPlan$DynamicCandidateReason as DynamicCandidateReason, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadKind, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadKind as CompiledLookaheadKind, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkNodeDefinition as CompiledLlkNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkEdgeDefinition as CompiledLlkEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadDefinition as CompiledLookaheadDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$BranchDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$BranchDefinition as BranchDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixDefinition as SharedPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupDefinition as CandidateGroupDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleTokenDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleTokenDefinition as CrossRuleTokenDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleFrontierDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleFrontierDefinition as CrossRuleFrontierDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphNodeDefinition as AdaptiveGraphNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphDefinition as AdaptiveGraphDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionKind, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionKind as FiniteInstructionKind, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteTokenClassDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteTokenClassDefinition as FiniteTokenClassDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteMatchEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteMatchEdgeDefinition as FiniteMatchEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteRuleReferenceDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteRuleReferenceDefinition as FiniteRuleReferenceDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteFrameDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteFrameDefinition as FiniteFrameDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteGateDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteGateDefinition as FiniteGateDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteCallPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteCallPrefixDefinition as FiniteCallPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionDefinition as FiniteInstructionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteDecisionProgramDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteDecisionProgramDefinition as FiniteDecisionProgramDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteProgramAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteProgramAddress as FiniteProgramAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixAddress as SharedPrefixAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupAddress as CandidateGroupAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleAddress as CrossRuleAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionDefinition as DecisionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AnalysisDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AnalysisDefinition as AnalysisDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$InvocationDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$InvocationDefinition as InvocationDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CoverageDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CoverageDefinition as CoverageDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticMetadata, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticMetadata as StaticMetadata, com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence, com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence as Occurrence, com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant, com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant as RuleVariant, com_subhuti_parser_SubhutiStaticGrammarPlan$VariantRecursionAnalysis, com_subhuti_parser_SubhutiStaticGrammarPlan$VariantRecursionAnalysis as VariantRecursionAnalysis, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan as StaticRuleInvocationPlan, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlans, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlans as StaticRuleInvocationPlans, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDecisionPlans, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDecisionPlans as StaticDecisionPlans } from "./SubhutiStaticGrammarPlan.ts";
import { com_subhuti_lexer_SubhutiLexerVocabulary, com_subhuti_lexer_SubhutiLexerVocabulary as SubhutiLexerVocabulary, com_subhuti_lexer_SubhutiLexerVocabulary$VocabularyKey, com_subhuti_lexer_SubhutiLexerVocabulary$VocabularyKey as VocabularyKey } from "../lexer/SubhutiLexerVocabulary.ts";
import { com_subhuti_parser_SubhutiBackData, com_subhuti_parser_SubhutiBackData as SubhutiBackData } from "./SubhutiBackData.ts";
import { com_subhuti_struct_SubhutiSourceLocation, com_subhuti_struct_SubhutiSourceLocation as SubhutiSourceLocation, com_subhuti_struct_SubhutiSourceLocation$Builder } from "../struct/SubhutiSourceLocation.ts";
import { com_subhuti_parser_SubhutiRecoveryDiagnostic, com_subhuti_parser_SubhutiRecoveryDiagnostic as SubhutiRecoveryDiagnostic } from "./SubhutiRecoveryDiagnostic.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangBoolean, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __QinJavaUtilArrayList, __QinJavaUtilUnmodifiableList, __QinJavaUtilHashMap, __QinJavaUtilUnmodifiableMap, __QinJavaUtilList, __QinJavaUtilHashSet, __QinJavaUtilUnmodifiableSet, __QinJavaUtilArrayDeque, __QinJavaUtilStream, __qin_java_hash_key_equals__, __QinJavaUtilArrays, __QinJavaUtilObjects } from "@qin/java-sdk-js";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const ArrayList = __QinJavaUtilArrayList;
const HashMap = __QinJavaUtilHashMap;
const Boolean = __QinJavaLangBoolean;
const LinkedHashMap = __QinJavaUtilHashMap;
const HashSet = __QinJavaUtilHashSet;
const ArrayDeque = __QinJavaUtilArrayDeque;
const IllegalArgumentException = __QinJavaLangIllegalArgumentException;
const IllegalStateException = __QinJavaLangIllegalStateException;
const Exception = __QinJavaLangException;
const RuntimeException = __QinJavaLangRuntimeException;
const Objects = __QinJavaUtilObjects;
const Arrays = __QinJavaUtilArrays;
const java_lang_Error = __QinJavaLangError;
class com_subhuti_parser_SubhutiParserState extends com_subhuti_lookahead_SubhutiTokenLookahead {
  __qin_field_sourceCode: string | null = null as any;
  __qin_field_lexer: com_subhuti_lexer_SubhutiLexer | null = null as any;
  __qin_field_tokenConsumer: com_subhuti_lookahead_SubhutiTokenConsumer | null = null as any;
  __qin_field_tokens: any = null as any;
  __qin_field_tokenCursor: number | null = null as any;
  __qin_field_currentIndex: number | null = null as any;
  __qin_field_currentPosition: com_subhuti_struct_SubhutiPosition | null = null as any;
  __qin_field_currentMode: com_subhuti_struct_LexerMode | null = null as any;
  __qin_field_lastTokenName: string | null = null as any;
  __qin_field_tokenCache: com_subhuti_parser_SubhutiTokenCache | null = null as any;
  __qin_field_parsedTokens: any = null as any;
  __qin_field_preTokenizedDefaultModeInput: boolean | null = null as any;
  __qin_field_preTokenizedEntriesByOrdinal: com_subhuti_lexer_TokenCacheEntry[] | null = null as any;
  __qin_field_preTokenizedOrdinalByCodeIndex: number[] | null = null as any;
  __qin_field_currentTokenEntryCacheSet: boolean | null = null as any;
  __qin_field_currentTokenEntryCacheCodeIndex: number | null = null as any;
  __qin_field_currentTokenEntryCacheLine: number | null = null as any;
  __qin_field_currentTokenEntryCacheColumn: number | null = null as any;
  __qin_field_currentTokenEntryCacheTokenCursor: number | null = null as any;
  __qin_field_currentTokenEntryCacheMode: com_subhuti_struct_LexerMode | null = null as any;
  __qin_field_currentTokenEntryCacheLastTokenName: string | null = null as any;
  __qin_field_currentTokenEntryCacheEntry: com_subhuti_lexer_TokenCacheEntry | null = null as any;
  __qin_field_cstStack: any = null as any;
  __qin_field_rootCst: com_subhuti_struct_SubhutiCst | null = null as any;
  __qin_field_buildCst: boolean | null = null as any;
  __qin_field_ruleExecutionDepth: number | null = null as any;
  __qin_field_parseSuccess: boolean | null = null as any;
  __qin_field_activeRuleInvocationsByName: any = null as any;
  __qin_field_indexedActiveRuleInvocations: com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations[] | null = null as any;
  __qin_field_indexedRuleInvocationsEnabled: boolean | null = null as any;
  __qin_field_indexedRuleInvocationLookups: number | null = null as any;
  __qin_field_staticPrimitiveInvocationEntries: number | null = null as any;
  __qin_field_staticRootRuleEntries: number | null = null as any;
  __qin_field_staticGateDispatches: number | null = null as any;
  __qin_field_staticActionDispatches: number | null = null as any;
  __qin_field_staticActionRetryRestores: number | null = null as any;
  __qin_field_staticBranchSelectorDispatches: number | null = null as any;
  __qin_field_staticBranchSelectorNoMatches: number | null = null as any;
  __qin_field_lastStaticNoMatchDecisionId: number | null = null as any;
  __qin_field_lastStaticAmbiguousDecisionId: number | null = null as any;
  __qin_field_lastStaticSharedPrefixSelection: string | null = null as any;
  __qin_field_lastStaticSharedPrefixNoMatch: string | null = null as any;
  __qin_field_staticBranchSelectorAmbiguities: number | null = null as any;
  __qin_field_staticDecisionCandidateExecutions: number | null = null as any;
  __qin_field_staticDecisionSkippedBranches: number | null = null as any;
  __qin_field_staticDecisionGateSkips: number | null = null as any;
  __qin_field_staticDecisionStateSaves: number | null = null as any;
  __qin_field_staticDecisionStateSaveSkips: number | null = null as any;
  __qin_field_staticDecisionDirectManyExecutions: number | null = null as any;
  __qin_field_staticCoreRuleEntries: number | null = null as any;
  __qin_field_staticRuleLoopRejects: number | null = null as any;
  __qin_field_staticNonNullableEmptyRejects: number | null = null as any;
  __qin_field_lastStaticNonNullableEmptyRule: string | null = null as any;
  __qin_field_staticExecutionModeBindings: number | null = null as any;
  __qin_field_staticExecutionModeName: string | null = null as any;
  __qin_field_ruleCache: com_subhuti_cache_SubhutiPackratCache | null = null as any;
  __qin_field_enableMemoization: boolean | null = null as any;
  __qin_field_enableOrPrediction: boolean | null = null as any;
  __qin_field_debugMode: boolean | null = null as any;
  __qin_field_firstTokenRecording: boolean | null = null as any;
  __qin_field_firstTokenRecordingTokenCount: number | null = null as any;
  __qin_field_firstTokenRecordingMaxTokens: number | null = null as any;
  __qin_field_orPredictionSkippedAlternatives: number | null = null as any;
  __qin_field_orPredictionGateSkippedAlternatives: number | null = null as any;
  __qin_field_orPredictionUnknownAlternatives: number | null = null as any;
  __qin_field_orPredictionCandidateAlternatives: number | null = null as any;
  __qin_field_orPredictionStateSaves: number | null = null as any;
  __qin_field_orPredictionStateSaveSkips: number | null = null as any;
  __qin_field_orPredictionDirectManyExecutions: number | null = null as any;
  __qin_field_staticLl1LookaheadHits: number | null = null as any;
  __qin_field_staticValueAwareLl1LookaheadHits: number | null = null as any;
  __qin_field_staticLlkLookaheadHits: number | null = null as any;
  __qin_field_alternativeStartPredictionDirectOptionExecutions: number | null = null as any;
  __qin_field_predictionCurrentTokenCacheHits: number | null = null as any;
  __qin_field_predictionCurrentTokenCacheMisses: number | null = null as any;
  __qin_field_predictionCurrentTokenCacheSet: boolean | null = null as any;
  __qin_field_predictionCurrentTokenCacheIndex: number | null = null as any;
  __qin_field_predictionCurrentTokenCacheMode: com_subhuti_struct_LexerMode | null = null as any;
  __qin_field_predictionCurrentTokenCacheLastTokenName: string | null = null as any;
  __qin_field_predictionCurrentTokenCacheToken: com_subhuti_struct_SubhutiMatchToken | null = null as any;
  __qin_field_analysisOnlyOrPredictionStreak: number | null = null as any;
  __qin_field_lastOrPredictionDebug: string | null = null as any;
  __qin_field_orPredictionDebugEnabled: boolean | null = null as any;
  __qin_field_lastOrPredictionAmbiguityDiagnostics: any = null as any;
  __qin_field_lastOrPredictionRuntimePruningEnabled: boolean | null = null as any;
  __qin_field_parserRuntimePlan: com_subhuti_parser_SubhutiParserRuntimePlan | null = null as any;
  __qin_field_staticExecutionPlan: com_subhuti_parser_SubhutiStaticExecutionPlan | null = null as any;
  __qin_field_parserRuntimePlanBuilds: number | null = null as any;
  __qin_field_parserRuntimePlanCacheHits: number | null = null as any;
  __qin_field_ruleWrapperCalls: number | null = null as any;
  __qin_field_ruleCoreExecutions: number | null = null as any;
  __qin_field_ruleCacheKeyBuilds: number | null = null as any;
  __qin_field_ruleCacheHits: number | null = null as any;
  __qin_field_ruleCachePuts: number | null = null as any;
  __qin_field_ruleCachePassThroughSkips: number | null = null as any;
  __qin_field_ruleCacheTerminalLeafSkips: number | null = null as any;
  __qin_field_ruleCacheSpeculativeSkips: number | null = null as any;
  __qin_field_ruleCacheLowYieldSkips: number | null = null as any;
  __qin_field_ruleCacheAdaptiveLowYieldSkips: number | null = null as any;
  __qin_field_ruleWrapperPassThroughSkips: number | null = null as any;
  __qin_field_ruleWrapperTerminalLeafSkips: number | null = null as any;
  __qin_field_ruleWrapperRuleChainSkips: number | null = null as any;
  __qin_field_ruleWrapperDirectTerminalSkips: number | null = null as any;
  __qin_field_ruleWrapperDirectTerminalSequenceSkips: number | null = null as any;
  __qin_field_ruleWrapperDirectRecognizerPlanSkips: number | null = null as any;
  __qin_field_tokenCacheGets: number | null = null as any;
  __qin_field_tokenCacheHits: number | null = null as any;
  __qin_field_tokenCacheMisses: number | null = null as any;
  __qin_field_tokenCachePuts: number | null = null as any;
  __qin_field_tokenStreamGets: number | null = null as any;
  __qin_field_tokenStreamHits: number | null = null as any;
  __qin_field_currentTokenEntryCacheHits: number | null = null as any;
  __qin_field_sourceLookaheadCacheHits: number | null = null as any;
  __qin_field_sourceLookaheadCacheFills: number | null = null as any;
  __qin_field_sourceLookaheadCacheResets: number | null = null as any;
  __qin_field_preTokenizedRegexpNegativeHits: number | null = null as any;
  __qin_field_tokenCstNodes: number | null = null as any;
  __qin_field_ruleCstNodes: number | null = null as any;
  __qin_field_cstParentRebuilds: number | null = null as any;
  __qin_field_cachedCstAttachCount: number | null = null as any;
  __qin_field_coreRuleProfileEnabled: boolean | null = null as any;
  __qin_field_staticDecisionProfileEnabled: boolean | null = null as any;
  __qin_field_staticDecisionRetryCounts: any = null as any;
  __qin_field_ruleWrapperCallCounts: any = null as any;
  __qin_field_ruleCoreExecutionCounts: any = null as any;
  __qin_field_ruleCoreSuccessCounts: any = null as any;
  __qin_field_ruleCoreFailureCounts: any = null as any;
  __qin_field_ruleCacheHitCounts: any = null as any;
  __qin_field_ruleCachePutCounts: any = null as any;
  __qin_field_ruleCacheHitTokenSpanTotals: any = null as any;
  __qin_field_adaptiveRuleCacheHitCounts: any = null as any;
  __qin_field_adaptiveRuleCachePutCounts: any = null as any;
  __qin_field_adaptiveLowYieldMemoRules: any = null as any;
  __qin_field_ruleWrapperPassThroughCounts: any = null as any;
  __qin_field_ruleWrapperTerminalLeafCounts: any = null as any;
  __qin_field_ruleWrapperRuleChainCounts: any = null as any;
  __qin_field_activeRuleScopeStack: any = null as any;
  __qin_field_activeRuleProfileStack: any = null as any;
  __qin_field_orPredictionStateSaveRuleCounts: any = null as any;
  __qin_field_orPredictionStateSaveSkipRuleCounts: any = null as any;
  __qin_field_orPredictionCandidateRuleCounts: any = null as any;
  __qin_field_orPredictionCandidateRuleTotals: any = null as any;
  __qin_field_preTokenizedFallbackModeCounts: any = null as any;
  __qin_field__debugger: com_subhuti_debug_SubhutiTraceDebugger | null = null as any;
  __qin_field_errorHandler: com_subhuti_error_SubhutiErrorHandler | null = null as any;
  __qin_field_errorRecoveryMode: boolean | null = null as any;
  __qin_field_analysisMode: boolean | null = null as any;
  __qin_field_syncTokens: any = null as any;
  __qin_field_partialMatchCandidates: any = null as any;
  __qin_field_unparsedTokens: any = null as any;
  __qin_field_recoveryDiagnostics: any = null as any;
  __qin_field_parseRecordRoot: com_subhuti_parser_ParseRecordNode | null = null as any;
  __qin_field_parseRecordStack: any = null as any;
  __qin_field_speculativeParseDepth: number | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] === "function" || (typeof __qin_args[1] === "object" && typeof __qin_args[1].getName === "function" && typeof __qin_args[1].isAssignableFrom === "function")) && (__qin_args[2] === null || Array.isArray(__qin_args[2]) || __qin_args[2] instanceof __QinJavaUtilArrayList || __qin_args[2] instanceof __QinJavaUtilUnmodifiableList)) {
      const sourceCode: any = __qin_args[0];
      const tokenConsumerClass: any = __qin_args[1];
      const tokens: any = __qin_args[2];
      super();
      this.__qin_constructor_com_subhuti_parser_SubhutiParserState_3_0(sourceCode, tokenConsumerClass, tokens);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParserState/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParserState_3_0(sourceCode: string, tokenConsumerClass: any, tokens: any): void {
    this.__qin_field_sourceCode = null;
    this.__qin_field_lexer = null;
    this.__qin_field_tokenConsumer = null;
    this.__qin_field_tokens = null;
    this.__qin_field_tokenCursor = 0.0;
    this.__qin_field_currentIndex = 0.0;
    this.__qin_field_currentPosition = com_subhuti_struct_SubhutiPosition.start();
    this.__qin_field_currentMode = com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE;
    this.__qin_field_lastTokenName = null;
    this.__qin_field_tokenCache = null;
    this.__qin_field_parsedTokens = new __QinJavaUtilArrayList();
    this.__qin_field_preTokenizedDefaultModeInput = false;
    this.__qin_field_preTokenizedEntriesByOrdinal = null;
    this.__qin_field_preTokenizedOrdinalByCodeIndex = null;
    this.__qin_field_currentTokenEntryCacheSet = false;
    this.__qin_field_currentTokenEntryCacheCodeIndex = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_currentTokenEntryCacheLine = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_currentTokenEntryCacheColumn = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_currentTokenEntryCacheTokenCursor = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_currentTokenEntryCacheMode = null;
    this.__qin_field_currentTokenEntryCacheLastTokenName = null;
    this.__qin_field_currentTokenEntryCacheEntry = null;
    this.__qin_field_cstStack = new __QinJavaUtilArrayList();
    this.__qin_field_rootCst = null;
    this.__qin_field_buildCst = true;
    this.__qin_field_ruleExecutionDepth = 0.0;
    this.__qin_field_parseSuccess = true;
    this.__qin_field_activeRuleInvocationsByName = new __QinJavaUtilHashMap();
    this.__qin_field_indexedActiveRuleInvocations = [];
    this.__qin_field_indexedRuleInvocationsEnabled = true;
    this.__qin_field_indexedRuleInvocationLookups = 0.0;
    this.__qin_field_staticPrimitiveInvocationEntries = 0.0;
    this.__qin_field_staticRootRuleEntries = 0.0;
    this.__qin_field_staticGateDispatches = 0.0;
    this.__qin_field_staticActionDispatches = 0.0;
    this.__qin_field_staticActionRetryRestores = 0.0;
    this.__qin_field_staticBranchSelectorDispatches = 0.0;
    this.__qin_field_staticBranchSelectorNoMatches = 0.0;
    this.__qin_field_lastStaticNoMatchDecisionId = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_lastStaticAmbiguousDecisionId = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_lastStaticSharedPrefixSelection = "";
    this.__qin_field_lastStaticSharedPrefixNoMatch = "";
    this.__qin_field_staticBranchSelectorAmbiguities = 0.0;
    this.__qin_field_staticDecisionCandidateExecutions = 0.0;
    this.__qin_field_staticDecisionSkippedBranches = 0.0;
    this.__qin_field_staticDecisionGateSkips = 0.0;
    this.__qin_field_staticDecisionStateSaves = 0.0;
    this.__qin_field_staticDecisionStateSaveSkips = 0.0;
    this.__qin_field_staticDecisionDirectManyExecutions = 0.0;
    this.__qin_field_staticCoreRuleEntries = 0.0;
    this.__qin_field_staticRuleLoopRejects = 0.0;
    this.__qin_field_staticNonNullableEmptyRejects = 0.0;
    this.__qin_field_lastStaticNonNullableEmptyRule = "";
    this.__qin_field_staticExecutionModeBindings = 0.0;
    this.__qin_field_staticExecutionModeName = "UNBOUND";
    this.__qin_field_ruleCache = null;
    this.__qin_field_enableMemoization = true;
    this.__qin_field_enableOrPrediction = true;
    this.__qin_field_debugMode = false;
    this.__qin_field_firstTokenRecording = false;
    this.__qin_field_firstTokenRecordingTokenCount = 0.0;
    this.__qin_field_firstTokenRecordingMaxTokens = 1.0;
    this.__qin_field_orPredictionSkippedAlternatives = 0.0;
    this.__qin_field_orPredictionGateSkippedAlternatives = 0.0;
    this.__qin_field_orPredictionUnknownAlternatives = 0.0;
    this.__qin_field_orPredictionCandidateAlternatives = 0.0;
    this.__qin_field_orPredictionStateSaves = 0.0;
    this.__qin_field_orPredictionStateSaveSkips = 0.0;
    this.__qin_field_orPredictionDirectManyExecutions = 0.0;
    this.__qin_field_staticLl1LookaheadHits = 0.0;
    this.__qin_field_staticValueAwareLl1LookaheadHits = 0.0;
    this.__qin_field_staticLlkLookaheadHits = 0.0;
    this.__qin_field_alternativeStartPredictionDirectOptionExecutions = 0.0;
    this.__qin_field_predictionCurrentTokenCacheHits = 0.0;
    this.__qin_field_predictionCurrentTokenCacheMisses = 0.0;
    this.__qin_field_predictionCurrentTokenCacheSet = false;
    this.__qin_field_predictionCurrentTokenCacheIndex = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_predictionCurrentTokenCacheMode = null;
    this.__qin_field_predictionCurrentTokenCacheLastTokenName = null;
    this.__qin_field_predictionCurrentTokenCacheToken = null;
    this.__qin_field_analysisOnlyOrPredictionStreak = 0.0;
    this.__qin_field_lastOrPredictionDebug = "";
    this.__qin_field_orPredictionDebugEnabled = __QinJavaLangBoolean.getBoolean("subhuti.debug.prediction");
    this.__qin_field_lastOrPredictionAmbiguityDiagnostics = __QinJavaUtilList.of();
    this.__qin_field_lastOrPredictionRuntimePruningEnabled = false;
    this.__qin_field_parserRuntimePlan = null;
    this.__qin_field_staticExecutionPlan = null;
    this.__qin_field_parserRuntimePlanBuilds = 0.0;
    this.__qin_field_parserRuntimePlanCacheHits = 0.0;
    this.__qin_field_ruleWrapperCalls = 0.0;
    this.__qin_field_ruleCoreExecutions = 0.0;
    this.__qin_field_ruleCacheKeyBuilds = 0.0;
    this.__qin_field_ruleCacheHits = 0.0;
    this.__qin_field_ruleCachePuts = 0.0;
    this.__qin_field_ruleCachePassThroughSkips = 0.0;
    this.__qin_field_ruleCacheTerminalLeafSkips = 0.0;
    this.__qin_field_ruleCacheSpeculativeSkips = 0.0;
    this.__qin_field_ruleCacheLowYieldSkips = 0.0;
    this.__qin_field_ruleCacheAdaptiveLowYieldSkips = 0.0;
    this.__qin_field_ruleWrapperPassThroughSkips = 0.0;
    this.__qin_field_ruleWrapperTerminalLeafSkips = 0.0;
    this.__qin_field_ruleWrapperRuleChainSkips = 0.0;
    this.__qin_field_ruleWrapperDirectTerminalSkips = 0.0;
    this.__qin_field_ruleWrapperDirectTerminalSequenceSkips = 0.0;
    this.__qin_field_ruleWrapperDirectRecognizerPlanSkips = 0.0;
    this.__qin_field_tokenCacheGets = 0.0;
    this.__qin_field_tokenCacheHits = 0.0;
    this.__qin_field_tokenCacheMisses = 0.0;
    this.__qin_field_tokenCachePuts = 0.0;
    this.__qin_field_tokenStreamGets = 0.0;
    this.__qin_field_tokenStreamHits = 0.0;
    this.__qin_field_currentTokenEntryCacheHits = 0.0;
    this.__qin_field_sourceLookaheadCacheHits = 0.0;
    this.__qin_field_sourceLookaheadCacheFills = 0.0;
    this.__qin_field_sourceLookaheadCacheResets = 0.0;
    this.__qin_field_preTokenizedRegexpNegativeHits = 0.0;
    this.__qin_field_tokenCstNodes = 0.0;
    this.__qin_field_ruleCstNodes = 0.0;
    this.__qin_field_cstParentRebuilds = 0.0;
    this.__qin_field_cachedCstAttachCount = 0.0;
    this.__qin_field_coreRuleProfileEnabled = __QinJavaLangBoolean.getBoolean("subhuti.profile.rules");
    this.__qin_field_staticDecisionProfileEnabled = __QinJavaLangBoolean.getBoolean("subhuti.profile.decisions");
    this.__qin_field_staticDecisionRetryCounts = new __QinJavaUtilHashMap();
    this.__qin_field_ruleWrapperCallCounts = new __QinJavaUtilHashMap();
    this.__qin_field_ruleCoreExecutionCounts = new __QinJavaUtilHashMap();
    this.__qin_field_ruleCoreSuccessCounts = new __QinJavaUtilHashMap();
    this.__qin_field_ruleCoreFailureCounts = new __QinJavaUtilHashMap();
    this.__qin_field_ruleCacheHitCounts = new __QinJavaUtilHashMap();
    this.__qin_field_ruleCachePutCounts = new __QinJavaUtilHashMap();
    this.__qin_field_ruleCacheHitTokenSpanTotals = new __QinJavaUtilHashMap();
    this.__qin_field_adaptiveRuleCacheHitCounts = new __QinJavaUtilHashMap();
    this.__qin_field_adaptiveRuleCachePutCounts = new __QinJavaUtilHashMap();
    this.__qin_field_adaptiveLowYieldMemoRules = new __QinJavaUtilHashSet();
    this.__qin_field_ruleWrapperPassThroughCounts = new __QinJavaUtilHashMap();
    this.__qin_field_ruleWrapperTerminalLeafCounts = new __QinJavaUtilHashMap();
    this.__qin_field_ruleWrapperRuleChainCounts = new __QinJavaUtilHashMap();
    this.__qin_field_activeRuleScopeStack = new __QinJavaUtilArrayDeque();
    this.__qin_field_activeRuleProfileStack = new __QinJavaUtilArrayDeque();
    this.__qin_field_orPredictionStateSaveRuleCounts = new __QinJavaUtilHashMap();
    this.__qin_field_orPredictionStateSaveSkipRuleCounts = new __QinJavaUtilHashMap();
    this.__qin_field_orPredictionCandidateRuleCounts = new __QinJavaUtilHashMap();
    this.__qin_field_orPredictionCandidateRuleTotals = new __QinJavaUtilHashMap();
    this.__qin_field_preTokenizedFallbackModeCounts = new __QinJavaUtilHashMap();
    this.__qin_field__debugger = null;
    this.__qin_field_errorHandler = null;
    this.__qin_field_errorRecoveryMode = false;
    this.__qin_field_analysisMode = false;
    this.__qin_field_syncTokens = new __QinJavaUtilHashSet();
    this.__qin_field_partialMatchCandidates = new __QinJavaUtilArrayList();
    this.__qin_field_unparsedTokens = new __QinJavaUtilArrayList();
    this.__qin_field_recoveryDiagnostics = new __QinJavaUtilArrayList();
    this.__qin_field_parseRecordRoot = null;
    this.__qin_field_parseRecordStack = new __QinJavaUtilArrayList();
    this.__qin_field_speculativeParseDepth = 0.0;
    if (__qin_binary__("==", sourceCode, null)) {
      throw new __QinJavaLangIllegalArgumentException("sourceCode cannot be null");
    }
    if (__qin_binary__("==", tokenConsumerClass, null)) {
      throw new __QinJavaLangIllegalArgumentException("tokenConsumerClass cannot be null");
    }
    if ((__qin_binary__("==", tokens, null) || ((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(tokens))) {
      throw new __QinJavaLangIllegalArgumentException("tokens cannot be null or empty");
    }
    this.__qin_field_sourceCode = sourceCode;
    this.__qin_field_tokens = __QinJavaUtilList.copyOf(tokens);
    this.__qin_field_lexer = new com_subhuti_lexer_SubhutiLexer(tokens);
    this.__qin_field_tokenCache = new com_subhuti_parser_SubhutiTokenCache(__QinJavaLangString.length(sourceCode), this.__qin_field_lexer.dependsOnPreviousTokenName());
    this.__qin_field_tokenConsumer = this.createTokenConsumer(tokenConsumerClass);
  }
  staticGrammarPlan(): any {
    throw new Error("Abstract Java method is not implemented: staticGrammarPlan");
  }
  ruleCache(): any {
    if (__qin_binary__("==", this.__qin_field_ruleCache, null)) {
      if (__qin_binary__("!=", this.staticGrammarPlan(), null)) {
        throw new __QinJavaLangIllegalStateException("generated static parsers do not own a generic packrat cache");
      }
      this.__qin_field_ruleCache = new com_subhuti_cache_SubhutiPackratCache(262144.0);
    }
    return this.__qin_field_ruleCache;
  }
  staticExecutionPlan(): any {
    if (__qin_binary__("==", this.__qin_field_staticExecutionPlan, null)) {
      let grammarPlan: any = this.staticGrammarPlan();
      if (__qin_binary__("==", grammarPlan, null)) {
        throw new __QinJavaLangIllegalStateException("static execution requires a generated grammar plan");
      }
      this.__qin_field_staticExecutionPlan = grammarPlan.linkedExecutionPlan(this.__qin_field_lexer);
    }
    return this.__qin_field_staticExecutionPlan;
  }
  tokenVocabulary(): any {
    return this.__qin_field_lexer.vocabulary();
  }
  clearRuleCacheIfAllocated(): any {
    if (__qin_binary__("!=", this.__qin_field_ruleCache, null)) {
      this.__qin_field_ruleCache.clear();
    }
    return null;
  }
  ruleCacheStatsReport(): any {
    return (__qin_binary__("==", this.__qin_field_ruleCache, null) ? "PackratCache(unallocated)" : this.__qin_field_ruleCache.getStatsReport());
  }
  createTokenConsumer(clazz: any): any {
    try {
      let consumer: any = clazz.getDeclaredConstructor().newInstance();
      consumer.setParser(this);
      return consumer;
    } catch (e) {
      if (!(e instanceof __QinJavaLangException)) {
        throw e;
      }
      throw new __QinJavaLangRuntimeException(("Failed to create token consumer: " + e.getMessage()), e);
    }
    return null;
  }
  initTopLevelData(): any {
    this.__qin_field_parseSuccess = true;
    this.__qin_field__parseSuccess = true;
    this.__qin_field_rootCst = null;
    this.__qin_field_cstStack.clear();
    this.__qin_field_activeRuleScopeStack.clear();
    this.__qin_field_activeRuleInvocationsByName.values().forEach(((...__qin_args) => { const __qin_method = com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations.clear; if (typeof __qin_method === "function") { return __qin_method.apply(com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, __qin_args); } const __qin_receiver = __qin_args[0]; return __qin_receiver.clear(...__qin_args.slice(1)); }));
    for (const invocations of this.__qin_field_indexedActiveRuleInvocations) {
      if (__qin_binary__("!=", invocations, null)) {
        invocations.clear();
      }
    }
    this.__qin_field_currentIndex = 0.0;
    this.__qin_field_currentPosition = com_subhuti_struct_SubhutiPosition.start();
    this.__qin_field_parsedTokens.clear();
    this.__qin_field_tokenCursor = 0.0;
    this.__qin_field_tokenCache.clear();
    this.clearCurrentTokenEntryCache();
    this.__qin_field_lastTokenName = null;
    this.__qin_field_recoveryDiagnostics.clear();
    this.resetCoreProfileStats();
    return null;
  }
  usePreTokenizedDefaultModeInput(tokenStream: any): any {
    if (__qin_binary__("==", tokenStream, null)) {
      throw new __QinJavaLangIllegalArgumentException("tokenStream cannot be null");
    }
    let ordinalByCodeIndex: any = [];
    __QinJavaUtilArrays.fill(ordinalByCodeIndex, ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(tokenStream));
    let entries: any = [];
    let previousEnd: any = 0.0;
    for (let i: any = 0.0; __qin_binary__("<", i, ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(tokenStream)); i++) {
      let token: any = ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(tokenStream, i);
      if ((__qin_binary__("==", token, null) || __qin_binary__("==", token.index(), null))) {
        throw new __QinJavaLangIllegalArgumentException(("tokenStream contains token without source index at ordinal " + i));
      }
      let tokenStart: any = token.index();
      let tokenEnd: any = token.endOffset();
      if ((__qin_binary__("<", tokenStart, 0.0) || __qin_binary__("<", tokenEnd, tokenStart) || __qin_binary__(">", tokenEnd, __QinJavaLangString.length(this.__qin_field_sourceCode)))) {
        throw new __QinJavaLangIllegalArgumentException(("tokenStream contains invalid source range at ordinal " + i));
      }
      for (let codeIndex: any = Math.max(0.0, previousEnd); __qin_binary__("<", codeIndex, tokenEnd); codeIndex++) {
        ordinalByCodeIndex[codeIndex] = i;
      }
      let nextPosition: any = this.advancePosition(token);
      entries[i] = new com_subhuti_lexer_TokenCacheEntry(token, tokenEnd, nextPosition.line(), nextPosition.column(), token.tokenName(), tokenEnd);
      previousEnd = tokenEnd;
    }
    this.__qin_field_preTokenizedDefaultModeInput = true;
    this.__qin_field_preTokenizedOrdinalByCodeIndex = ordinalByCodeIndex;
    this.__qin_field_preTokenizedEntriesByOrdinal = entries;
    this.__qin_field_tokenCache.clear();
    this.clearCurrentTokenEntryCache();
    return this;
  }
  useDefaultModeTokenArrayInput(): any {
    return this.usePreTokenizedDefaultModeInput(this.__qin_field_lexer.tokenize(this.__qin_field_sourceCode));
  }
  isUsingDefaultModeTokenArrayInput(): any {
    return this.__qin_field_preTokenizedDefaultModeInput;
  }
  clearCurrentTokenEntryCache(): any {
    this.__qin_field_currentTokenEntryCacheSet = false;
    this.__qin_field_currentTokenEntryCacheCodeIndex = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_currentTokenEntryCacheLine = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_currentTokenEntryCacheColumn = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_currentTokenEntryCacheTokenCursor = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_currentTokenEntryCacheMode = null;
    this.__qin_field_currentTokenEntryCacheLastTokenName = null;
    this.__qin_field_currentTokenEntryCacheEntry = null;
    return null;
  }
  preTokenizedEntryAt(codeIndex: number, mode: com_subhuti_struct_LexerMode): any {
    if ((!this.__qin_field_preTokenizedDefaultModeInput || (__qin_binary__("!=", mode, null) && !com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE.equals(mode)) || __qin_binary__("==", this.__qin_field_preTokenizedOrdinalByCodeIndex, null) || __qin_binary__("<", codeIndex, 0.0) || __qin_binary__(">=", codeIndex, this.__qin_field_preTokenizedOrdinalByCodeIndex.length))) {
      return null;
    }
    let ordinal: any = this.__qin_field_preTokenizedOrdinalByCodeIndex[codeIndex];
    if ((__qin_binary__("<", ordinal, 0.0) || __qin_binary__(">=", ordinal, this.__qin_field_preTokenizedEntriesByOrdinal.length))) {
      return null;
    }
    return this.__qin_field_preTokenizedEntriesByOrdinal[ordinal];
  }
  preTokenizedEntryAtParsedOrdinal(offset: number, mode: com_subhuti_struct_LexerMode): any {
    if ((!this.__qin_field_preTokenizedDefaultModeInput || (__qin_binary__("!=", mode, null) && !com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE.equals(mode)) || __qin_binary__("==", this.__qin_field_preTokenizedEntriesByOrdinal, null) || __qin_binary__("<", offset, 1.0))) {
      return null;
    }
    let ordinal: any = __qin_binary__("-", __qin_binary__("+", this.__qin_field_tokenCursor, offset), 1.0);
    if ((__qin_binary__("<", ordinal, 0.0) || __qin_binary__(">=", ordinal, this.__qin_field_preTokenizedEntriesByOrdinal.length))) {
      return null;
    }
    if (__qin_binary__("<", this.__qin_field_tokenCursor, this.__qin_field_preTokenizedEntriesByOrdinal.length)) {
      let currentEntry: any = this.__qin_field_preTokenizedEntriesByOrdinal[this.__qin_field_tokenCursor];
      let currentToken: any = (__qin_binary__("!=", currentEntry, null) ? currentEntry.getToken() : null);
      let currentTokenStart: any = (__qin_binary__("!=", currentToken, null) ? currentToken.index() : null);
      if ((__qin_binary__("!=", currentTokenStart, null) && __qin_binary__("<", currentTokenStart, this.__qin_field_currentIndex))) {
        return null;
      }
    }
    let entry: any = this.__qin_field_preTokenizedEntriesByOrdinal[ordinal];
    let token: any = (__qin_binary__("!=", entry, null) ? entry.getToken() : null);
    let tokenStart: any = (__qin_binary__("!=", token, null) ? token.index() : null);
    if ((__qin_binary__("!=", tokenStart, null) && __qin_binary__("<", tokenStart, this.__qin_field_currentIndex))) {
      return null;
    }
    return entry;
  }
  tokenOrdinalAtCodeIndex(codeIndex: number): any {
    if ((!this.__qin_field_preTokenizedDefaultModeInput || __qin_binary__("==", this.__qin_field_preTokenizedOrdinalByCodeIndex, null))) {
      return this.__qin_field_tokenCursor;
    }
    let boundedIndex: any = Math.max(0.0, Math.min(codeIndex, __qin_binary__("-", this.__qin_field_preTokenizedOrdinalByCodeIndex.length, 1.0)));
    return this.__qin_field_preTokenizedOrdinalByCodeIndex[boundedIndex];
  }
  advanceTokenCursor(entry: com_subhuti_lexer_TokenCacheEntry): any {
    if ((__qin_binary__("==", entry, null) || __qin_binary__("==", entry.getToken(), null))) {
      throw new __QinJavaLangIllegalArgumentException("entry must contain a token");
    }
    this.__qin_field_currentIndex = entry.getNextCodeIndex();
    this.__qin_field_currentPosition = com_subhuti_struct_SubhutiPosition.of(entry.getNextLine(), entry.getNextColumn(), entry.getNextCodeIndex());
    this.__qin_field_lastTokenName = entry.getToken().tokenName();
    this.__qin_field_tokenCursor = (this.__qin_field_preTokenizedDefaultModeInput ? this.tokenOrdinalAtCodeIndex(entry.getNextCodeIndex()) : __qin_binary__("+", this.__qin_field_tokenCursor, 1.0));
    this.clearCurrentTokenEntryCache();
    return null;
  }
  cursorStamp(): any {
    return Math.trunc(this.__qin_field_tokenCursor);
  }
  advancePosition(token: com_subhuti_struct_SubhutiMatchToken): any {
    let value: any = token.tokenValue();
    let line: any = token.rowNum();
    let column: any = token.columnStartNum();
    let lineBreaks: any = this.countLineBreaks(value);
    if (__qin_binary__("==", lineBreaks, 0.0)) {
      return com_subhuti_struct_SubhutiPosition.of(line, __qin_binary__("+", column, __QinJavaLangString.length(value)), token.endOffset());
    }
    let lastBreakIndex: any = value.lastIndexOf("\n");
    if (__qin_binary__("==", lastBreakIndex, __qin_binary__("-", 0.0, 1.0))) {
      lastBreakIndex = value.lastIndexOf("\r");
    }
    return com_subhuti_struct_SubhutiPosition.of(__qin_binary__("+", line, lineBreaks), __qin_binary__("-", __QinJavaLangString.length(value), lastBreakIndex), token.endOffset());
  }
  countLineBreaks(text: string): any {
    let count: any = 0.0;
    for (let i: any = 0.0; __qin_binary__("<", i, __QinJavaLangString.length(text)); i++) {
      let ch: any = __QinJavaLangString.charAt(text, i);
      if (__qin_binary__("==", ch, "\n")) {
        count++;
      } else {
        if (__qin_binary__("==", ch, "\r")) {
          count++;
          if ((__qin_binary__("<", __qin_binary__("+", i, 1.0), __QinJavaLangString.length(text)) && __qin_binary__("==", __QinJavaLangString.charAt(text, __qin_binary__("+", i, 1.0)), "\n"))) {
            i++;
          }
        }
      }
    }
    return count;
  }
  resetCoreProfileStats(): any {
    this.__qin_field_ruleWrapperCalls = 0.0;
    this.__qin_field_ruleCoreExecutions = 0.0;
    this.__qin_field_ruleCacheKeyBuilds = 0.0;
    this.__qin_field_ruleCacheHits = 0.0;
    this.__qin_field_ruleCachePuts = 0.0;
    this.__qin_field_ruleCachePassThroughSkips = 0.0;
    this.__qin_field_ruleCacheTerminalLeafSkips = 0.0;
    this.__qin_field_ruleCacheSpeculativeSkips = 0.0;
    this.__qin_field_ruleCacheLowYieldSkips = 0.0;
    this.__qin_field_ruleCacheAdaptiveLowYieldSkips = 0.0;
    this.__qin_field_ruleWrapperPassThroughSkips = 0.0;
    this.__qin_field_ruleWrapperTerminalLeafSkips = 0.0;
    this.__qin_field_ruleWrapperRuleChainSkips = 0.0;
    this.__qin_field_ruleWrapperDirectTerminalSkips = 0.0;
    this.__qin_field_ruleWrapperDirectTerminalSequenceSkips = 0.0;
    this.__qin_field_ruleWrapperDirectRecognizerPlanSkips = 0.0;
    this.__qin_field_indexedRuleInvocationLookups = 0.0;
    this.__qin_field_staticPrimitiveInvocationEntries = 0.0;
    this.__qin_field_staticRootRuleEntries = 0.0;
    this.__qin_field_staticGateDispatches = 0.0;
    this.__qin_field_staticActionDispatches = 0.0;
    this.__qin_field_staticActionRetryRestores = 0.0;
    this.__qin_field_staticBranchSelectorDispatches = 0.0;
    this.__qin_field_staticBranchSelectorNoMatches = 0.0;
    this.__qin_field_lastStaticNoMatchDecisionId = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_lastStaticAmbiguousDecisionId = __qin_binary__("-", 0.0, 1.0);
    this.__qin_field_lastStaticSharedPrefixSelection = "";
    this.__qin_field_lastStaticSharedPrefixNoMatch = "";
    this.__qin_field_staticBranchSelectorAmbiguities = 0.0;
    this.__qin_field_staticDecisionCandidateExecutions = 0.0;
    this.__qin_field_staticDecisionSkippedBranches = 0.0;
    this.__qin_field_staticDecisionGateSkips = 0.0;
    this.__qin_field_staticDecisionStateSaves = 0.0;
    this.__qin_field_staticDecisionStateSaveSkips = 0.0;
    this.__qin_field_staticDecisionDirectManyExecutions = 0.0;
    this.__qin_field_staticCoreRuleEntries = 0.0;
    this.__qin_field_staticRuleLoopRejects = 0.0;
    this.__qin_field_staticNonNullableEmptyRejects = 0.0;
    this.__qin_field_lastStaticNonNullableEmptyRule = "";
    this.__qin_field_staticExecutionModeBindings = 0.0;
    this.__qin_field_staticExecutionModeName = "UNBOUND";
    this.__qin_field_tokenCacheGets = 0.0;
    this.__qin_field_tokenCacheHits = 0.0;
    this.__qin_field_tokenCacheMisses = 0.0;
    this.__qin_field_tokenCachePuts = 0.0;
    this.__qin_field_tokenStreamGets = 0.0;
    this.__qin_field_tokenStreamHits = 0.0;
    this.__qin_field_currentTokenEntryCacheHits = 0.0;
    this.__qin_field_sourceLookaheadCacheHits = 0.0;
    this.__qin_field_sourceLookaheadCacheFills = 0.0;
    this.__qin_field_sourceLookaheadCacheResets = 0.0;
    this.__qin_field_preTokenizedRegexpNegativeHits = 0.0;
    this.__qin_field_orPredictionDirectManyExecutions = 0.0;
    this.__qin_field_staticLl1LookaheadHits = 0.0;
    this.__qin_field_staticValueAwareLl1LookaheadHits = 0.0;
    this.__qin_field_staticLlkLookaheadHits = 0.0;
    this.__qin_field_alternativeStartPredictionDirectOptionExecutions = 0.0;
    this.__qin_field_parserRuntimePlanBuilds = 0.0;
    this.__qin_field_parserRuntimePlanCacheHits = 0.0;
    this.__qin_field_tokenCstNodes = 0.0;
    this.__qin_field_ruleCstNodes = 0.0;
    this.__qin_field_cstParentRebuilds = 0.0;
    this.__qin_field_cachedCstAttachCount = 0.0;
    this.__qin_field_adaptiveRuleCacheHitCounts.clear();
    this.__qin_field_adaptiveRuleCachePutCounts.clear();
    this.__qin_field_adaptiveLowYieldMemoRules.clear();
    if (this.__qin_field_staticDecisionProfileEnabled) {
      this.__qin_field_staticDecisionRetryCounts.clear();
    }
    if (this.__qin_field_coreRuleProfileEnabled) {
      this.__qin_field_ruleWrapperCallCounts.clear();
      this.__qin_field_ruleCoreExecutionCounts.clear();
      this.__qin_field_ruleCoreSuccessCounts.clear();
      this.__qin_field_ruleCoreFailureCounts.clear();
      this.__qin_field_ruleCacheHitCounts.clear();
      this.__qin_field_ruleCachePutCounts.clear();
      this.__qin_field_ruleCacheHitTokenSpanTotals.clear();
      this.__qin_field_ruleWrapperPassThroughCounts.clear();
      this.__qin_field_ruleWrapperTerminalLeafCounts.clear();
      this.__qin_field_ruleWrapperRuleChainCounts.clear();
      this.__qin_field_activeRuleProfileStack.clear();
      this.__qin_field_orPredictionStateSaveRuleCounts.clear();
      this.__qin_field_orPredictionStateSaveSkipRuleCounts.clear();
      this.__qin_field_orPredictionCandidateRuleCounts.clear();
      this.__qin_field_orPredictionCandidateRuleTotals.clear();
      this.__qin_field_preTokenizedFallbackModeCounts.clear();
    }
    return null;
  }
  saveState(): any {
    if ((!this.__qin_field_buildCst && ((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(this.__qin_field_parsedTokens))) {
      return new com_subhuti_parser_SubhutiBackData(this.__qin_field_tokenCursor, this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), this.__qin_field_lastTokenName, 0.0, 0.0);
    }
    let currentCst: any = this.getCurCst();
    return new com_subhuti_parser_SubhutiBackData(this.__qin_field_tokenCursor, this.__qin_field_currentIndex, this.__qin_field_currentPosition.line(), this.__qin_field_currentPosition.column(), this.__qin_field_lastTokenName, (__qin_binary__("!=", currentCst, null) ? currentCst.getChildCount() : 0.0), ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parsedTokens));
  }
  getCurToken(): any {
    return this.LA(1.0);
  }
  restoreState(state: com_subhuti_parser_SubhutiBackData): any {
    this.__qin_field_tokenCursor = state.tokenCursor();
    this.__qin_field_currentIndex = state.codeIndex();
    this.__qin_field_currentPosition = com_subhuti_struct_SubhutiPosition.of(state.codeLine(), state.codeColumn(), this.__qin_field_currentIndex);
    this.__qin_field_lastTokenName = state.lastTokenName();
    if ((!this.__qin_field_buildCst && ((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(this.__qin_field_parsedTokens))) {
      return null;
    }
    let currentCst: any = this.getCurCst();
    if ((__qin_binary__("!=", currentCst, null) && __qin_binary__(">", currentCst.getChildCount(), state.curCstChildrenLength()))) {
      let children: any = currentCst.getChildren();
      while (__qin_binary__(">", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(children), state.curCstChildrenLength())) {
        children.remove(__qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(children), 1.0));
      }
    }
    while (__qin_binary__(">", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parsedTokens), state.parsedTokensLength())) {
      this.__qin_field_parsedTokens.remove(__qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_parsedTokens), 1.0));
    }
    return null;
  }
  getCurCst(): any {
    if (((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(this.__qin_field_cstStack)) {
      return null;
    }
    return ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(this.__qin_field_cstStack, __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(this.__qin_field_cstStack), 1.0));
  }
  getCst(): any {
    return this.__qin_field_rootCst;
  }
  setLocation(cst: com_subhuti_struct_SubhutiCst): any {
    if ((__qin_binary__("!=", cst.getChildren(), null) && !((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(cst.getChildren()))) {
      let firstChild: any = ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(cst.getChildren(), 0.0);
      let lastChild: any = ((__qin_collection, __qin_index) => Array.isArray(__qin_collection) ? __qin_collection[Number(__qin_index)] : __qin_collection.get(__qin_index))(cst.getChildren(), __qin_binary__("-", ((__qin_collection) => __qin_collection == null ? 0 : (Array.isArray(__qin_collection) ? __qin_collection.length : __qin_collection.size()))(cst.getChildren()), 1.0));
      if ((__qin_binary__("!=", firstChild.getLocation(), null) && __qin_binary__("!=", lastChild.getLocation(), null))) {
        let location: any = com_subhuti_struct_SubhutiSourceLocation.of(firstChild.getLocation().start(), lastChild.getLocation().end());
        cst.setLocation(location);
      }
    }
    return null;
  }
  isParserFail(): any {
    return (!this.__qin_field_parseSuccess);
  }
  setParseFail(): any {
    this.__qin_field_parseSuccess = false;
    this.__qin_field__parseSuccess = false;
    return null;
  }
  setParseSuccess(): any {
    this.__qin_field_parseSuccess = true;
    this.__qin_field__parseSuccess = true;
    return null;
  }
  getCurrentPosition(): any {
    return this.__qin_field_currentPosition;
  }
  getCurrentIndex(): any {
    return this.__qin_field_currentIndex;
  }
  getSourceCode(): any {
    return this.__qin_field_sourceCode;
  }
  isAtEnd(): any {
    return __qin_binary__(">=", this.__qin_field_currentIndex, __QinJavaLangString.length(this.__qin_field_sourceCode));
  }
  setSyncTokens(...tokens: string[]): any {
    this.__qin_field_syncTokens.clear();
    if (__qin_binary__("!=", tokens, null)) {
      for (const token of tokens) {
        this.__qin_field_syncTokens.add(token);
      }
    }
    return this;
  }
  addSyncTokens(...tokens: string[]): any {
    if (__qin_binary__("!=", tokens, null)) {
      for (const token of tokens) {
        this.__qin_field_syncTokens.add(token);
      }
    }
    return this;
  }
  enableErrorRecovery(): any {
    this.__qin_field_errorRecoveryMode = true;
    if (((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(this.__qin_field_syncTokens)) {
      this.addSyncTokens("LetTok", "ConstTok", "VarTok", "FunctionTok", "ClassTok", "AsyncTok", "IfTok", "ForTok", "WhileTok", "DoTok", "SwitchTok", "TryTok", "ThrowTok", "ReturnTok", "BreakTok", "ContinueTok", "ImportTok", "ExportTok", "DebuggerTok", "Semicolon");
    }
    return this;
  }
  isErrorRecoveryMode(): any {
    return this.__qin_field_errorRecoveryMode;
  }
  getUnparsedTokens(): any {
    return new __QinJavaUtilArrayList(this.__qin_field_unparsedTokens);
  }
  hasUnparsedTokens(): any {
    return (!((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(this.__qin_field_unparsedTokens));
  }
  getRecoveryDiagnostics(): any {
    return new __QinJavaUtilArrayList(this.__qin_field_recoveryDiagnostics);
  }
  hasRecoveryDiagnostics(): any {
    return (!((__qin_collection) => __qin_collection == null ? true : (Array.isArray(__qin_collection) ? __qin_collection.length === 0 : __qin_collection.isEmpty()))(this.__qin_field_recoveryDiagnostics));
  }
  recordRecoveryDiagnostic(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_subhuti_struct_SubhutiMatchToken))) return this.__qin_overload_recordRecoveryDiagnostic_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_subhuti_struct_SubhutiPosition)) && (__qin_args[2] === null || __qin_instanceof__(__qin_args[2], com_subhuti_struct_SubhutiPosition))) return this.__qin_overload_recordRecoveryDiagnostic_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: recordRecoveryDiagnostic/" + __qin_args.length);
  }
  __qin_overload_recordRecoveryDiagnostic_2_0(message: string, token: com_subhuti_struct_SubhutiMatchToken): any {
    if ((!this.isErrorRecoveryMode() || __qin_binary__("==", token, null) || token.isEof())) {
      return null;
    }
    this.__qin_field_recoveryDiagnostics.add(new com_subhuti_parser_SubhutiRecoveryDiagnostic(message, token.startPosition(), token.endPosition(), token.tokenName(), token.value()));
    return null;
  }
  __qin_overload_recordRecoveryDiagnostic_3_1(message: string, start: com_subhuti_struct_SubhutiPosition, end: com_subhuti_struct_SubhutiPosition): any {
    if ((!this.isErrorRecoveryMode())) {
      return null;
    }
    this.__qin_field_recoveryDiagnostics.add(new com_subhuti_parser_SubhutiRecoveryDiagnostic(message, start, end, null, null));
    return null;
  }
  analysisMode(enable: boolean): any {
    this.__qin_field_analysisMode = enable;
    return this;
  }
  isAnalysisMode(): any {
    return this.__qin_field_analysisMode;
  }
  errorHandler(enable: boolean): any {
    if ((enable && __qin_binary__("==", this.__qin_field_errorHandler, null))) {
      this.__qin_field_errorHandler = new com_subhuti_error_SubhutiErrorHandler();
    } else {
      if ((!enable)) {
        this.__qin_field_errorHandler = null;
      }
    }
    return this;
  }
  getErrorHandler(): any {
    return this.__qin_field_errorHandler;
  }
  getRuleStack(): any {
    let ruleStack: any = new __QinJavaUtilArrayList();
    for (const cst of this.__qin_field_cstStack) {
      ruleStack.add(cst.getName());
    }
    return ruleStack;
  }
  getParsedTokens(): any {
    return new __QinJavaUtilArrayList(this.__qin_field_parsedTokens);
  }
  lastTokenIndex(): any {
    return (__qin_binary__("==", this.__qin_field_tokenCursor, 0.0) ? __qin_binary__("-", 0.0, 1.0) : __qin_binary__("-", this.__qin_field_tokenCursor, 1.0));
  }
  currentTokenIndex(): any {
    return this.__qin_field_tokenCursor;
  }
  shouldStoreParsedTokens(): any {
    return (this.__qin_field_buildCst || this.isErrorRecoveryMode() || __qin_binary__("!=", this.__qin_field__debugger, null));
  }
}
const SubhutiParserState = com_subhuti_parser_SubhutiParserState;
class com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations {
  __qin_field_cacheKeyExtras: any[] | null = null as any;
  __qin_field_cursorStamps: number[] | null = null as any;
  __qin_field_modes: com_subhuti_struct_LexerMode[] | null = null as any;
  __qin_field_lastTokenNames: string[] | null = null as any;
  __qin_field_size: number | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length !== 0) {
      throw new Error("Unsupported Java constructor arity: SubhutiParserState$ActiveRuleInvocations/" + __qin_args.length);
    }
    this.__qin_field_cacheKeyExtras = [];
    this.__qin_field_cursorStamps = [];
    this.__qin_field_modes = [];
    this.__qin_field_lastTokenNames = [];
    this.__qin_field_size = null;
  }
  enter(cacheKeyExtra: any, cursorStamp: number, mode: com_subhuti_struct_LexerMode, lastTokenName: string): any {
    let normalizedExtra: any = (__qin_binary__("==", cacheKeyExtra, null) ? "" : cacheKeyExtra);
    for (let i: any = 0.0; __qin_binary__("<", i, this.__qin_field_size); i++) {
      if ((__qin_binary__("==", this.__qin_field_cursorStamps[i], cursorStamp) && __qin_binary__("==", this.__qin_field_modes[i], mode) && __QinJavaUtilObjects.equals(this.__qin_field_cacheKeyExtras[i], normalizedExtra) && __QinJavaUtilObjects.equals(this.__qin_field_lastTokenNames[i], lastTokenName))) {
        return false;
      }
    }
    this.ensureCapacity(__qin_binary__("+", this.__qin_field_size, 1.0));
    this.__qin_field_cacheKeyExtras[this.__qin_field_size] = normalizedExtra;
    this.__qin_field_cursorStamps[this.__qin_field_size] = cursorStamp;
    this.__qin_field_modes[this.__qin_field_size] = mode;
    this.__qin_field_lastTokenNames[this.__qin_field_size] = lastTokenName;
    this.__qin_field_size++;
    return true;
  }
  exit(): any {
    let index: any = --this.__qin_field_size;
    this.__qin_field_cacheKeyExtras[index] = null;
    this.__qin_field_modes[index] = null;
    this.__qin_field_lastTokenNames[index] = null;
    return null;
  }
  clear(): any {
    while (__qin_binary__(">", this.__qin_field_size, 0.0)) {
      this.exit();
    }
    return null;
  }
  ensureCapacity(capacity: number): any {
    if (__qin_binary__("<=", capacity, this.__qin_field_cursorStamps.length)) {
      return null;
    }
    let newCapacity: any = __qin_binary__("*", this.__qin_field_cursorStamps.length, 2.0);
    this.__qin_field_cacheKeyExtras = __QinJavaUtilArrays.copyOf(this.__qin_field_cacheKeyExtras, newCapacity);
    this.__qin_field_cursorStamps = __QinJavaUtilArrays.copyOf(this.__qin_field_cursorStamps, newCapacity);
    this.__qin_field_modes = __QinJavaUtilArrays.copyOf(this.__qin_field_modes, newCapacity);
    this.__qin_field_lastTokenNames = __QinJavaUtilArrays.copyOf(this.__qin_field_lastTokenNames, newCapacity);
    return null;
  }
}
const SubhutiParserState$ActiveRuleInvocations = com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations;
class com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException extends java_lang_Error {
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 0 && true) {
      super(null, null, false, false);
      this.__qin_constructor_com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException_0_0();
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParserState$SubhutiFirstTokenRecordedException/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException_0_0(): void {
    null;
  }
}
const SubhutiParserState$SubhutiFirstTokenRecordedException = com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException;
class com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException extends java_lang_Error {
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 0 && true) {
      super(null, null, false, false);
      this.__qin_constructor_com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException_0_0();
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParserState$SubhutiFirstTokenUnknownException/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException_0_0(): void {
    null;
  }
}
const SubhutiParserState$SubhutiFirstTokenUnknownException = com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException;

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException };
