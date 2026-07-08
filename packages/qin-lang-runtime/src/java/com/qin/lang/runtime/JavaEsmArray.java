package com.qin.lang.runtime;

import java.util.ArrayList;
import java.util.List;

/**
 * Phase-1 Array builtin helpers for Qin's Java-backed ESM runtime.
 */
public final class JavaEsmArray {
    private JavaEsmArray() {
    }

    static Object memberGet(List<?> list, Object property) {
        String name = String.valueOf(property);
        if ("length".equals(name)) {
            return list.size();
        }
        int index = toIndex(property);
        return index >= 0 && index < list.size() ? list.get(index) : null;
    }

    static Object memberSet(List<Object> list, Object property, Object value) {
        if ("length".equals(String.valueOf(property))) {
            int newLength = Math.max(0, toIndex(value));
            while (list.size() > newLength) {
                list.remove(list.size() - 1);
            }
            while (list.size() < newLength) {
                list.add(null);
            }
            return value;
        }
        int index = toIndex(property);
        while (mutableIndex(index) && list.size() <= index) {
            list.add(null);
        }
        if (mutableIndex(index)) {
            list.set(index, value);
        }
        return value;
    }

    static boolean supports(String methodName) {
        return switch (methodName) {
            case "push", "pop", "unshift", "shift", "concat", "map", "forEach", "at", "filter", "fill", "join", "slice", "splice",
                    "includes", "indexOf", "find", "findIndex", "some", "every",
                    "reduce", "flat", "flatMap", "sort",
                    "add", "addAll", "size", "isEmpty", "clear", "get", "set", "remove", "subList", "toArray" -> true;
            default -> false;
        };
    }

    static Object invoke(List<?> rawList, String methodName, Object[] args) {
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) rawList;
        return switch (methodName) {
            case "push" -> push(list, args);
            case "pop" -> pop(list, args);
            case "unshift" -> unshift(list, args);
            case "shift" -> shift(list, args);
            case "concat" -> concat(list, args);
            case "map" -> map(list, args);
            case "forEach" -> forEach(list, args);
            case "at" -> at(list, args);
            case "filter" -> filter(list, args);
            case "fill" -> fill(list, args);
            case "join" -> join(list, args);
            case "slice" -> slice(list, args);
            case "splice" -> splice(list, args);
            case "includes" -> includes(list, args);
            case "indexOf" -> indexOf(list, args);
            case "find" -> find(list, args);
            case "findIndex" -> findIndex(list, args);
            case "some" -> some(list, args);
            case "every" -> every(list, args);
            case "reduce" -> reduce(list, args);
            case "flat" -> flat(list, args);
            case "flatMap" -> flatMap(list, args);
            case "sort" -> sort(list, args);
            case "add" -> javaListAdd(list, args);
            case "addAll" -> javaListAddAll(list, args);
            case "size" -> javaListSize(list, args);
            case "isEmpty" -> javaListIsEmpty(list, args);
            case "clear" -> javaListClear(list, args);
            case "get" -> javaListGet(list, args);
            case "set" -> javaListSet(list, args);
            case "remove" -> javaListRemove(list, args);
            case "subList" -> javaListSubList(list, args);
            case "toArray" -> javaListToArray(list, args);
            default -> throw new IllegalArgumentException("Unsupported Array builtin: " + methodName);
        };
    }

    public static Object from(Object value) {
        List<Object> result = new ArrayList<>();
        if (value == null) {
            return result;
        }
        if (value instanceof List<?> list) {
            result.addAll(list);
            return result;
        }
        if (value instanceof JavaEsmSetObject setObject) {
            result.addAll(setObject.values());
            return result;
        }
        if (value instanceof JavaEsmMapObject mapObject) {
            result.addAll(mapObject.entries());
            return result;
        }
        if (value instanceof java.util.Map<?, ?> map && map.get("length") instanceof Number length) {
            for (int i = 0; i < Math.max(0, length.intValue()); i++) {
                result.add(null);
            }
            return result;
        }
        if (value instanceof Iterable<?> iterable) {
            for (Object item : iterable) {
                result.add(item);
            }
            return result;
        }
        try {
            for (Object item : JavaEsmGlobal.asIterableForOf(value)) {
                result.add(item);
            }
            return result;
        } catch (IllegalArgumentException ignored) {
            // Fall through to array-like/string handling below.
        }
        if (value.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(value);
            for (int i = 0; i < length; i++) {
                result.add(java.lang.reflect.Array.get(value, i));
            }
            return result;
        }
        if (value instanceof CharSequence text) {
            for (int i = 0; i < text.length(); i++) {
                result.add(String.valueOf(text.charAt(i)));
            }
        }
        return result;
    }

    public static Object from(Object value, Object mapFn) {
        if (!JavaEsmGlobal.isRuntimeCallable(mapFn)) {
            throw new IllegalArgumentException("Array.from expects a callable map function");
        }
        List<Object> source = runtimeListValues(value);
        List<Object> result = new ArrayList<>(source.size());
        for (int i = 0; i < source.size(); i++) {
            result.add(JavaEsmGlobal.callRuntimeCallable(mapFn, source.get(i), (double) i));
        }
        return result;
    }

    public static boolean isArray(Object value) {
        return value instanceof List<?> || (value != null && value.getClass().isArray());
    }

    private static Object push(List<Object> list, Object[] args) {
        for (Object arg : args) {
            list.add(arg);
        }
        return list.size();
    }

    private static Object pop(List<Object> list, Object[] args) {
        requireArgCount("Array.pop", args, 0);
        return list.isEmpty() ? null : list.remove(list.size() - 1);
    }

    private static Object unshift(List<Object> list, Object[] args) {
        for (int i = args.length - 1; i >= 0; i--) {
            list.add(0, args[i]);
        }
        return list.size();
    }

    private static Object shift(List<Object> list, Object[] args) {
        requireArgCount("Array.shift", args, 0);
        return list.isEmpty() ? null : list.remove(0);
    }

    private static Object concat(List<Object> list, Object[] args) {
        List<Object> result = new ArrayList<>(list);
        for (Object arg : args) {
            if (arg instanceof List<?> argList) {
                result.addAll(argList);
                continue;
            }
            if (arg != null && arg.getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(arg);
                for (int i = 0; i < length; i++) {
                    result.add(java.lang.reflect.Array.get(arg, i));
                }
                continue;
            }
            result.add(arg);
        }
        return result;
    }

    private static Object map(List<Object> list, Object[] args) {
        Object callback = requireCallback("Array.map", args);
        callback = bindCallbackThis(callback, args);
        List<Object> mapped = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            mapped.add(JavaEsmGlobal.callRuntimeCallable(callback, list.get(i), (double) i, list));
        }
        return mapped;
    }

    private static Object forEach(List<Object> list, Object[] args) {
        Object callback = requireCallback("Array.forEach", args);
        callback = bindCallbackThis(callback, args);
        for (int i = 0; i < list.size(); i++) {
            JavaEsmGlobal.callRuntimeCallable(callback, list.get(i), (double) i, list);
        }
        return null;
    }

    private static Object at(List<Object> list, Object[] args) {
        requireArgRange("Array.at", args, 1, 1);
        int index = toIndex(args[0]);
        if (index < 0) {
            index = list.size() + index;
        }
        return index >= 0 && index < list.size() ? list.get(index) : null;
    }

    private static Object slice(List<Object> list, Object[] args) {
        requireArgRange("Array.slice", args, 0, 2);
        int start = args.length >= 1 ? normalizeFromIndex(args[0], list.size()) : 0;
        int end = args.length >= 2 ? normalizeFromIndex(args[1], list.size()) : list.size();
        if (end < start) {
            end = start;
        }
        return new ArrayList<>(list.subList(start, end));
    }

    private static Object splice(List<Object> list, Object[] args) {
        requireArgRange("Array.splice", args, 0, Integer.MAX_VALUE);
        int start = args.length >= 1 ? normalizeFromIndex(args[0], list.size()) : 0;
        int deleteCount = args.length >= 2
                ? Math.max(0, Math.min(toIndex(args[1]), list.size() - start))
                : list.size() - start;
        List<Object> removed = new ArrayList<>();
        for (int i = 0; i < deleteCount; i++) {
            removed.add(list.remove(start));
        }
        for (int i = args.length - 1; i >= 2; i--) {
            list.add(start, args[i]);
        }
        return removed;
    }

    private static Object filter(List<Object> list, Object[] args) {
        Object callback = requireCallback("Array.filter", args);
        callback = bindCallbackThis(callback, args);
        List<Object> filtered = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object item = list.get(i);
            Object result = JavaEsmGlobal.callRuntimeCallable(callback, item, (double) i, list);
            if (JavaEsmGlobal.isRuntimeTruthy(result)) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    private static Object fill(List<Object> list, Object[] args) {
        requireArgRange("Array.fill", args, 1, 3);
        Object value = args[0];
        int start = args.length >= 2 ? normalizeFromIndex(args[1], list.size()) : 0;
        int end = args.length >= 3 ? normalizeFromIndex(args[2], list.size()) : list.size();
        for (int i = start; i < end; i++) {
            list.set(i, value);
        }
        return list;
    }

    private static Object join(List<Object> list, Object[] args) {
        requireArgRange("Array.join", args, 0, 1);
        String separator = args.length == 0 ? "," : String.valueOf(args[0]);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                builder.append(separator);
            }
            Object value = list.get(i);
            if (value != null) {
                builder.append(value);
            }
        }
        return builder.toString();
    }

    private static Object includes(List<Object> list, Object[] args) {
        requireArgRange("Array.includes", args, 1, 2);
        int fromIndex = args.length == 2 ? normalizeFromIndex(args[1], list.size()) : 0;
        for (int i = fromIndex; i < list.size(); i++) {
            if (JavaEsmGlobal.sameValueZero(list.get(i), args[0])) {
                return true;
            }
        }
        return false;
    }

    private static Object indexOf(List<Object> list, Object[] args) {
        requireArgRange("Array.indexOf", args, 1, 2);
        int fromIndex = args.length == 2 ? normalizeFromIndex(args[1], list.size()) : 0;
        for (int i = fromIndex; i < list.size(); i++) {
            if (JavaEsmGlobal.sameValueZero(list.get(i), args[0])) {
                return i;
            }
        }
        return -1;
    }

    private static Object find(List<Object> list, Object[] args) {
        Object callback = requireCallback("Array.find", args);
        callback = bindCallbackThis(callback, args);
        for (int i = 0; i < list.size(); i++) {
            Object item = list.get(i);
            Object result = JavaEsmGlobal.callRuntimeCallable(callback, item, (double) i, list);
            if (JavaEsmGlobal.isRuntimeTruthy(result)) {
                return item;
            }
        }
        return null;
    }

    private static Object findIndex(List<Object> list, Object[] args) {
        Object callback = requireCallback("Array.findIndex", args);
        callback = bindCallbackThis(callback, args);
        for (int i = 0; i < list.size(); i++) {
            Object result = JavaEsmGlobal.callRuntimeCallable(callback, list.get(i), (double) i, list);
            if (JavaEsmGlobal.isRuntimeTruthy(result)) {
                return i;
            }
        }
        return -1;
    }

    private static Object some(List<Object> list, Object[] args) {
        Object callback = requireCallback("Array.some", args);
        callback = bindCallbackThis(callback, args);
        for (int i = 0; i < list.size(); i++) {
            Object result = JavaEsmGlobal.callRuntimeCallable(callback, list.get(i), (double) i, list);
            if (JavaEsmGlobal.isRuntimeTruthy(result)) {
                return true;
            }
        }
        return false;
    }

    private static Object every(List<Object> list, Object[] args) {
        Object callback = requireCallback("Array.every", args);
        callback = bindCallbackThis(callback, args);
        for (int i = 0; i < list.size(); i++) {
            Object result = JavaEsmGlobal.callRuntimeCallable(callback, list.get(i), (double) i, list);
            if (!JavaEsmGlobal.isRuntimeTruthy(result)) {
                return false;
            }
        }
        return true;
    }

    private static Object reduce(List<Object> list, Object[] args) {
        requireArgRange("Array.reduce", args, 1, 2);
        Object callback = requireCallback("Array.reduce", args);
        if (list.isEmpty() && args.length < 2) {
            throw new IllegalArgumentException("Array.reduce of empty array with no initial value");
        }
        Object accumulator;
        int startIndex;
        if (args.length >= 2) {
            accumulator = args[1];
            startIndex = 0;
        } else {
            accumulator = list.get(0);
            startIndex = 1;
        }
        for (int i = startIndex; i < list.size(); i++) {
            accumulator = JavaEsmGlobal.callRuntimeCallable(
                    callback,
                    accumulator,
                    list.get(i),
                    (double) i,
                    list);
        }
        return accumulator;
    }

    private static Object flatMap(List<Object> list, Object[] args) {
        Object callback = requireCallback("Array.flatMap", args);
        callback = bindCallbackThis(callback, args);
        List<Object> mapped = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object result = JavaEsmGlobal.callRuntimeCallable(callback, list.get(i), (double) i, list);
            appendFlatMapResult(mapped, result);
        }
        return mapped;
    }

    private static Object flat(List<Object> list, Object[] args) {
        requireArgRange("Array.flat", args, 0, 1);
        int depth = args.length == 0 ? 1 : toFlatDepth(args[0]);
        List<Object> flattened = new ArrayList<>();
        for (Object item : list) {
            appendFlat(flattened, item, depth);
        }
        return flattened;
    }

    private static void appendFlat(List<Object> target, Object value, int depth) {
        if (depth > 0 && value instanceof List<?> list) {
            for (Object item : list) {
                appendFlat(target, item, depth - 1);
            }
            return;
        }
        if (depth > 0 && value != null && value.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(value);
            for (int i = 0; i < length; i++) {
                appendFlat(target, java.lang.reflect.Array.get(value, i), depth - 1);
            }
            return;
        }
        target.add(value);
    }

    private static int toFlatDepth(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number number) {
            double doubleValue = number.doubleValue();
            if (Double.isInfinite(doubleValue)) {
                return Integer.MAX_VALUE;
            }
            return Math.max(0, (int) doubleValue);
        }
        if (value instanceof String text) {
            try {
                double doubleValue = Double.parseDouble(text.trim());
                if (Double.isInfinite(doubleValue)) {
                    return Integer.MAX_VALUE;
                }
                return Math.max(0, (int) doubleValue);
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
        return 0;
    }

    private static void appendFlatMapResult(List<Object> target, Object value) {
        if (value instanceof List<?> list) {
            target.addAll(list);
            return;
        }
        if (value instanceof JavaEsmSetObject setObject) {
            target.addAll(setObject.values());
            return;
        }
        if (value != null && value.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(value);
            for (int i = 0; i < length; i++) {
                target.add(java.lang.reflect.Array.get(value, i));
            }
            return;
        }
        target.add(value);
    }

    private static Object sort(List<Object> list, Object[] args) {
        requireArgRange("Array.sort", args, 0, 1);
        if (args.length == 0 || args[0] == null) {
            list.sort((left, right) -> String.valueOf(left).compareTo(String.valueOf(right)));
            return list;
        }
        Object callback = requireCallback("Array.sort", args);
        list.sort((left, right) -> {
            Object result = JavaEsmGlobal.callRuntimeCallable(callback, left, right);
            if (result instanceof Number number) {
                return Double.compare(number.doubleValue(), 0.0d);
            }
            if (result instanceof String text) {
                try {
                    return Double.compare(Double.parseDouble(text.trim()), 0.0d);
                } catch (NumberFormatException ignored) {
                    return 0;
                }
            }
            return JavaEsmGlobal.isRuntimeTruthy(result) ? 1 : 0;
        });
        return list;
    }

    private static Object javaListAdd(List<Object> list, Object[] args) {
        requireArgRange("List.add", args, 1, 2);
        if (args.length == 1) {
            return list.add(args[0]);
        }
        list.add(normalizeInsertIndex(args[0], list.size()), args[1]);
        return null;
    }

    private static Object javaListAddAll(List<Object> list, Object[] args) {
        requireArgRange("List.addAll", args, 1, 2);
        List<Object> values = runtimeListValues(args.length == 1 ? args[0] : args[1]);
        if (args.length == 1) {
            return list.addAll(values);
        }
        return list.addAll(normalizeInsertIndex(args[0], list.size()), values);
    }

    private static Object javaListSize(List<Object> list, Object[] args) {
        requireArgCount("List.size", args, 0);
        return list.size();
    }

    private static Object javaListIsEmpty(List<Object> list, Object[] args) {
        requireArgCount("List.isEmpty", args, 0);
        return list.isEmpty();
    }

    private static Object javaListClear(List<Object> list, Object[] args) {
        requireArgCount("List.clear", args, 0);
        list.clear();
        return null;
    }

    private static Object javaListGet(List<Object> list, Object[] args) {
        requireArgCount("List.get", args, 1);
        return list.get(normalizeExistingIndex(args[0], list.size()));
    }

    private static Object javaListSet(List<Object> list, Object[] args) {
        requireArgCount("List.set", args, 2);
        return list.set(normalizeExistingIndex(args[0], list.size()), args[1]);
    }

    private static Object javaListRemove(List<Object> list, Object[] args) {
        requireArgCount("List.remove", args, 1);
        if (args[0] instanceof Number || args[0] instanceof String) {
            return list.remove(normalizeExistingIndex(args[0], list.size()));
        }
        return list.remove(args[0]);
    }

    private static Object javaListSubList(List<Object> list, Object[] args) {
        requireArgCount("List.subList", args, 2);
        int from = normalizeRangeIndex(args[0], list.size());
        int to = normalizeRangeIndex(args[1], list.size());
        if (to < from) {
            to = from;
        }
        return new ArrayList<>(list.subList(from, to));
    }

    private static Object javaListToArray(List<Object> list, Object[] args) {
        requireArgRange("List.toArray", args, 0, 1);
        return new ArrayList<>(list);
    }

    private static List<Object> runtimeListValues(Object value) {
        Object arrayLike = JavaEsmArray.from(value);
        if (arrayLike instanceof List<?> values) {
            return new ArrayList<>(values);
        }
        throw new IllegalArgumentException("List.addAll expects iterable/list value; got=" + value);
    }

    private static Object requireCallback(String methodName, Object[] args) {
        requireArgRange(methodName, args, 1, 2);
        Object callback = args[0];
        if (!JavaEsmGlobal.isRuntimeCallable(callback)) {
            throw new IllegalArgumentException(methodName + " expects a callable callback");
        }
        return callback;
    }

    private static Object bindCallbackThis(Object callback, Object[] args) {
        return args.length >= 2 ? JavaEsmGlobal.bindRuntimeCallableThis(callback, args[1]) : callback;
    }

    private static int normalizeFromIndex(Object value, int size) {
        int index = toIndex(value);
        if (index < 0) {
            index = Math.max(size + index, 0);
        }
        return Math.min(index, size);
    }

    private static int normalizeInsertIndex(Object value, int size) {
        int index = toIndex(value);
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return index;
    }

    private static int normalizeExistingIndex(Object value, int size) {
        int index = toIndex(value);
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return index;
    }

    private static int normalizeRangeIndex(Object value, int size) {
        int index = toIndex(value);
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return index;
    }

    private static int toIndex(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String text) {
            try {
                return (int) Double.parseDouble(text.trim());
            } catch (NumberFormatException ignored) {
                return -1;
            }
        }
        return -1;
    }

    private static boolean mutableIndex(int index) {
        return index >= 0;
    }

    private static void requireArgCount(String methodName, Object[] args, int expected) {
        requireArgRange(methodName, args, expected, expected);
    }

    private static void requireArgRange(String methodName, Object[] args, int min, int max) {
        if (args.length < min || args.length > max) {
            if (min == max) {
                throw new IllegalArgumentException(methodName + " expects exactly " + min + " argument(s)");
            }
            throw new IllegalArgumentException(
                    methodName + " expects between " + min + " and " + max + " argument(s)");
        }
    }
}
