package com.qin.orm;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class QinOrmSmokeTestMain {
    private QinOrmSmokeTestMain() {
    }

    public static void main(String[] args) {
        UserModel User = new UserModel();
        QinOrmColumn<UserEntity, Long> id = User.id;
        QinOrmColumn<UserEntity, String> name = User.name;
        List<Tuple> twoRows = List.of(tuple(), tuple());

        JpaFixture allFixture = fixture(twoRows, true);
        QinOrmSelect<UserEntity> query = QinOrm.session(allFixture.entityManager())
                .select(User, User.id, User.name);
        requireEquals("select qinEntity.id as id, qinEntity.name as name from AccountUser qinEntity",
                query.toJpql());
        requireEquals(2, query.all().size());
        requireEquals(query.toJpql(), allFixture.lastQuery().get());

        JpaFixture mixedFixture = fixture(twoRows, true);
        String mixedQualifiedAndBare = QinOrm.session(mixedFixture.entityManager())
                .select(User, User.id, name)
                .toJpql();
        requireEquals(query.toJpql(), mixedQualifiedAndBare);

        JpaFixture bareFixture = fixture(twoRows, true);
        String unqualifiedColumns = QinOrm.session(bareFixture.entityManager())
                .select(User, id, name)
                .toJpql();
        requireEquals(query.toJpql(), unqualifiedColumns);

        JpaFixture firstFixture = fixture(twoRows, true);
        Optional<QinOrmRow> first = QinOrm.session(firstFixture.entityManager())
                .select(User, User.id, User.name)
                .first();
        require(first.isPresent(), "first should return the first row");
        requireEquals(1, firstFixture.maxResults().get());

        JpaFixture oneFixture = fixture(List.of(tuple()), true);
        require(QinOrm.session(oneFixture.entityManager()).select(User, User.id).one().isPresent(),
                "one should return the only row");
        expectFailure(NonUniqueResultException.class,
                () -> QinOrm.session(fixture(twoRows, true).entityManager()).select(User, User.id).one());
        require(QinOrm.session(fixture(List.of(), true).entityManager()).select(User, User.id).one().isEmpty(),
                "one should return empty when there are no rows");

        JpaFixture countFixture = fixture(List.of(), true);
        requireEquals(7L, QinOrm.session(countFixture.entityManager()).select(User, User.id).count());
        requireEquals("select count(qinEntity) from AccountUser qinEntity", countFixture.lastQuery().get());

        require(QinOrm.session(fixture(List.of(), true).entityManager()).select(User, User.id).exists(),
                "exists should return true when a row is present");
        require(!QinOrm.session(fixture(List.of(), false).entityManager()).select(User, User.id).exists(),
                "exists should return false when no row is present");

        requireEquals("DefaultNameEntity", QinOrm.entity(DefaultNameEntity.class).entityName());
        expectFailure(IllegalArgumentException.class, () -> User.column("name desc", String.class));
        expectFailure(IllegalArgumentException.class,
                () -> QinOrm.session(allFixture.entityManager()).select(User));
        System.out.println("QinOrmSmokeTestMain OK");
    }

    @SuppressWarnings("unchecked")
    private static JpaFixture fixture(List<Tuple> tupleResults, boolean exists) {
        AtomicReference<String> lastQuery = new AtomicReference<>("");
        AtomicInteger maxResults = new AtomicInteger(-1);

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
                String jpql = (String) args[0];
                Class<?> resultType = (Class<?>) args[1];
                lastQuery.set(jpql);
                TypedQuery<Object>[] queryRef = (TypedQuery<Object>[]) new TypedQuery<?>[1];
                queryRef[0] = proxy(TypedQuery.class, (query, queryMethod, queryArgs) -> {
                    if (queryMethod.getName().equals("setMaxResults")) {
                        maxResults.set((Integer) queryArgs[0]);
                        return queryRef[0];
                    }
                    if (queryMethod.getName().equals("getResultList")) {
                        if (resultType == Tuple.class) {
                            return tupleResults;
                        }
                        return exists ? List.of(1) : List.of();
                    }
                    if (queryMethod.getName().equals("getSingleResult")) {
                        return 7L;
                    }
                    return null;
                });
                return queryRef[0];
            }
            return null;
        });

        return new JpaFixture(entityManager, lastQuery, maxResults);
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

    private static Tuple tuple() {
        return proxy(Tuple.class, (instance, method, args) -> null);
    }

    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        Object proxy = Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, handler);
        return type.cast(proxy);
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
            AtomicReference<String> lastQuery,
            AtomicInteger maxResults) {
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
