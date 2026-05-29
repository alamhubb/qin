package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;
import com.qin.lang.runtime.QinFunctionModelRegistry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class QinFunctionModelRegistrySmokeTestMain {
    private QinFunctionModelRegistrySmokeTestMain() {
    }

    public static void main(String[] args) {
        QinFunctionModelRegistry.clear();
        QinFunctionModelRegistry.register("demo:add-one", QinFunctionModelRegistrySmokeTestMain::addOneAst);

        Map<String, Object> definition = new LinkedHashMap<>();
        definition.put("__qin_function_model", "slime-ast-v1");
        definition.put("debugNode", "ExternalFunctionModelSmoke");
        definition.put("astRef", "demo:add-one");
        definition.put("closure", Map.of());

        Object function = JavaEsmGlobal.__qin_make_function__(definition);
        Object result = JavaEsmGlobal.__qin_call__(function, 41);
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42.0 from external function model, got: " + result);
        }

        QinFunctionModelRegistry.clear();
        System.out.println("QinFunctionModelRegistrySmokeTestMain passed.");
    }

    private static Map<String, Object> addOneAst() {
        Map<String, Object> ast = new LinkedHashMap<>();
        ast.put("type", "FunctionExpression");
        ast.put("id", null);
        ast.put("params", List.of(identifier("value")));
        ast.put("body", Map.of(
                "type", "BlockStatement",
                "body", List.of(Map.of(
                        "type", "ReturnStatement",
                        "argument", Map.of(
                                "type", "BinaryExpression",
                                "operator", "+",
                                "left", identifier("value"),
                                "right", literal(1.0d))))));
        return ast;
    }

    private static Map<String, Object> identifier(String name) {
        return Map.of("type", "Identifier", "name", name);
    }

    private static Map<String, Object> literal(Object value) {
        return Map.of("type", "Literal", "value", value);
    }
}
