package com.qin.lang.pipeline.cfa;

import com.qin.lang.ir.QinJavaSdkAliasSupport;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.module.resolver.QinLinkedModuleSection;
import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
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
    private static final Pattern LOCAL_ALIAS_PATTERN = Pattern.compile(
            "\\bconst\\s+([A-Za-z_$][\\w$]*)\\s*=\\s*([A-Za-z_$][\\w$]*)\\b");
    private static final Pattern EXPORT_INIT_PATTERN = Pattern.compile(
            "__qin_export_init__\\s*\\(\\s*([A-Za-z_$][\\w$]*)\\s*,\\s*([A-Za-z_$][\\w$]*)\\s*\\)");
    private static final Pattern EXPORT_INIT_GET_PATTERN = Pattern.compile(
            "__qin_export_init__\\s*\\(\\s*([A-Za-z_$][\\w$]*)\\s*,\\s*__qin_export_get__\\s*\\(\\s*([A-Za-z_$][\\w$]*)\\s*\\)\\s*\\)");
    private static final Pattern EXPORT_GET_ALIAS_PATTERN = Pattern.compile(
            "\\bconst\\s+([A-Za-z_$][\\w$]*)\\s*=\\s*__qin_export_get__\\s*\\(\\s*([A-Za-z_$][\\w$]*)\\s*\\)");

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
        Map<String, String> declarationClassExportSlots =
                buildDeclarationClassExportSlots(semanticStageResult.linkedSource());
        logPhase("ir start", startNanos, sourceFile.toString());
        QinCfaIrStageResult irStageResult = irStage.execute(
                semanticStageResult,
                declarationClassExportSlots);
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

        LoweredModuleClassSource initializerLowered = null;
        if (!linkedSource.moduleInitializerSource().isBlank()) {
            initializerLowered = lowerModuleClassSource(
                    semanticStageResult,
                    sourceFile,
                    -1,
                    request.className() + "$QinModuleInitializer",
                    linkedSource.moduleInitializerSource());
            logPhase("module-class initializer lower done", startNanos, initializerLowered.className());
        }

        Map<String, String> declarationClassExportSlots = buildDeclarationClassExportSlots(linkedSource);
        List<LoweredModuleClassSource> loweredModuleSections = lowerModuleSections(
                request,
                semanticStageResult,
                linkedSource.moduleSections(),
                declarationClassExportSlots,
                Map.of(),
                startNanos);
        Map<String, QinIrExpression> staticExportSlotValues =
                buildStaticExportSlotValues(loweredModuleSections);
        if (!staticExportSlotValues.isEmpty()) {
            logPhase("module-class static export slots", startNanos,
                    "slots=" + staticExportSlotValues.size());
            loweredModuleSections = lowerModuleSections(
                    request,
                    semanticStageResult,
                    linkedSource.moduleSections(),
                    declarationClassExportSlots,
                    staticExportSlotValues,
                    startNanos);
        }
        Map<String, QinIrClassDeclaration> declarationIndex =
                buildModuleDeclarationIndex(initializerLowered, loweredModuleSections);
        QinCfaModuleClassFile initializerClass = initializerLowered == null
                ? null
                : emitLoweredModuleClass(initializerLowered, declarationIndex);
        if (initializerClass != null) {
            logPhase("module-class initializer done", startNanos, initializerClass.className());
        }
        List<QinCfaModuleClassFile> moduleClasses = emitModuleSections(
                loweredModuleSections,
                declarationIndex,
                startNanos);

        return new QinCfaModuleClassCompileResult(
                projectRoot,
                sourceFile,
                linkedSource,
                semanticStageResult.semanticModel(),
                initializerClass,
                moduleClasses);
    }

    private List<LoweredModuleClassSource> lowerModuleSections(
            QinCfaCompileRequest request,
            QinCfaSemanticStageResult semanticStageResult,
            List<QinLinkedModuleSection> sections,
            Map<String, String> declarationClassExportSlots,
            Map<String, QinIrExpression> staticExportSlotValues,
            long startNanos) throws Exception {
        if (sections == null || sections.isEmpty()) {
            return List.of();
        }
        int parallelism = moduleClassParallelism(sections.size());
        if (parallelism <= 1) {
            List<LoweredModuleClassSource> moduleClasses = new ArrayList<>();
            for (QinLinkedModuleSection section : sections) {
                moduleClasses.add(lowerOneModuleSection(
                        request,
                        semanticStageResult,
                        section,
                        declarationClassExportSlots,
                        staticExportSlotValues,
                        startNanos));
            }
            return moduleClasses;
        }

        logPhase("module-class parallel lower start", startNanos,
                "modules=" + sections.size() + ", parallelism=" + parallelism);
        ExecutorService executor = Executors.newFixedThreadPool(
                parallelism,
                new ModuleClassThreadFactory());
        try {
            List<Future<LoweredModuleClassSource>> futures = new ArrayList<>();
            for (QinLinkedModuleSection section : sections) {
                futures.add(executor.submit(() -> lowerOneModuleSection(
                        request,
                        semanticStageResult,
                        section,
                        declarationClassExportSlots,
                        staticExportSlotValues,
                        startNanos)));
            }
            List<LoweredModuleClassSource> moduleClasses = new ArrayList<>(futures.size());
            for (Future<LoweredModuleClassSource> future : futures) {
                moduleClasses.add(awaitLoweredModuleClass(future));
            }
            logPhase("module-class parallel lower done", startNanos,
                    "modules=" + moduleClasses.size());
            return moduleClasses;
        } finally {
            executor.shutdownNow();
        }
    }

    private LoweredModuleClassSource lowerOneModuleSection(
            QinCfaCompileRequest request,
            QinCfaSemanticStageResult semanticStageResult,
            QinLinkedModuleSection section,
            Map<String, String> declarationClassExportSlots,
            Map<String, QinIrExpression> staticExportSlotValues,
            long startNanos) {
        String className = request.className() + "$QinModule" + section.index();
        logPhase("module-class lower start", startNanos, className + " :: " + section.file());
        try {
            LoweredModuleClassSource moduleClass = lowerModuleClassSource(
                    semanticStageResult,
                    section.file(),
                    section.index(),
                    className,
                    section.classSource(),
                    declarationClassExportSlots,
                    staticExportSlotValues);
            logPhase("module-class lower done", startNanos, className);
            return moduleClass;
        } catch (RuntimeException error) {
            throw new IllegalArgumentException(
                    "Failed to lower module class " + className
                            + " for " + section.file()
                            + " (moduleIndex=" + section.index() + ")",
                    error);
        }
    }

    private LoweredModuleClassSource awaitLoweredModuleClass(Future<LoweredModuleClassSource> future) throws Exception {
        try {
            return future.get();
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while lowering Qin module classes", error);
        } catch (ExecutionException error) {
            Throwable cause = error.getCause();
            if (cause instanceof Exception exception) {
                throw exception;
            }
            if (cause instanceof Error fatal) {
                throw fatal;
            }
            throw new IllegalStateException("Failed to lower Qin module class", cause);
        }
    }

    private List<QinCfaModuleClassFile> emitModuleSections(
            List<LoweredModuleClassSource> loweredSections,
            Map<String, QinIrClassDeclaration> declarationIndex,
            long startNanos) throws Exception {
        if (loweredSections == null || loweredSections.isEmpty()) {
            return List.of();
        }
        int parallelism = moduleClassParallelism(loweredSections.size());
        if (parallelism <= 1) {
            List<QinCfaModuleClassFile> moduleClasses = new ArrayList<>();
            for (LoweredModuleClassSource loweredSection : loweredSections) {
                moduleClasses.add(emitOneLoweredModuleSection(loweredSection, declarationIndex, startNanos));
            }
            return moduleClasses;
        }
        logPhase("module-class parallel emit start", startNanos,
                "modules=" + loweredSections.size() + ", parallelism=" + parallelism);
        ExecutorService executor = Executors.newFixedThreadPool(
                parallelism,
                new ModuleClassThreadFactory());
        try {
            List<Future<QinCfaModuleClassFile>> futures = new ArrayList<>();
            for (LoweredModuleClassSource loweredSection : loweredSections) {
                futures.add(executor.submit(() ->
                        emitOneLoweredModuleSection(loweredSection, declarationIndex, startNanos)));
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

    private QinCfaModuleClassFile emitOneLoweredModuleSection(
            LoweredModuleClassSource loweredSection,
            Map<String, QinIrClassDeclaration> declarationIndex,
            long startNanos) {
        logPhase("module-class emit start", startNanos,
                loweredSection.className() + " :: " + loweredSection.sourceFile());
        try {
            QinCfaModuleClassFile moduleClass = emitLoweredModuleClass(loweredSection, declarationIndex);
            logPhase("module-class emit done", startNanos, loweredSection.className());
            return moduleClass;
        } catch (RuntimeException error) {
            throw new IllegalArgumentException(
                    "Failed to compile module class " + loweredSection.className()
                            + " for " + loweredSection.sourceFile()
                            + " (moduleIndex=" + loweredSection.moduleIndex() + ")",
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
        LoweredModuleClassSource lowered = lowerModuleClassSource(
                originalResult,
                sourceFile,
                moduleIndex,
                className,
                source,
                declarationClassExportSlots);
        return emitLoweredModuleClass(lowered, buildModuleDeclarationIndex(lowered, List.of()));
    }

    private LoweredModuleClassSource lowerModuleClassSource(
            QinCfaSemanticStageResult originalResult,
            Path sourceFile,
            int moduleIndex,
            String className,
            String source) {
        return lowerModuleClassSource(
                originalResult,
                sourceFile,
                moduleIndex,
                className,
                source,
                Map.of());
    }

    private LoweredModuleClassSource lowerModuleClassSource(
            QinCfaSemanticStageResult originalResult,
            Path sourceFile,
            int moduleIndex,
            String className,
            String source,
            Map<String, String> declarationClassExportSlots) {
        return lowerModuleClassSource(
                originalResult,
                sourceFile,
                moduleIndex,
                className,
                source,
                declarationClassExportSlots,
                Map.of());
    }

    private LoweredModuleClassSource lowerModuleClassSource(
            QinCfaSemanticStageResult originalResult,
            Path sourceFile,
            int moduleIndex,
            String className,
            String source,
            Map<String, String> declarationClassExportSlots,
            Map<String, QinIrExpression> staticExportSlotValues) {
        dumpModuleClassSourceIfRequested(sourceFile, moduleIndex, className, source);
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
                declarationClassExportSlots,
                staticExportSlotValues);
        return new LoweredModuleClassSource(sourceFile, moduleIndex, className, irStageResult);
    }

    private QinCfaModuleClassFile emitLoweredModuleClass(
            LoweredModuleClassSource lowered,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        QinCfaIrStageResult irStageResult = lowered.irStageResult();
        byte[] classBytes = new QinCfaEmitStage().emit(irStageResult, lowered.className(), declarationIndex);
        return new QinCfaModuleClassFile(
                lowered.sourceFile(),
                lowered.moduleIndex(),
                lowered.className(),
                irStageResult.irBeforeLowering(),
                irStageResult.loweredProgram(),
                irStageResult.cfaProgram(),
                irStageResult.astText(),
                classBytes);
    }

    private Map<String, QinIrExpression> buildStaticExportSlotValues(
            List<LoweredModuleClassSource> loweredSections) {
        if (loweredSections == null || loweredSections.isEmpty()) {
            return Map.of();
        }
        Map<String, QinIrExpression> values = new LinkedHashMap<>();
        for (LoweredModuleClassSource loweredSection : loweredSections) {
            if (loweredSection == null
                    || loweredSection.irStageResult() == null
                    || loweredSection.irStageResult().irBeforeLowering() == null) {
                continue;
            }
            addStaticExportSlotValues(values, loweredSection.irStageResult().irBeforeLowering());
        }
        return values.isEmpty() ? Map.of() : Map.copyOf(values);
    }

    private void addStaticExportSlotValues(
            Map<String, QinIrExpression> values,
            QinIrProgram program) {
        Map<String, QinIrExpression> declarations = new LinkedHashMap<>();
        for (QinIrConstDeclaration declaration : program.declarations()) {
            declarations.put(declaration.name(), declaration.initializer());
            if (Boolean.getBoolean("qin.staticExportSlot.trace")) {
                System.err.println("[QinSlimeCfaCompiler] static export declaration "
                        + declaration.name() + " -> " + declaration.initializer());
            }
        }
        for (QinIrConstDeclaration declaration : program.declarations()) {
            addStaticExportSlotValue(values, declarations, declaration.initializer());
        }
        for (QinIrExpressionStatement statement : program.expressionStatements()) {
            addStaticExportSlotValue(values, declarations, statement.expression());
        }
    }

    private void addStaticExportSlotValue(
            Map<String, QinIrExpression> values,
            Map<String, QinIrExpression> declarations,
            QinIrExpression expression) {
        if (!(expression instanceof QinIrBuiltinCallExpression call)
                || !"Global".equals(call.receiverName())
                || !"__qin_export_init__".equals(call.methodName())
                || call.arguments().size() != 2) {
            return;
        }
        String slotName = exportSlotName(call.arguments().get(0), declarations);
        if (slotName == null) {
            return;
        }
        QinIrExpression value = resolveDeclarationValue(call.arguments().get(1), declarations);
        if (value instanceof QinIrObjectLiteral) {
            values.putIfAbsent(slotName, value);
        }
    }

    private QinIrExpression resolveDeclarationValue(
            QinIrExpression expression,
            Map<String, QinIrExpression> declarations) {
        if (expression instanceof QinIrIdentifierReference identifierReference) {
            QinIrExpression resolved = declarations.get(identifierReference.name());
            return resolved == null ? expression : resolved;
        }
        if (expression instanceof QinIrBuiltinCallExpression call
                && "Global".equals(call.receiverName())
                && "__qin_global__".equals(call.methodName())
                && call.arguments().size() == 1
                && call.arguments().get(0) instanceof QinIrStringLiteral stringLiteral) {
            QinIrExpression resolved = declarations.get(stringLiteral.value());
            return resolved == null ? expression : resolved;
        }
        return expression;
    }

    private String exportSlotName(
            QinIrExpression expression,
            Map<String, QinIrExpression> declarations) {
        if (expression instanceof QinIrStringLiteral stringLiteral
                && isModuleExportSlotName(stringLiteral.value())) {
            return stringLiteral.value();
        }
        if (expression instanceof QinIrIdentifierReference identifierReference) {
            return exportSlotName(declarations.get(identifierReference.name()), declarations);
        }
        if (expression instanceof QinIrBuiltinCallExpression call
                && "Global".equals(call.receiverName())
                && "__qin_global__".equals(call.methodName())
                && call.arguments().size() == 1
                && call.arguments().get(0) instanceof QinIrStringLiteral stringLiteral
                && isModuleExportSlotName(stringLiteral.value())) {
            return stringLiteral.value();
        }
        return null;
    }

    private boolean isModuleExportSlotName(String value) {
        return value != null && value.startsWith("__qesm_m") && value.contains("_e_");
    }

    private Map<String, QinIrClassDeclaration> buildModuleDeclarationIndex(
            LoweredModuleClassSource initializerLowered,
            List<LoweredModuleClassSource> loweredSections) {
        Map<String, QinIrClassDeclaration> index = new LinkedHashMap<>();
        addModuleDeclarations(index, initializerLowered);
        if (loweredSections != null) {
            for (LoweredModuleClassSource loweredSection : loweredSections) {
                addModuleDeclarations(index, loweredSection);
            }
        }
        return index.isEmpty() ? Map.of() : Map.copyOf(index);
    }

    private void addModuleDeclarations(
            Map<String, QinIrClassDeclaration> index,
            LoweredModuleClassSource loweredSection) {
        if (index == null || loweredSection == null) {
            return;
        }
        for (QinIrClassDeclaration declaration : loweredSection.irStageResult().loweredProgram().classDeclarations()) {
            if (declaration == null) {
                continue;
            }
            putModuleDeclaration(index, declaration.binaryName(), declaration);
            putModuleDeclaration(index, declaration.simpleName(), declaration);
        }
    }

    private void putModuleDeclaration(
            Map<String, QinIrClassDeclaration> index,
            String key,
            QinIrClassDeclaration declaration) {
        if (index == null || key == null || key.isBlank() || declaration == null) {
            return;
        }
        index.putIfAbsent(key, declaration);
        if (key.startsWith("__Qin") || declaration.simpleName().startsWith("__Qin")) {
            return;
        }
        String canonical = canonicalDeclarationClassBinaryName(key);
        if (canonical != null && !canonical.isBlank()) {
            index.putIfAbsent(canonical, declaration);
        }
    }

    private void dumpModuleClassSourceIfRequested(
            Path sourceFile,
            int moduleIndex,
            String className,
            String source) {
        String dumpDir = System.getProperty("qin.moduleClass.sourceDumpDir");
        if (dumpDir == null || dumpDir.isBlank()) {
            return;
        }
        try {
            Path root = Path.of(dumpDir).toAbsolutePath().normalize();
            Files.createDirectories(root);
            String safeName = className.replaceAll("[^A-Za-z0-9_$.-]", "_");
            Path out = root.resolve(moduleIndex + "-" + safeName + ".ts");
            String header = "// sourceFile: " + sourceFile + System.lineSeparator()
                    + "// moduleIndex: " + moduleIndex + System.lineSeparator()
                    + "// className: " + className + System.lineSeparator();
            Files.writeString(out, header + (source == null ? "" : source), StandardCharsets.UTF_8);
        } catch (Exception error) {
            throw new IllegalStateException("Failed to dump module class source for " + className, error);
        }
    }

    private Map<String, String> buildDeclarationClassExportSlots(QinLinkedModuleSource linkedSource) {
        Map<String, String> exportSlots = new LinkedHashMap<>();
        Map<String, String> slotAliases = new LinkedHashMap<>();
        if (linkedSource == null || linkedSource.moduleSections().isEmpty()) {
            return Map.of();
        }
        for (QinLinkedModuleSection section : linkedSource.moduleSections()) {
            String source = section.classSource();
            if (source == null || source.isBlank()) {
                continue;
            }
            Set<String> classNames = collectClassDeclarationNames(source);
            Map<String, String> aliasTargets = collectLocalAliasTargets(source);
            Map<String, String> localExportGetTargets = collectLocalExportGetTargets(source);
            Matcher matcher = EXPORT_INIT_PATTERN.matcher(source);
            while (matcher.find()) {
                String slotName = matcher.group(1);
                String localName = matcher.group(2);
                String resolvedName = resolveDeclarationClassName(localName, classNames, aliasTargets);
                if (resolvedName != null) {
                    exportSlots.put(slotName, canonicalDeclarationClassBinaryName(resolvedName));
                    continue;
                }
                String resolvedSlot = resolveDeclarationClassSlot(localName, aliasTargets, localExportGetTargets);
                if (resolvedSlot != null) {
                    slotAliases.put(slotName, resolvedSlot);
                }
            }
            Matcher getMatcher = EXPORT_INIT_GET_PATTERN.matcher(source);
            while (getMatcher.find()) {
                slotAliases.put(getMatcher.group(1), getMatcher.group(2));
            }
        }
        resolveDeclarationClassSlotAliases(exportSlots, slotAliases);
        return exportSlots.isEmpty() ? Map.of() : Map.copyOf(exportSlots);
    }

    private String canonicalDeclarationClassBinaryName(String binaryName) {
        if (binaryName == null || binaryName.isBlank()) {
            return null;
        }
        String canonical = QinJavaSdkAliasSupport.canonicalBinaryName(binaryName);
        if (!binaryName.equals(canonical)) {
            return canonical;
        }
        if (binaryName.startsWith("__Qin")) {
            return canonical;
        }
        if (binaryName.indexOf('_') < 0) {
            return canonical;
        }
        String dottedCandidate = binaryName.replace('_', '.');
        if (dottedCandidate.contains("..")) {
            return binaryName;
        }
        canonical = QinJavaSdkAliasSupport.canonicalBinaryName(dottedCandidate);
        return Objects.equals(dottedCandidate, canonical) ? binaryName : canonical;
    }

    private Map<String, String> collectLocalAliasTargets(String source) {
        Map<String, String> aliases = new LinkedHashMap<>();
        Matcher matcher = LOCAL_ALIAS_PATTERN.matcher(source);
        while (matcher.find()) {
            String alias = matcher.group(1);
            String target = matcher.group(2);
            if (alias != null && !alias.isBlank() && target != null && !target.isBlank()) {
                aliases.putIfAbsent(alias, target);
            }
        }
        return aliases.isEmpty() ? Map.of() : Map.copyOf(aliases);
    }

    private Map<String, String> collectLocalExportGetTargets(String source) {
        Map<String, String> aliases = new LinkedHashMap<>();
        Matcher matcher = EXPORT_GET_ALIAS_PATTERN.matcher(source);
        while (matcher.find()) {
            String alias = matcher.group(1);
            String targetSlot = matcher.group(2);
            if (alias != null && !alias.isBlank() && targetSlot != null && !targetSlot.isBlank()) {
                aliases.putIfAbsent(alias, targetSlot);
            }
        }
        return aliases.isEmpty() ? Map.of() : Map.copyOf(aliases);
    }

    private String resolveDeclarationClassSlot(
            String name,
            Map<String, String> aliasTargets,
            Map<String, String> localExportGetTargets) {
        if (name == null || name.isBlank()) {
            return null;
        }
        String current = name;
        Set<String> seen = new java.util.LinkedHashSet<>();
        for (int depth = 0; depth < 32 && current != null && !current.isBlank() && seen.add(current); depth++) {
            String slot = localExportGetTargets == null ? null : localExportGetTargets.get(current);
            if (slot != null && !slot.isBlank()) {
                return slot;
            }
            if (aliasTargets == null || aliasTargets.isEmpty()) {
                return null;
            }
            current = aliasTargets.get(current);
        }
        return null;
    }

    private void resolveDeclarationClassSlotAliases(
            Map<String, String> exportSlots,
            Map<String, String> slotAliases) {
        if (exportSlots == null || slotAliases == null || slotAliases.isEmpty()) {
            return;
        }
        boolean changed;
        do {
            changed = false;
            for (Map.Entry<String, String> entry : slotAliases.entrySet()) {
                String slot = entry.getKey();
                if (exportSlots.containsKey(slot)) {
                    continue;
                }
                String resolved = resolveDeclarationClassSlotAlias(entry.getValue(), exportSlots, slotAliases);
                if (resolved != null && !resolved.isBlank()) {
                    exportSlots.put(slot, resolved);
                    changed = true;
                }
            }
        } while (changed);
    }

    private String resolveDeclarationClassSlotAlias(
            String slot,
            Map<String, String> exportSlots,
            Map<String, String> slotAliases) {
        String current = slot;
        Set<String> seen = new java.util.LinkedHashSet<>();
        for (int depth = 0; depth < 64 && current != null && !current.isBlank() && seen.add(current); depth++) {
            String resolved = exportSlots.get(current);
            if (resolved != null && !resolved.isBlank()) {
                return resolved;
            }
            current = slotAliases.get(current);
        }
        return null;
    }

    private String resolveDeclarationClassName(
            String name,
            Set<String> classNames,
            Map<String, String> aliasTargets) {
        if (name == null || name.isBlank()) {
            return null;
        }
        String current = name;
        for (int depth = 0; depth < 32 && current != null && !current.isBlank(); depth++) {
            if (classNames.contains(current)) {
                return current;
            }
            if (aliasTargets == null || aliasTargets.isEmpty()) {
                return null;
            }
            String next = aliasTargets.get(current);
            if (next == null || next.isBlank() || next.equals(current)) {
                return null;
            }
            current = next;
        }
        return null;
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

    private record LoweredModuleClassSource(
            Path sourceFile,
            int moduleIndex,
            String className,
            QinCfaIrStageResult irStageResult) {
        private LoweredModuleClassSource {
            Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
            Objects.requireNonNull(className, "className cannot be null");
            Objects.requireNonNull(irStageResult, "irStageResult cannot be null");
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
