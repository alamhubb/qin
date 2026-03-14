package com.qin.conformance;

import com.qin.conformance.QinConformanceModels.CaseExecution;
import com.qin.runtime.core.QinBuildCoordinator;
import com.qin.runtime.core.QinBuildRequest;
import com.qin.runtime.core.QinBuildResult;
import com.qin.runtime.core.QinBuildTarget;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Compiles and runs one JS entry through Qin JVM/CFA pipeline.
 */
public final class QinJvmRunner {
    private QinJvmRunner() {
    }

    public static CaseExecution run(Path projectRoot, Path entryFile, String className) {
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream capture = new PrintStream(output, true, StandardCharsets.UTF_8);

        try {
            System.setOut(capture);
            System.setErr(capture);

            Path outDir = projectRoot.resolve("build").resolve("conformance").resolve("classes");
            Path jsOut = projectRoot.resolve("build").resolve("conformance").resolve("app.js");
            Files.createDirectories(outDir);
            QinBuildRequest request = new QinBuildRequest(
                    projectRoot,
                    entryFile,
                    QinBuildTarget.JVM,
                    className,
                    outDir,
                    jsOut,
                    false);
            QinBuildResult result = new QinBuildCoordinator().build(request);
            Path classFile = result.classFile();
            if (classFile == null || !Files.exists(classFile)) {
                return new CaseExecution("FAIL", "MissingClassFile", "Class file was not generated", capture(output));
            }
            invokeRunMethod(className, classFile);
            return new CaseExecution("PASS", "", "", capture(output));
        } catch (Exception ex) {
            Throwable root = rootCause(ex);
            String msg = root.getMessage() == null ? "" : root.getMessage();
            String type = normalizeJsErrorType(root, msg);
            return new CaseExecution("FAIL", type, msg, capture(output));
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
            capture.close();
        }
    }

    private static void invokeRunMethod(String className, Path classFile) throws Exception {
        byte[] bytes = Files.readAllBytes(classFile);
        ByteArrayClassLoader loader = new ByteArrayClassLoader(QinJvmRunner.class.getClassLoader());
        Class<?> generated = loader.define(className, bytes);
        generated.getMethod("run").invoke(null);
    }

    private static Throwable rootCause(Throwable throwable) {
        Throwable cursor = throwable;
        while (cursor.getCause() != null && cursor.getCause() != cursor) {
            cursor = cursor.getCause();
        }
        return cursor;
    }

    private static String normalizeJsErrorType(Throwable root, String message) {
        String rawType = root.getClass().getSimpleName();
        String msg = message == null ? "" : message;
        if ("QinEsmSemanticException".equals(rawType)
                && (msg.contains("ESM2") || msg.contains("ESM3"))) {
            return "SyntaxError";
        }
        return rawType;
    }

    private static String capture(ByteArrayOutputStream output) {
        String text = output.toString(StandardCharsets.UTF_8);
        String trimmed = text == null ? "" : text.trim();
        return trimmed.length() > 1600 ? trimmed.substring(0, 1600) : trimmed;
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private ByteArrayClassLoader(ClassLoader parent) {
            super(parent);
        }

        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
