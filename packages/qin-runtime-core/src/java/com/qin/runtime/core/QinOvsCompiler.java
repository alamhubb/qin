package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileVisitResult;
import java.nio.file.SimpleFileVisitor;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QinOvsCompiler {
    private static final int MAX_CACHE_ENTRIES = 64;
    private static final List<String> TRANSFORM_TOOLCHAIN_PACKAGES = List.of(
            "ovs-compiler",
            "vite-plugin-ovs",
            "cssts-compiler",
            "@qin/generated-qin-parser-ts",
            "slime-generator",
            "slime-ast");
    private static final Set<String> IGNORED_TOOLCHAIN_DIRS = Set.of(
            ".git", ".idea", ".qin", "node_modules", "build", "target", "out");
    private static final Pattern CLASS_PREFIX_PATTERN = Pattern.compile(
            "\\bclassPrefix\\s*:\\s*(['\"])(.*?)\\1");
    private static final Pattern QIN_PACKAGE_OVERRIDES_BLOCK = Pattern.compile(
            "packageOverrides\\s*:\\s*\\{([^}]*)\\}",
            Pattern.DOTALL);
    private static final Pattern QIN_STRING_FIELD = Pattern.compile("[\"']([^\"']+)[\"']\\s*:\\s*[\"']([^\"']*)[\"']");

    private final QinJsPackageRunner packageRunner = new QinJsPackageRunner();
    private final Map<CacheKey, QinOvsCompileResult> cache = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<CacheKey, QinOvsCompileResult> eldest) {
            return size() > MAX_CACHE_ENTRIES;
        }
    };

    public QinOvsCompileResult compile(Path projectRoot, String source) throws Exception {
        return compile(projectRoot, null, source);
    }

    String frontendCacheIdentity(Path projectRoot, Path moduleFile, String source) throws Exception {
        Path normalizedRoot = projectRoot.toAbsolutePath().normalize();
        Path normalizedModuleFile = moduleFile == null
                ? null
                : moduleFile.toAbsolutePath().normalize();
        String configSource = readConfigSource(normalizedRoot);
        String toolchainFingerprint = transformToolchainFingerprint(normalizedRoot, configSource);
        String semanticRoot = semanticRoot(normalizedRoot);
        String semanticModule = semanticModuleFile(normalizedRoot, normalizedModuleFile);
        return QinFrontendTransformDiskCache.keyMaterial(
                semanticRoot,
                "module=" + semanticModule
                        + "\ntoolchain=" + toolchainFingerprint
                        + "\nsource=" + (source == null ? "" : source),
                configSource);
    }

    public QinOvsCompileResult compile(Path projectRoot, Path moduleFile, String source) throws Exception {
        Path normalizedRoot = projectRoot.toAbsolutePath().normalize();
        Path normalizedModuleFile = moduleFile == null
                ? null
                : moduleFile.toAbsolutePath().normalize();
        source = source == null ? "" : source;
        String configSource = readConfigSource(normalizedRoot);
        String toolchainFingerprint = transformToolchainFingerprint(normalizedRoot, configSource);
        CacheKey key = new CacheKey(normalizedRoot, normalizedModuleFile, source, toolchainFingerprint);
        synchronized (cache) {
            QinOvsCompileResult cached = cache.get(key);
            if (cached != null) {
                return cached;
            }
        }
        Path transformCacheRoot = transformCacheRoot(normalizedRoot);
        String semanticRoot = semanticRoot(normalizedRoot);
        String semanticModule = semanticModuleFile(normalizedRoot, normalizedModuleFile);
        String diskKey = QinFrontendTransformDiskCache.keyMaterial(
                semanticRoot,
                "module=" + semanticModule
                        + "\ntoolchain=" + toolchainFingerprint
                        + "\nsource=" + source,
                configSource);
        QinOvsCompileResult diskCached = QinFrontendTransformDiskCache.read(
                        transformCacheRoot,
                        normalizedRoot,
                        "ovs",
                        diskKey)
                .map(this::decodeDiskCache)
                .orElse(null);
        if (diskCached != null) {
            synchronized (cache) {
                cache.put(key, diskCached);
            }
            System.out.println("[QinOvsCompiler] transform disk cache hit");
            return diskCached;
        }
        try {
            bindTransformInputs(
                    normalizedRoot,
                    source,
                    renderViteId(normalizedRoot, normalizedModuleFile, semanticModule));
            Object result = packageRunner.runModuleSource(
                    normalizedRoot,
                    buildWrapperSource(normalizedRoot),
                    "vite_plugin_ovs_transform");
            QinOvsCompileResult decoded = decodeResult(result);
            QinFrontendTransformDiskCache.write(transformCacheRoot, normalizedRoot, "ovs", diskKey, encodeDiskCache(decoded));
            synchronized (cache) {
                cache.put(key, decoded);
            }
            return decoded;
        } catch (Exception error) {
            throw new IllegalStateException("Qin vite-plugin-ovs transform failed for " + projectRoot, error);
        }
    }

    public Map<Path, QinOvsCompileResult> compileAll(Path projectRoot, Map<Path, String> modules) throws Exception {
        if (modules == null || modules.isEmpty()) {
            return Map.of();
        }

        Path normalizedRoot = projectRoot.toAbsolutePath().normalize();
        String configSource = readConfigSource(normalizedRoot);
        String toolchainFingerprint = transformToolchainFingerprint(normalizedRoot, configSource);
        Path transformCacheRoot = transformCacheRoot(normalizedRoot);
        String semanticRoot = semanticRoot(normalizedRoot);
        Map<Path, QinOvsCompileResult> results = new LinkedHashMap<>();
        List<BatchCompileInput> toTransform = new java.util.ArrayList<>();

        for (Map.Entry<Path, String> entry : modules.entrySet()) {
            Path normalizedModuleFile = entry.getKey() == null
                    ? null
                    : entry.getKey().toAbsolutePath().normalize();
            String source = entry.getValue() == null ? "" : entry.getValue();
            String semanticModule = semanticModuleFile(normalizedRoot, normalizedModuleFile);
            CacheKey key = new CacheKey(normalizedRoot, normalizedModuleFile, source, toolchainFingerprint);
            QinOvsCompileResult cached;
            synchronized (cache) {
                cached = cache.get(key);
            }
            if (cached != null) {
                results.put(normalizedModuleFile, cached);
                continue;
            }

            String diskKey = QinFrontendTransformDiskCache.keyMaterial(
                    semanticRoot,
                    "module=" + semanticModule
                            + "\ntoolchain=" + toolchainFingerprint
                            + "\nsource=" + source,
                    configSource);
            QinOvsCompileResult diskCached = QinFrontendTransformDiskCache.read(
                            transformCacheRoot,
                            normalizedRoot,
                            "ovs",
                            diskKey)
                    .map(this::decodeDiskCache)
                    .orElse(null);
            if (diskCached != null) {
                synchronized (cache) {
                    cache.put(key, diskCached);
                }
                System.out.println("[QinOvsCompiler] transform disk cache hit");
                results.put(normalizedModuleFile, diskCached);
                continue;
            }
            toTransform.add(new BatchCompileInput(
                    normalizedModuleFile,
                    source,
                    semanticModule,
                    diskKey,
                    key));
        }

        if (toTransform.isEmpty()) {
            return results;
        }

        try {
            bindBatchTransformInputs(normalizedRoot, toTransform);
            Object result = packageRunner.runModuleSource(
                    normalizedRoot,
                    buildBatchWrapperSource(normalizedRoot),
                    "vite_plugin_ovs_transform_batch");
            List<QinOvsCompileResult> decodedResults = decodeBatchResult(result, toTransform.size());
            for (int i = 0; i < toTransform.size(); i++) {
                BatchCompileInput input = toTransform.get(i);
                QinOvsCompileResult decoded = decodedResults.get(i);
                QinFrontendTransformDiskCache.write(
                        transformCacheRoot,
                        normalizedRoot,
                        "ovs",
                        input.diskKey(),
                        encodeDiskCache(decoded));
                synchronized (cache) {
                    cache.put(input.cacheKey(), decoded);
                }
                results.put(input.moduleFile(), decoded);
            }
            return results;
        } catch (Exception error) {
            throw new IllegalStateException("Qin vite-plugin-ovs batch transform failed for " + projectRoot, error);
        }
    }

    private void bindTransformInputs(Path projectRoot, String source, String id) {
        JavaEsmGlobal.__qin_bind_global__(ovsTransformGlobalName(projectRoot, "source"), source == null ? "" : source);
        JavaEsmGlobal.__qin_bind_global__(ovsTransformGlobalName(projectRoot, "id"), id == null ? "" : id);
    }

    private void bindBatchTransformInputs(Path projectRoot, List<BatchCompileInput> inputs) {
        List<Object> boundInputs = new ArrayList<>();
        for (BatchCompileInput input : inputs) {
            Map<String, Object> bound = new LinkedHashMap<>();
            bound.put("source", input.source() == null ? "" : input.source());
            bound.put("id", renderViteId(projectRoot, input.moduleFile(), input.semanticModule()));
            boundInputs.add(bound);
        }
        JavaEsmGlobal.__qin_bind_global__(ovsTransformGlobalName(projectRoot, "batch_inputs"), boundInputs);
    }

    private String buildWrapperSource(Path projectRoot) {
        String sourceGlobal = ovsTransformGlobalName(projectRoot, "source");
        String idGlobal = ovsTransformGlobalName(projectRoot, "id");
        return """
                import vitePluginOvs from "vite-plugin-ovs";
                import { vitePluginOvsTransform } from "ovs-compiler";
                import { RuntimeStore, generateStylesCss, generateCsstsAtomModule } from "cssts-compiler";
                %s
                function __qinFlattenPlugins(value) {
                  const out = [];
                  for (const item of Array.isArray(value) ? value : []) {
                    if (!item) continue;
                    if (Array.isArray(item)) out.push(...__qinFlattenPlugins(item));
                    else out.push(item);
                  }
                  return out;
                }
                function __qinPluginName(plugin) {
                  return plugin && typeof plugin.name === "string" ? plugin.name : "";
                }
                function __qinResolveMaybePromise(value) {
                  if (value && value.then) {
                    let resolved = value;
                    value.then(next => { resolved = next; });
                    return resolved;
                  }
                  return value;
                }
                function __qinCallPluginHook(hook, context, ...args) {
                  if (!hook) return null;
                  const result = typeof hook === "function"
                    ? hook.call(context, ...args)
                    : hook.handler.call(context, ...args);
                  return __qinResolveMaybePromise(result);
                }
                const __qin_user_config__ = qinUserViteConfig || {};
                const __qin_plugins__ = __qinFlattenPlugins(__qin_user_config__.plugins);
                if (!__qin_plugins__.find(plugin => __qinPluginName(plugin) === "vite-plugin-ovs")) {
                  console.log("[QinOvsCompiler] instantiate vite-plugin-ovs");
                  __qin_plugins__.push(...__qinFlattenPlugins(vitePluginOvs(%s)));
                }
                const __qin_plugin__ = __qin_plugins__.find(plugin => plugin && plugin.name === "vite-plugin-ovs");
                if (!__qin_plugin__ || !__qin_plugin__.transform) {
                  throw new Error("vite-plugin-ovs transform hook not found");
                }
                const __qin_context__ = {
                  parse(code) { return {}; },
                  addWatchFile(file) {},
                  emitFile(file) { return "qin-ovs-file"; },
                  warn(message) {},
                  error(message) { throw new Error(String(message)); }
                };
                const __qin_source__ = globalThis.%s;
                const __qin_id__ = globalThis.%s;
                const __qin_shared_styles__ = new Set();
                let __qin_result__ = vitePluginOvsTransform(__qin_source__, { globalStyles: __qin_shared_styles__ });
                let __qin_code__ = typeof __qin_result__ === "string" ? __qin_result__ : __qin_result__ && __qin_result__.code;
                if (typeof __qin_code__ !== "string" || __qin_code__.length === 0) {
                  __qin_result__ = __qinCallPluginHook(
                    __qin_plugin__.transform,
                    __qin_context__,
                    __qin_source__,
                    __qin_id__
                  );
                  __qin_code__ = typeof __qin_result__ === "string" ? __qin_result__ : __qin_result__ && __qin_result__.code;
                }
                if (typeof __qin_code__ !== "string" || __qin_code__.length === 0) {
                  throw new Error("vite-plugin-ovs transform returned empty code for " + __qin_id__);
                }
                if (__qin_shared_styles__.size > 0 && !__qin_code__.includes("virtual:cssts.css")) {
                  __qin_code__ = "import 'virtual:cssts.css'\\n" + __qin_code__;
                }
                RuntimeStore.addUsedStyles(__qin_shared_styles__);
                const __qin_css__ = __qin_shared_styles__.size > 0 ? generateStylesCss() : "";
                const __qin_atom__ = __qin_shared_styles__.size > 0 ? generateCsstsAtomModule() : "";
                ({
                  code: __qin_code__,
                  hasStyles: __qin_code__.includes("virtual:cssts.css") || __qin_css__.length > 0,
                  css: __qin_css__,
                  atomModule: __qin_atom__,
                  pluginName: __qin_plugin__.name
                });
                """.formatted(
                viteConfigImportSource(projectRoot),
                ovsPluginOptionsSource(projectRoot),
                sourceGlobal,
                idGlobal);
    }

    private String buildBatchWrapperSource(Path projectRoot) {
        String inputsGlobal = ovsTransformGlobalName(projectRoot, "batch_inputs");
        return """
                import vitePluginOvs from "vite-plugin-ovs";
                import { vitePluginOvsTransform } from "ovs-compiler";
                import { RuntimeStore, generateStylesCss, generateCsstsAtomModule } from "cssts-compiler";
                %s
                function __qinFlattenPlugins(value) {
                  const out = [];
                  for (const item of Array.isArray(value) ? value : []) {
                    if (!item) continue;
                    if (Array.isArray(item)) out.push(...__qinFlattenPlugins(item));
                    else out.push(item);
                  }
                  return out;
                }
                function __qinPluginName(plugin) {
                  return plugin && typeof plugin.name === "string" ? plugin.name : "";
                }
                function __qinResolveMaybePromise(value) {
                  if (value && value.then) {
                    let resolved = value;
                    value.then(next => { resolved = next; });
                    return resolved;
                  }
                  return value;
                }
                function __qinCallPluginHook(hook, context, ...args) {
                  if (!hook) return null;
                  const result = typeof hook === "function"
                    ? hook.call(context, ...args)
                    : hook.handler.call(context, ...args);
                  return __qinResolveMaybePromise(result);
                }
                const __qin_user_config__ = qinUserViteConfig || {};
                const __qin_plugins__ = __qinFlattenPlugins(__qin_user_config__.plugins);
                if (!__qin_plugins__.find(plugin => __qinPluginName(plugin) === "vite-plugin-ovs")) {
                  console.log("[QinOvsCompiler] instantiate vite-plugin-ovs");
                  __qin_plugins__.push(...__qinFlattenPlugins(vitePluginOvs(%s)));
                }
                const __qin_plugin__ = __qin_plugins__.find(plugin => plugin && plugin.name === "vite-plugin-ovs");
                if (!__qin_plugin__ || !__qin_plugin__.transform) {
                  throw new Error("vite-plugin-ovs transform hook not found");
                }
                const __qin_context__ = {
                  parse(code) { return {}; },
                  addWatchFile(file) {},
                  emitFile(file) { return "qin-ovs-file"; },
                  warn(message) {},
                  error(message) { throw new Error(String(message)); }
                };
                function __qinTransformOne(input) {
                  try {
                    RuntimeStore.clearUsedStyles();
                    const __qin_shared_styles__ = new Set();
                    let __qin_result__ = vitePluginOvsTransform(input.source, { globalStyles: __qin_shared_styles__ });
                    let __qin_code__ = typeof __qin_result__ === "string" ? __qin_result__ : __qin_result__ && __qin_result__.code;
                    if (typeof __qin_code__ !== "string" || __qin_code__.length === 0) {
                      __qin_result__ = __qinCallPluginHook(
                        __qin_plugin__.transform,
                        __qin_context__,
                        input.source,
                        input.id
                      );
                      __qin_code__ = typeof __qin_result__ === "string" ? __qin_result__ : __qin_result__ && __qin_result__.code;
                    }
                    if (typeof __qin_code__ !== "string" || __qin_code__.length === 0) {
                      throw new Error("vite-plugin-ovs transform returned empty code for " + input.id);
                    }
                    if (__qin_shared_styles__.size > 0 && !__qin_code__.includes("virtual:cssts.css")) {
                      __qin_code__ = "import 'virtual:cssts.css'\\n" + __qin_code__;
                    }
                    RuntimeStore.addUsedStyles(__qin_shared_styles__);
                    const __qin_css__ = __qin_shared_styles__.size > 0 ? generateStylesCss() : "";
                    const __qin_atom__ = __qin_shared_styles__.size > 0 ? generateCsstsAtomModule() : "";
                    return {
                      code: __qin_code__,
                      hasStyles: __qin_code__.includes("virtual:cssts.css") || __qin_css__.length > 0,
                      css: __qin_css__,
                      atomModule: __qin_atom__,
                      pluginName: __qin_plugin__.name
                    };
                  } catch (error) {
                    const message = error && error.message ? error.message : String(error);
                    throw new Error("vite-plugin-ovs transform failed for " + input.id + ": " + message);
                  }
                }
                const __qin_inputs__ = globalThis.%s;
                __qin_inputs__.map(input => __qinTransformOne(input));
                """.formatted(
                viteConfigImportSource(projectRoot),
                ovsPluginOptionsSource(projectRoot),
                inputsGlobal);
    }

    private String renderViteId(Path projectRoot, Path moduleFile, String semanticModule) {
        if (moduleFile == null) {
            return "/qin/app/Inline.ovs";
        }
        if (semanticModule != null && !semanticModule.isBlank()) {
            return "/qin/" + semanticModule.replace('\\', '/');
        }
        Path normalizedRoot = projectRoot.toAbsolutePath().normalize();
        Path normalizedModule = moduleFile.toAbsolutePath().normalize();
        try {
            String relative = normalizedRoot.relativize(normalizedModule).toString().replace('\\', '/');
            if (!relative.startsWith("/")) {
                relative = "/" + relative;
            }
            return "/qin" + relative;
        } catch (IllegalArgumentException ignored) {
            return normalizedModule.toString().replace('\\', '/');
        }
    }

    private String ovsTransformGlobalName(Path projectRoot, String suffix) {
        String identity = projectRoot.toAbsolutePath().normalize().toString().replace('\\', '/');
        return "__qin_ovs_transform_" + shortSha256(identity) + "_" + suffix;
    }

    private String shortSha256(String text) {
        MessageDigest digest = newSha256Digest();
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash, 0, 8);
    }

    private String semanticModuleFile(Path projectRoot, Path moduleFile) {
        if (moduleFile == null) {
            return "";
        }
        Path normalizedRoot = projectRoot.toAbsolutePath().normalize();
        Path normalizedModule = moduleFile.toAbsolutePath().normalize();
        try {
            return normalizedRoot.relativize(normalizedModule).toString().replace('\\', '/');
        } catch (IllegalArgumentException ignored) {
            return normalizedModule.getFileName() == null
                    ? normalizedModule.toString().replace('\\', '/')
                    : normalizedModule.getFileName().toString();
        }
    }

    private Path transformCacheRoot(Path projectRoot) {
        Path workspaceRoot = locateWorkspaceRoot();
        if (workspaceRoot != null) {
            return workspaceRoot.resolve("qin").toAbsolutePath().normalize();
        }
        return projectRoot.toAbsolutePath().normalize();
    }

    private String semanticRoot(Path projectRoot) {
        Path root = projectRoot.toAbsolutePath().normalize();
        if (isTempQinSmokeRoot(root)) {
            return "qin-runtime-core-smoke";
        }
        return root.toString();
    }

    private boolean isTempQinSmokeRoot(Path root) {
        Path fileName = root.getFileName();
        return fileName != null && fileName.toString().startsWith("qin-");
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

    private String viteConfigImportSource(Path projectRoot) {
        Path config = findQinConfig(projectRoot);
        if (!java.nio.file.Files.isRegularFile(config)) {
            return "const qinUserViteConfig = null;";
        }
        try {
            String text = java.nio.file.Files.readString(config);
            if (!text.contains("export default")) {
                return "const qinUserViteConfig = null;";
            }
            if (!requiresFullOvsConfigEvaluation(text)) {
                return "const qinUserViteConfig = null;";
            }
        } catch (Exception ignored) {
            return "const qinUserViteConfig = null;";
        }
        Path wrapperDir = projectRoot.toAbsolutePath().normalize()
                .resolve(".qin")
                .resolve("runtime")
                .resolve("npm-host")
                .normalize();
        String relative = wrapperDir.relativize(config.toAbsolutePath().normalize()).toString().replace('\\', '/');
        if (!relative.startsWith(".")) {
            relative = "./" + relative;
        }
        return "import qinUserViteConfig from "
                + QinJsPackageRunner.renderJsLiteral(relative)
                + ";";
    }

    private boolean requiresFullOvsConfigEvaluation(String configText) {
        return configText.contains("vite-plugin-ovs")
                && configText.contains("transform(");
    }

    private String ovsPluginOptionsSource(Path projectRoot) {
        Path config = findQinConfig(projectRoot);
        if (!java.nio.file.Files.isRegularFile(config)) {
            return "{ cssts: { classPrefix: \"cmp-\" } }";
        }
        try {
            String text = java.nio.file.Files.readString(config);
            Matcher matcher = CLASS_PREFIX_PATTERN.matcher(text);
            if (matcher.find()) {
                return "{ cssts: { classPrefix: "
                        + QinJsPackageRunner.renderJsLiteral(matcher.group(2))
                        + " } }";
            }
        } catch (Exception ignored) {
            return "{ cssts: { classPrefix: \"cmp-\" } }";
        }
        return "{ cssts: { classPrefix: \"cmp-\" } }";
    }

    private Path findQinConfig(Path projectRoot) {
        return projectRoot.toAbsolutePath().normalize().resolve("qin.config.js");
    }

    @SuppressWarnings("unchecked")
    private QinOvsCompileResult decodeResult(Object result) {
        if (!(result instanceof Map<?, ?> rawMap)) {
            throw new IllegalStateException("vite-plugin-ovs did not return an object payload: " + result);
        }
        Map<String, Object> map = (Map<String, Object>) rawMap;
        Object code = map.get("code");
        Object hasStyles = map.get("hasStyles");
        Object css = map.get("css");
        Object atomModule = map.get("atomModule");
        if (!(code instanceof String codeText)) {
            throw new IllegalStateException("vite-plugin-ovs result missing code string: " + result);
        }
        boolean styles = Boolean.TRUE.equals(hasStyles);
        return new QinOvsCompileResult(
                codeText,
                styles,
                css instanceof String cssText ? cssText : "",
                atomModule instanceof String atomText ? atomText : "");
    }

    private List<QinOvsCompileResult> decodeBatchResult(Object result, int expectedSize) {
        List<?> rawResults;
        if (result instanceof List<?> list) {
            rawResults = list;
        } else if (result instanceof Object[] array) {
            rawResults = List.of(array);
        } else {
            throw new IllegalStateException("vite-plugin-ovs batch did not return an array payload: " + result);
        }
        if (rawResults.size() != expectedSize) {
            throw new IllegalStateException(
                    "vite-plugin-ovs batch returned " + rawResults.size() + " result(s), expected " + expectedSize);
        }
        List<QinOvsCompileResult> decoded = new java.util.ArrayList<>();
        for (Object rawResult : rawResults) {
            decoded.add(decodeResult(rawResult));
        }
        return decoded;
    }

    private Map<String, String> encodeDiskCache(QinOvsCompileResult result) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("code", result.code());
        values.put("hasStyles", String.valueOf(result.hasStyles()));
        values.put("css", result.css());
        values.put("atomModule", result.atomModule());
        return values;
    }

    private QinOvsCompileResult decodeDiskCache(Map<String, String> values) {
        String code = values.get("code");
        if (code == null || code.isBlank()) {
            return null;
        }
        return new QinOvsCompileResult(
                code,
                Boolean.parseBoolean(values.getOrDefault("hasStyles", "false")),
                values.getOrDefault("css", ""),
                values.getOrDefault("atomModule", ""));
    }

    private String readConfigSource(Path projectRoot) {
        Path config = findQinConfig(projectRoot);
        if (!java.nio.file.Files.isRegularFile(config)) {
            return "";
        }
        try {
            return java.nio.file.Files.readString(config);
        } catch (Exception ignored) {
            return "";
        }
    }

    private String transformToolchainFingerprint(Path projectRoot, String configSource) throws Exception {
        Map<String, Path> overrides = readPackageOverrides(projectRoot, configSource);
        Map<String, Path> workspacePackages = indexWorkspaceToolchainPackages();
        MessageDigest digest = newSha256Digest();
        updateClassResourceDigest(digest, QinOvsCompiler.class);
        updateClassResourceDigest(digest, QinJsPackageRunner.class);
        for (String packageName : TRANSFORM_TOOLCHAIN_PACKAGES) {
            digest.update(packageName.getBytes(StandardCharsets.UTF_8));
            digest.update((byte) '=');
            Path packageDir = overrides.get(packageName);
            if (packageDir == null) {
                packageDir = workspacePackages.get(packageName);
            }
            if (packageDir == null) {
                digest.update((byte) '-');
            } else {
                digest.update(packageDir.toString().replace('\\', '/').getBytes(StandardCharsets.UTF_8));
                digest.update((byte) 0);
                updateDirectoryDigest(digest, packageDir);
            }
            digest.update((byte) '\n');
        }
        return HexFormat.of().formatHex(digest.digest());
    }

    private void updateClassResourceDigest(MessageDigest digest, Class<?> type) throws Exception {
        digest.update(("class:" + type.getName()).getBytes(StandardCharsets.UTF_8));
        digest.update((byte) 0);
        String resourceName = "/" + type.getName().replace('.', '/') + ".class";
        try (InputStream input = type.getResourceAsStream(resourceName)) {
            if (input == null) {
                digest.update("missing".getBytes(StandardCharsets.UTF_8));
            } else {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = input.read(buffer)) != -1) {
                    digest.update(buffer, 0, read);
                }
            }
        }
        digest.update((byte) '\n');
    }

    private Map<String, Path> indexWorkspaceToolchainPackages() {
        Path workspaceRoot = locateWorkspaceRoot();
        if (workspaceRoot == null) {
            return Map.of();
        }
        Map<String, Path> packages = new LinkedHashMap<>();
        registerWorkspacePackage(packages, "ovs-compiler", workspaceRoot.resolve("ovsjs").resolve("ovs").resolve("ovs-compiler"));
        registerWorkspacePackage(packages, "vite-plugin-ovs", workspaceRoot.resolve("ovsjs").resolve("vite-plugin-ovs"));
        registerWorkspacePackage(packages, "cssts-compiler", workspaceRoot.resolve("cssts").resolve("cssts").resolve("cssts-compiler"));
        registerWorkspacePackage(packages, "@qin/generated-qin-parser-ts", workspaceRoot.resolve("qin")
                .resolve("packages").resolve("qin-language").resolve("generated").resolve("qin-parser-ts"));
        registerWorkspacePackage(packages, "slime-generator", workspaceRoot.resolve("slime").resolve("slime-generator"));
        registerWorkspacePackage(packages, "slime-ast", workspaceRoot.resolve("slime").resolve("slime-ast"));
        return packages;
    }

    private void registerWorkspacePackage(Map<String, Path> packages, String packageName, Path packageDir) {
        Path normalized = packageDir.toAbsolutePath().normalize();
        if (Files.isRegularFile(normalized.resolve("package.json"))) {
            packages.put(packageName, normalized);
        }
    }

    private Map<String, Path> readPackageOverrides(Path projectRoot, String configSource) {
        if (configSource == null || configSource.isBlank()) {
            return Map.of();
        }
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
    }

    private void updateDirectoryDigest(MessageDigest digest, Path root) throws Exception {
        List<Path> files = new ArrayList<>();
        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if (!dir.toAbsolutePath().normalize().equals(root.toAbsolutePath().normalize())
                        && isIgnoredToolchainPath(root, dir)) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (attrs.isRegularFile() && !isIgnoredToolchainPath(root, file)) {
                    files.add(file.toAbsolutePath().normalize());
                }
                return FileVisitResult.CONTINUE;
            }
        });
        files.sort(Path::compareTo);
        byte[] buffer = new byte[8192];
        for (Path file : files) {
            Path relative = root.relativize(file.toAbsolutePath().normalize());
            digest.update(relative.toString().replace('\\', '/').getBytes(StandardCharsets.UTF_8));
            digest.update((byte) 0);
            try (var input = Files.newInputStream(file)) {
                int read;
                while ((read = input.read(buffer)) >= 0) {
                    digest.update(buffer, 0, read);
                }
            }
            digest.update((byte) 0);
        }
    }

    private boolean isIgnoredToolchainPath(Path root, Path path) {
        Path relative = root.relativize(path.toAbsolutePath().normalize());
        for (Path part : relative) {
            if (IGNORED_TOOLCHAIN_DIRS.contains(part.toString())) {
                return true;
            }
        }
        return false;
    }

    private MessageDigest newSha256Digest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException error) {
            throw new IllegalStateException("SHA-256 is not available", error);
        }
    }

    public record QinOvsCompileResult(
            String code,
            boolean hasStyles,
            String css,
            String atomModule) {
    }

    private record BatchCompileInput(
            Path moduleFile,
            String source,
            String semanticModule,
            String diskKey,
            CacheKey cacheKey) {
    }

    private record CacheKey(Path projectRoot, Path moduleFile, String source, String toolchainFingerprint) {
        private CacheKey {
            Objects.requireNonNull(projectRoot, "projectRoot cannot be null");
            Objects.requireNonNull(source, "source cannot be null");
            Objects.requireNonNull(toolchainFingerprint, "toolchainFingerprint cannot be null");
        }
    }
}
