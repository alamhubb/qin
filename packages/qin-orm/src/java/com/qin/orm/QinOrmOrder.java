package com.qin.orm;

import org.springframework.data.domain.Sort;

import java.util.Objects;

public final class QinOrmOrder<E> {
    private final Class<E> entityClass;
    private final String propertyName;
    private final boolean ascending;
    private final boolean ignoreCase;
    private final Sort.NullHandling nullHandling;

    QinOrmOrder(QinOrmColumn<E, ?> column, boolean ascending) {
        this(Objects.requireNonNull(column, "column").entityClass(), column.propertyName(), ascending);
    }

    QinOrmOrder(Class<E> entityClass, String propertyName, boolean ascending) {
        this(entityClass, propertyName, ascending, false, Sort.NullHandling.NATIVE);
    }

    QinOrmOrder(Class<E> entityClass, String propertyName, boolean ascending, boolean ignoreCase,
                Sort.NullHandling nullHandling) {
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass");
        this.propertyName = QinOrmEntity.requireIdentifier(propertyName, "sort property");
        this.ascending = ascending;
        this.ignoreCase = ignoreCase;
        this.nullHandling = Objects.requireNonNull(nullHandling, "nullHandling");
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

    Sort.NullHandling nullHandling() {
        return nullHandling;
    }
}
