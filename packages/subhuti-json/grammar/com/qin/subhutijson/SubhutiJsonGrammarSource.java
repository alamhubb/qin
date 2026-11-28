package com.qin.subhutijson;

import com.qin.parser.QinParser;
import com.subhuti.parser.SubhutiDecisionPredicate;
import com.subhuti.parser.SubhutiRule;
import com.subhuti.parser.SubhutiStaticGrammarSource;

import static com.subhuti.parser.SubhutiCompileOnlyDsl.Many;
import static com.subhuti.parser.SubhutiCompileOnlyDsl.Or;
import static com.subhuti.parser.SubhutiCompileOnlyDsl.gate;

@SubhutiStaticGrammarSource
public final class SubhutiJsonGrammarSource extends QinParser {
    public SubhutiJsonGrammarSource(String source) {
        super(source);
    }

    @SubhutiRule
    public void JsonDocument() {
        JsonValue();
    }

    @SubhutiRule
    public void JsonValue() {
        if (false) {
            Or(
                    gate(this::jsonObjectStart, () -> JsonObject()),
                    gate(this::jsonArrayStart, () -> JsonArray()),
                    gate(this::jsonStringStart, () -> tokenConsumer.StringLiteral()),
                    gate(this::jsonNumberStart, () -> tokenConsumer.NumericLiteral()),
                    gate(this::jsonTrueStart, () -> tokenConsumer.True()),
                    gate(this::jsonFalseStart, () -> tokenConsumer.False()),
                    gate(this::jsonNullStart, () -> tokenConsumer.NullLiteral()));
            return;
        }
        if (jsonObjectStart()) JsonObject();
        else if (jsonArrayStart()) JsonArray();
        else if (jsonStringStart()) tokenConsumer.StringLiteral();
        else if (jsonNumberStart()) tokenConsumer.NumericLiteral();
        else if (jsonTrueStart()) tokenConsumer.True();
        else if (jsonFalseStart()) tokenConsumer.False();
        else if (jsonNullStart()) tokenConsumer.NullLiteral();
        else setParseFail();
    }

    @SubhutiRule
    public void JsonObject() {
        tokenConsumer.LBrace();
        if ("StringLiteral".equals(tokenNameAt(1))) {
            JsonMember();
            Many(() -> {
                tokenConsumer.Comma();
                JsonMember();
            });
        }
        tokenConsumer.RBrace();
    }

    @SubhutiRule
    public void JsonMember() {
        tokenConsumer.StringLiteral();
        tokenConsumer.Colon();
        JsonValue();
    }

    @SubhutiRule
    public void JsonArray() {
        tokenConsumer.LBracket();
        if (!"RBracket".equals(tokenNameAt(1))) {
            JsonValue();
            Many(() -> {
                tokenConsumer.Comma();
                JsonValue();
            });
        }
        tokenConsumer.RBracket();
    }

    @SubhutiDecisionPredicate
    protected boolean jsonObjectStart() {
        return "LBrace".equals(tokenNameAt(1));
    }

    @SubhutiDecisionPredicate
    protected boolean jsonArrayStart() {
        return "LBracket".equals(tokenNameAt(1));
    }

    @SubhutiDecisionPredicate
    protected boolean jsonStringStart() {
        return "StringLiteral".equals(tokenNameAt(1));
    }

    @SubhutiDecisionPredicate
    protected boolean jsonNumberStart() {
        return "NumericLiteral".equals(tokenNameAt(1));
    }

    @SubhutiDecisionPredicate
    protected boolean jsonTrueStart() {
        return "True".equals(tokenNameAt(1));
    }

    @SubhutiDecisionPredicate
    protected boolean jsonFalseStart() {
        return "False".equals(tokenNameAt(1));
    }

    @SubhutiDecisionPredicate
    protected boolean jsonNullStart() {
        return "NullLiteral".equals(tokenNameAt(1));
    }
}
