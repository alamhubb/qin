package com.qin.orm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.function.Function;

public final class QinOrmPredicate<E> {
    private final Class<E> entityClass;
    private final Kind kind;
    private final String propertyName;
    private final Class<?> comparisonEntityClass;
    private final String comparisonPropertyName;
    private final String operator;
    private final Object firstValue;
    private final Object secondValue;
    private final List<QinOrmPredicate<?>> children;

    private QinOrmPredicate(
            Class<E> entityClass,
            Kind kind,
            String propertyName,
            Class<?> comparisonEntityClass,
            String comparisonPropertyName,
            String operator,
            Object firstValue,
            Object secondValue,
            List<QinOrmPredicate<?>> children) {
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass");
        this.kind = Objects.requireNonNull(kind, "kind");
        this.propertyName = propertyName;
        this.comparisonEntityClass = comparisonEntityClass;
        this.comparisonPropertyName = comparisonPropertyName;
        this.operator = operator;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.children = children;
    }

    static <E> QinOrmPredicate<E> compare(QinOrmColumn<E, ?> column, String operator, Object value) {
        return new QinOrmPredicate<>(column.entityClass(), Kind.COMPARE, column.propertyName(), null, null, operator,
                value, null, List.of());
    }

    static <E, V> QinOrmPredicate<E> compareColumns(
            QinOrmColumn<E, V> column,
            QinOrmColumn<?, ? extends V> other,
            String operator) {
        Objects.requireNonNull(other, "other");
        return new QinOrmPredicate<>(column.entityClass(), Kind.COMPARE, column.propertyName(),
                other.entityClass(), other.propertyName(), operator, null, null, List.of());
    }

    static <E> QinOrmPredicate<E> between(QinOrmColumn<E, ?> column, Object lower, Object upper) {
        return new QinOrmPredicate<>(column.entityClass(), Kind.BETWEEN, column.propertyName(), null, null, null,
                lower, upper, List.of());
    }

    static <E> QinOrmPredicate<E> in(QinOrmColumn<E, ?> column, Collection<?> values) {
        return new QinOrmPredicate<>(column.entityClass(), Kind.IN, column.propertyName(), null, null, null,
                List.copyOf(values), null, List.of());
    }

    static <E> QinOrmPredicate<E> nullCheck(QinOrmColumn<E, ?> column, boolean negated) {
        return new QinOrmPredicate<>(column.entityClass(), negated ? Kind.IS_NOT_NULL : Kind.IS_NULL,
                column.propertyName(), null, null, null, null, null, List.of());
    }

    public QinOrmPredicate<E> and(QinOrmPredicate<?> other) {
        return combine(Kind.AND, other);
    }

    public QinOrmPredicate<E> or(QinOrmPredicate<?> other) {
        return combine(Kind.OR, other);
    }

    public QinOrmPredicate<E> not() {
        return new QinOrmPredicate<>(entityClass, Kind.NOT, null, null, null, null, null, null, List.of(this));
    }

    static <E> QinOrmPredicate<E> andAll(Collection<QinOrmPredicate<E>> predicates) {
        return combineAll(Kind.AND, predicates);
    }

    static <E> QinOrmPredicate<E> orAll(Collection<QinOrmPredicate<E>> predicates) {
        return combineAll(Kind.OR, predicates);
    }

    Class<E> entityClass() {
        return entityClass;
    }

    String render(QinOrmJpqlContext context, Function<Class<?>, String> aliasFor) {
        return switch (kind) {
            case COMPARE -> renderComparison(context, aliasFor);
            case BETWEEN -> aliasFor.apply(entityClass) + "." + propertyName + " between " + context.bind(firstValue)
                    + " and " + context.bind(secondValue);
            case IN -> aliasFor.apply(entityClass) + "." + propertyName + " in " + context.bind(firstValue);
            case IS_NULL -> aliasFor.apply(entityClass) + "." + propertyName + " is null";
            case IS_NOT_NULL -> aliasFor.apply(entityClass) + "." + propertyName + " is not null";
            case AND, OR -> renderChildren(context, aliasFor);
            case NOT -> "not (" + children.get(0).render(context, aliasFor) + ")";
        };
    }

    Set<PropertyReference> propertyReferences() {
        Set<PropertyReference> names = new LinkedHashSet<>();
        if (propertyName != null) {
            names.add(new PropertyReference(entityClass, propertyName));
        }
        if (comparisonPropertyName != null) {
            names.add(new PropertyReference(comparisonEntityClass, comparisonPropertyName));
        }
        for (QinOrmPredicate<?> child : children) {
            names.addAll(child.propertyReferences());
        }
        return names;
    }

    private String renderComparison(QinOrmJpqlContext context, Function<Class<?>, String> aliasFor) {
        String path = aliasFor.apply(entityClass) + "." + propertyName;
        if (comparisonPropertyName != null) {
            return path + " " + operator + " " + aliasFor.apply(comparisonEntityClass)
                    + "." + comparisonPropertyName;
        }
        if (firstValue == null && operator.equals("=")) {
            return path + " is null";
        }
        if (firstValue == null && operator.equals("<>")) {
            return path + " is not null";
        }
        return path + " " + operator + " " + context.bind(firstValue);
    }

    private String renderChildren(QinOrmJpqlContext context, Function<Class<?>, String> aliasFor) {
        String separator = kind == Kind.AND ? " and " : " or ";
        List<String> expressions = new ArrayList<>(children.size());
        for (QinOrmPredicate<?> child : children) {
            expressions.add("(" + child.render(context, aliasFor) + ")");
        }
        return String.join(separator, expressions);
    }

    private QinOrmPredicate<E> combine(Kind combinedKind, QinOrmPredicate<?> other) {
        Objects.requireNonNull(other, "other");
        List<QinOrmPredicate<?>> combined = new ArrayList<>();
        appendFlattened(combined, combinedKind, this);
        appendFlattened(combined, combinedKind, other);
        return new QinOrmPredicate<>(entityClass, combinedKind, null, null, null, null, null, null,
                List.copyOf(combined));
    }

    private static <E> QinOrmPredicate<E> combineAll(Kind kind, Collection<QinOrmPredicate<E>> predicates) {
        Objects.requireNonNull(predicates, "predicates");
        if (predicates.isEmpty()) {
            throw new IllegalArgumentException("At least one predicate is required");
        }
        QinOrmPredicate<E> result = null;
        for (QinOrmPredicate<E> predicate : predicates) {
            result = result == null ? Objects.requireNonNull(predicate, "predicate") : result.combine(kind, predicate);
        }
        return result;
    }

    private static void appendFlattened(
            List<QinOrmPredicate<?>> target,
            Kind kind,
            QinOrmPredicate<?> predicate) {
        if (predicate.kind == kind) {
            target.addAll(predicate.children);
        } else {
            target.add(predicate);
        }
    }

    private enum Kind {
        COMPARE,
        BETWEEN,
        IN,
        IS_NULL,
        IS_NOT_NULL,
        AND,
        OR,
        NOT
    }

    record PropertyReference(Class<?> entityType, String propertyName) {
    }
}
