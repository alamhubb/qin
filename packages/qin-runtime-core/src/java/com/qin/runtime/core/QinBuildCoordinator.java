package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.backend.jvm.QinClassFileWriter;
import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.lowering.jvm.QinEsmJvmLowerer;
import com.qin.lang.lowering.jvm.QinEsmJvmLoweringContext;
import com.qin.lang.lowering.jvm.QinStrictEsmJvmLowerer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Coordinates source resolution, frontend compile, IR validation and backend emissions.
 */
public final class QinBuildCoordinator {
    private final QinDependencyService dependencyService;
    private final QinSourceResolver sourceResolver;
    private final QinFrontendCompiler frontendCompiler;
    private final QinIrValidator irValidator;
    private final QinJvmClassFileBackend jvmBackend;
    private final QinJsBackend jsBackend;
    private final QinEsmJvmLowerer esmJvmLowerer;
    private final QinCompileSnapshotWriter snapshotWriter;

    public QinBuildCoordinator() {
        this(new QinDependencyService(),
                new QinSourceResolver(),
                new QinFrontendCompiler(),
                new QinIrValidator(),
                new QinJvmClassFileBackend(),
                new QinJsBackend(),
                new QinStrictEsmJvmLowerer(),
                new QinCompileSnapshotWriter());
    }

    public QinBuildCoordinator(
            QinDependencyService dependencyService,
            QinSourceResolver sourceResolver,
            QinFrontendCompiler frontendCompiler,
            QinIrValidator irValidator,
            QinJvmClassFileBackend jvmBackend,
            QinJsBackend jsBackend,
            QinEsmJvmLowerer esmJvmLowerer,
            QinCompileSnapshotWriter snapshotWriter) {
        this.dependencyService = dependencyService;
        this.sourceResolver = sourceResolver;
        this.frontendCompiler = frontendCompiler;
        this.irValidator = irValidator;
        this.jvmBackend = jvmBackend;
        this.jsBackend = jsBackend;
        this.esmJvmLowerer = esmJvmLowerer;
        this.snapshotWriter = snapshotWriter;
    }

    public QinBuildResult build(QinBuildRequest request) throws Exception {
        Path root = sourceResolver.resolveRoot(request.rootDir());
        QinResolvedDependencies deps = dependencyService.resolve(root);
        if (!deps.classpathEntries().isEmpty()) {
            System.out.println("Resolved dependencies: " + deps.classpathEntries().size());
        }
        QinRuntimeProjectLayout layout = QinRuntimeProjectLayout.discover(root);
        Path sourceFile = sourceResolver.resolveSourceFile(request, layout);

        QinFrontendCompileResult frontendResult = frontendCompiler.compile(sourceFile, root);
        QinIrProgram program = frontendResult.program();
        QinIrProgram irBeforeLowering = program;
        QinEsmJvmLoweringContext loweringContext = new QinEsmJvmLoweringContext(
                frontendResult.linkedSource().entryFile(),
                frontendResult.linkedSource().modules());
        program = esmJvmLowerer.lower(program, frontendResult.semanticModel(), loweringContext);
        QinIrProgram loweredProgram = program;
        irValidator.validate(program, request.target());

        Path classFile = null;
        Path jsFile = null;
        byte[] classBytes = null;

        if (request.target().emitJvm()) {
            classBytes = jvmBackend.compileProgram(program, request.className());
            classFile = QinClassFileWriter.writeClassFile(request.classOutputDir(), request.className(), classBytes);
        }

        if (request.target().emitJs()) {
            String jsCode = jsBackend.compileProgram(program);
            Path parent = request.jsOutputFile().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(request.jsOutputFile(), jsCode, StandardCharsets.UTF_8);
            jsFile = request.jsOutputFile();
        }

        try {
            String originalSource = Files.readString(sourceFile, StandardCharsets.UTF_8);
            snapshotWriter.writeSnapshot(
                    sourceFile,
                    originalSource,
                    frontendResult.linkedSource().source(),
                    frontendResult.astText(),
                    irBeforeLowering,
                    loweredProgram,
                    request.className(),
                    classBytes);
        } catch (Exception snapshotError) {
            System.err.println("[WARN] failed to write compile snapshot: " + snapshotError.getMessage());
        }

        return new QinBuildResult(layout, sourceFile, program, classFile, jsFile);
    }
}
