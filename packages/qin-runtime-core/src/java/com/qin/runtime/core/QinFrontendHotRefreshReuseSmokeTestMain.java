package com.qin.runtime.core;

import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinFrontendHotRefreshReuseSmokeTestMain {
    private QinFrontendHotRefreshReuseSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-frontend-hot-refresh-");
        Path app = root.resolve("app");
        Files.createDirectories(app);
        Path entry = app.resolve("main.qin");
        Path panel = app.resolve("Panel.ovs");
        Files.writeString(entry, """
                import Panel from "./Panel.ovs"

                export default Panel
                """, StandardCharsets.UTF_8);
        Files.writeString(panel, """
                export default div {
                  'BeforeHotRefresh'
                }
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, entry);
        String before = service.transpileByRequestPath("/@qin-mod/app/Panel.ovs.js");
        requireContains(before, "BeforeHotRefresh", "initial OVS module");

        QinFullstackMain.BuildArtifacts artifacts = newBuildArtifacts(root, service);
        long beforeVersion = artifacts.version();

        Files.writeString(panel, """
                export default div {
                  'AfterHotRefresh'
                }
                """, StandardCharsets.UTF_8);
        if (!artifacts.applyFrontendHotRefresh(List.of(panel))) {
            throw new IllegalStateException("Existing OVS frontend module should hot refresh without full rebuild");
        }
        if (artifacts.frontendEsmService() != service) {
            throw new IllegalStateException("Frontend hot refresh must preserve the existing QinFrontendEsmService");
        }
        if (artifacts.version() <= beforeVersion) {
            throw new IllegalStateException("Frontend hot refresh must bump the dev runtime version");
        }

        String after = service.transpileByRequestPath("/@qin-mod/app/Panel.ovs.js");
        requireContains(after, "AfterHotRefresh", "refreshed OVS module");
        if (after.contains("BeforeHotRefresh")) {
            throw new IllegalStateException("Refreshed OVS module returned stale output:\n" + after);
        }

        Files.writeString(panel, """
                import Other from "./Other.ovs"

                export default div {
                  Other {}
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(app.resolve("Other.ovs"), """
                export default div {
                  'Other'
                }
                """, StandardCharsets.UTF_8);
        if (artifacts.applyFrontendHotRefresh(List.of(panel))) {
            throw new IllegalStateException("Changed frontend import graph must use the standard full rebuild path");
        }

        System.out.println("QinFrontendHotRefreshReuseSmokeTestMain OK");
    }

    private static QinFullstackMain.BuildArtifacts newBuildArtifacts(
            Path root,
            QinFrontendEsmService service) throws Exception {
        Constructor<QinFullstackMain.BuildArtifacts> constructor =
                QinFullstackMain.BuildArtifacts.class.getDeclaredConstructor(
                        Path.class,
                        Path.class,
                        java.lang.reflect.Method.class,
                        java.lang.reflect.Method.class,
                        QinFrontendEsmService.class);
        constructor.setAccessible(true);
        return constructor.newInstance(root, root, null, null, service);
    }

    private static void requireContains(String source, String marker, String label) {
        if (source == null || !source.contains(marker)) {
            throw new IllegalStateException("Expected " + label + " to contain " + marker + ", got:\n" + source);
        }
    }
}
