package com.qin.orm;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public final class QinOrm {
    private static final Pattern SQL_IDENTIFIER = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");

    private QinOrm() {
    }

    public static <E> QinOrmEntity<E> entity(Class<E> entityClass) {
        return new QinOrmEntity<>(entityClass);
    }

    public static QinOrmSession session(EntityManager entityManager) {
        return new QinOrmSession(entityManager);
    }

    public static String nativeIdentifier(String identifier, Set<String> allowedIdentifiers) {
        Objects.requireNonNull(identifier, "identifier");
        Objects.requireNonNull(allowedIdentifiers, "allowedIdentifiers");
        if (!SQL_IDENTIFIER.matcher(identifier).matches() || !allowedIdentifiers.contains(identifier)) {
            throw new IllegalArgumentException("Native SQL identifier is outside the allowlist");
        }
        return identifier;
    }

    static String resolveEntityName(Class<?> entityClass) {
        if (entityClass == null) {
            throw new NullPointerException("entityClass");
        }
        Entity annotation = entityClass.getAnnotation(Entity.class);
        if (annotation == null) {
            return entityClass.getSimpleName();
        }

        String declaredName = annotation.name().trim();
        return declaredName.isEmpty() ? entityClass.getSimpleName() : declaredName;
    }
}
