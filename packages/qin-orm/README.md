# Qin ORM

Qin ORM is an entity-first Qin query DSL that renders JPQL and executes it
through the application's JPA `EntityManager`. It does not own entity writes or
transactions. Spring Data JPA repositories remain responsible for CRUD.

```java
QinOrmSession db = QinOrm.session(entityManager);
UserModel User = new UserModel();

List<UserEntity> users = db.select(User)
        .where(User.status.eq("active").and(User.id.gt(10L)))
        .orderBy(User.name.asc())
        .limit(50)
        .all();

UserEntity first = db.select(User)
        .where(User.email.eq(email))
        .first();

List<Long> userIds = db.select(User, User.id)
        .where(User.status.in(List.of("active", "pending")))
        .all();

List<Object[]> userNames = db.select(User, User.id, User.name)
        .where(User.status.eq("active"))
        .all();

List<UserSummary> summaries = db.select(User, User.id, User.name)
        .where(User.status.eq("active"))
        .as(UserSummary.class)
        .all();
```

The multi-column query renders a standard JPQL constructor projection when
`.as(...)` is used:

```sql
select new com.example.UserSummary(qinEntity.id, qinEntity.name)
from User qinEntity
where qinEntity.status = :qin_p0
```

Every Qin predicate value is bound as a named JPA parameter. Entity and
property names are resolved from the JPA `Metamodel` before execution. A
custom `@Entity(name = "...")` name is honored. Database table and column
names remain owned by JPA mappings.

## Result Shapes

- `select(Entity).all()` returns `List<Entity>`.
- `select(Entity, column).all()` returns `List<ColumnType>`.
- `select(Entity, columnA, columnB).all()` returns `List<Object[]>`, matching
  the standard JPA multi-select result shape.
- Adding `.as(Dto.class)` uses a JPQL constructor expression and returns
  `List<Dto>`.
- `one()` returns the single result or `null`, and throws
  `NonUniqueResultException` if more than one row matches.
- `first()` returns the first result or `null`. Add ordering for deterministic
  results.
- `count()` returns the number of matching entities.
- `exists()` returns whether any entity matches.
- `page(Pageable)` returns Spring Data `Page` and runs a count query.
- `slice(Pageable)` returns Spring Data `Slice` without a count query.

## Supported Predicates

Columns support `eq`, `ne`, `gt`, `ge`, `lt`, `le`, `between`, `in`, `like`,
`isNull`, and `isNotNull`. Predicates can be combined with `and`, `or`, and
`not`. Sorting is available through typed `column.asc()/desc()` descriptors or
Spring Data `Sort`/`Pageable` values. Sorting properties from those values are
validated against the JPA metamodel.

## Current Boundary

The current DSL handles one entity root and scalar properties of that entity.
Relationship joins, fetch joins, group/having, aggregate projections, and
subqueries are not implemented yet. Those should be added as typed JPQL
expression nodes so JPA remains the parser, validator, and execution engine.
Database-specific JSONB and window-function queries can use a separate
parameterized native-query API when needed.

Entity and column descriptors are currently declared explicitly. The JPA
metamodel validates descriptors at execution. Qin compiler-generated
descriptors are a future integration so source code can use `User.id` without
handwritten descriptor declarations.
