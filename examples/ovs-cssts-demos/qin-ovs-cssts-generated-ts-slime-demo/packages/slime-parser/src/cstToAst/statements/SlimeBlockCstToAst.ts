import {SubhutiCst} from "subhuti";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import {
     SlimeBlockStatement, type SlimeExportAllDeclaration,
    type SlimeExportDefaultDeclaration,
    type SlimeExportNamedDeclaration, SlimeExpressionStatement,
    SlimeFunctionDeclaration, SlimeFunctionExpression, SlimeAstTypeName, SlimeStatement,
    SlimeTokenCreateUtils, SlimeAstCreateUtils
} from "slime-ast";
import { SlimeTokenConsumer } from "../../SlimeTokenConsumer.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import {SlimeVariableCstToAstSingle} from "./SlimeVariableCstToAst.ts";

export class SlimeBlockCstToAstSingle {
    /**
     * 浠嶣lock CST鍒涘缓BlockStatement AST
     * Block: LBrace StatementList? RBrace
     */
    createBlockAst(cst: SubhutiCst): SlimeBlockStatement {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.Block?.name)

        // Block 鐨勭粨鏋勶細LBrace StatementList? RBrace
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        if (cst.getChildren()) {
            for (const child of cst.getChildren()) {
                if (child.getName() === 'LBrace' || child.getValue() === '{') {
                    lBraceToken = SlimeTokenCreateUtils.createLBraceToken(child.getLoc())
                } else if (child.getName() === 'RBrace' || child.getValue() === '}') {
                    rBraceToken = SlimeTokenCreateUtils.createRBraceToken(child.getLoc())
                }
            }
        }

        const statementListCst = cst.getChildren()?.find(
            child => child.getName() === SlimeParser.prototype.StatementList?.name
        )

        const statements = statementListCst ? SlimeCstToAstUtils.createStatementListAst(statementListCst) : []

        return SlimeAstCreateUtils.createBlockStatement(statements, cst.getLoc(), lBraceToken, rBraceToken)
    }


    /**
     * 鍒涘缓 BlockStatement AST
     * 澶勭悊涓ょ鎯呭喌锟?
     * 1. 鐩存帴锟?StatementList锛堟棫鐨勫疄鐜帮級
     * 2. 锟?BlockStatement锛岄渶瑕佹彁鍙栧唴閮ㄧ殑 Block -> StatementList
     */
    createBlockStatementAst(cst: SubhutiCst): SlimeBlockStatement {
        let statements: Array<SlimeStatement>

        // 濡傛灉锟?StatementList锛岀洿鎺ヨ浆锟?
        if (cst.getName() === SlimeParser.prototype.StatementList?.name) {
            statements = SlimeCstToAstUtils.createStatementListAst(cst)
        }
        // 濡傛灉锟?BlockStatement锛岄渶瑕佹彁锟?Block -> StatementList
        else if (cst.getName() === SlimeParser.prototype.BlockStatement?.name) {
            // BlockStatement -> Block -> StatementList
            const blockCst = cst.getChildren()?.[0]
            if (blockCst && blockCst.getName() === SlimeParser.prototype.Block?.name) {
                return this.createBlockAst(blockCst)
            } else {
                statements = []
            }
        } else {
            throw new Error(`Expected StatementList or BlockStatement, got ${cst.getName()}`)
        }

        const ast: SlimeBlockStatement = {
            type: SlimeParser.prototype.BlockStatement?.name as any,
            body: statements,
            loc: cst.getLoc()
        }
        return ast
    }


    createStatementListAst(cst: SubhutiCst): Array<SlimeStatement> {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.StatementList?.name);
        if (cst.getChildren()) {
            const statements = cst.getChildren().map(item => SlimeCstToAstUtils.createStatementListItemAst(item)).flat()
            return statements
        }
        return []
    }


    createStatementAst(cst: SubhutiCst): Array<SlimeStatement> {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.Statement?.name);
        const statements: SlimeStatement[] = cst.getChildren()
            .map(item => SlimeCstToAstUtils.createStatementDeclarationAst(item))
            .filter(stmt => stmt !== undefined)  // 杩囨护锟?undefined
        return statements
    }


    createStatementListItemAst(cst: SubhutiCst): Array<SlimeStatement> {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.StatementListItem?.name);
        const statements = cst.getChildren().map(item => {
            // 濡傛灉锟?Declaration锛岀洿鎺ュ锟?
            if (item.getName() === SlimeParser.prototype.Declaration?.name || item.getName() === 'Declaration') {
                return [SlimeCstToAstUtils.createDeclarationAst(item) as any]
            }

            // 濡傛灉锟?Statement锛岄渶瑕佺壒娈婂锟?FunctionExpression 锟?ClassExpression
            const statement = SlimeCstToAstUtils.createStatementAst(item)
            const result = statement.flat()

            // 妫€鏌ユ槸鍚︽槸鍛藉悕锟?FunctionExpression 锟?ClassExpression锛堝簲璇ヨ浆锟?Declaration锟?
            return result.map(stmt => {
                if (stmt.type === SlimeAstTypeName.ExpressionStatement) {
                    const expr = (stmt as SlimeExpressionStatement).expression

                    // 鍛藉悕锟?FunctionExpression 锟?FunctionDeclaration
                    if (expr.type === SlimeAstTypeName.FunctionExpression) {
                        const funcExpr = expr as SlimeFunctionExpression
                        if (funcExpr.id) {
                            return {
                                type: SlimeAstTypeName.FunctionDeclaration,
                                id: funcExpr.id,
                                params: funcExpr.params,
                                body: funcExpr.body,
                                generator: funcExpr.generator,
                                async: funcExpr.async,
                                loc: funcExpr.loc
                            } as SlimeFunctionDeclaration
                        }
                    }

                    // ClassExpression 锟?ClassDeclaration
                    if (expr.type === SlimeAstTypeName.ClassExpression) {
                        const classExpr = expr as any
                        if (classExpr.id) {
                            return {
                                type: SlimeAstTypeName.ClassDeclaration,
                                id: classExpr.id,
                                superClass: classExpr.superClass,
                                body: classExpr.body,
                                loc: classExpr.loc
                            } as any
                        }
                    }
                }
                return stmt
            })
        }).flat()
        return statements
    }


    /**
     * [鏍稿績鍒嗗彂鏂规硶] 鏍规嵁 CST 鑺傜偣绫诲瀷鍒涘缓瀵瑰簲锟?Statement/Declaration AST
     *
     * 瀛樺湪蹇呰鎬э細ECMAScript 璇硶锟?Statement 锟?Declaration 鏈夊绉嶅叿浣撶被鍨嬶紝
     * 闇€瑕佷竴涓粺涓€鐨勫垎鍙戞柟娉曟潵澶勭悊鍚勭璇彞鍜屽０鏄庯拷?
     *
     * 澶勭悊鐨勮妭鐐圭被鍨嬪寘鎷細
     * - Statement 鍖呰鑺傜偣 锟?閫掑綊澶勭悊瀛愯妭锟?
     * - BreakableStatement 锟?IterationStatement | SwitchStatement
     * - VariableStatement 锟?VariableDeclaration
     * - ExpressionStatement 锟?ExpressionStatement
     * - IfStatement, ForStatement, WhileStatement 绛夊叿浣撹锟?
     * - FunctionDeclaration, ClassDeclaration 绛夊０锟?
     */
    createStatementDeclarationAst(cst: SubhutiCst) {
        // Statement - 鍖呰鑺傜偣锛岄€掑綊澶勭悊瀛愯妭锟?
        if (cst.getName() === SlimeParser.prototype.Statement?.name || cst.getName() === 'Statement') {
            if (cst.getChildren() && cst.getChildren().length > 0) {
                return SlimeCstToAstUtils.createStatementDeclarationAst(cst.getChildren()[0])
            }
            return undefined
        }
        // BreakableStatement - 鍖呰鑺傜偣锛岄€掑綊澶勭悊瀛愯妭锟?
        else if (cst.getName() === SlimeParser.prototype.BreakableStatement?.name) {
            if (cst.getChildren() && cst.getChildren().length > 0) {
                return SlimeCstToAstUtils.createStatementDeclarationAst(cst.getChildren()[0])
            }
            return undefined
        }
        // IterationStatement - 寰幆璇彞鍖呰鑺傜偣
        else if (cst.getName() === SlimeParser.prototype.IterationStatement?.name) {
            if (cst.getChildren() && cst.getChildren().length > 0) {
                return SlimeCstToAstUtils.createStatementDeclarationAst(cst.getChildren()[0])
            }
            return undefined
        }
        // IfStatementBody - if/else 璇彞浣撳寘瑁呰妭鐐癸紝閫掑綊澶勭悊瀛愯妭锟?
        else if (cst.getName() === 'IfStatementBody') {
            if (cst.getChildren() && cst.getChildren().length > 0) {
                return SlimeCstToAstUtils.createStatementDeclarationAst(cst.getChildren()[0])
            }
            return undefined
        }
        // var 鍙橀噺澹版槑璇彞 (ES2025: VariableStatement)
        else if (cst.getName() === SlimeParser.prototype.VariableStatement?.name || cst.getName() === 'VariableStatement') {
            return SlimeCstToAstUtils.createVariableStatementAst(cst)
        }
        // 鍙橀噺澹版槑 (鐢ㄤ簬 for 寰幆锟?
        else if (cst.getName() === SlimeParser.prototype.VariableDeclaration?.name) {
            return SlimeCstToAstUtils.createVariableDeclarationAst(cst)
        }
        // 琛ㄨ揪寮忚锟?
        else if (cst.getName() === SlimeParser.prototype.ExpressionStatement?.name) {
            return SlimeCstToAstUtils.createExpressionStatementAst(cst)
        }
        // return 璇彞
        else if (cst.getName() === SlimeParser.prototype.ReturnStatement?.name) {
            return SlimeCstToAstUtils.createReturnStatementAst(cst)
        }
        // if 璇彞
        else if (cst.getName() === SlimeParser.prototype.IfStatement?.name) {
            return SlimeCstToAstUtils.createIfStatementAst(cst)
        }
        // for 璇彞
        else if (cst.getName() === SlimeParser.prototype.ForStatement?.name) {
            return SlimeCstToAstUtils.createForStatementAst(cst)
        }
        // for...in / for...of 璇彞
        else if (cst.getName() === SlimeParser.prototype.ForInOfStatement?.name) {
            return SlimeCstToAstUtils.createForInOfStatementAst(cst)
        }
        // while 璇彞
        else if (cst.getName() === SlimeParser.prototype.WhileStatement?.name) {
            return SlimeCstToAstUtils.createWhileStatementAst(cst)
        }
        // do...while 璇彞
        else if (cst.getName() === SlimeParser.prototype.DoWhileStatement?.name) {
            return SlimeCstToAstUtils.createDoWhileStatementAst(cst)
        }
        // 鍧楄锟?
        else if (cst.getName() === SlimeParser.prototype.BlockStatement?.name) {
            return SlimeCstToAstUtils.createBlockStatementAst(cst)
        }
        // switch 璇彞
        else if (cst.getName() === SlimeParser.prototype.SwitchStatement?.name) {
            return SlimeCstToAstUtils.createSwitchStatementAst(cst)
        }
        // try 璇彞
        else if (cst.getName() === SlimeParser.prototype.TryStatement?.name) {
            return SlimeCstToAstUtils.createTryStatementAst(cst)
        }
        // throw 璇彞
        else if (cst.getName() === SlimeParser.prototype.ThrowStatement?.name) {
            return SlimeCstToAstUtils.createThrowStatementAst(cst)
        }
        // break 璇彞
        else if (cst.getName() === SlimeParser.prototype.BreakStatement?.name) {
            return SlimeCstToAstUtils.createBreakStatementAst(cst)
        }
        // continue 璇彞
        else if (cst.getName() === SlimeParser.prototype.ContinueStatement?.name) {
            return SlimeCstToAstUtils.createContinueStatementAst(cst)
        }
        // 鏍囩璇彞
        else if (cst.getName() === SlimeParser.prototype.LabelledStatement?.name) {
            return SlimeCstToAstUtils.createLabelledStatementAst(cst)
        }
        // with 璇彞
        else if (cst.getName() === SlimeParser.prototype.WithStatement?.name) {
            return SlimeCstToAstUtils.createWithStatementAst(cst)
        }
        // debugger 璇彞
        else if (cst.getName() === SlimeParser.prototype.DebuggerStatement?.name) {
            return SlimeCstToAstUtils.createDebuggerStatementAst(cst)
        }
        // 绌鸿锟?
        else if (cst.getName() === SlimeParser.prototype.EmptyStatement?.name) {
            return SlimeCstToAstUtils.createEmptyStatementAst(cst)
        }
        // 鍑芥暟澹版槑
        else if (cst.getName() === SlimeParser.prototype.FunctionDeclaration?.name) {
            return SlimeCstToAstUtils.createFunctionDeclarationAst(cst)
        }
        // 绫诲０锟?
        else if (cst.getName() === SlimeParser.prototype.ClassDeclaration?.name) {
            return SlimeCstToAstUtils.createClassDeclarationAst(cst)
        }
    }

}

export const SlimeBlockCstToAst = new SlimeBlockCstToAstSingle()
