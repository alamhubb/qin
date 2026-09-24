import { com_subhuti_lookahead_SubhutiTokenConsumer, com_subhuti_lookahead_SubhutiTokenConsumer as SubhutiTokenConsumer } from "../lookahead/SubhutiTokenConsumer.ts";
import { com_subhuti_struct_SubhutiCreateToken, com_subhuti_struct_SubhutiCreateToken as SubhutiCreateToken, com_subhuti_struct_SubhutiCreateToken$Builder } from "../struct/SubhutiCreateToken.ts";
import { com_subhuti_struct_SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken as SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken$Builder } from "../struct/SubhutiMatchToken.ts";
import { com_subhuti_parser_SubhutiParserFinal, com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal } from "./SubhutiParserFinal.ts";
import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "./SubhutiStaticGrammar.ts";
import { com_subhuti_parser_SubhutiBackData, com_subhuti_parser_SubhutiBackData as SubhutiBackData } from "./SubhutiBackData.ts";
import { com_subhuti_struct_LexerMode, com_subhuti_struct_LexerMode as LexerMode } from "../struct/LexerMode.ts";
import { com_subhuti_parser_SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators as SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl as StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher as StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext as AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext as AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes as PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates as StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame as ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames as CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys as CurrentTokenKeys } from "./SubhutiParserCombinators.ts";
import { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore as SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments as StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult as RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode as StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks as StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$CacheWork, com_subhuti_parser_SubhutiParserCore$CacheWork as CacheWork, com_subhuti_parser_SubhutiParserCore$FailureWork, com_subhuti_parser_SubhutiParserCore$FailureWork as FailureWork } from "./SubhutiParserCore.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations as ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException as SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException as SubhutiFirstTokenUnknownException } from "./SubhutiParserState.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../lookahead/SubhutiTokenLookahead.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangSystem, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __QinJavaLangStringBuilder, __qin_java_string_hash_code__, __qin_java_identity_hash_code__, __qin_java_value_hash_code__, __qin_java_values_equal__, __qin_java_hash_key__, __qin_java_hash_key_equals__, __QinJavaUtilArrayList, __QinJavaUtilUnmodifiableList, __qin_java_implements, __qin_java_functional, __QinJavaUtilArrays, __qin_collection_size__, __qin_collection_get__, __qin_collection_is_empty__, __qin_collection_to_array__, __QinJavaUtilList, __QinJavaUtilObjects } from "@qin/java-sdk-js";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const System = __QinJavaLangSystem;
const UnsupportedOperationException = __QinJavaLangUnsupportedOperationException;
const IllegalArgumentException = __QinJavaLangIllegalArgumentException;
const ArrayList = __QinJavaUtilArrayList;
const IllegalStateException = __QinJavaLangIllegalStateException;
const StringBuilder = __QinJavaLangStringBuilder;
class com_subhuti_parser_SubhutiParser extends com_subhuti_parser_SubhutiParserFinal {
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] === "function" || (typeof __qin_args[1] === "object" && typeof __qin_args[1].getName === "function" && typeof __qin_args[1].isAssignableFrom === "function")) && (__qin_args[2] === null || Array.isArray(__qin_args[2]) || __qin_instanceof__(__qin_args[2], __QinJavaUtilArrayList) || __qin_instanceof__(__qin_args[2], __QinJavaUtilUnmodifiableList))) {
      const sourceCode: any = __qin_args[0];
      const tokenConsumerClass: any = __qin_args[1];
      const tokens: any = __qin_args[2];
      super(sourceCode, tokenConsumerClass, tokens);
      this.__qin_constructor_com_subhuti_parser_SubhutiParser_3_0(sourceCode, tokenConsumerClass, tokens);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParser/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParser_3_0(sourceCode: string, tokenConsumerClass: __QinJavaLangClass, tokens: __QinJavaUtilList<com_subhuti_struct_SubhutiCreateToken>): void {
    let runtimeClassName: string = this.getClass().getName();
    let enhancedClass: boolean = __QinJavaLangString.endsWith(runtimeClassName, "StaticEnhanced");
    if ((!enhancedClass)) {
      __QinJavaLangSystem.err.println(("Warning: " + this.getClass().getSimpleName() + " is not enhanced. Use a generated/static enhanced parser " + "to enable @SubhutiRule packrat wrappers."));
    }
  }
  static create(parserClass: __QinJavaLangClass, ...args: any[]): any {
    throw new __QinJavaLangUnsupportedOperationException(("SubhutiParser.create(...) was the removed historical parser factory entry. " + "Instantiate the grammar's generated/static enhanced parser class instead of " + parserClass.getName() + "."));
  }
  executeStaticRule(...__qin_args: any[]): void {
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_subhuti_parser_SubhutiStaticGrammar)) && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || __qin_java_implements(__qin_args[2], "com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"))) return this.__qin_overload_executeStaticRule_3_0(__qin_args[0], __qin_args[1], __qin_args[2]);
    if (__qin_args.length === 4 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_subhuti_parser_SubhutiStaticGrammar)) && (__qin_args[1] === null || typeof __qin_args[1] === "string") && (__qin_args[2] === null || typeof __qin_args[2] !== "undefined") && (__qin_args[3] === null || __qin_java_implements(__qin_args[3], "com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"))) return this.__qin_overload_executeStaticRule_4_1(__qin_args[0], __qin_args[1], __qin_args[2], __qin_args[3]);
    throw new Error("Unsupported Java overload: executeStaticRule/" + __qin_args.length);
  }
  __qin_overload_executeStaticRule_3_0(grammar: com_subhuti_parser_SubhutiStaticGrammar, ruleName: string, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): void {
    {
      const __qin_typed_receiver_351: com_subhuti_parser_SubhutiParser = this;
      __qin_typed_receiver_351.executeStaticRule(grammar, ruleName, null, runtime);
    }
    return null;
  }
  __qin_overload_executeStaticRule_4_1(grammar: com_subhuti_parser_SubhutiStaticGrammar, ruleName: string, variantKey: any, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): void {
    if (__qin_binary__("==", grammar, null)) {
      throw new __QinJavaLangIllegalArgumentException("grammar cannot be null");
    }
    if ((__qin_binary__("==", ruleName, null) || __QinJavaLangString.isBlank(ruleName))) {
      throw new __QinJavaLangIllegalArgumentException("ruleName cannot be blank");
    }
    if (__qin_binary__("==", runtime, null)) {
      throw new __QinJavaLangIllegalArgumentException("runtime cannot be null");
    }
    let rule: com_subhuti_parser_SubhutiStaticGrammar$RuleDef = (__qin_binary__("==", variantKey, null) ? grammar.rule(ruleName) : grammar.variants().get(new com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey(ruleName, variantKey)));
    if (__qin_binary__("==", rule, null)) {
      throw new __QinJavaLangIllegalArgumentException((__qin_binary__("==", variantKey, null) ? ("unknown static rule: " + ruleName) : ("unknown static rule variant: " + ruleName + "@" + variantKey)));
    }
    {
      const __qin_typed_receiver_352: com_subhuti_parser_SubhutiParser = this;
      __qin_typed_receiver_352.enterInterpretedStaticRule();
    }
    try {
      if ((!this.executeStaticNode(rule.body(), runtime))) {
        {
          const __qin_typed_receiver_353: com_subhuti_parser_SubhutiParser = this;
          __qin_typed_receiver_353.setParseFail();
        }
      }
    } catch (e) {
      if (!(e instanceof __QinJavaLangUnsupportedOperationException)) {
        throw e;
      }
      throw this.staticExecutionException(ruleName, variantKey, e);
    } finally {
      {
        const __qin_typed_receiver_354: com_subhuti_parser_SubhutiParser = this;
        __qin_typed_receiver_354.leaveInterpretedStaticRule();
      }
    }
    return null;
  }
  executeStaticTolerantManyCall(ruleName: string, variantKey: any, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, ...stopTokens: com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher[]): void {
    if ((__qin_binary__("==", ruleName, null) || __QinJavaLangString.isBlank(ruleName))) {
      throw new __QinJavaLangIllegalArgumentException("ruleName cannot be blank");
    }
    if (__qin_binary__("==", runtime, null)) {
      throw new __QinJavaLangIllegalArgumentException("runtime cannot be null");
    }
    {
      const __qin_typed_receiver_355: com_subhuti_parser_SubhutiParser = this;
      __qin_typed_receiver_355.ManyTolerant(__qin_java_functional(() => {
      if (runtime.callStaticRule(ruleName, variantKey)) {
        {
          const __qin_typed_receiver_356: com_subhuti_parser_SubhutiParser = this;
          __qin_typed_receiver_356.setParseSuccess();
        }
      } else {
        {
          const __qin_typed_receiver_357: com_subhuti_parser_SubhutiParser = this;
          __qin_typed_receiver_357.setParseFail();
        }
      }
      return null;
    }), ...stopTokens);
    }
    return null;
  }
  executeStaticNode(node: com_subhuti_parser_SubhutiStaticGrammar$Node, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): boolean {
    if (this.isParserFail()) {
      return false;
    }
    return (() => {
      const __switch_discriminant = node.kind();
      if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_ALTERNATION) {
        return this.executeStaticAlternation(node, runtime);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_ALTERNATIVE || __switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_SEQUENCE) {
        return this.executeStaticSequence(node.children(), runtime);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_CONSUME) {
        return this.executeStaticConsume(node);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_GATE) {
        return this.executeStaticGate(node, runtime);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_ACTION) {
        return runtime.runStaticAction(node.name());
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_CALL) {
        return runtime.callStaticRule(node.name(), node.ruleVariantKey());
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_OPTION) {
        return this.executeStaticOption(node, runtime);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_MANY) {
        return this.executeStaticMany(node, runtime);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_AT_LEAST_ONE) {
        return this.executeStaticAtLeastOne(node, runtime);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_DYNAMIC_CALLBACK) {
        throw new __QinJavaLangUnsupportedOperationException("static runtime execution does not support dynamic callbacks");
      }
      return null;
    })();
  }
  executeStaticAlternation(node: com_subhuti_parser_SubhutiStaticGrammar$Node, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): boolean {
    return this.executeStaticOrderedChoiceSequences(__QinJavaUtilArrays.stream(node.children()).map((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=staticSequence receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.staticSequence.bind(__qin_bound_receiver); })()).toList(), runtime);
  }
  executeStaticOrderedChoices(choices: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node>, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): boolean {
    return this.executeStaticOrderedChoiceSequences(__QinJavaUtilArrays.stream(choices).map((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=staticSequence receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.staticSequence.bind(__qin_bound_receiver); })()).toList(), runtime);
  }
  executeStaticOrderedChoiceSequences(choices: __QinJavaUtilList<__QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node>>, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): boolean {
    let productive: __QinJavaUtilList<com_subhuti_parser_SubhutiParser$StaticChoice> = new __QinJavaUtilArrayList();
    let firstEmpty: com_subhuti_parser_SubhutiParser$StaticChoice = null;
    for (let index: number = 0.0; __qin_binary__("<", index, __qin_collection_size__(choices)); index++) {
      let sequence: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node> = __qin_collection_get__(choices, index);
      let prefix: com_subhuti_parser_SubhutiParser$StaticPrefix = this.firstStaticPrefix(sequence);
      if (__qin_binary__("==", prefix, null)) {
        if (__qin_binary__("==", firstEmpty, null)) {
          firstEmpty = new com_subhuti_parser_SubhutiParser$StaticChoice(index, sequence, null);
        }
        continue;
      }
      if (this.staticSequenceMatches(sequence, runtime)) {
        productive.add(new com_subhuti_parser_SubhutiParser$StaticChoice(index, sequence, prefix));
      }
    }
    if (__qin_collection_is_empty__(productive)) {
      if (__qin_binary__("!=", firstEmpty, null)) {
        return this.executeStaticSequence(firstEmpty.sequence(), runtime);
      }
      {
        const __qin_typed_receiver_358: com_subhuti_parser_SubhutiParser = this;
        __qin_typed_receiver_358.setParseFail();
      }
      return false;
    }
    if (__qin_binary__("==", __qin_collection_size__(productive), 1.0)) {
      return this.executeStaticSequence(__qin_collection_get__(productive, 0.0).sequence(), runtime);
    }
    let commonPrefix: com_subhuti_parser_SubhutiStaticGrammar$Node = __qin_collection_get__(productive, 0.0).prefix().node();
    for (const choice of productive) {
      if ((!this.sameStaticPrefix(commonPrefix, choice.prefix().node()))) {
        throw new __QinJavaLangUnsupportedOperationException(("ambiguous static alternatives require DecisionProgram before runtime execution: " + "productiveChoices=" + this.describeStaticChoices(productive) + ", lookahead=" + this.describeStaticLookahead()));
      }
    }
    if ((!this.executeStaticNode(commonPrefix, runtime))) {
      return false;
    }
    let residuals: __QinJavaUtilList<__QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node>> = new __QinJavaUtilArrayList(__qin_collection_size__(productive));
    let hasEmptyResidual: boolean = false;
    for (const choice of productive) {
      let residual: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node> = new __QinJavaUtilArrayList(__qin_collection_to_array__(choice.sequence()).slice(Number(__qin_binary__("+", choice.prefix().sequenceIndex(), 1.0)), Number(__qin_collection_size__(choice.sequence()))));
      if (__qin_collection_is_empty__(residual)) {
        hasEmptyResidual = true;
      } else {
        residuals.add(residual);
      }
    }
    if (__qin_collection_is_empty__(residuals)) {
      return true;
    }
    let savedState: com_subhuti_parser_SubhutiBackData = this.saveState();
    if (this.executeStaticOrderedChoiceSequences(residuals, runtime)) {
      return true;
    }
    {
      const __qin_typed_receiver_359: com_subhuti_parser_SubhutiParser = this;
      __qin_typed_receiver_359.restoreState(savedState);
    }
    if (hasEmptyResidual) {
      {
        const __qin_typed_receiver_360: com_subhuti_parser_SubhutiParser = this;
        __qin_typed_receiver_360.setParseSuccess();
      }
      return true;
    }
    {
      const __qin_typed_receiver_361: com_subhuti_parser_SubhutiParser = this;
      __qin_typed_receiver_361.setParseFail();
    }
    return false;
  }
  executeStaticSequence(nodes: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node>, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): boolean {
    for (const child of nodes) {
      if ((!this.executeStaticNode(child, runtime))) {
        return false;
      }
    }
    {
      const __qin_typed_receiver_362: com_subhuti_parser_SubhutiParser = this;
      __qin_typed_receiver_362.setParseSuccess();
    }
    return true;
  }
  executeStaticConsume(node: com_subhuti_parser_SubhutiStaticGrammar$Node): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.staticLookaheadToken(node, 1.0);
    if ((__qin_binary__("==", token, null) || !__QinJavaLangString.equals(node.name(), token.tokenName()))) {
      {
        const __qin_typed_receiver_363: com_subhuti_parser_SubhutiParser = this;
        __qin_typed_receiver_363.setParseFail();
      }
      return false;
    }
    if ((__qin_binary__("!=", node.tokenValue(), null) && !__QinJavaLangString.equals(node.tokenValue(), token.value()))) {
      {
        const __qin_typed_receiver_364: com_subhuti_parser_SubhutiParser = this;
        __qin_typed_receiver_364.setParseFail();
      }
      return false;
    }
    {
      const __qin_typed_receiver_365: com_subhuti_parser_SubhutiParser = this;
      __qin_typed_receiver_365._consumeToken(node.name(), node.lexerMode());
    }
    return (!this.isParserFail());
  }
  executeStaticOption(node: com_subhuti_parser_SubhutiStaticGrammar$Node, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): boolean {
    let savedState: com_subhuti_parser_SubhutiBackData = this.saveState();
    let startCursor: number = this.cursorStamp();
    if (this.executeStaticSequence(node.children(), runtime)) {
      if (__qin_binary__("!=", this.cursorStamp(), startCursor)) {
        return true;
      }
      {
        const __qin_typed_receiver_366: com_subhuti_parser_SubhutiParser = this;
        __qin_typed_receiver_366.restoreState(savedState);
      }
      {
        const __qin_typed_receiver_367: com_subhuti_parser_SubhutiParser = this;
        __qin_typed_receiver_367.setParseSuccess();
      }
      return true;
    }
    if (__qin_binary__("!=", this.cursorStamp(), startCursor)) {
      return false;
    }
    {
      const __qin_typed_receiver_368: com_subhuti_parser_SubhutiParser = this;
      __qin_typed_receiver_368.restoreState(savedState);
    }
    {
      const __qin_typed_receiver_369: com_subhuti_parser_SubhutiParser = this;
      __qin_typed_receiver_369.setParseSuccess();
    }
    return true;
  }
  executeStaticMany(node: com_subhuti_parser_SubhutiStaticGrammar$Node, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): boolean {
    if (__qin_binary__("==", this.firstStaticPrefix(node.children()), null)) {
      throw new __QinJavaLangUnsupportedOperationException("static MANY has no consuming prefix");
    }
    while (this.staticSequenceMatches(node.children(), runtime)) {
      let startCursor: number = this.cursorStamp();
      if ((!this.executeStaticSequence(node.children(), runtime))) {
        return false;
      }
      if (__qin_binary__("==", this.cursorStamp(), startCursor)) {
        throw new __QinJavaLangIllegalStateException("static MANY body consumed no input");
      }
    }
    {
      const __qin_typed_receiver_370: com_subhuti_parser_SubhutiParser = this;
      __qin_typed_receiver_370.setParseSuccess();
    }
    return true;
  }
  executeStaticAtLeastOne(node: com_subhuti_parser_SubhutiStaticGrammar$Node, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): boolean {
    if (__qin_binary__("==", this.firstStaticPrefix(node.children()), null)) {
      throw new __QinJavaLangUnsupportedOperationException("static AT_LEAST_ONE has no consuming prefix");
    }
    if ((!this.staticSequenceMatches(node.children(), runtime))) {
      {
        const __qin_typed_receiver_371: com_subhuti_parser_SubhutiParser = this;
        __qin_typed_receiver_371.setParseFail();
      }
      return false;
    }
    let startCursor: number = this.cursorStamp();
    if ((!this.executeStaticSequence(node.children(), runtime))) {
      return false;
    }
    if (__qin_binary__("==", this.cursorStamp(), startCursor)) {
      throw new __QinJavaLangIllegalStateException("static AT_LEAST_ONE body consumed no input");
    }
    return this.executeStaticMany(node, runtime);
  }
  executeStaticGate(node: com_subhuti_parser_SubhutiStaticGrammar$Node, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): boolean {
    if ((!runtime.testStaticGate(node.name()))) {
      {
        const __qin_typed_receiver_372: com_subhuti_parser_SubhutiParser = this;
        __qin_typed_receiver_372.setParseFail();
      }
      return false;
    }
    return (__qin_collection_is_empty__(node.children()) || this.executeStaticSequence(node.children(), runtime));
  }
  staticAlternativeMatches(node: com_subhuti_parser_SubhutiStaticGrammar$Node, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): boolean {
    return (() => {
      const __switch_discriminant = node.kind();
      if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_ALTERNATIVE || __switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_SEQUENCE) {
        return this.staticSequenceMatches(node.children(), runtime);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_CONSUME) {
        return this.staticConsumeMatches(node);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_GATE) {
        return (runtime.testStaticGate(node.name()) && (__qin_collection_is_empty__(node.children()) || this.staticSequenceMatches(node.children(), runtime)));
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_ACTION) {
        return true;
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_CALL) {
        return runtime.canStartStaticRule(node.name(), node.ruleVariantKey(), 1.0);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_OPTION || __switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_MANY) {
        return true;
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_AT_LEAST_ONE) {
        return this.staticSequenceMatches(node.children(), runtime);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_ALTERNATION) {
        return this.staticAlternationMatches(node.children(), runtime);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_DYNAMIC_CALLBACK) {
        throw new __QinJavaLangUnsupportedOperationException("static alternative matching does not support dynamic callbacks");
      }
      return null;
    })();
  }
  staticAlternationMatches(choices: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node>, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): boolean {
    for (const choice of choices) {
      if (this.staticAlternativeMatches(choice, runtime)) {
        return true;
      }
    }
    return false;
  }
  staticPrefixMatches(node: com_subhuti_parser_SubhutiStaticGrammar$Node, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): boolean {
    return (() => {
      const __switch_discriminant = node.kind();
      if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_CONSUME) {
        return this.staticConsumeMatches(node);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_CALL) {
        return runtime.canStartStaticRule(node.name(), node.ruleVariantKey(), 1.0);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_ALTERNATION) {
        return this.staticAlternationMatches(node.children(), runtime);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_ALTERNATIVE || __switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_SEQUENCE) {
        return this.staticSequenceMatches(node.children(), runtime);
      }
      else if (__switch_discriminant === com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_GATE) {
        return (runtime.testStaticGate(node.name()) && (__qin_collection_is_empty__(node.children()) || this.staticSequenceMatches(node.children(), runtime)));
      }
      else {
        throw new __QinJavaLangUnsupportedOperationException(("shared-prefix static execution supports only CONSUME/CALL/GATE/ALTERNATION/SEQUENCE prefixes, found: " + node.kind()));
      }
      return null;
    })();
  }
  staticSequenceMatches(...__qin_args: any[]): boolean {
    if (__qin_args.length === 2 && (__qin_args[0] === null || Array.isArray(__qin_args[0]) || __qin_instanceof__(__qin_args[0], __QinJavaUtilArrayList) || __qin_instanceof__(__qin_args[0], __QinJavaUtilUnmodifiableList)) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"))) return this.__qin_overload_staticSequenceMatches_2_0((Array.isArray(__qin_args[0]) ? new __QinJavaUtilArrayList(__qin_args[0]) : __qin_args[0]), __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || Array.isArray(__qin_args[0]) || __qin_instanceof__(__qin_args[0], __QinJavaUtilArrayList) || __qin_instanceof__(__qin_args[0], __QinJavaUtilUnmodifiableList)) && (__qin_args[1] === null || __qin_java_implements(__qin_args[1], "com.subhuti.parser.SubhutiParser$StaticGrammarRuntime")) && typeof __qin_args[2] === "number") return this.__qin_overload_staticSequenceMatches_3_1((Array.isArray(__qin_args[0]) ? new __QinJavaUtilArrayList(__qin_args[0]) : __qin_args[0]), __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: staticSequenceMatches/" + __qin_args.length);
  }
  __qin_overload_staticSequenceMatches_2_0(nodes: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node>, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime): boolean {
    return this.staticSequenceMatches(nodes, runtime, 1.0);
  }
  __qin_overload_staticSequenceMatches_3_1(nodes: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node>, runtime: com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, initialLookaheadOffset: number): boolean {
    let lookaheadOffset: number = initialLookaheadOffset;
    for (const child of nodes) {
      if (__qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_ACTION)) {
        continue;
      }
      if (__qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_CONSUME)) {
        if ((!this.staticConsumeMatches(child, lookaheadOffset))) {
          return false;
        }
        lookaheadOffset++;
        continue;
      }
      if (__qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_CALL)) {
        return runtime.canStartStaticRule(child.name(), child.ruleVariantKey(), lookaheadOffset);
      }
      if (__qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_GATE)) {
        if ((!runtime.testStaticGate(child.name()))) {
          return false;
        }
        if (__qin_collection_is_empty__(child.children())) {
          continue;
        }
        return this.staticSequenceMatches(child.children(), runtime, lookaheadOffset);
      }
      return this.staticAlternativeMatches(child, runtime);
    }
    return true;
  }
  staticSequence(node: com_subhuti_parser_SubhutiStaticGrammar$Node): __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node> {
    if ((__qin_binary__("==", node.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_ALTERNATIVE) || __qin_binary__("==", node.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_SEQUENCE))) {
      return node.children();
    }
    return __QinJavaUtilList.of(node);
  }
  firstStaticPrefix(sequence: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node>): com_subhuti_parser_SubhutiParser$StaticPrefix {
    for (let index: number = 0.0; __qin_binary__("<", index, __qin_collection_size__(sequence)); index++) {
      let child: com_subhuti_parser_SubhutiStaticGrammar$Node = __qin_collection_get__(sequence, index);
      if (__qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_ACTION)) {
        continue;
      }
      if ((__qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_CONSUME) || __qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_CALL) || __qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_GATE) || __qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_ALTERNATION))) {
        return new com_subhuti_parser_SubhutiParser$StaticPrefix(index, child);
      }
      if ((__qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_ALTERNATIVE) || __qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_SEQUENCE))) {
        let nested: com_subhuti_parser_SubhutiParser$StaticPrefix = this.firstStaticPrefix(child.children());
        if (__qin_binary__("==", nested, null)) {
          continue;
        }
        return new com_subhuti_parser_SubhutiParser$StaticPrefix(index, child);
      }
      if ((__qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_OPTION) || __qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_MANY))) {
        continue;
      }
      if (__qin_binary__("==", child.kind(), com_subhuti_parser_SubhutiStaticGrammar$NodeKind.__qin_field_AT_LEAST_ONE)) {
        let nested: com_subhuti_parser_SubhutiParser$StaticPrefix = this.firstStaticPrefix(child.children());
        if (__qin_binary__("==", nested, null)) {
          throw new __QinJavaLangUnsupportedOperationException("static AT_LEAST_ONE has no consuming prefix");
        }
        return new com_subhuti_parser_SubhutiParser$StaticPrefix(index, child);
      }
      throw new __QinJavaLangUnsupportedOperationException(("unsupported static prefix node kind: " + child.kind()));
    }
    return null;
  }
  sameStaticPrefix(left: com_subhuti_parser_SubhutiStaticGrammar$Node, right: com_subhuti_parser_SubhutiStaticGrammar$Node): boolean {
    if (__qin_binary__("!=", left.kind(), right.kind())) {
      return false;
    }
    if ((!__QinJavaLangString.equals(left.name(), right.name()))) {
      return false;
    }
    if ((!__QinJavaUtilObjects.equals(left.tokenValue(), right.tokenValue()))) {
      return false;
    }
    if ((!__QinJavaUtilObjects.equals(left.lexerMode(), right.lexerMode()))) {
      return false;
    }
    return __QinJavaUtilObjects.equals(left.ruleVariantKey(), right.ruleVariantKey());
  }
  staticExecutionException(ruleName: string, variantKey: any, cause: __QinJavaLangUnsupportedOperationException): __QinJavaLangUnsupportedOperationException {
    let message: string = cause.getMessage();
    if ((__qin_binary__("!=", message, null) && __QinJavaLangString.startsWith(message, "staticRule="))) {
      return cause;
    }
    return new __QinJavaLangUnsupportedOperationException(("staticRule=" + ruleName + (__qin_binary__("==", variantKey, null) ? "" : ("@" + variantKey)) + ", lookahead=" + this.describeStaticLookahead() + ", reason=" + message), cause);
  }
  describeStaticChoices(choices: __QinJavaUtilList<com_subhuti_parser_SubhutiParser$StaticChoice>): string {
    let builder: __QinJavaLangStringBuilder = new __QinJavaLangStringBuilder("[");
    for (let index: number = 0.0; __qin_binary__("<", index, __qin_collection_size__(choices)); index++) {
      if (__qin_binary__(">", index, 0.0)) {
        builder.append(", ");
      }
      let choice: com_subhuti_parser_SubhutiParser$StaticChoice = __qin_collection_get__(choices, index);
      builder.append("#").append(choice.sourceIndex()).append(" prefix=").append(this.describeStaticNode(choice.prefix().node()));
    }
    return builder.append("]").toString();
  }
  describeStaticNode(node: com_subhuti_parser_SubhutiStaticGrammar$Node): string {
    if (__qin_binary__("==", node, null)) {
      return "<none>";
    }
    let builder: __QinJavaLangStringBuilder = new __QinJavaLangStringBuilder(node.kind().name());
    if ((!__QinJavaLangString.isBlank(node.name()))) {
      builder.append("(").append(node.name());
      if (__qin_binary__("!=", node.ruleVariantKey(), null)) {
        builder.append("@").append(node.ruleVariantKey());
      }
      if (__qin_binary__("!=", node.tokenValue(), null)) {
        builder.append(" value=").append(node.tokenValue());
      }
      builder.append(")");
    }
    return builder.toString();
  }
  describeStaticLookahead(): string {
    return (__qin_binary__("==", this.LA(1.0), null) ? "<none>" : (this.LA(1.0) + " atIndex=" + this.__qin_field_currentIndex));
  }
  staticConsumeMatches(...__qin_args: any[]): boolean {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_subhuti_parser_SubhutiStaticGrammar$Node))) return this.__qin_overload_staticConsumeMatches_1_0(__qin_args[0]);
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_subhuti_parser_SubhutiStaticGrammar$Node)) && typeof __qin_args[1] === "number") return this.__qin_overload_staticConsumeMatches_2_1(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: staticConsumeMatches/" + __qin_args.length);
  }
  __qin_overload_staticConsumeMatches_1_0(node: com_subhuti_parser_SubhutiStaticGrammar$Node): boolean {
    return this.staticConsumeMatches(node, 1.0);
  }
  __qin_overload_staticConsumeMatches_2_1(node: com_subhuti_parser_SubhutiStaticGrammar$Node, lookaheadOffset: number): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.staticLookaheadToken(node, lookaheadOffset);
    if ((__qin_binary__("==", token, null) || !__QinJavaLangString.equals(node.name(), token.tokenName()))) {
      return false;
    }
    return (__qin_binary__("==", node.tokenValue(), null) || __QinJavaLangString.equals(node.tokenValue(), token.value()));
  }
  staticLookaheadToken(node: com_subhuti_parser_SubhutiStaticGrammar$Node, lookaheadOffset: number): com_subhuti_struct_SubhutiMatchToken {
    if ((__qin_binary__("==", node.lexerMode(), null) || com_subhuti_struct_LexerMode.__qin_field_DEFAULT_MODE.equals(node.lexerMode()))) {
      return this.LA(lookaheadOffset);
    }
    return this.LA(lookaheadOffset, __QinJavaUtilList.of(node.lexerMode()));
  }
}
const SubhutiParser = com_subhuti_parser_SubhutiParser;
class com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
  constructor(...__qin_args: any[]) {
    if (__qin_args.length !== 0) {
      throw new Error("Unsupported Java constructor arity: SubhutiParser$StaticGrammarRuntime/" + __qin_args.length);
    }
  }
  testStaticGate(gateId: string): boolean {
    throw new __QinJavaLangUnsupportedOperationException(("unsupported static gate: " + gateId));
  }
  runStaticAction(actionId: string): boolean {
    throw new __QinJavaLangUnsupportedOperationException(("unsupported static action: " + actionId));
  }
  canStartStaticRule(...__qin_args: any[]): boolean {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): boolean {
    throw new __QinJavaLangUnsupportedOperationException(("unsupported static rule call: " + ruleName + "@" + variantKey));
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): boolean {
    if (__qin_binary__("==", lookaheadOffset, 1.0)) {
      return this.canStartStaticRule(ruleName, variantKey);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported static rule lookahead: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    throw new __QinJavaLangUnsupportedOperationException(("unsupported static rule call: " + ruleName + "@" + variantKey));
  }
}
com_subhuti_parser_SubhutiParser$StaticGrammarRuntime.__qin_java_interface = true;
const SubhutiParser$StaticGrammarRuntime = com_subhuti_parser_SubhutiParser$StaticGrammarRuntime;
class com_subhuti_parser_SubhutiParser$StaticPrefix {
  __qin_field_sequenceIndex: number | null = 0 as any;
  __qin_field_node: com_subhuti_parser_SubhutiStaticGrammar$Node | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && typeof __qin_args[0] === "number" && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_subhuti_parser_SubhutiStaticGrammar$Node))) {
      const sequenceIndex: any = __qin_args[0];
      const node: any = __qin_args[1];
      this.__qin_constructor_com_subhuti_parser_SubhutiParser$StaticPrefix_2_0(sequenceIndex, node);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParser$StaticPrefix/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParser$StaticPrefix_2_0(sequenceIndex: number, node: com_subhuti_parser_SubhutiStaticGrammar$Node): void {
    this.__qin_field_sequenceIndex = 0;
    this.__qin_field_node = null;
    (() => {
      this.__qin_field_sequenceIndex = sequenceIndex;
      this.__qin_field_node = node;
      return null;
    })();
  }
  sequenceIndex(): number {
    return this.__qin_field_sequenceIndex;
  }
  node(): com_subhuti_parser_SubhutiStaticGrammar$Node {
    return this.__qin_field_node;
  }
  equals(other: any): boolean {
    if (this === other) return true;
    if (!__qin_instanceof__(other, com_subhuti_parser_SubhutiParser$StaticPrefix)) return false;
    const __qin_record_other: com_subhuti_parser_SubhutiParser$StaticPrefix = other;
    return __qin_java_values_equal__(this.__qin_field_sequenceIndex, __qin_record_other.__qin_field_sequenceIndex)
      && __qin_java_values_equal__(this.__qin_field_node, __qin_record_other.__qin_field_node);
  }
  hashCode(): number {
    let result = 1;
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_sequenceIndex);
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_node);
    return result;
  }
  toString(): string {
    return ["SubhutiParser$StaticPrefix[", "sequenceIndex=", this.__qin_field_sequenceIndex, ", ", "node=", this.__qin_field_node, "]"].join("");
  }
}
const SubhutiParser$StaticPrefix = com_subhuti_parser_SubhutiParser$StaticPrefix;
class com_subhuti_parser_SubhutiParser$StaticChoice {
  __qin_field_sourceIndex: number | null = 0 as any;
  __qin_field_sequence: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node> | null = null as any;
  __qin_field_prefix: com_subhuti_parser_SubhutiParser$StaticPrefix | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 3 && typeof __qin_args[0] === "number" && (__qin_args[1] === null || Array.isArray(__qin_args[1]) || __qin_instanceof__(__qin_args[1], __QinJavaUtilArrayList) || __qin_instanceof__(__qin_args[1], __QinJavaUtilUnmodifiableList)) && (__qin_args[2] === null || __qin_instanceof__(__qin_args[2], com_subhuti_parser_SubhutiParser$StaticPrefix))) {
      const sourceIndex: any = __qin_args[0];
      const sequence: any = __qin_args[1];
      const prefix: any = __qin_args[2];
      this.__qin_constructor_com_subhuti_parser_SubhutiParser$StaticChoice_3_0(sourceIndex, sequence, prefix);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SubhutiParser$StaticChoice/" + __qin_args.length);
  }
  __qin_constructor_com_subhuti_parser_SubhutiParser$StaticChoice_3_0(sourceIndex: number, sequence: __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node>, prefix: com_subhuti_parser_SubhutiParser$StaticPrefix): void {
    this.__qin_field_sourceIndex = 0;
    this.__qin_field_sequence = null;
    this.__qin_field_prefix = null;
    (() => {
      this.__qin_field_sourceIndex = sourceIndex;
      this.__qin_field_sequence = sequence;
      this.__qin_field_prefix = prefix;
      return null;
    })();
  }
  sourceIndex(): number {
    return this.__qin_field_sourceIndex;
  }
  sequence(): __QinJavaUtilList<com_subhuti_parser_SubhutiStaticGrammar$Node> {
    return this.__qin_field_sequence;
  }
  prefix(): com_subhuti_parser_SubhutiParser$StaticPrefix {
    return this.__qin_field_prefix;
  }
  equals(other: any): boolean {
    if (this === other) return true;
    if (!__qin_instanceof__(other, com_subhuti_parser_SubhutiParser$StaticChoice)) return false;
    const __qin_record_other: com_subhuti_parser_SubhutiParser$StaticChoice = other;
    return __qin_java_values_equal__(this.__qin_field_sourceIndex, __qin_record_other.__qin_field_sourceIndex)
      && __qin_java_values_equal__(this.__qin_field_sequence, __qin_record_other.__qin_field_sequence)
      && __qin_java_values_equal__(this.__qin_field_prefix, __qin_record_other.__qin_field_prefix);
  }
  hashCode(): number {
    let result = 1;
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_sourceIndex);
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_sequence);
    result = result * 31 + __qin_java_value_hash_code__(this.__qin_field_prefix);
    return result;
  }
  toString(): string {
    return ["SubhutiParser$StaticChoice[", "sourceIndex=", this.__qin_field_sourceIndex, ", ", "sequence=", this.__qin_field_sequence, ", ", "prefix=", this.__qin_field_prefix, "]"].join("");
  }
}
const SubhutiParser$StaticChoice = com_subhuti_parser_SubhutiParser$StaticChoice;

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticPrefix, com_subhuti_parser_SubhutiParser$StaticChoice };
