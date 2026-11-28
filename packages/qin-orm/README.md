# Qin ORM

Qin ORM is an entity-first Qin query DSL that renders JPQL and executes it
through the application's JPA `EntityManager`. It does not own entity writes or
transactions. Spring Data JPA repositories remain responsible for CRUD.

```java
QinOrmSession db = QinOrm.session(entityManager);
UserEntityQin User = new UserEntityQin();

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
List<UserEntity> namedUsers = db.select(User)
        .where(User.id.in(customerIds))
        .all();

List<Long> activeIds = db.select(User, User.id)
        .where(User.status.eq("active"))
        .union(db.select(User, User.id).where(User.status.eq("pending")))
        .all();

String table = QinOrm.nativeIdentifier(sourceTable, Set.of("account_user", "archive_user"));
List<Long> nativeIds = db.nativeQuery("select id from " + table + " where status = :status")
        .bind("status", "active")
        .as(Long.class)
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
- `countDistinct(expression)` counts distinct non-null scalar values.
- `exists()` returns whether any entity matches.
- `page(Pageable)` returns Spring Data `Page` and runs a count query.
- `slice(Pageable)` returns Spring Data `Slice` without a count query.

Page totals follow the result shape. A scalar `distinct()` page counts
distinct non-null values and includes one result for null when present.
Distinct projections with multiple expressions
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

Spring `Sort.Order.ignoreCase()` uses `lower(property)` for string properties.
`nullsFirst()` and `nullsLast()` use the corresponding JPQL 3.2 clauses.
Use a JPA provider that supports those clauses.

Subqueries support `exists()` and scalar `in(subquery)`. Declare outer
descriptors explicitly for correlated predicates.

```java
var customerExists = db.select(Customer, Customer.id)
        .where(Customer.id.eq(User.customerId))
        .subquery(User);
var linkedUsers = db.select(User).where(customerExists.exists()).all();
```

Set queries support JPQL 3.2 `union`, `unionAll`, `intersect`, and `except`.
Operands must use the same `EntityManager` and select the same result types.
Provider support for these operators depends on the JPA implementation version.

Use trusted SQL with `nativeQuery(sql).bind(name, value).as(Result.class)`
for database-specific queries. For dynamic table or column names, pass the
requested identifier through `QinOrm.nativeIdentifier` with an application-owned
allowlist before inserting it into the SQL. Named parameters and native DTO
mapping depend on the JPA provider.

Entity queries support Spring Data `Window` scrolling with offset or forward
keyset positions. The single basic identifier is the final ordering key.
Use unjoined entity queries and non-null basic sort keys for keyset scrolling.

The `QinOrmEntityProcessor` generates `<Entity>Qin` descriptors for scalar
JPA properties. With annotation processing enabled, `UserEntity` produces
`UserEntityQin`, whose typed column fields can be used directly in query
expressions. The processor generates columns for basic properties. Activate
the processor in the application build, for example by placing the Qin ORM JAR
on the javac processor path. Qin source can import the compiled descriptor.

```ts
import { UserEntityQin } from "java:com.example";

const User = new UserEntityQin();
const id = User.id;
```

Joins and correlated subqueries currently identify entity occurrences by Java
type. Use distinct entity types within a join chain and across correlated
references.

## Integration smoke

Compile `src/integration/java/com/qin/orm/QinOrmHibernateSmokeTestMain.java`
with the Qin ORM JAR on the classpath and processor path. Add Jakarta
Persistence, Spring Data Commons, Hibernate ORM and H2 with their runtime
dependencies to the classpath. Run `com.qin.orm.QinOrmHibernateSmokeTestMain`.
The fixture has been verified with Hibernate ORM 7.4.17.Final and H2 2.3.232.
With the integration classes and dependencies on the Qin runtime classpath,
`src/integration/descriptor.qin` exercises a join using generated columns.
