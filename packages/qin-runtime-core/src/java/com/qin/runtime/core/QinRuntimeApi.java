package com.qin.runtime.core;

import java.nio.file.Path;

/**
 * Shared runtime API used by both CLI and Java launchers.
 */
public final class QinRuntimeApi {
    private QinRuntimeApi() {
    }

    public static void runFullstack(Path root, int port, boolean devMode) throws Exception {
        if (root == null) {
            throw new IllegalArgumentException("root cannot be null");
        }
        if (port <= 0) {
            throw new IllegalArgumentException("port must be > 0");
        }
        if (devMode) {
            QinFullstackMain.main(new String[] {
                    "--dev",
                    "--root", root.toAbsolutePath().normalize().toString(),
                    "--port", String.valueOf(port)
            });
            return;
        }
        QinFullstackMain.main(new String[] {
                "--root", root.toAbsolutePath().normalize().toString(),
                "--port", String.valueOf(port)
        });
    }
}

