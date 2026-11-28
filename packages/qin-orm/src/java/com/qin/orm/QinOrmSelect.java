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
    private final List<QinOrmExpression<?, ?>> columns;
    private final QinOrmPredicate<?> predicate;
    private final List<JoinSpec> joins;
    private final List<QinOrmOrder<?>> orders;
    private final List<QinOrmExpression<?, ?>> groupBy;
    private final QinOrmPredicate<?> having;
    private final boolean distinct;
    private final int limit;
    private final int offset;
    private final Class<?> dtoType;

    private QinOrmSelect(
            QinOrmEntity<E> entity,
            EntityManager entityManager,
            SelectionKind selectionKind,
            Class<R> resultType,
            List<QinOrmExpression<?, ?>> columns,
            QinOrmPredicate<?> predicate,
            List<JoinSpec> joins,
            List<QinOrmOrder<?>> orders,
            List<QinOrmExpression<?, ?>> groupBy,
            QinOrmPredicate<?> having,
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
        this.groupBy = List.copyOf(groupBy);
        this.having = having;
        this.distinct = distinct;
        this.limit = limit;
        this.offset = offset;
        this.dtoType = dtoType;
    }

    static <E> QinOrmSelect<E, E> entity(QinOrmEntity<E> entity, EntityManager entityManager) {
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.ENTITY, entity.javaType(),
                List.of(), null, List.of(), List.of(), List.of(), null, false, -1, 0, null);
    }

    static <E, V> QinOrmSelect<E, V> scalar(
            QinOrmEntity<E> entity,
            EntityManager entityManager,
            QinOrmExpression<E, V> column) {
        requireSameEntity(entity, column);
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.SCALAR, column.propertyType(),
                List.of(column), null, List.of(), List.of(), List.of(), null, false, -1, 0, null);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    static <E> QinOrmSelect<E, Object[]> tuple(
            QinOrmEntity<E> entity,
            EntityManager entityManager,
            QinOrmExpression<?, ?> first,
            QinOrmExpression<?, ?> second,
            QinOrmExpression<?, ?>... remaining) {
        List<QinOrmExpression<?, ?>> columns = new ArrayList<>();
        columns.add(Objects.requireNonNull(first, "first"));
        columns.add(Objects.requireNonNull(second, "second"));
        for (QinOrmExpression<?, ?> expression : remaining) {
            columns.add(Objects.requireNonNull(expression, "expression"));
        }
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.TUPLE, Object[].class,
                columns, null, List.of(), List.of(), List.of(), null, false, -1, 0, null);
    }

    public <D> QinOrmSelect<E, D> as(Class<D> dtoType) {
        if (selectionKind != SelectionKind.TUPLE) {
            throw new IllegalStateException("DTO projection requires at least two selected properties");
        }
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.DTO, dtoType,
                columns, predicate, joins, orders, groupBy, having, distinct, limit, offset,
                Objects.requireNonNull(dtoType, "dtoType"));
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public final QinOrmSelect<E, Object[]> project(QinOrmExpression<?, ?>... selectedExpressions) {
        Objects.requireNonNull(selectedExpressions, "selectedExpressions");
        if (selectedExpressions.length == 0) {
            throw new IllegalArgumentException("project requires at least one property");
        }
        List<QinOrmExpression<?, ?>> nextColumns = new ArrayList<>(selectedExpressions.length);
        Set<String> names = new HashSet<>();
        for (QinOrmExpression<?, ?> expression : selectedExpressions) {
            Objects.requireNonNull(expression, "expression");
            if (!names.add(expression.signature())) {
                throw new IllegalArgumentException("Duplicate selected expression: " + expression.signature());
            }
            nextColumns.add(expression);
        }
        return new QinOrmSelect<>(entity, entityManager, SelectionKind.TUPLE, Object[].class,
                nextColumns, predicate, joins, orders, groupBy, having, distinct, limit, offset, null);
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
        return copy(predicate, joins, ordersForSort(sort), distinct, limit, offset);
    }

    public QinOrmSelect<E, R> distinct() {
        return copy(predicate, joins, orders, true, limit, offset);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public final QinOrmSelect<E, R> groupBy(QinOrmExpression<?, ?>... expressions) {
        Objects.requireNonNull(expressions, "expressions");
        if (expressions.length == 0) {
            throw new IllegalArgumentException("groupBy requires at least one expression");
        }
        List<QinOrmExpression<?, ?>> combined = new ArrayList<>(groupBy);
        for (QinOrmExpression<?, ?> expression : expressions) {
            Objects.requireNonNull(expression, "expression");
            if (expression instanceof QinOrmAggregate<?, ?>) {
                throw new IllegalArgumentException("groupBy cannot contain aggregate expressions");
            }
            combined.add(expression);
        }
        return copy(predicate, joins, orders, combined, having, distinct, limit, offset);
    }

    public QinOrmSelect<E, R> having(QinOrmPredicate<?> nextPredicate) {
        Objects.requireNonNull(nextPredicate, "nextPredicate");
        QinOrmPredicate<?> combined = having == null ? nextPredicate : having.and(nextPredicate);
        return copy(predicate, joins, orders, groupBy, combined, distinct, limit, offset);
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
        requireUngroupedCount();
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
        QinOrmJpqlContext context = new QinOrmJpqlContext();
        StringBuilder jpql = new StringBuilder("select count(distinct ").append(ROOT_ALIAS).append(")");
        appendFrom(jpql, jpaEntities, context);
        appendWhere(jpql, context);
        appendGroupBy(jpql);
        appendHaving(jpql, context);
        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
        bind(query, context.parameters());
        return query.getSingleResult();
    }

    public long countDistinct(QinOrmExpression<?, ?> expression) {
        Objects.requireNonNull(expression, "expression");
        requireUngroupedCount();
        if (expression instanceof QinOrmAggregate<?, ?>) {
            throw new IllegalArgumentException("countDistinct requires a scalar property expression");
        }
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
        validateExpression(jpaEntities, expression);
        QinOrmJpqlContext context = new QinOrmJpqlContext();
        StringBuilder jpql = new StringBuilder("select count(distinct ")
                .append(expression.render(this::aliasFor)).append(')');
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
        appendGroupBy(jpql);
        appendHaving(jpql, context);
        TypedQuery<Integer> query = entityManager.createQuery(jpql.toString(), Integer.class).setMaxResults(1);
        bind(query, context.parameters());
        return !query.getResultList().isEmpty();
    }

    public Page<R> page(Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable");
        requireUngroupedCount();
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
        if (pageable.isUnpaged()) {
            List<R> content = all();
            return new PageImpl<>(content, pageable, content.size());
        }
        requirePageCountShape();
        List<QinOrmOrder<?>> pageOrders = ordersForSort(pageable.getSort());
        validateOrders(jpaEntities, pageOrders);
        QueryPlan plan = render(jpaEntities, new QinOrmJpqlContext(), pageOrders);
        TypedQuery<R> query = createQuery(plan);
        applyRange(query, pageable.getOffset(), pageable.getPageSize());
        List<R> content = query.getResultList();
        return new PageImpl<>(content, pageable, countPageRows());
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
            case SCALAR -> jpql.append(columns.get(0).render(this::aliasFor));
            case TUPLE -> appendColumnSelection(jpql);
            case DTO -> appendDtoSelection(jpql);
        }
        appendFrom(jpql, jpaEntities, context);
        appendWhere(jpql, context);
        appendGroupBy(jpql);
        appendHaving(jpql, context);
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
            jpql.append(columns.get(index).render(this::aliasFor));
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

    private void appendGroupBy(StringBuilder jpql) {
        if (groupBy.isEmpty()) {
            return;
        }
        jpql.append(" group by ");
        for (int index = 0; index < groupBy.size(); index++) {
            if (index > 0) {
                jpql.append(", ");
            }
            jpql.append(groupBy.get(index).render(this::aliasFor));
        }
    }

    private void appendHaving(StringBuilder jpql, QinOrmJpqlContext context) {
        if (having != null) {
            jpql.append(" having ").append(having.render(context, this::aliasFor));
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
            String path = aliasFor(order.entityClass()) + "." + order.propertyName();
            jpql.append(order.ignoreCase() ? "lower(" + path + ")" : path)
                    .append(order.ascending() ? " asc" : " desc");
        }
    }

    private List<QinOrmOrder<?>> ordersForSort(Sort sort) {
        if (sort.isUnsorted()) {
            return orders;
        }
        List<QinOrmOrder<?>> combined = new ArrayList<>(orders);
        for (Sort.Order order : sort) {
            if (order.getNullHandling() != Sort.NullHandling.NATIVE) {
                throw new UnsupportedOperationException("Explicit null precedence is not supported");
            }
            String property = QinOrmEntity.requireIdentifier(order.getProperty(), "sort property");
            combined.add(new QinOrmOrder<>(entity.javaType(), property, order.isAscending(), order.isIgnoreCase()));
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
        for (QinOrmExpression<?, ?> expression : columns) {
            validateExpression(jpaEntities, expression);
        }
        if (predicate != null) {
            for (QinOrmPredicate.PropertyReference reference : predicate.propertyReferences()) {
                EntityType<?> owner = requireQueryEntity(jpaEntities, reference.entityType(), reference.propertyName());
                validateProperty(owner, reference.propertyName());
            }
        }
        for (QinOrmExpression<?, ?> expression : groupBy) {
            validateExpression(jpaEntities, expression);
        }
        if (having != null) {
            for (QinOrmPredicate.PropertyReference reference : having.propertyReferences()) {
                EntityType<?> owner = requireQueryEntity(jpaEntities, reference.entityType(), reference.propertyName());
                validateProperty(owner, reference.propertyName());
            }
        }
        for (QinOrmOrder<?> order : orders) {
            EntityType<?> owner = requireQueryEntity(jpaEntities, order.entityClass(), order.propertyName());
            validateOrder(owner, order);
        }
        return jpaEntities;
    }

    private void validateExpression(
            Map<Class<?>, EntityType<?>> jpaEntities,
            QinOrmExpression<?, ?> expression) {
        if (expression instanceof QinOrmAggregate<?, ?> aggregate) {
            validateExpression(jpaEntities, aggregate.argument());
            return;
        }
        for (QinOrmPredicate.PropertyReference reference : expression.propertyReferences()) {
            EntityType<?> owner = requireQueryEntity(jpaEntities, reference.entityType(), reference.propertyName());
            Class<?> mappedType = validateProperty(owner, reference.propertyName());
            if (expression instanceof QinOrmColumn<?, ?> column && !mappedType.equals(column.propertyType())) {
                throw new IllegalArgumentException("Column type mismatch for " + column.propertyName()
                        + ": mapped type is " + mappedType.getTypeName()
                        + " but descriptor declares " + column.propertyType().getTypeName());
            }
        }
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
            validateOrder(owner, order);
        }
    }

    private void validateOrder(EntityType<?> owner, QinOrmOrder<?> order) {
        Class<?> propertyType = validateProperty(owner, order.propertyName());
        if (order.ignoreCase() && propertyType != String.class) {
            throw new IllegalArgumentException("ignoreCase sorting requires a string property: "
                    + order.propertyName());
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

    private void requireUngroupedCount() {
        if (!groupBy.isEmpty() || having != null) {
            throw new UnsupportedOperationException("count and Page are not supported for grouped queries");
        }
    }

    private void requirePageCountShape() {
        if (columns.stream().anyMatch(QinOrmAggregate.class::isInstance)) {
            throw new UnsupportedOperationException("Page is not supported for aggregate projections; use Slice");
        }
        if (distinct && selectionKind != SelectionKind.ENTITY && columns.size() > 1) {
            throw new UnsupportedOperationException(
                    "Page is not supported for distinct multi-expression projections; use Slice");
        }
    }

    private long countPageRows() {
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
        QinOrmJpqlContext context = new QinOrmJpqlContext();
        String countExpression;
        if (selectionKind == SelectionKind.ENTITY && distinct) {
            countExpression = "count(distinct " + ROOT_ALIAS + ")";
        } else if (distinct && selectionKind != SelectionKind.ENTITY) {
            countExpression = "count(distinct " + columns.get(0).render(this::aliasFor) + ")";
        } else {
            countExpression = "count(" + ROOT_ALIAS + ")";
        }
        StringBuilder jpql = new StringBuilder("select ").append(countExpression);
        appendFrom(jpql, jpaEntities, context);
        appendWhere(jpql, context);
        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
        bind(query, context.parameters());
        return query.getSingleResult();
    }

    private QinOrmSelect<E, R> copy(
            QinOrmPredicate<?> nextPredicate,
            List<JoinSpec> nextJoins,
            List<QinOrmOrder<?>> nextOrders,
            boolean nextDistinct,
            int nextLimit,
            int nextOffset) {
        return copy(nextPredicate, nextJoins, nextOrders, groupBy, having,
                nextDistinct, nextLimit, nextOffset);
    }

    private QinOrmSelect<E, R> copy(
            QinOrmPredicate<?> nextPredicate,
            List<JoinSpec> nextJoins,
            List<QinOrmOrder<?>> nextOrders,
            List<QinOrmExpression<?, ?>> nextGroupBy,
            QinOrmPredicate<?> nextHaving,
            boolean nextDistinct,
            int nextLimit,
            int nextOffset) {
        return new QinOrmSelect<>(entity, entityManager, selectionKind, resultType, columns,
                nextPredicate, nextJoins, nextOrders, nextGroupBy, nextHaving,
                nextDistinct, nextLimit, nextOffset, dtoType);
    }

    private static <E, V> void requireSameEntity(QinOrmEntity<E> entity, QinOrmExpression<E, V> column) {
        Objects.requireNonNull(entity, "entity");
        Objects.requireNonNull(column, "column");
        if (column.entityClass() != entity.javaType()) {
            throw new IllegalArgumentException("Expression belongs to a different entity: " + column.signature());
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
