package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;

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
            "^\\s*const\\s+([A-Za-z_$][A-Za-z0-9_$]*)\\s*=\\s*\\{\\s*([A-Za-z_$][A-Za-z0-9_$]*)\\s*:\\s*([0-9]+)\\s*}\\s*;?\\s*$");

    public QinIrProgram parseConstObjectDeclaration(String source) {
        Objects.requireNonNull(source, "source cannot be null");

        Matcher matcher = CONST_OBJECT_PATTERN.matcher(source);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Current POC supports only: const <id> = { <id>: <int> }");
        }

        String constName = matcher.group(1);
        String propertyName = matcher.group(2);
        int numericValue = Integer.parseInt(matcher.group(3));

        QinIrObjectLiteral objectLiteral = new QinIrObjectLiteral(
                List.of(new QinIrObjectProperty(propertyName, new QinIrNumberLiteral(numericValue))));
        QinIrConstDeclaration declaration = new QinIrConstDeclaration(constName, objectLiteral);
        return new QinIrProgram(List.of(declaration));
    }
}
