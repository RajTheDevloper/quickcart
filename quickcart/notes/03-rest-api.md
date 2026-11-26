# Station 03: First REST API

## REST in one table
| Verb | URL | Meaning | Success |
|---|---|---|---|
| GET | `/api/products` | list | 200 |
| GET | `/api/products/{sku}` | one | 200 / 404 |
| POST | `/api/products` | create | 201 + `Location` header |
| PUT | `/api/products/{sku}` | replace | 200 |
| DELETE | `/api/products/{sku}` | remove | 204 |

Rules: URLs are plural nouns; the verb is the action; GET never changes data; status codes carry meaning (2xx ok, 4xx client mistake, 5xx server bug). **Idempotent** (same result if repeated): GET, PUT, DELETE. POST is not.

## What happens inside a request
```
Client -> Tomcat -> DispatcherServlet -> HandlerMapping (which method?)
 -> argument resolvers (@PathVariable, @RequestParam, @RequestBody via Jackson)
 -> controller method -> service -> return value
 -> Jackson (HttpMessageConverter) object -> JSON -> response (status, headers, body)
```
Jackson converts JSON <-> Java objects. The record `Product` becomes `{"sku":"E1",...}` automatically.

## Annotations
| Annotation | Meaning |
|---|---|
| `@RestController` | `@Controller` + `@ResponseBody`: return data (JSON), not view names |
| `@RequestMapping("/api/products")` | base path |
| `@GetMapping` `@PostMapping` `@PutMapping` `@DeleteMapping` | verb + path |
| `@PathVariable` | value in the path `/products/{sku}` |
| `@RequestParam` | query string `?category=x` |
| `@RequestBody` | JSON body -> Java object |
| `ResponseEntity<T>` | control status code and headers |

## Layers (controller has no business logic)
`ProductController` (HTTP <-> Java) -> `CatalogService` (rules) -> `ProductStore` (data).

## Code
```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final CatalogService catalog;
    public ProductController(CatalogService catalog) { this.catalog = catalog; }

    @GetMapping
    public List<Product> list(@RequestParam(required = false) String category) {
        return category == null ? catalog.findAll() : catalog.byCategory(category);
    }
    @GetMapping("/{sku}")  public Product get(@PathVariable String sku) { return catalog.getBySku(sku); }
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product p) {
        Product saved = catalog.create(p);
        return ResponseEntity.created(URI.create("/api/products/" + saved.sku())).body(saved);
    }
    @PutMapping("/{sku}")  public Product update(@PathVariable String sku, @RequestBody Product p) { return catalog.update(sku, p); }
    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> delete(@PathVariable String sku) { catalog.delete(sku); return ResponseEntity.noContent().build(); }
}
```

## Debugging a request (Eclipse)
Breakpoint (double-click margin) -> Debug As -> call the URL -> Variables panel shows `sku` filled from the URL. F5 step into, F6 step over, F8 resume. The call stack below your code shows `DispatcherServlet` and Tomcat.

## Known gap (fixed in Station 05)
`ProductNotFoundException` currently gives 500; it should be 404. A global `@ControllerAdvice` will map exceptions to status codes.

## Common errors
| Symptom | Cause |
|---|---|
| 404 on everything | wrong path, or controller outside the scanned package |
| 415 | POST body not set to JSON content type |
| 400 | malformed JSON or wrong field type |
| 405 | right URL, wrong verb |

## Interview questions
1. **What happens when a POST reaches a Spring Boot app?** Tomcat -> DispatcherServlet -> handler mapping finds the `@PostMapping` method -> Jackson converts the body to the `@RequestBody` object -> method runs -> Jackson converts the result to JSON -> response.
2. **`@RestController` vs `@Controller`?** `@RestController` = `@Controller` + `@ResponseBody`.
3. **`@PathVariable` vs `@RequestParam`?** Path segment vs query string.
4. **Why 201 and a `Location` header on create?** 201 = created; `Location` = URL of the new resource.
5. **Which verbs are idempotent?** GET, PUT, DELETE (not POST).
6. **Why keep business logic out of controllers?** Controllers translate HTTP only; logic in services is reusable and testable without HTTP.
