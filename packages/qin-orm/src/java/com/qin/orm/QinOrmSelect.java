package com.qin.orm;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class QinOrmSelect<E, R> {
    private static final String ROOT_ALIAS = "qinEntity";

    private final QinOrmEntity<E> entity;
    private final EntityManager entityManager;
    private final SelectionKind selectionKind;
    private final Class<R> resultType;
    private final List<QinOrmColumn<E, ?>> columns;
    private final QinOrmPredicate<E> predicate;
    private final List<QinOrmOrder<E>> orders;
    private final boolean distinct;
    private final int limit;
    private final int offset;
    private final Class<?> dtoType;

    private QinOrmSelect(
            QinOrmEntity<E> entity,
            EntityManager entityManager,
            SelectionKind selectionKind,
            Class<R> resultType,
            List<QinOrmColumn<E, ?>> columns,
            QinOrmPredicate<E> predicate,
            List<QinOrmOrder<E>> orders,
            boolean distinct,
            int limit,
            int offset,
            Class<?> dtoType) {
        this.entity = Objects.requireNonNull(entity, "entity");
        this.entityManager = Objects.requireNonNull(entityManager, "entityManager");
        this.selectionKind = Objects.requireNonNull(selectionKind, "selectionKind");
        this.resultType = Objects.requireNonNull(resultType, "resultType");
        this.columns = List.copyOf(columns);
        this.predicate = predicate;
        this.orders = List.copyOf(orders);
        this.distinct = distinct;
        this.limit = limit;
        this.offset = offset;
        this.dtoType = dtoType;
    }

    static <E> QinOrmSelect<E, E> entity(QinOrmEntity<E> entity, EntityManager entityManager) {
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.ENTITY, entity.javaType(),
                List.of(), null, List.of(), false, -1, 0, null);
    }

    static <E, V> QinOrmSelect<E, V> scalar(
            QinOrmEntity<E> entity,
            EntityManager entityManager,
            QinOrmColumn<E, V> column) {
        requireSameEntity(entity, column);
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.SCALAR, column.propertyType(),
                List.of(column), null, List.of(), false, -1, 0, null);
    }

    @SafeVarargs
    static <E> QinOrmSelect<E, Object[]> tuple(
            QinOrmEntity<E> entity,
            EntityManager entityManager,
            QinOrmColumn<E, ?> first,
            QinOrmColumn<E, ?> second,
            QinOrmColumn<E, ?>... remaining) {
        List<QinOrmColumn<E, ?>> columns = new ArrayList<>();
        columns.add(Objects.requireNonNull(first, "first"));
        columns.add(Objects.requireNonNull(second, "second"));
        for (QinOrmColumn<E, ?> column : remaining) {
            columns.add(Objects.requireNonNull(column, "column"));
        }
        Set<String> selectedNames = new HashSet<>();
        for (QinOrmColumn<E, ?> column : columns) {
            requireSameEntity(entity, column);
            if (!selectedNames.add(column.propertyName())) {
                throw new IllegalArgumentException("Duplicate selected entity property: " + column.propertyName());
            }
        }
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.TUPLE, Object[].class,
                columns, null, List.of(), false, -1, 0, null);
    }

    public <D> QinOrmSelect<E, D> as(Class<D> dtoType) {
        if (selectionKind != SelectionKind.TUPLE) {
            throw new IllegalStateException("DTO projection requires at least two selected properties");
        }
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.DTO, dtoType,
                columns, predicate, orders, distinct, limit, offset,
                Objects.requireNonNull(dtoType, "dtoType"));
    }

    public QinOrmSelect<E, R> where(QinOrmPredicate<E> nextPredicate) {
        Objects.requireNonNull(nextPredicate, "nextPredicate");
        if (nextPredicate.entityClass() != entity.javaType()) {
            throw new IllegalArgumentException("Predicate belongs to a different entity");
        }
        QinOrmPredicate<E> combined = predicate == null ? nextPredicate : predicate.and(nextPredicate);
        return copy(combined, orders, distinct, limit, offset);
    }

    @SafeVarargs
    public final QinOrmSelect<E, R> orderBy(QinOrmOrder<E>... nextOrders) {
        Objects.requireNonNull(nextOrders, "orders");
        List<QinOrmOrder<E>> combined = new ArrayList<>(orders);
        for (QinOrmOrder<E> order : nextOrders) {
            Objects.requireNonNull(order, "order");
            if (order.entityClass() != entity.javaType()) {
                throw new IllegalArgumentException("Sort property belongs to a different entity");
            }
            combined.add(order);
        }
        return copy(predicate, combined, distinct, limit, offset);
    }

    public QinOrmSelect<E, R> orderBy(Sort sort) {
        Objects.requireNonNull(sort, "sort");
        List<QinOrmOrder<E>> combined = new ArrayList<>(orders);
        for (Sort.Order order : sort) {
            String property = QinOrmEntity.requireIdentifier(order.getProperty(), "sort property");
            combined.add(new QinOrmOrder<>(entity.column(property, Object.class), order.isAscending()));
        }
        return copy(predicate, combined, distinct, limit, offset);
    }

    public QinOrmSelect<E, R> distinct() {
        return copy(predicate, orders, true, limit, offset);
    }

    public QinOrmSelect<E, R> limit(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be greater than zero");
        }
        return copy(predicate, orders, distinct, limit, offset);
    }

    public QinOrmSelect<E, R> offset(int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
        return copy(predicate, orders, distinct, limit, offset);
    }

    public String toJpql() {
        return render(resolveManagedEntity().getName(), new QinOrmJpqlContext(), orders).jpql();
    }

    public List<R> all() {
        EntityType<E> jpaEntity = resolveManagedEntity();
        QueryPlan plan = render(jpaEntity.getName(), new QinOrmJpqlContext(), orders);
        TypedQuery<R> query = createQuery(plan);
        applyRange(query, offset, limit);
        return query.getResultList();
    }

    public R one() {
        EntityType<E> jpaEntity = resolveManagedEntity();
        QueryPlan plan = render(jpaEntity.getName(), new QinOrmJpqlContext(), orders);
        TypedQuery<R> query = createQuery(plan);
        applyRange(query, offset, limit > 0 ? Math.min(limit, 2) : 2);
        List<R> results = query.getResultList();
        if (results.size() > 1) {
            throw new NonUniqueResultException("Expected at most one result but query returned multiple results");
        }
        return results.isEmpty() ? null : results.get(0);
    }

    public R first() {
        EntityType<E> jpaEntity = resolveManagedEntity();
        QueryPlan plan = render(jpaEntity.getName(), new QinOrmJpqlContext(), orders);
        TypedQuery<R> query = createQuery(plan);
        applyRange(query, offset, 1);
        List<R> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public long count() {
        EntityType<E> jpaEntity = resolveManagedEntity();
        QinOrmJpqlContext context = new QinOrmJpqlContext();
        StringBuilder jpql = new StringBuilder("select count(").append(ROOT_ALIAS).append(") from ")
                .append(jpaEntity.getName()).append(' ').append(ROOT_ALIAS);
        appendWhere(jpql, context);
        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
        bind(query, context.parameters());
        return query.getSingleResult();
    }

    public boolean exists() {
        EntityType<E> jpaEntity = resolveManagedEntity();
        QinOrmJpqlContext context = new QinOrmJpqlContext();
        StringBuilder jpql = new StringBuilder("select 1 from ").append(jpaEntity.getName()).append(' ')
                .append(ROOT_ALIAS);
        appendWhere(jpql, context);
        TypedQuery<Integer> query = entityManager.createQuery(jpql.toString(), Integer.class).setMaxResults(1);
        bind(query, context.parameters());
        return !query.getResultList().isEmpty();
    }

    public Page<R> page(Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable");
        EntityType<E> jpaEntity = resolveManagedEntity();
        if (pageable.isUnpaged()) {
            List<R> content = all();
            return new PageImpl<>(content, pageable, content.size());
        }
        List<QinOrmOrder<E>> pageOrders = ordersForSort(pageable.getSort());
        validateOrders(jpaEntity, pageOrders);
        QueryPlan plan = render(jpaEntity.getName(), new QinOrmJpqlContext(), pageOrders);
        TypedQuery<R> query = createQuery(plan);
        applyRange(query, pageable.getOffset(), pageable.getPageSize());
        List<R> content = query.getResultList();
        return new PageImpl<>(content, pageable, count());
    }

    public Slice<R> slice(Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable");
        if (pageable.isUnpaged()) {
            return new SliceImpl<>(all(), pageable, false);
        }
        EntityType<E> jpaEntity = resolveManagedEntity();
        List<QinOrmOrder<E>> pageOrders = ordersForSort(pageable.getSort());
        validateOrders(jpaEntity, pageOrders);
        QueryPlan plan = render(jpaEntity.getName(), new QinOrmJpqlContext(), pageOrders);
        TypedQuery<R> query = createQuery(plan);
        applyRange(query, pageable.getOffset(), pageable.getPageSize() + 1);
        List<R> results = query.getResultList();
        boolean hasNext = results.size() > pageable.getPageSize();
        List<R> content = hasNext ? results.subList(0, pageable.getPageSize()) : results;
        return new SliceImpl<>(content, pageable, hasNext);
    }

    private QueryPlan render(String entityName, QinOrmJpqlContext context, List<QinOrmOrder<E>> queryOrders) {
        StringBuilder jpql = new StringBuilder("select ");
        if (distinct) {
            jpql.append("distinct ");
        }
        switch (selectionKind) {
            case ENTITY -> jpql.append(ROOT_ALIAS);
            case SCALAR -> jpql.append(path(columns.get(0)));
            case TUPLE -> appendColumnSelection(jpql);
            case DTO -> appendDtoSelection(jpql);
        }
        jpql.append(" from ").append(entityName).append(' ').append(ROOT_ALIAS);
        appendWhere(jpql, context);
        appendOrderBy(jpql, queryOrders);
        return new QueryPlan(jpql.toString(), context.parameters());
    }

    private void appendColumnSelection(StringBuilder jpql) {
        for (int index = 0; index < columns.size(); index++) {
            if (index > 0) {
                jpql.append(", ");
            }
            jpql.append(path(columns.get(index)));
        }
    }

    private void appendDtoSelection(StringBuilder jpql) {
        String dtoName = dtoType.getCanonicalName();
        if (dtoName == null) {
            throw new IllegalArgumentException("DTO projection type must have a canonical class name");
        }
        jpql.append("new ").append(dtoName).append('(');
        appendColumnSelection(jpql);
        jpql.append(')');
    }

    private void appendWhere(StringBuilder jpql, QinOrmJpqlContext context) {
        if (predicate != null) {
            jpql.append(" where ").append(predicate.render(context, ROOT_ALIAS));
        }
    }

    private void appendOrderBy(StringBuilder jpql, List<QinOrmOrder<E>> queryOrders) {
        if (queryOrders.isEmpty()) {
            return;
        }
        jpql.append(" order by ");
        for (int index = 0; index < queryOrders.size(); index++) {
            if (index > 0) {
                jpql.append(", ");
            }
            QinOrmOrder<E> order = queryOrders.get(index);
            jpql.append(ROOT_ALIAS).append('.').append(order.propertyName())
                    .append(order.ascending() ? " asc" : " desc");
        }
    }

    private List<QinOrmOrder<E>> ordersForSort(Sort sort) {
        if (sort.isUnsorted()) {
            return orders;
        }
        List<QinOrmOrder<E>> combined = new ArrayList<>(orders);
        for (Sort.Order order : sort) {
            String property = QinOrmEntity.requireIdentifier(order.getProperty(), "sort property");
            combined.add(new QinOrmOrder<>(entity.column(property, Object.class), order.isAscending()));
        }
        return combined;
    }

    private TypedQuery<R> createQuery(QueryPlan plan) {
        TypedQuery<R> query = entityManager.createQuery(plan.jpql(), resultType);
        bind(query, plan.parameters());
        return query;
    }

    private void bind(TypedQuery<?> query, Map<String, Object> parameters) {
        parameters.forEach(query::setParameter);
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

    private EntityType<E> resolveManagedEntity() {
        EntityType<E> jpaEntity;
        try {
            jpaEntity = entityManager.getMetamodel().entity(entity.javaType());
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException(
                    "Class is not a managed JPA entity: " + entity.javaType().getName(), error);
        }

        for (QinOrmColumn<E, ?> column : columns) {
            validateProperty(jpaEntity, column.propertyName());
            Class<?> mappedType = jpaEntity.getAttribute(column.propertyName()).getJavaType();
            if (!mappedType.equals(column.propertyType())) {
                throw new IllegalArgumentException(
                        "Column type mismatch for " + column.propertyName() + ": expected "
                                + mappedType.getTypeName() + " but descriptor declares "
                                + column.propertyType().getTypeName());
            }
        }
        if (predicate != null) {
            for (String property : predicate.propertyNames()) {
                validateProperty(jpaEntity, property);
            }
        }
        for (QinOrmOrder<E> order : orders) {
            validateProperty(jpaEntity, order.propertyName());
        }
        return jpaEntity;
    }

    private void validateOrders(EntityType<E> jpaEntity, List<QinOrmOrder<E>> queryOrders) {
        for (QinOrmOrder<E> order : queryOrders) {
            validateProperty(jpaEntity, order.propertyName());
        }
    }

    private void validateProperty(EntityType<E> jpaEntity, String propertyName) {
        try {
            jpaEntity.getAttribute(propertyName);
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException(
                    "Unknown persistent property " + propertyName + " on " + entity.javaType().getName(), error);
        }
    }

    private static String path(QinOrmColumn<?, ?> column) {
        return ROOT_ALIAS + "." + column.propertyName();
    }

    private QinOrmSelect<E, R> copy(
            QinOrmPredicate<E> nextPredicate,
            List<QinOrmOrder<E>> nextOrders,
            boolean nextDistinct,
            int nextLimit,
            int nextOffset) {
        return new QinOrmSelect<>(entity, entityManager, selectionKind, resultType, columns,
                nextPredicate, nextOrders, nextDistinct, nextLimit, nextOffset, dtoType);
    }

    private static <E, V> void requireSameEntity(QinOrmEntity<E> entity, QinOrmColumn<E, V> column) {
        Objects.requireNonNull(entity, "entity");
        Objects.requireNonNull(column, "column");
        if (column.entityClass() != entity.javaType()) {
            throw new IllegalArgumentException("Column belongs to a different entity: " + column.propertyName());
        }
    }

    private record QueryPlan(String jpql, Map<String, Object> parameters) {
    }

    private enum SelectionKind {
        ENTITY,
        SCALAR,
        TUPLE,
        DTO
    }
}
