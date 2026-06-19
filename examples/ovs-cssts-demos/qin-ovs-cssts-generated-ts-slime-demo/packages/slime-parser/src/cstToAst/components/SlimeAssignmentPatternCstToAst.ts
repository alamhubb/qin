import { SubhutiCst } from "subhuti";
import {
    SlimeArrayPattern,
    SlimeBlockStatement,
    SlimeFunctionExpression,
    SlimeFunctionParam,
    SlimeIdentifier, SlimeObjectPattern,
    SlimeTokenCreateUtils
} from "slime-ast";

import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";

export class SlimeAssignmentPatternCstToAstSingle {
    // ==================== 瑙ｆ瀯鐩稿叧杞崲鏂规硶 ====================

    /**
     * AssignmentPattern CST 锟?AST
     * AssignmentPattern -> ObjectAssignmentPattern | ArrayAssignmentPattern
     */
    createAssignmentPatternAst(cst: SubhutiCst): any {
        const firstChild = cst.getChildren()?.[0]
        if (!firstChild) throw new Error('AssignmentPattern has no children')

        if (firstChild.getName() === SlimeParser.prototype.ObjectAssignmentPattern?.name ||
            firstChild.getName() === 'ObjectAssignmentPattern') {
            return SlimeCstToAstUtils.createObjectAssignmentPatternAst(firstChild) as any
        } else if (firstChild.getName() === SlimeParser.prototype.ArrayAssignmentPattern?.name ||
            firstChild.getName() === 'ArrayAssignmentPattern') {
            return SlimeCstToAstUtils.createArrayAssignmentPatternAst(firstChild) as any
        }

        throw new Error(`Unknown AssignmentPattern type: ${firstChild.getName()}`)
    }

    /**
     * ObjectAssignmentPattern CST 锟?AST
     */
    createObjectAssignmentPatternAst(cst: SubhutiCst): SlimeObjectPattern {
        return SlimeCstToAstUtils.createObjectBindingPatternAst(cst)
    }

    /**
     * ArrayAssignmentPattern CST 锟?AST
     */
    createArrayAssignmentPatternAst(cst: SubhutiCst): SlimeArrayPattern {
        return SlimeCstToAstUtils.createArrayBindingPatternAst(cst)
    }


    /**
     * AssignmentPropertyList CST 锟?AST
     */
    createAssignmentPropertyListAst(cst: SubhutiCst): any[] {
        const properties: any[] = []
        for (const child of cst.getChildren() || []) {
            if (child.getName() === SlimeParser.prototype.AssignmentProperty?.name ||
                child.getName() === 'AssignmentProperty') {
                properties.push(SlimeCstToAstUtils.createAssignmentPropertyAst(child))
            }
        }
        return properties
    }

    /**
     * AssignmentProperty CST 锟?AST
     */
    createAssignmentPropertyAst(cst: SubhutiCst): any {
        return SlimeCstToAstUtils.createBindingPropertyAst(cst)
    }

    /**
     * AssignmentElementList CST 锟?AST
     */
    createAssignmentElementListAst(cst: SubhutiCst): any[] {
        return SlimeCstToAstUtils.createBindingElementListAst(cst)
    }

    /**
     * AssignmentElement CST 锟?AST
     */
    createAssignmentElementAst(cst: SubhutiCst): any {
        return SlimeCstToAstUtils.createBindingElementAst(cst)
    }

    /**
     * AssignmentElisionElement CST 锟?AST
     */
    createAssignmentElisionElementAst(cst: SubhutiCst): any {
        return SlimeCstToAstUtils.createBindingElisionElementAst(cst)
    }

    /**
     * AssignmentRestElement CST 锟?AST
     */
    createAssignmentRestElementAst(cst: SubhutiCst): any {
        return SlimeCstToAstUtils.createBindingRestElementAst(cst)
    }

    /**
     * AssignmentRestProperty CST 锟?AST
     */
    createAssignmentRestPropertyAst(cst: SubhutiCst): any {
        return SlimeCstToAstUtils.createBindingRestPropertyAst(cst)
    }
}


export const SlimeAssignmentPatternCstToAst = new SlimeAssignmentPatternCstToAstSingle()
