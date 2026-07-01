package com.qin.runtime.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    private QinFullstackMain() {
    }

    public static void main(String[] args) throws Exception {
        Options options = parseArgs(args);
        if (options.showHelp) {
            printHelp();
            return;
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
        Path root = resolveRoot(options.rootDir);
        QinRuntimeProjectLayout layout = QinRuntimeProjectLayout.discover(root);
        QinBuildCoordinator coordinator = new QinBuildCoordinator();

        materializeProjectNpmDependencies(root);
        Path backendSource = resolveBackendSource(layout, options.backendSourceFile);
        Path frontendSource = resolveFrontendSource(layout, options.frontendSourceFile);
        Path classOutputDir = resolvePath(root, options.classOutputDir);
        Path staticRoot = resolveStaticRoot(layout, root, options.staticDir);
        Path jsOutputFile = staticRoot.resolve("app.js").normalize();

        BackendBuild backendBuild = buildBackend(
                coordinator,
                root,
                backendSource,
                classOutputDir,
                jsOutputFile,
                options);
        Method runMethod = backendBuild.runMethod();
        QinFrontendEsmService frontendEsmService = null;

        if (frontendSource != null) {
            frontendEsmService = QinFrontendEsmService.create(root, frontendSource);
            if (!options.dev || options.buildOnly) {
                frontendEsmService.emitProduction(staticRoot);
            }
        } else {
            Files.createDirectories(staticRoot);
            if (!Files.exists(jsOutputFile)) {
                Files.writeString(jsOutputFile, "console.log('Qin frontend source not found.');", StandardCharsets.UTF_8);
            }
        }

        ensureIndexFile(staticRoot);

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
        Path adapterSource = writeQinBackendAdapterSource(
                root,
                classOutputDir,
                options.className + "FullstackAdapter",
                options.className);
        BackendBuild adapterBuild = compileJavaBackend(root, adapterSource, classOutputDir);
        return new BackendBuild(adapterBuild.classFile(), adapterBuild.runMethod(), adapterBuild.httpAppMethod());
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
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("JDK compiler is required for " + description + ".");
        }

        Files.createDirectories(classOutputDir);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            Iterable<? extends JavaFileObject> units = fileManager.getJavaFileObjectsFromPaths(sourceFiles);
            List<String> javacOptions = new ArrayList<>();
            javacOptions.add("-encoding");
            javacOptions.add("UTF-8");
            javacOptions.add("-d");
            javacOptions.add(classOutputDir.toString());
            javacOptions.add("-sourcepath");
            javacOptions.add(root.toString());
            String classpath = System.getProperty("java.class.path", "");
            javacOptions.add("-classpath");
            javacOptions.add(classpath == null || classpath.isBlank()
                    ? classOutputDir.toString()
                    : classOutputDir + java.io.File.pathSeparator + classpath);

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
    }

    private record FileSnapshot(long modifiedMillis, long size) {
    }
}
