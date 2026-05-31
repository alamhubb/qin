package com.qin.runtime.core.vue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class QinVueTemplateBlockCompiler {
    private static final Pattern INTERPOLATION_PATTERN = Pattern.compile("\\{\\{\\s*([^}]+?)\\s*\\}\\}");
    private static final Pattern CLASS_BINDING_PATTERN = Pattern.compile(
            "(?i)(:class|v-bind:class)\\s*=\\s*(\"([^\"]*)\"|'([^']*)')");
    private static final Pattern STAGE1_COMPONENT_PLACEHOLDER_PATTERN = Pattern.compile(
            "<\\s*([A-Z][A-Za-z0-9_$]*)\\s*/\\s*>");

    private QinVueTemplateBlockCompiler() {
    }

    static String compile(Object templateBlock) {
        String template = QinVueSfcBlockSupport.extractBlockContent(templateBlock);
        template = rewriteStage1ComponentPlaceholders(template);
        String escaped = escapeTemplateLiteral(template);
        escaped = rewriteClassBindings(escaped);
        String rendered = rewriteInterpolations(escaped);
        return """
                function __qinEscapeHtml(value) {
                  const text = value == null ? '' : String(value);
                  return text
                    .replace(/&/g, '&amp;')
                    .replace(/</g, '&lt;')
                    .replace(/>/g, '&gt;')
                    .replace(/\\"/g, '&quot;')
                    .replace(/'/g, '&#39;');
                }
                function __qinNormalizeClass(value) {
                  if (Array.isArray(value)) {
                    return value.map(__qinNormalizeClass).filter(Boolean).join(' ');
                  }
                  if (value && typeof value === 'object') {
                    return Object.entries(value)
                      .filter(([, enabled]) => !!enabled)
                      .map(([key]) => key)
                      .join(' ');
                  }
                  return value == null ? '' : String(value);
                }
                function __qinRenderVueTemplate() {
                  return `%s`;
                }
                """.formatted(rendered);
    }

    private static String rewriteInterpolations(String escapedTemplate) {
        Matcher matcher = INTERPOLATION_PATTERN.matcher(escapedTemplate);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String expr = matcher.group(1);
            String replacement = "${__qinEscapeHtml((" + expr + "))}";
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static String rewriteStage1ComponentPlaceholders(String template) {
        Matcher matcher = STAGE1_COMPONENT_PLACEHOLDER_PATTERN.matcher(template == null ? "" : template);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String componentName = matcher.group(1);
            String replacement = "<section data-qin-component=\"" + componentName + "\"></section>";
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static String rewriteClassBindings(String escapedTemplate) {
        Matcher matcher = CLASS_BINDING_PATTERN.matcher(escapedTemplate);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String expr = matcher.group(3) != null ? matcher.group(3) : matcher.group(4);
            String replacement = toClassBindingReplacement(expr);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static String toClassBindingReplacement(String expr) {
        if (expr == null || expr.isBlank()) {
            return "class=\"${__qinNormalizeClass('')}\"";
        }

        var parts = QinVueSfcBlockSupport.splitTopLevelCommaParts(expr);
        if (parts.size() > 1) {
            StringBuilder builder = new StringBuilder("class=\"${__qinNormalizeClass([");
            for (int index = 0; index < parts.size(); index++) {
                if (index > 0) {
                    builder.append(", ");
                }
                builder.append(parts.get(index));
            }
            builder.append("])}\"");
            return builder.toString();
        }

        return "class=\"${__qinNormalizeClass((" + expr + "))}\"";
    }

    private static String escapeTemplateLiteral(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("\\", "\\\\")
                .replace("`", "\\`")
                .replace("${", "\\${");
    }
}
