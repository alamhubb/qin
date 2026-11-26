package com.qin.parser;

import com.subhuti.parser.SubhutiDecisionPredicate;
import com.subhuti.parser.SubhutiRule;
import com.subhuti.parser.SubhutiStaticGrammarOnly;
import static com.subhuti.parser.SubhutiCompileOnlyDsl.*;

/** OVS author grammar extending the same Qin/Slime Java grammar. */
public class OvsParser extends CssTsParser {
    public OvsParser(String sourceCode) {
        super(sourceCode);
    }

    @Override
    @SubhutiStaticGrammarOnly
    @SubhutiRule
    public void ConditionalExpression(ExpressionParams params) {
        if (false) {
            BinaryExpression(params);
            Option(() -> {
                tokenConsumer.Question();
                AssignmentExpression(params);
                tokenConsumer.Colon();
                AssignmentExpression(params);
            });
            return;
        }
        super.ConditionalExpression(params);
    }

    @SubhutiStaticGrammarOnly
    @SubhutiRule
    public void BinaryExpression(ExpressionParams params) {
        if (false) {
            UnaryExpression(params);
            Many(gate(this::canStartFlatBinaryOperator, () -> {
                BinaryOperator();
                UnaryExpression(params);
            }));
            return;
        }
        UnaryExpression(params);
    }

    @SubhutiStaticGrammarOnly
    @SubhutiRule
    public void BinaryOperator() {
        if (false) {
            Or(
                () -> tokenConsumer.StrictEqual(),
                () -> tokenConsumer.StrictNotEqual(),
                () -> tokenConsumer.Equal(),
                () -> tokenConsumer.NotEqual(),
                () -> tokenConsumer.LessEqual(),
                () -> tokenConsumer.GreaterEqual(),
                () -> tokenConsumer.NullishCoalescing(),
                () -> tokenConsumer.LogicalOr(),
                () -> tokenConsumer.LogicalAnd(),
                () -> tokenConsumer.Plus(),
                () -> tokenConsumer.Minus(),
                () -> tokenConsumer.Asterisk(),
                () -> tokenConsumer.Slash(),
                () -> tokenConsumer.Modulo(),
                () -> tokenConsumer.Less(),
                () -> tokenConsumer.Greater(),
                () -> tokenConsumer.BitwiseAnd(),
                () -> tokenConsumer.BitwiseOr(),
                () -> tokenConsumer.BitwiseXor(),
                () -> tokenConsumer.Instanceof(),
                () -> tokenConsumer.In(),
                gate(this::canStartFlatOf, () -> tokenConsumer.Of()),
                gate(this::canStartFlatAs, () -> tokenConsumer.As())
            );
            return;
        }
        setParseFail();
    }

    @SubhutiDecisionPredicate
    private boolean canStartFlatBinaryOperator() {
        String token = tokenNameAt(1);
        return "StrictEqual".equals(token) || "StrictNotEqual".equals(token)
            || "Equal".equals(token) || "NotEqual".equals(token)
            || "LessEqual".equals(token) || "GreaterEqual".equals(token)
            || "NullishCoalescing".equals(token) || "LogicalOr".equals(token)
            || "LogicalAnd".equals(token) || "Plus".equals(token)
            || "Minus".equals(token) || "Asterisk".equals(token)
            || "Slash".equals(token) || "Modulo".equals(token)
            || "Less".equals(token) || "Greater".equals(token)
            || "BitwiseAnd".equals(token) || "BitwiseOr".equals(token)
            || "BitwiseXor".equals(token) || "Instanceof".equals(token)
            || "In".equals(token) || matchIdentifierValue("of") || matchIdentifierValue("as");
    }

    @SubhutiDecisionPredicate
    private boolean canStartFlatOf() {
        return matchIdentifierValue("of");
    }

    @SubhutiDecisionPredicate
    private boolean canStartFlatAs() {
        return matchIdentifierValue("as");
    }


    @SubhutiDecisionPredicate
    private boolean canStartOvsRender() {
        if (!"IdentifierName".equals(tokenNameAt(1)) || ovsReservedTag()) return false;
        if ("LBrace".equals(tokenNameAt(2))) return !lineBreakBeforeTokenAt(2);
        if (!"LParen".equals(tokenNameAt(2))) return false;
        int depth = 0;
        for (int offset = 2; ; offset++) {
            String name = tokenNameAt(offset);
            if (name == null || "EOF".equals(name)) return false;
            if ("LParen".equals(name)) depth++;
            if ("RParen".equals(name)) {
                depth--;
                if (depth == 0) {
                    return "LBrace".equals(tokenNameAt(offset + 1)) && !lineBreakBeforeTokenAt(offset + 1);
                }
            }
        }
    }

    @SubhutiDecisionPredicate
    private boolean ovsReservedTag() {
        return matchIdentifierValue("break") || matchIdentifierValue("case") || matchIdentifierValue("catch")
            || matchIdentifierValue("class") || matchIdentifierValue("const") || matchIdentifierValue("continue")
            || matchIdentifierValue("debugger") || matchIdentifierValue("default") || matchIdentifierValue("delete")
            || matchIdentifierValue("do") || matchIdentifierValue("else") || matchIdentifierValue("enum")
            || matchIdentifierValue("export") || matchIdentifierValue("extends") || matchIdentifierValue("false")
            || matchIdentifierValue("finally") || matchIdentifierValue("for") || matchIdentifierValue("function")
            || matchIdentifierValue("if") || matchIdentifierValue("import") || matchIdentifierValue("in")
            || matchIdentifierValue("instanceof") || matchIdentifierValue("new") || matchIdentifierValue("null")
            || matchIdentifierValue("return") || matchIdentifierValue("super") || matchIdentifierValue("switch")
            || matchIdentifierValue("this") || matchIdentifierValue("throw") || matchIdentifierValue("true")
            || matchIdentifierValue("try") || matchIdentifierValue("typeof") || matchIdentifierValue("var")
            || matchIdentifierValue("void") || matchIdentifierValue("while") || matchIdentifierValue("with")
            || matchIdentifierValue("await") || matchIdentifierValue("yield") || matchIdentifierValue("async")
            || matchIdentifierValue("let") || matchIdentifierValue("static") || matchIdentifierValue("as")
            || matchIdentifierValue("of") || matchIdentifierValue("from") || matchIdentifierValue("get")
            || matchIdentifierValue("set") || matchIdentifierValue("view") || matchIdentifierValue("css");
    }

    @Override
    @SubhutiRule
    public void OptionalExpression(ExpressionParams params) {
        Or(
            gate(() -> !params.disableOvsRender() && canStartOvsRender(), () -> {
                MemberExpression(params);
                Many(gate(this::callExpressionSuffixStart, () -> CallExpressionSuffix(params)));
            }),
            () -> super.OptionalExpression(params)
        );
    }

    @Override
    @SubhutiRule
    public void PrimaryExpression(ExpressionParams params) {
        Or(
            gate(() -> !params.disableOvsRender() && canStartOvsRender(), () -> OvsRenderFunction(params)),
            () -> super.PrimaryExpression(params)
        );
    }

    @Override
    @SubhutiRule
    public void Statement(StatementParams params) {
        Or(
            gate(this::canStartOvsRender, () -> OvsRenderStatement(params)),
            gate(() -> "Hash".equals(tokenNameAt(1)), () -> NoRenderBlock(params)),
            () -> super.Statement(params)
        );
    }

    @Override
    @SubhutiRule
    public void StatementListItem(StatementParams params) {
        Or(
            gate(() -> matchIdentifierValue("view") && "IdentifierName".equals(tokenNameAt(2)), this::OvsViewDeclaration),
            () -> super.StatementListItem(params)
        );
    }

    @Override
    @SubhutiRule
    public void Declaration(DeclarationParams params) {
        Or(
            gate(() -> matchIdentifierValue("view") && "IdentifierName".equals(tokenNameAt(2)), this::OvsViewDeclaration),
            () -> super.Declaration(params)
        );
    }

    @Override
    @SubhutiRule
    public void ClassHeritage(DeclarationParams params) {
        tokenConsumer.Extends();
        LeftHandSideExpression(new ExpressionParams(true, params.yield(), params.await(), true));
    }

    @SubhutiRule
    public void OvsRenderFunction(ExpressionParams params) {
        tokenConsumer.IdentifierName();
        Option(() -> OvsArguments(params));
        assertNoLineBreak();
        tokenConsumer.LBrace();
        Option(() -> StatementList(new StatementParams(params.yield(), params.await(), true)));
        tokenConsumer.RBrace();
    }

    @SubhutiRule
    public void OvsRenderStatement(StatementParams params) {
        tokenConsumer.IdentifierName();
        Option(() -> OvsArguments(ExpressionParams.DEFAULT));
        assertNoLineBreak();
        tokenConsumer.LBrace();
        Option(() -> StatementList(params));
        tokenConsumer.RBrace();
    }

    @SubhutiRule
    public void OvsArguments(ExpressionParams params) {
        tokenConsumer.LParen();
        Option(() -> OvsPropertyDefinitionList(params));
        tokenConsumer.RParen();
    }

    @SubhutiRule
    public void OvsPropertyDefinitionList(ExpressionParams params) {
        OvsPropertyDefinition(params);
        Many(gate(() -> "Semicolon".equals(tokenNameAt(1)) && !"RParen".equals(tokenNameAt(2)), () -> {
            tokenConsumer.Semicolon();
            OvsPropertyDefinition(params);
        }));
        Option(() -> tokenConsumer.Semicolon());
    }

    @SubhutiRule
    public void OvsPropertyDefinition(ExpressionParams params) {
        Or(
            () -> { tokenConsumer.Ellipsis(); AssignmentExpression(params); },
            () -> { PropertyName(params); tokenConsumer.Assign(); AssignmentExpression(params); },
            () -> MethodDefinition(params),
            () -> IdentifierReference(params)
        );
    }

    @SubhutiRule
    public void OvsViewDeclaration() {
        consumeIdentifierValue("view");
        tokenConsumer.IdentifierName();
        Option(() -> ArrowFormalParameters(ExpressionParams.DEFAULT));
        tokenConsumer.LBrace();
        Option(() -> StatementList(new StatementParams(false, true, true)));
        tokenConsumer.RBrace();
    }

    @SubhutiRule
    public void NoRenderBlock(StatementParams params) {
        tokenConsumer.Hash();
        tokenConsumer.LBrace();
        Option(() -> StatementList(params));
        tokenConsumer.RBrace();
    }
}
