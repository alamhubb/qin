package com.qin.runtime.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
    private static final Pattern QIN_PACKAGE_OVERRIDES_BLOCK = Pattern.compile(
            "packageOverrides\\s*:\\s*\\{([^}]*)\\}",
            Pattern.DOTALL);
    private static final Pattern QIN_STRING_FIELD = Pattern.compile("[\"']([^\"']+)[\"']\\s*:\\s*[\"']([^\"']*)[\"']");
    private static final Pattern FROM_IMPORT_PATTERN = Pattern.compile("\\bfrom\\s+[\"']([^\"']+)[\"']");
    private static final Pattern SIDE_EFFECT_IMPORT_PATTERN = Pattern.compile(
            "\\bimport\\s+[\"']([^\"']+)[\"']");
    private static final Set<String> IGNORED_COPY_DIRS = Set.of(
            ".git", ".idea", ".qin", "node_modules", "build", "target", "out");
    private static final Set<String> IGNORED_INSTALLED_PACKAGE_DIRS = Set.of(
            ".git", ".idea", ".qin", "node_modules");
    private static final Map<Path, Object> NPM_HOST_LOCK_MONITORS = new ConcurrentHashMap<>();

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

        Object monitor = NPM_HOST_LOCK_MONITORS.computeIfAbsent(wrapperDir, ignored -> new Object());
        synchronized (monitor) {
            try (NpmHostLock ignored = acquireNpmHostLock(wrapperDir)) {
                Set<String> activePackages = materializeWorkspaceDependencies(root, wrapperDir, wrapperSource);
                logPhase("materialize workspace dependencies", startNanos, wrapperDir.resolve("node_modules").toString());
                String dependencyFingerprint = moduleDependencyFingerprint(wrapperDir.resolve("node_modules"), activePackages);

                String token = sanitizeToken(nameHint == null || nameHint.isBlank() ? "module" : nameHint);
                String identity = shortSha256(token + "\n" + dependencyFingerprint + "\n" + wrapperSource);
                Path wrapperFile = wrapperDir.resolve(
                        "invoke-" + token + "-" + identity + ".js");
                pruneStaleInvocationWrappers(wrapperDir, wrapperFile);
                Files.writeString(wrapperFile, wrapperSource, StandardCharsets.UTF_8);
                logPhase("write wrapper source", startNanos, wrapperFile.toString());

                String className = "com.qin.runtime.generated.npm.Invoke"
                        + capitalize(token)
                        + "_"
                        + identity;
                Object result = runner.compileAndRunModuleClasses(
                        wrapperFile,
                        root,
                        className,
                        dependencyFingerprint,
                        moduleClassCacheRoot(root),
                        stableModuleClassCacheIdentity(wrapperSource, token, dependencyFingerprint));
                logPhase("compile and run wrapper", startNanos, className);
                return result;
            }
        }
    }

    String moduleDependencyFingerprint(Path nodeModules) throws IOException {
        return moduleDependencyFingerprint(nodeModules, null);
    }

    String moduleDependencyFingerprint(Path nodeModules, Set<String> activePackages) throws IOException {
        if (!Files.isDirectory(nodeModules)) {
            return "";
        }
        MessageDigest digest = newSha256Digest();
        for (Path packageDir : installedPackageDirs(nodeModules)) {
            Path relative = nodeModules.relativize(packageDir.toAbsolutePath().normalize());
            String packageName = relative.toString().replace('\\', '/');
            if (activePackages != null && !activePackages.contains(packageName)) {
                continue;
            }
            digest.update(relative.toString().replace('\\', '/').getBytes(StandardCharsets.UTF_8));
            digest.update((byte) 0);
            updateDependencyFileDigest(digest, packageDir.resolve("package.json"), true);
            Path stamp = packageDir.resolve(".qin-package-sync.json");
            if (!Files.isRegularFile(stamp)) {
                if (activePackages != null) {
                    throw new IOException("Qin runtime package is missing .qin-package-sync.json: " + packageDir);
                }
                ensureRuntimePackageStamp(packageDir);
            }
            updateDependencyFileDigest(digest, stamp, true);
            digest.update((byte) 0);
        }
        return HexFormat.of().formatHex(digest.digest());
    }

    private List<Path> installedPackageDirs(Path nodeModules) throws IOException {
        List<Path> packages = new ArrayList<>();
        try (var topLevel = Files.list(nodeModules)) {
            for (Path entry : topLevel.sorted().toList()) {
                if (!Files.isDirectory(entry)) {
                    continue;
                }
                String name = entry.getFileName() == null ? "" : entry.getFileName().toString();
                if (name.startsWith("@")) {
                    try (var scoped = Files.list(entry)) {
                        scoped
                                .filter(Files::isDirectory)
                                .filter(path -> Files.isRegularFile(path.resolve("package.json")))
                                .sorted()
                                .forEach(packages::add);
                    }
                } else if (Files.isRegularFile(entry.resolve("package.json"))) {
                    packages.add(entry);
                }
            }
        }
        return packages;
    }

    private void updateDependencyFileDigest(
            MessageDigest digest,
            Path file,
            boolean includeContents) throws IOException {
        if (!Files.isRegularFile(file)) {
            digest.update((byte) '-');
            return;
        }
        digest.update(file.getFileName().toString().getBytes(StandardCharsets.UTF_8));
        digest.update((byte) ':');
        if (!includeContents) {
            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
            digest.update(Long.toString(attributes.size()).getBytes(StandardCharsets.UTF_8));
            digest.update((byte) ':');
            digest.update(Long.toString(attributes.lastModifiedTime().toMillis()).getBytes(StandardCharsets.UTF_8));
        }
        if (includeContents) {
            digest.update((byte) ':');
            digest.update(Files.readString(file, StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8));
        }
        digest.update((byte) ';');
    }

    private String stableModuleClassCacheIdentity(String wrapperSource, String token, String dependencyFingerprint) {
        if (containsLocalModuleSpecifier(wrapperSource)) {
            return "";
        }
        return "qin-js-package-runner"
                + "\n"
                + token
                + "\n"
                + (dependencyFingerprint == null ? "" : dependencyFingerprint)
                + "\n"
                + shortSha256(wrapperSource);
    }

    private boolean containsLocalModuleSpecifier(String source) {
        for (String specifier : extractModuleSpecifiers(source)) {
            if (specifier.startsWith(".") || specifier.startsWith("/") || specifier.matches("^[A-Za-z]:[\\\\/].*")) {
                return true;
            }
        }
        return false;
    }

    private Set<String> extractModuleSpecifiers(String source) {
        Set<String> specifiers = new LinkedHashSet<>();
        boolean[] code = codeMask(source);
        collectModuleSpecifiers(specifiers, code, FROM_IMPORT_PATTERN.matcher(source));
        collectModuleSpecifiers(specifiers, code, SIDE_EFFECT_IMPORT_PATTERN.matcher(source));
        return specifiers;
    }

    private void collectModuleSpecifiers(Set<String> specifiers, boolean[] code, Matcher matcher) {
        while (matcher.find()) {
            if (!isCodePosition(code, matcher.start())) {
                continue;
            }
            String specifier = matcher.group(1);
            if (specifier != null && !specifier.isBlank()) {
                specifiers.add(specifier);
            }
        }
    }

    private NpmHostLock acquireNpmHostLock(Path wrapperDir) throws IOException {
        Files.createDirectories(wrapperDir);
        Path lockPath = wrapperDir.resolve(".qin-npm-host.lock");
        FileChannel channel = FileChannel.open(
                lockPath,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE);
        FileLock lock = channel.lock();
        return new NpmHostLock(channel, lock);
    }

    private Set<String> materializeWorkspaceDependencies(Path projectRoot, Path wrapperDir, String wrapperSource)
            throws IOException {
        Set<String> bareSpecifiers = extractBareModuleSpecifiers(wrapperSource);
        collectLocalImportBareModuleSpecifiers(
                projectRoot.toAbsolutePath().normalize(),
                wrapperDir.toAbsolutePath().normalize(),
                wrapperSource,
                bareSpecifiers,
                new LinkedHashSet<>(),
                0);
        if (bareSpecifiers.isEmpty()) {
            return Set.of();
        }

        Path workspaceRoot = locateWorkspaceRoot();
        if (workspaceRoot == null) {
            return Set.of();
        }

        Map<String, Path> packageOverrides = readProjectPackageOverrides(projectRoot);
        Map<String, Path> workspacePackages = indexWorkspacePackages(workspaceRoot);
        Path runtimeNodeModules = wrapperDir.resolve("node_modules");
        Files.createDirectories(runtimeNodeModules);

        Set<String> materialized = new LinkedHashSet<>();
        for (String specifier : bareSpecifiers) {
            materializeDependency(
                    specifier,
                    null,
                    null,
                    projectRoot,
                    runtimeNodeModules,
                    workspaceRoot,
                    workspacePackages,
                    packageOverrides,
                    materialized);
        }
        materializeQinViteShimIfNeeded(bareSpecifiers, runtimeNodeModules);
        if (bareSpecifiers.contains("vite") || bareSpecifiers.contains("@vitejs/plugin-vue")) {
            materialized.add("vite");
        }
        if (bareSpecifiers.contains("@vue/compiler-sfc") || bareSpecifiers.contains("@vitejs/plugin-vue")) {
            materialized.add("@vue/compiler-sfc");
        }
        if (bareSpecifiers.contains("@vitejs/plugin-vue")) {
            materialized.add("vue");
        }
        pruneStaleQinMaterializedPackages(runtimeNodeModules, materialized);
        return Set.copyOf(materialized);
    }

    private Map<String, Path> readProjectPackageOverrides(Path projectRoot) throws IOException {
        Path configFile = projectRoot.resolve("qin.config.js");
        if (!Files.isRegularFile(configFile)) {
            return Map.of();
        }
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
            if (!Files.isDirectory(overridePath)) {
                throw new IllegalStateException(
                        "Qin package override for " + packageName + " does not exist: " + overridePath);
            }
            overrides.put(packageName, overridePath);
        }
        return overrides;
    }

    private void materializeDependency(
            String specifier,
            String versionRange,
            Path dependencyBaseDir,
            Path projectRoot,
            Path runtimeNodeModules,
            Path workspaceRoot,
            Map<String, Path> workspacePackages,
            Map<String, Path> packageOverrides,
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
        if ("@vue/compiler-sfc".equals(packageName)) {
            materializeQinVueCompilerSfcShim(runtimeNodeModules);
            return;
        }
        if ("subhuti".equals(packageName)) {
            materializeQinSubhutiShim(runtimeNodeModules);
            materializeDependency(
                    "@qin/generated-qin-parser-ts",
                    "file:" + workspaceRoot.resolve("qin")
                            .resolve("packages")
                            .resolve("qin-language")
                            .resolve("generated")
                            .resolve("qin-parser-ts")
                            .toString()
                            .replace('\\', '/'),
                    projectRoot,
                    projectRoot,
                    runtimeNodeModules,
                    workspaceRoot,
                    workspacePackages,
                    packageOverrides,
                    materialized);
            return;
        }
        if ("slime-generator".equals(packageName)) {
            materializeQinSlimeGeneratorShim(runtimeNodeModules);
            return;
        }

        Path overridePackageDir = packageOverrides.get(packageName);
        boolean overridePackage = overridePackageDir != null;
        Path filePackageDir = overridePackage ? null : resolveFileDependencyDir(versionRange, dependencyBaseDir);
        boolean filePackage = filePackageDir != null;
        Path workspacePackageDir = overridePackage ? null : workspacePackages.get(packageName);
        boolean workspacePackage = workspacePackageDir != null;
        Path sourcePackageDir = overridePackage
                ? overridePackageDir
                : filePackage
                ? filePackageDir
                : workspacePackage
                ? workspacePackageDir
                : resolveInstalledPackageDir(packageName, workspaceRoot, projectRoot, runtimeNodeModules);
        if (sourcePackageDir == null || !Files.isDirectory(sourcePackageDir)) {
            if (versionRange != null && !versionRange.isBlank()) {
                npmDependencyMaterializer.materializePackageDependency(packageName, versionRange, runtimeNodeModules);
                Path installedPackageDir = runtimeNodeModules.resolve(
                        packageName.replace('/', java.io.File.separatorChar)).normalize();
                patchQinJvmHostPackage(packageName, installedPackageDir);
                ensureRuntimePackageStamp(installedPackageDir);
                for (Map.Entry<String, String> dependency : readDependencyVersions(
                        installedPackageDir.resolve("package.json")).entrySet()) {
                    materializeDependency(
                            dependency.getKey(),
                            dependency.getValue(),
                            installedPackageDir,
                            projectRoot,
                            runtimeNodeModules,
                            workspaceRoot,
                            workspacePackages,
                            packageOverrides,
                            materialized);
                }
            }
            return;
        }

        Path targetPackageDir = runtimeNodeModules.resolve(packageName.replace('/', java.io.File.separatorChar)).normalize();
        boolean sourceIsTarget = sourcePackageDir.toAbsolutePath().normalize()
                .equals(targetPackageDir.toAbsolutePath().normalize());
        if (!sourceIsTarget) {
            boolean workspaceLikePackage = workspacePackage || overridePackage;
            if (!isMaterializedPackageFresh(sourcePackageDir, targetPackageDir, workspaceLikePackage, false)) {
                deleteRecursively(targetPackageDir);
                Files.createDirectories(targetPackageDir.getParent());
                copyPackageTree(sourcePackageDir, targetPackageDir, workspaceLikePackage, false);
                writeMaterializedPackageStamp(sourcePackageDir, targetPackageDir, workspaceLikePackage, false);
            }
        }
        patchQinJvmHostPackage(packageName, targetPackageDir);
        ensureRuntimePackageStamp(targetPackageDir);

        if ((workspacePackage || overridePackage || filePackage)
                && shouldRewriteWorkspacePackageManifest(packageName, sourcePackageDir, overridePackage)) {
            rewriteWorkspacePackageManifest(targetPackageDir, sourcePackageDir, packageName);
            writeRuntimePackageStamp(targetPackageDir);
        }

        for (Map.Entry<String, String> dependency : readDependencyVersions(sourcePackageDir.resolve("package.json")).entrySet()) {
            materializeDependency(
                    dependency.getKey(),
                    dependency.getValue(),
                    sourcePackageDir,
                    projectRoot,
                    runtimeNodeModules,
                    workspaceRoot,
                    workspacePackages,
                    packageOverrides,
                    materialized);
        }
        for (String importedSpecifier : scanPackageBareModuleSpecifiers(
                targetPackageDir,
                sourcePackageDir)) {
            String importedPackage = parseBarePackageName(importedSpecifier);
            if (packageName.equals(importedPackage)) {
                continue;
            }
            materializeDependency(
                    importedSpecifier,
                    null,
                    null,
                    projectRoot,
                    runtimeNodeModules,
                    workspaceRoot,
                    workspacePackages,
                    packageOverrides,
                    materialized);
        }
    }

    private void pruneStaleQinMaterializedPackages(Path runtimeNodeModules, Set<String> activePackages) throws IOException {
        if (!Files.isDirectory(runtimeNodeModules)) {
            return;
        }
        for (Path packageDir : installedPackageDirs(runtimeNodeModules)) {
            Path relative = runtimeNodeModules.relativize(packageDir.toAbsolutePath().normalize());
            String packageName = relative.toString().replace('\\', '/');
            if (!activePackages.contains(packageName)
                    && Files.isRegularFile(packageDir.resolve(".qin-package-sync.json"))) {
                deleteRecursively(packageDir);
            }
        }
    }

    private void pruneStaleInvocationWrappers(Path wrapperDir, Path activeWrapperFile) throws IOException {
        if (!Files.isDirectory(wrapperDir)) {
            return;
        }
        Path active = activeWrapperFile.toAbsolutePath().normalize();
        try (var files = Files.list(wrapperDir)) {
            for (Path file : files.toList()) {
                if (!Files.isRegularFile(file)) {
                    continue;
                }
                String name = file.getFileName() == null ? "" : file.getFileName().toString();
                if (name.startsWith("invoke-")
                        && name.endsWith(".js")
                        && !file.toAbsolutePath().normalize().equals(active)) {
                    Files.deleteIfExists(file);
                }
            }
        }
    }

    private void patchQinJvmHostPackage(String packageName, Path targetPackageDir) throws IOException {
        if ("@vitejs/plugin-vue".equals(packageName)) {
            patchVitePluginVueForQinStaticCompilerImport(targetPackageDir);
        }
        if ("ovs-compiler".equals(packageName)) {
            patchOvsCompilerForQinJvmHost(targetPackageDir);
        }
        if ("lru-cache".equals(packageName)) {
            patchLruCacheForQinJvmHost(targetPackageDir);
        }
    }

    private Path resolveFileDependencyDir(String versionRange, Path dependencyBaseDir) {
        if (versionRange == null || dependencyBaseDir == null || !versionRange.startsWith("file:")) {
            return null;
        }
        String pathText = versionRange.substring("file:".length());
        if (pathText.isBlank()) {
            return null;
        }
        Path candidate = Path.of(pathText);
        if (!candidate.isAbsolute()) {
            candidate = dependencyBaseDir.resolve(pathText);
        }
        candidate = candidate.toAbsolutePath().normalize();
        if (Files.isDirectory(candidate) && Files.isRegularFile(candidate.resolve("package.json"))) {
            return candidate;
        }
        return null;
    }

    private void patchVitePluginVueForQinStaticCompilerImport(Path packageDir) throws IOException {
        Path entry = packageDir.resolve("dist").resolve("index.mjs");
        Files.createDirectories(entry.getParent());
        Files.writeString(entry, qinVitePluginVueShimSource(), StandardCharsets.UTF_8);
    }

    private String qinVitePluginVueShimSource() {
        return qinVueCompilerSfcHostSource()
                + System.lineSeparator()
                + """
                function __qinParseVueQuery(id) {
                  const text = String(id || "");
                  const question = text.indexOf("?");
                  const query = {};
                  if (question < 0) return query;
                  for (const part of text.slice(question + 1).split("&")) {
                    if (!part) continue;
                    const eq = part.indexOf("=");
                    if (eq < 0) query[decodeURIComponent(part)] = true;
                    else query[decodeURIComponent(part.slice(0, eq))] = decodeURIComponent(part.slice(eq + 1));
                  }
                  return query;
                }
                function __qinStripVueQuery(id) {
                  const text = String(id || "");
                  const question = text.indexOf("?");
                  return question < 0 ? text : text.slice(0, question);
                }
                function __qinTemplateAsModule(templateCode) {
                  return String(templateCode || "")
                    .replace(/export\\s+function\\s+render\\s*\\(/, "function render(")
                    .replace(/export\\s+function\\s+ssrRender\\s*\\(/, "function ssrRender(");
                }
                const __qinExportDefaultPrefix = String.fromCharCode(101, 120, 112, 111, 114, 116) + " default ";
                function __qinCompileVueMain(code, id, options) {
                  const compiler = (options && options.compiler) || __qinVueCompilerSfc;
                  const parsed = compiler.parse(String(code || ""), { filename: id });
                  const descriptor = parsed.descriptor || {};
                  if (parsed.errors && parsed.errors.length) {
                    throw new Error(String(parsed.errors[0]));
                  }
                  const script = compiler.compileScript(descriptor, { id, genDefaultAs: "_sfc_main" });
                  let out = String(script && script.content || "const _sfc_main = {}") + "\\n";
                  if (descriptor.template) {
                    const template = compiler.compileTemplate({
                      source: descriptor.template.content || "",
                      filename: id,
                      id
                    });
                    if (template.errors && template.errors.length) {
                      throw new Error(String(template.errors[0]));
                    }
                    out += __qinTemplateAsModule(template.code) + "\\n";
                    out += "_sfc_main.render = render;\\n";
                  }
                  out += __qinExportDefaultPrefix + "_sfc_main;\\n";
                  return { code: out, map: null };
                }
                function __qinCompileVueQuery(code, id, options) {
                  const query = __qinParseVueQuery(id);
                  const filename = __qinStripVueQuery(id);
                  const compiler = (options && options.compiler) || __qinVueCompilerSfc;
                  const parsed = compiler.parse(String(code || ""), { filename });
                  const descriptor = parsed.descriptor || {};
                  if (query.type === "template" && descriptor.template) {
                    const template = compiler.compileTemplate({
                      source: descriptor.template.content || "",
                      filename,
                      id: filename
                    });
                    return { code: template.code, map: null };
                  }
                  if (query.type === "script") {
                    const script = compiler.compileScript(descriptor, { id: filename, genDefaultAs: "_sfc_main" });
                    return { code: String(script && script.content || "const _sfc_main = {}") + "\\n" + __qinExportDefaultPrefix + "_sfc_main;\\n", map: null };
                  }
                  if (query.type === "style") {
                    const index = Number(query.index || 0);
                    const style = descriptor.styles && descriptor.styles[index];
                    return { code: style ? style.content || "" : "", map: null };
                  }
                  return null;
                }
                function vuePlugin(rawOptions = {}) {
                  const options = rawOptions || {};
                  return {
                    name: "vite:vue",
                    config() {
                      return { define: { __VUE_OPTIONS_API__: true, __VUE_PROD_DEVTOOLS__: false } };
                    },
                    configResolved(config) {
                      options.devServer = config && config.server ? { config } : null;
                      options.root = config && config.root;
                      options.isProduction = !!(config && config.isProduction);
                      options.compiler = options.compiler || __qinVueCompilerSfc;
                    },
                    resolveId(id) {
                      if (String(id || "").includes("plugin-vue:export-helper")) return "plugin-vue:qin-helper";
                      return null;
                    },
                    load(id) {
                      if (id === "plugin-vue:qin-helper") {
                        return __qinExportDefaultPrefix + "(sfc, props) => { for (const [key, val] of props) sfc[key] = val; return sfc; }";
                      }
                      return null;
                    },
                    transform(code, id) {
                      const text = String(id || "");
                      if (!text.includes(".vue")) return null;
                      if (text.includes("?vue")) return __qinCompileVueQuery(code, id, options);
                      return __qinCompileVueMain(code, id, options);
                    },
                    handleHotUpdate(ctx) {
                      if (ctx && ctx.server && ctx.server.ws) {
                        ctx.server.ws.send({ type: "full-reload", path: ctx.file });
                      }
                      return ctx && ctx.modules ? ctx.modules : [];
                    }
                  };
                }
                export { vuePlugin as default };
                """;
    }

    @SuppressWarnings("unused")
    private String patchVitePluginVueForQinStaticCompilerImportLegacy(String source) {
        String patched = source.replace("import * as __qinVueCompilerSfc from \"@vue/compiler-sfc\";\n", "");
        if (patched.contains("const __qinVueCompilerSfc =")) {
            patched = replaceQinVueCompilerSfcHost(patched);
        } else {
            patched = patched.replace(
                    "//#region package.json",
                    qinVueCompilerSfcHostSource() + System.lineSeparator() + "//#region package.json");
        }
        if (patched.contains("function tryRequire(id, from)")) {
            patched = patched.replace(
                    """
                    function createDescriptor(filename, source, { root, isProduction, sourceMap, compiler, template, features }, hmr = false) {
                    \tconst { descriptor, errors } = compiler.parse(source, {
                    """,
                    """
                    function createDescriptor(filename, source, { root, isProduction, sourceMap, compiler, template, features }, hmr = false) {
                    \tconst { descriptor, errors } = __qinVueCompilerSfc.parse(source, {
                    """);
            patched = patched.replace(
                    """
                    function transformMain(code, filename, options, pluginContext, ssr, customElement) {
                    \tconst { devServer, isProduction, devToolsEnabled } = options;
                    """,
                    """
                    function transformMain(code, filename, options, pluginContext, ssr, customElement) {
                    \toptions.compiler = __qinVueCompilerSfc;
                    \tconst { devServer, isProduction, devToolsEnabled } = options;
                    """);
            patched = patched.replace(
                    """
                    function resolveCompiler(root) {
                    \tconst compiler = tryResolveCompiler(root) || tryResolveCompiler();
                    \tif (!compiler) throw new Error("Failed to resolve vue/compiler-sfc.\\n@vitejs/plugin-vue requires vue (>=3.2.25) to be present in the dependency tree.");
                    \treturn compiler;
                    }
                    """,
                    """
                    function resolveCompiler(root) {
                    \treturn __qinVueCompilerSfc;
                    }
                    """);
            patched = patched.replace(
                    """
                    function tryResolveCompiler(root) {
                    \tconst vueMeta = tryRequire("vue/package.json", root);
                    \tif (vueMeta && vueMeta.version.split(".")[0] >= 3) return tryRequire("vue/compiler-sfc", root);
                    }
                    """,
                    """
                    function tryResolveCompiler(root) {
                    \treturn __qinVueCompilerSfc;
                    }
                    """);
            patched = patched.replace(
                    """
                    function tryRequire(id, from) {
                    \ttry {
                    \t\treturn from ? _require(_require.resolve(id, { paths: [from] })) : _require(id);
                    \t} catch (e) {}
                    }
                    """,
                    """
                    function tryRequire(id, from) {
                    \tif (id === "vue/package.json") return { version: "3.5.0" };
                    \tif (id === "vue/compiler-sfc") return __qinVueCompilerSfc;
                    }
                    """);
        }
        patched = patchVitePluginVueSourcemapParseName(patched);
        patched = patchVitePluginVueHelperCodeTemplate(patched);
        patched = patchVitePluginVueSyncTransforms(patched);
        return patched;
    }

    private String patchVitePluginVueHelperCodeTemplate(String source) {
        String helperTemplate = """
                const helperCode = `
                export default (sfc, props) => {
                  const target = sfc.__vccOpts || sfc;
                  for (const [key, val] of props) {
                    target[key] = val;
                  }
                  return target;
                }
                `;
                """;
        if (!source.contains(helperTemplate)) {
            return source;
        }
        return source
                .replace("EXPORT_HELPER_ID", "QIN_VUE_HELPER_ID")
                .replace("plugin-vue:export-helper", "plugin-vue:qin-helper")
                .replace("_export_sfc", "_qin_sfc_helper")
                .replace("${nl}export function render", "${nl}${String.fromCharCode(101, 120, 112, 111, 114, 116)} function render")
                .replace("\\nexport const multiRoot", "\\n${String.fromCharCode(101, 120, 112, 111, 114, 116)} const multiRoot")
                .replace("`export const _rerender_only = ", "String.fromCharCode(101, 120, 112, 111, 114, 116) + ` const _rerender_only = ")
                .replace("`export default _sfc_main`", "String.fromCharCode(101, 120, 112, 111, 114, 116) + \" default _sfc_main\"")
                .replace("`export default /*#__PURE__*/_qin_sfc_helper", "String.fromCharCode(101, 120, 112, 111, 114, 116) + ` default /*#__PURE__*/_qin_sfc_helper")
                .replace("\\nexport * from ${request}", "\\n${String.fromCharCode(101, 120, 112, 111, 114, 116)} * from ${request}")
                .replace(
                helperTemplate,
                "const helperCode = \"\\n\" + String.fromCharCode(101, 120, 112, 111, 114, 116) + \" default (sfc, props) => {\\n\"\n"
                        + "  + \"  const target = sfc.__vccOpts || sfc;\\n\"\n"
                        + "  + \"  for (const [key, val] of props) {\\n\"\n"
                        + "  + \"    target[key] = val;\\n\"\n"
                        + "  + \"  }\\n\"\n"
                        + "  + \"  return target;\\n\"\n"
                        + "  + \"}\\n\";\n");
    }

    private String patchVitePluginVueSourcemapParseName(String source) {
        return source
                .replace(
                        """
                        function parse(map) {
                        \treturn typeof map === "string" ? JSON.parse(map) : map;
                        }
                        var TraceMap = class {
                        """,
                        """
                        function qinSourcemapParse(map) {
                        \treturn typeof map === "string" ? JSON.parse(map) : map;
                        }
                        var TraceMap = class {
                        """)
                .replace("const parsed = parse(map);", "const parsed = qinSourcemapParse(map);");
    }

    private String quoteJsString(String value) {
        StringBuilder out = new StringBuilder("\"");
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            switch (ch) {
                case '\\' -> out.append("\\\\");
                case '"' -> out.append("\\\"");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> out.append(ch);
            }
        }
        return out.append('"').toString();
    }

    private void patchOvsCompilerForQinJvmHost(Path packageDir) throws IOException {
        Path entry = packageDir.resolve("dist").resolve("index.mjs");
        if (!Files.isRegularFile(entry)) {
            return;
        }
        String source = Files.readString(entry, StandardCharsets.UTF_8);
        String patched = patchOvsCompilerDecorateHelpers(source);
        patched = patchOvsCompilerCstToAstProxyFacade(patched);
        if (!patched.equals(source)) {
            Files.writeString(entry, patched, StandardCharsets.UTF_8);
        }
    }

    private String patchOvsCompilerDecorateHelpers(String source) {
        String patched = source.replace(
                """
                function __decorateMetadata(k, v) {
                \tif (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
                }
                """,
                """
                function __decorateMetadata(k, v) {
                }
                """);
        return patched.replace(
                """
                function __decorate(decorators, target, key, desc) {
                \tvar c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
                \tif (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
                \telse for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
                \treturn c > 3 && r && Object.defineProperty(target, key, r), r;
                }
                """,
                """
                function __decorate(decorators, target, key, desc) {
                \tvar c = key === void 0 ? 2 : 4;
                \tvar r = c < 3 ? target : desc === null ? { value: target[key], writable: true, enumerable: false, configurable: true } : desc;
                \tvar d;
                \tfor (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
                \tif (c > 3 && r && r.value !== void 0) Object.defineProperty(target, key, r);
                \treturn r;
                }
                """);
    }

    private String patchOvsCompilerCstToAstProxyFacade(String source) {
        return source.replace(
                """
                const OvsCstToSlimeAstUtils = new Proxy({}, { get(_, prop) {
                \tconst val = _ovsCstToSlimeAstUtil[prop];
                \treturn typeof val === "function" ? val.bind(_ovsCstToSlimeAstUtil) : val;
                } });
                """,
                """
                const OvsCstToSlimeAstUtils = {};
                function bindOvsCstToSlimeAstUtilsForwarders() {
                \tconst props = ["toProgram", "toFileAst", "clearUsedAtoms", "getUsedAtoms"];
                \tfor (const prop of props) {
                \t\tOvsCstToSlimeAstUtils[prop] = function(...args) {
                \t\t\treturn _ovsCstToSlimeAstUtil[prop](...args);
                \t\t};
                \t}
                }
                bindOvsCstToSlimeAstUtilsForwarders();
                """);
    }

    private void patchLruCacheForQinJvmHost(Path packageDir) throws IOException {
        Path entry = packageDir.resolve("dist").resolve("esm").resolve("index.min.js");
        if (!Files.isRegularFile(entry)) {
            return;
        }
        String source = Files.readString(entry, StandardCharsets.UTF_8);
        String patched = patchLruCacheOptionalDiagnosticsImport(source)
                .replace("[Symbol.toStringTag]=\"LRUCache\";", "");
        if (!patched.equals(source)) {
            Files.writeString(entry, patched, StandardCharsets.UTF_8);
        }
    }

    private String patchLruCacheOptionalDiagnosticsImport(String source) {
        return Pattern.compile(
                "import\\(\"node:diagnostics_channel\"\\)\\.then\\([^;]+?\\)\\.catch\\(\\(\\)=>\\{\\}\\);",
                Pattern.DOTALL)
                .matcher(source)
                .replaceFirst("");
    }

    private boolean shouldRewriteWorkspacePackageManifest(
            String packageName,
            Path sourcePackageDir,
            boolean packageOverride) throws IOException {
        WorkspacePackageEntrypoint entrypoint = inspectWorkspacePackageEntrypoint(sourcePackageDir);
        if (packageOverride && entrypoint.hasSourceEntry() && !entrypoint.hasManifestEntry()) {
            return true;
        }
        if (hasResolvablePublishedEntry(sourcePackageDir, entrypoint)) {
            return false;
        }
        return entrypoint.hasSourceEntry()
                && (entrypoint.declaredSourceEntry() || !entrypoint.hasManifestEntry());
    }

    private boolean hasResolvablePublishedEntry(Path sourcePackageDir, WorkspacePackageEntrypoint entrypoint) {
        if (!entrypoint.hasManifestEntry()) {
            return false;
        }
        String normalizedEntry = entrypoint.manifestEntry().replace('\\', '/');
        if (!normalizedEntry.startsWith("./dist/") && !normalizedEntry.startsWith("dist/")) {
            return false;
        }
        return Files.isRegularFile(sourcePackageDir.resolve(normalizeManifestRelativePath(normalizedEntry)));
    }

    private String replaceQinVueCompilerSfcHost(String source) {
        String startMarker = "function __qinCreateVueCompilerSfc() {";
        int start = source.indexOf(startMarker);
        if (start < 0) {
            startMarker = "const __qinVueCompilerSfc = (function() {";
            start = source.indexOf(startMarker);
        }
        if (start < 0) {
            startMarker = "const __qinVueCompilerSfc = (() => {";
            start = source.indexOf(startMarker);
        }
        String endMarker = System.lineSeparator() + "//#region package.json";
        int end = source.indexOf(endMarker, start);
        if (start < 0 || end < 0) {
            return source;
        }
        return source.substring(0, start)
                + qinVueCompilerSfcHostSource()
                + source.substring(end);
    }

    private String patchVitePluginVueSyncTransforms(String source) {
        String patched = source
                .replace("async function transformTemplateAsModule", "function transformTemplateAsModule")
                .replace("async function transformMain", "function transformMain")
                .replace("async function genTemplateCode", "function genTemplateCode")
                .replace("async function genScriptCode", "function genScriptCode")
                .replace("async function genStyleCode", "function genStyleCode")
                .replace("async function genCustomBlockCode", "function genCustomBlockCode")
                .replace("async function transformStyle", "function transformStyle")
                .replace("await genScriptCode(", "genScriptCode(")
                .replace("await genTemplateCode(", "genTemplateCode(")
                .replace("await genStyleCode(", "genStyleCode(")
                .replace("await genCustomBlockCode(", "genCustomBlockCode(")
                .replace("await linkSrcToDescriptor(", "linkSrcToDescriptor(")
                .replace("await options.compiler.compileStyleAsync(", "options.compiler.compileStyleAsync(")
                .replace("await formatPostcssSourceMap(", "formatPostcssSourceMap(");
        patched = patched.replace(
                "\tif (hasTemplateImport) ({code: templateCode, map: templateMap, multiRoot: templateMultiRoot} = genTemplateCode(descriptor, options, pluginContext, ssr, customElement));",
                "\tif (hasTemplateImport) { const __qin_template_result = genTemplateCode(descriptor, options, pluginContext, ssr, customElement); templateCode = __qin_template_result.code; templateMap = __qin_template_result.map; templateMultiRoot = __qin_template_result.multiRoot; }");
        return removeVitePluginVueTsPostTransformBlock(patched);
    }

    private String removeVitePluginVueTsPostTransformBlock(String source) {
        String marker = "\tif (lang && /tsx?$/.test(lang) && !descriptor.script?.src) {";
        int start = source.indexOf(marker);
        if (start < 0) {
            return source;
        }
        int end = source.indexOf("\n\treturn {", start);
        if (end < 0) {
            return source;
        }
        return source.substring(0, start) + source.substring(end + 1);
    }

    private String qinVueCompilerSfcHostSource() {
        return """
                function __qinCreateVueCompilerSfc() {
                  const nl = String.fromCharCode(10);
                  function loc(source, start, end) {
                    return { start: position(source, start), end: position(source, end), source: source.slice(start, end) };
                  }
                  function position(source, offset) {
                    let line = 1;
                    let column = 1;
                    for (let i = 0; i < offset; i++) {
                      if (source.charCodeAt(i) === 10) {
                        line++;
                        column = 1;
                      } else {
                        column++;
                      }
                    }
                    return { offset, line, column };
                  }
                  function parseAttrs(raw) {
                    const attrs = {};
                    const rawAttrs = [];
                    const text = raw || "";
                    const parts = text.trim() ? text.trim().split(" ") : [];
                    for (const part of parts) {
                      if (!part) continue;
                      const eq = part.indexOf("=");
                      const name = eq >= 0 ? part.slice(0, eq) : part;
                      let value = eq >= 0 ? part.slice(eq + 1) : true;
                      if (typeof value === "string" && value.length >= 2) {
                        const quote = value[0];
                        if ((quote === '"' || quote === "'") && value[value.length - 1] === quote) {
                          value = value.slice(1, -1);
                        }
                      }
                      attrs[name] = value;
                      if (name === "lang") attrs.lang = value;
                      if (name === "setup") attrs.setup = true;
                      if (name === "scoped") attrs.scoped = true;
                      rawAttrs.push({ name, value: value === true ? void 0 : { content: value } });
                    }
                    return { attrs, rawAttrs };
                  }
                  function block(source, tag, index) {
                    const open = source.indexOf("<" + tag, index);
                    if (open < 0) return null;
                    const openEnd = source.indexOf(">", open);
                    if (openEnd < 0) return null;
                    const close = source.indexOf("</" + tag + ">", openEnd + 1);
                    if (close < 0) return null;
                    const closeEnd = close + tag.length + 3;
                    const rawAttrs = source.slice(open + tag.length + 1, openEnd);
                    const parsed = parseAttrs(rawAttrs);
                    return {
                      type: tag,
                      tag,
                      content: source.slice(openEnd + 1, close),
                      attrs: parsed.attrs,
                      rawAttrs: parsed.rawAttrs,
                      lang: parsed.attrs.lang,
                      setup: !!parsed.attrs.setup,
                      scoped: !!parsed.attrs.scoped,
                      loc: loc(source, open, closeEnd),
                      map: { mappings: "" },
                      start: openEnd + 1,
                      end: close,
                      next: closeEnd
                    };
                  }
                  function parse(source, options = {}) {
                    const filename = options.filename || "anonymous.vue";
                    const template = block(source, "template", 0);
                    const firstScript = block(source, "script", 0);
                    const descriptor = {
                      filename,
                      source,
                      template,
                      script: firstScript && !firstScript.setup ? firstScript : null,
                      scriptSetup: firstScript && firstScript.setup ? firstScript : null,
                      styles: [],
                      customBlocks: [],
                      qinScriptSetupImports: firstScript && firstScript.setup ? importLinesFromCode(firstScript.content || "") : "",
                      cssVars: [],
                      slotted: false,
                      vapor: false,
                      shouldForceReload() { return false; }
                    };
                    let styleIndex = 0;
                    while (true) {
                      const style = block(source, "style", styleIndex);
                      if (!style) break;
                      descriptor.styles.push(style);
                      styleIndex = style.next;
                    }
                    return { descriptor, errors: [] };
                  }
                  function namesFromSetup(code) {
                    const names = [];
                    let depth = 0;
                    for (const line of String(code || "").split("\\n")) {
                      const text = line.trim();
                      if (depth === 0) {
                        for (const kind of ["const ", "let ", "var "]) {
                          if (!text.startsWith(kind)) continue;
                          let rest = text.slice(kind.length).trim();
                          let name = "";
                          for (let i = 0; i < rest.length; i++) {
                            const ch = rest[i];
                            const isStart = (ch >= "A" && ch <= "Z") || (ch >= "a" && ch <= "z") || ch === "_" || ch === "$";
                            const isPart = isStart || (i > 0 && ch >= "0" && ch <= "9");
                            if (!isPart) break;
                            name += ch;
                          }
                          if (name) names.push(name);
                        }
                      }
                      depth += braceDeltaOutsideStrings(text);
                      if (depth < 0) depth = 0;
                    }
                    return names;
                  }
                  function braceDeltaOutsideStrings(text) {
                    let delta = 0;
                    let quote = "";
                    let escaped = false;
                    for (let i = 0; i < text.length; i++) {
                      const ch = text[i];
                      if (escaped) {
                        escaped = false;
                        continue;
                      }
                      if (ch === "\\\\") {
                        escaped = true;
                        continue;
                      }
                      if (quote) {
                        if (ch === quote) quote = "";
                        continue;
                      }
                      if (ch === '"' || ch === "'" || ch === "`") {
                        quote = ch;
                        continue;
                      }
                      if (ch === "{") delta++;
                      else if (ch === "}") delta--;
                    }
                    return delta;
                  }
                  function importLinesFromCode(code) {
                    let importsText = "";
                    for (const line of String(code || "").split("\\n")) {
                      if (/^\\s*import\\b/.test(line)) importsText += line + nl;
                    }
                    return importsText;
                  }
                  function appendDefaultNamesFromImportText(importsText, names) {
                    for (const line of String(importsText || "").split("\\n")) {
                      appendDefaultNameFromImport(line, names);
                      appendNamedNamesFromImport(line, names);
                    }
                  }
                  function appendDefaultNameFromImport(line, names) {
                    const text = String(line || "").trim();
                    if (!text.startsWith("import ")) return;
                    const fromIndex = text.indexOf(" from ");
                    if (fromIndex < 0) return;
                    let clause = text.slice(7, fromIndex).trim();
                    const comma = clause.indexOf(",");
                    if (comma >= 0) clause = clause.slice(0, comma).trim();
                    if (!clause || clause.startsWith("{") || clause.startsWith("*")) return;
                    names.push(clause);
                  }
                  function appendNamedNamesFromImport(line, names) {
                    const text = String(line || "").trim();
                    if (!text.startsWith("import ")) return;
                    const open = text.indexOf("{");
                    const close = text.indexOf("}", open + 1);
                    if (open < 0 || close < 0) return;
                    const body = text.slice(open + 1, close);
                    for (const rawPart of body.split(",")) {
                      let part = rawPart.trim();
                      if (!part) continue;
                      const asIndex = part.indexOf(" as ");
                      if (asIndex >= 0) part = part.slice(asIndex + 4).trim();
                      if (/^[A-Za-z_$][\\w$]*$/.test(part)) names.push(part);
                    }
                  }
                  function compileScript(descriptor, options = {}) {
                    if (descriptor.scriptSetup) {
                      const code = descriptor.scriptSetup.content || "";
                      const sourceForImports = descriptor.source
                        || (descriptor.scriptSetup.loc && descriptor.scriptSetup.loc.source)
                        || "";
                      const originalScriptSetup = sourceForImports ? block(sourceForImports, "script", 0) : null;
                      const importSourceCode = originalScriptSetup && originalScriptSetup.setup
                        ? originalScriptSetup.content || ""
                        : sourceForImports || code;
                      let importsText = importLinesFromCode(importSourceCode);
                      let bodyText = "";
                      const names = [];
                      appendDefaultNamesFromImportText(importsText, names);
                      for (const line of code.split(/\\r?\\n/)) {
                        if (/^\\s*import\\b/.test(line)) {
                          if (!importsText) {
                            importsText += line + nl;
                            appendDefaultNameFromImport(line, names);
                            appendNamedNamesFromImport(line, names);
                          }
                        } else {
                          bodyText += line + nl;
                        }
                      }
                      for (const name of namesFromSetup(bodyText)) {
                        names.push(name);
                      }
                      const returned = names.length ? `{ ${names.join(", ")} }` : "{}";
                      return {
                        content: importsText
                          + "const _sfc_main = { setup(__props) {" + nl
                          + bodyText
                          + "return " + returned + ";" + nl
                          + "} };",
                        map: null,
                        bindings: {}
                      };
                    }
                    if (descriptor.script) {
                      return { content: rewriteDefault(descriptor.script.content || "", options.genDefaultAs || "_sfc_main"), map: null, bindings: {} };
                    }
                    return { content: `const ${options.genDefaultAs || "_sfc_main"} = {}`, map: null, bindings: {} };
                  }
                  function rewriteDefault(code, name) {
                    const text = String(code || "");
                    if (/export\\s+default/.test(text)) return text.replace(/export\\s+default/, `const ${name} =`);
                    return `${text}\\nconst ${name} = {}`;
                  }
                  function compileTemplate(options = {}) {
                    const source = String(options.source || "");
                    const roots = parseTemplateNodes(source);
                    const expression = roots.length === 1
                      ? emitTemplateNode(roots[0])
                      : `[${roots.map(emitTemplateNode).join(", ")}]`;
                    return {
                      code: `import { h as _h, toDisplayString as _toDisplayString } from "vue"${nl}export function render(_ctx, _cache) { return ${expression}; }`,
                      ast: {},
                      tips: [],
                      errors: [],
                      map: null
                    };
                  }
                  function parseTemplateNodes(source) {
                    const root = { type: "root", children: [] };
                    const stack = [root];
                    let index = 0;
                    while (index < source.length) {
                      const lt = source.indexOf("<", index);
                      if (lt < 0) {
                        appendText(stack[stack.length - 1], source.slice(index));
                        break;
                      }
                      appendText(stack[stack.length - 1], source.slice(index, lt));
                      if (source.startsWith("<!--", lt)) {
                        const endComment = source.indexOf("-->", lt + 4);
                        index = endComment < 0 ? source.length : endComment + 3;
                        continue;
                      }
                      if (source.startsWith("</", lt)) {
                        const closeEnd = source.indexOf(">", lt + 2);
                        if (closeEnd < 0) break;
                        const closeTag = source.slice(lt + 2, closeEnd).trim().toLowerCase();
                        while (stack.length > 1 && stack[stack.length - 1].tag.toLowerCase() !== closeTag) {
                          stack.pop();
                        }
                        if (stack.length > 1) stack.pop();
                        index = closeEnd + 1;
                        continue;
                      }
                      const tagEnd = findTagEnd(source, lt + 1);
                      if (tagEnd < 0) break;
                      const raw = source.slice(lt + 1, tagEnd).trim();
                      const selfClosing = raw.endsWith("/");
                      const clean = selfClosing ? raw.slice(0, -1).trim() : raw;
                      const space = firstWhitespace(clean);
                      const tag = space < 0 ? clean : clean.slice(0, space);
                      const rawAttrs = space < 0 ? "" : clean.slice(space + 1);
                      const node = { type: "element", tag, attrs: parseTemplateAttrs(rawAttrs), children: [] };
                      stack[stack.length - 1].children.push(node);
                      if (!selfClosing && !isVoidTag(tag)) {
                        stack.push(node);
                      }
                      index = tagEnd + 1;
                    }
                    return root.children.filter(node => !(node.type === "text" && !node.value.trim()));
                  }
                  function appendText(parent, text) {
                    if (!text) return;
                    const parts = String(text).split(/(\\{\\{[\\s\\S]*?\\}\\})/g);
                    for (const part of parts) {
                      if (!part) continue;
                      if (part.startsWith("{{") && part.endsWith("}}")) {
                        const expression = part.slice(2, -2).trim();
                        if (expression) parent.children.push({ type: "expression", value: expression });
                        continue;
                      }
                      const normalized = part.replace(/\\s+/g, " ");
                      if (normalized.trim()) parent.children.push({ type: "text", value: normalized });
                    }
                  }
                  function findTagEnd(source, start) {
                    let quote = "";
                    for (let i = start; i < source.length; i++) {
                      const ch = source[i];
                      if (quote) {
                        if (ch === quote) quote = "";
                        continue;
                      }
                      if (ch === '"' || ch === "'") {
                        quote = ch;
                        continue;
                      }
                      if (ch === ">") return i;
                    }
                    return -1;
                  }
                  function firstWhitespace(text) {
                    for (let i = 0; i < text.length; i++) {
                      const code = text.charCodeAt(i);
                      if (code === 32 || code === 9 || code === 10 || code === 13 || code === 12) return i;
                    }
                    return -1;
                  }
                  function parseTemplateAttrs(raw) {
                    const attrs = [];
                    const pattern = /([^\\s=]+)(?:\\s*=\\s*("[^"]*"|'[^']*'|[^\\s"']+))?/g;
                    let match;
                    while ((match = pattern.exec(raw || ""))) {
                      let value = match[2];
                      if (value == null) value = true;
                      else if ((value.startsWith('"') && value.endsWith('"')) || (value.startsWith("'") && value.endsWith("'"))) {
                        value = value.slice(1, -1);
                      }
                      attrs.push({ name: match[1], value });
                    }
                    return attrs;
                  }
                  function emitTemplateNode(node) {
                    if (node.type === "text") return JSON.stringify(node.value);
                    if (node.type === "expression") return `_toDisplayString(_ctx.${node.value})`;
                    const tagExpression = isComponentTag(node.tag) ? `_ctx.${node.tag}` : JSON.stringify(node.tag);
                    const props = emitProps(node.attrs);
                    const children = node.children.map(emitTemplateNode).filter((child) => !!child);
                    const childrenExpression = children.length === 0
                      ? "null"
                      : children.length === 1
                        ? children[0]
                        : `[${children.join(", ")}]`;
                    return `_h(${tagExpression}, ${props}, ${childrenExpression})`;
                  }
                  function emitProps(attrs) {
                    const entries = [];
                    for (const attr of attrs || []) {
                      const name = String(attr.name || "");
                      if (!name) continue;
                      if (name.startsWith(":")) {
                        entries.push(`${JSON.stringify(name.slice(1))}: _ctx.${attr.value}`);
                      } else if (name.startsWith("@")) {
                        const event = "on" + name.slice(1, 2).toUpperCase() + name.slice(2);
                        entries.push(`${JSON.stringify(event)}: $event => (_ctx.${String(attr.value).replace(/;\\s*$/, "")})`);
                      } else if (attr.value === true) {
                        entries.push(`${JSON.stringify(name)}: true`);
                      } else {
                        entries.push(`${JSON.stringify(name)}: ${JSON.stringify(attr.value)}`);
                      }
                    }
                    return entries.length ? `{ ${entries.join(", ")} }` : "null";
                  }
                  function isComponentTag(tag) {
                    const first = String(tag || "")[0] || "";
                    return first >= "A" && first <= "Z";
                  }
                  function isVoidTag(tag) {
                    return ["area", "base", "br", "col", "embed", "hr", "img", "input", "link", "meta", "param", "source", "track", "wbr"].includes(String(tag || "").toLowerCase());
                  }
                  function compileStyleAsync(options = {}) {
                    return { code: String(options.source || ""), errors: [], map: null };
                  }
                  function firstSelfClosingComponent(source) {
                    const open = source.indexOf("<");
                    if (open < 0 || open + 1 >= source.length) return "";
                    const first = source[open + 1];
                    if (!(first >= "A" && first <= "Z")) return "";
                    let name = "";
                    for (let i = open + 1; i < source.length; i++) {
                      const ch = source[i];
                      const part = (ch >= "A" && ch <= "Z") || (ch >= "a" && ch <= "z") || (ch >= "0" && ch <= "9") || ch === "_";
                      if (!part) break;
                      name += ch;
                    }
                    return source.indexOf("</" + name + ">") < 0 ? name : "";
                  }
                  function firstAttrValue(source, name) {
                    const marker = name + "=";
                    const index = source.indexOf(marker);
                    if (index < 0) return "";
                    const quote = source[index + marker.length];
                    if (quote !== '"' && quote !== "'") return "";
                    const start = index + marker.length + 1;
                    const end = source.indexOf(quote, start);
                    return end < 0 ? "" : source.slice(start, end);
                  }
                  function firstInterpolation(source) {
                    const start = source.indexOf("{{");
                    if (start < 0) return "";
                    const end = source.indexOf("}}", start + 2);
                    if (end < 0) return "";
                    return source.slice(start + 2, end).trim();
                  }
                  return {
                    version: "3.5.35",
                    parse,
                    compileScript,
                    compileTemplate,
                    compileStyleAsync,
                    rewriteDefault
                  };
                }
                const __qinVueCompilerSfc = __qinCreateVueCompilerSfc();
                """;
    }

    private void materializeQinViteShimIfNeeded(Set<String> bareSpecifiers, Path runtimeNodeModules) throws IOException {
        if (bareSpecifiers == null || bareSpecifiers.isEmpty()) {
            return;
        }
        if (bareSpecifiers.contains("vite") || bareSpecifiers.contains("@vitejs/plugin-vue")) {
            materializeQinViteShim(runtimeNodeModules);
        }
        if (bareSpecifiers.contains("@vue/compiler-sfc")) {
            materializeQinVueCompilerSfcShim(runtimeNodeModules);
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
        writeRuntimePackageStamp(shimDir);
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

                export function defineConfig(config) {
                  return config;
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
        writeRuntimePackageStamp(shimDir);
    }

    private void materializeQinSubhutiShim(Path runtimeNodeModules) throws IOException {
        Path shimDir = runtimeNodeModules.resolve("subhuti").normalize();
        deleteRecursively(shimDir);
        Files.createDirectories(shimDir);
        Files.writeString(shimDir.resolve("package.json"), """
                {
                  "name": "subhuti",
                  "version": "0.0.0-qin-generated-shim",
                  "type": "module",
                  "exports": {
                    ".": "./index.ts"
                  },
                  "main": "./index.ts",
                  "module": "./index.ts",
                  "dependencies": {
                    "@qin/generated-qin-parser-ts": "file:../../../../../../qin/packages/qin-language/generated/qin-parser-ts"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(shimDir.resolve("index.ts"), """
                import { com_subhuti_struct_SubhutiCreateToken as GeneratedSubhutiCreateToken } from "@qin/generated-qin-parser-ts/com/subhuti/struct/SubhutiCreateToken.ts";
                import { com_subhuti_parser_SubhutiParser as GeneratedSubhutiParser } from "@qin/generated-qin-parser-ts/com/subhuti/parser/SubhutiParser.ts";
                import { com_subhuti_cache_SubhutiPackratCache as GeneratedSubhutiPackratCache } from "@qin/generated-qin-parser-ts/com/subhuti/cache/SubhutiPackratCache.ts";
                import { com_subhuti_struct_SubhutiCst as GeneratedSubhutiCst } from "@qin/generated-qin-parser-ts/com/subhuti/struct/SubhutiCst.ts";
                import { com_subhuti_struct_SubhutiMatchToken as GeneratedSubhutiMatchToken } from "@qin/generated-qin-parser-ts/com/subhuti/struct/SubhutiMatchToken.ts";
                import { com_subhuti_lookahead_SubhutiTokenConsumer as GeneratedSubhutiTokenConsumer } from "@qin/generated-qin-parser-ts/com/subhuti/lookahead/SubhutiTokenConsumer.ts";
                import { com_subhuti_lookahead_SubhutiTokenLookahead as GeneratedSubhutiTokenLookahead } from "@qin/generated-qin-parser-ts/com/subhuti/lookahead/SubhutiTokenLookahead.ts";
                import { com_subhuti_lexer_SubhutiLexer as GeneratedSubhutiLexer } from "@qin/generated-qin-parser-ts/com/subhuti/lexer/SubhutiLexer.ts";
                export { Alternative } from "@qin/generated-qin-parser-ts";

                export const SubhutiParser = GeneratedSubhutiParser;
                export default GeneratedSubhutiParser;
                export const SubhutiCreateToken = GeneratedSubhutiCreateToken;
                export const SubhutiPackratCache = GeneratedSubhutiPackratCache;
                export const SubhutiCst = GeneratedSubhutiCst;
                export const SubhutiMatchToken = GeneratedSubhutiMatchToken;
                export const SubhutiTokenConsumer = GeneratedSubhutiTokenConsumer;
                export const SubhutiTokenLookahead = GeneratedSubhutiTokenLookahead;
                export const SubhutiLexer = GeneratedSubhutiLexer;

                function __qin_regex_source(pattern) {
                  if (pattern == null) return "";
                  if (typeof pattern === "string") return pattern;
                  if (pattern.source != null) return String(pattern.source);
                  if (typeof pattern.pattern === "function") return pattern.pattern();
                  return String(pattern);
                }

                export function createRegToken(name, pattern) {
                  return GeneratedSubhutiCreateToken.createRegToken(String(name), __qin_regex_source(pattern));
                }

                export function Subhuti(target) {
                  return target;
                }

                function __qin_mark_subhuti_rule(method, ruleName) {
                  if (!method) return method;
                  Object.defineProperty(method, "__isSubhutiRule__", {
                    value: true,
                    writable: false,
                    enumerable: false,
                    configurable: true
                  });
                  if (ruleName && !method.__qinSubhutiRuleName) {
                    Object.defineProperty(method, "__qinSubhutiRuleName", {
                      value: String(ruleName),
                      writable: false,
                      enumerable: false,
                      configurable: true
                    });
                  }
                  return method;
                }

                function __qin_wrap_subhuti_rule(method, ruleName) {
                  if (!method || method.__qinSubhutiRuleWrapped) return __qin_mark_subhuti_rule(method, ruleName);
                  const original = method;
                  const wrapped = function (...args) {
                    if (this && typeof this.executeRuleWrapper === "function") {
                      return this.executeRuleWrapper(
                        original,
                        String(ruleName || original.name || ""),
                        this.constructor && this.constructor.name ? String(this.constructor.name) : "",
                        ...args
                      );
                    }
                    return original.call(this, ...args);
                  };
                  Object.defineProperty(wrapped, "__qinSubhutiRuleWrapped", {
                    value: true,
                    writable: false,
                    enumerable: false,
                    configurable: true
                  });
                  return __qin_mark_subhuti_rule(wrapped, ruleName || original.name);
                }

                export function SubhutiRule(targetOrMethod, propertyKeyOrContext, descriptor) {
                  if (typeof propertyKeyOrContext === "string") {
                    const actualDescriptor = descriptor || Object.getOwnPropertyDescriptor(targetOrMethod, propertyKeyOrContext);
                    if (actualDescriptor && actualDescriptor.value) {
                      actualDescriptor.value = __qin_wrap_subhuti_rule(actualDescriptor.value, propertyKeyOrContext);
                      return actualDescriptor;
                    }
                    if (targetOrMethod && targetOrMethod[propertyKeyOrContext]) {
                      targetOrMethod[propertyKeyOrContext] = __qin_wrap_subhuti_rule(
                        targetOrMethod[propertyKeyOrContext],
                        propertyKeyOrContext
                      );
                    }
                    return descriptor;
                  }
                  const ruleName = propertyKeyOrContext && propertyKeyOrContext.name
                    ? propertyKeyOrContext.name
                    : targetOrMethod && targetOrMethod.name;
                  return __qin_wrap_subhuti_rule(targetOrMethod, ruleName);
                }
                """, StandardCharsets.UTF_8);
        writeRuntimePackageStamp(shimDir);
    }

    private void materializeQinSlimeGeneratorShim(Path runtimeNodeModules) throws IOException {
        Path shimDir = runtimeNodeModules.resolve("slime-generator").normalize();
        deleteRecursively(shimDir);
        Files.createDirectories(shimDir);
        Files.writeString(shimDir.resolve("package.json"), """
                {
                  "name": "slime-generator",
                  "version": "0.0.0-qin-shim",
                  "type": "module",
                  "exports": {
                    ".": "./index.ts"
                  },
                  "main": "./index.ts",
                  "module": "./index.ts"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(shimDir.resolve("index.ts"), """
                export class SlimeCodeLocation {
                  type = "";
                  line = 0;
                  value = "";
                  column = 0;
                  length = 0;
                  synthetic = false;
                  index = 0;
                }

                export class SlimeCodeMapping {
                  source = null;
                  generate = null;
                }

                function field(node, name) {
                  if (node == null) return undefined;
                  const direct = node[name];
                  if (direct !== undefined && typeof direct !== "function") return direct;
                  if (name === "arguments") {
                    const escapedDirect = node.__qin_arguments;
                    if (escapedDirect !== undefined && typeof escapedDirect !== "function") return escapedDirect;
                    if (node.__qin_field___qin_arguments !== undefined) return node.__qin_field___qin_arguments;
                    if (typeof escapedDirect === "function") return escapedDirect.call(node);
                  }
                  const qinName = "__qin_field_" + name;
                  if (node[qinName] !== undefined) return node[qinName];
                  if (typeof direct === "function") return direct.call(node);
                  const getter = "get" + name.slice(0, 1).toUpperCase() + name.slice(1);
                  if (typeof node[getter] === "function") return node[getter]();
                  return undefined;
                }

                function normalizeNodeType(raw) {
                  const text = String(raw == null ? "" : raw)
                    .replace(/^SlimeAstTypeName\\./, "")
                    .replace(/^AstNodeType\\./, "");
                  if (!/^[A-Z][A-Z0-9_]*$/.test(text)) return text;
                  const lower = text.toLowerCase();
                  let result = "";
                  let upperNext = true;
                  for (let i = 0; i < lower.length; i++) {
                    const ch = lower[i];
                    if (ch === "_") {
                      upperNext = true;
                    } else if (upperNext) {
                      result += ch.toUpperCase();
                      upperNext = false;
                    } else {
                      result += ch;
                    }
                  }
                  return result;
                }

                function nodeType(node) {
                  return normalizeNodeType(field(node, "type"));
                }

                function list(value) {
                  if (value == null) return [];
                  if (Array.isArray(value)) return value;
                  if (typeof value.toArray === "function") return value.toArray();
                  if (typeof value.size === "function" && typeof value.get === "function") {
                    const out = [];
                    for (let i = 0; i < value.size(); i++) out.push(value.get(i));
                    return out;
                  }
                  return [];
                }

                function literalValue(node) {
                  const raw = field(node, "raw");
                  if (raw != null && raw !== "") return String(raw);
                  const value = field(node, "value");
                  if (value == null) return "null";
                  if (typeof value === "string") return JSON.stringify(value);
                  return String(value);
                }

                function idName(node) {
                  const name = field(node, "name");
                  if (name != null) return String(name);
                  const value = field(node, "value");
                  return value == null ? "" : String(value);
                }

                function join(nodes, separator) {
                  const values = [];
                  for (const node of list(nodes)) {
                    const text = generateNode(node);
                    if (text !== "") values.push(text);
                  }
                  return values.join(separator);
                }

                function generateKey(node, computed) {
                  const text = generateNode(node);
                  return computed ? "[" + text + "]" : text;
                }

                function generateParams(params) {
                  const values = [];
                  for (const param of list(params)) {
                    const paramNode = field(param, "param");
                    const argumentNode = field(param, "argument");
                    values.push(generateNode(paramNode !== undefined ? paramNode : argumentNode !== undefined ? argumentNode : param));
                  }
                  return values.join(", ");
                }

                function generateBlock(node) {
                  const body = list(field(node, "body"));
                  const values = [];
                  for (const item of body) {
                    const text = generateNode(item);
                    if (text) values.push(text);
                  }
                  return "{\\n" + values.join("\\n") + "\\n}";
                }

                function joinLocalSpecifiers(specifiers) {
                  const values = [];
                  for (const spec of list(specifiers)) {
                    values.push(generateNode(field(spec, "local")));
                  }
                  return values.join(", ");
                }

                function generateImportDeclaration(node) {
                  const specifiers = list(field(node, "specifiers"));
                  const source = generateNode(field(node, "source"));
                  if (!specifiers.length) return "import " + source + ";";
                  const named = [];
                  let defaultImport = "";
                  let namespaceImport = "";
                  for (const specifier of specifiers) {
                    const type = nodeType(specifier);
                    if (type === "ImportDefaultSpecifier") defaultImport = generateNode(field(specifier, "local"));
                    else if (type === "ImportNamespaceSpecifier") namespaceImport = "* as " + generateNode(field(specifier, "local"));
                    else {
                      const imported = generateNode(field(specifier, "imported"));
                      const local = generateNode(field(specifier, "local"));
                      named.push(imported && local && imported !== local ? imported + " as " + local : (imported || local));
                    }
                  }
                  const parts = [];
                  if (defaultImport) parts.push(defaultImport);
                  if (namespaceImport) parts.push(namespaceImport);
                  if (named.length) parts.push("{ " + named.join(", ") + " }");
                  return "import " + parts.join(", ") + " from " + source + ";";
                }

                function tokenText(value, fallback = "") {
                  if (value == null) return fallback;
                  if (typeof value === "string") return value;
                  const rawValue = field(value, "value");
                  if (rawValue != null) return tokenText(rawValue, fallback);
                  const rawName = field(value, "name");
                  if (rawName != null) return tokenText(rawName, fallback).toLowerCase();
                  return String(value);
                }

                function generateVariableDeclaration(node, forHeader = false) {
                  const kind = tokenText(field(node, "kind"), "const");
                  const code = kind + " " + join(field(node, "declarations"), ", ");
                  return forHeader ? code : code + ";";
                }

                function generateNode(node) {
                  if (node == null) return "";
                  const type = nodeType(node);
                  switch (type) {
                    case "Program":
                      return join(field(node, "body"), "\\n");
                    case "Identifier":
                    case "PrivateIdentifier":
                      return idName(node);
                    case "Literal":
                    case "StringLiteral":
                    case "NumericLiteral":
                    case "BooleanLiteral":
                    case "NullLiteral":
                      return literalValue(node);
                    case "ThisExpression":
                      return "this";
                    case "Super":
                      return "super";
                    case "ImportDeclaration":
                      return generateImportDeclaration(node);
                    case "ExportDefaultDeclaration":
                      return "export default " + generateNode(field(node, "declaration")) + ";";
                    case "ExportNamedDeclaration": {
                      const declaration = field(node, "declaration");
                      if (declaration) return "export " + generateNode(declaration);
                      const specs = joinLocalSpecifiers(field(node, "specifiers"));
                      const source = field(node, "source");
                      return "export { " + specs + " }" + (source ? " from " + generateNode(source) : "") + ";";
                    }
                    case "VariableDeclaration":
                      return generateVariableDeclaration(node);
                    case "VariableDeclarator": {
                      const init = field(node, "init");
                      return generateNode(field(node, "id")) + (init ? " = " + generateNode(init) : "");
                    }
                    case "ExpressionStatement":
                      return generateNode(field(node, "expression")) + ";";
                    case "BlockStatement":
                      return generateBlock(node);
                    case "ReturnStatement": {
                      const argument = field(node, "argument");
                      return "return" + (argument ? " " + generateNode(argument) : "") + ";";
                    }
                    case "CallExpression":
                    case "NewExpression": {
                      const prefix = type === "NewExpression" ? "new " : "";
                      return prefix + generateNode(field(node, "callee")) + "(" + join(field(node, "arguments"), ", ") + ")";
                    }
                    case "MemberExpression": {
                      const object = generateNode(field(node, "object"));
                      const property = generateNode(field(node, "property"));
                      return field(node, "computed") ? object + "[" + property + "]" : object + "." + property;
                    }
                    case "ArrayExpression":
                      return "[" + join(field(node, "elements"), ", ") + "]";
                    case "ArrayElement":
                      return generateNode(field(node, "element") !== undefined ? field(node, "element") : field(node, "argument"));
                    case "ObjectExpression":
                      return "{ " + join(field(node, "properties"), ", ") + " }";
                    case "Property": {
                      const wrappedProperty = field(node, "property");
                      if (wrappedProperty !== undefined) return generateNode(wrappedProperty);
                      const key = generateKey(field(node, "key"), field(node, "computed"));
                      const value = field(node, "value");
                      if (field(node, "method")) return key + "(" + generateParams(field(value, "params")) + ") " + generateNode(field(value, "body"));
                      if (field(node, "shorthand")) return key;
                      return key + ": " + generateNode(value);
                    }
                    case "ArrowFunctionExpression": {
                      const body = field(node, "body");
                      return "(" + generateParams(field(node, "params")) + ") => " + generateNode(body);
                    }
                    case "FunctionDeclaration":
                    case "FunctionExpression": {
                      const id = field(node, "id");
                      return "function" + (id ? " " + generateNode(id) : "") + "(" + generateParams(field(node, "params")) + ") " + generateNode(field(node, "body"));
                    }
                    case "BinaryExpression":
                    case "LogicalExpression":
                    case "AssignmentExpression":
                      return generateNode(field(node, "left")) + " " + String(field(node, "operator") || "") + " " + generateNode(field(node, "right"));
                    case "UnaryExpression":
                      return String(field(node, "operator") || "") + generateNode(field(node, "argument"));
                    case "ParenthesizedExpression":
                      return "(" + generateNode(field(node, "expression")) + ")";
                    case "ConditionalExpression":
                      return generateNode(field(node, "test")) + " ? " + generateNode(field(node, "consequent")) + " : " + generateNode(field(node, "alternate"));
                    case "TemplateLiteral": {
                      const quasis = list(field(node, "quasis"));
                      const expressions = list(field(node, "expressions"));
                      let out = "`";
                      for (let i = 0; i < quasis.length; i++) {
                        const raw = field(quasis[i], "raw");
                        const valueRaw = field(field(quasis[i], "value"), "raw");
                        out += raw != null ? raw : valueRaw != null ? valueRaw : "";
                        if (i < expressions.length) out += "${" + generateNode(expressions[i]) + "}";
                      }
                      return out + "`";
                    }
                    default:
                      if (field(node, "property") !== undefined) return generateNode(field(node, "property"));
                      if (field(node, "element") !== undefined) return generateNode(field(node, "element"));
                      if (field(node, "argument") !== undefined) return generateNode(field(node, "argument"));
                      if (field(node, "param") !== undefined) return generateNode(field(node, "param"));
                      return "";
                  }
                }

                export const SlimeGenerator = {
                  generator(ast, tokens) {
                    return { code: generateNode(ast), mapping: [] };
                  },
                  generate(ast) {
                    return generateNode(ast);
                  }
                };

                export function __qin_smoke_generate() {
                  return SlimeGenerator.generator({
                    type: "Program",
                    body: [{ type: "ExpressionStatement", expression: { type: "Literal", value: "ok" } }]
                  }, []).code;
                }

                export function __qin_smoke_generate_java_style_ast() {
                  const ast = {
                    type() { return "PROGRAM"; },
                    body() {
                      return [{
                        type() { return "EXPRESSION_STATEMENT"; },
                        expression() {
                          return {
                            type() { return "LITERAL"; },
                            value() { return "java-style"; }
                          };
                        }
                      }];
                    }
                  };
                  return SlimeGenerator.generator(ast, []).code;
                }

                export function __qin_smoke_generate_normalized_over_qin_fields() {
                  const ast = {
                    __qin_field_type: "PROGRAM",
                    __qin_field_body: [],
                    type: "Program",
                    body: [{
                      __qin_field_type: "EXPRESSION_STATEMENT",
                      __qin_field_expression: { __qin_field_type: "LITERAL", __qin_field_value: "stale" },
                      type: "ExpressionStatement",
                      expression: { type: "Literal", value: "normalized" }
                    }]
                  };
                  return SlimeGenerator.generator(ast, []).code;
                }

                export function __qin_smoke_generate_escaped_arguments() {
                  const ast = {
                    type() { return "PROGRAM"; },
                    body() {
                      return [{
                        type() { return "EXPRESSION_STATEMENT"; },
                        expression() {
                          return {
                            type() { return "CALL_EXPRESSION"; },
                            callee() { return { type() { return "IDENTIFIER"; }, name() { return "fn"; } }; },
                            __qin_arguments() {
                              return [
                                { type() { return "LITERAL"; }, value() { return "a"; } },
                                { type() { return "LITERAL"; }, value() { return "b"; } }
                              ];
                            }
                          };
                        }
                      }];
                    }
                  };
                  return SlimeGenerator.generator(ast, []).code;
                }

                export function __qin_smoke_generate_wrapped_object_array_items() {
                  const ast = {
                    type: "Program",
                    body: [{
                      type: "ExpressionStatement",
                      expression: {
                        type: "CallExpression",
                        callee: { type: "Identifier", name: "fn" },
                        arguments: [{
                          type: "ObjectExpression",
                          properties: [{
                            property: {
                              type: "Property",
                              key: { type: "Identifier", name: "id" },
                              value: { type: "Literal", value: "balance-panel" }
                            }
                          }]
                        }, {
                          type: "ArrayExpression",
                          elements: [{
                            element: { type: "Literal", value: "Loading balance monitor..." }
                          }]
                        }]
                      }
                    }]
                  };
                  return SlimeGenerator.generator(ast, []).code;
                }

                export function __qin_smoke_generate_token_kind_export() {
                  const ast = {
                    type: "Program",
                    body: [{
                      type: "ExportNamedDeclaration",
                      declaration: {
                        type: "VariableDeclaration",
                        kind: { type: "Const", value: "const" },
                        declarations: [{
                          type: "VariableDeclarator",
                          id: { type: "Identifier", name: "SummaryGrid" },
                          init: {
                            type: "ArrowFunctionExpression",
                            params: [],
                            body: { type: "BlockStatement", body: [] }
                          }
                        }]
                      },
                      specifiers: [],
                      source: null
                    }]
                  };
                  return SlimeGenerator.generator(ast, []).code;
                }

                export function __qin_smoke_generate_parenthesized_conditional() {
                  const ast = {
                    type: "Program",
                    body: [{
                      type: "ExpressionStatement",
                      expression: {
                        type: "ConditionalExpression",
                        test: { type: "Identifier", name: "ready" },
                        consequent: {
                          type: "ParenthesizedExpression",
                          expression: {
                            type: "ConditionalExpression",
                            test: { type: "Identifier", name: "cached" },
                            consequent: { type: "Literal", value: "cache" },
                            alternate: { type: "Literal", value: "live" }
                          }
                        },
                        alternate: { type: "Literal", value: "waiting" }
                      }
                    }]
                  };
                  return SlimeGenerator.generator(ast, []).code;
                }

                export default SlimeGenerator;
                """, StandardCharsets.UTF_8);
        writeRuntimePackageStamp(shimDir);
    }

    private void materializeQinVueCompilerSfcShim(Path runtimeNodeModules) throws IOException {
        Path shimDir = runtimeNodeModules.resolve("@vue").resolve("compiler-sfc").normalize();
        deleteRecursively(shimDir);
        Files.createDirectories(shimDir);
        Files.writeString(shimDir.resolve("package.json"), """
                {
                  "name": "@vue/compiler-sfc",
                  "version": "0.0.0-qin-shim",
                  "type": "module",
                  "exports": {
                    ".": "./index.js"
                  },
                  "main": "./index.js",
                  "module": "./index.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(shimDir.resolve("index.js"), qinVueCompilerSfcShimSource(), StandardCharsets.UTF_8);
        writeRuntimePackageStamp(shimDir);
    }

    private String qinVueCompilerSfcShimSource() {
        return qinVueCompilerSfcHostSource()
                + System.lineSeparator()
                + """
                export const version = __qinVueCompilerSfc.version;
                export const parse = __qinVueCompilerSfc.parse;
                export const compileScript = __qinVueCompilerSfc.compileScript;
                export const compileTemplate = __qinVueCompilerSfc.compileTemplate;
                export const compileStyleAsync = __qinVueCompilerSfc.compileStyleAsync;
                export const rewriteDefault = __qinVueCompilerSfc.rewriteDefault;
                export { __qinVueCompilerSfc as default };
                """;
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
                    ".": "./index.js",
                    "./compiler-sfc": "./compiler-sfc/index.js",
                    "./compiler-sfc/*": "./compiler-sfc/*"
                  },
                  "main": "./index.js",
                  "module": "./index.js"
                }
                """, StandardCharsets.UTF_8);
        Path compilerSfcDir = shimDir.resolve("compiler-sfc").normalize();
        Files.createDirectories(compilerSfcDir);
        Files.writeString(compilerSfcDir.resolve("index.js"), """
                export * from "@vue/compiler-sfc";
                export { default } from "@vue/compiler-sfc";
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
        writeRuntimePackageStamp(shimDir);
    }

    private void rewriteWorkspacePackageManifest(Path targetPackageDir, Path sourcePackageDir, String packageName)
            throws IOException {
        WorkspacePackageEntrypoint entrypoint = inspectWorkspacePackageEntrypoint(sourcePackageDir);
        String sourceEntry = entrypoint.sourceEntry();
        if (sourceEntry == null || sourceEntry.isBlank()) {
            throw new IllegalStateException("Cannot determine workspace source entry for package: " + sourcePackageDir);
        }
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

    private WorkspacePackageEntrypoint inspectWorkspacePackageEntrypoint(Path sourcePackageDir) throws IOException {
        String declaredSourceEntry = readDeclaredWorkspaceSourceEntry(sourcePackageDir);
        String sourceEntry = declaredSourceEntry != null
                ? declaredSourceEntry
                : detectWorkspaceSourceEntry(sourcePackageDir);
        return new WorkspacePackageEntrypoint(
                readResolvableManifestEntry(sourcePackageDir),
                sourceEntry,
                declaredSourceEntry != null);
    }

    private String readResolvableManifestEntry(Path sourcePackageDir) throws IOException {
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

    private String readDeclaredWorkspaceSourceEntry(Path sourcePackageDir) throws IOException {
        Path packageJson = sourcePackageDir.resolve("package.json");
        if (Files.isRegularFile(packageJson)) {
            String json = Files.readString(packageJson, StandardCharsets.UTF_8);
            Matcher localMatcher = JSON_LOCAL_FIELD.matcher(json);
            if (localMatcher.find()) {
                return normalizeManifestRelativePath(localMatcher.group(1));
            }
        }
        return null;
    }

    private String detectWorkspaceSourceEntry(Path sourcePackageDir) {
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
        return null;
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

    private void collectLocalImportBareModuleSpecifiers(
            Path projectRoot,
            Path importerDir,
            String source,
            Set<String> bareSpecifiers,
            Set<Path> visited,
            int depth) {
        if (source == null || source.isBlank() || depth > 2) {
            return;
        }
        boolean[] code = codeMask(source);
        collectLocalImportBareModuleSpecifiers(projectRoot, importerDir, code, FROM_IMPORT_PATTERN.matcher(source),
                bareSpecifiers, visited, depth);
        collectLocalImportBareModuleSpecifiers(projectRoot, importerDir, code, SIDE_EFFECT_IMPORT_PATTERN.matcher(source),
                bareSpecifiers, visited, depth);
    }

    private void collectLocalImportBareModuleSpecifiers(
            Path projectRoot,
            Path importerDir,
            boolean[] code,
            Matcher matcher,
            Set<String> bareSpecifiers,
            Set<Path> visited,
            int depth) {
        while (matcher.find()) {
            if (!isCodePosition(code, matcher.start())) {
                continue;
            }
            String specifier = matcher.group(1);
            Path local = resolveLocalImport(importerDir, specifier);
            if (local == null || !local.startsWith(projectRoot) || !visited.add(local)) {
                continue;
            }
            try {
                String localSource = Files.readString(local, StandardCharsets.UTF_8);
                bareSpecifiers.addAll(extractBareModuleSpecifiers(localSource));
                Path parent = local.getParent();
                if (parent != null) {
                    collectLocalImportBareModuleSpecifiers(projectRoot, parent, localSource, bareSpecifiers, visited, depth + 1);
                }
            } catch (IOException ignored) {
                // A missing optional config import should not block package materialization.
            }
        }
    }

    private Path resolveLocalImport(Path importerDir, String specifier) {
        if (specifier == null
                || specifier.isBlank()
                || (!specifier.startsWith("./") && !specifier.startsWith("../"))) {
            return null;
        }
        Path candidate = importerDir.resolve(specifier).normalize();
        if (Files.isRegularFile(candidate)) {
            return candidate;
        }
        for (String extension : List.of(".js", ".mjs", ".cjs")) {
            Path withExtension = importerDir.resolve(specifier + extension).normalize();
            if (Files.isRegularFile(withExtension)) {
                return withExtension;
            }
        }
        Path index = candidate.resolve("index.js").normalize();
        return Files.isRegularFile(index) ? index : null;
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

    private Path moduleClassCacheRoot(Path projectRoot) {
        Path workspaceRoot = locateWorkspaceRoot();
        if (workspaceRoot != null) {
            return workspaceRoot.resolve("qin").toAbsolutePath().normalize();
        }
        return projectRoot.toAbsolutePath().normalize();
    }

    private Map<String, Path> indexWorkspacePackages(Path workspaceRoot) throws IOException {
        Map<String, Path> packages = new LinkedHashMap<>();
        Files.walkFileTree(workspaceRoot, Set.of(), 6, new java.nio.file.SimpleFileVisitor<>() {
            @Override
            public java.nio.file.FileVisitResult preVisitDirectory(
                    Path dir,
                    java.nio.file.attribute.BasicFileAttributes attrs) {
                if (!dir.equals(workspaceRoot) && isIgnoredDirectoryName(dir.getFileName())) {
                    return java.nio.file.FileVisitResult.SKIP_SUBTREE;
                }
                return java.nio.file.FileVisitResult.CONTINUE;
            }

            @Override
            public java.nio.file.FileVisitResult visitFile(
                    Path file,
                    java.nio.file.attribute.BasicFileAttributes attrs) {
                if (file.getFileName() != null && "package.json".equals(file.getFileName().toString())) {
                    String packageName = readPackageName(file);
                    if (packageName != null && !packageName.isBlank()) {
                        packages.putIfAbsent(packageName, file.getParent().toAbsolutePath().normalize());
                    }
                }
                return java.nio.file.FileVisitResult.CONTINUE;
            }
        });
        return packages;
    }

    private boolean isIgnoredDirectoryName(Path fileName) {
        return fileName != null && IGNORED_COPY_DIRS.contains(fileName.toString());
    }

    private boolean isIgnoredPath(Path path) {
        String normalized = path.toAbsolutePath().normalize().toString().replace('\\', '/');
        for (Path part : path) {
            String name = String.valueOf(part);
            if (IGNORED_COPY_DIRS.contains(name)) {
                return true;
            }
        }
        for (String ignoredDir : IGNORED_COPY_DIRS) {
            if (normalized.contains("/" + ignoredDir + "/")
                    || normalized.endsWith("/" + ignoredDir)) {
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

    private Set<String> scanPackageBareModuleSpecifiers(
            Path packageDir,
            Path sourcePackageDir) throws IOException {
        if (packageDir == null || !Files.isDirectory(packageDir)) {
            return Set.of();
        }
        Set<String> specifiers = new LinkedHashSet<>();
        String entry = readResolvableManifestEntry(sourcePackageDir);
        if (entry != null) {
            Path entryFile = packageDir.resolve(entry).normalize();
            if (Files.isRegularFile(entryFile)) {
                collectPackageSourceBareSpecifiers(specifiers, packageDir, entryFile);
            }
        }
        return specifiers;
    }

    private void collectPackageSourceBareSpecifiers(Set<String> specifiers, Path packageDir, Path sourceFile) {
        try {
            String source = Files.readString(sourceFile, StandardCharsets.UTF_8);
            specifiers.addAll(extractBareModuleSpecifiers(source));
            Path parent = sourceFile.getParent();
            if (parent != null) {
                collectLocalImportBareModuleSpecifiers(
                        packageDir.toAbsolutePath().normalize(),
                        parent,
                        source,
                        specifiers,
                        new LinkedHashSet<>(Set.of(sourceFile.toAbsolutePath().normalize())),
                        0);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to scan package imports: " + sourceFile, e);
        }
    }

    private Path resolveInstalledPackageDir(String packageName, Path workspaceRoot, Path projectRoot, Path runtimeNodeModules) {
        if (runtimeNodeModules != null) {
            Path runtimeCandidate = runtimeNodeModules.resolve(packageName.replace('/', java.io.File.separatorChar));
            if (Files.isDirectory(runtimeCandidate)
                    && Files.isRegularFile(runtimeCandidate.resolve("package.json"))
                    && packageManifestNameMatches(runtimeCandidate, packageName)) {
                return runtimeCandidate.toAbsolutePath().normalize();
            }
        }
        List<Path> searchRoots = List.of(
                projectRoot,
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
        if (!Boolean.getBoolean("qin.scanWorkspaceNodeModules")) {
            return null;
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

    private record WorkspacePackageEntrypoint(
            String manifestEntry,
            String sourceEntry,
            boolean declaredSourceEntry) {
        private boolean hasManifestEntry() {
            return manifestEntry != null && !manifestEntry.isBlank();
        }

        private boolean hasSourceEntry() {
            return sourceEntry != null && !sourceEntry.isBlank();
        }
    }

    private void copyPackageTree(Path sourceDir, Path targetDir, boolean workspacePackage, boolean includeNodeModules) throws IOException {
        Set<String> ignoredDirs = workspacePackage ? IGNORED_COPY_DIRS : IGNORED_INSTALLED_PACKAGE_DIRS;
        if (includeNodeModules && ignoredDirs.contains("node_modules")) {
            ignoredDirs = new LinkedHashSet<>(ignoredDirs);
            ignoredDirs.remove("node_modules");
        }
        Set<String> finalIgnoredDirs = ignoredDirs;
        Files.walkFileTree(sourceDir, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path relative = sourceDir.relativize(dir);
                if (!relative.toString().isEmpty()) {
                    String name = dir.getFileName() == null ? "" : dir.getFileName().toString();
                    if (finalIgnoredDirs.contains(name)) {
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

    private boolean isMaterializedPackageFresh(
            Path sourceDir,
            Path targetDir,
            boolean workspacePackage,
            boolean includeNodeModules) throws IOException {
        Path stampFile = targetDir.resolve(".qin-package-sync.json");
        if (!Files.isRegularFile(stampFile) || !Files.isRegularFile(targetDir.resolve("package.json"))) {
            return false;
        }
        String expected = materializedPackageStamp(sourceDir, workspacePackage, includeNodeModules);
        String actual = Files.readString(stampFile, StandardCharsets.UTF_8).trim();
        return expected.equals(actual);
    }

    private void writeMaterializedPackageStamp(
            Path sourceDir,
            Path targetDir,
            boolean workspacePackage,
            boolean includeNodeModules) throws IOException {
        Files.writeString(
                targetDir.resolve(".qin-package-sync.json"),
                materializedPackageStamp(sourceDir, workspacePackage, includeNodeModules),
                StandardCharsets.UTF_8);
    }

    private void ensureRuntimePackageStamp(Path packageDir) throws IOException {
        if (!Files.isRegularFile(packageDir.resolve(".qin-package-sync.json"))) {
            writeRuntimePackageStamp(packageDir);
        }
    }

    private void writeRuntimePackageStamp(Path packageDir) throws IOException {
        Files.writeString(
                packageDir.resolve(".qin-package-sync.json"),
                runtimePackageStamp(packageDir),
                StandardCharsets.UTF_8);
    }

    private String runtimePackageStamp(Path packageDir) throws IOException {
        PackageTreeFingerprint fingerprint = fingerprintPackageTree(packageDir, false, false);
        return "{"
                + "\"files\":" + fingerprint.files + ","
                + "\"bytes\":" + fingerprint.bytes + ","
                + "\"sha256\":\"" + fingerprint.sha256 + "\""
                + "}";
    }

    private String materializedPackageStamp(
            Path sourceDir,
            boolean workspacePackage,
            boolean includeNodeModules) throws IOException {
        PackageTreeFingerprint fingerprint = fingerprintPackageTree(sourceDir, workspacePackage, includeNodeModules);
        return "{"
                + "\"files\":" + fingerprint.files + ","
                + "\"bytes\":" + fingerprint.bytes + ","
                + "\"sha256\":\"" + fingerprint.sha256 + "\""
                + "}";
    }

    private PackageTreeFingerprint fingerprintPackageTree(
            Path sourceDir,
            boolean workspacePackage,
            boolean includeNodeModules) throws IOException {
        Set<String> ignoredDirs = workspacePackage ? IGNORED_COPY_DIRS : IGNORED_INSTALLED_PACKAGE_DIRS;
        if (includeNodeModules && ignoredDirs.contains("node_modules")) {
            ignoredDirs = new LinkedHashSet<>(ignoredDirs);
            ignoredDirs.remove("node_modules");
        }
        Set<String> finalIgnoredDirs = ignoredDirs;
        PackageTreeFingerprint fingerprint = new PackageTreeFingerprint();
        List<Path> files = new ArrayList<>();
        Files.walkFileTree(sourceDir, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                Path relative = sourceDir.relativize(dir.toAbsolutePath().normalize());
                if (!relative.toString().isEmpty()) {
                    String name = dir.getFileName() == null ? "" : dir.getFileName().toString();
                    if (finalIgnoredDirs.contains(name)) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (".qin-package-sync.json".equals(file.getFileName() == null ? "" : file.getFileName().toString())) {
                    return FileVisitResult.CONTINUE;
                }
                files.add(file.toAbsolutePath().normalize());
                return FileVisitResult.CONTINUE;
            }
        });
        files.sort(Path::compareTo);
        MessageDigest digest = newSha256Digest();
        byte[] buffer = new byte[8192];
        for (Path file : files) {
            BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
            fingerprint.files++;
            fingerprint.bytes += attrs.size();
            fingerprint.modifiedMillis = Math.max(fingerprint.modifiedMillis, attrs.lastModifiedTime().toMillis());

            Path relative = sourceDir.relativize(file.toAbsolutePath().normalize());
            digest.update(relative.toString().replace('\\', '/').getBytes(StandardCharsets.UTF_8));
            digest.update((byte) 0);
            try (InputStream input = Files.newInputStream(file)) {
                int read;
                while ((read = input.read(buffer)) >= 0) {
                    digest.update(buffer, 0, read);
                }
            }
            digest.update((byte) 0);
        }
        fingerprint.sha256 = HexFormat.of().formatHex(digest.digest());
        return fingerprint;
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
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

    private static final class NpmHostLock implements AutoCloseable {
        private final FileChannel channel;
        private final FileLock lock;

        private NpmHostLock(FileChannel channel, FileLock lock) {
            this.channel = channel;
            this.lock = lock;
        }

        @Override
        public void close() throws IOException {
            try {
                if (lock != null && lock.isValid()) {
                    lock.release();
                }
            } finally {
                channel.close();
            }
        }
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

    private static String shortSha256(String text) {
        MessageDigest digest = newSha256Digest();
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash, 0, 8);
    }

    private static MessageDigest newSha256Digest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException error) {
            throw new IllegalStateException("SHA-256 is not available", error);
        }
    }

    private static final class PackageTreeFingerprint {
        private long files;
        private long bytes;
        private long modifiedMillis;
        private String sha256;
    }
}
