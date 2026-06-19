package com.qin.lang.frontend.adapter;

final class QinFunctionModelBudget {
    private QinFunctionModelBudget() {
    }

    static int compute(int sourceLength) {
        if (sourceLength <= 0) {
            return 120000;
        }
        if (sourceLength <= 25_000) {
            return 240000;
        }
        if (sourceLength <= 50_000) {
            return 80000;
        }
        if (sourceLength <= 80_000) {
            return 20000;
        }
        if (sourceLength <= 300_000) {
            return 12000;
        }
        if (sourceLength <= 500_000) {
            return 8000;
        }
        return 4000;
    }
}
