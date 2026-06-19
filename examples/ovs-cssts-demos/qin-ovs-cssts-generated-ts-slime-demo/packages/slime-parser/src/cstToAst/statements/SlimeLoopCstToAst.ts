/**
 * SlimeLoopCstToAst - 寰幆璇彞
 *
 * 璐熻矗锛?
 * - for 璇彞
 * - for-in 璇彞
 * - for-of 璇彞
 * - for-await-of 璇彞
 * - while 璇彞
 * - do-while 璇彞
 */
import {SubhutiCst} from "subhuti";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import {SlimeAstCreateUtils, SlimeAstTypeName, SlimeTokenCreateUtils} from "slime-ast";

export class SlimeLoopCstToAstSingle {

    /**
     * 鍒涘缓 for 璇彞 AST
     * ES2025 ForStatement:
     *   for ( var VariableDeclarationList ; Expression_opt ; Expression_opt ) Statement
     *   for ( LexicalDeclaration Expression_opt ; Expression_opt ) Statement
     *   for ( Expression_opt ; Expression_opt ; Expression_opt ) Statement
     *
     * 娉ㄦ剰锛歀exicalDeclaration 鍐呴儴宸茬粡鍖呭惈鍒嗗彿锛圫emicolonASI锟?
     */
    createForStatementAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ForStatement?.name);

        let init: any = null
        let test: any = null
        let update: any = null
        let body: any = null
        let forToken: any = undefined
        let varToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        const semicolonTokens: any[] = []

        const children = cst.getChildren() || []

        // 鏀堕泦鎵€鏈夎〃杈惧紡锛堝彲鑳芥槸 test 锟?update锟?
        const expressions: any[] = []
        let hasLexicalDeclaration = false

        for (const child of children) {
            if (!child) continue
            const name = child.getName()

            // for token
            if (name === 'For' || child.getValue() === 'for') {
                forToken = SlimeTokenCreateUtils.createForToken(child.getLoc())
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
            // var token
            if (name === 'Var' || child.getValue() === 'var') {
                varToken = SlimeTokenCreateUtils.createVarToken(child.getLoc())
                continue
            }
            // Semicolon token
            if (name === 'Semicolon' || child.getValue() === ';' || child.getLoc()?.type === 'Semicolon') {
                semicolonTokens.push(SlimeTokenCreateUtils.createSemicolonToken(child.getLoc()))
                continue
            }
            if (name === 'SemicolonASI') {
                const semicolonCst = child.getChildren()?.find(c => c.name === 'Semicolon' || c.value === ';')
                if (semicolonCst) {
                    semicolonTokens.push(SlimeTokenCreateUtils.createSemicolonToken(semicolonCst.getLoc()))
                }
                continue
            }

            // VariableDeclarationList (for var) - init
            if (name === SlimeParser.prototype.VariableDeclarationList?.name || name === 'VariableDeclarationList') {
                init = SlimeCstToAstUtils.createVariableDeclarationFromList(child, 'var')
                if (varToken && init) {
                    (init as any).kind = varToken
                }
                continue
            }

            // LexicalDeclaration (for let/const) - init
            // 娉ㄦ剰锛歀exicalDeclaration 鍐呴儴鍖呭惈浜嗗垎锟?
            if (name === SlimeParser.prototype.LexicalDeclaration?.name || name === 'LexicalDeclaration') {
                init = SlimeCstToAstUtils.createLexicalDeclarationAst(child)
                hasLexicalDeclaration = true
                continue
            }

            // VariableDeclaration (legacy) - init
            if (name === SlimeParser.prototype.VariableDeclaration?.name || name === 'VariableDeclaration') {
                init = SlimeCstToAstUtils.createVariableDeclarationAst(child)
                continue
            }

            // Expression - 鏀堕泦鎵€鏈夎〃杈惧紡
            if (name === SlimeParser.prototype.Expression?.name || name === 'Expression') {
                expressions.push(SlimeCstToAstUtils.createExpressionAst(child))
                continue
            }

            // Statement (body)
            if (name === SlimeParser.prototype.Statement?.name || name === 'Statement') {
                const stmts = SlimeCstToAstUtils.createStatementAst(child)
                body = Array.isArray(stmts) ? stmts[0] : stmts
                continue
            }
        }

        // 鏍规嵁鏀堕泦鐨勮〃杈惧紡鍜屾槸鍚︽湁 LexicalDeclaration 鏉ュ垎锟?
        if (hasLexicalDeclaration) {
            // for (let i = 0; test; update) - LexicalDeclaration 宸茬粡锟?init
            // 鍚庨潰涓や釜琛ㄨ揪寮忓垎鍒槸 test 锟?update
            if (expressions.length >= 1) test = expressions[0]
            if (expressions.length >= 2) update = expressions[1]
        } else if (init) {
            // for (var i = 0; test; update) - init 宸茶锟?
            // 鍚庨潰涓や釜琛ㄨ揪寮忓垎鍒槸 test 锟?update
            if (expressions.length >= 1) test = expressions[0]
            if (expressions.length >= 2) update = expressions[1]
        } else {
            // for (init; test; update) - 涓変釜琛ㄨ揪锟?
            if (expressions.length >= 1) init = expressions[0]
            if (expressions.length >= 2) test = expressions[1]
            if (expressions.length >= 3) update = expressions[2]
        }

        if (hasLexicalDeclaration && init && (init as any).semicolonToken) {
            if (semicolonTokens.length === 0) {
                semicolonTokens.push((init as any).semicolonToken)
            } else if (semicolonTokens.length === 1) {
                semicolonTokens.unshift((init as any).semicolonToken)
            }
        }

        return SlimeAstCreateUtils.createForStatement(
            body, init, test, update, cst.getLoc(),
            forToken, lParenToken, rParenToken,
            semicolonTokens[0], semicolonTokens[1]
        )
    }

    /**
     * 鍒涘缓 for...in / for...of 璇彞 AST
     */
    createForInOfStatementAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ForInOfStatement?.name);

        // ForInOfStatement 缁撴瀯锛堝绉嶅舰寮忥級锟?
        // 鏅拷?for-in/of: [ForTok, LParen, ForDeclaration, InTok/OfTok, Expression, RParen, Statement]
        // for await: [ForTok, AwaitTok, LParen, ForDeclaration, OfTok, AssignmentExpression, RParen, Statement]

        // 妫€鏌ユ槸鍚︽槸 for await
        const hasAwait = cst.getChildren().some(ch => ch.name === 'Await')

        // 鍔ㄦ€佹煡鎵惧悇涓儴锟?
        let left: any = null
        let right: any = null
        let body: any = null
        let isForOf = false
        let forToken: any = undefined
        let awaitToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let inToken: any = undefined
        let ofToken: any = undefined

        for (const child of cst.getChildren() || []) {
            if (!child) continue
            const name = child.getName()
            if (name === 'For' || child.getValue() === 'for') {
                forToken = SlimeTokenCreateUtils.createForToken(child.getLoc())
            } else if (name === 'Await' || child.getValue() === 'await') {
                awaitToken = SlimeTokenCreateUtils.createAwaitToken(child.getLoc())
            } else if (name === 'LParen' || child.getValue() === '(') {
                lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
            } else if (name === 'RParen' || child.getValue() === ')') {
                rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
            } else if (name === 'In' || child.getValue() === 'in') {
                inToken = SlimeTokenCreateUtils.createInToken(child.getLoc())
            } else if (name === 'Of' || child.getValue() === 'of') {
                ofToken = SlimeTokenCreateUtils.createOfToken(child.getLoc())
            }
        }

        // 鏌ユ壘 ForDeclaration 锟?LeftHandSideExpression
        const forDeclarationCst = cst.getChildren().find(ch =>
            ch.name === SlimeParser.prototype.ForDeclaration?.name ||
            ch.name === 'ForDeclaration'
        )
        const leftHandSideCst = cst.getChildren().find(ch =>
            ch.name === SlimeParser.prototype.LeftHandSideExpression?.name ||
            ch.name === 'LeftHandSideExpression'
        )
        const varBindingCst = cst.getChildren().find(ch =>
            ch.name === SlimeParser.prototype.ForBinding?.name ||
            ch.name === 'ForBinding'
        )

        // 妫€鏌ユ槸鍚︽槸 ES5 閬楃暀璇硶: for (var x = init in expr)
        // CST 缁撴瀯: [For, LParen, Var, BindingIdentifier, Initializer, In, Expression, RParen, Statement]
        const varTokenCst = cst.getChildren().find(ch => ch.name === 'Var' || ch.value === 'var')
        const bindingIdCst = cst.getChildren().find(ch =>
            ch.name === SlimeParser.prototype.BindingIdentifier?.name || ch.name === 'BindingIdentifier'
        )
        const initializerCst = cst.getChildren().find(ch =>
            ch.name === SlimeParser.prototype.Initializer?.name || ch.name === 'Initializer'
        )

        if (forDeclarationCst) {
            // ForDeclaration 鍐呴儴锟?LetOrConst + ForBinding
            const letOrConstCst = forDeclarationCst.children[0]
            const forBindingCst = forDeclarationCst.children[1]
            const letOrConstTokenCst = letOrConstCst?.children?.find(ch =>
                ch.name === 'Let' || ch.name === 'Const' || ch.value === 'let' || ch.value === 'const'
            ) || letOrConstCst?.children?.[0]

            // ForBinding鍙兘鏄疊indingIdentifier鎴朆indingPattern
            const actualBinding = forBindingCst.children[0]
            let id;

            if (actualBinding.name === SlimeParser.prototype.BindingPattern?.name || actualBinding.name === 'BindingPattern') {
                id = SlimeCstToAstUtils.createBindingPatternAst(actualBinding);
            } else if (actualBinding.name === SlimeParser.prototype.BindingIdentifier?.name || actualBinding.name === 'BindingIdentifier') {
                id = SlimeCstToAstUtils.createBindingIdentifierAst(actualBinding);
            } else {
                id = SlimeCstToAstUtils.createBindingIdentifierAst(actualBinding);
            }

            const kind = letOrConstTokenCst?.value  // 'let' or 'const'
            const kindLoc = letOrConstTokenCst?.loc || letOrConstCst?.loc

            left = {
                type: SlimeAstTypeName.VariableDeclaration,
                declarations: [{
                    type: SlimeAstTypeName.VariableDeclarator,
                    id: id,
                    init: null,
                    loc: forBindingCst.loc
                }],
                kind: {
                    type: 'VariableDeclarationKind',
                    value: kind,
                    loc: kindLoc
                },
                loc: forDeclarationCst.loc
            }
        } else if (varTokenCst && bindingIdCst && initializerCst) {
            // ES5 閬楃暀璇硶: for (var x = init in expr) - 闈炰弗鏍兼ā寮忎笅鍏佽
            const id = SlimeCstToAstUtils.createBindingIdentifierAst(bindingIdCst)
            const init = SlimeCstToAstUtils.createInitializerAst(initializerCst)
            const assignCst = initializerCst.children?.[0]
            const eqToken = assignCst ? SlimeTokenCreateUtils.createAssignToken(assignCst.getLoc()) : undefined
            left = {
                type: SlimeAstTypeName.VariableDeclaration,
                declarations: [{
                    type: SlimeAstTypeName.VariableDeclarator,
                    id: id,
                    init: init,
                    eqToken: eqToken,
                    loc: {
                        ...bindingIdCst.loc,
                        end: initializerCst.loc.end
                    }
                }],
                kind: {
                    type: 'VariableDeclarationKind',
                    value: 'var',
                    loc: varTokenCst.loc
                },
                loc: {
                    ...varTokenCst.loc,
                    end: initializerCst.loc.end
                }
            }
        } else if (leftHandSideCst) {
            left = SlimeCstToAstUtils.createLeftHandSideExpressionAst(leftHandSideCst)
        } else if (varBindingCst) {
            // var ForBinding
            const actualBinding = varBindingCst.children[0]
            let id;
            if (actualBinding.name === SlimeParser.prototype.BindingPattern?.name || actualBinding.name === 'BindingPattern') {
                id = SlimeCstToAstUtils.createBindingPatternAst(actualBinding);
            } else {
                id = SlimeCstToAstUtils.createBindingIdentifierAst(actualBinding);
            }
            left = {
                type: SlimeAstTypeName.VariableDeclaration,
                declarations: [{
                    type: SlimeAstTypeName.VariableDeclarator,
                    id: id,
                    init: null,
                    loc: varBindingCst.loc
                }],
                kind: {
                    type: 'VariableDeclarationKind',
                    value: 'var',
                    loc: cst.getChildren().find(ch => ch.name === 'Var')?.loc
                },
                loc: varBindingCst.loc
            }
        }

        // 鏌ユ壘 in/of token
        const inOrOfCst = cst.getChildren().find(ch =>
            ch.name === 'In' || ch.name === 'Of' ||
            ch.value === 'in' || ch.value === 'of'
        )
        isForOf = inOrOfCst?.value === 'of' || inOrOfCst?.name === 'OfTok'

        // 鏌ユ壘 right expression (锟?in/of 涔嬪悗)
        const inOrOfIndex = cst.getChildren().indexOf(inOrOfCst)
        if (inOrOfIndex !== -1 && inOrOfIndex + 1 < cst.getChildren().length) {
            const rightCst = cst.getChildren()[inOrOfIndex + 1]
            if (rightCst.getName() !== 'RParen') {
                right = SlimeCstToAstUtils.createExpressionAst(rightCst)
            }
        }

        // 鏌ユ壘 Statement (body)
        const statementCst = cst.getChildren().find(ch =>
            ch.name === SlimeParser.prototype.Statement?.name ||
            ch.name === 'Statement'
        )
        if (statementCst) {
            const bodyStatements = SlimeCstToAstUtils.createStatementAst(statementCst)
            body = Array.isArray(bodyStatements) && bodyStatements.length > 0
                ? bodyStatements[0]
                : bodyStatements
        }

        const result: any = {
            type: isForOf ? SlimeAstTypeName.ForOfStatement : SlimeAstTypeName.ForInStatement,
            left: left,
            right: right,
            body: body,
            loc: cst.getLoc()
        }
        if (forToken) result.forToken = forToken
        if (awaitToken) result.awaitToken = awaitToken
        if (lParenToken) result.lParenToken = lParenToken
        if (rParenToken) result.rParenToken = rParenToken
        if (inToken) result.inToken = inToken
        if (ofToken) result.ofToken = ofToken

        // for await 闇€瑕佽锟?await 灞烇拷?
        if (hasAwait) {
            result.await = true
        }

        return result
    }

    /**
     * 鍒涘缓 while 璇彞 AST
     */
    createWhileStatementAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.WhileStatement?.name);
        // WhileStatement: WhileTok LParen Expression RParen Statement

        let whileToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined

        for (const child of cst.getChildren()) {
            if (!child) continue
            if (child.getName() === 'While' || child.getValue() === 'while') {
                whileToken = SlimeTokenCreateUtils.createWhileToken(child.getLoc())
            } else if (child.getName() === 'LParen' || child.getValue() === '(') {
                lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
            } else if (child.getName() === 'RParen' || child.getValue() === ')') {
                rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
            }
        }

        const expression = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.Expression?.name)
        const statement = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.Statement?.name)

        const test = expression ? SlimeCstToAstUtils.createExpressionAst(expression) : null
        // createStatementAst杩斿洖鏁扮粍锛屽彇绗竴涓厓锟?
        const bodyArray = statement ? SlimeCstToAstUtils.createStatementAst(statement) : []
        const body = bodyArray.length > 0 ? bodyArray[0] : null

        return SlimeAstCreateUtils.createWhileStatement(test, body, cst.getLoc(), whileToken, lParenToken, rParenToken)
    }

    /**
     * 鍒涘缓 do...while 璇彞 AST
     */
    createDoWhileStatementAst(cst: SubhutiCst): any {
        SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.DoWhileStatement?.name);
        // DoWhileStatement: do Statement while ( Expression ) ;

        let doToken: any = undefined
        let whileToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let semicolonToken: any = undefined
        let body: any = null
        let test: any = null

        for (const child of cst.getChildren()) {
            if (!child) continue
            const name = child.getName()

            if (name === 'Do' || child.getValue() === 'do') {
                doToken = SlimeTokenCreateUtils.createDoToken(child.getLoc())
            } else if (name === 'While' || child.getValue() === 'while') {
                whileToken = SlimeTokenCreateUtils.createWhileToken(child.getLoc())
            } else if (name === 'LParen' || child.getValue() === '(') {
                lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
            } else if (name === 'RParen' || child.getValue() === ')') {
                rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
            } else if (name === 'Semicolon' || child.getValue() === ';') {
                semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(child.getLoc())
            } else if (name === SlimeParser.prototype.Statement?.name || name === 'Statement') {
                const bodyArray = SlimeCstToAstUtils.createStatementAst(child)
                body = bodyArray.length > 0 ? bodyArray[0] : null
            } else if (name === SlimeParser.prototype.Expression?.name || name === 'Expression') {
                test = SlimeCstToAstUtils.createExpressionAst(child)
            }
        }

        return SlimeAstCreateUtils.createDoWhileStatement(body, test, cst.getLoc(), doToken, whileToken, lParenToken, rParenToken, semicolonToken)
    }


    /**
     * ForDeclaration CST 锟?AST
     * ForDeclaration -> LetOrConst ForBinding
     */
    createForDeclarationAst(cst: SubhutiCst): any {
        const letOrConst = cst.getChildren()?.find(ch =>
            ch.name === SlimeParser.prototype.LetOrConst?.name || ch.name === 'LetOrConst'
        )
        const forBinding = cst.getChildren()?.find(ch =>
            ch.name === SlimeParser.prototype.ForBinding?.name || ch.name === 'ForBinding'
        )

        const letOrConstTokenCst = letOrConst?.children?.find(ch =>
            ch.name === 'Let' || ch.name === 'Const' || ch.value === 'let' || ch.value === 'const'
        ) || letOrConst?.children?.[0]
        const kind = letOrConstTokenCst?.value || 'let'
        const kindLoc = letOrConstTokenCst?.loc || letOrConst?.loc
        const id = forBinding ? SlimeCstToAstUtils.createForBindingAst(forBinding) : null

        return {
            type: SlimeAstTypeName.VariableDeclaration,
            declarations: [{
                type: SlimeAstTypeName.VariableDeclarator,
                id: id,
                init: null,
                loc: forBinding?.loc
            }],
            kind: {type: 'VariableDeclarationKind', value: kind, loc: kindLoc},
            loc: cst.getLoc()
        }
    }

    /**
     * ForBinding CST 锟?AST
     * ForBinding -> BindingIdentifier | BindingPattern
     */
    createForBindingAst(cst: SubhutiCst): any {
        const firstChild = cst.getChildren()?.[0]
        if (!firstChild) return null

        if (firstChild.getName() === SlimeParser.prototype.BindingIdentifier?.name || firstChild.getName() === 'BindingIdentifier') {
            return SlimeCstToAstUtils.createBindingIdentifierAst(firstChild)
        } else if (firstChild.getName() === SlimeParser.prototype.BindingPattern?.name || firstChild.getName() === 'BindingPattern') {
            return SlimeCstToAstUtils.createBindingPatternAst(firstChild)
        }
        return SlimeCstToAstUtils.createBindingIdentifierAst(firstChild)
    }
}

export const SlimeLoopCstToAst = new SlimeLoopCstToAstSingle()
