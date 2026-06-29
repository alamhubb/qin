package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrBreakStatement;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrContinueStatement;
import com.qin.lang.ir.QinIrForStatement;
import com.qin.lang.ir.QinIrIfStatement;
import com.qin.lang.ir.QinIrProgram;

/**
 * Proves parsed Qin for/break/continue method bodies lower to statement IR and
 * execute as JVM .class loop bytecode.
 */
public final class QinJvmParsedForBreakContinueSmokeTestMain {
    private QinJvmParsedForBreakContinueSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class ParsedForBreakContinueService {
                  sum(limit: number): number {
                    let total = 0
                    for (let i = 0; i < limit; i = i + 1) {
                      if (i == 2) {
                        continue
                      }
                      if (i == 5) {
                        break
                      }
                      total = total + i
                    }
                    return total
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = requireClass(program, "ParsedForBreakContinueService");
        if (declaration.methods().size() != 1) {
            throw new IllegalStateException("Expected one parsed for/break/continue method");
        }
        QinIrForStatement forStatement = declaration.methods().get(0).bodyStatements().stream()
                .filter(QinIrForStatement.class::isInstance)
                .map(QinIrForStatement.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Parsed method did not lower for statement IR"));
        if (forStatement.initializerDeclarations().size() != 1 || forStatement.updateExpressions().size() != 1) {
            throw new IllegalStateException("Parsed for statement did not lower initializer/update IR");
        }
        boolean hasContinue = forStatement.body().stream()
                .filter(QinIrIfStatement.class::isInstance)
                .map(QinIrIfStatement.class::cast)
                .flatMap(statement -> statement.consequent().stream())
                .anyMatch(QinIrContinueStatement.class::isInstance);
        boolean hasBreak = forStatement.body().stream()
                .filter(QinIrIfStatement.class::isInstance)
                .map(QinIrIfStatement.class::cast)
                .flatMap(statement -> statement.consequent().stream())
                .anyMatch(QinIrBreakStatement.class::isInstance);
        if (!hasContinue || !hasBreak) {
            throw new IllegalStateException("Parsed for body did not lower break/continue IR");
        }

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "ParsedForBreakContinueService");
        Class<?> defined = new ByteArrayClassLoader().define("ParsedForBreakContinueService", classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();

        Object seven = defined.getDeclaredMethod("sum", double.class).invoke(instance, 7.0d);
        Object three = defined.getDeclaredMethod("sum", double.class).invoke(instance, 3.0d);
        if (!Double.valueOf(8.0d).equals(seven) || !Double.valueOf(1.0d).equals(three)) {
            throw new IllegalStateException("Unexpected parsed for/break/continue results: " + seven + ", " + three);
        }

        System.out.println("QinJvmParsedForBreakContinueSmokeTestMain passed.");
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
