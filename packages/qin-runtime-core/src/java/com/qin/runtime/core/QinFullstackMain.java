package com.qin.runtime.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Minimal fullstack entry:
 * 1) compile backend .qin/.js/.ts or .java -> JVM .class
 * 2) compile frontend modules -> app.js
 * 3) serve static files + API on one port
 */
public final class QinFullstackMain {
    private static final String INDEX_HTML = "index.html";
    private static final String INDEX = "index";
    private static final List<String> DEV_WATCH_EXTENSIONS = List.of(
            ".html", ".css", ".cssts", ".js", ".mjs", ".ts", ".qin", ".vue", ".ovs", ".java");
    private static final Pattern JAVA_PACKAGE_PATTERN = Pattern.compile("(?m)^\\s*package\\s+([A-Za-z_$][\\w$]*(?:\\.[A-Za-z_$][\\w$]*)*)\\s*;");
    private static final Pattern JAVA_PUBLIC_TYPE_PATTERN = Pattern.compile("(?m)^\\s*public\\s+(?:final\\s+|abstract\\s+|sealed\\s+|non-sealed\\s+)*"
            + "(?:class|interface|record|enum)\\s+([A-Za-z_$][\\w$]*)\\b");
    private static final Pattern IMPORT_LINE_PATTERN = Pattern.compile("(?m)^\\s*import\\s+[^\\n;]+(?:;)?\\s*$");
    private static final Pattern APP_OBJECT_START_PATTERN = Pattern.compile(
            "(?s)@WebRoot\\s*\\([^)]*\\)\\s*export\\s+object\\s+App\\s*\\{");
    private static final Pattern IMPORT_SPECIFIER_PATTERN = Pattern.compile("([\"'])([^\"']+)([\"'])");
    private static final List<String> DEV_WATCH_IGNORED_DIRS = List.of(
            ".git", ".qin", "@qin-mod", "build", "dist", "target", "node_modules", "out");
    private static final String JAVAC_CACHE_VERSION = "qin-javac-cache-v1";
    private static final String QIN_BACKEND_MODULE_CACHE_VERSION = "qin-backend-module-cache-v1";

    private QinFullstackMain() {
    }

    public static void main(String[] args) throws Exception {
        Options options = parseArgs(args);
        if (options.showHelp) {
            printHelp();
            return;
        }
        if (options.profile) {
            System.setProperty("qin.profile", "true");
        }

        BuildArtifacts artifacts = build(options);
        if (options.buildOnly) {
            System.out.println("Build only mode finished.");
            return;
        }

        if (options.dev) {
            startDevRebuildLoop(artifacts, options);
        }
        QinDevServer.serve(artifacts, options.dev, options.port);
    }

    private static BuildArtifacts build(Options options) throws Exception {
        QinPhaseTimer profile = QinPhaseTimer.start("fullstack-build");
        Path root = resolveRoot(options.rootDir);
        QinRuntimeProjectLayout layout = QinRuntimeProjectLayout.discover(root);
        QinBuildCoordinator coordinator = new QinBuildCoordinator();
        profile.checkpoint("resolve project layout", "root=" + root);

        materializeProjectNpmDependencies(root);
        profile.checkpoint("materialize npm dependencies");
        Path backendSource = resolveBackendSource(layout, options.backendSourceFile);
        Path frontendSource = resolveFrontendSource(layout, options.frontendSourceFile);
        Path classOutputDir = resolvePath(root, options.classOutputDir);
        Path staticRoot = resolveStaticRoot(layout, root, options.staticDir);
        Path jsOutputFile = staticRoot.resolve("app.js").normalize();
        profile.checkpoint("resolve sources", "backend=" + backendSource.getFileName()
                + ", frontend=" + (frontendSource == null ? "<none>" : frontendSource.getFileName()));

        BackendBuild backendBuild = buildBackend(
                coordinator,
                root,
                backendSource,
                classOutputDir,
                jsOutputFile,
                options);
        profile.checkpoint("build backend", "class=" + backendBuild.classFile().getFileName());
        Method runMethod = backendBuild.runMethod();
        QinFrontendEsmService frontendEsmService = null;

        if (frontendSource != null) {
            frontendEsmService = QinFrontendEsmService.create(root, frontendSource);
            profile.checkpoint("create frontend service");
            if (!options.dev || options.buildOnly) {
                frontendEsmService.emitProduction(staticRoot);
                profile.checkpoint("emit frontend production");
            }
        } else {
            Files.createDirectories(staticRoot);
            if (!Files.exists(jsOutputFile)) {
                Files.writeString(jsOutputFile, "console.log('Qin frontend source not found.');", StandardCharsets.UTF_8);
            }
            profile.checkpoint("write frontend placeholder");
        }

        ensureIndexFile(staticRoot);
        profile.checkpoint("ensure index");

        System.out.println("Project root: " + root.toAbsolutePath());
        System.out.println("Backend source: " + backendSource.toAbsolutePath());
        if (frontendSource != null) {
            System.out.println("Frontend source: " + frontendSource.toAbsolutePath());
        } else {
            System.out.println("Frontend source: <none>");
        }
        System.out.println("Generated server class: " + backendBuild.classFile().toAbsolutePath());
        if (frontendSource != null) {
            if (!options.dev || options.buildOnly) {
                System.out.println("Generated frontend js: " + jsOutputFile.toAbsolutePath());
            } else {
                System.out.println("Frontend js mode: in-memory dev transform (no disk emit)");
            }
        } else {
            System.out.println("Generated frontend js: " + jsOutputFile.toAbsolutePath());
        }
        System.out.println("Static root: " + staticRoot.toAbsolutePath());
        profile.done("mode=" + (options.dev ? "dev" : "build")
                + ", buildOnly=" + options.buildOnly);

        return new BuildArtifacts(root, staticRoot, runMethod, backendBuild.httpAppMethod(), frontendEsmService);
    }

    private static BackendMethods loadBackendMethods(Path classOutputDir, String className) throws Exception {
        Files.createDirectories(classOutputDir);
        URL[] urls = { classOutputDir.toUri().toURL() };
        URLClassLoader classLoader = newProjectClassFirstLoader(classOutputDir, urls);
        Class<?> serverClass = Class.forName(className, true, classLoader);
        Method runMethod = serverClass.getMethod("run");
        if (!Modifier.isStatic(runMethod.getModifiers()) || runMethod.getParameterCount() != 0) {
            throw new IllegalStateException("Generated run method must be `public static Object run()`");
        }
        Method httpAppMethod = null;
        try {
            Method candidate = serverClass.getMethod("app");
            if (!Modifier.isStatic(candidate.getModifiers()) || candidate.getParameterCount() != 0) {
                throw new IllegalStateException("Backend app method must be `public static QinHttpApp app()`");
            }
            if (!QinHttpApp.class.isAssignableFrom(candidate.getReturnType())) {
                throw new IllegalStateException("Backend app method must return QinHttpApp");
            }
            httpAppMethod = candidate;
        } catch (NoSuchMethodException ignored) {
            httpAppMethod = null;
        }
        return new BackendMethods(runMethod, httpAppMethod);
    }

    private static URLClassLoader newProjectClassFirstLoader(Path classOutputDir, URL[] urls) {
        ClassLoader parent = QinFullstackMain.class.getClassLoader();
        return new URLClassLoader(urls, parent) {
            @Override
            protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                synchronized (getClassLoadingLock(name)) {
                    Class<?> loaded = findLoadedClass(name);
                    if (loaded == null && isProjectClassFilePresent(classOutputDir, name)) {
                        try {
                            loaded = findClass(name);
                        } catch (ClassNotFoundException ignored) {
                            loaded = null;
                        }
                    }
                    if (loaded == null) {
                        loaded = super.loadClass(name, false);
                    }
                    if (resolve) {
                        resolveClass(loaded);
                    }
                    return loaded;
                }
            }
        };
    }

    private static boolean isProjectClassFilePresent(Path classOutputDir, String binaryName) {
        return Files.isRegularFile(classOutputDir.resolve(binaryName.replace('.', '/') + ".class"));
    }

    private static void materializeProjectNpmDependencies(Path root) throws IOException {
        new QinNpmDependencyMaterializer().materializeProjectDependencies(root, root.resolve("node_modules"));
    }

    private static BackendBuild buildBackend(
            QinBuildCoordinator coordinator,
            Path root,
            Path backendSource,
            Path classOutputDir,
            Path jsOutputFile,
            Options options) throws Exception {
        if (isJavaSource(backendSource)) {
            return compileJavaBackend(root, backendSource, classOutputDir);
        }

        backendSource = prepareUnifiedAppBackendSource(root, backendSource);
        compileProjectJavaHelpers(root, classOutputDir);
        String backendCacheKey = qinBackendModuleCacheKey(root, backendSource, classOutputDir, options.className);
        Path backendClassFile = tryUseQinBackendModuleCache(
                classOutputDir,
                backendCacheKey,
                options.className);
        if (backendClassFile == null) {
            QinBuildRequest backendRequest = new QinBuildRequest(
                    root,
                    backendSource,
                    QinBuildTarget.JVM,
                    options.className,
                    classOutputDir,
                    jsOutputFile,
                    options.printIr);
            QinBuildResult backendResult = withProjectClassLoader(
                    classOutputDir,
                    () -> coordinator.build(backendRequest));
            backendClassFile = backendResult.classFile();
            writeQinBackendModuleCache(classOutputDir, backendCacheKey);
        }
        Path adapterSource = writeQinBackendAdapterSource(
                root,
                classOutputDir,
                options.className + "FullstackAdapter",
                options.className);
        BackendBuild adapterBuild = compileJavaBackend(root, adapterSource, classOutputDir);
        return new BackendBuild(adapterBuild.classFile(), adapterBuild.runMethod(), adapterBuild.httpAppMethod());
    }

    private static Path tryUseQinBackendModuleCache(
            Path classOutputDir,
            String cacheKey,
            String backendClassName) throws IOException {
        Path stampFile = qinBackendModuleCacheStampFile(classOutputDir, cacheKey);
        if (!Files.isRegularFile(stampFile)) {
            return null;
        }
        Properties stamp = new Properties();
        try (var reader = Files.newBufferedReader(stampFile, StandardCharsets.UTF_8)) {
            stamp.load(reader);
        }
        if (!cacheKey.equals(stamp.getProperty("cacheKey"))) {
            return null;
        }
        int outputCount = Integer.parseInt(stamp.getProperty("outputCount", "0"));
        if (outputCount <= 0) {
            return null;
        }
        for (int i = 0; i < outputCount; i++) {
            String relative = stamp.getProperty("output." + i);
            if (relative == null || !Files.isRegularFile(classOutputDir.resolve(relative).normalize())) {
                return null;
            }
        }
        Path backendClassFile = classOutputDir.resolve(backendClassName.replace('.', '/') + ".class").normalize();
        if (!Files.isRegularFile(backendClassFile)) {
            return null;
        }
        System.out.println("[QinFullstackMain] qin backend module cache hit :: " + backendClassName);
        return backendClassFile;
    }

    private static void writeQinBackendModuleCache(Path classOutputDir, String cacheKey) throws IOException {
        List<Path> outputs = collectClassFiles(classOutputDir);
        if (outputs.isEmpty()) {
            return;
        }
        Properties stamp = new Properties();
        stamp.setProperty("version", QIN_BACKEND_MODULE_CACHE_VERSION);
        stamp.setProperty("cacheKey", cacheKey);
        stamp.setProperty("outputCount", Integer.toString(outputs.size()));
        for (int i = 0; i < outputs.size(); i++) {
            stamp.setProperty("output." + i, classOutputDir.relativize(outputs.get(i)).toString().replace('\\', '/'));
        }
        Path stampFile = qinBackendModuleCacheStampFile(classOutputDir, cacheKey);
        Files.createDirectories(stampFile.getParent());
        try (var writer = Files.newBufferedWriter(stampFile, StandardCharsets.UTF_8)) {
            stamp.store(writer, "Qin backend module cache stamp");
        }
    }

    private static Path qinBackendModuleCacheStampFile(Path classOutputDir, String cacheKey) {
        String fileName = cacheKey.length() > 32 ? cacheKey.substring(0, 32) : cacheKey;
        return classOutputDir.resolve(".qin/qin-backend-module-cache").resolve(fileName + ".properties").normalize();
    }

    private static String qinBackendModuleCacheKey(
            Path root,
            Path backendSource,
            Path classOutputDir,
            String backendClassName) throws Exception {
        Path normalizedRoot = root.toAbsolutePath().normalize();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        updateDigest(digest, QIN_BACKEND_MODULE_CACHE_VERSION);
        updateDigest(digest, System.getProperty("java.version", ""));
        updateDigest(digest, normalizedRoot.toString());
        updateDigest(digest, classOutputDir.toAbsolutePath().normalize().toString());
        updateDigest(digest, backendClassName);
        updateDigest(digest, backendCompilerClasspath(classOutputDir));
        updateClassResourceDigest(digest, QinFullstackMain.class);
        updateClassResourceDigest(digest, QinBuildCoordinator.class);
        updateClassResourceDigest(digest, com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler.class);
        for (Path input : qinBackendModuleCacheInputs(normalizedRoot, backendSource)) {
            Path normalized = input.toAbsolutePath().normalize();
            String identity = normalized.startsWith(normalizedRoot)
                    ? normalizedRoot.relativize(normalized).toString().replace('\\', '/')
                    : normalized.toString();
            byte[] bytes = Files.readAllBytes(normalized);
            updateDigest(digest, identity);
            updateDigest(digest, Integer.toString(bytes.length));
            digest.update(bytes);
            digest.update((byte) 0);
        }
        return HexFormat.of().formatHex(digest.digest());
    }

    private static String backendCompilerClasspath(Path classOutputDir) {
        String classpath = System.getProperty("java.class.path", "");
        if (classpath == null || classpath.isBlank()) {
            return classOutputDir.toString();
        }
        return classOutputDir + java.io.File.pathSeparator + classpath;
    }

    private static List<Path> qinBackendModuleCacheInputs(Path root, Path backendSource) throws IOException {
        List<Path> inputs = new ArrayList<>();
        addBackendModuleCacheInput(inputs, root.resolve("qin.config.js"));
        addBackendModuleCacheInput(inputs, backendSource);
        collectBackendModuleCacheDir(inputs, root.resolve("src/main"));
        collectBackendModuleCacheDir(inputs, root.resolve("src/shared"));
        collectBackendModuleCacheDir(inputs, root.resolve("main"));
        collectBackendModuleCacheDir(inputs, root.resolve("shared"));
        return inputs.stream()
                .map(path -> path.toAbsolutePath().normalize())
                .distinct()
                .sorted()
                .toList();
    }

    private static void collectBackendModuleCacheDir(List<Path> inputs, Path dir) throws IOException {
        if (!Files.isDirectory(dir)) {
            return;
        }
        try (var stream = Files.walk(dir)) {
            stream.filter(Files::isRegularFile)
                    .filter(QinFullstackMain::isBackendModuleCacheInputFile)
                    .forEach(path -> addBackendModuleCacheInput(inputs, path));
        }
    }

    private static void addBackendModuleCacheInput(List<Path> inputs, Path path) {
        if (path != null && Files.isRegularFile(path) && isBackendModuleCacheInputFile(path)) {
            inputs.add(path.toAbsolutePath().normalize());
        }
    }

    private static boolean isBackendModuleCacheInputFile(Path path) {
        String name = path == null || path.getFileName() == null
                ? ""
                : path.getFileName().toString().toLowerCase();
        return name.equals("qin.config.js")
                || name.endsWith(".qin")
                || name.endsWith(".java")
                || name.endsWith(".js")
                || name.endsWith(".mjs")
                || name.endsWith(".ts");
    }

    private static Path prepareUnifiedAppBackendSource(Path root, Path backendSource) throws IOException {
        Path normalized = backendSource.toAbsolutePath().normalize();
        Path unifiedEntry = root.resolve("src/app.qin").toAbsolutePath().normalize();
        if (!normalized.equals(unifiedEntry)) {
            return backendSource;
        }
        String source = Files.readString(normalized, StandardCharsets.UTF_8);
        Matcher matcher = APP_OBJECT_START_PATTERN.matcher(source);
        if (!matcher.find()) {
            return backendSource;
        }
        int objectStart = matcher.start();
        int bodyOpen = matcher.end() - 1;
        int objectEnd = findMatchingBrace(source, bodyOpen);
        if (objectEnd < 0) {
            throw new IllegalArgumentException("Unclosed Qin App object in src/app.qin");
        }
        String appObjectSource = source.substring(objectStart, objectEnd + 1);
        StringBuilder backendSourceText = new StringBuilder();
        Matcher importMatcher = IMPORT_LINE_PATTERN.matcher(source);
        while (importMatcher.find()) {
            String line = importMatcher.group();
            String backendImport = rewriteBackendUnifiedImport(root, normalized, line);
            if (backendImport != null) {
                backendSourceText.append(backendImport).append(System.lineSeparator());
            }
        }
        backendSourceText.append(System.lineSeparator()).append(appObjectSource).append(System.lineSeparator());
        Path generated = root.resolve("build/fullstack/generated-backend/app.qin").toAbsolutePath().normalize();
        Files.createDirectories(generated.getParent());
        Files.writeString(generated, backendSourceText.toString(), StandardCharsets.UTF_8);
        return generated;
    }

    private static String rewriteBackendUnifiedImport(Path root, Path originalFile, String importLine) {
        Matcher specifierMatcher = IMPORT_SPECIFIER_PATTERN.matcher(importLine);
        if (!specifierMatcher.find()) {
            return importLine;
        }
        String specifier = specifierMatcher.group(2);
        if ("qin".equals(specifier) || specifier.startsWith("java:")) {
            return importLine;
        }
        if (!specifier.startsWith("./") && !specifier.startsWith("../")) {
            return null;
        }
        Path target = resolveExistingScriptImport(originalFile.getParent(), specifier);
        if (target == null || !isBackendUnifiedImportTarget(root, target)) {
            return null;
        }
        Path generatedParent = root.resolve("build/fullstack/generated-backend").toAbsolutePath().normalize();
        String rewritten = generatedParent.relativize(target.toAbsolutePath().normalize()).toString().replace('\\', '/');
        if (!rewritten.startsWith(".")) {
            rewritten = "./" + rewritten;
        }
        return specifierMatcher.replaceFirst(Matcher.quoteReplacement(specifierMatcher.group(1) + rewritten + specifierMatcher.group(3)));
    }

    private static Path resolveExistingScriptImport(Path importerDir, String specifier) {
        Path target = importerDir.resolve(specifier).toAbsolutePath().normalize();
        if (Files.isRegularFile(target)) {
            return target;
        }
        for (String extension : List.of(".qin", ".ts", ".js", ".mjs")) {
            Path candidate = importerDir.resolve(specifier + extension).toAbsolutePath().normalize();
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    private static boolean isBackendUnifiedImportTarget(Path root, Path target) {
        Path normalized = target.toAbsolutePath().normalize();
        return normalized.startsWith(root.resolve("src/main").toAbsolutePath().normalize())
                || normalized.startsWith(root.resolve("main").toAbsolutePath().normalize());
    }

    private static int findMatchingBrace(String source, int openBrace) {
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

    private static <T> T withProjectClassLoader(Path classOutputDir, ThrowingSupplier<T> supplier) throws Exception {
        Files.createDirectories(classOutputDir);
        Thread thread = Thread.currentThread();
        ClassLoader previous = thread.getContextClassLoader();
        try (URLClassLoader classLoader = newProjectClassFirstLoader(
                classOutputDir,
                new URL[] { classOutputDir.toUri().toURL() })) {
            thread.setContextClassLoader(classLoader);
            return supplier.get();
        } finally {
            thread.setContextClassLoader(previous);
        }
    }

    private static void compileProjectJavaHelpers(Path root, Path classOutputDir) throws Exception {
        List<Path> mainDirs = List.of(root.resolve("src/main").normalize(), root.resolve("main").normalize());
        List<Path> sources;
        List<Path> collected = new ArrayList<>();
        for (Path mainDir : mainDirs) {
            if (!Files.isDirectory(mainDir)) {
                continue;
            }
            try (var stream = Files.walk(mainDir)) {
                stream.filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().endsWith(".java"))
                        .forEach(collected::add);
            }
        }
        sources = collected.stream().distinct().sorted().toList();
        if (sources.isEmpty()) {
            return;
        }
        compileJavaSources(root, sources, classOutputDir, "project Java helper sources");
    }

    private static BackendBuild compileJavaBackend(Path root, Path sourceFile, Path classOutputDir) throws Exception {
        List<Path> sources = collectJavaBackendSources(root, sourceFile);
        compileJavaSources(root, sources, classOutputDir, "backend Java sources");
        String className = inferJavaBinaryClassName(sourceFile);
        Path classFile = classOutputDir.resolve(className.replace('.', '/') + ".class").normalize();
        BackendMethods methods = loadBackendMethods(classOutputDir, className);
        return new BackendBuild(classFile, methods.runMethod(), methods.httpAppMethod());
    }

    private static List<Path> collectJavaBackendSources(Path root, Path sourceFile) throws IOException {
        Path normalizedSource = sourceFile.toAbsolutePath().normalize();
        Path srcMainDir = root.resolve("src/main").toAbsolutePath().normalize();
        Path conventionalMainDir = root.resolve("main").toAbsolutePath().normalize();
        Path sourceRoot;
        if (normalizedSource.startsWith(srcMainDir)) {
            sourceRoot = srcMainDir;
        } else if (normalizedSource.startsWith(conventionalMainDir)) {
            sourceRoot = conventionalMainDir;
        } else {
            sourceRoot = normalizedSource.getParent();
        }
        if (sourceRoot == null || !Files.isDirectory(sourceRoot)) {
            return List.of(sourceFile);
        }
        try (var stream = Files.walk(sourceRoot)) {
            List<Path> sources = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".java"))
                    .map(path -> path.toAbsolutePath().normalize())
                    .sorted()
                    .toList();
            return sources.isEmpty() ? List.of(sourceFile) : sources;
        }
    }

    private static void compileJavaSources(
            Path root,
            List<Path> sourceFiles,
            Path classOutputDir,
            String description) throws Exception {
        QinPhaseTimer profile = QinPhaseTimer.start("javac");
        List<Path> normalizedSources = sourceFiles.stream()
                .map(path -> path.toAbsolutePath().normalize())
                .sorted()
                .toList();
        Files.createDirectories(classOutputDir);
        String classpath = System.getProperty("java.class.path", "");
        if (classpath == null || classpath.isBlank()) {
            classpath = classOutputDir.toString();
        } else {
            classpath = classOutputDir + java.io.File.pathSeparator + classpath;
        }
        String cacheKey = javacCacheKey(root, normalizedSources, classOutputDir, description, classpath);
        if (tryUseJavacCache(classOutputDir, cacheKey, description, normalizedSources.size())) {
            profile.done("cache hit, " + description + ", sources=" + normalizedSources.size());
            return;
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("JDK compiler is required for " + description + ".");
        }

        profile.checkpoint("prepare", description + ", sources=" + sourceFiles.size());
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            Iterable<? extends JavaFileObject> units = fileManager.getJavaFileObjectsFromPaths(normalizedSources);
            List<String> javacOptions = new ArrayList<>();
            javacOptions.add("-encoding");
            javacOptions.add("UTF-8");
            javacOptions.add("-d");
            javacOptions.add(classOutputDir.toString());
            javacOptions.add("-sourcepath");
            javacOptions.add(root.toString());
            javacOptions.add("-classpath");
            javacOptions.add(classpath);

            Boolean ok = compiler.getTask(null, fileManager, diagnostics, javacOptions, null, units).call();
            if (!Boolean.TRUE.equals(ok)) {
                StringBuilder message = new StringBuilder("Failed to compile ")
                        .append(description)
                        .append(": ")
                        .append(sourceFiles);
                diagnostics.getDiagnostics().forEach(diagnostic -> message
                        .append(System.lineSeparator())
                        .append(diagnostic.getKind())
                        .append(" line ")
                        .append(diagnostic.getLineNumber())
                        .append(": ")
                        .append(diagnostic.getMessage(null)));
                throw new IllegalStateException(message.toString());
            }
        }
        writeJavacCache(classOutputDir, cacheKey);
        profile.done(description + ", sources=" + sourceFiles.size());
    }

    private static boolean tryUseJavacCache(
            Path classOutputDir,
            String cacheKey,
            String description,
            int sourceCount) throws IOException {
        Path stampFile = javacCacheStampFile(classOutputDir, cacheKey);
        if (!Files.isRegularFile(stampFile)) {
            return false;
        }
        Properties stamp = new Properties();
        try (var reader = Files.newBufferedReader(stampFile, StandardCharsets.UTF_8)) {
            stamp.load(reader);
        }
        if (!cacheKey.equals(stamp.getProperty("cacheKey"))) {
            return false;
        }
        int outputCount = Integer.parseInt(stamp.getProperty("outputCount", "0"));
        if (outputCount <= 0) {
            return false;
        }
        for (int i = 0; i < outputCount; i++) {
            String relative = stamp.getProperty("output." + i);
            if (relative == null || !Files.isRegularFile(classOutputDir.resolve(relative).normalize())) {
                return false;
            }
        }
        System.out.println("[QinFullstackMain] javac cache hit :: " + description
                + ", sources=" + sourceCount);
        return true;
    }

    private static void writeJavacCache(Path classOutputDir, String cacheKey) throws IOException {
        List<Path> outputs = collectClassFiles(classOutputDir);
        if (outputs.isEmpty()) {
            return;
        }
        Properties stamp = new Properties();
        stamp.setProperty("version", JAVAC_CACHE_VERSION);
        stamp.setProperty("cacheKey", cacheKey);
        stamp.setProperty("outputCount", Integer.toString(outputs.size()));
        for (int i = 0; i < outputs.size(); i++) {
            stamp.setProperty("output." + i, classOutputDir.relativize(outputs.get(i)).toString().replace('\\', '/'));
        }
        Path stampFile = javacCacheStampFile(classOutputDir, cacheKey);
        Files.createDirectories(stampFile.getParent());
        try (var writer = Files.newBufferedWriter(stampFile, StandardCharsets.UTF_8)) {
            stamp.store(writer, "Qin javac cache stamp");
        }
    }

    private static Path javacCacheStampFile(Path classOutputDir, String cacheKey) {
        String fileName = cacheKey.length() > 32 ? cacheKey.substring(0, 32) : cacheKey;
        return classOutputDir.resolve(".qin/javac-cache").resolve(fileName + ".properties").normalize();
    }

    private static String javacCacheKey(
            Path root,
            List<Path> sourceFiles,
            Path classOutputDir,
            String description,
            String classpath) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        updateDigest(digest, JAVAC_CACHE_VERSION);
        updateDigest(digest, System.getProperty("java.version", ""));
        updateDigest(digest, root.toAbsolutePath().normalize().toString());
        updateDigest(digest, classOutputDir.toAbsolutePath().normalize().toString());
        updateDigest(digest, description);
        updateDigest(digest, classpath);
        for (Path sourceFile : sourceFiles) {
            Path normalized = sourceFile.toAbsolutePath().normalize();
            String identity = normalized.startsWith(root.toAbsolutePath().normalize())
                    ? root.toAbsolutePath().normalize().relativize(normalized).toString().replace('\\', '/')
                    : normalized.toString();
            byte[] bytes = Files.readAllBytes(normalized);
            updateDigest(digest, identity);
            updateDigest(digest, Integer.toString(bytes.length));
            digest.update(bytes);
            digest.update((byte) 0);
        }
        return HexFormat.of().formatHex(digest.digest());
    }

    private static void updateDigest(MessageDigest digest, String value) {
        digest.update(value.getBytes(StandardCharsets.UTF_8));
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

    private static List<Path> collectClassFiles(Path classOutputDir) throws IOException {
        if (!Files.isDirectory(classOutputDir)) {
            return List.of();
        }
        try (var stream = Files.walk(classOutputDir)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".class"))
                    .map(path -> path.toAbsolutePath().normalize())
                    .sorted()
                    .toList();
        }
    }

    private static Path writeQinBackendAdapterSource(
            Path root,
            Path classOutputDir,
            String adapterClassName,
            String qinModuleClassName) throws IOException {
        int lastDot = adapterClassName.lastIndexOf('.');
        String packageName = lastDot < 0 ? "" : adapterClassName.substring(0, lastDot);
        String simpleName = lastDot < 0 ? adapterClassName : adapterClassName.substring(lastDot + 1);
        String source = """
                %s
                public final class %s {
                    private static boolean initialized;
                    private static Object runResult;

                    private %s() {}

                    public static Object run() throws Exception {
                        return ensureInitialized();
                    }

                    public static com.qin.runtime.core.QinHttpApp app() throws Exception {
                        ensureInitialized();
                        Object app = null;
                        try {
                            app = com.qin.lang.runtime.JavaEsmGlobal.__qin_module_ref_get__("app");
                        } catch (IllegalStateException ignored) {
                            app = null;
                        }
                        if (app != null && !(app instanceof com.qin.runtime.core.QinHttpApp)) {
                            app = com.qin.lang.runtime.JavaEsmGlobal.__qin_call__(app);
                        }
                        if (app instanceof com.qin.runtime.core.QinHttpApp qinHttpApp) {
                            return qinHttpApp;
                        }
                        Object appObject = com.qin.lang.runtime.JavaEsmGlobal.__qin_module_ref_get__("App");
                        return new com.qin.web.QinWebApplicationAssembler().assemble(appObject);
                    }

                    private static synchronized Object ensureInitialized() throws Exception {
                        if (!initialized) {
                            runResult = %s.run();
                            initialized = true;
                        }
                        return runResult;
                    }
                }
                """.formatted(
                packageName.isBlank() ? "" : "package " + packageName + ";" + System.lineSeparator(),
                simpleName,
                simpleName,
                qinModuleClassName);
        Path sourceFile = classOutputDir
                .resolve("__qin_fullstack_adapter_sources")
                .resolve(adapterClassName.replace('.', '/') + ".java")
                .normalize();
        Files.createDirectories(sourceFile.getParent());
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        return sourceFile;
    }

    private static boolean isJavaSource(Path sourceFile) {
        return sourceFile != null && sourceFile.getFileName().toString().toLowerCase().endsWith(".java");
    }

    private static String inferJavaBinaryClassName(Path sourceFile) throws IOException {
        String source = Files.readString(sourceFile, StandardCharsets.UTF_8);
        Matcher matcher = JAVA_PACKAGE_PATTERN.matcher(source);
        String packageName = matcher.find() ? matcher.group(1) : null;
        String fileName = sourceFile.getFileName().toString();
        Matcher typeMatcher = JAVA_PUBLIC_TYPE_PATTERN.matcher(source);
        String simpleName = typeMatcher.find()
                ? typeMatcher.group(1)
                : fileName.endsWith(".java") ? fileName.substring(0, fileName.length() - ".java".length()) : fileName;
        return packageName == null || packageName.isBlank() ? simpleName : packageName + "." + simpleName;
    }

    private static Path resolveBackendSource(QinRuntimeProjectLayout layout, Path fromArgs) throws IOException {
        if (fromArgs != null) {
            Path resolved = resolvePath(layout.root(), fromArgs);
            requireFile(resolved, "--backend-file");
            return resolved;
        }

        List<Path> candidates = List.of(
                layout.root().resolve("src/app.qin"),
                layout.root().resolve("main/main.qin"),
                layout.root().resolve("main/Main.qin"),
                layout.root().resolve("main/main.js"),
                layout.root().resolve("main/Main.js"),
                layout.root().resolve("main/main.mjs"),
                layout.root().resolve("main/Main.mjs"),
                layout.root().resolve("main/main.ts"),
                layout.root().resolve("main/Main.ts"),
                layout.root().resolve("main/main.java"),
                layout.root().resolve("main/Main.java"),
                layout.root().resolve("shared/main.qin"),
                layout.root().resolve("shared/main.js"),
                layout.root().resolve("shared/main.mjs"),
                layout.root().resolve("shared/main.ts"),
                layout.root().resolve("shared/shared.qin"),
                layout.root().resolve("shared/shared.js"),
                layout.root().resolve("shared/shared.mjs"),
                layout.root().resolve("shared/shared.ts"),
                layout.root().resolve("app/main.qin"),
                layout.root().resolve("app/main.js"),
                layout.root().resolve("src/Main.java"));
        for (Path candidate : candidates) {
            if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
                return candidate.toAbsolutePath().normalize();
            }
        }

        return writeGeneratedFrontendOnlyBackend(layout.root());
    }

    private static Path writeGeneratedFrontendOnlyBackend(Path root) throws IOException {
        Path generated = root.resolve("build/fullstack/generated-frontend-only-backend.qin").normalize();
        Files.createDirectories(generated.getParent());
        Files.writeString(generated, """
                const result = {
                  message: "hello from Qin generated frontend-only backend",
                  source: "build/fullstack/generated-frontend-only-backend.qin"
                }
                """, StandardCharsets.UTF_8);
        return generated.toAbsolutePath().normalize();
    }

    private static Path resolveFrontendSource(QinRuntimeProjectLayout layout, Path fromArgs) {
        if (fromArgs != null) {
            Path resolved = resolvePath(layout.root(), fromArgs);
            requireFile(resolved, "--frontend-file");
            return resolved;
        }

        List<Path> candidates = List.of(
                layout.root().resolve("src/app.qin"),
                layout.root().resolve("app/main.vue"),
                layout.root().resolve("app/Main.vue"),
                layout.root().resolve("app/main.ovs"),
                layout.root().resolve("app/Main.ovs"),
                layout.root().resolve("app/main.qin"),
                layout.root().resolve("app/Main.qin"),
                layout.root().resolve("app/main.js"),
                layout.root().resolve("app/Main.js"),
                layout.root().resolve("app/main.mjs"),
                layout.root().resolve("app/Main.mjs"),
                layout.root().resolve("app/main.ts"),
                layout.root().resolve("app/Main.ts"),
                layout.root().resolve("shared/main.qin"),
                layout.root().resolve("shared/shared.qin"),
                layout.root().resolve("shared/main.js"),
                layout.root().resolve("shared/shared.js"),
                layout.root().resolve("shared/main.mjs"),
                layout.root().resolve("shared/shared.mjs"),
                layout.root().resolve("shared/main.ts"),
                layout.root().resolve("shared/shared.ts"),
                layout.root().resolve("shared/main.vue"),
                layout.root().resolve("shared/shared.vue"),
                layout.root().resolve("shared/main.ovs"),
                layout.root().resolve("shared/shared.ovs"));
        for (Path candidate : candidates) {
            if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
                return candidate.toAbsolutePath().normalize();
            }
        }

        Path appDir = layout.appDir();
        if (Files.isDirectory(appDir)) {
            try (var stream = Files.walk(appDir)) {
                return stream
                        .filter(Files::isRegularFile)
                        .filter(path -> !path.toString().contains("\\node_modules\\"))
                        .filter(path -> !path.toString().contains("/node_modules/"))
                        .filter(path -> hasFrontendModuleExtension(path.getFileName().toString()))
                        .sorted(Comparator.comparing(Path::toString))
                        .map(path -> path.toAbsolutePath().normalize())
                        .findFirst()
                        .orElse(null);
            } catch (IOException ignored) {
                return null;
            }
        }
        return null;
    }

    private static boolean hasFrontendModuleExtension(String fileName) {
        return fileName.endsWith(".js")
                || fileName.endsWith(".mjs")
                || fileName.endsWith(".ts")
                || fileName.endsWith(".qin")
                || fileName.endsWith(".vue")
                || fileName.endsWith(".ovs")
                || fileName.endsWith(".cssts");
    }

    private static Path resolveStaticRoot(QinRuntimeProjectLayout layout, Path root, Path fromArgs) throws IOException {
        Path resolved;
        if (fromArgs != null) {
            resolved = resolvePath(root, fromArgs);
        } else if (Files.isRegularFile(root.resolve(INDEX_HTML)) || Files.isRegularFile(root.resolve(INDEX))) {
            resolved = root.toAbsolutePath().normalize();
        } else if (Files.isDirectory(layout.appDir())) {
            resolved = layout.appDir().toAbsolutePath().normalize();
        } else {
            resolved = root.resolve("build/fullstack/web").toAbsolutePath().normalize();
        }
        Files.createDirectories(resolved);
        return resolved;
    }

    private static void ensureIndexFile(Path staticRoot) throws IOException {
        Path indexHtml = staticRoot.resolve(INDEX_HTML);
        Path index = staticRoot.resolve(INDEX);
        if (!Files.exists(indexHtml) && !Files.exists(index)) {
            Files.writeString(indexHtml, defaultIndexHtml(), StandardCharsets.UTF_8);
        }
    }

    private static void startDevRebuildLoop(BuildArtifacts artifacts, Options options) {
        Thread thread = Thread.ofPlatform().name("qin-dev-rebuild-loop").daemon(true).start(() -> {
            Map<Path, FileSnapshot> lastSnapshot = snapshotDevSourceFiles(artifacts.root());
            while (true) {
                try {
                    Thread.sleep(1000L);
                    Map<Path, FileSnapshot> currentSnapshot = snapshotDevSourceFiles(artifacts.root());
                    List<Path> changedFiles = diffChangedFiles(lastSnapshot, currentSnapshot);
                    if (changedFiles.isEmpty()) {
                        continue;
                    }
                    lastSnapshot = currentSnapshot;
                    if (artifacts.applyFrontendHotRefresh(changedFiles)) {
                        System.out.println("[dev] frontend source change applied without full rebuild (version "
                                + artifacts.version() + ")");
                        continue;
                    }
                    System.out.println("[dev] source change detected, rebuilding...");
                    BuildArtifacts rebuilt = build(options);
                    artifacts.updateFrom(rebuilt, changedFiles);
                    System.out.println("[dev] rebuild finished (version " + artifacts.version() + ")");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } catch (Exception e) {
                    System.err.println("[dev] rebuild failed: " + Objects.toString(e.getMessage(), "unknown error"));
                }
            }
        });
        System.out.println("[dev] watching source files (thread: " + thread.getName() + ")");
    }

    private static Map<Path, FileSnapshot> snapshotDevSourceFiles(Path root) {
        Map<Path, FileSnapshot> snapshots = new LinkedHashMap<>();
        try (var stream = Files.walk(root)) {
            List<Path> files = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> isDevSourceFile(root, path))
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();

            for (Path file : files) {
                snapshots.put(
                        file.toAbsolutePath().normalize(),
                        new FileSnapshot(Files.getLastModifiedTime(file).toMillis(), Files.size(file)));
            }
        } catch (IOException ignored) {
            // ignore and keep current snapshot
        }
        return snapshots;
    }

    private static List<Path> diffChangedFiles(Map<Path, FileSnapshot> previous, Map<Path, FileSnapshot> current) {
        List<Path> changed = new ArrayList<>();
        for (Map.Entry<Path, FileSnapshot> entry : current.entrySet()) {
            FileSnapshot before = previous.get(entry.getKey());
            if (!entry.getValue().equals(before)) {
                changed.add(entry.getKey());
            }
        }
        for (Path file : previous.keySet()) {
            if (!current.containsKey(file)) {
                changed.add(file);
            }
        }
        changed.sort(Comparator.comparing(Path::toString));
        return changed;
    }

    private static boolean isDevSourceFile(Path root, Path file) {
        String rel = root.relativize(file).toString().replace('\\', '/');
        for (String ignored : DEV_WATCH_IGNORED_DIRS) {
            if (rel.startsWith(ignored + "/") || rel.contains("/" + ignored + "/")) {
                return false;
            }
        }

        String name = file.getFileName().toString().toLowerCase();
        if ("app.js".equals(name)) {
            return false;
        }
        for (String ext : DEV_WATCH_EXTENSIONS) {
            if (name.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    private static String defaultIndexHtml() {
        return """
                <!doctype html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>Qin Fullstack MVP</title>
                  <style>
                    body { font-family: sans-serif; margin: 2rem; }
                    pre { background: #f6f8fa; padding: 1rem; border-radius: 8px; }
                  </style>
                </head>
                <body>
                  <h1>Qin Fullstack MVP</h1>
                  <p>Backend result from <code>/api/result</code>:</p>
                  <pre id="server-data">loading...</pre>
                  <script src="/app.js"></script>
                  <script>
                    fetch("/api/result")
                      .then(r => r.json())
                      .then(data => {
                        document.getElementById("server-data").textContent = JSON.stringify(data, null, 2);
                      })
                      .catch(err => {
                        document.getElementById("server-data").textContent = "error: " + err;
                      });
                  </script>
                </body>
                </html>
                """;
    }

    private static Path resolveRoot(Path rootDir) {
        if (rootDir == null) {
            return Path.of("").toAbsolutePath().normalize();
        }
        return rootDir.toAbsolutePath().normalize();
    }

    private static Path resolvePath(Path root, Path path) {
        Path p = path;
        if (!p.isAbsolute()) {
            p = root.resolve(p);
        }
        return p.toAbsolutePath().normalize();
    }

    private static void requireFile(Path file, String flag) {
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            throw new IllegalArgumentException("Missing file for " + flag + ": " + file.toAbsolutePath());
        }
    }

    private static Options parseArgs(String[] args) {
        Options options = new Options();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "--help" -> options.showHelp = true;
                case "--dev" -> options.dev = true;
                case "--build-only" -> options.buildOnly = true;
                case "--print-ir" -> options.printIr = true;
                case "--profile" -> options.profile = true;
                case "--port" -> options.port = parsePort(nextValue(args, ++i, "--port"));
                case "--root" -> options.rootDir = Path.of(nextValue(args, ++i, "--root"));
                case "--class" -> options.className = nextValue(args, ++i, "--class");
                case "--class-out" -> options.classOutputDir = Path.of(nextValue(args, ++i, "--class-out"));
                case "--static-dir" -> options.staticDir = Path.of(nextValue(args, ++i, "--static-dir"));
                case "--backend-file" -> options.backendSourceFile = Path.of(nextValue(args, ++i, "--backend-file"));
                case "--frontend-file" -> options.frontendSourceFile = Path.of(nextValue(args, ++i, "--frontend-file"));
                default -> throw new IllegalArgumentException("Unknown arg: " + arg);
            }
        }
        return options;
    }

    private static int parsePort(String raw) {
        int port = Integer.parseInt(raw);
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Port out of range: " + port);
        }
        return port;
    }

    private static String nextValue(String[] args, int index, String flag) {
        if (index >= args.length) {
            throw new IllegalArgumentException("Missing value for " + flag);
        }
        return args[index];
    }

    private static void printHelp() {
        System.out.println("QinFullstackMain - build and serve Qin fullstack project");
        System.out.println("Usage:");
        System.out.println("  --root <path>            Project root (default: current dir)");
        System.out.println("  --port <num>             HTTP port (default: 8080)");
        System.out.println("  --dev                    Enable watch + browser auto reload");
        System.out.println("  --class <binary.name>    Generated backend class (default: com.qin.runtime.generated.ServerApp)");
        System.out.println("  --class-out <dir>        Backend class output (default: build/fullstack/server-classes)");
        System.out.println("  --static-dir <dir>       Static root (default: app/ or build/fullstack/web)");
        System.out.println("  --backend-file <file>    Backend .qin/.js/.mjs/.ts/.java source override");
        System.out.println("  --frontend-file <file>   Frontend .js/.ts/.qin/.vue/.ovs/.cssts source override");
        System.out.println("  --print-ir               Print IR summaries while building");
        System.out.println("  --profile                Print phase timings for build diagnostics");
        System.out.println("  --build-only             Build outputs only");
        System.out.println("  --help                   Show help");
    }

    private static final class Options {
        private Path rootDir;
        private int port = 8080;
        private String className = "com.qin.runtime.generated.ServerApp";
        private Path classOutputDir = Path.of("build", "fullstack", "server-classes");
        private Path staticDir;
        private Path backendSourceFile;
        private Path frontendSourceFile;
        private boolean printIr;
        private boolean profile;
        private boolean dev;
        private boolean buildOnly;
        private boolean showHelp;
    }

    private record BackendMethods(Method runMethod, Method httpAppMethod) {
    }

    @FunctionalInterface
    private interface ThrowingSupplier<T> {
        T get() throws Exception;
    }

    private record BackendBuild(Path classFile, Method runMethod, Method httpAppMethod) {
    }

    static final class BuildArtifacts implements QinDevServer.RuntimeView {
        private final Path root;
        private volatile Path staticRoot;
        private final AtomicReference<Method> runMethodRef;
        private final AtomicReference<Method> httpAppMethodRef;
        private final AtomicReference<QinFrontendEsmService> frontendEsmServiceRef;
        private final AtomicReference<List<String>> hmrMessagesRef;
        private final AtomicLong version;

        private BuildArtifacts(Path root, Path staticRoot, Method runMethod, Method httpAppMethod, QinFrontendEsmService frontendEsmService) {
            this.root = root;
            this.staticRoot = staticRoot;
            this.runMethodRef = new AtomicReference<>(runMethod);
            this.httpAppMethodRef = new AtomicReference<>(httpAppMethod);
            this.frontendEsmServiceRef = new AtomicReference<>(frontendEsmService);
            this.hmrMessagesRef = new AtomicReference<>(List.of());
            this.version = new AtomicLong(System.currentTimeMillis());
        }

        private Path root() {
            return root;
        }

        @Override
        public Path staticRoot() {
            return staticRoot;
        }

        @Override
        public Method currentRunMethod() {
            return runMethodRef.get();
        }

        @Override
        public Method currentHttpAppMethod() {
            return httpAppMethodRef.get();
        }

        @Override
        public QinFrontendEsmService frontendEsmService() {
            return frontendEsmServiceRef.get();
        }

        @Override
        public long version() {
            return version.get();
        }

        @Override
        public List<String> consumeHmrMessages() {
            return hmrMessagesRef.getAndSet(List.of());
        }

        private void updateFrom(BuildArtifacts rebuilt, List<Path> changedFiles) {
            this.staticRoot = rebuilt.staticRoot;
            this.runMethodRef.set(rebuilt.currentRunMethod());
            this.httpAppMethodRef.set(rebuilt.currentHttpAppMethod());
            this.frontendEsmServiceRef.set(rebuilt.frontendEsmService());
            QinFrontendEsmService frontend = rebuilt.frontendEsmService();
            if (frontend != null && changedFiles != null && !changedFiles.isEmpty()) {
                this.hmrMessagesRef.set(frontend.collectViteHotUpdateMessages(changedFiles));
            } else {
                this.hmrMessagesRef.set(List.of());
            }
            this.version.incrementAndGet();
        }

        boolean applyFrontendHotRefresh(List<Path> changedFiles) throws Exception {
            QinFrontendEsmService frontend = frontendEsmServiceRef.get();
            if (frontend == null || changedFiles == null || changedFiles.isEmpty()) {
                return false;
            }
            if (!frontend.refreshChangedFrontendModules(changedFiles)) {
                return false;
            }
            this.hmrMessagesRef.set(frontend.collectViteHotUpdateMessages(changedFiles));
            this.version.incrementAndGet();
            return true;
        }
    }

    private record FileSnapshot(long modifiedMillis, long size) {
    }
}
