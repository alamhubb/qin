package com.qin.runtime.core.vue;

import java.util.List;

final class QinVueStyleBlockCompiler {
    private QinVueStyleBlockCompiler() {
    }

    static String compile(Object styles) {
        List<?> list = QinVueSfcBlockSupport.asList(styles);
        if (list.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Object styleBlock : list) {
            String content = QinVueSfcBlockSupport.extractBlockContent(styleBlock);
            if (content.isBlank()) {
                continue;
            }
            builder.append(injectStyle(content.trim())).append('\n');
        }
        return builder.toString().trim();
    }

    private static String injectStyle(String styleSource) {
        String escaped = escapeJsString(styleSource);
        return """
                (function __qinInjectVueStyle() {
                  if (typeof document === 'undefined') return;
                  const style = document.createElement('style');
                  style.setAttribute('data-qin-vue', 'true');
                  style.textContent = "%s";
                  document.head.appendChild(style);
                })();
                """.formatted(escaped);
    }

    private static String escapeJsString(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("\\", "\\\\")
                .replace("`", "\\`")
                .replace("${", "\\${")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }
}
