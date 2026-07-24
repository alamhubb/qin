import { com_slime_java_statement_JavaStatementParser, com_slime_java_statement_JavaStatementParser as JavaStatementParser } from "../statement/JavaStatementParser.ts";
import { com_subhuti_struct_SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken as SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken$Builder } from "../../../subhuti/struct/SubhutiMatchToken.ts";
import { com_slime_java_expression_JavaExpressionParser, com_slime_java_expression_JavaExpressionParser as JavaExpressionParser } from "../expression/JavaExpressionParser.ts";
import { com_slime_java_type_JavaTypeParser, com_slime_java_type_JavaTypeParser as JavaTypeParser } from "../type/JavaTypeParser.ts";
import { com_slime_java_literal_JavaLiteralParser, com_slime_java_literal_JavaLiteralParser as JavaLiteralParser } from "../literal/JavaLiteralParser.ts";
import { com_slime_java_identifier_JavaIdentifierParser, com_slime_java_identifier_JavaIdentifierParser as JavaIdentifierParser } from "../identifier/JavaIdentifierParser.ts";
import { com_slime_java_base_JavaParserBase, com_slime_java_base_JavaParserBase as JavaParserBase } from "../base/JavaParserBase.ts";
import { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser as SubhutiParser, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticGrammarRuntime as StaticGrammarRuntime, com_subhuti_parser_SubhutiParser$StaticPrefix, com_subhuti_parser_SubhutiParser$StaticPrefix as StaticPrefix, com_subhuti_parser_SubhutiParser$StaticChoice, com_subhuti_parser_SubhutiParser$StaticChoice as StaticChoice } from "../../../subhuti/parser/SubhutiParser.ts";
import { com_subhuti_parser_SubhutiParserFinal, com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal } from "../../../subhuti/parser/SubhutiParserFinal.ts";
import { com_subhuti_parser_SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators as SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StaticSourceReturnControl as StaticSourceReturnControl, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher as StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext as AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext as AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$PlannedAlternativeIndexes as PlannedAlternativeIndexes, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$StaticPlannedCandidates as StaticPlannedCandidates, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame as ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenNames as CurrentTokenNames, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys, com_subhuti_parser_SubhutiParserCombinators$CurrentTokenKeys as CurrentTokenKeys } from "../../../subhuti/parser/SubhutiParserCombinators.ts";
import { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore as SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$StaticInvocationArguments as StaticInvocationArguments, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult as RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticExecutionMode as StaticExecutionMode, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$StaticDebugHooks as StaticDebugHooks, com_subhuti_parser_SubhutiParserCore$CacheWork, com_subhuti_parser_SubhutiParserCore$CacheWork as CacheWork, com_subhuti_parser_SubhutiParserCore$FailureWork, com_subhuti_parser_SubhutiParserCore$FailureWork as FailureWork } from "../../../subhuti/parser/SubhutiParserCore.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$ActiveRuleInvocations as ActiveRuleInvocations, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenRecordedException as SubhutiFirstTokenRecordedException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException, com_subhuti_parser_SubhutiParserState$SubhutiFirstTokenUnknownException as SubhutiFirstTokenUnknownException } from "../../../subhuti/parser/SubhutiParserState.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../../../subhuti/lookahead/SubhutiTokenLookahead.ts";

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
class com_slime_java_clazz_JavaClassParser extends com_slime_java_statement_JavaStatementParser {
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_java_clazz_JavaClassParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: JavaClassParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_java_clazz_JavaClassParser_1_0(sourceCode: string): void {
    null;
  }
  classDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_classDeclaration();
    }), "classDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_classDeclaration(): any {
    this.__qin_field_tokenConsumer.CLASS();
    this.typeIdentifier();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.typeParameters();
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.EXTENDS();
      this.typeType();
      return null;
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.IMPLEMENTS();
      this.typeList();
      return null;
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.PERMITS();
      this.typeList();
      return null;
    }));
    this.classBody();
    return null;
  }
  interfaceDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_interfaceDeclaration();
    }), "interfaceDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceDeclaration(): any {
    this.__qin_field_tokenConsumer.INTERFACE();
    this.typeIdentifier();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.typeParameters();
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.EXTENDS();
      this.typeList();
      return null;
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.PERMITS();
      this.typeList();
      return null;
    }));
    this.interfaceBody();
    return null;
  }
  enumDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_enumDeclaration();
    }), "enumDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_enumDeclaration(): any {
    this.__qin_field_tokenConsumer.ENUM();
    this.typeIdentifier();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.IMPLEMENTS();
      this.typeList();
      return null;
    }));
    this.__qin_field_tokenConsumer.LBRACE();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.enumConstants();
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.COMMA();
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.enumBodyDeclarations();
    }));
    this.__qin_field_tokenConsumer.RBRACE();
    return null;
  }
  recordDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_recordDeclaration();
    }), "recordDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_recordDeclaration(): any {
    this.__qin_field_tokenConsumer.RECORD();
    this.typeIdentifier();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.typeParameters();
    }));
    this.recordHeader();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.IMPLEMENTS();
      this.typeList();
      return null;
    }));
    this.recordBody();
    return null;
  }
  annotationTypeDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_annotationTypeDeclaration();
    }), "annotationTypeDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationTypeDeclaration(): any {
    this.__qin_field_tokenConsumer.AT();
    this.__qin_field_tokenConsumer.INTERFACE();
    this.typeIdentifier();
    this.annotationTypeBody();
    return null;
  }
  classBody(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_classBody();
    }), "classBody", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_classBody(): any {
    this.__qin_field_tokenConsumer.LBRACE();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.classBodyDeclaration();
    }));
    this.__qin_field_tokenConsumer.RBRACE();
    return null;
  }
  classBodyDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_classBodyDeclaration();
    }), "classBodyDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_classBodyDeclaration(): any {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentClassBodyDeclarationLooksLikeEmpty.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentClassBodyDeclarationLooksLikeInitializerBlock.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.STATIC();
    }));
      this.block();
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentClassBodyDeclarationLooksLikeMember.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.modifier();
    }));
      this.memberDeclaration();
      return null;
    })));
    return null;
  }
  currentClassBodyDeclarationLooksLikeEmpty(): any {
    return __QinJavaLangString.equals("SEMI", this.tokenNameAt(1.0));
  }
  currentClassBodyDeclarationLooksLikeInitializerBlock(): any {
    return (__QinJavaLangString.equals("LBRACE", this.tokenNameAt(1.0)) || (__QinJavaLangString.equals("STATIC", this.tokenNameAt(1.0)) && __QinJavaLangString.equals("LBRACE", this.tokenNameAt(2.0))));
  }
  currentClassBodyDeclarationLooksLikeMember(): any {
    return (!this.currentClassBodyDeclarationLooksLikeEmpty() && !this.currentClassBodyDeclarationLooksLikeInitializerBlock() && !__QinJavaLangString.equals("RBRACE", this.tokenNameAt(1.0)));
  }
  memberDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_memberDeclaration();
    }), "memberDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_memberDeclaration(): any {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.memberDeclarationLooksLikeRecord.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.recordDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.memberDeclarationLooksLikeGenericConstructor.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.genericConstructorDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.memberDeclarationLooksLikeConstructor.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.constructorDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.memberDeclarationLooksLikeGenericMethod.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.genericMethodDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.memberDeclarationLooksLikeMethod.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.methodDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.memberDeclarationLooksLikeField.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.fieldDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.memberDeclarationLooksLikeInterface.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.interfaceDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.memberDeclarationLooksLikeAnnotationType.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.annotationTypeDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.memberDeclarationLooksLikeClass.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.classDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.memberDeclarationLooksLikeEnum.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.enumDeclaration();
    })));
    return null;
  }
  methodDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_methodDeclaration();
    }), "methodDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_methodDeclaration(): any {
    this.typeTypeOrVoid();
    this.identifier();
    this.formalParameters();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LBRACK();
      this.__qin_field_tokenConsumer.RBRACK();
      return null;
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.THROWS();
      this.qualifiedNameList();
      return null;
    }));
    this.methodBody();
    return null;
  }
  genericMethodDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_genericMethodDeclaration();
    }), "genericMethodDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_genericMethodDeclaration(): any {
    this.typeParameters();
    this.methodDeclaration();
    return null;
  }
  constructorDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_constructorDeclaration();
    }), "constructorDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_constructorDeclaration(): any {
    this.identifier();
    this.formalParameters();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.THROWS();
      this.qualifiedNameList();
      return null;
    }));
    this.block();
    return null;
  }
  genericConstructorDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_genericConstructorDeclaration();
    }), "genericConstructorDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_genericConstructorDeclaration(): any {
    this.typeParameters();
    this.constructorDeclaration();
    return null;
  }
  fieldDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_fieldDeclaration();
    }), "fieldDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_fieldDeclaration(): any {
    this.typeType();
    this.variableDeclarators();
    this.__qin_field_tokenConsumer.SEMI();
    return null;
  }
  memberDeclarationLooksLikeMethod(): any {
    return __QinJavaLangString.equals("LPAREN", this.memberDeclarationTokenAfterTypeName());
  }
  memberDeclarationLooksLikeField(): any {
    return (__qin_binary__("!=", this.memberDeclarationTokenAfterTypeName(), null) && !__QinJavaLangString.equals("LPAREN", this.memberDeclarationTokenAfterTypeName()));
  }
  memberDeclarationLooksLikeRecord(): any {
    return __QinJavaLangString.equals("RECORD", this.tokenNameAt(1.0));
  }
  memberDeclarationLooksLikeInterface(): any {
    return __QinJavaLangString.equals("INTERFACE", this.tokenNameAt(1.0));
  }
  memberDeclarationLooksLikeAnnotationType(): any {
    return (__QinJavaLangString.equals("AT", this.tokenNameAt(1.0)) && __QinJavaLangString.equals("INTERFACE", this.tokenNameAt(2.0)));
  }
  memberDeclarationLooksLikeClass(): any {
    return __QinJavaLangString.equals("CLASS", this.tokenNameAt(1.0));
  }
  memberDeclarationLooksLikeEnum(): any {
    return __QinJavaLangString.equals("ENUM", this.tokenNameAt(1.0));
  }
  memberDeclarationLooksLikeConstructor(): any {
    return (this.isIdentifierToken(this.LA(1.0)) && __QinJavaLangString.equals("LPAREN", this.tokenNameAt(2.0)));
  }
  memberDeclarationLooksLikeGenericConstructor(): any {
    return (__qin_binary__(">", this.memberDeclarationTypeParametersEndOffset(), 0.0) && this.isIdentifierToken(this.LA(this.memberDeclarationTypeParametersEndOffset())) && __QinJavaLangString.equals("LPAREN", this.tokenNameAt(__qin_binary__("+", this.memberDeclarationTypeParametersEndOffset(), 1.0))));
  }
  memberDeclarationLooksLikeGenericMethod(): any {
    let offset: any = this.memberDeclarationTypeParametersEndOffset();
    if (__qin_binary__("<", offset, 1.0)) {
      return false;
    }
    if (__QinJavaLangString.equals("VOID", this.tokenNameAt(offset))) {
      return (this.isIdentifierToken(this.LA(__qin_binary__("+", offset, 1.0))) && __QinJavaLangString.equals("LPAREN", this.tokenNameAt(__qin_binary__("+", offset, 2.0))));
    }
    let nameOffset: any = this.consumeTypeLookaheadPrefix(offset);
    return (__qin_binary__(">", nameOffset, 0.0) && this.isIdentifierToken(this.LA(nameOffset)) && __QinJavaLangString.equals("LPAREN", this.tokenNameAt(__qin_binary__("+", nameOffset, 1.0))));
  }
  memberDeclarationTypeParametersEndOffset(): any {
    if ((!__QinJavaLangString.equals("LT", this.tokenNameAt(1.0)))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    return this.skipBalancedTypeArguments(1.0);
  }
  memberDeclarationTokenAfterTypeName(): any {
    let nameOffset: any = this.memberDeclarationNameOffsetAfterType();
    if (__qin_binary__("<", nameOffset, 1.0)) {
      return null;
    }
    let afterName: any = this.LA(__qin_binary__("+", nameOffset, 1.0));
    return (__qin_binary__("==", afterName, null) ? null : afterName.getTokenName());
  }
  memberDeclarationNameOffsetAfterType(): any {
    let first: any = this.LA(1.0);
    if ((__qin_binary__("==", first, null) || first.isEof())) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    if (__QinJavaLangString.equals("VOID", first.getTokenName())) {
      return (this.isIdentifierToken(this.LA(2.0)) ? 2.0 : __qin_binary__("-", 0.0, 1.0));
    }
    let offset: any = this.consumeTypeLookaheadPrefix(1.0);
    return ((__qin_binary__(">", offset, 0.0) && this.isIdentifierToken(this.LA(offset))) ? offset : __qin_binary__("-", 0.0, 1.0));
  }
  consumeTypeLookaheadPrefix(offset: number): any {
    let token: any = this.LA(offset);
    if ((!this.isTypeStartToken(token))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    offset++;
    while (true) {
      let current: any = this.LA(offset);
      if ((__qin_binary__("==", current, null) || current.isEof())) {
        return __qin_binary__("-", 0.0, 1.0);
      }
      let tokenName: any = current.getTokenName();
      if (__QinJavaLangString.equals("DOT", tokenName)) {
        if ((!this.isTypeIdentifierToken(this.LA(__qin_binary__("+", offset, 1.0))))) {
          return __qin_binary__("-", 0.0, 1.0);
        }
        offset += 2.0;
        continue;
      }
      if (__QinJavaLangString.equals("LT", tokenName)) {
        offset = this.skipBalancedTypeArguments(offset);
        if (__qin_binary__("<", offset, 1.0)) {
          return __qin_binary__("-", 0.0, 1.0);
        }
        continue;
      }
      if ((__QinJavaLangString.equals("LBRACK", tokenName) && __QinJavaLangString.equals("RBRACK", this.tokenNameAt(__qin_binary__("+", offset, 1.0))))) {
        offset += 2.0;
        continue;
      }
      return offset;
    }
    return null;
  }
  skipBalancedTypeArguments(offset: number): any {
    let depth: any = 0.0;
    while (true) {
      let token: any = this.LA(offset);
      if ((__qin_binary__("==", token, null) || token.isEof())) {
        return __qin_binary__("-", 0.0, 1.0);
      }
      let tokenName: any = token.getTokenName();
      if (__QinJavaLangString.equals("LT", tokenName)) {
        depth++;
      } else {
        if (__QinJavaLangString.equals("GT", tokenName)) {
          depth--;
          if (__qin_binary__("==", depth, 0.0)) {
            return __qin_binary__("+", offset, 1.0);
          }
        }
      }
      offset++;
    }
    return null;
  }
  tokenNameAt(offset: number): any {
    return (__qin_binary__("==", this.LA(offset), null) ? null : this.LA(offset).getTokenName());
  }
  isTypeStartToken(token: com_subhuti_struct_SubhutiMatchToken): any {
    if ((__qin_binary__("==", token, null) || token.isEof())) {
      return false;
    }
    let tokenName: any = token.getTokenName();
    return (this.isTypeIdentifierToken(token) || __QinJavaLangString.equals("BOOLEAN", tokenName) || __QinJavaLangString.equals("BYTE", tokenName) || __QinJavaLangString.equals("CHAR", tokenName) || __QinJavaLangString.equals("DOUBLE", tokenName) || __QinJavaLangString.equals("FLOAT", tokenName) || __QinJavaLangString.equals("INT", tokenName) || __QinJavaLangString.equals("LONG", tokenName) || __QinJavaLangString.equals("SHORT", tokenName));
  }
  isIdentifierToken(token: com_subhuti_struct_SubhutiMatchToken): any {
    if ((__qin_binary__("==", token, null) || token.isEof())) {
      return false;
    }
    let tokenName: any = token.getTokenName();
    return (__QinJavaLangString.equals("IDENTIFIER", tokenName) || __QinJavaLangString.equals("MODULE", tokenName) || __QinJavaLangString.equals("OPEN", tokenName) || __QinJavaLangString.equals("REQUIRES", tokenName) || __QinJavaLangString.equals("EXPORTS", tokenName) || __QinJavaLangString.equals("OPENS", tokenName) || __QinJavaLangString.equals("TO", tokenName) || __QinJavaLangString.equals("USES", tokenName) || __QinJavaLangString.equals("PROVIDES", tokenName) || __QinJavaLangString.equals("WITH", tokenName) || __QinJavaLangString.equals("TRANSITIVE", tokenName) || __QinJavaLangString.equals("SEALED", tokenName) || __QinJavaLangString.equals("PERMITS", tokenName) || __QinJavaLangString.equals("RECORD", tokenName) || __QinJavaLangString.equals("VAR", tokenName) || __QinJavaLangString.equals("YIELD", tokenName) || __QinJavaLangString.equals("WHEN", tokenName));
  }
  isTypeIdentifierToken(token: com_subhuti_struct_SubhutiMatchToken): any {
    if ((__qin_binary__("==", token, null) || token.isEof())) {
      return false;
    }
    let tokenName: any = token.getTokenName();
    return (__QinJavaLangString.equals("IDENTIFIER", tokenName) || __QinJavaLangString.equals("MODULE", tokenName) || __QinJavaLangString.equals("OPEN", tokenName) || __QinJavaLangString.equals("REQUIRES", tokenName) || __QinJavaLangString.equals("EXPORTS", tokenName) || __QinJavaLangString.equals("OPENS", tokenName) || __QinJavaLangString.equals("TO", tokenName) || __QinJavaLangString.equals("USES", tokenName) || __QinJavaLangString.equals("PROVIDES", tokenName) || __QinJavaLangString.equals("WITH", tokenName) || __QinJavaLangString.equals("TRANSITIVE", tokenName) || __QinJavaLangString.equals("SEALED", tokenName) || __QinJavaLangString.equals("PERMITS", tokenName) || __QinJavaLangString.equals("WHEN", tokenName));
  }
  interfaceBody(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_interfaceBody();
    }), "interfaceBody", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceBody(): any {
    this.__qin_field_tokenConsumer.LBRACE();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.interfaceBodyDeclaration();
    }));
    this.__qin_field_tokenConsumer.RBRACE();
    return null;
  }
  interfaceBodyDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_interfaceBodyDeclaration();
    }), "interfaceBodyDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceBodyDeclaration(): any {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.interfaceModifier();
    }));
      this.interfaceMemberDeclaration();
      return null;
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
    return null;
  }
  interfaceModifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_interfaceModifier();
    }), "interfaceModifier", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceModifier(): any {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.modifier();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.DEFAULT();
    }));
    return null;
  }
  interfaceMemberDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_interfaceMemberDeclaration();
    }), "interfaceMemberDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceMemberDeclaration(): any {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.interfaceMemberDeclarationLooksLikeRecord.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.recordDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.interfaceMemberDeclarationLooksLikeConst.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.constDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.interfaceMemberDeclarationLooksLikeGenericMethod.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.genericInterfaceMethodDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.interfaceMemberDeclarationLooksLikeMethod.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.interfaceMethodDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.interfaceMemberDeclarationLooksLikeInterface.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.interfaceDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.interfaceMemberDeclarationLooksLikeAnnotationType.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.annotationTypeDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.interfaceMemberDeclarationLooksLikeClass.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.classDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.interfaceMemberDeclarationLooksLikeEnum.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.enumDeclaration();
    })));
    return null;
  }
  interfaceMemberDeclarationLooksLikeRecord(): any {
    return __QinJavaLangString.equals("RECORD", this.tokenNameAt(1.0));
  }
  interfaceMemberDeclarationLooksLikeInterface(): any {
    return __QinJavaLangString.equals("INTERFACE", this.tokenNameAt(1.0));
  }
  interfaceMemberDeclarationLooksLikeAnnotationType(): any {
    return (__QinJavaLangString.equals("AT", this.tokenNameAt(1.0)) && __QinJavaLangString.equals("INTERFACE", this.tokenNameAt(2.0)));
  }
  interfaceMemberDeclarationLooksLikeClass(): any {
    return __QinJavaLangString.equals("CLASS", this.tokenNameAt(1.0));
  }
  interfaceMemberDeclarationLooksLikeEnum(): any {
    return __QinJavaLangString.equals("ENUM", this.tokenNameAt(1.0));
  }
  interfaceMemberDeclarationLooksLikeGenericMethod(): any {
    let offset: any = this.memberDeclarationTypeParametersEndOffset();
    if (__qin_binary__("<", offset, 1.0)) {
      return false;
    }
    return this.interfaceCommonBodyLooksLikeMethodAt(offset);
  }
  interfaceMemberDeclarationLooksLikeMethod(): any {
    return this.interfaceCommonBodyLooksLikeMethodAt(1.0);
  }
  interfaceMemberDeclarationLooksLikeConst(): any {
    return (__qin_binary__("!=", this.interfaceMemberDeclarationTokenAfterTypeName(), null) && !__QinJavaLangString.equals("LPAREN", this.interfaceMemberDeclarationTokenAfterTypeName()));
  }
  interfaceCommonBodyLooksLikeMethodAt(offset: number): any {
    if (__QinJavaLangString.equals("VOID", this.tokenNameAt(offset))) {
      return (this.isIdentifierToken(this.LA(__qin_binary__("+", offset, 1.0))) && __QinJavaLangString.equals("LPAREN", this.tokenNameAt(__qin_binary__("+", offset, 2.0))));
    }
    let nameOffset: any = this.consumeTypeLookaheadPrefix(offset);
    return (__qin_binary__(">", nameOffset, 0.0) && this.isIdentifierToken(this.LA(nameOffset)) && __QinJavaLangString.equals("LPAREN", this.tokenNameAt(__qin_binary__("+", nameOffset, 1.0))));
  }
  interfaceMemberDeclarationTokenAfterTypeName(): any {
    let nameOffset: any = this.interfaceMemberDeclarationNameOffsetAfterType();
    if (__qin_binary__("<", nameOffset, 1.0)) {
      return null;
    }
    let afterName: any = this.LA(__qin_binary__("+", nameOffset, 1.0));
    return (__qin_binary__("==", afterName, null) ? null : afterName.getTokenName());
  }
  interfaceMemberDeclarationNameOffsetAfterType(): any {
    let first: any = this.LA(1.0);
    if ((__qin_binary__("==", first, null) || first.isEof() || __QinJavaLangString.equals("VOID", first.getTokenName()))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    let offset: any = this.consumeTypeLookaheadPrefix(1.0);
    return ((__qin_binary__(">", offset, 0.0) && this.isIdentifierToken(this.LA(offset))) ? offset : __qin_binary__("-", 0.0, 1.0));
  }
  constDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_constDeclaration();
    }), "constDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_constDeclaration(): any {
    this.typeType();
    this.constantDeclarator();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.COMMA();
      this.constantDeclarator();
      return null;
    }));
    this.__qin_field_tokenConsumer.SEMI();
    return null;
  }
  constantDeclarator(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_constantDeclarator();
    }), "constantDeclarator", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_constantDeclarator(): any {
    this.identifier();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LBRACK();
      this.__qin_field_tokenConsumer.RBRACK();
      return null;
    }));
    this.__qin_field_tokenConsumer.ASSIGN();
    this.variableInitializer();
    return null;
  }
  interfaceMethodDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_interfaceMethodDeclaration();
    }), "interfaceMethodDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceMethodDeclaration(): any {
    this.interfaceCommonBodyDeclaration();
    return null;
  }
  genericInterfaceMethodDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_genericInterfaceMethodDeclaration();
    }), "genericInterfaceMethodDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_genericInterfaceMethodDeclaration(): any {
    this.typeParameters();
    this.interfaceCommonBodyDeclaration();
    return null;
  }
  interfaceCommonBodyDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_interfaceCommonBodyDeclaration();
    }), "interfaceCommonBodyDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceCommonBodyDeclaration(): any {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.annotation();
    }));
    this.typeTypeOrVoid();
    this.identifier();
    this.formalParameters();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LBRACK();
      this.__qin_field_tokenConsumer.RBRACK();
      return null;
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.THROWS();
      this.qualifiedNameList();
      return null;
    }));
    this.methodBody();
    return null;
  }
  variableDeclarators(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_variableDeclarators();
    }), "variableDeclarators", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_variableDeclarators(): any {
    this.variableDeclarator();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.COMMA();
      this.variableDeclarator();
      return null;
    }));
    return null;
  }
  variableDeclarator(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_variableDeclarator();
    }), "variableDeclarator", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_variableDeclarator(): any {
    this.variableDeclaratorId();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.ASSIGN();
      this.variableInitializer();
      return null;
    }));
    return null;
  }
  variableDeclaratorId(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_variableDeclaratorId();
    }), "variableDeclaratorId", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_variableDeclaratorId(): any {
    this.identifier();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LBRACK();
      this.__qin_field_tokenConsumer.RBRACK();
      return null;
    }));
    return null;
  }
  variableModifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_variableModifier();
    }), "variableModifier", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_variableModifier(): any {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.FINAL();
    }), __qin_java_functional(() => {
      return this.annotation();
    }));
    return null;
  }
  modifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_modifier();
    }), "modifier", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_modifier(): any {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.classOrInterfaceModifier();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.NATIVE();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SYNCHRONIZED();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.TRANSIENT();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.VOLATILE();
    }));
    return null;
  }
  classOrInterfaceModifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_classOrInterfaceModifier();
    }), "classOrInterfaceModifier", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_classOrInterfaceModifier(): any {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentClassOrInterfaceModifierLooksLikeAnnotation.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.annotation();
    })), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.PUBLIC();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.PROTECTED();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.PRIVATE();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.STATIC();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.ABSTRACT();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.FINAL();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.STRICTFP();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEALED();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.NON_SEALED();
    }));
    return null;
  }
  currentClassOrInterfaceModifierLooksLikeAnnotation(): any {
    return (__QinJavaLangString.equals("AT", this.tokenNameAt(1.0)) && !__QinJavaLangString.equals("INTERFACE", this.tokenNameAt(2.0)));
  }
  methodBody(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_methodBody();
    }), "methodBody", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_methodBody(): any {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.block();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
    return null;
  }
  formalParameters(): any {
    throw new Error("Abstract Java method is not implemented: formalParameters");
  }
  annotation(): any {
    throw new Error("Abstract Java method is not implemented: annotation");
  }
  enumConstants(): any {
    throw new Error("Abstract Java method is not implemented: enumConstants");
  }
  enumBodyDeclarations(): any {
    throw new Error("Abstract Java method is not implemented: enumBodyDeclarations");
  }
  recordHeader(): any {
    throw new Error("Abstract Java method is not implemented: recordHeader");
  }
  recordBody(): any {
    throw new Error("Abstract Java method is not implemented: recordBody");
  }
  annotationTypeBody(): any {
    throw new Error("Abstract Java method is not implemented: annotationTypeBody");
  }
  switchBlockStatementGroup(): any {
    throw new Error("Abstract Java method is not implemented: switchBlockStatementGroup");
  }
  switchLabel(): any {
    throw new Error("Abstract Java method is not implemented: switchLabel");
  }
  __qin_arguments(): any {
    throw new Error("Abstract Java method is not implemented: arguments");
  }
  nonWildcardTypeArguments(): any {
    throw new Error("Abstract Java method is not implemented: nonWildcardTypeArguments");
  }
  explicitGenericInvocationSuffix(): any {
    throw new Error("Abstract Java method is not implemented: explicitGenericInvocationSuffix");
  }
  explicitGenericInvocation(): any {
    throw new Error("Abstract Java method is not implemented: explicitGenericInvocation");
  }
  innerCreator(): any {
    throw new Error("Abstract Java method is not implemented: innerCreator");
  }
  superSuffix(): any {
    throw new Error("Abstract Java method is not implemented: superSuffix");
  }
  pattern(): any {
    throw new Error("Abstract Java method is not implemented: pattern");
  }
}
const JavaClassParser = com_slime_java_clazz_JavaClassParser;

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_java_clazz_JavaClassParser };
