package com.qin.lang.ir;

import java.util.Objects;

/**
 * Java import binding from ESM-like syntax:
 * import { Math } from "java:java.lang"
 */
public record QinIrJavaImport(
        String moduleName,
        String importedName,
        String localName,
        String ownerBinaryName) {
    public QinIrJavaImport {
        Objects.requireNonNull(moduleName, "moduleName cannot be null");
        Objects.requireNonNull(importedName, "importedName cannot be null");
        Objects.requireNonNull(localName, "localName cannot be null");
        Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
    }
}
