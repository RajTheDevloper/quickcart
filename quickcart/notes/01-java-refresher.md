# Station 01: Java Refresher for Spring

## Interface vs abstract class
| | Interface | Abstract class |
|---|---|---|
| Purpose | contract: *what* it can do | shared state/code with gaps to fill |
| State | constants only | fields allowed |
| Per class | implement many | extend one |

Default to an interface. Use an abstract class when subclasses share fields and logic.
**Polymorphism:** the variable type is the interface, the real object decides which method runs (at runtime).
```java
PaymentMethod m = new UpiPayment();  // swap for CardPayment without touching callers
```
Spring link: services depend on interfaces, Spring injects the implementation; tests inject fakes.

## Collections
| Type | Notes | Common class |
|---|---|---|
| List | ordered, duplicates ok | ArrayList (array that grows ~1.5x) |
| Set | no duplicates | HashSet |
| Map | key -> value | HashMap, LinkedHashMap (insertion order), TreeMap (sorted) |

**HashMap inside:** array of buckets (16). `put`: `hashCode()` -> bucket index -> `equals()` to find/replace an existing key, else add to the bucket. Resizes (doubles) at 75% load. Lookup is O(1) on average. Buckets with 8+ entries become trees (Java 8+). Not thread-safe (use ConcurrentHashMap).
**Contract:** equal objects must have equal hashCodes. Without overriding both, a `HashSet` treats two objects with the same data as different (identity comparison).

## Lambdas, streams, Optional
- Lambda = inline implementation of a functional interface (one abstract method): `Predicate` (T->boolean), `Function` (T->R), `Consumer` (T->void), `Supplier` (()->T). Method reference: `Product::getName`.
- Stream = lazy pipeline: source -> intermediate (`filter`, `map`, `sorted`) -> terminal (`toList`, `count`, `sum`, `anyMatch`, `findFirst`, `collect`). Nothing runs until the terminal step.
- `Optional<T>`: explicit "may be absent". Use `map`, `orElse`, `orElseThrow`; avoid bare `.get()`. Use as a return type, not a field or parameter.

```java
products.stream().filter(p -> p.stock() > 0).map(Product::name).sorted().toList();
products.stream().collect(Collectors.groupingBy(Product::category));
repo.findBySku(sku).orElseThrow(() -> new ProductNotFoundException(sku));
```

## Records and exceptions
- Record: immutable data carrier with generated constructor, accessors (`p.name()`), `equals`, `hashCode`, `toString`. Compact constructor validates. Used for DTOs; not for JPA entities.
- Checked exceptions (`Exception`): compiler forces handling. Unchecked (`RuntimeException`): business/programming errors. Spring code uses unchecked ones, handled centrally later.
- `try / catch / finally`; custom exception = `extends RuntimeException`.

## QuickCart code built
Package `catalog`: `Product` (record), `ProductNotFoundException`, `ProductStore` (interface), `InMemoryProductStore` (LinkedHashMap), `CatalogService` (rules: lookup, in-stock, reduce stock), `CatalogDemo`.
`CatalogService` gets its store through the constructor = dependency injection by hand. Station 02 lets Spring do it; Station 04 swaps in PostgreSQL without changing the service.

## Interview questions
1. **Interface vs abstract class?** Interface = stateless contract, many allowed. Abstract class = partial implementation with state, one allowed. Default to interfaces.
2. **How does HashMap work?** Buckets; hashCode picks the bucket, equals finds the key; resize at 75%; O(1) average; trees for long chains.
3. **Why override equals and hashCode together?** Maps/sets find keys by hash first, then equals. Inconsistent overrides lose entries or create duplicates.
4. **What are lazy streams?** Intermediate operations only describe the pipeline; the terminal operation triggers execution.
5. **Why Optional?** Makes "not found" explicit and avoids NullPointerException.
6. **Checked vs unchecked exceptions?** Checked must be handled/declared; unchecked need not. Business errors are unchecked in Spring apps.
7. **Why records for DTOs?** Immutable, concise, correct equals/hashCode/toString for free.
