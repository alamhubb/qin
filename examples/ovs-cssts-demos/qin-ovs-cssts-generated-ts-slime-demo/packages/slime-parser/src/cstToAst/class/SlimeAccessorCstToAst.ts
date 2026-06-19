/**
 * SlimeAccessorCstToAst - 璁块棶鍣ㄦ柟娉曞畾涔?
 *
 * 璐熻矗锛?
 * - getter 鏂规硶
 * - setter 鏂规硶
 * - async 鏂规硶
 * - generator 鏂规硶
 */
import {SubhutiCst} from "subhuti";
import {
    SlimeAstCreateUtils,
    type SlimeBlockStatement, SlimeExpression, SlimeFunctionExpression, SlimeFunctionParam, type SlimeIdentifier, SlimeLiteral,
    SlimeMethodDefinition,
    SlimePattern,
    SlimeTokenCreateUtils
} from "slime-ast";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";

export class SlimeAccessorCstToAstSingle {
    private extractComputedPropertyNameBracketTokens(cst: SubhutiCst): { lBracketToken?: any; rBracketToken?: any } {
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined
        const stack: SubhutiCst[] = [cst]
        while (stack.length > 0) {
            const node = stack.pop()!
            if (node.name === 'LBracket' || node.value === '[') {
                if (!lBracketToken) {
                    lBracketToken = SlimeTokenCreateUtils.createLBracketToken(node.loc)
                }
            } else if (node.name === 'RBracket' || node.value === ']') {
                if (!rBracketToken) {
                    rBracketToken = SlimeTokenCreateUtils.createRBracketToken(node.loc)
                }
            }
            if (node.children) {
                for (const child of node.children) {
                    stack.push(child)
                }
            }
        }
        return { lBracketToken, rBracketToken }
    }


    /**
     * [鍐呴儴鏂规硶] getter 鏂规硶
     * 澶勭悊 ES2025 Parser 鐨?get ClassElementName ( ) TSTypeAnnotation_opt { FunctionBody } 缁撴瀯
     * [TypeScript] 鏀寔杩斿洖绫诲瀷娉ㄨВ
     * @internal
     */
    createMethodDefinitionGetterMethodAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        const children = cst.getChildren()

        // Token fields
        let staticToken: any = undefined
        let getToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined
        let returnType: any = undefined  // [TypeScript] 杩斿洖绫诲瀷

        // 妫€鏌?token
        if (staticCst && (staticCst.name === 'Static' || staticCst.value === 'static')) {
            staticToken = SlimeTokenCreateUtils.createStaticToken(staticCst.loc)
        }

        let classElementNameCst: SubhutiCst | null = null
        let bodyCst: SubhutiCst | null = null

        for (const child of children) {
            const name = child.getName()
            if (name === 'Get' || child.getValue() === 'get') {
                getToken = SlimeTokenCreateUtils.createGetToken(child.getLoc())
            } else if (name === 'ClassElementName' || name === SlimeParser.prototype.ClassElementName?.name) {
                classElementNameCst = child
            } else if (name === 'LParen' || child.getValue() === '(') {
                lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
            } else if (name === 'RParen' || child.getValue() === ')') {
                rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
            } else if (name === 'LBrace' || child.getValue() === '{') {
                lBraceToken = SlimeTokenCreateUtils.createLBraceToken(child.getLoc())
            } else if (name === 'RBrace' || child.getValue() === '}') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(child.getLoc())
            } else if (name === 'FunctionBody' || name === SlimeParser.prototype.FunctionBody?.name) {
                bodyCst = child
            } else if (name === 'TSTypeAnnotation') {
                // [TypeScript] 杩斿洖绫诲瀷娉ㄨВ
                returnType = SlimeCstToAstUtils.createTSTypeAnnotationAst(child)
            }
        }

        if (!classElementNameCst) {
            throw new Error('Getter missing ClassElementName')
        }

        const key = SlimeCstToAstUtils.createClassElementNameAst(classElementNameCst)
        const isComputed = SlimeCstToAstUtils.isComputedPropertyName(classElementNameCst)
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined
        if (isComputed) {
            const bracketTokens = this.extractComputedPropertyNameBracketTokens(classElementNameCst)
            lBracketToken = bracketTokens.lBracketToken
            rBracketToken = bracketTokens.rBracketToken
        }

        // 瑙ｆ瀽鍑芥暟浣?
        let body: SlimeBlockStatement
        if (bodyCst) {
            const bodyStatements = SlimeCstToAstUtils.createFunctionBodyAst(bodyCst)
            body = SlimeAstCreateUtils.createBlockStatement(bodyStatements, cst.getLoc(), lBraceToken, rBraceToken)
        } else {
            body = SlimeAstCreateUtils.createBlockStatement([], undefined, lBraceToken, rBraceToken)
        }

        // 鍒涘缓鍑芥暟琛ㄨ揪寮忥紝浼犻€?token 淇℃伅
        const functionExpression = SlimeAstCreateUtils.createFunctionExpression(
            body, null, [], false, false, cst.getLoc(),
            undefined, undefined, undefined, lParenToken, rParenToken, lBraceToken, rBraceToken
        ) as SlimeFunctionExpression & { returnType?: any }

        // [TypeScript] 娣诲姞杩斿洖绫诲瀷
        if (returnType) {
            functionExpression.returnType = returnType
        }

        const methodDef = SlimeAstCreateUtils.createMethodDefinition(key, functionExpression, 'get', isComputed, SlimeCstToAstUtils.isStaticModifier(staticCst), cst.getLoc(), staticToken, getToken)

        if (lBracketToken) {
            (methodDef as any).lBracketToken = lBracketToken
        }
        if (rBracketToken) {
            (methodDef as any).rBracketToken = rBracketToken
        }
        return methodDef
    }


    /**
     * [鍐呴儴鏂规硶] setter 鏂规硶
     * 澶勭悊 ES2025 Parser 鐨?set ClassElementName ( PropertySetParameterList ) { FunctionBody } 缁撴瀯
     * @internal
     */
    createMethodDefinitionSetterMethodAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        // children: [SetTok, ClassElementName, LParen, PropertySetParameterList, RParen, LBrace, FunctionBody?, RBrace]
        const children = cst.getChildren()
        let i = 0

        // Token fields
        let staticToken: any = undefined
        let setToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        // 妫€鏌?token
        if (staticCst && (staticCst.name === 'Static' || staticCst.value === 'static')) {
            staticToken = SlimeTokenCreateUtils.createStaticToken(staticCst.loc)
        }

        // SetTok
        if (children[i]?.name === 'Set' || children[i]?.value === 'set') {
            setToken = SlimeTokenCreateUtils.createSetToken(children[i].loc)
            i++
        }

        const classElementNameCst = children[i++]
        const key = SlimeCstToAstUtils.createClassElementNameAst(classElementNameCst)
        const isComputed = SlimeCstToAstUtils.isComputedPropertyName(classElementNameCst)
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined
        if (isComputed) {
            const bracketTokens = this.extractComputedPropertyNameBracketTokens(classElementNameCst)
            lBracketToken = bracketTokens.lBracketToken
            rBracketToken = bracketTokens.rBracketToken
        }

        // LParen - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'LParen') {
            lParenToken = SlimeTokenCreateUtils.createLParenToken(children[i].loc)
            i++
        }

        // PropertySetParameterList
        let params: SlimePattern[] = []
        if (children[i]?.name === 'PropertySetParameterList' || children[i]?.name === SlimeParser.prototype.PropertySetParameterList?.name) {
            params = SlimeCstToAstUtils.createPropertySetParameterListAst(children[i])
            i++
        }

        // RParen - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'RParen') {
            rParenToken = SlimeTokenCreateUtils.createRParenToken(children[i].loc)
            i++
        }
        // LBrace - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'LBrace') {
            lBraceToken = SlimeTokenCreateUtils.createLBraceToken(children[i].loc)
            i++
        }

        // FunctionBody
        let body: SlimeBlockStatement
        if (children[i]?.name === 'FunctionBody' || children[i]?.name === SlimeParser.prototype.FunctionBody?.name) {
            const bodyStatements = SlimeCstToAstUtils.createFunctionBodyAst(children[i])
            i++
            // RBrace
            if (children[i]?.name === 'RBrace') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(children[i].loc)
            }
            body = SlimeAstCreateUtils.createBlockStatement(bodyStatements, cst.getLoc(), lBraceToken, rBraceToken)
        } else {
            if (children[i]?.name === 'RBrace') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(children[i].loc)
            }
            body = SlimeAstCreateUtils.createBlockStatement([], undefined, lBraceToken, rBraceToken)
        }

        // 鍒涘缓鍑芥暟琛ㄨ揪寮忥紝浼犻€?token 淇℃伅
        const functionExpression = SlimeAstCreateUtils.createFunctionExpression(
            body, null, params as any, false, false, cst.getLoc(),
            undefined, undefined, undefined, lParenToken, rParenToken, lBraceToken, rBraceToken
        )

        const methodDef = SlimeAstCreateUtils.createMethodDefinition(key, functionExpression, 'set', isComputed, SlimeCstToAstUtils.isStaticModifier(staticCst), cst.getLoc(), staticToken, undefined, setToken)

        if (lBracketToken) {
            (methodDef as any).lBracketToken = lBracketToken
        }
        if (rBracketToken) {
            (methodDef as any).rBracketToken = rBracketToken
        }
        return methodDef
    }


    /**
     * [鍐呴儴鏂规硶] async 鏂规硶
     * 澶勭悊 ES2025 Parser 鐨?async ClassElementName ( UniqueFormalParameters ) { AsyncFunctionBody } 缁撴瀯
     * @internal
     */
    createMethodDefinitionAsyncMethodAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        // AsyncMethod children: [AsyncTok, ClassElementName, LParen, UniqueFormalParameters?, RParen, LBrace, AsyncFunctionBody, RBrace]
        const children = cst.getChildren()
        let i = 0

        // Token fields
        let staticToken: any = undefined
        let asyncToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        // 妫€鏌?token
        if (staticCst && (staticCst.name === 'Static' || staticCst.value === 'static')) {
            staticToken = SlimeTokenCreateUtils.createStaticToken(staticCst.loc)
        }

        // AsyncTok
        if (children[i]?.name === 'Async' || children[i]?.value === 'async') {
            asyncToken = SlimeTokenCreateUtils.createAsyncToken(children[i].loc)
            i++
        }

        const classElementNameCst = children[i++]
        const key = SlimeCstToAstUtils.createClassElementNameAst(classElementNameCst)
        const isComputed = SlimeCstToAstUtils.isComputedPropertyName(classElementNameCst)
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined
        if (isComputed) {
            const bracketTokens = this.extractComputedPropertyNameBracketTokens(classElementNameCst)
            lBracketToken = bracketTokens.lBracketToken
            rBracketToken = bracketTokens.rBracketToken
        }

        // LParen - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'LParen') {
            lParenToken = SlimeTokenCreateUtils.createLParenToken(children[i].loc)
            i++
        }

        // UniqueFormalParameters
        let params: SlimeFunctionParam[] = []
        if (children[i]?.name === 'UniqueFormalParameters' || children[i]?.name === SlimeParser.prototype.UniqueFormalParameters?.name) {
            params = SlimeCstToAstUtils.createUniqueFormalParametersAstWrapped(children[i])
            i++
        } else if (children[i]?.name === 'FormalParameters' || children[i]?.name === SlimeParser.prototype.FormalParameters?.name) {
            params = SlimeCstToAstUtils.createFormalParametersAstWrapped(children[i])
            i++
        }

        // RParen - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'RParen') {
            rParenToken = SlimeTokenCreateUtils.createRParenToken(children[i].loc)
            i++
        }
        // LBrace - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'LBrace') {
            lBraceToken = SlimeTokenCreateUtils.createLBraceToken(children[i].loc)
            i++
        }

        // AsyncFunctionBody 鎴?FunctionBody
        let body: SlimeBlockStatement
        const bodyChild = children[i]
        if (bodyChild?.name === 'AsyncFunctionBody' || bodyChild?.name === SlimeParser.prototype.AsyncFunctionBody?.name ||
            bodyChild?.name === 'FunctionBody' || bodyChild?.name === SlimeParser.prototype.FunctionBody?.name) {
            const bodyStatements = SlimeCstToAstUtils.createFunctionBodyAst(bodyChild)
            i++
            // RBrace
            if (children[i]?.name === 'RBrace') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(children[i].loc)
            }
            body = SlimeAstCreateUtils.createBlockStatement(bodyStatements, cst.getLoc(), lBraceToken, rBraceToken)
        } else {
            if (children[i]?.name === 'RBrace') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(children[i].loc)
            }
            body = SlimeAstCreateUtils.createBlockStatement([], undefined, lBraceToken, rBraceToken)
        }

        // 鍒涘缓鍑芥暟琛ㄨ揪寮忥紝浼犻€?token 淇℃伅
        const functionExpression = SlimeAstCreateUtils.createFunctionExpression(
            body, null, params as any, false, true, cst.getLoc(),
            undefined, asyncToken, undefined, lParenToken, rParenToken, lBraceToken, rBraceToken
        )

        const methodDef = SlimeAstCreateUtils.createMethodDefinition(
            key,
            functionExpression,
            'method',
            isComputed,
            SlimeCstToAstUtils.isStaticModifier(staticCst),
            cst.getLoc(),
            staticToken,
            undefined,
            undefined,
            asyncToken,
            undefined
        )

        if (lBracketToken) {
            (methodDef as any).lBracketToken = lBracketToken
        }
        if (rBracketToken) {
            (methodDef as any).rBracketToken = rBracketToken
        }
        return methodDef
    }


    /**
     * [鍐呴儴鏂规硶] generator 鏂规硶
     * 澶勭悊 ES2025 Parser 鐨?* ClassElementName ( UniqueFormalParameters ) { GeneratorBody } 缁撴瀯
     * @internal
     */
    createMethodDefinitionGeneratorMethodAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        // GeneratorMethod children: [Asterisk, ClassElementName, LParen, UniqueFormalParameters?, RParen, LBrace, GeneratorBody, RBrace]
        const children = cst.getChildren()
        let i = 0

        // Token fields
        let staticToken: any = undefined
        let asteriskToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        // 妫€鏌?token
        if (staticCst && (staticCst.name === 'Static' || staticCst.value === 'static')) {
            staticToken = SlimeTokenCreateUtils.createStaticToken(staticCst.loc)
        }

        // Asterisk
        if (children[i]?.name === 'Asterisk' || children[i]?.value === '*') {
            asteriskToken = SlimeTokenCreateUtils.createAsteriskToken(children[i].loc)
            i++
        }

        const classElementNameCst = children[i++]
        const key = SlimeCstToAstUtils.createClassElementNameAst(classElementNameCst)
        const isComputed = SlimeCstToAstUtils.isComputedPropertyName(classElementNameCst)
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined
        if (isComputed) {
            const bracketTokens = this.extractComputedPropertyNameBracketTokens(classElementNameCst)
            lBracketToken = bracketTokens.lBracketToken
            rBracketToken = bracketTokens.rBracketToken
        }

        // LParen - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'LParen') {
            lParenToken = SlimeTokenCreateUtils.createLParenToken(children[i].loc)
            i++
        }

        // UniqueFormalParameters
        let params: SlimeFunctionParam[] = []
        if (children[i]?.name === 'UniqueFormalParameters' || children[i]?.name === SlimeParser.prototype.UniqueFormalParameters?.name) {
            params = SlimeCstToAstUtils.createUniqueFormalParametersAstWrapped(children[i])
            i++
        } else if (children[i]?.name === 'FormalParameters' || children[i]?.name === SlimeParser.prototype.FormalParameters?.name) {
            params = SlimeCstToAstUtils.createFormalParametersAstWrapped(children[i])
            i++
        }

        // RParen - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'RParen') {
            rParenToken = SlimeTokenCreateUtils.createRParenToken(children[i].loc)
            i++
        }
        // LBrace - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'LBrace') {
            lBraceToken = SlimeTokenCreateUtils.createLBraceToken(children[i].loc)
            i++
        }

        // GeneratorBody 鎴?FunctionBody
        let body: SlimeBlockStatement
        const bodyChild = children[i]
        if (bodyChild?.name === 'GeneratorBody' || bodyChild?.name === SlimeParser.prototype.GeneratorBody?.name ||
            bodyChild?.name === 'FunctionBody' || bodyChild?.name === SlimeParser.prototype.FunctionBody?.name) {
            const bodyStatements = SlimeCstToAstUtils.createFunctionBodyAst(bodyChild)
            i++
            // RBrace
            if (children[i]?.name === 'RBrace') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(children[i].loc)
            }
            body = SlimeAstCreateUtils.createBlockStatement(bodyStatements, cst.getLoc(), lBraceToken, rBraceToken)
        } else {
            if (children[i]?.name === 'RBrace') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(children[i].loc)
            }
            body = SlimeAstCreateUtils.createBlockStatement([], undefined, lBraceToken, rBraceToken)
        }

        // 鍒涘缓鍑芥暟琛ㄨ揪寮忥紝浼犻€?token 淇℃伅
        const functionExpression = SlimeAstCreateUtils.createFunctionExpression(
            body, null, params as any, true, false, cst.getLoc(),
            undefined, undefined, asteriskToken, lParenToken, rParenToken, lBraceToken, rBraceToken
        )

        const methodDef = SlimeAstCreateUtils.createMethodDefinition(
            key,
            functionExpression,
            'method',
            isComputed,
            SlimeCstToAstUtils.isStaticModifier(staticCst),
            cst.getLoc(),
            staticToken,
            undefined,
            undefined,
            undefined,
            asteriskToken
        )

        if (lBracketToken) {
            (methodDef as any).lBracketToken = lBracketToken
        }
        if (rBracketToken) {
            (methodDef as any).rBracketToken = rBracketToken
        }
        return methodDef
    }


    /**
     * [鍐呴儴鏂规硶] async generator 鏂规硶
     * 澶勭悊 ES2025 Parser 鐨?async * ClassElementName ( ... ) { AsyncGeneratorBody } 缁撴瀯
     * @internal
     */
    createMethodDefinitionAsyncGeneratorMethodAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        // AsyncGeneratorMethod children: [AsyncTok, Asterisk, ClassElementName, LParen, UniqueFormalParameters?, RParen, LBrace, AsyncGeneratorBody, RBrace]
        const children = cst.getChildren()
        let i = 0

        // Token fields
        let staticToken: any = undefined
        let asyncToken: any = undefined
        let asteriskToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        // 妫€鏌?token
        if (staticCst && (staticCst.name === 'Static' || staticCst.value === 'static')) {
            staticToken = SlimeTokenCreateUtils.createStaticToken(staticCst.loc)
        }

        // AsyncTok
        if (children[i]?.name === 'Async' || children[i]?.value === 'async') {
            asyncToken = SlimeTokenCreateUtils.createAsyncToken(children[i].loc)
            i++
        }

        // Asterisk
        if (children[i]?.name === 'Asterisk' || children[i]?.value === '*') {
            asteriskToken = SlimeTokenCreateUtils.createAsteriskToken(children[i].loc)
            i++
        }

        const classElementNameCst = children[i++]
        const key = SlimeCstToAstUtils.createClassElementNameAst(classElementNameCst)
        const isComputed = SlimeCstToAstUtils.isComputedPropertyName(classElementNameCst)
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined
        if (isComputed) {
            const bracketTokens = this.extractComputedPropertyNameBracketTokens(classElementNameCst)
            lBracketToken = bracketTokens.lBracketToken
            rBracketToken = bracketTokens.rBracketToken
        }

        // LParen - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'LParen') {
            lParenToken = SlimeTokenCreateUtils.createLParenToken(children[i].loc)
            i++
        }

        // UniqueFormalParameters
        let params: SlimeFunctionParam[] = []
        if (children[i]?.name === 'UniqueFormalParameters' || children[i]?.name === SlimeParser.prototype.UniqueFormalParameters?.name) {
            params = SlimeCstToAstUtils.createUniqueFormalParametersAstWrapped(children[i])
            i++
        } else if (children[i]?.name === 'FormalParameters' || children[i]?.name === SlimeParser.prototype.FormalParameters?.name) {
            params = SlimeCstToAstUtils.createFormalParametersAstWrapped(children[i])
            i++
        }

        // RParen - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'RParen') {
            rParenToken = SlimeTokenCreateUtils.createRParenToken(children[i].loc)
            i++
        }
        // LBrace - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'LBrace') {
            lBraceToken = SlimeTokenCreateUtils.createLBraceToken(children[i].loc)
            i++
        }

        // AsyncGeneratorBody 鎴?FunctionBody
        let body: SlimeBlockStatement
        const bodyChild = children[i]
        if (bodyChild?.name === 'AsyncGeneratorBody' || bodyChild?.name === SlimeParser.prototype.AsyncGeneratorBody?.name ||
            bodyChild?.name === 'FunctionBody' || bodyChild?.name === SlimeParser.prototype.FunctionBody?.name) {
            const bodyStatements = SlimeCstToAstUtils.createFunctionBodyAst(bodyChild)
            i++
            // RBrace
            if (children[i]?.name === 'RBrace') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(children[i].loc)
            }
            body = SlimeAstCreateUtils.createBlockStatement(bodyStatements, cst.getLoc(), lBraceToken, rBraceToken)
        } else {
            if (children[i]?.name === 'RBrace') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(children[i].loc)
            }
            body = SlimeAstCreateUtils.createBlockStatement([], undefined, lBraceToken, rBraceToken)
        }

        // 鍒涘缓鍑芥暟琛ㄨ揪寮忥紝浼犻€?token 淇℃伅
        const functionExpression = SlimeAstCreateUtils.createFunctionExpression(
            body, null, params as any, true, true, cst.getLoc(),
            undefined, asyncToken, asteriskToken, lParenToken, rParenToken, lBraceToken, rBraceToken
        )

        const methodDef = SlimeAstCreateUtils.createMethodDefinition(
            key,
            functionExpression,
            'method',
            isComputed,
            SlimeCstToAstUtils.isStaticModifier(staticCst),
            cst.getLoc(),
            staticToken,
            undefined,
            undefined,
            asyncToken,
            asteriskToken
        )

        if (lBracketToken) {
            (methodDef as any).lBracketToken = lBracketToken
        }
        if (rBracketToken) {
            (methodDef as any).rBracketToken = rBracketToken
        }
        return methodDef
    }
}

export const SlimeAccessorCstToAst = new SlimeAccessorCstToAstSingle()
