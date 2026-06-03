package com.qin.runtime.core;

import com.slime.java.ast.JavaCstToAst;
import com.slime.java.ast.JavaAstInstanceofPatternExpression;
import com.slime.java.ast.JavaAstMethodDeclaration;
import com.slime.java.ast.JavaAstProgram;

public final class QinJavaParserSnippetSmokeTestMain {
    private QinJavaParserSnippetSmokeTestMain() {
    }

    public static void main(String[] args) {
        JavaAstProgram program = JavaCstToAst.parse("""
                package demo;

                import java.util.List;
                import java.util.Map;

                public abstract class SubhutiParserCore<T extends SubhutiTokenConsumer> extends SubhutiParserState<T> {
                    private static final class RuleExecutionResult<R> {
                        private final R ruleResult;
                        private final SubhutiCst cst;

                        private RuleExecutionResult(R ruleResult, SubhutiCst cst) {
                            this.ruleResult = ruleResult;
                            this.cst = cst;
                        }
                    }

                    public SubhutiParserCore(String sourceCode, Class<T> tokenConsumerClass, List<SubhutiCreateToken> tokens) {
                        super(sourceCode, tokenConsumerClass, tokens);
                    }

                    private void lambdaSmoke(Map<Integer, List<ParseRecordNode>> groups, String key, ParseRecordNode node) {
                        groups.computeIfAbsent(key, k -> new java.util.ArrayList<>()).add(node);
                    }

                    public static <P extends SubhutiParser<?>> P create(Class<P> parserClass, Object... args) {
                        return null;
                    }

                    Class<?> parserClassLiteral() {
                        return SubhutiParser.class;
                    }

                    boolean charLiteral(String value, int i) {
                        return value.charAt(i) == '\\\\'
                            || value.charAt(i) == 'u'
                            || value.charAt(i) == '\\n'
                            || value.charAt(i) == '\\r'
                            || value.charAt(i) == '\\u2028'
                            || value.charAt(i) == '\\u2029';
                    }

                    void arrayLiteral() {
                        String[] values = new String[]{"LBrace", "Function", "Class"};
                    }

                    void switchRuleSmoke(Token token) {
                        switch (token.getTokenName()) {
                            case "Greater" -> tokenConsumer.Greater();
                            case "RightShift", "GreaterEqual" -> consumePartialToken("Greater", ">", 1);
                            default -> setParseFail();
                        }
                    }

                    boolean patternSmoke(Object obj) {
                        return obj instanceof ParseRecordNode other;
                    }

                    private record MethodKey(Class<?> owner, String name, Class<?>[] params) {
                        private MethodKey {
                            params = params == null ? new Class<?>[0] : params.clone();
                        }

                        @Override
                        public boolean equals(Object obj) {
                            if (!(obj instanceof MethodKey other)) {
                                return false;
                            }
                            return name.equals(other.name);
                        }
                    }
                }
                """);
        if (program.classes().get(0).nestedClasses().size() < 2) {
            throw new IllegalStateException("Expected nested class and nested record in Java AST");
        }
        JavaAstMethodDeclaration patternSmoke = program.classes().get(0).methods().stream()
                .filter(method -> "patternSmoke".equals(method.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Expected patternSmoke method in Java AST"));
        if (!(patternSmoke.returnExpression()
                instanceof JavaAstInstanceofPatternExpression patternExpression)
                || !"ParseRecordNode".equals(patternExpression.typeName())
                || !"other".equals(patternExpression.variableName())) {
            throw new IllegalStateException("Expected instanceof pattern expression in Java AST");
        }
        JavaAstProgram recordProgram = JavaCstToAst.parse("""
                package demo;
                import java.util.Set;
                public record SubhutiTokenContextConstraint(Set<String> onlyAfter, boolean onlyAtStart) {
                    public SubhutiTokenContextConstraint {
                        onlyAtStart = onlyAtStart;
                    }

                    public boolean hasConstraints() {
                        return onlyAtStart || onlyAfter != null;
                    }
                }
                """);
        if (recordProgram.classes().isEmpty()
                || recordProgram.classes().get(0).fields().size() != 2) {
            throw new IllegalStateException("Expected record components in Java AST");
        }
        JavaAstProgram enumProgram = JavaCstToAst.parse("""
                package demo;
                public enum ErrorType {
                    TOKEN_MISMATCH("Token mismatch");

                    private final String description;

                    ErrorType(String description) {
                        this.description = description;
                    }

                    public String getDescription() {
                        return description;
                    }
                }
                """);
        if (enumProgram.classes().isEmpty()
                || enumProgram.classes().get(0).fields().size() != 2
                || enumProgram.classes().get(0).methods().size() != 2) {
            throw new IllegalStateException("Expected enum fields, constructor, and methods in Java AST");
        }
        System.out.println("QinJavaParserSnippetSmokeTestMain OK");
    }
}
