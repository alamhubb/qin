package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;

/**
 * End-to-end smoke test for extending a java:-imported JVM class.
 */
public final class QinJvmJavaExtendsEndToEndSmokeTestMain {
    private QinJvmJavaExtendsEndToEndSmokeTestMain() {
    }

    public static void main(String[] args) {
        String text = """
                import { ArrayList } from "java:java.util"

                class MyList extends ArrayList {
                  label(): string {
                    return "ok"
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        if (program.classDeclarations().size() != 1) {
            throw new IllegalStateException(
                    "Expected one class declaration, got " + program.classDeclarations().size());
        }

        QinIrClassDeclaration declaration = program.classDeclarations().get(0);
        if (!"java.util.ArrayList".equals(declaration.superType().binaryName())) {
            throw new IllegalStateException("Unexpected IR superclass: " + declaration.superType());
        }

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, "MyList");
        ClassModel classModel = ClassFile.of().parse(classBytes);
        String superclass = classModel.superclass()
                .orElseThrow(() -> new IllegalStateException("Missing classfile superclass"))
                .asInternalName();
        if (!"java/util/ArrayList".equals(superclass)) {
            throw new IllegalStateException("Unexpected classfile superclass: " + superclass);
        }

        System.out.println("QinJvmJavaExtendsEndToEndSmokeTestMain passed.");
    }
}
