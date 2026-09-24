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
                import java.util.Set;
                import java.util.IdentityHashMap;
                import com.subhuti.struct.*;
                import com.github.benmanes.caffeine.cache.Caffeine;
                import com.github.benmanes.caffeine.cache.RemovalCause;
                import com.slime.java.ast.JavaAstStatement;
                import static com.example.TokenOwner.rule;
                record BackData(int codeIndex) {}
                class OtherBuilderOwner { static class Builder {} }
                class SourceNestedOwner { static class Builder { Builder touch() { return this; } } }
                class OuterStaticHelperOwner {
                    private static boolean helper(List<SubhutiCst> children) { return true; }
                    record Nested(List<SubhutiCst> children) {
                        boolean ok() { return helper(children); }
                    }
                }
                class TokenOwner {
                    static class Node {}
                    static class RuleDef {}
                    static class Builder {
                        Builder name(String value) { return this; }
                        Builder rule(RuleDef rule) { return this; }
                        TokenOwner build() { return new TokenOwner(); }
                    }
                    static Builder builder() { return new Builder(); }
                    static Node node() { return new Node(); }
                    static RuleDef rule(String name, Node node) { return new RuleDef(); }
                }
                class SourceVarargsOwner {
                    void putAlternation(String ruleScope, String... ruleNames) {}
                }
                class SourceWorklistOwner {
                    record Worklist(List<CacheWork> states) {}
                    record Result(List<Worklist> states) {}
                    static Worklist worklist(List<CacheWork> states) { return new Worklist(states); }
                    List<String> stateNames(List<CacheWork> states) {
                        return worklist(states).states().stream().map(state -> state.ruleName()).toList();
                    }
                    static Worklist staticWorklist(List<CacheWork> states) { return new Worklist(states); }
                    List<String> sameClassStaticRecordAccessorStream(List<CacheWork> states) {
                        Worklist worklist = staticWorklist(states);
                        return worklist.states().stream().map(state -> state.ruleName()).toList();
                    }
                    Map<Integer, CacheWork> stateMap(List<CacheWork> states) {
                        Worklist worklist = staticWorklist(states);
                        return worklist.states().stream().collect(java.util.stream.Collectors.toMap(
                            state -> state.children().size(),
                            java.util.function.Function.identity(),
                            (left, right) -> left,
                            LinkedHashMap::new
                        ));
                    }
                    List<String> nestedRecordListGetAccessorStream(Result result) {
                        return result.states().get(0).states().stream()
                            .map(state -> state.ruleName())
                            .toList();
                    }
                    List<String> dynamicNestedRecordListGetAccessorStream(Result result, CacheWork instruction) {
                        return result.states()
                            .get(instruction.children().size())
                            .states()
                            .stream()
                            .map(state -> state.ruleName())
                            .toList();
                    }
                }
                record CacheWork(String ruleName, long hits, long puts, List<CacheWork> children) {
                    long total() { return hits + puts; }
                }
                enum SourceReason {
                    NONE,
                    MISSING;
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
                    List<String> copied(List<String> input) { return List.copyOf(input); }
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
                    boolean streamAllMatch(List<CacheWork> works) {
                        return works.stream().allMatch(work -> work.total() >= 0);
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
                    String formatTopRuleCounts(Map<String, Long> counts) {
                        return counts.entrySet().stream()
                            .sorted((left, right) -> {
                                int countCompare = Long.compare(right.getValue(), left.getValue());
                                if (countCompare != 0) {
                                    return countCompare;
                                }
                                return left.getKey().compareTo(right.getKey());
                            })
                            .map(entry -> entry.getKey() + "=" + entry.getValue())
                            .collect(java.util.stream.Collectors.joining(", ", "[", "]"));
                    }
                    String formatTopCacheWork(List<CacheWork> works) {
                        return works.stream()
                            .map(work -> work.ruleName() + "=" + work.total())
                            .collect(java.util.stream.Collectors.joining(", ", "[", "]"));
                    }
                    List<Long> flatMapCacheWorkTotals(List<CacheWork> works) {
                        return works.stream()
                            .flatMap(work -> work.children().stream())
                            .map(child -> child.total())
                            .toList();
                    }
                    void sourceVarargsCall(SourceVarargsOwner owner) {
                        owner.putAlternation("Scope", "A", "B");
                    }
                    String skipsDeadCompileOnlyBranch(String value) {
                        if (false) {
                            missingCompileOnlyRuntime(value);
                            return "";
                        }
                        return value;
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
                    int switchCaseLocalLoop(int kind) {
                        return switch (kind) {
                            case 1 -> {
                                int nodeCount = 2;
                                for (int index = 0; index < nodeCount; index++) {
                                    nodeCount = nodeCount + index;
                                }
                                yield nodeCount;
                            }
                            default -> 0;
                        };
                    }
                    Class<?> runnableClass(Runnable runnable) { return runnable.getClass(); }
                    int enumOrdinal() { return SourceReason.MISSING.ordinal(); }
                    boolean streamMapMethodReferenceAnyMatch(List<CacheWork> works) {
                        return works.stream()
                            .map(CacheWork::children)
                            .filter(Objects::nonNull)
                            .anyMatch(children -> children != null && !children.isEmpty());
                    }
                    List<CacheWork> cacheWorkChildren(CacheWork work) { return work.children(); }
                    List<List<CacheWork>> streamMapThisMethodReference(List<CacheWork> works) {
                        return works.stream().map(this::cacheWorkChildren).toList();
                    }
                    List<CacheWork> streamMapOwnerCallMethodReference(CacheWork work, List<Integer> indexes) {
                        return indexes.stream().map(work.children()::get).toList();
                    }
                    static CacheWork fromChildren(List<CacheWork> children) {
                        return new CacheWork("", 0, 0, children);
                    }
                    List<CacheWork> streamMapStaticMethodReference(List<List<CacheWork>> groups) {
                        return groups.stream().map(Person::fromChildren).toList();
                    }
                    String negatedPatternOrUsesPattern(Object value) {
                        if (!(value instanceof Person person) || person.display().isEmpty()) {
                            return "";
                        }
                        return person.display();
                    }
                    String switchPatternCase(Object value) {
                        return switch (value) {
                            case Person person -> person.display();
                            default -> "";
                        };
                    }
                    String bareFieldName() { return name; }
                    int streamMapToIntMin(List<CacheWork> works) {
                        return works.stream().mapToInt(work -> work.total()).min().orElse(0);
                    }
                    List<CacheWork> streamSortedComparatorChain(List<CacheWork> works) {
                        return works.stream()
                            .sorted(java.util.Comparator.comparingInt(CacheWork::total)
                                .thenComparing(work -> work.ruleName()))
                            .toList();
                    }
                    List<String> streamSortedComparatorComparingLambda(Map<String, Integer> entries) {
                        return entries.entrySet().stream()
                            .sorted(java.util.Comparator.comparing(entry -> entry.getKey().toString()))
                            .map(entry -> entry.getKey())
                            .toList();
                    }
                    Map<String, CacheWork> streamCollectToMap(List<CacheWork> works) {
                        return works.stream().collect(java.util.stream.Collectors.toMap(
                            work -> work.ruleName(),
                            java.util.function.Function.identity()
                        ));
                    }
                    List<CacheWork> streamSortedExplicitLambdaComparator(List<CacheWork> works) {
                        return works.stream().sorted(java.util.Comparator
                            .comparing((CacheWork work) -> work.ruleName())
                            .thenComparingInt(work -> work.children().size()))
                            .toList();
                    }
                    String collectionToString(Set<String> names) { return names.toString(); }
                    boolean boxedPrimitiveEquals(Map<String, Integer> values, int id) {
                        return values.entrySet().stream()
                            .anyMatch(entry -> entry.getValue().equals(id));
                    }
                    java.util.stream.IntStream streamFlatMapToInt(List<CacheWork> works) {
                        return works.stream().flatMapToInt(work -> java.util.stream.IntStream.of(
                            work.children().size(), work.ruleName().length()
                        ));
                    }
                    Map<String, List<CacheWork>> streamCollectGroupingBy(List<CacheWork> works) {
                        return works.stream().collect(java.util.stream.Collectors.groupingBy(
                            work -> work.ruleName(),
                            LinkedHashMap::new,
                            java.util.stream.Collectors.toList()
                        ));
                    }
                    List<String> streamCollectingAndThenBlockFinisher(List<CacheWork> works) {
                        return works.stream()
                            .map(CacheWork::ruleName)
                            .collect(java.util.stream.Collectors.collectingAndThen(
                                java.util.stream.Collectors.toList(),
                                names -> {
                                    List<String> sortedNames = names.stream().sorted().toList();
                                    return sortedNames;
                                }
                            ));
                    }
                    List<CacheWork> streamSortedComparatorMethodReference(List<CacheWork> works) {
                        return works.stream()
                            .sorted(java.util.Comparator.comparing(CacheWork::ruleName))
                            .toList();
                    }
                    void streamForEachConsumer(List<CacheWork> works, List<String> names) {
                        works.stream().forEach(work -> names.add(work.ruleName()));
                    }
                    Set<String> setCopied(Set<String> names) { return Set.copyOf(names); }
                    String mapForEachBiConsumer(Map<String, CacheWork> works) {
                        final String[] bodyName = new String[] { "" };
                        works.forEach((name, definition) -> bodyName[0] = definition.ruleName());
                        return bodyName[0];
                    }
                    CacheWork identityMapComputeIfAbsentPut(IdentityHashMap<CacheWork, Map<Integer, CacheWork>> memo, CacheWork work) {
                        return memo.computeIfAbsent(work, ignored -> new LinkedHashMap<>()).put(1, work);
                    }
                    TokenOwner sourceStaticImportedBuilderChain() {
                        return TokenOwner.builder()
                            .rule(rule("R", TokenOwner.node()))
                            .build();
                    }
                    java.util.stream.IntStream intStreamOfArray(int[] values) {
                        return java.util.stream.IntStream.of(values);
                    }
                }
                interface ConstantsOwner {
                    int UNAVAILABLE = -1;
                    static int selection() {
                        return UNAVAILABLE;
                    }
                    class Impl {
                        int noMatch() {
                            return UNAVAILABLE;
                        }
                    }
                }
                """;

        QinJavaSemanticModel model = new QinJavaSemanticAnalyzer().analyzeSource(source);
        require(model.classes().size() == 21, "class count");
        QinJavaSemanticClass nestedOuterHelper = model.classes().get(6);
        QinJavaSemanticMethod nestedOuterHelperOk = nestedOuterHelper.methods().get(0);
        require(nestedOuterHelperOk.returnExpressionType().kind() == QinIrTypeKind.BOOLEAN,
                "nested source class can call enclosing static helper");
        QinJavaSemanticClass sourceWorklistOwner = classByName(model, "com.example.SourceWorklistOwner");
        QinJavaSemanticMethod stateNames = sourceWorklistOwner.methods().get(1);
        require("java.util.List".equals(stateNames.returnExpressionType().binaryName()),
                "source nested record accessor stream return binary name");
        require(stateNames.returnExpressionType().typeArguments().size() == 1,
                "source nested record accessor stream generic count");
        require(stateNames.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.STRING,
                "source nested record accessor stream generic type");
        QinJavaSemanticMethod sameClassStaticRecordAccessorStream = sourceWorklistOwner.methods().get(3);
        require("java.util.List".equals(sameClassStaticRecordAccessorStream.returnExpressionType().binaryName()),
                "same-class static record accessor stream return binary name");
        require(sameClassStaticRecordAccessorStream.returnExpressionType().typeArguments().size() == 1,
                "same-class static record accessor stream generic count");
        require(sameClassStaticRecordAccessorStream.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.STRING,
                "same-class static record accessor stream generic type");
        QinJavaSemanticMethod stateMap = sourceWorklistOwner.methods().get(4);
        require("java.util.Map".equals(stateMap.returnExpressionType().binaryName()),
                "same-class static record accessor toMap return binary name");
        require(stateMap.returnExpressionType().typeArguments().size() == 2,
                "same-class static record accessor toMap generic count");
        require(stateMap.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.INT,
                "same-class static record accessor toMap key type");
        require("com.example.CacheWork".equals(stateMap.returnExpressionType().typeArguments().get(1).binaryName()),
                "same-class static record accessor toMap value type");
        QinJavaSemanticMethod nestedRecordListGetAccessorStream = sourceWorklistOwner.methods().get(5);
        require("java.util.List".equals(nestedRecordListGetAccessorStream.returnExpressionType().binaryName()),
                "nested record list get accessor stream return binary name");
        require(nestedRecordListGetAccessorStream.returnExpressionType().typeArguments().size() == 1,
                "nested record list get accessor stream generic count");
        require(nestedRecordListGetAccessorStream.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.STRING,
                "nested record list get accessor stream generic type");
        QinJavaSemanticMethod dynamicNestedRecordListGetAccessorStream = sourceWorklistOwner.methods().get(6);
        require("java.util.List".equals(dynamicNestedRecordListGetAccessorStream.returnExpressionType().binaryName()),
                "dynamic nested record list get accessor stream return binary name");
        require(dynamicNestedRecordListGetAccessorStream.returnExpressionType().typeArguments().size() == 1,
                "dynamic nested record list get accessor stream generic count");
        require(dynamicNestedRecordListGetAccessorStream.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.STRING,
                "dynamic nested record list get accessor stream generic type");
        QinJavaSemanticClass person = classByName(model, "com.example.Person");
        require("com.example.Person".equals(person.binaryName()), "class binary name");
        require(person.fields().size() == 2, "field count");
        require(person.fields().get(0).type().kind() == QinIrTypeKind.STRING, "String field type");
        require("java.util.List".equals(person.fields().get(1).type().binaryName()), "imported field type");
        require(person.methods().size() == 69, "method count");
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
        QinJavaSemanticMethod copied = person.methods().get(11);
        require("java.util.List".equals(copied.returnExpressionType().binaryName()), "List.copyOf return binary name");
        require(copied.returnExpressionType().typeArguments().size() == 1, "List.copyOf return generic count");
        require(copied.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.STRING,
                "List.copyOf return generic type");
        QinJavaSemanticMethod runtimeClass = person.methods().get(12);
        require("java.lang.Class".equals(runtimeClass.returnType().binaryName()), "runtimeClass declared binary name");
        require("java.lang.Class".equals(runtimeClass.returnExpressionType().binaryName()),
                "runtimeClass expression binary name");
        QinJavaSemanticMethod grouped = person.methods().get(13);
        require(grouped.returnType().kind() == QinIrTypeKind.CLASS, "grouped declared return type");
        require("java.util.List".equals(grouped.returnType().binaryName()), "grouped declared binary name");
        require(grouped.returnExpressionType().kind() == QinIrTypeKind.CLASS, "grouped return expression type");
        require("java.util.List".equals(grouped.returnExpressionType().binaryName()), "grouped return binary name");
        require(grouped.returnExpressionType().typeArguments().size() == 1, "grouped return generic count");
        require(grouped.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.STRING,
                "grouped return generic type");
        QinJavaSemanticMethod runnableClass = person.methods().get(41);
        require("java.lang.Class".equals(runnableClass.returnType().binaryName()), "runnableClass declared binary name");
        require("java.lang.Class".equals(runnableClass.returnExpressionType().binaryName()),
                "runnableClass expression binary name");
        QinJavaSemanticMethod entry = person.methods().get(14);
        require("java.util.Map$Entry".equals(entry.returnType().binaryName()), "nested return binary name");
        require("java.util.Map$Entry".equals(entry.returnExpressionType().binaryName()), "nested expression binary name");
        QinJavaSemanticMethod builderChildren = person.methods().get(15);
        require("com.subhuti.struct.SubhutiCst$Builder".equals(builderChildren.returnType().binaryName()),
                "builder declared return binary name");
        require("com.subhuti.struct.SubhutiCst$Builder".equals(builderChildren.returnExpressionType().binaryName()),
                "builder expression return binary name");
        QinJavaSemanticMethod nestedBuilderChildren = person.methods().get(16);
        require("com.subhuti.struct.SubhutiCst$Builder".equals(nestedBuilderChildren.returnExpressionType().binaryName()),
                "nested builder expression return binary name");
        QinJavaSemanticMethod readRecordAccessor = person.methods().get(17);
        require(readRecordAccessor.returnExpressionType().kind() == QinIrTypeKind.INT,
                "record accessor expression return type");
        QinJavaSemanticMethod ownBuilder = person.methods().get(18);
        require("com.example.Person$Builder".equals(ownBuilder.returnType().binaryName()),
                "own nested builder return type");
        QinJavaSemanticMethod sourceNested = person.methods().get(19);
        require("com.example.SourceNestedOwner$Builder".equals(sourceNested.returnExpressionType().binaryName()),
                "source dotted nested return type");
        QinJavaSemanticMethod sourceBuilderChain = person.methods().get(20);
        require("com.example.TokenOwner$Builder".equals(sourceBuilderChain.returnType().binaryName()),
                "source builder chain declared return type");
        require("com.example.TokenOwner$Builder".equals(sourceBuilderChain.returnExpressionType().binaryName()),
                "source builder chain expression return type");
        QinJavaSemanticMethod caffeineRemovalListener = person.methods().get(21);
        require(caffeineRemovalListener.returnType().kind() == QinIrTypeKind.VOID,
                "caffeine removal listener declared return type");
        QinJavaSemanticMethod castObject = person.methods().get(22);
        require("com.example.Person".equals(castObject.returnType().binaryName()),
                "cast object declared return binary name");
        require("com.example.Person".equals(castObject.returnExpressionType().binaryName()),
                "cast object expression return binary name");
        QinJavaSemanticMethod streamFilter = person.methods().get(23);
        require("java.util.List".equals(streamFilter.returnType().binaryName()),
                "stream filter declared return binary name");
        require("java.util.List".equals(streamFilter.returnExpressionType().binaryName()),
                "stream filter expression return binary name");
        require(streamFilter.returnExpressionType().typeArguments().size() == 1,
                "stream filter expression generic count");
        require("com.subhuti.struct.SubhutiCst".equals(streamFilter.returnExpressionType().typeArguments().get(0).binaryName()),
                "stream filter expression generic binary name");
        QinJavaSemanticMethod streamAllMatch = person.methods().get(24);
        require(streamAllMatch.returnExpressionType().kind() == QinIrTypeKind.BOOLEAN,
                "stream allMatch predicate return type");
        QinJavaSemanticMethod streamMapToStatements = person.methods().get(25);
        require("java.util.List".equals(streamMapToStatements.returnExpressionType().binaryName()),
                "stream map toList expression binary name");
        require(streamMapToStatements.returnExpressionType().typeArguments().size() == 1,
                "stream map toList expression generic count");
        require(JavaAstStatement.class.getName().equals(streamMapToStatements.returnExpressionType().typeArguments().get(0).binaryName()),
                "stream map toList expression generic binary name: "
                        + streamMapToStatements.returnExpressionType().typeArguments().get(0));
        QinJavaSemanticMethod streamFindFirst = person.methods().get(26);
        require("com.subhuti.struct.SubhutiCst".equals(streamFindFirst.returnType().binaryName()),
                "stream find first declared return binary name");
        require("com.subhuti.struct.SubhutiCst".equals(streamFindFirst.returnExpressionType().binaryName()),
                "stream find first expression return binary name");
        QinJavaSemanticMethod mergeCount = person.methods().get(27);
        require("java.lang.Integer".equals(mergeCount.returnType().binaryName()),
                "merge count declared return binary name");
        require("java.lang.Integer".equals(mergeCount.returnExpressionType().binaryName()),
                "merge count expression return binary name");
        QinJavaSemanticMethod formatTopRuleCounts = person.methods().get(28);
        require(formatTopRuleCounts.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "entry stream sorted comparator return type");
        QinJavaSemanticMethod formatTopCacheWork = person.methods().get(29);
        require(formatTopCacheWork.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "source record stream map lambda return type");
        QinJavaSemanticMethod flatMapCacheWorkTotals = person.methods().get(30);
        require("java.util.List".equals(flatMapCacheWorkTotals.returnExpressionType().binaryName()),
                "stream flatMap lambda return binary name");
        require(flatMapCacheWorkTotals.returnExpressionType().typeArguments().size() == 1,
                "stream flatMap lambda return generic count");
        require(flatMapCacheWorkTotals.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.INT,
                "stream flatMap lambda mapped generic type: " + flatMapCacheWorkTotals.returnExpressionType());
        QinJavaSemanticMethod sourceVarargsCall = person.methods().get(31);
        require(sourceVarargsCall.returnType().kind() == QinIrTypeKind.VOID,
                "source varargs call declared return type");
        QinJavaSemanticMethod skipsDeadCompileOnlyBranch = person.methods().get(32);
        require(skipsDeadCompileOnlyBranch.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "dead compile-only branch skipped return type");
        QinJavaSemanticMethod firstChildName = person.methods().get(33);
        require(firstChildName.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "generic list get chained method return type");
        QinJavaSemanticMethod prefixIncrementIndex = person.methods().get(34);
        require(prefixIncrementIndex.returnExpressionType().kind() == QinIrTypeKind.INT,
                "prefix increment index char return type");
        QinJavaSemanticMethod typedArray = person.methods().get(35);
        require("[Lcom.subhuti.struct.SubhutiCst;".equals(typedArray.returnExpressionType().binaryName()),
                "generic collection toArray typed return type");
        QinJavaSemanticMethod enhancedForVarChildName = person.methods().get(36);
        require(enhancedForVarChildName.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "enhanced for var generic element method return type");
        QinJavaSemanticMethod conditionalPatternName = person.methods().get(37);
        require(conditionalPatternName.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "conditional expression pattern variable return type");
        QinJavaSemanticMethod logicalPatternName = person.methods().get(38);
        require(logicalPatternName.returnExpressionType().kind() == QinIrTypeKind.BOOLEAN,
                "logical expression pattern variable return type");
        QinJavaSemanticMethod guardPatternName = person.methods().get(39);
        require(guardPatternName.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "guard return pattern variable return type");
        QinJavaSemanticMethod switchCaseLocalLoop = person.methods().get(40);
        require(switchCaseLocalLoop.returnExpressionType().kind() == QinIrTypeKind.INT,
                "switch case local visible to later loop");
        QinJavaSemanticMethod enumOrdinal = person.methods().get(42);
        require(enumOrdinal.returnExpressionType().kind() == QinIrTypeKind.INT,
                "source enum constant instance ordinal return type");
        QinJavaSemanticMethod streamMapMethodReferenceAnyMatch = person.methods().get(43);
        require(streamMapMethodReferenceAnyMatch.returnExpressionType().kind() == QinIrTypeKind.BOOLEAN,
                "stream map method reference anyMatch return type");
        QinJavaSemanticMethod streamMapThisMethodReference = person.methods().get(45);
        require("java.util.List".equals(streamMapThisMethodReference.returnExpressionType().binaryName()),
                "stream map this method reference return binary name");
        require(streamMapThisMethodReference.returnExpressionType().typeArguments().size() == 1,
                "stream map this method reference return generic count");
        require("java.util.List".equals(streamMapThisMethodReference.returnExpressionType().typeArguments().get(0).binaryName()),
                "stream map this method reference generic binary name");
        QinJavaSemanticMethod streamMapOwnerCallMethodReference = person.methods().get(46);
        require("java.util.List".equals(streamMapOwnerCallMethodReference.returnExpressionType().binaryName()),
                "stream map owner-call method reference return binary name");
        require(streamMapOwnerCallMethodReference.returnExpressionType().typeArguments().size() == 1,
                "stream map owner-call method reference return generic count");
        require("com.example.CacheWork".equals(streamMapOwnerCallMethodReference.returnExpressionType().typeArguments().get(0).binaryName()),
                "stream map owner-call method reference generic binary name");
        QinJavaSemanticMethod streamMapStaticMethodReference = person.methods().get(48);
        require("java.util.List".equals(streamMapStaticMethodReference.returnExpressionType().binaryName()),
                "stream map static method reference return binary name");
        require(streamMapStaticMethodReference.returnExpressionType().typeArguments().size() == 1,
                "stream map static method reference return generic count");
        require("com.example.CacheWork".equals(streamMapStaticMethodReference.returnExpressionType().typeArguments().get(0).binaryName()),
                "stream map static method reference generic binary name");
        QinJavaSemanticMethod negatedPatternOrUsesPattern = person.methods().get(49);
        require(negatedPatternOrUsesPattern.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "negated pattern OR RHS variable return type");
        QinJavaSemanticMethod switchPatternCase = person.methods().get(50);
        require(switchPatternCase.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "switch pattern case variable return type");
        QinJavaSemanticMethod bareFieldName = person.methods().get(51);
        require(bareFieldName.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "bare field identifier return type");
        QinJavaSemanticMethod streamMapToIntMin = person.methods().get(52);
        require(streamMapToIntMin.returnExpressionType().kind() == QinIrTypeKind.INT,
                "stream mapToInt lambda parameter return type");
        QinJavaSemanticMethod streamSortedComparatorChain = person.methods().get(53);
        require("java.util.List".equals(streamSortedComparatorChain.returnExpressionType().binaryName()),
                "stream sorted comparator chain return binary name");
        require(streamSortedComparatorChain.returnExpressionType().typeArguments().size() == 1,
                "stream sorted comparator chain return generic count");
        require("com.example.CacheWork".equals(streamSortedComparatorChain.returnExpressionType().typeArguments().get(0).binaryName()),
                "stream sorted comparator chain generic binary name");
        QinJavaSemanticMethod streamSortedComparatorComparingLambda = person.methods().get(54);
        require("java.util.List".equals(streamSortedComparatorComparingLambda.returnExpressionType().binaryName()),
                "stream sorted comparator comparing lambda return binary name");
        require(streamSortedComparatorComparingLambda.returnExpressionType().typeArguments().size() == 1,
                "stream sorted comparator comparing lambda return generic count");
        require(streamSortedComparatorComparingLambda.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.STRING,
                "stream sorted comparator comparing lambda generic type");
        QinJavaSemanticMethod streamCollectToMap = person.methods().get(55);
        require("java.util.Map".equals(streamCollectToMap.returnExpressionType().binaryName()),
                "stream collect toMap return binary name");
        require(streamCollectToMap.returnExpressionType().typeArguments().size() == 2,
                "stream collect toMap generic count");
        require(streamCollectToMap.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.STRING,
                "stream collect toMap key type");
        require("com.example.CacheWork".equals(streamCollectToMap.returnExpressionType().typeArguments().get(1).binaryName()),
                "stream collect toMap value type");
        QinJavaSemanticMethod streamSortedExplicitLambdaComparator = person.methods().get(56);
        require("java.util.List".equals(streamSortedExplicitLambdaComparator.returnExpressionType().binaryName()),
                "stream sorted explicit lambda comparator return binary name");
        require(streamSortedExplicitLambdaComparator.returnExpressionType().typeArguments().size() == 1,
                "stream sorted explicit lambda comparator generic count");
        require("com.example.CacheWork".equals(streamSortedExplicitLambdaComparator.returnExpressionType().typeArguments().get(0).binaryName()),
                "stream sorted explicit lambda comparator generic binary name");
        QinJavaSemanticMethod collectionToString = person.methods().get(57);
        require(collectionToString.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "collection Object.toString return type");
        QinJavaSemanticMethod boxedPrimitiveEquals = person.methods().get(58);
        require(boxedPrimitiveEquals.returnExpressionType().kind() == QinIrTypeKind.BOOLEAN,
                "boxed primitive equals return type");
        QinJavaSemanticMethod streamFlatMapToInt = person.methods().get(59);
        require("java.util.stream.IntStream".equals(streamFlatMapToInt.returnExpressionType().binaryName()),
                "stream flatMapToInt return type");
        QinJavaSemanticMethod streamCollectGroupingBy = person.methods().get(60);
        require("java.util.Map".equals(streamCollectGroupingBy.returnExpressionType().binaryName()),
                "stream collect groupingBy return binary name");
        require(streamCollectGroupingBy.returnExpressionType().typeArguments().size() == 2,
                "stream collect groupingBy generic count");
        require(streamCollectGroupingBy.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.STRING,
                "stream collect groupingBy key type");
        QinJavaSemanticMethod streamCollectingAndThenBlockFinisher = person.methods().get(61);
        require("java.util.List".equals(streamCollectingAndThenBlockFinisher.returnExpressionType().binaryName()),
                "stream collectingAndThen block finisher return binary name");
        require(streamCollectingAndThenBlockFinisher.returnExpressionType().typeArguments().size() == 1,
                "stream collectingAndThen block finisher generic count");
        require(streamCollectingAndThenBlockFinisher.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.STRING,
                "stream collectingAndThen block finisher generic type");
        QinJavaSemanticMethod streamSortedComparatorMethodReference = person.methods().get(62);
        require("java.util.List".equals(streamSortedComparatorMethodReference.returnExpressionType().binaryName()),
                "stream sorted comparator method reference return binary name");
        require(streamSortedComparatorMethodReference.returnExpressionType().typeArguments().size() == 1,
                "stream sorted comparator method reference generic count");
        require("com.example.CacheWork".equals(streamSortedComparatorMethodReference.returnExpressionType().typeArguments().get(0).binaryName()),
                "stream sorted comparator method reference generic binary name");
        QinJavaSemanticMethod streamForEachConsumer = person.methods().get(63);
        require(streamForEachConsumer.returnType().kind() == QinIrTypeKind.VOID,
                "stream forEach consumer return type");
        QinJavaSemanticMethod setCopied = person.methods().get(64);
        require("java.util.Set".equals(setCopied.returnExpressionType().binaryName()),
                "Set.copyOf return binary name");
        require(setCopied.returnExpressionType().typeArguments().size() == 1,
                "Set.copyOf generic count");
        require(setCopied.returnExpressionType().typeArguments().get(0).kind() == QinIrTypeKind.STRING,
                "Set.copyOf generic type");
        QinJavaSemanticMethod mapForEachBiConsumer = person.methods().get(65);
        require(mapForEachBiConsumer.returnExpressionType().kind() == QinIrTypeKind.STRING,
                "map forEach BiConsumer value type");
        QinJavaSemanticMethod identityMapComputeIfAbsentPut = person.methods().get(66);
        require("com.example.CacheWork".equals(identityMapComputeIfAbsentPut.returnExpressionType().binaryName()),
                "IdentityHashMap computeIfAbsent nested put return type");
        QinJavaSemanticMethod sourceStaticImportedBuilderChain = person.methods().get(67);
        require("com.example.TokenOwner".equals(sourceStaticImportedBuilderChain.returnExpressionType().binaryName()),
                "source static-imported builder chain return type");
        QinJavaSemanticMethod intStreamOfArray = person.methods().get(68);
        require("java.util.stream.IntStream".equals(intStreamOfArray.returnExpressionType().binaryName()),
                "IntStream.of int array return type");
        QinJavaSemanticClass constantsOwner = classByName(model, "com.example.ConstantsOwner");
        QinJavaSemanticMethod selection = constantsOwner.methods().get(0);
        require(selection.returnExpressionType().kind() == QinIrTypeKind.INT,
                "bare static interface constant return type");
        QinJavaSemanticClass constantsOwnerImpl = classByName(model, "com.example.ConstantsOwner$Impl");
        QinJavaSemanticMethod noMatch = constantsOwnerImpl.methods().get(0);
        require(noMatch.returnExpressionType().kind() == QinIrTypeKind.INT,
                "nested class outer interface constant return type");

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
                import com.multi.struct.SourceCst.Builder;
                class SourceUse {
                    void restore(SourceCst currentCst) {
                        SourceCst.builder().from(currentCst);
                    }
                    Builder restoreNested(SourceCst currentCst) {
                        return SourceCst.builder().from(currentCst);
                    }
                }
                """;
        QinJavaSemanticModel multiProgramModel = new QinJavaSemanticAnalyzer().analyzePrograms(List.of(
                JavaCstToAst.parse(ownerSource),
                JavaCstToAst.parse(useSource)));
        require(multiProgramModel.classes().size() == 3, "multi program class count");
        QinJavaSemanticClass sourceUse = multiProgramModel.classes().get(2);
        QinJavaSemanticMethod restoreNested = sourceUse.methods().get(1);
        require("com.multi.struct.SourceCst$Builder".equals(restoreNested.returnExpressionType().binaryName()),
                "source imported nested class binary name");
        QinJavaSemanticAnalyzer analyzer = new QinJavaSemanticAnalyzer();
        require(!analyzer.isLoadableClass("com.subhuti.debug.logWriter"), "wrong-case class probe");

        System.out.println("QinJavaSemanticAnalyzerSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }

    private static QinJavaSemanticClass classByName(QinJavaSemanticModel model, String binaryName) {
        for (QinJavaSemanticClass candidate : model.classes()) {
            if (binaryName.equals(candidate.binaryName())) {
                return candidate;
            }
        }
        throw new IllegalStateException("Missing semantic class " + binaryName);
    }
}
