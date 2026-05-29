package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrFunctionModelArtifact;
import com.qin.lang.ir.QinIrObjectLiteral;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Shared mutable lowering context for staged migration away from adapter-owned
 * implicit state.
 */
final class QinLoweringContext {
    private static final AtomicLong ARTIFACT_SEQUENCE = new AtomicLong();

    private int sourceLength;
    private int functionModelBudgetRemaining;
    private String functionModelArtifactPrefix = "qin-fn-" + ARTIFACT_SEQUENCE.incrementAndGet();
    private int functionModelArtifactCounter;
    private final List<QinIrFunctionModelArtifact> functionModelArtifacts = new ArrayList<>();

    int sourceLength() {
        return sourceLength;
    }

    void setSourceLength(int sourceLength) {
        this.sourceLength = Math.max(0, sourceLength);
    }

    int functionModelBudgetRemaining() {
        return functionModelBudgetRemaining;
    }

    void setFunctionModelBudgetRemaining(int functionModelBudgetRemaining) {
        this.functionModelBudgetRemaining = Math.max(0, functionModelBudgetRemaining);
    }

    void resetFunctionModelArtifacts() {
        functionModelArtifactPrefix = "qin-fn-" + ARTIFACT_SEQUENCE.incrementAndGet();
        functionModelArtifactCounter = 0;
        functionModelArtifacts.clear();
    }

    String addFunctionModelArtifact(QinIrObjectLiteral ast) {
        String id = functionModelArtifactPrefix + "-" + (++functionModelArtifactCounter);
        functionModelArtifacts.add(new QinIrFunctionModelArtifact(id, ast));
        return id;
    }

    List<QinIrFunctionModelArtifact> functionModelArtifacts() {
        return List.copyOf(functionModelArtifacts);
    }
}
