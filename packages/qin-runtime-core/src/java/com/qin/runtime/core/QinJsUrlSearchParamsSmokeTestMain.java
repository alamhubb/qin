package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Map;

public final class QinJsUrlSearchParamsSmokeTestMain {
    private QinJsUrlSearchParamsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("packages/qin-runtime-core/examples/fullstack-mvp")
                .toAbsolutePath()
                .normalize();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                const query = Object.fromEntries(new URLSearchParams("vue&type=template&index=0"));
                query.vue = query.vue != null;
                query.index = Number(query.index);
                query;
                """, "js_url_search_params");
        if (!(result instanceof Map<?, ?> map)
                || !Boolean.TRUE.equals(map.get("vue"))
                || !"template".equals(map.get("type"))
                || !Double.valueOf(0).equals(map.get("index"))) {
            throw new IllegalStateException("Expected parsed URLSearchParams query, got: " + result);
        }
        System.out.println("QinJsUrlSearchParamsSmokeTestMain OK");
    }
}
