package com.qin.orm;

import java.util.Objects;

public final class QinOrmOrder<E> {
    private final Class<E> entityClass;
    private final String propertyName;
    private final boolean ascending;

    QinOrmOrder(QinOrmColumn<E, ?> column, boolean ascending) {
        Objects.requireNonNull(column, "column");
        this.entityClass = column.entityClass();
        this.propertyName = column.propertyName();
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
