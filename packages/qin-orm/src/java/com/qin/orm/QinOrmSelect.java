package com.qin.orm;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class QinOrmSelect<E> {
    private static final String ROOT_ALIAS = "qinEntity";

    private final QinOrmEntity<E> entity;
    private final List<QinOrmColumn<E, ?>> columns;

    @SafeVarargs
    QinOrmSelect(QinOrmEntity<E> entity, QinOrmColumn<E, ?>... columns) {
        this.entity = Objects.requireNonNull(entity, "entity");
        Objects.requireNonNull(columns, "columns");
        if (columns.length == 0) {
            throw new IllegalArgumentException("select requires at least one entity column");
        }

        this.columns = new ArrayList<>(columns.length);
        Set<String> selectedNames = new HashSet<>();
        for (QinOrmColumn<E, ?> column : columns) {
            Objects.requireNonNull(column, "column");
            if (column.entityClass() != entity.javaType()) {
                throw new IllegalArgumentException("Column belongs to a different entity: " + column.propertyName());
            }
            if (!selectedNames.add(column.propertyName())) {
                throw new IllegalArgumentException("Duplicate selected entity property: " + column.propertyName());
            }
            this.columns.add(column);
        }
    }

    public String toJpql() {
        return toJpql(entity.entityName());
    }

    public String toJpql(EntityManager entityManager) {
        return toJpql(resolveManagedEntity(entityManager).getName());
    }

    private String toJpql(String entityName) {
        StringBuilder jpql = new StringBuilder("select ");
        for (int index = 0; index < columns.size(); index++) {
            if (index > 0) {
                jpql.append(", ");
            }
            String propertyName = columns.get(index).propertyName();
            jpql.append(ROOT_ALIAS).append('.').append(propertyName).append(" as ").append(propertyName);
        }
        return jpql.append(" from ").append(entityName).append(' ').append(ROOT_ALIAS).toString();
    }

    public List<QinOrmRow> fetch(EntityManager entityManager) {
        EntityType<E> jpaEntity = resolveManagedEntity(entityManager);
        return entityManager.createQuery(toJpql(jpaEntity.getName()), Tuple.class)
                .getResultList()
                .stream()
                .map(QinOrmRow::new)
                .toList();
    }

    private EntityType<E> resolveManagedEntity(EntityManager entityManager) {
        Objects.requireNonNull(entityManager, "entityManager");
        EntityType<E> jpaEntity;
        try {
            jpaEntity = entityManager.getMetamodel().entity(entity.javaType());
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException(
                    "Class is not a managed JPA entity: " + entity.javaType().getName(), error);
        }

        for (QinOrmColumn<E, ?> column : columns) {
            var attribute = jpaEntity.getAttribute(column.propertyName());
            if (!attribute.getJavaType().equals(column.propertyType())) {
                throw new IllegalArgumentException(
                        "Column type mismatch for " + column.propertyName() + ": expected "
                                + attribute.getJavaType().getTypeName() + " but descriptor declares "
                                + column.propertyType().getTypeName());
            }
        }
        return jpaEntity;
    }
}
