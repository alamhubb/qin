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
 * Frontend ESM service: validate module graph and transpile .js modules to browser JS.
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
    private final String entryModuleUrl;

    private QinFrontendEsmService(
            Path projectRoot,
            Path entryFile,
            QinModuleGraph graph,
            Map<Path, QinModuleSource> moduleSourceMap,
            Map<Path, String> moduleUrlMap,
            String entryModuleUrl) {
        this.projectRoot = projectRoot;
        this.entryFile = entryFile;
        this.graph = graph;
        this.moduleSourceMap = moduleSourceMap;
        this.moduleUrlMap = moduleUrlMap;
        this.entryModuleUrl = entryModuleUrl;
    }

    public static QinFrontendEsmService create(Path projectRoot, Path frontendEntry) throws Exception {
        Path root = projectRoot.toAbsolutePath().normalize();
        Path entry = frontendEntry.toAbsolutePath().normalize();

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        validatePolicyAndSemantics(root, graph);

        Map<Path, QinModuleSource> sourceMap = new LinkedHashMap<>();
        Map<Path, String> urlMap = new LinkedHashMap<>();
        for (QinModuleSource module : graph.modules()) {
            Path file = module.file().toAbsolutePath().normalize();
            sourceMap.put(file, module);
            if (file.toString().endsWith(".js")) {
                urlMap.put(file, toModuleUrl(root, file));
            }
        }

        String entryUrl = urlMap.get(entry);
        if (entryUrl == null) {
            throw new IllegalArgumentException("Frontend entry is not a .js module: " + entry.toAbsolutePath());
        }
        return new QinFrontendEsmService(root, entry, graph, sourceMap, urlMap, entryUrl);
    }

    public String bootstrapJs() {
        return "import(\"" + entryModuleUrl + "\");\n";
    }

    public String transpileByRequestPath(String requestPath) throws IOException {
        Path moduleFile = resolveRequestToModuleFile(requestPath);
        if (moduleFile == null) {
            return null;
        }
        return transpileModule(moduleFile);
    }

    public void emitProduction(Path staticRoot) throws IOException {
        Path moduleRoot = staticRoot.resolve("@qin-mod").normalize();
        Files.createDirectories(moduleRoot);

        for (QinModuleSource module : graph.modules()) {
            Path file = module.file().toAbsolutePath().normalize();
            if (!file.toString().endsWith(".js")) {
                continue;
            }
            String relative = toRelativeUnix(projectRoot, file);
            Path output = moduleRoot.resolve(relative).normalize();
            Path parent = output.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(output, transpileModule(file), StandardCharsets.UTF_8);
        }

        Files.writeString(staticRoot.resolve("app.js"), bootstrapJs(), StandardCharsets.UTF_8);
    }

    private String transpileModule(Path moduleFile) throws IOException {
        QinModuleSource module = moduleSourceMap.get(moduleFile.toAbsolutePath().normalize());
        if (module == null) {
            throw new IllegalArgumentException("Unknown frontend module: " + moduleFile.toAbsolutePath());
        }

        String source = module.source();
        source = rewriteSpecifiers(module, source, IMPORT_FROM_PATTERN);
        source = rewriteSpecifiers(module, source, EXPORT_FROM_PATTERN);
        source = rewriteSpecifiers(module, source, IMPORT_SIDE_EFFECT_PATTERN);
        return source;
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
        if (requestPath == null || !requestPath.startsWith("/@qin-mod/")) {
            return null;
        }
        String rel = requestPath.substring("/@qin-mod/".length());
        if (rel.isBlank() || rel.contains("..")) {
            return null;
        }
        String jsRel = rel.endsWith(".js") ? rel : (rel + ".js");
        Path file = projectRoot.resolve(jsRel).normalize().toAbsolutePath();
        if (!file.startsWith(projectRoot) || !Files.exists(file) || !Files.isRegularFile(file)) {
            return null;
        }
        return file;
    }

    private static void validatePolicyAndSemantics(Path root, QinModuleGraph graph) {
        List<QinImportDescriptor> imports = new ArrayList<>();
        for (QinModuleSource module : graph.modules()) {
            for (QinResolvedImport resolvedImport : module.imports()) {
                imports.add(resolvedImport.descriptor());
            }
        }

        new QinImportPolicyChecker().validate(root, imports);
        new QinEsmRuntimeFeatureValidator().validate(graph);
        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);
        new QinEsmLinkValidator().validate(model);
    }

    private static String toModuleUrl(Path root, Path file) {
        return "/@qin-mod/" + toRelativeUnix(root, file);
    }

    private static String toRelativeUnix(Path root, Path file) {
        return root.relativize(file).toString().replace('\\', '/');
    }
}
