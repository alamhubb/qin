package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrTypeKind;

public class QinJavaAstIrLowererSmokeTestMain {
    public static void main(String[] args) {
        String source = """
                package com.example;
                import java.util.List;
                class Person {
                    String name;
                    List items;
                    int add(int a, int b) { return a + b; }
                }
                """;

        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource(source);
        require(program.classDeclarations().size() == 1, "class count");

        QinIrClassDeclaration person = program.classDeclarations().get(0);
        require("com.example".equals(person.packageName()), "package name");
        require("Person".equals(person.simpleName()), "class name");
        require("com.example.Person".equals(person.binaryName()), "binary name");
        require(person.fields().size() == 2, "field count");
        require(person.fields().get(0).type().kind() == QinIrTypeKind.STRING, "String field type");
        require("name".equals(person.fields().get(0).name()), "String field name");
        require(person.fields().get(1).type().kind() == QinIrTypeKind.CLASS, "imported field type kind");
        require("java.util.List".equals(person.fields().get(1).type().binaryName()), "imported field binary name");

        require(person.methods().size() == 1, "method count");
        QinIrMethodDeclaration add = person.methods().get(0);
        require("add".equals(add.name()), "method name");
        require(add.returnType().kind() == QinIrTypeKind.INT, "method return type");
        require(add.parameters().size() == 2, "parameter count");
        require("a".equals(add.parameters().get(0).name()), "first parameter name");
        require(add.parameters().get(0).type().kind() == QinIrTypeKind.INT, "first parameter type");
        require("b".equals(add.parameters().get(1).name()), "second parameter name");
        require(add.parameters().get(1).type().kind() == QinIrTypeKind.INT, "second parameter type");

        System.out.println("QinJavaAstIrLowererSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
