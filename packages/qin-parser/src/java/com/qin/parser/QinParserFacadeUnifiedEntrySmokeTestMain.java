package com.qin.parser;

public final class QinParserFacadeUnifiedEntrySmokeTestMain {
    private QinParserFacadeUnifiedEntrySmokeTestMain() {
    }

    public static void main(String[] args) {
        String source = """
                let _showRulePath = true;
                function getShowRulePath() {
                    return _showRulePath;
                }
                var TreeFormatHelper = class {
                    static formatLine(content, options) {
                        return (options.prefix ?? "  ".repeat(options.depth ?? 0)) + content;
                    }
                };
                """;

        QinParsedSource parsed = new QinParserFacade().parseSource(source);
        if (!parsed.hasProgram()) {
            throw new AssertionError("Expected unified Qin parser entry to parse ordinary JS source");
        }

        System.out.println("QinParserFacadeUnifiedEntrySmokeTestMain passed.");
    }
}
