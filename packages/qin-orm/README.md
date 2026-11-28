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

List<Object[]> usersWithCustomers = db.select(User)
        .join(Customer, User.customerId.eq(Customer.id))
        .project(User.name, Customer.name)
        .all();

QinOrmAggregate<UserEntity, Number> total = User.amount.sum();
List<Object[]> totalsByCustomer = db.select(User, User.customerId, total)
        .groupBy(User.customerId)
        .having(total.gt(100L))
        .all();

QinOrmSubquery<Long> customerIds = db.select(Customer, Customer.id)
        .where(Customer.name.eq("Ada"))
        .subquery();
List<UserEntity> users = db.select(User)
        .where(User.id.in(customerIds))
        .all();

List<Long> nativeIds = db.nativeQuery(
        "select id from account_user where status = :status", Long.class)
        .bind("status", "active")
        .all();

Window<UserEntity> usersWindow = db.select(User)
        .orderBy(User.createdAt.desc())
        .scroll(ScrollPosition.keyset(), 100);
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

Joins target an entity explicitly and state the matching scalar fields with
`ON`, for example `join(Customer, User.customerId.eq(Customer.id))`. This uses
ordinary mapped properties and JPQL range joins. Qin ORM does not require or
use JPA `@OneToMany` or `@ManyToOne` mappings.

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
- `countDistinct(expression)` counts distinct scalar values.
- `exists()` returns whether any entity matches.
- `page(Pageable)` returns Spring Data `Page` and runs a count query.
- `slice(Pageable)` returns Spring Data `Slice` without a count query.

Page totals follow the result shape. Scalar `distinct()` uses
`count(distinct expression)`. Distinct projections with multiple expressions
and aggregate projections reject `page()` because JPQL cannot portably count
those result rows. Use `slice()` for those shapes.

## Supported Predicates

Columns support `eq`, `ne`, `gt`, `ge`, `lt`, `le`, `between`, `in`, `like`,
`isNull`, and `isNotNull`. Predicates can be combined with `and`, `or`, and
`not`. Sorting is available through typed `column.asc()/desc()` descriptors or
Spring Data `Sort`/`Pageable` values. Sorting properties from those values are
validated against the JPA metamodel.

Expressions support `count`, `countDistinct`, `sum`, `avg`, `min`, and `max`.
`groupBy` accepts scalar expressions and `having` accepts normal typed
predicates, including comparisons against aggregates. Grouped queries support
`all`, `one`, `first`, `exists`, and `slice`. `count()` and `page()` reject
grouped queries because portable JPQL cannot count grouped result rows without
wrapping the query in a derived table.

Spring `Sort.Order.ignoreCase()` is rendered as `lower(property)` after the
metamodel confirms the property is a string. Explicit null precedence is
rejected because it is not portable across the supported JPA provider range.

Subqueries support `exists()` and scalar `in(subquery)`. Predicates inside a
subquery can reference outer query entity descriptors, which are resolved to
the enclosing aliases and validated through the JPA metamodel. Subqueries use
separate aliases and share the outer named-parameter context.

Database-specific SQL is available only through the explicitly named
`nativeQuery(sql, resultType)` escape hatch. Bind values by name with `bind`,
so SQL text and parameter values remain separate. This API is read-only.

Entity queries support Spring Data `Window` scrolling with offset or forward
keyset positions. Keyset scrolling appends the single-field entity identifier
as a final ordering key when needed. It rejects collection joins, composite
identifiers, nullable cursor values, non-basic sort fields, and backward
positions until those cases have a defined portable result contract.

## Current Boundary

The current DSL supports one entity root, one explicit entity join using an
`ON` predicate, scalar and aggregate expressions, and grouping. Multiple
joins are not implemented yet. Subquery scalar selection is limited to one
expression, as required by JPQL. Database-specific JSONB and window-function
queries can use a separate parameterized native-query API when needed.

Entity and column descriptors are currently declared explicitly. The JPA
metamodel validates descriptors at execution. Qin compiler-generated
descriptors are a future integration so source code can use `User.id` without
handwritten descriptor declarations.
