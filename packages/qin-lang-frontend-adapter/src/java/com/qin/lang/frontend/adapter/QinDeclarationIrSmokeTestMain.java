package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Smoke test for first-phase declaration IR lowering.
 */
public final class QinDeclarationIrSmokeTestMain {
    private QinDeclarationIrSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path source = Path.of("D:/project/qkyproject/qinall/qin/examples/apps/hello-java/src/server/HelloController.qin");
        String text = Files.readString(source);

        QinIrProgram program = new QinFrontendLowerer().lowerSource(text);
        if (program.classDeclarations().size() != 1) {
            throw new IllegalStateException(
                    "Expected exactly one declaration class, got " + program.classDeclarations().size());
        }

        QinIrClassDeclaration classDeclaration = program.classDeclarations().get(0);
        if (!"HelloController".equals(classDeclaration.simpleName())) {
            throw new IllegalStateException("Unexpected class name: " + classDeclaration.simpleName());
        }
        if (classDeclaration.annotations().size() != 1) {
            throw new IllegalStateException("Expected one class annotation, got " + classDeclaration.annotations().size());
        }
        if (classDeclaration.methods().size() != 2) {
            throw new IllegalStateException("Expected two methods, got " + classDeclaration.methods().size());
        }

        requireMethod(classDeclaration.methods(), "hello");
        requireMethod(classDeclaration.methods(), "ping");

        System.out.println("QinDeclarationIrSmokeTestMain passed.");
        System.out.println("class: " + classDeclaration.simpleName());
    }

    private static void requireMethod(Iterable<QinIrMethodDeclaration> methods, String expectedName) {
        for (QinIrMethodDeclaration method : methods) {
            if (!expectedName.equals(method.name())) {
                continue;
            }
            if (method.annotations().size() != 1) {
                throw new IllegalStateException(
                        "Expected one annotation on method `" + expectedName + "`, got " + method.annotations().size());
            }
            if (method.parameters().size() != 0) {
                throw new IllegalStateException(
                        "Expected zero parameters on method `" + expectedName + "`, got " + method.parameters().size());
            }
            if (!"java.lang.String".equals(method.returnType().binaryName())) {
                throw new IllegalStateException(
                        "Expected String return type on method `" + expectedName + "`, got "
                                + method.returnType().binaryName());
            }
            return;
        }
        throw new IllegalStateException("Missing method: " + expectedName);
    }
}
