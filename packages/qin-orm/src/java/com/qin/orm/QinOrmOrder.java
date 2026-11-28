package com.qin.orm;

import java.util.Objects;

public final class QinOrmOrder<E> {
    private final Class<E> entityClass;
    private final String propertyName;
    private final boolean ascending;
    private final boolean ignoreCase;

    QinOrmOrder(QinOrmColumn<E, ?> column, boolean ascending) {
        this(Objects.requireNonNull(column, "column").entityClass(), column.propertyName(), ascending, false);
    }

    QinOrmOrder(QinOrmEntity<E> entity, String propertyName, boolean ascending) {
        this(Objects.requireNonNull(entity, "entity").javaType(), propertyName, ascending, false);
    }

    QinOrmOrder(Class<E> entityClass, String propertyName, boolean ascending) {
        this(entityClass, propertyName, ascending, false);
    }

    QinOrmOrder(Class<E> entityClass, String propertyName, boolean ascending, boolean ignoreCase) {
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass");
        this.propertyName = QinOrmEntity.requireIdentifier(propertyName, "sort property");
        this.ascending = ascending;
        this.ignoreCase = ignoreCase;
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

    boolean ignoreCase() {
        return ignoreCase;
    }
}
