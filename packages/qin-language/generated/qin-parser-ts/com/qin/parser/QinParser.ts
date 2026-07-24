import { com_slime_parser_SlimeParser, com_slime_parser_SlimeParser as SlimeParser } from "../../slime/parser/SlimeParser.ts";
import { com_slime_parser_SlimeParserRuntimeBase, com_slime_parser_SlimeParserRuntimeBase as SlimeParserRuntimeBase, com_slime_parser_SlimeParserRuntimeBase$EmptyStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$EmptyStaticRuntime as EmptyStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootOptionalStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootOptionalStaticRuntime as TSRootOptionalStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootPrimaryStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootPrimaryStaticRuntime as TSRootPrimaryStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootFormalParametersStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootFormalParametersStaticRuntime as TSRootFormalParametersStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootMethodDefinitionStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSRootMethodDefinitionStaticRuntime as TSRootMethodDefinitionStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$ClassBindingStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$ClassBindingStaticRuntime as ClassBindingStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$RootClassTailStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$RootClassTailStaticRuntime as RootClassTailStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSModifierStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSModifierStaticRuntime as TSModifierStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSAsExpressionTailStaticRuntime, com_slime_parser_SlimeParserRuntimeBase$TSAsExpressionTailStaticRuntime as TSAsExpressionTailStaticRuntime } from "../../slime/parser/SlimeParserRuntimeBase.ts";
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
  QinModule(sourceType: com_slime_parser_SlimeJavascriptParser$SourceType): any {
    this.Program(sourceType);
    return null;
  }
  QinObjectDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_QinObjectDeclaration(params);
    }), "QinObjectDeclaration", "QinParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_QinObjectDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    if (false) {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.TSDecorators();
    }));
      this.QinObjectDeclarationBody(params);
      return null;
    }
    this.OptionalTSDecorators();
    this.QinObjectDeclarationBody(params);
    return null;
  }
  QinObjectDeclarationBody(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_QinObjectDeclarationBody(params);
    }), "QinObjectDeclarationBody", "QinParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_QinObjectDeclarationBody(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.consumeIdentifierValue("object");
    this.QinObjectName();
    this.ClassTail(params);
    return null;
  }
  QinObjectName(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_QinObjectName();
    }), "QinObjectName", "QinParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_QinObjectName(): any {
    if (false) {
      this.IdentifierName();
      return null;
    }
    this.IdentifierName();
    return null;
  }
  Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Declaration(params);
    }), "Declaration", "QinParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    if (false) {
      SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.canStartQinObjectDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.QinObjectDeclaration(params);
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.canStartTSInterfaceDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
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
    if (this.canStartQinObjectDeclaration()) {
      this.QinObjectDeclaration(params);
      return null;
    }
    if (this.canStartTSInterfaceDeclaration()) {
      this.TSInterfaceDeclaration();
      return null;
    }
    if (this.canStartTSTypeAliasDeclaration()) {
      this.TSTypeAliasDeclaration();
      return null;
    }
    if (this.canStartTSEnumDeclaration()) {
      this.TSEnumDeclaration();
      return null;
    }
    if (this.canStartTSModuleDeclaration()) {
      this.TSModuleDeclaration();
      return null;
    }
    if (this.canStartTSDeclareStatement()) {
      this.TSDeclareStatement();
      return null;
    }
    if (this.declarationStandardStart()) {
      this.StandardDeclaration(params);
      return null;
    }
    this.setParseFail();
    return null;
  }
  StatementListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_StatementListItem(params);
    }), "StatementListItem", "QinParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_StatementListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    if (false) {
      SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.canStartQinObjectDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.Declaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(params.__qin_yield(), params.__qin_await(), false));
    })), __qin_java_functional(() => {
      return super.__qin_subhuti_raw_StatementListItem(params);
    }));
      return null;
    }
    if (this.canStartQinObjectDeclaration()) {
      this.Declaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(params.__qin_yield(), params.__qin_await(), false));
      return null;
    }
    super.__qin_subhuti_raw_StatementListItem(params);
    return null;
  }
  canStartQinObjectDeclaration(): any {
    return this.canStartQinObjectDeclarationAt(1.0);
  }
  canStartQinObjectDeclarationAt(lookaheadOffset: number): any {
    if (this.matchIdentifierValue("object", lookaheadOffset)) {
      return true;
    }
    if ((!__QinJavaLangString.equals("At", this.tokenNameAt(lookaheadOffset)))) {
      return false;
    }
    return this.decoratedQinObjectDeclarationStart(lookaheadOffset);
  }
  canStartStatementListItemAt(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return (this.canStartQinObjectDeclarationAt(lookaheadOffset) || super.canStartStatementListItemAt(lookaheadOffset, params));
  }
  decoratedQinObjectDeclarationStart(lookaheadOffset: number): any {
    let depth: any = 0.0;
    for (let offset: any = __qin_binary__("+", lookaheadOffset, 1.0); __qin_binary__("<=", offset, __qin_binary__("+", lookaheadOffset, 24.0)); offset++) {
      let tokenName: any = this.tokenNameAt(offset);
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
  ExportDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ExportDeclaration();
    }), "ExportDeclaration", "QinParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ExportDeclaration(): any {
    if (false) {
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      this.TSDecorators();
      this.__qin_field_tokenConsumer.Export();
      this.__qin_field_tokenConsumer.Default();
      this.QinObjectDeclarationBody(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
      return null;
    }), __qin_java_functional(() => {
      this.TSDecorators();
      this.__qin_field_tokenConsumer.Export();
      this.QinObjectDeclarationBody(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Export();
      this.__qin_field_tokenConsumer.Default();
      this.QinObjectDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Export();
      this.QinObjectDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
      return null;
    }), __qin_java_functional(() => {
      return super.__qin_subhuti_raw_ExportDeclaration();
    }));
      return null;
    }
    if ((__QinJavaLangString.equals("At", this.tokenNameAt(1.0)) && this.decoratedQinObjectDeclarationStart(1.0))) {
      this.TSDecorators();
      this.__qin_field_tokenConsumer.Export();
      if (__QinJavaLangString.equals("Default", this.tokenNameAt(1.0))) {
        this.__qin_field_tokenConsumer.Default();
        this.QinObjectDeclarationBody(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
      } else {
        this.QinObjectDeclarationBody(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
      }
      return null;
    }
    if ((__QinJavaLangString.equals("Export", this.tokenNameAt(1.0)) && __QinJavaLangString.equals("Default", this.tokenNameAt(2.0)) && (__QinJavaLangString.equals("At", this.tokenNameAt(3.0)) || this.matchIdentifierValue("object", 3.0)))) {
      this.__qin_field_tokenConsumer.Export();
      this.__qin_field_tokenConsumer.Default();
      this.QinObjectDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
      return null;
    }
    if ((__QinJavaLangString.equals("Export", this.tokenNameAt(1.0)) && (__QinJavaLangString.equals("At", this.tokenNameAt(2.0)) || this.matchIdentifierValue("object", 2.0)))) {
      this.__qin_field_tokenConsumer.Export();
      this.QinObjectDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
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
