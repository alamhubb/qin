package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.lang.classfile.Annotation;
import java.lang.classfile.AnnotationElement;
import java.lang.classfile.AnnotationValue;
import java.lang.classfile.Attributes;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.FieldModel;

/**
 * End-to-end smoke test for field decorator lowering through JVM emission.
 */
public final class QinJvmFieldAnnotationEndToEndSmokeTestMain {
    private QinJvmFieldAnnotationEndToEndSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                import { JsonProperty as JP } from "java:com.example"

                class Payload {
                  @JP("user_name")
                  name: string
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "Payload");

        ClassModel classModel = ClassFile.of().parse(classBytes);
        FieldModel field = classModel.fields().stream()
                .filter(candidate -> candidate.fieldName().stringValue().equals("name"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing field: name"));

        Annotation annotation = field.findAttribute(Attributes.runtimeVisibleAnnotations())
                .orElseThrow(() -> new IllegalStateException("Missing runtime-visible field annotations"))
                .annotations().stream()
                .filter(candidate -> candidate.className().stringValue().equals("Lcom/example/JsonProperty;"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing JsonProperty field annotation"));

        AnnotationElement valueElement = annotation.elements().stream()
                .filter(candidate -> candidate.name().stringValue().equals("value"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing value element on field annotation"));
        if (!(valueElement.value() instanceof AnnotationValue.OfString stringValue)
                || !"user_name".equals(stringValue.stringValue())) {
            throw new IllegalStateException("Unexpected JsonProperty field annotation payload: " + valueElement.value());
        }

        System.out.println("QinJvmFieldAnnotationEndToEndSmokeTestMain passed.");
    }
}
