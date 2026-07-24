import { com_subhuti_lexer_SubhutiLexer, com_subhuti_lexer_SubhutiLexer as SubhutiLexer, com_subhuti_lexer_SubhutiLexer$MatchedTokenInfo, com_subhuti_lexer_SubhutiLexer$MatchedTokenInfo as MatchedTokenInfo, com_subhuti_lexer_SubhutiLexer$LexerException, com_subhuti_lexer_SubhutiLexer$LexerException as LexerException } from "../lexer/SubhutiLexer.ts";
import { com_subhuti_parser_SubhutiStaticGrammarPlan, com_subhuti_parser_SubhutiStaticGrammarPlan as SubhutiStaticGrammarPlan, com_subhuti_parser_SubhutiStaticGrammarPlan$Kind, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionAvailability, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionAvailability as DecisionAvailability, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDispatchBlocker, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDispatchBlocker as StaticDispatchBlocker, com_subhuti_parser_SubhutiStaticGrammarPlan$DynamicCandidateReason, com_subhuti_parser_SubhutiStaticGrammarPlan$DynamicCandidateReason as DynamicCandidateReason, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadKind, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadKind as CompiledLookaheadKind, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkNodeDefinition as CompiledLlkNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLlkEdgeDefinition as CompiledLlkEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CompiledLookaheadDefinition as CompiledLookaheadDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$BranchDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$BranchDefinition as BranchDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixDefinition as SharedPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupDefinition as CandidateGroupDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleTokenDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleTokenDefinition as CrossRuleTokenDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleFrontierDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleFrontierDefinition as CrossRuleFrontierDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphNodeDefinition as AdaptiveGraphNodeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AdaptiveGraphDefinition as AdaptiveGraphDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionKind, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionKind as FiniteInstructionKind, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteTokenClassDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteTokenClassDefinition as FiniteTokenClassDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteMatchEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteMatchEdgeDefinition as FiniteMatchEdgeDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteRuleReferenceDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteRuleReferenceDefinition as FiniteRuleReferenceDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteFrameDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteFrameDefinition as FiniteFrameDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteGateDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteGateDefinition as FiniteGateDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteCallPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteCallPrefixDefinition as FiniteCallPrefixDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteInstructionDefinition as FiniteInstructionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteDecisionProgramDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteDecisionProgramDefinition as FiniteDecisionProgramDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteProgramAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$FiniteProgramAddress as FiniteProgramAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$SharedPrefixAddress as SharedPrefixAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CandidateGroupAddress as CandidateGroupAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$CrossRuleAddress as CrossRuleAddress, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$DecisionDefinition as DecisionDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AnalysisDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$AnalysisDefinition as AnalysisDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$InvocationDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$InvocationDefinition as InvocationDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CoverageDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$CoverageDefinition as CoverageDefinition, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticMetadata, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticMetadata as StaticMetadata, com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence, com_subhuti_parser_SubhutiStaticGrammarPlan$Occurrence as Occurrence, com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant, com_subhuti_parser_SubhutiStaticGrammarPlan$RuleVariant as RuleVariant, com_subhuti_parser_SubhutiStaticGrammarPlan$VariantRecursionAnalysis, com_subhuti_parser_SubhutiStaticGrammarPlan$VariantRecursionAnalysis as VariantRecursionAnalysis, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlan as StaticRuleInvocationPlan, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlans, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticRuleInvocationPlans as StaticRuleInvocationPlans, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDecisionPlans, com_subhuti_parser_SubhutiStaticGrammarPlan$StaticDecisionPlans as StaticDecisionPlans } from "./SubhutiStaticGrammarPlan.ts";
import { com_subhuti_parser_SubhutiDecisionPlan, com_subhuti_parser_SubhutiDecisionPlan as SubhutiDecisionPlan, com_subhuti_parser_SubhutiDecisionPlan$Availability, com_subhuti_parser_SubhutiDecisionPlan$CandidateGroup, com_subhuti_parser_SubhutiDecisionPlan$Branch } from "./SubhutiDecisionPlan.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __QinJavaUtilArrays, __QinJavaUtilObjects } from "@qin/java-sdk-js";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const IllegalStateException = __QinJavaLangIllegalStateException;
const IllegalArgumentException = __QinJavaLangIllegalArgumentException;
class com_subhuti_parser_SubhutiStaticExecutionPlan {
  __qin_field_grammarPlan: com_subhuti_parser_SubhutiStaticGrammarPlan | null = null as any;
  __qin_field_decisionsById: com_subhuti_parser_SubhutiDecisionPlan[] | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_subhuti_parser_SubhutiStaticGrammarPlan)) && true) {
      const grammarPlan: any = __qin_args[0];
      const decisionsById: any = __qin_args[1];
      this.__qin_constructor_com_subhuti_parser_SubhutiStaticExecutionPlan_2_0(grammarPlan, decisionsById);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiStaticExecutionPlan/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiStaticExecutionPlan_2_0(grammarPlan: com_subhuti_parser_SubhutiStaticGrammarPlan, decisionsById: com_subhuti_parser_SubhutiDecisionPlan[]): void {
    this.__qin_field_grammarPlan = null;
    this.__qin_field_decisionsById = null;
    this.__qin_field_grammarPlan = grammarPlan;
    this.__qin_field_decisionsById = decisionsById;
  }
  static requireVocabulary(grammarPlan: com_subhuti_parser_SubhutiStaticGrammarPlan, lexer: com_subhuti_lexer_SubhutiLexer): any {
    if ((__qin_binary__("==", grammarPlan, null) || !grammarPlan.hasGeneratedMetadata())) {
      throw new __QinJavaLangIllegalStateException("generated static execution requires generated grammar metadata");
    }
    let runtimeFingerprint: any = lexer.tokenVocabularyFingerprint();
    let generatedFingerprint: any = grammarPlan.tokenVocabularyFingerprint();
    if ((!__QinJavaLangString.equals(generatedFingerprint, runtimeFingerprint))) {
      throw new __QinJavaLangIllegalStateException(("generated parser token vocabulary mismatch: metadata=" + generatedFingerprint + ", runtime=" + runtimeFingerprint));
    }
    return null;
  }
  occurrence(ruleId: number, variantId: number, occurrenceId: number): any {
    return this.__qin_field_grammarPlan.occurrence(ruleId, variantId, occurrenceId);
  }
  decision(...__qin_args: any[]): any {
    if (__qin_args.length === 4 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number" && typeof __qin_args[2] === "number" && (__qin_args[3] === null || __qin_instanceof__(__qin_args[3], com_subhuti_parser_SubhutiStaticGrammarPlan$Kind))) return this.__qin_overload_decision_4_0(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    if (__qin_args.length === 2 && typeof __qin_args[0] === "number" && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_subhuti_parser_SubhutiStaticGrammarPlan$Kind))) return this.__qin_overload_decision_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: decision/" + __qin_args.length);
  }
  __qin_overload_decision_4_0(ruleId: number, variantId: number, occurrenceId: number, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): any {
    let decision: any = this.__qin_field_grammarPlan.occurrenceDecisionPlan(ruleId, variantId, occurrenceId);
    if ((__qin_binary__("==", decision, null) || __qin_binary__("!=", decision.kind(), expectedKind))) {
      throw new __QinJavaLangIllegalStateException(("static decision kind mismatch: expected " + expectedKind + " but found " + (__qin_binary__("==", decision, null) ? "missing" : decision.kind())));
    }
    return decision;
  }
  __qin_overload_decision_2_1(decisionId: number, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): any {
    if ((!this.__qin_field_grammarPlan.hasGeneratedMetadata())) {
      throw new __QinJavaLangIllegalStateException("dense linked decisions require generated grammar metadata");
    }
    return this.decisionPlan(decisionId, expectedKind);
  }
  invocation(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && typeof __qin_args[0] === "number" && typeof __qin_args[1] === "number") return this.__qin_overload_invocation_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_invocation_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: invocation/" + __qin_args.length);
  }
  __qin_overload_invocation_2_0(ruleId: number, variantId: number): any {
    return this.__qin_field_grammarPlan.staticRuleInvocationPlan(ruleId, variantId);
  }
  __qin_overload_invocation_1_1(invocationId: number): any {
    return this.__qin_field_grammarPlan.staticRuleInvocationPlan(invocationId);
  }
  decisionPlan(decisionId: number, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): any {
    if ((__qin_binary__("<", decisionId, 0.0) || __qin_binary__(">=", decisionId, this.__qin_field_decisionsById.length))) {
      throw new __QinJavaLangIllegalArgumentException(("unknown static decision id: " + decisionId));
    }
    return com_subhuti_parser_SubhutiStaticExecutionPlan.requireKind(this.__qin_field_decisionsById[decisionId], expectedKind);
  }
  static requireKind(plan: com_subhuti_parser_SubhutiDecisionPlan, expectedKind: com_subhuti_parser_SubhutiStaticGrammarPlan$Kind): any {
    if ((__qin_binary__("==", plan, null) || __qin_binary__("!=", plan.kind(), expectedKind))) {
      throw new __QinJavaLangIllegalStateException(("static decision kind mismatch: expected " + expectedKind + " but found " + (__qin_binary__("==", plan, null) ? "missing" : plan.kind())));
    }
    return plan;
  }
  static build(grammarPlan: com_subhuti_parser_SubhutiStaticGrammarPlan): any {
    let plansById: any = [];
    for (let decisionId: any = 0.0; __qin_binary__("<", decisionId, plansById.length); decisionId++) {
      let decision: any = grammarPlan.decisionPlan(decisionId);
      if ((decision.planned() && !decision.hasCompiledLookahead())) {
        throw new __QinJavaLangIllegalStateException(("generated lookahead is missing for decision " + decisionId));
      }
      plansById[decisionId] = decision;
    }
    if (__QinJavaUtilArrays.stream(plansById).anyMatch(((...__qin_args) => { const __qin_method = __QinJavaUtilObjects.isNull; if (typeof __qin_method === "function") { return __qin_method.apply(__QinJavaUtilObjects, __qin_args); } const __qin_receiver = __qin_args[0]; return __qin_receiver.isNull(...__qin_args.slice(1)); }))) {
      throw new __QinJavaLangIllegalStateException("static decision ids must be dense");
    }
    return new com_subhuti_parser_SubhutiStaticExecutionPlan(grammarPlan, plansById);
  }
}
const SubhutiStaticExecutionPlan = com_subhuti_parser_SubhutiStaticExecutionPlan;

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_subhuti_parser_SubhutiStaticExecutionPlan };
