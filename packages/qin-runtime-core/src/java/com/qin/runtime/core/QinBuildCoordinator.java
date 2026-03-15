package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.backend.jvm.QinClassFileWriter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinCfaCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaPipeline;
import com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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
        Path root = sourceResolver.resolveRoot(request.rootDir());
        QinResolvedDependencies deps = dependencyService.resolve(root);
        if (!deps.classpathEntries().isEmpty()) {
            System.out.println("Resolved dependencies: " + deps.classpathEntries().size());
        }
        QinRuntimeProjectLayout layout = QinRuntimeProjectLayout.discover(root);
        Path sourceFile = sourceResolver.resolveSourceFile(request, layout);

        QinCfaCompileResult compileResult = cfaPipeline.compile(
                new QinCfaCompileRequest(
                        sourceFile,
                        root,
                        request.className(),
                        request.target().emitJvm()));
        QinIrProgram program = compileResult.loweredProgram();
        irValidator.validate(program, request.target());

        Path classFile = null;
        Path jsFile = null;
        byte[] classBytes = compileResult.classBytes();

        if (request.target().emitJvm()) {
            if (classBytes == null || classBytes.length == 0) {
                throw new IllegalStateException("CFA compiler returned empty class bytes");
            }
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

        return new QinBuildResult(layout, sourceFile, program, classFile, jsFile);
    }
}
