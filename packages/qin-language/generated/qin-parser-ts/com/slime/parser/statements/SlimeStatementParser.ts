import { com_slime_parser_expressions_SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser as SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentOperatorStaticRuntime as AssignmentOperatorStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime, com_slime_parser_expressions_SlimeAssignmentExpressionParser$AssignmentExpressionStaticRuntime as AssignmentExpressionStaticRuntime } from "../expressions/SlimeAssignmentExpressionParser.ts";
import { com_subhuti_parser_SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar as SubhutiStaticGrammar, com_subhuti_parser_SubhutiStaticGrammar$NodeKind, com_subhuti_parser_SubhutiStaticGrammar$NodeKind as NodeKind, com_subhuti_parser_SubhutiStaticGrammar$SourceRef, com_subhuti_parser_SubhutiStaticGrammar$SourceRef as SourceRef, com_subhuti_parser_SubhutiStaticGrammar$RuleVariantKey, com_subhuti_parser_SubhutiStaticGrammar$RuleDef, com_subhuti_parser_SubhutiStaticGrammar$RuleDef as RuleDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$AlternativeDef as AlternativeDef, com_subhuti_parser_SubhutiStaticGrammar$Node, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$GrammarBuilder as GrammarBuilder, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner, com_subhuti_parser_SubhutiStaticGrammar$OccurrenceAssigner as OccurrenceAssigner } from "../../../subhuti/parser/SubhutiStaticGrammar.ts";
import { com_slime_parser_statements_SlimeStatementRootStaticGrammar, com_slime_parser_statements_SlimeStatementRootStaticGrammar as SlimeStatementRootStaticGrammar } from "./SlimeStatementRootStaticGrammar.ts";
import { com_slime_parser_statements_SlimeStatementJumpStaticGrammar, com_slime_parser_statements_SlimeStatementJumpStaticGrammar as SlimeStatementJumpStaticGrammar } from "./SlimeStatementJumpStaticGrammar.ts";
import { com_slime_parser_statements_SlimeStatementBranchStaticGrammar, com_slime_parser_statements_SlimeStatementBranchStaticGrammar as SlimeStatementBranchStaticGrammar } from "./SlimeStatementBranchStaticGrammar.ts";
import { com_slime_parser_statements_SlimeStatementListStaticGrammar, com_slime_parser_statements_SlimeStatementListStaticGrammar as SlimeStatementListStaticGrammar } from "./SlimeStatementListStaticGrammar.ts";
import { com_slime_parser_statements_SlimeStatementVariableStaticGrammar, com_slime_parser_statements_SlimeStatementVariableStaticGrammar as SlimeStatementVariableStaticGrammar } from "./SlimeStatementVariableStaticGrammar.ts";
import { com_slime_parser_statements_SlimeStatementIfStaticGrammar, com_slime_parser_statements_SlimeStatementIfStaticGrammar as SlimeStatementIfStaticGrammar } from "./SlimeStatementIfStaticGrammar.ts";
import { com_slime_parser_statements_SlimeStatementTryStaticGrammar, com_slime_parser_statements_SlimeStatementTryStaticGrammar as SlimeStatementTryStaticGrammar } from "./SlimeStatementTryStaticGrammar.ts";
import { com_slime_parser_statements_SlimeStatementLoopStaticGrammar, com_slime_parser_statements_SlimeStatementLoopStaticGrammar as SlimeStatementLoopStaticGrammar } from "./SlimeStatementLoopStaticGrammar.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../base/SlimeJavascriptParserBase.ts";
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
const UnsupportedOperationException = __QinJavaLangUnsupportedOperationException;
class com_slime_parser_statements_SlimeStatementParser extends com_slime_parser_expressions_SlimeAssignmentExpressionParser {
  static __qin_field_STATIC_STATEMENT_ROOT_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_STATEMENT_JUMP_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_STATEMENT_BRANCH_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_STATEMENT_LIST_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_STATEMENT_VARIABLE_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_STATEMENT_IF_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_STATEMENT_TRY_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  static __qin_field_STATIC_STATEMENT_LOOP_GRAMMAR: com_subhuti_parser_SubhutiStaticGrammar | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_parser_statements_SlimeStatementParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeStatementParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_statements_SlimeStatementParser_1_0(sourceCode: string): void {
    null;
  }
  Statement(...__qin_args: any[]): any {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$StatementParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_Statement_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_Statement_0_1();
    throw new Error("Unsupported Java overload: Statement/" + __qin_args.length);
  }
  __qin_overload_Statement_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_Statement_1_0(params);
    }), "Statement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_Statement_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_ROOT_GRAMMAR, "Statement", this.statementRootStaticRuntime(params));
    return null;
  }
  __qin_overload_Statement_0_1(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_overload_Statement_0_1();
    }), "Statement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_Statement_0_1(): any {
    this.Statement(com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT);
    return null;
  }
  BlockStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BlockStatement(params);
    }), "BlockStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BlockStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.Block(params);
    return null;
  }
  Block(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Block(params);
    }), "Block", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Block(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "Block", this.statementListStaticRuntime(params));
    return null;
  }
  StatementList(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_StatementList(params);
    }), "StatementList", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_StatementList(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    if (this.isErrorRecoveryMode()) {
      this.executeStaticTolerantManyCall("StatementListItem", null, this.statementListStaticRuntime(params), new com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher("RBrace"));
    } else {
      this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "StatementList", this.statementListStaticRuntime(params));
    }
    return null;
  }
  StatementListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_StatementListItem(params);
    }), "StatementListItem", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_StatementListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "StatementListItem", this.statementListStaticRuntime(params));
    return null;
  }
  VariableStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_VariableStatement(params);
    }), "VariableStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_VariableStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_VARIABLE_GRAMMAR, "VariableStatement", this.statementVariableStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await())));
    return null;
  }
  VariableDeclarationList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_VariableDeclarationList(params);
    }), "VariableDeclarationList", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_VariableDeclarationList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_VARIABLE_GRAMMAR, "VariableDeclarationList", this.statementVariableStaticRuntime(params));
    return null;
  }
  VariableDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_VariableDeclaration(params);
    }), "VariableDeclaration", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_VariableDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_VARIABLE_GRAMMAR, "VariableDeclaration", this.statementVariableStaticRuntime(params));
    return null;
  }
  EmptyStatement(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_EmptyStatement();
    }), "EmptyStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_EmptyStatement(): any {
    this.__qin_field_tokenConsumer.Semicolon();
    return null;
  }
  ExpressionStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ExpressionStatement(params);
    }), "ExpressionStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ExpressionStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.assertLookaheadNotIn(["LBrace", "Function", "Class"]);
    this.assertNotContextualSequenceNoLT("async", "Function");
    this.assertNotContextualSequence("let", "LBracket");
    this.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    this.debugLog(("ExpressionStatement after Expression: parseSuccess=" + this.__qin_field_parseSuccess + ", curToken=" + this.LA(1.0) + ", index=" + this.__qin_field_currentIndex));
    this.SemicolonASI();
    return null;
  }
  IfStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_IfStatement(params);
    }), "IfStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_IfStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_IF_GRAMMAR, "IfStatement", this.statementIfStaticRuntime(params));
    return null;
  }
  IfStatementBody(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_IfStatementBody(params);
    }), "IfStatementBody", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_IfStatementBody(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_BRANCH_GRAMMAR, "IfStatementBody", this.statementBranchStaticRuntime(params));
    return null;
  }
  BreakableStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BreakableStatement(params);
    }), "BreakableStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BreakableStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_BRANCH_GRAMMAR, "BreakableStatement", this.statementBranchStaticRuntime(params));
    return null;
  }
  IterationStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_IterationStatement(params);
    }), "IterationStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_IterationStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LOOP_GRAMMAR, "IterationStatement", this.statementLoopStaticRuntime(params));
    return null;
  }
  canStartForInOfStatementHead(): any {
    if ((!__QinJavaLangString.equals("For", this.tokenNameAt(1.0)))) {
      return false;
    }
    if (__QinJavaLangString.equals("Await", this.tokenNameAt(2.0))) {
      return true;
    }
    if ((!__QinJavaLangString.equals("LParen", this.tokenNameAt(2.0)))) {
      return false;
    }
    let parenDepth: any = 0.0;
    let bracketDepth: any = 0.0;
    let braceDepth: any = 0.0;
    for (let offset: any = 3.0; ; offset++) {
      let tokenName: any = this.tokenNameAt(offset);
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
  canStartOrdinaryForStatementHead(): any {
    return (__QinJavaLangString.equals("For", this.tokenNameAt(1.0)) && !this.canStartForInOfStatementHead());
  }
  DoWhileStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_DoWhileStatement(params);
    }), "DoWhileStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_DoWhileStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LOOP_GRAMMAR, "DoWhileStatement", this.statementLoopStaticRuntime(params));
    return null;
  }
  WhileStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_WhileStatement(params);
    }), "WhileStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_WhileStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LOOP_GRAMMAR, "WhileStatement", this.statementLoopStaticRuntime(params));
    return null;
  }
  ForStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ForStatement(params);
    }), "ForStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ForStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.__qin_field_tokenConsumer.For();
    this.__qin_field_tokenConsumer.LParen();
    let tokenName: any = this.tokenNameAt(1.0);
    if (__QinJavaLangString.equals("Var", tokenName)) {
      this.__qin_field_tokenConsumer.Var();
      this.VariableDeclarationList(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, params.__qin_yield(), params.__qin_await()));
      this.__qin_field_tokenConsumer.Semicolon();
      this.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
      this.__qin_field_tokenConsumer.Semicolon();
      this.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
      this.__qin_field_tokenConsumer.RParen();
      this.Statement(params);
      return null;
    }
    if ((this.matchIdentifierValue("let") || __QinJavaLangString.equals("Const", tokenName))) {
      this.LexicalDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, params.__qin_yield(), params.__qin_await()));
      this.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
      this.__qin_field_tokenConsumer.Semicolon();
      this.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
      this.__qin_field_tokenConsumer.RParen();
      this.Statement(params);
      return null;
    }
    this.assertNotContextualSequence("let", "LBracket");
    this.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, params.__qin_yield(), params.__qin_await()));
    this.__qin_field_tokenConsumer.Semicolon();
    this.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    this.__qin_field_tokenConsumer.Semicolon();
    this.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    this.__qin_field_tokenConsumer.RParen();
    this.Statement(params);
    return null;
  }
  OptionalForExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    if (this.canStartRequiredExpressionAt(1.0)) {
      this.Expression(params);
    }
    return null;
  }
  ForInOfStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ForInOfStatement(params);
    }), "ForInOfStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ForInOfStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.__qin_field_tokenConsumer.For();
    let __qin_await: any = this.consumeOptionalForAwait();
    this.__qin_field_tokenConsumer.LParen();
    if (__QinJavaLangString.equals("Var", this.tokenNameAt(1.0))) {
      this.__qin_field_tokenConsumer.Var();
      if ((this.canStartAnnexBForInInitializer() && !__qin_await)) {
        this.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
        this.Initializer(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, params.__qin_yield(), params.__qin_await()));
        this.__qin_field_tokenConsumer.In();
        this.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
      } else {
        this.ForBinding(params);
        this.ForInOfRhs(params);
      }
      this.__qin_field_tokenConsumer.RParen();
      this.Statement(params);
      return null;
    }
    if ((this.matchIdentifierValue("let") || __QinJavaLangString.equals("Const", this.tokenNameAt(1.0)))) {
      this.ForDeclaration(params);
      this.ForInOfRhs(params);
      this.__qin_field_tokenConsumer.RParen();
      this.Statement(params);
      return null;
    }
    this.assertNotContextual("let");
    this.LeftHandSideExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    this.ForInOfRhs(params);
    this.__qin_field_tokenConsumer.RParen();
    this.Statement(params);
    return null;
  }
  consumeOptionalForAwait(): any {
    if (__QinJavaLangString.equals("Await", this.tokenNameAt(1.0))) {
      this.__qin_field_tokenConsumer.Await();
      return true;
    }
    return false;
  }
  canStartAnnexBForInInitializer(): any {
    return ((__QinJavaLangString.equals("IdentifierName", this.tokenNameAt(1.0)) || __QinJavaLangString.equals("Yield", this.tokenNameAt(1.0)) || __QinJavaLangString.equals("Await", this.tokenNameAt(1.0))) && __QinJavaLangString.equals("Assign", this.tokenNameAt(2.0)));
  }
  ForInOfRhs(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    if (__QinJavaLangString.equals("In", this.tokenNameAt(1.0))) {
      this.__qin_field_tokenConsumer.In();
      this.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
      return null;
    }
    this.consumeIdentifierValue("of");
    this.AssignmentExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    return null;
  }
  ForBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ForBinding(params);
    }), "ForBinding", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ForBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_BRANCH_GRAMMAR, "ForBinding", this.statementBranchStaticRuntime(params));
    return null;
  }
  SwitchStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_SwitchStatement(params);
    }), "SwitchStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_SwitchStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.__qin_field_tokenConsumer.Switch();
    this.__qin_field_tokenConsumer.LParen();
    this.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    this.__qin_field_tokenConsumer.RParen();
    this.CaseBlock(params);
    return null;
  }
  CaseBlock(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CaseBlock(params);
    }), "CaseBlock", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CaseBlock(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "CaseBlock", this.statementListStaticRuntime(params));
    return null;
  }
  CaseClauses(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CaseClauses(params);
    }), "CaseClauses", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CaseClauses(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "CaseClauses", this.statementListStaticRuntime(params));
    return null;
  }
  CaseClause(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CaseClause(params);
    }), "CaseClause", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CaseClause(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "CaseClause", this.statementListStaticRuntime(params));
    return null;
  }
  DefaultClause(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_DefaultClause(params);
    }), "DefaultClause", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_DefaultClause(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "DefaultClause", this.statementListStaticRuntime(params));
    return null;
  }
  ContinueStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ContinueStatement(params);
    }), "ContinueStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ContinueStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_JUMP_GRAMMAR, "ContinueStatement", this.statementJumpStaticRuntime(params));
    return null;
  }
  BreakStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BreakStatement(params);
    }), "BreakStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BreakStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_JUMP_GRAMMAR, "BreakStatement", this.statementJumpStaticRuntime(params));
    return null;
  }
  ReturnStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ReturnStatement(params);
    }), "ReturnStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ReturnStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_JUMP_GRAMMAR, "ReturnStatement", this.statementJumpStaticRuntime(params));
    return null;
  }
  statementRootStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return new com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime(this, params);
  }
  canStartStatementRootExternalRuleAt(ruleName: string, lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if (__QinJavaLangString.equals("BlockStatement", ruleName)) {
      return __QinJavaLangString.equals("LBrace", tokenName);
    }
    if (__QinJavaLangString.equals("VariableStatement", ruleName)) {
      return __QinJavaLangString.equals("Var", tokenName);
    }
    if (__QinJavaLangString.equals("EmptyStatement", ruleName)) {
      return __QinJavaLangString.equals("Semicolon", tokenName);
    }
    if (__QinJavaLangString.equals("ExpressionStatement", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && this.canStartExpressionStatement(params));
    }
    if (__QinJavaLangString.equals("IfStatement", ruleName)) {
      return __QinJavaLangString.equals("If", tokenName);
    }
    if (__QinJavaLangString.equals("BreakableStatement", ruleName)) {
      return (this.canStartIterationStatementAt(lookaheadOffset) || __QinJavaLangString.equals("Switch", tokenName));
    }
    if (__QinJavaLangString.equals("ContinueStatement", ruleName)) {
      return __QinJavaLangString.equals("Continue", tokenName);
    }
    if (__QinJavaLangString.equals("BreakStatement", ruleName)) {
      return __QinJavaLangString.equals("Break", tokenName);
    }
    if (__QinJavaLangString.equals("ReturnStatement", ruleName)) {
      return __QinJavaLangString.equals("Return", tokenName);
    }
    if (__QinJavaLangString.equals("WithStatement", ruleName)) {
      return __QinJavaLangString.equals("With", tokenName);
    }
    if (__QinJavaLangString.equals("LabelledStatement", ruleName)) {
      return (__qin_binary__("==", lookaheadOffset, 1.0) && this.canStartLabelledStatement(params));
    }
    if (__QinJavaLangString.equals("ThrowStatement", ruleName)) {
      return __QinJavaLangString.equals("Throw", tokenName);
    }
    if (__QinJavaLangString.equals("TryStatement", ruleName)) {
      return __QinJavaLangString.equals("Try", tokenName);
    }
    if (__QinJavaLangString.equals("DebuggerStatement", ruleName)) {
      return __QinJavaLangString.equals("Debugger", tokenName);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement root static rule start: " + ruleName + "@null offset=" + lookaheadOffset));
  }
  statementLoopStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return new com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime(this, params);
  }
  statementTryStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return new com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime(this, params);
  }
  statementIfStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return new com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime(this, params);
  }
  executeStaticAction(ruleId: number, variantId: number, actionId: number): any {
    let statementParams: any = null;
    let ruleNames: any = this.staticRuleNamesById();
    if ((__qin_binary__("==", variantId, 0.0) && __qin_binary__("==", actionId, 0.0) && __qin_binary__(">=", ruleId, 0.0) && __qin_binary__("<", ruleId, ruleNames.length) && __QinJavaLangString.equals("IfStatement", ruleNames[ruleId]))) {
      let invocationArgument: any = this.activeStaticInvocationArgument();
      let effectiveParams: any = ((() => { const __qin_pattern_value = invocationArgument; return __qin_instanceof__(__qin_pattern_value, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams) && (statementParams = __qin_pattern_value, true); })() ? statementParams : com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT);
      this.parseIfConditionExpression(effectiveParams);
      return null;
    }
    return super.executeStaticAction(ruleId, variantId, actionId);
  }
  parseIfConditionExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    let effectiveParams: any = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT : params);
    this.parseExpressionBody(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, effectiveParams.__qin_yield(), effectiveParams.__qin_await()));
    return null;
  }
  canStartRequiredExpressionAt(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.tokenNameAt(lookaheadOffset), null) && !__QinJavaLangString.equals("RParen", this.tokenNameAt(lookaheadOffset)) && !__QinJavaLangString.equals("Semicolon", this.tokenNameAt(lookaheadOffset)) && !__QinJavaLangString.equals("RBrace", this.tokenNameAt(lookaheadOffset)));
  }
  statementVariableStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return new com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime(this, params);
  }
  canStartVariableDeclarationAt(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return (this.canStartVariableBindingIdentifierAt(lookaheadOffset, params) || this.canStartBindingPatternAt(lookaheadOffset));
  }
  canStartVariableBindingIdentifierAt(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    if (this.canStartIdentifier(lookaheadOffset)) {
      return true;
    }
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if (__QinJavaLangString.equals("Yield", tokenName)) {
      return (__qin_binary__("==", params, null) || !params.__qin_yield());
    }
    if (__QinJavaLangString.equals("Await", tokenName)) {
      return (__qin_binary__("==", params, null) || !params.__qin_await());
    }
    return false;
  }
  statementListStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return new com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime(this, params);
  }
  statementListStopToken(tokenName: string): any {
    return new com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher(tokenName);
  }
  callCaseClauseExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    let startIndex: any = this.__qin_field_currentIndex;
    this.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    if ((__qin_binary__(">", this.__qin_field_currentIndex, startIndex) && __QinJavaLangString.equals("Colon", this.tokenNameAt(1.0)))) {
      this.setParseSuccess();
      return true;
    }
    return (!this.isParserFail());
  }
  canStartStatementListItemAt(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return (this.canStartDeclarationAt(lookaheadOffset) || this.canStartStatementListStatementAt(lookaheadOffset, params));
  }
  canStartStatementListStatementAt(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if ((__qin_binary__("==", tokenName, null) || __QinJavaLangString.equals("RBrace", tokenName) || __QinJavaLangString.equals("Case", tokenName) || __QinJavaLangString.equals("Default", tokenName))) {
      return false;
    }
    return this.canStartStatementAt(lookaheadOffset, params);
  }
  canStartDeclarationAt(lookaheadOffset: number): any {
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    if ((__QinJavaLangString.equals("At", tokenName) || __QinJavaLangString.equals("Class", tokenName) || __QinJavaLangString.equals("Const", tokenName) || __QinJavaLangString.equals("Function", tokenName) || __QinJavaLangString.equals("Let", tokenName))) {
      return true;
    }
    if ((!__QinJavaLangString.equals("IdentifierName", tokenName))) {
      return false;
    }
    if ((this.matchIdentifierValue("interface", lookaheadOffset) || this.matchIdentifierValue("type", lookaheadOffset) || this.matchIdentifierValue("namespace", lookaheadOffset) || this.matchIdentifierValue("module", lookaheadOffset) || this.matchIdentifierValue("declare", lookaheadOffset) || this.matchIdentifierValue("abstract", lookaheadOffset) || this.matchIdentifierValue("let", lookaheadOffset))) {
      return true;
    }
    return (this.matchIdentifierValue("async", lookaheadOffset) && __QinJavaLangString.equals("Function", this.tokenNameAt(__qin_binary__("+", lookaheadOffset, 1.0))));
  }
  statementJumpStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return new com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime(this, params);
  }
  canStartJumpLabelIdentifier(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    if (this.canStartIdentifier(lookaheadOffset)) {
      return true;
    }
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if (__QinJavaLangString.equals("Yield", tokenName)) {
      return (__qin_binary__("==", params, null) || !params.__qin_yield());
    }
    if (__QinJavaLangString.equals("Await", tokenName)) {
      return (__qin_binary__("==", params, null) || !params.__qin_await());
    }
    return false;
  }
  canStartJumpExpression(lookaheadOffset: number): any {
    return (__qin_binary__("!=", this.tokenNameAt(lookaheadOffset), null) && !__QinJavaLangString.equals("Semicolon", this.tokenNameAt(lookaheadOffset)) && !__QinJavaLangString.equals("RBrace", this.tokenNameAt(lookaheadOffset)));
  }
  WithStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_WithStatement(params);
    }), "WithStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_WithStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.__qin_field_tokenConsumer.With();
    this.__qin_field_tokenConsumer.LParen();
    this.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    this.__qin_field_tokenConsumer.RParen();
    this.Statement(params);
    return null;
  }
  LabelledStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_LabelledStatement(params);
    }), "LabelledStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LabelledStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.LabelIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    this.__qin_field_tokenConsumer.Colon();
    this.LabelledItem(params);
    return null;
  }
  LabelledItem(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_LabelledItem(params);
    }), "LabelledItem", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LabelledItem(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_BRANCH_GRAMMAR, "LabelledItem", this.statementBranchStaticRuntime(params));
    return null;
  }
  ThrowStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ThrowStatement(params);
    }), "ThrowStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ThrowStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.__qin_field_tokenConsumer.Throw();
    this.assertNoLineBreak();
    this.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    this.debugLog(("ExpressionStatement after Expression: parseSuccess=" + this.__qin_field_parseSuccess + ", curToken=" + this.LA(1.0) + ", index=" + this.__qin_field_currentIndex));
    this.SemicolonASI();
    return null;
  }
  TryStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TryStatement(params);
    }), "TryStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_TryStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_TRY_GRAMMAR, "TryStatement", this.statementTryStaticRuntime(params));
    return null;
  }
  Catch(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Catch(params);
    }), "Catch", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Catch(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_TRY_GRAMMAR, "Catch", this.statementTryStaticRuntime(params));
    return null;
  }
  CatchParameter(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CatchParameter(params);
    }), "CatchParameter", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CatchParameter(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_BRANCH_GRAMMAR, "CatchParameter", this.statementBranchStaticRuntime(params));
    return null;
  }
  statementBranchStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return new com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime(this, params);
  }
  canStartStatementAt(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    if ((__QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("Var", tokenName) || __QinJavaLangString.equals("Semicolon", tokenName) || __QinJavaLangString.equals("If", tokenName) || __QinJavaLangString.equals("Do", tokenName) || __QinJavaLangString.equals("For", tokenName) || __QinJavaLangString.equals("Switch", tokenName) || __QinJavaLangString.equals("While", tokenName) || __QinJavaLangString.equals("Continue", tokenName) || __QinJavaLangString.equals("Break", tokenName) || (__QinJavaLangString.equals("Return", tokenName) && params.returnAllowed()) || __QinJavaLangString.equals("With", tokenName) || __QinJavaLangString.equals("Throw", tokenName) || __QinJavaLangString.equals("Try", tokenName) || __QinJavaLangString.equals("Debugger", tokenName))) {
      return true;
    }
    return (__qin_binary__("==", lookaheadOffset, 1.0) && this.canStartExpressionStatement(params));
  }
  canStartIterationStatementAt(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("Do", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("For", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("While", this.tokenNameAt(lookaheadOffset)));
  }
  canStartBindingPatternAt(lookaheadOffset: number): any {
    return (__QinJavaLangString.equals("LBrace", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBracket", this.tokenNameAt(lookaheadOffset)));
  }
  canStartBindingIdentifierAt(lookaheadOffset: number): any {
    if (this.canStartIdentifier(lookaheadOffset)) {
      return true;
    }
    let tokenName: any = this.tokenNameAt(lookaheadOffset);
    return (__QinJavaLangString.equals("Yield", tokenName) || __QinJavaLangString.equals("Await", tokenName));
  }
  Finally(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Finally(params);
    }), "Finally", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Finally(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    this.__qin_field_tokenConsumer.Finally();
    this.Block(params);
    return null;
  }
  DebuggerStatement(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_DebuggerStatement();
    }), "DebuggerStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_DebuggerStatement(): any {
    this.__qin_field_tokenConsumer.Debugger();
    this.SemicolonASI();
    return null;
  }
  SemicolonASI(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_SemicolonASI();
    }), "SemicolonASI", "SlimeStatementParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_SemicolonASI(): any {
    if (this.match("Semicolon")) {
      this.__qin_field_tokenConsumer.Semicolon();
      return null;
    }
    if ((!this.canAutoInsertSemicolon())) {
      let curToken: any = this.LA(1.0);
      this.debugLog(("SemicolonASI fail: curToken=" + curToken + ", lineBreakBefore=" + (__qin_binary__("!=", curToken, null) && curToken.hasLineBreakBefore()) + ", index=" + this.__qin_field_currentIndex));
      this.setParseFail();
      return null;
    }
    this.setParseSuccess();
    return null;
  }
  canAutoInsertSemicolon(): any {
    if (this.isEof()) {
      return true;
    }
    let curToken: any = this.LA(1.0);
    if (__qin_binary__("==", curToken, null)) {
      return true;
    }
    if (curToken.hasLineBreakBefore()) {
      return true;
    }
    let nextIndex: any = curToken.index();
    if ((__qin_binary__("!=", nextIndex, null) && __qin_binary__(">", nextIndex, this.__qin_field_currentIndex))) {
      if (this.hasLineTerminatorBetween(this.__qin_field_currentIndex, nextIndex)) {
        return true;
      }
    }
    if (__QinJavaLangString.equals("RBrace", curToken.tokenName())) {
      return true;
    }
    return false;
  }
  allowStaticNonNullableEmptySuccess(ruleName: string): any {
    return (__QinJavaLangString.equals("SemicolonASI", ruleName) || super.allowStaticNonNullableEmptySuccess(ruleName));
  }
  hasLineTerminatorBetween(start: number, end: number): any {
    if ((__qin_binary__("<", start, 0.0) || __qin_binary__(">", end, __QinJavaLangString.length(this.__qin_field_sourceCode)) || __qin_binary__(">=", start, end))) {
      return false;
    }
    for (let i: any = start; __qin_binary__("<", i, end); i++) {
      let ch: any = __QinJavaLangString.charAt(this.__qin_field_sourceCode, i);
      if ((__qin_binary__("==", ch, "\n") || __qin_binary__("==", ch, "\r") || __qin_binary__("==", ch, " ") || __qin_binary__("==", ch, " "))) {
        return true;
      }
    }
    return false;
  }
  Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    throw new Error("Abstract Java method is not implemented: Declaration");
  }
  LexicalDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    throw new Error("Abstract Java method is not implemented: LexicalDeclaration");
  }
  ForDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): any {
    throw new Error("Abstract Java method is not implemented: ForDeclaration");
  }
  BindingRestElement(): any {
    throw new Error("Abstract Java method is not implemented: BindingRestElement");
  }
  BindingElement(): any {
    throw new Error("Abstract Java method is not implemented: BindingElement");
  }
  FunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    throw new Error("Abstract Java method is not implemented: FunctionDeclaration");
  }
}
const SlimeStatementParser = com_slime_parser_statements_SlimeStatementParser;
class com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime {
  __qin_field_parser: com_slime_parser_statements_SlimeStatementParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_statements_SlimeStatementParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$StatementParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const params: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime_2_0(parser, params);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeStatementParser$StatementRootStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime_2_0(parser: com_slime_parser_statements_SlimeStatementParser, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT : params);
  }
  testStaticGate(gateId: string): any {
    if (__QinJavaLangString.equals(com_slime_parser_statements_SlimeStatementRootStaticGrammar.__qin_field_GATE_CAN_START_EXPRESSION_STATEMENT, gateId)) {
      return this.__qin_field_parser.canStartExpressionStatement(this.__qin_field_effectiveParams);
    }
    if (__QinJavaLangString.equals(com_slime_parser_statements_SlimeStatementRootStaticGrammar.__qin_field_GATE_CAN_START_BREAKABLE_STATEMENT, gateId)) {
      return this.__qin_field_parser.canStartBreakableStatement();
    }
    if (__QinJavaLangString.equals(com_slime_parser_statements_SlimeStatementRootStaticGrammar.__qin_field_GATE_RETURN_ALLOWED, gateId)) {
      return this.__qin_field_effectiveParams.returnAllowed();
    }
    if (__QinJavaLangString.equals(com_slime_parser_statements_SlimeStatementRootStaticGrammar.__qin_field_GATE_CAN_START_LABELLED_STATEMENT, gateId)) {
      return this.__qin_field_parser.canStartLabelledStatement(this.__qin_field_effectiveParams);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement root static gate: " + gateId));
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement root static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    return this.__qin_field_parser.canStartStatementRootExternalRuleAt(ruleName, lookaheadOffset, this.__qin_field_effectiveParams);
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement root static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("BlockStatement", ruleName)) {
      this.__qin_field_parser.BlockStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("VariableStatement", ruleName)) {
      this.__qin_field_parser.VariableStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("EmptyStatement", ruleName)) {
      this.__qin_field_parser.EmptyStatement();
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ExpressionStatement", ruleName)) {
      this.__qin_field_parser.ExpressionStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("IfStatement", ruleName)) {
      this.__qin_field_parser.IfStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BreakableStatement", ruleName)) {
      this.__qin_field_parser.BreakableStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ContinueStatement", ruleName)) {
      this.__qin_field_parser.ContinueStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BreakStatement", ruleName)) {
      this.__qin_field_parser.BreakStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ReturnStatement", ruleName)) {
      this.__qin_field_parser.ReturnStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("WithStatement", ruleName)) {
      this.__qin_field_parser.WithStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("LabelledStatement", ruleName)) {
      this.__qin_field_parser.LabelledStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ThrowStatement", ruleName)) {
      this.__qin_field_parser.ThrowStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TryStatement", ruleName)) {
      this.__qin_field_parser.TryStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("DebuggerStatement", ruleName)) {
      this.__qin_field_parser.DebuggerStatement();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement root static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeStatementParser$StatementRootStaticRuntime = com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime;
class com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime {
  __qin_field_parser: com_slime_parser_statements_SlimeStatementParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_statements_SlimeStatementParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$StatementParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const params: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime_2_0(parser, params);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeStatementParser$StatementLoopStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime_2_0(parser: com_slime_parser_statements_SlimeStatementParser, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT : params);
  }
  runStaticAction(actionId: string): any {
    if (__QinJavaLangString.equals(com_slime_parser_statements_SlimeStatementLoopStaticGrammar.__qin_field_ACTION_STATEMENT, actionId)) {
      this.__qin_field_parser.Statement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals(com_slime_parser_statements_SlimeStatementLoopStaticGrammar.__qin_field_ACTION_EXPRESSION, actionId)) {
      this.__qin_field_parser.parseExpressionBody(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.__qin_yield(), this.__qin_field_effectiveParams.__qin_await()));
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement loop static action: " + actionId));
  }
  testStaticGate(gateId: string): any {
    if (__QinJavaLangString.equals("forInOfStatementHead", gateId)) {
      return this.__qin_field_parser.canStartForInOfStatementHead();
    }
    if (__QinJavaLangString.equals("ordinaryForStatementHead", gateId)) {
      return this.__qin_field_parser.canStartOrdinaryForStatementHead();
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement loop static gate: " + gateId));
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement loop static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Statement", ruleName)) {
      return this.__qin_field_parser.canStartStatementAt(lookaheadOffset, this.__qin_field_effectiveParams);
    }
    if (__QinJavaLangString.equals("Expression", ruleName)) {
      return this.__qin_field_parser.canStartAssignmentExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.__qin_yield(), this.__qin_field_effectiveParams.__qin_await()), lookaheadOffset);
    }
    if (__QinJavaLangString.equals("DoWhileStatement", ruleName)) {
      return __QinJavaLangString.equals("Do", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("WhileStatement", ruleName)) {
      return __QinJavaLangString.equals("While", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("ForStatement", ruleName)) {
      return __QinJavaLangString.equals("For", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("ForInOfStatement", ruleName)) {
      return __QinJavaLangString.equals("For", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement loop static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement loop static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Statement", ruleName)) {
      this.__qin_field_parser.Statement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Expression", ruleName)) {
      this.__qin_field_parser.parseExpressionBody(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.__qin_yield(), this.__qin_field_effectiveParams.__qin_await()));
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("DoWhileStatement", ruleName)) {
      this.__qin_field_parser.DoWhileStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("WhileStatement", ruleName)) {
      this.__qin_field_parser.WhileStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ForStatement", ruleName)) {
      this.__qin_field_parser.ForStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ForInOfStatement", ruleName)) {
      this.__qin_field_parser.ForInOfStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement loop static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeStatementParser$StatementLoopStaticRuntime = com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime;
class com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime {
  __qin_field_parser: com_slime_parser_statements_SlimeStatementParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_statements_SlimeStatementParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$StatementParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const params: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime_2_0(parser, params);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeStatementParser$StatementTryStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime_2_0(parser: com_slime_parser_statements_SlimeStatementParser, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT : params);
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement try static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Block", ruleName)) {
      return __QinJavaLangString.equals("LBrace", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Catch", ruleName)) {
      return __QinJavaLangString.equals("Catch", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Finally", ruleName)) {
      return __QinJavaLangString.equals("Finally", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("CatchParameter", ruleName)) {
      return (this.__qin_field_parser.canStartBindingIdentifierAt(lookaheadOffset) || this.__qin_field_parser.canStartBindingPatternAt(lookaheadOffset));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement try static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement try static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Block", ruleName)) {
      this.__qin_field_parser.Block(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Catch", ruleName)) {
      this.__qin_field_parser.Catch(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Finally", ruleName)) {
      this.__qin_field_parser.Finally(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CatchParameter", ruleName)) {
      this.__qin_field_parser.CatchParameter(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement try static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeStatementParser$StatementTryStaticRuntime = com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime;
class com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime {
  __qin_field_parser: com_slime_parser_statements_SlimeStatementParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_statements_SlimeStatementParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$StatementParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const params: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime_2_0(parser, params);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeStatementParser$StatementIfStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime_2_0(parser: com_slime_parser_statements_SlimeStatementParser, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT : params);
  }
  runStaticAction(actionId: string): any {
    if (__QinJavaLangString.equals(com_slime_parser_statements_SlimeStatementIfStaticGrammar.__qin_field_ACTION_CONDITION_EXPRESSION, actionId)) {
      this.__qin_field_parser.parseIfConditionExpression(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement if static action: " + actionId));
  }
  testStaticGate(gateId: string): any {
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement if static gate: " + gateId));
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement if static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("IfStatementBody", ruleName)) {
      return (this.__qin_field_parser.canStartStatementAt(lookaheadOffset, this.__qin_field_effectiveParams) || __QinJavaLangString.equals("Function", this.__qin_field_parser.tokenNameAt(lookaheadOffset)));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement if static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement if static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("IfStatementBody", ruleName)) {
      this.__qin_field_parser.IfStatementBody(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement if static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeStatementParser$StatementIfStaticRuntime = com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime;
class com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime {
  __qin_field_parser: com_slime_parser_statements_SlimeStatementParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_statements_SlimeStatementParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const params: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime_2_0(parser, params);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeStatementParser$StatementVariableStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime_2_0(parser: com_slime_parser_statements_SlimeStatementParser, params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT : params);
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement variable static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if ((__QinJavaLangString.equals("VariableDeclarationList", ruleName) || __QinJavaLangString.equals("VariableDeclaration", ruleName))) {
      return this.__qin_field_parser.canStartVariableDeclarationAt(lookaheadOffset, this.__qin_field_effectiveParams);
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      return this.__qin_field_parser.canStartVariableBindingIdentifierAt(lookaheadOffset, this.__qin_field_effectiveParams);
    }
    if (__QinJavaLangString.equals("BindingPattern", ruleName)) {
      return this.__qin_field_parser.canStartBindingPatternAt(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("Initializer", ruleName)) {
      return __QinJavaLangString.equals("Assign", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("SemicolonASI", ruleName)) {
      return true;
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement variable static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement variable static rule call variant: " + ruleName + "@" + variantKey));
    }
    if ((__QinJavaLangString.equals("VariableDeclarationList", ruleName) || __QinJavaLangString.equals("VariableDeclaration", ruleName))) {
      this.__qin_field_parser.executeStaticRule(STATIC_STATEMENT_VARIABLE_GRAMMAR, ruleName, this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      this.__qin_field_parser.BindingIdentifier(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingPattern", ruleName)) {
      this.__qin_field_parser.BindingPattern(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Initializer", ruleName)) {
      this.__qin_field_parser.Initializer(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("SemicolonASI", ruleName)) {
      this.__qin_field_parser.SemicolonASI();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement variable static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeStatementParser$StatementVariableStaticRuntime = com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime;
class com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime {
  __qin_field_parser: com_slime_parser_statements_SlimeStatementParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_statements_SlimeStatementParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$StatementParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const params: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime_2_0(parser, params);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeStatementParser$StatementListStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime_2_0(parser: com_slime_parser_statements_SlimeStatementParser, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT : params);
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement list static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("StatementList", ruleName)) {
      return this.__qin_field_parser.canStartStatementListItemAt(lookaheadOffset, this.__qin_field_effectiveParams);
    }
    if (__QinJavaLangString.equals("StatementListItem", ruleName)) {
      return this.__qin_field_parser.canStartStatementListItemAt(lookaheadOffset, this.__qin_field_effectiveParams);
    }
    if (__QinJavaLangString.equals("CaseBlock", ruleName)) {
      return __QinJavaLangString.equals("LBrace", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Declaration", ruleName)) {
      return this.__qin_field_parser.canStartDeclarationAt(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("Statement", ruleName)) {
      return (!this.__qin_field_parser.canStartDeclarationAt(lookaheadOffset) && this.__qin_field_parser.canStartStatementListStatementAt(lookaheadOffset, this.__qin_field_effectiveParams));
    }
    if (__QinJavaLangString.equals("DefaultClause", ruleName)) {
      return __QinJavaLangString.equals("Default", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if ((__QinJavaLangString.equals("CaseClauses", ruleName) || __QinJavaLangString.equals("CaseClause", ruleName))) {
      return __QinJavaLangString.equals("Case", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Expression", ruleName)) {
      return this.__qin_field_parser.canStartRequiredExpressionAt(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement list static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement list static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("StatementList", ruleName)) {
      if (this.__qin_field_parser.isErrorRecoveryMode()) {
        this.__qin_field_parser.executeStaticTolerantManyCall("StatementListItem", null, this, this.__qin_field_parser.statementListStopToken("RBrace"));
      } else {
        this.__qin_field_parser.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "StatementList", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("StatementListItem", ruleName)) {
      this.__qin_field_parser.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "StatementListItem", this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CaseBlock", ruleName)) {
      this.__qin_field_parser.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "CaseBlock", this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("DefaultClause", ruleName)) {
      this.__qin_field_parser.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, ruleName, this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__QinJavaLangString.equals("CaseClauses", ruleName) || __QinJavaLangString.equals("CaseClause", ruleName))) {
      this.__qin_field_parser.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, ruleName, this);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Expression", ruleName)) {
      return this.__qin_field_parser.callCaseClauseExpression(this.__qin_field_effectiveParams);
    }
    if (__QinJavaLangString.equals("Declaration", ruleName)) {
      this.__qin_field_parser.Declaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(this.__qin_field_effectiveParams.__qin_yield(), this.__qin_field_effectiveParams.__qin_await(), false));
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Statement", ruleName)) {
      this.__qin_field_parser.Statement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement list static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeStatementParser$StatementListStaticRuntime = com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime;
class com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime {
  __qin_field_parser: com_slime_parser_statements_SlimeStatementParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_statements_SlimeStatementParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$StatementParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const params: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime_2_0(parser, params);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeStatementParser$StatementJumpStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime_2_0(parser: com_slime_parser_statements_SlimeStatementParser, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT : params);
  }
  testStaticGate(gateId: string): any {
    if (__QinJavaLangString.equals(com_slime_parser_statements_SlimeStatementJumpStaticGrammar.__qin_field_GATE_NO_LINE_BREAK, gateId)) {
      return (!this.__qin_field_parser.lookaheadHasLineBreak());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement jump static gate: " + gateId));
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement jump static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("LabelIdentifier", ruleName)) {
      return this.__qin_field_parser.canStartJumpLabelIdentifier(lookaheadOffset, this.__qin_field_effectiveParams);
    }
    if (__QinJavaLangString.equals("Expression", ruleName)) {
      return this.__qin_field_parser.canStartJumpExpression(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("SemicolonASI", ruleName)) {
      return true;
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement jump static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement jump static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("LabelIdentifier", ruleName)) {
      this.__qin_field_parser.LabelIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.__qin_yield(), this.__qin_field_effectiveParams.__qin_await()));
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Expression", ruleName)) {
      this.__qin_field_parser.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.__qin_yield(), this.__qin_field_effectiveParams.__qin_await()));
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("SemicolonASI", ruleName)) {
      this.__qin_field_parser.SemicolonASI();
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement jump static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeStatementParser$StatementJumpStaticRuntime = com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime;
class com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime {
  __qin_field_parser: com_slime_parser_statements_SlimeStatementParser | null = null as any;
  __qin_field_effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 2 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_statements_SlimeStatementParser)) && (__qin_args[1] === null || __qin_instanceof__(__qin_args[1], com_slime_parser_base_SlimeJavascriptParserBase$StatementParams) || __qin_structural_object__(__qin_args[1]))) {
      const parser: any = __qin_args[0];
      const params: any = __qin_args[1];
      this.__qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime_2_0(parser, params);
      return;
    }
    throw new Error("Unsupported Java constructor overload: SlimeStatementParser$StatementBranchStaticRuntime/" + __qin_args.length);
  }
  __qin_constructor_com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime_2_0(parser: com_slime_parser_statements_SlimeStatementParser, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    this.__qin_field_parser = null;
    this.__qin_field_effectiveParams = null;
    this.__qin_field_parser = parser;
    this.__qin_field_effectiveParams = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT : params);
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement branch static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Statement", ruleName)) {
      return this.__qin_field_parser.canStartStatementAt(lookaheadOffset, this.__qin_field_effectiveParams);
    }
    if (__QinJavaLangString.equals("FunctionDeclaration", ruleName)) {
      return __QinJavaLangString.equals("Function", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("IterationStatement", ruleName)) {
      return this.__qin_field_parser.canStartIterationStatementAt(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("SwitchStatement", ruleName)) {
      return __QinJavaLangString.equals("Switch", this.__qin_field_parser.tokenNameAt(lookaheadOffset));
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      return this.__qin_field_parser.canStartBindingIdentifierAt(lookaheadOffset);
    }
    if (__QinJavaLangString.equals("BindingPattern", ruleName)) {
      return this.__qin_field_parser.canStartBindingPatternAt(lookaheadOffset);
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement branch static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): any {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement branch static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Statement", ruleName)) {
      this.__qin_field_parser.Statement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("FunctionDeclaration", ruleName)) {
      this.__qin_field_parser.FunctionDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(this.__qin_field_effectiveParams.__qin_yield(), this.__qin_field_effectiveParams.__qin_await(), false));
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("IterationStatement", ruleName)) {
      this.__qin_field_parser.IterationStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("SwitchStatement", ruleName)) {
      this.__qin_field_parser.SwitchStatement(this.__qin_field_effectiveParams);
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      this.__qin_field_parser.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.__qin_yield(), this.__qin_field_effectiveParams.__qin_await()));
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingPattern", ruleName)) {
      this.__qin_field_parser.BindingPattern(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.__qin_yield(), this.__qin_field_effectiveParams.__qin_await()));
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement branch static rule call: " + ruleName + "@" + variantKey));
  }
}
com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime.__qin_java_interfaces = ["com.subhuti.parser.SubhutiParser$StaticGrammarRuntime"];
const SlimeStatementParser$StatementBranchStaticRuntime = com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime;
com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_ROOT_GRAMMAR = com_slime_parser_statements_SlimeStatementRootStaticGrammar.grammar();
com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_JUMP_GRAMMAR = com_slime_parser_statements_SlimeStatementJumpStaticGrammar.grammar();
com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_BRANCH_GRAMMAR = com_slime_parser_statements_SlimeStatementBranchStaticGrammar.grammar();
com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR = com_slime_parser_statements_SlimeStatementListStaticGrammar.grammar();
com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_VARIABLE_GRAMMAR = com_slime_parser_statements_SlimeStatementVariableStaticGrammar.grammar();
com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_IF_GRAMMAR = com_slime_parser_statements_SlimeStatementIfStaticGrammar.grammar();
com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_TRY_GRAMMAR = com_slime_parser_statements_SlimeStatementTryStaticGrammar.grammar();
com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LOOP_GRAMMAR = com_slime_parser_statements_SlimeStatementLoopStaticGrammar.grammar();

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_statements_SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime, com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime };
