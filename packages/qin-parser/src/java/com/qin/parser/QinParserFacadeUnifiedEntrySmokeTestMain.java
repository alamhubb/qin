package com.qin.parser;

import com.slime.ast.nodes.modules.ImportDeclaration;

public final class QinParserFacadeUnifiedEntrySmokeTestMain {
    private QinParserFacadeUnifiedEntrySmokeTestMain() {
    }

    public static void main(String[] args) {
        preservesJavaImportsBeforeDeclarations();
        validatesNullishOperators();
        new QinParserFacade().parseSource("const methods = { regionMatches(value, ...args) { return args; } };");
        new QinParserFacade().parseSource("function f(value, ...args) { return args; } const g = (value,) => value;");
        new QinParserFacade().parseSource("while (index < items.length && compare(items[index], value) < 0) { index++; }");
        new QinParserFacade().parseSource("const value = identity<Array<string>>([]); const box = new Box<string>();");
        new QinParserFacade().parseSource("class Flags { public override(): boolean { return true; } readonly() { return false; } declare() {} }");
        new QinParserFacade().parseSource("const options = { ...defaults, value: 1, }; const names = [...values,];");
        String comparison = new QinParserFacade().parseProgram("const atEnd = offset >= bytes.length;").toString();
        if (!comparison.contains("operator=>=")) throw new AssertionError("Expected the complete >= operator in the AST");

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

    private static void preservesJavaImportsBeforeDeclarations() {
        QinParsedSource parsed = new QinParserFacade().parseSource("""
                import { ArrayList } from "java:java.util"
                class MyList extends ArrayList {
                  label(): string {
                    return "ok"
                  }
                }
                """);
        if (parsed.requireProgram().body().size() != 2
                || !(parsed.requireProgram().body().get(0) instanceof ImportDeclaration)
                || !"ClassDeclaration".equals(parsed.requireProgram().body().get(1).getClass().getSimpleName())) {
            throw new AssertionError("ModuleItemList must preserve imports before following declarations");
        }
    }

    private static void validatesNullishOperators() {
        QinParser parser = new QinParser("const result = (a && b) ?? c;");
        if (parser.Program() == null || parser.isParserFail()) {
            throw new AssertionError("Parenthesized nullish/logical expressions should parse");
        }
        for (String expression : new String[]{"a && b ?? c", "a || b ?? c", "a ?? b && c", "a ?? b || c"}) {
            QinParser invalid = new QinParser("const result = " + expression + ";");
            invalid.Program();
            if (!invalid.isParserFail()) throw new AssertionError("Accepted mixed nullish/logical expression " + expression);
        }
    }
}
