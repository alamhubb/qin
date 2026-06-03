package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendLogicalBuiltinSmokeTestMain {
    private QinJsBackendLogicalBuiltinSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(new QinIrConstDeclaration(
                        "value",
                        new QinIrBuiltinCallExpression(
                                "Global",
                                "__qin_logical__",
                                List.of(
                                        new QinIrStringLiteral("&&"),
                                        new QinIrBooleanLiteral(true),
                                        new QinIrBooleanLiteral(false))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("function __qin_logical__"), "logical helper emission");
        require(generated.contains("__qin_logical__(\"&&\", true, false)"), "logical helper call");

        Path root = Files.createTempDirectory("qin-js-backend-logical-builtin-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-logical-builtin\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nglobalThis.__qinResult;\n",
                "js_backend_logical_builtin");
        if (!Boolean.FALSE.equals(result)) {
            throw new IllegalStateException("Expected logical builtin result false, got: " + result);
        }
        System.out.println("QinJsBackendLogicalBuiltinSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
