package com.qin.orm;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public final class QinOrmAggregate<E, V> implements QinOrmExpression<E, V> {
    private final QinOrmExpression<E, ?> argument;
    private final String function;
    private final Class<V> resultType;
    private final boolean distinct;

    private QinOrmAggregate(
            QinOrmExpression<E, ?> argument,
            String function,
            Class<V> resultType,
            boolean distinct) {
        this.argument = Objects.requireNonNull(argument, "argument");
        this.function = QinOrmEntity.requireIdentifier(function, "function");
        this.resultType = Objects.requireNonNull(resultType, "resultType");
        this.distinct = distinct;
    }

    static <E, V> QinOrmAggregate<E, V> of(
            QinOrmExpression<E, ?> argument,
            String function,
            Class<V> resultType,
            boolean distinct) {
        return new QinOrmAggregate<>(argument, function, resultType, distinct);
    }

    QinOrmExpression<E, ?> argument() {
        return argument;
    }

    @Override
    public Class<E> entityClass() {
        return argument.entityClass();
    }

    @Override
    public Class<V> propertyType() {
        return resultType;
    }

    @Override
    public String render(Function<Class<?>, String> aliasFor) {
        return function + "(" + (distinct ? "distinct " : "") + argument.render(aliasFor) + ")";
    }

    @Override
    public Set<QinOrmPredicate.PropertyReference> propertyReferences() {
        return argument.propertyReferences();
    }
}
