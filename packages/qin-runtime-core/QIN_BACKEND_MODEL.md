# Qin Backend Model

This document defines the target backend architecture for Qin.

For the user-facing application model above this backend architecture, also see:

- `QIN_APP_MODEL.md`
- `QIN_MISSION_AND_VALUE.md`
- `QIN_PRODUCT_POSITIONING.md`
- `QIN_JS_COMPATIBILITY_MODEL.md`
- `QIN_NPM_COMPATIBILITY_POLICY.md`
- `QIN_HOST_CAPABILITY_MODEL.md`

It answers one core question directly:

- If Qin does not use Node as its server runtime, what supports backend code?

The answer is:

- Qin backend code is supported by the JVM and the `.class` ecosystem.

## 1. Core Position

Qin is:

- an independent Java/JVM language
- with ESM-style source syntax
- and `.class` as the primary backend artifact
- with mixed Qin source inputs (`.ts`, `.js`, `.qin`) routed through one compilation pipeline

Qin is not:

- a Node clone
- a Java-hosted Node compatibility layer
- a server-side JavaScript runtime whose main goal is npm compatibility

For backend work, Qin should stand in the same broad category as:

- Kotlin
- Scala
- Groovy

That is:

- source language is Qin
- runtime platform is JVM
- backend ecosystem is the Java/class ecosystem

Important product clarification:

- this document defines backend architecture
- it does not define the final primary user mental model
- final-form Qin should let users think in application concepts first, while JVM backend architecture remains underneath
- backend interop by itself is not enough to justify Qin; it matters only as part of a stronger fullstack and AI-friendly language/platform story

## 2. Backend Support Stack

Qin backend should be understood in three layers.

### Layer 1: Qin Language Surface

This is the language developers write:

- modules
- classes
- methods
- decorators / annotations
- expressions
- ESM-style imports
- mixed-source file resolution across `.ts`, `.js`, and `.qin`

This layer should feel modern and ESM-like.

But its semantics are defined by Qin, not by Node.

### Layer 2: Qin Standard Library + Runtime Boundary

This layer provides language-level backend capabilities in a platform-shaped but
Qin-owned form.

Typical examples:

- collections
- strings
- JSON
- time
- HTTP abstractions
- configuration
- filesystem abstractions when Qin chooses to expose them

Important rule:

- these APIs should be defined as Qin-facing APIs first
- their implementation may be backed by Java/JDK facilities

So the standard library boundary is:

- Qin API on the outside
- Java/JDK implementation on the inside

### Layer 3: JVM / `.class` Ecosystem

This is the real backend ecosystem Qin should integrate with directly.

Examples:

- Spring Boot / Spring Framework
- Jackson
- JDBC
- JPA / Hibernate
- MyBatis
- Netty
- Redis Java clients
- Kafka Java clients
- any normal Java library reachable through `.class`

This means Qin backend code should be able to:

- compile to normal JVM classes
- carry normal reflection-visible metadata
- be consumed naturally by Java frameworks
- call Java libraries through `java:` interop and normal JVM lowering

In practice, the compiler should be able to accept a mixed workspace where:

- `.qin` and `.ts` carry Qin-authored source most of the time
- `.js` remains a first-class ESM source input for compatibility and ecosystem reuse
- `.java` may still exist as host/interoperability code and gradual migration paths, but not as a Qin language source suffix

## 3. Why This Is The Right Direction

This direction is more coherent than Node-compat mode for Qin because:

1. The implementation language and runtime are already Java/JVM.
2. The strongest backend ecosystem available locally is the `.class` ecosystem.
3. The long-term target is Kotlin-like legitimacy inside Spring, not Node emulation.
4. Full Node compatibility would consume large effort while pulling Qin away from its core identity.

## 3.5. Relation To JavaScript Runtimes

It is reasonable to say Qin has a JavaScript-like source surface.

But it is not accurate to define Qin as "just JavaScript running on the JVM".

The important distinction is:

- JavaScript runtimes such as browser engines, Node, and Bun aim to execute JavaScript as the normative language/runtime.
- Qin uses ESM-style syntax as its source-language model, while the runtime/platform model is defined by Qin on top of the JVM.

So the comparison should be:

- Bun: a JS runtime/toolchain with Zig as a major implementation language
- Qin: a JVM language platform with ESM-style source syntax

This means Qin should be evaluated primarily against:

- Kotlin/JVM
- Scala
- Groovy

not primarily against:

- Node
- Bun
- Deno

## 3.6. Similar Projects In Practice

There is no exact one-to-one equivalent to Qin in the current mainstream ecosystem.

The nearest comparisons are:

- Kotlin
  - closest in backend legitimacy model
  - independent language, JVM target, Spring-friendly ecosystem fit
  - not ESM-style in source syntax
- Scala / Groovy
  - same broad category of JVM-hosted language platforms
  - strong `.class` ecosystem integration
  - not designed around ESM-style fullstack authoring
- TypeScript
  - closest in source-language familiarity and module authoring style
  - not a JVM backend language by default
- Bun / Deno / Node
  - useful comparison for what Qin is not
  - they are JavaScript runtimes/toolchains
  - Qin is a language platform that uses ESM-style syntax without taking Node as its platform definition

So if one sentence is needed:

- Qin is closer to "Kotlin for the JVM, but with an ESM-style source language surface" than to "another Node runtime".

## 4. Design Rule For Backend Features

When adding a backend feature, the decision order should be:

1. Should this be a Qin language/stdlib concept?
2. If yes, can it be implemented cleanly on top of Java/JDK?
3. If not a Qin-owned abstraction, should Qin integrate directly with an existing JVM ecosystem library?

This implies:

- simple cross-project capabilities should prefer Qin-owned APIs
- heavy enterprise/backend capabilities should usually reuse the JVM ecosystem directly

## 5. Spring Example

Spring is the canonical example of this model.

Qin should not try to "replace Spring with a JS-style server runtime".

Instead:

- Qin source declares backend classes in `.qin`
- Qin compiler emits `.class`
- generated classes expose Java/Spring-visible metadata
- Spring consumes those classes naturally

That is exactly the direction of:

- `import { RestController } from "java:org.springframework.web.bind.annotation"`
- `.qin` controller/service classes
- declaration IR
- JVM class emission

But Spring-shaped authoring should be understood as a Stage 1 bridge, not the final Qin product surface for common app development.

## 6. Node Is Not The Backend Foundation

Node can be useful as a comparison point, but it is not the foundation of Qin backend architecture.

Qin backend should not require:

- Node runtime APIs
- CommonJS
- `node:*`
- Node loader rules
- "npm package compatibility" as the definition of correctness

At most, Qin may selectively support some pure ESM libraries in places where
they fit Qin's own language/runtime rules.

This does not forbid Node compatibility shims.
It means Node compatibility is a compatibility layer, not the platform definition.
Qin may add missing Node-adjacent behavior incrementally when third-party packages require it.

But that is optional compatibility work, not the backend foundation.

## 7. Practical Guidance

For backend development, the preferred priority should be:

1. Qin language syntax and declaration model
2. Qin standard library abstractions where they make sense
3. Java/JDK host implementation
4. Direct `.class` ecosystem integration

For concurrency, the preferred priority should be:

1. synchronous business code by default
2. explicit Qin async constructs when concurrency is needed
3. Java 25 concurrency as the host substrate
4. framework integration through normal JVM-visible types and adapters

So when asking "what supports Qin backend code?", the primary answer is:

- Qin-on-JVM
- Java/JDK as host
- `.class` ecosystem as the main backend library world

When asking "what should ordinary Qin users think they are building?", the answer should increasingly come from the Qin app model, not from raw backend layering.

## 8. Definition Of Success

This backend model is successful when:

1. Developers can write backend business code primarily in `.qin`.
2. Qin backend classes compile to normal `.class` outputs.
3. Java frameworks can consume Qin classes naturally.
4. Qin does not need Node compatibility to be productive on the server.
5. The main value of Qin backend comes from JVM integration, not from trying to mimic Node.

## 9. Native Capability Tradeoff

Choosing JVM as the backend gives Qin strong server-side leverage, but it also means
some JavaScript-native behaviors are not automatic.

Qin keeps naturally:

- ESM-style source modules
- decorator-driven declaration authoring
- JS-like expression syntax
- Java framework interoperability through emitted `.class`

Qin does not get for free:

- full prototype-chain identity
- `undefined` / coercion edge-case parity
- Node host objects and loader rules
- exact event-loop / microtask semantics
- "all ESM packages behave the same as in Node or browsers"

These are not defects in the JVM itself. They are the expected boundary between:

- a JVM language inspired by ESM
- and a native JavaScript runtime
