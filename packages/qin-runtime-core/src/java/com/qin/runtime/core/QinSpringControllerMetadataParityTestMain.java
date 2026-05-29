package com.qin.runtime.core;

import com.qin.lang.ir.QinIrClassDeclaration;

import java.lang.classfile.Annotation;
import java.lang.classfile.AnnotationElement;
import java.lang.classfile.AnnotationValue;
import java.lang.classfile.Attributes;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.MethodModel;
import java.lang.classfile.attribute.RuntimeVisibleAnnotationsAttribute;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Verifies Qin-generated Spring controller class metadata is visible to JVM
 * reflection/frameworks in the same shape that Spring expects.
 */
public final class QinSpringControllerMetadataParityTestMain {
    private static final int EXPECTED_CLASSFILE_MAJOR_VERSION = ClassFile.JAVA_21_VERSION;
    private static final String REST_CONTROLLER_DESCRIPTOR =
            "Lorg/springframework/web/bind/annotation/RestController;";
    private static final String GET_MAPPING_DESCRIPTOR =
            "Lorg/springframework/web/bind/annotation/GetMapping;";
    private static final String POST_MAPPING_DESCRIPTOR =
            "Lorg/springframework/web/bind/annotation/PostMapping;";

    private QinSpringControllerMetadataParityTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path projectRoot = resolveHelloJavaRoot();
        Path controllerSource = projectRoot.resolve("src/server/HelloController.qin").normalize();
        Path serviceSource = projectRoot.resolve("src/server/HelloService.qin").normalize();
        QinSpringCompileUnit compileUnit = QinSpringCompileUnit.compileAll(serviceSource, controllerSource);
        String controllerBinaryName = requireControllerBinaryName(compileUnit);
        byte[] controllerClassBytes = compileUnit.compiledClasses().get(controllerBinaryName);
        if (controllerClassBytes == null || controllerClassBytes.length == 0) {
            throw new IllegalStateException("Missing compiled controller class bytes for " + controllerBinaryName);
        }

        ClassModel classModel = ClassFile.of().parse(controllerClassBytes);
        requireClassFileVersion(classModel, EXPECTED_CLASSFILE_MAJOR_VERSION);
        requireClassAnnotation(classModel, REST_CONTROLLER_DESCRIPTOR);
        requireMethodGetMapping(classModel, "hello", "/api/hello");
        requireMethodGetMapping(classModel, "helloDetail", "/api/hello/detail");
        requireMethodGetMapping(classModel, "ping", "/api/ping");
        requireMethodPostMapping(classModel, "greet", "/api/greet");
        requireMethodPostMapping(classModel, "greetLoud", "/api/greet/loud");

        System.out.println("QinSpringControllerMetadataParityTestMain passed.");
        System.out.println("source: " + controllerSource.toAbsolutePath());
    }

    private static Path resolveHelloJavaRoot() {
        return Paths.get("examples/apps/hello-java").toAbsolutePath().normalize();
    }

    private static String requireControllerBinaryName(QinSpringCompileUnit compileUnit) {
        for (QinIrClassDeclaration declaration : compileUnit.program().classDeclarations()) {
            if (containsAnnotationBinaryName(declaration.annotations(), "org.springframework.web.bind.annotation.RestController")) {
                return declaration.binaryName();
            }
        }
        throw new IllegalStateException("Missing @RestController class in hello-java example");
    }

    private static void requireClassFileVersion(ClassModel classModel, int expectedMajorVersion) {
        if (classModel.majorVersion() != expectedMajorVersion) {
            throw new IllegalStateException(
                    "Unexpected classfile major version: expected "
                            + expectedMajorVersion + ", got " + classModel.majorVersion());
        }
    }

    private static void requireClassAnnotation(ClassModel classModel, String descriptor) {
        RuntimeVisibleAnnotationsAttribute attribute = classModel.findAttribute(Attributes.runtimeVisibleAnnotations())
                .orElseThrow(() -> new IllegalStateException("Missing runtime-visible class annotations"));
        if (!containsAnnotation(attribute.annotations(), descriptor)) {
            throw new IllegalStateException("Missing class annotation: " + descriptor);
        }
    }

    private static void requireMethodGetMapping(ClassModel classModel, String methodName, String path) {
        requireMethodMapping(classModel, methodName, GET_MAPPING_DESCRIPTOR, path);
    }

    private static void requireMethodPostMapping(ClassModel classModel, String methodName, String path) {
        requireMethodMapping(classModel, methodName, POST_MAPPING_DESCRIPTOR, path);
    }

    private static void requireMethodMapping(
            ClassModel classModel,
            String methodName,
            String annotationDescriptor,
            String path) {
        MethodModel method = classModel.methods().stream()
                .filter(candidate -> candidate.methodName().stringValue().equals(methodName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing method: " + methodName));

        RuntimeVisibleAnnotationsAttribute attribute = method.findAttribute(Attributes.runtimeVisibleAnnotations())
                .orElseThrow(() -> new IllegalStateException("Missing runtime-visible annotations on method: " + methodName));

        Annotation mapping = attribute.annotations().stream()
                .filter(annotation -> annotation.className().stringValue().equals(annotationDescriptor))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Missing mapping annotation " + annotationDescriptor + " on method: " + methodName));

        String actualPath = extractFirstStringArrayElement(mapping, "value");
        if (!path.equals(actualPath)) {
            throw new IllegalStateException(
                    "Unexpected mapping value on method `" + methodName + "`: expected "
                            + path + ", got " + actualPath);
        }
    }

    private static boolean containsAnnotation(List<Annotation> annotations, String descriptor) {
        return annotations.stream().anyMatch(annotation -> annotation.className().stringValue().equals(descriptor));
    }

    private static boolean containsAnnotationBinaryName(
            List<com.qin.lang.ir.QinIrAnnotation> annotations,
            String binaryName) {
        return annotations.stream().anyMatch(annotation -> binaryName.equals(annotation.ownerBinaryName()));
    }

    private static String extractFirstStringArrayElement(Annotation annotation, String elementName) {
        AnnotationElement element = annotation.elements().stream()
                .filter(candidate -> candidate.name().stringValue().equals(elementName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Missing annotation element `" + elementName + "` on " + annotation.className().stringValue()));

        if (!(element.value() instanceof AnnotationValue.OfArray arrayValue)) {
            throw new IllegalStateException("Expected annotation element `" + elementName + "` to be an array");
        }
        List<AnnotationValue> values = arrayValue.values();
        if (values.isEmpty()) {
            throw new IllegalStateException("Annotation array `" + elementName + "` must not be empty");
        }
        if (!(values.get(0) instanceof AnnotationValue.OfString stringValue)) {
            throw new IllegalStateException("Expected first annotation array element to be a string");
        }
        return stringValue.stringValue();
    }
}
