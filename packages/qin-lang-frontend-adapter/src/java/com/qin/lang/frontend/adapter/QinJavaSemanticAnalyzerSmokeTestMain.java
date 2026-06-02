package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrTypeKind;

public class QinJavaSemanticAnalyzerSmokeTestMain {
    public static void main(String[] args) {
        String source = """
                package com.example;
                import java.util.ArrayList;
                import java.util.List;
                class Person {
                    String name;
                    List items;
                    int add(int a, int b) { return a + b; }
                    String display() { return this.name; }
                    String greet(String name) { String prefix = "hello "; return prefix + name; }
                    String label() { return this.display(); }
                    String alias() { return display(); }
                    String joined(String name) { return greet(name); }
                    ArrayList fresh() { return new ArrayList(); }
                }
                """;

        QinJavaSemanticModel model = new QinJavaSemanticAnalyzer().analyzeSource(source);
        require(model.classes().size() == 1, "class count");
        QinJavaSemanticClass person = model.classes().get(0);
        require("com.example.Person".equals(person.binaryName()), "class binary name");
        require(person.fields().size() == 2, "field count");
        require(person.fields().get(0).type().kind() == QinIrTypeKind.STRING, "String field type");
        require("java.util.List".equals(person.fields().get(1).type().binaryName()), "imported field type");
        require(person.methods().size() == 7, "method count");
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
        QinJavaSemanticMethod label = person.methods().get(3);
        require(label.returnType().kind() == QinIrTypeKind.STRING, "label declared return type");
        require(label.returnExpressionType().kind() == QinIrTypeKind.STRING, "label return expression type");
        QinJavaSemanticMethod alias = person.methods().get(4);
        require(alias.returnType().kind() == QinIrTypeKind.STRING, "alias declared return type");
        require(alias.returnExpressionType().kind() == QinIrTypeKind.STRING, "alias return expression type");
        QinJavaSemanticMethod joined = person.methods().get(5);
        require(joined.returnType().kind() == QinIrTypeKind.STRING, "joined declared return type");
        require(joined.returnExpressionType().kind() == QinIrTypeKind.STRING, "joined return expression type");
        QinJavaSemanticMethod fresh = person.methods().get(6);
        require(fresh.returnType().kind() == QinIrTypeKind.CLASS, "fresh declared return type");
        require("java.util.ArrayList".equals(fresh.returnType().binaryName()), "fresh declared binary name");
        require(fresh.returnExpressionType().kind() == QinIrTypeKind.CLASS, "fresh return expression type");
        require("java.util.ArrayList".equals(fresh.returnExpressionType().binaryName()), "fresh return expression binary name");

        System.out.println("QinJavaSemanticAnalyzerSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
