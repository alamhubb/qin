package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleSource;

import java.nio.file.Path;

/**
 * Internal Vue SFC compilation boundary for Qin frontend orchestration.
 *
 * <p>Long-term target:
 * route `.vue` compilation through the official `@vue/compiler-sfc`
 * package under Qin-managed package/module execution.
 *
 * <p>Current transition state:
 * runtime may still delegate to a legacy/bootstrap implementation.
 */
interface QinVueSfcCompiler {
    QinVueSfcModuleResult transpileVueModule(
            Path moduleFile,
            String source,
            QinModuleSource sourceModule,
            QinVueSpecifierRewriter specifierRewriter);

    default String transpileVueQueryModule(
            Path moduleFile,
            String source,
            String query,
            QinModuleSource sourceModule,
            QinVueSpecifierRewriter specifierRewriter) {
        return null;
    }
}
