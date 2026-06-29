package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrProgram;

/**
 * Proves parsed Qin try/catch method bodies lower to statement IR and emit
 * executable JVM .class exception flow.
 */
public final class QinJvmParsedTryCatchMethodBodySmokeTestMain {
    private QinJvmParsedTryCatchMethodBodySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                import { RuntimeException } from "java:java.lang"

                class ParsedTryCatchMethodBodyService {
                  label(flag: boolean): string {
                    try {
                      if (flag) {
                        throw new RuntimeException("boom")
                      }
                      return "ok"
                    } catch (e) {
                      return "caught"
                    } finally {
                    }
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = requireClass(program, "ParsedTryCatchMethodBodyService");
        if (declaration.methods().size() != 1 || declaration.methods().get(0).bodyStatements().isEmpty()) {
            throw new IllegalStateException("Parsed try/catch method did not lower to statement body IR");
        }

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "ParsedTryCatchMethodBodyService");
        Class<?> defined = new ByteArrayClassLoader().define("ParsedTryCatchMethodBodyService", classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();

        Object ok = defined.getDeclaredMethod("label", boolean.class).invoke(instance, false);
        Object caught = defined.getDeclaredMethod("label", boolean.class).invoke(instance, true);
        if (!"ok".equals(ok) || !"caught".equals(caught)) {
            throw new IllegalStateException("Unexpected parsed try/catch results: " + ok + ", " + caught);
        }

        System.out.println("QinJvmParsedTryCatchMethodBodySmokeTestMain passed.");
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
