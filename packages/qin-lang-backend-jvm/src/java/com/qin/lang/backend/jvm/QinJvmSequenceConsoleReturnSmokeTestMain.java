package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrSequenceExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrTypeRef;

import java.util.List;

/**
 * Smoke test for declaration sequence expression emission into JVM bytecode.
 */
public final class QinJvmSequenceConsoleReturnSmokeTestMain {
    private QinJvmSequenceConsoleReturnSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrMethodDeclaration method = new QinIrMethodDeclaration(
                "message",
                QinIrTypeRef.stringType(),
                List.of(),
                List.of(),
                new QinIrSequenceExpression(
                        List.of(new QinIrBuiltinCallExpression(
                                "console",
                                "log",
                                List.of(new QinIrStringLiteral("before")))),
                        new QinIrStringLiteral("hello")));

        QinIrClassDeclaration declaration = new QinIrClassDeclaration(
                null,
                "HelloService",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(method));
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

        Object result = defined.getDeclaredMethod("message").invoke(instance);
        if (!"hello".equals(result)) {
            throw new IllegalStateException("Unexpected sequence return result: " + result);
        }

        System.out.println("QinJvmSequenceConsoleReturnSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
