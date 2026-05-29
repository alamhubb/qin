package com.qin.runtime.core;

/**
 * Stage-1 canonical dev/fullstack server entry for Qin.
 *
 * <p>This class exists to make the single-process server role explicit in the
 * runtime surface. The current implementation reuses the proven
 * {@link QinFullstackMain} behavior while the internal runtime/server structure
 * is gradually split into dedicated Qin-owned components.
 */
public final class QinDevServerMain {

    private QinDevServerMain() {
    }

    public static void main(String[] args) throws Exception {
        QinFullstackMain.main(args);
    }
}
