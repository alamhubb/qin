package com.qin.runtime.core;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QinCsstsCompiler {
    private static final Pattern CSSTS_MERGE_PATTERN = Pattern.compile("cssts\\.merge\\(([^)]*)\\)");
    private static final Pattern CSS_CLASS_RULE_PATTERN = Pattern.compile("\\.cssts_([A-Za-z0-9_-]+)\\s*\\{\\s*([^:}]+):");
    private static final int MAX_CACHE_ENTRIES = 64;

    private final QinJsPackageRunner packageRunner = new QinJsPackageRunner();
    private final Map<CacheKey, QinCsstsCompileResult> cache = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<CacheKey, QinCsstsCompileResult> eldest) {
            return size() > MAX_CACHE_ENTRIES;
        }
    };

    public QinCsstsCompileResult compile(Path projectRoot, String source) throws Exception {
        Path normalizedRoot = projectRoot.toAbsolutePath().normalize();
        CacheKey key = new CacheKey(normalizedRoot, source);
        synchronized (cache) {
            QinCsstsCompileResult cached = cache.get(key);
            if (cached != null) {
                return cached;
            }
        }
        String configSource = readConfigSource(normalizedRoot);
        Path transformCacheRoot = transformCacheRoot(normalizedRoot);
        String diskKey = QinFrontendTransformDiskCache.keyMaterial(semanticRoot(normalizedRoot), source, configSource);
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

    private record CacheKey(Path projectRoot, String source) {
        private CacheKey {
            Objects.requireNonNull(projectRoot, "projectRoot cannot be null");
            Objects.requireNonNull(source, "source cannot be null");
        }
    }
}
