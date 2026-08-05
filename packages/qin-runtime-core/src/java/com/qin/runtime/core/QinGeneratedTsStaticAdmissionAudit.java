package com.qin.runtime.core;

import com.qin.parser.QinParserFacade;
import com.slime.ast.AstNode;
import com.slime.ast.Position;
import com.slime.ast.SourceLocation;
import com.slime.ast.nodes.expressions.CallExpression;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.expressions.MemberExpression;

import java.lang.reflect.Array;
import java.lang.reflect.RecordComponent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Audits dynamic-looking generated TypeScript shapes that are allowed only
 * when the Java-to-TS compiler emitted a proven static wrapper contract.
 */
public final class QinGeneratedTsStaticAdmissionAudit {
    private static final Pattern STATIC_ADMISSION_CONTRACT_PATTERN = Pattern.compile(
            "/\\*\\s*@qin-static-admission\\s+([^*]+?)\\s*\\*/",
            Pattern.DOTALL);
    private static final Pattern CONTRACT_OWNER_PATTERN = Pattern.compile(
            "[A-Za-z_$][A-Za-z0-9_$]*(?:[.$_][A-Za-z_$][A-Za-z0-9_$]*)*");
    private static final Pattern CONTRACT_METHOD_PATTERN = Pattern.compile(
            "[A-Za-z_$][A-Za-z0-9_$]*");
    private static final Pattern CONTRACT_RECEIVER_PATTERN = Pattern.compile(
            "(?:this|super|[A-Za-z_$][A-Za-z0-9_$]*(?:[.$_][A-Za-z_$][A-Za-z0-9_$]*)*)");
    private static final Pattern CONTRACT_ARITY_PATTERN = Pattern.compile(
            "(?:0|[1-9][0-9]*|bound|spread)");

    private QinGeneratedTsStaticAdmissionAudit() {
    }

    public static Result audit(List<QinJavaProjectJsCompiler.EsmFileOutput> outputs) {
        List<Finding> findings = new ArrayList<>();
        int allowed = 0;
        int contractAllowed = 0;
        int legacyAllowed = 0;
        Map<String, Integer> legacyReasons = new TreeMap<>();
        for (QinJavaProjectJsCompiler.EsmFileOutput output : outputs) {
            ScanResult result = scanOne(output.outputFile(), output.code());
            allowed += result.allowed();
            contractAllowed += result.contractAllowed();
            legacyAllowed += result.legacyAllowed();
            for (Map.Entry<String, Integer> entry : result.legacyReasons().entrySet()) {
                legacyReasons.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
            findings.addAll(result.findings());
        }
        if (!findings.isEmpty()) {
            throw new IllegalStateException("Generated TS static admission failed: " + findings);
        }
        return new Result(allowed, contractAllowed, legacyAllowed, Map.copyOf(legacyReasons));
    }

    public static void assertRejectsUnprovenDynamicShapes() {
        assertRejectsGeneratedTsDynamicShape(
                "call",
                """
                        export function bad(method, receiver) {
                          return method.call(receiver);
                        }
                        """);
        assertRejectsGeneratedTsDynamicShape(
                "apply",
                """
                        export function bad(method, receiver, args) {
                          return method.apply(receiver, args);
                        }
                        """);
        assertRejectsGeneratedTsDynamicShape(
                "bind",
                """
                        export function bad(method, receiver) {
                          return method.bind(receiver);
                        }
                        """);
        assertAllowsGeneratedTsContract(
                "call",
                """
                        export function generated(method, receiver) {
                          return /* @qin-static-admission member=call owner=com.example.Generated method=rule receiver=receiver arity=0 */ method.call(receiver);
                        }
                        """);
        assertAllowsGeneratedTsContract(
                "call",
                """
                        export function generatedStaticCall(owner, first, second) {
                          return /* @qin-static-admission member=call owner=com.example.Generated method=call receiver=owner arity=2 */ owner.call(first, second);
                        }
                        """);
        assertRejectsGeneratedTsDynamicShape(
                "call",
                """
                        export function bad(method, receiver) {
                          return /* @qin-static-admission member=call owner=com.example.Generated method=rule receiver=receiver arity=2 */ method.call(receiver);
                        }
                        """);
        assertRejectsGeneratedTsDynamicShape(
                "bind",
                """
                        export function bad(method, receiver) {
                          return /* @qin-static-admission member=bind owner=com.example.Generated method=rule receiver=receiver arity=maybe */ method.bind(receiver);
                        }
                        """);
        assertRejectsGeneratedTsDynamicShape(
                "apply",
                """
                        export function bad(method, receiver, args) {
                          return /* @qin-static-admission member=apply owner=com.example.Generated method=rule receiver=receiver-name arity=1 */ method.apply(receiver, args);
                        }
                        """);
    }

    private static void assertRejectsGeneratedTsDynamicShape(String member, String source) {
        try {
            scanOne(Path.of("memory-unproven-generated.ts"), source).throwIfFindings();
        } catch (IllegalStateException expected) {
            if (!expected.getMessage().contains("QIN_GENERATED_TS_DYNAMIC_FUNCTION_" + member.toUpperCase(Locale.ROOT))) {
                throw new IllegalStateException("Unexpected generated TS audit diagnostic", expected);
            }
            return;
        }
        throw new IllegalStateException("Expected generated TS audit to reject unproven method." + member);
    }

    private static ScanResult scanOne(Path file, String source) {
        String code = source == null ? "" : source;
        List<Finding> findings = new ArrayList<>();
        GeneratedAdmissionContracts contracts = GeneratedAdmissionContracts.parse(code);
        int allowed = 0;
        int contractAllowed = 0;
        for (DynamicFunctionCall occurrence : dynamicFunctionCallsFromAst(file, code)) {
            if (contracts.admit(occurrence)) {
                allowed++;
                contractAllowed++;
                continue;
            }
            findings.add(new Finding(
                    file,
                    occurrence.line(),
                    occurrence.column(),
                    "QIN_GENERATED_TS_DYNAMIC_FUNCTION_" + occurrence.member().toUpperCase(Locale.ROOT),
                    occurrence.sourceLine().trim()));
        }
        return new ScanResult(
                allowed,
                contractAllowed,
                0,
                Map.of(),
                List.copyOf(findings));
    }

    private static void assertAllowsGeneratedTsContract(String member, String source) {
        ScanResult result = scanOne(Path.of("memory-proven-generated.ts"), source);
        result.throwIfFindings();
        if (result.allowed() != 1) {
            throw new IllegalStateException(
                    "Expected generated TS contract to admit one method."
                            + member
                            + ", got "
                            + result.allowed());
        }
        if (result.contractAllowed() != 1 || result.legacyAllowed() != 0) {
            throw new IllegalStateException(
                    "Expected generated TS contract admission to avoid legacy predicates for method."
                            + member
                            + ", got contract="
                            + result.contractAllowed()
                            + ", legacy="
                            + result.legacyAllowed());
        }
    }

    private static List<DynamicFunctionCall> dynamicFunctionCallsFromAst(Path file, String source) {
        AstNode program = new QinParserFacade().parseSource(source).requireProgram();
        List<DynamicFunctionCall> calls = new ArrayList<>();
        collectDynamicFunctionCalls(file, source, program, calls, newIdentitySet());
        return List.copyOf(calls);
    }

    private static void collectDynamicFunctionCalls(
            Path file,
            String source,
            Object node,
            List<DynamicFunctionCall> calls,
            Set<Object> seen) {
        if (node == null || isScalar(node) || !seen.add(node)) {
            return;
        }
        if (node instanceof CallExpression callExpression
                && callExpression.callee() instanceof MemberExpression memberExpression
                && !memberExpression.computed()
                && memberExpression.property() instanceof Identifier propertyIdentifier) {
            String member = propertyIdentifier.name();
            if (isDynamicFunctionMember(member)) {
                calls.add(dynamicFunctionCall(file, source, callExpression, memberExpression, propertyIdentifier, member));
            }
        }
        if (node instanceof Collection<?> collection) {
            for (Object item : collection) {
                collectDynamicFunctionCalls(file, source, item, calls, seen);
            }
            return;
        }
        if (node instanceof Map<?, ?> map) {
            for (Object value : map.values()) {
                collectDynamicFunctionCalls(file, source, value, calls, seen);
            }
            return;
        }
        Class<?> type = node.getClass();
        if (type.isArray()) {
            int length = Array.getLength(node);
            for (int i = 0; i < length; i++) {
                collectDynamicFunctionCalls(file, source, Array.get(node, i), calls, seen);
            }
            return;
        }
        if (!type.isRecord()) {
            return;
        }
        for (RecordComponent component : type.getRecordComponents()) {
            try {
                collectDynamicFunctionCalls(file, source, component.getAccessor().invoke(node), calls, seen);
            } catch (ReflectiveOperationException error) {
                throw new IllegalStateException(
                        "Cannot inspect generated TS AST component "
                                + type.getName()
                                + "."
                                + component.getName()
                                + " while auditing "
                                + file,
                        error);
            }
        }
    }

    private static DynamicFunctionCall dynamicFunctionCall(
            Path file,
            String source,
            CallExpression callExpression,
            MemberExpression memberExpression,
            Identifier propertyIdentifier,
            String member) {
        int propertyIndex = sourceIndex(propertyIdentifier.location());
        int dotIndex = dotIndexBefore(source, propertyIndex, member);
        int[] lineCol = lineCol(source, dotIndex >= 0 ? dotIndex : Math.max(0, propertyIndex));
        return new DynamicFunctionCall(
                file,
                member,
                dotIndex,
                lineCol[0],
                lineCol[1],
                sourceLine(source, dotIndex >= 0 ? dotIndex : Math.max(0, propertyIndex)),
                callExpression.arguments().size(),
                memberExpression);
    }

    private static int sourceIndex(SourceLocation location) {
        if (location == null || location.start() == null) {
            return -1;
        }
        Position start = location.start();
        return start.index();
    }

    private static int dotIndexBefore(String source, int propertyIndex, String member) {
        if (source == null || source.isEmpty()) {
            return -1;
        }
        int start = propertyIndex >= 0 && propertyIndex < source.length()
                ? propertyIndex
                : source.indexOf("." + member);
        if (start < 0) {
            return -1;
        }
        for (int i = start; i >= 0; i--) {
            char ch = source.charAt(i);
            if (ch == '.') {
                return i;
            }
            if (!Character.isWhitespace(ch) && i < start && !isIdentifierPart(ch)) {
                break;
            }
        }
        int fallback = source.indexOf("." + member, Math.max(0, start - 120));
        return fallback >= 0 ? fallback : -1;
    }

    private static boolean isDynamicFunctionMember(String member) {
        return "call".equals(member) || "apply".equals(member) || "bind".equals(member);
    }

    private static <T> Set<T> newIdentitySet() {
        return java.util.Collections.newSetFromMap(new IdentityHashMap<>());
    }

    private static boolean isScalar(Object value) {
        return value instanceof String
                || value instanceof Number
                || value instanceof Boolean
                || value instanceof Character
                || value instanceof Enum<?>;
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

    public record Result(
            int allowedDynamicWrapperCount,
            int contractAllowedDynamicWrapperCount,
            int legacyAllowedDynamicWrapperCount,
            Map<String, Integer> legacyAllowedDynamicWrapperReasons) {
    }

    private record DynamicFunctionCall(
            Path file,
            String member,
            int dotIndex,
            int line,
            int column,
            String sourceLine,
            int argumentCount,
            MemberExpression memberExpression) {
    }

    private static final class GeneratedAdmissionContracts {
        private final List<GeneratedAdmissionContract> contracts;
        private final Set<GeneratedAdmissionContract> consumed = newIdentitySet();

        private GeneratedAdmissionContracts(List<GeneratedAdmissionContract> contracts) {
            this.contracts = contracts;
        }

        private static GeneratedAdmissionContracts parse(String source) {
            List<GeneratedAdmissionContract> parsed = new ArrayList<>();
            Matcher matcher = STATIC_ADMISSION_CONTRACT_PATTERN.matcher(source == null ? "" : source);
            while (matcher.find()) {
                Map<String, String> attributes = parseAttributes(matcher.group(1));
                parsed.add(new GeneratedAdmissionContract(
                        matcher.start(),
                        matcher.end(),
                        attributes.get("member"),
                        attributes.get("owner"),
                        attributes.get("method"),
                        attributes.get("receiver"),
                        attributes.get("arity")));
            }
            return new GeneratedAdmissionContracts(List.copyOf(parsed));
        }

        private boolean admit(DynamicFunctionCall occurrence) {
            for (GeneratedAdmissionContract contract : contracts) {
                if (consumed.contains(contract)
                        || !contract.matches(occurrence)
                        || !contract.isNear(occurrence)) {
                    continue;
                }
                consumed.add(contract);
                return true;
            }
            return false;
        }

        private static Map<String, String> parseAttributes(String raw) {
            Map<String, String> attributes = new java.util.LinkedHashMap<>();
            if (raw == null || raw.isBlank()) {
                return attributes;
            }
            String[] parts = raw.trim().split("\\s+");
            for (String part : parts) {
                int equals = part.indexOf('=');
                if (equals <= 0 || equals == part.length() - 1) {
                    continue;
                }
                attributes.put(part.substring(0, equals), part.substring(equals + 1));
            }
            return Map.copyOf(attributes);
        }
    }

    private record GeneratedAdmissionContract(
            int start,
            int end,
            String member,
            String owner,
            String method,
            String receiver,
            String arity) {
        private boolean matches(DynamicFunctionCall occurrence) {
            return member != null
                    && !member.isBlank()
                    && member.equals(occurrence.member())
                    && isValidOwner(owner)
                    && isValidMethod(method)
                    && isValidReceiver(receiver)
                    && isValidArity(arity)
                    && isApplicableArity(occurrence);
        }

        private boolean isNear(DynamicFunctionCall occurrence) {
            return occurrence.dotIndex() >= end && occurrence.dotIndex() - end <= 320;
        }

        private boolean isApplicableArity(DynamicFunctionCall occurrence) {
            if (!"call".equals(member) || "bound".equals(arity) || "spread".equals(arity)) {
                return true;
            }
            int declaredArity;
            try {
                declaredArity = Integer.parseInt(arity);
            } catch (NumberFormatException error) {
                return false;
            }
            if ("call".equals(method)) {
                return occurrence.argumentCount() == declaredArity;
            }
            return Math.max(0, occurrence.argumentCount() - 1) == declaredArity;
        }

        private static boolean isValidOwner(String value) {
            return value != null && CONTRACT_OWNER_PATTERN.matcher(value).matches();
        }

        private static boolean isValidMethod(String value) {
            return value != null && CONTRACT_METHOD_PATTERN.matcher(value).matches();
        }

        private static boolean isValidReceiver(String value) {
            return value != null && CONTRACT_RECEIVER_PATTERN.matcher(value).matches();
        }

        private static boolean isValidArity(String value) {
            return value != null && CONTRACT_ARITY_PATTERN.matcher(value).matches();
        }
    }

    private record ScanResult(
            int allowed,
            int contractAllowed,
            int legacyAllowed,
            Map<String, Integer> legacyReasons,
            List<Finding> findings) {
        private void throwIfFindings() {
            if (!findings.isEmpty()) {
                throw new IllegalStateException("Generated TS static admission failed: " + findings);
            }
        }
    }

    private record Finding(Path file, int line, int column, String code, String source) {
    }
}
