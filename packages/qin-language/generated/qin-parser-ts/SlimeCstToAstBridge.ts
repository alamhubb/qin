// Generated Slime CST-to-AST bridge by Qin. Source Java: com.slime.parser.cstToAst.SlimeCstToAstUtils
import { com_slime_parser_cstToAst_SlimeCstToAstUtils as __QinGeneratedSlimeCstToAstUtils } from "./com/slime/parser/cstToAst/SlimeCstToAstUtils.ts";

import { com_slime_ast_nodes_misc_Program } from "./com/slime/ast/nodes/misc/Program.ts";
import { com_subhuti_struct_SubhutiCst } from "./com/subhuti/struct/SubhutiCst.ts";
import { com_slime_ast_nodes_expressions_Identifier } from "./com/slime/ast/nodes/expressions/Identifier.ts";
import { com_slime_ast_nodes_expressions_Literal } from "./com/slime/ast/nodes/expressions/Literal.ts";
import { com_slime_ast_Expression } from "./com/slime/ast/Expression.ts";
import { com_slime_ast_nodes_expressions_TemplateLiteral } from "./com/slime/ast/nodes/expressions/TemplateLiteral.ts";
import { com_slime_ast_nodes_expressions_AwaitExpression } from "./com/slime/ast/nodes/expressions/AwaitExpression.ts";
import { com_slime_ast_nodes_expressions_YieldExpression } from "./com/slime/ast/nodes/expressions/YieldExpression.ts";
import { com_slime_ast_nodes_statements_ExpressionStatement } from "./com/slime/ast/nodes/statements/ExpressionStatement.ts";
import { com_slime_ast_nodes_statements_BlockStatement } from "./com/slime/ast/nodes/statements/BlockStatement.ts";
import { com_slime_ast_nodes_misc_VariableDeclarator } from "./com/slime/ast/nodes/misc/VariableDeclarator.ts";
import { com_slime_ast_Pattern } from "./com/slime/ast/Pattern.ts";
import { com_slime_ast_nodes_patterns_ArrayPattern } from "./com/slime/ast/nodes/patterns/ArrayPattern.ts";
import { com_slime_ast_nodes_patterns_ObjectPattern } from "./com/slime/ast/nodes/patterns/ObjectPattern.ts";
import { com_slime_ast_nodes_statements_IfStatement } from "./com/slime/ast/nodes/statements/IfStatement.ts";
import { com_slime_ast_nodes_statements_ReturnStatement } from "./com/slime/ast/nodes/statements/ReturnStatement.ts";
import { com_slime_ast_nodes_statements_ForStatement } from "./com/slime/ast/nodes/statements/ForStatement.ts";
import { com_slime_ast_Statement } from "./com/slime/ast/Statement.ts";
import { com_slime_ast_nodes_statements_WhileStatement } from "./com/slime/ast/nodes/statements/WhileStatement.ts";
import { com_slime_ast_nodes_statements_DoWhileStatement } from "./com/slime/ast/nodes/statements/DoWhileStatement.ts";
import { com_slime_ast_nodes_statements_BreakStatement } from "./com/slime/ast/nodes/statements/BreakStatement.ts";
import { com_slime_ast_nodes_statements_ContinueStatement } from "./com/slime/ast/nodes/statements/ContinueStatement.ts";
import { com_slime_ast_nodes_statements_ThrowStatement } from "./com/slime/ast/nodes/statements/ThrowStatement.ts";
import { com_slime_ast_nodes_statements_SwitchStatement } from "./com/slime/ast/nodes/statements/SwitchStatement.ts";
import { com_slime_ast_nodes_statements_TryStatement } from "./com/slime/ast/nodes/statements/TryStatement.ts";
import { com_slime_ast_nodes_statements_WithStatement } from "./com/slime/ast/nodes/statements/WithStatement.ts";
import { com_slime_ast_nodes_statements_DebuggerStatement } from "./com/slime/ast/nodes/statements/DebuggerStatement.ts";
import { com_slime_ast_nodes_statements_LabeledStatement } from "./com/slime/ast/nodes/statements/LabeledStatement.ts";
import { com_slime_ast_nodes_statements_EmptyStatement } from "./com/slime/ast/nodes/statements/EmptyStatement.ts";
import { com_slime_ast_nodes_misc_FunctionParameter } from "./com/slime/ast/nodes/misc/FunctionParameter.ts";
import { com_slime_ast_nodes_expressions_FunctionExpression } from "./com/slime/ast/nodes/expressions/FunctionExpression.ts";
import { com_slime_ast_nodes_expressions_ArrowFunctionExpression } from "./com/slime/ast/nodes/expressions/ArrowFunctionExpression.ts";
import { com_slime_ast_nodes_expressions_ObjectExpression } from "./com/slime/ast/nodes/expressions/ObjectExpression.ts";
import { com_slime_ast_nodes_expressions_ArrayExpression } from "./com/slime/ast/nodes/expressions/ArrayExpression.ts";
import { com_slime_ast_nodes_misc_MethodDefinition } from "./com/slime/ast/nodes/misc/MethodDefinition.ts";
import { com_slime_ast_nodes_expressions_ClassExpression } from "./com/slime/ast/nodes/expressions/ClassExpression.ts";
import { com_slime_ast_nodes_misc_ClassBody } from "./com/slime/ast/nodes/misc/ClassBody.ts";
import { com_slime_ast_nodes_misc_Decorator } from "./com/slime/ast/nodes/misc/Decorator.ts";

export class SlimeCstToAst extends __QinGeneratedSlimeCstToAstUtils {
  constructor() {
    super();
    __qinBindSlimeCstToAstTransformer(this);
    registerSlimeCstToAstUtil(this);
  }
  resetState(): void {
  return super.resetState();
  }
  toProgram(cst: com_subhuti_struct_SubhutiCst): any {
  return super.toProgram(cst);
  }
  createIdentifierAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createIdentifierAst(cst);
  }
  createBindingIdentifierAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createBindingIdentifierAst(cst);
  }
  createBooleanLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createBooleanLiteralAst(cst);
  }
  createNumericLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createNumericLiteralAst(cst);
  }
  createStringLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createStringLiteralAst(cst);
  }
  createPrimaryExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createPrimaryExpressionAst(cst);
  }
  createBinaryExpressionAst(left: com_slime_ast_Expression, op: string, right: com_slime_ast_Expression, cst: com_subhuti_struct_SubhutiCst): any {
  return super.createBinaryExpressionAst(left, op, right, cst);
  }
  createUnaryExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createUnaryExpressionAst(cst);
  }
  createUpdateExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createUpdateExpressionAst(cst);
  }
  createAssignmentExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createAssignmentExpressionAst(cst);
  }
  createCallExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createCallExpressionAst(cst);
  }
  createMemberExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createMemberExpressionAst(cst);
  }
  createNewExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createNewExpressionAst(cst);
  }
  createOptionalExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createOptionalExpressionAst(cst);
  }
  createOptionalChainAst(object: com_slime_ast_Expression, chainCst: com_subhuti_struct_SubhutiCst): any {
  return super.createOptionalChainAst(object, chainCst);
  }
  createExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createExpressionAst(cst);
  }
  createExpressionAstUncached(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createExpressionAstUncached(cst);
  }
  createTemplateLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createTemplateLiteralAst(cst);
  }
  createAwaitExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createAwaitExpressionAst(cst);
  }
  createYieldExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createYieldExpressionAst(cst);
  }
  createExpressionStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createExpressionStatementAst(cst);
  }
  createBlockStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createBlockStatementAst(cst);
  }
  createStatementListItemAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createStatementListItemAst(cst);
  }
  createDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createDeclarationAst(cst);
  }
  createDeclarationAstBase(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createDeclarationAstBase(cst);
  }
  createVariableDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createVariableDeclarationAst(cst);
  }
  createInitializerExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createInitializerExpressionAst(cst);
  }
  createLexicalBindingAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createLexicalBindingAst(cst);
  }
  createBindingTargetAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createBindingTargetAst(cst);
  }
  createBindingPatternAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createBindingPatternAst(cst);
  }
  createArrayBindingPatternAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createArrayBindingPatternAst(cst);
  }
  createObjectBindingPatternAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createObjectBindingPatternAst(cst);
  }
  createIfStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createIfStatementAst(cst);
  }
  createReturnStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createReturnStatementAst(cst);
  }
  createForStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createForStatementAst(cst);
  }
  createForInStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createForInStatementAst(cst);
  }
  createForOfStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createForOfStatementAst(cst);
  }
  createForInOfStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createForInOfStatementAst(cst);
  }
  createWhileStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createWhileStatementAst(cst);
  }
  createDoWhileStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createDoWhileStatementAst(cst);
  }
  createBreakStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createBreakStatementAst(cst);
  }
  createContinueStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createContinueStatementAst(cst);
  }
  createThrowStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createThrowStatementAst(cst);
  }
  createSwitchStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createSwitchStatementAst(cst);
  }
  createTryStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createTryStatementAst(cst);
  }
  createWithStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createWithStatementAst(cst);
  }
  createDebuggerStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createDebuggerStatementAst(cst);
  }
  createLabeledStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createLabeledStatementAst(cst);
  }
  createEmptyStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createEmptyStatementAst(cst);
  }
  createFunctionDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createFunctionDeclarationAst(cst);
  }
  createFunctionBodyAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createFunctionBodyAst(cst);
  }
  createFunctionBodyAstBase(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createFunctionBodyAstBase(cst);
  }
  createFormalParametersAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createFormalParametersAst(cst);
  }
  createFormalParameterMetadataAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createFormalParameterMetadataAst(cst);
  }
  createConciseBodyAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createConciseBodyAst(cst);
  }
  createConciseBodyAstBase(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createConciseBodyAstBase(cst);
  }
  createFunctionExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createFunctionExpressionAst(cst);
  }
  createGeneratorExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createGeneratorExpressionAst(cst);
  }
  createAsyncFunctionExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createAsyncFunctionExpressionAst(cst);
  }
  createAsyncGeneratorExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createAsyncGeneratorExpressionAst(cst);
  }
  createArrowFunctionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createArrowFunctionAst(cst);
  }
  createObjectLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createObjectLiteralAst(cst);
  }
  createArrayLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createArrayLiteralAst(cst);
  }
  createClassDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createClassDeclarationAst(cst);
  }
  createMethodDefinitionAst(cst: com_subhuti_struct_SubhutiCst, isStatic: boolean): any {
  return super.createMethodDefinitionAst(cst, isStatic);
  }
  createClassExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createClassExpressionAst(cst);
  }
  createClassHeritageAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createClassHeritageAst(cst);
  }
  createClassBodyAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createClassBodyAst(cst);
  }
  createImportDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createImportDeclarationAst(cst);
  }
  createExportDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createExportDeclarationAst(cst);
  }
  createDecoratorsAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createDecoratorsAst(cst);
  }
  createDecoratorAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createDecoratorAst(cst);
  }
  createTSTypeAnnotationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createTSTypeAnnotationAst(cst);
  }
  createTSTypeAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createTSTypeAst(cst);
  }
  createTSInterfaceDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createTSInterfaceDeclarationAst(cst);
  }
  createTSTypeAliasDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createTSTypeAliasDeclarationAst(cst);
  }
  createTSEnumDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createTSEnumDeclarationAst(cst);
  }
  createProgramAst(cst: com_subhuti_struct_SubhutiCst): any {
  return super.createProgramAst(cst);
  }
}

function __qinBindSlimeCstToAstTransformer(instance: SlimeCstToAst): void {
  if (instance.__qin_field_programCstToAst !== null) {
    instance.__qin_field_programCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_expressionCstToAst !== null) {
    instance.__qin_field_expressionCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_primaryExpressionCstToAst !== null) {
    instance.__qin_field_primaryExpressionCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_assignmentExpressionCstToAst !== null) {
    instance.__qin_field_assignmentExpressionCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_unaryExpressionCstToAst !== null) {
    instance.__qin_field_unaryExpressionCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_callExpressionCstToAst !== null) {
    instance.__qin_field_callExpressionCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_memberExpressionCstToAst !== null) {
    instance.__qin_field_memberExpressionCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_newExpressionCstToAst !== null) {
    instance.__qin_field_newExpressionCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_optionalExpressionCstToAst !== null) {
    instance.__qin_field_optionalExpressionCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_statementCstToAst !== null) {
    instance.__qin_field_statementCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_variableCstToAst !== null) {
    instance.__qin_field_variableCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_functionBodyCstToAst !== null) {
    instance.__qin_field_functionBodyCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_methodDefinitionCstToAst !== null) {
    instance.__qin_field_methodDefinitionCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_classCstToAst !== null) {
    instance.__qin_field_classCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_classExpressionCstToAst !== null) {
    instance.__qin_field_classExpressionCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_controlFlowCstToAst !== null) {
    instance.__qin_field_controlFlowCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_loopCstToAst !== null) {
    instance.__qin_field_loopCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_functionCstToAst !== null) {
    instance.__qin_field_functionCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_functionExpressionCstToAst !== null) {
    instance.__qin_field_functionExpressionCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_arrowFunctionCstToAst !== null) {
    instance.__qin_field_arrowFunctionCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_breakContinueCstToAst !== null) {
    instance.__qin_field_breakContinueCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_functionParameterCstToAst !== null) {
    instance.__qin_field_functionParameterCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_bindingPatternCstToAst !== null) {
    instance.__qin_field_bindingPatternCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_compoundLiteralCstToAst !== null) {
    instance.__qin_field_compoundLiteralCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_exportCstToAst !== null) {
    instance.__qin_field_exportCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_templateLiteralCstToAst !== null) {
    instance.__qin_field_templateLiteralCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_tsDecoratorCstToAst !== null) {
    instance.__qin_field_tsDecoratorCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_switchStatementCstToAst !== null) {
    instance.__qin_field_switchStatementCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_otherStatementCstToAst !== null) {
    instance.__qin_field_otherStatementCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_yieldAwaitCstToAst !== null) {
    instance.__qin_field_yieldAwaitCstToAst.__qin_field_transformer = instance;
  }
  if (instance.__qin_field_tryStatementCstToAst !== null) {
    instance.__qin_field_tryStatementCstToAst.__qin_field_transformer = instance;
  }
}

var __qinSlimeCstToAstUtils: SlimeCstToAst = new SlimeCstToAst();
export function registerSlimeCstToAstUtil(instance: SlimeCstToAst): void {
  __qinSlimeCstToAstUtils = instance;
}

export class SlimeCstToAstUtils {
  static resetState(): void {
  return __qinSlimeCstToAstUtils.resetState();
  }
  static toProgram(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.toProgram(cst);
  }
  static createIdentifierAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createIdentifierAst(cst);
  }
  static createBindingIdentifierAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createBindingIdentifierAst(cst);
  }
  static createBooleanLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createBooleanLiteralAst(cst);
  }
  static createNumericLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createNumericLiteralAst(cst);
  }
  static createStringLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createStringLiteralAst(cst);
  }
  static createPrimaryExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createPrimaryExpressionAst(cst);
  }
  static createBinaryExpressionAst(left: com_slime_ast_Expression, op: string, right: com_slime_ast_Expression, cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createBinaryExpressionAst(left, op, right, cst);
  }
  static createUnaryExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createUnaryExpressionAst(cst);
  }
  static createUpdateExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createUpdateExpressionAst(cst);
  }
  static createAssignmentExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createAssignmentExpressionAst(cst);
  }
  static createCallExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createCallExpressionAst(cst);
  }
  static createMemberExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createMemberExpressionAst(cst);
  }
  static createNewExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createNewExpressionAst(cst);
  }
  static createOptionalExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createOptionalExpressionAst(cst);
  }
  static createOptionalChainAst(object: com_slime_ast_Expression, chainCst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createOptionalChainAst(object, chainCst);
  }
  static createExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createExpressionAst(cst);
  }
  static createExpressionAstUncached(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createExpressionAstUncached(cst);
  }
  static createTemplateLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createTemplateLiteralAst(cst);
  }
  static createAwaitExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createAwaitExpressionAst(cst);
  }
  static createYieldExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createYieldExpressionAst(cst);
  }
  static createExpressionStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createExpressionStatementAst(cst);
  }
  static createBlockStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createBlockStatementAst(cst);
  }
  static createStatementListItemAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createStatementListItemAst(cst);
  }
  static createDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createDeclarationAst(cst);
  }
  static createDeclarationAstBase(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createDeclarationAstBase(cst);
  }
  static createVariableDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createVariableDeclarationAst(cst);
  }
  static createInitializerExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createInitializerExpressionAst(cst);
  }
  static createLexicalBindingAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createLexicalBindingAst(cst);
  }
  static createBindingTargetAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createBindingTargetAst(cst);
  }
  static createBindingPatternAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createBindingPatternAst(cst);
  }
  static createArrayBindingPatternAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createArrayBindingPatternAst(cst);
  }
  static createObjectBindingPatternAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createObjectBindingPatternAst(cst);
  }
  static createIfStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createIfStatementAst(cst);
  }
  static createReturnStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createReturnStatementAst(cst);
  }
  static createForStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createForStatementAst(cst);
  }
  static createForInStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createForInStatementAst(cst);
  }
  static createForOfStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createForOfStatementAst(cst);
  }
  static createForInOfStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createForInOfStatementAst(cst);
  }
  static createWhileStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createWhileStatementAst(cst);
  }
  static createDoWhileStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createDoWhileStatementAst(cst);
  }
  static createBreakStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createBreakStatementAst(cst);
  }
  static createContinueStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createContinueStatementAst(cst);
  }
  static createThrowStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createThrowStatementAst(cst);
  }
  static createSwitchStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createSwitchStatementAst(cst);
  }
  static createTryStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createTryStatementAst(cst);
  }
  static createWithStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createWithStatementAst(cst);
  }
  static createDebuggerStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createDebuggerStatementAst(cst);
  }
  static createLabeledStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createLabeledStatementAst(cst);
  }
  static createEmptyStatementAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createEmptyStatementAst(cst);
  }
  static createFunctionDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createFunctionDeclarationAst(cst);
  }
  static createFunctionBodyAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createFunctionBodyAst(cst);
  }
  static createFunctionBodyAstBase(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createFunctionBodyAstBase(cst);
  }
  static createFormalParametersAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createFormalParametersAst(cst);
  }
  static createFormalParameterMetadataAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createFormalParameterMetadataAst(cst);
  }
  static createConciseBodyAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createConciseBodyAst(cst);
  }
  static createConciseBodyAstBase(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createConciseBodyAstBase(cst);
  }
  static createFunctionExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createFunctionExpressionAst(cst);
  }
  static createGeneratorExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createGeneratorExpressionAst(cst);
  }
  static createAsyncFunctionExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createAsyncFunctionExpressionAst(cst);
  }
  static createAsyncGeneratorExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createAsyncGeneratorExpressionAst(cst);
  }
  static createArrowFunctionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createArrowFunctionAst(cst);
  }
  static createObjectLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createObjectLiteralAst(cst);
  }
  static createArrayLiteralAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createArrayLiteralAst(cst);
  }
  static createClassDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createClassDeclarationAst(cst);
  }
  static createMethodDefinitionAst(cst: com_subhuti_struct_SubhutiCst, isStatic: boolean): any {
  return __qinSlimeCstToAstUtils.createMethodDefinitionAst(cst, isStatic);
  }
  static createClassExpressionAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createClassExpressionAst(cst);
  }
  static createClassHeritageAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createClassHeritageAst(cst);
  }
  static createClassBodyAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createClassBodyAst(cst);
  }
  static createImportDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createImportDeclarationAst(cst);
  }
  static createExportDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createExportDeclarationAst(cst);
  }
  static createDecoratorsAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createDecoratorsAst(cst);
  }
  static createDecoratorAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createDecoratorAst(cst);
  }
  static createTSTypeAnnotationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createTSTypeAnnotationAst(cst);
  }
  static createTSTypeAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createTSTypeAst(cst);
  }
  static createTSInterfaceDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createTSInterfaceDeclarationAst(cst);
  }
  static createTSTypeAliasDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createTSTypeAliasDeclarationAst(cst);
  }
  static createTSEnumDeclarationAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createTSEnumDeclarationAst(cst);
  }
  static createProgramAst(cst: com_subhuti_struct_SubhutiCst): any {
  return __qinSlimeCstToAstUtils.createProgramAst(cst);
  }
}
