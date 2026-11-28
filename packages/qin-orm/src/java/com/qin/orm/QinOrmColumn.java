package com.qin.orm;

import java.util.Objects;
import java.util.Collection;

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

    public QinOrmPredicate<E> eq(V value) {
        return QinOrmPredicate.compare(this, "=", value);
    }

    public <T> QinOrmPredicate<E> eq(QinOrmColumn<T, ? extends V> other) {
        return QinOrmPredicate.compareColumns(this, other, "=");
    }

    public QinOrmPredicate<E> ne(V value) {
        return QinOrmPredicate.compare(this, "<>", value);
    }

    public <T> QinOrmPredicate<E> ne(QinOrmColumn<T, ? extends V> other) {
        return QinOrmPredicate.compareColumns(this, other, "<>");
    }

    public QinOrmPredicate<E> gt(V value) {
        return QinOrmPredicate.compare(this, ">", requireValue(value));
    }

    public QinOrmPredicate<E> ge(V value) {
        return QinOrmPredicate.compare(this, ">=", requireValue(value));
    }

    public QinOrmPredicate<E> lt(V value) {
        return QinOrmPredicate.compare(this, "<", requireValue(value));
    }

    public QinOrmPredicate<E> le(V value) {
        return QinOrmPredicate.compare(this, "<=", requireValue(value));
    }

    public QinOrmPredicate<E> between(V lower, V upper) {
        return QinOrmPredicate.between(this, requireValue(lower), requireValue(upper));
    }

    public QinOrmPredicate<E> in(Collection<? extends V> values) {
        Objects.requireNonNull(values, "values");
        if (values.isEmpty()) {
            throw new IllegalArgumentException("in requires at least one value");
        }
        return QinOrmPredicate.in(this, values);
    }

    public QinOrmPredicate<E> isNull() {
        return QinOrmPredicate.nullCheck(this, false);
    }

    public QinOrmPredicate<E> isNotNull() {
        return QinOrmPredicate.nullCheck(this, true);
    }

    public QinOrmPredicate<E> like(String pattern) {
        if (propertyType != String.class) {
            throw new IllegalStateException("like is only supported for string properties");
        }
        return QinOrmPredicate.compare(this, "like", Objects.requireNonNull(pattern, "pattern"));
    }

    public QinOrmOrder<E> asc() {
        return new QinOrmOrder<>(this, true);
    }

    public QinOrmOrder<E> desc() {
        return new QinOrmOrder<>(this, false);
    }

    private V requireValue(V value) {
        return Objects.requireNonNull(value, "value");
    }
}
