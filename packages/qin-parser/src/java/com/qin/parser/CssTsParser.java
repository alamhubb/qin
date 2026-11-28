package com.qin.parser;

import com.subhuti.parser.SubhutiRule;
import static com.subhuti.parser.SubhutiCompileOnlyDsl.*;

/** Java author grammar compiled to the Java Chevrotain backend. */
public class CssTsParser extends QinParser {
    public CssTsParser(String sourceCode) {
        super(sourceCode);
    }

    @Override
    @SubhutiRule
    public void PrimaryExpression(ExpressionParams params) {
        Or(
            gate(() -> matchIdentifierValue("css") && !lineBreakBeforeTokenAt(2)
                    && "LBrace".equals(tokenNameAt(2)), this::CssExpression),
            () -> super.PrimaryExpression(params)
        );
    }

    @SubhutiRule
    public void CssExpression() {
        consumeIdentifierValue("css");
        CssStyleObject();
    }

    @SubhutiRule
    public void CssStyleObject() {
        tokenConsumer.LBrace();
        Option(this::CssAtomList);
        tokenConsumer.RBrace();
    }

    @SubhutiRule
    public void CssAtomList() {
        CssAtom();
        Many(() -> {
            tokenConsumer.Comma();
            CssAtom();
        });
    }

    @SubhutiRule
    public void CssAtom() {
        Or(
            gate(() -> "Ellipsis".equals(tokenNameAt(1)), () -> {
                tokenConsumer.Ellipsis();
                AssignmentExpression(ExpressionParams.DEFAULT);
            }),
            () -> AssignmentExpression(ExpressionParams.DEFAULT)
        );
    }
}
