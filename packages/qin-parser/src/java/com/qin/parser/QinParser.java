package com.qin.parser;

import com.slime.parser.SlimeParser;
import com.subhuti.parser.SubhutiRule;
import com.subhuti.parser.SubhutiStaticGrammarSource;

import static com.subhuti.parser.SubhutiCompileOnlyDsl.Option;
import static com.subhuti.parser.SubhutiCompileOnlyDsl.Or;
import static com.subhuti.parser.SubhutiCompileOnlyDsl.gate;

/**
 * Qin parser entry built on top of Slime's TypeScript-capable parser layer.
 */
@SubhutiStaticGrammarSource
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
        if (false) {
            Option(() -> TSDecorators());
            QinObjectDeclarationBody(params);
            return;
        }
        OptionalTSDecorators();
        QinObjectDeclarationBody(params);
    }

    @SubhutiRule public void QinObjectDeclarationBody(DeclarationParams params) {
        consumeIdentifierValue("object");
        QinObjectName();
        ClassTail(params);
    }

    @SubhutiRule public void QinObjectName() {
        if (false) {
            IdentifierName();
            return;
        }
        IdentifierName();
    }

    @Override
    @SubhutiRule public void Declaration(DeclarationParams params) {
        if (false) {
            Or(
                    gate(this::canStartQinObjectDeclaration, () -> QinObjectDeclaration(params)),
                    gate(this::canStartTSInterfaceDeclaration, () -> TSInterfaceDeclaration()),
                    gate(this::canStartTSTypeAliasDeclaration, () -> TSTypeAliasDeclaration()),
                    gate(this::canStartTSEnumDeclaration, () -> TSEnumDeclaration()),
                    gate(this::canStartTSModuleDeclaration, () -> TSModuleDeclaration()),
                    gate(this::canStartTSDeclareStatement, () -> TSDeclareStatement()),
                    gate(this::declarationStandardStart, () -> StandardDeclaration(params))
            );
            return;
        }
        if (canStartQinObjectDeclaration()) {
            QinObjectDeclaration(params);
            return;
        }
        if (canStartTSInterfaceDeclaration()) {
            TSInterfaceDeclaration();
            return;
        }
        if (canStartTSTypeAliasDeclaration()) {
            TSTypeAliasDeclaration();
            return;
        }
        if (canStartTSEnumDeclaration()) {
            TSEnumDeclaration();
            return;
        }
        if (canStartTSModuleDeclaration()) {
            TSModuleDeclaration();
            return;
        }
        if (canStartTSDeclareStatement()) {
            TSDeclareStatement();
            return;
        }
        if (declarationStandardStart()) {
            StandardDeclaration(params);
            return;
        }
        setParseFail();
    }

    @Override
    @SubhutiRule public void StatementListItem(StatementParams params) {
        if (false) {
            Or(
                    gate(this::canStartQinObjectDeclaration, () ->
                            Declaration(new DeclarationParams(params.yield(), params.await(), false))),
                    () -> super.StatementListItem(params)
            );
            return;
        }
        if (canStartQinObjectDeclaration()) {
            Declaration(new DeclarationParams(params.yield(), params.await(), false));
            return;
        }
        super.StatementListItem(params);
    }

    protected boolean canStartQinObjectDeclaration() {
        return canStartQinObjectDeclarationAt(1);
    }

    protected boolean canStartQinObjectDeclarationAt(int lookaheadOffset) {
        if (matchIdentifierValue("object", lookaheadOffset)) {
            return true;
        }
        if (!"At".equals(tokenNameAt(lookaheadOffset))) {
            return false;
        }
        return decoratedQinObjectDeclarationStart(lookaheadOffset);
    }

    @Override
    protected boolean canStartStatementListItemAt(int lookaheadOffset, StatementParams params) {
        return canStartQinObjectDeclarationAt(lookaheadOffset)
                || super.canStartStatementListItemAt(lookaheadOffset, params);
    }

    private boolean decoratedQinObjectDeclarationStart(int lookaheadOffset) {
        int depth = 0;
        for (int offset = lookaheadOffset + 1; offset <= lookaheadOffset + 24; offset++) {
            String tokenName = tokenNameAt(offset);
            if (tokenName == null) {
                return false;
            }
            if ("LParen".equals(tokenName) || "LBracket".equals(tokenName) || "LBrace".equals(tokenName)) {
                depth++;
                continue;
            }
            if ("RParen".equals(tokenName) || "RBracket".equals(tokenName) || "RBrace".equals(tokenName)) {
                if (depth == 0) {
                    return false;
                }
                depth--;
                continue;
            }
            if (depth == 0 && matchIdentifierValue("object", offset)) {
                return true;
            }
            if (depth == 0 && ("Class".equals(tokenName)
                    || "Const".equals(tokenName)
                    || "Function".equals(tokenName)
                    || "Let".equals(tokenName))) {
                return false;
            }
        }
        return false;
    }

    @Override
    @SubhutiRule public void ExportDeclaration() {
        if (false) {
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
            return;
        }
        if ("At".equals(tokenNameAt(1)) && decoratedQinObjectDeclarationStart(1)) {
            TSDecorators();
            tokenConsumer.Export();
            if ("Default".equals(tokenNameAt(1))) {
                tokenConsumer.Default();
                QinObjectDeclarationBody(new DeclarationParams(false, true, true));
            } else {
                QinObjectDeclarationBody(new DeclarationParams(false, true, false));
            }
            return;
        }
        if ("Export".equals(tokenNameAt(1)) && "Default".equals(tokenNameAt(2))
                && ("At".equals(tokenNameAt(3)) || matchIdentifierValue("object", 3))) {
            tokenConsumer.Export();
            tokenConsumer.Default();
            QinObjectDeclaration(new DeclarationParams(false, true, true));
            return;
        }
        if ("Export".equals(tokenNameAt(1))
                && ("At".equals(tokenNameAt(2)) || matchIdentifierValue("object", 2))) {
            tokenConsumer.Export();
            QinObjectDeclaration(new DeclarationParams(false, true, false));
            return;
        }
        super.ExportDeclaration();
    }
}
