package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrTypeRef;

import java.util.List;
import java.util.Map;

/**
 * Smoke test for declaration method return member access against a local DTO.
 */
public final class QinJvmMemberAccessReturnSmokeTestMain {
    private QinJvmMemberAccessReturnSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrClassDeclaration payload = new QinIrClassDeclaration(
                null,
                "Payload",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(new QinIrFieldDeclaration(
                        "name",
                        QinIrTypeRef.stringType(),
                        List.of(),
                        null)),
                List.of());
        QinIrMethodDeclaration echoName = new QinIrMethodDeclaration(
                "echoName",
                QinIrTypeRef.stringType(),
                List.of(new QinIrParameter(
                        "payload",
                        QinIrTypeRef.classType("Payload"),
                        List.of())),
                List.of(),
                new QinIrMemberAccessExpression("payload", "name"));
        QinIrClassDeclaration controller = new QinIrClassDeclaration(
                null,
                "PayloadController",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(echoName));
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
                List.of(payload, controller));

        Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        ByteArrayClassLoader loader = new ByteArrayClassLoader();
        Class<?> payloadClass = loader.define("Payload", compiled.get("Payload"));
        Class<?> controllerClass = loader.define("PayloadController", compiled.get("PayloadController"));

        Object payloadInstance = payloadClass.getDeclaredConstructor().newInstance();
        payloadClass.getDeclaredMethod("setName", String.class).invoke(payloadInstance, "alice");
        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

        Object result = controllerClass.getDeclaredMethod("echoName", payloadClass).invoke(controllerInstance, payloadInstance);
        if (!"alice".equals(result)) {
            throw new IllegalStateException("Unexpected member access return result: " + result);
        }

        System.out.println("QinJvmMemberAccessReturnSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
