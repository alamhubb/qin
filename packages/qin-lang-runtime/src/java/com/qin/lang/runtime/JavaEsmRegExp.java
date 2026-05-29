package com.qin.lang.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Phase-1 RegExp runtime backed by java.util.regex.
 */
final class JavaEsmRegExp {
    private final String source;
    private final String flags;
    private final Pattern pattern;

    JavaEsmRegExp(Object source, Object flags) {
        this.source = normalizeSource(source);
        this.flags = flags == null ? "" : String.valueOf(flags);
        this.pattern = compilePattern(this.source, this.flags);
    }

    Object memberGet(Object property) {
        return switch (String.valueOf(property)) {
            case "source" -> source;
            case "flags" -> flags;
            case "global" -> flags.indexOf('g') >= 0;
            case "ignoreCase" -> flags.indexOf('i') >= 0;
            case "multiline" -> flags.indexOf('m') >= 0;
            case "dotAll" -> flags.indexOf('s') >= 0;
            case "unicode" -> flags.indexOf('u') >= 0;
            case "sticky" -> flags.indexOf('y') >= 0;
            default -> null;
        };
    }

    boolean supports(String methodName) {
        return switch (methodName) {
            case "test", "exec", "toString" -> true;
            default -> false;
        };
    }

    Object invoke(String methodName, Object[] args) {
        return switch (methodName) {
            case "test" -> test(args);
            case "exec" -> exec(args);
            case "toString" -> "/" + source + "/" + flags;
            default -> throw new IllegalArgumentException("Unsupported RegExp method: " + methodName);
        };
    }

    Object match(String text) {
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) {
            return null;
        }
        List<Object> result = new ArrayList<>();
        result.add(matcher.group());
        for (int i = 1; i <= matcher.groupCount(); i++) {
            result.add(matcher.group(i));
        }
        return result;
    }

    String replace(String text, Object replacement) {
        Matcher matcher = pattern.matcher(text);
        if (isCallable(replacement)) {
            StringBuffer buffer = new StringBuffer();
            boolean replaceAll = flags.indexOf('g') >= 0;
            while (matcher.find()) {
                matcher.appendReplacement(buffer, Matcher.quoteReplacement(callReplacement(replacement, matcher)));
                if (!replaceAll) {
                    break;
                }
            }
            matcher.appendTail(buffer);
            return buffer.toString();
        }
        String replacementText = replacement == null ? "" : String.valueOf(replacement);
        boolean replaceAll = flags.indexOf('g') >= 0;
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(expandReplacement(replacementText, matcher)));
            if (!replaceAll) {
                break;
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static String expandReplacement(String replacementText, Matcher matcher) {
        StringBuilder out = new StringBuilder(replacementText.length());
        for (int i = 0; i < replacementText.length(); i++) {
            char ch = replacementText.charAt(i);
            if (ch != '$' || i + 1 >= replacementText.length()) {
                out.append(ch);
                continue;
            }
            char next = replacementText.charAt(i + 1);
            if (next == '$') {
                out.append('$');
                i++;
                continue;
            }
            if (next == '&') {
                out.append(matcher.group());
                i++;
                continue;
            }
            if (Character.isDigit(next)) {
                int groupIndex = next - '0';
                int consumed = 1;
                if (i + 2 < replacementText.length() && Character.isDigit(replacementText.charAt(i + 2))) {
                    int twoDigitGroup = groupIndex * 10 + (replacementText.charAt(i + 2) - '0');
                    if (twoDigitGroup <= matcher.groupCount()) {
                        groupIndex = twoDigitGroup;
                        consumed = 2;
                    }
                }
                if (groupIndex > 0 && groupIndex <= matcher.groupCount()) {
                    String group = matcher.group(groupIndex);
                    if (group != null) {
                        out.append(group);
                    }
                    i += consumed;
                    continue;
                }
            }
            out.append(ch);
        }
        return out.toString();
    }

    String[] split(String text, int limit) {
        return pattern.split(text, limit == Integer.MAX_VALUE ? -1 : limit);
    }

    private Object test(Object[] args) {
        requireArgRange("RegExp.test", args, 1, 1);
        return pattern.matcher(String.valueOf(args[0])).find();
    }

    private Object exec(Object[] args) {
        requireArgRange("RegExp.exec", args, 1, 1);
        return match(String.valueOf(args[0]));
    }

    private static boolean isCallable(Object value) {
        return JavaEsmGlobal.isRuntimeCallable(value);
    }

    private static String callReplacement(Object replacement, Matcher matcher) {
        List<Object> args = new ArrayList<>();
        args.add(matcher.group());
        for (int i = 1; i <= matcher.groupCount(); i++) {
            args.add(matcher.group(i));
        }
        Object result = JavaEsmGlobal.callRuntimeCallable(replacement, args.toArray());
        return result == null ? "" : String.valueOf(result);
    }

    private static String normalizeSource(Object value) {
        if (value instanceof JavaEsmRegExp regexp) {
            return regexp.source;
        }
        return value == null ? "(?:)" : String.valueOf(value);
    }

    private static Pattern compilePattern(String source, String flags) {
        int javaFlags = 0;
        if (flags.indexOf('i') >= 0) {
            javaFlags |= Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
        }
        if (flags.indexOf('m') >= 0) {
            javaFlags |= Pattern.MULTILINE;
        }
        if (flags.indexOf('s') >= 0) {
            javaFlags |= Pattern.DOTALL;
        }
        String javaPattern = translateJsPatternToJava(source);
        try {
            return Pattern.compile(javaPattern, javaFlags);
        } catch (PatternSyntaxException error) {
            throw new IllegalArgumentException("Invalid RegExp pattern: /" + source + "/" + flags, error);
        }
    }

    private static String translateJsPatternToJava(String source) {
        StringBuilder out = new StringBuilder(source.length());
        boolean inClass = false;
        boolean escaped = false;
        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            if (escaped) {
                String unicodeProperty = readJsUnicodeProperty(source, i);
                if (unicodeProperty != null) {
                    out.append(translateJsUnicodeProperty(unicodeProperty));
                    i += unicodeProperty.length() + 2;
                    escaped = false;
                    continue;
                }
                if (ch == '/' || (inClass && ch == ']')) {
                    out.append(ch);
                } else {
                    out.append('\\').append(ch);
                }
                escaped = false;
                continue;
            }
            if (ch == '\\') {
                escaped = true;
                continue;
            }
            if (ch == '[' && !inClass && i + 2 < source.length()
                    && source.charAt(i + 1) == '^'
                    && source.charAt(i + 2) == ']') {
                out.append("[\\s\\S]");
                i += 2;
                continue;
            }
            if (ch == '[' && !inClass) {
                inClass = true;
                out.append(ch);
                continue;
            }
            if (ch == ']' && inClass) {
                inClass = false;
                out.append(ch);
                continue;
            }
            if (ch == '[' && inClass) {
                // JS treats a raw "[" inside a character class as a literal.
                // Java Pattern treats it as a nested class opener, so escape it.
                out.append("\\[");
                continue;
            }
            if (inClass && (ch == '{' || ch == '}')) {
                // In JS character classes these are literals. Java Pattern can
                // still treat them as repetition syntax in edge cases.
                out.append('\\').append(ch);
                continue;
            }
            out.append(ch);
        }
        if (escaped) {
            out.append('\\');
        }
        return out.toString();
    }

    private static String readJsUnicodeProperty(String source, int index) {
        if (index >= source.length() || source.charAt(index) != 'p') {
            return null;
        }
        int open = index + 1;
        if (open >= source.length() || source.charAt(open) != '{') {
            return null;
        }
        int close = source.indexOf('}', open + 1);
        if (close < 0) {
            return null;
        }
        return source.substring(open + 1, close);
    }

    private static String translateJsUnicodeProperty(String propertyName) {
        return switch (propertyName) {
            case "ID_Start" -> "\\p{javaJavaIdentifierStart}";
            case "ID_Continue" -> "\\p{javaJavaIdentifierPart}";
            default -> "\\p{" + propertyName + "}";
        };
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
