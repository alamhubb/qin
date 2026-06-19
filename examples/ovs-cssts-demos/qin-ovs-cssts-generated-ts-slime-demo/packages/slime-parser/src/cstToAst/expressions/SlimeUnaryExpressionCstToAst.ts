/**
 * UnaryExpressionCstToAst - 涓€鍏?鏇存柊琛ㄨ揪寮忚浆鎹?
 */
import { SubhutiCst } from "subhuti";
import {
    SlimeAstCreateUtils,
    type SlimeBlockStatement,
    SlimeExpression,
    type SlimeFunctionExpression,
    type SlimeFunctionParam,
    type SlimeIdentifier, SlimeAstTypeName, SlimeTokenCreateUtils
} from "slime-ast";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import { SlimeTokenConsumer } from "../../SlimeTokenConsumer.ts";
import { SlimeVariableCstToAstSingle } from "../statements/SlimeVariableCstToAst.ts";

export class SlimeUnaryExpressionCstToAstSingle {
    private createIncompleteExpression(loc: any, reason: string): SlimeExpression {
        return SlimeCstToAstUtils.createIncompleteExpressionAst({ loc } as any, reason)
    }

    private createUnaryPlaceholder(operatorToken: any, cstLoc: any): SlimeExpression {
        const operatorMap: { [key: string]: string } = {
            Exclamation: '!',
            Plus: '+',
            Minus: '-',
            Tilde: '~',
            Typeof: 'typeof',
            Void: 'void',
            Delete: 'delete',
            PlusPlus: '++',
            MinusMinus: '--',
        }
        const operatorValue = operatorMap[operatorToken?.name] || operatorToken?.value || '+'
        const placeholderLoc = SlimeCstToAstUtils.resolveBestLoc(cstLoc, operatorToken?.loc)
        const operator = SlimeTokenCreateUtils.createUnaryOperatorToken(
            operatorValue as any,
            SlimeCstToAstUtils.resolveBestLoc(operatorToken?.loc, cstLoc)
        )
        const node = {
            type: SlimeAstTypeName.UnaryExpression,
            operator,
            prefix: true,
            argument: null,
            loc: placeholderLoc
        } as any
        return node
    }

    createUnaryExpressionAst(cst: SubhutiCst): SlimeExpression {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.UnaryExpression?.name);

        // Defensive fallback for incomplete CST in edit-half states.
        if (!cst.getChildren() || cst.getChildren().length === 0) {
            return this.createIncompleteExpression(cst.getLoc(), 'UnaryExpression has no children')
        }

        const operatorMap: { [key: string]: string } = {
            'Exclamation': '!',
            'Plus': '+',
            'Minus': '-',
            'Tilde': '~',
            'Typeof': 'typeof',
            'Void': 'void',
            'Delete': 'delete',
            'PlusPlus': '++',
            'MinusMinus': '--',
        }

        // Single child can be either a nested expression or an incomplete operator token.
        if (cst.getChildren().length === 1) {
            const child = cst.getChildren()[0]

            // Token child means parser accepted operator but operand is missing.
            if (child.getValue() !== undefined && !child.getChildren()) {
                return this.createUnaryPlaceholder(child, cst.getLoc())
            }

            // Expression child: recurse as normal.
            return SlimeCstToAstUtils.createExpressionAst(child)
        }

        // children[0]: unary operator token
        // children[1]: UnaryExpression operand
        const operatorToken = cst.getChildren()[0]
        const argumentCst = cst.getChildren()[1]

        if (!argumentCst) {
            return this.createUnaryPlaceholder(operatorToken, cst.getLoc())
        }

        const operatorValue = operatorMap[operatorToken.name] || operatorToken.value
        const operatorLoc = operatorToken.loc
        const operator = SlimeTokenCreateUtils.createUnaryOperatorToken(operatorValue as any, operatorLoc)

        // Recurse operand.
        const argument = SlimeCstToAstUtils.createExpressionAst(argumentCst)

        // 鍒涘缓 UnaryExpression AST
        return {
            type: SlimeAstTypeName.UnaryExpression,
            operator: operator,
            prefix: true,  // 鍓嶇紑杩愮畻锟?
            argument: argument,
            loc: SlimeCstToAstUtils.resolveBestLoc(cst.getLoc(), operatorLoc, argument?.loc)
        } as any
    }

    // Renamed from createPostfixExpressionAst - ES2025 uses UpdateExpression
    createUpdateExpressionAst(cst: SubhutiCst): SlimeExpression {
        // Support both PostfixExpression (old) and UpdateExpression (new)
        if (!cst.getChildren() || cst.getChildren().length === 0) {
            return this.createIncompleteExpression(cst.getLoc(), 'UpdateExpression has no children')
        }
        if (cst.getChildren().length > 1) {
            const first = cst.getChildren()[0]
            const isPrefix = first.getLoc()?.type === 'PlusPlus' || first.getLoc()?.type === 'MinusMinus' ||
                first.getValue() === '++' || first.getValue() === '--'

            if (isPrefix) {
                // Prefix: ++argument or --argument
                const operatorValue = first.getValue() || first.getLoc()?.value
                const operator = SlimeTokenCreateUtils.createUpdateOperatorToken(operatorValue as any, first.getLoc())
                const argument = cst.getChildren()[1]
                    ? SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[1])
                    : this.createIncompleteExpression(first.getLoc() || cst.getLoc(), 'prefix update missing operand')
                return {
                    type: SlimeAstTypeName.UpdateExpression,
                    operator: operator,
                    argument: argument,
                    prefix: true,
                    loc: SlimeCstToAstUtils.resolveBestLoc(cst.getLoc(), first?.loc, argument?.loc)
        } as any
            } else {
                // Postfix: argument++ or argument--
                // 鎴?TypeScript 琛ㄨ揪寮忓熬閮?(as/satisfies/!)
                const argument = SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[0])

                // 妫€鏌ユ槸鍚︽槸 ++ 鎴?-- 杩愮畻绗?
                const second = cst.getChildren()[1]
                if (second && (second.loc?.type === 'PlusPlus' || second.loc?.type === 'MinusMinus' ||
                    second.value === '++' || second.value === '--')) {
                    const operatorValue = second.value || second.loc?.value
                    const operator = SlimeTokenCreateUtils.createUpdateOperatorToken(operatorValue as any, second.loc)
                    return {
                        type: SlimeAstTypeName.UpdateExpression,
                        operator: operator,
                        argument: argument,
                        prefix: false,
                        loc: SlimeCstToAstUtils.resolveBestLoc(cst.getLoc(), second?.loc, argument?.loc)
        } as any
                }

                // 浣跨敤缁熶竴鍚庣紑澶勭悊鍣ㄥ鐞?TypeScript 琛ㄨ揪寮忓熬閮?
                return SlimeCstToAstUtils.processPostfixOperations(argument, cst.getChildren(), 1, cst.getLoc())
            }
        }
        return cst.getChildren()[0]
            ? SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[0])
            : this.createIncompleteExpression(cst.getLoc(), 'postfix update missing operand')
    }

    createYieldExpressionAst(cst: SubhutiCst): any {
        // yield [*] AssignmentExpression?
        let yieldToken: any = undefined
        let asteriskToken: any = undefined
        let delegate = false
        let startIndex = 1

        // 鎻愬彇 yield token
        if (cst.getChildren()[0] && (cst.getChildren()[0].name === 'Yield' || cst.getChildren()[0].value === 'yield')) {
            yieldToken = SlimeTokenCreateUtils.createYieldToken(cst.getChildren()[0].loc)
        }

        if (cst.getChildren()[1] && cst.getChildren()[1].name === SlimeTokenConsumer.prototype.Asterisk?.name) {
            asteriskToken = SlimeTokenCreateUtils.createAsteriskToken(cst.getChildren()[1].loc)
            delegate = true
            startIndex = 2
        }
        let argument: any = null
        if (cst.getChildren()[startIndex]) {
            argument = SlimeCstToAstUtils.createAssignmentExpressionAst(cst.getChildren()[startIndex])
        }

        return SlimeAstCreateUtils.createYieldExpression(argument, delegate, cst.getLoc(), yieldToken, asteriskToken)
    }

    createAwaitExpressionAst(cst: SubhutiCst): any {
        // await UnaryExpression
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.AwaitExpression?.name);

        let awaitToken: any = undefined

        // 鎻愬彇 await token
        if (cst.getChildren()[0] && (cst.getChildren()[0].name === 'Await' || cst.getChildren()[0].value === 'await')) {
            awaitToken = SlimeTokenCreateUtils.createAwaitToken(cst.getChildren()[0].loc)
        }

        const argumentCst = cst.getChildren()[1]
        const argument = SlimeCstToAstUtils.createExpressionAst(argumentCst)

        return SlimeAstCreateUtils.createAwaitExpression(argument, cst.getLoc(), awaitToken)
    }


}


export const SlimeUnaryExpressionCstToAst = new SlimeUnaryExpressionCstToAstSingle()
