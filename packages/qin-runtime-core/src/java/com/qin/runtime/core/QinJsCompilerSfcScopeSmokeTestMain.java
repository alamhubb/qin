package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Map;

public final class QinJsCompilerSfcScopeSmokeTestMain {
    private QinJsCompilerSfcScopeSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("packages/qin-runtime-core/examples/fullstack-mvp")
                .toAbsolutePath()
                .normalize();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                var id = { exports: {} };
                var hasRequiredId;

                function requireId() {
                  if (hasRequiredId) return id.exports;
                  hasRequiredId = 1;
                  (function (module, exports) {
                    exports.__esModule = true;
                    exports["default"] = "id-default";
                    module.exports = exports.default;
                  } (id, id.exports));
                  return id.exports;
                }

                var parser = { exports: {} };
                var hasRequiredParser;

                function requireParser() {
                  if (hasRequiredParser) return parser.exports;
                  hasRequiredParser = 1;
                  (function (module, exports) {
                    exports.__esModule = true;
                    var _id = requireId();
                    exports["default"] = _id.default || _id;
                  } (parser, parser.exports));
                  return parser.exports;
                }

                var constructors = {};
                var hasRequiredConstructors;

                function requireConstructors() {
                  if (hasRequiredConstructors) return constructors;
                  hasRequiredConstructors = 1;
                  var _id = requireId();
                  var id = function id(opts) {
                    return _id.default || opts;
                  };
                  constructors.id = id;
                  return constructors;
                }

                var dist = { exports: {} };

                function requireDist() {
                  (function (module, exports) {
                    var _parser = requireParser();
                    var _constructors = requireConstructors();
                    exports.parser = _parser.default;
                    exports.ctor = _constructors.id("ctor-fallback");
                  } (dist, dist.exports));
                  return dist.exports;
                }

                requireDist();
                """, "js_compiler_sfc_scope");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected result map, got: " + result);
        }
        if (!"id-default".equals(map.get("parser")) || !"ctor-fallback".equals(map.get("ctor"))) {
            throw new IllegalStateException("Expected parser id-default and ctor fallback values, got: " + map);
        }
        System.out.println("QinJsCompilerSfcScopeSmokeTestMain OK");
    }
}
