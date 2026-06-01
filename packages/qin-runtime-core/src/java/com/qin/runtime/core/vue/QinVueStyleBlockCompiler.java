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
                  const styleId = import.meta.url
                    .replace(/[?&]qin-hmr=[^&]+/g, '')
                    .replace(/[?&]$/, '') + '#vue-style';
                  let style = Array.from(document.querySelectorAll('style[data-qin-vue]'))
                    .find(candidate => candidate.getAttribute('data-qin-style-id') === styleId);
                  if (!style) {
                    style = document.createElement('style');
                    style.setAttribute('data-qin-vue', 'true');
                    style.setAttribute('data-qin-style-id', styleId);
                    document.head.appendChild(style);
                  }
                  style.setAttribute('data-qin-vue', 'true');
                  style.textContent = "%s";
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
