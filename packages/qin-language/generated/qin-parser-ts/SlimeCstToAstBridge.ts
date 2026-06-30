// Generated Slime CST-to-AST bridge by Qin. Source Java: com.slime.parser.cstToAst.SlimeCstToAstUtils
import { com_slime_parser_cstToAst_SlimeCstToAstUtils as __QinGeneratedSlimeCstToAstUtils } from "./com/slime/parser/cstToAst/SlimeCstToAstUtils.ts";

export class SlimeCstToAst {
  constructor(...args: any[]) {
    if (args.length !== 0) {
      throw new Error("Unsupported SlimeCstToAst constructor arity: " + args.length);
    }
    registerSlimeCstToAstUtil(this);
  }
  createIdentifierAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createIdentifierAst(...args);
  }
  createBooleanLiteralAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createBooleanLiteralAst(...args);
  }
  createNumericLiteralAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createNumericLiteralAst(...args);
  }
  createStringLiteralAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createStringLiteralAst(...args);
  }
  createPrimaryExpressionAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createPrimaryExpressionAst(...args);
  }
  createBinaryExpressionAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createBinaryExpressionAst(...args);
  }
  createUnaryExpressionAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createUnaryExpressionAst(...args);
  }
  createAssignmentExpressionAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createAssignmentExpressionAst(...args);
  }
  createCallExpressionAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createCallExpressionAst(...args);
  }
  createMemberExpressionAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createMemberExpressionAst(...args);
  }
  createExpressionAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createExpressionAst(...args);
  }
  createExpressionStatementAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createExpressionStatementAst(...args);
  }
  createBlockStatementAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createBlockStatementAst(...args);
  }
  createVariableDeclarationAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createVariableDeclarationAst(...args);
  }
  createIfStatementAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createIfStatementAst(...args);
  }
  createReturnStatementAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createReturnStatementAst(...args);
  }
  createForStatementAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createForStatementAst(...args);
  }
  createWhileStatementAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createWhileStatementAst(...args);
  }
  createFunctionDeclarationAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createFunctionDeclarationAst(...args);
  }
  createFunctionExpressionAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createFunctionExpressionAst(...args);
  }
  createArrowFunctionAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createArrowFunctionAst(...args);
  }
  createClassDeclarationAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createClassDeclarationAst(...args);
  }
  createClassExpressionAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createClassExpressionAst(...args);
  }
  createImportDeclarationAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createImportDeclarationAst(...args);
  }
  createExportDeclarationAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createExportDeclarationAst(...args);
  }
  createTSTypeAnnotationAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createTSTypeAnnotationAst(...args);
  }
  createTSTypeAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createTSTypeAst(...args);
  }
  createTSInterfaceDeclarationAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createTSInterfaceDeclarationAst(...args);
  }
  createTSTypeAliasDeclarationAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createTSTypeAliasDeclarationAst(...args);
  }
  createTSEnumDeclarationAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createTSEnumDeclarationAst(...args);
  }
  createProgramAst(...args: any[]): any {
    return __QinGeneratedSlimeCstToAstUtils.createProgramAst(...args);
  }
}

let __qinSlimeCstToAstUtils: SlimeCstToAst;
export function registerSlimeCstToAstUtil(instance: SlimeCstToAst): void {
  __qinSlimeCstToAstUtils = instance;
}

export const SlimeCstToAstUtils = {} as SlimeCstToAst;
const __qinSlimeCstToAstFacade: any = SlimeCstToAstUtils;
__qinSlimeCstToAstFacade.createIdentifierAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createIdentifierAst(...args);
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
__qinSlimeCstToAstFacade.createAssignmentExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createAssignmentExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createCallExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createCallExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createMemberExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createMemberExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createExpressionStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createExpressionStatementAst(...args);
};
__qinSlimeCstToAstFacade.createBlockStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createBlockStatementAst(...args);
};
__qinSlimeCstToAstFacade.createVariableDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createVariableDeclarationAst(...args);
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
__qinSlimeCstToAstFacade.createWhileStatementAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createWhileStatementAst(...args);
};
__qinSlimeCstToAstFacade.createFunctionDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createFunctionDeclarationAst(...args);
};
__qinSlimeCstToAstFacade.createFunctionExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createFunctionExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createArrowFunctionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createArrowFunctionAst(...args);
};
__qinSlimeCstToAstFacade.createClassDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createClassDeclarationAst(...args);
};
__qinSlimeCstToAstFacade.createClassExpressionAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createClassExpressionAst(...args);
};
__qinSlimeCstToAstFacade.createImportDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createImportDeclarationAst(...args);
};
__qinSlimeCstToAstFacade.createExportDeclarationAst = function (...args: any[]): any {
  return __qinSlimeCstToAstUtils.createExportDeclarationAst(...args);
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
