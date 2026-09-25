package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrTypeRef;

import java.util.List;
import java.util.Map;

/**
 * Proves Java String.charAt facade calls can feed JVM numeric char parameters
 * without changing ordinary charAt-as-string semantics.
 */
public final class QinJvmJavaLangStringCharNumericArgumentSmokeTestMain {
    private QinJvmJavaLangStringCharNumericArgumentSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrInstanceMethodCallExpression charAt = new QinIrInstanceMethodCallExpression(
                new QinIrIdentifierReference("__QinJavaLangString"),
                "charAt",
                List.of(new QinIrIdentifierReference("text"), new QinIrNumberLiteral(1)));
        QinIrClassDeclaration service = new QinIrClassDeclaration(
                null,
                "CharNumericArgumentService",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(
                        new QinIrMethodDeclaration(
                                "isLowerB",
                                QinIrTypeRef.booleanType(),
                                List.of(new QinIrParameter("ch", QinIrTypeRef.doubleType(), List.of())),
                                List.of(),
                                new QinIrBuiltinCallExpression(
                                        "Global",
                                        "__qin_binary__",
                                        List.of(
                                                new QinIrStringLiteral("=="),
                                                new QinIrIdentifierReference("ch"),
                                                new QinIrNumberLiteral('b'))),
                                null,
                                true),
                        new QinIrMethodDeclaration(
                                "matches",
                                QinIrTypeRef.booleanType(),
                                List.of(new QinIrParameter("text", QinIrTypeRef.stringType(), List.of())),
                                List.of(),
                                new QinIrStaticMethodCallExpression(
                                        "CharNumericArgumentService",
                                        "CharNumericArgumentService",
                                        "isLowerB",
                                        List.of(charAt))),
                        new QinIrMethodDeclaration(
                                "prefix",
                                QinIrTypeRef.stringType(),
                                List.of(new QinIrParameter("text", QinIrTypeRef.stringType(), List.of())),
                                List.of(),
                                new QinIrBuiltinCallExpression(
                                        "Global",
                                        "__qin_binary__",
                                        List.of(
                                                new QinIrStringLiteral("+"),
                                                new QinIrStaticMethodCallExpression(
                                                        "__QinJavaLangString",
                                                        "__QinJavaLangString",
                                                        "substring",
                                                        List.of(
                                                                new QinIrIdentifierReference("text"),
                                                                new QinIrNumberLiteral(0),
                                                                new QinIrNumberLiteral(2))),
                                                new QinIrStringLiteral("!"))))));
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
                List.of(service));

        Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        ByteArrayClassLoader loader = new ByteArrayClassLoader();
        Class<?> serviceClass = loader.define("CharNumericArgumentService", compiled.get("CharNumericArgumentService"));
        Object instance = serviceClass.getDeclaredConstructor().newInstance();

        Object match = serviceClass.getDeclaredMethod("matches", String.class).invoke(instance, "abc");
        Object miss = serviceClass.getDeclaredMethod("matches", String.class).invoke(instance, "axc");
        if (!Boolean.TRUE.equals(match) || !Boolean.FALSE.equals(miss)) {
            throw new IllegalStateException("Unexpected char numeric argument results: " + match + ", " + miss);
        }
        Object prefix = serviceClass.getDeclaredMethod("prefix", String.class).invoke(instance, "abcd");
        if (!"ab!".equals(prefix)) {
            throw new IllegalStateException("Unexpected static String.substring result: " + prefix);
        }

        System.out.println("QinJvmJavaLangStringCharNumericArgumentSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
