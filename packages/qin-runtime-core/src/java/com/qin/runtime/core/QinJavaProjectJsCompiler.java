package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;
import com.slime.java.ast.JavaAstArrayAccessExpression;
import com.slime.java.ast.JavaAstArrayLiteralExpression;
import com.slime.java.ast.JavaAstAssignmentExpression;
import com.slime.java.ast.JavaAstBinaryExpression;
import com.slime.java.ast.JavaAstCastExpression;
import com.slime.java.ast.JavaAstClassDeclaration;
import com.slime.java.ast.JavaAstClassLiteralExpression;
import com.slime.java.ast.JavaAstDoWhileStatement;
import com.slime.java.ast.JavaAstEnhancedForStatement;
import com.slime.java.ast.JavaAstExpression;
import com.slime.java.ast.JavaAstExpressionStatement;
import com.slime.java.ast.JavaAstFieldDeclaration;
import com.slime.java.ast.JavaAstForStatement;
import com.slime.java.ast.JavaAstIdentifierExpression;
import com.slime.java.ast.JavaAstIfStatement;
import com.slime.java.ast.JavaAstImportDeclaration;
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
import com.slime.java.ast.JavaAstUnaryExpression;
import com.slime.java.ast.JavaAstUpdateExpression;
import com.slime.java.ast.JavaAstWhileStatement;
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
import java.util.stream.Stream;

public final class QinJavaProjectJsCompiler {
    public String compileSuperclassClosure(
            List<Path> sourceRoots,
            String entryBinaryName,
            Path outputFile) throws IOException {
        Map<String, Path> sourceFiles = sourceDependencyFiles(sourceRoots, entryBinaryName);
        List<JavaAstProgram> parsedPrograms = new ArrayList<>();
        for (Path sourceFile : sourceFiles.values()) {
            try {
                parsedPrograms.add(JavaCstToAst.parse(Files.readString(sourceFile, StandardCharsets.UTF_8)));
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Could not parse Java source file: " + sourceFile, e);
            }
        }

        QinIrProgram bundleProgram;
        try {
            bundleProgram = lowerBundle(parsedPrograms);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Could not lower Java source bundle for " + entryBinaryName, e);
        }
        String generated = new QinJsBackend().compileProgram(bundleProgram)
                + projectExports(bundleProgram);
        Files.createDirectories(outputFile.getParent());
        Files.writeString(outputFile, generated, StandardCharsets.UTF_8);
        return generated;
    }

    private QinIrProgram lowerBundle(List<JavaAstProgram> programs) {
        return new QinJavaAstIrLowerer().lowerPrograms(programs);
    }

    private String projectExports(QinIrProgram program) {
        if (program.classDeclarations().isEmpty()) {
            return "";
        }
        StringBuilder js = new StringBuilder();
        js.append("\nif (typeof globalThis !== 'undefined') {\n");
        js.append("  globalThis.__qinJavaProjectExports = globalThis.__qinJavaProjectExports || {};\n");
        for (var classDeclaration : program.classDeclarations()) {
            js.append("  globalThis.__qinJavaProjectExports[\"")
                    .append(escapeJs(classDeclaration.binaryName()))
                    .append("\"] = ")
                    .append(classDeclaration.simpleName())
                    .append(";\n");
        }
        js.append("}\n");
        return js.toString();
    }

    private String escapeJs(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    public Map<String, Path> superclassSourceFiles(List<Path> sourceRoots, String entryBinaryName) {
        return sourceDependencyFiles(sourceRoots, entryBinaryName);
    }

    public Map<String, Path> sourceDependencyFiles(List<Path> sourceRoots, String entryBinaryName) {
        Map<String, Path> sourceIndex = sourceIndex(sourceRoots);
        Map<String, Path> sourceFiles = new LinkedHashMap<>();
        Map<String, JavaAstProgram> parsedPrograms = new HashMap<>();
        LinkedHashSet<String> pending = new LinkedHashSet<>(superclassBinaryNames(entryBinaryName));
        pending.add(entryBinaryName);

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
            case JavaAstLambdaExpression lambdaExpression -> {
                collectExpressionReferences(lambdaExpression.bodyExpression(), resolver, sourceBinaryNames, referenced);
                for (JavaAstStatement bodyStatement : lambdaExpression.bodyStatements()) {
                    collectStatementReferences(bodyStatement, resolver, sourceBinaryNames, referenced);
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
                return sourceBinaryNames.contains(typeName) ? typeName : null;
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
