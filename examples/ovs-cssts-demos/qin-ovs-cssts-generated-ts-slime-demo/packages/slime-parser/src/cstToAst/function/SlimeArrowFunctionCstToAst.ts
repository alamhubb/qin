/**
 * ArrowFunctionCstToAst - 绠ご鍑芥暟杞崲
 */
import {SubhutiCst} from "subhuti";
import {
    SlimeAstCreateUtils,
    SlimeBlockStatement,
    SlimeExpression,
    type SlimeFunctionParam,
    SlimeMethodDefinition,
    SlimePattern,
    SlimeTokenCreateUtils,
    SlimeAstTypeName, SlimeArrowFunctionExpression, SlimeIdentifier
} from "slime-ast";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeTokenConsumer } from "../../SlimeTokenConsumer.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import {SlimeVariableCstToAstSingle} from "../statements/SlimeVariableCstToAst.ts";

export class SlimeArrowFunctionCstToAstSingle {

    private findAsyncTokenCst(node: SubhutiCst | undefined): SubhutiCst | undefined {
        if (!node) return undefined
        if (node.value === 'async') return node
        for (const ch of node.children || []) {
            const found = this.findAsyncTokenCst(ch)
            if (found) return found
        }
        return undefined
    }

    private recoverExpressionFromArrowParameters(node: SubhutiCst | undefined): SlimeExpression | null {
        if (!node) {
            return null
        }

        if (node.name === SlimeParser.prototype.ArrowParameters?.name) {
            return this.recoverExpressionFromArrowParameters(node.children?.[0])
        }

        if (node.name === SlimeParser.prototype.BindingIdentifier?.name || node.name === 'BindingIdentifier') {
            return SlimeCstToAstUtils.createBindingIdentifierAst(node)
        }

        if (node.name === SlimeParser.prototype.CoverParenthesizedExpressionAndArrowParameterList?.name) {
            const expressionCst = (node.children || []).find(child =>
                child.getName() === SlimeParser.prototype.Expression?.name
                || child.getName() === SlimeParser.prototype.AssignmentExpression?.name
            )
            if (expressionCst) {
                const expression = SlimeCstToAstUtils.createExpressionAst(expressionCst)
                const parenthesized = SlimeAstCreateUtils.createParenthesizedExpression(expression, node.loc) as any
                const lParenCst = (node.children || []).find(child => child.getName() === 'LParen' || child.getValue() === '(')
                const rParenCst = (node.children || []).find(child => child.getName() === 'RParen' || child.getValue() === ')')
                parenthesized.lParenToken = lParenCst ? SlimeTokenCreateUtils.createLParenToken(lParenCst.loc) : undefined
                parenthesized.rParenToken = rParenCst ? SlimeTokenCreateUtils.createRParenToken(rParenCst.loc) : undefined
                return parenthesized
            }
        }

        return null
    }


    /**
     * 鍒涘缓绠ご鍑芥暟 AST
     */
    createArrowFunctionAst(cst: SubhutiCst): SlimeArrowFunctionExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ArrowFunction?.name);
        // ArrowFunction 缁撴瀯锛堝甫async锛夛細
        // children[0]: AsyncTok (鍙拷?
        // children[1]: BindingIdentifier 锟?CoverParenthesizedExpressionAndArrowParameterList (鍙傛暟)
        // children[2]: Arrow (=>)
        // children[3]: ConciseBody (鍑芥暟锟?

        // Token fields
        let asyncToken: any = undefined
        let arrowToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        const commaTokens: any[] = []

        // 妫€鏌ユ槸鍚︽湁async
        let offset = 0;
        let isAsync = false;
        const firstChild = cst.getChildren()[0]
        if (firstChild && (firstChild.getName() === 'Async' || firstChild.getValue() === 'async')) {
            asyncToken = SlimeTokenCreateUtils.createAsyncToken(firstChild.getLoc())
            isAsync = true;
            offset = 1;
        }

        // 闃插尽鎬ф鏌ワ細纭繚children瀛樺湪涓旀湁瓒冲鍏冪礌
        if (!cst.getChildren() || cst.getChildren().length < 3 + offset) {
            const recovered = this.recoverExpressionFromArrowParameters(cst.getChildren()?.[0 + offset] || cst.getChildren()?.[0])
            if (recovered) {
                return recovered as any
            }
            return SlimeAstCreateUtils.createIdentifier('', cst.getLoc()) as any
        }

        const arrowParametersCst = cst.getChildren()[0 + offset]
        const arrowCst = cst.getChildren()[1 + offset]
        const conciseBodyCst = cst.getChildren()[2 + offset]

        // 鎻愬彇绠ご token
        if (arrowCst && (arrowCst.name === 'Arrow' || arrowCst.value === '=>')) {
            arrowToken = SlimeTokenCreateUtils.createArrowToken(arrowCst.loc)
        }

        // 瑙ｆ瀽鍙傛暟 - 鏍规嵁鑺傜偣绫诲瀷鍒嗗埆澶勭悊
        // SlimeFunctionParam 鏄寘瑁呯被鍨嬶紝鍖呭惈 param 鍜屽彲閫夌殑 commaToken
        let params: SlimeFunctionParam[];
        if (arrowParametersCst.name === SlimeParser.prototype.BindingIdentifier?.name) {
            // 鍗曚釜鍙傛暟锛歺 => x * 2
            params = [{param: SlimeCstToAstUtils.createBindingIdentifierAst(arrowParametersCst)}]
        } else if (arrowParametersCst.name === SlimeParser.prototype.CoverParenthesizedExpressionAndArrowParameterList?.name) {
            // 鎷彿鍙傛暟锟?a, b) => a + b 锟?() => 42
            // 鎻愬彇鎷彿 tokens
            for (const child of arrowParametersCst.children || []) {
                if (child.getName() === 'LParen' || child.getValue() === '(') {
                    lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
                } else if (child.getName() === 'RParen' || child.getValue() === ')') {
                    rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
                } else if (child.getName() === 'Comma' || child.getValue() === ',') {
                    commaTokens.push(SlimeTokenCreateUtils.createCommaToken(child.getLoc()))
                } else if (child.getName() === SlimeParser.prototype.Expression?.name) {
                    for (const exprChild of child.getChildren() || []) {
                        if (exprChild.name === 'Comma' || exprChild.value === ',') {
                            commaTokens.push(SlimeTokenCreateUtils.createCommaToken(exprChild.loc))
                        }
                    }
                }
            }
            // 锟?SlimePattern[] 杞崲锟?SlimeFunctionParam[]
            const rawParams = SlimeCstToAstUtils.createArrowParametersFromCoverGrammar(arrowParametersCst)
            params = rawParams.map((p, i) => ({
                param: p,
                commaToken: commaTokens[i] // 涓烘瘡涓弬鏁板叧鑱旈€楀彿 token锛堟渶鍚庝竴涓弬鏁版棤閫楀彿锟?
            }))
        } else if (arrowParametersCst.name === SlimeParser.prototype.ArrowParameters?.name) {
            // ArrowParameters 瑙勫垯锛氬叾瀛愯妭鐐瑰彲鑳芥槸 CoverParenthesizedExpressionAndArrowParameterList 锟?BindingIdentifier
            const firstChild = arrowParametersCst.children?.[0]
            if (firstChild?.name === SlimeParser.prototype.CoverParenthesizedExpressionAndArrowParameterList?.name) {
                // 锟?CoverParenthesizedExpressionAndArrowParameterList 鎻愬彇鎷彿 tokens
                for (const child of firstChild.getChildren() || []) {
                    if (child.getName() === 'LParen' || child.getValue() === '(') {
                        lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
                    } else if (child.getName() === 'RParen' || child.getValue() === ')') {
                        rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
                    } else if (child.getName() === 'Comma' || child.getValue() === ',') {
                        commaTokens.push(SlimeTokenCreateUtils.createCommaToken(child.getLoc()))
                    } else if (child.getName() === SlimeParser.prototype.Expression?.name) {
                        for (const exprChild of child.getChildren() || []) {
                            if (exprChild.name === 'Comma' || exprChild.value === ',') {
                                commaTokens.push(SlimeTokenCreateUtils.createCommaToken(exprChild.loc))
                            }
                        }
                    }
                }
            }
            const rawParams = SlimeCstToAstUtils.createArrowParametersAst(arrowParametersCst)
            params = rawParams.map((p, i) => ({
                param: p,
                commaToken: commaTokens[i] // 涓烘瘡涓弬鏁板叧鑱旈€楀彿 token锛堟渶鍚庝竴涓弬鏁版棤閫楀彿锟?
            }))
        } else {
            const recovered = this.recoverExpressionFromArrowParameters(arrowParametersCst)
            if (recovered) {
                return recovered as any
            }
            return SlimeAstCreateUtils.createIdentifier('', cst.getLoc()) as any
        }

        // 瑙ｆ瀽鍑芥暟锟?
        const body = SlimeCstToAstUtils.createConciseBodyAst(conciseBodyCst)

        // 娉ㄦ剰锛歝reateArrowFunctionExpression 鍙傛暟椤哄簭锟?(body, params, expression, async, loc, arrowToken, asyncToken, lParenToken, rParenToken)
        // commaTokens 鐩墠鍑芥暟绛惧悕涓嶆敮鎸侊紝鏆傛椂蹇界暐
        return SlimeAstCreateUtils.createArrowFunctionExpression(
            body, params, body.type !== SlimeAstTypeName.BlockStatement, isAsync, cst.getLoc(),
            arrowToken, asyncToken, lParenToken, rParenToken
        )
    }

    /**
     * 鍒涘缓 Async 绠ご鍑芥暟 AST
     * AsyncArrowFunction: async AsyncArrowBindingIdentifier => AsyncConciseBody
     *                   | CoverCallExpressionAndAsyncArrowHead => AsyncConciseBody
     */
    createAsyncArrowFunctionAst(cst: SubhutiCst): SlimeArrowFunctionExpression {
        // AsyncArrowFunction 缁撴瀯锟?
        // 褰㈠紡1: [AsyncTok, BindingIdentifier, Arrow, AsyncConciseBody]
        // 褰㈠紡2: [CoverCallExpressionAndAsyncArrowHead, Arrow, AsyncConciseBody]

        let params: SlimePattern[] = []
        let body: SlimeExpression | SlimeBlockStatement
        let arrowIndex = -1
        let arrowToken: any = undefined
        let asyncToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined

        // 鎵惧埌 Arrow token 鐨勪綅锟?
        for (let i = 0; i < cst.getChildren().length; i++) {
            if (cst.getChildren()[i].name === 'Arrow' || cst.getChildren()[i].value === '=>') {
                arrowToken = SlimeTokenCreateUtils.createArrowToken(cst.getChildren()[i].loc)
                arrowIndex = i
                break
            }
        }

        // 瀹归敊妯″紡锛氬鏋滄壘涓嶅埌 Arrow token锛屽皾璇曚粠涓嶅畬鏁寸殑 CST 涓彁鍙栦俊锟?
        if (arrowIndex === -1) {
            // 灏濊瘯锟?CoverCallExpressionAndAsyncArrowHead 鎻愬彇鍙傛暟
            for (const child of cst.getChildren()) {
                if (child.getName() === 'CoverCallExpressionAndAsyncArrowHead') {
                    params = SlimeCstToAstUtils.createAsyncArrowParamsFromCover(child)
                    break
                } else if (child.getName() === 'Async') {
                    continue
                } else if (child.getName() === 'BindingIdentifier' || child.getName() === SlimeParser.prototype.BindingIdentifier?.name) {
                    params = [SlimeCstToAstUtils.createBindingIdentifierAst(child)]
                    break
                }
            }
            // 杩斿洖涓嶅畬鏁寸殑绠ご鍑芥暟锛堟病锟?body锟?
            return {
                type: SlimeAstTypeName.ArrowFunctionExpression,
                id: null,
                params: params,
                body: SlimeAstCreateUtils.createBlockStatement([]),
                generator: false,
                async: true,
                expression: false,
                loc: cst.getLoc()
            } as any
        }

        // 瑙ｆ瀽鍙傛暟锛圓rrow 涔嬪墠鐨勯儴鍒嗭級
        for (let i = 0; i < arrowIndex; i++) {
            const child = cst.getChildren()[i]
            if (child.getName() === 'Async' || child.getValue() === 'async') {
                asyncToken = SlimeTokenCreateUtils.createAsyncToken(child.getLoc())
                continue // 璺宠繃 async 鍏抽敭锟?
            }
            if (child.getName() === SlimeParser.prototype.BindingIdentifier?.name || child.getName() === 'BindingIdentifier') {
                params = [SlimeCstToAstUtils.createBindingIdentifierAst(child)]
            } else if (child.getName() === 'AsyncArrowBindingIdentifier' || child.getName() === SlimeParser.prototype.AsyncArrowBindingIdentifier?.name) {
                // AsyncArrowBindingIdentifier 鍖呭惈涓€锟?BindingIdentifier
                const bindingId = child.getChildren()?.find((c: any) =>
                    c.name === 'BindingIdentifier' || c.name === SlimeParser.prototype.BindingIdentifier?.name
                ) || child.getChildren()?.[0]
                if (bindingId) {
                    params = [SlimeCstToAstUtils.createBindingIdentifierAst(bindingId)]
                }
            } else if (child.getName() === 'CoverCallExpressionAndAsyncArrowHead') {
                if (!asyncToken) {
                    const asyncCst = this.findAsyncTokenCst(child)
                    if (asyncCst) {
                        asyncToken = SlimeTokenCreateUtils.createAsyncToken(asyncCst.loc)
                    }
                }
                // 锟?CoverCallExpressionAndAsyncArrowHead 鎻愬彇鍙傛暟鍜屾嫭锟?tokens
                params = SlimeCstToAstUtils.createAsyncArrowParamsFromCover(child)
                // 鎻愬彇鎷彿 tokens
                for (const subChild of child.getChildren() || []) {
                    if (subChild.name === 'Arguments' || subChild.name === SlimeParser.prototype.Arguments?.name) {
                        for (const argChild of subChild.children || []) {
                            if (argChild.name === 'LParen' || argChild.value === '(') {
                                lParenToken = SlimeTokenCreateUtils.createLParenToken(argChild.loc)
                            } else if (argChild.name === 'RParen' || argChild.value === ')') {
                                rParenToken = SlimeTokenCreateUtils.createRParenToken(argChild.loc)
                            }
                        }
                    }
                }
            } else if (child.getName() === SlimeParser.prototype.ArrowFormalParameters?.name || child.getName() === 'ArrowFormalParameters') {
                params = SlimeCstToAstUtils.createArrowFormalParametersAst(child)
                // 鎻愬彇鎷彿 tokens
                for (const subChild of child.getChildren() || []) {
                    if (subChild.name === 'LParen' || subChild.value === '(') {
                        lParenToken = SlimeTokenCreateUtils.createLParenToken(subChild.loc)
                    } else if (subChild.name === 'RParen' || subChild.value === ')') {
                        rParenToken = SlimeTokenCreateUtils.createRParenToken(subChild.loc)
                    }
                }
            }
        }

        // 瑙ｆ瀽鍑芥暟浣擄紙Arrow 涔嬪悗鐨勯儴鍒嗭級
        const bodyIndex = arrowIndex + 1
        if (bodyIndex < cst.getChildren().length) {
            const bodyCst = cst.getChildren()[bodyIndex]
            if (bodyCst.name === 'AsyncConciseBody' || bodyCst.name === 'ConciseBody') {
                body = SlimeCstToAstUtils.createConciseBodyAst(bodyCst)
            } else {
                body = SlimeCstToAstUtils.createExpressionAst(bodyCst)
            }
        } else {
            body = SlimeAstCreateUtils.createBlockStatement([])
        }

        return {
            type: SlimeAstTypeName.ArrowFunctionExpression,
            id: null,
            params: params,
            body: body,
            generator: false,
            async: true,
            expression: body.type !== SlimeAstTypeName.BlockStatement,
            arrowToken: arrowToken,
            asyncToken: asyncToken,
            lParenToken: lParenToken,
            rParenToken: rParenToken,
            loc: cst.getLoc()
        } as any
    }

    /**
     * AsyncArrowHead CST 锟?AST锛堥€忎紶锟?
     */
    createAsyncArrowHeadAst(cst: SubhutiCst): any {
        // AsyncArrowHead 涓昏鐢ㄤ簬瑙ｆ瀽锛屽疄锟?AST 澶勭悊锟?AsyncArrowFunction 锟?
        return cst.getChildren()?.[0] ? SlimeCstToAstUtils.createAstFromCst(cst.getChildren()[0]) : null
    }

    /**
     * 浠嶤overParenthesizedExpressionAndArrowParameterList鎻愬彇绠ご鍑芥暟鍙傛暟
     */
    createArrowParametersFromCoverGrammar(cst: SubhutiCst): SlimePattern[] {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.CoverParenthesizedExpressionAndArrowParameterList?.name);

        // CoverParenthesizedExpressionAndArrowParameterList 鐨刢hildren缁撴瀯锟?
        // LParen + (FormalParameterList | Expression | ...) + RParen
        // 鎴栵拷?LParen + Expression + Comma + Ellipsis + BindingIdentifier + RParen

        if (cst.getChildren().length === 0) {
            return []
        }

        // () - 绌哄弬锟?
        if (cst.getChildren().length === 2) {
            return []
        }

        const params: SlimePattern[] = []

        // 鏌ユ壘FormalParameterList
        const formalParameterListCst = cst.getChildren().find(
            child => child.getName() === SlimeParser.prototype.FormalParameterList?.name
        )
        if (formalParameterListCst) {
            return SlimeCstToAstUtils.createFormalParameterListAst(formalParameterListCst)
        }

        // 鏌ユ壘Expression锛堝彲鑳芥槸閫楀彿琛ㄨ揪寮忥紝锟?(a, b) 鎴栧崟涓弬锟?(x)锟?
        const expressionCst = cst.getChildren().find(
            child => child.getName() === SlimeParser.prototype.Expression?.name
        )
        if (expressionCst && expressionCst.getChildren()?.length) {
            // 鐩存帴锟?Expression 锟?children 涓婇亶锟?AssignmentExpression 绛夊€欓€夊弬鏁拌妭锟?
            for (const child of expressionCst.getChildren()) {
                if (child.getName() === 'Comma' || child.getValue() === ',') continue
                const param = SlimeCstToAstUtils.convertCoverParameterCstToPattern(child, false)
                if (param) {
                    params.push(param)
                }
            }
        }

        // 妫€鏌ユ槸鍚︽湁 rest 鍙傛暟锛圗llipsis + BindingIdentifier 锟?BindingPattern锟?
        const hasEllipsis = cst.getChildren().some(
            child => child.getName() === 'Ellipsis' || child.getName() === 'Ellipsis'
        )
        if (hasEllipsis) {
            const ellipsisCst = cst.getChildren().find(
                child => child.getName() === 'Ellipsis' || child.getValue() === '...'
            )
            // 棣栧厛鏌ユ壘 BindingIdentifier / BindingPattern 浣滀负 rest 鐨勭洰锟?
            const bindingIdentifierCst = cst.getChildren().find(
                child => child.getName() === SlimeParser.prototype.BindingIdentifier?.name || child.getName() === 'BindingIdentifier'
            )
            const bindingPatternCst = bindingIdentifierCst
                ? null
                : cst.getChildren().find(
                    child => child.getName() === SlimeParser.prototype.BindingPattern?.name ||
                        child.getName() === 'BindingPattern' ||
                        child.getName() === SlimeParser.prototype.ArrayBindingPattern?.name ||
                        child.getName() === 'ArrayBindingPattern' ||
                        child.getName() === SlimeParser.prototype.ObjectBindingPattern?.name ||
                        child.getName() === 'ObjectBindingPattern'
                )

            const restTarget = bindingIdentifierCst || bindingPatternCst
            if (restTarget) {
                const restParam = SlimeCstToAstUtils.convertCoverParameterCstToPattern(restTarget, true)
                if (restParam) {
                    if (ellipsisCst && (restParam as any).type === SlimeAstTypeName.RestElement) {
                        (restParam as any).ellipsisToken = SlimeTokenCreateUtils.createEllipsisToken(ellipsisCst.loc)
                    }
                    params.push(restParam)
                }
            }
        } else if (params.length === 0) {
            // 娌℃湁 Expression 涔熸病锟?rest锛屾鏌ユ槸鍚︽湁鍗曠嫭锟?BindingIdentifier
            const bindingIdentifierCst = cst.getChildren().find(
                child => child.getName() === SlimeParser.prototype.BindingIdentifier?.name || child.getName() === 'BindingIdentifier'
            )
            if (bindingIdentifierCst) {
                params.push(SlimeCstToAstUtils.createBindingIdentifierAst(bindingIdentifierCst))
            }
        }

        return params
    }

    /**
     * 锟?ArrowFormalParameters 鎻愬彇鍙傛暟
     */
    createArrowFormalParametersAst(cst: SubhutiCst): SlimePattern[] {
        // ArrowFormalParameters: ( UniqueFormalParameters )
        const params: SlimePattern[] = []

        for (const child of cst.getChildren() || []) {
            if (child.getName() === 'UniqueFormalParameters' || child.getName() === SlimeParser.prototype.UniqueFormalParameters?.name) {
                return SlimeCstToAstUtils.createUniqueFormalParametersAst(child)
            }
            if (child.getName() === 'FormalParameters' || child.getName() === SlimeParser.prototype.FormalParameters?.name) {
                return SlimeCstToAstUtils.createFormalParametersAst(child)
            }
        }

        return params
    }


    /**
     * 锟?ArrowFormalParameters 鎻愬彇鍙傛暟 (鍖呰绫诲瀷鐗堟湰)
     */
    createArrowFormalParametersAstWrapped(cst: SubhutiCst): SlimeFunctionParam[] {
        // ArrowFormalParameters: ( UniqueFormalParameters )
        for (const child of cst.getChildren() || []) {
            if (child.getName() === 'UniqueFormalParameters' || child.getName() === SlimeParser.prototype.UniqueFormalParameters?.name) {
                return SlimeCstToAstUtils.createUniqueFormalParametersAstWrapped(child)
            }
            if (child.getName() === 'FormalParameters' || child.getName() === SlimeParser.prototype.FormalParameters?.name) {
                return SlimeCstToAstUtils.createFormalParametersAstWrapped(child)
            }
        }

        return []
    }


    /**
     * 鍒涘缓绠ご鍑芥暟鍙傛暟 AST
     */
    createArrowParametersAst(cst: SubhutiCst): SlimePattern[] {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ArrowParameters?.name);

        // ArrowParameters 鍙互鏄绉嶅舰寮忥紝杩欓噷绠€鍖栧锟?
        if (cst.getChildren().length === 0) {
            return []
        }

        const first = cst.getChildren()[0]

        // 鍗曚釜鍙傛暟锛欱indingIdentifier
        if (first.getName() === SlimeParser.prototype.BindingIdentifier?.name) {
            const param = SlimeCstToAstUtils.createBindingIdentifierAst(first)
            return [param]
        }

        // CoverParenthesizedExpressionAndArrowParameterList: 鎷彿鍙傛暟
        if (first.getName() === SlimeParser.prototype.CoverParenthesizedExpressionAndArrowParameterList?.name) {
            return SlimeCstToAstUtils.createArrowParametersFromCoverGrammar(first)
        }

        // 鍙傛暟鍒楄〃锟? FormalParameterList )
        if (first.getName() === SlimeTokenConsumer.prototype.LParen?.name) {
            // 鏌ユ壘 FormalParameterList
            const formalParameterListCst = cst.getChildren().find(
                child => child.getName() === SlimeParser.prototype.FormalParameterList?.name
            )
            if (formalParameterListCst) {
                return SlimeCstToAstUtils.createFormalParameterListAst(formalParameterListCst)
            }
            return []
        }

        return []
    }


    /**
     * 锟?CoverCallExpressionAndAsyncArrowHead 鎻愬彇 async 绠ご鍑芥暟鍙傛暟
     */
    createAsyncArrowParamsFromCover(cst: SubhutiCst): SlimePattern[] {
        // CoverCallExpressionAndAsyncArrowHead 缁撴瀯锟?
        // [MemberExpression, Arguments] 鎴栫被浼肩粨锟?
        // 鎴戜滑闇€瑕佷粠 Arguments 涓彁鍙栧弬锟?

        const params: SlimePattern[] = []

        for (const child of cst.getChildren() || []) {
            if (child.getName() === 'Arguments' || child.getName() === SlimeParser.prototype.Arguments?.name) {
                // 锟?Arguments 涓彁鍙栧弬锟?
                for (const argChild of child.getChildren() || []) {
                    if (argChild.name === 'ArgumentList' || argChild.name === SlimeParser.prototype.ArgumentList?.name) {
                        let hasEllipsis = false // 鏍囪鏄惁閬囧埌锟?...
                        let ellipsisCst: SubhutiCst | null = null
                        for (const arg of argChild.children || []) {
                            if (arg.value === ',') {
                                if (params.length > 0 && !(params[params.length - 1] as any).commaToken) {
                                    (params[params.length - 1] as any).commaToken = SlimeTokenCreateUtils.createCommaToken(arg.loc)
                                }
                                continue // 璺宠繃閫楀彿
                            }
                            // 澶勭悊 rest 鍙傛暟锟?..ids
                            if (arg.name === 'Ellipsis' || arg.value === '...') {
                                hasEllipsis = true
                                ellipsisCst = arg
                                continue
                            }
                            const param = SlimeCstToAstUtils.convertCoverParameterCstToPattern(arg, hasEllipsis)
                            if (param) {
                                if (ellipsisCst && (param as any).type === SlimeAstTypeName.RestElement) {
                                    (param as any).ellipsisToken = SlimeTokenCreateUtils.createEllipsisToken(ellipsisCst.loc)
                                }
                                params.push(param)
                                hasEllipsis = false
                                ellipsisCst = null
                            }
                        }
                    }
                }
            }
        }

        return params
    }

    /**
     * AsyncArrowBindingIdentifier CST 锟?AST
     */
    createAsyncArrowBindingIdentifierAst(cst: SubhutiCst): SlimeIdentifier {
        const bindingId = cst.getChildren()?.find(ch =>
            ch.name === SlimeParser.prototype.BindingIdentifier?.name ||
            ch.name === 'BindingIdentifier'
        )
        if (bindingId) {
            return SlimeCstToAstUtils.createBindingIdentifierAst(bindingId)
        }
        // 鐩存帴鏄爣璇嗙
        const firstChild = cst.getChildren()?.[0]
        if (firstChild) {
            return SlimeCstToAstUtils.createBindingIdentifierAst(firstChild)
        }
        throw new Error('AsyncArrowBindingIdentifier has no identifier')
    }


}


export const SlimeArrowFunctionCstToAst = new SlimeArrowFunctionCstToAstSingle()
