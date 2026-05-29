package com.qin.lang.cli;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.backend.jvm.QinClassFileWriter;
import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Fullstack demo builder and launcher:
 * shared + server -> .class, shared + web -> .js.
 */
public final class QinFullstackDemoMain {
    private QinFullstackDemoMain() {
    }

    public static void main(String[] args) throws Exception {
        Path demoRoot = resolveDemoRoot(args);

        Path sharedFile = demoRoot.resolve("packages/shared/src/qin/shared.js");
        Path serverFile = demoRoot.resolve("packages/server/src/qin/server.js");
        Path webFile = demoRoot.resolve("packages/web/src/qin/web.js");
        validateInputs(sharedFile, serverFile, webFile);

        String sharedSource = Files.readString(sharedFile, StandardCharsets.UTF_8).trim();
        String serverSource = Files.readString(serverFile, StandardCharsets.UTF_8).trim();
        String webSource = Files.readString(webFile, StandardCharsets.UTF_8).trim();

        String serverProgramSource = sharedSource + System.lineSeparator() + System.lineSeparator() + serverSource;
        String webProgramSource = sharedSource + System.lineSeparator() + System.lineSeparator() + webSource;

        QinFrontendLowerer lowerer = new QinFrontendLowerer();

        QinIrProgram serverProgram = lowerer.lowerSource(serverProgramSource);
        QinIrProgram webProgram = lowerer.lowerSource(webProgramSource);

        Path buildDir = demoRoot.resolve("build");
        Path serverOutputDir = buildDir.resolve("server-classes");
        Path webOutputDir = buildDir.resolve("web");
        Files.createDirectories(serverOutputDir);
        Files.createDirectories(webOutputDir);

        String serverClassName = "com.qin.demo.ServerApp";
        QinJvmClassFileBackend jvmBackend = new QinJvmClassFileBackend();
        byte[] classBytes = jvmBackend.compileProgram(serverProgram, serverClassName);
        Path classFile = QinClassFileWriter.writeClassFile(serverOutputDir, serverClassName, classBytes);

        QinJsBackend jsBackend = new QinJsBackend();
        String jsCode = jsBackend.compileProgram(webProgram);
        Path webJsFile = webOutputDir.resolve("app.js");
        Files.writeString(webJsFile, jsCode, StandardCharsets.UTF_8);

        System.out.println("Demo root: " + demoRoot.toAbsolutePath());
        System.out.println("Generated server class: " + classFile.toAbsolutePath());
        System.out.println("Generated web js: " + webJsFile.toAbsolutePath());
        System.out.println();

        runGeneratedClass(serverClassName, classBytes);
        runGeneratedJs(webJsFile);
    }

    private static Path resolveDemoRoot(String[] args) {
        if (args.length > 0 && !args[0].isBlank()) {
            return Path.of(args[0]).toAbsolutePath().normalize();
        }
        Path cwd = Path.of("").toAbsolutePath().normalize();

        Path[] candidates = new Path[]{
                cwd.resolve("qin/examples/qin-fullstack-demo"),
                cwd.resolve("../examples/qin-fullstack-demo"),
                cwd.resolve("examples/qin-fullstack-demo")
        };
        for (Path candidate : candidates) {
            if (Files.exists(candidate.resolve("qin.config.json"))) {
                return candidate;
            }
        }
        throw new IllegalArgumentException(
                "Cannot find demo root. Pass it as arg[0], for example: qin/examples/qin-fullstack-demo");
    }

    private static void validateInputs(Path sharedFile, Path serverFile, Path webFile) {
        if (!Files.exists(sharedFile)) {
            throw new IllegalArgumentException("Missing shared source: " + sharedFile.toAbsolutePath());
        }
        if (!Files.exists(serverFile)) {
            throw new IllegalArgumentException("Missing server source: " + serverFile.toAbsolutePath());
        }
        if (!Files.exists(webFile)) {
            throw new IllegalArgumentException("Missing web source: " + webFile.toAbsolutePath());
        }
    }

    private static void runGeneratedClass(String className, byte[] classBytes) throws Exception {
        System.out.println("[server] running generated class...");
        Class<?> generatedClass = new ByteArrayClassLoader(QinFullstackDemoMain.class.getClassLoader())
                .define(className, classBytes);
        Object result = generatedClass.getMethod("run").invoke(null);
        System.out.println("[server] run() result: " + result);
        System.out.println();
    }

    private static void runGeneratedJs(Path jsFile) throws Exception {
        System.out.println("[web] running generated js with Node...");
        ProcessBuilder pb = new ProcessBuilder("node", jsFile.toAbsolutePath().toString());
        pb.redirectErrorStream(true);
        Process process = pb.start();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[web] " + line);
            }
        }
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("Node exited with code " + exitCode);
        }
        System.out.println("[web] node run finished");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private ByteArrayClassLoader(ClassLoader parent) {
            super(parent);
        }

        private Class<?> define(String binaryName, byte[] classBytes) {
            return defineClass(binaryName, classBytes, 0, classBytes.length);
        }
    }
}
