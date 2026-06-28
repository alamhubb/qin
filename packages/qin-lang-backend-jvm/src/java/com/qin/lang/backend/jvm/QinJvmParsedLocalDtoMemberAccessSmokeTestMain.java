package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.util.Map;

/**
 * Proves parsed Qin source can emit multiple local classes to JVM .class bytes
 * and execute member access across those local types.
 */
public final class QinJvmParsedLocalDtoMemberAccessSmokeTestMain {
    private QinJvmParsedLocalDtoMemberAccessSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class Payload {
                  name: string = "guest"
                }

                class PayloadController {
                  echoName(payload: Payload): string {
                    return payload.name
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        requireClass(program, "Payload");
        requireClass(program, "PayloadController");

        Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        ByteArrayClassLoader loader = new ByteArrayClassLoader();
        Class<?> payloadClass = loader.define("Payload", compiled.get("Payload"));
        Class<?> controllerClass = loader.define("PayloadController", compiled.get("PayloadController"));

        Object payload = payloadClass.getDeclaredConstructor().newInstance();
        payloadClass.getDeclaredMethod("setName", String.class).invoke(payload, "alice");
        Object controller = controllerClass.getDeclaredConstructor().newInstance();

        Object result = controllerClass.getDeclaredMethod("echoName", payloadClass).invoke(controller, payload);
        if (!"alice".equals(result)) {
            throw new IllegalStateException("Unexpected parsed local DTO member access result: " + result);
        }

        System.out.println("QinJvmParsedLocalDtoMemberAccessSmokeTestMain passed.");
    }

    private static void requireClass(QinIrProgram program, String binaryName) {
        QinIrClassDeclaration declaration = program.classDeclarations().stream()
                .filter(candidate -> binaryName.equals(candidate.binaryName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing class declaration: " + binaryName));
        if (!binaryName.equals(declaration.simpleName())) {
            throw new IllegalStateException("Unexpected simple name for " + binaryName + ": " + declaration.simpleName());
        }
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            if (bytes == null || bytes.length == 0) {
                throw new IllegalStateException("Missing class bytes for " + binaryName);
            }
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
