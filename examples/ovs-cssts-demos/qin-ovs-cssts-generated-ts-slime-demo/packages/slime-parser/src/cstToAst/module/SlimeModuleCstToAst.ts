import { SubhutiCst } from "subhuti";
import {
    SlimeAstCreateUtils,
    SlimeFunctionParam,
    SlimeModuleDeclaration,
    SlimePattern,
    SlimeProgram,
    SlimeStatement
} from "slime-ast";
import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import {SlimeVariableCstToAstSingle} from "../statements/SlimeVariableCstToAst.ts";

export class SlimeModuleCstToAstSingle {

    /**
     * 閲嶇疆鐘舵€侀挬瀛愭柟娉?
     *
     * [鍏ュ彛鏂规硶] 灏嗛《灞?CST 杞崲涓?Program AST
     *
     * 瀛樺湪蹇呰鎬э細杩欐槸澶栭儴璋冪敤鐨勪富鍏ュ彛锛屾敮鎸?Module銆丼cript銆丳rogram 澶氱椤跺眰 CST
     *
     * 娉ㄦ剰锛氬瓙绫诲闇€閲嶇疆鐘舵€侊紝搴旈噸鍐欐鏂规硶锛屽厛璋冪敤鑷繁鐨?resetState()锛屽啀璋冪敤 super.toProgram()
     */
    toProgram(cst: SubhutiCst): SlimeProgram {
        // Support both Module and Script entry points
        const isModule = cst.getName() === SlimeParser.prototype.Module?.name || cst.getName() === 'Module'
        const isScript = cst.getName() === SlimeParser.prototype.Script?.name || cst.getName() === 'Script'
        const isProgram = cst.getName() === SlimeParser.prototype.Program?.name || cst.getName() === 'Program'

        if (!isModule && !isScript && !isProgram) {
            throw new Error(`Expected CST name 'Module', 'Script' or 'Program', but got '${cst.getName()}'`)
        }

        let program: SlimeProgram
        let hashbangComment: string | null = null

        // If children is empty, return empty program
        if (!cst.getChildren() || cst.getChildren().length === 0) {
            return SlimeAstCreateUtils.createProgram([], isModule ? 'module' : 'script')
        }

        // 閬嶅巻瀛愯妭鐐癸紝澶勭悊 HashbangComment 鍜屼富浣撳唴?
        let bodyChild: SubhutiCst | null = null
        for (const child of cst.getChildren()) {
            if (child.getName() === 'HashbangComment') {
                // 鎻愬彇 Hashbang 娉ㄩ噴鐨?
                hashbangComment = child.getValue() || child.getChildren()?.[0]?.getValue() || null
            } else if (child.getName() === 'ModuleBody' || child.getName() === 'ScriptBody' ||
                child.getName() === 'ModuleItemList' || child.getName() === SlimeParser.prototype.ModuleItemList?.name ||
                child.getName() === 'StatementList' || child.getName() === SlimeParser.prototype.StatementList?.name) {
                bodyChild = child
            }
        }

        // 澶勭悊涓讳綋鍐呭
        if (bodyChild) {
            if (bodyChild.getName() === 'ModuleBody') {
                const moduleItemList = bodyChild.getChildren()?.[0]
                if (moduleItemList && (moduleItemList.getName() === 'ModuleItemList' || moduleItemList.getName() === SlimeParser.prototype.ModuleItemList?.name)) {
                    const body = SlimeCstToAstUtils.createModuleItemListAst(moduleItemList)
                    program = SlimeAstCreateUtils.createProgram(body, 'module')
                } else {
                    program = SlimeAstCreateUtils.createProgram([], 'module')
                }
            } else if (bodyChild.getName() === SlimeParser.prototype.ModuleItemList?.name || bodyChild.getName() === 'ModuleItemList') {
                const body = SlimeCstToAstUtils.createModuleItemListAst(bodyChild)
                program = SlimeAstCreateUtils.createProgram(body, 'module')
            } else if (bodyChild.getName() === 'ScriptBody') {
                const statementList = bodyChild.getChildren()?.[0]
                if (statementList && (statementList.getName() === 'StatementList' || statementList.getName() === SlimeParser.prototype.StatementList?.name)) {
                    const body = SlimeCstToAstUtils.createStatementListAst(statementList)
                    program = SlimeAstCreateUtils.createProgram(body, 'script')
                } else {
                    program = SlimeAstCreateUtils.createProgram([], 'script')
                }
            } else if (bodyChild.getName() === SlimeParser.prototype.StatementList?.name || bodyChild.getName() === 'StatementList') {
                const body = SlimeCstToAstUtils.createStatementListAst(bodyChild)
                program = SlimeAstCreateUtils.createProgram(body, 'script')
            } else {
                throw new Error(`Unexpected body child: ${bodyChild.getName()}`)
            }
        } else {
            // 娌℃湁涓讳綋鍐呭锛堝彲鑳藉彧?HashbangComment?
            program = SlimeAstCreateUtils.createProgram([], isModule ? 'module' : 'script')
        }

        // 璁剧疆 hashbang 娉ㄩ噴锛堝鏋滃瓨鍦級
        if (hashbangComment) {
            (program as any).hashbang = hashbangComment
        }

        program.loc = cst.getLoc()
        return program
    }

    /**
     * Program CST ?AST
     *
     * 瀛樺湪蹇呰鎬э細Program 鏄《灞傚叆鍙ｈ鍒欙紝闇€瑕佸?Script ?Module 涓ょ鎯呭喌?
     */
    createProgramAst(cst: SubhutiCst): SlimeProgram {
        // 澶勭悊 Program -> Script | Module
        const firstChild = cst.getChildren()?.[0]
        if (firstChild) {
            if (firstChild.getName() === 'Script' || firstChild.getName() === SlimeParser.prototype.Script?.name) {
                return SlimeCstToAstUtils.createScriptAst(firstChild)
            } else if (firstChild.getName() === 'Module' || firstChild.getName() === SlimeParser.prototype.Module?.name) {
                return SlimeCstToAstUtils.createModuleAst(firstChild)
            }
        }
        // 濡傛灉鐩存帴灏辨槸鍐呭锛岃皟?toProgram
        return SlimeCstToAstUtils.toProgram(cst)
    }

    /**
     * Module CST ?AST
     */
    createModuleAst(cst: SubhutiCst): SlimeProgram {
        const moduleBody = cst.getChildren()?.find(ch =>
            ch.getName() === 'ModuleBody' || ch.getName() === SlimeParser.prototype.ModuleBody?.name
        )
        if (moduleBody) {
            return SlimeCstToAstUtils.createModuleBodyAst(moduleBody)
        }
        return SlimeAstCreateUtils.createProgram([], 'module')
    }

    /**
     * Script CST ?AST
     */
    createScriptAst(cst: SubhutiCst): SlimeProgram {
        const scriptBody = cst.getChildren()?.find(ch =>
            ch.getName() === 'ScriptBody' || ch.getName() === SlimeParser.prototype.ScriptBody?.name
        )
        if (scriptBody) {
            return SlimeCstToAstUtils.createScriptBodyAst(scriptBody)
        }
        return SlimeAstCreateUtils.createProgram([], 'script')
    }

    /**
     * ModuleBody CST ?AST
     */
    createModuleBodyAst(cst: SubhutiCst): SlimeProgram {
        const moduleItemList = cst.getChildren()?.find(ch =>
            ch.getName() === 'ModuleItemList' || ch.getName() === SlimeParser.prototype.ModuleItemList?.name
        )
        if (moduleItemList) {
            const body = SlimeCstToAstUtils.createModuleItemListAst(moduleItemList)
            return SlimeAstCreateUtils.createProgram(body, 'module')
        }
        return SlimeAstCreateUtils.createProgram([], 'module')
    }

    /**
     * ScriptBody CST ?AST
     */
    createScriptBodyAst(cst: SubhutiCst): SlimeProgram {
        const stmtList = cst.getChildren()?.find(ch =>
            ch.getName() === 'StatementList' || ch.getName() === SlimeParser.prototype.StatementList?.name
        )
        if (stmtList) {
            const body = SlimeCstToAstUtils.createStatementListAst(stmtList)
            return SlimeAstCreateUtils.createProgram(body, 'script')
        }
        return SlimeAstCreateUtils.createProgram([], 'script')
    }

    createModuleItemListAst(cst: SubhutiCst): Array<SlimeStatement | SlimeModuleDeclaration> {
        const asts = cst.getChildren().map(item => {
            if (item.getName() === SlimeParser.prototype.ModuleItemList?.name || item.getName() === 'ModuleItemList') {
                return SlimeCstToAstUtils.createModuleItemListAst(item)
            }
            // Es2025Parser uses ModuleItem wrapper
            if (item.getName() === SlimeParser.prototype.ModuleItem?.name || item.getName() === 'ModuleItem') {
                const innerItem = item.getChildren()?.[0]
                if (!innerItem) return undefined
                return SlimeCstToAstUtils.createModuleItemAst(innerItem)
            }
            // Fallback: direct type
            return SlimeCstToAstUtils.createModuleItemAst(item)
        }).filter(ast => ast !== undefined)

        return asts.flat()
    }

    createModuleItemAst(item: SubhutiCst): SlimeStatement | SlimeModuleDeclaration | SlimeStatement[] | undefined {
        const name = item.getName()
        if (name === SlimeParser.prototype.ExportDeclaration?.name || name === 'ExportDeclaration') {
            return SlimeCstToAstUtils.createExportDeclarationAst(item)
        } else if (name === SlimeParser.prototype.ImportDeclaration?.name || name === 'ImportDeclaration') {
            return SlimeCstToAstUtils.createImportDeclarationAst(item)
        } else if (name === SlimeParser.prototype.StatementListItem?.name || name === 'StatementListItem') {
            return SlimeCstToAstUtils.createStatementListItemAst(item)
        }
        console.warn(`createModuleItemAst: Unknown item type: ${name}`)
        return undefined
    }

}

export const SlimeModuleCstToAst = new SlimeModuleCstToAstSingle()
