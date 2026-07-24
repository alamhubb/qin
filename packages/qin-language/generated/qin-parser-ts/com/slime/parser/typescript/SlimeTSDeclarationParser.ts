import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "../../../subhuti/parser/SubhutiStaticGrammar.ts";
import { com_slime_parser_typescript_SlimeTSTypeParser, com_slime_parser_typescript_SlimeTSTypeParser as SlimeTSTypeParser, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeStaticRuntime as TSTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrimaryTypeStaticRuntime as TSPrimaryTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeLiteralStaticRuntime as TSTypeLiteralStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeReferenceStaticRuntime as TSTypeReferenceStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameStaticRuntime as TSTypeNameStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeNameSuffixStaticRuntime as TSTypeNameSuffixStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterInstantiationStaticRuntime as TSTypeParameterInstantiationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPrefixTypeOrPrimaryStaticRuntime as TSPrefixTypeOrPrimaryStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeOperandStaticRuntime as TSTypeOperandStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterDeclarationStaticRuntime as TSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSKeywordTypeStaticRuntime as TSKeywordTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSUnionOrIntersectionTypeStaticRuntime as TSUnionOrIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSIntersectionTypeStaticRuntime as TSIntersectionTypeStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSPropertyOrMethodSignatureStaticRuntime as TSPropertyOrMethodSignatureStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterListStaticRuntime as TSParameterListStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSParameterStaticRuntime as TSParameterStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeAnnotationStaticRuntime as TSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeAnnotationStaticRuntime as OptionalTSTypeAnnotationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$OptionalTSTypeParameterDeclarationStaticRuntime as OptionalTSTypeParameterDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime, com_slime_parser_typescript_SlimeTSTypeParser$TSTypeParameterStaticRuntime as TSTypeParameterStaticRuntime } from "./SlimeTSTypeParser.ts";
import { com_slime_parser_typescript_SlimeTSDeclarationStaticGrammar, com_slime_parser_typescript_SlimeTSDeclarationStaticGrammar as SlimeTSDeclarationStaticGrammar } from "./SlimeTSDeclarationStaticGrammar.ts";
import { com_slime_parser_SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser as SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser$SourceType, com_slime_parser_SlimeJavascriptParser$SourceType as SourceType, com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime, com_slime_parser_SlimeJavascriptParser$JavascriptStaticRuntime as JavascriptStaticRuntime } from "../SlimeJavascriptParser.ts";
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
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../base/SlimeJavascriptParserBase.ts";
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
const UnsupportedOperationException = __QinJavaLangUnsupportedOperationException;
class com_slime_parser_typescript_SlimeTSDeclarationParser extends com_slime_parser_typescript_SlimeTSTypeParser {
  static __qin_field_STATIC_TS_DECLARATION_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser_1_0(sourceCode: string): void {
    null;
  }
  TSInterfaceDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSInterfaceDeclaration();
    }), "TSInterfaceDeclaration", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSInterfaceDeclaration(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSInterfaceDeclaration", this.tsInterfaceDeclarationStaticRuntime());
    return null;
  }
  TSInterfaceExtends(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSInterfaceExtends();
    }), "TSInterfaceExtends", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSInterfaceExtends(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSInterfaceExtends", this.tsInterfaceExtendsStaticRuntime());
    return null;
  }
  TSInterfaceBody(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSInterfaceBody();
    }), "TSInterfaceBody", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSInterfaceBody(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSInterfaceBody", this.tsInterfaceBodyStaticRuntime());
    return null;
  }
  TSTypeAliasDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeAliasDeclaration();
    }), "TSTypeAliasDeclaration", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeAliasDeclaration(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSTypeAliasDeclaration", this.tsTypeAliasDeclarationStaticRuntime());
    return null;
  }
  TSEnumDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSEnumDeclaration();
    }), "TSEnumDeclaration", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSEnumDeclaration(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSEnumDeclaration", this.tsEnumDeclarationStaticRuntime());
    return null;
  }
  TSEnumBody(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSEnumBody();
    }), "TSEnumBody", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSEnumBody(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSEnumBody", this.tsEnumBodyStaticRuntime());
    return null;
  }
  TSEnumMemberList(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSEnumMemberList();
    }), "TSEnumMemberList", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSEnumMemberList(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSEnumMemberList", this.tsEnumMemberListStaticRuntime());
    return null;
  }
  TSEnumMember(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSEnumMember();
    }), "TSEnumMember", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSEnumMember(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSEnumMember", this.tsEnumMemberStaticRuntime());
    return null;
  }
  TSEnumMemberInitializer(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSEnumMemberInitializer();
    }), "TSEnumMemberInitializer", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSEnumMemberInitializer(): any {
    this.__qin_field_tokenConsumer.Assign();
    this.AssignmentExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, false, false));
    return null;
  }
  TSModuleDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSModuleDeclaration();
    }), "TSModuleDeclaration", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSModuleDeclaration(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSModuleDeclaration", this.tsModuleDeclarationStaticRuntime());
    return null;
  }
  TSModuleName(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSModuleName();
    }), "TSModuleName", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSModuleName(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSModuleName", this.tsModuleNameStaticRuntime());
    return null;
  }
  TSModuleBlock(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSModuleBlock();
    }), "TSModuleBlock", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSModuleBlock(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSModuleBlock", this.tsModuleBlockStaticRuntime());
    return null;
  }
  TSDeclareStatement(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSDeclareStatement();
    }), "TSDeclareStatement", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSDeclareStatement(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSDeclareStatement", this.tsDeclareStatementStaticRuntime());
    return null;
  }
  TSImportType(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSImportType();
    }), "TSImportType", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSImportType(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSImportType", this.tsImportTypeStaticRuntime());
    return null;
  }
  TSExportAssignment(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSExportAssignment();
    }), "TSExportAssignment", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSExportAssignment(): any {
    this.__qin_field_tokenConsumer.Export();
    this.__qin_field_tokenConsumer.Assign();
    this.Expression();
    this.SemicolonASI();
    return null;
  }
  TSExpressionWithTypeArguments(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSExpressionWithTypeArguments();
    }), "TSExpressionWithTypeArguments", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSExpressionWithTypeArguments(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSExpressionWithTypeArguments", this.tsExpressionWithTypeArgumentsStaticRuntime());
    return null;
  }
  TSClassImplements(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSClassImplements();
    }), "TSClassImplements", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSClassImplements(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSClassImplements", this.tsClassImplementsStaticRuntime());
    return null;
  }
  TSModuleIdentifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSModuleIdentifier();
    }), "TSModuleIdentifier", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSModuleIdentifier(): any {
    this.TSModuleName();
    return null;
  }
  tsInterfaceDeclarationStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime(this);
  }
  tsInterfaceExtendsStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime(this);
  }
  tsInterfaceBodyStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime(this);
  }
  tsTypeAliasDeclarationStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime(this);
  }
  tsExpressionWithTypeArgumentsStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime(this);
  }
  TSThisParameter(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSThisParameter();
    }), "TSThisParameter", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSThisParameter(): any {
    this.__qin_field_tokenConsumer.This();
    this.TSTypeAnnotation();
    return null;
  }
  TSParameterProperty(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSParameterProperty();
    }), "TSParameterProperty", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSParameterProperty(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSParameterProperty", this.tsParameterPropertyStaticRuntime());
    return null;
  }
  TSAccessibilityModifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSAccessibilityModifier();
    }), "TSAccessibilityModifier", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSAccessibilityModifier(): any {
    this.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSAccessibilityModifier", new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime());
    return null;
  }
  canStartTSAccessibilityModifier(lookaheadOffset: number): any {
    return (this.matchIdentifierValue("public", lookaheadOffset) || this.matchIdentifierValue("private", lookaheadOffset) || this.matchIdentifierValue("protected", lookaheadOffset));
  }
  canStartTSParameterPropertyExpression(lookaheadOffset: number): any {
    return __qin_binary__("!=", this.tokenNameAt(lookaheadOffset), null);
  }
  tsParameterPropertyStaticRuntime(): any {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsClassImplementsStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSClassImplementsStaticRuntime(this);
  }
  tsDeclareStatementStaticRuntime(): any {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsEnumDeclarationStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime(this);
  }
  tsEnumBodyStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumBodyStaticRuntime(this);
  }
  tsEnumMemberListStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime(this);
  }
  tsEnumMemberStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberStaticRuntime(this);
  }
  canStartTSEnumMember(lookaheadOffset: number): any {
    return (this.canStartIdentifier(lookaheadOffset) || __QinJavaLangString.equals("StringLiteral", this.tokenNameAt(lookaheadOffset)));
  }
  tsModuleDeclarationStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime(this);
  }
  tsModuleNameStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleNameStaticRuntime(this);
  }
  tsModuleBlockStaticRuntime(): any {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleBlockStaticRuntime(this);
  }
  canStartTSModuleName(lookaheadOffset: number): any {
    return (this.canStartIdentifier(lookaheadOffset) || __QinJavaLangString.equals("StringLiteral", this.tokenNameAt(lookaheadOffset)));
  }
  tsImportTypeStaticRuntime(): any {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
}
const SlimeTSDeclarationParser = com_slime_parser_typescript_SlimeTSDeclarationParser;
class com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSDeclarationParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSDeclarationParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSDeclarationParser): void {
    this.__qin_field_parser = null;
    this.__qin_field_parser = parser;
  }
  testStaticGate(gateId: string): any {
    if (__QinJavaLangString.equals("TSDeclareStatement.constEnumStart", gateId)) {
      return (__QinJavaLangString.equals("Const", this.__qin_field_parser.tokenNameAt(1.0)) && __QinJavaLangString.equals("Enum", this.__qin_field_parser.tokenNameAt(2.0)));
    }
    if (__QinJavaLangString.equals("TSDeclareStatement.constVarStart", gateId)) {
      return (__QinJavaLangString.equals("Const", this.__qin_field_parser.tokenNameAt(1.0)) && !__QinJavaLangString.equals("Enum", this.__qin_field_parser.tokenNameAt(2.0)));
    }
    if (__QinJavaLangString.equals("TSDeclareStatement.letVarStart", gateId)) {
      return (__QinJavaLangString.equals("Let", this.__qin_field_parser.tokenNameAt(1.0)) || __QinJavaLangString.equals("Var", this.__qin_field_parser.tokenNameAt(1.0)));
    }
    if (__QinJavaLangString.equals("TSDeclareStatement.functionStart", gateId)) {
      return __QinJavaLangString.equals("Function", this.__qin_field_parser.tokenNameAt(1.0));
    }
    if (__QinJavaLangString.equals("TSDeclareStatement.classStart", gateId)) {
      return __QinJavaLangString.equals("Class", this.__qin_field_parser.tokenNameAt(1.0));
    }
    if (__QinJavaLangString.equals("TSDeclareStatement.interfaceStart", gateId)) {
      return this.__qin_field_parser.matchIdentifierValue("interface", 1.0);
    }
    if (__QinJavaLangString.equals("TSDeclareStatement.typeStart", gateId)) {
      return this.__qin_field_parser.matchIdentifierValue("type", 1.0);
    }
    if (__QinJavaLangString.equals("TSDeclareStatement.enumStart", gateId)) {
      return __QinJavaLangString.equals("Enum", this.__qin_field_parser.tokenNameAt(1.0));
    }
    if (__QinJavaLangString.equals("TSDeclareStatement.moduleStart", gateId)) {
      return (this.__qin_field_parser.matchIdentifierValue("namespace", 1.0) || this.__qin_field_parser.matchIdentifierValue("module", 1.0));
    }
    if (__QinJavaLangString.equals("TSDeclareStatement.globalStart", gateId)) {
      return this.__qin_field_parser.matchIdentifierValue("global", 1.0);
    }
    return false;
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceDeclaration static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      return __QinJavaLangString.equals("Less", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSInterfaceExtends", ruleName)) {
      return __QinJavaLangString.equals("Extends", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSInterfaceBody", ruleName)) {
      return this.__qin_field_parser.canStartTSTypeLiteral(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceDeclaration static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceDeclaration static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      this.__qin_field_parser.Identifier();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      this.__qin_field_parser.TSTypeParameterDeclaration();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSInterfaceExtends", ruleName)) {
      this.__qin_field_parser.TSInterfaceExtends();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSInterfaceBody", ruleName)) {
      this.__qin_field_parser.TSInterfaceBody();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceDeclaration static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime = com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime;
class com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSDeclarationParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSDeclarationParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSDeclarationParser): void {
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceExtends static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSExpressionWithTypeArguments", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceExtends static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceExtends static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSExpressionWithTypeArguments", ruleName)) {
      this.__qin_field_parser.TSExpressionWithTypeArguments();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceExtends static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime = com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime;
class com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSDeclarationParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSDeclarationParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSDeclarationParser): void {
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceBody static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeLiteral", ruleName)) {
      return this.__qin_field_parser.canStartTSTypeLiteral(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceBody static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceBody static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeLiteral", ruleName)) {
      this.__qin_field_parser.TSTypeLiteral();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceBody static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime = com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime;
class com_slime_parser_typescript_SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSDeclarationParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSDeclarationParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSDeclarationParser): void {
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeAliasDeclaration static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      return __QinJavaLangString.equals("Less", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      return this.__qin_field_parser.canStartTSType(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeAliasDeclaration static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeAliasDeclaration static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      this.__qin_field_parser.Identifier();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      this.__qin_field_parser.TSTypeParameterDeclaration();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      this.__qin_field_parser.TSType();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeAliasDeclaration static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime = com_slime_parser_typescript_SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime;
class com_slime_parser_typescript_SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSDeclarationParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSDeclarationParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSDeclarationParser): void {
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSExpressionWithTypeArguments static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeName", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSTypeParameterInstantiation", ruleName)) {
      return __QinJavaLangString.equals("Less", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSExpressionWithTypeArguments static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSExpressionWithTypeArguments static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeName", ruleName)) {
      this.__qin_field_parser.TSTypeName();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeParameterInstantiation", ruleName)) {
      this.__qin_field_parser.TSTypeParameterInstantiation();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSExpressionWithTypeArguments static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime = com_slime_parser_typescript_SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime;
class com_slime_parser_typescript_SlimeTSDeclarationParser$TSClassImplementsStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSDeclarationParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSDeclarationParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSClassImplementsStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser$TSClassImplementsStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSClassImplementsStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSDeclarationParser): void {
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSClassImplements static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSExpressionWithTypeArguments", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSClassImplements static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSClassImplements static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSExpressionWithTypeArguments", ruleName)) {
      this.__qin_field_parser.TSExpressionWithTypeArguments();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSClassImplements static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSDeclarationParser$TSClassImplementsStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSDeclarationParser$TSClassImplementsStaticRuntime = com_slime_parser_typescript_SlimeTSDeclarationParser$TSClassImplementsStaticRuntime;
class com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSDeclarationParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSDeclarationParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSDeclarationParser): void {
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumDeclaration static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSEnumBody", ruleName)) {
      return __QinJavaLangString.equals("LBrace", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumDeclaration static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumDeclaration static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      this.__qin_field_parser.Identifier();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSEnumBody", ruleName)) {
      this.__qin_field_parser.TSEnumBody();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumDeclaration static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime = com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime;
class com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumBodyStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSDeclarationParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSDeclarationParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumBodyStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser$TSEnumBodyStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumBodyStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSDeclarationParser): void {
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumBody static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSEnumMemberList", ruleName)) {
      return this.__qin_field_parser.canStartTSEnumMember(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumBody static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumBody static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSEnumMemberList", ruleName)) {
      this.__qin_field_parser.TSEnumMemberList();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumBody static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumBodyStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSDeclarationParser$TSEnumBodyStaticRuntime = com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumBodyStaticRuntime;
class com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSDeclarationParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSDeclarationParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSDeclarationParser): void {
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumMemberList static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSEnumMember", ruleName)) {
      return this.__qin_field_parser.canStartTSEnumMember(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumMemberList static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumMemberList static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSEnumMember", ruleName)) {
      this.__qin_field_parser.TSEnumMember();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumMemberList static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime = com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime;
class com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSDeclarationParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSDeclarationParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser$TSEnumMemberStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSDeclarationParser): void {
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumMember static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("StringLiteral", ruleName)) {
      return __QinJavaLangString.equals("StringLiteral", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSEnumMemberInitializer", ruleName)) {
      return __QinJavaLangString.equals("Assign", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumMember static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumMember static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      this.__qin_field_parser.Identifier();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("StringLiteral", ruleName)) {
      this.__qin_field_parser.StringLiteral();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSEnumMemberInitializer", ruleName)) {
      this.__qin_field_parser.TSEnumMemberInitializer();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumMember static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSDeclarationParser$TSEnumMemberStaticRuntime = com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberStaticRuntime;
class com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSDeclarationParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSDeclarationParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSDeclarationParser): void {
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleDeclaration static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSModuleName", ruleName)) {
      return this.__qin_field_parser.canStartTSModuleName(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("TSModuleBlock", ruleName)) {
      return __QinJavaLangString.equals("LBrace", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleDeclaration static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleDeclaration static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSModuleName", ruleName)) {
      this.__qin_field_parser.TSModuleName();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSModuleBlock", ruleName)) {
      this.__qin_field_parser.TSModuleBlock();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleDeclaration static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime = com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime;
class com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleNameStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSDeclarationParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSDeclarationParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleNameStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser$TSModuleNameStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleNameStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSDeclarationParser): void {
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleName static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("StringLiteral", ruleName)) {
      return __QinJavaLangString.equals("StringLiteral", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleName static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleName static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      this.__qin_field_parser.Identifier();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("StringLiteral", ruleName)) {
      this.__qin_field_parser.StringLiteral();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleName static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleNameStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSDeclarationParser$TSModuleNameStaticRuntime = com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleNameStaticRuntime;
class com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleBlockStaticRuntime {
  __qin_field_parser: com_slime_parser_typescript_SlimeTSDeclarationParser | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_typescript_SlimeTSDeclarationParser))) {
      const parser: any = __qin_args[0];
      this.__qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleBlockStaticRuntime_1_0(parser);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeTSDeclarationParser$TSModuleBlockStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleBlockStaticRuntime_1_0(parser: com_slime_parser_typescript_SlimeTSDeclarationParser): void {
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleBlock static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("ModuleBody", ruleName)) {
      let tokenName: any = this.__qin_field_parser.tokenNameAt(lookaheadOffset);
      return (__qin_binary__("!=", tokenName, null) && !__QinJavaLangString.equals("RBrace", tokenName));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleBlock static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleBlock static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("ModuleBody", ruleName)) {
      return this.__qin_field_parser.executeModuleBodyStaticBody();
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleBlock static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleBlockStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeTSDeclarationParser$TSModuleBlockStaticRuntime = com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleBlockStaticRuntime;
com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR = com_slime_parser_typescript_SlimeTSDeclarationStaticGrammar.grammar();

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_typescript_SlimeTSDeclarationParser, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSClassImplementsStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumBodyStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleNameStaticRuntime, com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleBlockStaticRuntime };
