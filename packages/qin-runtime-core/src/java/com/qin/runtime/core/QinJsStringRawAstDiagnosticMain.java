package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;

public final class QinJsStringRawAstDiagnosticMain {
    private QinJsStringRawAstDiagnosticMain() {
    }

    public static void main(String[] args) {
        String source = """
                const raw = String.raw`[\\p{ID_Start}$_]|\\\\u[0-9a-fA-F]{4}`;
                raw;
                """;
        System.out.println(new QinFrontendLowerer().parseAst(source));
    }
}
