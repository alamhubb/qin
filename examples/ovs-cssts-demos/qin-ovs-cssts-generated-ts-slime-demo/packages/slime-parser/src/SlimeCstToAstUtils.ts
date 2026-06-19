/**
 * SlimeCstToAstUtil - CST 鍒?AST 杞崲鍒嗗彂鍣?
 *
 * 鈿狅笍 閲嶈瑙勫垯锛氭湰鏂囦欢鍙仛鍒嗗彂锛屼笉鍐欎换浣曡浆鎹㈤€昏緫锛?
 *
 * ## 鑱岃矗
 * - 浣滀负 CST鈫扐ST 杞崲鐨勭粺涓€鍏ュ彛鍜屽崗璋冧腑蹇?
 * - 灏嗚浆鎹㈣姹傚垎鍙戝埌 cstToAst/ 鐩綍涓嬬殑鍏蜂綋瀹炵幇绫?
 * - 鎻愪緵缂撳瓨鏈哄埗锛坋xpressionAstCache锛?
 * - 閬垮厤鍚勮浆鎹㈠櫒涔嬮棿鐨勫惊鐜緷璧?
 *
 * ## 鏋舵瀯璇存槑
 * 璇︾粏鏋舵瀯璁捐璇峰弬闃? ./cstToAst/README.md
 *
 * ```
 * SlimeCstToAstUtil (鏈枃浠?- 鍒嗗彂鍣?
 *     鈫?
 * 鍚勯鍩熻浆鎹㈠櫒 (expressions/, statements/, typescript/, ...)
 *     鈫?
 * SlimeAstCreateUtils (宸ュ巶鏂规硶)
 * ```
 *
 * ## 姝ｇ‘鍋氭硶
 * ```typescript
 * createSomeAst(cst: SubhutiCst): any {
 *     return SomeConverter.createSomeAst(cst)  // 绾垎鍙?
 * }
 * ```
 *
 * ## 閿欒鍋氭硶
 * ```typescript
 * // 鉂?涓嶈鍦ㄨ繖閲屽啓閫昏緫
 * createSomeAst(cst: SubhutiCst): any {
 *     const children = cst.getChildren() || []
 *     // ... 杞崲閫昏緫搴旇鍦?cstToAst/ 鐩綍涓?
 *     return { ... }
 * }
 * ```
 *
 * @see ./cstToAst/README.md 瀹屾暣鏋舵瀯鏂囨。
 */

import {
    type SlimeAssignmentExpression,
    SlimeBlockStatement,
    SlimeCallExpression,
    SlimeClassBody,
    SlimeClassDeclaration,
    SlimeConditionalExpression,
    SlimeDeclaration,
    SlimeExportDefaultDeclaration,
    SlimeExportNamedDeclaration,
    SlimeExpression,
    SlimeExpressionStatement,
    SlimeFunctionExpression,
    SlimeIdentifier,
    SlimeLiteral,
    SlimeModuleDeclaration,
    SlimePattern,
    SlimeProgram,
    SlimeStatement,
    SlimeStringLiteral,
    SlimeVariableDeclaration,
    SlimeVariableDeclarator,
    SlimeReturnStatement,
    SlimeSpreadElement,
    SlimeMethodDefinition,
    SlimeRestElement,
    SlimeMemberExpression,
    SlimeImportDeclaration,
    SlimeImportSpecifier,
    SlimeClassExpression,
    SlimeArrayPattern,
    SlimeObjectPattern,
    SlimeAssignmentProperty,
    // Wrapper types for comma token association
    type SlimeArrayElement,
    SlimeObjectPropertyItem,
    SlimeFunctionParam,
    SlimeCallArgument,
    SlimeArrayPatternElement,
    SlimeObjectPatternProperty,
    SlimeImportSpecifierItem,
    SlimeExportSpecifierItem,
    SlimeFunctionDeclaration,
    SlimeImportDefaultSpecifier,
    SlimeImportNamespaceSpecifier,
    // Additional needed types
    type SlimeObjectExpression,
    SlimeProperty,
    SlimeNumericLiteral,
    SlimeArrayExpression,
    SlimeArrowFunctionExpression,
    SlimeDotToken,
    SlimeAssignToken,
    SlimeLBracketToken,
    SlimeRBracketToken,
    SlimeCommaToken,
    SlimeLBraceToken,
    SlimeRBraceToken,
    SlimeSuper,
    SlimeThisExpression,
    SlimePropertyDefinition,
    SlimeMaybeNamedFunctionDeclaration,
    SlimeMaybeNamedClassDeclaration,
    SlimeExportAllDeclaration,
    SlimeExportSpecifier,
} from "slime-ast";
import { SubhutiCst, type SubhutiSourceLocation } from "subhuti";
import { com_slime_parser_SlimeParser as SlimeParser } from "../com/slime/parser/SlimeParser.ts";
import { SlimeTokenCreateUtils, SlimeAstTypeName } from "slime-ast";
import {
    SlimeArrowFunctionCstToAst,
    SlimeAssignmentPatternCstToAst,
    SlimeBinaryExpressionCstToAst,
    SlimeBindingPatternCstToAst,
    SlimeBlockCstToAst,
    SlimeCompoundLiteralCstToAst,
    SlimeControlFlowCstToAst,
    SlimeLoopCstToAst,
    SlimeExpressionCstToAst,
    SlimeExportCstToAst,
    SlimeFunctionBodyCstToAst,
    SlimeFunctionDeclarationCstToAst,
    SlimeFunctionExpressionCstToAst,
    SlimeFunctionParameterCstToAst,
    SlimeIdentifierCstToAst,
    SlimeImportCstToAst,
    SlimeLiteralCstToAst,
    SlimeMemberCallCstToAst,
    SlimeMethodDefinitionCstToAst,
    SlimeAccessorCstToAst,
    SlimeModuleCstToAst,
    SlimeOptionalExpressionCstToAst,
    SlimeOtherStatementCstToAst,
    SlimePatternConvertCstToAst,
    SlimePrimaryExpressionCstToAst,
    SlimeUnaryExpressionCstToAst,
    SlimeVariableCstToAst,
    SlimeClassDeclarationCstToAst,
} from "./cstToAst";

// 鍑芥暟璋冪敤琛ㄨ揪寮忚浆鎹?
import { SlimeCallExpressionCstToAst } from "./cstToAst/expressions/SlimeCallExpressionCstToAst.ts";

// TypeScript 绫诲瀷杞崲
import { SlimeTSTypeAnnotationCstToAst } from "./cstToAst/typescript/SlimeTSTypeAnnotationCstToAst.ts";
import { SlimeTSCompositeTypeCstToAst } from "./cstToAst/typescript/SlimeTSCompositeTypeCstToAst.ts";
import { SlimeTSFunctionTypeCstToAst } from "./cstToAst/typescript/SlimeTSFunctionTypeCstToAst.ts";
import { SlimeTSKeywordTypeCstToAst } from "./cstToAst/typescript/SlimeTSKeywordTypeCstToAst.ts";
import { SlimeTSPrimaryTypeCstToAst } from "./cstToAst/typescript/SlimeTSPrimaryTypeCstToAst.ts";
import { SlimeTSTypeLiteralCstToAst } from "./cstToAst/typescript/SlimeTSTypeLiteralCstToAst.ts";
import { SlimeTSDeclarationCstToAst } from "./cstToAst/typescript/SlimeTSDeclarationCstToAst.ts";
import { SlimeTSExpressionCstToAst } from "./cstToAst/typescript/SlimeTSExpressionCstToAst.ts";

// 缁熶竴鍚庣紑鎿嶄綔澶勭悊鍣?
import { SlimePostfixExpressionCstToAst } from "./cstToAst/expressions/SlimePostfixExpressionCstToAst.ts";



// ============================================
// Unicode 杞箟搴忓垪瑙ｇ爜
// ES2025 瑙勮寖 12.9.4 - \uXXXX \u{XXXXX} 杞崲涓哄疄闄呭瓧绗?
// 鍙傝€冨疄鐜帮細Babel銆丄corn銆乀ypeScript
// ============================================

/**
 * CST 鍒?AST 杞崲鍣?
 *
 * ## 涓ゅ眰鏋舵瀯璁捐
 *
 * ### 绗竴灞傦細AST 宸ュ巶绫?(SlimeAstCreateUtils.ts / SlimeCreateUtils)
 * - 涓?ESTree AST 鑺傜偣绫诲瀷涓€涓€瀵瑰簲鐨勭函绮瑰垱寤烘柟娉?
 * - 涓嶄緷璧?CST 缁撴瀯锛屽彧鎺ユ敹鍙傛暟鍒涘缓鑺傜偣
 * - 绀轰緥锛歝reateIdentifier(name, loc) -> SlimeIdentifier
 *
 * ### 绗簩灞傦細CST 杞崲鍣?(鏈被)
 * - 涓?CST 瑙勫垯涓€涓€瀵瑰簲鐨勮浆鎹㈡柟娉?(createXxxAst)
 * - 瑙ｆ瀽 CST 缁撴瀯锛屾彁鍙栦俊鎭紝璋冪敤 AST 宸ュ巶绫?
 * - 涓績杞彂鏂规硶锛歝reateAstFromCst(cst) - 鑷姩鏍规嵁绫诲瀷鍒嗗彂
 *
 * ## 鏂规硶鍛藉悕瑙勮寖
 *
 * | 鏂规硶绫诲瀷 | 鍛藉悕妯″紡 | 璇存槑 |
 * |----------|----------|------|
 * | CST 瑙勫垯杞崲 | createXxxAst | 涓?@SubhutiRule 瑙勫垯涓€涓€瀵瑰簲 |
 * | AST 绫诲瀷鏄犲皠 | createXxxAst | CST 瑙勫垯鍚嶄笌 AST 绫诲瀷鍚嶄笉涓€鑷存椂浣跨敤 |
 * | 鍐呴儴杈呭姪鏂规硶 | createXxxAst | ES2025 涓撶敤澶勭悊绛?|
 * | 宸ュ叿鏂规硶 | convertXxx / isXxx | 琛ㄨ揪寮忚浆妯″紡銆佹鏌ユ柟娉曠瓑 |
 *
 * ## 鏂规硶鍛藉悕瑙勮寖
 *
 * 鎵€鏈?CST 杞崲鏂规硶鍛藉悕涓?createXxxAst锛屽叾涓?Xxx 涓?CST 瑙勫垯鍚嶄竴鑷淬€?
 * 鍐呴儴璋冪敤 SlimeNodeCreate / SlimeCreateUtils 涓笌 AST 绫诲瀷鍚嶄竴鑷寸殑宸ュ巶鏂规硶銆?
 *
 * 渚嬪锛?
 * - createArrayLiteralAst (CST 瑙勫垯鍚? -> 鍐呴儴璋冪敤 createArrayExpression (AST 绫诲瀷鍚?
 * - createObjectLiteralAst (CST 瑙勫垯鍚? -> 鍐呴儴璋冪敤 createObjectExpression (AST 绫诲瀷鍚?
 * - createCatchAst (CST 瑙勫垯鍚? -> 鍐呴儴璋冪敤 createCatchClause (AST 绫诲瀷鍚?
 *
 * ## 鏍稿績鍒嗗彂鏂规硶
 * - createAstFromCst: 涓績杞彂锛屾牴鎹?CST 绫诲瀷鏄惧紡鍒嗗彂鍒板搴旀柟娉?
 * - createStatementDeclarationAst: 璇彞/澹版槑鍒嗗彂
 *
 * ## 杈呭姪澶勭悊鏂规硶
 * - toProgram: Program 鍏ュ彛澶勭悊
 */
export class SlimeCstToAst {

    constructor() {
        // 娉ㄥ唽褰撳墠瀹炰緥鍒板叏灞€
        // 鐢变簬 this 鍦ㄥ瓙绫昏皟鐢ㄦ椂鏄瓙绫诲疄渚嬶紝鎵€浠ヤ細鑷姩娉ㄥ唽姝ｇ‘鐨勫疄渚?
        registerSlimeCstToAstUtil(this)
    }

    /**
     * 灏?Unicode 杞箟搴忓垪瑙ｇ爜涓哄疄闄呭瓧绗?
     * 鏀寔 \uXXXX 鍜?\u{XXXXX} 鏍煎紡
     *
     * @param str 鍙兘鍖呭惈 Unicode 杞箟鐨勫瓧绗︿覆
     * @returns 瑙ｇ爜鍚庣殑瀛楃涓?
     */
    decodeUnicodeEscapes(str: string | undefined): string {
        // 濡傛灉涓虹┖鎴栦笉鍖呭惈杞箟搴忓垪锛岀洿鎺ヨ繑鍥烇紙鎬ц兘浼樺寲锟?
        if (!str || !str.includes('\\u')) {
            return str || ''
        }

        return str.replace(/\\u\{([0-9a-fA-F]+)\}|\\u([0-9a-fA-F]{4})/g,
            (match, braceCode, fourDigitCode) => {
                const codePoint = parseInt(braceCode || fourDigitCode, 16)
                return String.fromCodePoint(codePoint)
            }
        )
    }

    /**
     * 妫€鏌?CST 鑺傜偣鍚嶇О鏄惁鍖归厤
     */
    checkCstName(cst: SubhutiCst, cstName: string | undefined) {
        if (!cstName) {
            return cst.getName()
        }
        if (cst.getName() !== cstName) {
            throw new Error(cst.getName())
        }
        return cstName
    }

    private isValidSourceLocation(loc: any): boolean {
        if (!loc || typeof loc !== 'object') {
            return false
        }
        if (!loc.start || !loc.end) {
            return false
        }
        return typeof loc.start.index === 'number'
            && typeof loc.start.line === 'number'
            && typeof loc.start.column === 'number'
            && typeof loc.end.index === 'number'
            && typeof loc.end.line === 'number'
            && typeof loc.end.column === 'number'
    }

    resolveBestLoc(...candidates: Array<any>): any {
        for (const candidate of candidates) {
            if (this.isValidSourceLocation(candidate)) {
                return candidate
            }
        }
        return undefined
    }

    extractCstRaw(cst: SubhutiCst | undefined): string {
        if (!cst) {
            return ''
        }
        if (cst.getValue() !== undefined && cst.getValue() !== null) {
            return String(cst.getValue())
        }
        return (cst.getChildren() || []).map(child => this.extractCstRaw(child)).join('')
    }

    createIncompleteExpressionAst(cst: SubhutiCst | undefined, reason: string): SlimeExpression {
        const raw = this.extractCstRaw(cst)
        return {
            type: 'IncompleteExpression',
            raw,
            reason,
            loc: cst?.getLoc(),
        } as any
    }

    readonly expressionAstCache = new WeakMap<SubhutiCst, SlimeExpression>()

    // === identifier / IdentifierCstToAst ===

    createIdentifierNameAst(cst: SubhutiCst): SlimeIdentifier {
        return SlimeIdentifierCstToAst.createIdentifierNameAst(cst)
    }

    createBindingIdentifierAst(cst: SubhutiCst): SlimeIdentifier {
        return SlimeIdentifierCstToAst.createBindingIdentifierAst(cst)
    }

    createPrivateIdentifierAst(cst: SubhutiCst): SlimeIdentifier {
        return SlimeIdentifierCstToAst.createPrivateIdentifierAst(cst)
    }

    createLabelIdentifierAst(cst: SubhutiCst): SlimeIdentifier {
        return SlimeIdentifierCstToAst.createLabelIdentifierAst(cst)
    }

    createIdentifierReferenceAst(cst: SubhutiCst): SlimeIdentifier {
        return SlimeIdentifierCstToAst.createIdentifierReferenceAst(cst)
    }

    createIdentifierAst(cst: SubhutiCst): SlimeIdentifier {
        return SlimeIdentifierCstToAst.createIdentifierAst(cst)
    }

    // === literal / LiteralCstToAst ===

    createBooleanLiteralAst(cst: SubhutiCst): SlimeLiteral {
        return SlimeLiteralCstToAst.createBooleanLiteralAst(cst)
    }

    createNumericLiteralAst(cst: SubhutiCst): SlimeNumericLiteral {
        return SlimeLiteralCstToAst.createNumericLiteralAst(cst)
    }

    createStringLiteralAst(cst: SubhutiCst): SlimeStringLiteral {
        return SlimeLiteralCstToAst.createStringLiteralAst(cst)
    }

    createRegExpLiteralAst(cst: SubhutiCst): any {
        return SlimeLiteralCstToAst.createRegExpLiteralAst(cst)
    }

    createLiteralFromToken(token: any): SlimeExpression {
        return SlimeLiteralCstToAst.createLiteralFromToken(token)
    }

    createLiteralAst(cst: SubhutiCst): SlimeLiteral {
        return SlimeLiteralCstToAst.createLiteralAst(cst)
    }

    createElisionAst(cst: SubhutiCst): number {
        return SlimeLiteralCstToAst.createElisionAst(cst)
    }

    processTemplateMiddleList(cst: SubhutiCst, quasis: any[], expressions: SlimeExpression[]): void {
        return SlimeLiteralCstToAst.processTemplateMiddleList(cst, quasis, expressions)
    }

    processTemplateSpans(cst: SubhutiCst, quasis: any[], expressions: SlimeExpression[]): void {
        return SlimeLiteralCstToAst.processTemplateSpans(cst, quasis, expressions)
    }

    createTemplateLiteralAst(cst: SubhutiCst): SlimeExpression {
        return SlimeLiteralCstToAst.createTemplateLiteralAst(cst)
    }

    // === literal / CompoundLiteralCstToAst ===

    createPropertyNameAst(cst: SubhutiCst): SlimeIdentifier | SlimeLiteral | SlimeExpression {
        return SlimeCompoundLiteralCstToAst.createPropertyNameAst(cst)
    }

    createLiteralPropertyNameAst(cst: SubhutiCst): SlimeIdentifier | SlimeLiteral {
        return SlimeCompoundLiteralCstToAst.createLiteralPropertyNameAst(cst)
    }

    createSpreadElementAst(cst: SubhutiCst): SlimeSpreadElement {
        return SlimeCompoundLiteralCstToAst.createSpreadElementAst(cst)
    }

    createElementListAst(cst: SubhutiCst): Array<SlimeArrayElement> {
        return SlimeCompoundLiteralCstToAst.createElementListAst(cst)
    }

    createArrayLiteralAst(cst: SubhutiCst): SlimeArrayExpression {
        return SlimeCompoundLiteralCstToAst.createArrayLiteralAst(cst)
    }

    createObjectLiteralAst(cst: SubhutiCst): SlimeObjectExpression {
        return SlimeCompoundLiteralCstToAst.createObjectLiteralAst(cst)
    }

    createPropertyDefinitionAst(cst: SubhutiCst): SlimeProperty {
        return SlimeCompoundLiteralCstToAst.createPropertyDefinitionAst(cst)
    }

    // === pattern / BindingPatternCstToAst ===

    createBindingElementAst(cst: SubhutiCst): any {
        return SlimeBindingPatternCstToAst.createBindingElementAst(cst)
    }

    createSingleNameBindingAst(cst: SubhutiCst): any {
        return SlimeBindingPatternCstToAst.createSingleNameBindingAst(cst)
    }

    createBindingRestPropertyAst(cst: SubhutiCst): SlimeRestElement {
        return SlimeBindingPatternCstToAst.createBindingRestPropertyAst(cst)
    }

    createBindingPropertyAst(cst: SubhutiCst): any {
        return SlimeBindingPatternCstToAst.createBindingPropertyAst(cst)
    }

    createBindingPropertyListAst(cst: SubhutiCst): any[] {
        return SlimeBindingPatternCstToAst.createBindingPropertyListAst(cst)
    }

    createBindingElementListAst(cst: SubhutiCst): any[] {
        return SlimeBindingPatternCstToAst.createBindingElementListAst(cst)
    }

    createBindingElisionElementAst(cst: SubhutiCst): any {
        return SlimeBindingPatternCstToAst.createBindingElisionElementAst(cst)
    }

    createBindingPatternAst(cst: SubhutiCst): SlimePattern {
        return SlimeBindingPatternCstToAst.createBindingPatternAst(cst)
    }

    createArrayBindingPatternAst(cst: SubhutiCst): SlimeArrayPattern {
        return SlimeBindingPatternCstToAst.createArrayBindingPatternAst(cst)
    }

    createObjectBindingPatternAst(cst: SubhutiCst): SlimeObjectPattern {
        return SlimeBindingPatternCstToAst.createObjectBindingPatternAst(cst)
    }

    // === pattern / AssignmentPatternCstToAst ===

    createAssignmentPatternAst(cst: SubhutiCst): any {
        return SlimeAssignmentPatternCstToAst.createAssignmentPatternAst(cst)
    }

    createObjectAssignmentPatternAst(cst: SubhutiCst): SlimeObjectPattern {
        return SlimeAssignmentPatternCstToAst.createObjectAssignmentPatternAst(cst)
    }

    createArrayAssignmentPatternAst(cst: SubhutiCst): SlimeArrayPattern {
        return SlimeAssignmentPatternCstToAst.createArrayAssignmentPatternAst(cst)
    }

    createAssignmentPropertyListAst(cst: SubhutiCst): any[] {
        return SlimeAssignmentPatternCstToAst.createAssignmentPropertyListAst(cst)
    }

    createAssignmentPropertyAst(cst: SubhutiCst): any {
        return SlimeAssignmentPatternCstToAst.createAssignmentPropertyAst(cst)
    }

    createAssignmentElementListAst(cst: SubhutiCst): any[] {
        return SlimeAssignmentPatternCstToAst.createAssignmentElementListAst(cst)
    }

    createAssignmentElementAst(cst: SubhutiCst): any {
        return SlimeAssignmentPatternCstToAst.createAssignmentElementAst(cst)
    }

    createAssignmentElisionElementAst(cst: SubhutiCst): any {
        return SlimeAssignmentPatternCstToAst.createAssignmentElisionElementAst(cst)
    }

    createAssignmentRestElementAst(cst: SubhutiCst): any {
        return SlimeAssignmentPatternCstToAst.createAssignmentRestElementAst(cst)
    }

    createAssignmentRestPropertyAst(cst: SubhutiCst): any {
        return SlimeAssignmentPatternCstToAst.createAssignmentRestPropertyAst(cst)
    }

    // === pattern / PatternConvertCstToAst ===

    convertArrayExpressionToPattern(expr: any): SlimeArrayPattern {
        return SlimePatternConvertCstToAst.convertArrayExpressionToPattern(expr)
    }

    convertCstToPattern(cst: SubhutiCst): SlimePattern | null {
        return SlimePatternConvertCstToAst.convertCstToPattern(cst)
    }

    convertCoverParameterCstToPattern(cst: SubhutiCst, hasEllipsis: boolean): SlimePattern | null {
        return SlimePatternConvertCstToAst.convertCoverParameterCstToPattern(cst, hasEllipsis)
    }

    convertObjectLiteralToPattern(cst: SubhutiCst): SlimeObjectPattern {
        return SlimePatternConvertCstToAst.convertObjectLiteralToPattern(cst)
    }

    convertPropertyDefinitionToPatternProperty(cst: SubhutiCst): SlimeAssignmentProperty | null {
        return SlimePatternConvertCstToAst.convertPropertyDefinitionToPatternProperty(cst)
    }

    convertObjectExpressionToPattern(expr: any): SlimeObjectPattern {
        return SlimePatternConvertCstToAst.convertObjectExpressionToPattern(expr)
    }

    convertAssignmentExpressionToPattern(expr: any): any {
        return SlimePatternConvertCstToAst.convertAssignmentExpressionToPattern(expr)
    }

    convertExpressionToPatternFromAST(expr: any): SlimePattern | null {
        return SlimePatternConvertCstToAst.convertExpressionToPatternFromAST(expr)
    }

    convertArrayLiteralToPattern(cst: SubhutiCst): SlimeArrayPattern {
        return SlimePatternConvertCstToAst.convertArrayLiteralToPattern(cst)
    }

    convertExpressionToPattern(expr: any): SlimePattern {
        return SlimePatternConvertCstToAst.convertExpressionToPattern(expr)
    }

    // === expression / ExpressionCstToAst ===

    createYieldExpressionAst(cst: SubhutiCst): any {
        return SlimeUnaryExpressionCstToAst.createYieldExpressionAst(cst)
    }

    createAwaitExpressionAst(cst: SubhutiCst): any {
        return SlimeUnaryExpressionCstToAst.createAwaitExpressionAst(cst)
    }

    createConditionalExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeExpressionCstToAst.createConditionalExpressionAst(cst)
    }

    // === expression / PrimaryExpressionCstToAst ===

    createComputedPropertyNameAst(cst: SubhutiCst): SlimeExpression {
        return SlimeCompoundLiteralCstToAst.createComputedPropertyNameAst(cst)
    }

    createPrimaryExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimePrimaryExpressionCstToAst.createPrimaryExpressionAst(cst)
    }

    createParenthesizedExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimePrimaryExpressionCstToAst.createParenthesizedExpressionAst(cst)
    }

    createCoverParenthesizedExpressionAndArrowParameterListAst(cst: SubhutiCst): SlimeExpression {
        return SlimePrimaryExpressionCstToAst.createCoverParenthesizedExpressionAndArrowParameterListAst(cst)
    }

    createCoverInitializedNameAst(cst: SubhutiCst): any {
        return SlimeCompoundLiteralCstToAst.createCoverInitializedNameAst(cst)
    }

    createCoverCallExpressionAndAsyncArrowHeadAst(cst: SubhutiCst): SlimeExpression {
        return SlimeMemberCallCstToAst.createCoverCallExpressionAndAsyncArrowHeadAst(cst)
    }

    createLeftHandSideExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeMemberCallCstToAst.createLeftHandSideExpressionAst(cst)
    }

    // === expression / AssignmentExpressionCstToAst ===

    createExpressionBodyAst(cst: SubhutiCst): SlimeExpression {
        return SlimeMemberCallCstToAst.createExpressionBodyAst(cst)
    }

    createAssignmentExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeExpressionCstToAst.createAssignmentExpressionAst(cst)
    }

    createExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeExpressionCstToAst.createExpressionAst(cst)
    }

    createExpressionAstUncached(cst: SubhutiCst): SlimeExpression {
        return SlimeExpressionCstToAst.createExpressionAstUncached(cst)
    }

    // === expression / BinaryExpressionCstToAst ===

    createMultiplicativeOperatorAst(cst: SubhutiCst): string {
        return SlimeBinaryExpressionCstToAst.createMultiplicativeOperatorAst(cst)
    }

    createAssignmentOperatorAst(cst: SubhutiCst): string {
        return SlimeExpressionCstToAst.createAssignmentOperatorAst(cst)
    }

    createExponentiationExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeBinaryExpressionCstToAst.createExponentiationExpressionAst(cst)
    }

    createLogicalORExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeBinaryExpressionCstToAst.createLogicalORExpressionAst(cst)
    }

    createLogicalANDExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeBinaryExpressionCstToAst.createLogicalANDExpressionAst(cst)
    }

    createBitwiseORExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeBinaryExpressionCstToAst.createBitwiseORExpressionAst(cst)
    }

    createBitwiseXORExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeBinaryExpressionCstToAst.createBitwiseXORExpressionAst(cst)
    }

    createBitwiseANDExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeBinaryExpressionCstToAst.createBitwiseANDExpressionAst(cst)
    }

    createEqualityExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeBinaryExpressionCstToAst.createEqualityExpressionAst(cst)
    }

    createRelationalExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeBinaryExpressionCstToAst.createRelationalExpressionAst(cst)
    }

    createShiftExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeBinaryExpressionCstToAst.createShiftExpressionAst(cst)
    }

    createCoalesceExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeExpressionCstToAst.createCoalesceExpressionAst(cst)
    }

    createCoalesceExpressionHeadAst(cst: SubhutiCst): SlimeExpression {
        return SlimeExpressionCstToAst.createCoalesceExpressionHeadAst(cst)
    }

    createShortCircuitExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeExpressionCstToAst.createShortCircuitExpressionAst(cst)
    }

    createShortCircuitExpressionTailAst(left: SlimeExpression, tailCst: SubhutiCst): SlimeExpression {
        return SlimeExpressionCstToAst.createShortCircuitExpressionTailAst(left, tailCst)
    }

    // === expression / UnaryExpressionCstToAst ===

    createUnaryExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeUnaryExpressionCstToAst.createUnaryExpressionAst(cst)
    }

    createUpdateExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeUnaryExpressionCstToAst.createUpdateExpressionAst(cst)
    }

    createAdditiveExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeBinaryExpressionCstToAst.createAdditiveExpressionAst(cst)
    }

    createMultiplicativeExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeBinaryExpressionCstToAst.createMultiplicativeExpressionAst(cst)
    }

    // === expression / MemberCallCstToAst ===

    createMemberExpressionFirstOr(cst: SubhutiCst): SlimeExpression | SlimeSuper {
        return SlimeMemberCallCstToAst.createMemberExpressionFirstOr(cst)
    }

    createMemberExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeMemberCallCstToAst.createMemberExpressionAst(cst)
    }

    createCallExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeCallExpressionCstToAst.createCallExpressionAst(cst)
    }


    createNewExpressionAst(cst: SubhutiCst): any {
        return SlimeCallExpressionCstToAst.createNewExpressionAst(cst)
    }

    createSuperCallAst(cst: SubhutiCst): SlimeExpression {
        return SlimeCallExpressionCstToAst.createSuperCallAst(cst)
    }

    createSuperPropertyAst(cst: SubhutiCst): SlimeExpression {
        return SlimeMemberCallCstToAst.createSuperPropertyAst(cst)
    }

    createMetaPropertyAst(cst: SubhutiCst): SlimeExpression {
        return SlimeMemberCallCstToAst.createMetaPropertyAst(cst)
    }

    // === expression / OptionalExpressionCstToAst ===

    createOptionalChainAst(object: SlimeExpression, chainCst: SubhutiCst): SlimeExpression {
        return SlimeOptionalExpressionCstToAst.createOptionalChainAst(object, chainCst)
    }

    createOptionalExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeOptionalExpressionCstToAst.createOptionalExpressionAst(cst)
    }

    // === function / ArrowFunctionCstToAst ===

    createAsyncConciseBodyAst(cst: SubhutiCst): SlimeBlockStatement | SlimeExpression {
        return SlimeFunctionBodyCstToAst.createAsyncConciseBodyAst(cst)
    }

    createAsyncArrowHeadAst(cst: SubhutiCst): any {
        return SlimeArrowFunctionCstToAst.createAsyncArrowHeadAst(cst)
    }

    createAsyncArrowBindingIdentifierAst(cst: SubhutiCst): SlimeIdentifier {
        return SlimeArrowFunctionCstToAst.createAsyncArrowBindingIdentifierAst(cst)
    }

    findFirstIdentifierInExpression(cst: SubhutiCst): SubhutiCst | null {
        return SlimePrimaryExpressionCstToAst.findFirstIdentifierInExpression(cst)
    }

    extractParametersFromExpression(expressionCst: SubhutiCst): SlimePattern[] {
        return SlimeFunctionParameterCstToAst.extractParametersFromExpression(expressionCst)
    }

    createArrowParametersFromCoverGrammar(cst: SubhutiCst): SlimePattern[] {
        return SlimeArrowFunctionCstToAst.createArrowParametersFromCoverGrammar(cst)
    }

    createArrowFormalParametersAst(cst: SubhutiCst): SlimePattern[] {
        return SlimeArrowFunctionCstToAst.createArrowFormalParametersAst(cst)
    }

    createArrowFormalParametersAstWrapped(cst: SubhutiCst): SlimeFunctionParam[] {
        return SlimeArrowFunctionCstToAst.createArrowFormalParametersAstWrapped(cst)
    }

    createArrowParametersAst(cst: SubhutiCst): SlimePattern[] {
        return SlimeArrowFunctionCstToAst.createArrowParametersAst(cst)
    }

    createArrowFunctionAst(cst: SubhutiCst): SlimeArrowFunctionExpression {
        return SlimeArrowFunctionCstToAst.createArrowFunctionAst(cst)
    }

    createAsyncArrowFunctionAst(cst: SubhutiCst): SlimeArrowFunctionExpression {
        return SlimeArrowFunctionCstToAst.createAsyncArrowFunctionAst(cst)
    }

    createAsyncArrowParamsFromCover(cst: SubhutiCst): SlimePattern[] {
        return SlimeArrowFunctionCstToAst.createAsyncArrowParamsFromCover(cst)
    }

    createConciseBodyAst(cst: SubhutiCst): SlimeBlockStatement | SlimeExpression {
        return SlimeFunctionBodyCstToAst.createConciseBodyAst(cst)
    }

    // === function / FunctionExpressionCstToAst ===

    createFunctionExpressionAst(cst: SubhutiCst): SlimeFunctionExpression {
        return SlimeFunctionExpressionCstToAst.createFunctionExpressionAst(cst)
    }

    createGeneratorExpressionAst(cst: SubhutiCst): SlimeFunctionExpression {
        return SlimeFunctionExpressionCstToAst.createGeneratorExpressionAst(cst)
    }

    createAsyncFunctionExpressionAst(cst: SubhutiCst): SlimeFunctionExpression {
        return SlimeFunctionExpressionCstToAst.createAsyncFunctionExpressionAst(cst)
    }

    createAsyncGeneratorExpressionAst(cst: SubhutiCst): SlimeFunctionExpression {
        return SlimeFunctionExpressionCstToAst.createAsyncGeneratorExpressionAst(cst)
    }

    // === function / FunctionParameterCstToAst ===

    createBindingRestElementAst(cst: SubhutiCst): SlimeRestElement {
        return SlimePatternConvertCstToAst.createBindingRestElementAst(cst)
    }

    createFunctionRestParameterAst(cst: SubhutiCst): SlimeRestElement {
        return SlimeFunctionParameterCstToAst.createFunctionRestParameterAst(cst)
    }

    /**
     * [TypeScript] 鍒涘缓 TSThisParameter AST
     */
    createTSThisParameterAst(cst: SubhutiCst): SlimeIdentifier {
        return SlimeFunctionParameterCstToAst.createTSThisParameterAst(cst)
    }



    createFormalParameterAst(cst: SubhutiCst): SlimePattern {
        return SlimeFunctionParameterCstToAst.createFormalParameterAst(cst)
    }

    createFormalParameterListAst(cst: SubhutiCst): SlimePattern[] {
        return SlimeFunctionParameterCstToAst.createFormalParameterListAst(cst)
    }

    createFormalParameterListAstWrapped(cst: SubhutiCst): SlimeFunctionParam[] {
        return SlimeFunctionParameterCstToAst.createFormalParameterListAstWrapped(cst)
    }

    createFormalParametersAst(cst: SubhutiCst): SlimePattern[] {
        return SlimeFunctionParameterCstToAst.createFormalParametersAst(cst)
    }

    createFormalParametersAstWrapped(cst: SubhutiCst): SlimeFunctionParam[] {
        return SlimeFunctionParameterCstToAst.createFormalParametersAstWrapped(cst)
    }

    createFormalParameterListFromEs2025Wrapped(cst: SubhutiCst): SlimeFunctionParam[] {
        return SlimeFunctionParameterCstToAst.createFormalParameterListFromEs2025Wrapped(cst)
    }

    createUniqueFormalParametersAst(cst: SubhutiCst): SlimePattern[] {
        return SlimeFunctionParameterCstToAst.createUniqueFormalParametersAst(cst)
    }

    createUniqueFormalParametersAstWrapped(cst: SubhutiCst): SlimeFunctionParam[] {
        return SlimeFunctionParameterCstToAst.createUniqueFormalParametersAstWrapped(cst)
    }

    // === declaration / FunctionDeclarationCstToAst ===

    createFunctionDeclarationAst(cst: SubhutiCst): SlimeFunctionDeclaration {
        return SlimeFunctionDeclarationCstToAst.createFunctionDeclarationAst(cst)
    }

    createGeneratorDeclarationAst(cst: SubhutiCst): SlimeFunctionDeclaration {
        return SlimeFunctionDeclarationCstToAst.createGeneratorDeclarationAst(cst)
    }

    createAsyncFunctionDeclarationAst(cst: SubhutiCst): SlimeFunctionDeclaration {
        return SlimeFunctionDeclarationCstToAst.createAsyncFunctionDeclarationAst(cst)
    }

    createAsyncGeneratorDeclarationAst(cst: SubhutiCst): SlimeFunctionDeclaration {
        return SlimeFunctionDeclarationCstToAst.createAsyncGeneratorDeclarationAst(cst)
    }

    // === declaration / VariableCstToAst ===

    createLetOrConstAst(cst: SubhutiCst): string {
        return SlimeVariableCstToAst.createLetOrConstAst(cst)
    }

    createVariableDeclarationFromList(cst: SubhutiCst, kind: string): SlimeVariableDeclaration {
        return SlimeVariableCstToAst.createVariableDeclarationFromList(cst, kind)
    }

    createForBindingAst(cst: SubhutiCst): any {
        return SlimeLoopCstToAst.createForBindingAst(cst)
    }

    createForDeclarationAst(cst: SubhutiCst): any {
        return SlimeLoopCstToAst.createForDeclarationAst(cst)
    }

    createInitializerAst(cst: SubhutiCst): SlimeExpression {
        return SlimeVariableCstToAst.createInitializerAst(cst)
    }

    createVariableDeclaratorAst(cst: SubhutiCst): SlimeVariableDeclarator {
        return SlimeVariableCstToAst.createVariableDeclaratorAst(cst)
    }

    createVariableDeclaratorFromVarDeclaration(cst: SubhutiCst): SlimeVariableDeclarator {
        return SlimeVariableCstToAst.createVariableDeclaratorFromVarDeclaration(cst)
    }

    createVariableDeclarationListAst(cst: SubhutiCst): SlimeVariableDeclarator[] {
        return SlimeVariableCstToAst.createVariableDeclarationListAst(cst)
    }

    createLexicalBindingAst(cst: SubhutiCst): SlimeVariableDeclarator {
        return SlimeVariableCstToAst.createLexicalBindingAst(cst)
    }

    createLexicalDeclarationAst(cst: SubhutiCst): SlimeVariableDeclaration {
        return SlimeVariableCstToAst.createLexicalDeclarationAst(cst)
    }

    createVariableDeclarationAst(cst: SubhutiCst): SlimeVariableDeclaration {
        return SlimeVariableCstToAst.createVariableDeclarationAst(cst)
    }

    createVariableStatementAst(cst: SubhutiCst): SlimeVariableDeclaration {
        return SlimeVariableCstToAst.createVariableStatementAst(cst)
    }

    createDeclarationAst(cst: SubhutiCst): SlimeDeclaration {
        return SlimeVariableCstToAst.createDeclarationAst(cst)
    }

    createHoistableDeclarationAst(cst: SubhutiCst): SlimeDeclaration {
        return SlimeVariableCstToAst.createHoistableDeclarationAst(cst)
    }

    // === class / ClassDeclarationCstToAst ===

    createClassElementNameAst(cst: SubhutiCst): SlimeIdentifier | SlimeLiteral | SlimeExpression {
        return SlimeClassDeclarationCstToAst.createClassElementNameAst(cst)
    }

    isComputedPropertyName(cst: SubhutiCst): boolean {
        return SlimeClassDeclarationCstToAst.isComputedPropertyName(cst)
    }

    isStaticModifier(cst: SubhutiCst | null): boolean {
        return SlimeClassDeclarationCstToAst.isStaticModifier(cst)
    }

    createClassDeclarationAst(cst: SubhutiCst): SlimeClassDeclaration {
        return SlimeClassDeclarationCstToAst.createClassDeclarationAst(cst)
    }

    createClassTailAst(cst: SubhutiCst): {
        superClass: SlimeExpression | null;
        body: SlimeClassBody;
        extendsToken?: any;
        lBraceToken?: any;
        rBraceToken?: any;
    } {
        return SlimeClassDeclarationCstToAst.createClassTailAst(cst)
    }

    createClassHeritageAst(cst: SubhutiCst): SlimeExpression {
        return SlimeClassDeclarationCstToAst.createClassHeritageAst(cst)
    }

    createClassHeritageAstWithToken(cst: SubhutiCst): { superClass: SlimeExpression; extendsToken?: any } {
        return SlimeClassDeclarationCstToAst.createClassHeritageAstWithToken(cst)
    }

    createFieldDefinitionAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimePropertyDefinition {
        return SlimeClassDeclarationCstToAst.createFieldDefinitionAst(staticCst, cst)
    }

    createClassBodyAst(cst: SubhutiCst): SlimeClassBody {
        return SlimeClassDeclarationCstToAst.createClassBodyAst(cst)
    }

    createClassStaticBlockAst(cst: SubhutiCst): any {
        return SlimeClassDeclarationCstToAst.createClassStaticBlockAst(cst)
    }

    createClassElementAst(cst: SubhutiCst): any {
        return SlimeClassDeclarationCstToAst.createClassElementAst(cst)
    }

    createClassElementListAst(cst: SubhutiCst): any[] {
        return SlimeClassDeclarationCstToAst.createClassElementListAst(cst)
    }

    createClassStaticBlockBodyAst(cst: SubhutiCst): Array<SlimeStatement> {
        return SlimeClassDeclarationCstToAst.createClassStaticBlockBodyAst(cst)
    }

    createClassStaticBlockStatementListAst(cst: SubhutiCst): Array<SlimeStatement> {
        return SlimeClassDeclarationCstToAst.createClassStaticBlockStatementListAst(cst)
    }

    createClassExpressionAst(cst: SubhutiCst): SlimeClassExpression {
        return SlimeClassDeclarationCstToAst.createClassExpressionAst(cst)
    }

    // === class / MethodDefinitionCstToAst ===

    createPropertySetParameterListAst(cst: SubhutiCst): SlimePattern[] {
        return SlimeMethodDefinitionCstToAst.createPropertySetParameterListAst(cst)
    }

    createPropertySetParameterListAstWrapped(cst: SubhutiCst): SlimeFunctionParam[] {
        return SlimeMethodDefinitionCstToAst.createPropertySetParameterListAstWrapped(cst)
    }

    createMethodDefinitionAstInternal(cst: SubhutiCst, kind: 'method' | 'get' | 'set', generator: boolean, async: boolean): SlimeMethodDefinition {
        return SlimeMethodDefinitionCstToAst.createMethodDefinitionAstInternal(cst, kind, generator, async)
    }

    createGeneratorMethodAst(cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeMethodDefinitionCstToAst.createGeneratorMethodAst(cst)
    }

    createAsyncMethodAst(cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeMethodDefinitionCstToAst.createAsyncMethodAst(cst)
    }

    createAsyncGeneratorMethodAst(cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeMethodDefinitionCstToAst.createAsyncGeneratorMethodAst(cst)
    }

    createMethodDefinitionAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeMethodDefinitionCstToAst.createMethodDefinitionAst(staticCst, cst)
    }

    createMethodDefinitionMethodDefinitionFromIdentifier(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeMethodDefinitionCstToAst.createMethodDefinitionMethodDefinitionFromIdentifier(staticCst, cst)
    }

    createMethodDefinitionClassElementNameAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeMethodDefinitionCstToAst.createMethodDefinitionClassElementNameAst(staticCst, cst)
    }

    createMethodDefinitionGetterMethodAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeAccessorCstToAst.createMethodDefinitionGetterMethodAst(staticCst, cst)
    }

    createMethodDefinitionSetterMethodAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeAccessorCstToAst.createMethodDefinitionSetterMethodAst(staticCst, cst)
    }

    createMethodDefinitionGetterMethodFromIdentifier(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeMethodDefinitionCstToAst.createMethodDefinitionGetterMethodFromIdentifier(staticCst, cst)
    }

    createMethodDefinitionSetterMethodFromIdentifier(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeMethodDefinitionCstToAst.createMethodDefinitionSetterMethodFromIdentifier(staticCst, cst)
    }

    createMethodDefinitionGeneratorMethodAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeAccessorCstToAst.createMethodDefinitionGeneratorMethodAst(staticCst, cst)
    }

    createMethodDefinitionGeneratorMethodFromChildren(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeMethodDefinitionCstToAst.createMethodDefinitionGeneratorMethodFromChildren(staticCst, cst)
    }

    createMethodDefinitionAsyncMethodAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeAccessorCstToAst.createMethodDefinitionAsyncMethodAst(staticCst, cst)
    }

    createMethodDefinitionAsyncMethodFromChildren(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeMethodDefinitionCstToAst.createMethodDefinitionAsyncMethodFromChildren(staticCst, cst)
    }

    createMethodDefinitionAsyncGeneratorMethodAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeAccessorCstToAst.createMethodDefinitionAsyncGeneratorMethodAst(staticCst, cst)
    }

    // === statement / BlockCstToAst ===

    createBlockAst(cst: SubhutiCst): SlimeBlockStatement {
        return SlimeBlockCstToAst.createBlockAst(cst)
    }

    createBlockStatementAst(cst: SubhutiCst): SlimeBlockStatement {
        return SlimeBlockCstToAst.createBlockStatementAst(cst)
    }

    createStatementDeclarationAst(cst: SubhutiCst): any {
        return SlimeBlockCstToAst.createStatementDeclarationAst(cst)
    }

    createStatementAst(cst: SubhutiCst): Array<SlimeStatement> {
        return SlimeBlockCstToAst.createStatementAst(cst)
    }

    createStatementListItemAst(cst: SubhutiCst): Array<SlimeStatement> {
        return SlimeBlockCstToAst.createStatementListItemAst(cst)
    }

    createStatementListAst(cst: SubhutiCst): Array<SlimeStatement> {
        return SlimeBlockCstToAst.createStatementListAst(cst)
    }

    // === statement / ControlFlowCstToAst ===

    createBreakableStatementAst(cst: SubhutiCst): any {
        return SlimeControlFlowCstToAst.createBreakableStatementAst(cst)
    }

    createIterationStatementAst(cst: SubhutiCst): any {
        return SlimeControlFlowCstToAst.createIterationStatementAst(cst)
    }

    createIfStatementAst(cst: SubhutiCst): any {
        return SlimeControlFlowCstToAst.createIfStatementAst(cst)
    }

    createIfStatementBodyAst(cst: SubhutiCst): any {
        return SlimeControlFlowCstToAst.createIfStatementBodyAst(cst)
    }

    createForStatementAst(cst: SubhutiCst): any {
        return SlimeLoopCstToAst.createForStatementAst(cst)
    }

    createForInOfStatementAst(cst: SubhutiCst): any {
        return SlimeLoopCstToAst.createForInOfStatementAst(cst)
    }

    createWhileStatementAst(cst: SubhutiCst): any {
        return SlimeLoopCstToAst.createWhileStatementAst(cst)
    }

    createDoWhileStatementAst(cst: SubhutiCst): any {
        return SlimeLoopCstToAst.createDoWhileStatementAst(cst)
    }

    createSwitchStatementAst(cst: SubhutiCst): any {
        return SlimeControlFlowCstToAst.createSwitchStatementAst(cst)
    }

    // === statement / FunctionBodyCstToAst ===

    createFunctionStatementListAst(cst: SubhutiCst): Array<SlimeStatement> {
        return SlimeFunctionBodyCstToAst.createFunctionStatementListAst(cst)
    }

    createFunctionBodyAst(cst: SubhutiCst): Array<SlimeStatement> {
        return SlimeFunctionBodyCstToAst.createFunctionBodyAst(cst)
    }

    createGeneratorBodyAst(cst: SubhutiCst): Array<SlimeStatement> {
        return SlimeFunctionBodyCstToAst.createGeneratorBodyAst(cst)
    }

    createAsyncFunctionBodyAst(cst: SubhutiCst): Array<SlimeStatement> {
        return SlimeFunctionBodyCstToAst.createAsyncFunctionBodyAst(cst)
    }

    createAsyncGeneratorBodyAst(cst: SubhutiCst): Array<SlimeStatement> {
        return SlimeFunctionBodyCstToAst.createAsyncGeneratorBodyAst(cst)
    }

    // === statement / OtherStatementCstToAst ===

    createSemicolonASIAst(cst: SubhutiCst): any {
        return SlimeOtherStatementCstToAst.createSemicolonASIAst(cst)
    }

    createEmptyStatementAst(cst: SubhutiCst): any {
        return SlimeOtherStatementCstToAst.createEmptyStatementAst(cst)
    }

    createThrowStatementAst(cst: SubhutiCst): any {
        return SlimeOtherStatementCstToAst.createThrowStatementAst(cst)
    }

    createBreakStatementAst(cst: SubhutiCst): any {
        return SlimeOtherStatementCstToAst.createBreakStatementAst(cst)
    }

    createContinueStatementAst(cst: SubhutiCst): any {
        return SlimeOtherStatementCstToAst.createContinueStatementAst(cst)
    }

    createTryStatementAst(cst: SubhutiCst): any {
        return SlimeOtherStatementCstToAst.createTryStatementAst(cst)
    }

    createFinallyAst(cst: SubhutiCst): any {
        return SlimeOtherStatementCstToAst.createFinallyAst(cst)
    }

    createCatchAst(cst: SubhutiCst): any {
        return SlimeOtherStatementCstToAst.createCatchAst(cst)
    }

    createCatchParameterAst(cst: SubhutiCst): any {
        return SlimeOtherStatementCstToAst.createCatchParameterAst(cst)
    }

    createReturnStatementAst(cst: SubhutiCst): SlimeReturnStatement {
        return SlimeOtherStatementCstToAst.createReturnStatementAst(cst)
    }

    createExpressionStatementAst(cst: SubhutiCst): SlimeExpressionStatement {
        return SlimeOtherStatementCstToAst.createExpressionStatementAst(cst)
    }

    createLabelledStatementAst(cst: SubhutiCst): any {
        return SlimeOtherStatementCstToAst.createLabelledStatementAst(cst)
    }

    createWithStatementAst(cst: SubhutiCst): any {
        return SlimeOtherStatementCstToAst.createWithStatementAst(cst)
    }

    createDebuggerStatementAst(cst: SubhutiCst): any {
        return SlimeOtherStatementCstToAst.createDebuggerStatementAst(cst)
    }

    createLabelledItemAst(cst: SubhutiCst): any {
        return SlimeOtherStatementCstToAst.createLabelledItemAst(cst)
    }

    // === statement / SwitchCstToAst ===

    createCaseClauseAst(cst: SubhutiCst): any {
        return SlimeControlFlowCstToAst.createCaseClauseAst(cst)
    }

    createDefaultClauseAst(cst: SubhutiCst): any {
        return SlimeControlFlowCstToAst.createDefaultClauseAst(cst)
    }

    createCaseClausesAst(cst: SubhutiCst): any[] {
        return SlimeControlFlowCstToAst.createCaseClausesAst(cst)
    }

    createCaseBlockAst(cst: SubhutiCst): any[] {
        return SlimeControlFlowCstToAst.createCaseBlockAst(cst)
    }

    createSwitchCaseAst(cst: SubhutiCst): any {
        return SlimeControlFlowCstToAst.createSwitchCaseAst(cst)
    }

    extractCasesFromCaseBlock(caseBlockCst: SubhutiCst): any[] {
        return SlimeControlFlowCstToAst.extractCasesFromCaseBlock(caseBlockCst)
    }

    // === module / ExportCstToAst ===

    createExportFromClauseAst(cst: SubhutiCst): any {
        return SlimeExportCstToAst.createExportFromClauseAst(cst)
    }

    createExportDeclarationAst(cst: SubhutiCst): SlimeExportDefaultDeclaration | SlimeExportNamedDeclaration | SlimeExportAllDeclaration {
        return SlimeExportCstToAst.createExportDeclarationAst(cst)
    }

    createNamedExportsAst(cst: SubhutiCst): SlimeExportSpecifierItem[] {
        return SlimeExportCstToAst.createNamedExportsAst(cst)
    }

    createExportsListAst(cst: SubhutiCst): SlimeExportSpecifierItem[] {
        return SlimeExportCstToAst.createExportsListAst(cst)
    }

    createExportSpecifierAst(cst: SubhutiCst): SlimeExportSpecifier {
        return SlimeExportCstToAst.createExportSpecifierAst(cst)
    }

    createModuleExportNameAst(cst: SubhutiCst): SlimeIdentifier | SlimeLiteral {
        return SlimeExportCstToAst.createModuleExportNameAst(cst)
    }

    // === module / ImportCstToAst ===

    createImportCallAst(cst: SubhutiCst): SlimeExpression {
        return SlimeImportCstToAst.createImportCallAst(cst)
    }

    createNameSpaceImportAst(cst: SubhutiCst): SlimeImportNamespaceSpecifier {
        return SlimeImportCstToAst.createNameSpaceImportAst(cst)
    }

    createNamedImportsAst(cst: SubhutiCst): Array<SlimeImportSpecifier> {
        return SlimeImportCstToAst.createNamedImportsAst(cst)
    }

    createImportsListAst(cst: SubhutiCst): Array<SlimeImportSpecifier> {
        return SlimeImportCstToAst.createImportsListAst(cst)
    }

    createImportSpecifierAst(cst: SubhutiCst): SlimeImportSpecifier {
        return SlimeImportCstToAst.createImportSpecifierAst(cst)
    }

    createAttributeKeyAst(cst: SubhutiCst): SlimeIdentifier | SlimeLiteral {
        return SlimeImportCstToAst.createAttributeKeyAst(cst)
    }

    createWithEntriesAst(cst: SubhutiCst): any[] {
        return SlimeImportCstToAst.createWithEntriesAst(cst)
    }

    createImportDeclarationAst(cst: SubhutiCst): SlimeImportDeclaration {
        return SlimeImportCstToAst.createImportDeclarationAst(cst)
    }

    createWithClauseAst(cst: SubhutiCst): { attributes: any[], withToken: any, lBraceToken?: any, rBraceToken?: any } {
        return SlimeImportCstToAst.createWithClauseAst(cst)
    }

    createFromClauseAst(cst: SubhutiCst): { source: SlimeStringLiteral, fromToken?: any } {
        return SlimeImportCstToAst.createFromClauseAst(cst)
    }

    createModuleSpecifierAst(cst: SubhutiCst): SlimeStringLiteral {
        return SlimeImportCstToAst.createModuleSpecifierAst(cst)
    }

    createImportClauseAst(cst: SubhutiCst): {
        specifiers: Array<SlimeImportSpecifierItem>,
        lBraceToken?: any,
        rBraceToken?: any
    } {
        return SlimeImportCstToAst.createImportClauseAst(cst)
    }

    createImportedDefaultBindingAst(cst: SubhutiCst): SlimeImportDefaultSpecifier {
        return SlimeImportCstToAst.createImportedDefaultBindingAst(cst)
    }

    createImportedBindingAst(cst: SubhutiCst): SlimeIdentifier {
        return SlimeImportCstToAst.createImportedBindingAst(cst)
    }

    createNamedImportsListAstWrapped(cst: SubhutiCst): {
        specifiers: Array<SlimeImportSpecifierItem>,
        lBraceToken?: any,
        rBraceToken?: any
    } {
        return SlimeImportCstToAst.createNamedImportsListAstWrapped(cst)
    }

    // === module / ModuleCstToAst ===

    createProgramAst(cst: SubhutiCst): SlimeProgram {
        return SlimeModuleCstToAst.createProgramAst(cst)
    }

    createScriptAst(cst: SubhutiCst): SlimeProgram {
        return SlimeModuleCstToAst.createScriptAst(cst)
    }

    createScriptBodyAst(cst: SubhutiCst): SlimeProgram {
        return SlimeModuleCstToAst.createScriptBodyAst(cst)
    }

    createModuleAst(cst: SubhutiCst): SlimeProgram {
        return SlimeModuleCstToAst.createModuleAst(cst)
    }

    createModuleBodyAst(cst: SubhutiCst): SlimeProgram {
        return SlimeModuleCstToAst.createModuleBodyAst(cst)
    }

    createModuleItemAst(item: SubhutiCst): SlimeStatement | SlimeModuleDeclaration | SlimeStatement[] | undefined {
        return SlimeModuleCstToAst.createModuleItemAst(item)
    }

    toProgram(cst: SubhutiCst): SlimeProgram {
        return SlimeModuleCstToAst.toProgram(cst)
    }

    createModuleItemListAst(cst: SubhutiCst): Array<SlimeStatement | SlimeModuleDeclaration> {
        return SlimeModuleCstToAst.createModuleItemListAst(cst)
    }

    // ============================================
    // TypeScript 绫诲瀷杞崲鏂规硶 - 濮旀墭鍒?typescript/ 鐩綍
    // ============================================

    createTSTypeAst(cst: SubhutiCst): any {
        return SlimeTSTypeAnnotationCstToAst.createTSTypeAst(cst)
    }

    createTSTypeAnnotationAst(cst: SubhutiCst): any {
        return SlimeTSTypeAnnotationCstToAst.createTSTypeAnnotationAst(cst)
    }

    createTSUnionOrIntersectionTypeAst(cst: SubhutiCst): any {
        return SlimeTSCompositeTypeCstToAst.createTSUnionOrIntersectionTypeAst(cst)
    }

    createTSIntersectionTypeAst(cst: SubhutiCst): any {
        return SlimeTSCompositeTypeCstToAst.createTSIntersectionTypeAst(cst)
    }

    createTSConditionalTypeAst(cst: SubhutiCst): any {
        return SlimeTSCompositeTypeCstToAst.createTSConditionalTypeAst(cst)
    }

    createTSTypeOperandAst(cst: SubhutiCst): any {
        return SlimeTSCompositeTypeCstToAst.createTSTypeOperandAst(cst)
    }

    createTSPrefixTypeOrPrimaryAst(cst: SubhutiCst): any {
        return SlimeTSCompositeTypeCstToAst.createTSPrefixTypeOrPrimaryAst(cst)
    }

    createTSTypeQueryAst(cst: SubhutiCst): any {
        return SlimeTSCompositeTypeCstToAst.createTSTypeQueryAst(cst)
    }

    createTSTypeOperatorAst(cst: SubhutiCst): any {
        return SlimeTSCompositeTypeCstToAst.createTSTypeOperatorAst(cst)
    }

    createTSInferTypeAst(cst: SubhutiCst): any {
        return SlimeTSCompositeTypeCstToAst.createTSInferTypeAst(cst)
    }

    createTSFunctionTypeAst(cst: SubhutiCst): any {
        return SlimeTSFunctionTypeCstToAst.createTSFunctionTypeAst(cst)
    }

    createTSConstructorTypeAst(cst: SubhutiCst): any {
        return SlimeTSFunctionTypeCstToAst.createTSConstructorTypeAst(cst)
    }

    createTSTypeParameterDeclarationAst(cst: SubhutiCst): any {
        return SlimeTSFunctionTypeCstToAst.createTSTypeParameterDeclarationAst(cst)
    }

    createTSTypeParameterAst(cst: SubhutiCst): any {
        return SlimeTSFunctionTypeCstToAst.createTSTypeParameterAst(cst)
    }

    createTSKeywordTypeWrapperAst(cst: SubhutiCst): any {
        return SlimeTSKeywordTypeCstToAst.createTSKeywordTypeWrapperAst(cst)
    }

    createTSKeywordTypeAst(cst: SubhutiCst, typeName: string): any {
        return SlimeTSKeywordTypeCstToAst.createTSKeywordTypeAst(cst, typeName)
    }

    createTSLiteralTypeAst(cst: SubhutiCst): any {
        return SlimeTSKeywordTypeCstToAst.createTSLiteralTypeAst(cst)
    }

    createTSPrimaryTypeAst(cst: SubhutiCst): any {
        return SlimeTSPrimaryTypeCstToAst.createTSPrimaryTypeAst(cst)
    }

    createTSTypeReferenceAst(cst: SubhutiCst): any {
        return SlimeTSPrimaryTypeCstToAst.createTSTypeReferenceAst(cst)
    }

    createTSTypeNameAst(cst: SubhutiCst): any {
        return SlimeTSPrimaryTypeCstToAst.createTSTypeNameAst(cst)
    }

    buildQualifiedName(parts: string[], loc: SubhutiSourceLocation): any {
        return SlimeTSPrimaryTypeCstToAst.buildQualifiedName(parts, loc)
    }

    createTSTypeParameterInstantiationAst(cst: SubhutiCst): any {
        return SlimeTSPrimaryTypeCstToAst.createTSTypeParameterInstantiationAst(cst)
    }

    createTSTupleTypeAst(cst: SubhutiCst): any {
        return SlimeTSPrimaryTypeCstToAst.createTSTupleTypeAst(cst)
    }

    createTSTupleElementAst(cst: SubhutiCst): any {
        return SlimeTSPrimaryTypeCstToAst.createTSTupleElementAst(cst)
    }

    createTSRestTypeAst(cst: SubhutiCst): any {
        return SlimeTSPrimaryTypeCstToAst.createTSRestTypeAst(cst)
    }

    createTSNamedTupleMemberAst(cst: SubhutiCst): any {
        return SlimeTSPrimaryTypeCstToAst.createTSNamedTupleMemberAst(cst)
    }

    createTSMappedTypeAst(cst: SubhutiCst): any {
        return SlimeTSPrimaryTypeCstToAst.createTSMappedTypeAst(cst)
    }

    createTSParenthesizedTypeAst(cst: SubhutiCst): any {
        return SlimeTSPrimaryTypeCstToAst.createTSParenthesizedTypeAst(cst)
    }

    createTSTypeLiteralAst(cst: SubhutiCst): any {
        return SlimeTSTypeLiteralCstToAst.createTSTypeLiteralAst(cst)
    }

    createTSTypeMemberAst(cst: SubhutiCst): any {
        return SlimeTSTypeLiteralCstToAst.createTSTypeMemberAst(cst)
    }

    createTSPropertyOrMethodSignatureAst(cst: SubhutiCst): any {
        return SlimeTSTypeLiteralCstToAst.createTSPropertyOrMethodSignatureAst(cst)
    }

    extractPropertyNameKey(cst: SubhutiCst): any {
        return SlimeTSTypeLiteralCstToAst.extractPropertyNameKey(cst)
    }

    createTSPropertySignatureAst(cst: SubhutiCst): any {
        return SlimeTSTypeLiteralCstToAst.createTSPropertySignatureAst(cst)
    }

    createTSMethodSignatureAst(cst: SubhutiCst): any {
        return SlimeTSTypeLiteralCstToAst.createTSMethodSignatureAst(cst)
    }

    createTSIndexSignatureAst(cst: SubhutiCst): any {
        return SlimeTSTypeLiteralCstToAst.createTSIndexSignatureAst(cst)
    }

    createTSCallSignatureDeclarationAst(cst: SubhutiCst): any {
        return SlimeTSTypeLiteralCstToAst.createTSCallSignatureDeclarationAst(cst)
    }

    createTSConstructSignatureDeclarationAst(cst: SubhutiCst): any {
        return SlimeTSTypeLiteralCstToAst.createTSConstructSignatureDeclarationAst(cst)
    }

    createTSParameterListAst(cst: SubhutiCst): any[] {
        return SlimeTSTypeLiteralCstToAst.createTSParameterListAst(cst)
    }

    createTSParameterAst(cst: SubhutiCst): any {
        return SlimeTSTypeLiteralCstToAst.createTSParameterAst(cst)
    }

    createTSInterfaceDeclarationAst(cst: SubhutiCst): any {
        return SlimeTSDeclarationCstToAst.createTSInterfaceDeclarationAst(cst)
    }

    createTSInterfaceExtendsAst(cst: SubhutiCst): any[] {
        return SlimeTSDeclarationCstToAst.createTSInterfaceExtendsAst(cst)
    }

    createTSExpressionWithTypeArgumentsAst(cst: SubhutiCst): any {
        return SlimeTSDeclarationCstToAst.createTSExpressionWithTypeArgumentsAst(cst)
    }

    createTSInterfaceBodyAst(cst: SubhutiCst): any {
        return SlimeTSDeclarationCstToAst.createTSInterfaceBodyAst(cst)
    }

    createTSTypeAliasDeclarationAst(cst: SubhutiCst): any {
        return SlimeTSDeclarationCstToAst.createTSTypeAliasDeclarationAst(cst)
    }

    createTSEnumDeclarationAst(cst: SubhutiCst): any {
        return SlimeTSDeclarationCstToAst.createTSEnumDeclarationAst(cst)
    }

    createTSEnumMemberAst(cst: SubhutiCst): any {
        return SlimeTSDeclarationCstToAst.createTSEnumMemberAst(cst)
    }

    createTSAsExpressionAst(expression: any, typeCst: SubhutiCst, loc: any): any {
        return SlimeTSExpressionCstToAst.createTSAsExpressionAst(expression, typeCst, loc)
    }

    createTSSatisfiesExpressionAst(expression: any, typeCst: SubhutiCst, loc: any): any {
        return SlimeTSExpressionCstToAst.createTSSatisfiesExpressionAst(expression, typeCst, loc)
    }

    createTSNonNullExpressionAst(expression: any, loc: any): any {
        return SlimeTSExpressionCstToAst.createTSNonNullExpressionAst(expression, loc)
    }

    createTSTypeAssertionAst(cst: SubhutiCst): any {
        return SlimeTSExpressionCstToAst.createTSTypeAssertionAst(cst)
    }

    createTSTypePredicateAst(cst: SubhutiCst): any {
        return SlimeTSExpressionCstToAst.createTSTypePredicateAst(cst)
    }

    // ============================================
    // TypeScript 琛ㄨ揪寮忕粺涓€鍏ュ彛
    // ============================================

    /**
     * TypeScript 琛ㄨ揪寮忕粺涓€鍏ュ彛
     * 鏍规嵁 CST 鑺傜偣绫诲瀷鍒嗗彂鍒板搴旂殑澶勭悊鏂规硶
     */
    createTSExpressionAst(cst: SubhutiCst): SlimeExpression {
        return SlimeTSExpressionCstToAst.createTSExpressionAst(cst)
    }

    /**
     * 妫€鏌ユ槸鍚︽槸 TypeScript 琛ㄨ揪寮忚妭鐐?
     */
    isTypeScriptExpression(name: string): boolean {
        return SlimeTSExpressionCstToAst.isTypeScriptExpression(name)
    }

    // ============================================
    // 缁熶竴鍚庣紑鎿嶄綔澶勭悊
    // ============================================

    /**
     * 缁熶竴澶勭悊鎵€鏈夊悗缂€鎿嶄綔
     */
    processPostfixOperations(base: SlimeExpression, children: SubhutiCst[], startIdx: number, loc?: any): SlimeExpression {
        return SlimePostfixExpressionCstToAst.processPostfixOperations(base, children, startIdx, loc)
    }

    /**
     * 瑙ｆ瀽 Arguments锛堢粺涓€鍏ュ彛锛?
     */
    createArgumentsAstUnified(cst: SubhutiCst): Array<SlimeCallArgument> {
        return SlimePostfixExpressionCstToAst.createArgumentsAst(cst)
    }

}

// ============================================
// 鍏ㄥ眬鍙敞鍐屾ā寮?
// ============================================

let _SlimeCstToAstUtils: SlimeCstToAst

_SlimeCstToAstUtils = new SlimeCstToAst()

export function registerSlimeCstToAstUtil(instance: SlimeCstToAst) {
    _SlimeCstToAstUtils = instance
}

// Proxy: 淇濇寔 SlimeCstToAstUtils.xxx() 璋冪敤鏂瑰紡锛屽悓鏃舵敮鎸佸姩鎬佹浛鎹?
export const SlimeCstToAstUtils = new Proxy({} as SlimeCstToAst, {
    get(_, prop) {
        const val = (_SlimeCstToAstUtils as any)[prop]
        return typeof val === 'function' ? val.bind(_SlimeCstToAstUtils) : val
    }
})
