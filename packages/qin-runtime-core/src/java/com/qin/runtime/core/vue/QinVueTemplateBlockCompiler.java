package com.qin.runtime.core.vue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class QinVueTemplateBlockCompiler {
    private static final Pattern INTERPOLATION_PATTERN = Pattern.compile("\\{\\{\\s*([^}]+?)\\s*\\}\\}");
    private static final Pattern TAG_PATTERN = Pattern.compile(
            "(?s)<\\s*([A-Za-z][A-Za-z0-9_-]*)\\b([^>]*)>(.*?)</\\s*\\1\\s*>");
    private static final Pattern CLASS_BINDING_PATTERN = Pattern.compile(
            "(?i)(:class|v-bind:class)\\s*=\\s*(\"([^\"]*)\"|'([^']*)')");
    private static final Pattern CLICK_BINDING_PATTERN = Pattern.compile(
            "(?i)(@click|v-on:click)\\s*=\\s*(\"([^\"]*)\"|'([^']*)')");
    private static final Pattern STATIC_CLASS_PATTERN = Pattern.compile(
            "(?i)(?<![\\w:-])class\\s*=\\s*(\"([^\"]*)\"|'([^']*)')");
    private static final Pattern SIMPLE_DYNAMIC_ATTRIBUTE_PATTERN = Pattern.compile(
            "(?i)(?<![\\w:-])(:|v-bind:)([A-Za-z_][A-Za-z0-9_:-]*)\\s*=\\s*(\"([^\"]*)\"|'([^']*)')");
    private static final Pattern STAGE1_COMPONENT_PLACEHOLDER_PATTERN = Pattern.compile(
            "<\\s*([A-Z][A-Za-z0-9_$]*)\\s*/\\s*>");

    private QinVueTemplateBlockCompiler() {
    }

    static String compile(Object templateBlock) {
        String template = QinVueSfcBlockSupport.extractBlockContent(templateBlock);
        template = rewriteStage1ComponentPlaceholders(template);
        String escaped = escapeTemplateLiteral(template);
        escaped = rewriteClassBindings(escaped);
        escaped = rewriteSimpleDynamicAttributes(escaped);
        String rendered = rewriteInterpolations(escaped);
        return """
                function __qinEscapeHtml(value) {
                  const text = value == null ? '' : String(__qinDisplayValue(value));
                  return text
                    .replace(/&/g, '&amp;')
                    .replace(/</g, '&lt;')
                    .replace(/>/g, '&gt;')
                    .replace(/\\"/g, '&quot;')
                    .replace(/'/g, '&#39;');
                }
                function __qinDisplayValue(value) {
                  if (value && typeof value === 'object' && 'value' in value) {
                    return value.value;
                  }
                  return value;
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

    static String compileVueRender(Object templateBlock) {
        String template = QinVueSfcBlockSupport.extractBlockContent(templateBlock).trim();
        String renderExpression = toVNodeExpression(template);
        if (renderExpression.isBlank()) {
            renderExpression = "null";
        }
        return """
                function __qinRenderVueComponent() {
                  return %s;
                }
                """.formatted(renderExpression);
    }

    private static String toVNodeExpression(String source) {
        if (source == null || source.isBlank()) {
            return "";
        }
        Matcher root = TAG_PATTERN.matcher(source.trim());
        if (!root.matches()) {
            return renderTextExpression(source);
        }
        String tag = root.group(1);
        String attrs = root.group(2);
        String body = root.group(3);
        String props = renderPropsExpression(attrs);
        String children = renderChildrenExpression(body);
        return "__qinVueH(\"" + escapeJsString(tag) + "\", " + props + ", " + children + ")";
    }

    private static String renderChildrenExpression(String body) {
        if (body == null || body.isBlank()) {
            return "null";
        }
        Matcher childMatcher = TAG_PATTERN.matcher(body);
        StringBuilder children = new StringBuilder();
        int cursor = 0;
        while (childMatcher.find()) {
            appendTextChild(children, body.substring(cursor, childMatcher.start()));
            appendChild(children, toVNodeExpression(childMatcher.group(0)));
            cursor = childMatcher.end();
        }
        appendTextChild(children, body.substring(cursor));
        if (children.isEmpty()) {
            return renderTextExpression(body);
        }
        return "[" + children + "]";
    }

    private static void appendTextChild(StringBuilder children, String rawText) {
        String normalized = normalizeTemplateText(rawText);
        if (normalized.isBlank()) {
            return;
        }
        appendChild(children, renderTextExpression(normalized));
    }

    private static void appendChild(StringBuilder children, String expression) {
        if (expression == null || expression.isBlank()) {
            return;
        }
        if (!children.isEmpty()) {
            children.append(", ");
        }
        children.append(expression);
    }

    private static String renderPropsExpression(String attrs) {
        StringBuilder props = new StringBuilder();
        appendProp(props, classPropExpression(attrs));
        appendProp(props, clickPropExpression(attrs));
        if (props.isEmpty()) {
            return "null";
        }
        return "{" + props + "}";
    }

    private static String classPropExpression(String attrs) {
        Matcher dynamic = CLASS_BINDING_PATTERN.matcher(attrs == null ? "" : attrs);
        if (dynamic.find()) {
            String expr = dynamic.group(3) != null ? dynamic.group(3) : dynamic.group(4);
            return "class: __qinNormalizeClass((" + expr + "))";
        }
        Matcher plain = STATIC_CLASS_PATTERN.matcher(attrs == null ? "" : attrs);
        if (plain.find()) {
            String value = plain.group(2) != null ? plain.group(2) : plain.group(3);
            return "class: \"" + escapeJsString(value) + "\"";
        }
        return "";
    }

    private static String clickPropExpression(String attrs) {
        Matcher matcher = CLICK_BINDING_PATTERN.matcher(attrs == null ? "" : attrs);
        if (!matcher.find()) {
            return "";
        }
        String expr = matcher.group(3) != null ? matcher.group(3) : matcher.group(4);
        return "onClick: " + expr;
    }

    private static void appendProp(StringBuilder props, String prop) {
        if (prop == null || prop.isBlank()) {
            return;
        }
        if (!props.isEmpty()) {
            props.append(", ");
        }
        props.append(prop);
    }

    private static String renderTextExpression(String text) {
        String normalized = normalizeTemplateText(text);
        if (normalized.isEmpty()) {
            return "\"\"";
        }
        Matcher matcher = INTERPOLATION_PATTERN.matcher(normalized);
        StringBuilder expression = new StringBuilder();
        int cursor = 0;
        while (matcher.find()) {
            appendStringPart(expression, normalized.substring(cursor, matcher.start()));
            appendExpressionPart(expression, "__qinDisplayValue((" + matcher.group(1).trim() + "))");
            cursor = matcher.end();
        }
        appendStringPart(expression, normalized.substring(cursor));
        return expression.isEmpty() ? "\"\"" : expression.toString();
    }

    private static void appendStringPart(StringBuilder expression, String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        appendExpressionPart(expression, "\"" + escapeJsString(text) + "\"");
    }

    private static void appendExpressionPart(StringBuilder expression, String part) {
        if (!expression.isEmpty()) {
            expression.append(" + ");
        }
        expression.append(part);
    }

    private static String normalizeTemplateText(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("\\s+", " ").trim();
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

    private static String rewriteSimpleDynamicAttributes(String escapedTemplate) {
        Matcher matcher = SIMPLE_DYNAMIC_ATTRIBUTE_PATTERN.matcher(escapedTemplate);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String attrName = matcher.group(2);
            if ("class".equalsIgnoreCase(attrName)) {
                matcher.appendReplacement(buffer, Matcher.quoteReplacement(matcher.group(0)));
                continue;
            }
            String expr = matcher.group(4) != null ? matcher.group(4) : matcher.group(5);
            String replacement = attrName + "=\"${__qinEscapeHtml((" + expr + "))}\"";
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
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

    private static String escapeJsString(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }
}
