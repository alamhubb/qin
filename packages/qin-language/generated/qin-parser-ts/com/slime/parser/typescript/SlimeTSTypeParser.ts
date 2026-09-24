import { com_slime_parser_SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser as SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser$SourceType, com_slime_parser_SlimeJavascriptParser$SourceType as SourceType, com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime, com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime as JavascriptStaticRuntime } from "../SlimeJavascriptParser.ts";
import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "../../../subhuti/parser/SubhutiStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTypeStaticGrammar, com_slime_parser_typescript_SlimeTSTypeStaticGrammar as SlimeTSTypeStaticGrammar } from "./SlimeTSTypeStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSPrimaryTypeStaticGrammar, com_slime_parser_typescript_SlimeTSPrimaryTypeStaticGrammar as SlimeTSPrimaryTypeStaticGrammar } from "./SlimeTSPrimaryTypeStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSMappedTypeStaticGrammar, com_slime_parser_typescript_SlimeTSMappedTypeStaticGrammar as SlimeTSMappedTypeStaticGrammar } from "./SlimeTSMappedTypeStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTupleTypeStaticGrammar, com_slime_parser_typescript_SlimeTSTupleTypeStaticGrammar as SlimeTSTupleTypeStaticGrammar } from "./SlimeTSTupleTypeStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTypeNameStaticGrammar, com_slime_parser_typescript_SlimeTSTypeNameStaticGrammar as SlimeTSTypeNameStaticGrammar } from "./SlimeTSTypeNameStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTypeReferenceStaticGrammar, com_slime_parser_typescript_SlimeTSTypeReferenceStaticGrammar as SlimeTSTypeReferenceStaticGrammar } from "./SlimeTSTypeReferenceStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTypeParameterInstantiationStaticGrammar, com_slime_parser_typescript_SlimeTSTypeParameterInstantiationStaticGrammar as SlimeTSTypeParameterInstantiationStaticGrammar } from "./SlimeTSTypeParameterInstantiationStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSLiteralTypeStaticGrammar, com_slime_parser_typescript_SlimeTSLiteralTypeStaticGrammar as SlimeTSLiteralTypeStaticGrammar } from "./SlimeTSLiteralTypeStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSPrefixTypeOrPrimaryStaticGrammar, com_slime_parser_typescript_SlimeTSPrefixTypeOrPrimaryStaticGrammar as SlimeTSPrefixTypeOrPrimaryStaticGrammar } from "./SlimeTSPrefixTypeOrPrimaryStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTypeOperandStaticGrammar, com_slime_parser_typescript_SlimeTSTypeOperandStaticGrammar as SlimeTSTypeOperandStaticGrammar } from "./SlimeTSTypeOperandStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTypeParameterDeclarationStaticGrammar, com_slime_parser_typescript_SlimeTSTypeParameterDeclarationStaticGrammar as SlimeTSTypeParameterDeclarationStaticGrammar } from "./SlimeTSTypeParameterDeclarationStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTypeAnnotationStaticGrammar, com_slime_parser_typescript_SlimeTSTypeAnnotationStaticGrammar as SlimeTSTypeAnnotationStaticGrammar } from "./SlimeTSTypeAnnotationStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTypeParameterStaticGrammar, com_slime_parser_typescript_SlimeTSTypeParameterStaticGrammar as SlimeTSTypeParameterStaticGrammar } from "./SlimeTSTypeParameterStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeOptionalTSTypeAnnotationStaticGrammar, com_slime_parser_typescript_SlimeOptionalTSTypeAnnotationStaticGrammar as SlimeOptionalTSTypeAnnotationStaticGrammar } from "./SlimeOptionalTSTypeAnnotationStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeOptionalTSTypeParameterDeclarationStaticGrammar, com_slime_parser_typescript_SlimeOptionalTSTypeParameterDeclarationStaticGrammar as SlimeOptionalTSTypeParameterDeclarationStaticGrammar } from "./SlimeOptionalTSTypeParameterDeclarationStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSKeywordTypeStaticGrammar, com_slime_parser_typescript_SlimeTSKeywordTypeStaticGrammar as SlimeTSKeywordTypeStaticGrammar } from "./SlimeTSKeywordTypeStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTypeOperatorStaticGrammar, com_slime_parser_typescript_SlimeTSTypeOperatorStaticGrammar as SlimeTSTypeOperatorStaticGrammar } from "./SlimeTSTypeOperatorStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSInferTypeStaticGrammar, com_slime_parser_typescript_SlimeTSInferTypeStaticGrammar as SlimeTSInferTypeStaticGrammar } from "./SlimeTSInferTypeStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSUnionOrIntersectionTypeStaticGrammar, com_slime_parser_typescript_SlimeTSUnionOrIntersectionTypeStaticGrammar as SlimeTSUnionOrIntersectionTypeStaticGrammar } from "./SlimeTSUnionOrIntersectionTypeStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSIntersectionTypeStaticGrammar, com_slime_parser_typescript_SlimeTSIntersectionTypeStaticGrammar as SlimeTSIntersectionTypeStaticGrammar } from "./SlimeTSIntersectionTypeStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTypePredicateStaticGrammar, com_slime_parser_typescript_SlimeTSTypePredicateStaticGrammar as SlimeTSTypePredicateStaticGrammar } from "./SlimeTSTypePredicateStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSPropertyOrMethodSignatureStaticGrammar, com_slime_parser_typescript_SlimeTSPropertyOrMethodSignatureStaticGrammar as SlimeTSPropertyOrMethodSignatureStaticGrammar } from "./SlimeTSPropertyOrMethodSignatureStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTypeLiteralStaticGrammar, com_slime_parser_typescript_SlimeTSTypeLiteralStaticGrammar as SlimeTSTypeLiteralStaticGrammar } from "./SlimeTSTypeLiteralStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSSignatureStaticGrammar, com_slime_parser_typescript_SlimeTSSignatureStaticGrammar as SlimeTSSignatureStaticGrammar } from "./SlimeTSSignatureStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSFunctionTypeStaticGrammar, com_slime_parser_typescript_SlimeTSFunctionTypeStaticGrammar as SlimeTSFunctionTypeStaticGrammar } from "./SlimeTSFunctionTypeStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSParameterListStaticGrammar, com_slime_parser_typescript_SlimeTSParameterListStaticGrammar as SlimeTSParameterListStaticGrammar } from "./SlimeTSParameterListStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSParameterStaticGrammar, com_slime_parser_typescript_SlimeTSParameterStaticGrammar as SlimeTSParameterStaticGrammar } from "./SlimeTSParameterStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTypeQueryStaticGrammar, com_slime_parser_typescript_SlimeTSTypeQueryStaticGrammar as SlimeTSTypeQueryStaticGrammar } from "./SlimeTSTypeQueryStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSConditionalTypeStaticGrammar, com_slime_parser_typescript_SlimeTSConditionalTypeStaticGrammar as SlimeTSConditionalTypeStaticGrammar } from "./SlimeTSConditionalTypeStaticGrammar.ts";
import { com_subhuti_struct_SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken as SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken$Builder } from "../../../subhuti/struct/SubhutiMatchToken.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../base/SlimeJavascriptParserBase.ts";
import { com_slime_parser_module_SlimeModuleParser, com_slime_parser_module_SlimeModuleParser as SlimeModuleParser, com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime, com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime as ModuleStaticRuntime } from "../module/SlimeModuleParser.ts";
import { com_slime_parser_class__SlimeClassParser, com_slime_parser_class__SlimeClassParser as SlimeClassParser, com_slime_parser_class__SlimeClassParser$ClassStaticRuntime, com_slime_parser_class__SlimeClassParser$ClassStaticRuntime as ClassStaticRuntime } from "../class_/SlimeClassParser.ts";
import { com_slime_parser_function_SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser as SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime, com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime as FunctionStaticRuntime } from "../function/SlimeFunctionParser.ts";
import { com_slime_parser_statements_SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser as SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime as StatementRootStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime as StatementLoopStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime as StatementTryStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime as StatementIfStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime as StatementVariableStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime as StatementListStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime as StatementJumpStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime as StatementBranchStaticRuntime } from "../statements/SlimeStatementParser.ts";
import { com_slime_parser_expressions_SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser as SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime as AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime as AssignmentExpressionStaticRuntime } from "../expressions/SlimeAssignmentExpressionParser.ts";
import { com_slime_parser_expressions_SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser as SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime as BinaryStaticRuntime } from "../expressions/SlimeBinaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser as SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime as UnaryStaticRuntime } from "../expressions/SlimeUnaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser as SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime as PrimaryStaticRuntime } from "../expressions/SlimePrimaryExpressionParser.ts";
import { com_slime_parser_literal_SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser as SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime as LiteralStaticRuntime } from "../literal/SlimeLiteralParser.ts";
import { com_slime_parser_identifier_SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser as SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime as IdentifierStaticRuntime } from "../identifier/SlimeIdentifierParser.ts";
import { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser as SubhutiParser, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime as StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticPrefix, com_subhuti_parser_SubhutiParser$StaticPrefix as StaticPrefix, com_subhuti_parser_SubhutiParser$StaticChoice, com_subhuti_parser_SubhutiParser$StaticChoice as StaticChoice } from "../../../subhuti/parser/SubhutiParser.ts";
import { com_subhuti_parser_SubhutiParserFinal, com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal } from "../../../subhuti/parser/SubhutiParserFinal.ts";
import { com_subhuti_parser_SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators as SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl as StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher as StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext as AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext as AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes as PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates as StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame as ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames as CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys as CurrentTokenKeys } from "../../../subhuti/parser/SubhutiParserCombinators.ts";
import { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore as SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments as StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult as RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode as StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks as StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$CacheWork, com_subhuti_parser_SubhutiParserCore$CacheWork as CacheWork, com_subhuti_parser_SubhutiParserCore$FailureWork, com_subhuti_parser_SubhutiParserCore$FailureWork as FailureWork } from "../../../subhuti/parser/SubhutiParserCore.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations as ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException as SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException as SubhutiFirstTokenUnknownException } from "../../../subhuti/parser/SubhutiParserState.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../../../subhuti/lookahead/SubhutiTokenLookahead.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __qin_java_functional } from "@qin/java-sdk-js";
import { __qin_subhuti_rule_cache_key } from "@qin/java-sdk-js/tooling";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const RuntimeException = __QinJavaLangRuntimeException;
const UnsupportedOperationException = __QinJavaLangUnsupportedOperationException;
class com_slime_parser_typescript_SlimeTSTypeParser extends com_slime_parser_SlimeJavascriptParser {
  static __qin_field_STATIC_TSTYPE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_PRIMARY_TYPE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_MAPPED_TYPE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_TUPLE_TYPE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_TYPE_NAME_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TSTYPE_REFERENCE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_TYPE_PARAMETER_INSTANTIATION_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_LITERAL_TYPE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_PREFIX_TYPE_OR_PRIMARY_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_TYPE_OPERAND_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_TYPE_PARAMETER_DECLARATION_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_TYPE_ANNOTATION_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_TYPE_PARAMETER_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_OPTIONAL_TS_TYPE_ANNOTATION_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_OPTIONAL_TS_TYPE_PARAMETER_DECLARATION_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_KEYWORD_TYPE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_TYPE_OPERATOR_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_INFER_TYPE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_UNION_OR_INTERSECTION_TYPE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_INTERSECTION_TYPE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_TYPE_PREDICATE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_PROPERTY_OR_METHOD_SIGNATURE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_TYPE_LITERAL_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_SIGNATURE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_FUNCTION_TYPE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_PARAMETER_LIST_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_PARAMETER_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_TYPE_QUERY_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_TS_CONDITIONAL_TYPE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser_1_0(sourceCode: string): void {
    null;
  }
  TSTypeAnnotation(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypeAnnotation receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypeAnnotation.call(this);
    }), "TSTypeAnnotation", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeAnnotation(): void {
    {
      const __qin_typed_receiver_1179: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1179.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_ANNOTATION_GRAMMAR, "TSTypeAnnotation", this.tsTypeAnnotationStaticRuntime());
    }
    return null;
  }
  OptionalTSTypeAnnotation(): void {
    {
      const __qin_typed_receiver_1180: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1180.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_OPTIONAL_TS_TYPE_ANNOTATION_GRAMMAR, "OptionalTSTypeAnnotation", this.optionalTSTypeAnnotationStaticRuntime());
    }
    return null;
  }
  OptionalTSTypeParameterDeclaration(): void {
    {
      const __qin_typed_receiver_1181: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1181.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_OPTIONAL_TS_TYPE_PARAMETER_DECLARATION_GRAMMAR, "OptionalTSTypeParameterDeclaration", this.optionalTSTypeParameterDeclarationStaticRuntime());
    }
    return null;
  }
  TSType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSType.call(this);
    }), "TSType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSType(): void {
    {
      const __qin_typed_receiver_1182: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1182.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TSTYPE_GRAMMAR, "TSType", this.tstypeStaticRuntime());
    }
    return null;
  }
  TSKeywordType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSKeywordType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSKeywordType.call(this);
    }), "TSKeywordType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSKeywordType(): void {
    {
      const __qin_typed_receiver_1183: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1183.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_KEYWORD_TYPE_GRAMMAR, "TSKeywordType", this.tsKeywordTypeStaticRuntime());
    }
    return null;
  }
  TSNumberKeyword(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSNumberKeyword receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSNumberKeyword.call(this);
    }), "TSNumberKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSNumberKeyword(): void {
    {
      const __qin_typed_receiver_1184: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1184.consumeIdentifierValue("number");
    }
    return null;
  }
  TSStringKeyword(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSStringKeyword receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSStringKeyword.call(this);
    }), "TSStringKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSStringKeyword(): void {
    {
      const __qin_typed_receiver_1185: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1185.consumeIdentifierValue("string");
    }
    return null;
  }
  TSBooleanKeyword(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSBooleanKeyword receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSBooleanKeyword.call(this);
    }), "TSBooleanKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSBooleanKeyword(): void {
    {
      const __qin_typed_receiver_1186: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1186.consumeIdentifierValue("boolean");
    }
    return null;
  }
  TSAnyKeyword(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSAnyKeyword receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSAnyKeyword.call(this);
    }), "TSAnyKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSAnyKeyword(): void {
    {
      const __qin_typed_receiver_1187: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1187.consumeIdentifierValue("any");
    }
    return null;
  }
  TSUnknownKeyword(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSUnknownKeyword receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSUnknownKeyword.call(this);
    }), "TSUnknownKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSUnknownKeyword(): void {
    {
      const __qin_typed_receiver_1188: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1188.consumeIdentifierValue("unknown");
    }
    return null;
  }
  TSVoidKeyword(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSVoidKeyword receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSVoidKeyword.call(this);
    }), "TSVoidKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSVoidKeyword(): void {
    {
      const __qin_typed_receiver_1189: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1189.Void();
    }
    return null;
  }
  TSNeverKeyword(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSNeverKeyword receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSNeverKeyword.call(this);
    }), "TSNeverKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSNeverKeyword(): void {
    {
      const __qin_typed_receiver_1190: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1190.consumeIdentifierValue("never");
    }
    return null;
  }
  TSNullKeyword(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSNullKeyword receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSNullKeyword.call(this);
    }), "TSNullKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSNullKeyword(): void {
    {
      const __qin_typed_receiver_1191: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1191.NullLiteral();
    }
    return null;
  }
  TSUndefinedKeyword(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSUndefinedKeyword receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSUndefinedKeyword.call(this);
    }), "TSUndefinedKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSUndefinedKeyword(): void {
    {
      const __qin_typed_receiver_1192: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1192.consumeIdentifierValue("undefined");
    }
    return null;
  }
  TSObjectKeyword(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSObjectKeyword receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSObjectKeyword.call(this);
    }), "TSObjectKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSObjectKeyword(): void {
    {
      const __qin_typed_receiver_1193: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1193.consumeIdentifierValue("object");
    }
    return null;
  }
  TSSymbolKeyword(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSSymbolKeyword receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSSymbolKeyword.call(this);
    }), "TSSymbolKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSSymbolKeyword(): void {
    {
      const __qin_typed_receiver_1194: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1194.consumeIdentifierValue("symbol");
    }
    return null;
  }
  TSBigIntKeyword(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSBigIntKeyword receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSBigIntKeyword.call(this);
    }), "TSBigIntKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSBigIntKeyword(): void {
    {
      const __qin_typed_receiver_1195: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1195.consumeIdentifierValue("bigint");
    }
    return null;
  }
  TSThisType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSThisType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSThisType.call(this);
    }), "TSThisType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSThisType(): void {
    {
      const __qin_typed_receiver_1196: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1196.This();
    }
    return null;
  }
  TSUnionOrIntersectionType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSUnionOrIntersectionType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSUnionOrIntersectionType.call(this);
    }), "TSUnionOrIntersectionType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSUnionOrIntersectionType(): void {
    {
      const __qin_typed_receiver_1197: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1197.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_UNION_OR_INTERSECTION_TYPE_GRAMMAR, "TSUnionOrIntersectionType", this.tsUnionOrIntersectionTypeStaticRuntime());
    }
    return null;
  }
  TSIntersectionType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSIntersectionType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSIntersectionType.call(this);
    }), "TSIntersectionType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSIntersectionType(): void {
    {
      const __qin_typed_receiver_1198: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1198.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_INTERSECTION_TYPE_GRAMMAR, "TSIntersectionType", this.tsIntersectionTypeStaticRuntime());
    }
    return null;
  }
  TSPrimaryType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSPrimaryType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSPrimaryType.call(this);
    }), "TSPrimaryType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSPrimaryType(): void {
    {
      const __qin_typed_receiver_1199: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1199.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PRIMARY_TYPE_GRAMMAR, "TSPrimaryType", this.tsPrimaryTypeStaticRuntime());
    }
    return null;
  }
  TSTypeReference(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypeReference receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypeReference.call(this);
    }), "TSTypeReference", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeReference(): void {
    {
      const __qin_typed_receiver_1200: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1200.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TSTYPE_REFERENCE_GRAMMAR, "TSTypeReference", this.tstypeReferenceStaticRuntime());
    }
    return null;
  }
  TSTypeName(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypeName receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypeName.call(this);
    }), "TSTypeName", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeName(): void {
    {
      const __qin_typed_receiver_1201: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1201.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_NAME_GRAMMAR, "TSTypeName", this.tstypeNameStaticRuntime());
    }
    return null;
  }
  TSTypeNameSuffix(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypeNameSuffix receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypeNameSuffix.call(this);
    }), "TSTypeNameSuffix", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeNameSuffix(): void {
    {
      const __qin_typed_receiver_1202: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1202.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_NAME_GRAMMAR, "TSTypeNameSuffix", this.tstypeNameStaticRuntime());
    }
    return null;
  }
  TSTypeParameterInstantiation(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypeParameterInstantiation receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypeParameterInstantiation.call(this);
    }), "TSTypeParameterInstantiation", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeParameterInstantiation(): void {
    {
      const __qin_typed_receiver_1203: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1203.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_PARAMETER_INSTANTIATION_GRAMMAR, "TSTypeParameterInstantiation", this.tstypeParameterInstantiationStaticRuntime());
    }
    return null;
  }
  consumeGreaterInTypeContext(): void {
    if ((!this.consumeGreaterInTypeContextStatic())) {
      {
        const __qin_typed_receiver_1204: com_slime_parser_typescript_SlimeTSTypeParser = this;
        __qin_typed_receiver_1204.setParseFail();
      }
    }
    return null;
  }
  consumeGreaterInTypeContextStatic(): boolean {
    return (() => {
      const __switch_discriminant = this.tokenNameAt(1.0);
      if (__switch_discriminant === "Greater") {
        {
          const __qin_typed_receiver_1205: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
          __qin_typed_receiver_1205.Greater();
        }
        return (!this.isParserFail());
      }
      else if (__switch_discriminant === "RightShift" || __switch_discriminant === "GreaterEqual" || __switch_discriminant === "UnsignedRightShift" || __switch_discriminant === "RightShiftAssign" || __switch_discriminant === "UnsignedRightShiftAssign") {
        {
          const __qin_typed_receiver_1206: com_slime_parser_typescript_SlimeTSTypeParser = this;
          __qin_typed_receiver_1206.consumePartialToken("Greater", ">", 1.0);
        }
        return (!this.isParserFail());
      }
      else {
        return false;
      }
      return null;
    })();
  }
  TSTypeLiteral(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypeLiteral receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypeLiteral.call(this);
    }), "TSTypeLiteral", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeLiteral(): void {
    {
      const __qin_typed_receiver_1207: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1207.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_LITERAL_GRAMMAR, "TSTypeLiteral", this.tsTypeLiteralStaticRuntime());
    }
    return null;
  }
  TSTypeMembers(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypeMembers receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypeMembers.call(this);
    }), "TSTypeMembers", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeMembers(): void {
    {
      const __qin_typed_receiver_1208: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1208.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_LITERAL_GRAMMAR, "TSTypeMembers", this.tsTypeLiteralStaticRuntime());
    }
    return null;
  }
  TSTypeMember(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypeMember receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypeMember.call(this);
    }), "TSTypeMember", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeMember(): void {
    {
      const __qin_typed_receiver_1209: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1209.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_LITERAL_GRAMMAR, "TSTypeMember", this.tsTypeLiteralStaticRuntime());
    }
    return null;
  }
  TSPropertyOrMethodSignature(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSPropertyOrMethodSignature receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSPropertyOrMethodSignature.call(this);
    }), "TSPropertyOrMethodSignature", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSPropertyOrMethodSignature(): void {
    {
      const __qin_typed_receiver_1210: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1210.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PROPERTY_OR_METHOD_SIGNATURE_GRAMMAR, "TSPropertyOrMethodSignature", this.tsPropertyOrMethodSignatureStaticRuntime());
    }
    return null;
  }
  TSPropertySignature(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSPropertySignature receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSPropertySignature.call(this);
    }), "TSPropertySignature", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSPropertySignature(): void {
    {
      const __qin_typed_receiver_1211: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1211.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_SIGNATURE_GRAMMAR, "TSPropertySignature", this.tsSignatureStaticRuntime());
    }
    return null;
  }
  canStartTSReadonlyProperty(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSReadonlyProperty_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSReadonlyProperty_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSReadonlyProperty/" + __qin_args.length);
  }
  __qin_overload_canStartTSReadonlyProperty_0_0(): boolean {
    return (this.isContextual("readonly") && !this.lookaheadIn(["Colon", "Question"], 2.0));
  }
  __qin_overload_canStartTSReadonlyProperty_1_1(lookaheadOffset: number): boolean {
    return (this.matchIdentifierValue("readonly", lookaheadOffset) && !__QinJavaLangString.equals("Colon", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && !__QinJavaLangString.equals("Question", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartPropertyName(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("IdentifierName", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("StringLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("NumericLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBracket", this.tokenNameAt(lookaheadOffset)));
  }
  canStartTSPropertyOrMethodSignature(lookaheadOffset: number): boolean {
    return (this.canStartTSReadonlyProperty(lookaheadOffset) || this.canStartPropertyName(lookaheadOffset));
  }
  canStartTSMethodSignatureSuffix(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Less", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LParen", this.tokenNameAt(lookaheadOffset)));
  }
  canStartTSPropertySignatureSuffix(lookaheadOffset: number): boolean {
    return (!this.canStartTSMethodSignatureSuffix(lookaheadOffset));
  }
  canStartTSParameterList(lookaheadOffset: number): boolean {
    return this.canStartTSParameter(lookaheadOffset);
  }
  canStartTSParameter(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Ellipsis", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("IdentifierName", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Await", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Yield", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBrace", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBracket", this.tokenNameAt(lookaheadOffset)));
  }
  canStartTSParameterBindingIdentifier(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("IdentifierName", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Await", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Yield", this.tokenNameAt(lookaheadOffset)));
  }
  canStartTSParameterBindingPattern(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("LBrace", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBracket", this.tokenNameAt(lookaheadOffset)));
  }
  TSMethodSignature(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSMethodSignature receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSMethodSignature.call(this);
    }), "TSMethodSignature", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSMethodSignature(): void {
    {
      const __qin_typed_receiver_1212: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1212.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_SIGNATURE_GRAMMAR, "TSMethodSignature", this.tsSignatureStaticRuntime());
    }
    return null;
  }
  TSCallSignatureDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSCallSignatureDeclaration receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSCallSignatureDeclaration.call(this);
    }), "TSCallSignatureDeclaration", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSCallSignatureDeclaration(): void {
    {
      const __qin_typed_receiver_1213: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1213.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_SIGNATURE_GRAMMAR, "TSCallSignatureDeclaration", this.tsSignatureStaticRuntime());
    }
    return null;
  }
  TSConstructSignatureDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSConstructSignatureDeclaration receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSConstructSignatureDeclaration.call(this);
    }), "TSConstructSignatureDeclaration", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSConstructSignatureDeclaration(): void {
    {
      const __qin_typed_receiver_1214: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1214.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_SIGNATURE_GRAMMAR, "TSConstructSignatureDeclaration", this.tsSignatureStaticRuntime());
    }
    return null;
  }
  TSIndexSignature(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSIndexSignature receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSIndexSignature.call(this);
    }), "TSIndexSignature", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSIndexSignature(): void {
    {
      const __qin_typed_receiver_1215: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1215.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_SIGNATURE_GRAMMAR, "TSIndexSignature", this.tsSignatureStaticRuntime());
    }
    return null;
  }
  TSNonArrayType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSNonArrayType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSNonArrayType.call(this);
    }), "TSNonArrayType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSNonArrayType(): void {
    {
      const __qin_typed_receiver_1216: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1216.TSPrefixTypeOrPrimary();
    }
    return null;
  }
  TSArrayType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSArrayType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSArrayType.call(this);
    }), "TSArrayType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSArrayType(): void {
    {
      const __qin_typed_receiver_1217: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1217.TSNonArrayType();
    }
    {
      const __qin_typed_receiver_1218: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1218.LBracket();
    }
    {
      const __qin_typed_receiver_1219: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1219.RBracket();
    }
    return null;
  }
  TSTupleType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTupleType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTupleType.call(this);
    }), "TSTupleType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTupleType(): void {
    {
      const __qin_typed_receiver_1220: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1220.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TUPLE_TYPE_GRAMMAR, "TSTupleType", this.tsTupleTypeStaticRuntime());
    }
    return null;
  }
  TSTupleElementType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTupleElementType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTupleElementType.call(this);
    }), "TSTupleElementType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTupleElementType(): void {
    {
      const __qin_typed_receiver_1221: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1221.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TUPLE_TYPE_GRAMMAR, "TSTupleElementType", this.tsTupleTypeStaticRuntime());
    }
    return null;
  }
  TSNamedTupleMember(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSNamedTupleMember receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSNamedTupleMember.call(this);
    }), "TSNamedTupleMember", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSNamedTupleMember(): void {
    {
      const __qin_typed_receiver_1222: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1222.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TUPLE_TYPE_GRAMMAR, "TSNamedTupleMember", this.tsTupleTypeStaticRuntime());
    }
    return null;
  }
  TSFunctionType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSFunctionType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSFunctionType.call(this);
    }), "TSFunctionType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSFunctionType(): void {
    {
      const __qin_typed_receiver_1223: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1223.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_FUNCTION_TYPE_GRAMMAR, "TSFunctionType", this.tsFunctionTypeStaticRuntime());
    }
    return null;
  }
  TSConstructorType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSConstructorType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSConstructorType.call(this);
    }), "TSConstructorType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSConstructorType(): void {
    {
      const __qin_typed_receiver_1224: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1224.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_FUNCTION_TYPE_GRAMMAR, "TSConstructorType", this.tsFunctionTypeStaticRuntime());
    }
    return null;
  }
  TSParameterList(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSParameterList receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSParameterList.call(this);
    }), "TSParameterList", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSParameterList(): void {
    {
      const __qin_typed_receiver_1225: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1225.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PARAMETER_LIST_GRAMMAR, "TSParameterList", this.tsParameterListStaticRuntime());
    }
    return null;
  }
  TSParameter(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSParameter receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSParameter.call(this);
    }), "TSParameter", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSParameter(): void {
    {
      const __qin_typed_receiver_1226: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1226.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PARAMETER_GRAMMAR, "TSParameter", this.tsParameterStaticRuntime());
    }
    return null;
  }
  TSTypeQuery(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypeQuery receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypeQuery.call(this);
    }), "TSTypeQuery", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeQuery(): void {
    {
      const __qin_typed_receiver_1227: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1227.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_QUERY_GRAMMAR, "TSTypeQuery", this.tsTypeQueryStaticRuntime());
    }
    return null;
  }
  TSIndexedAccessType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSIndexedAccessType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSIndexedAccessType.call(this);
    }), "TSIndexedAccessType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSIndexedAccessType(): void {
    {
      const __qin_typed_receiver_1228: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1228.LBracket();
    }
    {
      const __qin_typed_receiver_1229: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1229.TSType();
    }
    {
      const __qin_typed_receiver_1230: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1230.RBracket();
    }
    return null;
  }
  TSParenthesizedType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSParenthesizedType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSParenthesizedType.call(this);
    }), "TSParenthesizedType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSParenthesizedType(): void {
    {
      const __qin_typed_receiver_1231: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1231.LParen();
    }
    {
      const __qin_typed_receiver_1232: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1232.TSType();
    }
    {
      const __qin_typed_receiver_1233: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1233.RParen();
    }
    return null;
  }
  TSConditionalType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSConditionalType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSConditionalType.call(this);
    }), "TSConditionalType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSConditionalType(): void {
    {
      const __qin_typed_receiver_1234: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1234.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_CONDITIONAL_TYPE_GRAMMAR, "TSConditionalType", this.tsConditionalTypeStaticRuntime());
    }
    return null;
  }
  TSLiteralType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSLiteralType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSLiteralType.call(this);
    }), "TSLiteralType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSLiteralType(): void {
    {
      const __qin_typed_receiver_1235: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1235.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_LITERAL_TYPE_GRAMMAR, "TSLiteralType", this.tsLiteralTypeStaticRuntime());
    }
    return null;
  }
  TSTemplateLiteralType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTemplateLiteralType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTemplateLiteralType.call(this);
    }), "TSTemplateLiteralType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTemplateLiteralType(): void {
    {
      const __qin_typed_receiver_1236: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1236.TemplateLiteral();
    }
    return null;
  }
  TSTypeParameterDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypeParameterDeclaration receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypeParameterDeclaration.call(this);
    }), "TSTypeParameterDeclaration", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeParameterDeclaration(): void {
    {
      const __qin_typed_receiver_1237: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1237.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_PARAMETER_DECLARATION_GRAMMAR, "TSTypeParameterDeclaration", this.tsTypeParameterDeclarationStaticRuntime());
    }
    return null;
  }
  TSTypeParameter(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypeParameter receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypeParameter.call(this);
    }), "TSTypeParameter", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeParameter(): void {
    {
      const __qin_typed_receiver_1238: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1238.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_PARAMETER_GRAMMAR, "TSTypeParameter", this.tsTypeParameterStaticRuntime());
    }
    return null;
  }
  TSTypeOperand(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypeOperand receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypeOperand.call(this);
    }), "TSTypeOperand", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeOperand(): void {
    {
      const __qin_typed_receiver_1239: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1239.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_OPERAND_GRAMMAR, "TSTypeOperand", this.tsTypeOperandStaticRuntime());
    }
    return null;
  }
  TSPrefixTypeOrPrimary(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSPrefixTypeOrPrimary receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSPrefixTypeOrPrimary.call(this);
    }), "TSPrefixTypeOrPrimary", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSPrefixTypeOrPrimary(): void {
    {
      const __qin_typed_receiver_1240: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1240.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PREFIX_TYPE_OR_PRIMARY_GRAMMAR, "TSPrefixTypeOrPrimary", this.tsPrefixTypeOrPrimaryStaticRuntime());
    }
    return null;
  }
  canStartTSTypeQuery(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypeQuery_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypeQuery_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypeQuery/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypeQuery_0_0(): boolean {
    return this.canStartTSTypeQuery(1.0);
  }
  __qin_overload_canStartTSTypeQuery_1_1(lookaheadOffset: number): boolean {
    return __QinJavaLangString.equals("Typeof", this.tokenNameAt(lookaheadOffset));
  }
  canStartTSTypeOperator(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypeOperator_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypeOperator_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypeOperator/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypeOperator_0_0(): boolean {
    return this.canStartTSTypeOperator(1.0);
  }
  __qin_overload_canStartTSTypeOperator_1_1(lookaheadOffset: number): boolean {
    return (this.matchIdentifierValue("keyof", lookaheadOffset) || this.matchIdentifierValue("readonly", lookaheadOffset) || this.matchIdentifierValue("unique", lookaheadOffset));
  }
  canStartTSInferType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSInferType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSInferType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSInferType/" + __qin_args.length);
  }
  __qin_overload_canStartTSInferType_0_0(): boolean {
    return this.canStartTSInferType(1.0);
  }
  __qin_overload_canStartTSInferType_1_1(lookaheadOffset: number): boolean {
    return this.matchIdentifierValue("infer", lookaheadOffset);
  }
  canStartTSFunctionType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSFunctionType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSFunctionType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSFunctionType/" + __qin_args.length);
  }
  __qin_overload_canStartTSFunctionType_0_0(): boolean {
    return this.canStartTSFunctionType(1.0);
  }
  __qin_overload_canStartTSFunctionType_1_1(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Less", this.tokenNameAt(lookaheadOffset)) || (__QinJavaLangString.equals("LParen", this.tokenNameAt(lookaheadOffset)) && this.hasArrowAfterBalancedTypeParens(lookaheadOffset)));
  }
  canStartTSConstructorType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSConstructorType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSConstructorType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSConstructorType/" + __qin_args.length);
  }
  __qin_overload_canStartTSConstructorType_0_0(): boolean {
    return this.canStartTSConstructorType(1.0);
  }
  __qin_overload_canStartTSConstructorType_1_1(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("New", this.tokenNameAt(lookaheadOffset)) || (this.matchIdentifierValue("abstract", lookaheadOffset) && __QinJavaLangString.equals("New", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0)))));
  }
  canStartTSTypePredicate(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypePredicate_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypePredicate_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypePredicate/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypePredicate_0_0(): boolean {
    return this.canStartTSTypePredicate(1.0);
  }
  __qin_overload_canStartTSTypePredicate_1_1(lookaheadOffset: number): boolean {
    if (this.matchIdentifierValue("asserts", lookaheadOffset)) {
      return true;
    }
    let first: string = this.tokenNameAt(lookaheadOffset);
    return ((__QinJavaLangString.equals("This", first) || __QinJavaLangString.equals("IdentifierName", first)) && this.matchIdentifierValue("is", __qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartTSPrimaryType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSPrimaryType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSPrimaryType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSPrimaryType/" + __qin_args.length);
  }
  __qin_overload_canStartTSPrimaryType_0_0(): boolean {
    return this.canStartTSPrimaryType(1.0);
  }
  __qin_overload_canStartTSPrimaryType_1_1(lookaheadOffset: number): boolean {
    return (this.canStartTSMappedType(lookaheadOffset) || this.canStartTSTypeLiteral(lookaheadOffset) || this.canStartTSTupleType(lookaheadOffset) || this.canStartTSThisType(lookaheadOffset) || this.canStartTSKeywordType(lookaheadOffset) || this.canStartTSLiteralType(lookaheadOffset) || this.canStartTSPrimaryTypeReference(lookaheadOffset) || this.canStartTSParenthesizedType(lookaheadOffset));
  }
  canStartTSType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSType/" + __qin_args.length);
  }
  __qin_overload_canStartTSType_0_0(): boolean {
    return this.canStartTSType(1.0);
  }
  __qin_overload_canStartTSType_1_1(lookaheadOffset: number): boolean {
    return (this.canStartTSTypePredicate(lookaheadOffset) || this.canStartTSFunctionType(lookaheadOffset) || this.canStartTSConstructorType(lookaheadOffset) || this.canStartTSUnionOrIntersectionType(lookaheadOffset));
  }
  canStartTSTypeReference(): boolean {
    return this.canStartIdentifier(1.0);
  }
  hasArrowAfterBalancedTypeParens(lParenOffset: number): boolean {
    let open: com_subhuti_struct_SubhutiMatchToken = this.safeTypeLookahead(lParenOffset);
    if ((__qin_binary__("==", open, null) || !__QinJavaLangString.equals("LParen", open.tokenName()))) {
      return false;
    }
    let depth: number = 0.0;
    let seenClosingParen: boolean = false;
    for (let offset: number = lParenOffset; __qin_binary__("<=", offset, 128.0); offset++) {
      let token: com_subhuti_struct_SubhutiMatchToken = this.safeTypeLookahead(offset);
      if (__qin_binary__("==", token, null)) {
        return false;
      }
      let tokenName: string = token.tokenName();
      if (__QinJavaLangString.equals("LParen", tokenName)) {
        depth++;
        continue;
      }
      if (__QinJavaLangString.equals("RParen", tokenName)) {
        depth--;
        if (__qin_binary__("==", depth, 0.0)) {
          seenClosingParen = true;
        }
        if (__qin_binary__("<", depth, 0.0)) {
          return false;
        }
        continue;
      }
      if ((!seenClosingParen)) {
        continue;
      }
      if (__QinJavaLangString.equals("Arrow", tokenName)) {
        return (!token.hasLineBreakBefore());
      }
      if ((__QinJavaLangString.equals("Semicolon", tokenName) || __QinJavaLangString.equals("Comma", tokenName) || __QinJavaLangString.equals("Assign", tokenName) || __QinJavaLangString.equals("RBrace", tokenName))) {
        return false;
      }
    }
    return false;
  }
  safeTypeLookahead(offset: number): com_subhuti_struct_SubhutiMatchToken {
    try {
      return this.LA(offset);
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      return null;
    }
    return null;
  }
  canStartTSMappedType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSMappedType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSMappedType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSMappedType/" + __qin_args.length);
  }
  __qin_overload_canStartTSMappedType_0_0(): boolean {
    return this.canStartTSMappedType(1.0);
  }
  __qin_overload_canStartTSMappedType_1_1(lookaheadOffset: number): boolean {
    if ((!__QinJavaLangString.equals("LBrace", this.tokenNameAt(lookaheadOffset)))) {
      return false;
    }
    if (__QinJavaLangString.equals("LBracket", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0)))) {
      return true;
    }
    if (((__QinJavaLangString.equals("Plus", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) || __QinJavaLangString.equals("Minus", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0)))) && this.matchIdentifierValue("readonly", __qin_binary__("+", lookaheadOffset, 2.0)))) {
      return __QinJavaLangString.equals("LBracket", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 3.0)));
    }
    return (this.matchIdentifierValue("readonly", __qin_binary__("+", lookaheadOffset, 1.0)) && __QinJavaLangString.equals("LBracket", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 2.0))));
  }
  canStartTSTypeLiteral(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypeLiteral_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypeLiteral_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypeLiteral/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypeLiteral_0_0(): boolean {
    return this.canStartTSTypeLiteral(1.0);
  }
  __qin_overload_canStartTSTypeLiteral_1_1(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("LBrace", this.tokenNameAt(lookaheadOffset)) && !this.canStartTSMappedType(lookaheadOffset));
  }
  canStartTSTypeMember(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypeMember_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypeMember_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypeMember/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypeMember_0_0(): boolean {
    return this.canStartTSTypeMember(1.0);
  }
  __qin_overload_canStartTSTypeMember_1_1(lookaheadOffset: number): boolean {
    return (this.canStartTSIndexSignature(lookaheadOffset) || this.canStartTSCallSignatureDeclaration(lookaheadOffset) || this.canStartTSConstructSignatureDeclaration(lookaheadOffset) || this.canStartTSTypeMemberPropertyOrMethodSignature(lookaheadOffset));
  }
  canStartTSIndexSignature(lookaheadOffset: number): boolean {
    let offset: number = lookaheadOffset;
    if (this.matchIdentifierValue("readonly", offset)) {
      offset++;
    }
    return (__QinJavaLangString.equals("LBracket", this.tokenNameAt(offset)) && this.canStartIdentifier(__qin_binary__("+", offset, 1.0)) && __QinJavaLangString.equals("Colon", this.tokenNameAt(__qin_binary__("+", offset, 2.0))));
  }
  canStartTSCallSignatureDeclaration(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Less", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LParen", this.tokenNameAt(lookaheadOffset)));
  }
  canStartTSConstructSignatureDeclaration(lookaheadOffset: number): boolean {
    return __QinJavaLangString.equals("New", this.tokenNameAt(lookaheadOffset));
  }
  canStartTSTypeMemberPropertyOrMethodSignature(lookaheadOffset: number): boolean {
    return (!this.canStartTSIndexSignature(lookaheadOffset) && !this.canStartTSCallSignatureDeclaration(lookaheadOffset) && !this.canStartTSConstructSignatureDeclaration(lookaheadOffset) && this.canStartTSPropertyOrMethodSignature(lookaheadOffset));
  }
  canStartTSTupleType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTupleType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTupleType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTupleType/" + __qin_args.length);
  }
  __qin_overload_canStartTSTupleType_0_0(): boolean {
    return this.canStartTSTupleType(1.0);
  }
  __qin_overload_canStartTSTupleType_1_1(lookaheadOffset: number): boolean {
    return __QinJavaLangString.equals("LBracket", this.tokenNameAt(lookaheadOffset));
  }
  canStartTSRestType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSRestType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSRestType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSRestType/" + __qin_args.length);
  }
  __qin_overload_canStartTSRestType_0_0(): boolean {
    return this.canStartTSRestType(1.0);
  }
  __qin_overload_canStartTSRestType_1_1(lookaheadOffset: number): boolean {
    return __QinJavaLangString.equals("Ellipsis", this.tokenNameAt(lookaheadOffset));
  }
  canStartTSNamedTupleMember(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSNamedTupleMember_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSNamedTupleMember_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSNamedTupleMember/" + __qin_args.length);
  }
  __qin_overload_canStartTSNamedTupleMember_0_0(): boolean {
    return this.canStartTSNamedTupleMember(1.0);
  }
  __qin_overload_canStartTSNamedTupleMember_1_1(lookaheadOffset: number): boolean {
    return (this.canStartIdentifier(lookaheadOffset) && __QinJavaLangString.equals("Colon", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartTSTupleElementTypeValue(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTupleElementTypeValue_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTupleElementTypeValue_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTupleElementTypeValue/" + __qin_args.length);
  }
  __qin_overload_canStartTSTupleElementTypeValue_0_0(): boolean {
    return this.canStartTSTupleElementTypeValue(1.0);
  }
  __qin_overload_canStartTSTupleElementTypeValue_1_1(lookaheadOffset: number): boolean {
    return (this.canStartTSPrimaryType(lookaheadOffset) && !(this.canStartIdentifier(lookaheadOffset) && (__QinJavaLangString.equals("Question", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) || __QinJavaLangString.equals("Colon", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))))) && !__QinJavaLangString.equals("Ellipsis", this.tokenNameAt(lookaheadOffset)));
  }
  canStartTSThisType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSThisType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSThisType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSThisType/" + __qin_args.length);
  }
  __qin_overload_canStartTSThisType_0_0(): boolean {
    return this.canStartTSThisType(1.0);
  }
  __qin_overload_canStartTSThisType_1_1(lookaheadOffset: number): boolean {
    return __QinJavaLangString.equals("This", this.tokenNameAt(lookaheadOffset));
  }
  canStartTSKeywordType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSKeywordType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSKeywordType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSKeywordType/" + __qin_args.length);
  }
  __qin_overload_canStartTSKeywordType_0_0(): boolean {
    return this.canStartTSKeywordType(1.0);
  }
  __qin_overload_canStartTSKeywordType_1_1(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Void", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("NullLiteral", this.tokenNameAt(lookaheadOffset)) || this.matchIdentifierValue("number", lookaheadOffset) || this.matchIdentifierValue("string", lookaheadOffset) || this.matchIdentifierValue("boolean", lookaheadOffset) || this.matchIdentifierValue("any", lookaheadOffset) || this.matchIdentifierValue("unknown", lookaheadOffset) || this.matchIdentifierValue("never", lookaheadOffset) || this.matchIdentifierValue("undefined", lookaheadOffset) || this.matchIdentifierValue("object", lookaheadOffset) || this.matchIdentifierValue("symbol", lookaheadOffset) || this.matchIdentifierValue("bigint", lookaheadOffset));
  }
  canStartTSIntersectionType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSIntersectionType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSIntersectionType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSIntersectionType/" + __qin_args.length);
  }
  __qin_overload_canStartTSIntersectionType_0_0(): boolean {
    return this.canStartTSIntersectionType(1.0);
  }
  __qin_overload_canStartTSIntersectionType_1_1(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("BitwiseAnd", this.tokenNameAt(lookaheadOffset)) || this.canStartTSTypeOperand(lookaheadOffset));
  }
  canStartTSLiteralType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSLiteralType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSLiteralType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSLiteralType/" + __qin_args.length);
  }
  __qin_overload_canStartTSLiteralType_0_0(): boolean {
    return this.canStartTSLiteralType(1.0);
  }
  __qin_overload_canStartTSLiteralType_1_1(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("NumericLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("StringLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("True", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("False", this.tokenNameAt(lookaheadOffset)));
  }
  canStartTSPrimaryTypeReference(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSPrimaryTypeReference_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSPrimaryTypeReference_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSPrimaryTypeReference/" + __qin_args.length);
  }
  __qin_overload_canStartTSPrimaryTypeReference_0_0(): boolean {
    return this.canStartTSPrimaryTypeReference(1.0);
  }
  __qin_overload_canStartTSPrimaryTypeReference_1_1(lookaheadOffset: number): boolean {
    return (this.canStartIdentifier(lookaheadOffset) && !this.canStartTSKeywordType(lookaheadOffset));
  }
  canStartTSParenthesizedType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSParenthesizedType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSParenthesizedType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSParenthesizedType/" + __qin_args.length);
  }
  __qin_overload_canStartTSParenthesizedType_0_0(): boolean {
    return this.canStartTSParenthesizedType(1.0);
  }
  __qin_overload_canStartTSParenthesizedType_1_1(lookaheadOffset: number): boolean {
    return __QinJavaLangString.equals("LParen", this.tokenNameAt(lookaheadOffset));
  }
  canStartTSTypeStaticRule(ruleName: string, variantKey: any, lookaheadOffset: number): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSType static rule variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypePredicate", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && this.canStartTSTypePredicate());
    }
    if (__QinJavaLangString.equals("TSFunctionType", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && this.canStartTSFunctionType());
    }
    if (__QinJavaLangString.equals("TSConstructorType", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && this.canStartTSConstructorType());
    }
    if (__QinJavaLangString.equals("TSUnionOrIntersectionType", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && this.canStartTSTypeUnionFallback());
    }
    if (__QinJavaLangString.equals("TSTypeReference", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && this.canStartTSTypeReference());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSType static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  tstypeStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime(this);
  }
  canStartTSTypeUnionFallback(): boolean {
    return (this.canStartTSUnionOrIntersectionType() && !this.canStartTSTypePredicate() && !this.canStartTSFunctionType() && !this.canStartTSConstructorType());
  }
  tsPrimaryTypeStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime(this);
  }
  tsTypeLiteralStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime(this);
  }
  canStartTSSignatureStaticRule(ruleName: string, variantKey: any, lookaheadOffset: number): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSSignature static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("PropertyName", ruleName)) {
      return this.canStartPropertyName(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      return __QinJavaLangString.equals("Less", this.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSParameterList", ruleName)) {
      return this.canStartTSParameterList(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSTypeAnnotation", ruleName)) {
      return __QinJavaLangString.equals("Colon", this.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      return this.canStartIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      return this.canStartTSType(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSSignature static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  tsSignatureStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsFunctionTypeStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsConditionalTypeStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsTupleTypeStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tstypeReferenceStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime(this);
  }
  tstypeNameStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime(this);
  }
  tstypeNameSuffixStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime(this);
  }
  consumeTSTypeNameSuffixDot(): void {
    {
      const __qin_typed_receiver_1241: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1241.Dot();
    }
    return null;
  }
  tstypeParameterInstantiationStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime(this);
  }
  tsLiteralTypeStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsPrefixTypeOrPrimaryStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime(this);
  }
  canStartTSPrefixPrimaryFallback(): boolean {
    return (!this.canStartTSTypeQuery() && !this.canStartTSTypeOperator() && !this.canStartTSInferType() && this.canStartTSPrimaryType());
  }
  tsTypeOperandStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime(this);
  }
  canStartTSPrefixTypeOrPrimary(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSPrefixTypeOrPrimary_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSPrefixTypeOrPrimary_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSPrefixTypeOrPrimary/" + __qin_args.length);
  }
  __qin_overload_canStartTSPrefixTypeOrPrimary_0_0(): boolean {
    return this.canStartTSPrefixTypeOrPrimary(1.0);
  }
  __qin_overload_canStartTSPrefixTypeOrPrimary_1_1(lookaheadOffset: number): boolean {
    return (this.canStartTSTypeQuery(lookaheadOffset) || this.canStartTSTypeOperator(lookaheadOffset) || this.canStartTSInferType(lookaheadOffset) || this.canStartTSPrimaryType(lookaheadOffset));
  }
  canStartTSTypeOperand(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypeOperand_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypeOperand_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypeOperand/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypeOperand_0_0(): boolean {
    return this.canStartTSTypeOperand(1.0);
  }
  __qin_overload_canStartTSTypeOperand_1_1(lookaheadOffset: number): boolean {
    return this.canStartTSPrefixTypeOrPrimary(lookaheadOffset);
  }
  canStartTSUnionOrIntersectionType(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSUnionOrIntersectionType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSUnionOrIntersectionType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSUnionOrIntersectionType/" + __qin_args.length);
  }
  __qin_overload_canStartTSUnionOrIntersectionType_0_0(): boolean {
    return this.canStartTSUnionOrIntersectionType(1.0);
  }
  __qin_overload_canStartTSUnionOrIntersectionType_1_1(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("BitwiseOr", this.tokenNameAt(lookaheadOffset)) || this.canStartTSIntersectionType(lookaheadOffset) || this.canStartTSTypeOperand(lookaheadOffset));
  }
  tsTypeParameterDeclarationStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime(this);
  }
  tsKeywordTypeStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime(this);
  }
  tsTypeOperatorStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsInferTypeStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsUnionOrIntersectionTypeStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime(this);
  }
  tsIntersectionTypeStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime(this);
  }
  tsTypePredicateStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsPropertyOrMethodSignatureStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime(this);
  }
  tsParameterListStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime(this);
  }
  tsParameterStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime(this);
  }
  tsTypeQueryStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsTypeAnnotationStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime(this);
  }
  optionalTSTypeAnnotationStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime(this);
  }
  optionalTSTypeParameterDeclarationStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime(this);
  }
  tsTypeParameterStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime(this);
  }
  canStartTSTypeParameter(...__qin_args: any[]): boolean {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypeParameter_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypeParameter_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypeParameter/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypeParameter_0_0(): boolean {
    return this.canStartTSTypeParameter(1.0);
  }
  __qin_overload_canStartTSTypeParameter_1_1(lookaheadOffset: number): boolean {
    return this.canStartIdentifier(lookaheadOffset);
  }
  TSTypeOperator(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypeOperator receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypeOperator.call(this);
    }), "TSTypeOperator", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeOperator(): void {
    {
      const __qin_typed_receiver_1242: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1242.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_OPERATOR_GRAMMAR, "TSTypeOperator", this.tsTypeOperatorStaticRuntime());
    }
    return null;
  }
  TSInferType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSInferType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSInferType.call(this);
    }), "TSInferType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSInferType(): void {
    {
      const __qin_typed_receiver_1243: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1243.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_INFER_TYPE_GRAMMAR, "TSInferType", this.tsInferTypeStaticRuntime());
    }
    return null;
  }
  TSRestType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSRestType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSRestType.call(this);
    }), "TSRestType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSRestType(): void {
    {
      const __qin_typed_receiver_1244: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1244.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TUPLE_TYPE_GRAMMAR, "TSRestType", this.tsTupleTypeStaticRuntime());
    }
    return null;
  }
  TSTypePredicate(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSTypePredicate receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSTypePredicate.call(this);
    }), "TSTypePredicate", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypePredicate(): void {
    {
      const __qin_typed_receiver_1245: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1245.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_PREDICATE_GRAMMAR, "TSTypePredicate", this.tsTypePredicateStaticRuntime());
    }
    return null;
  }
  TSMappedType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSTypeParser method=__qin_subhuti_raw_TSMappedType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSTypeParser.prototype.__qin_subhuti_raw_TSMappedType.call(this);
    }), "TSMappedType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSMappedType(): void {
    {
      const __qin_typed_receiver_1246: com_slime_parser_typescript_SlimeTSTypeParser = this;
      __qin_typed_receiver_1246.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_MAPPED_TYPE_GRAMMAR, "TSMappedType", this.tsMappedTypeStaticRuntime());
    }
    return null;
  }
  tsMappedTypeStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  canStartTSMappedTypeReadonly(): boolean {
    return (this.matchIdentifierValue("readonly", 1.0) || (__QinJavaLangString.equals("Plus", this.tokenNameAt(1.0)) && this.matchIdentifierValue("readonly", 2.0)) || (__QinJavaLangString.equals("Minus", this.tokenNameAt(1.0)) && this.matchIdentifierValue("readonly", 2.0)));
  }
  canStartTSMappedTypeTypeParameter(): boolean {
    return this.canStartIdentifier(1.0);
  }
  canStartTSMappedTypeRemap(): boolean {
    return this.matchIdentifierValue("as", 1.0);
  }
  canStartTSMappedTypeTypeEnd(): boolean {
    return (this.canStartIdentifier(1.0) || this.canStartTSType(1.0));
  }
  canStartTSMappedTypeQuestion(): boolean {
    return (__QinJavaLangString.equals("Question", this.tokenNameAt(1.0)) || __QinJavaLangString.equals("Plus", this.tokenNameAt(1.0)) || __QinJavaLangString.equals("Minus", this.tokenNameAt(1.0)));
  }
}
const SlimeTSTypeParser = com_slime_parser_typescript_SlimeTSTypeParser;
class com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSTypeStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
  }
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSTypeStaticGrammar.__qin_field_GATE_TS_PREDICATE_START, gateId)) {
      return this.__qin_field_parser.canStartTSTypePredicate();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSTypeStaticGrammar.__qin_field_GATE_TS_FUNCTION_TYPE_START, gateId)) {
      return this.__qin_field_parser.canStartTSFunctionType();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSTypeStaticGrammar.__qin_field_GATE_TS_CONSTRUCTOR_TYPE_START, gateId)) {
      return this.__qin_field_parser.canStartTSConstructorType();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSTypeStaticGrammar.__qin_field_GATE_TS_UNION_OR_INTERSECTION_TYPE_START, gateId)) {
      return this.__qin_field_parser.canStartTSTypeUnionFallback();
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSType static gate: " + gateId));
  }
  canStartStaticRule(...__qin_args: any[]): boolean {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): boolean {
    return this.__qin_field_parser.canStartTSTypeStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): boolean {
    return this.__qin_field_parser.canStartTSTypeStaticRule(ruleName, variantKey, lookaheadOffset);
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSType static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypePredicate", ruleName)) {
      {
        const __qin_typed_receiver_1247: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1247.TSTypePredicate();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSFunctionType", ruleName)) {
      {
        const __qin_typed_receiver_1248: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1248.TSFunctionType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSConstructorType", ruleName)) {
      {
        const __qin_typed_receiver_1249: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1249.TSConstructorType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSUnionOrIntersectionType", ruleName)) {
      {
        const __qin_typed_receiver_1250: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1250.TSUnionOrIntersectionType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeReference", ruleName)) {
      {
        const __qin_typed_receiver_1251: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1251.TSTypeReference();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSType static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSTypeStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSPrimaryTypeStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
  }
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPrimaryTypeStaticGrammar.__qin_field_GATE_TS_MAPPED_TYPE_START, gateId)) {
      return this.__qin_field_parser.canStartTSMappedType();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPrimaryTypeStaticGrammar.__qin_field_GATE_TS_TYPE_LITERAL_START, gateId)) {
      return this.__qin_field_parser.canStartTSTypeLiteral();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPrimaryTypeStaticGrammar.__qin_field_GATE_TS_TUPLE_TYPE_START, gateId)) {
      return this.__qin_field_parser.canStartTSTupleType();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPrimaryTypeStaticGrammar.__qin_field_GATE_TS_THIS_TYPE_START, gateId)) {
      return this.__qin_field_parser.canStartTSThisType();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPrimaryTypeStaticGrammar.__qin_field_GATE_TS_KEYWORD_TYPE_START, gateId)) {
      return this.__qin_field_parser.canStartTSKeywordType();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPrimaryTypeStaticGrammar.__qin_field_GATE_TS_LITERAL_TYPE_START, gateId)) {
      return this.__qin_field_parser.canStartTSLiteralType();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPrimaryTypeStaticGrammar.__qin_field_GATE_TS_TYPE_REFERENCE_START, gateId)) {
      return this.__qin_field_parser.canStartTSPrimaryTypeReference();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPrimaryTypeStaticGrammar.__qin_field_GATE_TS_PARENTHESIZED_TYPE_START, gateId)) {
      return this.__qin_field_parser.canStartTSParenthesizedType();
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPrimaryType static gate: " + gateId));
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
    if ((__qin_binary__("!=", lookaheadOffset, 1.0) || __qin_binary__("!=", variantKey, null))) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPrimaryType static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSMappedType", ruleName)) {
      return this.__qin_field_parser.canStartTSMappedType();
    }
    if (__QinJavaLangString.equals("TSTypeLiteral", ruleName)) {
      return this.__qin_field_parser.canStartTSTypeLiteral();
    }
    if (__QinJavaLangString.equals("TSTupleType", ruleName)) {
      return this.__qin_field_parser.canStartTSTupleType();
    }
    if (__QinJavaLangString.equals("TSThisType", ruleName)) {
      return this.__qin_field_parser.canStartTSThisType();
    }
    if (__QinJavaLangString.equals("TSKeywordType", ruleName)) {
      return this.__qin_field_parser.canStartTSKeywordType();
    }
    if (__QinJavaLangString.equals("TSLiteralType", ruleName)) {
      return this.__qin_field_parser.canStartTSLiteralType();
    }
    if (__QinJavaLangString.equals("TSTypeReference", ruleName)) {
      return this.__qin_field_parser.canStartTSPrimaryTypeReference();
    }
    if (__QinJavaLangString.equals("TSParenthesizedType", ruleName)) {
      return this.__qin_field_parser.canStartTSParenthesizedType();
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPrimaryType static rule start: " + ruleName + "@" + variantKey));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPrimaryType static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSMappedType", ruleName)) {
      {
        const __qin_typed_receiver_1252: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1252.TSMappedType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeLiteral", ruleName)) {
      {
        const __qin_typed_receiver_1253: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1253.TSTypeLiteral();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTupleType", ruleName)) {
      {
        const __qin_typed_receiver_1254: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1254.TSTupleType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSThisType", ruleName)) {
      {
        const __qin_typed_receiver_1255: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1255.TSThisType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSKeywordType", ruleName)) {
      {
        const __qin_typed_receiver_1256: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1256.TSKeywordType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSLiteralType", ruleName)) {
      {
        const __qin_typed_receiver_1257: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1257.TSLiteralType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeReference", ruleName)) {
      {
        const __qin_typed_receiver_1258: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1258.TSTypeReference();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSParenthesizedType", ruleName)) {
      {
        const __qin_typed_receiver_1259: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1259.TSParenthesizedType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPrimaryType static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSPrimaryTypeStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSTypeLiteralStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
  }
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSTypeLiteralStaticGrammar.__qin_field_GATE_TS_TYPE_MEMBER_START, gateId)) {
      return this.__qin_field_parser.canStartTSTypeMember();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSTypeLiteralStaticGrammar.__qin_field_GATE_TS_INDEX_SIGNATURE_START, gateId)) {
      return this.__qin_field_parser.canStartTSIndexSignature(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSTypeLiteralStaticGrammar.__qin_field_GATE_TS_CALL_SIGNATURE_START, gateId)) {
      return this.__qin_field_parser.canStartTSCallSignatureDeclaration(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSTypeLiteralStaticGrammar.__qin_field_GATE_TS_CONSTRUCT_SIGNATURE_START, gateId)) {
      return this.__qin_field_parser.canStartTSConstructSignatureDeclaration(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSTypeLiteralStaticGrammar.__qin_field_GATE_TS_PROPERTY_OR_METHOD_SIGNATURE_START, gateId)) {
      return this.__qin_field_parser.canStartTSTypeMemberPropertyOrMethodSignature(1.0);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeLiteral static gate: " + gateId));
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeLiteral static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeMember", ruleName)) {
      return this.__qin_field_parser.canStartTSTypeMember(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSIndexSignature", ruleName)) {
      return this.__qin_field_parser.canStartTSIndexSignature(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSCallSignatureDeclaration", ruleName)) {
      return this.__qin_field_parser.canStartTSCallSignatureDeclaration(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSConstructSignatureDeclaration", ruleName)) {
      return this.__qin_field_parser.canStartTSConstructSignatureDeclaration(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSPropertyOrMethodSignature", ruleName)) {
      return this.__qin_field_parser.canStartTSTypeMemberPropertyOrMethodSignature(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeLiteral static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeLiteral static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeMember", ruleName)) {
      {
        const __qin_typed_receiver_1260: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1260.TSTypeMember();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSIndexSignature", ruleName)) {
      {
        const __qin_typed_receiver_1261: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1261.TSIndexSignature();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSCallSignatureDeclaration", ruleName)) {
      {
        const __qin_typed_receiver_1262: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1262.TSCallSignatureDeclaration();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSConstructSignatureDeclaration", ruleName)) {
      {
        const __qin_typed_receiver_1263: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1263.TSConstructSignatureDeclaration();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSPropertyOrMethodSignature", ruleName)) {
      {
        const __qin_typed_receiver_1264: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1264.TSPropertyOrMethodSignature();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeLiteral static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSTypeLiteralStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSTypeReferenceStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeReference static rule variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeName", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSTypeParameterInstantiation", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && __QinJavaLangString.equals("Less", this.__qin_field_parser.tokenNameAt(lookaheadOffset)));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeReference static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeReference static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeName", ruleName)) {
      {
        const __qin_typed_receiver_1265: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1265.TSTypeName();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeParameterInstantiation", ruleName)) {
      {
        const __qin_typed_receiver_1266: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1266.TSTypeParameterInstantiation();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeReference static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSTypeReferenceStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSTypeNameStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeName static rule variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSTypeNameSuffix", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && __QinJavaLangString.equals("Dot", this.__qin_field_parser.tokenNameAt(lookaheadOffset)));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeName static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeName static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      {
        const __qin_typed_receiver_1267: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1267.Identifier();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeNameSuffix", ruleName)) {
      {
        const __qin_typed_receiver_1268: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1268.TSTypeNameSuffix();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeName static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSTypeNameStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeNameSuffix static rule variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Dot", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && __QinJavaLangString.equals("Dot", this.__qin_field_parser.tokenNameAt(lookaheadOffset)));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeNameSuffix static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeNameSuffix static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Dot", ruleName)) {
      {
        const __qin_typed_receiver_1269: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1269.consumeTSTypeNameSuffixDot();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      {
        const __qin_typed_receiver_1270: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1270.Identifier();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeNameSuffix static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterInstantiation static rule variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      return this.__qin_field_parser.canStartTSType(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterInstantiation static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterInstantiation static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      {
        const __qin_typed_receiver_1271: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1271.TSType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterInstantiation static rule call: " + ruleName + "@" + variantKey));
  }
  runStaticAction(actionId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSTypeParameterInstantiationStaticGrammar.__qin_field_ACTION_CLOSE_GREATER, actionId)) {
      return this.__qin_field_parser.consumeGreaterInTypeContextStatic();
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterInstantiation static action: " + actionId));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
  }
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPrefixTypeOrPrimaryStaticGrammar.__qin_field_GATE_TS_TYPE_QUERY_START, gateId)) {
      return this.__qin_field_parser.canStartTSTypeQuery();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPrefixTypeOrPrimaryStaticGrammar.__qin_field_GATE_TS_TYPE_OPERATOR_START, gateId)) {
      return this.__qin_field_parser.canStartTSTypeOperator();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPrefixTypeOrPrimaryStaticGrammar.__qin_field_GATE_TS_INFER_TYPE_START, gateId)) {
      return this.__qin_field_parser.canStartTSInferType();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPrefixTypeOrPrimaryStaticGrammar.__qin_field_GATE_TS_PRIMARY_TYPE_FALLBACK, gateId)) {
      return this.__qin_field_parser.canStartTSPrefixPrimaryFallback();
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPrefixTypeOrPrimary static gate: " + gateId));
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
    if ((__qin_binary__("!=", lookaheadOffset, 1.0) || __qin_binary__("!=", variantKey, null))) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPrefixTypeOrPrimary static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeQuery", ruleName)) {
      return this.__qin_field_parser.canStartTSTypeQuery();
    }
    if (__QinJavaLangString.equals("TSTypeOperator", ruleName)) {
      return this.__qin_field_parser.canStartTSTypeOperator();
    }
    if (__QinJavaLangString.equals("TSInferType", ruleName)) {
      return this.__qin_field_parser.canStartTSInferType();
    }
    if (__QinJavaLangString.equals("TSPrimaryType", ruleName)) {
      return this.__qin_field_parser.canStartTSPrefixPrimaryFallback();
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPrefixTypeOrPrimary static rule start: " + ruleName + "@" + variantKey));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPrefixTypeOrPrimary static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeQuery", ruleName)) {
      {
        const __qin_typed_receiver_1272: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1272.TSTypeQuery();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeOperator", ruleName)) {
      {
        const __qin_typed_receiver_1273: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1273.TSTypeOperator();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSInferType", ruleName)) {
      {
        const __qin_typed_receiver_1274: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1274.TSInferType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSPrimaryType", ruleName)) {
      {
        const __qin_typed_receiver_1275: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1275.TSPrimaryType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPrefixTypeOrPrimary static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSTypeOperandStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeOperand static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__qin_binary__("!=", lookaheadOffset, 1.0)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeOperand static rule start offset: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSPrefixTypeOrPrimary", ruleName)) {
      return this.__qin_field_parser.canStartTSPrefixTypeOrPrimary();
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      return this.__qin_field_parser.canStartTSType();
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeOperand static rule start: " + ruleName + "@" + variantKey));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeOperand static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSPrefixTypeOrPrimary", ruleName)) {
      {
        const __qin_typed_receiver_1276: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1276.TSPrefixTypeOrPrimary();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      {
        const __qin_typed_receiver_1277: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1277.TSType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeOperand static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSTypeOperandStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterDeclaration static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__qin_binary__("<", lookaheadOffset, 1.0)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterDeclaration static rule start offset: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeParameter", ruleName)) {
      return this.__qin_field_parser.canStartTSTypeParameter(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterDeclaration static rule start: " + ruleName + "@" + variantKey));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterDeclaration static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeParameter", ruleName)) {
      {
        const __qin_typed_receiver_1278: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1278.TSTypeParameter();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterDeclaration static rule call: " + ruleName + "@" + variantKey));
  }
  runStaticAction(actionId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSTypeParameterDeclarationStaticGrammar.__qin_field_ACTION_CLOSE_GREATER, actionId)) {
      return this.__qin_field_parser.consumeGreaterInTypeContextStatic();
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterDeclaration static action: " + actionId));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSKeywordTypeStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSKeywordType static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSNumberKeyword", ruleName)) {
      return this.__qin_field_parser.matchIdentifierValue("number", lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSStringKeyword", ruleName)) {
      return this.__qin_field_parser.matchIdentifierValue("string", lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSBooleanKeyword", ruleName)) {
      return this.__qin_field_parser.matchIdentifierValue("boolean", lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSAnyKeyword", ruleName)) {
      return this.__qin_field_parser.matchIdentifierValue("any", lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSUnknownKeyword", ruleName)) {
      return this.__qin_field_parser.matchIdentifierValue("unknown", lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSVoidKeyword", ruleName)) {
      return __QinJavaLangString.equals("Void", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSNeverKeyword", ruleName)) {
      return this.__qin_field_parser.matchIdentifierValue("never", lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSNullKeyword", ruleName)) {
      return __QinJavaLangString.equals("NullLiteral", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSUndefinedKeyword", ruleName)) {
      return this.__qin_field_parser.matchIdentifierValue("undefined", lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSObjectKeyword", ruleName)) {
      return this.__qin_field_parser.matchIdentifierValue("object", lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSSymbolKeyword", ruleName)) {
      return this.__qin_field_parser.matchIdentifierValue("symbol", lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSBigIntKeyword", ruleName)) {
      return this.__qin_field_parser.matchIdentifierValue("bigint", lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSKeywordType static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSKeywordType static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSNumberKeyword", ruleName)) {
      {
        const __qin_typed_receiver_1279: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1279.TSNumberKeyword();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSStringKeyword", ruleName)) {
      {
        const __qin_typed_receiver_1280: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1280.TSStringKeyword();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSBooleanKeyword", ruleName)) {
      {
        const __qin_typed_receiver_1281: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1281.TSBooleanKeyword();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSAnyKeyword", ruleName)) {
      {
        const __qin_typed_receiver_1282: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1282.TSAnyKeyword();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSUnknownKeyword", ruleName)) {
      {
        const __qin_typed_receiver_1283: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1283.TSUnknownKeyword();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSVoidKeyword", ruleName)) {
      {
        const __qin_typed_receiver_1284: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1284.TSVoidKeyword();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSNeverKeyword", ruleName)) {
      {
        const __qin_typed_receiver_1285: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1285.TSNeverKeyword();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSNullKeyword", ruleName)) {
      {
        const __qin_typed_receiver_1286: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1286.TSNullKeyword();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSUndefinedKeyword", ruleName)) {
      {
        const __qin_typed_receiver_1287: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1287.TSUndefinedKeyword();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSObjectKeyword", ruleName)) {
      {
        const __qin_typed_receiver_1288: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1288.TSObjectKeyword();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSSymbolKeyword", ruleName)) {
      {
        const __qin_typed_receiver_1289: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1289.TSSymbolKeyword();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSBigIntKeyword", ruleName)) {
      {
        const __qin_typed_receiver_1290: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1290.TSBigIntKeyword();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSKeywordType static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSKeywordTypeStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSUnionOrIntersectionType static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSIntersectionType", ruleName)) {
      return this.__qin_field_parser.canStartTSIntersectionType(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSUnionOrIntersectionType static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSUnionOrIntersectionType static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSIntersectionType", ruleName)) {
      {
        const __qin_typed_receiver_1291: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1291.TSIntersectionType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSUnionOrIntersectionType static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSIntersectionTypeStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSIntersectionType static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeOperand", ruleName)) {
      return this.__qin_field_parser.canStartTSTypeOperand(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSIntersectionType static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSIntersectionType static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeOperand", ruleName)) {
      {
        const __qin_typed_receiver_1292: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1292.TSTypeOperand();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSIntersectionType static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSIntersectionTypeStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
  }
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPropertyOrMethodSignatureStaticGrammar.__qin_field_GATE_TS_READONLY_PROPERTY_START, gateId)) {
      return this.__qin_field_parser.canStartTSReadonlyProperty();
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPropertyOrMethodSignatureStaticGrammar.__qin_field_GATE_TS_METHOD_SUFFIX_START, gateId)) {
      return this.__qin_field_parser.canStartTSMethodSignatureSuffix(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_typescript_SlimeTSPropertyOrMethodSignatureStaticGrammar.__qin_field_GATE_TS_PROPERTY_SUFFIX_START, gateId)) {
      return this.__qin_field_parser.canStartTSPropertySignatureSuffix(1.0);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPropertyOrMethodSignature static gate: " + gateId));
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPropertyOrMethodSignature static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("PropertyName", ruleName)) {
      return this.__qin_field_parser.canStartPropertyName(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      return __QinJavaLangString.equals("Less", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSParameterList", ruleName)) {
      return this.__qin_field_parser.canStartTSParameterList(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSTypeAnnotation", ruleName)) {
      return __QinJavaLangString.equals("Colon", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPropertyOrMethodSignature static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPropertyOrMethodSignature static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("PropertyName", ruleName)) {
      {
        const __qin_typed_receiver_1293: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1293.PropertyName(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      {
        const __qin_typed_receiver_1294: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1294.TSTypeParameterDeclaration();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSParameterList", ruleName)) {
      {
        const __qin_typed_receiver_1295: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1295.TSParameterList();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeAnnotation", ruleName)) {
      {
        const __qin_typed_receiver_1296: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1296.TSTypeAnnotation();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPropertyOrMethodSignature static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSParameterListStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSParameterList static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSParameter", ruleName)) {
      return this.__qin_field_parser.canStartTSParameter(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSParameterList static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSParameterList static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSParameter", ruleName)) {
      {
        const __qin_typed_receiver_1297: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1297.TSParameter();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSParameterList static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSParameterListStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSParameterStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSParameter static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      return this.__qin_field_parser.canStartTSParameterBindingIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BindingPattern", ruleName)) {
      return this.__qin_field_parser.canStartTSParameterBindingPattern(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSTypeAnnotation", ruleName)) {
      return __QinJavaLangString.equals("Colon", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Initializer", ruleName)) {
      return __QinJavaLangString.equals("Assign", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSParameter static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSParameter static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      {
        const __qin_typed_receiver_1298: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1298.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, false));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingPattern", ruleName)) {
      {
        const __qin_typed_receiver_1299: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1299.BindingPattern(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeAnnotation", ruleName)) {
      {
        const __qin_typed_receiver_1300: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1300.TSTypeAnnotation();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Initializer", ruleName)) {
      {
        const __qin_typed_receiver_1301: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1301.Initializer(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSParameter static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSParameterStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSTypeAnnotationStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeAnnotation static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      return this.__qin_field_parser.canStartTSType(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeAnnotation static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeAnnotation static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      {
        const __qin_typed_receiver_1302: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1302.TSType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeAnnotation static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSTypeAnnotationStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeAnnotation static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeAnnotation", ruleName)) {
      return __QinJavaLangString.equals("Colon", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeAnnotation static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeAnnotation static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeAnnotation", ruleName)) {
      {
        const __qin_typed_receiver_1303: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1303.TSTypeAnnotation();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeAnnotation static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeParameterDeclaration static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      return __QinJavaLangString.equals("Less", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeParameterDeclaration static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeParameterDeclaration static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      {
        const __qin_typed_receiver_1304: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1304.TSTypeParameterDeclaration();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeParameterDeclaration static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime;
class com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSTypeParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSTypeParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSTypeParser$TSTypeParameterStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSTypeParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
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
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameter static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      return this.__qin_field_parser.canStartTSType(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameter static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameter static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      {
        const __qin_typed_receiver_1305: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1305.Identifier();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      {
        const __qin_typed_receiver_1306: com_slime_parser_typescript_SlimeTSTypeParser = this.__qin_field_parser;
        __qin_typed_receiver_1306.TSType();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameter static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSTypeParser$TSTypeParameterStaticRuntime = com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime;
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TSTYPE_GRAMMAR = com_slime_parser_typescript_SlimeTSTypeStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PRIMARY_TYPE_GRAMMAR = com_slime_parser_typescript_SlimeTSPrimaryTypeStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_MAPPED_TYPE_GRAMMAR = com_slime_parser_typescript_SlimeTSMappedTypeStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TUPLE_TYPE_GRAMMAR = com_slime_parser_typescript_SlimeTSTupleTypeStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_NAME_GRAMMAR = com_slime_parser_typescript_SlimeTSTypeNameStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TSTYPE_REFERENCE_GRAMMAR = com_slime_parser_typescript_SlimeTSTypeReferenceStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_PARAMETER_INSTANTIATION_GRAMMAR = com_slime_parser_typescript_SlimeTSTypeParameterInstantiationStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_LITERAL_TYPE_GRAMMAR = com_slime_parser_typescript_SlimeTSLiteralTypeStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PREFIX_TYPE_OR_PRIMARY_GRAMMAR = com_slime_parser_typescript_SlimeTSPrefixTypeOrPrimaryStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_OPERAND_GRAMMAR = com_slime_parser_typescript_SlimeTSTypeOperandStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_PARAMETER_DECLARATION_GRAMMAR = com_slime_parser_typescript_SlimeTSTypeParameterDeclarationStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_ANNOTATION_GRAMMAR = com_slime_parser_typescript_SlimeTSTypeAnnotationStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_PARAMETER_GRAMMAR = com_slime_parser_typescript_SlimeTSTypeParameterStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_OPTIONAL_TS_TYPE_ANNOTATION_GRAMMAR = com_slime_parser_typescript_SlimeOptionalTSTypeAnnotationStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_OPTIONAL_TS_TYPE_PARAMETER_DECLARATION_GRAMMAR = com_slime_parser_typescript_SlimeOptionalTSTypeParameterDeclarationStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_KEYWORD_TYPE_GRAMMAR = com_slime_parser_typescript_SlimeTSKeywordTypeStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_OPERATOR_GRAMMAR = com_slime_parser_typescript_SlimeTSTypeOperatorStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_INFER_TYPE_GRAMMAR = com_slime_parser_typescript_SlimeTSInferTypeStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_UNION_OR_INTERSECTION_TYPE_GRAMMAR = com_slime_parser_typescript_SlimeTSUnionOrIntersectionTypeStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_INTERSECTION_TYPE_GRAMMAR = com_slime_parser_typescript_SlimeTSIntersectionTypeStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_PREDICATE_GRAMMAR = com_slime_parser_typescript_SlimeTSTypePredicateStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PROPERTY_OR_METHOD_SIGNATURE_GRAMMAR = com_slime_parser_typescript_SlimeTSPropertyOrMethodSignatureStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_LITERAL_GRAMMAR = com_slime_parser_typescript_SlimeTSTypeLiteralStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_SIGNATURE_GRAMMAR = com_slime_parser_typescript_SlimeTSSignatureStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_FUNCTION_TYPE_GRAMMAR = com_slime_parser_typescript_SlimeTSFunctionTypeStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PARAMETER_LIST_GRAMMAR = com_slime_parser_typescript_SlimeTSParameterListStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PARAMETER_GRAMMAR = com_slime_parser_typescript_SlimeTSParameterStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_QUERY_GRAMMAR = com_slime_parser_typescript_SlimeTSTypeQueryStaticGrammar.grammar();
com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_CONDITIONAL_TYPE_GRAMMAR = com_slime_parser_typescript_SlimeTSConditionalTypeStaticGrammar.grammar();

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_typescript_SlimeTSTypeParser, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime };
