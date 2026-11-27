package com.qin.lang.runtime;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class JavaEsmPrototypeChainSmokeTestMain {
    private JavaEsmPrototypeChainSmokeTestMain() {
    }

    public static void main(String[] args) {
        Object parentClass = JavaEsmGlobal.__qin_make_function__(
                functionDefinition(classAst("Parent", null, "parentRule"), Map.of()));
        Object childClass = JavaEsmGlobal.__qin_make_function__(
                functionDefinition(classAst("Child", "Parent", "childRule"), Map.of("Parent", parentClass)));

        Object childPrototype = JavaEsmGlobal.__qin_member_get__(childClass, "prototype");
        if (!(childPrototype instanceof Map<?, ?> rawChildPrototype)) {
            throw new AssertionError("Child prototype should be a map");
        }
        Map<String, Object> childPrototypeMap = castMap(rawChildPrototype);
        if (childPrototypeMap.containsKey("parentRule")) {
            throw new AssertionError("Child prototype should not eagerly copy parent prototype methods");
        }
        Object inherited = JavaEsmGlobal.__qin_member_get__(childPrototype, "parentRule");
        if (inherited == null) {
            throw new AssertionError("Child prototype should resolve parent method through prototype chain");
        }
        Object keys = JavaEsmObject.keys(childPrototype);
        if (keys instanceof List<?> list
                && list.contains(JavaEsmGlobal.RUNTIME_HIDDEN_KEY_PREFIX + "prototype_parent__")) {
            throw new AssertionError("Runtime prototype link should not be enumerable");
        }
        verifyFunctionsWithoutSuperClass();
        System.out.println("JavaEsmPrototypeChainSmokeTestMain OK");
    }

    private static void verifyFunctionsWithoutSuperClass() {
        Map<String, Object> value = Map.of("type", "Literal", "value", "ok");
        Map<String, Object> arrow = Map.of("type", "ArrowFunctionExpression", "params", List.of(), "body", value);
        Map<String, Object> outer = Map.of(
                "type", "FunctionExpression", "params", List.of(),
                "body", Map.of("type", "BlockStatement", "body", List.of(
                        Map.of("type", "ReturnStatement", "argument", arrow))));
        Thread thread = Thread.currentThread();
        ClassLoader previousLoader = thread.getContextClassLoader();
        AtomicInteger nullClassLookups = new AtomicInteger();
        thread.setContextClassLoader(new ClassLoader(previousLoader) {
            @Override
            protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                if ("null".equals(name)) {
                    nullClassLookups.incrementAndGet();
                }
                return super.loadClass(name, resolve);
            }
        });
        try {
            Object factory = JavaEsmGlobal.__qin_make_function__(functionDefinition(outer, Map.of()));
            Object closure = JavaEsmGlobal.__qin_call__(factory);
            if (!"ok".equals(JavaEsmGlobal.__qin_call__(closure))) {
                throw new AssertionError("Expected the nested arrow to return ok");
            }
            Object baseClass = JavaEsmGlobal.__qin_make_function__(
                    functionDefinition(classAst("Base", null, "baseRule"), Map.of()));
            Object basePrototype = JavaEsmGlobal.__qin_member_get__(baseClass, "prototype");
            if (JavaEsmGlobal.__qin_member_get__(basePrototype, "baseRule") == null) {
                throw new AssertionError("Expected a method on the base class prototype");
            }
        } finally {
            thread.setContextClassLoader(previousLoader);
        }
        if (nullClassLookups.get() != 0) {
            throw new AssertionError("Unexpected class lookups for absent superclass: " + nullClassLookups.get());
        }
    }

    private static Map<String, Object> functionDefinition(
            Map<String, Object> ast,
            Map<String, Object> closure) {
        LinkedHashMap<String, Object> definition = new LinkedHashMap<>();
        definition.put("ast", ast);
        definition.put("closure", new LinkedHashMap<>(closure));
        return definition;
    }

    private static Map<String, Object> classAst(String name, String superName, String methodName) {
        LinkedHashMap<String, Object> ast = new LinkedHashMap<>();
        ast.put("type", "ClassDeclaration");
        ast.put("id", identifier(name));
        ast.put("superClass", superName == null ? null : identifier(superName));
        ast.put("decorators", List.of());
        ast.put("body", Map.of("type", "ClassBody", "body", List.of(method(methodName))));
        return ast;
    }

    private static Map<String, Object> method(String name) {
        LinkedHashMap<String, Object> method = new LinkedHashMap<>();
        method.put("type", "MethodDefinition");
        method.put("kind", "method");
        method.put("static", false);
        method.put("computed", false);
        method.put("key", identifier(name));
        method.put("decorators", List.of());
        method.put("value", functionExpression(name));
        return method;
    }

    private static Map<String, Object> functionExpression(String name) {
        LinkedHashMap<String, Object> function = new LinkedHashMap<>();
        function.put("type", "FunctionExpression");
        function.put("id", identifier(name));
        function.put("params", List.of());
        function.put("body", Map.of("type", "BlockStatement", "body", List.of()));
        return function;
    }

    private static Map<String, Object> identifier(String name) {
        return Map.of("type", "Identifier", "name", name);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> castMap(Object value) {
        return (Map<String, Object>) value;
    }
}
