package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrBreakStatement;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrContinueStatement;
import com.qin.lang.ir.QinIrForEachStatement;
import com.qin.lang.ir.QinIrIfStatement;
import com.qin.lang.ir.QinIrProgram;

import java.util.List;

/**
 * Proves parsed Qin for...of method bodies lower to Qin for-each IR and
 * execute as JVM .class iterator bytecode.
 */
public final class QinJvmParsedForOfSmokeTestMain {
    private QinJvmParsedForOfSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                import { List } from 'java:java.util'

                class ParsedForOfService {
                  sum(values: List): number {
                    let total = 0
                    for (const item of values) {
                      if (item == 2) {
                        continue
                      }
                      if (item == 5) {
                        break
                      }
                      total = total + item
                    }
                    return total
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = requireClass(program, "ParsedForOfService");
        if (declaration.methods().size() != 1) {
            throw new IllegalStateException("Expected one parsed for...of method");
        }
        QinIrForEachStatement forEachStatement = declaration.methods().get(0).bodyStatements().stream()
                .filter(QinIrForEachStatement.class::isInstance)
                .map(QinIrForEachStatement.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Parsed method did not lower for...of statement IR"));
        if (!"item".equals(forEachStatement.itemName())) {
            throw new IllegalStateException("Parsed for...of item binding was not preserved");
        }
        boolean hasContinue = forEachStatement.body().stream()
                .filter(QinIrIfStatement.class::isInstance)
                .map(QinIrIfStatement.class::cast)
                .flatMap(statement -> statement.consequent().stream())
                .anyMatch(QinIrContinueStatement.class::isInstance);
        boolean hasBreak = forEachStatement.body().stream()
                .filter(QinIrIfStatement.class::isInstance)
                .map(QinIrIfStatement.class::cast)
                .flatMap(statement -> statement.consequent().stream())
                .anyMatch(QinIrBreakStatement.class::isInstance);
        if (!hasContinue || !hasBreak) {
            throw new IllegalStateException("Parsed for...of body did not lower break/continue IR");
        }

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "ParsedForOfService");
        Class<?> defined = new ByteArrayClassLoader().define("ParsedForOfService", classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();

        Object result = defined.getDeclaredMethod("sum", List.class).invoke(instance, List.of(1, 2, 3, 5, 8));
        if (!Double.valueOf(4.0d).equals(result)) {
            throw new IllegalStateException("Unexpected parsed for...of result: " + result);
        }

        System.out.println("QinJvmParsedForOfSmokeTestMain passed.");
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
