/**
 * LiteralCstToAst - 鍩虹瀛楅潰閲忥紙鏁板瓧/瀛楃涓?甯冨皵绛夛級杞崲
 */
import { SubhutiCst } from "subhuti";
import {
    type SlimeArrayElement,
    type SlimeArrayExpression, type SlimeArrowFunctionExpression, type SlimeAssignmentExpression,
    type SlimeClassExpression,
    type SlimeExpression, type SlimeFunctionParam, type SlimeIdentifier, SlimeLiteral,
    SlimeAstTypeName, SlimeNumericLiteral, type SlimeSpreadElement,
    SlimeStringLiteral, SlimeTokenCreateUtils, SlimeAstCreateUtils
} from "slime-ast";
import { SlimeTokenConsumer } from "../../SlimeTokenConsumer.ts";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import { SlimeVariableCstToAstSingle } from "../statements/SlimeVariableCstToAst.ts";

export class SlimeLiteralCstToAstSingle {
    private extractRawValue(cst: SubhutiCst): string {
        const value = cst.getValue()
        if (value !== undefined && value !== null) {
            return String(value)
        }
        return (cst.getChildren() || []).map(child => this.extractRawValue(child)).join('')
    }

    // ==================== 瀛楅潰閲忕浉鍏宠浆鎹㈡柟锟?====================

    /**
     * 甯冨皵瀛楅潰锟?CST 锟?AST
     * BooleanLiteral -> true | false
     */
    createBooleanLiteralAst(cst: SubhutiCst): SlimeLiteral {
        const firstChild = cst.getChildren()?.[0]
        if (firstChild?.name === 'True' || firstChild?.value === 'true') {
            const lit = SlimeAstCreateUtils.createBooleanLiteral(true)
            lit.loc = firstChild.getLoc() || cst.getLoc()
            return lit
        } else {
            const lit = SlimeAstCreateUtils.createBooleanLiteral(false)
            lit.loc = firstChild?.loc || cst.getLoc()
            return lit
        }
    }

    /**
     * [AST 绫诲瀷鏄犲皠] NumericLiteral 缁堢锟?锟?Literal AST
     *
     * 瀛樺湪蹇呰鎬э細NumericLiteral 锟?CST 涓槸缁堢绗︼紝锟?ESTree AST 涓槸 Literal 绫诲瀷锟?
     */
    createNumericLiteralAst(cst: SubhutiCst): SlimeNumericLiteral {
        // 鍏煎澶氱 NumericLiteral 鍚嶇О锛歂umericLiteral, NumericLiteralTok, Number
        const validNames = [
            SlimeTokenConsumer.prototype.NumericLiteral?.name,
            'NumericLiteral',
            'NumericLiteral',
            'Number'
        ]
        if (!validNames.includes(cst.getName())) {
            throw new Error(`Expected NumericLiteral, got ${cst.getName()}`)
        }
        // 淇濆瓨鍘熷鍊硷紙raw锛変互淇濇寔鏍煎紡锛堝鍗佸叚杩涘埗 0xFF锟?
        const rawValue = this.extractRawValue(cst)
        const ast = SlimeAstCreateUtils.createNumericLiteral(Number(rawValue), rawValue)
        ast.loc = cst.getLoc()
        return ast
    }


    /**
    * 瑙ｆ瀽瀛楃涓插瓧闈㈤噺鐨勫€?
    * 浠庡甫寮曞彿鐨勫瓧绗︿覆涓彁鍙栧疄闄呭€硷紝骞舵纭鐞嗚浆涔夊簭鍒?
    */
    static parseStringLiteralValue(raw: string): string {
        if (raw.length < 2) return raw

        const quote = raw[0]
        if ((quote === '"' || quote === "'") && raw[raw.length - 1] === quote) {
            try {
                // 杞崲涓?JSON 鍏煎鏍煎紡骞惰В鏋?
                const jsonStr = quote === '"'
                    ? raw
                    : `"${raw.slice(1, -1).replace(/"/g, '\\"')}"`
                return JSON.parse(jsonStr)  // 澶勭悊鎵€鏈夎浆涔?
            } catch {
                return raw.slice(1, -1)  // 闄嶇骇鏂规
            }
        }
        return raw
    }


    /**
     * [AST 绫诲瀷鏄犲皠] StringLiteral 缁堢锟?锟?Literal AST
     *
     * 瀛樺湪蹇呰鎬э細StringLiteral 锟?CST 涓槸缁堢绗︼紝锟?ESTree AST 涓槸 Literal 绫诲瀷锟?
     */
    createStringLiteralAst(cst: SubhutiCst): SlimeStringLiteral {
        // 鍏煎澶氱 StringLiteral 鍚嶇О锛歋tringLiteral, StringLiteralTok, String
        const validNames = [
            SlimeTokenConsumer.prototype.StringLiteral?.name,
            'StringLiteral',
            'StringLiteral',
            'String'
        ]
        if (!validNames.includes(cst.getName())) {
            throw new Error(`Expected StringLiteral, got ${cst.getName()}`)
        }

        // Parser 杈撳嚭鐨?value 鍖呭惈寮曞彿锛堝 'vue'锛?
        const rawValue = cst.getValue() as string

        // 浣跨敤宸ュ叿鍑芥暟瑙ｆ瀽锛氬幓闄ゅ紩鍙?+ 澶勭悊杞箟
        const cleanValue = SlimeLiteralCstToAstSingle.parseStringLiteralValue(rawValue)

        // AST 宸ュ巶鍙礋璐ｅ垱寤鸿妭鐐癸紝涓嶅仛涓氬姟澶勭悊
        const ast = SlimeAstCreateUtils.createStringLiteral(cleanValue, cst.getLoc(), rawValue)
        return ast
    }



    /**
     * [AST 绫诲瀷鏄犲皠] RegularExpressionLiteral 缁堢锟?锟?Literal AST
     *
     * 瀛樺湪蹇呰鎬э細RegularExpressionLiteral 锟?CST 涓槸缁堢绗︼紝
     * 锟?ESTree AST 涓槸 Literal 绫诲瀷锛岄渶瑕佽В鏋愭鍒欒〃杈惧紡锟?pattern 锟?flags锟?
     *
     * RegularExpressionLiteral: /pattern/flags
     */
    createRegExpLiteralAst(cst: SubhutiCst): any {
        const rawValue = cst.getValue() as string
        // 瑙ｆ瀽姝ｅ垯琛ㄨ揪寮忓瓧闈㈤噺锟?pattern/flags
        // 姝ｅ垯瀛楅潰閲忔牸寮忥細/.../ 鍚庨潰鍙兘璺熺潃 flags
        const match = rawValue.match(/^\/(.*)\/([gimsuy]*)$/)
        if (match) {
            const pattern = match[1]
            const flags = match[2]
            return {
                type: SlimeAstTypeName.Literal,
                value: new RegExp(pattern, flags),
                raw: rawValue,
                regex: {
                    pattern: pattern,
                    flags: flags
                },
                loc: cst.getLoc()
            }
        }
        // 濡傛灉鏃犳硶瑙ｆ瀽锛岃繑鍥炲師濮嬶拷?
        return {
            type: SlimeAstTypeName.Literal,
            value: rawValue,
            raw: rawValue,
            loc: cst.getLoc()
        }
    }



    createLiteralFromToken(token: any): SlimeExpression {
        const tokenName = token.tokenName
        if (tokenName === SlimeTokenConsumer.prototype.NullLiteral?.name) {
            return SlimeAstCreateUtils.createNullLiteralToken()
        } else if (tokenName === SlimeTokenConsumer.prototype.True?.name) {
            return SlimeAstCreateUtils.createBooleanLiteral(true)
        } else if (tokenName === SlimeTokenConsumer.prototype.False?.name) {
            return SlimeAstCreateUtils.createBooleanLiteral(false)
        } else if (tokenName === SlimeTokenConsumer.prototype.NumericLiteral?.name) {
            return SlimeAstCreateUtils.createNumericLiteral(Number(token.tokenValue))
        } else if (tokenName === SlimeTokenConsumer.prototype.StringLiteral?.name) {
            return SlimeAstCreateUtils.createStringLiteral(token.tokenValue)
        } else {
            throw new Error(`Unsupported literal token: ${tokenName}`)
        }
    }


    createLiteralAst(cst: SubhutiCst): SlimeLiteral {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.Literal?.name);
        const firstChild = cst.getChildren()[0]
        let value: SlimeLiteral

        // 澶勭悊鍚勭瀛楅潰閲忕被锟?
        const childName = firstChild.getName()

        // 鐩存帴锟?token 鐨勬儏锟?
        if (childName === SlimeTokenConsumer.prototype.NumericLiteral?.name || childName === 'NumericLiteral') {
            const rawValue = firstChild.getValue() as string
            value = SlimeAstCreateUtils.createNumericLiteral(Number(rawValue), rawValue)
        } else if (childName === SlimeTokenConsumer.prototype.True?.name || childName === 'True') {
            value = SlimeAstCreateUtils.createBooleanLiteral(true)
        } else if (childName === SlimeTokenConsumer.prototype.False?.name || childName === 'False') {
            value = SlimeAstCreateUtils.createBooleanLiteral(false)
        } else if (childName === SlimeTokenConsumer.prototype.NullLiteral?.name || childName === 'NullLiteral' || childName === 'Null') {
            value = SlimeAstCreateUtils.createNullLiteralToken()
        } else if (childName === SlimeTokenConsumer.prototype.StringLiteral?.name || childName === 'StringLiteral') {
            const rawValue = firstChild.getValue() as string
            value = SlimeAstCreateUtils.createStringLiteral(rawValue, firstChild.getLoc(), rawValue)
        }
        // 鍖呰鑺傜偣鐨勬儏鍐碉紙锟?BooleanLiteral 鍖呭惈 True/False锟?
        else if (childName === 'BooleanLiteral' || childName === SlimeParser.prototype.BooleanLiteral?.name) {
            // BooleanLiteral 锟?True | False
            const innerChild = firstChild.getChildren()?.[0]
            if (innerChild?.name === 'True' || innerChild?.value === 'true') {
                value = SlimeAstCreateUtils.createBooleanLiteral(true)
            } else {
                value = SlimeAstCreateUtils.createBooleanLiteral(false)
            }
            value.loc = innerChild?.loc || firstChild.getLoc()
            return value
        }
        // Null 瀛楅潰閲忕殑鍖呰
        else if (childName === 'NullLiteral') {
            value = SlimeAstCreateUtils.createNullLiteralToken()
        }
        // BigInt 瀛楅潰锟?
        else if (childName === 'BigIntLiteral') {
            const rawValue = firstChild.getValue() as string || firstChild.getChildren()?.[0]?.value as string
            // 鍘绘帀鏈熬锟?'n'
            const numStr = rawValue.endsWith('n') ? rawValue.slice(0, -1) : rawValue
            value = SlimeAstCreateUtils.createBigIntLiteral(numStr, rawValue) as any
        }
        // 榛樿澶勭悊涓哄瓧绗︿覆
        else {
            const rawValue = firstChild.getValue() as string
            if (rawValue !== undefined) {
                value = SlimeAstCreateUtils.createStringLiteral(rawValue, firstChild.getLoc(), rawValue)
            } else {
                // 閫掑綊澶勭悊宓屽鐨勫瓙鑺傜偣
                const innerChild = firstChild.getChildren()?.[0]
                if (innerChild?.value) {
                    value = SlimeAstCreateUtils.createStringLiteral(innerChild.getValue(), innerChild.getLoc(), innerChild.getValue())
                } else {
                    throw new Error(`Cannot extract value from Literal: ${childName}`)
                }
            }
        }

        value.loc = firstChild.getLoc()
        return value
    }


    /**
     * Elision锛堥€楀彿绌轰綅锛塁ST 锟?AST
     * Elision -> , | Elision ,
     * 杩斿洖 null 鍏冪礌鐨勬暟锟?
     */
    createElisionAst(cst: SubhutiCst): number {
        // 璁＄畻閫楀彿鏁伴噺锛屾瘡涓€楀彿浠ｈ〃涓€涓┖锟?
        let count = 0
        for (const child of cst.getChildren() || []) {
            if (child.getValue() === ',') {
                count++
            }
        }
        return count
    }


    // 澶勭悊TemplateMiddleList锛氬鐞嗗涓猅emplateMiddle+Expression锟?
    processTemplateMiddleList(cst: SubhutiCst, quasis: any[], expressions: SlimeExpression[]): void {
        // TemplateMiddleList缁撴瀯锛圗s2025锛夛細
        // - children = [TemplateMiddle, Expression, TemplateMiddle, Expression, ...]
        // 鎴栬€呴€掑綊缁撴瀯锟?
        // - children[0] = TemplateMiddle (token)
        // - children[1] = Expression
        // - children[2] = TemplateMiddleList (閫掑綊锛屽彲锟?

        for (let i = 0; i < cst.getChildren().length; i++) {
            const child = cst.getChildren()[i]

            if (child.getName() === SlimeTokenConsumer.prototype.TemplateMiddle?.name ||
                child.getName() === 'TemplateMiddle') {
                const tokenValue = child.getValue() || ''
                const raw = tokenValue.slice(1, -2)
                const cooked = raw
                // 鍘绘帀 } 锟?${
                quasis.push(SlimeAstCreateUtils.createTemplateElement(false, raw, cooked, child.getLoc()))
            } else if (child.getName() === SlimeParser.prototype.Expression?.name ||
                child.getName() === 'Expression') {
                expressions.push(SlimeCstToAstUtils.createExpressionAst(child))
            } else if (child.getName() === SlimeParser.prototype.TemplateMiddleList?.name ||
                child.getName() === 'TemplateMiddleList') {
                // 閫掑綊澶勭悊宓屽锟?TemplateMiddleList
                SlimeCstToAstUtils.processTemplateMiddleList(child, quasis, expressions)
            }
        }
    }


    // 澶勭悊TemplateSpans锛氬彲鑳芥槸TemplateTail鎴朤emplateMiddleList+TemplateTail
    processTemplateSpans(cst: SubhutiCst, quasis: any[], expressions: SlimeExpression[]): void {
        const first = cst.getChildren()[0]

        // 鎯呭喌1锛氱洿鎺ユ槸TemplateTail -> }` 缁撴潫
        if (first.getName() === SlimeTokenConsumer.prototype.TemplateTail?.name) {
            const tokenValue = first.getValue() || ''
            const raw = tokenValue.slice(1, -1)
            const cooked = raw // 鍘绘帀 } 锟?`
            quasis.push(SlimeAstCreateUtils.createTemplateElement(true, raw, cooked, first.getLoc()))
            return
        }

        // 鎯呭喌2锛歍emplateMiddleList -> 鏈夋洿澶氭彃锟?
        if (first.getName() === SlimeParser.prototype.TemplateMiddleList?.name) {
            SlimeCstToAstUtils.processTemplateMiddleList(first, quasis, expressions)

            // 鐒跺悗澶勭悊TemplateTail
            if (cst.getChildren()[1] && cst.getChildren()[1].name === SlimeTokenConsumer.prototype.TemplateTail?.name) {
                const tail = cst.getChildren()[1]
                const tokenValue = tail.value || ''
                const raw = tokenValue.slice(1, -1)
                const cooked = raw // 鍘绘帀 } 锟?`
                quasis.push(SlimeAstCreateUtils.createTemplateElement(true, raw, cooked, tail.loc))
            }
        }
    }


    // 妯℃澘瀛楃涓插锟?
    createTemplateLiteralAst(cst: SubhutiCst): SlimeExpression {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.TemplateLiteral?.name)

        const first = cst.getChildren()[0]

        // 绠€鍗曟ā鏉匡細`hello` (鏃犳彃锟?
        if (first.getName() === SlimeTokenConsumer.prototype.NoSubstitutionTemplate?.name ||
            first.getName() === 'NoSubstitutionTemplate') {
            // 杩斿洖 TemplateLiteral AST锛屼繚鎸佸師濮嬫牸锟?
            const tokenValue = first.getValue() as string || '``'
            const raw = tokenValue.slice(1, -1)
            const cooked = raw
            // 鍘绘帀 ` 锟?`
            const quasis = [SlimeAstCreateUtils.createTemplateElement(true, raw, cooked, first.getLoc())]
            return SlimeAstCreateUtils.createTemplateLiteral(quasis, [], cst.getLoc())
        }

        // 甯︽彃鍊兼ā鏉匡細`hello ${name}` 锟?`a ${x} b ${y} c`
        // ES2025 缁撴瀯: TemplateLiteral -> SubstitutionTemplate -> [TemplateHead, Expression, TemplateSpans]
        // 妫€鏌ユ槸鍚︽槸 SubstitutionTemplate 鍖呰
        let targetCst = cst
        if (first.getName() === SlimeParser.prototype.SubstitutionTemplate?.name ||
            first.getName() === 'SubstitutionTemplate') {
            targetCst = first
        }

        const quasis: any[] = []
        const expressions: SlimeExpression[] = []

        // 閬嶅巻 targetCst.getChildren() 澶勭悊妯℃澘缁撴瀯
        for (let i = 0; i < targetCst.getChildren().length; i++) {
            const child = targetCst.getChildren()[i]

            // TemplateHead: `xxx${
            if (child.getName() === SlimeTokenConsumer.prototype.TemplateHead?.name ||
                child.getName() === 'TemplateHead') {
                const tokenValue = child.getValue() as string || ''
                const raw = tokenValue.slice(1, -2)
                const cooked = raw
                // 鍘绘帀 ` 锟?${
                quasis.push(SlimeAstCreateUtils.createTemplateElement(false, raw, cooked, child.getLoc()))
            }
            // Expression
            else if (child.getName() === SlimeParser.prototype.Expression?.name ||
                child.getName() === 'Expression') {
                expressions.push(SlimeCstToAstUtils.createExpressionAst(child))
            }
            // TemplateSpans
            else if (child.getName() === SlimeParser.prototype.TemplateSpans?.name ||
                child.getName() === 'TemplateSpans') {
                SlimeCstToAstUtils.processTemplateSpans(child, quasis, expressions)
            }
        }

        return SlimeAstCreateUtils.createTemplateLiteral(quasis, expressions, cst.getLoc())
    }

}


export const SlimeLiteralCstToAst = new SlimeLiteralCstToAstSingle()
