package com.qin.runtime.core;

import java.nio.file.Path;

public final class QinSubhutiAlternativePackageSmokeTestMain {
    private QinSubhutiAlternativePackageSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("packages/qin-runtime-core/examples/fullstack-mvp")
                .toAbsolutePath()
                .normalize();
        try {
            new QinJsPackageRunner().runModuleSource(root, """
                    import { Alternative } from "subhuti";
                    Alternative;
                    """, "subhuti_alternative_package");
        } catch (RuntimeException error) {
            if (String.valueOf(error.getMessage()).contains("Imported binding does not exist: Alternative from subhuti")) {
                System.out.println("QinSubhutiAlternativePackageSmokeTestMain OK");
                return;
            }
            throw error;
        }
        throw new IllegalStateException("Legacy Subhuti Alternative export should fail on the static route");
    }
}
