package com.qin.lang.lowering.jvm;

import com.qin.lang.ir.QinBuiltinRegistry;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrConsoleLogJavaInstanceCall;
import com.qin.lang.ir.QinIrConsoleLogJavaStaticCall;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrConsoleLogValue;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJsImport;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.module.resolver.QinEsmSpecifierResolver;
import com.qin.lang.sema.esm.QinEsmExportBinding;
import com.qin.lang.sema.esm.QinEsmExportKind;
import com.qin.lang.sema.esm.QinEsmImportBinding;
import com.qin.lang.sema.esm.QinEsmModuleSemantic;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Shared strict lowering gate for both JVM and JS targets.
 * This implementation validates semantic invariants and performs target-neutral
 * IR rewrites required by Qin runtime shims.
 */
public final class QinStrictEsmJvmLowerer implements QinEsmJvmLowerer {
    private static final int MAX_EXPORT_RESOLUTION_DEPTH = 128;
    private static final String IMPORT_META_SENTINEL = "import.meta";
    private static final String IMPORT_META_URL_SENTINEL = "import.meta.url";
    private static final String QIN_DYNAMIC_IMPORT = "__qin_dynamic_import__";

    private final QinEsmSpecifierResolver specifierResolver = new QinEsmSpecifierResolver();

    @Override
    public QinIrProgram lower(
            QinIrProgram program,
            QinEsmSemanticModel semanticModel,
            QinEsmJvmLoweringContext context) {
        Objects.requireNonNull(program, "program cannot be null");
        Objects.requireNonNull(semanticModel, "semanticModel cannot be null");
        Objects.requireNonNull(context, "context cannot be null");

        for (QinEsmModuleSemantic module : semanticModel.modules().values()) {
            validateUniqueLocalImports(module);
        }
        QinIrProgram loweredProgram = rewriteProgram(program, semanticModel, context);
        validateBuiltinCalls(loweredProgram.consoleValueLogs());
        validateExpressionStatements(loweredProgram.expressionStatements());
        return loweredProgram;
    }

    private QinIrProgram rewriteProgram(
            QinIrProgram program,
            QinEsmSemanticModel semanticModel,
            QinEsmJvmLoweringContext context) {
        String moduleUrl = context.entryFile().toAbsolutePath().normalize().toUri().toString();
        Map<Path, Integer> moduleIndex = buildModuleIndex(context.orderedModules());
        Map<String, QinIrExpression> declarationLookup = new LinkedHashMap<>();
        Set<String> liveImportAliases = new LinkedHashSet<>();

        List<QinIrConstDeclaration> rewrittenDeclarations = new ArrayList<>();
        for (QinIrConstDeclaration declaration : program.declarations()) {
            QinIrExpression rewrittenInitializer = declaration.initializer();
            QinIrIdentifierReference liveAliasSlot = extractLiveImportAliasSlot(rewrittenInitializer);
            if (liveAliasSlot != null) {
                rewrittenInitializer = liveAliasSlot;
                liveImportAliases.add(declaration.name());
            } else {
                rewrittenInitializer = rewriteExpression(
                        declaration.initializer(),
                        semanticModel,
                        context,
                        moduleIndex,
                        moduleUrl,
                        declarationLookup,
                        liveImportAliases);
            }
            rewrittenDeclarations.add(new QinIrConstDeclaration(declaration.name(), rewrittenInitializer));
            declarationLookup.put(declaration.name(), rewrittenInitializer);
        }

        List<QinIrExpressionStatement> rewrittenExpressionStatements = new ArrayList<>();
        for (QinIrExpressionStatement expressionStatement : program.expressionStatements()) {
            rewrittenExpressionStatements.add(new QinIrExpressionStatement(
                    rewriteExpression(
                            expressionStatement.expression(),
                            semanticModel,
                            context,
                            moduleIndex,
                            moduleUrl,
                            declarationLookup,
                            liveImportAliases)));
        }

        List<QinIrConsoleLogValue> rewrittenConsoleValueLogs = new ArrayList<>();
        for (QinIrConsoleLogValue consoleValueLog : program.consoleValueLogs()) {
            rewrittenConsoleValueLogs.add(new QinIrConsoleLogValue(
                    rewriteExpression(
                            consoleValueLog.value(),
                            semanticModel,
                            context,
                            moduleIndex,
                            moduleUrl,
                            declarationLookup,
                            liveImportAliases)));
        }

        // Preserve non-expression IR buckets as-is for now.
        List<QinIrConsoleLogStatement> consoleLogs = program.consoleLogs();
        List<QinIrJavaImport> javaImports = program.javaImports();
        List<QinIrJsImport> jsImports = program.jsImports();
        List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs = program.javaStaticConsoleLogs();
        List<QinIrJavaInstanceMethodCall> javaInstanceMethodCalls = program.javaInstanceMethodCalls();
        List<QinIrConsoleLogJavaInstanceCall> javaInstanceConsoleLogs = program.javaInstanceConsoleLogs();

        return new QinIrProgram(
                rewrittenDeclarations,
                rewrittenExpressionStatements,
                rewrittenConsoleValueLogs,
                consoleLogs,
                javaImports,
                jsImports,
                javaStaticConsoleLogs,
                javaInstanceMethodCalls,
                javaInstanceConsoleLogs,
                program.classDeclarations(),
                program.executionSteps(),
                program.functionModelArtifacts());
    }

    private Map<Path, Integer> buildModuleIndex(List<Path> orderedModules) {
        Map<Path, Integer> moduleIndex = new LinkedHashMap<>();
        for (int i = 0; i < orderedModules.size(); i++) {
            moduleIndex.put(orderedModules.get(i).toAbsolutePath().normalize(), i);
        }
        return moduleIndex;
    }

    private QinIrExpression rewriteExpression(
            QinIrExpression expression,
            QinEsmSemanticModel semanticModel,
            QinEsmJvmLoweringContext context,
            Map<Path, Integer> moduleIndex,
            String moduleUrl,
            Map<String, QinIrExpression> declarationLookup,
            Set<String> liveImportAliases) {
        if (expression instanceof QinIrStringLiteral stringLiteral) {
            if (IMPORT_META_SENTINEL.equals(stringLiteral.value())) {
                return new QinIrObjectLiteral(List.of(
                        new QinIrObjectProperty("url", new QinIrStringLiteral(moduleUrl))));
            }
            if (IMPORT_META_URL_SENTINEL.equals(stringLiteral.value())) {
                return new QinIrStringLiteral(moduleUrl);
            }
            return stringLiteral;
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCall) {
            List<QinIrExpression> rewrittenArguments = new ArrayList<>();
            for (QinIrExpression argument : builtinCall.arguments()) {
                rewrittenArguments.add(rewriteExpression(
                        argument,
                        semanticModel,
                        context,
                        moduleIndex,
                        moduleUrl,
                        declarationLookup,
                        liveImportAliases));
            }

            if ("Global".equals(builtinCall.receiverName())
                    && QIN_DYNAMIC_IMPORT.equals(builtinCall.methodName())
                    && rewrittenArguments.size() == 1) {
                String staticSpecifier = extractDynamicImportSpecifier(
                        rewrittenArguments.get(0),
                        declarationLookup);
                QinIrObjectLiteral namespace = staticSpecifier == null ? null : resolveNamespaceObject(
                        staticSpecifier,
                        semanticModel,
                        context,
                        moduleIndex);
                if (namespace != null) {
                    rewrittenArguments.add(namespace);
                }
            }
            return new QinIrBuiltinCallExpression(
                    builtinCall.receiverName(),
                    builtinCall.methodName(),
                    rewrittenArguments);
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            List<QinIrObjectProperty> rewrittenProperties = new ArrayList<>();
            for (QinIrObjectProperty property : objectLiteral.properties()) {
                rewrittenProperties.add(new QinIrObjectProperty(
                        property.key(),
                        rewriteExpression(
                                property.value(),
                                semanticModel,
                                context,
                                moduleIndex,
                                moduleUrl,
                                declarationLookup,
                                liveImportAliases)));
            }
            return new QinIrObjectLiteral(rewrittenProperties);
        }
        if (expression instanceof QinIrMemberAccessExpression memberAccessExpression) {
            if (liveImportAliases.contains(memberAccessExpression.objectName())) {
                return new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_member_get__",
                        List.of(
                                new QinIrBuiltinCallExpression(
                                        "Global",
                                        "__qin_export_get__",
                                        List.of(new QinIrIdentifierReference(memberAccessExpression.objectName()))),
                                new QinIrStringLiteral(memberAccessExpression.propertyName())));
            }
            return memberAccessExpression;
        }
        if (expression instanceof QinIrIdentifierReference identifierReference) {
            if (liveImportAliases.contains(identifierReference.name())) {
                return new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_export_get__",
                        List.of(new QinIrIdentifierReference(identifierReference.name())));
            }
            return identifierReference;
        }
        return expression;
    }

    private QinIrIdentifierReference extractLiveImportAliasSlot(QinIrExpression initializer) {
        if (!(initializer instanceof QinIrBuiltinCallExpression builtinCallExpression)) {
            return null;
        }
        if (!"Global".equals(builtinCallExpression.receiverName())
                || !"__qin_export_get__".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() != 1) {
            return null;
        }
        QinIrExpression arg0 = builtinCallExpression.arguments().get(0);
        if (!(arg0 instanceof QinIrIdentifierReference identifierReference)) {
            return null;
        }
        String name = identifierReference.name();
        if (!name.startsWith("__qesm_m") || !name.contains("_e_")) {
            return null;
        }
        return identifierReference;
    }

    private String extractDynamicImportSpecifier(
            QinIrExpression argument,
            Map<String, QinIrExpression> declarationLookup) {
        if (argument instanceof QinIrStringLiteral stringLiteral) {
            return stringLiteral.value();
        }
        if (argument instanceof QinIrIdentifierReference identifierReference) {
            QinIrExpression declared = declarationLookup.get(identifierReference.name());
            if (declared instanceof QinIrStringLiteral stringLiteral) {
                return stringLiteral.value();
            }
        }
        return null;
    }

    private QinIrObjectLiteral resolveNamespaceObject(
            String specifier,
            QinEsmSemanticModel semanticModel,
            QinEsmJvmLoweringContext context,
            Map<Path, Integer> moduleIndex) {
        Path importer = context.entryFile().toAbsolutePath().normalize();
        Path targetModule;
        try {
            targetModule = specifierResolver.resolveModule(importer, specifier);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
        if (targetModule == null) {
            return null;
        }
        targetModule = targetModule.toAbsolutePath().normalize();
        if (!semanticModel.modules().containsKey(targetModule)) {
            return null;
        }
        Integer targetModuleIndex = moduleIndex.get(targetModule);
        if (targetModuleIndex == null) {
            return null;
        }

        List<String> exportNames = resolveExportedNames(semanticModel, targetModule, new HashSet<>(), 0);
        if (exportNames.isEmpty()) {
            return new QinIrObjectLiteral(List.of());
        }

        List<QinIrObjectProperty> properties = new ArrayList<>();
        for (String exportName : exportNames) {
            if ("*".equals(exportName)) {
                continue;
            }
            ExportResolution resolution = resolveExportName(
                    semanticModel,
                    targetModule,
                    exportName,
                    new HashSet<>(),
                    0);
            if (!resolution.exists() || resolution.isAmbiguous()) {
                continue;
            }
            Integer ownerIndex = moduleIndex.get(resolution.owner());
            if (ownerIndex == null) {
                continue;
            }
            String symbol = exportSymbol(ownerIndex, resolution.exportName());
            properties.add(new QinIrObjectProperty(exportName, new QinIrIdentifierReference(symbol)));
        }
        return new QinIrObjectLiteral(properties);
    }

    private List<String> resolveExportedNames(
            QinEsmSemanticModel semanticModel,
            Path moduleFile,
            Set<Path> visiting,
            int depth) {
        if (moduleFile == null || depth > MAX_EXPORT_RESOLUTION_DEPTH) {
            return List.of();
        }
        Path normalized = moduleFile.toAbsolutePath().normalize();
        if (!visiting.add(normalized)) {
            return List.of();
        }

        QinEsmModuleSemantic module = semanticModel.modules().get(normalized);
        if (module == null) {
            return List.of();
        }

        LinkedHashSet<String> names = new LinkedHashSet<>();
        for (QinEsmExportBinding exportBinding : module.exports()) {
            if (exportBinding.kind() == QinEsmExportKind.RE_EXPORT_ALL) {
                continue;
            }
            names.add(exportBinding.exportName());
        }

        for (QinEsmExportBinding exportBinding : module.exports()) {
            if (exportBinding.kind() != QinEsmExportKind.RE_EXPORT_ALL) {
                continue;
            }
            if (exportBinding.resolvedModule() == null) {
                continue;
            }
            for (String inherited : resolveExportedNames(
                    semanticModel,
                    exportBinding.resolvedModule(),
                    visiting,
                    depth + 1)) {
                if ("default".equals(inherited)) {
                    continue;
                }
                ExportResolution resolution = resolveExportName(
                        semanticModel,
                        normalized,
                        inherited,
                        new HashSet<>(),
                        0);
                if (resolution.exists() && !resolution.isAmbiguous()) {
                    names.add(inherited);
                }
            }
        }
        visiting.remove(normalized);
        return new ArrayList<>(names);
    }

    private ExportResolution resolveExportName(
            QinEsmSemanticModel semanticModel,
            Path moduleFile,
            String exportName,
            Set<String> visiting,
            int depth) {
        if (moduleFile == null || exportName == null || exportName.isBlank() || depth > MAX_EXPORT_RESOLUTION_DEPTH) {
            return ExportResolution.notResolvedResult();
        }
        Path normalized = moduleFile.toAbsolutePath().normalize();
        String visitKey = normalized + "::" + exportName;
        if (!visiting.add(visitKey)) {
            return ExportResolution.notResolvedResult();
        }
        try {
            QinEsmModuleSemantic module = semanticModel.modules().get(normalized);
            if (module == null) {
                return ExportResolution.notResolvedResult();
            }

            List<QinEsmExportBinding> direct = new ArrayList<>();
            List<QinEsmExportBinding> stars = new ArrayList<>();
            for (QinEsmExportBinding exportBinding : module.exports()) {
                if (exportBinding.kind() == QinEsmExportKind.RE_EXPORT_ALL) {
                    stars.add(exportBinding);
                    continue;
                }
                if (exportName.equals(exportBinding.exportName())) {
                    direct.add(exportBinding);
                }
            }

            if (direct.size() > 1) {
                return ExportResolution.ambiguousResult();
            }
            if (direct.size() == 1) {
                QinEsmExportBinding directExport = direct.get(0);
                if (directExport.kind() == QinEsmExportKind.RE_EXPORT_NAMED
                        && directExport.resolvedModule() != null) {
                    return resolveExportName(
                            semanticModel,
                            directExport.resolvedModule(),
                            directExport.localName(),
                            visiting,
                            depth + 1);
                }
                return ExportResolution.found(normalized, exportName);
            }

            ExportResolution found = ExportResolution.notResolvedResult();
            for (QinEsmExportBinding star : stars) {
                if (star.resolvedModule() == null) {
                    continue;
                }
                ExportResolution sub = resolveExportName(
                        semanticModel,
                        star.resolvedModule(),
                        exportName,
                        visiting,
                        depth + 1);
                if (sub.isAmbiguous()) {
                    return ExportResolution.ambiguousResult();
                }
                if (sub.exists()) {
                    if (found.exists() && !sameResolvedBinding(found, sub)) {
                        return ExportResolution.ambiguousResult();
                    }
                    found = sub;
                }
            }
            return found;
        } finally {
            visiting.remove(visitKey);
        }
    }

    private boolean sameResolvedBinding(ExportResolution left, ExportResolution right) {
        if (!left.exists() || !right.exists()) {
            return false;
        }
        return left.owner() != null
                && right.owner() != null
                && left.owner().equals(right.owner())
                && left.exportName().equals(right.exportName());
    }

    private String exportSymbol(int moduleIndex, String exportName) {
        return "__qesm_m" + moduleIndex + "_e_" + sanitize(exportName);
    }

    private String sanitize(String name) {
        if (name == null || name.isBlank()) {
            return "_";
        }
        String sanitized = name.replaceAll("[^A-Za-z0-9_$]", "_");
        if (sanitized.isEmpty()) {
            return "_";
        }
        char first = sanitized.charAt(0);
        if (Character.isDigit(first)) {
            return "_" + sanitized;
        }
        return sanitized;
    }

    private record ExportResolution(
            boolean exists,
            boolean isAmbiguous,
            Path owner,
            String exportName) {
        private static ExportResolution found(Path owner, String exportName) {
            return new ExportResolution(true, false, owner, exportName);
        }

        private static ExportResolution ambiguousResult() {
            return new ExportResolution(false, true, null, "");
        }

        private static ExportResolution notResolvedResult() {
            return new ExportResolution(false, false, null, "");
        }
    }

    private void validateUniqueLocalImports(QinEsmModuleSemantic module) {
        Set<String> locals = new HashSet<>();
        for (QinEsmImportBinding binding : module.imports()) {
            if (binding.localName() == null || binding.localName().isBlank()) {
                continue;
            }
            if (!locals.add(binding.localName())) {
                throw new IllegalArgumentException(
                        "ESM3101 duplicate local import binding: " + binding.localName()
                                + " at " + module.sourceFile().toAbsolutePath());
            }
        }
    }

    private void validateBuiltinCalls(Iterable<QinIrConsoleLogValue> logs) {
        for (QinIrConsoleLogValue log : logs) {
            validateExpression(log.value());
        }
    }

    private void validateExpressionStatements(Iterable<QinIrExpressionStatement> expressionStatements) {
        for (QinIrExpressionStatement expressionStatement : expressionStatements) {
            validateExpression(expressionStatement.expression());
        }
    }

    private void validateExpression(QinIrExpression expression) {
        if (expression instanceof QinIrBuiltinCallExpression builtinCall) {
            validateBuiltinCall(builtinCall);
            for (QinIrExpression argument : builtinCall.arguments()) {
                validateExpression(argument);
            }
            return;
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            for (QinIrObjectProperty property : objectLiteral.properties()) {
                validateExpression(property.value());
            }
            return;
        }
        if (expression instanceof QinIrMemberAccessExpression
                || expression instanceof QinIrStringLiteral
                || expression instanceof QinIrIdentifierReference) {
            return;
        }
    }

    private void validateBuiltinCall(QinIrBuiltinCallExpression call) {
        QinBuiltinRegistry.BuiltinMethod builtinMethod = QinBuiltinRegistry
                .resolve(call.receiverName(), call.methodName(), call.arguments().size())
                .orElseThrow(() -> new IllegalArgumentException(
                        "QJS1001 unknown builtin call: "
                                + call.receiverName() + "." + call.methodName()
                                + "/" + call.arguments().size()));

        for (int i = 0; i < builtinMethod.argumentKinds().size(); i++) {
            QinBuiltinRegistry.BuiltinArgKind kind = builtinMethod.argumentKinds().get(i);
            QinIrExpression argument = call.arguments().get(i);
            if (kind == QinBuiltinRegistry.BuiltinArgKind.STRING && !(argument instanceof QinIrStringLiteral)) {
                throw new IllegalArgumentException(
                        "QJS1003 builtin argument type mismatch at index " + i + " for "
                                + call.receiverName() + "." + call.methodName() + ": expected string");
            }
        }
    }
}
