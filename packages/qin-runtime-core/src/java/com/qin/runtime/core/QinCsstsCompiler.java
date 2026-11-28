package com.qin.runtime.core;

import com.qin.lang.sema.esm.QinEsmModuleBindings;
import com.qin.parser.CsstsAtomNames;
import com.qin.parser.QinParserFacade;
import com.slime.generator.SlimeGenerator.MappedCode;
import com.slime.generator.SlimeGenerator.SourceMapping;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Java-native CSSTS compiler and its source/result cache. */
public final class QinCsstsCompiler {
    private static final int MAX_CACHE_ENTRIES = 64;
    private final Map<CacheKey, QinCsstsCompileResult> cache = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<CacheKey, QinCsstsCompileResult> eldest) {
            return size() > MAX_CACHE_ENTRIES;
        }
    };

    public QinCsstsCompileResult compile(Path projectRoot, String source) throws Exception {
        Path root = projectRoot.toAbsolutePath().normalize();
        String input = source == null ? "" : source;
        String config = readConfig(root);
        String fingerprint = toolchainFingerprint();
        CacheKey key = new CacheKey(root, input, config, fingerprint);
        synchronized (cache) {
            QinCsstsCompileResult existing = cache.get(key);
            if (existing != null) return existing;
        }

        Path cacheRoot = transformCacheRoot(root);
        String diskKey = QinFrontendTransformDiskCache.keyMaterial(
                semanticRoot(root),
                "route=java-cssts-v1\ntoolchain=" + fingerprint + "\nsource=" + input,
                config);
        QinCsstsCompileResult diskResult = QinFrontendTransformDiskCache.read(cacheRoot, root, "cssts", diskKey)
                .map(this::decodeDiskCache)
                .orElse(null);
        if (diskResult != null) {
            synchronized (cache) {
                cache.put(key, diskResult);
            }
            return diskResult;
        }

        QinCsstsCompileResult result = QinCsstsJavaCompiler.compile(input, config, true);
        QinFrontendTransformDiskCache.write(cacheRoot, root, "cssts", diskKey, encodeDiskCache(result));
        synchronized (cache) {
            cache.put(key, result);
        }
        return result;
    }

    private String readConfig(Path projectRoot) throws Exception {
        Path path = projectRoot.resolve("qin.config.js");
        return Files.isRegularFile(path) ? Files.readString(path) : "";
    }

    private Path transformCacheRoot(Path projectRoot) {
        Path workspace = locateWorkspaceRoot();
        return workspace == null ? projectRoot : workspace.resolve("qin").toAbsolutePath().normalize();
    }

    private String semanticRoot(Path projectRoot) {
        Path fileName = projectRoot.getFileName();
        return fileName != null && fileName.toString().startsWith("qin-")
                ? "qin-runtime-core-smoke"
                : projectRoot.toString();
    }

    private Path locateWorkspaceRoot() {
        Path current = Path.of("").toAbsolutePath().normalize();
        while (current != null) {
            if (Files.isDirectory(current.resolve("qin")) && Files.isDirectory(current.resolve("slime"))) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    private String toolchainFingerprint() throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update("qin-cssts-java-pipeline-v2".getBytes(StandardCharsets.UTF_8));
        digest.update(QinParserFacade.runtimeFingerprint().getBytes(StandardCharsets.UTF_8));
        updateClassDigest(digest, QinCsstsCompiler.class);
        updateClassDigest(digest, QinCsstsJavaCompiler.class);
        updateClassDigest(digest, QinEsmSourceMap.class);
        updateClassDigest(digest, QinFrontendModuleMetadata.class);
        QinFrontendModuleMetadata.updateBindingFingerprint(digest);
        updateClassDigest(digest, QinConfigStaticValueReader.class);
        updateClassDigest(digest, com.qin.parser.QinOvsCstToAst.class);
        updateClassDigest(digest, CsstsAtomNames.class);
        return HexFormat.of().formatHex(digest.digest());
    }

    private void updateClassDigest(MessageDigest digest, Class<?> type) throws Exception {
        digest.update(type.getName().getBytes(StandardCharsets.UTF_8));
        digest.update((byte) 0);
        String resource = "/" + type.getName().replace('.', '/') + ".class";
        try (InputStream input = type.getResourceAsStream(resource)) {
            if (input == null) {
                digest.update("missing".getBytes(StandardCharsets.UTF_8));
            } else {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = input.read(buffer)) >= 0) digest.update(buffer, 0, count);
            }
        }
        digest.update((byte) '\n');
    }

    private Map<String, String> encodeDiskCache(QinCsstsCompileResult result) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("code", result.code());
        values.put("rawCode", result.rawCode());
        values.put("hasStyles", Boolean.toString(result.hasStyles()));
        values.put("css", result.css());
        values.put("atomModule", result.atomModule());
        values.put("mappings", QinEsmSourceMap.cacheMappings(result.mappings()));
        QinFrontendModuleMetadata.writeBindings(values, result.moduleBindings());
        return values;
    }

    private QinCsstsCompileResult decodeDiskCache(Map<String, String> values) {
        String code = values.get("code");
        String rawCode = values.get("rawCode");
        String mappings = values.get("mappings");
        if (code == null || rawCode == null || mappings == null) return null;
        List<SourceMapping> offsets = QinEsmSourceMap.cachedMappings(mappings, code.length());
        var bindings = QinFrontendModuleMetadata.readBindings(values);
        if (offsets == null || bindings == null) return null;
        return new QinCsstsCompileResult(code, rawCode,
                Boolean.parseBoolean(values.getOrDefault("hasStyles", "false")),
                values.getOrDefault("css", ""), values.getOrDefault("atomModule", ""),
                offsets, bindings);
    }

    private record CacheKey(Path projectRoot, String source, String config, String fingerprint) {
        private CacheKey {
            Objects.requireNonNull(projectRoot);
            Objects.requireNonNull(source);
            Objects.requireNonNull(config);
            Objects.requireNonNull(fingerprint);
        }
    }

    public record QinCsstsCompileResult(String code, String rawCode, boolean hasStyles, String css, String atomModule,
                                        List<SourceMapping> mappings, QinEsmModuleBindings moduleBindings) {
        public QinCsstsCompileResult(String code, String rawCode, boolean hasStyles, String css, String atomModule) {
            this(code, rawCode, hasStyles, css, atomModule, List.of());
        }

        public QinCsstsCompileResult(String code, String rawCode, boolean hasStyles, String css, String atomModule,
                                     List<SourceMapping> mappings) {
            this(code, rawCode, hasStyles, css, atomModule, mappings, QinEsmModuleBindings.EMPTY);
        }

        MappedCode mappedCode() {
            return new MappedCode(code, mappings);
        }
    }
}
