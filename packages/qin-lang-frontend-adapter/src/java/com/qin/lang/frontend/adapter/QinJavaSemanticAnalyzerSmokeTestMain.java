package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrTypeKind;

public class QinJavaSemanticAnalyzerSmokeTestMain {
    public static void main(String[] args) {
        String source = """
                package com.example;
                import java.util.List;
                class Person {
                    String name;
                    List items;
                    int add(int a, int b) { return a + b; }
                    String display() { return this.name; }
                    String greet(String name) { String prefix = "hello "; return prefix + name; }
                }
                """;

        QinJavaSemanticModel model = new QinJavaSemanticAnalyzer().analyzeSource(source);
        require(model.classes().size() == 1, "class count");
        QinJavaSemanticClass person = model.classes().get(0);
        require("com.example.Person".equals(person.binaryName()), "class binary name");
        require(person.fields().size() == 2, "field count");
        require(person.fields().get(0).type().kind() == QinIrTypeKind.STRING, "String field type");
        require("java.util.List".equals(person.fields().get(1).type().binaryName()), "imported field type");
        require(person.methods().size() == 3, "method count");
        QinJavaSemanticMethod add = person.methods().get(0);
        require(add.returnType().kind() == QinIrTypeKind.INT, "declared return type");
        require(add.returnExpressionType().kind() == QinIrTypeKind.INT, "return expression type");
        require(add.parameters().size() == 2, "parameter count");
        require(add.parameters().get(0).type().kind() == QinIrTypeKind.INT, "first parameter type");
        require(add.parameters().get(1).type().kind() == QinIrTypeKind.INT, "second parameter type");
        QinJavaSemanticMethod display = person.methods().get(1);
        require(display.returnType().kind() == QinIrTypeKind.STRING, "display declared return type");
        require(display.returnExpressionType().kind() == QinIrTypeKind.STRING, "display return expression type");
        QinJavaSemanticMethod greet = person.methods().get(2);
        require(greet.returnType().kind() == QinIrTypeKind.STRING, "greet declared return type");
        require(greet.returnExpressionType().kind() == QinIrTypeKind.STRING, "greet return expression type");

        System.out.println("QinJavaSemanticAnalyzerSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
