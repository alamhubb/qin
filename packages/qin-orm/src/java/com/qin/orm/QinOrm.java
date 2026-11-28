package com.qin.orm;

import jakarta.persistence.Entity;

public final class QinOrm {
    private QinOrm() {
    }

    public static <E> QinOrmEntity<E> entity(Class<E> entityClass) {
        return new QinOrmEntity<>(entityClass);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <E> QinOrmSelect<E> select(
            QinOrmEntity<E> entity,
            QinOrmColumn<E, ?>... columns) {
        return new QinOrmSelect<>(entity, columns);
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
