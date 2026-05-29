package com.qin.lang.runtime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal JS TypedArray model used by npm packages that need indexed numeric
 * storage. Phase 1 keeps the runtime shape list-like while preserving fixed
 * length and unsigned integer coercion.
 */
public final class JavaEsmTypedArray extends ArrayList<Object> {
    private final int bits;
    private final Map<String, Object> properties = new LinkedHashMap<>();

    private JavaEsmTypedArray(int length, int bits) {
        super(Math.max(length, 0));
        this.bits = bits;
        for (int i = 0; i < length; i++) {
            super.add(0.0d);
        }
    }

    public static JavaEsmTypedArray uint8(Object length) {
        return new JavaEsmTypedArray(toLength(length), 8);
    }

    public static JavaEsmTypedArray uint16(Object length) {
        return new JavaEsmTypedArray(toLength(length), 16);
    }

    public static JavaEsmTypedArray uint32(Object length) {
        return new JavaEsmTypedArray(toLength(length), 32);
    }

    @Override
    public Object set(int index, Object element) {
        return super.set(index, coerce(element));
    }

    @Override
    public boolean add(Object element) {
        throw new UnsupportedOperationException("TypedArray length is fixed");
    }

    @Override
    public void add(int index, Object element) {
        throw new UnsupportedOperationException("TypedArray length is fixed");
    }

    public JavaEsmTypedArray subarray(Object start, Object end) {
        int from = normalizeSliceIndex(start, size(), 0);
        int to = end == null ? size() : normalizeSliceIndex(end, size(), size());
        int length = Math.max(0, to - from);
        JavaEsmTypedArray sliced = new JavaEsmTypedArray(length, bits);
        for (int i = 0; i < length; i++) {
            sliced.set(i, get(from + i));
        }
        return sliced;
    }

    public JavaEsmTypedArray subarray(Object start) {
        return subarray(start, null);
    }

    public JavaEsmTypedArray subarray() {
        return subarray(null, null);
    }

    Object memberGet(Object property) {
        String key = String.valueOf(property);
        if ("length".equals(key)) {
            return size();
        }
        Object ownValue = properties.get(key);
        if (ownValue != null || properties.containsKey(key)) {
            return ownValue;
        }
        Object prototype = properties.get("__proto__");
        if (prototype != null) {
            return JavaEsmGlobal.__qin_member_get__(prototype, property);
        }
        int index = toIndex(property);
        return index >= 0 && index < size() ? get(index) : null;
    }

    Object memberSet(Object property, Object value) {
        String key = String.valueOf(property);
        if ("__proto__".equals(key) || toIndex(property) < 0) {
            properties.put(key, value);
            return value;
        }
        int index = toIndex(property);
        if (index >= size()) {
            throw new IndexOutOfBoundsException("TypedArray index out of bounds: " + index);
        }
        set(index, value);
        return value;
    }

    private Object coerce(Object value) {
        long number = value instanceof Number numeric ? numeric.longValue() : 0L;
        long mask = switch (bits) {
            case 8 -> 0xffL;
            case 16 -> 0xffffL;
            case 32 -> 0xffffffffL;
            default -> 0L;
        };
        return (double) (number & mask);
    }

    private static int toIndex(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String text) {
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException ignored) {
                try {
                    double number = Double.parseDouble(text);
                    return number == Math.rint(number) ? (int) number : -1;
                } catch (NumberFormatException ignoredAgain) {
                    return -1;
                }
            }
        }
        return -1;
    }

    private static int toLength(Object length) {
        if (length instanceof Number number) {
            return Math.max(0, number.intValue());
        }
        if (length instanceof String text) {
            try {
                return Math.max(0, (int) Double.parseDouble(text.trim()));
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
        return 0;
    }

    private static int normalizeSliceIndex(Object value, int length, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        int index = toInteger(value);
        if (index < 0) {
            return Math.max(length + index, 0);
        }
        return Math.min(index, length);
    }

    private static int toInteger(Object value) {
        if (value instanceof Number number) {
            double numeric = number.doubleValue();
            if (Double.isNaN(numeric) || numeric == 0.0d) {
                return 0;
            }
            if (numeric >= Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            if (numeric <= Integer.MIN_VALUE) {
                return Integer.MIN_VALUE;
            }
            return (int) numeric;
        }
        if (value instanceof String text) {
            try {
                return toInteger(Double.parseDouble(text.trim()));
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
        return 0;
    }
}
