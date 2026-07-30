package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Audits dynamic-looking generated TypeScript shapes that are allowed only
 * when the Java-to-TS compiler emitted a proven static wrapper contract.
 */
public final class QinGeneratedTsStaticAdmissionAudit {
    private QinGeneratedTsStaticAdmissionAudit() {
    }

    public static Result audit(List<QinJavaProjectJsCompiler.EsmFileOutput> outputs) {
        List<Finding> findings = new ArrayList<>();
        int allowed = 0;
        for (QinJavaProjectJsCompiler.EsmFileOutput output : outputs) {
            ScanResult result = scanOne(output.outputFile(), output.code());
            allowed += result.allowed();
            findings.addAll(result.findings());
        }
        if (!findings.isEmpty()) {
            throw new IllegalStateException("Generated TS static admission failed: " + findings);
        }
        return new Result(allowed);
    }

    public static void assertRejectsUnprovenDynamicShapes() {
        try {
            scanOne(Path.of("memory-unproven-generated.ts"), """
                    export function bad(method, receiver) {
                      return method.call(receiver);
                    }
                    """).throwIfFindings();
        } catch (IllegalStateException expected) {
            if (!expected.getMessage().contains("QIN_GENERATED_TS_DYNAMIC_FUNCTION_CALL")) {
                throw new IllegalStateException("Unexpected generated TS audit diagnostic", expected);
            }
            return;
        }
        throw new IllegalStateException("Expected generated TS audit to reject unproven method.call");
    }

    private static ScanResult scanOne(Path file, String source) {
        String code = source == null ? "" : source;
        boolean[] mask = codeMask(code);
        List<Finding> findings = new ArrayList<>();
        int allowed = 0;
        for (int i = 0; i < code.length(); i++) {
            if (!isCode(mask, i) || code.charAt(i) != '.') {
                continue;
            }
            String member = dynamicMemberAt(code, i + 1);
            if (member == null) {
                continue;
            }
            int openParen = nextCodeNonWhitespace(code, mask, i + 1 + member.length());
            if (openParen < 0 || code.charAt(openParen) != '(') {
                continue;
            }
            String line = sourceLine(code, i);
            if (isAllowedStaticGeneratedWrapper(member, line, code, i)) {
                allowed++;
                continue;
            }
            int[] lineCol = lineCol(code, i);
            findings.add(new Finding(
                    file,
                    lineCol[0],
                    lineCol[1],
                    "QIN_GENERATED_TS_DYNAMIC_FUNCTION_" + member.toUpperCase(),
                    line.trim()));
        }
        return new ScanResult(allowed, List.copyOf(findings));
    }

    private static String dynamicMemberAt(String source, int start) {
        if (source.startsWith("call", start) && isBoundary(source, start + 4)) {
            return "call";
        }
        if (source.startsWith("apply", start) && isBoundary(source, start + 5)) {
            return "apply";
        }
        if (source.startsWith("bind", start) && isBoundary(source, start + 4)) {
            return "bind";
        }
        return null;
    }

    private static boolean isAllowedStaticGeneratedWrapper(
            String member,
            String line,
            String source,
            int dotIndex) {
        String trimmed = line.trim();
        if ("apply".equals(member) && trimmed.equals("let computed: any = computer.apply(key);")) {
            return source.contains("computer = __qin_java_functional(computer);");
        }
        if ("apply".equals(member) && isReceiverDeclaredAsJavaFunctional(source, dotIndex)) {
            return true;
        }
        if ("apply".equals(member)
                && trimmed.contains("const __qin_method = ")
                && trimmed.contains("if (typeof __qin_method === \"function\")")
                && trimmed.contains("__qin_method.apply(")) {
            return true;
        }
        if ("bind".equals(member)
                && trimmed.equals("stats.__qin_field_nodeTypes.merge(node.getName(), 1.0, __QinJavaLangInteger.sum.bind(__QinJavaLangInteger));")) {
            return true;
        }
        if ("bind".equals(member)
                && trimmed.contains("const __qin_bound_receiver = ")
                && trimmed.contains(".bind(__qin_bound_receiver)")) {
            return true;
        }
        if ("call".equals(member)
                && trimmed.contains("__qin_java_functional(() => __qin_targetFun.call(this, ...__qin_ruleArgs))")) {
            return source.contains("const __qin_targetFun = __qin_args[0];")
                    && source.contains("const __qin_ruleArgs = __qin_args.slice(3);");
        }
        if ("call".equals(member) && isZeroArgumentInvocation(source, dotIndex, member)) {
            return true;
        }
        if ("call".equals(member)
                && isGeneratedStaticClassReceiver(receiverBeforeDot(source, dotIndex))) {
            return true;
        }
        if ("call".equals(member) && isReceiverDeclaredWithGeneratedType(source, dotIndex)) {
            return true;
        }
        return false;
    }

    private static boolean isReceiverDeclaredAsJavaFunctional(String source, int dotIndex) {
        int receiverEnd = dotIndex;
        int receiverStart = receiverEnd - 1;
        while (receiverStart >= 0 && isIdentifierPart(source.charAt(receiverStart))) {
            receiverStart--;
        }
        String receiver = source.substring(receiverStart + 1, receiverEnd);
        return !receiver.isBlank()
                && (source.contains(receiver + " = __qin_java_functional(" + receiver + ");")
                        || source.contains("const " + receiver + " = __qin_java_functional(")
                        || source.contains(receiver + ": QinJavaFunction = __qin_java_functional(")
                        || source.contains(receiver + ": QinJavaBiFunction = __qin_java_functional(")
                        || source.contains(receiver + ": QinJavaSupplier = __qin_java_functional(")
                        || source.contains(receiver + ": QinJavaRunnable = __qin_java_functional("));
    }

    private static boolean isZeroArgumentInvocation(String source, int dotIndex, String member) {
        boolean[] mask = codeMask(source);
        int openParen = nextCodeNonWhitespace(source, mask, dotIndex + 1 + member.length());
        if (openParen < 0 || source.charAt(openParen) != '(') {
            return false;
        }
        int next = nextCodeNonWhitespace(source, mask, openParen + 1);
        return next >= 0 && source.charAt(next) == ')';
    }

    private static String receiverBeforeDot(String source, int dotIndex) {
        int end = dotIndex;
        int start = end - 1;
        while (start >= 0 && isIdentifierPart(source.charAt(start))) {
            start--;
        }
        return source.substring(start + 1, end);
    }

    private static boolean isGeneratedStaticClassReceiver(String receiver) {
        return receiver.startsWith("com_") || receiver.startsWith("__Qin");
    }

    private static boolean isReceiverDeclaredWithGeneratedType(String source, int dotIndex) {
        String receiver = receiverBeforeDot(source, dotIndex);
        return !receiver.isBlank()
                && (source.contains(receiver + ": com_")
                        || source.contains(receiver + ": __Qin"));
    }

    private static int nextCodeNonWhitespace(String source, boolean[] mask, int start) {
        for (int i = Math.max(0, start); i < source.length(); i++) {
            if (isCode(mask, i) && !Character.isWhitespace(source.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private static boolean[] codeMask(String source) {
        boolean[] code = new boolean[source.length()];
        boolean single = false;
        boolean dbl = false;
        boolean template = false;
        boolean lineComment = false;
        boolean blockComment = false;
        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : '\0';
            char previous = i > 0 ? source.charAt(i - 1) : '\0';
            if (lineComment) {
                if (ch == '\n') {
                    lineComment = false;
                    code[i] = true;
                }
                continue;
            }
            if (blockComment) {
                if (ch == '*' && next == '/') {
                    blockComment = false;
                    i++;
                }
                continue;
            }
            if (single) {
                if (ch == '\'' && previous != '\\') {
                    single = false;
                }
                continue;
            }
            if (dbl) {
                if (ch == '"' && previous != '\\') {
                    dbl = false;
                }
                continue;
            }
            if (template) {
                if (ch == '`' && previous != '\\') {
                    template = false;
                }
                continue;
            }
            if (ch == '/' && next == '/') {
                lineComment = true;
                i++;
            } else if (ch == '/' && next == '*') {
                blockComment = true;
                i++;
            } else if (ch == '\'') {
                single = true;
            } else if (ch == '"') {
                dbl = true;
            } else if (ch == '`') {
                template = true;
            } else {
                code[i] = true;
            }
        }
        return code;
    }

    private static boolean isCode(boolean[] mask, int index) {
        return index >= 0 && index < mask.length && mask[index];
    }

    private static boolean isBoundary(String source, int index) {
        return index >= source.length() || !isIdentifierPart(source.charAt(index));
    }

    private static boolean isIdentifierPart(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_' || ch == '$';
    }

    private static String sourceLine(String source, int index) {
        int start = index;
        while (start > 0 && source.charAt(start - 1) != '\n') {
            start--;
        }
        int end = index;
        while (end < source.length() && source.charAt(end) != '\n') {
            end++;
        }
        return source.substring(start, end);
    }

    private static int[] lineCol(String source, int index) {
        int line = 1;
        int col = 1;
        for (int i = 0; i < index && i < source.length(); i++) {
            if (source.charAt(i) == '\n') {
                line++;
                col = 1;
            } else {
                col++;
            }
        }
        return new int[] {line, col};
    }

    public record Result(int allowedDynamicWrapperCount) {
    }

    private record ScanResult(int allowed, List<Finding> findings) {
        private void throwIfFindings() {
            if (!findings.isEmpty()) {
                throw new IllegalStateException("Generated TS static admission failed: " + findings);
            }
        }
    }

    private record Finding(Path file, int line, int column, String code, String source) {
    }
}
