package com.qin.parser;

import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJsImport;
import com.slime.ast.nodes.misc.Program;
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
            return new QinParsedSource(sourceForParser, sourceForParser, null, null, List.of(), List.of());
        }

        String parserInput = preprocessRuntimeSyntax(sourceForParser);
        try {
            SubhutiCst cst = createProgramCst(parserInput);
            Program programAst = createProgramAst(cst);
            return new QinParsedSource(sourceForParser, parserInput, cst, programAst, List.of(), List.of());
        } catch (Exception error) {
            writeFailureSnapshot(sourceForParser, parserInput, error);
            throw new IllegalArgumentException(
                    "Failed to parse Qin source with Qin parser facade.\n"
                            + "Make sure Slime Java modules are on classpath.\n"
                            + "Cause: " + safeMessage(error),
                    error);
        }
    }

    Program createProgramAst(String source) {
        return createProgramAst(createProgramCst(source));
    }

    SubhutiCst createProgramCst(String source) {
        QinParser parser = SubhutiParser.create(QinParser.class, source);
        parser.cache(true);
        SubhutiCst cst = parser.Program(QinParser.SourceType.MODULE);
        if (cst == null) {
            cst = parser.getCst();
        }
        if (cst == null) {
            throw new IllegalArgumentException("Qin parser returned null CST");
        }
        return cst;
    }

    Program createProgramAst(SubhutiCst cst) {
        Program programAst = QinProgramCstToAst.createProgramAst(cst);
        if (programAst == null) {
            throw new IllegalArgumentException("Slime CST->AST returned null Program");
        }
        return programAst;
    }

    private String preprocessRuntimeSyntax(String source) {
        String rewritten = source == null ? "" : source;
        rewritten = stripHashbang(rewritten);
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

}
