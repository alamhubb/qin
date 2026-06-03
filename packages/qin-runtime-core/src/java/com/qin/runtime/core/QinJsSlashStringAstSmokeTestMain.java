package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;

public final class QinJsSlashStringAstSmokeTestMain {
    private QinJsSlashStringAstSmokeTestMain() {
    }

    public static void main(String[] args) {
        String ast = new QinFrontendLowerer().parseAst("const path = \"/workspace/app\"; const regex = /workspace\\/app/;");
        if (!ast.contains("\"value\":\"\\\"/workspace/app\\\"\"")
                || !ast.contains("\"raw\":\"\\\"/workspace/app\\\"\"")) {
            throw new IllegalStateException("Expected slash path string literal in AST, got: " + ast);
        }
        if (!ast.contains("\"regex\"")) {
            throw new IllegalStateException("Expected regex literal to keep regex metadata, got: " + ast);
        }
        System.out.println("QinJsSlashStringAstSmokeTestMain OK");
    }
}
