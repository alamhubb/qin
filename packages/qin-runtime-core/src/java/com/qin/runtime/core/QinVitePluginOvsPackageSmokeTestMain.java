package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Map;

public final class QinVitePluginOvsPackageSmokeTestMain {
    private QinVitePluginOvsPackageSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("packages/qin-runtime-core/examples/fullstack-mvp")
                .toAbsolutePath()
                .normalize();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import vitePluginOvs from "vite-plugin-ovs";
                const plugins = vitePluginOvs({ cssts: { classPrefix: "cmp-" } }).flat();
                ({
                  pluginNames: plugins.map(plugin => plugin && plugin.name).join(","),
                  hasOvsTransform: !!plugins.find(plugin => plugin && plugin.name === "vite-plugin-ovs" && plugin.transform),
                  hasCsstsLoad: !!plugins.find(plugin => plugin && plugin.name === "vite-plugin-cssts" && plugin.load)
                });
                """, "vite_plugin_ovs_package");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!String.valueOf(map.get("pluginNames")).contains("vite-plugin-ovs")
                || !Boolean.TRUE.equals(map.get("hasOvsTransform"))
                || !Boolean.TRUE.equals(map.get("hasCsstsLoad"))) {
            throw new IllegalStateException("Qin did not load vite-plugin-ovs plugin array correctly: " + map);
        }
        System.out.println("QinVitePluginOvsPackageSmokeTestMain OK");
    }
}
