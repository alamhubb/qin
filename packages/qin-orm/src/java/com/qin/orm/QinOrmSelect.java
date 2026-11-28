package com.qin.orm;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
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

    public QinOrmSubquery<R> subquery() {
        return subquery(new QinOrmEntity<?>[0]);
    }

    public QinOrmSubquery<R> subquery(QinOrmEntity<?>... outerEntities) {
        if (!orders.isEmpty() || limit > 0 || offset > 0) {
            throw new IllegalStateException("JPQL subqueries cannot contain order, limit, or offset clauses");
        }
        if (selectionKind == SelectionKind.DTO
                || (selectionKind == SelectionKind.TUPLE && columns.size() != 1)) {
            throw new IllegalStateException("JPQL subqueries must select one expression");
        }
        Set<Class<?>> allowed = new HashSet<>();
        for (QinOrmEntity<?> outer : outerEntities) {
            Class<?> outerType = outer.javaType();
            if (outerType == entity.javaType()
                    || joins.stream().anyMatch(join -> join.target().javaType() == outerType)) {
                throw new IllegalArgumentException("An outer entity type also occurs in the subquery: "
                        + outerType.getName());
            }
            allowed.add(outerType);
        }
        return new QinOrmSubquery<>(this, allowed);
    }

    public QinOrmSetQuery<R> union(QinOrmSelect<?, R> other) {
        return QinOrmSetQuery.start(this, other, QinOrmSetQuery.Operator.UNION);
    }

    public QinOrmSetQuery<R> unionAll(QinOrmSelect<?, R> other) {
        return QinOrmSetQuery.start(this, other, QinOrmSetQuery.Operator.UNION_ALL);
    }

    public QinOrmSetQuery<R> intersect(QinOrmSelect<?, R> other) {
        return QinOrmSetQuery.start(this, other, QinOrmSetQuery.Operator.INTERSECT);
    }

    public QinOrmSetQuery<R> except(QinOrmSelect<?, R> other) {
        return QinOrmSetQuery.start(this, other, QinOrmSetQuery.Operator.EXCEPT);
    }

    jakarta.persistence.EntityManager entityManager() {
        return entityManager;
    }

    Class<R> resultType() {
        return resultType;
    }

    List<Class<?>> selectionTypes() {
        return switch (selectionKind) {
            case ENTITY -> List.of(entity.javaType());
            case SCALAR -> List.of(columns.get(0).propertyType());
            case TUPLE, DTO -> {
                List<Class<?>> types = new ArrayList<>(columns.size());
                for (QinOrmExpression<?, ?> expression : columns) {
                    types.add(expression.propertyType());
                }
                yield List.copyOf(types);
            }
        };
    }

    String renderSetOperand(QinOrmJpqlContext context) {
        if (!orders.isEmpty() || limit > 0 || offset > 0) {
            throw new UnsupportedOperationException("Set-operation operands cannot contain order, limit, or offset");
        }
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
        return render(jpaEntities, context, List.of()).jpql();
    }

    String renderSubquery(
            QinOrmJpqlContext context,
            java.util.function.Function<Class<?>, String> parentAliases) {
        String subqueryRootAlias = context.nextSubqueryAlias();
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities(parentAliases);
        return render(jpaEntities, context, orders, subqueryRootAlias, parentAliases).jpql();
    }

    Class<R> subqueryResultType() {
        if (selectionKind == SelectionKind.SCALAR) {
            return resultType;
        }
        if (selectionKind == SelectionKind.TUPLE && columns.size() == 1) {
            @SuppressWarnings("unchecked")
            Class<R> singleResultType = (Class<R>) columns.get(0).propertyType();
            return singleResultType;
        }
        throw new IllegalStateException("IN subqueries must select exactly one scalar expression");
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
        if (pageable.isUnpaged()) {
            List<R> content = orderBy(pageable.getSort()).all();
            return new PageImpl<>(content, pageable, content.size());
        }
        requirePageCountShape();
        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
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
            return new SliceImpl<>(orderBy(pageable.getSort()).all(), pageable, false);
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

    public Window<R> scroll(ScrollPosition position, int windowSize) {
        Objects.requireNonNull(position, "position");
        if (windowSize < 1 || windowSize == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("windowSize must be greater than zero");
        }
        if (selectionKind != SelectionKind.ENTITY || !groupBy.isEmpty() || having != null) {
            throw new UnsupportedOperationException("Scroll requires an ungrouped entity selection");
        }
        if (!joins.isEmpty() || limit > 0 || offset > 0) {
            throw new UnsupportedOperationException("Scroll cannot be combined with joins, limit, or offset");
        }

        Map<Class<?>, EntityType<?>> jpaEntities = resolveQueryEntities();
        boolean keyset = position instanceof KeysetScrollPosition;
        List<QinOrmOrder<?>> scrollOrders = stableScrollOrders(jpaEntities, keyset);
        long firstResult = 0;
        QinOrmPredicate<?> scrollPredicate = predicate;
        if (position instanceof OffsetScrollPosition offsetPosition) {
            firstResult = offsetPosition.isInitial() ? 0 : Math.addExact(offsetPosition.getOffset(), 1);
        } else if (keyset && !position.isInitial()) {
            if (((KeysetScrollPosition) position).scrollsBackward()) {
                throw new UnsupportedOperationException("Backward keyset scrolling is not supported");
            }
            scrollPredicate = combine(scrollPredicate,
                    keysetPredicate(scrollOrders, ((KeysetScrollPosition) position).getKeys()));
        } else if (!keyset) {
            throw new IllegalArgumentException("ScrollPosition must be offset-based or keyset-based");
        }

        QinOrmSelect<E, R> scrollQuery = copy(scrollPredicate, joins, scrollOrders,
                groupBy, having, distinct, limit, offset);
        QueryPlan plan = scrollQuery.render(jpaEntities, new QinOrmJpqlContext(), scrollOrders);
        TypedQuery<R> query = scrollQuery.createQuery(plan);
        applyRange(query, firstResult, windowSize + 1);
        List<R> results = query.getResultList();
        boolean hasNext = results.size() > windowSize;
        List<R> content = new ArrayList<>(hasNext ? results.subList(0, windowSize) : results);
        EntityType<?> rootType = jpaEntities.get(entity.javaType());
        List<String> keyProperties = scrollOrders.stream().map(QinOrmOrder::propertyName).toList();
        long windowOffset = firstResult;
        return Window.from(content, index -> {
            if (position instanceof OffsetScrollPosition) {
                return ScrollPosition.offset(windowOffset + index);
            }
            Map<String, Object> keys = new LinkedHashMap<>();
            for (String property : keyProperties) {
                Object value = readProperty(rootType, content.get(index), property);
                if (value == null) {
                    throw new IllegalStateException("Keyset scrolling does not support null sort values: " + property);
                }
                keys.put(property, value);
            }
            return ScrollPosition.forward(keys);
        }, hasNext);
    }

    private List<QinOrmOrder<?>> stableScrollOrders(Map<Class<?>, EntityType<?>> jpaEntities, boolean keyset) {
        EntityType<?> rootType = jpaEntities.get(entity.javaType());
        if (!rootType.hasSingleIdAttribute()) {
            throw new UnsupportedOperationException("Scrolling requires a single-field entity identifier");
        }
        var identifier = rootType.getId(rootType.getIdType().getJavaType());
        if (identifier.getPersistentAttributeType() != Attribute.PersistentAttributeType.BASIC) {
            throw new UnsupportedOperationException("Scrolling requires a basic scalar entity identifier");
        }
        String idProperty = identifier.getName();
        List<QinOrmOrder<?>> stableOrders = new ArrayList<>();
        QinOrmOrder<?> idOrder = null;
        for (QinOrmOrder<?> order : orders) {
            if (order.entityClass() != entity.javaType()) {
                throw new UnsupportedOperationException("Scroll ordering must use root entity properties");
            }
            if (keyset && (order.ignoreCase() || order.nullHandling() != Sort.NullHandling.NATIVE)) {
                throw new UnsupportedOperationException(
                        "Case folding and explicit null precedence are not supported for keyset scrolling");
            }
            Attribute<?, ?> attribute = rootType.getAttribute(order.propertyName());
            if (attribute.getPersistentAttributeType() != Attribute.PersistentAttributeType.BASIC) {
                throw new UnsupportedOperationException("Keyset sort keys must be basic scalar properties: "
                        + order.propertyName());
            }
            if (order.propertyName().equals(idProperty)) {
                idOrder = order;
            } else {
                stableOrders.add(order);
            }
        }
        stableOrders.add(idOrder == null
                ? new QinOrmOrder<>(entity.javaType(), idProperty, true)
                : idOrder);
        validateOrders(jpaEntities, stableOrders);
        return List.copyOf(stableOrders);
    }

    private QinOrmPredicate<?> keysetPredicate(
            List<QinOrmOrder<?>> scrollOrders,
            Map<String, Object> keys) {
        QinOrmPredicate<?> disjunction = null;
        QinOrmPredicate<?> equalPrefix = null;
        for (QinOrmOrder<?> order : scrollOrders) {
            if (!keys.containsKey(order.propertyName())) {
                throw new IllegalArgumentException("Keyset position is missing sort property: " + order.propertyName());
            }
            Object key = keys.get(order.propertyName());
            if (key == null) {
                throw new IllegalArgumentException("Keyset position cannot contain null sort values: "
                        + order.propertyName());
            }
            QinOrmColumn<E, Object> column = entity.column(order.propertyName(), Object.class);
            String operator = order.ascending() ? ">" : "<";
            QinOrmPredicate<?> comparison = QinOrmPredicate.compare(column, operator, key);
            QinOrmPredicate<?> branch = equalPrefix == null ? comparison : equalPrefix.and(comparison);
            disjunction = disjunction == null ? branch : disjunction.or(branch);
            QinOrmPredicate<?> equalsKey = QinOrmPredicate.compare(column, "=", key);
            equalPrefix = equalPrefix == null ? equalsKey : equalPrefix.and(equalsKey);
        }
        return Objects.requireNonNull(disjunction, "scrollOrders");
    }

    private static QinOrmPredicate<?> combine(
            QinOrmPredicate<?> first,
            QinOrmPredicate<?> second) {
        return first == null ? second : first.and(second);
    }

    private Object readProperty(EntityType<?> entityType, Object instance, String propertyName) {
        Attribute<?, ?> attribute = entityType.getAttribute(propertyName);
        Member member = attribute.getJavaMember();
        try {
            if (member instanceof Field field) {
                field.trySetAccessible();
                return field.get(instance);
            }
            if (member instanceof Method method) {
                method.trySetAccessible();
                return method.invoke(instance);
            }
        } catch (ReflectiveOperationException error) {
            throw new IllegalStateException("Cannot read scroll key property " + propertyName, error);
        }
        throw new IllegalArgumentException("Unsupported JPA member for scroll key: " + propertyName);
    }

    private QueryPlan render(
            Map<Class<?>, EntityType<?>> jpaEntities,
            QinOrmJpqlContext context,
            List<QinOrmOrder<?>> queryOrders) {
        return render(jpaEntities, context, queryOrders, ROOT_ALIAS, null);
    }

    private QueryPlan render(
            Map<Class<?>, EntityType<?>> jpaEntities,
            QinOrmJpqlContext context,
            List<QinOrmOrder<?>> queryOrders,
            String rootAlias,
            java.util.function.Function<Class<?>, String> parentAliases) {
        java.util.function.Function<Class<?>, String> aliasResolver =
                javaType -> aliasFor(javaType, rootAlias, parentAliases);
        StringBuilder jpql = new StringBuilder("select ");
        if (distinct) {
            jpql.append("distinct ");
        }
        switch (selectionKind) {
            case ENTITY -> jpql.append(rootAlias);
            case SCALAR -> jpql.append(columns.get(0).render(aliasResolver));
            case TUPLE -> appendColumnSelection(jpql, aliasResolver);
            case DTO -> appendDtoSelection(jpql, aliasResolver);
        }
        appendFrom(jpql, jpaEntities, context, rootAlias, aliasResolver);
        appendWhere(jpql, context, aliasResolver);
        appendGroupBy(jpql, aliasResolver);
        appendHaving(jpql, context, aliasResolver);
        appendOrderBy(jpql, queryOrders, aliasResolver);
        return new QueryPlan(jpql.toString(), context.parameters());
    }

    private void appendFrom(
            StringBuilder jpql,
            Map<Class<?>, EntityType<?>> jpaEntities,
            QinOrmJpqlContext context) {
        appendFrom(jpql, jpaEntities, context, ROOT_ALIAS, this::aliasFor);
    }

    private void appendFrom(
            StringBuilder jpql,
            Map<Class<?>, EntityType<?>> jpaEntities,
            QinOrmJpqlContext context,
            String rootAlias,
            java.util.function.Function<Class<?>, String> aliasResolver) {
        jpql.append(" from ").append(jpaEntities.get(entity.javaType()).getName()).append(' ')
                .append(rootAlias);
        for (JoinSpec join : joins) {
            jpql.append(join.kind() == JoinKind.LEFT ? " left join " : " join ");
            jpql.append(jpaEntities.get(join.target().javaType()).getName()).append(' ')
                    .append(aliasResolver.apply(join.target().javaType())).append(" on ")
                    .append(join.onPredicate().render(context, aliasResolver));
        }
    }

    private void appendColumnSelection(
            StringBuilder jpql,
            java.util.function.Function<Class<?>, String> aliasResolver) {
        for (int index = 0; index < columns.size(); index++) {
            if (index > 0) {
                jpql.append(", ");
            }
            jpql.append(columns.get(index).render(aliasResolver));
        }
    }

    private void appendDtoSelection(
            StringBuilder jpql,
            java.util.function.Function<Class<?>, String> aliasResolver) {
        if (dtoType.getCanonicalName() == null) {
            throw new IllegalArgumentException("DTO projection type must have a canonical class name");
        }
        jpql.append("new ").append(dtoType.getName()).append('(');
        appendColumnSelection(jpql, aliasResolver);
        jpql.append(')');
    }

    private void appendWhere(
            StringBuilder jpql,
            QinOrmJpqlContext context) {
        appendWhere(jpql, context, this::aliasFor);
    }

    private void appendWhere(
            StringBuilder jpql,
            QinOrmJpqlContext context,
            java.util.function.Function<Class<?>, String> aliasResolver) {
        if (predicate != null) {
            jpql.append(" where ").append(predicate.render(context, aliasResolver));
        }
    }

    private void appendGroupBy(
            StringBuilder jpql) {
        appendGroupBy(jpql, this::aliasFor);
    }

    private void appendGroupBy(
            StringBuilder jpql,
            java.util.function.Function<Class<?>, String> aliasResolver) {
        if (groupBy.isEmpty()) {
            return;
        }
        jpql.append(" group by ");
        for (int index = 0; index < groupBy.size(); index++) {
            if (index > 0) {
                jpql.append(", ");
            }
            jpql.append(groupBy.get(index).render(aliasResolver));
        }
    }

    private void appendHaving(
            StringBuilder jpql,
            QinOrmJpqlContext context) {
        appendHaving(jpql, context, this::aliasFor);
    }

    private void appendHaving(
            StringBuilder jpql,
            QinOrmJpqlContext context,
            java.util.function.Function<Class<?>, String> aliasResolver) {
        if (having != null) {
            jpql.append(" having ").append(having.render(context, aliasResolver));
        }
    }

    private void appendOrderBy(
            StringBuilder jpql,
            List<QinOrmOrder<?>> queryOrders) {
        appendOrderBy(jpql, queryOrders, this::aliasFor);
    }

    private void appendOrderBy(
            StringBuilder jpql,
            List<QinOrmOrder<?>> queryOrders,
            java.util.function.Function<Class<?>, String> aliasResolver) {
        if (queryOrders.isEmpty()) {
            return;
        }
        jpql.append(" order by ");
        for (int index = 0; index < queryOrders.size(); index++) {
            if (index > 0) {
                jpql.append(", ");
            }
            QinOrmOrder<?> order = queryOrders.get(index);
            String path = aliasResolver.apply(order.entityClass()) + "." + order.propertyName();
            jpql.append(order.ignoreCase() ? "lower(" + path + ")" : path)
                    .append(order.ascending() ? " asc" : " desc");
            switch (order.nullHandling()) {
                case NULLS_FIRST -> jpql.append(" nulls first");
                case NULLS_LAST -> jpql.append(" nulls last");
                case NATIVE -> { }
            }
        }
    }

    private List<QinOrmOrder<?>> ordersForSort(Sort sort) {
        if (sort.isUnsorted()) {
            return orders;
        }
        List<QinOrmOrder<?>> combined = new ArrayList<>(orders);
        for (Sort.Order order : sort) {
            String property = QinOrmEntity.requireIdentifier(order.getProperty(), "sort property");
            combined.add(new QinOrmOrder<>(entity.javaType(), property, order.isAscending(),
                    order.isIgnoreCase(), order.getNullHandling()));
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
        if (firstResult < 0 || firstResult > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("JPA offset exceeds the supported integer range");
        }
        query.setFirstResult((int) firstResult);
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }
    }

    private Map<Class<?>, EntityType<?>> resolveQueryEntities() {
        return resolveQueryEntities(null);
    }

    private Map<Class<?>, EntityType<?>> resolveQueryEntities(
            java.util.function.Function<Class<?>, String> parentAliases) {
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
                validatePropertyReference(jpaEntities, reference, parentAliases);
            }
        }
        for (QinOrmExpression<?, ?> expression : columns) {
            validateExpression(jpaEntities, expression, parentAliases);
        }
        if (predicate != null) {
            for (QinOrmPredicate.PropertyReference reference : predicate.propertyReferences()) {
                validatePropertyReference(jpaEntities, reference, parentAliases);
            }
        }
        for (QinOrmExpression<?, ?> expression : groupBy) {
            validateExpression(jpaEntities, expression, parentAliases);
        }
        if (having != null) {
            for (QinOrmPredicate.PropertyReference reference : having.propertyReferences()) {
                validatePropertyReference(jpaEntities, reference, parentAliases);
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
        validateExpression(jpaEntities, expression, null);
    }

    private void validateExpression(
            Map<Class<?>, EntityType<?>> jpaEntities,
            QinOrmExpression<?, ?> expression,
            java.util.function.Function<Class<?>, String> parentAliases) {
        if (expression instanceof QinOrmAggregate<?, ?> aggregate) {
            validateExpression(jpaEntities, aggregate.argument(), parentAliases);
            return;
        }
        for (QinOrmPredicate.PropertyReference reference : expression.propertyReferences()) {
            Class<?> mappedType = validatePropertyReference(jpaEntities, reference, parentAliases);
            if (expression instanceof QinOrmColumn<?, ?> column
                    && !QinOrmColumn.boxedType(mappedType).equals(column.propertyType())) {
                throw new IllegalArgumentException("Column type mismatch for " + column.propertyName()
                        + ": mapped type is " + mappedType.getTypeName()
                        + " but descriptor declares " + column.propertyType().getTypeName());
            }
        }
    }

    private Class<?> validatePropertyReference(
            Map<Class<?>, EntityType<?>> jpaEntities,
            QinOrmPredicate.PropertyReference reference,
            java.util.function.Function<Class<?>, String> parentAliases) {
        EntityType<?> owner = jpaEntities.get(reference.entityType());
        if (owner == null) {
            if (parentAliases == null) {
                owner = requireQueryEntity(jpaEntities, reference.entityType(), reference.propertyName());
            } else {
                parentAliases.apply(reference.entityType());
                owner = managedEntity(reference.entityType());
            }
        }
        return validateProperty(owner, reference.propertyName());
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
        Attribute<?, ?> attribute;
        try {
            attribute = jpaEntity.getAttribute(propertyName);
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("Unknown persistent property " + propertyName
                    + " on " + jpaEntity.getJavaType().getName(), error);
        }
        if (attribute.getPersistentAttributeType() != Attribute.PersistentAttributeType.BASIC) {
            throw new IllegalArgumentException("Qin ORM requires a basic scalar property: " + propertyName);
        }
        return attribute.getJavaType();
    }

    private String aliasFor(Class<?> javaType) {
        return aliasFor(javaType, ROOT_ALIAS, null);
    }

    private String aliasFor(
            Class<?> javaType,
            String rootAlias,
            java.util.function.Function<Class<?>, String> parentAliases) {
        if (javaType == entity.javaType()) {
            return rootAlias;
        }
        for (JoinSpec join : joins) {
            if (join.target().javaType() == javaType) {
                return rootAlias.equals(ROOT_ALIAS) ? join.alias() : rootAlias + "_" + join.alias();
            }
        }
        if (parentAliases != null) {
            return parentAliases.apply(javaType);
        }
        throw new IllegalArgumentException("Entity is not joined in this query: " + javaType.getName());
    }

    private <T> QinOrmSelect<E, R> addJoin(
            QinOrmEntity<T> target,
            QinOrmPredicate<?> onPredicate,
            JoinKind kind) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(onPredicate, "onPredicate");
        if (target.javaType() == entity.javaType()
                || joins.stream().anyMatch(join -> join.target().javaType() == target.javaType())) {
            throw new IllegalArgumentException("The same entity type cannot be joined more than once: "
                    + target.javaType().getName());
        }
        Set<Class<?>> sources = new HashSet<>();
        sources.add(entity.javaType());
        for (JoinSpec join : joins) {
            sources.add(join.target().javaType());
        }
        if (!onPredicate.connects(target.javaType(), sources)) {
            throw new IllegalArgumentException("Join ON requires equality between a target field and an existing entity field");
        }
        List<JoinSpec> nextJoins = new ArrayList<>(joins);
        nextJoins.add(new JoinSpec(target, kind, joinAlias(target.javaType()), onPredicate));
        return copy(predicate, nextJoins, orders, distinct, limit, offset);
    }

    private String joinAlias(Class<?> targetType) {
        String preferred = "qin" + targetType.getSimpleName();
        if (!preferred.equals(ROOT_ALIAS)
                && joins.stream().noneMatch(join -> join.alias().equals(preferred))) {
            return preferred;
        }
        String qualified = targetType.getName().replaceAll("[^A-Za-z0-9_]", "_");
        String base = "qinJoin" + qualified;
        String alias = base;
        int suffix = 2;
        while (alias.equals(ROOT_ALIAS) || containsJoinAlias(alias)) {
            alias = base + suffix++;
        }
        return alias;
    }

    private boolean containsJoinAlias(String alias) {
        return joins.stream().anyMatch(join -> join.alias().equals(alias));
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
            String path = columns.getFirst().render(this::aliasFor);
            countExpression = "count(distinct " + path + ") + case when count(" + ROOT_ALIAS
                    + ") > count(" + path + ") then 1 else 0 end";
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
