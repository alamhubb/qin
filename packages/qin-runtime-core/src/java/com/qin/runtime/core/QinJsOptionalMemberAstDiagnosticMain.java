package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;

public final class QinJsOptionalMemberAstDiagnosticMain {
    private QinJsOptionalMemberAstDiagnosticMain() {
    }

    public static void main(String[] args) {
        String source = """
                const options = { tokenConsumer: "consumer", tokenDefinitions: ["A", "B"] };
                const value = options?.tokenDefinitions ?? ["X"];
                value;
                """;
        System.out.println(new QinFrontendLowerer().parseAst(source));
    }
}
