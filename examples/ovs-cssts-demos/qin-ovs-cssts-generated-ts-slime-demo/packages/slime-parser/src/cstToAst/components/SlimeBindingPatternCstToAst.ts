/**
 * BindingPatternCstToAst - 缁戝畾妯″紡杞崲
 */
import { SubhutiCst } from "subhuti";
import {
    type SlimeArrayPattern,
    type SlimeBlockStatement, type SlimeExpressionStatement,
    type SlimeFunctionExpression, type SlimeFunctionParam,
    SlimeIdentifier, type SlimeObjectPattern,
    SlimePattern,
    SlimeRestElement, type SlimeReturnStatement,
    type SlimeStatement, SlimeTokenCreateUtils, SlimeAstTypeName,
    type SlimeArrayPatternElement, type SlimeLBracketToken, type SlimeRBracketToken,
    type SlimeCommaToken, type SlimeLBraceToken, type SlimeRBraceToken,
    type SlimeObjectPatternProperty, type SlimeAssignmentProperty, SlimeAstCreateUtils
} from "slime-ast";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

export class SlimeBindingPatternCstToAstSingle {

    createBindingElementAst(cst: SubhutiCst): any {
        // 鏀寔 BindingElement, FormalParameter, TSParameterProperty
        const cstName = cst.getName()
        if (cstName !== 'BindingElement' && cstName !== SlimeParser.prototype.BindingElement?.name &&
            cstName !== 'FormalParameter' && cstName !== SlimeParser.prototype.FormalParameter?.name &&
            cstName !== 'TSParameterProperty' && cstName !== SlimeParser.prototype.TSParameterProperty?.name) {
            throw new Error(`Expected BindingElement, FormalParameter or TSParameterProperty, got ${cstName}`)
        }

        // [TypeScript] 澶勭悊 TSParameterProperty
        if (cstName === 'TSParameterProperty' || cstName === SlimeParser.prototype.TSParameterProperty?.name) {
            return this.createTSParameterPropertyAst(cst)
        }

        const first = cst.getChildren()[0]

        // 妫€鏌ョ涓€涓瓙鑺傜偣鏄惁鏄?TSParameterProperty
        if (first.getName() === 'TSParameterProperty' || first.getName() === SlimeParser.prototype.TSParameterProperty?.name) {
            return this.createTSParameterPropertyAst(first)
        }

        if (first.getName() === SlimeParser.prototype.SingleNameBinding?.name) {
            return SlimeCstToAstUtils.createSingleNameBindingAst(first)
        } else if (first.getName() === SlimeParser.prototype.BindingPattern?.name ||
            first.getName() === SlimeParser.prototype.ArrayBindingPattern?.name ||
            first.getName() === SlimeParser.prototype.ObjectBindingPattern?.name) {
            // 瑙ｆ瀯鍙傛暟锛歠unction({name, age}) 锟?function([a, b])
            // 妫€鏌ユ槸鍚︽湁 Initializer锛堥粯璁ゅ€硷級
            const initializer = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.Initializer?.name || ch.name === 'Initializer')
            let pattern: SlimePattern
            if (first.getName() === SlimeParser.prototype.BindingPattern?.name) {
                pattern = SlimeCstToAstUtils.createBindingPatternAst(first)
            } else if (first.getName() === SlimeParser.prototype.ArrayBindingPattern?.name) {
                pattern = SlimeCstToAstUtils.createArrayBindingPatternAst(first)
            } else {
                pattern = SlimeCstToAstUtils.createObjectBindingPatternAst(first)
            }

            if (initializer) {
                // 鏈夐粯璁ゅ€硷紝鍒涘缓 AssignmentPattern
                const init = SlimeCstToAstUtils.createInitializerAst(initializer)
                const assignCst = initializer.children?.[0]
                const equalToken = assignCst ? SlimeTokenCreateUtils.createAssignToken(assignCst.getLoc()) : undefined
                return {
                    type: SlimeAstTypeName.AssignmentPattern,
                    left: pattern,
                    right: init,
                    equalToken,
                    loc: cst.getLoc()
                }
            }
            return pattern
        }
        return SlimeCstToAstUtils.createSingleNameBindingAst(first)
    }

    createSingleNameBindingAst(cst: SubhutiCst): any {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.SingleNameBinding?.name);
        //BindingIdentifier + Initializer?
        const first = cst.getChildren()[0]
        const id = SlimeCstToAstUtils.createBindingIdentifierAst(first)

        // 妫€鏌ユ槸鍚︽湁榛樿鍊硷紙Initializer锛?
        const initializer = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.Initializer?.name)
        if (initializer) {
            // 鏈夐粯璁ゅ€硷紝鍒涘缓AssignmentPattern
            const init = SlimeCstToAstUtils.createInitializerAst(initializer)
            const assignCst = initializer.children?.[0]
            const equalToken = assignCst ? SlimeTokenCreateUtils.createAssignToken(assignCst.getLoc()) : undefined
            return {
                type: SlimeAstTypeName.AssignmentPattern,
                left: id,
                right: init,
                equalToken,
                loc: cst.getLoc()
            }
        }

        return id
    }


    /**
     * [TypeScript] TSParameterProperty CST 鍒?AST
     * TSParameterProperty: TSAccessibilityModifier_opt readonly_opt BindingIdentifier ?_opt TSTypeAnnotation_opt Initializer_opt
     */
    createTSParameterPropertyAst(cst: SubhutiCst): any {
        let accessibility: 'public' | 'private' | 'protected' | undefined = undefined
        let isReadonly = false
        let isOptional = false
        let identifier: SlimeIdentifier | null = null
        let typeAnnotation: any = undefined
        let initializer: any = undefined

        for (const child of cst.getChildren() || []) {
            const name = child.getName()
            const value = child.getValue()

            if (value === 'public' || name === 'TSPublic') {
                accessibility = 'public'
            } else if (value === 'private' || name === 'TSPrivate') {
                accessibility = 'private'
            } else if (value === 'protected' || name === 'TSProtected') {
                accessibility = 'protected'
            } else if (value === 'readonly' || name === 'TSReadonly') {
                isReadonly = true
            } else if (name === 'Identifier' || name === SlimeParser.prototype.Identifier?.name) {
                identifier = SlimeCstToAstUtils.createIdentifierAst(child)
            } else if (value === '?' || name === 'Question') {
                isOptional = true
            } else if (name === 'TSTypeAnnotation') {
                typeAnnotation = SlimeCstToAstUtils.createTSTypeAnnotationAst(child)
            } else if (name === 'Initializer' || name === SlimeParser.prototype.Initializer?.name) {
                initializer = SlimeCstToAstUtils.createInitializerAst(child)
            }
        }

        // 鍒涘缓 TSParameterProperty AST
        const result: any = {
            type: 'TSParameterProperty',
            parameter: identifier,
            loc: cst.getLoc()
        }

        if (accessibility) {
            result.accessibility = accessibility
        }
        if (isReadonly) {
            result.readonly = true
        }
        if (isOptional && identifier) {
            (identifier as any).optional = true
        }
        if (typeAnnotation && identifier) {
            (identifier as any).typeAnnotation = typeAnnotation
        }
        if (initializer) {
            // 鏈夐粯璁ゅ€兼椂锛宲arameter 鍙樻垚 AssignmentPattern
            result.parameter = {
                type: SlimeAstTypeName.AssignmentPattern,
                left: identifier,
                right: initializer,
                loc: cst.getLoc()
            }
        }

        return result
    }


    /**
     * BindingRestProperty CST 鍒?AST
     */
    createBindingRestPropertyAst(cst: SubhutiCst): SlimeRestElement {
        const argument = cst.getChildren()?.find(ch =>
            ch.name === SlimeParser.prototype.BindingIdentifier?.name ||
            ch.name === 'BindingIdentifier'
        )
        const id = argument ? SlimeCstToAstUtils.createBindingIdentifierAst(argument) : null
        const ellipsisCst = cst.getChildren()?.find((ch: any) => ch.value === '...' || ch.name === 'Ellipsis')
        const ellipsisToken = ellipsisCst ? SlimeTokenCreateUtils.createEllipsisToken(ellipsisCst.loc) : undefined
        return SlimeAstCreateUtils.createRestElement(id as any, cst.getLoc(), ellipsisToken)
    }

    /**
     * BindingProperty CST 锟?AST
     * BindingProperty -> SingleNameBinding | PropertyName : BindingElement
     */
    createBindingPropertyAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        // 妫€鏌ユ槸鍚︽槸 SingleNameBinding
        const singleNameBinding = children.find(ch =>
            ch.name === SlimeParser.prototype.SingleNameBinding?.name ||
            ch.name === 'SingleNameBinding'
        )
        if (singleNameBinding) {
            return SlimeCstToAstUtils.createSingleNameBindingAst(singleNameBinding)
        }

        // 鍚﹀垯锟?PropertyName : BindingElement
        const propertyName = children.find(ch =>
            ch.name === SlimeParser.prototype.PropertyName?.name ||
            ch.name === 'PropertyName'
        )
        const bindingElement = children.find(ch =>
            ch.name === SlimeParser.prototype.BindingElement?.name ||
            ch.name === 'BindingElement'
        )

        const key = propertyName ? SlimeCstToAstUtils.createPropertyNameAst(propertyName) : null
        const value = bindingElement ? SlimeCstToAstUtils.createBindingElementAst(bindingElement) : null
        const colonCst = children.find(ch => ch.name === 'Colon' || ch.value === ':')

        const prop: any = {
            type: SlimeAstTypeName.Property,
            key: key,
            value: value,
            kind: 'init',
            method: false,
            shorthand: false,
            computed: false,
            loc: cst.getLoc()
        }

        if (colonCst) {
            prop.colonToken = SlimeTokenCreateUtils.createColonToken(colonCst.loc)
        }

        const computedNameCst = propertyName
            ? ((propertyName.name === 'ComputedPropertyName' || propertyName.name === SlimeParser.prototype.ComputedPropertyName?.name)
                ? propertyName
                : propertyName.children?.[0])
            : null
        if (computedNameCst &&
            (computedNameCst.name === 'ComputedPropertyName' || computedNameCst.name === SlimeParser.prototype.ComputedPropertyName?.name)) {
            prop.computed = true
            const lBracketCst = computedNameCst.children?.find((ch: any) => ch.name === 'LBracket' || ch.value === '[')
            const rBracketCst = computedNameCst.children?.find((ch: any) => ch.name === 'RBracket' || ch.value === ']')
            if (lBracketCst) {
                prop.lBracketToken = SlimeTokenCreateUtils.createLBracketToken(lBracketCst.loc)
            }
            if (rBracketCst) {
                prop.rBracketToken = SlimeTokenCreateUtils.createRBracketToken(rBracketCst.loc)
            }
        }

        return prop
    }

    /**
     * BindingPropertyList CST 杞?AST
     */
    createBindingPropertyListAst(cst: SubhutiCst): any[] {
        const properties: any[] = []
        for (const child of cst.getChildren() || []) {
            if (child.getName() === SlimeParser.prototype.BindingProperty?.name ||
                child.getName() === 'BindingProperty') {
                properties.push(SlimeCstToAstUtils.createBindingPropertyAst(child))
            }
        }
        return properties
    }

    /**
     * BindingElementList CST 锟?AST
     */
    createBindingElementListAst(cst: SubhutiCst): any[] {
        const elements: any[] = []
        for (const child of cst.getChildren() || []) {
            if (child.getName() === SlimeParser.prototype.BindingElement?.name ||
                child.getName() === 'BindingElement') {
                elements.push(SlimeCstToAstUtils.createBindingElementAst(child))
            } else if (child.getName() === SlimeParser.prototype.BindingRestElement?.name ||
                child.getName() === 'BindingRestElement') {
                elements.push(SlimeCstToAstUtils.createBindingRestElementAst(child))
            } else if (child.getName() === SlimeParser.prototype.BindingElisionElement?.name ||
                child.getName() === 'BindingElisionElement') {
                // Elision 鍚庤窡 BindingElement
                elements.push(null) // 绌轰綅
                const bindingElement = child.getChildren()?.find((ch: SubhutiCst) =>
                    ch.name === SlimeParser.prototype.BindingElement?.name ||
                    ch.name === 'BindingElement'
                )
                if (bindingElement) {
                    elements.push(SlimeCstToAstUtils.createBindingElementAst(bindingElement))
                }
            }
        }
        return elements
    }

    /**
     * BindingElisionElement CST 锟?AST
     */
    createBindingElisionElementAst(cst: SubhutiCst): any {
        const bindingElement = cst.getChildren()?.find(ch =>
            ch.name === SlimeParser.prototype.BindingElement?.name ||
            ch.name === 'BindingElement'
        )
        if (bindingElement) {
            return SlimeCstToAstUtils.createBindingElementAst(bindingElement)
        }
        return null
    }




    createBindingPatternAst(cst: SubhutiCst): SlimePattern {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.BindingPattern?.name)

        const child = cst.getChildren()[0]

        if (child.getName() === SlimeParser.prototype.ArrayBindingPattern?.name) {
            return SlimeCstToAstUtils.createArrayBindingPatternAst(child)
        } else if (child.getName() === SlimeParser.prototype.ObjectBindingPattern?.name) {
            return SlimeCstToAstUtils.createObjectBindingPatternAst(child)
        } else {
            throw new Error(`Unknown BindingPattern type: ${child.getName()}`)
        }
    }

    createArrayBindingPatternAst(cst: SubhutiCst): SlimeArrayPattern {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ArrayBindingPattern?.name)

        // CST缁撴瀯锛歔LBracket, BindingElementList?, Comma?, Elision?, BindingRestElement?, RBracket]
        const elements: SlimeArrayPatternElement[] = []

        // 鎻愬彇 LBracket 锟?RBracket tokens
        let lBracketToken: SlimeLBracketToken | undefined
        let rBracketToken: SlimeRBracketToken | undefined
        for (const child of cst.getChildren()) {
            if (child.getValue() === '[') {
                lBracketToken = SlimeTokenCreateUtils.createLBracketToken(child.getLoc())
            } else if (child.getValue() === ']') {
                rBracketToken = SlimeTokenCreateUtils.createRBracketToken(child.getLoc())
            }
        }

        // 鏌ユ壘BindingElementList
        const bindingList = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.BindingElementList?.name)
        if (bindingList) {
            // BindingElementList鍖呭惈BindingElisionElement鍜孋omma
            let pendingCommaToken: SlimeCommaToken | undefined
            for (let i = 0; i < bindingList.children.length; i++) {
                const child = bindingList.children[i]
                if (child.getValue() === ',') {
                    // 濡傛灉鏈夊緟澶勭悊鐨勫厓绱狅紝灏嗛€楀彿鍏宠仈鍒板畠
                    if (elements.length > 0 && !elements[elements.length - 1].commaToken) {
                        elements[elements.length - 1].commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                    } else {
                        pendingCommaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                    }
                } else if (child.getName() === SlimeParser.prototype.BindingElisionElement?.name) {
                    // BindingElisionElement鍙兘鍖呭惈锛欵lision + BindingElement
                    // 鍏堟鏌ユ槸鍚︽湁Elision锛堣烦杩囩殑鍏冪礌锟?
                    const elision = child.getChildren().find((ch: any) =>
                        ch.name === SlimeParser.prototype.Elision?.name)
                    if (elision) {
                        // Elision鍙兘鍖呭惈澶氫釜閫楀彿锛屾瘡涓€楀彿浠ｈ〃涓€涓猲ull
                        let isFirstElisionComma = true
                        for (const elisionChild of elision.children || []) {
                            if (elisionChild.value === ',') {
                                const commaToken = SlimeTokenCreateUtils.createCommaToken(elisionChild.loc)
                                if (isFirstElisionComma && elements.length > 0 && !elements[elements.length - 1].commaToken) {
                                    elements[elements.length - 1].commaToken = commaToken
                                } else {
                                    elements.push({
                                        element: null,
                                        commaToken
                                    })
                                }
                                isFirstElisionComma = false
                            }
                        }
                    }

                    // 鐒跺悗妫€鏌ユ槸鍚︽湁BindingElement
                    const bindingElement = child.getChildren().find((ch: any) =>
                        ch.name === SlimeParser.prototype.BindingElement?.name)

                    if (bindingElement) {
                        // 浣跨敤 createBindingElementAst 姝ｇ‘澶勭悊 BindingElement锛堝寘锟?Initializer锟?
                        const element = SlimeCstToAstUtils.createBindingElementAst(bindingElement)
                        if (element) {
                            elements.push({ element })
                        }
                    }
                }
            }
        }

        // 澶勭悊 ArrayBindingPattern 鐩存帴瀛愯妭鐐逛腑锟?Comma 锟?Elision锛堝熬閮ㄧ┖浣嶏級
        // CST: [LBracket, BindingElementList, Comma, Elision, RBracket]
        for (let i = 0; i < cst.getChildren().length; i++) {
            const child = cst.getChildren()[i]
            // 璺宠繃 LBracket, RBracket, BindingElementList锛堝凡澶勭悊锟?
            if (child.getValue() === '[' || child.getValue() === ']' ||
                child.getName() === SlimeParser.prototype.BindingElementList?.name ||
                child.getName() === SlimeParser.prototype.BindingRestElement?.name) {
                continue
            }

            // 澶勭悊 BindingElementList 涔嬪悗锟?Comma
            if (child.getValue() === ',') {
                // 灏嗛€楀彿鍏宠仈鍒版渶鍚庝竴涓厓锟?
                if (elements.length > 0 && !elements[elements.length - 1].commaToken) {
                    elements[elements.length - 1].commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                }
            }

            // 澶勭悊灏鹃儴锟?Elision
            if (child.getName() === SlimeParser.prototype.Elision?.name || child.getName() === 'Elision') {
                let isFirstElisionComma = true
                for (const elisionChild of child.getChildren() || []) {
                    if (elisionChild.value === ',') {
                        const commaToken = SlimeTokenCreateUtils.createCommaToken(elisionChild.loc)
                        if (isFirstElisionComma && elements.length > 0 && !elements[elements.length - 1].commaToken) {
                            elements[elements.length - 1].commaToken = commaToken
                        } else {
                            elements.push({
                                element: null,
                                commaToken
                            })
                        }
                        isFirstElisionComma = false
                    }
                }
            }
        }

        // 妫€鏌ユ槸鍚︽湁BindingRestElement锟?..rest 锟?...[a, b]锟?
        const restElement = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.BindingRestElement?.name)
        if (restElement) {
            const restNode = SlimeCstToAstUtils.createBindingRestElementAst(restElement)
            elements.push({ element: restNode as any })
        }

        return {
            type: SlimeAstTypeName.ArrayPattern,
            elements,
            lBracketToken,
            rBracketToken,
            loc: cst.getLoc()
        } as SlimeArrayPattern
    }

    createObjectBindingPatternAst(cst: SubhutiCst): SlimeObjectPattern {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ObjectBindingPattern?.name)

        // CST缁撴瀯锛歔LBrace, BindingPropertyList?, RBrace]
        const properties: SlimeObjectPatternProperty[] = []

        // 鎻愬彇 LBrace 锟?RBrace tokens
        let lBraceToken: SlimeLBraceToken | undefined
        let rBraceToken: SlimeRBraceToken | undefined
        for (const child of cst.getChildren()) {
            if (child.getValue() === '{') {
                lBraceToken = SlimeTokenCreateUtils.createLBraceToken(child.getLoc())
            } else if (child.getValue() === '}') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(child.getLoc())
            }
        }

        // 鏌ユ壘BindingPropertyList
        const propList = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.BindingPropertyList?.name)
        if (propList) {
            // BindingPropertyList鍖呭惈BindingProperty鍜孋omma鑺傜偣
            for (let i = 0; i < propList.children.length; i++) {
                const child = propList.children[i]
                if (child.getValue() === ',') {
                    // 灏嗛€楀彿鍏宠仈鍒板墠涓€涓睘锟?
                    if (properties.length > 0 && !properties[properties.length - 1].commaToken) {
                        properties[properties.length - 1].commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                    }
                } else if (child.getName() === SlimeParser.prototype.BindingProperty?.name) {
                    // BindingProperty -> SingleNameBinding (绠€锟? 锟?PropertyName + BindingElement (瀹屾暣)
                    const singleName = child.getChildren().find((ch: any) =>
                        ch.name === SlimeParser.prototype.SingleNameBinding?.name)

                    if (singleName) {
                        // 绠€鍐欏舰寮忥細{name} 锟?{name = "Guest"}
                        const value = SlimeCstToAstUtils.createSingleNameBindingAst(singleName)
                        const identifier = singleName.children.find((ch: any) =>
                            ch.name === SlimeParser.prototype.BindingIdentifier?.name)
                        const key = SlimeCstToAstUtils.createBindingIdentifierAst(identifier)

                        properties.push({
                            property: {
                                type: SlimeAstTypeName.Property,
                                key: key,
                                value: value,
                                kind: 'init',
                                computed: false,
                                shorthand: true,
                                loc: child.getLoc()
                            } as SlimeAssignmentProperty
                        })
                    } else {
                        // 瀹屾暣褰㈠紡锛歿name: userName}
                        const propName = child.getChildren().find((ch: any) =>
                            ch.name === SlimeParser.prototype.PropertyName?.name)
                        const bindingElement = child.getChildren().find((ch: any) =>
                            ch.name === SlimeParser.prototype.BindingElement?.name)

                        if (propName && bindingElement) {
                            const key = SlimeCstToAstUtils.createPropertyNameAst(propName)
                            const value = SlimeCstToAstUtils.createBindingElementAst(bindingElement)
                            const isComputed = SlimeCstToAstUtils.isComputedPropertyName(propName)
                            const colonCst = child.getChildren().find((ch: any) => ch.name === 'Colon' || ch.value === ':')

                            properties.push({
                                property: {
                                    type: SlimeAstTypeName.Property,
                                    key: key,
                                    value: value,
                                    kind: 'init',
                                    computed: isComputed,
                                    shorthand: false,
                                    loc: child.getLoc(),
                                    colonToken: colonCst ? SlimeTokenCreateUtils.createColonToken(colonCst.loc) : undefined,
                                    lBracketToken: undefined,
                                    rBracketToken: undefined
                                } as SlimeAssignmentProperty
                            })

                            if (isComputed) {
                                const computedNameCst = (propName.name === 'ComputedPropertyName' || propName.name === SlimeParser.prototype.ComputedPropertyName?.name)
                                    ? propName
                                    : propName.children?.[0]
                                if (computedNameCst) {
                                    const lBracketCst = computedNameCst.children?.find((ch: any) => ch.name === 'LBracket' || ch.value === '[')
                                    const rBracketCst = computedNameCst.children?.find((ch: any) => ch.name === 'RBracket' || ch.value === ']')
                                    const prop = properties[properties.length - 1].property as any
                                    if (lBracketCst) {
                                        prop.lBracketToken = SlimeTokenCreateUtils.createLBracketToken(lBracketCst.loc)
                                    }
                                    if (rBracketCst) {
                                        prop.rBracketToken = SlimeTokenCreateUtils.createRBracketToken(rBracketCst.loc)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 妫€鏌ュ灞傛槸鍚︽湁閫楀彿锛堝湪 BindingPropertyList 涔嬪悗銆丅indingRestProperty 涔嬪墠锟?
        // CST 缁撴瀯: { BindingPropertyList , BindingRestProperty }
        // 閫楀彿锟?ObjectBindingPattern 鐨勭洿鎺ュ瓙鑺傜偣
        for (const child of cst.getChildren()) {
            if (child.getValue() === ',') {
                // 灏嗛€楀彿鍏宠仈鍒版渶鍚庝竴涓睘锟?
                if (properties.length > 0 && !properties[properties.length - 1].commaToken) {
                    properties[properties.length - 1].commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                }
            }
        }

        // ES2018: 妫€鏌ユ槸鍚︽湁BindingRestElement 锟?BindingRestProperty锟?..rest锟?
        const restElement = cst.getChildren().find(ch =>
            ch.name === SlimeParser.prototype.BindingRestElement?.name ||
            ch.name === 'BindingRestElement' ||
            ch.name === SlimeParser.prototype.BindingRestProperty?.name ||
            ch.name === 'BindingRestProperty'
        )
        if (restElement) {
            const identifier = restElement.children.find((ch: any) =>
                ch.name === SlimeParser.prototype.BindingIdentifier?.name ||
                ch.name === 'BindingIdentifier'
            )
            if (identifier) {
                const restId = SlimeCstToAstUtils.createBindingIdentifierAst(identifier)
                // 鎻愬彇 ellipsis token
                const ellipsisCst = restElement.children.find((ch: any) => ch.value === '...')
                const ellipsisToken = ellipsisCst ? SlimeTokenCreateUtils.createEllipsisToken(ellipsisCst.loc) : undefined
                const restNode: SlimeRestElement = {
                    type: SlimeAstTypeName.RestElement,
                    argument: restId,
                    ellipsisToken,
                    loc: restElement.loc
                }
                properties.push({ property: restNode })
            }
        }

        return {
            type: SlimeAstTypeName.ObjectPattern,
            properties,
            lBraceToken,
            rBraceToken,
            loc: cst.getLoc()
        } as SlimeObjectPattern
    }
}

export const SlimeBindingPatternCstToAst = new SlimeBindingPatternCstToAstSingle()
