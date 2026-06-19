package com.qin.lang.pipeline.cfa;

import java.lang.reflect.Method;

public final class QinCfaAstSnapshotLimitSmokeTestMain {
    private QinCfaAstSnapshotLimitSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Method method = QinCfaIrStage.class.getDeclaredMethod("renderAstTextForSnapshot", String.class);
        method.setAccessible(true);

        StringBuilder source = new StringBuilder();
        source.append("const seed = 1;\n");
        while (source.length() <= 33_000) {
            source.append("const value").append(source.length()).append(" = seed;\n");
        }

        String astText = (String) method.invoke(new QinCfaIrStage(), source.toString());
        if (!astText.startsWith("Program(AST snapshot skipped;")) {
            throw new IllegalStateException("Expected large AST snapshot to be skipped, got: " + astText);
        }
        if (!astText.contains("limit=32000")) {
            throw new IllegalStateException("Expected AST snapshot limit in message, got: " + astText);
        }

        System.out.println("QinCfaAstSnapshotLimitSmokeTestMain OK");
    }
}
