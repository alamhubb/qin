package com.qin.parser;

import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJsImport;
import com.slime.ast.nodes.misc.Program;
import com.slime.parser.cstToAst.SlimeCstToAstUtils;
import com.subhuti.parser.SubhutiParser;
import com.subhuti.struct.SubhutiCst;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stable Qin-owned parser facade.
 *
 * <p>This is the intended entry boundary for Qin source parsing so downstream
 * packages do not need to bind directly to raw Slime parser construction.
 */
public final class QinParserFacade {
    private static final DateTimeFormatter FAILURE_SNAPSHOT_TIME = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
    private static final Pattern IMPORT_LINE_PATTERN = Pattern.compile(
            "(?m)^\\s*import\\s+(?:[^;\\n]*?\\s+from\\s+)?[\"'][^\"'\\n]+[\"']\\s*;?\\s*$");
    private static final Pattern IMPORT_FROM_PATTERN = Pattern.compile(
            "^\\s*import\\s+(.+?)\\s+from\\s+[\"']([^\"'\\n]+)[\"']\\s*;?\\s*$");
    private static final Pattern IMPORT_TYPE_FROM_PATTERN = Pattern.compile(
            "^\\s*import\\s+type\\s+(.+?)\\s+from\\s+[\"']([^\"'\\n]+)[\"']\\s*;?\\s*$");
    private static final Pattern IMPORT_SIDE_EFFECT_PATTERN = Pattern.compile(
            "^\\s*import\\s+[\"']([^\"'\\n]+)[\"']\\s*;?\\s*$");
    private static final Pattern SOURCE_IMPORT_META_URL_PATTERN = Pattern.compile("\\bimport\\s*\\.\\s*meta\\s*\\.\\s*url\\b");
    private static final Pattern SOURCE_DYNAMIC_IMPORT_PATTERN = Pattern.compile("\\bimport\\s*\\(([^\\n\\)]*)\\)");
    private static final Pattern SOURCE_TYPEOF_DYNAMIC_IMPORT_SHIM_PATTERN = Pattern.compile(
            "\\btypeof\\s+" + Pattern.quote(QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM) + "\\s*\\(");
    private static final Pattern SOURCE_SIMPLE_SWITCH_PATTERN = Pattern.compile(
            "(?s)switch\\s*\\(([^\\)]*)\\)\\s*\\{([^\\{\\}]*)\\}");
    private static final Pattern SOURCE_HASHBANG_PATTERN = Pattern.compile("\\A#![^\\r\\n]*(\\r?\\n|\\z)");
    private static final int MAX_SIMPLE_SWITCH_REWRITES = 1;
    public Program parseProgram(String source) {
        return parseSource(source).requireProgram();
    }

    public QinParsedSource parseSource(String source) {
        Objects.requireNonNull(source, "source cannot be null");
        String sourceForParser = stripBom(source).trim();
        if (sourceForParser.isEmpty()) {
            return new QinParsedSource(sourceForParser, sourceForParser, null, List.of(), List.of());
        }

        String parserInput = preprocessRuntimeSyntax(sourceForParser);
        try {
            Program programAst = createProgramAst(parserInput);
            return new QinParsedSource(sourceForParser, parserInput, programAst, List.of(), List.of());
        } catch (Exception primaryError) {
            try {
                ExtractedImports extracted = extractImports(sourceForParser);
                if (!extracted.hasAnyImport()) {
                    throw primaryError;
                }
                String strippedSource = extracted.strippedSource().trim();
                if (strippedSource.isEmpty()) {
                    return new QinParsedSource(
                            sourceForParser,
                            strippedSource,
                            null,
                            extracted.javaImports(),
                            extracted.jsImports());
                }
                String strippedParserInput = preprocessRuntimeSyntax(strippedSource);
                Program programAst = createProgramAst(strippedParserInput);
                return new QinParsedSource(
                        sourceForParser,
                        strippedParserInput,
                        programAst,
                        extracted.javaImports(),
                        extracted.jsImports());
            } catch (Exception fallbackError) {
                Throwable cause = fallbackError == primaryError ? primaryError : fallbackError;
                String message = fallbackError == primaryError
                        ? safeMessage(primaryError)
                        : ("primary=" + safeMessage(primaryError) + "; fallback=" + safeMessage(fallbackError));
                writeFailureSnapshot(sourceForParser, parserInput, cause);
                throw new IllegalArgumentException(
                        "Failed to parse Qin source with Qin parser facade.\n"
                                + "Make sure Slime Java modules are on classpath.\n"
                                + "Cause: " + message,
                        cause);
            }
        }
    }

    Program createProgramAst(String source) {
        QinParser parser = SubhutiParser.create(QinParser.class, source);
        parser.cache(true);
        SubhutiCst cst = parser.Program(QinParser.SourceType.MODULE);
        if (cst == null) {
            cst = parser.getCst();
        }
        if (cst == null) {
            throw new IllegalArgumentException("Qin parser returned null CST");
        }
        Program programAst = SlimeCstToAstUtils.createProgramAst(cst);
        if (programAst == null) {
            throw new IllegalArgumentException("Slime CST->AST returned null Program");
        }
        return programAst;
    }

    private ExtractedImports extractImports(String source) {
        List<QinIrJavaImport> javaImports = new ArrayList<>();
        List<QinIrJsImport> jsImports = new ArrayList<>();
        Matcher matcher = IMPORT_LINE_PATTERN.matcher(source);
        StringBuilder stripped = new StringBuilder();
        int cursor = 0;
        while (matcher.find()) {
            stripped.append(source, cursor, matcher.start());
            String importLine = matcher.group();
            parseImportLine(importLine, javaImports, jsImports);
            cursor = matcher.end();
        }
        stripped.append(source, cursor, source.length());
        return new ExtractedImports(stripped.toString(), List.copyOf(javaImports), List.copyOf(jsImports));
    }

    private void parseImportLine(
            String importLine,
            List<QinIrJavaImport> javaImports,
            List<QinIrJsImport> jsImports) {
        if (IMPORT_TYPE_FROM_PATTERN.matcher(importLine).matches()) {
            return;
        }
        Matcher fromMatcher = IMPORT_FROM_PATTERN.matcher(importLine);
        if (fromMatcher.matches()) {
            String clause = fromMatcher.group(1).trim();
            String module = fromMatcher.group(2).trim();
            parseImportClause(clause, module, javaImports, jsImports);
            return;
        }
        Matcher sideEffectMatcher = IMPORT_SIDE_EFFECT_PATTERN.matcher(importLine);
        if (sideEffectMatcher.matches()) {
            String module = sideEffectMatcher.group(1).trim();
            if (module.startsWith("java:")) {
                throw new IllegalArgumentException("java: import does not support side-effect form: " + module);
            }
            if (isJsModule(module)) {
                jsImports.add(new QinIrJsImport(module, "", ""));
                return;
            }
            throw new IllegalArgumentException("Unsupported import module: " + module);
        }
        throw new IllegalArgumentException("Unsupported import syntax: " + importLine);
    }

    private void parseImportClause(
            String clause,
            String module,
            List<QinIrJavaImport> javaImports,
            List<QinIrJsImport> jsImports) {
        ParsedImportClause parsed = parseSpecifierClause(clause);
        if (module.startsWith("java:")) {
            String javaModule = module.substring("java:".length()).trim();
            if (javaModule.isBlank()) {
                throw new IllegalArgumentException("java: import module cannot be blank");
            }
            if (parsed.defaultLocalName() != null || parsed.namespaceLocalName() != null) {
                throw new IllegalArgumentException("Only named import specifier is supported for java: imports");
            }
            if (parsed.namedImports().isEmpty()) {
                throw new IllegalArgumentException("java: import requires named specifiers");
            }
            for (NamedImport named : parsed.namedImports()) {
                String ownerBinaryName = javaModule + "." + named.importedName();
                javaImports.add(new QinIrJavaImport(module, named.importedName(), named.localName(), ownerBinaryName));
            }
            return;
        }
        if (!isJsModule(module)) {
            throw new IllegalArgumentException("Unsupported import module: " + module);
        }
        if (parsed.defaultLocalName() != null) {
            jsImports.add(new QinIrJsImport(module, "default", parsed.defaultLocalName()));
        }
        if (parsed.namespaceLocalName() != null) {
            jsImports.add(new QinIrJsImport(module, "*", parsed.namespaceLocalName()));
        }
        for (NamedImport named : parsed.namedImports()) {
            jsImports.add(new QinIrJsImport(module, named.importedName(), named.localName()));
        }
    }

    private ParsedImportClause parseSpecifierClause(String clause) {
        String normalized = clause == null ? "" : clause.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Import clause cannot be empty");
        }

        String defaultLocalName = null;
        String namespaceLocalName = null;
        List<NamedImport> namedImports = new ArrayList<>();
        int topLevelComma = findTopLevelComma(normalized);
        String head = topLevelComma >= 0 ? normalized.substring(0, topLevelComma).trim() : normalized;
        String tail = topLevelComma >= 0 ? normalized.substring(topLevelComma + 1).trim() : "";

        if (head.startsWith("{")) {
            parseNamedImports(head, namedImports);
        } else if (head.startsWith("*")) {
            namespaceLocalName = parseNamespaceClause(head);
        } else {
            defaultLocalName = parseIdentifier(head, "default import local name");
        }

        if (!tail.isEmpty()) {
            if (tail.startsWith("{")) {
                parseNamedImports(tail, namedImports);
            } else if (tail.startsWith("*")) {
                if (namespaceLocalName != null) {
                    throw new IllegalArgumentException("Duplicated namespace import specifier");
                }
                namespaceLocalName = parseNamespaceClause(tail);
            } else {
                throw new IllegalArgumentException("Unsupported import specifier tail: " + tail);
            }
        }

        return new ParsedImportClause(defaultLocalName, namespaceLocalName, List.copyOf(namedImports));
    }

    private void parseNamedImports(String braceClause, List<NamedImport> namedImports) {
        String content = parseBraceContent(braceClause);
        if (content.isBlank()) {
            return;
        }
        for (String rawPart : content.split(",")) {
            String part = rawPart.trim();
            if (part.isEmpty()) {
                continue;
            }
            String importedName;
            String localName;
            int asIndex = indexOfKeywordAs(part);
            if (asIndex >= 0) {
                importedName = parseIdentifier(part.substring(0, asIndex).trim(), "named import imported name");
                localName = parseIdentifier(part.substring(asIndex + 4).trim(), "named import local name");
            } else {
                importedName = parseIdentifier(part, "named import imported name");
                localName = importedName;
            }
            namedImports.add(new NamedImport(importedName, localName));
        }
    }

    private String parseNamespaceClause(String clause) {
        String text = clause.trim();
        if (!text.startsWith("*")) {
            throw new IllegalArgumentException("Namespace import must start with '*': " + clause);
        }
        String remainder = text.substring(1).trim();
        if (!remainder.startsWith("as ")) {
            throw new IllegalArgumentException("Namespace import must use 'as': " + clause);
        }
        return parseIdentifier(remainder.substring(3).trim(), "namespace import local name");
    }

    private static String parseBraceContent(String text) {
        String trimmed = text.trim();
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
            throw new IllegalArgumentException("Named import specifier must be wrapped in braces: " + text);
        }
        return trimmed.substring(1, trimmed.length() - 1).trim();
    }

    private static String parseIdentifier(String text, String where) {
        String identifier = text == null ? "" : text.trim();
        if (!QinParserRuntimeNames.IDENTIFIER_PATTERN.matcher(identifier).matches()) {
            throw new IllegalArgumentException(where + " must be Identifier, got: " + text);
        }
        return identifier;
    }

    private static int findTopLevelComma(String text) {
        int braceDepth = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                braceDepth++;
                continue;
            }
            if (c == '}') {
                braceDepth = Math.max(0, braceDepth - 1);
                continue;
            }
            if (c == ',' && braceDepth == 0) {
                return i;
            }
        }
        return -1;
    }

    private static int indexOfKeywordAs(String text) {
        String normalized = " " + text.trim().replaceAll("\\s+", " ") + " ";
        int idx = normalized.indexOf(" as ");
        if (idx < 0) {
            return -1;
        }
        String collapsed = text.trim().replaceAll("\\s+", " ");
        return collapsed.indexOf(" as ");
    }

    private boolean isJsModule(String module) {
        return !module.startsWith("java:");
    }

    private String preprocessRuntimeSyntax(String source) {
        String rewritten = source == null ? "" : source;
        rewritten = stripHashbang(rewritten);
        rewritten = QinObjectSyntaxLowerer.lower(rewritten);
        rewritten = SOURCE_IMPORT_META_URL_PATTERN.matcher(rewritten)
                .replaceAll(QinParserRuntimeNames.IMPORT_META_URL_SHIM);
        rewritten = SOURCE_DYNAMIC_IMPORT_PATTERN.matcher(rewritten)
                .replaceAll(QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM + "($1)");
        rewritten = restoreTypeofImportQueries(rewritten);
        rewritten = rewriteSimpleSwitchStatements(rewritten);
        return rewritten;
    }

    private String stripHashbang(String source) {
        source = stripBom(source);
        if (source == null || !source.startsWith("#!")) {
            return source;
        }
        Matcher matcher = SOURCE_HASHBANG_PATTERN.matcher(source);
        if (!matcher.find()) {
            return source;
        }
        String lineEnding = matcher.group(1);
        if (lineEnding == null || lineEnding.isEmpty()) {
            return "";
        }
        return lineEnding + source.substring(matcher.end());
    }

    private String stripBom(String source) {
        if (source == null || source.isEmpty() || source.charAt(0) != '\uFEFF') {
            return source;
        }
        return source.substring(1);
    }

    private String restoreTypeofImportQueries(String source) {
        Matcher matcher = SOURCE_TYPEOF_DYNAMIC_IMPORT_SHIM_PATTERN.matcher(source);
        StringBuffer out = new StringBuffer();
        while (matcher.find()) {
            String matched = matcher.group();
            int openParen = matched.lastIndexOf('(');
            String replacement = matched.substring(0, matched.indexOf(QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM))
                    + "import"
                    + matched.substring(openParen);
            matcher.appendReplacement(out, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(out);
        return out.toString();
    }

    private String rewriteSimpleSwitchStatements(String source) {
        if (source == null || source.length() > 10_000) {
            return source;
        }
        Matcher matcher = SOURCE_SIMPLE_SWITCH_PATTERN.matcher(source);
        StringBuffer out = new StringBuffer();
        int switchId = 0;
        int rewrittenCount = 0;
        while (matcher.find()) {
            String discriminant = matcher.group(1);
            String body = matcher.group(2);
            String lowered = lowerSimpleSwitch(discriminant, body, switchId++);
            if (lowered == null || rewrittenCount >= MAX_SIMPLE_SWITCH_REWRITES) {
                matcher.appendReplacement(out, Matcher.quoteReplacement(matcher.group()));
            } else {
                rewrittenCount++;
                matcher.appendReplacement(out, Matcher.quoteReplacement(lowered));
            }
        }
        matcher.appendTail(out);
        return out.toString();
    }

    private String lowerSimpleSwitch(String discriminant, String body, int switchId) {
        if (discriminant == null || body == null) {
            return null;
        }
        String discriminantExpr = discriminant.trim();
        if (discriminantExpr.isEmpty()) {
            return null;
        }
        String tempName = "__qin_switch_" + switchId;
        List<String> caseExpressions = new ArrayList<>();
        List<String> caseReturns = new ArrayList<>();
        int position = 0;
        int caseCount = 0;
        String defaultReturn = null;
        while (position < body.length()) {
            position = skipWhitespace(body, position);
            if (position >= body.length()) {
                break;
            }
            if (startsWithWord(body, position, "case")) {
                int expressionStart = position + "case".length();
                int colon = body.indexOf(':', expressionStart);
                if (colon < 0) {
                    return null;
                }
                String caseExpression = body.substring(expressionStart, colon).trim();
                int next = findNextCaseOrDefault(body, colon + 1);
                String caseBlock = next < 0 ? body.substring(colon + 1) : body.substring(colon + 1, next);
                String returnExpression = extractReturnExpression(caseBlock);
                if (returnExpression == null) {
                    return null;
                }
                caseExpressions.add(caseExpression);
                caseReturns.add(returnExpression);
                caseCount++;
                position = next < 0 ? body.length() : next;
                continue;
            }
            if (startsWithWord(body, position, "default")) {
                int colon = body.indexOf(':', position + "default".length());
                if (colon < 0) {
                    return null;
                }
                int next = findNextCaseOrDefault(body, colon + 1);
                String defaultBlock = next < 0 ? body.substring(colon + 1) : body.substring(colon + 1, next);
                defaultReturn = extractReturnExpression(defaultBlock);
                position = next < 0 ? body.length() : next;
                continue;
            }
            return null;
        }
        if (caseCount == 0) {
            return null;
        }
        String fallback = defaultReturn == null ? "null" : defaultReturn;
        String reduced = fallback;
        for (int i = caseExpressions.size() - 1; i >= 0; i--) {
            reduced = "(" + tempName + " === " + caseExpressions.get(i) + " ? "
                    + caseReturns.get(i) + " : " + reduced + ")";
        }
        return "var " + tempName + " = " + discriminantExpr + "; return " + reduced + ";";
    }

    private int skipWhitespace(String text, int from) {
        int index = Math.max(0, from);
        while (index < text.length() && Character.isWhitespace(text.charAt(index))) {
            index++;
        }
        return index;
    }

    private boolean startsWithWord(String text, int from, String word) {
        if (from < 0 || from + word.length() > text.length()) {
            return false;
        }
        if (!text.regionMatches(from, word, 0, word.length())) {
            return false;
        }
        int before = from - 1;
        int after = from + word.length();
        boolean beforeOk = before < 0 || !Character.isLetterOrDigit(text.charAt(before)) && text.charAt(before) != '_';
        boolean afterOk = after >= text.length()
                || !Character.isLetterOrDigit(text.charAt(after)) && text.charAt(after) != '_';
        return beforeOk && afterOk;
    }

    private int findNextCaseOrDefault(String body, int from) {
        int best = -1;
        for (int i = Math.max(0, from); i < body.length(); i++) {
            if (startsWithWord(body, i, "case") || startsWithWord(body, i, "default")) {
                best = i;
                break;
            }
        }
        return best;
    }

    private String extractReturnExpression(String block) {
        if (block == null) {
            return null;
        }
        String text = block.trim();
        if (text.isEmpty() || text.startsWith("break")) {
            return null;
        }
        int returnIndex = text.indexOf("return");
        if (returnIndex < 0) {
            return null;
        }
        String afterReturn = text.substring(returnIndex + "return".length()).trim();
        int semicolon = afterReturn.indexOf(';');
        String expression = semicolon >= 0 ? afterReturn.substring(0, semicolon).trim() : afterReturn.trim();
        if (expression.isEmpty()) {
            return null;
        }
        return expression;
    }

    private static String safeMessage(Throwable throwable) {
        if (throwable == null) {
            return "<none>";
        }
        String msg = throwable.getMessage();
        if (msg == null || msg.isBlank()) {
            return throwable.getClass().getSimpleName();
        }
        return msg;
    }

    private void writeFailureSnapshot(String originalSource, String parserInput, Throwable throwable) {
        try {
            Path tempDir = Path.of(System.getProperty("java.io.tmpdir"), "qin-parser-failures");
            Files.createDirectories(tempDir);
            String stamp = LocalDateTime.now().format(FAILURE_SNAPSHOT_TIME);
            String baseName = "qin-parser-failure-" + stamp;
            Files.writeString(
                    tempDir.resolve(baseName + "-original.js"),
                    originalSource == null ? "" : originalSource,
                    StandardCharsets.UTF_8);
            Files.writeString(
                    tempDir.resolve(baseName + "-parser-input.js"),
                    parserInput == null ? "" : parserInput,
                    StandardCharsets.UTF_8);
            Files.writeString(
                    tempDir.resolve(baseName + "-error.txt"),
                    safeMessage(throwable),
                    StandardCharsets.UTF_8);
        } catch (IOException ignored) {
            // Keep parser failure behavior stable even when debug snapshot writing fails.
        }
    }

    private record ExtractedImports(
            String strippedSource,
            List<QinIrJavaImport> javaImports,
            List<QinIrJsImport> jsImports) {
        private boolean hasAnyImport() {
            return !javaImports.isEmpty() || !jsImports.isEmpty();
        }
    }

    private record NamedImport(String importedName, String localName) {
    }

    private record ParsedImportClause(
            String defaultLocalName,
            String namespaceLocalName,
            List<NamedImport> namedImports) {
    }
}
