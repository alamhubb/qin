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
        if (sourceLength <= 80_000) {
            return 180000;
        }
        if (sourceLength <= 300_000) {
            return 140000;
        }
        if (sourceLength <= 500_000) {
            return 100000;
        }
        return 80000;
    }
}
