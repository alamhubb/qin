package com.qin.runtime.core;

import com.qin.lang.sema.esm.QinEsmModuleBindings;
import com.slime.generator.SlimeGenerator.ModuleLayout;

import com.slime.generator.SlimeGenerator.MappedCode;
import com.slime.generator.SlimeGenerator.SourceMapping;

import com.qin.parser.QinParserFacade;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class QinOvsCompiler {
    private static final int MAX_CACHE_ENTRIES = 64;
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
        String toolchainFingerprint = transformToolchainFingerprint(configSource);
        String semanticRoot = semanticRoot(normalizedRoot);
        String semanticModule = semanticModuleFile(normalizedRoot, normalizedModuleFile);
        return QinFrontendTransformDiskCache.keyMaterial(
                semanticRoot,
                "module=" + semanticModule
                        + "\ntoolchain=" + toolchainFingerprint
                        + "\nsource=" + (source == null ? "" : source),
                configSource);
    }

    Map<Path, String> frontendCacheIdentities(Path projectRoot, Map<Path, String> modules) throws Exception {
        if (modules == null || modules.isEmpty()) {
            return Map.of();
        }
        Path normalizedRoot = projectRoot.toAbsolutePath().normalize();
        String configSource = readConfigSource(normalizedRoot);
        String toolchainFingerprint = transformToolchainFingerprint(configSource);
        String semanticRoot = semanticRoot(normalizedRoot);
        Map<Path, String> identities = new LinkedHashMap<>();
        for (Map.Entry<Path, String> entry : modules.entrySet()) {
            Path normalizedModuleFile = entry.getKey() == null
                    ? null
                    : entry.getKey().toAbsolutePath().normalize();
            String source = entry.getValue() == null ? "" : entry.getValue();
            String semanticModule = semanticModuleFile(normalizedRoot, normalizedModuleFile);
            String identity = QinFrontendTransformDiskCache.keyMaterial(
                    semanticRoot,
                    "module=" + semanticModule
                            + "\ntoolchain=" + toolchainFingerprint
                            + "\nsource=" + source,
                    configSource);
            identities.put(normalizedModuleFile, identity);
        }
        return identities;
    }

    public QinOvsCompileResult compile(Path projectRoot, Path moduleFile, String source) throws Exception {
        Path normalizedRoot = projectRoot.toAbsolutePath().normalize();
        Path normalizedModuleFile = moduleFile == null
                ? null
                : moduleFile.toAbsolutePath().normalize();
        source = source == null ? "" : source;
        String configSource = readConfigSource(normalizedRoot);
        String toolchainFingerprint = transformToolchainFingerprint(configSource);
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
            QinOvsCompileResult decoded = QinOvsJavaCompiler.compile(
                    source, QinCsstsJavaCompiler.parseOvsProjectOptions(configSource), true);
            QinFrontendTransformDiskCache.write(transformCacheRoot, normalizedRoot, "ovs", diskKey, encodeDiskCache(decoded));
            synchronized (cache) {
                cache.put(key, decoded);
            }
            return decoded;
        } catch (Exception error) {
            throw new IllegalStateException("Qin ovs-compiler transform failed for " + projectRoot, error);
        }
    }

    public Map<Path, QinOvsCompileResult> compileAll(Path projectRoot, Map<Path, String> modules) throws Exception {
        if (modules == null || modules.isEmpty()) {
            return Map.of();
        }

        QinPhaseTimer profile = QinPhaseTimer.start("ovs-compile-all");
        Path normalizedRoot = projectRoot.toAbsolutePath().normalize();
        String configSource = readConfigSource(normalizedRoot);
        profile.checkpoint("read config", normalizedRoot.toString());
        String toolchainFingerprint = transformToolchainFingerprint(configSource);
        profile.checkpoint("Java OVS toolchain fingerprint");
        Path transformCacheRoot = transformCacheRoot(normalizedRoot);
        String semanticRoot = semanticRoot(normalizedRoot);
        Map<Path, QinOvsCompileResult> results = new LinkedHashMap<>();
        List<BatchCompileInput> toTransform = new java.util.ArrayList<>();
        int memoryHits = 0;
        int diskHits = 0;

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
                memoryHits++;
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
                diskHits++;
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
        profile.checkpoint("scan caches",
                "modules=" + modules.size()
                        + ", memoryHits=" + memoryHits
                        + ", diskHits=" + diskHits
                        + ", toTransform=" + toTransform.size());

        if (toTransform.isEmpty()) {
            profile.done("all cached");
            return results;
        }

        try {
            profile.checkpoint("java OVS transform", "toTransform=" + toTransform.size());
            var options = QinCsstsJavaCompiler.parseOvsProjectOptions(configSource);
            profile.checkpoint("prepare CSSTS options");
            for (BatchCompileInput input : toTransform) {
                QinOvsCompileResult decoded = QinOvsJavaCompiler.compile(input.source(), options, true);
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
            profile.done("written=" + toTransform.size());
            return results;
        } catch (Exception error) {
            throw new IllegalStateException("Qin ovs-compiler batch transform failed for " + projectRoot, error);
        }
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

    private Path findQinConfig(Path projectRoot) {
        return projectRoot.toAbsolutePath().normalize().resolve("qin.config.js");
    }

    private Map<String, String> encodeDiskCache(QinOvsCompileResult result) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("code", result.code());
        values.put("hasStyles", String.valueOf(result.hasStyles()));
        values.put("css", result.css());
        values.put("atomModule", result.atomModule());
        values.put("mappings", QinEsmSourceMap.cacheMappings(result.mappings()));
        QinFrontendModuleMetadata.writeBindings(values, result.moduleBindings());
        QinFrontendModuleMetadata.writeLayout(values, result.moduleLayout());
        return values;
    }

    private QinOvsCompileResult decodeDiskCache(Map<String, String> values) {
        String code = values.get("code");
        String mappings = values.get("mappings");
        if (code == null || mappings == null) {
            return null;
        }
        List<SourceMapping> offsets = QinEsmSourceMap.cachedMappings(mappings, code.length());
        var bindings = QinFrontendModuleMetadata.readBindings(values);
        var layout = QinFrontendModuleMetadata.readLayout(values, code);
        if (offsets == null || bindings == null || layout == null) return null;
        return new QinOvsCompileResult(
                code,
                Boolean.parseBoolean(values.getOrDefault("hasStyles", "false")),
                values.getOrDefault("css", ""),
                values.getOrDefault("atomModule", ""),
                offsets, bindings, layout);
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

    private String transformToolchainFingerprint(String configSource) throws Exception {
        MessageDigest digest = newSha256Digest();
        digest.update(QinParserFacade.runtimeFingerprint().getBytes(StandardCharsets.UTF_8));
        digest.update((configSource == null ? "" : configSource).getBytes(StandardCharsets.UTF_8));
        updateClassResourceDigest(digest, QinConfigStaticValueReader.class);
        updateClassResourceDigest(digest, QinOvsCompiler.class);
        updateClassResourceDigest(digest, QinOvsJavaCompiler.class);
        updateClassResourceDigest(digest, com.qin.parser.QinOvsCstToAst.class);
        updateClassResourceDigest(digest, QinCsstsJavaCompiler.class);
        updateClassResourceDigest(digest, QinEsmSourceMap.class);
        updateClassResourceDigest(digest, QinFrontendModuleMetadata.class);
        QinFrontendModuleMetadata.updateBindingFingerprint(digest);
        updateClassResourceDigest(digest, com.qin.parser.CsstsAtomNames.class);
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
            String atomModule,
            List<SourceMapping> mappings,
            QinEsmModuleBindings moduleBindings,
            ModuleLayout moduleLayout) {
        public QinOvsCompileResult(String code, boolean hasStyles, String css, String atomModule) {
            this(code, hasStyles, css, atomModule, List.of());
        }

        public QinOvsCompileResult(String code, boolean hasStyles, String css, String atomModule,
                                   List<SourceMapping> mappings) {
            this(code, hasStyles, css, atomModule, mappings, QinEsmModuleBindings.EMPTY, ModuleLayout.EMPTY);
        }

        MappedCode mappedCode() {
            return new MappedCode(code, mappings);
        }
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
