package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaAtomicLongSmokeTestMain {
    private QinJsBackendJavaAtomicLongSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(new QinIrConstDeclaration(
                        "counter",
                        new QinIrJavaNewExpression(
                                "AtomicLong",
                                "java.util.concurrent.atomic.AtomicLong",
                                List.of(new QinIrNumberLiteral(2))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:java.util.concurrent.atomic",
                        "AtomicLong",
                        "AtomicLong",
                        "java.util.concurrent.atomic.AtomicLong")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class __QinJavaUtilConcurrentAtomicLong"), "AtomicLong runtime shim");
        require(generated.contains("const AtomicLong = __QinJavaUtilConcurrentAtomicLong;"), "AtomicLong import alias");

        Path root = Files.createTempDirectory("qin-js-backend-atomic-long-");
        Files.writeString(
                root.resolve("qin.config.js"),
                "export default { name: \"qin-js-backend-atomic-long\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst first = globalThis.__qinResult.incrementAndGet();\n"
                        + "globalThis.__qinResult.set(10);\n"
                        + "first + \":\" + globalThis.__qinResult.get() + \":\" + globalThis.__qinResult.compareAndSet(10, 12) + \":\" + globalThis.__qinResult.get();\n",
                "js_backend_atomic_long");
        if (!"3:10:true:12".equals(result)) {
            throw new IllegalStateException("Expected generated AtomicLong result, got: " + result);
        }
        System.out.println("QinJsBackendJavaAtomicLongSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
