package com.qin.parser;

public final class QinParserHashbangSmokeTestMain {
    private QinParserHashbangSmokeTestMain() {
    }

    public static void main(String[] args) {
        String source = "\uFEFF#!/usr/bin/env node\n"
                + "var parser = require(\"..\");\n"
                + "console.log(\"ok\");\n";

        QinParsedSource parsed = new QinParserFacade().parseSource(source);
        if (!parsed.hasProgram()) {
            throw new AssertionError("Expected hashbang source to parse into a Program AST");
        }

        System.out.println("QinParserHashbangSmokeTestMain passed.");
    }
}
