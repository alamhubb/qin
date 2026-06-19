package com.qin.lang.runtime;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Minimal JS-like global runtime used by JVM-emitted Qin programs.
 */
public final class JavaEsmGlobal {
    private static final String SUBHUTI_ALTERNATIVE_CLASS_NAME = "com.subhuti.parser.Alternative";
    private static final Set<String> SLIME_PARSER_PARAM_BOUNDARY_KEYS =
            Set.of("In", "Yield", "Await", "Return", "Default");
    static final String RUNTIME_HIDDEN_KEY_PREFIX = "__qin_runtime_";
    private static final String PROTOTYPE_PARENT_KEY = RUNTIME_HIDDEN_KEY_PREFIX + "prototype_parent__";
    private static final Map<String, Object> GLOBAL_BINDINGS = new NullFriendlyConcurrentMap();
    private static final Map<String, Object> GLOBAL_OBJECT = new NullFriendlyConcurrentMap();
    private static final Object FIELD_LOOKUP_MISS = new Object();
    private static final Map<FieldLookupKey, Object> FIELD_LOOKUP_CACHE = new ConcurrentHashMap<>();
    private static final Object METHOD_LOOKUP_MISS = new Object();
    private static final Map<Class<?>, List<Method>> METHOD_CANDIDATE_CACHE = new ConcurrentHashMap<>();
    private static final Map<MethodCandidateKey, List<Method>> METHOD_CANDIDATE_BY_NAME_CACHE = new ConcurrentHashMap<>();
    private static final Map<MethodLookupKey, Object> METHOD_LOOKUP_CACHE = new ConcurrentHashMap<>();
    private static final Map<TypedMethodLookupKey, Object> TYPED_METHOD_LOOKUP_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, JavaRecordInfo> JAVA_RECORD_INFO_CACHE = new ConcurrentHashMap<>();
    private static final Object UNRESOLVED_MODULE_REF = new Object();
    private static final Set<String> ERROR_CONSTRUCTORS =
            Set.of("Error", "TypeError", "RangeError", "ReferenceError", "SyntaxError");
    private static final Map<String, List<ModuleFieldRef>> MODULE_REFS = new ConcurrentHashMap<>();
    private static final Map<ModuleFieldKey, List<ModuleFieldRef>> MODULE_REFS_BY_FIELD = new ConcurrentHashMap<>();
    private static final Map<SerializedModuleFieldKey, List<ModuleFieldRef>> MODULE_REFS_BY_SERIALIZED_FIELD =
            new ConcurrentHashMap<>();
    private static final StackWalker CALLER_CLASS_WALKER =
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private static final ThreadLocal<List<String>> CONSTRUCT_STACK =
            ThreadLocal.withInitial(ArrayList::new);
    private static final ThreadLocal<List<String>> INTERPRETED_CALL_STACK =
            ThreadLocal.withInitial(ArrayList::new);
    private static final ThreadLocal<List<Object>> INTERPRETED_THIS_STACK =
            ThreadLocal.withInitial(ArrayList::new);
    private static final ThreadLocal<Long> INTERPRETED_CALL_COUNT =
            ThreadLocal.withInitial(() -> 0L);
    private static volatile long interpretedCallCountLimit;
    private static final int MAX_INTERPRETED_CALL_DEPTH =
            Integer.getInteger("qin.runtime.maxInterpretedCallDepth", 2048);

    static {
        installGlobalObjectBuiltins();
    }

    private JavaEsmGlobal() {
    }

    private static void installGlobalObjectBuiltins() {
        GLOBAL_OBJECT.put("__qin_builtin_constructor__",
                methodHandle(JavaEsmGlobal.class, "__qin_builtin_constructor__", Object.class));
        GLOBAL_OBJECT.put("__qin_java_pattern_regexp__",
                methodHandle(JavaEsmGlobal.class, "__qin_java_pattern_regexp__", Object.class, Object.class));
        for (String constructor : List.of(
                "Array", "Object", "Map", "Set", "WeakMap", "WeakSet", "Proxy", "Promise", "Symbol",
                "Date", "String", "Boolean", "Number",
                "Uint8Array", "Uint16Array", "Uint32Array", "TextDecoder", "URLSearchParams",
                "RegExp", "Error", "TypeError", "RangeError", "ReferenceError", "SyntaxError")) {
            GLOBAL_OBJECT.put(constructor, constructor);
        }
    }

    public static void setInterpretedCallCountLimit(long maxCalls) {
        interpretedCallCountLimit = Math.max(0L, maxCalls);
        INTERPRETED_CALL_COUNT.set(0L);
    }

    public static void clearInterpretedCallCountLimit() {
        interpretedCallCountLimit = 0L;
        INTERPRETED_CALL_COUNT.remove();
    }

    public static Object parseInt(Object value) {
        return parseInt(value, 10);
    }

    public static Object parseInt(Object value, Object radix) {
        String text = String.valueOf(value).trim();
        int base = radix instanceof Number number ? number.intValue() : 10;
        if (text.isEmpty()) {
            return Double.NaN;
        }
        try {
            return (double) Integer.parseInt(text, base);
        } catch (NumberFormatException error) {
            return Double.NaN;
        }
    }

    public static Object parseFloat(Object value) {
        String text = String.valueOf(value).trim();
        if (text.isEmpty()) {
            return Double.NaN;
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException error) {
            return Double.NaN;
        }
    }

    public static Object isNaN(Object value) {
        Double number = asNumber(value);
        return number == null || number.isNaN();
    }

    public static Object isFinite(Object value) {
        Double number = asNumber(value);
        return number != null && Double.isFinite(number);
    }

    public static Object __qin_make_function__(Object definition) {
        if (definition instanceof QinCallable callable) {
            return callable;
        }
        if (definition instanceof Map<?, ?> definitionMap) {
            return new InterpretedFunction(castMap(definitionMap));
        }
        throw new IllegalArgumentException("Unsupported function definition: " + simpleName(definition));
    }

    public static Object __qin_call_function_definition__(Object definition, Object thisArg, Object[] args) {
        Object function = bindRuntimeCallableThis(__qin_make_function__(definition), thisArg);
        return callAny(function, args == null ? new Object[0] : args);
    }

    public static Object __qin_constant_return_function__(Object value) {
        return new NativeFunction("constant", args -> value);
    }

    public static Object __qin_builtin_constructor__(Object name) {
        String builtinName = String.valueOf(name);
        if (isBuiltinConstructorName(builtinName)) {
            return builtinName;
        }
        throw new IllegalArgumentException("Unsupported JS builtin constructor: " + builtinName);
    }

    private static boolean isBuiltinConstructorName(String name) {
        return switch (name) {
            case "Array", "Object", "Map", "Set", "WeakMap", "WeakSet", "Proxy", "Promise", "Symbol",
                    "Date", "String", "Boolean", "Number",
                    "Uint8Array", "Uint16Array", "Uint32Array", "TextDecoder", "URLSearchParams",
                    "RegExp", "Error", "TypeError", "RangeError", "ReferenceError", "SyntaxError" -> true;
            default -> false;
        };
    }

    public static Object __qin_java_pattern_regexp__(Object source, Object flags) {
        return JavaEsmRegExp.fromJavaPattern(source, flags);
    }

    public static Object __qin_global__(Object name) {
        String globalName = String.valueOf(name);
        Object bound = GLOBAL_BINDINGS.get(globalName);
        if (bound != null) {
            return bound;
        }
        return switch (globalName) {
            case "globalThis", "global", "window", "self" -> GLOBAL_OBJECT;
            case "console" -> JavaEsmConsole.class;
            case "fs", "node:fs" -> NodeHostRuntime.fsNamespace();
            case "path", "node:path" -> NodeHostRuntime.pathNamespace();
            case "url", "node:url" -> NodeHostRuntime.urlNamespace();
            case "util", "node:util" -> NodeHostRuntime.utilNamespace();
            case "process", "node:process" -> NodeHostRuntime.processNamespace();
            case "module", "node:module" -> NodeHostRuntime.moduleNamespace();
            case "crypto", "node:crypto" -> NodeHostRuntime.cryptoNamespace();
            case "tty", "node:tty" -> NodeHostRuntime.ttyNamespace();
            case "diagnostics_channel", "node:diagnostics_channel" -> NodeHostRuntime.diagnosticsChannelNamespace();
            case "performance" -> JavaEsmPerformance.class;
            case "parseInt" -> methodHandle(JavaEsmGlobal.class, "parseInt", Object.class, Object.class);
            case "parseFloat" -> methodHandle(JavaEsmGlobal.class, "parseFloat", Object.class);
            case "isNaN" -> methodHandle(JavaEsmGlobal.class, "isNaN", Object.class);
            case "isFinite" -> methodHandle(JavaEsmGlobal.class, "isFinite", Object.class);
            case "__qin_builtin_constructor__" ->
                    methodHandle(JavaEsmGlobal.class, "__qin_builtin_constructor__", Object.class);
            case "__qin_java_pattern_regexp__" ->
                    methodHandle(JavaEsmGlobal.class, "__qin_java_pattern_regexp__", Object.class, Object.class);
            case "globalthis" -> methodHandle(NodeHostRuntime.class, "globalThis");
            case "node:fs.default" -> NodeHostRuntime.fsNamespace();
            case "node:path.default" -> NodeHostRuntime.pathNamespace();
            case "node:crypto.default" -> NodeHostRuntime.cryptoNamespace();
            case "node:util.default" -> NodeHostRuntime.utilNamespace();
            case "node:module.default" -> NodeHostRuntime.moduleNamespace();
            case "node:tty.default" -> NodeHostRuntime.ttyNamespace();
            case "node:fs.readFileSync" -> methodHandle(NodeHostRuntime.class, "readFileSync", Object.class, Object.class);
            case "node:fs.existsSync" -> methodHandle(NodeHostRuntime.class, "existsSync", Object.class);
            case "node:fs.writeFileSync" -> methodHandle(NodeHostRuntime.class, "writeFileSync", Object.class, Object.class);
            case "node:fs.appendFileSync" -> methodHandle(NodeHostRuntime.class, "appendFileSync", Object.class, Object.class);
            case "node:fs.mkdirSync" -> methodHandle(NodeHostRuntime.class, "mkdirSync", Object.class);
            case "node:fs.createWriteStream" -> methodHandle(NodeHostRuntime.class, "createWriteStream", Object.class);
            case "node:path.dirname" -> methodHandle(NodeHostRuntime.class, "dirname", Object.class);
            case "node:path.relative" -> methodHandle(NodeHostRuntime.class, "relative", Object.class, Object.class);
            case "node:path.join" -> methodHandle(NodeHostRuntime.class, "join", Object[].class);
            case "node:path.resolve" -> methodHandle(NodeHostRuntime.class, "resolve", Object[].class);
            case "node:url.fileURLToPath" -> methodHandle(NodeHostRuntime.class, "fileURLToPath", Object.class);
            case "node:module.createRequire" -> methodHandle(NodeHostRuntime.class, "createRequire", Object.class);
            case "node:crypto.hash" -> methodHandle(NodeHostRuntime.class, "hash", Object.class, Object.class, Object.class);
            case "node:tty.isatty" -> methodHandle(NodeHostRuntime.class, "isatty", Object.class);
            case "node:util.deprecate" -> methodHandle(NodeHostRuntime.class, "deprecate", Object.class, Object.class);
            case "node:util.formatWithOptions" -> methodHandle(NodeHostRuntime.class, "formatWithOptions", Object.class, Object[].class);
            case "node:util.inspect" -> methodHandle(NodeHostRuntime.class, "inspect", Object.class);
            case "node:process.cwd" -> methodHandle(NodeHostRuntime.class, "cwd");
            case "node:diagnostics_channel.channel" -> methodHandle(NodeHostRuntime.class, "channel", Object.class);
            case "node:diagnostics_channel.tracingChannel" -> methodHandle(NodeHostRuntime.class, "tracingChannel", Object.class);
            // The current linked-source emitter lowers host named imports to bare global lookups.
            // Keep these aliases until host import lowering preserves module-qualified names.
            case "createRequire" -> methodHandle(NodeHostRuntime.class, "createRequire", Object.class);
            case "readFileSync" -> methodHandle(NodeHostRuntime.class, "readFileSync", Object.class, Object.class);
            case "existsSync" -> methodHandle(NodeHostRuntime.class, "existsSync", Object.class);
            case "writeFileSync" -> methodHandle(NodeHostRuntime.class, "writeFileSync", Object.class, Object.class);
            case "appendFileSync" -> methodHandle(NodeHostRuntime.class, "appendFileSync", Object.class, Object.class);
            case "mkdirSync" -> methodHandle(NodeHostRuntime.class, "mkdirSync", Object.class);
            case "createWriteStream" -> methodHandle(NodeHostRuntime.class, "createWriteStream", Object.class);
            case "dirname" -> methodHandle(NodeHostRuntime.class, "dirname", Object.class);
            case "relative" -> methodHandle(NodeHostRuntime.class, "relative", Object.class, Object.class);
            case "join" -> methodHandle(NodeHostRuntime.class, "join", Object[].class);
            case "resolve" -> methodHandle(NodeHostRuntime.class, "resolve", Object[].class);
            case "fileURLToPath" -> methodHandle(NodeHostRuntime.class, "fileURLToPath", Object.class);
            case "hash" -> methodHandle(NodeHostRuntime.class, "hash", Object.class, Object.class, Object.class);
            case "isatty" -> methodHandle(NodeHostRuntime.class, "isatty", Object.class);
            case "deprecate" -> methodHandle(NodeHostRuntime.class, "deprecate", Object.class, Object.class);
            case "formatWithOptions" -> methodHandle(NodeHostRuntime.class, "formatWithOptions", Object.class, Object[].class);
            case "inspect" -> methodHandle(NodeHostRuntime.class, "inspect", Object.class);
            case "cwd" -> methodHandle(NodeHostRuntime.class, "cwd");
            case "channel" -> methodHandle(NodeHostRuntime.class, "channel", Object.class);
            case "tracingChannel" -> methodHandle(NodeHostRuntime.class, "tracingChannel", Object.class);
            case "Math", "JSON", "Number", "Object", "Array", "Map", "Set", "Proxy", "Promise", "Symbol",
                    "WeakMap", "WeakSet", "Date", "String", "Boolean",
                    "Uint8Array", "Uint16Array", "Uint32Array", "TextDecoder", "URLSearchParams",
                    "RegExp", "Error", "TypeError", "RangeError", "ReferenceError", "SyntaxError" -> globalName;
            case "Infinity" -> Double.POSITIVE_INFINITY;
            case "NaN" -> Double.NaN;
            case "undefined" -> null;
            default -> null;
        };
    }

    public static Object __qin_register_js_import__(Object localName, Object moduleName, Object importedName) {
        String local = String.valueOf(localName);
        String module = String.valueOf(moduleName);
        String imported = String.valueOf(importedName);
        if (local.isBlank()) {
            return null;
        }
        Object value = resolveRuntimeJsImport(module, imported);
        GLOBAL_BINDINGS.put(local, value);
        GLOBAL_OBJECT.put(local, value);
        return value;
    }

    public static Object __qin_bind_global__(Object name, Object value) {
        String key = String.valueOf(name);
        GLOBAL_BINDINGS.put(key, value);
        GLOBAL_OBJECT.put(key, value);
        return value;
    }

    public static Object __qin_declare_global__(Object name) {
        String key = String.valueOf(name);
        GLOBAL_BINDINGS.putIfAbsent(key, null);
        GLOBAL_OBJECT.putIfAbsent(key, null);
        return null;
    }

    public static Object __qin_bind_module_ref__(Object name, Object fieldName) {
        String key = String.valueOf(name);
        String field = String.valueOf(fieldName);
        Class<?> callerClass = CALLER_CLASS_WALKER.getCallerClass();
        ModuleFieldRef ref = new ModuleFieldRef(callerClass, field);
        MODULE_REFS.computeIfAbsent(key, ignored -> Collections.synchronizedList(new ArrayList<>()))
                .add(ref);
        MODULE_REFS_BY_FIELD.computeIfAbsent(new ModuleFieldKey(callerClass, field),
                        ignored -> Collections.synchronizedList(new ArrayList<>()))
                .add(ref);
        MODULE_REFS_BY_SERIALIZED_FIELD.computeIfAbsent(new SerializedModuleFieldKey(callerClass.getName(), field),
                        ignored -> Collections.synchronizedList(new ArrayList<>()))
                .add(ref);
        return null;
    }

    public static Object __qin_ref_descriptor__(Object name, Object fieldName) {
        Class<?> callerClass = CALLER_CLASS_WALKER.getCallerClass();
        LinkedHashMap<String, Object> descriptor = new LinkedHashMap<>();
        descriptor.put("__qin_ref_name", name);
        descriptor.put("__qin_ref_owner", callerClass.getName());
        descriptor.put("__qin_ref_field", String.valueOf(fieldName));
        descriptor.put("__qin_ref", new ModuleFieldRef(callerClass, String.valueOf(fieldName)));
        return descriptor;
    }

    public static Object __qin_mark_module_ref_initialized__(Object name, Object fieldName) {
        String field = String.valueOf(fieldName);
        Class<?> callerClass = CALLER_CLASS_WALKER.getCallerClass();
        List<ModuleFieldRef> refs = MODULE_REFS_BY_FIELD.get(new ModuleFieldKey(callerClass, field));
        if (refs != null) {
            synchronized (refs) {
                for (ModuleFieldRef ref : refs) {
                    ref.markInitialized();
                }
            }
        }
        return null;
    }

    public static Object __qin_module_ref_get__(Object name) {
        Object value = resolveModuleReference(name);
        if (value != UNRESOLVED_MODULE_REF) {
            return unwrapExportSlotValue(value);
        }
        Object global = __qin_global__(name);
        if (global != null) {
            return unwrapExportSlotValue(global);
        }
        throw new IllegalStateException("Unresolved module reference: " + name);
    }

    private static Object resolveRuntimeJsImport(String moduleName, String importedName) {
        if (moduleName == null || moduleName.isBlank()) {
            throw new IllegalArgumentException("Runtime JS import module cannot be empty");
        }
        String imported = importedName == null ? "" : importedName;
        if ("*".equals(imported) || imported.isBlank()) {
            Object namespace = __qin_global__(moduleName);
            if (namespace == null) {
                throw new IllegalArgumentException("Unsupported runtime JS import namespace: " + moduleName);
            }
            return namespace;
        }
        if ("default".equals(imported)) {
            Object defaultValue = __qin_global__(moduleName + ".default");
            if (defaultValue != null) {
                return defaultValue;
            }
        }
        Object value = __qin_global__(moduleName + "." + imported);
        if (value == null && "node:diagnostics_channel".equals(moduleName)) {
            throw new IllegalArgumentException("Unsupported node:diagnostics_channel import: " + imported);
        }
        if (value == null) {
            throw new IllegalArgumentException("Unsupported runtime JS import: " + moduleName + "." + imported);
        }
        return value;
    }


    public static Object __qin_assign__(Object name, Object value) {
        String key = String.valueOf(name);
        if (assignModuleReference(key, value)
                || GLOBAL_BINDINGS.containsKey(key)
                || GLOBAL_OBJECT.containsKey(key)) {
            GLOBAL_BINDINGS.put(key, value);
            GLOBAL_OBJECT.put(key, value);
            return value;
        }
        throw new IllegalArgumentException("Unknown assignment target: " + key);
    }

    public static Object __qin_export_slot__() {
        return new ExportSlot();
    }

    public static Object __qin_export_init__(Object slot, Object value) {
        if (slot instanceof ExportSlot exportSlot) {
            exportSlot.value = value;
        }
        return value;
    }

    public static Object __qin_export_get__(Object slot) {
        if (slot instanceof ExportSlot exportSlot) {
            return exportSlot.value;
        }
        return null;
    }

    public static Object __qin_value__(Object value) {
        return unwrapExportSlotValue(value);
    }

    public static Object __qin_call__(Object callable) {
        return callAny(callable);
    }

    public static Object __qin_call__(Object callable, Object arg0) {
        return callAny(callable, arg0);
    }

    public static Object __qin_call__(Object callable, Object arg0, Object arg1) {
        return callAny(callable, arg0, arg1);
    }

    public static Object __qin_call__(Object callable, Object arg0, Object arg1, Object arg2) {
        return callAny(callable, arg0, arg1, arg2);
    }

    public static Object __qin_call__(Object callable, Object arg0, Object arg1, Object arg2, Object arg3) {
        return callAny(callable, arg0, arg1, arg2, arg3);
    }

    public static Object __qin_call_array__(Object callable, Object[] args) {
        return callAny(callable, args);
    }

    public static Object __qin_call_method__(Object target, Object methodName) {
        return callMethod(target, methodName);
    }

    public static Object __qin_call_method__(Object target, Object methodName, Object arg0) {
        return callMethod(target, methodName, arg0);
    }

    public static Object __qin_call_method__(Object target, Object methodName, Object arg0, Object arg1) {
        return callMethod(target, methodName, arg0, arg1);
    }

    public static Object __qin_call_method__(Object target, Object methodName, Object arg0, Object arg1, Object arg2) {
        return callMethod(target, methodName, arg0, arg1, arg2);
    }

    public static Object __qin_call_method__(
            Object target,
            Object methodName,
            Object arg0,
            Object arg1,
            Object arg2,
            Object arg3) {
        return callMethod(target, methodName, arg0, arg1, arg2, arg3);
    }

    public static Object __qin_call_method_array__(Object target, Object methodName, Object[] args) {
        return callMethod(target, methodName, args);
    }

    public static Object __qin_optional_call_method__(Object target, Object methodName) {
        return optionalCallMethod(target, methodName);
    }

    public static Object __qin_optional_call_method__(Object target, Object methodName, Object arg0) {
        return optionalCallMethod(target, methodName, arg0);
    }

    public static Object __qin_optional_call_method__(Object target, Object methodName, Object arg0, Object arg1) {
        return optionalCallMethod(target, methodName, arg0, arg1);
    }

    public static Object __qin_optional_call_method__(
            Object target,
            Object methodName,
            Object arg0,
            Object arg1,
            Object arg2) {
        return optionalCallMethod(target, methodName, arg0, arg1, arg2);
    }

    public static Object __qin_optional_call_method__(
            Object target,
            Object methodName,
            Object arg0,
            Object arg1,
            Object arg2,
            Object arg3) {
        return optionalCallMethod(target, methodName, arg0, arg1, arg2, arg3);
    }

    public static Object __qin_optional_call_method_array__(Object target, Object methodName, Object[] args) {
        return optionalCallMethod(target, methodName, args);
    }

    public static Object __qin_binary__(Object operator, Object left, Object right) {
        String op = String.valueOf(operator);
        Double leftNumber = asNumber(left);
        Double rightNumber = asNumber(right);
        return switch (op) {
            case "+" -> left instanceof String || right instanceof String
                    ? jsString(left) + jsString(right)
                    : leftNumber != null && rightNumber != null
                            ? leftNumber + rightNumber
                            : jsString(left) + jsString(right);
            case "-" -> jsNumericOperand(left) - jsNumericOperand(right);
            case "*" -> jsNumericOperand(left) * jsNumericOperand(right);
            case "/" -> jsNumericOperand(left) / jsNumericOperand(right);
            case "%" -> jsNumericOperand(left) % jsNumericOperand(right);
            case "|" -> (double) (toInt32(left) | toInt32(right));
            case "&" -> (double) (toInt32(left) & toInt32(right));
            case "^" -> (double) (toInt32(left) ^ toInt32(right));
            case "<<" -> (double) (toInt32(left) << (toInt32(right) & 0x1f));
            case ">>" -> (double) (toInt32(left) >> (toInt32(right) & 0x1f));
            case ">>>" -> (double) Integer.toUnsignedLong(toInt32(left) >>> (toInt32(right) & 0x1f));
            case "==" -> looseEquals(left, right);
            case "!=" -> !looseEquals(left, right);
            case "===" -> strictEquals(left, right);
            case "!==" -> !strictEquals(left, right);
            case "<" -> jsRelationalCompare(op, left, right);
            case "<=" -> jsRelationalCompare(op, left, right);
            case ">" -> jsRelationalCompare(op, left, right);
            case ">=" -> jsRelationalCompare(op, left, right);
            case "in" -> jsIn(left, right);
            case "instanceof" -> jsInstanceOf(left, right);
            default -> throw new IllegalArgumentException("Unsupported binary operator: " + op);
        };
    }

    private static boolean jsInstanceOf(Object value, Object constructor) {
        if (constructor == null) {
            throw new IllegalArgumentException("Right-hand side of 'instanceof' is not callable");
        }
        constructor = unwrapExportSlotValue(constructor);
        if (constructor instanceof String builtinName) {
            return jsBuiltinInstanceOf(value, builtinName);
        }
        if (constructor instanceof InterpretedFunction interpretedFunction) {
            if (value instanceof JavaRuntimeThrowable throwable) {
                return throwable.isInstanceOf(interpretedFunction.classDebugName());
            }
            Object prototype = interpretedFunction.get("prototype");
            return value instanceof InterpretedInstance instance && instance.hasPrototypeObject(prototype);
        }
        if (constructor instanceof Class<?> clazz) {
            return value != null && clazz.isInstance(value);
        }
        if (constructor instanceof NativeFunction || constructor instanceof QinCallable || constructor instanceof Method) {
            return false;
        }
        throw new IllegalArgumentException("Right-hand side of 'instanceof' is not callable: "
                + simpleName(constructor));
    }

    private static boolean jsBuiltinInstanceOf(Object value, String builtinName) {
        return switch (builtinName) {
            case "Object" -> isObjectLike(value);
            case "Array" -> value instanceof List<?> || value instanceof JavaEsmArrayObject
                    || value != null && value.getClass().isArray();
            case "Map", "WeakMap" -> value instanceof JavaEsmMapObject;
            case "Set", "WeakSet" -> value instanceof JavaEsmSetObject;
            case "RegExp" -> value instanceof JavaEsmRegExp;
            case "Date" -> value instanceof JavaEsmDate;
            case "URLSearchParams" -> value instanceof JavaEsmUrlSearchParams;
            case "Uint8Array", "Uint16Array", "Uint32Array" -> value instanceof JavaEsmTypedArray;
            case "Error", "TypeError", "RangeError", "ReferenceError", "SyntaxError" ->
                    isErrorObjectInstance(value, builtinName);
            default -> false;
        };
    }

    private static boolean isErrorObjectInstance(Object value, String constructorName) {
        if (!(value instanceof Map<?, ?> rawMap)) {
            return false;
        }
        Object rawName = rawMap.get("name");
        if (!(rawName instanceof String errorName) || !ERROR_CONSTRUCTORS.contains(errorName)) {
            return false;
        }
        return "Error".equals(constructorName) || constructorName.equals(errorName);
    }

    private static boolean jsIn(Object property, Object target) {
        if (target == null) {
            throw new IllegalArgumentException("Right-hand side of 'in' cannot be null");
        }
        String key = propertyKey(property);
        if (target instanceof QinRuntimeObject runtimeObject) {
            return runtimeObject.has(property);
        }
        if (target instanceof Map<?, ?> map) {
            Map<String, Object> cast = castMap(map);
            return cast.containsKey(key) && !isRuntimeHiddenObjectKey(key)
                    || mapPrototypeChainHas(cast, key);
        }
        if (target instanceof JavaEsmMapObject mapObject) {
            return mapObject.has(property);
        }
        if (target instanceof List<?> list) {
            if ("length".equals(key)) {
                return true;
            }
            int index = toIndex(property);
            return index >= 0 && index < list.size();
        }
        if (target.getClass().isArray()) {
            if ("length".equals(key)) {
                return true;
            }
            int index = toIndex(property);
            return index >= 0 && index < Array.getLength(target);
        }
        if (tryReadField(target, key) != null) {
            return true;
        }
        Class<?> ownerClass = target instanceof Class<?> clazz ? clazz : target.getClass();
        return findCompatibleMethod(ownerClass, key, 0, target instanceof Class<?>) != null;
    }

    private static boolean strictEquals(Object left, Object right) {
        if (left instanceof Number leftNumber && right instanceof Number rightNumber) {
            double leftValue = leftNumber.doubleValue();
            double rightValue = rightNumber.doubleValue();
            return !Double.isNaN(leftValue) && !Double.isNaN(rightValue) && leftValue == rightValue;
        }
        return sameValueZero(left, right);
    }

    private static boolean jsRelationalCompare(String operator, Object left, Object right) {
        if (left instanceof CharSequence leftText && right instanceof CharSequence rightText) {
            int compared = leftText.toString().compareTo(rightText.toString());
            return switch (operator) {
                case "<" -> compared < 0;
                case "<=" -> compared <= 0;
                case ">" -> compared > 0;
                case ">=" -> compared >= 0;
                default -> throw new IllegalArgumentException("Unsupported relational operator: " + operator);
            };
        }
        double leftNumber = toRelationalNumber(left);
        double rightNumber = toRelationalNumber(right);
        if (Double.isNaN(leftNumber) || Double.isNaN(rightNumber)) {
            return false;
        }
        return switch (operator) {
            case "<" -> leftNumber < rightNumber;
            case "<=" -> leftNumber <= rightNumber;
            case ">" -> leftNumber > rightNumber;
            case ">=" -> leftNumber >= rightNumber;
            default -> throw new IllegalArgumentException("Unsupported relational operator: " + operator);
        };
    }

    private static double toRelationalNumber(Object value) {
        if (value instanceof Boolean bool) {
            return bool ? 1.0d : 0.0d;
        }
        Double number = asNumber(value);
        return number == null ? Double.NaN : number;
    }

    public static Object __qin_logical__(Object operator, Object left, Object right) {
        String op = String.valueOf(operator);
        return switch (op) {
            case "&&" -> truthy(left) ? right : left;
            case "||" -> truthy(left) ? left : right;
            case "??" -> left != null ? left : right;
            default -> throw new IllegalArgumentException("Unsupported logical operator: " + op);
        };
    }

    public static Object __qin_unary__(Object operator, Object argument) {
        String op = String.valueOf(operator);
        return switch (op) {
            case "!" -> !truthy(argument);
            case "+" -> requireNumber(asNumber(argument), op);
            case "-" -> -requireNumber(asNumber(argument), op);
            case "~" -> (double) ~toInt32(argument);
            case "typeof" -> typeOf(argument);
            case "void" -> null;
            default -> throw new IllegalArgumentException("Unsupported unary operator: " + op);
        };
    }

    public static Object __qin_conditional__(Object test, Object consequent, Object alternate) {
        return truthy(test) ? consequent : alternate;
    }

    public static boolean __qin_truthy__(Object value) {
        return truthy(value);
    }

    public static Object __qin_member_get__(Object target, Object property) {
        target = unwrapExportSlotValue(target);
        if (target == null) {
            return null;
        }
        if (target instanceof String builtinName) {
            Object value = tryReadBuiltinNamespaceMember(builtinName, property);
            if (value != BUILTIN_MISS) {
                return value;
            }
        }
        if (target instanceof QinRuntimeObject runtimeObject) {
            return normalizeRuntimeMemberValue(runtimeObject.get(property));
        }
        if (target instanceof CharSequence text) {
            return JavaEsmString.memberGet(text, property);
        }
        if (target instanceof JavaEsmRegExp regexp) {
            return regexp.memberGet(property);
        }
        if (target instanceof JavaEsmTypedArray typedArray) {
            return typedArray.memberGet(property);
        }
        if (target instanceof JavaEsmSetObject setObject && Objects.equals(property, JavaEsmSymbol.iterator())) {
            return new NativeFunction("Set.Symbol.iterator", args -> setObject.values().iterator());
        }
        if (target instanceof JavaEsmMapObject mapObject && Objects.equals(property, JavaEsmSymbol.iterator())) {
            return new NativeFunction("Map.Symbol.iterator", args -> mapObject.entries().iterator());
        }
        if (target instanceof List<?> list && Objects.equals(property, JavaEsmSymbol.iterator())) {
            return new NativeFunction("Array.Symbol.iterator", args -> list.iterator());
        }
        if (target instanceof Map<?, ?> map) {
            String key = propertyKey(property);
            Map<String, Object> cast = castMap(map);
            if (cast.containsKey(key) && !isRuntimeHiddenObjectKey(key)) {
                return normalizeRuntimeMemberValue(JavaEsmObject.resolveStoredPropertyValue(cast.get(key)));
            }
            Object prototypeValue = mapPrototypeChainValue(cast, key);
            if (prototypeValue != BUILTIN_MISS) {
                return prototypeValue;
            }
            Object globalBuiltinValue = tryReadGlobalObjectBuiltin(key);
            if (globalBuiltinValue != BUILTIN_MISS) {
                return globalBuiltinValue;
            }
            Object objectPrototypeValue = objectPrototypeMember(target, key);
            if (objectPrototypeValue != BUILTIN_MISS) {
                return objectPrototypeValue;
            }
            return null;
        }
        if (target instanceof JavaEsmArrayObject arrayObject) {
            return arrayObject.memberGet(property);
        }
        if (target instanceof List<?> list) {
            return JavaEsmArray.memberGet(list, property);
        }
        if (target.getClass().isArray()) {
            String name = String.valueOf(property);
            if ("length".equals(name)) {
                return Array.getLength(target);
            }
            int index = toIndex(property);
            return index >= 0 && index < Array.getLength(target) ? Array.get(target, index) : null;
        }
        String name = String.valueOf(property);
        Method method = findCompatibleMethod(target instanceof Class<?> clazz ? clazz : target.getClass(), name, 0, target instanceof Class<?>);
        if (method != null) {
            return method;
        }
        return tryReadField(target, name);
    }

    private static Object normalizeRuntimeMemberValue(Object value) {
        if (isFunctionDefinition(value)) {
            return new InterpretedFunction(castMap((Map<?, ?>) value));
        }
        return value;
    }

    public static Object __qin_member_set__(Object target, Object property, Object value) {
        target = unwrapExportSlotValue(target);
        if (target instanceof QinRuntimeObject runtimeObject) {
            return runtimeObject.set(property, value);
        }
        if (target instanceof JavaEsmRegExp regexp) {
            return regexp.memberSet(property, value);
        }
        if (target instanceof Map<?, ?> map) {
            castMap(map).put(propertyKey(property), value);
            return value;
        }
        if (target instanceof JavaEsmTypedArray typedArray) {
            return typedArray.memberSet(property, value);
        }
        if (target instanceof JavaEsmArrayObject arrayObject) {
            return arrayObject.memberSet(property, value);
        }
        if (target instanceof List<?> list) {
            @SuppressWarnings("unchecked")
            List<Object> mutable = (List<Object>) list;
            return JavaEsmArray.memberSet(mutable, property, value);
        }
        String name = String.valueOf(property);
        if (tryWriteField(target, name, value)) {
            return value;
        }
        throw new IllegalArgumentException("Unsupported member set target: " + simpleName(target));
    }

    public static Object __qin_delete_member__(Object target, Object property) {
        target = unwrapExportSlotValue(target);
        if (target == null) {
            return true;
        }
        if (target instanceof QinRuntimeObject runtimeObject) {
            if (runtimeObject instanceof InterpretedInstance instance) {
                instance.fields.remove(String.valueOf(property));
            }
            return true;
        }
        if (target instanceof JavaEsmArrayObject arrayObject) {
            return arrayObject.memberDelete(property);
        }
        if (target instanceof Map<?, ?> map) {
            castMap(map).remove(propertyKey(property));
            return true;
        }
        if (target instanceof List<?> list) {
            int index = toIndex(property);
            if (index >= 0 && index < list.size()) {
                ((List<Object>) list).set(index, null);
            }
            return true;
        }
        return true;
    }

    public static Object __qin_array_item__(Object value) {
        return new ArrayLiteralSegment(false, value);
    }

    public static Object __qin_array_spread__(Object value) {
        return new ArrayLiteralSegment(true, value);
    }

    public static Object __qin_array_literal_array__(Object[] segments) {
        List<Object> result = new ArrayList<>();
        if (segments == null) {
            return result;
        }
        for (Object segment : segments) {
            if (segment instanceof ArrayLiteralSegment arraySegment) {
                if (arraySegment.spread()) {
                    @SuppressWarnings("unchecked")
                    List<Object> spreadValues = (List<Object>) JavaEsmArray.from(arraySegment.value());
                    result.addAll(spreadValues);
                } else {
                    result.add(arraySegment.value());
                }
                continue;
            }
            result.add(segment);
        }
        return result;
    }

    public static Object __qin_new__(Object callee) {
        return constructFromRuntimeNew(callee);
    }

    public static Object __qin_new__(Object callee, Object arg0) {
        return constructFromRuntimeNew(callee, arg0);
    }

    public static Object __qin_new__(Object callee, Object arg0, Object arg1) {
        return constructFromRuntimeNew(callee, arg0, arg1);
    }

    public static Object __qin_new__(Object callee, Object arg0, Object arg1, Object arg2) {
        return constructFromRuntimeNew(callee, arg0, arg1, arg2);
    }

    public static Object __qin_new__(Object callee, Object arg0, Object arg1, Object arg2, Object arg3) {
        return constructFromRuntimeNew(callee, arg0, arg1, arg2, arg3);
    }

    public static Object __qin_new__(Object callee, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        return constructFromRuntimeNew(callee, arg0, arg1, arg2, arg3, arg4);
    }

    public static Object __qin_new_array__(Object callee, Object[] args) {
        return constructFromRuntimeNew(callee, args);
    }

    private static Object constructFromRuntimeNew(Object callee, Object... args) {
        if (callee == null) {
            throw new IllegalArgumentException("Unsupported constructor target: null; runtime __qin_new__ callee=null");
        }
        return construct(callee, args);
    }

    public static Object __qin_dynamic_import__(Object source) {
        return dynamicImportPromise(source, null);
    }

    public static Object __qin_dynamic_import__(Object source, Object options) {
        return dynamicImportPromise(source, options);
    }

    private static Object dynamicImportPromise(Object source, Object staticallyResolvedNamespace) {
        try {
            if (staticallyResolvedNamespace != null) {
                return ImmediatePromise.resolved(staticallyResolvedNamespace);
            }
            Object hostNamespace = resolveDynamicHostNamespace(source);
            if (hostNamespace != null) {
                return ImmediatePromise.resolved(hostNamespace);
            }
            return ImmediatePromise.resolved(QinRuntimeModuleRegistry.importModule(source));
        } catch (Throwable throwable) {
            return ImmediatePromise.rejected(throwable);
        }
    }

    public static Object __qin_top_level_await__(Object value) {
        return value;
    }

    private static Object resolveDynamicHostNamespace(Object source) {
        String specifier = String.valueOf(source);
        return switch (specifier) {
            case "fs", "node:fs", "path", "node:path", "url", "node:url", "util", "node:util",
                    "process", "node:process", "module", "node:module", "crypto", "node:crypto",
                    "tty", "node:tty", "diagnostics_channel", "node:diagnostics_channel" ->
                    __qin_global__(specifier);
            default -> null;
        };
    }

    static Object callRuntimeCallable(Object callable, Object... args) {
        return callAny(callable, args);
    }

    static boolean isRuntimeCallable(Object value) {
        return value instanceof QinCallable || value instanceof Method || isFunctionDefinition(value);
    }

    private static boolean isRuntimeCallableValue(Object value) {
        return isRuntimeCallable(unwrapExportSlotValue(value));
    }

    private static List<String> interpretedCallStackSnapshot() {
        List<String> stack = INTERPRETED_CALL_STACK.get();
        if (stack.isEmpty()) {
            return List.of();
        }
        int from = Math.max(0, stack.size() - 24);
        return List.copyOf(stack.subList(from, stack.size()));
    }

    static Object bindRuntimeCallableThis(Object callable, Object thisArg) {
        callable = unwrapExportSlotValue(callable);
        if (callable instanceof InterpretedFunction interpretedFunction) {
            return interpretedFunction.bindThis(thisArg);
        }
        if (isFunctionDefinition(callable)) {
            return new InterpretedFunction(castMap((Map<?, ?>) callable)).bindThis(thisArg);
        }
        return callable;
    }

    static boolean isRuntimeTruthy(Object value) {
        return truthy(value);
    }

    private static Object callAny(Object callable, Object... args) {
        callable = unwrapExportSlotValue(callable);
        if (callable instanceof InterpretedFunction interpretedFunction) {
            Object receiver = interpretedFunction.recoverClassMemberFunctionalReceiver();
            InterpretedFunction target = receiver == null ? interpretedFunction : interpretedFunction.bindThis(receiver);
            Object[] callArgs = receiver == null
                    ? (args == null ? new Object[0] : args)
                    : dropReceiverOnlyFunctionalAdapterArg(receiver, interpretedFunction, args);
            return target.call(callArgs);
        }
        if (callable instanceof QinCallable qinCallable) {
            return qinCallable.call(args);
        }
        if (isFunctionDefinition(callable)) {
            return new InterpretedFunction(castMap((Map<?, ?>) callable)).call(args);
        }
        if (callable instanceof String builtinName) {
            Object result = callBuiltinFunction(builtinName, args);
            if (result != BUILTIN_MISS) {
                return result;
            }
        }
        if (callable instanceof Method method) {
            try {
                if (method.isVarArgs()) {
                    return method.invoke(null, adaptVarArgs(args, method.getParameterTypes()));
                }
                return method.invoke(Modifier.isStatic(method.getModifiers()) ? null : null, args);
            } catch (IllegalAccessException | InvocationTargetException error) {
                throw new IllegalArgumentException("Failed to invoke method callable", error);
            }
        }
        throw new IllegalArgumentException("Unsupported callable: " + simpleName(callable));
    }

    private static final Object INTERPRETED_INSTANCE_COMPATIBILITY_MISS = new Object();

    private static Object tryCallGeneratedJavaHashSetFastPath(
            InterpretedInstance interpretedInstance,
            String name,
            Object[] args) {
        if (!isGeneratedJavaHashSetShape(interpretedInstance)) {
            return INTERPRETED_INSTANCE_COMPATIBILITY_MISS;
        }
        return switch (name) {
            case "contains" -> args.length == 1 && args[0] instanceof CharSequence
                    ? generatedJavaHashSetContains(interpretedInstance, String.valueOf(args[0]))
                    : INTERPRETED_INSTANCE_COMPATIBILITY_MISS;
            case "add" -> args.length == 1 && args[0] instanceof CharSequence
                    ? generatedJavaHashSetAdd(interpretedInstance, String.valueOf(args[0]))
                    : INTERPRETED_INSTANCE_COMPATIBILITY_MISS;
            case "remove" -> args.length == 1 && args[0] instanceof CharSequence
                    ? generatedJavaHashSetRemove(interpretedInstance, String.valueOf(args[0]))
                    : INTERPRETED_INSTANCE_COMPATIBILITY_MISS;
            case "size" -> args.length == 0 ? generatedJavaHashSetSize(interpretedInstance) : INTERPRETED_INSTANCE_COMPATIBILITY_MISS;
            case "isEmpty" -> args.length == 0 ? generatedJavaHashSetSize(interpretedInstance) == 0.0d : INTERPRETED_INSTANCE_COMPATIBILITY_MISS;
            case "clear" -> {
                if (args.length != 0) {
                    yield INTERPRETED_INSTANCE_COMPATIBILITY_MISS;
                }
                generatedJavaHashSetBuckets(interpretedInstance).clear();
                interpretedInstance.fields.put("__size", 0.0d);
                yield null;
            }
            case "toArray" -> args.length == 0 ? generatedJavaHashSetToArray(interpretedInstance) : INTERPRETED_INSTANCE_COMPATIBILITY_MISS;
            default -> INTERPRETED_INSTANCE_COMPATIBILITY_MISS;
        };
    }

    private static boolean isGeneratedJavaHashSetShape(InterpretedInstance interpretedInstance) {
        return interpretedInstance.fields.get("__buckets") instanceof JavaEsmMapObject
                && interpretedInstance.fields.containsKey("__size")
                && interpretedInstance.methods.containsKey("__bucket")
                && interpretedInstance.methods.containsKey("__findEntry")
                && interpretedInstance.methods.containsKey("contains")
                && interpretedInstance.methods.containsKey("add");
    }

    private static Object generatedJavaHashSetNativeMethod(InterpretedInstance interpretedInstance, String name) {
        if (!isGeneratedJavaHashSetShape(interpretedInstance)
                || !Set.of("contains", "add", "remove", "size", "isEmpty", "clear", "toArray").contains(name)) {
            return null;
        }
        return new NativeFunction("__QinJavaUtilHashSet." + name, args -> {
            Object result = tryCallGeneratedJavaHashSetFastPath(interpretedInstance, name, args);
            if (result != INTERPRETED_INSTANCE_COMPATIBILITY_MISS) {
                return result;
            }
            InterpretedFunction method = interpretedInstance.methods.get(name);
            if (method == null) {
                throw new IllegalArgumentException("Unknown generated HashSet method: " + name);
            }
            return method.bindThis(interpretedInstance).call(args);
        });
    }

    private static JavaEsmMapObject generatedJavaHashSetBuckets(InterpretedInstance interpretedInstance) {
        return (JavaEsmMapObject) interpretedInstance.fields.get("__buckets");
    }

    private static double generatedJavaHashSetSize(InterpretedInstance interpretedInstance) {
        Object size = interpretedInstance.fields.get("__size");
        return size instanceof Number number ? number.doubleValue() : 0.0d;
    }

    @SuppressWarnings("unchecked")
    private static List<Object> generatedJavaHashSetBucket(
            InterpretedInstance interpretedInstance,
            String value,
            boolean create) {
        JavaEsmMapObject buckets = generatedJavaHashSetBuckets(interpretedInstance);
        String hash = "hash:" + javaStringHashCode(value);
        Object bucket = buckets.get(hash);
        if (bucket == null && create) {
            bucket = new ArrayList<Object>();
            buckets.set(hash, bucket);
        }
        if (bucket instanceof List<?> list) {
            return (List<Object>) list;
        }
        return null;
    }

    private static boolean generatedJavaHashSetContains(InterpretedInstance interpretedInstance, String value) {
        List<Object> bucket = generatedJavaHashSetBucket(interpretedInstance, value, false);
        if (bucket == null) {
            return false;
        }
        for (Object entry : bucket) {
            if (String.valueOf(entry).equals(value)) {
                return true;
            }
        }
        return false;
    }

    private static boolean generatedJavaHashSetAdd(InterpretedInstance interpretedInstance, String value) {
        if (generatedJavaHashSetContains(interpretedInstance, value)) {
            return false;
        }
        List<Object> bucket = generatedJavaHashSetBucket(interpretedInstance, value, true);
        if (bucket == null) {
            return false;
        }
        bucket.add(value);
        interpretedInstance.fields.put("__size", generatedJavaHashSetSize(interpretedInstance) + 1.0d);
        return true;
    }

    private static boolean generatedJavaHashSetRemove(InterpretedInstance interpretedInstance, String value) {
        List<Object> bucket = generatedJavaHashSetBucket(interpretedInstance, value, false);
        if (bucket == null) {
            return false;
        }
        for (int index = 0; index < bucket.size(); index++) {
            if (String.valueOf(bucket.get(index)).equals(value)) {
                bucket.remove(index);
                interpretedInstance.fields.put("__size", Math.max(0.0d, generatedJavaHashSetSize(interpretedInstance) - 1.0d));
                return true;
            }
        }
        return false;
    }

    private static List<Object> generatedJavaHashSetToArray(InterpretedInstance interpretedInstance) {
        List<Object> values = new ArrayList<>();
        for (Object bucket : generatedJavaHashSetBuckets(interpretedInstance).values()) {
            if (bucket instanceof List<?> list) {
                values.addAll(list);
            }
        }
        return values;
    }

    private static int javaStringHashCode(String value) {
        int hash = 0;
        for (int index = 0; index < value.length(); index++) {
            hash = 31 * hash + value.charAt(index);
        }
        return hash;
    }

    private static Object tryCallInterpretedInstanceCompatibilityMethod(
            InterpretedInstance interpretedInstance,
            String name,
            Object[] args) {
        if ("consume".equals(name)) {
            if (args.length == 1 && interpretedInstance.hasCallableMember("token")) {
                return callMethod(interpretedInstance, "token", args);
            }
            if (args.length == 2 && interpretedInstance.hasCallableMember("_consumeToken")) {
                return callMethod(interpretedInstance, "_consumeToken", args);
            }
        }
        return INTERPRETED_INSTANCE_COMPATIBILITY_MISS;
    }

    private static Object callBuiltinFunction(String builtinName, Object[] args) {
        return switch (builtinName) {
            case "String" -> args.length == 0 ? "" : jsString(args[0]);
            case "Number" -> args.length == 0 ? 0.0d : toJsNumber(args[0]);
            case "Boolean" -> args.length != 0 && truthy(args[0]);
            case "Symbol" -> JavaEsmSymbol.create(args.length == 0 ? null : args[0]);
            case "Array" -> new ArrayList<>(List.of(args));
            case "Object" -> {
                if (args.length == 0 || args[0] == null) {
                    yield new LinkedHashMap<String, Object>();
                }
                yield args[0];
            }
            case "RegExp", "Date", "Error", "TypeError", "RangeError", "ReferenceError", "SyntaxError" ->
                    construct(builtinName, args);
            default -> BUILTIN_MISS;
        };
    }

    static Map<String, Object> __qin_own_enumerable_entries__(Object value) {
        value = unwrapExportSlotValue(value);
        if (value instanceof InterpretedInstance instance) {
            return instance.ownEnumerableProperties();
        }
        return null;
    }

    private static Object callMethod(Object target, Object methodName, Object... args) {
        Object rawTarget = target;
        target = unwrapExportSlotValue(target);
        if (target == null) {
            throw new IllegalArgumentException("Cannot call method on null: method="
                    + methodName
                    + "; rawTarget="
                    + summarizeRuntimeValue(rawTarget)
                    + "; args="
                    + summarizeRuntimeValue(Arrays.asList(args))
                    + "; stack="
                    + runtimeStackHint()
                    + "; recentCalls="
                    + interpretedCallStackSnapshot());
        }
        String name = String.valueOf(methodName);
        Object builtinResult = tryCallBuiltinNamespace(target, name, args);
        if (builtinResult != BUILTIN_MISS) {
            return builtinResult;
        }
        if (target instanceof CharSequence text && JavaEsmString.supports(name)) {
            return JavaEsmString.invoke(text, name, args);
        }
        if (target instanceof JavaEsmRegExp regexp && regexp.supports(name)) {
            return regexp.invoke(name, args);
        }
        if (target instanceof JavaEsmTypedArray typedArray) {
            Object value = typedArray.memberGet(methodName);
            if (value != null) {
                return callRuntimeMethodValue(target, value, args);
            }
        }
        if (target instanceof JavaEsmSetObject setObject && Objects.equals(methodName, JavaEsmSymbol.iterator())) {
            return setObject.values().iterator();
        }
        if (target instanceof JavaEsmMapObject mapObject && Objects.equals(methodName, JavaEsmSymbol.iterator())) {
            return mapObject.entries().iterator();
        }
        if (target instanceof List<?> list && Objects.equals(methodName, JavaEsmSymbol.iterator())) {
            return list.iterator();
        }
        if (target instanceof Number number && JavaEsmNumber.supports(name)) {
            return JavaEsmNumber.invoke(number, name, args);
        }
        if (target instanceof List<?> list && JavaEsmArray.supports(name)) {
            return JavaEsmArray.invoke(list, name, args);
        }
        if (target instanceof QinRuntimeObject runtimeObject) {
            if (target instanceof InterpretedInstance interpretedInstance) {
                Object generatedHashSetResult = tryCallGeneratedJavaHashSetFastPath(
                        interpretedInstance,
                        name,
                        args);
                if (generatedHashSetResult != INTERPRETED_INSTANCE_COMPATIBILITY_MISS) {
                    return generatedHashSetResult;
                }
                if (isJavaFunctionalAdapterMethod(name, args.length)) {
                    Object adapterMethod = interpretedInstance.getOwnField(name);
                    if (isRuntimeCallableValue(adapterMethod)) {
                        return callRuntimeMethodValue(
                                target,
                                adapterMethod,
                                "apply".equals(name) ? args : new Object[0]);
                    }
                    Object alternativeCallable = interpretedInstance.get("alt");
                    if (isRuntimeCallableValue(alternativeCallable)) {
                        return callRuntimeMethodValue(
                                target,
                                alternativeCallable,
                                "apply".equals(name) ? args : new Object[0]);
                    }
                }
                Object methodValue = interpretedInstance.getMethod(name);
                if (methodValue != null) {
                    return callRuntimeMethodValue(target, methodValue, args);
                }
            }
            Object value = runtimeObject.get(methodName);
            if (value != null) {
                return callRuntimeMethodValue(target, value, args);
            }
            Object builtinObjectMethod = tryCallObjectPrototypeMethod(target, name, args);
            if (builtinObjectMethod != BUILTIN_MISS) {
                return builtinObjectMethod;
            }
            if (target instanceof InterpretedInstance interpretedInstance) {
                Object compatibilityResult = tryCallInterpretedInstanceCompatibilityMethod(
                        interpretedInstance,
                        name,
                        args);
                if (compatibilityResult != INTERPRETED_INSTANCE_COMPATIBILITY_MISS) {
                    return compatibilityResult;
                }
                throw new IllegalArgumentException(
                        "Unknown interpreted instance method: "
                                + name + "/" + args.length
                                + "; methods=" + interpretedInstance.methodNames()
                                + "; fields=" + interpretedInstance.fieldNames()
                                + "; accessors=" + interpretedInstance.accessorNames());
            }
        }
        if (target instanceof InterpretedFunction interpretedFunction
                && isJavaFunctionalAdapterMethod(name, args.length)) {
            Object receiver = interpretedFunction.recoverClassMemberFunctionalReceiver();
            InterpretedFunction callable = receiver == null ? interpretedFunction : interpretedFunction.bindThis(receiver);
            return callable.call("apply".equals(name) ? args : new Object[0]);
        }
        if (target instanceof Map<?, ?> map) {
            if (isModuleNamespaceMap(map)) {
                Object recoveredReceiverResult = tryCallRecoveredInstanceReceiver(name, args);
                if (recoveredReceiverResult != RECOVERED_RECEIVER_MISS) {
                    return recoveredReceiverResult;
                }
            }
            Object value = JavaEsmObject.resolveStoredPropertyValue(castMap(map).get(propertyKey(methodName)));
            if (value != null) {
                return callRuntimeMethodValue(target, value, args);
            }
            if (isJavaFunctionalAdapterMethod(name, args.length)) {
                Object alternativeCallable = JavaEsmObject.resolveStoredPropertyValue(castMap(map).get(propertyKey("alt")));
                if (isRuntimeCallableValue(alternativeCallable)) {
                    return callRuntimeMethodValue(
                            target,
                            alternativeCallable,
                            "apply".equals(name) ? args : new Object[0]);
                }
            }
            Object structuralParamsResult = tryCallStructuralParamsMapMethod(map, name, args);
            if (structuralParamsResult != STRUCTURAL_PARAMS_MISS) {
                return structuralParamsResult;
            }
            Object recoveredReceiverResult = tryCallRecoveredInstanceReceiver(name, args);
            if (recoveredReceiverResult != RECOVERED_RECEIVER_MISS) {
                return recoveredReceiverResult;
            }
        }
        Class<?> ownerClass = target instanceof Class<?> clazz ? clazz : target.getClass();
        boolean staticOnly = target instanceof Class<?>;
        Method method = findCompatibleMethod(ownerClass, name, args, staticOnly);
        if (method == null) {
            throw new IllegalArgumentException(
                    "Unknown method: "
                            + ownerClass.getName() + "." + name + "/" + args.length
                            + "; target=" + summarizeRuntimeValue(target)
                            + "; args=" + describeArgs(args));
        }
        try {
            Object[] invokeArgs = method.isVarArgs()
                    ? adaptVarArgs(args, method.getParameterTypes(), method.getGenericParameterTypes())
                    : coerceArguments(args, method.getParameterTypes(), method.getGenericParameterTypes());
            return method.invoke(staticOnly ? null : target, invokeArgs);
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException(
                    "Failed to adapt method arguments: "
                            + ownerClass.getName() + "." + name + "/" + args.length
                            + "; parameterTypes=" + Arrays.toString(method.getParameterTypes())
                            + "; args=" + describeArgs(args),
                    error);
        } catch (IllegalAccessException | InvocationTargetException error) {
            throw new IllegalArgumentException("Failed to invoke method: " + ownerClass.getName() + "." + name, error);
        }
    }

    private static final Object STRUCTURAL_PARAMS_MISS = new Object();

    private static Object tryCallStructuralParamsMapMethod(Map<?, ?> rawMap, String name, Object[] args) {
        Map<String, Object> map = castMap(rawMap);
        if (args.length == 0) {
            return switch (name) {
                case "in" -> readStructuralParam(map, true, "In", "in", "__qin_in");
                case "yield" -> readStructuralParam(map, false, "Yield", "yield", "__qin_yield");
                case "await" -> readStructuralParam(map, false, "Await", "await", "__qin_await");
                case "tagged" -> readStructuralParam(map, false, "Tagged", "tagged");
                case "returnAllowed" -> readStructuralParam(map, false, "Return", "ReturnAllowed", "returnAllowed");
                case "isDefault" -> readStructuralParam(map, false, "Default", "IsDefault", "isDefault", "default");
                case "expressionParams" -> structuralExpressionParams(map);
                default -> STRUCTURAL_PARAMS_MISS;
            };
        }
        if (args.length == 1) {
            return switch (name) {
                case "withIn" -> copyStructuralParam(map, "In", args[0]);
                case "withYield" -> copyStructuralParam(map, "Yield", args[0]);
                case "withAwait" -> copyStructuralParam(map, "Await", args[0]);
                default -> STRUCTURAL_PARAMS_MISS;
            };
        }
        return STRUCTURAL_PARAMS_MISS;
    }

    private static Map<String, Object> structuralExpressionParams(Map<String, Object> map) {
        Map<String, Object> expressionParams = new LinkedHashMap<>();
        expressionParams.put("In", readStructuralParam(map, true, "In", "in", "__qin_in"));
        expressionParams.put("Yield", readStructuralParam(map, false, "Yield", "yield", "__qin_yield"));
        expressionParams.put("Await", readStructuralParam(map, false, "Await", "await", "__qin_await"));
        return expressionParams;
    }

    private static Object readStructuralParam(Map<String, Object> map, Object defaultValue, String... keys) {
        for (String key : keys) {
            if (map.containsKey(key)) {
                return JavaEsmObject.resolveStoredPropertyValue(map.get(key));
            }
        }
        return defaultValue;
    }

    private static Map<String, Object> copyStructuralParam(Map<String, Object> map, String key, Object value) {
        Map<String, Object> copy = new LinkedHashMap<>(map);
        copy.put(key, value);
        return copy;
    }

    private static final Object RECOVERED_RECEIVER_MISS = new Object();

    private static Object tryCallRecoveredInstanceReceiver(String name, Object[] args) {
        List<Object> thisStack = INTERPRETED_THIS_STACK.get();
        for (int i = thisStack.size() - 1; i >= 0; i--) {
            Object candidate = unwrapExportSlotValue(thisStack.get(i));
            if (candidate instanceof InterpretedInstance interpretedInstance
                    && interpretedInstance.hasCallableMember(name)) {
                return callMethod(interpretedInstance, name, args);
            }
        }
        return RECOVERED_RECEIVER_MISS;
    }

    private static Object recoverLexicalThis(Object value) {
        if (!(value instanceof Map<?, ?> map) || !isModuleNamespaceMap(map)) {
            return value;
        }
        List<Object> thisStack = INTERPRETED_THIS_STACK.get();
        for (int i = thisStack.size() - 1; i >= 0; i--) {
            Object candidate = unwrapExportSlotValue(thisStack.get(i));
            if (candidate instanceof InterpretedInstance) {
                return candidate;
            }
        }
        return value;
    }

    private static boolean isModuleNamespaceMap(Map<?, ?> map) {
        for (Object key : map.keySet()) {
            if (key instanceof String text && text.startsWith("__qesm_")) {
                return true;
            }
        }
        return false;
    }

    private static boolean isJavaFunctionalAdapterMethod(String name, int argCount) {
        return switch (name) {
            case "get", "run", "execute" -> argCount <= 1;
            case "apply" -> true;
            default -> false;
        };
    }

    private static Object[] dropReceiverOnlyFunctionalAdapterArg(Object receiver, Object value, Object[] args) {
        if (args == null || args.length != 1 || args[0] != receiver) {
            return args == null ? new Object[0] : args;
        }
        String functionName = runtimeFunctionName(value);
        if ("get".equals(functionName) || "run".equals(functionName) || "execute".equals(functionName)) {
            return new Object[0];
        }
        return args;
    }

    private static String runtimeFunctionName(Object value) {
        if (value instanceof InterpretedFunction interpretedFunction) {
            Object name = interpretedFunction.definition.get("functionName");
            return name == null ? null : String.valueOf(name);
        }
        if (isFunctionDefinition(value)) {
            Object name = castMap((Map<?, ?>) value).get("functionName");
            return name == null ? null : String.valueOf(name);
        }
        return null;
    }

    private static String describeArgs(Object[] args) {
        if (args == null) {
            return "null";
        }
        List<String> parts = new ArrayList<>(args.length);
        for (Object arg : args) {
            parts.add(arg == null ? "null" : arg + " (" + arg.getClass().getName() + ")");
        }
        return parts.toString();
    }

    private static Object unwrapExportSlotValue(Object value) {
        if (value instanceof ExportSlot) {
            return __qin_export_get__(value);
        }
        return value;
    }

    private static Object callRuntimeMethodValue(Object receiver, Object value, Object... args) {
        Object[] callArgs = dropReceiverOnlyFunctionalAdapterArg(receiver, value, args);
        if (value instanceof InterpretedFunction interpretedFunction) {
            return interpretedFunction.bindThis(receiver).call(callArgs);
        }
        if (isFunctionDefinition(value)) {
            return new InterpretedFunction(castMap((Map<?, ?>) value)).bindThis(receiver).call(callArgs);
        }
        return callAny(value, callArgs);
    }

    private static Method findCompatibleMethod(Class<?> ownerClass, String name, int argCount, boolean staticOnly) {
        MethodLookupKey cacheKey = new MethodLookupKey(ownerClass, name, argCount, staticOnly);
        Object cached = METHOD_LOOKUP_CACHE.get(cacheKey);
        if (cached != null) {
            return cached == METHOD_LOOKUP_MISS ? null : (Method) cached;
        }
        for (Method method : candidateMethods(ownerClass, name, argCount, staticOnly)) {
            METHOD_LOOKUP_CACHE.put(cacheKey, method);
            return method;
        }
        METHOD_LOOKUP_CACHE.put(cacheKey, METHOD_LOOKUP_MISS);
        return null;
    }

    private static Method findCompatibleMethod(Class<?> ownerClass, String name, Object[] args, boolean staticOnly) {
        TypedMethodLookupKey cacheKey = typedMethodLookupKey(ownerClass, name, args, staticOnly);
        Object cached = TYPED_METHOD_LOOKUP_CACHE.get(cacheKey);
        if (cached != null) {
            return cached == METHOD_LOOKUP_MISS ? null : (Method) cached;
        }
        Method varArgsCandidate = null;
        boolean varArgsCandidateCacheable = false;
        boolean cacheableMiss = true;
        for (Method method : candidateMethods(ownerClass, name, args.length, staticOnly)) {
            boolean cacheableCompatibility = isValueInsensitiveCompatibility(method);
            if (!cacheableCompatibility) {
                cacheableMiss = false;
            }
            if (!areCompatibleArguments(args, method)) {
                continue;
            }
            if (!method.isVarArgs()) {
                if (cacheableCompatibility) {
                    TYPED_METHOD_LOOKUP_CACHE.put(cacheKey, method);
                }
                return method;
            }
            if (varArgsCandidate == null) {
                varArgsCandidate = method;
                varArgsCandidateCacheable = cacheableCompatibility;
            }
        }
        if (varArgsCandidate != null && varArgsCandidateCacheable) {
            TYPED_METHOD_LOOKUP_CACHE.put(cacheKey, varArgsCandidate);
        } else if (varArgsCandidate == null && cacheableMiss) {
            TYPED_METHOD_LOOKUP_CACHE.put(cacheKey, METHOD_LOOKUP_MISS);
        }
        return varArgsCandidate;
    }

    private static TypedMethodLookupKey typedMethodLookupKey(
            Class<?> ownerClass,
            String name,
            Object[] args,
            boolean staticOnly) {
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i] == null ? NullMethodArgument.class : args[i].getClass();
        }
        return new TypedMethodLookupKey(ownerClass, name, List.of(argTypes), staticOnly);
    }

    private static boolean isValueInsensitiveCompatibility(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (!method.isVarArgs()) {
            for (int i = 0; i < parameterTypes.length; i++) {
                Type genericParameterType = genericParameterTypes == null || i >= genericParameterTypes.length
                        ? parameterTypes[i]
                        : genericParameterTypes[i];
                if (isValueSensitiveCompatibility(parameterTypes[i], genericParameterType)) {
                    return false;
                }
            }
            return true;
        }
        int fixedCount = parameterTypes.length - 1;
        for (int i = 0; i < fixedCount; i++) {
            Type genericParameterType = genericParameterTypes == null || i >= genericParameterTypes.length
                    ? parameterTypes[i]
                    : genericParameterTypes[i];
            if (isValueSensitiveCompatibility(parameterTypes[i], genericParameterType)) {
                return false;
            }
        }
        Class<?> componentType = parameterTypes[fixedCount].getComponentType();
        return !isValueSensitiveCompatibility(componentType, componentType);
    }

    private static boolean isValueSensitiveCompatibility(Class<?> parameterType, Type genericParameterType) {
        return isJavaFunctionalInterface(parameterType)
                || parameterType.isRecord()
                || isSubhutiAlternativeType(parameterType)
                || (List.class.isAssignableFrom(parameterType)
                        && isSubhutiAlternativeType(listElementType(genericParameterType)));
    }

    private static List<Method> candidateMethods(Class<?> ownerClass) {
        return METHOD_CANDIDATE_CACHE.computeIfAbsent(ownerClass, JavaEsmGlobal::computeCandidateMethods);
    }

    private static List<Method> candidateMethods(Class<?> ownerClass, String name, int argCount, boolean staticOnly) {
        MethodCandidateKey cacheKey = new MethodCandidateKey(ownerClass, name, argCount, staticOnly);
        return METHOD_CANDIDATE_BY_NAME_CACHE.computeIfAbsent(cacheKey, key -> {
            List<Method> methods = new ArrayList<>();
            for (Method method : candidateMethods(key.ownerClass())) {
                if (!method.getName().equals(key.name()) || !isCompatibleArity(method, key.argCount())) {
                    continue;
                }
                if (key.staticOnly() && !Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                methods.add(method);
            }
            return List.copyOf(methods);
        });
    }

    private static List<Method> computeCandidateMethods(Class<?> ownerClass) {
        List<Method> methods = new ArrayList<>();
        for (Method method : ownerClass.getMethods()) {
            methods.add(method);
        }
        for (Class<?> current = ownerClass;
                current != null && current != Object.class;
                current = current.getSuperclass()) {
            for (Method method : current.getDeclaredMethods()) {
                int modifiers = method.getModifiers();
                if (!Modifier.isPublic(modifiers) && !Modifier.isProtected(modifiers)) {
                    continue;
                }
                try {
                    method.setAccessible(true);
                } catch (RuntimeException ignored) {
                    continue;
                }
                methods.add(method);
            }
        }
        return List.copyOf(methods);
    }

    private static Object[] coerceArguments(Object[] args, Class<?>[] parameterTypes) {
        return coerceArguments(args, parameterTypes, parameterTypes);
    }

    private static Object[] coerceArguments(Object[] args, Class<?>[] parameterTypes, Type[] genericParameterTypes) {
        Object[] coerced = Arrays.copyOf(args, args.length);
        for (int i = 0; i < coerced.length; i++) {
            Type genericParameterType = genericParameterTypes == null || i >= genericParameterTypes.length
                    ? parameterTypes[i]
                    : genericParameterTypes[i];
            coerced[i] = coerceArgument(coerced[i], parameterTypes[i], genericParameterType);
        }
        return coerced;
    }

    private static Object coerceArgument(Object value, Class<?> parameterType) {
        return coerceArgument(value, parameterType, parameterType);
    }

    private static Object coerceArgument(Object value, Class<?> parameterType, Type genericParameterType) {
        Object ffiValue = coerceTypedFfiArgument(value, parameterType, genericParameterType);
        if (ffiValue != value) {
            return ffiValue;
        }
        if (value == null || !parameterType.isPrimitive()) {
            if (parameterType == String.class && value != null) {
                return String.valueOf(value);
            }
            return value;
        }
        Double number = asNumber(value);
        if (parameterType == int.class) {
            return requireNumber(number, "int").intValue();
        }
        if (parameterType == long.class) {
            return requireNumber(number, "long").longValue();
        }
        if (parameterType == double.class) {
            return requireNumber(number, "double");
        }
        if (parameterType == float.class) {
            return requireNumber(number, "float").floatValue();
        }
        if (parameterType == boolean.class) {
            return truthy(value);
        }
        return value;
    }

    private static Object coerceTypedFfiArgument(Object value, Class<?> parameterType, Type genericParameterType) {
        if (value == null) {
            return null;
        }
        if (isJavaFunctionalInterface(parameterType)) {
            return coerceJavaFunctionalInterface(value, parameterType);
        }
        if (parameterType.isRecord()) {
            return coerceJavaRecord(value, parameterType);
        }
        if (isSubhutiAlternativeType(parameterType)) {
            return coerceSubhutiAlternative(value);
        }
        if (List.class.isAssignableFrom(parameterType) && isSubhutiAlternativeType(listElementType(genericParameterType))) {
            if (!(value instanceof List<?> list)) {
                throw new IllegalArgumentException("Expected List for typed Alternative FFI argument: " + simpleName(value));
            }
            List<Object> alternatives = new ArrayList<>(list.size());
            for (Object item : list) {
                alternatives.add(coerceSubhutiAlternative(item));
            }
            return alternatives;
        }
        return value;
    }

    private static Object coerceJavaRecord(Object value, Class<?> recordType) {
        if (recordType.isInstance(value)) {
            return value;
        }
        if (value != null && value.getClass().isRecord()) {
            return coerceJavaRecordFromMap(javaRecordEntries(value), recordType, false);
        }
        if (!(value instanceof Map<?, ?> rawMap)) {
            throw new IllegalArgumentException("Expected object literal for Java record "
                    + recordType.getName() + ", got: " + simpleName(value));
        }
        return coerceJavaRecordFromMap(castMap(rawMap), recordType, true);
    }

    private static Object coerceJavaRecordFromMap(Map<String, Object> map, Class<?> recordType, boolean validateUnknownKeys) {
        JavaRecordInfo recordInfo = javaRecordInfo(recordType);
        if (validateUnknownKeys) {
            validateJavaRecordKeys(map, recordInfo);
        }
        Object[] args = new Object[recordInfo.components().length];
        for (int i = 0; i < recordInfo.components().length; i++) {
            JavaRecordComponentInfo component = recordInfo.components()[i];
            Object componentValue = readJavaRecordComponentValue(map, component);
            args[i] = coerceArgument(componentValue, component.type(), component.genericType());
        }
        try {
            return recordInfo.constructor().newInstance(args);
        } catch (ReflectiveOperationException error) {
            throw new IllegalArgumentException("Failed to lower object literal to Java record "
                    + recordType.getName(), error);
        }
    }

    private static JavaRecordInfo javaRecordInfo(Class<?> recordType) {
        return JAVA_RECORD_INFO_CACHE.computeIfAbsent(recordType, JavaEsmGlobal::computeJavaRecordInfo);
    }

    private static JavaRecordInfo computeJavaRecordInfo(Class<?> recordType) {
        try {
            RecordComponent[] recordComponents = recordType.getRecordComponents();
            JavaRecordComponentInfo[] components = new JavaRecordComponentInfo[recordComponents.length];
            Class<?>[] parameterTypes = new Class<?>[recordComponents.length];
            Object defaultRecord = instantiateDefaultRecord(recordType);
            for (int i = 0; i < recordComponents.length; i++) {
                RecordComponent component = recordComponents[i];
                Method accessor = component.getAccessor();
                accessor.setAccessible(true);
                Object defaultValue = NO_DEFAULT_RECORD_VALUE;
                if (defaultRecord != null) {
                    defaultValue = accessor.invoke(defaultRecord);
                }
                parameterTypes[i] = component.getType();
                components[i] = new JavaRecordComponentInfo(
                        component.getName(),
                        component.getType(),
                        component.getGenericType(),
                        accessor,
                        javaRecordComponentKeys(component),
                        javaRecordComponentSpreadKey(component),
                        defaultValue);
            }
            Constructor<?> canonicalConstructor = recordType.getDeclaredConstructor(parameterTypes);
            canonicalConstructor.setAccessible(true);
            return new JavaRecordInfo(recordType, components, canonicalConstructor, defaultRecord != null);
        } catch (ReflectiveOperationException error) {
            throw new IllegalArgumentException("Failed to inspect Java record " + recordType.getName(), error);
        }
    }

    private static final Object NO_DEFAULT_RECORD_VALUE = new Object();

    private static Object instantiateDefaultRecord(Class<?> recordType) {
        try {
            Constructor<?> constructor = recordType.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException ignored) {
            return null;
        } catch (ReflectiveOperationException error) {
            throw new IllegalArgumentException("Failed to instantiate default Java record "
                    + recordType.getName(), error);
        }
    }

    private static void validateJavaRecordKeys(
            Map<String, Object> map,
            JavaRecordInfo recordInfo) {
        for (String key : map.keySet()) {
            boolean matched = false;
            for (JavaRecordComponentInfo component : recordInfo.components()) {
                if (component.keys().contains(key)) {
                    matched = true;
                    break;
                }
            }
            if (!matched
                    && isSlimeParserParamsRecord(recordInfo.recordType())
                    && SLIME_PARSER_PARAM_BOUNDARY_KEYS.contains(key)) {
                matched = true;
            }
            if (!matched) {
                throw new IllegalArgumentException("Unknown key '" + key + "' for Java record "
                        + recordInfo.recordType().getName());
            }
        }
    }

    private static boolean isSlimeParserParamsRecord(Class<?> recordType) {
        return recordType.getName().startsWith("com.slime.parser.base.SlimeJavascriptParserBase$")
                && recordType.getSimpleName().endsWith("Params");
    }

    private static Object readJavaRecordComponentValue(
            Map<String, Object> map,
            JavaRecordComponentInfo component) {
        for (String key : component.keys()) {
            if (map.containsKey(key)) {
                return JavaEsmObject.resolveStoredPropertyValue(map.get(key));
            }
        }
        if (component.defaultValue() != NO_DEFAULT_RECORD_VALUE) {
            return component.defaultValue();
        }
        if (component.type().isPrimitive()) {
            throw new IllegalArgumentException("Missing required Java record component " + component.name());
        }
        return null;
    }

    private static Set<String> javaRecordComponentKeys(RecordComponent component) {
        String name = component.getName();
        LinkedHashSet<String> keys = new LinkedHashSet<>();
        keys.add(name);
        keys.add(javaRecordComponentSpreadKey(component));
        keys.add(upperCamel(name));
        if (name.endsWith("Allowed") && name.length() > "Allowed".length()) {
            keys.add(upperCamel(name.substring(0, name.length() - "Allowed".length())));
        }
        if (name.startsWith("is") && name.length() > 2 && Character.isUpperCase(name.charAt(2))) {
            keys.add(name.substring(2));
        }
        return keys;
    }

    private static String javaRecordComponentSpreadKey(RecordComponent component) {
        String name = component.getName();
        if (name.endsWith("Allowed") && name.length() > "Allowed".length()) {
            return upperCamel(name.substring(0, name.length() - "Allowed".length()));
        }
        if (name.startsWith("is") && name.length() > 2 && Character.isUpperCase(name.charAt(2))) {
            return name.substring(2);
        }
        return upperCamel(name);
    }

    private static Map<String, Object> javaRecordEntries(Object record) {
        LinkedHashMap<String, Object> entries = new LinkedHashMap<>();
        for (JavaRecordComponentInfo component : javaRecordInfo(record.getClass()).components()) {
            try {
                entries.put(component.spreadKey(), component.accessor().invoke(record));
            } catch (ReflectiveOperationException error) {
                throw new IllegalArgumentException("Failed to read Java record component "
                        + component.name(), error);
            }
        }
        return entries;
    }

    private static String upperCamel(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    private static Object coerceJavaFunctionalInterface(Object value, Class<?> parameterType) {
        if (parameterType.isInstance(value)) {
            return value;
        }
        Method functionalMethod = functionalInterfaceMethod(parameterType);
        if (functionalMethod == null) {
            return value;
        }
        Object callable = unwrapExportSlotValue(value);
        if (!isRuntimeCallable(callable)) {
            throw new IllegalArgumentException("Expected callable for Java functional interface "
                    + parameterType.getName() + ", got: " + simpleName(value));
        }
        return Proxy.newProxyInstance(
                parameterType.getClassLoader(),
                new Class<?>[]{parameterType},
                (proxy, method, args) -> {
                    if (method.getDeclaringClass() == Object.class) {
                        return switch (method.getName()) {
                            case "toString" -> "QinFunctionalInterfaceProxy(" + parameterType.getName() + ")";
                            case "hashCode" -> System.identityHashCode(proxy);
                            case "equals" -> proxy == (args == null || args.length == 0 ? null : args[0]);
                            default -> throw new UnsupportedOperationException(method.getName());
                        };
                    }
                    Object result = callAny(callable, args == null ? new Object[0] : args);
                    if (method.getReturnType() == void.class) {
                        return null;
                    }
                    return coerceArgument(result, method.getReturnType(), method.getGenericReturnType());
                });
    }

    private static boolean isJavaFunctionalInterface(Class<?> type) {
        return functionalInterfaceMethod(type) != null;
    }

    private static Method functionalInterfaceMethod(Class<?> type) {
        if (!type.isInterface()) {
            return null;
        }
        Method functionalMethod = null;
        for (Method method : type.getMethods()) {
            int modifiers = method.getModifiers();
            if (method.getDeclaringClass() == Object.class
                    || Modifier.isStatic(modifiers)
                    || method.isDefault()
                    || !Modifier.isAbstract(modifiers)) {
                continue;
            }
            if (functionalMethod != null && !sameMethodSignature(functionalMethod, method)) {
                return null;
            }
            functionalMethod = method;
        }
        return functionalMethod;
    }

    private static boolean sameMethodSignature(Method left, Method right) {
        return left.getName().equals(right.getName())
                && Arrays.equals(left.getParameterTypes(), right.getParameterTypes());
    }

    private static Type listElementType(Type genericParameterType) {
        if (!(genericParameterType instanceof ParameterizedType parameterizedType)) {
            return null;
        }
        Type rawType = parameterizedType.getRawType();
        if (!(rawType instanceof Class<?> rawClass) || !List.class.isAssignableFrom(rawClass)) {
            return null;
        }
        Type[] arguments = parameterizedType.getActualTypeArguments();
        return arguments.length == 1 ? arguments[0] : null;
    }

    private static boolean isSubhutiAlternativeType(Type type) {
        if (type instanceof Class<?> clazz) {
            return SUBHUTI_ALTERNATIVE_CLASS_NAME.equals(clazz.getName());
        }
        if (type instanceof ParameterizedType parameterizedType) {
            return isSubhutiAlternativeType(parameterizedType.getRawType());
        }
        return false;
    }

    private static Object coerceSubhutiAlternative(Object value) {
        try {
            Class<?> alternativeClass = Class.forName(SUBHUTI_ALTERNATIVE_CLASS_NAME);
            if (alternativeClass.isInstance(value)) {
                return value;
            }
            if (!(value instanceof Map<?, ?> rawMap)) {
                throw new IllegalArgumentException("Expected { alt: () => ... } for Alternative, got: "
                        + simpleName(value));
            }
            Map<String, Object> map = castMap(rawMap);
            if (map.size() != 1 || !map.containsKey("alt")) {
                throw new IllegalArgumentException("Alternative object literal must have exact shape { alt: callable }");
            }
            Object callable = JavaEsmObject.resolveStoredPropertyValue(map.get("alt"));
            Supplier<Object> supplier = () -> callAny(callable);
            Method ofMethod = alternativeClass.getMethod("of", Supplier.class);
            return ofMethod.invoke(null, supplier);
        } catch (ReflectiveOperationException error) {
            throw new IllegalArgumentException("Failed to lower JS Alternative object to "
                    + SUBHUTI_ALTERNATIVE_CLASS_NAME, error);
        }
    }

    private static Object construct(Object callee, Object... args) {
        callee = unwrapExportSlotValue(callee);
        if (callee instanceof String text) {
            return switch (text) {
                case "Array" -> new ArrayList<>(List.of(args));
                case "Object" -> new LinkedHashMap<String, Object>();
                case "Map", "WeakMap" -> new JavaEsmMapObject();
                case "Set", "WeakSet" -> new JavaEsmSetObject(args.length == 0 ? null : args[0]);
                case "Proxy" -> createProxyObject(args);
                case "URLSearchParams" -> new JavaEsmUrlSearchParams(args.length == 0 ? null : args[0]);
                case "Date" -> JavaEsmDate.create(args);
                case "String" -> args.length == 0 ? "" : String.valueOf(args[0]);
                case "Boolean" -> args.length != 0 && truthy(args[0]);
                case "Number" -> args.length == 0 ? 0.0d : requireNumber(asNumber(args[0]), text);
                case "Uint8Array" -> JavaEsmTypedArray.uint8(args.length == 0 ? 0 : args[0]);
                case "Uint16Array" -> JavaEsmTypedArray.uint16(args.length == 0 ? 0 : args[0]);
                case "Uint32Array" -> JavaEsmTypedArray.uint32(args.length == 0 ? 0 : args[0]);
                case "TextDecoder" -> args.length == 0
                        ? new JavaEsmTextDecoder()
                        : new JavaEsmTextDecoder(args[0]);
                case "RegExp" -> new JavaEsmRegExp(
                        args.length == 0 ? "" : args[0],
                        args.length >= 2 ? args[1] : null);
                case "Error", "TypeError", "RangeError", "ReferenceError", "SyntaxError" -> createErrorObject(text, args);
                default -> throw new IllegalArgumentException("Unsupported constructor: " + text);
            };
        }
        if (callee instanceof Class<?> ownerClass) {
            for (Constructor<?> constructor : ownerClass.getConstructors()) {
                if (constructor.getParameterCount() != args.length) {
                    continue;
                }
                try {
                    return constructor.newInstance(coerceArguments(args, constructor.getParameterTypes()));
                } catch (ReflectiveOperationException ignored) {
                    // Try next constructor.
                }
            }
            throw new IllegalArgumentException("No compatible constructor: " + ownerClass.getName() + "/" + args.length);
        }
        if (isFunctionDefinition(callee)) {
            return new InterpretedFunction(castMap((Map<?, ?>) callee)).construct(args);
        }
        if (callee instanceof InterpretedFunction interpretedFunction) {
            return interpretedFunction.construct(args);
        }
        if (callee instanceof QinCallable qinCallable) {
            return qinCallable.call(args);
        }
        if (callee instanceof Map<?, ?> rawMap) {
            throw new IllegalArgumentException("Unsupported constructor target: "
                    + simpleName(callee)
                    + "; keys="
                    + rawMap.keySet());
        }
        throw new IllegalArgumentException("Unsupported constructor target: " + simpleName(callee));
    }

    private static Object createProxyObject(Object[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Proxy constructor expects target and handler");
        }
        return new ProxyObject(args[0], args[1]);
    }

    private static Object tryReadField(Object target, String name) {
        Class<?> ownerClass = target instanceof Class<?> clazz ? clazz : target.getClass();
        boolean staticOnly = target instanceof Class<?>;
        Field field = findAccessibleField(ownerClass, name, staticOnly);
        if (field == null) {
            return null;
        }
        try {
            return field.get(staticOnly ? null : target);
        } catch (IllegalAccessException ignored) {
            return null;
        }
    }

    private static boolean tryWriteField(Object target, String name, Object value) {
        Class<?> ownerClass = target instanceof Class<?> clazz ? clazz : target.getClass();
        boolean staticOnly = target instanceof Class<?>;
        Field field = findAccessibleField(ownerClass, name, staticOnly);
        if (field == null || Modifier.isFinal(field.getModifiers())) {
            return false;
        }
        try {
            Object coerced = coerceArgument(value, field.getType(), field.getGenericType());
            field.set(staticOnly ? null : target, coerced);
            return true;
        } catch (IllegalAccessException | IllegalArgumentException ignored) {
            return false;
        }
    }

    private static Field findAccessibleField(Class<?> ownerClass, String name, boolean staticOnly) {
        FieldLookupKey cacheKey = new FieldLookupKey(ownerClass, name, staticOnly);
        Object cached = FIELD_LOOKUP_CACHE.get(cacheKey);
        if (cached != null) {
            return cached == FIELD_LOOKUP_MISS ? null : (Field) cached;
        }
        Field field = findAccessibleFieldUncached(ownerClass, name, staticOnly);
        FIELD_LOOKUP_CACHE.put(cacheKey, field == null ? FIELD_LOOKUP_MISS : field);
        return field;
    }

    private static Field findAccessibleFieldUncached(Class<?> ownerClass, String name, boolean staticOnly) {
        for (Field field : ownerClass.getFields()) {
            if (field.getName().equals(name)
                    && (!staticOnly || Modifier.isStatic(field.getModifiers()))) {
                return field;
            }
        }
        for (Class<?> current = ownerClass;
                current != null && current != Object.class;
                current = current.getSuperclass()) {
            try {
                Field field = current.getDeclaredField(name);
                int modifiers = field.getModifiers();
                if (staticOnly && !Modifier.isStatic(modifiers)) {
                    continue;
                }
                if (!Modifier.isPublic(modifiers) && !Modifier.isProtected(modifiers)) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                } catch (RuntimeException ignored) {
                    continue;
                }
                return field;
            } catch (NoSuchFieldException ignored) {
                // Continue searching the superclass chain.
            }
        }
        return null;
    }

    private record FieldLookupKey(Class<?> ownerClass, String name, boolean staticOnly) {
    }

    private record MethodLookupKey(Class<?> ownerClass, String name, int argCount, boolean staticOnly) {
    }

    private record MethodCandidateKey(Class<?> ownerClass, String name, int argCount, boolean staticOnly) {
    }

    private record TypedMethodLookupKey(Class<?> ownerClass, String name, List<Class<?>> argTypes, boolean staticOnly) {
    }

    private static final class NullMethodArgument {
        private NullMethodArgument() {
        }
    }

    private record JavaRecordInfo(
            Class<?> recordType,
            JavaRecordComponentInfo[] components,
            Constructor<?> constructor,
            boolean hasDefaultRecord) {
    }

    private record JavaRecordComponentInfo(
            String name,
            Class<?> type,
            Type genericType,
            Method accessor,
            Set<String> keys,
            String spreadKey,
            Object defaultValue) {
    }

    private static int toIndex(Object value) {
        Double number = asNumber(value);
        return number == null ? -1 : number.intValue();
    }

    private static int toInt32(Object value) {
        Double number = asNumber(value);
        if (number == null || !Double.isFinite(number) || number == 0.0d) {
            return 0;
        }
        return number.intValue();
    }

    private static boolean looseEquals(Object left, Object right) {
        if (sameValueZero(left, right)) {
            return true;
        }
        Double leftNumber = asNumber(left);
        Double rightNumber = asNumber(right);
        return leftNumber != null && rightNumber != null && Double.compare(leftNumber, rightNumber) == 0;
    }

    static boolean sameValueZero(Object left, Object right) {
        if (left == right) {
            return true;
        }
        if (left instanceof Number leftNumber && right instanceof Number rightNumber) {
            double leftValue = leftNumber.doubleValue();
            double rightValue = rightNumber.doubleValue();
            return Double.isNaN(leftValue) && Double.isNaN(rightValue) || leftValue == rightValue;
        }
        if (isObjectLike(left) || isObjectLike(right)) {
            return false;
        }
        return Objects.equals(left, right);
    }

    private static boolean isObjectLike(Object value) {
        return value instanceof Map<?, ?>
                || value instanceof List<?>
                || value instanceof QinRuntimeObject
                || value instanceof JavaEsmMapObject
                || value instanceof JavaEsmSetObject
                || value instanceof JavaEsmArrayObject
                || value instanceof JavaEsmTypedArray
                || value instanceof InterpretedFunction
                || value instanceof Class<?>
                || value != null && value.getClass().isArray();
    }

    private static String typeOf(Object value) {
        if (value == null) {
            return "undefined";
        }
        if (value instanceof String) {
            return "string";
        }
        if (value instanceof Number) {
            return "number";
        }
        if (value instanceof Boolean) {
            return "boolean";
        }
        if (value instanceof JavaEsmSymbol.JavaSymbol) {
            return "symbol";
        }
        if (value instanceof QinCallable || value instanceof Method || isFunctionDefinition(value)) {
            return "function";
        }
        return "object";
    }

    private static Double asNumber(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value instanceof String text) {
            String trimmed = text.trim();
            if (trimmed.isEmpty()) {
                return null;
            }
            if (!"NaN".equals(trimmed)
                    && !"Infinity".equals(trimmed)
                    && !"+Infinity".equals(trimmed)
                    && !"-Infinity".equals(trimmed)
                    && !isJsDecimalNumberLiteral(trimmed)) {
                return null;
            }
            return Double.parseDouble(trimmed);
        }
        return null;
    }

    private static boolean isJsDecimalNumberLiteral(String text) {
        int length = text.length();
        int index = 0;
        if (index < length && (text.charAt(index) == '+' || text.charAt(index) == '-')) {
            index++;
        }

        boolean digitsBeforeDot = false;
        while (index < length && isAsciiDigit(text.charAt(index))) {
            digitsBeforeDot = true;
            index++;
        }

        boolean digitsAfterDot = false;
        if (index < length && text.charAt(index) == '.') {
            index++;
            while (index < length && isAsciiDigit(text.charAt(index))) {
                digitsAfterDot = true;
                index++;
            }
        }

        if (!digitsBeforeDot && !digitsAfterDot) {
            return false;
        }

        if (index < length && (text.charAt(index) == 'e' || text.charAt(index) == 'E')) {
            index++;
            if (index < length && (text.charAt(index) == '+' || text.charAt(index) == '-')) {
                index++;
            }
            boolean exponentDigits = false;
            while (index < length && isAsciiDigit(text.charAt(index))) {
                exponentDigits = true;
                index++;
            }
            if (!exponentDigits) {
                return false;
            }
        }

        return index == length;
    }

    private static boolean isAsciiDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private static Double toJsNumber(Object value) {
        if (value == null) {
            return 0.0d;
        }
        if (value instanceof Boolean bool) {
            return bool ? 1.0d : 0.0d;
        }
        if (value instanceof String text && text.trim().isEmpty()) {
            return 0.0d;
        }
        Double number = asNumber(value);
        return number == null ? Double.NaN : number;
    }

    private static double jsNumericOperand(Object value) {
        return toJsNumber(value);
    }

    private static Double requireNumber(Double value, String operator) {
        if (value == null) {
            throw new IllegalArgumentException("Expected numeric operand for " + operator);
        }
        return value;
    }

    private static boolean truthy(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value instanceof Number number) {
            return number.doubleValue() != 0.0d && !Double.isNaN(number.doubleValue());
        }
        if (value instanceof String text) {
            return !text.isEmpty();
        }
        return true;
    }

    static Iterable<?> asIterableForOf(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("for...of cannot iterate null");
        }
        if (value instanceof Iterable<?> iterable) {
            return iterable;
        }
        if (value instanceof CharSequence text) {
            List<String> chars = new ArrayList<>(text.length());
            for (int i = 0; i < text.length(); i++) {
                chars.add(String.valueOf(text.charAt(i)));
            }
            return chars;
        }
        if (value instanceof JavaEsmSetObject setObject) {
            return setObject.values();
        }
        if (value instanceof JavaEsmMapObject mapObject) {
            return mapObject.entries();
        }
        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            List<Object> items = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                items.add(Array.get(value, i));
            }
            return items;
        }
        if (value instanceof Map<?, ?> map) {
            List<Object> entries = new ArrayList<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                entries.add(List.of(entry.getKey(), entry.getValue()));
            }
            return entries;
        }
        Object iteratorMethod = __qin_member_get__(value, JavaEsmSymbol.iterator());
        if (iteratorMethod != null) {
            Object iterator = callRuntimeMethodValue(value, iteratorMethod);
            return iterableFromJsIterator(iterator);
        }
        throw new IllegalArgumentException("Unsupported for...of target: " + simpleName(value));
    }

    private static Iterable<Object> iterableFromJsIterator(Object iterator) {
        if (iterator == null) {
            throw new IllegalArgumentException("Iterator method returned null");
        }
        if (iterator instanceof Iterator<?> javaIterator) {
            return () -> new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return javaIterator.hasNext();
                }

                @Override
                public Object next() {
                    return javaIterator.next();
                }
            };
        }
        return () -> new Iterator<>() {
            private Object nextValue;
            private boolean nextDone;
            private boolean loaded;

            @Override
            public boolean hasNext() {
                load();
                return !nextDone;
            }

            @Override
            public Object next() {
                load();
                if (nextDone) {
                    throw new NoSuchElementException();
                }
                Object value = nextValue;
                loaded = false;
                nextValue = null;
                return value;
            }

            private void load() {
                if (loaded) {
                    return;
                }
                Object nextFunction = __qin_member_get__(iterator, "next");
                if (nextFunction == null) {
                    throw new IllegalArgumentException("Iterator object does not expose next()");
                }
                Object result = callRuntimeMethodValue(iterator, nextFunction);
                nextDone = truthy(__qin_member_get__(result, "done"));
                nextValue = __qin_member_get__(result, "value");
                loaded = true;
            }
        };
    }

    private static Object createErrorObject(String name, Object[] args) {
        LinkedHashMap<String, Object> error = new LinkedHashMap<>();
        String message = args.length == 0 || args[0] == null ? "" : String.valueOf(args[0]);
        error.put("name", name);
        error.put("message", message);
        return error;
    }

    private static String jsString(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Number number) {
            double doubleValue = number.doubleValue();
            if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
                return String.valueOf(doubleValue);
            }
            if (doubleValue == Math.rint(doubleValue)) {
                return Long.toString((long) doubleValue);
            }
            return String.valueOf(doubleValue);
        }
        if (value instanceof Double number) {
            if (number.isNaN() || number.isInfinite()) {
                return String.valueOf(number);
            }
            if (number == Math.rint(number)) {
                return Long.toString(number.longValue());
            }
            return String.valueOf(number);
        }
        if (value instanceof Float number) {
            if (number.isNaN() || number.isInfinite()) {
                return String.valueOf(number);
            }
            if (number == Math.rint(number)) {
                return Long.toString(number.longValue());
            }
            return String.valueOf(number);
        }
        return String.valueOf(value);
    }

    private static String simpleName(Object value) {
        return value == null ? "null" : value.getClass().getSimpleName();
    }

    private static String summarizeRuntimeValue(Object value) {
        return summarizeRuntimeValue(value, 0, new IdentityHashMap<>());
    }

    private static String runtimeStackHint() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        List<String> frames = new ArrayList<>();
        for (StackTraceElement frame : stack) {
            String className = frame.getClassName();
            if (className.startsWith("com.qin.runtime.generated.")
                    || className.startsWith("com.qin.lang.runtime.JavaEsmGlobal")) {
                frames.add(className + "." + frame.getMethodName());
            }
            if (frames.size() >= 8) {
                break;
            }
        }
        return frames.toString();
    }

    private static String summarizeRuntimeValue(Object value, int depth, IdentityHashMap<Object, Boolean> seen) {
        if (value == null) {
            return "null";
        }
        if (depth > 5) {
            return simpleName(value);
        }
        if (value instanceof String text) {
            return "\"" + truncateRuntimeSummary(text) + "\"";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }
        if (seen.containsKey(value)) {
            return "[Circular " + simpleName(value) + "]";
        }
        seen.put(value, Boolean.TRUE);
        if (value instanceof InterpretedInstance instance) {
            Map<String, Object> fields = instance.fieldSnapshot();
            Object name = fields.get("name");
            Object message = fields.get("message");
            return "InterpretedInstance(name="
                    + summarizeRuntimeValue(name, depth + 1, seen)
                    + ", message="
                    + summarizeRuntimeValue(message, depth + 1, seen)
                    + ", fields="
                    + summarizeRuntimeMap(fields, depth + 1, seen, 16)
                    + ", methods="
                    + summarizeRuntimeCollection(instance.methodNames(), depth + 1, seen, 12)
                    + ")";
        }
        if (value instanceof Map<?, ?> map) {
            Object name = map.get("name");
            Object message = map.get("message");
            if (name != null || message != null) {
                return "MapError(name="
                        + summarizeRuntimeValue(name, depth + 1, seen)
                        + ", message="
                        + summarizeRuntimeValue(message, depth + 1, seen)
                        + ", fields="
                        + summarizeRuntimeMap(map, depth + 1, seen, 16)
                        + ")";
            }
            return "Map" + summarizeRuntimeMap(map, depth + 1, seen, 10);
        }
        if (value instanceof Collection<?> collection) {
            return simpleName(value) + summarizeRuntimeCollection(collection, depth + 1, seen, 10);
        }
        String type = value.getClass().getName();
        String text = String.valueOf(value);
        text = truncateRuntimeSummary(text);
        return type + "(" + text + ")";
    }

    private static String summarizeRuntimeMap(
            Map<?, ?> map,
            int depth,
            IdentityHashMap<Object, Boolean> seen,
            int limit) {
        StringBuilder builder = new StringBuilder("{");
        int index = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (index > 0) {
                builder.append(", ");
            }
            if (index >= limit) {
                builder.append("... +").append(map.size() - index).append(" more");
                break;
            }
            builder.append(String.valueOf(entry.getKey()))
                    .append("=")
                    .append(summarizeRuntimeValue(entry.getValue(), depth + 1, seen));
            index++;
        }
        return builder.append("}").toString();
    }

    private static String summarizeRuntimeCollection(
            Collection<?> collection,
            int depth,
            IdentityHashMap<Object, Boolean> seen,
            int limit) {
        StringBuilder builder = new StringBuilder("[");
        int index = 0;
        for (Object item : collection) {
            if (index > 0) {
                builder.append(", ");
            }
            if (index >= limit) {
                builder.append("... +").append(collection.size() - index).append(" more");
                break;
            }
            builder.append(summarizeRuntimeValue(item, depth + 1, seen));
            index++;
        }
        return builder.append("]").toString();
    }

    private static String truncateRuntimeSummary(String text) {
        if (text.length() > 240) {
            return text.substring(0, 240) + "...";
        }
        return text;
    }

    private static final Object BUILTIN_MISS = new Object();

    private static Object tryCallBuiltinNamespace(Object target, String methodName, Object[] args) {
        if (!(target instanceof String builtinName)) {
            return BUILTIN_MISS;
        }
        return switch (builtinName) {
            case "Math" -> invokeBuiltinNamespace(builtinName, methodName, args);
            case "JSON" -> invokeBuiltinNamespace(builtinName, methodName, args);
            case "Number" -> invokeBuiltinNamespace(builtinName, methodName, args);
            case "Array" -> invokeBuiltinNamespace(builtinName, methodName, args);
            case "Object" -> invokeBuiltinNamespace(builtinName, methodName, args);
            case "String" -> invokeBuiltinNamespace(builtinName, methodName, args);
            case "Date" -> invokeBuiltinNamespace(builtinName, methodName, args);
            case "Symbol" -> invokeSymbolNamespace(methodName, args);
            case "Promise" -> invokePromiseNamespace(methodName, args);
            case "fs", "node:fs", "path", "node:path", "url", "node:url", "util", "node:util",
                    "process", "node:process", "module", "node:module", "crypto", "node:crypto",
                    "tty", "node:tty" -> invokeHostRuntimeNamespace(builtinName, methodName, args);
            default -> BUILTIN_MISS;
        };
    }

    private static Object tryReadBuiltinNamespaceMember(String builtinName, Object property) {
        String key = propertyKey(property);
        if ("Object".equals(builtinName) && "prototype".equals(key)) {
            return ObjectPrototype.INSTANCE;
        }
        Object method = tryReadBuiltinNamespaceMethod(builtinName, key);
        if (method != BUILTIN_MISS) {
            return method;
        }
        return switch (builtinName) {
            case "Number" -> {
                Object value = JavaEsmNumber.staticMemberGet(property);
                yield value == null ? BUILTIN_MISS : value;
            }
            case "Symbol" -> {
                if ("iterator".equals(key)) {
                    yield JavaEsmSymbol.iterator();
                }
                yield BUILTIN_MISS;
            }
            case "process", "node:process" -> {
                Object value = NodeHostRuntime.processMember(key);
                yield value == null ? BUILTIN_MISS : value;
            }
            default -> BUILTIN_MISS;
        };
    }

    private static Object tryReadGlobalObjectBuiltin(String key) {
        return switch (key) {
            case "Math", "JSON", "Number", "Object", "Array", "Map", "Set", "Proxy", "Promise", "Symbol",
                    "WeakMap", "WeakSet", "Date", "String", "Boolean",
                    "Uint8Array", "Uint16Array", "Uint32Array", "TextDecoder", "URLSearchParams",
                    "RegExp", "Error", "TypeError", "RangeError", "ReferenceError", "SyntaxError" -> key;
            case "Infinity" -> Double.POSITIVE_INFINITY;
            case "NaN", "undefined" -> null;
            default -> BUILTIN_MISS;
        };
    }

    private static Object tryReadBuiltinNamespaceMethod(String builtinName, String methodName) {
        Class<?> owner = switch (builtinName) {
            case "Math" -> JavaEsmMath.class;
            case "JSON" -> JavaEsmJson.class;
            case "Number" -> JavaEsmNumber.class;
            case "Array" -> JavaEsmArray.class;
            case "Object" -> JavaEsmObject.class;
            case "Date" -> JavaEsmDate.class;
            default -> null;
        };
        if (owner == null) {
            return BUILTIN_MISS;
        }
        for (Method method : owner.getMethods()) {
            if (Modifier.isStatic(method.getModifiers()) && method.getName().equals(methodName)) {
                return new NativeFunction(methodName, args -> invokeBuiltinNamespace(builtinName, methodName, args));
            }
        }
        return BUILTIN_MISS;
    }

    private static Object tryCallObjectPrototypeMethod(Object target, String methodName, Object[] args) {
        return switch (methodName) {
            case "hasOwnProperty" -> {
                if (args.length != 1) {
                    throw new IllegalArgumentException("Object.prototype.hasOwnProperty expects exactly 1 argument");
                }
                yield JavaEsmObject.hasOwn(target, args[0]);
            }
            default -> BUILTIN_MISS;
        };
    }

    private static Object invokeSymbolNamespace(String methodName, Object[] args) {
        return switch (methodName) {
            case "for" -> JavaEsmSymbol.for_(args.length == 0 ? "" : args[0]);
            case "keyFor" -> {
                if (args.length != 1) {
                    throw new IllegalArgumentException("Symbol.keyFor expects exactly 1 argument");
                }
                yield JavaEsmSymbol.keyFor(args[0]);
            }
            default -> BUILTIN_MISS;
        };
    }

    private static Object invokePromiseNamespace(String methodName, Object[] args) {
        return switch (methodName) {
            case "resolve" -> ImmediatePromise.resolved(args.length == 0 ? null : args[0]);
            case "reject" -> ImmediatePromise.rejected(args.length == 0 ? null : args[0]);
            default -> BUILTIN_MISS;
        };
    }

    private static Object invokeBuiltinNamespace(String builtinName, String methodName, Object[] args) {
        try {
            Class<?> owner = switch (builtinName) {
                case "Math" -> JavaEsmMath.class;
                case "JSON" -> JavaEsmJson.class;
                case "Number" -> JavaEsmNumber.class;
                case "Array" -> JavaEsmArray.class;
                case "Object" -> JavaEsmObject.class;
                case "String" -> JavaEsmString.class;
                case "Date" -> JavaEsmDate.class;
                default -> null;
            };
            if (owner == null) {
                return BUILTIN_MISS;
            }
            Method method = findCompatibleMethod(owner, methodName, args.length, true);
            if (method == null) {
                return BUILTIN_MISS;
            }
            Object[] invokeArgs = method.isVarArgs()
                    ? adaptVarArgs(args, method.getParameterTypes())
                    : coerceArguments(args, method.getParameterTypes());
            return method.invoke(null, invokeArgs);
        } catch (IllegalAccessException | InvocationTargetException error) {
            throw new IllegalArgumentException(
                    "Failed to invoke builtin namespace method: " + builtinName + "." + methodName,
                    error);
        }
    }

    private static Object invokeHostRuntimeNamespace(String builtinName, String methodName, Object[] args) {
        try {
            Method method = switch (builtinName) {
                case "fs", "node:fs" -> findHostMethod(NodeHostRuntime.class, methodName, args,
                        Map.of(
                                "readFileSync", args.length >= 2
                                        ? new Class<?>[] {Object.class, Object.class}
                                        : new Class<?>[] {Object.class},
                                "existsSync", new Class<?>[] {Object.class},
                                "writeFileSync", new Class<?>[] {Object.class, Object.class},
                                "appendFileSync", new Class<?>[] {Object.class, Object.class},
                                "mkdirSync", args.length >= 2
                                        ? new Class<?>[] {Object.class, Object.class}
                                        : new Class<?>[] {Object.class},
                                "createWriteStream", args.length >= 2
                                        ? new Class<?>[] {Object.class, Object.class}
                                        : new Class<?>[] {Object.class}
                        ));
                case "path", "node:path" -> findHostMethod(NodeHostRuntime.class, methodName, args,
                        Map.of(
                                "dirname", new Class<?>[] {Object.class},
                                "relative", new Class<?>[] {Object.class, Object.class},
                                "join", new Class<?>[] {Object[].class},
                                "resolve", new Class<?>[] {Object[].class}
                        ));
                case "url", "node:url" -> findHostMethod(NodeHostRuntime.class, methodName, args,
                        Map.of("fileURLToPath", new Class<?>[] {Object.class}));
                case "util", "node:util" -> findHostMethod(NodeHostRuntime.class, methodName, args,
                        Map.of(
                                "deprecate", new Class<?>[] {Object.class, Object.class},
                                "formatWithOptions", new Class<?>[] {Object.class, Object[].class},
                                "inspect", new Class<?>[] {Object.class}
                        ));
                case "process", "node:process" -> findHostMethod(NodeHostRuntime.class, methodName, args,
                        Map.of("cwd", new Class<?>[] {}));
                case "module", "node:module" -> findHostMethod(NodeHostRuntime.class, methodName, args,
                        Map.of("createRequire", new Class<?>[] {Object.class}));
                case "crypto", "node:crypto" -> findHostMethod(NodeHostRuntime.class, methodName, args,
                        Map.of("hash", new Class<?>[] {Object.class, Object.class, Object.class}));
                case "tty", "node:tty" -> findHostMethod(NodeHostRuntime.class, methodName, args,
                        Map.of("isatty", new Class<?>[] {Object.class}));
                default -> null;
            };
            if (method == null) {
                return BUILTIN_MISS;
            }
            if (method.isVarArgs()) {
                return method.invoke(null, adaptVarArgs(args, method.getParameterTypes()));
            }
            return method.invoke(null, coerceArguments(args, method.getParameterTypes()));
        } catch (IllegalAccessException | InvocationTargetException error) {
            throw new IllegalArgumentException(
                    "Failed to invoke host runtime namespace method: " + builtinName + "." + methodName,
                    error);
        }
    }

    private static Method findHostMethod(
            Class<?> owner,
            String methodName,
            Object[] args,
            Map<String, Class<?>[]> signatures) {
        Class<?>[] parameterTypes = signatures.get(methodName);
        if (parameterTypes == null) {
            return null;
        }
        try {
            Method method = owner.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException error) {
            return null;
        }
    }

    private static Object[] adaptVarArgs(Object[] args, Class<?>[] parameterTypes) {
        return adaptVarArgs(args, parameterTypes, parameterTypes);
    }

    private static Object[] adaptVarArgs(Object[] args, Class<?>[] parameterTypes, Type[] genericParameterTypes) {
        int varArgIndex = parameterTypes.length - 1;
        if (varArgIndex < 0 || !parameterTypes[varArgIndex].isArray()) {
            return coerceArguments(args, parameterTypes, genericParameterTypes);
        }
        Object[] adapted = new Object[parameterTypes.length];
        for (int i = 0; i < varArgIndex; i++) {
            Type genericParameterType = genericParameterTypes == null || i >= genericParameterTypes.length
                    ? parameterTypes[i]
                    : genericParameterTypes[i];
            adapted[i] = coerceArgument(i < args.length ? args[i] : null, parameterTypes[i], genericParameterType);
        }
        Class<?> componentType = parameterTypes[varArgIndex].getComponentType();
        int varArgLength = Math.max(0, args.length - varArgIndex);
        Object packed = Array.newInstance(componentType, varArgLength);
        for (int i = 0; i < varArgLength; i++) {
            Array.set(packed, i, coerceArgument(args[varArgIndex + i], componentType, componentType));
        }
        adapted[varArgIndex] = packed;
        return adapted;
    }

    private static boolean isCompatibleArity(Method method, int argCount) {
        if (!method.isVarArgs()) {
            return method.getParameterCount() == argCount;
        }
        return argCount >= method.getParameterCount() - 1;
    }

    private static boolean areCompatibleArguments(Object[] args, Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (!method.isVarArgs()) {
            if (args.length != parameterTypes.length) {
                return false;
            }
            for (int i = 0; i < args.length; i++) {
                Type genericParameterType = genericParameterTypes == null || i >= genericParameterTypes.length
                        ? parameterTypes[i]
                        : genericParameterTypes[i];
                if (!canCoerceArgument(args[i], parameterTypes[i], genericParameterType)) {
                    return false;
                }
            }
            return true;
        }
        int fixedCount = parameterTypes.length - 1;
        if (args.length < fixedCount) {
            return false;
        }
        for (int i = 0; i < fixedCount; i++) {
            Type genericParameterType = genericParameterTypes == null || i >= genericParameterTypes.length
                    ? parameterTypes[i]
                    : genericParameterTypes[i];
            if (!canCoerceArgument(args[i], parameterTypes[i], genericParameterType)) {
                return false;
            }
        }
        Class<?> componentType = parameterTypes[fixedCount].getComponentType();
        for (int i = fixedCount; i < args.length; i++) {
            if (!canCoerceArgument(args[i], componentType, componentType)) {
                return false;
            }
        }
        return true;
    }

    private static boolean canCoerceArgument(Object value, Class<?> parameterType) {
        return canCoerceArgument(value, parameterType, parameterType);
    }

    private static boolean canCoerceArgument(Object value, Class<?> parameterType, Type genericParameterType) {
        if (value == null) {
            return !parameterType.isPrimitive();
        }
        if (canCoerceTypedFfiArgument(value, parameterType, genericParameterType)) {
            return true;
        }
        if (parameterType.isInstance(value)) {
            return true;
        }
        if (parameterType == String.class) {
            return true;
        }
        if (parameterType.isPrimitive()) {
            return primitiveWrapperType(parameterType).isInstance(value) || value instanceof Number;
        }
        return false;
    }

    private static boolean canCoerceTypedFfiArgument(Object value, Class<?> parameterType, Type genericParameterType) {
        if (isJavaFunctionalInterface(parameterType)) {
            return parameterType.isInstance(value) || isRuntimeCallableValue(value);
        }
        if (parameterType.isRecord()) {
            return isJavaRecordObjectShape(value, parameterType);
        }
        if (isSubhutiAlternativeType(parameterType)) {
            return isSubhutiAlternativeShape(value);
        }
        if (List.class.isAssignableFrom(parameterType) && isSubhutiAlternativeType(listElementType(genericParameterType))) {
            if (!(value instanceof List<?> list)) {
                return false;
            }
            for (Object item : list) {
                if (!isSubhutiAlternativeShape(item)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isJavaRecordObjectShape(Object value, Class<?> recordType) {
        if (recordType.isInstance(value)) {
            return true;
        }
        if (value != null && value.getClass().isRecord()) {
            return true;
        }
        if (!(value instanceof Map<?, ?> rawMap)) {
            return false;
        }
        Map<String, Object> map = castMap(rawMap);
        JavaRecordInfo recordInfo = javaRecordInfo(recordType);
        for (String key : map.keySet()) {
            boolean matched = false;
            for (JavaRecordComponentInfo component : recordInfo.components()) {
                if (component.keys().contains(key)) {
                    matched = true;
                    break;
                }
            }
            if (!matched
                    && isSlimeParserParamsRecord(recordType)
                    && SLIME_PARSER_PARAM_BOUNDARY_KEYS.contains(key)) {
                matched = true;
            }
            if (!matched) {
                return false;
            }
        }
        if (recordInfo.hasDefaultRecord()) {
            return true;
        }
        for (JavaRecordComponentInfo component : recordInfo.components()) {
            boolean present = false;
            for (String key : component.keys()) {
                if (map.containsKey(key)) {
                    present = true;
                    break;
                }
            }
            if (!present && component.type().isPrimitive()) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSubhutiAlternativeShape(Object value) {
        if (value == null) {
            return false;
        }
        try {
            Class<?> alternativeClass = Class.forName(SUBHUTI_ALTERNATIVE_CLASS_NAME);
            if (alternativeClass.isInstance(value)) {
                return true;
            }
        } catch (ClassNotFoundException ignored) {
            return false;
        }
        if (!(value instanceof Map<?, ?> rawMap)) {
            return false;
        }
        Map<String, Object> map = castMap(rawMap);
        return map.size() == 1
                && map.containsKey("alt")
                && isRuntimeCallableValue(JavaEsmObject.resolveStoredPropertyValue(map.get("alt")));
    }

    private static Class<?> primitiveWrapperType(Class<?> primitiveType) {
        if (primitiveType == boolean.class) {
            return Boolean.class;
        }
        if (primitiveType == byte.class) {
            return Byte.class;
        }
        if (primitiveType == short.class) {
            return Short.class;
        }
        if (primitiveType == int.class) {
            return Integer.class;
        }
        if (primitiveType == long.class) {
            return Long.class;
        }
        if (primitiveType == float.class) {
            return Float.class;
        }
        if (primitiveType == double.class) {
            return Double.class;
        }
        if (primitiveType == char.class) {
            return Character.class;
        }
        return Void.class;
    }

    private static Method methodHandle(Class<?> owner, String name, Class<?>... parameterTypes) {
        try {
            Method method = owner.getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException error) {
            throw new IllegalStateException("Missing runtime method: " + owner.getName() + "." + name, error);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> castMap(Map<?, ?> raw) {
        return (Map<String, Object>) raw;
    }

    private static String propertyKey(Object property) {
        if (property instanceof JavaEsmSymbol.JavaSymbol symbol) {
            return "\u0000symbol:" + symbol.id();
        }
        if (property instanceof Number number) {
            double value = number.doubleValue();
            if (Double.isFinite(value) && value == Math.rint(value)) {
                long integer = (long) value;
                return String.valueOf(integer);
            }
        }
        return String.valueOf(property);
    }

    private static boolean isFunctionDefinition(Object value) {
        if (!(value instanceof Map<?, ?> raw)) {
            return false;
        }
        Map<String, Object> definition = castMap(raw);
        return definition.containsKey("__qin_function_model")
                && (definition.get("ast") instanceof Map<?, ?> || definition.get("astRef") != null)
                && definition.get("closure") instanceof Map<?, ?>;
    }

    private interface QinCallable {
        Object call(Object... args);
    }

    private interface QinRuntimeObject {
        Object get(Object property);

        Object set(Object property, Object value);

        boolean has(Object property);
    }

    static boolean isRuntimeHiddenObjectKey(String key) {
        return key != null && key.startsWith(RUNTIME_HIDDEN_KEY_PREFIX);
    }

    private static Object mapPrototypeChainValue(Map<String, Object> object, String key) {
        if (isRuntimeHiddenObjectKey(key)) {
            return BUILTIN_MISS;
        }
        Object prototype = object.get(PROTOTYPE_PARENT_KEY);
        IdentityHashMap<Object, Boolean> seen = new IdentityHashMap<>();
        while (prototype instanceof Map<?, ?> rawPrototype && !seen.containsKey(prototype)) {
            seen.put(prototype, Boolean.TRUE);
            Map<String, Object> prototypeMap = castMap(rawPrototype);
            if (prototypeMap.containsKey(key) && !isRuntimeHiddenObjectKey(key)) {
                return normalizeRuntimeMemberValue(
                        JavaEsmObject.resolveStoredPropertyValue(prototypeMap.get(key)));
            }
            prototype = prototypeMap.get(PROTOTYPE_PARENT_KEY);
        }
        return BUILTIN_MISS;
    }

    private static boolean mapPrototypeChainHas(Map<String, Object> object, String key) {
        if (isRuntimeHiddenObjectKey(key)) {
            return false;
        }
        Object prototype = object.get(PROTOTYPE_PARENT_KEY);
        IdentityHashMap<Object, Boolean> seen = new IdentityHashMap<>();
        while (prototype instanceof Map<?, ?> rawPrototype && !seen.containsKey(prototype)) {
            seen.put(prototype, Boolean.TRUE);
            Map<String, Object> prototypeMap = castMap(rawPrototype);
            if (prototypeMap.containsKey(key) && !isRuntimeHiddenObjectKey(key)) {
                return true;
            }
            prototype = prototypeMap.get(PROTOTYPE_PARENT_KEY);
        }
        return false;
    }

    private static Object objectPrototypeMember(Object receiver, String name) {
        return switch (name) {
            case "hasOwnProperty" -> new NativeFunction("hasOwnProperty", args ->
                    JavaEsmObject.hasOwn(receiver, args.length == 0 ? null : args[0]));
            case "propertyIsEnumerable" -> new NativeFunction("propertyIsEnumerable", args ->
                    JavaEsmObject.hasOwn(receiver, args.length == 0 ? null : args[0]));
            case "toString" -> new NativeFunction("toString", args -> objectToStringTag(receiver));
            default -> BUILTIN_MISS;
        };
    }

    private static Object objectToStringTag(Object receiver) {
        Object value = __qin_value__(receiver);
        String tag;
        if (value == null) {
            tag = "Null";
        } else if (value instanceof String || value instanceof CharSequence) {
            tag = "String";
        } else if (value instanceof Number) {
            tag = "Number";
        } else if (value instanceof Boolean) {
            tag = "Boolean";
        } else if (value instanceof List<?> || value instanceof JavaEsmArrayObject || value.getClass().isArray()) {
            tag = "Array";
        } else if (value instanceof JavaEsmMapObject) {
            tag = "Map";
        } else if (value instanceof JavaEsmSetObject) {
            tag = "Set";
        } else {
            tag = "Object";
        }
        return "[object " + tag + "]";
    }

    private static final class JavaRuntimeClass implements QinRuntimeObject {
        private final InterpretedFunction classFunction;
        private final JavaRuntimeClass superClass;
        private final String simpleName;
        private final String binaryName;

        private JavaRuntimeClass(InterpretedFunction classFunction, JavaRuntimeClass superClass) {
            this.classFunction = classFunction;
            this.superClass = superClass;
            this.simpleName = classFunction.classDebugName();
            this.binaryName = simpleName;
        }

        @Override
        public Object get(Object property) {
            String key = propertyKey(property);
            return switch (key) {
                case "getName" -> new NativeFunction("Class.getName", args -> binaryName);
                case "getSimpleName" -> new NativeFunction("Class.getSimpleName", args -> simpleName);
                case "getSuperclass" -> new NativeFunction("Class.getSuperclass", args -> superClass);
                case "getMethod", "getDeclaredMethod" -> new NativeFunction("Class." + key, args -> {
                    if (args.length == 0) {
                        throw new IllegalArgumentException("Class.getDeclaredMethod requires a method name");
                    }
                    String methodName = String.valueOf(args[0]);
                    InterpretedFunction method = "getDeclaredMethod".equals(key)
                            ? classFunction.findDeclaredInstanceMethod(methodName)
                            : classFunction.findInstanceMethod(methodName);
                    if (method == null) {
                        throw noSuchMethod(binaryName + "." + methodName);
                    }
                    return new JavaRuntimeMethod(binaryName, methodName, method);
                });
                default -> null;
            };
        }

        @Override
        public Object set(Object property, Object value) {
            return value;
        }

        @Override
        public boolean has(Object property) {
            return get(property) != null;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof JavaRuntimeClass runtimeClass && classFunction == runtimeClass.classFunction;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(classFunction);
        }

        @Override
        public String toString() {
            return "class " + binaryName;
        }
    }

    private static Object optionalCallMethod(Object target, Object methodName, Object... args) {
        target = unwrapExportSlotValue(target);
        if (target == null || !hasCallableMember(target, methodName)) {
            return null;
        }
        return callMethod(target, methodName, args);
    }

    private static boolean hasCallableMember(Object target, Object property) {
        target = unwrapExportSlotValue(target);
        if (target == null) {
            return false;
        }
        String name = propertyKey(property);
        if (target instanceof QinRuntimeObject runtimeObject) {
            return runtimeObject.has(property)
                    || tryCallObjectPrototypeMethod(target, name, new Object[0]) != BUILTIN_MISS;
        }
        if (target instanceof Map<?, ?> map) {
            return JavaEsmObject.resolveStoredPropertyValue(castMap(map).get(name)) != null;
        }
        if (target instanceof CharSequence && JavaEsmString.supports(name)) {
            return true;
        }
        if (target instanceof Number && JavaEsmNumber.supports(name)) {
            return true;
        }
        if (target instanceof List<?> && JavaEsmArray.supports(name)) {
            return true;
        }
        if (target instanceof JavaEsmRegExp regexp && regexp.supports(name)) {
            return true;
        }
        Class<?> ownerClass = target instanceof Class<?> clazz ? clazz : target.getClass();
        return findCompatibleMethod(ownerClass, name, new Object[0], target instanceof Class<?>) != null;
    }

    private static ThrownValue noSuchMethod(String message) {
        return new ThrownValue(new JavaRuntimeThrowable(
                "__QinJavaLangNoSuchMethodException",
                message,
                List.of(
                        "__QinJavaLangNoSuchMethodException",
                        "__QinJavaLangReflectiveOperationException",
                        "__QinJavaLangException",
                        "__QinJavaLangThrowable")));
    }

    private static final class JavaRuntimeThrowable implements QinRuntimeObject {
        private final String name;
        private final String message;
        private final List<String> typeNames;

        private JavaRuntimeThrowable(String name, String message, List<String> typeNames) {
            this.name = name;
            this.message = message;
            this.typeNames = List.copyOf(typeNames);
        }

        private boolean isInstanceOf(String constructorName) {
            return typeNames.contains(constructorName);
        }

        @Override
        public Object get(Object property) {
            return switch (propertyKey(property)) {
                case "name" -> name;
                case "message" -> message;
                case "getMessage" -> new NativeFunction("Throwable.getMessage", args -> message);
                case "getCause" -> new NativeFunction("Throwable.getCause", args -> null);
                case "toString" -> new NativeFunction("Throwable.toString", args -> toString());
                default -> null;
            };
        }

        @Override
        public Object set(Object property, Object value) {
            return value;
        }

        @Override
        public boolean has(Object property) {
            return get(property) != null;
        }

        @Override
        public String toString() {
            return message == null || message.isBlank() ? name : name + ": " + message;
        }
    }

    private static final class JavaRuntimeMethod implements QinRuntimeObject {
        private final String ownerName;
        private final String name;
        private final InterpretedFunction method;

        private JavaRuntimeMethod(String ownerName, String name, InterpretedFunction method) {
            this.ownerName = ownerName;
            this.name = name;
            this.method = method;
        }

        @Override
        public Object get(Object property) {
            return switch (propertyKey(property)) {
                case "getName" -> new NativeFunction("Method.getName", args -> name);
                case "setAccessible" -> new NativeFunction("Method.setAccessible", args -> null);
                case "invoke" -> new NativeFunction("Method.invoke", args -> {
                    if (args.length == 0) {
                        throw new IllegalArgumentException("Method.invoke requires a receiver");
                    }
                    Object receiver = unwrapExportSlotValue(args[0]);
                    Object[] invokeArgs = args.length <= 1 ? new Object[0] : Arrays.copyOfRange(args, 1, args.length);
                    return method.bindThis(receiver).call(invokeArgs);
                });
                default -> null;
            };
        }

        @Override
        public Object set(Object property, Object value) {
            return value;
        }

        @Override
        public boolean has(Object property) {
            return get(property) != null;
        }

        @Override
        public String toString() {
            return ownerName + "." + name + "()";
        }
    }

    private static final class ObjectPrototype implements QinRuntimeObject {
        private static final ObjectPrototype INSTANCE = new ObjectPrototype();

        @Override
        public Object get(Object property) {
            Object value = objectPrototypeMember(GLOBAL_OBJECT, propertyKey(property));
            return value == BUILTIN_MISS ? null : value;
        }

        @Override
        public Object set(Object property, Object value) {
            return value;
        }

        @Override
        public boolean has(Object property) {
            return objectPrototypeMember(GLOBAL_OBJECT, propertyKey(property)) != BUILTIN_MISS;
        }
    }

    private static final class NativeFunction implements QinCallable, QinRuntimeObject {
        private final String name;
        private final QinCallable callable;
        private final Map<String, Object> ownProperties = new LinkedHashMap<>();

        private NativeFunction(String name, QinCallable callable) {
            this.name = name;
            this.callable = callable;
        }

        @Override
        public Object call(Object... args) {
            return callable.call(args);
        }

        @Override
        public Object get(Object property) {
            String key = propertyKey(property);
            if (ownProperties.containsKey(key)) {
                return ownProperties.get(key);
            }
            if ("name".equals(key)) {
                return name;
            }
            if ("call".equals(key)) {
                return new NativeFunction(name + ".call", args -> {
                    Object receiver = args.length == 0 ? GLOBAL_OBJECT : args[0];
                    Object[] callArgs = args.length <= 1 ? new Object[0] : Arrays.copyOfRange(args, 1, args.length);
                    if ("hasOwnProperty".equals(name)) {
                        return JavaEsmObject.hasOwn(receiver, callArgs.length == 0 ? null : callArgs[0]);
                    }
                    if ("propertyIsEnumerable".equals(name)) {
                        return JavaEsmObject.hasOwn(receiver, callArgs.length == 0 ? null : callArgs[0]);
                    }
                    return callable.call(callArgs);
                });
            }
            if ("apply".equals(key)) {
                return new NativeFunction(name + ".apply", args -> {
                    List<Object> applyArgs = new ArrayList<>();
                    if (args.length > 1) {
                        addRuntimeSpreadValues(applyArgs, args[1]);
                    }
                    return callable.call(applyArgs.toArray());
                });
            }
            if ("bind".equals(key)) {
                return new NativeFunction(name + ".bind", args -> {
                    Object[] boundArgs = args.length <= 1 ? new Object[0] : Arrays.copyOfRange(args, 1, args.length);
                    return new NativeFunction(name + ".bound", callArgs -> {
                        Object[] combined = Arrays.copyOf(boundArgs, boundArgs.length + callArgs.length);
                        System.arraycopy(callArgs, 0, combined, boundArgs.length, callArgs.length);
                        return callable.call(combined);
                    });
                });
            }
            return null;
        }

        @Override
        public Object set(Object property, Object value) {
            ownProperties.put(propertyKey(property), value);
            return value;
        }

        @Override
        public boolean has(Object property) {
            String key = propertyKey(property);
            return ownProperties.containsKey(key)
                    || "name".equals(key)
                    || "call".equals(key)
                    || "apply".equals(key)
                    || "bind".equals(key);
        }
    }

    private static void addRuntimeSpreadValues(List<Object> target, Object spreadValue) {
        if (spreadValue == null) {
            return;
        }
        if (spreadValue instanceof Collection<?> collection) {
            target.addAll(collection);
            return;
        }
        if (spreadValue instanceof JavaEsmSetObject setObject) {
            target.addAll(setObject.values());
            return;
        }
        if (spreadValue.getClass().isArray()) {
            int length = Array.getLength(spreadValue);
            for (int i = 0; i < length; i++) {
                target.add(Array.get(spreadValue, i));
            }
            return;
        }
        throw new IllegalArgumentException(
                "Spread element expects an array-like value; got=" + summarizeRuntimeValue(spreadValue));
    }

    private static final class ImmediatePromise implements QinRuntimeObject {
        private final Object value;
        private final Object error;
        private final boolean rejected;

        private ImmediatePromise(Object value, Object error, boolean rejected) {
            this.value = value;
            this.error = error;
            this.rejected = rejected;
        }

        private static ImmediatePromise resolved(Object value) {
            return new ImmediatePromise(value, null, false);
        }

        private static ImmediatePromise rejected(Object error) {
            return new ImmediatePromise(null, error, true);
        }

        @Override
        public Object get(Object property) {
            return switch (propertyKey(property)) {
                case "then" -> new NativeFunction("Promise.then", args -> then(args.length == 0 ? null : args[0]));
                case "catch" -> new NativeFunction("Promise.catch", args -> catchError(args.length == 0 ? null : args[0]));
                default -> null;
            };
        }

        @Override
        public Object set(Object property, Object value) {
            throw new IllegalArgumentException("Promise properties are read-only in Qin immediate Promise shim");
        }

        @Override
        public boolean has(Object property) {
            String key = propertyKey(property);
            return "then".equals(key) || "catch".equals(key);
        }

        private ImmediatePromise then(Object onFulfilled) {
            if (rejected || onFulfilled == null) {
                return this;
            }
            try {
                return resolved(callAny(onFulfilled, value));
            } catch (Throwable throwable) {
                return rejected(throwable);
            }
        }

        private ImmediatePromise catchError(Object onRejected) {
            if (!rejected || onRejected == null) {
                return this;
            }
            try {
                return resolved(callAny(onRejected, error));
            } catch (Throwable throwable) {
                return rejected(throwable);
            }
        }
    }

    private static final class JavaEsmUrlSearchParams implements QinRuntimeObject, Iterable<Object> {
        private final List<List<Object>> entries = new ArrayList<>();

        private JavaEsmUrlSearchParams(Object init) {
            if (init == null) {
                return;
            }
            String query = String.valueOf(init);
            if (query.startsWith("?")) {
                query = query.substring(1);
            }
            if (query.isBlank()) {
                return;
            }
            for (String pair : query.split("&")) {
                if (pair.isEmpty()) {
                    continue;
                }
                int eq = pair.indexOf('=');
                String key = eq >= 0 ? pair.substring(0, eq) : pair;
                String value = eq >= 0 ? pair.substring(eq + 1) : "";
                entries.add(new ArrayList<>(List.of(decodeUrlComponent(key), decodeUrlComponent(value))));
            }
        }

        @Override
        public Iterator<Object> iterator() {
            return new ArrayList<Object>(entries).iterator();
        }

        @Override
        public Object get(Object property) {
            return switch (propertyKey(property)) {
                case "entries" -> new NativeFunction("URLSearchParams.entries", args -> entries());
                case "get" -> new NativeFunction("URLSearchParams.get", args -> getParam(args.length == 0 ? null : args[0]));
                case "has" -> new NativeFunction("URLSearchParams.has", args -> hasParam(args.length == 0 ? null : args[0]));
                case "forEach" -> new NativeFunction("URLSearchParams.forEach", args -> forEach(args.length == 0 ? null : args[0]));
                default -> null;
            };
        }

        @Override
        public Object set(Object property, Object value) {
            return value;
        }

        @Override
        public boolean has(Object property) {
            String key = propertyKey(property);
            return "entries".equals(key) || "get".equals(key) || "has".equals(key) || "forEach".equals(key);
        }

        private Object entries() {
            return new ArrayList<Object>(entries);
        }

        private Object getParam(Object key) {
            String target = String.valueOf(key);
            for (List<Object> entry : entries) {
                if (entry.size() >= 2 && Objects.equals(String.valueOf(entry.get(0)), target)) {
                    return entry.get(1);
                }
            }
            return null;
        }

        private Object hasParam(Object key) {
            String target = String.valueOf(key);
            for (List<Object> entry : entries) {
                if (!entry.isEmpty() && Objects.equals(String.valueOf(entry.get(0)), target)) {
                    return true;
                }
            }
            return false;
        }

        private Object forEach(Object callback) {
            if (callback == null) {
                return null;
            }
            for (List<Object> entry : entries) {
                Object key = entry.isEmpty() ? null : entry.get(0);
                Object value = entry.size() < 2 ? "" : entry.get(1);
                callAny(callback, value, key, this);
            }
            return null;
        }

        private static String decodeUrlComponent(String value) {
            return java.net.URLDecoder.decode(
                    value.replace("+", "%20"),
                    java.nio.charset.StandardCharsets.UTF_8);
        }
    }

    private static final class ProxyObject implements QinRuntimeObject {
        private final Object target;
        private final Object handler;

        private ProxyObject(Object target, Object handler) {
            this.target = target;
            this.handler = handler;
        }

        @Override
        public Object get(Object property) {
            Object trap = __qin_member_get__(handler, "get");
            if (trap != null) {
                return callAny(trap, target, property);
            }
            return __qin_member_get__(target, property);
        }

        @Override
        public Object set(Object property, Object value) {
            Object trap = __qin_member_get__(handler, "set");
            if (trap != null) {
                return callAny(trap, target, property, value);
            }
            return __qin_member_set__(target, property, value);
        }

        @Override
        public boolean has(Object property) {
            Object trap = __qin_member_get__(handler, "has");
            if (trap != null) {
                return truthy(callAny(trap, target, property));
            }
            return jsIn(property, target);
        }
    }

    private static final class InterpretedInstance implements QinRuntimeObject {
        private final Map<String, Object> fields = new LinkedHashMap<>();
        private final Map<String, InterpretedFunction> methods;
        private final Map<String, AccessorProperty> accessors;
        private final Map<String, InterpretedFunction> superMethods;
        private final Map<String, AccessorProperty> superAccessors;
        private final Map<String, Object> prototypeProperties;
        private final List<Object> prototypeChain;
        private Object constructorFunction;

        private InterpretedInstance(Map<String, InterpretedFunction> methods, Map<String, AccessorProperty> accessors) {
            this(methods, accessors, Map.of(), Map.of(), Map.of(), List.of());
        }

        private InterpretedInstance(
                Map<String, InterpretedFunction> methods,
                Map<String, AccessorProperty> accessors,
                Map<String, InterpretedFunction> superMethods,
                Map<String, AccessorProperty> superAccessors) {
            this(methods, accessors, superMethods, superAccessors, Map.of(), List.of());
        }

        private InterpretedInstance(
                Map<String, InterpretedFunction> methods,
                Map<String, AccessorProperty> accessors,
                Map<String, InterpretedFunction> superMethods,
                Map<String, AccessorProperty> superAccessors,
                Map<String, Object> prototypeProperties,
                List<Object> prototypeChain) {
            this.methods = methods;
            this.accessors = accessors;
            this.superMethods = superMethods;
            this.superAccessors = superAccessors;
            this.prototypeProperties = prototypeProperties;
            this.prototypeChain = List.copyOf(prototypeChain);
        }

        @Override
        public Object get(Object property) {
            String name = propertyKey(property);
            List<Object> javaListItems = interpretedJavaListItems();
            if (javaListItems != null) {
                if ("length".equals(name)) {
                    return (double) javaListItems.size();
                }
                int index = toIndex(name);
                if (index >= 0 && index < javaListItems.size()) {
                    return javaListItems.get(index);
                }
            }
            if (fields.containsKey(name)) {
                return fields.get(name);
            }
            String javaFieldName = javaFieldAliasName(name);
            if (fields.containsKey(javaFieldName)) {
                return fields.get(javaFieldName);
            }
            AccessorProperty accessor = accessors.get(name);
            if (accessor != null && accessor.getter != null) {
                return accessor.getter.bindThis(this).call();
            }
            AccessorProperty superAccessor = superAccessors.get(name);
            if (superAccessor != null && superAccessor.getter != null) {
                return superAccessor.getter.bindThis(this).call();
            }
            if ("constructor".equals(name) && constructorFunction != null) {
                return constructorFunction;
            }
            if ("getClass".equals(name) && constructorFunction instanceof InterpretedFunction classFunction) {
                return new NativeFunction("Object.getClass", args -> classFunction.javaRuntimeClass());
            }
            if ("_markParseFail".equals(name) && methods.containsKey("setParseFail")) {
                return methods.get("setParseFail").bindThis(this);
            }
            Object generatedJavaHashSetMethod = generatedJavaHashSetNativeMethod(this, name);
            if (generatedJavaHashSetMethod != null) {
                return generatedJavaHashSetMethod;
            }
            InterpretedFunction method = methods.get(name);
            if (method != null) {
                return method.bindThis(this);
            }
            Object prototypeValue = prototypeProperties.get(name);
            if (prototypeValue instanceof InterpretedFunction prototypeFunction) {
                return prototypeFunction.bindThis(this);
            }
            if (isFunctionDefinition(prototypeValue)) {
                return new InterpretedFunction(castMap((Map<?, ?>) prototypeValue)).bindThis(this);
            }
            if (prototypeValue != null || prototypeProperties.containsKey(name)) {
                return prototypeValue;
            }
            Object prototypeChainValue = getPrototypeChainValue(name);
            if (prototypeChainValue != null || hasPrototypeChainProperty(name)) {
                return bindPrototypeValue(prototypeChainValue);
            }
            return null;
        }

        @Override
        public Object set(Object property, Object value) {
            String name = propertyKey(property);
            List<Object> javaListItems = interpretedJavaListItems();
            if (javaListItems != null) {
                int index = toIndex(name);
                if (index >= 0) {
                    while (javaListItems.size() <= index) {
                        javaListItems.add(null);
                    }
                    javaListItems.set(index, value);
                    return value;
                }
            }
            String storageName = fields.containsKey(name) ? name : javaFieldAliasName(name);
            if (!fields.containsKey(name) && !fields.containsKey(storageName)) {
                AccessorProperty accessor = accessors.get(name);
                if (accessor != null && accessor.setter != null) {
                    accessor.setter.bindThis(this).call(value);
                    return value;
                }
            }
            fields.put(fields.containsKey(storageName) ? storageName : name, value);
            return value;
        }

        @Override
        public boolean has(Object property) {
            String name = propertyKey(property);
            List<Object> javaListItems = interpretedJavaListItems();
            if (javaListItems != null) {
                if ("length".equals(name)) {
                    return true;
                }
                int index = toIndex(name);
                if (index >= 0 && index < javaListItems.size()) {
                    return true;
                }
            }
            return fields.containsKey(name)
                    || fields.containsKey(javaFieldAliasName(name))
                    || accessors.containsKey(name)
                    || superAccessors.containsKey(name)
                    || methods.containsKey(name)
                    || superMethods.containsKey(name)
                    || prototypeProperties.containsKey(name)
                    || hasPrototypeChainProperty(name);
        }

        @SuppressWarnings("unchecked")
        private List<Object> interpretedJavaListItems() {
            Object items = fields.get("__items");
            if (items instanceof List<?> list) {
                return (List<Object>) list;
            }
            Object source = fields.get("__source");
            if (source instanceof List<?> list) {
                return (List<Object>) list;
            }
            if (source instanceof InterpretedInstance interpretedInstance) {
                return interpretedInstance.interpretedJavaListItems();
            }
            return null;
        }

        private Object getPrototypeChainValue(String name) {
            for (Object prototype : prototypeChain) {
                if (prototype instanceof Map<?, ?> rawPrototype) {
                    Map<String, Object> map = castMap(rawPrototype);
                    if (map.containsKey(name)) {
                        return map.get(name);
                    }
                }
            }
            return null;
        }

        private boolean hasPrototypeChainProperty(String name) {
            for (Object prototype : prototypeChain) {
                if (prototype instanceof Map<?, ?> rawPrototype
                        && castMap(rawPrototype).containsKey(name)) {
                    return true;
                }
            }
            return false;
        }

        private Object bindPrototypeValue(Object value) {
            if (value instanceof InterpretedFunction prototypeFunction) {
                return prototypeFunction.bindThis(this);
            }
            if (isFunctionDefinition(value)) {
                return new InterpretedFunction(castMap((Map<?, ?>) value)).bindThis(this);
            }
            return value;
        }

        private boolean hasCallableMember(String name) {
            Object method = getMethod(name);
            if (method != null) {
                return true;
            }
            Object value = get(name);
            return isRuntimeCallableValue(value);
        }

        private Object getOwnField(String name) {
            if (fields.containsKey(name)) {
                return fields.get(name);
            }
            String javaFieldName = javaFieldAliasName(name);
            if (fields.containsKey(javaFieldName)) {
                return fields.get(javaFieldName);
            }
            return null;
        }

        private String javaFieldAliasName(String name) {
            return "__qin_field_" + name;
        }

        private Set<String> methodNames() {
            LinkedHashSet<String> names = new LinkedHashSet<>(methods.keySet());
            names.addAll(superMethods.keySet());
            return names;
        }

        private Object getMethod(String name) {
            InterpretedFunction method = methods.get(name);
            if (method != null) {
                return method.bindThis(this);
            }
            InterpretedFunction superMethod = superMethods.get(name);
            if (superMethod != null) {
                return superMethod.bindThis(this);
            }
            Object prototypeValue = prototypeProperties.get(name);
            if (prototypeValue instanceof InterpretedFunction prototypeFunction) {
                return prototypeFunction.bindThis(this);
            }
            if (isFunctionDefinition(prototypeValue)) {
                return new InterpretedFunction(castMap((Map<?, ?>) prototypeValue)).bindThis(this);
            }
            Object prototypeChainValue = getPrototypeChainValue(name);
            Object boundPrototypeChainValue = bindPrototypeValue(prototypeChainValue);
            if (boundPrototypeChainValue instanceof InterpretedFunction interpretedFunction) {
                return interpretedFunction;
            }
            return null;
        }

        private Set<String> fieldNames() {
            return fields.keySet();
        }

        private Map<String, Object> fieldSnapshot() {
            return new LinkedHashMap<>(fields);
        }

        private Map<String, Object> ownEnumerableProperties() {
            return new LinkedHashMap<>(fields);
        }

        private boolean hasPrototypeObject(Object prototype) {
            if (prototype == null) {
                return false;
            }
            for (Object candidate : prototypeChain) {
                if (candidate == prototype) {
                    return true;
                }
            }
            return false;
        }

        private Set<String> ownEnumerablePropertyNames() {
            return new LinkedHashSet<>(fields.keySet());
        }

        private Set<String> accessorNames() {
            LinkedHashSet<String> names = new LinkedHashSet<>(accessors.keySet());
            names.addAll(superAccessors.keySet());
            return names;
        }

        private void setConstructorFunction(Object constructorFunction) {
            this.constructorFunction = constructorFunction;
        }
    }

    private static final class AccessorProperty {
        private InterpretedFunction getter;
        private InterpretedFunction setter;
    }

    private record ArrayLiteralSegment(boolean spread, Object value) {
    }

    private record ReturnSignal(Object value) {
    }

    private record BreakSignal() {
    }

    private record ContinueSignal() {
    }

    private static final class ExportSlot {
        private Object value;
    }

    private static final class ThrownValue extends RuntimeException {
        private final Object value;

        private ThrownValue(Object value) {
            super(summarizeRuntimeValue(value), null, false, false);
            this.value = value;
        }

        private Object value() {
            return value;
        }
    }

    private static final class InterpretedFunction implements QinCallable, QinRuntimeObject {
        private static final String LOCAL_BINDINGS_KEY = "__qin_runtime_local_bindings__";
        private static final String PARENT_CLOSURE_KEY = "__qin_runtime_parent_closure__";
        private static final int MAX_LEXICAL_ENV_CHAIN_DEPTH =
                Integer.getInteger("qin.runtime.maxLexicalEnvChainDepth", 4096);
        private final Map<String, Object> definition;
        private Map<String, Object> ast;
        private final Map<String, Object> closure;
        private final Object thisValue;
        private final Map<String, Object> ownProperties;
        private boolean initialized;
        private boolean initializing;
        private boolean prototypeMembersInstalled;
        private boolean prototypeMembersInstalling;
        private InterpretedFunction cachedSuperClassFunction;
        private boolean superClassFunctionResolved;
        private Map<String, InterpretedFunction> cachedInstanceMethods;
        private Map<String, InterpretedFunction> cachedInheritedInstanceMethods;
        private Map<String, AccessorProperty> cachedInstanceAccessors;
        private Map<String, AccessorProperty> cachedInheritedInstanceAccessors;
        private List<String> cachedHoistedVarBindings;

        private InterpretedFunction(Map<String, Object> definition) {
            this(definition, new LinkedHashMap<>());
        }

        private InterpretedFunction(Map<String, Object> definition, Map<String, Object> ownProperties) {
            this.definition = definition;
            if (definition.get("ast") instanceof Map<?, ?>) {
                this.ast = resolveFunctionAst(definition);
            }
            Object rawClosure = definition.get("closure");
            this.closure = rawClosure instanceof Map<?, ?> closureMap
                    ? castMap(closureMap)
                    : new LinkedHashMap<>();
            this.thisValue = definition.getOrDefault("thisValue", GLOBAL_OBJECT);
            this.ownProperties = ownProperties;
        }

        private Map<String, Object> ast() {
            if (ast == null) {
                ast = resolveFunctionAst(definition);
            }
            return ast;
        }

        private void ensureInitialized() {
            if (initialized || initializing) {
                return;
            }
            initializing = true;
            try {
                ast();
            bindSelfName();
            installClassStaticMembers();
                initialized = true;
            } finally {
                initializing = false;
            }
        }

        private void ensureClassPrototypeMembersInstalled() {
            ensureInitialized();
            if (prototypeMembersInstalled || prototypeMembersInstalling) {
                return;
            }
            prototypeMembersInstalling = true;
            try {
                String type = String.valueOf(ast().get("type"));
                if ("ClassDeclaration".equals(type) || "ClassExpression".equals(type)) {
                    installClassPrototypeMembers();
                }
                prototypeMembersInstalled = true;
            } finally {
                prototypeMembersInstalling = false;
            }
        }

        private Map<String, Object> resolveFunctionAst(Map<String, Object> definition) {
            Object ast = definition.get("ast");
            if (ast instanceof Map<?, ?> rawAst) {
                return castMap(rawAst);
            }
            Object astRef = definition.get("astRef");
            if (astRef != null) {
                return QinFunctionModelRegistry.resolve(astRef);
            }
            throw new IllegalArgumentException("Function definition is missing ast/astRef");
        }

        private void bindSelfName() {
            String type = String.valueOf(ast().get("type"));
            if ("FunctionExpression".equals(type) || "ClassExpression".equals(type)) {
                return;
            }
            Object idNode = ast().get("id");
            if (!(idNode instanceof Map<?, ?> rawId)) {
                return;
            }
            String name = extractPropertyName(rawId);
            if (name != null && !name.isBlank() && !"null".equals(name)) {
                closure.put(name, this);
            }
        }

        @Override
        public Object call(Object... args) {
            ensureInitialized();
            enterInterpretedCall(functionDebugName());
            Map<String, Object> env = new LinkedHashMap<>();
            try {
                installLocalBindings(env);
                installClosureBindings(env);
                bindParameters(env, args);
                env.put("this", thisValue);
                pushInterpretedThis(thisValue);
                bindExpressionSelfName(env);
                Object result = evalFunctionBody(ast(), env);
                return result instanceof ReturnSignal signal ? signal.value() : result;
            } finally {
                popInterpretedThis();
                exitInterpretedCall();
            }
        }

        private Object callConstructor(Object... args) {
            ensureInitialized();
            enterInterpretedCall(functionDebugName() + ".constructor");
            Map<String, Object> env = new LinkedHashMap<>();
            try {
                installLocalBindings(env);
                installClosureBindings(env);
                bindParameters(env, args);
                env.put("this", thisValue);
                bindConstructorParameterProperties(args);
                pushInterpretedThis(thisValue);
                bindExpressionSelfName(env);
                return evalFunctionBody(ast(), env);
            } finally {
                popInterpretedThis();
                exitInterpretedCall();
            }
        }

        private static void pushInterpretedThis(Object value) {
            INTERPRETED_THIS_STACK.get().add(value);
        }

        private static void popInterpretedThis() {
            List<Object> stack = INTERPRETED_THIS_STACK.get();
            if (!stack.isEmpty()) {
                stack.remove(stack.size() - 1);
            }
        }

        private static void enterInterpretedCall(String label) {
            List<String> stack = INTERPRETED_CALL_STACK.get();
            stack.add(label == null || label.isBlank() ? "<anonymous>" : label);
            long callLimit = interpretedCallCountLimit;
            if (callLimit > 0L) {
                long callCount = INTERPRETED_CALL_COUNT.get() + 1L;
                INTERPRETED_CALL_COUNT.set(callCount);
                if (callCount > callLimit) {
                    int from = Math.max(0, stack.size() - 48);
                    throw new IllegalStateException(
                            "Interpreted JS call count exceeded "
                                    + callLimit
                                    + "; count="
                                    + callCount
                                    + "; recentCalls="
                                    + stack.subList(from, stack.size()));
                }
            }
            if (stack.size() > MAX_INTERPRETED_CALL_DEPTH) {
                int from = Math.max(0, stack.size() - 48);
                throw new IllegalStateException(
                        "Interpreted JS call depth exceeded "
                                + MAX_INTERPRETED_CALL_DEPTH
                                + "; recentCalls=" + stack.subList(from, stack.size()));
            }
        }

        private static void exitInterpretedCall() {
            List<String> stack = INTERPRETED_CALL_STACK.get();
            if (!stack.isEmpty()) {
                stack.remove(stack.size() - 1);
            }
            if (stack.isEmpty()) {
                if (interpretedCallCountLimit <= 0L) {
                    INTERPRETED_CALL_COUNT.remove();
                }
            }
        }

        @Override
        public Object get(Object property) {
            ensureInitialized();
            String name = propertyKey(property);
            if (ownProperties.containsKey(name)) {
                Object value = ownProperties.get(name);
                if (value instanceof AccessorProperty accessor && accessor.getter != null) {
                    return accessor.getter.bindThis(this).call();
                }
                return value;
            }
            if ("name".equals(name)) {
                Object explicitName = definition.get("functionName");
                if (explicitName != null) {
                    return explicitName;
                }
                return isClassFunction() ? classDebugName() : functionDebugName();
            }
            if ("length".equals(name)) {
                return (double) asList(ast.get("params")).size();
            }
            if ("bind".equals(name)) {
                return new NativeFunction(functionDebugName() + ".bind", args -> {
                    Object boundThis = args.length == 0 ? GLOBAL_OBJECT : args[0];
                    Object[] boundArgs = args.length <= 1 ? new Object[0] : Arrays.copyOfRange(args, 1, args.length);
                    InterpretedFunction rebound = bindThis(boundThis);
                    return new NativeFunction(functionDebugName() + ".bound", callArgs -> {
                        Object[] combined = Arrays.copyOf(boundArgs, boundArgs.length + callArgs.length);
                        System.arraycopy(callArgs, 0, combined, boundArgs.length, callArgs.length);
                        return rebound.call(combined);
                    });
                });
            }
            if ("call".equals(name)) {
                return new NativeFunction(functionDebugName() + ".call", args -> {
                    Object boundThis = args.length == 0 ? GLOBAL_OBJECT : args[0];
                    Object[] callArgs = args.length <= 1 ? new Object[0] : Arrays.copyOfRange(args, 1, args.length);
                    return bindThis(boundThis).call(callArgs);
                });
            }
            if ("apply".equals(name)) {
                return new NativeFunction(functionDebugName() + ".apply", args -> {
                    Object boundThis = args.length == 0 ? GLOBAL_OBJECT : args[0];
                    Object arrayLikeArgs = args.length <= 1 ? List.of() : args[1];
                    List<Object> applyArgs = new ArrayList<>();
                    addSpreadValues(applyArgs, arrayLikeArgs);
                    return bindThis(boundThis).call(applyArgs.toArray());
                });
            }
            if ("prototype".equals(name) && isPrototypeBearingFunction()) {
                Map<String, Object> prototype = prototypeObject();
                if (isClassFunction()) {
                    ensureClassPrototypeMembersInstalled();
                }
                return prototype;
            }
            return null;
        }

        @Override
        public Object set(Object property, Object value) {
            ensureInitialized();
            String name = propertyKey(property);
            Object existing = ownProperties.get(name);
            if (existing instanceof AccessorProperty accessor && accessor.setter != null) {
                accessor.setter.bindThis(this).call(value);
                return value;
            }
            ownProperties.put(name, value);
            return value;
        }

        @Override
        public boolean has(Object property) {
            ensureInitialized();
            String name = propertyKey(property);
            return ownProperties.containsKey(name)
                    || "name".equals(name)
                    || "length".equals(name)
                    || ("prototype".equals(name) && isPrototypeBearingFunction());
        }

        private InterpretedFunction bindThis(Object value) {
            ensureInitialized();
            if (Boolean.TRUE.equals(definition.get("__qin_arrow_lexical_this"))
                    && !Boolean.TRUE.equals(definition.get("__qin_class_member_function"))) {
                return this;
            }
            LinkedHashMap<String, Object> rebound = new LinkedHashMap<>(definition);
            rebound.put("thisValue", value);
            if (ast != null) {
                rebound.put("ast", ast);
            }
            return new InterpretedFunction(rebound, ownProperties);
        }

        private InterpretedFunction asClassMemberFunction() {
            ensureInitialized();
            if (Boolean.TRUE.equals(definition.get("__qin_class_member_function"))) {
                return this;
            }
            LinkedHashMap<String, Object> marked = new LinkedHashMap<>(definition);
            marked.put("__qin_class_member_function", true);
            if (ast != null) {
                marked.put("ast", ast);
            }
            return new InterpretedFunction(marked, ownProperties);
        }

        private InterpretedFunction asClassMemberFunctionWithOwnerFrom(InterpretedFunction ownerSource) {
            InterpretedFunction classMemberFunction = asClassMemberFunction();
            if (ownerSource == null) {
                return classMemberFunction;
            }
            LinkedHashMap<String, Object> marked = new LinkedHashMap<>(classMemberFunction.definition);
            copyIfMissing(marked, ownerSource.definition, "ownerSuperClass");
            copyIfMissing(marked, ownerSource.definition, "ownerSuperClassFunction");
            copyIfMissing(marked, ownerSource.definition, "ownerSuperClassValue");
            if (classMemberFunction.ast != null) {
                marked.put("ast", classMemberFunction.ast);
            }
            return new InterpretedFunction(marked, classMemberFunction.ownProperties);
        }

        private void copyIfMissing(Map<String, Object> target, Map<String, Object> source, String key) {
            if (!target.containsKey(key) && source.containsKey(key)) {
                target.put(key, source.get(key));
            }
        }

        private Object recoverClassMemberFunctionalReceiver() {
            if (!Boolean.TRUE.equals(definition.get("__qin_class_member_function"))) {
                return null;
            }
            if (unwrapExportSlotValue(thisValue) instanceof InterpretedInstance) {
                return null;
            }
            List<Object> thisStack = INTERPRETED_THIS_STACK.get();
            for (int i = thisStack.size() - 1; i >= 0; i--) {
                Object candidate = unwrapExportSlotValue(thisStack.get(i));
                if (candidate instanceof InterpretedInstance) {
                    return candidate;
                }
            }
            return null;
        }

        private boolean isPrototypeBearingFunction() {
            Object type = ast().get("type");
            return !"ArrowFunctionExpression".equals(type) && !"MethodDefinition".equals(type);
        }

        private boolean isClassFunction() {
            Object type = ast().get("type");
            return "ClassDeclaration".equals(type) || "ClassExpression".equals(type);
        }

        private boolean isStaticClassMember(Map<String, Object> member) {
            return Boolean.TRUE.equals(member.get("static")) || Boolean.TRUE.equals(member.get("isStatic"));
        }

        private void installClassStaticMembers() {
            String type = String.valueOf(ast().get("type"));
            if (!"ClassDeclaration".equals(type) && !"ClassExpression".equals(type)) {
                return;
            }
            Map<String, Object> body = castMap(asMap(ast().get("body")));
            for (Object memberNode : asList(body.get("body"))) {
                Map<String, Object> member = castMap(asMap(memberNode));
                if (!isStaticClassMember(member)) {
                    continue;
                }
                String name = classMemberKey(member);
                if (name == null) {
                    continue;
                }
                if (ownProperties.containsKey(propertyKey(name))) {
                    Object existing = ownProperties.get(propertyKey(name));
                    if (!(existing instanceof AccessorProperty)) {
                        continue;
                    }
                }
                if ("MethodDefinition".equals(member.get("type"))) {
                    Object valueNode = member.get("value");
                    if (!(valueNode instanceof Map<?, ?> rawValue)) {
                        continue;
                    }
                    InterpretedFunction memberFunction = createMemberFunction(name, castMap(rawValue));
                    Object decoratedFunction = applyLegacyMethodDecorators(member, name, memberFunction, this);
                    InterpretedFunction loweredFunction =
                            toInterpretedFunction(decoratedFunction, memberFunction).asClassMemberFunction();
                    String kind = String.valueOf(member.get("kind"));
                    if ("get".equals(kind) || "set".equals(kind)) {
                        installAccessor(ownProperties, name, loweredFunction, kind);
                    } else {
                        set(name, loweredFunction);
                    }
                    continue;
                }
                if (isClassField(member)) {
                    set(name, evalClassFieldInitializer(member, this));
                }
            }
        }

        private void installClassPrototypeMembers() {
            String type = String.valueOf(ast().get("type"));
            if (!"ClassDeclaration".equals(type) && !"ClassExpression".equals(type)) {
                return;
            }
            Map<String, Object> prototype = prototypeObject();
            InterpretedFunction parent = resolveSuperClassFunction();
            if (parent != null) {
                Object parentPrototype = parent.get("prototype");
                if (parentPrototype instanceof Map<?, ?> rawParentPrototype) {
                    prototype.put(PROTOTYPE_PARENT_KEY, castMap(rawParentPrototype));
                }
            }
            Map<String, Object> body = castMap(asMap(ast().get("body")));
            for (Object memberNode : asList(body.get("body"))) {
                Map<String, Object> member = castMap(asMap(memberNode));
                if (!"MethodDefinition".equals(member.get("type"))
                        || isStaticClassMember(member)
                        || isAccessorMember(member)) {
                    continue;
                }
                String name = classMemberKey(member);
                Object valueNode = member.get("value");
                if (name == null || !(valueNode instanceof Map<?, ?> rawValue)) {
                    continue;
                }
                prototype.put(propertyKey(name), createMemberFunction(name, castMap(rawValue)));
            }
        }

        private Object construct(Object... args) {
            ensureInitialized();
            String type = String.valueOf(ast().get("type"));
            if (!"ClassDeclaration".equals(type) && !"ClassExpression".equals(type)) {
                return constructFunction(args);
            }
            List<String> stack = CONSTRUCT_STACK.get();
            String label = classDebugName();
            stack.add(label);
            try {
                if (stack.size() > 64) {
                    throw new IllegalStateException("Recursive interpreted class construction: " + stack);
                }
                InterpretedFunction parent = resolveSuperClassFunction();
                List<Object> prototypeChain = prototypeChain();
                InterpretedInstance instance = new InterpretedInstance(
                        collectInheritedInstanceMethods(),
                        collectInheritedInstanceAccessors(),
                        parent == null ? Map.of() : parent.collectInheritedInstanceMethods(),
                        parent == null ? Map.of() : parent.collectInheritedInstanceAccessors(),
                        functionPrototypeProperties(),
                        prototypeChain);
                instance.setConstructorFunction(this);
                if (parent != null) {
                    parent.installInheritedInstanceFields(instance);
                }
                installInstanceFields(instance);
                InterpretedFunction constructor = instance.methods.get("constructor");
                if (constructor != null) {
                    Object constructed = constructor.bindThis(instance).callConstructor(args);
                    if (constructed instanceof ReturnSignal signal) {
                        Object returned = signal.value();
                        if (returned instanceof Map<?, ?> || returned instanceof QinRuntimeObject) {
                            return returned;
                        }
                    }
                }
                return instance;
            } finally {
                stack.remove(stack.size() - 1);
                if (stack.isEmpty()) {
                    CONSTRUCT_STACK.remove();
                }
            }
        }

        private Object constructFunction(Object... args) {
            ensureInitialized();
            if ("ArrowFunctionExpression".equals(ast().get("type"))) {
                throw new IllegalArgumentException("Arrow function is not constructible");
            }
            List<String> stack = CONSTRUCT_STACK.get();
            String label = functionDebugName();
            stack.add(label);
            try {
                if (stack.size() > 64) {
                    throw new IllegalStateException("Recursive interpreted function construction: " + stack);
                }
                InterpretedInstance instance = new InterpretedInstance(
                        Map.of(),
                        Map.of(),
                        Map.of(),
                        Map.of(),
                        functionPrototypeProperties(),
                        prototypeChain());
                instance.setConstructorFunction(this);
                Object constructed = bindThis(instance).callConstructor(args);
                if (constructed instanceof ReturnSignal signal) {
                    Object returned = signal.value();
                    if (returned instanceof Map<?, ?> || returned instanceof QinRuntimeObject) {
                        return returned;
                    }
                }
                return instance;
            } finally {
                stack.remove(stack.size() - 1);
                if (stack.isEmpty()) {
                    CONSTRUCT_STACK.remove();
                }
            }
        }

        private Map<String, Object> functionPrototypeProperties() {
            Object prototype = get("prototype");
            if (prototype instanceof Map<?, ?> rawPrototype) {
                return castMap(rawPrototype);
            }
            return Map.of();
        }

        private List<Object> prototypeChain() {
            List<Object> chain = new ArrayList<>();
            Object prototype = get("prototype");
            if (prototype != null) {
                chain.add(prototype);
            }
            InterpretedFunction parent = resolveSuperClassFunction();
            if (parent != null) {
                chain.addAll(parent.prototypeChain());
            }
            return chain;
        }

        private void installInheritedInstanceFields(InterpretedInstance instance) {
            InterpretedFunction parent = resolveSuperClassFunction();
            if (parent != null) {
                parent.installInheritedInstanceFields(instance);
            }
            installInstanceFields(instance);
        }

        private String functionDebugName() {
            Object explicitName = definition.get("functionName");
            if (explicitName != null) {
                Object ownerClassName = definition.get("ownerClassName");
                if (ownerClassName != null) {
                    return ownerClassName + "." + explicitName;
                }
                return String.valueOf(explicitName);
            }
            Object idNode = ast().get("id");
            String name = idNode instanceof Map<?, ?> rawId
                    ? extractPropertyName(rawId)
                    : null;
            if (name == null || name.isBlank() || "null".equals(name)) {
                Object debug = definition.get("debugNode");
                return debug == null ? String.valueOf(ast().get("type")) : String.valueOf(debug);
            }
            return name;
        }

        private String classDebugName() {
            Object idNode = ast().get("id");
            String name = idNode instanceof Map<?, ?> rawId
                    ? extractPropertyName(rawId)
                    : null;
            if (name == null || name.isBlank() || "null".equals(name)) {
                Object debug = definition.get("debugNode");
                return debug == null ? String.valueOf(ast().get("type")) : String.valueOf(debug);
            }
            return name;
        }

        private JavaRuntimeClass javaRuntimeClass() {
            InterpretedFunction parent = resolveSuperClassFunction();
            return new JavaRuntimeClass(this, parent == null ? null : parent.javaRuntimeClass());
        }

        private InterpretedFunction findInstanceMethod(String name) {
            return reflectedInstanceMethod(collectInheritedInstanceMethods(), name);
        }

        private InterpretedFunction findDeclaredInstanceMethod(String name) {
            return reflectedInstanceMethod(collectInstanceMethods(), name);
        }

        private InterpretedFunction reflectedInstanceMethod(Map<String, InterpretedFunction> methods, String name) {
            String key = propertyKey(name);
            InterpretedFunction method = methods.get(key);
            if (method != null) {
                return method;
            }
            if ("_markParseFail".equals(key)) {
                return methods.get("setParseFail");
            }
            return null;
        }

        private Map<String, InterpretedFunction> collectInstanceMethods() {
            if (cachedInstanceMethods != null) {
                return cachedInstanceMethods;
            }
            LinkedHashMap<String, InterpretedFunction> methods = new LinkedHashMap<>();
            Map<String, Object> body = castMap(asMap(ast.get("body")));
            for (Object memberNode : asList(body.get("body"))) {
                Map<String, Object> member = castMap(asMap(memberNode));
                if (!"MethodDefinition".equals(member.get("type"))
                        || isStaticClassMember(member)
                        || isAccessorMember(member)) {
                    continue;
                }
                String name = methodDefinitionName(member);
                Object valueNode = member.get("value");
                if (!(valueNode instanceof Map<?, ?> rawValue)) {
                    continue;
                }
                InterpretedFunction memberFunction = createMemberFunction(name, castMap(rawValue));
                Object decoratedFunction = applyLegacyMethodDecorators(member, name, memberFunction, null);
                InterpretedFunction loweredFunction =
                        toInterpretedFunction(decoratedFunction, memberFunction).asClassMemberFunction();
                if (!"constructor".equals(name) && asList(member.get("decorators")).isEmpty()) {
                    InterpretedFunction prototypeFunction = toInterpretedFunction(
                            prototypeObjectForDecorator().get(propertyKey(name)),
                            null);
                    if (prototypeFunction != null) {
                        loweredFunction = prototypeFunction.asClassMemberFunctionWithOwnerFrom(memberFunction);
                    }
                }
                methods.put(name, loweredFunction);
            }
            cachedInstanceMethods = Map.copyOf(methods);
            return cachedInstanceMethods;
        }

        private Map<String, InterpretedFunction> collectInheritedInstanceMethods() {
            if (cachedInheritedInstanceMethods != null) {
                return cachedInheritedInstanceMethods;
            }
            LinkedHashMap<String, InterpretedFunction> methods = new LinkedHashMap<>();
            InterpretedFunction parent = resolveSuperClassFunction();
            if (parent != null) {
                methods.putAll(parent.collectInheritedInstanceMethods());
            }
            methods.putAll(collectInstanceMethods());
            cachedInheritedInstanceMethods = Map.copyOf(methods);
            return cachedInheritedInstanceMethods;
        }

        private Map<String, AccessorProperty> collectInstanceAccessors() {
            if (cachedInstanceAccessors != null) {
                return cachedInstanceAccessors;
            }
            LinkedHashMap<String, AccessorProperty> accessors = new LinkedHashMap<>();
            Map<String, Object> body = castMap(asMap(ast.get("body")));
            for (Object memberNode : asList(body.get("body"))) {
                Map<String, Object> member = castMap(asMap(memberNode));
                if (!"MethodDefinition".equals(member.get("type"))
                        || isStaticClassMember(member)
                        || !isAccessorMember(member)) {
                    continue;
                }
                String name = methodDefinitionName(member);
                Object valueNode = member.get("value");
                if (name == null || !(valueNode instanceof Map<?, ?> rawValue)) {
                    continue;
                }
                InterpretedFunction memberFunction = createMemberFunction(name, castMap(rawValue));
                Object decoratedFunction = applyLegacyMethodDecorators(member, name, memberFunction, null);
                installAccessor(
                        accessors,
                        name,
                        toInterpretedFunction(decoratedFunction, memberFunction).asClassMemberFunction(),
                        String.valueOf(member.get("kind")));
            }
            cachedInstanceAccessors = Map.copyOf(accessors);
            return cachedInstanceAccessors;
        }

        private Map<String, AccessorProperty> collectInheritedInstanceAccessors() {
            if (cachedInheritedInstanceAccessors != null) {
                return cachedInheritedInstanceAccessors;
            }
            LinkedHashMap<String, AccessorProperty> accessors = new LinkedHashMap<>();
            InterpretedFunction parent = resolveSuperClassFunction();
            if (parent != null) {
                accessors.putAll(parent.collectInheritedInstanceAccessors());
            }
            accessors.putAll(collectInstanceAccessors());
            cachedInheritedInstanceAccessors = Map.copyOf(accessors);
            return cachedInheritedInstanceAccessors;
        }

        private InterpretedFunction resolveSuperClassFunction() {
            if (superClassFunctionResolved) {
                return cachedSuperClassFunction;
            }
            Object capturedSuperClassValue = definition.get("ownerSuperClassFunction");
            if (capturedSuperClassValue == null) {
                capturedSuperClassValue = definition.get("ownerSuperClassValue");
            }
            Object superClassNode = ast.get("superClass");
            if (!(superClassNode instanceof Map<?, ?>)) {
                superClassNode = definition.get("ownerSuperClass");
            }
            if (!(superClassNode instanceof Map<?, ?> rawSuperClass)) {
                capturedSuperClassValue = unwrapExportSlotValue(capturedSuperClassValue);
                if (isFunctionDefinition(capturedSuperClassValue)) {
                    cachedSuperClassFunction = new InterpretedFunction(castMap((Map<?, ?>) capturedSuperClassValue));
                    superClassFunctionResolved = true;
                    return cachedSuperClassFunction;
                }
                if (capturedSuperClassValue instanceof InterpretedFunction interpretedFunction) {
                    cachedSuperClassFunction = interpretedFunction;
                    superClassFunctionResolved = true;
                    return cachedSuperClassFunction;
                }
                superClassFunctionResolved = true;
                return null;
            }
            String superName = extractPropertyName(rawSuperClass);
            if (superName == null || superName.isBlank() || "null".equals(superName)) {
                superClassFunctionResolved = true;
                return null;
            }
            Object value = capturedSuperClassValue;
            if (value == null) {
                value = resolveIdentifier(superName, resolveClosure());
            }
            if (value == null) {
                value = __qin_global__(superName);
            }
            value = unwrapExportSlotValue(value);
            if (isFunctionDefinition(value)) {
                cachedSuperClassFunction = new InterpretedFunction(castMap((Map<?, ?>) value));
                superClassFunctionResolved = true;
                return cachedSuperClassFunction;
            }
            cachedSuperClassFunction = value instanceof InterpretedFunction interpretedFunction ? interpretedFunction : null;
            Object ownerClassName = definition.get("ownerClassName");
            if (cachedSuperClassFunction != null
                    && ownerClassName != null
                    && String.valueOf(ownerClassName).equals(cachedSuperClassFunction.classDebugName())) {
                cachedSuperClassFunction = cachedSuperClassFunction.resolveSuperClassFunction();
            }
            superClassFunctionResolved = true;
            return cachedSuperClassFunction;
        }

        private String classMemberKey(Map<String, Object> member) {
            if (Boolean.TRUE.equals(member.get("computed"))) {
                return propertyKey(evalNode(member.get("key"), closure));
            }
            return extractPropertyName(member.get("key"));
        }

        private String methodDefinitionName(Map<String, Object> member) {
            if ("constructor".equals(String.valueOf(member.get("kind")))) {
                return "constructor";
            }
            return classMemberKey(member);
        }

        private InterpretedFunction createMemberFunction(Map<String, Object> valueAst) {
            return createMemberFunction(null, valueAst);
        }

        private InterpretedFunction createMemberFunction(String name, Map<String, Object> valueAst) {
            LinkedHashMap<String, Object> methodDefinition = new LinkedHashMap<>();
            methodDefinition.put("__qin_function_model", definition.get("__qin_function_model"));
            methodDefinition.put("ast", valueAst);
            methodDefinition.put("__qin_class_member_function", true);
            if (name != null && !name.isBlank()) {
                methodDefinition.put("functionName", name);
            }
            String ownerClassName = classDebugName();
            if (ownerClassName != null && !ownerClassName.isBlank() && !"null".equals(ownerClassName)) {
                methodDefinition.put("ownerClassName", ownerClassName);
            }
            Map<String, Object> resolvedClosure = new LinkedHashMap<>();
            Map<String, Object> parentClosure = resolveClosure();
            if (parentClosure != null && !parentClosure.isEmpty()) {
                resolvedClosure.put(PARENT_CLOSURE_KEY, parentClosure);
            }
            bindClassSelfName(resolvedClosure);
            methodDefinition.put("closure", resolvedClosure);
            methodDefinition.put("ownerSuperClass", ast.get("superClass"));
            methodDefinition.put("ownerSuperClassFunction", resolveSuperClassFunction());
            Object rawSuperName = extractPropertyName(ast.get("superClass"));
            if (rawSuperName != null) {
                methodDefinition.put("ownerSuperClassValue", resolveIdentifier(String.valueOf(rawSuperName), resolvedClosure));
            }
            return new InterpretedFunction(methodDefinition);
        }

        private Object applyLegacyMethodDecorators(
                Map<String, Object> member,
                String name,
                InterpretedFunction memberFunction,
                Object target) {
            List<?> decorators = asList(member.get("decorators"));
            if (decorators.isEmpty()) {
                return memberFunction;
            }
            LinkedHashMap<String, Object> descriptor = new LinkedHashMap<>();
            descriptor.put("value", memberFunction);
            descriptor.put("writable", true);
            descriptor.put("enumerable", false);
            descriptor.put("configurable", true);
            Object decoratorTarget = target == null ? prototypeObjectForDecorator() : target;
            List<?> reversed = new ArrayList<>(decorators);
            Collections.reverse(reversed);
            for (Object decoratorNode : reversed) {
                Object decorator = evalDecoratorExpression(decoratorNode);
                Object result = callAny(decorator, decoratorTarget, name, descriptor);
                if (result instanceof Map<?, ?> rawDescriptor) {
                    descriptor = new LinkedHashMap<>(castMap(rawDescriptor));
                }
            }
            Object decoratedValue = descriptor.get("value");
            return decoratedValue == null ? memberFunction : decoratedValue;
        }

        private Object evalDecoratorExpression(Object decoratorNode) {
            Object expressionNode = decoratorNode;
            if (decoratorNode instanceof Map<?, ?> rawDecorator) {
                Map<String, Object> decorator = castMap(rawDecorator);
                if ("Decorator".equals(String.valueOf(decorator.get("type"))) && decorator.containsKey("expression")) {
                    expressionNode = decorator.get("expression");
                }
            }
            return evalNode(expressionNode, resolveClosure());
        }

        private Map<String, Object> prototypeObjectForDecorator() {
            return prototypeObject();
        }

        private Map<String, Object> prototypeObject() {
            Object prototype = ownProperties.get("prototype");
            if (prototype instanceof Map<?, ?> rawPrototype) {
                return castMap(rawPrototype);
            }
            LinkedHashMap<String, Object> created = new LinkedHashMap<>();
            ownProperties.put("prototype", created);
            return created;
        }

        private InterpretedFunction toInterpretedFunction(Object value, InterpretedFunction fallback) {
            if (value instanceof InterpretedFunction interpretedFunction) {
                return interpretedFunction;
            }
            if (isFunctionDefinition(value)) {
                return new InterpretedFunction(castMap((Map<?, ?>) value));
            }
            return fallback;
        }

        private void installAccessor(Map<String, ?> rawTarget, String name, InterpretedFunction function, String kind) {
            @SuppressWarnings("unchecked")
            Map<String, Object> target = (Map<String, Object>) rawTarget;
            AccessorProperty accessor = target.get(propertyKey(name)) instanceof AccessorProperty existing
                    ? existing
                    : new AccessorProperty();
            if ("get".equals(kind)) {
                accessor.getter = function;
            } else if ("set".equals(kind)) {
                accessor.setter = function;
            }
            target.put(propertyKey(name), accessor);
        }

        private void installInstanceFields(InterpretedInstance instance) {
            Map<String, Object> body = castMap(asMap(ast.get("body")));
            for (Object memberNode : asList(body.get("body"))) {
                Map<String, Object> member = castMap(asMap(memberNode));
                if (!isClassField(member) || isStaticClassMember(member)) {
                    continue;
                }
                String name = extractPropertyName(member.get("key"));
                if (name != null) {
                    instance.set(name, evalClassFieldInitializer(member, instance));
                }
            }
        }

        private boolean isClassField(Map<String, Object> member) {
            String type = String.valueOf(member.get("type"));
            return "PropertyDefinition".equals(type) || "FieldDefinition".equals(type);
        }

        private boolean isAccessorMember(Map<String, Object> member) {
            Object kind = member.get("kind");
            return "get".equals(kind) || "set".equals(kind);
        }

        private Object evalClassFieldInitializer(Map<String, Object> member, Object receiver) {
            Object valueNode = member.get("value");
            if (!(valueNode instanceof Map<?, ?>)) {
                return null;
            }
            Map<String, Object> env = new LinkedHashMap<>();
            installLocalBindings(env);
            installClosureBindings(env);
            env.put("this", receiver);
            bindClassSelfName(env);
            return evalNode(valueNode, env);
        }

        private void bindClassSelfName(Map<String, Object> env) {
            String className = classDebugName();
            if (className != null && !className.isBlank() && !"null".equals(className)) {
                env.put(className, this);
            }
        }

        private void bindParameters(Map<String, Object> env, Object[] args) {
            env.put("arguments", argumentsObject(args));
            List<?> params = asList(ast.get("params"));
            for (int i = 0; i < params.size(); i++) {
                Object paramNode = unwrapFunctionParam(params.get(i));
                if (!(paramNode instanceof Map<?, ?> map)) {
                    continue;
                }
                if ("RestElement".equals(String.valueOf(map.get("type")))) {
                    List<Object> rest = new ArrayList<>(Arrays.asList(args).subList(Math.min(i, args.length), args.length));
                    bindPattern(paramNode, rest, env);
                    continue;
                }
                bindPattern(paramNode, i < args.length ? args[i] : null, env);
            }
        }

        private Object unwrapFunctionParam(Object paramNode) {
            if (paramNode instanceof Map<?, ?> map && map.containsKey("param")) {
                return map.get("param");
            }
            if (paramNode instanceof Map<?, ?> map && "TSParameterProperty".equals(String.valueOf(map.get("type")))) {
                return map.get("parameter");
            }
            return paramNode;
        }

        private void bindConstructorParameterProperties(Object[] args) {
            if (!(unwrapExportSlotValue(thisValue) instanceof InterpretedInstance instance)) {
                return;
            }
            List<?> params = asList(ast.get("params"));
            for (int i = 0; i < params.size(); i++) {
                Object paramNode = params.get(i);
                if (!(paramNode instanceof Map<?, ?> map)
                        || !"TSParameterProperty".equals(String.valueOf(map.get("type")))) {
                    continue;
                }
                String fieldName = constructorParameterPropertyName(map.get("parameter"));
                if (fieldName != null) {
                    instance.set(fieldName, i < args.length ? args[i] : null);
                }
            }
        }

        private String constructorParameterPropertyName(Object parameterNode) {
            Object unwrapped = unwrapFunctionParam(parameterNode);
            if (!(unwrapped instanceof Map<?, ?> map)) {
                return null;
            }
            String type = String.valueOf(map.get("type"));
            if ("Identifier".equals(type)) {
                return String.valueOf(map.get("name"));
            }
            if ("AssignmentPattern".equals(type)) {
                return constructorParameterPropertyName(map.get("left"));
            }
            return null;
        }

        private Object argumentsObject(Object[] args) {
            LinkedHashMap<String, Object> values = new LinkedHashMap<>();
            values.put("length", (double) args.length);
            for (int i = 0; i < args.length; i++) {
                values.put(String.valueOf(i), args[i]);
            }
            return values;
        }

        private void installLocalBindings(Map<String, Object> env) {
            env.put(LOCAL_BINDINGS_KEY, new LinkedHashSet<String>());
        }

        private void installClosureBindings(Map<String, Object> env) {
            if (!closure.isEmpty()) {
                env.put(PARENT_CLOSURE_KEY, closure);
            }
        }

        @SuppressWarnings("unchecked")
        private Set<String> localBindings(Map<String, Object> env) {
            Object raw = env.get(LOCAL_BINDINGS_KEY);
            if (raw instanceof Set<?> set) {
                return (Set<String>) set;
            }
            LinkedHashSet<String> created = new LinkedHashSet<>();
            env.put(LOCAL_BINDINGS_KEY, created);
            return created;
        }

        private void markLocalBinding(Map<String, Object> env, String name) {
            if (name != null && !name.isBlank()) {
                localBindings(env).add(name);
            }
        }

        private Map<String, Object> resolveClosure() {
            return closure;
        }

        private Object evalFunctionBody(Map<String, Object> functionAst, Map<String, Object> env) {
            String type = String.valueOf(functionAst.get("type"));
            if ("ArrowFunctionExpression".equals(type) && Boolean.TRUE.equals(functionAst.get("expression"))) {
                return evalNode(functionAst.get("body"), env);
            }
            applyHoistedVarDeclarations(functionAst.get("body"), env);
            return evalNode(functionAst.get("body"), env);
        }

        private Object evalNode(Object node, Map<String, Object> env) {
            if (node == null) {
                return null;
            }
            if (node instanceof String || node instanceof Number || node instanceof Boolean) {
                return node;
            }
            if (node instanceof List<?> list) {
                List<Object> result = new ArrayList<>(list.size());
                for (Object item : list) {
                    result.add(evalNode(item, env));
                }
                return result;
            }
            if (!(node instanceof Map<?, ?> rawMap)) {
                return node;
            }
            Map<String, Object> astNode = castMap(rawMap);
            String type = String.valueOf(astNode.get("type"));
            return switch (type) {
                case "Literal" -> literalValue(astNode);
                case "Identifier" -> resolveIdentifier(String.valueOf(astNode.get("name")), env);
                case "ChainExpression" -> evalNode(astNode.get("expression"), env);
                case "ParenthesizedExpression" -> evalNode(astNode.get("expression"), env);
                case "TSAsExpression", "TSTypeAssertion", "TSNonNullExpression" ->
                        evalNode(astNode.get("expression"), env);
                case "TemplateLiteral" -> evalTemplateLiteral(astNode, env);
                case "TaggedTemplateExpression" -> evalTaggedTemplate(astNode, env);
                case "SequenceExpression" -> evalSequence(astNode, env);
                case "ExpressionStatement" -> evalNode(astNode.get("expression"), env);
                case "ReturnStatement" -> new ReturnSignal(evalNode(astNode.get("argument"), env));
                case "ThrowStatement" -> throwThrownValue(astNode, env);
                case "BlockStatement" -> evalBlock(asList(astNode.get("body")), env);
                case "IfStatement" -> evalIf(astNode, env);
                case "SwitchStatement" -> evalSwitch(astNode, env);
                case "ForStatement" -> evalFor(astNode, env);
                case "ForOfStatement" -> evalForOf(astNode, env);
                case "ForInStatement" -> evalForIn(astNode, env);
                case "WhileStatement" -> evalWhile(astNode, env);
                case "DoWhileStatement" -> evalDoWhile(astNode, env);
                case "BreakStatement" -> new BreakSignal();
                case "ContinueStatement" -> new ContinueSignal();
                case "TryStatement" -> evalTry(astNode, env);
                case "VariableDeclaration" -> evalVariableDeclaration(astNode, env);
                case "CallExpression", "OptionalCallExpression" -> evalCall(astNode, env);
                case "NewExpression" -> evalNew(astNode, env);
                case "MemberExpression", "OptionalMemberExpression" -> evalMember(astNode, env);
                case "ObjectExpression" -> evalObject(astNode, env);
                case "ArrayExpression" -> evalArray(astNode, env);
                case "BinaryExpression", "LogicalExpression", "AssignmentExpression" -> evalBinaryLike(astNode, env, type);
                case "UnaryExpression" -> evalUnary(astNode, env);
                case "UpdateExpression" -> evalUpdate(astNode, env);
                case "ConditionalExpression" -> evalConditional(astNode, env);
                case "FunctionDeclaration" -> evalFunctionDeclaration(astNode, env);
                case "ClassDeclaration" -> evalClassDeclaration(astNode, env);
                case "FunctionExpression", "ArrowFunctionExpression", "ClassExpression" -> createRuntimeFunction(astNode, env);
                case "EmptyStatement", "DebuggerStatement" -> null;
                case "ThisExpression" -> env.getOrDefault("this", GLOBAL_OBJECT);
                default -> throw new IllegalArgumentException("Unsupported runtime AST node: " + type);
            };
        }

        private Object evalFunctionDeclaration(Map<String, Object> astNode, Map<String, Object> env) {
            InterpretedFunction function = createRuntimeFunction(astNode, env);
            Object idNode = astNode.get("id");
            if (idNode instanceof Map<?, ?> rawId) {
                String name = extractPropertyName(rawId);
                if (name != null && !name.isBlank() && !"null".equals(name)) {
                    markLocalBinding(env, name);
                    env.put(name, function);
                }
            }
            return function;
        }

        private Object evalClassDeclaration(Map<String, Object> astNode, Map<String, Object> env) {
            InterpretedFunction classFunction = createRuntimeFunction(astNode, env);
            Object idNode = astNode.get("id");
            if (idNode instanceof Map<?, ?> rawId) {
                String name = extractPropertyName(rawId);
                if (name != null && !name.isBlank() && !"null".equals(name)) {
                    markLocalBinding(env, name);
                    env.put(name, classFunction);
                }
            }
            return classFunction;
        }

        private InterpretedFunction createRuntimeFunction(Map<String, Object> astNode, Map<String, Object> env) {
            LinkedHashMap<String, Object> runtimeDefinition = new LinkedHashMap<>();
            runtimeDefinition.put("__qin_function_model", definition.get("__qin_function_model"));
            runtimeDefinition.put("ast", astNode);
            // JS closures capture the lexical environment, not a one-time value snapshot.
            runtimeDefinition.put("closure", env);
            if ("ArrowFunctionExpression".equals(String.valueOf(astNode.get("type")))) {
                runtimeDefinition.put("thisValue", recoverLexicalThis(env.getOrDefault("this", thisValue)));
                runtimeDefinition.put("__qin_arrow_lexical_this", true);
                copyIfMissing(runtimeDefinition, definition, "ownerClassName");
                copyIfMissing(runtimeDefinition, definition, "ownerSuperClass");
                copyIfMissing(runtimeDefinition, definition, "ownerSuperClassFunction");
                copyIfMissing(runtimeDefinition, definition, "ownerSuperClassValue");
            }
            Object rawSuperName = extractPropertyName(astNode.get("superClass"));
            if (rawSuperName != null) {
                Object superClassValue = resolveIdentifier(String.valueOf(rawSuperName), env);
                runtimeDefinition.put("ownerSuperClass", astNode.get("superClass"));
                runtimeDefinition.put("ownerSuperClassValue", superClassValue);
                if (superClassValue instanceof InterpretedFunction interpretedSuperClass) {
                    runtimeDefinition.put("ownerSuperClassFunction", interpretedSuperClass);
                }
            }
            InterpretedFunction function = new InterpretedFunction(runtimeDefinition);
            Object idNode = astNode.get("id");
            String type = String.valueOf(astNode.get("type"));
            if (("FunctionDeclaration".equals(type) || "ClassDeclaration".equals(type))
                    && idNode instanceof Map<?, ?> rawId) {
                String name = extractPropertyName(rawId);
                if (name != null && !name.isBlank() && !"null".equals(name)) {
                    env.put(name, function);
                }
            }
            return function;
        }

        private void bindExpressionSelfName(Map<String, Object> env) {
            String type = String.valueOf(ast.get("type"));
            if (!"FunctionExpression".equals(type) && !"ClassExpression".equals(type)) {
                return;
            }
            Object idNode = ast.get("id");
            if (!(idNode instanceof Map<?, ?> rawId)) {
                return;
            }
            String name = extractPropertyName(rawId);
            if (name == null || name.isBlank() || "null".equals(name)) {
                return;
            }
            markLocalBinding(env, name);
            env.put(name, this);
        }

        private Object evalBlock(List<?> statements, Map<String, Object> env) {
            hoistFunctionDeclarations(statements, env);
            Object last = null;
            for (Object statement : statements) {
                last = evalNode(statement, env);
                if (last instanceof ReturnSignal
                        || last instanceof BreakSignal
                        || last instanceof ContinueSignal) {
                    return last;
                }
            }
            return last;
        }

        private void applyHoistedVarDeclarations(Object node, Map<String, Object> env) {
            for (String name : hoistedVarBindings(node)) {
                markLocalBinding(env, name);
                if (!env.containsKey(name)) {
                    env.put(name, null);
                }
            }
        }

        private List<String> hoistedVarBindings(Object node) {
            if (cachedHoistedVarBindings == null) {
                LinkedHashSet<String> bindings = new LinkedHashSet<>();
                collectHoistedVarDeclarations(node, bindings);
                cachedHoistedVarBindings = List.copyOf(bindings);
            }
            return cachedHoistedVarBindings;
        }

        private void collectHoistedVarDeclarations(Object node, Set<String> bindings) {
            if (node instanceof List<?> list) {
                for (Object item : list) {
                    collectHoistedVarDeclarations(item, bindings);
                }
                return;
            }
            if (!(node instanceof Map<?, ?> rawNode)) {
                return;
            }
            Map<String, Object> astNode = castMap(rawNode);
            String type = String.valueOf(astNode.get("type"));
            if (isNestedFunctionOrClassScope(type)) {
                return;
            }
            if ("VariableDeclaration".equals(type) && "var".equals(String.valueOf(astNode.get("kind")))) {
                for (Object declaratorNode : asList(astNode.get("declarations"))) {
                    if (declaratorNode instanceof Map<?, ?> rawDeclarator) {
                        Map<String, Object> declarator = castMap(rawDeclarator);
                        collectPatternBindings(declarator.get("id"), bindings);
                    }
                }
            }
            for (Object value : astNode.values()) {
                collectHoistedVarDeclarations(value, bindings);
            }
        }

        private boolean isNestedFunctionOrClassScope(String type) {
            return "FunctionDeclaration".equals(type)
                    || "FunctionExpression".equals(type)
                    || "ArrowFunctionExpression".equals(type)
                    || "ClassDeclaration".equals(type)
                    || "ClassExpression".equals(type);
        }

        private void markPatternBindings(Object patternNode, Map<String, Object> env) {
            if (!(patternNode instanceof Map<?, ?> rawPattern)) {
                return;
            }
            Map<String, Object> pattern = castMap(rawPattern);
            String type = String.valueOf(pattern.get("type"));
            switch (type) {
                case "Identifier" -> {
                    String name = String.valueOf(pattern.get("name"));
                    markLocalBinding(env, name);
                    if (!env.containsKey(name)) {
                        env.put(name, null);
                    }
                }
                case "ArrayPattern" -> {
                    for (Object element : asList(pattern.get("elements"))) {
                        markPatternBindings(element, env);
                    }
                }
                case "ObjectPattern" -> {
                    for (Object propertyNode : asList(pattern.get("properties"))) {
                        if (!(propertyNode instanceof Map<?, ?> rawProperty)) {
                            continue;
                        }
                        Map<String, Object> property = castMap(rawProperty);
                        if ("RestElement".equals(String.valueOf(property.get("type")))) {
                            markPatternBindings(property.get("argument"), env);
                        } else {
                            markPatternBindings(property.getOrDefault("value", property.get("key")), env);
                        }
                    }
                }
                case "RestElement" -> markPatternBindings(pattern.get("argument"), env);
                case "AssignmentPattern" -> markPatternBindings(pattern.get("left"), env);
                default -> {
                }
            }
        }

        private void collectPatternBindings(Object patternNode, Set<String> bindings) {
            if (!(patternNode instanceof Map<?, ?> rawPattern)) {
                return;
            }
            Map<String, Object> pattern = castMap(rawPattern);
            String type = String.valueOf(pattern.get("type"));
            switch (type) {
                case "Identifier" -> {
                    String name = String.valueOf(pattern.get("name"));
                    if (name != null && !name.isBlank()) {
                        bindings.add(name);
                    }
                }
                case "ArrayPattern" -> {
                    for (Object element : asList(pattern.get("elements"))) {
                        collectPatternBindings(element, bindings);
                    }
                }
                case "ObjectPattern" -> {
                    for (Object propertyNode : asList(pattern.get("properties"))) {
                        if (!(propertyNode instanceof Map<?, ?> rawProperty)) {
                            continue;
                        }
                        Map<String, Object> property = castMap(rawProperty);
                        if ("RestElement".equals(String.valueOf(property.get("type")))) {
                            collectPatternBindings(property.get("argument"), bindings);
                        } else {
                            collectPatternBindings(property.getOrDefault("value", property.get("key")), bindings);
                        }
                    }
                }
                case "RestElement" -> collectPatternBindings(pattern.get("argument"), bindings);
                case "AssignmentPattern" -> collectPatternBindings(pattern.get("left"), bindings);
                default -> {
                }
            }
        }

        private void hoistFunctionDeclarations(List<?> statements, Map<String, Object> env) {
            for (Object statement : statements) {
                if (!(statement instanceof Map<?, ?> rawStatement)) {
                    continue;
                }
                Map<String, Object> astNode = castMap(rawStatement);
                if (!"FunctionDeclaration".equals(String.valueOf(astNode.get("type")))) {
                    continue;
                }
                Object idNode = astNode.get("id");
                if (!(idNode instanceof Map<?, ?> rawId)) {
                    continue;
                }
                String name = extractPropertyName(rawId);
                if (name == null || name.isBlank() || "null".equals(name)) {
                    continue;
                }
                markLocalBinding(env, name);
                env.put(name, createRuntimeFunction(astNode, env));
            }
        }

        private Object evalIf(Map<String, Object> astNode, Map<String, Object> env) {
            Object test = evalNode(astNode.get("test"), env);
            if (truthy(test)) {
                return evalNode(astNode.get("consequent"), env);
            }
            return evalNode(astNode.get("alternate"), env);
        }

        private Object evalSwitch(Map<String, Object> astNode, Map<String, Object> env) {
            Object discriminant = evalNode(astNode.get("discriminant"), env);
            List<?> cases = asList(astNode.get("cases"));
            int startIndex = -1;
            int defaultIndex = -1;
            for (int i = 0; i < cases.size(); i++) {
                Map<String, Object> switchCase = castMap(asMap(cases.get(i)));
                Object testNode = switchCase.get("test");
                if (testNode == null) {
                    if (defaultIndex < 0) {
                        defaultIndex = i;
                    }
                    continue;
                }
                Object testValue = evalNode(testNode, env);
                if (Boolean.TRUE.equals(__qin_binary__("===", discriminant, testValue))) {
                    startIndex = i;
                    break;
                }
            }
            if (startIndex < 0) {
                startIndex = defaultIndex;
            }
            if (startIndex < 0) {
                return null;
            }
            Object last = null;
            for (int i = startIndex; i < cases.size(); i++) {
                Map<String, Object> switchCase = castMap(asMap(cases.get(i)));
                for (Object consequent : asList(switchCase.get("consequent"))) {
                    last = evalNode(consequent, env);
                    if (last instanceof BreakSignal) {
                        return null;
                    }
                    if (last instanceof ReturnSignal || last instanceof ContinueSignal) {
                        return last;
                    }
                }
            }
            return last;
        }

        private Object evalConditional(Map<String, Object> astNode, Map<String, Object> env) {
            return truthy(evalNode(astNode.get("test"), env))
                    ? evalNode(astNode.get("consequent"), env)
                    : evalNode(astNode.get("alternate"), env);
        }

        private Object evalTemplateLiteral(Map<String, Object> astNode, Map<String, Object> env) {
            StringBuilder builder = new StringBuilder();
            List<?> quasis = asList(astNode.get("quasis"));
            List<?> expressions = asList(astNode.get("expressions"));
            for (int i = 0; i < quasis.size(); i++) {
                builder.append(extractTemplateQuasiText(quasis.get(i), i, quasis.size()));
                if (i < expressions.size()) {
                    Object evaluated = evalNode(expressions.get(i), env);
                    builder.append(jsString(evaluated));
                }
            }
            return builder.toString();
        }

        private Object evalTaggedTemplate(Map<String, Object> astNode, Map<String, Object> env) {
            Object tagNode = astNode.get("tag");
            if (isStringRawTag(tagNode)) {
                Object quasiNode = astNode.get("quasi");
                if (!(quasiNode instanceof Map<?, ?> rawQuasiNode)) {
                    return "";
                }
                return evalRawTemplateLiteral(castMap(rawQuasiNode), env);
            }
            Object tag = evalNode(tagNode, env);
            Object quasiNode = astNode.get("quasi");
            if (!(quasiNode instanceof Map<?, ?> rawQuasiNode)) {
                throw new IllegalArgumentException("Unsupported tagged template quasi: " + quasiNode);
            }
            return callAny(tag, taggedTemplateArguments(castMap(rawQuasiNode), env));
        }

        private boolean isStringRawTag(Object tagNode) {
            if (!(tagNode instanceof Map<?, ?> rawTagNode)) {
                return false;
            }
            Map<String, Object> tag = castMap(rawTagNode);
            if (!isMemberExpressionType(tag.get("type"))) {
                return false;
            }
            Object objectNode = tag.get("object");
            Object propertyNode = tag.get("property");
            return objectNode instanceof Map<?, ?> rawObjectNode
                    && "Identifier".equals(String.valueOf(castMap(rawObjectNode).get("type")))
                    && "String".equals(String.valueOf(castMap(rawObjectNode).get("name")))
                    && "raw".equals(Boolean.TRUE.equals(tag.get("computed"))
                            ? String.valueOf(evalNode(propertyNode, Map.of()))
                            : extractPropertyName(propertyNode));
        }

        private Object evalRawTemplateLiteral(Map<String, Object> astNode, Map<String, Object> env) {
            StringBuilder builder = new StringBuilder();
            List<?> quasis = asList(astNode.get("quasis"));
            List<?> expressions = asList(astNode.get("expressions"));
            for (int i = 0; i < quasis.size(); i++) {
                builder.append(extractTemplateQuasiRawText(quasis.get(i), i, quasis.size()));
                if (i < expressions.size()) {
                    Object evaluated = evalNode(expressions.get(i), env);
                    builder.append(jsString(evaluated));
                }
            }
            return builder.toString();
        }

        private String extractTemplateQuasiText(Object quasiNode, int index, int totalQuasis) {
            if (!(quasiNode instanceof Map<?, ?> rawQuasi)) {
                return "";
            }
            Map<String, Object> quasi = castMap(rawQuasi);
            String text = null;

            Object valueNode = quasi.get("value");
            if (valueNode instanceof Map<?, ?> rawValue) {
                Map<String, Object> value = castMap(rawValue);
                Object cooked = value.get("cooked");
                if (cooked != null) {
                    text = String.valueOf(cooked);
                } else if (value.get("raw") != null) {
                    text = String.valueOf(value.get("raw"));
                }
            }

            if (text == null) {
                Object cooked = quasi.get("cooked");
                if (cooked != null) {
                    text = String.valueOf(cooked);
                } else if (quasi.get("raw") != null) {
                    text = String.valueOf(quasi.get("raw"));
                } else {
                    text = "";
                }
            }

            return normalizeTemplateQuasiText(text, index, totalQuasis);
        }

        private String extractTemplateQuasiRawText(Object quasiNode, int index, int totalQuasis) {
            if (!(quasiNode instanceof Map<?, ?> rawQuasi)) {
                return "";
            }
            Map<String, Object> quasi = castMap(rawQuasi);
            String text = null;

            Object valueNode = quasi.get("value");
            if (valueNode instanceof Map<?, ?> rawValue) {
                Map<String, Object> value = castMap(rawValue);
                Object raw = value.get("raw");
                if (raw != null) {
                    text = String.valueOf(raw);
                }
            }

            if (text == null && quasi.get("raw") != null) {
                text = String.valueOf(quasi.get("raw"));
            }
            if (text == null) {
                text = "";
            }

            return normalizeTemplateQuasiText(text, index, totalQuasis);
        }

        private Object[] taggedTemplateArguments(Map<String, Object> quasiNode, Map<String, Object> env) {
            List<?> quasis = asList(quasiNode.get("quasis"));
            List<?> expressions = asList(quasiNode.get("expressions"));
            ArrayList<Object> cooked = new ArrayList<>(quasis.size());
            ArrayList<Object> raw = new ArrayList<>(quasis.size());
            for (int i = 0; i < quasis.size(); i++) {
                cooked.add(extractTemplateQuasiText(quasis.get(i), i, quasis.size()));
                raw.add(extractTemplateQuasiRawText(quasis.get(i), i, quasis.size()));
            }
            JavaEsmArrayObject templateObject = new JavaEsmArrayObject(cooked);
            templateObject.memberSet("raw", new JavaEsmArrayObject(raw));

            ArrayList<Object> args = new ArrayList<>(1 + expressions.size());
            args.add(templateObject);
            for (Object expressionNode : expressions) {
                args.add(evalNode(expressionNode, env));
            }
            return args.toArray();
        }

        private String normalizeTemplateQuasiText(String text, int index, int totalQuasis) {
            if (text == null || text.isEmpty()) {
                return "";
            }
            String normalized = text;
            boolean first = index == 0;
            boolean last = index == totalQuasis - 1;

            if (first && normalized.startsWith("`")) {
                normalized = normalized.substring(1);
            }
            if (!first && normalized.startsWith("}")) {
                normalized = normalized.substring(1);
            }
            if (!last && normalized.endsWith("${")) {
                normalized = normalized.substring(0, normalized.length() - 2);
            }
            if (last && normalized.endsWith("`")) {
                normalized = normalized.substring(0, normalized.length() - 1);
            }
            return normalized;
        }

        private Object evalSequence(Map<String, Object> astNode, Map<String, Object> env) {
            Object last = null;
            for (Object expressionNode : asList(astNode.get("expressions"))) {
                last = evalNode(expressionNode, env);
                if (last instanceof ReturnSignal
                        || last instanceof BreakSignal
                        || last instanceof ContinueSignal) {
                    return last;
                }
            }
            return last;
        }

        private Object evalFor(Map<String, Object> astNode, Map<String, Object> env) {
            Object init = astNode.get("init");
            if (init != null) {
                evalNode(init, env);
            }
            Object last = null;
            while (astNode.get("test") == null || truthy(evalNode(astNode.get("test"), env))) {
                last = evalNode(astNode.get("body"), env);
                if (last instanceof ReturnSignal) {
                    return last;
                }
                if (last instanceof BreakSignal) {
                    break;
                }
                if (astNode.get("update") != null) {
                    evalNode(astNode.get("update"), env);
                }
                if (last instanceof ContinueSignal) {
                    continue;
                }
            }
            return null;
        }

        private Object evalForIn(Map<String, Object> astNode, Map<String, Object> env) {
            Object rightValue = evalNode(astNode.get("right"), env);
            if (rightValue == null) {
                return null;
            }
            Object leftNode = astNode.get("left");
            Object last = null;
            for (String key : JavaEsmObject.enumerableEntries(rightValue).keySet()) {
                assignForOfBinding(leftNode, key, env);
                last = evalNode(astNode.get("body"), env);
                if (last instanceof ReturnSignal) {
                    return last;
                }
                if (last instanceof BreakSignal) {
                    break;
                }
                if (last instanceof ContinueSignal) {
                    continue;
                }
            }
            return null;
        }

        private Object evalWhile(Map<String, Object> astNode, Map<String, Object> env) {
            Object last = null;
            while (truthy(evalNode(astNode.get("test"), env))) {
                last = evalNode(astNode.get("body"), env);
                if (last instanceof ReturnSignal) {
                    return last;
                }
                if (last instanceof BreakSignal) {
                    break;
                }
                if (last instanceof ContinueSignal) {
                    continue;
                }
            }
            return null;
        }

        private Object evalForOf(Map<String, Object> astNode, Map<String, Object> env) {
            Object rightValue = evalNode(astNode.get("right"), env);
            if (rightValue == null) {
                throw new IllegalArgumentException(
                        "for...of cannot iterate null; right=" + summarizeAstNode(astNode.get("right")));
            }
            Iterable<?> iterable = asIterableForOf(rightValue);
            Object leftNode = astNode.get("left");
            Object last = null;
            for (Object item : iterable) {
                assignForOfBinding(leftNode, item, env);
                last = evalNode(astNode.get("body"), env);
                if (last instanceof ReturnSignal) {
                    return last;
                }
                if (last instanceof BreakSignal) {
                    break;
                }
                if (last instanceof ContinueSignal) {
                    continue;
                }
            }
            return null;
        }

        private String summarizeAstNode(Object node) {
            if (!(node instanceof Map<?, ?> rawMap)) {
                return String.valueOf(node);
            }
            Map<String, Object> map = castMap(rawMap);
            String type = String.valueOf(map.get("type"));
            if ("Identifier".equals(type)) {
                return "Identifier(" + map.get("name") + ")";
            }
            if ("MemberExpression".equals(type)) {
                return "MemberExpression(object="
                        + summarizeAstNode(map.get("object"))
                        + ", property="
                        + summarizeAstNode(map.get("property"))
                        + ")";
            }
            if ("CallExpression".equals(type)) {
                return "CallExpression(callee=" + summarizeAstNode(map.get("callee")) + ")";
            }
            if ("BinaryExpression".equals(type) || "LogicalExpression".equals(type)) {
                return type
                        + "(operator=" + map.get("operator")
                        + ", left=" + summarizeAstNode(map.get("left"))
                        + ", right=" + summarizeAstNode(map.get("right"))
                        + ")";
            }
            if ("Literal".equals(type)) {
                return "Literal(" + map.get("value") + ")";
            }
            return type;
        }

        private String summarizeParams() {
            List<String> params = new ArrayList<>();
            for (Object param : asList(ast.get("params"))) {
                params.add(summarizeAstNode(param));
            }
            return params.toString();
        }

        private Object evalDoWhile(Map<String, Object> astNode, Map<String, Object> env) {
            Object last;
            do {
                last = evalNode(astNode.get("body"), env);
                if (last instanceof ReturnSignal) {
                    return last;
                }
                if (last instanceof BreakSignal) {
                    break;
                }
            } while (truthy(evalNode(astNode.get("test"), env)));
            return null;
        }

        private Object evalTry(Map<String, Object> astNode, Map<String, Object> env) {
            Object result;
            try {
                result = evalNode(astNode.get("block"), env);
            } catch (ThrownValue thrown) {
                result = handleCatch(astNode.get("handler"), env, thrown.value());
            } catch (RuntimeException runtimeException) {
                result = handleCatch(astNode.get("handler"), env, runtimeException);
            } finally {
                Object finalizer = astNode.get("finalizer");
                if (finalizer != null) {
                    Object finalResult = evalNode(finalizer, env);
                    if (finalResult instanceof ReturnSignal
                            || finalResult instanceof BreakSignal
                            || finalResult instanceof ContinueSignal) {
                        return finalResult;
                    }
                }
            }
            return result;
        }

        private Object handleCatch(Object handlerNode, Map<String, Object> env, Object thrownValue) {
            if (handlerNode == null) {
                if (thrownValue instanceof RuntimeException runtimeException) {
                    throw runtimeException;
                }
                throw new ThrownValue(thrownValue);
            }
            if (!(handlerNode instanceof Map<?, ?> rawHandler)) {
                throw new IllegalArgumentException("Unsupported catch handler: " + handlerNode);
            }
            Map<String, Object> handler = castMap(rawHandler);
            Map<String, Object> catchEnv = createChildLexicalEnv(env);
            Object paramNode = handler.get("param");
            if (paramNode instanceof Map<?, ?> rawParam) {
                bindPattern(rawParam, thrownValue, catchEnv);
            }
            return evalNode(handler.get("body"), catchEnv);
        }

        private Map<String, Object> createChildLexicalEnv(Map<String, Object> parent) {
            Map<String, Object> child = new LinkedHashMap<>();
            installLocalBindings(child);
            child.put(PARENT_CLOSURE_KEY, parent);
            return child;
        }

        private Object evalVariableDeclaration(Map<String, Object> astNode, Map<String, Object> env) {
            Object last = null;
            for (Object declaratorNode : asList(astNode.get("declarations"))) {
                if (!(declaratorNode instanceof Map<?, ?> rawDeclarator)) {
                    continue;
                }
                Map<String, Object> declarator = castMap(rawDeclarator);
                Object idNode = declarator.get("id");
                if (!(idNode instanceof Map<?, ?> rawId)) {
                    throw new IllegalArgumentException("Unsupported variable declarator id: " + idNode);
                }
                Object init = declarator.containsKey("init") ? evalNode(declarator.get("init"), env) : null;
                bindDeclarationPattern(rawId, init, env);
                last = init;
            }
            return last;
        }

        private void bindDeclarationPattern(Object patternNode, Object value, Map<String, Object> env) {
            Map<String, Object> pattern = castMap(asMap(patternNode));
            if ("Identifier".equals(String.valueOf(pattern.get("type")))) {
                String name = String.valueOf(pattern.get("name"));
                if (shouldWriteThroughTopLevelBinding(name)) {
                    Object assigned = __qin_assign__(name, value);
                    markLocalBinding(env, name);
                    env.put(name, assigned);
                    return;
                }
            }
            bindPattern(patternNode, value, env);
        }

        private void assignForOfBinding(Object leftNode, Object item, Map<String, Object> env) {
            if (!(leftNode instanceof Map<?, ?> rawLeft)) {
                throw new IllegalArgumentException("Unsupported for-of binding: " + leftNode);
            }
            Map<String, Object> left = castMap(rawLeft);
            String type = String.valueOf(left.get("type"));
            if ("VariableDeclaration".equals(type)) {
                List<?> declarations = asList(left.get("declarations"));
                if (declarations.size() != 1) {
                    throw new IllegalArgumentException("Unsupported for-of declaration count: " + declarations.size());
                }
                Map<String, Object> declarator = castMap(asMap(declarations.get(0)));
                bindPattern(declarator.get("id"), item, env);
                return;
            }
            if ("Identifier".equals(type)) {
                String name = String.valueOf(left.get("name"));
                if (localBindings(env).contains(name) && !env.containsKey(name)) {
                    env.put(name, item);
                    return;
                }
                if (!localBindings(env).contains(name)
                        && !shouldWriteThroughTopLevelBinding(name)) {
                    markLocalBinding(env, name);
                    env.put(name, item);
                    return;
                }
                assignIdentifier(name, item, env);
                return;
            }
            bindPattern(left, item, env);
        }

        private void bindPattern(Object patternNode, Object value, Map<String, Object> env) {
            Map<String, Object> pattern = castMap(asMap(patternNode));
            String type = String.valueOf(pattern.get("type"));
            switch (type) {
                case "Identifier" -> {
                    String name = String.valueOf(pattern.get("name"));
                    markLocalBinding(env, name);
                    env.put(name, value);
                }
                case "ArrayPattern" -> bindArrayPattern(pattern, value, env);
                case "ObjectPattern" -> bindObjectPattern(pattern, value, env);
                case "RestElement" -> bindPattern(pattern.get("argument"), value, env);
                case "AssignmentPattern" -> {
                    Object boundValue = value == null ? evalNode(pattern.get("right"), env) : value;
                    bindPattern(pattern.get("left"), boundValue, env);
                }
                default -> throw new IllegalArgumentException("Unsupported binding pattern: " + type);
            }
        }

        private void bindArrayPattern(Map<String, Object> pattern, Object value, Map<String, Object> env) {
            List<?> elements = asList(pattern.get("elements"));
            List<Object> values = arrayPatternValues(value);
            for (int i = 0; i < elements.size(); i++) {
                Object element = elements.get(i);
                if (element == null) {
                    continue;
                }
                if (element instanceof Map<?, ?> rawElement
                        && "RestElement".equals(String.valueOf(rawElement.get("type")))) {
                    bindPattern(element, new ArrayList<>(values.subList(Math.min(i, values.size()), values.size())), env);
                    continue;
                }
                bindPattern(element, i < values.size() ? values.get(i) : null, env);
            }
        }

        private List<Object> arrayPatternValues(Object value) {
            if (value == null) {
                return List.of();
            }
            if (value instanceof List<?> list) {
                return new ArrayList<>(list);
            }
            if (value instanceof Collection<?> collection) {
                return new ArrayList<>(collection);
            }
            if (value.getClass().isArray()) {
                int length = Array.getLength(value);
                List<Object> values = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    values.add(Array.get(value, i));
                }
                return values;
            }
            if (value instanceof Iterable<?> iterable) {
                List<Object> values = new ArrayList<>();
                for (Object item : iterable) {
                    values.add(item);
                }
                return values;
            }
            throw new IllegalArgumentException("Array pattern expects iterable value: " + simpleName(value));
        }

        private void bindObjectPattern(Map<String, Object> pattern, Object value, Map<String, Object> env) {
            for (Object propertyNode : asList(pattern.get("properties"))) {
                if (!(propertyNode instanceof Map<?, ?> rawProperty)) {
                    continue;
                }
                Map<String, Object> property = castMap(rawProperty);
                String propertyType = String.valueOf(property.get("type"));
                if ("RestElement".equals(propertyType)) {
                    bindPattern(property.get("argument"), objectPatternRest(value), env);
                    continue;
                }
                if (!"Property".equals(propertyType)) {
                    throw new IllegalArgumentException("Unsupported object binding property: " + propertyType);
                }
                String key = extractPropertyName(property.get("key"));
                Object targetPattern = property.containsKey("value") ? property.get("value") : property.get("key");
                bindPattern(targetPattern, __qin_member_get__(value, key), env);
            }
        }

        private Object objectPatternRest(Object value) {
            if (value instanceof Map<?, ?> map) {
                return new LinkedHashMap<>(castMap(map));
            }
            return new LinkedHashMap<String, Object>();
        }

        private void bindForOfIdentifier(Object idNode, Object item, Map<String, Object> env) {
            bindPattern(idNode, item, env);
        }

        /*
         * Kept for compatibility with older call sites while the interpreter's
         * declaration and loop binding paths converge on bindPattern(...).
         */
        @SuppressWarnings("unused")
        private void bindForOfIdentifierLegacy(Object idNode, Object item, Map<String, Object> env) {
            bindPattern(idNode, item, env);
        }

        private void unsupportedForOfBinding(String type) {
            throw new IllegalArgumentException("Unsupported for-of binding type: " + type);
        }

        private Object evalCall(Map<String, Object> astNode, Map<String, Object> env) {
            List<?> arguments = asList(astNode.get("arguments"));
            List<Object> evaluatedArguments = new ArrayList<>();
            for (Object argumentNode : arguments) {
                if (argumentNode instanceof Map<?, ?> rawArgument) {
                    Map<String, Object> argument = castMap(rawArgument);
                    if ("SpreadElement".equals(argument.get("type"))) {
                        addSpreadValues(evaluatedArguments, evalNode(argument.get("argument"), env));
                        continue;
                    }
                }
                evaluatedArguments.add(evalNode(argumentNode, env));
            }
            Object[] evaluated = evaluatedArguments.toArray();
            Object calleeNode = astNode.get("callee");
            if (calleeNode instanceof Map<?, ?> rawCallee) {
                Map<String, Object> calleeAst = castMap(rawCallee);
                if (isSuperNode(calleeAst)) {
                    return callSuperConstructor(env, evaluated);
                }
                if (isMemberExpressionType(calleeAst.get("type"))) {
                    if (isSuperNode(calleeAst.get("object"))) {
                        Object property = Boolean.TRUE.equals(calleeAst.get("computed"))
                                ? evalNode(calleeAst.get("property"), env)
                                : extractPropertyName(calleeAst.get("property"));
                        return callSuperMethod(property, env, evaluated);
                    }
                    Object target = evalNode(calleeAst.get("object"), env);
                    Object property = Boolean.TRUE.equals(calleeAst.get("computed"))
                            ? evalNode(calleeAst.get("property"), env)
                            : extractPropertyName(calleeAst.get("property"));
                    if (target == null) {
                        if (Boolean.TRUE.equals(calleeAst.get("optional"))
                                || "OptionalCallExpression".equals(astNode.get("type"))) {
                            return null;
                        }
                        throw new IllegalArgumentException(
                                "Cannot call method on null"
                                        + "; function=" + functionDebugName()
                                        + "; method=" + property
                                        + "; object=" + summarizeAstNode(calleeAst.get("object")));
                    }
                    if (isOptionalCall(astNode, calleeAst) && !hasCallableMember(target, property)) {
                        return null;
                    }
                    try {
                        if (property instanceof String propertyName && propertyName.startsWith("__qin_subhuti_raw_")) {
                            Object method = __qin_member_get__(target, propertyName);
                            if (method instanceof InterpretedFunction interpretedFunction) {
                                Object receiver = target instanceof InterpretedInstance
                                        ? target
                                        : env.getOrDefault("this", GLOBAL_OBJECT);
                                return interpretedFunction.bindThis(receiver).call(evaluated);
                            }
                        }
                        return callMethod(target, property, evaluated);
                    } catch (ThrownValue thrown) {
                        throw thrown;
                    } catch (RuntimeException error) {
                        throw new IllegalArgumentException(
                                "Failed runtime method call"
                                        + "; function=" + functionDebugName()
                                        + "; method=" + property
                                        + "; object=" + summarizeAstNode(calleeAst.get("object"))
                                        + "; target=" + summarizeRuntimeValue(target),
                                error);
                    }
                }
            }
            Object callee = evalNode(calleeNode, env);
            if (callee == null) {
                throw new IllegalArgumentException(
                        "Unsupported callable: null"
                                + "; function=" + functionDebugName()
                                + "; callee=" + summarizeAstNode(calleeNode)
                                + "; params=" + summarizeParams()
                                + "; envKeys=" + env.keySet());
            }
            if (callee instanceof InterpretedFunction interpretedFunction
                    && isGeneratedRawRuleIdentifier(calleeNode)) {
                return interpretedFunction.bindThis(env.getOrDefault("this", GLOBAL_OBJECT)).call(evaluated);
            }
            try {
                return callAny(callee, evaluated);
            } catch (ThrownValue thrown) {
                throw thrown;
            } catch (RuntimeException error) {
                throw new IllegalArgumentException(
                        "Failed runtime callable"
                                + "; function=" + functionDebugName()
                                + "; callee=" + summarizeAstNode(calleeNode)
                                + "; value=" + summarizeRuntimeValue(callee)
                                + "; args=" + describeArgs(evaluated),
                        error);
            }
        }

        private boolean isGeneratedRawRuleIdentifier(Object calleeNode) {
            if (!(calleeNode instanceof Map<?, ?> rawCallee)) {
                return false;
            }
            Map<String, Object> calleeAst = castMap(rawCallee);
            if (!"Identifier".equals(calleeAst.get("type"))) {
                return false;
            }
            return String.valueOf(calleeAst.get("name")).startsWith("__qin_subhuti_raw_");
        }

        private Object evalNew(Map<String, Object> astNode, Map<String, Object> env) {
            Object calleeNode = astNode.get("callee");
            String directCalleeName = calleeNode instanceof Map<?, ?> rawCallee
                    ? extractPropertyName(rawCallee)
                    : null;
            Object callee = evalNode(calleeNode, env);
            if (directCalleeName != null
                    && isBuiltinConstructorName(directCalleeName)
                    && !localBindings(env).contains(directCalleeName)) {
                callee = directCalleeName;
            }
            if (callee == null) {
                String calleeName = directCalleeName == null ? String.valueOf(calleeNode) : directCalleeName;
                throw new IllegalArgumentException(
                        "Unsupported constructor target: null"
                                + "; function=" + functionDebugName()
                                + "; callee=" + calleeName
                                + "; envHasCallee=" + env.containsKey(calleeName)
                                + "; globalHasCallee=" + (__qin_global__(calleeName) != null));
            }
            List<?> arguments = asList(astNode.get("arguments"));
            List<Object> evaluatedArguments = new ArrayList<>();
            for (Object argumentNode : arguments) {
                if (argumentNode instanceof Map<?, ?> rawArgument) {
                    Map<String, Object> argument = castMap(rawArgument);
                    if ("SpreadElement".equals(argument.get("type"))) {
                        addSpreadValues(evaluatedArguments, evalNode(argument.get("argument"), env));
                        continue;
                    }
                }
                evaluatedArguments.add(evalNode(argumentNode, env));
            }
            return JavaEsmGlobal.construct(callee, evaluatedArguments.toArray());
        }

        private Object evalMember(Map<String, Object> astNode, Map<String, Object> env) {
            if (isSuperNode(astNode.get("object"))) {
                Object propertyNode = astNode.get("property");
                boolean computed = Boolean.TRUE.equals(astNode.get("computed"));
                Object propertyValue = computed ? evalNode(propertyNode, env) : extractPropertyName(propertyNode);
                return resolveSuperMethod(propertyValue, env);
            }
            Object objectValue = evalNode(astNode.get("object"), env);
            if (objectValue == null && Boolean.TRUE.equals(astNode.get("optional"))) {
                return null;
            }
            Object propertyNode = astNode.get("property");
            boolean computed = Boolean.TRUE.equals(astNode.get("computed"));
            Object propertyValue = computed ? evalNode(propertyNode, env) : extractPropertyName(propertyNode);
            return __qin_member_get__(objectValue, propertyValue);
        }

        private boolean isMemberExpressionType(Object type) {
            return "MemberExpression".equals(type) || "OptionalMemberExpression".equals(type);
        }

        private boolean isOptionalCall(Map<String, Object> callAst, Map<String, Object> calleeAst) {
            return Boolean.TRUE.equals(calleeAst.get("optional"))
                    || "OptionalMemberExpression".equals(calleeAst.get("type"))
                    || "OptionalCallExpression".equals(callAst.get("type"))
                    || Boolean.TRUE.equals(callAst.get("optional"));
        }

        private boolean isSuperNode(Object node) {
            return node instanceof Map<?, ?> rawNode
                    && "Super".equals(String.valueOf(castMap(rawNode).get("type")));
        }

        private Object callSuperConstructor(Map<String, Object> env, Object[] args) {
            Object receiver = env.get("this");
            if (!(receiver instanceof InterpretedInstance instance)) {
                throw new IllegalArgumentException(
                        "super constructor is not available without an interpreted instance: "
                                + functionDebugName());
            }
            InterpretedFunction constructor = resolveNearestOwnSuperConstructor();
            if (constructor == null) {
                Object constructed = constructNonInterpretedSuper(args);
                if (constructed != null) {
                    mergeConstructedSuperInstance(instance, constructed);
                }
                return receiver;
            }
            Object constructed = constructor.bindThis(receiver).callConstructor(args);
            if (constructed instanceof ReturnSignal signal) {
                Object returned = signal.value();
                if (returned instanceof Map<?, ?> || returned instanceof QinRuntimeObject) {
                    return returned;
                }
            }
            return receiver;
        }

        private Object constructNonInterpretedSuper(Object[] args) {
            Object superClassValue = unwrapExportSlotValue(definition.get("ownerSuperClassValue"));
            if (superClassValue == null) {
                Object superClassNode = definition.get("ownerSuperClass");
                if (superClassNode instanceof Map<?, ?> rawSuperClass) {
                    String superName = extractPropertyName(rawSuperClass);
                    if (superName != null && !superName.isBlank() && !"null".equals(superName)) {
                        superClassValue = unwrapExportSlotValue(resolveIdentifier(superName, resolveClosure()));
                        if (superClassValue == null) {
                            superClassValue = unwrapExportSlotValue(__qin_global__(superName));
                        }
                    }
                }
            }
            if (superClassValue == null || superClassValue instanceof InterpretedFunction) {
                return null;
            }
            if (!isRuntimeCallable(superClassValue) && !(superClassValue instanceof Class<?>)) {
                return null;
            }
            return JavaEsmGlobal.construct(superClassValue, args);
        }

        private void mergeConstructedSuperInstance(InterpretedInstance receiver, Object constructed) {
            constructed = unwrapExportSlotValue(constructed);
            if (constructed instanceof InterpretedInstance interpretedSuper) {
                receiver.fields.putAll(interpretedSuper.fieldSnapshot());
                return;
            }
            Map<String, Object> entries = __qin_own_enumerable_entries__(constructed);
            if (entries != null) {
                receiver.fields.putAll(entries);
            }
        }

        private InterpretedFunction resolveNearestOwnSuperConstructor() {
            InterpretedFunction parent = resolveSuperClassFunction();
            while (parent != null) {
                InterpretedFunction constructor = parent.collectInstanceMethods().get("constructor");
                if (constructor != null) {
                    return constructor;
                }
                parent = parent.resolveSuperClassFunction();
            }
            return null;
        }

        private Object callSuperMethod(Object property, Map<String, Object> env, Object[] args) {
            Object method = resolveSuperMethod(property, env);
            if (method instanceof InterpretedFunction interpretedFunction) {
                return interpretedFunction.bindThis(env.getOrDefault("this", GLOBAL_OBJECT)).call(args);
            }
            return callRuntimeMethodValue(env.getOrDefault("this", GLOBAL_OBJECT), method, args);
        }

        private Object resolveSuperMethod(Object property, Map<String, Object> env) {
            InterpretedFunction parent = resolveSuperClassFunction();
            if (parent == null) {
                Object thisObject = env.get("this");
                if (thisObject instanceof InterpretedInstance instance) {
                    Object resolved = resolveSuperMemberFromInstance(instance, property, env);
                    if (resolved != null) {
                        return resolved;
                    }
                }
                throw new IllegalArgumentException(
                        "super is not available in function: " + functionDebugName()
                                + "; definitionKeys=" + definition.keySet()
                                + "; ownerSuperClass=" + summarizeAstNode(definition.get("ownerSuperClass"))
                                + "; astKeys=" + ast.keySet());
            }
            String name = propertyKey(property);
            InterpretedFunction method = resolveSuperOwnMethod(parent, name);
            if (method != null) {
                return method;
            }
            AccessorProperty accessor = resolveSuperOwnAccessor(parent, name);
            if (accessor != null && accessor.getter != null) {
                return accessor.getter.bindThis(env.getOrDefault("this", GLOBAL_OBJECT)).call();
            }
            throw new IllegalArgumentException("Unknown super method: " + functionDebugName() + "." + name);
        }

        private InterpretedFunction resolveSuperOwnMethod(InterpretedFunction parent, String name) {
            InterpretedFunction current = parent;
            while (current != null) {
                InterpretedFunction method = current.collectInstanceMethods().get(name);
                if (method != null) {
                    return method;
                }
                current = current.resolveSuperClassFunction();
            }
            return null;
        }

        private AccessorProperty resolveSuperOwnAccessor(InterpretedFunction parent, String name) {
            InterpretedFunction current = parent;
            while (current != null) {
                AccessorProperty accessor = current.collectInstanceAccessors().get(name);
                if (accessor != null) {
                    return accessor;
                }
                current = current.resolveSuperClassFunction();
            }
            return null;
        }

        private Object resolveSuperMemberFromInstance(InterpretedInstance instance, Object property, Map<String, Object> env) {
            String name = propertyKey(property);
            InterpretedFunction method = instance.superMethods.get(name);
            if (method != null) {
                return method;
            }
            AccessorProperty accessor = instance.superAccessors.get(name);
            if (accessor != null && accessor.getter != null) {
                return accessor.getter.bindThis(env.getOrDefault("this", GLOBAL_OBJECT)).call();
            }
            return null;
        }

        private Object evalObject(Map<String, Object> astNode, Map<String, Object> env) {
            LinkedHashMap<String, Object> object = new LinkedHashMap<>();
            for (Object propertyNode : asList(astNode.get("properties"))) {
                Map<String, Object> property = castMap(asMap(propertyNode));
                if (property.containsKey("property")
                        && property.get("property") instanceof Map<?, ?> wrappedProperty) {
                    property = castMap(wrappedProperty);
                }
                if ("SpreadElement".equals(property.get("type"))) {
                    Object spreadValue = evalNode(property.get("argument"), env);
                    if (spreadValue == null) {
                        continue;
                    }
                    if (spreadValue instanceof Map<?, ?> spreadMap) {
                        object.putAll(castMap(spreadMap));
                        continue;
                    }
                    if (spreadValue instanceof InterpretedInstance instance) {
                        object.putAll(instance.ownEnumerableProperties());
                        continue;
                    }
                    if (spreadValue != null && spreadValue.getClass().isRecord()) {
                        object.putAll(javaRecordEntries(spreadValue));
                        continue;
                    }
                    throw new IllegalArgumentException("Object spread expects a map-like value");
                }
                object.put(
                        extractPropertyName(property.get("key")),
                        property.containsKey("value")
                                ? evalNode(property.get("value"), env)
                                : resolveIdentifier(extractPropertyName(property.get("key")), env));
            }
            return object;
        }

        private Object evalArray(Map<String, Object> astNode, Map<String, Object> env) {
            List<Object> result = new ArrayList<>();
            for (Object element : asList(astNode.get("elements"))) {
                if (element instanceof Map<?, ?> rawElement) {
                    Map<String, Object> spreadElement = castMap(rawElement);
                    if ("SpreadElement".equals(spreadElement.get("type"))) {
                        addSpreadValues(result, evalNode(spreadElement.get("argument"), env));
                        continue;
                    }
                }
                result.add(evalNode(element, env));
            }
            return result;
        }

        private Object evalBinaryLike(Map<String, Object> astNode, Map<String, Object> env, String type) {
            if ("AssignmentExpression".equals(type)) {
                Object leftNode = astNode.get("left");
                String operator = String.valueOf(astNode.get("operator"));
                if (leftNode instanceof Map<?, ?> rawLeft) {
                    Map<String, Object> left = castMap(rawLeft);
                    if ("Identifier".equals(left.get("type"))) {
                        String name = String.valueOf(left.get("name"));
                        Object value = evalAssignmentValue(operator, resolveIdentifier(name, env), astNode.get("right"), env);
                        return assignIdentifier(name, value, env);
                    }
                    if (isMemberExpressionType(left.get("type"))) {
                        Object target = evalNode(left.get("object"), env);
                        Object property = Boolean.TRUE.equals(left.get("computed"))
                                ? evalNode(left.get("property"), env)
                                : extractPropertyName(left.get("property"));
                        Object current = __qin_member_get__(target, property);
                        Object value = evalAssignmentValue(operator, current, astNode.get("right"), env);
                        try {
                            return __qin_member_set__(target, property, value);
                        } catch (RuntimeException error) {
                            throw new IllegalArgumentException(
                                    "Failed member assignment"
                                            + "; function=" + debugFunctionName()
                                            + "; assignment=" + summarizeAstNode(astNode)
                                            + "; left=" + summarizeAstNode(left)
                                            + "; target=" + summarizeRuntimeValue(target)
                                            + "; property=" + property
                                            + "; current=" + summarizeRuntimeValue(current)
                                            + "; value=" + summarizeRuntimeValue(value),
                                    error);
                        }
                    }
                    if ("ObjectPattern".equals(left.get("type")) || "ArrayPattern".equals(left.get("type"))) {
                        if (!"=".equals(operator)) {
                            throw new IllegalArgumentException("Unsupported destructuring assignment operator: " + operator);
                        }
                        Object value = evalNode(astNode.get("right"), env);
                        assignPattern(left, value, env);
                        return value;
                    }
                }
                throw new IllegalArgumentException("Unsupported assignment target");
            }
            if ("LogicalExpression".equals(type)) {
                return evalLogical(astNode, env);
            }
            return __qin_binary__(
                    astNode.get("operator"),
                    evalBinaryOperand(astNode, "left", env),
                    evalBinaryOperand(astNode, "right", env));
        }

        private String debugFunctionName() {
            Object explicitName = definition.get("functionName");
            if (explicitName != null) {
                Object ownerClassName = definition.get("ownerClassName");
                if (ownerClassName != null) {
                    return ownerClassName + "." + explicitName;
                }
                return String.valueOf(explicitName);
            }
            Object idNode = ast.get("id");
            if (idNode instanceof Map<?, ?> rawId) {
                String name = extractPropertyName(castMap(rawId));
                if (name != null && !name.isBlank() && !"null".equals(name)) {
                    return name;
                }
            }
            Object debugNode = definition.get("debugNode");
            return debugNode == null ? "<anonymous>" : String.valueOf(debugNode);
        }

        private Object evalBinaryOperand(Map<String, Object> astNode, String side, Map<String, Object> env) {
            try {
                return evalNode(astNode.get(side), env);
            } catch (RuntimeException error) {
                throw new IllegalArgumentException(
                        "Failed to evaluate " + side + " operand"
                                + "; expression=" + summarizeAstNode(astNode)
                                + "; operand=" + summarizeAstNode(astNode.get(side)),
                        error);
            }
        }

        private Object evalLogical(Map<String, Object> astNode, Map<String, Object> env) {
            String operator = String.valueOf(astNode.get("operator"));
            Object left = evalNode(astNode.get("left"), env);
            return switch (operator) {
                case "&&" -> truthy(left) ? evalNode(astNode.get("right"), env) : left;
                case "||" -> truthy(left) ? left : evalNode(astNode.get("right"), env);
                case "??" -> left != null ? left : evalNode(astNode.get("right"), env);
                default -> throw new IllegalArgumentException("Unsupported logical operator: " + operator);
            };
        }

        private Object evalAssignmentValue(String operator, Object currentValue, Object rightNode, Map<String, Object> env) {
            Object rightValue = evalNode(rightNode, env);
            return switch (operator) {
                case "=" -> rightValue;
                case "+=" -> __qin_binary__("+", currentValue, rightValue);
                case "-=" -> __qin_binary__("-", currentValue, rightValue);
                case "*=" -> __qin_binary__("*", currentValue, rightValue);
                case "/=" -> __qin_binary__("/", currentValue, rightValue);
                case "%=" -> __qin_binary__("%", currentValue, rightValue);
                case "|=" -> __qin_binary__("|", currentValue, rightValue);
                case "&=" -> __qin_binary__("&", currentValue, rightValue);
                case "^=" -> __qin_binary__("^", currentValue, rightValue);
                case "<<=" -> __qin_binary__("<<", currentValue, rightValue);
                case ">>=" -> __qin_binary__(">>", currentValue, rightValue);
                case ">>>=" -> __qin_binary__(">>>", currentValue, rightValue);
                default -> throw new IllegalArgumentException("Unsupported assignment operator: " + operator);
            };
        }

        private void assignPattern(Object patternNode, Object value, Map<String, Object> env) {
            Map<String, Object> pattern = castMap(asMap(patternNode));
            String type = String.valueOf(pattern.get("type"));
            switch (type) {
                case "Identifier" -> assignIdentifier(String.valueOf(pattern.get("name")), value, env);
                case "MemberExpression", "OptionalMemberExpression" -> {
                    Object target = evalNode(pattern.get("object"), env);
                    Object property = Boolean.TRUE.equals(pattern.get("computed"))
                            ? evalNode(pattern.get("property"), env)
                            : extractPropertyName(pattern.get("property"));
                    __qin_member_set__(target, property, value);
                }
                case "ArrayPattern" -> assignArrayPattern(pattern, value, env);
                case "ObjectPattern" -> assignObjectPattern(pattern, value, env);
                case "RestElement" -> assignPattern(pattern.get("argument"), value, env);
                case "AssignmentPattern" -> {
                    Object boundValue = value == null ? evalNode(pattern.get("right"), env) : value;
                    assignPattern(pattern.get("left"), boundValue, env);
                }
                default -> throw new IllegalArgumentException("Unsupported assignment pattern: " + type);
            }
        }

        private void assignArrayPattern(Map<String, Object> pattern, Object value, Map<String, Object> env) {
            List<?> elements = asList(pattern.get("elements"));
            List<Object> values = arrayPatternValues(value);
            for (int i = 0; i < elements.size(); i++) {
                Object element = elements.get(i);
                if (element == null) {
                    continue;
                }
                if (element instanceof Map<?, ?> rawElement
                        && "RestElement".equals(String.valueOf(rawElement.get("type")))) {
                    assignPattern(element, new ArrayList<>(values.subList(Math.min(i, values.size()), values.size())), env);
                    continue;
                }
                assignPattern(element, i < values.size() ? values.get(i) : null, env);
            }
        }

        private void assignObjectPattern(Map<String, Object> pattern, Object value, Map<String, Object> env) {
            for (Object propertyNode : asList(pattern.get("properties"))) {
                if (!(propertyNode instanceof Map<?, ?> rawProperty)) {
                    continue;
                }
                Map<String, Object> property = castMap(rawProperty);
                String propertyType = String.valueOf(property.get("type"));
                if ("RestElement".equals(propertyType)) {
                    assignPattern(property.get("argument"), objectPatternRest(value), env);
                    continue;
                }
                if (!"Property".equals(propertyType)) {
                    throw new IllegalArgumentException("Unsupported object assignment property: " + propertyType);
                }
                String key = extractPropertyName(property.get("key"));
                Object targetPattern = property.containsKey("value") ? property.get("value") : property.get("key");
                assignPattern(targetPattern, __qin_member_get__(value, key), env);
            }
        }

        private Object evalUpdate(Map<String, Object> astNode, Map<String, Object> env) {
            Object argumentNode = astNode.get("argument");
            String operator = String.valueOf(astNode.get("operator"));
            boolean prefix = Boolean.TRUE.equals(astNode.get("prefix"));
            double delta = "++".equals(operator) ? 1.0d : -1.0d;

            if (argumentNode instanceof Map<?, ?> rawArgument) {
                Map<String, Object> argument = castMap(rawArgument);
                if ("Identifier".equals(argument.get("type"))) {
                    String name = String.valueOf(argument.get("name"));
                    double current = requireNumber(asNumber(resolveIdentifier(name, env)), operator);
                    double next = current + delta;
                    assignIdentifier(name, next, env);
                    return prefix ? next : current;
                }
                if (isMemberExpressionType(argument.get("type"))) {
                    Object target = evalNode(argument.get("object"), env);
                    Object property = Boolean.TRUE.equals(argument.get("computed"))
                            ? evalNode(argument.get("property"), env)
                            : extractPropertyName(argument.get("property"));
                    double current = requireNumber(asNumber(__qin_member_get__(target, property)), operator);
                    double next = current + delta;
                    __qin_member_set__(target, property, next);
                    return prefix ? next : current;
                }
            }
            throw new IllegalArgumentException("Unsupported update target");
        }

        private Object evalUnary(Map<String, Object> astNode, Map<String, Object> env) {
            String operator = String.valueOf(astNode.get("operator"));
            Object argumentNode = astNode.get("argument");
            if ("delete".equals(operator) && argumentNode instanceof Map<?, ?> rawArgument) {
                Map<String, Object> argument = castMap(rawArgument);
                if (isMemberExpressionType(argument.get("type"))) {
                    Object target = evalNode(argument.get("object"), env);
                    Object property = Boolean.TRUE.equals(argument.get("computed"))
                            ? evalNode(argument.get("property"), env)
                            : extractPropertyName(argument.get("property"));
                    return __qin_delete_member__(target, property);
                }
                return true;
            }
            return __qin_unary__(operator, evalNode(argumentNode, env));
        }

        private Object assignIdentifier(String name, Object value, Map<String, Object> env) {
            if (localBindings(env).contains(name)) {
                env.put(name, value);
                return value;
            }
            if (shouldWriteThroughTopLevelBinding(name)) {
                Object assigned = __qin_assign__(name, value);
                env.put(name, assigned);
                return assigned;
            }
            Object refName = capturedReferenceName(name, env);
            if (refName != null) {
                Object assigned = __qin_assign__(refName, value);
                env.put(name, referenceDescriptor(refName));
                return assigned;
            }
            if (!localBindings(env).contains(name) && assignOuterLexicalBinding(name, value, env)) {
                return value;
            }
            env.put(name, value);
            return value;
        }

        private Object capturedReferenceName(String name, Map<String, Object> env) {
            Object envRef = referenceName(resolveRawLexicalValue(name, env));
            if (envRef != null) {
                return envRef;
            }
            return referenceName(resolveClosureValue(name));
        }

        private Object referenceName(Object value) {
            if (value instanceof Map<?, ?> descriptorMap) {
                return descriptorMap.get("__qin_ref_name");
            }
            return null;
        }

        private Map<String, Object> referenceDescriptor(Object refName) {
            LinkedHashMap<String, Object> descriptor = new LinkedHashMap<>();
            descriptor.put("__qin_ref_name", refName);
            return descriptor;
        }

        private boolean shouldWriteThroughTopLevelBinding(String name) {
            if (!isSyntheticTopLevelControlFunction()) {
                return false;
            }
            return GLOBAL_BINDINGS.containsKey(name) || GLOBAL_OBJECT.containsKey(name);
        }

        private boolean isSyntheticTopLevelControlFunction() {
            Object debugNode = definition.get("debugNode");
            return debugNode instanceof String text && text.startsWith("TopLevel");
        }

        private Object throwThrownValue(Map<String, Object> astNode, Map<String, Object> env) {
            throw new ThrownValue(evalNode(astNode.get("argument"), env));
        }

        private void addSpreadValues(List<Object> target, Object spreadValue) {
            if (spreadValue == null) {
                return;
            }
            if (spreadValue instanceof Collection<?> collection) {
                target.addAll(collection);
                return;
            }
            if (spreadValue instanceof JavaEsmSetObject setObject) {
                target.addAll(setObject.values());
                return;
            }
            if (spreadValue.getClass().isArray()) {
                int length = Array.getLength(spreadValue);
                for (int i = 0; i < length; i++) {
                    target.add(Array.get(spreadValue, i));
                }
                return;
            }
            throw new IllegalArgumentException(
                    "Spread element expects an array-like value; got=" + summarizeRuntimeValue(spreadValue));
        }

        private Object resolveIdentifier(String name, Map<String, Object> env) {
            Object lexicalValue = resolveRawLexicalValue(name, env);
            if (lexicalValue != UNRESOLVED_MODULE_REF) {
                Object value = lexicalValue;
                if (value instanceof Map<?, ?> descriptorMap) {
                    Object refName = descriptorMap.get("__qin_ref_name");
                    if (refName != null) {
                        return resolveRuntimeReference(refName, value);
                    }
                }
                return unwrapRuntimeReferenceValue(value);
            }
            Object global = __qin_global__(name);
            if (global != null || "undefined".equals(name)) {
                return unwrapRuntimeReferenceValue(global);
            }
            return null;
        }

        private Object resolveClosureValue(String name) {
            Object value = resolveRawLexicalValue(name, closure);
            if (value == UNRESOLVED_MODULE_REF) {
                return null;
            }
            if (value instanceof Map<?, ?> descriptorMap) {
                Object refName = descriptorMap.get("__qin_ref_name");
                if (refName != null) {
                    return resolveRuntimeReference(refName, value);
                }
            }
            return value;
        }

        @SuppressWarnings("unchecked")
        private Object resolveRawLexicalValue(String name, Map<String, Object> env) {
            Map<String, Object> current = env;
            int depth = 0;
            while (current != null && depth++ < MAX_LEXICAL_ENV_CHAIN_DEPTH) {
                Object value = current.get(name);
                if (value != null || current.containsKey(name)) {
                    return value;
                }
                Object parent = current.get(PARENT_CLOSURE_KEY);
                current = parent instanceof Map<?, ?> rawParent ? (Map<String, Object>) rawParent : null;
            }
            return UNRESOLVED_MODULE_REF;
        }

        @SuppressWarnings("unchecked")
        private boolean assignOuterLexicalBinding(String name, Object value, Map<String, Object> env) {
            Object parent = env.get(PARENT_CLOSURE_KEY);
            Map<String, Object> current = parent instanceof Map<?, ?> rawParent ? (Map<String, Object>) rawParent : closure;
            int depth = 0;
            while (current != null && depth++ < MAX_LEXICAL_ENV_CHAIN_DEPTH) {
                if (current.containsKey(name) && !LOCAL_BINDINGS_KEY.equals(name) && !PARENT_CLOSURE_KEY.equals(name)) {
                    current.put(name, value);
                    return true;
                }
                parent = current.get(PARENT_CLOSURE_KEY);
                current = parent instanceof Map<?, ?> rawParent ? (Map<String, Object>) rawParent : null;
            }
            return false;
        }

        private Object resolveRuntimeReference(Object refName, Object fallback) {
            if (fallback instanceof Map<?, ?> descriptorMap) {
                Object scopedRef = descriptorMap.get("__qin_ref");
                if (scopedRef instanceof ModuleFieldRef moduleFieldRef) {
                    return unwrapRuntimeReferenceValue(moduleFieldRef.get());
                }
                ModuleFieldRef serializedRef = moduleFieldRefFromDescriptor(descriptorMap);
                if (serializedRef != null) {
                    return unwrapRuntimeReferenceValue(serializedRef.get());
                }
            }
            Object moduleRef = resolveModuleReference(refName);
            if (moduleRef != UNRESOLVED_MODULE_REF) {
                return unwrapRuntimeReferenceValue(moduleRef);
            }
            Object global = __qin_global__(refName);
            return global == null ? fallback : unwrapRuntimeReferenceValue(global);
        }

        private Object unwrapRuntimeReferenceValue(Object value) {
            if (value instanceof ExportSlot) {
                return __qin_export_get__(value);
            }
            return value;
        }

        private String extractPropertyName(Object propertyNode) {
            if (propertyNode instanceof Map<?, ?> rawMap) {
                Map<String, Object> map = castMap(rawMap);
                String type = String.valueOf(map.get("type"));
                if ("Identifier".equals(type)) {
                    return String.valueOf(map.get("name"));
                }
                if ("Literal".equals(type)) {
                    return String.valueOf(map.get("value"));
                }
            }
            return String.valueOf(propertyNode);
        }

        private Object literalValue(Map<String, Object> astNode) {
            Object value = astNode.get("value");
            if (value instanceof String text && looksQuotedStringLiteral(text)) {
                return decodeQuotedStringLiteral(text);
            }
            Object raw = astNode.get("raw");
            if (astNode.containsKey("regex") && raw instanceof String rawText) {
                JavaEsmRegExp regexp = regexLiteral(rawText);
                if (regexp != null) {
                    return regexp;
                }
            }
            if (raw instanceof String rawText && looksQuotedStringLiteral(rawText)) {
                return decodeQuotedStringLiteral(rawText);
            }
            return value;
        }

        private JavaEsmRegExp regexLiteral(String rawText) {
            if (rawText == null || rawText.length() < 2 || rawText.charAt(0) != '/') {
                return null;
            }
            int endSlash = rawText.lastIndexOf('/');
            if (endSlash <= 0) {
                return null;
            }
            String flags = rawText.substring(endSlash + 1);
            if (!flags.chars().allMatch(Character::isLetter)) {
                return null;
            }
            return new JavaEsmRegExp(rawText.substring(1, endSlash), flags);
        }

        private boolean looksQuotedStringLiteral(String text) {
            return text.length() >= 2
                    && ((text.charAt(0) == '"' && text.charAt(text.length() - 1) == '"')
                    || (text.charAt(0) == '\'' && text.charAt(text.length() - 1) == '\''));
        }

        private String decodeQuotedStringLiteral(String literalText) {
            StringBuilder builder = new StringBuilder(Math.max(0, literalText.length() - 2));
            char quote = literalText.charAt(0);
            for (int i = 1; i < literalText.length() - 1; i++) {
                char ch = literalText.charAt(i);
                if (ch != '\\') {
                    builder.append(ch);
                    continue;
                }
                if (i + 1 >= literalText.length() - 1) {
                    builder.append('\\');
                    break;
                }
                char escaped = literalText.charAt(++i);
                switch (escaped) {
                    case '\\' -> builder.append('\\');
                    case '\'' -> builder.append('\'');
                    case '"' -> builder.append('"');
                    case 'n' -> builder.append('\n');
                    case 'r' -> builder.append('\r');
                    case 't' -> builder.append('\t');
                    case 'b' -> builder.append('\b');
                    case 'f' -> builder.append('\f');
                    case '/' -> builder.append('/');
                    case 'u' -> {
                        if (i + 4 < literalText.length() - 1) {
                            String hex = literalText.substring(i + 1, i + 5);
                            try {
                                builder.append((char) Integer.parseInt(hex, 16));
                                i += 4;
                            } catch (NumberFormatException error) {
                                builder.append("\\u").append(hex);
                                i += 4;
                            }
                        } else {
                            builder.append("\\u");
                        }
                    }
                    default -> builder.append(escaped);
                }
            }
            if (quote == '\'' || quote == '"') {
                return builder.toString();
            }
            return literalText;
        }

        private static Map<?, ?> asMap(Object value) {
            if (value instanceof Map<?, ?> map) {
                return map;
            }
            throw new IllegalArgumentException("Expected map node, got: " + simpleName(value));
        }

        private static List<?> asList(Object value) {
            if (value instanceof List<?> list) {
                return list;
            }
            throw new IllegalArgumentException("Expected list node, got: " + simpleName(value));
        }
    }

    /**
     * ConcurrentHashMap rejects null values, but JS globals may legitimately be
     * bound to undefined/null during module initialization.
     */
    private static Object resolveModuleReference(Object name) {
        List<ModuleFieldRef> refs = MODULE_REFS.get(String.valueOf(name));
        if (refs == null || refs.isEmpty()) {
            return UNRESOLVED_MODULE_REF;
        }
        synchronized (refs) {
            for (ModuleFieldRef ref : refs) {
                if (ref.initialized()) {
                    return ref.get();
                }
            }
        }
        return UNRESOLVED_MODULE_REF;
    }

    private static boolean assignModuleReference(String name, Object value) {
        List<ModuleFieldRef> refs = MODULE_REFS.get(String.valueOf(name));
        if (refs == null || refs.isEmpty()) {
            return false;
        }
        boolean assigned = false;
        synchronized (refs) {
            for (ModuleFieldRef ref : refs) {
                if (ref.initialized()) {
                    ref.set(value);
                    assigned = true;
                }
            }
        }
        return assigned;
    }

    private static final class ModuleFieldRef {
        private final Field field;
        private volatile boolean initialized;

        private ModuleFieldRef(Class<?> ownerClass, String fieldName) {
            try {
                this.field = ownerClass.getDeclaredField(fieldName);
                this.field.setAccessible(true);
            } catch (NoSuchFieldException error) {
                throw new IllegalArgumentException(
                        "Unknown Qin module field: " + ownerClass.getName() + "." + fieldName,
                        error);
            }
        }

        private Class<?> ownerClass() {
            return field.getDeclaringClass();
        }

        private String fieldName() {
            return field.getName();
        }

        private boolean initialized() {
            return initialized;
        }

        private void markInitialized() {
            initialized = true;
        }

        private Object get() {
            return readField();
        }

        private Object readField() {
            try {
                return field.get(null);
            } catch (IllegalAccessException error) {
                throw new IllegalStateException(
                        "Cannot read Qin module field: " + field.getDeclaringClass().getName() + "." + field.getName(),
                        error);
            }
        }

        private void set(Object value) {
            try {
                field.set(null, value);
                initialized = true;
            } catch (IllegalAccessException error) {
                throw new IllegalStateException(
                        "Cannot write Qin module field: " + field.getDeclaringClass().getName() + "." + field.getName(),
                        error);
            }
        }
    }

    private static ModuleFieldRef moduleFieldRefFromDescriptor(Map<?, ?> descriptorMap) {
        Object ownerName = descriptorMap.get("__qin_ref_owner");
        Object fieldName = descriptorMap.get("__qin_ref_field");
        if (ownerName == null || fieldName == null) {
            return null;
        }
        String owner = String.valueOf(ownerName);
        String field = String.valueOf(fieldName);
        List<ModuleFieldRef> refs = MODULE_REFS_BY_SERIALIZED_FIELD.get(new SerializedModuleFieldKey(owner, field));
        if (refs != null) {
            synchronized (refs) {
                if (!refs.isEmpty()) {
                    return refs.get(refs.size() - 1);
                }
            }
        }
        try {
            ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
            ClassLoader loader = contextLoader == null ? JavaEsmGlobal.class.getClassLoader() : contextLoader;
            Class<?> ownerClass = Class.forName(owner, false, loader);
            return new ModuleFieldRef(ownerClass, field);
        } catch (ClassNotFoundException error) {
            throw new IllegalStateException("Unknown Qin module class in runtime reference: " + ownerName, error);
        }
    }

    private record ModuleFieldKey(Class<?> ownerClass, String fieldName) {
    }

    private record SerializedModuleFieldKey(String ownerClassName, String fieldName) {
    }

    private static final class NullFriendlyConcurrentMap extends AbstractMap<String, Object> {
        private static final Object NULL_SENTINEL = new Object();

        private final ConcurrentHashMap<String, Object> delegate = new ConcurrentHashMap<>();

        @Override
        public Object put(String key, Object value) {
            Object previous = delegate.put(key, encode(value));
            return decode(previous);
        }

        @Override
        public Object get(Object key) {
            return decode(delegate.get(key));
        }

        @Override
        public boolean containsKey(Object key) {
            return delegate.containsKey(key);
        }

        @Override
        public Object remove(Object key) {
            return decode(delegate.remove(key));
        }

        @Override
        public void clear() {
            delegate.clear();
        }

        @Override
        public int size() {
            return delegate.size();
        }

        @Override
        public boolean isEmpty() {
            return delegate.isEmpty();
        }

        @Override
        public Collection<Object> values() {
            List<Object> values = new ArrayList<>(delegate.size());
            for (Object value : delegate.values()) {
                values.add(decode(value));
            }
            return Collections.unmodifiableList(values);
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            return Collections.unmodifiableSet(new AbstractSet<>() {
                @Override
                public Iterator<Entry<String, Object>> iterator() {
                    Iterator<Entry<String, Object>> iterator = delegate.entrySet().iterator();
                    return new Iterator<>() {
                        @Override
                        public boolean hasNext() {
                            return iterator.hasNext();
                        }

                        @Override
                        public Entry<String, Object> next() {
                            Entry<String, Object> entry = iterator.next();
                            return new AbstractMap.SimpleEntry<>(entry.getKey(), decode(entry.getValue()));
                        }
                    };
                }

                @Override
                public int size() {
                    return delegate.size();
                }
            });
        }

        private static Object encode(Object value) {
            return value == null ? NULL_SENTINEL : value;
        }

        private static Object decode(Object value) {
            return value == NULL_SENTINEL ? null : value;
        }
    }
}
