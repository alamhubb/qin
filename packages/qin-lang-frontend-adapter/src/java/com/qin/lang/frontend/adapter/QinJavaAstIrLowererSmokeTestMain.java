package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrThisExpression;
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
                    String display() { return this.name; }
                    String greet(String name) { String prefix = "hello "; return prefix + name; }
                    String label() { return this.display(); }
                    String alias() { return display(); }
                    String joined(String name) { return greet(name); }
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

        require(person.methods().size() == 6, "method count");
        QinIrMethodDeclaration add = person.methods().get(0);
        require("add".equals(add.name()), "method name");
        require(add.returnType().kind() == QinIrTypeKind.INT, "method return type");
        require(add.parameters().size() == 2, "parameter count");
        require("a".equals(add.parameters().get(0).name()), "first parameter name");
        require(add.parameters().get(0).type().kind() == QinIrTypeKind.INT, "first parameter type");
        require("b".equals(add.parameters().get(1).name()), "second parameter name");
        require(add.parameters().get(1).type().kind() == QinIrTypeKind.INT, "second parameter type");
        require(add.returnExpression() instanceof QinIrBuiltinCallExpression, "return expression");
        QinIrBuiltinCallExpression binary = (QinIrBuiltinCallExpression) add.returnExpression();
        require("Global".equals(binary.receiverName()), "binary receiver");
        require("__qin_binary__".equals(binary.methodName()), "binary method");
        require(binary.arguments().size() == 3, "binary argument count");
        require(binary.arguments().get(0) instanceof QinIrStringLiteral, "binary operator literal");
        require("+".equals(((QinIrStringLiteral) binary.arguments().get(0)).value()), "binary operator");
        require(binary.arguments().get(1) instanceof QinIrIdentifierReference, "binary left expression");
        require("a".equals(((QinIrIdentifierReference) binary.arguments().get(1)).name()), "binary left name");
        require(binary.arguments().get(2) instanceof QinIrIdentifierReference, "binary right expression");
        require("b".equals(((QinIrIdentifierReference) binary.arguments().get(2)).name()), "binary right name");
        QinIrMethodDeclaration display = person.methods().get(1);
        require("display".equals(display.name()), "display method name");
        require(display.returnType().kind() == QinIrTypeKind.STRING, "display return type");
        require(display.returnExpression() instanceof QinIrPropertyAccessExpression, "display return expression");
        QinIrPropertyAccessExpression propertyAccess = (QinIrPropertyAccessExpression) display.returnExpression();
        require(propertyAccess.receiver() instanceof QinIrThisExpression, "display receiver");
        require("name".equals(propertyAccess.propertyName()), "display property name");
        QinIrMethodDeclaration greet = person.methods().get(2);
        require("greet".equals(greet.name()), "greet method name");
        require(greet.returnType().kind() == QinIrTypeKind.STRING, "greet return type");
        require(greet.returnExpression() instanceof QinIrBuiltinCallExpression, "greet return expression");
        QinIrBuiltinCallExpression greetBinary = (QinIrBuiltinCallExpression) greet.returnExpression();
        require("__qin_binary__".equals(greetBinary.methodName()), "greet binary method");
        require(greetBinary.arguments().get(0) instanceof QinIrStringLiteral, "greet binary operator");
        require("+".equals(((QinIrStringLiteral) greetBinary.arguments().get(0)).value()), "greet binary operator value");
        require(greetBinary.arguments().get(1) instanceof QinIrStringLiteral, "greet local inline value");
        require("hello ".equals(((QinIrStringLiteral) greetBinary.arguments().get(1)).value()), "greet local inline text");
        require(greetBinary.arguments().get(2) instanceof QinIrIdentifierReference, "greet parameter reference");
        require("name".equals(((QinIrIdentifierReference) greetBinary.arguments().get(2)).name()), "greet parameter name");
        QinIrMethodDeclaration label = person.methods().get(3);
        require("label".equals(label.name()), "label method name");
        require(label.returnType().kind() == QinIrTypeKind.STRING, "label return type");
        require(label.returnExpression() instanceof QinIrInstanceMethodCallExpression, "label return expression");
        QinIrInstanceMethodCallExpression methodCall = (QinIrInstanceMethodCallExpression) label.returnExpression();
        require(methodCall.receiver() instanceof QinIrThisExpression, "label receiver");
        require("display".equals(methodCall.methodName()), "label method call name");
        require(methodCall.arguments().isEmpty(), "label argument count");
        QinIrMethodDeclaration alias = person.methods().get(4);
        require("alias".equals(alias.name()), "alias method name");
        require(alias.returnExpression() instanceof QinIrInstanceMethodCallExpression, "alias return expression");
        QinIrInstanceMethodCallExpression implicitCall = (QinIrInstanceMethodCallExpression) alias.returnExpression();
        require(implicitCall.receiver() instanceof QinIrThisExpression, "alias receiver");
        require("display".equals(implicitCall.methodName()), "alias method call name");
        require(implicitCall.arguments().isEmpty(), "alias argument count");
        QinIrMethodDeclaration joined = person.methods().get(5);
        require("joined".equals(joined.name()), "joined method name");
        require(joined.returnExpression() instanceof QinIrInstanceMethodCallExpression, "joined return expression");
        QinIrInstanceMethodCallExpression argumentCall = (QinIrInstanceMethodCallExpression) joined.returnExpression();
        require(argumentCall.receiver() instanceof QinIrThisExpression, "joined receiver");
        require("greet".equals(argumentCall.methodName()), "joined method call name");
        require(argumentCall.arguments().size() == 1, "joined argument count");
        require(argumentCall.arguments().get(0) instanceof QinIrIdentifierReference, "joined first argument");
        require("name".equals(((QinIrIdentifierReference) argumentCall.arguments().get(0)).name()), "joined argument name");

        System.out.println("QinJavaAstIrLowererSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
