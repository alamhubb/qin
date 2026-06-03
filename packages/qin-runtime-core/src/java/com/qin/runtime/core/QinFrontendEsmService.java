package com.qin.runtime.core;

import com.qin.lang.module.policy.QinImportDescriptor;
import com.qin.lang.module.policy.QinImportPolicyChecker;
import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.module.resolver.QinModuleSource;
import com.qin.lang.module.resolver.QinResolvedImport;
import com.qin.lang.sema.esm.QinEsmLinkValidator;
import com.qin.lang.sema.esm.QinEsmRuntimeFeatureValidator;
import com.qin.lang.sema.esm.QinEsmSemanticAnalyzer;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    private final Path projectRoot;
    private final Path entryFile;
    private final QinModuleGraph graph;
    private final Map<Path, QinModuleSource> moduleSourceMap;
    private final Map<Path, String> moduleUrlMap;
    private final Map<String, Path> requestPathMap;
    private final Map<String, String> virtualModuleContentMap;
    private final Map<Path, String> transpiledModuleCache = new LinkedHashMap<>();
    private final String entryModuleUrl;
    private final QinVueSfcCompiler vueSfcCompiler;
    private final QinOvsCompiler ovsCompiler;
    private final QinCsstsCompiler csstsCompiler;

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
        Path root = projectRoot.toAbsolutePath().normalize();
        Path entry = frontendEntry.toAbsolutePath().normalize();

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        validatePolicyAndSemantics(root, graph);

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
            }
        }

        String entryUrl = urlMap.get(entry);
        if (entryUrl == null) {
            throw new IllegalArgumentException("Frontend entry is not a supported module: " + entry.toAbsolutePath());
        }
        QinVueSfcCompiler vueCompiler = QinViteVuePluginCompiler.isEnabled(root)
                ? new QinViteVuePluginCompiler()
                : new QinOfficialVueSfcCompiler();
        return new QinFrontendEsmService(
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
        int queryIndex = normalizedRequestPath.indexOf('?');
        String pathOnly = queryIndex < 0 ? normalizedRequestPath : normalizedRequestPath.substring(0, queryIndex);
        if (!pathOnly.startsWith("/")) {
            return null;
        }
        Path moduleFile = projectRoot.resolve(pathOnly.substring(1)).toAbsolutePath().normalize();
        if (!moduleFile.startsWith(projectRoot) || !moduleSourceMap.containsKey(moduleFile)) {
            return null;
        }
        if (!isBrowserScriptModuleFile(moduleFile)) {
            return null;
        }
        String moduleUrl = moduleUrlMap.get(moduleFile);
        if (moduleUrl == null) {
            return null;
        }
        return injectQinHmrPrelude(moduleUrl, moduleFile, transpileModule(moduleFile));
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
        Path moduleRoot = staticRoot.resolve("@qin-mod").normalize();
        Files.createDirectories(moduleRoot);

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
        }

        emitVirtualModules(staticRoot);

        Files.writeString(staticRoot.resolve("app.js"), bootstrapJs(), StandardCharsets.UTF_8);
    }

    private synchronized String transpileModule(Path moduleFile) throws IOException {
        Path normalizedModuleFile = moduleFile.toAbsolutePath().normalize();
        String cached = transpiledModuleCache.get(normalizedModuleFile);
        if (cached != null) {
            return cached;
        }
        QinModuleSource module = moduleSourceMap.get(moduleFile.toAbsolutePath().normalize());
        if (module == null) {
            throw new IllegalArgumentException("Unknown frontend module: " + moduleFile.toAbsolutePath());
        }

        String source = module.source();
        String transpiled;
        if (isVueModuleFile(moduleFile)) {
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
            source = rewriteSpecifiers(module, source, IMPORT_FROM_PATTERN);
            source = rewriteSpecifiers(module, source, EXPORT_FROM_PATTERN);
            source = rewriteSpecifiers(module, source, IMPORT_SIDE_EFFECT_PATTERN);
            transpiled = source;
        }
        transpiledModuleCache.put(normalizedModuleFile, transpiled);
        return transpiled;
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

    private String transpileOvsModule(Path moduleFile, String source) {
        QinModuleSource module = moduleSourceMap.get(moduleFile.toAbsolutePath().normalize());
        QinModuleSource sourceModule = module != null
                ? module
                : new QinModuleSource(moduleFile.toAbsolutePath().normalize(), source, List.of());
        QinOvsCompiler.QinOvsCompileResult result;
        try {
            result = ovsCompiler.compile(projectRoot, source);
        } catch (Exception error) {
            throw new IllegalStateException("Qin OVS compilation failed for " + moduleFile.toAbsolutePath(), error);
        }

        registerOvsVirtualModules(moduleFile, result);
        String compiled = result.code();
        compiled = rewriteSpecifiers(sourceModule, compiled, IMPORT_FROM_PATTERN);
        compiled = rewriteSpecifiers(sourceModule, compiled, EXPORT_FROM_PATTERN);
        compiled = rewriteSpecifiers(sourceModule, compiled, IMPORT_SIDE_EFFECT_PATTERN);
        return mountOvsModule(moduleFile, compiled);
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
        return compiled;
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
            return toModuleUrl(projectRoot, module.file().toAbsolutePath().normalize()) + "?qin-vue-cssts=style";
        }
        if ("virtual:csstsAtom".equals(specifier)) {
            return toModuleUrl(projectRoot, module.file().toAbsolutePath().normalize()) + "?qin-vue-cssts=atom";
        }
        if ("cssts-ts".equals(specifier)) {
            return toModuleUrl(projectRoot, module.file().toAbsolutePath().normalize()) + "?qin-vue-cssts=runtime";
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

        if (css != null && !css.isBlank()) {
            virtualModuleContentMap.put(cssRequestPath, renderCssInjectionModule(css));
        }
        if (atom != null && !atom.isBlank()) {
            virtualModuleContentMap.put(atomRequestPath, atom);
        }
        virtualModuleContentMap.put(runtimeRequestPath, readCsstsRuntimeModule());
    }

    private void registerOvsVirtualModules(Path moduleFile, QinOvsCompiler.QinOvsCompileResult result) {
        String base = toModuleUrl(projectRoot, moduleFile);
        String cssRequestPath = base + "?qin-vue-cssts=style";
        String atomRequestPath = base + "?qin-vue-cssts=atom";
        String runtimeRequestPath = base + "?qin-vue-cssts=runtime";
        String ovsRuntimeRequestPath = base + "?qin-ovs=runtime";
        String vueRuntimeRequestPath = base + "?qin-ovs=vue";

        if (result.css() != null && !result.css().isBlank()) {
            virtualModuleContentMap.put(cssRequestPath, renderCssInjectionModule(result.css()));
        }
        if (result.atomModule() != null && !result.atomModule().isBlank()) {
            virtualModuleContentMap.put(atomRequestPath, result.atomModule());
        }
        virtualModuleContentMap.put(runtimeRequestPath, readCsstsRuntimeModule());
        virtualModuleContentMap.put(ovsRuntimeRequestPath, readOvsRuntimeModule(vueRuntimeRequestPath));
        virtualModuleContentMap.put(vueRuntimeRequestPath, readVueBrowserRuntimeModule());
    }

    private void registerCsstsVirtualModules(Path moduleFile, QinCsstsCompiler.QinCsstsCompileResult result) {
        String base = toModuleUrl(projectRoot, moduleFile);
        String cssRequestPath = base + "?qin-vue-cssts=style";
        String atomRequestPath = base + "?qin-vue-cssts=atom";
        String runtimeRequestPath = base + "?qin-vue-cssts=runtime";
        if (result.css() != null && !result.css().isBlank()) {
            virtualModuleContentMap.put(cssRequestPath, renderCssInjectionModule(result.css()));
        }
        if (result.atomModule() != null && !result.atomModule().isBlank()) {
            virtualModuleContentMap.put(atomRequestPath, result.atomModule());
        }
        virtualModuleContentMap.put(runtimeRequestPath, readCsstsRuntimeModule());
    }

    private String mountOvsModule(Path moduleFile, String source) {
        String vueRuntime = toModuleUrl(projectRoot, moduleFile) + "?qin-ovs=vue";
        String marker = "export default ";
        int exportIndex = source.indexOf(marker);
        if (exportIndex < 0) {
            return source;
        }
        String transformed = source.substring(0, exportIndex)
                + "const __qinOvsDefault = "
                + source.substring(exportIndex + marker.length());
        return """
                import { createApp as __qinCreateApp } from "%s";
                %s
                function __qinMountOvs() {
                  if (typeof document === 'undefined') return null;
                  const __qinOvsTarget = document.querySelector('[data-qin-component]') || document.querySelector('#ovs-demo');
                  if (!__qinOvsTarget) return null;
                  __qinOvsTarget.innerHTML = '';
                  return __qinCreateApp(__qinOvsDefault).mount(__qinOvsTarget);
                }
                if (typeof document !== 'undefined') {
                  setTimeout(__qinMountOvs, 0);
                }
                export { __qinMountOvs };
                export default __qinOvsDefault;
                """.formatted(vueRuntime, transformed);
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
        List<Path> candidates = List.of(
                projectRoot.resolve(".qin").resolve("runtime").resolve("npm-host").resolve("node_modules"),
                projectRoot.resolve("node_modules"));
        for (Path nodeModules : candidates) {
            Path runtimeModule = nodeModules
                    .resolve("ovsjs")
                    .resolve("dist")
                    .resolve("index.mjs")
                    .toAbsolutePath()
                    .normalize();
            if (Files.exists(runtimeModule) && Files.isRegularFile(runtimeModule)) {
                return runtimeModule;
            }
        }
        return candidates.get(candidates.size() - 1)
                .resolve("ovsjs")
                .resolve("dist")
                .resolve("index.mjs")
                .toAbsolutePath()
                .normalize();
    }

    private String readVueBrowserRuntimeModule() {
        Path runtimeModule = resolveVueBrowserRuntimeModule();
        if (!Files.exists(runtimeModule) || !Files.isRegularFile(runtimeModule)) {
            throw new IllegalStateException("Missing Vue browser runtime module: " + runtimeModule);
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
        List<Path> candidates = List.of(
                projectRoot.resolve("node_modules"),
                projectRoot.resolve(".qin").resolve("runtime").resolve("npm-host").resolve("node_modules"));
        for (Path nodeModules : candidates) {
            for (String entry : List.of("dist/index.mjs", "src/index.js", "src/index.ts")) {
                Path runtimeModule = nodeModules
                        .resolve("cssts-ts")
                        .resolve(entry)
                        .toAbsolutePath()
                        .normalize();
                if (Files.exists(runtimeModule) && Files.isRegularFile(runtimeModule)) {
                    return runtimeModule;
                }
            }
        }
        return candidates.get(candidates.size() - 1)
                .resolve("cssts-ts")
                .resolve("src")
                .resolve("index.ts")
                .toAbsolutePath()
                .normalize();
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

    private String renderAssetUrlModule(Path moduleFile) {
        return "export default \"" + escapeJsStringLiteral(toPublicUrl(projectRoot, moduleFile)) + "\";\n";
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

    private static void validatePolicyAndSemantics(Path root, QinModuleGraph graph) {
        List<QinImportDescriptor> imports = new ArrayList<>();
        for (QinModuleSource module : graph.modules()) {
            for (QinResolvedImport resolvedImport : module.imports()) {
                imports.add(resolvedImport.descriptor());
            }
        }

        new QinImportPolicyChecker().validate(root, imports);
        QinEsmRuntimeFeatureValidator.forBrowserFrontend().validate(graph);
        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);
        new QinEsmLinkValidator().validate(model);
    }

    private static String toModuleUrl(Path root, Path file) {
        String relative = toRelativeUnix(root, file);
        relative = toJsModuleRelativePath(relative);
        return "/@qin-mod/" + relative;
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
