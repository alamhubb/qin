package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.slime.parser.SlimeParser;

import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;

/**
 * Proves the Qin JVM target can compile a TS class to .class while directly
 * extending the Java SlimeParser class.
 */
public final class QinJvmJavaSlimeParserExtendsSmokeTestMain {
    private QinJvmJavaSlimeParserExtendsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String text = """
                import { SlimeParser } from "java:com.slime.parser"

                class QinCssTsParser extends SlimeParser {
                  label(): string {
                    return "java-slimeparser-direct"
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        if (program.classDeclarations().size() != 1) {
            throw new IllegalStateException(
                    "Expected one class declaration, got " + program.classDeclarations().size());
        }

        QinIrClassDeclaration declaration = program.classDeclarations().get(0);
        if (!"com.slime.parser.SlimeParser".equals(declaration.superType().binaryName())) {
            throw new IllegalStateException("Unexpected IR superclass: " + declaration.superType());
        }

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, declaration.binaryName());
        ClassModel classModel = ClassFile.of().parse(classBytes);
        String superclass = classModel.superclass()
                .orElseThrow(() -> new IllegalStateException("Missing classfile superclass"))
                .asInternalName();
        if (!"com/slime/parser/SlimeParser".equals(superclass)) {
            throw new IllegalStateException("Unexpected classfile superclass: " + superclass);
        }

        Class<?> defined = new ByteArrayClassLoader().define(declaration.binaryName(), classBytes);
        Object instance = defined.getDeclaredConstructor(String.class).newInstance("const answer = 42;");
        if (!(instance instanceof SlimeParser)) {
            throw new IllegalStateException("Generated class is not a Java SlimeParser: " + instance);
        }
        Object label = defined.getMethod("label").invoke(instance);
        if (!"java-slimeparser-direct".equals(label)) {
            throw new IllegalStateException("Unexpected generated method result: " + label);
        }

        System.out.println("QinJvmJavaSlimeParserExtendsSmokeTestMain passed.");
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
