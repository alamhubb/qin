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
  Statement(...__qin_args: any[]): void {
    if (__qin_args.length === 1 && (__qin_args[0] === null || __qin_instanceof__(__qin_args[0], com_slime_parser_base_SlimeJavascriptParserBase$StatementParams) || __qin_structural_object__(__qin_args[0]))) return this.__qin_overload_Statement_1_0(__qin_args[0]);
    if (__qin_args.length === 0 && true) return this.__qin_overload_Statement_0_1();
    throw new Error("Unsupported Java overload: Statement/" + __qin_args.length);
  }
  __qin_overload_Statement_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw___qin_overload_Statement_1_0 receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw___qin_overload_Statement_1_0.call(this, params);
    }), "Statement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw___qin_overload_Statement_1_0(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_640: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_640.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_ROOT_GRAMMAR, "Statement", this.statementRootStaticRuntime(params));
    }
    return null;
  }
  __qin_overload_Statement_0_1(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw___qin_overload_Statement_0_1 receiver=this arity=0 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw___qin_overload_Statement_0_1.call(this);
    }), "Statement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_overload_Statement_0_1(): void {
    {
      const __qin_typed_receiver_641: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_641.Statement(com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT);
    }
    return null;
  }
  BlockStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_BlockStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_BlockStatement.call(this, params);
    }), "BlockStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BlockStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_642: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_642.Block(params);
    }
    return null;
  }
  Block(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_Block receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_Block.call(this, params);
    }), "Block", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Block(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_643: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_643.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "Block", this.statementListStaticRuntime(params));
    }
    return null;
  }
  StatementList(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_StatementList receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_StatementList.call(this, params);
    }), "StatementList", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_StatementList(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    if (this.isErrorRecoveryMode()) {
      {
        const __qin_typed_receiver_644: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_644.executeStaticTolerantManyCall("StatementListItem", null, this.statementListStaticRuntime(params), new com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher("RBrace"));
      }
    } else {
      {
        const __qin_typed_receiver_645: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_645.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "StatementList", this.statementListStaticRuntime(params));
      }
    }
    return null;
  }
  StatementListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_StatementListItem receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_StatementListItem.call(this, params);
    }), "StatementListItem", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_StatementListItem(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_646: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_646.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "StatementListItem", this.statementListStaticRuntime(params));
    }
    return null;
  }
  VariableStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_VariableStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_VariableStatement.call(this, params);
    }), "VariableStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_VariableStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_647: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_647.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_VARIABLE_GRAMMAR, "VariableStatement", this.statementVariableStaticRuntime(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await())));
    }
    return null;
  }
  VariableDeclarationList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_VariableDeclarationList receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_VariableDeclarationList.call(this, params);
    }), "VariableDeclarationList", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_VariableDeclarationList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_648: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_648.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_VARIABLE_GRAMMAR, "VariableDeclarationList", this.statementVariableStaticRuntime(params));
    }
    return null;
  }
  VariableDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_VariableDeclaration receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_VariableDeclaration.call(this, params);
    }), "VariableDeclaration", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_VariableDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    {
      const __qin_typed_receiver_649: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_649.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_VARIABLE_GRAMMAR, "VariableDeclaration", this.statementVariableStaticRuntime(params));
    }
    return null;
  }
  EmptyStatement(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_EmptyStatement receiver=this arity=0 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_EmptyStatement.call(this);
    }), "EmptyStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_EmptyStatement(): void {
    {
      const __qin_typed_receiver_650: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_650.Semicolon();
    }
    return null;
  }
  ExpressionStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_ExpressionStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_ExpressionStatement.call(this, params);
    }), "ExpressionStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ExpressionStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_651: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_651.assertLookaheadNotIn(["LBrace", "Function", "Class"]);
    }
    {
      const __qin_typed_receiver_652: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_652.assertNotContextualSequenceNoLT("async", "Function");
    }
    {
      const __qin_typed_receiver_653: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_653.assertNotContextualSequence("let", "LBracket");
    }
    {
      const __qin_typed_receiver_654: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_654.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
    }
    {
      const __qin_typed_receiver_655: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_655.debugLog(("ExpressionStatement after Expression: parseSuccess=" + this.__qin_field_parseSuccess + ", curToken=" + this.LA(1.0) + ", index=" + this.__qin_field_currentIndex));
    }
    {
      const __qin_typed_receiver_656: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_656.SemicolonASI();
    }
    return null;
  }
  IfStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_IfStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_IfStatement.call(this, params);
    }), "IfStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_IfStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_657: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_657.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_IF_GRAMMAR, "IfStatement", this.statementIfStaticRuntime(params));
    }
    return null;
  }
  IfStatementBody(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_IfStatementBody receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_IfStatementBody.call(this, params);
    }), "IfStatementBody", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_IfStatementBody(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_658: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_658.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_BRANCH_GRAMMAR, "IfStatementBody", this.statementBranchStaticRuntime(params));
    }
    return null;
  }
  BreakableStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_BreakableStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_BreakableStatement.call(this, params);
    }), "BreakableStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BreakableStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_659: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_659.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_BRANCH_GRAMMAR, "BreakableStatement", this.statementBranchStaticRuntime(params));
    }
    return null;
  }
  IterationStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_IterationStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_IterationStatement.call(this, params);
    }), "IterationStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_IterationStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_660: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_660.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LOOP_GRAMMAR, "IterationStatement", this.statementLoopStaticRuntime(params));
    }
    return null;
  }
  canStartForInOfStatementHead(): boolean {
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
  canStartOrdinaryForStatementHead(): boolean {
    return (__QinJavaLangString.equals("For", this.tokenNameAt(1.0)) && !this.canStartForInOfStatementHead());
  }
  DoWhileStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_DoWhileStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_DoWhileStatement.call(this, params);
    }), "DoWhileStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_DoWhileStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_661: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_661.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LOOP_GRAMMAR, "DoWhileStatement", this.statementLoopStaticRuntime(params));
    }
    return null;
  }
  WhileStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_WhileStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_WhileStatement.call(this, params);
    }), "WhileStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_WhileStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_662: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_662.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LOOP_GRAMMAR, "WhileStatement", this.statementLoopStaticRuntime(params));
    }
    return null;
  }
  ForStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_ForStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_ForStatement.call(this, params);
    }), "ForStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ForStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_663: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_663.For();
    }
    {
      const __qin_typed_receiver_664: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_664.LParen();
    }
    let tokenName: string = this.tokenNameAt(1.0);
    if (__QinJavaLangString.equals("Var", tokenName)) {
      {
        const __qin_typed_receiver_665: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_665.Var();
      }
      {
        const __qin_typed_receiver_666: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_666.VariableDeclarationList(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, params.yield(), params.await()));
      }
      {
        const __qin_typed_receiver_667: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_667.Semicolon();
      }
      {
        const __qin_typed_receiver_668: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_668.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
      }
      {
        const __qin_typed_receiver_669: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_669.Semicolon();
      }
      {
        const __qin_typed_receiver_670: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_670.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
      }
      {
        const __qin_typed_receiver_671: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_671.RParen();
      }
      {
        const __qin_typed_receiver_672: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_672.Statement(params);
      }
      return null;
    }
    if ((this.matchIdentifierValue("let") || __QinJavaLangString.equals("Const", tokenName))) {
      {
        const __qin_typed_receiver_673: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_673.LexicalDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, params.yield(), params.await()));
      }
      {
        const __qin_typed_receiver_674: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_674.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
      }
      {
        const __qin_typed_receiver_675: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_675.Semicolon();
      }
      {
        const __qin_typed_receiver_676: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_676.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
      }
      {
        const __qin_typed_receiver_677: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_677.RParen();
      }
      {
        const __qin_typed_receiver_678: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_678.Statement(params);
      }
      return null;
    }
    {
      const __qin_typed_receiver_679: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_679.assertNotContextualSequence("let", "LBracket");
    }
    {
      const __qin_typed_receiver_680: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_680.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, params.yield(), params.await()));
    }
    {
      const __qin_typed_receiver_681: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_681.Semicolon();
    }
    {
      const __qin_typed_receiver_682: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_682.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
    }
    {
      const __qin_typed_receiver_683: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_683.Semicolon();
    }
    {
      const __qin_typed_receiver_684: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_684.OptionalForExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
    }
    {
      const __qin_typed_receiver_685: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_685.RParen();
    }
    {
      const __qin_typed_receiver_686: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_686.Statement(params);
    }
    return null;
  }
  OptionalForExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): void {
    if (this.canStartRequiredExpressionAt(1.0)) {
      {
        const __qin_typed_receiver_687: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_687.Expression(params);
      }
    }
    return null;
  }
  ForInOfStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_ForInOfStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_ForInOfStatement.call(this, params);
    }), "ForInOfStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ForInOfStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_688: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_688.For();
    }
    let __qin_await: boolean = this.consumeOptionalForAwait();
    {
      const __qin_typed_receiver_689: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_689.LParen();
    }
    if (__QinJavaLangString.equals("Var", this.tokenNameAt(1.0))) {
      {
        const __qin_typed_receiver_690: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_690.Var();
      }
      if ((this.canStartAnnexBForInInitializer() && !__qin_await)) {
        {
          const __qin_typed_receiver_691: com_slime_parser_statements_SlimeStatementParser = this;
          __qin_typed_receiver_691.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
        }
        {
          const __qin_typed_receiver_692: com_slime_parser_statements_SlimeStatementParser = this;
          __qin_typed_receiver_692.Initializer(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(false, params.yield(), params.await()));
        }
        {
          const __qin_typed_receiver_693: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
          __qin_typed_receiver_693.In();
        }
        {
          const __qin_typed_receiver_694: com_slime_parser_statements_SlimeStatementParser = this;
          __qin_typed_receiver_694.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
        }
      } else {
        {
          const __qin_typed_receiver_695: com_slime_parser_statements_SlimeStatementParser = this;
          __qin_typed_receiver_695.ForBinding(params);
        }
        {
          const __qin_typed_receiver_696: com_slime_parser_statements_SlimeStatementParser = this;
          __qin_typed_receiver_696.ForInOfRhs(params);
        }
      }
      {
        const __qin_typed_receiver_697: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_697.RParen();
      }
      {
        const __qin_typed_receiver_698: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_698.Statement(params);
      }
      return null;
    }
    if ((this.matchIdentifierValue("let") || __QinJavaLangString.equals("Const", this.tokenNameAt(1.0)))) {
      {
        const __qin_typed_receiver_699: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_699.ForDeclaration(params);
      }
      {
        const __qin_typed_receiver_700: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_700.ForInOfRhs(params);
      }
      {
        const __qin_typed_receiver_701: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_701.RParen();
      }
      {
        const __qin_typed_receiver_702: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_702.Statement(params);
      }
      return null;
    }
    {
      const __qin_typed_receiver_703: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_703.assertNotContextual("let");
    }
    {
      const __qin_typed_receiver_704: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_704.LeftHandSideExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
    }
    {
      const __qin_typed_receiver_705: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_705.ForInOfRhs(params);
    }
    {
      const __qin_typed_receiver_706: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_706.RParen();
    }
    {
      const __qin_typed_receiver_707: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_707.Statement(params);
    }
    return null;
  }
  consumeOptionalForAwait(): boolean {
    if (__QinJavaLangString.equals("Await", this.tokenNameAt(1.0))) {
      {
        const __qin_typed_receiver_708: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_708.Await();
      }
      return true;
    }
    return false;
  }
  canStartAnnexBForInInitializer(): boolean {
    return ((__QinJavaLangString.equals("IdentifierName", this.tokenNameAt(1.0)) || __QinJavaLangString.equals("Yield", this.tokenNameAt(1.0)) || __QinJavaLangString.equals("Await", this.tokenNameAt(1.0))) && __QinJavaLangString.equals("Assign", this.tokenNameAt(2.0)));
  }
  ForInOfRhs(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    if (__QinJavaLangString.equals("In", this.tokenNameAt(1.0))) {
      {
        const __qin_typed_receiver_709: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_709.In();
      }
      {
        const __qin_typed_receiver_710: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_710.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
      }
      return null;
    }
    {
      const __qin_typed_receiver_711: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_711.consumeIdentifierValue("of");
    }
    {
      const __qin_typed_receiver_712: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_712.AssignmentExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
    }
    return null;
  }
  ForBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_ForBinding receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_ForBinding.call(this, params);
    }), "ForBinding", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ForBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_713: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_713.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_BRANCH_GRAMMAR, "ForBinding", this.statementBranchStaticRuntime(params));
    }
    return null;
  }
  SwitchStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_SwitchStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_SwitchStatement.call(this, params);
    }), "SwitchStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_SwitchStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_714: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_714.Switch();
    }
    {
      const __qin_typed_receiver_715: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_715.LParen();
    }
    {
      const __qin_typed_receiver_716: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_716.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
    }
    {
      const __qin_typed_receiver_717: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_717.RParen();
    }
    {
      const __qin_typed_receiver_718: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_718.CaseBlock(params);
    }
    return null;
  }
  CaseBlock(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_CaseBlock receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_CaseBlock.call(this, params);
    }), "CaseBlock", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CaseBlock(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_719: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_719.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "CaseBlock", this.statementListStaticRuntime(params));
    }
    return null;
  }
  CaseClauses(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_CaseClauses receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_CaseClauses.call(this, params);
    }), "CaseClauses", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CaseClauses(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_720: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_720.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "CaseClauses", this.statementListStaticRuntime(params));
    }
    return null;
  }
  CaseClause(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_CaseClause receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_CaseClause.call(this, params);
    }), "CaseClause", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CaseClause(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_721: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_721.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "CaseClause", this.statementListStaticRuntime(params));
    }
    return null;
  }
  DefaultClause(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_DefaultClause receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_DefaultClause.call(this, params);
    }), "DefaultClause", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_DefaultClause(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_722: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_722.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "DefaultClause", this.statementListStaticRuntime(params));
    }
    return null;
  }
  ContinueStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_ContinueStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_ContinueStatement.call(this, params);
    }), "ContinueStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ContinueStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_723: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_723.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_JUMP_GRAMMAR, "ContinueStatement", this.statementJumpStaticRuntime(params));
    }
    return null;
  }
  BreakStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_BreakStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_BreakStatement.call(this, params);
    }), "BreakStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BreakStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_724: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_724.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_JUMP_GRAMMAR, "BreakStatement", this.statementJumpStaticRuntime(params));
    }
    return null;
  }
  ReturnStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_ReturnStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_ReturnStatement.call(this, params);
    }), "ReturnStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ReturnStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_725: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_725.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_JUMP_GRAMMAR, "ReturnStatement", this.statementJumpStaticRuntime(params));
    }
    return null;
  }
  statementRootStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_statements_SlimeStatementParser$StatementRootStaticRuntime(this, params);
  }
  canStartStatementRootExternalRuleAt(ruleName: string, lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
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
  statementLoopStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_statements_SlimeStatementParser$StatementLoopStaticRuntime(this, params);
  }
  statementTryStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_statements_SlimeStatementParser$StatementTryStaticRuntime(this, params);
  }
  statementIfStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_statements_SlimeStatementParser$StatementIfStaticRuntime(this, params);
  }
  executeStaticAction(ruleId: number, variantId: number, actionId: number): any {
    let statementParams: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams = null as any;
    let ruleNames: string[] = this.staticRuleNamesById();
    if ((__qin_binary__("==", variantId, 0.0) && __qin_binary__("==", actionId, 0.0) && __qin_binary__(">=", ruleId, 0.0) && __qin_binary__("<", ruleId, ruleNames.length) && __QinJavaLangString.equals("IfStatement", ruleNames[ruleId]))) {
      let invocationArgument: any = this.activeStaticInvocationArgument();
      let effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams = ((__qin_instanceof__(invocationArgument, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams) && (statementParams = invocationArgument, true)) ? statementParams : com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT);
      {
        const __qin_typed_receiver_726: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_726.parseIfConditionExpression(effectiveParams);
      }
      return null;
    }
    return super.executeStaticAction(ruleId, variantId, actionId);
  }
  parseIfConditionExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    let effectiveParams: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams = (__qin_binary__("==", params, null) ? com_slime_parser_base_SlimeJavascriptParserBase$StatementParams.__qin_field_DEFAULT : params);
    {
      const __qin_typed_receiver_727: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_727.parseExpressionBody(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, effectiveParams.yield(), effectiveParams.await()));
    }
    return null;
  }
  canStartRequiredExpressionAt(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.tokenNameAt(lookaheadOffset), null) && !__QinJavaLangString.equals("RParen", this.tokenNameAt(lookaheadOffset)) && !__QinJavaLangString.equals("Semicolon", this.tokenNameAt(lookaheadOffset)) && !__QinJavaLangString.equals("RBrace", this.tokenNameAt(lookaheadOffset)));
  }
  statementVariableStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_statements_SlimeStatementParser$StatementVariableStaticRuntime(this, params);
  }
  canStartVariableDeclarationAt(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): boolean {
    return (this.canStartVariableBindingIdentifierAt(lookaheadOffset, params) || this.canStartBindingPatternAt(lookaheadOffset));
  }
  canStartVariableBindingIdentifierAt(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): boolean {
    if (this.canStartIdentifier(lookaheadOffset)) {
      return true;
    }
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__QinJavaLangString.equals("Yield", tokenName)) {
      return (__qin_binary__("==", params, null) || !params.yield());
    }
    if (__QinJavaLangString.equals("Await", tokenName)) {
      return (__qin_binary__("==", params, null) || !params.await());
    }
    return false;
  }
  statementListStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_statements_SlimeStatementParser$StatementListStaticRuntime(this, params);
  }
  statementListStopToken(tokenName: string): com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher {
    return new com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher(tokenName);
  }
  callCaseClauseExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): boolean {
    let startIndex: number = this.__qin_field_currentIndex;
    {
      const __qin_typed_receiver_728: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_728.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
    }
    if ((__qin_binary__(">", this.__qin_field_currentIndex, startIndex) && __QinJavaLangString.equals("Colon", this.tokenNameAt(1.0)))) {
      {
        const __qin_typed_receiver_729: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_729.setParseSuccess();
      }
      return true;
    }
    return (!this.isParserFail());
  }
  canStartStatementListItemAt(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): boolean {
    return (this.canStartDeclarationAt(lookaheadOffset) || this.canStartStatementListStatementAt(lookaheadOffset, params));
  }
  canStartStatementListStatementAt(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if ((__qin_binary__("==", tokenName, null) || __QinJavaLangString.equals("RBrace", tokenName) || __QinJavaLangString.equals("Case", tokenName) || __QinJavaLangString.equals("Default", tokenName))) {
      return false;
    }
    return this.canStartStatementAt(lookaheadOffset, params);
  }
  canStartDeclarationAt(lookaheadOffset: number): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
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
  statementJumpStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_statements_SlimeStatementParser$StatementJumpStaticRuntime(this, params);
  }
  canStartJumpLabelIdentifier(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): boolean {
    if (this.canStartIdentifier(lookaheadOffset)) {
      return true;
    }
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__QinJavaLangString.equals("Yield", tokenName)) {
      return (__qin_binary__("==", params, null) || !params.yield());
    }
    if (__QinJavaLangString.equals("Await", tokenName)) {
      return (__qin_binary__("==", params, null) || !params.await());
    }
    return false;
  }
  canStartJumpExpression(lookaheadOffset: number): boolean {
    return (__qin_binary__("!=", this.tokenNameAt(lookaheadOffset), null) && !__QinJavaLangString.equals("Semicolon", this.tokenNameAt(lookaheadOffset)) && !__QinJavaLangString.equals("RBrace", this.tokenNameAt(lookaheadOffset)));
  }
  WithStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_WithStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_WithStatement.call(this, params);
    }), "WithStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_WithStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_730: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_730.With();
    }
    {
      const __qin_typed_receiver_731: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_731.LParen();
    }
    {
      const __qin_typed_receiver_732: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_732.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
    }
    {
      const __qin_typed_receiver_733: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_733.RParen();
    }
    {
      const __qin_typed_receiver_734: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_734.Statement(params);
    }
    return null;
  }
  LabelledStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_LabelledStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_LabelledStatement.call(this, params);
    }), "LabelledStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LabelledStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_735: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_735.LabelIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
    }
    {
      const __qin_typed_receiver_736: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_736.Colon();
    }
    {
      const __qin_typed_receiver_737: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_737.LabelledItem(params);
    }
    return null;
  }
  LabelledItem(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_LabelledItem receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_LabelledItem.call(this, params);
    }), "LabelledItem", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LabelledItem(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_738: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_738.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_BRANCH_GRAMMAR, "LabelledItem", this.statementBranchStaticRuntime(params));
    }
    return null;
  }
  ThrowStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_ThrowStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_ThrowStatement.call(this, params);
    }), "ThrowStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ThrowStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_739: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_739.Throw();
    }
    {
      const __qin_typed_receiver_740: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_740.assertNoLineBreak();
    }
    {
      const __qin_typed_receiver_741: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_741.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.yield(), params.await()));
    }
    {
      const __qin_typed_receiver_742: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_742.debugLog(("ExpressionStatement after Expression: parseSuccess=" + this.__qin_field_parseSuccess + ", curToken=" + this.LA(1.0) + ", index=" + this.__qin_field_currentIndex));
    }
    {
      const __qin_typed_receiver_743: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_743.SemicolonASI();
    }
    return null;
  }
  TryStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_TryStatement receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_TryStatement.call(this, params);
    }), "TryStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_TryStatement(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_744: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_744.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_TRY_GRAMMAR, "TryStatement", this.statementTryStaticRuntime(params));
    }
    return null;
  }
  Catch(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_Catch receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_Catch.call(this, params);
    }), "Catch", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Catch(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_745: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_745.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_TRY_GRAMMAR, "Catch", this.statementTryStaticRuntime(params));
    }
    return null;
  }
  CatchParameter(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_CatchParameter receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_CatchParameter.call(this, params);
    }), "CatchParameter", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CatchParameter(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_746: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_746.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_BRANCH_GRAMMAR, "CatchParameter", this.statementBranchStaticRuntime(params));
    }
    return null;
  }
  statementBranchStaticRuntime(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): com_subhuti_parser_SubhutiParser$StaticGrammarRuntime {
    return new com_slime_parser_statements_SlimeStatementParser$StatementBranchStaticRuntime(this, params);
  }
  canStartStatementAt(lookaheadOffset: number, params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): boolean {
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    if (__qin_binary__("==", tokenName, null)) {
      return false;
    }
    if ((__QinJavaLangString.equals("LBrace", tokenName) || __QinJavaLangString.equals("Var", tokenName) || __QinJavaLangString.equals("Semicolon", tokenName) || __QinJavaLangString.equals("If", tokenName) || __QinJavaLangString.equals("Do", tokenName) || __QinJavaLangString.equals("For", tokenName) || __QinJavaLangString.equals("Switch", tokenName) || __QinJavaLangString.equals("While", tokenName) || __QinJavaLangString.equals("Continue", tokenName) || __QinJavaLangString.equals("Break", tokenName) || (__QinJavaLangString.equals("Return", tokenName) && params.returnAllowed()) || __QinJavaLangString.equals("With", tokenName) || __QinJavaLangString.equals("Throw", tokenName) || __QinJavaLangString.equals("Try", tokenName) || __QinJavaLangString.equals("Debugger", tokenName))) {
      return true;
    }
    return (__qin_binary__("==", lookaheadOffset, 1.0) && this.canStartExpressionStatement(params));
  }
  canStartIterationStatementAt(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("Do", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("For", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("While", this.tokenNameAt(lookaheadOffset)));
  }
  canStartBindingPatternAt(lookaheadOffset: number): boolean {
    return (__QinJavaLangString.equals("LBrace", this.tokenNameAt(lookaheadOffset)) || __QinJavaLangString.equals("LBracket", this.tokenNameAt(lookaheadOffset)));
  }
  canStartBindingIdentifierAt(lookaheadOffset: number): boolean {
    if (this.canStartIdentifier(lookaheadOffset)) {
      return true;
    }
    let tokenName: string = this.tokenNameAt(lookaheadOffset);
    return (__QinJavaLangString.equals("Yield", tokenName) || __QinJavaLangString.equals("Await", tokenName));
  }
  Finally(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_Finally receiver=this arity=1 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_Finally.call(this, params);
    }), "Finally", "SlimeStatementParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Finally(params: com_slime_parser_base_SlimeJavascriptParserBase$StatementParams): void {
    {
      const __qin_typed_receiver_747: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_747.Finally();
    }
    {
      const __qin_typed_receiver_748: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_748.Block(params);
    }
    return null;
  }
  DebuggerStatement(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_DebuggerStatement receiver=this arity=0 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_DebuggerStatement.call(this);
    }), "DebuggerStatement", "SlimeStatementParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_DebuggerStatement(): void {
    {
      const __qin_typed_receiver_749: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_749.Debugger();
    }
    {
      const __qin_typed_receiver_750: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_750.SemicolonASI();
    }
    return null;
  }
  SemicolonASI(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.parser.statements.SlimeStatementParser method=__qin_subhuti_raw_SemicolonASI receiver=this arity=0 */ com_slime_parser_statements_SlimeStatementParser.prototype.__qin_subhuti_raw_SemicolonASI.call(this);
    }), "SemicolonASI", "SlimeStatementParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_SemicolonASI(): void {
    if (this.match("Semicolon")) {
      {
        const __qin_typed_receiver_751: com_slime_parser_consumer_SlimeTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_751.Semicolon();
      }
      return null;
    }
    if ((!this.canAutoInsertSemicolon())) {
      let curToken: com_subhuti_struct_SubhutiMatchToken = this.LA(1.0);
      {
        const __qin_typed_receiver_752: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_752.debugLog(("SemicolonASI fail: curToken=" + curToken + ", lineBreakBefore=" + (__qin_binary__("!=", curToken, null) && curToken.hasLineBreakBefore()) + ", index=" + this.__qin_field_currentIndex));
      }
      {
        const __qin_typed_receiver_753: com_slime_parser_statements_SlimeStatementParser = this;
        __qin_typed_receiver_753.setParseFail();
      }
      return null;
    }
    {
      const __qin_typed_receiver_754: com_slime_parser_statements_SlimeStatementParser = this;
      __qin_typed_receiver_754.setParseSuccess();
    }
    return null;
  }
  canAutoInsertSemicolon(): boolean {
    if (this.isEof()) {
      return true;
    }
    let curToken: com_subhuti_struct_SubhutiMatchToken = this.LA(1.0);
    if (__qin_binary__("==", curToken, null)) {
      return true;
    }
    if (curToken.hasLineBreakBefore()) {
      return true;
    }
    let nextIndex: number | null = curToken.index();
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
  allowStaticNonNullableEmptySuccess(ruleName: string): boolean {
    return (__QinJavaLangString.equals("SemicolonASI", ruleName) || super.allowStaticNonNullableEmptySuccess(ruleName));
  }
  hasLineTerminatorBetween(start: number, end: number): boolean {
    if ((__qin_binary__("<", start, 0.0) || __qin_binary__(">", end, __QinJavaLangString.length(this.__qin_field_sourceCode)) || __qin_binary__(">=", start, end))) {
      return false;
    }
    for (let i: number = start; __qin_binary__("<", i, end); i++) {
      let ch: number = __QinJavaLangString.charAt(this.__qin_field_sourceCode, i);
      if ((__qin_binary__("==", ch, "\n") || __qin_binary__("==", ch, "\r") || __qin_binary__("==", ch, " ") || __qin_binary__("==", ch, " "))) {
        return true;
      }
    }
    return false;
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
  testStaticGate(gateId: string): boolean {
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement root static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    return this.__qin_field_parser.canStartStatementRootExternalRuleAt(ruleName, lookaheadOffset, this.__qin_field_effectiveParams);
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement root static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("BlockStatement", ruleName)) {
      {
        const __qin_typed_receiver_755: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_755.BlockStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("VariableStatement", ruleName)) {
      {
        const __qin_typed_receiver_756: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_756.VariableStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("EmptyStatement", ruleName)) {
      {
        const __qin_typed_receiver_757: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_757.EmptyStatement();
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ExpressionStatement", ruleName)) {
      {
        const __qin_typed_receiver_758: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_758.ExpressionStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("IfStatement", ruleName)) {
      {
        const __qin_typed_receiver_759: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_759.IfStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BreakableStatement", ruleName)) {
      {
        const __qin_typed_receiver_760: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_760.BreakableStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ContinueStatement", ruleName)) {
      {
        const __qin_typed_receiver_761: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_761.ContinueStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BreakStatement", ruleName)) {
      {
        const __qin_typed_receiver_762: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_762.BreakStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ReturnStatement", ruleName)) {
      {
        const __qin_typed_receiver_763: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_763.ReturnStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("WithStatement", ruleName)) {
      {
        const __qin_typed_receiver_764: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_764.WithStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("LabelledStatement", ruleName)) {
      {
        const __qin_typed_receiver_765: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_765.LabelledStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ThrowStatement", ruleName)) {
      {
        const __qin_typed_receiver_766: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_766.ThrowStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("TryStatement", ruleName)) {
      {
        const __qin_typed_receiver_767: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_767.TryStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("DebuggerStatement", ruleName)) {
      {
        const __qin_typed_receiver_768: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_768.DebuggerStatement();
      }
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
  runStaticAction(actionId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_statements_SlimeStatementLoopStaticGrammar.__qin_field_ACTION_STATEMENT, actionId)) {
      {
        const __qin_typed_receiver_769: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_769.Statement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals(com_slime_parser_statements_SlimeStatementLoopStaticGrammar.__qin_field_ACTION_EXPRESSION, actionId)) {
      {
        const __qin_typed_receiver_770: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_770.parseExpressionBody(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.yield(), this.__qin_field_effectiveParams.await()));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement loop static action: " + actionId));
  }
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals("forInOfStatementHead", gateId)) {
      return this.__qin_field_parser.canStartForInOfStatementHead();
    }
    if (__QinJavaLangString.equals("ordinaryForStatementHead", gateId)) {
      return this.__qin_field_parser.canStartOrdinaryForStatementHead();
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement loop static gate: " + gateId));
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement loop static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("Statement", ruleName)) {
      return this.__qin_field_parser.canStartStatementAt(lookaheadOffset, this.__qin_field_effectiveParams);
    }
    if (__QinJavaLangString.equals("Expression", ruleName)) {
      return this.__qin_field_parser.canStartAssignmentExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.yield(), this.__qin_field_effectiveParams.await()), lookaheadOffset);
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement loop static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Statement", ruleName)) {
      {
        const __qin_typed_receiver_771: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_771.Statement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Expression", ruleName)) {
      {
        const __qin_typed_receiver_772: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_772.parseExpressionBody(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.yield(), this.__qin_field_effectiveParams.await()));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("DoWhileStatement", ruleName)) {
      {
        const __qin_typed_receiver_773: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_773.DoWhileStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("WhileStatement", ruleName)) {
      {
        const __qin_typed_receiver_774: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_774.WhileStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ForStatement", ruleName)) {
      {
        const __qin_typed_receiver_775: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_775.ForStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("ForInOfStatement", ruleName)) {
      {
        const __qin_typed_receiver_776: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_776.ForInOfStatement(this.__qin_field_effectiveParams);
      }
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement try static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Block", ruleName)) {
      {
        const __qin_typed_receiver_777: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_777.Block(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Catch", ruleName)) {
      {
        const __qin_typed_receiver_778: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_778.Catch(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Finally", ruleName)) {
      {
        const __qin_typed_receiver_779: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_779.Finally(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CatchParameter", ruleName)) {
      {
        const __qin_typed_receiver_780: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_780.CatchParameter(this.__qin_field_effectiveParams);
      }
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
  runStaticAction(actionId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_statements_SlimeStatementIfStaticGrammar.__qin_field_ACTION_CONDITION_EXPRESSION, actionId)) {
      {
        const __qin_typed_receiver_781: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_781.parseIfConditionExpression(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement if static action: " + actionId));
  }
  testStaticGate(gateId: string): boolean {
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement if static gate: " + gateId));
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
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement if static rule start variant: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
    }
    if (__QinJavaLangString.equals("IfStatementBody", ruleName)) {
      return (this.__qin_field_parser.canStartStatementAt(lookaheadOffset, this.__qin_field_effectiveParams) || __QinJavaLangString.equals("Function", this.__qin_field_parser.tokenNameAt(lookaheadOffset)));
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement if static rule start: " + ruleName + "@" + variantKey + " offset=" + lookaheadOffset));
  }
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement if static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("IfStatementBody", ruleName)) {
      {
        const __qin_typed_receiver_782: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_782.IfStatementBody(this.__qin_field_effectiveParams);
      }
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement variable static rule call variant: " + ruleName + "@" + variantKey));
    }
    if ((__QinJavaLangString.equals("VariableDeclarationList", ruleName) || __QinJavaLangString.equals("VariableDeclaration", ruleName))) {
      {
        const __qin_typed_receiver_783: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_783.executeStaticRule(STATIC_STATEMENT_VARIABLE_GRAMMAR, ruleName, this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      {
        const __qin_typed_receiver_784: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_784.BindingIdentifier(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingPattern", ruleName)) {
      {
        const __qin_typed_receiver_785: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_785.BindingPattern(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Initializer", ruleName)) {
      {
        const __qin_typed_receiver_786: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_786.Initializer(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("SemicolonASI", ruleName)) {
      {
        const __qin_typed_receiver_787: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_787.SemicolonASI();
      }
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement list static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("StatementList", ruleName)) {
      if (this.__qin_field_parser.isErrorRecoveryMode()) {
        {
          const __qin_typed_receiver_788: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
          __qin_typed_receiver_788.executeStaticTolerantManyCall("StatementListItem", null, this, this.__qin_field_parser.statementListStopToken("RBrace"));
        }
      } else {
        {
          const __qin_typed_receiver_789: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
          __qin_typed_receiver_789.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "StatementList", this);
        }
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("StatementListItem", ruleName)) {
      {
        const __qin_typed_receiver_790: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_790.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "StatementListItem", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("CaseBlock", ruleName)) {
      {
        const __qin_typed_receiver_791: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_791.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, "CaseBlock", this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("DefaultClause", ruleName)) {
      {
        const __qin_typed_receiver_792: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_792.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, ruleName, this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if ((__QinJavaLangString.equals("CaseClauses", ruleName) || __QinJavaLangString.equals("CaseClause", ruleName))) {
      {
        const __qin_typed_receiver_793: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_793.executeStaticRule(com_slime_parser_statements_SlimeStatementParser.__qin_field_STATIC_STATEMENT_LIST_GRAMMAR, ruleName, this);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Expression", ruleName)) {
      return this.__qin_field_parser.callCaseClauseExpression(this.__qin_field_effectiveParams);
    }
    if (__QinJavaLangString.equals("Declaration", ruleName)) {
      {
        const __qin_typed_receiver_794: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_794.Declaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(this.__qin_field_effectiveParams.yield(), this.__qin_field_effectiveParams.await(), false));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Statement", ruleName)) {
      {
        const __qin_typed_receiver_795: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_795.Statement(this.__qin_field_effectiveParams);
      }
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
  testStaticGate(gateId: string): boolean {
    if (__QinJavaLangString.equals(com_slime_parser_statements_SlimeStatementJumpStaticGrammar.__qin_field_GATE_NO_LINE_BREAK, gateId)) {
      return (!this.__qin_field_parser.lookaheadHasLineBreak());
    }
    throw new __QinJavaLangUnsupportedOperationException(("unsupported statement jump static gate: " + gateId));
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement jump static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("LabelIdentifier", ruleName)) {
      {
        const __qin_typed_receiver_796: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_796.LabelIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.yield(), this.__qin_field_effectiveParams.await()));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("Expression", ruleName)) {
      {
        const __qin_typed_receiver_797: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_797.Expression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.yield(), this.__qin_field_effectiveParams.await()));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("SemicolonASI", ruleName)) {
      {
        const __qin_typed_receiver_798: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_798.SemicolonASI();
      }
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
  callStaticRule(ruleName: string, variantKey: any): boolean {
    if (__qin_binary__("!=", variantKey, null)) {
      throw new __QinJavaLangUnsupportedOperationException(("unsupported statement branch static rule call variant: " + ruleName + "@" + variantKey));
    }
    if (__QinJavaLangString.equals("Statement", ruleName)) {
      {
        const __qin_typed_receiver_799: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_799.Statement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("FunctionDeclaration", ruleName)) {
      {
        const __qin_typed_receiver_800: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_800.FunctionDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(this.__qin_field_effectiveParams.yield(), this.__qin_field_effectiveParams.await(), false));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("IterationStatement", ruleName)) {
      {
        const __qin_typed_receiver_801: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_801.IterationStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("SwitchStatement", ruleName)) {
      {
        const __qin_typed_receiver_802: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_802.SwitchStatement(this.__qin_field_effectiveParams);
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingIdentifier", ruleName)) {
      {
        const __qin_typed_receiver_803: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_803.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.yield(), this.__qin_field_effectiveParams.await()));
      }
      return (!this.__qin_field_parser.isParserFail());
    }
    if (__QinJavaLangString.equals("BindingPattern", ruleName)) {
      {
        const __qin_typed_receiver_804: com_slime_parser_statements_SlimeStatementParser = this.__qin_field_parser;
        __qin_typed_receiver_804.BindingPattern(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, this.__qin_field_effectiveParams.yield(), this.__qin_field_effectiveParams.await()));
      }
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
