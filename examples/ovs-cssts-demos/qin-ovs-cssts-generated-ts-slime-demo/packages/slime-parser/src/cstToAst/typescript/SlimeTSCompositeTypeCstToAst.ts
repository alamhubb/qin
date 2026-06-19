/**
 * SlimeTSCompositeTypeCstToAst - TypeScript 澶嶅悎绫诲瀷
 *
 * 璐熻矗锛?
 * - createTSUnionOrIntersectionTypeAst
 * - createTSIntersectionTypeAst
 * - createTSConditionalTypeAst
 * - createTSTypeOperandAst
 * - createTSPrefixTypeOrPrimaryAst
 * - createTSTypeQueryAst
 * - createTSTypeOperatorAst
 * - createTSInferTypeAst
 */
import { SubhutiCst, type SubhutiSourceLocation } from "subhuti";
import { SlimeAstCreateUtils, SlimeAstTypeName } from "slime-ast";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

export class SlimeTSCompositeTypeCstToAstSingle {

    /**
     * [TypeScript] 杞崲 TSUnionOrIntersectionType CST 涓?AST
     * CST: TSUnionOrIntersectionType -> |_opt TSIntersectionType (BitwiseOr TSIntersectionType)*
     */
    createTSUnionOrIntersectionTypeAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        // 妫€娴嬪墠瀵?| (鐢ㄤ簬鏍煎紡鍖?
        let hasLeadingPipe = false
        const firstChild = children[0]
        if (firstChild && (firstChild.getName() === 'BitwiseOr' || firstChild.getValue() === '|')) {
            hasLeadingPipe = true
        }

        // 鏀堕泦鎵€鏈?TSIntersectionType
        const intersectionTypes: any[] = []
        for (const child of children) {
            if (child.getName() === 'TSIntersectionType') {
                intersectionTypes.push(SlimeCstToAstUtils.createTSIntersectionTypeAst(child))
            }
        }

        // 濡傛灉鍙湁涓€涓笖娌℃湁鍓嶅 |锛岀洿鎺ヨ繑鍥?
        if (intersectionTypes.length === 1 && !hasLeadingPipe) {
            return intersectionTypes[0]
        }

        // 澶氫釜鎴栨湁鍓嶅 | 鍒欏垱寤?TSUnionType
        const ast = SlimeAstCreateUtils.createTSUnionType(intersectionTypes, cst.getLoc())
        // 淇濆瓨鍓嶅 | 淇℃伅
        if (hasLeadingPipe) {
            ast.hasLeadingPipe = true
        }
        return ast
    }




    /**
     * [TypeScript] 杞崲 TSConditionalType CST 涓?AST
     * CST: TSConditionalType -> TSUnionOrIntersectionType (extends TSUnionOrIntersectionType ? TSType : TSType)?
     */
    createTSConditionalTypeAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        // 绗竴涓瓙鑺傜偣鏄?checkType (TSUnionOrIntersectionType)
        const checkTypeCst = children.find(c => c.name === 'TSUnionOrIntersectionType')
        if (!checkTypeCst) {
            throw new Error('TSConditionalType missing checkType')
        }
        const checkType = SlimeCstToAstUtils.createTSUnionOrIntersectionTypeAst(checkTypeCst)

        // 妫€鏌ユ槸鍚︽湁鏉′欢閮ㄥ垎 (extends ... ? ... : ...)
        const extendsToken = children.find(c => c.name === 'Extends')
        if (!extendsToken) {
            // 娌℃湁鏉′欢閮ㄥ垎锛岀洿鎺ヨ繑鍥?checkType
            return checkType
        }

        // 鎵惧埌鎵€鏈?TSUnionOrIntersectionType锛岀浜屼釜鏄?extendsType
        const unionTypes = children.filter(c => c.name === 'TSUnionOrIntersectionType')
        if (unionTypes.length < 2) {
            throw new Error('TSConditionalType missing extendsType')
        }
        const extendsType = SlimeCstToAstUtils.createTSUnionOrIntersectionTypeAst(unionTypes[1])

        // 鎵惧埌鎵€鏈?TSType锛岀涓€涓槸 trueType锛岀浜屼釜鏄?falseType
        const tsTypes = children.filter(c => c.name === 'TSType')
        if (tsTypes.length < 2) {
            throw new Error('TSConditionalType missing trueType or falseType')
        }
        const trueType = SlimeCstToAstUtils.createTSTypeAst(tsTypes[0])
        const falseType = SlimeCstToAstUtils.createTSTypeAst(tsTypes[1])

        return SlimeAstCreateUtils.createTSConditionalType(
            checkType,
            extendsType,
            trueType,
            falseType,
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSIntersectionType CST 涓?AST
     * CST: TSIntersectionType -> TSTypeOperand (BitwiseAnd TSTypeOperand)*
     */
    createTSIntersectionTypeAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        // 鏀堕泦鎵€鏈?TSTypeOperand
        const operandTypes: any[] = []
        for (const child of children) {
            if (child.getName() === 'TSTypeOperand') {
                operandTypes.push(SlimeCstToAstUtils.createTSTypeOperandAst(child))
            }
        }

        // 濡傛灉鍙湁涓€涓紝鐩存帴杩斿洖
        if (operandTypes.length === 1) {
            return operandTypes[0]
        }

        // 澶氫釜鍒欏垱寤?TSIntersectionType
        return SlimeAstCreateUtils.createTSIntersectionType(operandTypes, cst.getLoc())
    }

    /**
     * [TypeScript] 杞崲 TSTypeOperand CST 涓?AST
     * CST: TSTypeOperand -> TSPrefixTypeOrPrimary ([] | [TSType])*
     */
    createTSTypeOperandAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        // 绗竴涓瓙鑺傜偣鏄?TSPrefixTypeOrPrimary
        const prefixOrPrimaryCst = children.find(c => c.name === 'TSPrefixTypeOrPrimary')
        if (!prefixOrPrimaryCst) {
            // 鍏煎鏃х殑 TSPrimaryType
            const primaryCst = children.find(c => c.name === 'TSPrimaryType')
            if (!primaryCst) {
                throw new Error('TSTypeOperand: TSPrefixTypeOrPrimary or TSPrimaryType not found')
            }
            return SlimeCstToAstUtils.createTSPrimaryTypeAst(primaryCst)
        }

        let result = SlimeCstToAstUtils.createTSPrefixTypeOrPrimaryAst(prefixOrPrimaryCst)

        // 妫€鏌ユ槸鍚︽湁鏁扮粍鍚庣紑 []
        let i = children.indexOf(prefixOrPrimaryCst) + 1
        while (i < children.length) {
            const child = children[i]
            if (child.getName() === 'LBracket' || child.getValue() === '[') {
                // 妫€鏌ヤ笅涓€涓槸 RBracket 杩樻槸 TSType
                const next = children[i + 1]
                if (next && (next.name === 'RBracket' || next.value === ']')) {
                    // 绌烘嫭鍙?[] - 鏁扮粍绫诲瀷
                    result = SlimeAstCreateUtils.createTSArrayType(result, cst.getLoc())
                    i += 2
                } else if (next && next.name === 'TSType') {
                    // [TSType] - 绱㈠紩璁块棶绫诲瀷
                    result = SlimeAstCreateUtils.createTSIndexedAccessType(
                        result,
                        SlimeCstToAstUtils.createTSTypeAst(next),
                        cst.getLoc()
                    )
                    i += 3 // skip TSType and RBracket
                } else {
                    i++
                }
            } else {
                i++
            }
        }

        return result
    }

    /**
     * [TypeScript] 杞崲 TSPrefixTypeOrPrimary CST 涓?AST
     * CST: TSPrefixTypeOrPrimary -> TSTypeQuery | TSTypeOperator | TSInferType | TSPrimaryType
     */
    createTSPrefixTypeOrPrimaryAst(cst: SubhutiCst): any {
        const child = cst.getChildren()?.[0]
        if (!child) {
            throw new Error('TSPrefixTypeOrPrimary has no children')
        }

        const name = child.getName()

        // 绫诲瀷鏌ヨ typeof x
        if (name === 'TSTypeQuery') {
            return SlimeCstToAstUtils.createTSTypeQueryAst(child)
        }

        // 绫诲瀷鎿嶄綔绗?keyof, readonly, unique
        if (name === 'TSTypeOperator') {
            return SlimeCstToAstUtils.createTSTypeOperatorAst(child)
        }

        // 鎺ㄦ柇绫诲瀷 infer R
        if (name === 'TSInferType') {
            return SlimeCstToAstUtils.createTSInferTypeAst(child)
        }

        // 鍩虹绫诲瀷
        if (name === 'TSPrimaryType') {
            return SlimeCstToAstUtils.createTSPrimaryTypeAst(child)
        }

        throw new Error(`Unknown TSPrefixTypeOrPrimary child: ${name}`)
    }

    /**
     * [TypeScript] 杞崲 TSTypeQuery CST 涓?AST (typeof x)
     * 鏀寔锛歵ypeof identifier, typeof this, typeof this.xxx
     */
    createTSTypeQueryAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        // 妫€鏌ユ槸鍚︽槸 typeof this 鎴?typeof this.xxx
        const thisCst = children.find(c => c.name === 'This' || c.value === 'this')
        if (thisCst) {
            // typeof this 鎴?typeof this.xxx
            // 鏀堕泦灞炴€ч摼锛堝鏋滄湁锛?
            let exprName: any = {
                type: 'ThisExpression',
                loc: thisCst.loc
            }

            // 鏌ユ壘鍚庣画鐨勫睘鎬ц闂紙.identifier锛?
            let i = children.indexOf(thisCst) + 1
            while (i < children.length) {
                const dotCst = children[i]
                if (dotCst.name === 'Dot' || dotCst.value === '.') {
                    i++
                    const identCst = children[i]
                    if (identCst && (identCst.name === 'Identifier' || identCst.name === 'IdentifierName')) {
                        exprName = {
                            type: 'TSQualifiedName',
                            left: exprName,
                            right: SlimeCstToAstUtils.createIdentifierAst(identCst),
                            loc: cst.getLoc()
                        }
                        i++
                    }
                } else {
                    break
                }
            }

            return SlimeAstCreateUtils.createTSTypeQuery(exprName, cst.getLoc())
        }

        // 鎵惧埌 TSTypeName
        const typeNameCst = children.find(c => c.name === 'TSTypeName')
        if (!typeNameCst) {
            // 鍙兘鏄洿鎺ョ殑 Identifier
            const identCst = children.find(c => c.name === 'Identifier' || c.name === 'IdentifierName')
            if (identCst) {
                const exprName = SlimeCstToAstUtils.createIdentifierAst(identCst)
                return SlimeAstCreateUtils.createTSTypeQuery(exprName, cst.getLoc())
            }
            throw new Error('TSTypeQuery: TSTypeName or Identifier not found')
        }

        const exprName = SlimeCstToAstUtils.createTSTypeNameAst(typeNameCst)

        // 鍙€夌殑绫诲瀷鍙傛暟
        const typeParamsCst = children.find(c => c.name === 'TSTypeParameterInstantiation')
        const typeParameters = typeParamsCst ? SlimeCstToAstUtils.createTSTypeParameterInstantiationAst(typeParamsCst) : undefined

        return SlimeAstCreateUtils.createTSTypeQuery(exprName, cst.getLoc())
    }

    /**
     * [TypeScript] 杞崲 TSTypeOperator CST 涓?AST (keyof, readonly, unique)
     */
    createTSTypeOperatorAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        // 纭畾鎿嶄綔绗︾被鍨?
        let operator: 'keyof' | 'readonly' | 'unique'
        let typeAnnotation: any

        // 妫€鏌ョ涓€涓瓙鑺傜偣鏉ョ‘瀹氭搷浣滅
        const firstChild = children[0]
        if (!firstChild) {
            throw new Error('TSTypeOperator has no children')
        }

        if (firstChild.getValue() === 'keyof' || firstChild.getName()?.includes('Keyof')) {
            operator = 'keyof'
            const operandCst = children.find(c => c.name === 'TSTypeOperand')
            if (!operandCst) {
                throw new Error('TSTypeOperator keyof: TSTypeOperand not found')
            }
            typeAnnotation = SlimeCstToAstUtils.createTSTypeOperandAst(operandCst)
        } else if (firstChild.getValue() === 'readonly' || firstChild.getName()?.includes('Readonly')) {
            operator = 'readonly'
            const operandCst = children.find(c => c.name === 'TSTypeOperand')
            if (!operandCst) {
                throw new Error('TSTypeOperator readonly: TSTypeOperand not found')
            }
            typeAnnotation = SlimeCstToAstUtils.createTSTypeOperandAst(operandCst)
        } else if (firstChild.getValue() === 'unique' || firstChild.getName()?.includes('Unique')) {
            operator = 'unique'
            // unique symbol - 鎵惧埌 TSSymbolKeyword
            const symbolCst = children.find(c => c.name === 'TSSymbolKeyword')
            if (!symbolCst) {
                throw new Error('TSTypeOperator unique: TSSymbolKeyword not found')
            }
            typeAnnotation = SlimeCstToAstUtils.createTSKeywordTypeAst(symbolCst, SlimeAstTypeName.TSSymbolKeyword)
        } else {
            throw new Error(`Unknown TSTypeOperator: ${firstChild.getValue() || firstChild.getName()}`)
        }

        return SlimeAstCreateUtils.createTSTypeOperator(
            operator,
            typeAnnotation,
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSInferType CST 涓?AST (infer R)
     */
    createTSInferTypeAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        // 鎵惧埌鏍囪瘑绗?
        const identifierCst = children.find(c => c.name === 'Identifier')
        if (!identifierCst) {
            throw new Error('TSInferType: Identifier not found')
        }

        const typeParameter: any = {
            type: SlimeAstTypeName.TSTypeParameter,
            name: SlimeCstToAstUtils.createIdentifierAst(identifierCst),
            loc: identifierCst.loc,
        }

        // 鍙€夌殑绾︽潫 extends TSType
        const extendsCst = children.find(c => c.name === 'Extends')
        if (extendsCst) {
            const constraintCst = children.find(c => c.name === 'TSType')
            if (constraintCst) {
                typeParameter.constraint = SlimeCstToAstUtils.createTSTypeAst(constraintCst)
            }
        }

        return {
            type: SlimeAstTypeName.TSInferType,
            typeParameter,
            loc: cst.getLoc(),
        }
    }
}

export const SlimeTSCompositeTypeCstToAst = new SlimeTSCompositeTypeCstToAstSingle()
