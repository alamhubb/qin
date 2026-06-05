package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsGlobalThisBuiltinConstructorSmokeTestMain {
    private QinJsGlobalThisBuiltinConstructorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-globalthis-builtin-constructor-");
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                const WeakMapCtor = globalThis.__qin_builtin_constructor__("WeakMap");
                const map = new WeakMapCtor();
                const key = {};
                map.set(key, "ok");
                export default map.has(key) && map.get(key) === "ok";
                """, "js_globalthis_builtin_constructor");
        if (!Boolean.TRUE.equals(result)) {
            throw new IllegalStateException("Expected globalThis.__qin_builtin_constructor__ to construct WeakMap, got: "
                    + result);
        }
        System.out.println("QinJsGlobalThisBuiltinConstructorSmokeTestMain OK");
    }
}
