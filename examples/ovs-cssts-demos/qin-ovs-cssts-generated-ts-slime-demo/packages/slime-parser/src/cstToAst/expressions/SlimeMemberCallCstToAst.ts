/**
 * MemberCallCstToAst - 鎴愬憳璁块棶/璋冪敤琛ㄨ揪寮?鍙€夐摼杞崲
 */
import { SubhutiCst } from "subhuti";
import {
    type SlimeCallArgument,
    SlimeExpression,
    type SlimeIdentifier, SlimeAstTypeName, type SlimePattern, SlimeSpreadElement, type SlimeSuper,
    SlimeTokenCreateUtils,
    type SlimeVariableDeclarator, SlimeAstCreateUtils
} from "slime-ast";

import { com_slime_parser_SlimeParser as SlimeParser } from "../../../com/slime/parser/SlimeParser.ts";
import { SlimeCstToAstUtils } from "../../SlimeCstToAstUtils.ts";
import { SlimeVariableCstToAstSingle } from "../statements/SlimeVariableCstToAst.ts";

export class SlimeMemberCallCstToAstSingle {

    /**
     * ExpressionBody CST 锟?AST
     * ExpressionBody -> AssignmentExpression
     */
    createExpressionBodyAst(cst: SubhutiCst): SlimeExpression {
        const firstChild = cst.getChildren()?.[0]
        if (firstChild) {
            return SlimeCstToAstUtils.createAssignmentExpressionAst(firstChild)
        }
        throw new Error('ExpressionBody has no children')
    }


    createMemberExpressionFirstOr(cst: SubhutiCst): SlimeExpression | SlimeSuper {
        if (cst.getName() === SlimeParser.prototype.PrimaryExpression?.name || cst.getName() === 'PrimaryExpression') {
            return SlimeCstToAstUtils.createPrimaryExpressionAst(cst)
        } else if (cst.getName() === SlimeParser.prototype.SuperProperty?.name || cst.getName() === 'SuperProperty') {
            return SlimeCstToAstUtils.createSuperPropertyAst(cst)
        } else if (cst.getName() === SlimeParser.prototype.MetaProperty?.name || cst.getName() === 'MetaProperty') {
            return SlimeCstToAstUtils.createMetaPropertyAst(cst)
        } else if (cst.getName() === 'NewMemberExpressionArguments') {
            return SlimeCstToAstUtils.createNewExpressionAst(cst)
        } else if (cst.getName() === 'New') {
            // Es2025Parser: new MemberExpression Arguments 鏄洿鎺ョ殑 token 搴忓垪
            // 杩欑鎯呭喌搴旇锟?createMemberExpressionAst 涓锟?
            throw new Error('createMemberExpressionFirstOr: NewTok should be handled in createMemberExpressionAst')
        } else {
            throw new Error('createMemberExpressionFirstOr: 涓嶆敮鎸佺殑绫诲瀷: ' + cst.getName())
        }
    }


    createMemberExpressionAst(cst: SubhutiCst): SlimeExpression {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.MemberExpression?.name);

        if (cst.getChildren().length === 0) {
            throw new Error('MemberExpression has no children')
        }

        // 浠庣涓€涓猚hild鍒涘缓base瀵硅薄
        let current: SlimeExpression
        let startIdx = 1

        // Es2025Parser: 妫€鏌ユ槸鍚︽槸 new MemberExpression ... 妯″紡
        if (cst.getChildren()[0].name === 'New') {
            // new MemberExpression [TSTypeParameterInstantiation] Arguments [鍚庣画鎿嶄綔]
            // TypeScript 娉涘瀷: new Foo<T>()
            const newCst = cst.getChildren()[0]
            const memberExprCst = cst.getChildren()[1]

            const callee = SlimeCstToAstUtils.createMemberExpressionAst(memberExprCst)

            // 鏌ユ壘 Arguments 鍜屽彲閫夌殑 TSTypeParameterInstantiation
            let argsCst: SubhutiCst | undefined
            let typeParamsCst: SubhutiCst | undefined
            let argsIndex = 2

            for (let i = 2; i < cst.getChildren().length; i++) {
                const child = cst.getChildren()[i]
                if (child.getName() === 'TSTypeParameterInstantiation') {
                    typeParamsCst = child
                } else if (child.getName() === SlimeParser.prototype.Arguments?.name || child.getName() === 'Arguments') {
                    argsCst = child
                    argsIndex = i
                    break
                }
            }

            const args = argsCst ? SlimeCstToAstUtils.createArgumentsAstUnified(argsCst) : []

            // 鎻愬彇 tokens
            const newToken = SlimeTokenCreateUtils.createNewToken(newCst.getLoc())
            let lParenToken: any = undefined
            let rParenToken: any = undefined

            if (argsCst && argsCst.children) {
                for (const child of argsCst.children) {
                    if (child.getName() === 'LParen' || child.getValue() === '(') {
                        lParenToken = SlimeTokenCreateUtils.createLParenToken(child.getLoc())
                    } else if (child.getName() === 'RParen' || child.getValue() === ')') {
                        rParenToken = SlimeTokenCreateUtils.createRParenToken(child.getLoc())
                    }
                }
            }

            current = {
                type: 'NewExpression',
                callee: callee,
                arguments: args,
                typeParameters: typeParamsCst ? SlimeCstToAstUtils.createTSTypeParameterInstantiationAst(typeParamsCst) : undefined,
                newToken: newToken,
                lParenToken: lParenToken,
                rParenToken: rParenToken,
                loc: cst.getLoc()
            } as any

            startIdx = argsIndex + 1
        } else {
            current = SlimeCstToAstUtils.createMemberExpressionFirstOr(cst.getChildren()[0]) as SlimeExpression
        }

        // 浣跨敤缁熶竴鐨勫悗缂€澶勭悊鍣ㄥ鐞嗘墍鏈夊悗缂€鎿嶄綔
        return SlimeCstToAstUtils.processPostfixOperations(current, cst.getChildren(), startIdx, cst.getLoc())
    }






    createSuperPropertyAst(cst: SubhutiCst): SlimeExpression {
        // SuperProperty:
        // 褰㈠紡1: SuperTok + Dot + IdentifierName (super.prop)
        // 褰㈠紡2: SuperTok + LBracket + Expression + RBracket (super[expr])
        const superNode: SlimeSuper = SlimeAstCreateUtils.createSuper(cst.getChildren()[0].loc)

        const second = cst.getChildren()[1]

        // 璁＄畻灞炴€ц闂? super[expression]
        if (second.name === 'BracketExpression') {
            const lBracketCst = second.children?.find((c: SubhutiCst) => c.name === 'LBracket' || c.value === '[')
            const rBracketCst = second.children?.find((c: SubhutiCst) => c.name === 'RBracket' || c.value === ']')
            const lBracketToken = lBracketCst ? SlimeTokenCreateUtils.createLBracketToken(lBracketCst.loc) : undefined
            const rBracketToken = rBracketCst ? SlimeTokenCreateUtils.createRBracketToken(rBracketCst.loc) : undefined
            const propertyExpression = SlimeCstToAstUtils.createExpressionAst(second.children[1])
            return SlimeAstCreateUtils.createComputedMemberExpression(
                superNode,
                propertyExpression,
                lBracketToken,
                rBracketToken
            )
        }

        if (second.name === 'LBracket') {
            const lBracketToken = SlimeTokenCreateUtils.createLBracketToken(second.loc)
            const rBracketCst = cst.getChildren()[3]
            const rBracketToken = rBracketCst && (rBracketCst.name === 'RBracket' || rBracketCst.value === ']')
                ? SlimeTokenCreateUtils.createRBracketToken(rBracketCst.loc)
                : undefined
            const expressionCst = cst.getChildren()[2]
            const propertyExpression = SlimeCstToAstUtils.createExpressionAst(expressionCst)
            return SlimeAstCreateUtils.createComputedMemberExpression(
                superNode,
                propertyExpression,
                lBracketToken,
                rBracketToken
            )
        }

        // 鐐硅闂? super.property
        if (second.name === 'Dot') {
            const dotToken = SlimeTokenCreateUtils.createDotToken(second.loc)
            const identifierNameCst = cst.getChildren()[2]
            let property: SlimeIdentifier
            if (identifierNameCst.getName() === 'IdentifierName' || identifierNameCst.getName() === SlimeParser.prototype.IdentifierName?.name) {
                const tokenCst = identifierNameCst.getChildren()[0]
                property = SlimeAstCreateUtils.createIdentifier(tokenCst.getValue(), tokenCst.getLoc())
            } else {
                property = SlimeAstCreateUtils.createIdentifier(identifierNameCst.getValue(), identifierNameCst.getLoc())
            }
            return SlimeAstCreateUtils.createMemberExpression(superNode, dotToken, property)
        }

        // 鏃х増鍏煎: super.property
        const propToken = cst.getChildren()[2]
        const property = SlimeAstCreateUtils.createIdentifier(propToken.value, propToken.loc)
        const dotToken = SlimeTokenCreateUtils.createDotToken(second.loc)
        return SlimeAstCreateUtils.createMemberExpression(superNode, dotToken, property)
    }

    createMetaPropertyAst(cst: SubhutiCst): SlimeExpression {
        // MetaProperty: children[0]鏄疦ewTarget鎴朓mportMeta
        const first = cst.getChildren()[0]
        const children = first?.children || []
        let metaTokenCst: any = undefined
        let dotTokenCst: any = undefined
        let propertyTokenCst: any = undefined

        for (const child of children) {
            if (child.getName() === 'Dot' || child.getValue() === '.') {
                dotTokenCst = child
            } else if (child.getName() === 'Meta' || child.getValue() === 'meta') {
                propertyTokenCst = child
            } else if (child.getName() === 'Target' || child.getValue() === 'target') {
                propertyTokenCst = child
            } else if (child.getName() === 'Import' || child.getValue() === 'import') {
                metaTokenCst = child
            } else if (child.getName() === 'New' || child.getValue() === 'new') {
                metaTokenCst = child
            }
        }

        const metaValue = metaTokenCst?.value || (first.getName() === SlimeParser.prototype.NewTarget?.name ? 'new' : 'import')
        const propertyValue = propertyTokenCst?.value || (first.getName() === SlimeParser.prototype.NewTarget?.name ? 'target' : 'meta')

        return {
            type: 'MetaProperty',
            meta: SlimeAstCreateUtils.createIdentifier(metaValue, metaTokenCst?.loc || first.getLoc()),
            property: SlimeAstCreateUtils.createIdentifier(propertyValue, propertyTokenCst?.loc || first.getLoc()),
            dotToken: dotTokenCst ? SlimeTokenCreateUtils.createDotToken(dotTokenCst.loc) : undefined,
            loc: cst.getLoc()
        } as any
    }



    /**
     * CoverCallExpressionAndAsyncArrowHead CST 杞?AST
     * 杩欐槸涓€涓?cover grammar锛岄€氬父浣滀负 CallExpression 澶勭悊
     */
    createCoverCallExpressionAndAsyncArrowHeadAst(cst: SubhutiCst): SlimeExpression {
        return SlimeCstToAstUtils.createCallExpressionAst(cst)
    }



    createLeftHandSideExpressionAst(cst: SubhutiCst): SlimeExpression {
        const astName = SlimeCstToAstUtils.checkCstName(cst, SlimeParser.prototype.LeftHandSideExpression?.name);
        // 瀹归敊锛歅arser鍦ˋSI鍦烘櫙涓嬪彲鑳界敓鎴愪笉瀹屾暣鐨凜ST锛岃繑鍥炵┖鏍囪瘑锟?
        if (!cst.getChildren() || cst.getChildren().length === 0) {
            return SlimeAstCreateUtils.createIdentifier('', cst.getLoc())
        }
        if (cst.getChildren().length > 1) {

        }
        return SlimeCstToAstUtils.createExpressionAst(cst.getChildren()[0])
    }

}


export const SlimeMemberCallCstToAst = new SlimeMemberCallCstToAstSingle()
