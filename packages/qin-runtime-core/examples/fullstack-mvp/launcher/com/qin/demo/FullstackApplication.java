package com.qin.demo;

import com.qin.runtime.core.QinFullstackMain;

/**
 * Convention startup class for the fullstack MVP project.
 * Run this class directly, no command-line args required.
 */
public final class FullstackApplication {
    private FullstackApplication() {
    }

    public static void main(String[] args) throws Exception {
        QinFullstackMain.main(new String[] { "--dev" });
    }
}
