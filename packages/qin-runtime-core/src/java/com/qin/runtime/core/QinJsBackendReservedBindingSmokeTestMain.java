package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrTypeRef;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendReservedBindingSmokeTestMain {
    private QinJsBackendReservedBindingSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrClassDeclaration(
                        "demo",
                        "KeywordBox",
                        null,
                        List.of(),
                        List.of(),
                        List.of(new QinIrMethodDeclaration(
                                "echo",
                                QinIrTypeRef.stringType(),
                                List.of(new QinIrParameter("in", QinIrTypeRef.stringType(), List.of())),
                                List.of(),
                                new QinIrIdentifierReference("in"))))),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("echo(__qin_in)"), "reserved parameter alias");
        require(generated.contains("return __qin_in;"), "reserved parameter reference alias");

        Path root = Files.createTempDirectory("qin-js-backend-reserved-binding-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-reserved-binding\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nnew KeywordBox().echo(\"ok\");\n",
                "reserved_binding");
        if (!"ok".equals(result)) {
            throw new IllegalStateException("Expected reserved binding result, got: " + result);
        }
        System.out.println("QinJsBackendReservedBindingSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
