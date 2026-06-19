package com.qin.lang.frontend.adapter;

public final class QinFunctionModelBudgetSmokeTestMain {
    private QinFunctionModelBudgetSmokeTestMain() {
    }

    public static void main(String[] args) {
        assertBudget(940, 240000);
        assertBudget(49_999, 80000);
        assertBudget(71_316, 20000);
        assertBudget(160_966, 12000);
        assertBudget(501_000, 4000);
        System.out.println("QinFunctionModelBudgetSmokeTestMain OK");
    }

    private static void assertBudget(int sourceLength, int expectedBudget) {
        int actual = QinFunctionModelBudget.compute(sourceLength);
        if (actual != expectedBudget) {
            throw new AssertionError(
                    "Unexpected function model budget for sourceLength="
                            + sourceLength
                            + ": expected="
                            + expectedBudget
                            + ", actual="
                            + actual);
        }
    }
}
