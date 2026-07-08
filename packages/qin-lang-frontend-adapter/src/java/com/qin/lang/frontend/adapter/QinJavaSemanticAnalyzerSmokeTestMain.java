package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrTypeKind;
import com.slime.java.ast.JavaAstStatement;
import com.slime.java.ast.JavaCstToAst;
import java.util.List;

public class QinJavaSemanticAnalyzerSmokeTestMain {
    public static void main(String[] args) {
        String source = """
                package com.example;
                import java.util.ArrayList;
                import java.util.List;
                import java.util.LinkedHashMap;
                import java.util.Map;
                import java.util.Objects;
                import com.subhuti.struct.*;
                import com.github.benmanes.caffeine.cache.Caffeine;
                import com.github.benmanes.caffeine.cache.RemovalCause;
                import com.slime.java.ast.JavaAstStatement;
                record BackData(int codeIndex) {}
                class OtherBuilderOwner { static class Builder {} }
                class SourceNestedOwner { static class Builder { Builder touch() { return this; } } }
                class TokenOwner {
                    static class Builder {
                        Builder name(String value) { return this; }
                    }
                    static Builder builder() { return new Builder(); }
                }
                class Person {
                    static class Builder {}
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
                    String formatted(String name, int index) { return String.format("hello %s %d", name, index); }
                    boolean boxedBooleanEquals(Boolean flag) { return Boolean.TRUE.equals(flag); }
                    List<String> readonly(List<String> input) { return java.util.Collections.unmodifiableList(input); }
                    Class<?> runtimeClass() { return getClass(); }
                    List<String> grouped(int index) {
                        Map<Integer, List<String>> groups = new LinkedHashMap<>();
                        return groups.computeIfAbsent(index, key -> new ArrayList<>());
                    }
                    Map.Entry entry(Map.Entry value) { return value; }
                    SubhutiCst.Builder builderChildren(List<SubhutiCst> children) {
                        return SubhutiCst.builder().children(children);
                    }
                    SubhutiCst.Builder nestedBuilderChildren(List<SubhutiCst> input) {
                        SubhutiCst.Builder builder = SubhutiCst.builder();
                        if (!input.isEmpty()) {
                            List<SubhutiCst> children = input;
                            if (children.isEmpty()) {
                                builder.children(null);
                            } else {
                                builder.children(children);
                            }
                        }
                        return builder;
                    }
                    int readRecordAccessor(BackData state) { return state.codeIndex(); }
                    Builder ownBuilder() { return new Builder(); }
                    SourceNestedOwner.Builder sourceNested(SourceNestedOwner.Builder builder) { return builder.touch(); }
                    TokenOwner.Builder sourceBuilderChain() { return TokenOwner.builder().name("x"); }
                    void caffeineRemovalListener() {
                        Caffeine<String, String> builder = Caffeine.newBuilder()
                            .removalListener((String key, String value, RemovalCause cause) -> {
                                if (cause.wasEvicted()) {
                                    System.out.println(key);
                                }
                            });
                    }
                    Person castObject(Object obj) { return (Person) obj; }
                    List<SubhutiCst> streamFilter(List<SubhutiCst> children, String name) {
                        return children.stream()
                            .filter(c -> name.equals(c.getName()))
                            .collect(java.util.stream.Collectors.toList());
                    }
                    List<JavaAstStatement> streamMapToStatements(List<com.slime.java.ast.JavaAstExpression> expressions) {
                        return expressions.stream()
                            .<JavaAstStatement>map(com.slime.java.ast.JavaAstExpressionStatement::new)
                            .toList();
                    }
                    SubhutiCst streamFindFirst(List<SubhutiCst> children, String name) {
                        return children.stream()
                            .filter(c -> name.equals(c.getName()) && c.getValue() != null)
                            .findFirst()
                            .orElse(null);
                    }
                    Integer mergeCount(Map<String, Integer> counts, String name) {
                        return counts.merge(name, 1, Integer::sum);
                    }
                    String firstChildName(List<SubhutiCst> children) {
                        return children.get(0).getName();
                    }
                    char prefixIncrementIndex(String text) {
                        int i = 0;
                        return text.charAt(++i);
                    }
                    SubhutiCst[] typedArray(List<SubhutiCst> children) {
                        return children.toArray(new SubhutiCst[0]);
                    }
                    String enhancedForVarChildName(List<SubhutiCst> children) {
                        String name = "";
                        for (var child : children) {
                            name = child.getName();
                        }
                        return name;
                    }
                    String conditionalPatternName(Object value) {
                        return value instanceof Person person ? person.display() : "";
                    }
                    boolean logicalPatternName(Object value) {
                        return value instanceof Person person && person.display().isEmpty();
                    }
                    String guardPatternName(Object value, boolean blank) {
                        if (!(value instanceof Person person) || blank) {
                            return "";
                        }
                        return person.display();
                    }
                    Class<?> runnableClass(Runnable runnable) { return runnable.getClass(); }
                }
                """;

        QinJavaSemanticModel model = new QinJavaSemanticAnalyzer().analyzeSource(source);
        require(model.classes().size() == 9, "class count");
        QinJavaSemanticClass person = model.classes().get(7);
        require("com.example.Person".equals(person.binaryName()), "class binary name");
        require(person.fields().size() == 2, "field count");
        require(person.fields().get(0).type().kind() == QinIrTypeKind.STRING, "String field type");
        require("java.util.List".equals(person.fields().get(1).type().binaryName()), "imported field type");
        require(person.methods().size() == 34, "method count");
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
        QinJavaSemanticMethod safe = person.methods().get(7);
        require(safe.returnType().kind() == QinIrTypeKind.STRING, "safe declared return type");
        require(safe.returnExpressionType().kind() == QinIrTypeKind.STRING, "safe return expression type");
        QinJavaSemanticMethod formatted = person.methods().get(8);
        require(formatted.returnType().kind() == QinIrTypeKind.STRING, "formatted declared return type");
        require(formatted.returnExpressionType().kind() == QinIrTypeKind.STRING, "formatted return expression type");
        QinJavaSemanticMethod boxedBooleanEquals = person.methods().get(9);
        require(boxedBooleanEquals.returnType().kind() == QinIrTypeKind.BOOLEAN,
                "boxed boolean equals declared return type");
        require(boxedBooleanEquals.returnExpressionType().kind() == QinIrTypeKind.BOOLEAN,
                "boxed boolean equals expression return type");
        QinJavaSemanticMethod readonly = person.methods().get(10);
        require(readonly.returnType().kind() == QinIrTypeKind.CLASS, "readonly declared return type");
        require("java.util.List".equals(readonly.returnType().binaryName()), "readonly declared binary name");
        require(readonly.returnExpressionType().kind() == QinIrTypeKind.CLASS, "readonly return expression type");
        require("java.util.List".equals(readonly.returnExpressionType().binaryName()), "readonly return binary name");
        QinJavaSemanticMethod runtimeClass = person.methods().get(11);
        require("java.lang.Class".equals(runtimeClass.returnType().binaryName()), "runtimeClass declared binary name");
        require("java.lang.Class".equals(runtimeClass.returnExpressionType().binaryName()),
                "runtimeClass expression binary name");
        QinJavaSemanticMethod grouped = person.methods().get(12);
        require(grouped.returnType().kind() == QinIrTypeKind.CLASS, "grouped declared return type");
        require("java.util.List".equals(grouped.returnType().binaryName()), "grouped declared binary name");
        require(grouped.returnExpressionType().kind() == QinIrTypeKind.CLASS, "grouped return expression type");
        require("java.util.List".equals(grouped.returnExpressionType().binaryName()), "grouped return binary name");
        require(grouped.returnExpressionType().typeArguments().size() == 1, "grouped return generic count");
        require(grouped.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.STRING,
                "grouped return generic type");
        QinJavaSemanticMethod runnableClass = person.methods().get(person.methods().size() - 1);
        require("java.lang.Class".equals(runnableClass.returnType().binaryName()), "runnableClass declared binary name");
        require("java.lang.Class".equals(runnableClass.returnExpressionType().binaryName()),
                "runnableClass expression binary name");
        QinJavaSemanticMethod entry = person.methods().get(13);
        require("java.util.Map$Entry".equals(entry.returnType().binaryName()), "nested return binary name");
        require("java.util.Map$Entry".equals(entry.returnExpressionType().binaryName()), "nested expression binary name");
        QinJavaSemanticMethod builderChildren = person.methods().get(14);
        require("com.subhuti.struct.SubhutiCst$Builder".equals(builderChildren.returnType().binaryName()),
                "builder declared return binary name");
        require("com.subhuti.struct.SubhutiCst$Builder".equals(builderChildren.returnExpressionType().binaryName()),
                "builder expression return binary name");
        QinJavaSemanticMethod nestedBuilderChildren = person.methods().get(15);
        require("com.subhuti.struct.SubhutiCst$Builder".equals(nestedBuilderChildren.returnExpressionType().binaryName()),
                "nested builder expression return binary name");
        QinJavaSemanticMethod readRecordAccessor = person.methods().get(16);
        require(readRecordAccessor.returnExpressionType().kind() == QinIrTypeKind.INT,
                "record accessor expression return type");
        QinJavaSemanticMethod ownBuilder = person.methods().get(17);
        require("com.example.Person$Builder".equals(ownBuilder.returnType().binaryName()),
                "own nested builder return type");
        QinJavaSemanticMethod sourceNested = person.methods().get(18);
        require("com.example.SourceNestedOwner$Builder".equals(sourceNested.returnExpressionType().binaryName()),
                "source dotted nested return type");
        QinJavaSemanticMethod sourceBuilderChain = person.methods().get(19);
        require("com.example.TokenOwner$Builder".equals(sourceBuilderChain.returnType().binaryName()),
                "source builder chain declared return type");
        require("com.example.TokenOwner$Builder".equals(sourceBuilderChain.returnExpressionType().binaryName()),
                "source builder chain expression return type");
        QinJavaSemanticMethod caffeineRemovalListener = person.methods().get(20);
        require(caffeineRemovalListener.returnType().kind() == QinIrTypeKind.VOID,
                "caffeine removal listener declared return type");
        QinJavaSemanticMethod castObject = person.methods().get(21);
        require("com.example.Person".equals(castObject.returnType().binaryName()),
                "cast object declared return binary name");
        require("com.example.Person".equals(castObject.returnExpressionType().binaryName()),
                "cast object expression return binary name");
        QinJavaSemanticMethod streamFilter = person.methods().get(22);
        require("java.util.List".equals(streamFilter.returnType().binaryName()),
                "stream filter declared return binary name");
        require("java.util.List".equals(streamFilter.returnExpressionType().binaryName()),
                "stream filter expression return binary name");
        require(streamFilter.returnExpressionType().typeArguments().size() == 1,
                "stream filter expression generic count");
        require("com.subhuti.struct.SubhutiCst".equals(streamFilter.returnExpressionType().typeArguments().get(0).binaryName()),
                "stream filter expression generic binary name");
        QinJavaSemanticMethod streamMapToStatements = person.methods().get(23);
        require("java.util.List".equals(streamMapToStatements.returnExpressionType().binaryName()),
                "stream map toList expression binary name");
        require(streamMapToStatements.returnExpressionType().typeArguments().size() == 1,
                "stream map toList expression generic count");
        require(JavaAstStatement.class.getName().equals(streamMapToStatements.returnExpressionType().typeArguments().get(0).binaryName()),
                "stream map toList expression generic binary name: "
                        + streamMapToStatements.returnExpressionType().typeArguments().get(0));
        QinJavaSemanticMethod streamFindFirst = person.methods().get(24);
        require("com.subhuti.struct.SubhutiCst".equals(streamFindFirst.returnType().binaryName()),
                "stream find first declared return binary name");
        require("com.subhuti.struct.SubhutiCst".equals(streamFindFirst.returnExpressionType().binaryName()),
                "stream find first expression return binary name");
        QinJavaSemanticMethod mergeCount = person.methods().get(25);
        require("java.lang.Integer".equals(mergeCount.returnType().binaryName()),
                "merge count declared return binary name");
        require("java.lang.Integer".equals(mergeCount.returnExpressionType().binaryName()),
                "merge count expression return binary name");
        QinJavaSemanticMethod firstChildName = person.methods().get(26);
        require(firstChildName.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "generic list get chained method return type");
        QinJavaSemanticMethod prefixIncrementIndex = person.methods().get(27);
        require(prefixIncrementIndex.returnExpressionType().kind() == QinIrTypeKind.INT,
                "prefix increment index char return type");
        QinJavaSemanticMethod typedArray = person.methods().get(28);
        require("[Lcom.subhuti.struct.SubhutiCst;".equals(typedArray.returnExpressionType().binaryName()),
                "generic collection toArray typed return type");
        QinJavaSemanticMethod enhancedForVarChildName = person.methods().get(29);
        require(enhancedForVarChildName.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "enhanced for var generic element method return type");
        QinJavaSemanticMethod conditionalPatternName = person.methods().get(30);
        require(conditionalPatternName.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "conditional expression pattern variable return type");
        QinJavaSemanticMethod logicalPatternName = person.methods().get(31);
        require(logicalPatternName.returnExpressionType().kind() == QinIrTypeKind.BOOLEAN,
                "logical expression pattern variable return type");
        QinJavaSemanticMethod guardPatternName = person.methods().get(32);
        require(guardPatternName.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "guard return pattern variable return type");

        String ownerSource = """
                package com.multi.struct;
                class SourceCst {
                    static class Builder {
                        Builder from(SourceCst cst) { return this; }
                    }
                    static Builder builder() { return new Builder(); }
                }
                """;
        String useSource = """
                package com.multi.parser;
                import com.multi.struct.*;
                class SourceUse {
                    void restore(SourceCst currentCst) {
                        SourceCst.builder().from(currentCst);
                    }
                }
                """;
        QinJavaSemanticModel multiProgramModel = new QinJavaSemanticAnalyzer().analyzePrograms(List.of(
                JavaCstToAst.parse(ownerSource),
                JavaCstToAst.parse(useSource)));
        require(multiProgramModel.classes().size() == 3, "multi program class count");
        QinJavaSemanticAnalyzer analyzer = new QinJavaSemanticAnalyzer();
        require(!analyzer.isLoadableClass("com.subhuti.debug.logWriter"), "wrong-case class probe");

        System.out.println("QinJavaSemanticAnalyzerSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
