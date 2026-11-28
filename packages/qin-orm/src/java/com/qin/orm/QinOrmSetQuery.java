package com.qin.orm;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class QinOrmSetQuery<R> {
    private final EntityManager entityManager;
    private final Class<R> resultType;
    private final List<QinOrmSelect<?, R>> operands;
    private final List<Operator> operators;
    private final int limit;
    private final int offset;

    private QinOrmSetQuery(
            EntityManager entityManager,
            Class<R> resultType,
            List<QinOrmSelect<?, R>> operands,
            List<Operator> operators,
            int limit,
            int offset) {
        this.entityManager = entityManager;
        this.resultType = resultType;
        this.operands = List.copyOf(operands);
        this.operators = List.copyOf(operators);
        this.limit = limit;
        this.offset = offset;
    }

    static <R> QinOrmSetQuery<R> start(
            QinOrmSelect<?, R> first,
            QinOrmSelect<?, R> second,
            Operator operator) {
        Objects.requireNonNull(first, "first");
        requireCompatible(first, second);
        return new QinOrmSetQuery<>(first.entityManager(), first.resultType(),
                List.of(first, second), List.of(operator), -1, 0);
    }

    public QinOrmSetQuery<R> union(QinOrmSelect<?, R> next) {
        return append(next, Operator.UNION);
    }

    public QinOrmSetQuery<R> unionAll(QinOrmSelect<?, R> next) {
        return append(next, Operator.UNION_ALL);
    }

    public QinOrmSetQuery<R> intersect(QinOrmSelect<?, R> next) {
        return append(next, Operator.INTERSECT);
    }

    public QinOrmSetQuery<R> except(QinOrmSelect<?, R> next) {
        return append(next, Operator.EXCEPT);
    }

    public QinOrmSetQuery<R> limit(int nextLimit) {
        if (nextLimit < 1) {
            throw new IllegalArgumentException("limit must be greater than zero");
        }
        return copy(operands, operators, nextLimit, offset);
    }

    public QinOrmSetQuery<R> offset(int nextOffset) {
        if (nextOffset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
        return copy(operands, operators, limit, nextOffset);
    }

    public String toJpql() {
        return render().jpql();
    }

    public List<R> all() {
        Plan plan = render();
        TypedQuery<R> query = createQuery(plan);
        applyRange(query, offset, limit);
        return query.getResultList();
    }

    public R first() {
        Plan plan = render();
        TypedQuery<R> query = createQuery(plan);
        applyRange(query, offset, 1);
        List<R> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public R one() {
        Plan plan = render();
        TypedQuery<R> query = createQuery(plan);
        applyRange(query, offset, limit > 0 ? Math.min(limit, 2) : 2);
        List<R> results = query.getResultList();
        if (results.size() > 1) {
            throw new NonUniqueResultException("Expected at most one set query result but got multiple rows");
        }
        return results.isEmpty() ? null : results.get(0);
    }

    private QinOrmSetQuery<R> append(QinOrmSelect<?, R> next, Operator operator) {
        requireCompatible(operands.getFirst(), next);
        List<QinOrmSelect<?, R>> nextOperands = new ArrayList<>(operands);
        nextOperands.add(next);
        List<Operator> nextOperators = new ArrayList<>(operators);
        nextOperators.add(operator);
        return copy(nextOperands, nextOperators, limit, offset);
    }

    private Plan render() {
        QinOrmJpqlContext context = new QinOrmJpqlContext();
        StringBuilder jpql = new StringBuilder();
        // Reproduce left-associative method chaining even when INTERSECT binds more tightly.
        jpql.append("(".repeat(operands.size() - 2));
        for (int index = 0; index < operands.size(); index++) {
            if (index > 0) {
                jpql.append(' ').append(operators.get(index - 1).jpql()).append(' ');
            }
            jpql.append(operands.get(index).renderSetOperand(context));
            if (index > 0 && index < operands.size() - 1) {
                jpql.append(')');
            }
        }
        return new Plan(jpql.toString(), context.parameters());
    }

    private TypedQuery<R> createQuery(Plan plan) {
        TypedQuery<R> query = entityManager.createQuery(plan.jpql(), resultType);
        plan.parameters().forEach(query::setParameter);
        return query;
    }

    private static void applyRange(TypedQuery<?> query, long firstResult, int maxResults) {
        if (firstResult > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("JPA offset exceeds the supported integer range");
        }
        query.setFirstResult((int) firstResult);
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }
    }

    private QinOrmSetQuery<R> copy(
            List<QinOrmSelect<?, R>> nextOperands,
            List<Operator> nextOperators,
            int nextLimit,
            int nextOffset) {
        return new QinOrmSetQuery<>(entityManager, resultType, nextOperands,
                nextOperators, nextLimit, nextOffset);
    }

    private static void requireCompatible(QinOrmSelect<?, ?> first, QinOrmSelect<?, ?> second) {
        Objects.requireNonNull(second, "next");
        if (first.entityManager() != second.entityManager()) {
            throw new IllegalArgumentException("Set operation queries must use the same EntityManager");
        }
        if (first.resultType() != second.resultType() || !first.selectionTypes().equals(second.selectionTypes())) {
            throw new IllegalArgumentException("Set operation operands must select the same result shape and types");
        }
    }

    private record Plan(String jpql, Map<String, Object> parameters) {
    }

    enum Operator {
        UNION("union"),
        UNION_ALL("union all"),
        INTERSECT("intersect"),
        EXCEPT("except");

        private final String jpql;

        Operator(String jpql) {
            this.jpql = jpql;
        }

        String jpql() {
            return jpql;
        }
    }
}
