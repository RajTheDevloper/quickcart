# Station 04: Spring Data JPA with PostgreSQL

## Part A: persisting the catalog

### Three names
| Name | What it is |
|---|---|
| JPA (`jakarta.persistence`) | specification: `@Entity`, mapping rules |
| Hibernate | implementation that generates SQL |
| Spring Data JPA | generates repository implementations from interfaces |

ORM = work with objects, the framework writes SQL. Entity = class mapped to a table (object = row, field = column).

### Persistence context and dirty checking
Inside a transaction Hibernate tracks every loaded entity (**managed**). Change a field on a managed entity and Hibernate issues the `UPDATE` at commit, with no `save` call. States: new -> managed -> detached -> removed.

### Data flow (end to end)
```
React -HTTP-> Controller -> Service -> JpaProductStore -> ProductJpaRepository (proxy)
  -> Hibernate (SQL) -> HikariCP/JDBC -> PostgreSQL
Row -> ProductEntity -> Product (record) -> Jackson -> JSON -> React
```
The service and controller did not change when the in-memory store was replaced: they depend on the `ProductStore` interface.

### Flyway (versioned schema)
- Files: `src/main/resources/db/migration/V<n>__<description>.sql` (two underscores). Applied in order, recorded in `flyway_schema_history`.
- **Never edit an applied migration** (checksum mismatch). Fix forward with a new version.
- `ddl-auto=validate`: Hibernate checks entities against the schema at startup (`missing table`, `missing column` errors).
- **Spring Boot 4:** `flyway-core` alone no longer enables Flyway. Use `spring-boot-starter-flyway` + `flyway-database-postgresql`. If you see `Schema validation: missing table` and no Flyway log lines, Flyway did not run.

### Properties used
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
```
`open-in-view=false`: no database session held for the whole web request; lazy loading outside a transaction fails loudly instead of silently running queries from the view layer.

### Entity rules
- `@Entity`, `@Table`, `@Id`, `@GeneratedValue(strategy = IDENTITY)` (PostgreSQL `BIGSERIAL`).
- Needs a no-arg constructor (`protected`): Hibernate creates objects by reflection.
- Cannot be a record (Hibernate mutates fields).
- Money = `BigDecimal` / `NUMERIC(12,2)`, never `double`.
- Business key (`sku`) without a setter; changes go through an `update(...)` method.

### Repository
```java
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findBySku(String sku);   // derived query from the method name
    void deleteBySku(String sku);
}
```
No implementation is written; Spring Data creates a proxy at startup.

### Adapter and transactions
`JpaProductStore` (`@Repository @Transactional`) maps entity <-> record and implements `ProductStore`.
- Class-level `@Transactional` = read-write. Only the read methods get `@Transactional(readOnly = true)`.
- `readOnly = true` puts the connection in read-only mode: a write inside it fails with `cannot execute INSERT in a read-only transaction`. Inner methods join the outer transaction, so the outermost one decides.
- Derived `deleteBy...` needs a transaction (`TransactionRequiredException` without one).
- `save` is an upsert by `sku`: update the managed entity if it exists, otherwise insert.
- Seeding is idempotent (`if (!catalog.findAll().isEmpty()) return;`).

### Errors met
| Message | Cause |
|---|---|
| `required a bean of type ProductStore that could not be found` | class not scanned: wrong `package` line (`quickstart` instead of `quickcart`), missing stereotype annotation, or compile error |
| `Schema validation: missing table [products]` | Flyway did not run (Boot 4 starter, wrong folder or file name) |
| `cannot execute INSERT in a read-only transaction` | `readOnly = true` on a write method or an outer read-only transaction |
| `Migration checksum mismatch` | edited an applied migration |
| `permission denied for schema public` | `GRANT ALL ON SCHEMA public TO quickcart_user;` as `postgres` |

### Interview questions
1. **JPA vs Hibernate vs Spring Data JPA?** Specification vs implementation vs repository generator.
2. **What is dirty checking?** Hibernate tracks managed entities and flushes changed fields as an `UPDATE` at commit.
3. **Why Flyway over `ddl-auto=update`?** Versioned, repeatable, reviewable, identical in every environment.
4. **Why a no-arg constructor?** Hibernate instantiates by reflection, then fills the fields.
5. **What does `@Transactional` do?** One database transaction: commit on normal return, rollback on a runtime exception.
6. **Why not expose entities in the API?** Couples the API to the table design and risks lazy-loading failures; use DTOs.
7. **Why `BigDecimal` for money?** Binary floating point cannot represent decimal fractions exactly.
