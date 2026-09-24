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
  classDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_classDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_classDeclaration.call(this);
    }), "classDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_classDeclaration(): void {
    {
      const __qin_typed_receiver_3004: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3004.CLASS();
    }
    {
      const __qin_typed_receiver_3005: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3005.typeIdentifier();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.typeParameters();
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3006: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3006.EXTENDS();
      }
      {
        const __qin_typed_receiver_3007: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3007.typeType();
      }
      return null;
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3008: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3008.IMPLEMENTS();
      }
      {
        const __qin_typed_receiver_3009: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3009.typeList();
      }
      return null;
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3010: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3010.PERMITS();
      }
      {
        const __qin_typed_receiver_3011: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3011.typeList();
      }
      return null;
    }));
    {
      const __qin_typed_receiver_3012: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3012.classBody();
    }
    return null;
  }
  interfaceDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_interfaceDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_interfaceDeclaration.call(this);
    }), "interfaceDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceDeclaration(): void {
    {
      const __qin_typed_receiver_3013: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3013.INTERFACE();
    }
    {
      const __qin_typed_receiver_3014: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3014.typeIdentifier();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.typeParameters();
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3015: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3015.EXTENDS();
      }
      {
        const __qin_typed_receiver_3016: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3016.typeList();
      }
      return null;
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3017: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3017.PERMITS();
      }
      {
        const __qin_typed_receiver_3018: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3018.typeList();
      }
      return null;
    }));
    {
      const __qin_typed_receiver_3019: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3019.interfaceBody();
    }
    return null;
  }
  enumDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_enumDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_enumDeclaration.call(this);
    }), "enumDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_enumDeclaration(): void {
    {
      const __qin_typed_receiver_3020: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3020.ENUM();
    }
    {
      const __qin_typed_receiver_3021: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3021.typeIdentifier();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3022: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3022.IMPLEMENTS();
      }
      {
        const __qin_typed_receiver_3023: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3023.typeList();
      }
      return null;
    }));
    {
      const __qin_typed_receiver_3024: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3024.LBRACE();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.enumConstants();
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.COMMA();
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.enumBodyDeclarations();
    }));
    {
      const __qin_typed_receiver_3025: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3025.RBRACE();
    }
    return null;
  }
  recordDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_recordDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_recordDeclaration.call(this);
    }), "recordDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_recordDeclaration(): void {
    {
      const __qin_typed_receiver_3026: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3026.RECORD();
    }
    {
      const __qin_typed_receiver_3027: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3027.typeIdentifier();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.typeParameters();
    }));
    {
      const __qin_typed_receiver_3028: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3028.recordHeader();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3029: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3029.IMPLEMENTS();
      }
      {
        const __qin_typed_receiver_3030: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3030.typeList();
      }
      return null;
    }));
    {
      const __qin_typed_receiver_3031: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3031.recordBody();
    }
    return null;
  }
  annotationTypeDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_annotationTypeDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_annotationTypeDeclaration.call(this);
    }), "annotationTypeDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationTypeDeclaration(): void {
    {
      const __qin_typed_receiver_3032: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3032.AT();
    }
    {
      const __qin_typed_receiver_3033: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3033.INTERFACE();
    }
    {
      const __qin_typed_receiver_3034: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3034.typeIdentifier();
    }
    {
      const __qin_typed_receiver_3035: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3035.annotationTypeBody();
    }
    return null;
  }
  classBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_classBody receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_classBody.call(this);
    }), "classBody", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_classBody(): void {
    {
      const __qin_typed_receiver_3036: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3036.LBRACE();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.classBodyDeclaration();
    }));
    {
      const __qin_typed_receiver_3037: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3037.RBRACE();
    }
    return null;
  }
  classBodyDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_classBodyDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_classBodyDeclaration.call(this);
    }), "classBodyDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_classBodyDeclaration(): void {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentClassBodyDeclarationLooksLikeEmpty receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentClassBodyDeclarationLooksLikeEmpty.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentClassBodyDeclarationLooksLikeInitializerBlock receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentClassBodyDeclarationLooksLikeInitializerBlock.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.STATIC();
    }));
      {
        const __qin_typed_receiver_3038: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3038.block();
      }
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentClassBodyDeclarationLooksLikeMember receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentClassBodyDeclarationLooksLikeMember.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.modifier();
    }));
      {
        const __qin_typed_receiver_3039: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3039.memberDeclaration();
      }
      return null;
    })));
    return null;
  }
  currentClassBodyDeclarationLooksLikeEmpty(): boolean {
    return __QinJavaLangString.equals("SEMI", this.tokenNameAt(1.0));
  }
  currentClassBodyDeclarationLooksLikeInitializerBlock(): boolean {
    return (__QinJavaLangString.equals("LBRACE", this.tokenNameAt(1.0)) || (__QinJavaLangString.equals("STATIC", this.tokenNameAt(1.0)) && __QinJavaLangString.equals("LBRACE", this.tokenNameAt(2.0))));
  }
  currentClassBodyDeclarationLooksLikeMember(): boolean {
    return (!this.currentClassBodyDeclarationLooksLikeEmpty() && !this.currentClassBodyDeclarationLooksLikeInitializerBlock() && !__QinJavaLangString.equals("RBRACE", this.tokenNameAt(1.0)));
  }
  memberDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_memberDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_memberDeclaration.call(this);
    }), "memberDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_memberDeclaration(): void {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=memberDeclarationLooksLikeRecord receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.memberDeclarationLooksLikeRecord.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.recordDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=memberDeclarationLooksLikeGenericConstructor receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.memberDeclarationLooksLikeGenericConstructor.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.genericConstructorDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=memberDeclarationLooksLikeConstructor receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.memberDeclarationLooksLikeConstructor.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.constructorDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=memberDeclarationLooksLikeGenericMethod receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.memberDeclarationLooksLikeGenericMethod.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.genericMethodDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=memberDeclarationLooksLikeMethod receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.memberDeclarationLooksLikeMethod.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.methodDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=memberDeclarationLooksLikeField receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.memberDeclarationLooksLikeField.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.fieldDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=memberDeclarationLooksLikeInterface receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.memberDeclarationLooksLikeInterface.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.interfaceDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=memberDeclarationLooksLikeAnnotationType receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.memberDeclarationLooksLikeAnnotationType.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.annotationTypeDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=memberDeclarationLooksLikeClass receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.memberDeclarationLooksLikeClass.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.classDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=memberDeclarationLooksLikeEnum receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.memberDeclarationLooksLikeEnum.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.enumDeclaration();
    })));
    return null;
  }
  methodDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_methodDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_methodDeclaration.call(this);
    }), "methodDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_methodDeclaration(): void {
    {
      const __qin_typed_receiver_3040: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3040.typeTypeOrVoid();
    }
    {
      const __qin_typed_receiver_3041: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3041.identifier();
    }
    {
      const __qin_typed_receiver_3042: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3042.formalParameters();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3043: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3043.LBRACK();
      }
      {
        const __qin_typed_receiver_3044: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3044.RBRACK();
      }
      return null;
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3045: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3045.THROWS();
      }
      {
        const __qin_typed_receiver_3046: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3046.qualifiedNameList();
      }
      return null;
    }));
    {
      const __qin_typed_receiver_3047: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3047.methodBody();
    }
    return null;
  }
  genericMethodDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_genericMethodDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_genericMethodDeclaration.call(this);
    }), "genericMethodDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_genericMethodDeclaration(): void {
    {
      const __qin_typed_receiver_3048: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3048.typeParameters();
    }
    {
      const __qin_typed_receiver_3049: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3049.methodDeclaration();
    }
    return null;
  }
  constructorDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_constructorDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_constructorDeclaration.call(this);
    }), "constructorDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_constructorDeclaration(): void {
    {
      const __qin_typed_receiver_3050: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3050.identifier();
    }
    {
      const __qin_typed_receiver_3051: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3051.formalParameters();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3052: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3052.THROWS();
      }
      {
        const __qin_typed_receiver_3053: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3053.qualifiedNameList();
      }
      return null;
    }));
    {
      const __qin_typed_receiver_3054: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3054.block();
    }
    return null;
  }
  genericConstructorDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_genericConstructorDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_genericConstructorDeclaration.call(this);
    }), "genericConstructorDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_genericConstructorDeclaration(): void {
    {
      const __qin_typed_receiver_3055: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3055.typeParameters();
    }
    {
      const __qin_typed_receiver_3056: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3056.constructorDeclaration();
    }
    return null;
  }
  fieldDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_fieldDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_fieldDeclaration.call(this);
    }), "fieldDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_fieldDeclaration(): void {
    {
      const __qin_typed_receiver_3057: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3057.typeType();
    }
    {
      const __qin_typed_receiver_3058: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3058.variableDeclarators();
    }
    {
      const __qin_typed_receiver_3059: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3059.SEMI();
    }
    return null;
  }
  memberDeclarationLooksLikeMethod(): boolean {
    return __QinJavaLangString.equals("LPAREN", this.memberDeclarationTokenAfterTypeName());
  }
  memberDeclarationLooksLikeField(): boolean {
    return (__qin_binary__("!=", this.memberDeclarationTokenAfterTypeName(), null) && !__QinJavaLangString.equals("LPAREN", this.memberDeclarationTokenAfterTypeName()));
  }
  memberDeclarationLooksLikeRecord(): boolean {
    return __QinJavaLangString.equals("RECORD", this.tokenNameAt(1.0));
  }
  memberDeclarationLooksLikeInterface(): boolean {
    return __QinJavaLangString.equals("INTERFACE", this.tokenNameAt(1.0));
  }
  memberDeclarationLooksLikeAnnotationType(): boolean {
    return (__QinJavaLangString.equals("AT", this.tokenNameAt(1.0)) && __QinJavaLangString.equals("INTERFACE", this.tokenNameAt(2.0)));
  }
  memberDeclarationLooksLikeClass(): boolean {
    return __QinJavaLangString.equals("CLASS", this.tokenNameAt(1.0));
  }
  memberDeclarationLooksLikeEnum(): boolean {
    return __QinJavaLangString.equals("ENUM", this.tokenNameAt(1.0));
  }
  memberDeclarationLooksLikeConstructor(): boolean {
    return (this.isIdentifierToken(this.LA(1.0)) && __QinJavaLangString.equals("LPAREN", this.tokenNameAt(2.0)));
  }
  memberDeclarationLooksLikeGenericConstructor(): boolean {
    return (__qin_binary__(">", this.memberDeclarationTypeParametersEndOffset(), 0.0) && this.isIdentifierToken(this.LA(this.memberDeclarationTypeParametersEndOffset())) && __QinJavaLangString.equals("LPAREN", this.tokenNameAt(__qin_binary__("+", this.memberDeclarationTypeParametersEndOffset(), 1.0))));
  }
  memberDeclarationLooksLikeGenericMethod(): boolean {
    let offset: number = this.memberDeclarationTypeParametersEndOffset();
    if (__qin_binary__("<", offset, 1.0)) {
      return false;
    }
    if (__QinJavaLangString.equals("VOID", this.tokenNameAt(offset))) {
      return (this.isIdentifierToken(this.LA(__qin_binary__("+", offset, 1.0))) && __QinJavaLangString.equals("LPAREN", this.tokenNameAt(__qin_binary__("+", offset, 2.0))));
    }
    let nameOffset: number = this.consumeTypeLookaheadPrefix(offset);
    return (__qin_binary__(">", nameOffset, 0.0) && this.isIdentifierToken(this.LA(nameOffset)) && __QinJavaLangString.equals("LPAREN", this.tokenNameAt(__qin_binary__("+", nameOffset, 1.0))));
  }
  memberDeclarationTypeParametersEndOffset(): number {
    if ((!__QinJavaLangString.equals("LT", this.tokenNameAt(1.0)))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    return this.skipBalancedTypeArguments(1.0);
  }
  memberDeclarationTokenAfterTypeName(): string {
    let nameOffset: number = this.memberDeclarationNameOffsetAfterType();
    if (__qin_binary__("<", nameOffset, 1.0)) {
      return null;
    }
    let afterName: com_subhuti_struct_SubhutiMatchToken = this.LA(__qin_binary__("+", nameOffset, 1.0));
    return (__qin_binary__("==", afterName, null) ? null : afterName.getTokenName());
  }
  memberDeclarationNameOffsetAfterType(): number {
    let first: com_subhuti_struct_SubhutiMatchToken = this.LA(1.0);
    if ((__qin_binary__("==", first, null) || first.isEof())) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    if (__QinJavaLangString.equals("VOID", first.getTokenName())) {
      return (this.isIdentifierToken(this.LA(2.0)) ? 2.0 : __qin_binary__("-", 0.0, 1.0));
    }
    let offset: number = this.consumeTypeLookaheadPrefix(1.0);
    return ((__qin_binary__(">", offset, 0.0) && this.isIdentifierToken(this.LA(offset))) ? offset : __qin_binary__("-", 0.0, 1.0));
  }
  consumeTypeLookaheadPrefix(offset: number): number {
    let token: com_subhuti_struct_SubhutiMatchToken = this.LA(offset);
    if ((!this.isTypeStartToken(token))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    offset++;
    while (true) {
      let current: com_subhuti_struct_SubhutiMatchToken = this.LA(offset);
      if ((__qin_binary__("==", current, null) || current.isEof())) {
        return __qin_binary__("-", 0.0, 1.0);
      }
      let tokenName: string = current.getTokenName();
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
  skipBalancedTypeArguments(offset: number): number {
    let depth: number = 0.0;
    while (true) {
      let token: com_subhuti_struct_SubhutiMatchToken = this.LA(offset);
      if ((__qin_binary__("==", token, null) || token.isEof())) {
        return __qin_binary__("-", 0.0, 1.0);
      }
      let tokenName: string = token.getTokenName();
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
  tokenNameAt(offset: number): string {
    return (__qin_binary__("==", this.LA(offset), null) ? null : this.LA(offset).getTokenName());
  }
  isTypeStartToken(token: com_subhuti_struct_SubhutiMatchToken): boolean {
    if ((__qin_binary__("==", token, null) || token.isEof())) {
      return false;
    }
    let tokenName: string = token.getTokenName();
    return (this.isTypeIdentifierToken(token) || __QinJavaLangString.equals("BOOLEAN", tokenName) || __QinJavaLangString.equals("BYTE", tokenName) || __QinJavaLangString.equals("CHAR", tokenName) || __QinJavaLangString.equals("DOUBLE", tokenName) || __QinJavaLangString.equals("FLOAT", tokenName) || __QinJavaLangString.equals("INT", tokenName) || __QinJavaLangString.equals("LONG", tokenName) || __QinJavaLangString.equals("SHORT", tokenName));
  }
  isIdentifierToken(token: com_subhuti_struct_SubhutiMatchToken): boolean {
    if ((__qin_binary__("==", token, null) || token.isEof())) {
      return false;
    }
    let tokenName: string = token.getTokenName();
    return (__QinJavaLangString.equals("IDENTIFIER", tokenName) || __QinJavaLangString.equals("MODULE", tokenName) || __QinJavaLangString.equals("OPEN", tokenName) || __QinJavaLangString.equals("REQUIRES", tokenName) || __QinJavaLangString.equals("EXPORTS", tokenName) || __QinJavaLangString.equals("OPENS", tokenName) || __QinJavaLangString.equals("TO", tokenName) || __QinJavaLangString.equals("USES", tokenName) || __QinJavaLangString.equals("PROVIDES", tokenName) || __QinJavaLangString.equals("WITH", tokenName) || __QinJavaLangString.equals("TRANSITIVE", tokenName) || __QinJavaLangString.equals("SEALED", tokenName) || __QinJavaLangString.equals("PERMITS", tokenName) || __QinJavaLangString.equals("RECORD", tokenName) || __QinJavaLangString.equals("VAR", tokenName) || __QinJavaLangString.equals("YIELD", tokenName) || __QinJavaLangString.equals("WHEN", tokenName));
  }
  isTypeIdentifierToken(token: com_subhuti_struct_SubhutiMatchToken): boolean {
    if ((__qin_binary__("==", token, null) || token.isEof())) {
      return false;
    }
    let tokenName: string = token.getTokenName();
    return (__QinJavaLangString.equals("IDENTIFIER", tokenName) || __QinJavaLangString.equals("MODULE", tokenName) || __QinJavaLangString.equals("OPEN", tokenName) || __QinJavaLangString.equals("REQUIRES", tokenName) || __QinJavaLangString.equals("EXPORTS", tokenName) || __QinJavaLangString.equals("OPENS", tokenName) || __QinJavaLangString.equals("TO", tokenName) || __QinJavaLangString.equals("USES", tokenName) || __QinJavaLangString.equals("PROVIDES", tokenName) || __QinJavaLangString.equals("WITH", tokenName) || __QinJavaLangString.equals("TRANSITIVE", tokenName) || __QinJavaLangString.equals("SEALED", tokenName) || __QinJavaLangString.equals("PERMITS", tokenName) || __QinJavaLangString.equals("WHEN", tokenName));
  }
  interfaceBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_interfaceBody receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_interfaceBody.call(this);
    }), "interfaceBody", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceBody(): void {
    {
      const __qin_typed_receiver_3060: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3060.LBRACE();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.interfaceBodyDeclaration();
    }));
    {
      const __qin_typed_receiver_3061: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3061.RBRACE();
    }
    return null;
  }
  interfaceBodyDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_interfaceBodyDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_interfaceBodyDeclaration.call(this);
    }), "interfaceBodyDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceBodyDeclaration(): void {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.interfaceModifier();
    }));
      {
        const __qin_typed_receiver_3062: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3062.interfaceMemberDeclaration();
      }
      return null;
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
    return null;
  }
  interfaceModifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_interfaceModifier receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_interfaceModifier.call(this);
    }), "interfaceModifier", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceModifier(): void {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.modifier();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.DEFAULT();
    }));
    return null;
  }
  interfaceMemberDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_interfaceMemberDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_interfaceMemberDeclaration.call(this);
    }), "interfaceMemberDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceMemberDeclaration(): void {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=interfaceMemberDeclarationLooksLikeRecord receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.interfaceMemberDeclarationLooksLikeRecord.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.recordDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=interfaceMemberDeclarationLooksLikeConst receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.interfaceMemberDeclarationLooksLikeConst.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.constDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=interfaceMemberDeclarationLooksLikeGenericMethod receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.interfaceMemberDeclarationLooksLikeGenericMethod.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.genericInterfaceMethodDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=interfaceMemberDeclarationLooksLikeMethod receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.interfaceMemberDeclarationLooksLikeMethod.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.interfaceMethodDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=interfaceMemberDeclarationLooksLikeInterface receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.interfaceMemberDeclarationLooksLikeInterface.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.interfaceDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=interfaceMemberDeclarationLooksLikeAnnotationType receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.interfaceMemberDeclarationLooksLikeAnnotationType.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.annotationTypeDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=interfaceMemberDeclarationLooksLikeClass receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.interfaceMemberDeclarationLooksLikeClass.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.classDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=interfaceMemberDeclarationLooksLikeEnum receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.interfaceMemberDeclarationLooksLikeEnum.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.enumDeclaration();
    })));
    return null;
  }
  interfaceMemberDeclarationLooksLikeRecord(): boolean {
    return __QinJavaLangString.equals("RECORD", this.tokenNameAt(1.0));
  }
  interfaceMemberDeclarationLooksLikeInterface(): boolean {
    return __QinJavaLangString.equals("INTERFACE", this.tokenNameAt(1.0));
  }
  interfaceMemberDeclarationLooksLikeAnnotationType(): boolean {
    return (__QinJavaLangString.equals("AT", this.tokenNameAt(1.0)) && __QinJavaLangString.equals("INTERFACE", this.tokenNameAt(2.0)));
  }
  interfaceMemberDeclarationLooksLikeClass(): boolean {
    return __QinJavaLangString.equals("CLASS", this.tokenNameAt(1.0));
  }
  interfaceMemberDeclarationLooksLikeEnum(): boolean {
    return __QinJavaLangString.equals("ENUM", this.tokenNameAt(1.0));
  }
  interfaceMemberDeclarationLooksLikeGenericMethod(): boolean {
    let offset: number = this.memberDeclarationTypeParametersEndOffset();
    if (__qin_binary__("<", offset, 1.0)) {
      return false;
    }
    return this.interfaceCommonBodyLooksLikeMethodAt(offset);
  }
  interfaceMemberDeclarationLooksLikeMethod(): boolean {
    return this.interfaceCommonBodyLooksLikeMethodAt(1.0);
  }
  interfaceMemberDeclarationLooksLikeConst(): boolean {
    return (__qin_binary__("!=", this.interfaceMemberDeclarationTokenAfterTypeName(), null) && !__QinJavaLangString.equals("LPAREN", this.interfaceMemberDeclarationTokenAfterTypeName()));
  }
  interfaceCommonBodyLooksLikeMethodAt(offset: number): boolean {
    if (__QinJavaLangString.equals("VOID", this.tokenNameAt(offset))) {
      return (this.isIdentifierToken(this.LA(__qin_binary__("+", offset, 1.0))) && __QinJavaLangString.equals("LPAREN", this.tokenNameAt(__qin_binary__("+", offset, 2.0))));
    }
    let nameOffset: number = this.consumeTypeLookaheadPrefix(offset);
    return (__qin_binary__(">", nameOffset, 0.0) && this.isIdentifierToken(this.LA(nameOffset)) && __QinJavaLangString.equals("LPAREN", this.tokenNameAt(__qin_binary__("+", nameOffset, 1.0))));
  }
  interfaceMemberDeclarationTokenAfterTypeName(): string {
    let nameOffset: number = this.interfaceMemberDeclarationNameOffsetAfterType();
    if (__qin_binary__("<", nameOffset, 1.0)) {
      return null;
    }
    let afterName: com_subhuti_struct_SubhutiMatchToken = this.LA(__qin_binary__("+", nameOffset, 1.0));
    return (__qin_binary__("==", afterName, null) ? null : afterName.getTokenName());
  }
  interfaceMemberDeclarationNameOffsetAfterType(): number {
    let first: com_subhuti_struct_SubhutiMatchToken = this.LA(1.0);
    if ((__qin_binary__("==", first, null) || first.isEof() || __QinJavaLangString.equals("VOID", first.getTokenName()))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    let offset: number = this.consumeTypeLookaheadPrefix(1.0);
    return ((__qin_binary__(">", offset, 0.0) && this.isIdentifierToken(this.LA(offset))) ? offset : __qin_binary__("-", 0.0, 1.0));
  }
  constDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_constDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_constDeclaration.call(this);
    }), "constDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_constDeclaration(): void {
    {
      const __qin_typed_receiver_3063: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3063.typeType();
    }
    {
      const __qin_typed_receiver_3064: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3064.constantDeclarator();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3065: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3065.COMMA();
      }
      {
        const __qin_typed_receiver_3066: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3066.constantDeclarator();
      }
      return null;
    }));
    {
      const __qin_typed_receiver_3067: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3067.SEMI();
    }
    return null;
  }
  constantDeclarator(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_constantDeclarator receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_constantDeclarator.call(this);
    }), "constantDeclarator", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_constantDeclarator(): void {
    {
      const __qin_typed_receiver_3068: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3068.identifier();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3069: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3069.LBRACK();
      }
      {
        const __qin_typed_receiver_3070: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3070.RBRACK();
      }
      return null;
    }));
    {
      const __qin_typed_receiver_3071: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_3071.ASSIGN();
    }
    {
      const __qin_typed_receiver_3072: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3072.variableInitializer();
    }
    return null;
  }
  interfaceMethodDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_interfaceMethodDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_interfaceMethodDeclaration.call(this);
    }), "interfaceMethodDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceMethodDeclaration(): void {
    {
      const __qin_typed_receiver_3073: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3073.interfaceCommonBodyDeclaration();
    }
    return null;
  }
  genericInterfaceMethodDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_genericInterfaceMethodDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_genericInterfaceMethodDeclaration.call(this);
    }), "genericInterfaceMethodDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_genericInterfaceMethodDeclaration(): void {
    {
      const __qin_typed_receiver_3074: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3074.typeParameters();
    }
    {
      const __qin_typed_receiver_3075: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3075.interfaceCommonBodyDeclaration();
    }
    return null;
  }
  interfaceCommonBodyDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_interfaceCommonBodyDeclaration receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_interfaceCommonBodyDeclaration.call(this);
    }), "interfaceCommonBodyDeclaration", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_interfaceCommonBodyDeclaration(): void {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.annotation();
    }));
    {
      const __qin_typed_receiver_3076: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3076.typeTypeOrVoid();
    }
    {
      const __qin_typed_receiver_3077: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3077.identifier();
    }
    {
      const __qin_typed_receiver_3078: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3078.formalParameters();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3079: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3079.LBRACK();
      }
      {
        const __qin_typed_receiver_3080: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3080.RBRACK();
      }
      return null;
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3081: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3081.THROWS();
      }
      {
        const __qin_typed_receiver_3082: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3082.qualifiedNameList();
      }
      return null;
    }));
    {
      const __qin_typed_receiver_3083: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3083.methodBody();
    }
    return null;
  }
  variableDeclarators(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_variableDeclarators receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_variableDeclarators.call(this);
    }), "variableDeclarators", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_variableDeclarators(): void {
    {
      const __qin_typed_receiver_3084: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3084.variableDeclarator();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3085: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3085.COMMA();
      }
      {
        const __qin_typed_receiver_3086: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3086.variableDeclarator();
      }
      return null;
    }));
    return null;
  }
  variableDeclarator(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_variableDeclarator receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_variableDeclarator.call(this);
    }), "variableDeclarator", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_variableDeclarator(): void {
    {
      const __qin_typed_receiver_3087: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3087.variableDeclaratorId();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3088: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3088.ASSIGN();
      }
      {
        const __qin_typed_receiver_3089: com_slime_java_clazz_JavaClassParser = this;
        __qin_typed_receiver_3089.variableInitializer();
      }
      return null;
    }));
    return null;
  }
  variableDeclaratorId(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_variableDeclaratorId receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_variableDeclaratorId.call(this);
    }), "variableDeclaratorId", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_variableDeclaratorId(): void {
    {
      const __qin_typed_receiver_3090: com_slime_java_clazz_JavaClassParser = this;
      __qin_typed_receiver_3090.identifier();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_3091: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3091.LBRACK();
      }
      {
        const __qin_typed_receiver_3092: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_3092.RBRACK();
      }
      return null;
    }));
    return null;
  }
  variableModifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_variableModifier receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_variableModifier.call(this);
    }), "variableModifier", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_variableModifier(): void {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.FINAL();
    }), __qin_java_functional(() => {
      return this.annotation();
    }));
    return null;
  }
  modifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_modifier receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_modifier.call(this);
    }), "modifier", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_modifier(): void {
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
  classOrInterfaceModifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_classOrInterfaceModifier receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_classOrInterfaceModifier.call(this);
    }), "classOrInterfaceModifier", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_classOrInterfaceModifier(): void {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentClassOrInterfaceModifierLooksLikeAnnotation receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentClassOrInterfaceModifierLooksLikeAnnotation.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
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
  currentClassOrInterfaceModifierLooksLikeAnnotation(): boolean {
    return (__QinJavaLangString.equals("AT", this.tokenNameAt(1.0)) && !__QinJavaLangString.equals("INTERFACE", this.tokenNameAt(2.0)));
  }
  methodBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.clazz.JavaClassParser method=__qin_subhuti_raw_methodBody receiver=this arity=0 */ com_slime_java_clazz_JavaClassParser.prototype.__qin_subhuti_raw_methodBody.call(this);
    }), "methodBody", "JavaClassParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_methodBody(): void {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.block();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
    return null;
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
