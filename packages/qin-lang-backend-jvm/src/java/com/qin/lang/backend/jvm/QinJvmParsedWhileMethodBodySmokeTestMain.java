package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrWhileStatementNode;

/**
 * Proves parsed Qin while method bodies lower to statement IR and emit
 * executable JVM .class loop control flow.
 */
public final class QinJvmParsedWhileMethodBodySmokeTestMain {
    private QinJvmParsedWhileMethodBodySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class ParsedWhileMethodBodyService {
                  label(active: boolean): string {
                    while (active) {
                      return "loop"
                    }
                    return "done"
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = requireClass(program, "ParsedWhileMethodBodyService");
        if (declaration.methods().size() != 1
                || declaration.methods().get(0).bodyStatements().stream()
                .noneMatch(QinIrWhileStatementNode.class::isInstance)) {
            throw new IllegalStateException("Parsed while method did not lower to while statement IR");
        }

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "ParsedWhileMethodBodyService");
        Class<?> defined = new ByteArrayClassLoader().define("ParsedWhileMethodBodyService", classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();

        Object loop = defined.getDeclaredMethod("label", boolean.class).invoke(instance, true);
        Object done = defined.getDeclaredMethod("label", boolean.class).invoke(instance, false);
        if (!"loop".equals(loop) || !"done".equals(done)) {
            throw new IllegalStateException("Unexpected parsed while results: " + loop + ", " + done);
        }

        System.out.println("QinJvmParsedWhileMethodBodySmokeTestMain passed.");
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
