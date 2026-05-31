package com.qin.runtime.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Minimal Qin-owned JS package invocation host.
 *
 * <p>This runner does not use Node or GraalVM JS. Instead it generates a small
 * temporary Qin/ESM wrapper module, compiles that wrapper through the existing
 * Qin JVM pipeline, and invokes the wrapper's {@code run()} entry.
 */
final class QinJsPackageRunner {
    private static final AtomicLong INVOCATION_SEQUENCE = new AtomicLong();
    private static final Pattern JSON_NAME_FIELD = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern JSON_LOCAL_FIELD = Pattern.compile("\"(?:local|monorepo)\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern JSON_MODULE_FIELD = Pattern.compile("\"module\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern JSON_MAIN_FIELD = Pattern.compile("\"main\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern JSON_IMPORT_FIELD = Pattern.compile("\"import\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern JSON_DEPENDENCIES_BLOCK = Pattern.compile(
            "\"dependencies\"\\s*:\\s*\\{([^}]*)\\}",
            Pattern.DOTALL);
    private static final Pattern JSON_DEPENDENCY_NAME = Pattern.compile("\"([^\"]+)\"\\s*:");
    private static final Pattern JSON_STRING_FIELD = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]*)\"");
    private static final Pattern FROM_IMPORT_PATTERN = Pattern.compile("\\bfrom\\s+[\"']([^\"']+)[\"']");
    private static final Pattern SIDE_EFFECT_IMPORT_PATTERN = Pattern.compile(
            "\\bimport\\s+[\"']([^\"']+)[\"']");
    private static final Set<String> IGNORED_COPY_DIRS = Set.of(
            ".git", ".idea", ".qin", "node_modules", "build", "target", "out");
    private static final Set<String> IGNORED_INSTALLED_PACKAGE_DIRS = Set.of(
            ".git", ".idea", ".qin", "node_modules");

    private final QinInMemoryJvmRunner runner = new QinInMemoryJvmRunner();
    private final QinNpmDependencyMaterializer npmDependencyMaterializer = new QinNpmDependencyMaterializer();

    Object invokeNamedExport(
            Path projectRoot,
            String moduleSpecifier,
            String exportName,
            List<Object> args) throws Exception {
        Objects.requireNonNull(projectRoot, "projectRoot cannot be null");
        Objects.requireNonNull(moduleSpecifier, "moduleSpecifier cannot be null");
        Objects.requireNonNull(exportName, "exportName cannot be null");
        List<Object> safeArgs = args == null ? List.of() : List.copyOf(args);

        return runModuleSource(
                projectRoot,
                buildWrapperSource(moduleSpecifier, exportName, safeArgs),
                sanitizeToken(moduleSpecifier) + "_" + sanitizeToken(exportName));
    }

    Object runModuleSource(Path projectRoot, String wrapperSource, String nameHint) throws Exception {
        Objects.requireNonNull(projectRoot, "projectRoot cannot be null");
        Objects.requireNonNull(wrapperSource, "wrapperSource cannot be null");

        long startNanos = System.nanoTime();
        Path root = projectRoot.toAbsolutePath().normalize();
        Path wrapperDir = root.resolve(".qin").resolve("runtime").resolve("npm-host").normalize();
        Files.createDirectories(wrapperDir);
        logPhase("prepare wrapper dir", startNanos, wrapperDir.toString());
        materializeWorkspaceDependencies(root, wrapperDir, wrapperSource);
        logPhase("materialize workspace dependencies", startNanos, wrapperDir.resolve("node_modules").toString());

        long sequence = INVOCATION_SEQUENCE.incrementAndGet();
        String token = sanitizeToken(nameHint == null || nameHint.isBlank() ? "module" : nameHint);
        Path wrapperFile = wrapperDir.resolve(
                "invoke-" + token + "-" + sequence + ".js");
        Files.writeString(wrapperFile, wrapperSource, StandardCharsets.UTF_8);
        logPhase("write wrapper source", startNanos, wrapperFile.toString());

        String className = "com.qin.runtime.generated.npm.Invoke"
                + capitalize(token)
                + sequence;
        Object result = runner.compileAndRun(wrapperFile, root, className);
        logPhase("compile and run wrapper", startNanos, className);
        return result;
    }

    private void materializeWorkspaceDependencies(Path projectRoot, Path wrapperDir, String wrapperSource) throws IOException {
        Set<String> bareSpecifiers = extractBareModuleSpecifiers(wrapperSource);
        if (bareSpecifiers.isEmpty()) {
            return;
        }

        Path workspaceRoot = locateWorkspaceRoot();
        if (workspaceRoot == null) {
            return;
        }

        Map<String, Path> workspacePackages = indexWorkspacePackages(workspaceRoot);
        Path runtimeNodeModules = wrapperDir.resolve("node_modules");
        Files.createDirectories(runtimeNodeModules);
        npmDependencyMaterializer.materializeProjectDependencies(projectRoot, runtimeNodeModules);

        Set<String> materialized = new LinkedHashSet<>();
        for (String specifier : bareSpecifiers) {
            materializeDependency(specifier, null, runtimeNodeModules, workspaceRoot, workspacePackages, materialized);
        }
        materializeQinViteShimIfNeeded(bareSpecifiers, runtimeNodeModules);
    }

    private void materializeDependency(
            String specifier,
            String versionRange,
            Path runtimeNodeModules,
            Path workspaceRoot,
            Map<String, Path> workspacePackages,
            Set<String> materialized) throws IOException {
        String packageName = parseBarePackageName(specifier);
        if (packageName == null || packageName.isBlank() || !materialized.add(packageName)) {
            return;
        }
        if ("vite".equals(packageName)) {
            materializeQinViteShim(runtimeNodeModules);
            return;
        }
        if ("glogjs".equals(packageName)) {
            materializeQinGlogShim(runtimeNodeModules);
            return;
        }

        Path workspacePackageDir = workspacePackages.get(packageName);
        boolean workspacePackage = workspacePackageDir != null;
        Path sourcePackageDir = workspacePackage
                ? workspacePackageDir
                : resolveInstalledPackageDir(packageName, workspaceRoot);
        if (sourcePackageDir == null || !Files.isDirectory(sourcePackageDir)) {
            if (versionRange != null && !versionRange.isBlank()) {
                npmDependencyMaterializer.materializePackageDependency(packageName, versionRange, runtimeNodeModules);
            }
            return;
        }

        Path targetPackageDir = runtimeNodeModules.resolve(packageName.replace('/', java.io.File.separatorChar)).normalize();
        deleteRecursively(targetPackageDir);
        Files.createDirectories(targetPackageDir.getParent());
        copyPackageTree(sourcePackageDir, targetPackageDir, workspacePackage);

        if (workspacePackage && (hasDeclaredWorkspaceSourceEntry(sourcePackageDir)
                || readExistingPackageEntry(sourcePackageDir) == null)) {
            rewriteWorkspacePackageManifest(targetPackageDir, sourcePackageDir, packageName);
        }

        for (Map.Entry<String, String> dependency : readDependencyVersions(sourcePackageDir.resolve("package.json")).entrySet()) {
            materializeDependency(
                    dependency.getKey(),
                    dependency.getValue(),
                    runtimeNodeModules,
                    workspaceRoot,
                    workspacePackages,
                    materialized);
        }
    }

    private void materializeQinViteShimIfNeeded(Set<String> bareSpecifiers, Path runtimeNodeModules) throws IOException {
        if (bareSpecifiers == null || bareSpecifiers.isEmpty()) {
            return;
        }
        if (bareSpecifiers.contains("vite") || bareSpecifiers.contains("@vitejs/plugin-vue")) {
            materializeQinViteShim(runtimeNodeModules);
        }
        if (bareSpecifiers.contains("@vitejs/plugin-vue")) {
            materializeQinVuePluginHostShim(runtimeNodeModules);
        }
    }

    private void materializeQinViteShim(Path runtimeNodeModules) throws IOException {
        Path shimDir = runtimeNodeModules.resolve("vite").normalize();
        Files.createDirectories(shimDir);
        Files.writeString(shimDir.resolve("package.json"), """
                {
                  "name": "vite",
                  "version": "0.0.0-qin-shim",
                  "type": "module",
                  "exports": {
                    ".": "./index.js"
                  },
                  "main": "./index.js",
                  "module": "./index.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(shimDir.resolve("index.js"), qinViteShimSource(), StandardCharsets.UTF_8);
    }

    private String qinViteShimSource() {
        return """
                function __qin_to_array(value) {
                  if (value == null) return [];
                  return Array.isArray(value) ? value : [value];
                }

                function __qin_match(pattern, id) {
                  if (pattern == null) return false;
                  if (typeof pattern === "function") return !!pattern(id);
                  if (pattern instanceof RegExp) return pattern.test(id);
                  const text = String(pattern);
                  if (text === id) return true;
                  if (text.includes("*")) {
                    const escaped = text
                      .replace(/[.+?^${}()|[\\]\\\\]/g, "\\\\$&")
                      .replace(/\\\\\\*/g, ".*");
                    return new RegExp("^" + escaped + "$").test(id);
                  }
                  return id.includes(text);
                }

                export function createFilter(include, exclude) {
                  const includes = __qin_to_array(include);
                  const excludes = __qin_to_array(exclude);
                  return function qinViteFilter(id) {
                    const text = String(id || "");
                    for (const pattern of excludes) {
                      if (__qin_match(pattern, text)) return false;
                    }
                    if (includes.length === 0) return true;
                    for (const pattern of includes) {
                      if (__qin_match(pattern, text)) return true;
                    }
                    return false;
                  };
                }

                export function normalizePath(path) {
                  return String(path || "").replace(/\\\\/g, "/");
                }

                export function isCSSRequest(id) {
                  return /(?:\\.css|\\.less|\\.sass|\\.scss|\\.styl|\\.stylus|\\.pcss|\\.postcss)(?:$|\\?)/.test(String(id || ""));
                }

                export function formatPostcssSourceMap(map, filename) {
                  return map || { mappings: "" };
                }

                export function transformWithEsbuild(code, filename, options) {
                  return { code: String(code || ""), map: { mappings: "" } };
                }
                """;
    }

    private void materializeQinGlogShim(Path runtimeNodeModules) throws IOException {
        Path shimDir = runtimeNodeModules.resolve("glogjs").normalize();
        Files.createDirectories(shimDir);
        Files.writeString(shimDir.resolve("package.json"), """
                {
                  "name": "glogjs",
                  "version": "0.0.0-qin-shim",
                  "type": "module",
                  "exports": {
                    ".": "./index.js"
                  },
                  "main": "./index.js",
                  "module": "./index.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(shimDir.resolve("index.js"), """
                function noop() {}
                const Glog = {
                  debug: noop,
                  info: noop,
                  warn: noop,
                  error: noop,
                  log: noop
                };
                export const debug = Glog.debug;
                export const info = Glog.info;
                export const warn = Glog.warn;
                export const error = Glog.error;
                export const log = Glog.log;
                export default Glog;
                """, StandardCharsets.UTF_8);
    }

    private void materializeQinVuePluginHostShim(Path runtimeNodeModules) throws IOException {
        Path shimDir = runtimeNodeModules.resolve("vue").normalize();
        Files.createDirectories(shimDir);
        Files.writeString(shimDir.resolve("package.json"), """
                {
                  "name": "vue",
                  "version": "0.0.0-qin-plugin-host-shim",
                  "type": "module",
                  "exports": {
                    ".": "./index.js"
                  },
                  "main": "./index.js",
                  "module": "./index.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(shimDir.resolve("index.js"), """
                export function shallowRef(value) {
                  return { value };
                }

                export function computed(factory) {
                  return {
                    get value() {
                      return factory();
                    }
                  };
                }
                """, StandardCharsets.UTF_8);
    }

    private void rewriteWorkspacePackageManifest(Path targetPackageDir, Path sourcePackageDir, String packageName)
            throws IOException {
        String sourceEntry = readWorkspaceSourceEntry(sourcePackageDir);
        String manifest = """
                {
                  "name": %s,
                  "type": "module",
                  "main": %s,
                  "module": %s,
                  "exports": {
                    ".": {
                      "import": %s,
                      "default": %s
                    }
                  }
                }
                """.formatted(
                quote(packageName),
                quote(sourceEntry),
                quote(sourceEntry),
                quote(sourceEntry),
                quote(sourceEntry));
        Files.writeString(targetPackageDir.resolve("package.json"), manifest, StandardCharsets.UTF_8);
    }

    private String readExistingPackageEntry(Path sourcePackageDir) throws IOException {
        Path packageJson = sourcePackageDir.resolve("package.json");
        if (!Files.isRegularFile(packageJson)) {
            return null;
        }
        String json = Files.readString(packageJson, StandardCharsets.UTF_8);
        for (Pattern pattern : List.of(JSON_IMPORT_FIELD, JSON_MODULE_FIELD, JSON_MAIN_FIELD)) {
            Matcher matcher = pattern.matcher(json);
            if (matcher.find()) {
                String candidate = normalizeManifestRelativePath(matcher.group(1));
                if (Files.isRegularFile(sourcePackageDir.resolve(candidate).normalize())) {
                    return candidate;
                }
            }
        }
        return null;
    }

    private String readWorkspaceSourceEntry(Path sourcePackageDir) throws IOException {
        Path packageJson = sourcePackageDir.resolve("package.json");
        if (Files.isRegularFile(packageJson)) {
            String json = Files.readString(packageJson, StandardCharsets.UTF_8);
            Matcher localMatcher = JSON_LOCAL_FIELD.matcher(json);
            if (localMatcher.find()) {
                return normalizeManifestRelativePath(localMatcher.group(1));
            }
        }

        Path srcIndex = sourcePackageDir.resolve("src").resolve("index.ts");
        if (Files.isRegularFile(srcIndex)) {
            return "./src/index.ts";
        }
        Path indexTs = sourcePackageDir.resolve("index.ts");
        if (Files.isRegularFile(indexTs)) {
            return "./index.ts";
        }
        Path srcIndexJs = sourcePackageDir.resolve("src").resolve("index.js");
        if (Files.isRegularFile(srcIndexJs)) {
            return "./src/index.js";
        }
        Path indexJs = sourcePackageDir.resolve("index.js");
        if (Files.isRegularFile(indexJs)) {
            return "./index.js";
        }
        throw new IllegalStateException("Cannot determine workspace source entry for package: " + sourcePackageDir);
    }

    private boolean hasDeclaredWorkspaceSourceEntry(Path sourcePackageDir) throws IOException {
        Path packageJson = sourcePackageDir.resolve("package.json");
        if (!Files.isRegularFile(packageJson)) {
            return false;
        }
        String json = Files.readString(packageJson, StandardCharsets.UTF_8);
        return JSON_LOCAL_FIELD.matcher(json).find();
    }

    private String normalizeManifestRelativePath(String value) {
        String normalized = value.replace('\\', '/').trim();
        if (normalized.startsWith("./") || normalized.startsWith("../")) {
            return normalized;
        }
        return "./" + normalized;
    }

    private Set<String> extractBareModuleSpecifiers(String source) {
        Set<String> specifiers = new LinkedHashSet<>();
        boolean[] code = codeMask(source);
        collectBareSpecifiers(specifiers, code, FROM_IMPORT_PATTERN.matcher(source));
        collectBareSpecifiers(specifiers, code, SIDE_EFFECT_IMPORT_PATTERN.matcher(source));
        return specifiers;
    }

    private void collectBareSpecifiers(Set<String> specifiers, boolean[] code, Matcher matcher) {
        while (matcher.find()) {
            if (!isCodePosition(code, matcher.start())) {
                continue;
            }
            String specifier = matcher.group(1);
            if (specifier == null
                    || specifier.isBlank()
                    || specifier.startsWith("./")
                    || specifier.startsWith("../")
                    || specifier.startsWith("/")
                    || specifier.startsWith("java:")
                    || specifier.startsWith("js:")
                    || specifier.startsWith("node:")
                    || specifier.startsWith("http://")
                    || specifier.startsWith("https://")) {
                continue;
            }
            specifiers.add(specifier);
        }
    }

    private boolean[] codeMask(String source) {
        boolean[] code = new boolean[source.length()];
        boolean single = false;
        boolean dbl = false;
        boolean template = false;
        int templateExpressionDepth = 0;
        boolean lineComment = false;
        boolean blockComment = false;
        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : '\0';
            char previous = i > 0 ? source.charAt(i - 1) : '\0';

            if (lineComment) {
                if (ch == '\n') {
                    lineComment = false;
                    code[i] = true;
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
                if (ch == '$' && next == '{' && previous != '\\') {
                    code[i] = true;
                    code[i + 1] = true;
                    templateExpressionDepth = 1;
                    template = false;
                    i++;
                    continue;
                }
                if (ch == '`' && previous != '\\') {
                    template = false;
                }
                continue;
            }

            if (ch == '/' && next == '/') {
                lineComment = true;
                i++;
            } else if (ch == '/' && next == '*') {
                blockComment = true;
                i++;
            } else if (ch == '/' && startsRegexLiteral(source, i)) {
                i = skipRegexLiteral(source, i);
            } else if (ch == '\'') {
                single = true;
            } else if (ch == '"') {
                dbl = true;
            } else if (ch == '`') {
                template = true;
            } else {
                code[i] = true;
                if (templateExpressionDepth > 0) {
                    if (ch == '{') {
                        templateExpressionDepth++;
                    } else if (ch == '}') {
                        templateExpressionDepth--;
                        if (templateExpressionDepth == 0) {
                            template = true;
                        }
                    }
                }
            }
        }
        return code;
    }

    private boolean isCodePosition(boolean[] code, int index) {
        return index >= 0 && index < code.length && code[index];
    }

    private boolean startsRegexLiteral(String source, int slashIndex) {
        int previous = slashIndex - 1;
        while (previous >= 0 && Character.isWhitespace(source.charAt(previous))) {
            previous--;
        }
        if (previous < 0) {
            return true;
        }
        char ch = source.charAt(previous);
        return "([{:;,=!?&|+-*~^<>%".indexOf(ch) >= 0;
    }

    private int skipRegexLiteral(String source, int slashIndex) {
        boolean inClass = false;
        for (int i = slashIndex + 1; i < source.length(); i++) {
            char ch = source.charAt(i);
            char previous = i > 0 ? source.charAt(i - 1) : '\0';
            if (ch == '\n' || ch == '\r') {
                return i - 1;
            }
            if (ch == '[' && previous != '\\') {
                inClass = true;
            } else if (ch == ']' && previous != '\\') {
                inClass = false;
            } else if (ch == '/' && previous != '\\' && !inClass) {
                while (i + 1 < source.length() && Character.isLetter(source.charAt(i + 1))) {
                    i++;
                }
                return i;
            }
        }
        return slashIndex;
    }

    private String parseBarePackageName(String specifier) {
        String trimmed = specifier == null ? "" : specifier.trim();
        if (trimmed.isBlank()) {
            return null;
        }
        if (trimmed.startsWith("@")) {
            int firstSlash = trimmed.indexOf('/');
            if (firstSlash < 0) {
                return null;
            }
            int secondSlash = trimmed.indexOf('/', firstSlash + 1);
            return secondSlash < 0 ? trimmed : trimmed.substring(0, secondSlash);
        }
        int slash = trimmed.indexOf('/');
        return slash < 0 ? trimmed : trimmed.substring(0, slash);
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

    private Map<String, Path> indexWorkspacePackages(Path workspaceRoot) throws IOException {
        Map<String, Path> packages = new LinkedHashMap<>();
        try (var paths = Files.walk(workspaceRoot, 6)) {
            paths.filter(path -> path.getFileName() != null && "package.json".equals(path.getFileName().toString()))
                    .filter(path -> !isIgnoredPath(path))
                    .forEach(path -> {
                        String packageName = readPackageName(path);
                        if (packageName != null && !packageName.isBlank()) {
                            packages.putIfAbsent(packageName, path.getParent().toAbsolutePath().normalize());
                        }
                    });
        }
        return packages;
    }

    private boolean isIgnoredPath(Path path) {
        for (Path part : path) {
            if (IGNORED_COPY_DIRS.contains(String.valueOf(part))) {
                return true;
            }
        }
        return false;
    }

    private String readPackageName(Path packageJson) {
        try {
            String json = Files.readString(packageJson, StandardCharsets.UTF_8);
            Matcher matcher = JSON_NAME_FIELD.matcher(json);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return null;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read package manifest: " + packageJson, e);
        }
    }

    private List<String> readDependencyNames(Path packageJson) throws IOException {
        if (!Files.isRegularFile(packageJson)) {
            return List.of();
        }
        String json = Files.readString(packageJson, StandardCharsets.UTF_8);
        Matcher blockMatcher = JSON_DEPENDENCIES_BLOCK.matcher(json);
        if (!blockMatcher.find()) {
            return List.of();
        }

        String block = blockMatcher.group(1);
        Matcher dependencyMatcher = JSON_DEPENDENCY_NAME.matcher(block);
        List<String> names = new ArrayList<>();
        while (dependencyMatcher.find()) {
            names.add(dependencyMatcher.group(1));
        }
        return names;
    }

    private Map<String, String> readDependencyVersions(Path packageJson) throws IOException {
        if (!Files.isRegularFile(packageJson)) {
            return Map.of();
        }
        String json = Files.readString(packageJson, StandardCharsets.UTF_8);
        Matcher blockMatcher = JSON_DEPENDENCIES_BLOCK.matcher(json);
        if (!blockMatcher.find()) {
            return Map.of();
        }

        Map<String, String> dependencies = new LinkedHashMap<>();
        Matcher fieldMatcher = JSON_STRING_FIELD.matcher(blockMatcher.group(1));
        while (fieldMatcher.find()) {
            dependencies.put(fieldMatcher.group(1), fieldMatcher.group(2));
        }
        return dependencies;
    }

    private Set<String> scanPackageBareModuleSpecifiers(Path packageDir) throws IOException {
        if (packageDir == null || !Files.isDirectory(packageDir)) {
            return Set.of();
        }
        Set<String> specifiers = new LinkedHashSet<>();
        try (var paths = Files.walk(packageDir)) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(this::isScannablePackageSourceFile)
                    .forEach(path -> collectPackageSourceBareSpecifiers(specifiers, path));
        }
        return specifiers;
    }

    private boolean isScannablePackageSourceFile(Path path) {
        String name = path.getFileName() == null ? "" : path.getFileName().toString().toLowerCase();
        return name.endsWith(".js")
                || name.endsWith(".mjs")
                || name.endsWith(".cjs")
                || name.endsWith(".ts")
                || name.endsWith(".qin");
    }

    private void collectPackageSourceBareSpecifiers(Set<String> specifiers, Path sourceFile) {
        try {
            specifiers.addAll(extractBareModuleSpecifiers(Files.readString(sourceFile, StandardCharsets.UTF_8)));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to scan package imports: " + sourceFile, e);
        }
    }

    private Path resolveInstalledPackageDir(String packageName, Path workspaceRoot) {
        List<Path> searchRoots = List.of(
                workspaceRoot,
                workspaceRoot.resolve("qin"),
                Path.of("").toAbsolutePath().normalize());
        for (Path root : searchRoots) {
            Path candidate = root.resolve("node_modules")
                    .resolve(packageName.replace('/', java.io.File.separatorChar));
            if (Files.isDirectory(candidate)
                    && Files.isRegularFile(candidate.resolve("package.json"))
                    && packageManifestNameMatches(candidate, packageName)) {
                return candidate.toAbsolutePath().normalize();
            }
        }
        Path discovered = scanWorkspaceNodeModules(workspaceRoot, packageName);
        if (discovered != null) {
            return discovered;
        }
        return null;
    }

    private Path scanWorkspaceNodeModules(Path workspaceRoot, String packageName) {
        String expectedSuffix = packageName.replace('/', java.io.File.separatorChar);
        try (var paths = Files.walk(workspaceRoot, 8)) {
            return paths
                    .filter(Files::isDirectory)
                    .filter(path -> path.toString().contains(java.io.File.separator + "node_modules" + java.io.File.separator))
                    .filter(path -> path.endsWith(expectedSuffix))
                    .filter(path -> Files.isRegularFile(path.resolve("package.json")))
                    .filter(path -> packageManifestNameMatches(path, packageName))
                    .findFirst()
                    .map(path -> path.toAbsolutePath().normalize())
                    .orElse(null);
        } catch (IOException error) {
            throw new IllegalStateException("Failed to scan workspace node_modules for " + packageName, error);
        }
    }

    private boolean packageManifestNameMatches(Path packageDir, String expectedName) {
        String actualName = readPackageName(packageDir.resolve("package.json"));
        return expectedName.equals(actualName);
    }

    private void copyPackageTree(Path sourceDir, Path targetDir, boolean workspacePackage) throws IOException {
        Set<String> ignoredDirs = workspacePackage ? IGNORED_COPY_DIRS : IGNORED_INSTALLED_PACKAGE_DIRS;
        Files.walkFileTree(sourceDir, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path relative = sourceDir.relativize(dir);
                if (!relative.toString().isEmpty()) {
                    String name = dir.getFileName() == null ? "" : dir.getFileName().toString();
                    if (ignoredDirs.contains(name)) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                }
                Files.createDirectories(targetDir.resolve(relative));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path relative = sourceDir.relativize(file);
                Files.createDirectories(targetDir.resolve(relative).getParent());
                Files.copy(file, targetDir.resolve(relative));
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void deleteRecursively(Path path) throws IOException {
        if (path == null || !Files.exists(path)) {
            return;
        }
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void logPhase(String phase, long startNanos, String detail) {
        long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000L;
        System.out.println("[QinJsPackageRunner] " + phase + " +" + elapsedMs + "ms :: " + detail);
    }

    private String buildWrapperSource(String moduleSpecifier, String exportName, List<Object> args) {
        StringBuilder source = new StringBuilder();
        if ("default".equals(exportName)) {
            source.append("import __qin_target__ from ")
                    .append(renderJsLiteral(moduleSpecifier))
                    .append(";\n");
        } else {
            source.append("import { ")
                    .append(exportName)
                    .append(" as __qin_target__ } from ")
                    .append(renderJsLiteral(moduleSpecifier))
                    .append(";\n");
        }
        for (int i = 0; i < args.size(); i++) {
            source.append("const __qin_arg")
                    .append(i)
                    .append(" = ")
                    .append(renderJsLiteral(args.get(i)))
                    .append(";\n");
        }
        source.append("const __qin_result__ = __qin_target__(");
        for (int i = 0; i < args.size(); i++) {
            if (i > 0) {
                source.append(", ");
            }
            source.append("__qin_arg").append(i);
        }
        source.append(");\n");
        source.append("(__qin_result__);\n");
        return source.toString();
    }

    static String renderJsLiteral(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String text) {
            return quote(text);
        }
        if (value instanceof Boolean) {
            return String.valueOf(value);
        }
        if (value instanceof Byte
                || value instanceof Short
                || value instanceof Integer
                || value instanceof Long) {
            return String.valueOf(value);
        }
        if (value instanceof Float floatValue) {
            if (!Float.isFinite(floatValue)) {
                throw new IllegalArgumentException("Unsupported non-finite float literal: " + floatValue);
            }
            return String.valueOf(floatValue);
        }
        if (value instanceof Double doubleValue) {
            if (!Double.isFinite(doubleValue)) {
                throw new IllegalArgumentException("Unsupported non-finite double literal: " + doubleValue);
            }
            return String.valueOf(doubleValue);
        }
        if (value instanceof Map<?, ?> map) {
            return toJsObjectLiteral(map);
        }
        if (value instanceof Collection<?> collection) {
            return toJsArrayLiteral(collection);
        }
        if (value.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(value);
            List<Object> items = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                items.add(java.lang.reflect.Array.get(value, i));
            }
            return toJsArrayLiteral(items);
        }
        throw new IllegalArgumentException("Unsupported JS literal value: " + value.getClass().getName());
    }

    private static String toJsObjectLiteral(Map<?, ?> map) {
        StringBuilder out = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) {
                out.append(", ");
            }
            first = false;
            out.append(quote(String.valueOf(entry.getKey())))
                    .append(": ")
                    .append(renderJsLiteral(entry.getValue()));
        }
        out.append('}');
        return out.toString();
    }

    private static String toJsArrayLiteral(Collection<?> values) {
        StringBuilder out = new StringBuilder("[");
        boolean first = true;
        for (Object value : values) {
            if (!first) {
                out.append(", ");
            }
            first = false;
            out.append(renderJsLiteral(value));
        }
        out.append(']');
        return out.toString();
    }

    private static String quote(String text) {
        StringBuilder out = new StringBuilder("\"");
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            switch (ch) {
                case '\\' -> out.append("\\\\");
                case '"' -> out.append("\\\"");
                case '\b' -> out.append("\\b");
                case '\f' -> out.append("\\f");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> {
                    if (ch < 0x20) {
                        out.append(String.format("\\u%04x", (int) ch));
                    } else {
                        out.append(ch);
                    }
                }
            }
        }
        out.append('"');
        return out.toString();
    }

    private String sanitizeToken(String value) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (Character.isLetterOrDigit(ch)) {
                out.append(ch);
            } else {
                out.append('_');
            }
        }
        if (out.isEmpty()) {
            return "module";
        }
        if (!Character.isJavaIdentifierStart(out.charAt(0))) {
            out.insert(0, '_');
        }
        return out.toString();
    }

    private String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return "Module";
        }
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }
}
