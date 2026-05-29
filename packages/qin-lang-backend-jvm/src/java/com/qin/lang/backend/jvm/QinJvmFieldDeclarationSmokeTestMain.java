package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrAnnotationArgument;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrTypeRef;

import java.lang.classfile.Annotation;
import java.lang.classfile.AnnotationElement;
import java.lang.classfile.AnnotationValue;
import java.lang.classfile.Attributes;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.FieldModel;
import java.lang.classfile.MethodModel;
import java.lang.classfile.attribute.MethodParametersAttribute;
import java.lang.classfile.attribute.RuntimeVisibleAnnotationsAttribute;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Smoke test for field declaration IR -> JVM class emission.
 */
public final class QinJvmFieldDeclarationSmokeTestMain {
    private QinJvmFieldDeclarationSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrClassDeclaration declaration = new QinIrClassDeclaration(
                null,
                "Payload",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(
                        new QinIrFieldDeclaration(
                                "name",
                                QinIrTypeRef.stringType(),
                                List.of(new QinIrAnnotation(
                                        "com.example.JsonProperty",
                                        List.of(new QinIrAnnotationArgument(
                                                "value",
                                                new QinIrStringLiteral("name"))))),
                                new QinIrStringLiteral("guest")),
                        new QinIrFieldDeclaration("active", QinIrTypeRef.booleanType(), List.of(), new com.qin.lang.ir.QinIrBooleanLiteral(true))),
                List.of());
        QinIrProgram program = new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(declaration));

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, declaration.binaryName());
        ClassModel classModel = ClassFile.of().parse(classBytes);

        requireField(classModel, "name", "Ljava/lang/String;");
        requireField(classModel, "active", "Z");
        requireFieldAnnotation(
                classModel,
                "name",
                "Lcom/example/JsonProperty;",
                "value",
                "name");
        requireAllArgsConstructor(classModel, "(Ljava/lang/String;Z)V", List.of("name", "active"));

        Class<?> defined = new ByteArrayClassLoader()
                .define(declaration.binaryName(), classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();
        Object nameValue = defined.getDeclaredMethod("getName").invoke(instance);
        Object activeValue = defined.getDeclaredMethod("isActive").invoke(instance);
        if (!"guest".equals(nameValue)) {
            throw new IllegalStateException("Unexpected default field value for name: " + nameValue);
        }
        if (!Boolean.TRUE.equals(activeValue)) {
            throw new IllegalStateException("Unexpected default field value for active: " + activeValue);
        }
        Constructor<?> allArgsConstructor = defined.getDeclaredConstructor(String.class, boolean.class);
        Object constructed = allArgsConstructor.newInstance("alice", false);
        Object constructedName = defined.getDeclaredMethod("getName").invoke(constructed);
        Object constructedActive = defined.getDeclaredMethod("isActive").invoke(constructed);
        if (!"alice".equals(constructedName)) {
            throw new IllegalStateException("Unexpected all-args field value for name: " + constructedName);
        }
        if (!Boolean.FALSE.equals(constructedActive)) {
            throw new IllegalStateException("Unexpected all-args field value for active: " + constructedActive);
        }

        System.out.println("QinJvmFieldDeclarationSmokeTestMain passed.");
    }

    private static void requireField(ClassModel classModel, String name, String expectedDescriptor) {
        FieldModel field = classModel.fields().stream()
                .filter(candidate -> candidate.fieldName().stringValue().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing field: " + name));

        String descriptor = field.fieldType().stringValue();
        if (!expectedDescriptor.equals(descriptor)) {
            throw new IllegalStateException(
                    "Unexpected descriptor for field `" + name + "`: " + descriptor);
        }
    }

    private static void requireFieldAnnotation(
            ClassModel classModel,
            String fieldName,
            String annotationDescriptor,
            String elementName,
            String expectedValue) {
        FieldModel field = classModel.fields().stream()
                .filter(candidate -> candidate.fieldName().stringValue().equals(fieldName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing field: " + fieldName));

        RuntimeVisibleAnnotationsAttribute attribute = field.findAttribute(Attributes.runtimeVisibleAnnotations())
                .orElseThrow(() -> new IllegalStateException(
                        "Missing runtime-visible field annotations on: " + fieldName));

        Annotation annotation = attribute.annotations().stream()
                .filter(candidate -> candidate.className().stringValue().equals(annotationDescriptor))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Missing field annotation " + annotationDescriptor + " on: " + fieldName));

        AnnotationElement element = annotation.elements().stream()
                .filter(candidate -> candidate.name().stringValue().equals(elementName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Missing annotation element `" + elementName + "` on field: " + fieldName));

        if (!(element.value() instanceof AnnotationValue.OfString stringValue)) {
            throw new IllegalStateException(
                    "Expected string annotation value for field `" + fieldName + "` element `" + elementName + "`");
        }
        if (!expectedValue.equals(stringValue.stringValue())) {
            throw new IllegalStateException(
                    "Unexpected field annotation value for `" + fieldName + "`: " + stringValue.stringValue());
        }
    }

    private static void requireAllArgsConstructor(
            ClassModel classModel,
            String expectedDescriptor,
            List<String> expectedParameterNames) {
        MethodModel constructor = classModel.methods().stream()
                .filter(candidate -> candidate.methodName().stringValue().equals("<init>"))
                .filter(candidate -> candidate.methodType().stringValue().equals(expectedDescriptor))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Missing all-args constructor with descriptor: " + expectedDescriptor));

        MethodParametersAttribute parameters = constructor.findAttribute(Attributes.methodParameters())
                .orElseThrow(() -> new IllegalStateException("Missing MethodParameters on all-args constructor"));
        if (parameters.parameters().size() != expectedParameterNames.size()) {
            throw new IllegalStateException(
                    "Unexpected all-args constructor parameter count: " + parameters.parameters().size());
        }
        for (int i = 0; i < expectedParameterNames.size(); i++) {
            int index = i;
            String actualName = parameters.parameters().get(i).name()
                    .orElseThrow(() -> new IllegalStateException("Missing constructor parameter name at index " + index))
                    .stringValue();
            if (!expectedParameterNames.get(i).equals(actualName)) {
                throw new IllegalStateException(
                        "Unexpected constructor parameter name at index " + i + ": " + actualName);
            }
        }
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
