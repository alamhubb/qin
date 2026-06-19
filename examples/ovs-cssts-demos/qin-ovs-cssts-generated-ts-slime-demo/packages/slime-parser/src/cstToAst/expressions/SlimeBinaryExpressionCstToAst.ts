/**
 * BinaryExpressionCstToAst - 浜屽厓琛ㄨ揪寮忚浆鎹?
 *
 * 閲嶆瀯锛氫娇鐢ㄧ粺涓€鐨?createBinaryExpressionChain 鏂规硶澶勭悊鎵€鏈変簩鍏冭〃杈惧紡
 */
import { SubhutiCst } from "subhuti";
import {
    SlimeExpression,
    SlimeAstTypeName,
    SlimeTokenCreateUtils
} from "slime-ast";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

export class SlimeBinaryExpressionCstToAstSingle {

    // ========================================
    // 閫氱敤浜屽厓琛ㄨ揪寮忛摼鏋勫缓鏂规硶
    // ========================================

    /**
     * 閫氱敤浜屽厓琛ㄨ揪寮忛摼鏋勫缓
     * 鐢ㄤ簬澶勭悊鎵€鏈夌被鍨嬬殑浜屽厓/閫昏緫琛ㄨ揪寮?
     *
     * @param cst CST 鑺傜偣
     * @param astType AST 绫诲瀷锛圔inaryExpression 鎴?LogicalExpression锛?
     * @returns 鏋勫缓鐨勮〃杈惧紡 AST
     */
    private createBinaryExpressionChain(
        cst: SubhutiCst,
        astType: 'BinaryExpression' | 'LogicalExpression'
    ): SlimeExpression {
        if (cst.getChildren().length > 1) {
            // 鏀寔閾惧紡杩愮畻锛歛 op b op c => (a op b) op c
            let left = SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[0])

            for (let i = 1; i < cst.getChildren().length; i += 2) {
                const operatorNode = cst.getChildren()[i]
                const operatorLeaf = operatorNode.getChildren()?.[0] ?? operatorNode
                const operatorValue = operatorLeaf.value ?? operatorNode.getValue()
                const operatorLoc = operatorLeaf.loc ?? operatorNode.getLoc()
                const operatorToken = astType === 'LogicalExpression'
                    ? SlimeTokenCreateUtils.createLogicalOperatorToken(operatorValue as any, operatorLoc)
                    : SlimeTokenCreateUtils.createBinaryOperatorToken(operatorValue as any, operatorLoc)

                const right = SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[i + 1])

                left = {
                    type: astType === 'LogicalExpression'
                        ? SlimeAstTypeName.LogicalExpression
                        : SlimeAstTypeName.BinaryExpression,
                    operator: operatorToken,
                    left: left,
                    right: right,
                    loc: cst.getLoc()
                } as any
            }
            return left
        }
        return SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[0])
    }

    // ========================================
    // 閫昏緫琛ㄨ揪寮忥紙LogicalExpression锛?
    // ========================================

    createLogicalORExpressionAst(cst: SubhutiCst): SlimeExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.LogicalORExpression?.name)
        return this.createBinaryExpressionChain(cst, 'LogicalExpression')
    }

    createLogicalANDExpressionAst(cst: SubhutiCst): SlimeExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.LogicalANDExpression?.name)
        return this.createBinaryExpressionChain(cst, 'LogicalExpression')
    }

    // ========================================
    // 浣嶈繍绠楄〃杈惧紡锛圔inaryExpression锛?
    // ========================================

    createBitwiseORExpressionAst(cst: SubhutiCst): SlimeExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.BitwiseORExpression?.name)
        return this.createBinaryExpressionChain(cst, 'BinaryExpression')
    }

    createBitwiseXORExpressionAst(cst: SubhutiCst): SlimeExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.BitwiseXORExpression?.name)
        return this.createBinaryExpressionChain(cst, 'BinaryExpression')
    }

    createBitwiseANDExpressionAst(cst: SubhutiCst): SlimeExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.BitwiseANDExpression?.name)
        return this.createBinaryExpressionChain(cst, 'BinaryExpression')
    }

    // ========================================
    // 姣旇緝琛ㄨ揪寮忥紙BinaryExpression锛?
    // ========================================

    createEqualityExpressionAst(cst: SubhutiCst): SlimeExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.EqualityExpression?.name)
        return this.createBinaryExpressionChain(cst, 'BinaryExpression')
    }

    createRelationalExpressionAst(cst: SubhutiCst): SlimeExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.RelationalExpression?.name)
        return this.createBinaryExpressionChain(cst, 'BinaryExpression')
    }

    // ========================================
    // 绉讳綅/绠楁湳琛ㄨ揪寮忥紙BinaryExpression锛?
    // ========================================

    createShiftExpressionAst(cst: SubhutiCst): SlimeExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ShiftExpression?.name)
        return this.createBinaryExpressionChain(cst, 'BinaryExpression')
    }

    createAdditiveExpressionAst(cst: SubhutiCst): SlimeExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.AdditiveExpression?.name)
        return this.createBinaryExpressionChain(cst, 'BinaryExpression')
    }

    createMultiplicativeExpressionAst(cst: SubhutiCst): SlimeExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.MultiplicativeExpression?.name)
        return this.createBinaryExpressionChain(cst, 'BinaryExpression')
    }

    /**
     * MultiplicativeOperator CST 杞?AST
     * MultiplicativeOperator -> * | / | %
     */
    createMultiplicativeOperatorAst(cst: SubhutiCst): string {
        const token = cst.getChildren()?.[0]
        return token?.value || '*'
    }

    /**
     * 鍒涘缓 ExponentiationExpression AST锛圗S2016锛?
     * 澶勭悊 ** 骞傝繍绠楃锛堝彸缁撳悎锛?
     */
    createExponentiationExpressionAst(cst: SubhutiCst): SlimeExpression {
        // ExponentiationExpression -> UnaryExpression | UpdateExpression ** ExponentiationExpression
        if (cst.getChildren().length === 1) {
            return SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[0])
        }

        // 鏈夊涓瓙鑺傜偣锛屽彸缁撳悎锛歛 ** b ** c = a ** (b ** c)
        const left = SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[0])
        const operatorNode = cst.getChildren()[1]
        const operatorLeaf = operatorNode?.children?.[0] ?? operatorNode
        const operatorValue = operatorLeaf?.value ?? operatorNode?.value ?? '**'
        const operatorLoc = operatorLeaf?.loc ?? operatorNode?.loc
        const operatorToken = SlimeTokenCreateUtils.createBinaryOperatorToken(operatorValue as any, operatorLoc)
        const right = SlimeCstToAstUtils.createExponentiationExpressionAst(cst.getChildren()[2])  // 閫掑綊澶勭悊鍙充晶
        return {
            type: SlimeAstTypeName.BinaryExpression,
            operator: operatorToken,
            left: left,
            right: right
        } as any
    }
}

export const SlimeBinaryExpressionCstToAst = new SlimeBinaryExpressionCstToAstSingle()
