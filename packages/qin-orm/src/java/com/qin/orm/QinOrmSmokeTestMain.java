package com.qin.orm;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public final class QinOrmSmokeTestMain {
    private QinOrmSmokeTestMain() {
    }

    public static void main(String[] args) {
        UserModel User = new UserModel();
        QinOrmColumn<UserEntity, Long> id = User.id;
        QinOrmColumn<UserEntity, String> name = User.name;
        QinOrmSession db = QinOrm.session(fixture(List.of(new UserEntity()), 7L, false).entityManager());

        JpaFixture entityFixture = fixture(List.of(new UserEntity()), 7L, false);
        QinOrmSelect<UserEntity, UserEntity> entityQuery = QinOrm.session(entityFixture.entityManager())
                .select(User)
                .where(User.id.eq(12L).and(User.name.like("A%")))
                .orderBy(User.name.asc());
        requireEquals("select qinEntity from AccountUser qinEntity where (qinEntity.id = :qin_p0) and (qinEntity.name like :qin_p1) order by qinEntity.name asc",
                entityQuery.toJpql());
        requireEquals("select qinEntity from AccountUser qinEntity order by lower(qinEntity.name) asc",
                QinOrm.session(fixture(List.of(), 0L, false).entityManager()).select(User)
                        .orderBy(Sort.by(Sort.Order.asc("name").ignoreCase())).toJpql());
        expectFailure(IllegalArgumentException.class,
                () -> QinOrm.session(fixture(List.of(), 0L, false).entityManager()).select(User)
                        .orderBy(Sort.by(Sort.Order.asc("amount").ignoreCase())).toJpql());
        requireEquals("select qinEntity from AccountUser qinEntity order by qinEntity.name asc nulls first",
                QinOrm.session(fixture(List.of(), 0L, false).entityManager()).select(User)
                        .orderBy(Sort.by(Sort.Order.asc("name").nullsFirst())).toJpql());
        requireEquals(1, entityQuery.all().size());
        requireEquals(Map.of("qin_p0", 12L, "qin_p1", "A%"), entityFixture.parameters().get());

        JpaFixture scalarFixture = fixture(List.of(5L, 7L), 7L, false);
        QinOrmSelect<UserEntity, Long> scalarQuery = QinOrm.session(scalarFixture.entityManager())
                .select(User, User.id)
                .where(User.name.eq("Ada"));
        requireEquals("select qinEntity.id from AccountUser qinEntity where qinEntity.name = :qin_p0",
                scalarQuery.toJpql());
        requireEquals(List.of(5L, 7L), scalarQuery.all());
        requireEquals("Ada", scalarFixture.parameters().get().get("qin_p0"));

        JpaFixture nativeFixture = fixture(List.of(5L, 7L), 2L, false);
        List<Long> nativeIds = QinOrm.session(nativeFixture.entityManager())
                .nativeQuery("select id from account_user where status = :status", Long.class)
                .bind("status", "active")
                .all();
        requireEquals(List.of(5L, 7L), nativeIds);
        requireEquals("select id from account_user where status = :status", nativeFixture.jpql().get());
        requireEquals("active", nativeFixture.parameters().get().get("status"));

        JpaFixture setFixture = fixture(List.of(5L, 7L), 2L, false);
        QinOrmSession setSession = QinOrm.session(setFixture.entityManager());
        QinOrmSelect<UserEntity, Long> firstSetOperand = setSession.select(User, User.id)
                .where(User.id.eq(5L));
        QinOrmSelect<UserEntity, Long> secondSetOperand = setSession.select(User, User.id)
                .where(User.id.eq(7L));
        QinOrmSetQuery<Long> unionQuery = firstSetOperand.union(secondSetOperand);
        requireEquals("select qinEntity.id from AccountUser qinEntity where qinEntity.id = :qin_p0 "
                        + "union select qinEntity.id from AccountUser qinEntity where qinEntity.id = :qin_p1",
                unionQuery.toJpql());
        requireEquals(List.of(5L, 7L), unionQuery.all());
        requireEquals(Map.of("qin_p0", 5L, "qin_p1", 7L), setFixture.parameters().get());
        require(firstSetOperand.unionAll(secondSetOperand).toJpql().contains(" union all "),
                "unionAll should serialize with its duplicate-preserving operator");
        require(firstSetOperand.intersect(secondSetOperand).toJpql().contains(" intersect "),
                "intersect should serialize as a JPQL set operator");
        require(firstSetOperand.except(secondSetOperand).toJpql().contains(" except "),
                "except should serialize as a JPQL set operator");

        UserEntity firstScrollUser = new UserEntity();
        firstScrollUser.id = 1L;
        firstScrollUser.name = "Ada";
        UserEntity secondScrollUser = new UserEntity();
        secondScrollUser.id = 2L;
        secondScrollUser.name = "Lin";
        JpaFixture scrollFixture = fixture(List.of(firstScrollUser, secondScrollUser), 2L, false);
        QinOrmSelect<UserEntity, UserEntity> scrollQuery = QinOrm.session(scrollFixture.entityManager())
                .select(User).orderBy(User.name.asc());
        Window<UserEntity> firstWindow = scrollQuery.scroll(ScrollPosition.keyset(), 1);
        require(firstWindow.hasNext(), "keyset scroll should detect the extra row");
        requireEquals(List.of("name", "id"),
                new java.util.ArrayList<>(((KeysetScrollPosition) firstWindow.positionAt(0))
                        .getKeys().keySet()));
        requireEquals("select qinEntity from AccountUser qinEntity "
                        + "order by qinEntity.name asc, qinEntity.id asc",
                scrollFixture.jpql().get());
        scrollQuery.scroll(firstWindow.positionAt(0), 1);
        require(scrollFixture.jpql().get().contains("qinEntity.name > :qin_p0"),
                "keyset continuation should add a lexicographic predicate");

        JpaFixture tupleFixture = fixture(List.<Object[]>of(new Object[] { 5L, "Ada" }), 7L, false);
        QinOrmSelect<UserEntity, Object[]> tupleQuery = QinOrm.session(tupleFixture.entityManager())
                .select(User, User.id, User.name);
        requireEquals("select qinEntity.id, qinEntity.name from AccountUser qinEntity", tupleQuery.toJpql());
        requireEquals(2, tupleQuery.all().get(0).length);

        JpaFixture dtoFixture = fixture(List.of(new UserSummary(5L, "Ada")), 7L, false);
        requireEquals(new UserSummary(5L, "Ada"), QinOrm.session(dtoFixture.entityManager())
                .select(User, User.id, User.name)
                .as(UserSummary.class)
                .first());
        requireEquals("select new com.qin.orm.QinOrmSmokeTestMain$UserSummary(qinEntity.id, qinEntity.name) from AccountUser qinEntity",
                dtoFixture.jpql().get());

        JpaFixture oneFixture = fixture(List.of(new UserEntity()), 1L, false);
        require(QinOrm.session(oneFixture.entityManager()).select(User).one() != null,
                "one should return the only entity");
        expectFailure(NonUniqueResultException.class,
                () -> QinOrm.session(fixture(List.of(new UserEntity(), new UserEntity()), 2L, false)
                        .entityManager()).select(User).one());
        require(QinOrm.session(fixture(List.of(), 0L, false).entityManager()).select(User).one() == null,
                "one should return null when there are no rows");
        require(QinOrm.session(fixture(List.of(), 0L, false).entityManager()).select(User).first() == null,
                "first should return null when there are no rows");

        JpaFixture countFixture = fixture(List.of(), 7L, false);
        requireEquals(7L, QinOrm.session(countFixture.entityManager()).select(User, User.id)
                .where(User.id.in(List.of(2L, 3L))).count());
        requireEquals("select count(distinct qinEntity) from AccountUser qinEntity where qinEntity.id in :qin_p0",
                countFixture.jpql().get());
        JpaFixture distinctCountFixture = fixture(List.of(), 3L, false);
        requireEquals(3L, QinOrm.session(distinctCountFixture.entityManager()).select(User)
                .countDistinct(User.name));
        requireEquals("select count(distinct qinEntity.name) from AccountUser qinEntity",
                distinctCountFixture.jpql().get());

        CustomerModel Customer = new CustomerModel();
        JpaFixture joinedFixture = fixture(List.<Object[]>of(new Object[] { "Ada" }), 1L, false);
        QinOrmSelect<UserEntity, Object[]> joined = QinOrm.session(joinedFixture.entityManager())
                .select(User)
                .join(Customer, User.customerId.eq(Customer.id))
                .project(Customer.name)
                .where(User.id.eq(12L).and(Customer.name.eq("Ada")))
                .orderBy(Customer.name.desc());
        requireEquals("select qinCustomerEntity.name from AccountUser qinEntity "
                        + "join CustomerEntity qinCustomerEntity on qinEntity.customerId = qinCustomerEntity.id "
                        + "where (qinEntity.id = :qin_p0) and (qinCustomerEntity.name = :qin_p1) "
                        + "order by qinCustomerEntity.name desc",
                joined.toJpql());
        joined.all();
        requireEquals("Ada", joinedFixture.parameters().get().get("qin_p1"));

        QinOrmAggregate<UserEntity, Number> totalAmount = User.amount.sum();
        JpaFixture groupedFixture = fixture(List.<Object[]>of(new Object[] { 12L, 250L }), 1L, false);
        QinOrmSelect<UserEntity, Object[]> grouped = QinOrm.session(groupedFixture.entityManager())
                .select(User, User.customerId, totalAmount)
                .groupBy(User.customerId)
                .having(totalAmount.gt(100L));
        requireEquals("select qinEntity.customerId, sum(qinEntity.amount) from AccountUser qinEntity "
                        + "group by qinEntity.customerId having sum(qinEntity.amount) > :qin_p0",
                grouped.toJpql());
        grouped.all();
        requireEquals(100L, groupedFixture.parameters().get().get("qin_p0"));
        expectFailure(UnsupportedOperationException.class,
                () -> grouped.page(PageRequest.of(0, 10)));

        JpaFixture leftJoinFixture = fixture(List.of(new UserEntity()), 1L, false);
        requireEquals("select qinEntity from AccountUser qinEntity left join CustomerEntity qinCustomerEntity "
                        + "on qinEntity.customerId = qinCustomerEntity.id",
                QinOrm.session(leftJoinFixture.entityManager()).select(User)
                        .leftJoin(Customer, User.customerId.eq(Customer.id)).toJpql());

        CityModel City = new CityModel();
        JpaFixture chainedJoinFixture = fixture(List.<Object[]>of(new Object[] { "Ada", "Oslo" }), 1L, false);
        requireEquals("select qinCustomerEntity.name, qinCityEntity.name from AccountUser qinEntity "
                        + "join CustomerEntity qinCustomerEntity on qinEntity.customerId = qinCustomerEntity.id "
                        + "left join CityEntity qinCityEntity on qinCustomerEntity.cityId = qinCityEntity.id",
                QinOrm.session(chainedJoinFixture.entityManager()).select(User)
                        .join(Customer, User.customerId.eq(Customer.id))
                        .leftJoin(City, Customer.cityId.eq(City.id))
                        .project(Customer.name, City.name)
                        .toJpql());

        JpaFixture subqueryFixture = fixture(List.of(new UserEntity()), 1L, false);
        QinOrmSession subquerySession = QinOrm.session(subqueryFixture.entityManager());
        QinOrmSubquery<Long> correlatedIds = subquerySession.select(Customer, Customer.id)
                .where(Customer.id.eq(User.id)).subquery(User);
        requireEquals("select qinEntity from AccountUser qinEntity where exists "
                        + "(select qinSubquery0.id from CustomerEntity qinSubquery0 "
                        + "where qinSubquery0.id = qinEntity.id)",
                subquerySession.select(User).where(correlatedIds.exists()).toJpql());

        QinOrmSubquery<Long> customerIds = subquerySession.select(Customer, Customer.id)
                .where(Customer.name.eq("Ada")).subquery();
        QinOrmSelect<UserEntity, UserEntity> inSubquery = subquerySession.select(User)
                .where(User.id.in(customerIds));
        requireEquals("select qinEntity from AccountUser qinEntity where qinEntity.id in "
                        + "(select qinSubquery0.id from CustomerEntity qinSubquery0 "
                        + "where qinSubquery0.name = :qin_p0)",
                inSubquery.toJpql());
        inSubquery.all();
        requireEquals("Ada", subqueryFixture.parameters().get().get("qin_p0"));

        require(QinOrm.session(fixture(List.of(1), 0L, true).entityManager()).select(User).exists(),
                "exists should return true when a row is present");
        require(!QinOrm.session(fixture(List.of(), 0L, false).entityManager()).select(User).exists(),
                "exists should return false when no row is present");

        JpaFixture firstFixture = fixture(List.of(new UserEntity()), 1L, false);
        QinOrm.session(firstFixture.entityManager()).select(User).first();
        requireEquals(1, firstFixture.maxResults().get());

        JpaFixture pageFixture = fixture(List.of(new UserEntity(), new UserEntity()), 7L, false);
        Page<UserEntity> page = QinOrm.session(pageFixture.entityManager()).select(User)
                .where(User.name.eq("Ada"))
                .page(PageRequest.of(1, 5));
        requireEquals(7L, page.getTotalElements());
        requireEquals(5, pageFixture.maxResults().get());
        requireEquals(5, pageFixture.firstResult().get());

        JpaFixture distinctScalarPageFixture = fixture(List.of("Ada"), 4L, false);
        Page<String> distinctScalarPage = QinOrm.session(distinctScalarPageFixture.entityManager())
                .select(User, User.name).distinct().page(PageRequest.of(0, 1));
        requireEquals(4L, distinctScalarPage.getTotalElements());
        requireEquals("select count(distinct qinEntity.name) + case when count(qinEntity) > count(qinEntity.name)"
                        + " then 1 else 0 end from AccountUser qinEntity",
                distinctScalarPageFixture.jpql().get());

        JpaFixture distinctTupleFixture = fixture(List.of(), 0L, false);
        expectFailure(UnsupportedOperationException.class,
                () -> QinOrm.session(distinctTupleFixture.entityManager()).select(User, User.id, User.name)
                        .distinct().page(PageRequest.of(0, 5)));
        requireEquals(-1, distinctTupleFixture.maxResults().get());

        JpaFixture sliceFixture = fixture(List.of(new UserEntity(), new UserEntity(), new UserEntity()), 3L, false);
        Slice<UserEntity> slice = QinOrm.session(sliceFixture.entityManager()).select(User)
                .slice(PageRequest.of(0, 2));
        requireEquals(2, slice.getNumberOfElements());
        require(slice.hasNext(), "slice should detect the extra row");
        requireEquals(3, sliceFixture.maxResults().get());

        String defaultEntityName = QinOrm.entity(DefaultNameEntity.class).entityName();
        requireEquals("DefaultNameEntity", defaultEntityName);
        expectFailure(IllegalArgumentException.class, () -> User.column("name desc", String.class));
        System.out.println("QinOrmSmokeTestMain OK");
    }

    @SuppressWarnings("unchecked")
    private static JpaFixture fixture(List<?> queryResults, long count, boolean exists) {
        AtomicReference<String> jpql = new AtomicReference<>("");
        AtomicReference<Map<String, Object>> parameters = new AtomicReference<>(Map.of());
        AtomicInteger maxResults = new AtomicInteger(-1);
        AtomicInteger firstResult = new AtomicInteger(-1);
        AtomicBoolean existsResult = new AtomicBoolean(exists);

        EntityType<UserEntity> entityType = (EntityType<UserEntity>) proxy(EntityType.class, (instance, method, args) -> {
            if (method.getName().equals("getName")) {
                return "AccountUser";
            }
            if (method.getName().equals("getAttribute")) {
                return attribute(UserEntity.class, (String) args[0]);
            }
            if (method.getName().equals("hasSingleIdAttribute")) {
                return true;
            }
            if (method.getName().equals("getId")) {
                return proxy(SingularAttribute.class, (target, idMethod, idArgs) -> switch (idMethod.getName()) {
                    case "getName" -> "id";
                    case "getPersistentAttributeType" -> Attribute.PersistentAttributeType.BASIC;
                    default -> null;
                });
            }
            if (method.getName().equals("getIdType")) {
                return proxy(jakarta.persistence.metamodel.Type.class, (target, typeMethod, typeArgs) ->
                        typeMethod.getName().equals("getJavaType") ? Long.class : null);
            }
            return null;
        });
        EntityType<CustomerEntity> customerEntityType = (EntityType<CustomerEntity>) proxy(EntityType.class,
                (instance, method, args) -> {
                    if (method.getName().equals("getName")) {
                        return "CustomerEntity";
                    }
                    if (method.getName().equals("getAttribute")) {
                        return attribute(CustomerEntity.class, (String) args[0]);
                    }
                    if (method.getName().equals("hasSingleIdAttribute")) {
                        return true;
                    }
                    if (method.getName().equals("getId")) {
                        return proxy(SingularAttribute.class, (target, idMethod, idArgs) ->
                                idMethod.getName().equals("getName") ? "id" : null);
                    }
                    return null;
                });
        EntityType<CityEntity> cityEntityType = (EntityType<CityEntity>) proxy(EntityType.class,
                (instance, method, args) -> {
                    if (method.getName().equals("getName")) {
                        return "CityEntity";
                    }
                    if (method.getName().equals("getAttribute")) {
                        return attribute(CityEntity.class, (String) args[0]);
                    }
                    return null;
                });
        Metamodel metamodel = proxy(Metamodel.class, (instance, method, args) -> {
            if (!method.getName().equals("entity")) {
                return null;
            }
            if (args[0] == CustomerEntity.class) {
                return customerEntityType;
            }
            return args[0] == CityEntity.class ? cityEntityType : entityType;
        });
        EntityManager entityManager = proxy(EntityManager.class, (instance, method, args) -> {
            if (method.getName().equals("getMetamodel")) {
                return metamodel;
            }
            if (method.getName().equals("createQuery") || method.getName().equals("createNativeQuery")) {
                jpql.set((String) args[0]);
                Class<?> resultType = (Class<?>) args[1];
                AtomicReference<TypedQuery<Object>> queryRef = new AtomicReference<>();
                TypedQuery<Object> query = proxy(TypedQuery.class, (target, queryMethod, queryArgs) -> {
                    switch (queryMethod.getName()) {
                        case "setParameter" -> {
                            Map<String, Object> next = new java.util.LinkedHashMap<>(parameters.get());
                            next.put((String) queryArgs[0], queryArgs[1]);
                            parameters.set(Map.copyOf(next));
                            return queryRef.get();
                        }
                        case "setFirstResult" -> {
                            firstResult.set((Integer) queryArgs[0]);
                            return queryRef.get();
                        }
                        case "setMaxResults" -> {
                            maxResults.set((Integer) queryArgs[0]);
                            return queryRef.get();
                        }
                        case "getResultList" -> {
                            if (jpql.get().startsWith("select 1 ")) {
                                return existsResult.get() ? List.of(1) : List.of();
                            }
                            if (resultType == Long.class && jpql.get().startsWith("select count(")) {
                                return List.of(count);
                            }
                            return queryResults;
                        }
                        case "getSingleResult" -> {
                            return count;
                        }
                        default -> {
                            return null;
                        }
                    }
                });
                queryRef.set(query);
                return query;
            }
            return null;
        });
        return new JpaFixture(entityManager, jpql, parameters, maxResults, firstResult);
    }

    private static Attribute<?, ?> attribute(Class<?> owner, String name) {
        Class<?> propertyType = switch (name) {
            case "id" -> Long.class;
            case "customerId" -> Long.class;
            case "cityId" -> Long.class;
            case "amount" -> Long.class;
            case "name" -> String.class;
            default -> throw new IllegalArgumentException("Unknown test attribute: " + name);
        };
        return proxy(Attribute.class, (instance, method, args) -> switch (method.getName()) {
            case "getJavaType" -> propertyType;
            case "getPersistentAttributeType" -> Attribute.PersistentAttributeType.BASIC;
            case "getJavaMember" -> {
                try {
                    yield owner.getDeclaredField(name);
                } catch (NoSuchFieldException error) {
                    throw new IllegalArgumentException(error);
                }
            }
            default -> null;
        });
    }

    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        Object value = Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, handler);
        return type.cast(value);
    }

    private static void requireEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected <" + expected + "> but got <" + actual + ">");
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static <T extends Throwable> void expectFailure(Class<T> type, Runnable action) {
        try {
            action.run();
        } catch (Throwable error) {
            if (type.isInstance(error)) {
                return;
            }
            throw new AssertionError("Expected " + type.getSimpleName() + " but got " + error, error);
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    private record JpaFixture(
            EntityManager entityManager,
            AtomicReference<String> jpql,
            AtomicReference<Map<String, Object>> parameters,
            AtomicInteger maxResults,
            AtomicInteger firstResult) {
    }

    public record UserSummary(Long id, String name) {
    }

    @Entity(name = "AccountUser")
    public static class UserEntity {
        private Long id;
        private String name;
        private Long customerId;
        private Long amount;
    }

    @Entity
    public static class CustomerEntity {
        private Long id;
        private String name;
        private Long cityId;
    }

    @Entity
    public static class CityEntity {
        private Long id;
        private String name;
    }

    private static final class UserModel extends QinOrmEntity<UserEntity> {
        private final QinOrmColumn<UserEntity, Long> id = column("id", Long.class);
        private final QinOrmColumn<UserEntity, Long> customerId = column("customerId", Long.class);
        private final QinOrmColumn<UserEntity, Long> amount = column("amount", Long.class);
        private final QinOrmColumn<UserEntity, String> name = column("name", String.class);

        private UserModel() {
            super(UserEntity.class);
        }
    }

    private static final class CustomerModel extends QinOrmEntity<CustomerEntity> {
        private final QinOrmColumn<CustomerEntity, Long> id = column("id", Long.class);
        private final QinOrmColumn<CustomerEntity, Long> cityId = column("cityId", Long.class);
        private final QinOrmColumn<CustomerEntity, String> name = column("name", String.class);

        private CustomerModel() {
            super(CustomerEntity.class);
        }
    }

    private static final class CityModel extends QinOrmEntity<CityEntity> {
        private final QinOrmColumn<CityEntity, Long> id = column("id", Long.class);
        private final QinOrmColumn<CityEntity, String> name = column("name", String.class);

        private CityModel() {
            super(CityEntity.class);
        }
    }

    @Entity
    public static class DefaultNameEntity {
    }
}
