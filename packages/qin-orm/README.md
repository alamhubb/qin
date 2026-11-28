# Qin ORM

Qin ORM starts with entity-first JPQL selection backed by Jakarta Persistence.
The first argument is an entity descriptor backed by an `@Entity` class. Every
following argument is a typed column descriptor for that entity, so a later
entity cannot be passed in the column position.

```java
import static com.qin.orm.QinOrm.select;

UserModel User = new UserModel();
QinOrmColumn<UserEntity, Long> id = User.id;
QinOrmColumn<UserEntity, String> name = User.name;

var query = select(User, User.id, User.name);
String jpql = query.toJpql();
```

The three accepted forms are equivalent when the unqualified field variables
are in scope:

```java
select(User, User.id, User.name);
select(User, User.id, name);
select(User, id, name);
```

The query above renders as:

```sql
select qinEntity.id as id, qinEntity.name as name from UserEntity qinEntity
```

When an entity uses `@Entity(name = "...")`, JPQL uses that entity name. The
default entity name is the Java class's simple name. Before execution, Qin ORM
resolves the class and each selected property against the JPA `Metamodel`,
including the mapped property type. Database table and column names remain
owned by the JPA mapping.

Selected properties are returned as `QinOrmRow` values. They are projections,
not managed entity instances.

## Current query surface

- Select one or more scalar entity properties.
- Render a JPQL select statement.
- Execute through `EntityManager` and read aliased tuple values.
- Reject invalid identifiers, duplicate selected properties, unmanaged
  entities, missing persistent properties, and mismatched descriptor types.

The supported call shape is `select(User, User.id, User.name)`. A mixed form
such as `select(User, User.id, name)` works when `name` is a column descriptor
already in scope. The fully unqualified form `select(User, id, name)` works
when `id` and `name` are descriptors in scope. This keeps ordinary Qin name
resolution intact and makes the Java API's generic signature enforce that all
selected columns belong to the initial entity.

An entity descriptor is a small model object backed by a JPA entity class:

```java
final class UserModel extends QinOrmEntity<UserEntity> {
    final QinOrmColumn<UserEntity, Long> id = column("id", Long.class);
    final QinOrmColumn<UserEntity, String> name = column("name", String.class);

    UserModel() {
        super(UserEntity.class);
    }
}
```

The JPA `Metamodel` is authoritative during execution. It verifies that the
class is managed, each selected property exists, and the descriptor type
matches. It also supplies the JPQL entity name, including names supplied by
mapping metadata. `@Entity` provides a descriptor-time default, but does not
replace runtime metamodel validation.
