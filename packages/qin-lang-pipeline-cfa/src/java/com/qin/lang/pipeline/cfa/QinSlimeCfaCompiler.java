package com.qin.lang.pipeline.cfa;

import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.module.resolver.QinLinkedModuleSection;
import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unified compiler facade:
 * Slime AST -> ESM sema -> JVM lowering -> Class-File API bytecode.
 */
public final class QinSlimeCfaCompiler implements QinCfaPipeline {
    private static final int LARGE_LINKED_CLASS_SOURCE_LIMIT = 2_000_000;
    private static final boolean ALLOW_LARGE_LINKED_CLASS =
            Boolean.getBoolean("qin.allowLargeLinkedClass");
    private static final Pattern CLASS_DECLARATION_PATTERN = Pattern.compile(
            "\\bclass\\s+([A-Za-z_$][\\w$]*)\\b");
    private static final Pattern EXPORT_INIT_PATTERN = Pattern.compile(
            "__qin_export_init__\\s*\\(\\s*([A-Za-z_$][\\w$]*)\\s*,\\s*([A-Za-z_$][\\w$]*)\\s*\\)");

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

        Map<String, String> declarationClassExportSlots = buildDeclarationClassExportSlots(linkedSource);
        List<QinCfaModuleClassFile> moduleClasses = compileModuleSections(
                request,
                semanticStageResult,
                linkedSource.moduleSections(),
                declarationClassExportSlots,
                startNanos);

        return new QinCfaModuleClassCompileResult(
                projectRoot,
                sourceFile,
                linkedSource,
                semanticStageResult.semanticModel(),
                initializerClass,
                moduleClasses);
    }

    private List<QinCfaModuleClassFile> compileModuleSections(
            QinCfaCompileRequest request,
            QinCfaSemanticStageResult semanticStageResult,
            List<QinLinkedModuleSection> sections,
            Map<String, String> declarationClassExportSlots,
            long startNanos) throws Exception {
        if (sections == null || sections.isEmpty()) {
            return List.of();
        }
        int parallelism = moduleClassParallelism(sections.size());
        if (parallelism <= 1) {
            List<QinCfaModuleClassFile> moduleClasses = new ArrayList<>();
            for (QinLinkedModuleSection section : sections) {
                moduleClasses.add(compileOneModuleSection(
                        request,
                        semanticStageResult,
                        section,
                        declarationClassExportSlots,
                        startNanos));
            }
            return moduleClasses;
        }

        logPhase("module-class parallel emit start", startNanos,
                "modules=" + sections.size() + ", parallelism=" + parallelism);
        ExecutorService executor = Executors.newFixedThreadPool(
                parallelism,
                new ModuleClassThreadFactory());
        try {
            List<Future<QinCfaModuleClassFile>> futures = new ArrayList<>();
            for (QinLinkedModuleSection section : sections) {
                futures.add(executor.submit(() -> compileOneModuleSection(
                        request,
                        semanticStageResult,
                        section,
                        declarationClassExportSlots,
                        startNanos)));
            }
            List<QinCfaModuleClassFile> moduleClasses = new ArrayList<>(futures.size());
            for (Future<QinCfaModuleClassFile> future : futures) {
                moduleClasses.add(awaitModuleClass(future));
            }
            logPhase("module-class parallel emit done", startNanos,
                    "modules=" + moduleClasses.size());
            return moduleClasses;
        } finally {
            executor.shutdownNow();
        }
    }

    private QinCfaModuleClassFile compileOneModuleSection(
            QinCfaCompileRequest request,
            QinCfaSemanticStageResult semanticStageResult,
            QinLinkedModuleSection section,
            Map<String, String> declarationClassExportSlots,
            long startNanos) {
        String className = request.className() + "$QinModule" + section.index();
        logPhase("module-class emit start", startNanos, className + " :: " + section.file());
        try {
            QinCfaModuleClassFile moduleClass = compileModuleClassSource(
                    semanticStageResult,
                    section.file(),
                    section.index(),
                    className,
                    section.classSource(),
                    declarationClassExportSlots);
            logPhase("module-class emit done", startNanos, className);
            return moduleClass;
        } catch (RuntimeException error) {
            throw new IllegalArgumentException(
                    "Failed to compile module class " + className
                            + " for " + section.file()
                            + " (moduleIndex=" + section.index() + ")",
                    error);
        }
    }

    private QinCfaModuleClassFile awaitModuleClass(Future<QinCfaModuleClassFile> future) throws Exception {
        try {
            return future.get();
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while compiling Qin module classes", error);
        } catch (ExecutionException error) {
            Throwable cause = error.getCause();
            if (cause instanceof Exception exception) {
                throw exception;
            }
            if (cause instanceof Error fatal) {
                throw fatal;
            }
            throw new IllegalStateException("Failed to compile Qin module class", cause);
        }
    }

    private int moduleClassParallelism(int moduleCount) {
        if (moduleCount <= 1 || Boolean.getBoolean("qin.moduleClass.sequential")) {
            return 1;
        }
        int configured = Integer.getInteger("qin.moduleClass.parallelism", 0);
        if (configured > 0) {
            return Math.max(1, Math.min(moduleCount, configured));
        }
        int processors = Runtime.getRuntime().availableProcessors();
        return Math.max(1, Math.min(moduleCount, Math.min(processors, 8)));
    }

    private QinCfaModuleClassFile compileModuleClassSource(
            QinCfaSemanticStageResult originalResult,
            Path sourceFile,
            int moduleIndex,
            String className,
            String source) {
        return compileModuleClassSource(
                originalResult,
                sourceFile,
                moduleIndex,
                className,
                source,
                Map.of());
    }

    private QinCfaModuleClassFile compileModuleClassSource(
            QinCfaSemanticStageResult originalResult,
            Path sourceFile,
            int moduleIndex,
            String className,
            String source,
            Map<String, String> declarationClassExportSlots) {
        QinLinkedModuleSource originalLinkedSource = originalResult.linkedSource();
        QinLinkedModuleSource classLinkedSource = new QinLinkedModuleSource(
                originalLinkedSource.entryFile(),
                source == null ? "" : source,
                "",
                originalLinkedSource.modules(),
                List.of(),
                originalLinkedSource.imports(),
                originalLinkedSource.moduleGraph());
        QinCfaIrStageResult irStageResult = new QinCfaIrStage().execute(
                new QinCfaSemanticStageResult(classLinkedSource, originalResult.semanticModel()),
                declarationClassExportSlots);
        byte[] classBytes = new QinCfaEmitStage().emit(irStageResult, className);
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

    private Map<String, String> buildDeclarationClassExportSlots(QinLinkedModuleSource linkedSource) {
        Map<String, String> exportSlots = new LinkedHashMap<>();
        if (linkedSource == null || linkedSource.moduleSections().isEmpty()) {
            return Map.of();
        }
        for (QinLinkedModuleSection section : linkedSource.moduleSections()) {
            String source = section.classSource();
            if (source == null || source.isBlank()) {
                continue;
            }
            Set<String> classNames = collectClassDeclarationNames(source);
            Matcher matcher = EXPORT_INIT_PATTERN.matcher(source);
            while (matcher.find()) {
                String slotName = matcher.group(1);
                String localName = matcher.group(2);
                if (!classNames.contains(localName)) {
                    continue;
                }
                exportSlots.put(slotName, localName);
            }
        }
        return exportSlots.isEmpty() ? Map.of() : Map.copyOf(exportSlots);
    }

    private Set<String> collectClassDeclarationNames(String source) {
        Set<String> names = new java.util.LinkedHashSet<>();
        Matcher matcher = CLASS_DECLARATION_PATTERN.matcher(source);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
        return Set.copyOf(names);
    }

    private void logPhase(String phase, long startNanos, String detail) {
        long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000L;
        System.out.println("[QinSlimeCfaCompiler] " + phase + " +" + elapsedMs + "ms :: " + detail);
    }

    private static final class ModuleClassThreadFactory implements ThreadFactory {
        private int nextIndex = 1;

        @Override
        public synchronized Thread newThread(Runnable task) {
            Thread thread = new Thread(task, "qin-module-class-emit-" + nextIndex++);
            thread.setDaemon(true);
            return thread;
        }
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
