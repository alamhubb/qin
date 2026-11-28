package com.qin.orm;

import java.util.Objects;

public class QinOrmEntity<E> {
    private final Class<E> javaType;
    private final String entityName;

    public QinOrmEntity(Class<E> javaType) {
        this.javaType = Objects.requireNonNull(javaType, "javaType");
        this.entityName = QinOrmEntity.requireIdentifier(QinOrm.resolveEntityName(javaType), "entityName");
    }

    public Class<E> javaType() {
        return javaType;
    }

    public String entityName() {
        return entityName;
    }

    static String requireIdentifier(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank() || !Character.isJavaIdentifierStart(value.charAt(0))) {
            throw new IllegalArgumentException(name + " must be a non-empty identifier: " + value);
        }
        for (int index = 1; index < value.length(); index++) {
            if (!Character.isJavaIdentifierPart(value.charAt(index))) {
                throw new IllegalArgumentException(name + " must be a non-empty identifier: " + value);
            }
        }
        return value;
    }

    public <V> QinOrmColumn<E, V> column(String propertyName, Class<V> propertyType) {
        return new QinOrmColumn<>(this, propertyName, propertyType);
    }
}
