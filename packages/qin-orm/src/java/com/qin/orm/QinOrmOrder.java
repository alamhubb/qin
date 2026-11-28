package com.qin.orm;

import java.util.Objects;

public final class QinOrmOrder<E> {
    private final Class<E> entityClass;
    private final String propertyName;
    private final boolean ascending;

    QinOrmOrder(QinOrmColumn<E, ?> column, boolean ascending) {
        this(Objects.requireNonNull(column, "column").entityClass(), column.propertyName(), ascending);
    }

    QinOrmOrder(QinOrmEntity<E> entity, String propertyName, boolean ascending) {
        this(Objects.requireNonNull(entity, "entity").javaType(), propertyName, ascending);
    }

    QinOrmOrder(Class<E> entityClass, String propertyName, boolean ascending) {
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass");
        this.propertyName = QinOrmEntity.requireIdentifier(propertyName, "sort property");
        this.ascending = ascending;
    }

    Class<E> entityClass() {
        return entityClass;
    }

    String propertyName() {
        return propertyName;
    }

    boolean ascending() {
        return ascending;
    }
}
