package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.backend.js.QinIrCodeBackend;
import com.qin.lang.backend.js.QinTsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.slime.java.ast.JavaAstArrayAccessExpression;
import com.slime.java.ast.JavaAstArrayLiteralExpression;
import com.slime.java.ast.JavaAstAssignmentExpression;
import com.slime.java.ast.JavaAstBinaryExpression;
import com.slime.java.ast.JavaAstBreakStatement;
import com.slime.java.ast.JavaAstCatchClause;
import com.slime.java.ast.JavaAstCastExpression;
import com.slime.java.ast.JavaAstClassDeclaration;
import com.slime.java.ast.JavaAstClassLiteralExpression;
import com.slime.java.ast.JavaAstContinueStatement;
import com.slime.java.ast.JavaAstDoWhileStatement;
import com.slime.java.ast.JavaAstEnhancedForStatement;
import com.slime.java.ast.JavaAstExpression;
import com.slime.java.ast.JavaAstExpressionStatement;
import com.slime.java.ast.JavaAstFieldDeclaration;
import com.slime.java.ast.JavaAstForStatement;
import com.slime.java.ast.JavaAstIdentifierExpression;
import com.slime.java.ast.JavaAstIfStatement;
import com.slime.java.ast.JavaAstImportDeclaration;
import com.slime.java.ast.JavaAstInstanceofExpression;
import com.slime.java.ast.JavaAstInstanceofPatternExpression;
import com.slime.java.ast.JavaAstLambdaExpression;
import com.slime.java.ast.JavaAstLocalVariableDeclaration;
import com.slime.java.ast.JavaAstMemberAccessExpression;
import com.slime.java.ast.JavaAstMethodCallExpression;
import com.slime.java.ast.JavaAstMethodDeclaration;
import com.slime.java.ast.JavaAstMethodReferenceExpression;
import com.slime.java.ast.JavaAstNewExpression;
import com.slime.java.ast.JavaAstParameter;
import com.slime.java.ast.JavaAstProgram;
import com.slime.java.ast.JavaAstReturnStatement;
import com.slime.java.ast.JavaAstStatement;
import com.slime.java.ast.JavaAstSwitchCase;
import com.slime.java.ast.JavaAstSwitchExpression;
import com.slime.java.ast.JavaAstSwitchStatement;
import com.slime.java.ast.JavaAstThrowStatement;
import com.slime.java.ast.JavaAstTryStatement;
import com.slime.java.ast.JavaAstUnaryExpression;
import com.slime.java.ast.JavaAstUpdateExpression;
import com.slime.java.ast.JavaAstWhileStatement;
import com.slime.java.ast.JavaAstYieldStatement;
import com.slime.java.ast.JavaCstToAst;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class QinJavaProjectJsCompiler {
    public String compileSuperclassClosure(
            List<Path> sourceRoots,
            String entryBinaryName,
            Path outputFile) throws IOException {
        return compileSuperclassClosure(sourceRoots, entryBinaryName, outputFile, new QinJsBackend());
    }

    public String compileSuperclassClosureTs(
            List<Path> sourceRoots,
            String entryBinaryName,
            Path outputFile) throws IOException {
        return compileSuperclassClosure(sourceRoots, entryBinaryName, outputFile, new QinTsBackend());
    }

    private String compileSuperclassClosure(
            List<Path> sourceRoots,
            String entryBinaryName,
            Path outputFile,
            QinIrCodeBackend backend) throws IOException {
        Map<String, Path> sourceFiles = sourceDependencyFiles(sourceRoots, entryBinaryName);
        Map<String, JavaAstProgram> parsedPrograms = parseSourceFiles(sourceFiles);

        QinIrProgram bundleProgram;
        try {
            bundleProgram = sortJavaClassDeclarations(lowerBundle(parsedPrograms.values()));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Could not lower Java source bundle for " + entryBinaryName, e);
        }
        String generated = backend.compileProgram(bundleProgram)
                + projectExports(bundleProgram, backend.options().emitTypeAnnotations());
        Files.createDirectories(outputFile.getParent());
        Files.writeString(outputFile, generated, StandardCharsets.UTF_8);
        return generated;
    }

    public List<EsmFileOutput> compileSuperclassClosureEsmFiles(
            List<Path> sourceRoots,
            String entryBinaryName,
            Path outputRoot) throws IOException {
        return compileSuperclassClosureEsmFiles(sourceRoots, entryBinaryName, outputRoot, new QinJsBackend());
    }

    public List<EsmFileOutput> compileSuperclassClosureEsmTsFiles(
            List<Path> sourceRoots,
            String entryBinaryName,
            Path outputRoot) throws IOException {
        return compileSuperclassClosureEsmFiles(sourceRoots, entryBinaryName, List.of(), outputRoot, new QinTsBackend());
    }

    public List<EsmFileOutput> compileSuperclassClosureEsmTsFiles(
            List<Path> sourceRoots,
            String entryBinaryName,
            List<String> additionalEntryBinaryNames,
            Path outputRoot) throws IOException {
        return compileSuperclassClosureEsmFiles(
                sourceRoots,
                entryBinaryName,
                additionalEntryBinaryNames,
                outputRoot,
                new QinTsBackend());
    }

    private List<EsmFileOutput> compileSuperclassClosureEsmFiles(
            List<Path> sourceRoots,
            String entryBinaryName,
            Path outputRoot,
            QinIrCodeBackend backend) throws IOException {
        return compileSuperclassClosureEsmFiles(sourceRoots, entryBinaryName, List.of(), outputRoot, backend);
    }

    private List<EsmFileOutput> compileSuperclassClosureEsmFiles(
            List<Path> sourceRoots,
            String entryBinaryName,
            List<String> additionalEntryBinaryNames,
            Path outputRoot,
            QinIrCodeBackend backend) throws IOException {
        List<String> rootBinaryNames = rootBinaryNames(entryBinaryName, additionalEntryBinaryNames);
        Map<String, Path> sourceFiles = sourceDependencyFiles(sourceRoots, rootBinaryNames);
        Map<String, JavaAstProgram> parsedPrograms = parseSourceFiles(sourceFiles);
        QinIrProgram bundleProgram;
        try {
            bundleProgram = lowerBundle(parsedPrograms.values());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Could not lower Java source bundle for " + entryBinaryName, e);
        }

        Map<String, Path> outputFilesByBinaryName = new LinkedHashMap<>();
        Map<String, Set<String>> programClassBinaryNames = new LinkedHashMap<>();
        for (Map.Entry<String, Path> sourceEntry : sourceFiles.entrySet()) {
            String sourceBinaryName = sourceEntry.getKey();
            Path outputFile = outputFileForSource(sourceRoots, sourceEntry.getValue(), outputRoot, backend.fileExtension());
            outputFilesByBinaryName.put(sourceBinaryName, outputFile);
            programClassBinaryNames.put(
                    sourceBinaryName,
                    programClassBinaryNames(parsedPrograms.get(sourceBinaryName)));
        }

        List<EsmFileOutput> outputs = new ArrayList<>();
        writeJavaSdkJsPackage(outputRoot);
        for (Map.Entry<String, Path> sourceEntry : sourceFiles.entrySet()) {
            String sourceBinaryName = sourceEntry.getKey();
            Path sourceFile = sourceEntry.getValue();
            Path outputFile = outputFilesByBinaryName.get(sourceBinaryName);
            Set<String> localClassBinaryNames = programClassBinaryNames.get(sourceBinaryName);
            QinIrProgram fileProgram = programWithClasses(bundleProgram, localClassBinaryNames);
            Set<String> dependencyBinaryNames = referencedSourceBinaryNames(
                    parsedPrograms.get(sourceBinaryName),
                    sourceFiles.keySet());
            dependencyBinaryNames.addAll(superclassClosureDependencyBinaryNames(
                    sourceBinaryName,
                    sourceFiles.keySet()));
            dependencyBinaryNames.addAll(superclassDependencyBinaryNames(
                    parsedPrograms.get(sourceBinaryName),
                    sourceFiles.keySet()));
            Set<String> externallyBoundClassBinaryNames = externallyBoundClassBinaryNames(
                    sourceBinaryName,
                    outputFile,
                    dependencyBinaryNames,
                    outputFilesByBinaryName,
                    programClassBinaryNames);
            String generated = esmImports(
                    sourceBinaryName,
                    outputFile,
                    dependencyBinaryNames,
                    outputFilesByBinaryName,
                    programClassBinaryNames)
                    + backend.compileProgramWithExternalJavaSdk(
                            fileProgram,
                            externallyBoundClassBinaryNames,
                            bundleProgram.classDeclarations())
                    + esmExports(fileProgram);
            Files.createDirectories(outputFile.getParent());
            Files.writeString(outputFile, generated, StandardCharsets.UTF_8);
            outputs.add(new EsmFileOutput(sourceBinaryName, sourceFile, outputFile, generated));
        }
        writeGeneratedEsmPackage(outputRoot, entryBinaryName, additionalEntryBinaryNames, backend, bundleProgram);
        return List.copyOf(outputs);
    }

    private List<String> rootBinaryNames(String entryBinaryName, List<String> additionalEntryBinaryNames) {
        LinkedHashSet<String> rootBinaryNames = new LinkedHashSet<>();
        rootBinaryNames.add(entryBinaryName);
        if (additionalEntryBinaryNames != null) {
            rootBinaryNames.addAll(additionalEntryBinaryNames);
        }
        return List.copyOf(rootBinaryNames);
    }

    private Set<String> superclassClosureDependencyBinaryNames(String binaryName, Set<String> sourceBinaryNames) {
        Set<String> dependencies = new LinkedHashSet<>();
        Class<?> current = loadClass(binaryName).getSuperclass();
        while (current != null && current != Object.class) {
            String superclassBinaryName = current.getName();
            if (sourceBinaryNames.contains(superclassBinaryName)) {
                dependencies.add(superclassBinaryName);
            }
            current = current.getSuperclass();
        }
        return dependencies;
    }

    private Set<String> superclassDependencyBinaryNames(JavaAstProgram program, Set<String> sourceBinaryNames) {
        TypeResolver resolver = new TypeResolver(program, sourceBinaryNames);
        Set<String> referenced = new LinkedHashSet<>();
        for (JavaAstClassDeclaration classDeclaration : program.classes()) {
            collectSuperclassDependencyBinaryNames(classDeclaration, resolver, sourceBinaryNames, referenced);
        }
        return referenced;
    }

    private void collectSuperclassDependencyBinaryNames(
            JavaAstClassDeclaration classDeclaration,
            TypeResolver resolver,
            Set<String> sourceBinaryNames,
            Set<String> referenced) {
        collectTypeReference(classDeclaration.superTypeName(), resolver, sourceBinaryNames, referenced);
        for (JavaAstClassDeclaration nestedClass : classDeclaration.nestedClasses()) {
            collectSuperclassDependencyBinaryNames(nestedClass, resolver, sourceBinaryNames, referenced);
        }
    }

    private void writeJavaSdkJsPackage(Path outputRoot) throws IOException {
        Path packageRoot = outputRoot.getParent()
                .resolve("java-sdk-js")
                .normalize();
        cleanGeneratedJavaSdkPackage(outputRoot, packageRoot);
        cleanEmbeddedJavaSdkPackage(outputRoot);
        Files.createDirectories(packageRoot);
        JavaSdkSource javaSdkSource = findSiblingJavaSdkSource(outputRoot);
        Files.writeString(
                packageRoot.resolve("package.json"),
                javaSdkPackageJson(javaSdkSource.packageName()),
                StandardCharsets.UTF_8);
        if (javaSdkSource.fromSiblingProject()) {
            copyJavaSdkSourceTree(javaSdkSource, packageRoot);
        } else {
            Files.writeString(packageRoot.resolve("index.js"), javaSdkSource.source(), StandardCharsets.UTF_8);
        }
    }

    private void cleanGeneratedJavaSdkPackage(Path outputRoot, Path packageRoot) throws IOException {
        Path expectedPackageRoot = outputRoot.getParent().resolve("java-sdk-js").toAbsolutePath().normalize();
        Path absolutePackageRoot = packageRoot.toAbsolutePath().normalize();
        if (!absolutePackageRoot.equals(expectedPackageRoot)) {
            throw new IllegalStateException("Refusing to clean unexpected Java SDK package path: " + absolutePackageRoot);
        }
        if (!Files.exists(absolutePackageRoot)) {
            return;
        }
        try (Stream<Path> stream = Files.walk(absolutePackageRoot)) {
            List<Path> paths = stream.sorted((left, right) -> right.compareTo(left)).toList();
            for (Path path : paths) {
                Files.delete(path);
            }
        }
    }

    private void cleanEmbeddedJavaSdkPackage(Path outputRoot) throws IOException {
        Path embeddedNodeModules = outputRoot.resolve("node_modules").toAbsolutePath().normalize();
        if (!Files.exists(embeddedNodeModules)) {
            return;
        }
        try (Stream<Path> stream = Files.walk(embeddedNodeModules)) {
            List<Path> paths = stream.sorted((left, right) -> right.compareTo(left)).toList();
            for (Path path : paths) {
                Files.delete(path);
            }
        }
    }

    private void copyJavaSdkSourceTree(JavaSdkSource javaSdkSource, Path packageRoot) throws IOException {
        Path sourceDir = javaSdkSource.sourceRoot();
        try (Stream<Path> stream = Files.walk(sourceDir)) {
            for (Path sourceFile : stream.filter(Files::isRegularFile).toList()) {
                String fileName = sourceFile.getFileName().toString();
                if (!fileName.endsWith(".ts") && !fileName.endsWith(".js") && !"package.json".equals(fileName)) {
                    continue;
                }
                Path relative = sourceDir.relativize(sourceFile);
                Path target = packageRoot.resolve("package.json".equals(fileName) ? relative : jsModuleFileName(relative)).normalize();
                Files.createDirectories(target.getParent());
                String source = Files.readString(sourceFile, StandardCharsets.UTF_8);
                if (sourceFile.equals(javaSdkSource.sourceFile())) {
                    source = source.replaceFirst("^// Qin Java SDK for generated JavaScript\\R", "");
                    source = "// Qin Java SDK for generated JavaScript\n"
                            + "// Source: " + sourceFile.toAbsolutePath().normalize() + "\n"
                            + source;
                }
                Files.writeString(target, source, StandardCharsets.UTF_8);
            }
        }
    }

    private Path jsModuleFileName(Path relative) {
        Path parent = relative.getParent();
        String fileName = relative.getFileName().toString();
        if (fileName.endsWith(".ts")) {
            fileName = fileName.substring(0, fileName.length() - ".ts".length()) + ".js";
        }
        return parent == null ? Path.of(fileName) : parent.resolve(fileName);
    }

    private JavaSdkSource findSiblingJavaSdkSource(Path outputRoot) throws IOException {
        Path current = outputRoot.toAbsolutePath().normalize();
        while (current != null) {
            Path candidate = current.resolve("java-sdk").resolve("qin.config.js");
            if (Files.isRegularFile(candidate)) {
                return readJavaSdkSource(candidate);
            }
            current = current.getParent();
        }
        current = outputRoot.toAbsolutePath().normalize();
        while (current != null) {
            Path generatedPackage = current.resolve("java-sdk-js").resolve("package.json");
            if (Files.isRegularFile(generatedPackage)) {
                return readJavaSdkPackageSource(generatedPackage);
            }
            Path generatedSiblingPackage = current.resolve("generated").resolve("java-sdk-js").resolve("package.json");
            if (Files.isRegularFile(generatedSiblingPackage)) {
                return readJavaSdkPackageSource(generatedSiblingPackage);
            }
            current = current.getParent();
        }
        return new JavaSdkSource(
                QinJsBackend.javaSdkJsPackageName(),
                null,
                null,
                QinJsBackend.javaSdkJsModule(),
                false);
    }

    private JavaSdkSource readJavaSdkPackageSource(Path packageJsonFile) throws IOException {
        String packageJson = Files.readString(packageJsonFile, StandardCharsets.UTF_8);
        String packageName = readConfigString(packageJson, "name", QinJsBackend.javaSdkJsPackageName());
        Path sourceFile = packageJsonFile.getParent().resolve("index.js").normalize();
        if (!Files.isRegularFile(sourceFile)) {
            throw new IllegalStateException("java-sdk-js package index.js does not exist: " + sourceFile);
        }
        return new JavaSdkSource(
                packageName,
                packageJsonFile.getParent(),
                sourceFile,
                Files.readString(sourceFile, StandardCharsets.UTF_8),
                true);
    }

    private JavaSdkSource readJavaSdkSource(Path configFile) throws IOException {
        String config = Files.readString(configFile, StandardCharsets.UTF_8);
        String packageName = readConfigString(config, "name", QinJsBackend.javaSdkJsPackageName());
        String entry = readConfigString(config, "entry", "src/index.ts");
        Path sourceFile = configFile.getParent().resolve(entry).normalize();
        if (!Files.isRegularFile(sourceFile)) {
            throw new IllegalStateException("java-sdk qin.config.js entry does not exist: " + sourceFile);
        }
        return new JavaSdkSource(
                packageName,
                sourceFile.getParent(),
                sourceFile,
                Files.readString(sourceFile, StandardCharsets.UTF_8),
                true);
    }

    private String readConfigString(String config, String name, String defaultValue) {
        Matcher matcher = Pattern.compile("\\b" + Pattern.quote(name) + "\\s*:\\s*['\"]([^'\"]+)['\"]")
                .matcher(config);
        return matcher.find() ? matcher.group(1) : defaultValue;
    }

    private String javaSdkPackageJson(String packageName) {
        return """
                {
                  "name": "%s",
                  "version": "0.0.0-qin-generated",
                  "type": "module",
                  "exports": {
                    ".": "./index.js",
                    "./tooling": "./tooling/index.js"
                  }
                }
                """.formatted(packageName);
    }

    private void writeGeneratedEsmPackage(
            Path outputRoot,
            String entryBinaryName,
            List<String> additionalEntryBinaryNames,
            QinIrCodeBackend backend,
            QinIrProgram program) throws IOException {
        String packageName = generatedPackageName(entryBinaryName, backend);
        String extension = backend.fileExtension();
        Files.createDirectories(outputRoot);
        Files.writeString(
                outputRoot.resolve("package.json"),
                generatedPackageJson(
                        packageName,
                        entryBinaryName,
                        additionalEntryBinaryNames,
                        extension,
                        backend.options().emitTypeAnnotations()),
                StandardCharsets.UTF_8);
        Files.writeString(
                outputRoot.resolve("qin.config.js"),
                generatedQinConfig(packageName, entryBinaryName, additionalEntryBinaryNames, extension),
                StandardCharsets.UTF_8);
        Files.writeString(
                outputRoot.resolve("index." + extension),
                generatedPackageIndex(
                        entryBinaryName,
                        additionalEntryBinaryNames,
                        extension,
                        program,
                        backend.options().emitTypeAnnotations()),
                StandardCharsets.UTF_8);
        if (shouldGenerateSlimeCstToAstBridge(additionalEntryBinaryNames)) {
            Files.writeString(
                    outputRoot.resolve("SlimeCstToAstBridge." + extension),
                    generatedSlimeCstToAstBridge(program, extension, backend.options().emitTypeAnnotations()),
                    StandardCharsets.UTF_8);
        }
        if (isGeneratedSlimeParserEntry(entryBinaryName)) {
            writeGeneratedSlimeParserCompatibilityEntries(outputRoot, extension);
        }
    }

    private void writeGeneratedSlimeParserCompatibilityEntries(Path outputRoot, String extension) throws IOException {
        Path src = outputRoot.resolve("src");
        Path languageEs2025 = src.resolve("language").resolve("es2025");
        Files.createDirectories(languageEs2025);
        Files.writeString(src.resolve("index." + extension), """
                export * from "../index.%1$s";
                export { default } from "../index.%1$s";
                """.formatted(extension), StandardCharsets.UTF_8);
        Files.writeString(src.resolve("SlimeParser." + extension), """
                export * from "../index.%1$s";
                export { default } from "../index.%1$s";
                """.formatted(extension), StandardCharsets.UTF_8);
        Files.writeString(src.resolve("SlimeTokenConsumer." + extension), """
                export {
                  SlimeTokenConsumer,
                  SlimeTokenConsumer as default
                } from "../index.%1$s";
                """.formatted(extension), StandardCharsets.UTF_8);
        Files.writeString(src.resolve("SlimeTokens." + extension), """
                export {
                  JavaScriptTokens,
                  ReservedWords,
                  SlimeTokensObj,
                  SlimeJavascriptTokensObj,
                  slimeTokens,
                  slimeTokens as default
                } from "../index.%1$s";
                """.formatted(extension), StandardCharsets.UTF_8);
        Files.writeString(languageEs2025.resolve("SlimeParser." + extension), """
                export * from "../../../index.%1$s";
                export { default } from "../../../index.%1$s";
                """.formatted(extension), StandardCharsets.UTF_8);
        Files.writeString(languageEs2025.resolve("SlimeTokenConsumer." + extension), """
                export {
                  SlimeTokenConsumer,
                  SlimeTokenConsumer as default
                } from "../../../index.%1$s";
                """.formatted(extension), StandardCharsets.UTF_8);
        Files.writeString(languageEs2025.resolve("SlimeTokens." + extension), """
                export {
                  JavaScriptTokens,
                  ReservedWords,
                  SlimeTokensObj,
                  SlimeJavascriptTokensObj,
                  slimeTokens,
                  slimeTokens as default
                } from "../../../index.%1$s";
                """.formatted(extension), StandardCharsets.UTF_8);
    }

    private String generatedPackageName(String entryBinaryName, QinIrCodeBackend backend) {
        String suffix = backend.options().emitTypeAnnotations() ? "-ts" : "-js";
        return "@qin/generated-" + kebabCase(simpleClassName(entryBinaryName)) + suffix;
    }

    private String kebabCase(String value) {
        String spaced = value.replaceAll("([a-z0-9])([A-Z])", "$1-$2");
        return spaced.replace('_', '-').replace('$', '-').toLowerCase();
    }

    private String generatedPackageJson(
            String packageName,
            String entryBinaryName,
            List<String> additionalEntryBinaryNames,
            String extension,
            boolean typeScript) {
        String indexFile = "./index." + extension;
        String entryFile = "./" + entryBinaryName.replace('.', '/') + "." + extension;
        String type = typeScript ? "typescript" : "javascript";
        String compatibilityExports = isGeneratedSlimeParserEntry(entryBinaryName)
                ? generatedSlimeParserCompatibilityPackageExports(extension)
                : "";
        String compatibilityFiles = isGeneratedSlimeParserEntry(entryBinaryName)
                ? "    \"src\",\n"
                : "";
        String cstToAstBridgeExports = shouldGenerateSlimeCstToAstBridge(additionalEntryBinaryNames)
                ? generatedSlimeCstToAstBridgePackageExports(extension)
                : "";
        String subhutiStructExports = shouldGenerateSlimeCstToAstBridge(additionalEntryBinaryNames)
                ? generatedSubhutiStructPackageExports(extension)
                : "";
        String cstToAstBridgeFiles = shouldGenerateSlimeCstToAstBridge(additionalEntryBinaryNames)
                ? "    \"SlimeCstToAstBridge.%s\",\n".formatted(extension)
                : "";
        String additionalEntryExports = generatedAdditionalEntryPackageExports(additionalEntryBinaryNames, extension);
        return """
                {
                  "name": "%s",
                  "version": "0.0.0-qin-generated",
                  "private": true,
                  "type": "module",
                  "description": "Qin generated %s ESM package for %s.",
                  "main": "%s",
                  "module": "%s",
                  "exports": {
                    ".": {
                      "types": "%s",
                      "import": "%s",
                      "default": "%s"
                    },
                %s
                %s
                %s
                %s
                    "./entry": {
                      "types": "%s",
                      "import": "%s",
                      "default": "%s"
                    },
                    "./package.json": "./package.json"
                  },
                  "qin": {
                    "generated": true,
                    "entryBinaryName": "%s",
                    "format": "%s-esm-files"
                  },
                  "dependencies": {
                    "%s": "file:../java-sdk-js"
                  },
                  "files": [
                    "com",
                %s
                %s
                    "index.%s",
                    "qin.config.js"
                  ]
                }
                """.formatted(
                escapeJs(packageName),
                type,
                escapeJs(entryBinaryName),
                indexFile,
                indexFile,
                indexFile,
                indexFile,
                indexFile,
                compatibilityExports,
                additionalEntryExports,
                cstToAstBridgeExports,
                subhutiStructExports,
                entryFile,
                entryFile,
                entryFile,
                escapeJs(entryBinaryName),
                typeScript ? "ts" : "js",
                QinJsBackend.javaSdkJsPackageName(),
                compatibilityFiles,
                cstToAstBridgeFiles,
                extension);
    }

    private String generatedAdditionalEntryPackageExports(
            List<String> additionalEntryBinaryNames,
            String extension) {
        if (additionalEntryBinaryNames == null || additionalEntryBinaryNames.isEmpty()) {
            return "";
        }
        StringBuilder exports = new StringBuilder();
        for (String binaryName : additionalEntryBinaryNames) {
            String sourceBinaryName = binaryName.contains("$")
                    ? binaryName.substring(0, binaryName.indexOf('$'))
                    : binaryName;
            String sourceFile = "./" + sourceBinaryName.replace('.', '/') + "." + extension;
            exports.append("    \"./")
                    .append(escapeJs(simpleClassName(binaryName)))
                    .append("\": {\n")
                    .append("      \"types\": \"")
                    .append(sourceFile)
                    .append("\",\n")
                    .append("      \"import\": \"")
                    .append(sourceFile)
                    .append("\",\n")
                    .append("      \"default\": \"")
                    .append(sourceFile)
                    .append("\"\n")
                    .append("    },\n");
        }
        return exports.toString();
    }

    private boolean shouldGenerateSlimeCstToAstBridge(List<String> additionalEntryBinaryNames) {
        return additionalEntryBinaryNames != null
                && additionalEntryBinaryNames.contains("com.slime.parser.cstToAst.SlimeCstToAstUtils");
    }

    private String generatedSlimeCstToAstBridgePackageExports(String extension) {
        return """
                    "./SlimeCstToAstBridge": {
                      "types": "./SlimeCstToAstBridge.%1$s",
                      "import": "./SlimeCstToAstBridge.%1$s",
                      "default": "./SlimeCstToAstBridge.%1$s"
                    },
                """.formatted(extension);
    }

    private String generatedSubhutiStructPackageExports(String extension) {
        return """
                    "./SubhutiSourceLocation": {
                      "types": "./com/subhuti/struct/SubhutiSourceLocation.%1$s",
                      "import": "./com/subhuti/struct/SubhutiSourceLocation.%1$s",
                      "default": "./com/subhuti/struct/SubhutiSourceLocation.%1$s"
                    },
                    "./SubhutiPosition": {
                      "types": "./com/subhuti/struct/SubhutiPosition.%1$s",
                      "import": "./com/subhuti/struct/SubhutiPosition.%1$s",
                      "default": "./com/subhuti/struct/SubhutiPosition.%1$s"
                    },
                """.formatted(extension);
    }

    private String generatedSlimeCstToAstBridge(
            QinIrProgram program,
            String extension,
            boolean typeScript) {
        QinIrClassDeclaration utilityClass = program.classDeclarations().stream()
                .filter(classDeclaration -> "com.slime.parser.cstToAst.SlimeCstToAstUtils"
                        .equals(classDeclaration.binaryName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cannot generate SlimeCstToAstBridge without com.slime.parser.cstToAst.SlimeCstToAstUtils"));
        String generatedIdentifier =
                QinJsBackend.generatedJavaClassIdentifier("com.slime.parser.cstToAst.SlimeCstToAstUtils");
        String typeArgs = typeScript ? "...args: any[]" : "...args";
        String returnAny = typeScript ? ": any" : "";
        String instanceType = typeScript ? ": SlimeCstToAst" : "";
        String instanceParamType = typeScript ? "instance: SlimeCstToAst" : "instance";
        String nullReturnType = typeScript ? ": void" : "";

        StringBuilder js = new StringBuilder();
        js.append("// Generated Slime CST-to-AST bridge by Qin. Source Java: ")
                .append(utilityClass.binaryName())
                .append('\n');
        js.append("import { ")
                .append(generatedIdentifier)
                .append(" as __QinGeneratedSlimeCstToAstUtils } from \"./com/slime/parser/cstToAst/SlimeCstToAstUtils.")
                .append(extension)
                .append("\";\n\n");
        js.append("export class SlimeCstToAst extends __QinGeneratedSlimeCstToAstUtils {\n");
        js.append("  constructor(...args")
                .append(typeScript ? ": any[]" : "")
                .append(") {\n");
        js.append("    if (args.length !== 0) {\n");
        js.append("      throw new Error(\"Unsupported SlimeCstToAst constructor arity: \" + args.length);\n");
        js.append("    }\n");
        js.append("    super();\n");
        js.append("    __qinBindSlimeCstToAstTransformer(this);\n");
        js.append("    registerSlimeCstToAstUtil(this);\n");
        js.append("  }\n");
        LinkedHashSet<String> methodNames = new LinkedHashSet<>();
        for (QinIrMethodDeclaration method : utilityClass.methods()) {
            if ("constructor".equals(method.name()) || !methodNames.add(method.name())) {
                continue;
            }
        }
        js.append("}\n\n");
        js.append("function __qinBindSlimeCstToAstTransformer(instance")
                .append(typeScript ? ": any" : "")
                .append(")")
                .append(nullReturnType)
                .append(" {\n");
        js.append("  for (const key of Object.keys(instance)) {\n");
        js.append("    const helper = instance[key];\n");
        js.append("    if (helper && typeof helper === \"object\" && \"__qin_field_transformer\" in helper) {\n");
        js.append("      helper.__qin_field_transformer = instance;\n");
        js.append("    }\n");
        js.append("  }\n");
        js.append("}\n\n");
        js.append("let __qinSlimeCstToAstUtils")
                .append(instanceType)
                .append(";\n");
        js.append("export function registerSlimeCstToAstUtil(")
                .append(instanceParamType)
                .append(")")
                .append(nullReturnType)
                .append(" {\n");
        js.append("  __qinSlimeCstToAstUtils = instance;\n");
        js.append("}\n\n");
        js.append("export const SlimeCstToAstUtils = {}")
                .append(typeScript ? " as SlimeCstToAst" : "")
                .append(";\n");
        js.append("const __qinSlimeCstToAstFacade")
                .append(typeScript ? ": any" : "")
                .append(" = SlimeCstToAstUtils;\n");
        for (String methodName : methodNames) {
            js.append("__qinSlimeCstToAstFacade.")
                    .append(methodName)
                    .append(" = function (")
                    .append(typeArgs)
                    .append(")")
                    .append(returnAny)
                    .append(" {\n");
            js.append("  return __qinSlimeCstToAstUtils.")
                    .append(methodName)
                    .append("(...args);\n");
            js.append("};\n");
        }
        js.append("\n__qinSlimeCstToAstUtils = new SlimeCstToAst();\n");
        return js.toString();
    }

    private boolean isGeneratedSlimeParserEntry(String entryBinaryName) {
        return "com.slime.parser.SlimeParser".equals(entryBinaryName);
    }

    private String generatedSlimeParserCompatibilityPackageExports(String extension) {
        return """
                    "./src": {
                      "types": "./src/index.%1$s",
                      "import": "./src/index.%1$s",
                      "default": "./src/index.%1$s"
                    },
                    "./src/index.%1$s": {
                      "types": "./src/index.%1$s",
                      "import": "./src/index.%1$s",
                      "default": "./src/index.%1$s"
                    },
                    "./src/SlimeParser.%1$s": {
                      "types": "./src/SlimeParser.%1$s",
                      "import": "./src/SlimeParser.%1$s",
                      "default": "./src/SlimeParser.%1$s"
                    },
                    "./src/SlimeTokenConsumer.%1$s": {
                      "types": "./src/SlimeTokenConsumer.%1$s",
                      "import": "./src/SlimeTokenConsumer.%1$s",
                      "default": "./src/SlimeTokenConsumer.%1$s"
                    },
                    "./src/SlimeTokens.%1$s": {
                      "types": "./src/SlimeTokens.%1$s",
                      "import": "./src/SlimeTokens.%1$s",
                      "default": "./src/SlimeTokens.%1$s"
                    },
                    "./src/language/es2025/SlimeParser.%1$s": {
                      "types": "./src/language/es2025/SlimeParser.%1$s",
                      "import": "./src/language/es2025/SlimeParser.%1$s",
                      "default": "./src/language/es2025/SlimeParser.%1$s"
                    },
                    "./src/language/es2025/SlimeTokenConsumer.%1$s": {
                      "types": "./src/language/es2025/SlimeTokenConsumer.%1$s",
                      "import": "./src/language/es2025/SlimeTokenConsumer.%1$s",
                      "default": "./src/language/es2025/SlimeTokenConsumer.%1$s"
                    },
                    "./src/language/es2025/SlimeTokens.%1$s": {
                      "types": "./src/language/es2025/SlimeTokens.%1$s",
                      "import": "./src/language/es2025/SlimeTokens.%1$s",
                      "default": "./src/language/es2025/SlimeTokens.%1$s"
                    },
                """.formatted(extension);
    }

    private String generatedQinConfig(
            String packageName,
            String entryBinaryName,
            List<String> additionalEntryBinaryNames,
            String extension) {
        String additionalEntries = "";
        if (additionalEntryBinaryNames != null && !additionalEntryBinaryNames.isEmpty()) {
            additionalEntries = ",\n    additionalEntryBinaryNames: ["
                    + additionalEntryBinaryNames.stream()
                    .map(value -> "\"" + escapeJs(value) + "\"")
                    .collect(java.util.stream.Collectors.joining(", "))
                    + "]";
        }
        return """
                export default {
                  name: "%s",
                  type: "library",
                  entry: "./index.%s",
                  generated: {
                    source: "java",
                    entryBinaryName: "%s"%s
                  }
                }
                """.formatted(escapeJs(packageName), extension, escapeJs(entryBinaryName), additionalEntries);
    }

    private String generatedPackageIndex(
            String entryBinaryName,
            List<String> additionalEntryBinaryNames,
            String extension,
            QinIrProgram program,
            boolean typeScript) {
        Set<String> binaryNames = new LinkedHashSet<>();
        for (var classDeclaration : program.classDeclarations()) {
            binaryNames.add(classDeclaration.binaryName());
        }

        StringBuilder js = new StringBuilder();
        js.append("// Generated package entry by Qin. Source Java entry: ")
                .append(entryBinaryName)
                .append('\n');
        boolean slimeParserEntry = "com.slime.parser.SlimeParser".equals(entryBinaryName)
                && binaryNames.contains(entryBinaryName);
        if (binaryNames.contains(entryBinaryName)) {
            String entryIdentifier = QinJsBackend.generatedJavaClassIdentifier(entryBinaryName);
            String sourceBinaryName = entryBinaryName.contains("$")
                    ? entryBinaryName.substring(0, entryBinaryName.indexOf('$'))
                    : entryBinaryName;
            js.append("import { ")
                    .append(entryIdentifier)
                    .append(slimeParserEntry ? " as __QinGeneratedSlimeParserBase" : "")
                    .append(" } from \"./")
                    .append(sourceBinaryName.replace('.', '/'))
                    .append(".")
                    .append(extension)
                    .append("\";\n");
            if (slimeParserEntry) {
                appendGeneratedSlimeParserAdapter(js);
                js.append("export { __QinGeneratedSlimeParserBase as ")
                        .append(entryIdentifier)
                        .append(" };\n");
                js.append("export default SlimeParser;\n");
            } else {
                js.append("export default ")
                        .append(entryIdentifier)
                        .append(";\n");
            }
        }
        if (!slimeParserEntry) {
            appendExportAlias(js, entryBinaryName, simpleClassName(entryBinaryName), extension, binaryNames);
        }
        appendExportAlias(js, "com.slime.parser.SlimeJavascriptParser", "SlimeJavascriptParser", extension, binaryNames);
        appendExportAlias(js, "com.slime.parser.consumer.SlimeTokenConsumer", "SlimeTokenConsumer", extension, binaryNames);
        appendExportAlias(js, "com.slime.parser.SlimeJavascriptParser$SourceType", "SlimeJavascriptParserSourceType", extension, binaryNames);
        appendExportAlias(js, "com.slime.parser.base.SlimeJavascriptParserBase$ExpressionParams", "ExpressionParams", extension, binaryNames);
        appendExportAlias(js, "com.slime.parser.base.SlimeJavascriptParserBase$StatementParams", "StatementParams", extension, binaryNames);
        appendExportAlias(js, "com.slime.parser.base.SlimeJavascriptParserBase$DeclarationParams", "DeclarationParams", extension, binaryNames);
        appendExportAlias(js, "com.slime.parser.base.SlimeJavascriptParserBase$TemplateLiteralParams", "TemplateLiteralParams", extension, binaryNames);

        if (binaryNames.contains("com.slime.token.JavaScriptTokens")) {
            String tokenIdentifier = QinJsBackend.generatedJavaClassIdentifier("com.slime.token.JavaScriptTokens");
            js.append("import { ")
                    .append(tokenIdentifier)
                    .append(" } from \"./com/slime/token/JavaScriptTokens.")
                    .append(extension)
                    .append("\";\n");
            js.append("export { ")
                    .append(tokenIdentifier)
                    .append(" as JavaScriptTokens };\n");
            js.append("const __qinSlimeTokens = ")
                    .append(tokenIdentifier)
                    .append(".getTokens();\n");
            if (typeScript) {
                js.append("const __qinSlimeTokenEntries: any[] = [];\n");
                js.append("for (const token of __qinSlimeTokens as any) {\n");
            } else {
                js.append("const __qinSlimeTokenEntries = [];\n");
                js.append("for (const token of __qinSlimeTokens) {\n");
            }
            js.append("  const tokenName = token.getName();\n");
            js.append("  __qinSlimeTokenEntries.push([tokenName, token]);\n");
            js.append("  if (!tokenName.endsWith(\"Tok\")) __qinSlimeTokenEntries.push([tokenName + \"Tok\", token]);\n");
            js.append("}\n");
            js.append("export const SlimeTokensObj = Object.fromEntries(__qinSlimeTokenEntries);\n");
            js.append("export const SlimeJavascriptTokensObj = SlimeTokensObj;\n");
            js.append("export const slimeTokens = Object.values(SlimeTokensObj);\n");
            if (typeScript) {
                js.append("const __qinSlimeTokenTypeEntries: any[] = [];\n");
            } else {
                js.append("const __qinSlimeTokenTypeEntries = [];\n");
            }
            js.append("for (const token of __qinSlimeTokens) {\n");
            js.append("  const tokenName = token.getName();\n");
            js.append("  __qinSlimeTokenTypeEntries.push([tokenName, tokenName]);\n");
            js.append("}\n");
            js.append("export const SlimeJavascriptContextualKeywordTokenTypes = Object.freeze({\n");
            js.append("  Async: \"async\",\n");
            js.append("  Static: \"static\",\n");
            js.append("  Let: \"let\",\n");
            js.append("  Get: \"get\",\n");
            js.append("  Set: \"set\",\n");
            js.append("  Of: \"of\",\n");
            js.append("  From: \"from\",\n");
            js.append("  As: \"as\",\n");
            js.append("  Target: \"target\",\n");
            js.append("  Meta: \"meta\"\n");
            js.append("});\n");
            js.append("export const SlimeContextualKeywordTokenTypes = SlimeJavascriptContextualKeywordTokenTypes;\n");
            js.append("export const SlimeTokenType = Object.freeze(Object.assign(Object.fromEntries(__qinSlimeTokenTypeEntries), SlimeJavascriptContextualKeywordTokenTypes));\n");
            if (typeScript) {
                js.append("function __qinReadTokenMember(token: any, methodName: string, fieldName: string): any {\n");
                js.append("  if (token == null) return null;\n");
                js.append("  const method = token[methodName];\n");
                js.append("  if (typeof method === \"function\") return method.call(token);\n");
                js.append("  return token[fieldName];\n");
                js.append("}\n");
                js.append("export const ReservedWords = new Set(slimeTokens.filter((token: any) => __qinReadTokenMember(token, \"isKeyword\", \"isKeyword\")).map((token: any) => __qinReadTokenMember(token, \"getValue\", \"value\")).filter(Boolean));\n");
            } else {
                js.append("function __qinReadTokenMember(token, methodName, fieldName) {\n");
                js.append("  if (token == null) return null;\n");
                js.append("  const method = token[methodName];\n");
                js.append("  if (typeof method === \"function\") return method.call(token);\n");
                js.append("  return token[fieldName];\n");
                js.append("}\n");
                js.append("export const ReservedWords = new Set(slimeTokens.filter((token) => __qinReadTokenMember(token, \"isKeyword\", \"isKeyword\")).map((token) => __qinReadTokenMember(token, \"getValue\", \"value\")).filter(Boolean));\n");
            }
        }
        if (typeScript) {
            if (!binaryNames.contains("com.slime.parser.base.SlimeJavascriptParserBase$ExpressionParams")) {
                js.append("export type ExpressionParams = any;\n");
            }
            if (!binaryNames.contains("com.slime.parser.base.SlimeJavascriptParserBase$StatementParams")) {
                js.append("export type StatementParams = any;\n");
            }
            if (!binaryNames.contains("com.slime.parser.base.SlimeJavascriptParserBase$DeclarationParams")) {
                js.append("export type DeclarationParams = any;\n");
            }
            if (!binaryNames.contains("com.slime.parser.base.SlimeJavascriptParserBase$TemplateLiteralParams")) {
                js.append("export type TemplateLiteralParams = any;\n");
            }
        }
        return js.toString();
    }

    private void appendGeneratedSlimeParserAdapter(StringBuilder js) {
        js.append("""
                function __qinToJsArray(value) {
                  if (value == null) return [];
                  if (Array.isArray(value)) return value;
                  if (typeof value.toArray === "function") return value.toArray();
                  if (typeof value[Symbol.iterator] === "function") return Array.from(value);
                  return [];
                }
                class SlimeParser extends __QinGeneratedSlimeParserBase {
                  constructor(...args) {
                    super(...args);
                    this.parsedTokens = [];
                    this.unparsedTokens = [];
                    this.tokenConsumer = super.getTokenConsumer();
                    this.__qinSyncJsParserProperties();
                  }
                  __qinSyncJsParserProperties() {
                    this.parsedTokens = __qinToJsArray(super.parsedTokens());
                    this.unparsedTokens = __qinToJsArray(super.unparsedTokens());
                    this.tokenConsumer = super.getTokenConsumer();
                    return null;
                  }
                  Program(...args) {
                    const result = super.Program(...args);
                    this.__qinSyncJsParserProperties();
                    return result;
                  }
                  parse(...args) {
                    const result = super.parse(...args);
                    this.__qinSyncJsParserProperties();
                    return result;
                  }
                }
                export { SlimeParser };
                """);
    }

    private void appendExportAlias(
            StringBuilder js,
            String binaryName,
            String alias,
            String extension,
            Set<String> binaryNames) {
        if (!binaryNames.contains(binaryName)) {
            return;
        }
        String identifier = QinJsBackend.generatedJavaClassIdentifier(binaryName);
        String sourceBinaryName = binaryName.contains("$")
                ? binaryName.substring(0, binaryName.indexOf('$'))
                : binaryName;
        js.append("export { ")
                .append(identifier)
                .append(", ")
                .append(identifier)
                .append(" as ")
                .append(alias)
                .append(" } from \"./")
                .append(sourceBinaryName.replace('.', '/'))
                .append(".")
                .append(extension)
                .append("\";\n");
    }

    private Map<String, JavaAstProgram> parseSourceFiles(Map<String, Path> sourceFiles) {
        Map<String, JavaAstProgram> parsedPrograms = new LinkedHashMap<>();
        for (Map.Entry<String, Path> sourceEntry : sourceFiles.entrySet()) {
            try {
                parsedPrograms.put(
                        sourceEntry.getKey(),
                        JavaCstToAst.parse(Files.readString(sourceEntry.getValue(), StandardCharsets.UTF_8)));
            } catch (IOException e) {
                throw new IllegalArgumentException("Could not read Java source file: " + sourceEntry.getValue(), e);
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Could not parse Java source file: " + sourceEntry.getValue(), e);
            }
        }
        return parsedPrograms;
    }

    private QinIrProgram lowerBundle(Iterable<JavaAstProgram> programs) {
        List<JavaAstProgram> programList = new ArrayList<>();
        for (JavaAstProgram program : programs) {
            programList.add(program);
        }
        return new QinJavaAstIrLowerer().lowerPrograms(programList);
    }

    private QinIrProgram sortJavaClassDeclarations(QinIrProgram program) {
        List<com.qin.lang.ir.QinIrClassDeclaration> sortedClasses =
                sortJavaClassDeclarations(program.classDeclarations());
        if (sortedClasses.equals(program.classDeclarations())) {
            return program;
        }
        return new QinIrProgram(
                program.declarations(),
                program.expressionStatements(),
                program.consoleValueLogs(),
                program.consoleLogs(),
                program.javaImports(),
                program.jsImports(),
                program.javaStaticConsoleLogs(),
                program.javaInstanceMethodCalls(),
                program.javaInstanceConsoleLogs(),
                sortedClasses,
                program.executionSteps(),
                program.functionModelArtifacts());
    }

    private List<com.qin.lang.ir.QinIrClassDeclaration> sortJavaClassDeclarations(
            List<com.qin.lang.ir.QinIrClassDeclaration> classDeclarations) {
        Map<String, com.qin.lang.ir.QinIrClassDeclaration> byBinaryName = new LinkedHashMap<>();
        for (com.qin.lang.ir.QinIrClassDeclaration classDeclaration : classDeclarations) {
            byBinaryName.put(classDeclaration.binaryName(), classDeclaration);
        }
        List<com.qin.lang.ir.QinIrClassDeclaration> sorted = new ArrayList<>();
        Set<String> visiting = new LinkedHashSet<>();
        Set<String> visited = new LinkedHashSet<>();
        for (com.qin.lang.ir.QinIrClassDeclaration classDeclaration : classDeclarations) {
            visitJavaClassDeclaration(classDeclaration, byBinaryName, visiting, visited, sorted);
        }
        return List.copyOf(sorted);
    }

    private void visitJavaClassDeclaration(
            com.qin.lang.ir.QinIrClassDeclaration classDeclaration,
            Map<String, com.qin.lang.ir.QinIrClassDeclaration> byBinaryName,
            Set<String> visiting,
            Set<String> visited,
            List<com.qin.lang.ir.QinIrClassDeclaration> sorted) {
        String binaryName = classDeclaration.binaryName();
        if (visited.contains(binaryName)) {
            return;
        }
        if (!visiting.add(binaryName)) {
            throw new IllegalArgumentException("Cyclic Java class inheritance in generated JS bundle: " + visiting);
        }
        if (classDeclaration.superType() != null) {
            com.qin.lang.ir.QinIrClassDeclaration superClass =
                    byBinaryName.get(classDeclaration.superType().binaryName());
            if (superClass != null) {
                visitJavaClassDeclaration(superClass, byBinaryName, visiting, visited, sorted);
            }
        }
        visiting.remove(binaryName);
        visited.add(binaryName);
        sorted.add(classDeclaration);
    }

    private String projectExports(QinIrProgram program, boolean typeScript) {
        if (program.classDeclarations().isEmpty()) {
            return "";
        }
        StringBuilder js = new StringBuilder();
        js.append("\nif (typeof globalThis !== 'undefined') {\n");
        if (typeScript) {
            js.append("  const __qinGlobal = globalThis as any;\n");
            js.append("  __qinGlobal.__qinJavaProjectExports = __qinGlobal.__qinJavaProjectExports || {};\n");
        } else {
            js.append("  globalThis.__qinJavaProjectExports = globalThis.__qinJavaProjectExports || {};\n");
        }
        for (var classDeclaration : program.classDeclarations()) {
            js.append(typeScript ? "  __qinGlobal.__qinJavaProjectExports[\"" : "  globalThis.__qinJavaProjectExports[\"")
                    .append(escapeJs(classDeclaration.binaryName()))
                    .append("\"] = ")
                    .append(QinJsBackend.generatedJavaClassIdentifier(classDeclaration.binaryName()))
                    .append(";\n");
        }
        js.append("}\n");
        return js.toString();
    }

    private QinIrProgram programWithClasses(QinIrProgram program, Set<String> classBinaryNames) {
        List<com.qin.lang.ir.QinIrClassDeclaration> classes = new ArrayList<>();
        for (var classDeclaration : program.classDeclarations()) {
            if (classBinaryNames.contains(classDeclaration.binaryName())) {
                classes.add(classDeclaration);
            }
        }
        return new QinIrProgram(
                program.declarations(),
                program.expressionStatements(),
                program.consoleValueLogs(),
                program.consoleLogs(),
                program.javaImports(),
                program.jsImports(),
                program.javaStaticConsoleLogs(),
                program.javaInstanceMethodCalls(),
                program.javaInstanceConsoleLogs(),
                sortJavaClassDeclarations(classes),
                program.executionSteps(),
                program.functionModelArtifacts());
    }

    private String esmImports(
            String sourceBinaryName,
            Path outputFile,
            Set<String> dependencyBinaryNames,
            Map<String, Path> outputFilesByBinaryName,
            Map<String, Set<String>> programClassBinaryNames) {
        StringBuilder js = new StringBuilder();
        Set<String> importedClassBinaryNames = new LinkedHashSet<>();
        for (String dependencyBinaryName : dependencyBinaryNames) {
            Path dependencyOutputFile = outputFilesByBinaryName.get(dependencyBinaryName);
            if (dependencyOutputFile == null || outputFile.equals(dependencyOutputFile)) {
                continue;
            }
            Set<String> dependencyClassBinaryNames = programClassBinaryNames.get(dependencyBinaryName);
            if (dependencyClassBinaryNames == null || dependencyClassBinaryNames.isEmpty()) {
                continue;
            }
            List<String> identifiers = new ArrayList<>();
            for (String classBinaryName : dependencyClassBinaryNames) {
                if (importedClassBinaryNames.add(classBinaryName)) {
                    String generatedIdentifier = QinJsBackend.generatedJavaClassIdentifier(classBinaryName);
                    String simpleName = nestedSimpleClassName(classBinaryName);
                    if (isJsIdentifier(simpleName)
                            && uniqueSimpleClassName(simpleName, sourceBinaryName, programClassBinaryNames)
                            && !simpleName.equals(generatedIdentifier)) {
                        identifiers.add(generatedIdentifier);
                        identifiers.add(generatedIdentifier + " as " + simpleName);
                    } else {
                        identifiers.add(generatedIdentifier);
                    }
                }
            }
            if (identifiers.isEmpty()) {
                continue;
            }
            js.append("import { ")
                    .append(String.join(", ", identifiers))
                    .append(" } from \"")
                    .append(relativeModuleSpecifier(outputFile, dependencyOutputFile))
                    .append("\";\n");
        }
        if (js.length() > 0) {
            js.append('\n');
        }
        return js.toString();
    }

    private boolean uniqueSimpleClassName(
            String simpleName,
            String sourceBinaryName,
            Map<String, Set<String>> programClassBinaryNames) {
        int count = 0;
        for (Map.Entry<String, Set<String>> entry : programClassBinaryNames.entrySet()) {
            if (entry.getKey().equals(sourceBinaryName)) {
                continue;
            }
            for (String classBinaryName : entry.getValue()) {
                if (simpleName.equals(nestedSimpleClassName(classBinaryName))) {
                    count++;
                    if (count > 1) {
                        return false;
                    }
                }
            }
        }
        return count == 1;
    }

    private String nestedSimpleClassName(String binaryName) {
        String simpleName = simpleClassName(binaryName);
        int nestedSeparator = simpleName.lastIndexOf('$');
        return nestedSeparator < 0 ? simpleName : simpleName.substring(nestedSeparator + 1);
    }

    private String simpleClassName(String binaryName) {
        int dot = binaryName.lastIndexOf('.');
        return dot < 0 ? binaryName : binaryName.substring(dot + 1);
    }

    private boolean isJsIdentifier(String value) {
        return value != null && value.matches("[A-Za-z_$][A-Za-z0-9_$]*");
    }

    private Set<String> externallyBoundClassBinaryNames(
            String sourceBinaryName,
            Path outputFile,
            Set<String> dependencyBinaryNames,
            Map<String, Path> outputFilesByBinaryName,
            Map<String, Set<String>> programClassBinaryNames) {
        Set<String> classBinaryNames = new LinkedHashSet<>();
        for (String dependencyBinaryName : dependencyBinaryNames) {
            Path dependencyOutputFile = outputFilesByBinaryName.get(dependencyBinaryName);
            if (dependencyOutputFile == null || outputFile.equals(dependencyOutputFile)) {
                continue;
            }
            Set<String> dependencyClassBinaryNames = programClassBinaryNames.get(dependencyBinaryName);
            if (dependencyClassBinaryNames != null) {
                classBinaryNames.addAll(dependencyClassBinaryNames);
            }
        }
        return classBinaryNames;
    }

    private String esmExports(QinIrProgram program) {
        if (program.classDeclarations().isEmpty()) {
            return "";
        }
        List<String> identifiers = new ArrayList<>();
        for (var classDeclaration : program.classDeclarations()) {
            identifiers.add(QinJsBackend.generatedJavaClassIdentifier(classDeclaration.binaryName()));
        }
        return "\nexport { " + String.join(", ", identifiers) + " };\n";
    }

    private Path outputFileForSource(List<Path> sourceRoots, Path sourceFile, Path outputRoot) {
        return outputFileForSource(sourceRoots, sourceFile, outputRoot, "js");
    }

    private Path outputFileForSource(List<Path> sourceRoots, Path sourceFile, Path outputRoot, String extension) {
        Path normalizedSourceFile = sourceFile.toAbsolutePath().normalize();
        for (Path sourceRoot : sourceRoots) {
            Path normalizedSourceRoot = sourceRoot.toAbsolutePath().normalize();
            if (normalizedSourceFile.startsWith(normalizedSourceRoot)) {
                Path relative = normalizedSourceRoot.relativize(normalizedSourceFile);
                String relativeText = relative.toString();
                relativeText = relativeText.substring(0, relativeText.length() - ".java".length())
                        + "."
                        + extension;
                return outputRoot.resolve(relativeText).normalize();
            }
        }
        throw new IllegalArgumentException("Source file is not under any source root: " + sourceFile);
    }

    private String relativeModuleSpecifier(Path fromOutputFile, Path toOutputFile) {
        Path fromDir = fromOutputFile.toAbsolutePath().normalize().getParent();
        Path toFile = toOutputFile.toAbsolutePath().normalize();
        String specifier = fromDir.relativize(toFile).toString().replace('\\', '/');
        if (!specifier.startsWith(".")) {
            specifier = "./" + specifier;
        }
        return specifier;
    }

    private Set<String> programClassBinaryNames(JavaAstProgram program) {
        Set<String> binaryNames = new LinkedHashSet<>();
        for (JavaAstClassDeclaration classDeclaration : program.classes()) {
            collectProgramClassBinaryNames(program.packageName(), null, classDeclaration, binaryNames);
        }
        return binaryNames;
    }

    private void collectProgramClassBinaryNames(
            String packageName,
            String ownerSimpleName,
            JavaAstClassDeclaration classDeclaration,
            Set<String> binaryNames) {
        String simpleName = ownerSimpleName == null
                ? classDeclaration.name()
                : ownerSimpleName + "$" + classDeclaration.name();
        binaryNames.add(binaryName(packageName, simpleName));
        for (JavaAstClassDeclaration nestedClass : classDeclaration.nestedClasses()) {
            collectProgramClassBinaryNames(packageName, simpleName, nestedClass, binaryNames);
        }
    }

    private String binaryName(String packageName, String simpleName) {
        return packageName == null || packageName.isBlank()
                ? simpleName
                : packageName + "." + simpleName;
    }

    private String escapeJs(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    public record EsmFileOutput(
            String binaryName,
            Path sourceFile,
            Path outputFile,
            String js) {
        public String code() {
            return js;
        }
    }

    private record JavaSdkSource(
            String packageName,
            Path sourceRoot,
            Path sourceFile,
            String source,
            boolean fromSiblingProject) {
    }

    public Map<String, Path> superclassSourceFiles(List<Path> sourceRoots, String entryBinaryName) {
        return sourceDependencyFiles(sourceRoots, entryBinaryName);
    }

    public Map<String, Path> sourceDependencyFiles(List<Path> sourceRoots, String entryBinaryName) {
        return sourceDependencyFiles(sourceRoots, List.of(entryBinaryName));
    }

    public Map<String, Path> sourceDependencyFiles(List<Path> sourceRoots, List<String> rootBinaryNames) {
        Map<String, Path> sourceIndex = sourceIndex(sourceRoots);
        Map<String, Path> sourceFiles = new LinkedHashMap<>();
        Map<String, JavaAstProgram> parsedPrograms = new HashMap<>();
        LinkedHashSet<String> pending = new LinkedHashSet<>();
        for (String rootBinaryName : rootBinaryNames) {
            pending.addAll(superclassBinaryNames(rootBinaryName));
            pending.add(rootBinaryName);
        }

        while (!pending.isEmpty()) {
            String binaryName = pending.removeFirst();
            if (sourceFiles.containsKey(binaryName)) {
                continue;
            }
            Path sourceFile = sourceIndex.get(binaryName);
            if (sourceFile == null) {
                continue;
            }
            JavaAstProgram program = parsedPrograms.computeIfAbsent(binaryName, ignored -> parseSource(sourceFile));
            sourceFiles.put(binaryName, sourceFile);

            for (String dependencyBinaryName : referencedSourceBinaryNames(program, sourceIndex.keySet())) {
                if (!sourceFiles.containsKey(dependencyBinaryName)) {
                    pending.add(dependencyBinaryName);
                }
            }
        }
        return sourceFiles;
    }

    private List<String> superclassBinaryNames(String entryBinaryName) {
        List<String> binaryNames = new ArrayList<>();
        Class<?> current = loadClass(entryBinaryName);
        while (current != null && current != Object.class) {
            binaryNames.add(current.getName());
            current = current.getSuperclass();
        }
        Collections.reverse(binaryNames);
        return binaryNames;
    }

    private Map<String, Path> sourceIndex(List<Path> sourceRoots) {
        Map<String, Path> sourceIndex = new LinkedHashMap<>();
        for (Path sourceRoot : sourceRoots) {
            if (!Files.isDirectory(sourceRoot)) {
                continue;
            }
            try (Stream<Path> stream = Files.walk(sourceRoot)) {
                stream.filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().endsWith(".java"))
                        .forEach(path -> {
                            String binaryName = sourceRoot.relativize(path).toString()
                                    .replace('\\', '.')
                                    .replace('/', '.');
                            binaryName = binaryName.substring(0, binaryName.length() - ".java".length());
                            sourceIndex.putIfAbsent(binaryName, path);
                        });
            } catch (IOException e) {
                throw new IllegalArgumentException("Could not scan Java source root: " + sourceRoot, e);
            }
        }
        return sourceIndex;
    }

    private JavaAstProgram parseSource(Path sourceFile) {
        try {
            return JavaCstToAst.parse(Files.readString(sourceFile, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read Java source file: " + sourceFile, e);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Could not parse Java source file: " + sourceFile, e);
        }
    }

    private Set<String> referencedSourceBinaryNames(JavaAstProgram program, Set<String> sourceBinaryNames) {
        TypeResolver resolver = new TypeResolver(program, sourceBinaryNames);
        Set<String> referenced = new LinkedHashSet<>();
        for (JavaAstImportDeclaration importDeclaration : program.imports()) {
            if (!importDeclaration.staticImport() && !importDeclaration.onDemand()) {
                addIfSource(referenced, sourceBinaryNames, importDeclaration.name());
            }
        }
        for (JavaAstClassDeclaration classDeclaration : program.classes()) {
            collectClassReferences(classDeclaration, resolver, sourceBinaryNames, referenced);
        }
        return referenced;
    }

    private void collectClassReferences(
            JavaAstClassDeclaration classDeclaration,
            TypeResolver resolver,
            Set<String> sourceBinaryNames,
            Set<String> referenced) {
        collectTypeReference(classDeclaration.superTypeName(), resolver, sourceBinaryNames, referenced);
        for (JavaAstFieldDeclaration field : classDeclaration.fields()) {
            collectTypeReference(field.typeName(), resolver, sourceBinaryNames, referenced);
            collectExpressionReferences(field.initializer(), resolver, sourceBinaryNames, referenced);
        }
        for (JavaAstMethodDeclaration method : classDeclaration.methods()) {
            collectTypeReference(method.returnTypeName(), resolver, sourceBinaryNames, referenced);
            for (JavaAstParameter parameter : method.parameters()) {
                collectTypeReference(parameter.typeName(), resolver, sourceBinaryNames, referenced);
            }
            for (JavaAstStatement statement : method.bodyStatements()) {
                collectStatementReferences(statement, resolver, sourceBinaryNames, referenced);
            }
            collectExpressionReferences(method.returnExpression(), resolver, sourceBinaryNames, referenced);
        }
        for (JavaAstClassDeclaration nestedClass : classDeclaration.nestedClasses()) {
            collectClassReferences(nestedClass, resolver, sourceBinaryNames, referenced);
        }
    }

    private void collectStatementReferences(
            JavaAstStatement statement,
            TypeResolver resolver,
            Set<String> sourceBinaryNames,
            Set<String> referenced) {
        if (statement == null) {
            return;
        }
        switch (statement) {
            case JavaAstDoWhileStatement doWhileStatement -> {
                for (JavaAstStatement bodyStatement : doWhileStatement.bodyStatements()) {
                    collectStatementReferences(bodyStatement, resolver, sourceBinaryNames, referenced);
                }
                collectExpressionReferences(doWhileStatement.test(), resolver, sourceBinaryNames, referenced);
            }
            case JavaAstBreakStatement ignored -> {
            }
            case JavaAstContinueStatement ignored -> {
            }
            case JavaAstEnhancedForStatement enhancedForStatement -> {
                collectTypeReference(enhancedForStatement.variableTypeName(), resolver, sourceBinaryNames, referenced);
                collectExpressionReferences(enhancedForStatement.iterableExpression(), resolver, sourceBinaryNames, referenced);
                for (JavaAstStatement bodyStatement : enhancedForStatement.bodyStatements()) {
                    collectStatementReferences(bodyStatement, resolver, sourceBinaryNames, referenced);
                }
            }
            case JavaAstExpressionStatement expressionStatement ->
                    collectExpressionReferences(expressionStatement.expression(), resolver, sourceBinaryNames, referenced);
            case JavaAstForStatement forStatement -> {
                for (JavaAstStatement initializerStatement : forStatement.initializerStatements()) {
                    collectStatementReferences(initializerStatement, resolver, sourceBinaryNames, referenced);
                }
                collectExpressionReferences(forStatement.test(), resolver, sourceBinaryNames, referenced);
                for (JavaAstExpression updateExpression : forStatement.updateExpressions()) {
                    collectExpressionReferences(updateExpression, resolver, sourceBinaryNames, referenced);
                }
                for (JavaAstStatement bodyStatement : forStatement.bodyStatements()) {
                    collectStatementReferences(bodyStatement, resolver, sourceBinaryNames, referenced);
                }
            }
            case JavaAstIfStatement ifStatement -> {
                collectExpressionReferences(ifStatement.test(), resolver, sourceBinaryNames, referenced);
                for (JavaAstStatement consequentStatement : ifStatement.consequentStatements()) {
                    collectStatementReferences(consequentStatement, resolver, sourceBinaryNames, referenced);
                }
                for (JavaAstStatement alternateStatement : ifStatement.alternateStatements()) {
                    collectStatementReferences(alternateStatement, resolver, sourceBinaryNames, referenced);
                }
            }
            case JavaAstLocalVariableDeclaration localVariableDeclaration -> {
                collectTypeReference(localVariableDeclaration.typeName(), resolver, sourceBinaryNames, referenced);
                collectExpressionReferences(localVariableDeclaration.initializer(), resolver, sourceBinaryNames, referenced);
            }
            case JavaAstReturnStatement returnStatement ->
                    collectExpressionReferences(returnStatement.expression(), resolver, sourceBinaryNames, referenced);
            case JavaAstThrowStatement throwStatement ->
                    collectExpressionReferences(throwStatement.expression(), resolver, sourceBinaryNames, referenced);
            case JavaAstYieldStatement yieldStatement ->
                    collectExpressionReferences(yieldStatement.expression(), resolver, sourceBinaryNames, referenced);
            case JavaAstTryStatement tryStatement -> {
                for (JavaAstStatement tryBodyStatement : tryStatement.tryStatements()) {
                    collectStatementReferences(tryBodyStatement, resolver, sourceBinaryNames, referenced);
                }
                for (JavaAstCatchClause catchClause : tryStatement.catchClauses()) {
                    collectTypeReference(catchClause.parameterTypeName(), resolver, sourceBinaryNames, referenced);
                    for (JavaAstStatement catchBodyStatement : catchClause.bodyStatements()) {
                        collectStatementReferences(catchBodyStatement, resolver, sourceBinaryNames, referenced);
                    }
                }
                for (JavaAstStatement finallyBodyStatement : tryStatement.finallyStatements()) {
                    collectStatementReferences(finallyBodyStatement, resolver, sourceBinaryNames, referenced);
                }
            }
            case JavaAstSwitchStatement switchStatement -> {
                collectExpressionReferences(switchStatement.discriminant(), resolver, sourceBinaryNames, referenced);
                for (JavaAstSwitchCase switchCase : switchStatement.cases()) {
                    collectExpressionReferences(switchCase.test(), resolver, sourceBinaryNames, referenced);
                    for (JavaAstStatement caseStatement : switchCase.statements()) {
                        collectStatementReferences(caseStatement, resolver, sourceBinaryNames, referenced);
                    }
                }
            }
            case JavaAstWhileStatement whileStatement -> {
                collectExpressionReferences(whileStatement.test(), resolver, sourceBinaryNames, referenced);
                for (JavaAstStatement bodyStatement : whileStatement.bodyStatements()) {
                    collectStatementReferences(bodyStatement, resolver, sourceBinaryNames, referenced);
                }
            }
        }
    }

    private void collectExpressionReferences(
            JavaAstExpression expression,
            TypeResolver resolver,
            Set<String> sourceBinaryNames,
            Set<String> referenced) {
        if (expression == null) {
            return;
        }
        switch (expression) {
            case JavaAstArrayAccessExpression arrayAccessExpression -> {
                collectExpressionReferences(arrayAccessExpression.receiver(), resolver, sourceBinaryNames, referenced);
                collectExpressionReferences(arrayAccessExpression.index(), resolver, sourceBinaryNames, referenced);
            }
            case JavaAstArrayLiteralExpression arrayLiteralExpression -> {
                collectTypeReference(arrayLiteralExpression.typeName(), resolver, sourceBinaryNames, referenced);
                for (JavaAstExpression element : arrayLiteralExpression.elements()) {
                    collectExpressionReferences(element, resolver, sourceBinaryNames, referenced);
                }
            }
            case JavaAstAssignmentExpression assignmentExpression -> {
                collectExpressionReferences(assignmentExpression.target(), resolver, sourceBinaryNames, referenced);
                collectExpressionReferences(assignmentExpression.value(), resolver, sourceBinaryNames, referenced);
            }
            case JavaAstBinaryExpression binaryExpression -> {
                collectExpressionReferences(binaryExpression.left(), resolver, sourceBinaryNames, referenced);
                collectExpressionReferences(binaryExpression.right(), resolver, sourceBinaryNames, referenced);
            }
            case JavaAstCastExpression castExpression -> {
                collectTypeReference(castExpression.typeName(), resolver, sourceBinaryNames, referenced);
                collectExpressionReferences(castExpression.expression(), resolver, sourceBinaryNames, referenced);
            }
            case JavaAstClassLiteralExpression classLiteralExpression ->
                    collectTypeReference(classLiteralExpression.typeName(), resolver, sourceBinaryNames, referenced);
            case JavaAstIdentifierExpression identifierExpression ->
                    collectTypeReference(identifierExpression.name(), resolver, sourceBinaryNames, referenced);
            case JavaAstInstanceofPatternExpression instanceofPatternExpression -> {
                collectExpressionReferences(instanceofPatternExpression.value(), resolver, sourceBinaryNames, referenced);
                collectTypeReference(instanceofPatternExpression.typeName(), resolver, sourceBinaryNames, referenced);
            }
            case JavaAstInstanceofExpression instanceofExpression -> {
                collectExpressionReferences(instanceofExpression.value(), resolver, sourceBinaryNames, referenced);
                collectTypeReference(instanceofExpression.typeName(), resolver, sourceBinaryNames, referenced);
            }
            case JavaAstLambdaExpression lambdaExpression -> {
                collectExpressionReferences(lambdaExpression.bodyExpression(), resolver, sourceBinaryNames, referenced);
                for (JavaAstStatement bodyStatement : lambdaExpression.bodyStatements()) {
                    collectStatementReferences(bodyStatement, resolver, sourceBinaryNames, referenced);
                }
            }
            case JavaAstSwitchExpression switchExpression -> {
                collectExpressionReferences(switchExpression.discriminant(), resolver, sourceBinaryNames, referenced);
                for (JavaAstSwitchCase switchCase : switchExpression.cases()) {
                    collectExpressionReferences(switchCase.test(), resolver, sourceBinaryNames, referenced);
                    for (JavaAstStatement caseStatement : switchCase.statements()) {
                        collectStatementReferences(caseStatement, resolver, sourceBinaryNames, referenced);
                    }
                }
            }
            case JavaAstMemberAccessExpression memberAccessExpression ->
                    collectReceiverReferences(memberAccessExpression.receiver(), resolver, sourceBinaryNames, referenced);
            case JavaAstMethodCallExpression methodCallExpression -> {
                collectReceiverReferences(methodCallExpression.receiver(), resolver, sourceBinaryNames, referenced);
                for (JavaAstExpression argument : methodCallExpression.arguments()) {
                    collectExpressionReferences(argument, resolver, sourceBinaryNames, referenced);
                }
            }
            case JavaAstMethodReferenceExpression methodReferenceExpression ->
                    collectTypeReference(methodReferenceExpression.ownerName(), resolver, sourceBinaryNames, referenced);
            case JavaAstNewExpression newExpression -> {
                collectTypeReference(newExpression.typeName(), resolver, sourceBinaryNames, referenced);
                for (JavaAstExpression argument : newExpression.arguments()) {
                    collectExpressionReferences(argument, resolver, sourceBinaryNames, referenced);
                }
            }
            case JavaAstUnaryExpression unaryExpression ->
                    collectExpressionReferences(unaryExpression.operand(), resolver, sourceBinaryNames, referenced);
            case JavaAstUpdateExpression updateExpression ->
                    collectExpressionReferences(updateExpression.target(), resolver, sourceBinaryNames, referenced);
            default -> {
            }
        }
    }

    private void collectReceiverReferences(
            JavaAstExpression receiver,
            TypeResolver resolver,
            Set<String> sourceBinaryNames,
            Set<String> referenced) {
        String qualifiedName = qualifiedName(receiver);
        if (qualifiedName != null) {
            collectTypeReference(qualifiedName, resolver, sourceBinaryNames, referenced);
        }
        collectExpressionReferences(receiver, resolver, sourceBinaryNames, referenced);
    }

    private String qualifiedName(JavaAstExpression expression) {
        if (expression instanceof JavaAstIdentifierExpression identifierExpression) {
            return identifierExpression.name();
        }
        if (expression instanceof JavaAstMemberAccessExpression memberAccessExpression) {
            String receiverName = qualifiedName(memberAccessExpression.receiver());
            return receiverName == null
                    ? null
                    : receiverName + "." + memberAccessExpression.propertyName();
        }
        return null;
    }

    private void collectTypeReference(
            String typeName,
            TypeResolver resolver,
            Set<String> sourceBinaryNames,
            Set<String> referenced) {
        String resolved = resolver.resolve(typeName);
        addIfSource(referenced, sourceBinaryNames, resolved);
    }

    private void addIfSource(Set<String> referenced, Set<String> sourceBinaryNames, String binaryName) {
        if (binaryName != null && sourceBinaryNames.contains(binaryName)) {
            referenced.add(binaryName);
        }
    }

    private Class<?> loadClass(String binaryName) {
        try {
            return Class.forName(binaryName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown Java entry class: " + binaryName, e);
        }
    }

    private static final class TypeResolver {
        private static final Set<String> PRIMITIVE_TYPES = Set.of(
                "boolean", "byte", "char", "short", "int", "long", "float", "double", "void");

        private final String packageName;
        private final Set<String> sourceBinaryNames;
        private final Map<String, String> explicitImports = new HashMap<>();
        private final Set<String> wildcardImports = new HashSet<>();

        private TypeResolver(JavaAstProgram program, Set<String> sourceBinaryNames) {
            this.packageName = program.packageName() == null ? "" : program.packageName();
            this.sourceBinaryNames = sourceBinaryNames;
            for (JavaAstImportDeclaration importDeclaration : program.imports()) {
                if (importDeclaration.staticImport()) {
                    continue;
                }
                if (importDeclaration.onDemand()) {
                    wildcardImports.add(importDeclaration.name());
                    continue;
                }
                String importName = importDeclaration.name();
                explicitImports.put(simpleName(importName), importName);
            }
        }

        private String resolve(String rawTypeName) {
            if (rawTypeName == null || rawTypeName.isBlank()) {
                return null;
            }
            String typeName = eraseType(rawTypeName);
            if (typeName.isBlank() || PRIMITIVE_TYPES.contains(typeName)) {
                return null;
            }
            if (typeName.contains(".")) {
                return resolveQualifiedTypeName(typeName);
            }
            String imported = explicitImports.get(typeName);
            if (imported != null) {
                return imported;
            }
            String samePackage = packageName.isBlank() ? typeName : packageName + "." + typeName;
            if (sourceBinaryNames.contains(samePackage)) {
                return samePackage;
            }
            for (String wildcardImport : wildcardImports) {
                String candidate = wildcardImport + "." + typeName;
                if (sourceBinaryNames.contains(candidate)) {
                    return candidate;
                }
            }
            return null;
        }

        private String resolveQualifiedTypeName(String typeName) {
            String candidate = typeName;
            while (!candidate.isBlank()) {
                if (sourceBinaryNames.contains(candidate)) {
                    return candidate;
                }
                int dot = candidate.lastIndexOf('.');
                if (dot < 0) {
                    break;
                }
                candidate = candidate.substring(0, dot);
            }

            int dot = typeName.indexOf('.');
            String rootName = dot < 0 ? typeName : typeName.substring(0, dot);
            String imported = explicitImports.get(rootName);
            if (imported != null) {
                String importedCandidate = imported + typeName.substring(rootName.length());
                while (!importedCandidate.isBlank()) {
                    if (sourceBinaryNames.contains(importedCandidate)) {
                        return importedCandidate;
                    }
                    int importedDot = importedCandidate.lastIndexOf('.');
                    if (importedDot < imported.length()) {
                        break;
                    }
                    importedCandidate = importedCandidate.substring(0, importedDot);
                }
            }
            return null;
        }

        private String eraseType(String rawTypeName) {
            String typeName = rawTypeName.strip();
            while (typeName.endsWith("[]")) {
                typeName = typeName.substring(0, typeName.length() - 2).strip();
            }
            int genericStart = typeName.indexOf('<');
            if (genericStart >= 0) {
                typeName = typeName.substring(0, genericStart).strip();
            }
            if (typeName.startsWith("? extends ")) {
                typeName = typeName.substring("? extends ".length()).strip();
            } else if (typeName.startsWith("? super ")) {
                typeName = typeName.substring("? super ".length()).strip();
            }
            return typeName;
        }

        private String simpleName(String binaryName) {
            int dot = binaryName.lastIndexOf('.');
            return dot >= 0 ? binaryName.substring(dot + 1) : binaryName;
        }
    }
}
