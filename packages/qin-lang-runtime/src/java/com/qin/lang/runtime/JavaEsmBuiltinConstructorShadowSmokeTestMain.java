package com.qin.lang.runtime;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JavaEsmBuiltinConstructorShadowSmokeTestMain {
    private JavaEsmBuiltinConstructorShadowSmokeTestMain() {
    }

    public static void main(String[] args) {
        LinkedHashMap<String, Object> javaMapNamespace = new LinkedHashMap<>();
        javaMapNamespace.put("of", new Object());

        LinkedHashMap<String, Object> definition = new LinkedHashMap<>();
        definition.put("__qin_function_model", "slime-ast-v1");
        definition.put("ast", Map.of(
                "type", "FunctionDeclaration",
                "id", Map.of("type", "Identifier", "name", "makeMap"),
                "params", List.of(),
                "body", Map.of(
                        "type", "BlockStatement",
                        "body", List.of(Map.of(
                                "type", "ReturnStatement",
                                "argument", Map.of(
                                        "type", "NewExpression",
                                        "callee", Map.of("type", "Identifier", "name", "Map"),
                                        "arguments", List.of()))))));
        definition.put("closure", new LinkedHashMap<>(Map.of("Map", javaMapNamespace)));

        Object value = JavaEsmGlobal.__qin_call_function_definition__(definition, null, new Object[0]);
        if (!(value instanceof JavaEsmMapObject)) {
            throw new AssertionError("Expected JS Map constructor despite Java Map namespace shadow, got: " + value);
        }
        System.out.println("JavaEsmBuiltinConstructorShadowSmokeTestMain OK");
    }
}
