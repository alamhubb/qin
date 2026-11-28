package com.qin.orm;

import java.util.Set;
import java.util.Objects;
import java.util.function.Function;

public sealed interface QinOrmExpression<E, V>
        permits QinOrmColumn, QinOrmAggregate {
    Class<E> entityClass();

    Class<V> propertyType();

    String render(Function<Class<?>, String> aliasFor);

    Set<QinOrmPredicate.PropertyReference> propertyReferences();

    default String signature() {
        return getClass().getName() + propertyReferences();
    }

    default QinOrmPredicate<E> eq(V value) {
        return QinOrmPredicate.compareExpression(this, "=", value);
    }

    default <T> QinOrmPredicate<E> eq(QinOrmExpression<T, ? extends V> other) {
        return QinOrmPredicate.compareExpressions(this, other, "=");
    }

    default QinOrmPredicate<E> ne(V value) {
        return QinOrmPredicate.compareExpression(this, "<>", value);
    }

    default <T> QinOrmPredicate<E> ne(QinOrmExpression<T, ? extends V> other) {
        return QinOrmPredicate.compareExpressions(this, other, "<>");
    }

    default QinOrmPredicate<E> gt(V value) {
        return QinOrmPredicate.compareExpression(this, ">", Objects.requireNonNull(value, "value"));
    }

    default QinOrmPredicate<E> ge(V value) {
        return QinOrmPredicate.compareExpression(this, ">=", Objects.requireNonNull(value, "value"));
    }

    default QinOrmPredicate<E> lt(V value) {
        return QinOrmPredicate.compareExpression(this, "<", Objects.requireNonNull(value, "value"));
    }

    default QinOrmPredicate<E> le(V value) {
        return QinOrmPredicate.compareExpression(this, "<=", Objects.requireNonNull(value, "value"));
    }

    default QinOrmPredicate<E> in(QinOrmSubquery<? extends V> subquery) {
        return QinOrmPredicate.inSubquery(this, subquery);
    }

    default QinOrmPredicate<E> isNull() {
        return QinOrmPredicate.compareExpression(this, "=", null);
    }

    default QinOrmPredicate<E> isNotNull() {
        return QinOrmPredicate.compareExpression(this, "<>", null);
    }

    default QinOrmAggregate<E, Long> count() {
        return QinOrmAggregate.of(this, "count", Long.class, false);
    }

    default QinOrmAggregate<E, Long> countDistinct() {
        return QinOrmAggregate.of(this, "count", Long.class, true);
    }

    default QinOrmAggregate<E, Number> sum() {
        requireNumeric("sum");
        return QinOrmAggregate.of(this, "sum", Number.class, false);
    }

    default QinOrmAggregate<E, Double> avg() {
        requireNumeric("avg");
        return QinOrmAggregate.<E, Double>of(this, "avg", Double.class, false);
    }

    default QinOrmAggregate<E, V> min() {
        return QinOrmAggregate.of(this, "min", propertyType(), false);
    }

    default QinOrmAggregate<E, V> max() {
        return QinOrmAggregate.of(this, "max", propertyType(), false);
    }

    default void requireNumeric(String function) {
        if (!Number.class.isAssignableFrom(propertyType())) {
            throw new IllegalStateException(function + " is only supported for numeric properties");
        }
    }
}
