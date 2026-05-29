package com.qin.runtime.core;

import java.net.URI;

public final class QinDevServerQueryModuleSmokeTestMain {
    private QinDevServerQueryModuleSmokeTestMain() {
    }

    public static void main(String[] args) {
        String requestPath = QinDevServer.frontendModuleRequestPath(
                URI.create("/@qin-mod/app/Cssts.js?qin-vue-cssts=style"));
        if (!"/@qin-mod/app/Cssts.js?qin-vue-cssts=style".equals(requestPath)) {
            throw new IllegalStateException("Frontend module request query was not preserved: " + requestPath);
        }

        String plainPath = QinDevServer.frontendModuleRequestPath(
                URI.create("/@qin-mod/app/main.js"));
        if (!"/@qin-mod/app/main.js".equals(plainPath)) {
            throw new IllegalStateException("Plain frontend module request changed unexpectedly: " + plainPath);
        }

        System.out.println("QinDevServerQueryModuleSmokeTestMain passed.");
    }
}
