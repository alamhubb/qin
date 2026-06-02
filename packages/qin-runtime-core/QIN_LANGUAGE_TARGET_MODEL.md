# Qin Language Target Model

This document defines the normative language/target layering model for Qin.

For the user-facing application model above this target architecture, also see:

- `QIN_APP_MODEL.md`

It answers one core question directly:

- If Qin is one language across `shared/`, `main/`, and `app/`, how do target rules differ?

The answer is:

- Qin is one language with one core syntax/semantic model.
- Target differences come from platform capability boundaries and backend emission targets.

## 1. Core Position

Qin is:

- a Qin-owned programming language
- with an ESM-style source surface
- with JVM as the primary backend/runtime kernel
- with frontend JS emission as a first-class target
- with multi-suffix Qin source inputs: `.ts`, `.js`, and `.qin`

Qin is not:

- a Node compatibility layer
- a JS engine reimplementation
- three different dialects split across `shared/`, `main/`, and `app/`

The most accurate short definition is:

- Qin is a fullstack language with ESM-style source syntax and target-specific backends.

Product-direction clarification:

- the current target/zoning model is normative architecture
- it is not the final desired primary end-user mental model
- final-form Qin should let users think in application concepts first, with zoning mostly handled by tooling and compilation policy
- `QIN_APP_MODEL.md` defines that intended application-first surface

## 2. One Language, Three Zones

`shared/`, `main/`, and `app/` all belong to the same Qin language.

They share:

- the same parser entry
- the same frontend/lowering pipeline
- the same core language semantics
- the same default execution model
- the same explicit async language forms

They differ in:

- allowed platform capabilities
- allowed import categories
- final compilation targets

## 2.5 Source File Surface

Qin accepts three Qin-language source suffixes in the same workspace and compilation flow:

- `.ts`: ESM-style source authored in TypeScript-like Qin syntax
- `.js`: ESM-style source authored in JavaScript-like Qin syntax
- `.qin`: Qin-native source

Java source has two distinct roles:

- backend `.java` is host/JVM code that may coexist in a backend project
- frontend `.java` is a future Stage 3 Qin-managed source surface, limited to a Java 8 subset
- Qin backend code may import Java ecosystem types and frameworks through `java:`
- frontend code may not import JVM host modules through `java:`

Stage 3 frontend Java is not a direct browser JVM execution model. Its intended compiler path is:

- parse `.java` with Qin/Slime's Java8 `JavaParser` built on the Java Subhuti parser stack
- convert Java CST to a Java AST
- build a Java semantic model for the supported subset
- lower that model to Qin IR
- emit browser JS through the existing Qin JS backend path

The frontend Java subset also needs a Qin-owned Java standard-library runtime for JS.
This should be a small Java 8 subset mapping, not a full JDK port at the start:

- `java.lang`: `String`, boxed primitives where needed, `Object`, `Math`, `StringBuilder`
- `java.util`: `ArrayList`, `HashMap`, `HashSet`, `Collections`, `Objects`, `Arrays`
- runtime helpers for Java equality, null checks, method dispatch, and Java collection iteration

GWT, J2CL, TeaVM, and similar projects are useful references, but Qin should keep the
runtime surface explicit and grow it from real Java8 lowering needs.

The intended compiler behavior is:

- parse and link Qin source files through one mixed-source import system
- lower Qin-managed source into the same compilation pipeline
- emit `.class` for JVM targets
- emit `.js` for frontend targets
- use `qin.config.js` as the project/package manifest that defines entry, dependencies, workspace package discovery, and runtime/build coordination
- allow compatible npm package sources to participate in the same target compilation flow

This means Qin should support mixed projects, not suffix-isolated projects.
For Qin language source, the file suffix is an input surface, not a product boundary.

## 3. Zone Semantics

### 3.1 `shared/`

`shared/` is for platform-neutral and weak-platform Qin code.

Rules:

- intended to compile to both JVM and JS
- may contain `.ts`, `.js`, and `.qin` Qin source
- may use Qin core syntax and portable Qin stdlib only
- may not directly depend on `java:`
- may not directly depend on browser/web-specific modules

This is the portability boundary for code that should survive cross-target compilation.

### 3.2 `main/`

`main/` is the backend/JVM zone.

Rules:

- compiles to JVM `.class`
- may mix `.qin`, `.ts`, and `.js` Qin source inputs under the same project graph
- may coexist with host `.java` code in the same backend project, but `.java` is not treated as Qin source
- may use `java:` interop
- may integrate directly with JVM ecosystem libraries and frameworks
- is the natural home for Spring, JDK, and `.class`-ecosystem code

This is where Qin expresses backend business code and framework integration.

### 3.3 `app/`

`app/` is the frontend/web zone.

Rules:

- compiles to JS
- may mix `.qin`, `.ts`, and `.js` inputs under the same project graph
- Stage 3 may add `.java` inputs through a Java 8 subset parser/lowering path
- may use browser/web capabilities
- may not use `java:`

This is where Qin expresses frontend/browser behavior.

## 4. Target Mapping

The normative target matrix is:

- `shared/ -> js | jvm`
- `main/ -> jvm only`
- `app/ -> js only`

This means:

- target-specific backends are part of Qin architecture
- the language is unified, but backend legality is zone-aware
- target restrictions are language rules, not accidental tooling details

## 5. Import Policy As Language Policy

Zone-based import policy is not just an implementation detail.
It is part of Qin language design.

Current hard rules:

- `app/`: JS/web imports allowed, `java:` denied
- `main/`: `java:` and JS imports allowed where backend rules permit
- `shared/`: platform-specific imports denied

So import policy and target policy are aligned:

- if a module must remain cross-target, it must stay portable
- if a module imports JVM-only capability, it belongs in `main/`
- if a module imports browser-only capability, it belongs in `app/`

Project-level manifest note:

- zone policy belongs to the language model
- package identity, dependency declarations, workspace shape, and entry selection belong to `qin.config.js`
- see `QIN_PACKAGE_MANIFEST_MODEL.md`

## 6. Sync And Async Model

Qin execution is synchronous by default in every zone.

That means:

- `shared/` defaults to synchronous semantics
- `main/` defaults to synchronous semantics
- `app/` defaults to synchronous semantics

Async is not inherited from JavaScript runtime assumptions.
Async is a Qin language feature and must be explicit.

Preferred surface:

- `async expr`
- `async { ... }`

Unified semantic intent:

- both forms produce `Task<T>`
- `async expr` is sugar over `async { return expr }`

Target mapping:

- JVM backend maps explicit async to Java/JVM host concurrency
- JS backend maps explicit async to JS/web-side async machinery

So async is:

- Qin-owned at the language level
- target-specific at the runtime/backend implementation level

## 7. Backend Neutrality Rule

Qin frontend output and semantic IR should stay as backend-neutral as practical.

That means:

- parser should not bake in backend-specific semantics
- lowering should preserve a Qin semantic model before backend emission
- backend-specific behavior should be finalized in target backends and runtime layers

Mixed-source support does not change that rule:

- Qin source suffixes are an authoring convenience
- backend legality still comes from zoning and target policy
- host `.java` participation does not mean Qin becomes Java-first in product definition
- npm package availability does not imply universal runtime compatibility

This is why Qin keeps:

- `qin-parser` as parser boundary
- `qin-lang-frontend-adapter` as parse-output to IR lowering boundary
- separate `qin-lang-backend-jvm` and `qin-lang-backend-js`

## 8. Practical Design Rule

When designing a new Qin feature, the decision order should be:

1. Is this a Qin language feature shared by all zones?
2. If yes, can it remain portable enough for `shared/`?
3. If not portable, is it JVM-only (`main/`) or web-only (`app/`)?
4. Should target-specific behavior live in stdlib/runtime mapping rather than syntax?

This keeps Qin coherent as one language rather than a bag of target-specific exceptions.

## 9. Definition Of Success

This model is successful when:

1. Developers can think of Qin as one language across `shared/`, `main/`, and `app/`.
2. Target restrictions are predictable and enforced by policy.
3. `shared/` code is genuinely portable across JS and JVM targets.
4. `main/` integrates naturally with JVM frameworks.
5. `app/` emits clean browser-usable JS.
6. Async remains explicit and language-owned instead of ambient and Promise-driven.

## 10. Gap To Final Qin

This document defines the correct target architecture for Qin, but not the full final product shape by itself.

Current Qin still exposes more implementation layering than the final product should:

- users still often think in frontend/backend split
- `.qin + Spring Boot` currently still uses a Java host shell
- framework-shaped authoring is still more visible than final-form application-shaped authoring

So the gap is not mainly in target architecture correctness.
The gap is mainly in product abstraction level.

Final Qin should progressively add a Qin-owned application layer above this target model, so users can think first in:

- models
- actions
- queries
- pages
- forms
- auth
- deploy

while this zoning model remains the stable compiler/runtime foundation underneath.

