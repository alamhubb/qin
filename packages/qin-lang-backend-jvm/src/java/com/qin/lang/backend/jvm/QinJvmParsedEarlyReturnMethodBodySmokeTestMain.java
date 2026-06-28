package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrProgram;

/**
 * Proves parsed Qin declaration method bodies with local bindings and early
 * return branches lower to JVM .class execution.
 */
public final class QinJvmParsedEarlyReturnMethodBodySmokeTestMain {
    private QinJvmParsedEarlyReturnMethodBodySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class ParsedEarlyReturnMethodBodyService {
                  label(name: string, flag: boolean): string {
                    const prefix = "hello "
                    if (flag) {
                      return prefix + name
                    }
                    return "bye " + name
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "ParsedEarlyReturnMethodBodyService");
        Class<?> defined = new ByteArrayClassLoader().define("ParsedEarlyReturnMethodBodyService", classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();

        Object hello = defined.getDeclaredMethod("label", String.class, boolean.class)
                .invoke(instance, "qin", true);
        Object bye = defined.getDeclaredMethod("label", String.class, boolean.class)
                .invoke(instance, "qin", false);
        if (!"hello qin".equals(hello) || !"bye qin".equals(bye)) {
            throw new IllegalStateException("Unexpected parsed early-return method results: " + hello + ", " + bye);
        }

        System.out.println("QinJvmParsedEarlyReturnMethodBodySmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
