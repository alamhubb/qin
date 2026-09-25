package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrShortCircuitExpression;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrTypeRef;

import java.util.List;
import java.util.Map;

/**
 * Proves local static method overload resolution can use the existing
 * target-aware Java String charAt numeric emission rule before a short-circuit
 * condition asks for the call's return type.
 */
public final class QinJvmStaticCharAtNumericArgumentSmokeTestMain {
    private QinJvmStaticCharAtNumericArgumentSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrInstanceMethodCallExpression charAt = new QinIrInstanceMethodCallExpression(
                new QinIrIdentifierReference("__QinJavaLangString"),
                "charAt",
                List.of(new QinIrStringLiteral("abc"), new QinIrNumberLiteral(1)));
        QinIrClassDeclaration owner = new QinIrClassDeclaration(
                "probe",
                "Owner",
                null,
                List.of(),
                List.of(),
                List.of(
                        new QinIrMethodDeclaration(
                                "isPart",
                                QinIrTypeRef.booleanType(),
                                List.of(new QinIrParameter("value", QinIrTypeRef.doubleType(), List.of())),
                                List.of(),
                                new QinIrBooleanLiteral(true),
                                null,
                                true),
                        new QinIrMethodDeclaration(
                                "run",
                                QinIrTypeRef.booleanType(),
                                List.of(),
                                List.of(),
                                new QinIrShortCircuitExpression(
                                        new QinIrStaticMethodCallExpression(
                                                "Owner",
                                                "probe.Owner",
                                                "isPart",
                                                List.of(charAt)),
                                        "&&",
                                        new QinIrBooleanLiteral(true)))));
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
                List.of(owner));

        Map<String, byte[]> bytes = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        Loader loader = new Loader(QinJvmStaticCharAtNumericArgumentSmokeTestMain.class.getClassLoader());
        Class<?> ownerClass = loader.define("probe.Owner", bytes.get("probe.Owner"));
        Object instance = ownerClass.getConstructor().newInstance();
        Object result = ownerClass.getMethod("run").invoke(instance);
        if (!Boolean.TRUE.equals(result)) {
            throw new IllegalStateException("Expected target-aware charAt static argument call to return true");
        }

        System.out.println("QinJvmStaticCharAtNumericArgumentSmokeTestMain OK");
    }

    private static final class Loader extends ClassLoader {
        private Loader(ClassLoader parent) {
            super(parent);
        }

        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
