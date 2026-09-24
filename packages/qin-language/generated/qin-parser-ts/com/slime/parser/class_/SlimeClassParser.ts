import { com_slime_parser_function_SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser as SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime, com_slime_parser_function_SlimeFunctionParser$FunctionStaticRuntime as FunctionStaticRuntime } from "../function/SlimeFunctionParser.ts";
import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "../../../subhuti/parser/SubhutiStaticGrammar.ts";
import { com_slime_parser_class__SlimeClassStaticGrammar, com_slime_parser_class__SlimeClassStaticGrammar as SlimeClassStaticGrammar } from "./SlimeClassStaticGrammar.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../base/SlimeJavascriptParserBase.ts";
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
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__, __QinJavaLangThrowable, __QinJavaLangException, __QinJavaLangRuntimeException, __QinJavaLangReflectiveOperationException, __QinJavaLangClassNotFoundException, __QinJavaLangClassCastException, __QinJavaLangNoSuchMethodException, __QinJavaLangReflectInvocationTargetException, __QinJavaLangError, __QinJavaLangStackOverflowError, __QinJavaLangIllegalArgumentException, __QinJavaLangNumberFormatException, __QinJavaLangIllegalStateException, __QinJavaLangUnsupportedOperationException, __QinJavaIoIOException, __qin_java_functional, __QinJavaUtilObjects } from "@qin/java-sdk-js";
import { __qin_subhuti_rule_cache_key } from "@qin/java-sdk-js/tooling";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
const UnsupportedOperationException = __QinJavaLangUnsupportedOperationException;
class com_slime_parser_class__SlimeClassParser extends com_slime_parser_function_SlimeFunctionParser {
  static __qin_field_STATIC_CLASS_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_class__SlimeClassParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeClassParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_class__SlimeClassParser_1_0(sourceCode: string): void {
    null;
  }
  ClassDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ClassDeclaration receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ClassDeclaration.call(this, params);
    }), "ClassDeclaration", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_967: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_967.Class();
    }
    if (params.isDefault()) {
      {
        const __qin_typed_receiver_968: com_slime_parser_class__SlimeClassParser = this;
        __qin_typed_receiver_968.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "OptionalClassBindingIdentifier", this.classStaticRuntime(null, new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await())));
      }
    } else {
      {
        const __qin_typed_receiver_969: com_slime_parser_class__SlimeClassParser = this;
        __qin_typed_receiver_969.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "RequiredClassBindingIdentifier", this.classStaticRuntime(null, new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await())));
      }
    }
    if (this.isParserFail()) {
      return null;
    }
    {
      const __qin_typed_receiver_970: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_970.ClassTail(params);
    }
    return null;
  }
  ClassExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ClassExpression receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ClassExpression.call(this, params);
    }), "ClassExpression", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_971: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_971.Class();
    }
    {
      const __qin_typed_receiver_972: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_972.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "OptionalClassBindingIdentifier", this.classStaticRuntime(null, params));
    }
    {
      const __qin_typed_receiver_973: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_973.ClassTail(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(params.yield(), params.await(), false));
    }
    return null;
  }
  ClassTail(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ClassTail receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ClassTail.call(this, params);
    }), "ClassTail", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassTail(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_974: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_974.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassTail", this.classStaticRuntime(params));
    }
    return null;
  }
  ClassHeritage(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ClassHeritage receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ClassHeritage.call(this, params);
    }), "ClassHeritage", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassHeritage(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_975: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_975.Extends();
    }
    {
      const __qin_typed_receiver_976: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_976.LeftHandSideExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
    }
    return null;
  }
  ClassBody(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ClassBody receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ClassBody.call(this, params);
    }), "ClassBody", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassBody(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_977: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_977.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassBody", this.classStaticRuntime(params));
    }
    return null;
  }
  ClassElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ClassElementList receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ClassElementList.call(this, params);
    }), "ClassElementList", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_978: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_978.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassElementList", this.classStaticRuntime(params));
    }
    return null;
  }
  ClassElement(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ClassElement receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ClassElement.call(this, params);
    }), "ClassElement", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassElement(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_979: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_979.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassElement", this.classStaticRuntime(params, new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await())));
    }
    return null;
  }
  MethodDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_MethodDefinition receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_MethodDefinition.call(this, params);
    }), "MethodDefinition", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_MethodDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_980: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_980.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "MethodDefinition", this.classStaticRuntime(null, params));
    }
    return null;
  }
  GeneratorMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_GeneratorMethod receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_GeneratorMethod.call(this, params);
    }), "GeneratorMethod", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_GeneratorMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_981: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_981.Asterisk();
    }
    {
      const __qin_typed_receiver_982: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_982.ClassElementName(params);
    }
    {
      const __qin_typed_receiver_983: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_983.LParen();
    }
    {
      const __qin_typed_receiver_984: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_984.UniqueFormalParameters();
    }
    {
      const __qin_typed_receiver_985: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_985.RParen();
    }
    {
      const __qin_typed_receiver_986: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_986.LBrace();
    }
    {
      const __qin_typed_receiver_987: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_987.GeneratorBody();
    }
    {
      const __qin_typed_receiver_988: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_988.RBrace();
    }
    return null;
  }
  AsyncMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_AsyncMethod receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_AsyncMethod.call(this, params);
    }), "AsyncMethod", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_989: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_989.consumeIdentifierValue("async");
    }
    {
      const __qin_typed_receiver_990: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_990.assertNoLineBreak();
    }
    {
      const __qin_typed_receiver_991: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_991.ClassElementName(params);
    }
    {
      const __qin_typed_receiver_992: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_992.LParen();
    }
    {
      const __qin_typed_receiver_993: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_993.UniqueFormalParameters();
    }
    {
      const __qin_typed_receiver_994: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_994.RParen();
    }
    {
      const __qin_typed_receiver_995: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_995.LBrace();
    }
    {
      const __qin_typed_receiver_996: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_996.AsyncFunctionBody();
    }
    {
      const __qin_typed_receiver_997: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_997.RBrace();
    }
    return null;
  }
  AsyncGeneratorMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_AsyncGeneratorMethod receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_AsyncGeneratorMethod.call(this, params);
    }), "AsyncGeneratorMethod", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncGeneratorMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_998: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_998.consumeIdentifierValue("async");
    }
    {
      const __qin_typed_receiver_999: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_999.assertNoLineBreak();
    }
    {
      const __qin_typed_receiver_1000: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1000.Asterisk();
    }
    {
      const __qin_typed_receiver_1001: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1001.ClassElementName(params);
    }
    {
      const __qin_typed_receiver_1002: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1002.LParen();
    }
    {
      const __qin_typed_receiver_1003: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1003.UniqueFormalParameters();
    }
    {
      const __qin_typed_receiver_1004: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1004.RParen();
    }
    {
      const __qin_typed_receiver_1005: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1005.LBrace();
    }
    {
      const __qin_typed_receiver_1006: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1006.AsyncGeneratorBody();
    }
    {
      const __qin_typed_receiver_1007: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_1007.RBrace();
    }
    return null;
  }
  ClassElementName(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ClassElementName receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ClassElementName.call(this, params);
    }), "ClassElementName", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassElementName(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_1008: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1008.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassElementName", this.classStaticRuntime(null, params));
    }
    return null;
  }
  ClassContextualModifierList(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ClassContextualModifierList receiver=this arity=0 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ClassContextualModifierList.call(this);
    }), "ClassContextualModifierList", "SlimeClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ClassContextualModifierList(): void {
    {
      const __qin_typed_receiver_1009: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1009.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassContextualModifierList", this.classStaticRuntime());
    }
    return null;
  }
  ClassContextualModifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ClassContextualModifier receiver=this arity=0 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ClassContextualModifier.call(this);
    }), "ClassContextualModifier", "SlimeClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ClassContextualModifier(): void {
    {
      const __qin_typed_receiver_1010: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1010.consumeClassContextualModifier();
    }
    return null;
  }
  FieldDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_FieldDefinition receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_FieldDefinition.call(this, params);
    }), "FieldDefinition", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_FieldDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_1011: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1011.ClassElementName(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
    }
    {
      const __qin_typed_receiver_1012: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1012.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "OptionalFieldInitializer", this.classStaticRuntime());
    }
    return null;
  }
  ClassStaticBlock(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ClassStaticBlock receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ClassStaticBlock.call(this, params);
    }), "ClassStaticBlock", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassStaticBlock(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_1013: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1013.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassStaticBlock", this.classStaticRuntime(params));
    }
    return null;
  }
  ClassStaticBlockBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ClassStaticBlockBody receiver=this arity=0 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ClassStaticBlockBody.call(this);
    }), "ClassStaticBlockBody", "SlimeClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ClassStaticBlockBody(): void {
    {
      const __qin_typed_receiver_1014: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1014.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassStaticBlockBody", this.classStaticRuntime());
    }
    return null;
  }
  ClassStaticBlockStatementList(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ClassStaticBlockStatementList receiver=this arity=0 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ClassStaticBlockStatementList.call(this);
    }), "ClassStaticBlockStatementList", "SlimeClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ClassStaticBlockStatementList(): void {
    {
      const __qin_typed_receiver_1015: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1015.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassStaticBlockStatementList", this.classStaticRuntime());
    }
    return null;
  }
  UniqueFormalParameters(...__qin_args: any[]): void {
    if (__qin_args.length === 0 && true) return this.__qin_overload_UniqueFormalParameters_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_UniqueFormalParameters_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: UniqueFormalParameters/" + __qin_args.length);
  }
  __qin_overload_UniqueFormalParameters_0_0(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw___qin_overload_UniqueFormalParameters_0_0 receiver=this arity=0 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw___qin_overload_UniqueFormalParameters_0_0.call(this);
    }), "UniqueFormalParameters", "SlimeClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_UniqueFormalParameters_0_0(): void {
    {
      const __qin_typed_receiver_1016: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1016.FormalParameters();
    }
    return null;
  }
  __qin_overload_UniqueFormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw___qin_overload_UniqueFormalParameters_1_1 receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw___qin_overload_UniqueFormalParameters_1_1.call(this, params);
    }), "UniqueFormalParameters", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_UniqueFormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_1017: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1017.FormalParameters(params);
    }
    return null;
  }
  PropertySetParameterList(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_PropertySetParameterList receiver=this arity=0 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_PropertySetParameterList.call(this);
    }), "PropertySetParameterList", "SlimeClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_PropertySetParameterList(): void {
    {
      const __qin_typed_receiver_1018: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1018.FormalParameter();
    }
    return null;
  }
  Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_Declaration receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_Declaration.call(this, params);
    }), "Declaration", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_1019: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1019.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "Declaration", this.classStaticRuntime(params, new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await())));
    }
    return null;
  }
  HoistableDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_HoistableDeclaration receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_HoistableDeclaration.call(this, params);
    }), "HoistableDeclaration", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_HoistableDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): void {
    {
      const __qin_typed_receiver_1020: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1020.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "HoistableDeclaration", this.classStaticRuntime(params));
    }
    return null;
  }
  LexicalDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_LexicalDeclaration receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_LexicalDeclaration.call(this, params);
    }), "LexicalDeclaration", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LexicalDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_1021: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1021.LetOrConst();
    }
    {
      const __qin_typed_receiver_1022: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1022.BindingList(params);
    }
    {
      const __qin_typed_receiver_1023: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1023.SemicolonASI();
    }
    return null;
  }
  LetOrConst(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_LetOrConst receiver=this arity=0 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_LetOrConst.call(this);
    }), "LetOrConst", "SlimeClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_LetOrConst(): void {
    {
      const __qin_typed_receiver_1024: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1024.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "LetOrConst", this.classStaticRuntime());
    }
    return null;
  }
  classStaticRuntime(...__qin_args: any[]): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    if (__qin_args.length === 0 && true) return this.__qin_overload_classStaticRuntime_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_classStaticRuntime_1_1(__qin_args[0]);
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams) || __qin_structural_object__(__qin_args[0])) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[1]))) return this.__qin_overload_classStaticRuntime_2_2(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: classStaticRuntime/" + __qin_args.length);
  }
  __qin_overload_classStaticRuntime_0_0(): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return this.classStaticRuntime(null, null);
  }
  __qin_overload_classStaticRuntime_1_1(declarationParams: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return this.classStaticRuntime(declarationParams, null);
  }
  __qin_overload_classStaticRuntime_2_2(declarationParams: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, expressionParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_class__SlimeClassParser$ClassStaticRuntime(this, declarationParams, expressionParams);
  }
  canStartLetOrConst(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && (__QinJavaLangString.equals("Const", this.LA(lookaheadOffset).tokenName()) || (__QinJavaLangString.equals("IdentifierName", this.LA(lookaheadOffset).tokenName()) && __QinJavaLangString.equals("let", this.LA(lookaheadOffset).value()))));
  }
  canStartToken(tokenName: string, lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals(tokenName, this.LA(lookaheadOffset).tokenName()));
  }
  canStartClassBody(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && !__QinJavaLangString.equals("RBrace", this.LA(lookaheadOffset).tokenName()));
  }
  canStartClassStaticBlock(lookaheadOffset: number): boolean {
    return (this.isIdentifierValueAt(lookaheadOffset, "static") && __QinJavaLangString.equals("LBrace", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartDecoratedClassDeclaration(lookaheadOffset: number): boolean {
    if ((!this.canStartToken("At", lookaheadOffset))) {
      return false;
    }
    let nesting: number = 0.0;
    for (let offset: number = __qin_binary__("+", lookaheadOffset, 1.0); __qin_binary__("<", offset, __qin_binary__("+", lookaheadOffset, 64.0)); offset++) {
      let tokenName: string = this.tokenNameAt(offset);
      if ((__qin_binary__("==", tokenName, null) || __QinJavaLangString.equals("Semicolon", tokenName) || __QinJavaLangString.equals("RBrace", tokenName))) {
        return false;
      }
      if ((__qin_binary__("==", nesting, 0.0) && __QinJavaLangString.equals("Class", tokenName))) {
        return true;
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
  canStartClassMethodDefinition(lookaheadOffset: number): boolean {
    return (this.canStartGeneratorMethod(lookaheadOffset) || this.canStartAsyncGeneratorMethod(lookaheadOffset) || this.canStartAsyncMethod(lookaheadOffset) || this.canStartGetterMethod(lookaheadOffset) || this.canStartSetterMethod(lookaheadOffset) || this.canStartOrdinaryMethod(lookaheadOffset));
  }
  canStartClassElement(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Semicolon", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("At", this.tokenNameAt(lookaheadOffset)) || this.canStartClassElementMethod(lookaheadOffset) || this.canStartClassElementStaticMethod(lookaheadOffset) || this.canStartClassElementModifiedMethod(lookaheadOffset) || this.canStartClassElementModifiedStaticMethod(lookaheadOffset) || this.canStartClassElementField(lookaheadOffset) || this.canStartClassElementStaticField(lookaheadOffset) || this.canStartClassElementModifiedField(lookaheadOffset) || this.canStartClassElementModifiedStaticField(lookaheadOffset) || this.canStartClassElementStaticBlock(lookaheadOffset));
  }
  canStartClassElementMethod(lookaheadOffset: number): boolean {
    return this.canStartClassMethodDefinition(lookaheadOffset);
  }
  canStartClassElementStaticMethod(lookaheadOffset: number): boolean {
    return (!this.canStartClassElementMethod(lookaheadOffset) && this.isIdentifierValueAt(lookaheadOffset, "static") && this.canStartClassMethodDefinition(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartClassElementModifiedMethod(lookaheadOffset: number): boolean {
    return (__qin_binary__(">", this.offsetAfterClassContextualModifiers(lookaheadOffset), lookaheadOffset) && this.canStartClassMethodDefinition(this.offsetAfterClassContextualModifiers(lookaheadOffset)));
  }
  canStartClassElementModifiedStaticMethod(lookaheadOffset: number): boolean {
    return (__qin_binary__(">", this.offsetAfterClassContextualModifiers(lookaheadOffset), lookaheadOffset) && this.isIdentifierValueAt(this.offsetAfterClassContextualModifiers(lookaheadOffset), "static") && this.canStartClassMethodDefinition(__qin_binary__("+", this.offsetAfterClassContextualModifiers(lookaheadOffset), 1.0)));
  }
  canStartClassElementField(lookaheadOffset: number): boolean {
    return (!this.canStartClassElementMethod(lookaheadOffset) && !this.canStartClassElementStaticMethod(lookaheadOffset) && this.canStartClassFieldElement(lookaheadOffset));
  }
  canStartClassElementStaticField(lookaheadOffset: number): boolean {
    return (!this.canStartClassElementMethod(lookaheadOffset) && !this.canStartClassElementStaticMethod(lookaheadOffset) && !this.canStartClassElementField(lookaheadOffset) && this.isIdentifierValueAt(lookaheadOffset, "static") && this.canStartClassFieldElement(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartClassElementModifiedField(lookaheadOffset: number): boolean {
    return (__qin_binary__(">", this.offsetAfterClassContextualModifiers(lookaheadOffset), lookaheadOffset) && !this.canStartClassElementModifiedMethod(lookaheadOffset) && !this.canStartClassElementModifiedStaticMethod(lookaheadOffset) && this.canStartClassFieldElement(this.offsetAfterClassContextualModifiers(lookaheadOffset)));
  }
  canStartClassElementModifiedStaticField(lookaheadOffset: number): boolean {
    return (__qin_binary__(">", this.offsetAfterClassContextualModifiers(lookaheadOffset), lookaheadOffset) && !this.canStartClassElementModifiedMethod(lookaheadOffset) && !this.canStartClassElementModifiedStaticMethod(lookaheadOffset) && !this.canStartClassElementModifiedField(lookaheadOffset) && this.isIdentifierValueAt(this.offsetAfterClassContextualModifiers(lookaheadOffset), "static") && this.canStartClassFieldElement(__qin_binary__("+", this.offsetAfterClassContextualModifiers(lookaheadOffset), 1.0)));
  }
  canStartClassElementStaticBlock(lookaheadOffset: number): boolean {
    return (!this.canStartClassElementMethod(lookaheadOffset) && !this.canStartClassElementStaticMethod(lookaheadOffset) && !this.canStartClassElementField(lookaheadOffset) && !this.canStartClassElementStaticField(lookaheadOffset) && this.canStartClassStaticBlock(lookaheadOffset));
  }
  canStartClassFieldElement(lookaheadOffset: number): boolean {
    let nameEndOffset: number = this.classElementNameEndOffset(lookaheadOffset);
    if (__qin_binary__("<", nameEndOffset, 0.0)) {
      return false;
    }
    let nextTokenName: string = this.tokenNameAt(__qin_binary__("+", nameEndOffset, 1.0));
    if (__qin_binary__("==", nextTokenName, null)) {
      return true;
    }
    if ((__QinJavaLangString.equals("Assign", nextTokenName) || __QinJavaLangString.equals("Semicolon", nextTokenName) || __QinJavaLangString.equals("RBrace", nextTokenName) || __QinJavaLangString.equals("Colon", nextTokenName) || __QinJavaLangString.equals("Question", nextTokenName) || __QinJavaLangString.equals("LogicalNot", nextTokenName))) {
      return true;
    }
    let nextToken: com_subhuti_struct_SubhutiMatchToken = this.LA(__qin_binary__("+", nameEndOffset, 1.0));
    return (__qin_binary__("!=", nextToken, null) && nextToken.hasLineBreakBefore());
  }
  canStartGeneratorMethod(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Asterisk", this.tokenNameAt(lookaheadOffset)) && this.classElementNameIsFollowedByLParen(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartAsyncGeneratorMethod(lookaheadOffset: number): boolean {
    return (this.isIdentifierValueAt(lookaheadOffset, "async") && this.noLineBreakBefore(__qin_binary__("+", lookaheadOffset, 1.0)) && __QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && this.classElementNameIsFollowedByLParen(__qin_binary__("+", lookaheadOffset, 2.0)));
  }
  canStartAsyncMethod(lookaheadOffset: number): boolean {
    return (this.isIdentifierValueAt(lookaheadOffset, "async") && this.noLineBreakBefore(__qin_binary__("+", lookaheadOffset, 1.0)) && !__QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && this.classElementNameIsFollowedByLParen(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartGetterMethod(lookaheadOffset: number): boolean {
    return (this.canStartGetterMethodWithParens(lookaheadOffset) || this.canStartTSGetterMethod(lookaheadOffset));
  }
  canStartGetterMethodWithParens(lookaheadOffset: number): boolean {
    return (!this.canStartGeneratorMethod(lookaheadOffset) && !this.canStartAsyncGeneratorMethod(lookaheadOffset) && !this.canStartAsyncMethod(lookaheadOffset) && this.isIdentifierValueAt(lookaheadOffset, "get") && this.classElementNameIsFollowedByLParen(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartTSGetterMethod(lookaheadOffset: number): boolean {
    if ((this.canStartGeneratorMethod(lookaheadOffset) || this.canStartAsyncGeneratorMethod(lookaheadOffset) || this.canStartAsyncMethod(lookaheadOffset) || !this.isIdentifierValueAt(lookaheadOffset, "get"))) {
      return false;
    }
    let nameEndOffset: number = this.classElementNameEndOffset(__qin_binary__("+", lookaheadOffset, 1.0));
    if ((__qin_binary__("<", nameEndOffset, 0.0) || __QinJavaLangString.equals("LParen", this.tokenNameAt(__qin_binary__("+", nameEndOffset, 1.0))))) {
      return false;
    }
    let tail: string = this.tokenNameAt(__qin_binary__("+", nameEndOffset, 1.0));
    return (__QinJavaLangString.equals("Colon", tail) || __QinJavaLangString.equals("LBrace", tail));
  }
  canStartSetterMethod(lookaheadOffset: number): boolean {
    return (!this.canStartGeneratorMethod(lookaheadOffset) && !this.canStartAsyncGeneratorMethod(lookaheadOffset) && !this.canStartAsyncMethod(lookaheadOffset) && !this.canStartGetterMethod(lookaheadOffset) && this.isIdentifierValueAt(lookaheadOffset, "set") && this.classElementNameIsFollowedByLParen(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartOrdinaryMethod(lookaheadOffset: number): boolean {
    return (!this.canStartGeneratorMethod(lookaheadOffset) && !this.canStartAsyncGeneratorMethod(lookaheadOffset) && !this.canStartAsyncMethod(lookaheadOffset) && !this.canStartGetterMethod(lookaheadOffset) && !this.canStartSetterMethod(lookaheadOffset) && this.classElementNameIsFollowedByLParen(lookaheadOffset));
  }
  canStartClassElementNameAt(lookaheadOffset: number): boolean {
    return (this.canStartClassPropertyName(lookaheadOffset) || this.canStartToken("PrivateIdentifier", lookaheadOffset));
  }
  noLineBreakBefore(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && !this.LA(lookaheadOffset).hasLineBreakBefore());
  }
  classElementNameIsFollowedByLParen(lookaheadOffset: number): boolean {
    let nameEndOffset: number = this.classElementNameEndOffset(lookaheadOffset);
    if (__qin_binary__("<", nameEndOffset, 0.0)) {
      return false;
    }
    let nextOffset: number = __qin_binary__("+", nameEndOffset, 1.0);
    if (__QinJavaLangString.equals("Less", this.tokenNameAt(nextOffset))) {
      nextOffset = this.matchingBalancedOffset(nextOffset, "Less", "Greater");
      if (__qin_binary__("<", nextOffset, 0.0)) {
        return false;
      }
      nextOffset++;
    }
    return __QinJavaLangString.equals("LParen", this.tokenNameAt(nextOffset));
  }
  classElementNameEndOffset(lookaheadOffset: number): number {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if ((this.canStartIdentifierNameToken(lookaheadOffset) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || __QinJavaLangString.equals("PrivateIdentifier", tokenName))) {
      return lookaheadOffset;
    }
    if ((!__QinJavaLangString.equals("LBracket", tokenName))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    let depth: number = 1.0;
    let offset: number = __qin_binary__("+", lookaheadOffset, 1.0);
    while (__qin_binary__("!=", this.tokenNameAt(offset), null)) {
      let currentTokenName: string = this.tokenNameAt(offset);
      if (__QinJavaLangString.equals("LBracket", currentTokenName)) {
        depth++;
      } else {
        if (__QinJavaLangString.equals("RBracket", currentTokenName)) {
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
  canStartClassStaticBlockStatementList(lookaheadOffset: number): boolean {
    return (this.canStartStatementListItemAt(lookaheadOffset, this.classStaticBlockStatementParams()) || __QinJavaLangString.equals("RBrace", this.tokenNameAt(lookaheadOffset)) || __qin_binary__("==", this.tokenNameAt(lookaheadOffset), null));
  }
  classStaticBlockStatementParams(): com_slime_parser_base_SlimeJavascriptParserBase$StatementParams {
    return new com_slime_parser_base_SlimeJavascriptParserBase$StatementParams(false, true, false);
  }
  canStartClassPropertyName(lookaheadOffset: number): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    let tokenName: string = token.tokenName();
    return (this.canStartIdentifierNameToken(lookaheadOffset) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || __QinJavaLangString.equals("LBracket", tokenName));
  }
  canStartClassBindingIdentifier(lookaheadOffset: number): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    let tokenName: string = token.tokenName();
    return (__QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("Yield", tokenName) || __QinJavaLangString.equals("Await", tokenName));
  }
  canStartClassBindingPattern(lookaheadOffset: number): boolean {
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    let tokenName: string = token.tokenName();
    return (__QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("LBracket", tokenName));
  }
  canStartHoistableDeclaration(lookaheadOffset: number): boolean {
    return (this.canStartFunctionDeclaration(lookaheadOffset) || this.canStartGeneratorDeclaration(lookaheadOffset) || this.canStartAsyncFunctionDeclaration(lookaheadOffset) || this.canStartAsyncGeneratorDeclaration(lookaheadOffset));
  }
  isDefaultOrSourceFunctionDeclarationVariant(variantKey: any): boolean {
    if (__qin_binary__("==", variantKey, null)) {
      return true;
    }
    return __QinJavaUtilObjects.equals(variantKey, com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_SOURCE_FUNCTION_DECLARATION_VARIANT);
  }
  canStartFunctionDeclaration(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Function", this.tokenNameAt(lookaheadOffset)) && !__QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartGeneratorDeclaration(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Function", this.tokenNameAt(lookaheadOffset)) && __QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartAsyncFunctionDeclaration(lookaheadOffset: number): boolean {
    return (this.isIdentifierValueAt(lookaheadOffset, "async") && this.noLineBreakBefore(__qin_binary__("+", lookaheadOffset, 1.0)) && __QinJavaLangString.equals("Function", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && !__QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 2.0))));
  }
  canStartAsyncGeneratorDeclaration(lookaheadOffset: number): boolean {
    return (this.isIdentifierValueAt(lookaheadOffset, "async") && this.noLineBreakBefore(__qin_binary__("+", lookaheadOffset, 1.0)) && __QinJavaLangString.equals("Function", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && __QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 2.0))));
  }
  isIdentifierValueAt(lookaheadOffset: number, value: string): boolean {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("IdentifierName", this.LA(lookaheadOffset).tokenName()) && __QinJavaLangString.equals(value, this.LA(lookaheadOffset).value()));
  }
  offsetAfterClassContextualModifiers(lookaheadOffset: number): number {
    let offset: number = lookaheadOffset;
    while (this.canStartClassContextualModifier(offset)) {
      offset++;
    }
    return offset;
  }
  canStartClassContextualModifier(lookaheadOffset: number): boolean {
    if (__QinJavaLangString.equals("LParen", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0)))) {
      return false;
    }
    return (this.isIdentifierValueAt(lookaheadOffset, "public") || this.isIdentifierValueAt(lookaheadOffset, "private") || this.isIdentifierValueAt(lookaheadOffset, "protected") || this.isIdentifierValueAt(lookaheadOffset, "readonly") || this.isIdentifierValueAt(lookaheadOffset, "override") || this.isIdentifierValueAt(lookaheadOffset, "declare") || this.isIdentifierValueAt(lookaheadOffset, "accessor"));
  }
  consumeClassContextualModifier(): void {
    if (this.isIdentifierValueAt(1.0, "public")) {
      {
        const __qin_typed_receiver_1025: com_slime_parser_class__SlimeClassParser = this;
        __qin_typed_receiver_1025.consumeIdentifierValue("public");
      }
      return null;
    }
    if (this.isIdentifierValueAt(1.0, "private")) {
      {
        const __qin_typed_receiver_1026: com_slime_parser_class__SlimeClassParser = this;
        __qin_typed_receiver_1026.consumeIdentifierValue("private");
      }
      return null;
    }
    if (this.isIdentifierValueAt(1.0, "protected")) {
      {
        const __qin_typed_receiver_1027: com_slime_parser_class__SlimeClassParser = this;
        __qin_typed_receiver_1027.consumeIdentifierValue("protected");
      }
      return null;
    }
    if (this.isIdentifierValueAt(1.0, "readonly")) {
      {
        const __qin_typed_receiver_1028: com_slime_parser_class__SlimeClassParser = this;
        __qin_typed_receiver_1028.consumeIdentifierValue("readonly");
      }
      return null;
    }
    if (this.isIdentifierValueAt(1.0, "override")) {
      {
        const __qin_typed_receiver_1029: com_slime_parser_class__SlimeClassParser = this;
        __qin_typed_receiver_1029.consumeIdentifierValue("override");
      }
      return null;
    }
    if (this.isIdentifierValueAt(1.0, "declare")) {
      {
        const __qin_typed_receiver_1030: com_slime_parser_class__SlimeClassParser = this;
        __qin_typed_receiver_1030.consumeIdentifierValue("declare");
      }
      return null;
    }
    if (this.isIdentifierValueAt(1.0, "accessor")) {
      {
        const __qin_typed_receiver_1031: com_slime_parser_class__SlimeClassParser = this;
        __qin_typed_receiver_1031.consumeIdentifierValue("accessor");
      }
      return null;
    }
    {
      const __qin_typed_receiver_1032: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1032.setParseFail();
    }
    return null;
  }
  ForDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_ForDeclaration receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_ForDeclaration.call(this, params);
    }), "ForDeclaration", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ForDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_1033: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1033.LetOrConst();
    }
    {
      const __qin_typed_receiver_1034: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1034.ForBinding(params);
    }
    return null;
  }
  BindingList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_BindingList receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_BindingList.call(this, params);
    }), "BindingList", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_1035: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1035.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "BindingList", this.classStaticRuntime(null, params));
    }
    return null;
  }
  LexicalBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.class_.SlimeClassParser method=__qin_subhuti_raw_LexicalBinding receiver=this arity=1 */ com_slime_parser_class__SlimeClassParser.prototype.__qin_subhuti_raw_LexicalBinding.call(this, params);
    }), "LexicalBinding", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LexicalBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_1036: com_slime_parser_class__SlimeClassParser = this;
      __qin_typed_receiver_1036.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "LexicalBinding", this.classStaticRuntime(null, params));
    }
    return null;
  }
}
const SlimeClassParser = com_slime_parser_class__SlimeClassParser;
class com_slime_parser_class__SlimeClassParser$ClassStaticRuntime {
  __qin_field_parser: com_slime_parser_class__SlimeClassParser | null = null as any;
  __qin_field_declarationParams: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams | null = null as any;
  __qin_field_expressionParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 3 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_class__SlimeClassParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams) || __qin_structural_object__(__qin_args[1])) && (__qin_args[2] === null || __qin_instanceof__(__qin_args[2], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[2]))) {
      const parser: any = __qin_args[0];
      const declarationParams: any = __qin_args[1];
      const expressionParams: any = __qin_args[2];
      this.__qin_constructor_com_slime_parser_class__SlimeClassParser$ClassStaticRuntime_3_0(parser, declarationParams, expressionParams);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeClassParser$ClassStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_class__SlimeClassParser$ClassStaticRuntime_3_0(parser: com_slime_parser_class__SlimeClassParser, declarationParams: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, expressionParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_declarationParams = null;
    this.__qin_field_expressionParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_declarationParams = declarationParams;
    this.__qin_field_expressionParams = expressionParams;
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
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("LetOrConst", ruleName))) {
      return this.__qin_field_parser.canStartLetOrConst(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("Initializer", ruleName))) {
      return this.__qin_field_parser.canStartToken("Assign", lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassHeritage", ruleName))) {
      return this.__qin_field_parser.canStartToken("Extends", lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassBody", ruleName))) {
      return this.__qin_field_parser.canStartClassBody(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassElementList", ruleName))) {
      return this.__qin_field_parser.canStartClassBody(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassElement", ruleName))) {
      return this.__qin_field_parser.canStartClassElement(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassContextualModifierList", ruleName))) {
      return this.__qin_field_parser.canStartClassContextualModifier(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassContextualModifier", ruleName))) {
      return this.__qin_field_parser.canStartClassContextualModifier(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("MethodDefinition", ruleName))) {
      return this.__qin_field_parser.canStartClassMethodDefinition(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("FieldDefinition", ruleName))) {
      return this.__qin_field_parser.canStartClassFieldElement(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("SemicolonASI", ruleName))) {
      return true;
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("GeneratorMethod", ruleName))) {
      return this.__qin_field_parser.canStartGeneratorMethod(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("AsyncGeneratorMethod", ruleName))) {
      return this.__qin_field_parser.canStartAsyncGeneratorMethod(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("AsyncMethod", ruleName))) {
      return this.__qin_field_parser.canStartAsyncMethod(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassStaticBlock", ruleName))) {
      return this.__qin_field_parser.canStartClassStaticBlock(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassStaticBlockBody", ruleName))) {
      return this.__qin_field_parser.canStartClassStaticBlockStatementList(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassStaticBlockStatementList", ruleName))) {
      return this.__qin_field_parser.canStartClassStaticBlockStatementList(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("StatementList", ruleName))) {
      return this.__qin_field_parser.canStartStatementListItemAt(lookaheadOffset, this.__qin_field_parser.classStaticBlockStatementParams());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("PropertyName", ruleName))) {
      return this.__qin_field_parser.canStartClassPropertyName(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("PrivateIdentifier", ruleName))) {
      return this.__qin_field_parser.canStartToken("PrivateIdentifier", lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("BindingIdentifier", ruleName))) {
      return this.__qin_field_parser.canStartClassBindingIdentifier(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("BindingPattern", ruleName))) {
      return this.__qin_field_parser.canStartClassBindingPattern(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("LexicalBinding", ruleName))) {
      return (this.__qin_field_parser.canStartClassBindingIdentifier(lookaheadOffset) || this.__qin_field_parser.canStartClassBindingPattern(lookaheadOffset));
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("HoistableDeclaration", ruleName))) {
      return this.__qin_field_parser.canStartHoistableDeclaration(lookaheadOffset);
    }
    if ((this.__qin_field_parser.isDefaultOrSourceFunctionDeclarationVariant(variantKey) && __QinJavaLangString.equals("FunctionDeclaration", ruleName))) {
      return this.__qin_field_parser.canStartFunctionDeclaration(lookaheadOffset);
    }
    if ((this.__qin_field_parser.isDefaultOrSourceFunctionDeclarationVariant(variantKey) && __QinJavaLangString.equals("GeneratorDeclaration", ruleName))) {
      return this.__qin_field_parser.canStartGeneratorDeclaration(lookaheadOffset);
    }
    if ((this.__qin_field_parser.isDefaultOrSourceFunctionDeclarationVariant(variantKey) && __QinJavaLangString.equals("AsyncFunctionDeclaration", ruleName))) {
      return this.__qin_field_parser.canStartAsyncFunctionDeclaration(lookaheadOffset);
    }
    if ((this.__qin_field_parser.isDefaultOrSourceFunctionDeclarationVariant(variantKey) && __QinJavaLangString.equals("AsyncGeneratorDeclaration", ruleName))) {
      return this.__qin_field_parser.canStartAsyncGeneratorDeclaration(lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassDeclaration", ruleName))) {
      return (this.__qin_field_parser.canStartToken("Class", lookaheadOffset) || this.__qin_field_parser.canStartDecoratedClassDeclaration(lookaheadOffset));
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("LexicalDeclaration", ruleName))) {
      return this.__qin_field_parser.canStartLetOrConst(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported class static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_REQUIRED_CLASS_BINDING_IDENTIFIER, gateId)) {
      return this.__qin_field_parser.canStartClassBindingIdentifier(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_METHOD_GENERATOR_START, gateId)) {
      return this.__qin_field_parser.canStartGeneratorMethod(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_METHOD_ASYNC_GENERATOR_START, gateId)) {
      return this.__qin_field_parser.canStartAsyncGeneratorMethod(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_METHOD_ASYNC_START, gateId)) {
      return this.__qin_field_parser.canStartAsyncMethod(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_METHOD_GETTER_START, gateId)) {
      return this.__qin_field_parser.canStartGetterMethod(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_METHOD_SETTER_START, gateId)) {
      return this.__qin_field_parser.canStartSetterMethod(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_METHOD_ORDINARY_START, gateId)) {
      return this.__qin_field_parser.canStartOrdinaryMethod(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_CLASS_ELEMENT_METHOD_START, gateId)) {
      return this.__qin_field_parser.canStartClassElementMethod(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_CLASS_ELEMENT_STATIC_METHOD_START, gateId)) {
      return this.__qin_field_parser.canStartClassElementStaticMethod(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_CLASS_ELEMENT_FIELD_START, gateId)) {
      return this.__qin_field_parser.canStartClassElementField(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_CLASS_ELEMENT_STATIC_FIELD_START, gateId)) {
      return this.__qin_field_parser.canStartClassElementStaticField(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_CLASS_ELEMENT_STATIC_BLOCK_START, gateId)) {
      return this.__qin_field_parser.canStartClassElementStaticBlock(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_CLASS_ELEMENT_MODIFIED_METHOD_START, gateId)) {
      return this.__qin_field_parser.canStartClassElementModifiedMethod(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_CLASS_ELEMENT_MODIFIED_STATIC_METHOD_START, gateId)) {
      return this.__qin_field_parser.canStartClassElementModifiedStaticMethod(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_CLASS_ELEMENT_MODIFIED_FIELD_START, gateId)) {
      return this.__qin_field_parser.canStartClassElementModifiedField(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_CLASS_ELEMENT_MODIFIED_STATIC_FIELD_START, gateId)) {
      return this.__qin_field_parser.canStartClassElementModifiedStaticField(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_CLASS_CONTEXTUAL_MODIFIER_START, gateId)) {
      return this.__qin_field_parser.canStartClassContextualModifier(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_HOISTABLE_FUNCTION_START, gateId)) {
      return this.__qin_field_parser.canStartFunctionDeclaration(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_HOISTABLE_GENERATOR_START, gateId)) {
      return this.__qin_field_parser.canStartGeneratorDeclaration(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_HOISTABLE_ASYNC_FUNCTION_START, gateId)) {
      return this.__qin_field_parser.canStartAsyncFunctionDeclaration(1.0);
    }
    if (__QinJavaLangString.equals(com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_GATE_HOISTABLE_ASYNC_GENERATOR_START, gateId)) {
      return this.__qin_field_parser.canStartAsyncGeneratorDeclaration(1.0);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported class static gate: " + gateId));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("LetOrConst", ruleName))) {
      {
        const __qin_typed_receiver_1037: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1037.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "LetOrConst", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("Initializer", ruleName))) {
      let initializerParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams = (__qin_binary__("==", this.__qin_field_expressionParams, null) ? new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, false, false) : this.__qin_field_expressionParams);
      {
        const __qin_typed_receiver_1038: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1038.Initializer(initializerParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassHeritage", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("ClassHeritage static call requires DeclarationParams");
      }
      {
        const __qin_typed_receiver_1039: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1039.ClassHeritage(this.__qin_field_declarationParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassBody", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("ClassBody static call requires DeclarationParams");
      }
      {
        const __qin_typed_receiver_1040: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1040.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassBody", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassElementList", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("ClassElementList static call requires DeclarationParams");
      }
      {
        const __qin_typed_receiver_1041: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1041.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassElementList", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassElement", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("ClassElement static call requires DeclarationParams");
      }
      {
        const __qin_typed_receiver_1042: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1042.ClassElement(this.__qin_field_declarationParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassContextualModifierList", ruleName))) {
      {
        const __qin_typed_receiver_1043: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1043.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassContextualModifierList", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassContextualModifier", ruleName))) {
      {
        const __qin_typed_receiver_1044: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1044.ClassContextualModifier();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("MethodDefinition", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("MethodDefinition static call requires ExpressionParams");
      }
      {
        const __qin_typed_receiver_1045: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1045.MethodDefinition(this.__qin_field_expressionParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("FieldDefinition", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("FieldDefinition static call requires DeclarationParams");
      }
      {
        const __qin_typed_receiver_1046: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1046.FieldDefinition(this.__qin_field_declarationParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("SemicolonASI", ruleName))) {
      {
        const __qin_typed_receiver_1047: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1047.SemicolonASI();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("GeneratorMethod", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("GeneratorMethod static call requires ExpressionParams");
      }
      {
        const __qin_typed_receiver_1048: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1048.GeneratorMethod(this.__qin_field_expressionParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("AsyncGeneratorMethod", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("AsyncGeneratorMethod static call requires ExpressionParams");
      }
      {
        const __qin_typed_receiver_1049: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1049.AsyncGeneratorMethod(this.__qin_field_expressionParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("AsyncMethod", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("AsyncMethod static call requires ExpressionParams");
      }
      {
        const __qin_typed_receiver_1050: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1050.AsyncMethod(this.__qin_field_expressionParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("FunctionBody", ruleName))) {
      {
        const __qin_typed_receiver_1051: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1051.FunctionBody();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("PropertySetParameterList", ruleName))) {
      {
        const __qin_typed_receiver_1052: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1052.PropertySetParameterList();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("UniqueFormalParameters", ruleName))) {
      {
        const __qin_typed_receiver_1053: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1053.UniqueFormalParameters();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassStaticBlock", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("ClassStaticBlock static call requires DeclarationParams");
      }
      {
        const __qin_typed_receiver_1054: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1054.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassStaticBlock", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassStaticBlockBody", ruleName))) {
      {
        const __qin_typed_receiver_1055: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1055.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassStaticBlockBody", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassStaticBlockStatementList", ruleName))) {
      {
        const __qin_typed_receiver_1056: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1056.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassStaticBlockStatementList", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("StatementList", ruleName))) {
      {
        const __qin_typed_receiver_1057: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1057.StatementList(this.__qin_field_parser.classStaticBlockStatementParams());
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("PropertyName", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("PropertyName static call requires ExpressionParams");
      }
      {
        const __qin_typed_receiver_1058: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1058.PropertyName(this.__qin_field_expressionParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("PrivateIdentifier", ruleName))) {
      {
        const __qin_typed_receiver_1059: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1059.PrivateIdentifier();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("BindingIdentifier", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("BindingIdentifier static call requires ExpressionParams");
      }
      {
        const __qin_typed_receiver_1060: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1060.BindingIdentifier(this.__qin_field_expressionParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("BindingPattern", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("BindingPattern static call requires ExpressionParams");
      }
      {
        const __qin_typed_receiver_1061: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1061.BindingPattern(this.__qin_field_expressionParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("LexicalBinding", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("LexicalBinding static call requires ExpressionParams");
      }
      {
        const __qin_typed_receiver_1062: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1062.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "LexicalBinding", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("HoistableDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("HoistableDeclaration static call requires DeclarationParams");
      }
      {
        const __qin_typed_receiver_1063: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1063.HoistableDeclaration(this.__qin_field_declarationParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((this.__qin_field_parser.isDefaultOrSourceFunctionDeclarationVariant(variantKey) && __QinJavaLangString.equals("FunctionDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("FunctionDeclaration static call requires DeclarationParams");
      }
      {
        const __qin_typed_receiver_1064: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1064.FunctionDeclaration(this.__qin_field_declarationParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((this.__qin_field_parser.isDefaultOrSourceFunctionDeclarationVariant(variantKey) && __QinJavaLangString.equals("GeneratorDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("GeneratorDeclaration static call requires DeclarationParams");
      }
      {
        const __qin_typed_receiver_1065: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1065.GeneratorDeclaration(this.__qin_field_declarationParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((this.__qin_field_parser.isDefaultOrSourceFunctionDeclarationVariant(variantKey) && __QinJavaLangString.equals("AsyncFunctionDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("AsyncFunctionDeclaration static call requires DeclarationParams");
      }
      {
        const __qin_typed_receiver_1066: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1066.AsyncFunctionDeclaration(this.__qin_field_declarationParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((this.__qin_field_parser.isDefaultOrSourceFunctionDeclarationVariant(variantKey) && __QinJavaLangString.equals("AsyncGeneratorDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("AsyncGeneratorDeclaration static call requires DeclarationParams");
      }
      {
        const __qin_typed_receiver_1067: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1067.AsyncGeneratorDeclaration(this.__qin_field_declarationParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("ClassDeclaration static call requires DeclarationParams");
      }
      {
        const __qin_typed_receiver_1068: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1068.ClassDeclaration(this.__qin_field_declarationParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("LexicalDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("LexicalDeclaration static call requires ExpressionParams");
      }
      {
        const __qin_typed_receiver_1069: com_slime_parser_class__SlimeClassParser = this.__qin_field_parser;
        __qin_typed_receiver_1069.LexicalDeclaration(this.__qin_field_expressionParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported class static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_class__SlimeClassParser$ClassStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeClassParser$ClassStaticRuntime = com_slime_parser_class__SlimeClassParser$ClassStaticRuntime;
com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR = com_slime_parser_class__SlimeClassStaticGrammar.grammar();

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_class__SlimeClassParser, com_slime_parser_class__SlimeClassParser$ClassStaticRuntime };
