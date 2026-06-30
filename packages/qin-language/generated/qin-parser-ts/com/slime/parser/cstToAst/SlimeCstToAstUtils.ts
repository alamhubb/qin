import { com_slime_parser_cstToAst_SlimeAstCreateUtils, com_slime_parser_cstToAst_SlimeAstCreateUtils as SlimeAstCreateUtils } from "./SlimeAstCreateUtils.ts";
import { com_slime_parser_cstToAst_identifier_SlimeIdentifierCstToAst, com_slime_parser_cstToAst_identifier_SlimeIdentifierCstToAst as SlimeIdentifierCstToAst } from "./identifier/SlimeIdentifierCstToAst.ts";
import { com_slime_parser_cstToAst_literal_SlimeLiteralCstToAst, com_slime_parser_cstToAst_literal_SlimeLiteralCstToAst as SlimeLiteralCstToAst } from "./literal/SlimeLiteralCstToAst.ts";
import { com_slime_parser_cstToAst_SlimeProgramCstToAst, com_slime_parser_cstToAst_SlimeProgramCstToAst as SlimeProgramCstToAst } from "./SlimeProgramCstToAst.ts";
import { com_subhuti_struct_SubhutiCst, com_subhuti_struct_SubhutiCst as SubhutiCst, com_subhuti_struct_SubhutiCst$Builder } from "../../../subhuti/struct/SubhutiCst.ts";
import { com_slime_parser_cstToAst_expressions_SlimePrimaryExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimePrimaryExpressionCstToAst as SlimePrimaryExpressionCstToAst } from "./expressions/SlimePrimaryExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeBinaryExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeBinaryExpressionCstToAst as SlimeBinaryExpressionCstToAst } from "./expressions/SlimeBinaryExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeUnaryExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeUnaryExpressionCstToAst as SlimeUnaryExpressionCstToAst } from "./expressions/SlimeUnaryExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeAssignmentExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeAssignmentExpressionCstToAst as SlimeAssignmentExpressionCstToAst } from "./expressions/SlimeAssignmentExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeCallExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeCallExpressionCstToAst as SlimeCallExpressionCstToAst } from "./expressions/SlimeCallExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeMemberExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeMemberExpressionCstToAst as SlimeMemberExpressionCstToAst } from "./expressions/SlimeMemberExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeExpressionCstToAst as SlimeExpressionCstToAst } from "./expressions/SlimeExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_statements_SlimeStatementCstToAst, com_slime_parser_cstToAst_statements_SlimeStatementCstToAst as SlimeStatementCstToAst } from "./statements/SlimeStatementCstToAst.ts";
import { com_slime_parser_cstToAst_statements_SlimeVariableCstToAst, com_slime_parser_cstToAst_statements_SlimeVariableCstToAst as SlimeVariableCstToAst } from "./statements/SlimeVariableCstToAst.ts";
import { com_slime_parser_cstToAst_statements_SlimeControlFlowCstToAst, com_slime_parser_cstToAst_statements_SlimeControlFlowCstToAst as SlimeControlFlowCstToAst } from "./statements/SlimeControlFlowCstToAst.ts";
import { com_slime_parser_cstToAst_statements_SlimeLoopCstToAst, com_slime_parser_cstToAst_statements_SlimeLoopCstToAst as SlimeLoopCstToAst, com_slime_parser_cstToAst_statements_SlimeLoopCstToAst$ForInOfParts, com_slime_parser_cstToAst_statements_SlimeLoopCstToAst$ForInOfParts as ForInOfParts } from "./statements/SlimeLoopCstToAst.ts";
import { com_slime_parser_cstToAst_function_SlimeFunctionCstToAst, com_slime_parser_cstToAst_function_SlimeFunctionCstToAst as SlimeFunctionCstToAst } from "./function/SlimeFunctionCstToAst.ts";
import { com_slime_parser_cstToAst_function_SlimeFunctionExpressionCstToAst, com_slime_parser_cstToAst_function_SlimeFunctionExpressionCstToAst as SlimeFunctionExpressionCstToAst } from "./function/SlimeFunctionExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_function_SlimeArrowFunctionCstToAst, com_slime_parser_cstToAst_function_SlimeArrowFunctionCstToAst as SlimeArrowFunctionCstToAst } from "./function/SlimeArrowFunctionCstToAst.ts";
import { com_slime_parser_cstToAst_class__SlimeClassCstToAst, com_slime_parser_cstToAst_class__SlimeClassCstToAst as SlimeClassCstToAst, com_slime_parser_cstToAst_class__SlimeClassCstToAst$ClassTailParts } from "./class_/SlimeClassCstToAst.ts";
import { com_slime_parser_cstToAst_class__SlimeClassExpressionCstToAst, com_slime_parser_cstToAst_class__SlimeClassExpressionCstToAst as SlimeClassExpressionCstToAst, com_slime_parser_cstToAst_class__SlimeClassExpressionCstToAst$ClassTailParts } from "./class_/SlimeClassExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_module_SlimeImportCstToAst, com_slime_parser_cstToAst_module_SlimeImportCstToAst as SlimeImportCstToAst } from "./module/SlimeImportCstToAst.ts";
import { com_slime_parser_cstToAst_module_SlimeExportCstToAst, com_slime_parser_cstToAst_module_SlimeExportCstToAst as SlimeExportCstToAst } from "./module/SlimeExportCstToAst.ts";
import { com_slime_parser_cstToAst_typescript_SlimeTSTypeCstToAst, com_slime_parser_cstToAst_typescript_SlimeTSTypeCstToAst as SlimeTSTypeCstToAst } from "./typescript/SlimeTSTypeCstToAst.ts";
import { com_slime_parser_cstToAst_typescript_SlimeTSDeclarationCstToAst, com_slime_parser_cstToAst_typescript_SlimeTSDeclarationCstToAst as SlimeTSDeclarationCstToAst } from "./typescript/SlimeTSDeclarationCstToAst.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_functional, __qin_java_class_info__, __qin_binary__, __qin_logical__ } from "@qin/java-sdk-js";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
class com_slime_parser_cstToAst_SlimeCstToAstUtils {
  constructor(...__qin_args: any[]) {
    if (__qin_args.length !== 0) {
      throw new Error("Unsupported Java constructor arity: SlimeCstToAstUtils/" + __qin_args.length);
    }
  }
  static createIdentifierAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_identifier_SlimeIdentifierCstToAst.createIdentifierAst(cst);
  }
  static createBooleanLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_literal_SlimeLiteralCstToAst.createBooleanLiteralAst(cst);
  }
  static createNumericLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_literal_SlimeLiteralCstToAst.createNumericLiteralAst(cst);
  }
  static createStringLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_literal_SlimeLiteralCstToAst.createStringLiteralAst(cst);
  }
  static createPrimaryExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_expressions_SlimePrimaryExpressionCstToAst.createPrimaryExpressionAst(cst);
  }
  static createBinaryExpressionAst(left: any, op: string, right: any, cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_expressions_SlimeBinaryExpressionCstToAst.createBinaryExpressionAst(left, op, right, cst);
  }
  static createUnaryExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_expressions_SlimeUnaryExpressionCstToAst.createUnaryExpressionAst(cst);
  }
  static createAssignmentExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_expressions_SlimeAssignmentExpressionCstToAst.createAssignmentExpressionAst(cst);
  }
  static createCallExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_expressions_SlimeCallExpressionCstToAst.createCallExpressionAst(cst);
  }
  static createMemberExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_expressions_SlimeMemberExpressionCstToAst.createMemberExpressionAst(cst);
  }
  static createExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_expressions_SlimeExpressionCstToAst.createExpressionAst(cst);
  }
  static createExpressionStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_statements_SlimeStatementCstToAst.createExpressionStatementAst(cst);
  }
  static createBlockStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_statements_SlimeStatementCstToAst.createBlockStatementAst(cst);
  }
  static createVariableDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_statements_SlimeVariableCstToAst.createVariableDeclarationAst(cst);
  }
  static createIfStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_statements_SlimeControlFlowCstToAst.createIfStatementAst(cst);
  }
  static createReturnStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_statements_SlimeControlFlowCstToAst.createReturnStatementAst(cst);
  }
  static createForStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_statements_SlimeLoopCstToAst.createForStatementAst(cst);
  }
  static createWhileStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_statements_SlimeLoopCstToAst.createWhileStatementAst(cst);
  }
  static createFunctionDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_function_SlimeFunctionCstToAst.createFunctionDeclarationAst(cst);
  }
  static createFunctionExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_function_SlimeFunctionExpressionCstToAst.createFunctionExpressionAst(cst);
  }
  static createArrowFunctionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_function_SlimeArrowFunctionCstToAst.createArrowFunctionAst(cst);
  }
  static createClassDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_class__SlimeClassCstToAst.createClassDeclarationAst(cst);
  }
  static createClassExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_class__SlimeClassExpressionCstToAst.createClassExpressionAst(cst);
  }
  static createImportDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_module_SlimeImportCstToAst.createImportDeclarationAst(cst);
  }
  static createExportDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_module_SlimeExportCstToAst.createExportDeclarationAst(cst);
  }
  static createTSTypeAnnotationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_typescript_SlimeTSTypeCstToAst.createTSTypeAnnotationAst(cst);
  }
  static createTSTypeAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_typescript_SlimeTSTypeCstToAst.createTSTypeAst(cst);
  }
  static createTSInterfaceDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_typescript_SlimeTSDeclarationCstToAst.createTSInterfaceDeclarationAst(cst);
  }
  static createTSTypeAliasDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_typescript_SlimeTSDeclarationCstToAst.createTSTypeAliasDeclarationAst(cst);
  }
  static createTSEnumDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_typescript_SlimeTSDeclarationCstToAst.createTSEnumDeclarationAst(cst);
  }
  static createProgramAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_SlimeProgramCstToAst.createProgramAst(cst);
  }
}
const SlimeCstToAstUtils = com_slime_parser_cstToAst_SlimeCstToAstUtils;

function run() {
  return null;
}

const __qinResult = run();
if (typeof globalThis !== 'undefined') {
  const __qinGlobal = globalThis as any;
  __qinGlobal.__qinResult = __qinResult;
}

export { com_slime_parser_cstToAst_SlimeCstToAstUtils };
