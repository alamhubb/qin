import { com_slime_parser_cstToAst_SlimeAstCreateUtils, com_slime_parser_cstToAst_SlimeAstCreateUtils as SlimeAstCreateUtils } from "./SlimeAstCreateUtils.ts";
import { com_slime_parser_cstToAst_identifier_SlimeIdentifierCstToAst, com_slime_parser_cstToAst_identifier_SlimeIdentifierCstToAst as SlimeIdentifierCstToAst } from "./identifier/SlimeIdentifierCstToAst.ts";
import { com_slime_parser_cstToAst_literal_SlimeLiteralCstToAst, com_slime_parser_cstToAst_literal_SlimeLiteralCstToAst as SlimeLiteralCstToAst } from "./literal/SlimeLiteralCstToAst.ts";
import { com_slime_parser_cstToAst_literal_SlimeCompoundLiteralCstToAst, com_slime_parser_cstToAst_literal_SlimeCompoundLiteralCstToAst as SlimeCompoundLiteralCstToAst } from "./literal/SlimeCompoundLiteralCstToAst.ts";
import { com_slime_parser_cstToAst_literal_SlimeTemplateLiteralCstToAst, com_slime_parser_cstToAst_literal_SlimeTemplateLiteralCstToAst as SlimeTemplateLiteralCstToAst } from "./literal/SlimeTemplateLiteralCstToAst.ts";
import { com_slime_parser_cstToAst_SlimeProgramCstToAst, com_slime_parser_cstToAst_SlimeProgramCstToAst as SlimeProgramCstToAst } from "./SlimeProgramCstToAst.ts";
import { com_subhuti_struct_SubhutiCst, com_subhuti_struct_SubhutiCst as SubhutiCst, com_subhuti_struct_SubhutiCst$Builder } from "../../../subhuti/struct/SubhutiCst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeExpressionCstToAst as SlimeExpressionCstToAst } from "./expressions/SlimeExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimePrimaryExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimePrimaryExpressionCstToAst as SlimePrimaryExpressionCstToAst } from "./expressions/SlimePrimaryExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeAssignmentExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeAssignmentExpressionCstToAst as SlimeAssignmentExpressionCstToAst } from "./expressions/SlimeAssignmentExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeUnaryExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeUnaryExpressionCstToAst as SlimeUnaryExpressionCstToAst } from "./expressions/SlimeUnaryExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeCallExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeCallExpressionCstToAst as SlimeCallExpressionCstToAst } from "./expressions/SlimeCallExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeMemberExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeMemberExpressionCstToAst as SlimeMemberExpressionCstToAst } from "./expressions/SlimeMemberExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeNewExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeNewExpressionCstToAst as SlimeNewExpressionCstToAst } from "./expressions/SlimeNewExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeOptionalExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeOptionalExpressionCstToAst as SlimeOptionalExpressionCstToAst } from "./expressions/SlimeOptionalExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_statements_SlimeStatementCstToAst, com_slime_parser_cstToAst_statements_SlimeStatementCstToAst as SlimeStatementCstToAst } from "./statements/SlimeStatementCstToAst.ts";
import { com_slime_parser_cstToAst_statements_SlimeVariableCstToAst, com_slime_parser_cstToAst_statements_SlimeVariableCstToAst as SlimeVariableCstToAst } from "./statements/SlimeVariableCstToAst.ts";
import { com_slime_parser_cstToAst_components_SlimeFunctionBodyCstToAst, com_slime_parser_cstToAst_components_SlimeFunctionBodyCstToAst as SlimeFunctionBodyCstToAst } from "./components/SlimeFunctionBodyCstToAst.ts";
import { com_slime_parser_cstToAst_class__SlimeMethodDefinitionCstToAst, com_slime_parser_cstToAst_class__SlimeMethodDefinitionCstToAst as SlimeMethodDefinitionCstToAst } from "./class_/SlimeMethodDefinitionCstToAst.ts";
import { com_slime_parser_cstToAst_class__SlimeClassCstToAst, com_slime_parser_cstToAst_class__SlimeClassCstToAst as SlimeClassCstToAst, com_slime_parser_cstToAst_class__SlimeClassCstToAst$ClassTailParts } from "./class_/SlimeClassCstToAst.ts";
import { com_slime_parser_cstToAst_class__SlimeClassExpressionCstToAst, com_slime_parser_cstToAst_class__SlimeClassExpressionCstToAst as SlimeClassExpressionCstToAst, com_slime_parser_cstToAst_class__SlimeClassExpressionCstToAst$ClassTailParts } from "./class_/SlimeClassExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_statements_SlimeControlFlowCstToAst, com_slime_parser_cstToAst_statements_SlimeControlFlowCstToAst as SlimeControlFlowCstToAst } from "./statements/SlimeControlFlowCstToAst.ts";
import { com_slime_parser_cstToAst_statements_SlimeLoopCstToAst, com_slime_parser_cstToAst_statements_SlimeLoopCstToAst as SlimeLoopCstToAst, com_slime_parser_cstToAst_statements_SlimeLoopCstToAst$ForInOfParts, com_slime_parser_cstToAst_statements_SlimeLoopCstToAst$ForInOfParts as ForInOfParts } from "./statements/SlimeLoopCstToAst.ts";
import { com_slime_parser_cstToAst_function_SlimeFunctionCstToAst, com_slime_parser_cstToAst_function_SlimeFunctionCstToAst as SlimeFunctionCstToAst } from "./function/SlimeFunctionCstToAst.ts";
import { com_slime_parser_cstToAst_function_SlimeFunctionExpressionCstToAst, com_slime_parser_cstToAst_function_SlimeFunctionExpressionCstToAst as SlimeFunctionExpressionCstToAst } from "./function/SlimeFunctionExpressionCstToAst.ts";
import { com_slime_parser_cstToAst_function_SlimeArrowFunctionCstToAst, com_slime_parser_cstToAst_function_SlimeArrowFunctionCstToAst as SlimeArrowFunctionCstToAst } from "./function/SlimeArrowFunctionCstToAst.ts";
import { com_slime_parser_cstToAst_statements_SlimeBreakContinueCstToAst, com_slime_parser_cstToAst_statements_SlimeBreakContinueCstToAst as SlimeBreakContinueCstToAst } from "./statements/SlimeBreakContinueCstToAst.ts";
import { com_slime_parser_cstToAst_components_SlimeFunctionParameterCstToAst, com_slime_parser_cstToAst_components_SlimeFunctionParameterCstToAst as SlimeFunctionParameterCstToAst } from "./components/SlimeFunctionParameterCstToAst.ts";
import { com_slime_parser_cstToAst_components_SlimeBindingPatternCstToAst, com_slime_parser_cstToAst_components_SlimeBindingPatternCstToAst as SlimeBindingPatternCstToAst } from "./components/SlimeBindingPatternCstToAst.ts";
import { com_slime_parser_cstToAst_module_SlimeExportCstToAst, com_slime_parser_cstToAst_module_SlimeExportCstToAst as SlimeExportCstToAst } from "./module/SlimeExportCstToAst.ts";
import { com_slime_parser_cstToAst_typescript_SlimeTSDecoratorCstToAst, com_slime_parser_cstToAst_typescript_SlimeTSDecoratorCstToAst as SlimeTSDecoratorCstToAst } from "./typescript/SlimeTSDecoratorCstToAst.ts";
import { com_slime_parser_cstToAst_statements_SlimeSwitchStatementCstToAst, com_slime_parser_cstToAst_statements_SlimeSwitchStatementCstToAst as SlimeSwitchStatementCstToAst } from "./statements/SlimeSwitchStatementCstToAst.ts";
import { com_slime_parser_cstToAst_statements_SlimeOtherStatementCstToAst, com_slime_parser_cstToAst_statements_SlimeOtherStatementCstToAst as SlimeOtherStatementCstToAst } from "./statements/SlimeOtherStatementCstToAst.ts";
import { com_slime_parser_cstToAst_expressions_SlimeYieldAwaitCstToAst, com_slime_parser_cstToAst_expressions_SlimeYieldAwaitCstToAst as SlimeYieldAwaitCstToAst } from "./expressions/SlimeYieldAwaitCstToAst.ts";
import { com_slime_parser_cstToAst_statements_SlimeTryStatementCstToAst, com_slime_parser_cstToAst_statements_SlimeTryStatementCstToAst as SlimeTryStatementCstToAst } from "./statements/SlimeTryStatementCstToAst.ts";
import { com_slime_ast_nodes_misc_Program, com_slime_ast_nodes_misc_Program as Program } from "../../ast/nodes/misc/Program.ts";
import { com_slime_ast_nodes_expressions_Identifier, com_slime_ast_nodes_expressions_Identifier as Identifier } from "../../ast/nodes/expressions/Identifier.ts";
import { com_slime_ast_nodes_expressions_Literal, com_slime_ast_nodes_expressions_Literal as Literal, com_slime_ast_nodes_expressions_Literal$BigintValue, com_slime_ast_nodes_expressions_Literal$BigintValue as BigintValue } from "../../ast/nodes/expressions/Literal.ts";
import { com_slime_ast_Expression, com_slime_ast_Expression as Expression } from "../../ast/Expression.ts";
import { com_slime_parser_cstToAst_expressions_SlimeBinaryExpressionCstToAst, com_slime_parser_cstToAst_expressions_SlimeBinaryExpressionCstToAst as SlimeBinaryExpressionCstToAst } from "./expressions/SlimeBinaryExpressionCstToAst.ts";
import { com_slime_ast_nodes_expressions_TemplateLiteral, com_slime_ast_nodes_expressions_TemplateLiteral as TemplateLiteral } from "../../ast/nodes/expressions/TemplateLiteral.ts";
import { com_slime_ast_nodes_expressions_AwaitExpression, com_slime_ast_nodes_expressions_AwaitExpression as AwaitExpression } from "../../ast/nodes/expressions/AwaitExpression.ts";
import { com_slime_ast_nodes_expressions_YieldExpression, com_slime_ast_nodes_expressions_YieldExpression as YieldExpression } from "../../ast/nodes/expressions/YieldExpression.ts";
import { com_slime_ast_nodes_statements_ExpressionStatement, com_slime_ast_nodes_statements_ExpressionStatement as ExpressionStatement } from "../../ast/nodes/statements/ExpressionStatement.ts";
import { com_slime_ast_nodes_statements_BlockStatement, com_slime_ast_nodes_statements_BlockStatement as BlockStatement } from "../../ast/nodes/statements/BlockStatement.ts";
import { com_slime_ast_nodes_misc_VariableDeclarator, com_slime_ast_nodes_misc_VariableDeclarator as VariableDeclarator } from "../../ast/nodes/misc/VariableDeclarator.ts";
import { com_slime_ast_nodes_declarations_VariableDeclaration, com_slime_ast_nodes_declarations_VariableDeclaration as VariableDeclaration } from "../../ast/nodes/declarations/VariableDeclaration.ts";
import { com_slime_ast_Pattern, com_slime_ast_Pattern as Pattern } from "../../ast/Pattern.ts";
import { com_slime_ast_nodes_patterns_ArrayPattern, com_slime_ast_nodes_patterns_ArrayPattern as ArrayPattern } from "../../ast/nodes/patterns/ArrayPattern.ts";
import { com_slime_ast_nodes_patterns_ObjectPattern, com_slime_ast_nodes_patterns_ObjectPattern as ObjectPattern } from "../../ast/nodes/patterns/ObjectPattern.ts";
import { com_slime_ast_nodes_statements_IfStatement, com_slime_ast_nodes_statements_IfStatement as IfStatement } from "../../ast/nodes/statements/IfStatement.ts";
import { com_slime_ast_nodes_statements_ReturnStatement, com_slime_ast_nodes_statements_ReturnStatement as ReturnStatement } from "../../ast/nodes/statements/ReturnStatement.ts";
import { com_slime_ast_nodes_statements_ForStatement, com_slime_ast_nodes_statements_ForStatement as ForStatement } from "../../ast/nodes/statements/ForStatement.ts";
import { com_slime_ast_Statement, com_slime_ast_Statement as Statement } from "../../ast/Statement.ts";
import { com_slime_ast_nodes_statements_WhileStatement, com_slime_ast_nodes_statements_WhileStatement as WhileStatement } from "../../ast/nodes/statements/WhileStatement.ts";
import { com_slime_ast_nodes_statements_DoWhileStatement, com_slime_ast_nodes_statements_DoWhileStatement as DoWhileStatement } from "../../ast/nodes/statements/DoWhileStatement.ts";
import { com_slime_ast_nodes_statements_BreakStatement, com_slime_ast_nodes_statements_BreakStatement as BreakStatement } from "../../ast/nodes/statements/BreakStatement.ts";
import { com_slime_ast_nodes_statements_ContinueStatement, com_slime_ast_nodes_statements_ContinueStatement as ContinueStatement } from "../../ast/nodes/statements/ContinueStatement.ts";
import { com_slime_ast_nodes_statements_ThrowStatement, com_slime_ast_nodes_statements_ThrowStatement as ThrowStatement } from "../../ast/nodes/statements/ThrowStatement.ts";
import { com_slime_ast_nodes_statements_SwitchStatement, com_slime_ast_nodes_statements_SwitchStatement as SwitchStatement } from "../../ast/nodes/statements/SwitchStatement.ts";
import { com_slime_ast_nodes_statements_TryStatement, com_slime_ast_nodes_statements_TryStatement as TryStatement } from "../../ast/nodes/statements/TryStatement.ts";
import { com_slime_ast_nodes_statements_WithStatement, com_slime_ast_nodes_statements_WithStatement as WithStatement } from "../../ast/nodes/statements/WithStatement.ts";
import { com_slime_ast_nodes_statements_DebuggerStatement, com_slime_ast_nodes_statements_DebuggerStatement as DebuggerStatement } from "../../ast/nodes/statements/DebuggerStatement.ts";
import { com_slime_ast_nodes_statements_LabeledStatement, com_slime_ast_nodes_statements_LabeledStatement as LabeledStatement } from "../../ast/nodes/statements/LabeledStatement.ts";
import { com_slime_ast_nodes_statements_EmptyStatement, com_slime_ast_nodes_statements_EmptyStatement as EmptyStatement } from "../../ast/nodes/statements/EmptyStatement.ts";
import { com_slime_ast_nodes_expressions_FunctionExpression, com_slime_ast_nodes_expressions_FunctionExpression as FunctionExpression } from "../../ast/nodes/expressions/FunctionExpression.ts";
import { com_slime_ast_nodes_expressions_ArrowFunctionExpression, com_slime_ast_nodes_expressions_ArrowFunctionExpression as ArrowFunctionExpression } from "../../ast/nodes/expressions/ArrowFunctionExpression.ts";
import { com_slime_ast_nodes_expressions_ObjectExpression, com_slime_ast_nodes_expressions_ObjectExpression as ObjectExpression } from "../../ast/nodes/expressions/ObjectExpression.ts";
import { com_slime_ast_nodes_expressions_ArrayExpression, com_slime_ast_nodes_expressions_ArrayExpression as ArrayExpression } from "../../ast/nodes/expressions/ArrayExpression.ts";
import { com_slime_ast_nodes_misc_MethodDefinition, com_slime_ast_nodes_misc_MethodDefinition as MethodDefinition } from "../../ast/nodes/misc/MethodDefinition.ts";
import { com_slime_ast_nodes_expressions_ClassExpression, com_slime_ast_nodes_expressions_ClassExpression as ClassExpression } from "../../ast/nodes/expressions/ClassExpression.ts";
import { com_slime_ast_nodes_misc_ClassBody, com_slime_ast_nodes_misc_ClassBody as ClassBody } from "../../ast/nodes/misc/ClassBody.ts";
import { com_slime_parser_cstToAst_module_SlimeImportCstToAst, com_slime_parser_cstToAst_module_SlimeImportCstToAst as SlimeImportCstToAst } from "./module/SlimeImportCstToAst.ts";
import { com_slime_ast_nodes_misc_Decorator, com_slime_ast_nodes_misc_Decorator as Decorator } from "../../ast/nodes/misc/Decorator.ts";
import { com_slime_parser_cstToAst_typescript_SlimeTSTypeCstToAst, com_slime_parser_cstToAst_typescript_SlimeTSTypeCstToAst as SlimeTSTypeCstToAst } from "./typescript/SlimeTSTypeCstToAst.ts";
import { com_slime_parser_cstToAst_typescript_SlimeTSDeclarationCstToAst, com_slime_parser_cstToAst_typescript_SlimeTSDeclarationCstToAst as SlimeTSDeclarationCstToAst } from "./typescript/SlimeTSDeclarationCstToAst.ts";

// Generated by Qin TypeScript backend
import { __qin_builtin_constructor__, __qin_java_pattern_regexp__, __QinJavaLangString, __qin_java_functional, __qin_java_class_info__, __qin_binary__, __qin_instanceof__, __qin_logical__ } from "@qin/java-sdk-js";

function __qin_structural_object__(value) {
  if (value == null || typeof value !== "object" || Array.isArray(value)) return false;
  return value.__qin_structural_object__ === true || value.constructor == null || value.constructor === Object;
}
class com_slime_parser_cstToAst_SlimeCstToAstUtils {
  __qin_field_programCstToAst: com_slime_parser_cstToAst_SlimeProgramCstToAst | null = null as any;
  __qin_field_expressionCstToAst: com_slime_parser_cstToAst_expressions_SlimeExpressionCstToAst | null = null as any;
  __qin_field_primaryExpressionCstToAst: com_slime_parser_cstToAst_expressions_SlimePrimaryExpressionCstToAst | null = null as any;
  __qin_field_assignmentExpressionCstToAst: com_slime_parser_cstToAst_expressions_SlimeAssignmentExpressionCstToAst | null = null as any;
  __qin_field_unaryExpressionCstToAst: com_slime_parser_cstToAst_expressions_SlimeUnaryExpressionCstToAst | null = null as any;
  __qin_field_callExpressionCstToAst: com_slime_parser_cstToAst_expressions_SlimeCallExpressionCstToAst | null = null as any;
  __qin_field_memberExpressionCstToAst: com_slime_parser_cstToAst_expressions_SlimeMemberExpressionCstToAst | null = null as any;
  __qin_field_newExpressionCstToAst: com_slime_parser_cstToAst_expressions_SlimeNewExpressionCstToAst | null = null as any;
  __qin_field_optionalExpressionCstToAst: com_slime_parser_cstToAst_expressions_SlimeOptionalExpressionCstToAst | null = null as any;
  __qin_field_statementCstToAst: com_slime_parser_cstToAst_statements_SlimeStatementCstToAst | null = null as any;
  __qin_field_variableCstToAst: com_slime_parser_cstToAst_statements_SlimeVariableCstToAst | null = null as any;
  __qin_field_functionBodyCstToAst: com_slime_parser_cstToAst_components_SlimeFunctionBodyCstToAst | null = null as any;
  __qin_field_methodDefinitionCstToAst: com_slime_parser_cstToAst_class__SlimeMethodDefinitionCstToAst | null = null as any;
  __qin_field_classCstToAst: com_slime_parser_cstToAst_class__SlimeClassCstToAst | null = null as any;
  __qin_field_classExpressionCstToAst: com_slime_parser_cstToAst_class__SlimeClassExpressionCstToAst | null = null as any;
  __qin_field_controlFlowCstToAst: com_slime_parser_cstToAst_statements_SlimeControlFlowCstToAst | null = null as any;
  __qin_field_loopCstToAst: com_slime_parser_cstToAst_statements_SlimeLoopCstToAst | null = null as any;
  __qin_field_functionCstToAst: com_slime_parser_cstToAst_function_SlimeFunctionCstToAst | null = null as any;
  __qin_field_functionExpressionCstToAst: com_slime_parser_cstToAst_function_SlimeFunctionExpressionCstToAst | null = null as any;
  __qin_field_arrowFunctionCstToAst: com_slime_parser_cstToAst_function_SlimeArrowFunctionCstToAst | null = null as any;
  __qin_field_breakContinueCstToAst: com_slime_parser_cstToAst_statements_SlimeBreakContinueCstToAst | null = null as any;
  __qin_field_functionParameterCstToAst: com_slime_parser_cstToAst_components_SlimeFunctionParameterCstToAst | null = null as any;
  __qin_field_bindingPatternCstToAst: com_slime_parser_cstToAst_components_SlimeBindingPatternCstToAst | null = null as any;
  __qin_field_compoundLiteralCstToAst: com_slime_parser_cstToAst_literal_SlimeCompoundLiteralCstToAst | null = null as any;
  __qin_field_exportCstToAst: com_slime_parser_cstToAst_module_SlimeExportCstToAst | null = null as any;
  __qin_field_templateLiteralCstToAst: com_slime_parser_cstToAst_literal_SlimeTemplateLiteralCstToAst | null = null as any;
  __qin_field_tsDecoratorCstToAst: com_slime_parser_cstToAst_typescript_SlimeTSDecoratorCstToAst | null = null as any;
  __qin_field_switchStatementCstToAst: com_slime_parser_cstToAst_statements_SlimeSwitchStatementCstToAst | null = null as any;
  __qin_field_otherStatementCstToAst: com_slime_parser_cstToAst_statements_SlimeOtherStatementCstToAst | null = null as any;
  __qin_field_yieldAwaitCstToAst: com_slime_parser_cstToAst_expressions_SlimeYieldAwaitCstToAst | null = null as any;
  __qin_field_tryStatementCstToAst: com_slime_parser_cstToAst_statements_SlimeTryStatementCstToAst | null = null as any;
  constructor(...__qin_args: any[]) {
    if (__qin_args.length !== 0) {
      throw new Error("Unsupported Java constructor arity: SlimeCstToAstUtils/" + __qin_args.length);
    }
    this.__qin_field_programCstToAst = new com_slime_parser_cstToAst_SlimeProgramCstToAst(this);
    this.__qin_field_expressionCstToAst = new com_slime_parser_cstToAst_expressions_SlimeExpressionCstToAst(this);
    this.__qin_field_primaryExpressionCstToAst = new com_slime_parser_cstToAst_expressions_SlimePrimaryExpressionCstToAst(this);
    this.__qin_field_assignmentExpressionCstToAst = new com_slime_parser_cstToAst_expressions_SlimeAssignmentExpressionCstToAst(this);
    this.__qin_field_unaryExpressionCstToAst = new com_slime_parser_cstToAst_expressions_SlimeUnaryExpressionCstToAst(this);
    this.__qin_field_callExpressionCstToAst = new com_slime_parser_cstToAst_expressions_SlimeCallExpressionCstToAst(this);
    this.__qin_field_memberExpressionCstToAst = new com_slime_parser_cstToAst_expressions_SlimeMemberExpressionCstToAst(this);
    this.__qin_field_newExpressionCstToAst = new com_slime_parser_cstToAst_expressions_SlimeNewExpressionCstToAst(this);
    this.__qin_field_optionalExpressionCstToAst = new com_slime_parser_cstToAst_expressions_SlimeOptionalExpressionCstToAst(this);
    this.__qin_field_statementCstToAst = new com_slime_parser_cstToAst_statements_SlimeStatementCstToAst(this);
    this.__qin_field_variableCstToAst = new com_slime_parser_cstToAst_statements_SlimeVariableCstToAst(this);
    this.__qin_field_functionBodyCstToAst = new com_slime_parser_cstToAst_components_SlimeFunctionBodyCstToAst(this);
    this.__qin_field_methodDefinitionCstToAst = new com_slime_parser_cstToAst_class__SlimeMethodDefinitionCstToAst(this);
    this.__qin_field_classCstToAst = new com_slime_parser_cstToAst_class__SlimeClassCstToAst(this);
    this.__qin_field_classExpressionCstToAst = new com_slime_parser_cstToAst_class__SlimeClassExpressionCstToAst(this);
    this.__qin_field_controlFlowCstToAst = new com_slime_parser_cstToAst_statements_SlimeControlFlowCstToAst(this);
    this.__qin_field_loopCstToAst = new com_slime_parser_cstToAst_statements_SlimeLoopCstToAst(this);
    this.__qin_field_functionCstToAst = new com_slime_parser_cstToAst_function_SlimeFunctionCstToAst(this);
    this.__qin_field_functionExpressionCstToAst = new com_slime_parser_cstToAst_function_SlimeFunctionExpressionCstToAst(this);
    this.__qin_field_arrowFunctionCstToAst = new com_slime_parser_cstToAst_function_SlimeArrowFunctionCstToAst(this);
    this.__qin_field_breakContinueCstToAst = new com_slime_parser_cstToAst_statements_SlimeBreakContinueCstToAst(this);
    this.__qin_field_functionParameterCstToAst = new com_slime_parser_cstToAst_components_SlimeFunctionParameterCstToAst(this);
    this.__qin_field_bindingPatternCstToAst = new com_slime_parser_cstToAst_components_SlimeBindingPatternCstToAst(this);
    this.__qin_field_compoundLiteralCstToAst = new com_slime_parser_cstToAst_literal_SlimeCompoundLiteralCstToAst(this);
    this.__qin_field_exportCstToAst = new com_slime_parser_cstToAst_module_SlimeExportCstToAst(this);
    this.__qin_field_templateLiteralCstToAst = new com_slime_parser_cstToAst_literal_SlimeTemplateLiteralCstToAst(this);
    this.__qin_field_tsDecoratorCstToAst = new com_slime_parser_cstToAst_typescript_SlimeTSDecoratorCstToAst(this);
    this.__qin_field_switchStatementCstToAst = new com_slime_parser_cstToAst_statements_SlimeSwitchStatementCstToAst(this);
    this.__qin_field_otherStatementCstToAst = new com_slime_parser_cstToAst_statements_SlimeOtherStatementCstToAst(this);
    this.__qin_field_yieldAwaitCstToAst = new com_slime_parser_cstToAst_expressions_SlimeYieldAwaitCstToAst(this);
    this.__qin_field_tryStatementCstToAst = new com_slime_parser_cstToAst_statements_SlimeTryStatementCstToAst(this);
  }
  resetState(): any {
    return null;
  }
  toProgram(cst: com_subhuti_struct_SubhutiCst): any {
    this.resetState();
    return this.__qin_field_programCstToAst.createProgramAst(cst);
  }
  createIdentifierAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_identifier_SlimeIdentifierCstToAst.createIdentifierAst(cst);
  }
  createBindingIdentifierAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_identifier_SlimeIdentifierCstToAst.createBindingIdentifierAst(cst);
  }
  createBooleanLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_literal_SlimeLiteralCstToAst.createBooleanLiteralAst(cst);
  }
  createNumericLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_literal_SlimeLiteralCstToAst.createNumericLiteralAst(cst);
  }
  createStringLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_literal_SlimeLiteralCstToAst.createStringLiteralAst(cst);
  }
  createPrimaryExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_primaryExpressionCstToAst.createPrimaryExpressionAst(cst);
  }
  createBinaryExpressionAst(left: com_slime_ast_Expression, op: string, right: com_slime_ast_Expression, cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_expressions_SlimeBinaryExpressionCstToAst.createBinaryExpressionAst(left, op, right, cst);
  }
  createUnaryExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_unaryExpressionCstToAst.createUnaryExpressionAst(cst);
  }
  createUpdateExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_unaryExpressionCstToAst.createUpdateExpressionAst(cst);
  }
  createAssignmentExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_assignmentExpressionCstToAst.createAssignmentExpressionAst(cst);
  }
  createCallExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_callExpressionCstToAst.createCallExpressionAst(cst);
  }
  createMemberExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_memberExpressionCstToAst.createMemberExpressionAst(cst);
  }
  createNewExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_newExpressionCstToAst.createNewExpressionAst(cst);
  }
  createOptionalExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_optionalExpressionCstToAst.createOptionalExpressionAst(cst);
  }
  createOptionalChainAst(object: com_slime_ast_Expression, chainCst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_optionalExpressionCstToAst.createOptionalChainAst(object, chainCst);
  }
  createExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_expressionCstToAst.createExpressionAst(cst);
  }
  createExpressionAstUncached(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_expressionCstToAst.createExpressionAst(cst);
  }
  createTemplateLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_templateLiteralCstToAst.createTemplateLiteralAst(cst);
  }
  createAwaitExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_yieldAwaitCstToAst.createAwaitExpressionAst(cst);
  }
  createYieldExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_yieldAwaitCstToAst.createYieldExpressionAst(cst);
  }
  createExpressionStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_statementCstToAst.createExpressionStatementAst(cst);
  }
  createBlockStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_statementCstToAst.createBlockStatementAst(cst);
  }
  createStatementListItemAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_programCstToAst.createStatementListItemAst(cst);
  }
  createDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_statementCstToAst.createStatementAst(cst);
  }
  createDeclarationAstBase(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_statementCstToAst.createStatementAst(cst);
  }
  createVariableDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_variableCstToAst.createVariableDeclarationAst(cst);
  }
  createInitializerExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_variableCstToAst.createInitializerExpressionAst(cst);
  }
  createLexicalBindingAst(cst: com_subhuti_struct_SubhutiCst): any {
    let variableDeclaration: any = null;
    let declaration: any = this.__qin_field_variableCstToAst.createVariableDeclarationAst(cst);
    if ((() => {
      if ((() => {
      if ((() => { const __qin_pattern_value = declaration; return __qin_instanceof__(__qin_pattern_value, com_slime_ast_nodes_declarations_VariableDeclaration) && (variableDeclaration = __qin_pattern_value, true); })()) {
        return __qin_binary__("!=", variableDeclaration.declarations(), null);
      }
      return false;
    })()) {
        return (() => {
      if (variableDeclaration.declarations().isEmpty()) {
        return false;
      }
      return true;
    })();
      }
      return false;
    })()) {
      return variableDeclaration.declarations().get(0.0);
    }
    return com_slime_parser_cstToAst_SlimeAstCreateUtils.createVariableDeclarator(com_slime_parser_cstToAst_SlimeAstCreateUtils.createIdentifier("_", com_slime_parser_cstToAst_SlimeAstCreateUtils.resolveSubhutiLocation(cst)), null, com_slime_parser_cstToAst_SlimeAstCreateUtils.resolveSubhutiLocation(cst));
  }
  createBindingTargetAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_variableCstToAst.createBindingTargetAst(cst);
  }
  createBindingPatternAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_bindingPatternCstToAst.createBindingPatternAst(cst);
  }
  createArrayBindingPatternAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_bindingPatternCstToAst.createArrayBindingPatternAst(cst);
  }
  createObjectBindingPatternAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_bindingPatternCstToAst.createObjectBindingPatternAst(cst);
  }
  createIfStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_controlFlowCstToAst.createIfStatementAst(cst);
  }
  createReturnStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_controlFlowCstToAst.createReturnStatementAst(cst);
  }
  createForStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_loopCstToAst.createForStatementAst(cst);
  }
  createForInStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_loopCstToAst.createForInStatementAst(cst);
  }
  createForOfStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_loopCstToAst.createForOfStatementAst(cst);
  }
  createForInOfStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_loopCstToAst.createForInOfStatementAst(cst);
  }
  createWhileStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_loopCstToAst.createWhileStatementAst(cst);
  }
  createDoWhileStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_loopCstToAst.createDoWhileStatementAst(cst);
  }
  createBreakStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_breakContinueCstToAst.createBreakStatementAst(cst);
  }
  createContinueStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_breakContinueCstToAst.createContinueStatementAst(cst);
  }
  createThrowStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_breakContinueCstToAst.createThrowStatementAst(cst);
  }
  createSwitchStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_switchStatementCstToAst.createSwitchStatementAst(cst);
  }
  createTryStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_tryStatementCstToAst.createTryStatementAst(cst);
  }
  createWithStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_otherStatementCstToAst.createWithStatementAst(cst);
  }
  createDebuggerStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_otherStatementCstToAst.createDebuggerStatementAst(cst);
  }
  createLabeledStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_otherStatementCstToAst.createLabeledStatementAst(cst);
  }
  createEmptyStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_otherStatementCstToAst.createEmptyStatementAst(cst);
  }
  createFunctionDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_functionCstToAst.createFunctionDeclarationAst(cst);
  }
  createFunctionBodyAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_functionBodyCstToAst.createFunctionBodyAst(cst);
  }
  createFunctionBodyAstBase(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_functionBodyCstToAst.createFunctionBodyAst(cst);
  }
  createFormalParametersAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_functionParameterCstToAst.createFormalParameters(cst);
  }
  createFormalParameterMetadataAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_functionParameterCstToAst.createFormalParameterMetadata(cst);
  }
  createConciseBodyAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_functionBodyCstToAst.createConciseBodyAst(cst);
  }
  createConciseBodyAstBase(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_functionBodyCstToAst.createConciseBodyAst(cst);
  }
  createFunctionExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_functionExpressionCstToAst.createFunctionExpressionAst(cst);
  }
  createGeneratorExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_functionExpressionCstToAst.createGeneratorExpressionAst(cst);
  }
  createAsyncFunctionExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_functionExpressionCstToAst.createAsyncFunctionExpressionAst(cst);
  }
  createAsyncGeneratorExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_functionExpressionCstToAst.createAsyncGeneratorExpressionAst(cst);
  }
  createArrowFunctionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_arrowFunctionCstToAst.createArrowFunctionAst(cst);
  }
  createObjectLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_compoundLiteralCstToAst.createObjectLiteral(cst);
  }
  createArrayLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_compoundLiteralCstToAst.createArrayLiteral(cst);
  }
  createClassDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_classCstToAst.createClassDeclarationAst(cst);
  }
  createMethodDefinitionAst(cst: com_subhuti_struct_SubhutiCst, isStatic: boolean): any {
    return this.__qin_field_methodDefinitionCstToAst.createMethodDefinitionAst(cst, isStatic);
  }
  createClassExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_classExpressionCstToAst.createClassExpressionAst(cst);
  }
  createClassHeritageAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_classCstToAst.createClassHeritageAst(cst);
  }
  createClassBodyAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_classCstToAst.createClassBodyAst(cst);
  }
  createImportDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_module_SlimeImportCstToAst.createImportDeclarationAst(cst);
  }
  createExportDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_exportCstToAst.createExportDeclarationAst(cst);
  }
  createDecoratorsAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_tsDecoratorCstToAst.createDecoratorsAst(cst);
  }
  createDecoratorAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_tsDecoratorCstToAst.createDecoratorAst(cst);
  }
  createTSTypeAnnotationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_typescript_SlimeTSTypeCstToAst.createTSTypeAnnotationAst(cst);
  }
  createTSTypeAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_typescript_SlimeTSTypeCstToAst.createTSTypeAst(cst);
  }
  createTSInterfaceDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_typescript_SlimeTSDeclarationCstToAst.createTSInterfaceDeclarationAst(cst);
  }
  createTSTypeAliasDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_typescript_SlimeTSDeclarationCstToAst.createTSTypeAliasDeclarationAst(cst);
  }
  createTSEnumDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
    return com_slime_parser_cstToAst_typescript_SlimeTSDeclarationCstToAst.createTSEnumDeclarationAst(cst);
  }
  createProgramAst(cst: com_subhuti_struct_SubhutiCst): any {
    return this.__qin_field_programCstToAst.createProgramAst(cst);
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
