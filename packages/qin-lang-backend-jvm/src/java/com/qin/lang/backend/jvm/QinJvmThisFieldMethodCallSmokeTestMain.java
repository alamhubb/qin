package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrThisExpression;
import com.qin.lang.ir.QinIrTypeRef;

import java.util.List;
import java.util.Map;

/**
 * Smoke test for declaration return lowering of this.field.method().
 */
public final class QinJvmThisFieldMethodCallSmokeTestMain {
    private QinJvmThisFieldMethodCallSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrClassDeclaration service = new QinIrClassDeclaration(
                null,
                "HelloService",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(new QinIrMethodDeclaration(
                        "message",
                        QinIrTypeRef.stringType(),
                        List.of(),
                        List.of(),
                        new QinIrStringLiteral("hello from service"))));

        QinIrClassDeclaration controller = new QinIrClassDeclaration(
                null,
                "HelloController",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(new QinIrFieldDeclaration(
                        "service",
                        QinIrTypeRef.classType("HelloService"),
                        List.of(),
                        null)),
                List.of(new QinIrMethodDeclaration(
                        "hello",
                        QinIrTypeRef.classType("java.lang.Object"),
                        List.of(),
                        List.of(),
                        new QinIrInstanceMethodCallExpression(
                                new QinIrPropertyAccessExpression(
                                        new QinIrThisExpression(),
                                        "service"),
                                "message",
                                List.of()))));

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
                List.of(service, controller));

        Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        ByteArrayClassLoader loader = new ByteArrayClassLoader();
        Class<?> serviceClass = loader.define("HelloService", compiled.get("HelloService"));
        Class<?> controllerClass = loader.define("HelloController", compiled.get("HelloController"));

        Object serviceInstance = serviceClass.getDeclaredConstructor().newInstance();
        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
        controllerClass.getDeclaredMethod("setService", serviceClass).invoke(controllerInstance, serviceInstance);

        Object result = controllerClass.getDeclaredMethod("hello").invoke(controllerInstance);
        if (!"hello from service".equals(result)) {
            throw new IllegalStateException("Unexpected method-call return result: " + result);
        }

        System.out.println("QinJvmThisFieldMethodCallSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
