import {SubhutiCst} from "subhuti";
import {

    SlimeBlockStatement,
    SlimeExpression,
    SlimeMethodDefinition,
    SlimeStatement,
    SlimeAstCreateUtils,
    SlimeTokenCreateUtils
} from "slime-ast";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

export class SlimeFunctionBodyCstToAstSingle {

    createFunctionBodyAst(cst: SubhutiCst): Array<SlimeStatement> {
        // FunctionBody: FunctionStatementList | StatementList
        // GeneratorBody, AsyncFunctionBody, AsyncGeneratorBody 閮藉寘锟?FunctionBody
        const children = cst.getChildren() || []

        if (children.length === 0) {
            return []
        }

        const first = children[0]
        if (!first) {
            return []
        }

        const name = first.getName()

        // Handle nested FunctionBody (from GeneratorBody, AsyncFunctionBody, AsyncGeneratorBody)
        if (name === 'FunctionBody' || name === SlimeParser.prototype.FunctionBody?.name) {
            return SlimeCstToAstUtils.createFunctionBodyAst(first)
        }

        // Handle FunctionStatementList (ES2025)
        if (name === 'FunctionStatementList' || name === SlimeParser.prototype.FunctionStatementList?.name) {
            return SlimeCstToAstUtils.createFunctionStatementListAst(first)
        }

        // Handle StatementList (legacy)
        if (name === 'StatementList' || name === SlimeParser.prototype.StatementList?.name) {
            return SlimeCstToAstUtils.createStatementListAst(first)
        }

        // If the first child is a statement directly, process it
        return SlimeCstToAstUtils.createStatementListAst(first)
    }

    /**
     * GeneratorBody CST 锟?AST锛堥€忎紶锟?FunctionBody锟?
     */
    createGeneratorBodyAst(cst: SubhutiCst): Array<SlimeStatement> {
        return SlimeCstToAstUtils.createFunctionBodyAst(cst)
    }


    /**
     * AsyncFunctionBody CST 锟?AST锛堥€忎紶锟?FunctionBody锟?
     */
    createAsyncFunctionBodyAst(cst: SubhutiCst): Array<SlimeStatement> {
        return SlimeCstToAstUtils.createFunctionBodyAst(cst)
    }


    /**
     * AsyncGeneratorBody CST 锟?AST锛堥€忎紶锟?FunctionBody锟?
     */
    createAsyncGeneratorBodyAst(cst: SubhutiCst): Array<SlimeStatement> {
        return SlimeCstToAstUtils.createFunctionBodyAst(cst)
    }


    /**
     * 鍒涘缓绠ご鍑芥暟锟?AST
     */
    createConciseBodyAst(cst: SubhutiCst): SlimeBlockStatement | SlimeExpression {
        // 闃插尽鎬ф锟?
        if (!cst) {
            throw new Error('createConciseBodyAst: cst is null or undefined')
        }

        // 鏀寔 ConciseBody 锟?AsyncConciseBody
        const validNames = [
            SlimeParser.prototype.ConciseBody?.name,
            'ConciseBody',
            'AsyncConciseBody'
        ]
        if (!validNames.includes(cst.getName())) {
            throw new Error(`createConciseBodyAst: 鏈熸湜 ConciseBody 锟?AsyncConciseBody锛屽疄锟?${cst.getName()}`)
        }

        const first = cst.getChildren()[0]

        // Es2025Parser: { FunctionBody } 鏍煎紡
        // children: [LBrace, FunctionBody/AsyncFunctionBody, RBrace]
        if (first.getName() === 'LBrace') {
            const lBraceCst = cst.getChildren().find(child =>
                child.getName() === 'LBrace' || child.getValue() === '{'
            )
            const rBraceCst = cst.getChildren().find(child =>
                child.getName() === 'RBrace' || child.getValue() === '}'
            )
            const lBraceToken = lBraceCst
                ? SlimeTokenCreateUtils.createLBraceToken(lBraceCst.getLoc())
                : undefined
            const rBraceToken = rBraceCst
                ? SlimeTokenCreateUtils.createRBraceToken(rBraceCst.getLoc())
                : undefined
            // 鎵惧埌 FunctionBody 锟?AsyncFunctionBody
            const functionBodyCst = cst.getChildren().find(child =>
                child.getName() === 'FunctionBody' || child.getName() === SlimeParser.prototype.FunctionBody?.name ||
                child.getName() === 'AsyncFunctionBody' || child.getName() === SlimeParser.prototype.AsyncFunctionBody?.name
            )
            if (functionBodyCst) {
                const bodyStatements = SlimeCstToAstUtils.createFunctionBodyAst(functionBodyCst)
                return SlimeAstCreateUtils.createBlockStatement(bodyStatements, cst.getLoc(), lBraceToken, rBraceToken)
            }
            // 绌哄嚱鏁颁綋
            return SlimeAstCreateUtils.createBlockStatement([], cst.getLoc(), lBraceToken, rBraceToken)
        }

        // 鍚﹀垯鏄〃杈惧紡锛岃В鏋愪负琛ㄨ揪锟?
        if (first.getName() === SlimeParser.prototype.AssignmentExpression?.name || first.getName() === 'AssignmentExpression') {
            return SlimeCstToAstUtils.createAssignmentExpressionAst(first)
        }

        // Es2025Parser: ExpressionBody 绫诲瀷
        if (first.getName() === 'ExpressionBody') {
            // ExpressionBody 鍐呴儴鍖呭惈 AssignmentExpression
            const innerExpr = first.getChildren()[0]
            if (innerExpr) {
                if (innerExpr.name === 'AssignmentExpression' || innerExpr.name === SlimeParser.prototype.AssignmentExpression?.name) {
                    return SlimeCstToAstUtils.createAssignmentExpressionAst(innerExpr)
                }
                return SlimeCstToAstUtils.createExpressionAst(innerExpr)
            }
        }

        return SlimeCstToAstUtils.createExpressionAst(first)
    }


    /**
     * AsyncConciseBody CST 锟?AST
     */
    createAsyncConciseBodyAst(cst: SubhutiCst): SlimeBlockStatement | SlimeExpression {
        return SlimeCstToAstUtils.createConciseBodyAst(cst)
    }

    createFunctionStatementListAst(cst: SubhutiCst): Array<SlimeStatement> {
        // FunctionStatementList: StatementList?
        const children = cst.getChildren() || []

        if (children.length === 0) {
            return []
        }

        const first = children[0]
        if (!first) {
            return []
        }

        // If child is StatementList, process it
        if (first.getName() === 'StatementList' || first.getName() === SlimeParser.prototype.StatementList?.name) {
            return SlimeCstToAstUtils.createStatementListAst(first)
        }

        // If child is a statement directly
        return SlimeCstToAstUtils.createStatementListItemAst(first)
    }
}


export const SlimeFunctionBodyCstToAst = new SlimeFunctionBodyCstToAstSingle()
