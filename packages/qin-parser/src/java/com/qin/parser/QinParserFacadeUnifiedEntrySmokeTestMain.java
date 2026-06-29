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

        QinParsedSource runtimeSyntaxParsed = new QinParserFacade().parseSource("""
                export const moduleUrl = import.meta.url;
                export async function loadModule() {
                    const loaded = await import("./dep.qin");
                    return loaded;
                }
                """);
        if (!runtimeSyntaxParsed.hasProgram()) {
            throw new AssertionError("Expected unified Qin parser entry to parse import.meta.url and dynamic import");
        }
        if (!runtimeSyntaxParsed.effectiveSource().contains("import.meta.url")) {
            throw new AssertionError("Expected import.meta.url syntax to stay in parser input");
        }
        if (!runtimeSyntaxParsed.effectiveSource().contains("import(\"./dep.qin\")")) {
            throw new AssertionError("Expected dynamic import syntax to stay in parser input");
        }
        if (runtimeSyntaxParsed.effectiveSource().contains("__qin_import_meta_url__")
                || runtimeSyntaxParsed.effectiveSource().contains("__qin_dynamic_import__")) {
            throw new AssertionError("Runtime ESM syntax must not be lowered by QinParserFacade");
        }

        System.out.println("QinParserFacadeUnifiedEntrySmokeTestMain passed.");
    }
}
