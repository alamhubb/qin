package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFrontendOvsEsmServiceSmokeTestMain {
    private QinFrontendOvsEsmServiceSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-frontend-ovs-");
        Files.writeString(root.resolve("qin.config.json"), """
                {
                  "name": "qin-frontend-ovs-smoke",
                  "dependencies": {
                    "ovs-compiler": "0.2.2",
                    "ovsjs": "0.2.2",
                    "vue": "latest",
                    "cssts-compiler": "0.2.87",
                    "cssts-ts": "0.2.87"
                  }
                }
                """, StandardCharsets.UTF_8);
        Path appDir = root.resolve("app");
        Files.createDirectories(appDir);
        Path ovsFile = appDir.resolve("OvsDemo.ovs");
        Files.writeString(ovsFile, """
                div(class = css { colorBlue, fontWeight700, padding16px }) {
                  "Hello Qin OVS frontend"
                }
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, ovsFile);
        String bootstrap = service.bootstrapJs();
        if (!bootstrap.contains("/@qin-mod/app/OvsDemo.ovs.js")) {
            throw new IllegalStateException("OVS bootstrap did not point at .ovs.js:\n" + bootstrap);
        }

        String module = service.transpileByRequestPath("/@qin-mod/app/OvsDemo.ovs.js");
        if (module == null
                || !module.contains("__qinMountOvs")
                || !module.contains("qin-ovs=runtime")
                || !module.contains("qin-vue-cssts=style")
                || !module.contains("$OvsHtmlTag")) {
            throw new IllegalStateException("OVS module did not include expected runtime wiring:\n" + module);
        }

        String cssModule = service.transpileByRequestPath("/@qin-mod/app/OvsDemo.ovs.js?qin-vue-cssts=style");
        if (cssModule == null || !cssModule.contains("color: blue")) {
            throw new IllegalStateException("OVS CSS virtual module missing colorBlue CSS:\n" + cssModule);
        }

        String atomModule = service.transpileByRequestPath("/@qin-mod/app/OvsDemo.ovs.js?qin-vue-cssts=atom");
        if (atomModule == null || !atomModule.contains("fontWeight700")) {
            throw new IllegalStateException("OVS atom virtual module missing fontWeight700:\n" + atomModule);
        }

        String ovsRuntime = service.transpileByRequestPath("/@qin-mod/app/OvsDemo.ovs.js?qin-ovs=runtime");
        if (ovsRuntime == null
                || !ovsRuntime.contains("defineOvsComponent")
                || !ovsRuntime.contains("defineReactiveExpression")
                || ovsRuntime.contains("from \"vue\"")) {
            throw new IllegalStateException("OVS runtime virtual module was not rewritten:\n" + ovsRuntime);
        }

        String vueRuntime = service.transpileByRequestPath("/@qin-mod/app/OvsDemo.ovs.js?qin-ovs=vue");
        if (vueRuntime == null || !vueRuntime.contains("createApp")) {
            throw new IllegalStateException("Vue browser runtime virtual module missing createApp.");
        }

        System.out.println("QinFrontendOvsEsmServiceSmokeTestMain passed.");
    }
}
