package com.qin.runtime.core;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinFrontendPublicOvsModuleSmokeTestMain {
    private QinFrontendPublicOvsModuleSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-frontend-public-ovs-");
        Path app = root.resolve("app");
        Files.createDirectories(app);
        Files.writeString(app.resolve("main.qin"), """
                import Panel from "./Panel.ovs"
                import "./Broken.ovs"

                export default Panel
                """, StandardCharsets.UTF_8);
        Files.writeString(app.resolve("Panel.ovs"), """
                export default div {
                  'Panel'
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(app.resolve("Broken.ovs"), """
                export default div(
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, app.resolve("main.qin"));
        assertNoPathOnlyOvsCache(service, app.resolve("Panel.ovs"), "create");
        assertNoToolchainAwareOvsCache(service, app.resolve("Panel.ovs"), "create");
        assertNoToolchainAwareOvsCache(service, app.resolve("Broken.ovs"), "create");

        String publicOvsModule = service.transpileByPublicRequestPath("/app/Panel.ovs.js");
        if (publicOvsModule == null
                || publicOvsModule.contains("<!doctype html>")
                || !publicOvsModule.contains("export default __qinVueComponent")
                || !publicOvsModule.contains("?qin-vue-cssts=style")
                || !publicOvsModule.contains("?qin-vue-cssts=atom")) {
            throw new IllegalStateException("Public OVS .js request did not return a browser module:\n" + publicOvsModule);
        }
        assertNoPathOnlyOvsCache(service, app.resolve("Panel.ovs"), "public request");
        assertToolchainAwareOvsCache(service, app.resolve("Panel.ovs"), "public request");
        assertNoToolchainAwareOvsCache(service, app.resolve("Broken.ovs"), "public request for valid module");

        try {
            service.transpileByPublicRequestPath("/app/Broken.ovs.js");
            throw new IllegalStateException("Invalid OVS module must fail when requested, not during service create");
        } catch (IllegalStateException expected) {
            if (!String.valueOf(expected.getMessage()).contains("Qin OVS compilation failed")) {
                throw expected;
            }
        }

        String styleModule = service.transpileByRequestPath("/@qin-mod/app/Panel.ovs.js?qin-vue-cssts=style");
        if (styleModule == null
                || !styleModule.contains("export default css")) {
            throw new IllegalStateException("Empty OVS style virtual module must still resolve:\n" + styleModule);
        }

        String atomModule = service.transpileByRequestPath("/@qin-mod/app/Panel.ovs.js?qin-vue-cssts=atom");
        if (atomModule == null
                || !atomModule.contains("export const csstsAtom = {}")
                || !atomModule.contains("export default csstsAtom")) {
            throw new IllegalStateException("Empty OVS atom virtual module must still resolve:\n" + atomModule);
        }

        System.out.println("QinFrontendPublicOvsModuleSmokeTestMain passed.");
    }

    private static void assertNoPathOnlyOvsCache(
            QinFrontendEsmService service,
            Path ovsModule,
            String phase) throws Exception {
        Field cacheField = QinFrontendEsmService.class.getDeclaredField("transpiledModuleCache");
        cacheField.setAccessible(true);
        Map<?, ?> cache = (Map<?, ?>) cacheField.get(service);
        Path normalized = ovsModule.toAbsolutePath().normalize();
        if (cache.containsKey(normalized)) {
            throw new IllegalStateException(
                    "OVS modules must not be stored in the frontend path-only cache after " + phase);
        }
    }

    private static void assertToolchainAwareOvsCache(
            QinFrontendEsmService service,
            Path ovsModule,
            String phase) throws Exception {
        Field cacheField = QinFrontendEsmService.class.getDeclaredField("ovsTranspiledModuleCache");
        cacheField.setAccessible(true);
        Map<?, ?> cache = (Map<?, ?>) cacheField.get(service);
        Path normalized = ovsModule.toAbsolutePath().normalize();
        Object entry = cache.get(normalized);
        if (entry == null) {
            throw new IllegalStateException("OVS modules must be cached with a toolchain-aware identity after " + phase);
        }
        var identityMethod = entry.getClass().getDeclaredMethod("cacheIdentity");
        identityMethod.setAccessible(true);
        Object identity = identityMethod.invoke(entry);
        if (!(identity instanceof String text) || text.isBlank() || !text.contains("toolchain=")) {
            throw new IllegalStateException("OVS frontend cache identity must include the OVS toolchain after " + phase);
        }
    }

    private static void assertNoToolchainAwareOvsCache(
            QinFrontendEsmService service,
            Path ovsModule,
            String phase) throws Exception {
        Field cacheField = QinFrontendEsmService.class.getDeclaredField("ovsTranspiledModuleCache");
        cacheField.setAccessible(true);
        Map<?, ?> cache = (Map<?, ?>) cacheField.get(service);
        Path normalized = ovsModule.toAbsolutePath().normalize();
        if (cache.containsKey(normalized)) {
            throw new IllegalStateException(
                    "OVS modules must be compiled on demand, not cached after " + phase + ": " + normalized);
        }
    }
}
