package com.qin.parser;

import java.util.Arrays;

/**
 * Static Subhuti rule wrapper for QinParser.
 *
 * <p>Generated from QinParser's visible @SubhutiRule methods so the Qin parser
 * path does not pay runtime ByteBuddy class generation before the first parse.
 */
public final class QinParserStaticEnhanced extends QinParser {
    public QinParserStaticEnhanced(String sourceCode) {
        super(sourceCode);
    }

    public static QinParserStaticEnhanced create(String sourceCode) {
        return new QinParserStaticEnhanced(sourceCode);
    }

    @Override
    public void AdditiveExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AdditiveExpression(arg0);
            return null;
        }, "AdditiveExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ArgumentList(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ArgumentList(arg0);
            return null;
        }, "ArgumentList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void Arguments(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.Arguments(arg0);
            return null;
        }, "Arguments", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ArrayAssignmentPattern(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ArrayAssignmentPattern(arg0);
            return null;
        }, "ArrayAssignmentPattern", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ArrayBindingPattern(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ArrayBindingPattern(arg0);
            return null;
        }, "ArrayBindingPattern", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ArrayLiteral(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ArrayLiteral(arg0);
            return null;
        }, "ArrayLiteral", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ArrowFormalParameters(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ArrowFormalParameters(arg0);
            return null;
        }, "ArrowFormalParameters", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ArrowFunction(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ArrowFunction(arg0);
            return null;
        }, "ArrowFunction", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ArrowParameters(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ArrowParameters(arg0);
            return null;
        }, "ArrowParameters", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AssignmentElement(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AssignmentElement(arg0);
            return null;
        }, "AssignmentElement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AssignmentElementList(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AssignmentElementList(arg0);
            return null;
        }, "AssignmentElementList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AssignmentElisionElement(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AssignmentElisionElement(arg0);
            return null;
        }, "AssignmentElisionElement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AssignmentExpression() {
        executeRuleWrapper(() -> {
            super.AssignmentExpression();
            return null;
        }, "AssignmentExpression", "QinParser", "");
    }

    @Override
    public void AssignmentExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AssignmentExpression(arg0);
            return null;
        }, "AssignmentExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AssignmentOperator() {
        executeRuleWrapper(() -> {
            super.AssignmentOperator();
            return null;
        }, "AssignmentOperator", "QinParser", "");
    }

    @Override
    public void AssignmentPattern(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AssignmentPattern(arg0);
            return null;
        }, "AssignmentPattern", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AssignmentProperty(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AssignmentProperty(arg0);
            return null;
        }, "AssignmentProperty", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AssignmentPropertyList(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AssignmentPropertyList(arg0);
            return null;
        }, "AssignmentPropertyList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AssignmentRestElement(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AssignmentRestElement(arg0);
            return null;
        }, "AssignmentRestElement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AssignmentRestProperty(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AssignmentRestProperty(arg0);
            return null;
        }, "AssignmentRestProperty", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AsyncArrowBindingIdentifier(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AsyncArrowBindingIdentifier(arg0);
            return null;
        }, "AsyncArrowBindingIdentifier", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AsyncArrowFunction(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AsyncArrowFunction(arg0);
            return null;
        }, "AsyncArrowFunction", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AsyncArrowHead() {
        executeRuleWrapper(() -> {
            super.AsyncArrowHead();
            return null;
        }, "AsyncArrowHead", "QinParser", "");
    }

    @Override
    public void AsyncConciseBody(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AsyncConciseBody(arg0);
            return null;
        }, "AsyncConciseBody", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AsyncFunctionBody() {
        executeRuleWrapper(() -> {
            super.AsyncFunctionBody();
            return null;
        }, "AsyncFunctionBody", "QinParser", "");
    }

    @Override
    public void AsyncFunctionDeclaration(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.AsyncFunctionDeclaration(arg0);
            return null;
        }, "AsyncFunctionDeclaration", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AsyncFunctionExpression() {
        executeRuleWrapper(() -> {
            super.AsyncFunctionExpression();
            return null;
        }, "AsyncFunctionExpression", "QinParser", "");
    }

    @Override
    public void AsyncGeneratorBody() {
        executeRuleWrapper(() -> {
            super.AsyncGeneratorBody();
            return null;
        }, "AsyncGeneratorBody", "QinParser", "");
    }

    @Override
    public void AsyncGeneratorDeclaration(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.AsyncGeneratorDeclaration(arg0);
            return null;
        }, "AsyncGeneratorDeclaration", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AsyncGeneratorExpression() {
        executeRuleWrapper(() -> {
            super.AsyncGeneratorExpression();
            return null;
        }, "AsyncGeneratorExpression", "QinParser", "");
    }

    @Override
    public void AsyncGeneratorMethod(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AsyncGeneratorMethod(arg0);
            return null;
        }, "AsyncGeneratorMethod", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AsyncMethod(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AsyncMethod(arg0);
            return null;
        }, "AsyncMethod", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void AttributeKey() {
        executeRuleWrapper(() -> {
            super.AttributeKey();
            return null;
        }, "AttributeKey", "QinParser", "");
    }

    @Override
    public void AwaitExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.AwaitExpression(arg0);
            return null;
        }, "AwaitExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BindingElement(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.BindingElement(arg0);
            return null;
        }, "BindingElement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BindingElementList(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.BindingElementList(arg0);
            return null;
        }, "BindingElementList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BindingElisionElement(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.BindingElisionElement(arg0);
            return null;
        }, "BindingElisionElement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BindingIdentifier() {
        executeRuleWrapper(() -> {
            super.BindingIdentifier();
            return null;
        }, "BindingIdentifier", "QinParser", "");
    }

    @Override
    public void BindingIdentifier(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.BindingIdentifier(arg0);
            return null;
        }, "BindingIdentifier", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BindingList(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.BindingList(arg0);
            return null;
        }, "BindingList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BindingPattern(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.BindingPattern(arg0);
            return null;
        }, "BindingPattern", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BindingProperty(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.BindingProperty(arg0);
            return null;
        }, "BindingProperty", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BindingPropertyList(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.BindingPropertyList(arg0);
            return null;
        }, "BindingPropertyList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BindingRestElement(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.BindingRestElement(arg0);
            return null;
        }, "BindingRestElement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BindingRestProperty(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.BindingRestProperty(arg0);
            return null;
        }, "BindingRestProperty", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BitwiseANDExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.BitwiseANDExpression(arg0);
            return null;
        }, "BitwiseANDExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BitwiseORExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.BitwiseORExpression(arg0);
            return null;
        }, "BitwiseORExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BitwiseXORExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.BitwiseXORExpression(arg0);
            return null;
        }, "BitwiseXORExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void Block(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.Block(arg0);
            return null;
        }, "Block", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BlockStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.BlockStatement(arg0);
            return null;
        }, "BlockStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BooleanLiteral() {
        executeRuleWrapper(() -> {
            super.BooleanLiteral();
            return null;
        }, "BooleanLiteral", "QinParser", "");
    }

    @Override
    public void BreakStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.BreakStatement(arg0);
            return null;
        }, "BreakStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void BreakableStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.BreakableStatement(arg0);
            return null;
        }, "BreakableStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void CallExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.CallExpression(arg0);
            return null;
        }, "CallExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void CallMemberExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.CallMemberExpression(arg0);
            return null;
        }, "CallMemberExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void CaseBlock(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.CaseBlock(arg0);
            return null;
        }, "CaseBlock", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void CaseClause(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.CaseClause(arg0);
            return null;
        }, "CaseClause", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void CaseClauses(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.CaseClauses(arg0);
            return null;
        }, "CaseClauses", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void Catch(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.Catch(arg0);
            return null;
        }, "Catch", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void CatchParameter(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.CatchParameter(arg0);
            return null;
        }, "CatchParameter", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ClassBody(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.ClassBody(arg0);
            return null;
        }, "ClassBody", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ClassDeclaration(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.ClassDeclaration(arg0);
            return null;
        }, "ClassDeclaration", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ClassElement(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.ClassElement(arg0);
            return null;
        }, "ClassElement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ClassElementList(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.ClassElementList(arg0);
            return null;
        }, "ClassElementList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ClassElementName(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ClassElementName(arg0);
            return null;
        }, "ClassElementName", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ClassExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ClassExpression(arg0);
            return null;
        }, "ClassExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ClassHeritage(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.ClassHeritage(arg0);
            return null;
        }, "ClassHeritage", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ClassStaticBlock(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.ClassStaticBlock(arg0);
            return null;
        }, "ClassStaticBlock", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ClassStaticBlockBody() {
        executeRuleWrapper(() -> {
            super.ClassStaticBlockBody();
            return null;
        }, "ClassStaticBlockBody", "QinParser", "");
    }

    @Override
    public void ClassStaticBlockStatementList() {
        executeRuleWrapper(() -> {
            super.ClassStaticBlockStatementList();
            return null;
        }, "ClassStaticBlockStatementList", "QinParser", "");
    }

    @Override
    public void ClassTail(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.ClassTail(arg0);
            return null;
        }, "ClassTail", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void CoalesceExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.CoalesceExpression(arg0);
            return null;
        }, "CoalesceExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void CoalesceExpressionHead(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.CoalesceExpressionHead(arg0);
            return null;
        }, "CoalesceExpressionHead", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void CoalesceExpressionTail(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.CoalesceExpressionTail(arg0);
            return null;
        }, "CoalesceExpressionTail", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ComputedPropertyName(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ComputedPropertyName(arg0);
            return null;
        }, "ComputedPropertyName", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ConciseBody(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ConciseBody(arg0);
            return null;
        }, "ConciseBody", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ConditionalExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ConditionalExpression(arg0);
            return null;
        }, "ConditionalExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ContinueStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.ContinueStatement(arg0);
            return null;
        }, "ContinueStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void CoverCallExpressionAndAsyncArrowHead(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.CoverCallExpressionAndAsyncArrowHead(arg0);
            return null;
        }, "CoverCallExpressionAndAsyncArrowHead", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void CoverInitializedName(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.CoverInitializedName(arg0);
            return null;
        }, "CoverInitializedName", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void CoverParenthesizedExpressionAndArrowParameterList(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.CoverParenthesizedExpressionAndArrowParameterList(arg0);
            return null;
        }, "CoverParenthesizedExpressionAndArrowParameterList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void DebuggerStatement() {
        executeRuleWrapper(() -> {
            super.DebuggerStatement();
            return null;
        }, "DebuggerStatement", "QinParser", "");
    }

    @Override
    public void Declaration(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.Declaration(arg0);
            return null;
        }, "Declaration", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void DefaultClause(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.DefaultClause(arg0);
            return null;
        }, "DefaultClause", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void DestructuringAssignmentTarget(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.DestructuringAssignmentTarget(arg0);
            return null;
        }, "DestructuringAssignmentTarget", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void DoWhileStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.DoWhileStatement(arg0);
            return null;
        }, "DoWhileStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ElementList(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ElementList(arg0);
            return null;
        }, "ElementList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void Elision() {
        executeRuleWrapper(() -> {
            super.Elision();
            return null;
        }, "Elision", "QinParser", "");
    }

    @Override
    public void EmptyStatement() {
        executeRuleWrapper(() -> {
            super.EmptyStatement();
            return null;
        }, "EmptyStatement", "QinParser", "");
    }

    @Override
    public void EqualityExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.EqualityExpression(arg0);
            return null;
        }, "EqualityExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ExponentiationExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ExponentiationExpression(arg0);
            return null;
        }, "ExponentiationExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ExportDeclaration() {
        executeRuleWrapper(() -> {
            super.ExportDeclaration();
            return null;
        }, "ExportDeclaration", "QinParser", "");
    }

    @Override
    public void ExportFromClause() {
        executeRuleWrapper(() -> {
            super.ExportFromClause();
            return null;
        }, "ExportFromClause", "QinParser", "");
    }

    @Override
    public void ExportSpecifier() {
        executeRuleWrapper(() -> {
            super.ExportSpecifier();
            return null;
        }, "ExportSpecifier", "QinParser", "");
    }

    @Override
    public void ExportsList() {
        executeRuleWrapper(() -> {
            super.ExportsList();
            return null;
        }, "ExportsList", "QinParser", "");
    }

    @Override
    public void Expression() {
        executeRuleWrapper(() -> {
            super.Expression();
            return null;
        }, "Expression", "QinParser", "");
    }

    @Override
    public void Expression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.Expression(arg0);
            return null;
        }, "Expression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ExpressionBody(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ExpressionBody(arg0);
            return null;
        }, "ExpressionBody", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ExpressionStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.ExpressionStatement(arg0);
            return null;
        }, "ExpressionStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void FieldDefinition(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.FieldDefinition(arg0);
            return null;
        }, "FieldDefinition", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void Finally(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.Finally(arg0);
            return null;
        }, "Finally", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ForBinding(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.ForBinding(arg0);
            return null;
        }, "ForBinding", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ForDeclaration(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.ForDeclaration(arg0);
            return null;
        }, "ForDeclaration", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ForInOfStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.ForInOfStatement(arg0);
            return null;
        }, "ForInOfStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ForStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.ForStatement(arg0);
            return null;
        }, "ForStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void FormalParameter() {
        executeRuleWrapper(() -> {
            super.FormalParameter();
            return null;
        }, "FormalParameter", "QinParser", "");
    }

    @Override
    public void FormalParameter(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.FormalParameter(arg0);
            return null;
        }, "FormalParameter", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void FormalParameterList() {
        executeRuleWrapper(() -> {
            super.FormalParameterList();
            return null;
        }, "FormalParameterList", "QinParser", "");
    }

    @Override
    public void FormalParameterList(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.FormalParameterList(arg0);
            return null;
        }, "FormalParameterList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void FormalParameters() {
        executeRuleWrapper(() -> {
            super.FormalParameters();
            return null;
        }, "FormalParameters", "QinParser", "");
    }

    @Override
    public void FormalParameters(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.FormalParameters(arg0);
            return null;
        }, "FormalParameters", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void FromClause() {
        executeRuleWrapper(() -> {
            super.FromClause();
            return null;
        }, "FromClause", "QinParser", "");
    }

    @Override
    public void FunctionBody() {
        executeRuleWrapper(() -> {
            super.FunctionBody();
            return null;
        }, "FunctionBody", "QinParser", "");
    }

    @Override
    public void FunctionBody(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.FunctionBody(arg0);
            return null;
        }, "FunctionBody", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void FunctionDeclaration(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.FunctionDeclaration(arg0);
            return null;
        }, "FunctionDeclaration", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void FunctionExpression() {
        executeRuleWrapper(() -> {
            super.FunctionExpression();
            return null;
        }, "FunctionExpression", "QinParser", "");
    }

    @Override
    public void FunctionRestParameter() {
        executeRuleWrapper(() -> {
            super.FunctionRestParameter();
            return null;
        }, "FunctionRestParameter", "QinParser", "");
    }

    @Override
    public void FunctionRestParameter(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.FunctionRestParameter(arg0);
            return null;
        }, "FunctionRestParameter", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void FunctionStatementList(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.FunctionStatementList(arg0);
            return null;
        }, "FunctionStatementList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void GeneratorBody() {
        executeRuleWrapper(() -> {
            super.GeneratorBody();
            return null;
        }, "GeneratorBody", "QinParser", "");
    }

    @Override
    public void GeneratorDeclaration(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.GeneratorDeclaration(arg0);
            return null;
        }, "GeneratorDeclaration", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void GeneratorExpression() {
        executeRuleWrapper(() -> {
            super.GeneratorExpression();
            return null;
        }, "GeneratorExpression", "QinParser", "");
    }

    @Override
    public void GeneratorMethod(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.GeneratorMethod(arg0);
            return null;
        }, "GeneratorMethod", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void HoistableDeclaration(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.HoistableDeclaration(arg0);
            return null;
        }, "HoistableDeclaration", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void Identifier() {
        executeRuleWrapper(() -> {
            super.Identifier();
            return null;
        }, "Identifier", "QinParser", "");
    }

    @Override
    public void IdentifierName() {
        executeRuleWrapper(() -> {
            super.IdentifierName();
            return null;
        }, "IdentifierName", "QinParser", "");
    }

    @Override
    public void IdentifierReference() {
        executeRuleWrapper(() -> {
            super.IdentifierReference();
            return null;
        }, "IdentifierReference", "QinParser", "");
    }

    @Override
    public void IdentifierReference(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.IdentifierReference(arg0);
            return null;
        }, "IdentifierReference", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void IfStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.IfStatement(arg0);
            return null;
        }, "IfStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void IfStatementBody(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.IfStatementBody(arg0);
            return null;
        }, "IfStatementBody", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ImportCall(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ImportCall(arg0);
            return null;
        }, "ImportCall", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ImportClause() {
        executeRuleWrapper(() -> {
            super.ImportClause();
            return null;
        }, "ImportClause", "QinParser", "");
    }

    @Override
    public void ImportDeclaration() {
        executeRuleWrapper(() -> {
            super.ImportDeclaration();
            return null;
        }, "ImportDeclaration", "QinParser", "");
    }

    @Override
    public void ImportMeta() {
        executeRuleWrapper(() -> {
            super.ImportMeta();
            return null;
        }, "ImportMeta", "QinParser", "");
    }

    @Override
    public void ImportSpecifier() {
        executeRuleWrapper(() -> {
            super.ImportSpecifier();
            return null;
        }, "ImportSpecifier", "QinParser", "");
    }

    @Override
    public void ImportedBinding() {
        executeRuleWrapper(() -> {
            super.ImportedBinding();
            return null;
        }, "ImportedBinding", "QinParser", "");
    }

    @Override
    public void ImportedDefaultBinding() {
        executeRuleWrapper(() -> {
            super.ImportedDefaultBinding();
            return null;
        }, "ImportedDefaultBinding", "QinParser", "");
    }

    @Override
    public void ImportsList() {
        executeRuleWrapper(() -> {
            super.ImportsList();
            return null;
        }, "ImportsList", "QinParser", "");
    }

    @Override
    public void IncompleteMemberAccessProperty() {
        executeRuleWrapper(() -> {
            super.IncompleteMemberAccessProperty();
            return null;
        }, "IncompleteMemberAccessProperty", "QinParser", "");
    }

    @Override
    public void Initializer(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.Initializer(arg0);
            return null;
        }, "Initializer", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void IterationStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.IterationStatement(arg0);
            return null;
        }, "IterationStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void LabelIdentifier(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.LabelIdentifier(arg0);
            return null;
        }, "LabelIdentifier", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void LabelledItem(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.LabelledItem(arg0);
            return null;
        }, "LabelledItem", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void LabelledStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.LabelledStatement(arg0);
            return null;
        }, "LabelledStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void LeftHandSideExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.LeftHandSideExpression(arg0);
            return null;
        }, "LeftHandSideExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void LetOrConst() {
        executeRuleWrapper(() -> {
            super.LetOrConst();
            return null;
        }, "LetOrConst", "QinParser", "");
    }

    @Override
    public void LexicalBinding(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.LexicalBinding(arg0);
            return null;
        }, "LexicalBinding", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void LexicalDeclaration(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.LexicalDeclaration(arg0);
            return null;
        }, "LexicalDeclaration", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void Literal() {
        executeRuleWrapper(() -> {
            super.Literal();
            return null;
        }, "Literal", "QinParser", "");
    }

    @Override
    public void LiteralPropertyName() {
        executeRuleWrapper(() -> {
            super.LiteralPropertyName();
            return null;
        }, "LiteralPropertyName", "QinParser", "");
    }

    @Override
    public void LogicalANDExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.LogicalANDExpression(arg0);
            return null;
        }, "LogicalANDExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void LogicalORExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.LogicalORExpression(arg0);
            return null;
        }, "LogicalORExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void LogicalORExpressionTail(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.LogicalORExpressionTail(arg0);
            return null;
        }, "LogicalORExpressionTail", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void MemberExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.MemberExpression(arg0);
            return null;
        }, "MemberExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void MetaProperty() {
        executeRuleWrapper(() -> {
            super.MetaProperty();
            return null;
        }, "MetaProperty", "QinParser", "");
    }

    @Override
    public void MethodDefinition(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.MethodDefinition(arg0);
            return null;
        }, "MethodDefinition", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void Module() {
        executeRuleWrapper(() -> {
            super.Module();
            return null;
        }, "Module", "QinParser", "");
    }

    @Override
    public void ModuleBody() {
        executeRuleWrapper(() -> {
            super.ModuleBody();
            return null;
        }, "ModuleBody", "QinParser", "");
    }

    @Override
    public void ModuleExportName() {
        executeRuleWrapper(() -> {
            super.ModuleExportName();
            return null;
        }, "ModuleExportName", "QinParser", "");
    }

    @Override
    public void ModuleItem() {
        executeRuleWrapper(() -> {
            super.ModuleItem();
            return null;
        }, "ModuleItem", "QinParser", "");
    }

    @Override
    public void ModuleItemList() {
        executeRuleWrapper(() -> {
            super.ModuleItemList();
            return null;
        }, "ModuleItemList", "QinParser", "");
    }

    @Override
    public void ModuleSpecifier() {
        executeRuleWrapper(() -> {
            super.ModuleSpecifier();
            return null;
        }, "ModuleSpecifier", "QinParser", "");
    }

    @Override
    public void MultiplicativeExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.MultiplicativeExpression(arg0);
            return null;
        }, "MultiplicativeExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void MultiplicativeOperator() {
        executeRuleWrapper(() -> {
            super.MultiplicativeOperator();
            return null;
        }, "MultiplicativeOperator", "QinParser", "");
    }

    @Override
    public void NameSpaceImport() {
        executeRuleWrapper(() -> {
            super.NameSpaceImport();
            return null;
        }, "NameSpaceImport", "QinParser", "");
    }

    @Override
    public void NamedExports() {
        executeRuleWrapper(() -> {
            super.NamedExports();
            return null;
        }, "NamedExports", "QinParser", "");
    }

    @Override
    public void NamedImports() {
        executeRuleWrapper(() -> {
            super.NamedImports();
            return null;
        }, "NamedImports", "QinParser", "");
    }

    @Override
    public void NewExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.NewExpression(arg0);
            return null;
        }, "NewExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void NewTarget() {
        executeRuleWrapper(() -> {
            super.NewTarget();
            return null;
        }, "NewTarget", "QinParser", "");
    }

    @Override
    public void NoSubstitutionTemplate() {
        executeRuleWrapper(() -> {
            super.NoSubstitutionTemplate();
            return null;
        }, "NoSubstitutionTemplate", "QinParser", "");
    }

    @Override
    public void NumericLiteral() {
        executeRuleWrapper(() -> {
            super.NumericLiteral();
            return null;
        }, "NumericLiteral", "QinParser", "");
    }

    @Override
    public void ObjectAssignmentPattern(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ObjectAssignmentPattern(arg0);
            return null;
        }, "ObjectAssignmentPattern", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ObjectBindingPattern(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ObjectBindingPattern(arg0);
            return null;
        }, "ObjectBindingPattern", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ObjectLiteral(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ObjectLiteral(arg0);
            return null;
        }, "ObjectLiteral", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void OptionalChain(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.OptionalChain(arg0);
            return null;
        }, "OptionalChain", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void OptionalExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.OptionalExpression(arg0);
            return null;
        }, "OptionalExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ParenthesizedExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ParenthesizedExpression(arg0);
            return null;
        }, "ParenthesizedExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void PrimaryExpression() {
        executeRuleWrapper(() -> {
            super.PrimaryExpression();
            return null;
        }, "PrimaryExpression", "QinParser", "");
    }

    @Override
    public void PrimaryExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.PrimaryExpression(arg0);
            return null;
        }, "PrimaryExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void PrivateIdentifier() {
        executeRuleWrapper(() -> {
            super.PrivateIdentifier();
            return null;
        }, "PrivateIdentifier", "QinParser", "");
    }

    @Override
    public com.subhuti.struct.SubhutiCst Program() {
        return executeRuleWrapper(() -> super.Program(), "Program", "QinParser", "");
    }

    @Override
    public com.subhuti.struct.SubhutiCst Program(com.slime.parser.SlimeJavascriptParser.SourceType arg0) {
        return executeRuleWrapper(() -> super.Program(arg0), "Program", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void PropertyDefinition(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.PropertyDefinition(arg0);
            return null;
        }, "PropertyDefinition", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void PropertyDefinitionList(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.PropertyDefinitionList(arg0);
            return null;
        }, "PropertyDefinitionList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void PropertyName(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.PropertyName(arg0);
            return null;
        }, "PropertyName", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void PropertySetParameterList() {
        executeRuleWrapper(() -> {
            super.PropertySetParameterList();
            return null;
        }, "PropertySetParameterList", "QinParser", "");
    }

    @Override
    public void QinObjectDeclaration(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.QinObjectDeclaration(arg0);
            return null;
        }, "QinObjectDeclaration", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void QinObjectDeclarationBody(com.slime.parser.base.SlimeJavascriptParserBase.DeclarationParams arg0) {
        executeRuleWrapper(() -> {
            super.QinObjectDeclarationBody(arg0);
            return null;
        }, "QinObjectDeclarationBody", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void RegularExpressionLiteral() {
        executeRuleWrapper(() -> {
            super.RegularExpressionLiteral();
            return null;
        }, "RegularExpressionLiteral", "QinParser", "");
    }

    @Override
    public void RelationalExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.RelationalExpression(arg0);
            return null;
        }, "RelationalExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ReturnStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.ReturnStatement(arg0);
            return null;
        }, "ReturnStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void Script() {
        executeRuleWrapper(() -> {
            super.Script();
            return null;
        }, "Script", "QinParser", "");
    }

    @Override
    public void ScriptBody() {
        executeRuleWrapper(() -> {
            super.ScriptBody();
            return null;
        }, "ScriptBody", "QinParser", "");
    }

    @Override
    public void SemicolonASI() {
        executeRuleWrapper(() -> {
            super.SemicolonASI();
            return null;
        }, "SemicolonASI", "QinParser", "");
    }

    @Override
    public void ShiftExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ShiftExpression(arg0);
            return null;
        }, "ShiftExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ShortCircuitExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ShortCircuitExpression(arg0);
            return null;
        }, "ShortCircuitExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ShortCircuitExpressionTail(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.ShortCircuitExpressionTail(arg0);
            return null;
        }, "ShortCircuitExpressionTail", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void SingleNameBinding(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.SingleNameBinding(arg0);
            return null;
        }, "SingleNameBinding", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void SpreadElement(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.SpreadElement(arg0);
            return null;
        }, "SpreadElement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void Statement() {
        executeRuleWrapper(() -> {
            super.Statement();
            return null;
        }, "Statement", "QinParser", "");
    }

    @Override
    public void Statement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.Statement(arg0);
            return null;
        }, "Statement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void StatementList(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.StatementList(arg0);
            return null;
        }, "StatementList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void StatementListItem(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.StatementListItem(arg0);
            return null;
        }, "StatementListItem", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void StringLiteral() {
        executeRuleWrapper(() -> {
            super.StringLiteral();
            return null;
        }, "StringLiteral", "QinParser", "");
    }

    @Override
    public void SubstitutionTemplate(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.SubstitutionTemplate(arg0);
            return null;
        }, "SubstitutionTemplate", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void SuperCall(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.SuperCall(arg0);
            return null;
        }, "SuperCall", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void SuperProperty(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.SuperProperty(arg0);
            return null;
        }, "SuperProperty", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void SwitchStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.SwitchStatement(arg0);
            return null;
        }, "SwitchStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void TSAbstractModifier() {
        executeRuleWrapper(() -> {
            super.TSAbstractModifier();
            return null;
        }, "TSAbstractModifier", "QinParser", "");
    }

    @Override
    public void TSAccessibilityModifier() {
        executeRuleWrapper(() -> {
            super.TSAccessibilityModifier();
            return null;
        }, "TSAccessibilityModifier", "QinParser", "");
    }

    @Override
    public void TSAnyKeyword() {
        executeRuleWrapper(() -> {
            super.TSAnyKeyword();
            return null;
        }, "TSAnyKeyword", "QinParser", "");
    }

    @Override
    public void TSArrayType() {
        executeRuleWrapper(() -> {
            super.TSArrayType();
            return null;
        }, "TSArrayType", "QinParser", "");
    }

    @Override
    public void TSAsExpressionTail() {
        executeRuleWrapper(() -> {
            super.TSAsExpressionTail();
            return null;
        }, "TSAsExpressionTail", "QinParser", "");
    }

    @Override
    public void TSBigIntKeyword() {
        executeRuleWrapper(() -> {
            super.TSBigIntKeyword();
            return null;
        }, "TSBigIntKeyword", "QinParser", "");
    }

    @Override
    public void TSBooleanKeyword() {
        executeRuleWrapper(() -> {
            super.TSBooleanKeyword();
            return null;
        }, "TSBooleanKeyword", "QinParser", "");
    }

    @Override
    public void TSCallSignatureDeclaration() {
        executeRuleWrapper(() -> {
            super.TSCallSignatureDeclaration();
            return null;
        }, "TSCallSignatureDeclaration", "QinParser", "");
    }

    @Override
    public void TSClassAbstractPropertySignature(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.TSClassAbstractPropertySignature(arg0);
            return null;
        }, "TSClassAbstractPropertySignature", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void TSClassImplements() {
        executeRuleWrapper(() -> {
            super.TSClassImplements();
            return null;
        }, "TSClassImplements", "QinParser", "");
    }

    @Override
    public void TSClassMethodSignature(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.TSClassMethodSignature(arg0);
            return null;
        }, "TSClassMethodSignature", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void TSConditionalType() {
        executeRuleWrapper(() -> {
            super.TSConditionalType();
            return null;
        }, "TSConditionalType", "QinParser", "");
    }

    @Override
    public void TSConstructSignatureDeclaration() {
        executeRuleWrapper(() -> {
            super.TSConstructSignatureDeclaration();
            return null;
        }, "TSConstructSignatureDeclaration", "QinParser", "");
    }

    @Override
    public void TSConstructorType() {
        executeRuleWrapper(() -> {
            super.TSConstructorType();
            return null;
        }, "TSConstructorType", "QinParser", "");
    }

    @Override
    public void TSDeclareStatement() {
        executeRuleWrapper(() -> {
            super.TSDeclareStatement();
            return null;
        }, "TSDeclareStatement", "QinParser", "");
    }

    @Override
    public void TSDecorator() {
        executeRuleWrapper(() -> {
            super.TSDecorator();
            return null;
        }, "TSDecorator", "QinParser", "");
    }

    @Override
    public void TSDecorators() {
        executeRuleWrapper(() -> {
            super.TSDecorators();
            return null;
        }, "TSDecorators", "QinParser", "");
    }

    @Override
    public void TSEnumBody() {
        executeRuleWrapper(() -> {
            super.TSEnumBody();
            return null;
        }, "TSEnumBody", "QinParser", "");
    }

    @Override
    public void TSEnumDeclaration() {
        executeRuleWrapper(() -> {
            super.TSEnumDeclaration();
            return null;
        }, "TSEnumDeclaration", "QinParser", "");
    }

    @Override
    public void TSEnumMember() {
        executeRuleWrapper(() -> {
            super.TSEnumMember();
            return null;
        }, "TSEnumMember", "QinParser", "");
    }

    @Override
    public void TSEnumMemberInitializer() {
        executeRuleWrapper(() -> {
            super.TSEnumMemberInitializer();
            return null;
        }, "TSEnumMemberInitializer", "QinParser", "");
    }

    @Override
    public void TSEnumMemberList() {
        executeRuleWrapper(() -> {
            super.TSEnumMemberList();
            return null;
        }, "TSEnumMemberList", "QinParser", "");
    }

    @Override
    public void TSExportAssignment() {
        executeRuleWrapper(() -> {
            super.TSExportAssignment();
            return null;
        }, "TSExportAssignment", "QinParser", "");
    }

    @Override
    public void TSExpressionWithTypeArguments() {
        executeRuleWrapper(() -> {
            super.TSExpressionWithTypeArguments();
            return null;
        }, "TSExpressionWithTypeArguments", "QinParser", "");
    }

    @Override
    public void TSFunctionType() {
        executeRuleWrapper(() -> {
            super.TSFunctionType();
            return null;
        }, "TSFunctionType", "QinParser", "");
    }

    @Override
    public void TSImplementsClause() {
        executeRuleWrapper(() -> {
            super.TSImplementsClause();
            return null;
        }, "TSImplementsClause", "QinParser", "");
    }

    @Override
    public void TSImportType() {
        executeRuleWrapper(() -> {
            super.TSImportType();
            return null;
        }, "TSImportType", "QinParser", "");
    }

    @Override
    public void TSIndexSignature() {
        executeRuleWrapper(() -> {
            super.TSIndexSignature();
            return null;
        }, "TSIndexSignature", "QinParser", "");
    }

    @Override
    public void TSIndexedAccessType() {
        executeRuleWrapper(() -> {
            super.TSIndexedAccessType();
            return null;
        }, "TSIndexedAccessType", "QinParser", "");
    }

    @Override
    public void TSInferType() {
        executeRuleWrapper(() -> {
            super.TSInferType();
            return null;
        }, "TSInferType", "QinParser", "");
    }

    @Override
    public void TSInterfaceBody() {
        executeRuleWrapper(() -> {
            super.TSInterfaceBody();
            return null;
        }, "TSInterfaceBody", "QinParser", "");
    }

    @Override
    public void TSInterfaceDeclaration() {
        executeRuleWrapper(() -> {
            super.TSInterfaceDeclaration();
            return null;
        }, "TSInterfaceDeclaration", "QinParser", "");
    }

    @Override
    public void TSInterfaceExtends() {
        executeRuleWrapper(() -> {
            super.TSInterfaceExtends();
            return null;
        }, "TSInterfaceExtends", "QinParser", "");
    }

    @Override
    public void TSIntersectionType() {
        executeRuleWrapper(() -> {
            super.TSIntersectionType();
            return null;
        }, "TSIntersectionType", "QinParser", "");
    }

    @Override
    public void TSKeywordType() {
        executeRuleWrapper(() -> {
            super.TSKeywordType();
            return null;
        }, "TSKeywordType", "QinParser", "");
    }

    @Override
    public void TSLiteralType() {
        executeRuleWrapper(() -> {
            super.TSLiteralType();
            return null;
        }, "TSLiteralType", "QinParser", "");
    }

    @Override
    public void TSMappedType() {
        executeRuleWrapper(() -> {
            super.TSMappedType();
            return null;
        }, "TSMappedType", "QinParser", "");
    }

    @Override
    public void TSMethodSignature() {
        executeRuleWrapper(() -> {
            super.TSMethodSignature();
            return null;
        }, "TSMethodSignature", "QinParser", "");
    }

    @Override
    public void TSModuleBlock() {
        executeRuleWrapper(() -> {
            super.TSModuleBlock();
            return null;
        }, "TSModuleBlock", "QinParser", "");
    }

    @Override
    public void TSModuleDeclaration() {
        executeRuleWrapper(() -> {
            super.TSModuleDeclaration();
            return null;
        }, "TSModuleDeclaration", "QinParser", "");
    }

    @Override
    public void TSModuleIdentifier() {
        executeRuleWrapper(() -> {
            super.TSModuleIdentifier();
            return null;
        }, "TSModuleIdentifier", "QinParser", "");
    }

    @Override
    public void TSModuleName() {
        executeRuleWrapper(() -> {
            super.TSModuleName();
            return null;
        }, "TSModuleName", "QinParser", "");
    }

    @Override
    public void TSNamedTupleMember() {
        executeRuleWrapper(() -> {
            super.TSNamedTupleMember();
            return null;
        }, "TSNamedTupleMember", "QinParser", "");
    }

    @Override
    public void TSNeverKeyword() {
        executeRuleWrapper(() -> {
            super.TSNeverKeyword();
            return null;
        }, "TSNeverKeyword", "QinParser", "");
    }

    @Override
    public void TSNonArrayType() {
        executeRuleWrapper(() -> {
            super.TSNonArrayType();
            return null;
        }, "TSNonArrayType", "QinParser", "");
    }

    @Override
    public void TSNonNullExpressionTail() {
        executeRuleWrapper(() -> {
            super.TSNonNullExpressionTail();
            return null;
        }, "TSNonNullExpressionTail", "QinParser", "");
    }

    @Override
    public void TSNullKeyword() {
        executeRuleWrapper(() -> {
            super.TSNullKeyword();
            return null;
        }, "TSNullKeyword", "QinParser", "");
    }

    @Override
    public void TSNumberKeyword() {
        executeRuleWrapper(() -> {
            super.TSNumberKeyword();
            return null;
        }, "TSNumberKeyword", "QinParser", "");
    }

    @Override
    public void TSObjectKeyword() {
        executeRuleWrapper(() -> {
            super.TSObjectKeyword();
            return null;
        }, "TSObjectKeyword", "QinParser", "");
    }

    @Override
    public void TSParameter() {
        executeRuleWrapper(() -> {
            super.TSParameter();
            return null;
        }, "TSParameter", "QinParser", "");
    }

    @Override
    public void TSParameterList() {
        executeRuleWrapper(() -> {
            super.TSParameterList();
            return null;
        }, "TSParameterList", "QinParser", "");
    }

    @Override
    public void TSParameterProperty() {
        executeRuleWrapper(() -> {
            super.TSParameterProperty();
            return null;
        }, "TSParameterProperty", "QinParser", "");
    }

    @Override
    public void TSParenthesizedType() {
        executeRuleWrapper(() -> {
            super.TSParenthesizedType();
            return null;
        }, "TSParenthesizedType", "QinParser", "");
    }

    @Override
    public void TSPrefixTypeOrPrimary() {
        executeRuleWrapper(() -> {
            super.TSPrefixTypeOrPrimary();
            return null;
        }, "TSPrefixTypeOrPrimary", "QinParser", "");
    }

    @Override
    public void TSPrimaryType() {
        executeRuleWrapper(() -> {
            super.TSPrimaryType();
            return null;
        }, "TSPrimaryType", "QinParser", "");
    }

    @Override
    public void TSPropertyOrMethodSignature() {
        executeRuleWrapper(() -> {
            super.TSPropertyOrMethodSignature();
            return null;
        }, "TSPropertyOrMethodSignature", "QinParser", "");
    }

    @Override
    public void TSPropertySignature() {
        executeRuleWrapper(() -> {
            super.TSPropertySignature();
            return null;
        }, "TSPropertySignature", "QinParser", "");
    }

    @Override
    public void TSRestType() {
        executeRuleWrapper(() -> {
            super.TSRestType();
            return null;
        }, "TSRestType", "QinParser", "");
    }

    @Override
    public void TSSatisfiesExpressionTail() {
        executeRuleWrapper(() -> {
            super.TSSatisfiesExpressionTail();
            return null;
        }, "TSSatisfiesExpressionTail", "QinParser", "");
    }

    @Override
    public void TSStringKeyword() {
        executeRuleWrapper(() -> {
            super.TSStringKeyword();
            return null;
        }, "TSStringKeyword", "QinParser", "");
    }

    @Override
    public void TSSymbolKeyword() {
        executeRuleWrapper(() -> {
            super.TSSymbolKeyword();
            return null;
        }, "TSSymbolKeyword", "QinParser", "");
    }

    @Override
    public void TSTemplateLiteralType() {
        executeRuleWrapper(() -> {
            super.TSTemplateLiteralType();
            return null;
        }, "TSTemplateLiteralType", "QinParser", "");
    }

    @Override
    public void TSThisParameter() {
        executeRuleWrapper(() -> {
            super.TSThisParameter();
            return null;
        }, "TSThisParameter", "QinParser", "");
    }

    @Override
    public void TSThisType() {
        executeRuleWrapper(() -> {
            super.TSThisType();
            return null;
        }, "TSThisType", "QinParser", "");
    }

    @Override
    public void TSTupleElementType() {
        executeRuleWrapper(() -> {
            super.TSTupleElementType();
            return null;
        }, "TSTupleElementType", "QinParser", "");
    }

    @Override
    public void TSTupleType() {
        executeRuleWrapper(() -> {
            super.TSTupleType();
            return null;
        }, "TSTupleType", "QinParser", "");
    }

    @Override
    public void TSType() {
        executeRuleWrapper(() -> {
            super.TSType();
            return null;
        }, "TSType", "QinParser", "");
    }

    @Override
    public void TSTypeAliasDeclaration() {
        executeRuleWrapper(() -> {
            super.TSTypeAliasDeclaration();
            return null;
        }, "TSTypeAliasDeclaration", "QinParser", "");
    }

    @Override
    public void TSTypeAnnotation() {
        executeRuleWrapper(() -> {
            super.TSTypeAnnotation();
            return null;
        }, "TSTypeAnnotation", "QinParser", "");
    }

    @Override
    public void TSTypeAssertion(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.TSTypeAssertion(arg0);
            return null;
        }, "TSTypeAssertion", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void TSTypeLiteral() {
        executeRuleWrapper(() -> {
            super.TSTypeLiteral();
            return null;
        }, "TSTypeLiteral", "QinParser", "");
    }

    @Override
    public void TSTypeMember() {
        executeRuleWrapper(() -> {
            super.TSTypeMember();
            return null;
        }, "TSTypeMember", "QinParser", "");
    }

    @Override
    public void TSTypeMembers() {
        executeRuleWrapper(() -> {
            super.TSTypeMembers();
            return null;
        }, "TSTypeMembers", "QinParser", "");
    }

    @Override
    public void TSTypeName() {
        executeRuleWrapper(() -> {
            super.TSTypeName();
            return null;
        }, "TSTypeName", "QinParser", "");
    }

    @Override
    public void TSTypeOperand() {
        executeRuleWrapper(() -> {
            super.TSTypeOperand();
            return null;
        }, "TSTypeOperand", "QinParser", "");
    }

    @Override
    public void TSTypeOperator() {
        executeRuleWrapper(() -> {
            super.TSTypeOperator();
            return null;
        }, "TSTypeOperator", "QinParser", "");
    }

    @Override
    public void TSTypeParameter() {
        executeRuleWrapper(() -> {
            super.TSTypeParameter();
            return null;
        }, "TSTypeParameter", "QinParser", "");
    }

    @Override
    public void TSTypeParameterDeclaration() {
        executeRuleWrapper(() -> {
            super.TSTypeParameterDeclaration();
            return null;
        }, "TSTypeParameterDeclaration", "QinParser", "");
    }

    @Override
    public void TSTypeParameterInstantiation() {
        executeRuleWrapper(() -> {
            super.TSTypeParameterInstantiation();
            return null;
        }, "TSTypeParameterInstantiation", "QinParser", "");
    }

    @Override
    public void TSTypePredicate() {
        executeRuleWrapper(() -> {
            super.TSTypePredicate();
            return null;
        }, "TSTypePredicate", "QinParser", "");
    }

    @Override
    public void TSTypeQuery() {
        executeRuleWrapper(() -> {
            super.TSTypeQuery();
            return null;
        }, "TSTypeQuery", "QinParser", "");
    }

    @Override
    public void TSTypeReference() {
        executeRuleWrapper(() -> {
            super.TSTypeReference();
            return null;
        }, "TSTypeReference", "QinParser", "");
    }

    @Override
    public void TSUndefinedKeyword() {
        executeRuleWrapper(() -> {
            super.TSUndefinedKeyword();
            return null;
        }, "TSUndefinedKeyword", "QinParser", "");
    }

    @Override
    public void TSUnionOrIntersectionType() {
        executeRuleWrapper(() -> {
            super.TSUnionOrIntersectionType();
            return null;
        }, "TSUnionOrIntersectionType", "QinParser", "");
    }

    @Override
    public void TSUnknownKeyword() {
        executeRuleWrapper(() -> {
            super.TSUnknownKeyword();
            return null;
        }, "TSUnknownKeyword", "QinParser", "");
    }

    @Override
    public void TSVoidKeyword() {
        executeRuleWrapper(() -> {
            super.TSVoidKeyword();
            return null;
        }, "TSVoidKeyword", "QinParser", "");
    }

    @Override
    public void TemplateLiteral() {
        executeRuleWrapper(() -> {
            super.TemplateLiteral();
            return null;
        }, "TemplateLiteral", "QinParser", "");
    }

    @Override
    public void TemplateLiteral(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.TemplateLiteral(arg0);
            return null;
        }, "TemplateLiteral", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void TemplateLiteral(com.slime.parser.base.SlimeJavascriptParserBase.TemplateLiteralParams arg0) {
        executeRuleWrapper(() -> {
            super.TemplateLiteral(arg0);
            return null;
        }, "TemplateLiteral", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void TemplateMiddleList(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.TemplateMiddleList(arg0);
            return null;
        }, "TemplateMiddleList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void TemplateSpans(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.TemplateSpans(arg0);
            return null;
        }, "TemplateSpans", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void ThrowStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.ThrowStatement(arg0);
            return null;
        }, "ThrowStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void TryStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.TryStatement(arg0);
            return null;
        }, "TryStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void TypeOnlyExportSpecifier() {
        executeRuleWrapper(() -> {
            super.TypeOnlyExportSpecifier();
            return null;
        }, "TypeOnlyExportSpecifier", "QinParser", "");
    }

    @Override
    public void UnaryExpression() {
        executeRuleWrapper(() -> {
            super.UnaryExpression();
            return null;
        }, "UnaryExpression", "QinParser", "");
    }

    @Override
    public void UnaryExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.UnaryExpression(arg0);
            return null;
        }, "UnaryExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void UnaryOperator() {
        executeRuleWrapper(() -> {
            super.UnaryOperator();
            return null;
        }, "UnaryOperator", "QinParser", "");
    }

    @Override
    public void UniqueFormalParameters() {
        executeRuleWrapper(() -> {
            super.UniqueFormalParameters();
            return null;
        }, "UniqueFormalParameters", "QinParser", "");
    }

    @Override
    public void UniqueFormalParameters(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.UniqueFormalParameters(arg0);
            return null;
        }, "UniqueFormalParameters", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void UpdateExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.UpdateExpression(arg0);
            return null;
        }, "UpdateExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void VariableDeclaration(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.VariableDeclaration(arg0);
            return null;
        }, "VariableDeclaration", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void VariableDeclarationList(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.VariableDeclarationList(arg0);
            return null;
        }, "VariableDeclarationList", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void VariableStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.VariableStatement(arg0);
            return null;
        }, "VariableStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void WhileStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.WhileStatement(arg0);
            return null;
        }, "WhileStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void WithClause() {
        executeRuleWrapper(() -> {
            super.WithClause();
            return null;
        }, "WithClause", "QinParser", "");
    }

    @Override
    public void WithEntries() {
        executeRuleWrapper(() -> {
            super.WithEntries();
            return null;
        }, "WithEntries", "QinParser", "");
    }

    @Override
    public void WithStatement(com.slime.parser.base.SlimeJavascriptParserBase.StatementParams arg0) {
        executeRuleWrapper(() -> {
            super.WithStatement(arg0);
            return null;
        }, "WithStatement", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public void YieldExpression(com.slime.parser.base.SlimeJavascriptParserBase.ExpressionParams arg0) {
        executeRuleWrapper(() -> {
            super.YieldExpression(arg0);
            return null;
        }, "YieldExpression", "QinParser", cacheKeyExtra(new Object[] {arg0}));
    }

    @Override
    public com.subhuti.struct.SubhutiCst parse() {
        return executeRuleWrapper(() -> super.parse(), "parse", "QinParser", "");
    }

    private static Object cacheKeyExtra(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        if (args.length == 1) {
            return args[0];
        }
        return Arrays.asList(args);
    }
}
