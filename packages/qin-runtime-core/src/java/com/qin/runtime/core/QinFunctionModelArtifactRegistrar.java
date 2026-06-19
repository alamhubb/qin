package com.qin.runtime.core;

import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrFunctionLiteral;
import com.qin.lang.ir.QinIrFunctionModelArtifact;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.runtime.QinFunctionModelRegistry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registers externalized function models for JVM runtime execution.
 */
public final class QinFunctionModelArtifactRegistrar {
    private QinFunctionModelArtifactRegistrar() {
    }

    public static void register(QinIrProgram program) {
        if (program == null || program.functionModelArtifacts().isEmpty()) {
            return;
        }
        for (QinIrFunctionModelArtifact artifact : program.functionModelArtifacts()) {
            QinFunctionModelRegistry.register(artifact.id(), () -> toRuntimeMap(artifact.ast()));
        }
    }

    private static Map<String, Object> toRuntimeMap(QinIrObjectLiteral objectLiteral) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (QinIrObjectProperty property : objectLiteral.properties()) {
            map.put(property.key(), toRuntimeValue(property.value()));
        }
        return map;
    }

    private static Object toRuntimeValue(QinIrExpression expression) {
        if (expression instanceof QinIrNullLiteral) {
            return null;
        }
        if (expression instanceof QinIrStringLiteral stringLiteral) {
            return stringLiteral.value();
        }
        if (expression instanceof QinIrNumberLiteral numberLiteral) {
            return numberLiteral.value();
        }
        if (expression instanceof QinIrBooleanLiteral booleanLiteral) {
            return booleanLiteral.value();
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            return toRuntimeMap(objectLiteral);
        }
        if (expression instanceof QinIrArrayLiteral arrayLiteral) {
            List<Object> values = new ArrayList<>();
            for (QinIrExpression element : arrayLiteral.elements()) {
                values.add(toRuntimeValue(element));
            }
            return values;
        }
        if (expression instanceof QinIrFunctionLiteral) {
            throw new IllegalArgumentException("Function model artifact cannot contain nested QinIrFunctionLiteral");
        }
        throw new IllegalArgumentException(
                "Unsupported function model artifact expression: " + expression.getClass().getName());
    }
}
