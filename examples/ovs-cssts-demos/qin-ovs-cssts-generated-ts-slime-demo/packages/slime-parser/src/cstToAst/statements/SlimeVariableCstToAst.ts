/**
 * VariableCstToAst - var/let/const 澹版槑杞崲
 */
import { SubhutiCst } from "subhuti";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import {
    type SlimeBlockStatement, type SlimeClassDeclaration, type SlimeDeclaration,
    type SlimeFunctionDeclaration, type SlimeFunctionExpression,
    type SlimeFunctionParam,
    type SlimeIdentifier, SlimeAstTypeName, type SlimePropertyDefinition,
    SlimeTokenCreateUtils, type SlimeVariableDeclaration, type SlimeVariableDeclarator,
    SlimeAstCreateUtils, type SlimePattern, type SlimeExpression
} from "slime-ast";
import { SlimeClassDeclarationCstToAstSingle } from "../class/SlimeClassDeclarationCstToAst.ts";
import { SlimeIdentifierCstToAst } from "../identifier/SlimeIdentifierCstToAst.ts";



export class SlimeVariableCstToAstSingle {

    /**
     * 鍒涘缓 var 鍙橀噺澹版槑璇彞 AST
     * ES2025 VariableStatement: var VariableDeclarationList ;
     */
    createVariableStatementAst(cst: SubhutiCst): SlimeVariableDeclaration {
        const children = cst.getChildren() || []
        const declarations: SlimeVariableDeclarator[] = []
        let kindToken: any = undefined
        let semicolonToken: any = undefined

        // ?? VariableDeclarationList
        for (const child of children) {
            if (!child) continue

            if (child.getName() === 'Var' || child.getValue() === 'var') {
                kindToken = SlimeTokenCreateUtils.createVarToken(child.getLoc())
                continue
            }
            if (child.getName() === 'Semicolon' || child.getValue() === ';') {
                semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(child.getLoc())
                continue
            }
            if (child.getName() === 'SemicolonASI') {
                const semicolonCst = child.getChildren()?.find(c => c.name === 'Semicolon' || c.value === ';')
                if (semicolonCst) {
                    semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(semicolonCst.getLoc())
                }
                continue
            }

            if (child.getName() === SlimeParser.prototype.VariableDeclarationList?.name ||
                child.getName() === 'VariableDeclarationList') {
                const list = SlimeCstToAstUtils.createVariableDeclarationListAst(child)
                declarations.push(...list)
            }
        }

        if (!kindToken) {
            kindToken = SlimeTokenCreateUtils.createVarToken(cst.getLoc())
        }
        const result = SlimeAstCreateUtils.createVariableDeclaration(kindToken, declarations, cst.getLoc()) as any
        if (semicolonToken) {
            result.semicolonToken = semicolonToken
        }
        return result
    }


    createVariableDeclarationAst(cst: SubhutiCst): SlimeVariableDeclaration {
        //??????????????????
        //                 SlimeCstToAstUtils.Statement()
        //                 SlimeCstToAstUtils.Declaration()
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.VariableDeclaration?.name);
        let kindCst: SubhutiCst = cst.getChildren()[0].children[0]

        // ?????? kind token
        let kindToken: any = undefined
        const kindValue = kindCst.getValue() as string
        if (kindValue === 'var') {
            kindToken = SlimeTokenCreateUtils.createVarToken(kindCst.getLoc())
        } else if (kindValue === 'let') {
            kindToken = SlimeTokenCreateUtils.createLetToken(kindCst.getLoc())
        } else if (kindValue === 'const') {
            kindToken = SlimeTokenCreateUtils.createConstToken(kindCst.getLoc())
        }

        let declarations: SlimeVariableDeclarator[] = []
        if (cst.getChildren()[1]) {
            declarations = SlimeCstToAstUtils.createVariableDeclarationListAst(cst.getChildren()[1])
        }
        return SlimeAstCreateUtils.createVariableDeclaration(kindToken, declarations, cst.getLoc())
    }


    createVariableDeclarationListAst(cst: SubhutiCst): SlimeVariableDeclarator[] {
        const declarations: SlimeVariableDeclarator[] = []
        let lastDeclaration: SlimeVariableDeclarator | null = null

        for (const child of cst.getChildren() || []) {
            if (!child) continue

            if (child.getValue() === ',' || child.getName() === 'Comma') {
                if (lastDeclaration) {
                    (lastDeclaration as any).commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                }
                continue
            }

            if (child.getName() === SlimeParser.prototype.LexicalBinding?.name || child.getName() === 'LexicalBinding') {
                const decl = SlimeCstToAstUtils.createLexicalBindingAst(child)
                declarations.push(decl)
                lastDeclaration = decl
                continue
            }

            if (child.getName() === SlimeParser.prototype.VariableDeclaration?.name || child.getName() === 'VariableDeclaration') {
                const decl = SlimeCstToAstUtils.createVariableDeclaratorFromVarDeclaration(child)
                declarations.push(decl)
                lastDeclaration = decl
                continue
            }

            if (child.getName() === SlimeParser.prototype.VariableDeclarator?.name || child.getName() === 'VariableDeclarator') {
                const decl = SlimeCstToAstUtils.createVariableDeclaratorAst(child)
                declarations.push(decl)
                lastDeclaration = decl
            }
        }

        return declarations
    }


    createVariableDeclaratorAst(cst: SubhutiCst): SlimeVariableDeclarator {
        // 鍏煎 LexicalBinding 锟?VariableDeclaration
        // const astName = SlimeCstToAstUtils.checkCstName(cst, 'LexicalBinding');

        // children[0]鍙兘鏄疊indingIdentifier鎴朆indingPattern锛堣В鏋勶級
        const firstChild = cst.getChildren()[0]
        let id: SlimeIdentifier | SlimePattern

        if (firstChild.getName() === SlimeParser.prototype.BindingIdentifier?.name) {
            id = SlimeCstToAstUtils.createBindingIdentifierAst(firstChild)
        } else if (firstChild.getName() === SlimeParser.prototype.BindingPattern?.name) {
            id = SlimeCstToAstUtils.createBindingPatternAst(firstChild)
        } else {
            throw new Error(`Unexpected variable declarator id type: ${firstChild.getName()}`)
        }

        // console.log(6565656)
        // console.log(id)
        let variableDeclarator: SlimeVariableDeclarator
        const varCst = cst.getChildren()[1]
        if (varCst) {
            const eqCst = varCst.children[0]
            const eqAst = SlimeTokenCreateUtils.createAssignToken(eqCst.loc)
            const initCst = varCst.children[1]
            if (initCst) {
                // 妫€鏌nitCst鏄惁鏄疉ssignmentExpression
                if (initCst.name === SlimeParser.prototype.AssignmentExpression?.name) {
                    const init = SlimeCstToAstUtils.createAssignmentExpressionAst(initCst)
                    variableDeclarator = SlimeAstCreateUtils.createVariableDeclarator(id, eqAst, init)
                } else {
                    // 濡傛灉涓嶆槸AssignmentExpression锛岀洿鎺ヤ綔涓鸿〃杈惧紡澶勭悊
                    const init = SlimeCstToAstUtils.createExpressionAst(initCst)
                    variableDeclarator = SlimeAstCreateUtils.createVariableDeclarator(id, eqAst, init)
                }
            } else {
                variableDeclarator = SlimeAstCreateUtils.createVariableDeclarator(id, eqAst)
            }
        } else {
            variableDeclarator = SlimeAstCreateUtils.createVariableDeclarator(id)
        }
        variableDeclarator.loc = cst.getLoc()
        return variableDeclarator
    }


    /**
     * 锟?VariableDeclaration CST 鍒涘缓 VariableDeclarator AST
     * VariableDeclaration: BindingIdentifier Initializer? | BindingPattern Initializer
     */
    createVariableDeclaratorFromVarDeclaration(cst: SubhutiCst): SlimeVariableDeclarator {
        const children = cst.getChildren() || []
        let id: any = null
        let init: any = null
        let eqToken: any = undefined

        for (const child of children) {
            if (!child) continue
            const name = child.getName()

            if (name === SlimeParser.prototype.BindingIdentifier?.name || name === 'BindingIdentifier') {
                id = SlimeCstToAstUtils.createBindingIdentifierAst(child)
            } else if (name === SlimeParser.prototype.BindingPattern?.name || name === 'BindingPattern') {
                id = SlimeCstToAstUtils.createBindingPatternAst(child)
            } else if (name === SlimeParser.prototype.Initializer?.name || name === 'Initializer') {
                const assignCst = child.getChildren()?.[0]
                if (assignCst) {
                    eqToken = SlimeTokenCreateUtils.createAssignToken(assignCst.getLoc())
                }
                init = SlimeCstToAstUtils.createInitializerAst(child)
            }
        }

        const result = SlimeAstCreateUtils.createVariableDeclarator(id, eqToken, init) as any
        result.loc = cst.getLoc()
        return result
    }


    /**
     * 锟?VariableDeclarationList 鍒涘缓 VariableDeclaration AST
     */
    createVariableDeclarationFromList(cst: SubhutiCst, kind: string): SlimeVariableDeclaration {
        const declarations = SlimeCstToAstUtils.createVariableDeclarationListAst(cst)

        return {
            type: SlimeAstTypeName.VariableDeclaration,
            kind: kind as any,
            declarations: declarations,
            loc: cst.getLoc()
        } as any
    }


    createLexicalDeclarationAst(cst: SubhutiCst): SlimeVariableDeclaration {
        // ES2025 LexicalDeclaration: LetOrConst BindingList ;
        // BindingList: LexicalBinding (, LexicalBinding)*
        // LexicalBinding: BindingIdentifier Initializer? | BindingPattern Initializer

        const children = cst.getChildren() || []
        let kindToken: any = undefined  // 浣跨敤甯?loc 鐨?token 瀵硅薄 // 榛樿锟?
        let semicolonToken: any = undefined
        const declarations: any[] = []

        for (const child of children) {
            if (!child) continue
            const name = child.getName()

            if (child.getName() === 'Semicolon' || child.getValue() === ';' || child.getLoc()?.type === 'Semicolon') {
                semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(child.getLoc())
                continue
            }
            if (child.getName() === 'SemicolonASI') {
                const semicolonCst = child.getChildren()?.find(c => c.name === 'Semicolon' || c.value === ';')
                if (semicolonCst) {
                    semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(semicolonCst.getLoc())
                }
                continue
            }

            // Skip commas
            if (child.getValue() === ',') {
                continue
            }

            // LetOrConst 瑙勫垯
            if (name === SlimeParser.prototype.LetOrConst?.name || name === 'LetOrConst') {
                // 鍐呴儴锟?LetTok 锟?ConstTok
                if (child.getChildren() && child.getChildren().length > 0) {
                    const tokenCst = child.getChildren()[0]
                    const kindValue = tokenCst.getValue() as string || 'const'
                    if (kindValue === 'let') {
                        kindToken = SlimeTokenCreateUtils.createLetToken(tokenCst.getLoc())
                    } else if (kindValue === 'const') {
                        kindToken = SlimeTokenCreateUtils.createConstToken(tokenCst.getLoc())
                    }
                }
                continue
            }

            // 鐩存帴锟?LetTok 锟?ConstTok (ES2025 鍙兘鐩存帴浣跨敤)
            if (name === 'Let' || child.getValue() === 'let') {
                kindToken = SlimeTokenCreateUtils.createLetToken(child.getLoc())
                continue
            }
            if (name === 'Const' || child.getValue() === 'const') {
                kindToken = SlimeTokenCreateUtils.createConstToken(child.getLoc())
                continue
            }

            // Handle BindingList wrapper
            if (name === 'BindingList' || name === SlimeParser.prototype.BindingList?.name) {
                let lastDeclaration: any = null
                for (const binding of child.getChildren() || []) {
                    if (binding.getValue() === ',' || binding.getName() === 'Comma') {
                        if (lastDeclaration) {
                            lastDeclaration.commaToken = SlimeTokenCreateUtils.createCommaToken(binding.getLoc())
                        }
                        continue
                    }
                    if (binding.getName() === 'LexicalBinding' || binding.getName() === SlimeParser.prototype.LexicalBinding?.name) {
                        const decl = SlimeCstToAstUtils.createLexicalBindingAst(binding)
                        declarations.push(decl)
                        lastDeclaration = decl
                    }
                }
                continue
            }

            // Direct LexicalBinding
            if (name === 'LexicalBinding' || name === SlimeParser.prototype.LexicalBinding?.name) {
                declarations.push(SlimeCstToAstUtils.createLexicalBindingAst(child))
            }
        }

        const result = SlimeAstCreateUtils.createVariableDeclaration(kindToken, declarations, cst.getLoc()) as any
        if (semicolonToken) {
            result.semicolonToken = semicolonToken
        }
        return result
    }


    createLexicalBindingAst(cst: SubhutiCst): SlimeVariableDeclarator {
        // LexicalBinding: BindingIdentifier Initializer? | BindingPattern Initializer
        const children = cst.getChildren() || []

        let id: any = null
        let init: any = null
        let assignToken: any = undefined

        for (const child of children) {
            if (!child) continue

            const name = child.getName()
            if (name === SlimeParser.prototype.BindingIdentifier?.name || name === 'BindingIdentifier') {
                id = SlimeCstToAstUtils.createBindingIdentifierAst(child)
            } else if (name === SlimeParser.prototype.BindingPattern?.name || name === 'BindingPattern') {
                id = SlimeCstToAstUtils.createBindingPatternAst(child)
            } else if (name === SlimeParser.prototype.Initializer?.name || name === 'Initializer') {
                // Initializer: = AssignmentExpression
                // children[0] 锟?Assign token锛宑hildren[1] 锟?AssignmentExpression
                if (child.getChildren() && child.getChildren()[0]) {
                    const assignCst = child.getChildren()[0]
                    assignToken = SlimeTokenCreateUtils.createAssignToken(assignCst.getLoc())
                }
                init = SlimeCstToAstUtils.createInitializerAst(child)
            }
        }

        return SlimeAstCreateUtils.createVariableDeclarator(id, assignToken, init, cst.getLoc())
    }


    /**
     * LetOrConst CST 锟?AST
     * LetOrConst -> let | const
     */
    createLetOrConstAst(cst: SubhutiCst): string {
        const token = cst.getChildren()?.[0]
        return token?.getValue() || 'let'
    }


    createInitializerAst(cst: SubhutiCst): SlimeExpression {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.Initializer?.name);
        // Initializer -> Eq + AssignmentExpression
        const assignmentExpressionCst = cst.getChildren()[1]
        return SlimeCstToAstUtils.createAssignmentExpressionAst(assignmentExpressionCst)
    }


    /**
     * [TypeScript] createDeclarationAst 鏀寔 TypeScript 澹版槑
     */
    createDeclarationAst(cst: SubhutiCst): SlimeDeclaration {
        // Support both Declaration wrapper and direct types
        const first = cst.getName() === SlimeParser.prototype.Declaration?.name || cst.getName() === 'Declaration'
            ? cst.getChildren()[0]
            : cst

        const name = first.getName()

        // TypeScript 澹版槑
        if (name === 'TSInterfaceDeclaration') {
            return SlimeCstToAstUtils.createTSInterfaceDeclarationAst(first)
        }
        if (name === 'TSTypeAliasDeclaration') {
            return SlimeCstToAstUtils.createTSTypeAliasDeclarationAst(first)
        }
        if (name === 'TSEnumDeclaration') {
            return SlimeCstToAstUtils.createTSEnumDeclarationAst(first)
        }
        if (name === 'TSModuleDeclaration') {
            return SlimeIdentifierCstToAst.createTSModuleDeclarationAst(first)
        }
        if (name === 'TSDeclareStatement') {
            return SlimeIdentifierCstToAst.createTSDeclareStatementAst(first)
        }

        // JavaScript 澹版槑
        if (name === SlimeParser.prototype.VariableDeclaration?.name || name === 'VariableDeclaration') {
            return SlimeCstToAstUtils.createVariableDeclarationAst(first);
        } else if (name === SlimeParser.prototype.LexicalDeclaration?.name || name === 'LexicalDeclaration') {
            // LexicalDeclaration: let/const declarations
            return SlimeCstToAstUtils.createLexicalDeclarationAst(first);
        } else if (name === SlimeParser.prototype.ClassDeclaration?.name || name === 'ClassDeclaration') {
            return SlimeCstToAstUtils.createClassDeclarationAst(first);
        } else if (name === SlimeParser.prototype.FunctionDeclaration?.name || name === 'FunctionDeclaration') {
            return SlimeCstToAstUtils.createFunctionDeclarationAst(first);
        } else if (name === SlimeParser.prototype.HoistableDeclaration?.name || name === 'HoistableDeclaration') {
            return SlimeCstToAstUtils.createHoistableDeclarationAst(first);
        } else {
            throw new Error(`Unsupported Declaration type: ${name}`)
        }
    }


    createHoistableDeclarationAst(cst: SubhutiCst): SlimeDeclaration {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.HoistableDeclaration?.name);
        const first = cst.getChildren()[0]
        if (first.getName() === SlimeParser.prototype.FunctionDeclaration?.name || first.getName() === 'FunctionDeclaration') {
            return SlimeCstToAstUtils.createFunctionDeclarationAst(first)
        } else if (first.getName() === SlimeParser.prototype.GeneratorDeclaration?.name || first.getName() === 'GeneratorDeclaration') {
            // GeneratorDeclaration -> 绫讳技FunctionDeclaration浣嗘湁*锟?
            return SlimeCstToAstUtils.createGeneratorDeclarationAst(first)
        } else if (first.getName() === SlimeParser.prototype.AsyncFunctionDeclaration?.name || first.getName() === 'AsyncFunctionDeclaration') {
            // AsyncFunctionDeclaration -> async function
            return SlimeCstToAstUtils.createAsyncFunctionDeclarationAst(first)
        } else if (first.getName() === SlimeParser.prototype.AsyncGeneratorDeclaration?.name || first.getName() === 'AsyncGeneratorDeclaration') {
            // AsyncGeneratorDeclaration -> async function*
            return SlimeCstToAstUtils.createAsyncGeneratorDeclarationAst(first)
        } else {
            throw new Error(`Unsupported HoistableDeclaration type: ${first.getName()}`)
        }
    }




}

export const SlimeVariableCstToAst = new SlimeVariableCstToAstSingle()
