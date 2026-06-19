import {
    SlimeAstCreateUtils,
    type SlimeBlockStatement,
    type SlimeExpression,
    type SlimeFunctionParam,
    SlimeAstTypeName,
    type SlimePattern,
    SlimeTokenCreateUtils,
    type SlimeArrayPattern,
    type SlimeArrayPatternElement,
    type SlimeObjectPattern,
    type SlimeObjectPatternProperty,
    type SlimeAssignmentProperty,
    type SlimeRestElement, SlimeStatement, SlimeIdentifier
} from "slime-ast";
import { SubhutiCst } from "subhuti";
import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";

import { SlimeTokenConsumer } from "../../SlimeTokenConsumer.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import {SlimeVariableCstToAstSingle} from "../statements/SlimeVariableCstToAst.ts";

export class SlimePatternConvertCstToAstSingle {
    /**
     * 锟?ArrayExpression AST 杞崲锟?ArrayPattern
     */
    convertArrayExpressionToPattern(expr: any): SlimeArrayPattern {
        const elements: SlimeArrayPatternElement[] = []
        for (const elem of expr.elements || []) {
            if (elem === null || elem.element === null) {
                elements.push({ element: null })
            } else {
                const element = elem.element || elem
                if (element && element.type === SlimeAstTypeName.SpreadElement) {
                    elements.push({
                        element: {
                            type: SlimeAstTypeName.RestElement,
                            argument: element.argument,
                            ellipsisToken: (element as any).ellipsisToken,
                            loc: element.loc
                        },
                        commaToken: elem.commaToken
                    })
                    continue
                }
                const pattern = SlimeCstToAstUtils.convertExpressionToPatternFromAST(element)
                elements.push({ element: pattern || element, commaToken: elem.commaToken })
            }
        }
        return {
            type: SlimeAstTypeName.ArrayPattern,
            elements,
            lBracketToken: expr.lBracketToken,
            rBracketToken: expr.rBracketToken,
            loc: expr.loc
        } as SlimeArrayPattern
    }

    /**
     * 灏嗚〃杈惧紡 CST 杞崲锟?Pattern锛堢敤锟?cover grammar锟?
     * 杩欑敤浜庡锟?async (expr) => body 涓殑 expr 锟?pattern 鐨勮浆锟?
     */
    /**
     * 锟?CST 琛ㄨ揪寮忚浆鎹负 Pattern锛堢敤锟?cover grammar锟?
     * 杩欑敤浜庡锟?async (expr) => body 涓殑 expr 锟?pattern 鐨勮浆锟?
     * 娉ㄦ剰锛氳繖涓柟娉曞锟?CST 鑺傜偣锛宑onvertExpressionToPattern 澶勭悊 AST 鑺傜偣
     */
    convertCstToPattern(cst: SubhutiCst): SlimePattern | null {
        // 棣栧厛妫€鏌ユ槸鍚︽槸 AssignmentExpression (榛樿鍙傛暟 options = {})
        // 杩欏繀椤诲湪 findInnerExpr 涔嬪墠澶勭悊锛屽惁鍒欎細涓㈠け = 鍜岄粯璁わ拷?
        if (cst.getName() === 'AssignmentExpression' || cst.getName() === SlimeParser.prototype.AssignmentExpression?.name) {
            // 妫€鏌ユ槸鍚︽湁 Assign token (=)
            const hasAssign = cst.getChildren()?.some(ch => ch.name === 'Assign' || ch.value === '=')
            if (hasAssign && cst.getChildren() && cst.getChildren().length >= 3) {
                // 杩欐槸榛樿鍙傛暟: left = right
                const expr = SlimeCstToAstUtils.createAssignmentExpressionAst(cst)
                if (expr.type === SlimeAstTypeName.AssignmentExpression) {
                    return SlimeCstToAstUtils.convertAssignmentExpressionToPattern(expr)
                }
            }
        }

        // 閫掑綊鏌ユ壘鏈€鍐呭眰鐨勮〃杈惧紡
        const findInnerExpr = (node: SubhutiCst): SubhutiCst => {
            if (!node.children || node.children.length === 0) return node
            // 濡傛灉锟?ObjectLiteral銆丄rrayLiteral銆両dentifier 绛夛紝杩斿洖锟?
            const first = node.children[0]
            if (first.getName() === 'ObjectLiteral' || first.getName() === 'ArrayLiteral' ||
                first.getName() === 'IdentifierReference' || first.getName() === 'Identifier' ||
                first.getName() === 'BindingIdentifier') {
                return first
            }
            // 鍚﹀垯閫掑綊鍚戜笅
            return findInnerExpr(first)
        }

        const inner = findInnerExpr(cst)

        if (inner.name === 'ObjectLiteral') {
            // 锟?ObjectLiteral 杞崲锟?ObjectPattern
            return SlimeCstToAstUtils.convertObjectLiteralToPattern(inner)
        } else if (inner.name === 'ArrayLiteral') {
            // 锟?ArrayLiteral 杞崲锟?ArrayPattern
            return SlimeCstToAstUtils.convertArrayLiteralToPattern(inner)
        } else if (inner.name === 'IdentifierReference' || inner.name === 'Identifier') {
            // 鏍囪瘑绗︾洿鎺ヨ浆锟?
            const idNode = inner.name === 'IdentifierReference' ? findInnerExpr(inner) : inner
            const identifierName = idNode.children?.[0]
            if (identifierName) {
                return SlimeAstCreateUtils.createIdentifier(identifierName.getValue(), identifierName.getLoc())
            }
        } else if (inner.name === 'BindingIdentifier') {
            return SlimeCstToAstUtils.createBindingIdentifierAst(inner)
        }

        // 灏濊瘯灏嗚〃杈惧紡浣滀负 AST 澶勭悊
        const expr = SlimeCstToAstUtils.createExpressionAst(cst)
        if (expr.type === SlimeAstTypeName.Identifier) {
            return expr as any
        } else if (expr.type === SlimeAstTypeName.ObjectExpression) {
            // ObjectExpression 闇€瑕佽浆鎹负 ObjectPattern
            return SlimeCstToAstUtils.convertObjectExpressionToPattern(expr)
        } else if (expr.type === SlimeAstTypeName.ArrayExpression) {
            // ArrayExpression 闇€瑕佽浆鎹负 ArrayPattern
            return SlimeCstToAstUtils.convertArrayExpressionToPattern(expr)
        } else if (expr.type === SlimeAstTypeName.AssignmentExpression) {
            // AssignmentExpression 杞崲锟?AssignmentPattern
            return SlimeCstToAstUtils.convertAssignmentExpressionToPattern(expr)
        }

        // 濡傛灉浠嶇劧鏃犳硶杞崲锛岃繑锟?null锛堜笉瑕佽繑鍥炲師锟?CST锟?
        return null
    }

    /**
     * Cover 璇硶涓嬶紝灏嗗崟涓弬鏁扮浉鍏崇殑 CST 鑺傜偣杞崲锟?Pattern
     * 浠呭湪鈥滃弬鏁颁綅缃€濊皟鐢紝鐢ㄤ簬 Arrow / AsyncArrow 绛夊満锟?
     */
    convertCoverParameterCstToPattern(cst: SubhutiCst, hasEllipsis: boolean): SlimePattern | null {
        let basePattern: SlimePattern | null = null

        // 1. 宸茬粡锟?BindingIdentifier / BindingPattern 绯诲垪鐨勶紝鐩存帴璧扮粦瀹氭ā寮忓熀纭€鏂规硶
        if (cst.getName() === SlimeParser.prototype.BindingIdentifier?.name || cst.getName() === 'BindingIdentifier') {
            basePattern = SlimeCstToAstUtils.createBindingIdentifierAst(cst)
        } else if (cst.getName() === SlimeParser.prototype.BindingPattern?.name || cst.getName() === 'BindingPattern') {
            basePattern = SlimeCstToAstUtils.createBindingPatternAst(cst)
        } else if (cst.getName() === SlimeParser.prototype.ArrayBindingPattern?.name || cst.getName() === 'ArrayBindingPattern') {
            basePattern = SlimeCstToAstUtils.createArrayBindingPatternAst(cst)
        } else if (cst.getName() === SlimeParser.prototype.ObjectBindingPattern?.name || cst.getName() === 'ObjectBindingPattern') {
            basePattern = SlimeCstToAstUtils.createObjectBindingPatternAst(cst)
        }

        // 2. 鍏跺畠鎯呭喌锛圓ssignmentExpression / ObjectLiteral / ArrayLiteral 绛夛級锛屼娇鐢ㄩ€氱敤锟?CST鈫扨attern 閫昏緫
        if (!basePattern) {
            basePattern = SlimeCstToAstUtils.convertCstToPattern(cst)
        }

        // 3. 鍏煎鍏滃簳锛氫粛鐒舵棤娉曡浆鎹㈡椂锛屽皾璇曚粠琛ㄨ揪寮忎腑鎻愬彇绗竴锟?Identifier
        if (!basePattern) {
            const identifierCst = SlimeCstToAstUtils.findFirstIdentifierInExpression(cst)
            if (identifierCst) {
                basePattern = SlimeCstToAstUtils.createIdentifierAst(identifierCst) as any
            }
        }

        if (!basePattern) return null

        // 4. 澶勭悊 rest 鍙傛暟锛氭牴鎹皟鐢ㄦ柟浼犲叆锟?hasEllipsis 鍐冲畾鏄惁鍖呰锟?RestElement
        if (hasEllipsis) {
            return SlimeAstCreateUtils.createRestElement(basePattern)
        }

        return basePattern
    }


    /**
     * 锟?ObjectLiteral CST 杞崲锟?ObjectPattern
     */
    convertObjectLiteralToPattern(cst: SubhutiCst): SlimeObjectPattern {
        const properties: SlimeObjectPatternProperty[] = []
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        for (const child of cst.getChildren() || []) {
            if (child.getValue() === '{') {
                lBraceToken = SlimeTokenCreateUtils.createLBraceToken(child.getLoc())
            } else if (child.getValue() === '}') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(child.getLoc())
            } else if (child.getName() === 'PropertyDefinitionList') {
                for (const prop of child.getChildren() || []) {
                    if (prop.value === ',') {
                        // 灏嗛€楀彿鍏宠仈鍒板墠涓€涓睘锟?
                        if (properties.length > 0 && !properties[properties.length - 1].commaToken) {
                            properties[properties.length - 1].commaToken = SlimeTokenCreateUtils.createCommaToken(prop.loc)
                        }
                        continue
                    }
                    if (prop.name === 'PropertyDefinition') {
                        // 妫€鏌ユ槸鍚︽槸 SpreadElement (... identifier)
                        const ellipsis = prop.children?.find((c: any) => c.value === '...' || c.name === 'Ellipsis')
                        if (ellipsis) {
                            // 杩欐槸涓€锟?RestElement
                            const assignExpr = prop.children?.find((c: any) => c.name === 'AssignmentExpression')
                            if (assignExpr) {
                                // 锟?AssignmentExpression 涓彁锟?identifier
                                const idCst = SlimeCstToAstUtils.findFirstIdentifierInExpression(assignExpr)
                                if (idCst) {
                                    const restId = SlimeCstToAstUtils.createIdentifierAst(idCst)
                                    const restNode: SlimeRestElement = {
                                        type: SlimeAstTypeName.RestElement,
                                        argument: restId,
                                        ellipsisToken: SlimeTokenCreateUtils.createEllipsisToken(ellipsis.loc),
                                        loc: prop.loc
                                    }
                                    properties.push({ property: restNode })
                                }
                            }
                        } else {
                            const patternProp = SlimeCstToAstUtils.convertPropertyDefinitionToPatternProperty(prop)
                            if (patternProp) {
                                properties.push({ property: patternProp })
                            }
                        }
                    }
                }
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

    /**
     * 锟?PropertyDefinition CST 杞崲锟?Pattern 灞烇拷?
     */
    convertPropertyDefinitionToPatternProperty(cst: SubhutiCst): SlimeAssignmentProperty | null {
        const first = cst.getChildren()?.[0]
        if (!first) return null

        if (first.getName() === 'IdentifierReference') {
            // 绠€鍐欏舰锟? { id } -> { id: id }
            const idNode = first.getChildren()?.[0]?.children?.[0]
            if (idNode) {
                const id = SlimeAstCreateUtils.createIdentifier(idNode.value, idNode.loc)
                return {
                    type: SlimeAstTypeName.Property,
                    key: id,
                    value: id,
                    kind: 'init',
                    computed: false,
                    shorthand: true,
                    loc: cst.getLoc()
                } as SlimeAssignmentProperty
            }
        } else if (first.getName() === 'CoverInitializedName') {
            // 甯﹂粯璁ゅ€肩殑绠€鍐欏舰锟? { id = value }
            const idRef = first.getChildren()?.find((c: any) => c.name === 'IdentifierReference')
            const initializer = first.getChildren()?.find((c: any) => c.name === 'Initializer')
            if (idRef) {
                const idNode = idRef.children?.[0]?.children?.[0]
                if (idNode) {
                    const id = SlimeAstCreateUtils.createIdentifier(idNode.value, idNode.loc)
                    let value: any = id
                    if (initializer) {
                        const init = SlimeCstToAstUtils.createInitializerAst(initializer)
                        const assignCst = initializer.children?.[0]
                        const equalToken = assignCst ? SlimeTokenCreateUtils.createAssignToken(assignCst.getLoc()) : undefined
                        value = {
                            type: SlimeAstTypeName.AssignmentPattern,
                            left: id,
                            right: init,
                            equalToken,
                            loc: first.getLoc()
                        }
                    }
                    return {
                        type: SlimeAstTypeName.Property,
                        key: id,
                        value: value,
                        kind: 'init',
                        computed: false,
                        shorthand: true,
                        loc: cst.getLoc()
                    } as SlimeAssignmentProperty
                }
            }
        } else if (first.getName() === 'PropertyName') {
            // 瀹屾暣褰㈠紡: { key: value }
            const propName = first
            const colonCst = cst.getChildren()?.find((c: any) => c.name === 'Colon' || c.value === ':')
            const valueCst = cst.getChildren()?.[2]
            if (colonCst && valueCst) {
                const key = SlimeCstToAstUtils.createPropertyNameAst(propName)
                const valueExpr = SlimeCstToAstUtils.createExpressionAst(valueCst)
                const value = SlimeCstToAstUtils.convertExpressionToPatternFromAST(valueExpr)
                const computed = SlimeCstToAstUtils.isComputedPropertyName(propName)
                const prop: any = {
                    type: SlimeAstTypeName.Property,
                    key: key,
                    value: value || valueExpr,
                    kind: 'init',
                    computed: computed,
                    shorthand: false,
                    colonToken: SlimeTokenCreateUtils.createColonToken(colonCst.loc),
                    loc: cst.getLoc()
                } as SlimeAssignmentProperty
                if (computed) {
                    const computedNameCst = (propName.name === 'ComputedPropertyName' || propName.name === SlimeParser.prototype.ComputedPropertyName?.name)
                        ? propName
                        : propName.children?.[0]
                    if (computedNameCst) {
                        const lBracketCst = computedNameCst.children?.find((ch: any) => ch.name === 'LBracket' || ch.value === '[')
                        const rBracketCst = computedNameCst.children?.find((ch: any) => ch.name === 'RBracket' || ch.value === ']')
                        if (lBracketCst) {
                            prop.lBracketToken = SlimeTokenCreateUtils.createLBracketToken(lBracketCst.loc)
                        }
                        if (rBracketCst) {
                            prop.rBracketToken = SlimeTokenCreateUtils.createRBracketToken(rBracketCst.loc)
                        }
                    }
                }
                return prop
            }
        }

        return null
    }

    /**
     * 锟?ObjectExpression AST 杞崲锟?ObjectPattern
     */
    convertObjectExpressionToPattern(expr: any): SlimeObjectPattern {
        const properties: SlimeObjectPatternProperty[] = []
        for (const prop of expr.properties || []) {
            const property = prop.property || prop
            if (property.type === SlimeAstTypeName.SpreadElement) {
                properties.push({
                    property: {
                        type: SlimeAstTypeName.RestElement,
                        argument: property.argument,
                        ellipsisToken: (property as any).ellipsisToken,
                        loc: property.loc
                    } as SlimeRestElement
                })
            } else {
                const value = SlimeCstToAstUtils.convertExpressionToPatternFromAST(property.value)
                properties.push({
                    property: {
                        type: SlimeAstTypeName.Property,
                        key: property.key,
                        value: value || property.value,
                        kind: 'init',
                        computed: property.computed,
                        shorthand: property.shorthand,
                        colonToken: (property as any).colonToken,
                        lBracketToken: (property as any).lBracketToken,
                        rBracketToken: (property as any).rBracketToken,
                        loc: property.loc
                    } as SlimeAssignmentProperty
                })
            }
        }
        return {
            type: SlimeAstTypeName.ObjectPattern,
            properties,
            lBraceToken: expr.lBraceToken,
            rBraceToken: expr.rBraceToken,
            loc: expr.loc
        } as SlimeObjectPattern
    }



    /**
     * 锟?AssignmentExpression AST 杞崲锟?AssignmentPattern
     */
    convertAssignmentExpressionToPattern(expr: any): any {
        const left = SlimeCstToAstUtils.convertExpressionToPatternFromAST(expr.left)
        const operatorLoc = (expr.operator && typeof expr.operator === 'object') ? expr.operator.loc : undefined
        const equalToken = operatorLoc ? SlimeTokenCreateUtils.createAssignToken(operatorLoc) : undefined
        return {
            type: SlimeAstTypeName.AssignmentPattern,
            left: left || expr.left,
            right: expr.right,
            equalToken,
            loc: expr.loc
        }
    }

    /**
     * 灏嗚〃杈惧紡 AST 杞崲锟?Pattern
     */
    convertExpressionToPatternFromAST(expr: any): SlimePattern | null {
        if (!expr) return null
        if (expr.type === SlimeAstTypeName.Identifier) {
            return expr
        } else if (expr.type === SlimeAstTypeName.ObjectExpression) {
            return SlimeCstToAstUtils.convertObjectExpressionToPattern(expr)
        } else if (expr.type === SlimeAstTypeName.ArrayExpression) {
            return SlimeCstToAstUtils.convertArrayExpressionToPattern(expr)
        } else if (expr.type === SlimeAstTypeName.AssignmentExpression) {
            return SlimeCstToAstUtils.convertAssignmentExpressionToPattern(expr)
        }
        return null
    }

    /**
     * 锟?ArrayLiteral CST 杞崲锟?ArrayPattern
     */
    convertArrayLiteralToPattern(cst: SubhutiCst): SlimeArrayPattern {
        // 绠€鍖栧疄鐜帮細浣跨敤 createArrayBindingPatternAst 鐨勯€昏緫
        const elements: SlimeArrayPatternElement[] = []
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined

        // 杈呭姪鍑芥暟锛氬锟?Elision 鑺傜偣
        const processElision = (elisionNode: SubhutiCst) => {
            let isFirstElisionComma = true
            for (const elisionChild of elisionNode.children || []) {
                if (elisionChild.value === ',') {
                    const commaToken = SlimeTokenCreateUtils.createCommaToken(elisionChild.loc)
                    if (isFirstElisionComma && elements.length > 0 && !elements[elements.length - 1].commaToken) {
                        elements[elements.length - 1].commaToken = commaToken
                    } else {
                        elements.push({ element: null, commaToken })
                    }
                    isFirstElisionComma = false
                }
            }
        }

        for (const child of cst.getChildren() || []) {
            if (child.getValue() === '[') {
                lBracketToken = SlimeTokenCreateUtils.createLBracketToken(child.getLoc())
            } else if (child.getValue() === ']') {
                rBracketToken = SlimeTokenCreateUtils.createRBracketToken(child.getLoc())
            } else if (child.getName() === 'Elision') {
                // 鐩存帴锟?ArrayLiteral 涓嬬殑 Elision锛堝 [,,]锟?
                processElision(child)
            } else if (child.getName() === 'ElementList') {
                const elemChildren = child.getChildren() || []
                for (let i = 0; i < elemChildren.length; i++) {
                    const elem = elemChildren[i]
                    if (elem.value === ',') {
                        // 灏嗛€楀彿鍏宠仈鍒板墠涓€涓厓锟?
                        if (elements.length > 0 && !elements[elements.length - 1].commaToken) {
                            elements[elements.length - 1].commaToken = SlimeTokenCreateUtils.createCommaToken(elem.loc)
                        }
                    } else if (elem.name === 'Elision') {
                        // ElementList 鍐呯殑 Elision
                        processElision(elem)
                    } else if (elem.name === 'AssignmentExpression') {
                        const expr = SlimeCstToAstUtils.createExpressionAst(elem)
                        const pattern = SlimeCstToAstUtils.convertExpressionToPatternFromAST(expr)
                    elements.push({ element: pattern || expr as any })
                } else if (elem.name === 'SpreadElement') {
                    const restNode = SlimeCstToAstUtils.createSpreadElementAst(elem)
                    elements.push({
                        element: {
                            type: SlimeAstTypeName.RestElement,
                            argument: restNode.argument,
                            ellipsisToken: (restNode as any).ellipsisToken,
                            loc: restNode.loc
                        } as SlimeRestElement
                    })
                }
            }
            }
        }

        return {
            type: SlimeAstTypeName.ArrayPattern,
            elements,
            lBracketToken,
            rBracketToken,
            loc: cst.getLoc()
        } as SlimeArrayPattern
    }

    /**
     * 灏嗚〃杈惧紡杞崲涓烘ā寮忥紙鐢ㄤ簬绠ご鍑芥暟鍙傛暟瑙ｆ瀯锟?
     * ObjectExpression -> ObjectPattern
     * ArrayExpression -> ArrayPattern
     * Identifier -> Identifier
     * SpreadElement -> RestElement
     */
    convertExpressionToPattern(expr: any): SlimePattern {
        if (!expr) return expr

        if (expr.type === SlimeAstTypeName.Identifier) {
            return expr
        }

        if (expr.type === SlimeAstTypeName.ObjectExpression) {
            // 锟?ObjectExpression 杞崲锟?ObjectPattern
            const properties: any[] = []
            for (const item of expr.properties || []) {
                const prop = item.property !== undefined ? item.property : item
                if (prop.type === SlimeAstTypeName.SpreadElement) {
                    // SpreadElement -> RestElement
                    properties.push({
                        property: {
                            type: SlimeAstTypeName.RestElement,
                            argument: SlimeCstToAstUtils.convertExpressionToPattern(prop.argument),
                            ellipsisToken: (prop as any).ellipsisToken,
                            loc: prop.loc
                        },
                        commaToken: item.commaToken
                    })
                } else if (prop.type === SlimeAstTypeName.Property) {
                    // 杞崲 Property 锟?value
                    const convertedValue = SlimeCstToAstUtils.convertExpressionToPattern(prop.value)
                    properties.push({
                        property: {
                            ...prop,
                            value: convertedValue
                        },
                        commaToken: item.commaToken
                    })
                } else {
                    properties.push(item)
                }
            }
            return {
                type: SlimeAstTypeName.ObjectPattern,
                properties: properties,
                loc: expr.loc,
                lBraceToken: expr.lBraceToken,
                rBraceToken: expr.rBraceToken
            } as any
        }

        if (expr.type === SlimeAstTypeName.ArrayExpression) {
            // 锟?ArrayExpression 杞崲锟?ArrayPattern
            const elements: any[] = []
            for (const item of expr.elements || []) {
                const elem = item.element !== undefined ? item.element : item
                if (elem === null) {
                    elements.push(item)
                } else if (elem.type === SlimeAstTypeName.SpreadElement) {
                    // SpreadElement -> RestElement
                    elements.push({
                        element: {
                            type: SlimeAstTypeName.RestElement,
                            argument: SlimeCstToAstUtils.convertExpressionToPattern(elem.argument),
                            ellipsisToken: (elem as any).ellipsisToken,
                            loc: elem.loc
                        },
                        commaToken: item.commaToken
                    })
                } else {
                    elements.push({
                        element: SlimeCstToAstUtils.convertExpressionToPattern(elem),
                        commaToken: item.commaToken
                    })
                }
            }
            return {
                type: SlimeAstTypeName.ArrayPattern,
                elements: elements,
                loc: expr.loc,
                lBracketToken: expr.lBracketToken,
                rBracketToken: expr.rBracketToken
            } as any
        }

        if (expr.type === SlimeAstTypeName.AssignmentExpression) {
            const operatorLoc = (expr.operator && typeof expr.operator === 'object') ? expr.operator.loc : undefined
            const equalToken = operatorLoc ? SlimeTokenCreateUtils.createAssignToken(operatorLoc) : undefined
            // 锟?AssignmentExpression 杞崲锟?AssignmentPattern
            return {
                type: SlimeAstTypeName.AssignmentPattern,
                left: SlimeCstToAstUtils.convertExpressionToPattern(expr.left),
                right: expr.right,
                equalToken,
                loc: expr.loc
            } as any
        }

        if (expr.type === SlimeAstTypeName.SpreadElement) {
            // SpreadElement -> RestElement
            return {
                type: SlimeAstTypeName.RestElement,
                argument: SlimeCstToAstUtils.convertExpressionToPattern(expr.argument),
                ellipsisToken: (expr as any).ellipsisToken,
                loc: expr.loc
            } as any
        }

        // 鍏朵粬绫诲瀷鐩存帴杩斿洖
        return expr
    }


    createBindingRestElementAst(cst: SubhutiCst): SlimeRestElement {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.BindingRestElement?.name);
        // BindingRestElement: ... BindingIdentifier | ... BindingPattern
        const ellipsisCst = cst.getChildren()[0]
        const argumentCst = cst.getChildren()[1]
        const ellipsisToken = (ellipsisCst && (ellipsisCst.name === 'Ellipsis' || ellipsisCst.value === '...'))
            ? SlimeTokenCreateUtils.createEllipsisToken(ellipsisCst.loc)
            : undefined

        let argument: SlimeIdentifier | SlimePattern

        if (argumentCst.name === SlimeParser.prototype.BindingIdentifier?.name) {
            // 绠€鍗曟儏鍐碉細...rest
            argument = SlimeCstToAstUtils.createBindingIdentifierAst(argumentCst)
        } else if (argumentCst.name === SlimeParser.prototype.BindingPattern?.name) {
            // 宓屽瑙ｆ瀯锟?..[a, b] 锟?...{x, y}
            argument = SlimeCstToAstUtils.createBindingPatternAst(argumentCst)
        } else {
            throw new Error(`BindingRestElement: 涓嶆敮鎸佺殑绫诲瀷 ${argumentCst.name}`)
        }

        return SlimeAstCreateUtils.createRestElement(argument, cst.getLoc(), ellipsisToken)
    }
}


export const SlimePatternConvertCstToAst = new SlimePatternConvertCstToAstSingle()
