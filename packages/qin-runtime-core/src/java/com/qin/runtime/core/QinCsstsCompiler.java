package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class QinCsstsCompiler {
    private final QinJsPackageRunner packageRunner = new QinJsPackageRunner();

    public QinCsstsCompileResult compile(Path projectRoot, String source) throws Exception {
        Object result = packageRunner.runModuleSource(
                projectRoot,
                buildWrapperSource(source),
                "cssts_compiler");
        return decodeResult(result);
    }

    private String buildWrapperSource(String source) {
        String sourceLiteral = QinJsPackageRunner.renderJsLiteral(source);
        return """
                import { CsstsInit, transformCssTs, generateStylesCss, generateCsstsAtomModule } from "cssts-compiler";
                const __qin_context__ = { styles: new Set() };
                CsstsInit.init({ dts: false });
                const __qin_result__ = transformCssTs(%s, __qin_context__);
                ({
                  code: __qin_result__.code,
                  hasStyles: __qin_result__.hasStyles,
                  css: __qin_result__.hasStyles ? generateStylesCss(__qin_context__.styles) : "",
                  atomModule: __qin_result__.hasStyles ? generateCsstsAtomModule(__qin_context__.styles) : ""
                });
                """.formatted(sourceLiteral);
    }

    @SuppressWarnings("unchecked")
    private QinCsstsCompileResult decodeResult(Object result) {
        if (!(result instanceof Map<?, ?> rawMap)) {
            throw new IllegalStateException("cssts-compiler did not return an object payload: " + result);
        }
        Map<String, Object> map = (Map<String, Object>) rawMap;
        Object code = map.get("code");
        Object hasStyles = map.get("hasStyles");
        Object css = map.get("css");
        Object atomModule = map.get("atomModule");
        if (!(code instanceof String codeText)) {
            throw new IllegalStateException("cssts-compiler result missing code string: " + result);
        }
        boolean styles = Boolean.TRUE.equals(hasStyles);
        return new QinCsstsCompileResult(
                codeText,
                styles,
                css instanceof String cssText ? cssText : "",
                atomModule instanceof String atomText ? atomText : "");
    }

    public record QinCsstsCompileResult(
            String code,
            boolean hasStyles,
            String css,
            String atomModule) {
    }
}
