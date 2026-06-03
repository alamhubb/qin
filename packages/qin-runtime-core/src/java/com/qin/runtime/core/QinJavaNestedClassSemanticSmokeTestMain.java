package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinJavaSemanticAnalyzer;
import com.qin.lang.frontend.adapter.QinJavaSemanticModel;
import com.slime.java.ast.JavaAstProgram;
import com.slime.java.ast.JavaCstToAst;

public final class QinJavaNestedClassSemanticSmokeTestMain {
    private QinJavaNestedClassSemanticSmokeTestMain() {
    }

    public static void main(String[] args) {
        String source = """
                package demo;

                import java.util.ArrayList;
                import java.util.List;
                import java.util.Set;

                public class BaseBox {
                    protected static final Set<String> RESERVED_WORDS = Set.of("class");
                    protected final List<Token> inheritedTokens = new ArrayList<>();
                }

                public class Box extends BaseBox {
                    private static final class Token {
                    }

                    private static final class Inner {
                        private final int value;
                        private final List<Token> tokens;

                        private Inner(int value, List<Token> tokens) {
                            this.value = value;
                            this.tokens = tokens;
                        }

                        private boolean matches(Token token) {
                            return tokens.contains(token);
                        }
                    }

                    int read() {
                        Inner inner = new Inner(7, new ArrayList<>());
                        return inner.value;
                    }

                    void copy(Inner inner, List<Token> target) {
                        target.addAll(inner.tokens);
                    }

                    boolean has(List<Inner> inners, Token token) {
                        return inners.stream().anyMatch(inner -> inner.matches(token));
                    }

                    boolean reserved(String value) {
                        return RESERVED_WORDS.contains(value);
                    }

                    boolean inherited(Token token) {
                        return inheritedTokens.contains(token);
                    }
                }
                """;
        JavaAstProgram ast = JavaCstToAst.parse(source);
        require(ast.classes().get(1).nestedClasses().size() == 2, "nested class count");
        QinJavaSemanticModel model = new QinJavaSemanticAnalyzer().analyzeSource(source);
        require(model.classes().size() == 4, "semantic class count");
        require("demo.Box$Token".equals(model.classes().get(2).binaryName()), "first nested binary name");
        require("demo.Box$Inner".equals(model.classes().get(3).binaryName()), "second nested binary name");
        System.out.println("QinJavaNestedClassSemanticSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
