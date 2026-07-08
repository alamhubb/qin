package com.qin.parser;

public final class QinParserTrailingCommentSmokeTestMain {
    private QinParserTrailingCommentSmokeTestMain() {
    }

    public static void main(String[] args) {
        new QinParserFacade().parseSource("""
                const value = 1;

                /** trailing block comment */
                """);
        new QinParserFacade().parseSource("""
                const value = 1;

                // trailing line comment
                """);
        System.out.println("QinParserTrailingCommentSmokeTestMain passed.");
    }
}
