package com.qin.lang.pipeline.cfa;

import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.module.resolver.QinLinkedModuleSection;
import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Unified compiler facade:
 * Slime AST -> ESM sema -> JVM lowering -> Class-File API bytecode.
 */
public final class QinSlimeCfaCompiler implements QinCfaPipeline {
    private static final int LARGE_LINKED_CLASS_SOURCE_LIMIT = 2_000_000;
    private static final boolean ALLOW_LARGE_LINKED_CLASS =
            Boolean.getBoolean("qin.allowLargeLinkedClass");

    private final QinCfaSemanticStage semanticStage;
    private final QinCfaIrStage irStage;
    private final QinCfaEmitStage emitStage;

    public QinSlimeCfaCompiler() {
        this(new QinCfaSemanticStage(), new QinCfaIrStage(), new QinCfaEmitStage());
    }

    public QinSlimeCfaCompiler(
            QinCfaSemanticStage semanticStage,
            QinCfaIrStage irStage,
            QinCfaEmitStage emitStage) {
        this.semanticStage = Objects.requireNonNull(semanticStage, "semanticStage cannot be null");
        this.irStage = Objects.requireNonNull(irStage, "irStage cannot be null");
        this.emitStage = Objects.requireNonNull(emitStage, "emitStage cannot be null");
    }

    public QinCfaCompileResult compile(QinCfaCompileRequest request) throws Exception {
        Objects.requireNonNull(request, "request cannot be null");
        long startNanos = System.nanoTime();

        Path sourceFile = requireFile(request.sourceFile());
        Path projectRoot = request.projectRoot().toAbsolutePath().normalize();

        logPhase("semantic start", startNanos, sourceFile.toString());
        QinCfaSemanticStageResult semanticStageResult = semanticStage.execute(sourceFile, projectRoot);
        logPhase("semantic done", startNanos, sourceFile.toString());
        validateLinkedClassBoundary(request, semanticStageResult);
        logPhase("ir start", startNanos, sourceFile.toString());
        QinCfaIrStageResult irStageResult = irStage.execute(semanticStageResult);
        logPhase("ir done", startNanos, sourceFile.toString());

        QinIrProgram irBeforeLowering = irStageResult.irBeforeLowering();
        QinIrProgram loweredProgram = irStageResult.loweredProgram();
        QinCfaProgram cfaProgram = irStageResult.cfaProgram();
        String astText = irStageResult.astText();

        byte[] classBytes = null;
        if (request.emitClassBytes()) {
            logPhase("emit start", startNanos, request.className());
            classBytes = emitStage.emit(irStageResult, request.className());
            logPhase("emit done", startNanos, request.className());
        }

        return new QinCfaCompileResult(
                projectRoot,
                sourceFile,
                semanticStageResult.linkedSource(),
                semanticStageResult.semanticModel(),
                irBeforeLowering,
                loweredProgram,
                cfaProgram,
                astText,
                classBytes);
    }

    public QinCfaModuleClassCompileResult compileModuleClasses(QinCfaCompileRequest request) throws Exception {
        Objects.requireNonNull(request, "request cannot be null");
        if (!request.emitClassBytes()) {
            throw new IllegalArgumentException("compileModuleClasses requires emitClassBytes=true");
        }
        long startNanos = System.nanoTime();

        Path sourceFile = requireFile(request.sourceFile());
        Path projectRoot = request.projectRoot().toAbsolutePath().normalize();

        logPhase("module-class semantic start", startNanos, sourceFile.toString());
        QinCfaSemanticStageResult semanticStageResult = semanticStage.executeForModuleClasses(sourceFile, projectRoot);
        QinLinkedModuleSource linkedSource = semanticStageResult.linkedSource();
        logPhase("module-class semantic done", startNanos, "modules=" + linkedSource.moduleSections().size());

        QinCfaModuleClassFile initializerClass = null;
        if (!linkedSource.moduleInitializerSource().isBlank()) {
            initializerClass = compileModuleClassSource(
                    semanticStageResult,
                    sourceFile,
                    -1,
                    request.className() + "$QinModuleInitializer",
                    linkedSource.moduleInitializerSource());
            logPhase("module-class initializer done", startNanos, initializerClass.className());
        }

        List<QinCfaModuleClassFile> moduleClasses = new ArrayList<>();
        for (QinLinkedModuleSection section : linkedSource.moduleSections()) {
            String className = request.className() + "$QinModule" + section.index();
            logPhase("module-class emit start", startNanos, className + " :: " + section.file());
            QinCfaModuleClassFile moduleClass = compileModuleClassSource(
                    semanticStageResult,
                    section.file(),
                    section.index(),
                    className,
                    section.classSource());
            moduleClasses.add(moduleClass);
            logPhase("module-class emit done", startNanos, className);
        }

        return new QinCfaModuleClassCompileResult(
                projectRoot,
                sourceFile,
                linkedSource,
                semanticStageResult.semanticModel(),
                initializerClass,
                moduleClasses);
    }

    private QinCfaModuleClassFile compileModuleClassSource(
            QinCfaSemanticStageResult originalResult,
            Path sourceFile,
            int moduleIndex,
            String className,
            String source) {
        QinLinkedModuleSource originalLinkedSource = originalResult.linkedSource();
        QinLinkedModuleSource classLinkedSource = new QinLinkedModuleSource(
                originalLinkedSource.entryFile(),
                source == null ? "" : source,
                "",
                originalLinkedSource.modules(),
                List.of(),
                originalLinkedSource.imports(),
                originalLinkedSource.moduleGraph());
        QinCfaIrStageResult irStageResult = irStage.execute(
                new QinCfaSemanticStageResult(classLinkedSource, originalResult.semanticModel()));
        byte[] classBytes = emitStage.emit(irStageResult, className);
        return new QinCfaModuleClassFile(
                sourceFile,
                moduleIndex,
                className,
                irStageResult.irBeforeLowering(),
                irStageResult.loweredProgram(),
                irStageResult.cfaProgram(),
                irStageResult.astText(),
                classBytes);
    }

    private void logPhase(String phase, long startNanos, String detail) {
        long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000L;
        System.out.println("[QinSlimeCfaCompiler] " + phase + " +" + elapsedMs + "ms :: " + detail);
    }

    private void validateLinkedClassBoundary(
            QinCfaCompileRequest request,
            QinCfaSemanticStageResult semanticStageResult) {
        if (!request.emitClassBytes() || ALLOW_LARGE_LINKED_CLASS) {
            return;
        }
        int sourceLength = semanticStageResult.linkedSource().source().length();
        if (sourceLength <= LARGE_LINKED_CLASS_SOURCE_LIMIT) {
            return;
        }
        int moduleCount = semanticStageResult.linkedSource().modules().size();
        throw new IllegalStateException(
                "QJS9001 linked source is too large for the current single-class JVM backend: "
                        + sourceLength + " chars across " + moduleCount + " modules. "
                        + "This is a compiler architecture boundary, not a JS syntax fallback. "
                        + "Move this package path to module-level/class-cache compilation before treating it as supported. "
                        + "For compiler diagnostics only, rerun with -Dqin.allowLargeLinkedClass=true.");
    }

    private Path requireFile(Path file) {
        if (file == null) {
            throw new IllegalArgumentException("source file cannot be null");
        }
        Path normalized = file.toAbsolutePath().normalize();
        if (!Files.exists(normalized) || !Files.isRegularFile(normalized)) {
            throw new IllegalArgumentException("Missing file: " + normalized);
        }
        return normalized;
    }
}
