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
                }
                """;

        QinJavaSemanticModel model = new QinJavaSemanticAnalyzer().analyzeSource(source);
        require(model.classes().size() == 1, "class count");
        QinJavaSemanticClass person = model.classes().get(0);
        require("com.example.Person".equals(person.binaryName()), "class binary name");
        require(person.fields().size() == 2, "field count");
        require(person.fields().get(0).type().kind() == QinIrTypeKind.STRING, "String field type");
        require("java.util.List".equals(person.fields().get(1).type().binaryName()), "imported field type");
        require(person.methods().size() == 1, "method count");
        QinJavaSemanticMethod add = person.methods().get(0);
        require(add.returnType().kind() == QinIrTypeKind.INT, "declared return type");
        require(add.returnExpressionType().kind() == QinIrTypeKind.INT, "return expression type");
        require(add.parameters().size() == 2, "parameter count");
        require(add.parameters().get(0).type().kind() == QinIrTypeKind.INT, "first parameter type");
        require(add.parameters().get(1).type().kind() == QinIrTypeKind.INT, "second parameter type");

        System.out.println("QinJavaSemanticAnalyzerSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
