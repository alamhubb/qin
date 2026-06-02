package com.qin.runtime.core;

import java.nio.file.Path;

public final class QinJsNamedFunctionExpressionScopeSmokeTestMain {
    private QinJsNamedFunctionExpressionScopeSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("packages/qin-runtime-core/examples/fullstack-mvp")
                .toAbsolutePath()
                .normalize();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                var id = { exports: {} };
                function requireId() {
                  (function (module, exports) {
                    exports["default"] = "ok";
                  } (id, id.exports));
                  return id.exports;
                }
                function requireConstructors() {
                  var id = function id(opts) {
                    return opts;
                  };
                  return id;
                }
                requireConstructors();
                requireId().default;
                """, "js_named_function_expression_scope");
        if (!"ok".equals(result)) {
            throw new IllegalStateException("Expected outer id module to survive named function expression, got: " + result);
        }
        System.out.println("QinJsNamedFunctionExpressionScopeSmokeTestMain OK");
    }
}
