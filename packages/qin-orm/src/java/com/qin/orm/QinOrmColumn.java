package com.qin.orm;

import java.util.Objects;

public final class QinOrmColumn<E, V> {
    private final Class<E> entityClass;
    private final String propertyName;
    private final Class<V> propertyType;

    QinOrmColumn(QinOrmEntity<E> entity, String propertyName, Class<V> propertyType) {
        Objects.requireNonNull(entity, "entity");
        this.entityClass = entity.javaType();
        this.propertyName = QinOrmEntity.requireIdentifier(propertyName, "propertyName");
        this.propertyType = Objects.requireNonNull(propertyType, "propertyType");
    }

    Class<E> entityClass() {
        return entityClass;
    }

    public String propertyName() {
        return propertyName;
    }

    public Class<V> propertyType() {
        return propertyType;
    }
}
