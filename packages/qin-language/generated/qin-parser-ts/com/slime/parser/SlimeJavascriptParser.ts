import { com_slime_parser_module_SlimeModuleParser, com_slime_parser_module_SlimeModuleParser as SlimeModuleParser, com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime, com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime as ModuleStaticRuntime } from "./module/SlimeModuleParser.ts";
import { com_subhuti_struct_SubhutiCst, com_subhuti_struct_SubhutiCst as SubhutiCst, com_subhuti_struct_SubhutiCst$Builder } from "../../subhuti/struct/SubhutiCst.ts";
import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "../../subhuti/parser/SubhutiStaticGrammar.ts";
import { com_slime_parser_SlimeJavascriptStaticGrammar, com_slime_parser_SlimeJavascriptStaticGrammar as SlimeJavascriptStaticGrammar } from "./SlimeJavascriptStaticGrammar.ts";
import { com_slime_parser_class__SlimeClassParser, com_slime_parser_class__SlimeClassParser as SlimeClassParser, com_slime_parser_class__SlimeClassParser$ClassStaticRuntime, com_slime_parser_class__SlimeClassParser$ClassStaticRuntime as ClassStaticRuntime } from "./class_/SlimeClassParser.ts";
import { com_slime_parser_function_SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser as SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime, com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime as FunctionStaticRuntime } from "./function/SlimeFunctionParser.ts";
import { com_slime_parser_statements_SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser as SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime as StatementRootStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime as StatementLoopStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime as StatementTryStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime as StatementIfStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime as StatementVariableStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime as StatementListStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime as StatementJumpStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime as StatementBranchStaticRuntime } from "./statements/SlimeStatementParser.ts";
import { com_slime_parser_expressions_SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser as SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime as AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime as AssignmentExpressionStaticRuntime } from "./expressions/SlimeAssignmentExpressionParser.ts";
import { com_slime_parser_expressions_SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser as SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime as BinaryStaticRuntime } from "./expressions/SlimeBinaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser as SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime as UnaryStaticRuntime } from "./expressions/SlimeUnaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser as SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime as PrimaryStaticRuntime } from "./expressions/SlimePrimaryExpressionParser.ts";
import { com_slime_parser_literal_SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser as SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime as LiteralStaticRuntime } from "./literal/SlimeLiteralParser.ts";
import { com_slime_parser_identifier_SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser as SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime as IdentifierStaticRuntime } from "./identifier/SlimeIdentifierParser.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "./base/SlimeJavascriptParserBase.ts";
import { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser as SubhutiParser, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime as StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticPrefix, com_subhuti_parser_SubhutiParser$StaticPrefix as StaticPrefix, com_subhuti_parser_SubhutiParser$StaticChoice, com_subhuti_parser_SubhutiParser$StaticChoice as StaticChoice } from "../../subhuti/parser/SubhutiParser.ts";
import { com_subhuti_parser_SubhutiParserFinal, com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal } from "../../subhuti/parser/SubhutiParserFinal.ts";
import { com_subhuti_parser_SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators as SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl as StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher as StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext as AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext as AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes as PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates as StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame as ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames as CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys as CurrentTokenKeys } from "../../subhuti/parser/SubhutiParserCombinators.ts";
import { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore as SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments as StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult as RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode as StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks as StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$CacheWork, com_subhuti_parser_SubhutiParserCore$CacheWork as CacheWork, com_subhuti_parser_SubhutiParserCore$FailureWork, com_subhuti_parser_SubhutiParserCore$FailureWork as FailureWork } from "../../subhuti/parser/SubhutiParserCore.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations as ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException as SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException as SubhutiFirstTokenUnknownException } from "../../subhuti/parser/SubhutiParserState.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../../subhuti/lookahead/SubhutiTokenLookahead.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangEnum, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __qin_java_functional, __qin_init_enum_value } from "@qin/java-sdk-js";
import { __qin_subhuti_rule_cache_key } from "@qin/java-sdk-js/tooling";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const java_lang_Enum = __QinJavaLangEnum;
const UnsupportedOperationException = __QinJavaLangUnsupportedOperationException;
class com_slime_parser_SlimeJavascriptParser extends com_slime_parser_module_SlimeModuleParser {
  static __qin_field_STATIC_JAVASCRIPT_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_SlimeJavascriptParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeJavascriptParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_SlimeJavascriptParser_1_0(sourceCode: string): void {
    null;
  }
  parseProgramInternal(sourceType: com_slime_parser_SlimeJavascriptParser$SourceType): any {
    if (__qin_binary__("==", sourceType, com_slime_parser_SlimeJavascriptParser$SourceType.__qin_field_MODULE)) {
      this.executeStaticRule(com_slime_parser_SlimeJavascriptParser.__qin_field_STATIC_JAVASCRIPT_GRAMMAR, "OptionalModuleBody", this.javascriptStaticRuntime());
    } else {
      this.executeStaticRule(com_slime_parser_SlimeJavascriptParser.__qin_field_STATIC_JAVASCRIPT_GRAMMAR, "OptionalScriptBody", this.javascriptStaticRuntime());
    }
    return this.getCurCst();
  }
  Program(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_SlimeJavascriptParser$SourceType))) return this.__qin_overload_Program_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_Program_0_1();
    throw new Error("Unsupported Java overload: Program/" + __qin_args.length);
  }
  __qin_overload_Program_1_0(sourceType: com_slime_parser_SlimeJavascriptParser$SourceType): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_Program_1_0(sourceType);
    }), "Program", "SlimeJavascriptParser", __qin_subhuti_rule_cache_key([sourceType]));
  }
  __qin_subhuti_raw___qin_overload_Program_1_0(sourceType: com_slime_parser_SlimeJavascriptParser$SourceType): any {
    return this.parseProgramInternal(sourceType);
  }
  __qin_overload_Program_0_1(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_Program_0_1();
    }), "Program", "SlimeJavascriptParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_Program_0_1(): any {
    return this.parseProgramInternal(com_slime_parser_SlimeJavascriptParser$SourceType.__qin_field_MODULE);
  }
  parse(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_parse();
    }), "parse", "SlimeJavascriptParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_parse(): any {
    return this.parseProgramInternal(com_slime_parser_SlimeJavascriptParser$SourceType.__qin_field_MODULE);
  }
  Script(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Script();
    }), "Script", "SlimeJavascriptParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_Script(): any {
    this.executeStaticRule(com_slime_parser_SlimeJavascriptParser.__qin_field_STATIC_JAVASCRIPT_GRAMMAR, "Script", this.javascriptStaticRuntime());
    return null;
  }
  ScriptBody(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ScriptBody();
    }), "ScriptBody", "SlimeJavascriptParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ScriptBody(): any {
    this.StatementList(com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT);
    return null;
  }
  javascriptStaticRuntime(): any {
    return new com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime(this);
  }
}
const SlimeJavascriptParser = com_slime_parser_SlimeJavascriptParser;
class com_slime_parser_SlimeJavascriptParser$SourceType extends java_lang_Enum {
  static __qin_field_SCRIPT: com_slime_parser_SlimeJavascriptParser$SourceType | null = null as any;
  static __qin_field_MODULE: com_slime_parser_SlimeJavascriptParser$SourceType | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length !== 0) {
      throw new Error("Unsupported Java constructor arity: SlimeJavascriptParser$SourceType/" + __qin_args.length);
    }
    super();
  }
}
const SlimeJavascriptParser$SourceType = com_slime_parser_SlimeJavascriptParser$SourceType;
class com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime {
  __qin_field_parser: com_slime_parser_SlimeJavascriptParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_SlimeJavascriptParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeJavascriptParser$JavascriptStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime_1_0(parser: com_slime_parser_SlimeJavascriptParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("==", variantKey, null)) {
      if ((__QinJavaLangString.equals("ModuleBody", ruleName) || __QinJavaLangString.equals("ScriptBody", ruleName))) {
        return __qin_binary__("!=", this.__qin_field_parser.LA(lookaheadOffset), null);
      }
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported javascript static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("==", variantKey, null)) {
      if (__QinJavaLangString.equals("ModuleBody", ruleName)) {
        this.__qin_field_parser.ModuleBody();
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ScriptBody", ruleName)) {
        this.__qin_field_parser.ScriptBody();
        return (!this.__qin_field_parser.isParserFail());
      }
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported javascript static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeJavascriptParser$JavascriptStaticRuntime = com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime;
com_slime_parser_SlimeJavascriptParser.__qin_field_STATIC_JAVASCRIPT_GRAMMAR = com_slime_parser_SlimeJavascriptStaticGrammar.grammar();
com_slime_parser_SlimeJavascriptParser$SourceType.__qin_field_SCRIPT = __qin_init_enum_value(new com_slime_parser_SlimeJavascriptParser$SourceType(), "SCRIPT", 0);
com_slime_parser_SlimeJavascriptParser$SourceType.__qin_field_MODULE = __qin_init_enum_value(new com_slime_parser_SlimeJavascriptParser$SourceType(), "MODULE", 1);

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser$SourceType, com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime };
