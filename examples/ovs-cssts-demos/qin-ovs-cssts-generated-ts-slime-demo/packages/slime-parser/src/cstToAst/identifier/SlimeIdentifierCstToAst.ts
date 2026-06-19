/**
 * IdentifierCstToAst - 鏍囪瘑绗︾浉鍏宠浆鎹?
 */
import { SubhutiCst, type SubhutiSourceLocation } from "subhuti";
import {
    SlimeAstCreateUtils,
    SlimeClassBody, SlimeFunctionParam,
    SlimeIdentifier,
    SlimeMethodDefinition, SlimePattern,
    SlimePropertyDefinition,
    SlimeStatement,
    SlimeAstTypeName,
    SlimeTokenCreateUtils,
    SlimeJavascriptCreateUtils,
} from "slime-ast";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import { SlimeTokenConsumer } from "../../SlimeTokenConsumer.ts";
import { SlimeVariableCstToAstSingle } from "../statements/SlimeVariableCstToAst.ts";

export class SlimeIdentifierCstToAstSingle {

    createIdentifierNameAst(cst: SubhutiCst): SlimeIdentifier {
        // IdentifierName 鍙兘锟?
        // 1. 鐩存帴锟?value 锟?token
        // 2. 鍖呭惈瀛愯妭鐐圭殑瑙勫垯鑺傜偣

        // 濡傛灉鐩存帴锟?value锛屼娇鐢ㄥ畠
        if (cst.getValue() !== undefined) {
            const decodedName = SlimeCstToAstUtils.decodeUnicodeEscapes(cst.getValue() as string)
            return SlimeAstCreateUtils.createIdentifier(decodedName, cst.getLoc())
        }

        // 鍚﹀垯閫掑綊鏌ユ壘 value
        let current = cst
        while (current.children && current.children.length > 0 && current.value === undefined) {
            current = current.children[0]
        }

        if (current.value !== undefined) {
            const decodedName = SlimeCstToAstUtils.decodeUnicodeEscapes(current.value as string)
            return SlimeAstCreateUtils.createIdentifier(decodedName, current.loc || cst.getLoc())
        }

        throw new Error(`createIdentifierNameAst: Cannot extract value from IdentifierName`)
    }


    /**
     * [TypeScript] createBindingIdentifierAst 鏀寔鍙€夊弬鏁板拰绫诲瀷娉ㄨВ
     */
    createBindingIdentifierAst(cst: SubhutiCst): SlimeIdentifier {
        const children = cst.getChildren() || []
        const first = children[0]

        let identifier: SlimeIdentifier

        if (first.getName() === 'Identifier' || first.getName() === SlimeParser.prototype.Identifier?.name) {
            const tokenCst = first.getChildren()?.[0]
            if (tokenCst && tokenCst.getValue() !== undefined) {
                identifier = SlimeJavascriptCreateUtils.createIdentifier(tokenCst.getValue(), tokenCst.getLoc())
            } else {
                throw new Error(`createBindingIdentifierAst: Cannot extract value from Identifier`)
            }
        } else if (first.getValue() !== undefined) {
            identifier = SlimeJavascriptCreateUtils.createIdentifier(first.getValue(), first.getLoc())
        } else {
            throw new Error(`createBindingIdentifierAst: Cannot extract identifier value from ${first.getName()}`)
        }

        // [TypeScript] 妫€鏌ユ槸鍚︽槸鍙€夊弬鏁?(?)
        const questionCst = children.find(child =>
            child.getName() === 'Question' || child.getValue() === '?'
        )
        if (questionCst) {
            (identifier as any).optional = true;
            (identifier as any).questionToken = { type: 'Question', value: '?', loc: questionCst.loc }
        }

        // [TypeScript] 妫€鏌ユ槸鍚︽湁绫诲瀷娉ㄨВ
        const tsTypeAnnotationName = SlimeParser.prototype.TSTypeAnnotation?.name || 'TSTypeAnnotation'
        const typeAnnotationCst = children.find(child =>
            child.getName() === tsTypeAnnotationName || child.getName() === 'TSTypeAnnotation'
        )
        if (typeAnnotationCst) {
            (identifier as any).typeAnnotation = SlimeCstToAstUtils.createTSTypeAnnotationAst(typeAnnotationCst)
        }

        return identifier
    }


    /**
     * [AST 绫诲瀷鏄犲皠] PrivateIdentifier 缁堢锟?锟?Identifier AST
     *
     * 瀛樺湪蹇呰鎬э細PrivateIdentifier 锟?CST 涓槸涓€涓粓绔锛坱oken锛夛紝
     * 浣嗗湪 ESTree AST 涓渶瑕佽〃绀轰负 Identifier 鑺傜偣锛宯ame 锟?# 寮€澶达拷?
     *
     * PrivateIdentifier :: # IdentifierName
     * AST 琛ㄧず锛歿 type: "Identifier", name: "#count" }
     */
    createPrivateIdentifierAst(cst: SubhutiCst): SlimeIdentifier {
        // Es2025Parser: PrivateIdentifier 鏄竴涓洿鎺ョ殑 token锛寁alue 宸茬粡鍖呭惈 #
        // 渚嬪锛歿 name: 'PrivateIdentifier', value: '#count' } 锟?value: '#\u{61}'
        if (cst.getValue()) {
            const rawName = cst.getValue() as string
            const decodedName = SlimeCstToAstUtils.decodeUnicodeEscapes(rawName)
            // 淇濆瓨鍘熷鍊煎拰瑙ｇ爜鍚庣殑锟?
            const name = decodedName.startsWith('#') ? decodedName : '#' + decodedName
            const raw = rawName.startsWith('#') ? rawName : '#' + rawName
            const identifier = SlimeAstCreateUtils.createIdentifier(name, cst.getLoc())
            // 濡傛灉鍘熷鍊间笌瑙ｇ爜鍊间笉鍚岋紝淇濆瓨 raw 浠ヤ究鐢熸垚鍣ㄤ娇锟?
            if (raw !== name) {
                (identifier as any).raw = raw
            }
            return identifier
        }

        // 鏃х増鍏煎锛歅rivateIdentifier -> HashTok + IdentifierName
        if (cst.getChildren() && cst.getChildren().length >= 2) {
            const identifierNameCst = cst.getChildren()[1]
            const identifierCst = identifierNameCst.getChildren()[0]
            const rawName = identifierCst.value as string
            const decodedName = SlimeCstToAstUtils.decodeUnicodeEscapes(rawName)
            const identifier = SlimeAstCreateUtils.createIdentifier('#' + decodedName)
            // 淇濆瓨鍘熷锟?
            if (rawName !== decodedName) {
                (identifier as any).raw = '#' + rawName
            }
            return identifier
        }

        // 濡傛灉鍙湁涓€涓瓙鑺傜偣锛屽彲鑳芥槸鐩存帴锟?IdentifierName
        if (cst.getChildren() && cst.getChildren().length === 1) {
            const child = cst.getChildren()[0]
            if (child.getValue()) {
                const rawName = child.getValue() as string
                const decodedName = SlimeCstToAstUtils.decodeUnicodeEscapes(rawName)
                const identifier = SlimeAstCreateUtils.createIdentifier('#' + decodedName)
                if (rawName !== decodedName) {
                    (identifier as any).raw = '#' + rawName
                }
                return identifier
            }
        }

        throw new Error('createPrivateIdentifierAst: 鏃犳硶瑙ｆ瀽 PrivateIdentifier')
    }

    /**
     * 鍒涘缓 LabelIdentifier 锟?AST
     *
     * 璇硶锛歀abelIdentifier -> Identifier | [~Yield] yield | [~Await] await
     *
     * LabelIdentifier 鐢ㄤ簬 break/continue 璇彞鐨勬爣绛惧拰 LabelledStatement 鐨勬爣绛撅拷?
     * 缁撴瀯锟?IdentifierReference 鐩稿悓锟?
     */
    createLabelIdentifierAst(cst: SubhutiCst): SlimeIdentifier {
        const expectedName = SlimeParser.prototype.LabelIdentifier?.name || 'LabelIdentifier'
        if (cst.getName() !== expectedName && cst.getName() !== 'LabelIdentifier') {
            throw new Error(`Expected LabelIdentifier, got ${cst.getName()}`)
        }

        // LabelIdentifier -> Identifier | yield | await
        const child = cst.getChildren()?.[0]
        if (!child) {
            throw new Error('LabelIdentifier has no children')
        }

        return SlimeCstToAstUtils.createIdentifierAst(child)
    }


    /**
     * 鍒涘缓 IdentifierReference 锟?AST
     *
     * 璇硶锛欼dentifierReference -> Identifier | yield | await
     *
     * IdentifierReference 鏄 Identifier 鐨勫紩鐢ㄥ寘瑁咃紝
     * 锟?ES 瑙勮寖涓敤浜庡尯鍒嗘爣璇嗙鐨勪笉鍚屼娇鐢ㄥ満鏅拷?
     */
    createIdentifierReferenceAst(cst: SubhutiCst): SlimeIdentifier {
        const expectedName = SlimeParser.prototype.IdentifierReference?.name || 'IdentifierReference'
        if (cst.getName() !== expectedName && cst.getName() !== 'IdentifierReference') {
            throw new Error(`Expected IdentifierReference, got ${cst.getName()}`)
        }

        // IdentifierReference -> Identifier | yield | await
        const child = cst.getChildren()?.[0]
        if (!child) {
            throw new Error('IdentifierReference has no children')
        }

        return SlimeCstToAstUtils.createIdentifierAst(child)
    }


    createIdentifierAst(cst: SubhutiCst): SlimeIdentifier {
        // Support Identifier, IdentifierName, and contextual keywords (yield, await) used as identifiers
        const expectedName = SlimeParser.prototype.Identifier?.name || 'Identifier'
        const isIdentifier = cst.getName() === expectedName || cst.getName() === 'Identifier'
        const isIdentifierName = cst.getName() === 'IdentifierName' || cst.getName() === SlimeParser.prototype.IdentifierName?.name
        const isYield = cst.getName() === 'Yield'
        const isAwait = cst.getName() === 'Await'

        // ES2025 Parser: Identifier 瑙勫垯鍐呴儴璋冪敤 IdentifierNameTok()
        // 鎵€锟?CST 缁撴瀯鏄細Identifier -> IdentifierNameTok (token with value)
        let value: string
        let tokenLoc: SubhutiSourceLocation | undefined = undefined

        // 澶勭悊 yield/await 浣滀负鏍囪瘑绗︾殑鎯呭喌
        if (isYield || isAwait) {
            // 杩欐槸涓€锟?token锛岀洿鎺ヤ娇鐢ㄥ叾锟?
            value = cst.getValue() as string || cst.getName().toLowerCase()
            tokenLoc = cst.getLoc()
        } else if (isIdentifierName) {
            // IdentifierName 缁撴瀯锛欼dentifierName -> token (with value)
            if (cst.getValue() !== undefined && cst.getValue() !== null) {
                value = cst.getValue() as string
                tokenLoc = cst.getLoc()
            } else if (cst.getChildren() && cst.getChildren().length > 0) {
                const tokenCst = cst.getChildren()[0]
                if (tokenCst.getValue() !== undefined) {
                    value = tokenCst.getValue() as string
                    tokenLoc = tokenCst.getLoc() || cst.getLoc()
                } else {
                    throw new Error(`createIdentifierAst: Cannot extract value from IdentifierName CST`)
                }
            } else {
                throw new Error(`createIdentifierAst: Invalid IdentifierName CST structure`)
            }
        } else if (!isIdentifier) {
            throw new Error(`Expected Identifier, got ${cst.getName()}`)
        } else if (cst.getValue() !== undefined && cst.getValue() !== null) {
            // 鐩存帴锟?token锛堟棫鐗堝吋瀹癸級
            value = cst.getValue() as string
            tokenLoc = cst.getLoc()
        } else if (cst.getChildren() && cst.getChildren().length > 0) {
            // ES2025: Identifier 瑙勫垯锛屽瓙鑺傜偣锟?IdentifierNameTok
            const tokenCst = cst.getChildren()[0]
            if (tokenCst.getValue() !== undefined) {
                value = tokenCst.getValue() as string
                tokenLoc = tokenCst.getLoc() || cst.getLoc()
            } else {
                throw new Error(`createIdentifierAst: Cannot extract value from Identifier CST`)
            }
        } else {
            throw new Error(`createIdentifierAst: Invalid Identifier CST structure`)
        }

        // 瑙ｇ爜 Unicode 杞箟搴忓垪锛堝 \u0061 -> a锟?
        const decodedName = SlimeCstToAstUtils.decodeUnicodeEscapes(value)
        // 浣跨敤 token 锟?loc锛堝寘鍚師濮嬪€硷級锛岃€屼笉鏄鍒欑殑 loc
        const identifier = SlimeAstCreateUtils.createIdentifier(decodedName, tokenLoc || cst.getLoc())
        return identifier
    }

    // ============================================
    // TypeScript 妯″潡澹版槑鐩稿叧鏂规硶
    // ============================================

    /**
     * [TypeScript] 杞崲 TSModuleDeclaration CST 涓?AST
     * namespace A.B.C { } / module "name" { }
     */
    createTSModuleDeclarationAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let id: any = undefined
        let body: any = undefined
        let declare = false
        let global = false

        // 妫€鏌ユ槸鍚︽槸 namespace 鎴?module
        const isNamespace = children.some(c => c.value === 'namespace')
        const isModule = children.some(c => c.value === 'module')

        // 鎵惧埌妯″潡鏍囪瘑绗?
        const moduleIdCst = children.find(c => c.name === 'TSModuleIdentifier')
        if (moduleIdCst) {
            id = this.createTSModuleIdentifierAst(moduleIdCst)
        } else {
            // 鍙兘鏄瓧绗︿覆瀛楅潰閲忔ā鍧楀悕 module "name"
            const stringCst = children.find(c => c.name === 'StringLiteral')
            if (stringCst) {
                const tokenCst = stringCst.children?.[0] || stringCst
                id = {
                    type: 'Literal',
                    value: tokenCst.getValue(),
                    raw: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            }
        }

        // 鎵惧埌妯″潡浣?
        const moduleBlockCst = children.find(c => c.name === 'TSModuleBlock')
        if (moduleBlockCst) {
            body = this.createTSModuleBlockAst(moduleBlockCst)
        }

        return {
            type: SlimeAstTypeName.TSModuleDeclaration,
            id,
            body,
            declare,
            global,
            loc: cst.getLoc(),
        }
    }

    /**
     * [TypeScript] 杞崲 TSModuleIdentifier CST 涓?AST
     * 鏀寔鐐瑰垎闅旂殑宓屽鍛藉悕绌洪棿 A.B.C
     */
    createTSModuleIdentifierAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []
        const nameParts: string[] = []

        for (const child of children) {
            if (child.getName() === 'Identifier' || child.getName() === 'IdentifierName') {
                const tokenCst = child.getChildren()?.[0] || child
                if (tokenCst.getValue()) {
                    nameParts.push(tokenCst.getValue())
                }
            }
        }

        if (nameParts.length === 0) {
            throw new Error('TSModuleIdentifier: no identifier found')
        }

        // 瀵逛簬宓屽鍛藉悕绌洪棿 A.B.C锛岃繑鍥炵涓€涓爣璇嗙
        // 宓屽閮ㄥ垎浼氬湪 body 涓€掑綊澶勭悊
        return {
            type: 'Identifier',
            name: nameParts.join('.'),
            loc: cst.getLoc(),
        }
    }

    /**
     * [TypeScript] 杞崲 TSModuleBlock CST 涓?AST
     */
    createTSModuleBlockAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []
        const body: any[] = []

        for (const child of children) {
            if (child.getName() === 'ModuleItem') {
                // ModuleItem 闇€瑕佽В鍖呰幏鍙栧唴閮ㄨ妭鐐?
                const innerItem = child.getChildren()?.[0]
                if (innerItem) {
                    const result = SlimeCstToAstUtils.createModuleItemAst(innerItem)
                    if (result) {
                        if (Array.isArray(result)) {
                            body.push(...result)
                        } else {
                            body.push(result)
                        }
                    }
                }
            } else if (child.getName() === 'ModuleItemList') {
                // 閫掑綊澶勭悊 ModuleItemList
                const items = SlimeCstToAstUtils.createModuleItemListAst(child)
                body.push(...items)
            } else if (child.getName() === 'LBrace' || child.getName() === 'RBrace') {
                // 璺宠繃澶ф嫭鍙?
                continue
            } else if (child.getName() === 'StatementListItem') {
                // 鐩存帴澶勭悊璇彞
                const result = SlimeCstToAstUtils.createStatementListItemAst(child)
                if (result) {
                    body.push(...result)
                }
            }
        }

        return {
            type: SlimeAstTypeName.TSModuleBlock,
            body,
            loc: cst.getLoc(),
        }
    }

    /**
     * [TypeScript] 杞崲 TSDeclareStatement CST 涓?AST
     * declare const/let/var/function/class/namespace/module/global
     */
    createTSDeclareStatementAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        // 妫€鏌ュ０鏄庣被鍨?
        const hasConst = children.some(c => c.name === 'Const' || c.value === 'const')
        const hasLet = children.some(c => c.name === 'Let' || c.value === 'let')
        const hasVar = children.some(c => c.name === 'Var' || c.value === 'var')
        const hasFunction = children.some(c => c.name === 'Function' || c.value === 'function')
        const hasClass = children.some(c => c.name === 'Class' || c.value === 'class')
        const hasNamespace = children.some(c => c.name === 'TSModuleDeclaration')
        const hasGlobal = children.some(c => c.value === 'global')

        if (hasConst || hasLet || hasVar) {
            // declare const/let/var x: Type
            const kind = hasConst ? 'const' : hasLet ? 'let' : 'var'
            const identifierCst = children.find(c => c.name === 'BindingIdentifier')
            const typeAnnotationCst = children.find(c => c.name === 'TSTypeAnnotation')

            let id: any = undefined
            if (identifierCst) {
                id = this.createBindingIdentifierAst(identifierCst)
            }

            return {
                type: 'VariableDeclaration',
                kind,
                declarations: [{
                    type: 'VariableDeclarator',
                    id,
                    init: null,
                    loc: cst.getLoc(),
                }],
                declare: true,
                loc: cst.getLoc(),
            }
        }

        if (hasFunction) {
            // declare function name(): Type
            const identifierCst = children.find(c => c.name === 'Identifier')
            const typeParamsCst = children.find(c => c.name === 'TSTypeParameterDeclaration')
            const formalParamsCst = children.find(c => c.name === 'FormalParameters')
            const returnTypeCst = children.find(c => c.name === 'TSTypeAnnotation')

            let id: any = undefined
            if (identifierCst) {
                const tokenCst = identifierCst.children?.[0] || identifierCst
                id = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            }

            return {
                type: 'TSDeclareFunction',
                id,
                params: formalParamsCst ? SlimeCstToAstUtils.createFormalParametersAst(formalParamsCst) : [],
                typeParameters: typeParamsCst ? SlimeCstToAstUtils.createTSTypeParameterDeclarationAst(typeParamsCst) : undefined,
                returnType: returnTypeCst ? SlimeCstToAstUtils.createTSTypeAnnotationAst(returnTypeCst) : undefined,
                declare: true,
                loc: cst.getLoc(),
            }
        }

        if (hasClass) {
            // declare class Name { }
            const identifierCst = children.find(c => c.name === 'Identifier')
            const typeParamsCst = children.find(c => c.name === 'TSTypeParameterDeclaration')
            const classTailCst = children.find(c => c.name === 'ClassTail')

            let id: any = undefined
            if (identifierCst) {
                const tokenCst = identifierCst.children?.[0] || identifierCst
                id = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            }

            return {
                type: 'ClassDeclaration',
                id,
                typeParameters: typeParamsCst ? SlimeCstToAstUtils.createTSTypeParameterDeclarationAst(typeParamsCst) : undefined,
                body: classTailCst ? SlimeCstToAstUtils.createClassTailAst(classTailCst) : { type: 'ClassBody', body: [] },
                declare: true,
                loc: cst.getLoc(),
            }
        }

        if (hasNamespace) {
            // declare namespace/module
            const moduleCst = children.find(c => c.name === 'TSModuleDeclaration')
            if (moduleCst) {
                const result = this.createTSModuleDeclarationAst(moduleCst)
                result.declare = true
                return result
            }
        }

        if (hasGlobal) {
            // declare global { }
            const moduleBlockCst = children.find(c => c.name === 'TSModuleBlock')
            return {
                type: SlimeAstTypeName.TSModuleDeclaration,
                id: { type: 'Identifier', name: 'global', loc: cst.getLoc() },
                body: moduleBlockCst ? this.createTSModuleBlockAst(moduleBlockCst) : undefined,
                declare: true,
                global: true,
                loc: cst.getLoc(),
            }
        }

        throw new Error(`TSDeclareStatement: unsupported declaration type`)
    }
}

export const SlimeIdentifierCstToAst = new SlimeIdentifierCstToAstSingle()
