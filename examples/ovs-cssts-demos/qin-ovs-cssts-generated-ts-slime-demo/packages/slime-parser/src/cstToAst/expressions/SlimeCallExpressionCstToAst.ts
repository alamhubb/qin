/**
 * SlimeCallExpressionCstToAst - 鍑芥暟璋冪敤/new 琛ㄨ揪寮?
 *
 * 璐熻矗锛?
 * - CallExpression 澶勭悊
 * - NewExpression 澶勭悊
 * - SuperCall 澶勭悊
 *
 * 娉ㄦ剰锛欰rguments 瑙ｆ瀽宸茶縼绉诲埌 SlimePostfixExpressionCstToAst
 */
import { SubhutiCst } from "subhuti";
import {
    SlimeAstCreateUtils,
    type SlimeCallArgument,
    SlimeExpression,
    type SlimeSuper,
    SlimeTokenCreateUtils
} from "slime-ast";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";

export class SlimeCallExpressionCstToAstSingle {

    /**
     * 鍒涘缓 CallExpression AST
     * 鏀寔 CallExpression 鍜?CoverCallExpressionAndAsyncArrowHead
     */
    createCallExpressionAst(cst: SubhutiCst): SlimeExpression {
        const isCallExpr = cst.getName() === SlimeParser.prototype.CallExpression?.name || cst.getName() === 'CallExpression'
        const isCoverExpr = cst.getName() === 'CoverCallExpressionAndAsyncArrowHead'

        if (!isCallExpr && !isCoverExpr) {
            throw new Error(`createCallExpressionAst: Expected CallExpression or CoverCallExpressionAndAsyncArrowHead, got ${cst.getName()}`)
        }

        if (cst.getChildren().length === 1) {
            // 鍗曚釜瀛愯妭鐐癸紝鍙兘鏄?SuperCall
            const first = cst.getChildren()[0]
            if (first.getName() === SlimeParser.prototype.SuperCall?.name) {
                return SlimeCstToAstUtils.createSuperCallAst(first)
            }
            return SlimeCstToAstUtils.createExpressionAst(first)
        }

        // 澶氫釜 children锛氬鐞嗙涓€涓瓙鑺傜偣锛岀劧鍚庝娇鐢ㄧ粺涓€鍚庣紑澶勭悊鍣?
        let current: SlimeExpression
        const firstChild = cst.getChildren()[0]

        if (firstChild.getName() === 'CoverCallExpressionAndAsyncArrowHead') {
            current = SlimeCstToAstUtils.createCallExpressionAst(firstChild)
        } else if (firstChild.getName() === SlimeParser.prototype.MemberExpression?.name || firstChild.getName() === 'MemberExpression') {
            current = SlimeCstToAstUtils.createMemberExpressionAst(firstChild)
        } else if (firstChild.getName() === SlimeParser.prototype.SuperCall?.name || firstChild.getName() === 'SuperCall') {
            current = SlimeCstToAstUtils.createSuperCallAst(firstChild)
        } else if (firstChild.getName() === SlimeParser.prototype.ImportCall?.name || firstChild.getName() === 'ImportCall') {
            current = SlimeCstToAstUtils.createImportCallAst(firstChild)
        } else {
            current = SlimeCstToAstUtils.createExpressionAst(firstChild)
        }

        // 浣跨敤缁熶竴鍚庣紑澶勭悊鍣ㄥ鐞嗗墿浣欑殑鎿嶄綔
        return SlimeCstToAstUtils.processPostfixOperations(current, cst.getChildren(), 1, cst.getLoc())
    }

    /**
     * 鍒涘缓 NewExpression AST
     * 鏀寔 NewExpression 鍜?NewMemberExpressionArguments
     */
    createNewExpressionAst(cst: SubhutiCst): any {
        const isNewMemberExpr = cst.getName() === 'NewMemberExpressionArguments'
        const isNewExpr = cst.getName() === SlimeParser.prototype.NewExpression?.name || cst.getName() === 'NewExpression'

        if (!isNewMemberExpr && !isNewExpr) {
            throw new Error('createNewExpressionAst: 涓嶆敮鎸佺殑绫诲瀷 ' + cst.getName())
        }

        if (isNewMemberExpr) {
            // NewMemberExpressionArguments -> NewTok + MemberExpression + Arguments
            let newToken: any = undefined
            let lParenToken: any = undefined
            let rParenToken: any = undefined

            const newCst = cst.getChildren()[0]
            if (newCst && (newCst.getName() === 'New' || newCst.getValue() === 'new')) {
                newToken = SlimeTokenCreateUtils.createNewToken(newCst.getLoc())
            }

            const argsCst = cst.getChildren()[2]
            if (argsCst && argsCst.getChildren()) {
                for (const child of argsCst.getChildren()) {
                    if (child.getName() === 'LParen' || child.getValue() === '(') {
                        lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
                    } else if (child.getName() === 'RParen' || child.getValue() === ')') {
                        rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
                    }
                }
            }

            const calleeExpression = SlimeCstToAstUtils.createMemberExpressionAst(cst.getChildren()[1])
            const args = SlimeCstToAstUtils.createArgumentsAstUnified(argsCst)

            return SlimeAstCreateUtils.createNewExpression(
                calleeExpression, args, cst.getLoc(),
                newToken, lParenToken, rParenToken
            )
        } else {
            // NewExpression 涓ょ褰㈠紡锛?
            // 1. MemberExpression - 濮旀墭
            // 2. new NewExpression - 鏃犲弬鏁?NewExpression
            const firstChild = cst.getChildren()[0]
            if (firstChild.getName() === 'New' || firstChild.getValue() === 'new') {
                const newToken = SlimeTokenCreateUtils.createNewToken(firstChild.getLoc())
                const innerNewExpr = cst.getChildren()[1]
                const calleeExpression = SlimeCstToAstUtils.createNewExpressionAst(innerNewExpr)

                return SlimeAstCreateUtils.createNewExpression(
                    calleeExpression, [], cst.getLoc(),
                    newToken, undefined, undefined
                )
            } else {
                return SlimeCstToAstUtils.createExpressionAst(firstChild)
            }
        }
    }

    /**
     * 鍒涘缓 SuperCall AST
     * SuperCall -> super Arguments
     */
    createSuperCallAst(cst: SubhutiCst): SlimeExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.SuperCall?.name)

        const argumentsCst = cst.getChildren()[1]
        const argumentsAst: SlimeCallArgument[] = SlimeCstToAstUtils.createArgumentsAstUnified(argumentsCst)
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        for (const child of argumentsCst?.children || []) {
            if (child.getName() === 'LParen' || child.getValue() === '(') {
                lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
            } else if (child.getName() === 'RParen' || child.getValue() === ')') {
                rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
            }
        }

        const superNode: SlimeSuper = {
            type: "Super",
            loc: cst.getChildren()[0].loc
        }

        return SlimeAstCreateUtils.createCallExpression(
            superNode,
            argumentsAst,
            cst.getLoc(),
            lParenToken,
            rParenToken
        ) as SlimeExpression
    }
}

export const SlimeCallExpressionCstToAst = new SlimeCallExpressionCstToAstSingle()
