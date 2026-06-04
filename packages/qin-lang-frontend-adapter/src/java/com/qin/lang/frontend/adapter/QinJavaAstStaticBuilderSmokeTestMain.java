package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

public final class QinJavaAstStaticBuilderSmokeTestMain {
    private QinJavaAstStaticBuilderSmokeTestMain() {
    }

    public static void main(String[] args) {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                package com.example;
                class Holder {
                    static class Builder {
                        Holder build() {
                            return new Holder();
                        }
                    }

                    public static Builder builder() {
                        return new Builder();
                    }
                }
                """);
        QinIrClassDeclaration holder = program.classDeclarations().stream()
                .filter(declaration -> "Holder".equals(declaration.simpleName()))
                .findFirst()
                .orElseThrow();
        QinIrMethodDeclaration builder = holder.methods().stream()
                .filter(method -> "builder".equals(method.name()))
                .findFirst()
                .orElseThrow();
        if (!builder.staticMethod()) {
            throw new IllegalStateException("Expected Holder.builder to stay static in Java AST -> Qin IR");
        }
        QinIrProgram recordProgram = new QinJavaAstIrLowerer().lowerSource("""
                package com.example;
                record RecordHolder(String value) {
                    static class Builder {
                        RecordHolder build() {
                            return new RecordHolder("ok");
                        }
                    }

                    public static Builder builder() {
                        return new Builder();
                    }
                }
                """);
        QinIrClassDeclaration recordHolder = recordProgram.classDeclarations().stream()
                .filter(declaration -> "RecordHolder".equals(declaration.simpleName()))
                .findFirst()
                .orElseThrow();
        QinIrMethodDeclaration recordBuilder = recordHolder.methods().stream()
                .filter(method -> "builder".equals(method.name()))
                .findFirst()
                .orElseThrow();
        if (!recordBuilder.staticMethod()) {
            throw new IllegalStateException("Expected RecordHolder.builder to stay static in Java AST -> Qin IR");
        }
        System.out.println("QinJavaAstStaticBuilderSmokeTestMain OK");
    }
}
