import {SubhutiCst} from "subhuti";
import {

    SlimeBlockStatement, SlimeExpressionStatement,
    SlimeFunctionDeclaration,
    SlimeFunctionParam,
    SlimeIdentifier,
    SlimeAstTypeName, type SlimePattern, SlimeReturnStatement, SlimeTokenCreateUtils, type SlimeVariableDeclarator,
    SlimeAstCreateUtils
} from "slime-ast";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeTokenConsumer } from "../../SlimeTokenConsumer.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import {SlimeVariableCstToAstSingle} from "./SlimeVariableCstToAst.ts";

/**
 * OtherStatementCstToAst - try/switch/break/continue/label 绛夎浆鎹?
 */
export class SlimeOtherStatementCstToAstSingle {

    createReturnStatementAst(cst: SubhutiCst): SlimeReturnStatement {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ReturnStatement?.name);

        // return 璇彞鍙兘鏈夋垨娌℃湁琛ㄨ揪锟?
        // children[0] = ReturnTok
        // children[1] = Expression? | Semicolon | SemicolonASI
        let argument: any = null
        let returnToken: any = undefined
        let semicolonToken: any = undefined

        // 鎻愬彇 return token
        const returnCst = cst.getChildren()[0]
        if (returnCst && (returnCst.name === 'Return' || returnCst.value === 'return')) {
            returnToken = SlimeTokenCreateUtils.createReturnToken(returnCst.loc)
        }

        if (cst.getChildren().length > 1) {
            for (let i = 1; i < cst.getChildren().length; i++) {
                const child = cst.getChildren()[i]
                // 璺宠繃鍒嗗彿鐩稿叧鑺傜偣
                if (child.getName() === 'Semicolon' || child.getValue() === ';') {
                    semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(child.getLoc())
                } else if (child.getName() === 'SemicolonASI') {
                    const semicolonCst = child.getChildren()?.find(c => c.name === 'Semicolon' || c.value === ';')
                    if (semicolonCst) {
                        semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(semicolonCst.getLoc())
                    }
                } else if (!argument) {
                    argument = SlimeCstToAstUtils.createExpressionAst(child)
                }
            }
        }

        return SlimeAstCreateUtils.createReturnStatement(argument, cst.getLoc(), returnToken, semicolonToken)
    }


    /**
     * 鍒涘缓 break 璇彞 AST
     */
    createBreakStatementAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.BreakStatement?.name);
        // BreakStatement: break Identifier? ;

        let breakToken: any = undefined
        let semicolonToken: any = undefined
        let label: any = null

        for (const child of cst.getChildren() || []) {
            if (child.getName() === 'Break' || child.getValue() === 'break') {
                breakToken = SlimeTokenCreateUtils.createBreakToken(child.getLoc())
            } else if (child.getName() === 'Semicolon' || child.getValue() === ';') {
                semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(child.getLoc())
            } else if (child.getName() === 'SemicolonASI') {
                const semicolonCst = child.getChildren()?.find(c => c.name === 'Semicolon' || c.value === ';')
                if (semicolonCst) {
                    semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(semicolonCst.getLoc())
                }
            } else if (child.getName() === SlimeParser.prototype.LabelIdentifier?.name || child.getName() === 'LabelIdentifier') {
                label = SlimeCstToAstUtils.createLabelIdentifierAst(child)
            } else if (child.getName() === SlimeParser.prototype.IdentifierName?.name) {
                label = SlimeCstToAstUtils.createIdentifierNameAst(child)
            } else if (child.getName() === SlimeTokenConsumer.prototype.IdentifierName?.name) {
                label = SlimeCstToAstUtils.createIdentifierAst(child)
            }
        }

        return SlimeAstCreateUtils.createBreakStatement(label, cst.getLoc(), breakToken, semicolonToken)
    }


    /**
     * 鍒涘缓 continue 璇彞 AST
     */
    createContinueStatementAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ContinueStatement?.name);
        // ContinueStatement: continue Identifier? ;

        let continueToken: any = undefined
        let semicolonToken: any = undefined
        let label: any = null

        for (const child of cst.getChildren() || []) {
            if (child.getName() === 'Continue' || child.getValue() === 'continue') {
                continueToken = SlimeTokenCreateUtils.createContinueToken(child.getLoc())
            } else if (child.getName() === 'Semicolon' || child.getValue() === ';') {
                semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(child.getLoc())
            } else if (child.getName() === 'SemicolonASI') {
                const semicolonCst = child.getChildren()?.find(c => c.name === 'Semicolon' || c.value === ';')
                if (semicolonCst) {
                    semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(semicolonCst.getLoc())
                }
            } else if (child.getName() === SlimeParser.prototype.LabelIdentifier?.name || child.getName() === 'LabelIdentifier') {
                label = SlimeCstToAstUtils.createLabelIdentifierAst(child)
            } else if (child.getName() === SlimeParser.prototype.IdentifierName?.name) {
                label = SlimeCstToAstUtils.createIdentifierNameAst(child)
            } else if (child.getName() === SlimeTokenConsumer.prototype.IdentifierName?.name) {
                label = SlimeCstToAstUtils.createIdentifierAst(child)
            }
        }

        return SlimeAstCreateUtils.createContinueStatement(label, cst.getLoc(), continueToken, semicolonToken)
    }


    /**
     * 鍒涘缓 try 璇彞 AST
     */
    createTryStatementAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.TryStatement?.name);
        // TryStatement: TryTok Block (Catch Finally? | Finally)

        let tryToken: any = undefined
        let finallyToken: any = undefined

        for (const child of cst.getChildren()) {
            if (!child) continue
            if (child.getName() === 'Try' || child.getValue() === 'try') {
                tryToken = SlimeTokenCreateUtils.createTryToken(child.getLoc())
            } else if (child.getName() === 'Finally' || child.getValue() === 'finally') {
                const finallyCst = child.getChildren()?.find(c => c.name === 'Finally' || c.value === 'finally')
                if (finallyCst) {
                    finallyToken = SlimeTokenCreateUtils.createFinallyToken(finallyCst.loc)
                } else {
                    finallyToken = SlimeTokenCreateUtils.createFinallyToken(child.getLoc())
                }
            }
        }

        const blockCst = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.Block?.name)
        const catchCst = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.Catch?.name)
        const finallyCst = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.Finally?.name)

        const block = blockCst ? SlimeCstToAstUtils.createBlockAst(blockCst) : null
        const handler = catchCst ? SlimeCstToAstUtils.createCatchAst(catchCst) : null
        const finalizer = finallyCst ? SlimeCstToAstUtils.createFinallyAst(finallyCst) : null

        return SlimeAstCreateUtils.createTryStatement(block, handler, finalizer, cst.getLoc(), tryToken, finallyToken)
    }


    /**
     * Catch CST 锟?CatchClause AST
     * Catch -> catch ( CatchParameter ) Block | catch Block
     */
    createCatchAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.Catch?.name);
        // Catch: CatchTok LParen CatchParameter RParen Block

        let catchToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined

        for (const child of cst.getChildren()) {
            if (!child) continue
            if (child.getName() === 'Catch' || child.getValue() === 'catch') {
                catchToken = SlimeTokenCreateUtils.createCatchToken(child.getLoc())
            } else if (child.getName() === 'LParen' || child.getValue() === '(') {
                lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
            } else if (child.getName() === 'RParen' || child.getValue() === ')') {
                rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
            }
        }

        const paramCst = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.CatchParameter?.name)
        const blockCst = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.Block?.name)

        const param = paramCst ? SlimeCstToAstUtils.createCatchParameterAst(paramCst) : null
        const body = blockCst ? SlimeCstToAstUtils.createBlockAst(blockCst) : SlimeAstCreateUtils.createBlockStatement([])

        return SlimeAstCreateUtils.createCatchClause(body, param, cst.getLoc(), catchToken, lParenToken, rParenToken)
    }


    /**
     * 鍒涘缓 CatchParameter AST
     */
    createCatchParameterAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.CatchParameter?.name);
        // CatchParameter: BindingIdentifier | BindingPattern
        const first = cst.getChildren()[0]

        if (first.getName() === SlimeParser.prototype.BindingIdentifier?.name) {
            return SlimeCstToAstUtils.createBindingIdentifierAst(first)
        } else if (first.getName() === SlimeParser.prototype.BindingPattern?.name) {
            return SlimeCstToAstUtils.createBindingPatternAst(first)
        }

        return null
    }


    /**
     * 鍒涘缓 Finally 瀛愬彞 AST
     */
    createFinallyAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.Finally?.name);
        // Finally: FinallyTok Block

        const blockCst = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.Block?.name)
        return blockCst ? SlimeCstToAstUtils.createBlockAst(blockCst) : null
    }


    /**
     * 鍒涘缓 throw 璇彞 AST
     */
    createThrowStatementAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ThrowStatement?.name);
        // ThrowStatement: throw Expression ;

        let throwToken: any = undefined
        let semicolonToken: any = undefined
        let argument: any = null

        for (const child of cst.getChildren() || []) {
            if (child.getName() === 'Throw' || child.getValue() === 'throw') {
                throwToken = SlimeTokenCreateUtils.createThrowToken(child.getLoc())
            } else if (child.getName() === 'Semicolon' || child.getValue() === ';') {
                semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(child.getLoc())
            } else if (child.getName() === 'SemicolonASI') {
                const semicolonCst = child.getChildren()?.find(c => c.name === 'Semicolon' || c.value === ';')
                if (semicolonCst) {
                    semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(semicolonCst.getLoc())
                }
            } else if (child.getName() === SlimeParser.prototype.Expression?.name || child.getName() === 'Expression') {
                argument = SlimeCstToAstUtils.createExpressionAst(child)
            }
        }

        return SlimeAstCreateUtils.createThrowStatement(argument, cst.getLoc(), throwToken, semicolonToken)
    }


    createExpressionStatementAst(cst: SubhutiCst): SlimeExpressionStatement {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ExpressionStatement?.name);

        let semicolonToken: any = undefined
        let expression: any = null

        for (const child of cst.getChildren() || []) {
            if (child.getName() === 'Semicolon' || child.getValue() === ';') {
                semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(child.getLoc())
            } else if (child.getName() === 'SemicolonASI') {
                const semicolonCst = child.getChildren()?.find(c => c.name === 'Semicolon' || c.value === ';')
                if (semicolonCst) {
                    semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(semicolonCst.getLoc())
                }
            } else if (child.getName() === SlimeParser.prototype.Expression?.name ||
                child.getName() === 'Expression' ||
                !expression) {
                expression = SlimeCstToAstUtils.createExpressionAst(child)
            }
        }

        return SlimeAstCreateUtils.createExpressionStatement(expression, cst.getLoc(), semicolonToken)
    }


    /**
     * 鍒涘缓绌鸿锟?AST
     */
    createEmptyStatementAst(cst: SubhutiCst): any {
        // 鍏煎 EmptyStatement 鍜屾棫锟?NotEmptySemicolon
        // SlimeCstToAstUtils.checkCstName(cst, Es2025Parser.prototype.EmptyStatement?.name);

        let semicolonToken: any = undefined

        // EmptyStatement 鍙兘鐩存帴锟?Semicolon token
        if (cst.getValue() === ';' || cst.getName() === SlimeTokenConsumer.prototype.Semicolon?.name) {
            semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(cst.getLoc())
        } else {
            // 锟?semicolon token
            const semicolonCst = cst.getChildren()?.find(ch => ch.name === 'Semicolon' || ch.value === ';')
            if (semicolonCst) {
                semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(semicolonCst.getLoc())
            }
        }

        return SlimeAstCreateUtils.createEmptyStatement(cst.getLoc(), semicolonToken)
    }


    /**
     * SemicolonASI CST 锟?AST
     * 澶勭悊鑷姩鍒嗗彿鎻掑叆
     */
    createSemicolonASIAst(cst: SubhutiCst): any {
        // ASI 涓嶄骇鐢熷疄闄呯殑 AST 鑺傜偣锛岃繑锟?null
        return null
    }


    /**
     * 鍒涘缓 debugger 璇彞 AST
     */
    createDebuggerStatementAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.DebuggerStatement?.name);

        let debuggerToken: any = undefined
        let semicolonToken: any = undefined

        for (const child of cst.getChildren() || []) {
            if (child.getName() === 'Debugger' || child.getValue() === 'debugger') {
                debuggerToken = SlimeTokenCreateUtils.createDebuggerToken(child.getLoc())
            } else if (child.getName() === 'Semicolon' || child.getValue() === ';') {
                semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(child.getLoc())
            } else if (child.getName() === 'SemicolonASI') {
                const semicolonCst = child.getChildren()?.find(c => c.name === 'Semicolon' || c.value === ';')
                if (semicolonCst) {
                    semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(semicolonCst.getLoc())
                }
            }
        }

        return SlimeAstCreateUtils.createDebuggerStatement(cst.getLoc(), debuggerToken, semicolonToken)
    }


    /**
     * 鍒涘缓鏍囩璇彞 AST
     * ES2025: LabelledStatement -> LabelIdentifier : LabelledItem
     * LabelledItem -> Statement | FunctionDeclaration
     */
    createLabelledStatementAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.LabelledStatement?.name);

        let label: any = null
        let body: any = null
        let colonToken: any = undefined

        if (cst.getChildren() && cst.getChildren().length > 0) {
            for (const child of cst.getChildren()) {
                if (!child) continue
                const name = child.getName()

                // Skip tokens (Colon)
                if (child.getValue() === ':' || name === 'Colon') {
                    colonToken = SlimeTokenCreateUtils.createColonToken(child.getLoc())
                    continue
                }

                // LabelIdentifier -> Identifier | yield | await
                if (name === SlimeParser.prototype.LabelIdentifier?.name || name === 'LabelIdentifier') {
                    label = SlimeCstToAstUtils.createLabelIdentifierAst(child)
                    continue
                }

                // LabelledItem -> Statement | FunctionDeclaration
                if (name === SlimeParser.prototype.LabelledItem?.name || name === 'LabelledItem') {
                    // LabelledItem 鍐呴儴锟?Statement 锟?FunctionDeclaration
                    const itemChild = child.getChildren()?.[0]
                    if (itemChild) {
                        // 浣跨敤 createStatementDeclarationAst 鑰屼笉锟?createStatementAst
                        // 鍥犱负 LabelledItem 鍙兘鐩存帴鍖呭惈 FunctionDeclaration
                        body = SlimeCstToAstUtils.createStatementDeclarationAst(itemChild)
                    }
                    continue
                }

                // 鏃х増鍏煎锛氱洿鎺ユ槸 Statement
                if (name === SlimeParser.prototype.Statement?.name || name === 'Statement') {
                    body = SlimeCstToAstUtils.createStatementDeclarationAst(child)
                    continue
                }

                // 鏃х増鍏煎锛氱洿鎺ユ槸 Identifier
                if (name === SlimeParser.prototype.IdentifierName?.name) {
                    label = SlimeCstToAstUtils.createIdentifierNameAst(child)
                    continue
                }
                if (name === SlimeTokenConsumer.prototype.IdentifierName?.name) {
                    label = SlimeCstToAstUtils.createIdentifierAst(child)
                    continue
                }
            }
        }

        return {
            type: SlimeAstTypeName.LabeledStatement,
            label: label,
            body: body,
            colonToken: colonToken,
            loc: cst.getLoc()
        }
    }


    /**
     * LabelledItem CST 锟?AST锛堥€忎紶锟?
     * LabelledItem -> Statement | FunctionDeclaration
     */
    createLabelledItemAst(cst: SubhutiCst): any {
        const firstChild = cst.getChildren()?.[0]
        if (firstChild) {
            return SlimeCstToAstUtils.createStatementDeclarationAst(firstChild)
        }
        throw new Error('LabelledItem has no children')
    }


    /**
     * 鍒涘缓 with 璇彞 AST
     * WithStatement: with ( Expression ) Statement
     */
    createWithStatementAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.WithStatement?.name);

        let object: any = null
        let body: any = null
        let withToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined

        for (const child of cst.getChildren() || []) {
            if (child.getName() === 'With' || child.getValue() === 'with') {
                withToken = child
            } else if (child.getName() === 'LParen' || child.getValue() === '(') {
                lParenToken = child
            } else if (child.getName() === 'RParen' || child.getValue() === ')') {
                rParenToken = child
            } else if (child.getName() === SlimeParser.prototype.Expression?.name || child.getName() === 'Expression') {
                object = SlimeCstToAstUtils.createExpressionAst(child)
            } else if (child.getName() === SlimeParser.prototype.Statement?.name || child.getName() === 'Statement') {
                // createStatementAst 杩斿洖鏁扮粍锛屽彇绗竴涓厓锟?
                const bodyArray = SlimeCstToAstUtils.createStatementAst(child)
                body = Array.isArray(bodyArray) && bodyArray.length > 0 ? bodyArray[0] : bodyArray
            }
        }

        return {
            type: SlimeAstTypeName.WithStatement,
            object,
            body,
            withToken,
            lParenToken,
            rParenToken,
            loc: cst.getLoc()
        }
    }


}

export const SlimeOtherStatementCstToAst = new SlimeOtherStatementCstToAstSingle()
