package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Map;

public final class QinOvsCompiler {
    private final QinJsPackageRunner packageRunner = new QinJsPackageRunner();

    public QinOvsCompileResult compile(Path projectRoot, String source) throws Exception {
        Object result = packageRunner.runModuleSource(
                projectRoot,
                buildWrapperSource(source),
                "ovs_compiler");
        return decodeResult(result);
    }

    private String buildWrapperSource(String source) {
        String sourceLiteral = QinJsPackageRunner.renderJsLiteral(source);
        return """
                import { vitePluginOvsTransform } from "ovs-compiler";
                import { generateStylesCss, generateCsstsAtomModule } from "cssts-compiler";
                const __qin_styles__ = new Set();
                const __qin_result__ = vitePluginOvsTransform(%s, { globalStyles: __qin_styles__ });
                ({
                  code: __qin_result__.code,
                  hasStyles: __qin_styles__.size > 0,
                  css: __qin_styles__.size > 0 ? generateStylesCss(__qin_styles__) : "",
                  atomModule: __qin_styles__.size > 0 ? generateCsstsAtomModule(__qin_styles__) : ""
                });
                """.formatted(sourceLiteral);
    }

    @SuppressWarnings("unchecked")
    private QinOvsCompileResult decodeResult(Object result) {
        if (!(result instanceof Map<?, ?> rawMap)) {
            throw new IllegalStateException("ovs-compiler did not return an object payload: " + result);
        }
        Map<String, Object> map = (Map<String, Object>) rawMap;
        Object code = map.get("code");
        Object hasStyles = map.get("hasStyles");
        Object css = map.get("css");
        Object atomModule = map.get("atomModule");
        if (!(code instanceof String codeText)) {
            throw new IllegalStateException("ovs-compiler result missing code string: " + result);
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
