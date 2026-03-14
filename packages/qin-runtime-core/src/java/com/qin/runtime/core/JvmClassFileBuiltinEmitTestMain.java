package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies generated classfile contains JS-SDK runtime references.
 */
public final class JvmClassFileBuiltinEmitTestMain {
    private JvmClassFileBuiltinEmitTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinJsSdkTestPaths.resolveJsSdkRoot();
        Path source = root.resolve("main/main.js").normalize();
        Path classOut = root.resolve("build/classfile-emit/classes");

        QinBuildRequest request = new QinBuildRequest(
                root,
                source,
                QinBuildTarget.JVM,
                "com.qin.runtime.generated.JsSdkEmit",
                classOut,
                root.resolve("build/classfile-emit/app.js"),
                false);
        QinBuildResult result = new QinBuildCoordinator().build(request);
        Path classFile = result.classFile();
        if (classFile == null || !Files.exists(classFile)) {
            throw new IllegalStateException("Missing generated class file");
        }

        byte[] bytes = Files.readAllBytes(classFile);
        String content = new String(bytes, StandardCharsets.ISO_8859_1);
        requireContains(content, "com/qin/lang/runtime/QinConsole");
        requireContains(content, "com/qin/lang/runtime/JavaEsmMath");
        requireContains(content, "com/qin/lang/runtime/JavaEsmJson");

        System.out.println("JvmClassFileBuiltinEmitTestMain passed.");
        System.out.println("class file: " + classFile.toAbsolutePath());
    }

    private static void requireContains(String content, String token) {
        if (!content.contains(token)) {
            throw new IllegalStateException("Expected classfile constant pool token: " + token);
        }
    }
}
