package com.qin.orm;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class QinOrmNativeQuery<R> {
    private final EntityManager entityManager;
    private final String sql;
    private final Class<R> resultType;
    private final Map<String, Object> parameters;
    private final int limit;
    private final int offset;

    QinOrmNativeQuery(EntityManager entityManager, String sql, Class<R> resultType) {
        this(entityManager, sql, resultType, Map.of(), -1, 0);
    }

    private QinOrmNativeQuery(
            EntityManager entityManager,
            String sql,
            Class<R> resultType,
            Map<String, Object> parameters,
            int limit,
            int offset) {
        this.entityManager = Objects.requireNonNull(entityManager, "entityManager");
        this.sql = requireSql(sql);
        this.resultType = Objects.requireNonNull(resultType, "resultType");
        this.parameters = Collections.unmodifiableMap(new LinkedHashMap<>(parameters));
        this.limit = limit;
        this.offset = offset;
    }

    public QinOrmNativeQuery<R> bind(String name, Object value) {
        String parameterName = QinOrmEntity.requireIdentifier(name, "parameter name");
        Map<String, Object> nextParameters = new LinkedHashMap<>(parameters);
        nextParameters.put(parameterName, value);
        return copy(nextParameters, limit, offset);
    }

    public QinOrmNativeQuery<R> limit(int nextLimit) {
        if (nextLimit < 1) {
            throw new IllegalArgumentException("limit must be greater than zero");
        }
        return copy(parameters, nextLimit, offset);
    }

    public QinOrmNativeQuery<R> offset(int nextOffset) {
        if (nextOffset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
        return copy(parameters, limit, nextOffset);
    }

    public List<R> all() {
        Query query = createQuery(limit);
        @SuppressWarnings("unchecked")
        List<R> results = (List<R>) query.getResultList();
        return results;
    }

    public R first() {
        List<R> results = allWithLimit(1);
        return results.isEmpty() ? null : results.get(0);
    }

    public R one() {
        List<R> results = allWithLimit(2);
        if (results.size() > 1) {
            throw new NonUniqueResultException("Expected at most one native query result but got multiple rows");
        }
        return results.isEmpty() ? null : results.get(0);
    }

    private List<R> allWithLimit(int maxResults) {
        Query query = createQuery(limit > 0 ? Math.min(limit, maxResults) : maxResults);
        @SuppressWarnings("unchecked")
        List<R> results = (List<R>) query.getResultList();
        return new ArrayList<>(results);
    }

    private Query createQuery(int maxResults) {
        Query query = entityManager.createNativeQuery(sql, resultType);
        parameters.forEach(query::setParameter);
        if (offset > 0) {
            query.setFirstResult(offset);
        }
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }
        return query;
    }

    private QinOrmNativeQuery<R> copy(Map<String, Object> nextParameters, int nextLimit, int nextOffset) {
        return new QinOrmNativeQuery<>(entityManager, sql, resultType,
                nextParameters, nextLimit, nextOffset);
    }

    private static String requireSql(String sql) {
        Objects.requireNonNull(sql, "sql");
        if (sql.isBlank()) {
            throw new IllegalArgumentException("Native SQL cannot be blank");
        }
        return sql;
    }
}
