package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinVueMinimalFrontendSmokeTestMain {
    private QinVueMinimalFrontendSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = resolveVueMinimalRoot();
        new QinNpmDependencyMaterializer().materializeProjectDependencies(root, root.resolve("node_modules"));

        QinFrontendEsmService service = QinFrontendEsmService.create(root, root.resolve("app/main.vue"));

        String bootstrap = service.bootstrapJs();
        if (!bootstrap.contains("/@qin-mod/app/main.vue.js")) {
            throw new IllegalStateException("Vue minimal bootstrap did not point at the SFC module:\n" + bootstrap);
        }

        String module = service.transpileByRequestPath("/@qin-mod/app/main.vue.js");
        if (module == null
                || !module.contains("__qinMountVue")
                || !module.contains("Qin Vue without Vite")
                || !module.contains("Hello from Qin Vue Minimal")
                || !module.contains("qin-vue-cssts=style")) {
            throw new IllegalStateException("Vue minimal module missing Qin/Vue/CSSTS wiring:\n" + module);
        }

        String hmrModule = service.transpileByRequestPath("/@qin-mod/app/main.vue.js?qin-hmr=123");
        if (hmrModule == null || !hmrModule.contains("Hello from Qin Vue Minimal")) {
            throw new IllegalStateException("Vue minimal HMR module request did not resolve:\n" + hmrModule);
        }

        String style = service.transpileByRequestPath("/@qin-mod/app/main.vue.js?qin-vue-cssts=style");
        if (style == null
                || !style.contains("data-qin-cssts")
                || !style.contains("display: flex")
                || !style.contains("min-height: 100vh")) {
            throw new IllegalStateException("Vue minimal CSSTS style module missing expected CSS:\n" + style);
        }

        String hmrStyle = service.transpileByRequestPath("/@qin-mod/app/main.vue.js?qin-vue-cssts=style&qin-hmr=123");
        if (hmrStyle == null || !hmrStyle.contains("min-height: 100vh")) {
            throw new IllegalStateException("Vue minimal HMR style request did not resolve:\n" + hmrStyle);
        }

        System.out.println("QinVueMinimalFrontendSmokeTestMain passed.");
    }

    private static Path resolveVueMinimalRoot() {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[] {
                cwd.resolve("packages/qin-runtime-core/examples/vue-minimal"),
                cwd.resolve("qin/packages/qin-runtime-core/examples/vue-minimal"),
                cwd.resolve("examples/vue-minimal")
        };
        for (Path candidate : candidates) {
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Cannot locate examples/vue-minimal directory.");
    }
}
