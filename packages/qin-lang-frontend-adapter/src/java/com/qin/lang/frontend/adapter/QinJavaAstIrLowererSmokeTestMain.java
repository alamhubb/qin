package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrAssignmentExpression;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrBoundMethodReferenceExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrIfExpression;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrFunctionLiteral;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStatement;
import com.qin.lang.ir.QinIrStatementExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrSwitchExpression;
import com.qin.lang.ir.QinIrThisExpression;
import com.qin.lang.ir.QinIrTypeKind;

public class QinJavaAstIrLowererSmokeTestMain {
    public static void main(String[] args) {
        String source = """
                package com.example;
                import com.qin.runtime.core.QinBuildTarget;
                import java.io.File;
                import java.util.ArrayList;
                import java.util.List;
                import java.util.Objects;
                import java.util.function.Supplier;
                import static java.util.Objects.toString;
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
                    String safe(String name) { return Objects.toString(name); }
                    String safeImported(String name) { return toString(name); }
                    String choose(boolean flag) { return flag ? "yes" : "no"; }
                    Class[] copy(Class[] params) { return params.clone(); }
                    String chooseTarget(QinBuildTarget target) {
                        return switch (target) {
                            case DEV -> "dev";
                            default -> "other";
                        };
                    }
                    Supplier<String> displaySupplier(Person other) { return other::display; }
                    Supplier<String> thisDisplaySupplier() { return this::display; }
                    Supplier<String> freshStringSupplier() { return this.fresh()::toString; }
                    File namedFile(String name) { return new File(name); }
                    static String conflict(String name) { return name; }
                    String conflict() { return display(); }
                    String conflictAlias() { return conflict(); }
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

        require(person.methods().size() == 19, "method count");
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
        QinIrMethodDeclaration fresh = person.methods().get(6);
        require("fresh".equals(fresh.name()), "fresh method name");
        require(fresh.returnType().kind() == QinIrTypeKind.CLASS, "fresh return type");
        require("java.util.ArrayList".equals(fresh.returnType().binaryName()), "fresh return binary name");
        require(fresh.returnExpression() instanceof QinIrJavaNewExpression, "fresh return expression");
        QinIrJavaNewExpression newExpression = (QinIrJavaNewExpression) fresh.returnExpression();
        require("ArrayList".equals(newExpression.classLocalName()), "fresh new local name");
        require("java.util.ArrayList".equals(newExpression.ownerBinaryName()), "fresh new owner name");
        require(newExpression.arguments().isEmpty(), "fresh argument count");
        QinIrMethodDeclaration safe = person.methods().get(7);
        require("safe".equals(safe.name()), "safe method name");
        require(safe.returnType().kind() == QinIrTypeKind.STRING, "safe return type");
        require(safe.returnExpression() instanceof QinIrStaticMethodCallExpression, "safe return expression");
        QinIrStaticMethodCallExpression staticCall = (QinIrStaticMethodCallExpression) safe.returnExpression();
        require("Objects".equals(staticCall.classLocalName()), "safe class local name");
        require("java.util.Objects".equals(staticCall.ownerBinaryName()), "safe owner name");
        require("toString".equals(staticCall.methodName()), "safe method call name");
        require(staticCall.arguments().size() == 1, "safe argument count");
        require(staticCall.arguments().get(0) instanceof QinIrIdentifierReference, "safe first argument");
        require("name".equals(((QinIrIdentifierReference) staticCall.arguments().get(0)).name()), "safe argument name");
        QinIrMethodDeclaration safeImported = person.methods().get(8);
        require("safeImported".equals(safeImported.name()), "safeImported method name");
        require(safeImported.returnExpression() instanceof QinIrStaticMethodCallExpression,
                "safeImported return expression");
        QinIrStaticMethodCallExpression importedStaticCall =
                (QinIrStaticMethodCallExpression) safeImported.returnExpression();
        require("Objects".equals(importedStaticCall.classLocalName()), "safeImported class local name");
        require("java.util.Objects".equals(importedStaticCall.ownerBinaryName()), "safeImported owner name");
        require("toString".equals(importedStaticCall.methodName()), "safeImported method call name");
        require(importedStaticCall.arguments().size() == 1, "safeImported argument count");
        require(importedStaticCall.arguments().get(0) instanceof QinIrIdentifierReference,
                "safeImported first argument");
        require("name".equals(((QinIrIdentifierReference) importedStaticCall.arguments().get(0)).name()),
                "safeImported argument name");
        QinIrMethodDeclaration choose = person.methods().get(9);
        require("choose".equals(choose.name()), "choose method name");
        require(choose.returnExpression() instanceof QinIrIfExpression, "choose return expression");
        QinIrIfExpression chooseExpression = (QinIrIfExpression) choose.returnExpression();
        require(chooseExpression.test() instanceof QinIrIdentifierReference, "choose condition");
        require("flag".equals(((QinIrIdentifierReference) chooseExpression.test()).name()), "choose condition name");
        require(chooseExpression.consequent() instanceof QinIrStringLiteral, "choose consequent");
        require("yes".equals(((QinIrStringLiteral) chooseExpression.consequent()).value()), "choose consequent value");
        require(chooseExpression.alternate() instanceof QinIrStringLiteral, "choose alternate");
        require("no".equals(((QinIrStringLiteral) chooseExpression.alternate()).value()), "choose alternate value");
        QinIrMethodDeclaration copy = person.methods().get(10);
        require("copy".equals(copy.name()), "copy method name");
        require(copy.returnType().kind() == QinIrTypeKind.CLASS, "copy return type");
        require("[Ljava.lang.Class;".equals(copy.returnType().binaryName()), "copy return binary name");
        require(copy.returnExpression() instanceof QinIrInstanceMethodCallExpression, "copy return expression");
        QinIrInstanceMethodCallExpression cloneCall = (QinIrInstanceMethodCallExpression) copy.returnExpression();
        require(cloneCall.receiver() instanceof QinIrIdentifierReference, "copy receiver");
        require("params".equals(((QinIrIdentifierReference) cloneCall.receiver()).name()), "copy receiver name");
        require("clone".equals(cloneCall.methodName()), "copy method call name");
        require(cloneCall.arguments().isEmpty(), "copy argument count");
        QinIrMethodDeclaration chooseTarget = person.methods().get(11);
        require("chooseTarget".equals(chooseTarget.name()), "chooseTarget method name");
        require(chooseTarget.returnExpression() instanceof QinIrSwitchExpression, "chooseTarget return expression");
        QinIrSwitchExpression targetSwitch = (QinIrSwitchExpression) chooseTarget.returnExpression();
        require(targetSwitch.cases().get(0).test() instanceof QinIrMemberAccessExpression, "chooseTarget enum case");
        QinIrMemberAccessExpression enumCase = (QinIrMemberAccessExpression) targetSwitch.cases().get(0).test();
        require("com.qin.runtime.core.QinBuildTarget".equals(enumCase.objectName()), "chooseTarget enum owner");
        require("DEV".equals(enumCase.propertyName()), "chooseTarget enum property");
        QinIrMethodDeclaration displaySupplier = person.methods().get(12);
        require("displaySupplier".equals(displaySupplier.name()), "displaySupplier method name");
        require(displaySupplier.returnExpression() instanceof QinIrBoundMethodReferenceExpression,
                "displaySupplier bound method reference");
        QinIrBoundMethodReferenceExpression otherReference =
                (QinIrBoundMethodReferenceExpression) displaySupplier.returnExpression();
        require(otherReference.receiver() instanceof QinIrIdentifierReference, "displaySupplier receiver");
        require("other".equals(((QinIrIdentifierReference) otherReference.receiver()).name()),
                "displaySupplier receiver name");
        require("display".equals(otherReference.methodName()), "displaySupplier method reference name");
        QinIrMethodDeclaration thisDisplaySupplier = person.methods().get(13);
        require("thisDisplaySupplier".equals(thisDisplaySupplier.name()), "thisDisplaySupplier method name");
        require(thisDisplaySupplier.returnExpression() instanceof QinIrBoundMethodReferenceExpression,
                "thisDisplaySupplier bound method reference");
        QinIrBoundMethodReferenceExpression thisReference =
                (QinIrBoundMethodReferenceExpression) thisDisplaySupplier.returnExpression();
        require(thisReference.receiver() instanceof QinIrThisExpression, "thisDisplaySupplier receiver");
        require("display".equals(thisReference.methodName()), "thisDisplaySupplier method reference name");
        QinIrMethodDeclaration freshStringSupplier = person.methods().get(14);
        require("freshStringSupplier".equals(freshStringSupplier.name()), "freshStringSupplier method name");
        require(freshStringSupplier.returnExpression() instanceof QinIrBoundMethodReferenceExpression,
                "freshStringSupplier bound method reference");
        QinIrBoundMethodReferenceExpression freshStringReference =
                (QinIrBoundMethodReferenceExpression) freshStringSupplier.returnExpression();
        require(freshStringReference.receiver() instanceof QinIrInstanceMethodCallExpression,
                "freshStringSupplier receiver expression");
        QinIrInstanceMethodCallExpression freshReceiver =
                (QinIrInstanceMethodCallExpression) freshStringReference.receiver();
        require(freshReceiver.receiver() instanceof QinIrThisExpression, "freshStringSupplier receiver owner");
        require("fresh".equals(freshReceiver.methodName()), "freshStringSupplier receiver call");
        require("toString".equals(freshStringReference.methodName()), "freshStringSupplier method reference name");
        QinIrMethodDeclaration namedFile = person.methods().get(15);
        require("namedFile".equals(namedFile.name()), "namedFile method name");
        require(namedFile.returnType().kind() == QinIrTypeKind.CLASS, "namedFile return type");
        require("java.io.File".equals(namedFile.returnType().binaryName()), "namedFile return binary name");
        require(namedFile.returnExpression() instanceof QinIrJavaNewExpression, "namedFile return expression");
        QinIrJavaNewExpression namedFileNewExpression = (QinIrJavaNewExpression) namedFile.returnExpression();
        require("File".equals(namedFileNewExpression.classLocalName()), "namedFile new local name");
        require("java.io.File".equals(namedFileNewExpression.ownerBinaryName()), "namedFile new owner name");
        require(namedFileNewExpression.arguments().size() == 1, "namedFile argument count");
        require(namedFileNewExpression.arguments().get(0) instanceof QinIrIdentifierReference, "namedFile first argument");
        require("name".equals(((QinIrIdentifierReference) namedFileNewExpression.arguments().get(0)).name()),
                "namedFile argument name");
        QinIrMethodDeclaration staticConflict = person.methods().get(16);
        require("conflict".equals(staticConflict.name()), "static conflict method name");
        require(staticConflict.staticMethod(), "static conflict method flag");
        require(staticConflict.parameters().size() == 1, "static conflict arity");
        QinIrMethodDeclaration instanceConflict = person.methods().get(17);
        require("conflict".equals(instanceConflict.name()), "instance conflict method name");
        require(!instanceConflict.staticMethod(), "instance conflict method flag");
        QinIrMethodDeclaration conflictAlias = person.methods().get(18);
        require("conflictAlias".equals(conflictAlias.name()), "conflictAlias method name");
        require(conflictAlias.returnExpression() instanceof QinIrInstanceMethodCallExpression,
                "conflictAlias return expression");
        QinIrInstanceMethodCallExpression conflictCall =
                (QinIrInstanceMethodCallExpression) conflictAlias.returnExpression();
        require(conflictCall.receiver() instanceof QinIrThisExpression, "conflictAlias receiver");
        require("conflict".equals(conflictCall.methodName()), "conflictAlias method call name");
        require(conflictCall.arguments().isEmpty(), "conflictAlias argument count");

        String compactRecordSource = """
                package com.example;
                record Compact(int index, String value) {
                    public Compact {
                        if (index < 0) {
                            throw new IllegalArgumentException("bad index");
                        }
                        value = value == null ? "" : value;
                    }
                }
                """;
        QinIrProgram compactProgram = new QinJavaAstIrLowerer().lowerSource(compactRecordSource);
        QinIrClassDeclaration compact = compactProgram.classDeclarations().get(0);
        long compactConstructorCount = compact.methods().stream()
                .filter(method -> "constructor".equals(method.name()))
                .count();
        require(compactConstructorCount == 1, "compact record constructor count");
        QinIrMethodDeclaration compactConstructor = compact.methods().stream()
                .filter(method -> "constructor".equals(method.name()))
                .findFirst()
                .orElseThrow();
        require(compactConstructor.parameters().size() == 2, "compact record constructor parameter count");
        require("index".equals(compactConstructor.parameters().get(0).name()),
                "compact record first constructor parameter");
        require("value".equals(compactConstructor.parameters().get(1).name()),
                "compact record second constructor parameter");
        require(compactConstructor.bodyStatements().size() >= 3, "compact record constructor body size");
        QinIrStatement trailingStatement =
                compactConstructor.bodyStatements().get(compactConstructor.bodyStatements().size() - 1);
        require(trailingStatement instanceof QinIrStatementExpression,
                "compact record trailing implicit assignment statement");
        QinIrStatementExpression trailingExpression = (QinIrStatementExpression) trailingStatement;
        require(trailingExpression.expression() instanceof QinIrAssignmentExpression,
                "compact record trailing implicit assignment expression");
        QinIrAssignmentExpression trailingAssignment = (QinIrAssignmentExpression) trailingExpression.expression();
        require(trailingAssignment.target() instanceof QinIrPropertyAccessExpression,
                "compact record trailing implicit assignment target");
        require(trailingAssignment.value() instanceof QinIrIdentifierReference,
                "compact record trailing implicit assignment value");
        require("value".equals(((QinIrIdentifierReference) trailingAssignment.value()).name()),
                "compact record trailing implicit assignment value name");

        String charStaticSource = """
                package com.example;
                final class CharStatic {
                    private static final char VALUE_SEPARATOR = '\u0000';
                    static boolean contains(String key) {
                        return key != null && key.indexOf(VALUE_SEPARATOR) >= 0;
                    }
                    static String key(String tokenName, String tokenValue) {
                        return tokenName + VALUE_SEPARATOR + tokenValue;
                    }
                }
                """;
        QinIrProgram charStaticProgram = new QinJavaAstIrLowerer().lowerSource(charStaticSource);
        QinIrClassDeclaration charStatic = charStaticProgram.classDeclarations().get(0);
        QinIrFieldDeclaration valueSeparator = charStatic.fields().get(0);
        require(valueSeparator.type().kind() == QinIrTypeKind.STRING,
                "Java char static field type should be string for generated TS");
        require(valueSeparator.initializer() instanceof QinIrStringLiteral,
                "Java char static field initializer should be a string literal");
        require("\u0000".equals(((QinIrStringLiteral) valueSeparator.initializer()).value()),
                "Java char static field initializer value");

        String interfaceConstantSource = """
                package com.example;
                interface Plan {
                    int NO_MATCH = -2;
                }
                class UsePlan {
                    int read() {
                        return Plan.NO_MATCH;
                    }
                }
                """;
        QinIrProgram interfaceConstantProgram = new QinJavaAstIrLowerer().lowerSource(interfaceConstantSource);
        QinIrClassDeclaration plan = interfaceConstantProgram.classDeclarations().stream()
                .filter(declaration -> "Plan".equals(declaration.simpleName()))
                .findFirst()
                .orElseThrow();
        require(plan.interfaceClass(), "Java interface declaration flag");
        QinIrFieldDeclaration noMatch = plan.fields().stream()
                .filter(field -> "NO_MATCH".equals(field.name()))
                .findFirst()
                .orElseThrow();
        require(noMatch.staticField(), "Java interface constant static IR flag");
        require(noMatch.initializer() != null, "Java interface constant initializer");

        System.out.println("QinJavaAstIrLowererSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
