package com.qin.runtime.core;

import java.nio.file.Path;

public final class QinFullstackMvpFrontendSmokeTestMain {
    private QinFullstackMvpFrontendSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("packages", "qin-runtime-core", "examples", "fullstack-mvp").toAbsolutePath().normalize();
        QinFrontendEsmService service = QinFrontendEsmService.create(root, root.resolve("app/main.vue"));

        String mainModule = service.transpileByRequestPath("/@qin-mod/app/main.vue.js");
        if (mainModule == null
                || !mainModule.contains("import OvsDemo from \"/@qin-mod/app/OvsDemo.ovs.js\"")
                || !mainModule.contains("data-qin-component=\"OvsDemo\"")
                || !mainModule.contains("/@qin-mod/__virtual/cssts.css.js")) {
            throw new IllegalStateException("Fullstack MVP main Vue module missing OVS/CSSTS wiring:\n" + mainModule);
        }

        String ovsModule = service.transpileByRequestPath("/@qin-mod/app/OvsDemo.ovs.js");
        if (ovsModule == null
                || !ovsModule.contains("__qinMountOvs")
                || !ovsModule.contains("__qinMountVue")
                || !ovsModule.contains("__qinOvsDefault.__qinMountVue")
                || !ovsModule.contains("qin-ovs=runtime")
                || !ovsModule.contains("/@qin-mod/__virtual/cssts.css.js")) {
            throw new IllegalStateException("Fullstack MVP OVS module missing runtime wiring:\n" + ovsModule);
        }

        String styleModule = service.transpileByRequestPath("/@qin-mod/app/OvsDemo.ovs.js?qin-vue-cssts=style");
        if (styleModule == null
                || !styleModule.contains("padding: 16px")) {
            throw new IllegalStateException("Fullstack MVP OVS CSSTS module missing expected CSS:\n" + styleModule);
        }

        System.out.println("QinFullstackMvpFrontendSmokeTestMain passed.");
    }
}
