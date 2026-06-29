package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrAssignmentExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrLocalDeclarationStatement;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStatementExpression;
import com.qin.lang.ir.QinIrWhileStatementNode;

/**
 * Proves parsed Qin mutable locals and assignments execute through JVM .class
 * while-loop bytecode, not through runtime fallback interpretation.
 */
public final class QinJvmParsedWhileMutableLocalSmokeTestMain {
    private QinJvmParsedWhileMutableLocalSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class ParsedWhileMutableLocalService {
                  count(limit: number): number {
                    let total = 0
                    while (total < limit) {
                      total = total + 1
                    }
                    return total
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = requireClass(program, "ParsedWhileMutableLocalService");
        if (declaration.methods().size() != 1) {
            throw new IllegalStateException("Expected one parsed mutable while method");
        }
        var statements = declaration.methods().get(0).bodyStatements();
        if (statements.stream().noneMatch(QinIrLocalDeclarationStatement.class::isInstance)) {
            throw new IllegalStateException("Parsed mutable while method did not lower local declaration IR");
        }
        QinIrWhileStatementNode whileStatement = statements.stream()
                .filter(QinIrWhileStatementNode.class::isInstance)
                .map(QinIrWhileStatementNode.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Parsed mutable while method did not lower while IR"));
        boolean hasAssignment = whileStatement.body().stream()
                .filter(QinIrStatementExpression.class::isInstance)
                .map(QinIrStatementExpression.class::cast)
                .anyMatch(statement -> statement.expression() instanceof QinIrAssignmentExpression);
        if (!hasAssignment) {
            throw new IllegalStateException("Parsed mutable while method did not lower assignment IR");
        }

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "ParsedWhileMutableLocalService");
        Class<?> defined = new ByteArrayClassLoader().define("ParsedWhileMutableLocalService", classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();

        Object three = defined.getDeclaredMethod("count", double.class).invoke(instance, 3.0d);
        Object zero = defined.getDeclaredMethod("count", double.class).invoke(instance, 0.0d);
        if (!Double.valueOf(3.0d).equals(three) || !Double.valueOf(0.0d).equals(zero)) {
            throw new IllegalStateException("Unexpected parsed mutable while results: " + three + ", " + zero);
        }

        System.out.println("QinJvmParsedWhileMutableLocalSmokeTestMain passed.");
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
