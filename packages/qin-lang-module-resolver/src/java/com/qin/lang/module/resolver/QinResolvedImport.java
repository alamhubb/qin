package com.qin.lang.module.resolver;

import com.qin.lang.module.policy.QinImportDescriptor;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Import descriptor with optional resolved local target module.
 */
public record QinResolvedImport(
        QinImportDescriptor descriptor,
        Path resolvedModule) {
    public QinResolvedImport {
        Objects.requireNonNull(descriptor, "descriptor cannot be null");
    }
}
