package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFullstackVueCsstsFrontendSmokeTestMain {
    private QinFullstackVueCsstsFrontendSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = resolveFullstackMvpRoot();
        QinFrontendEsmService service = QinFrontendEsmService.create(root, root.resolve("app/main.vue"));

        String bootstrap = service.bootstrapJs();
        if (!bootstrap.contains("/@qin-mod/app/main.vue.js")) {
            throw new IllegalStateException("Unexpected bootstrap js: " + bootstrap);
        }

        String module = service.transpileByRequestPath("/@qin-mod/app/main.vue.js");
        if (module == null || !module.contains("__qinMountVue")) {
            throw new IllegalStateException("Vue module did not emit mount code:\n" + module);
        }
        if (module.length() > 64_000) {
            throw new IllegalStateException(
                    "Vue module should expose a sanitized SFC descriptor, but generated "
                            + module.length()
                            + " chars. This usually means compiler internals leaked into descriptor JSON.");
        }
        if (!module.contains("/@qin-mod/__virtual/cssts.css.js")
                || !module.contains("/@qin-mod/__virtual/csstsAtom.js")) {
            throw new IllegalStateException("Vue module did not reference cssts virtual modules:\n" + module);
        }
        if (!module.contains("/@qin-mod/__virtual/cssts-runtime.js") || module.contains("from \"cssts-ts\"")) {
            throw new IllegalStateException("Vue module did not rewrite cssts-ts runtime import:\n" + module);
        }

        String cssModule = service.transpileByRequestPath("/@qin-mod/__virtual/cssts.css.js");
        if (cssModule == null || !cssModule.contains("data-qin-cssts") || !cssModule.contains("background-color")) {
            throw new IllegalStateException("Cssts CSS virtual module did not expose injectable CSS:\n" + cssModule);
        }
        String runtimeModule = service.transpileByRequestPath("/@qin-mod/__virtual/cssts-runtime.js");
        if (runtimeModule == null || !runtimeModule.contains("export") || !runtimeModule.contains("merge")) {
            throw new IllegalStateException("Cssts runtime virtual module did not expose cssts-ts runtime:\n" + runtimeModule);
        }

        System.out.println("QinFullstackVueCsstsFrontendSmokeTestMain passed.");
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
