package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrBreakStatement;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrContinueStatement;
import com.qin.lang.ir.QinIrDoWhileStatementNode;
import com.qin.lang.ir.QinIrIfStatement;
import com.qin.lang.ir.QinIrProgram;

/**
 * Proves parsed Qin do-while method bodies execute as JVM .class loop bytecode.
 */
public final class QinJvmParsedDoWhileSmokeTestMain {
    private QinJvmParsedDoWhileSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class ParsedDoWhileService {
                  sum(limit: number): number {
                    let total = 0
                    let i = 0
                    do {
                      i = i + 1
                      if (i == 2) {
                        continue
                      }
                      if (i == 5) {
                        break
                      }
                      total = total + i
                    } while (i < limit)
                    return total
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = requireClass(program, "ParsedDoWhileService");
        if (declaration.methods().size() != 1) {
            throw new IllegalStateException("Expected one parsed do-while method");
        }
        QinIrDoWhileStatementNode doWhileStatement = declaration.methods().get(0).bodyStatements().stream()
                .filter(QinIrDoWhileStatementNode.class::isInstance)
                .map(QinIrDoWhileStatementNode.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Parsed method did not lower do-while statement IR"));
        boolean hasContinue = doWhileStatement.body().stream()
                .filter(QinIrIfStatement.class::isInstance)
                .map(QinIrIfStatement.class::cast)
                .flatMap(statement -> statement.consequent().stream())
                .anyMatch(QinIrContinueStatement.class::isInstance);
        boolean hasBreak = doWhileStatement.body().stream()
                .filter(QinIrIfStatement.class::isInstance)
                .map(QinIrIfStatement.class::cast)
                .flatMap(statement -> statement.consequent().stream())
                .anyMatch(QinIrBreakStatement.class::isInstance);
        if (!hasContinue || !hasBreak) {
            throw new IllegalStateException("Parsed do-while body did not lower break/continue IR");
        }

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "ParsedDoWhileService");
        Class<?> defined = new ByteArrayClassLoader().define("ParsedDoWhileService", classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();

        Object seven = defined.getDeclaredMethod("sum", double.class).invoke(instance, 7.0d);
        Object zero = defined.getDeclaredMethod("sum", double.class).invoke(instance, 0.0d);
        if (!Double.valueOf(8.0d).equals(seven) || !Double.valueOf(1.0d).equals(zero)) {
            throw new IllegalStateException("Unexpected parsed do-while results: " + seven + ", " + zero);
        }

        System.out.println("QinJvmParsedDoWhileSmokeTestMain passed.");
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
