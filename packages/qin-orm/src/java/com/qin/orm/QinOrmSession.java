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

    public <R> QinOrmNativeQuery<R> nativeQuery(String sql, Class<R> resultType) {
        return new QinOrmNativeQuery<>(entityManager, sql, resultType);
    }

    public <E, V> QinOrmSelect<E, V> select(
            QinOrmEntity<E> entity,
            QinOrmExpression<E, V> column) {
        return QinOrmSelect.scalar(entity, entityManager, column);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public final <E> QinOrmSelect<E, Object[]> select(
            QinOrmEntity<E> entity,
            QinOrmExpression<?, ?> first,
            QinOrmExpression<?, ?> second,
            QinOrmExpression<?, ?>... remaining) {
        return QinOrmSelect.tuple(entity, entityManager, first, second, remaining);
    }
}
