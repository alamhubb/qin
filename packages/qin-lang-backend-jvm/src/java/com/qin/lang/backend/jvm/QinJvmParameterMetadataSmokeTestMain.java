package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrTypeRef;

import java.lang.classfile.Attributes;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.MethodModel;
import java.lang.classfile.attribute.MethodParametersAttribute;
import java.lang.classfile.attribute.RuntimeVisibleParameterAnnotationsAttribute;
import java.util.List;

/**
 * Smoke test for emitted method parameter metadata.
 */
public final class QinJvmParameterMetadataSmokeTestMain {
    private QinJvmParameterMetadataSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrAnnotation requestBody = new QinIrAnnotation(
                "org.springframework.web.bind.annotation.RequestBody",
                List.of());
        QinIrParameter parameter = new QinIrParameter(
                "payload",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(requestBody));
        QinIrMethodDeclaration method = new QinIrMethodDeclaration(
                "create",
                QinIrTypeRef.stringType(),
                List.of(parameter),
                List.of(),
                new QinIrStringLiteral("ok"));
        QinIrClassDeclaration declaration = new QinIrClassDeclaration(
                "server.generated",
                "ParamController",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(method));
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

        MethodModel emittedMethod = classModel.methods().stream()
                .filter(candidate -> candidate.methodName().stringValue().equals("create"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing method create"));

        MethodParametersAttribute parameters = emittedMethod.findAttribute(Attributes.methodParameters())
                .orElseThrow(() -> new IllegalStateException("Missing MethodParameters attribute"));
        if (parameters.parameters().size() != 1) {
            throw new IllegalStateException("Expected exactly one parameter metadata entry");
        }
        String name = parameters.parameters().get(0).name()
                .orElseThrow(() -> new IllegalStateException("Missing parameter name"))
                .stringValue();
        if (!"payload".equals(name)) {
            throw new IllegalStateException("Unexpected parameter name: " + name);
        }

        RuntimeVisibleParameterAnnotationsAttribute parameterAnnotations =
                emittedMethod.findAttribute(Attributes.runtimeVisibleParameterAnnotations())
                        .orElseThrow(() -> new IllegalStateException("Missing parameter annotations attribute"));
        if (parameterAnnotations.parameterAnnotations().size() != 1
                || parameterAnnotations.parameterAnnotations().get(0).size() != 1) {
            throw new IllegalStateException("Expected exactly one parameter annotation");
        }

        String annotationDesc = parameterAnnotations.parameterAnnotations().get(0).get(0).className().stringValue();
        if (!"Lorg/springframework/web/bind/annotation/RequestBody;".equals(annotationDesc)) {
            throw new IllegalStateException("Unexpected parameter annotation: " + annotationDesc);
        }

        System.out.println("QinJvmParameterMetadataSmokeTestMain passed.");
    }
}
