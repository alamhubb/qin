package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFrontendTsJsAliasSmokeTestMain {
    private QinFrontendTsJsAliasSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-frontend-ts-js-alias-");
        Path src = Files.createDirectories(root.resolve("src"));
        Path main = src.resolve("main.ts");
        Files.writeString(main, """
                export const message = "hello from ts alias";
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, main);
        String canonical = service.transpileByRequestPath("/@qin-mod/src/main.ts");
        String jsAlias = service.transpileByRequestPath("/@qin-mod/src/main.ts.js");

        if (canonical == null || !canonical.contains("hello from ts alias")) {
            throw new IllegalStateException("Canonical .ts module request failed:\n" + canonical);
        }
        if (jsAlias == null || !jsAlias.contains("hello from ts alias")) {
            throw new IllegalStateException(".ts.js module alias request failed:\n" + jsAlias);
        }

        System.out.println("QinFrontendTsJsAliasSmokeTestMain passed.");
    }
}
