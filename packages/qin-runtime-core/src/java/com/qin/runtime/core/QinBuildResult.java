package com.qin.runtime.core;

import com.qin.lang.ir.QinIrProgram;

import java.nio.file.Path;

/**
 * Build result returned by coordinator.
 */
public record QinBuildResult(
        QinRuntimeProjectLayout layout,
        Path sourceFile,
        QinIrProgram program,
        Path classFile,
        Path jsFile) {
}
