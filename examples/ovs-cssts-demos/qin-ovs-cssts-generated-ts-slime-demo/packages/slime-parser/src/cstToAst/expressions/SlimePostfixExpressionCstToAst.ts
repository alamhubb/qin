/**
 * SlimePostfixExpressionCstToAst - 缁熶竴鍚庣紑鎿嶄綔澶勭悊鍣?
 *
 * 璐熻矗澶勭悊鎵€鏈夊悗缂€鎿嶄綔锛?
 * - .property (鎴愬憳璁块棶)
 * - [expression] (璁＄畻灞炴€ц闂?
 * - () (鍑芥暟璋冪敤)
 * - <T>() (TypeScript 娉涘瀷璋冪敤)
 * - `template` (鏍囩妯℃澘)
 * - ! (TypeScript 闈炵┖鏂█)
 */
import { SubhutiCst } from "subhuti";
import {
    SlimeAstCreateUtils,
    SlimeAstTypeName,
    type SlimeCallArgument,
    SlimeExpression,
    type SlimeIdentifier,
    SlimeSpreadElement,
    SlimeTokenCreateUtils
} from "slime-ast";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";

export class SlimePostfixExpressionCstToAstSingle {

    /**
     * 缁熶竴澶勭悊鎵€鏈夊悗缂€鎿嶄綔
     *
     * @param base 鍩虹琛ㄨ揪寮?
     * @param children CST 瀛愯妭鐐规暟缁?
     * @param startIdx 寮€濮嬪鐞嗙殑绱㈠紩
     * @returns 澶勭悊鍚庣殑琛ㄨ揪寮?
     */
    processPostfixOperations(
        base: SlimeExpression,
        children: SubhutiCst[],
        startIdx: number,
        loc?: any
    ): SlimeExpression {
        let current = base
        let pendingTypeParams: any = undefined

        for (let i = startIdx; i < children.length; i++) {
            const child = children[i]
            const result = this.processSinglePostfix(current, child, children, i, pendingTypeParams, loc)

            current = result.expression
            pendingTypeParams = result.pendingTypeParams
            i = result.nextIndex ?? i
        }

        return current
    }

    /**
     * 澶勭悊鍗曚釜鍚庣紑鎿嶄綔
     */
    private processSinglePostfix(
        current: SlimeExpression,
        child: SubhutiCst,
        children: SubhutiCst[],
        index: number,
        pendingTypeParams: any,
        loc?: any
    ): { expression: SlimeExpression; pendingTypeParams?: any; nextIndex?: number } {
        const name = child.getName()

        // [TypeScript] 娉涘瀷绫诲瀷鍙傛暟 <T, U>
        if (name === 'TSTypeParameterInstantiation') {
            return {
                expression: current,
                pendingTypeParams: SlimeCstToAstUtils.createTSTypeParameterInstantiationAst(child),
                nextIndex: index
            }
        }

        // 鍑芥暟璋冪敤 ()
        if (name === SlimeParser.prototype.Arguments?.name || name === 'Arguments') {
            const args = this.createArgumentsAst(child)
            let lParenToken: any = undefined
            let rParenToken: any = undefined
            for (const argChild of child.getChildren() || []) {
                if (argChild.name === 'LParen' || argChild.value === '(') {
                    lParenToken = SlimeTokenCreateUtils.createLParenToken(argChild.loc)
                } else if (argChild.name === 'RParen' || argChild.value === ')') {
                    rParenToken = SlimeTokenCreateUtils.createRParenToken(argChild.loc)
                }
            }
            const callExpr = SlimeAstCreateUtils.createCallExpression(
                current,
                args,
                loc,
                lParenToken,
                rParenToken
            ) as any

            // 闄勫姞 TypeScript 绫诲瀷鍙傛暟
            if (pendingTypeParams) {
                callExpr.typeParameters = pendingTypeParams
            }

            return { expression: callExpr, pendingTypeParams: undefined }
        }

        // 鎴愬憳璁块棶 .property (DotIdentifier 鏃х増鍏煎)
        if (name === 'DotIdentifier') {
            const dotToken = SlimeTokenCreateUtils.createDotToken(child.getChildren()[0].loc)
            const identifierNameCst = child.getChildren()[1]
            const tokenCst = identifierNameCst.getChildren()[0]
            const property = SlimeAstCreateUtils.createIdentifier(tokenCst.getValue(), tokenCst.getLoc())

            return {
                expression: SlimeAstCreateUtils.createMemberExpression(current, dotToken, property)
            }
        }

        // 鎴愬憳璁块棶 .property (Dot token)
        if (name === 'Dot') {
            const dotToken = SlimeTokenCreateUtils.createDotToken(child.getLoc())
            const nextChild = children[index + 1]
            let property: SlimeIdentifier | null = null
            let skipCount = 0

            if (nextChild) {
                if (nextChild.name === SlimeParser.prototype.IdentifierName?.name || nextChild.name === 'IdentifierName') {
                    const tokenCst = nextChild.children?.[0]
                    if (tokenCst) {
                        property = SlimeAstCreateUtils.createIdentifier(tokenCst.getValue(), tokenCst.getLoc())
                    }
                    skipCount = 1
                } else if (nextChild.name === 'PrivateIdentifier') {
                    property = SlimeAstCreateUtils.createIdentifier(nextChild.value, nextChild.loc)
                    skipCount = 1
                } else if (nextChild.value !== undefined) {
                    const identifierName = typeof nextChild.value === 'string' ? nextChild.value : String(nextChild.value)
                    property = SlimeAstCreateUtils.createIdentifier(identifierName, nextChild.loc)
                    skipCount = 1
                }
            }

            return {
                expression: SlimeAstCreateUtils.createMemberExpression(current, dotToken, property),
                nextIndex: index + skipCount
            }
        }

        // 璁＄畻灞炴€ц闂?[expression] (BracketExpression 鏃х増鍏煎)
        if (name === 'BracketExpression') {
            const lBracketCst = child.getChildren()?.find((c: SubhutiCst) => c.name === 'LBracket' || c.value === '[')
            const rBracketCst = child.getChildren()?.find((c: SubhutiCst) => c.name === 'RBracket' || c.value === ']')
            const lBracketToken = lBracketCst ? SlimeTokenCreateUtils.createLBracketToken(lBracketCst.loc) : undefined
            const rBracketToken = rBracketCst ? SlimeTokenCreateUtils.createRBracketToken(rBracketCst.loc) : undefined
            const propertyExpression = SlimeCstToAstUtils.createExpressionAst(child.getChildren()[1])
            return {
                expression: SlimeAstCreateUtils.createComputedMemberExpression(
                    current,
                    propertyExpression,
                    lBracketToken,
                    rBracketToken
                )
            }
        }

        // 璁＄畻灞炴€ц闂?[expression] (LBracket token)
        if (name === 'LBracket') {
            const lBracketToken = SlimeTokenCreateUtils.createLBracketToken(child.getLoc())
            const expressionChild = children[index + 1]
            const rBracketChild = children[index + 2]
            const rBracketToken = rBracketChild && (rBracketChild.name === 'RBracket' || rBracketChild.value === ']')
                ? SlimeTokenCreateUtils.createRBracketToken(rBracketChild.loc)
                : undefined
            if (expressionChild && expressionChild.name !== 'RBracket') {
                const propertyExpression = SlimeCstToAstUtils.createExpressionAst(expressionChild)
                return {
                    expression: SlimeAstCreateUtils.createComputedMemberExpression(
                        current,
                        propertyExpression,
                        lBracketToken,
                        rBracketToken
                    ),
                    nextIndex: rBracketToken ? index + 2 : index + 1
                }
            }
            return { expression: current }
        }

        // 鏍囩妯℃澘 `template`
        if (name === SlimeParser.prototype.TemplateLiteral?.name || name === 'TemplateLiteral') {
            const quasi = SlimeCstToAstUtils.createTemplateLiteralAst(child)
            return {
                expression: {
                    type: 'TaggedTemplateExpression',
                    tag: current,
                    quasi: quasi,
                    loc: loc
                } as any
            }
        }

        // [TypeScript] 闈炵┖鏂█ !
        if (name === 'LogicalNot') {
            return {
                expression: SlimeCstToAstUtils.createTSNonNullExpressionAst(current, loc)
            }
        }

        // [TypeScript] TSAsExpressionTail: as Type
        if (name === 'TSAsExpressionTail') {
            const asChildren = child.getChildren() || []
            const typeCst = asChildren.find((c: SubhutiCst) => c.name === 'TSType')
            const constCst = asChildren.find((c: SubhutiCst) => c.name === 'Const' || c.value === 'const')

            if (constCst) {
                // as const 鏂█
                return {
                    expression: {
                        type: SlimeAstTypeName.TSAsExpression,
                        expression: current,
                        typeAnnotation: {
                            type: 'TSTypeReference',
                            typeName: { type: 'Identifier', name: 'const' }
                        },
                        loc: loc
                    } as any
                }
            } else if (typeCst) {
                return {
                    expression: SlimeCstToAstUtils.createTSAsExpressionAst(current, typeCst, loc)
                }
            }
            return { expression: current }
        }

        // [TypeScript] TSSatisfiesExpressionTail: satisfies Type
        if (name === 'TSSatisfiesExpressionTail') {
            const satisfiesChildren = child.getChildren() || []
            const typeCst = satisfiesChildren.find((c: SubhutiCst) => c.name === 'TSType')
            if (typeCst) {
                return {
                    expression: SlimeCstToAstUtils.createTSSatisfiesExpressionAst(current, typeCst, loc)
                }
            }
            return { expression: current }
        }

        // [TypeScript] TSNonNullExpressionTail: !
        if (name === 'TSNonNullExpressionTail') {
            return {
                expression: SlimeCstToAstUtils.createTSNonNullExpressionAst(current, loc)
            }
        }

        // 璺宠繃 RBracket (宸插湪 LBracket 澶勭悊涓鐞?
        if (name === 'RBracket') {
            return { expression: current }
        }

        // 鏈煡鑺傜偣绫诲瀷
        throw new Error(`SlimePostfixExpressionCstToAst: 鏈煡鐨勫悗缂€鎿嶄綔鑺傜偣绫诲瀷: ${name}`)
    }

    // ========================================
    // Arguments 瑙ｆ瀽锛堜粠 SlimeCallExpressionCstToAst 鍚堝苟锛?
    // ========================================

    /**
     * 瑙ｆ瀽 Arguments CST 涓哄弬鏁版暟缁?
     */
    createArgumentsAst(cst: SubhutiCst): Array<SlimeCallArgument> {
        const expectedName = SlimeParser.prototype.Arguments?.name
        if (cst.getName() !== expectedName && cst.getName() !== 'Arguments') {
            throw new Error(`createArgumentsAst: Expected Arguments, got ${cst.getName()}`)
        }

        let args: Array<SlimeCallArgument> = []
        const first1 = cst.getChildren()[1]
        if (first1 && first1.name === SlimeParser.prototype.ArgumentList?.name) {
            args = this.createArgumentListAst(first1)
        }
        const trailingCommaCst = cst.getChildren().find(ch => ch.name === 'Comma' || ch.value === ',')
        if (trailingCommaCst && args.length > 0) {
            const lastArg: any = args[args.length - 1]
            if (!lastArg.commaToken) {
                lastArg.commaToken = SlimeTokenCreateUtils.createCommaToken(trailingCommaCst.getLoc())
            }
        }
        return args
    }

    /**
     * 瑙ｆ瀽 ArgumentList CST 涓哄弬鏁版暟缁?
     */
    createArgumentListAst(cst: SubhutiCst): Array<SlimeCallArgument> {
        const expectedName = SlimeParser.prototype.ArgumentList?.name
        if (cst.getName() !== expectedName && cst.getName() !== 'ArgumentList') {
            throw new Error(`createArgumentListAst: Expected ArgumentList, got ${cst.getName()}`)
        }

        const arguments_: Array<SlimeCallArgument> = []
        let currentArg: SlimeExpression | SlimeSpreadElement | null = null
        let hasArg = false
        let pendingEllipsis: SubhutiCst | null = null

        for (let i = 0; i < cst.getChildren().length; i++) {
            const child = cst.getChildren()[i]

            if (child.getName() === 'Ellipsis') {
                pendingEllipsis = child
            } else if (child.getName() === SlimeParser.prototype.AssignmentExpression?.name) {
                if (hasArg) {
                    arguments_.push(SlimeAstCreateUtils.createCallArgument(currentArg!, undefined))
                }

                const expr = SlimeCstToAstUtils.createAssignmentExpressionAst(child)
                if (pendingEllipsis) {
                    const ellipsisToken = SlimeTokenCreateUtils.createEllipsisToken(pendingEllipsis.loc)
                    currentArg = SlimeAstCreateUtils.createSpreadElement(expr, child.getLoc(), ellipsisToken)
                    pendingEllipsis = null
                } else {
                    currentArg = expr
                }
                hasArg = true
            } else if (child.getName() === SlimeParser.prototype.SpreadElement?.name) {
                if (hasArg) {
                    arguments_.push(SlimeAstCreateUtils.createCallArgument(currentArg!, undefined))
                }
                currentArg = SlimeCstToAstUtils.createSpreadElementAst(child)
                hasArg = true
            } else if (child.getName() === 'Comma' || child.getValue() === ',') {
                const commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                if (hasArg) {
                    arguments_.push(SlimeAstCreateUtils.createCallArgument(currentArg!, commaToken))
                    hasArg = false
                    currentArg = null
                }
            }
        }

        // 澶勭悊鏈€鍚庝竴涓弬鏁?
        if (hasArg) {
            arguments_.push(SlimeAstCreateUtils.createCallArgument(currentArg!, undefined))
        }

        return arguments_
    }
}

export const SlimePostfixExpressionCstToAst = new SlimePostfixExpressionCstToAstSingle()
