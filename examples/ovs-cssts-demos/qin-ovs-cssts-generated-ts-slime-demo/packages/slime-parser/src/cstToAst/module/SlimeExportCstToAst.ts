/**
 * ExportCstToAst - export 鐩稿叧杞崲
 */
import {SubhutiCst} from "subhuti";
import {

    SlimeExportAllDeclaration,
    SlimeExportDefaultDeclaration,
    SlimeExportNamedDeclaration, SlimeExportSpecifier, SlimeExportSpecifierItem, SlimeFunctionParam, SlimeIdentifier,
    SlimeLiteral,
    SlimeModuleDeclaration, SlimePattern,
    SlimeStatement, SlimeTokenCreateUtils, SlimeAstCreateUtils
} from "slime-ast";
import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";

import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import { SlimeTokenConsumer } from "../../SlimeTokenConsumer.ts";
import {SlimeVariableCstToAstSingle} from "../statements/SlimeVariableCstToAst.ts";

export class SlimeExportCstToAstSingle {

    createExportDeclarationAst(cst: SubhutiCst): SlimeExportDefaultDeclaration | SlimeExportNamedDeclaration | SlimeExportAllDeclaration {
        let astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ExportDeclaration?.name);
        const children = cst.getChildren() || []

        // Token fields
        let exportToken: any = undefined
        let defaultToken: any = undefined
        let asteriskToken: any = undefined
        let semicolonToken: any = undefined
        let asToken: any = undefined

        // [TypeScript] 妫€鏌ユ槸鍚︽槸 export type
        let exportKind: 'type' | 'value' = 'value'

        // 閬嶅巻瀛愯妭鐐规彁鍙栦俊鎭?
        let exportFromClause: SubhutiCst | null = null
        let fromClause: SubhutiCst | null = null
        let namedExports: SubhutiCst | null = null
        let variableStatement: SubhutiCst | null = null
        let declaration: SubhutiCst | null = null
        let hoistableDeclaration: SubhutiCst | null = null
        let classDeclaration: SubhutiCst | null = null
        let assignmentExpression: SubhutiCst | null = null
        let withClauseCst: SubhutiCst | null = null
        let isDefault = false

        for (let i = 0; i < children.length; i++) {
            const child = children[i]
            const name = child.getName()
            if (name === SlimeTokenConsumer.prototype.Export?.name || child.getValue() === 'export') {
                exportToken = SlimeTokenCreateUtils.createExportToken(child.getLoc())
            } else if (name === SlimeTokenConsumer.prototype.Default?.name || child.getValue() === 'default') {
                defaultToken = SlimeTokenCreateUtils.createDefaultToken(child.getLoc())
                isDefault = true
            } else if (name === SlimeTokenConsumer.prototype.Asterisk?.name || child.getValue() === '*') {
                asteriskToken = SlimeTokenCreateUtils.createAsteriskToken(child.getLoc())
            } else if (name === SlimeTokenConsumer.prototype.Semicolon?.name || child.getValue() === ';') {
                semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(child.getLoc())
            } else if (name === 'SemicolonASI') {
                const semicolonCst = child.getChildren()?.find((ch: any) => ch.name === 'Semicolon' || ch.value === ';')
                if (semicolonCst) {
                    semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(semicolonCst.getLoc())
                }
            } else if (name === SlimeTokenConsumer.prototype.As?.name || child.getValue() === 'as') {
                asToken = SlimeTokenCreateUtils.createAsToken(child.getLoc())
            } else if (child.getValue() === 'type' && i > 0) {
                // [TypeScript] 鏌ユ壘 type 鍏抽敭瀛楋紙鍦?export 涔嬪悗锛?
                const prevChild = children[i - 1]
                if (prevChild.name === SlimeTokenConsumer.prototype.Export?.name || prevChild.value === 'export') {
                    exportKind = 'type'
                }
            } else if (name === SlimeParser.prototype.ExportFromClause?.name) {
                exportFromClause = child
            } else if (name === SlimeParser.prototype.FromClause?.name) {
                fromClause = child
            } else if (name === SlimeParser.prototype.NamedExports?.name) {
                namedExports = child
            } else if (name === SlimeParser.prototype.VariableStatement?.name) {
                variableStatement = child
            } else if (name === SlimeParser.prototype.Declaration?.name) {
                declaration = child
            } else if (name === SlimeParser.prototype.HoistableDeclaration?.name) {
                hoistableDeclaration = child
            } else if (name === SlimeParser.prototype.ClassDeclaration?.name) {
                classDeclaration = child
            } else if (name === SlimeParser.prototype.AssignmentExpression?.name) {
                assignmentExpression = child
            } else if (name === SlimeParser.prototype.WithClause?.name || name === 'WithClause') {
                withClauseCst = child
            }
        }

        // 瑙ｆ瀽 WithClause (ES2025 Import Attributes)
        let attributes: any[] = []
        let withToken: any = undefined
        let attributesLBraceToken: any = undefined
        let attributesRBraceToken: any = undefined
        if (withClauseCst) {
            const parsed = SlimeCstToAstUtils.createWithClauseAst(withClauseCst)
            attributes = parsed.attributes
            withToken = parsed.withToken
            attributesLBraceToken = (parsed as any).lBraceToken
            attributesRBraceToken = (parsed as any).rBraceToken
        }

        // export default ...
        if (isDefault) {
            let decl: any = null
            let isExpressionDefault = false
            if (hoistableDeclaration) {
                decl = SlimeCstToAstUtils.createHoistableDeclarationAst(hoistableDeclaration)
            } else if (classDeclaration) {
                decl = SlimeCstToAstUtils.createClassDeclarationAst(classDeclaration)
            } else if (assignmentExpression) {
                decl = SlimeCstToAstUtils.createAssignmentExpressionAst(assignmentExpression)
                isExpressionDefault = true
            }
            const result = SlimeAstCreateUtils.createExportDefaultDeclaration(decl, cst.getLoc(), exportToken, defaultToken) as any
            if (isExpressionDefault && semicolonToken) {
                result.semicolonToken = semicolonToken
            }
            return result
        }

        // export ExportFromClause FromClause ; (export * from ... or export { } from ...)
        if (exportFromClause && fromClause) {
            const fromClauseResult = SlimeCstToAstUtils.createFromClauseAst(fromClause)

            // Check if it's export * or export * as name
            const hasAsterisk = exportFromClause.children?.some((ch: any) =>
                ch.name === SlimeTokenConsumer.prototype.Asterisk?.name || ch.value === '*')

            if (hasAsterisk) {
                // export * from ... or export * as name from ...
                let localAsteriskToken = asteriskToken
                let localAsToken = asToken
                if (!localAsteriskToken || !localAsToken) {
                    for (const child of exportFromClause.children || []) {
                        if (!localAsteriskToken &&
                            (child.getName() === SlimeTokenConsumer.prototype.Asterisk?.name || child.getValue() === '*')) {
                            localAsteriskToken = SlimeTokenCreateUtils.createAsteriskToken(child.getLoc())
                        } else if (!localAsToken &&
                            (child.getName() === SlimeTokenConsumer.prototype.As?.name || child.getValue() === 'as')) {
                            localAsToken = SlimeTokenCreateUtils.createAsToken(child.getLoc())
                        }
                    }
                }
                let exported: any = null
                const moduleExportName = exportFromClause.children?.find((ch: any) =>
                    ch.name === SlimeParser.prototype.ModuleExportName?.name)
                if (moduleExportName) {
                    exported = SlimeCstToAstUtils.createModuleExportNameAst(moduleExportName)
                }
                const result = SlimeAstCreateUtils.createExportAllDeclaration(
                    fromClauseResult.source, exported, cst.getLoc(),
                    exportToken, localAsteriskToken, localAsToken, fromClauseResult.fromToken, semicolonToken
                ) as any
                // 娣诲姞 attributes锛堝鏋滄湁 withToken锛屽嵆浣?attributes 涓虹┖涔熻娣诲姞锛?
                if (withToken) {
                    result.attributes = attributes
                    result.withToken = withToken
                    result.attributesLBraceToken = attributesLBraceToken
                    result.attributesRBraceToken = attributesRBraceToken
                }
                // [TypeScript] 娣诲姞 exportKind
                if (exportKind === 'type') {
                    result.exportKind = 'type'
                }
                return result
            } else {
                // export { ... } from ...
                // exportFromClause 鐨勭粨鏋勬槸 [NamedExports]锛岄渶瑕佷粠涓彁鍙?NamedExports
                const namedExportsCst = exportFromClause.children?.find((ch: any) =>
                    ch.name === SlimeParser.prototype.NamedExports?.name || ch.name === 'NamedExports'
                )
                const namedExportsResult = namedExportsCst
                    ? this.createNamedExportsAstWrapped(namedExportsCst)
                    : { specifiers: [], lBraceToken: undefined, rBraceToken: undefined }
                const result = SlimeAstCreateUtils.createExportNamedDeclaration(
                    null, namedExportsResult.specifiers, fromClauseResult.source, cst.getLoc(),
                    exportToken, fromClauseResult.fromToken,
                    namedExportsResult.lBraceToken, namedExportsResult.rBraceToken, semicolonToken
                )
                // 娣诲姞 attributes锛堝鏋滄湁 withToken锛屽嵆浣?attributes 涓虹┖涔熻娣诲姞锛?
                if (withToken) {
                    const resultAny = result as any
                    resultAny.attributes = attributes
                    resultAny.withToken = withToken
                    resultAny.attributesLBraceToken = attributesLBraceToken
                    resultAny.attributesRBraceToken = attributesRBraceToken
                }
                // [TypeScript] 娣诲姞 exportKind
                if (exportKind === 'type') {
                    (result as any).exportKind = 'type'
                }
                return result
            }
        }

        // export NamedExports FromClause? ; (export { ... } from 'module' OR export { ... })
        if (namedExports) {
            const namedExportsResult = this.createNamedExportsAstWrapped(namedExports)
            // [TypeScript] 妫€鏌ユ槸鍚︽湁 FromClause (export type { ... } from 'module')
            if (fromClause) {
                const fromClauseResult = SlimeCstToAstUtils.createFromClauseAst(fromClause)
                const result = SlimeAstCreateUtils.createExportNamedDeclaration(
                    null, namedExportsResult.specifiers, fromClauseResult.source, cst.getLoc(),
                    exportToken, fromClauseResult.fromToken,
                    namedExportsResult.lBraceToken, namedExportsResult.rBraceToken, semicolonToken
                )
                // [TypeScript] 娣诲姞 exportKind
                if (exportKind === 'type') {
                    (result as any).exportKind = 'type'
                }
                return result
            } else {
                const result = SlimeAstCreateUtils.createExportNamedDeclaration(
                    null, namedExportsResult.specifiers, null, cst.getLoc(),
                    exportToken, undefined,
                    namedExportsResult.lBraceToken, namedExportsResult.rBraceToken, semicolonToken
                )
                // [TypeScript] 娣诲姞 exportKind
                if (exportKind === 'type') {
                    (result as any).exportKind = 'type'
                }
                return result
            }
        }

        // export VariableStatement
        if (variableStatement) {
            const decl = SlimeCstToAstUtils.createVariableStatementAst(variableStatement)
            return SlimeAstCreateUtils.createExportNamedDeclaration(
                decl, [], null, cst.getLoc(), exportToken
            )
        }

        // export Declaration
        if (declaration) {
            const decl = SlimeCstToAstUtils.createDeclarationAst(declaration)
            return SlimeAstCreateUtils.createExportNamedDeclaration(
                decl, [], null, cst.getLoc(), exportToken
            )
        }

        throw new Error(`Unsupported export declaration structure`)
    }


    /**
     * ExportFromClause CST 锟?AST
     * ExportFromClause -> * | * as ModuleExportName | NamedExports
     */
    createExportFromClauseAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []

        // 妫€鏌ユ槸鍚︽槸 * (export all)
        const asterisk = children.find(ch => ch.name === 'Asterisk' || ch.value === '*')
        if (asterisk) {
            const asTok = children.find(ch => ch.name === 'As' || ch.value === 'as')
            const exportedName = children.find(ch =>
                ch.name === SlimeParser.prototype.ModuleExportName?.name ||
                ch.name === 'ModuleExportName'
            )

            if (asTok && exportedName) {
                // * as name
                return {
                    type: 'exportAll',
                    exported: SlimeCstToAstUtils.createModuleExportNameAst(exportedName)
                }
            } else {
                // * (export all)
                return {type: 'exportAll', exported: null}
            }
        }

        // NamedExports
        const namedExports = children.find(ch =>
            ch.name === SlimeParser.prototype.NamedExports?.name ||
            ch.name === 'NamedExports'
        )
        if (namedExports) {
            return {
                type: 'namedExports',
                specifiers: SlimeCstToAstUtils.createNamedExportsAst(namedExports)
            }
        }

        return {type: 'unknown'}
    }


    /**
     * 鍒涘缓 NamedExports AST (export { a, b, c })
     */
    createNamedExportsAst(cst: SubhutiCst): SlimeExportSpecifierItem[] {
        // NamedExports: { ExportsList? }
        const specifiers: SlimeExportSpecifierItem[] = []
        let lastSpecifier: SlimeExportSpecifier | null = null

        for (const child of cst.getChildren() || []) {
            if (child.getName() === SlimeParser.prototype.ExportsList?.name) {
                if (lastSpecifier) {
                    specifiers.push({specifier: lastSpecifier})
                    lastSpecifier = null
                }
                const listSpecifiers = SlimeCstToAstUtils.createExportsListAst(child)
                specifiers.push(...listSpecifiers)
            } else if (child.getName() === SlimeParser.prototype.ExportSpecifier?.name) {
                if (lastSpecifier) {
                    specifiers.push({specifier: lastSpecifier})
                }
                lastSpecifier = SlimeCstToAstUtils.createExportSpecifierAst(child)
            } else if (child.getName() === SlimeTokenConsumer.prototype.Comma?.name || child.getValue() === ',') {
                if (lastSpecifier) {
                    specifiers.push({
                        specifier: lastSpecifier,
                        commaToken: SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                    })
                    lastSpecifier = null
                } else if (specifiers.length > 0) {
                    const lastItem = specifiers[specifiers.length - 1]
                    if (!lastItem.commaToken) {
                        lastItem.commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                    }
                }
            }
        }

        if (lastSpecifier) {
            specifiers.push({specifier: lastSpecifier})
        }

        return specifiers
    }

    createNamedExportsAstWrapped(cst: SubhutiCst): {
        specifiers: SlimeExportSpecifierItem[],
        lBraceToken?: any,
        rBraceToken?: any
    } {
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        for (const child of cst.getChildren() || []) {
            if (child.getName() === 'LBrace' || child.getValue() === '{') {
                lBraceToken = SlimeTokenCreateUtils.createLBraceToken(child.getLoc())
            } else if (child.getName() === 'RBrace' || child.getValue() === '}') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(child.getLoc())
            }
        }

        const specifiers = this.createNamedExportsAst(cst)
        return { specifiers, lBraceToken, rBraceToken }
    }

    /**
     * 鍒涘缓 ExportsList AST
     */
    createExportsListAst(cst: SubhutiCst): SlimeExportSpecifierItem[] {
        const specifiers: SlimeExportSpecifierItem[] = []
        let lastSpecifier: SlimeExportSpecifier | null = null

        for (const child of cst.getChildren() || []) {
            if (child.getName() === SlimeParser.prototype.ExportSpecifier?.name) {
                if (lastSpecifier) {
                    specifiers.push({specifier: lastSpecifier})
                }
                lastSpecifier = SlimeCstToAstUtils.createExportSpecifierAst(child)
            } else if (child.getName() === SlimeTokenConsumer.prototype.Comma?.name || child.getValue() === ',') {
                if (lastSpecifier) {
                    specifiers.push({
                        specifier: lastSpecifier,
                        commaToken: SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                    })
                    lastSpecifier = null
                }
            }
        }

        if (lastSpecifier) {
            specifiers.push({specifier: lastSpecifier})
        }

        return specifiers
    }

    /**
     * 鍒涘缓 ExportSpecifier AST
     */
    createExportSpecifierAst(cst: SubhutiCst): SlimeExportSpecifier {
        // ExportSpecifier: ModuleExportName | ModuleExportName as ModuleExportName
        const children = cst.getChildren() || []
        let local: any = null
        let exported: any = null
        let asToken: any = undefined

        for (let i = 0; i < children.length; i++) {
            const child = children[i]
            if (child.getName() === SlimeParser.prototype.ModuleExportName?.name) {
                if (!local) {
                    local = SlimeCstToAstUtils.createModuleExportNameAst(child)
                } else {
                    exported = SlimeCstToAstUtils.createModuleExportNameAst(child)
                }
            } else if (child.getName() === SlimeTokenConsumer.prototype.As?.name || child.getValue() === 'as') {
                asToken = SlimeTokenCreateUtils.createAsToken(child.getLoc())
            }
        }

        // If no 'as', exported is same as local
        if (!exported) {
            exported = local
        }

        return SlimeAstCreateUtils.createExportSpecifier(local, exported, cst.getLoc(), asToken)
    }

    /**
     * 鍒涘缓 ModuleExportName AST
     */
    createModuleExportNameAst(cst: SubhutiCst): SlimeIdentifier | SlimeLiteral {
        const first = cst.getChildren()?.[0]
        if (!first) {
            throw new Error('ModuleExportName has no children')
        }

        if (first.getName() === SlimeParser.prototype.IdentifierName?.name) {
            return SlimeCstToAstUtils.createIdentifierNameAst(first)
        } else if (first.getName() === SlimeTokenConsumer.prototype.StringLiteral?.name) {
            return SlimeCstToAstUtils.createStringLiteralAst(first)
        } else {
            // Direct token
            return SlimeAstCreateUtils.createIdentifier(first.getValue(), first.getLoc())
        }
    }

}

export const SlimeExportCstToAst = new SlimeExportCstToAstSingle()
