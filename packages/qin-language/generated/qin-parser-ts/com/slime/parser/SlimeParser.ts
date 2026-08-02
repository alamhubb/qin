import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "./base/SlimeJavascriptParserBase.ts";
import { com_slime_parser_SlimeParserRuntimeBase, com_slime_parser_SlimeParserRuntimeBase as SlimeParserRuntimeBase, com_slime_parser_SlimeParserRuntimeBase$EmptyStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$EmptyStaticRuntime as EmptyStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootOptionalStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootOptionalStaticRuntime as TSRootOptionalStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootPrimaryStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootPrimaryStaticRuntime as TSRootPrimaryStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootFormalParametersStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootFormalParametersStaticRuntime as TSRootFormalParametersStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootDeclarationStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootDeclarationStaticRuntime as TSRootDeclarationStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootImportSpecifierStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootImportSpecifierStaticRuntime as TSRootImportSpecifierStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootMethodDefinitionStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootMethodDefinitionStaticRuntime as TSRootMethodDefinitionStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$ClassBindingStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$ClassBindingStaticRuntime as ClassBindingStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$RootClassTailStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$RootClassTailStaticRuntime as RootClassTailStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSModifierStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSModifierStaticRuntime as TSModifierStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSAsExpressionTailStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSAsExpressionTailStaticRuntime as TSAsExpressionTailStaticRuntime } from "./SlimeParserRuntimeBase.ts";
import { com_slime_parser_typescript_SlimeTSDeclarationParser, com_slime_parser_typescript_SlimeTSDeclarationParser as SlimeTSDeclarationParser, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime as TSInterfaceDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime as TSInterfaceExtendsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime as TSInterfaceBodyStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime as TSTypeAliasDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime as TSExpressionWithTypeArgumentsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSClassImplementsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSClassImplementsStaticRuntime as TSClassImplementsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime as TSEnumDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumBodyStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumBodyStaticRuntime as TSEnumBodyStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime as TSEnumMemberListStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberStaticRuntime as TSEnumMemberStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime as TSModuleDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleNameStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleNameStaticRuntime as TSModuleNameStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleBlockStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleBlockStaticRuntime as TSModuleBlockStaticRuntime } from "./typescript/SlimeTSDeclarationParser.ts";
import { com_slime_parser_typescript_SlimeTSTypeParser, com_slime_parser_typescript_SlimeTSTypeParser as SlimeTSTypeParser, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime as TSTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime as TSPrimaryTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime as TSTypeLiteralStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime as TSTypeReferenceStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime as TSTypeNameStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime as TSTypeNameSuffixStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime as TSTypeParameterInstantiationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime as TSPrefixTypeOrPrimaryStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime as TSTypeOperandStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime as TSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime as TSKeywordTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime as TSUnionOrIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime as TSIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime as TSPropertyOrMethodSignatureStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime as TSParameterListStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime as TSParameterStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime as TSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime as OptionalTSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime as OptionalTSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime as TSTypeParameterStaticRuntime } from "./typescript/SlimeTSTypeParser.ts";
import { com_slime_parser_SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser as SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser$SourceType, com_slime_parser_SlimeJavascriptParser$SourceType as SourceType, com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime, com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime as JavascriptStaticRuntime } from "./SlimeJavascriptParser.ts";
import { com_slime_parser_module_SlimeModuleParser, com_slime_parser_module_SlimeModuleParser as SlimeModuleParser, com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime, com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime as ModuleStaticRuntime } from "./module/SlimeModuleParser.ts";
import { com_slime_parser_class__SlimeClassParser, com_slime_parser_class__SlimeClassParser as SlimeClassParser, com_slime_parser_class__SlimeClassParser$ClassStaticRuntime, com_slime_parser_class__SlimeClassParser$ClassStaticRuntime as ClassStaticRuntime } from "./class_/SlimeClassParser.ts";
import { com_slime_parser_function_SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser as SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime, com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime as FunctionStaticRuntime } from "./function/SlimeFunctionParser.ts";
import { com_slime_parser_statements_SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser as SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime as StatementRootStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime as StatementLoopStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime as StatementTryStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime as StatementIfStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime as StatementVariableStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime as StatementListStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime as StatementJumpStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime as StatementBranchStaticRuntime } from "./statements/SlimeStatementParser.ts";
import { com_slime_parser_expressions_SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser as SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime as AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime as AssignmentExpressionStaticRuntime } from "./expressions/SlimeAssignmentExpressionParser.ts";
import { com_slime_parser_expressions_SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser as SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime as BinaryStaticRuntime } from "./expressions/SlimeBinaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser as SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime as UnaryStaticRuntime } from "./expressions/SlimeUnaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser as SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime as PrimaryStaticRuntime } from "./expressions/SlimePrimaryExpressionParser.ts";
import { com_slime_parser_literal_SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser as SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime as LiteralStaticRuntime } from "./literal/SlimeLiteralParser.ts";
import { com_slime_parser_identifier_SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser as SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime as IdentifierStaticRuntime } from "./identifier/SlimeIdentifierParser.ts";
import { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser as SubhutiParser, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime as StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticPrefix, com_subhuti_parser_SubhutiParser$StaticPrefix as StaticPrefix, com_subhuti_parser_SubhutiParser$StaticChoice, com_subhuti_parser_SubhutiParser$StaticChoice as StaticChoice } from "../../subhuti/parser/SubhutiParser.ts";
import { com_subhuti_parser_SubhutiParserFinal, com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal } from "../../subhuti/parser/SubhutiParserFinal.ts";
import { com_subhuti_parser_SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators as SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl as StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher as StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext as AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext as AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes as PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates as StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame as ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames as CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys as CurrentTokenKeys } from "../../subhuti/parser/SubhutiParserCombinators.ts";
import { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore as SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments as StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult as RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode as StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks as StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$CacheWork, com_subhuti_parser_SubhutiParserCore$CacheWork as CacheWork, com_subhuti_parser_SubhutiParserCore$FailureWork, com_subhuti_parser_SubhutiParserCore$FailureWork as FailureWork } from "../../subhuti/parser/SubhutiParserCore.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations as ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException as SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException as SubhutiFirstTokenUnknownException } from "../../subhuti/parser/SubhutiParserState.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../../subhuti/lookahead/SubhutiTokenLookahead.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __qin_java_functional } from "@qin/java-sdk-js";
import { __qin_subhuti_rule_cache_key } from "@qin/java-sdk-js/tooling";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const __QinSubhutiCompileOnlyDsl = {
  __fail(name) {
    throw new Error("SubhutiCompileOnlyDsl." + name + " is compile-time-only and cannot execute at runtime");
  },
  Or(..._args) { return this.__fail("Or"); },
  Option(..._args) { return this.__fail("Option"); },
  Many(..._args) { return this.__fail("Many"); },
  AtLeastOne(..._args) { return this.__fail("AtLeastOne"); },
  gate(..._args) { return this.__fail("gate"); }
};
const SubhutiCompileOnlyDsl = __QinSubhutiCompileOnlyDsl;
const RuntimeException = __QinJavaLangRuntimeException;
class com_slime_parser_SlimeParser extends com_slime_parser_SlimeParserRuntimeBase {
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_SlimeParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_SlimeParser_1_0(sourceCode: string): void {
    null;
  }
  OptionalInitializer(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalInitializer(params);
    }), "OptionalInitializer", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_OptionalInitializer(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.Initializer(params);
    }));
      return null;
    }
    super.OptionalInitializer(params);
    return null;
  }
  OptionalBindingIdentifierQuestion(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalBindingIdentifierQuestion();
    }), "OptionalBindingIdentifierQuestion", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_OptionalBindingIdentifierQuestion(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Question();
    }));
      return null;
    }
    super.OptionalBindingIdentifierQuestion();
    return null;
  }
  OptionalTSPropertyMarker(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalTSPropertyMarker();
    }), "OptionalTSPropertyMarker", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_OptionalTSPropertyMarker(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Question();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.LogicalNot();
    }));
    }));
      return null;
    }
    super.OptionalTSPropertyMarker();
    return null;
  }
  OptionalTSTypeParameterInstantiation(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalTSTypeParameterInstantiation();
    }), "OptionalTSTypeParameterInstantiation", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_OptionalTSTypeParameterInstantiation(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.TSTypeParameterInstantiation();
    }));
      return null;
    }
    super.OptionalTSTypeParameterInstantiation();
    return null;
  }
  OptionalWithClause(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalWithClause();
    }), "OptionalWithClause", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_OptionalWithClause(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.WithClause();
    }));
      return null;
    }
    super.OptionalWithClause();
    return null;
  }
  OptionalTSTypeAnnotation(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalTSTypeAnnotation();
    }), "OptionalTSTypeAnnotation", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_OptionalTSTypeAnnotation(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      return null;
    }
    super.OptionalTSTypeAnnotation();
    return null;
  }
  OptionalTSDefiniteAssignmentAssertion(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalTSDefiniteAssignmentAssertion();
    }), "OptionalTSDefiniteAssignmentAssertion", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_OptionalTSDefiniteAssignmentAssertion(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.LogicalNot();
    }));
      return null;
    }
    super.OptionalTSDefiniteAssignmentAssertion();
    return null;
  }
  BindingIdentifier(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingIdentifier(params);
    }), "BindingIdentifier", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingIdentifier(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      super.__qin_subhuti_raw_BindingIdentifier(params);
      {
        const __qin_typed_receiver_1684: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1684.OptionalBindingIdentifierQuestion();
      }
      {
        const __qin_typed_receiver_1685: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1685.OptionalTSTypeAnnotation();
      }
      return null;
    }
    super.__qin_subhuti_raw_BindingIdentifier(params);
    return null;
  }
  OptionalTSDecorators(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalTSDecorators();
    }), "OptionalTSDecorators", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_OptionalTSDecorators(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.TSDecorators();
    }));
      return null;
    }
    super.OptionalTSDecorators();
    return null;
  }
  OptionalTSAbstractModifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalTSAbstractModifier();
    }), "OptionalTSAbstractModifier", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_OptionalTSAbstractModifier(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.TSAbstractModifier();
    }));
      return null;
    }
    super.OptionalTSAbstractModifier();
    return null;
  }
  ManyTSAccessibilityModifiers(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ManyTSAccessibilityModifiers();
    }), "ManyTSAccessibilityModifiers", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ManyTSAccessibilityModifiers(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.TSAccessibilityModifier();
    }));
      return null;
    }
    super.ManyTSAccessibilityModifiers();
    return null;
  }
  ManyTSClassMethodModifiers(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ManyTSClassMethodModifiers();
    }), "ManyTSClassMethodModifiers", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ManyTSClassMethodModifiers(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.TSAbstractModifier();
    }), __qin_java_functional(() => {
      return this.TSAccessibilityModifier();
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("static");
    }));
      return null;
    }));
      return null;
    }
    super.ManyTSClassMethodModifiers();
    return null;
  }
  Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Declaration(params);
    }), "Declaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.canStartTSInterfaceDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSInterfaceDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.canStartTSTypeAliasDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSTypeAliasDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.canStartTSEnumDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSEnumDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.canStartTSModuleDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSModuleDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.canStartTSDeclareStatement.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSDeclareStatement();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.declarationStandardStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.StandardDeclaration(params);
    })));
      return null;
    }
    super.__qin_subhuti_raw_Declaration(params);
    return null;
  }
  StandardDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_StandardDeclaration(params);
    }), "StandardDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_StandardDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      super.__qin_subhuti_raw_Declaration(params);
      return null;
    }
    super.__qin_subhuti_raw_StandardDeclaration(params);
    return null;
  }
  VariableDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_VariableDeclaration(params);
    }), "VariableDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_VariableDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1686: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1686.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
      }
      {
        const __qin_typed_receiver_1687: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1687.OptionalTSDefiniteAssignmentAssertion();
      }
      {
        const __qin_typed_receiver_1688: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1688.OptionalTSTypeAnnotation();
      }
      {
        const __qin_typed_receiver_1689: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1689.OptionalInitializer(params);
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1690: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1690.BindingPattern(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
      }
      {
        const __qin_typed_receiver_1691: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1691.OptionalTSTypeAnnotation();
      }
      {
        const __qin_typed_receiver_1692: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1692.Initializer(params);
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_VariableDeclaration(params);
    return null;
  }
  LexicalBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_LexicalBinding(params);
    }), "LexicalBinding", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LexicalBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1693: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1693.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
      }
      {
        const __qin_typed_receiver_1694: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1694.OptionalTSDefiniteAssignmentAssertion();
      }
      {
        const __qin_typed_receiver_1695: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1695.OptionalTSTypeAnnotation();
      }
      {
        const __qin_typed_receiver_1696: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1696.OptionalInitializer(params);
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1697: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1697.BindingPattern(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
      }
      {
        const __qin_typed_receiver_1698: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1698.OptionalTSTypeAnnotation();
      }
      {
        const __qin_typed_receiver_1699: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1699.Initializer(params);
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_LexicalBinding(params);
    return null;
  }
  ClassElement(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassElement(params);
    }), "ClassElement", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassElement(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      let elementNameParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams = new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await());
      {
        const __qin_typed_receiver_1700: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1700.OptionalTSDecorators();
      }
      SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.classElementSemicolonStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Semicolon();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.classElementTsClassMethodSignatureStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSClassMethodSignature(elementNameParams);
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.classElementTsClassAbstractPropertySignatureStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSClassAbstractPropertySignature(elementNameParams);
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.classElementMethodStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1701: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1701.OptionalTSAbstractModifier();
      }
      {
        const __qin_typed_receiver_1702: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1702.ManyTSAccessibilityModifiers();
      }
      {
        const __qin_typed_receiver_1703: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1703.MethodDefinition(elementNameParams);
      }
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.classElementStaticMethodStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1704: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1704.OptionalTSAbstractModifier();
      }
      {
        const __qin_typed_receiver_1705: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1705.ManyTSAccessibilityModifiers();
      }
      {
        const __qin_typed_receiver_1706: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1706.IdentifierName();
      }
      {
        const __qin_typed_receiver_1707: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1707.MethodDefinition(elementNameParams);
      }
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.classElementFieldStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1708: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1708.OptionalTSAbstractModifier();
      }
      {
        const __qin_typed_receiver_1709: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1709.ManyTSAccessibilityModifiers();
      }
      {
        const __qin_typed_receiver_1710: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1710.FieldDefinition(params);
      }
      {
        const __qin_typed_receiver_1711: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1711.SemicolonASI();
      }
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.classElementStaticFieldStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1712: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1712.OptionalTSAbstractModifier();
      }
      {
        const __qin_typed_receiver_1713: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1713.ManyTSAccessibilityModifiers();
      }
      {
        const __qin_typed_receiver_1714: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1714.IdentifierName();
      }
      {
        const __qin_typed_receiver_1715: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1715.FieldDefinition(params);
      }
      {
        const __qin_typed_receiver_1716: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1716.SemicolonASI();
      }
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.classElementStaticBlockStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1717: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1717.OptionalTSAbstractModifier();
      }
      {
        const __qin_typed_receiver_1718: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1718.ManyTSAccessibilityModifiers();
      }
      {
        const __qin_typed_receiver_1719: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1719.ClassStaticBlock(params);
      }
      return null;
    })));
      return null;
    }
    super.__qin_subhuti_raw_ClassElement(params);
    return null;
  }
  MethodDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_MethodDefinition(params);
    }), "MethodDefinition", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_MethodDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.methodDefinitionGeneratorStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.GeneratorMethod(params);
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.methodDefinitionAsyncGeneratorStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.AsyncGeneratorMethod(params);
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.methodDefinitionAsyncStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.AsyncMethod(params);
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.methodDefinitionGetterStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1720: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1720.IdentifierName();
      }
      {
        const __qin_typed_receiver_1721: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1721.ClassElementName(params);
      }
      {
        const __qin_typed_receiver_1722: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1722.LParen();
      }
      {
        const __qin_typed_receiver_1723: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1723.RParen();
      }
      {
        const __qin_typed_receiver_1724: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1724.LBrace();
      }
      {
        const __qin_typed_receiver_1725: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1725.FunctionBody();
      }
      {
        const __qin_typed_receiver_1726: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1726.RBrace();
      }
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.methodDefinitionSetterStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1727: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1727.IdentifierName();
      }
      {
        const __qin_typed_receiver_1728: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1728.ClassElementName(params);
      }
      {
        const __qin_typed_receiver_1729: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1729.LParen();
      }
      {
        const __qin_typed_receiver_1730: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1730.PropertySetParameterList();
      }
      {
        const __qin_typed_receiver_1731: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1731.RParen();
      }
      {
        const __qin_typed_receiver_1732: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1732.LBrace();
      }
      {
        const __qin_typed_receiver_1733: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1733.FunctionBody();
      }
      {
        const __qin_typed_receiver_1734: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1734.RBrace();
      }
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.methodDefinitionOrdinaryStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1735: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1735.ClassElementName(params);
      }
      {
        const __qin_typed_receiver_1736: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1736.LParen();
      }
      {
        const __qin_typed_receiver_1737: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1737.UniqueFormalParameters();
      }
      {
        const __qin_typed_receiver_1738: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1738.RParen();
      }
      {
        const __qin_typed_receiver_1739: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1739.LBrace();
      }
      {
        const __qin_typed_receiver_1740: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1740.FunctionBody();
      }
      {
        const __qin_typed_receiver_1741: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1741.RBrace();
      }
      return null;
    })));
      return null;
    }
    super.__qin_subhuti_raw_MethodDefinition(params);
    return null;
  }
  classElementSemicolonStart(): boolean {
    return __QinJavaLangString.equals("Semicolon", this.tokenNameAt(1.0));
  }
  classElementTsClassMethodSignatureStart(): boolean {
    return this.canStartClassMethodSignatureAt(1.0);
  }
  classElementTsClassAbstractPropertySignatureStart(): boolean {
    return this.canStartClassAbstractPropertySignatureAt(1.0);
  }
  classElementMethodStart(): boolean {
    return this.canStartClassElementMethodAt(1.0);
  }
  classElementStaticMethodStart(): boolean {
    return (!this.classElementMethodStart() && this.isIdentifierValueAt(1.0, "static") && this.canStartClassElementMethodAt(2.0));
  }
  classElementFieldStart(): boolean {
    return (!this.classElementMethodStart() && !this.classElementStaticMethodStart() && this.canStartClassFieldElementAt(1.0));
  }
  classElementStaticFieldStart(): boolean {
    return (!this.classElementMethodStart() && !this.classElementStaticMethodStart() && !this.classElementFieldStart() && this.isIdentifierValueAt(1.0, "static") && this.canStartClassFieldElementAt(2.0));
  }
  classElementStaticBlockStart(): boolean {
    return (!this.classElementMethodStart() && !this.classElementStaticMethodStart() && !this.classElementFieldStart() && !this.classElementStaticFieldStart() && this.isIdentifierValueAt(1.0, "static") && __QinJavaLangString.equals("LBrace", this.tokenNameAt(2.0)));
  }
  canStartClassMethodSignatureAt(lookaheadOffset: number): boolean {
    let nameOffset: number = this.skipTSClassMethodModifiers(lookaheadOffset);
    let lParenOffset: number = this.classElementNameCallLParenOffset(nameOffset);
    if (__qin_binary__("<", lParenOffset, 0.0)) {
      return false;
    }
    let rParenOffset: number = this.matchingBalancedOffset(lParenOffset, "LParen", "RParen");
    if (__qin_binary__("<", rParenOffset, 0.0)) {
      return false;
    }
    return this.isSignatureTerminatorBeforeBody(__qin_binary__("+", rParenOffset, 1.0));
  }
  canStartClassAbstractPropertySignatureAt(lookaheadOffset: number): boolean {
    let offset: number = this.skipTSAccessibilityModifiers(lookaheadOffset);
    if ((!this.isIdentifierValueAt(offset, "abstract"))) {
      return false;
    }
    offset++;
    offset = this.skipTSAccessibilityModifiers(offset);
    if (__qin_binary__(">=", this.classElementNameCallLParenOffset(offset), 0.0)) {
      return false;
    }
    let nameEndOffset: number = this.classElementNameEndOffset(offset);
    if (__qin_binary__("<", nameEndOffset, 0.0)) {
      return false;
    }
    return this.canStartClassPropertyTailAt(__qin_binary__("+", nameEndOffset, 1.0));
  }
  skipTSClassMethodModifiers(lookaheadOffset: number): number {
    let offset: number = lookaheadOffset;
    let advanced: boolean = true;
    while (advanced) {
      advanced = false;
      if ((this.isIdentifierValueAt(offset, "abstract") || this.isClassContextualModifierAt(offset) || this.isIdentifierValueAt(offset, "static"))) {
        offset++;
        advanced = true;
      }
    }
    return offset;
  }
  skipTSAccessibilityModifiers(lookaheadOffset: number): number {
    let offset: number = lookaheadOffset;
    while (this.isClassContextualModifierAt(offset)) {
      offset++;
    }
    return offset;
  }
  canStartClassElementMethodAt(lookaheadOffset: number): boolean {
    return this.canStartClassMethodDefinitionAt(lookaheadOffset);
  }
  canStartClassFieldElementAt(lookaheadOffset: number): boolean {
    if (__qin_binary__(">=", this.classElementNameCallLParenOffset(lookaheadOffset), 0.0)) {
      return false;
    }
    let nameEndOffset: number = this.classElementNameEndOffset(lookaheadOffset);
    if (__qin_binary__("<", nameEndOffset, 0.0)) {
      return false;
    }
    return this.canStartClassPropertyTailAt(__qin_binary__("+", nameEndOffset, 1.0));
  }
  canStartClassPropertyTailAt(lookaheadOffset: number): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return true;
    }
    if ((__QinJavaLangString.equals("Assign", tokenName) || __QinJavaLangString.equals("Semicolon", tokenName) || __QinJavaLangString.equals("RBrace", tokenName) || __QinJavaLangString.equals("Colon", tokenName) || __QinJavaLangString.equals("Question", tokenName) || __QinJavaLangString.equals("LogicalNot", tokenName))) {
      return true;
    }
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    return (__qin_binary__("!=", token, null) && token.hasLineBreakBefore());
  }
  canStartClassMethodDefinitionAt(lookaheadOffset: number): boolean {
    return (this.canStartClassGeneratorMethodAt(lookaheadOffset) || this.canStartClassAsyncGeneratorMethodAt(lookaheadOffset) || this.canStartClassAsyncMethodAt(lookaheadOffset) || this.canStartClassGetterMethodAt(lookaheadOffset) || this.canStartClassSetterMethodAt(lookaheadOffset) || this.canStartClassOrdinaryMethodAt(lookaheadOffset));
  }
  canStartClassGeneratorMethodAt(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Asterisk", this.tokenNameAt(lookaheadOffset)) && __qin_binary__(">=", this.classElementNameCallLParenOffset(__qin_binary__("+", lookaheadOffset, 1.0)), 0.0));
  }
  canStartClassAsyncGeneratorMethodAt(lookaheadOffset: number): boolean {
    return (this.isIdentifierValueAt(lookaheadOffset, "async") && !this.hasSourceLineBreakBefore(__qin_binary__("+", lookaheadOffset, 1.0)) && __QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && __qin_binary__(">=", this.classElementNameCallLParenOffset(__qin_binary__("+", lookaheadOffset, 2.0)), 0.0));
  }
  canStartClassAsyncMethodAt(lookaheadOffset: number): boolean {
    return (this.isIdentifierValueAt(lookaheadOffset, "async") && !this.hasSourceLineBreakBefore(__qin_binary__("+", lookaheadOffset, 1.0)) && !__QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && __qin_binary__(">=", this.classElementNameCallLParenOffset(__qin_binary__("+", lookaheadOffset, 1.0)), 0.0));
  }
  canStartClassGetterMethodAt(lookaheadOffset: number): boolean {
    return (!this.canStartClassGeneratorMethodAt(lookaheadOffset) && !this.canStartClassAsyncGeneratorMethodAt(lookaheadOffset) && !this.canStartClassAsyncMethodAt(lookaheadOffset) && this.isIdentifierValueAt(lookaheadOffset, "get") && __qin_binary__(">=", this.classElementNameCallLParenOffset(__qin_binary__("+", lookaheadOffset, 1.0)), 0.0));
  }
  canStartClassSetterMethodAt(lookaheadOffset: number): boolean {
    return (!this.canStartClassGeneratorMethodAt(lookaheadOffset) && !this.canStartClassAsyncGeneratorMethodAt(lookaheadOffset) && !this.canStartClassAsyncMethodAt(lookaheadOffset) && !this.canStartClassGetterMethodAt(lookaheadOffset) && this.isIdentifierValueAt(lookaheadOffset, "set") && __qin_binary__(">=", this.classElementNameCallLParenOffset(__qin_binary__("+", lookaheadOffset, 1.0)), 0.0));
  }
  canStartClassOrdinaryMethodAt(lookaheadOffset: number): boolean {
    return (!this.canStartClassGeneratorMethodAt(lookaheadOffset) && !this.canStartClassAsyncGeneratorMethodAt(lookaheadOffset) && !this.canStartClassAsyncMethodAt(lookaheadOffset) && !this.canStartClassGetterMethodAt(lookaheadOffset) && !this.canStartClassSetterMethodAt(lookaheadOffset) && __qin_binary__(">=", this.classElementNameCallLParenOffset(lookaheadOffset), 0.0));
  }
  classElementNameCallLParenOffset(lookaheadOffset: number): number {
    let nameEndOffset: number = this.classElementNameEndOffset(lookaheadOffset);
    if (__qin_binary__("<", nameEndOffset, 0.0)) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    let nextOffset: number = __qin_binary__("+", nameEndOffset, 1.0);
    if (__QinJavaLangString.equals("Less", this.tokenNameAt(nextOffset))) {
      nextOffset = this.matchingBalancedOffset(nextOffset, "Less", "Greater");
      if (__qin_binary__("<", nextOffset, 0.0)) {
        return __qin_binary__("-", 0.0, 1.0);
      }
      nextOffset++;
    }
    if (__QinJavaLangString.equals("LParen", this.tokenNameAt(nextOffset))) {
      return nextOffset;
    }
    return __qin_binary__("-", 0.0, 1.0);
  }
  classElementNameEndOffset(lookaheadOffset: number): number {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if ((__QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || __QinJavaLangString.equals("PrivateIdentifier", tokenName))) {
      return lookaheadOffset;
    }
    if ((!__QinJavaLangString.equals("LBracket", tokenName))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    return this.matchingBalancedOffset(lookaheadOffset, "LBracket", "RBracket");
  }
  matchingBalancedOffset(openOffset: number, openTokenName: string, closeTokenName: string): number {
    if ((!__QinJavaLangString.equals(openTokenName, this.tokenNameAt(openOffset)))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    let depth: number = 1.0;
    let offset: number = __qin_binary__("+", openOffset, 1.0);
    while (__qin_binary__("!=", this.tokenNameAt(offset), null)) {
      let tokenName: string = this.tokenNameAt(offset);
      if (__QinJavaLangString.equals(openTokenName, tokenName)) {
        depth++;
      } else {
        if (__QinJavaLangString.equals(closeTokenName, tokenName)) {
          depth--;
          if (__qin_binary__("==", depth, 0.0)) {
            return offset;
          }
        }
      }
      offset++;
    }
    return __qin_binary__("-", 0.0, 1.0);
  }
  isSignatureTerminatorBeforeBody(lookaheadOffset: number): boolean {
    let offset: number = lookaheadOffset;
    if (__QinJavaLangString.equals("Colon", this.tokenNameAt(offset))) {
      offset++;
      let braceDepth: number = 0.0;
      let bracketDepth: number = 0.0;
      let parenDepth: number = 0.0;
      let angleDepth: number = 0.0;
      while (__qin_binary__("!=", this.tokenNameAt(offset), null)) {
        let tokenName: string = this.tokenNameAt(offset);
        if ((__qin_binary__("==", braceDepth, 0.0) && __qin_binary__("==", bracketDepth, 0.0) && __qin_binary__("==", parenDepth, 0.0) && __qin_binary__("==", angleDepth, 0.0))) {
          if ((__QinJavaLangString.equals("Semicolon", tokenName) || __QinJavaLangString.equals("RBrace", tokenName))) {
            return true;
          }
          if (__QinJavaLangString.equals("LBrace", tokenName)) {
            return false;
          }
          let token: com_subhuti_struct_SubhutiMatchToken = this.LA(offset);
          if ((__qin_binary__("!=", token, null) && token.hasLineBreakBefore())) {
            return true;
          }
        }
        if (__QinJavaLangString.equals("LBrace", tokenName)) {
          braceDepth++;
        } else {
          if (__QinJavaLangString.equals("RBrace", tokenName)) {
            if (__qin_binary__("==", braceDepth, 0.0)) {
              return true;
            }
            braceDepth--;
          } else {
            if (__QinJavaLangString.equals("LBracket", tokenName)) {
              bracketDepth++;
            } else {
              if (__QinJavaLangString.equals("RBracket", tokenName)) {
                if (__qin_binary__(">", bracketDepth, 0.0)) {
                  bracketDepth--;
                }
              } else {
                if (__QinJavaLangString.equals("LParen", tokenName)) {
                  parenDepth++;
                } else {
                  if (__QinJavaLangString.equals("RParen", tokenName)) {
                    if (__qin_binary__(">", parenDepth, 0.0)) {
                      parenDepth--;
                    }
                  } else {
                    if (__QinJavaLangString.equals("Less", tokenName)) {
                      angleDepth++;
                    } else {
                      if (__QinJavaLangString.equals("Greater", tokenName)) {
                        if (__qin_binary__(">", angleDepth, 0.0)) {
                          angleDepth--;
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        offset++;
      }
      return true;
    }
    if ((__QinJavaLangString.equals("Semicolon", this.tokenNameAt(offset)) || __QinJavaLangString.equals("RBrace", this.tokenNameAt(offset)) || __qin_binary__("==", this.tokenNameAt(offset), null))) {
      return true;
    }
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(offset);
    return (__qin_binary__("!=", token, null) && token.hasLineBreakBefore());
  }
  isClassContextualModifierAt(lookaheadOffset: number): boolean {
    return (this.isIdentifierValueAt(lookaheadOffset, "public") || this.isIdentifierValueAt(lookaheadOffset, "private") || this.isIdentifierValueAt(lookaheadOffset, "protected") || this.isIdentifierValueAt(lookaheadOffset, "readonly") || this.isIdentifierValueAt(lookaheadOffset, "declare") || this.isIdentifierValueAt(lookaheadOffset, "override"));
  }
  isIdentifierValueAt(lookaheadOffset: number, value: string): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("IdentifierName", this.LA(lookaheadOffset).tokenName()) && __QinJavaLangString.equals(value, this.LA(lookaheadOffset).value()));
  }
  IterationStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_IterationStatement(params);
    }), "IterationStatement", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_IterationStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.iterationStatementDoWhileStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.DoWhileStatement(params);
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.iterationStatementWhileStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.WhileStatement(params);
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.iterationStatementForInOfStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.ForInOfStatement(params);
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.iterationStatementForStatementStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.ForStatement(params);
    })));
      return null;
    }
    super.__qin_subhuti_raw_IterationStatement(params);
    return null;
  }
  ClassElementName(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassElementName(params);
    }), "ClassElementName", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassElementName(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.PropertyName(params);
    }), __qin_java_functional(() => {
      return this.PrivateIdentifier();
    }));
      return null;
    }
    super.__qin_subhuti_raw_ClassElementName(params);
    return null;
  }
  ClassBody(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassBody(params);
    }), "ClassBody", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassBody(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1742: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1742.ClassElementList(params);
      }
      return null;
    }
    super.__qin_subhuti_raw_ClassBody(params);
    return null;
  }
  ClassElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassElementList(params);
    }), "ClassElementList", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1743: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1743.ClassElement(params);
      }
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.ClassElement(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw_ClassElementList(params);
    return null;
  }
  ClassTail(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassTail(params);
    }), "ClassTail", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassTail(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.ClassHeritage(params);
    }));
      {
        const __qin_typed_receiver_1744: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1744.LBrace();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.ClassBody(params);
    }));
      {
        const __qin_typed_receiver_1745: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1745.RBrace();
      }
      return null;
    }
    super.__qin_subhuti_raw_ClassTail(params);
    return null;
  }
  ClassStaticBlock(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassStaticBlock(params);
    }), "ClassStaticBlock", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassStaticBlock(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1746: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1746.consumeIdentifierValue("static");
      }
      {
        const __qin_typed_receiver_1747: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1747.LBrace();
      }
      {
        const __qin_typed_receiver_1748: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1748.ClassStaticBlockBody();
      }
      {
        const __qin_typed_receiver_1749: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1749.RBrace();
      }
      return null;
    }
    super.__qin_subhuti_raw_ClassStaticBlock(params);
    return null;
  }
  ClassStaticBlockBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassStaticBlockBody();
    }), "ClassStaticBlockBody", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ClassStaticBlockBody(): void {
    if (false) {
      {
        const __qin_typed_receiver_1750: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1750.ClassStaticBlockStatementList();
      }
      return null;
    }
    super.__qin_subhuti_raw_ClassStaticBlockBody();
    return null;
  }
  ClassStaticBlockStatementList(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassStaticBlockStatementList();
    }), "ClassStaticBlockStatementList", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ClassStaticBlockStatementList(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.StatementList(new com_slime_parser_base_SlimeJavascriptParserBase$StatementParams(false, true, false));
    }));
      return null;
    }
    super.__qin_subhuti_raw_ClassStaticBlockStatementList();
    return null;
  }
  FieldDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FieldDefinition(params);
    }), "FieldDefinition", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_FieldDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1751: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1751.ClassElementName(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
      }
      {
        const __qin_typed_receiver_1752: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1752.OptionalTSTypeAnnotation();
      }
      {
        const __qin_typed_receiver_1753: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1753.OptionalInitializer(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, false, false));
      }
      return null;
    }
    super.__qin_subhuti_raw_FieldDefinition(params);
    return null;
  }
  FormalParameter(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FormalParameter_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FormalParameter_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FormalParameter/" + __qin_args.length);
  }
  __qin_overload_FormalParameter_0_0(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FormalParameter_0_0();
    }), "FormalParameter", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameter_0_0(): void {
    if (false) {
      {
        const __qin_typed_receiver_1754: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1754.OptionalTSDecorators();
      }
      SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.canStartTSParameterProperty.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSParameterProperty();
    })), __qin_java_functional(() => {
      return this.BindingElement();
    }));
      return null;
    }
    super.__qin_subhuti_raw___qin_overload_FormalParameter_0_0();
    return null;
  }
  __qin_overload_FormalParameter_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FormalParameter_1_1(params);
    }), "FormalParameter", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameter_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1755: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1755.OptionalTSDecorators();
      }
      SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.canStartTSParameterProperty.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSParameterProperty();
    })), __qin_java_functional(() => {
      return this.BindingElement(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw___qin_overload_FormalParameter_1_1(params);
    return null;
  }
  FormalParameters(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FormalParameters_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FormalParameters_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FormalParameters/" + __qin_args.length);
  }
  __qin_overload_FormalParameters_0_0(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FormalParameters_0_0();
    }), "FormalParameters", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameters_0_0(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1756: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1756.TSThisParameter();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1757: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1757.Comma();
      }
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1758: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1758.FormalParameterList();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1759: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1759.Comma();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.FunctionRestParameter();
    }));
      return null;
    }));
      return null;
    }), __qin_java_functional(() => {
      return this.FunctionRestParameter();
    }));
      return null;
    }));
      return null;
    }), __qin_java_functional(() => {
      return this.StandardFormalParameters();
    }));
      return null;
    }
    super.__qin_subhuti_raw_FormalParameters();
    return null;
  }
  __qin_overload_FormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FormalParameters_1_1(params);
    }), "FormalParameters", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1760: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1760.TSThisParameter();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1761: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1761.Comma();
      }
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1762: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1762.FormalParameterList(params);
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1763: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1763.Comma();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.FunctionRestParameter(params);
    }));
      return null;
    }));
      return null;
    }), __qin_java_functional(() => {
      return this.FunctionRestParameter(params);
    }));
      return null;
    }));
      return null;
    }), __qin_java_functional(() => {
      return this.StandardFormalParameters();
    }));
      return null;
    }
    super.__qin_subhuti_raw___qin_overload_FormalParameters_1_1(params);
    return null;
  }
  UniqueFormalParameters(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_UniqueFormalParameters_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_UniqueFormalParameters_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: UniqueFormalParameters/" + __qin_args.length);
  }
  __qin_overload_UniqueFormalParameters_0_0(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_UniqueFormalParameters_0_0();
    }), "UniqueFormalParameters", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_UniqueFormalParameters_0_0(): void {
    if (false) {
      {
        const __qin_typed_receiver_1764: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1764.FormalParameters();
      }
      return null;
    }
    super.__qin_subhuti_raw___qin_overload_UniqueFormalParameters_0_0();
    return null;
  }
  __qin_overload_UniqueFormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_UniqueFormalParameters_1_1(params);
    }), "UniqueFormalParameters", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_UniqueFormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1765: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1765.FormalParameters(params);
      }
      return null;
    }
    super.__qin_subhuti_raw___qin_overload_UniqueFormalParameters_1_1(params);
    return null;
  }
  FormalParameterList(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FormalParameterList_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FormalParameterList_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FormalParameterList/" + __qin_args.length);
  }
  __qin_overload_FormalParameterList_0_0(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FormalParameterList_0_0();
    }), "FormalParameterList", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameterList_0_0(): void {
    if (false) {
      {
        const __qin_typed_receiver_1766: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1766.FormalParameter();
      }
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1767: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1767.Comma();
      }
      {
        const __qin_typed_receiver_1768: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1768.FormalParameter();
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw___qin_overload_FormalParameterList_0_0();
    return null;
  }
  __qin_overload_FormalParameterList_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FormalParameterList_1_1(params);
    }), "FormalParameterList", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FormalParameterList_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1769: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1769.FormalParameter(params);
      }
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1770: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1770.Comma();
      }
      {
        const __qin_typed_receiver_1771: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1771.FormalParameter(params);
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw___qin_overload_FormalParameterList_1_1(params);
    return null;
  }
  FunctionRestParameter(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FunctionRestParameter_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FunctionRestParameter_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FunctionRestParameter/" + __qin_args.length);
  }
  __qin_overload_FunctionRestParameter_0_0(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FunctionRestParameter_0_0();
    }), "FunctionRestParameter", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FunctionRestParameter_0_0(): void {
    if (false) {
      {
        const __qin_typed_receiver_1772: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1772.Ellipsis();
      }
      {
        const __qin_typed_receiver_1773: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1773.BindingRestElement();
      }
      return null;
    }
    super.__qin_subhuti_raw___qin_overload_FunctionRestParameter_0_0();
    return null;
  }
  __qin_overload_FunctionRestParameter_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FunctionRestParameter_1_1(params);
    }), "FunctionRestParameter", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FunctionRestParameter_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1774: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1774.Ellipsis();
      }
      {
        const __qin_typed_receiver_1775: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1775.BindingRestElement(params);
      }
      return null;
    }
    super.__qin_subhuti_raw___qin_overload_FunctionRestParameter_1_1(params);
    return null;
  }
  OptionalFunctionBindingIdentifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalFunctionBindingIdentifier();
    }), "OptionalFunctionBindingIdentifier", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_OptionalFunctionBindingIdentifier(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.BindingIdentifier();
    }));
      return null;
    }
    {
      const __qin_typed_receiver_1776: com_slime_parser_SlimeParser = this;
      __qin_typed_receiver_1776.BindingIdentifier();
    }
    return null;
  }
  RequiredFunctionBindingIdentifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_RequiredFunctionBindingIdentifier();
    }), "RequiredFunctionBindingIdentifier", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_RequiredFunctionBindingIdentifier(): void {
    if (false) {
      {
        const __qin_typed_receiver_1777: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1777.BindingIdentifier();
      }
      return null;
    }
    {
      const __qin_typed_receiver_1778: com_slime_parser_SlimeParser = this;
      __qin_typed_receiver_1778.BindingIdentifier();
    }
    return null;
  }
  FunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FunctionDeclaration(params);
    }), "FunctionDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_FunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1779: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1779.Function();
      }
      if (params.isDefault()) {
        {
          const __qin_typed_receiver_1780: com_slime_parser_SlimeParser = this;
          __qin_typed_receiver_1780.OptionalFunctionBindingIdentifier();
        }
      } else {
        {
          const __qin_typed_receiver_1781: com_slime_parser_SlimeParser = this;
          __qin_typed_receiver_1781.RequiredFunctionBindingIdentifier();
        }
      }
      {
        const __qin_typed_receiver_1782: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1782.LParen();
      }
      {
        const __qin_typed_receiver_1783: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1783.FormalParameters();
      }
      {
        const __qin_typed_receiver_1784: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1784.RParen();
      }
      {
        const __qin_typed_receiver_1785: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1785.LBrace();
      }
      {
        const __qin_typed_receiver_1786: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1786.FunctionBody();
      }
      {
        const __qin_typed_receiver_1787: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1787.RBrace();
      }
      return null;
    }
    super.__qin_subhuti_raw_FunctionDeclaration(params);
    return null;
  }
  FunctionExpression(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FunctionExpression();
    }), "FunctionExpression", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_FunctionExpression(): void {
    if (false) {
      {
        const __qin_typed_receiver_1788: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1788.Function();
      }
      {
        const __qin_typed_receiver_1789: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1789.OptionalFunctionBindingIdentifier();
      }
      {
        const __qin_typed_receiver_1790: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1790.LParen();
      }
      {
        const __qin_typed_receiver_1791: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1791.FormalParameters();
      }
      {
        const __qin_typed_receiver_1792: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1792.RParen();
      }
      {
        const __qin_typed_receiver_1793: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1793.LBrace();
      }
      {
        const __qin_typed_receiver_1794: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1794.FunctionBody();
      }
      {
        const __qin_typed_receiver_1795: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1795.RBrace();
      }
      return null;
    }
    super.__qin_subhuti_raw_FunctionExpression();
    return null;
  }
  FunctionBody(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_FunctionBody_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_FunctionBody_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: FunctionBody/" + __qin_args.length);
  }
  __qin_overload_FunctionBody_0_0(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FunctionBody_0_0();
    }), "FunctionBody", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_FunctionBody_0_0(): void {
    {
      const __qin_typed_receiver_1796: com_slime_parser_SlimeParser = this;
      __qin_typed_receiver_1796.FunctionStatementList(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, false));
    }
    return null;
  }
  __qin_overload_FunctionBody_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_FunctionBody_1_1(params);
    }), "FunctionBody", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_FunctionBody_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_1797: com_slime_parser_SlimeParser = this;
      __qin_typed_receiver_1797.FunctionStatementList(params);
    }
    return null;
  }
  FunctionStatementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FunctionStatementList(params);
    }), "FunctionStatementList", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_FunctionStatementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      super.__qin_subhuti_raw_FunctionStatementList(params);
      return null;
    }
    super.__qin_subhuti_raw_FunctionStatementList(params);
    return null;
  }
  GeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_GeneratorDeclaration(params);
    }), "GeneratorDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_GeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1798: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1798.Function();
      }
      {
        const __qin_typed_receiver_1799: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1799.Asterisk();
      }
      if (params.isDefault()) {
        {
          const __qin_typed_receiver_1800: com_slime_parser_SlimeParser = this;
          __qin_typed_receiver_1800.OptionalFunctionBindingIdentifier();
        }
      } else {
        {
          const __qin_typed_receiver_1801: com_slime_parser_SlimeParser = this;
          __qin_typed_receiver_1801.RequiredFunctionBindingIdentifier();
        }
      }
      {
        const __qin_typed_receiver_1802: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1802.LParen();
      }
      {
        const __qin_typed_receiver_1803: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1803.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, false));
      }
      {
        const __qin_typed_receiver_1804: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1804.RParen();
      }
      {
        const __qin_typed_receiver_1805: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1805.LBrace();
      }
      {
        const __qin_typed_receiver_1806: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1806.GeneratorBody();
      }
      {
        const __qin_typed_receiver_1807: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1807.RBrace();
      }
      return null;
    }
    super.__qin_subhuti_raw_GeneratorDeclaration(params);
    return null;
  }
  GeneratorExpression(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_GeneratorExpression();
    }), "GeneratorExpression", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_GeneratorExpression(): void {
    if (false) {
      {
        const __qin_typed_receiver_1808: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1808.Function();
      }
      {
        const __qin_typed_receiver_1809: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1809.Asterisk();
      }
      {
        const __qin_typed_receiver_1810: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1810.OptionalFunctionBindingIdentifier();
      }
      {
        const __qin_typed_receiver_1811: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1811.LParen();
      }
      {
        const __qin_typed_receiver_1812: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1812.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, false));
      }
      {
        const __qin_typed_receiver_1813: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1813.RParen();
      }
      {
        const __qin_typed_receiver_1814: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1814.LBrace();
      }
      {
        const __qin_typed_receiver_1815: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1815.GeneratorBody();
      }
      {
        const __qin_typed_receiver_1816: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1816.RBrace();
      }
      return null;
    }
    super.__qin_subhuti_raw_GeneratorExpression();
    return null;
  }
  GeneratorBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_GeneratorBody();
    }), "GeneratorBody", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_GeneratorBody(): void {
    if (false) {
      {
        const __qin_typed_receiver_1817: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1817.FunctionBody(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, false));
      }
      return null;
    }
    super.__qin_subhuti_raw_GeneratorBody();
    return null;
  }
  AsyncFunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncFunctionDeclaration(params);
    }), "AsyncFunctionDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncFunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1818: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1818.consumeIdentifierValue("async");
      }
      {
        const __qin_typed_receiver_1819: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1819.assertNoLineBreak();
      }
      {
        const __qin_typed_receiver_1820: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1820.Function();
      }
      if (params.isDefault()) {
        {
          const __qin_typed_receiver_1821: com_slime_parser_SlimeParser = this;
          __qin_typed_receiver_1821.OptionalFunctionBindingIdentifier();
        }
      } else {
        {
          const __qin_typed_receiver_1822: com_slime_parser_SlimeParser = this;
          __qin_typed_receiver_1822.RequiredFunctionBindingIdentifier();
        }
      }
      {
        const __qin_typed_receiver_1823: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1823.LParen();
      }
      {
        const __qin_typed_receiver_1824: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1824.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, true));
      }
      {
        const __qin_typed_receiver_1825: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1825.RParen();
      }
      {
        const __qin_typed_receiver_1826: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1826.LBrace();
      }
      {
        const __qin_typed_receiver_1827: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1827.AsyncFunctionBody();
      }
      {
        const __qin_typed_receiver_1828: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1828.RBrace();
      }
      return null;
    }
    super.__qin_subhuti_raw_AsyncFunctionDeclaration(params);
    return null;
  }
  AsyncFunctionExpression(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncFunctionExpression();
    }), "AsyncFunctionExpression", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncFunctionExpression(): void {
    if (false) {
      {
        const __qin_typed_receiver_1829: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1829.consumeIdentifierValue("async");
      }
      {
        const __qin_typed_receiver_1830: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1830.assertNoLineBreak();
      }
      {
        const __qin_typed_receiver_1831: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1831.Function();
      }
      {
        const __qin_typed_receiver_1832: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1832.OptionalFunctionBindingIdentifier();
      }
      {
        const __qin_typed_receiver_1833: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1833.LParen();
      }
      {
        const __qin_typed_receiver_1834: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1834.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, true));
      }
      {
        const __qin_typed_receiver_1835: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1835.RParen();
      }
      {
        const __qin_typed_receiver_1836: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1836.LBrace();
      }
      {
        const __qin_typed_receiver_1837: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1837.AsyncFunctionBody();
      }
      {
        const __qin_typed_receiver_1838: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1838.RBrace();
      }
      return null;
    }
    super.__qin_subhuti_raw_AsyncFunctionExpression();
    return null;
  }
  AsyncFunctionBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncFunctionBody();
    }), "AsyncFunctionBody", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncFunctionBody(): void {
    {
      const __qin_typed_receiver_1839: com_slime_parser_SlimeParser = this;
      __qin_typed_receiver_1839.FunctionBody(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, false, true));
    }
    return null;
  }
  AsyncGeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncGeneratorDeclaration(params);
    }), "AsyncGeneratorDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncGeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1840: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1840.consumeIdentifierValue("async");
      }
      {
        const __qin_typed_receiver_1841: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1841.assertNoLineBreak();
      }
      {
        const __qin_typed_receiver_1842: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1842.Function();
      }
      {
        const __qin_typed_receiver_1843: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1843.Asterisk();
      }
      if (params.isDefault()) {
        {
          const __qin_typed_receiver_1844: com_slime_parser_SlimeParser = this;
          __qin_typed_receiver_1844.OptionalFunctionBindingIdentifier();
        }
      } else {
        {
          const __qin_typed_receiver_1845: com_slime_parser_SlimeParser = this;
          __qin_typed_receiver_1845.RequiredFunctionBindingIdentifier();
        }
      }
      {
        const __qin_typed_receiver_1846: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1846.LParen();
      }
      {
        const __qin_typed_receiver_1847: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1847.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, true));
      }
      {
        const __qin_typed_receiver_1848: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1848.RParen();
      }
      {
        const __qin_typed_receiver_1849: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1849.LBrace();
      }
      {
        const __qin_typed_receiver_1850: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1850.AsyncGeneratorBody();
      }
      {
        const __qin_typed_receiver_1851: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1851.RBrace();
      }
      return null;
    }
    super.__qin_subhuti_raw_AsyncGeneratorDeclaration(params);
    return null;
  }
  AsyncGeneratorExpression(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncGeneratorExpression();
    }), "AsyncGeneratorExpression", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncGeneratorExpression(): void {
    if (false) {
      {
        const __qin_typed_receiver_1852: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1852.consumeIdentifierValue("async");
      }
      {
        const __qin_typed_receiver_1853: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1853.assertNoLineBreak();
      }
      {
        const __qin_typed_receiver_1854: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1854.Function();
      }
      {
        const __qin_typed_receiver_1855: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1855.Asterisk();
      }
      {
        const __qin_typed_receiver_1856: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1856.OptionalFunctionBindingIdentifier();
      }
      {
        const __qin_typed_receiver_1857: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1857.LParen();
      }
      {
        const __qin_typed_receiver_1858: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1858.FormalParameters(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, true));
      }
      {
        const __qin_typed_receiver_1859: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1859.RParen();
      }
      {
        const __qin_typed_receiver_1860: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1860.LBrace();
      }
      {
        const __qin_typed_receiver_1861: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1861.AsyncGeneratorBody();
      }
      {
        const __qin_typed_receiver_1862: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1862.RBrace();
      }
      return null;
    }
    super.__qin_subhuti_raw_AsyncGeneratorExpression();
    return null;
  }
  AsyncGeneratorBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncGeneratorBody();
    }), "AsyncGeneratorBody", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncGeneratorBody(): void {
    if (false) {
      {
        const __qin_typed_receiver_1863: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1863.FunctionBody(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, true, true));
      }
      return null;
    }
    super.__qin_subhuti_raw_AsyncGeneratorBody();
    return null;
  }
  PropertySetParameterList(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_PropertySetParameterList();
    }), "PropertySetParameterList", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_PropertySetParameterList(): void {
    if (false) {
      {
        const __qin_typed_receiver_1864: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1864.FormalParameter();
      }
      return null;
    }
    super.__qin_subhuti_raw_PropertySetParameterList();
    return null;
  }
  TSThisParameter(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSThisParameter();
    }), "TSThisParameter", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSThisParameter(): void {
    if (false) {
      {
        const __qin_typed_receiver_1865: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1865.This();
      }
      {
        const __qin_typed_receiver_1866: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1866.TSTypeAnnotation();
      }
      return null;
    }
    super.__qin_subhuti_raw_TSThisParameter();
    return null;
  }
  TSParameterProperty(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSParameterProperty();
    }), "TSParameterProperty", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSParameterProperty(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.TSAccessibilityModifier();
    }));
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.consumeIdentifierValue("readonly");
    }));
      {
        const __qin_typed_receiver_1867: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1867.Identifier();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Question();
    }));
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1868: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1868.Assign();
      }
      {
        const __qin_typed_receiver_1869: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1869.Expression();
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_TSParameterProperty();
    return null;
  }
  BindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingPattern(params);
    }), "BindingPattern", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.ObjectBindingPattern(params);
    }), __qin_java_functional(() => {
      return this.ArrayBindingPattern(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw_BindingPattern(params);
    return null;
  }
  ObjectBindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ObjectBindingPattern(params);
    }), "ObjectBindingPattern", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ObjectBindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1870: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1870.LBrace();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.BindingRestProperty(params);
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1871: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1871.BindingPropertyList(params);
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1872: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1872.Comma();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.BindingRestProperty(params);
    }));
      return null;
    }));
      return null;
    }));
    }));
      {
        const __qin_typed_receiver_1873: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1873.RBrace();
      }
      return null;
    }
    super.__qin_subhuti_raw_ObjectBindingPattern(params);
    return null;
  }
  ArrayBindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArrayBindingPattern(params);
    }), "ArrayBindingPattern", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrayBindingPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1874: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1874.LBracket();
      }
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.RBracket();
    }), __qin_java_functional(() => {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.Elision();
    }));
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.BindingRestElement(params);
    }));
      {
        const __qin_typed_receiver_1875: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1875.RBracket();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1876: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1876.BindingElementList(params);
      }
      {
        const __qin_typed_receiver_1877: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1877.RBracket();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1878: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1878.BindingElementList(params);
      }
      {
        const __qin_typed_receiver_1879: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1879.Comma();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.Elision();
    }));
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.BindingRestElement(params);
    }));
      {
        const __qin_typed_receiver_1880: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1880.RBracket();
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_ArrayBindingPattern(params);
    return null;
  }
  BindingRestProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingRestProperty(params);
    }), "BindingRestProperty", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingRestProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1881: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1881.Ellipsis();
      }
      {
        const __qin_typed_receiver_1882: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1882.BindingIdentifier(params);
      }
      return null;
    }
    super.__qin_subhuti_raw_BindingRestProperty(params);
    return null;
  }
  BindingPropertyList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingPropertyList(params);
    }), "BindingPropertyList", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingPropertyList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1883: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1883.BindingProperty(params);
      }
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1884: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1884.Comma();
      }
      {
        const __qin_typed_receiver_1885: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1885.BindingProperty(params);
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_BindingPropertyList(params);
    return null;
  }
  BindingProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingProperty(params);
    }), "BindingProperty", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1886: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1886.PropertyName(params);
      }
      {
        const __qin_typed_receiver_1887: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1887.Colon();
      }
      {
        const __qin_typed_receiver_1888: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1888.BindingElement(params);
      }
      return null;
    }), __qin_java_functional(() => {
      return this.SingleNameBinding(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw_BindingProperty(params);
    return null;
  }
  SingleNameBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_SingleNameBinding(params);
    }), "SingleNameBinding", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_SingleNameBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1889: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1889.BindingIdentifier(params);
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.Initializer(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw_SingleNameBinding(params);
    return null;
  }
  BindingElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingElementList(params);
    }), "BindingElementList", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1890: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1890.BindingElisionElement(params);
      }
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1891: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1891.Comma();
      }
      {
        const __qin_typed_receiver_1892: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1892.BindingElisionElement(params);
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_BindingElementList(params);
    return null;
  }
  BindingElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingElisionElement(params);
    }), "BindingElisionElement", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.Elision();
    }));
      {
        const __qin_typed_receiver_1893: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1893.BindingElement(params);
      }
      return null;
    }
    super.__qin_subhuti_raw_BindingElisionElement(params);
    return null;
  }
  BindingElement(...__qin_args: any[]): void {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_BindingElement_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_BindingElement_0_1();
    throw new Error("Unsupported Java overload: BindingElement/" + __qin_args.length);
  }
  __qin_overload_BindingElement_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_BindingElement_1_0(params);
    }), "BindingElement", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_BindingElement_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.SingleNameBinding(params);
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1894: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1894.BindingPattern(params);
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.Initializer(params);
    }));
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw___qin_overload_BindingElement_1_0(params);
    return null;
  }
  __qin_overload_BindingElement_0_1(): void {
    if (false) {
      {
        const __qin_typed_receiver_1895: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1895.BindingElement(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
      }
      return null;
    }
    super.__qin_subhuti_raw___qin_overload_BindingElement_1_0(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    return null;
  }
  BindingRestElement(...__qin_args: any[]): void {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_BindingRestElement_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_BindingRestElement_0_1();
    throw new Error("Unsupported Java overload: BindingRestElement/" + __qin_args.length);
  }
  __qin_overload_BindingRestElement_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_BindingRestElement_1_0(params);
    }), "BindingRestElement", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_BindingRestElement_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1896: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1896.Ellipsis();
      }
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.BindingIdentifier(params);
    }), __qin_java_functional(() => {
      return this.BindingPattern(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw___qin_overload_BindingRestElement_1_1(params);
    return null;
  }
  __qin_overload_BindingRestElement_0_1(): void {
    if (false) {
      {
        const __qin_typed_receiver_1897: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1897.BindingRestElement(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
      }
      return null;
    }
    super.BindingRestElement();
    return null;
  }
  AssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentPattern(params);
    }), "AssignmentPattern", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.ObjectAssignmentPattern(params);
    }), __qin_java_functional(() => {
      return this.ArrayAssignmentPattern(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw_AssignmentPattern(params);
    return null;
  }
  ObjectAssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ObjectAssignmentPattern(params);
    }), "ObjectAssignmentPattern", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ObjectAssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1898: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1898.LBrace();
      }
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.RBrace();
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1899: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1899.AssignmentRestProperty(params);
      }
      {
        const __qin_typed_receiver_1900: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1900.RBrace();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1901: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1901.AssignmentPropertyList(params);
      }
      {
        const __qin_typed_receiver_1902: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1902.Comma();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.AssignmentRestProperty(params);
    }));
      {
        const __qin_typed_receiver_1903: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1903.RBrace();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1904: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1904.AssignmentPropertyList(params);
      }
      {
        const __qin_typed_receiver_1905: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1905.RBrace();
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_ObjectAssignmentPattern(params);
    return null;
  }
  ArrayAssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArrayAssignmentPattern(params);
    }), "ArrayAssignmentPattern", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrayAssignmentPattern(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1906: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1906.LBracket();
      }
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.RBracket();
    }), __qin_java_functional(() => {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.Elision();
    }));
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.AssignmentRestElement(params);
    }));
      {
        const __qin_typed_receiver_1907: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1907.RBracket();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1908: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1908.AssignmentElementList(params);
      }
      {
        const __qin_typed_receiver_1909: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1909.Comma();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.Elision();
    }));
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.AssignmentRestElement(params);
    }));
      {
        const __qin_typed_receiver_1910: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1910.RBracket();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1911: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1911.AssignmentElementList(params);
      }
      {
        const __qin_typed_receiver_1912: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1912.RBracket();
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_ArrayAssignmentPattern(params);
    return null;
  }
  AssignmentRestProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentRestProperty(params);
    }), "AssignmentRestProperty", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentRestProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1913: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1913.Ellipsis();
      }
      {
        const __qin_typed_receiver_1914: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1914.DestructuringAssignmentTarget(params);
      }
      return null;
    }
    super.__qin_subhuti_raw_AssignmentRestProperty(params);
    return null;
  }
  AssignmentPropertyList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentPropertyList(params);
    }), "AssignmentPropertyList", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentPropertyList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1915: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1915.AssignmentProperty(params);
      }
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1916: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1916.Comma();
      }
      {
        const __qin_typed_receiver_1917: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1917.AssignmentProperty(params);
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_AssignmentPropertyList(params);
    return null;
  }
  AssignmentProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentProperty(params);
    }), "AssignmentProperty", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentProperty(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1918: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1918.PropertyName(params);
      }
      {
        const __qin_typed_receiver_1919: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1919.Colon();
      }
      {
        const __qin_typed_receiver_1920: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1920.AssignmentElement(params);
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1921: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1921.IdentifierReference(params);
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.AssignmentInitializer(params);
    }));
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_AssignmentProperty(params);
    return null;
  }
  AssignmentElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentElisionElement(params);
    }), "AssignmentElisionElement", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentElisionElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.Elision();
    }));
      {
        const __qin_typed_receiver_1922: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1922.AssignmentElement(params);
      }
      return null;
    }
    super.__qin_subhuti_raw_AssignmentElisionElement(params);
    return null;
  }
  AssignmentElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentElementList(params);
    }), "AssignmentElementList", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1923: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1923.AssignmentElisionElement(params);
      }
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1924: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1924.Comma();
      }
      {
        const __qin_typed_receiver_1925: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1925.AssignmentElisionElement(params);
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_AssignmentElementList(params);
    return null;
  }
  AssignmentElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentElement(params);
    }), "AssignmentElement", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.IdentifierReference(params);
    }), __qin_java_functional(() => {
      return this.AssignmentPattern(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw_AssignmentElement(params);
    return null;
  }
  AssignmentRestElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentRestElement(params);
    }), "AssignmentRestElement", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentRestElement(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1926: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1926.Ellipsis();
      }
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.IdentifierReference(params);
    }), __qin_java_functional(() => {
      return this.AssignmentPattern(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw_AssignmentRestElement(params);
    return null;
  }
  TSTypeAssertion(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeAssertion(params);
    }), "TSTypeAssertion", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_TSTypeAssertion(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1927: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1927.Less();
      }
      {
        const __qin_typed_receiver_1928: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1928.TSType();
      }
      {
        const __qin_typed_receiver_1929: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1929.Greater();
      }
      {
        const __qin_typed_receiver_1930: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1930.Expression();
      }
      return null;
    }
    super.__qin_subhuti_raw_TSTypeAssertion(params);
    return null;
  }
  AssignmentExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentExpression(params);
    }), "AssignmentExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      super.__qin_subhuti_raw_AssignmentExpression(params);
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.TSAsExpressionTail();
    }), __qin_java_functional(() => {
      return this.TSSatisfiesExpressionTail();
    }));
    }));
      return null;
    }
    super.__qin_subhuti_raw_AssignmentExpression(params);
    return null;
  }
  TSAsExpressionTail(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSAsExpressionTail();
    }), "TSAsExpressionTail", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSAsExpressionTail(): void {
    if (false) {
      {
        const __qin_typed_receiver_1931: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1931.consumeIdentifierValue("as");
      }
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Const();
    }), __qin_java_functional(() => {
      return this.TSType();
    }));
      return null;
    }
    super.__qin_subhuti_raw_TSAsExpressionTail();
    return null;
  }
  TSSatisfiesExpressionTail(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSSatisfiesExpressionTail();
    }), "TSSatisfiesExpressionTail", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSSatisfiesExpressionTail(): void {
    if (false) {
      {
        const __qin_typed_receiver_1932: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1932.consumeIdentifierValue("satisfies");
      }
      {
        const __qin_typed_receiver_1933: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1933.TSType();
      }
      return null;
    }
    super.__qin_subhuti_raw_TSSatisfiesExpressionTail();
    return null;
  }
  TSNonNullExpressionTail(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSNonNullExpressionTail();
    }), "TSNonNullExpressionTail", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSNonNullExpressionTail(): void {
    if (false) {
      {
        const __qin_typed_receiver_1934: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1934.LogicalNot();
      }
      return null;
    }
    super.__qin_subhuti_raw_TSNonNullExpressionTail();
    return null;
  }
  ArrowParameters(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArrowParameters(params);
    }), "ArrowParameters", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrowParameters(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.BindingIdentifier(params);
    }), __qin_java_functional(() => {
      return this.CoverParenthesizedExpressionAndArrowParameterList(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw_ArrowParameters(params);
    return null;
  }
  CoverParenthesizedExpressionAndArrowParameterList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CoverParenthesizedExpressionAndArrowParameterList(params);
    }), "CoverParenthesizedExpressionAndArrowParameterList", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoverParenthesizedExpressionAndArrowParameterList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1935: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1935.LParen();
      }
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.RParen();
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1936: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1936.Ellipsis();
      }
      {
        const __qin_typed_receiver_1937: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1937.coverArrowBindingTarget(params);
      }
      {
        const __qin_typed_receiver_1938: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1938.RParen();
      }
      return null;
    }), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.coverArrowFormalParameterListStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1939: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1939.FormalParameterList(params);
      }
      {
        const __qin_typed_receiver_1940: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1940.RParen();
      }
      return null;
    })), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1941: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1941.Expression(params.withIn(true));
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1942: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1942.Comma();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1943: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1943.Ellipsis();
      }
      {
        const __qin_typed_receiver_1944: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1944.coverArrowBindingTarget(params);
      }
      return null;
    }));
      return null;
    }));
      {
        const __qin_typed_receiver_1945: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1945.RParen();
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_CoverParenthesizedExpressionAndArrowParameterList(params);
    return null;
  }
  ImportSpecifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ImportSpecifier();
    }), "ImportSpecifier", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportSpecifier(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.importSpecifierTypeAliasStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1946: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1946.consumeIdentifierValue("type");
      }
      {
        const __qin_typed_receiver_1947: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1947.ModuleExportName();
      }
      {
        const __qin_typed_receiver_1948: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1948.consumeIdentifierValue("as");
      }
      {
        const __qin_typed_receiver_1949: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1949.ImportedBinding();
      }
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.importSpecifierTypeBindingOnlyStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1950: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1950.consumeIdentifierValue("type");
      }
      {
        const __qin_typed_receiver_1951: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1951.ImportedBinding();
      }
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.importSpecifierAliasStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1952: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1952.ModuleExportName();
      }
      {
        const __qin_typed_receiver_1953: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1953.consumeIdentifierValue("as");
      }
      {
        const __qin_typed_receiver_1954: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1954.ImportedBinding();
      }
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.importSpecifierBindingOnlyStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.ImportedBinding();
    })));
      return null;
    }
    super.__qin_subhuti_raw_ImportSpecifier();
    return null;
  }
  UpdateExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_UpdateExpression(params);
    }), "UpdateExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_UpdateExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_1955: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1955.Increment();
      }
      {
        const __qin_typed_receiver_1956: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1956.UnaryExpression(params);
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1957: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1957.Decrement();
      }
      {
        const __qin_typed_receiver_1958: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1958.UnaryExpression(params);
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1959: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1959.LeftHandSideExpression(params);
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.noLineBreakBeforeNextToken.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Increment();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.noLineBreakBeforeNextToken.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Decrement();
    })));
    }));
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_UpdateExpression(params);
    return null;
  }
  UnaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_UnaryExpression(params);
    }), "UnaryExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_UnaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.UpdateExpression(params);
    }), __qin_java_functional(() => {
      return this.TSTypeAssertion(params);
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1960: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1960.Delete();
      }
      {
        const __qin_typed_receiver_1961: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1961.UnaryExpression(params);
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1962: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1962.Void();
      }
      {
        const __qin_typed_receiver_1963: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1963.UnaryExpression(params);
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1964: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1964.Typeof();
      }
      {
        const __qin_typed_receiver_1965: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1965.UnaryExpression(params);
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1966: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1966.Plus();
      }
      {
        const __qin_typed_receiver_1967: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1967.UnaryExpression(params);
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1968: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1968.Minus();
      }
      {
        const __qin_typed_receiver_1969: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1969.UnaryExpression(params);
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1970: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1970.BitwiseNot();
      }
      {
        const __qin_typed_receiver_1971: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1971.UnaryExpression(params);
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1972: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1972.LogicalNot();
      }
      {
        const __qin_typed_receiver_1973: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1973.UnaryExpression(params);
      }
      return null;
    }), __qin_java_functional(() => {
      if (params.await()) {
        {
          const __qin_typed_receiver_1974: com_slime_parser_SlimeParser = this;
          __qin_typed_receiver_1974.AwaitExpression(params);
        }
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_UnaryExpression(params);
    return null;
  }
  LeftHandSideExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_LeftHandSideExpression(params);
    }), "LeftHandSideExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LeftHandSideExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.OptionalExpression(params);
    }), __qin_java_functional(() => {
      return this.CallExpression(params);
    }), __qin_java_functional(() => {
      return this.NewExpression(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw_LeftHandSideExpression(params);
    return null;
  }
  AwaitExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AwaitExpression(params);
    }), "AwaitExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AwaitExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1975: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1975.Await();
      }
      {
        const __qin_typed_receiver_1976: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1976.UnaryExpression(params);
      }
      return null;
    }
    super.__qin_subhuti_raw_AwaitExpression(params);
    return null;
  }
  MetaProperty(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_MetaProperty();
    }), "MetaProperty", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_MetaProperty(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.NewTarget();
    }), __qin_java_functional(() => {
      return this.ImportMeta();
    }));
      return null;
    }
    super.__qin_subhuti_raw_MetaProperty();
    return null;
  }
  NewTarget(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_NewTarget();
    }), "NewTarget", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_NewTarget(): void {
    if (false) {
      {
        const __qin_typed_receiver_1977: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1977.New();
      }
      {
        const __qin_typed_receiver_1978: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1978.Dot();
      }
      {
        const __qin_typed_receiver_1979: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1979.consumeIdentifierValue("target");
      }
      return null;
    }
    super.__qin_subhuti_raw_NewTarget();
    return null;
  }
  ImportMeta(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ImportMeta();
    }), "ImportMeta", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportMeta(): void {
    if (false) {
      {
        const __qin_typed_receiver_1980: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1980.Import();
      }
      {
        const __qin_typed_receiver_1981: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1981.Dot();
      }
      {
        const __qin_typed_receiver_1982: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1982.consumeIdentifierValue("meta");
      }
      return null;
    }
    super.__qin_subhuti_raw_ImportMeta();
    return null;
  }
  CallExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CallExpression(params);
    }), "CallExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CallExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.CoverCallExpressionAndAsyncArrowHead(params);
    }), __qin_java_functional(() => {
      return this.SuperCall(params);
    }), __qin_java_functional(() => {
      return this.ImportCall(params);
    }));
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.CallExpressionSuffix(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw_CallExpression(params);
    return null;
  }
  CallExpressionSuffix(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CallExpressionSuffix(params);
    }), "CallExpressionSuffix", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CallExpressionSuffix(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.Arguments(params);
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1983: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1983.LBracket();
      }
      {
        const __qin_typed_receiver_1984: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1984.Expression(params.withIn(true));
      }
      {
        const __qin_typed_receiver_1985: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1985.RBracket();
      }
      return null;
    }), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.dotIdentifierMemberPropertyStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1986: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1986.Dot();
      }
      {
        const __qin_typed_receiver_1987: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1987.IdentifierName();
      }
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.incompleteMemberAccessStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.IncompleteMemberAccessProperty();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.templateLiteralStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TemplateLiteral(new com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams(params, true));
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.dotPrivateMemberPropertyStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1988: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1988.Dot();
      }
      {
        const __qin_typed_receiver_1989: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1989.PrivateIdentifier();
      }
      return null;
    })), __qin_java_functional(() => {
      return this.OptionalChain(params);
    }), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.tsNonNullExpressionTailStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSNonNullExpressionTailStatic();
    })));
      return null;
    }
    {
      const __qin_typed_receiver_1990: com_slime_parser_SlimeParser = this;
      __qin_typed_receiver_1990.setParseFail();
    }
    return null;
  }
  OptionalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalExpression(params);
    }), "OptionalExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_OptionalExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.callExpressionBeforeOptionalChainStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.CallExpression(params);
    })), __qin_java_functional(() => {
      if (this.memberExpressionBeforeOptionalChainStart(params)) {
        {
          const __qin_typed_receiver_1991: com_slime_parser_SlimeParser = this;
          __qin_typed_receiver_1991.MemberExpression(params);
        }
      }
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_OptionalExpression(params);
    return null;
  }
  OptionalChain(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalChain(params);
    }), "OptionalChain", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_OptionalChain(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_1992: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1992.QuestionDot();
      }
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.Arguments(params);
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1993: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1993.LBracket();
      }
      {
        const __qin_typed_receiver_1994: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1994.Expression(params.withIn(true));
      }
      {
        const __qin_typed_receiver_1995: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1995.RBracket();
      }
      return null;
    }), __qin_java_functional(() => {
      return this.IdentifierName();
    }), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.templateLiteralStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TemplateLiteral(new com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams(params, true));
    })), __qin_java_functional(() => {
      return this.PrivateIdentifier();
    }));
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.OptionalChainSuffix(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw_OptionalChain(params);
    return null;
  }
  OptionalChainSuffix(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_OptionalChainSuffix(params);
    }), "OptionalChainSuffix", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_OptionalChainSuffix(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.Arguments(params);
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1996: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1996.LBracket();
      }
      {
        const __qin_typed_receiver_1997: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_1997.Expression(params.withIn(true));
      }
      {
        const __qin_typed_receiver_1998: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1998.RBracket();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_1999: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_1999.Dot();
      }
      {
        const __qin_typed_receiver_2000: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2000.IdentifierName();
      }
      return null;
    }), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.templateLiteralStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TemplateLiteral(new com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams(params, true));
    })), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2001: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2001.Dot();
      }
      {
        const __qin_typed_receiver_2002: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2002.PrivateIdentifier();
      }
      return null;
    }));
      return null;
    }
    {
      const __qin_typed_receiver_2003: com_slime_parser_SlimeParser = this;
      __qin_typed_receiver_2003.setParseFail();
    }
    return null;
  }
  MemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_MemberExpression(params);
    }), "MemberExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_MemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.PrimaryExpression(params);
    }), __qin_java_functional(() => {
      return this.SuperProperty(params);
    }), __qin_java_functional(() => {
      return this.MetaProperty();
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2004: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2004.New();
      }
      {
        const __qin_typed_receiver_2005: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2005.MemberExpression(params);
      }
      SubhutiCompileOnlyDsl.Option(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.tsTypeParameterInstantiationStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSTypeParameterInstantiationStatic();
    })));
      {
        const __qin_typed_receiver_2006: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2006.Arguments(params);
      }
      return null;
    }));
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.MemberExpressionSuffix(params);
    }));
      return null;
    }
    super.__qin_subhuti_raw_MemberExpression(params);
    return null;
  }
  MemberExpressionSuffix(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_MemberExpressionSuffix(params);
    }), "MemberExpressionSuffix", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_MemberExpressionSuffix(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2007: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2007.LBracket();
      }
      {
        const __qin_typed_receiver_2008: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2008.Expression(params.withIn(true));
      }
      {
        const __qin_typed_receiver_2009: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2009.RBracket();
      }
      return null;
    }), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.dotIdentifierMemberPropertyStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2010: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2010.Dot();
      }
      {
        const __qin_typed_receiver_2011: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2011.IdentifierName();
      }
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.incompleteMemberAccessStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.IncompleteMemberAccessProperty();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.templateLiteralStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TemplateLiteral(new com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams(params, true));
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.dotPrivateMemberPropertyStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2012: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2012.Dot();
      }
      {
        const __qin_typed_receiver_2013: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2013.PrivateIdentifier();
      }
      return null;
    })), __qin_java_functional(() => {
      return this.OptionalChain(params);
    }), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.tsNonNullExpressionTailStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSNonNullExpressionTailStatic();
    })));
      return null;
    }
    {
      const __qin_typed_receiver_2014: com_slime_parser_SlimeParser = this;
      __qin_typed_receiver_2014.setParseFail();
    }
    return null;
  }
  NewExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_NewExpression(params);
    }), "NewExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_NewExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.notNewExpressionStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.MemberExpression(params);
    })), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2015: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2015.New();
      }
      {
        const __qin_typed_receiver_2016: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2016.NewExpression(params);
      }
      SubhutiCompileOnlyDsl.Option(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.tsTypeParameterInstantiationStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSTypeParameterInstantiationStatic();
    })));
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.Arguments(params);
    }));
      return null;
    }));
      return null;
    }
    super.__qin_subhuti_raw_NewExpression(params);
    return null;
  }
  CoverCallExpressionAndAsyncArrowHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CoverCallExpressionAndAsyncArrowHead(params);
    }), "CoverCallExpressionAndAsyncArrowHead", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoverCallExpressionAndAsyncArrowHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_2017: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2017.MemberExpression(params);
      }
      SubhutiCompileOnlyDsl.Option(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.tsTypeParameterInstantiationStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSTypeParameterInstantiationStatic();
    })));
      {
        const __qin_typed_receiver_2018: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2018.Arguments(params);
      }
      return null;
    }
    super.__qin_subhuti_raw_CoverCallExpressionAndAsyncArrowHead(params);
    return null;
  }
  CallMemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CallMemberExpression(params);
    }), "CallMemberExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CallMemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_2019: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2019.MemberExpression(params);
      }
      SubhutiCompileOnlyDsl.Option(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.tsTypeParameterInstantiationStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSTypeParameterInstantiationStatic();
    })));
      {
        const __qin_typed_receiver_2020: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2020.Arguments(params);
      }
      return null;
    }
    super.__qin_subhuti_raw_CallMemberExpression(params);
    return null;
  }
  SuperCall(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_SuperCall(params);
    }), "SuperCall", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_SuperCall(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_2021: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2021.Super();
      }
      {
        const __qin_typed_receiver_2022: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2022.Arguments(params);
      }
      return null;
    }
    super.__qin_subhuti_raw_SuperCall(params);
    return null;
  }
  ImportCall(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ImportCall(params);
    }), "ImportCall", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ImportCall(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_2023: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2023.Import();
      }
      {
        const __qin_typed_receiver_2024: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2024.LParen();
      }
      {
        const __qin_typed_receiver_2025: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2025.AssignmentExpression(params.withIn(true));
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2026: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2026.Comma();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2027: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2027.AssignmentExpression(params.withIn(true));
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Comma();
    }));
      return null;
    }));
      return null;
    }));
      {
        const __qin_typed_receiver_2028: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2028.RParen();
      }
      return null;
    }
    super.__qin_subhuti_raw_ImportCall(params);
    return null;
  }
  Arguments(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Arguments(params);
    }), "Arguments", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Arguments(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_2029: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2029.LParen();
      }
      SubhutiCompileOnlyDsl.Option(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.argumentListStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.ArgumentList(params);
    })));
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Comma();
    }));
      {
        const __qin_typed_receiver_2030: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2030.RParen();
      }
      return null;
    }
    super.__qin_subhuti_raw_Arguments(params);
    return null;
  }
  ArgumentList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArgumentList(params);
    }), "ArgumentList", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArgumentList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      {
        const __qin_typed_receiver_2031: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2031.ArgumentListItem(params);
      }
      SubhutiCompileOnlyDsl.Many(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.argumentListContinuationStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2032: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2032.Comma();
      }
      {
        const __qin_typed_receiver_2033: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2033.ArgumentListItem(params);
      }
      return null;
    })));
      return null;
    }
    super.__qin_subhuti_raw_ArgumentList(params);
    return null;
  }
  ArgumentListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArgumentListItem(params);
    }), "ArgumentListItem", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArgumentListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Ellipsis();
    }));
      {
        const __qin_typed_receiver_2034: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2034.AssignmentExpression(params.withIn(true));
      }
      return null;
    }
    super.__qin_subhuti_raw_ArgumentListItem(params);
    return null;
  }
  PrimaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_PrimaryExpression(params);
    }), "PrimaryExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_PrimaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.TSTypeAssertion(params);
    }), __qin_java_functional(() => {
      return this.StandardPrimaryExpression(params);
    }));
      return null;
    }
    this.__qin_subhuti_raw_StandardPrimaryExpression(params);
    return null;
  }
  TSAccessibilityModifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSAccessibilityModifier();
    }), "TSAccessibilityModifier", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSAccessibilityModifier(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.consumeIdentifierValue("public");
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("private");
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("protected");
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("readonly");
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("override");
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("declare");
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("accessor");
    }));
      return null;
    }
    super.__qin_subhuti_raw_TSAccessibilityModifier();
    return null;
  }
  TSAbstractModifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSAbstractModifier();
    }), "TSAbstractModifier", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSAbstractModifier(): void {
    if (false) {
      {
        const __qin_typed_receiver_2035: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2035.consumeIdentifierValue("abstract");
      }
      return null;
    }
    super.__qin_subhuti_raw_TSAbstractModifier();
    return null;
  }
  TSDecorators(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSDecorators();
    }), "TSDecorators", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSDecorators(): void {
    if (false) {
      {
        const __qin_typed_receiver_2036: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2036.TSDecorator();
      }
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.TSDecorator();
    }));
      return null;
    }
    super.__qin_subhuti_raw_TSDecorators();
    return null;
  }
  TSDecorator(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSDecorator();
    }), "TSDecorator", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSDecorator(): void {
    if (false) {
      {
        const __qin_typed_receiver_2037: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2037.At();
      }
      {
        const __qin_typed_receiver_2038: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2038.LeftHandSideExpression(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
      }
      return null;
    }
    super.__qin_subhuti_raw_TSDecorator();
    return null;
  }
  methodDefinitionGeneratorStart(): boolean {
    return __QinJavaLangString.equals("Asterisk", this.tokenNameAt(1.0));
  }
  methodDefinitionAsyncGeneratorStart(): boolean {
    return (this.matchIdentifierValue("async") && !this.hasSourceLineBreakBefore(2.0) && __QinJavaLangString.equals("Asterisk", this.tokenNameAt(2.0)));
  }
  methodDefinitionAsyncStart(): boolean {
    return (this.matchIdentifierValue("async") && !this.hasSourceLineBreakBefore(2.0) && !__QinJavaLangString.equals("Asterisk", this.tokenNameAt(2.0)));
  }
  methodDefinitionGetterStart(): boolean {
    return (this.matchIdentifierValue("get") && __QinJavaLangString.equals("LParen", this.tokenNameAt(3.0)));
  }
  methodDefinitionSetterStart(): boolean {
    return (this.matchIdentifierValue("set") && __QinJavaLangString.equals("LParen", this.tokenNameAt(3.0)));
  }
  methodDefinitionOrdinaryStart(): boolean {
    return (__QinJavaLangString.equals("IdentifierName", this.tokenNameAt(1.0)) || __QinJavaLangString.equals("StringLiteral", this.tokenNameAt(1.0)) || __QinJavaLangString.equals("NumericLiteral", this.tokenNameAt(1.0)) || __QinJavaLangString.equals("LBracket", this.tokenNameAt(1.0)) || __QinJavaLangString.equals("PrivateIdentifier", this.tokenNameAt(1.0)));
  }
  iterationStatementDoWhileStart(): boolean {
    return __QinJavaLangString.equals("Do", this.tokenNameAt(1.0));
  }
  iterationStatementWhileStart(): boolean {
    return __QinJavaLangString.equals("While", this.tokenNameAt(1.0));
  }
  iterationStatementForInOfStart(): boolean {
    if ((!__QinJavaLangString.equals("For", this.tokenNameAt(1.0)))) {
      return false;
    }
    if (__QinJavaLangString.equals("Await", this.tokenNameAt(2.0))) {
      return true;
    }
    if ((!__QinJavaLangString.equals("LParen", this.tokenNameAt(2.0)))) {
      return false;
    }
    let parenDepth: number = 0.0;
    let bracketDepth: number = 0.0;
    let braceDepth: number = 0.0;
    for (let offset: number = 3.0; true; offset++) {
      let tokenName: string = this.tokenNameAt(offset);
      if (__qin_binary__("==", tokenName, null)) {
        return false;
      }
      if ((__qin_binary__("==", parenDepth, 0.0) && __qin_binary__("==", bracketDepth, 0.0) && __qin_binary__("==", braceDepth, 0.0))) {
        if (__QinJavaLangString.equals("Semicolon", tokenName)) {
          return false;
        }
        if ((__QinJavaLangString.equals("In", tokenName) || this.matchIdentifierValue("of", offset))) {
          return true;
        }
        if (__QinJavaLangString.equals("RParen", tokenName)) {
          return false;
        }
      }
      if (__QinJavaLangString.equals("LParen", tokenName)) {
        parenDepth++;
      } else {
        if (__QinJavaLangString.equals("RParen", tokenName)) {
          parenDepth = Math.max(0.0, __qin_binary__("-", parenDepth, 1.0));
        } else {
          if (__QinJavaLangString.equals("LBracket", tokenName)) {
            bracketDepth++;
          } else {
            if (__QinJavaLangString.equals("RBracket", tokenName)) {
              bracketDepth = Math.max(0.0, __qin_binary__("-", bracketDepth, 1.0));
            } else {
              if (__QinJavaLangString.equals("LBrace", tokenName)) {
                braceDepth++;
              } else {
                if (__QinJavaLangString.equals("RBrace", tokenName)) {
                  braceDepth = Math.max(0.0, __qin_binary__("-", braceDepth, 1.0));
                }
              }
            }
          }
        }
      }
    }
    return null;
  }
  iterationStatementForStatementStart(): boolean {
    return (__QinJavaLangString.equals("For", this.tokenNameAt(1.0)) && !this.iterationStatementForInOfStart());
  }
  importSpecifierTypeAliasStart(): boolean {
    return (this.matchIdentifierValue("type", 1.0) && this.canStartImportSpecifierModuleExportNameAt(2.0) && this.matchIdentifierValue("as", 3.0) && this.canStartImportSpecifierImportedBindingAt(4.0));
  }
  importSpecifierTypeBindingOnlyStart(): boolean {
    return (this.matchIdentifierValue("type", 1.0) && this.canStartImportSpecifierImportedBindingAt(2.0) && !this.importSpecifierTypeAliasStart());
  }
  importSpecifierAliasStart(): boolean {
    return (this.canStartImportSpecifierModuleExportNameAt(1.0) && this.matchIdentifierValue("as", 2.0) && this.canStartImportSpecifierImportedBindingAt(3.0) && !this.importSpecifierTypeAliasStart() && !this.importSpecifierTypeBindingOnlyStart());
  }
  importSpecifierBindingOnlyStart(): boolean {
    return (this.canStartImportSpecifierImportedBindingAt(1.0) && !this.importSpecifierTypeAliasStart() && !this.importSpecifierTypeBindingOnlyStart() && !this.importSpecifierAliasStart());
  }
  declarationStandardStart(): boolean {
    return (this.canStartStandardDeclaration() && !this.canStartTSInterfaceDeclaration() && !this.canStartTSTypeAliasDeclaration() && !this.canStartTSEnumDeclaration() && !this.canStartTSModuleDeclaration() && !this.canStartTSDeclareStatement());
  }
  noLineBreakBeforeNextToken(): boolean {
    return (!this.lookaheadHasLineBreak());
  }
  dotMemberPropertyStart(): boolean {
    if ((!__QinJavaLangString.equals("Dot", this.tokenNameAt(1.0)))) {
      return false;
    }
    let property: string = this.tokenNameAt(2.0);
    return (__QinJavaLangString.equals("IdentifierName", property) || __QinJavaLangString.equals("PrivateIdentifier", property));
  }
  dotIdentifierMemberPropertyStart(): boolean {
    return (__QinJavaLangString.equals("Dot", this.tokenNameAt(1.0)) && this.canStartIdentifierNameToken(2.0));
  }
  dotPrivateMemberPropertyStart(): boolean {
    return (__QinJavaLangString.equals("Dot", this.tokenNameAt(1.0)) && __QinJavaLangString.equals("PrivateIdentifier", this.tokenNameAt(2.0)));
  }
  incompleteMemberAccessStart(): boolean {
    return this.canRecoverIncompleteMemberAccess();
  }
  templateLiteralStart(): boolean {
    return this.canStartTemplateLiteral();
  }
  tsNonNullExpressionTailStart(): boolean {
    return this.canStartTSNonNullExpressionTailStatic();
  }
  callExpressionBeforeOptionalChainStart(): boolean {
    return this.hasCallExpressionPostfixAhead();
  }
  memberExpressionBeforeOptionalChainStart(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): boolean {
    return (this.canStartMemberExpressionSource(params, 1.0) && !this.hasCallExpressionPostfixAhead());
  }
  tsTypeParameterInstantiationStart(): boolean {
    return this.canStartTSTypeParameterInstantiationStatic();
  }
  notNewExpressionStart(): boolean {
    return (!__QinJavaLangString.equals("New", this.tokenNameAt(1.0)) && !this.canStartOptionalExpression());
  }
  argumentListStart(): boolean {
    return this.canStartArgumentListItemSource(this.activeStaticExpressionParams(), 1.0);
  }
  argumentListContinuationStart(): boolean {
    return (__QinJavaLangString.equals("Comma", this.tokenNameAt(1.0)) && this.canStartArgumentListItemSource(this.activeStaticExpressionParams(), 2.0));
  }
  canStartArgumentListItemSource(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params);
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    if (__QinJavaLangString.equals("Ellipsis", tokenName)) {
      return true;
    }
    if ((effectiveParams.yield() && __QinJavaLangString.equals("Yield", tokenName))) {
      return true;
    }
    if ((effectiveParams.await() && __QinJavaLangString.equals("Await", tokenName))) {
      return true;
    }
    if ((__QinJavaLangString.equals("Delete", tokenName) || __QinJavaLangString.equals("Void", tokenName) || __QinJavaLangString.equals("Typeof", tokenName) || __QinJavaLangString.equals("Plus", tokenName) || __QinJavaLangString.equals("Minus", tokenName) || __QinJavaLangString.equals("Increment", tokenName) || __QinJavaLangString.equals("Decrement", tokenName) || __QinJavaLangString.equals("BitwiseNot", tokenName) || __QinJavaLangString.equals("LogicalNot", tokenName) || __QinJavaLangString.equals("This", tokenName) || __QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("NullLiteral", tokenName) || __QinJavaLangString.equals("True", tokenName) || __QinJavaLangString.equals("False", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("Function", tokenName) || __QinJavaLangString.equals("Class", tokenName) || __QinJavaLangString.equals("LBracket", tokenName) || __QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("RegularExpressionLiteral", tokenName) || __QinJavaLangString.equals("NoSubstitutionTemplate", tokenName) || __QinJavaLangString.equals("TemplateHead", tokenName) || __QinJavaLangString.equals("LParen", tokenName) || __QinJavaLangString.equals("Super", tokenName) || __QinJavaLangString.equals("Import", tokenName) || __QinJavaLangString.equals("New", tokenName))) {
      return true;
    }
    return ((__QinJavaLangString.equals("Yield", tokenName) && !effectiveParams.yield()) || (__QinJavaLangString.equals("Await", tokenName) && !effectiveParams.await()));
  }
  canStartMemberExpressionSource(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    if (__QinJavaLangString.equals("New", tokenName)) {
      return true;
    }
    if (this.canStartPrimaryExpressionSource(params, lookaheadOffset)) {
      return true;
    }
    return (this.canStartSuperPropertySource(lookaheadOffset) || this.canStartMetaPropertySource(lookaheadOffset));
  }
  canStartPrimaryExpressionSource(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, lookaheadOffset: number): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    if ((__QinJavaLangString.equals("This", tokenName) || __QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("NullLiteral", tokenName) || __QinJavaLangString.equals("True", tokenName) || __QinJavaLangString.equals("False", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("Function", tokenName) || __QinJavaLangString.equals("Class", tokenName) || __QinJavaLangString.equals("LBracket", tokenName) || __QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("RegularExpressionLiteral", tokenName) || __QinJavaLangString.equals("NoSubstitutionTemplate", tokenName) || __QinJavaLangString.equals("TemplateHead", tokenName) || __QinJavaLangString.equals("LParen", tokenName))) {
      return true;
    }
    if (__QinJavaLangString.equals("Yield", tokenName)) {
      return (__qin_binary__("==", params, null) || !params.yield());
    }
    if (__QinJavaLangString.equals("Await", tokenName)) {
      return (__qin_binary__("==", params, null) || !params.await());
    }
    return false;
  }
  canStartSuperPropertySource(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Super", this.tokenNameAt(lookaheadOffset)) && (__QinJavaLangString.equals("LBracket", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) || __QinJavaLangString.equals("Dot", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0)))));
  }
  canStartMetaPropertySource(lookaheadOffset: number): boolean {
    return (this.canStartNewTargetSource(lookaheadOffset) || this.canStartImportMetaSource(lookaheadOffset));
  }
  canStartNewTargetSource(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("New", this.tokenNameAt(lookaheadOffset)) && __QinJavaLangString.equals("Dot", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && __qin_binary__("!=", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)), null) && __QinJavaLangString.equals("IdentifierName", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)).tokenName()) && __QinJavaLangString.equals("target", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)).value()));
  }
  canStartImportMetaSource(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Import", this.tokenNameAt(lookaheadOffset)) && __QinJavaLangString.equals("Dot", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && __qin_binary__("!=", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)), null) && __QinJavaLangString.equals("IdentifierName", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)).tokenName()) && __QinJavaLangString.equals("meta", this.LA(__qin_binary__("+", lookaheadOffset, 2.0)).value()));
  }
  canStartImportSpecifierImportedBindingAt(offset: number): boolean {
    return (this.canStartIdentifier(offset) || __QinJavaLangString.equals("Yield", this.tokenNameAt(offset)));
  }
  canStartImportSpecifierModuleExportNameAt(offset: number): boolean {
    return (this.canStartIdentifierName(offset) || __QinJavaLangString.equals("StringLiteral", this.tokenNameAt(offset)));
  }
  coverArrowFormalParameterListStart(): boolean {
    let nestedParens: number = 0.0;
    let nestedBraces: number = 0.0;
    let nestedBrackets: number = 0.0;
    let conditionalDepth: number = 0.0;
    for (let offset: number = 1.0; true; offset++) {
      let tokenName: string = this.tokenNameAt(offset);
      if (__qin_binary__("==", tokenName, null)) {
        return false;
      }
      if ((__qin_binary__("==", nestedParens, 0.0) && __qin_binary__("==", nestedBraces, 0.0) && __qin_binary__("==", nestedBrackets, 0.0))) {
        if ((__QinJavaLangString.equals("RParen", tokenName) || __QinJavaLangString.equals("Comma", tokenName) || __QinJavaLangString.equals("Ellipsis", tokenName))) {
          return false;
        }
        if (__QinJavaLangString.equals("Colon", tokenName)) {
          if (__qin_binary__(">", conditionalDepth, 0.0)) {
            conditionalDepth--;
            continue;
          }
          return true;
        }
        if (__QinJavaLangString.equals("Question", tokenName)) {
          let next: string = this.tokenNameAt(__qin_binary__("+", offset, 1.0));
          if ((__QinJavaLangString.equals("Colon", next) || __QinJavaLangString.equals("Comma", next) || __QinJavaLangString.equals("RParen", next))) {
            return true;
          }
          conditionalDepth++;
        }
      }
      if (__QinJavaLangString.equals("LParen", tokenName)) {
        nestedParens++;
      } else {
        if (__QinJavaLangString.equals("RParen", tokenName)) {
          if (__qin_binary__("==", nestedParens, 0.0)) {
            return false;
          }
          nestedParens--;
        } else {
          if (__QinJavaLangString.equals("LBrace", tokenName)) {
            nestedBraces++;
          } else {
            if (__QinJavaLangString.equals("RBrace", tokenName)) {
              if (__qin_binary__(">", nestedBraces, 0.0)) {
                nestedBraces--;
              }
            } else {
              if (__QinJavaLangString.equals("LBracket", tokenName)) {
                nestedBrackets++;
              } else {
                if (__QinJavaLangString.equals("RBracket", tokenName)) {
                  if (__qin_binary__(">", nestedBrackets, 0.0)) {
                    nestedBrackets--;
                  }
                }
              }
            }
          }
        }
      }
    }
    return null;
  }
  coverArrowBindingTarget(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    let tokenName: string = this.tokenNameAt(1.0);
    if ((__QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("LBracket", tokenName))) {
      {
        const __qin_typed_receiver_2039: com_slime_parser_SlimeParser = this;
        __qin_typed_receiver_2039.BindingPattern(params);
      }
      return null;
    }
    {
      const __qin_typed_receiver_2040: com_slime_parser_SlimeParser = this;
      __qin_typed_receiver_2040.BindingIdentifier(params);
    }
    return null;
  }
  hasSourceLineBreakBefore(offset: number): boolean {
    try {
      let token: com_subhuti_struct_SubhutiMatchToken = this.LA(offset);
      return (__qin_binary__("!=", token, null) && token.hasLineBreakBefore());
    } catch (exception) {
      if (!(exception instanceof __QinJavaLangRuntimeException)) {
        throw exception;
      }
      return false;
    }
    return null;
  }
}
const SlimeParser = com_slime_parser_SlimeParser;

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_SlimeParser };
