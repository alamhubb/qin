package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies npm packages compile only when code stays inside Qin subset.
 */
public final class NpmSubsetCompileTestMain {
    private NpmSubsetCompileTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinJsSdkTestPaths.resolveNpmBareRoot();
        compileOk(root.resolve("main/main.js"), "com.qin.runtime.generated.NpmSubsetOk");
        compileOk(root.resolve("main/default-export.js"), "com.qin.runtime.generated.NpmSubsetDefaultOk");
        compileFail(root.resolve("main/unsupported.js"), "QJS2");
        System.out.println("NpmSubsetCompileTestMain passed.");
    }

    private static void compileOk(Path source, String className) throws Exception {
        Path root = source.getParent().getParent().normalize();
        Path classOut = root.resolve("build/npm-subset/classes");
        Files.createDirectories(classOut);
        QinBuildRequest request = new QinBuildRequest(
                root,
                source,
                QinBuildTarget.JVM,
                className,
                classOut,
                root.resolve("build/npm-subset/app.js"),
                false);
        QinBuildResult result = new QinBuildCoordinator().build(request);
        if (result.classFile() == null || !Files.exists(result.classFile())) {
            throw new IllegalStateException("Expected class output for " + source.toAbsolutePath());
        }
    }

    private static void compileFail(Path source, String expectedCode) throws Exception {
        Path root = source.getParent().getParent().normalize();
        QinBuildRequest request = new QinBuildRequest(
                root,
                source,
                QinBuildTarget.JVM,
                "com.qin.runtime.generated.NpmSubsetFail",
                root.resolve("build/npm-subset/classes"),
                root.resolve("build/npm-subset/app.js"),
                false);
        try {
            new QinBuildCoordinator().build(request);
        } catch (Exception ex) {
            String msg = ex.getMessage() == null ? "" : ex.getMessage();
            if (!msg.contains(expectedCode)) {
                throw new IllegalStateException(
                        "Expected error code " + expectedCode + " but got: " + msg, ex);
            }
            return;
        }
        throw new IllegalStateException("Expected compile failure for " + source.toAbsolutePath());
    }
}
