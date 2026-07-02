import { com_slime_parser_typescript_SlimeTSDeclarationParser, com_slime_parser_typescript_SlimeTSDeclarationParser as SlimeTSDeclarationParser } from "./typescript/SlimeTSDeclarationParser.ts";
import { com_subhuti_struct_SubhutiCst, com_subhuti_struct_SubhutiCst as SubhutiCst, com_subhuti_struct_SubhutiCst$Builder } from "../../subhuti/struct/SubhutiCst.ts";
import { com_slime_parser_typescript_SlimeTSTypeParser, com_slime_parser_typescript_SlimeTSTypeParser as SlimeTSTypeParser } from "./typescript/SlimeTSTypeParser.ts";
import { com_slime_parser_SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser as SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser$SourceType, com_slime_parser_SlimeJavascriptParser$SourceType as SourceType } from "./SlimeJavascriptParser.ts";
import { com_slime_parser_module_SlimeModuleParser, com_slime_parser_module_SlimeModuleParser as SlimeModuleParser } from "./module/SlimeModuleParser.ts";
import { com_slime_parser_class__SlimeClassParser, com_slime_parser_class__SlimeClassParser as SlimeClassParser } from "./class_/SlimeClassParser.ts";
import { com_slime_parser_function_SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser as SlimeFunctionParser } from "./function/SlimeFunctionParser.ts";
import { com_slime_parser_statements_SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser as SlimeStatementParser } from "./statements/SlimeStatementParser.ts";
import { com_slime_parser_expressions_SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser as SlimeAssignmentExpressionParser } from "./expressions/SlimeAssignmentExpressionParser.ts";
import { com_slime_parser_expressions_SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser as SlimeBinaryExpressionParser } from "./expressions/SlimeBinaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser as SlimeUnaryExpressionParser } from "./expressions/SlimeUnaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser as SlimePrimaryExpressionParser } from "./expressions/SlimePrimaryExpressionParser.ts";
import { com_slime_parser_literal_SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser as SlimeLiteralParser } from "./literal/SlimeLiteralParser.ts";
import { com_slime_parser_identifier_SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser as SlimeIdentifierParser } from "./identifier/SlimeIdentifierParser.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "./base/SlimeJavascriptParserBase.ts";
import { com_subhuti_parser_SubhutiParser, com_subhuti_parser_SubhutiParser as SubhutiParser } from "../../subhuti/parser/SubhutiParser.ts";
import { com_subhuti_parser_SubhutiParserFinal, com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal } from "../../subhuti/parser/SubhutiParserFinal.ts";
import { com_subhuti_parser_SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators as SubhutiParserCombinators, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$StopTokenMatcher as StopTokenMatcher, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorOrBranchContext as AllowErrorOrBranchContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$AllowErrorContext as AllowErrorContext, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame, com_subhuti_parser_SubhutiParserCombinators$ManyTolerantFrame as ManyTolerantFrame } from "../../subhuti/parser/SubhutiParserCombinators.ts";
import { com_subhuti_parser_SubhutiParserCore, com_subhuti_parser_SubhutiParserCore as SubhutiParserCore, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult, com_subhuti_parser_SubhutiParserCore$RuleExecutionResult as RuleExecutionResult } from "../../subhuti/parser/SubhutiParserCore.ts";
import { com_subhuti_parser_SubhutiParserState, com_subhuti_parser_SubhutiParserState as SubhutiParserState } from "../../subhuti/parser/SubhutiParserState.ts";
import { com_subhuti_lookahead_SubhutiTokenLookahead } from "../../subhuti/lookahead/SubhutiTokenLookahead.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_functional, __qin_java_class_info__, __qin_binary__, __qin_logical__ } from "@qin/java-sdk-js";
import { __qin_subhuti_rule_cache_key } from "@qin/java-sdk-js/tooling";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
class com_slime_parser_SlimeParser extends com_slime_parser_typescript_SlimeTSDeclarationParser {
  constructor(...__qin_args: any[]) {
    switch (__qin_args.length) {
      case 1: {
        const sourceCode: any = __qin_args[0];
        super(sourceCode);
        this.__qin_constructor_com_slime_parser_SlimeParser_1(sourceCode);
        return;
      }
      default: throw new Error("Unsupported Java constructor arity: SlimeParser/" + __qin_args.length);
    }
  }
  __qin_constructor_com_slime_parser_SlimeParser_1(sourceCode: string): void {
    null;
  }
  BindingIdentifier(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_BindingIdentifier(params);
    }), "BindingIdentifier", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_BindingIdentifier(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    super.__qin_subhuti_raw___qin_overload_BindingIdentifier_1_0(params);
    this.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Question();
    }));
    this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
    return null;
  }
  FormalParameter(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FormalParameter();
    }), "FormalParameter", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_FormalParameter(): any {
    this.Option(__qin_java_functional(() => {
      return this.TSDecorators();
    }));
    let nextTokenValue: any = (() => {
      if (__qin_binary__("!=", this.LA(1.0), null)) {
        return this.LA(1.0).getTokenValue();
      }
      return null;
    })();
    let hasAccessModifier: any = (() => {
      if ((() => {
      if (__QinJavaLangString.equals("public", nextTokenValue)) {
        return true;
      }
      return __QinJavaLangString.equals("private", nextTokenValue);
    })()) {
        return true;
      }
      return __QinJavaLangString.equals("protected", nextTokenValue);
    })();
    let hasReadonlyModifier: any = false;
    if (__QinJavaLangString.equals("readonly", nextTokenValue)) {
      let secondTokenValue: any = (() => {
      if (__qin_binary__("!=", this.LA(2.0), null)) {
        return this.LA(2.0).getTokenValue();
      }
      return null;
    })();
      let isParameterSuffix: any = (() => {
      if ((() => {
      if ((() => {
      if ((() => {
      if (__QinJavaLangString.equals("?", secondTokenValue)) {
        return true;
      }
      return __QinJavaLangString.equals(":", secondTokenValue);
    })()) {
        return true;
      }
      return __QinJavaLangString.equals(",", secondTokenValue);
    })()) {
        return true;
      }
      return __QinJavaLangString.equals(")", secondTokenValue);
    })()) {
        return true;
      }
      return __QinJavaLangString.equals("=", secondTokenValue);
    })();
      hasReadonlyModifier = (() => {
      if (isParameterSuffix) {
        return false;
      }
      return true;
    })();
    }
    if ((() => {
      if (hasAccessModifier) {
        return true;
      }
      return hasReadonlyModifier;
    })()) {
      this.TSParameterProperty();
    } else {
      this.BindingElement();
    }
    return null;
  }
  FormalParameters(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FormalParameters();
    }), "FormalParameters", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_FormalParameters(): any {
    let nextTokenValue: any = (() => {
      if (__qin_binary__("!=", this.LA(1.0), null)) {
        return this.LA(1.0).getTokenValue();
      }
      return null;
    })();
    if (__QinJavaLangString.equals("this", nextTokenValue)) {
      this.TSThisParameter();
      this.Option(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Comma();
      this.Or(__qin_java_functional(() => {
      this.FormalParameterList();
      this.__qin_field_tokenConsumer.Comma();
      this.FunctionRestParameter();
      return null;
    }), __qin_java_functional(() => {
      this.FormalParameterList();
      this.__qin_field_tokenConsumer.Comma();
      return null;
    }), __qin_java_functional(() => {
      return this.FormalParameterList();
    }), __qin_java_functional(() => {
      return this.FunctionRestParameter();
    }));
      return null;
    }));
    } else {
      super.__qin_subhuti_raw___qin_overload_FormalParameters_0_0();
    }
    return null;
  }
  TSAccessibilityModifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSAccessibilityModifier();
    }), "TSAccessibilityModifier", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSAccessibilityModifier(): any {
    this.Or(__qin_java_functional(() => {
      return this.consumeIdentifierValue("public");
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("private");
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("protected");
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("readonly");
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("override");
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("declare");
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("accessor");
    }));
    return null;
  }
  ClassElement(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassElement(params);
    }), "ClassElement", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassElement(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.Or(__qin_java_functional(() => {
      this.Option(__qin_java_functional(() => {
      return this.TSDecorators();
    }));
      this.TSClassMethodSignature(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
      return null;
    }), __qin_java_functional(() => {
      this.Option(__qin_java_functional(() => {
      return this.TSDecorators();
    }));
      this.TSClassAbstractPropertySignature(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
      return null;
    }), __qin_java_functional(() => {
      this.Option(__qin_java_functional(() => {
      return this.TSDecorators();
    }));
      this.Option(__qin_java_functional(() => {
      return this.TSAbstractModifier();
    }));
      this.Many(__qin_java_functional(() => {
      return this.TSAccessibilityModifier();
    }));
      this.MethodDefinition(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
      return null;
    }), __qin_java_functional(() => {
      this.Option(__qin_java_functional(() => {
      return this.TSDecorators();
    }));
      this.Option(__qin_java_functional(() => {
      return this.TSAbstractModifier();
    }));
      this.Many(__qin_java_functional(() => {
      return this.TSAccessibilityModifier();
    }));
      this.consumeIdentifierValue("static");
      this.MethodDefinition(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
      return null;
    }), __qin_java_functional(() => {
      this.Option(__qin_java_functional(() => {
      return this.TSDecorators();
    }));
      this.Option(__qin_java_functional(() => {
      return this.TSAbstractModifier();
    }));
      this.Many(__qin_java_functional(() => {
      return this.TSAccessibilityModifier();
    }));
      this.FieldDefinition(params);
      this.SemicolonASI();
      return null;
    }), __qin_java_functional(() => {
      this.Option(__qin_java_functional(() => {
      return this.TSDecorators();
    }));
      this.Option(__qin_java_functional(() => {
      return this.TSAbstractModifier();
    }));
      this.Many(__qin_java_functional(() => {
      return this.TSAccessibilityModifier();
    }));
      this.consumeIdentifierValue("static");
      this.FieldDefinition(params);
      this.SemicolonASI();
      return null;
    }), __qin_java_functional(() => {
      this.Option(__qin_java_functional(() => {
      return this.TSDecorators();
    }));
      this.Option(__qin_java_functional(() => {
      return this.TSAbstractModifier();
    }));
      this.ClassStaticBlock(params);
      return null;
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Semicolon();
    }));
    return null;
  }
  TSAbstractModifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSAbstractModifier();
    }), "TSAbstractModifier", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSAbstractModifier(): any {
    this.consumeIdentifierValue("abstract");
    return null;
  }
  TSClassMethodSignature(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSClassMethodSignature(params);
    }), "TSClassMethodSignature", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_TSClassMethodSignature(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Many(__qin_java_functional(() => {
      return this.Or(__qin_java_functional(() => {
      return this.TSAbstractModifier();
    }), __qin_java_functional(() => {
      return this.TSAccessibilityModifier();
    }), __qin_java_functional(() => {
      return this.consumeIdentifierValue("static");
    }));
    }));
    this.ClassElementName(params);
    this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
    this.__qin_field_tokenConsumer.LParen();
    this.UniqueFormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
    this.SemicolonASI();
    return null;
  }
  TSClassAbstractPropertySignature(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSClassAbstractPropertySignature(params);
    }), "TSClassAbstractPropertySignature", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_TSClassAbstractPropertySignature(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Many(__qin_java_functional(() => {
      return this.TSAccessibilityModifier();
    }));
    this.TSAbstractModifier();
    this.Many(__qin_java_functional(() => {
      return this.TSAccessibilityModifier();
    }));
    this.ClassElementName(params);
    this.Option(__qin_java_functional(() => {
      return this.Or(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Question();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.LogicalNot();
    }));
    }));
    this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
    this.SemicolonASI();
    return null;
  }
  TSDecorators(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSDecorators();
    }), "TSDecorators", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSDecorators(): any {
    this.TSDecorator();
    this.Many(__qin_java_functional(() => {
      return this.TSDecorator();
    }));
    return null;
  }
  TSDecorator(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSDecorator();
    }), "TSDecorator", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSDecorator(): any {
    this.__qin_field_tokenConsumer.At();
    this.LeftHandSideExpression(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    return null;
  }
  Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Declaration(params);
    }), "Declaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.Or(__qin_java_functional(() => {
      return this.TSInterfaceDeclaration();
    }), __qin_java_functional(() => {
      return this.TSTypeAliasDeclaration();
    }), __qin_java_functional(() => {
      return this.TSEnumDeclaration();
    }), __qin_java_functional(() => {
      return this.TSModuleDeclaration();
    }), __qin_java_functional(() => {
      return this.TSDeclareStatement();
    }), __qin_java_functional(() => {
      return super.__qin_subhuti_raw_Declaration(params);
    }));
    return null;
  }
  PrimaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_PrimaryExpression(params);
    }), "PrimaryExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_PrimaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Or(__qin_java_functional(() => {
      return this.TSTypeAssertion(params);
    }), __qin_java_functional(() => {
      return super.__qin_subhuti_raw___qin_overload_PrimaryExpression_1_0(params);
    }));
    return null;
  }
  TSTypeAssertion(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSTypeAssertion(params);
    }), "TSTypeAssertion", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_TSTypeAssertion(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.__qin_field_tokenConsumer.Less();
    this.TSType();
    this.__qin_field_tokenConsumer.Greater();
    this.UnaryExpression(params);
    return null;
  }
  AssignmentExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AssignmentExpression(params);
    }), "AssignmentExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AssignmentExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    super.__qin_subhuti_raw_AssignmentExpression(params);
    this.Many(__qin_java_functional(() => {
      return this.Or(__qin_java_functional(() => {
      return this.TSAsExpressionTail();
    }), __qin_java_functional(() => {
      return this.TSSatisfiesExpressionTail();
    }));
    }));
    return null;
  }
  TSAsExpressionTail(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSAsExpressionTail();
    }), "TSAsExpressionTail", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSAsExpressionTail(): any {
    this.__qin_field_tokenConsumer.As();
    this.Or(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Const();
    }), __qin_java_functional(() => {
      return this.TSType();
    }));
    return null;
  }
  TSSatisfiesExpressionTail(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSSatisfiesExpressionTail();
    }), "TSSatisfiesExpressionTail", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSSatisfiesExpressionTail(): any {
    this.consumeIdentifierValue("satisfies");
    this.TSType();
    return null;
  }
  TSNonNullExpressionTail(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSNonNullExpressionTail();
    }), "TSNonNullExpressionTail", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSNonNullExpressionTail(): any {
    this.__qin_field_tokenConsumer.LogicalNot();
    return null;
  }
  UpdateExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_UpdateExpression(params);
    }), "UpdateExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_UpdateExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Or(__qin_java_functional(() => {
      if ((() => {
      if (this.hasTopLevelPostfixTailAhead()) {
        return false;
      }
      return true;
    })()) {
        this.setParseFail();
        return null;
      }
      this.LeftHandSideExpression(params);
      this.assertNoLineBreak();
      this.__qin_field_tokenConsumer.Increment();
      return null;
    }), __qin_java_functional(() => {
      if ((() => {
      if (this.hasTopLevelPostfixTailAhead()) {
        return false;
      }
      return true;
    })()) {
        this.setParseFail();
        return null;
      }
      this.LeftHandSideExpression(params);
      this.assertNoLineBreak();
      this.__qin_field_tokenConsumer.Decrement();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Increment();
      this.UnaryExpression(params);
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Decrement();
      this.UnaryExpression(params);
      return null;
    }), __qin_java_functional(() => {
      if ((() => {
      if (this.hasTopLevelPostfixTailAhead()) {
        return false;
      }
      return true;
    })()) {
        this.setParseFail();
        return null;
      }
      this.LeftHandSideExpression(params);
      this.TSAsExpressionTail();
      return null;
    }), __qin_java_functional(() => {
      if ((() => {
      if (this.hasTopLevelPostfixTailAhead()) {
        return false;
      }
      return true;
    })()) {
        this.setParseFail();
        return null;
      }
      this.LeftHandSideExpression(params);
      this.TSSatisfiesExpressionTail();
      return null;
    }), __qin_java_functional(() => {
      if ((() => {
      if (this.hasTopLevelPostfixTailAhead()) {
        return false;
      }
      return true;
    })()) {
        this.setParseFail();
        return null;
      }
      this.LeftHandSideExpression(params);
      this.TSNonNullExpressionTail();
      return null;
    }), __qin_java_functional(() => {
      return this.LeftHandSideExpression(params);
    }));
    return null;
  }
  MemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_MemberExpression(params);
    }), "MemberExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_MemberExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Or(__qin_java_functional(() => {
      return this.PrimaryExpression(params);
    }), __qin_java_functional(() => {
      return this.SuperProperty(params);
    }), __qin_java_functional(() => {
      return this.MetaProperty();
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.New();
      this.MemberExpression(params);
      this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterInstantiation();
    }));
      this.Arguments(params);
      return null;
    }));
    this.Many(__qin_java_functional(() => {
      return this.Or(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LBracket();
      this.Expression(params.withIn(true));
      this.__qin_field_tokenConsumer.RBracket();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Dot();
      this.IdentifierName();
      return null;
    }), __qin_java_functional(() => {
      return this.IncompleteMemberAccessProperty();
    }), __qin_java_functional(() => {
      if ((() => {
      if (this.canStartTemplateLiteral()) {
        return false;
      }
      return true;
    })()) {
        this.setParseFail();
        return null;
      }
      this.TemplateLiteral(params);
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Dot();
      this.__qin_field_tokenConsumer.PrivateIdentifier();
      return null;
    }), __qin_java_functional(() => {
      return this.OptionalChain(params);
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.LogicalNot();
    }));
    }));
    return null;
  }
  CallExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CallExpression(params);
    }), "CallExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CallExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Or(__qin_java_functional(() => {
      return this.CoverCallExpressionAndAsyncArrowHead(params);
    }), __qin_java_functional(() => {
      return this.SuperCall(params);
    }), __qin_java_functional(() => {
      return this.ImportCall(params);
    }));
    this.Many(__qin_java_functional(() => {
      return this.Or(__qin_java_functional(() => {
      return this.Arguments(params);
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LBracket();
      this.Expression(params.withIn(true));
      this.__qin_field_tokenConsumer.RBracket();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Dot();
      this.IdentifierName();
      return null;
    }), __qin_java_functional(() => {
      return this.IncompleteMemberAccessProperty();
    }), __qin_java_functional(() => {
      if ((() => {
      if (this.canStartTemplateLiteral()) {
        return false;
      }
      return true;
    })()) {
        this.setParseFail();
        return null;
      }
      this.TemplateLiteral(params);
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Dot();
      this.__qin_field_tokenConsumer.PrivateIdentifier();
      return null;
    }), __qin_java_functional(() => {
      return this.OptionalChain(params);
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.LogicalNot();
    }));
    }));
    return null;
  }
  UnaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_UnaryExpression(params);
    }), "UnaryExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_UnaryExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Or(__qin_java_functional(() => {
      return this.TSTypeAssertion(params);
    }), __qin_java_functional(() => {
      return this.UpdateExpression(params);
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Delete();
      this.UnaryExpression(params);
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Void();
      this.UnaryExpression(params);
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Typeof();
      this.UnaryExpression(params);
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Plus();
      this.UnaryExpression(params);
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Minus();
      this.UnaryExpression(params);
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.BitwiseNot();
      this.UnaryExpression(params);
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LogicalNot();
      this.UnaryExpression(params);
      return null;
    }), (() => {
      if (params.__qin_await()) {
        return __qin_java_functional(() => {
      return this.AwaitExpression(params);
    });
      }
      return null;
    })());
    return null;
  }
  MethodDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_MethodDefinition(params);
    }), "MethodDefinition", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_MethodDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Or(__qin_java_functional(() => {
      return this.AsyncGeneratorMethod(params);
    }), __qin_java_functional(() => {
      return this.AsyncMethod(params);
    }), __qin_java_functional(() => {
      return this.GeneratorMethod(params);
    }), __qin_java_functional(() => {
      this.consumeIdentifierValue("get");
      this.ClassElementName(params);
      this.__qin_field_tokenConsumer.LParen();
      this.__qin_field_tokenConsumer.RParen();
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.__qin_field_tokenConsumer.LBrace();
      this.FunctionBody();
      this.__qin_field_tokenConsumer.RBrace();
      return null;
    }), __qin_java_functional(() => {
      this.consumeIdentifierValue("set");
      this.ClassElementName(params);
      this.__qin_field_tokenConsumer.LParen();
      this.PropertySetParameterList();
      this.__qin_field_tokenConsumer.RParen();
      this.__qin_field_tokenConsumer.LBrace();
      this.FunctionBody();
      this.__qin_field_tokenConsumer.RBrace();
      return null;
    }), __qin_java_functional(() => {
      this.ClassElementName(params);
      this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
      this.__qin_field_tokenConsumer.LParen();
      this.UniqueFormalParameters();
      this.__qin_field_tokenConsumer.RParen();
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.__qin_field_tokenConsumer.LBrace();
      this.FunctionBody();
      this.__qin_field_tokenConsumer.RBrace();
      return null;
    }));
    return null;
  }
  GeneratorMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_GeneratorMethod(params);
    }), "GeneratorMethod", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_GeneratorMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.__qin_field_tokenConsumer.Asterisk();
    this.ClassElementName(params);
    this.__qin_field_tokenConsumer.LParen();
    this.UniqueFormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
    this.__qin_field_tokenConsumer.LBrace();
    this.GeneratorBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  AsyncMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncMethod(params);
    }), "AsyncMethod", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.consumeIdentifierValue("async");
    this.assertNoLineBreak();
    this.ClassElementName(params);
    this.__qin_field_tokenConsumer.LParen();
    this.UniqueFormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
    this.__qin_field_tokenConsumer.LBrace();
    this.AsyncFunctionBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  AsyncGeneratorMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncGeneratorMethod(params);
    }), "AsyncGeneratorMethod", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncGeneratorMethod(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.consumeIdentifierValue("async");
    this.assertNoLineBreak();
    this.__qin_field_tokenConsumer.Asterisk();
    this.ClassElementName(params);
    this.__qin_field_tokenConsumer.LParen();
    this.UniqueFormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
    this.__qin_field_tokenConsumer.LBrace();
    this.AsyncGeneratorBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  FieldDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FieldDefinition(params);
    }), "FieldDefinition", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_FieldDefinition(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.ClassElementName(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    this.Option(__qin_java_functional(() => {
      return this.Or(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Question();
    }), __qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.LogicalNot();
    }));
    }));
    this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
    this.Option(__qin_java_functional(() => {
      return this.Initializer(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, false, false));
    }));
    return null;
  }
  ClassDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassDeclaration(params);
    }), "ClassDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.Option(__qin_java_functional(() => {
      return this.TSDecorators();
    }));
    this.Option(__qin_java_functional(() => {
      return this.TSAbstractModifier();
    }));
    this.__qin_field_tokenConsumer.Class();
    this.Or(__qin_java_functional(() => {
      return this.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    }), (() => {
      if (params.isDefault()) {
        return __qin_java_functional(() => {
      return null;
    });
      }
      return null;
    })());
    this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
    this.ClassTail(params);
    return null;
  }
  ClassTail(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassTail(params);
    }), "ClassTail", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassTail(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.Option(__qin_java_functional(() => {
      return this.ClassHeritage(params);
    }));
    this.Option(__qin_java_functional(() => {
      return this.TSImplementsClause();
    }));
    this.__qin_field_tokenConsumer.LBrace();
    this.Option(__qin_java_functional(() => {
      return this.ClassBody(params);
    }));
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  ClassExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassExpression(params);
    }), "ClassExpression", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassExpression(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.__qin_field_tokenConsumer.Class();
    this.Option(__qin_java_functional(() => {
      return this.BindingIdentifier(params);
    }));
    this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
    this.ClassTail(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(params.__qin_yield(), params.__qin_await(), false));
    return null;
  }
  ClassHeritage(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ClassHeritage(params);
    }), "ClassHeritage", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ClassHeritage(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.__qin_field_tokenConsumer.Extends();
    this.LeftHandSideExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterInstantiation();
    }));
    return null;
  }
  TSImplementsClause(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_TSImplementsClause();
    }), "TSImplementsClause", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_TSImplementsClause(): any {
    this.consumeIdentifierValue("implements");
    this.TSExpressionWithTypeArguments();
    this.Many(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Comma();
      this.TSExpressionWithTypeArguments();
      return null;
    }));
    return null;
  }
  FunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FunctionDeclaration(params);
    }), "FunctionDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_FunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.__qin_field_tokenConsumer.Function();
    this.Or(__qin_java_functional(() => {
      return this.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    }), (() => {
      if (params.isDefault()) {
        return __qin_java_functional(() => {
      return null;
    });
      }
      return null;
    })());
    this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
    this.__qin_field_tokenConsumer.LParen();
    this.FormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
    this.__qin_field_tokenConsumer.LBrace();
    this.FunctionBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  FunctionExpression(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_FunctionExpression();
    }), "FunctionExpression", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_FunctionExpression(): any {
    this.__qin_field_tokenConsumer.Function();
    this.Option(__qin_java_functional(() => {
      return this.BindingIdentifier(com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams.__qin_field_DEFAULT);
    }));
    this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
    this.__qin_field_tokenConsumer.LParen();
    this.FormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
    this.__qin_field_tokenConsumer.LBrace();
    this.FunctionBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  ArrowFunction(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArrowFunction(params);
    }), "ArrowFunction", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrowFunction(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
    this.ArrowParameters(params);
    this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
    this.assertNoLineBreak();
    this.__qin_field_tokenConsumer.Arrow();
    this.ConciseBody(params);
    return null;
  }
  AsyncArrowFunction(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncArrowFunction(params);
    }), "AsyncArrowFunction", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncArrowFunction(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Or(__qin_java_functional(() => {
      this.consumeIdentifierValue("async");
      this.assertNoLineBreak();
      this.AsyncArrowBindingIdentifier(params);
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.assertNoLineBreak();
      this.__qin_field_tokenConsumer.Arrow();
      this.AsyncConciseBody(params);
      return null;
    }), __qin_java_functional(() => {
      this.CoverCallExpressionAndAsyncArrowHead(params);
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.assertNoLineBreak();
      this.__qin_field_tokenConsumer.Arrow();
      this.AsyncConciseBody(params);
      return null;
    }));
    return null;
  }
  VariableDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_VariableDeclaration(params);
    }), "VariableDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_VariableDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Or(__qin_java_functional(() => {
      this.BindingIdentifier(params);
      this.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Exclamation();
    }));
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.Option(__qin_java_functional(() => {
      return this.Initializer(params);
    }));
      return null;
    }), __qin_java_functional(() => {
      this.BindingPattern(params);
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.Initializer(params);
      return null;
    }));
    return null;
  }
  LexicalBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_LexicalBinding(params);
    }), "LexicalBinding", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_LexicalBinding(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Or(__qin_java_functional(() => {
      this.BindingIdentifier(params);
      this.Option(__qin_java_functional(() => {
      return this.__qin_field_tokenConsumer.Exclamation();
    }));
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.Option(__qin_java_functional(() => {
      return this.Initializer(params);
    }));
      return null;
    }), __qin_java_functional(() => {
      this.BindingPattern(params);
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.Initializer(params);
      return null;
    }));
    return null;
  }
  ArrowParameters(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ArrowParameters(params);
    }), "ArrowParameters", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_ArrowParameters(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Or(__qin_java_functional(() => {
      return this.BindingIdentifier(params);
    }), __qin_java_functional(() => {
      return this.ArrowFormalParameters(params);
    }));
    return null;
  }
  CoverParenthesizedExpressionAndArrowParameterList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CoverParenthesizedExpressionAndArrowParameterList(params);
    }), "CoverParenthesizedExpressionAndArrowParameterList", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoverParenthesizedExpressionAndArrowParameterList(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.Or(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LParen();
      this.__qin_field_tokenConsumer.RParen();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LParen();
      this.__qin_field_tokenConsumer.Ellipsis();
      this.BindingIdentifier(params);
      this.__qin_field_tokenConsumer.RParen();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LParen();
      this.__qin_field_tokenConsumer.Ellipsis();
      this.BindingPattern(params);
      this.__qin_field_tokenConsumer.RParen();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LParen();
      this.Expression(params.withIn(true));
      this.__qin_field_tokenConsumer.Comma();
      this.__qin_field_tokenConsumer.Ellipsis();
      this.BindingIdentifier(params);
      this.__qin_field_tokenConsumer.RParen();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LParen();
      this.Expression(params.withIn(true));
      this.__qin_field_tokenConsumer.Comma();
      this.__qin_field_tokenConsumer.Ellipsis();
      this.BindingPattern(params);
      this.__qin_field_tokenConsumer.RParen();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LParen();
      this.Expression(params.withIn(true));
      this.__qin_field_tokenConsumer.Comma();
      this.__qin_field_tokenConsumer.RParen();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LParen();
      this.Expression(params.withIn(true));
      this.__qin_field_tokenConsumer.RParen();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.LParen();
      this.FormalParameterList();
      this.__qin_field_tokenConsumer.RParen();
      return null;
    }));
    return null;
  }
  GeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_GeneratorDeclaration(params);
    }), "GeneratorDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_GeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.Or(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Function();
      this.__qin_field_tokenConsumer.Asterisk();
      this.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
      this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
      this.__qin_field_tokenConsumer.LParen();
      this.FormalParameters();
      this.__qin_field_tokenConsumer.RParen();
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.__qin_field_tokenConsumer.LBrace();
      this.GeneratorBody();
      this.__qin_field_tokenConsumer.RBrace();
      return null;
    }), (() => {
      if (params.isDefault()) {
        return __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Function();
      this.__qin_field_tokenConsumer.Asterisk();
      this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
      this.__qin_field_tokenConsumer.LParen();
      this.FormalParameters();
      this.__qin_field_tokenConsumer.RParen();
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.__qin_field_tokenConsumer.LBrace();
      this.GeneratorBody();
      this.__qin_field_tokenConsumer.RBrace();
      return null;
    });
      }
      return null;
    })());
    return null;
  }
  GeneratorExpression(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_GeneratorExpression();
    }), "GeneratorExpression", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_GeneratorExpression(): any {
    this.__qin_field_tokenConsumer.Function();
    this.__qin_field_tokenConsumer.Asterisk();
    this.Option(__qin_java_functional(() => {
      return this.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, true, false));
    }));
    this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
    this.__qin_field_tokenConsumer.LParen();
    this.FormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
    this.__qin_field_tokenConsumer.LBrace();
    this.GeneratorBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  AsyncFunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncFunctionDeclaration(params);
    }), "AsyncFunctionDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncFunctionDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.Or(__qin_java_functional(() => {
      this.consumeIdentifierValue("async");
      this.assertNoLineBreak();
      this.__qin_field_tokenConsumer.Function();
      this.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
      this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
      this.__qin_field_tokenConsumer.LParen();
      this.FormalParameters();
      this.__qin_field_tokenConsumer.RParen();
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.__qin_field_tokenConsumer.LBrace();
      this.AsyncFunctionBody();
      this.__qin_field_tokenConsumer.RBrace();
      return null;
    }), (() => {
      if (params.isDefault()) {
        return __qin_java_functional(() => {
      this.consumeIdentifierValue("async");
      this.assertNoLineBreak();
      this.__qin_field_tokenConsumer.Function();
      this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
      this.__qin_field_tokenConsumer.LParen();
      this.FormalParameters();
      this.__qin_field_tokenConsumer.RParen();
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.__qin_field_tokenConsumer.LBrace();
      this.AsyncFunctionBody();
      this.__qin_field_tokenConsumer.RBrace();
      return null;
    });
      }
      return null;
    })());
    return null;
  }
  AsyncFunctionExpression(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncFunctionExpression();
    }), "AsyncFunctionExpression", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncFunctionExpression(): any {
    this.consumeIdentifierValue("async");
    this.assertNoLineBreak();
    this.__qin_field_tokenConsumer.Function();
    this.Option(__qin_java_functional(() => {
      return this.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, false, true));
    }));
    this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
    this.__qin_field_tokenConsumer.LParen();
    this.FormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
    this.__qin_field_tokenConsumer.LBrace();
    this.AsyncFunctionBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  AsyncGeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncGeneratorDeclaration(params);
    }), "AsyncGeneratorDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_AsyncGeneratorDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.Or(__qin_java_functional(() => {
      this.consumeIdentifierValue("async");
      this.assertNoLineBreak();
      this.__qin_field_tokenConsumer.Function();
      this.__qin_field_tokenConsumer.Asterisk();
      this.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
      this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
      this.__qin_field_tokenConsumer.LParen();
      this.FormalParameters();
      this.__qin_field_tokenConsumer.RParen();
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.__qin_field_tokenConsumer.LBrace();
      this.AsyncGeneratorBody();
      this.__qin_field_tokenConsumer.RBrace();
      return null;
    }), (() => {
      if (params.isDefault()) {
        return __qin_java_functional(() => {
      this.consumeIdentifierValue("async");
      this.assertNoLineBreak();
      this.__qin_field_tokenConsumer.Function();
      this.__qin_field_tokenConsumer.Asterisk();
      this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
      this.__qin_field_tokenConsumer.LParen();
      this.FormalParameters();
      this.__qin_field_tokenConsumer.RParen();
      this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
      this.__qin_field_tokenConsumer.LBrace();
      this.AsyncGeneratorBody();
      this.__qin_field_tokenConsumer.RBrace();
      return null;
    });
      }
      return null;
    })());
    return null;
  }
  AsyncGeneratorExpression(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_AsyncGeneratorExpression();
    }), "AsyncGeneratorExpression", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_AsyncGeneratorExpression(): any {
    this.consumeIdentifierValue("async");
    this.assertNoLineBreak();
    this.__qin_field_tokenConsumer.Function();
    this.__qin_field_tokenConsumer.Asterisk();
    this.Option(__qin_java_functional(() => {
      return this.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, true, true));
    }));
    this.Option(__qin_java_functional(() => {
      return this.TSTypeParameterDeclaration();
    }));
    this.__qin_field_tokenConsumer.LParen();
    this.FormalParameters();
    this.__qin_field_tokenConsumer.RParen();
    this.Option(__qin_java_functional(() => {
      return this.TSTypeAnnotation();
    }));
    this.__qin_field_tokenConsumer.LBrace();
    this.AsyncGeneratorBody();
    this.__qin_field_tokenConsumer.RBrace();
    return null;
  }
  ImportDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ImportDeclaration();
    }), "ImportDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportDeclaration(): any {
    this.Or(__qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Import();
      this.consumeIdentifierValue("type");
      this.ImportClause();
      this.FromClause();
      this.SemicolonASI();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Import();
      this.ImportClause();
      this.FromClause();
      this.Option(__qin_java_functional(() => {
      return this.WithClause();
    }));
      this.SemicolonASI();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Import();
      this.ModuleSpecifier();
      this.Option(__qin_java_functional(() => {
      return this.WithClause();
    }));
      this.SemicolonASI();
      return null;
    }));
    return null;
  }
  ImportSpecifier(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ImportSpecifier();
    }), "ImportSpecifier", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ImportSpecifier(): any {
    this.Or(__qin_java_functional(() => {
      this.consumeIdentifierValue("type");
      this.ModuleExportName();
      this.__qin_field_tokenConsumer.As();
      this.ImportedBinding();
      return null;
    }), __qin_java_functional(() => {
      this.consumeIdentifierValue("type");
      this.ImportedBinding();
      return null;
    }), __qin_java_functional(() => {
      this.ModuleExportName();
      this.__qin_field_tokenConsumer.As();
      this.ImportedBinding();
      return null;
    }), __qin_java_functional(() => {
      return this.ImportedBinding();
    }));
    return null;
  }
  ExportDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ExportDeclaration();
    }), "ExportDeclaration", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ExportDeclaration(): any {
    this.Or(__qin_java_functional(() => {
      this.TSDecorators();
      this.__qin_field_tokenConsumer.Export();
      this.__qin_field_tokenConsumer.Default();
      this.ClassDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
      return null;
    }), __qin_java_functional(() => {
      this.TSDecorators();
      this.__qin_field_tokenConsumer.Export();
      this.ClassDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Export();
      this.consumeIdentifierValue("type");
      this.NamedExports();
      this.FromClause();
      this.SemicolonASI();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Export();
      this.consumeIdentifierValue("type");
      this.NamedExports();
      this.SemicolonASI();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Export();
      this.ExportFromClause();
      this.FromClause();
      this.Option(__qin_java_functional(() => {
      return this.WithClause();
    }));
      this.SemicolonASI();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Export();
      this.NamedExports();
      this.SemicolonASI();
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Export();
      this.VariableStatement(new com_slime_parser_base_SlimeJavascriptParserBase$StatementParams(false, true, false));
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Export();
      this.Declaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Export();
      this.__qin_field_tokenConsumer.Default();
      this.HoistableDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Export();
      this.__qin_field_tokenConsumer.Default();
      this.ClassDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Export();
      this.__qin_field_tokenConsumer.Default();
      this.AssignmentExpression(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, false, true));
      this.SemicolonASI();
      return null;
    }));
    return null;
  }
  CoverCallExpressionAndAsyncArrowHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_CoverCallExpressionAndAsyncArrowHead(params);
    }), "CoverCallExpressionAndAsyncArrowHead", "SlimeParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_CoverCallExpressionAndAsyncArrowHead(params: com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams): any {
    this.MemberExpression(params);
    this.Or(__qin_java_functional(() => {
      this.TSTypeParameterInstantiation();
      this.Arguments(params);
      return null;
    }), __qin_java_functional(() => {
      return this.Arguments(params);
    }));
    return null;
  }
  parse(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_parse();
    }), "parse", "SlimeParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_parse(): any {
    return this.Program(com_slime_parser_SlimeJavascriptParser$SourceType.__qin_field_MODULE);
  }
}
const SlimeParser = com_slime_parser_SlimeParser;

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_SlimeParser };
