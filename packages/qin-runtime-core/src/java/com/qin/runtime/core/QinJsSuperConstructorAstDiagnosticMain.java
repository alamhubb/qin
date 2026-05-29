package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;

public final class QinJsSuperConstructorAstDiagnosticMain {
    private QinJsSuperConstructorAstDiagnosticMain() {
    }

    public static void main(String[] args) {
        String source = """
                class Base {
                  constructor(value) { this.value = value; }
                }
                class Child extends Base {
                  constructor(value) { super(value); }
                }
                const child = new Child(42);
                const result = child.value;
                """;
        System.out.println(new QinFrontendLowerer().parseAst(source));
    }
}
