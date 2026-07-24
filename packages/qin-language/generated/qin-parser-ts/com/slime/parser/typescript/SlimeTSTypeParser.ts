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
  TSTypeAnnotation(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeAnnotation();
    }), "TSTypeAnnotation", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeAnnotation(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_ANNOTATION_GRAMMAR, "TSTypeAnnotation", this.tsTypeAnnotationStaticRuntime());
    return null;
  }
  OptionalTSTypeAnnotation(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_OPTIONAL_TS_TYPE_ANNOTATION_GRAMMAR, "OptionalTSTypeAnnotation", this.optionalTSTypeAnnotationStaticRuntime());
    return null;
  }
  OptionalTSTypeParameterDeclaration(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_OPTIONAL_TS_TYPE_PARAMETER_DECLARATION_GRAMMAR, "OptionalTSTypeParameterDeclaration", this.optionalTSTypeParameterDeclarationStaticRuntime());
    return null;
  }
  TSType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSType();
    }), "TSType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TSTYPE_GRAMMAR, "TSType", this.tstypeStaticRuntime());
    return null;
  }
  TSKeywordType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSKeywordType();
    }), "TSKeywordType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSKeywordType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_KEYWORD_TYPE_GRAMMAR, "TSKeywordType", this.tsKeywordTypeStaticRuntime());
    return null;
  }
  TSNumberKeyword(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSNumberKeyword();
    }), "TSNumberKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSNumberKeyword(): any {
    this.consumeIdentifierValue("number");
    return null;
  }
  TSStringKeyword(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSStringKeyword();
    }), "TSStringKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSStringKeyword(): any {
    this.consumeIdentifierValue("string");
    return null;
  }
  TSBooleanKeyword(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSBooleanKeyword();
    }), "TSBooleanKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSBooleanKeyword(): any {
    this.consumeIdentifierValue("boolean");
    return null;
  }
  TSAnyKeyword(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSAnyKeyword();
    }), "TSAnyKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSAnyKeyword(): any {
    this.consumeIdentifierValue("any");
    return null;
  }
  TSUnknownKeyword(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSUnknownKeyword();
    }), "TSUnknownKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSUnknownKeyword(): any {
    this.consumeIdentifierValue("unknown");
    return null;
  }
  TSVoidKeyword(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSVoidKeyword();
    }), "TSVoidKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSVoidKeyword(): any {
    this.__qin_field_tokenConsumer.Void();
    return null;
  }
  TSNeverKeyword(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSNeverKeyword();
    }), "TSNeverKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSNeverKeyword(): any {
    this.consumeIdentifierValue("never");
    return null;
  }
  TSNullKeyword(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSNullKeyword();
    }), "TSNullKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSNullKeyword(): any {
    this.__qin_field_tokenConsumer.NullLiteral();
    return null;
  }
  TSUndefinedKeyword(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSUndefinedKeyword();
    }), "TSUndefinedKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSUndefinedKeyword(): any {
    this.consumeIdentifierValue("undefined");
    return null;
  }
  TSObjectKeyword(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSObjectKeyword();
    }), "TSObjectKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSObjectKeyword(): any {
    this.consumeIdentifierValue("object");
    return null;
  }
  TSSymbolKeyword(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSSymbolKeyword();
    }), "TSSymbolKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSSymbolKeyword(): any {
    this.consumeIdentifierValue("symbol");
    return null;
  }
  TSBigIntKeyword(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSBigIntKeyword();
    }), "TSBigIntKeyword", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSBigIntKeyword(): any {
    this.consumeIdentifierValue("bigint");
    return null;
  }
  TSThisType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSThisType();
    }), "TSThisType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSThisType(): any {
    this.__qin_field_tokenConsumer.This();
    return null;
  }
  TSUnionOrIntersectionType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSUnionOrIntersectionType();
    }), "TSUnionOrIntersectionType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSUnionOrIntersectionType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_UNION_OR_INTERSECTION_TYPE_GRAMMAR, "TSUnionOrIntersectionType", this.tsUnionOrIntersectionTypeStaticRuntime());
    return null;
  }
  TSIntersectionType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSIntersectionType();
    }), "TSIntersectionType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSIntersectionType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_INTERSECTION_TYPE_GRAMMAR, "TSIntersectionType", this.tsIntersectionTypeStaticRuntime());
    return null;
  }
  TSPrimaryType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSPrimaryType();
    }), "TSPrimaryType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSPrimaryType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PRIMARY_TYPE_GRAMMAR, "TSPrimaryType", this.tsPrimaryTypeStaticRuntime());
    return null;
  }
  TSTypeReference(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeReference();
    }), "TSTypeReference", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeReference(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TSTYPE_REFERENCE_GRAMMAR, "TSTypeReference", this.tstypeReferenceStaticRuntime());
    return null;
  }
  TSTypeName(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeName();
    }), "TSTypeName", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeName(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_NAME_GRAMMAR, "TSTypeName", this.tstypeNameStaticRuntime());
    return null;
  }
  TSTypeNameSuffix(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeNameSuffix();
    }), "TSTypeNameSuffix", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeNameSuffix(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_NAME_GRAMMAR, "TSTypeNameSuffix", this.tstypeNameStaticRuntime());
    return null;
  }
  TSTypeParameterInstantiation(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeParameterInstantiation();
    }), "TSTypeParameterInstantiation", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeParameterInstantiation(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_PARAMETER_INSTANTIATION_GRAMMAR, "TSTypeParameterInstantiation", this.tstypeParameterInstantiationStaticRuntime());
    return null;
  }
  consumeGreaterInTypeContext(): any {
    if ((!this.consumeGreaterInTypeContextStatic())) {
      this.setParseFail();
    }
    return null;
  }
  consumeGreaterInTypeContextStatic(): any {
    return (() => {
      switch (this.tokenNameAt(1.0)) {
        case "Greater": {
          return this.__qin_field_tokenConsumer.Greater();
          return (!this.isParserFail());
        }
        case "RightShift": {
        }
        case "GreaterEqual": {
        }
        case "UnsignedRightShift": {
        }
        case "RightShiftAssign": {
        }
        case "UnsignedRightShiftAssign": {
          return this.consumePartialToken("Greater", ">", 1.0);
          return (!this.isParserFail());
        }
        default: {
          return false;
        }
      }
      return null;
    })();
  }
  TSTypeLiteral(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeLiteral();
    }), "TSTypeLiteral", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeLiteral(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_LITERAL_GRAMMAR, "TSTypeLiteral", this.tsTypeLiteralStaticRuntime());
    return null;
  }
  TSTypeMembers(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeMembers();
    }), "TSTypeMembers", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeMembers(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_LITERAL_GRAMMAR, "TSTypeMembers", this.tsTypeLiteralStaticRuntime());
    return null;
  }
  TSTypeMember(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeMember();
    }), "TSTypeMember", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeMember(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_LITERAL_GRAMMAR, "TSTypeMember", this.tsTypeLiteralStaticRuntime());
    return null;
  }
  TSPropertyOrMethodSignature(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSPropertyOrMethodSignature();
    }), "TSPropertyOrMethodSignature", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSPropertyOrMethodSignature(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PROPERTY_OR_METHOD_SIGNATURE_GRAMMAR, "TSPropertyOrMethodSignature", this.tsPropertyOrMethodSignatureStaticRuntime());
    return null;
  }
  TSPropertySignature(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSPropertySignature();
    }), "TSPropertySignature", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSPropertySignature(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_SIGNATURE_GRAMMAR, "TSPropertySignature", this.tsSignatureStaticRuntime());
    return null;
  }
  canStartTSReadonlyProperty(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSReadonlyProperty_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSReadonlyProperty_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSReadonlyProperty/" + __qin_args.length);
  }
  __qin_overload_canStartTSReadonlyProperty_0_0(): any {
    return (this.isContextual("readonly") && !this.lookaheadIn(["Colon", "Question"], 2.0));
  }
  __qin_overload_canStartTSReadonlyProperty_1_1(lookaheadOffset: number): any {
    return (this.matchIdentifierValue("readonly", lookaheadOffset) && !__QinJavaLangString.equals("Colon", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && !__QinJavaLangString.equals("Question", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartPropertyName(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("IdentifierName", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("StringLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("NumericLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBracket", this.tokenNameAt(lookaheadOffset)));
  }
  canStartTSPropertyOrMethodSignature(lookaheadOffset: number): any {
    return (this.canStartTSReadonlyProperty(lookaheadOffset) || this.canStartPropertyName(lookaheadOffset));
  }
  canStartTSMethodSignatureSuffix(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("Less", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LParen", this.tokenNameAt(lookaheadOffset)));
  }
  canStartTSPropertySignatureSuffix(lookaheadOffset: number): any {
    return (!this.canStartTSMethodSignatureSuffix(lookaheadOffset));
  }
  canStartTSParameterList(lookaheadOffset: number): any {
    return this.canStartTSParameter(lookaheadOffset);
  }
  canStartTSParameter(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("Ellipsis", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("IdentifierName", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Await", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Yield", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBrace", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBracket", this.tokenNameAt(lookaheadOffset)));
  }
  canStartTSParameterBindingIdentifier(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("IdentifierName", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Await", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("Yield", this.tokenNameAt(lookaheadOffset)));
  }
  canStartTSParameterBindingPattern(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("LBrace", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBracket", this.tokenNameAt(lookaheadOffset)));
  }
  TSMethodSignature(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSMethodSignature();
    }), "TSMethodSignature", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSMethodSignature(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_SIGNATURE_GRAMMAR, "TSMethodSignature", this.tsSignatureStaticRuntime());
    return null;
  }
  TSCallSignatureDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSCallSignatureDeclaration();
    }), "TSCallSignatureDeclaration", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSCallSignatureDeclaration(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_SIGNATURE_GRAMMAR, "TSCallSignatureDeclaration", this.tsSignatureStaticRuntime());
    return null;
  }
  TSConstructSignatureDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSConstructSignatureDeclaration();
    }), "TSConstructSignatureDeclaration", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSConstructSignatureDeclaration(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_SIGNATURE_GRAMMAR, "TSConstructSignatureDeclaration", this.tsSignatureStaticRuntime());
    return null;
  }
  TSIndexSignature(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSIndexSignature();
    }), "TSIndexSignature", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSIndexSignature(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_SIGNATURE_GRAMMAR, "TSIndexSignature", this.tsSignatureStaticRuntime());
    return null;
  }
  TSNonArrayType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSNonArrayType();
    }), "TSNonArrayType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSNonArrayType(): any {
    this.TSPrefixTypeOrPrimary();
    return null;
  }
  TSArrayType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSArrayType();
    }), "TSArrayType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSArrayType(): any {
    this.TSNonArrayType();
    this.__qin_field_tokenConsumer.LBracket();
    this.__qin_field_tokenConsumer.RBracket();
    return null;
  }
  TSTupleType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTupleType();
    }), "TSTupleType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTupleType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TUPLE_TYPE_GRAMMAR, "TSTupleType", this.tsTupleTypeStaticRuntime());
    return null;
  }
  TSTupleElementType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTupleElementType();
    }), "TSTupleElementType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTupleElementType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TUPLE_TYPE_GRAMMAR, "TSTupleElementType", this.tsTupleTypeStaticRuntime());
    return null;
  }
  TSNamedTupleMember(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSNamedTupleMember();
    }), "TSNamedTupleMember", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSNamedTupleMember(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TUPLE_TYPE_GRAMMAR, "TSNamedTupleMember", this.tsTupleTypeStaticRuntime());
    return null;
  }
  TSFunctionType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSFunctionType();
    }), "TSFunctionType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSFunctionType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_FUNCTION_TYPE_GRAMMAR, "TSFunctionType", this.tsFunctionTypeStaticRuntime());
    return null;
  }
  TSConstructorType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSConstructorType();
    }), "TSConstructorType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSConstructorType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_FUNCTION_TYPE_GRAMMAR, "TSConstructorType", this.tsFunctionTypeStaticRuntime());
    return null;
  }
  TSParameterList(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSParameterList();
    }), "TSParameterList", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSParameterList(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PARAMETER_LIST_GRAMMAR, "TSParameterList", this.tsParameterListStaticRuntime());
    return null;
  }
  TSParameter(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSParameter();
    }), "TSParameter", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSParameter(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PARAMETER_GRAMMAR, "TSParameter", this.tsParameterStaticRuntime());
    return null;
  }
  TSTypeQuery(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeQuery();
    }), "TSTypeQuery", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeQuery(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_QUERY_GRAMMAR, "TSTypeQuery", this.tsTypeQueryStaticRuntime());
    return null;
  }
  TSIndexedAccessType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSIndexedAccessType();
    }), "TSIndexedAccessType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSIndexedAccessType(): any {
    this.__qin_field_tokenConsumer.LBracket();
    this.TSType();
    this.__qin_field_tokenConsumer.RBracket();
    return null;
  }
  TSParenthesizedType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSParenthesizedType();
    }), "TSParenthesizedType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSParenthesizedType(): any {
    this.__qin_field_tokenConsumer.LParen();
    this.TSType();
    this.__qin_field_tokenConsumer.RParen();
    return null;
  }
  TSConditionalType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSConditionalType();
    }), "TSConditionalType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSConditionalType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_CONDITIONAL_TYPE_GRAMMAR, "TSConditionalType", this.tsConditionalTypeStaticRuntime());
    return null;
  }
  TSLiteralType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSLiteralType();
    }), "TSLiteralType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSLiteralType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_LITERAL_TYPE_GRAMMAR, "TSLiteralType", this.tsLiteralTypeStaticRuntime());
    return null;
  }
  TSTemplateLiteralType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTemplateLiteralType();
    }), "TSTemplateLiteralType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTemplateLiteralType(): any {
    this.TemplateLiteral();
    return null;
  }
  TSTypeParameterDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeParameterDeclaration();
    }), "TSTypeParameterDeclaration", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeParameterDeclaration(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_PARAMETER_DECLARATION_GRAMMAR, "TSTypeParameterDeclaration", this.tsTypeParameterDeclarationStaticRuntime());
    return null;
  }
  TSTypeParameter(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeParameter();
    }), "TSTypeParameter", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeParameter(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_PARAMETER_GRAMMAR, "TSTypeParameter", this.tsTypeParameterStaticRuntime());
    return null;
  }
  TSTypeOperand(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeOperand();
    }), "TSTypeOperand", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeOperand(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_OPERAND_GRAMMAR, "TSTypeOperand", this.tsTypeOperandStaticRuntime());
    return null;
  }
  TSPrefixTypeOrPrimary(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSPrefixTypeOrPrimary();
    }), "TSPrefixTypeOrPrimary", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSPrefixTypeOrPrimary(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_PREFIX_TYPE_OR_PRIMARY_GRAMMAR, "TSPrefixTypeOrPrimary", this.tsPrefixTypeOrPrimaryStaticRuntime());
    return null;
  }
  canStartTSTypeQuery(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypeQuery_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypeQuery_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypeQuery/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypeQuery_0_0(): any {
    return this.canStartTSTypeQuery(1.0);
  }
  __qin_overload_canStartTSTypeQuery_1_1(lookaheadOffset: number): any {
    return __QinJavaLangString.equals("Typeof", this.tokenNameAt(lookaheadOffset));
  }
  canStartTSTypeOperator(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypeOperator_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypeOperator_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypeOperator/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypeOperator_0_0(): any {
    return this.canStartTSTypeOperator(1.0);
  }
  __qin_overload_canStartTSTypeOperator_1_1(lookaheadOffset: number): any {
    return (this.matchIdentifierValue("keyof", lookaheadOffset) || this.matchIdentifierValue("readonly", lookaheadOffset) || this.matchIdentifierValue("unique", lookaheadOffset));
  }
  canStartTSInferType(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSInferType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSInferType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSInferType/" + __qin_args.length);
  }
  __qin_overload_canStartTSInferType_0_0(): any {
    return this.canStartTSInferType(1.0);
  }
  __qin_overload_canStartTSInferType_1_1(lookaheadOffset: number): any {
    return this.matchIdentifierValue("infer", lookaheadOffset);
  }
  canStartTSFunctionType(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSFunctionType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSFunctionType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSFunctionType/" + __qin_args.length);
  }
  __qin_overload_canStartTSFunctionType_0_0(): any {
    return this.canStartTSFunctionType(1.0);
  }
  __qin_overload_canStartTSFunctionType_1_1(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("Less", this.tokenNameAt(lookaheadOffset)) || (__QinJavaLangString.equals("LParen", this.tokenNameAt(lookaheadOffset)) && this.hasArrowAfterBalancedTypeParens(lookaheadOffset)));
  }
  canStartTSConstructorType(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSConstructorType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSConstructorType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSConstructorType/" + __qin_args.length);
  }
  __qin_overload_canStartTSConstructorType_0_0(): any {
    return this.canStartTSConstructorType(1.0);
  }
  __qin_overload_canStartTSConstructorType_1_1(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("New", this.tokenNameAt(lookaheadOffset)) || (this.matchIdentifierValue("abstract", lookaheadOffset) && __QinJavaLangString.equals("New", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0)))));
  }
  canStartTSTypePredicate(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypePredicate_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypePredicate_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypePredicate/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypePredicate_0_0(): any {
    return this.canStartTSTypePredicate(1.0);
  }
  __qin_overload_canStartTSTypePredicate_1_1(lookaheadOffset: number): any {
    if (this.matchIdentifierValue("asserts", lookaheadOffset)) {
      return true;
    }
    let first: any = this.tokenNameAt(lookaheadOffset);
    return ((__QinJavaLangString.equals("This", first) || __QinJavaLangString.equals("IdentifierName", first)) && this.matchIdentifierValue("is", __qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartTSPrimaryType(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSPrimaryType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSPrimaryType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSPrimaryType/" + __qin_args.length);
  }
  __qin_overload_canStartTSPrimaryType_0_0(): any {
    return this.canStartTSPrimaryType(1.0);
  }
  __qin_overload_canStartTSPrimaryType_1_1(lookaheadOffset: number): any {
    return (this.canStartTSMappedType(lookaheadOffset) || this.canStartTSTypeLiteral(lookaheadOffset) || this.canStartTSTupleType(lookaheadOffset) || this.canStartTSThisType(lookaheadOffset) || this.canStartTSKeywordType(lookaheadOffset) || this.canStartTSLiteralType(lookaheadOffset) || this.canStartTSPrimaryTypeReference(lookaheadOffset) || this.canStartTSParenthesizedType(lookaheadOffset));
  }
  canStartTSType(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSType/" + __qin_args.length);
  }
  __qin_overload_canStartTSType_0_0(): any {
    return this.canStartTSType(1.0);
  }
  __qin_overload_canStartTSType_1_1(lookaheadOffset: number): any {
    return (this.canStartTSTypePredicate(lookaheadOffset) || this.canStartTSFunctionType(lookaheadOffset) || this.canStartTSConstructorType(lookaheadOffset) || this.canStartTSUnionOrIntersectionType(lookaheadOffset));
  }
  canStartTSTypeReference(): any {
    return this.canStartIdentifier(1.0);
  }
  hasArrowAfterBalancedTypeParens(lParenOffset: number): any {
    let open: any = this.safeTypeLookahead(lParenOffset);
    if ((__qin_binary__("==", open, null) || !__QinJavaLangString.equals("LParen", open.tokenName()))) {
      return false;
    }
    let depth: any = 0.0;
    let seenClosingParen: any = false;
    for (let offset: any = lParenOffset; __qin_binary__("<=", offset, 128.0); offset++) {
      let token: any = this.safeTypeLookahead(offset);
      if (__qin_binary__("==", token, null)) {
        return false;
      }
      let tokenName: any = token.tokenName();
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
  safeTypeLookahead(offset: number): any {
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
  canStartTSMappedType(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSMappedType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSMappedType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSMappedType/" + __qin_args.length);
  }
  __qin_overload_canStartTSMappedType_0_0(): any {
    return this.canStartTSMappedType(1.0);
  }
  __qin_overload_canStartTSMappedType_1_1(lookaheadOffset: number): any {
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
  canStartTSTypeLiteral(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypeLiteral_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypeLiteral_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypeLiteral/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypeLiteral_0_0(): any {
    return this.canStartTSTypeLiteral(1.0);
  }
  __qin_overload_canStartTSTypeLiteral_1_1(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("LBrace", this.tokenNameAt(lookaheadOffset)) && !this.canStartTSMappedType(lookaheadOffset));
  }
  canStartTSTypeMember(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypeMember_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypeMember_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypeMember/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypeMember_0_0(): any {
    return this.canStartTSTypeMember(1.0);
  }
  __qin_overload_canStartTSTypeMember_1_1(lookaheadOffset: number): any {
    return (this.canStartTSIndexSignature(lookaheadOffset) || this.canStartTSCallSignatureDeclaration(lookaheadOffset) || this.canStartTSConstructSignatureDeclaration(lookaheadOffset) || this.canStartTSTypeMemberPropertyOrMethodSignature(lookaheadOffset));
  }
  canStartTSIndexSignature(lookaheadOffset: number): any {
    let offset: any = lookaheadOffset;
    if (this.matchIdentifierValue("readonly", offset)) {
      offset++;
    }
    return (__QinJavaLangString.equals("LBracket", this.tokenNameAt(offset)) && this.canStartIdentifier(__qin_binary__("+", offset, 1.0)) && __QinJavaLangString.equals("Colon", this.tokenNameAt(__qin_binary__("+", offset, 2.0))));
  }
  canStartTSCallSignatureDeclaration(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("Less", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LParen", this.tokenNameAt(lookaheadOffset)));
  }
  canStartTSConstructSignatureDeclaration(lookaheadOffset: number): any {
    return __QinJavaLangString.equals("New", this.tokenNameAt(lookaheadOffset));
  }
  canStartTSTypeMemberPropertyOrMethodSignature(lookaheadOffset: number): any {
    return (!this.canStartTSIndexSignature(lookaheadOffset) && !this.canStartTSCallSignatureDeclaration(lookaheadOffset) && !this.canStartTSConstructSignatureDeclaration(lookaheadOffset) && this.canStartTSPropertyOrMethodSignature(lookaheadOffset));
  }
  canStartTSTupleType(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTupleType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTupleType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTupleType/" + __qin_args.length);
  }
  __qin_overload_canStartTSTupleType_0_0(): any {
    return this.canStartTSTupleType(1.0);
  }
  __qin_overload_canStartTSTupleType_1_1(lookaheadOffset: number): any {
    return __QinJavaLangString.equals("LBracket", this.tokenNameAt(lookaheadOffset));
  }
  canStartTSRestType(): any {
    return __QinJavaLangString.equals("Ellipsis", this.tokenNameAt(1.0));
  }
  canStartTSNamedTupleMember(): any {
    return (this.canStartIdentifier(1.0) && __QinJavaLangString.equals("Colon", this.tokenNameAt(2.0)));
  }
  canStartTSTupleElementTypeValue(): any {
    return (this.canStartTSPrimaryType() && !(this.canStartIdentifier(1.0) && (__QinJavaLangString.equals("Question", this.tokenNameAt(2.0)) || __QinJavaLangString.equals("Colon", this.tokenNameAt(2.0)))) && !__QinJavaLangString.equals("Ellipsis", this.tokenNameAt(1.0)));
  }
  canStartTSThisType(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSThisType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSThisType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSThisType/" + __qin_args.length);
  }
  __qin_overload_canStartTSThisType_0_0(): any {
    return this.canStartTSThisType(1.0);
  }
  __qin_overload_canStartTSThisType_1_1(lookaheadOffset: number): any {
    return __QinJavaLangString.equals("This", this.tokenNameAt(lookaheadOffset));
  }
  canStartTSKeywordType(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSKeywordType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSKeywordType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSKeywordType/" + __qin_args.length);
  }
  __qin_overload_canStartTSKeywordType_0_0(): any {
    return this.canStartTSKeywordType(1.0);
  }
  __qin_overload_canStartTSKeywordType_1_1(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("Void", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("NullLiteral", this.tokenNameAt(lookaheadOffset)) || this.matchIdentifierValue("number", lookaheadOffset) || this.matchIdentifierValue("string", lookaheadOffset) || this.matchIdentifierValue("boolean", lookaheadOffset) || this.matchIdentifierValue("any", lookaheadOffset) || this.matchIdentifierValue("unknown", lookaheadOffset) || this.matchIdentifierValue("never", lookaheadOffset) || this.matchIdentifierValue("undefined", lookaheadOffset) || this.matchIdentifierValue("object", lookaheadOffset) || this.matchIdentifierValue("symbol", lookaheadOffset) || this.matchIdentifierValue("bigint", lookaheadOffset));
  }
  canStartTSIntersectionType(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSIntersectionType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSIntersectionType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSIntersectionType/" + __qin_args.length);
  }
  __qin_overload_canStartTSIntersectionType_0_0(): any {
    return this.canStartTSIntersectionType(1.0);
  }
  __qin_overload_canStartTSIntersectionType_1_1(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("BitwiseAnd", this.tokenNameAt(lookaheadOffset)) || this.canStartTSTypeOperand(lookaheadOffset));
  }
  canStartTSLiteralType(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSLiteralType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSLiteralType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSLiteralType/" + __qin_args.length);
  }
  __qin_overload_canStartTSLiteralType_0_0(): any {
    return this.canStartTSLiteralType(1.0);
  }
  __qin_overload_canStartTSLiteralType_1_1(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("NumericLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("StringLiteral", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("True", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("False", this.tokenNameAt(lookaheadOffset)));
  }
  canStartTSPrimaryTypeReference(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSPrimaryTypeReference_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSPrimaryTypeReference_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSPrimaryTypeReference/" + __qin_args.length);
  }
  __qin_overload_canStartTSPrimaryTypeReference_0_0(): any {
    return this.canStartTSPrimaryTypeReference(1.0);
  }
  __qin_overload_canStartTSPrimaryTypeReference_1_1(lookaheadOffset: number): any {
    return (this.canStartIdentifier(lookaheadOffset) && !this.canStartTSKeywordType(lookaheadOffset));
  }
  canStartTSParenthesizedType(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSParenthesizedType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSParenthesizedType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSParenthesizedType/" + __qin_args.length);
  }
  __qin_overload_canStartTSParenthesizedType_0_0(): any {
    return this.canStartTSParenthesizedType(1.0);
  }
  __qin_overload_canStartTSParenthesizedType_1_1(lookaheadOffset: number): any {
    return __QinJavaLangString.equals("LParen", this.tokenNameAt(lookaheadOffset));
  }
  canStartTSTypeStaticRule(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
      return (__qin_binary__("==", lookaheadOffset, 1.0) && this.canStartTSUnionOrIntersectionType());
    }
    if (__QinJavaLangString.equals("TSTypeReference", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && this.canStartTSTypeReference());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSType static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  tstypeStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime(this);
  }
  tsPrimaryTypeStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime(this);
  }
  tsTypeLiteralStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime(this);
  }
  canStartTSSignatureStaticRule(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  tsSignatureStaticRuntime(): any {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsFunctionTypeStaticRuntime(): any {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsConditionalTypeStaticRuntime(): any {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsTupleTypeStaticRuntime(): any {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tstypeReferenceStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime(this);
  }
  tstypeNameStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime(this);
  }
  tstypeNameSuffixStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime(this);
  }
  consumeTSTypeNameSuffixDot(): any {
    this.__qin_field_tokenConsumer.Dot();
    return null;
  }
  tstypeParameterInstantiationStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime(this);
  }
  tsLiteralTypeStaticRuntime(): any {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsPrefixTypeOrPrimaryStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime(this);
  }
  canStartTSPrefixPrimaryFallback(): any {
    return (!this.canStartTSTypeQuery() && !this.canStartTSTypeOperator() && !this.canStartTSInferType() && this.canStartTSPrimaryType());
  }
  tsTypeOperandStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime(this);
  }
  canStartTSPrefixTypeOrPrimary(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSPrefixTypeOrPrimary_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSPrefixTypeOrPrimary_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSPrefixTypeOrPrimary/" + __qin_args.length);
  }
  __qin_overload_canStartTSPrefixTypeOrPrimary_0_0(): any {
    return this.canStartTSPrefixTypeOrPrimary(1.0);
  }
  __qin_overload_canStartTSPrefixTypeOrPrimary_1_1(lookaheadOffset: number): any {
    return (this.canStartTSTypeQuery(lookaheadOffset) || this.canStartTSTypeOperator(lookaheadOffset) || this.canStartTSInferType(lookaheadOffset) || this.canStartTSPrimaryType(lookaheadOffset));
  }
  canStartTSTypeOperand(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypeOperand_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypeOperand_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypeOperand/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypeOperand_0_0(): any {
    return this.canStartTSTypeOperand(1.0);
  }
  __qin_overload_canStartTSTypeOperand_1_1(lookaheadOffset: number): any {
    return this.canStartTSPrefixTypeOrPrimary(lookaheadOffset);
  }
  canStartTSUnionOrIntersectionType(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSUnionOrIntersectionType_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSUnionOrIntersectionType_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSUnionOrIntersectionType/" + __qin_args.length);
  }
  __qin_overload_canStartTSUnionOrIntersectionType_0_0(): any {
    return this.canStartTSUnionOrIntersectionType(1.0);
  }
  __qin_overload_canStartTSUnionOrIntersectionType_1_1(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("BitwiseOr", this.tokenNameAt(lookaheadOffset)) || this.canStartTSIntersectionType(lookaheadOffset) || this.canStartTSTypeOperand(lookaheadOffset));
  }
  tsTypeParameterDeclarationStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime(this);
  }
  tsKeywordTypeStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime(this);
  }
  tsTypeOperatorStaticRuntime(): any {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsInferTypeStaticRuntime(): any {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsUnionOrIntersectionTypeStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime(this);
  }
  tsIntersectionTypeStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime(this);
  }
  tsTypePredicateStaticRuntime(): any {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsPropertyOrMethodSignatureStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime(this);
  }
  tsParameterListStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime(this);
  }
  tsParameterStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime(this);
  }
  tsTypeQueryStaticRuntime(): any {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsTypeAnnotationStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime(this);
  }
  optionalTSTypeAnnotationStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime(this);
  }
  optionalTSTypeParameterDeclarationStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime(this);
  }
  tsTypeParameterStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime(this);
  }
  canStartTSTypeParameter(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_canStartTSTypeParameter_0_0();
    if (__qin_args.length === 1 && typeof __qin_args[0] === "number") return this.__qin_overload_canStartTSTypeParameter_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: canStartTSTypeParameter/" + __qin_args.length);
  }
  __qin_overload_canStartTSTypeParameter_0_0(): any {
    return this.canStartTSTypeParameter(1.0);
  }
  __qin_overload_canStartTSTypeParameter_1_1(lookaheadOffset: number): any {
    return this.canStartIdentifier(lookaheadOffset);
  }
  TSTypeOperator(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeOperator();
    }), "TSTypeOperator", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeOperator(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_OPERATOR_GRAMMAR, "TSTypeOperator", this.tsTypeOperatorStaticRuntime());
    return null;
  }
  TSInferType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSInferType();
    }), "TSInferType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSInferType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_INFER_TYPE_GRAMMAR, "TSInferType", this.tsInferTypeStaticRuntime());
    return null;
  }
  TSRestType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSRestType();
    }), "TSRestType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSRestType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TUPLE_TYPE_GRAMMAR, "TSRestType", this.tsTupleTypeStaticRuntime());
    return null;
  }
  TSTypePredicate(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypePredicate();
    }), "TSTypePredicate", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypePredicate(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_TYPE_PREDICATE_GRAMMAR, "TSTypePredicate", this.tsTypePredicateStaticRuntime());
    return null;
  }
  TSMappedType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSMappedType();
    }), "TSMappedType", "SlimeTSTypeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSMappedType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSTypeParser.__qin_field_STATIC_TS_MAPPED_TYPE_GRAMMAR, "TSMappedType", this.tsMappedTypeStaticRuntime());
    return null;
  }
  tsMappedTypeStaticRuntime(): any {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  canStartTSMappedTypeReadonly(): any {
    return (this.matchIdentifierValue("readonly", 1.0) || (__QinJavaLangString.equals("Plus", this.tokenNameAt(1.0)) && this.matchIdentifierValue("readonly", 2.0)) || (__QinJavaLangString.equals("Minus", this.tokenNameAt(1.0)) && this.matchIdentifierValue("readonly", 2.0)));
  }
  canStartTSMappedTypeTypeParameter(): any {
    return this.canStartIdentifier(1.0);
  }
  canStartTSMappedTypeRemap(): any {
    return this.matchIdentifierValue("as", 1.0);
  }
  canStartTSMappedTypeTypeEnd(): any {
    return (this.canStartIdentifier(1.0) || this.canStartTSType(1.0));
  }
  canStartTSMappedTypeQuestion(): any {
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
  testStaticGate(gateId: string): any {
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
      return this.__qin_field_parser.canStartTSUnionOrIntersectionType();
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSType static gate: " + gateId));
  }
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.__qin_field_parser.canStartTSTypeStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
    return this.__qin_field_parser.canStartTSTypeStaticRule(ruleName, variantKey, lookaheadOffset);
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSType static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypePredicate", ruleName)) {
      this.__qin_field_parser.TSTypePredicate();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSFunctionType", ruleName)) {
      this.__qin_field_parser.TSFunctionType();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSConstructorType", ruleName)) {
      this.__qin_field_parser.TSConstructorType();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSUnionOrIntersectionType", ruleName)) {
      this.__qin_field_parser.TSUnionOrIntersectionType();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeReference", ruleName)) {
      this.__qin_field_parser.TSTypeReference();
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
  testStaticGate(gateId: string): any {
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPrimaryType static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSMappedType", ruleName)) {
      this.__qin_field_parser.TSMappedType();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeLiteral", ruleName)) {
      this.__qin_field_parser.TSTypeLiteral();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTupleType", ruleName)) {
      this.__qin_field_parser.TSTupleType();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSThisType", ruleName)) {
      this.__qin_field_parser.TSThisType();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSKeywordType", ruleName)) {
      this.__qin_field_parser.TSKeywordType();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSLiteralType", ruleName)) {
      this.__qin_field_parser.TSLiteralType();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeReference", ruleName)) {
      this.__qin_field_parser.TSTypeReference();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSParenthesizedType", ruleName)) {
      this.__qin_field_parser.TSParenthesizedType();
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
  testStaticGate(gateId: string): any {
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeLiteral static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeMember", ruleName)) {
      this.__qin_field_parser.TSTypeMember();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSIndexSignature", ruleName)) {
      this.__qin_field_parser.TSIndexSignature();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSCallSignatureDeclaration", ruleName)) {
      this.__qin_field_parser.TSCallSignatureDeclaration();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSConstructSignatureDeclaration", ruleName)) {
      this.__qin_field_parser.TSConstructSignatureDeclaration();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSPropertyOrMethodSignature", ruleName)) {
      this.__qin_field_parser.TSPropertyOrMethodSignature();
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeReference static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeName", ruleName)) {
      this.__qin_field_parser.TSTypeName();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeParameterInstantiation", ruleName)) {
      this.__qin_field_parser.TSTypeParameterInstantiation();
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeName static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      this.__qin_field_parser.Identifier();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeNameSuffix", ruleName)) {
      this.__qin_field_parser.TSTypeNameSuffix();
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeNameSuffix static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Dot", ruleName)) {
      this.__qin_field_parser.consumeTSTypeNameSuffixDot();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      this.__qin_field_parser.Identifier();
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterInstantiation static rule variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      return this.__qin_field_parser.canStartTSType(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterInstantiation static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterInstantiation static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      this.__qin_field_parser.TSType();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterInstantiation static rule call: " + ruleName + "@" + variantKey));
  }
  runStaticAction(actionId: string): any {
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
  testStaticGate(gateId: string): any {
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPrefixTypeOrPrimary static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeQuery", ruleName)) {
      this.__qin_field_parser.TSTypeQuery();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeOperator", ruleName)) {
      this.__qin_field_parser.TSTypeOperator();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSInferType", ruleName)) {
      this.__qin_field_parser.TSInferType();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSPrimaryType", ruleName)) {
      this.__qin_field_parser.TSPrimaryType();
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeOperand static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSPrefixTypeOrPrimary", ruleName)) {
      this.__qin_field_parser.TSPrefixTypeOrPrimary();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      this.__qin_field_parser.TSType();
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterDeclaration static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeParameter", ruleName)) {
      this.__qin_field_parser.TSTypeParameter();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameterDeclaration static rule call: " + ruleName + "@" + variantKey));
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSKeywordType static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSNumberKeyword", ruleName)) {
      this.__qin_field_parser.TSNumberKeyword();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSStringKeyword", ruleName)) {
      this.__qin_field_parser.TSStringKeyword();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSBooleanKeyword", ruleName)) {
      this.__qin_field_parser.TSBooleanKeyword();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSAnyKeyword", ruleName)) {
      this.__qin_field_parser.TSAnyKeyword();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSUnknownKeyword", ruleName)) {
      this.__qin_field_parser.TSUnknownKeyword();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSVoidKeyword", ruleName)) {
      this.__qin_field_parser.TSVoidKeyword();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSNeverKeyword", ruleName)) {
      this.__qin_field_parser.TSNeverKeyword();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSNullKeyword", ruleName)) {
      this.__qin_field_parser.TSNullKeyword();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSUndefinedKeyword", ruleName)) {
      this.__qin_field_parser.TSUndefinedKeyword();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSObjectKeyword", ruleName)) {
      this.__qin_field_parser.TSObjectKeyword();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSSymbolKeyword", ruleName)) {
      this.__qin_field_parser.TSSymbolKeyword();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSBigIntKeyword", ruleName)) {
      this.__qin_field_parser.TSBigIntKeyword();
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSUnionOrIntersectionType static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSIntersectionType", ruleName)) {
      return this.__qin_field_parser.canStartTSIntersectionType(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSUnionOrIntersectionType static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSUnionOrIntersectionType static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSIntersectionType", ruleName)) {
      this.__qin_field_parser.TSIntersectionType();
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSIntersectionType static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeOperand", ruleName)) {
      return this.__qin_field_parser.canStartTSTypeOperand(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSIntersectionType static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSIntersectionType static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeOperand", ruleName)) {
      this.__qin_field_parser.TSTypeOperand();
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
  testStaticGate(gateId: string): any {
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSPropertyOrMethodSignature static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("PropertyName", ruleName)) {
      this.__qin_field_parser.PropertyName(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      this.__qin_field_parser.TSTypeParameterDeclaration();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSParameterList", ruleName)) {
      this.__qin_field_parser.TSParameterList();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeAnnotation", ruleName)) {
      this.__qin_field_parser.TSTypeAnnotation();
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSParameterList static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSParameter", ruleName)) {
      return this.__qin_field_parser.canStartTSParameter(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSParameterList static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSParameterList static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSParameter", ruleName)) {
      this.__qin_field_parser.TSParameter();
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSParameter static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      this.__qin_field_parser.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, false));
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingPattern", ruleName)) {
      this.__qin_field_parser.BindingPattern(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeAnnotation", ruleName)) {
      this.__qin_field_parser.TSTypeAnnotation();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Initializer", ruleName)) {
      this.__qin_field_parser.Initializer(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeAnnotation static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      return this.__qin_field_parser.canStartTSType(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeAnnotation static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeAnnotation static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      this.__qin_field_parser.TSType();
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeAnnotation static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeAnnotation", ruleName)) {
      return __QinJavaLangString.equals("Colon", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeAnnotation static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeAnnotation static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeAnnotation", ruleName)) {
      this.__qin_field_parser.TSTypeAnnotation();
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeParameterDeclaration static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      return __QinJavaLangString.equals("Less", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeParameterDeclaration static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported OptionalTSTypeParameterDeclaration static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      this.__qin_field_parser.TSTypeParameterDeclaration();
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeParameter static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      this.__qin_field_parser.Identifier();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      this.__qin_field_parser.TSType();
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
