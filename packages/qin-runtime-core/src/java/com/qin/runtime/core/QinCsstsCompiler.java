package com.qin.runtime.core;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileVisitResult;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QinCsstsCompiler {
    private static final Pattern CSSTS_MERGE_PATTERN = Pattern.compile("cssts\\.merge\\(([^)]*)\\)");
    private static final Pattern CSS_CLASS_RULE_PATTERN = Pattern.compile("\\.cssts_([A-Za-z0-9_-]+)\\s*\\{\\s*([^:}]+):");
    private static final int MAX_CACHE_ENTRIES = 64;
    private static final List<String> TRANSFORM_TOOLCHAIN_PACKAGES = List.of(
            "cssts-compiler",
            "@qin/generated-qin-parser-ts",
            "slime-parser",
            "@qin/generated-slime-parser-ts",
            "slime-generator",
            "slime-ast");
    private static final Set<String> IGNORED_TOOLCHAIN_DIRS = Set.of(
            ".git", ".idea", ".qin", "node_modules", "build", "target", "out");
    private static final Pattern QIN_PACKAGE_OVERRIDES_BLOCK = Pattern.compile(
            "packageOverrides\\s*:\\s*\\{([^}]*)\\}",
            Pattern.DOTALL);
    private static final Pattern QIN_STRING_FIELD = Pattern.compile("[\"']([^\"']+)[\"']\\s*:\\s*[\"']([^\"']*)[\"']");

    private final QinJsPackageRunner packageRunner = new QinJsPackageRunner();
    private final Map<CacheKey, QinCsstsCompileResult> cache = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<CacheKey, QinCsstsCompileResult> eldest) {
            return size() > MAX_CACHE_ENTRIES;
        }
    };
    private final Map<Path, DirectoryDigestCacheEntry> directoryDigestCache = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Path, DirectoryDigestCacheEntry> eldest) {
            return size() > MAX_CACHE_ENTRIES;
        }
    };
    private int directoryDigestCacheHits;
    private int directoryDigestContentHashes;

    public QinCsstsCompileResult compile(Path projectRoot, String source) throws Exception {
        Path normalizedRoot = projectRoot.toAbsolutePath().normalize();
        source = source == null ? "" : source;
        String configSource = readConfigSource(normalizedRoot);
        String toolchainFingerprint = transformToolchainFingerprint(normalizedRoot, configSource);
        CacheKey key = new CacheKey(normalizedRoot, source, toolchainFingerprint);
        synchronized (cache) {
            QinCsstsCompileResult cached = cache.get(key);
            if (cached != null) {
                return cached;
            }
        }
        Path transformCacheRoot = transformCacheRoot(normalizedRoot);
        String diskKey = QinFrontendTransformDiskCache.keyMaterial(
                semanticRoot(normalizedRoot),
                "toolchain=" + toolchainFingerprint + "\nsource=" + source,
                configSource);
        QinCsstsCompileResult diskCached = QinFrontendTransformDiskCache.read(
                        transformCacheRoot,
                        normalizedRoot,
                        "cssts",
                        diskKey)
                .map(this::decodeDiskCache)
                .orElse(null);
        if (diskCached != null) {
            synchronized (cache) {
                cache.put(key, diskCached);
            }
            System.out.println("[QinCsstsCompiler] transform disk cache hit");
            return diskCached;
        }
        Object result = packageRunner.runModuleSource(
                normalizedRoot,
                buildWrapperSource(source),
                "cssts_compiler");
        QinCsstsCompileResult decoded = decodeResult(result);
        QinFrontendTransformDiskCache.write(transformCacheRoot, normalizedRoot, "cssts", diskKey, encodeDiskCache(decoded));
        synchronized (cache) {
            cache.put(key, decoded);
        }
        return decoded;
    }

    private String buildWrapperSource(String source) {
        String sourceLiteral = QinJsPackageRunner.renderJsLiteral(source);
        return """
                import { CsstsInit, RuntimeStore, transformCssTs, generateStylesCss, generateCsstsAtomModule } from "cssts-compiler";
                CsstsInit.init({ dts: false });
                const __qin_result__ = transformCssTs(%s);
                const __qin_css__ = generateStylesCss.length > 0
                  ? generateStylesCss(RuntimeStore.getUsedStyles())
                  : generateStylesCss();
                const __qin_atom__ = generateCsstsAtomModule.length > 0
                  ? generateCsstsAtomModule(RuntimeStore.getUsedStyles())
                  : generateCsstsAtomModule();
                ({
                  code: __qin_result__.code,
                  hasStyles: __qin_result__.hasStyles,
                  css: __qin_css__,
                  atomModule: __qin_atom__
                });
                """.formatted(sourceLiteral);
    }

    @SuppressWarnings("unchecked")
    private QinCsstsCompileResult decodeResult(Object result) {
        if (!(result instanceof Map<?, ?> rawMap)) {
            throw new IllegalStateException("cssts-compiler did not return an object payload: " + result);
        }
        Map<String, Object> map = (Map<String, Object>) rawMap;
        Object code = map.get("code");
        Object hasStyles = map.get("hasStyles");
        Object css = map.get("css");
        Object atomModule = map.get("atomModule");
        if (!(code instanceof String codeText)) {
            throw new IllegalStateException("cssts-compiler result missing code string: " + result);
        }
        boolean styles = Boolean.TRUE.equals(hasStyles);
        String cssText = css instanceof String text ? text : "";
        String atomText = atomModule instanceof String text ? text : "";
        String normalizedAtomText = ensureAtomModule(codeText, cssText, atomText);
        String normalizedCode = normalizeCsstsAtomReferences(codeText, extractAtomNames(codeText));
        return new QinCsstsCompileResult(
                normalizedCode,
                codeText,
                styles,
                cssText,
                normalizedAtomText);
    }

    private Map<String, String> encodeDiskCache(QinCsstsCompileResult result) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("code", result.code());
        values.put("rawCode", result.rawCode());
        values.put("hasStyles", String.valueOf(result.hasStyles()));
        values.put("css", result.css());
        values.put("atomModule", result.atomModule());
        return values;
    }

    private QinCsstsCompileResult decodeDiskCache(Map<String, String> values) {
        String code = values.get("code");
        String rawCode = values.get("rawCode");
        if (code == null || code.isBlank() || rawCode == null || rawCode.isBlank()) {
            return null;
        }
        return new QinCsstsCompileResult(
                code,
                rawCode,
                Boolean.parseBoolean(values.getOrDefault("hasStyles", "false")),
                values.getOrDefault("css", ""),
                values.getOrDefault("atomModule", ""));
    }

    private String readConfigSource(Path projectRoot) {
        Path config = projectRoot.toAbsolutePath().normalize().resolve("qin.config.js");
        if (!java.nio.file.Files.isRegularFile(config)) {
            return "";
        }
        try {
            return java.nio.file.Files.readString(config);
        } catch (Exception ignored) {
            return "";
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

    private String transformToolchainFingerprint(Path projectRoot, String configSource) throws Exception {
        Map<String, Path> overrides = readPackageOverrides(projectRoot, configSource);
        Map<String, Path> workspacePackages = indexWorkspaceToolchainPackages();
        MessageDigest digest = newSha256Digest();
        updateClassResourceDigest(digest, QinCsstsCompiler.class);
        updateClassResourceDigest(digest, QinJsPackageRunner.class);
        digest.update("module-class-toolchain".getBytes(StandardCharsets.UTF_8));
        digest.update((byte) 0);
        digest.update(QinInMemoryJvmRunner.moduleClassToolchainFingerprintValue().getBytes(StandardCharsets.UTF_8));
        digest.update((byte) '\n');
        digest.update("dynamic-semantic-policy".getBytes(StandardCharsets.UTF_8));
        digest.update((byte) 0);
        digest.update(QinDynamicSemanticPolicyFingerprint.current().getBytes(StandardCharsets.UTF_8));
        digest.update((byte) '\n');
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
        registerWorkspacePackage(packages, "cssts-compiler", workspaceRoot.resolve("cssts").resolve("cssts").resolve("cssts-compiler"));
        registerWorkspacePackage(packages, "@qin/generated-qin-parser-ts", workspaceRoot.resolve("qin")
                .resolve("packages").resolve("qin-language").resolve("generated").resolve("qin-parser-ts"));
        registerWorkspacePackage(packages, "@qin/generated-slime-parser-ts", workspaceRoot.resolve("qin")
                .resolve("examples").resolve("ovs-cssts-demos").resolve("qin-ovs-cssts-generated-ts-slime-demo")
                .resolve("packages").resolve("slime-parser"));
        registerWorkspacePackage(packages, "slime-parser", workspaceRoot.resolve("qin")
                .resolve("examples").resolve("ovs-cssts-demos").resolve("qin-ovs-cssts-generated-ts-slime-demo")
                .resolve("packages").resolve("slime-parser"));
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
        digest.update(directoryDigest(root).getBytes(StandardCharsets.UTF_8));
    }

    private synchronized String directoryDigest(Path root) throws Exception {
        root = root.toAbsolutePath().normalize();
        DirectorySnapshot snapshot = directorySnapshot(root);
        DirectoryDigestCacheEntry cached = directoryDigestCache.get(root);
        if (cached != null && cached.snapshot().equals(snapshot)) {
            directoryDigestCacheHits++;
            return cached.digest();
        }

        String digest = hashDirectoryContent(root, snapshot);
        directoryDigestContentHashes++;
        directoryDigestCache.put(root, new DirectoryDigestCacheEntry(snapshot, digest));
        return digest;
    }

    private DirectorySnapshot directorySnapshot(Path root) throws Exception {
        List<DirectoryFileSnapshot> files = new ArrayList<>();
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
                    Path normalized = file.toAbsolutePath().normalize();
                    files.add(new DirectoryFileSnapshot(
                            root.relativize(normalized).toString().replace('\\', '/'),
                            attrs.size(),
                            attrs.lastModifiedTime().to(TimeUnit.NANOSECONDS),
                            Objects.toString(attrs.fileKey(), "")));
                }
                return FileVisitResult.CONTINUE;
            }
        });
        files.sort(Comparator.comparing(DirectoryFileSnapshot::relativePath));
        return new DirectorySnapshot(files);
    }

    private String hashDirectoryContent(Path root, DirectorySnapshot snapshot) throws Exception {
        MessageDigest digest = newSha256Digest();
        byte[] buffer = new byte[8192];
        for (DirectoryFileSnapshot file : snapshot.files()) {
            digest.update(file.relativePath().getBytes(StandardCharsets.UTF_8));
            digest.update((byte) 0);
            try (var input = Files.newInputStream(root.resolve(file.relativePath()).normalize())) {
                int read;
                while ((read = input.read(buffer)) >= 0) {
                    digest.update(buffer, 0, read);
                }
            }
            digest.update((byte) 0);
        }
        return HexFormat.of().formatHex(digest.digest());
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

    private String normalizeCsstsAtomReferences(String code, Set<String> atomNames) {
        if (code == null || code.isBlank() || atomNames == null || atomNames.isEmpty()) {
            return code == null ? "" : code;
        }
        Matcher matcher = CSSTS_MERGE_PATTERN.matcher(code);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            String[] parts = matcher.group(1).split(",");
            StringBuilder args = new StringBuilder();
            for (String part : parts) {
                String trimmed = part.trim();
                if (!args.isEmpty()) {
                    args.append(',');
                }
                if (atomNames.contains(trimmed)) {
                    args.append("csstsAtom.").append(trimmed);
                } else {
                    args.append(part);
                }
            }
            matcher.appendReplacement(builder, Matcher.quoteReplacement("cssts.merge(" + args + ")"));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    private String ensureAtomModule(String code, String css, String atomModule) {
        Set<String> atomNames = extractAtomNames(code);
        if (atomNames.isEmpty()) {
            return atomModule == null ? "" : atomModule;
        }
        if (atomModule != null && containsAllAtoms(atomModule, atomNames)) {
            return atomModule;
        }
        Map<String, String> cssPropertiesByClass = extractCssProperties(css);
        Map<String, String> cssClassByNormalizedName = extractCssClassesByNormalizedName(css);
        StringBuilder builder = new StringBuilder();
        builder.append("// Auto-generated by Qin from cssts-compiler output").append(System.lineSeparator());
        builder.append("export const csstsAtom = {").append(System.lineSeparator());
        int index = 0;
        for (String atomName : atomNames) {
            String cssClassName = cssClassNameForAtom(atomName, cssClassByNormalizedName);
            String property = cssPropertiesByClass.getOrDefault(cssClassName, "null");
            builder.append("  ")
                    .append(atomName)
                    .append(": { '")
                    .append(cssClassName)
                    .append("': ")
                    .append("null".equals(property) ? "null" : "'" + escapeJsString(property) + "'")
                    .append(" }");
            if (++index < atomNames.size()) {
                builder.append(',');
            }
            builder.append(System.lineSeparator());
        }
        builder.append("}").append(System.lineSeparator());
        builder.append("export default csstsAtom").append(System.lineSeparator());
        return builder.toString();
    }

    private boolean containsAllAtoms(String atomModule, Set<String> atomNames) {
        for (String atomName : atomNames) {
            if (!atomModule.contains(atomName + ":")) {
                return false;
            }
        }
        return true;
    }

    private Set<String> extractAtomNames(String code) {
        LinkedHashSet<String> names = new LinkedHashSet<>();
        if (code == null || code.isBlank()) {
            return names;
        }
        Matcher matcher = CSSTS_MERGE_PATTERN.matcher(code);
        while (matcher.find()) {
            String[] parts = matcher.group(1).split(",");
            for (String part : parts) {
                String name = part.trim();
                if (name.matches("[A-Za-z_$][\\w$]*")) {
                    names.add(name);
                }
            }
        }
        return names;
    }

    private Map<String, String> extractCssProperties(String css) {
        LinkedHashMap<String, String> properties = new LinkedHashMap<>();
        if (css == null || css.isBlank()) {
            return properties;
        }
        Matcher matcher = CSS_CLASS_RULE_PATTERN.matcher(css);
        while (matcher.find()) {
            properties.put("cssts_" + matcher.group(1), matcher.group(2).trim());
        }
        return properties;
    }

    private Map<String, String> extractCssClassesByNormalizedName(String css) {
        LinkedHashMap<String, String> classes = new LinkedHashMap<>();
        if (css == null || css.isBlank()) {
            return classes;
        }
        Matcher matcher = CSS_CLASS_RULE_PATTERN.matcher(css);
        while (matcher.find()) {
            String cssClassName = "cssts_" + matcher.group(1);
            classes.putIfAbsent(normalizeAtomClassName(matcher.group(1)), cssClassName);
        }
        return classes;
    }

    private String cssClassNameForAtom(String atomName, Map<String, String> cssClassByNormalizedName) {
        String direct = cssClassByNormalizedName.get(normalizeAtomClassName(atomName));
        if (direct != null) {
            return direct;
        }
        int pseudoIndex = atomName.indexOf("$$");
        if (pseudoIndex > 0) {
            String withoutPseudo = atomName.substring(0, pseudoIndex);
            String pseudoClass = cssClassByNormalizedName.get(normalizeAtomClassName(withoutPseudo));
            if (pseudoClass != null) {
                return pseudoClass;
            }
        }
        return "cssts_" + atomClassSuffix(atomName);
    }

    private String normalizeAtomClassName(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetterOrDigit(ch)) {
                builder.append(Character.toLowerCase(ch));
            }
        }
        return builder.toString();
    }

    private String atomClassSuffix(String atomName) {
        String kebab = camelToKebab(atomName);
        for (Map.Entry<String, String> entry : Map.of(
                "_px", "px",
                "_rem", "rem",
                "_em", "em",
                "_vh", "vh",
                "_vw", "vw").entrySet()) {
            if (kebab.endsWith(entry.getKey())) {
                kebab = kebab.substring(0, kebab.length() - entry.getKey().length()) + entry.getValue();
            }
        }
        return kebab.replace('-', '_');
    }

    private String camelToKebab(String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isUpperCase(ch) && i > 0) {
                builder.append('-');
            }
            builder.append(Character.toLowerCase(ch));
        }
        return builder.toString();
    }

    private String escapeJsString(String text) {
        return text.replace("\\", "\\\\").replace("'", "\\'");
    }

    public record QinCsstsCompileResult(
            String code,
            String rawCode,
            boolean hasStyles,
            String css,
            String atomModule) {
    }

    private record CacheKey(Path projectRoot, String source, String toolchainFingerprint) {
        private CacheKey {
            Objects.requireNonNull(projectRoot, "projectRoot cannot be null");
            Objects.requireNonNull(source, "source cannot be null");
            Objects.requireNonNull(toolchainFingerprint, "toolchainFingerprint cannot be null");
        }
    }

    private record DirectoryDigestCacheEntry(DirectorySnapshot snapshot, String digest) {
    }

    private record DirectorySnapshot(List<DirectoryFileSnapshot> files) {
    }

    private record DirectoryFileSnapshot(String relativePath, long size, long modifiedNanos, String fileKey) {
    }
}
