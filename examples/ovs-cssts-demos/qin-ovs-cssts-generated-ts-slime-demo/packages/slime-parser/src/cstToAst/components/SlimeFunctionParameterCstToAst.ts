import { SubhutiCst } from "subhuti";
import {
    SlimeAstCreateUtils,
    type SlimeBlockStatement,
    SlimeFunctionExpression, type SlimeFunctionParam,
    SlimeIdentifier, type SlimeMethodDefinition, SlimeAstTypeName,
    SlimePattern, SlimeRestElement, type SlimeReturnStatement,
    SlimeStatement, SlimeTokenCreateUtils,
    SlimeVariableDeclarator
} from "slime-ast";
import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";

import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import { SlimeTokenConsumer } from "../../SlimeTokenConsumer.ts";

export class SlimeFunctionParameterCstToAstSingle {

    /**
     * 澶勭悊 FormalParameters CST 鑺傜偣
     */
    createFormalParametersAst(cst: SubhutiCst): SlimePattern[] {
        // FormalParameters 鍙兘鍖呭惈 FormalParameterList 鎴栦负锟?
        if (!cst.getChildren() || cst.getChildren().length === 0) {
            return []
        }

        const params: SlimePattern[] = []

        for (const child of cst.getChildren()) {
            const name = child.getName()

            // FormalParameterList
            if (name === SlimeParser.prototype.FormalParameterList?.name || name === 'FormalParameterList') {
                return SlimeCstToAstUtils.createFormalParameterListAst(child)
            }

            // FormalParameter
            if (name === SlimeParser.prototype.FormalParameter?.name || name === 'FormalParameter') {
                params.push(SlimeCstToAstUtils.createFormalParameterAst(child))
                continue
            }

            // BindingIdentifier - 鐩存帴浣滀负鍙傛暟
            if (name === SlimeParser.prototype.BindingIdentifier?.name || name === 'BindingIdentifier') {
                params.push(SlimeCstToAstUtils.createBindingIdentifierAst(child))
                continue
            }

            // BindingElement
            if (name === SlimeParser.prototype.BindingElement?.name || name === 'BindingElement') {
                params.push(SlimeCstToAstUtils.createBindingElementAst(child))
                continue
            }

            // FunctionRestParameter
            if (name === SlimeParser.prototype.FunctionRestParameter?.name || name === 'FunctionRestParameter') {
                params.push(SlimeCstToAstUtils.createFunctionRestParameterAst(child))
                continue
            }

            // [TypeScript] TSThisParameter - this 鍙傛暟
            if (name === 'TSThisParameter') {
                params.push(SlimeCstToAstUtils.createTSThisParameterAst(child))
                continue
            }

            // 璺宠繃閫楀彿鍜屾嫭鍙?
            if (child.getValue() === ',' || child.getValue() === '(' || child.getValue() === ')') {
                continue
            }
        }

        return params
    }


    /**
     * Create FormalParameters AST
     * ES2025 FormalParameters:
     *   [empty]
     *   FunctionRestParameter
     *   FormalParameterList
     *   FormalParameterList ,
     *   FormalParameterList , FunctionRestParameter
     */
    createFormalParametersAstWrapped(cst: SubhutiCst): SlimeFunctionParam[] {
        const children = cst.getChildren() || []
        const params: SlimeFunctionParam[] = []

        let currentParam: SlimePattern | null = null
        let hasParam = false

        for (let i = 0; i < children.length; i++) {
            const child = children[i]
            if (!child) continue

            const name = child.getName()

            // Skip parentheses
            if (child.getValue() === '(' || name === 'LParen') continue
            if (child.getValue() === ')' || name === 'RParen') continue

            // Handle comma - pair with previous param
            if (child.getValue() === ',' || name === 'Comma') {
                if (hasParam) {
                    const commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                    params.push(SlimeAstCreateUtils.createFunctionParam(currentParam!, commaToken))
                    hasParam = false
                    currentParam = null
                } else if (params.length > 0) {
                    const lastParam = params[params.length - 1] as any
                    if (!lastParam.commaToken) {
                        lastParam.commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                    }
                }
                continue
            }

            // FormalParameterList锛氬寘锟?FormalParameter (澶氫釜浠ラ€楀彿鍒嗛殧)
            if (name === SlimeParser.prototype.FormalParameterList?.name || name === 'FormalParameterList') {
                // 濡傛灉涔嬪墠鏈夊弬鏁版病澶勭悊锛屽厛鎺ㄥ叆
                if (hasParam) {
                    params.push(SlimeAstCreateUtils.createFunctionParam(currentParam!, undefined))
                    hasParam = false
                    currentParam = null
                }
                params.push(...SlimeCstToAstUtils.createFormalParameterListFromEs2025Wrapped(child))
                continue
            }

            // FunctionRestParameter
            if (name === SlimeParser.prototype.FunctionRestParameter?.name || name === 'FunctionRestParameter') {
                if (hasParam) {
                    params.push(SlimeAstCreateUtils.createFunctionParam(currentParam!, undefined))
                }
                currentParam = SlimeCstToAstUtils.createFunctionRestParameterAst(child)
                hasParam = true
                continue
            }

            // Direct FormalParameter锛圗S2025 缁撴瀯锟?
            if (name === SlimeParser.prototype.FormalParameter?.name || name === 'FormalParameter') {
                if (hasParam) {
                    params.push(SlimeAstCreateUtils.createFunctionParam(currentParam!, undefined))
                }
                currentParam = SlimeCstToAstUtils.createFormalParameterAst(child)
                hasParam = true
                continue
            }

            // Direct BindingElement or BindingIdentifier
            if (name === SlimeParser.prototype.BindingElement?.name || name === 'BindingElement') {
                if (hasParam) {
                    params.push(SlimeAstCreateUtils.createFunctionParam(currentParam!, undefined))
                }
                currentParam = SlimeCstToAstUtils.createBindingElementAst(child)
                hasParam = true
                continue
            }

            if (name === SlimeParser.prototype.BindingIdentifier?.name || name === 'BindingIdentifier') {
                if (hasParam) {
                    params.push(SlimeAstCreateUtils.createFunctionParam(currentParam!, undefined))
                }
                currentParam = SlimeCstToAstUtils.createBindingIdentifierAst(child)
                hasParam = true
                continue
            }

            // [TypeScript] TSThisParameter - this 鍙傛暟
            if (name === 'TSThisParameter') {
                if (hasParam) {
                    params.push(SlimeAstCreateUtils.createFunctionParam(currentParam!, undefined))
                }
                currentParam = SlimeCstToAstUtils.createTSThisParameterAst(child)
                hasParam = true
                continue
            }
        }

        // 澶勭悊鏈€鍚庝竴涓弬鏁帮紙娌℃湁灏鹃殢閫楀彿锛?
        if (hasParam) {
            params.push(SlimeAstCreateUtils.createFunctionParam(currentParam!, undefined))
        }

        return params
    }


    createFormalParameterListAst(cst: SubhutiCst): SlimePattern[] {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.FormalParameterList?.name);

        if (!cst.getChildren() || cst.getChildren().length === 0) {
            return []
        }

        const params: SlimePattern[] = []

        for (const child of cst.getChildren()) {
            const name = child.getName()

            // FunctionRestParameter - rest鍙傛暟
            if (name === 'FunctionRestParameter' || name === SlimeParser.prototype.FunctionRestParameter?.name) {
                params.push(SlimeCstToAstUtils.createFunctionRestParameterAst(child))
                continue
            }

            // FormalParameter - 鐩存帴鐨勫弬鏁?
            if (name === 'FormalParameter' || name === SlimeParser.prototype.FormalParameter?.name) {
                params.push(SlimeCstToAstUtils.createFormalParameterAst(child))
                continue
            }

            // BindingElement
            if (name === 'BindingElement' || name === SlimeParser.prototype.BindingElement?.name) {
                params.push(SlimeCstToAstUtils.createBindingElementAst(child))
                continue
            }

            // BindingIdentifier
            if (name === 'BindingIdentifier' || name === SlimeParser.prototype.BindingIdentifier?.name) {
                params.push(SlimeCstToAstUtils.createBindingIdentifierAst(child))
                continue
            }

            // 璺宠繃閫楀彿
            if (child.getValue() === ',') {
                continue
            }
        }

        return params
    }

    /**
     * 鍒涘缓 FormalParameterList AST (鍖呰鐗堟湰)
     */
    createFormalParameterListAstWrapped(cst: SubhutiCst): SlimeFunctionParam[] {
        const params: SlimeFunctionParam[] = []
        let lastParam: SlimePattern | null = null

        for (const child of cst.getChildren() || []) {
            if (child.getName() === SlimeParser.prototype.FormalParameter?.name) {
                if (lastParam) {
                    params.push(SlimeAstCreateUtils.createFunctionParam(lastParam))
                }
                lastParam = SlimeCstToAstUtils.createFormalParameterAst(child)
            } else if (child.getName() === SlimeParser.prototype.FunctionRestParameter?.name) {
                if (lastParam) {
                    params.push(SlimeAstCreateUtils.createFunctionParam(lastParam))
                }
                lastParam = SlimeCstToAstUtils.createFunctionRestParameterAst(child)
            } else if (child.getName() === SlimeTokenConsumer.prototype.Comma?.name || child.getValue() === ',') {
                if (lastParam) {
                    params.push(SlimeAstCreateUtils.createFunctionParam(lastParam, SlimeTokenCreateUtils.createCommaToken(child.getLoc())))
                    lastParam = null
                }
            }
        }

        if (lastParam) {
            params.push(SlimeAstCreateUtils.createFunctionParam(lastParam))
        }

        return params
    }


    /**
     * 锟?ES2025 FormalParameterList 鍒涘缓鍙傛暟 AST锛堝寘瑁呯被鍨嬶級
     * FormalParameterList: FormalParameter (, FormalParameter)*
     */
    createFormalParameterListFromEs2025Wrapped(cst: SubhutiCst): SlimeFunctionParam[] {
        const children = cst.getChildren() || []
        const params: SlimeFunctionParam[] = []

        let currentParam: SlimePattern | null = null
        let hasParam = false

        for (let i = 0; i < children.length; i++) {
            const child = children[i]
            if (!child) continue
            const name = child.getName()

            // Handle comma - pair with previous param
            if (child.getValue() === ',' || name === 'Comma') {
                if (hasParam) {
                    const commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                    params.push(SlimeAstCreateUtils.createFunctionParam(currentParam!, commaToken))
                    hasParam = false
                    currentParam = null
                }
                continue
            }

            // FormalParameter -> BindingElement
            if (name === SlimeParser.prototype.FormalParameter?.name || name === 'FormalParameter') {
                if (hasParam) {
                    params.push(SlimeAstCreateUtils.createFunctionParam(currentParam!, undefined))
                }
                currentParam = SlimeCstToAstUtils.createFormalParameterAst(child)
                hasParam = true
            }
        }

        // 澶勭悊鏈€鍚庝竴涓弬锟?
        if (hasParam) {
            params.push(SlimeAstCreateUtils.createFunctionParam(currentParam!, undefined))
        }

        return params
    }


    createFormalParameterAst(cst: SubhutiCst): SlimePattern {
        // FormalParameter: BindingElement
        const first = cst.getChildren()[0]
        if (first.getName() === 'BindingElement' || first.getName() === SlimeParser.prototype.BindingElement?.name) {
            return SlimeCstToAstUtils.createBindingElementAst(first)
        }
        return SlimeCstToAstUtils.createBindingElementAst(cst)
    }

    createFunctionRestParameterAst(cst: SubhutiCst): SlimeRestElement {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.FunctionRestParameter?.name);
        const first = cst.getChildren()[0]
        return SlimeCstToAstUtils.createBindingRestElementAst(first)
    }


    /**
     * 澶勭悊 UniqueFormalParameters CST 鑺傜偣
     */
    createUniqueFormalParametersAst(cst: SubhutiCst): SlimePattern[] {
        // UniqueFormalParameters: FormalParameters
        if (!cst.getChildren() || cst.getChildren().length === 0) {
            return []
        }
        const first = cst.getChildren()[0]
        if (first.getName() === 'FormalParameters' || first.getName() === SlimeParser.prototype.FormalParameters?.name) {
            return SlimeCstToAstUtils.createFormalParametersAst(first)
        }
        // 鍙兘鐩存帴锟?FormalParameterList
        return SlimeCstToAstUtils.createFormalParametersAst(cst)
    }

    /** 杩斿洖鍖呰绫诲瀷鐨勭増锟?*/
    createUniqueFormalParametersAstWrapped(cst: SubhutiCst): SlimeFunctionParam[] {
        // UniqueFormalParameters: FormalParameters
        if (!cst.getChildren() || cst.getChildren().length === 0) {
            return []
        }
        const first = cst.getChildren()[0]
        if (first.getName() === 'FormalParameters' || first.getName() === SlimeParser.prototype.FormalParameters?.name) {
            return SlimeCstToAstUtils.createFormalParametersAstWrapped(first)
        }
        // 鍙兘鐩存帴锟?FormalParameterList
        return SlimeCstToAstUtils.createFormalParametersAstWrapped(cst)
    }


    /**
     * 浠嶦xpression涓彁鍙栫澶村嚱鏁板弬锟?
     * 澶勭悊閫楀彿琛ㄨ揪锟?(a, b) 鎴栧崟涓弬锟?(x)
     */
    extractParametersFromExpression(expressionCst: SubhutiCst): SlimePattern[] {
        // Expression鍙兘鏄細
        // 1. 鍗曚釜Identifier: x
        // 2. 閫楀彿琛ㄨ揪锟? a, b 锟?a, b, c
        // 3. 璧嬪€艰〃杈惧紡锛堥粯璁ゅ弬鏁帮級: a = 1

        // 妫€鏌ユ槸鍚︽槸AssignmentExpression
        if (expressionCst.getName() === SlimeParser.prototype.AssignmentExpression?.name) {
            const assignmentAst = SlimeCstToAstUtils.createAssignmentExpressionAst(expressionCst)
            // 濡傛灉鏄畝鍗曠殑identifier锛岃繑鍥炲畠
            if (assignmentAst.type === SlimeAstTypeName.Identifier) {
                return [assignmentAst as any]
            }
            // 濡傛灉鏄祴鍊硷紙榛樿鍙傛暟锛夛紝杩斿洖AssignmentPattern
            if (assignmentAst.type === SlimeAstTypeName.AssignmentExpression) {
                const equalToken = assignmentAst.operator?.loc
                    ? SlimeTokenCreateUtils.createAssignToken(assignmentAst.operator.loc)
                    : undefined
                return [{
                    type: 'AssignmentPattern',
                    left: assignmentAst.left,
                    right: assignmentAst.right,
                    equalToken
                } as any]
            }
            return [assignmentAst as any]
        }

        // 濡傛灉鏄疎xpression锛屾鏌hildren
        if (expressionCst.getChildren() && expressionCst.getChildren().length > 0) {
            const params: SlimePattern[] = []

            // 閬嶅巻children锛屾煡鎵炬墍鏈堿ssignmentExpression锛堢敤閫楀彿鍒嗛殧锟?
            for (const child of expressionCst.getChildren()) {
                if (child.getName() === SlimeParser.prototype.AssignmentExpression?.name) {
                    const assignmentAst = SlimeCstToAstUtils.createAssignmentExpressionAst(child)
                    // 杞崲涓哄弬锟?
                    if (assignmentAst.type === SlimeAstTypeName.Identifier) {
                        params.push(assignmentAst as any)
                    } else if (assignmentAst.type === SlimeAstTypeName.AssignmentExpression) {
                        // 榛樿鍙傛暟
                        const equalToken = assignmentAst.operator?.loc
                            ? SlimeTokenCreateUtils.createAssignToken(assignmentAst.operator.loc)
                            : undefined
                        params.push({
                            type: 'AssignmentPattern',
                            left: assignmentAst.left,
                            right: assignmentAst.right,
                            equalToken
                        } as any)
                    } else if (assignmentAst.type === SlimeAstTypeName.ObjectExpression) {
                        // 瀵硅薄瑙ｆ瀯鍙傛暟锟?{ a, b }) => ...
                        // 闇€瑕佸皢 ObjectExpression 杞崲锟?ObjectPattern
                        params.push(SlimeCstToAstUtils.convertExpressionToPattern(assignmentAst) as any)
                    } else if (assignmentAst.type === SlimeAstTypeName.ArrayExpression) {
                        // 鏁扮粍瑙ｆ瀯鍙傛暟锟?[a, b]) => ...
                        // 闇€瑕佸皢 ArrayExpression 杞崲锟?ArrayPattern
                        params.push(SlimeCstToAstUtils.convertExpressionToPattern(assignmentAst) as any)
                    } else {
                        // 鍏朵粬澶嶆潅鎯呭喌锛屽皾璇曟彁鍙杋dentifier
                        const identifier = SlimeCstToAstUtils.findFirstIdentifierInExpression(child)
                        if (identifier) {
                            params.push(SlimeCstToAstUtils.createIdentifierAst(identifier) as any)
                        }
                    }
                }
            }

            if (params.length > 0) {
                return params
            }
        }

        // 鍥為€€锛氬皾璇曟煡鎵剧涓€涓猧dentifier
        const identifierCst = SlimeCstToAstUtils.findFirstIdentifierInExpression(expressionCst)
        if (identifierCst) {
            return [SlimeCstToAstUtils.createIdentifierAst(identifierCst) as any]
        }

        return []
    }

    /**
     * [TypeScript] 鍒涘缓 TSThisParameter AST
     *
     * TSThisParameter CST 缁撴瀯:
     *   - This (token with value "this")
     *   - TSTypeAnnotation
     *
     * 杞崲涓?AST:
     *   Identifier {
     *     name: "this",
     *     typeAnnotation: TSTypeAnnotation
     *   }
     */
    createTSThisParameterAst(cst: SubhutiCst): SlimeIdentifier {
        const children = cst.getChildren() || []

        let typeAnnotation: any = undefined
        let loc = cst.getLoc()

        for (const child of children) {
            if (child.getName() === 'TSTypeAnnotation') {
                typeAnnotation = SlimeCstToAstUtils.createTSTypeAnnotationAst(child)
            } else if (child.getValue() === 'this') {
                // This token - use its location for the identifier
                loc = child.getLoc() || loc
            }
        }

        const identifier: any = {
            type: SlimeAstTypeName.Identifier,
            name: 'this',
            loc,
        }

        if (typeAnnotation) {
            identifier.typeAnnotation = typeAnnotation
        }

        return identifier
    }
}

export const SlimeFunctionParameterCstToAst = new SlimeFunctionParameterCstToAstSingle()
