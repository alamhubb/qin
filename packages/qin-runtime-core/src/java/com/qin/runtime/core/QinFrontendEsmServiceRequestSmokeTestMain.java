package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFrontendEsmServiceRequestSmokeTestMain {
    private QinFrontendEsmServiceRequestSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = resolveFullstackMvpRoot();
        Path entry = root.resolve("app/main.vue").toAbsolutePath().normalize();
        QinFrontendEsmService service = QinFrontendEsmService.create(root, entry);

        String module = service.transpileByRequestPath("/@qin-mod/app/main.vue.js");
        if (module == null || !module.contains("__qinMountVue") || !module.contains("Hello from .vue")) {
            throw new IllegalStateException("Expected Vue module for /@qin-mod/app/main.vue.js, got:\n" + module);
        }

        String style = service.transpileByRequestPath("/@qin-mod/app/main.vue.js?qin-vue-cssts=style");
        if (style == null || !style.contains("data-qin-cssts")) {
            throw new IllegalStateException("Expected CSSTS style virtual module, got:\n" + style);
        }

        System.out.println("QinFrontendEsmServiceRequestSmokeTestMain passed.");
    }

    private static Path resolveFullstackMvpRoot() {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[] {
                cwd.resolve("packages/qin-runtime-core/examples/fullstack-mvp"),
                cwd.resolve("qin/packages/qin-runtime-core/examples/fullstack-mvp"),
                cwd.resolve("examples/fullstack-mvp")
        };
        for (Path candidate : candidates) {
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Cannot locate examples/fullstack-mvp directory.");
    }
}
