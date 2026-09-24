import { com_slime_java_clazz_JavaClassParser, com_slime_java_clazz_JavaClassParser as JavaClassParser } from "./clazz/JavaClassParser.ts";
import { com_subhuti_struct_SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken as SubhutiMatchToken, com_subhuti_struct_SubhutiMatchToken$Builder } from "../../subhuti/struct/SubhutiMatchToken.ts";
import { com_slime_java_statement_JavaStatementParser, com_slime_java_statement_JavaStatementParser as JavaStatementParser } from "./statement/JavaStatementParser.ts";
import { com_slime_java_expression_JavaExpressionParser, com_slime_java_expression_JavaExpressionParser as JavaExpressionParser } from "./expression/JavaExpressionParser.ts";
import { com_slime_java_type_JavaTypeParser, com_slime_java_type_JavaTypeParser as JavaTypeParser } from "./type/JavaTypeParser.ts";
import { com_slime_java_literal_JavaLiteralParser, com_slime_java_literal_JavaLiteralParser as JavaLiteralParser } from "./literal/JavaLiteralParser.ts";
import { com_slime_java_identifier_JavaIdentifierParser, com_slime_java_identifier_JavaIdentifierParser as JavaIdentifierParser } from "./identifier/JavaIdentifierParser.ts";
import { com_slime_java_base_JavaParserBase, com_slime_java_base_JavaParserBase as JavaParserBase } from "./base/JavaParserBase.ts";
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
class com_slime_java_JavaParser extends com_slime_java_clazz_JavaClassParser {
  constructor(...__qin_args: any[]) {
    if (__qin_args.length === 1 && (__qin_args[0] === null || typeof __qin_args[0] === "string")) {
      const sourceCode: any = __qin_args[0];
      super(sourceCode);
      this.__qin_constructor_com_slime_java_JavaParser_1_0(sourceCode);
      return;
    }
    throw new Error("Unsupported Java constructor overload: JavaParser/" + __qin_args.length);
  }
  __qin_constructor_com_slime_java_JavaParser_1_0(sourceCode: string): void {
    null;
  }
  compilationUnit(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_compilationUnit receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_compilationUnit.call(this);
    }), "compilationUnit", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_compilationUnit(): void {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.moduleDeclaration();
    }), __qin_java_functional(() => {
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.packageDeclaration();
    }));
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.importDeclaration();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
    }));
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.typeDeclaration();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
    }));
      return null;
    }));
    return null;
  }
  packageDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_packageDeclaration receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_packageDeclaration.call(this);
    }), "packageDeclaration", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_packageDeclaration(): void {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.annotation();
    }));
    {
      const __qin_typed_receiver_2212: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2212.PACKAGE();
    }
    {
      const __qin_typed_receiver_2213: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2213.qualifiedName();
    }
    {
      const __qin_typed_receiver_2214: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2214.SEMI();
    }
    return null;
  }
  importDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_importDeclaration receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_importDeclaration.call(this);
    }), "importDeclaration", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_importDeclaration(): void {
    {
      const __qin_typed_receiver_2215: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2215.IMPORT();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.STATIC();
    }));
    {
      const __qin_typed_receiver_2216: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2216.importQualifiedName();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2217: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2217.DOT();
      }
      {
        const __qin_typed_receiver_2218: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2218.MUL();
      }
      return null;
    }));
    {
      const __qin_typed_receiver_2219: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2219.SEMI();
    }
    return null;
  }
  importQualifiedName(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_importQualifiedName receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_importQualifiedName.call(this);
    }), "importQualifiedName", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_importQualifiedName(): void {
    {
      const __qin_typed_receiver_2220: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2220.identifier();
    }
    SubhutiCompileOnlyDsl.Many(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentImportQualifiedNameContinuesWithIdentifierSegment receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentImportQualifiedNameContinuesWithIdentifierSegment.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2221: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2221.DOT();
      }
      {
        const __qin_typed_receiver_2222: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2222.identifier();
      }
      return null;
    })));
    return null;
  }
  currentImportQualifiedNameContinuesWithIdentifierSegment(): boolean {
    return (__QinJavaLangString.equals("DOT", this.tokenNameAt(1.0)) && !__QinJavaLangString.equals("MUL", this.tokenNameAt(2.0)));
  }
  importQualifiedNameSegment(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_importQualifiedNameSegment receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_importQualifiedNameSegment.call(this);
    }), "importQualifiedNameSegment", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_importQualifiedNameSegment(): void {
    if ((!this.match("DOT") || this.lookahead("MUL", 2.0))) {
      {
        const __qin_typed_receiver_2223: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2223.setParseFail();
      }
      return null;
    }
    {
      const __qin_typed_receiver_2224: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2224.DOT();
    }
    {
      const __qin_typed_receiver_2225: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2225.identifier();
    }
    return null;
  }
  typeDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_typeDeclaration receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_typeDeclaration.call(this);
    }), "typeDeclaration", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_typeDeclaration(): void {
    SubhutiCompileOnlyDsl.Many(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentTypeDeclarationLooksLikeClassOrInterfaceModifier receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentTypeDeclarationLooksLikeClassOrInterfaceModifier.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.classOrInterfaceModifier();
    })));
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.classDeclaration();
    }), __qin_java_functional(() => {
      return this.enumDeclaration();
    }), __qin_java_functional(() => {
      return this.interfaceDeclaration();
    }), __qin_java_functional(() => {
      return this.annotationTypeDeclaration();
    }), __qin_java_functional(() => {
      return this.recordDeclaration();
    }));
    return null;
  }
  currentTypeDeclarationLooksLikeClassOrInterfaceModifier(): boolean {
    return (!__QinJavaLangString.equals("AT", this.tokenNameAt(1.0)) || !__QinJavaLangString.equals("INTERFACE", this.tokenNameAt(2.0)));
  }
  formalParameters(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_formalParameters receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_formalParameters.call(this);
    }), "formalParameters", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_formalParameters(): void {
    {
      const __qin_typed_receiver_2226: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2226.LPAREN();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentFormalParametersStartFormalList receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentFormalParametersStartFormalList.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.formalParameterList();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentFormalParametersStartReceiver receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentFormalParametersStartReceiver.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2227: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2227.receiverParameter();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2228: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2228.COMMA();
      }
      {
        const __qin_typed_receiver_2229: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2229.formalParameterList();
      }
      return null;
    }));
      return null;
    })));
    }));
    {
      const __qin_typed_receiver_2230: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2230.RPAREN();
    }
    return null;
  }
  currentFormalParametersStartReceiver(): boolean {
    let afterTypeOffset: number = this.formalParameterTypeTailOffset(1.0);
    if (__qin_binary__("<", afterTypeOffset, 1.0)) {
      return false;
    }
    let tokenName: string = this.tokenNameAt(afterTypeOffset);
    if (__QinJavaLangString.equals("THIS", tokenName)) {
      return true;
    }
    return (__QinJavaLangString.equals("IDENTIFIER", tokenName) && __QinJavaLangString.equals("DOT", this.tokenNameAt(__qin_binary__("+", afterTypeOffset, 1.0))) && __QinJavaLangString.equals("THIS", this.tokenNameAt(__qin_binary__("+", afterTypeOffset, 2.0))));
  }
  currentFormalParametersStartFormalList(): boolean {
    return (!this.match("RPAREN") && !this.currentFormalParametersStartReceiver());
  }
  receiverParameter(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_receiverParameter receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_receiverParameter.call(this);
    }), "receiverParameter", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_receiverParameter(): void {
    {
      const __qin_typed_receiver_2231: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2231.typeType();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2232: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2232.identifier();
      }
      {
        const __qin_typed_receiver_2233: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2233.DOT();
      }
      return null;
    }));
    {
      const __qin_typed_receiver_2234: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2234.THIS();
    }
    return null;
  }
  formalParameterList(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_formalParameterList receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_formalParameterList.call(this);
    }), "formalParameterList", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_formalParameterList(): void {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentParameterIsNotVarargs receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentParameterIsNotVarargs.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2235: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2235.formalParameter();
      }
      SubhutiCompileOnlyDsl.Many(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=commaFollowedByNonVarargsParameter receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.commaFollowedByNonVarargsParameter.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2236: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2236.COMMA();
      }
      {
        const __qin_typed_receiver_2237: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2237.formalParameter();
      }
      return null;
    })));
      SubhutiCompileOnlyDsl.Option(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=commaFollowedByVarargsParameter receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.commaFollowedByVarargsParameter.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2238: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2238.COMMA();
      }
      {
        const __qin_typed_receiver_2239: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2239.lastFormalParameter();
      }
      return null;
    })));
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentParameterIsVarargs receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentParameterIsVarargs.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.lastFormalParameter();
    })));
    return null;
  }
  currentParameterIsVarargs(): boolean {
    return this.parameterHasEllipsisBeforeBoundary(1.0);
  }
  currentParameterIsNotVarargs(): boolean {
    return (!this.currentParameterIsVarargs());
  }
  commaFollowedByVarargsParameter(): boolean {
    return (this.match("COMMA") && this.parameterHasEllipsisBeforeBoundary(2.0));
  }
  commaFollowedByNonVarargsParameter(): boolean {
    return (this.match("COMMA") && !this.parameterHasEllipsisBeforeBoundary(2.0));
  }
  parameterHasEllipsisBeforeBoundary(startOffset: number): boolean {
    let angleDepth: number = 0.0;
    let parenDepth: number = 0.0;
    let bracketDepth: number = 0.0;
    for (let offset: number = startOffset; __qin_binary__("<", offset, __qin_binary__("+", startOffset, 96.0)); offset++) {
      let token: com_subhuti_struct_SubhutiMatchToken = this.LA(offset);
      if (__qin_binary__("==", token, null)) {
        return false;
      }
      let name: string = token.getTokenName();
      if (__QinJavaLangString.equals("ELLIPSIS", name)) {
        return true;
      }
      if (__QinJavaLangString.equals("LT", name)) {
        angleDepth++;
      } else {
        if ((__QinJavaLangString.equals("GT", name) && __qin_binary__(">", angleDepth, 0.0))) {
          angleDepth--;
        } else {
          if (__QinJavaLangString.equals("LPAREN", name)) {
            parenDepth++;
          } else {
            if (__QinJavaLangString.equals("RPAREN", name)) {
              if ((__qin_binary__("==", angleDepth, 0.0) && __qin_binary__("==", parenDepth, 0.0) && __qin_binary__("==", bracketDepth, 0.0))) {
                return false;
              }
              if (__qin_binary__(">", parenDepth, 0.0)) {
                parenDepth--;
              }
            } else {
              if (__QinJavaLangString.equals("LBRACK", name)) {
                bracketDepth++;
              } else {
                if ((__QinJavaLangString.equals("RBRACK", name) && __qin_binary__(">", bracketDepth, 0.0))) {
                  bracketDepth--;
                } else {
                  if ((__QinJavaLangString.equals("COMMA", name) && __qin_binary__("==", angleDepth, 0.0) && __qin_binary__("==", parenDepth, 0.0) && __qin_binary__("==", bracketDepth, 0.0))) {
                    return false;
                  }
                }
              }
            }
          }
        }
      }
    }
    return false;
  }
  formalParameterTypeTailOffset(offset: number): number {
    let first: com_subhuti_struct_SubhutiMatchToken = this.LA(offset);
    if ((!this.isFormalParameterTypeStart(first))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    offset++;
    while (true) {
      let tokenName: string = this.tokenNameAt(offset);
      if (__QinJavaLangString.equals("DOT", tokenName)) {
        if ((!__QinJavaLangString.equals("IDENTIFIER", this.tokenNameAt(__qin_binary__("+", offset, 1.0))))) {
          return __qin_binary__("-", 0.0, 1.0);
        }
        offset += 2.0;
        continue;
      }
      if (__QinJavaLangString.equals("LT", tokenName)) {
        offset = this.skipFormalParameterTypeArguments(offset);
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
  skipFormalParameterTypeArguments(offset: number): number {
    let depth: number = 0.0;
    while (true) {
      let tokenName: string = this.tokenNameAt(offset);
      if (__qin_binary__("==", tokenName, null)) {
        return __qin_binary__("-", 0.0, 1.0);
      }
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
    return ((__qin_binary__("==", this.LA(offset), null) || this.LA(offset).isEof()) ? null : this.LA(offset).getTokenName());
  }
  isFormalParameterTypeStart(token: com_subhuti_struct_SubhutiMatchToken): boolean {
    if ((__qin_binary__("==", token, null) || token.isEof())) {
      return false;
    }
    let tokenName: string = token.getTokenName();
    return (__QinJavaLangString.equals("IDENTIFIER", tokenName) || __QinJavaLangString.equals("BOOLEAN", tokenName) || __QinJavaLangString.equals("BYTE", tokenName) || __QinJavaLangString.equals("CHAR", tokenName) || __QinJavaLangString.equals("DOUBLE", tokenName) || __QinJavaLangString.equals("FLOAT", tokenName) || __QinJavaLangString.equals("INT", tokenName) || __QinJavaLangString.equals("LONG", tokenName) || __QinJavaLangString.equals("SHORT", tokenName));
  }
  formalParameter(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_formalParameter receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_formalParameter.call(this);
    }), "formalParameter", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_formalParameter(): void {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.variableModifier();
    }));
    {
      const __qin_typed_receiver_2240: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2240.typeType();
    }
    {
      const __qin_typed_receiver_2241: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2241.variableDeclaratorId();
    }
    return null;
  }
  lastFormalParameter(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_lastFormalParameter receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_lastFormalParameter.call(this);
    }), "lastFormalParameter", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_lastFormalParameter(): void {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.variableModifier();
    }));
    {
      const __qin_typed_receiver_2242: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2242.typeType();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.annotation();
    }));
    {
      const __qin_typed_receiver_2243: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2243.ELLIPSIS();
    }
    {
      const __qin_typed_receiver_2244: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2244.variableDeclaratorId();
    }
    return null;
  }
  annotation(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_annotation receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_annotation.call(this);
    }), "annotation", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotation(): void {
    {
      const __qin_typed_receiver_2245: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2245.AT();
    }
    {
      const __qin_typed_receiver_2246: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2246.qualifiedName();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2247: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2247.LPAREN();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentAnnotationValueLooksLikePairs receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentAnnotationValueLooksLikePairs.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.elementValuePairs();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentAnnotationValueLooksLikeSingleValue receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentAnnotationValueLooksLikeSingleValue.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.elementValue();
    })));
    }));
      {
        const __qin_typed_receiver_2248: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2248.RPAREN();
      }
      return null;
    }));
    return null;
  }
  currentAnnotationValueLooksLikePairs(): boolean {
    return (this.isAnnotationElementNameToken(this.tokenNameAt(1.0)) && __QinJavaLangString.equals("ASSIGN", this.tokenNameAt(2.0)));
  }
  isAnnotationElementNameToken(tokenName: string): boolean {
    return (__QinJavaLangString.equals("IDENTIFIER", tokenName) || __QinJavaLangString.equals("MODULE", tokenName) || __QinJavaLangString.equals("OPEN", tokenName) || __QinJavaLangString.equals("REQUIRES", tokenName) || __QinJavaLangString.equals("EXPORTS", tokenName) || __QinJavaLangString.equals("OPENS", tokenName) || __QinJavaLangString.equals("TO", tokenName) || __QinJavaLangString.equals("USES", tokenName) || __QinJavaLangString.equals("PROVIDES", tokenName) || __QinJavaLangString.equals("WITH", tokenName) || __QinJavaLangString.equals("TRANSITIVE", tokenName) || __QinJavaLangString.equals("SEALED", tokenName) || __QinJavaLangString.equals("PERMITS", tokenName) || __QinJavaLangString.equals("RECORD", tokenName) || __QinJavaLangString.equals("VAR", tokenName) || __QinJavaLangString.equals("YIELD", tokenName) || __QinJavaLangString.equals("WHEN", tokenName));
  }
  currentAnnotationValueLooksLikeSingleValue(): boolean {
    return (!this.currentAnnotationValueLooksLikePairs() && !__QinJavaLangString.equals("RPAREN", this.tokenNameAt(1.0)));
  }
  elementValuePairs(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_elementValuePairs receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_elementValuePairs.call(this);
    }), "elementValuePairs", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_elementValuePairs(): void {
    {
      const __qin_typed_receiver_2249: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2249.elementValuePair();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2250: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2250.COMMA();
      }
      {
        const __qin_typed_receiver_2251: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2251.elementValuePair();
      }
      return null;
    }));
    return null;
  }
  elementValuePair(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_elementValuePair receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_elementValuePair.call(this);
    }), "elementValuePair", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_elementValuePair(): void {
    {
      const __qin_typed_receiver_2252: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2252.identifier();
    }
    {
      const __qin_typed_receiver_2253: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2253.ASSIGN();
    }
    {
      const __qin_typed_receiver_2254: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2254.elementValue();
    }
    return null;
  }
  elementValue(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_elementValue receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_elementValue.call(this);
    }), "elementValue", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_elementValue(): void {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentElementValueLooksLikeAnnotation receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentElementValueLooksLikeAnnotation.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.annotation();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentElementValueLooksLikeArrayInitializer receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentElementValueLooksLikeArrayInitializer.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.elementValueArrayInitializer();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentElementValueLooksLikeExpression receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentElementValueLooksLikeExpression.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.expression();
    })));
    return null;
  }
  currentElementValueLooksLikeAnnotation(): boolean {
    return __QinJavaLangString.equals("AT", this.tokenNameAt(1.0));
  }
  currentElementValueLooksLikeArrayInitializer(): boolean {
    return __QinJavaLangString.equals("LBRACE", this.tokenNameAt(1.0));
  }
  currentElementValueLooksLikeExpression(): boolean {
    return (!this.currentElementValueLooksLikeAnnotation() && !this.currentElementValueLooksLikeArrayInitializer());
  }
  elementValueArrayInitializer(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_elementValueArrayInitializer receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_elementValueArrayInitializer.call(this);
    }), "elementValueArrayInitializer", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_elementValueArrayInitializer(): void {
    {
      const __qin_typed_receiver_2255: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2255.LBRACE();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2256: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2256.elementValue();
      }
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2257: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2257.COMMA();
      }
      {
        const __qin_typed_receiver_2258: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2258.elementValue();
      }
      return null;
    }));
      return null;
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.COMMA();
    }));
    {
      const __qin_typed_receiver_2259: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2259.RBRACE();
    }
    return null;
  }
  annotationTypeBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_annotationTypeBody receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_annotationTypeBody.call(this);
    }), "annotationTypeBody", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationTypeBody(): void {
    {
      const __qin_typed_receiver_2260: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2260.LBRACE();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.annotationTypeElementDeclaration();
    }));
    {
      const __qin_typed_receiver_2261: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2261.RBRACE();
    }
    return null;
  }
  annotationTypeElementDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_annotationTypeElementDeclaration receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_annotationTypeElementDeclaration.call(this);
    }), "annotationTypeElementDeclaration", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationTypeElementDeclaration(): void {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.modifier();
    }));
      {
        const __qin_typed_receiver_2262: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2262.annotationTypeElementRest();
      }
      return null;
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
    return null;
  }
  annotationTypeElementRest(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_annotationTypeElementRest receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_annotationTypeElementRest.call(this);
    }), "annotationTypeElementRest", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationTypeElementRest(): void {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2263: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2263.typeType();
      }
      {
        const __qin_typed_receiver_2264: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2264.annotationMethodOrConstantRest();
      }
      {
        const __qin_typed_receiver_2265: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2265.SEMI();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2266: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2266.classDeclaration();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2267: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2267.interfaceDeclaration();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2268: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2268.enumDeclaration();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2269: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2269.annotationTypeDeclaration();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2270: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2270.recordDeclaration();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
      return null;
    }));
    return null;
  }
  annotationMethodOrConstantRest(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_annotationMethodOrConstantRest receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_annotationMethodOrConstantRest.call(this);
    }), "annotationMethodOrConstantRest", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationMethodOrConstantRest(): void {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentAnnotationMethodOrConstantRestLooksLikeMethod receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentAnnotationMethodOrConstantRestLooksLikeMethod.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.annotationMethodRest();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentAnnotationMethodOrConstantRestLooksLikeConstant receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentAnnotationMethodOrConstantRestLooksLikeConstant.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.annotationConstantRest();
    })));
    return null;
  }
  currentAnnotationMethodOrConstantRestLooksLikeMethod(): boolean {
    return __QinJavaLangString.equals("LPAREN", this.tokenNameAt(2.0));
  }
  currentAnnotationMethodOrConstantRestLooksLikeConstant(): boolean {
    return (!this.currentAnnotationMethodOrConstantRestLooksLikeMethod());
  }
  annotationMethodRest(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_annotationMethodRest receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_annotationMethodRest.call(this);
    }), "annotationMethodRest", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationMethodRest(): void {
    {
      const __qin_typed_receiver_2271: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2271.identifier();
    }
    {
      const __qin_typed_receiver_2272: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2272.LPAREN();
    }
    {
      const __qin_typed_receiver_2273: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2273.RPAREN();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.defaultValue();
    }));
    return null;
  }
  annotationConstantRest(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_annotationConstantRest receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_annotationConstantRest.call(this);
    }), "annotationConstantRest", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationConstantRest(): void {
    {
      const __qin_typed_receiver_2274: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2274.variableDeclarators();
    }
    return null;
  }
  defaultValue(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_defaultValue receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_defaultValue.call(this);
    }), "defaultValue", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_defaultValue(): void {
    {
      const __qin_typed_receiver_2275: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2275.DEFAULT();
    }
    {
      const __qin_typed_receiver_2276: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2276.elementValue();
    }
    return null;
  }
  enumConstants(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_enumConstants receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_enumConstants.call(this);
    }), "enumConstants", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_enumConstants(): void {
    {
      const __qin_typed_receiver_2277: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2277.enumConstant();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2278: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2278.COMMA();
      }
      {
        const __qin_typed_receiver_2279: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2279.enumConstant();
      }
      return null;
    }));
    return null;
  }
  enumConstant(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_enumConstant receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_enumConstant.call(this);
    }), "enumConstant", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_enumConstant(): void {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.annotation();
    }));
    {
      const __qin_typed_receiver_2280: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2280.identifier();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.arguments();
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.classBody();
    }));
    return null;
  }
  enumBodyDeclarations(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_enumBodyDeclarations receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_enumBodyDeclarations.call(this);
    }), "enumBodyDeclarations", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_enumBodyDeclarations(): void {
    {
      const __qin_typed_receiver_2281: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2281.SEMI();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.classBodyDeclaration();
    }));
    return null;
  }
  recordHeader(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_recordHeader receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_recordHeader.call(this);
    }), "recordHeader", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_recordHeader(): void {
    {
      const __qin_typed_receiver_2282: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2282.LPAREN();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.recordComponentList();
    }));
    {
      const __qin_typed_receiver_2283: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2283.RPAREN();
    }
    return null;
  }
  recordComponentList(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_recordComponentList receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_recordComponentList.call(this);
    }), "recordComponentList", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_recordComponentList(): void {
    {
      const __qin_typed_receiver_2284: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2284.recordComponent();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2285: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2285.COMMA();
      }
      {
        const __qin_typed_receiver_2286: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2286.recordComponent();
      }
      return null;
    }));
    return null;
  }
  recordComponent(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_recordComponent receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_recordComponent.call(this);
    }), "recordComponent", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_recordComponent(): void {
    {
      const __qin_typed_receiver_2287: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2287.typeType();
    }
    {
      const __qin_typed_receiver_2288: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2288.identifier();
    }
    return null;
  }
  recordBody(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_recordBody receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_recordBody.call(this);
    }), "recordBody", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_recordBody(): void {
    {
      const __qin_typed_receiver_2289: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2289.LBRACE();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentRecordBodyLooksLikeCompactConstructor receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentRecordBodyLooksLikeCompactConstructor.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.compactConstructorDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return /* @qin-static-admission member=bind owner=com.qin.lang.backend.js.QinJsBackend method=currentRecordBodyLooksLikeClassBodyDeclaration receiver=__qin_bound_receiver arity=bound */ __qin_bound_receiver.currentRecordBodyLooksLikeClassBodyDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.classBodyDeclaration();
    })));
    }));
    {
      const __qin_typed_receiver_2290: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2290.RBRACE();
    }
    return null;
  }
  currentRecordBodyLooksLikeCompactConstructor(): boolean {
    let offset: number = 1.0;
    while (this.isRecordBodyModifierToken(this.tokenNameAt(offset))) {
      offset++;
    }
    return (__QinJavaLangString.equals("IDENTIFIER", this.tokenNameAt(offset)) && __QinJavaLangString.equals("LBRACE", this.tokenNameAt(__qin_binary__("+", offset, 1.0))));
  }
  currentRecordBodyLooksLikeClassBodyDeclaration(): boolean {
    return (!__QinJavaLangString.equals("RBRACE", this.tokenNameAt(1.0)) && !this.currentRecordBodyLooksLikeCompactConstructor());
  }
  isRecordBodyModifierToken(tokenName: string): boolean {
    return (__QinJavaLangString.equals("PUBLIC", tokenName) || __QinJavaLangString.equals("PROTECTED", tokenName) || __QinJavaLangString.equals("PRIVATE", tokenName) || __QinJavaLangString.equals("STATIC", tokenName) || __QinJavaLangString.equals("ABSTRACT", tokenName) || __QinJavaLangString.equals("FINAL", tokenName) || __QinJavaLangString.equals("STRICTFP", tokenName) || __QinJavaLangString.equals("SEALED", tokenName) || __QinJavaLangString.equals("NON_SEALED", tokenName) || __QinJavaLangString.equals("NATIVE", tokenName) || __QinJavaLangString.equals("SYNCHRONIZED", tokenName) || __QinJavaLangString.equals("TRANSIENT", tokenName) || __QinJavaLangString.equals("VOLATILE", tokenName));
  }
  compactConstructorDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_compactConstructorDeclaration receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_compactConstructorDeclaration.call(this);
    }), "compactConstructorDeclaration", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_compactConstructorDeclaration(): void {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.modifier();
    }));
    {
      const __qin_typed_receiver_2291: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2291.identifier();
    }
    {
      const __qin_typed_receiver_2292: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2292.block();
    }
    return null;
  }
  switchBlockStatementGroup(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_switchBlockStatementGroup receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_switchBlockStatementGroup.call(this);
    }), "switchBlockStatementGroup", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_switchBlockStatementGroup(): void {
    SubhutiCompileOnlyDsl.AtLeastOne(__qin_java_functional(() => {
      return this.switchLabel();
    }));
    SubhutiCompileOnlyDsl.AtLeastOne(__qin_java_functional(() => {
      return this.blockStatement();
    }));
    return null;
  }
  switchLabel(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_switchLabel receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_switchLabel.call(this);
    }), "switchLabel", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_switchLabel(): void {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2293: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2293.CASE();
      }
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.pattern();
    }), __qin_java_functional(() => {
      return this.expression();
    }));
      {
        const __qin_typed_receiver_2294: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2294.COLON();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2295: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2295.DEFAULT();
      }
      {
        const __qin_typed_receiver_2296: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2296.COLON();
      }
      return null;
    }));
    return null;
  }
  pattern(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_pattern receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_pattern.call(this);
    }), "pattern", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_pattern(): void {
    {
      const __qin_typed_receiver_2297: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2297.typeType();
    }
    {
      const __qin_typed_receiver_2298: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2298.identifier();
    }
    return null;
  }
  arguments(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_arguments receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_arguments.call(this);
    }), "arguments", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_arguments(): void {
    {
      const __qin_typed_receiver_2299: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2299.LPAREN();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.expressionList();
    }));
    {
      const __qin_typed_receiver_2300: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2300.RPAREN();
    }
    return null;
  }
  nonWildcardTypeArguments(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_nonWildcardTypeArguments receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_nonWildcardTypeArguments.call(this);
    }), "nonWildcardTypeArguments", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_nonWildcardTypeArguments(): void {
    {
      const __qin_typed_receiver_2301: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2301.LT();
    }
    {
      const __qin_typed_receiver_2302: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2302.typeList();
    }
    {
      const __qin_typed_receiver_2303: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2303.GT();
    }
    return null;
  }
  explicitGenericInvocationSuffix(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_explicitGenericInvocationSuffix receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_explicitGenericInvocationSuffix.call(this);
    }), "explicitGenericInvocationSuffix", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_explicitGenericInvocationSuffix(): void {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2304: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2304.SUPER();
      }
      {
        const __qin_typed_receiver_2305: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2305.superSuffix();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2306: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2306.identifier();
      }
      {
        const __qin_typed_receiver_2307: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2307.arguments();
      }
      return null;
    }));
    return null;
  }
  explicitGenericInvocation(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_explicitGenericInvocation receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_explicitGenericInvocation.call(this);
    }), "explicitGenericInvocation", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_explicitGenericInvocation(): void {
    {
      const __qin_typed_receiver_2308: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2308.nonWildcardTypeArguments();
    }
    {
      const __qin_typed_receiver_2309: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2309.explicitGenericInvocationSuffix();
    }
    return null;
  }
  innerCreator(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_innerCreator receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_innerCreator.call(this);
    }), "innerCreator", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_innerCreator(): void {
    {
      const __qin_typed_receiver_2310: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2310.identifier();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.nonWildcardTypeArgumentsOrDiamond();
    }));
    {
      const __qin_typed_receiver_2311: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2311.classCreatorRest();
    }
    return null;
  }
  nonWildcardTypeArgumentsOrDiamond(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_nonWildcardTypeArgumentsOrDiamond receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_nonWildcardTypeArgumentsOrDiamond.call(this);
    }), "nonWildcardTypeArgumentsOrDiamond", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_nonWildcardTypeArgumentsOrDiamond(): void {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2312: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2312.LT();
      }
      {
        const __qin_typed_receiver_2313: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2313.GT();
      }
      return null;
    }), __qin_java_functional(() => {
      return this.nonWildcardTypeArguments();
    }));
    return null;
  }
  classCreatorRest(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_classCreatorRest receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_classCreatorRest.call(this);
    }), "classCreatorRest", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_classCreatorRest(): void {
    {
      const __qin_typed_receiver_2314: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2314.arguments();
    }
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.classBody();
    }));
    return null;
  }
  superSuffix(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_superSuffix receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_superSuffix.call(this);
    }), "superSuffix", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_superSuffix(): void {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.arguments();
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2315: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2315.DOT();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.typeArguments();
    }));
      {
        const __qin_typed_receiver_2316: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2316.identifier();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.arguments();
    }));
      return null;
    }));
    return null;
  }
  moduleDeclaration(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_moduleDeclaration receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_moduleDeclaration.call(this);
    }), "moduleDeclaration", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_moduleDeclaration(): void {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.annotation();
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.OPEN();
    }));
    {
      const __qin_typed_receiver_2317: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2317.MODULE();
    }
    {
      const __qin_typed_receiver_2318: com_slime_java_JavaParser = this;
      __qin_typed_receiver_2318.qualifiedName();
    }
    {
      const __qin_typed_receiver_2319: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2319.LBRACE();
    }
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.moduleDirective();
    }));
    {
      const __qin_typed_receiver_2320: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
      __qin_typed_receiver_2320.RBRACE();
    }
    return null;
  }
  moduleDirective(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_moduleDirective receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_moduleDirective.call(this);
    }), "moduleDirective", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_moduleDirective(): void {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2321: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2321.REQUIRES();
      }
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.requiresModifier();
    }));
      {
        const __qin_typed_receiver_2322: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2322.qualifiedName();
      }
      {
        const __qin_typed_receiver_2323: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2323.SEMI();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2324: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2324.EXPORTS();
      }
      {
        const __qin_typed_receiver_2325: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2325.qualifiedName();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2326: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2326.TO();
      }
      {
        const __qin_typed_receiver_2327: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2327.qualifiedName();
      }
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2328: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2328.COMMA();
      }
      {
        const __qin_typed_receiver_2329: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2329.qualifiedName();
      }
      return null;
    }));
      return null;
    }));
      {
        const __qin_typed_receiver_2330: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2330.SEMI();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2331: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2331.OPENS();
      }
      {
        const __qin_typed_receiver_2332: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2332.qualifiedName();
      }
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2333: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2333.TO();
      }
      {
        const __qin_typed_receiver_2334: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2334.qualifiedName();
      }
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2335: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2335.COMMA();
      }
      {
        const __qin_typed_receiver_2336: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2336.qualifiedName();
      }
      return null;
    }));
      return null;
    }));
      {
        const __qin_typed_receiver_2337: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2337.SEMI();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2338: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2338.USES();
      }
      {
        const __qin_typed_receiver_2339: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2339.qualifiedName();
      }
      {
        const __qin_typed_receiver_2340: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2340.SEMI();
      }
      return null;
    }), __qin_java_functional(() => {
      {
        const __qin_typed_receiver_2341: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2341.PROVIDES();
      }
      {
        const __qin_typed_receiver_2342: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2342.qualifiedName();
      }
      {
        const __qin_typed_receiver_2343: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2343.WITH();
      }
      {
        const __qin_typed_receiver_2344: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2344.qualifiedName();
      }
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      {
        const __qin_typed_receiver_2345: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2345.COMMA();
      }
      {
        const __qin_typed_receiver_2346: com_slime_java_JavaParser = this;
        __qin_typed_receiver_2346.qualifiedName();
      }
      return null;
    }));
      {
        const __qin_typed_receiver_2347: com_slime_java_JavaTokenConsumer = this.__qin_field_tokenConsumer;
        __qin_typed_receiver_2347.SEMI();
      }
      return null;
    }));
    return null;
  }
  requiresModifier(): void {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return /* @qin-static-admission member=call owner=com.slime.java.JavaParser method=__qin_subhuti_raw_requiresModifier receiver=this arity=0 */ com_slime_java_JavaParser.prototype.__qin_subhuti_raw_requiresModifier.call(this);
    }), "requiresModifier", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_requiresModifier(): void {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.TRANSITIVE();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.STATIC();
    }));
    return null;
  }
}
const JavaParser = com_slime_java_JavaParser;

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_java_JavaParser };
