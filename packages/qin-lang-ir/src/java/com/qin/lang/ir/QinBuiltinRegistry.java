package com.qin.lang.ir;

import java.lang.constant.MethodTypeDesc;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Registry of Qin global built-ins and their JVM targets.
 */
public final class QinBuiltinRegistry {
    private static final Map<String, BuiltinMethod> METHODS = Map.ofEntries(
            Map.entry(key("console", "log", 1),
                    new BuiltinMethod(
                            "console",
                            "log",
                            "com.qin.lang.runtime.JavaEsmConsole",
                            "log",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)V"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Math", "random", 0),
                    new BuiltinMethod(
                            "Math",
                            "random",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "random",
                            MethodTypeDesc.ofDescriptor("()Ljava/lang/Object;"),
                            List.of())),
            Map.entry(key("Math", "abs", 1),
                    new BuiltinMethod(
                            "Math",
                            "abs",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "abs",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Math", "floor", 1),
                    new BuiltinMethod(
                            "Math",
                            "floor",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "floor",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Math", "ceil", 1),
                    new BuiltinMethod(
                            "Math",
                            "ceil",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "ceil",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Math", "max", 2),
                    new BuiltinMethod(
                            "Math",
                            "max",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "max",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Math", "min", 2),
                    new BuiltinMethod(
                            "Math",
                            "min",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "min",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Math", "round", 1),
                    new BuiltinMethod(
                            "Math",
                            "round",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "round",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Math", "trunc", 1),
                    new BuiltinMethod(
                            "Math",
                            "trunc",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "trunc",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Math", "pow", 2),
                    new BuiltinMethod(
                            "Math",
                            "pow",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "pow",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Math", "sqrt", 1),
                    new BuiltinMethod(
                            "Math",
                            "sqrt",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "sqrt",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Math", "sin", 1),
                    new BuiltinMethod(
                            "Math",
                            "sin",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "sin",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Math", "cos", 1),
                    new BuiltinMethod(
                            "Math",
                            "cos",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "cos",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Math", "tan", 1),
                    new BuiltinMethod(
                            "Math",
                            "tan",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "tan",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Math", "log", 1),
                    new BuiltinMethod(
                            "Math",
                            "log",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "log",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Math", "exp", 1),
                    new BuiltinMethod(
                            "Math",
                            "exp",
                            "com.qin.lang.runtime.JavaEsmMath",
                            "exp",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("JSON", "stringify", 1),
                    new BuiltinMethod(
                            "JSON",
                            "stringify",
                            "com.qin.lang.runtime.JavaEsmJson",
                            "stringify",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/String;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("JSON", "parse", 1),
                    new BuiltinMethod(
                            "JSON",
                            "parse",
                            "com.qin.lang.runtime.JavaEsmJson",
                            "parse",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.STRING))),
            Map.entry(key("Number", "parseInt", 1),
                    new BuiltinMethod(
                            "Number",
                            "parseInt",
                            "com.qin.lang.runtime.JavaEsmNumber",
                            "parseInt",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Number", "parseInt", 2),
                    new BuiltinMethod(
                            "Number",
                            "parseInt",
                            "com.qin.lang.runtime.JavaEsmNumber",
                            "parseInt",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Number", "parseFloat", 1),
                    new BuiltinMethod(
                            "Number",
                            "parseFloat",
                            "com.qin.lang.runtime.JavaEsmNumber",
                            "parseFloat",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Number", "isNaN", 1),
                    new BuiltinMethod(
                            "Number",
                            "isNaN",
                            "com.qin.lang.runtime.JavaEsmNumber",
                            "isNaN",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Number", "isFinite", 1),
                    new BuiltinMethod(
                            "Number",
                            "isFinite",
                            "com.qin.lang.runtime.JavaEsmNumber",
                            "isFinite",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Number", "isInteger", 1),
                    new BuiltinMethod(
                            "Number",
                            "isInteger",
                            "com.qin.lang.runtime.JavaEsmNumber",
                            "isInteger",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Number", "isSafeInteger", 1),
                    new BuiltinMethod(
                            "Number",
                            "isSafeInteger",
                            "com.qin.lang.runtime.JavaEsmNumber",
                            "isSafeInteger",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Object", "keys", 1),
                    new BuiltinMethod(
                            "Object",
                            "keys",
                            "com.qin.lang.runtime.JavaEsmObject",
                            "keys",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Object", "values", 1),
                    new BuiltinMethod(
                            "Object",
                            "values",
                            "com.qin.lang.runtime.JavaEsmObject",
                            "values",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Object", "entries", 1),
                    new BuiltinMethod(
                            "Object",
                            "entries",
                            "com.qin.lang.runtime.JavaEsmObject",
                            "entries",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Object", "hasOwn", 2),
                    new BuiltinMethod(
                            "Object",
                            "hasOwn",
                            "com.qin.lang.runtime.JavaEsmObject",
                            "hasOwn",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Object", "assign", 2),
                    new BuiltinMethod(
                            "Object",
                            "assign",
                            "com.qin.lang.runtime.JavaEsmObject",
                            "assign",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Object", "fromEntry", 2),
                    new BuiltinMethod(
                            "Object",
                            "fromEntry",
                            "com.qin.lang.runtime.JavaEsmObject",
                            "fromEntry",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Date", "now", 0),
                    new BuiltinMethod(
                            "Date",
                            "now",
                            "com.qin.lang.runtime.JavaEsmDate",
                            "now",
                            MethodTypeDesc.ofDescriptor("()Ljava/lang/Object;"),
                            List.of())),
            Map.entry(key("Global", "parseInt", 1),
                    new BuiltinMethod(
                            "Global",
                            "parseInt",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "parseInt",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Global", "parseInt", 2),
                    new BuiltinMethod(
                            "Global",
                            "parseInt",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "parseInt",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "parseFloat", 1),
                    new BuiltinMethod(
                            "Global",
                            "parseFloat",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "parseFloat",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Global", "isNaN", 1),
                    new BuiltinMethod(
                            "Global",
                            "isNaN",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "isNaN",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Global", "isFinite", 1),
                    new BuiltinMethod(
                            "Global",
                            "isFinite",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "isFinite",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_make_function__", 1),
                    new BuiltinMethod(
                            "Global",
                            "__qin_make_function__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_make_function__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_global__", 1),
                    new BuiltinMethod(
                            "Global",
                            "__qin_global__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_global__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_bind_global__", 2),
                    new BuiltinMethod(
                            "Global",
                            "__qin_bind_global__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_bind_global__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_declare_global__", 1),
                    new BuiltinMethod(
                            "Global",
                            "__qin_declare_global__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_declare_global__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_assign__", 2),
                    new BuiltinMethod(
                            "Global",
                            "__qin_assign__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_assign__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_export_slot__", 0),
                    new BuiltinMethod(
                            "Global",
                            "__qin_export_slot__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_export_slot__",
                            MethodTypeDesc.ofDescriptor("()Ljava/lang/Object;"),
                            List.of())),
            Map.entry(key("Global", "__qin_export_init__", 2),
                    new BuiltinMethod(
                            "Global",
                            "__qin_export_init__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_export_init__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_export_get__", 1),
                    new BuiltinMethod(
                            "Global",
                            "__qin_export_get__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_export_get__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_call__", 1),
                    new BuiltinMethod(
                            "Global",
                            "__qin_call__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_call__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_call__", 2),
                    new BuiltinMethod(
                            "Global",
                            "__qin_call__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_call__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_call__", 3),
                    new BuiltinMethod(
                            "Global",
                            "__qin_call__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_call__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_call__", 4),
                    new BuiltinMethod(
                            "Global",
                            "__qin_call__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_call__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_call__", 5),
                    new BuiltinMethod(
                            "Global",
                            "__qin_call__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_call__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_call_method__", 2),
                    new BuiltinMethod(
                            "Global",
                            "__qin_call_method__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_call_method__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_call_method__", 3),
                    new BuiltinMethod(
                            "Global",
                            "__qin_call_method__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_call_method__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_call_method__", 4),
                    new BuiltinMethod(
                            "Global",
                            "__qin_call_method__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_call_method__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_call_method__", 5),
                    new BuiltinMethod(
                            "Global",
                            "__qin_call_method__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_call_method__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_call_method__", 6),
                    new BuiltinMethod(
                            "Global",
                            "__qin_call_method__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_call_method__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_binary__", 3),
                    new BuiltinMethod(
                            "Global",
                            "__qin_binary__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_binary__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_logical__", 3),
                    new BuiltinMethod(
                            "Global",
                            "__qin_logical__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_logical__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_unary__", 2),
                    new BuiltinMethod(
                            "Global",
                            "__qin_unary__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_unary__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_conditional__", 3),
                    new BuiltinMethod(
                            "Global",
                            "__qin_conditional__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_conditional__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_member_get__", 2),
                    new BuiltinMethod(
                            "Global",
                            "__qin_member_get__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_member_get__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_member_set__", 3),
                    new BuiltinMethod(
                            "Global",
                            "__qin_member_set__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_member_set__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_delete_member__", 2),
                    new BuiltinMethod(
                            "Global",
                            "__qin_delete_member__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_delete_member__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_array_item__", 1),
                    new BuiltinMethod(
                            "Global",
                            "__qin_array_item__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_array_item__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_array_spread__", 1),
                    new BuiltinMethod(
                            "Global",
                            "__qin_array_spread__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_array_spread__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_new__", 1),
                    new BuiltinMethod(
                            "Global",
                            "__qin_new__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_new__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_new__", 2),
                    new BuiltinMethod(
                            "Global",
                            "__qin_new__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_new__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_new__", 3),
                    new BuiltinMethod(
                            "Global",
                            "__qin_new__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_new__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_new__", 4),
                    new BuiltinMethod(
                            "Global",
                            "__qin_new__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_new__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_new__", 5),
                    new BuiltinMethod(
                            "Global",
                            "__qin_new__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_new__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_new__", 6),
                    new BuiltinMethod(
                            "Global",
                            "__qin_new__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_new__",
                            MethodTypeDesc.ofDescriptor(
                                    "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY,
                                    BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_dynamic_import__", 1),
                    new BuiltinMethod(
                            "Global",
                            "__qin_dynamic_import__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_dynamic_import__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_dynamic_import__", 2),
                    new BuiltinMethod(
                            "Global",
                            "__qin_dynamic_import__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_dynamic_import__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY))),
            Map.entry(key("Global", "__qin_top_level_await__", 1),
                    new BuiltinMethod(
                            "Global",
                            "__qin_top_level_await__",
                            "com.qin.lang.runtime.JavaEsmGlobal",
                            "__qin_top_level_await__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"),
                            List.of(BuiltinArgKind.ANY))));

    private QinBuiltinRegistry() {
    }

    public static Optional<BuiltinMethod> resolve(String receiverName, String methodName, int argCount) {
        Objects.requireNonNull(receiverName, "receiverName cannot be null");
        Objects.requireNonNull(methodName, "methodName cannot be null");
        BuiltinMethod exact = METHODS.get(key(receiverName, methodName, argCount));
        if (exact != null) {
            return Optional.of(exact);
        }
        return resolveRestArgsBuiltin(receiverName, methodName, argCount);
    }

    private static Optional<BuiltinMethod> resolveRestArgsBuiltin(String receiverName, String methodName, int argCount) {
        if (!"Global".equals(receiverName)) {
            return Optional.empty();
        }
        if ("__qin_call__".equals(methodName) && argCount >= 1) {
            return Optional.of(new BuiltinMethod(
                    receiverName,
                    methodName,
                    "com.qin.lang.runtime.JavaEsmGlobal",
                    "__qin_call_array__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;"),
                    List.of(BuiltinArgKind.ANY, BuiltinArgKind.ARRAY_REST)));
        }
        if ("__qin_call_method__".equals(methodName) && argCount >= 2) {
            return Optional.of(new BuiltinMethod(
                    receiverName,
                    methodName,
                    "com.qin.lang.runtime.JavaEsmGlobal",
                    "__qin_call_method_array__",
                    MethodTypeDesc.ofDescriptor(
                            "(Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;"),
                    List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ARRAY_REST)));
        }
        if ("__qin_optional_call_method__".equals(methodName) && argCount >= 2) {
            return Optional.of(new BuiltinMethod(
                    receiverName,
                    methodName,
                    "com.qin.lang.runtime.JavaEsmGlobal",
                    "__qin_optional_call_method_array__",
                    MethodTypeDesc.ofDescriptor(
                            "(Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;"),
                    List.of(BuiltinArgKind.ANY, BuiltinArgKind.ANY, BuiltinArgKind.ARRAY_REST)));
        }
        if ("__qin_new__".equals(methodName) && argCount >= 1) {
            return Optional.of(new BuiltinMethod(
                    receiverName,
                    methodName,
                    "com.qin.lang.runtime.JavaEsmGlobal",
                    "__qin_new_array__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;"),
                    List.of(BuiltinArgKind.ANY, BuiltinArgKind.ARRAY_REST)));
        }
        if ("__qin_array_literal__".equals(methodName)) {
            return Optional.of(new BuiltinMethod(
                    receiverName,
                    methodName,
                    "com.qin.lang.runtime.JavaEsmGlobal",
                    "__qin_array_literal_array__",
                    MethodTypeDesc.ofDescriptor("([Ljava/lang/Object;)Ljava/lang/Object;"),
                    List.of(BuiltinArgKind.ARRAY_REST)));
        }
        return Optional.empty();
    }

    private static String key(String receiverName, String methodName, int argCount) {
        return receiverName + "#" + methodName + "#" + argCount;
    }

    public enum BuiltinArgKind {
        ANY,
        STRING,
        ARRAY_REST
    }

    public record BuiltinMethod(
            String receiverName,
            String methodName,
            String ownerBinaryName,
            String jvmMethodName,
            MethodTypeDesc descriptor,
            List<BuiltinArgKind> argumentKinds) {
        public BuiltinMethod {
            Objects.requireNonNull(receiverName, "receiverName cannot be null");
            Objects.requireNonNull(methodName, "methodName cannot be null");
            Objects.requireNonNull(ownerBinaryName, "ownerBinaryName cannot be null");
            Objects.requireNonNull(jvmMethodName, "jvmMethodName cannot be null");
            Objects.requireNonNull(descriptor, "descriptor cannot be null");
            Objects.requireNonNull(argumentKinds, "argumentKinds cannot be null");
            argumentKinds = List.copyOf(argumentKinds);
        }
    }
}
