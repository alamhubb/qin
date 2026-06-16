package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class QinOvsCompiler {
    private static final int MAX_CACHE_ENTRIES = 64;

    private final QinJsPackageRunner packageRunner = new QinJsPackageRunner();
    private final Map<CacheKey, QinOvsCompileResult> cache = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<CacheKey, QinOvsCompileResult> eldest) {
            return size() > MAX_CACHE_ENTRIES;
        }
    };

    public QinOvsCompileResult compile(Path projectRoot, String source) throws Exception {
        CacheKey key = new CacheKey(projectRoot.toAbsolutePath().normalize(), source);
        synchronized (cache) {
            QinOvsCompileResult cached = cache.get(key);
            if (cached != null) {
                return cached;
            }
        }
        try {
            Object result = packageRunner.runModuleSource(
                    projectRoot,
                    buildWrapperSource(source),
                    "vite_plugin_ovs_transform");
            QinOvsCompileResult decoded = decodeResult(result);
            synchronized (cache) {
                cache.put(key, decoded);
            }
            return decoded;
        } catch (Exception error) {
            throw new IllegalStateException("Qin vite-plugin-ovs transform failed for " + projectRoot, error);
        }
    }

    private String buildWrapperSource(String source) {
        String sourceLiteral = QinJsPackageRunner.renderJsLiteral(source);
        return """
                import vitePluginOvs from "vite-plugin-ovs";
                const __qin_plugins__ = vitePluginOvs({ cssts: { classPrefix: "cmp-" } }).flat();
                const __qin_plugin__ = __qin_plugins__.find(plugin => plugin && plugin.name === "vite-plugin-ovs");
                if (!__qin_plugin__ || !__qin_plugin__.transform) {
                  throw new Error("vite-plugin-ovs transform hook not found");
                }
                const __qin_context__ = {
                  parse(code) { return {}; },
                  addWatchFile(file) {},
                  emitFile(file) { return "qin-ovs-file"; },
                  warn(message) {},
                  error(message) { throw new Error(String(message)); }
                };
                const __qin_source__ = %s;
                const __qin_id__ = "/qin/app/OvsDemo.ovs";
                let __qin_result__ = typeof __qin_plugin__.transform === "function"
                  ? __qin_plugin__.transform.call(__qin_context__, __qin_source__, __qin_id__)
                  : __qin_plugin__.transform.handler.call(__qin_context__, __qin_source__, __qin_id__);
                if (__qin_result__ && __qin_result__.then) {
                  __qin_result__.then(value => { __qin_result__ = value; });
                }
                const __qin_code__ = typeof __qin_result__ === "string" ? __qin_result__ : __qin_result__.code;
                const __qin_extract_atoms__ = (code) => {
                  const atoms = new Set();
                  const mergePattern = /cssts\\.merge\\(([^)]*)\\)/g;
                  let match;
                  while ((match = mergePattern.exec(code)) !== null) {
                    for (const raw of match[1].split(",")) {
                      const name = raw.trim();
                      if (/^[A-Za-z_$][\\w$]*$/.test(name)) {
                        atoms.add(name);
                      }
                    }
                  }
                  return atoms;
                };
                const __qin_cssts_plugin__ = __qin_plugins__.find(plugin => plugin && plugin.name === "vite-plugin-cssts");
                let __qin_css__ = "";
                let __qin_atom__ = "";
                if (__qin_cssts_plugin__ && __qin_cssts_plugin__.load) {
                  const __qin_atoms__ = __qin_extract_atoms__(__qin_code__);
                  if (__qin_cssts_plugin__.api && __qin_cssts_plugin__.api.RuntimeStore && __qin_cssts_plugin__.api.RuntimeStore.addUsedStyles) {
                    __qin_cssts_plugin__.api.RuntimeStore.addUsedStyles(__qin_atoms__);
                  }
                  const __qin_css_loaded__ = typeof __qin_cssts_plugin__.load === "function"
                    ? __qin_cssts_plugin__.load.call(__qin_context__, "\\0virtual:cssts.css")
                    : __qin_cssts_plugin__.load.handler.call(__qin_context__, "\\0virtual:cssts.css");
                  const __qin_atom_loaded__ = typeof __qin_cssts_plugin__.load === "function"
                    ? __qin_cssts_plugin__.load.call(__qin_context__, "\\0virtual:csstsAtom")
                    : __qin_cssts_plugin__.load.handler.call(__qin_context__, "\\0virtual:csstsAtom");
                  __qin_css__ = typeof __qin_css_loaded__ === "string" ? __qin_css_loaded__ : (__qin_css_loaded__ && __qin_css_loaded__.code) || "";
                  __qin_atom__ = typeof __qin_atom_loaded__ === "string" ? __qin_atom_loaded__ : (__qin_atom_loaded__ && __qin_atom_loaded__.code) || "";
                }
                ({
                  code: __qin_code__,
                  hasStyles: __qin_code__.includes("virtual:cssts.css") || __qin_css__.length > 0,
                  css: __qin_css__,
                  atomModule: __qin_atom__,
                  pluginName: __qin_plugin__.name
                });
                """.formatted(sourceLiteral);
    }

    @SuppressWarnings("unchecked")
    private QinOvsCompileResult decodeResult(Object result) {
        if (!(result instanceof Map<?, ?> rawMap)) {
            throw new IllegalStateException("vite-plugin-ovs did not return an object payload: " + result);
        }
        Map<String, Object> map = (Map<String, Object>) rawMap;
        Object code = map.get("code");
        Object hasStyles = map.get("hasStyles");
        Object css = map.get("css");
        Object atomModule = map.get("atomModule");
        if (!(code instanceof String codeText)) {
            throw new IllegalStateException("vite-plugin-ovs result missing code string: " + result);
        }
        boolean styles = Boolean.TRUE.equals(hasStyles);
        return new QinOvsCompileResult(
                codeText,
                styles,
                css instanceof String cssText ? cssText : "",
                atomModule instanceof String atomText ? atomText : "");
    }

    public record QinOvsCompileResult(
            String code,
            boolean hasStyles,
            String css,
            String atomModule) {
    }

    private record CacheKey(Path projectRoot, String source) {
        private CacheKey {
            Objects.requireNonNull(projectRoot, "projectRoot cannot be null");
            Objects.requireNonNull(source, "source cannot be null");
        }
    }
}
