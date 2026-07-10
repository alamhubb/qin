package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrForEachStatement;
import com.qin.lang.ir.QinIrProgram;

/**
 * Proves parsed Qin for...of over string lowers to static JVM bytecode.
 */
public final class QinJvmParsedStringForOfSmokeTestMain {
    private QinJvmParsedStringForOfSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class ParsedStringForOfService {
                  count(text: string): number {
                    let total = 0
                    for (const ch of text) {
                      total = total + 1
                    }
                    return total
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = requireClass(program, "ParsedStringForOfService");
        QinIrForEachStatement forEachStatement = declaration.methods().get(0).bodyStatements().stream()
                .filter(QinIrForEachStatement.class::isInstance)
                .map(QinIrForEachStatement.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Parsed method did not lower string for...of IR"));
        if (!"ch".equals(forEachStatement.itemName())) {
            throw new IllegalStateException("Parsed string for...of item binding was not preserved");
        }

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "ParsedStringForOfService");
        Class<?> defined = new ByteArrayClassLoader().define("ParsedStringForOfService", classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();

        Object result = defined.getDeclaredMethod("count", String.class).invoke(instance, "abcd");
        if (!Double.valueOf(4.0d).equals(result)) {
            throw new IllegalStateException("Unexpected parsed string for...of result: " + result);
        }

        System.out.println("QinJvmParsedStringForOfSmokeTestMain passed.");
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
