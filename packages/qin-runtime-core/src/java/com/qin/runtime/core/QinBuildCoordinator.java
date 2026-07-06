package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.backend.jvm.QinClassFileWriter;
import com.qin.lang.backend.jvm.QinJvmDeclarationClassEmitter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinCfaCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassFile;
import com.qin.lang.pipeline.cfa.QinCfaPipeline;
import com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Coordinates source resolution, frontend compile, IR validation and backend emissions.
 */
public final class QinBuildCoordinator {
    private final QinDependencyService dependencyService;
    private final QinSourceResolver sourceResolver;
    private final QinIrValidator irValidator;
    private final QinJsBackend jsBackend;
    private final QinCfaPipeline cfaPipeline;
    private final QinCompileSnapshotWriter snapshotWriter;

    public QinBuildCoordinator() {
        this(new QinDependencyService(),
                new QinSourceResolver(),
                new QinIrValidator(),
                new QinJsBackend(),
                new QinSlimeCfaCompiler(),
                new QinCompileSnapshotWriter());
    }

    public QinBuildCoordinator(
            QinDependencyService dependencyService,
            QinSourceResolver sourceResolver,
            QinIrValidator irValidator,
            QinJsBackend jsBackend,
            QinCfaPipeline cfaPipeline,
            QinCompileSnapshotWriter snapshotWriter) {
        this.dependencyService = dependencyService;
        this.sourceResolver = sourceResolver;
        this.irValidator = irValidator;
        this.jsBackend = jsBackend;
        this.cfaPipeline = cfaPipeline;
        this.snapshotWriter = snapshotWriter;
    }

    public QinBuildResult build(QinBuildRequest request) throws Exception {
        QinPhaseTimer profile = QinPhaseTimer.start("qin-build-coordinator");
        Path root = sourceResolver.resolveRoot(request.rootDir());
        QinResolvedDependencies deps = dependencyService.resolve(root);
        if (!deps.classpathEntries().isEmpty()) {
            System.out.println("Resolved dependencies: " + deps.classpathEntries().size());
        }
        QinRuntimeProjectLayout layout = QinRuntimeProjectLayout.discover(root);
        Path sourceFile = sourceResolver.resolveSourceFile(request, layout);
        profile.checkpoint("resolve inputs", "source=" + sourceFile.getFileName()
                + ", target=" + request.target());

        if (request.target() == QinBuildTarget.JVM && cfaPipeline instanceof QinSlimeCfaCompiler compiler) {
            QinBuildResult result = buildJvmModuleClasses(request, layout, root, sourceFile, compiler, profile);
            profile.done("module-class path");
            return result;
        }

        QinCfaCompileResult compileResult = cfaPipeline.compile(
                new QinCfaCompileRequest(
                        sourceFile,
                        root,
                        request.className(),
                        request.target().emitJvm()));
        profile.checkpoint("compile cfa pipeline");
        QinIrProgram program = compileResult.loweredProgram();
        QinFunctionModelArtifactRegistrar.register(program);
        irValidator.validate(program, request.target());
        profile.checkpoint("validate ir");

        Path classFile = null;
        Path jsFile = null;
        byte[] classBytes = compileResult.classBytes();

        if (request.target().emitJvm()) {
            if (classBytes == null || classBytes.length == 0) {
                throw new IllegalStateException("CFA compiler returned empty class bytes");
            }
            Map<String, byte[]> declarationClassBytes = program.classDeclarations().isEmpty()
                    ? Map.of()
                    : new QinJvmDeclarationClassEmitter().compileAllClasses(program);
            for (var entry : declarationClassBytes.entrySet()) {
                QinClassFileWriter.writeClassFile(request.classOutputDir(), entry.getKey(), entry.getValue());
            }
            classFile = QinClassFileWriter.writeClassFile(request.classOutputDir(), request.className(), classBytes);
            profile.checkpoint("emit jvm class");
        }

        if (request.target().emitJs()) {
            String jsCode = jsBackend.compileProgram(program);
            Path parent = request.jsOutputFile().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(request.jsOutputFile(), jsCode, StandardCharsets.UTF_8);
            jsFile = request.jsOutputFile();
            profile.checkpoint("emit js");
        }

        try {
            String originalSource = Files.readString(sourceFile, StandardCharsets.UTF_8);
            snapshotWriter.writeSnapshot(
                    sourceFile,
                    originalSource,
                    compileResult.linkedSource().source(),
                    compileResult.astText(),
                    compileResult.irBeforeLowering(),
                    compileResult.loweredProgram(),
                    compileResult.cfaProgram(),
                    request.className(),
                    classBytes);
        } catch (Exception snapshotError) {
            System.err.println("[WARN] failed to write compile snapshot: " + snapshotError.getMessage());
        }
        profile.done("single-module path");

        return new QinBuildResult(layout, sourceFile, program, classFile, jsFile);
    }

    private QinBuildResult buildJvmModuleClasses(
            QinBuildRequest request,
            QinRuntimeProjectLayout layout,
            Path root,
            Path sourceFile,
            QinSlimeCfaCompiler compiler,
            QinPhaseTimer profile) throws Exception {
        QinCfaModuleClassCompileResult compileResult = compiler.compileModuleClasses(
                QinCfaCompileRequest.forJvm(sourceFile, root, request.className()));
        profile.checkpoint("compile module classes", "modules=" + compileResult.moduleClasses().size());

        QinIrProgram program = entryProgram(compileResult);
        registerFunctionModelArtifacts(compileResult);
        irValidator.validate(program, request.target());
        profile.checkpoint("validate module ir");

        QinCfaModuleClassFile initializerClass = compileResult.initializerClass();
        if (initializerClass != null) {
            writeModuleClassFile(request.classOutputDir(), initializerClass);
        }
        for (QinCfaModuleClassFile moduleClass : compileResult.moduleClasses()) {
            writeModuleClassFile(request.classOutputDir(), moduleClass);
        }
        profile.checkpoint("write module classes", "modules=" + compileResult.moduleClasses().size());
        Path classFile = writeModuleLauncherClass(request.classOutputDir(), request.className(), compileResult);
        profile.checkpoint("write launcher class");
        return new QinBuildResult(layout, sourceFile, program, classFile, null);
    }

    private QinIrProgram entryProgram(QinCfaModuleClassCompileResult compileResult) {
        if (compileResult.moduleClasses().isEmpty()) {
            QinCfaModuleClassFile initializerClass = compileResult.initializerClass();
            if (initializerClass == null) {
                throw new IllegalStateException("Module-class compiler produced no classes");
            }
            return initializerClass.loweredProgram();
        }
        return compileResult.moduleClasses().get(compileResult.moduleClasses().size() - 1).loweredProgram();
    }

    private void registerFunctionModelArtifacts(QinCfaModuleClassCompileResult compileResult) {
        QinCfaModuleClassFile initializerClass = compileResult.initializerClass();
        if (initializerClass != null) {
            QinFunctionModelArtifactRegistrar.register(initializerClass.loweredProgram());
        }
        for (QinCfaModuleClassFile moduleClass : compileResult.moduleClasses()) {
            QinFunctionModelArtifactRegistrar.register(moduleClass.loweredProgram());
        }
    }

    private void writeModuleClassFile(Path classOutputDir, QinCfaModuleClassFile classFile) throws Exception {
        if (classFile.classBytes() == null || classFile.classBytes().length == 0) {
            throw new IllegalStateException("CFA module compiler returned empty class bytes: " + classFile.className());
        }
        Map<String, byte[]> declarationClassBytes = classFile.loweredProgram().classDeclarations().isEmpty()
                ? Map.of()
                : new QinJvmDeclarationClassEmitter().compileAllClasses(classFile.loweredProgram());
        for (var entry : declarationClassBytes.entrySet()) {
            QinClassFileWriter.writeClassFile(classOutputDir, entry.getKey(), entry.getValue());
        }
        QinClassFileWriter.writeClassFile(classOutputDir, classFile.className(), classFile.classBytes());
    }

    private Path writeModuleLauncherClass(
            Path classOutputDir,
            String launcherClassName,
            QinCfaModuleClassCompileResult compileResult) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("JDK compiler is required to compile Qin module launcher class.");
        }
        Path sourceFile = writeModuleLauncherSource(classOutputDir, launcherClassName, compileResult);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            Iterable<? extends JavaFileObject> units = fileManager.getJavaFileObjectsFromPaths(List.of(sourceFile));
            List<String> options = new ArrayList<>();
            options.add("-encoding");
            options.add("UTF-8");
            options.add("-d");
            options.add(classOutputDir.toString());
            String classpath = System.getProperty("java.class.path", "");
            if (classpath == null || classpath.isBlank()) {
                classpath = classOutputDir.toString();
            } else {
                classpath = classOutputDir + java.io.File.pathSeparator + classpath;
            }
            options.add("-classpath");
            options.add(classpath);
            Boolean ok = compiler.getTask(null, fileManager, diagnostics, options, null, units).call();
            if (!Boolean.TRUE.equals(ok)) {
                StringBuilder message = new StringBuilder("Failed to compile Qin module launcher: ")
                        .append(launcherClassName);
                diagnostics.getDiagnostics().forEach(diagnostic -> message
                        .append(System.lineSeparator())
                        .append(diagnostic.getKind())
                        .append(" line ")
                        .append(diagnostic.getLineNumber())
                        .append(": ")
                        .append(diagnostic.getMessage(null)));
                throw new IllegalStateException(message.toString());
            }
        }
        return classOutputDir.resolve(launcherClassName.replace('.', '/') + ".class").normalize();
    }

    private Path writeModuleLauncherSource(
            Path classOutputDir,
            String launcherClassName,
            QinCfaModuleClassCompileResult compileResult) throws Exception {
        int lastDot = launcherClassName.lastIndexOf('.');
        String packageName = lastDot < 0 ? "" : launcherClassName.substring(0, lastDot);
        String simpleName = lastDot < 0 ? launcherClassName : launcherClassName.substring(lastDot + 1);
        StringBuilder source = new StringBuilder();
        if (!packageName.isBlank()) {
            source.append("package ").append(packageName).append(";").append(System.lineSeparator()).append(System.lineSeparator());
        }
        source.append("public final class ").append(simpleName).append(" {").append(System.lineSeparator())
                .append("    private ").append(simpleName).append("() {}").append(System.lineSeparator())
                .append("    public static Object run() throws Exception {").append(System.lineSeparator())
                .append("        Object result = null;").append(System.lineSeparator());
        QinCfaModuleClassFile initializerClass = compileResult.initializerClass();
        if (initializerClass != null) {
            source.append("        result = ").append(initializerClass.className()).append(".run();")
                    .append(System.lineSeparator());
        }
        for (QinCfaModuleClassFile moduleClass : compileResult.moduleClasses()) {
            source.append("        result = ").append(moduleClass.className()).append(".run();")
                    .append(System.lineSeparator());
        }
        source.append("        return result;").append(System.lineSeparator())
                .append("    }").append(System.lineSeparator())
                .append("}").append(System.lineSeparator());

        Path sourceRoot = classOutputDir.resolve("__qin_launcher_sources").normalize();
        Path sourceFile = sourceRoot.resolve(launcherClassName.replace('.', '/') + ".java").normalize();
        Files.createDirectories(sourceFile.getParent());
        Files.writeString(sourceFile, source.toString(), StandardCharsets.UTF_8);
        return sourceFile;
    }
}
