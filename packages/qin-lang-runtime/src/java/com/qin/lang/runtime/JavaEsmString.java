package com.qin.lang.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Phase-1 String builtin helpers for Qin's Java-backed ESM runtime.
 */
final class JavaEsmString {
    private JavaEsmString() {
    }

    public static String fromCharCode(Object... codes) {
        StringBuilder builder = new StringBuilder(codes.length);
        for (Object code : codes) {
            builder.append((char) toIndex(code));
        }
        return builder.toString();
    }

    static Object memberGet(CharSequence text, Object property) {
        String name = String.valueOf(property);
        if ("length".equals(name)) {
            return text.length();
        }
        int index = propertyIndex(property);
        if (index >= 0 && index < text.length()) {
            return String.valueOf(text.charAt(index));
        }
        return null;
    }

    static boolean supports(String methodName) {
        return switch (methodName) {
            case "includes", "startsWith", "endsWith", "trim", "toUpperCase",
                    "toLowerCase", "slice", "substring", "substr", "split", "charAt",
                    "charCodeAt",
                    "padStart", "padEnd",
                    "indexOf", "lastIndexOf", "search", "match", "replace", "valueOf", "toString" -> true;
            default -> false;
        };
    }

    static Object invoke(CharSequence target, String methodName, Object[] args) {
        String text = target.toString();
        return switch (methodName) {
            case "includes" -> includes(text, args);
            case "startsWith" -> startsWith(text, args);
            case "endsWith" -> endsWith(text, args);
            case "trim" -> trim(text, args);
            case "toUpperCase" -> toUpperCase(text, args);
            case "toLowerCase" -> toLowerCase(text, args);
            case "slice" -> slice(text, args);
            case "substring" -> substring(text, args);
            case "substr" -> substr(text, args);
            case "split" -> split(text, args);
            case "charAt" -> charAt(text, args);
            case "charCodeAt" -> charCodeAt(text, args);
            case "padStart" -> pad(text, args, true);
            case "padEnd" -> pad(text, args, false);
            case "indexOf" -> indexOf(text, args);
            case "lastIndexOf" -> lastIndexOf(text, args);
            case "search" -> search(text, args);
            case "match" -> match(text, args);
            case "replace" -> replace(text, args);
            case "valueOf", "toString" -> primitiveValue(text, args, "String." + methodName);
            default -> throw new IllegalArgumentException("Unsupported String builtin: " + methodName);
        };
    }

    private static Object includes(String text, Object[] args) {
        requireArgRange("String.includes", args, 1, 2);
        String search = String.valueOf(args[0]);
        int start = args.length == 2 ? normalizeStart(args[1], text.length()) : 0;
        return text.indexOf(search, start) >= 0;
    }

    private static Object startsWith(String text, Object[] args) {
        requireArgRange("String.startsWith", args, 1, 2);
        String prefix = String.valueOf(args[0]);
        int start = args.length == 2 ? normalizeStart(args[1], text.length()) : 0;
        return text.startsWith(prefix, start);
    }

    private static Object endsWith(String text, Object[] args) {
        requireArgRange("String.endsWith", args, 1, 2);
        String suffix = String.valueOf(args[0]);
        int end = args.length == 2 ? clamp(toIndex(args[1]), 0, text.length()) : text.length();
        return text.substring(0, end).endsWith(suffix);
    }

    private static Object trim(String text, Object[] args) {
        requireArgCount("String.trim", args, 0);
        return text.strip();
    }

    private static Object toUpperCase(String text, Object[] args) {
        requireArgCount("String.toUpperCase", args, 0);
        return text.toUpperCase(Locale.ROOT);
    }

    private static Object toLowerCase(String text, Object[] args) {
        requireArgCount("String.toLowerCase", args, 0);
        return text.toLowerCase(Locale.ROOT);
    }

    private static Object slice(String text, Object[] args) {
        requireArgRange("String.slice", args, 1, 2);
        int start = normalizeSliceIndex(args[0], text.length());
        int end = args.length == 2 ? normalizeSliceIndex(args[1], text.length()) : text.length();
        if (end < start) {
            end = start;
        }
        return text.substring(start, end);
    }

    private static Object substring(String text, Object[] args) {
        requireArgRange("String.substring", args, 1, 2);
        int start = clamp(toIndex(args[0]), 0, text.length());
        int end = args.length == 2 ? clamp(toIndex(args[1]), 0, text.length()) : text.length();
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        return text.substring(start, end);
    }

    private static Object substr(String text, Object[] args) {
        requireArgRange("String.substr", args, 1, 2);
        int start = toIndex(args[0]);
        if (start < 0) {
            start = Math.max(text.length() + start, 0);
        } else {
            start = Math.min(start, text.length());
        }
        int length = args.length == 2 ? Math.max(toIndex(args[1]), 0) : text.length() - start;
        int end = Math.min(start + length, text.length());
        return text.substring(start, end);
    }

    private static Object split(String text, Object[] args) {
        requireArgRange("String.split", args, 0, 2);
        if (args.length == 0 || args[0] == null) {
            return new ArrayList<>(List.of(text));
        }
        int limit = args.length == 2 ? Math.max(toIndex(args[1]), 0) : Integer.MAX_VALUE;
        if (limit == 0) {
            return new ArrayList<Object>();
        }
        if (args[0] instanceof JavaEsmRegExp regexp) {
            return toList(regexp.split(text, limit));
        }
        String separator = String.valueOf(args[0]);
        List<Object> result = new ArrayList<>();
        if (separator.isEmpty()) {
            for (int i = 0; i < text.length() && result.size() < limit; i++) {
                result.add(String.valueOf(text.charAt(i)));
            }
            return result;
        }
        String[] parts = text.split(Pattern.quote(separator), limit == Integer.MAX_VALUE ? -1 : limit);
        for (String part : parts) {
            if (result.size() == limit) {
                break;
            }
            result.add(part);
        }
        return result;
    }

    private static Object match(String text, Object[] args) {
        requireArgRange("String.match", args, 1, 1);
        if (args[0] instanceof JavaEsmRegExp regexp) {
            return regexp.match(text);
        }
        return new JavaEsmRegExp(String.valueOf(args[0]), null).match(text);
    }

    private static Object search(String text, Object[] args) {
        requireArgRange("String.search", args, 1, 1);
        if (args[0] instanceof JavaEsmRegExp regexp) {
            return regexp.searchIndex(text);
        }
        return new JavaEsmRegExp(String.valueOf(args[0]), null).searchIndex(text);
    }

    private static Object replace(String text, Object[] args) {
        requireArgRange("String.replace", args, 2, 2);
        Object search = args[0];
        Object replacement = args[1];
        if (search instanceof JavaEsmRegExp regexp) {
            return regexp.replace(text, replacement);
        }
        String searchText = String.valueOf(search);
        int index = text.indexOf(searchText);
        if (index < 0) {
            return text;
        }
        return text.substring(0, index)
                + replacementText(replacement, searchText)
                + text.substring(index + searchText.length());
    }

    private static Object charAt(String text, Object[] args) {
        requireArgRange("String.charAt", args, 1, 1);
        int index = toIndex(args[0]);
        if (index < 0 || index >= text.length()) {
            return "";
        }
        return String.valueOf(text.charAt(index));
    }

    private static Object charCodeAt(String text, Object[] args) {
        requireArgRange("String.charCodeAt", args, 1, 1);
        int index = toIndex(args[0]);
        if (index < 0 || index >= text.length()) {
            return Double.NaN;
        }
        return (double) text.charAt(index);
    }

    private static Object pad(String text, Object[] args, boolean start) {
        requireArgRange(start ? "String.padStart" : "String.padEnd", args, 1, 2);
        int targetLength = toIndex(args[0]);
        if (targetLength <= text.length()) {
            return text;
        }
        String fill = args.length == 2 && args[1] != null ? String.valueOf(args[1]) : " ";
        if (fill.isEmpty()) {
            return text;
        }
        int needed = targetLength - text.length();
        StringBuilder padding = new StringBuilder(needed);
        while (padding.length() < needed) {
            padding.append(fill);
        }
        if (padding.length() > needed) {
            padding.setLength(needed);
        }
        return start ? padding + text : text + padding;
    }

    private static Object indexOf(String text, Object[] args) {
        requireArgRange("String.indexOf", args, 1, 2);
        String search = String.valueOf(args[0]);
        int start = args.length == 2 ? normalizeStart(args[1], text.length()) : 0;
        return text.indexOf(search, start);
    }

    private static Object lastIndexOf(String text, Object[] args) {
        requireArgRange("String.lastIndexOf", args, 1, 2);
        String search = String.valueOf(args[0]);
        if (args.length == 2 && args[1] != null) {
            return text.lastIndexOf(search, clamp(toIndex(args[1]), 0, text.length()));
        }
        return text.lastIndexOf(search);
    }

    private static Object primitiveValue(String text, Object[] args, String methodName) {
        requireArgCount(methodName, args, 0);
        return text;
    }

    private static int normalizeStart(Object value, int length) {
        return clamp(toIndex(value), 0, length);
    }

    private static int normalizeSliceIndex(Object value, int length) {
        int index = toIndex(value);
        if (index < 0) {
            index = Math.max(length + index, 0);
        }
        return Math.min(index, length);
    }

    private static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    private static int toIndex(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String text) {
            try {
                return (int) Double.parseDouble(text.trim());
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
        return 0;
    }

    private static int propertyIndex(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String text) {
            try {
                return (int) Double.parseDouble(text.trim());
            } catch (NumberFormatException ignored) {
                return -1;
            }
        }
        return -1;
    }

    private static List<Object> toList(String[] parts) {
        List<Object> result = new ArrayList<>();
        for (String part : parts) {
            result.add(part);
        }
        return result;
    }

    private static String replacementText(Object replacement, String matched) {
        if (replacement == null) {
            return "";
        }
        if (replacement instanceof String text) {
            return text;
        }
        return String.valueOf(JavaEsmGlobal.callRuntimeCallable(replacement, matched));
    }

    private static void requireArgCount(String methodName, Object[] args, int expected) {
        requireArgRange(methodName, args, expected, expected);
    }

    private static void requireArgRange(String methodName, Object[] args, int min, int max) {
        if (args.length < min || args.length > max) {
            if (min == max) {
                throw new IllegalArgumentException(methodName + " expects exactly " + min + " argument(s)");
            }
            throw new IllegalArgumentException(
                    methodName + " expects between " + min + " and " + max + " argument(s)");
        }
    }
}
