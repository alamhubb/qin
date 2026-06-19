/**
 * ImportCstToAst - import é©ç¨¿å§æîå´²
 */
import { SubhutiCst } from "subhuti";
import {
    SlimeCallArgument, SlimeExpression,
    SlimeIdentifier, type SlimeImportDeclaration, SlimeImportDefaultSpecifier, SlimeImportNamespaceSpecifier,
    SlimeImportSpecifier, SlimeImportSpecifierItem, SlimeLiteral,
    type SlimeModuleDeclaration, SlimeAstTypeName, SlimePattern, type SlimeStatement,
    SlimeStringLiteral, SlimeTokenCreateUtils, SlimeVariableDeclarator, SlimeAstCreateUtils
} from "slime-ast";
import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";

import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import { SlimeVariableCstToAstSingle } from "../statements/SlimeVariableCstToAst.ts";

export class SlimeImportCstToAstSingle {

    createImportDeclarationAst(cst: SubhutiCst): SlimeImportDeclaration {
        let astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ImportDeclaration?.name);
        const raw = SlimeCstToAstUtils.extractCstRaw(cst)
        const value = (raw.startsWith('"') || raw.startsWith("'")) && raw.length >= 2
            ? raw.slice(1, -1)
            : raw
        const first = cst.getChildren()[0]
        const first1 = cst.getChildren()[1]
        let importDeclaration!: SlimeImportDeclaration

        // Token fields
        let importToken: any = undefined
        let semicolonToken: any = undefined

        // [TypeScript] å¦«â¬éã¦æ§¸éï¸½æ§¸ import type
        let importKind: 'type' | 'value' = 'value'

        // é»æ¬å½?import token
        if (first && (first.getName() === 'Import' || first.getValue() === 'import')) {
            importToken = SlimeTokenCreateUtils.createImportToken(first.getLoc())
        }

        // [TypeScript] éã¦å£?type éæ½æ­çæ¥ç´é¦?import æ¶å¬ªæé?
        for (let i = 1; i < cst.getChildren().length; i++) {
            const child = cst.getChildren()[i]
            if (child.getValue() === 'type' && child.getName() !== 'ImportClause') {
                // çº­î»ç¹?type é?import æ¶å¬ªæéå±¼ç¬æ¶å¶æ§?ImportClause éå´å´é¨?type
                importKind = 'type'
                break
            }
            // æ¿¡åçé¬å§å ImportClause é?ModuleSpecifieréå±½ä» å§ã¡ç¡é?
            if (child.getName() === SlimeParser.prototype.ImportClause?.name ||
                child.getName() === SlimeParser.prototype.ModuleSpecifier?.name) {
                break
            }
        }

        // éã¦å£?semicolon
        const semicolonCst = cst.getChildren().find(ch => ch.getName() === 'Semicolon' || ch.getValue() === ';')
        if (semicolonCst) {
            semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(semicolonCst.getLoc())
        } else {
            const semicolonAsiCst = cst.getChildren().find(ch => ch.getName() === 'SemicolonASI')
            const semicolonChild = semicolonAsiCst?.getChildren()?.find((ch: SubhutiCst) => ch.getName() === 'Semicolon' || ch.getValue() === ';')
            if (semicolonChild) {
                semicolonToken = SlimeTokenCreateUtils.createSemicolonToken(semicolonChild.getLoc())
            }
        }

        // éã¦å£?WithClause (ES2025 Import Attributes)
        const withClauseCst = cst.getChildren().find(ch =>
            ch.getName() === SlimeParser.prototype.WithClause?.name || ch.getName() === 'WithClause'
        )
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

        // éã¦å£?ImportClauseéå å½²é³èæ¹ª type éæ½æ­çæ¤¾ç®£éåº¯ç´
        const importClauseCst = cst.getChildren().find(ch =>
            ch.getName() === SlimeParser.prototype.ImportClause?.name || ch.getName() === 'ImportClause'
        )
        // éã¦å£?FromClause
        const fromClauseCst = cst.getChildren().find(ch =>
            ch.getName() === SlimeParser.prototype.FromClause?.name || ch.getName() === 'FromClause'
        )
        // éã¦å£?ModuleSpecifier (for side-effect imports)
        const moduleSpecifierCst = cst.getChildren().find(ch =>
            ch.getName() === SlimeParser.prototype.ModuleSpecifier?.name || ch.getName() === 'ModuleSpecifier'
        )

        if (importClauseCst && fromClauseCst) {
            const clauseResult = SlimeCstToAstUtils.createImportClauseAst(importClauseCst)
            const fromClause = SlimeCstToAstUtils.createFromClauseAst(fromClauseCst)
            importDeclaration = SlimeAstCreateUtils.createImportDeclaration(
                clauseResult.specifiers, fromClause.source, cst.getLoc(),
                importToken, fromClause.fromToken,
                clauseResult.lBraceToken, clauseResult.rBraceToken,
                semicolonToken, attributes, withToken
            )
        } else if (moduleSpecifierCst) {
            // import 'module' (side effect import) é?import 'module' with {...}
            const source = SlimeCstToAstUtils.createModuleSpecifierAst(moduleSpecifierCst)
            importDeclaration = SlimeAstCreateUtils.createImportDeclaration(
                [], source, cst.getLoc(),
                importToken, undefined,
                undefined, undefined,
                semicolonToken, attributes, withToken
            )
        } else if (first1.getName() === SlimeParser.prototype.ImportClause?.name) {
            // éçîéÑæ®æ¾¶å­æéç°ç´?
            const clauseResult = SlimeCstToAstUtils.createImportClauseAst(first1)
            const fromClause = SlimeCstToAstUtils.createFromClauseAst(cst.getChildren()[2])
            importDeclaration = SlimeAstCreateUtils.createImportDeclaration(
                clauseResult.specifiers, fromClause.source, cst.getLoc(),
                importToken, fromClause.fromToken,
                clauseResult.lBraceToken, clauseResult.rBraceToken,
                semicolonToken, attributes, withToken
            )
        } else if (first1.getName() === SlimeParser.prototype.ModuleSpecifier?.name) {
            // import 'module' (side effect import)
            const source = SlimeCstToAstUtils.createModuleSpecifierAst(first1)
            importDeclaration = SlimeAstCreateUtils.createImportDeclaration(
                [], source, cst.getLoc(),
                importToken, undefined,
                undefined, undefined,
                semicolonToken, attributes, withToken
            )
        }

        // [TypeScript] å¨£è¯²å§?importKind çç´â?
        if (importKind === 'type') {
            (importDeclaration as any).importKind = 'type'
        }
        if (withToken) {
            const decl = importDeclaration as any
            decl.attributesLBraceToken = attributesLBraceToken
            decl.attributesRBraceToken = attributesRBraceToken
        }

        return importDeclaration
    }

    createImportClauseAst(cst: SubhutiCst): {
        specifiers: Array<SlimeImportSpecifierItem>,
        lBraceToken?: any,
        rBraceToken?: any
    } {
        let astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ImportClause?.name);
        const result: Array<SlimeImportSpecifierItem> = []
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined
        const first = cst.getChildren()[0]

        if (first.getName() === SlimeParser.prototype.ImportedDefaultBinding?.name) {
            // æ¦æ¨¿î»çµçå?
            const specifier = SlimeCstToAstUtils.createImportedDefaultBindingAst(first)
            // éã¦å£éåº¨æ½°é¨å¯â¬æ¥å½?
            const commaCst = cst.getChildren().find(ch => ch.getName() === 'Comma' || ch.getValue() === ',')
            const commaToken = commaCst ? SlimeTokenCreateUtils.createCommaToken(commaCst.getLoc()) : undefined
            result.push(SlimeAstCreateUtils.createImportSpecifierItem(specifier, commaToken))

            // å¦«â¬éã¦æ§¸éï¹ç¹é?NamedImports é?NameSpaceImportéå è´©éå î±éã¯ç´?
            const namedImportsCst = cst.getChildren().find(ch =>
                ch.getName() === SlimeParser.prototype.NamedImports?.name || ch.getName() === 'NamedImports'
            )
            const namespaceImportCst = cst.getChildren().find(ch =>
                ch.getName() === SlimeParser.prototype.NameSpaceImport?.name || ch.getName() === 'NameSpaceImport'
            )

            if (namedImportsCst) {
                const namedResult = SlimeCstToAstUtils.createNamedImportsListAstWrapped(namedImportsCst)
                result.push(...namedResult.specifiers)
                lBraceToken = namedResult.lBraceToken
                rBraceToken = namedResult.rBraceToken
            } else if (namespaceImportCst) {
                result.push(SlimeAstCreateUtils.createImportSpecifierItem(
                    SlimeCstToAstUtils.createNameSpaceImportAst(namespaceImportCst), undefined
                ))
            }
        } else if (first.getName() === SlimeParser.prototype.NameSpaceImport?.name) {
            // import * as name from 'module'
            result.push(SlimeAstCreateUtils.createImportSpecifierItem(SlimeCstToAstUtils.createNameSpaceImportAst(first), undefined))
        } else if (first.getName() === SlimeParser.prototype.NamedImports?.name) {
            // import {name, greet} from 'module'
            const namedResult = SlimeCstToAstUtils.createNamedImportsListAstWrapped(first)
            result.push(...namedResult.specifiers)
            lBraceToken = namedResult.lBraceToken
            rBraceToken = namedResult.rBraceToken
        }

        return { specifiers: result, lBraceToken, rBraceToken }
    }

    /**
     * NamedImports CST æ?AST
     * NamedImports -> { } | { ImportsList } | { ImportsList , }
     */
    createNamedImportsAst(cst: SubhutiCst): Array<SlimeImportSpecifier> {
        // NamedImports: {LBrace, ImportsList?, RBrace}
        const importsList = cst.getChildren().find(ch => ch.getName() === SlimeParser.prototype.ImportsList?.name)
        if (!importsList) return []

        const specifiers: Array<SlimeImportSpecifier> = []
        for (const child of importsList.getChildren()) {
            if (child.getName() === SlimeParser.prototype.ImportSpecifier?.name) {
                // ImportSpecifieréå¤è¢±ç»å¶è°å¯®å¿¥ç´?
                // 1. ImportedBinding éå ¢çéæ¬ç´
                // 2. IdentifierName AsTok ImportedBinding éå ¥å¸éèæé?

                const identifierName = child.getChildren().find((ch: any) =>
                    ch.getName() === SlimeParser.prototype.IdentifierName?.name)
                const binding = child.getChildren().find((ch: any) =>
                    ch.getName() === SlimeParser.prototype.ImportedBinding?.name)

                if (identifierName && binding) {
                    // import {name as localName} é?import {default as MyClass} - é²å¶æ¡éå¶è°å¯?
                    const imported = SlimeCstToAstUtils.createIdentifierNameAst(identifierName)
                    const local = SlimeCstToAstUtils.createImportedBindingAst(binding)
                    specifiers.push({
                        type: SlimeAstTypeName.ImportSpecifier,
                        imported: imported,
                        local: local,
                        loc: child.getLoc()
                    } as any)
                } else if (binding) {
                    // import {name} - ç» â¬éæ¬è°å¯®?
                    const id = SlimeCstToAstUtils.createImportedBindingAst(binding)
                    specifiers.push({
                        type: SlimeAstTypeName.ImportSpecifier,
                        imported: id,
                        local: id,
                        loc: child.getLoc()
                    } as any)
                }
            }
        }
        return specifiers
    }

    /** æ©æ¿æ´éå°îç»«è¯²ç·é¨å­å¢éîç´éå­æ brace tokens */
    createNamedImportsListAstWrapped(cst: SubhutiCst): {
        specifiers: Array<SlimeImportSpecifierItem>,
        lBraceToken?: any,
        rBraceToken?: any
    } {
        // NamedImports: {LBrace, ImportsList?, RBrace}
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        // é»æ¬å½?brace tokens
        for (const child of cst.getChildren() || []) {
            if (child.getName() === 'LBrace' || child.getValue() === '{') {
                lBraceToken = SlimeTokenCreateUtils.createLBraceToken(child.getLoc())
            } else if (child.getName() === 'RBrace' || child.getValue() === '}') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(child.getLoc())
            }
        }

        const importsList = cst.getChildren().find(ch => ch.getName() === SlimeParser.prototype.ImportsList?.name)
        // ç»åæ¡éå¶î±é?import {} from "foo" - æ©æ¿æ´é?specifiers æµ£åæ¹?brace tokens
        if (!importsList) return { specifiers: [], lBraceToken, rBraceToken }

        const specifiers: Array<SlimeImportSpecifierItem> = []
        let currentSpec: SlimeImportSpecifier | null = null
        let hasSpec = false

        for (let i = 0; i < importsList.getChildren().length; i++) {
            const child = importsList.getChildren()[i]

            if (child.getName() === SlimeParser.prototype.ImportSpecifier?.name) {
                // æ¿¡åçæ¶å¬ªå¢ é?specifier æµ£åçéå¤â¬æ¥å½¿éå±½åéºã¥å?
                if (hasSpec) {
                    specifiers.push(SlimeAstCreateUtils.createImportSpecifierItem(currentSpec!, undefined))
                }

                // æµ£è·¨æ?createImportSpecifierAst éè§ç¡¶éã¦îçº­î¼î©é?type éæ½æ­ç?
                currentSpec = SlimeCstToAstUtils.createImportSpecifierAst(child)
                hasSpec = true
            } else if (child.getName() === 'Comma' || child.getValue() === ',') {
                // é«æ¥å½¿æ¶åº¡å¢ éã¢æ®?specifier é°å¶î?
                if (hasSpec) {
                    const commaToken = SlimeTokenCreateUtils.createCommaToken(child.getLoc())
                    specifiers.push(SlimeAstCreateUtils.createImportSpecifierItem(currentSpec!, commaToken))
                    hasSpec = false
                    currentSpec = null
                }
            }
        }

        // æ¾¶å­æéâ¬éåºç«´é?specifieréå çéå¤ç¬éå¿â¬æ¥å½¿é?
        if (hasSpec) {
            specifiers.push(SlimeAstCreateUtils.createImportSpecifierItem(currentSpec!, undefined))
        }

        // Handle trailing comma at NamedImports level (e.g., { a, })
        const trailingCommaCst = (cst.getChildren() || []).find(ch => ch.getName() === 'Comma' || ch.getValue() === ',')
        if (trailingCommaCst && specifiers.length > 0) {
            const lastItem = specifiers[specifiers.length - 1]
            if (!(lastItem as any).commaToken) {
                (lastItem as any).commaToken = SlimeTokenCreateUtils.createCommaToken(trailingCommaCst.getLoc())
            }
        }

        return { specifiers, lBraceToken, rBraceToken }
    }

    /**
     * ImportSpecifier CST é?AST
     * ImportSpecifier -> ImportedBinding | ModuleExportName as ImportedBinding
     */
    createImportSpecifierAst(cst: SubhutiCst): SlimeImportSpecifier {
        const children = cst.getChildren() || []
        let imported: SlimeIdentifier | null = null
        let local: SlimeIdentifier | null = null
        let asToken: any = undefined
        let importKind: 'type' | 'value' | undefined = undefined


        for (const child of children) {

            if (child.getName() === 'As' || child.getValue() === 'as') {
                asToken = SlimeTokenCreateUtils.createAsToken(child.getLoc())
            } else if (child.getName() === 'IdentifierName' && child.getValue() === 'type') {
                // [TypeScript] type éæ½æ­çæ¥ç´æîå§é¿î¼ç§éå±½æ¹ª CST æ¶îæ§?IdentifierNameé?
                importKind = 'type'
            } else if (child.getName() === SlimeParser.prototype.ImportedBinding?.name ||
                child.getName() === 'ImportedBinding') {
                local = SlimeCstToAstUtils.createImportedBindingAst(child)
            } else if (child.getName() === SlimeParser.prototype.ModuleExportName?.name ||
                child.getName() === 'ModuleExportName' ||
                child.getName() === SlimeParser.prototype.IdentifierName?.name ||
                child.getName() === 'IdentifierName') {
                if (!imported) {
                    imported = SlimeCstToAstUtils.createModuleExportNameAst(child) as SlimeIdentifier
                }
            }
        }

        // æ¿¡åçå¨âæ¹ aséå®¨mported é?local é©ç¨¿æ?
        if (!local && imported) {
            local = { ...imported }
        }
        if (!imported && local) {
            imported = { ...local }
        }

        if (!imported || !local) {
            throw new Error('Invalid ImportSpecifier CST: empty or incomplete specifier')
        }

        return SlimeAstCreateUtils.createImportSpecifier(imported, local, asToken, importKind)
    }


    /**
     * éæ¶ç¼?ImportCall AST
     * ImportCall: import ( AssignmentExpression ,_opt )
     *           | import ( AssignmentExpression , AssignmentExpression ,_opt )
     */
    createImportCallAst(cst: SubhutiCst): SlimeExpression {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ImportCall?.name);
        // ImportCall -> ImportTok + LParen + AssignmentExpression + (Comma + AssignmentExpression)? + Comma? + RParen
        // children: [ImportTok, LParen, AssignmentExpression, (Comma, AssignmentExpression)?, Comma?, RParen]

        const args: SlimeCallArgument[] = []
        const commaTokens: any[] = []
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let importToken: any = undefined

        for (const child of cst.getChildren()) {
            if (child.getName() === 'Import' || child.getValue() === 'import') {
                importToken = SlimeTokenCreateUtils.createImportToken(child.getLoc())
            } else if (child.getName() === 'LParen' || child.getValue() === '(') {
                lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
            } else if (child.getName() === 'RParen' || child.getValue() === ')') {
                rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
            } else if (child.getName() === 'Comma' || child.getValue() === ',') {
                commaTokens.push(SlimeTokenCreateUtils.createCommaToken(child.getLoc()))
            } else if (child.getName() === SlimeParser.prototype.AssignmentExpression?.name) {
                const expr = SlimeCstToAstUtils.createAssignmentExpressionAst(child)
                args.push(SlimeAstCreateUtils.createCallArgument(expr))
            }
        }

        for (let i = 0; i < args.length; i++) {
            if (commaTokens[i]) {
                (args[i] as any).commaToken = commaTokens[i]
            }
        }

        // éæ¶ç¼?import éåªçç»ï¸¿ç¶é?callee
        const importIdentifier: SlimeIdentifier = SlimeAstCreateUtils.createIdentifier('import', importToken?.loc || cst.getChildren()[0].loc)

        return SlimeAstCreateUtils.createCallExpression(
            importIdentifier, args, cst.getLoc(), lParenToken, rParenToken
        ) as SlimeExpression
    }


    /**
     * NameSpaceImport CST é?AST
     * NameSpaceImport -> * as ImportedBinding
     */
    createNameSpaceImportAst(cst: SubhutiCst): SlimeImportNamespaceSpecifier {
        // NameSpaceImport: Asterisk as ImportedBinding
        // children: [Asterisk, AsTok, ImportedBinding]
        let asteriskToken: any = undefined
        let asToken: any = undefined

        for (const child of cst.getChildren()) {
            if (child.getName() === 'Asterisk' || child.getValue() === '*') {
                asteriskToken = SlimeTokenCreateUtils.createAsteriskToken(child.getLoc())
            } else if (child.getName() === 'As' || child.getValue() === 'as') {
                asToken = SlimeTokenCreateUtils.createAsToken(child.getLoc())
            }
        }

        const binding = cst.getChildren().find(ch => ch.name === SlimeParser.prototype.ImportedBinding?.name)
        if (!binding) throw new Error('NameSpaceImport missing ImportedBinding')
        const local = SlimeCstToAstUtils.createImportedBindingAst(binding)

        return SlimeAstCreateUtils.createImportNamespaceSpecifier(local, cst.getLoc(), asteriskToken, asToken)
    }


    /**
     * ImportsList CST é?AST
     * ImportsList -> ImportSpecifier (, ImportSpecifier)*
     */
    createImportsListAst(cst: SubhutiCst): Array<SlimeImportSpecifier> {
        const specifiers: SlimeImportSpecifier[] = []
        for (const child of cst.getChildren() || []) {
            if (child.getName() === SlimeParser.prototype.ImportSpecifier?.name ||
                child.getName() === 'ImportSpecifier') {
                specifiers.push(SlimeCstToAstUtils.createImportSpecifierAst(child))
            }
        }
        return specifiers
    }


    /**
     * AttributeKey CST é?AST
     * AttributeKey -> IdentifierName | StringLiteral
     */
    createAttributeKeyAst(cst: SubhutiCst): SlimeIdentifier | SlimeLiteral {
        const firstChild = cst.getChildren()?.[0]
        if (!firstChild) throw new Error('AttributeKey has no children')

        if (firstChild.getName() === SlimeParser.prototype.IdentifierName?.name ||
            firstChild.getName() === 'IdentifierName' ||
            firstChild.getValue() !== undefined && !firstChild.getValue().startsWith('"') && !firstChild.getValue().startsWith("'")) {
            return SlimeCstToAstUtils.createIdentifierNameAst(firstChild)
        } else {
            return SlimeCstToAstUtils.createStringLiteralAst(firstChild)
        }
    }


    /**
     * WithEntries CST é?AST
     * WithEntries -> AttributeKey : StringLiteral (, AttributeKey : StringLiteral)*
     */
    createWithEntriesAst(cst: SubhutiCst): any[] {
        const entries: any[] = []
        let currentKey: any = null

        for (const child of cst.getChildren() || []) {
            if (child.getName() === SlimeParser.prototype.AttributeKey?.name ||
                child.getName() === 'AttributeKey') {
                currentKey = SlimeCstToAstUtils.createAttributeKeyAst(child)
            } else if (child.getName() === 'StringLiteral' ||
                (child.getValue() && (child.getValue().startsWith('"') || child.getValue().startsWith("'")))) {
                if (currentKey) {
                    entries.push({
                        type: 'ImportAttribute',
                        key: currentKey,
                        value: SlimeCstToAstUtils.createStringLiteralAst(child)
                    })
                    currentKey = null
                }
            }
        }

        return entries
    }


    /** ?? WithClause: with { type: "json" } */
    createWithClauseAst(cst: SubhutiCst): { attributes: any[], withToken: any, lBraceToken?: any, rBraceToken?: any } {
        // WithClause: With, LBrace, WithEntries?, RBrace
        let withToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined
        const attributes: any[] = []

        const parseWithEntries = (entriesCst: SubhutiCst) => {
            let currentKey: any = null
            let currentColonToken: any = undefined
            for (const entry of entriesCst.children || []) {
                if (entry.name === SlimeParser.prototype.WithEntries?.name || entry.name === 'WithEntries') {
                    parseWithEntries(entry)
                    continue
                }
                if (entry.name === SlimeParser.prototype.AttributeKey?.name || entry.name === 'AttributeKey') {
                    currentKey = SlimeCstToAstUtils.createAttributeKeyAst(entry)
                    currentColonToken = undefined
                } else if (entry.name === 'Colon' || entry.value === ':') {
                    currentColonToken = SlimeTokenCreateUtils.createColonToken(entry.loc)
                } else if (entry.name === 'StringLiteral' || entry.value?.startsWith('"') || entry.value?.startsWith("'")) {
                    if (currentKey) {
                        attributes.push({
                            type: 'ImportAttribute',
                            key: currentKey,
                            value: SlimeCstToAstUtils.createStringLiteralAst(entry),
                            colonToken: currentColonToken
                        })
                        currentKey = null
                        currentColonToken = undefined
                    }
                } else if (entry.name === 'Comma' || entry.value === ',') {
                    const lastAttr = attributes[attributes.length - 1]
                    if (lastAttr && !(lastAttr as any).commaToken) {
                        (lastAttr as any).commaToken = SlimeTokenCreateUtils.createCommaToken(entry.loc)
                    }
                }
            }
        }

        for (const child of cst.getChildren() || []) {
            if (child.getName() === 'With' || child.getValue() === 'with') {
                withToken = SlimeTokenCreateUtils.createWithToken(child.getLoc())
            } else if (child.getName() === 'LBrace' || child.getValue() === '{') {
                lBraceToken = SlimeTokenCreateUtils.createLBraceToken(child.getLoc())
            } else if (child.getName() === 'RBrace' || child.getValue() === '}') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(child.getLoc())
            } else if (child.getName() === SlimeParser.prototype.WithEntries?.name || child.getName() === 'WithEntries') {
                parseWithEntries(child)
            }
        }

        const trailingCommaCst = (cst.getChildren() || []).find(ch => ch.name === 'Comma' || ch.value === ',')
        if (trailingCommaCst && attributes.length > 0) {
            const lastAttr = attributes[attributes.length - 1]
            if (!(lastAttr as any).commaToken) {
                (lastAttr as any).commaToken = SlimeTokenCreateUtils.createCommaToken(trailingCommaCst.getLoc())
            }
        }

        return { attributes, withToken, lBraceToken, rBraceToken }
    }


    createFromClauseAst(cst: SubhutiCst): { source: SlimeStringLiteral, fromToken?: any } {
        let astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.FromClause?.name);
        const first = cst.getChildren()[0]
        const ModuleSpecifier = SlimeCstToAstUtils.createModuleSpecifierAst(cst.getChildren()[1])

        // é»æ¬å½?from token
        let fromToken: any = undefined
        if (first && (first.getName() === 'From' || first.getValue() === 'from')) {
            fromToken = SlimeTokenCreateUtils.createFromToken(first.getLoc())
        }

        return {
            source: ModuleSpecifier,
            fromToken: fromToken
        }
    }

    createModuleSpecifierAst(cst: SubhutiCst): SlimeStringLiteral {
        let astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ModuleSpecifier?.name);
        const raw = SlimeCstToAstUtils.extractCstRaw(cst)
        const value = (raw.startsWith('"') || raw.startsWith("'")) && raw.length >= 2
            ? raw.slice(1, -1)
            : raw
        return SlimeAstCreateUtils.createStringLiteral(value, cst.getLoc(), raw)
    }


    createImportedDefaultBindingAst(cst: SubhutiCst): SlimeImportDefaultSpecifier {
        let astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ImportedDefaultBinding?.name);
        const first = cst.getChildren()[0]
        const id = SlimeCstToAstUtils.createImportedBindingAst(first)
        const importDefaultSpecifier: SlimeImportDefaultSpecifier = SlimeAstCreateUtils.createImportDefaultSpecifier(id)
        return importDefaultSpecifier
    }

    createImportedBindingAst(cst: SubhutiCst): SlimeIdentifier {
        let astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ImportedBinding?.name);
        const first = cst.getChildren()[0]
        return SlimeCstToAstUtils.createBindingIdentifierAst(first)
    }


}

export const SlimeImportCstToAst = new SlimeImportCstToAstSingle()
