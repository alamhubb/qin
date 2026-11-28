package com.qin.orm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.LinkedHashSet;

public final class QinOrmPredicate<E> {
    private final Class<E> entityClass;
    private final Kind kind;
    private final String propertyName;
    private final String operator;
    private final Object firstValue;
    private final Object secondValue;
    private final List<QinOrmPredicate<E>> children;

    private QinOrmPredicate(
            Class<E> entityClass,
            Kind kind,
            String propertyName,
            String operator,
            Object firstValue,
            Object secondValue,
            List<QinOrmPredicate<E>> children) {
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass");
        this.kind = Objects.requireNonNull(kind, "kind");
        this.propertyName = propertyName;
        this.operator = operator;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.children = children;
    }

    static <E> QinOrmPredicate<E> compare(QinOrmColumn<E, ?> column, String operator, Object value) {
        return new QinOrmPredicate<>(column.entityClass(), Kind.COMPARE, column.propertyName(), operator,
                value, null, List.of());
    }

    static <E> QinOrmPredicate<E> between(QinOrmColumn<E, ?> column, Object lower, Object upper) {
        return new QinOrmPredicate<>(column.entityClass(), Kind.BETWEEN, column.propertyName(), null,
                lower, upper, List.of());
    }

    static <E> QinOrmPredicate<E> in(QinOrmColumn<E, ?> column, Collection<?> values) {
        return new QinOrmPredicate<>(column.entityClass(), Kind.IN, column.propertyName(), null,
                List.copyOf(values), null, List.of());
    }

    static <E> QinOrmPredicate<E> nullCheck(QinOrmColumn<E, ?> column, boolean negated) {
        return new QinOrmPredicate<>(column.entityClass(), negated ? Kind.IS_NOT_NULL : Kind.IS_NULL,
                column.propertyName(), null, null, null, List.of());
    }

    public QinOrmPredicate<E> and(QinOrmPredicate<E> other) {
        return combine(Kind.AND, other);
    }

    public QinOrmPredicate<E> or(QinOrmPredicate<E> other) {
        return combine(Kind.OR, other);
    }

    public QinOrmPredicate<E> not() {
        return new QinOrmPredicate<>(entityClass, Kind.NOT, null, null, null, null, List.of(this));
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

    String render(QinOrmJpqlContext context, String alias) {
        return switch (kind) {
            case COMPARE -> renderComparison(context, alias);
            case BETWEEN -> alias + "." + propertyName + " between " + context.bind(firstValue)
                    + " and " + context.bind(secondValue);
            case IN -> alias + "." + propertyName + " in " + context.bind(firstValue);
            case IS_NULL -> alias + "." + propertyName + " is null";
            case IS_NOT_NULL -> alias + "." + propertyName + " is not null";
            case AND, OR -> renderChildren(context, alias);
            case NOT -> "not (" + children.get(0).render(context, alias) + ")";
        };
    }

    Set<String> propertyNames() {
        Set<String> names = new LinkedHashSet<>();
        if (propertyName != null) {
            names.add(propertyName);
        }
        for (QinOrmPredicate<E> child : children) {
            names.addAll(child.propertyNames());
        }
        return names;
    }

    private String renderComparison(QinOrmJpqlContext context, String alias) {
        if (firstValue == null && operator.equals("=")) {
            return alias + "." + propertyName + " is null";
        }
        if (firstValue == null && operator.equals("<>")) {
            return alias + "." + propertyName + " is not null";
        }
        return alias + "." + propertyName + " " + operator + " " + context.bind(firstValue);
    }

    private String renderChildren(QinOrmJpqlContext context, String alias) {
        String separator = kind == Kind.AND ? " and " : " or ";
        List<String> expressions = new ArrayList<>(children.size());
        for (QinOrmPredicate<E> child : children) {
            expressions.add("(" + child.render(context, alias) + ")");
        }
        return String.join(separator, expressions);
    }

    private QinOrmPredicate<E> combine(Kind combinedKind, QinOrmPredicate<E> other) {
        Objects.requireNonNull(other, "other");
        if (entityClass != other.entityClass) {
            throw new IllegalArgumentException("Cannot combine predicates from different entities");
        }
        List<QinOrmPredicate<E>> combined = new ArrayList<>();
        appendFlattened(combined, combinedKind, this);
        appendFlattened(combined, combinedKind, other);
        return new QinOrmPredicate<>(entityClass, combinedKind, null, null, null, null, List.copyOf(combined));
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

    private static <E> void appendFlattened(
            List<QinOrmPredicate<E>> target,
            Kind kind,
            QinOrmPredicate<E> predicate) {
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
}
