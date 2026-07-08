package com.qin.parser;

public final class QinParserDefaultParameterSmokeTestMain {
    public static void main(String[] args) {
        requireParses("default parameter", "export function f(a, b = null) { return b; }");
        requireParses("object method literal", "export function f() { return { getName() { return \"x\"; } }; }");
        requireParses("object method rest parameter", "export function f() { return { invoke(target, ...args) { return target(...args); } }; }");
        requireParses("adjacent export functions", "export function a(fn) { const f = (...args) => fn(...args); return f; }\nexport function b(ctor, meta = null) { return meta; }");
        requireParses("for-of const in method", "export function f() { return { m(xs) { for (const x of xs) { if (x) return true; } return false; } }; }");
        requireParses("nested object method return", "export function f() { return { getField(name) { return { get(target) { return target[name]; } }; } }; }");
        requireParses("nested conditional expression", "export function f(ctor, meta = null) { const className = meta && meta.name ? meta.name : (ctor && ctor.name ? ctor.name : \"Object\"); return className; }");
        System.out.println("QinParserDefaultParameterSmokeTestMain OK");
    }

    private static void requireParses(String label, String source) {
        QinParsedSource parsed;
        try {
            parsed = new QinParserFacade().parseSource(source);
        } catch (RuntimeException error) {
            throw new IllegalStateException("Failed snippet: " + label, error);
        }
        if (parsed.programAst() == null) {
            throw new AssertionError(label + " program AST must not be null");
        }
    }
}
