package com.qin.orm;

import static com.qin.orm.QinOrm.select;

import jakarta.persistence.Entity;

public final class QinOrmSmokeTestMain {
    private QinOrmSmokeTestMain() {
    }

    public static void main(String[] args) {
        UserModel User = new UserModel();
        QinOrmColumn<UserEntity, Long> id = User.id;
        QinOrmColumn<UserEntity, String> name = User.name;

        String jpql = select(User, User.id, User.name).toJpql();
        requireEquals("select qinEntity.id as id, qinEntity.name as name from AccountUser qinEntity", jpql);

        String mixedQualifiedAndBare = select(User, User.id, name).toJpql();
        requireEquals(jpql, mixedQualifiedAndBare);

        String unqualifiedColumns = select(User, id, name).toJpql();
        requireEquals(jpql, unqualifiedColumns);

        String defaultEntityName = QinOrm.entity(DefaultNameEntity.class).entityName();
        requireEquals("DefaultNameEntity", defaultEntityName);

        expectFailure(() -> User.column("name desc", String.class));
        expectFailure(() -> select(User));
        System.out.println("QinOrmSmokeTestMain OK");
    }

    private static void requireEquals(String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected <" + expected + "> but got <" + actual + ">");
        }
    }

    private static void expectFailure(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Expected IllegalArgumentException");
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
