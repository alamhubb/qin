package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Map;

public final class QinVueCompilerSfcShimPackageSmokeTestMain {
    private QinVueCompilerSfcShimPackageSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("packages/qin-runtime-core/examples/fullstack-mvp")
                .toAbsolutePath()
                .normalize();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { parse, version } from "@vue/compiler-sfc";
                const parsed = parse("<template><div>Hi</div></template><script setup lang=\\"cssts\\">const msg = 'ok'</script>", { filename: "Demo.vue" });
                ({
                  version,
                  hasParse: typeof parse === "function",
                  scriptLang: parsed.descriptor.scriptSetup.lang,
                  templateText: parsed.descriptor.template.content.trim()
                });
                """, "vue_compiler_sfc_shim_package");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!Boolean.TRUE.equals(map.get("hasParse"))
                || !"cssts".equals(map.get("scriptLang"))
                || !"<div>Hi</div>".equals(map.get("templateText"))) {
            throw new IllegalStateException("@vue/compiler-sfc Qin shim did not parse SFC correctly: " + map);
        }
        System.out.println("QinVueCompilerSfcShimPackageSmokeTestMain OK");
    }
}
