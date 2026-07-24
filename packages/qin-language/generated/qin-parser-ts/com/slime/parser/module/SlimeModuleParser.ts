import { com_slime_parser_class__SlimeClassParser, com_slime_parser_class__SlimeClassParser as SlimeClassParser, com_slime_parser_class__SlimeClassParser$ClassStaticRuntime, com_slime_parser_class__SlimeClassParser$ClassStaticRuntime as ClassStaticRuntime } from "../class_/SlimeClassParser.ts";
import { com_subhuti_parser_SubhutiGastGrammar, com_subhuti_parser_SubhutiGastGrammar as SubhutiGastGrammar, com_subhuti_parser_SubhutiGastGrammar$AlternationDefinition, com_subhuti_parser_SubhutiGastGrammar$AlternationDefinition as AlternationDefinition, com_subhuti_parser_SubhutiGastGrammar$RuleVariantKey, com_subhuti_parser_SubhutiGastGrammar$RuleVariantDefinition, com_subhuti_parser_SubhutiGastGrammar$RuleVariantDefinition as RuleVariantDefinition } from "../../../subhuti/parser/SubhutiGastGrammar.ts";
import { com_subhuti_parser_SubhutiGastNode, com_subhuti_parser_SubhutiGastNode as SubhutiGastNode, com_subhuti_parser_SubhutiGastNode$Kind } from "../../../subhuti/parser/SubhutiGastNode.ts";
import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "../../../subhuti/parser/SubhutiStaticGrammar.ts";
import { com_subhuti_struct_LexerMode, com_subhuti_struct_LexerMode as LexerMode } from "../../../subhuti/struct/LexerMode.ts";
import { com_slime_parser_module_SlimeModuleStaticGrammar, com_slime_parser_module_SlimeModuleStaticGrammar as SlimeModuleStaticGrammar } from "./SlimeModuleStaticGrammar.ts";
import { com_slime_parser_function_SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser as SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime, com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime as FunctionStaticRuntime } from "../function/SlimeFunctionParser.ts";
import { com_slime_parser_statements_SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser as SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime as StatementRootStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime as StatementLoopStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime as StatementTryStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime as StatementIfStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime as StatementVariableStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime as StatementListStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime as StatementJumpStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime as StatementBranchStaticRuntime } from "../statements/SlimeStatementParser.ts";
import { com_slime_parser_expressions_SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser as SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime as AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime as AssignmentExpressionStaticRuntime } from "../expressions/SlimeAssignmentExpressionParser.ts";
import { com_slime_parser_expressions_SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser as SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime as BinaryStaticRuntime } from "../expressions/SlimeBinaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser as SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime as UnaryStaticRuntime } from "../expressions/SlimeUnaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser as SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime as PrimaryStaticRuntime } from "../expressions/SlimePrimaryExpressionParser.ts";
import { com_slime_parser_literal_SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser as SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime as LiteralStaticRuntime } from "../literal/SlimeLiteralParser.ts";
import { com_slime_parser_identifier_SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser as SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime as IdentifierStaticRuntime } from "../identifier/SlimeIdentifierParser.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../base/SlimeJavascriptParserBase.ts";
import { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser as SubhutiParser, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime as StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticPrefix, com_subhuti_parser_SubhutiParser$StaticPrefix as StaticPrefix, com_subhuti_parser_SubhutiParser$StaticChoice, com_subhuti_parser_SubhutiParser$StaticChoice as StaticChoice } from "../../../subhuti/parser/SubhutiParser.ts";
import { com_subhuti_parser_SubhutiParserFinal, com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal } from "../../../subhuti/parser/SubhutiParserFinal.ts";
import { com_subhuti_parser_SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators as SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl as StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher as StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext as AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext as AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes as PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates as StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame as ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames as CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys as CurrentTokenKeys } from "../../../subhuti/parser/SubhutiParserCombinators.ts";
import { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore as SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments as StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult as RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode as StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks as StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$CacheWork, com_subhuti_parser_SubhutiParserCore$CacheWork as CacheWork, com_subhuti_parser_SubhutiParserCore$FailureWork, com_subhuti_parser_SubhutiParserCore$FailureWork as FailureWork } from "../../../subhuti/parser/SubhutiParserCore.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations as ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException as SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException as SubhutiFirstTokenUnknownException } from "../../../subhuti/parser/SubhutiParserState.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../../../subhuti/lookahead/SubhutiTokenLookahead.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __QinJavaUtilSet, __QinJavaUtilUnmodifiableSet, __QinJavaUtilList, __QinJavaUtilUnmodifiableList, __QinJavaUtilArrayList, __qin_java_functional } from "@qin/java-sdk-js";
import { __qin_subhuti_rule_cache_key } from "@qin/java-sdk-js/tooling";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const ArrayList = __QinJavaUtilArrayList;
const UnsupportedOperationException = __QinJavaLangUnsupportedOperationException;
class com_slime_parser_module_SlimeModuleParser extends com_slime_parser_class__SlimeClassParser {
  static __qin_field_SLIME_MODULE_GAST_GRAMMAR: com_subhuti_parser_SubhutiGastGrammar | null = null as any;
  static __qin_field_STATIC_MODULE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_module_SlimeModuleParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeModuleParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_module_SlimeModuleParser_1_0(sourceCode: string): void {
    null;
  }
  gastGrammar(): any {
    return com_slime_parser_module_SlimeModuleParser.__qin_field_SLIME_MODULE_GAST_GRAMMAR;
  }
  recognizerLowYieldMemoRules(): any {
    return __QinJavaUtilSet.of("AssignmentOperatorAny", "ArgumentListItem");
  }
  static createGastGrammar(): any {
    let gast: any = new com_subhuti_parser_SubhutiGastGrammar();
    gast.putRule("IdentifierName", com_slime_parser_module_SlimeModuleParser.gastTerminals("IdentifierName", "Await", "Break", "Case", "Catch", "Class", "Const", "Continue", "Debugger", "Default", "Delete", "Do", "Else", "Enum", "Export", "Extends", "False", "Finally", "For", "Function", "If", "Import", "In", "Instanceof", "New", "NullLiteral", "Return", "Super", "Switch", "This", "Throw", "True", "Try", "Typeof", "Var", "Void", "While", "With", "Yield", "Async", "Let", "Static", "As"));
    gast.putRule("StringLiteral", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("StringLiteral"))));
    gast.putRule("ThisExpression", com_subhuti_parser_SubhutiGastNode.terminal("This"));
    gast.putRule("BooleanLiteral", com_subhuti_parser_SubhutiGastNode.alternation(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("True"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("False"))))));
    gast.putRule("NumericLiteral", com_subhuti_parser_SubhutiGastNode.terminal("NumericLiteral"));
    gast.putRule("PrivateIdentifier", com_subhuti_parser_SubhutiGastNode.terminal("PrivateIdentifier"));
    gast.putRule("RegularExpressionLiteral", com_subhuti_parser_SubhutiGastNode.terminal("RegularExpressionLiteral", com_subhuti_struct_LexerMode.__qin_field_REGEXP));
    gast.putRule("NoSubstitutionTemplate", com_subhuti_parser_SubhutiGastNode.terminal("NoSubstitutionTemplate"));
    gast.putRule("Literal", com_subhuti_parser_SubhutiGastNode.alternation(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("NullLiteral"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("BooleanLiteral"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("NumericLiteral"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("StringLiteral"))))));
    gast.putRule("ModuleExportName", com_subhuti_parser_SubhutiGastNode.alternation(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("IdentifierName"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("StringLiteral"))))));
    gast.putAlternation("ModuleExportName", "IdentifierName", "StringLiteral");
    gast.putRule("NewTarget", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("New"), com_subhuti_parser_SubhutiGastNode.terminal("Dot"), com_subhuti_parser_SubhutiGastNode.terminalValue("IdentifierName", "target"))));
    gast.putRule("ImportMeta", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("Import"), com_subhuti_parser_SubhutiGastNode.terminal("Dot"), com_subhuti_parser_SubhutiGastNode.terminalValue("IdentifierName", "meta"))));
    gast.putRule("MetaProperty", com_subhuti_parser_SubhutiGastNode.alternation(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("NewTarget"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("ImportMeta"))))));
    gast.putAlternation("MetaProperty", "NewTarget", "ImportMeta");
    gast.putRule("TemplateLiteral", com_subhuti_parser_SubhutiGastNode.alternation(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("TemplateHead"), com_subhuti_parser_SubhutiGastNode.dynamic("SubstitutionTemplateTail"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("NoSubstitutionTemplate"))))));
    gast.putRule("ComputedMemberSuffix", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("LBracket"), com_subhuti_parser_SubhutiGastNode.dynamic("Expression"), com_subhuti_parser_SubhutiGastNode.terminal("RBracket"))));
    gast.putRule("DotMemberSuffix", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("Dot"), com_subhuti_parser_SubhutiGastNode.ruleReference("IdentifierName"))));
    gast.putRule("IncompleteMemberAccessProperty", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("Dot"), com_subhuti_parser_SubhutiGastNode.dynamic("ErrorRecovery"))));
    gast.putRule("TemplateLiteralSuffix", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("TemplateLiteral"))));
    gast.putRule("DotPrivateIdentifierSuffix", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("Dot"), com_subhuti_parser_SubhutiGastNode.terminal("PrivateIdentifier"))));
    gast.putRule("OptionalChain", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("QuestionDot"), com_subhuti_parser_SubhutiGastNode.dynamic("OptionalChainTail"))));
    gast.putRule("TSNonNullExpressionTail", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("LogicalNot"))));
    gast.putRule("MultiplicativeOperator", com_slime_parser_module_SlimeModuleParser.gastTerminals("Asterisk", "Slash", "Modulo"));
    gast.putRule("AssignmentOperator", com_slime_parser_module_SlimeModuleParser.gastTerminals("MultiplyAssign", "DivideAssign", "ModuloAssign", "PlusAssign", "MinusAssign", "LeftShiftAssign", "RightShiftAssign", "UnsignedRightShiftAssign", "BitwiseAndAssign", "BitwiseXorAssign", "BitwiseOrAssign", "ExponentiationAssign"));
    gast.putRule("AssignmentOperatorAny", com_subhuti_parser_SubhutiGastNode.alternation(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("Assign"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("AssignmentOperator"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("LogicalAndAssign"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("LogicalOrAssign"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("NullishCoalescingAssign"))))));
    return gast;
  }
  static gastTerminals(...tokenNames: string[]): any {
    let alternatives: any = new __QinJavaUtilArrayList(tokenNames.length);
    for (const tokenName of tokenNames) {
      alternatives.add(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal(tokenName))));
    }
    return com_subhuti_parser_SubhutiGastNode.alternation(alternatives);
  }
  Module(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Module();
    }), "Module", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_Module(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "Module", this.moduleStaticRuntime());
    return null;
  }
  ModuleBody(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ModuleBody();
    }), "ModuleBody", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ModuleBody(): any {
    this.ModuleItemList();
    return null;
  }
  ModuleItemList(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ModuleItemList();
    }), "ModuleItemList", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ModuleItemList(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleItemList", this.moduleStaticRuntime());
    return null;
  }
  executeModuleBodyStaticBody(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleBody", this.moduleStaticRuntime());
    return (!this.isParserFail());
  }
  ModuleItem(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ModuleItem();
    }), "ModuleItem", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ModuleItem(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleItem", this.moduleStaticRuntime());
    return null;
  }
  ModuleStatementListItem(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ModuleStatementListItem();
    }), "ModuleStatementListItem", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ModuleStatementListItem(): any {
    this.StatementListItem(this.moduleStatementListItemParams());
    return null;
  }
  ImportDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ImportDeclaration();
    }), "ImportDeclaration", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportDeclaration(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportDeclaration", this.moduleStaticRuntime());
    return null;
  }
  ImportClause(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ImportClause();
    }), "ImportClause", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportClause(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportClause", this.moduleStaticRuntime());
    return null;
  }
  ImportedDefaultBinding(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ImportedDefaultBinding();
    }), "ImportedDefaultBinding", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportedDefaultBinding(): any {
    this.ImportedBinding();
    return null;
  }
  NameSpaceImport(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_NameSpaceImport();
    }), "NameSpaceImport", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_NameSpaceImport(): any {
    this.__qin_field_tokenConsumer.Asterisk();
    this.consumeIdentifierValue("as");
    this.ImportedBinding();
    return null;
  }
  NamedImports(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_NamedImports();
    }), "NamedImports", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_NamedImports(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "NamedImports", this.moduleStaticRuntime());
    return null;
  }
  ImportsList(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ImportsList();
    }), "ImportsList", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportsList(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportsList", this.moduleStaticRuntime());
    return null;
  }
  ImportSpecifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ImportSpecifier();
    }), "ImportSpecifier", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportSpecifier(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportSpecifier", this.moduleStaticRuntime());
    return null;
  }
  ModuleExportName(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ModuleExportName();
    }), "ModuleExportName", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ModuleExportName(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleExportName", this.moduleStaticRuntime());
    return null;
  }
  moduleStaticRuntime(): any {
    return new com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime(this);
  }
  canStartStringLiteral(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("StringLiteral", this.LA(lookaheadOffset).tokenName()));
  }
  canStartToken(tokenName: string, lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals(tokenName, this.LA(lookaheadOffset).tokenName()));
  }
  canStartImportedBinding(lookaheadOffset: number): any {
    if (this.canStartIdentifier(lookaheadOffset)) {
      return true;
    }
    let token: any = this.LA(lookaheadOffset);
    return (__qin_binary__("!=", token, null) && __QinJavaLangString.equals("Yield", token.tokenName()));
  }
  isModuleExportNameAliasStart(lookaheadOffset: number): any {
    if ((!this.canStartIdentifierName(lookaheadOffset) && !this.canStartStringLiteral(lookaheadOffset))) {
      return false;
    }
    let token: any = this.LA(__qin_binary__("+", lookaheadOffset, 1.0));
    return (__qin_binary__("!=", token, null) && __QinJavaLangString.equals("IdentifierName", token.tokenName()) && __QinJavaLangString.equals("as", token.value()));
  }
  canStartModuleExportName(lookaheadOffset: number): any {
    return (this.canStartIdentifierName(lookaheadOffset) || this.canStartStringLiteral(lookaheadOffset));
  }
  isTypeOnlyExportSpecifierStart(lookaheadOffset: number): any {
    let token: any = this.LA(lookaheadOffset);
    if ((__qin_binary__("==", token, null) || !__QinJavaLangString.equals("IdentifierName", token.tokenName()) || !__QinJavaLangString.equals("type", token.value()))) {
      return false;
    }
    let second: any = this.LA(__qin_binary__("+", lookaheadOffset, 1.0));
    if ((__qin_binary__("==", second, null) || (__QinJavaLangString.equals("IdentifierName", second.tokenName()) && __QinJavaLangString.equals("as", second.value())))) {
      return false;
    }
    return this.canStartModuleExportName(__qin_binary__("+", lookaheadOffset, 1.0));
  }
  canStartImportClause(lookaheadOffset: number): any {
    return (this.canStartImportedBinding(lookaheadOffset) || this.canStartToken("Asterisk", lookaheadOffset) || this.canStartToken("LBrace", lookaheadOffset));
  }
  canStartFromClause(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("IdentifierName", this.LA(lookaheadOffset).tokenName()) && __QinJavaLangString.equals("from", this.LA(lookaheadOffset).value()));
  }
  isTypeOnlyImportDeclarationStart(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_isTypeOnlyImportDeclarationStart_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_isTypeOnlyImportDeclarationStart_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: isTypeOnlyImportDeclarationStart/" + __qin_args.length);
  }
  __qin_overload_isTypeOnlyImportDeclarationStart_0_0(): any {
    return this.isTypeOnlyImportDeclarationStart(1.0);
  }
  __qin_overload_isTypeOnlyImportDeclarationStart_1_1(lookaheadOffset: number): any {
    let second: any = this.LA(__qin_binary__("+", lookaheadOffset, 1.0));
    if ((__qin_binary__("==", second, null) || !__QinJavaLangString.equals("IdentifierName", second.tokenName()) || !__QinJavaLangString.equals("type", second.value()))) {
      return false;
    }
    if ((this.canStartToken("LBrace", __qin_binary__("+", lookaheadOffset, 2.0)) || this.canStartToken("Asterisk", __qin_binary__("+", lookaheadOffset, 2.0)))) {
      return true;
    }
    if ((!this.canStartImportedBinding(__qin_binary__("+", lookaheadOffset, 2.0)))) {
      return false;
    }
    return (this.canStartFromClause(__qin_binary__("+", lookaheadOffset, 3.0)) || this.canStartToken("Comma", __qin_binary__("+", lookaheadOffset, 3.0)));
  }
  canStartImportDeclarationAt(lookaheadOffset: number): any {
    if ((!this.canStartToken("Import", lookaheadOffset))) {
      return false;
    }
    return (this.isTypeOnlyImportDeclarationStart(lookaheadOffset) || (this.canStartImportClause(__qin_binary__("+", lookaheadOffset, 1.0)) && !this.isTypeOnlyImportDeclarationStart(lookaheadOffset)) || this.canStartStringLiteral(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartExportDeclarationAt(lookaheadOffset: number): any {
    return (this.canStartToken("Export", lookaheadOffset) || this.canStartDecoratedExportDeclarationAt(lookaheadOffset));
  }
  canStartDecoratedExportDeclarationAt(lookaheadOffset: number): any {
    if ((!this.canStartToken("At", lookaheadOffset))) {
      return false;
    }
    let nesting: any = 0.0;
    for (let offset: any = __qin_binary__("+", lookaheadOffset, 1.0); __qin_binary__("<", offset, __qin_binary__("+", lookaheadOffset, 64.0)); offset++) {
      let tokenName: any = this.tokenNameAt(offset);
      if ((__qin_binary__("==", tokenName, null) || __QinJavaLangString.equals("Semicolon", tokenName) || __QinJavaLangString.equals("RBrace", tokenName))) {
        return false;
      }
      if ((__qin_binary__("==", nesting, 0.0) && __QinJavaLangString.equals("Export", tokenName))) {
        return true;
      }
      if ((__qin_binary__("==", nesting, 0.0) && __QinJavaLangString.equals("Class", tokenName))) {
        return false;
      }
      if ((__QinJavaLangString.equals("LParen", tokenName) || __QinJavaLangString.equals("LBracket", tokenName) || __QinJavaLangString.equals("LBrace", tokenName))) {
        nesting++;
      } else {
        if ((__QinJavaLangString.equals("RParen", tokenName) || __QinJavaLangString.equals("RBracket", tokenName) || __QinJavaLangString.equals("RBrace", tokenName))) {
          if (__qin_binary__("==", nesting, 0.0)) {
            return false;
          }
          nesting--;
        }
      }
    }
    return false;
  }
  canStartModuleItem(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.tokenNameAt(lookaheadOffset), null) && !__QinJavaLangString.equals("RBrace", this.tokenNameAt(lookaheadOffset)));
  }
  moduleStatementListItemParams(): any {
    return new com_slime_parser_base_SlimeJavascriptParserBase$StatementParams(false, true, false);
  }
  ImportedBinding(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ImportedBinding();
    }), "ImportedBinding", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportedBinding(): any {
    this.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, true));
    return null;
  }
  FromClause(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FromClause();
    }), "FromClause", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_FromClause(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "FromClause", this.moduleStaticRuntime());
    return null;
  }
  ModuleSpecifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ModuleSpecifier();
    }), "ModuleSpecifier", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ModuleSpecifier(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleSpecifier", this.moduleStaticRuntime());
    return null;
  }
  ExportDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ExportDeclaration();
    }), "ExportDeclaration", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ExportDeclaration(): any {
    this.__qin_field_tokenConsumer.Export();
    if (this.matchIdentifierValue("type")) {
      this.consumeIdentifierValue("type");
      this.NamedExports();
      this.OptionalFromClause();
      this.SemicolonASI();
      return null;
    }
    if (__QinJavaLangString.equals("Default", this.tokenNameAt(1.0))) {
      this.__qin_field_tokenConsumer.Default();
      if (this.canStartDefaultHoistableDeclaration()) {
        this.HoistableDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
      } else {
        if (__QinJavaLangString.equals("Class", this.tokenNameAt(1.0))) {
          this.ClassDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
        } else {
          this.AssignmentExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, false, true));
          this.SemicolonASI();
        }
      }
      return null;
    }
    if (__QinJavaLangString.equals("Asterisk", this.tokenNameAt(1.0))) {
      this.ExportFromClause();
      this.FromClause();
      this.OptionalWithClause();
      this.SemicolonASI();
      return null;
    }
    if (__QinJavaLangString.equals("LBrace", this.tokenNameAt(1.0))) {
      this.NamedExports();
      if (this.matchIdentifierValue("from")) {
        this.FromClause();
        this.OptionalWithClause();
      }
      this.SemicolonASI();
      return null;
    }
    if ((__QinJavaLangString.equals("Var", this.tokenNameAt(1.0)) || __QinJavaLangString.equals("Const", this.tokenNameAt(1.0)) || this.matchIdentifierValue("let"))) {
      this.VariableStatement(new com_slime_parser_base_SlimeJavascriptParserBase$StatementParams(false, true, false));
      return null;
    }
    this.Declaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
    return null;
  }
  OptionalFromClause(): any {
    if (this.matchIdentifierValue("from")) {
      this.FromClause();
    }
    return null;
  }
  canStartDefaultHoistableDeclaration(): any {
    return (__QinJavaLangString.equals("Function", this.tokenNameAt(1.0)) || this.canStartAsyncFunctionExpression());
  }
  ExportFromClause(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ExportFromClause();
    }), "ExportFromClause", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ExportFromClause(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ExportFromClause", this.moduleStaticRuntime());
    return null;
  }
  NamedExports(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_NamedExports();
    }), "NamedExports", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_NamedExports(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "NamedExports", this.moduleStaticRuntime());
    return null;
  }
  ExportsList(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ExportsList();
    }), "ExportsList", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ExportsList(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ExportsList", this.moduleStaticRuntime());
    return null;
  }
  ExportSpecifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ExportSpecifier();
    }), "ExportSpecifier", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ExportSpecifier(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ExportSpecifier", this.moduleStaticRuntime());
    return null;
  }
  TypeOnlyExportSpecifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TypeOnlyExportSpecifier();
    }), "TypeOnlyExportSpecifier", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TypeOnlyExportSpecifier(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "TypeOnlyExportSpecifier", this.moduleStaticRuntime());
    return null;
  }
  WithClause(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_WithClause();
    }), "WithClause", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_WithClause(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "WithClause", this.moduleStaticRuntime());
    return null;
  }
  OptionalWithClause(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "OptionalWithClause", this.moduleStaticRuntime());
    return null;
  }
  WithEntries(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_WithEntries();
    }), "WithEntries", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_WithEntries(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "WithEntries", this.moduleStaticRuntime());
    return null;
  }
  AttributeKey(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AttributeKey();
    }), "AttributeKey", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AttributeKey(): any {
    this.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "AttributeKey", this.moduleStaticRuntime());
    return null;
  }
}
const SlimeModuleParser = com_slime_parser_module_SlimeModuleParser;
class com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime {
  __qin_field_parser: com_slime_parser_module_SlimeModuleParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_module_SlimeModuleParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeModuleParser$ModuleStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime_1_0(parser: com_slime_parser_module_SlimeModuleParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
  }
  testStaticGate(gateId: string): any {
    if (__QinJavaLangString.equals("ImportSpecifier.aliasStart", gateId)) {
      return this.__qin_field_parser.isModuleExportNameAliasStart(1.0);
    }
    if (__QinJavaLangString.equals("ImportSpecifier.bindingOnlyStart", gateId)) {
      return (this.__qin_field_parser.canStartImportedBinding(1.0) && !this.__qin_field_parser.isModuleExportNameAliasStart(1.0));
    }
    if (__QinJavaLangString.equals("ExportSpecifier.typeOnlyStart", gateId)) {
      return this.__qin_field_parser.isTypeOnlyExportSpecifierStart(1.0);
    }
    if (__QinJavaLangString.equals("ExportSpecifier.exportNameStart", gateId)) {
      return (this.__qin_field_parser.canStartModuleExportName(1.0) && !this.__qin_field_parser.isTypeOnlyExportSpecifierStart(1.0));
    }
    if (__QinJavaLangString.equals("ImportDeclaration.typeImportStart", gateId)) {
      return this.__qin_field_parser.isTypeOnlyImportDeclarationStart();
    }
    if (__QinJavaLangString.equals("ImportDeclaration.clauseImportStart", gateId)) {
      return (this.__qin_field_parser.canStartToken("Import", 1.0) && this.__qin_field_parser.canStartImportClause(2.0) && !this.__qin_field_parser.isTypeOnlyImportDeclarationStart());
    }
    if (__QinJavaLangString.equals("ImportDeclaration.sideEffectImportStart", gateId)) {
      return (this.__qin_field_parser.canStartToken("Import", 1.0) && this.__qin_field_parser.canStartStringLiteral(2.0));
    }
    if (__QinJavaLangString.equals("ModuleItem.importDeclarationStart", gateId)) {
      return this.__qin_field_parser.canStartImportDeclarationAt(1.0);
    }
    if (__QinJavaLangString.equals("ModuleItem.exportDeclarationStart", gateId)) {
      return this.__qin_field_parser.canStartExportDeclarationAt(1.0);
    }
    if (__QinJavaLangString.equals("ModuleItem.statementListItemStart", gateId)) {
      return (this.__qin_field_parser.canStartStatementListItemAt(1.0, this.__qin_field_parser.moduleStatementListItemParams()) && !this.__qin_field_parser.canStartImportDeclarationAt(1.0) && !this.__qin_field_parser.canStartExportDeclarationAt(1.0));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported module static gate: " + gateId));
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
      if (__QinJavaLangString.equals("IdentifierName", ruleName)) {
        return this.__qin_field_parser.canStartIdentifierName(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("StringLiteral", ruleName)) {
        return this.__qin_field_parser.canStartStringLiteral(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("ModuleExportName", ruleName)) {
        return this.__qin_field_parser.canStartModuleExportName(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("AttributeKey", ruleName)) {
        return (this.__qin_field_parser.canStartIdentifierName(lookaheadOffset) || this.__qin_field_parser.canStartStringLiteral(lookaheadOffset));
      }
      if (__QinJavaLangString.equals("WithEntries", ruleName)) {
        return (this.__qin_field_parser.canStartIdentifierName(lookaheadOffset) || this.__qin_field_parser.canStartStringLiteral(lookaheadOffset));
      }
      if (__QinJavaLangString.equals("WithClause", ruleName)) {
        return this.__qin_field_parser.matchIdentifierValue("with", lookaheadOffset);
      }
      if (__QinJavaLangString.equals("TypeOnlyExportSpecifier", ruleName)) {
        return this.__qin_field_parser.isTypeOnlyExportSpecifierStart(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("ImportDeclaration", ruleName)) {
        return this.__qin_field_parser.canStartImportDeclarationAt(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("ExportDeclaration", ruleName)) {
        return this.__qin_field_parser.canStartExportDeclarationAt(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("ModuleStatementListItem", ruleName)) {
        return this.__qin_field_parser.canStartStatementListItemAt(lookaheadOffset, this.__qin_field_parser.moduleStatementListItemParams());
      }
      if (__QinJavaLangString.equals("ImportClause", ruleName)) {
        return this.__qin_field_parser.canStartImportClause(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("FromClause", ruleName)) {
        return this.__qin_field_parser.canStartFromClause(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("ModuleSpecifier", ruleName)) {
        return this.__qin_field_parser.canStartStringLiteral(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("SemicolonASI", ruleName)) {
        return true;
      }
      if (__QinJavaLangString.equals("NamedExports", ruleName)) {
        return this.__qin_field_parser.canStartToken("LBrace", lookaheadOffset);
      }
      if (__QinJavaLangString.equals("NamedImports", ruleName)) {
        return this.__qin_field_parser.canStartToken("LBrace", lookaheadOffset);
      }
      if (__QinJavaLangString.equals("ImportedDefaultBinding", ruleName)) {
        return this.__qin_field_parser.canStartImportedBinding(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("NameSpaceImport", ruleName)) {
        return this.__qin_field_parser.canStartToken("Asterisk", lookaheadOffset);
      }
      if (__QinJavaLangString.equals("ImportSpecifier", ruleName)) {
        return (this.__qin_field_parser.canStartImportedBinding(lookaheadOffset) || this.__qin_field_parser.canStartStringLiteral(lookaheadOffset));
      }
      if (__QinJavaLangString.equals("ImportedBinding", ruleName)) {
        return this.__qin_field_parser.canStartImportedBinding(lookaheadOffset);
      }
      if (__QinJavaLangString.equals("ImportsList", ruleName)) {
        return (this.__qin_field_parser.canStartImportedBinding(lookaheadOffset) || this.__qin_field_parser.canStartStringLiteral(lookaheadOffset));
      }
      if ((__QinJavaLangString.equals("ExportsList", ruleName) || __QinJavaLangString.equals("ExportSpecifier", ruleName))) {
        return this.__qin_field_parser.canStartModuleExportName(lookaheadOffset);
      }
      if ((__QinJavaLangString.equals("Module", ruleName) || __QinJavaLangString.equals("ModuleBody", ruleName) || __QinJavaLangString.equals("ModuleItemList", ruleName) || __QinJavaLangString.equals("ModuleItem", ruleName))) {
        return this.__qin_field_parser.canStartModuleItem(lookaheadOffset);
      }
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported module static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("==", variantKey, null)) {
      if (__QinJavaLangString.equals("IdentifierName", ruleName)) {
        this.__qin_field_parser.IdentifierName();
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("StringLiteral", ruleName)) {
        this.__qin_field_parser.StringLiteral();
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ModuleExportName", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleExportName", this);
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("AttributeKey", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "AttributeKey", this);
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("WithEntries", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "WithEntries", this);
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("WithClause", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "WithClause", this);
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("TypeOnlyExportSpecifier", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "TypeOnlyExportSpecifier", this);
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ImportDeclaration", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportDeclaration", this);
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ExportDeclaration", ruleName)) {
        this.__qin_field_parser.ExportDeclaration();
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ModuleStatementListItem", ruleName)) {
        this.__qin_field_parser.StatementListItem(this.__qin_field_parser.moduleStatementListItemParams());
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ImportClause", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportClause", this);
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("FromClause", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "FromClause", this);
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ModuleSpecifier", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleSpecifier", this);
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("SemicolonASI", ruleName)) {
        this.__qin_field_parser.SemicolonASI();
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("NamedExports", ruleName)) {
        this.__qin_field_parser.NamedExports();
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("NamedImports", ruleName)) {
        this.__qin_field_parser.NamedImports();
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ImportedDefaultBinding", ruleName)) {
        this.__qin_field_parser.ImportedDefaultBinding();
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("NameSpaceImport", ruleName)) {
        this.__qin_field_parser.NameSpaceImport();
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ImportsList", ruleName)) {
        this.__qin_field_parser.ImportsList();
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ImportSpecifier", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportSpecifier", this);
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ImportedBinding", ruleName)) {
        this.__qin_field_parser.ImportedBinding();
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ExportsList", ruleName)) {
        this.__qin_field_parser.ExportsList();
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ExportSpecifier", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ExportSpecifier", this);
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("Module", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "Module", this);
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ModuleBody", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleBody", this);
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ModuleItemList", ruleName)) {
        if (this.__qin_field_parser.isErrorRecoveryMode()) {
          this.__qin_field_parser.executeStaticTolerantManyCall("ModuleItem", null, this);
        } else {
          this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleItemList", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ModuleItem", ruleName)) {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleItem", this);
        return (!this.__qin_field_parser.isParserFail());
      }
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported module static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeModuleParser$ModuleStaticRuntime = com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime;
com_slime_parser_module_SlimeModuleParser.__qin_field_SLIME_MODULE_GAST_GRAMMAR = com_slime_parser_module_SlimeModuleParser.createGastGrammar();
com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR = com_slime_parser_module_SlimeModuleStaticGrammar.grammar();

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_module_SlimeModuleParser, com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime };
