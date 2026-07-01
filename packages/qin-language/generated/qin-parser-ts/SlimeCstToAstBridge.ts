// Generated Slime CST-to-AST bridge by Qin. Source Java: com.slime.parser.cstToAst.SlimeCstToAstUtils
import { com_slime_parser_cstToAst_SlimeCstToAstUtils as __QinGeneratedSlimeCstToAstUtils } from "./com/slime/parser/cstToAst/SlimeCstToAstUtils.ts";

export class SlimeCstToAst extends __QinGeneratedSlimeCstToAstUtils {
  constructor(...args: any[]) {
    if (args.length !== 0) {
      throw new Error("Unsupported SlimeCstToAst constructor arity: " + args.length);
    }
    super();
    __qinBindSlimeCstToAstTransformer(this);
    registerSlimeCstToAstUtil(this);
  }
}

function __qinBindSlimeCstToAstTransformer(instance: any): void {
  for (const key of Object.keys(instance)) {
    const helper = instance[key];
    if (helper && typeof helper === "object" && "__qin_field_transformer" in helper) {
      helper.__qin_field_transformer = instance;
    }
  }
}

let __qinSlimeCstToAstUtils: SlimeCstToAst;
export function registerSlimeCstToAstUtil(instance: SlimeCstToAst): void {
  __qinSlimeCstToAstUtils = instance;
}

export const SlimeCstToAstUtils = {} as SlimeCstToAst;
const __qinSlimeCstToAstFacade: any = SlimeCstToAstUtils;
__qinSlimeCstToAstFacade.resetState = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.resetState(...args);
};
__qinSlimeCstToAstFacade.toProgram = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.toProgram(...args);
};
__qinSlimeCstToAstFacade.createIdentifierAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createIdentifierAst(...args);
};
__qinSlimeCstToAstFacade.createBindingIdentifierAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createBindingIdentifierAst(...args);
};
__qinSlimeCstToAstFacade.createBooleanLiteralAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createBooleanLiteralAst(...args);
};
__qinSlimeCstToAstFacade.createNumericLiteralAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createNumericLiteralAst(...args);
};
__qinSlimeCstToAstFacade.createStringLiteralAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createStringLiteralAst(...args);
};
__qinSlimeCstToAstFacade.createPrimaryExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createPrimaryExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createBinaryExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createBinaryExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createUnaryExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createUnaryExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createUpdateExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createUpdateExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createAssignmentExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createAssignmentExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createCallExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createCallExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createMemberExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createMemberExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createNewExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createNewExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createOptionalExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createOptionalExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createOptionalChainAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createOptionalChainAst(...args);
};
__qinSlimeCstToAstFacade.createExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createExpressionAstUncached = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createExpressionAstUncached(...args);
};
__qinSlimeCstToAstFacade.createTemplateLiteralAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createTemplateLiteralAst(...args);
};
__qinSlimeCstToAstFacade.createAwaitExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createAwaitExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createYieldExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createYieldExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createExpressionStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createExpressionStatementAst(...args);
};
__qinSlimeCstToAstFacade.createBlockStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createBlockStatementAst(...args);
};
__qinSlimeCstToAstFacade.createStatementListItemAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createStatementListItemAst(...args);
};
__qinSlimeCstToAstFacade.createDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createDeclarationAst(...args);
};
__qinSlimeCstToAstFacade.createDeclarationAstBase = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createDeclarationAstBase(...args);
};
__qinSlimeCstToAstFacade.createVariableDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createVariableDeclarationAst(...args);
};
__qinSlimeCstToAstFacade.createInitializerExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createInitializerExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createLexicalBindingAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createLexicalBindingAst(...args);
};
__qinSlimeCstToAstFacade.createBindingTargetAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createBindingTargetAst(...args);
};
__qinSlimeCstToAstFacade.createBindingPatternAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createBindingPatternAst(...args);
};
__qinSlimeCstToAstFacade.createArrayBindingPatternAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createArrayBindingPatternAst(...args);
};
__qinSlimeCstToAstFacade.createObjectBindingPatternAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createObjectBindingPatternAst(...args);
};
__qinSlimeCstToAstFacade.createIfStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createIfStatementAst(...args);
};
__qinSlimeCstToAstFacade.createReturnStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createReturnStatementAst(...args);
};
__qinSlimeCstToAstFacade.createForStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createForStatementAst(...args);
};
__qinSlimeCstToAstFacade.createForInStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createForInStatementAst(...args);
};
__qinSlimeCstToAstFacade.createForOfStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createForOfStatementAst(...args);
};
__qinSlimeCstToAstFacade.createForInOfStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createForInOfStatementAst(...args);
};
__qinSlimeCstToAstFacade.createWhileStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createWhileStatementAst(...args);
};
__qinSlimeCstToAstFacade.createDoWhileStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createDoWhileStatementAst(...args);
};
__qinSlimeCstToAstFacade.createBreakStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createBreakStatementAst(...args);
};
__qinSlimeCstToAstFacade.createContinueStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createContinueStatementAst(...args);
};
__qinSlimeCstToAstFacade.createThrowStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createThrowStatementAst(...args);
};
__qinSlimeCstToAstFacade.createSwitchStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createSwitchStatementAst(...args);
};
__qinSlimeCstToAstFacade.createTryStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createTryStatementAst(...args);
};
__qinSlimeCstToAstFacade.createWithStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createWithStatementAst(...args);
};
__qinSlimeCstToAstFacade.createDebuggerStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createDebuggerStatementAst(...args);
};
__qinSlimeCstToAstFacade.createLabeledStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createLabeledStatementAst(...args);
};
__qinSlimeCstToAstFacade.createEmptyStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createEmptyStatementAst(...args);
};
__qinSlimeCstToAstFacade.createFunctionDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createFunctionDeclarationAst(...args);
};
__qinSlimeCstToAstFacade.createFunctionBodyAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createFunctionBodyAst(...args);
};
__qinSlimeCstToAstFacade.createFunctionBodyAstBase = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createFunctionBodyAstBase(...args);
};
__qinSlimeCstToAstFacade.createFormalParametersAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createFormalParametersAst(...args);
};
__qinSlimeCstToAstFacade.createFormalParameterMetadataAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createFormalParameterMetadataAst(...args);
};
__qinSlimeCstToAstFacade.createConciseBodyAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createConciseBodyAst(...args);
};
__qinSlimeCstToAstFacade.createConciseBodyAstBase = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createConciseBodyAstBase(...args);
};
__qinSlimeCstToAstFacade.createFunctionExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createFunctionExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createGeneratorExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createGeneratorExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createAsyncFunctionExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createAsyncFunctionExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createAsyncGeneratorExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createAsyncGeneratorExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createArrowFunctionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createArrowFunctionAst(...args);
};
__qinSlimeCstToAstFacade.createObjectLiteralAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createObjectLiteralAst(...args);
};
__qinSlimeCstToAstFacade.createArrayLiteralAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createArrayLiteralAst(...args);
};
__qinSlimeCstToAstFacade.createClassDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createClassDeclarationAst(...args);
};
__qinSlimeCstToAstFacade.createMethodDefinitionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createMethodDefinitionAst(...args);
};
__qinSlimeCstToAstFacade.createClassExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createClassExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createClassHeritageAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createClassHeritageAst(...args);
};
__qinSlimeCstToAstFacade.createClassBodyAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createClassBodyAst(...args);
};
__qinSlimeCstToAstFacade.createImportDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createImportDeclarationAst(...args);
};
__qinSlimeCstToAstFacade.createExportDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createExportDeclarationAst(...args);
};
__qinSlimeCstToAstFacade.createDecoratorsAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createDecoratorsAst(...args);
};
__qinSlimeCstToAstFacade.createDecoratorAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createDecoratorAst(...args);
};
__qinSlimeCstToAstFacade.createTSTypeAnnotationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createTSTypeAnnotationAst(...args);
};
__qinSlimeCstToAstFacade.createTSTypeAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createTSTypeAst(...args);
};
__qinSlimeCstToAstFacade.createTSInterfaceDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createTSInterfaceDeclarationAst(...args);
};
__qinSlimeCstToAstFacade.createTSTypeAliasDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createTSTypeAliasDeclarationAst(...args);
};
__qinSlimeCstToAstFacade.createTSEnumDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createTSEnumDeclarationAst(...args);
};
__qinSlimeCstToAstFacade.createProgramAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createProgramAst(...args);
};

__qinSlimeCstToAstUtils = new SlimeCstToAst();
