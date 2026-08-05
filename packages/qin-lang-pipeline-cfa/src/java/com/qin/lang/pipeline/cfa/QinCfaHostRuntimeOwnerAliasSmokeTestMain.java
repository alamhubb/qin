package com.qin.lang.pipeline.cfa;

import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;

import java.util.List;
import java.util.Map;

public final class QinCfaHostRuntimeOwnerAliasSmokeTestMain {
    private QinCfaHostRuntimeOwnerAliasSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrClassDeclaration shadowRuntimeOwner = new QinIrClassDeclaration(
                null,
                "com_qin_lang_runtime_JavaEsmGlobal",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(new QinIrMethodDeclaration(
                        "__qin_binary__",
                        QinIrTypeRef.classType("java.lang.Object"),
                        List.of(
                                new QinIrParameter("operator", QinIrTypeRef.classType("java.lang.Object"), List.<QinIrAnnotation>of()),
                                new QinIrParameter("left", QinIrTypeRef.classType("java.lang.Object"), List.<QinIrAnnotation>of()),
                                new QinIrParameter("right", QinIrTypeRef.classType("java.lang.Object"), List.<QinIrAnnotation>of())),
                        List.of(),
                        null,
                        null,
                        true)));
        QinCfaProgram program = new QinCfaProgram(
                List.of(),
                List.of(
                        new QinCfaProgram.ExpressionStatement(new QinCfaProgram.StaticMethodCallExpression(
                                "JavaEsmGlobal",
                                "com.qin.lang.runtime.JavaEsmGlobal",
                                "__qin_binary__",
                                List.of(
                                        new QinCfaProgram.StringLiteral("=="),
                                        new QinCfaProgram.NumberLiteral(1.0),
                                        new QinCfaProgram.NumberLiteral(1.0)))),
                        new QinCfaProgram.ExpressionStatement(new QinCfaProgram.StaticMethodCallExpression(
                                "JavaEsmGlobal",
                                "com_qin_lang_runtime_JavaEsmGlobal",
                                "__qin_binary__",
                                List.of(
                                        new QinCfaProgram.StringLiteral("=="),
                                        new QinCfaProgram.NumberLiteral(2.0),
                                        new QinCfaProgram.NumberLiteral(2.0))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generatedName = "com.qin.lang.pipeline.cfa.QinCfaHostRuntimeOwnerAliasSmokeGenerated";
        byte[] moduleBytes = new QinCfaJvmClassFileBackend().compileProgram(
                program,
                generatedName,
                Map.of(shadowRuntimeOwner.binaryName(), shadowRuntimeOwner));

        Loader loader = new Loader(QinCfaHostRuntimeOwnerAliasSmokeTestMain.class.getClassLoader());
        Class<?> moduleClass = loader.define(generatedName, moduleBytes);
        Object result = moduleClass.getMethod("run").invoke(null);
        if (!Boolean.TRUE.equals(result)) {
            throw new AssertionError("expected host JavaEsmGlobal.__qin_binary__ result true, got " + result);
        }
        System.out.println("QinCfaHostRuntimeOwnerAliasSmokeTestMain OK");
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
