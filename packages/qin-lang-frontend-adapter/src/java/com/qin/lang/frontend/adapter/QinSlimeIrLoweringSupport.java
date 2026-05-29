package com.qin.lang.frontend.adapter;

/**
 * Shared lowering state holder for the staged migration away from the legacy
 * Slime-named adapter façade.
 */
abstract class QinSlimeIrLoweringSupport {
    protected final QinLoweringContext loweringContext = new QinLoweringContext();
    protected int functionModelBudgetRemaining = 0;
    protected int currentSourceLength = 0;
}
