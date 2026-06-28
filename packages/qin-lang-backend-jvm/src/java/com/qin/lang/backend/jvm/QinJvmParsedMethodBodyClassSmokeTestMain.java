package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrProgram;

/**
 * Proves parsed Qin class method bodies can lower to IR, emit JVM .class bytes,
 * and execute through reflection.
 */
public final class QinJvmParsedMethodBodyClassSmokeTestMain {
    private QinJvmParsedMethodBodyClassSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class ParsedMethodBodyService {
                  greet(name: string): string {
                    return "hello " + name
                  }

                  choose(flag: boolean): string {
                    return flag ? "yes" : "no"
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "ParsedMethodBodyService");
        Class<?> defined = new ByteArrayClassLoader().define("ParsedMethodBodyService", classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();

        Object greeting = defined.getDeclaredMethod("greet", String.class).invoke(instance, "qin");
        if (!"hello qin".equals(greeting)) {
            throw new IllegalStateException("Unexpected parsed binary method result: " + greeting);
        }

        Object yes = defined.getDeclaredMethod("choose", boolean.class).invoke(instance, true);
        Object no = defined.getDeclaredMethod("choose", boolean.class).invoke(instance, false);
        if (!"yes".equals(yes) || !"no".equals(no)) {
            throw new IllegalStateException("Unexpected parsed conditional method results: " + yes + ", " + no);
        }

        System.out.println("QinJvmParsedMethodBodyClassSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
