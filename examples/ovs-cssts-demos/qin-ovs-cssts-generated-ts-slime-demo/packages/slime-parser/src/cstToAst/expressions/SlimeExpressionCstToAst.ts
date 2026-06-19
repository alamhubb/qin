/**
 * ExpressionCstToAst - 鏍稿績琛ㄨ揪寮忚浆鎹紙Expression 璺敱鍜屾搷浣滅锛?
 */
import { SubhutiCst } from "subhuti";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { type SlimeExpression, SlimeAstTypeName, SlimeTokenCreateUtils, SlimeAstCreateUtils } from "slime-ast";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

export class SlimeExpressionCstToAstSingle {
    createExpressionAst(cst: SubhutiCst): SlimeExpression {
        const cached = SlimeCstToAstUtils.expressionAstCache.get(cst)
        if (cached) {
            return cached
        }
        const result = SlimeCstToAstUtils.createExpressionAstUncached(cst)
        SlimeCstToAstUtils.expressionAstCache.set(cst, result)
        return result
    }


    createExpressionAstUncached(cst: SubhutiCst): SlimeExpression {
        const astName = cst.getName()
        const isAstName = (prototypeName: string | undefined, fallbackName: string) =>
            astName === prototypeName || astName === fallbackName
        let left
        if (isAstName(SlimeParser.prototype.Expression?.name, 'Expression')) {
            // Expression 鍙兘鏄€楀彿琛ㄨ揪锟?(SequenceExpression)
            // 缁撴瀯: Expression -> AssignmentExpression | Expression, AssignmentExpression
            // 鏀堕泦鎵€鏈夎〃杈惧紡
            const expressions: SlimeExpression[] = []
            const commaTokens: any[] = []
            for (const child of cst.getChildren() || []) {
                if (child.getName() === 'Comma' || child.getValue() === ',') {
                    commaTokens.push(SlimeTokenCreateUtils.createCommaToken(child.getLoc()))
                    // 璺宠繃閫楀彿 token
                    continue
                }
                expressions.push(SlimeCstToAstUtils.createExpressionAst(child))
            }

            if (expressions.length === 1) {
                // 鍗曚釜琛ㄨ揪寮忥紝鐩存帴杩斿洖
                left = expressions[0]
            } else if (expressions.length > 1) {
                // 澶氫釜琛ㄨ揪寮忥紝鍒涘缓 SequenceExpression
                left = {
                    type: 'SequenceExpression',
                    expressions: expressions,
                    commaTokens: commaTokens,
                    loc: cst.getLoc()
                } as any
            } else {
                return SlimeAstCreateUtils.createIdentifier('', cst.getLoc()) as any
            }
        } else if (isAstName(SlimeParser.prototype.Statement?.name, 'Statement')) {
            left = SlimeCstToAstUtils.createStatementAst(cst)
        } else if (isAstName(SlimeParser.prototype.AssignmentExpression?.name, 'AssignmentExpression')) {
            left = SlimeCstToAstUtils.createAssignmentExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.ConditionalExpression?.name, 'ConditionalExpression')) {
            left = SlimeCstToAstUtils.createConditionalExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.LogicalORExpression?.name, 'LogicalORExpression')) {
            left = SlimeCstToAstUtils.createLogicalORExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.LogicalANDExpression?.name, 'LogicalANDExpression')) {
            left = SlimeCstToAstUtils.createLogicalANDExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.BitwiseORExpression?.name, 'BitwiseORExpression')) {
            left = SlimeCstToAstUtils.createBitwiseORExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.BitwiseXORExpression?.name, 'BitwiseXORExpression')) {
            left = SlimeCstToAstUtils.createBitwiseXORExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.BitwiseANDExpression?.name, 'BitwiseANDExpression')) {
            left = SlimeCstToAstUtils.createBitwiseANDExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.EqualityExpression?.name, 'EqualityExpression')) {
            left = SlimeCstToAstUtils.createEqualityExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.RelationalExpression?.name, 'RelationalExpression')) {
            left = SlimeCstToAstUtils.createRelationalExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.ShiftExpression?.name, 'ShiftExpression')) {
            left = SlimeCstToAstUtils.createShiftExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.AdditiveExpression?.name, 'AdditiveExpression')) {
            left = SlimeCstToAstUtils.createAdditiveExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.MultiplicativeExpression?.name, 'MultiplicativeExpression')) {
            left = SlimeCstToAstUtils.createMultiplicativeExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.UnaryExpression?.name, 'UnaryExpression')) {
            left = SlimeCstToAstUtils.createUnaryExpressionAst(cst)
        } else if (astName === 'PostfixExpression') {
            left = SlimeCstToAstUtils.createUpdateExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.UpdateExpression?.name, 'UpdateExpression')) {
            left = SlimeCstToAstUtils.createUpdateExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.LeftHandSideExpression?.name, 'LeftHandSideExpression')) {
            left = SlimeCstToAstUtils.createLeftHandSideExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.CallExpression?.name, 'CallExpression')) {
            left = SlimeCstToAstUtils.createCallExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.NewExpression?.name, 'NewExpression')) {
            left = SlimeCstToAstUtils.createNewExpressionAst(cst)
        } else if (astName === 'NewMemberExpressionArguments') {
            left = SlimeCstToAstUtils.createNewExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.MemberExpression?.name, 'MemberExpression')) {
            left = SlimeCstToAstUtils.createMemberExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.PrimaryExpression?.name, 'PrimaryExpression')) {
            left = SlimeCstToAstUtils.createPrimaryExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.YieldExpression?.name, 'YieldExpression')) {
            left = SlimeCstToAstUtils.createYieldExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.AwaitExpression?.name, 'AwaitExpression')) {
            left = SlimeCstToAstUtils.createAwaitExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.SuperProperty?.name, 'SuperProperty')) {
            left = SlimeCstToAstUtils.createSuperPropertyAst(cst)
        } else if (isAstName(SlimeParser.prototype.MetaProperty?.name, 'MetaProperty')) {
            left = SlimeCstToAstUtils.createMetaPropertyAst(cst)
        } else if (astName === 'ShortCircuitExpression') {
            // ES2020: ShortCircuitExpression = LogicalORExpression | CoalesceExpression
            // ShortCircuitExpression: LogicalANDExpression ShortCircuitExpressionTail?
            left = SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[0])

            // 妫€鏌ユ槸鍚︽湁 ShortCircuitExpressionTail (|| 杩愮畻锟?
            if (cst.getChildren().length > 1 && cst.getChildren()[1]) {
                const tailCst = cst.getChildren()[1]
                if (tailCst.getName() === 'ShortCircuitExpressionTail' ||
                    tailCst.getName() === 'LogicalORExpressionTail') {
                    // 澶勭悊灏鹃儴锛氬彲鑳芥槸 LogicalORExpressionTail 锟?CoalesceExpressionTail
                    left = SlimeCstToAstUtils.createShortCircuitExpressionTailAst(left, tailCst)
                }
            }
        } else if (astName === 'CoalesceExpression') {
            // ES2020: CoalesceExpression (澶勭悊 ?? 杩愮畻锟?
            left = SlimeCstToAstUtils.createCoalesceExpressionAst(cst)
        } else if (astName === 'ExponentiationExpression') {
            // ES2016: ExponentiationExpression (澶勭悊 ** 杩愮畻锟?
            left = SlimeCstToAstUtils.createExponentiationExpressionAst(cst)
        } else if (astName === 'CoverCallExpressionAndAsyncArrowHead') {
            // ES2017+: Cover grammar for CallExpression and async arrow function
            // In non-async-arrow context, this is a CallExpression
            left = SlimeCstToAstUtils.createCallExpressionAst(cst)
        } else if (astName === 'OptionalExpression') {
            // ES2020: Optional chaining (?.)
            left = SlimeCstToAstUtils.createOptionalExpressionAst(cst)
        } else if (isAstName(SlimeParser.prototype.ArrowFunction?.name, 'ArrowFunction')) {
            // 绠ご鍑芥暟
            left = SlimeCstToAstUtils.createArrowFunctionAst(cst)
        } else if (astName === 'AsyncArrowFunction') {
            // Async 绠ご鍑芥暟
            left = SlimeCstToAstUtils.createAsyncArrowFunctionAst(cst)
        } else if (isAstName(SlimeParser.prototype.ImportCall?.name, 'ImportCall')) {
            // ES2020: 鍔拷?import()
            left = SlimeCstToAstUtils.createImportCallAst(cst)
        } else if (astName === 'PrivateIdentifier') {
            // ES2022: PrivateIdentifier (e.g. #x in `#x in obj`)
            left = SlimeCstToAstUtils.createPrivateIdentifierAst(cst)
        } else if (astName === 'TSTypeAssertion') {
            // TypeScript: <Type>expression
            left = SlimeCstToAstUtils.createTSTypeAssertionAst(cst)
        } else {
            const firstChild = cst.getChildren()?.[0]
            if (firstChild) {
                return SlimeCstToAstUtils.createExpressionAst(firstChild)
            }
            return SlimeAstCreateUtils.createIdentifier('', cst.getLoc()) as any
        }
        return left
    }


    createAssignmentExpressionAst(cst: SubhutiCst): SlimeExpression {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.AssignmentExpression?.name);

        if (cst.getChildren().length === 1) {
            const child = cst.getChildren()[0]
            // 妫€鏌ユ槸鍚︽槸绠ご鍑芥暟
            if (child.getName() === SlimeParser.prototype.ArrowFunction?.name) {
                try {
                    return SlimeCstToAstUtils.createArrowFunctionAst(child)
                } catch (error: any) {
                    const recovered = child.getChildren()?.[0]
                    if (recovered) {
                        return SlimeCstToAstUtils.createExpressionAst(recovered)
                    }
                    return SlimeAstCreateUtils.createIdentifier('', child.getLoc()) as any
                }
            }
            // 鍚﹀垯浣滀负琛ㄨ揪寮忓锟?
            return SlimeCstToAstUtils.createExpressionAst(child)
        }

        // AssignmentExpression -> LeftHandSideExpression + Eq + AssignmentExpression
        // 锟?LeftHandSideExpression + AssignmentOperator + AssignmentExpression
        const leftCst = cst.getChildren()[0]
        const operatorCst = cst.getChildren()[1]
        const rightCst = cst.getChildren()[2]

        const left = SlimeCstToAstUtils.createExpressionAst(leftCst)
        const right = SlimeCstToAstUtils.createAssignmentExpressionAst(rightCst)
        // AssignmentOperator token may be nested under children[0].
        const operatorValue = (operatorCst.getChildren() && operatorCst.getChildren()[0]?.value) || operatorCst.getValue() || '='
        const operatorLoc = (operatorCst.getChildren() && operatorCst.getChildren()[0]?.loc) || operatorCst.getLoc()
        const operatorToken = SlimeTokenCreateUtils.createAssignmentOperatorToken(operatorValue, operatorLoc)

        return SlimeAstCreateUtils.createAssignmentExpression(
            operatorToken,
            left as any,
            right,
            cst.getLoc()
        )
    }

    /**
     * AssignmentOperator CST 锟?AST
     * AssignmentOperator -> *= | /= | %= | += | -= | <<= | >>= | >>>= | &= | ^= | |= | **= | &&= | ||= | ??=
     */
    createAssignmentOperatorAst(cst: SubhutiCst): string {
        const token = cst.getChildren()?.[0]
        return token?.value || '='
    }

    createConditionalExpressionAst(cst: SubhutiCst): SlimeExpression {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ConditionalExpression?.name);
        const firstChild = cst.getChildren()[0]
        let test = SlimeCstToAstUtils.createExpressionAst(firstChild)
        let alternate
        let consequent

        // Token fields
        let questionToken: any = undefined
        let colonToken: any = undefined

        if (cst.getChildren().length === 1) {
            return SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[0])
        } else {
            // CST children: [LogicalORExpression, Question, AssignmentExpression, Colon, AssignmentExpression]
            const questionCst = cst.getChildren()[1]
            const colonCst = cst.getChildren()[3]

            if (questionCst && (questionCst.name === 'Question' || questionCst.value === '?')) {
                questionToken = SlimeTokenCreateUtils.createQuestionToken(questionCst.loc)
            }
            if (colonCst && (colonCst.name === 'Colon' || colonCst.value === ':')) {
                colonToken = SlimeTokenCreateUtils.createColonToken(colonCst.loc)
            }

            consequent = SlimeCstToAstUtils.createAssignmentExpressionAst(cst.getChildren()[2])
            alternate = SlimeCstToAstUtils.createAssignmentExpressionAst(cst.getChildren()[4])
        }

        return SlimeAstCreateUtils.createConditionalExpression(test, consequent, alternate, cst.getLoc(), questionToken, colonToken)
    }


    /**
     * 鍒涘缓 CoalesceExpression AST锛圗S2020锟?
     * 澶勭悊 ?? 绌哄€煎悎骞惰繍绠楃
     */
    createCoalesceExpressionAst(cst: SubhutiCst): SlimeExpression {
        // CoalesceExpression -> BitwiseORExpression ( ?? BitwiseORExpression )*
        if (cst.getChildren().length === 1) {
            return SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[0])
        }

        // 鏈夊涓瓙鑺傜偣锛屾瀯寤哄乏缁撳悎鐨勯€昏緫琛ㄨ揪锟?
        let left = SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[0])
        for (let i = 1; i < cst.getChildren().length; i += 2) {
            const operatorNode = cst.getChildren()[i]  // ?? token
            const operatorLeaf = operatorNode.getChildren()?.[0] ?? operatorNode
            const operatorValue = operatorLeaf.value ?? operatorNode.getValue() ?? '??'
            const operatorLoc = operatorLeaf.loc ?? operatorNode.getLoc()
            const operatorToken = SlimeTokenCreateUtils.createLogicalOperatorToken(operatorValue as any, operatorLoc)
            const right = SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[i + 1])
            left = {
                type: SlimeAstTypeName.LogicalExpression,
                operator: operatorToken,
                left: left,
                right: right
            } as any
        }
        return left
    }


    /**
     * CoalesceExpressionHead CST 杞?AST
     * CoalesceExpressionHead -> CoalesceExpression | BitwiseORExpression
     */
    createCoalesceExpressionHeadAst(cst: SubhutiCst): SlimeExpression {
        const firstChild = cst.getChildren()?.[0]
        if (firstChild) {
            return SlimeCstToAstUtils.createExpressionAst(firstChild)
        }
        throw new Error('CoalesceExpressionHead has no children')
    }


    /**
     * ShortCircuitExpression CST 锟?AST锛堥€忎紶锟?
     * ShortCircuitExpression -> LogicalORExpression | CoalesceExpression
     */
    createShortCircuitExpressionAst(cst: SubhutiCst): SlimeExpression {
        const firstChild = cst.getChildren()?.[0]
        if (firstChild) {
            return SlimeCstToAstUtils.createExpressionAst(firstChild)
        }
        throw new Error('ShortCircuitExpression has no children')
    }

    /**
     * 澶勭悊 ShortCircuitExpressionTail (|| 锟??? 杩愮畻绗︾殑灏鹃儴)
     * CST 缁撴瀯锛歋hortCircuitExpressionTail -> LogicalORExpressionTail | CoalesceExpressionTail
     * LogicalORExpressionTail -> LogicalOr LogicalANDExpression LogicalORExpressionTail?
     */
    createShortCircuitExpressionTailAst(left: SlimeExpression, tailCst: SubhutiCst): SlimeExpression {
        const tailChildren = tailCst.getChildren() || []

        // 濡傛灉锟?ShortCircuitExpressionTail锛岃幏鍙栧唴閮ㄧ殑 tail
        if (tailCst.getName() === 'ShortCircuitExpressionTail' && tailChildren.length > 0) {
            const innerTail = tailChildren[0]
            return SlimeCstToAstUtils.createShortCircuitExpressionTailAst(left, innerTail)
        }

        // LogicalORExpressionTail: (LogicalOr LogicalANDExpression)+
        // 缁撴瀯鏄钩鍧︾殑锛歔LogicalOr, expr, LogicalOr, expr, ...]
        if (tailCst.getName() === 'LogicalORExpressionTail') {
            let result = left

            // 寰幆澶勭悊 (operator, operand) 锟?
            for (let i = 0; i < tailChildren.length; i += 2) {
                const operatorNode = tailChildren[i]
                const operatorLeaf = operatorNode.getChildren()?.[0] ?? operatorNode
                const operatorValue = operatorLeaf.value ?? operatorNode.getValue() ?? '||'
                const operatorLoc = operatorLeaf.loc ?? operatorNode.getLoc()
                const operatorToken = SlimeTokenCreateUtils.createLogicalOperatorToken(operatorValue as any, operatorLoc)

                const rightCst = tailChildren[i + 1]
                if (!rightCst) break

                const right = SlimeCstToAstUtils.createExpressionAst(rightCst)

                result = {
                    type: SlimeAstTypeName.LogicalExpression,
                    operator: operatorToken,
                    left: result,
                    right: right,
                    loc: tailCst.getLoc()
                } as any
            }

            return result
        }

        // CoalesceExpressionTail: (?? BitwiseORExpression)+
        // 缁撴瀯鏄钩鍧︾殑锛歔??, expr, ??, expr, ...]
        if (tailCst.getName() === 'CoalesceExpressionTail') {
            let result = left

            for (let i = 0; i < tailChildren.length; i += 2) {
                const operatorNode = tailChildren[i]
                const operatorLeaf = operatorNode.getChildren()?.[0] ?? operatorNode
                const operatorValue = operatorLeaf.value ?? operatorNode.getValue() ?? '??'
                const operatorLoc = operatorLeaf.loc ?? operatorNode.getLoc()
                const operatorToken = SlimeTokenCreateUtils.createLogicalOperatorToken(operatorValue as any, operatorLoc)

                const rightCst = tailChildren[i + 1]
                if (!rightCst) break

                const right = SlimeCstToAstUtils.createExpressionAst(rightCst)

                result = {
                    type: SlimeAstTypeName.LogicalExpression,
                    operator: operatorToken,
                    left: result,
                    right: right,
                    loc: tailCst.getLoc()
                } as any
            }

            return result
        }

        // 鏈煡锟?tail 绫诲瀷锛岃繑鍥炲乏鎿嶄綔锟?
        console.warn('Unknown ShortCircuitExpressionTail type:', tailCst.getName())
        return left
    }



}

export const SlimeExpressionCstToAst = new SlimeExpressionCstToAstSingle()
