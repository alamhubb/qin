package com.qin.runtime.core;

import com.qin.lang.backend.jvm.QinJvmDeclarationClassEmitter;
import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Transitional Spring bridge that now validates and emits from declaration IR
 * rather than directly compiling from raw parser AST.
 */
public final class QinSpringControllerCompiler {
    private static final String SPRING_WEB_BIND_ANNOTATION_MODULE =
            "java:org.springframework.web.bind.annotation";
    private static final String REST_CONTROLLER_BINARY_NAME =
            "org.springframework.web.bind.annotation.RestController";
    private static final String GET_MAPPING_BINARY_NAME =
            "org.springframework.web.bind.annotation.GetMapping";
    private static final String POST_MAPPING_BINARY_NAME =
            "org.springframework.web.bind.annotation.PostMapping";
    private static final String REQUEST_BODY_BINARY_NAME =
            "org.springframework.web.bind.annotation.RequestBody";

    public CompiledController compile(Path sourceFile, String binaryClassName) throws Exception {
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        Objects.requireNonNull(binaryClassName, "binaryClassName cannot be null");

        Path normalizedSource = sourceFile.toAbsolutePath().normalize();
        if (!Files.exists(normalizedSource) || !Files.isRegularFile(normalizedSource)) {
            throw new IllegalArgumentException("Missing Qin controller source: " + normalizedSource);
        }

        String source = Files.readString(normalizedSource, StandardCharsets.UTF_8);
        QinIrProgram program = new QinFrontendLowerer().lowerSource(source);
        QinIrClassDeclaration controller = analyzeController(program);
        byte[] classBytes = new QinJvmDeclarationClassEmitter().compileClass(controller, binaryClassName);
        return new CompiledController(binaryClassName, classBytes);
    }

    private QinIrClassDeclaration analyzeController(QinIrProgram program) {
        if (program.classDeclarations().isEmpty()) {
            throw new IllegalArgumentException(
                    "Qin controller source must contain at least one class declaration");
        }

        QinIrClassDeclaration classDeclaration = findControllerClass(program.classDeclarations());
        if (classDeclaration == null) {
            throw new IllegalArgumentException(
                    "Controller class must import and use @RestController from \"" +
                            SPRING_WEB_BIND_ANNOTATION_MODULE + "\"");
        }

        List<QinIrMethodDeclaration> methods = classDeclaration.methods();
        if (methods.isEmpty()) {
            throw new IllegalArgumentException("Qin controller source must contain controller methods");
        }

        boolean hasRouteMethod = false;
        for (QinIrMethodDeclaration method : methods) {
            QinIrAnnotation routeMapping = findRouteMapping(method.annotations());
            if (routeMapping == null) {
                continue;
            }
            hasRouteMethod = true;
            validateRouteMapping(method, routeMapping);
            validateMethodBody(method);
        }

        if (!hasRouteMethod) {
            throw new IllegalArgumentException("No @GetMapping/@PostMapping methods found in Qin controller");
        }
        return classDeclaration;
    }

    private QinIrClassDeclaration findControllerClass(List<QinIrClassDeclaration> classDeclarations) {
        QinIrClassDeclaration controller = null;
        for (QinIrClassDeclaration classDeclaration : classDeclarations) {
            if (findAnnotation(classDeclaration.annotations(), REST_CONTROLLER_BINARY_NAME) == null) {
                continue;
            }
            if (controller != null) {
                throw new IllegalArgumentException("Qin controller source must contain exactly one @RestController class");
            }
            controller = classDeclaration;
        }
        return controller;
    }

    private void validateRouteMapping(QinIrMethodDeclaration method, QinIrAnnotation annotation) {
        if (annotation.arguments().isEmpty()) {
            throw new IllegalArgumentException(
                    "@" + simpleAnnotationName(annotation.ownerBinaryName()) + " requires a path string argument");
        }

        QinIrExpression value = annotation.arguments().get(0).value();
        if (!isSupportedRoutePathValue(value)) {
            throw new IllegalArgumentException(
                    "@" + simpleAnnotationName(annotation.ownerBinaryName()) + " on method `" + method.name()
                            + "` only supports string literal path arguments");
        }
    }

    private boolean isSupportedRoutePathValue(QinIrExpression value) {
        if (value instanceof QinIrStringLiteral) {
            return true;
        }
        return value instanceof QinIrArrayLiteral arrayLiteral
                && !arrayLiteral.elements().isEmpty()
                && arrayLiteral.elements().get(0) instanceof QinIrStringLiteral;
    }

    private void validateMethodBody(QinIrMethodDeclaration method) {
        if (method.parameters().size() > 1) {
            throw new IllegalArgumentException(
                    "Controller method `" + method.name() + "` currently supports at most one parameter");
        }
        if (method.parameters().size() == 1) {
            validateSingleRequestBodyParameter(method, method.parameters().get(0));
        }
        if (!(method.returnExpression() instanceof QinIrStringLiteral)) {
            throw new IllegalArgumentException(
                    "Controller method `" + method.name() + "` currently supports only string literal returns");
        }
    }

    private void validateSingleRequestBodyParameter(QinIrMethodDeclaration method, QinIrParameter parameter) {
        if (findAnnotation(parameter.annotations(), REQUEST_BODY_BINARY_NAME) == null) {
            throw new IllegalArgumentException(
                    "Controller method `" + method.name()
                            + "` single parameter must declare @RequestBody");
        }
    }

    private void requireAnnotation(
            List<QinIrAnnotation> annotations,
            String requiredBinaryName,
            String errorMessage) {
        if (findAnnotation(annotations, requiredBinaryName) == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private QinIrAnnotation findAnnotation(List<QinIrAnnotation> annotations, String ownerBinaryName) {
        for (QinIrAnnotation annotation : annotations) {
            if (ownerBinaryName.equals(annotation.ownerBinaryName())) {
                return annotation;
            }
        }
        return null;
    }

    private QinIrAnnotation findRouteMapping(List<QinIrAnnotation> annotations) {
        QinIrAnnotation getMapping = findAnnotation(annotations, GET_MAPPING_BINARY_NAME);
        if (getMapping != null) {
            return getMapping;
        }
        return findAnnotation(annotations, POST_MAPPING_BINARY_NAME);
    }

    private String simpleAnnotationName(String ownerBinaryName) {
        int lastDot = ownerBinaryName.lastIndexOf('.');
        return lastDot >= 0 ? ownerBinaryName.substring(lastDot + 1) : ownerBinaryName;
    }

    public record CompiledController(String binaryClassName, byte[] classBytes) {
        public CompiledController {
            Objects.requireNonNull(binaryClassName, "binaryClassName cannot be null");
            Objects.requireNonNull(classBytes, "classBytes cannot be null");
        }

        public Class<?> define(ClassLoader parent) {
            return new ByteArrayClassLoader(parent).define(binaryClassName, classBytes);
        }
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private ByteArrayClassLoader(ClassLoader parent) {
            super(parent);
        }

        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
