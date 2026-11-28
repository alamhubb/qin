package com.qin.orm;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;

public final class QinOrm {
    private QinOrm() {
    }

    public static <E> QinOrmEntity<E> entity(Class<E> entityClass) {
        return new QinOrmEntity<>(entityClass);
    }

    public static QinOrmSession session(EntityManager entityManager) {
        return new QinOrmSession(entityManager);
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
