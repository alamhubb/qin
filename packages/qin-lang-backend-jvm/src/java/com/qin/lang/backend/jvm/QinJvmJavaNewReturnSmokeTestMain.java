package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrTypeRef;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Smoke test for declaration method return of a Java constructor expression.
 */
public final class QinJvmJavaNewReturnSmokeTestMain {
    private QinJvmJavaNewReturnSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrClassDeclaration controller = new QinIrClassDeclaration(
                null,
                "JavaNewController",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(new QinIrMethodDeclaration(
                        "fresh",
                        QinIrTypeRef.classType("java.util.ArrayList"),
                        List.of(),
                        List.of(),
                        new QinIrJavaNewExpression(
                                "ArrayList",
                                "java.util.ArrayList",
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
                List.of(controller));

        Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        ByteArrayClassLoader loader = new ByteArrayClassLoader();
        Class<?> controllerClass = loader.define("JavaNewController", compiled.get("JavaNewController"));
        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

        Object result = controllerClass.getDeclaredMethod("fresh").invoke(controllerInstance);
        if (!(result instanceof ArrayList<?> arrayList) || !arrayList.isEmpty()) {
            throw new IllegalStateException("Unexpected Java new return result: " + result);
        }

        System.out.println("QinJvmJavaNewReturnSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}

