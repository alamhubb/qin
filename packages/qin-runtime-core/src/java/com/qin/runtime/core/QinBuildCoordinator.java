package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.backend.jvm.QinClassFileWriter;
import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.ir.QinIrProgram;

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

    public QinBuildCoordinator() {
        this(new QinDependencyService(),
                new QinSourceResolver(),
                new QinFrontendCompiler(),
                new QinIrValidator(),
                new QinJvmClassFileBackend(),
                new QinJsBackend());
    }

    public QinBuildCoordinator(
            QinDependencyService dependencyService,
            QinSourceResolver sourceResolver,
            QinFrontendCompiler frontendCompiler,
            QinIrValidator irValidator,
            QinJvmClassFileBackend jvmBackend,
            QinJsBackend jsBackend) {
        this.dependencyService = dependencyService;
        this.sourceResolver = sourceResolver;
        this.frontendCompiler = frontendCompiler;
        this.irValidator = irValidator;
        this.jvmBackend = jvmBackend;
        this.jsBackend = jsBackend;
    }

    public QinBuildResult build(QinBuildRequest request) throws Exception {
        Path root = sourceResolver.resolveRoot(request.rootDir());
        QinResolvedDependencies deps = dependencyService.resolve(root);
        if (!deps.classpathEntries().isEmpty()) {
            System.out.println("Resolved dependencies: " + deps.classpathEntries().size());
        }
        QinRuntimeProjectLayout layout = QinRuntimeProjectLayout.discover(root);
        Path sourceFile = sourceResolver.resolveSourceFile(request, layout);

        QinIrProgram program = frontendCompiler.compile(sourceFile);
        irValidator.validate(program);

        Path classFile = null;
        Path jsFile = null;

        if (request.target().emitJvm()) {
            byte[] classBytes = jvmBackend.compileProgram(program, request.className());
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

        return new QinBuildResult(layout, sourceFile, program, classFile, jsFile);
    }
}
