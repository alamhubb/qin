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

        QinParsedSource switchParsed = new QinParserFacade().parseSource("""
                export function label(value) {
                    switch (value) {
                        case "a":
                            return "alpha";
                        default:
                            return "other";
                    }
                }
                """);
        if (!switchParsed.hasProgram()) {
            throw new AssertionError("Expected unified Qin parser entry to parse switch syntax");
        }
        if (!switchParsed.effectiveSource().contains("switch (value)")) {
            throw new AssertionError("Expected switch syntax to stay in parser input");
        }
        if (switchParsed.effectiveSource().contains("__qin_switch_")) {
            throw new AssertionError("Switch syntax must not be lowered by QinParserFacade");
        }

        System.out.println("QinParserFacadeUnifiedEntrySmokeTestMain passed.");
    }
}
