import { com_slime_parser_SlimeParser, com_slime_parser_SlimeParser as SlimeParser } from "../../slime/parser/SlimeParser.ts";
import { com_slime_parser_SlimeParserRuntimeBase, com_slime_parser_SlimeParserRuntimeBase as SlimeParserRuntimeBase, com_slime_parser_SlimeParserRuntimeBase$EmptyStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$EmptyStaticRuntime as EmptyStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootOptionalStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootOptionalStaticRuntime as TSRootOptionalStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootPrimaryStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootPrimaryStaticRuntime as TSRootPrimaryStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootFormalParametersStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootFormalParametersStaticRuntime as TSRootFormalParametersStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootDeclarationStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootDeclarationStaticRuntime as TSRootDeclarationStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootImportSpecifierStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootImportSpecifierStaticRuntime as TSRootImportSpecifierStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootMethodDefinitionStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootMethodDefinitionStaticRuntime as TSRootMethodDefinitionStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$ClassBindingStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$ClassBindingStaticRuntime as ClassBindingStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$RootClassTailStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$RootClassTailStaticRuntime as RootClassTailStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSModifierStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSModifierStaticRuntime as TSModifierStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSAsExpressionTailStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSAsExpressionTailStaticRuntime as TSAsExpressionTailStaticRuntime } from "../../slime/parser/SlimeParserRuntimeBase.ts";
import { com_slime_parser_typescript_SlimeTSDeclarationParser, com_slime_parser_typescript_SlimeTSDeclarationParser as SlimeTSDeclarationParser, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime as TSInterfaceDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime as TSInterfaceExtendsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime as TSInterfaceBodyStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime as TSTypeAliasDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime as TSExpressionWithTypeArgumentsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSClassImplementsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSClassImplementsStaticRuntime as TSClassImplementsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime as TSEnumDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumBodyStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumBodyStaticRuntime as TSEnumBodyStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime as TSEnumMemberListStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberStaticRuntime as TSEnumMemberStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime as TSModuleDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleNameStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleNameStaticRuntime as TSModuleNameStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleBlockStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleBlockStaticRuntime as TSModuleBlockStaticRuntime } from "../../slime/parser/typescript/SlimeTSDeclarationParser.ts";
import { com_slime_parser_typescript_SlimeTSTypeParser, com_slime_parser_typescript_SlimeTSTypeParser as SlimeTSTypeParser, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime as TSTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime as TSPrimaryTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime as TSTypeLiteralStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime as TSTypeReferenceStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime as TSTypeNameStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime as TSTypeNameSuffixStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime as TSTypeParameterInstantiationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime as TSPrefixTypeOrPrimaryStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime as TSTypeOperandStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime as TSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime as TSKeywordTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime as TSUnionOrIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime as TSIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime as TSPropertyOrMethodSignatureStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime as TSParameterListStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime as TSParameterStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime as TSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime as OptionalTSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime as OptionalTSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime as TSTypeParameterStaticRuntime } from "../../slime/parser/typescript/SlimeTSTypeParser.ts";
import { com_slime_parser_SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser as SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser$SourceType, com_slime_parser_SlimeJavascriptParser$SourceType as SourceType, com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime, com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime as JavascriptStaticRuntime } from "../../slime/parser/SlimeJavascriptParser.ts";
import { com_slime_parser_module_SlimeModuleParser, com_slime_parser_module_SlimeModuleParser as SlimeModuleParser, com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime, com_slime_parser_module_SlimeModuleParser$ModuleStaticRuntime as ModuleStaticRuntime } from "../../slime/parser/module/SlimeModuleParser.ts";
import { com_slime_parser_class__SlimeClassParser, com_slime_parser_class__SlimeClassParser as SlimeClassParser, com_slime_parser_class__SlimeClassParser$ClassStaticRuntime, com_slime_parser_class__SlimeClassParser$ClassStaticRuntime as ClassStaticRuntime } from "../../slime/parser/class_/SlimeClassParser.ts";
import { com_slime_parser_function_SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser as SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime, com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime as FunctionStaticRuntime } from "../../slime/parser/function/SlimeFunctionParser.ts";
import { com_slime_parser_statements_SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser as SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime as StatementRootStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime as StatementLoopStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime as StatementTryStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime as StatementIfStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime as StatementVariableStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime as StatementListStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime as StatementJumpStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime as StatementBranchStaticRuntime } from "../../slime/parser/statements/SlimeStatementParser.ts";
import { com_slime_parser_expressions_SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser as SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime as AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime as AssignmentExpressionStaticRuntime } from "../../slime/parser/expressions/SlimeAssignmentExpressionParser.ts";
import { com_slime_parser_expressions_SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser as SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime, com_slime_parser_expressions_SlimeBinaryExpressionParser$BinaryStaticRuntime as BinaryStaticRuntime } from "../../slime/parser/expressions/SlimeBinaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser as SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime, com_slime_parser_expressions_SlimeUnaryExpressionParser$UnaryStaticRuntime as UnaryStaticRuntime } from "../../slime/parser/expressions/SlimeUnaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser as SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime, com_slime_parser_expressions_SlimePrimaryExpressionParser$PrimaryStaticRuntime as PrimaryStaticRuntime } from "../../slime/parser/expressions/SlimePrimaryExpressionParser.ts";
import { com_slime_parser_literal_SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser as SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime, com_slime_parser_literal_SlimeLiteralParser$LiteralStaticRuntime as LiteralStaticRuntime } from "../../slime/parser/literal/SlimeLiteralParser.ts";
import { com_slime_parser_identifier_SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser as SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime, com_slime_parser_identifier_SlimeIdentifierParser$IdentifierStaticRuntime as IdentifierStaticRuntime } from "../../slime/parser/identifier/SlimeIdentifierParser.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../../slime/parser/base/SlimeJavascriptParserBase.ts";
import { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser as SubhutiParser, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime as StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticPrefix, com_subhuti_parser_SubhutiParser$StaticPrefix as StaticPrefix, com_subhuti_parser_SubhutiParser$StaticChoice, com_subhuti_parser_SubhutiParser$StaticChoice as StaticChoice } from "../../subhuti/parser/SubhutiParser.ts";
import { com_subhuti_parser_SubhutiParserFinal, com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal } from "../../subhuti/parser/SubhutiParserFinal.ts";
import { com_subhuti_parser_SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators as SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl as StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher as StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext as AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext as AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes as PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates as StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame as ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames as CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys as CurrentTokenKeys } from "../../subhuti/parser/SubhutiParserCombinators.ts";
import { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore as SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments as StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult as RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode as StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks as StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$CacheWork, com_subhuti_parser_SubhutiParserCore$CacheWork as CacheWork, com_subhuti_parser_SubhutiParserCore$FailureWork, com_subhuti_parser_SubhutiParserCore$FailureWork as FailureWork } from "../../subhuti/parser/SubhutiParserCore.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations as ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException as SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException as SubhutiFirstTokenUnknownException } from "../../subhuti/parser/SubhutiParserState.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../../subhuti/lookahead/SubhutiTokenLookahead.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __qin_java_functional } from "@qin/java-sdk-js";
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
class com_qin_parser_QinParser extends com_slime_parser_SlimeParser {
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_qin_parser_QinParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: QinParser/" + __qin_args.length);
  }
  __qin_constructor_com_qin_parser_QinParser_1_0(sourceCode: string): void {
    null;
  }
  QinModule(sourceType: com_slime_parser_SlimeJavascriptParser$SourceType): void {
    {
      const __qin_typed_receiver_2045: com_qin_parser_QinParser = this;
      __qin_typed_receiver_2045.Program(sourceType);
    }
    return null;
  }
  QinObjectDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.qin.parser.QinParser method=__qin_subhuti_raw_QinObjectDeclaration receiver=this arity=1 */ com_qin_parser_QinParser.prototype.__qin_subhuti_raw_QinObjectDeclaration.call(this, params);
    }), "QinObjectDeclaration", "QinParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_QinObjectDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.TSDecorators();
    }));
      {
        const __qin_typed_receiver_2046: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2046.QinObjectDeclarationBody(params);
      }
      return null;
    }
    {
      const __qin_typed_receiver_2047: com_qin_parser_QinParser = this;
      __qin_typed_receiver_2047.OptionalTSDecorators();
    }
    {
      const __qin_typed_receiver_2048: com_qin_parser_QinParser = this;
      __qin_typed_receiver_2048.QinObjectDeclarationBody(params);
    }
    return null;
  }
  QinObjectDeclarationBody(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.qin.parser.QinParser method=__qin_subhuti_raw_QinObjectDeclarationBody receiver=this arity=1 */ com_qin_parser_QinParser.prototype.__qin_subhuti_raw_QinObjectDeclarationBody.call(this, params);
    }), "QinObjectDeclarationBody", "QinParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_QinObjectDeclarationBody(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_2049: com_qin_parser_QinParser = this;
      __qin_typed_receiver_2049.consumeIdentifierValue("object");
    }
    {
      const __qin_typed_receiver_2050: com_qin_parser_QinParser = this;
      __qin_typed_receiver_2050.QinObjectName();
    }
    {
      const __qin_typed_receiver_2051: com_qin_parser_QinParser = this;
      __qin_typed_receiver_2051.ClassTail(params);
    }
    return null;
  }
  QinObjectName(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.qin.parser.QinParser method=__qin_subhuti_raw_QinObjectName receiver=this arity=0 */ com_qin_parser_QinParser.prototype.__qin_subhuti_raw_QinObjectName.call(this);
    }), "QinObjectName", "QinParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_QinObjectName(): void {
    if (false) {
      {
        const __qin_typed_receiver_2052: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2052.IdentifierName();
      }
      return null;
    }
    {
      const __qin_typed_receiver_2053: com_qin_parser_QinParser = this;
      __qin_typed_receiver_2053.IdentifierName();
    }
    return null;
  }
  Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.qin.parser.QinParser method=__qin_subhuti_raw_Declaration receiver=this arity=1 */ com_qin_parser_QinParser.prototype.__qin_subhuti_raw_Declaration.call(this, params);
    }), "Declaration", "QinParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=canStartQinObjectDeclaration receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.canStartQinObjectDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.QinObjectDeclaration(params);
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=canStartTSInterfaceDeclaration receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.canStartTSInterfaceDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSInterfaceDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=canStartTSTypeAliasDeclaration receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.canStartTSTypeAliasDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSTypeAliasDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=canStartTSEnumDeclaration receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.canStartTSEnumDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSEnumDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=canStartTSModuleDeclaration receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.canStartTSModuleDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSModuleDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=canStartTSDeclareStatement receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.canStartTSDeclareStatement.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.TSDeclareStatement();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=declarationStandardStart receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.declarationStandardStart.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.StandardDeclaration(params);
    })));
      return null;
    }
    if (this.canStartQinObjectDeclaration()) {
      {
        const __qin_typed_receiver_2054: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2054.QinObjectDeclaration(params);
      }
      return null;
    }
    if (this.canStartTSInterfaceDeclaration()) {
      {
        const __qin_typed_receiver_2055: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2055.TSInterfaceDeclaration();
      }
      return null;
    }
    if (this.canStartTSTypeAliasDeclaration()) {
      {
        const __qin_typed_receiver_2056: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2056.TSTypeAliasDeclaration();
      }
      return null;
    }
    if (this.canStartTSEnumDeclaration()) {
      {
        const __qin_typed_receiver_2057: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2057.TSEnumDeclaration();
      }
      return null;
    }
    if (this.canStartTSModuleDeclaration()) {
      {
        const __qin_typed_receiver_2058: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2058.TSModuleDeclaration();
      }
      return null;
    }
    if (this.canStartTSDeclareStatement()) {
      {
        const __qin_typed_receiver_2059: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2059.TSDeclareStatement();
      }
      return null;
    }
    if (this.declarationStandardStart()) {
      {
        const __qin_typed_receiver_2060: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2060.StandardDeclaration(params);
      }
      return null;
    }
    {
      const __qin_typed_receiver_2061: com_qin_parser_QinParser = this;
      __qin_typed_receiver_2061.setParseFail();
    }
    return null;
  }
  StatementListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.qin.parser.QinParser method=__qin_subhuti_raw_StatementListItem receiver=this arity=1 */ com_qin_parser_QinParser.prototype.__qin_subhuti_raw_StatementListItem.call(this, params);
    }), "StatementListItem", "QinParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_StatementListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=canStartQinObjectDeclaration receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.canStartQinObjectDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.Declaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(params.yield(), params.await(), false));
    })), __qin_java_functional(() => {
      return super.__qin_subhuti_raw_StatementListItem(params);
    }));
      return null;
    }
    if (this.canStartQinObjectDeclaration()) {
      {
        const __qin_typed_receiver_2062: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2062.Declaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(params.yield(), params.await(), false));
      }
      return null;
    }
    super.__qin_subhuti_raw_StatementListItem(params);
    return null;
  }
  canStartQinObjectDeclaration(): boolean {
    return this.canStartQinObjectDeclarationAt(1.0);
  }
  canStartQinObjectDeclarationAt(lookaheadOffset: number): boolean {
    if (this.matchIdentifierValue("object", lookaheadOffset)) {
      return true;
    }
    if ((!__QinJavaLangString.equals("At", this.tokenNameAt(lookaheadOffset)))) {
      return false;
    }
    return this.decoratedQinObjectDeclarationStart(lookaheadOffset);
  }
  canStartStatementListItemAt(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): boolean {
    return (this.canStartQinObjectDeclarationAt(lookaheadOffset) || super.canStartStatementListItemAt(lookaheadOffset, params));
  }
  decoratedQinObjectDeclarationStart(lookaheadOffset: number): boolean {
    let depth: number = 0.0;
    for (let offset: number = __qin_binary__("+", lookaheadOffset, 1.0); __qin_binary__("<=", offset, __qin_binary__("+", lookaheadOffset, 24.0)); offset++) {
      let tokenName: string = this.tokenNameAt(offset);
      if (__qin_binary__("==", tokenName, null)) {
        return false;
      }
      if ((__QinJavaLangString.equals("LParen", tokenName) || __QinJavaLangString.equals("LBracket", tokenName) || __QinJavaLangString.equals("LBrace", tokenName))) {
        depth++;
        continue;
      }
      if ((__QinJavaLangString.equals("RParen", tokenName) || __QinJavaLangString.equals("RBracket", tokenName) || __QinJavaLangString.equals("RBrace", tokenName))) {
        if (__qin_binary__("==", depth, 0.0)) {
          return false;
        }
        depth--;
        continue;
      }
      if ((__qin_binary__("==", depth, 0.0) && this.matchIdentifierValue("object", offset))) {
        return true;
      }
      if ((__qin_binary__("==", depth, 0.0) && (__QinJavaLangString.equals("Class", tokenName) || __QinJavaLangString.equals("Const", tokenName) || __QinJavaLangString.equals("Function", tokenName) || __QinJavaLangString.equals("Let", tokenName)))) {
        return false;
      }
    }
    return false;
  }
  ExportDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.qin.parser.QinParser method=__qin_subhuti_raw_ExportDeclaration receiver=this arity=0 */ com_qin_parser_QinParser.prototype.__qin_subhuti_raw_ExportDeclaration.call(this);
    }), "ExportDeclaration", "QinParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ExportDeclaration(): void {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2063: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2063.TSDecorators();
      }
      {
        const __qin_typed_receiver_2064: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2064.Export();
      }
      {
        const __qin_typed_receiver_2065: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2065.Default();
      }
      {
        const __qin_typed_receiver_2066: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2066.QinObjectDeclarationBody(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2067: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2067.TSDecorators();
      }
      {
        const __qin_typed_receiver_2068: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2068.Export();
      }
      {
        const __qin_typed_receiver_2069: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2069.QinObjectDeclarationBody(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2070: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2070.Export();
      }
      {
        const __qin_typed_receiver_2071: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2071.Default();
      }
      {
        const __qin_typed_receiver_2072: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2072.QinObjectDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2073: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2073.Export();
      }
      {
        const __qin_typed_receiver_2074: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2074.QinObjectDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
      }
      return null;
    }), __qin_java_functional(() => {
      return super.__qin_subhuti_raw_ExportDeclaration();
    }));
      return null;
    }
    if ((__QinJavaLangString.equals("At", this.tokenNameAt(1.0)) && this.decoratedQinObjectDeclarationStart(1.0))) {
      {
        const __qin_typed_receiver_2075: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2075.TSDecorators();
      }
      {
        const __qin_typed_receiver_2076: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2076.Export();
      }
      if (__QinJavaLangString.equals("Default", this.tokenNameAt(1.0))) {
        {
          const __qin_typed_receiver_2077: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
          __qin_typed_receiver_2077.Default();
        }
        {
          const __qin_typed_receiver_2078: com_qin_parser_QinParser = this;
          __qin_typed_receiver_2078.QinObjectDeclarationBody(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
        }
      } else {
        {
          const __qin_typed_receiver_2079: com_qin_parser_QinParser = this;
          __qin_typed_receiver_2079.QinObjectDeclarationBody(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
        }
      }
      return null;
    }
    if ((__QinJavaLangString.equals("Export", this.tokenNameAt(1.0)) && __QinJavaLangString.equals("Default", this.tokenNameAt(2.0)) && (__QinJavaLangString.equals("At", this.tokenNameAt(3.0)) || this.matchIdentifierValue("object", 3.0)))) {
      {
        const __qin_typed_receiver_2080: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2080.Export();
      }
      {
        const __qin_typed_receiver_2081: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2081.Default();
      }
      {
        const __qin_typed_receiver_2082: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2082.QinObjectDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
      }
      return null;
    }
    if ((__QinJavaLangString.equals("Export", this.tokenNameAt(1.0)) && (__QinJavaLangString.equals("At", this.tokenNameAt(2.0)) || this.matchIdentifierValue("object", 2.0)))) {
      {
        const __qin_typed_receiver_2083: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2083.Export();
      }
      {
        const __qin_typed_receiver_2084: com_qin_parser_QinParser = this;
        __qin_typed_receiver_2084.QinObjectDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
      }
      return null;
    }
    super.__qin_subhuti_raw_ExportDeclaration();
    return null;
  }
}
const QinParser = com_qin_parser_QinParser;

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_qin_parser_QinParser };
