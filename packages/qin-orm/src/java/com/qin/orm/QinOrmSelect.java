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
import java.util.LinkedHashMap;
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
    private final List<QinOrmColumn<?, ?>> columns;
    private final QinOrmPredicate<?> predicate;
    private final List<JoinSpec> joins;
    private final List<QinOrmOrder<?>> orders;
    private final boolean distinct;
    private final int limit;
    private final int offset;
    private final Class<?> dtoType;

    private QinOrmSelect(
            QinOrmEntity<E> entity,
            EntityManager entityManager,
            SelectionKind selectionKind,
            Class<R> resultType,
            List<QinOrmColumn<?, ?>> columns,
            QinOrmPredicate<?> predicate,
            List<JoinSpec> joins,
            List<QinOrmOrder<?>> orders,
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
        this.joins = List.copyOf(joins);
        this.orders = List.copyOf(orders);
        this.distinct = distinct;
        this.limit = limit;
        this.offset = offset;
        this.dtoType = dtoType;
    }

    static <E> QinOrmSelect<E, E> entity(QinOrmEntity<E> entity, EntityManager entityManager) {
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.ENTITY, entity.javaType(),
                List.of(), null, List.of(), List.of(), false, -1, 0, null);
    }

    static <E, V> QinOrmSelect<E, V> scalar(
            QinOrmEntity<E> entity,
            EntityManager entityManager,
            QinOrmColumn<E, V> column) {
        requireSameEntity(entity, column);
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.SCALAR, column.propertyType(),
                List.of(column), null, List.of(), List.of(), false, -1, 0, null);
    }

    @SafeVarargs
    static <E> QinOrmSelect<E, Object[]> tuple(
            QinOrmEntity<E> entity,
            EntityManager entityManager,
            QinOrmColumn<E, ?> first,
            QinOrmColumn<E, ?> second,
            QinOrmColumn<E, ?>... remaining) {
        List<QinOrmColumn<?, ?>> columns = new ArrayList<>();
        columns.add(Objects.requireNonNull(first, "first"));
        columns.add(Objects.requireNonNull(second, "second"));
        for (QinOrmColumn<E, ?> column : remaining) {
            columns.add(Objects.requireNonNull(column, "column"));
        }
        Set<String> selectedNames = new HashSet<>();
        for (QinOrmColumn<?, ?> column : columns) {
            if (column.entityClass() != entity.javaType()) {
                throw new IllegalArgumentException("Selected column belongs to a different entity: "
                        + column.propertyName());
            }
            if (!selectedNames.add(column.propertyName())) {
                throw new IllegalArgumentException("Duplicate selected entity property: " + column.propertyName());
            }
        }
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.TUPLE, Object[].class,
                columns, null, List.of(), List.of(), false, -1, 0, null);
    }

    public <D> QinOrmSelect<E, D> as(Class<D> dtoType) {
        if (selectionKind != SelectionKind.TUPLE) {
            throw new IllegalStateException("DTO projection requires at least two selected properties");
        }
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.DTO, dtoType,
                columns, predicate, joins, orders, distinct, limit, offset,
                Objects.requireNonNull(dtoType, "dtoType"));
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public final QinOrmSelect<E, Object[]> project(QinOrmColumn<?, ?>... selectedColumns) {
        Objects.requireNonNull(selectedColumns, "selectedColumns");
        if (selectedColumns.length == 0) {
            throw new IllegalArgumentException("project requires at least one property");
        }
        List<QinOrmColumn<?, ?>> nextColumns = new ArrayList<>(selectedColumns.length);
        Set<String> names = new HashSet<>();
        for (QinOrmColumn<?, ?> column : selectedColumns) {
            Objects.requireNonNull(column, "column");
            if (!names.add(column.entityClass().getName() + ":" + column.propertyName())) {
                throw new IllegalArgumentException("Duplicate selected property: " + column.propertyName());
            }
            nextColumns.add(column);
        }
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.TUPLE, Object[].class,
                nextColumns, predicate, joins, orders, distinct, limit, offset, null);
    }

    public <T> QinOrmSelect<E, R> join(QinOrmEntity<T> target, QinOrmPredicate<?> onPredicate) {
        return addJoin(target, onPredicate, JoinKind.INNER);
    }

    public <T> QinOrmSelect<E, R> leftJoin(QinOrmEntity<T> target, QinOrmPredicate<?> onPredicate) {
        return addJoin(target, onPredicate, JoinKind.LEFT);
    }

    public QinOrmSelect<E, R> where(QinOrmPredicate<?> nextPredicate) {
        Objects.requireNonNull(nextPredicate, "nextPredicate");
        QinOrmPredicate<?> combined = predicate == null ? nextPredicate : predicate.and(nextPredicate);
        return copy(combined, joins, orders, distinct, limit, offset);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public final QinOrmSelect<E, R> orderBy(QinOrmOrder<?>... nextOrders) {
        Objects.requireNonNull(nextOrders, "orders");
        List<QinOrmOrder<?>> combined = new ArrayList<>(orders);
        for (QinOrmOrder<?> order : nextOrders) {
            Objects.requireNonNull(order, "order");
            combined.add(order);
        }
        return copy(predicate, joins, combined, distinct, limit, offset);
    }

    public QinOrmSelect<E, R> orderBy(Sort sort) {
        Objects.requireNonNull(sort, "sort");
        List<QinOrmOrder<?>> combined = new ArrayList<>(orders);
        for (Sort.Order order : sort) {
            String property = QinOrmEntity.requireIdentifier(order.getProperty(), "sort property");
            combined.add(new QinOrmOrder<>(entity.column(property, Object.class), order.isAscending()));
        }
        return copy(predicate, joins, combined, distinct, limit, offset);
    }

    public QinOrmSelect<E, R> distinct() {
        return copy(predicate, joins, orders, true, limit, offset);
    }

    public QinOrmSelect<E, R> limit(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be greater than zero");
        }
        return copy(predicate, joins, orders, distinct, limit, offset);
    }

    public QinOrmSelect<E, R> offset(int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
        return copy(predicate, joins, orders, distinct, limit, offset);
    }

    public String toJpql() {
        return render(resolveQueryEntities(), new QinOrmJpqlContext(), orders).jpql();
    }

    public List<R> all() {
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
        QueryPlan plan = render(jpaEntities, new QinOrmJpqlContext(), orders);
        TypedQuery<R> query = createQuery(plan);
        applyRange(query, offset, limit);
        return query.getResultList();
    }

    public R one() {
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
        QueryPlan plan = render(jpaEntities, new QinOrmJpqlContext(), orders);
        TypedQuery<R> query = createQuery(plan);
        applyRange(query, offset, limit > 0 ? Math.min(limit, 2) : 2);
        List<R> results = query.getResultList();
        if (results.size() > 1) {
            throw new NonUniqueResultException("Expected at most one result but query returned multiple results");
        }
        return results.isEmpty() ? null : results.get(0);
    }

    public R first() {
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
        QueryPlan plan = render(jpaEntities, new QinOrmJpqlContext(), orders);
        TypedQuery<R> query = createQuery(plan);
        applyRange(query, offset, 1);
        List<R> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public long count() {
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
        QinOrmJpqlContext context = new QinOrmJpqlContext();
        StringBuilder jpql = new StringBuilder("select count(distinct ").append(ROOT_ALIAS).append(")");
        appendFrom(jpql, jpaEntities, context);
        appendWhere(jpql, context);
        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
        bind(query, context.parameters());
        return query.getSingleResult();
    }

    public boolean exists() {
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
        QinOrmJpqlContext context = new QinOrmJpqlContext();
        StringBuilder jpql = new StringBuilder("select 1");
        appendFrom(jpql, jpaEntities, context);
        appendWhere(jpql, context);
        TypedQuery<Integer> query = entityManager.createQuery(jpql.toString(), Integer.class).setMaxResults(1);
        bind(query, context.parameters());
        return !query.getResultList().isEmpty();
    }

    public Page<R> page(Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable");
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
        if (pageable.isUnpaged()) {
            List<R> content = all();
            return new PageImpl<>(content, pageable, content.size());
        }
        List<QinOrmOrder<?>> pageOrders = ordersForSort(pageable.getSort());
        validateOrders(jpaEntities, pageOrders);
        QueryPlan plan = render(jpaEntities, new QinOrmJpqlContext(), pageOrders);
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
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
        List<QinOrmOrder<?>> pageOrders = ordersForSort(pageable.getSort());
        validateOrders(jpaEntities, pageOrders);
        QueryPlan plan = render(jpaEntities, new QinOrmJpqlContext(), pageOrders);
        TypedQuery<R> query = createQuery(plan);
        applyRange(query, pageable.getOffset(), pageable.getPageSize() + 1);
        List<R> results = query.getResultList();
        boolean hasNext = results.size() > pageable.getPageSize();
        List<R> content = hasNext ? results.subList(0, pageable.getPageSize()) : results;
        return new SliceImpl<>(content, pageable, hasNext);
    }

    private QueryPlan render(
            Map<Class<?>, EntityType<?>> jpaEntities,
            QinOrmJpqlContext context,
            List<QinOrmOrder<?>> queryOrders) {
        StringBuilder jpql = new StringBuilder("select ");
        if (distinct) {
            jpql.append("distinct ");
        }
        switch (selectionKind) {
            case ENTITY -> jpql.append(aliasFor(entity.javaType()));
            case SCALAR -> jpql.append(path(columns.get(0)));
            case TUPLE -> appendColumnSelection(jpql);
            case DTO -> appendDtoSelection(jpql);
        }
        appendFrom(jpql, jpaEntities, context);
        appendWhere(jpql, context);
        appendOrderBy(jpql, queryOrders);
        return new QueryPlan(jpql.toString(), context.parameters());
    }

    private void appendFrom(
            StringBuilder jpql,
            Map<Class<?>, EntityType<?>> jpaEntities,
            QinOrmJpqlContext context) {
        jpql.append(" from ").append(jpaEntities.get(entity.javaType()).getName()).append(' ')
                .append(aliasFor(entity.javaType()));
        for (JoinSpec join : joins) {
            jpql.append(join.kind() == JoinKind.LEFT ? " left join " : " join ");
            jpql.append(jpaEntities.get(join.target().javaType()).getName()).append(' ')
                    .append(join.alias()).append(" on ")
                    .append(join.onPredicate().render(context, this::aliasFor));
        }
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
            jpql.append(" where ").append(predicate.render(context, this::aliasFor));
        }
    }

    private void appendOrderBy(StringBuilder jpql, List<QinOrmOrder<?>> queryOrders) {
        if (queryOrders.isEmpty()) {
            return;
        }
        jpql.append(" order by ");
        for (int index = 0; index < queryOrders.size(); index++) {
            if (index > 0) {
                jpql.append(", ");
            }
            QinOrmOrder<?> order = queryOrders.get(index);
            jpql.append(aliasFor(order.entityClass())).append('.').append(order.propertyName())
                    .append(order.ascending() ? " asc" : " desc");
        }
    }

    private List<QinOrmOrder<?>> ordersForSort(Sort sort) {
        if (sort.isUnsorted()) {
            return orders;
        }
        List<QinOrmOrder<?>> combined = new ArrayList<>(orders);
        for (Sort.Order order : sort) {
            String property = QinOrmEntity.requireIdentifier(order.getProperty(), "sort property");
            combined.add(new QinOrmOrder<>(entity.javaType(), property, order.isAscending()));
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

    private Map<Class<?>, EntityType<?>> resolveQueryEntities() {
        Map<Class<?>, EntityType<?>> jpaEntities = new LinkedHashMap<>();
        jpaEntities.put(entity.javaType(), managedEntity(entity.javaType()));
        Set<Class<?>> joinedTypes = new HashSet<>();
        for (JoinSpec join : joins) {
            Class<?> targetType = join.target().javaType();
            if (!joinedTypes.add(targetType)) {
                throw new IllegalArgumentException("Joining the same entity type more than once is not supported: "
                        + targetType.getName());
            }
            jpaEntities.put(targetType, managedEntity(targetType));
            for (QinOrmPredicate.PropertyReference reference : join.onPredicate().propertyReferences()) {
                EntityType<?> owner = requireQueryEntity(jpaEntities,
                        reference.entityType(), reference.propertyName());
                validateProperty(owner, reference.propertyName());
            }
        }
        for (QinOrmColumn<?, ?> column : columns) {
            EntityType<?> owner = requireQueryEntity(jpaEntities, column.entityClass(), column.propertyName());
            Class<?> mappedType = validateProperty(owner, column.propertyName());
            if (!mappedType.equals(column.propertyType())) {
                throw new IllegalArgumentException("Column type mismatch for " + column.propertyName()
                        + ": mapped type is " + mappedType.getTypeName()
                        + " but descriptor declares " + column.propertyType().getTypeName());
            }
        }
        if (predicate != null) {
            for (QinOrmPredicate.PropertyReference reference : predicate.propertyReferences()) {
                EntityType<?> owner = requireQueryEntity(jpaEntities, reference.entityType(), reference.propertyName());
                validateProperty(owner, reference.propertyName());
            }
        }
        for (QinOrmOrder<?> order : orders) {
            EntityType<?> owner = requireQueryEntity(jpaEntities, order.entityClass(), order.propertyName());
            validateProperty(owner, order.propertyName());
        }
        return jpaEntities;
    }

    private EntityType<?> managedEntity(Class<?> javaType) {
        try {
            return entityManager.getMetamodel().entity(javaType);
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("Class is not a managed JPA entity: " + javaType.getName(), error);
        }
    }

    private void validateOrders(Map<Class<?>, EntityType<?>> jpaEntities, List<QinOrmOrder<?>> queryOrders) {
        for (QinOrmOrder<?> order : queryOrders) {
            EntityType<?> owner = requireQueryEntity(jpaEntities, order.entityClass(), order.propertyName());
            validateProperty(owner, order.propertyName());
        }
    }

    private EntityType<?> requireQueryEntity(
            Map<Class<?>, EntityType<?>> jpaEntities,
            Class<?> javaType,
            String propertyName) {
        EntityType<?> owner = jpaEntities.get(javaType);
        if (owner == null) {
            throw new IllegalArgumentException("Property " + propertyName + " belongs to an entity not joined in this query: "
                    + javaType.getName());
        }
        return owner;
    }

    private Class<?> validateProperty(EntityType<?> jpaEntity, String propertyName) {
        try {
            return jpaEntity.getAttribute(propertyName).getJavaType();
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("Unknown persistent property " + propertyName
                    + " on " + jpaEntity.getJavaType().getName(), error);
        }
    }

    private String aliasFor(Class<?> javaType) {
        if (javaType == entity.javaType()) {
            return ROOT_ALIAS;
        }
        for (JoinSpec join : joins) {
            if (join.target().javaType() == javaType) {
                return join.alias();
            }
        }
        throw new IllegalArgumentException("Entity is not joined in this query: " + javaType.getName());
    }

    private String path(QinOrmColumn<?, ?> column) {
        return aliasFor(column.entityClass()) + "." + column.propertyName();
    }

    private <T> QinOrmSelect<E, R> addJoin(
            QinOrmEntity<T> target,
            QinOrmPredicate<?> onPredicate,
            JoinKind kind) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(onPredicate, "onPredicate");
        if (!joins.isEmpty()) {
            throw new UnsupportedOperationException("Only one entity join is supported in this first release");
        }
        JoinSpec join = new JoinSpec(target, kind, "qin" + target.javaType().getSimpleName(), onPredicate);
        return copy(predicate, List.of(join), orders, distinct, limit, offset);
    }

    private QinOrmSelect<E, R> copy(
            QinOrmPredicate<?> nextPredicate,
            List<JoinSpec> nextJoins,
            List<QinOrmOrder<?>> nextOrders,
            boolean nextDistinct,
            int nextLimit,
            int nextOffset) {
        return new QinOrmSelect<>(entity, entityManager, selectionKind, resultType, columns,
                nextPredicate, nextJoins, nextOrders, nextDistinct, nextLimit, nextOffset, dtoType);
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

    private record JoinSpec(QinOrmEntity<?> target, JoinKind kind, String alias,
                            QinOrmPredicate<?> onPredicate) {
    }

    private enum JoinKind {
        INNER,
        LEFT
    }

    private enum SelectionKind {
        ENTITY,
        SCALAR,
        TUPLE,
        DTO
    }
}
