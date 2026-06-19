/**
 * SlimeTSTypeAnnotationCstToAst - TypeScript 绫诲瀷娉ㄨВ鍏ュ彛
 *
 * 璐熻矗锛?
 * - createTSTypeAnnotationAst
 * - createTSTypeAst (鍏ュ彛鍒嗗彂)
 */
import { SubhutiCst } from "subhuti";
import { SlimeAstCreateUtils, SlimeAstTypeName, SlimeTokenCreateUtils } from "slime-ast";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

export class SlimeTSTypeAnnotationCstToAstSingle {

    /**
     * [TypeScript] 杞崲 TSTypeAnnotation CST 涓?AST
     */
    createTSTypeAnnotationAst(cst: SubhutiCst): any {
        const children = cst.getChildren() || []
        if (children.length < 2) {
            throw new Error(`TSTypeAnnotation expected at least 2 children, got ${children.length}`)
        }

        const typeCst = children[1]
        const typeAnnotation = SlimeCstToAstUtils.createTSTypeAst(typeCst)

        return SlimeAstCreateUtils.createTSTypeAnnotation(typeAnnotation, cst.getLoc())
    }

    /**
     * [TypeScript] 杞崲 TSType CST 涓?AST
     * 鏀寔鎵€鏈夊凡瀹炵幇鐨?TypeScript 绫诲瀷
     */
    createTSTypeAst(cst: SubhutiCst): any {
        const child = cst.getChildren()?.[0]
        if (!child) {
            throw new Error('TSType has no children')
        }

        const name = child.getName()

        // 鍑芥暟绫诲瀷
        if (name === 'TSFunctionType') {
            return SlimeCstToAstUtils.createTSFunctionTypeAst(child)
        }
        if (name === 'TSConstructorType') {
            return SlimeCstToAstUtils.createTSConstructorTypeAst(child)
        }

        // 鏉′欢绫诲瀷锛堝寘鍚仈鍚?浜ゅ弶绫诲瀷锛?
        if (name === 'TSConditionalType') {
            return SlimeCstToAstUtils.createTSConditionalTypeAst(child)
        }

        // 鑱斿悎/浜ゅ弶绫诲瀷锛堝吋瀹规棫浠ｇ爜锛?
        if (name === 'TSUnionOrIntersectionType') {
            return SlimeCstToAstUtils.createTSUnionOrIntersectionTypeAst(child)
        }

        throw new Error(`Unknown TSType child: ${name}`)
    }

}

export const SlimeTSTypeAnnotationCstToAst = new SlimeTSTypeAnnotationCstToAstSingle()
