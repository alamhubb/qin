package com.qin.lang.lowering.jvm;

import com.qin.lang.ir.QinBuiltinRegistry;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrConsoleLogValue;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.sema.esm.QinEsmImportBinding;
import com.qin.lang.sema.esm.QinEsmModuleSemantic;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Shared strict lowering gate for both JVM and JS targets.
 * The current implementation keeps IR unchanged but validates semantic invariants
 * so both backends fail fast with consistent messages.
 */
public final class QinStrictEsmJvmLowerer implements QinEsmJvmLowerer {
    @Override
    public QinIrProgram lower(
            QinIrProgram program,
            QinEsmSemanticModel semanticModel,
            QinEsmJvmLoweringContext context) {
        Objects.requireNonNull(program, "program cannot be null");
        Objects.requireNonNull(semanticModel, "semanticModel cannot be null");
        Objects.requireNonNull(context, "context cannot be null");

        for (QinEsmModuleSemantic module : semanticModel.modules().values()) {
            validateUniqueLocalImports(module);
        }
        validateBuiltinCalls(program.consoleValueLogs());
        return program;
    }

    private void validateUniqueLocalImports(QinEsmModuleSemantic module) {
        Set<String> locals = new HashSet<>();
        for (QinEsmImportBinding binding : module.imports()) {
            if (binding.localName() == null || binding.localName().isBlank()) {
                continue;
            }
            if (!locals.add(binding.localName())) {
                throw new IllegalArgumentException(
                        "ESM3101 duplicate local import binding: " + binding.localName()
                                + " at " + module.sourceFile().toAbsolutePath());
            }
        }
    }

    private void validateBuiltinCalls(Iterable<QinIrConsoleLogValue> logs) {
        for (QinIrConsoleLogValue log : logs) {
            validateExpression(log.value());
        }
    }

    private void validateExpression(QinIrExpression expression) {
        if (expression instanceof QinIrBuiltinCallExpression builtinCall) {
            validateBuiltinCall(builtinCall);
            for (QinIrExpression argument : builtinCall.arguments()) {
                validateExpression(argument);
            }
            return;
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            for (QinIrObjectProperty property : objectLiteral.properties()) {
                validateExpression(property.value());
            }
            return;
        }
        if (expression instanceof QinIrMemberAccessExpression || expression instanceof QinIrStringLiteral) {
            return;
        }
    }

    private void validateBuiltinCall(QinIrBuiltinCallExpression call) {
        QinBuiltinRegistry.BuiltinMethod builtinMethod = QinBuiltinRegistry
                .resolve(call.receiverName(), call.methodName(), call.arguments().size())
                .orElseThrow(() -> new IllegalArgumentException(
                        "QJS1001 unknown builtin call: "
                                + call.receiverName() + "." + call.methodName()
                                + "/" + call.arguments().size()));

        for (int i = 0; i < builtinMethod.argumentKinds().size(); i++) {
            QinBuiltinRegistry.BuiltinArgKind kind = builtinMethod.argumentKinds().get(i);
            QinIrExpression argument = call.arguments().get(i);
            if (kind == QinBuiltinRegistry.BuiltinArgKind.STRING && !(argument instanceof QinIrStringLiteral)) {
                throw new IllegalArgumentException(
                        "QJS1003 builtin argument type mismatch at index " + i + " for "
                                + call.receiverName() + "." + call.methodName() + ": expected string");
            }
        }
    }
}
