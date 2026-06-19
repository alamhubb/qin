/**
 * ControlFlowCstToAst - if/for/while/do-while 杞崲
 */
import { SubhutiCst } from "subhuti";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import {

    SlimeAstTypeName,
    SlimeTokenCreateUtils,
    type SlimeVariableDeclaration,
    type SlimeVariableDeclarator,
    SlimeAstCreateUtils
} from "slime-ast";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import {SlimeVariableCstToAstSingle} from "./SlimeVariableCstToAst.ts";

export class SlimeControlFlowCstToAstSingle {


    // ==================== 璇彞鐩稿叧杞崲鏂规硶 ====================

    /**
     * BreakableStatement CST 锟?AST锛堥€忎紶锟?
     * BreakableStatement -> IterationStatement | SwitchStatement
     */
    createBreakableStatementAst(cst: SubhutiCst): any {
        const firstChild = cst.getChildren()?.[0]
        if (firstChild) {
            return SlimeCstToAstUtils.createStatementDeclarationAst(firstChild)
        }
        throw new Error('BreakableStatement has no children')
    }

    /**
     * IterationStatement CST 锟?AST锛堥€忎紶锟?
     * IterationStatement -> DoWhileStatement | WhileStatement | ForStatement | ForInOfStatement
     */
    createIterationStatementAst(cst: SubhutiCst): any {
        const firstChild = cst.getChildren()?.[0]
        if (firstChild) {
            return SlimeCstToAstUtils.createStatementDeclarationAst(firstChild)
        }
        throw new Error('IterationStatement has no children')
    }


    /**
     * 鍒涘缓 if 璇彞 AST
     * if (test) consequent [else alternate]
     * ES2025: if ( Expression ) IfStatementBody [else IfStatementBody]
     */
    createIfStatementAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.IfStatement?.name);

        let test: any = null
        let consequent: any = null
        let alternate: any = null
        let ifToken: any = undefined
        let elseToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined

        const children = cst.getChildren() || []
        let foundElse = false

        for (const child of children) {
            if (!child) continue
            const name = child.getName()

            // if token
            if (name === 'If' || child.getValue() === 'if') {
                ifToken = SlimeTokenCreateUtils.createIfToken(child.getLoc())
                continue
            }
            // LParen token
            if (name === 'LParen' || child.getValue() === '(') {
                lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
                continue
            }
            // RParen token
            if (name === 'RParen' || child.getValue() === ')') {
                rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
                continue
            }

            // else token
            if (name === 'Else' || child.getValue() === 'else') {
                elseToken = SlimeTokenCreateUtils.createElseToken(child.getLoc())
                foundElse = true
                continue
            }

            // Expression (test condition)
            if (name === SlimeParser.prototype.Expression?.name || name === 'Expression') {
                test = SlimeCstToAstUtils.createExpressionAst(child)
                continue
            }

            // IfStatementBody
            if (name === SlimeParser.prototype.IfStatementBody?.name || name === 'IfStatementBody') {
                const body = SlimeCstToAstUtils.createIfStatementBodyAst(child)
                if (!foundElse) {
                    consequent = body
                } else {
                    alternate = body
                }
                continue
            }

            // Legacy: 鐩存帴锟?Statement
            if (name === SlimeParser.prototype.Statement?.name || name === 'Statement') {
                const stmts = SlimeCstToAstUtils.createStatementAst(child)
                const body = Array.isArray(stmts) ? stmts[0] : stmts
                if (!foundElse) {
                    consequent = body
                } else {
                    alternate = body
                }
                continue
            }
        }

        return SlimeAstCreateUtils.createIfStatement(test, consequent, alternate, cst.getLoc(), ifToken, elseToken, lParenToken, rParenToken)
    }

    /**
     * 鍒涘缓 IfStatementBody AST
     * IfStatementBody: Statement | FunctionDeclaration
     */
    createIfStatementBodyAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        for (const child of children) {
            if (!child) continue
            const name = child.getName()

            if (name === SlimeParser.prototype.Statement?.name || name === 'Statement') {
                const stmts = SlimeCstToAstUtils.createStatementAst(child)
                return Array.isArray(stmts) ? stmts[0] : stmts
            }

            if (name === SlimeParser.prototype.FunctionDeclaration?.name || name === 'FunctionDeclaration') {
                return SlimeCstToAstUtils.createFunctionDeclarationAst(child)
            }
        }

        // 濡傛灉娌℃湁鎵惧埌瀛愯妭鐐癸紝灏濊瘯鐩存帴澶勭悊
        return SlimeCstToAstUtils.createStatementDeclarationAst(cst)
    }



    /**
     * 鍒涘缓 switch 璇彞 AST
     * SwitchStatement: switch ( Expression ) CaseBlock
     */
    createSwitchStatementAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.SwitchStatement?.name);

        let switchToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        for (const child of cst.getChildren()) {
            if (!child) continue
            if (child.getName() === 'Switch' || child.getValue() === 'switch') {
                switchToken = SlimeTokenCreateUtils.createSwitchToken(child.getLoc())
            } else if (child.getName() === 'LParen' || child.getValue() === '(') {
                lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
            } else if (child.getName() === 'RParen' || child.getValue() === ')') {
                rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
            }
        }

        // 鎻愬彇 discriminant锛堝垽鏂〃杈惧紡锟?
        const discriminantCst = cst.getChildren()?.find(ch => ch.name === SlimeParser.prototype.Expression?.name)
        const discriminant = discriminantCst ? SlimeCstToAstUtils.createExpressionAst(discriminantCst) : null

        // 鎻愬彇 cases锛堜粠 CaseBlock 涓級
        const caseBlockCst = cst.getChildren()?.find(ch => ch.name === SlimeParser.prototype.CaseBlock?.name)
        const cases = caseBlockCst ? SlimeCstToAstUtils.extractCasesFromCaseBlock(caseBlockCst) : []

        // 锟?CaseBlock 鎻愬彇 brace tokens
        if (caseBlockCst && caseBlockCst.getChildren()) {
            const lBraceCst = caseBlockCst.getChildren().find(ch => ch.name === 'LBrace' || ch.value === '{')
            const rBraceCst = caseBlockCst.getChildren().find(ch => ch.name === 'RBrace' || ch.value === '}')
            if (lBraceCst) lBraceToken = SlimeTokenCreateUtils.createLBraceToken(lBraceCst.getLoc())
            if (rBraceCst) rBraceToken = SlimeTokenCreateUtils.createRBraceToken(rBraceCst.getLoc())
        }

        return SlimeAstCreateUtils.createSwitchStatement(
            discriminant, cases, cst.getLoc(),
            switchToken, lParenToken, rParenToken, lBraceToken, rBraceToken
        )
    }


    /**
     * CaseClause CST 锟?AST
     * CaseClause -> case Expression : StatementList?
     */
    createCaseClauseAst(cst: SubhutiCst): any {
        return SlimeCstToAstUtils.createSwitchCaseAst(cst)
    }

    /**
     * DefaultClause CST 锟?AST
     * DefaultClause -> default : StatementList?
     */
    createDefaultClauseAst(cst: SubhutiCst): any {
        return SlimeCstToAstUtils.createSwitchCaseAst(cst)
    }

    /**
     * CaseClauses CST 锟?AST
     * CaseClauses -> CaseClause+
     */
    createCaseClausesAst(cst: SubhutiCst): any[] {
        const cases: any[] = []
        for (const child of cst.getChildren() || []) {
            if (child.getName() === SlimeParser.prototype.CaseClause?.name || child.getName() === 'CaseClause') {
                cases.push(SlimeCstToAstUtils.createSwitchCaseAst(child))
            }
        }
        return cases
    }

    /**
     * CaseBlock CST 锟?AST
     * CaseBlock -> { CaseClauses? DefaultClause? CaseClauses? }
     */
    createCaseBlockAst(cst: SubhutiCst): any[] {
        return SlimeCstToAstUtils.extractCasesFromCaseBlock(cst)
    }


    /**
     * [AST 绫诲瀷鏄犲皠] CaseClause/DefaultClause CST 锟?SwitchCase AST
     *
     * 瀛樺湪蹇呰鎬э細CST 锟?case 锟?default 鏄垎寮€鐨勮鍒欙紙CaseClause/DefaultClause锛夛紝
     * 锟?ESTree AST 缁熶竴浣跨敤 SwitchCase 绫诲瀷锛岄€氳繃 test 鏄惁锟?null 鍖哄垎锟?
     *
     * CaseClause: case Expression : StatementList?
     * DefaultClause: default : StatementList?
     * @internal
     */
    createSwitchCaseAst(cst: SubhutiCst): any {
        let test = null
        let consequent: any[] = []
        let caseToken: any = undefined
        let defaultToken: any = undefined
        let colonToken: any = undefined

        if (cst.getName() === SlimeParser.prototype.CaseClause?.name) {
            // CaseClause 缁撴瀯锟?
            // children[0]: CaseTok
            // children[1]: Expression - test
            // children[2]: Colon
            // children[3]: StatementList锛堝彲閫夛級

            for (const child of cst.getChildren() || []) {
                if (child.getName() === 'Case' || child.getValue() === 'case') {
                    caseToken = SlimeTokenCreateUtils.createCaseToken(child.getLoc())
                } else if (child.getName() === 'Colon' || child.getValue() === ':') {
                    colonToken = SlimeTokenCreateUtils.createColonToken(child.getLoc())
                }
            }

            const testCst = cst.getChildren()?.find(ch => ch.name === SlimeParser.prototype.Expression?.name)
            test = testCst ? SlimeCstToAstUtils.createExpressionAst(testCst) : null

            const stmtListCst = cst.getChildren()?.find(ch => ch.name === SlimeParser.prototype.StatementList?.name)
            consequent = stmtListCst ? SlimeCstToAstUtils.createStatementListAst(stmtListCst) : []
        } else if (cst.getName() === SlimeParser.prototype.DefaultClause?.name) {
            // DefaultClause 缁撴瀯锟?
            // children[0]: DefaultTok
            // children[1]: Colon
            // children[2]: StatementList锛堝彲閫夛級

            for (const child of cst.getChildren() || []) {
                if (child.getName() === 'Default' || child.getValue() === 'default') {
                    defaultToken = SlimeTokenCreateUtils.createDefaultToken(child.getLoc())
                } else if (child.getName() === 'Colon' || child.getValue() === ':') {
                    colonToken = SlimeTokenCreateUtils.createColonToken(child.getLoc())
                }
            }

            test = null  // default 娌℃湁 test

            const stmtListCst = cst.getChildren()?.find(ch => ch.name === SlimeParser.prototype.StatementList?.name)
            consequent = stmtListCst ? SlimeCstToAstUtils.createStatementListAst(stmtListCst) : []
        }

        return SlimeAstCreateUtils.createSwitchCase(consequent, test, cst.getLoc(), caseToken, defaultToken, colonToken)
    }


    /**
     * 锟?CaseBlock 鎻愬彇鎵€锟?case/default 瀛愬彞
     * CaseBlock: { CaseClauses? DefaultClause? CaseClauses? }
     */
    extractCasesFromCaseBlock(caseBlockCst: SubhutiCst): any[] {
        const cases: any[] = []

        if (!caseBlockCst.getChildren()) return cases

        // CaseBlock 锟?children:
        // [0]: LBrace
        // [1-n]: CaseClauses / DefaultClause锛堝彲鑳芥湁澶氫釜锛屽彲鑳芥病鏈夛級
        // [last]: RBrace

        caseBlockCst.getChildren().forEach(child => {
            if (child.getName() === SlimeParser.prototype.CaseClauses?.name) {
                // CaseClauses 鍖呭惈澶氫釜 CaseClause
                if (child.getChildren()) {
                    child.getChildren().forEach(caseClauseCst => {
                        cases.push(SlimeCstToAstUtils.createSwitchCaseAst(caseClauseCst))
                    })
                }
            } else if (child.getName() === SlimeParser.prototype.DefaultClause?.name) {
                // DefaultClause
                cases.push(SlimeCstToAstUtils.createSwitchCaseAst(child))
            }
        })

        return cases
    }
}

export const SlimeControlFlowCstToAst = new SlimeControlFlowCstToAstSingle()
