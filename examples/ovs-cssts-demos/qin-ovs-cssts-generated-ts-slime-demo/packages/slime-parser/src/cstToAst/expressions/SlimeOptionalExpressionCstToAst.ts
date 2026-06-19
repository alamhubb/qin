import {
    SlimeAstCreateUtils,
    SlimeExpression,
    type SlimeFunctionExpression,
    type SlimeIdentifier,
    SlimeAstTypeName,
    SlimeTokenCreateUtils
} from "slime-ast";
import { SubhutiCst } from "subhuti";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import { SlimeVariableCstToAstSingle } from "../statements/SlimeVariableCstToAst.ts";

export class SlimeOptionalExpressionCstToAstSingle {

    /**
     * 鍒涘缓 OptionalChain AST
     * 澶勭悊 ?. 鍚庣殑鍚勭璁块棶褰㈠紡
     *
     * 娉ㄦ剰锛氬彧鏈夌揣璺熷湪 ?. 鍚庨潰鐨勬搷浣滄槸 optional: true
     * 閾惧紡鐨勫悗缁搷浣滐紙锟?foo?.().bar() 涓殑 .bar()锛夋槸 optional: false
     */
    createOptionalChainAst(object: SlimeExpression, chainCst: SubhutiCst): SlimeExpression {
        let result = object
        // 杩借釜鏄惁鍒氶亣锟??. token锛屼笅涓€涓搷浣滄槸 optional
        let nextIsOptional = false
        let nextOptionalChainingToken: any = undefined
        let pendingDotToken: any = undefined

        for (const child of chainCst.children) {
            const name = child.getName()

            if (name === 'OptionalChaining' || child.getValue() === '?.') {
                // 閬囧埌 ?. token锛屼笅涓€涓搷浣滄槸 optional
                nextIsOptional = true
                nextOptionalChainingToken = SlimeTokenCreateUtils.createOptionalChainingToken(child.getLoc())
                continue
            } else if (name === 'Arguments') {
                // ()璋冪敤 - 鍙兘鏄彲閫夎皟鐢ㄦ垨鏅€氳皟锟?
                const args = SlimeCstToAstUtils.createArgumentsAstUnified(child)
                let lParenToken: any = undefined
                let rParenToken: any = undefined
                for (const argChild of child.getChildren() || []) {
                    if (argChild.name === 'LParen' || argChild.value === '(') {
                        lParenToken = SlimeTokenCreateUtils.createLParenToken(argChild.loc)
                    } else if (argChild.name === 'RParen' || argChild.value === ')') {
                        rParenToken = SlimeTokenCreateUtils.createRParenToken(argChild.loc)
                    }
                }
                result = {
                    type: SlimeAstTypeName.OptionalCallExpression,
                    callee: result,
                    arguments: args,
                    optional: nextIsOptional,
                    optionalChainingToken: nextIsOptional ? nextOptionalChainingToken : undefined,
                    lParenToken: lParenToken,
                    rParenToken: rParenToken,
                    loc: chainCst.loc
                } as any
                nextIsOptional = false
                nextOptionalChainingToken = undefined
                pendingDotToken = undefined
            } else if (name === 'LBracket' || child.getValue() === '[') {
                // [expr] 璁＄畻灞炴€ц锟?- 鍙兘鏄彲閫夋垨鏅拷?
                // 涓嬩竴涓瓙鑺傜偣鏄〃杈惧紡锛岃烦锟?]
                const exprIndex = chainCst.children.indexOf(child) + 1
                if (exprIndex < chainCst.children.length) {
                    const property = SlimeCstToAstUtils.createExpressionAst(chainCst.children[exprIndex])
                    let rBracketToken: any = undefined
                    for (let i = exprIndex + 1; i < chainCst.children.length; i++) {
                        const nextChild = chainCst.children[i]
                        if (nextChild.name === 'RBracket' || nextChild.value === ']') {
                            rBracketToken = SlimeTokenCreateUtils.createRBracketToken(nextChild.loc)
                            break
                        }
                    }
                    result = {
                        type: SlimeAstTypeName.OptionalMemberExpression,
                        object: result,
                        property: property,
                        computed: true,
                        optional: nextIsOptional,
                        optionalChainingToken: nextIsOptional ? nextOptionalChainingToken : undefined,
                        lBracketToken: SlimeTokenCreateUtils.createLBracketToken(child.getLoc()),
                        rBracketToken: rBracketToken,
                        loc: chainCst.loc
                    } as any
                    nextIsOptional = false
                    nextOptionalChainingToken = undefined
                    pendingDotToken = undefined
                }
            } else if (name === 'IdentifierName') {
                // .prop 灞炴€ц锟?- 鍙兘鏄彲閫夋垨鏅拷?
                let property: SlimeIdentifier
                // IdentifierName 鍐呴儴鍖呭惈涓€锟?Identifier 鎴栧叧閿瓧 token
                const tokenCst = child.getChildren()[0]
                property = SlimeAstCreateUtils.createIdentifier(tokenCst.getValue(), tokenCst.getLoc())
                result = {
                    type: SlimeAstTypeName.OptionalMemberExpression,
                    object: result,
                    property: property,
                    computed: false,
                    optional: nextIsOptional,
                    optionalChainingToken: nextIsOptional ? nextOptionalChainingToken : undefined,
                    dotToken: nextIsOptional ? undefined : pendingDotToken,
                    loc: chainCst.loc
                } as any
                nextIsOptional = false
                nextOptionalChainingToken = undefined
                pendingDotToken = undefined
            } else if (name === 'Dot' || child.getValue() === '.') {
                // 鏅拷?. token 涓嶆敼锟?optional 鐘讹拷?
                pendingDotToken = SlimeTokenCreateUtils.createDotToken(child.getLoc())
                continue
            } else if (name === 'RBracket' || child.getValue() === ']') {
                // 璺宠繃 ] token
                continue
            } else if (name === 'PrivateIdentifier') {
                // #prop - 绉佹湁灞炴€ц锟?
                const property = SlimeCstToAstUtils.createPrivateIdentifierAst(child)
                result = {
                    type: SlimeAstTypeName.OptionalMemberExpression,
                    object: result,
                    property: property,
                    computed: false,
                    optional: nextIsOptional,
                    optionalChainingToken: nextIsOptional ? nextOptionalChainingToken : undefined,
                    dotToken: nextIsOptional ? undefined : pendingDotToken,
                    loc: chainCst.loc
                } as any
                nextIsOptional = false
                nextOptionalChainingToken = undefined
                pendingDotToken = undefined
            } else if (name === 'Expression') {
                // 璁＄畻灞炴€х殑琛ㄨ揪寮忛儴鍒嗭紝宸插湪 LBracket 澶勭悊涓锟?
                continue
            }
        }

        return result
    }


    /**
     * 鍒涘缓 OptionalExpression AST锛圗S2020锟?
     * 澶勭悊鍙€夐摼璇硶 ?.
     *
     * OptionalExpression:
     *   MemberExpression OptionalChain
     *   CallExpression OptionalChain
     *   OptionalExpression OptionalChain
     */
    createOptionalExpressionAst(cst: SubhutiCst): SlimeExpression {
        // OptionalExpression 缁撴瀯锟?
        // children[0] = MemberExpression | CallExpression
        // children[1...n] = OptionalChain

        if (!cst.getChildren() || cst.getChildren().length === 0) {
            throw new Error('OptionalExpression: no children')
        }

        // 棣栧厛澶勭悊鍩虹琛ㄨ揪寮忥紙MemberExpression 锟?CallExpression锟?
        let result = SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[0])

        // 澶勭悊 OptionalChain锛堝彲鑳芥湁澶氫釜閾惧紡璋冪敤锟?
        for (let i = 1; i < cst.getChildren().length; i++) {
            const chainCst = cst.getChildren()[i]
            if (chainCst.name === 'OptionalChain') {
                result = SlimeCstToAstUtils.createOptionalChainAst(result, chainCst)
            }
        }

        return result
    }

}

export const SlimeOptionalExpressionCstToAst = new SlimeOptionalExpressionCstToAstSingle()
