package com.qin.lang.ir;

import java.util.Objects;

/**
 * JavaScript import binding from ESM-like syntax:
 * import { foo as bar } from "./lib.js"
 */
public record QinIrJsImport(
        String moduleName,
        String importedName,
        String localName) {
    public QinIrJsImport {
        Objects.requireNonNull(moduleName, "moduleName cannot be null");
        Objects.requireNonNull(importedName, "importedName cannot be null");
        Objects.requireNonNull(localName, "localName cannot be null");
    }
}
