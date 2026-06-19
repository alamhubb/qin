/**
 * SlimeTSTypeLiteralCstToAst - TypeScript 绫诲瀷瀛楅潰閲?
 *
 * 璐熻矗锛?
 * - createTSTypeLiteralAst
 * - createTSTypeMemberAst
 * - createTSPropertySignatureAst
 * - createTSMethodSignatureAst
 * - createTSIndexSignatureAst
 * - createTSCallSignatureDeclarationAst
 * - createTSConstructSignatureDeclarationAst
 * - createTSParameterListAst
 */
import { SubhutiCst } from "subhuti";
import { SlimeAstCreateUtils, SlimeAstTypeName } from "slime-ast";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

export class SlimeTSTypeLiteralCstToAstSingle {



    /**
     * [TypeScript] 杞崲 TSTypeLiteral CST 涓?AST
     * 浣跨敤鍖呰绫诲瀷淇濆瓨鍒嗛殧绗︿俊鎭?
     */
    createTSTypeLiteralAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []
        const members: any[] = []

        let currentMember: any = null

        for (let i = 0; i < children.length; i++) {
            const child = children[i]

            if (child.getName() === 'TSTypeMember') {
                // 濡傛灉鏈夊墠涓€涓垚鍛橈紝鍏堜繚瀛橈紙娌℃湁鍒嗛殧绗︼級
                if (currentMember) {
                    members.push(SlimeAstCreateUtils.createTSTypeMemberItem(currentMember, undefined))
                }
                currentMember = SlimeCstToAstUtils.createTSTypeMemberAst(child)
            } else if (child.getName() === 'Semicolon' || child.getValue() === ';') {
                // 鍒嗗彿鍒嗛殧绗?
                if (currentMember) {
                    const separatorToken = { type: 'Semicolon', value: ';', loc: child.getLoc() }
                    members.push(SlimeAstCreateUtils.createTSTypeMemberItem(currentMember, separatorToken))
                    currentMember = null
                }
            } else if (child.getName() === 'Comma' || child.getValue() === ',') {
                // 閫楀彿鍒嗛殧绗?
                if (currentMember) {
                    const separatorToken = { type: 'Comma', value: ',', loc: child.getLoc() }
                    members.push(SlimeAstCreateUtils.createTSTypeMemberItem(currentMember, separatorToken))
                    currentMember = null
                }
            }
        }

        // 澶勭悊鏈€鍚庝竴涓垚鍛橈紙鍙兘娌℃湁鍒嗛殧绗︼級
        if (currentMember) {
            members.push(SlimeAstCreateUtils.createTSTypeMemberItem(currentMember, undefined))
        }

        return SlimeAstCreateUtils.createTSTypeLiteral(members, cst.getLoc())
    }

    /**
     * [TypeScript] 杞崲 TSTypeMember CST 涓?AST
     */
    createTSTypeMemberAst(cst: SubhutiCst): any {
        const child = cst.getChildren()?.[0]
        if (!child) {
            throw new Error('TSTypeMember has no children')
        }

        const name = child.getName()

        if (name === 'TSPropertySignature') {
            return SlimeCstToAstUtils.createTSPropertySignatureAst(child)
        }
        if (name === 'TSMethodSignature') {
            return SlimeCstToAstUtils.createTSMethodSignatureAst(child)
        }
        if (name === 'TSIndexSignature') {
            return SlimeCstToAstUtils.createTSIndexSignatureAst(child)
        }
        if (name === 'TSCallSignatureDeclaration') {
            return SlimeCstToAstUtils.createTSCallSignatureDeclarationAst(child)
        }
        if (name === 'TSConstructSignatureDeclaration') {
            return SlimeCstToAstUtils.createTSConstructSignatureDeclarationAst(child)
        }
        // 澶勭悊 TSPropertyOrMethodSignature锛堝悎骞剁殑灞炴€?鏂规硶绛惧悕锛?
        if (name === 'TSPropertyOrMethodSignature') {
            return SlimeCstToAstUtils.createTSPropertyOrMethodSignatureAst(child)
        }

        throw new Error(`Unknown TSTypeMember child: ${name}`)
    }

    /**
     * [TypeScript] 杞崲 TSPropertyOrMethodSignature CST 涓?AST
     * 杩欐槸涓€涓悎骞剁殑瑙勫垯锛岄渶瑕佹牴鎹唴瀹瑰垽鏂槸灞炴€ц繕鏄柟娉?
     */
    createTSPropertyOrMethodSignatureAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let key: any = undefined
        let typeAnnotation: any = undefined
        let readonly = false
        let optional = false
        let hasParams = false
        let parameters: any[] = []

        for (const child of children) {
            if (child.getName() === 'TSReadonly' || child.getValue() === 'readonly') {
                readonly = true
            } else if (child.getName() === 'PropertyName') {
                // PropertyName -> LiteralPropertyName -> IdentifierName -> IdentifierName (token)
                // 鎴?PropertyName -> ComputedPropertyName -> ...
                key = SlimeCstToAstUtils.extractPropertyNameKey(child)
            } else if (child.getName() === 'Identifier' || child.getName() === 'IdentifierName') {
                const tokenCst = child.getChildren()?.[0] || child
                key = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (child.getName() === 'Question' || child.getValue() === '?') {
                optional = true
            } else if (child.getName() === 'TSParameterList' || child.getName() === 'FormalParameters') {
                hasParams = true
                parameters = SlimeCstToAstUtils.createTSParameterListAst(child)
            } else if (child.getName() === 'TSTypeAnnotation') {
                typeAnnotation = SlimeCstToAstUtils.createTSTypeAnnotationAst(child)
            }
        }

        if (hasParams) {
            // 鏂规硶绛惧悕
            return SlimeAstCreateUtils.createTSMethodSignature(
                key,
                parameters,
                typeAnnotation,
                optional,
                false, // computed default
                cst.getLoc()
            )
        } else {
            // 灞炴€х鍚?
            return SlimeAstCreateUtils.createTSPropertySignature(
                key,
                typeAnnotation,
                optional,
                readonly,
                false, // computed default
                cst.getLoc()
            )
        }
    }

    /**
     * [TypeScript] 浠?PropertyName CST 涓彁鍙?key
     */
    extractPropertyNameKey(cst: SubhutiCst): any {
        const children = cst.getChildren() || []
        const firstChild = children[0]

        if (!firstChild) {
            throw new Error('PropertyName has no children')
        }

        if (firstChild.getName() === 'LiteralPropertyName') {
            // LiteralPropertyName -> IdentifierName | StringLiteral | NumericLiteral
            const literalChild = firstChild.getChildren()?.[0]
            if (!literalChild) {
                throw new Error('LiteralPropertyName has no children')
            }

            if (literalChild.name === 'IdentifierName') {
                // IdentifierName -> IdentifierName (token)
                const tokenCst = literalChild.children?.[0] || literalChild
                return {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (literalChild.name === 'StringLiteral') {
                const tokenCst = literalChild.children?.[0] || literalChild
                return {
                    type: 'Literal',
                    value: tokenCst.getValue(),
                    raw: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (literalChild.name === 'NumericLiteral') {
                const tokenCst = literalChild.children?.[0] || literalChild
                return {
                    type: 'Literal',
                    value: Number(tokenCst.getValue()),
                    raw: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            }
        } else if (firstChild.getName() === 'ComputedPropertyName') {
            // TODO: 澶勭悊璁＄畻灞炴€у悕
            throw new Error('ComputedPropertyName not yet supported in TSPropertyOrMethodSignature')
        }

        // 鍥為€€锛氬皾璇曠洿鎺ヤ粠 children 涓彁鍙?
        const tokenCst = firstChild.getChildren()?.[0]?.children?.[0] || firstChild.getChildren()?.[0] || firstChild
        return {
            type: 'Identifier',
            name: tokenCst.getValue(),
            loc: tokenCst.getLoc(),
        }
    }

    /**
     * [TypeScript] 杞崲 TSPropertySignature CST 涓?AST
     */
    createTSPropertySignatureAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let key: any = undefined
        let typeAnnotation: any = undefined
        let readonly = false
        let optional = false
        let computed = false

        for (const child of children) {
            if (child.getName() === 'TSReadonly' || child.getValue() === 'readonly') {
                readonly = true
            } else if (child.getName() === 'PropertyName') {
                const propChild = child.getChildren()?.[0]
                if (propChild?.name === 'ComputedPropertyName') {
                    computed = true
                    // TODO: 澶勭悊璁＄畻灞炴€у悕
                } else if (propChild?.name === 'LiteralPropertyName') {
                    const tokenCst = propChild.getChildren()?.[0]
                    key = {
                        type: 'Identifier',
                        name: tokenCst?.value,
                        loc: tokenCst?.loc,
                    }
                }
            } else if (child.getName() === 'Identifier' || child.getName() === 'IdentifierName') {
                const tokenCst = child.getChildren()?.[0] || child
                key = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (child.getName() === 'Question' || child.getValue() === '?') {
                optional = true
            } else if (child.getName() === 'TSTypeAnnotation') {
                typeAnnotation = SlimeCstToAstUtils.createTSTypeAnnotationAst(child)
            }
        }

        return SlimeAstCreateUtils.createTSPropertySignature(
            key,
            typeAnnotation,
            optional,
            readonly,
            computed,
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSMethodSignature CST 涓?AST
     */
    createTSMethodSignatureAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let key: any = undefined
        let parameters: any[] = []
        let typeAnnotation: any = undefined
        let optional = false

        for (const child of children) {
            if (child.getName() === 'PropertyName' || child.getName() === 'Identifier' || child.getName() === 'IdentifierName') {
                const tokenCst = child.getChildren()?.[0] || child
                key = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (child.getName() === 'Question' || child.getValue() === '?') {
                optional = true
            } else if (child.getName() === 'TSParameterList') {
                parameters = SlimeCstToAstUtils.createTSParameterListAst(child)
            } else if (child.getName() === 'TSTypeAnnotation') {
                typeAnnotation = SlimeCstToAstUtils.createTSTypeAnnotationAst(child)
            }
        }

        return SlimeAstCreateUtils.createTSMethodSignature(
            key,
            parameters,
            typeAnnotation,
            optional,
            false, // computed default
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSIndexSignature CST 涓?AST
     */
    createTSIndexSignatureAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let parameters: any[] = []
        let typeAnnotation: any = undefined
        let readonly = false

        for (const child of children) {
            if (child.getName() === 'TSReadonly' || child.getValue() === 'readonly') {
                readonly = true
            } else if (child.getName() === 'Identifier' || child.getName() === 'IdentifierName') {
                // 绱㈠紩鍙傛暟鍚?
                const tokenCst = child.getChildren()?.[0] || child
                // 鏌ユ壘鍚庨潰鐨勭被鍨嬫敞瑙?
                const idx = children.indexOf(child)
                const colonIdx = children.findIndex((c, i) => i > idx && (c.name === 'Colon' || c.value === ':'))
                if (colonIdx !== -1 && children[colonIdx + 1]?.name === 'TSType') {
                    parameters.push({
                        type: 'Identifier',
                        name: tokenCst.getValue(),
                        typeAnnotation: {
                            type: SlimeAstTypeName.TSTypeAnnotation,
                            typeAnnotation: SlimeCstToAstUtils.createTSTypeAst(children[colonIdx + 1]),
                        },
                        loc: tokenCst.getLoc(),
                    })
                }
            } else if (child.getName() === 'TSType' && !parameters.length) {
                // 璺宠繃绱㈠紩鍙傛暟鐨勭被鍨嬶紝宸插湪涓婇潰澶勭悊
            } else if (child.getName() === 'TSType' && parameters.length) {
                // 杩斿洖绫诲瀷
                typeAnnotation = {
                    type: SlimeAstTypeName.TSTypeAnnotation,
                    typeAnnotation: SlimeCstToAstUtils.createTSTypeAst(child),
                }
            }
        }

        return SlimeAstCreateUtils.createTSIndexSignature(
            parameters,
            typeAnnotation,
            readonly,
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSCallSignatureDeclaration CST 涓?AST
     */
    createTSCallSignatureDeclarationAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let parameters: any[] = []
        let typeAnnotation: any = undefined

        for (const child of children) {
            if (child.getName() === 'TSParameterList') {
                parameters = SlimeCstToAstUtils.createTSParameterListAst(child)
            } else if (child.getName() === 'TSTypeAnnotation') {
                typeAnnotation = SlimeCstToAstUtils.createTSTypeAnnotationAst(child)
            }
        }

        return SlimeAstCreateUtils.createTSCallSignatureDeclaration(
            parameters,
            typeAnnotation,
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSConstructSignatureDeclaration CST 涓?AST
     */
    createTSConstructSignatureDeclarationAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let parameters: any[] = []
        let typeAnnotation: any = undefined

        for (const child of children) {
            if (child.getName() === 'TSParameterList') {
                parameters = SlimeCstToAstUtils.createTSParameterListAst(child)
            } else if (child.getName() === 'TSTypeAnnotation') {
                typeAnnotation = SlimeCstToAstUtils.createTSTypeAnnotationAst(child)
            }
        }

        return SlimeAstCreateUtils.createTSConstructSignatureDeclaration(
            parameters,
            typeAnnotation,
            cst.getLoc()
        )
    }

    /**
     * [TypeScript] 杞崲 TSParameterList CST 涓?AST
     */
    createTSParameterListAst(cst: SubhutiCst): any[] {
        const children = cst.getChildren() || []
        const params: any[] = []

        for (const child of children) {
            if (child.getName() === 'TSParameter') {
                params.push(SlimeCstToAstUtils.createTSParameterAst(child))
            }
        }

        return params
    }

    /**
     * [TypeScript] 杞崲 TSParameter CST 涓?AST
     */
    createTSParameterAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        let name: any = undefined
        let typeAnnotation: any = undefined
        let optional = false
        let rest = false

        for (const child of children) {
            if (child.getName() === 'Ellipsis' || child.getValue() === '...') {
                rest = true
            } else if (child.getName() === 'BindingIdentifier' || child.getName() === 'Identifier' || child.getName() === 'IdentifierName') {
                const tokenCst = child.getChildren()?.[0]?.children?.[0] || child.getChildren()?.[0] || child
                name = {
                    type: 'Identifier',
                    name: tokenCst.getValue(),
                    loc: tokenCst.getLoc(),
                }
            } else if (child.getName() === 'Question' || child.getValue() === '?') {
                optional = true
            } else if (child.getName() === 'TSTypeAnnotation') {
                typeAnnotation = SlimeCstToAstUtils.createTSTypeAnnotationAst(child)
            }
        }

        if (rest) {
            return {
                type: 'RestElement',
                argument: name,
                typeAnnotation,
                loc: cst.getLoc(),
            }
        }

        return {
            type: 'Identifier',
            ...name,
            typeAnnotation,
            optional,
            loc: cst.getLoc(),
        }
    }

}

export const SlimeTSTypeLiteralCstToAst = new SlimeTSTypeLiteralCstToAstSingle()
