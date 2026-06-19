/**
 * SlimeTSFunctionTypeCstToAst - TypeScript 鍑芥暟绫诲瀷
 *
 * 璐熻矗锛?
 * - createTSFunctionTypeAst
 * - createTSConstructorTypeAst
 * - createTSTypeParameterDeclarationAst
 * - createTSTypeParameterAst
 * - createTSTypeParameterInstantiationAst
 */
import { SubhutiCst } from "subhuti";
import { SlimeAstCreateUtils, SlimeAstTypeName } from "slime-ast";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

export class SlimeTSFunctionTypeCstToAstSingle {
    /**
     * [TypeScript] 杞崲 TSFunctionType CST 涓?AST
     */
    createTSFunctionTypeAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let typeParameters: any = undefined
        let parameters: any[] = []
        let returnType: any = undefined

        for (const child of children) {
            if (child.getName() === 'TSTypeParameterDeclaration') {
                typeParameters = SlimeCstToAstUtils.createTSTypeParameterDeclarationAst(child)
            } else if (child.getName() === 'TSParameterList') {
                parameters = SlimeCstToAstUtils.createTSParameterListAst(child)
            } else if (child.getName() === 'TSType') {
                returnType = SlimeCstToAstUtils.createTSTypeAst(child)
            }
        }

        return SlimeAstCreateUtils.createTSFunctionType(
            typeParameters,
            parameters,
            returnType,
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSConstructorType CST 涓?AST
     */
    createTSConstructorTypeAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let typeParameters: any = undefined
        let parameters: any[] = []
        let returnType: any = undefined

        for (const child of children) {
            if (child.getName() === 'TSTypeParameterDeclaration') {
                typeParameters = SlimeCstToAstUtils.createTSTypeParameterDeclarationAst(child)
            } else if (child.getName() === 'TSParameterList') {
                parameters = SlimeCstToAstUtils.createTSParameterListAst(child)
            } else if (child.getName() === 'TSType') {
                returnType = SlimeCstToAstUtils.createTSTypeAst(child)
            }
        }

        return SlimeAstCreateUtils.createTSConstructorType(
            typeParameters,
            parameters,
            returnType,
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSTypeParameterDeclaration CST 涓?AST
     */
    createTSTypeParameterDeclarationAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []
        const params: any[] = []

        for (const child of children) {
            if (child.getName() === 'TSTypeParameter') {
                params.push(SlimeCstToAstUtils.createTSTypeParameterAst(child))
            }
        }

        return SlimeAstCreateUtils.createTSTypeParameterDeclaration(params, cst.getLoc())
    }

    /**
     * [TypeScript] 杞崲 TSTypeParameter CST 涓?AST
     */
    createTSTypeParameterAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let name: any = undefined
        let constraint: any = undefined
        let defaultType: any = undefined

        for (let i = 0; i < children.length; i++) {
            const child = children[i]
            if (child.getName() === 'Identifier' || child.getName() === 'IdentifierName') {
                const tokenCst = child.getChildren()?.[0] || child
                name = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (child.getName() === 'Extends' || child.getValue() === 'extends') {
                // 涓嬩竴涓槸绾︽潫绫诲瀷
                if (children[i + 1]?.name === 'TSType') {
                    constraint = SlimeCstToAstUtils.createTSTypeAst(children[i + 1])
                }
            } else if (child.getName() === 'Assign' || child.getValue() === '=') {
                // 涓嬩竴涓槸榛樿绫诲瀷
                if (children[i + 1]?.name === 'TSType') {
                    defaultType = SlimeCstToAstUtils.createTSTypeAst(children[i + 1])
                }
            }
        }

        return SlimeAstCreateUtils.createTSTypeParameter(
            name,
            constraint,
            defaultType,
            cst.getLoc()
        )
    }
}

export const SlimeTSFunctionTypeCstToAst = new SlimeTSFunctionTypeCstToAstSingle()
