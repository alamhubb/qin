package com.qin.orm;

import jakarta.persistence.EntityManager;
import java.util.Objects;

public final class QinOrmSession {
    private final EntityManager entityManager;

    QinOrmSession(EntityManager entityManager) {
        this.entityManager = Objects.requireNonNull(entityManager, "entityManager");
    }

    public <E> QinOrmSelect<E, E> select(QinOrmEntity<E> entity) {
        return QinOrmSelect.entity(entity, entityManager);
    }

    public <E, V> QinOrmSelect<E, V> select(
            QinOrmEntity<E> entity,
            QinOrmColumn<E, V> column) {
        return QinOrmSelect.scalar(entity, entityManager, column);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public final <E> QinOrmSelect<E, Object[]> select(
            QinOrmEntity<E> entity,
            QinOrmColumn<E, ?> first,
            QinOrmColumn<E, ?> second,
            QinOrmColumn<E, ?>... remaining) {
        return QinOrmSelect.tuple(entity, entityManager, first, second, remaining);
    }
}
