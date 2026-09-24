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
  gastGrammar(): com_subhuti_parser_SubhutiGastGrammar {
    return com_slime_parser_module_SlimeModuleParser.__qin_field_SLIME_MODULE_GAST_GRAMMAR;
  }
  recognizerLowYieldMemoRules(): __QinJavaUtilSet<string> {
    return __QinJavaUtilSet.of("AssignmentOperatorAny", "ArgumentListItem");
  }
  static createGastGrammar(): com_subhuti_parser_SubhutiGastGrammar {
    let gast: com_subhuti_parser_SubhutiGastGrammar = new com_subhuti_parser_SubhutiGastGrammar();
    {
      const __qin_typed_receiver_1070: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1070.putRule("IdentifierName", this.gastTerminals("IdentifierName", "Await", "Break", "Case", "Catch", "Class", "Const", "Continue", "Debugger", "Default", "Delete", "Do", "Else", "Enum", "Export", "Extends", "False", "Finally", "For", "Function", "If", "Import", "In", "Instanceof", "New", "NullLiteral", "Return", "Super", "Switch", "This", "Throw", "True", "Try", "Typeof", "Var", "Void", "While", "With", "Yield", "Async", "Let", "Static", "As"));
    }
    {
      const __qin_typed_receiver_1071: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1071.putRule("StringLiteral", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("StringLiteral"))));
    }
    {
      const __qin_typed_receiver_1072: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1072.putRule("ThisExpression", com_subhuti_parser_SubhutiGastNode.terminal("This"));
    }
    {
      const __qin_typed_receiver_1073: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1073.putRule("BooleanLiteral", com_subhuti_parser_SubhutiGastNode.alternation(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("True"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("False"))))));
    }
    {
      const __qin_typed_receiver_1074: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1074.putRule("NumericLiteral", com_subhuti_parser_SubhutiGastNode.terminal("NumericLiteral"));
    }
    {
      const __qin_typed_receiver_1075: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1075.putRule("PrivateIdentifier", com_subhuti_parser_SubhutiGastNode.terminal("PrivateIdentifier"));
    }
    {
      const __qin_typed_receiver_1076: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1076.putRule("RegularExpressionLiteral", com_subhuti_parser_SubhutiGastNode.terminal("RegularExpressionLiteral", com_subhuti_struct_LexerMode.__qin_field_REGEXP));
    }
    {
      const __qin_typed_receiver_1077: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1077.putRule("NoSubstitutionTemplate", com_subhuti_parser_SubhutiGastNode.terminal("NoSubstitutionTemplate"));
    }
    {
      const __qin_typed_receiver_1078: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1078.putRule("Literal", com_subhuti_parser_SubhutiGastNode.alternation(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("NullLiteral"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("BooleanLiteral"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("NumericLiteral"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("StringLiteral"))))));
    }
    {
      const __qin_typed_receiver_1079: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1079.putRule("ModuleExportName", com_subhuti_parser_SubhutiGastNode.alternation(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("IdentifierName"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("StringLiteral"))))));
    }
    {
      const __qin_typed_receiver_1080: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1080.putAlternation("ModuleExportName", "IdentifierName", "StringLiteral");
    }
    {
      const __qin_typed_receiver_1081: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1081.putRule("NewTarget", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("New"), com_subhuti_parser_SubhutiGastNode.terminal("Dot"), com_subhuti_parser_SubhutiGastNode.terminalValue("IdentifierName", "target"))));
    }
    {
      const __qin_typed_receiver_1082: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1082.putRule("ImportMeta", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("Import"), com_subhuti_parser_SubhutiGastNode.terminal("Dot"), com_subhuti_parser_SubhutiGastNode.terminalValue("IdentifierName", "meta"))));
    }
    {
      const __qin_typed_receiver_1083: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1083.putRule("MetaProperty", com_subhuti_parser_SubhutiGastNode.alternation(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("NewTarget"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("ImportMeta"))))));
    }
    {
      const __qin_typed_receiver_1084: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1084.putAlternation("MetaProperty", "NewTarget", "ImportMeta");
    }
    {
      const __qin_typed_receiver_1085: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1085.putRule("TemplateLiteral", com_subhuti_parser_SubhutiGastNode.alternation(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("TemplateHead"), com_subhuti_parser_SubhutiGastNode.dynamic("SubstitutionTemplateTail"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("NoSubstitutionTemplate"))))));
    }
    {
      const __qin_typed_receiver_1086: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1086.putRule("ComputedMemberSuffix", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("LBracket"), com_subhuti_parser_SubhutiGastNode.dynamic("Expression"), com_subhuti_parser_SubhutiGastNode.terminal("RBracket"))));
    }
    {
      const __qin_typed_receiver_1087: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1087.putRule("DotMemberSuffix", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("Dot"), com_subhuti_parser_SubhutiGastNode.ruleReference("IdentifierName"))));
    }
    {
      const __qin_typed_receiver_1088: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1088.putRule("IncompleteMemberAccessProperty", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("Dot"), com_subhuti_parser_SubhutiGastNode.dynamic("ErrorRecovery"))));
    }
    {
      const __qin_typed_receiver_1089: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1089.putRule("TemplateLiteralSuffix", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("TemplateLiteral"))));
    }
    {
      const __qin_typed_receiver_1090: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1090.putRule("DotPrivateIdentifierSuffix", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("Dot"), com_subhuti_parser_SubhutiGastNode.terminal("PrivateIdentifier"))));
    }
    {
      const __qin_typed_receiver_1091: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1091.putRule("OptionalChain", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("QuestionDot"), com_subhuti_parser_SubhutiGastNode.dynamic("OptionalChainTail"))));
    }
    {
      const __qin_typed_receiver_1092: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1092.putRule("TSNonNullExpressionTail", com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("LogicalNot"))));
    }
    {
      const __qin_typed_receiver_1093: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1093.putRule("MultiplicativeOperator", this.gastTerminals("Asterisk", "Slash", "Modulo"));
    }
    {
      const __qin_typed_receiver_1094: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1094.putRule("AssignmentOperator", this.gastTerminals("MultiplyAssign", "DivideAssign", "ModuloAssign", "PlusAssign", "MinusAssign", "LeftShiftAssign", "RightShiftAssign", "UnsignedRightShiftAssign", "BitwiseAndAssign", "BitwiseXorAssign", "BitwiseOrAssign", "ExponentiationAssign"));
    }
    {
      const __qin_typed_receiver_1095: com_subhuti_parser_SubhutiGastGrammar = gast;
      __qin_typed_receiver_1095.putRule("AssignmentOperatorAny", com_subhuti_parser_SubhutiGastNode.alternation(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("Assign"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.ruleReference("AssignmentOperator"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("LogicalAndAssign"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("LogicalOrAssign"))), com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal("NullishCoalescingAssign"))))));
    }
    return gast;
  }
  static gastTerminals(...tokenNames: string[]): com_subhuti_parser_SubhutiGastNode {
    let alternatives: __QinJavaUtilList<com_subhuti_parser_SubhutiGastNode> = new __QinJavaUtilArrayList(tokenNames.length);
    for (const tokenName of tokenNames) {
      alternatives.add(com_subhuti_parser_SubhutiGastNode.alternative(__QinJavaUtilList.of(com_subhuti_parser_SubhutiGastNode.terminal(tokenName))));
    }
    return com_subhuti_parser_SubhutiGastNode.alternation(alternatives);
  }
  Module(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_Module receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_Module.call(this);
    }), "Module", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_Module(): void {
    {
      const __qin_typed_receiver_1096: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1096.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "Module", this.moduleStaticRuntime());
    }
    return null;
  }
  ModuleBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ModuleBody receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ModuleBody.call(this);
    }), "ModuleBody", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ModuleBody(): void {
    {
      const __qin_typed_receiver_1097: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1097.ModuleItemList();
    }
    return null;
  }
  ModuleItemList(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ModuleItemList receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ModuleItemList.call(this);
    }), "ModuleItemList", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ModuleItemList(): void {
    {
      const __qin_typed_receiver_1098: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1098.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleItemList", this.moduleStaticRuntime());
    }
    return null;
  }
  executeModuleBodyStaticBody(): boolean {
    {
      const __qin_typed_receiver_1099: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1099.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleBody", this.moduleStaticRuntime());
    }
    return (!this.isParserFail());
  }
  ModuleItem(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ModuleItem receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ModuleItem.call(this);
    }), "ModuleItem", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ModuleItem(): void {
    {
      const __qin_typed_receiver_1100: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1100.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleItem", this.moduleStaticRuntime());
    }
    return null;
  }
  ModuleStatementListItem(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ModuleStatementListItem receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ModuleStatementListItem.call(this);
    }), "ModuleStatementListItem", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ModuleStatementListItem(): void {
    {
      const __qin_typed_receiver_1101: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1101.StatementListItem(this.moduleStatementListItemParams());
    }
    return null;
  }
  ImportDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ImportDeclaration receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ImportDeclaration.call(this);
    }), "ImportDeclaration", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportDeclaration(): void {
    {
      const __qin_typed_receiver_1102: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1102.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportDeclaration", this.moduleStaticRuntime());
    }
    return null;
  }
  ImportClause(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ImportClause receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ImportClause.call(this);
    }), "ImportClause", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportClause(): void {
    {
      const __qin_typed_receiver_1103: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1103.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportClause", this.moduleStaticRuntime());
    }
    return null;
  }
  ImportedDefaultBinding(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ImportedDefaultBinding receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ImportedDefaultBinding.call(this);
    }), "ImportedDefaultBinding", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportedDefaultBinding(): void {
    {
      const __qin_typed_receiver_1104: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1104.ImportedBinding();
    }
    return null;
  }
  NameSpaceImport(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_NameSpaceImport receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_NameSpaceImport.call(this);
    }), "NameSpaceImport", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_NameSpaceImport(): void {
    {
      const __qin_typed_receiver_1105: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1105.Asterisk();
    }
    {
      const __qin_typed_receiver_1106: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1106.consumeIdentifierValue("as");
    }
    {
      const __qin_typed_receiver_1107: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1107.ImportedBinding();
    }
    return null;
  }
  NamedImports(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_NamedImports receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_NamedImports.call(this);
    }), "NamedImports", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_NamedImports(): void {
    {
      const __qin_typed_receiver_1108: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1108.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "NamedImports", this.moduleStaticRuntime());
    }
    return null;
  }
  ImportsList(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ImportsList receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ImportsList.call(this);
    }), "ImportsList", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportsList(): void {
    {
      const __qin_typed_receiver_1109: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1109.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportsList", this.moduleStaticRuntime());
    }
    return null;
  }
  ImportSpecifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ImportSpecifier receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ImportSpecifier.call(this);
    }), "ImportSpecifier", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportSpecifier(): void {
    {
      const __qin_typed_receiver_1110: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1110.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportSpecifier", this.moduleStaticRuntime());
    }
    return null;
  }
  ModuleExportName(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ModuleExportName receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ModuleExportName.call(this);
    }), "ModuleExportName", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ModuleExportName(): void {
    {
      const __qin_typed_receiver_1111: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1111.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleExportName", this.moduleStaticRuntime());
    }
    return null;
  }
  moduleStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime(this);
  }
  canStartStringLiteral(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("StringLiteral", this.LA(lookaheadOffset).tokenName()));
  }
  canStartToken(tokenName: string, lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals(tokenName, this.LA(lookaheadOffset).tokenName()));
  }
  canStartImportedBinding(lookaheadOffset: number): boolean {
    if (this.canStartIdentifier(lookaheadOffset)) {
      return true;
    }
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    return (__qin_binary__("!=", token, null) && __QinJavaLangString.equals("Yield", token.tokenName()));
  }
  isModuleExportNameAliasStart(lookaheadOffset: number): boolean {
    if ((!this.canStartIdentifierName(lookaheadOffset) && !this.canStartStringLiteral(lookaheadOffset))) {
      return false;
    }
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(__qin_binary__("+", lookaheadOffset, 1.0));
    return (__qin_binary__("!=", token, null) && __QinJavaLangString.equals("IdentifierName", token.tokenName()) && __QinJavaLangString.equals("as", token.value()));
  }
  isImportSpecifierTypeAliasStart(lookaheadOffset: number): boolean {
    return (this.matchIdentifierValue("type", lookaheadOffset) && this.canStartModuleExportName(__qin_binary__("+", lookaheadOffset, 1.0)) && this.matchIdentifierValue("as", __qin_binary__("+", lookaheadOffset, 2.0)) && this.canStartImportedBinding(__qin_binary__("+", lookaheadOffset, 3.0)));
  }
  isImportSpecifierTypeBindingOnlyStart(lookaheadOffset: number): boolean {
    return (this.matchIdentifierValue("type", lookaheadOffset) && this.canStartImportedBinding(__qin_binary__("+", lookaheadOffset, 1.0)) && !this.isImportSpecifierTypeAliasStart(lookaheadOffset));
  }
  isImportSpecifierAliasStart(lookaheadOffset: number): boolean {
    return (this.isModuleExportNameAliasStart(lookaheadOffset) && !this.isImportSpecifierTypeAliasStart(lookaheadOffset) && !this.isImportSpecifierTypeBindingOnlyStart(lookaheadOffset));
  }
  isImportSpecifierBindingOnlyStart(lookaheadOffset: number): boolean {
    return (this.canStartImportedBinding(lookaheadOffset) && !this.isImportSpecifierTypeAliasStart(lookaheadOffset) && !this.isImportSpecifierTypeBindingOnlyStart(lookaheadOffset) && !this.isImportSpecifierAliasStart(lookaheadOffset));
  }
  canStartModuleExportName(lookaheadOffset: number): boolean {
    return (this.canStartIdentifierName(lookaheadOffset) || this.canStartStringLiteral(lookaheadOffset));
  }
  isTypeOnlyExportSpecifierStart(lookaheadOffset: number): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    if ((__qin_binary__("==", token, null) || !__QinJavaLangString.equals("IdentifierName", token.tokenName()) || !__QinJavaLangString.equals("type", token.value()))) {
      return false;
    }
    let second: com_subhuti_struct_SubhutiMatchToken = this.LA(__qin_binary__("+", lookaheadOffset, 1.0));
    if ((__qin_binary__("==", second, null) || (__QinJavaLangString.equals("IdentifierName", second.tokenName()) && __QinJavaLangString.equals("as", second.value())))) {
      return false;
    }
    return this.canStartModuleExportName(__qin_binary__("+", lookaheadOffset, 1.0));
  }
  canStartImportClause(lookaheadOffset: number): boolean {
    return (this.canStartImportedBinding(lookaheadOffset) || this.canStartToken("Asterisk", lookaheadOffset) || this.canStartToken("LBrace", lookaheadOffset));
  }
  canStartFromClause(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("IdentifierName", this.LA(lookaheadOffset).tokenName()) && __QinJavaLangString.equals("from", this.LA(lookaheadOffset).value()));
  }
  isTypeOnlyImportDeclarationStart(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_isTypeOnlyImportDeclarationStart_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_isTypeOnlyImportDeclarationStart_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: isTypeOnlyImportDeclarationStart/" + __qin_args.length);
  }
  __qin_overload_isTypeOnlyImportDeclarationStart_0_0(): boolean {
    return this.isTypeOnlyImportDeclarationStart(1.0);
  }
  __qin_overload_isTypeOnlyImportDeclarationStart_1_1(lookaheadOffset: number): boolean {
    let second: com_subhuti_struct_SubhutiMatchToken = this.LA(__qin_binary__("+", lookaheadOffset, 1.0));
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
  canStartImportDeclarationAt(lookaheadOffset: number): boolean {
    if ((!this.canStartToken("Import", lookaheadOffset))) {
      return false;
    }
    return (this.isTypeOnlyImportDeclarationStart(lookaheadOffset) || (this.canStartImportClause(__qin_binary__("+", lookaheadOffset, 1.0)) && !this.isTypeOnlyImportDeclarationStart(lookaheadOffset)) || this.canStartStringLiteral(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartExportDeclarationAt(lookaheadOffset: number): boolean {
    return (this.canStartToken("Export", lookaheadOffset) || this.canStartDecoratedExportDeclarationAt(lookaheadOffset));
  }
  canStartDecoratedExportDeclarationAt(lookaheadOffset: number): boolean {
    if ((!this.canStartToken("At", lookaheadOffset))) {
      return false;
    }
    let nesting: number = 0.0;
    for (let offset: number = __qin_binary__("+", lookaheadOffset, 1.0); __qin_binary__("<", offset, __qin_binary__("+", lookaheadOffset, 64.0)); offset++) {
      let tokenName: string = this.tokenNameAt(offset);
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
  canStartModuleItem(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.tokenNameAt(lookaheadOffset), null) && !__QinJavaLangString.equals("RBrace", this.tokenNameAt(lookaheadOffset)));
  }
  moduleStatementListItemParams(): com_slime_parser_base_SlimeJavascriptParserBase$StatementParams {
    return new com_slime_parser_base_SlimeJavascriptParserBase$StatementParams(false, true, false);
  }
  ImportedBinding(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ImportedBinding receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ImportedBinding.call(this);
    }), "ImportedBinding", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportedBinding(): void {
    {
      const __qin_typed_receiver_1112: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1112.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, true));
    }
    return null;
  }
  FromClause(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_FromClause receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_FromClause.call(this);
    }), "FromClause", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_FromClause(): void {
    {
      const __qin_typed_receiver_1113: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1113.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "FromClause", this.moduleStaticRuntime());
    }
    return null;
  }
  ModuleSpecifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ModuleSpecifier receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ModuleSpecifier.call(this);
    }), "ModuleSpecifier", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ModuleSpecifier(): void {
    {
      const __qin_typed_receiver_1114: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1114.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleSpecifier", this.moduleStaticRuntime());
    }
    return null;
  }
  ExportDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ExportDeclaration receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ExportDeclaration.call(this);
    }), "ExportDeclaration", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ExportDeclaration(): void {
    {
      const __qin_typed_receiver_1115: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1115.Export();
    }
    if (this.matchIdentifierValue("type")) {
      {
        const __qin_typed_receiver_1116: com_slime_parser_module_SlimeModuleParser = this;
        __qin_typed_receiver_1116.consumeIdentifierValue("type");
      }
      {
        const __qin_typed_receiver_1117: com_slime_parser_module_SlimeModuleParser = this;
        __qin_typed_receiver_1117.NamedExports();
      }
      {
        const __qin_typed_receiver_1118: com_slime_parser_module_SlimeModuleParser = this;
        __qin_typed_receiver_1118.OptionalFromClause();
      }
      {
        const __qin_typed_receiver_1119: com_slime_parser_module_SlimeModuleParser = this;
        __qin_typed_receiver_1119.SemicolonASI();
      }
      return null;
    }
    if (__QinJavaLangString.equals("Default", this.tokenNameAt(1.0))) {
      {
        const __qin_typed_receiver_1120: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1120.Default();
      }
      if (this.canStartDefaultHoistableDeclaration()) {
        {
          const __qin_typed_receiver_1121: com_slime_parser_module_SlimeModuleParser = this;
          __qin_typed_receiver_1121.HoistableDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
        }
      } else {
        if (__QinJavaLangString.equals("Class", this.tokenNameAt(1.0))) {
          {
            const __qin_typed_receiver_1122: com_slime_parser_module_SlimeModuleParser = this;
            __qin_typed_receiver_1122.ClassDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
          }
        } else {
          {
            const __qin_typed_receiver_1123: com_slime_parser_module_SlimeModuleParser = this;
            __qin_typed_receiver_1123.AssignmentExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, false, true));
          }
          {
            const __qin_typed_receiver_1124: com_slime_parser_module_SlimeModuleParser = this;
            __qin_typed_receiver_1124.SemicolonASI();
          }
        }
      }
      return null;
    }
    if (__QinJavaLangString.equals("Asterisk", this.tokenNameAt(1.0))) {
      {
        const __qin_typed_receiver_1125: com_slime_parser_module_SlimeModuleParser = this;
        __qin_typed_receiver_1125.ExportFromClause();
      }
      {
        const __qin_typed_receiver_1126: com_slime_parser_module_SlimeModuleParser = this;
        __qin_typed_receiver_1126.FromClause();
      }
      {
        const __qin_typed_receiver_1127: com_slime_parser_module_SlimeModuleParser = this;
        __qin_typed_receiver_1127.OptionalWithClause();
      }
      {
        const __qin_typed_receiver_1128: com_slime_parser_module_SlimeModuleParser = this;
        __qin_typed_receiver_1128.SemicolonASI();
      }
      return null;
    }
    if (__QinJavaLangString.equals("LBrace", this.tokenNameAt(1.0))) {
      {
        const __qin_typed_receiver_1129: com_slime_parser_module_SlimeModuleParser = this;
        __qin_typed_receiver_1129.NamedExports();
      }
      if (this.matchIdentifierValue("from")) {
        {
          const __qin_typed_receiver_1130: com_slime_parser_module_SlimeModuleParser = this;
          __qin_typed_receiver_1130.FromClause();
        }
        {
          const __qin_typed_receiver_1131: com_slime_parser_module_SlimeModuleParser = this;
          __qin_typed_receiver_1131.OptionalWithClause();
        }
      }
      {
        const __qin_typed_receiver_1132: com_slime_parser_module_SlimeModuleParser = this;
        __qin_typed_receiver_1132.SemicolonASI();
      }
      return null;
    }
    if ((__QinJavaLangString.equals("Var", this.tokenNameAt(1.0)) || __QinJavaLangString.equals("Const", this.tokenNameAt(1.0)) || this.matchIdentifierValue("let"))) {
      {
        const __qin_typed_receiver_1133: com_slime_parser_module_SlimeModuleParser = this;
        __qin_typed_receiver_1133.VariableStatement(new com_slime_parser_base_SlimeJavascriptParserBase$StatementParams(false, true, false));
      }
      return null;
    }
    {
      const __qin_typed_receiver_1134: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1134.Declaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
    }
    return null;
  }
  OptionalFromClause(): void {
    if (this.matchIdentifierValue("from")) {
      {
        const __qin_typed_receiver_1135: com_slime_parser_module_SlimeModuleParser = this;
        __qin_typed_receiver_1135.FromClause();
      }
    }
    return null;
  }
  canStartDefaultHoistableDeclaration(): boolean {
    return (__QinJavaLangString.equals("Function", this.tokenNameAt(1.0)) || this.canStartAsyncFunctionExpression());
  }
  ExportFromClause(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ExportFromClause receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ExportFromClause.call(this);
    }), "ExportFromClause", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ExportFromClause(): void {
    {
      const __qin_typed_receiver_1136: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1136.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ExportFromClause", this.moduleStaticRuntime());
    }
    return null;
  }
  NamedExports(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_NamedExports receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_NamedExports.call(this);
    }), "NamedExports", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_NamedExports(): void {
    {
      const __qin_typed_receiver_1137: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1137.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "NamedExports", this.moduleStaticRuntime());
    }
    return null;
  }
  ExportsList(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ExportsList receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ExportsList.call(this);
    }), "ExportsList", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ExportsList(): void {
    {
      const __qin_typed_receiver_1138: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1138.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ExportsList", this.moduleStaticRuntime());
    }
    return null;
  }
  ExportSpecifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_ExportSpecifier receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_ExportSpecifier.call(this);
    }), "ExportSpecifier", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ExportSpecifier(): void {
    {
      const __qin_typed_receiver_1139: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1139.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ExportSpecifier", this.moduleStaticRuntime());
    }
    return null;
  }
  TypeOnlyExportSpecifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_TypeOnlyExportSpecifier receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_TypeOnlyExportSpecifier.call(this);
    }), "TypeOnlyExportSpecifier", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TypeOnlyExportSpecifier(): void {
    {
      const __qin_typed_receiver_1140: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1140.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "TypeOnlyExportSpecifier", this.moduleStaticRuntime());
    }
    return null;
  }
  WithClause(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_WithClause receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_WithClause.call(this);
    }), "WithClause", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_WithClause(): void {
    {
      const __qin_typed_receiver_1141: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1141.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "WithClause", this.moduleStaticRuntime());
    }
    return null;
  }
  OptionalWithClause(): void {
    {
      const __qin_typed_receiver_1142: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1142.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "OptionalWithClause", this.moduleStaticRuntime());
    }
    return null;
  }
  WithEntries(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_WithEntries receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_WithEntries.call(this);
    }), "WithEntries", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_WithEntries(): void {
    {
      const __qin_typed_receiver_1143: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1143.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "WithEntries", this.moduleStaticRuntime());
    }
    return null;
  }
  AttributeKey(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.module.SlimeModuleParser method=__qin_subhuti_raw_AttributeKey receiver=this arity=0 */ com_slime_parser_module_SlimeModuleParser.prototype.__qin_subhuti_raw_AttributeKey.call(this);
    }), "AttributeKey", "SlimeModuleParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AttributeKey(): void {
    {
      const __qin_typed_receiver_1144: com_slime_parser_module_SlimeModuleParser = this;
      __qin_typed_receiver_1144.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "AttributeKey", this.moduleStaticRuntime());
    }
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
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals("ImportSpecifier.typeAliasStart", gateId)) {
      return this.__qin_field_parser.isImportSpecifierTypeAliasStart(1.0);
    }
    if (__QinJavaLangString.equals("ImportSpecifier.typeBindingOnlyStart", gateId)) {
      return this.__qin_field_parser.isImportSpecifierTypeBindingOnlyStart(1.0);
    }
    if (__QinJavaLangString.equals("ImportSpecifier.aliasStart", gateId)) {
      return this.__qin_field_parser.isImportSpecifierAliasStart(1.0);
    }
    if (__QinJavaLangString.equals("ImportSpecifier.bindingOnlyStart", gateId)) {
      return this.__qin_field_parser.isImportSpecifierBindingOnlyStart(1.0);
    }
    if (__QinJavaLangString.equals("ExportSpecifier.typeOnlyStart", gateId)) {
      return this.__qin_field_parser.isTypeOnlyExportSpecifierStart(1.0);
    }
    if (__QinJavaLangString.equals("ExportSpecifier.exportNameStart", gateId)) {
      return (this.__qin_field_parser.canStartModuleExportName(1.0) && !this.__qin_field_parser.isTypeOnlyExportSpecifierStart(1.0));
    }
    if (__QinJavaLangString.equals("ImportsList.nextSpecifierStart", gateId)) {
      return (this.__qin_field_parser.canStartToken("Comma", 1.0) && (this.__qin_field_parser.canStartImportedBinding(2.0) || this.__qin_field_parser.canStartStringLiteral(2.0)));
    }
    if (__QinJavaLangString.equals("ExportsList.nextSpecifierStart", gateId)) {
      return (this.__qin_field_parser.canStartToken("Comma", 1.0) && this.__qin_field_parser.canStartModuleExportName(2.0));
    }
    if (__QinJavaLangString.equals("WithEntries.nextEntryStart", gateId)) {
      return (this.__qin_field_parser.canStartToken("Comma", 1.0) && (this.__qin_field_parser.canStartIdentifierName(2.0) || this.__qin_field_parser.canStartStringLiteral(2.0)));
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
  canStartStaticRule(...__qin_args: any[]): boolean {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): boolean {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): boolean {
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("==", variantKey, null)) {
      if (__QinJavaLangString.equals("IdentifierName", ruleName)) {
        {
          const __qin_typed_receiver_1145: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1145.IdentifierName();
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("StringLiteral", ruleName)) {
        {
          const __qin_typed_receiver_1146: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1146.StringLiteral();
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ModuleExportName", ruleName)) {
        {
          const __qin_typed_receiver_1147: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1147.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleExportName", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("AttributeKey", ruleName)) {
        {
          const __qin_typed_receiver_1148: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1148.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "AttributeKey", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("WithEntries", ruleName)) {
        {
          const __qin_typed_receiver_1149: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1149.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "WithEntries", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("WithClause", ruleName)) {
        {
          const __qin_typed_receiver_1150: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1150.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "WithClause", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("TypeOnlyExportSpecifier", ruleName)) {
        {
          const __qin_typed_receiver_1151: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1151.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "TypeOnlyExportSpecifier", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ImportDeclaration", ruleName)) {
        {
          const __qin_typed_receiver_1152: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1152.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportDeclaration", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ExportDeclaration", ruleName)) {
        {
          const __qin_typed_receiver_1153: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1153.ExportDeclaration();
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ModuleStatementListItem", ruleName)) {
        {
          const __qin_typed_receiver_1154: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1154.StatementListItem(this.__qin_field_parser.moduleStatementListItemParams());
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ImportClause", ruleName)) {
        {
          const __qin_typed_receiver_1155: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1155.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportClause", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("FromClause", ruleName)) {
        {
          const __qin_typed_receiver_1156: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1156.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "FromClause", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ModuleSpecifier", ruleName)) {
        {
          const __qin_typed_receiver_1157: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1157.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleSpecifier", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("SemicolonASI", ruleName)) {
        {
          const __qin_typed_receiver_1158: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1158.SemicolonASI();
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("NamedExports", ruleName)) {
        {
          const __qin_typed_receiver_1159: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1159.NamedExports();
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("NamedImports", ruleName)) {
        {
          const __qin_typed_receiver_1160: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1160.NamedImports();
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ImportedDefaultBinding", ruleName)) {
        {
          const __qin_typed_receiver_1161: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1161.ImportedDefaultBinding();
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("NameSpaceImport", ruleName)) {
        {
          const __qin_typed_receiver_1162: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1162.NameSpaceImport();
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ImportsList", ruleName)) {
        {
          const __qin_typed_receiver_1163: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1163.ImportsList();
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ImportSpecifier", ruleName)) {
        {
          const __qin_typed_receiver_1164: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1164.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ImportSpecifier", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ImportedBinding", ruleName)) {
        {
          const __qin_typed_receiver_1165: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1165.ImportedBinding();
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ExportsList", ruleName)) {
        {
          const __qin_typed_receiver_1166: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1166.ExportsList();
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ExportSpecifier", ruleName)) {
        {
          const __qin_typed_receiver_1167: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1167.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ExportSpecifier", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("Module", ruleName)) {
        {
          const __qin_typed_receiver_1168: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1168.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "Module", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ModuleBody", ruleName)) {
        {
          const __qin_typed_receiver_1169: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1169.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleBody", this);
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ModuleItemList", ruleName)) {
        if (this.__qin_field_parser.isErrorRecoveryMode()) {
          {
            const __qin_typed_receiver_1170: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
            __qin_typed_receiver_1170.executeStaticTolerantManyCall("ModuleItem", null, this);
          }
        } else {
          {
            const __qin_typed_receiver_1171: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
            __qin_typed_receiver_1171.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleItemList", this);
          }
        }
        return (!this.__qin_field_parser.isParserFail());
      }
      if (__QinJavaLangString.equals("ModuleItem", ruleName)) {
        {
          const __qin_typed_receiver_1172: com_slime_parser_module_SlimeModuleParser = this.__qin_field_parser;
          __qin_typed_receiver_1172.executeStaticRule(com_slime_parser_module_SlimeModuleParser.__qin_field_STATIC_MODULE_GRAMMAR, "ModuleItem", this);
        }
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
