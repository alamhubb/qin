# Qin + Spring Architecture

This document defines the long-term direction for using `.qin` as a first-class
language for Spring Boot applications.

For the broader backend layering model behind this Spring direction, also see:

- `QIN_APP_MODEL.md`
- `QIN_BACKEND_MODEL.md`

It exists to keep implementation work aligned with the product goal:

- Qin should feel like ESM / TypeScript on the surface
- Qin should integrate with JVM / Spring like a real compiled language
- Qin should not depend on ad-hoc Java host glue forever
- Qin should remain an independent JVM language standard rather than a Node compatibility layer

## 1. Product Position

The target is not "Java source generation".

The target is:

- `.qin` as the source language
- JVM `.class` as the primary backend artifact
- Java / Spring as the runtime ecosystem

Product-layer clarification:

- Spring integration is part of Qin's backend execution story
- it is not the final preferred user mental model for ordinary app authoring
- final-form Qin should expose more Qin-owned app abstractions above raw Spring-shaped code

In that sense, Qin is best understood as:

- an ESM-style JVM language
- an independent Java/JVM backend language with its own runtime boundary
- with interoperability goals closer to Kotlin than to scripting runtimes

Important clarification:

- ESM is a source-language inspiration and module model, not a promise of Node compatibility
- Qin does not adopt Node as its platform standard
- Qin is free to define Qin-specific runtime/library behavior where JVM reality requires it

## 2. Design Goal

When a Qin backend module is compiled to `.class`, the result should be
behaviorally equivalent to an equivalent Java implementation.

Important clarification:

- We do **not** require byte-for-byte identical class files
- We **do** require equivalent JVM / reflection / framework-visible metadata

For Spring workloads, that means Qin-generated classes should preserve:

- class name and package
- method signatures
- parameter and return types
- runtime-visible annotations
- framework-relevant reflection behavior

## 3. Source-Level Syntax Direction

Qin should use standard ESM import syntax with `java:` module specifiers:

```ts
import { RestController, GetMapping } from "java:org.springframework.web.bind.annotation"
```

This is preferred over Java-style imports and over custom syntax forms like:

```ts
import { RestController } from java:"org.springframework..."
```

Rationale:

- keeps Qin aligned with ESM mental models
- keeps `java:` and other Qin module spaces under one consistent import model
- avoids introducing framework-specific surface syntax
- avoids coupling Qin language design to Node-specific module rules

## 4. Current Spring Path

Today the minimal working path is:

1. `.qin` source is parsed by Java Slime
2. `java:` imports are resolved to Java binary names
3. decorators such as `@RestController` / `@GetMapping` are matched via import bindings
4. class metadata is emitted through JDK Class-File API
5. generated class is registered into Spring Boot at startup

This path is intentionally a transitional bridge.

It proves that Qin-authored controller code can already participate in Spring.

Recent hardening on this bridge:

- reusable Spring host support now centralizes Qin class loading and bean registration
- application shells no longer need to hand-roll controller lookup and bean definition code
- Spring bean discovery now prefers normal Spring stereotype metadata instead of binary-name assumptions
- multiple `.qin` source files can now be compiled into one shared declaration index and defined through one shared classloader
- `.qin` Spring beans can now collaborate across source files instead of being limited to one controller-only source

This is strong progress for Stage 1, but it still belongs to the bridge layer between Qin's current implementation shape and Qin's final application-first product shape.

## 5. Transitional vs Long-Term Boundaries

### Transitional pieces we accept for now

- a dedicated Spring controller compiler path
- application-layer registration glue in Java startup code
- minimal supported controller subset

### Things that must become first-class over time

- class / method / annotation / parameter metadata in Qin IR
- generic JVM annotation lowering
- generic method parameter lowering
- reusable framework integration layer
- higher-level Qin app abstractions above direct controller/service authoring

## 6. Architecture Boundaries

### Core language / runtime layers

These layers should remain framework-neutral:

- `qin-lang-ir`
- `qin-lang-frontend-adapter`
- `qin-lang-lowering-jvm`
- `qin-lang-backend-jvm`
- `qin-runtime-core`

Framework-neutral means:

- no hard dependency on Spring Boot runtime APIs
- no framework-specific assumptions in the general language pipeline
- no Node runtime compatibility assumptions in the general language pipeline

### Framework integration layer

Spring-specific registration and conventions should live at the application
layer today, and later migrate into a dedicated Spring integration boundary.

That boundary may eventually be implemented as:

- a Spring-focused Qin plugin
- or a dedicated Spring/JVM framework integration package

But it should not pollute generic runtime orchestration code.

## 7. Java Import Semantics

`java:` import semantics must be shared.

The same meaning of:

```ts
import { GetMapping as GET } from "java:org.springframework.web.bind.annotation"
```

must apply consistently across:

- frontend lowering
- Spring-specific compilation
- future generic annotation lowering

Current rule:

- named imports only
- no side-effect `java:` imports
- local alias is the binding used inside Qin source

Verified locally:

```ts
import { Autowired as A } from "java:org.springframework.beans.factory.annotation"
import { RestController as RC, GetMapping as GET } from "java:org.springframework.web.bind.annotation"

@RC
class HelloController {
  @A
  service: HelloService

  @GET("/api/hello")
  hello() {
    return this.service.message()
  }
}
```

This alias form is not just a planned syntax.
It is already validated end-to-end through the current Qin + Spring bridge.

Language-standard clarification:

- `java:` is part of Qin's own module system
- it is not borrowed from Node and does not imply `node:` compatibility
- Spring/backend evolution should continue to target Qin-on-JVM semantics first

## 8. Current Known Gaps

The current Spring path is intentionally narrow.

Supported today:

- class decorator equivalent to `@RestController`
- method decorator equivalent to `@GetMapping`
- method decorator equivalent to `@PostMapping`
- class decorator equivalent to `@Service`
- one `@RequestBody` method parameter
- parameter decorator alias resolution through shared `java:` imports
- field decorator / annotation lowering for Spring/Jackson-style runtime annotations
- typed parameters for:
  - `string`
  - `boolean`
  - `number`
  - `java:`-imported JVM reference types
- local Qin-declared parameter types referenced from the same source file or merged Spring source set
- local Qin class fields lowered into declaration IR and emitted into JVM classfiles
- literal local Qin field default values lowered into declaration IR and emitted through generated no-arg constructors
- local Qin DTO classes compiled together with Spring beans inside one merged declaration compile
- basic DTO getter runtime shape emitted for local fields
- basic DTO setter runtime shape emitted for local fields
- basic DTO default field value runtime shape emitted for local fields
- basic DTO all-fields constructor runtime shape emitted for local fields
- DTO all-fields constructor parameter names emitted through `MethodParameters`
- host shell can now load multi-class Qin compile units instead of only a single generated controller class
- host shell can now load multiple `.qin` source files into one shared classloader
- declaration method returns now support:
  - string / boolean / number / null literals
  - `payload.name` style parameter property access
  - `this.service` style receiver-based field/property access
  - `this.service.message()` style receiver-based instance method calls
- `.qin @Service + .qin @RestController` collaboration now works through normal Spring metadata plus field injection
- string literal return bodies

Missing for the next phase:

- richer parameter subsets beyond single `@RequestBody`
- richer local Qin-declared DTO/class constructor shapes beyond the current synthesized no-arg + all-fields path
- explicit constructor syntax / constructor IR
- constructor injection / primary-constructor-style bean wiring
- broader declaration expression coverage beyond the current receiver/property/method subset
- typed return inference beyond the current minimal subset
- richer diagnostics with source locations

Recently validated:

- import alias decorators work in the running Spring sample
- generated controller metadata now has an explicit Java 21 classfile target
- generated runtime-visible annotations are verified through classfile-level inspection
- parameter decorators are now parsed through Java Slime mainline parser/AST
- `@RequestBody payload: string` lowers through shared declaration IR
- `@RequestBody payload: Payload` with `import { Payload } from "java:..."` emits the correct JVM method descriptor
- `@RequestBody payload: Payload` with local Qin `class Payload { ... }` emits the correct JVM method descriptor
- local Qin DTO classes now compile into their own `.class` outputs through the shared declaration emitter
- local Qin DTO classes now expose basic generated getters for field access / bean-style consumption
- local Qin DTO classes now expose basic generated setters for field mutation / bean-style consumption
- local Qin DTO literal field defaults now execute through generated no-arg constructors
- local Qin DTO classes now expose a generated all-fields constructor
- generated all-fields constructors now carry field parameter names in JVM metadata
- multi-source Qin Spring beans can now be compiled together and resolved through one declaration index
- `.qin` service/controller collaboration now works across separate source files
- field-injected `.qin` Spring services can now be called through `this.service.message()` and served over real HTTP

## 9. Required Evolution of Qin IR

The largest remaining architecture gap is the lack of a first-class IR model for:

- classes
- methods
- annotations / decorators
- parameters

As long as those concepts do not exist in shared IR, framework integrations must
continue using side channels or specialized compilers.

That is acceptable temporarily, but not as the final architecture.

## 10. Short-Term Implementation Policy

Until class/annotation IR exists:

- we may keep Spring support on a dedicated bridge path
- but new work should still move toward reuse and shared semantics

That means:

- share `java:` import semantics
- avoid duplicating annotation-resolution rules
- avoid moving Spring runtime dependencies into generic runtime packages

## 11. Medium-Term Target

The next meaningful milestone is:

- `.qin` Spring business classes beyond controller-only demos
- constructor-oriented DI that feels natural for a compiled JVM language
- fewer application-specific special cases
- clearer migration from Spring-shaped authoring to Qin-owned app-level authoring

The architecture target after that is:

- `.qin` class metadata lowered through shared JVM IR machinery
- Spring consumes generated classes as naturally as it consumes Java/Kotlin classes

## 12. Kotlin-Level Target

The long-term target is not merely "Qin can call Spring".

The target is that Qin can enter the JVM / Spring ecosystem with the same kind of
compiler-level legitimacy that Kotlin has.

That means all of the following must become true:

- `.qin` class declarations are first-class language constructs
- methods, parameters, return types, and annotations are first-class IR constructs
- JVM lowering emits framework-visible metadata through the shared compiler pipeline
- Spring-specific behavior is mostly a consequence of normal JVM metadata, not one-off compilers
- `.qin` backend code can be authored primarily in Qin, with Java retained only for optional host/bootstrap shells

In practical terms, "Kotlin-level" for Qin means:

1. Qin does not generate Java source as the primary path
2. Qin does not require framework-specific AST side channels as the primary path
3. Qin emits `.class` files whose reflection-visible behavior matches equivalent Java/Kotlin code
4. Frameworks consume Qin-generated classes naturally via standard JVM metadata
5. Qin remains independent from Node platform rules even while using ESM-style source syntax

## 13. Required Compiler Evolution

To reach that target, the compiler must evolve in this order:

### Stage A: Declaration IR

Qin must grow first-class IR nodes for:

- classes
- methods
- parameters
- annotations
- type references

Without this, the compiler remains script-oriented and framework support must
continue using special bridges.

### Stage B: Generic JVM Declaration Lowering

Once declaration IR exists, JVM lowering must become able to lower:

- class identity and hierarchy
- method descriptors
- parameter tables
- runtime-visible annotations
- constructor metadata

This lowering must be framework-neutral.

`@RestController` and `@GetMapping` should eventually be no more special than
any other runtime-visible Java annotation.

### Stage C: Spring Integration On Top Of Normal Class Metadata

After generic declaration lowering exists:

- Spring should consume Qin-generated classes through normal classpath scanning,
  registration, or bean loading
- dedicated Spring controller compilers should shrink or disappear
- framework glue should move toward an integration layer, not stay in the language core

### Stage D: Ecosystem Parity Hardening

After the main lowering path is in place, Qin still needs validation layers for:

- reflection parity
- serialization / Jackson compatibility
- Spring MVC parameter binding
- proxy / AOP compatibility
- diagnostics and tooling

## 14. Current Priority

The highest-priority architecture task now is:

- move from script-first IR to mixed script + declaration IR

Concretely, that means the next compiler work should focus on:

1. declaration IR design
2. frontend lowering from Slime AST into declaration IR
3. generic JVM declaration emission

This is more important than adding many new Spring decorators directly on the
current bridge path.

## 15. Revised Definition of Success

This direction is successful when all of the following become true:

1. Developers write Spring backend business code primarily in `.qin`
2. Qin source uses ESM-style imports, including `java:` imports
3. Generated `.class` files behave equivalently to Java/Kotlin classes for Spring
4. Framework-specific glue is isolated from framework-neutral compiler/runtime layers
5. Most Spring-facing features no longer require one-off special compilers
6. Declaration metadata flows through shared IR and shared JVM lowering
7. Backend language/runtime semantics are defined by Qin-on-JVM, not by Node compatibility requirements
