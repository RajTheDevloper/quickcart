# Station 00: Setup and Project Map

## What we built
A Spring Boot 3 project (Maven, Java 21) called QuickCart, connected to a local PostgreSQL database and pushed to GitHub.

## The pieces
| Piece | Job |
|---|---|
| JDK 21 | compiles Java, runs it on the JVM |
| Maven (`pom.xml`) | downloads libraries, builds the project |
| Spring Boot starters | bundles of libraries that go together (web, JPA, ...) |
| PostgreSQL (port 5432) | stores data; a separate program the app talks to over JDBC |
| Git / GitHub | history, branches, pull requests |

## Config that connects the app to the database
`src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/quickcart
spring.datasource.username=${DB_USER:quickcart_user}
spring.datasource.password=${DB_PASSWORD:change_me}
spring.jpa.hibernate.ddl-auto=none
```
- URL pattern: `jdbc:<db type>://<host>:<port>/<database>`
- `${NAME:default}` = environment variable if set, otherwise the default. Real passwords live in environment variables, never in Git.
- `ddl-auto=none`: Hibernate does not touch the tables. Migrations (Flyway) will own the schema.

## What happens when you press Run (startup, inside)
1. `main()` calls `SpringApplication.run(...)`.
2. `@SpringBootApplication` = `@Configuration` + `@ComponentScan` + `@EnableAutoConfiguration`.
3. Spring creates the **ApplicationContext** (the container that holds all beans).
4. **Component scan** finds your classes in `com.quickcart.quickcart` and below.
5. **Auto-configuration** looks at the classpath and properties, then creates beans for you:
   - web starter present -> embedded **Tomcat** on port 8080
   - PostgreSQL driver + datasource URL -> **HikariCP** connection pool (`DataSource`)
   - JPA starter -> **Hibernate** (`EntityManagerFactory`)
   - Actuator -> `/actuator/health`
6. The context is ready, Tomcat starts accepting requests: `Started QuickcartApplication in x seconds`.

## Checks
- `http://localhost:8080/actuator/health` returns `{"status":"UP"}` (app is running and reached PostgreSQL).
- `git status` must not list `target/`.

## Common errors
| Message | Cause |
|---|---|
| `password authentication failed` | password in properties differs from the `CREATE USER` one |
| `Connection refused` | PostgreSQL service not running |
| `database "quickcart" does not exist` | `CREATE DATABASE` not run |
| `Port 8080 was already in use` | an older run is still alive |

## Git habits
- `main` always works; work on branches: `git switch -c feature/<name>`
- Commit prefixes: `feat:` `fix:` `docs:` `refactor:` `test:` `chore:`
- `git status` and `git diff` before every commit
- Open a pull request even when working alone
- Never commit secrets; a leaked secret must be rotated

## Interview questions
1. **What happens between running a Spring Boot app and "Started Application"?**
   `main` calls `SpringApplication.run`, which builds the ApplicationContext, scans for components, applies auto-configuration based on the classpath and properties (Tomcat, DataSource, Hibernate), then starts the web server.
2. **What does `@SpringBootApplication` do?**
   It combines `@Configuration`, `@ComponentScan` and `@EnableAutoConfiguration`.
3. **Why use `${DB_PASSWORD:default}`?**
   So secrets come from the environment in each deployment instead of being committed with the code.
