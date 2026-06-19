/**
 * ClassDeclarationCstToAst - class body/element 杞崲
 */
import { SubhutiCst } from "subhuti";
import { SlimeTokenConsumer } from "../../SlimeTokenConsumer.ts";
import {

    SlimeClassBody, SlimeClassDeclaration, SlimeClassExpression,
    SlimeExpression, SlimeIdentifier, SlimeLiteral,
    SlimeMethodDefinition, SlimeAstTypeName,
    SlimePropertyDefinition, SlimeStatement,
    SlimeTokenCreateUtils, SlimeAstCreateUtils
} from "slime-ast";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";

export class SlimeClassDeclarationCstToAstSingle {

    createClassDeclarationAst(cst: SubhutiCst): SlimeClassDeclaration {
        // 妫€锟?CST 鑺傜偣鍚嶇О鏄惁锟?ClassDeclaration
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ClassDeclaration?.name);

        // Token fields
        let classToken: any = undefined
        let id: SlimeIdentifier | null = null
        let classTailCst: SubhutiCst | null = null
        // [TypeScript] 娉涘瀷鍙傛暟
        let typeParameters: any = undefined

        // 閬嶅巻瀛愯妭鐐癸紝鎻愬彇 class token銆佹爣璇嗙锟?ClassTail
        for (const child of cst.getChildren()) {
            const name = child.getName()
            if (name === 'Class' || child.getValue() === 'class') {
                classToken = SlimeTokenCreateUtils.createClassToken(child.getLoc())
            } else if (name === SlimeParser.prototype.BindingIdentifier?.name || name === 'BindingIdentifier') {
                id = SlimeCstToAstUtils.createBindingIdentifierAst(child)
            } else if (name === SlimeParser.prototype.ClassTail?.name || name === 'ClassTail') {
                classTailCst = child
            } else if (name === 'TSTypeParameterDeclaration') {
                // [TypeScript] 娉涘瀷鍙傛暟 <T extends ...>
                typeParameters = SlimeCstToAstUtils.createTSTypeParameterDeclarationAst(child)
            }
        }

        // ClassTail 鏄繀椤荤殑
        if (!classTailCst) {
            throw new Error('ClassDeclaration missing ClassTail')
        }

        // 瑙ｆ瀽 ClassTail锛岃幏鍙栫被浣撳拰鐖剁被淇℃伅
        const classTailResult = SlimeCstToAstUtils.createClassTailAst(classTailCst)

        // 鍒涘缓绫诲０锟?AST 鑺傜偣锛坕d 鍙兘锟?null锛岀敤浜庡尶鍚嶇被锟?
        const ast = SlimeAstCreateUtils.createClassDeclaration(
            id, classTailResult.body, classTailResult.superClass, cst.getLoc(),
            classToken, classTailResult.extendsToken
        ) as any

        // [TypeScript] 娣诲姞娉涘瀷鍙傛暟
        if (typeParameters) {
            ast.typeParameters = typeParameters
        }

        return ast
    }

    createClassExpressionAst(cst: SubhutiCst): SlimeClassExpression {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ClassExpression?.name);

        let classToken: any = undefined
        let id: SlimeIdentifier | null = null // class 琛ㄨ揪寮忓彲閫夌殑鏍囪瘑绗?
        let classTailCst: SubhutiCst | null = null
        // [TypeScript] 娉涘瀷鍙傛暟
        let typeParameters: any = undefined

        // 閬嶅巻瀛愯妭鐐?
        for (const child of cst.getChildren()) {
            const name = child.getName()
            if (name === 'Class' || child.getValue() === 'class') {
                classToken = SlimeTokenCreateUtils.createClassToken(child.getLoc())
            } else if (name === SlimeParser.prototype.BindingIdentifier?.name || name === 'BindingIdentifier') {
                id = SlimeCstToAstUtils.createBindingIdentifierAst(child)
            } else if (name === SlimeParser.prototype.ClassTail?.name || name === 'ClassTail') {
                classTailCst = child
            } else if (name === 'TSTypeParameterDeclaration') {
                // [TypeScript] 娉涘瀷鍙傛暟 <T extends ...>
                typeParameters = SlimeCstToAstUtils.createTSTypeParameterDeclarationAst(child)
            }
        }

        if (!classTailCst) {
            throw new Error('ClassExpression missing ClassTail')
        }

        const classTail = SlimeCstToAstUtils.createClassTailAst(classTailCst)

        const ast = SlimeAstCreateUtils.createClassExpression(id, classTail.superClass, classTail.body, cst.getLoc()) as any
        ast.classToken = classToken
        ast.extendsToken = classTail.extendsToken

        // [TypeScript] 娣诲姞娉涘瀷鍙傛暟
        if (typeParameters) {
            ast.typeParameters = typeParameters
        }

        return ast
    }

    createClassBodyAst(cst: SubhutiCst): SlimeClassBody {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ClassBody?.name);
        const elementsWrapper = cst.getChildren() && cst.getChildren()[0] // ClassBody -> ClassElementList?锛岀涓€椤逛负鍒楄〃瀹瑰櫒
        const body: Array<SlimeMethodDefinition | SlimePropertyDefinition | any> = [] // 鏀堕泦绫绘垚鍛?(any 鐢ㄤ簬 StaticBlock)
        if (elementsWrapper && Array.isArray(elementsWrapper.getChildren())) {
            for (const element of elementsWrapper.getChildren()) { // 閬嶅巻 ClassElement
                const elementChildren = element.children ?? [] // 鍏煎鏃犲瓙鑺傜偣鎯呭喌
                if (!elementChildren.length) {
                    continue // 娌℃湁鍐呭?ClassElement 鐩存帴蹇界暐
                }

                // 鎵惧埌鐪熸鐨勬垚鍛樺畾涔夛紙璺宠繃 ?SemicolonASI?
                let staticCst: SubhutiCst | null = null
                let targetCst: SubhutiCst | null = null
                let classStaticBlockCst: SubhutiCst | null = null
                let semicolonCst: SubhutiCst | null = null
                // [TypeScript] 璁块棶淇グ绗?
                let accessibility: 'public' | 'private' | 'protected' | undefined = undefined
                // [TypeScript] readonly 淇グ绗?
                let isReadonly = false
                // [TypeScript] override 淇グ绗?
                let isOverride = false

                for (const child of elementChildren) {
                    if (child.getName() === 'Static' || child.getValue() === 'static') {
                        staticCst = child
                    } else if (child.getName() === 'SemicolonASI' || child.getName() === 'Semicolon' || child.getValue() === ';') {
                        // Record empty class element semicolon.
                        semicolonCst = child
                        continue
                    } else if (child.getName() === 'ClassStaticBlock') {
                        // ES2022 闈欐€佸潡
                        classStaticBlockCst = child
                    } else if (child.getName() === SlimeParser.prototype.MethodDefinition?.name ||
                        child.getName() === SlimeParser.prototype.FieldDefinition?.name ||
                        child.getName() === 'MethodDefinition' || child.getName() === 'FieldDefinition') {
                        targetCst = child
                    } else if (child.getName() === 'TSAccessibilityModifier') {
                        // [TypeScript] 璁块棶淇グ绗﹁妭鐐癸紝闇€瑕佹彁鍙栧瓙鑺傜偣鐨勫€?
                        const modifierChild = child.getChildren()?.[0]
                        if (modifierChild) {
                            const modifierValue = modifierChild.value || modifierChild.name?.toLowerCase()
                            if (modifierValue === 'public') accessibility = 'public'
                            else if (modifierValue === 'private') accessibility = 'private'
                            else if (modifierValue === 'protected') accessibility = 'protected'
                            else if (modifierValue === 'readonly') isReadonly = true
                            else if (modifierValue === 'override') isOverride = true
                        }
                    } else if (child.getName() === 'TSOverride') {
                        // [TypeScript] override 鍏抽敭瀛?token
                        isOverride = true
                    } else if (child.getValue() === 'public' || child.getName() === 'Public') {
                        accessibility = 'public'
                    } else if (child.getValue() === 'private' || child.getName() === 'Private') {
                        accessibility = 'private'
                    } else if (child.getValue() === 'protected' || child.getName() === 'Protected') {
                        accessibility = 'protected'
                    } else if (child.getValue() === 'readonly' || child.getName() === 'Readonly') {
                        // [TypeScript] readonly 淇グ绗?
                        isReadonly = true
                    } else if (child.getValue() === 'override' || child.getName() === 'Override') {
                        // [TypeScript] override 淇グ绗?
                        isOverride = true
                    }
                }

                // 澶勭悊闈欐€佸潡
                if (classStaticBlockCst) {
                    const staticBlock = SlimeCstToAstUtils.createClassStaticBlockAst(classStaticBlockCst)
                    if (staticBlock) {
                        body.push(staticBlock)
                    }
                    continue
                }

                const getSemicolonToken = (semicolonNode: SubhutiCst | null): any => {
                    if (!semicolonNode) return undefined
                    if (semicolonNode.name === 'SemicolonASI') {
                        const semicolonChild = semicolonNode.children?.find((ch: any) =>
                            ch.name === 'Semicolon' || ch.value === ';'
                        )
                        return semicolonChild
                            ? SlimeTokenCreateUtils.createSemicolonToken(semicolonChild.loc)
                            : undefined
                    }
                    if (semicolonNode.name === 'Semicolon' || semicolonNode.value === ';') {
                        return SlimeTokenCreateUtils.createSemicolonToken(semicolonNode.loc)
                    }
                    return undefined
                }

                if (targetCst) {
                    // 鏍规嵁鎴愬憳绫诲瀷鐩存帴璋冪敤瀵瑰簲鏂规硶
                    if (targetCst.getName() === SlimeParser.prototype.MethodDefinition?.name) {
                        const methodDef = SlimeCstToAstUtils.createMethodDefinitionAst(staticCst, targetCst)
                        // [TypeScript] 娣诲姞璁块棶淇グ绗?
                        if (accessibility) {
                            (methodDef as any).accessibility = accessibility
                        }
                        // [TypeScript] 娣诲姞 override 淇グ绗?
                        if (isOverride) {
                            (methodDef as any).override = true
                        }
                        body.push(methodDef)
                    } else if (targetCst.getName() === SlimeParser.prototype.FieldDefinition?.name) {
                        const fieldDef = SlimeCstToAstUtils.createFieldDefinitionAst(staticCst, targetCst)
                        const semicolonToken = getSemicolonToken(semicolonCst)
                        if (semicolonToken) {
                            (fieldDef as any).semicolonToken = semicolonToken
                        }
                        // [TypeScript] 娣诲姞璁块棶淇グ绗?
                        if (accessibility) {
                            (fieldDef as any).accessibility = accessibility
                        }
                        // [TypeScript] 娣诲姞 readonly 淇グ绗?
                        if (isReadonly) {
                            (fieldDef as any).readonly = true
                        }
                        body.push(fieldDef)
                    }
                } else if (semicolonCst) {
                    body.push(SlimeCstToAstUtils.createEmptyStatementAst(semicolonCst))
                }
            }
        }
        return {
            type: astName as any, // 鏋勶拷?ClassBody AST
            body: body, // 鎸傝浇绫绘垚鍛樻暟锟?
            loc: cst.getLoc() // 閫忎紶浣嶇疆淇℃伅
        }
    }


    /**
     * ClassElementList CST 锟?AST
     */
    createClassElementListAst(cst: SubhutiCst): any[] {
        const elements: any[] = []
        for (const child of cst.getChildren() || []) {
            if (child.getName() === SlimeParser.prototype.ClassElement?.name || child.getName() === 'ClassElement') {
                const element = SlimeCstToAstUtils.createClassElementAst(child)
                if (element) {
                    elements.push(element)
                }
            }
        }
        return elements
    }


    /**
     * ClassElement CST 锟?AST
     * ClassElement -> MethodDefinition | MethodDefinition | FieldDefinition | ...
     */
    createClassElementAst(cst: SubhutiCst): any {
        const firstChild = cst.getChildren()?.[0]
        if (!firstChild) return null

        // 妫€鏌ユ槸鍚︽槸 static
        let staticCst: SubhutiCst | null = null
        let startIndex = 0
        if (firstChild.getName() === 'Static' || firstChild.getValue() === 'static') {
            staticCst = firstChild
            startIndex = 1
        }

        const actualChild = cst.getChildren()?.[startIndex]
        if (!actualChild) return null

        // 鏍规嵁绫诲瀷澶勭悊
        if (actualChild.getName() === SlimeParser.prototype.MethodDefinition?.name ||
            actualChild.getName() === 'MethodDefinition') {
            return SlimeCstToAstUtils.createMethodDefinitionAst(staticCst, actualChild)
        } else if (actualChild.getName() === SlimeParser.prototype.FieldDefinition?.name ||
            actualChild.getName() === 'FieldDefinition') {
            return SlimeCstToAstUtils.createFieldDefinitionAst(staticCst, actualChild)
        } else if (actualChild.getName() === SlimeParser.prototype.ClassStaticBlock?.name ||
            actualChild.getName() === 'ClassStaticBlock') {
            return SlimeCstToAstUtils.createClassStaticBlockAst(actualChild)
        }

        return null
    }


    /**
     * [TypeScript] createFieldDefinitionAst 鏀寔绫诲瀷娉ㄨВ
     * FieldDefinition: ClassElementName TSTypeAnnotation_opt Initializer_opt
     */
    createFieldDefinitionAst(staticCst: SubhutiCst | null, cst: SubhutiCst): SlimePropertyDefinition {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.FieldDefinition?.name);

        // FieldDefinition -> (ClassElementName | PropertyName) + Initializer?
        // ES2022: ClassElementName = PrivateIdentifier | PropertyName
        const elementNameCst = cst.getChildren()[0]
        const key = SlimeCstToAstUtils.createClassElementNameAst(elementNameCst)

        // 妫€鏌ユ槸鍚︽槸璁＄畻灞炴€?
        const isComputed = SlimeCstToAstUtils.isComputedPropertyName(elementNameCst)
        let lBracketToken: any = undefined
        let rBracketToken: any = undefined
        if (isComputed) {
            const stack: SubhutiCst[] = [elementNameCst]
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
        }

        // [TypeScript] 妫€鏌ユ槸鍚︽湁绫诲瀷娉ㄨВ
        let typeAnnotation: any = undefined
        let value: SlimeExpression | null = null
        let equalToken: any = undefined
        // [TypeScript] 妫€鏌ユ槸鍚︽槸鍙€夊睘鎬?
        let isOptional = false

        for (let i = 1; i < cst.getChildren().length; i++) {
            const child = cst.getChildren()[i]
            const childName = child.getName()

            if (childName === 'TSTypeAnnotation') {
                typeAnnotation = SlimeCstToAstUtils.createTSTypeAnnotationAst(child)
            } else if (childName === 'Initializer' ||
                childName === SlimeParser.prototype.Initializer?.name) {
                const assignCst = child.getChildren()?.[0]
                if (assignCst) {
                    equalToken = SlimeTokenCreateUtils.createAssignToken(assignCst.getLoc())
                }
                value = SlimeCstToAstUtils.createInitializerAst(child)
            } else if (childName === 'Question' || child.getValue() === '?') {
                // [TypeScript] 鍙€夊睘鎬ф爣璁?
                isOptional = true
            }
        }

        // 妫€鏌ユ槸鍚︽湁 static 淇グ绗?
        const isStatic = SlimeCstToAstUtils.isStaticModifier(staticCst)

        // 娉ㄦ剰鍙傛暟椤哄簭锛?key, value, computed, isStatic, loc)
        const ast = SlimeAstCreateUtils.createPropertyDefinition(key, value, isComputed, isStatic || false, cst.getLoc())
        if (lBracketToken) {
            (ast as any).lBracketToken = lBracketToken
        }
        if (rBracketToken) {
            (ast as any).rBracketToken = rBracketToken
        }
        if (equalToken) {
            (ast as any).equalToken = equalToken
        }
        if (staticCst) {
            (ast as any).staticToken = SlimeTokenCreateUtils.createStaticToken(staticCst.loc)
        }

        // [TypeScript] 娣诲姞绫诲瀷娉ㄨВ
        if (typeAnnotation) {
            (ast as any).typeAnnotation = typeAnnotation
        }

        // [TypeScript] 娣诲姞鍙€夊睘鎬ф爣璁?
        if (isOptional) {
            (ast as any).optional = true
        }

        return ast
    }


    /**
     * 鍒涘缓 ClassStaticBlock AST (ES2022)
     * ClassStaticBlock: { ClassStaticBlockBody }
     */
    createClassStaticBlockAst(cst: SubhutiCst): any {
        // CST 缁撴瀯: ClassStaticBlock -> [IdentifierName:"static", LBrace, ClassStaticBlockBody, RBrace]
        let staticToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined
        let bodyStatements: SlimeStatement[] = []

        for (const child of cst.getChildren() || []) {
            if (child.getName() === 'Static' || child.getValue() === 'static') {
                staticToken = SlimeTokenCreateUtils.createStaticToken(child.getLoc())
            } else if (child.getName() === 'LBrace' || child.getValue() === '{') {
                lBraceToken = SlimeTokenCreateUtils.createLBraceToken(child.getLoc())
            } else if (child.getName() === 'RBrace' || child.getValue() === '}') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(child.getLoc())
            } else if (child.getName() === 'ClassStaticBlockBody') {
                // ClassStaticBlockBody -> ClassStaticBlockStatementList -> StatementList
                const stmtListCst = child.getChildren()?.find((c: any) =>
                    c.name === 'ClassStaticBlockStatementList' || c.name === 'StatementList'
                )
                if (stmtListCst) {
                    const actualStatementList = stmtListCst.getName() === 'ClassStaticBlockStatementList'
                        ? stmtListCst.getChildren()?.find((c: any) => c.name === 'StatementList')
                        : stmtListCst
                    if (actualStatementList) {
                        bodyStatements = SlimeCstToAstUtils.createStatementListAst(actualStatementList)
                    }
                }
            }
        }

        const block = SlimeAstCreateUtils.createStaticBlock(bodyStatements, cst.getLoc(), lBraceToken, rBraceToken) as any
        if (staticToken) {
            block.staticToken = staticToken
        }
        return block
    }


    /**
     * ClassStaticBlockBody CST 锟?AST
     */
    createClassStaticBlockBodyAst(cst: SubhutiCst): Array<SlimeStatement> {
        const stmtList = cst.getChildren()?.find(ch =>
            ch.name === 'ClassStaticBlockStatementList' ||
            ch.name === SlimeParser.prototype.ClassStaticBlockStatementList?.name
        )
        if (stmtList) {
            return SlimeCstToAstUtils.createClassStaticBlockStatementListAst(stmtList)
        }
        return []
    }


    /**
     * ClassStaticBlockStatementList CST 锟?AST
     */
    createClassStaticBlockStatementListAst(cst: SubhutiCst): Array<SlimeStatement> {
        const stmtList = cst.getChildren()?.find(ch =>
            ch.name === 'StatementList' || ch.name === SlimeParser.prototype.StatementList?.name
        )
        if (stmtList) {
            return SlimeCstToAstUtils.createStatementListAst(stmtList)
        }
        return []
    }


    createClassHeritageAst(cst: SubhutiCst): SlimeExpression {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ClassHeritage?.name);
        return SlimeCstToAstUtils.createLeftHandSideExpressionAst(cst.getChildren()[1]) // ClassHeritage -> extends + LeftHandSideExpression
    }

    createClassHeritageAstWithToken(cst: SubhutiCst): { superClass: SlimeExpression; extendsToken?: any } {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ClassHeritage?.name);
        let extendsToken: any = undefined

        // ClassHeritage: extends LeftHandSideExpression
        const extendsCst = cst.getChildren().find(ch => ch.name === 'Extends' || ch.value === 'extends')
        if (extendsCst) {
            extendsToken = SlimeTokenCreateUtils.createExtendsToken(extendsCst.loc)
        }

        const superClass = SlimeCstToAstUtils.createLeftHandSideExpressionAst(cst.getChildren()[1])
        return { superClass, extendsToken }
    }


    createClassTailAst(cst: SubhutiCst): {
        superClass: SlimeExpression | null;
        body: SlimeClassBody;
        extendsToken?: any;
        lBraceToken?: any;
        rBraceToken?: any;
    } {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ClassTail?.name);
        let superClass: SlimeExpression | null = null // 瓒呯被榛樿锟?null
        let body: SlimeClassBody = { type: SlimeAstTypeName.ClassBody as any, body: [], loc: cst.getLoc() } // 榛樿绌虹被锟?
        let extendsToken: any = undefined
        let lBraceToken: any = undefined
        let rBraceToken: any = undefined

        // ClassTail = ClassHeritage? { ClassBody? }
        // 閬嶅巻 children 鎵惧埌 ClassHeritage 锟?ClassBody
        for (const child of cst.getChildren()) {
            if (child.getName() === SlimeParser.prototype.ClassHeritage?.name) {
                const heritageResult = SlimeCstToAstUtils.createClassHeritageAstWithToken(child)
                superClass = heritageResult.superClass
                extendsToken = heritageResult.extendsToken
            } else if (child.getName() === SlimeParser.prototype.ClassBody?.name) {
                body = SlimeCstToAstUtils.createClassBodyAst(child)
            } else if (child.getName() === 'LBrace' || child.getValue() === '{') {
                lBraceToken = SlimeTokenCreateUtils.createLBraceToken(child.getLoc())
            } else if (child.getName() === 'RBrace' || child.getValue() === '}') {
                rBraceToken = SlimeTokenCreateUtils.createRBraceToken(child.getLoc())
            }
        }

        // 璁剧疆 body 锟?brace tokens
        if (body) {
            body.lBraceToken = lBraceToken
            body.rBraceToken = rBraceToken
        }

        return { superClass, body, extendsToken, lBraceToken, rBraceToken }
    }


    /**
     * ClassElementName CST 锟?AST
     * ClassElementName :: PropertyName | PrivateIdentifier
     */
    createClassElementNameAst(cst: SubhutiCst): SlimeIdentifier | SlimeLiteral | SlimeExpression {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.ClassElementName?.name)
        const first = cst.getChildren()[0]
        if (!first) {
            throw new Error('createClassElementNameAst: ClassElementName has no children')
        }
        if (first.getName() === 'PrivateIdentifier') {
            return SlimeCstToAstUtils.createPrivateIdentifierAst(first)
        }
        // PropertyName
        return SlimeCstToAstUtils.createPropertyNameAst(first)
    }


    /**
     * 妫€锟?CST 鑺傜偣鏄惁琛ㄧず 淇グ锟?
     * 鍏煎 Static 锟?IdentifierNameTok (value='static') 涓ょ鎯呭喌
     */
    isStaticModifier(cst: SubhutiCst | null): boolean {
        if (!cst) return false
        // 鏂瑰紡1锛氱洿鎺ユ槸 Static
        if (cst.getName() === SlimeTokenConsumer.prototype.Static?.name || cst.getName() === 'Static' || cst.getName() === 'Static') {
            return true
        }
        // 鏂瑰紡2锛氭槸 IdentifierNameTok 锟?value 锟?'static'
        if ((cst.getName() === 'IdentifierName' || cst.getName() === 'IdentifierName') && cst.getValue() === 'static') {
            return true
        }
        return false
    }


    /**
     * 妫€锟?ClassElementName/PropertyName 鏄惁鏄绠楀睘鎬у悕
     */
    isComputedPropertyName(cst: SubhutiCst): boolean {
        if (!cst || !cst.getChildren()) return false

        // 閫掑綊鏌ユ壘 ComputedPropertyName
        function hasComputedPropertyName(node: SubhutiCst): boolean {
            if (!node) return false
            if (node.name === 'ComputedPropertyName' || node.name === SlimeParser.prototype.ComputedPropertyName?.name) {
                return true
            }
            if (node.children) {
                for (const child of node.children) {
                    if (hasComputedPropertyName(child)) return true
                }
            }
            return false
        }

        return hasComputedPropertyName(cst)
    }


}

export const SlimeClassDeclarationCstToAst = new SlimeClassDeclarationCstToAstSingle()
