/**
 * CompoundLiteralCstToAst - 鏁扮粍/瀵硅薄瀛楅潰閲忚浆鎹?
 */
import {SubhutiCst} from "subhuti";
import {
    type SlimeArrayElement,
    type SlimeArrayExpression, type SlimeArrowFunctionExpression,
    type SlimeAssignmentExpression,

    type SlimeClassBody,
    type SlimeExpression, type SlimeFunctionParam,
    SlimeIdentifier,
    SlimeLiteral,
    type SlimeMethodDefinition, SlimeAstTypeName,
    type SlimeObjectExpression,
    type SlimeObjectPropertyItem, SlimeProperty,
    type SlimePropertyDefinition,
    type SlimeSpreadElement,
    type SlimeStatement,
    SlimeTokenCreateUtils, SlimeAstCreateUtils
} from "slime-ast";
import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";

import { SlimeTokenConsumer } from "../../SlimeTokenConsumer.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import {SlimeVariableCstToAstSingle} from "../statements/SlimeVariableCstToAst.ts";

export class SlimeCompoundLiteralCstToAstSingle {
    /**
     * 瀵硅薄瀛楅潰锟?CST 锟?AST锛堥€忎紶锟?ObjectExpression锟?
     * ObjectLiteral -> { } | { PropertyDefinitionList } | { PropertyDefinitionList , }
     */
    createObjectLiteralAst(cst: SubhutiCst): SlimeObjectExpression {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ObjectLiteral?.name);
        const properties: Array<SlimeObjectPropertyItem> = []

        // 鎻愬彇 LBrace 锟?RBrace tokens
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        // ObjectLiteral: { PropertyDefinitionList? ,? }
        // children[0] = LBrace, children[last] = RBrace (if exists)
        if (cst.getChildren() && cst.getChildren().length > 0) {
            const firstChild = cst.getChildren()[0]
            if (firstChild && (firstChild.getName() === 'LBrace' || firstChild.getValue() === '{')) {
                lBraceToken = SlimeTokenCreateUtils.createLBraceToken(firstChild.getLoc())
            }

            const lastChild = cst.getChildren()[cst.getChildren().length - 1]
            if (lastChild && (lastChild.name === 'RBrace' || lastChild.value === '}')) {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(lastChild.loc)
            }
        }

        if (cst.getChildren().length > 2) {
            const PropertyDefinitionListCst = cst.getChildren()[1]
            let currentProperty: SlimeProperty | SlimeSpreadElement | null = null
            let hasProperty = false

            for (const child of PropertyDefinitionListCst.children) {
                // 璺宠繃娌℃湁children鐨凱ropertyDefinition鑺傜偣锛圫ubhutiParser浼樺寲瀵艰嚧锟?
                if (child.getName() === SlimeParser.prototype.PropertyDefinition?.name && child.getChildren() && child.getChildren().length > 0) {
                    // 濡傛灉涔嬪墠鏈夊睘鎬т絾娌℃湁閫楀彿锛屽厛鎺ㄥ叆
                    if (hasProperty) {
                        properties.push(SlimeAstCreateUtils.createObjectPropertyItem(currentProperty!, undefined))
                    }
                    currentProperty = SlimeCstToAstUtils.createPropertyDefinitionAst(child)
                    hasProperty = true
                } else if (child.getName() === 'Comma' || child.getValue() === ',') {
                    // 閫楀彿涓庡墠闈㈢殑灞炴€ч厤锟?
                    const commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                    if (hasProperty) {
                        properties.push(SlimeAstCreateUtils.createObjectPropertyItem(currentProperty!, commaToken))
                        hasProperty = false
                        currentProperty = null
                    }
                }
            }

            // 澶勭悊鏈€鍚庝竴涓睘鎬э紙濡傛灉娌℃湁灏鹃殢閫楀彿锟?
            if (hasProperty) {
                properties.push(SlimeAstCreateUtils.createObjectPropertyItem(currentProperty!, undefined))
            }

            const trailingCommaCst = cst.getChildren().find((child, idx) =>
                idx > 0 &&
                idx < cst.getChildren().length - 1 &&
                (child.getName() === 'Comma' || child.getValue() === ',')
            )
            if (trailingCommaCst && properties.length > 0) {
                const lastProperty = properties[properties.length - 1] as any
                if (!lastProperty.commaToken) {
                    lastProperty.commaToken = SlimeTokenCreateUtils.createCommaToken(trailingCommaCst.getLoc())
                }
            }
        }
        return SlimeAstCreateUtils.createObjectExpression(properties, cst.getLoc(), lBraceToken, rBraceToken)
    }


    /**
     * ArrayLiteral CST 锟?ArrayExpression AST
     * ArrayLiteral -> [ Elision? ] | [ ElementList ] | [ ElementList , Elision? ]
     */
    createArrayLiteralAst(cst: SubhutiCst): SlimeArrayExpression {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ArrayLiteral?.name);
        // ArrayLiteral: [LBracket, ElementList?, Comma?, Elision?, RBracket]

        // 鎻愬彇 LBracket 锟?RBracket tokens
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined

        if (cst.getChildren() && cst.getChildren().length > 0) {
            const firstChild = cst.getChildren()[0]
            if (firstChild && (firstChild.getName() === 'LBracket' || firstChild.getValue() === '[')) {
                lBracketToken = SlimeTokenCreateUtils.createLBracketToken(firstChild.getLoc())
            }

            const lastChild = cst.getChildren()[cst.getChildren().length - 1]
            if (lastChild && (lastChild.name === 'RBracket' || lastChild.value === ']')) {
                rBracketToken = SlimeTokenCreateUtils.createRBracketToken(lastChild.loc)
            }
        }

        const elementList = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.ElementList?.name)
        const elements = elementList ? SlimeCstToAstUtils.createElementListAst(elementList) : []

        // 澶勭悊 ArrayLiteral 椤跺眰锟?Comma 锟?Elision锛堝熬闅忛€楀彿鍜岀渷鐣ワ級
        // 渚嬪 [x,,] -> ElementList 鍚庨潰锟?Comma 锟?Elision
        const appendTrailingComma = (commaLoc: any) => {
            const commaToken = SlimeTokenCreateUtils.createCommaToken(commaLoc)
            if (elements.length > 0 && !elements[elements.length - 1].commaToken) {
                elements[elements.length - 1].commaToken = commaToken
            } else {
                elements.push(SlimeAstCreateUtils.createArrayElement(null, commaToken))
            }
        }

        for (const child of cst.getChildren()) {
            if (child.getName() === 'Comma' || child.getValue() === ',') {
                // 椤跺眰閫楀彿锛岃〃绀哄熬闅忛€楀彿
                appendTrailingComma(child.getLoc())
            } else if (child.getName() === SlimeParser.prototype.Elision?.name || child.getName() === 'Elision') {
                // 椤跺眰 Elision锛屾坊鍔犵┖鍏冪礌
                const elisionCommas = child.getChildren()?.filter((c: any) => c.name === 'Comma' || c.value === ',') || []
                for (let j = 0; j < elisionCommas.length; j++) {
                    appendTrailingComma(elisionCommas[j].loc)
                }
            }
        }

        return SlimeAstCreateUtils.createArrayExpression(elements, cst.getLoc(), lBracketToken, rBracketToken)
    }


    createSpreadElementAst(cst: SubhutiCst): SlimeSpreadElement {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.SpreadElement?.name);
        // SpreadElement: [Ellipsis, AssignmentExpression]

        // 鎻愬彇 Ellipsis token
        let ellipsisToken: any = undefined
        const ellipsisCst = cst.getChildren().find(ch =>
            ch.name === 'Ellipsis' || ch.name === 'Ellipsis' || ch.value === '...'
        )
        if (ellipsisCst) {
            ellipsisToken = SlimeTokenCreateUtils.createEllipsisToken(ellipsisCst.loc)
        }

        const expression = cst.getChildren().find(ch =>
            ch.name === SlimeParser.prototype.AssignmentExpression?.name
        )
        if (!expression) {
            throw new Error('SpreadElement missing AssignmentExpression')
        }

        return SlimeAstCreateUtils.createSpreadElement(
            SlimeCstToAstUtils.createAssignmentExpressionAst(expression),
            cst.getLoc(),
            ellipsisToken
        )
    }


    createPropertyDefinitionAst(cst: SubhutiCst): SlimeProperty {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.PropertyDefinition?.name);

        // 闃插尽鎬ф鏌ワ細濡傛灉 children 涓虹┖锛岃鏄庢槸绌哄璞＄殑鎯呭喌锛屼笉搴旇琚皟锟?
        // 杩欑鎯呭喌閫氬父涓嶄細鍙戠敓锛屽洜涓虹┖瀵硅薄{}涓嶄細鏈塒ropertyDefinition鑺傜偣
        if (!cst.getChildren() || cst.getChildren().length === 0) {
            throw new Error('PropertyDefinition CST has no children - this should not happen for valid syntax');
        }

        const first = cst.getChildren()[0]

        // ES2018: 瀵硅薄spread {...obj}
        // 妫€鏌irst鏄惁鏄疎llipsis token锛坣ame锟?Ellipsis'锟?
        if (first.getName() === 'Ellipsis' || first.getValue() === '...') {
            // PropertyDefinition -> Ellipsis + AssignmentExpression
            const AssignmentExpressionCst = cst.getChildren()[1]
            const argument = SlimeCstToAstUtils.createAssignmentExpressionAst(AssignmentExpressionCst)

            // 杩斿洖 SpreadElement锛堜綔涓?Property 鐨勪竴绉嶇壒娈婂舰寮忥級
            const ellipsisToken = SlimeTokenCreateUtils.createEllipsisToken(first.getLoc())
            return SlimeAstCreateUtils.createSpreadElement(argument, cst.getLoc(), ellipsisToken) as any
        } else if (cst.getChildren().length > 2) {
            // PropertyName : AssignmentExpression锛堝畬鏁村舰寮忥級
            const PropertyNameCst = cst.getChildren()[0]
            const colonCst = cst.getChildren()[1]
            const AssignmentExpressionCst = cst.getChildren()[2]

            const key = SlimeCstToAstUtils.createPropertyNameAst(PropertyNameCst)
            const value = SlimeCstToAstUtils.createAssignmentExpressionAst(AssignmentExpressionCst)
            const colonToken = (colonCst && (colonCst.name === 'Colon' || colonCst.value === ':'))
                ? SlimeTokenCreateUtils.createColonToken(colonCst.loc)
                : undefined

            const keyAst = SlimeAstCreateUtils.createPropertyAst(key, value)
            if (colonToken) {
                keyAst.colonToken = colonToken
            }

            // 妫€鏌ユ槸鍚︽槸璁＄畻灞炴€у悕
            const computedNameCst = (PropertyNameCst.name === 'ComputedPropertyName' || PropertyNameCst.name === SlimeParser.prototype.ComputedPropertyName?.name)
                ? PropertyNameCst
                : PropertyNameCst.children?.[0]
            if (computedNameCst &&
                (computedNameCst.name === 'ComputedPropertyName' || computedNameCst.name === SlimeParser.prototype.ComputedPropertyName?.name)) {
                keyAst.computed = true
                const lBracketCst = computedNameCst.children?.find((ch: any) => ch.name === 'LBracket' || ch.value === '[')
                const rBracketCst = computedNameCst.children?.find((ch: any) => ch.name === 'RBracket' || ch.value === ']')
                if (lBracketCst) {
                    keyAst.lBracketToken = SlimeTokenCreateUtils.createLBracketToken(lBracketCst.loc)
                }
                if (rBracketCst) {
                    keyAst.rBracketToken = SlimeTokenCreateUtils.createRBracketToken(rBracketCst.loc)
                }
            }

            return keyAst
        } else if (first.getName() === SlimeParser.prototype.MethodDefinition?.name) {
            // 鏂规硶瀹氫箟锛堝璞′腑鐨勬柟娉曟病鏈塻tatic锟?
            const SlimeMethodDefinition = SlimeCstToAstUtils.createMethodDefinitionAst(null, first)

            const keyAst = SlimeAstCreateUtils.createPropertyAst(SlimeMethodDefinition.key, SlimeMethodDefinition.value)

            // 缁ф壙MethodDefinition鐨刢omputed鏍囧織
            if (SlimeMethodDefinition.computed) {
                keyAst.computed = true
            }
            if ((SlimeMethodDefinition as any).lBracketToken) {
                keyAst.lBracketToken = (SlimeMethodDefinition as any).lBracketToken
            }
            if ((SlimeMethodDefinition as any).rBracketToken) {
                keyAst.rBracketToken = (SlimeMethodDefinition as any).rBracketToken
            }

            if (SlimeMethodDefinition.getToken) {
                keyAst.getToken = SlimeMethodDefinition.getToken
            }
            if (SlimeMethodDefinition.setToken) {
                keyAst.setToken = SlimeMethodDefinition.setToken
            }
            if (SlimeMethodDefinition.asyncToken) {
                keyAst.asyncToken = SlimeMethodDefinition.asyncToken
            }
            if (SlimeMethodDefinition.asteriskToken) {
                keyAst.asteriskToken = SlimeMethodDefinition.asteriskToken
            }

            // 缁ф壙MethodDefinition鐨刱ind鏍囧織锛坓etter/setter/method锟?
            if (SlimeMethodDefinition.kind === 'get' || SlimeMethodDefinition.kind === 'set') {
                keyAst.kind = SlimeMethodDefinition.kind
            } else {
                // 鏅€氭柟娉曚娇锟?method: true
                keyAst.method = true
            }

            return keyAst
        } else if (first.getName() === SlimeParser.prototype.IdentifierReference?.name) {
            // 灞炴€х畝锟?{name} -> {name: name}
            const identifierCst = first.getChildren()[0] // IdentifierReference -> Identifier
            const identifier = SlimeCstToAstUtils.createIdentifierAst(identifierCst)
            const keyAst = SlimeAstCreateUtils.createPropertyAst(identifier, identifier)
            keyAst.shorthand = true
            return keyAst
        } else if (first.getName() === 'CoverInitializedName') {
            // CoverInitializedName: 甯﹂粯璁ゅ€肩殑灞炴€х畝锟?{name = 'default'}
            // CoverInitializedName -> IdentifierReference + Initializer
            const identifierRefCst = first.getChildren()[0]
            const initializerCst = first.getChildren()[1]

            const identifierCst = identifierRefCst.children[0] // IdentifierReference -> Identifier
            const identifier = SlimeCstToAstUtils.createIdentifierAst(identifierCst)

            // Initializer -> Assign + AssignmentExpression
            const assignCst = initializerCst.children[0]
            const defaultValue = SlimeCstToAstUtils.createAssignmentExpressionAst(initializerCst.children[1])
            const equalToken = assignCst ? SlimeTokenCreateUtils.createAssignToken(assignCst.getLoc()) : undefined

            // 鍒涘缓 AssignmentPattern 浣滀负 value
            const assignmentPattern = {
                type: SlimeAstTypeName.AssignmentPattern,
                left: identifier,
                right: defaultValue,
                equalToken,
                loc: first.getLoc()
            }

            const keyAst = SlimeAstCreateUtils.createPropertyAst(identifier, assignmentPattern as any)
            keyAst.shorthand = true
            return keyAst
        } else {
            throw new Error(`涓嶆敮鎸佺殑PropertyDefinition绫诲瀷: ${first.getName()}`)
        }
    }


    createPropertyNameAst(cst: SubhutiCst): SlimeIdentifier | SlimeLiteral | SlimeExpression {
        if (!cst || !cst.getChildren() || cst.getChildren().length === 0) {
            throw new Error('createPropertyNameAst: invalid cst or no children')
        }

        const first = cst.getChildren()[0]

        if (first.getName() === SlimeParser.prototype.LiteralPropertyName?.name || first.getName() === 'LiteralPropertyName') {
            return SlimeCstToAstUtils.createLiteralPropertyNameAst(first)
        } else if (first.getName() === SlimeParser.prototype.ComputedPropertyName?.name || first.getName() === 'ComputedPropertyName') {
            // [expression]: value
            // ComputedPropertyName -> LBracket + AssignmentExpression + RBracket
            return SlimeCstToAstUtils.createAssignmentExpressionAst(first.getChildren()[1])
        }
        // 鍥為€€锛氬彲鑳絝irst鐩存帴灏辨槸 LiteralPropertyName 鐨勫唴锟?
        return SlimeCstToAstUtils.createLiteralPropertyNameAst(first)
    }


    createLiteralPropertyNameAst(cst: SubhutiCst): SlimeIdentifier | SlimeLiteral {
        if (!cst) {
            throw new Error('createLiteralPropertyNameAst: cst is null')
        }

        // 鍙兘锟?LiteralPropertyName 鑺傜偣锛屼篃鍙兘鐩存帴鏄唴閮ㄨ妭锟?
        let first = cst
        if (cst.getName() === SlimeParser.prototype.LiteralPropertyName?.name || cst.getName() === 'LiteralPropertyName') {
            if (!cst.getChildren() || cst.getChildren().length === 0) {
                throw new Error('createLiteralPropertyNameAst: LiteralPropertyName has no children')
            }
            first = cst.getChildren()[0]
        }

        // IdentifierName (Es2025Parser) - 鍙兘鏄鍒欒妭鐐规垨 token
        if (first.getName() === 'IdentifierName' || first.getName() === SlimeParser.prototype.IdentifierName?.name) {
            // 濡傛灉锟?value锛岀洿鎺ヤ娇锟?
            if (first.getValue() !== undefined) {
                return SlimeAstCreateUtils.createIdentifier(first.getValue(), first.getLoc())
            }
            // 鍚﹀垯閫掑綊鏌ユ壘 value
            let current = first
            while (current.children && current.children.length > 0 && current.value === undefined) {
                current = current.children[0]
            }
            if (current.value !== undefined) {
                return SlimeAstCreateUtils.createIdentifier(current.value, current.loc || first.getLoc())
            }
            throw new Error(`createLiteralPropertyNameAst: Cannot extract value from IdentifierName`)
        }
        // Identifier (鏃х増锟?Es2025)
        else if (first.getName() === 'Identifier' || first.getName() === SlimeParser.prototype.Identifier?.name) {
            return SlimeCstToAstUtils.createIdentifierAst(first)
        }
        // NumericLiteral
        else if (first.getName() === SlimeTokenConsumer.prototype.NumericLiteral?.name || first.getName() === 'NumericLiteral' || first.getName() === 'Number') {
            return SlimeCstToAstUtils.createNumericLiteralAst(first)
        }
        // StringLiteral
        else if (first.getName() === SlimeTokenConsumer.prototype.StringLiteral?.name || first.getName() === 'StringLiteral' || first.getName() === 'String') {
            return SlimeCstToAstUtils.createStringLiteralAst(first)
        }
        // 濡傛灉鏄洿鎺ョ殑 token锛堟湁 value 灞炴€э級锛屽垱锟?Identifier
        else if (first.getValue() !== undefined) {
            return SlimeAstCreateUtils.createIdentifier(first.getValue(), first.getLoc())
        }

        throw new Error(`createLiteralPropertyNameAst: Unknown type: ${first.getName()}`)
    }


    /**
     * ComputedPropertyName CST 锟?AST
     * ComputedPropertyName -> [ AssignmentExpression ]
     */
    createComputedPropertyNameAst(cst: SubhutiCst): SlimeExpression {
        const expr = cst.getChildren()?.find(ch =>
            ch.name === SlimeParser.prototype.AssignmentExpression?.name ||
            ch.name === 'AssignmentExpression'
        )
        if (expr) {
            return SlimeCstToAstUtils.createAssignmentExpressionAst(expr)
        }
        throw new Error('ComputedPropertyName missing AssignmentExpression')
    }


    createElementListAst(cst: SubhutiCst): Array<SlimeArrayElement> {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ElementList?.name);
        const elements: Array<SlimeArrayElement> = []

        // 閬嶅巻鎵€鏈夊瓙鑺傜偣锛屽锟?AssignmentExpression銆丼preadElement銆丒lision 锟?Comma
        // 姣忎釜鍏冪礌涓庡叾鍚庨潰鐨勯€楀彿閰嶅
        let currentElement: SlimeExpression | SlimeSpreadElement | null = null
        let hasElement = false

        for (let i = 0; i < cst.getChildren().length; i++) {
            const child = cst.getChildren()[i]

            if (child.getName() === SlimeParser.prototype.AssignmentExpression?.name) {
                // 濡傛灉涔嬪墠鏈夊厓绱犱絾娌℃湁閫楀彿锛屽厛鎺ㄥ叆
                if (hasElement) {
                    elements.push(SlimeAstCreateUtils.createArrayElement(currentElement, undefined))
                }
                currentElement = SlimeCstToAstUtils.createAssignmentExpressionAst(child)
                hasElement = true
            } else if (child.getName() === SlimeParser.prototype.SpreadElement?.name) {
                if (hasElement) {
                    elements.push(SlimeAstCreateUtils.createArrayElement(currentElement, undefined))
                }
                currentElement = SlimeCstToAstUtils.createSpreadElementAst(child)
                hasElement = true
            } else if (child.getName() === SlimeParser.prototype.Elision?.name) {
                // Elision 浠ｈ〃绌哄厓绱狅細[1, , 3] - 鍙兘鍖呭惈澶氫釜閫楀彿
                // 姣忎釜 Elision 鍐呴儴鐨勯€楀彿鏁伴噺鍐冲畾绌哄厓绱犳暟锟?
                const elisionCommas = child.getChildren()?.filter((c: any) => c.name === 'Comma' || c.value === ',') || []
                for (let j = 0; j < elisionCommas.length; j++) {
                    if (hasElement) {
                        const commaToken = SlimeTokenCreateUtils.createCommaToken(elisionCommas[j].loc)
                        elements.push(SlimeAstCreateUtils.createArrayElement(currentElement, commaToken))
                        hasElement = false
                        currentElement = null
                    } else {
                        // 杩炵画鐨勭┖鍏冪礌
                        const commaToken = SlimeTokenCreateUtils.createCommaToken(elisionCommas[j].loc)
                        elements.push(SlimeAstCreateUtils.createArrayElement(null, commaToken))
                    }
                }
            } else if (child.getName() === 'Comma' || child.getValue() === ',') {
                // 閫楀彿涓庡墠闈㈢殑鍏冪礌閰嶅
                const commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                elements.push(SlimeAstCreateUtils.createArrayElement(currentElement, commaToken))
                hasElement = false
                currentElement = null
            }
        }

        // 澶勭悊鏈€鍚庝竴涓厓绱狅紙濡傛灉娌℃湁灏鹃殢閫楀彿锟?
        if (hasElement) {
            elements.push(SlimeAstCreateUtils.createArrayElement(currentElement, undefined))
        }

        return elements
    }




    /**
     * CoverInitializedName CST 锟?AST
     * CoverInitializedName -> IdentifierReference Initializer
     */
    createCoverInitializedNameAst(cst: SubhutiCst): any {
        const idRef = cst.getChildren()?.find(ch =>
            ch.name === SlimeParser.prototype.IdentifierReference?.name ||
            ch.name === 'IdentifierReference'
        )
        const init = cst.getChildren()?.find(ch =>
            ch.name === SlimeParser.prototype.Initializer?.name ||
            ch.name === 'Initializer'
        )

        const id = idRef ? SlimeCstToAstUtils.createIdentifierReferenceAst(idRef) : null
        const initValue = init ? SlimeCstToAstUtils.createInitializerAst(init) : null
        const assignCst = init?.children?.[0]
        const equalToken = assignCst ? SlimeTokenCreateUtils.createAssignToken(assignCst.getLoc()) : undefined

        return {
            type: SlimeAstTypeName.AssignmentPattern,
            left: id,
            right: initValue,
            equalToken,
            loc: cst.getLoc()
        }
    }


}

export const SlimeCompoundLiteralCstToAst = new SlimeCompoundLiteralCstToAstSingle()
