package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.lang.classfile.Attributes;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.MethodModel;
import java.lang.classfile.attribute.MethodParametersAttribute;
import java.util.Map;

/**
 * Proves parsed Qin class fields lower to JVM fields, constructor metadata, and
 * executable accessor/method bytecode through the .class path.
 */
public final class QinJvmParsedFieldsConstructorSmokeTestMain {
    private QinJvmParsedFieldsConstructorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                class ParsedUserService {
                  name: string = "guest"
                  active: boolean = true

                  label(): string {
                    return this.name
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        QinIrClassDeclaration declaration = requireClass(program, "ParsedUserService");
        if (declaration.fields().size() != 2) {
            throw new IllegalStateException("Expected two parsed fields, got " + declaration.fields().size());
        }

        Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
        byte[] classBytes = compiled.get("ParsedUserService");
        if (classBytes == null || classBytes.length == 0) {
            throw new IllegalStateException("Missing ParsedUserService class bytes");
        }
        requireAllArgsConstructorMetadata(classBytes);

        Class<?> serviceClass = new ByteArrayClassLoader().define("ParsedUserService", classBytes);
        Object defaultInstance = serviceClass.getDeclaredConstructor().newInstance();
        Object defaultName = serviceClass.getDeclaredMethod("getName").invoke(defaultInstance);
        Object defaultActive = serviceClass.getDeclaredMethod("isActive").invoke(defaultInstance);
        Object defaultLabel = serviceClass.getDeclaredMethod("label").invoke(defaultInstance);
        if (!"guest".equals(defaultName) || !Boolean.TRUE.equals(defaultActive) || !"guest".equals(defaultLabel)) {
            throw new IllegalStateException("Unexpected default parsed field values: "
                    + defaultName + ", " + defaultActive + ", " + defaultLabel);
        }

        Object constructed = serviceClass.getDeclaredConstructor(String.class, boolean.class)
                .newInstance("alice", false);
        Object constructedName = serviceClass.getDeclaredMethod("getName").invoke(constructed);
        Object constructedActive = serviceClass.getDeclaredMethod("isActive").invoke(constructed);
        Object constructedLabel = serviceClass.getDeclaredMethod("label").invoke(constructed);
        if (!"alice".equals(constructedName)
                || !Boolean.FALSE.equals(constructedActive)
                || !"alice".equals(constructedLabel)) {
            throw new IllegalStateException("Unexpected all-args parsed constructor values: "
                    + constructedName + ", " + constructedActive + ", " + constructedLabel);
        }

        System.out.println("QinJvmParsedFieldsConstructorSmokeTestMain passed.");
    }

    private static QinIrClassDeclaration requireClass(QinIrProgram program, String binaryName) {
        return program.classDeclarations().stream()
                .filter(candidate -> binaryName.equals(candidate.binaryName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing class declaration: " + binaryName));
    }

    private static void requireAllArgsConstructorMetadata(byte[] classBytes) {
        ClassModel classModel = ClassFile.of().parse(classBytes);
        MethodModel constructor = classModel.methods().stream()
                .filter(candidate -> candidate.methodName().stringValue().equals("<init>")
                        && candidate.methodTypeSymbol().descriptorString().equals("(Ljava/lang/String;Z)V"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing parsed all-args constructor"));

        MethodParametersAttribute parameters = constructor.findAttribute(Attributes.methodParameters())
                .orElseThrow(() -> new IllegalStateException("Missing MethodParameters on parsed constructor"));
        if (parameters.parameters().size() != 2) {
            throw new IllegalStateException("Unexpected parsed constructor parameter count: "
                    + parameters.parameters().size());
        }
        requireParameter(parameters, 0, "name");
        requireParameter(parameters, 1, "active");
    }

    private static void requireParameter(MethodParametersAttribute parameters, int index, String expectedName) {
        String actualName = parameters.parameters().get(index).name()
                .orElseThrow(() -> new IllegalStateException("Missing parsed constructor parameter " + index))
                .stringValue();
        if (!expectedName.equals(actualName)) {
            throw new IllegalStateException("Unexpected parsed constructor parameter at " + index + ": " + actualName);
        }
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
