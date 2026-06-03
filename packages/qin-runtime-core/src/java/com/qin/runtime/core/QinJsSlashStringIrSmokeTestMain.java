package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;

public final class QinJsSlashStringIrSmokeTestMain {
    private QinJsSlashStringIrSmokeTestMain() {
    }

    public static void main(String[] args) {
        QinIrProgram program = new QinFrontendLowerer().lowerSource("const path = \"/workspace/app\";");
        if (program.declarations().size() != 1) {
            throw new IllegalStateException("Expected one declaration, got: " + program.declarations().size());
        }
        QinIrConstDeclaration declaration = program.declarations().get(0);
        if (!(declaration.initializer() instanceof QinIrStringLiteral literal)) {
            throw new IllegalStateException("Expected string literal initializer, got: "
                    + declaration.initializer().getClass().getName());
        }
        if (!"/workspace/app".equals(literal.value())) {
            throw new IllegalStateException("Expected normalized path string, got: " + literal.value());
        }
        System.out.println("QinJsSlashStringIrSmokeTestMain OK");
    }
}
