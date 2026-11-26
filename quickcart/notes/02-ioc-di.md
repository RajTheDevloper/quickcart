# Station 02: IoC and Dependency Injection

## The idea
- **IoC (Inversion of Control):** the framework creates objects and wires them; your code only declares what it needs.
- **ApplicationContext:** the container. **Bean:** an object it manages.
- Before: `new CatalogService(new InMemoryProductStore())` written by hand everywhere. Now Spring does it.

## Registering beans
| Annotation | Use |
|---|---|
| `@Component` | generic class |
| `@Service` | business logic |
| `@Repository` | data access (also translates persistence exceptions) |
| `@RestController` | API layer |
| `@Configuration` + `@Bean` | third-party classes you cannot annotate |

## Injection styles
```java
@Service
public class CatalogService {
    private final ProductStore store;
    public CatalogService(ProductStore store) { this.store = store; }  // single constructor: no @Autowired needed
}
```
| Style | Verdict |
|---|---|
| Constructor | use it: `final` fields, fails fast at startup, easy to unit test with plain `new` |
| Setter | optional dependencies only |
| Field `@Autowired` | avoid: hidden dependencies, not `final`, hard to test |

## How the container works (inside)
1. Component scan finds annotated classes under the main package -> bean definitions.
2. Dependency order is resolved (store before service).
3. Instantiate -> inject -> `@PostConstruct` -> ready.
4. On shutdown: `@PreDestroy`.

## Scopes
- **singleton (default):** one instance per container, shared by all threads -> beans must be stateless / thread-safe.
- prototype (new each time), request, session: rarely needed.

## Two beans of one type
Error: `Parameter 0 of constructor in CatalogService required a single bean, but 2 were found`.
Fix: `@Primary` on the default one, or `@Qualifier("inMemoryProductStore")` at the injection point. Default bean name = class name with lowercase first letter.

## Circular dependencies
A needs B and B needs A -> startup fails. It is a design smell: extract a third class or rethink responsibilities.

## QuickCart code
`InMemoryProductStore` (`@Repository`, `@PostConstruct`), `CatalogService` (`@Service`), `CatalogSeeder` (`CommandLineRunner`, runs once after the context is ready). `context.getBean(CatalogService.class) == catalog` printed `true` -> singleton.

## Interview questions
1. **What is IoC / DI?** The container creates and connects objects; dependencies are supplied from outside instead of created with `new` inside the class.
2. **Why constructor injection?** Immutability (`final`), fail-fast, no hidden state, simple tests.
3. **Default scope?** Singleton, one per container; keep beans stateless.
4. **Two beans of the same type?** `NoUniqueBeanDefinitionException`; use `@Primary` or `@Qualifier`.
5. **What does `@SpringBootApplication` scan?** Its own package and subpackages; classes outside are not found.
6. **`@Component` vs `@Service` vs `@Repository`?** All register beans; the latter two add meaning, `@Repository` adds exception translation.
