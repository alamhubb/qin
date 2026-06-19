/**
 * SlimeTSExpressionCstToAst - TypeScript 琛ㄨ揪寮忕粺涓€澶勭悊鍣?
 *
 * 璐熻矗鎵€鏈?TypeScript 鐗规湁琛ㄨ揪寮忥細
 * - TSTypeAssertion (<Type>expression)
 * - TSAsExpression (expression as Type)
 * - TSSatisfiesExpression (expression satisfies Type)
 * - TSNonNullExpression (expression!)
 * - TSTypePredicate (x is Type)
 */
import { SubhutiCst } from "subhuti";
import { SlimeAstCreateUtils, SlimeAstTypeName, type SlimeExpression } from "slime-ast";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

export class SlimeTSExpressionCstToAstSingle {
    private createIncompleteExpression(cst: SubhutiCst, reason: string): SlimeExpression {
        return SlimeCstToAstUtils.createIncompleteExpressionAst(cst, reason)
    }

    private createAnyTypeNode(cst: SubhutiCst): any {
        return {
            type: SlimeAstTypeName.TSAnyKeyword,
            loc: cst.getLoc(),
        }
    }

    private safeCreateExpression(cst: SubhutiCst | undefined, parent: SubhutiCst, reason: string): SlimeExpression {
        if (!cst) {
            return this.createIncompleteExpression(parent, reason)
        }
        try {
            return SlimeCstToAstUtils.createExpressionAst(cst)
        } catch (e: any) {
            console.warn(`[SlimeTSExpressionCstToAst] expression convert failed: ${reason}; ${e?.message || e}`)
            return this.createIncompleteExpression(parent, reason)
        }
    }

    private safeCreateType(cst: SubhutiCst | undefined, parent: SubhutiCst, reason: string): any {
        if (!cst) {
            console.warn(`[SlimeTSExpressionCstToAst] fallback to TSAnyKeyword: ${reason}`)
            return this.createAnyTypeNode(parent)
        }
        try {
            return SlimeCstToAstUtils.createTSTypeAst(cst)
        } catch (e: any) {
            console.warn(`[SlimeTSExpressionCstToAst] type convert failed: ${reason}; ${e?.message || e}`)
            return this.createAnyTypeNode(parent)
        }
    }

    /**
     * TypeScript 琛ㄨ揪寮忕粺涓€鍏ュ彛
     * 鏍规嵁 CST 鑺傜偣绫诲瀷鍒嗗彂鍒板搴旂殑澶勭悊鏂规硶
     */
    createTSExpressionAst(cst: SubhutiCst): SlimeExpression {
        switch (cst.getName()) {
            case 'TSTypeAssertion':
                return this.createTSTypeAssertionAst(cst)
            case 'TSAsExpression':
                return this.createTSAsExpressionFromCst(cst)
            case 'TSSatisfiesExpression':
                return this.createTSSatisfiesExpressionFromCst(cst)
            case 'TSNonNullExpression':
                return this.createTSNonNullExpressionFromCst(cst)
            default:
                return this.createIncompleteExpression(cst, `unknown TS expression type: ${cst.getName()}`)
        }
    }

    /**
     * 妫€鏌ユ槸鍚︽槸 TypeScript 琛ㄨ揪寮忚妭鐐?
     */
    isTypeScriptExpression(name: string): boolean {
        return [
            'TSTypeAssertion',
            'TSAsExpression',
            'TSSatisfiesExpression',
            'TSNonNullExpression'
        ].includes(name)
    }

    // ========================================
    // 缁熶竴 API锛氭墍鏈夋柟娉曟帴鏀?CST
    // ========================================

    /**
     * [TypeScript] 灏栨嫭鍙风被鍨嬫柇瑷€ <Type>expression
     * CST 缁撴瀯: [Less, TSType, Greater, UnaryExpression]
     */
    createTSTypeAssertionAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        const typeCst = children.find(c => c.name === 'TSType')
        const exprCst = children.find(c => c.name === 'UnaryExpression')

        return SlimeAstCreateUtils.createTSTypeAssertion(
            this.safeCreateType(typeCst, cst, 'TSTypeAssertion missing TSType'),
            exprCst
                ? SlimeCstToAstUtils.createUnaryExpressionAst(exprCst)
                : this.createIncompleteExpression(cst, 'TSTypeAssertion missing UnaryExpression'),
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] as 绫诲瀷鏂█ expression as Type
     * CST 缁撴瀯: UpdateExpression with TSAsExpressionTail
     */
    createTSAsExpressionFromCst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        // 绗竴涓瓙鑺傜偣鏄〃杈惧紡
        const exprCst = children[0]
        // 鏌ユ壘 TSAsExpressionTail 鎴栫洿鎺ユ煡鎵?TSType
        const tailCst = children.find(c => c.name === 'TSAsExpressionTail')
        const typeCst = tailCst?.children?.find((c: SubhutiCst) => c.name === 'TSType')
            || children.find(c => c.name === 'TSType')

        const expression = this.safeCreateExpression(exprCst, cst, 'TSAsExpression missing expression')

        // 妫€鏌ユ槸鍚︽槸 as const
        const constCst = tailCst?.children?.find((c: SubhutiCst) => c.name === 'Const' || c.value === 'const')
        if (constCst) {
            return {
                type: SlimeAstTypeName.TSAsExpression,
                expression,
                typeAnnotation: {
                    type: 'TSTypeReference',
                    typeName: { type: 'Identifier', name: 'const' }
                },
                loc: cst.getLoc(),
            }
        }

        return {
            type: SlimeAstTypeName.TSAsExpression,
            expression,
            typeAnnotation: this.safeCreateType(typeCst, cst, 'TSAsExpression missing TSType'),
            loc: cst.getLoc(),
        }
    }

    /**
     * [TypeScript] satisfies 绫诲瀷鏂█ expression satisfies Type
     * CST 缁撴瀯: UpdateExpression with TSSatisfiesExpressionTail
     */
    createTSSatisfiesExpressionFromCst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        const exprCst = children[0]
        const tailCst = children.find(c => c.name === 'TSSatisfiesExpressionTail')
        const typeCst = tailCst?.children?.find((c: SubhutiCst) => c.name === 'TSType')
            || children.find(c => c.name === 'TSType')

        return {
            type: SlimeAstTypeName.TSSatisfiesExpression,
            expression: this.safeCreateExpression(exprCst, cst, 'TSSatisfiesExpression missing expression'),
            typeAnnotation: this.safeCreateType(typeCst, cst, 'TSSatisfiesExpression missing TSType'),
            loc: cst.getLoc(),
        }
    }

    /**
     * [TypeScript] 闈炵┖鏂█ expression!
     * CST 缁撴瀯: UpdateExpression with TSNonNullExpressionTail
     */
    createTSNonNullExpressionFromCst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []
        const exprCst = children[0]

        return {
            type: SlimeAstTypeName.TSNonNullExpression,
            expression: this.safeCreateExpression(exprCst, cst, 'TSNonNullExpression missing expression'),
            loc: cst.getLoc(),
        }
    }

    // ========================================
    // 杈呭姪鏂规硶锛氭敮鎸侀澶勭悊鍙傛暟鐨勮皟鐢ㄦ柟寮忥紙鍚戝悗鍏煎锛?
    // ========================================

    /**
     * 鍒涘缓 TSAsExpression锛堥澶勭悊鍙傛暟鐗堟湰锛?
     */
    createTSAsExpressionAst(expression: any, typeCst: SubhutiCst, loc: any): any {
        return SlimeAstCreateUtils.createTSAsExpression(
            expression,
            SlimeCstToAstUtils.createTSTypeAst(typeCst),
            loc
        )
    }

    /**
     * 鍒涘缓 TSSatisfiesExpression锛堥澶勭悊鍙傛暟鐗堟湰锛?
     */
    createTSSatisfiesExpressionAst(expression: any, typeCst: SubhutiCst, loc: any): any {
        return SlimeAstCreateUtils.createTSSatisfiesExpression(
            expression,
            SlimeCstToAstUtils.createTSTypeAst(typeCst),
            loc
        )
    }

    /**
     * 鍒涘缓 TSNonNullExpression锛堥澶勭悊鍙傛暟鐗堟湰锛?
     */
    createTSNonNullExpressionAst(expression: any, loc: any): any {
        return SlimeAstCreateUtils.createTSNonNullExpression(expression, loc)
    }

    /**
     * [TypeScript] 绫诲瀷璋撹瘝 x is Type / asserts x is Type
     */
    createTSTypePredicateAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let asserts = false
        let parameterName: any = undefined
        let typeAnnotation: any = undefined

        for (const child of children) {
            if (child.getName() === 'TSAsserts' || child.getValue() === 'asserts') {
                asserts = true
            } else if (child.getName() === 'This' || child.getValue() === 'this') {
                parameterName = {
                    type: 'TSThisType',
                    loc: child.getLoc(),
                }
            } else if (child.getName() === 'Identifier') {
                const tokenCst = child.getChildren()?.[0] || child
                parameterName = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (child.getName() === 'TSType') {
                typeAnnotation = SlimeCstToAstUtils.createTSTypeAst(child)
            }
        }

        return {
            type: SlimeAstTypeName.TSTypePredicate,
            asserts,
            parameterName,
            typeAnnotation,
            loc: cst.getLoc(),
        }
    }
}

export const SlimeTSExpressionCstToAst = new SlimeTSExpressionCstToAstSingle()
