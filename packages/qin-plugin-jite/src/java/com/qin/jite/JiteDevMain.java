package com.qin.jite;

import com.qin.runtime.core.QinFullstackMain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Jite dev entry for Qin.
 * Delegates to QinFullstackMain with --dev enabled.
 */
public final class JiteDevMain {
    private JiteDevMain() {
    }

    public static void main(String[] args) throws Exception {
        List<String> forwarded = new ArrayList<>();
        if (!containsDevFlag(args)) {
            forwarded.add("--dev");
        }
        forwarded.addAll(Arrays.asList(args));
        QinFullstackMain.main(forwarded.toArray(String[]::new));
    }

    private static boolean containsDevFlag(String[] args) {
        for (String arg : args) {
            if ("--dev".equals(arg)) {
                return true;
            }
        }
        return false;
    }
}
