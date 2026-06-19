/**
 * SlimeTSKeywordTypeCstToAst - TypeScript 鍏抽敭瀛楃被鍨?
 *
 * 璐熻矗锛?
 * - createTSKeywordTypeWrapperAst
 * - createTSKeywordTypeAst
 * - createTSLiteralTypeAst
 */
import { SubhutiCst } from "subhuti";
import { SlimeAstCreateUtils, SlimeAstTypeName } from "slime-ast";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

export class SlimeTSKeywordTypeCstToAstSingle {
    /**
     * [TypeScript] 杞崲 TSKeywordType 鍖呰瑙勫垯 CST 涓?AST
     */
    createTSKeywordTypeWrapperAst(cst: SubhutiCst): any {
        const child = cst.getChildren()?.[0]
        if (!child) {
            throw new Error('TSKeywordType has no children')
        }

        const name = child.getName()

        // 鍩虹绫诲瀷鍏抽敭瀛?
        if (name === 'TSNumberKeyword') return SlimeCstToAstUtils.createTSKeywordTypeAst(child, SlimeAstTypeName.TSNumberKeyword)
        if (name === 'TSStringKeyword') return SlimeCstToAstUtils.createTSKeywordTypeAst(child, SlimeAstTypeName.TSStringKeyword)
        if (name === 'TSBooleanKeyword') return SlimeCstToAstUtils.createTSKeywordTypeAst(child, SlimeAstTypeName.TSBooleanKeyword)
        if (name === 'TSAnyKeyword') return SlimeCstToAstUtils.createTSKeywordTypeAst(child, SlimeAstTypeName.TSAnyKeyword)
        if (name === 'TSUnknownKeyword') return SlimeCstToAstUtils.createTSKeywordTypeAst(child, SlimeAstTypeName.TSUnknownKeyword)
        if (name === 'TSNeverKeyword') return SlimeCstToAstUtils.createTSKeywordTypeAst(child, SlimeAstTypeName.TSNeverKeyword)
        if (name === 'TSUndefinedKeyword') return SlimeCstToAstUtils.createTSKeywordTypeAst(child, SlimeAstTypeName.TSUndefinedKeyword)
        if (name === 'TSNullKeyword') return SlimeCstToAstUtils.createTSKeywordTypeAst(child, SlimeAstTypeName.TSNullKeyword)
        if (name === 'TSVoidKeyword') return SlimeCstToAstUtils.createTSKeywordTypeAst(child, SlimeAstTypeName.TSVoidKeyword)
        if (name === 'TSObjectKeyword') return SlimeCstToAstUtils.createTSKeywordTypeAst(child, SlimeAstTypeName.TSObjectKeyword)
        if (name === 'TSSymbolKeyword') return SlimeCstToAstUtils.createTSKeywordTypeAst(child, SlimeAstTypeName.TSSymbolKeyword)
        if (name === 'TSBigIntKeyword') return SlimeCstToAstUtils.createTSKeywordTypeAst(child, SlimeAstTypeName.TSBigIntKeyword)

        throw new Error(`Unknown TSKeywordType child: ${name}`)
    }

    /**
     * [TypeScript] 鍒涘缓鍏抽敭瀛楃被鍨?AST
     */
    createTSKeywordTypeAst(cst: SubhutiCst, typeName: string): any {
        return {
            type: typeName,
            loc: cst.getLoc(),
        }
    }

    /**
     * [TypeScript] 杞崲 TSLiteralType CST 涓?AST
     */
    createTSLiteralTypeAst(cst: SubhutiCst): any {
        const child = cst.getChildren()?.[0]
        if (!child) {
            throw new Error('TSLiteralType has no children')
        }

        // 鑾峰彇瀛楅潰閲忓€?
        let literal: any
        if (child.getName() === 'StringLiteral' || child.getName() === 'Literal') {
            const tokenCst = child.getChildren()?.[0] || child
            literal = {
                type: 'Literal',
                value: tokenCst.getValue(),
                raw: tokenCst.getValue(),
                loc: tokenCst.getLoc(),
            }
        } else if (child.getName() === 'NumericLiteral') {
            const tokenCst = child.getChildren()?.[0] || child
            literal = {
                type: 'Literal',
                value: Number(tokenCst.getValue()),
                raw: tokenCst.getValue(),
                loc: tokenCst.getLoc(),
            }
        } else if (child.getName() === 'TrueTok' || child.getValue() === 'true') {
            literal = {
                type: 'Literal',
                value: true,
                raw: 'true',
                loc: child.getLoc(),
            }
        } else if (child.getName() === 'FalseTok' || child.getValue() === 'false') {
            literal = {
                type: 'Literal',
                value: false,
                raw: 'false',
                loc: child.getLoc(),
            }
        } else {
            // 灏濊瘯浠?token 鑾峰彇鍊?
            const tokenCst = child.getChildren()?.[0] || child
            literal = {
                type: 'Literal',
                value: tokenCst.getValue(),
                raw: tokenCst.getValue(),
                loc: tokenCst.getLoc(),
            }
        }

        return SlimeAstCreateUtils.createTSLiteralType(literal, cst.getLoc())
    }
}

export const SlimeTSKeywordTypeCstToAst = new SlimeTSKeywordTypeCstToAstSingle()
