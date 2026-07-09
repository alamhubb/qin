package com.qin.parser;

import com.slime.parser.SlimeParser;
import com.subhuti.parser.Alternative;
import com.subhuti.parser.SubhutiRule;

/**
 * Qin parser entry built on top of Slime's TypeScript-capable parser layer.
 */
public class QinParser extends SlimeParser {
    public QinParser(String sourceCode) {
        super(sourceCode);
    }

    /**
     * Qin-owned module entry.
     */
    public void QinModule(SourceType sourceType) {
        Program(sourceType);
    }

    /**
     * Qin object declaration:
     *
     * <pre>
     * object Store { value = 1 }
     * </pre>
     *
     * The body intentionally reuses the TypeScript class tail grammar.
     */
    @SubhutiRule public void QinObjectDeclaration(DeclarationParams params) {
        Option(() -> TSDecorators());
        QinObjectDeclarationBody(params);
    }

    @SubhutiRule public void QinObjectDeclarationBody(DeclarationParams params) {
        consumeIdentifierValue("object");
        BindingIdentifier(new ExpressionParams(true, params.yield(), params.await()));
        ClassTail(params);
    }

    @Override
    @SubhutiRule public void Declaration(DeclarationParams params) {
        Or(
                Alternative.tokens("QinObjectDeclaration", this::canStartQinObjectDeclaration, () -> QinObjectDeclaration(params), "IdentifierName", "At"),
                Alternative.rule("StandardDeclaration", this::canStartNonQinDeclaration, () -> super.Declaration(params))
        );
    }

    protected boolean canStartQinObjectDeclaration() {
        return matchIdentifierValue("object") || match("At");
    }

    protected boolean canStartNonQinDeclaration() {
        return canStartStandardDeclaration()
                || canStartTSInterfaceDeclaration()
                || canStartTSTypeAliasDeclaration()
                || canStartTSEnumDeclaration()
                || canStartTSModuleDeclaration()
                || canStartTSDeclareStatement();
    }

    @Override
    @SubhutiRule public void ExportDeclaration() {
        Or(
                () -> {
                    TSDecorators();
                    tokenConsumer.Export();
                    tokenConsumer.Default();
                    QinObjectDeclarationBody(new DeclarationParams(false, true, true));
                },
                () -> {
                    TSDecorators();
                    tokenConsumer.Export();
                    QinObjectDeclarationBody(new DeclarationParams(false, true, false));
                },
                () -> {
                    tokenConsumer.Export();
                    tokenConsumer.Default();
                    QinObjectDeclaration(new DeclarationParams(false, true, true));
                },
                () -> {
                    tokenConsumer.Export();
                    QinObjectDeclaration(new DeclarationParams(false, true, false));
                },
                () -> super.ExportDeclaration()
        );
    }
}
