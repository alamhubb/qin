package com.qin.lang.runtime;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JavaEsmConstructFunctionDefinitionSmokeTestMain {
    private JavaEsmConstructFunctionDefinitionSmokeTestMain() {
    }

    public static void main(String[] args) {
        LinkedHashMap<String, Object> definition = new LinkedHashMap<>();
        definition.put("__qin_function_model", "slime-ast-v1");
        definition.put("ast", Map.of(
                "type", "FunctionDeclaration",
                "id", Map.of("type", "Identifier", "name", "Ctor"),
                "params", List.of(),
                "body", Map.of("type", "BlockStatement", "body", List.of())));
        definition.put("closure", new LinkedHashMap<String, Object>());

        Object constructed = JavaEsmGlobal.__qin_new__(definition);
        if (constructed == null) {
            throw new AssertionError("Expected constructed instance");
        }
        System.out.println("JavaEsmConstructFunctionDefinitionSmokeTestMain OK");
    }
}
