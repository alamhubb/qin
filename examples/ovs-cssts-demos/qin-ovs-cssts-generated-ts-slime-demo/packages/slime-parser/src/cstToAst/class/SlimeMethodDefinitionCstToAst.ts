/**
 * MethodDefinitionCstToAst - 鏂规硶瀹氫箟杞崲
 */
import {SubhutiCst} from "subhuti";
import {
    type SlimeBlockStatement, SlimeExpression,
    SlimeFunctionExpression,
    SlimeFunctionParam,
    type SlimeIdentifier, SlimeLiteral, SlimeMethodDefinition, SlimeAstTypeName, SlimePattern, SlimeTokenCreateUtils,
    SlimeAstCreateUtils
} from "slime-ast";
import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";

import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import {SlimeVariableCstToAstSingle} from "../statements/SlimeVariableCstToAst.ts";

export class SlimeMethodDefinitionCstToAstSingle {

    private extractComputedPropertyNameBracketTokens(cst: SubhutiCst): { lBracketToken?: any; rBracketToken?: any } {
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined
        const stack: SubhutiCst[] = [cst]
        while (stack.length > 0) {
            const node = stack.pop()!
            if (node.name === 'LBracket' || node.value === '[') {
                if (!lBracketToken) {
                    lBracketToken = SlimeTokenCreateUtils.createLBracketToken(node.loc)
                }
            } else if (node.name === 'RBracket' || node.value === ']') {
                if (!rBracketToken) {
                    rBracketToken = SlimeTokenCreateUtils.createRBracketToken(node.loc)
                }
            }
            if (node.children) {
                for (const child of node.children) {
                    stack.push(child)
                }
            }
        }
        return { lBracketToken, rBracketToken }
    }

    createMethodDefinitionAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        // 娉ㄦ剰锛氬弬鏁伴『搴忔槸 (staticCst, cst)锛屼笌璋冪敤淇濇寔涓€锟?
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.MethodDefinition?.name);
        const first = cst.getChildren()?.[0]

        if (!first) {
            throw new Error('MethodDefinition has no children')
        }

        if (first.getName() === 'ClassElementName') {
            // MethodDefinition 鍒嗘敮: ClassElementName ( UniqueFormalParameters ) { FunctionBody }
            return SlimeCstToAstUtils.createMethodDefinitionClassElementNameAst(staticCst, cst)
        } else if (first.getName() === 'Get') {
            // MethodDefinition 鍒嗘敮: get ClassElementName ( ) { FunctionBody }
            return SlimeCstToAstUtils.createMethodDefinitionGetterMethodAst(staticCst, cst)
        } else if (first.getName() === 'Set') {
            // MethodDefinition 鍒嗘敮: set ClassElementName ( PropertySetParameterList ) { FunctionBody }
            return SlimeCstToAstUtils.createMethodDefinitionSetterMethodAst(staticCst, cst)
        } else if (first.getName() === SlimeParser.prototype.GeneratorMethod?.name || first.getName() === 'GeneratorMethod') {
            // MethodDefinition 鍒嗘敮: GeneratorMethod
            return SlimeCstToAstUtils.createMethodDefinitionGeneratorMethodAst(staticCst, first)
        } else if (first.getName() === 'AsyncMethod' || first.getName() === SlimeParser.prototype.AsyncMethod?.name) {
            // MethodDefinition 鍒嗘敮: AsyncMethod
            return SlimeCstToAstUtils.createMethodDefinitionAsyncMethodAst(staticCst, first)
        } else if (first.getName() === 'AsyncGeneratorMethod' || first.getName() === SlimeParser.prototype.AsyncGeneratorMethod?.name) {
            // MethodDefinition 鍒嗘敮: AsyncGeneratorMethod
            return SlimeCstToAstUtils.createMethodDefinitionAsyncGeneratorMethodAst(staticCst, first)
        } else if (first.getName() === 'Asterisk') {
            // MethodDefinition 鍒嗘敮: * ClassElementName ( UniqueFormalParameters ) { GeneratorBody }
            return SlimeCstToAstUtils.createMethodDefinitionGeneratorMethodAst(staticCst, cst)
        } else if (first.getName() === 'Async') {
            // MethodDefinition 鍒嗘敮: async [no LineTerminator here] ClassElementName ( ... ) { ... }
            return SlimeCstToAstUtils.createMethodDefinitionAsyncMethodFromChildren(staticCst, cst)
        } else if (first.getName() === 'IdentifierName' || first.getName() === 'IdentifierName' ||
            first.getName() === 'PropertyName' || first.getName() === 'LiteralPropertyName') {
            // 妫€鏌ユ槸鍚︽槸 getter/setter
            if (first.getValue() === 'get' && cst.getChildren()[1]?.name === 'ClassElementName') {
                // getter鏂规硶锛歡et ClassElementName ( ) { FunctionBody }
                return SlimeCstToAstUtils.createMethodDefinitionGetterMethodFromIdentifier(staticCst, cst)
            } else if (first.getValue() === 'set' && cst.getChildren()[1]?.name === 'ClassElementName') {
                // setter鏂规硶锛歴et ClassElementName ( PropertySetParameterList ) { FunctionBody }
                return SlimeCstToAstUtils.createMethodDefinitionSetterMethodFromIdentifier(staticCst, cst)
            }
            // MethodDefinition 鍒嗘敮: 鐩存帴鐨勬爣璇嗙浣滀负鏂规硶鍚?
            return SlimeCstToAstUtils.createMethodDefinitionMethodDefinitionFromIdentifier(staticCst, cst)
        } else {
            throw new Error('涓嶆敮鎸佺殑绫诲瀷: ' + first.getName())
        }
    }


    /**
     * 鍐呴儴杈呭姪鏂规硶锛氬垱寤?MethodDefinition AST
     */
    createMethodDefinitionAstInternal(cst: SubhutiCst, kind: 'method' | 'get' | 'set', generator: boolean, async: boolean): SlimeMethodDefinition {
        // 鏌ユ壘灞炴€у悕
        const classElementName = cst.getChildren()?.find(ch =>
            ch.name === SlimeParser.prototype.ClassElementName?.name ||
            ch.name === 'ClassElementName' ||
            ch.name === SlimeParser.prototype.PropertyName?.name ||
            ch.name === 'PropertyName'
        )

        const key = classElementName ? SlimeCstToAstUtils.createClassElementNameAst(classElementName) : null
        const isComputed = !!classElementName && SlimeCstToAstUtils.isComputedPropertyName(classElementName)
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined
        if (isComputed && classElementName) {
            const bracketTokens = this.extractComputedPropertyNameBracketTokens(classElementName)
            lBracketToken = bracketTokens.lBracketToken
            rBracketToken = bracketTokens.rBracketToken
        }

        // 鏌ユ壘鍙傛暟
        const formalParams = cst.getChildren()?.find(ch =>
            ch.name === SlimeParser.prototype.UniqueFormalParameters?.name ||
            ch.name === 'UniqueFormalParameters' ||
            ch.name === SlimeParser.prototype.FormalParameters?.name ||
            ch.name === 'FormalParameters'
        )
        const params = formalParams ? SlimeCstToAstUtils.createFormalParametersAst(formalParams) : []

        // 鏌ユ壘鍑芥暟锟?
        const bodyNode = cst.getChildren()?.find(ch =>
            ch.name === 'GeneratorBody' || ch.name === 'AsyncFunctionBody' ||
            ch.name === 'AsyncGeneratorBody' || ch.name === 'FunctionBody' ||
            ch.name === SlimeParser.prototype.FunctionBody?.name
        )
        const bodyStatements = bodyNode ? SlimeCstToAstUtils.createFunctionBodyAst(bodyNode) : []
        const body = SlimeAstCreateUtils.createBlockStatement(bodyStatements, bodyNode?.loc)

        const value: SlimeFunctionExpression = {
            type: SlimeAstTypeName.FunctionExpression,
            id: null,
            params: params as any,
            body: body,
            generator: generator,
            async: async,
            loc: cst.getLoc()
        } as any

        const methodDef = SlimeAstCreateUtils.createMethodDefinition(key, value, kind, isComputed, false, cst.getLoc())
        if (lBracketToken) {
            (methodDef as any).lBracketToken = lBracketToken
        }
        if (rBracketToken) {
            (methodDef as any).rBracketToken = rBracketToken
        }
        return methodDef
    }


    // ==================== 鍑芥暟/绫荤浉鍏宠浆鎹㈡柟锟?====================

    /**
     * GeneratorMethod CST 锟?AST
     * GeneratorMethod -> * ClassElementName ( UniqueFormalParameters ) { GeneratorBody }
     */
    createGeneratorMethodAst(cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeCstToAstUtils.createMethodDefinitionAstInternal(cst, 'method', true, false)
    }


    /**
     * AsyncMethod CST 锟?AST
     * AsyncMethod -> async ClassElementName ( UniqueFormalParameters ) { AsyncFunctionBody }
     */
    createAsyncMethodAst(cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeCstToAstUtils.createMethodDefinitionAstInternal(cst, 'method', false, true)
    }


    /**
     * AsyncGeneratorMethod CST 锟?AST
     */
    createAsyncGeneratorMethodAst(cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeCstToAstUtils.createMethodDefinitionAstInternal(cst, 'method', true, true)
    }




    // ==================== ES2025 鍐呴儴杈呭姪鏂规硶 ====================
    // 浠ヤ笅鏂规硶鏄锟?ES2025 Parser CST 缁撴瀯鐨勫唴閮ㄨ緟鍔╂柟娉曪紝涓嶇洿鎺ュ锟?CST 瑙勫垯锟?
    // 瀛樺湪蹇呰鎬э細ES2025 Parser 锟?CST 缁撴瀯锟?ES6 鏈夊樊寮傦紝闇€瑕佷笓闂ㄧ殑澶勭悊閫昏緫锟?

    /**
     * [鍐呴儴鏂规硶] 浠庣洿鎺ョ殑鏍囪瘑绗﹀垱寤烘柟娉曞畾锟?
     * 澶勭悊 ES2025 Parser 锟?IdentifierNameTok ( UniqueFormalParameters ) { FunctionBody } 缁撴瀯
     * @internal
     */
    createMethodDefinitionMethodDefinitionFromIdentifier(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        let i = 0
        const children = cst.getChildren()

        // Token fields
        let staticToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        // 妫€锟?token
        if (staticCst && (staticCst.name === 'Static' || staticCst.value === 'static')) {
            staticToken = SlimeTokenCreateUtils.createStaticToken(staticCst.loc)
        }

        // 绗竴涓瓙鑺傜偣鏄柟娉曞悕锛堝彲鑳芥槸 IdentifierNameTok, IdentifierName, PropertyName, LiteralPropertyName锟?
        const firstChild = children[i++]
        let key: SlimeIdentifier | SlimeLiteral | SlimeExpression

        if (firstChild.getName() === 'IdentifierName') {
            // 鐩存帴锟?token
            key = SlimeAstCreateUtils.createIdentifier(firstChild.getValue(), firstChild.getLoc())
        } else if (firstChild.getName() === 'IdentifierName') {
            // IdentifierName 瑙勫垯鑺傜偣
            const tokenCst = firstChild.getChildren()[0]
            key = SlimeAstCreateUtils.createIdentifier(tokenCst.getValue(), tokenCst.getLoc())
        } else if (firstChild.getName() === 'PropertyName' || firstChild.getName() === 'LiteralPropertyName') {
            key = SlimeCstToAstUtils.createPropertyNameAst(firstChild)
        } else {
            key = SlimeCstToAstUtils.createClassElementNameAst(firstChild)
        }

        const isComputed = SlimeCstToAstUtils.isComputedPropertyName(firstChild)
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined
        if (isComputed) {
            const bracketTokens = this.extractComputedPropertyNameBracketTokens(firstChild)
            lBracketToken = bracketTokens.lBracketToken
            rBracketToken = bracketTokens.rBracketToken
        }

        // LParen
        if (children[i]?.name === 'LParen' || children[i]?.value === '(') {
            lParenToken = SlimeTokenCreateUtils.createLParenToken(children[i].loc)
            i++
        }

        // UniqueFormalParameters (浣跨敤鍖呰绫诲瀷)
        let params: SlimeFunctionParam[] = []
        if (children[i]?.name === 'UniqueFormalParameters' || children[i]?.name === SlimeParser.prototype.UniqueFormalParameters?.name) {
            params = SlimeCstToAstUtils.createUniqueFormalParametersAstWrapped(children[i])
            i++
        } else if (children[i]?.name === 'FormalParameters' || children[i]?.name === SlimeParser.prototype.FormalParameters?.name) {
            params = SlimeCstToAstUtils.createFormalParametersAstWrapped(children[i])
            i++
        }

        // RParen
        if (children[i]?.name === 'RParen' || children[i]?.value === ')') {
            rParenToken = SlimeTokenCreateUtils.createRParenToken(children[i].loc)
            i++
        }
        // LBrace
        if (children[i]?.name === 'LBrace' || children[i]?.value === '{') {
            lBraceToken = SlimeTokenCreateUtils.createLBraceToken(children[i].loc)
            i++
        }

        // FunctionBody
        let body: SlimeBlockStatement
        if (children[i]?.name === 'FunctionBody' || children[i]?.name === SlimeParser.prototype.FunctionBody?.name) {
            const bodyStatements = SlimeCstToAstUtils.createFunctionBodyAst(children[i])
            body = SlimeAstCreateUtils.createBlockStatement(bodyStatements, children[i].loc, lBraceToken, rBraceToken)
            i++
        } else {
            body = SlimeAstCreateUtils.createBlockStatement([], undefined, lBraceToken, rBraceToken)
        }

        // RBrace
        if (children[i]?.name === 'RBrace' || children[i]?.value === '}') {
            rBraceToken = SlimeTokenCreateUtils.createRBraceToken(children[i].loc)
        }

        // 鍒涘缓鍑芥暟琛ㄨ揪锟?
        const functionExpression = SlimeAstCreateUtils.createFunctionExpression(
            body, null, params, false, false, cst.getLoc(),
            undefined, undefined, undefined, lParenToken, rParenToken, lBraceToken, rBraceToken
        )

        // 妫€鏌ユ槸鍚︽槸 constructor
        const isConstructor = key.type === "Identifier" && (key as SlimeIdentifier).name === "constructor" &&
            !SlimeCstToAstUtils.isStaticModifier(staticCst)

        const isStatic = SlimeCstToAstUtils.isStaticModifier(staticCst)
        const kind = isConstructor ? 'constructor' : 'method' as "constructor" | "method" | "get" | "set"

        const methodDef = SlimeAstCreateUtils.createMethodDefinition(key, functionExpression, kind, isComputed, isStatic, cst.getLoc(), staticToken)
        if (lBracketToken) {
            (methodDef as any).lBracketToken = lBracketToken
        }
        if (rBracketToken) {
            (methodDef as any).rBracketToken = rBracketToken
        }

        return methodDef
    }


    /**
     * [鍐呴儴鏂规硶] 鏅€氭柟娉曞畾涔?
     * 澶勭悊 ES2025 Parser 鐨?ClassElementName ( UniqueFormalParameters ) TSTypeAnnotation_opt { FunctionBody } 缁撴瀯
     * [TypeScript] 鏀寔杩斿洖绫诲瀷娉ㄨВ
     * @internal
     */
    createMethodDefinitionClassElementNameAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        const children = cst.getChildren()

        // Token fields
        let staticToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined
        let asteriskToken: any = undefined
        let isGenerator = false
        let returnType: any = undefined  // [TypeScript] 杩斿洖绫诲瀷
        let typeParameters: any = undefined  // [TypeScript] 娉涘瀷鍙傛暟

        // 妫€鏌?token
        if (staticCst && (staticCst.name === 'Static' || staticCst.value === 'static')) {
            staticToken = SlimeTokenCreateUtils.createStaticToken(staticCst.loc)
        }

        // 閬嶅巻瀛愯妭鐐规彁鍙栧悇閮ㄥ垎
        let classElementNameCst: SubhutiCst | null = null
        let paramsCst: SubhutiCst | null = null
        let bodyCst: SubhutiCst | null = null

        for (const child of children) {
            const name = child.getName()
            if (name === 'ClassElementName' || name === SlimeParser.prototype.ClassElementName?.name) {
                classElementNameCst = child
            } else if (name === 'Asterisk' || child.getValue() === '*') {
                asteriskToken = SlimeTokenCreateUtils.createAsteriskToken(child.getLoc())
                isGenerator = true
            } else if (name === 'LParen' || child.getValue() === '(') {
                lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
            } else if (name === 'RParen' || child.getValue() === ')') {
                rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
            } else if (name === 'LBrace' || child.getValue() === '{') {
                lBraceToken = SlimeTokenCreateUtils.createLBraceToken(child.getLoc())
            } else if (name === 'RBrace' || child.getValue() === '}') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(child.getLoc())
            } else if (name === 'UniqueFormalParameters' || name === SlimeParser.prototype.UniqueFormalParameters?.name ||
                       name === 'FormalParameters' || name === SlimeParser.prototype.FormalParameters?.name) {
                paramsCst = child
            } else if (name === 'FunctionBody' || name === SlimeParser.prototype.FunctionBody?.name) {
                bodyCst = child
            } else if (name === 'TSTypeAnnotation') {
                // [TypeScript] 杩斿洖绫诲瀷娉ㄨВ
                returnType = SlimeCstToAstUtils.createTSTypeAnnotationAst(child)
            } else if (name === 'TSTypeParameterDeclaration') {
                // [TypeScript] 娉涘瀷鍙傛暟
                typeParameters = SlimeCstToAstUtils.createTSTypeParameterDeclarationAst(child)
            }
        }

        if (!classElementNameCst) {
            throw new Error('MethodDefinition missing ClassElementName')
        }

        const key = SlimeCstToAstUtils.createClassElementNameAst(classElementNameCst)

        // 瑙ｆ瀽鍙傛暟
        let params: SlimeFunctionParam[] = []
        if (paramsCst) {
            if (paramsCst.name === 'UniqueFormalParameters' || paramsCst.name === SlimeParser.prototype.UniqueFormalParameters?.name) {
                params = SlimeCstToAstUtils.createUniqueFormalParametersAstWrapped(paramsCst)
            } else {
                params = SlimeCstToAstUtils.createFormalParametersAstWrapped(paramsCst)
            }
        }

        // 瑙ｆ瀽鍑芥暟浣?
        let body: SlimeBlockStatement
        if (bodyCst) {
            const bodyStatements = SlimeCstToAstUtils.createFunctionBodyAst(bodyCst)
            body = SlimeAstCreateUtils.createBlockStatement(bodyStatements, cst.getLoc(), lBraceToken, rBraceToken)
        } else {
            body = SlimeAstCreateUtils.createBlockStatement([], undefined, lBraceToken, rBraceToken)
        }

        // 鍒涘缓鍑芥暟琛ㄨ揪寮忥紝浼犻€?token 淇℃伅
        const functionExpression = SlimeAstCreateUtils.createFunctionExpression(
            body, null, params, isGenerator, false, cst.getLoc(),
            undefined, undefined, asteriskToken, lParenToken, rParenToken, lBraceToken, rBraceToken
        ) as SlimeFunctionExpression & { returnType?: any, typeParameters?: any }

        // [TypeScript] 娣诲姞杩斿洖绫诲瀷
        if (returnType) {
            functionExpression.returnType = returnType
        }

        // [TypeScript] 娣诲姞娉涘瀷鍙傛暟
        if (typeParameters) {
            functionExpression.typeParameters = typeParameters
        }

        // 妫€鏌ユ槸鍚︽槸璁＄畻灞炴€?
        const isComputed = SlimeCstToAstUtils.isComputedPropertyName(classElementNameCst)
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined
        if (isComputed) {
            const bracketTokens = this.extractComputedPropertyNameBracketTokens(classElementNameCst)
            lBracketToken = bracketTokens.lBracketToken
            rBracketToken = bracketTokens.rBracketToken
        }

        // 妫€鏌ユ槸鍚︽槸 constructor
        const isConstructor = key.type === "Identifier" && (key as SlimeIdentifier).name === "constructor" &&
            !SlimeCstToAstUtils.isStaticModifier(staticCst)

        const isStatic = SlimeCstToAstUtils.isStaticModifier(staticCst)
        const kind = isConstructor ? 'constructor' : 'method' as "constructor" | "method" | "get" | "set"

        const methodDef = SlimeAstCreateUtils.createMethodDefinition(
            key,
            functionExpression,
            kind,
            isComputed,
            isStatic,
            cst.getLoc(),
            staticToken,
            undefined,
            undefined,
            undefined,
            asteriskToken
        )

        if (lBracketToken) {
            (methodDef as any).lBracketToken = lBracketToken
        }
        if (rBracketToken) {
            (methodDef as any).rBracketToken = rBracketToken
        }
        return methodDef
    }


    /**
     * [鍐呴儴鏂规硶] getter 鏂规硶 (浠?IdentifierNameTok="get" 寮€濮?
     * 澶勭悊 ES2025 Parser 鐨?IdentifierNameTok="get" ClassElementName ( ) TSTypeAnnotation_opt { FunctionBody } 缁撴瀯
     * [TypeScript] 鏀寔杩斿洖绫诲瀷娉ㄨВ
     * @internal
     */
    createMethodDefinitionGetterMethodFromIdentifier(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        const children = cst.getChildren()
        let i = 0

        // Token fields
        let staticToken: any = undefined
        let getToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined
        let returnType: any = undefined  // [TypeScript] 杩斿洖绫诲瀷

        // 妫€鏌?token
        if (staticCst && (staticCst.name === 'Static' || staticCst.value === 'static')) {
            staticToken = SlimeTokenCreateUtils.createStaticToken(staticCst.loc)
        }

        // IdentifierNameTok="get"
        if (children[i]?.value === 'get') {
            getToken = SlimeTokenCreateUtils.createGetToken(children[i].loc)
            i++
        }

        const classElementNameCst = children[i++]
        const key = SlimeCstToAstUtils.createClassElementNameAst(classElementNameCst)
        const isComputed = SlimeCstToAstUtils.isComputedPropertyName(classElementNameCst)
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined
        if (isComputed) {
            const bracketTokens = this.extractComputedPropertyNameBracketTokens(classElementNameCst)
            lBracketToken = bracketTokens.lBracketToken
            rBracketToken = bracketTokens.rBracketToken
        }

        // LParen - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'LParen') {
            lParenToken = SlimeTokenCreateUtils.createLParenToken(children[i].loc)
            i++
        }
        // RParen - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'RParen') {
            rParenToken = SlimeTokenCreateUtils.createRParenToken(children[i].loc)
            i++
        }
        // [TypeScript] TSTypeAnnotation - 杩斿洖绫诲瀷娉ㄨВ
        if (children[i]?.name === 'TSTypeAnnotation') {
            returnType = SlimeCstToAstUtils.createTSTypeAnnotationAst(children[i])
            i++
        }
        // LBrace - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'LBrace') {
            lBraceToken = SlimeTokenCreateUtils.createLBraceToken(children[i].loc)
            i++
        }

        // FunctionBody
        let body: SlimeBlockStatement
        if (children[i]?.name === 'FunctionBody' || children[i]?.name === SlimeParser.prototype.FunctionBody?.name) {
            const bodyStatements = SlimeCstToAstUtils.createFunctionBodyAst(children[i])
            i++
            // RBrace
            if (children[i]?.name === 'RBrace') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(children[i].loc)
            }
            body = SlimeAstCreateUtils.createBlockStatement(bodyStatements, cst.getLoc(), lBraceToken, rBraceToken)
        } else {
            if (children[i]?.name === 'RBrace') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(children[i].loc)
            }
            body = SlimeAstCreateUtils.createBlockStatement([], undefined, lBraceToken, rBraceToken)
        }

        // 鍒涘缓鍑芥暟琛ㄨ揪寮忥紝浼犻€?token 淇℃伅
        const functionExpression = SlimeAstCreateUtils.createFunctionExpression(
            body, null, [], false, false, cst.getLoc(),
            undefined, undefined, undefined, lParenToken, rParenToken, lBraceToken, rBraceToken
        ) as SlimeFunctionExpression & { returnType?: any }

        // [TypeScript] 娣诲姞杩斿洖绫诲瀷
        if (returnType) {
            functionExpression.returnType = returnType
        }

        const methodDef = SlimeAstCreateUtils.createMethodDefinition(key, functionExpression, 'get', isComputed, SlimeCstToAstUtils.isStaticModifier(staticCst), cst.getLoc(), staticToken, getToken)
        if (lBracketToken) {
            (methodDef as any).lBracketToken = lBracketToken
        }
        if (rBracketToken) {
            (methodDef as any).rBracketToken = rBracketToken
        }

        return methodDef
    }


    /**
     * [鍐呴儴鏂规硶] setter 鏂规硶 (浠?IdentifierNameTok="set" 寮€濮?
     * 澶勭悊 ES2025 Parser 鐨?IdentifierNameTok="set" ClassElementName ( ... ) { FunctionBody } 缁撴瀯
     * @internal
     */
    createMethodDefinitionSetterMethodFromIdentifier(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        const children = cst.getChildren()
        let i = 0

        // Token fields
        let staticToken: any = undefined
        let setToken: any = undefined
        let lParenToken: any = undefined
        let rParenToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        // 妫€鏌?token
        if (staticCst && (staticCst.name === 'Static' || staticCst.value === 'static')) {
            staticToken = SlimeTokenCreateUtils.createStaticToken(staticCst.loc)
        }

        // IdentifierNameTok="set"
        if (children[i]?.value === 'set') {
            setToken = SlimeTokenCreateUtils.createSetToken(children[i].loc)
            i++
        }

        const classElementNameCst = children[i++]
        const key = SlimeCstToAstUtils.createClassElementNameAst(classElementNameCst)
        const isComputed = SlimeCstToAstUtils.isComputedPropertyName(classElementNameCst)
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined
        if (isComputed) {
            const bracketTokens = this.extractComputedPropertyNameBracketTokens(classElementNameCst)
            lBracketToken = bracketTokens.lBracketToken
            rBracketToken = bracketTokens.rBracketToken
        }

        // LParen - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'LParen') {
            lParenToken = SlimeTokenCreateUtils.createLParenToken(children[i].loc)
            i++
        }

        // PropertySetParameterList 鎴栫洿鎺ョ殑 BindingIdentifier
        let params: SlimePattern[] = []
        if (children[i]?.name === 'PropertySetParameterList' || children[i]?.name === SlimeParser.prototype.PropertySetParameterList?.name) {
            params = SlimeCstToAstUtils.createPropertySetParameterListAst(children[i])
            i++
        } else if (children[i]?.name === 'BindingIdentifier' || children[i]?.name === 'BindingElement') {
            // 鐩存帴鐨勫弬鏁版爣璇嗙
            params = [SlimeCstToAstUtils.createBindingIdentifierAst(children[i])]
            i++
        }

        // RParen - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'RParen') {
            rParenToken = SlimeTokenCreateUtils.createRParenToken(children[i].loc)
            i++
        }
        // LBrace - 淇濆瓨 token 淇℃伅
        if (children[i]?.name === 'LBrace') {
            lBraceToken = SlimeTokenCreateUtils.createLBraceToken(children[i].loc)
            i++
        }

        // FunctionBody
        let body: SlimeBlockStatement
        if (children[i]?.name === 'FunctionBody' || children[i]?.name === SlimeParser.prototype.FunctionBody?.name) {
            const bodyStatements = SlimeCstToAstUtils.createFunctionBodyAst(children[i])
            i++
            // RBrace
            if (children[i]?.name === 'RBrace') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(children[i].loc)
            }
            body = SlimeAstCreateUtils.createBlockStatement(bodyStatements, cst.getLoc(), lBraceToken, rBraceToken)
        } else {
            if (children[i]?.name === 'RBrace') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(children[i].loc)
            }
            body = SlimeAstCreateUtils.createBlockStatement([], undefined, lBraceToken, rBraceToken)
        }

        // 鍒涘缓鍑芥暟琛ㄨ揪寮忥紝浼犻€?token 淇℃伅
        const functionExpression = SlimeAstCreateUtils.createFunctionExpression(
            body, null, params as any, false, false, cst.getLoc(),
            undefined, undefined, undefined, lParenToken, rParenToken, lBraceToken, rBraceToken
        )

        const methodDef = SlimeAstCreateUtils.createMethodDefinition(key, functionExpression, 'set', isComputed, SlimeCstToAstUtils.isStaticModifier(staticCst), cst.getLoc(), staticToken, undefined, setToken)
        if (lBracketToken) {
            (methodDef as any).lBracketToken = lBracketToken
        }
        if (rBracketToken) {
            (methodDef as any).rBracketToken = rBracketToken
        }

        return methodDef
    }


    /**
     * [鍐呴儴鏂规硶] generator 鏂规硶 (锟?MethodDefinition children 鐩存帴澶勭悊)
     * @internal
     */
    createMethodDefinitionGeneratorMethodFromChildren(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        return SlimeCstToAstUtils.createMethodDefinitionGeneratorMethodAst(staticCst, cst)
    }


    /**
     * [鍐呴儴鏂规硶] async 鏂规硶 (锟?MethodDefinition children 鐩存帴澶勭悊)
     * @internal
     */
    createMethodDefinitionAsyncMethodFromChildren(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimeMethodDefinition {
        // 妫€鏌ユ槸鍚︽槸 AsyncGeneratorMethod (async * ...)
        const children = cst.getChildren()
        if (children[1]?.name === 'Asterisk') {
            return SlimeCstToAstUtils.createMethodDefinitionAsyncGeneratorMethodAst(staticCst, cst)
        }
        return SlimeCstToAstUtils.createMethodDefinitionAsyncMethodAst(staticCst, cst)
    }


    /**
     * 澶勭悊 PropertySetParameterList
     */
    createPropertySetParameterListAst(cst: SubhutiCst): SlimePattern[] {
        // PropertySetParameterList: FormalParameter
        if (!cst.getChildren() || cst.getChildren().length === 0) {
            return []
        }
        const first = cst.getChildren()[0]
        if (first.getName() === 'FormalParameter' || first.getName() === SlimeParser.prototype.FormalParameter?.name) {
            return [SlimeCstToAstUtils.createFormalParameterAst(first)]
        }
        if (first.getName() === 'BindingElement' || first.getName() === SlimeParser.prototype.BindingElement?.name) {
            return [SlimeCstToAstUtils.createBindingElementAst(first)]
        }
        return []
    }


    /** 杩斿洖鍖呰绫诲瀷鐨勭増锟?*/
    createPropertySetParameterListAstWrapped(cst: SubhutiCst): SlimeFunctionParam[] {
        // PropertySetParameterList: FormalParameter
        if (!cst.getChildren() || cst.getChildren().length === 0) {
            return []
        }
        const first = cst.getChildren()[0]
        if (first.getName() === 'FormalParameter' || first.getName() === SlimeParser.prototype.FormalParameter?.name) {
            return [SlimeAstCreateUtils.createFunctionParam(SlimeCstToAstUtils.createFormalParameterAst(first), undefined)]
        }
        if (first.getName() === 'BindingElement' || first.getName() === SlimeParser.prototype.BindingElement?.name) {
            return [SlimeAstCreateUtils.createFunctionParam(SlimeCstToAstUtils.createBindingElementAst(first), undefined)]
        }
        return []
    }





}

export const SlimeMethodDefinitionCstToAst = new SlimeMethodDefinitionCstToAstSingle()
