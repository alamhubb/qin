package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QinOvsCompiler {
    private static final int MAX_CACHE_ENTRIES = 64;
    private static final Pattern CLASS_PREFIX_PATTERN = Pattern.compile(
            "\\bclassPrefix\\s*:\\s*(['\"])(.*?)\\1");

    private final QinJsPackageRunner packageRunner = new QinJsPackageRunner();
    private final Map<CacheKey, QinOvsCompileResult> cache = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<CacheKey, QinOvsCompileResult> eldest) {
            return size() > MAX_CACHE_ENTRIES;
        }
    };

    public QinOvsCompileResult compile(Path projectRoot, String source) throws Exception {
        Path normalizedRoot = projectRoot.toAbsolutePath().normalize();
        CacheKey key = new CacheKey(normalizedRoot, source);
        synchronized (cache) {
            QinOvsCompileResult cached = cache.get(key);
            if (cached != null) {
                return cached;
            }
        }
        String configSource = readConfigSource(normalizedRoot);
        String diskKey = QinFrontendTransformDiskCache.keyMaterial(normalizedRoot, source, configSource);
        QinOvsCompileResult diskCached = QinFrontendTransformDiskCache.read(normalizedRoot, "ovs", diskKey)
                .map(this::decodeDiskCache)
                .orElse(null);
        if (diskCached != null) {
            synchronized (cache) {
                cache.put(key, diskCached);
            }
            System.out.println("[QinOvsCompiler] transform disk cache hit");
            return diskCached;
        }
        try {
            Object result = packageRunner.runModuleSource(
                    normalizedRoot,
                    buildWrapperSource(normalizedRoot, source),
                    "vite_plugin_ovs_transform");
            QinOvsCompileResult decoded = decodeResult(result);
            QinFrontendTransformDiskCache.write(normalizedRoot, "ovs", diskKey, encodeDiskCache(decoded));
            synchronized (cache) {
                cache.put(key, decoded);
            }
            return decoded;
        } catch (Exception error) {
            throw new IllegalStateException("Qin vite-plugin-ovs transform failed for " + projectRoot, error);
        }
    }

    private String buildWrapperSource(Path projectRoot, String source) {
        String sourceLiteral = QinJsPackageRunner.renderJsLiteral(source);
        return """
                import vitePluginOvs from "vite-plugin-ovs";
                %s
                function __qinFlattenPlugins(value) {
                  const out = [];
                  for (const item of Array.isArray(value) ? value : []) {
                    if (!item) continue;
                    if (Array.isArray(item)) out.push(...__qinFlattenPlugins(item));
                    else out.push(item);
                  }
                  return out;
                }
                function __qinPluginName(plugin) {
                  return plugin && typeof plugin.name === "string" ? plugin.name : "";
                }
                function __qinResolveMaybePromise(value) {
                  if (value && value.then) {
                    let resolved = value;
                    value.then(next => { resolved = next; });
                    return resolved;
                  }
                  return value;
                }
                function __qinCallPluginHook(hook, context, ...args) {
                  if (!hook) return null;
                  const result = typeof hook === "function"
                    ? hook.call(context, ...args)
                    : hook.handler.call(context, ...args);
                  return __qinResolveMaybePromise(result);
                }
                const __qin_user_config__ = qinUserViteConfig || {};
                const __qin_plugins__ = __qinFlattenPlugins(__qin_user_config__.plugins);
                if (!__qin_plugins__.find(plugin => __qinPluginName(plugin) === "vite-plugin-ovs")) {
                  console.log("[QinOvsCompiler] instantiate vite-plugin-ovs");
                  __qin_plugins__.push(...__qinFlattenPlugins(vitePluginOvs(%s)));
                }
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
                console.log("[QinOvsCompiler] transform start");
                let __qin_result__ = __qinCallPluginHook(__qin_plugin__.transform, __qin_context__, __qin_source__, __qin_id__);
                console.log("[QinOvsCompiler] transform returned");
                if (!__qin_result__) {
                  throw new Error("vite-plugin-ovs transform returned empty result for " + __qin_id__);
                }
                const __qin_code__ = typeof __qin_result__ === "string" ? __qin_result__ : __qin_result__.code;
                if (typeof __qin_code__ !== "string" || __qin_code__.length === 0) {
                  throw new Error("vite-plugin-ovs transform returned empty code for " + __qin_id__);
                }
                console.log("[QinOvsCompiler] postprocess start");
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
                  const __qin_css_loaded__ = __qinCallPluginHook(__qin_cssts_plugin__.load, __qin_context__, "\\0virtual:cssts.css");
                  const __qin_atom_loaded__ = __qinCallPluginHook(__qin_cssts_plugin__.load, __qin_context__, "\\0virtual:csstsAtom");
                  __qin_css__ = typeof __qin_css_loaded__ === "string" ? __qin_css_loaded__ : (__qin_css_loaded__ && __qin_css_loaded__.code) || "";
                  __qin_atom__ = typeof __qin_atom_loaded__ === "string" ? __qin_atom_loaded__ : (__qin_atom_loaded__ && __qin_atom_loaded__.code) || "";
                }
                console.log("[QinOvsCompiler] postprocess done");
                ({
                  code: __qin_code__,
                  hasStyles: __qin_code__.includes("virtual:cssts.css") || __qin_css__.length > 0,
                  css: __qin_css__,
                  atomModule: __qin_atom__,
                  pluginName: __qin_plugin__.name
                });
                """.formatted(
                viteConfigImportSource(projectRoot),
                ovsPluginOptionsSource(projectRoot),
                sourceLiteral);
    }

    private String viteConfigImportSource(Path projectRoot) {
        Path config = findQinConfig(projectRoot);
        if (!java.nio.file.Files.isRegularFile(config)) {
            return "const qinUserViteConfig = null;";
        }
        try {
            String text = java.nio.file.Files.readString(config);
            if (!text.contains("export default")) {
                return "const qinUserViteConfig = null;";
            }
            if (!requiresFullOvsConfigEvaluation(text)) {
                return "const qinUserViteConfig = null;";
            }
        } catch (Exception ignored) {
            return "const qinUserViteConfig = null;";
        }
        Path wrapperDir = projectRoot.toAbsolutePath().normalize()
                .resolve(".qin")
                .resolve("runtime")
                .resolve("npm-host")
                .normalize();
        String relative = wrapperDir.relativize(config.toAbsolutePath().normalize()).toString().replace('\\', '/');
        if (!relative.startsWith(".")) {
            relative = "./" + relative;
        }
        return "import qinUserViteConfig from "
                + QinJsPackageRunner.renderJsLiteral(relative)
                + ";";
    }

    private boolean requiresFullOvsConfigEvaluation(String configText) {
        return configText.contains("vite-plugin-ovs")
                && configText.contains("transform(");
    }

    private String ovsPluginOptionsSource(Path projectRoot) {
        Path config = findQinConfig(projectRoot);
        if (!java.nio.file.Files.isRegularFile(config)) {
            return "{ cssts: { classPrefix: \"cmp-\" } }";
        }
        try {
            String text = java.nio.file.Files.readString(config);
            Matcher matcher = CLASS_PREFIX_PATTERN.matcher(text);
            if (matcher.find()) {
                return "{ cssts: { classPrefix: "
                        + QinJsPackageRunner.renderJsLiteral(matcher.group(2))
                        + " } }";
            }
        } catch (Exception ignored) {
            return "{ cssts: { classPrefix: \"cmp-\" } }";
        }
        return "{ cssts: { classPrefix: \"cmp-\" } }";
    }

    private Path findQinConfig(Path projectRoot) {
        return projectRoot.toAbsolutePath().normalize().resolve("qin.config.js");
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

    private Map<String, String> encodeDiskCache(QinOvsCompileResult result) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("code", result.code());
        values.put("hasStyles", String.valueOf(result.hasStyles()));
        values.put("css", result.css());
        values.put("atomModule", result.atomModule());
        return values;
    }

    private QinOvsCompileResult decodeDiskCache(Map<String, String> values) {
        String code = values.get("code");
        if (code == null || code.isBlank()) {
            return null;
        }
        return new QinOvsCompileResult(
                code,
                Boolean.parseBoolean(values.getOrDefault("hasStyles", "false")),
                values.getOrDefault("css", ""),
                values.getOrDefault("atomModule", ""));
    }

    private String readConfigSource(Path projectRoot) {
        Path config = findQinConfig(projectRoot);
        if (!java.nio.file.Files.isRegularFile(config)) {
            return "";
        }
        try {
            return java.nio.file.Files.readString(config);
        } catch (Exception ignored) {
            return "";
        }
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
