package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrProgram;

/**
 * Proves parsed Qin methods can call another method on the same class and emit
 * executable JVM .class bytecode.
 */
public final class QinJvmParsedSelfMethodCallSmokeTestMain {
    private QinJvmParsedSelfMethodCallSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class ParsedSelfMethodCallService {
                  prefix(): string {
                    return "hello "
                  }

                  label(name: string): string {
                    return this.prefix() + name
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = requireClass(program, "ParsedSelfMethodCallService");
        if (declaration.methods().size() != 2) {
            throw new IllegalStateException("Expected two parsed self-call methods, got "
                    + declaration.methods().size());
        }

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "ParsedSelfMethodCallService");
        Class<?> defined = new ByteArrayClassLoader().define("ParsedSelfMethodCallService", classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();

        Object prefix = defined.getDeclaredMethod("prefix").invoke(instance);
        Object label = defined.getDeclaredMethod("label", String.class).invoke(instance, "qin");
        if (!"hello ".equals(prefix) || !"hello qin".equals(label)) {
            throw new IllegalStateException("Unexpected parsed self-method call results: " + prefix + ", " + label);
        }

        System.out.println("QinJvmParsedSelfMethodCallSmokeTestMain passed.");
    }

    private static QinIrClassDeclaration requireClass(QinIrProgram program, String binaryName) {
        return program.classDeclarations().stream()
                .filter(candidate -> binaryName.equals(candidate.binaryName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing class declaration: " + binaryName));
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
