/**
 * SlimeTSDeclarationCstToAst - TypeScript 澹版槑
 *
 * 璐熻矗锛?
 * - createTSInterfaceDeclarationAst
 * - createTSTypeAliasDeclarationAst
 * - createTSEnumDeclarationAst
 * - createTSModuleDeclarationAst
 * - createTSDeclareStatementAst
 */
import { SubhutiCst } from "subhuti";
import { SlimeAstCreateUtils, SlimeAstTypeName } from "slime-ast";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

export class SlimeTSDeclarationCstToAstSingle {

    /**
     * [TypeScript] 杞崲 TSInterfaceDeclaration CST 涓?AST
     */
    createTSInterfaceDeclarationAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let id: any = undefined
        let typeParameters: any = undefined
        let extendsClause: any[] = []
        let body: any = undefined

        for (const child of children) {
            if (child.getName() === 'Identifier' || child.getName() === 'IdentifierName') {
                const tokenCst = child.getChildren()?.[0] || child
                id = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (child.getName() === 'TSTypeParameterDeclaration') {
                typeParameters = SlimeCstToAstUtils.createTSTypeParameterDeclarationAst(child)
            } else if (child.getName() === 'TSInterfaceExtends') {
                extendsClause = SlimeCstToAstUtils.createTSInterfaceExtendsAst(child)
            } else if (child.getName() === 'TSInterfaceBody') {
                body = SlimeCstToAstUtils.createTSInterfaceBodyAst(child)
            }
        }

        return SlimeAstCreateUtils.createTSInterfaceDeclaration(
            id,
            body,
            typeParameters,
            extendsClause.length > 0 ? extendsClause : undefined,
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSInterfaceExtends CST 涓?AST
     */
    createTSInterfaceExtendsAst(cst: SubhutiCst): any[] {
        const children = cst.getChildren() || []
        const result: any[] = []

        for (const child of children) {
            if (child.getName() === 'TSExpressionWithTypeArguments') {
                result.push(SlimeCstToAstUtils.createTSExpressionWithTypeArgumentsAst(child))
            }
        }

        return result
    }

    /**
     * [TypeScript] 杞崲 TSExpressionWithTypeArguments CST 涓?AST
     */
    createTSExpressionWithTypeArgumentsAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let expression: any = undefined
        let typeParameters: any = undefined

        for (const child of children) {
            if (child.getName() === 'Identifier' || child.getName() === 'IdentifierName') {
                const tokenCst = child.getChildren()?.[0] || child
                expression = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (child.getName() === 'TSTypeName') {
                // TSTypeName 鍖呭惈 Identifier 瀛愯妭鐐?
                expression = SlimeCstToAstUtils.createTSTypeNameAst(child)
            } else if (child.getName() === 'TSTypeParameterInstantiation') {
                typeParameters = SlimeCstToAstUtils.createTSTypeParameterInstantiationAst(child)
            }
        }

        return {
            type: SlimeAstTypeName.TSInterfaceHeritage,
            expression,
            typeParameters,
            loc: cst.getLoc(),
        }
    }

    /**
     * [TypeScript] 杞崲 TSInterfaceBody CST 涓?AST
     */
    createTSInterfaceBodyAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []
        const body: any[] = []

        for (const child of children) {
            if (child.getName() === 'TSTypeMember') {
                body.push(SlimeCstToAstUtils.createTSTypeMemberAst(child))
            }
        }

        return SlimeAstCreateUtils.createTSInterfaceBody(body, cst.getLoc())
    }

    /**
     * [TypeScript] 杞崲 TSTypeAliasDeclaration CST 涓?AST
     */
    createTSTypeAliasDeclarationAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let id: any = undefined
        let typeParameters: any = undefined
        let typeAnnotation: any = undefined

        for (const child of children) {
            if (child.getName() === 'Identifier' || child.getName() === 'IdentifierName') {
                const tokenCst = child.getChildren()?.[0] || child
                id = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (child.getName() === 'TSTypeParameterDeclaration') {
                typeParameters = SlimeCstToAstUtils.createTSTypeParameterDeclarationAst(child)
            } else if (child.getName() === 'TSType') {
                typeAnnotation = SlimeCstToAstUtils.createTSTypeAst(child)
            }
        }

        return SlimeAstCreateUtils.createTSTypeAliasDeclaration(
            id,
            typeAnnotation,
            typeParameters,
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSEnumDeclaration CST 涓?AST
     */
    createTSEnumDeclarationAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let id: any = undefined
        let members: any[] = []
        let isConst = false

        for (const child of children) {
            if (child.getName() === 'Const' || child.getValue() === 'const') {
                isConst = true
            } else if (child.getName() === 'Identifier' || child.getName() === 'IdentifierName') {
                const tokenCst = child.getChildren()?.[0] || child
                id = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (child.getName() === 'TSEnumMember') {
                members.push(SlimeCstToAstUtils.createTSEnumMemberAst(child))
            }
        }

        return SlimeAstCreateUtils.createTSEnumDeclaration(
            id,
            members,
            isConst,
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSEnumMember CST 涓?AST
     *
     * CST 缁撴瀯:
     * TSEnumMember
     *   - Identifier (鎴愬憳鍚?
     *   - Assign (鍙€夌殑 = 绗﹀彿)
     *   - AssignmentExpression (鍙€夌殑鍒濆鍖栬〃杈惧紡)
     */
    createTSEnumMemberAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let id: any = undefined
        let initializer: any = undefined

        for (const child of children) {
            if (child.getName() === 'Identifier') {
                // 鏋氫妇鎴愬憳鍚?
                const tokenCst = child.getChildren()?.[0] || child
                id = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (child.getName() === 'StringLiteral') {
                // 瀛楃涓插瓧闈㈤噺浣滀负鎴愬憳鍚?
                id = {
                    type: 'Literal',
                    value: child.getValue()?.slice(1, -1),
                    raw: child.getValue(),
                    loc: child.getLoc(),
                }
            } else if (child.getName() === 'AssignmentExpression') {
                // 鍒濆鍖栬〃杈惧紡
                initializer = SlimeCstToAstUtils.createAssignmentExpressionAst(child)
            }
        }

        return SlimeAstCreateUtils.createTSEnumMember(
            id,
            initializer,
            cst.getLoc()
        )
    }
}

export const SlimeTSDeclarationCstToAst = new SlimeTSDeclarationCstToAstSingle()
