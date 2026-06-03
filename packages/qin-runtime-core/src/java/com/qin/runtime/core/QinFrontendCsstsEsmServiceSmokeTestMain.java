package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFrontendCsstsEsmServiceSmokeTestMain {
    private QinFrontendCsstsEsmServiceSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-frontend-cssts-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "qin-frontend-cssts-smoke",
                  dependencies: {
                    "cssts-compiler": "0.2.87",
                    "cssts-ts": "0.2.87"
                  }
                }
                """, StandardCharsets.UTF_8);

        Path appDir = root.resolve("app");
        Files.createDirectories(appDir);
        Path mainFile = appDir.resolve("main.js");
        Files.writeString(mainFile, """
                import "./theme.cssts";
                export const ok = true;
                """, StandardCharsets.UTF_8);
        Files.writeString(appDir.resolve("theme.cssts"), """
                export const bannerStyle = css { displayFlex, colorBlue, padding16px };
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, mainFile);
        String mainModule = service.transpileByRequestPath("/@qin-mod/app/main.js");
        if (mainModule == null || !mainModule.contains("/@qin-mod/app/theme.cssts.js")) {
            throw new IllegalStateException("Main module did not rewrite .cssts import:\n" + mainModule);
        }

        String csstsModule = service.transpileByRequestPath("/@qin-mod/app/theme.cssts.js");
        if (csstsModule == null
                || !csstsModule.contains("qin-vue-cssts=style")
                || !csstsModule.contains("qin-vue-cssts=atom")
                || !csstsModule.contains("qin-vue-cssts=runtime")) {
            throw new IllegalStateException("CSSTS module missing virtual module wiring:\n" + csstsModule);
        }

        String cssModule = service.transpileByRequestPath("/@qin-mod/app/theme.cssts.js?qin-vue-cssts=style");
        if (cssModule == null || !cssModule.contains("data-qin-cssts") || !cssModule.contains("display")) {
            throw new IllegalStateException("CSSTS CSS virtual module missing generated CSS:\n" + cssModule);
        }

        String atomModule = service.transpileByRequestPath("/@qin-mod/app/theme.cssts.js?qin-vue-cssts=atom");
        if (atomModule == null || !atomModule.contains("displayFlex")) {
            throw new IllegalStateException("CSSTS atom virtual module missing displayFlex:\n" + atomModule);
        }

        String runtimeModule = service.transpileByRequestPath("/@qin-mod/app/theme.cssts.js?qin-vue-cssts=runtime");
        if (runtimeModule == null || !runtimeModule.contains("export")) {
            throw new IllegalStateException("CSSTS runtime virtual module missing exports:\n" + runtimeModule);
        }

        System.out.println("QinFrontendCsstsEsmServiceSmokeTestMain passed.");
    }
}
