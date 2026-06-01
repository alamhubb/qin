package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.lang.runtime.QinFunctionModelRegistry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class QinJvmRuntimeFunctionMethodSmokeTestMain {
    private QinJvmRuntimeFunctionMethodSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinFunctionModelRegistry.clear();
        QinFunctionModelRegistry.register("demo:answer-method", QinJvmRuntimeFunctionMethodSmokeTestMain::answerAst);

        QinIrMethodDeclaration answer = new QinIrMethodDeclaration(
                "answer",
                QinIrTypeRef.doubleType(),
                List.of(),
                List.of(),
                null,
                functionDefinition("demo:answer-method"));
        QinIrClassDeclaration declaration = new QinIrClassDeclaration(
                null,
                "RuntimeFunctionService",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(answer));
        QinIrProgram program = new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(declaration));

        byte[] classBytes = new QinJvmDeclarationClassEmitter()
                .compileSingleClass(program, declaration.binaryName());
        Class<?> defined = new ByteArrayClassLoader().define(declaration.binaryName(), classBytes);
        Object instance = defined.getDeclaredConstructor().newInstance();
        Object result = defined.getDeclaredMethod("answer").invoke(instance);
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Unexpected runtime function method result: " + result);
        }

        QinFunctionModelRegistry.clear();
        System.out.println("QinJvmRuntimeFunctionMethodSmokeTestMain passed.");
    }

    private static QinIrObjectLiteral functionDefinition(String astRef) {
        return new QinIrObjectLiteral(List.of(
                new QinIrObjectProperty("__qin_function_model", new QinIrStringLiteral("slime-ast-v1")),
                new QinIrObjectProperty("debugNode", new QinIrStringLiteral("RuntimeFunctionMethodSmoke")),
                new QinIrObjectProperty("astRef", new QinIrStringLiteral(astRef)),
                new QinIrObjectProperty("closure", new QinIrObjectLiteral(List.of()))));
    }

    private static Map<String, Object> answerAst() {
        Map<String, Object> ast = new LinkedHashMap<>();
        ast.put("type", "FunctionExpression");
        ast.put("id", null);
        ast.put("params", List.of());
        ast.put("body", Map.of(
                "type", "BlockStatement",
                "body", List.of(Map.of(
                        "type", "ReturnStatement",
                        "argument", Map.of("type", "Literal", "value", 42.0d)))));
        return ast;
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
