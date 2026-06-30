import { com_slime_parser_SlimeParser, com_slime_parser_SlimeParser as SlimeParser } from "../../slime/parser/SlimeParser.ts";
import { com_slime_parser_typescript_SlimeTSDeclarationParser, com_slime_parser_typescript_SlimeTSDeclarationParser as SlimeTSDeclarationParser } from "../../slime/parser/typescript/SlimeTSDeclarationParser.ts";
import { com_slime_parser_typescript_SlimeTSTypeParser, com_slime_parser_typescript_SlimeTSTypeParser as SlimeTSTypeParser } from "../../slime/parser/typescript/SlimeTSTypeParser.ts";
import { com_slime_parser_SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser as SlimeJavascriptParser, com_slime_parser_SlimeJavascriptParser$SourceType, com_slime_parser_SlimeJavascriptParser$SourceType as SourceType } from "../../slime/parser/SlimeJavascriptParser.ts";
import { com_slime_parser_module_SlimeModuleParser, com_slime_parser_module_SlimeModuleParser as SlimeModuleParser } from "../../slime/parser/module/SlimeModuleParser.ts";
import { com_slime_parser_class__SlimeClassParser, com_slime_parser_class__SlimeClassParser as SlimeClassParser } from "../../slime/parser/class_/SlimeClassParser.ts";
import { com_slime_parser_function_SlimeFunctionParser, com_slime_parser_function_SlimeFunctionParser as SlimeFunctionParser } from "../../slime/parser/function/SlimeFunctionParser.ts";
import { com_slime_parser_statements_SlimeStatementParser, com_slime_parser_statements_SlimeStatementParser as SlimeStatementParser } from "../../slime/parser/statements/SlimeStatementParser.ts";
import { com_slime_parser_expressions_SlimeAssignmentExpressionParser, com_slime_parser_expressions_SlimeAssignmentExpressionParser as SlimeAssignmentExpressionParser } from "../../slime/parser/expressions/SlimeAssignmentExpressionParser.ts";
import { com_slime_parser_expressions_SlimeBinaryExpressionParser, com_slime_parser_expressions_SlimeBinaryExpressionParser as SlimeBinaryExpressionParser } from "../../slime/parser/expressions/SlimeBinaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimeUnaryExpressionParser, com_slime_parser_expressions_SlimeUnaryExpressionParser as SlimeUnaryExpressionParser } from "../../slime/parser/expressions/SlimeUnaryExpressionParser.ts";
import { com_slime_parser_expressions_SlimePrimaryExpressionParser, com_slime_parser_expressions_SlimePrimaryExpressionParser as SlimePrimaryExpressionParser } from "../../slime/parser/expressions/SlimePrimaryExpressionParser.ts";
import { com_slime_parser_literal_SlimeLiteralParser, com_slime_parser_literal_SlimeLiteralParser as SlimeLiteralParser } from "../../slime/parser/literal/SlimeLiteralParser.ts";
import { com_slime_parser_identifier_SlimeIdentifierParser, com_slime_parser_identifier_SlimeIdentifierParser as SlimeIdentifierParser } from "../../slime/parser/identifier/SlimeIdentifierParser.ts";
import { com_slime_parser_base_SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase as SlimeJavascriptParserBase, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams, com_slime_parser_base_SlimeJavascriptParserBase$TemplateLiteralParams as TemplateLiteralParams } from "../../slime/parser/base/SlimeJavascriptParserBase.ts";
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
class com_qin_parser_QinParser extends com_slime_parser_SlimeParser {
  constructor(...__qin_args: any[]) {
    switch (__qin_args.length) {
      case 1: {
        const sourceCode: any = __qin_args[0];
        super(sourceCode);
        this.__qin_constructor_com_qin_parser_QinParser_1(sourceCode);
        return;
      }
      default: throw new Error("Unsupported Java constructor arity: QinParser/" + __qin_args.length);
    }
  }
  __qin_constructor_com_qin_parser_QinParser_1(sourceCode: string): void {
    null;
  }
  QinModule(sourceType: com_slime_parser_SlimeJavascriptParser$SourceType): any {
    this.Program(sourceType);
    return null;
  }
  QinObjectDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_QinObjectDeclaration(params);
    }), "QinObjectDeclaration", "QinParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_QinObjectDeclaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.Option(__qin_java_functional(() => {
      return this.TSDecorators();
    }));
    this.QinObjectDeclarationBody(params);
    return null;
  }
  QinObjectDeclarationBody(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_QinObjectDeclarationBody(params);
    }), "QinObjectDeclarationBody", "QinParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_QinObjectDeclarationBody(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.consumeIdentifierValue("object");
    this.BindingIdentifier(new com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams(true, params.__qin_yield(), params.__qin_await()));
    this.ClassTail(params);
    return null;
  }
  Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_Declaration(params);
    }), "Declaration", "QinParser", __qin_subhuti_rule_cache_key([params]));
  }
  __qin_subhuti_raw_Declaration(params: com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams): any {
    this.Or(__qin_java_functional(() => {
      return this.QinObjectDeclaration(params);
    }), __qin_java_functional(() => {
      return super.__qin_subhuti_raw_Declaration(params);
    }));
    return null;
  }
  ExportDeclaration(): any {
    return this.executeRuleWrapper(__qin_java_functional(() => {
      return this.__qin_subhuti_raw_ExportDeclaration();
    }), "ExportDeclaration", "QinParser", __qin_subhuti_rule_cache_key([]));
  }
  __qin_subhuti_raw_ExportDeclaration(): any {
    this.Or(__qin_java_functional(() => {
      this.TSDecorators();
      this.__qin_field_tokenConsumer.Export();
      this.__qin_field_tokenConsumer.Default();
      this.QinObjectDeclarationBody(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
      return null;
    }), __qin_java_functional(() => {
      this.TSDecorators();
      this.__qin_field_tokenConsumer.Export();
      this.QinObjectDeclarationBody(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Export();
      this.__qin_field_tokenConsumer.Default();
      this.QinObjectDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, true));
      return null;
    }), __qin_java_functional(() => {
      this.__qin_field_tokenConsumer.Export();
      this.QinObjectDeclaration(new com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams(false, true, false));
      return null;
    }), __qin_java_functional(() => {
      return super.__qin_subhuti_raw_ExportDeclaration();
    }));
    return null;
  }
}
const QinParser = com_qin_parser_QinParser;

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_qin_parser_QinParser };
