package com.qin.runtime.core;

import java.nio.file.Path;

public final class QinJsCjsWrapperExportsSmokeTestMain {
    private QinJsCjsWrapperExportsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("packages/qin-runtime-core/examples/fullstack-mvp")
                .toAbsolutePath()
                .normalize();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                var id = { exports: {} };
                (function (module, exports) {
                  exports.__esModule = true;
                  exports["default"] = "ok";
                } (id, id.exports));
                id.exports.default;
                """, "js_cjs_wrapper_exports");
        if (!"ok".equals(result)) {
            throw new IllegalStateException("Expected CJS wrapper export to be ok, got: " + result);
        }
        System.out.println("QinJsCjsWrapperExportsSmokeTestMain OK");
    }
}
