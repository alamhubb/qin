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
  compilationUnit(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_compilationUnit();
    }), "compilationUnit", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_compilationUnit(): any {
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
  packageDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_packageDeclaration();
    }), "packageDeclaration", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_packageDeclaration(): any {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.annotation();
    }));
    this.__qin_field_tokenConsumer.PACKAGE();
    this.qualifiedName();
    this.__qin_field_tokenConsumer.SEMI();
    return null;
  }
  importDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_importDeclaration();
    }), "importDeclaration", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_importDeclaration(): any {
    this.__qin_field_tokenConsumer.IMPORT();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.STATIC();
    }));
    this.importQualifiedName();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.DOT();
      this.__qin_field_tokenConsumer.MUL();
      return null;
    }));
    this.__qin_field_tokenConsumer.SEMI();
    return null;
  }
  importQualifiedName(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_importQualifiedName();
    }), "importQualifiedName", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_importQualifiedName(): any {
    this.identifier();
    SubhutiCompileOnlyDsl.Many(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentImportQualifiedNameContinuesWithIdentifierSegment.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.DOT();
      this.identifier();
      return null;
    })));
    return null;
  }
  currentImportQualifiedNameContinuesWithIdentifierSegment(): any {
    return (__QinJavaLangString.equals("DOT", this.tokenNameAt(1.0)) && !__QinJavaLangString.equals("MUL", this.tokenNameAt(2.0)));
  }
  importQualifiedNameSegment(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_importQualifiedNameSegment();
    }), "importQualifiedNameSegment", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_importQualifiedNameSegment(): any {
    if ((!this.match("DOT") || this.lookahead("MUL", 2.0))) {
      this.setParseFail();
      return null;
    }
    this.__qin_field_tokenConsumer.DOT();
    this.identifier();
    return null;
  }
  typeDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_typeDeclaration();
    }), "typeDeclaration", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_typeDeclaration(): any {
    SubhutiCompileOnlyDsl.Many(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentTypeDeclarationLooksLikeClassOrInterfaceModifier.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
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
  currentTypeDeclarationLooksLikeClassOrInterfaceModifier(): any {
    return (!__QinJavaLangString.equals("AT", this.tokenNameAt(1.0)) || !__QinJavaLangString.equals("INTERFACE", this.tokenNameAt(2.0)));
  }
  formalParameters(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_formalParameters();
    }), "formalParameters", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_formalParameters(): any {
    this.__qin_field_tokenConsumer.LPAREN();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentFormalParametersStartFormalList.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.formalParameterList();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentFormalParametersStartReceiver.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      this.receiverParameter();
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.COMMA();
      this.formalParameterList();
      return null;
    }));
      return null;
    })));
    }));
    this.__qin_field_tokenConsumer.RPAREN();
    return null;
  }
  currentFormalParametersStartReceiver(): any {
    let afterTypeOffset: any = this.formalParameterTypeTailOffset(1.0);
    if (__qin_binary__("<", afterTypeOffset, 1.0)) {
      return false;
    }
    let tokenName: any = this.tokenNameAt(afterTypeOffset);
    if (__QinJavaLangString.equals("THIS", tokenName)) {
      return true;
    }
    return (__QinJavaLangString.equals("IDENTIFIER", tokenName) && __QinJavaLangString.equals("DOT", this.tokenNameAt(__qin_binary__("+", afterTypeOffset, 1.0))) && __QinJavaLangString.equals("THIS", this.tokenNameAt(__qin_binary__("+", afterTypeOffset, 2.0))));
  }
  currentFormalParametersStartFormalList(): any {
    return (!this.match("RPAREN") && !this.currentFormalParametersStartReceiver());
  }
  receiverParameter(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_receiverParameter();
    }), "receiverParameter", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_receiverParameter(): any {
    this.typeType();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.identifier();
      this.__qin_field_tokenConsumer.DOT();
      return null;
    }));
    this.__qin_field_tokenConsumer.THIS();
    return null;
  }
  formalParameterList(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_formalParameterList();
    }), "formalParameterList", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_formalParameterList(): any {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentParameterIsNotVarargs.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      this.formalParameter();
      SubhutiCompileOnlyDsl.Many(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.commaFollowedByNonVarargsParameter.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.COMMA();
      this.formalParameter();
      return null;
    })));
      SubhutiCompileOnlyDsl.Option(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.commaFollowedByVarargsParameter.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.COMMA();
      this.lastFormalParameter();
      return null;
    })));
      return null;
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentParameterIsVarargs.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.lastFormalParameter();
    })));
    return null;
  }
  currentParameterIsVarargs(): any {
    return this.parameterHasEllipsisBeforeBoundary(1.0);
  }
  currentParameterIsNotVarargs(): any {
    return (!this.currentParameterIsVarargs());
  }
  commaFollowedByVarargsParameter(): any {
    return (this.match("COMMA") && this.parameterHasEllipsisBeforeBoundary(2.0));
  }
  commaFollowedByNonVarargsParameter(): any {
    return (this.match("COMMA") && !this.parameterHasEllipsisBeforeBoundary(2.0));
  }
  parameterHasEllipsisBeforeBoundary(startOffset: number): any {
    let angleDepth: any = 0.0;
    let parenDepth: any = 0.0;
    let bracketDepth: any = 0.0;
    for (let offset: any = startOffset; __qin_binary__("<", offset, __qin_binary__("+", startOffset, 96.0)); offset++) {
      let token: any = this.LA(offset);
      if (__qin_binary__("==", token, null)) {
        return false;
      }
      let name: any = token.getTokenName();
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
  formalParameterTypeTailOffset(offset: number): any {
    let first: any = this.LA(offset);
    if ((!this.isFormalParameterTypeStart(first))) {
      return __qin_binary__("-", 0.0, 1.0);
    }
    offset++;
    while (true) {
      let tokenName: any = this.tokenNameAt(offset);
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
  skipFormalParameterTypeArguments(offset: number): any {
    let depth: any = 0.0;
    while (true) {
      let tokenName: any = this.tokenNameAt(offset);
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
  tokenNameAt(offset: number): any {
    return ((__qin_binary__("==", this.LA(offset), null) || this.LA(offset).isEof()) ? null : this.LA(offset).getTokenName());
  }
  isFormalParameterTypeStart(token: com_subhuti_struct_SubhutiMatchToken): any {
    if ((__qin_binary__("==", token, null) || token.isEof())) {
      return false;
    }
    let tokenName: any = token.getTokenName();
    return (__QinJavaLangString.equals("IDENTIFIER", tokenName) || __QinJavaLangString.equals("BOOLEAN", tokenName) || __QinJavaLangString.equals("BYTE", tokenName) || __QinJavaLangString.equals("CHAR", tokenName) || __QinJavaLangString.equals("DOUBLE", tokenName) || __QinJavaLangString.equals("FLOAT", tokenName) || __QinJavaLangString.equals("INT", tokenName) || __QinJavaLangString.equals("LONG", tokenName) || __QinJavaLangString.equals("SHORT", tokenName));
  }
  formalParameter(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_formalParameter();
    }), "formalParameter", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_formalParameter(): any {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.variableModifier();
    }));
    this.typeType();
    this.variableDeclaratorId();
    return null;
  }
  lastFormalParameter(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_lastFormalParameter();
    }), "lastFormalParameter", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_lastFormalParameter(): any {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.variableModifier();
    }));
    this.typeType();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.annotation();
    }));
    this.__qin_field_tokenConsumer.ELLIPSIS();
    this.variableDeclaratorId();
    return null;
  }
  annotation(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_annotation();
    }), "annotation", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotation(): any {
    this.__qin_field_tokenConsumer.AT();
    this.qualifiedName();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LPAREN();
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentAnnotationValueLooksLikePairs.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.elementValuePairs();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentAnnotationValueLooksLikeSingleValue.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.elementValue();
    })));
    }));
      this.__qin_field_tokenConsumer.RPAREN();
      return null;
    }));
    return null;
  }
  currentAnnotationValueLooksLikePairs(): any {
    return (this.isAnnotationElementNameToken(this.tokenNameAt(1.0)) && __QinJavaLangString.equals("ASSIGN", this.tokenNameAt(2.0)));
  }
  isAnnotationElementNameToken(tokenName: string): any {
    return (__QinJavaLangString.equals("IDENTIFIER", tokenName) || __QinJavaLangString.equals("MODULE", tokenName) || __QinJavaLangString.equals("OPEN", tokenName) || __QinJavaLangString.equals("REQUIRES", tokenName) || __QinJavaLangString.equals("EXPORTS", tokenName) || __QinJavaLangString.equals("OPENS", tokenName) || __QinJavaLangString.equals("TO", tokenName) || __QinJavaLangString.equals("USES", tokenName) || __QinJavaLangString.equals("PROVIDES", tokenName) || __QinJavaLangString.equals("WITH", tokenName) || __QinJavaLangString.equals("TRANSITIVE", tokenName) || __QinJavaLangString.equals("SEALED", tokenName) || __QinJavaLangString.equals("PERMITS", tokenName) || __QinJavaLangString.equals("RECORD", tokenName) || __QinJavaLangString.equals("VAR", tokenName) || __QinJavaLangString.equals("YIELD", tokenName) || __QinJavaLangString.equals("WHEN", tokenName));
  }
  currentAnnotationValueLooksLikeSingleValue(): any {
    return (!this.currentAnnotationValueLooksLikePairs() && !__QinJavaLangString.equals("RPAREN", this.tokenNameAt(1.0)));
  }
  elementValuePairs(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_elementValuePairs();
    }), "elementValuePairs", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_elementValuePairs(): any {
    this.elementValuePair();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.COMMA();
      this.elementValuePair();
      return null;
    }));
    return null;
  }
  elementValuePair(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_elementValuePair();
    }), "elementValuePair", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_elementValuePair(): any {
    this.identifier();
    this.__qin_field_tokenConsumer.ASSIGN();
    this.elementValue();
    return null;
  }
  elementValue(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_elementValue();
    }), "elementValue", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_elementValue(): any {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentElementValueLooksLikeAnnotation.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.annotation();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentElementValueLooksLikeArrayInitializer.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.elementValueArrayInitializer();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentElementValueLooksLikeExpression.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.expression();
    })));
    return null;
  }
  currentElementValueLooksLikeAnnotation(): any {
    return __QinJavaLangString.equals("AT", this.tokenNameAt(1.0));
  }
  currentElementValueLooksLikeArrayInitializer(): any {
    return __QinJavaLangString.equals("LBRACE", this.tokenNameAt(1.0));
  }
  currentElementValueLooksLikeExpression(): any {
    return (!this.currentElementValueLooksLikeAnnotation() && !this.currentElementValueLooksLikeArrayInitializer());
  }
  elementValueArrayInitializer(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_elementValueArrayInitializer();
    }), "elementValueArrayInitializer", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_elementValueArrayInitializer(): any {
    this.__qin_field_tokenConsumer.LBRACE();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.elementValue();
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.COMMA();
      this.elementValue();
      return null;
    }));
      return null;
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.COMMA();
    }));
    this.__qin_field_tokenConsumer.RBRACE();
    return null;
  }
  annotationTypeBody(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_annotationTypeBody();
    }), "annotationTypeBody", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationTypeBody(): any {
    this.__qin_field_tokenConsumer.LBRACE();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.annotationTypeElementDeclaration();
    }));
    this.__qin_field_tokenConsumer.RBRACE();
    return null;
  }
  annotationTypeElementDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_annotationTypeElementDeclaration();
    }), "annotationTypeElementDeclaration", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationTypeElementDeclaration(): any {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.modifier();
    }));
      this.annotationTypeElementRest();
      return null;
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
    return null;
  }
  annotationTypeElementRest(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_annotationTypeElementRest();
    }), "annotationTypeElementRest", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationTypeElementRest(): any {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      this.typeType();
      this.annotationMethodOrConstantRest();
      this.__qin_field_tokenConsumer.SEMI();
      return null;
    }), __qin_java_functional(() => {
      this.classDeclaration();
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
      return null;
    }), __qin_java_functional(() => {
      this.interfaceDeclaration();
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
      return null;
    }), __qin_java_functional(() => {
      this.enumDeclaration();
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
      return null;
    }), __qin_java_functional(() => {
      this.annotationTypeDeclaration();
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
      return null;
    }), __qin_java_functional(() => {
      this.recordDeclaration();
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.SEMI();
    }));
      return null;
    }));
    return null;
  }
  annotationMethodOrConstantRest(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_annotationMethodOrConstantRest();
    }), "annotationMethodOrConstantRest", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationMethodOrConstantRest(): any {
    SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentAnnotationMethodOrConstantRestLooksLikeMethod.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.annotationMethodRest();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentAnnotationMethodOrConstantRestLooksLikeConstant.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.annotationConstantRest();
    })));
    return null;
  }
  currentAnnotationMethodOrConstantRestLooksLikeMethod(): any {
    return __QinJavaLangString.equals("LPAREN", this.tokenNameAt(2.0));
  }
  currentAnnotationMethodOrConstantRestLooksLikeConstant(): any {
    return (!this.currentAnnotationMethodOrConstantRestLooksLikeMethod());
  }
  annotationMethodRest(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_annotationMethodRest();
    }), "annotationMethodRest", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationMethodRest(): any {
    this.identifier();
    this.__qin_field_tokenConsumer.LPAREN();
    this.__qin_field_tokenConsumer.RPAREN();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.defaultValue();
    }));
    return null;
  }
  annotationConstantRest(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_annotationConstantRest();
    }), "annotationConstantRest", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_annotationConstantRest(): any {
    this.variableDeclarators();
    return null;
  }
  defaultValue(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_defaultValue();
    }), "defaultValue", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_defaultValue(): any {
    this.__qin_field_tokenConsumer.DEFAULT();
    this.elementValue();
    return null;
  }
  enumConstants(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_enumConstants();
    }), "enumConstants", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_enumConstants(): any {
    this.enumConstant();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.COMMA();
      this.enumConstant();
      return null;
    }));
    return null;
  }
  enumConstant(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_enumConstant();
    }), "enumConstant", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_enumConstant(): any {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.annotation();
    }));
    this.identifier();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_arguments();
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.classBody();
    }));
    return null;
  }
  enumBodyDeclarations(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_enumBodyDeclarations();
    }), "enumBodyDeclarations", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_enumBodyDeclarations(): any {
    this.__qin_field_tokenConsumer.SEMI();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.classBodyDeclaration();
    }));
    return null;
  }
  recordHeader(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_recordHeader();
    }), "recordHeader", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_recordHeader(): any {
    this.__qin_field_tokenConsumer.LPAREN();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.recordComponentList();
    }));
    this.__qin_field_tokenConsumer.RPAREN();
    return null;
  }
  recordComponentList(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_recordComponentList();
    }), "recordComponentList", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_recordComponentList(): any {
    this.recordComponent();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.COMMA();
      this.recordComponent();
      return null;
    }));
    return null;
  }
  recordComponent(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_recordComponent();
    }), "recordComponent", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_recordComponent(): any {
    this.typeType();
    this.identifier();
    return null;
  }
  recordBody(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_recordBody();
    }), "recordBody", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_recordBody(): any {
    this.__qin_field_tokenConsumer.LBRACE();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return SubhutiCompileOnlyDsl.Or(SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentRecordBodyLooksLikeCompactConstructor.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.compactConstructorDeclaration();
    })), SubhutiCompileOnlyDsl.gate((() => { const __qin_bound_receiver = this; return __qin_bound_receiver.currentRecordBodyLooksLikeClassBodyDeclaration.bind(__qin_bound_receiver); })(), __qin_java_functional(() => {
      return this.classBodyDeclaration();
    })));
    }));
    this.__qin_field_tokenConsumer.RBRACE();
    return null;
  }
  currentRecordBodyLooksLikeCompactConstructor(): any {
    let offset: any = 1.0;
    while (this.isRecordBodyModifierToken(this.tokenNameAt(offset))) {
      offset++;
    }
    return (__QinJavaLangString.equals("IDENTIFIER", this.tokenNameAt(offset)) && __QinJavaLangString.equals("LBRACE", this.tokenNameAt(__qin_binary__("+", offset, 1.0))));
  }
  currentRecordBodyLooksLikeClassBodyDeclaration(): any {
    return (!__QinJavaLangString.equals("RBRACE", this.tokenNameAt(1.0)) && !this.currentRecordBodyLooksLikeCompactConstructor());
  }
  isRecordBodyModifierToken(tokenName: string): any {
    return (__QinJavaLangString.equals("PUBLIC", tokenName) || __QinJavaLangString.equals("PROTECTED", tokenName) || __QinJavaLangString.equals("PRIVATE", tokenName) || __QinJavaLangString.equals("STATIC", tokenName) || __QinJavaLangString.equals("ABSTRACT", tokenName) || __QinJavaLangString.equals("FINAL", tokenName) || __QinJavaLangString.equals("STRICTFP", tokenName) || __QinJavaLangString.equals("SEALED", tokenName) || __QinJavaLangString.equals("NON_SEALED", tokenName) || __QinJavaLangString.equals("NATIVE", tokenName) || __QinJavaLangString.equals("SYNCHRONIZED", tokenName) || __QinJavaLangString.equals("TRANSIENT", tokenName) || __QinJavaLangString.equals("VOLATILE", tokenName));
  }
  compactConstructorDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_compactConstructorDeclaration();
    }), "compactConstructorDeclaration", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_compactConstructorDeclaration(): any {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.modifier();
    }));
    this.identifier();
    this.block();
    return null;
  }
  switchBlockStatementGroup(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_switchBlockStatementGroup();
    }), "switchBlockStatementGroup", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_switchBlockStatementGroup(): any {
    SubhutiCompileOnlyDsl.AtLeastOne(__qin_java_functional(() => {
      return this.switchLabel();
    }));
    SubhutiCompileOnlyDsl.AtLeastOne(__qin_java_functional(() => {
      return this.blockStatement();
    }));
    return null;
  }
  switchLabel(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_switchLabel();
    }), "switchLabel", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_switchLabel(): any {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.CASE();
      SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.pattern();
    }), __qin_java_functional(() => {
      return this.expression();
    }));
      this.__qin_field_tokenConsumer.COLON();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.DEFAULT();
      this.__qin_field_tokenConsumer.COLON();
      return null;
    }));
    return null;
  }
  pattern(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_pattern();
    }), "pattern", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_pattern(): any {
    this.typeType();
    this.identifier();
    return null;
  }
  __qin_arguments(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw___qin_arguments();
    }), "arguments", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw___qin_arguments(): any {
    this.__qin_field_tokenConsumer.LPAREN();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.expressionList();
    }));
    this.__qin_field_tokenConsumer.RPAREN();
    return null;
  }
  nonWildcardTypeArguments(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_nonWildcardTypeArguments();
    }), "nonWildcardTypeArguments", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_nonWildcardTypeArguments(): any {
    this.__qin_field_tokenConsumer.LT();
    this.typeList();
    this.__qin_field_tokenConsumer.GT();
    return null;
  }
  explicitGenericInvocationSuffix(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_explicitGenericInvocationSuffix();
    }), "explicitGenericInvocationSuffix", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_explicitGenericInvocationSuffix(): any {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.SUPER();
      this.superSuffix();
      return null;
    }), __qin_java_functional(() => {
      this.identifier();
      this.__qin_arguments();
      return null;
    }));
    return null;
  }
  explicitGenericInvocation(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_explicitGenericInvocation();
    }), "explicitGenericInvocation", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_explicitGenericInvocation(): any {
    this.nonWildcardTypeArguments();
    this.explicitGenericInvocationSuffix();
    return null;
  }
  innerCreator(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_innerCreator();
    }), "innerCreator", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_innerCreator(): any {
    this.identifier();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.nonWildcardTypeArgumentsOrDiamond();
    }));
    this.classCreatorRest();
    return null;
  }
  nonWildcardTypeArgumentsOrDiamond(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_nonWildcardTypeArgumentsOrDiamond();
    }), "nonWildcardTypeArgumentsOrDiamond", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_nonWildcardTypeArgumentsOrDiamond(): any {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LT();
      this.__qin_field_tokenConsumer.GT();
      return null;
    }), __qin_java_functional(() => {
      return this.nonWildcardTypeArguments();
    }));
    return null;
  }
  classCreatorRest(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_classCreatorRest();
    }), "classCreatorRest", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_classCreatorRest(): any {
    this.__qin_arguments();
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.classBody();
    }));
    return null;
  }
  superSuffix(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_superSuffix();
    }), "superSuffix", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_superSuffix(): any {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      return this.__qin_arguments();
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.DOT();
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.typeArguments();
    }));
      this.identifier();
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_arguments();
    }));
      return null;
    }));
    return null;
  }
  moduleDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_moduleDeclaration();
    }), "moduleDeclaration", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_moduleDeclaration(): any {
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.annotation();
    }));
    SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.OPEN();
    }));
    this.__qin_field_tokenConsumer.MODULE();
    this.qualifiedName();
    this.__qin_field_tokenConsumer.LBRACE();
    SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.moduleDirective();
    }));
    this.__qin_field_tokenConsumer.RBRACE();
    return null;
  }
  moduleDirective(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_moduleDirective();
    }), "moduleDirective", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_moduleDirective(): any {
    SubhutiCompileOnlyDsl.Or(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.REQUIRES();
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      return this.requiresModifier();
    }));
      this.qualifiedName();
      this.__qin_field_tokenConsumer.SEMI();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.EXPORTS();
      this.qualifiedName();
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.TO();
      this.qualifiedName();
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.COMMA();
      this.qualifiedName();
      return null;
    }));
      return null;
    }));
      this.__qin_field_tokenConsumer.SEMI();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.OPENS();
      this.qualifiedName();
      SubhutiCompileOnlyDsl.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.TO();
      this.qualifiedName();
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.COMMA();
      this.qualifiedName();
      return null;
    }));
      return null;
    }));
      this.__qin_field_tokenConsumer.SEMI();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.USES();
      this.qualifiedName();
      this.__qin_field_tokenConsumer.SEMI();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.PROVIDES();
      this.qualifiedName();
      this.__qin_field_tokenConsumer.WITH();
      this.qualifiedName();
      SubhutiCompileOnlyDsl.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.COMMA();
      this.qualifiedName();
      return null;
    }));
      this.__qin_field_tokenConsumer.SEMI();
      return null;
    }));
    return null;
  }
  requiresModifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_requiresModifier();
    }), "requiresModifier", "JavaParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_requiresModifier(): any {
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
