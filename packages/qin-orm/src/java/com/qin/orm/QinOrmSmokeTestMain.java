package com.qin.orm;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

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
        requireEquals("select new com.qin.orm.QinOrmSmokeTestMain.UserSummary(qinEntity.id, qinEntity.name) from AccountUser qinEntity",
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
        requireEquals("select count(qinEntity) from AccountUser qinEntity where qinEntity.id in :qin_p0",
                countFixture.jpql().get());

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
                return attribute((String) args[0]);
            }
            return null;
        });
        Metamodel metamodel = proxy(Metamodel.class, (instance, method, args) ->
                method.getName().equals("entity") ? entityType : null);
        EntityManager entityManager = proxy(EntityManager.class, (instance, method, args) -> {
            if (method.getName().equals("getMetamodel")) {
                return metamodel;
            }
            if (method.getName().equals("createQuery")) {
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

    private static Attribute<?, ?> attribute(String name) {
        Class<?> propertyType = switch (name) {
            case "id" -> Long.class;
            case "name" -> String.class;
            default -> throw new IllegalArgumentException("Unknown test attribute: " + name);
        };
        return proxy(Attribute.class, (instance, method, args) ->
                method.getName().equals("getJavaType") ? propertyType : null);
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
    }

    private static final class UserModel extends QinOrmEntity<UserEntity> {
        private final QinOrmColumn<UserEntity, Long> id = column("id", Long.class);
        private final QinOrmColumn<UserEntity, String> name = column("name", String.class);

        private UserModel() {
            super(UserEntity.class);
        }
    }

    @Entity
    public static class DefaultNameEntity {
    }
}
