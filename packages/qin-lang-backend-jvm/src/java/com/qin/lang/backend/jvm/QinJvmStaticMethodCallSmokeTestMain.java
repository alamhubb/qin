package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrTypeRef;
import java.util.List;
import java.util.Map;

/**
 * Smoke test for declaration method return of a Java static method call.
 */
public final class QinJvmStaticMethodCallSmokeTestMain {
    private QinJvmStaticMethodCallSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrClassDeclaration controller = new QinIrClassDeclaration(
                null,
                "StaticCallController",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(new QinIrMethodDeclaration(
                        "safe",
                        QinIrTypeRef.stringType(),
                        List.of(new QinIrParameter("name", QinIrTypeRef.stringType(), List.of())),
                        List.of(),
                        new QinIrStaticMethodCallExpression(
                                "Objects",
                                "java.util.Objects",
                                "toString",
                                List.of(new QinIrIdentifierReference("name"))))));
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
                List.of(controller));

        Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        ByteArrayClassLoader loader = new ByteArrayClassLoader();
        Class<?> controllerClass = loader.define("StaticCallController", compiled.get("StaticCallController"));
        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

        Object result = controllerClass.getDeclaredMethod("safe", String.class).invoke(controllerInstance, "alice");
        if (!"alice".equals(result)) {
            throw new IllegalStateException("Unexpected static-call return result: " + result);
        }

        System.out.println("QinJvmStaticMethodCallSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}

