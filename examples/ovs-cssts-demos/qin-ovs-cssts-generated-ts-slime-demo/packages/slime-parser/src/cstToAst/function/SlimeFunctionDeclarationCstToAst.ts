/**
 * SlimeFunctionDeclarationCstToAst - 鍑芥暟澹版槑杞崲
 *
 * 閲嶆瀯锛氫娇鐢ㄧ粺涓€鐨勬牳蹇冩柟娉?createFunctionDeclarationCore 澶勭悊鎵€鏈夊彉浣?
 */
import { SubhutiCst } from "subhuti";
import {
    SlimeAstCreateUtils,
    type SlimeBlockStatement,
    type SlimeFunctionDeclaration,
    type SlimeFunctionParam,
    SlimeIdentifier,
    SlimeAstTypeName,
    SlimeTokenCreateUtils
} from "slime-ast";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

/**
 * 鍑芥暟澹版槑鏍稿績鍙傛暟
 */
interface FunctionDeclarationParams {
    id: SlimeIdentifier | null
    params: SlimeFunctionParam[]
    body: SlimeBlockStatement
    isGenerator: boolean
    isAsync: boolean
    returnType?: any
    typeParameters?: any  // [TypeScript] 娉涘瀷鍙傛暟
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

export class SlimeFunctionDeclarationCstToAstSingle {

    // ========================================
    // 鏍稿績瑙ｆ瀽鏂规硶
    // ========================================

    /**
     * 浠?CST 瑙ｆ瀽鍑芥暟鏍稿績淇℃伅
     * 鎵€鏈夊嚱鏁板０鏄庡彉浣撶殑閫氱敤瑙ｆ瀽閫昏緫
     */
    private parseFunctionFromCst(cst: SubhutiCst): FunctionDeclarationParams {
        const children = cst.getChildren() || []

        let id: SlimeIdentifier | null = null
        let params: SlimeFunctionParam[] = []
        let body: SlimeBlockStatement | null = null
        let bodyStatements: any[] | null = null
        let bodyLoc: any = undefined
        let functionBodyCst: SubhutiCst | null = null
        let isAsync = false
        let isGenerator = false
        let returnType: any = undefined
        let typeParameters: any = undefined  // [TypeScript] 娉涘瀷鍙傛暟
        let inParams = false

        // Token fields
        let functionToken: any = undefined
        let asyncToken: any = undefined
        let asteriskToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        for (let i = 0; i < children.length; i++) {
            const child = children[i]
            if (!child) continue

            const name = child.getName()
            const value = child.getValue() || child.getLoc()?.value

            // Tokens
            if (name === 'Function' || value === 'function') {
                functionToken = SlimeTokenCreateUtils.createFunctionToken(child.getLoc())
                continue
            }
            if (name === 'LParen' || value === '(') {
                lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
                inParams = true
                continue
            }
            if (name === 'RParen' || value === ')') {
                rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
                inParams = false
                continue
            }
            if (name === 'LBrace' || value === '{') {
                lBraceToken = SlimeTokenCreateUtils.createLBraceToken(child.getLoc())
                continue
            }
            if (name === 'RBrace' || value === '}') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(child.getLoc())
                continue
            }
            if (name === 'Async' || value === 'async') {
                asyncToken = SlimeTokenCreateUtils.createAsyncToken(child.getLoc())
                isAsync = true
                continue
            }
            if (name === 'Asterisk' || value === '*') {
                asteriskToken = SlimeTokenCreateUtils.createAsteriskToken(child.getLoc())
                isGenerator = true
                continue
            }
            if (inParams && (name === 'Comma' || value === ',')) {
                if (params.length > 0) {
                    const lastParam = params[params.length - 1] as any
                    if (!lastParam.commaToken) {
                        lastParam.commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                    }
                }
                continue
            }

            // BindingIdentifier - function name
            if (name === SlimeParser.prototype.BindingIdentifier?.name || name === 'BindingIdentifier') {
                id = SlimeCstToAstUtils.createBindingIdentifierAst(child)
                continue
            }

            // FormalParameters - function parameters
            if (name === SlimeParser.prototype.FormalParameters?.name || name === 'FormalParameters') {
                params = SlimeCstToAstUtils.createFormalParametersAstWrapped(child)
                continue
            }

            // FormalParameterList (鏃х増鍏煎)
            if (name === SlimeParser.prototype.FormalParameterList?.name || name === 'FormalParameterList') {
                params = SlimeCstToAstUtils.createFormalParameterListFromEs2025Wrapped(child)
                continue
            }

            // [TypeScript] TSTypeAnnotation - return type
            if (name === 'TSTypeAnnotation') {
                returnType = SlimeCstToAstUtils.createTSTypeAnnotationAst(child)
                continue
            }

            // [TypeScript] TSTypeParameterDeclaration - 娉涘瀷鍙傛暟 <T extends ...>
            if (name === 'TSTypeParameterDeclaration') {
                typeParameters = SlimeCstToAstUtils.createTSTypeParameterDeclarationAst(child)
                continue
            }

            // Function body (鍚勭鍙樹綋)
            if (name === SlimeParser.prototype.FunctionBody?.name || name === 'FunctionBody' ||
                name === 'GeneratorBody' || name === SlimeParser.prototype.GeneratorBody?.name ||
                name === 'AsyncFunctionBody' || name === SlimeParser.prototype.AsyncFunctionBody?.name ||
                name === 'AsyncGeneratorBody' || name === SlimeParser.prototype.AsyncGeneratorBody?.name) {
                bodyStatements = SlimeCstToAstUtils.createFunctionBodyAst(child)
                bodyLoc = child.getLoc()
                functionBodyCst = child

                if (child.getChildren()) {
                    for (const bodyChild of child.getChildren()) {
                        if (!lBraceToken && (bodyChild.name === 'LBrace' || bodyChild.value === '{')) {
                            lBraceToken = SlimeTokenCreateUtils.createLBraceToken(bodyChild.loc)
                        } else if (!rBraceToken && (bodyChild.name === 'RBrace' || bodyChild.value === '}')) {
                            rBraceToken = SlimeTokenCreateUtils.createRBraceToken(bodyChild.loc)
                        }
                    }
                }
                continue
            }
        }

        if (functionBodyCst && (!lBraceToken || !rBraceToken)) {
            const stack = [functionBodyCst]
            let lBraceCst: any = undefined
            let rBraceCst: any = undefined

            while (stack.length > 0) {
                const current = stack.pop()
                if (!current) continue

                if (!lBraceCst && (current.name === 'LBrace' || current.value === '{')) {
                    lBraceCst = current
                }
                if (current.name === 'RBrace' || current.value === '}') {
                    rBraceCst = current
                }

                const children = current.children || []
                for (let i = children.length - 1; i >= 0; i--) {
                    stack.push(children[i])
                }
            }

            if (!lBraceToken && lBraceCst) {
                lBraceToken = SlimeTokenCreateUtils.createLBraceToken(lBraceCst.getLoc())
            }
            if (!rBraceToken && rBraceCst) {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(rBraceCst.getLoc())
            }
        }

        if (bodyStatements) {
            body = SlimeAstCreateUtils.createBlockStatement(bodyStatements, bodyLoc, lBraceToken, rBraceToken)
        } else {
            body = SlimeAstCreateUtils.createBlockStatement([], undefined, lBraceToken, rBraceToken)
        }

        return {
            id,
            params,
            body,
            isGenerator,
            isAsync,
            returnType,
            typeParameters,
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
     * 浠庤В鏋愮粨鏋滃垱寤?FunctionDeclaration AST
     */
    private buildFunctionDeclaration(parsed: FunctionDeclarationParams): SlimeFunctionDeclaration {
        const result = SlimeAstCreateUtils.createFunctionDeclaration(
            parsed.id, parsed.params, parsed.body,
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

        // [TypeScript] 娣诲姞娉涘瀷鍙傛暟
        if (parsed.typeParameters) {
            result.typeParameters = parsed.typeParameters
        }

        return result
    }

    // ========================================
    // 鍏紑鏂规硶锛堜娇鐢ㄦ牳蹇冩柟娉曪級
    // ========================================

    /**
     * 鍒涘缓鍑芥暟澹版槑 AST
     * function name(params) { body }
     */
    createFunctionDeclarationAst(cst: SubhutiCst): SlimeFunctionDeclaration {
        const parsed = this.parseFunctionFromCst(cst)
        return this.buildFunctionDeclaration(parsed)
    }

    /**
     * 鍒涘缓 Generator 澹版槑 AST
     * function* name(params) { body }
     */
    createGeneratorDeclarationAst(cst: SubhutiCst): SlimeFunctionDeclaration {
        const parsed = this.parseFunctionFromCst(cst)
        // 纭繚 generator 鏍囧織涓?true
        parsed.isGenerator = true
        return this.buildFunctionDeclaration(parsed)
    }

    /**
     * 鍒涘缓 Async 鍑芥暟澹版槑 AST
     * async function name(params) { body }
     */
    createAsyncFunctionDeclarationAst(cst: SubhutiCst): SlimeFunctionDeclaration {
        const parsed = this.parseFunctionFromCst(cst)
        // 纭繚 async 鏍囧織涓?true
        parsed.isAsync = true
        return this.buildFunctionDeclaration(parsed)
    }

    /**
     * 鍒涘缓 Async Generator 澹版槑 AST
     * async function* name(params) { body }
     */
    createAsyncGeneratorDeclarationAst(cst: SubhutiCst): SlimeFunctionDeclaration {
        const parsed = this.parseFunctionFromCst(cst)
        // 纭繚涓や釜鏍囧織閮戒负 true
        parsed.isAsync = true
        parsed.isGenerator = true
        return this.buildFunctionDeclaration(parsed)
    }
}

export const SlimeFunctionDeclarationCstToAst = new SlimeFunctionDeclarationCstToAstSingle()
