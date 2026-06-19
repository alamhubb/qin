/**
 * PrimaryExpressionCstToAst - 鍩虹琛ㄨ揪寮忚浆鎹?
 */
import {SubhutiCst} from "subhuti";
import {

    SlimeBlockStatement,
    SlimeExpression,
    SlimeFunctionExpression,
    SlimeFunctionParam,
    SlimeIdentifier, SlimeAstTypeName, SlimePattern, SlimeRestElement, SlimeAstCreateUtils, SlimeTokenCreateUtils
} from "slime-ast";
import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

import { SlimeTokenConsumer } from "../../SlimeTokenConsumer.ts";
import {SlimeVariableCstToAstSingle} from "../statements/SlimeVariableCstToAst.ts";

export class SlimePrimaryExpressionCstToAstSingle {
    private createIncompleteExpression(loc: any, reason: string): SlimeExpression {
        return SlimeCstToAstUtils.createIncompleteExpressionAst({ loc } as any, reason)
    }

    private getParenTokens(children?: SubhutiCst[]) {
        let lParenToken: any
        let rParenToken: any
        for (const child of children || []) {
            if (!lParenToken && (child.getName() === 'LParen' || child.getValue() === '(')) {
                lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
            } else if (!rParenToken && (child.getName() === 'RParen' || child.getValue() === ')')) {
                rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
            }
        }
        return { lParenToken, rParenToken }
    }

    private createParenthesizedExpressionWithTokens(expression: SlimeExpression, cst: SubhutiCst): SlimeExpression {
        const node = SlimeAstCreateUtils.createParenthesizedExpression(expression, cst.getLoc()) as any
        const { lParenToken, rParenToken } = this.getParenTokens(cst.getChildren())
        node.lParenToken = lParenToken
        node.rParenToken = rParenToken
        return node
    }

    createPrimaryExpressionAst(cst: SubhutiCst): SlimeExpression {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.PrimaryExpression?.name);
        if (!cst.getChildren() || cst.getChildren().length === 0) {
            return this.createIncompleteExpression(cst.getLoc(), 'PrimaryExpression has no children')
        }
        const first = cst.getChildren()[0]
        if (!first) {
            return this.createIncompleteExpression(cst.getLoc(), 'PrimaryExpression missing first child')
        }
        const firstName = first.getName()
        if (firstName === SlimeParser.prototype.IdentifierReference?.name || firstName === 'IdentifierReference') {
            return SlimeCstToAstUtils.createIdentifierAst(first.getChildren()[0])
        } else if (firstName === SlimeParser.prototype.Literal?.name || firstName === 'Literal') {
            return SlimeCstToAstUtils.createLiteralAst(first)
        } else if (firstName === SlimeParser.prototype.ArrayLiteral?.name || firstName === 'ArrayLiteral') {
            return SlimeCstToAstUtils.createArrayLiteralAst(first) as SlimeExpression
        } else if (firstName === SlimeParser.prototype.FunctionExpression?.name || firstName === 'FunctionExpression') {
            return SlimeCstToAstUtils.createFunctionExpressionAst(first) as SlimeExpression
        } else if (firstName === SlimeParser.prototype.ObjectLiteral?.name || firstName === 'ObjectLiteral') {
            return SlimeCstToAstUtils.createObjectLiteralAst(first) as SlimeExpression
        } else if (firstName === SlimeParser.prototype.ClassExpression?.name || firstName === 'ClassExpression') {
            return SlimeCstToAstUtils.createClassExpressionAst(first) as SlimeExpression
        } else if (firstName === SlimeTokenConsumer.prototype.This?.name || firstName === 'This') {
            // 澶勭悊 this 鍏抽敭锟?
            return SlimeAstCreateUtils.createThisExpression(first.getLoc())
        } else if (firstName === SlimeTokenConsumer.prototype.RegularExpressionLiteral?.name || firstName === 'RegularExpressionLiteral') {
            // 澶勭悊姝ｅ垯琛ㄨ揪寮忓瓧闈㈤噺
            return SlimeCstToAstUtils.createRegExpLiteralAst(first)
        } else if (firstName === SlimeParser.prototype.GeneratorExpression?.name || firstName === 'GeneratorExpression') {
            // 澶勭悊 function* 琛ㄨ揪锟?
            return SlimeCstToAstUtils.createGeneratorExpressionAst(first) as SlimeExpression
        } else if (firstName === SlimeParser.prototype.AsyncFunctionExpression?.name || firstName === 'AsyncFunctionExpression') {
            // 澶勭悊 async function 琛ㄨ揪锟?
            return SlimeCstToAstUtils.createAsyncFunctionExpressionAst(first) as SlimeExpression
        } else if (firstName === SlimeParser.prototype.AsyncGeneratorExpression?.name || firstName === 'AsyncGeneratorExpression') {
            // 澶勭悊 async function* 琛ㄨ揪锟?
            return SlimeCstToAstUtils.createAsyncGeneratorExpressionAst(first) as SlimeExpression
        } else if (firstName === SlimeParser.prototype.CoverParenthesizedExpressionAndArrowParameterList?.name ||
            firstName === 'CoverParenthesizedExpressionAndArrowParameterList') {
            // Cover Grammar - try to interpret as parenthesized expression
            // Structure varies: [LParen, content?, RParen] or [LParen, Expression, RParen]

            // Empty parentheses: ()
            if (!first.getChildren() || first.getChildren().length === 0) {
                return SlimeAstCreateUtils.createIdentifier('undefined', first.getLoc())
            }

            // Only 2 children (empty parens): LParen, RParen
            if (first.getChildren().length === 2) {
                return SlimeAstCreateUtils.createIdentifier('undefined', first.getLoc())
            }

            // Find the content (skip LParen at start, RParen at end)
            const middleCst = first.getChildren()[1]
            if (!middleCst) {
                return SlimeAstCreateUtils.createIdentifier('undefined', first.getLoc())
            }

            // If it's an Expression, process it directly
            if (middleCst.getName() === SlimeParser.prototype.Expression?.name || middleCst.getName() === 'Expression') {
                const innerExpr = SlimeCstToAstUtils.createExpressionAst(middleCst)
                return this.createParenthesizedExpressionWithTokens(innerExpr, first)
            }

            // If it's AssignmentExpression, process it
            if (middleCst.getName() === SlimeParser.prototype.AssignmentExpression?.name || middleCst.getName() === 'AssignmentExpression') {
                const innerExpr = SlimeCstToAstUtils.createExpressionAst(middleCst)
                return this.createParenthesizedExpressionWithTokens(innerExpr, first)
            }

            // If it's FormalParameterList, convert to expression
            if (middleCst.getName() === SlimeParser.prototype.FormalParameterList?.name || middleCst.getName() === 'FormalParameterList') {
                const params = SlimeCstToAstUtils.createFormalParameterListAst(middleCst)
                if (params.length === 1 && params[0].type === SlimeAstTypeName.Identifier) {
                    return this.createParenthesizedExpressionWithTokens(params[0] as any, first)
                }
                if (params.length > 1) {
                    const expressions = params.map(p => p as any)
                    return this.createParenthesizedExpressionWithTokens({
                        type: 'SequenceExpression',
                        expressions: expressions
                    } as any, first)
                }
                return SlimeAstCreateUtils.createIdentifier('undefined', first.getLoc())
            }

            // Try to process the middle content as an expression
            try {
                const innerExpr = SlimeCstToAstUtils.createExpressionAst(middleCst)
                return this.createParenthesizedExpressionWithTokens(innerExpr, first)
            } catch (e) {
                // Fallback: return the first child as identifier
                return SlimeAstCreateUtils.createIdentifier('undefined', first.getLoc())
            }
        } else if (firstName === SlimeParser.prototype.TemplateLiteral?.name || firstName === 'TemplateLiteral') {
            // 澶勭悊妯℃澘瀛楃锟?
            return SlimeCstToAstUtils.createTemplateLiteralAst(first)
        } else if (firstName === SlimeParser.prototype.ParenthesizedExpression?.name || firstName === 'ParenthesizedExpression') {
            // 澶勭悊鏅€氭嫭鍙疯〃杈惧紡锟? Expression )
            // children[0]=LParen, children[1]=Expression, children[2]=RParen
            const expressionCst = first.getChildren()[1]
            const innerExpression = SlimeCstToAstUtils.createExpressionAst(expressionCst)
            return this.createParenthesizedExpressionWithTokens(innerExpression, first)
        } else if (firstName === 'RegularExpressionLiteral') {
            // 澶勭悊姝ｅ垯琛ㄨ揪寮忓瓧闈㈤噺
            return SlimeCstToAstUtils.createRegExpLiteralAst(first)
        } else {
            throw new Error('鏈煡鐨?PrimaryExpression 绫诲瀷: ' + first.getName())
        }
    }

    /**
     * ParenthesizedExpression CST 锟?AST
     * ParenthesizedExpression -> ( Expression )
     */
    createParenthesizedExpressionAst(cst: SubhutiCst): SlimeExpression {
        // 鏌ユ壘鍐呴儴锟?Expression
        for (const child of cst.getChildren() || []) {
            if (child.getName() === SlimeParser.prototype.Expression?.name ||
                child.getName() === 'Expression' ||
                child.getName() === SlimeParser.prototype.AssignmentExpression?.name) {
                const innerExpression = SlimeCstToAstUtils.createExpressionAst(child)
                return this.createParenthesizedExpressionWithTokens(innerExpression, cst)
            }
        }
        // 濡傛灉娌℃湁鎵惧埌 Expression锛屽彲鑳芥槸绌烘嫭鍙锋垨鑰呯洿鎺ュ寘鍚叾浠栬〃杈惧紡
        const innerExpr = cst.getChildren()?.find(ch =>
            ch.name !== 'LParen' && ch.name !== 'RParen' && ch.value !== '(' && ch.value !== ')'
        )
        if (innerExpr) {
            const innerExpression = SlimeCstToAstUtils.createExpressionAst(innerExpr)
            return this.createParenthesizedExpressionWithTokens(innerExpression, cst)
        }
        throw new Error('ParenthesizedExpression has no inner expression')
    }


    /**
     * CoverParenthesizedExpressionAndArrowParameterList CST 锟?AST
     * 杩欐槸涓€锟?cover grammar锛屾牴鎹笂涓嬫枃鍙兘鏄嫭鍙疯〃杈惧紡鎴栫澶村嚱鏁板弬锟?
     */
    createCoverParenthesizedExpressionAndArrowParameterListAst(cst: SubhutiCst): SlimeExpression {
        // 閫氬父浣滀负鎷彿琛ㄨ揪寮忓鐞嗭紝绠ご鍑芥暟鍙傛暟鏈変笓闂ㄧ殑澶勭悊璺緞
        return SlimeCstToAstUtils.createParenthesizedExpressionAst(cst)
    }


    /**
     * 鍦‥xpression涓煡鎵剧涓€涓狪dentifier锛堣緟鍔╂柟娉曪級
     */
    findFirstIdentifierInExpression(cst: SubhutiCst): SubhutiCst | null {
        if (cst.getName() === SlimeTokenConsumer.prototype.IdentifierName?.name) {
            return cst
        }
        if (cst.getChildren()) {
            for (const child of cst.getChildren()) {
                const found = SlimeCstToAstUtils.findFirstIdentifierInExpression(child)
                if (found) return found
            }
        }
        return null
    }

}

export const SlimePrimaryExpressionCstToAst = new SlimePrimaryExpressionCstToAstSingle()
