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
  ClassDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassDeclaration(params);
    }), "ClassDeclaration", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.__qin_field_tokenConsumer.Class();
    if (params.isDefault()) {
      this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "OptionalClassBindingIdentifier", this.classStaticRuntime(null, new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await())));
    } else {
      this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "RequiredClassBindingIdentifier", this.classStaticRuntime(null, new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await())));
    }
    if (this.isParserFail()) {
      return null;
    }
    this.ClassTail(params);
    return null;
  }
  ClassExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassExpression(params);
    }), "ClassExpression", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.__qin_field_tokenConsumer.Class();
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "OptionalClassBindingIdentifier", this.classStaticRuntime(null, params));
    this.ClassTail(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(params.__qin_yield(), params.__qin_await(), false));
    return null;
  }
  ClassTail(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassTail(params);
    }), "ClassTail", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassTail(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassTail", this.classStaticRuntime(params));
    return null;
  }
  ClassHeritage(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassHeritage(params);
    }), "ClassHeritage", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassHeritage(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.__qin_field_tokenConsumer.Extends();
    this.LeftHandSideExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    return null;
  }
  ClassBody(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassBody(params);
    }), "ClassBody", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassBody(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassBody", this.classStaticRuntime(params));
    return null;
  }
  ClassElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassElementList(params);
    }), "ClassElementList", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassElementList(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassElementList", this.classStaticRuntime(params));
    return null;
  }
  ClassElement(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassElement(params);
    }), "ClassElement", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassElement(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassElement", this.classStaticRuntime(params, new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await())));
    return null;
  }
  MethodDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_MethodDefinition(params);
    }), "MethodDefinition", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_MethodDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "MethodDefinition", this.classStaticRuntime(null, params));
    return null;
  }
  GeneratorMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_GeneratorMethod(params);
    }), "GeneratorMethod", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_GeneratorMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.__qin_field_tokenConsumer.Asterisk();
    this.ClassElementName(params);
    this.__qin_field_tokenConsumer.LParen();
    this.UniqueFormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.__qin_field_tokenConsumer.LBrace();
    this.GeneratorBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  AsyncMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncMethod(params);
    }), "AsyncMethod", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.consumeIdentifierValue("async");
    this.assertNoLineBreak();
    this.ClassElementName(params);
    this.__qin_field_tokenConsumer.LParen();
    this.UniqueFormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.__qin_field_tokenConsumer.LBrace();
    this.AsyncFunctionBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  AsyncGeneratorMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncGeneratorMethod(params);
    }), "AsyncGeneratorMethod", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncGeneratorMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.consumeIdentifierValue("async");
    this.assertNoLineBreak();
    this.__qin_field_tokenConsumer.Asterisk();
    this.ClassElementName(params);
    this.__qin_field_tokenConsumer.LParen();
    this.UniqueFormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.__qin_field_tokenConsumer.LBrace();
    this.AsyncGeneratorBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  ClassElementName(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassElementName(params);
    }), "ClassElementName", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassElementName(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassElementName", this.classStaticRuntime(null, params));
    return null;
  }
  FieldDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FieldDefinition(params);
    }), "FieldDefinition", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_FieldDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.ClassElementName(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "OptionalFieldInitializer", this.classStaticRuntime());
    return null;
  }
  ClassStaticBlock(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassStaticBlock(params);
    }), "ClassStaticBlock", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassStaticBlock(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassStaticBlock", this.classStaticRuntime(params));
    return null;
  }
  ClassStaticBlockBody(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassStaticBlockBody();
    }), "ClassStaticBlockBody", "SlimeClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ClassStaticBlockBody(): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassStaticBlockBody", this.classStaticRuntime());
    return null;
  }
  ClassStaticBlockStatementList(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassStaticBlockStatementList();
    }), "ClassStaticBlockStatementList", "SlimeClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ClassStaticBlockStatementList(): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassStaticBlockStatementList", this.classStaticRuntime());
    return null;
  }
  UniqueFormalParameters(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_UniqueFormalParameters_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_UniqueFormalParameters_1_1(__qin_args[0]);
    throw new Error("Unsupported Java overload: UniqueFormalParameters/" + __qin_args.length);
  }
  __qin_overload_UniqueFormalParameters_0_0(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_UniqueFormalParameters_0_0();
    }), "UniqueFormalParameters", "SlimeClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_UniqueFormalParameters_0_0(): any {
    this.FormalParameters();
    return null;
  }
  __qin_overload_UniqueFormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_UniqueFormalParameters_1_1(params);
    }), "UniqueFormalParameters", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_UniqueFormalParameters_1_1(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.FormalParameters(params);
    return null;
  }
  PropertySetParameterList(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_PropertySetParameterList();
    }), "PropertySetParameterList", "SlimeClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_PropertySetParameterList(): any {
    this.FormalParameter();
    return null;
  }
  Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Declaration(params);
    }), "Declaration", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "Declaration", this.classStaticRuntime(params, new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await())));
    return null;
  }
  HoistableDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_HoistableDeclaration(params);
    }), "HoistableDeclaration", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_HoistableDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "HoistableDeclaration", this.classStaticRuntime(params));
    return null;
  }
  LexicalDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_LexicalDeclaration(params);
    }), "LexicalDeclaration", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LexicalDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.LetOrConst();
    this.BindingList(params);
    this.SemicolonASI();
    return null;
  }
  LetOrConst(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_LetOrConst();
    }), "LetOrConst", "SlimeClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_LetOrConst(): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "LetOrConst", this.classStaticRuntime());
    return null;
  }
  classStaticRuntime(...__qin_args: any[]): any {
    if (__qin_args.length === 0 && true) return this.__qin_overload_classStaticRuntime_0_0();
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_classStaticRuntime_1_1(__qin_args[0]);
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams) || __qin_structural_object__(__qin_args[0])) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[1]))) return this.__qin_overload_classStaticRuntime_2_2(__qin_args[0], __qin_args[1]);
    throw new Error("Unsupported Java overload: classStaticRuntime/" + __qin_args.length);
  }
  __qin_overload_classStaticRuntime_0_0(): any {
    return this.classStaticRuntime(null, null);
  }
  __qin_overload_classStaticRuntime_1_1(declarationParams: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.classStaticRuntime(declarationParams, null);
  }
  __qin_overload_classStaticRuntime_2_2(declarationParams: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, expressionParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return new com_slime_parser_class__SlimeClassParser$ClassStaticRuntime(this, declarationParams, expressionParams);
  }
  canStartLetOrConst(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && (__QinJavaLangString.equals("Const", this.LA(lookaheadOffset).tokenName()) || (__QinJavaLangString.equals("IdentifierName", this.LA(lookaheadOffset).tokenName()) && __QinJavaLangString.equals("let", this.LA(lookaheadOffset).value()))));
  }
  canStartToken(tokenName: string, lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals(tokenName, this.LA(lookaheadOffset).tokenName()));
  }
  canStartClassBody(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && !__QinJavaLangString.equals("RBrace", this.LA(lookaheadOffset).tokenName()));
  }
  canStartClassStaticBlock(lookaheadOffset: number): any {
    return (this.isIdentifierValueAt(lookaheadOffset, "static") && __QinJavaLangString.equals("LBrace", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartClassMethodDefinition(lookaheadOffset: number): any {
    return (this.canStartGeneratorMethod(lookaheadOffset) || this.canStartAsyncGeneratorMethod(lookaheadOffset) || this.canStartAsyncMethod(lookaheadOffset) || this.canStartGetterMethod(lookaheadOffset) || this.canStartSetterMethod(lookaheadOffset) || this.canStartOrdinaryMethod(lookaheadOffset));
  }
  canStartClassElement(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("Semicolon", this.tokenNameAt(lookaheadOffset)) || this.canStartClassElementMethod(lookaheadOffset) || this.canStartClassElementStaticMethod(lookaheadOffset) || this.canStartClassElementField(lookaheadOffset) || this.canStartClassElementStaticField(lookaheadOffset) || this.canStartClassElementStaticBlock(lookaheadOffset));
  }
  canStartClassElementMethod(lookaheadOffset: number): any {
    return this.canStartClassMethodDefinition(lookaheadOffset);
  }
  canStartClassElementStaticMethod(lookaheadOffset: number): any {
    return (!this.canStartClassElementMethod(lookaheadOffset) && this.isIdentifierValueAt(lookaheadOffset, "static") && this.canStartClassMethodDefinition(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartClassElementField(lookaheadOffset: number): any {
    return (!this.canStartClassElementMethod(lookaheadOffset) && !this.canStartClassElementStaticMethod(lookaheadOffset) && this.canStartClassFieldElement(lookaheadOffset));
  }
  canStartClassElementStaticField(lookaheadOffset: number): any {
    return (!this.canStartClassElementMethod(lookaheadOffset) && !this.canStartClassElementStaticMethod(lookaheadOffset) && !this.canStartClassElementField(lookaheadOffset) && this.isIdentifierValueAt(lookaheadOffset, "static") && this.canStartClassFieldElement(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartClassElementStaticBlock(lookaheadOffset: number): any {
    return (!this.canStartClassElementMethod(lookaheadOffset) && !this.canStartClassElementStaticMethod(lookaheadOffset) && !this.canStartClassElementField(lookaheadOffset) && !this.canStartClassElementStaticField(lookaheadOffset) && this.canStartClassStaticBlock(lookaheadOffset));
  }
  canStartClassFieldElement(lookaheadOffset: number): any {
    let nameEndOffset: any = this.classElementNameEndOffset(lookaheadOffset);
    if (__qin_binary__("<", nameEndOffset, 0.0)) {
      return false;
    }
    let nextTokenName: any = this.tokenNameAt(__qin_binary__("+", nameEndOffset, 1.0));
    if (__qin_binary__("==", nextTokenName, null)) {
      return true;
    }
    if ((__QinJavaLangString.equals("Assign", nextTokenName) || __QinJavaLangString.equals("Semicolon", nextTokenName) || __QinJavaLangString.equals("RBrace", nextTokenName) || __QinJavaLangString.equals("Colon", nextTokenName) || __QinJavaLangString.equals("Question", nextTokenName) || __QinJavaLangString.equals("LogicalNot", nextTokenName))) {
      return true;
    }
    let nextToken: any = this.LA(__qin_binary__("+", nameEndOffset, 1.0));
    return (__qin_binary__("!=", nextToken, null) && nextToken.hasLineBreakBefore());
  }
  canStartGeneratorMethod(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("Asterisk", this.tokenNameAt(lookaheadOffset)) && this.classElementNameIsFollowedByLParen(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartAsyncGeneratorMethod(lookaheadOffset: number): any {
    return (this.isIdentifierValueAt(lookaheadOffset, "async") && this.noLineBreakBefore(__qin_binary__("+", lookaheadOffset, 1.0)) && __QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && this.classElementNameIsFollowedByLParen(__qin_binary__("+", lookaheadOffset, 2.0)));
  }
  canStartAsyncMethod(lookaheadOffset: number): any {
    return (this.isIdentifierValueAt(lookaheadOffset, "async") && this.noLineBreakBefore(__qin_binary__("+", lookaheadOffset, 1.0)) && !__QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && this.classElementNameIsFollowedByLParen(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartGetterMethod(lookaheadOffset: number): any {
    return (!this.canStartGeneratorMethod(lookaheadOffset) && !this.canStartAsyncGeneratorMethod(lookaheadOffset) && !this.canStartAsyncMethod(lookaheadOffset) && this.isIdentifierValueAt(lookaheadOffset, "get") && this.classElementNameIsFollowedByLParen(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartSetterMethod(lookaheadOffset: number): any {
    return (!this.canStartGeneratorMethod(lookaheadOffset) && !this.canStartAsyncGeneratorMethod(lookaheadOffset) && !this.canStartAsyncMethod(lookaheadOffset) && !this.canStartGetterMethod(lookaheadOffset) && this.isIdentifierValueAt(lookaheadOffset, "set") && this.classElementNameIsFollowedByLParen(__qin_binary__("+", lookaheadOffset, 1.0)));
  }
  canStartOrdinaryMethod(lookaheadOffset: number): any {
    return (!this.canStartGeneratorMethod(lookaheadOffset) && !this.canStartAsyncGeneratorMethod(lookaheadOffset) && !this.canStartAsyncMethod(lookaheadOffset) && !this.canStartGetterMethod(lookaheadOffset) && !this.canStartSetterMethod(lookaheadOffset) && this.classElementNameIsFollowedByLParen(lookaheadOffset));
  }
  canStartClassElementNameAt(lookaheadOffset: number): any {
    return (this.canStartClassPropertyName(lookaheadOffset) || this.canStartToken("PrivateIdentifier", lookaheadOffset));
  }
  noLineBreakBefore(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && !this.LA(lookaheadOffset).hasLineBreakBefore());
  }
  classElementNameIsFollowedByLParen(lookaheadOffset: number): any {
    return (__qin_binary__(">=", this.classElementNameEndOffset(lookaheadOffset), 0.0) && __QinJavaLangString.equals("LParen", this.tokenNameAt(__qin_binary__("+", this.classElementNameEndOffset(lookaheadOffset), 1.0))));
  }
  classElementNameEndOffset(lookaheadOffset: number): any {
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if ((__QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || __QinJavaLangString.equals("PrivateIdentifier", tokenName))) {
      return lookaheadOffset;
    }
    if ((!__QinJavaLangString.equals("LBracket", tokenName))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    let depth: any = 1.0;
    let offset: any = __qin_binary__("+", lookaheadOffset, 1.0);
    while (__qin_binary__("!=", this.tokenNameAt(offset), null)) {
      let currentTokenName: any = this.tokenNameAt(offset);
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
  canStartClassStaticBlockStatementList(lookaheadOffset: number): any {
    return (this.canStartStatementListItemAt(lookaheadOffset, this.classStaticBlockStatementParams()) || __QinJavaLangString.equals("RBrace", this.tokenNameAt(lookaheadOffset)) || __qin_binary__("==", this.tokenNameAt(lookaheadOffset), null));
  }
  classStaticBlockStatementParams(): any {
    return new com_slime_parser_base_SlimeJavascriptParserBase$StatementParams(false, true, false);
  }
  canStartClassPropertyName(lookaheadOffset: number): any {
    let token: any = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    let tokenName: any = token.tokenName();
    return (__QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("StringLiteral", tokenName) || __QinJavaLangString.equals("NumericLiteral", tokenName) || __QinJavaLangString.equals("LBracket", tokenName));
  }
  canStartClassBindingIdentifier(lookaheadOffset: number): any {
    let token: any = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    let tokenName: any = token.tokenName();
    return (__QinJavaLangString.equals("IdentifierName", tokenName) || __QinJavaLangString.equals("Yield", tokenName) || __QinJavaLangString.equals("Await", tokenName));
  }
  canStartClassBindingPattern(lookaheadOffset: number): any {
    let token: any = this.LA(lookaheadOffset);
    if (__qin_binary__("==", token, null)) {
      return false;
    }
    let tokenName: any = token.tokenName();
    return (__QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("LBracket", tokenName));
  }
  canStartHoistableDeclaration(lookaheadOffset: number): any {
    return (this.canStartFunctionDeclaration(lookaheadOffset) || this.canStartGeneratorDeclaration(lookaheadOffset) || this.canStartAsyncFunctionDeclaration(lookaheadOffset) || this.canStartAsyncGeneratorDeclaration(lookaheadOffset));
  }
  isDefaultOrSourceFunctionDeclarationVariant(variantKey: any): any {
    if (__qin_binary__("==", variantKey, null)) {
      return true;
    }
    return __QinJavaUtilObjects.equals(variantKey, com_slime_parser_class__SlimeClassStaticGrammar.__qin_field_SOURCE_FUNCTION_DECLARATION_VARIANT);
  }
  canStartFunctionDeclaration(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("Function", this.tokenNameAt(lookaheadOffset)) && !__QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartGeneratorDeclaration(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("Function", this.tokenNameAt(lookaheadOffset)) && __QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  canStartAsyncFunctionDeclaration(lookaheadOffset: number): any {
    return (this.isIdentifierValueAt(lookaheadOffset, "async") && this.noLineBreakBefore(__qin_binary__("+", lookaheadOffset, 1.0)) && __QinJavaLangString.equals("Function", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && !__QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 2.0))));
  }
  canStartAsyncGeneratorDeclaration(lookaheadOffset: number): any {
    return (this.isIdentifierValueAt(lookaheadOffset, "async") && this.noLineBreakBefore(__qin_binary__("+", lookaheadOffset, 1.0)) && __QinJavaLangString.equals("Function", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))) && __QinJavaLangString.equals("Asterisk", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 2.0))));
  }
  isIdentifierValueAt(lookaheadOffset: number, value: string): any {
    return (__qin_binary__("!=", this.LA(lookaheadOffset), null) && __QinJavaLangString.equals("IdentifierName", this.LA(lookaheadOffset).tokenName()) && __QinJavaLangString.equals(value, this.LA(lookaheadOffset).value()));
  }
  ForDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ForDeclaration(params);
    }), "ForDeclaration", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ForDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.LetOrConst();
    this.ForBinding(params);
    return null;
  }
  BindingList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingList(params);
    }), "BindingList", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "BindingList", this.classStaticRuntime(null, params));
    return null;
  }
  LexicalBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_LexicalBinding(params);
    }), "LexicalBinding", "SlimeClassParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LexicalBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "LexicalBinding", this.classStaticRuntime(null, params));
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
  canStartStaticRule(...__qin_args: any[]): any {
    if (__qin_args.length === 2 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined")) return this.__qin_overload_canStartStaticRule_2_0(__qin_args[0], __qin_args[1]);
    if (__qin_args.length === 3 && (__qin_args[0] === null || typeof __qin_args[0] === "string") && (__qin_args[1] === null || typeof __qin_args[1] !== "undefined") && typeof __qin_args[2] === "number") return this.__qin_overload_canStartStaticRule_3_1(__qin_args[0], __qin_args[1], __qin_args[2]);
    throw new Error("Unsupported Java overload: canStartStaticRule/" + __qin_args.length);
  }
  __qin_overload_canStartStaticRule_2_0(ruleName: string, variantKey: any): any {
    return this.canStartStaticRule(ruleName, variantKey, 1.0);
  }
  __qin_overload_canStartStaticRule_3_1(ruleName: string, variantKey: any, lookaheadOffset: number): any {
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
      return this.__qin_field_parser.canStartToken("Class", lookaheadOffset);
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("LexicalDeclaration", ruleName))) {
      return this.__qin_field_parser.canStartLetOrConst(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported class static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  testStaticGate(gateId: string): any {
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
  callStaticRule(ruleName: string, variantKey: any): any {
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("LetOrConst", ruleName))) {
      this.__qin_field_parser.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "LetOrConst", this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("Initializer", ruleName))) {
      let initializerParams: any = (__qin_binary__("==", this.__qin_field_expressionParams, null) ? new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, false, false) : this.__qin_field_expressionParams);
      this.__qin_field_parser.Initializer(initializerParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassHeritage", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("ClassHeritage static call requires DeclarationParams");
      }
      this.__qin_field_parser.ClassHeritage(this.__qin_field_declarationParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassBody", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("ClassBody static call requires DeclarationParams");
      }
      this.__qin_field_parser.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassBody", this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassElementList", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("ClassElementList static call requires DeclarationParams");
      }
      this.__qin_field_parser.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassElementList", this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassElement", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("ClassElement static call requires DeclarationParams");
      }
      this.__qin_field_parser.ClassElement(this.__qin_field_declarationParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("MethodDefinition", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("MethodDefinition static call requires ExpressionParams");
      }
      this.__qin_field_parser.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "MethodDefinition", this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("FieldDefinition", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("FieldDefinition static call requires DeclarationParams");
      }
      this.__qin_field_parser.FieldDefinition(this.__qin_field_declarationParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("SemicolonASI", ruleName))) {
      this.__qin_field_parser.SemicolonASI();
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("GeneratorMethod", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("GeneratorMethod static call requires ExpressionParams");
      }
      this.__qin_field_parser.GeneratorMethod(this.__qin_field_expressionParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("AsyncGeneratorMethod", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("AsyncGeneratorMethod static call requires ExpressionParams");
      }
      this.__qin_field_parser.AsyncGeneratorMethod(this.__qin_field_expressionParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("AsyncMethod", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("AsyncMethod static call requires ExpressionParams");
      }
      this.__qin_field_parser.AsyncMethod(this.__qin_field_expressionParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("FunctionBody", ruleName))) {
      this.__qin_field_parser.FunctionBody();
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("PropertySetParameterList", ruleName))) {
      this.__qin_field_parser.PropertySetParameterList();
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("UniqueFormalParameters", ruleName))) {
      this.__qin_field_parser.UniqueFormalParameters();
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassStaticBlock", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("ClassStaticBlock static call requires DeclarationParams");
      }
      this.__qin_field_parser.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassStaticBlock", this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassStaticBlockBody", ruleName))) {
      this.__qin_field_parser.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassStaticBlockBody", this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassStaticBlockStatementList", ruleName))) {
      this.__qin_field_parser.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "ClassStaticBlockStatementList", this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("StatementList", ruleName))) {
      this.__qin_field_parser.StatementList(this.__qin_field_parser.classStaticBlockStatementParams());
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("PropertyName", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("PropertyName static call requires ExpressionParams");
      }
      this.__qin_field_parser.PropertyName(this.__qin_field_expressionParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("PrivateIdentifier", ruleName))) {
      this.__qin_field_parser.PrivateIdentifier();
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("BindingIdentifier", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("BindingIdentifier static call requires ExpressionParams");
      }
      this.__qin_field_parser.BindingIdentifier(this.__qin_field_expressionParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("BindingPattern", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("BindingPattern static call requires ExpressionParams");
      }
      this.__qin_field_parser.BindingPattern(this.__qin_field_expressionParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("LexicalBinding", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("LexicalBinding static call requires ExpressionParams");
      }
      this.__qin_field_parser.executeStaticRule(com_slime_parser_class__SlimeClassParser.__qin_field_STATIC_CLASS_GRAMMAR, "LexicalBinding", this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("HoistableDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("HoistableDeclaration static call requires DeclarationParams");
      }
      this.__qin_field_parser.HoistableDeclaration(this.__qin_field_declarationParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((this.__qin_field_parser.isDefaultOrSourceFunctionDeclarationVariant(variantKey) && __QinJavaLangString.equals("FunctionDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("FunctionDeclaration static call requires DeclarationParams");
      }
      this.__qin_field_parser.FunctionDeclaration(this.__qin_field_declarationParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((this.__qin_field_parser.isDefaultOrSourceFunctionDeclarationVariant(variantKey) && __QinJavaLangString.equals("GeneratorDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("GeneratorDeclaration static call requires DeclarationParams");
      }
      this.__qin_field_parser.GeneratorDeclaration(this.__qin_field_declarationParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((this.__qin_field_parser.isDefaultOrSourceFunctionDeclarationVariant(variantKey) && __QinJavaLangString.equals("AsyncFunctionDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("AsyncFunctionDeclaration static call requires DeclarationParams");
      }
      this.__qin_field_parser.AsyncFunctionDeclaration(this.__qin_field_declarationParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((this.__qin_field_parser.isDefaultOrSourceFunctionDeclarationVariant(variantKey) && __QinJavaLangString.equals("AsyncGeneratorDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("AsyncGeneratorDeclaration static call requires DeclarationParams");
      }
      this.__qin_field_parser.AsyncGeneratorDeclaration(this.__qin_field_declarationParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("ClassDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_declarationParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("ClassDeclaration static call requires DeclarationParams");
      }
      this.__qin_field_parser.ClassDeclaration(this.__qin_field_declarationParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__qin_binary__("==", variantKey, null) && __QinJavaLangString.equals("LexicalDeclaration", ruleName))) {
      if (__qin_binary__("==", this.__qin_field_expressionParams, null)) {
        throw new __QinJavaLangUnsupportedOperationException("LexicalDeclaration static call requires ExpressionParams");
      }
      this.__qin_field_parser.LexicalDeclaration(this.__qin_field_expressionParams);
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
