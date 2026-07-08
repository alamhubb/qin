package com.qin.parser;

import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJsImport;
import com.slime.ast.nodes.misc.Program;
import com.subhuti.struct.SubhutiCst;
import com.subhuti.struct.SubhutiMatchToken;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private static final Pattern SOURCE_HASHBANG_PATTERN = Pattern.compile("\\A#![^\\r\\n]*(\\r?\\n|\\z)");
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

    public Program createProgramAst(String source) {
        return createProgramAst(createProgramCst(source));
    }

    public SubhutiCst createProgramCst(String source) {
        QinParser parser = QinParserStaticEnhanced.create(source);
        parser.cache(true);
        SubhutiCst cst = parser.Program(QinParser.SourceType.MODULE);
        if (cst == null) {
            cst = parser.getCst();
        }
        ensureParserFinished(parser);
        if (cst == null) {
            throw new IllegalArgumentException("Qin parser returned null CST");
        }
        return cst;
    }

    public Program createProgramAst(SubhutiCst cst) {
        Program programAst = new QinProgramCstToAst().createProgramAst(cst);
        if (programAst == null) {
            throw new IllegalArgumentException("Slime CST->AST returned null Program");
        }
        return programAst;
    }

    private String preprocessRuntimeSyntax(String source) {
        String rewritten = source == null ? "" : source;
        rewritten = stripHashbang(rewritten);
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

    private void ensureParserFinished(QinParser parser) {
        if (parser.isParserFail()) {
            throw new IllegalArgumentException("Qin parser failed: " + parser.getErrorInfo());
        }
        SubhutiMatchToken token = parser.getCurToken();
        if (token == null) {
            String remaining = parser.getSourceCode().substring(parser.getCurrentIndex());
            if (remaining.isBlank()) {
                return;
            }
            throw new IllegalArgumentException(
                    "Qin parser left unconsumed source at position "
                            + parser.getCurrentIndex());
        }
        if (!token.isEof()) {
            throw new IllegalArgumentException(
                    "Qin parser left unconsumed source. Next token: "
                            + token.value()
                            + " ("
                            + token.tokenName()
                            + ") at position "
                            + token.index());
        }
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
