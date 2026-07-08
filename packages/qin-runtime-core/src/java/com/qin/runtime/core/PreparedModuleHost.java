package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Set;

record PreparedModuleHost(
        Path projectRoot,
        Path wrapperDir,
        Path wrapperFile,
        Path nodeModules,
        Set<String> activePackages,
        String dependencyFingerprint,
        long materializeMs,
        long dependencyFingerprintMs,
        long writeWrapperMs,
        long totalPrepareMs) {
}
