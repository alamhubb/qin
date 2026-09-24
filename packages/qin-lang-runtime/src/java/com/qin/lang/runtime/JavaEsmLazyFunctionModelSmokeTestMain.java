package com.qin.lang.runtime;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class JavaEsmLazyFunctionModelSmokeTestMain {
    private JavaEsmLazyFunctionModelSmokeTestMain() {
    }

    public static void main(String[] args) {
        QinFunctionModelRegistry.clear();
        AtomicInteger resolveCount = new AtomicInteger();
        QinFunctionModelRegistry.register("lazy-smoke", () -> {
            resolveCount.incrementAndGet();
            return functionAst();
        });

        Map<String, Object> definition = new LinkedHashMap<>();
        definition.put("__qin_function_model", "slime-ast-v1");
        definition.put("astRef", "lazy-smoke");
        definition.put("closure", new LinkedHashMap<String, Object>());

        Object function = JavaEsmGlobal.__qin_make_function__(definition);
        if (function == null) {
            throw new IllegalStateException("Expected interpreted function");
        }
        Object secondFunction = JavaEsmGlobal.__qin_make_function__(definition);
        if (function != secondFunction) {
            throw new IllegalStateException("Expected function model cache hit on repeated construction");
        }
        if (resolveCount.get() != 0) {
            throw new IllegalStateException("Function astRef resolved during construction");
        }

        Map<String, Object> holder = new LinkedHashMap<>();
        holder.put("handler", definition);
        Object firstMember = JavaEsmGlobal.__qin_member_get__(holder, "handler");
        Object secondMember = JavaEsmGlobal.__qin_member_get__(holder, "handler");
        if (firstMember != secondMember) {
            throw new IllegalStateException("Expected member lookup to reuse the interpreted function instance");
        }

        Object result = JavaEsmGlobal.__qin_call_function_definition__(definition, null, new Object[0]);
        if (!"ok".equals(result)) {
            throw new IllegalStateException("Expected lazy function result ok, got: " + result);
        }
        if (resolveCount.get() != 1) {
            throw new IllegalStateException("Expected one lazy astRef resolve, got: " + resolveCount.get());
        }

        System.out.println("JavaEsmLazyFunctionModelSmokeTestMain OK");
    }

    private static Map<String, Object> functionAst() {
        Map<String, Object> id = new LinkedHashMap<>();
        id.put("type", "Identifier");
        id.put("name", "lazyFn");

        Map<String, Object> literal = new LinkedHashMap<>();
        literal.put("type", "Literal");
        literal.put("value", "ok");

        Map<String, Object> returnStatement = new LinkedHashMap<>();
        returnStatement.put("type", "ReturnStatement");
        returnStatement.put("argument", literal);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "BlockStatement");
        body.put("body", List.of(returnStatement));

        Map<String, Object> ast = new LinkedHashMap<>();
        ast.put("type", "FunctionDeclaration");
        ast.put("id", id);
        ast.put("params", List.of());
        ast.put("body", body);
        return ast;
    }
}
