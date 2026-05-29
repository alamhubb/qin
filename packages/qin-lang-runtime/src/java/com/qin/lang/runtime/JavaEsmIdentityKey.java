package com.qin.lang.runtime;

import java.util.Objects;

/**
 * JS Map/Set key semantics: primitives compare by value, objects by identity.
 */
final class JavaEsmIdentityKey {
    private final Object value;
    private final boolean identity;
    private final int identityHash;

    private JavaEsmIdentityKey(Object value) {
        this.value = value;
        this.identity = usesIdentity(value);
        this.identityHash = System.identityHashCode(value);
    }

    static JavaEsmIdentityKey of(Object value) {
        return new JavaEsmIdentityKey(value);
    }

    Object value() {
        return value;
    }

    private static boolean usesIdentity(Object value) {
        return value != null
                && !(value instanceof CharSequence)
                && !(value instanceof Number)
                && !(value instanceof Boolean)
                && !(value instanceof Character)
                && !(value instanceof JavaEsmSymbol.JavaSymbol);
    }

    private static Object primitiveValue(Object value) {
        if (value instanceof CharSequence text) {
            return text.toString();
        }
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof JavaEsmIdentityKey otherKey)) {
            return false;
        }
        if (identity || otherKey.identity) {
            return identity && otherKey.identity && value == otherKey.value;
        }
        return Objects.equals(primitiveValue(value), primitiveValue(otherKey.value));
    }

    @Override
    public int hashCode() {
        return identity ? identityHash : Objects.hashCode(primitiveValue(value));
    }
}
