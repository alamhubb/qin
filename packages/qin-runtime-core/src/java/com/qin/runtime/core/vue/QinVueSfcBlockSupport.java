package com.qin.runtime.core.vue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class QinVueSfcBlockSupport {
    private QinVueSfcBlockSupport() {
    }

    static String extractBlockContent(Object block) {
        if (!(block instanceof Map<?, ?> map)) {
            return "";
        }
        Object content = map.get("content");
        return content instanceof String text ? text : "";
    }

    static List<?> asList(Object value) {
        if (value instanceof List<?> list) {
            return list;
        }
        return List.of();
    }

    static boolean isCsstsLang(Object block) {
        String lang = extractLang(block);
        return "cssts".equalsIgnoreCase(lang) || "ccsts".equalsIgnoreCase(lang);
    }

    static List<String> splitTopLevelCommaParts(String source) {
        if (source == null || source.isBlank()) {
            return List.of();
        }

        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int parenDepth = 0;
        int bracketDepth = 0;
        int braceDepth = 0;
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean inTemplate = false;

        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            char previous = i > 0 ? source.charAt(i - 1) : '\0';
            if (ch == '\'' && !inDoubleQuote && !inTemplate && previous != '\\') {
                inSingleQuote = !inSingleQuote;
            } else if (ch == '"' && !inSingleQuote && !inTemplate && previous != '\\') {
                inDoubleQuote = !inDoubleQuote;
            } else if (ch == '`' && !inSingleQuote && !inDoubleQuote && previous != '\\') {
                inTemplate = !inTemplate;
            } else if (!inSingleQuote && !inDoubleQuote && !inTemplate) {
                switch (ch) {
                    case '(' -> parenDepth++;
                    case ')' -> parenDepth = Math.max(0, parenDepth - 1);
                    case '[' -> bracketDepth++;
                    case ']' -> bracketDepth = Math.max(0, bracketDepth - 1);
                    case '{' -> braceDepth++;
                    case '}' -> braceDepth = Math.max(0, braceDepth - 1);
                    case ',' -> {
                        if (parenDepth == 0 && bracketDepth == 0 && braceDepth == 0) {
                            String part = current.toString().trim();
                            if (!part.isEmpty()) {
                                parts.add(part);
                            }
                            current.setLength(0);
                            continue;
                        }
                    }
                    default -> {
                    }
                }
            }
            current.append(ch);
        }

        String tail = current.toString().trim();
        if (!tail.isEmpty()) {
            parts.add(tail);
        }
        return parts;
    }

    static String describeBlock(Object block) {
        String lang = extractLang(block);
        if (lang == null || lang.isBlank()) {
            return "lang=<default>";
        }
        return "lang=" + lang;
    }

    private static String extractLang(Object block) {
        if (!(block instanceof Map<?, ?> map)) {
            return "";
        }
        Object lang = map.get("lang");
        if (lang instanceof String text && !text.isBlank()) {
            return text;
        }
        Object attrs = map.get("attrs");
        if (attrs instanceof Map<?, ?> attrsMap) {
            Object attrsLang = attrsMap.get("lang");
            if (attrsLang instanceof String text && !text.isBlank()) {
                return text;
            }
        }
        Object rawAttrs = map.get("rawAttrs");
        if (rawAttrs instanceof List<?> rawAttrList) {
            for (Object rawAttrNode : rawAttrList) {
                if (!(rawAttrNode instanceof Map<?, ?> rawAttrMap)) {
                    continue;
                }
                Object name = rawAttrMap.get("name");
                if (!"lang".equals(String.valueOf(name))) {
                    continue;
                }
                Object value = rawAttrMap.get("value");
                if (value instanceof String text && !text.isBlank()) {
                    return text;
                }
            }
        }
        return "";
    }
}
