package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.lang.classfile.Attributes;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.MethodModel;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Smoke test for first-phase declaration IR -> JVM class emission.
 */
public final class QinJvmDeclarationEmitterSmokeTestMain {
    private QinJvmDeclarationEmitterSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path source = Path.of("D:/project/qkyproject/qinall/qin/examples/apps/hello-java/src/server/HelloController.qin");
        String text = Files.readString(source);

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "server.generated.HelloControllerFromIr");

        ClassModel classModel = ClassFile.of().parse(classBytes);
        if (classModel.majorVersion() != ClassFile.JAVA_21_VERSION) {
            throw new IllegalStateException(
                    "Expected Java 21 classfile, got major version " + classModel.majorVersion());
        }

        if (classModel.findAttribute(Attributes.runtimeVisibleAnnotations()).isEmpty()) {
            throw new IllegalStateException("Expected class runtime-visible annotations");
        }

        requireMethod(classModel, "hello");
        requireMethod(classModel, "ping");

        System.out.println("QinJvmDeclarationEmitterSmokeTestMain passed.");
    }

    private static void requireMethod(ClassModel classModel, String methodName) {
        MethodModel method = classModel.methods().stream()
                .filter(candidate -> candidate.methodName().stringValue().equals(methodName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing method: " + methodName));

        if (method.findAttribute(Attributes.runtimeVisibleAnnotations()).isEmpty()) {
            throw new IllegalStateException("Expected runtime-visible annotations on method: " + methodName);
        }
    }
}
