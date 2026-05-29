package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrTypeRef;

import java.util.List;

/**
 * Smoke test for declaration binary return emission into JVM bytecode.
 */
public final class QinJvmBinaryReturnSmokeTestMain {
    private QinJvmBinaryReturnSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrMethodDeclaration greet = new QinIrMethodDeclaration(
                "greet",
                QinIrTypeRef.stringType(),
                List.of(new QinIrParameter("name", QinIrTypeRef.stringType(), List.of())),
                List.of(),
                new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_binary__",
                        List.of(
                                new QinIrStringLiteral("+"),
                                new QinIrStringLiteral("hello "),
                                new QinIrIdentifierReference("name"))));
        QinIrClassDeclaration declaration = new QinIrClassDeclaration(
                null,
                "HelloService",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(greet));
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
        Object result = defined.getDeclaredMethod("greet", String.class).invoke(instance, "qin");
        if (!"hello qin".equals(result)) {
            throw new IllegalStateException("Unexpected binary return result: " + result);
        }

        System.out.println("QinJvmBinaryReturnSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
