package com.qin.runtime.core;

import com.qin.lang.module.policy.QinImportDescriptor;
import com.qin.lang.module.policy.QinImportKind;
import com.qin.lang.module.policy.QinImportParser;
import com.qin.lang.module.policy.QinImportPolicyChecker;
import com.qin.lang.module.resolver.QinEsmSpecifierResolver;
import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.module.resolver.QinModuleSource;
import com.qin.lang.module.resolver.QinResolvedImport;
import com.qin.lang.sema.esm.QinEsmExportBinding;
import com.qin.lang.sema.esm.QinEsmExportKind;
import com.qin.lang.sema.esm.QinEsmImportBinding;
import com.qin.lang.sema.esm.QinEsmImportKind;
import com.qin.lang.sema.esm.QinEsmLinkValidator;
import com.qin.lang.sema.esm.QinEsmModuleSemantic;
import com.qin.lang.sema.esm.QinEsmRuntimeFeatureValidator;
import com.qin.lang.sema.esm.QinEsmSemanticAnalyzer;
import com.qin.lang.sema.esm.QinEsmSemanticModel;
import com.qin.parser.QinParserFacade;

import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Frontend ESM service: validate module graph and transpile frontend modules to browser JS.
 */
public final class QinFrontendEsmService {
    private static final Pattern IMPORT_FROM_PATTERN = Pattern.compile(
            "(?m)(import\\s+[^;\\n]*?\\s+from\\s*[\"'])([^\"']+)([\"'])");
    private static final Pattern IMPORT_SIDE_EFFECT_PATTERN = Pattern.compile(
            "(?m)(import\\s*[\"'])([^\"']+)([\"'])");
    private static final Pattern EXPORT_FROM_PATTERN = Pattern.compile(
            "(?m)(export\\s+(?:\\*\\s*(?:as\\s+[A-Za-z_$][\\w$]*\\s*)?|\\{[^}\\n]*})\\s*from\\s*[\"'])([^\"']+)([\"'])");
    private static final Pattern CONTROLLER_EXPORT_NAME_PATTERN = Pattern.compile(
            "\\bexport\\s+(?:object|class)\\s+([A-Za-z_$][\\w$]*)");
    private static final Pattern CONTROLLER_NAME_FIELD_PATTERN = Pattern.compile(
            "\\bcontrollerName\\s*=\\s*[\"']([^\"']+)[\"']");
    private static final Pattern CONTROLLER_ROUTE_PATTERN = Pattern.compile(
            "@(?:Get|Post|Delete)Mapping\\s*\\([^)]*\\)\\s*(?:async\\s+)?([A-Za-z_$][\\w$]*)\\s*\\(");
    private static final Pattern QIN_APP_OBJECT_START_PATTERN = Pattern.compile(
            "(?s)@WebRoot\\s*\\([^)]*\\)\\s*export\\s+object\\s+App\\s*\\{");
    private static final Pattern QIN_PACKAGE_OVERRIDES_BLOCK = Pattern.compile(
            "packageOverrides\\s*:\\s*\\{([^}]*)\\}",
            Pattern.DOTALL);
    private static final Pattern QIN_STRING_FIELD = Pattern.compile("[\"']([^\"']+)[\"']\\s*:\\s*[\"']([^\"']*)[\"']");
    private static final Pattern CSSTS_MERGE_PATTERN = Pattern.compile("cssts\\.merge\\(([^)]*)\\)");
    private static final String CSSTS_STYLE_VIRTUAL_MODULE_URL = "/@qin-mod/__virtual/cssts.css.js";
    private static final String CSSTS_ATOM_VIRTUAL_MODULE_URL = "/@qin-mod/__virtual/csstsAtom.js";
    private static final String CSSTS_RUNTIME_VIRTUAL_MODULE_URL = "/@qin-mod/__virtual/cssts-runtime.js";
    private static final String QIN_FRONTEND_RUNTIME_VIRTUAL_MODULE_URL = "/@qin-mod/__virtual/qin.js";
    private static final String FRONTEND_SEMANTIC_CACHE_VERSION = "qin-frontend-semantic-cache-v1";
    private static final Set<String> HOT_REFRESH_EXTENSIONS = Set.of(
            ".ovs", ".vue", ".cssts", ".css",
            ".svg", ".png", ".jpg", ".jpeg", ".gif", ".webp", ".ico", ".avif");

    private final Path projectRoot;
    private final Path entryFile;
    private final QinModuleGraph graph;
    private final Map<Path, QinModuleSource> moduleSourceMap;
    private final Map<Path, String> moduleUrlMap;
    private final Map<String, Path> requestPathMap;
    private final Map<String, String> virtualModuleContentMap;
    private final Map<Path, String> transpiledModuleCache = new LinkedHashMap<>();
    private final Map<Path, CachedOvsModule> ovsTranspiledModuleCache = new LinkedHashMap<>();
    private final String entryModuleUrl;
    private final QinVueSfcCompiler vueSfcCompiler;
    private final QinOvsCompiler ovsCompiler;
    private final QinCsstsCompiler csstsCompiler;
    private final Map<Path, String> csstsCssByModule = new LinkedHashMap<>();
    private final Map<Path, String> csstsAtomByModule = new LinkedHashMap<>();

    private QinFrontendEsmService(
            Path projectRoot,
            Path entryFile,
            QinModuleGraph graph,
            Map<Path, QinModuleSource> moduleSourceMap,
            Map<Path, String> moduleUrlMap,
            Map<String, Path> requestPathMap,
            Map<String, String> virtualModuleContentMap,
            String entryModuleUrl,
            QinVueSfcCompiler vueSfcCompiler,
            QinOvsCompiler ovsCompiler,
            QinCsstsCompiler csstsCompiler) {
        this.projectRoot = projectRoot;
        this.entryFile = entryFile;
        this.graph = graph;
        this.moduleSourceMap = moduleSourceMap;
        this.moduleUrlMap = moduleUrlMap;
        this.requestPathMap = requestPathMap;
        this.virtualModuleContentMap = virtualModuleContentMap;
        this.entryModuleUrl = entryModuleUrl;
        this.vueSfcCompiler = vueSfcCompiler;
        this.ovsCompiler = ovsCompiler;
        this.csstsCompiler = csstsCompiler;
    }

    public static QinFrontendEsmService create(Path projectRoot, Path frontendEntry) throws Exception {
        QinPhaseTimer profile = QinPhaseTimer.start("frontend-service-create");
        Path root = projectRoot.toAbsolutePath().normalize();
        Path entry = frontendEntry.toAbsolutePath().normalize();

        QinModuleGraph graph = new QinModuleGraphBuilder(
                (importer, descriptor, resolvedModule, resolvedSource) ->
                        virtualFrontendServerControllerSource(root, importer, resolvedModule, resolvedSource))
                .build(entry);
        profile.checkpoint("build module graph", "modules=" + graph.modules().size());
        validatePolicyAndSemantics(root, graph);
        profile.checkpoint("validate policy and semantics");

        Map<Path, QinModuleSource> sourceMap = new LinkedHashMap<>();
        Map<Path, String> urlMap = new LinkedHashMap<>();
        Map<String, Path> requestPathMap = new LinkedHashMap<>();
        Map<String, String> virtualModuleContentMap = new LinkedHashMap<>();
        for (QinModuleSource module : graph.modules()) {
            Path file = module.file().toAbsolutePath().normalize();
            sourceMap.put(file, module);
            if (isFrontendModuleFile(file)) {
                String moduleUrl = toModuleUrl(root, file);
                urlMap.put(file, moduleUrl);
                requestPathMap.put(moduleUrl, file);
                for (String alias : requestPathAliases(moduleUrl, file)) {
                    requestPathMap.put(alias, file);
                }
            }
        }
        profile.checkpoint("map modules", "frontendModules=" + urlMap.size());

        String entryUrl = urlMap.get(entry);
        if (entryUrl == null) {
            throw new IllegalArgumentException("Frontend entry is not a supported module: " + entry.toAbsolutePath());
        }
        QinVueSfcCompiler vueCompiler = QinViteVuePluginCompiler.isEnabled(root)
                ? new QinViteVuePluginCompiler()
                : new QinOfficialVueSfcCompiler();
        profile.checkpoint("select vue compiler", vueCompiler.getClass().getSimpleName());
        QinFrontendEsmService service = new QinFrontendEsmService(
                root,
                entry,
                graph,
                sourceMap,
                urlMap,
                requestPathMap,
                virtualModuleContentMap,
                entryUrl,
                vueCompiler,
                new QinOvsCompiler(),
                new QinCsstsCompiler());
        service.prewarmCsstsGraphModules();
        profile.done("entry=" + entry.getFileName());
        return service;
    }

    public String bootstrapJs() {
        return "import \"" + entryModuleUrl + "\";\n";
    }

    public List<String> collectViteHotUpdateMessages(List<Path> changedFiles) {
        if (changedFiles == null || changedFiles.isEmpty() || !(vueSfcCompiler instanceof QinViteVuePluginCompiler compiler)) {
            return List.of();
        }
        return compiler.collectHotUpdateMessages(projectRoot, changedFiles);
    }

    synchronized boolean refreshChangedFrontendModules(List<Path> changedFiles) throws IOException {
        if (changedFiles == null || changedFiles.isEmpty()) {
            return false;
        }
        Map<Path, QinModuleSource> refreshed = new LinkedHashMap<>();
        for (Path changedFile : changedFiles) {
            Path normalized = changedFile == null ? null : changedFile.toAbsolutePath().normalize();
            if (normalized == null
                    || !Files.isRegularFile(normalized)
                    || normalized.equals(entryFile)
                    || !moduleSourceMap.containsKey(normalized)
                    || !isHotRefreshModuleFile(normalized)) {
                return false;
            }
            QinModuleSource previous = moduleSourceMap.get(normalized);
            String source = readSource(normalized);
            List<QinResolvedImport> imports = resolveImportsForRefresh(normalized, source);
            if (!sameImportGraph(previous.imports(), imports)) {
                return false;
            }
            refreshed.put(normalized, new QinModuleSource(normalized, source, imports));
        }
        refreshed.forEach((file, module) -> {
            moduleSourceMap.put(file, module);
            invalidateFrontendModule(file);
        });
        return true;
    }

    public String transpileByRequestPath(String requestPath) throws IOException {
        String normalizedRequestPath = stripQinHmrQuery(requestPath);
        String virtualContent = resolveVirtualModuleContent(normalizedRequestPath);
        if (virtualContent != null) {
            return virtualContent;
        }
        String vueQueryModule = transpileVuePluginQueryModule(normalizedRequestPath);
        if (vueQueryModule != null) {
            return vueQueryModule;
        }
        Path moduleFile = resolveRequestToModuleFile(normalizedRequestPath);
        if (moduleFile == null) {
            return null;
        }
        return injectQinHmrPrelude(normalizedRequestPath, moduleFile, transpileModule(moduleFile));
    }

    public String transpileByPublicRequestPath(String requestPath) throws IOException {
        String normalizedRequestPath = stripQinHmrQuery(requestPath);
        if (normalizedRequestPath == null || normalizedRequestPath.isBlank()) {
            return null;
        }
        Path moduleFile = resolvePublicRequestToModuleFile(normalizedRequestPath);
        if (moduleFile == null) {
            return null;
        }
        String moduleUrl = moduleUrlMap.get(moduleFile);
        if (moduleUrl == null) {
            return null;
        }
        return injectQinHmrPrelude(moduleUrl, moduleFile, transpileModule(moduleFile));
    }

    private Path resolvePublicRequestToModuleFile(String normalizedRequestPath) {
        int queryIndex = normalizedRequestPath.indexOf('?');
        String pathOnly = queryIndex < 0 ? normalizedRequestPath : normalizedRequestPath.substring(0, queryIndex);
        if (!pathOnly.startsWith("/")) {
            return null;
        }
        Path directModuleFile = projectRoot.resolve(pathOnly.substring(1)).toAbsolutePath().normalize();
        if (directModuleFile.startsWith(projectRoot)
                && moduleSourceMap.containsKey(directModuleFile)
                && isBrowserScriptModuleFile(directModuleFile)) {
            return directModuleFile;
        }

        Path mappedModuleFile = requestPathMap.get("/@qin-mod" + pathOnly);
        if (mappedModuleFile != null && isBrowserScriptModuleFile(mappedModuleFile)) {
            return mappedModuleFile;
        }
        return null;
    }

    private String injectQinHmrPrelude(String requestPath, Path moduleFile, String transpiled) {
        if (transpiled == null || requestPath == null || requestPath.contains("?")) {
            return transpiled;
        }
        String moduleUrl = toModuleUrl(projectRoot, moduleFile.toAbsolutePath().normalize());
        String escapedModuleUrl = escapeJsStringLiteral(moduleUrl);
        return """
                import { createHotContext as __qinCreateHotContext } from "/@qin/dev-client.js";
                import.meta.hot = __qinCreateHotContext("%s");
                %s
                """.formatted(escapedModuleUrl, transpiled);
    }

    public void emitProduction(Path staticRoot) throws IOException {
        QinPhaseTimer profile = QinPhaseTimer.start("frontend-production-emit");
        Path moduleRoot = staticRoot.resolve("@qin-mod").normalize();
        Files.createDirectories(moduleRoot);
        profile.checkpoint("prepare output root", moduleRoot.toString());

        List<Path> ovsFiles = new ArrayList<>();
        int frontendModuleCount = 0;
        for (QinModuleSource module : graph.modules()) {
            Path file = module.file().toAbsolutePath().normalize();
            if (!isFrontendModuleFile(file)) {
                continue;
            }
            frontendModuleCount++;
            if (isOvsModuleFile(file)) {
                ovsFiles.add(file);
            }
        }
        profile.checkpoint("collect graph modules",
                "frontendModules=" + frontendModuleCount + ", ovsModules=" + ovsFiles.size());
        if (!ovsFiles.isEmpty()) {
            prewarmOvsModules(ovsFiles);
        }
        profile.checkpoint("prewarm ovs modules", "ovsModules=" + ovsFiles.size());

        int writtenModules = 0;
        for (QinModuleSource module : graph.modules()) {
            Path file = module.file().toAbsolutePath().normalize();
            if (!isFrontendModuleFile(file)) {
                continue;
            }
            String moduleUrl = moduleUrlMap.get(file);
            if (moduleUrl == null) {
                continue;
            }
            String relativeOutput = moduleUrl.substring("/@qin-mod/".length());
            Path output = moduleRoot.resolve(relativeOutput).normalize();
            Path parent = output.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(output, transpileModule(file), StandardCharsets.UTF_8);
            writtenModules++;
        }
        profile.checkpoint("write graph modules", "modules=" + writtenModules);

        emitVirtualModules(staticRoot);
        profile.checkpoint("emit virtual modules");

        Files.writeString(staticRoot.resolve("app.js"), bootstrapJs(), StandardCharsets.UTF_8);
        profile.done("bootstrap");
    }

    private synchronized String transpileModule(Path moduleFile) throws IOException {
        Path normalizedModuleFile = moduleFile.toAbsolutePath().normalize();
        QinModuleSource module = moduleSourceMap.get(moduleFile.toAbsolutePath().normalize());
        if (module == null) {
            throw new IllegalArgumentException("Unknown frontend module: " + moduleFile.toAbsolutePath());
        }

        String source = module.source();
        boolean cacheable = !isOvsModuleFile(normalizedModuleFile);
        if (cacheable) {
            String cached = transpiledModuleCache.get(normalizedModuleFile);
            if (cached != null) {
                return cached;
            }
        }

        String transpiled;
        if (isServerControllerModule(moduleFile, readSource(moduleFile))) {
            transpiled = renderQinWebRpcControllerClient(readSource(moduleFile));
        } else if (isVueModuleFile(moduleFile)) {
            transpiled = transpileVueModule(moduleFile, source);
        } else if (isOvsModuleFile(moduleFile)) {
            transpiled = transpileOvsModule(moduleFile, source);
        } else if (isCsstsModuleFile(moduleFile)) {
            transpiled = transpileCsstsModule(moduleFile, source);
        } else if (isCssModuleFile(moduleFile)) {
            transpiled = renderCssInjectionModule(source);
        } else if (isAssetModuleFile(moduleFile)) {
            transpiled = renderAssetUrlModule(moduleFile);
        } else {
            if (isUnifiedAppEntry(moduleFile, source)) {
                source = frontendSourceFromUnifiedAppEntry(source);
            }
            source = rewriteSpecifiers(module, source, IMPORT_FROM_PATTERN);
            source = rewriteSpecifiers(module, source, EXPORT_FROM_PATTERN);
            source = rewriteSpecifiers(module, source, IMPORT_SIDE_EFFECT_PATTERN);
            transpiled = source;
        }
        if (cacheable) {
            transpiledModuleCache.put(normalizedModuleFile, transpiled);
        }
        return transpiled;
    }

    private List<QinResolvedImport> resolveImportsForRefresh(Path sourceFile, String source) {
        QinImportParser importParser = new QinImportParser();
        QinEsmSpecifierResolver specifierResolver = new QinEsmSpecifierResolver();
        List<QinResolvedImport> imports = new ArrayList<>();
        for (QinImportDescriptor descriptor : importParser.parse(sourceFile, source)) {
            Path resolvedModule = null;
            if (!descriptor.typeOnly()
                    && (descriptor.kind() == QinImportKind.LOCAL || descriptor.kind() == QinImportKind.JS)) {
                resolvedModule = specifierResolver.resolveModule(sourceFile, descriptor.moduleSpecifier());
            }
            imports.add(new QinResolvedImport(descriptor, resolvedModule));
        }
        return imports;
    }

    private boolean sameImportGraph(List<QinResolvedImport> previous, List<QinResolvedImport> next) {
        if (previous.size() != next.size()) {
            return false;
        }
        for (int i = 0; i < previous.size(); i++) {
            QinResolvedImport left = previous.get(i);
            QinResolvedImport right = next.get(i);
            QinImportDescriptor leftDescriptor = left.descriptor();
            QinImportDescriptor rightDescriptor = right.descriptor();
            if (!leftDescriptor.moduleSpecifier().equals(rightDescriptor.moduleSpecifier())
                    || leftDescriptor.kind() != rightDescriptor.kind()
                    || leftDescriptor.typeOnly() != rightDescriptor.typeOnly()
                    || !Objects.equals(left.resolvedModule(), right.resolvedModule())) {
                return false;
            }
        }
        return true;
    }

    private void invalidateFrontendModule(Path moduleFile) {
        Path normalized = moduleFile.toAbsolutePath().normalize();
        transpiledModuleCache.remove(normalized);
        ovsTranspiledModuleCache.remove(normalized);
        clearVirtualModulesForModule(normalized);
        csstsCssByModule.remove(normalized);
        csstsAtomByModule.remove(normalized);
        refreshCsstsGlobalVirtualModules();
    }

    private void clearVirtualModulesForModule(Path moduleFile) {
        String base = toModuleUrl(projectRoot, moduleFile);
        virtualModuleContentMap.keySet().removeIf(key -> key != null && key.startsWith(base + "?"));
    }

    private static String virtualFrontendServerControllerSource(
            Path projectRoot,
            Path importer,
            Path resolvedModule,
            String resolvedSource) {
        if (projectRoot == null || importer == null || resolvedModule == null || resolvedSource == null) {
            return null;
        }
        Path root = projectRoot.toAbsolutePath().normalize();
        Path importerFile = importer.toAbsolutePath().normalize();
        Path resolvedFile = resolvedModule.toAbsolutePath().normalize();
        if (!isFrontendSourceFile(root, importerFile)
                || !isControllerSourceFile(root, resolvedFile)
                || !isServerControllerSource(resolvedSource)) {
            return null;
        }
        String exportName = controllerExportName(resolvedSource);
        if (exportName == null) {
            return null;
        }
        return "export const " + exportName + " = null;\n";
    }

    private static boolean isServerControllerModule(Path moduleFile, String source) {
        if (moduleFile == null || source == null) {
            return false;
        }
        String normalized = moduleFile.toAbsolutePath().normalize().toString().replace('\\', '/');
        return normalized.contains("/main/controllers/") && isServerControllerSource(source);
    }

    private static boolean isHotRefreshModuleFile(Path file) {
        String name = file == null || file.getFileName() == null
                ? ""
                : file.getFileName().toString().toLowerCase();
        for (String extension : HOT_REFRESH_EXTENSIONS) {
            if (name.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isFrontendSourceFile(Path root, Path file) {
        return file.startsWith(root.resolve("app").normalize())
                || file.startsWith(root.resolve("src/app").normalize())
                || file.equals(root.resolve("src/app.qin").normalize());
    }

    private static boolean isControllerSourceFile(Path root, Path file) {
        return file.startsWith(root.resolve("main").resolve("controllers").normalize())
                || file.startsWith(root.resolve("src").resolve("main").resolve("controllers").normalize());
    }

    private static boolean isServerControllerSource(String source) {
        return source != null
                && (source.contains("@RestController") || source.contains("@Controller"))
                && CONTROLLER_EXPORT_NAME_PATTERN.matcher(source).find();
    }

    private static String renderQinWebRpcControllerClient(String source) {
        String exportName = controllerExportName(source);
        if (exportName == null || exportName.isBlank()) {
            throw new IllegalArgumentException("QinWeb RPC controller source must export an object or class");
        }
        String controllerName = controllerRpcName(source, exportName);
        List<String> methods = controllerMethodNames(source);
        if (methods.isEmpty()) {
            methods = List.of("getAll", "create", "remove");
        }
        StringBuilder methodEntries = new StringBuilder();
        for (String method : methods) {
            methodEntries.append("  ")
                    .append(method)
                    .append("(input = {}) { return __qinWebCall(")
                    .append('"').append(escapeJsStringLiteral(controllerName)).append('.').append(method).append('"')
                    .append(", input); },\n");
        }
        return """
                async function __qinWebCall(methodName, input = {}) {
                  const response = await fetch(`/api/rpc/${encodeURIComponent(methodName)}`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(input ?? {})
                  });
                  const text = await response.text();
                  const payload = text ? JSON.parse(text) : {};
                  if (!response.ok) {
                    const detail = payload.detail ? `: ${payload.detail}` : "";
                    throw new Error(`${payload.error || response.statusText}${detail}`);
                  }
                  return payload;
                }

                export const %s = {
                %s};
                """.formatted(exportName, methodEntries);
    }

    private static String controllerExportName(String source) {
        Matcher matcher = CONTROLLER_EXPORT_NAME_PATTERN.matcher(source == null ? "" : source);
        return matcher.find() ? matcher.group(1) : null;
    }

    private static String controllerRpcName(String source, String exportName) {
        Matcher matcher = CONTROLLER_NAME_FIELD_PATTERN.matcher(source == null ? "" : source);
        return matcher.find() ? matcher.group(1) : exportName;
    }

    private static List<String> controllerMethodNames(String source) {
        Matcher matcher = CONTROLLER_ROUTE_PATTERN.matcher(source == null ? "" : source);
        List<String> methods = new ArrayList<>();
        Set<String> seen = new LinkedHashSet<>();
        while (matcher.find()) {
            String method = matcher.group(1);
            if (seen.add(method)) {
                methods.add(method);
            }
        }
        return methods;
    }

    private void prewarmCsstsGraphModules() throws IOException {
        boolean hasCsstsGraphModule = false;
        for (QinModuleSource module : graph.modules()) {
            Path file = module.file().toAbsolutePath().normalize();
            if (!isFrontendModuleFile(file)) {
                continue;
            }
            String source = module.source();
            if (isOvsModuleFile(file)) {
                hasCsstsGraphModule = true;
            } else if (isCsstsModuleFile(file)
                    || (isVueModuleFile(file) && requiresQinNativeVueCompiler(source))) {
                hasCsstsGraphModule = true;
                transpileModule(file);
            }
        }
        if (hasCsstsGraphModule || !csstsCssByModule.isEmpty() || !csstsAtomByModule.isEmpty()) {
            refreshCsstsGlobalVirtualModules();
        }
    }

    private void prewarmOvsModules(List<Path> files) throws IOException {
        Map<Path, String> sources = new LinkedHashMap<>();
        for (Path file : files) {
            Path normalizedFile = file.toAbsolutePath().normalize();
            QinModuleSource module = moduleSourceMap.get(normalizedFile);
            if (module != null) {
                sources.put(normalizedFile, module.source());
            } else {
                sources.put(normalizedFile, readSource(normalizedFile));
            }
        }
        Map<Path, QinOvsCompiler.QinOvsCompileResult> results;
        Map<Path, String> cacheIdentities;
        try {
            results = ovsCompiler.compileAll(projectRoot, sources);
            cacheIdentities = ovsCompiler.frontendCacheIdentities(projectRoot, sources);
        } catch (Exception error) {
            throw new IOException("Qin OVS batch compilation failed", error);
        }
        for (Path file : files) {
            Path normalizedFile = file.toAbsolutePath().normalize();
            QinOvsCompiler.QinOvsCompileResult result = results.get(normalizedFile);
            if (result == null) {
                continue;
            }
            QinModuleSource module = moduleSourceMap.get(normalizedFile);
            QinModuleSource sourceModule = module != null
                    ? module
                    : new QinModuleSource(normalizedFile, readSource(normalizedFile), List.of());
            String source = sources.getOrDefault(normalizedFile, sourceModule.source());
            String transpiled = finishOvsModule(normalizedFile, sourceModule, result);
            ovsTranspiledModuleCache.put(
                    normalizedFile,
                    new CachedOvsModule(
                            source == null ? "" : source,
                            cacheIdentities.get(normalizedFile),
                            transpiled));
        }
    }

    private String transpileVueModule(Path moduleFile, String source) {
        QinModuleSource module = moduleSourceMap.get(moduleFile.toAbsolutePath().normalize());
        QinModuleSource sourceModule = module != null
                ? module
                : new QinModuleSource(moduleFile.toAbsolutePath().normalize(), source, List.of());
        QinVueSfcCompiler compiler = requiresQinNativeVueCompiler(source)
                ? new QinOfficialVueSfcCompiler()
                : vueSfcCompiler;
        QinVueSfcModuleResult result = compiler.transpileVueModule(
                moduleFile,
                source,
                sourceModule,
                specifier -> rewriteSpecifier(sourceModule, specifier));
        registerViteVirtualModules(result);
        registerVueVirtualModules(moduleFile, result);
        return result.moduleCode();
    }

    private static boolean requiresQinNativeVueCompiler(String source) {
        if (source == null || source.isBlank()) {
            return false;
        }
        return source.contains("lang=\"cssts\"")
                || source.contains("lang='cssts'");
    }

    private void registerViteVirtualModules(QinVueSfcModuleResult result) {
        if (result == null || result.virtualModules() == null || result.virtualModules().isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : result.virtualModules().entrySet()) {
            if (entry.getKey() != null && !entry.getKey().isBlank() && entry.getValue() != null) {
                virtualModuleContentMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private String transpileVuePluginQueryModule(String requestPath) {
        if (requestPath == null || !requestPath.contains("?")) {
            return null;
        }
        int queryIndex = requestPath.indexOf('?');
        String pathOnly = requestPath.substring(0, queryIndex);
        String query = requestPath.substring(queryIndex + 1);
        if (!query.contains("vue") || !query.contains("type=")) {
            return null;
        }
        Path moduleFile = requestPathMap.get(pathOnly);
        if (moduleFile == null || !isVueModuleFile(moduleFile)) {
            return null;
        }
        QinModuleSource module = moduleSourceMap.get(moduleFile.toAbsolutePath().normalize());
        String source = module != null ? module.source() : readSource(moduleFile);
        QinModuleSource sourceModule = module != null
                ? module
                : new QinModuleSource(moduleFile.toAbsolutePath().normalize(), source, List.of());
        QinVueSfcModuleResult result = vueSfcCompiler.transpileVueQueryModule(
                moduleFile,
                source,
                query,
                sourceModule,
                specifier -> rewriteSpecifier(sourceModule, specifier));
        registerViteVirtualModules(result);
        return result == null || result.moduleCode() == null || result.moduleCode().isBlank()
                ? null
                : result.moduleCode();
    }

    private synchronized String transpileOvsModule(Path moduleFile, String source) {
        Path normalizedModuleFile = moduleFile.toAbsolutePath().normalize();
        String cacheSource = source == null ? "" : source;
        CachedOvsModule cached = ovsTranspiledModuleCache.get(normalizedModuleFile);
        if (cached != null && cacheSource.equals(cached.source())) {
            return cached.transpiled();
        }

        try {
            prewarmOvsModules(ovsGraphFilesForOnDemandBatch(normalizedModuleFile));
        } catch (Exception error) {
            throw new IllegalStateException("Qin OVS compilation failed for " + moduleFile.toAbsolutePath(), error);
        }

        cached = ovsTranspiledModuleCache.get(normalizedModuleFile);
        if (cached == null || !cacheSource.equals(cached.source())) {
            throw new IllegalStateException("Qin OVS batch compilation did not produce requested module: "
                    + moduleFile.toAbsolutePath());
        }
        return cached.transpiled();
    }

    private List<Path> ovsGraphFilesForOnDemandBatch(Path requestedModuleFile) {
        LinkedHashSet<Path> files = new LinkedHashSet<>();
        for (QinModuleSource module : graph.modules()) {
            Path file = module.file().toAbsolutePath().normalize();
            if (isFrontendModuleFile(file) && isOvsModuleFile(file)) {
                files.add(file);
            }
        }
        files.add(requestedModuleFile.toAbsolutePath().normalize());
        return new ArrayList<>(files);
    }

    private String finishOvsModule(
            Path moduleFile,
            QinModuleSource sourceModule,
            QinOvsCompiler.QinOvsCompileResult result) {
        String compiled = result.code();
        compiled = rewriteSpecifiers(sourceModule, compiled, IMPORT_FROM_PATTERN);
        compiled = rewriteSpecifiers(sourceModule, compiled, EXPORT_FROM_PATTERN);
        compiled = rewriteSpecifiers(sourceModule, compiled, IMPORT_SIDE_EFFECT_PATTERN);
        result = ensureOvsCsstsArtifacts(result, compiled);
        registerOvsVirtualModules(moduleFile, result);
        return mountOvsModule(moduleFile, compiled);
    }

    private QinOvsCompiler.QinOvsCompileResult ensureOvsCsstsArtifacts(
            QinOvsCompiler.QinOvsCompileResult result,
            String compiled) {
        Set<String> atomNames = extractCsstsAtomNamesFromCode(compiled);
        if (atomNames.isEmpty()) {
            return result;
        }
        String css = result.css();
        String atomModule = result.atomModule();
        if (!containsAllCsstsAtomEntries(atomModule, atomNames) || !containsAllCsstsCssRules(css, atomNames)) {
            try {
                QinCsstsCompiler.QinCsstsCompileResult csstsResult = csstsCompiler.compile(
                        projectRoot,
                        synthesizeCsstsSource(atomNames));
                if (css == null || css.isBlank() || !containsAllCsstsCssRules(css, atomNames)) {
                    css = csstsResult.css();
                }
                if (atomModule == null || atomModule.isBlank() || !containsAllCsstsAtomEntries(atomModule, atomNames)) {
                    atomModule = csstsResult.atomModule();
                }
            } catch (Exception error) {
                if (!containsAllCsstsAtomEntries(atomModule, atomNames)) {
                    atomModule = synthesizeCsstsAtomModule(atomNames);
                }
            }
        }
        return new QinOvsCompiler.QinOvsCompileResult(
                result.code(),
                result.hasStyles(),
                css == null ? "" : css,
                atomModule == null ? "" : atomModule);
    }

    private String transpileCsstsModule(Path moduleFile, String source) {
        QinModuleSource module = moduleSourceMap.get(moduleFile.toAbsolutePath().normalize());
        QinModuleSource sourceModule = module != null
                ? module
                : new QinModuleSource(moduleFile.toAbsolutePath().normalize(), source, List.of());
        QinCsstsCompiler.QinCsstsCompileResult result;
        try {
            result = csstsCompiler.compile(projectRoot, source);
        } catch (Exception error) {
            throw new IllegalStateException("Qin CSSTS compilation failed for " + moduleFile.toAbsolutePath(), error);
        }

        registerCsstsVirtualModules(moduleFile, result);
        String compiled = result.code();
        compiled = rewriteSpecifiers(sourceModule, compiled, IMPORT_FROM_PATTERN);
        compiled = rewriteSpecifiers(sourceModule, compiled, EXPORT_FROM_PATTERN);
        compiled = rewriteSpecifiers(sourceModule, compiled, IMPORT_SIDE_EFFECT_PATTERN);
        return mountCsstsModule(compiled);
    }

    private String mountCsstsModule(String source) {
        String body = source == null ? "" : source;
        StringBuilder prelude = new StringBuilder();
        if (!importsCsstsRuntimeBinding(body)) {
            prelude.append("import { cssts } from \"")
                    .append(CSSTS_RUNTIME_VIRTUAL_MODULE_URL)
                    .append("\";\n");
        }
        if (!importsCsstsAtomBinding(body)) {
            prelude.append("import { csstsAtom } from \"")
                    .append(CSSTS_ATOM_VIRTUAL_MODULE_URL)
                    .append("\";\n");
        }
        if (!importsCsstsStyleModule(body)) {
            prelude.append("import \"")
                    .append(CSSTS_STYLE_VIRTUAL_MODULE_URL)
                    .append("\";\n");
        }
        return prelude.append(body).toString();
    }

    private boolean importsCsstsRuntimeBinding(String source) {
        return Pattern.compile("(?m)^\\s*import\\s+(?:\\*\\s+as\\s+cssts|\\{[^}\\n]*\\bcssts\\b[^}\\n]*})\\s+from\\s*[\"']"
                        + Pattern.quote(CSSTS_RUNTIME_VIRTUAL_MODULE_URL)
                        + "[\"']")
                .matcher(source == null ? "" : source)
                .find();
    }

    private boolean importsCsstsAtomBinding(String source) {
        return Pattern.compile("(?m)^\\s*import\\s+\\{[^}\\n]*\\bcsstsAtom\\b[^}\\n]*}\\s+from\\s*[\"']"
                        + Pattern.quote(CSSTS_ATOM_VIRTUAL_MODULE_URL)
                        + "[\"']")
                .matcher(source == null ? "" : source)
                .find();
    }

    private boolean importsCsstsStyleModule(String source) {
        return Pattern.compile("(?m)^\\s*import\\s*[\"']"
                        + Pattern.quote(CSSTS_STYLE_VIRTUAL_MODULE_URL)
                        + "[\"']")
                .matcher(source == null ? "" : source)
                .find();
    }

    private String joinScriptBlocks(Object scriptBlock, Object scriptSetupBlock) {
        StringBuilder sb = new StringBuilder();
        String script = extractBlockContent(scriptBlock);
        String scriptSetup = extractBlockContent(scriptSetupBlock);
        if (!script.isBlank()) {
            sb.append(script.trim()).append('\n');
        }
        if (!scriptSetup.isBlank()) {
            sb.append(scriptSetup.trim()).append('\n');
        }
        return sb.toString().trim();
    }

    private String joinStyleBlocks(Object styles) {
        if (!(styles instanceof List<?> list) || list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object styleBlock : list) {
            String content = extractBlockContent(styleBlock);
            if (content.isBlank()) {
                continue;
            }
            sb.append(content.trim()).append('\n');
        }
        return sb.toString().trim();
    }

    private String extractBlockContent(Object block) {
        if (!(block instanceof Map<?, ?> map)) {
            return "";
        }
        Object content = map.get("content");
        return content instanceof String text ? text : "";
    }

    private String styleInjection(String styleSource) {
        String escaped = escapeJsString(styleSource);
        return """
                (function __qinInjectVueStyle() {
                  if (typeof document === 'undefined') return;
                  const style = document.createElement('style');
                  style.setAttribute('data-qin-vue', 'true');
                  style.textContent = "%s";
                  document.head.appendChild(style);
                })();
                """.formatted(escaped);
    }

    private String templateToRenderFunctionBody(String templateSource) {
        String template = templateSource == null ? "" : templateSource;
        template = renderStage1ComponentPlaceholders(template);
        String escaped = escapeTemplateLiteral(template);
        String rendered = escaped.replaceAll("\\{\\{\\s*([^}]+?)\\s*\\}\\}", "\\${__qinEscapeHtml(($1))}");
        return """
                function __qinEscapeHtml(value) {
                  const text = value == null ? '' : String(value);
                  return text
                    .replace(/&/g, '&amp;')
                    .replace(/</g, '&lt;')
                    .replace(/>/g, '&gt;')
                    .replace(/\"/g, '&quot;')
                    .replace(/'/g, '&#39;');
                }
                function __qinRenderVueTemplate() {
                  return `%s`;
                }
                """.formatted(rendered);
    }

    private String renderStage1ComponentPlaceholders(String template) {
        return template.replaceAll(
                "<\\s*([A-Z][A-Za-z0-9_$]*)\\s*/\\s*>",
                "<section data-qin-component=\"$1\"></section>");
    }

    private String escapeTemplateLiteral(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("\\", "\\\\")
                .replace("`", "\\`")
                .replace("${", "\\${");
    }

    private String escapeJsString(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("\\", "\\\\")
                .replace("`", "\\`")
                .replace("${", "\\${")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

    private String rewriteSpecifiers(QinModuleSource module, String source, Pattern pattern) {
        Matcher matcher = pattern.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String prefix = matcher.group(1);
            String specifier = matcher.group(2);
            String suffix = matcher.group(3);
            String rewritten = rewriteSpecifier(module, specifier);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(prefix + rewritten + suffix));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String rewriteSpecifier(QinModuleSource module, String specifier) {
        if (specifier == null || specifier.isBlank()) {
            return specifier;
        }
        if (specifier.startsWith("java:")) {
            throw new IllegalArgumentException("QIN1001 frontend cannot import java modules: " + specifier);
        }
        if ("virtual:cssts.css".equals(specifier)) {
            return CSSTS_STYLE_VIRTUAL_MODULE_URL;
        }
        if ("virtual:csstsAtom".equals(specifier)) {
            return CSSTS_ATOM_VIRTUAL_MODULE_URL;
        }
        if ("cssts-ts".equals(specifier)) {
            return CSSTS_RUNTIME_VIRTUAL_MODULE_URL;
        }
        if ("qin".equals(specifier)) {
            virtualModuleContentMap.putIfAbsent(QIN_FRONTEND_RUNTIME_VIRTUAL_MODULE_URL, qinFrontendRuntimeModule());
            return QIN_FRONTEND_RUNTIME_VIRTUAL_MODULE_URL;
        }
        if ("\0plugin-vue:export-helper".equals(specifier)
                || "plugin-vue:export-helper".equals(specifier)
                || specifier.contains("plugin-vue:export-helper")) {
            String requestPath = "/@qin/plugin-vue-export-helper.js";
            virtualModuleContentMap.putIfAbsent(requestPath, readPluginVueExportHelperModule());
            return requestPath;
        }
        if ("ovsjs".equals(specifier)) {
            return toModuleUrl(projectRoot, module.file().toAbsolutePath().normalize()) + "?qin-ovs=runtime";
        }
        if ("vue".equals(specifier)) {
            String requestPath = "/@qin-mod/qin-vue-runtime.js?qin-vue=runtime";
            registerVueRuntimeVirtualModules(requestPath);
            return requestPath;
        }
        String vueQuerySpecifier = rewriteVuePluginQuerySpecifier(specifier);
        if (vueQuerySpecifier != null) {
            return vueQuerySpecifier;
        }

        QinResolvedImport resolved = findResolvedImport(module, specifier);
        if (resolved != null && resolved.resolvedModule() != null) {
            Path resolvedFile = resolved.resolvedModule().toAbsolutePath().normalize();
            String url = moduleUrlMap.get(resolvedFile);
            if (url != null) {
                return url;
            }
        }
        return specifier;
    }

    private boolean isUnifiedAppEntry(Path moduleFile, String source) {
        Path normalized = moduleFile.toAbsolutePath().normalize();
        return normalized.equals(projectRoot.resolve("src/app.qin").toAbsolutePath().normalize())
                && source != null
                && source.contains("export object App")
                && source.contains("@WebRoot");
    }

    private String frontendSourceFromUnifiedAppEntry(String source) {
        Matcher matcher = QIN_APP_OBJECT_START_PATTERN.matcher(source);
        if (!matcher.find()) {
            return source;
        }
        int objectStart = matcher.start();
        int bodyOpen = matcher.end() - 1;
        int objectEnd = findMatchingBrace(source, bodyOpen);
        if (objectEnd < 0) {
            throw new IllegalArgumentException("Unclosed Qin App object in src/app.qin");
        }
        return source.substring(0, objectStart) + source.substring(objectEnd + 1);
    }

    private int findMatchingBrace(String source, int openBrace) {
        int depth = 0;
        boolean single = false;
        boolean dbl = false;
        boolean template = false;
        boolean lineComment = false;
        boolean blockComment = false;
        for (int i = openBrace; i < source.length(); i++) {
            char ch = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : '\0';
            char previous = i > 0 ? source.charAt(i - 1) : '\0';
            if (lineComment) {
                if (ch == '\n') {
                    lineComment = false;
                }
                continue;
            }
            if (blockComment) {
                if (ch == '*' && next == '/') {
                    blockComment = false;
                    i++;
                }
                continue;
            }
            if (single) {
                if (ch == '\'' && previous != '\\') {
                    single = false;
                }
                continue;
            }
            if (dbl) {
                if (ch == '"' && previous != '\\') {
                    dbl = false;
                }
                continue;
            }
            if (template) {
                if (ch == '`' && previous != '\\') {
                    template = false;
                }
                continue;
            }
            if (ch == '/' && next == '/') {
                lineComment = true;
                i++;
                continue;
            }
            if (ch == '/' && next == '*') {
                blockComment = true;
                i++;
                continue;
            }
            if (ch == '\'') {
                single = true;
                continue;
            }
            if (ch == '"') {
                dbl = true;
                continue;
            }
            if (ch == '`') {
                template = true;
                continue;
            }
            if (ch == '{') {
                depth++;
            } else if (ch == '}') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private String qinFrontendRuntimeModule() {
        return """
                export function WebRoot() {
                  return () => undefined;
                }
                export function Frontend(target) {
                  return target;
                }
                export function Backend(target) {
                  return target;
                }
                export function Controller(target) {
                  return target;
                }
                export const RestController = Controller;
                export function RequestMapping() {
                  return target => target;
                }
                export function GetMapping() {
                  return (_target, _propertyKey, descriptor) => descriptor;
                }
                export const PostMapping = GetMapping;
                export const DeleteMapping = GetMapping;
                """;
    }

    private String rewriteVuePluginQuerySpecifier(String specifier) {
        if (specifier == null || !specifier.contains("?") || !specifier.contains("vue")) {
            return null;
        }
        int queryIndex = specifier.indexOf('?');
        String path = specifier.substring(0, queryIndex);
        String query = specifier.substring(queryIndex + 1);
        if (!query.contains("type=")) {
            return null;
        }
        Path resolvedPath;
        try {
            resolvedPath = Path.of(path).toAbsolutePath().normalize();
        } catch (Exception ignored) {
            return null;
        }
        String moduleUrl = moduleUrlMap.get(resolvedPath);
        if (moduleUrl == null) {
            return null;
        }
        return moduleUrl + "?" + query;
    }

    private QinResolvedImport findResolvedImport(QinModuleSource module, String specifier) {
        for (QinResolvedImport resolvedImport : module.imports()) {
            QinImportDescriptor descriptor = resolvedImport.descriptor();
            if (specifier.equals(descriptor.moduleSpecifier())) {
                return resolvedImport;
            }
        }
        return null;
    }

    private Path resolveRequestToModuleFile(String requestPath) {
        if (requestPath == null) {
            return null;
        }
        return requestPathMap.get(requestPath);
    }

    private String resolveVirtualModuleContent(String requestPath) {
        if (requestPath == null) {
            return null;
        }
        if (CSSTS_STYLE_VIRTUAL_MODULE_URL.equals(requestPath)
                || CSSTS_ATOM_VIRTUAL_MODULE_URL.equals(requestPath)
                || CSSTS_RUNTIME_VIRTUAL_MODULE_URL.equals(requestPath)) {
            refreshCsstsGlobalVirtualModules();
        }
        return virtualModuleContentMap.get(requestPath);
    }

    private static String readSource(Path file) {
        try {
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (IOException error) {
            throw new IllegalStateException("Failed to read frontend module: " + file.toAbsolutePath(), error);
        }
    }

    private static String stripQinHmrQuery(String requestPath) {
        if (requestPath == null) {
            return null;
        }
        int queryIndex = requestPath.indexOf('?');
        if (queryIndex < 0) {
            return requestPath;
        }
        String path = requestPath.substring(0, queryIndex);
        String query = requestPath.substring(queryIndex + 1);
        StringBuilder kept = new StringBuilder();
        for (String part : query.split("&")) {
            if (part.isBlank()) {
                continue;
            }
            String key = part;
            int equalsIndex = part.indexOf('=');
            if (equalsIndex >= 0) {
                key = part.substring(0, equalsIndex);
            }
            if ("qin-hmr".equals(key)) {
                continue;
            }
            if (!kept.isEmpty()) {
                kept.append('&');
            }
            kept.append(part);
        }
        if (kept.isEmpty()) {
            return path;
        }
        return path + "?" + kept;
    }

    private void registerVueVirtualModules(Path moduleFile, QinVueSfcModuleResult result) {
        if (result == null) {
            return;
        }
        String css = result.csstsCss();
        String atom = result.csstsAtomModule();
        if ((css == null || css.isBlank()) && (atom == null || atom.isBlank())) {
            return;
        }

        String base = toModuleUrl(projectRoot, moduleFile);
        String cssRequestPath = base + "?qin-vue-cssts=style";
        String atomRequestPath = base + "?qin-vue-cssts=atom";
        String runtimeRequestPath = base + "?qin-vue-cssts=runtime";

        virtualModuleContentMap.put(cssRequestPath, renderCssInjectionModule(css == null ? "" : css));
        virtualModuleContentMap.put(atomRequestPath, isBlank(atom) ? emptyCsstsAtomModule() : atom);
        virtualModuleContentMap.put(runtimeRequestPath, readCsstsRuntimeModule());
        registerCsstsGlobalVirtualModules(moduleFile, css, atom);
    }

    private void registerOvsVirtualModules(Path moduleFile, QinOvsCompiler.QinOvsCompileResult result) {
        String base = toModuleUrl(projectRoot, moduleFile);
        String cssRequestPath = base + "?qin-vue-cssts=style";
        String atomRequestPath = base + "?qin-vue-cssts=atom";
        String runtimeRequestPath = base + "?qin-vue-cssts=runtime";
        String ovsRuntimeRequestPath = base + "?qin-ovs=runtime";
        String vueRuntimeRequestPath = "/@qin-mod/qin-vue-runtime.js?qin-vue=runtime";

        virtualModuleContentMap.put(cssRequestPath, renderCssInjectionModule(result.css() == null ? "" : result.css()));
        virtualModuleContentMap.put(
                atomRequestPath,
                isBlank(result.atomModule()) ? emptyCsstsAtomModule() : result.atomModule());
        virtualModuleContentMap.put(runtimeRequestPath, readCsstsRuntimeModule());
        virtualModuleContentMap.put(ovsRuntimeRequestPath, readOvsRuntimeModule(vueRuntimeRequestPath));
        registerVueRuntimeVirtualModules(vueRuntimeRequestPath);
        registerCsstsGlobalVirtualModules(moduleFile, result.css(), result.atomModule());
    }

    private void registerCsstsVirtualModules(Path moduleFile, QinCsstsCompiler.QinCsstsCompileResult result) {
        String base = toModuleUrl(projectRoot, moduleFile);
        String cssRequestPath = base + "?qin-vue-cssts=style";
        String atomRequestPath = base + "?qin-vue-cssts=atom";
        String runtimeRequestPath = base + "?qin-vue-cssts=runtime";
        virtualModuleContentMap.put(cssRequestPath, renderCssInjectionModule(result.css() == null ? "" : result.css()));
        virtualModuleContentMap.put(
                atomRequestPath,
                isBlank(result.atomModule()) ? emptyCsstsAtomModule() : result.atomModule());
        virtualModuleContentMap.put(runtimeRequestPath, readCsstsRuntimeModule());
        registerCsstsGlobalVirtualModules(moduleFile, result.css(), result.atomModule());
    }

    private void registerCsstsGlobalVirtualModules(Path moduleFile, String css, String atomModule) {
        Path key = moduleFile.toAbsolutePath().normalize();
        if (css != null && !css.isBlank()) {
            csstsCssByModule.put(key, css);
        } else {
            csstsCssByModule.remove(key);
        }
        if (atomModule != null && !atomModule.isBlank()) {
            csstsAtomByModule.put(key, atomModule);
        } else {
            csstsAtomByModule.remove(key);
        }
        refreshCsstsGlobalVirtualModules();
    }

    private void refreshCsstsGlobalVirtualModules() {
        virtualModuleContentMap.put(CSSTS_STYLE_VIRTUAL_MODULE_URL, renderCssInjectionModule(mergeCsstsCss()));
        virtualModuleContentMap.put(CSSTS_ATOM_VIRTUAL_MODULE_URL, mergeCsstsAtomModules());
        virtualModuleContentMap.put(CSSTS_RUNTIME_VIRTUAL_MODULE_URL, readCsstsRuntimeModule());
    }

    private String mergeCsstsCss() {
        Set<String> cssBlocks = new LinkedHashSet<>();
        for (String css : csstsCssByModule.values()) {
            if (css != null && !css.isBlank()) {
                cssBlocks.add(css.trim());
            }
        }
        return String.join(System.lineSeparator() + System.lineSeparator(), cssBlocks);
    }

    private String mergeCsstsAtomModules() {
        Map<String, String> entriesByName = new LinkedHashMap<>();
        for (String atomModule : csstsAtomByModule.values()) {
            for (String entry : extractCsstsAtomEntries(atomModule)) {
                int colon = entry.indexOf(':');
                if (colon <= 0) {
                    continue;
                }
                String name = entry.substring(0, colon).trim();
                if (!name.isBlank()) {
                    entriesByName.put(name, entry);
                }
            }
        }

        StringBuilder module = new StringBuilder();
        module.append("// Auto-generated by Qin from virtual:csstsAtom").append(System.lineSeparator());
        module.append("export const csstsAtom = {").append(System.lineSeparator());
        int index = 0;
        for (String entry : entriesByName.values()) {
            module.append("  ").append(entry);
            if (++index < entriesByName.size()) {
                module.append(',');
            }
            module.append(System.lineSeparator());
        }
        module.append("}").append(System.lineSeparator());
        module.append("export default csstsAtom").append(System.lineSeparator());
        return module.toString();
    }

    private List<String> extractCsstsAtomEntries(String atomModule) {
        if (atomModule == null || atomModule.isBlank()) {
            return List.of();
        }
        List<String> entries = new ArrayList<>();
        boolean inside = false;
        for (String line : atomModule.split("\\R")) {
            String trimmed = line.trim();
            if (!inside) {
                if (trimmed.startsWith("export const csstsAtom") && trimmed.contains("{")) {
                    inside = true;
                }
                continue;
            }
            if (trimmed.equals("}") || trimmed.equals("};")) {
                break;
            }
            if (trimmed.isBlank()) {
                continue;
            }
            if (trimmed.endsWith(",")) {
                trimmed = trimmed.substring(0, trimmed.length() - 1).trim();
            }
            if (!trimmed.isBlank()) {
                entries.add(trimmed);
            }
        }
        return entries;
    }

    private Set<String> extractCsstsAtomNamesFromCode(String code) {
        LinkedHashSet<String> names = new LinkedHashSet<>();
        if (code == null || code.isBlank()) {
            return names;
        }
        Matcher matcher = CSSTS_MERGE_PATTERN.matcher(code);
        while (matcher.find()) {
            for (String part : matcher.group(1).split(",")) {
                String name = part.trim();
                if (name.matches("[A-Za-z_$][\\w$]*")) {
                    names.add(name);
                }
            }
        }
        return names;
    }

    private boolean containsAllCsstsAtomEntries(String atomModule, Set<String> atomNames) {
        if (atomNames == null || atomNames.isEmpty()) {
            return true;
        }
        String source = atomModule == null ? "" : atomModule;
        for (String atomName : atomNames) {
            if (!source.contains(atomName + ":")) {
                return false;
            }
        }
        return true;
    }

    private boolean containsAllCsstsCssRules(String css, Set<String> atomNames) {
        if (atomNames == null || atomNames.isEmpty()) {
            return true;
        }
        String source = css == null ? "" : css;
        for (String atomName : atomNames) {
            if (!source.contains(".cssts_" + camelToSnakeAtom(atomName))) {
                return false;
            }
        }
        return true;
    }

    private String synthesizeCsstsSource(Set<String> atomNames) {
        return "const __qinOvsStyle = css { " + String.join(", ", atomNames) + " }";
    }

    private String synthesizeCsstsAtomModule(Set<String> atomNames) {
        StringBuilder builder = new StringBuilder();
        builder.append("// Auto-generated by Qin from OVS CSSTS output").append(System.lineSeparator());
        builder.append("export const csstsAtom = {").append(System.lineSeparator());
        int index = 0;
        for (String atomName : atomNames) {
            builder.append("  ")
                    .append(atomName)
                    .append(": { '")
                    .append("cssts_")
                    .append(camelToSnakeAtom(atomName))
                    .append("': null }");
            if (++index < atomNames.size()) {
                builder.append(',');
            }
            builder.append(System.lineSeparator());
        }
        builder.append("}").append(System.lineSeparator());
        builder.append("export default csstsAtom").append(System.lineSeparator());
        return builder.toString();
    }

    private String camelToSnakeAtom(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isUpperCase(ch) && i > 0) {
                builder.append('_');
            }
            builder.append(Character.toLowerCase(ch));
        }
        return builder.toString()
                .replace("_px", "px")
                .replace("_rem", "rem")
                .replace("_em", "em")
                .replace("_vh", "vh")
                .replace("_vw", "vw");
    }

    private String mountOvsModule(Path moduleFile, String source) {
        String vueRuntime = "/@qin-mod/qin-vue-runtime.js?qin-vue=runtime";
        String vueCreateAppRuntime = "/@qin-mod/qin-vue-runtime.js?qin-vue=browser-runtime";
        String csstsStyle = toModuleUrl(projectRoot, moduleFile) + "?qin-vue-cssts=style";
        String csstsRuntime = toModuleUrl(projectRoot, moduleFile) + "?qin-vue-cssts=runtime";
        String csstsAtom = toModuleUrl(projectRoot, moduleFile) + "?qin-vue-cssts=atom";
        Set<String> atomNames = extractCsstsAtomNamesFromCode(source);
        String atomPrelude = atomNames.isEmpty()
                ? ""
                : "const { " + String.join(", ", atomNames) + " } = __qinOvsCsstsAtom;\n";
        String ovsRuntimePrelude = missingOvsRuntimeImportPrelude(moduleFile, source);
        String marker = "export default ";
        int exportIndex = source.indexOf(marker);
        if (exportIndex < 0) {
            String implicitModule = mountImplicitDefaultOvsModule(
                    moduleFile,
                    source,
                    vueRuntime,
                    csstsStyle,
                    csstsRuntime,
                    csstsAtom,
                    atomPrelude,
                    ovsRuntimePrelude);
            if (implicitModule != null) {
                return implicitModule;
            }
            return """
                    import "%s";
                    import * as cssts from "%s";
                    import { csstsAtom as __qinOvsCsstsAtom } from "%s";
                    %s
                    %s
                    %s
                    """.formatted(csstsStyle, csstsRuntime, csstsAtom, atomPrelude, ovsRuntimePrelude, source);
        }
        String transformed = source.substring(0, exportIndex)
                + "const __qinOvsComponent = "
                + source.substring(exportIndex + marker.length());
        return """
                import { createApp as __qinCreateApp } from "%s";
                import "%s";
                import * as cssts from "%s";
                import { csstsAtom as __qinOvsCsstsAtom } from "%s";
                %s
                %s
                %s
                const __qinVueComponent = __qinOvsComponent && __qinOvsComponent.__vueComponent
                  ? __qinOvsComponent.__vueComponent
                  : __qinOvsComponent;
                function __qinMountOvs(target = null) {
                  if (typeof document === 'undefined') return null;
                  const __qinOvsTarget = typeof target === 'string'
                    ? document.querySelector(target)
                    : (target || document.querySelector('[data-qin-component]') || document.querySelector('#ovs-demo'));
                  if (!__qinOvsTarget) return null;
                  __qinOvsTarget.innerHTML = '';
                  return __qinCreateApp(__qinVueComponent).mount(__qinOvsTarget);
                }
                function __qinMountVue(target) {
                  return __qinMountOvs(target);
                }
                __qinVueComponent.component = __qinOvsComponent;
                __qinVueComponent.__qinMountVue = __qinMountVue;
                __qinVueComponent.__qinMountOvs = __qinMountOvs;
                if (typeof document !== 'undefined'
                    && (document.querySelector('[data-qin-component]') || document.querySelector('#ovs-demo'))) {
                  setTimeout(__qinMountOvs, 0);
                }
                export { __qinMountOvs, __qinMountVue };
                export default __qinVueComponent;
                """.formatted(vueCreateAppRuntime, csstsStyle, csstsRuntime, csstsAtom, atomPrelude, ovsRuntimePrelude, transformed);
    }

    private String mountImplicitDefaultOvsModule(
            Path moduleFile,
            String source,
            String vueRuntime,
            String csstsStyle,
            String csstsRuntime,
            String csstsAtom,
            String atomPrelude,
            String ovsRuntimePrelude) {
        if (containsTopLevelExport(source)) {
            return null;
        }
        StatementRange expression = findLastTopLevelExpressionStatement(source);
        if (expression == null) {
            return null;
        }
        String before = source.substring(0, expression.start());
        String after = source.substring(expression.end());
        String expressionSource = trimTrailingSemicolon(source.substring(expression.start(), expression.end()).trim());
        String ovsRuntime = toModuleUrl(projectRoot, moduleFile) + "?qin-ovs=runtime";
        String vueCreateAppRuntime = "/@qin-mod/qin-vue-runtime.js?qin-vue=browser-runtime";
        return """
                import { createApp as __qinCreateApp } from "%s";
                import "%s";
                import * as cssts from "%s";
                import { csstsAtom as __qinOvsCsstsAtom } from "%s";
                import { defineOvsComponent as __qinDefineOvsComponent } from "%s";
                %s
                %s
                %s%s
                const __qinOvsComponent = __qinDefineOvsComponent(() => {
                  return (%s);
                });
                const __qinVueComponent = __qinOvsComponent && __qinOvsComponent.__vueComponent
                  ? __qinOvsComponent.__vueComponent
                  : __qinOvsComponent;
                function __qinMountOvs(target = null) {
                  if (typeof document === 'undefined') return null;
                  const __qinOvsTarget = typeof target === 'string'
                    ? document.querySelector(target)
                    : (target || document.querySelector('[data-qin-component]') || document.querySelector('#ovs-demo'));
                  if (!__qinOvsTarget) return null;
                  __qinOvsTarget.innerHTML = '';
                  return __qinCreateApp(__qinVueComponent).mount(__qinOvsTarget);
                }
                function __qinMountVue(target) {
                  return __qinMountOvs(target);
                }
                __qinVueComponent.component = __qinOvsComponent;
                __qinVueComponent.__qinMountVue = __qinMountVue;
                __qinVueComponent.__qinMountOvs = __qinMountOvs;
                if (typeof document !== 'undefined'
                    && (document.querySelector('[data-qin-component]') || document.querySelector('#ovs-demo'))) {
                  setTimeout(__qinMountOvs, 0);
                }
                export { __qinMountOvs, __qinMountVue };
                export default __qinVueComponent;
                """.formatted(
                vueCreateAppRuntime,
                csstsStyle,
                csstsRuntime,
                csstsAtom,
                ovsRuntime,
                atomPrelude,
                ovsRuntimePrelude,
                before,
                after,
                expressionSource);
    }

    private String missingOvsRuntimeImportPrelude(Path moduleFile, String source) {
        String runtimeUrl = toModuleUrl(projectRoot, moduleFile) + "?qin-ovs=runtime";
        List<String> names = new ArrayList<>();
        for (String name : List.of("$OvsHtmlTag", "defineOvsComponent", "defineReactiveExpression")) {
            if (source.contains(name) && !hasImportedBinding(source, name)) {
                names.add(name);
            }
        }
        return names.isEmpty()
                ? ""
                : "import { " + String.join(", ", names) + " } from \""
                        + runtimeUrl
                        + "\";\n";
    }

    private boolean hasImportedBinding(String source, String name) {
        Pattern pattern = Pattern.compile("\\bimport\\s*\\{[^}]*"
                + Pattern.quote(name)
                + "(?:\\s+as\\s+[A-Za-z_$][\\w$]*)?[^}]*}\\s*from\\s*[\"'][^\"']+[\"']");
        return pattern.matcher(source).find();
    }

    private boolean containsTopLevelExport(String source) {
        Matcher matcher = Pattern.compile("(?m)^\\s*export\\s+").matcher(source);
        return matcher.find();
    }

    private StatementRange findLastTopLevelExpressionStatement(String source) {
        List<Integer> semicolons = topLevelSemicolonPositions(source);
        for (int i = semicolons.size() - 1; i >= 0; i--) {
            int end = semicolons.get(i) + 1;
            int start = i == 0 ? findFirstNonImportStatementStart(source) : semicolons.get(i - 1) + 1;
            while (start < end && Character.isWhitespace(source.charAt(start))) {
                start++;
            }
            if (start >= end) {
                continue;
            }
            String candidate = source.substring(start, end).trim();
            if (looksLikeExpressionStatement(candidate)) {
                return new StatementRange(start, end);
            }
        }
        return null;
    }

    private List<Integer> topLevelSemicolonPositions(String source) {
        List<Integer> positions = new ArrayList<>();
        int parenDepth = 0;
        int bracketDepth = 0;
        int braceDepth = 0;
        char quote = 0;
        boolean escaping = false;
        boolean lineComment = false;
        boolean blockComment = false;
        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : 0;
            if (lineComment) {
                if (ch == '\n' || ch == '\r') {
                    lineComment = false;
                }
                continue;
            }
            if (blockComment) {
                if (ch == '*' && next == '/') {
                    blockComment = false;
                    i++;
                }
                continue;
            }
            if (quote != 0) {
                if (escaping) {
                    escaping = false;
                } else if (ch == '\\') {
                    escaping = true;
                } else if (ch == quote) {
                    quote = 0;
                }
                continue;
            }
            if (ch == '/' && next == '/') {
                lineComment = true;
                i++;
                continue;
            }
            if (ch == '/' && next == '*') {
                blockComment = true;
                i++;
                continue;
            }
            if (ch == '"' || ch == '\'' || ch == '`') {
                quote = ch;
                continue;
            }
            if (ch == '(') {
                parenDepth++;
            } else if (ch == ')' && parenDepth > 0) {
                parenDepth--;
            } else if (ch == '[') {
                bracketDepth++;
            } else if (ch == ']' && bracketDepth > 0) {
                bracketDepth--;
            } else if (ch == '{') {
                braceDepth++;
            } else if (ch == '}' && braceDepth > 0) {
                braceDepth--;
            } else if (ch == ';' && parenDepth == 0 && bracketDepth == 0 && braceDepth == 0) {
                positions.add(i);
            }
        }
        return positions;
    }

    private int findFirstNonImportStatementStart(String source) {
        int index = 0;
        while (index < source.length()) {
            while (index < source.length() && Character.isWhitespace(source.charAt(index))) {
                index++;
            }
            if (!source.startsWith("import ", index)
                    && !source.startsWith("import{", index)
                    && !source.startsWith("import*", index)
                    && !source.startsWith("import\"", index)
                    && !source.startsWith("import'", index)) {
                return index;
            }
            int nextLine = source.indexOf('\n', index);
            if (nextLine < 0) {
                return source.length();
            }
            index = nextLine + 1;
        }
        return index;
    }

    private boolean looksLikeExpressionStatement(String statement) {
        String text = trimTrailingSemicolon(statement).trim();
        if (text.isEmpty()) {
            return false;
        }
        for (String keyword : List.of(
                "import ", "export ", "const ", "let ", "var ", "function ", "class ",
                "if ", "for ", "while ", "switch ", "try ", "return ", "throw ")) {
            if (text.startsWith(keyword)) {
                return false;
            }
        }
        return true;
    }

    private String trimTrailingSemicolon(String text) {
        String result = text.stripTrailing();
        if (result.endsWith(";")) {
            return result.substring(0, result.length() - 1).stripTrailing();
        }
        return result;
    }

    private record StatementRange(int start, int end) {
    }

    private String readOvsRuntimeModule(String vueRuntimeRequestPath) {
        Path runtimeModule = resolveOvsRuntimeModule();
        if (!Files.exists(runtimeModule) || !Files.isRegularFile(runtimeModule)) {
            return minimalOvsRuntimeModule(vueRuntimeRequestPath);
        }
        try {
            String source = Files.readString(runtimeModule, StandardCharsets.UTF_8);
            return source
                    .replace("from \"vue\"", "from \"" + vueRuntimeRequestPath + "\"")
                    .replace("from 'vue'", "from '" + vueRuntimeRequestPath + "'");
        } catch (IOException error) {
            throw new IllegalStateException("Failed to read ovsjs browser runtime module: " + runtimeModule, error);
        }
    }

    private String minimalOvsRuntimeModule(String vueRuntimeRequestPath) {
        return """
                import { h } from "%s";
                export function defineReactiveExpression(value) {
                  return typeof value === 'function' ? value() : value;
                }
                export function createElementVNode(tag, props, children) {
                  return h(tag, props || {}, children || []);
                }
                export function createComponentVNodeNew(component, props, children) {
                  return h(component, props || {}, children || []);
                }
                export function mapChildrenToVNodes(children) {
                  return Array.isArray(children) ? children : [children];
                }
                export function defineOvsComponent(render) {
                  return {
                    name: 'QinOvsComponent',
                    render() {
                      return render({});
                    }
                  };
                }
                export const $OvsHtmlTag = new Proxy({}, {
                  get(_target, tag) {
                    return (props, children) => h(String(tag), props || {}, children || []);
                  }
                });
                """.formatted(vueRuntimeRequestPath);
    }

    private Path resolveOvsRuntimeModule() {
        return resolvePackageRuntimeModule(
                "ovsjs",
                List.of("dist/index.mjs", "src/index.js", "src/index.ts"),
                "dist/index.mjs");
    }

    private String readVueBrowserRuntimeModule() {
        Path runtimeModule = resolveVueBrowserRuntimeModule();
        if (!Files.exists(runtimeModule) || !Files.isRegularFile(runtimeModule)) {
            return minimalVueBrowserRuntimeModule();
        }
        try {
            return Files.readString(runtimeModule, StandardCharsets.UTF_8);
        } catch (IOException error) {
            throw new IllegalStateException("Failed to read Vue browser runtime module: " + runtimeModule, error);
        }
    }

    private Path resolveVueBrowserRuntimeModule() {
        List<Path> candidates = List.of(
                projectRoot.resolve(".qin").resolve("runtime").resolve("npm-host").resolve("node_modules"),
                projectRoot.resolve("node_modules"));
        for (Path nodeModules : candidates) {
            for (String entry : List.of("dist/vue.esm-browser.js", "dist/vue.runtime.esm-browser.js")) {
                Path runtimeModule = nodeModules
                        .resolve("vue")
                        .resolve(entry)
                        .toAbsolutePath()
                        .normalize();
                if (Files.exists(runtimeModule) && Files.isRegularFile(runtimeModule)) {
                    return runtimeModule;
                }
            }
        }
        return candidates.get(candidates.size() - 1)
                .resolve("vue")
                .resolve("dist")
                .resolve("vue.esm-browser.js")
                .toAbsolutePath()
                .normalize();
    }

    private String readPluginVueExportHelperModule() {
        return """
                export default function _export_sfc(component, props) {
                  for (const [key, value] of props || []) {
                    component[key] = value;
                  }
                  return component;
                }
                """;
    }

    private String minimalVueBrowserRuntimeModule() {
        return """
                export function ref(value) {
                  return { value };
                }

                export function toDisplayString(value) {
                  if (value && typeof value === 'object' && 'value' in value) {
                    return String(value.value);
                  }
                  return value == null ? '' : String(value);
                }

                export function h(type, props, children) {
                  return { type, props: props || {}, children };
                }

                export function createApp(rootComponent, rootProps) {
                  return {
                    mount(targetSelector) {
                      const target = typeof targetSelector === 'string'
                        ? document.querySelector(targetSelector)
                        : targetSelector;
                      if (!target) return null;
                      const vnode = typeof rootComponent?.render === 'function'
                        ? rootComponent.render(rootProps || {})
                        : h('div', null, '');
                      target.replaceChildren(renderVNode(vnode));
                      return target;
                    }
                  };
                }

                function renderVNode(vnode) {
                  if (vnode == null || vnode === false) {
                    return document.createTextNode('');
                  }
                  if (typeof vnode === 'string' || typeof vnode === 'number') {
                    return document.createTextNode(String(vnode));
                  }
                  if (typeof vnode.type === 'function') {
                    return renderVNode(vnode.type(vnode.props || {}));
                  }
                  if (vnode.type && typeof vnode.type.render === 'function') {
                    return renderVNode(vnode.type.render(vnode.props || {}));
                  }
                  const node = document.createElement(String(vnode.type || 'div'));
                  for (const [key, value] of Object.entries(vnode.props || {})) {
                    if (key === 'class') {
                      node.className = value == null ? '' : String(value);
                    } else if (key.startsWith('on') && typeof value === 'function') {
                      node.addEventListener(key.slice(2).toLowerCase(), value);
                    } else if (value != null && value !== false) {
                      node.setAttribute(key, String(value));
                    }
                  }
                  const children = Array.isArray(vnode.children) ? vnode.children : [vnode.children];
                  for (const child of children) {
                    node.appendChild(renderVNode(child));
                  }
                  return node;
                }
                """;
    }

    private void registerVueRuntimeVirtualModules(String requestPath) {
        String browserRuntimePath = requestPath.replace("?qin-vue=runtime", "?qin-vue=browser-runtime");
        virtualModuleContentMap.putIfAbsent(browserRuntimePath, readVueBrowserRuntimeModule());
        virtualModuleContentMap.putIfAbsent(requestPath, renderQinVueRuntimeWrapper(browserRuntimePath));
    }

    private String renderQinVueRuntimeWrapper(String browserRuntimePath) {
        String escapedRuntimePath = escapeJsStringLiteral(browserRuntimePath);
        return """
                import * as Vue from "%s";
                export * from "%s";

                export function createApp(rootComponent, rootProps) {
                  const app = Vue.createApp(rootComponent, rootProps);
                  const originalMount = app.mount.bind(app);
                  app.mount = (target, ...args) => {
                    if (rootComponent && typeof rootComponent.__qinMountVue === 'function') {
                      return rootComponent.__qinMountVue(target);
                    }
                    return originalMount(target, ...args);
                  };
                  return app;
                }
                """.formatted(escapedRuntimePath, escapedRuntimePath);
    }

    private String readCsstsRuntimeModule() {
        Path runtimeModule = resolveCsstsRuntimeModule();
        if (!Files.exists(runtimeModule) || !Files.isRegularFile(runtimeModule)) {
            throw new IllegalStateException("Missing cssts-ts browser runtime module: " + runtimeModule);
        }
        try {
            String source = Files.readString(runtimeModule, StandardCharsets.UTF_8);
            return runtimeModule.toString().endsWith(".ts")
                    ? transpileTypescriptRuntimeModule(source)
                    : source;
        } catch (IOException error) {
            throw new IllegalStateException("Failed to read cssts-ts browser runtime module: " + runtimeModule, error);
        }
    }

    private Path resolveCsstsRuntimeModule() {
        return resolvePackageRuntimeModule(
                "cssts-ts",
                List.of("dist/index.mjs", "src/index.js", "src/index.ts"),
                "src/index.ts");
    }

    private Path resolvePackageRuntimeModule(
            String packageName,
            List<String> entries,
            String fallbackEntry) {
        List<Path> packageDirs = runtimePackageCandidateDirs(packageName);
        for (Path packageDir : packageDirs) {
            for (String entry : entries) {
                Path runtimeModule = packageDir.resolve(entry).toAbsolutePath().normalize();
                if (Files.exists(runtimeModule) && Files.isRegularFile(runtimeModule)) {
                    return runtimeModule;
                }
            }
        }
        Path fallbackPackageDir = packageDirs.isEmpty()
                ? projectRoot.resolve(".qin").resolve("runtime").resolve("npm-host").resolve("node_modules")
                        .resolve(packageName.replace('/', java.io.File.separatorChar))
                : packageDirs.get(packageDirs.size() - 1);
        return fallbackPackageDir.resolve(fallbackEntry).toAbsolutePath().normalize();
    }

    private List<Path> runtimePackageCandidateDirs(String packageName) {
        List<Path> candidates = new ArrayList<>();
        Path override = readProjectPackageOverrides().get(packageName);
        addPackageDirCandidate(candidates, override);
        addPackageDirCandidate(
                candidates,
                projectRoot.resolve(".qin").resolve("runtime").resolve("npm-host").resolve("node_modules")
                        .resolve(packageName.replace('/', java.io.File.separatorChar)));
        addPackageDirCandidate(
                candidates,
                projectRoot.resolve("node_modules")
                        .resolve(packageName.replace('/', java.io.File.separatorChar)));

        Path workspaceRoot = locateWorkspaceRoot();
        if (workspaceRoot != null) {
            if ("cssts-ts".equals(packageName)) {
                addPackageDirCandidate(candidates, workspaceRoot.resolve("cssts").resolve("cssts").resolve("cssts-runtime"));
            } else if ("ovsjs".equals(packageName)) {
                addPackageDirCandidate(candidates, workspaceRoot.resolve("ovsjs").resolve("ovs").resolve("ovs-runtime"));
            }
            addPackageDirCandidate(
                    candidates,
                    workspaceRoot.resolve("node_modules")
                            .resolve(packageName.replace('/', java.io.File.separatorChar)));
        }
        return candidates;
    }

    private void addPackageDirCandidate(List<Path> candidates, Path packageDir) {
        if (packageDir == null) {
            return;
        }
        Path normalized = packageDir.toAbsolutePath().normalize();
        if (Files.isDirectory(normalized) && !candidates.contains(normalized)) {
            candidates.add(normalized);
        }
    }

    private Map<String, Path> readProjectPackageOverrides() {
        Path configFile = projectRoot.resolve("qin.config.js");
        if (!Files.isRegularFile(configFile)) {
            return Map.of();
        }
        try {
            String configSource = Files.readString(configFile, StandardCharsets.UTF_8);
            Matcher blockMatcher = QIN_PACKAGE_OVERRIDES_BLOCK.matcher(configSource);
            if (!blockMatcher.find()) {
                return Map.of();
            }
            Map<String, Path> overrides = new LinkedHashMap<>();
            Matcher fieldMatcher = QIN_STRING_FIELD.matcher(blockMatcher.group(1));
            while (fieldMatcher.find()) {
                String packageName = fieldMatcher.group(1);
                String pathText = fieldMatcher.group(2);
                if (packageName == null || packageName.isBlank() || pathText == null || pathText.isBlank()) {
                    continue;
                }
                Path overridePath = projectRoot.resolve(pathText).toAbsolutePath().normalize();
                if (Files.isDirectory(overridePath)) {
                    overrides.put(packageName, overridePath);
                }
            }
            return overrides;
        } catch (IOException error) {
            throw new IllegalStateException("Failed to read Qin package overrides: " + configFile, error);
        }
    }

    private Path locateWorkspaceRoot() {
        Path current = Path.of("").toAbsolutePath().normalize();
        while (current != null) {
            if (Files.isDirectory(current.resolve("qin"))
                    && Files.isDirectory(current.resolve("slime"))) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    private static String transpileTypescriptRuntimeModule(String source) {
        String js = stripTypescriptDeclarations(source);
        js = js.replace("\uFEFF", "");
        js = js.replace("} as const", "}");
        js = js.replaceAll("new Map<[^\\n]+>\\(\\)", "new Map()");
        js = stripFunctionTypeAnnotations(js);
        js = js.replaceAll("(?m)^(\\s*(?:const|let|var)\\s+[$A-Za-z_][\\w$]*)\\s*:[^=;]+=", "$1 =");
        return js;
    }

    private static String stripTypescriptDeclarations(String source) {
        String[] lines = source.split("\\R", -1);
        StringBuilder js = new StringBuilder(source.length());
        boolean skippingTypeAlias = false;
        boolean skippingInterface = false;
        for (String line : lines) {
            String trimmed = line.trim();
            if (skippingTypeAlias) {
                if (trimmed.isEmpty()) {
                    skippingTypeAlias = false;
                }
                continue;
            }
            if (skippingInterface) {
                if (trimmed.equals("}")) {
                    skippingInterface = false;
                }
                continue;
            }
            if (trimmed.startsWith("export type ")) {
                skippingTypeAlias = true;
                continue;
            }
            if (trimmed.startsWith("interface ")) {
                skippingInterface = true;
                continue;
            }
            js.append(line).append('\n');
        }
        return js.toString();
    }

    private static String stripFunctionTypeAnnotations(String source) {
        String[] lines = source.split("\\R", -1);
        StringBuilder js = new StringBuilder(source.length());
        boolean inFunctionParameters = false;
        for (String line : lines) {
            String next = line;
            String trimmed = line.trim();
            if ((trimmed.startsWith("function ") || trimmed.startsWith("export function ")) && line.contains("(")) {
                if (line.contains(")")) {
                    next = stripInlineFunctionSignature(line);
                } else {
                    inFunctionParameters = true;
                }
            } else if (inFunctionParameters) {
                if (trimmed.startsWith(")")) {
                    next = line.replaceAll("^(\\s*\\))\\s*:.*\\{\\s*$", "$1 {");
                    inFunctionParameters = false;
                } else {
                    next = stripParameterTypeAnnotation(line);
                }
            }
            js.append(next).append('\n');
        }
        return js.toString();
    }

    private static String stripInlineFunctionSignature(String line) {
        int open = line.indexOf('(');
        int close = line.lastIndexOf(')');
        if (open < 0 || close < open) {
            return line;
        }
        String before = line.substring(0, open + 1);
        String parameters = stripParameterTypeAnnotation(line.substring(open + 1, close));
        String after = line.substring(close + 1).replaceAll("^\\s*:\\s*[^\\{]+\\{", " {");
        return before + parameters + ")" + after;
    }

    private static String stripParameterTypeAnnotation(String text) {
        return text.replaceAll("(\\.\\.\\.\\s*)?([$A-Za-z_][\\w$]*)\\s*:\\s*[^,)=]+", "$1$2");
    }

    private static String renderCssInjectionModule(String css) {
        String escaped = escapeJsStringLiteral(css);
        return """
                const css = "%s";
                if (typeof document !== 'undefined') {
                  const styleId = import.meta.url
                    .replace(/[?&]qin-hmr=[^&]+/g, '')
                    .replace(/[?&]$/, '');
                  let style = Array.from(document.querySelectorAll('style[data-qin-cssts]'))
                    .find(candidate => candidate.getAttribute('data-qin-style-id') === styleId);
                  if (!style) {
                    style = document.createElement('style');
                    style.setAttribute('data-qin-cssts', 'true');
                    style.setAttribute('data-qin-style-id', styleId);
                    document.head.appendChild(style);
                  }
                  style.setAttribute('data-qin-cssts', 'true');
                  style.textContent = css;
                }
                export default css;
                """.formatted(escaped);
    }

    private static String emptyCsstsAtomModule() {
        return """
                export const csstsAtom = {}
                export default csstsAtom
                """;
    }

    private static boolean isBlank(String text) {
        return text == null || text.isBlank();
    }

    private String renderAssetUrlModule(Path moduleFile) {
        return "export default \"" + escapeJsStringLiteral(toPublicUrl(projectRoot, moduleFile)) + "\";\n";
    }

    private record CachedOvsModule(String source, String cacheIdentity, String transpiled) {
    }

    private static String escapeJsStringLiteral(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

    private void emitVirtualModules(Path staticRoot) throws IOException {
        for (Map.Entry<String, String> entry : virtualModuleContentMap.entrySet()) {
            String requestPath = entry.getKey();
            String content = entry.getValue();
            if (requestPath == null || requestPath.isBlank() || content == null) {
                continue;
            }
            String relativeOutput = requestPath.startsWith("/@qin-mod/")
                    ? requestPath.substring("/@qin-mod/".length())
                    : requestPath.startsWith("/")
                    ? requestPath.substring(1)
                    : requestPath;
            int queryIndex = relativeOutput.indexOf('?');
            if (queryIndex >= 0) {
                relativeOutput = relativeOutput.substring(0, queryIndex)
                        + "__"
                        + relativeOutput.substring(queryIndex + 1).replace('=', '_').replace('&', '_');
            }
            Path output = staticRoot.resolve("@qin-mod").resolve(relativeOutput).normalize();
            Path parent = output.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(output, content, StandardCharsets.UTF_8);
        }
    }

    private static void validatePolicyAndSemantics(Path root, QinModuleGraph graph) throws Exception {
        QinPhaseTimer profile = QinPhaseTimer.start("frontend-service-validate");
        List<QinImportDescriptor> imports = new ArrayList<>();
        for (QinModuleSource module : graph.modules()) {
            for (QinResolvedImport resolvedImport : module.imports()) {
                imports.add(resolvedImport.descriptor());
            }
        }
        profile.checkpoint("collect imports", "imports=" + imports.size());

        new QinImportPolicyChecker().validate(root, imports);
        profile.checkpoint("import policy");
        QinEsmRuntimeFeatureValidator.forBrowserFrontend().validate(graph);
        profile.checkpoint("runtime feature scan");
        QinEsmSemanticModel model = analyzeFrontendSemanticsWithCache(root, graph);
        profile.checkpoint("semantic analyze", "modules=" + model.modules().size());
        new QinEsmLinkValidator().validate(model);
        profile.done("link validate");
    }

    private static QinEsmSemanticModel analyzeFrontendSemanticsWithCache(Path root, QinModuleGraph graph) throws Exception {
        String cacheKey = frontendSemanticCacheKey(root, graph);
        QinEsmSemanticModel cached = tryReadFrontendSemanticCache(root, cacheKey);
        if (cached != null) {
            System.out.println("[QinFrontendEsmService] frontend semantic cache hit :: modules="
                    + cached.modules().size());
            return cached;
        }
        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);
        writeFrontendSemanticCache(root, cacheKey, model);
        return model;
    }

    private static QinEsmSemanticModel tryReadFrontendSemanticCache(Path root, String cacheKey) throws IOException {
        Path stampFile = frontendSemanticCacheStampFile(root, cacheKey);
        if (!Files.isRegularFile(stampFile)) {
            return null;
        }
        Properties stamp = new Properties();
        try (var reader = Files.newBufferedReader(stampFile, StandardCharsets.UTF_8)) {
            stamp.load(reader);
        }
        if (!FRONTEND_SEMANTIC_CACHE_VERSION.equals(stamp.getProperty("version"))
                || !cacheKey.equals(stamp.getProperty("cacheKey"))) {
            return null;
        }
        int moduleCount = parseInt(stamp.getProperty("moduleCount"), -1);
        if (moduleCount < 0) {
            return null;
        }
        Map<Path, QinEsmModuleSemantic> modules = new LinkedHashMap<>();
        for (int moduleIndex = 0; moduleIndex < moduleCount; moduleIndex++) {
            String prefix = "module." + moduleIndex + ".";
            Path sourceFile = decodeCachePath(root, stamp.getProperty(prefix + "sourceFile"));
            if (sourceFile == null) {
                return null;
            }
            int importCount = parseInt(stamp.getProperty(prefix + "importCount"), -1);
            int exportCount = parseInt(stamp.getProperty(prefix + "exportCount"), -1);
            if (importCount < 0 || exportCount < 0) {
                return null;
            }
            List<QinEsmImportBinding> imports = new ArrayList<>();
            for (int importIndex = 0; importIndex < importCount; importIndex++) {
                QinEsmImportBinding binding = readImportBinding(root, stamp, prefix + "import." + importIndex + ".");
                if (binding == null) {
                    return null;
                }
                imports.add(binding);
            }
            List<QinEsmExportBinding> exports = new ArrayList<>();
            for (int exportIndex = 0; exportIndex < exportCount; exportIndex++) {
                QinEsmExportBinding binding = readExportBinding(root, stamp, prefix + "export." + exportIndex + ".");
                if (binding == null) {
                    return null;
                }
                exports.add(binding);
            }
            modules.put(sourceFile, new QinEsmModuleSemantic(sourceFile, imports, exports));
        }
        Path entryFile = decodeCachePath(root, stamp.getProperty("entryFile"));
        if (entryFile == null) {
            return null;
        }
        return new QinEsmSemanticModel(entryFile, modules);
    }

    private static QinEsmImportBinding readImportBinding(Path root, Properties stamp, String prefix) {
        Path sourceFile = decodeCachePath(root, stamp.getProperty(prefix + "sourceFile"));
        String kind = stamp.getProperty(prefix + "kind");
        if (sourceFile == null || kind == null) {
            return null;
        }
        return new QinEsmImportBinding(
                sourceFile,
                stamp.getProperty(prefix + "moduleSpecifier", ""),
                QinEsmImportKind.valueOf(kind),
                stamp.getProperty(prefix + "importedName", ""),
                stamp.getProperty(prefix + "localName", ""),
                parseInt(stamp.getProperty(prefix + "line"), 1),
                parseInt(stamp.getProperty(prefix + "column"), 1),
                decodeCachePath(root, stamp.getProperty(prefix + "resolvedModule")));
    }

    private static QinEsmExportBinding readExportBinding(Path root, Properties stamp, String prefix) {
        Path sourceFile = decodeCachePath(root, stamp.getProperty(prefix + "sourceFile"));
        String kind = stamp.getProperty(prefix + "kind");
        if (sourceFile == null || kind == null) {
            return null;
        }
        return new QinEsmExportBinding(
                sourceFile,
                QinEsmExportKind.valueOf(kind),
                stamp.getProperty(prefix + "exportName", ""),
                stamp.getProperty(prefix + "localName", ""),
                Boolean.parseBoolean(stamp.getProperty(prefix + "typeOnly", "false")),
                emptyToNull(stamp.getProperty(prefix + "moduleSpecifier", "")),
                decodeCachePath(root, stamp.getProperty(prefix + "resolvedModule")),
                parseInt(stamp.getProperty(prefix + "line"), 1),
                parseInt(stamp.getProperty(prefix + "column"), 1));
    }

    private static void writeFrontendSemanticCache(
            Path root,
            String cacheKey,
            QinEsmSemanticModel model) throws IOException {
        Properties stamp = new Properties();
        stamp.setProperty("version", FRONTEND_SEMANTIC_CACHE_VERSION);
        stamp.setProperty("cacheKey", cacheKey);
        stamp.setProperty("entryFile", encodeCachePath(root, model.entryFile()));
        List<Path> moduleFiles = model.modules().keySet().stream()
                .sorted(Comparator.comparing(Path::toString))
                .toList();
        stamp.setProperty("moduleCount", Integer.toString(moduleFiles.size()));
        for (int moduleIndex = 0; moduleIndex < moduleFiles.size(); moduleIndex++) {
            QinEsmModuleSemantic module = model.modules().get(moduleFiles.get(moduleIndex));
            String prefix = "module." + moduleIndex + ".";
            stamp.setProperty(prefix + "sourceFile", encodeCachePath(root, module.sourceFile()));
            stamp.setProperty(prefix + "importCount", Integer.toString(module.imports().size()));
            stamp.setProperty(prefix + "exportCount", Integer.toString(module.exports().size()));
            for (int importIndex = 0; importIndex < module.imports().size(); importIndex++) {
                writeImportBinding(root, stamp, prefix + "import." + importIndex + ".", module.imports().get(importIndex));
            }
            for (int exportIndex = 0; exportIndex < module.exports().size(); exportIndex++) {
                writeExportBinding(root, stamp, prefix + "export." + exportIndex + ".", module.exports().get(exportIndex));
            }
        }
        Path stampFile = frontendSemanticCacheStampFile(root, cacheKey);
        Files.createDirectories(stampFile.getParent());
        try (var writer = Files.newBufferedWriter(stampFile, StandardCharsets.UTF_8)) {
            stamp.store(writer, "Qin frontend semantic cache stamp");
        }
    }

    private static void writeImportBinding(
            Path root,
            Properties stamp,
            String prefix,
            QinEsmImportBinding binding) {
        stamp.setProperty(prefix + "sourceFile", encodeCachePath(root, binding.sourceFile()));
        stamp.setProperty(prefix + "moduleSpecifier", binding.moduleSpecifier());
        stamp.setProperty(prefix + "kind", binding.kind().name());
        stamp.setProperty(prefix + "importedName", binding.importedName());
        stamp.setProperty(prefix + "localName", binding.localName());
        stamp.setProperty(prefix + "line", Integer.toString(binding.line()));
        stamp.setProperty(prefix + "column", Integer.toString(binding.column()));
        stamp.setProperty(prefix + "resolvedModule", encodeCachePath(root, binding.resolvedModule()));
    }

    private static void writeExportBinding(
            Path root,
            Properties stamp,
            String prefix,
            QinEsmExportBinding binding) {
        stamp.setProperty(prefix + "sourceFile", encodeCachePath(root, binding.sourceFile()));
        stamp.setProperty(prefix + "kind", binding.kind().name());
        stamp.setProperty(prefix + "exportName", binding.exportName());
        stamp.setProperty(prefix + "localName", binding.localName());
        stamp.setProperty(prefix + "typeOnly", Boolean.toString(binding.typeOnly()));
        stamp.setProperty(prefix + "moduleSpecifier", nullToEmpty(binding.moduleSpecifier()));
        stamp.setProperty(prefix + "resolvedModule", encodeCachePath(root, binding.resolvedModule()));
        stamp.setProperty(prefix + "line", Integer.toString(binding.line()));
        stamp.setProperty(prefix + "column", Integer.toString(binding.column()));
    }

    private static String frontendSemanticCacheKey(Path root, QinModuleGraph graph) throws Exception {
        Path normalizedRoot = root.toAbsolutePath().normalize();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        updateDigest(digest, FRONTEND_SEMANTIC_CACHE_VERSION);
        updateDigest(digest, System.getProperty("java.version", ""));
        updateDigest(digest, normalizedRoot.toString());
        updateDigest(digest, encodeCachePath(normalizedRoot, graph.entryFile()));
        updateClassResourceDigest(digest, QinFrontendEsmService.class);
        updateClassResourceDigest(digest, QinEsmSemanticAnalyzer.class);
        updateClassResourceDigest(digest, QinEsmLinkValidator.class);
        updateClassResourceDigest(digest, QinEsmRuntimeFeatureValidator.class);
        updateClassResourceDigest(digest, QinParserFacade.class);
        List<QinModuleSource> modules = graph.modules().stream()
                .sorted(Comparator.comparing(module -> module.file().toString()))
                .toList();
        for (QinModuleSource module : modules) {
            updateDigest(digest, encodeCachePath(normalizedRoot, module.file()));
            String source = module.source() == null ? "" : module.source();
            byte[] bytes = source.getBytes(StandardCharsets.UTF_8);
            updateDigest(digest, Integer.toString(bytes.length));
            digest.update(bytes);
            digest.update((byte) 0);
            for (QinResolvedImport resolvedImport : module.imports()) {
                QinImportDescriptor descriptor = resolvedImport.descriptor();
                updateDigest(digest, descriptor.moduleSpecifier());
                updateDigest(digest, descriptor.kind().name());
                updateDigest(digest, Boolean.toString(descriptor.typeOnly()));
                updateDigest(digest, encodeCachePath(normalizedRoot, resolvedImport.resolvedModule()));
            }
        }
        return HexFormat.of().formatHex(digest.digest());
    }

    private static Path frontendSemanticCacheStampFile(Path root, String cacheKey) {
        String fileName = cacheKey.length() > 32 ? cacheKey.substring(0, 32) : cacheKey;
        return root.resolve(".qin/frontend-semantic-cache").resolve(fileName + ".properties").normalize();
    }

    private static void updateDigest(MessageDigest digest, String value) {
        digest.update((value == null ? "" : value).getBytes(StandardCharsets.UTF_8));
        digest.update((byte) 0);
    }

    private static void updateClassResourceDigest(MessageDigest digest, Class<?> type) throws IOException {
        updateDigest(digest, "class:" + type.getName());
        String resourceName = "/" + type.getName().replace('.', '/') + ".class";
        try (InputStream input = type.getResourceAsStream(resourceName)) {
            if (input == null) {
                updateDigest(digest, "missing");
            } else {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = input.read(buffer)) != -1) {
                    digest.update(buffer, 0, read);
                }
            }
        }
        digest.update((byte) 0);
    }

    private static String encodeCachePath(Path root, Path path) {
        if (path == null) {
            return "";
        }
        Path normalizedRoot = root.toAbsolutePath().normalize();
        Path normalized = path.toAbsolutePath().normalize();
        if (normalized.startsWith(normalizedRoot)) {
            return "root:" + normalizedRoot.relativize(normalized).toString().replace('\\', '/');
        }
        return "abs:" + normalized;
    }

    private static Path decodeCachePath(Path root, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        if (value.startsWith("root:")) {
            return root.resolve(value.substring("root:".length())).toAbsolutePath().normalize();
        }
        if (value.startsWith("abs:")) {
            return Path.of(value.substring("abs:".length())).toAbsolutePath().normalize();
        }
        return null;
    }

    private static int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (RuntimeException ignored) {
            return fallback;
        }
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }

    private static String toModuleUrl(Path root, Path file) {
        String relative = toRelativeUnix(root, file);
        relative = toJsModuleRelativePath(relative);
        return "/@qin-mod/" + relative;
    }

    private static List<String> requestPathAliases(String moduleUrl, Path file) {
        if (moduleUrl == null || moduleUrl.endsWith(".js")) {
            return List.of();
        }
        String name = file.getFileName() == null ? "" : file.getFileName().toString().toLowerCase();
        if (name.endsWith(".ts") || name.endsWith(".mjs")) {
            return List.of(moduleUrl + ".js");
        }
        return List.of();
    }

    private static String toJsModuleRelativePath(String relative) {
        if (relative.endsWith(".qin")) {
            return relative.substring(0, relative.length() - ".qin".length()) + ".js";
        }
        if (relative.endsWith(".vue")) {
            return relative + ".js";
        }
        if (relative.endsWith(".ovs")) {
            return relative + ".js";
        }
        if (relative.endsWith(".cssts")) {
            return relative + ".js";
        }
        if (isCssModuleName(relative) || isAssetModuleName(relative)) {
            return relative + ".js";
        }
        return relative;
    }

    private static String toPublicUrl(Path root, Path file) {
        return "/" + toRelativeUnix(root, file);
    }

    private static String toRelativeUnix(Path root, Path file) {
        return root.relativize(file).toString().replace('\\', '/');
    }

    private static boolean isFrontendModuleFile(Path file) {
        String name = file.getFileName() == null ? "" : file.getFileName().toString().toLowerCase();
        return name.endsWith(".js")
                || name.endsWith(".mjs")
                || name.endsWith(".ts")
                || name.endsWith(".qin")
                || name.endsWith(".vue")
                || name.endsWith(".ovs")
                || name.endsWith(".cssts")
                || isCssModuleName(name)
                || isAssetModuleName(name);
    }

    private static boolean isBrowserScriptModuleFile(Path file) {
        String name = file.getFileName() == null ? "" : file.getFileName().toString().toLowerCase();
        return name.endsWith(".js")
                || name.endsWith(".mjs")
                || name.endsWith(".ts")
                || name.endsWith(".qin")
                || name.endsWith(".vue")
                || name.endsWith(".ovs")
                || name.endsWith(".cssts");
    }

    private static boolean isVueModuleFile(Path file) {
        String name = file.getFileName() == null ? "" : file.getFileName().toString().toLowerCase();
        return name.endsWith(".vue");
    }

    private static boolean isOvsModuleFile(Path file) {
        String name = file.getFileName() == null ? "" : file.getFileName().toString().toLowerCase();
        return name.endsWith(".ovs");
    }

    private static boolean isCsstsModuleFile(Path file) {
        String name = file.getFileName() == null ? "" : file.getFileName().toString().toLowerCase();
        return name.endsWith(".cssts");
    }

    private static boolean isCssModuleFile(Path file) {
        String name = file.getFileName() == null ? "" : file.getFileName().toString().toLowerCase();
        return isCssModuleName(name);
    }

    private static boolean isAssetModuleFile(Path file) {
        String name = file.getFileName() == null ? "" : file.getFileName().toString().toLowerCase();
        return isAssetModuleName(name);
    }

    private static boolean isCssModuleName(String name) {
        return name != null && name.toLowerCase().endsWith(".css");
    }

    private static boolean isAssetModuleName(String name) {
        if (name == null) {
            return false;
        }
        String lower = name.toLowerCase();
        return lower.endsWith(".svg")
                || lower.endsWith(".png")
                || lower.endsWith(".jpg")
                || lower.endsWith(".jpeg")
                || lower.endsWith(".gif")
                || lower.endsWith(".webp")
                || lower.endsWith(".ico")
                || lower.endsWith(".avif");
    }
}
