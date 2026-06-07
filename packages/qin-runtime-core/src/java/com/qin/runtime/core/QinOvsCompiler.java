package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Map;

public final class QinOvsCompiler {
    private final QinJsPackageRunner packageRunner = new QinJsPackageRunner();

    public QinOvsCompileResult compile(Path projectRoot, String source) throws Exception {
        try {
            Object result = packageRunner.runModuleSource(
                    projectRoot,
                    buildWrapperSource(source),
                    "vite_plugin_ovs_transform");
            return decodeResult(result);
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
                const __qin_cssts_plugin__ = __qin_plugins__.find(plugin => plugin && plugin.name === "vite-plugin-cssts");
                let __qin_css__ = "";
                let __qin_atom__ = "";
                if (__qin_cssts_plugin__ && __qin_cssts_plugin__.load) {
                  const __qin_css_loaded__ = typeof __qin_cssts_plugin__.load === "function"
                    ? __qin_cssts_plugin__.load.call(__qin_context__, "\\0virtual:cssts.css")
                    : __qin_cssts_plugin__.load.handler.call(__qin_context__, "\\0virtual:cssts.css");
                  const __qin_atom_loaded__ = typeof __qin_cssts_plugin__.load === "function"
                    ? __qin_cssts_plugin__.load.call(__qin_context__, "\\0virtual:csstsAtom")
                    : __qin_cssts_plugin__.load.handler.call(__qin_context__, "\\0virtual:csstsAtom");
                  __qin_css__ = typeof __qin_css_loaded__ === "string" ? __qin_css_loaded__ : (__qin_css_loaded__ && __qin_css_loaded__.code) || "";
                  __qin_atom__ = typeof __qin_atom_loaded__ === "string" ? __qin_atom_loaded__ : (__qin_atom_loaded__ && __qin_atom_loaded__.code) || "";
                }
                const __qin_code__ = typeof __qin_result__ === "string" ? __qin_result__ : __qin_result__.code;
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
}
