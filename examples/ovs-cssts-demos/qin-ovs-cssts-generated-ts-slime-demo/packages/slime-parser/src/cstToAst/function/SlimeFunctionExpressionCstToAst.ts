/**
 * SlimeFunctionExpressionCstToAst - 鍑芥暟琛ㄨ揪寮忚浆鎹?
 *
 * 閲嶆瀯锛氫娇鐢ㄧ粺涓€鐨勬牳蹇冩柟娉?parseFunctionFromCst 澶勭悊鎵€鏈夊彉浣?
 */
import { SubhutiCst } from "subhuti";
import {
    SlimeBlockStatement,
    SlimeFunctionExpression,
    SlimeFunctionParam,
    SlimeIdentifier,
    SlimeTokenCreateUtils,
    SlimeAstCreateUtils
} from "slime-ast";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

/**
 * 鍑芥暟琛ㄨ揪寮忔牳蹇冨弬鏁?
 */
interface FunctionExpressionParams {
    id: SlimeIdentifier | null
    params: SlimeFunctionParam[]
    body: SlimeBlockStatement
    isGenerator: boolean
    isAsync: boolean
    returnType?: any
    loc: any
    tokens?: {
        functionToken?: any
        asyncToken?: any
        asteriskToken?: any
        lParenToken?: any
        rParenToken?: any
        lBraceToken?: any
        rBraceToken?: any
    }
}

export class SlimeFunctionExpressionCstToAstSingle {

    // ========================================
    // 鏍稿績瑙ｆ瀽鏂规硶
    // ========================================

    /**
     * 浠?CST 瑙ｆ瀽鍑芥暟鏍稿績淇℃伅
     * 鐩存帴浠?CST children 涓寜鍚嶇О鏌ユ壘鍚勪釜鑺傜偣
     */
    private parseFunctionFromCst(cst: SubhutiCst): FunctionExpressionParams {
        const children = cst.getChildren() || []

        // 杈呭姪鍑芥暟锛氭寜鍚嶇О鎴栧€兼煡鎵?CST 鑺傜偣
        const findByName = (...names: (string | undefined)[]) => {
            const validNames = names.filter((n): n is string => !!n)
            return children.find(c => c && (validNames.includes(c.name) || validNames.includes(c.value)))
        }

        const findByValue = (value: string) =>
            children.find(c => c && (c.value === value || c.loc?.value === value))

        // 鐩存帴鏌ユ壘鍚勪釜 token 鑺傜偣
        const functionCst = findByName('Function') || findByValue('function')
        const asyncCst = findByName('Async') || findByValue('async')
        const asteriskCst = findByName('Asterisk') || findByValue('*')
        const lParenCst = findByName('LParen') || findByValue('(')
        const rParenCst = findByName('RParen') || findByValue(')')
        const lBraceCst = findByName('LBrace') || findByValue('{')
        const rBraceCst = findByName('RBrace') || findByValue('}')

        // 鐩存帴鏌ユ壘鍚勪釜澶嶅悎鑺傜偣
        const bindingIdCst = findByName(SlimeParser.prototype.BindingIdentifier?.name, 'BindingIdentifier')
        const formalParamsCst = findByName(SlimeParser.prototype.FormalParameters?.name, 'FormalParameters')
        const formalParamListCst = findByName(SlimeParser.prototype.FormalParameterList?.name, 'FormalParameterList')
        const typeAnnotationCst = findByName('TSTypeAnnotation')
        const functionBodyCst = findByName(
            SlimeParser.prototype.FunctionBody?.name, 'FunctionBody',
            'GeneratorBody', SlimeParser.prototype.GeneratorBody?.name,
            'AsyncFunctionBody', SlimeParser.prototype.AsyncFunctionBody?.name,
            'AsyncGeneratorBody', SlimeParser.prototype.AsyncGeneratorBody?.name
        )

        // 鍒涘缓 tokens
        const functionToken = functionCst ? SlimeTokenCreateUtils.createFunctionToken(functionCst.loc) : undefined
        const asyncToken = asyncCst ? SlimeTokenCreateUtils.createAsyncToken(asyncCst.loc) : undefined
        const asteriskToken = asteriskCst ? SlimeTokenCreateUtils.createAsteriskToken(asteriskCst.loc) : undefined
        const lParenToken = lParenCst ? SlimeTokenCreateUtils.createLParenToken(lParenCst.loc) : undefined
        const rParenToken = rParenCst ? SlimeTokenCreateUtils.createRParenToken(rParenCst.loc) : undefined
        const lBraceToken = lBraceCst ? SlimeTokenCreateUtils.createLBraceToken(lBraceCst.getLoc()) : undefined
        const rBraceToken = rBraceCst ? SlimeTokenCreateUtils.createRBraceToken(rBraceCst.getLoc()) : undefined

        // 瑙ｆ瀽澶嶅悎鑺傜偣
        const id = bindingIdCst ? SlimeCstToAstUtils.createBindingIdentifierAst(bindingIdCst) : null
        const params = formalParamsCst
            ? SlimeCstToAstUtils.createFormalParametersAstWrapped(formalParamsCst)
            : formalParamListCst
                ? SlimeCstToAstUtils.createFormalParameterListFromEs2025Wrapped(formalParamListCst)
                : []

        if (params.length > 0) {
            const lParenIndex = children.findIndex(c => c && (c.name === 'LParen' || c.value === '('))
            const rParenIndex = children.findIndex(c => c && (c.name === 'RParen' || c.value === ')'))
            if (lParenIndex >= 0 && rParenIndex > lParenIndex) {
                for (let i = lParenIndex + 1; i < rParenIndex; i++) {
                    const child = children[i]
                    if (child && (child.getName() === 'Comma' || child.getValue() === ',')) {
                        const lastParam = params[params.length - 1] as any
                        if (!lastParam.commaToken) {
                            lastParam.commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                        }
                    }
                }
            }
        }
        const returnType = typeAnnotationCst ? SlimeCstToAstUtils.createTSTypeAnnotationAst(typeAnnotationCst) : undefined

        // 鍒涘缓鍑芥暟浣擄紙BlockStatement锛?
        let body: SlimeBlockStatement
        if (functionBodyCst) {
            const statements = SlimeCstToAstUtils.createFunctionBodyAst(functionBodyCst)
            body = SlimeAstCreateUtils.createBlockStatement(statements, functionBodyCst.loc, lBraceToken, rBraceToken)
        } else {
            body = SlimeAstCreateUtils.createBlockStatement([], undefined, lBraceToken, rBraceToken)
        }

        return {
            id,
            params,
            body,
            isGenerator: !!asteriskCst,
            isAsync: !!asyncCst,
            returnType,
            loc: cst.getLoc(),
            tokens: {
                functionToken,
                asyncToken,
                asteriskToken,
                lParenToken,
                rParenToken,
                lBraceToken,
                rBraceToken
            }
        }
    }

    /**
     * 浠庤В鏋愮粨鏋滃垱寤?FunctionExpression AST
     */
    private buildFunctionExpression(parsed: FunctionExpressionParams): SlimeFunctionExpression {
        const result = SlimeAstCreateUtils.createFunctionExpression(
            parsed.body, parsed.id, parsed.params,
            parsed.isGenerator, parsed.isAsync, parsed.loc,
            parsed.tokens?.functionToken, parsed.tokens?.asyncToken,
            parsed.tokens?.asteriskToken, parsed.tokens?.lParenToken,
            parsed.tokens?.rParenToken, parsed.tokens?.lBraceToken,
            parsed.tokens?.rBraceToken
        ) as any

        // [TypeScript] 娣诲姞杩斿洖绫诲瀷
        if (parsed.returnType) {
            result.returnType = parsed.returnType
        }

        return result
    }

    // ========================================
    // 鍏紑鏂规硶锛堜娇鐢ㄦ牳蹇冩柟娉曪級
    // ========================================

    /**
     * 鍒涘缓鍑芥暟琛ㄨ揪寮?AST
     * function [name](params) { body }
     */
    createFunctionExpressionAst(cst: SubhutiCst): SlimeFunctionExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.FunctionExpression?.name)
        const parsed = this.parseFunctionFromCst(cst)
        return this.buildFunctionExpression(parsed)
    }

    /**
     * 鍒涘缓 Generator 琛ㄨ揪寮?AST
     * function* [name](params) { body }
     */
    createGeneratorExpressionAst(cst: SubhutiCst): SlimeFunctionExpression {
        const parsed = this.parseFunctionFromCst(cst)
        parsed.isGenerator = true
        return this.buildFunctionExpression(parsed)
    }

    /**
     * 鍒涘缓 Async 鍑芥暟琛ㄨ揪寮?AST
     * async function [name](params) { body }
     */
    createAsyncFunctionExpressionAst(cst: SubhutiCst): SlimeFunctionExpression {
        const parsed = this.parseFunctionFromCst(cst)
        parsed.isAsync = true
        return this.buildFunctionExpression(parsed)
    }

    /**
     * 鍒涘缓 Async Generator 琛ㄨ揪寮?AST
     * async function* [name](params) { body }
     */
    createAsyncGeneratorExpressionAst(cst: SubhutiCst): SlimeFunctionExpression {
        const parsed = this.parseFunctionFromCst(cst)
        parsed.isAsync = true
        parsed.isGenerator = true
        return this.buildFunctionExpression(parsed)
    }
}

export const SlimeFunctionExpressionCstToAst = new SlimeFunctionExpressionCstToAstSingle()
