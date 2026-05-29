package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrTypeRef;

import java.util.List;
import java.util.Map;

/**
 * Smoke test for declaration object literal return emission into JVM bytecode.
 */
public final class QinJvmObjectLiteralReturnSmokeTestMain {
    private QinJvmObjectLiteralReturnSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrMethodDeclaration detail = new QinIrMethodDeclaration(
                "detail",
                QinIrTypeRef.classType("java.util.Map"),
                List.of(),
                List.of(),
                new QinIrObjectLiteral(List.of(
                        new QinIrObjectProperty("message", new QinIrStringLiteral("hello")),
                        new QinIrObjectProperty("ok", new QinIrBooleanLiteral(true)))));
        QinIrClassDeclaration declaration = new QinIrClassDeclaration(
                null,
                "HelloController",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(detail));
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
                List.of(declaration));

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, declaration.binaryName());
        Class<?> defined = new ByteArrayClassLoader().define(declaration.binaryName(), classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();
        Object result = defined.getDeclaredMethod("detail").invoke(instance);
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected Map return value, got " + result);
        }
        if (!"hello".equals(map.get("message"))) {
            throw new IllegalStateException("Unexpected object literal message: " + map.get("message"));
        }
        if (!Boolean.TRUE.equals(map.get("ok"))) {
            throw new IllegalStateException("Unexpected object literal ok: " + map.get("ok"));
        }

        System.out.println("QinJvmObjectLiteralReturnSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
