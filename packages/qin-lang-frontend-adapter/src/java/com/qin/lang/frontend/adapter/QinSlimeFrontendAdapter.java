package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Minimal adapter that lowers a tiny source subset into Qin IR.
 * This is a POC parser for one grammar shape.
 */
public final class QinSlimeFrontendAdapter {
    private static final Pattern CONST_OBJECT_PATTERN = Pattern.compile(
            "^const\\s+([A-Za-z_$][A-Za-z0-9_$]*)\\s*=\\s*\\{\\s*([A-Za-z_$][A-Za-z0-9_$]*)\\s*:\\s*([0-9]+)\\s*}\\s*;?$");
    private static final Pattern CONSOLE_LOG_MEMBER_PATTERN = Pattern.compile(
            "^console\\.log\\(\\s*([A-Za-z_$][A-Za-z0-9_$]*)\\s*\\.\\s*([A-Za-z_$][A-Za-z0-9_$]*)\\s*\\)\\s*;?$");

    public QinIrProgram parseProgram(String source) {
        Objects.requireNonNull(source, "source cannot be null");

        List<String> statements = source.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
        if (statements.isEmpty()) {
            throw new IllegalArgumentException("Source cannot be empty");
        }

        Matcher constMatcher = CONST_OBJECT_PATTERN.matcher(statements.get(0));
        if (!constMatcher.matches()) {
            throw new IllegalArgumentException(
                    "Current POC first statement must be: const <id> = { <id>: <int> }");
        }

        String constName = constMatcher.group(1);
        String propertyName = constMatcher.group(2);
        int numericValue = Integer.parseInt(constMatcher.group(3));
        QinIrObjectLiteral objectLiteral = new QinIrObjectLiteral(
                List.of(new QinIrObjectProperty(propertyName, new QinIrNumberLiteral(numericValue))));
        QinIrConstDeclaration declaration = new QinIrConstDeclaration(constName, objectLiteral);

        List<QinIrConsoleLogStatement> consoleLogs = new ArrayList<>();
        for (int i = 1; i < statements.size(); i++) {
            String statement = statements.get(i);
            Matcher consoleMatcher = CONSOLE_LOG_MEMBER_PATTERN.matcher(statement);
            if (!consoleMatcher.matches()) {
                throw new IllegalArgumentException(
                        "Current POC only supports: console.log(<id>.<id>) after the const declaration");
            }
            consoleLogs.add(new QinIrConsoleLogStatement(consoleMatcher.group(1), consoleMatcher.group(2)));
        }

        return new QinIrProgram(List.of(declaration), consoleLogs);
    }

    public QinIrProgram parseConstObjectDeclaration(String source) {
        return parseProgram(source);
    }
}
