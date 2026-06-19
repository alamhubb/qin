/**
 * SlimeTSPrimaryTypeCstToAst - TypeScript 鍩虹绫诲瀷
 *
 * 璐熻矗锛?
 * - createTSPrimaryTypeAst
 * - createTSTypeReferenceAst
 * - createTSTypeNameAst
 * - createTSTupleTypeAst
 * - createTSMappedTypeAst
 * - createTSArrayType
 * - createTSIndexedAccessType
 */
import { SubhutiCst, type SubhutiSourceLocation } from "subhuti";
import { SlimeAstCreateUtils, SlimeAstTypeName } from "slime-ast";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

export class SlimeTSPrimaryTypeCstToAstSingle {
    /**
     * [TypeScript] 杞崲 TSPrimaryType CST 涓?AST
     */
    createTSPrimaryTypeAst(cst: SubhutiCst): any {
        const child = cst.getChildren()?.[0]
        if (!child) {
            throw new Error('TSPrimaryType has no children')
        }

        const name = child.getName()

        // 鏄犲皠绫诲瀷
        if (name === 'TSMappedType') return SlimeCstToAstUtils.createTSMappedTypeAst(child)

        // TSKeywordType 鍖呰瑙勫垯
        if (name === 'TSKeywordType') {
            return SlimeCstToAstUtils.createTSKeywordTypeWrapperAst(child)
        }

        // 瀛楅潰閲忕被鍨?
        if (name === 'TSLiteralType') return SlimeCstToAstUtils.createTSLiteralTypeAst(child)

        // 绫诲瀷寮曠敤
        if (name === 'TSTypeReference') return SlimeCstToAstUtils.createTSTypeReferenceAst(child)

        // 鍏冪粍绫诲瀷
        if (name === 'TSTupleType') return SlimeCstToAstUtils.createTSTupleTypeAst(child)

        // 瀵硅薄绫诲瀷瀛楅潰閲?
        if (name === 'TSTypeLiteral') return SlimeCstToAstUtils.createTSTypeLiteralAst(child)

        // 鎷彿绫诲瀷
        if (name === 'TSParenthesizedType') return SlimeCstToAstUtils.createTSParenthesizedTypeAst(child)

        // this 绫诲瀷
        if (name === 'TSThisType') {
            return {
                type: 'TSThisType',
                loc: child.getLoc()
            }
        }

        throw new Error(`Unknown TSPrimaryType child: ${name}`)
    }


    /**
     * [TypeScript] 杞崲 TSTypeReference CST 涓?AST
     */
    createTSTypeReferenceAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let typeName: any = undefined
        let typeArguments: any = undefined

        for (const child of children) {
            if (child.getName() === 'TSTypeName') {
                typeName = SlimeCstToAstUtils.createTSTypeNameAst(child)
            } else if (child.getName() === 'TSTypeParameterInstantiation') {
                typeArguments = SlimeCstToAstUtils.createTSTypeParameterInstantiationAst(child)
            }
        }

        // 濡傛灉娌℃湁鎵惧埌 TSTypeName锛屽皾璇曠洿鎺ヤ粠 children 涓彁鍙?
        if (!typeName) {
            const nameParts: string[] = []
            for (const child of children) {
                if (child.getName() === 'Identifier' || child.getName() === 'IdentifierName') {
                    const tokenCst = child.getChildren()?.[0] || child
                    if (tokenCst.getValue()) {
                        nameParts.push(tokenCst.getValue())
                    }
                }
            }
            if (nameParts.length > 0) {
                typeName = SlimeCstToAstUtils.buildQualifiedName(nameParts, cst.getLoc())
            }
        }

        if (!typeName) {
            throw new Error('TSTypeReference: no type name found')
        }

        return SlimeAstCreateUtils.createTSTypeReference(
            typeName,
            typeArguments,
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSTypeName CST 涓?AST
     */
    createTSTypeNameAst(cst: SubhutiCst): any {
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
            throw new Error('TSTypeName: no identifier found')
        }

        return SlimeCstToAstUtils.buildQualifiedName(nameParts, cst.getLoc())
    }

    /**
     * 鏋勫缓闄愬畾鍚嶇О
     */
    buildQualifiedName(parts: string[], loc: SubhutiSourceLocation): any {
        if (parts.length === 0) {
            throw new Error('buildQualifiedName: parts is empty')
        }
        if (parts.length === 1) {
            return {
                type: 'Identifier',
                name: parts[0],
                loc,
            }
        }

        // 浠庡乏鍒板彸鏋勫缓: A.B.C -> TSQualifiedName(TSQualifiedName(A, B), C)
        let result: any = {
            type: 'Identifier',
            name: parts[0],
            loc,
        }

        for (let i = 1; i < parts.length; i++) {
            const rightId: any = {
                type: 'Identifier',
                name: parts[i],
                loc,
            }
            result = SlimeAstCreateUtils.createTSQualifiedName(result, rightId, loc)
        }

        return result
    }

    /**
     * [TypeScript] 杞崲 TSTypeParameterInstantiation CST 涓?AST
     */
    createTSTypeParameterInstantiationAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []
        const params: any[] = []

        for (const child of children) {
            if (child.getName() === 'TSType') {
                params.push(SlimeCstToAstUtils.createTSTypeAst(child))
            }
        }

        return SlimeAstCreateUtils.createTSTypeParameterInstantiation(params, cst.getLoc())
    }

    /**
     * [TypeScript] 杞崲 TSTupleType CST 涓?AST
     */
    createTSTupleTypeAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []
        const elementTypes: any[] = []

        for (const child of children) {
            if (child.getName() === 'TSTupleElement' || child.getName() === 'TSTupleElementType') {
                elementTypes.push(SlimeCstToAstUtils.createTSTupleElementAst(child))
            }
        }

        return SlimeAstCreateUtils.createTSTupleType(elementTypes, cst.getLoc())
    }

    /**
     * [TypeScript] 杞崲 TSTupleElement CST 涓?AST
     */
    createTSTupleElementAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        // 妫€鏌ユ槸鍚︽槸鍓╀綑鍏冪礌 TSRestType
        const restCst = children.find(c => c.name === 'TSRestType')
        if (restCst) {
            return SlimeCstToAstUtils.createTSRestTypeAst(restCst)
        }

        // 妫€鏌ユ槸鍚︽槸鍛藉悕鍏冪粍 TSNamedTupleMember
        const namedCst = children.find(c => c.name === 'TSNamedTupleMember')
        if (namedCst) {
            return SlimeCstToAstUtils.createTSNamedTupleMemberAst(namedCst)
        }

        // 妫€鏌ユ槸鍚︽湁 Ellipsis锛堟棫鏍煎紡锛?
        const hasEllipsis = children.some(c => c.name === 'Ellipsis' || c.value === '...')
        if (hasEllipsis) {
            const typeCst = children.find(c => c.name === 'TSType')
            return {
                type: SlimeAstTypeName.TSRestType,
                typeAnnotation: typeCst ? SlimeCstToAstUtils.createTSTypeAst(typeCst) : undefined,
                loc: cst.getLoc(),
            }
        }

        // 鏅€氬厓绱?- 鐩存帴鏄?TSType
        const typeCst = children.find(c => c.name === 'TSType')
        const hasQuestion = children.some(c => c.name === 'Question' || c.value === '?')

        if (typeCst) {
            const typeAst = SlimeCstToAstUtils.createTSTypeAst(typeCst)
            if (hasQuestion) {
                return {
                    type: SlimeAstTypeName.TSOptionalType,
                    typeAnnotation: typeAst,
                    loc: cst.getLoc(),
                }
            }
            return typeAst
        }

        // 濡傛灉娌℃湁鎵惧埌 TSType锛屽彲鑳藉瓙鑺傜偣鏈韩灏辨槸绫诲瀷
        // 灏濊瘯鐩存帴澶勭悊绗竴涓瓙鑺傜偣
        const firstChild = children[0]
        if (firstChild && firstChild.getName()) {
            // 鍙兘鏄?TSConditionalType 鎴栧叾浠栫被鍨?
            if (firstChild.getName() === 'TSConditionalType') {
                return SlimeCstToAstUtils.createTSConditionalTypeAst(firstChild)
            }
            if (firstChild.getName() === 'TSUnionOrIntersectionType') {
                return SlimeCstToAstUtils.createTSUnionOrIntersectionTypeAst(firstChild)
            }
        }

        throw new Error(`TSTupleElement: no type found, children: ${children.map(c => c.name).join(', ')}`)
    }

    /**
     * [TypeScript] 杞崲 TSRestType CST 涓?AST
     */
    createTSRestTypeAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []
        const typeCst = children.find(c => c.name === 'TSType')

        return {
            type: SlimeAstTypeName.TSRestType,
            typeAnnotation: typeCst ? SlimeCstToAstUtils.createTSTypeAst(typeCst) : undefined,
            loc: cst.getLoc(),
        }
    }

    /**
     * [TypeScript] 杞崲 TSNamedTupleMember CST 涓?AST
     */
    createTSNamedTupleMemberAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let label: any = undefined
        let elementType: any = undefined
        let optional = false

        for (const child of children) {
            if (child.getName() === 'Identifier' || child.getName() === 'IdentifierName') {
                const tokenCst = child.getChildren()?.[0] || child
                label = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (child.getName() === 'TSType') {
                elementType = SlimeCstToAstUtils.createTSTypeAst(child)
            } else if (child.getName() === 'Question' || child.getValue() === '?') {
                optional = true
            }
        }

        return {
            type: SlimeAstTypeName.TSNamedTupleMember,
            label,
            elementType,
            optional,
            loc: cst.getLoc(),
        }
    }


    /**
     * [TypeScript] 杞崲 TSMappedType CST 涓?AST
     * { [K in keyof T]: T[K] }
     */
    createTSMappedTypeAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let readonly: '+' | '-' | true | undefined = undefined
        let optional: '+' | '-' | true | undefined = undefined
        let typeParameter: any = undefined
        let nameType: any = undefined
        let typeAnnotation: any = undefined

        // 瑙ｆ瀽 readonly 淇グ绗?
        for (let i = 0; i < children.length; i++) {
            const child = children[i]
            if (child.getValue() === '+' && children[i + 1]?.value === 'readonly') {
                readonly = '+'
                i++
            } else if (child.getValue() === '-' && children[i + 1]?.value === 'readonly') {
                readonly = '-'
                i++
            } else if (child.getValue() === 'readonly') {
                readonly = true
            }
        }

        // 鎵惧埌绫诲瀷鍙傛暟 [K in T]
        const identifierCst = children.find(c => c.name === 'Identifier')
        if (identifierCst) {
            typeParameter = {
                type: SlimeAstTypeName.TSTypeParameter,
                name: SlimeCstToAstUtils.createIdentifierAst(identifierCst),
                loc: identifierCst.loc,
            }

            // 鎵惧埌 in 鍚庨潰鐨勭害鏉熺被鍨?
            const tsTypes = children.filter(c => c.name === 'TSType')
            if (tsTypes.length > 0) {
                typeParameter.constraint = SlimeCstToAstUtils.createTSTypeAst(tsTypes[0])
            }

            // 鎵惧埌 as 鍚庨潰鐨?nameType
            const asIndex = children.findIndex(c => c.value === 'as')
            if (asIndex !== -1 && tsTypes.length > 1) {
                nameType = SlimeCstToAstUtils.createTSTypeAst(tsTypes[1])
            }
        }

        // 瑙ｆ瀽 optional 淇グ绗?(?, +?, -?)
        for (let i = 0; i < children.length; i++) {
            const child = children[i]
            // 璺宠繃 LBracket 鍐呯殑 ?
            if (child.getValue() === ']') {
                // 妫€鏌?] 鍚庨潰鐨??
                const next = children[i + 1]
                if (next?.value === '?') {
                    optional = true
                } else if (next?.value === '+' && children[i + 2]?.value === '?') {
                    optional = '+'
                } else if (next?.value === '-' && children[i + 2]?.value === '?') {
                    optional = '-'
                }
            }
        }

        // 鎵惧埌鍊肩被鍨嬶紙鍐掑彿鍚庨潰鐨?TSType锛?
        const colonIndex = children.findIndex(c => c.value === ':')
        if (colonIndex !== -1) {
            const tsTypesAfterColon = children.slice(colonIndex + 1).filter(c => c.name === 'TSType')
            if (tsTypesAfterColon.length > 0) {
                typeAnnotation = SlimeCstToAstUtils.createTSTypeAst(tsTypesAfterColon[0])
            }
        }

        return SlimeAstCreateUtils.createTSMappedType(
            typeParameter,
            typeAnnotation,
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSParenthesizedType CST 涓?AST
     */
    createTSParenthesizedTypeAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []
        const typeCst = children.find(c => c.name === 'TSType')

        if (typeCst) {
            return SlimeAstCreateUtils.createTSParenthesizedType(
                SlimeCstToAstUtils.createTSTypeAst(typeCst),
                cst.getLoc()
            )
        }

        throw new Error('TSParenthesizedType: no TSType found')
    }
}

export const SlimeTSPrimaryTypeCstToAst = new SlimeTSPrimaryTypeCstToAstSingle()
