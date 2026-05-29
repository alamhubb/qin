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
 * Smoke test for declaration logical and conditional return emission into JVM bytecode.
 */
public final class QinJvmConditionalReturnSmokeTestMain {
    private QinJvmConditionalReturnSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrMethodDeclaration choose = new QinIrMethodDeclaration(
                "choose",
                QinIrTypeRef.stringType(),
                List.of(new QinIrParameter("flag", QinIrTypeRef.booleanType(), List.of())),
                List.of(),
                new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_conditional__",
                        List.of(
                                new QinIrIdentifierReference("flag"),
                                new QinIrStringLiteral("hello"),
                                new QinIrStringLiteral("bye"))));

        QinIrMethodDeclaration maybe = new QinIrMethodDeclaration(
                "maybe",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(
                        new QinIrParameter("flag", QinIrTypeRef.booleanType(), List.of()),
                        new QinIrParameter("name", QinIrTypeRef.stringType(), List.of())),
                List.of(),
                new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_logical__",
                        List.of(
                                new QinIrStringLiteral("&&"),
                                new QinIrIdentifierReference("flag"),
                                new QinIrIdentifierReference("name"))));

        QinIrClassDeclaration declaration = new QinIrClassDeclaration(
                null,
                "HelloService",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(choose, maybe));
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

        Object chooseTrue = defined.getDeclaredMethod("choose", boolean.class).invoke(instance, true);
        Object chooseFalse = defined.getDeclaredMethod("choose", boolean.class).invoke(instance, false);
        if (!"hello".equals(chooseTrue) || !"bye".equals(chooseFalse)) {
            throw new IllegalStateException("Unexpected conditional results: " + chooseTrue + ", " + chooseFalse);
        }

        Object maybeTrue = defined.getDeclaredMethod("maybe", boolean.class, String.class).invoke(instance, true, "qin");
        Object maybeFalse = defined.getDeclaredMethod("maybe", boolean.class, String.class).invoke(instance, false, "qin");
        if (!"qin".equals(maybeTrue) || !Boolean.FALSE.equals(maybeFalse)) {
            throw new IllegalStateException("Unexpected logical results: " + maybeTrue + ", " + maybeFalse);
        }

        System.out.println("QinJvmConditionalReturnSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
