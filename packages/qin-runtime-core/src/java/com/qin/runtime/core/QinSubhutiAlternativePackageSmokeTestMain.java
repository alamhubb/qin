package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Map;

public final class QinSubhutiAlternativePackageSmokeTestMain {
    private QinSubhutiAlternativePackageSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("packages/qin-runtime-core/examples/fullstack-mvp")
                .toAbsolutePath()
                .normalize();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { Alternative } from "subhuti";
                const alt = Alternative.of(() => 7);
                ({
                  hasAlt: !!alt.alt,
                  value: alt.alt(),
                  ctor: !!alt.constructor
                });
                """, "subhuti_alternative_package");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!Boolean.TRUE.equals(map.get("hasAlt"))
                || !(map.get("value") instanceof Number value)
                || value.intValue() != 7) {
            throw new IllegalStateException("Subhuti Alternative did not expose alt correctly: " + map);
        }
        System.out.println("QinSubhutiAlternativePackageSmokeTestMain OK");
    }
}
