package com.qin.orm;

import jakarta.persistence.EntityManager;
import java.util.Objects;

public final class QinOrmSession {
    private final EntityManager entityManager;

    QinOrmSession(EntityManager entityManager) {
        this.entityManager = Objects.requireNonNull(entityManager, "entityManager");
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public final <E> QinOrmSelect<E> select(
            QinOrmEntity<E> entity,
            QinOrmColumn<E, ?>... columns) {
        return new QinOrmSelect<>(entity, entityManager, columns);
    }
}
