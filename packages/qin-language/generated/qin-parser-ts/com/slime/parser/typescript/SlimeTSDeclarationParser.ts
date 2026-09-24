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
  TSInterfaceDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSInterfaceDeclaration receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSInterfaceDeclaration.call(this);
    }), "TSInterfaceDeclaration", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSInterfaceDeclaration(): void {
    {
      const __qin_typed_receiver_1307: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1307.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSInterfaceDeclaration", this.tsInterfaceDeclarationStaticRuntime());
    }
    return null;
  }
  TSInterfaceExtends(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSInterfaceExtends receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSInterfaceExtends.call(this);
    }), "TSInterfaceExtends", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSInterfaceExtends(): void {
    {
      const __qin_typed_receiver_1308: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1308.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSInterfaceExtends", this.tsInterfaceExtendsStaticRuntime());
    }
    return null;
  }
  TSInterfaceBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSInterfaceBody receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSInterfaceBody.call(this);
    }), "TSInterfaceBody", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSInterfaceBody(): void {
    {
      const __qin_typed_receiver_1309: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1309.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSInterfaceBody", this.tsInterfaceBodyStaticRuntime());
    }
    return null;
  }
  TSTypeAliasDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSTypeAliasDeclaration receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSTypeAliasDeclaration.call(this);
    }), "TSTypeAliasDeclaration", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSTypeAliasDeclaration(): void {
    {
      const __qin_typed_receiver_1310: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1310.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSTypeAliasDeclaration", this.tsTypeAliasDeclarationStaticRuntime());
    }
    return null;
  }
  TSEnumDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSEnumDeclaration receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSEnumDeclaration.call(this);
    }), "TSEnumDeclaration", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSEnumDeclaration(): void {
    {
      const __qin_typed_receiver_1311: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1311.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSEnumDeclaration", this.tsEnumDeclarationStaticRuntime());
    }
    return null;
  }
  TSEnumBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSEnumBody receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSEnumBody.call(this);
    }), "TSEnumBody", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSEnumBody(): void {
    {
      const __qin_typed_receiver_1312: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1312.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSEnumBody", this.tsEnumBodyStaticRuntime());
    }
    return null;
  }
  TSEnumMemberList(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSEnumMemberList receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSEnumMemberList.call(this);
    }), "TSEnumMemberList", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSEnumMemberList(): void {
    {
      const __qin_typed_receiver_1313: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1313.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSEnumMemberList", this.tsEnumMemberListStaticRuntime());
    }
    return null;
  }
  TSEnumMember(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSEnumMember receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSEnumMember.call(this);
    }), "TSEnumMember", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSEnumMember(): void {
    {
      const __qin_typed_receiver_1314: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1314.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSEnumMember", this.tsEnumMemberStaticRuntime());
    }
    return null;
  }
  TSEnumMemberInitializer(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSEnumMemberInitializer receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSEnumMemberInitializer.call(this);
    }), "TSEnumMemberInitializer", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSEnumMemberInitializer(): void {
    {
      const __qin_typed_receiver_1315: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1315.Assign();
    }
    {
      const __qin_typed_receiver_1316: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1316.AssignmentExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, false, false));
    }
    return null;
  }
  TSModuleDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSModuleDeclaration receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSModuleDeclaration.call(this);
    }), "TSModuleDeclaration", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSModuleDeclaration(): void {
    {
      const __qin_typed_receiver_1317: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1317.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSModuleDeclaration", this.tsModuleDeclarationStaticRuntime());
    }
    return null;
  }
  TSModuleName(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSModuleName receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSModuleName.call(this);
    }), "TSModuleName", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSModuleName(): void {
    {
      const __qin_typed_receiver_1318: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1318.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSModuleName", this.tsModuleNameStaticRuntime());
    }
    return null;
  }
  TSModuleBlock(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSModuleBlock receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSModuleBlock.call(this);
    }), "TSModuleBlock", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSModuleBlock(): void {
    {
      const __qin_typed_receiver_1319: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1319.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSModuleBlock", this.tsModuleBlockStaticRuntime());
    }
    return null;
  }
  TSDeclareStatement(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSDeclareStatement receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSDeclareStatement.call(this);
    }), "TSDeclareStatement", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSDeclareStatement(): void {
    {
      const __qin_typed_receiver_1320: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1320.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSDeclareStatement", this.tsDeclareStatementStaticRuntime());
    }
    return null;
  }
  TSImportType(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSImportType receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSImportType.call(this);
    }), "TSImportType", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSImportType(): void {
    {
      const __qin_typed_receiver_1321: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1321.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSImportType", this.tsImportTypeStaticRuntime());
    }
    return null;
  }
  TSExportAssignment(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSExportAssignment receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSExportAssignment.call(this);
    }), "TSExportAssignment", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSExportAssignment(): void {
    {
      const __qin_typed_receiver_1322: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1322.Export();
    }
    {
      const __qin_typed_receiver_1323: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1323.Assign();
    }
    {
      const __qin_typed_receiver_1324: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1324.Expression();
    }
    {
      const __qin_typed_receiver_1325: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1325.SemicolonASI();
    }
    return null;
  }
  TSExpressionWithTypeArguments(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSExpressionWithTypeArguments receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSExpressionWithTypeArguments.call(this);
    }), "TSExpressionWithTypeArguments", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSExpressionWithTypeArguments(): void {
    {
      const __qin_typed_receiver_1326: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1326.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSExpressionWithTypeArguments", this.tsExpressionWithTypeArgumentsStaticRuntime());
    }
    return null;
  }
  TSClassImplements(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSClassImplements receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSClassImplements.call(this);
    }), "TSClassImplements", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSClassImplements(): void {
    {
      const __qin_typed_receiver_1327: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1327.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSClassImplements", this.tsClassImplementsStaticRuntime());
    }
    return null;
  }
  TSModuleIdentifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSModuleIdentifier receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSModuleIdentifier.call(this);
    }), "TSModuleIdentifier", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSModuleIdentifier(): void {
    {
      const __qin_typed_receiver_1328: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1328.TSModuleName();
    }
    return null;
  }
  tsInterfaceDeclarationStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceDeclarationStaticRuntime(this);
  }
  tsInterfaceExtendsStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceExtendsStaticRuntime(this);
  }
  tsInterfaceBodyStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSInterfaceBodyStaticRuntime(this);
  }
  tsTypeAliasDeclarationStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSTypeAliasDeclarationStaticRuntime(this);
  }
  tsExpressionWithTypeArgumentsStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSExpressionWithTypeArgumentsStaticRuntime(this);
  }
  TSThisParameter(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSThisParameter receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSThisParameter.call(this);
    }), "TSThisParameter", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSThisParameter(): void {
    {
      const __qin_typed_receiver_1329: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1329.This();
    }
    {
      const __qin_typed_receiver_1330: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1330.TSTypeAnnotation();
    }
    return null;
  }
  TSParameterProperty(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSParameterProperty receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSParameterProperty.call(this);
    }), "TSParameterProperty", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSParameterProperty(): void {
    {
      const __qin_typed_receiver_1331: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1331.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSParameterProperty", this.tsParameterPropertyStaticRuntime());
    }
    return null;
  }
  TSAccessibilityModifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.typescript.SlimeTSDeclarationParser method=__qin_subhuti_raw_TSAccessibilityModifier receiver=this arity=0 */ com_slime_parser_typescript_SlimeTSDeclarationParser.prototype.__qin_subhuti_raw_TSAccessibilityModifier.call(this);
    }), "TSAccessibilityModifier", "SlimeTSDeclarationParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSAccessibilityModifier(): void {
    {
      const __qin_typed_receiver_1332: com_slime_parser_typescript_SlimeTSDeclarationParser = this;
      __qin_typed_receiver_1332.executeStaticRule(com_slime_parser_typescript_SlimeTSDeclarationParser.__qin_field_STATIC_TS_DECLARATION_GRAMMAR, "TSAccessibilityModifier", new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime());
    }
    return null;
  }
  canStartTSAccessibilityModifier(lookaheadOffset: number): boolean {
    return (this.matchIdentifierValue("public", lookaheadOffset) || this.matchIdentifierValue("private", lookaheadOffset) || this.matchIdentifierValue("protected", lookaheadOffset));
  }
  canStartTSParameterPropertyExpression(lookaheadOffset: number): boolean {
    return __qin_binary__("!=", this.tokenNameAt(lookaheadOffset), null);
  }
  tsParameterPropertyStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsClassImplementsStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSClassImplementsStaticRuntime(this);
  }
  tsDeclareStatementStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_subhuti_parser_SubhutiParser$StaticGrammarRuntime();
  }
  tsEnumDeclarationStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumDeclarationStaticRuntime(this);
  }
  tsEnumBodyStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumBodyStaticRuntime(this);
  }
  tsEnumMemberListStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberListStaticRuntime(this);
  }
  tsEnumMemberStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSEnumMemberStaticRuntime(this);
  }
  canStartTSEnumMember(lookaheadOffset: number): boolean {
    return (this.canStartIdentifier(lookaheadOffset) || __QinJavaLangString.equals("StringLiteral", this.tokenNameAt(lookaheadOffset)));
  }
  tsModuleDeclarationStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleDeclarationStaticRuntime(this);
  }
  tsModuleNameStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleNameStaticRuntime(this);
  }
  tsModuleBlockStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_typescript_SlimeTSDeclarationParser$TSModuleBlockStaticRuntime(this);
  }
  canStartTSModuleName(lookaheadOffset: number): boolean {
    return (this.canStartIdentifier(lookaheadOffset) || __QinJavaLangString.equals("StringLiteral", this.tokenNameAt(lookaheadOffset)));
  }
  tsImportTypeStaticRuntime(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
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
  testStaticGate(gateId: string): boolean {
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceDeclaration static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      {
        const __qin_typed_receiver_1333: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1333.Identifier();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      {
        const __qin_typed_receiver_1334: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1334.TSTypeParameterDeclaration();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSInterfaceExtends", ruleName)) {
      {
        const __qin_typed_receiver_1335: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1335.TSInterfaceExtends();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSInterfaceBody", ruleName)) {
      {
        const __qin_typed_receiver_1336: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1336.TSInterfaceBody();
      }
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceExtends static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSExpressionWithTypeArguments", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceExtends static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceExtends static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSExpressionWithTypeArguments", ruleName)) {
      {
        const __qin_typed_receiver_1337: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1337.TSExpressionWithTypeArguments();
      }
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceBody static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSTypeLiteral", ruleName)) {
      return this.__qin_field_parser.canStartTSTypeLiteral(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceBody static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSInterfaceBody static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeLiteral", ruleName)) {
      {
        const __qin_typed_receiver_1338: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1338.TSTypeLiteral();
      }
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSTypeAliasDeclaration static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      {
        const __qin_typed_receiver_1339: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1339.Identifier();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeParameterDeclaration", ruleName)) {
      {
        const __qin_typed_receiver_1340: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1340.TSTypeParameterDeclaration();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSType", ruleName)) {
      {
        const __qin_typed_receiver_1341: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1341.TSType();
      }
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSExpressionWithTypeArguments static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSTypeName", ruleName)) {
      {
        const __qin_typed_receiver_1342: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1342.TSTypeName();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSTypeParameterInstantiation", ruleName)) {
      {
        const __qin_typed_receiver_1343: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1343.TSTypeParameterInstantiation();
      }
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSClassImplements static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSExpressionWithTypeArguments", ruleName)) {
      return this.__qin_field_parser.canStartIdentifier(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSClassImplements static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSClassImplements static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSExpressionWithTypeArguments", ruleName)) {
      {
        const __qin_typed_receiver_1344: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1344.TSExpressionWithTypeArguments();
      }
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumDeclaration static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      {
        const __qin_typed_receiver_1345: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1345.Identifier();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSEnumBody", ruleName)) {
      {
        const __qin_typed_receiver_1346: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1346.TSEnumBody();
      }
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumBody static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSEnumMemberList", ruleName)) {
      return this.__qin_field_parser.canStartTSEnumMember(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumBody static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumBody static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSEnumMemberList", ruleName)) {
      {
        const __qin_typed_receiver_1347: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1347.TSEnumMemberList();
      }
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumMemberList static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("TSEnumMember", ruleName)) {
      return this.__qin_field_parser.canStartTSEnumMember(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumMemberList static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumMemberList static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSEnumMember", ruleName)) {
      {
        const __qin_typed_receiver_1348: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1348.TSEnumMember();
      }
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSEnumMember static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      {
        const __qin_typed_receiver_1349: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1349.Identifier();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("StringLiteral", ruleName)) {
      {
        const __qin_typed_receiver_1350: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1350.StringLiteral();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSEnumMemberInitializer", ruleName)) {
      {
        const __qin_typed_receiver_1351: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1351.TSEnumMemberInitializer();
      }
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleDeclaration static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("TSModuleName", ruleName)) {
      {
        const __qin_typed_receiver_1352: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1352.TSModuleName();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TSModuleBlock", ruleName)) {
      {
        const __qin_typed_receiver_1353: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1353.TSModuleBlock();
      }
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleName static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Identifier", ruleName)) {
      {
        const __qin_typed_receiver_1354: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1354.Identifier();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("StringLiteral", ruleName)) {
      {
        const __qin_typed_receiver_1355: com_slime_parser_typescript_SlimeTSDeclarationParser = this.__qin_field_parser;
        __qin_typed_receiver_1355.StringLiteral();
      }
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleBlock static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("ModuleBody", ruleName)) {
      let tokenName: string = this.__qin_field_parser.tokenNameAt(lookaheadOffset);
      return (__qin_binary__("!=", tokenName, null) && !__QinJavaLangString.equals("RBrace", tokenName));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported TSModuleBlock static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
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
