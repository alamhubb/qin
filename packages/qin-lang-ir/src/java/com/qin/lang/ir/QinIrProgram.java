package com.qin.lang.ir;

import java.util.List;
import java.util.Objects;

/**
 * Root Qin IR program node.
 */
public record QinIrProgram(List<QinIrConstDeclaration> declarations) {
    public QinIrProgram {
        Objects.requireNonNull(declarations, "declarations cannot be null");
        declarations = List.copyOf(declarations);
    }
}

