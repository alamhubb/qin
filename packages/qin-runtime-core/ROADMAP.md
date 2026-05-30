# Qin Runtime Roadmap

This document defines the near-term implementation plan for Qin as an independent Java/JVM language with ESM-style syntax, mixed Qin-source inputs, and JVM `.class` as the primary backend target.

## Product Position

- Qin app model is documented in `QIN_APP_MODEL.md`.
- Qin product positioning is documented in `QIN_PRODUCT_POSITIONING.md`.
- Qin syntax follows a constrained ES module subset.
- Qin runtime semantics are Java-oriented, not Node-oriented.
- Qin is not a Node clone and does not treat Node as its platform standard.
- Node compatibility may still be added incrementally as a compatibility layer when real packages require it.
- `java:` imports are Qin language syntax, not Java source imports.
- JVM backend is generated directly with JDK Class-File API.
- JS backend exists for frontend targets, but Java interop remains JVM-only.
- Qin language/target zoning is documented in `QIN_LANGUAGE_TARGET_MODEL.md`.
- Qin JS/TS compatibility boundary is documented in `QIN_JS_COMPATIBILITY_MODEL.md`.
- Qin npm package compatibility policy is documented in `QIN_NPM_COMPATIBILITY_POLICY.md`.
- Qin host/runtime capability boundary is documented in `QIN_HOST_CAPABILITY_MODEL.md`.
- Qin JS-on-JVM support-vs-deferred feature policy is documented in `QIN_JS_ON_JVM_FEASIBILITY.md`.
- Qin frontend lifecycle direction is Qin-owned; Vite is a reference point for lifecycle concepts, not a dependency or compatibility target.
- Qin CSSTS integration direction is documented in `QIN_CSSTS_INTEGRATION_MODEL.md`.
- Spring integration direction is documented in `SPRING_QIN_ARCHITECTURE.md`.
- Backend support layering is documented in `QIN_BACKEND_MODEL.md`.
- Builtin object/runtime policy is documented in `QIN_BUILTINS_STRATEGY.md`.
- Async/concurrency policy is documented in `QIN_ASYNC_MODEL.md`.
- Current target-zoning model is documented in `QIN_LANGUAGE_TARGET_MODEL.md`.
- Final product direction is application-first and AI-native, even though current implementation still exposes more host/framework layering than the final Qin shape should.

Normative language rule:

- ESM syntax is adopted because it is a clean module/programming model.
- Qin owns its own runtime/library standard.
- `shared/`, `main/`, and `app/` are one Qin language, not separate dialects.
- Target differences are expressed through zoning rules and backend mapping.
- Compatibility with Node, CommonJS, or npm package behavior is not the platform goal by default.
- Selective compatibility work is allowed when it helps Qin run important third-party packages without changing Qin's core definition.
- npm compatibility should grow first through Qin-compatible ESM/compiler/tooling packages rather than through full Node runtime emulation.
- high-cost JS engine fidelity features should remain explicitly deferred unless they unlock major Qin value.
- Backend execution model is synchronous by default; async is explicit, not ambient.

## Near-Term Goal

Build a minimal but credible Qin full-stack foundation:

1. One Qin language can be authored across `shared/`, `main/`, and `app`.
2. Qin IR remains backend-neutral enough for JVM and JS targets.
3. `shared/` can stay inside the cross-target portable subset.
4. `main/` can emit JVM `.class` and integrate with Java/JVM ecosystem APIs.
5. `app/` can emit browser-usable `.js` without Node semantics.
6. `.ts`, `.js`, and `.qin` can participate in one mixed-source Qin workspace graph.
7. Java code can coexist as host/interoperability code for backend projects, but not as a Qin source suffix.
8. compatible npm packages can be admitted through a graded Qin-compatibility policy instead of an "all npm works" assumption.

Also make the current implementation converge toward the final product story:

9. users can run a Qin application without having to manually reason about backend runtime assembly
10. current framework-host integration continues to shrink behind Qin-owned application/runtime boundaries
11. documentation clearly separates:
   - current implementation shape
   - final product shape
   - migration gaps between them
12. application-first authoring becomes clearer even while `shared/main/app` remains the technical zoning foundation

## Current Execution Priority

The current implementation priority is strictly Stage 1.

That means the team should first finish Qin as a normal, usable fullstack language before pushing hard on Stage 2 app-model abstractions.

Immediate priority:

- stabilize one language across `shared/`, `main/`, and `app`
- harden JVM backend compilation and JS frontend emission
- keep mixed-source Qin support consistent across `.ts`, `.js`, and `.qin`
- keep Java host interop clearly separated from Qin source semantics
- keep `.qin + Spring Boot` as the practical backend bridge
- improve `qin run`, `qin dev`, and `qin build` as real developer workflows
- make `qin dev` converge on one Qin-owned single-process dev server for `main/` + `app/`
- improve module policy, diagnostics, and target consistency
- push npm/package support first through Class-A Qin-compatible packages, then selective host-adapter work
- actively support the ESM/JVM-friendly feature set; do not burn Stage-1 effort on engine-parity work
- Vue frontend integration should run through Qin's native frontend pipeline, not through Vite or `@vitejs/plugin-vue`
- `lang=cssts` should resolve to the npm `cssts` package as the only formal implementation source

Explicitly not the current primary implementation focus:

- large new Qin-owned app-model syntax surface
- hiding all framework/host details before Stage 1 is stable
- prioritizing product-layer abstraction over compiler/runtime completeness

## Stage Plan

### Stage 1: Fullstack Language Bootstrap

Scope:

- `const a = { age: 1 }`
- `console.log(a.age)`
- `import { Math } from "java:java.lang"`
- `console.log(Math.random())`
- `const list = new ArrayList()`
- `list.add("hello")`
- `console.log(list.size())`
- working `.qin + Spring Boot` backend demo
- working JS-emission frontend target
- mixed-source Qin input support across `.ts`, `.js`, and `.qin`
- Java host/interop participation for backend projects where needed
- working single-port `qin dev` path for `app/index.html + app/main.js + main/main.qin`
- stable `shared/main/app` target-zoning model

Product meaning:

- Qin is already presented as a fullstack language
- even though framework-host integration is still visible in current implementation

Rules:

- Only named ESM imports are allowed for `java:`.
- Imported items represent Java classes only.
- Case-sensitive binding, matching ESM semantics.
- Only public constructors and public methods are callable.
- Parameter support is intentionally small:
  - integer literals
  - string literals
  - zero or more arguments

Out of scope:

- fields and bean-property auto mapping
- wildcard imports
- default imports
- dynamic reflection fallback
- async semantics

### Stage 2: Qin-Owned Application Layer

Scope:

- local module graph and target zoning continue to mature
- introduce Qin-owned app abstractions above raw framework structure
- begin converging authoring toward:
  - model
  - query
  - action
  - page
  - form
  - auth
  - job
  - deploy

Rules:

- `java:` imports are valid only for JVM-capable modules.
- `shared/` targets both JS and JVM and must stay inside the portable subset.
- `main/` targets JVM only.
- `app/` targets JS only.
- target zoning remains real architecture, but should become less prominent in end-user mental models

### Stage 3: AI-Native App Platform

Scope:

- Qin app model becomes the primary authoring surface
- `qin dev` becomes the default fullstack local workflow
- `qin deploy` becomes the default deployment workflow
- target/runtime expansion is increasingly hidden behind Qin tooling

This stage is the intended final product direction.

## Backend Architecture Rules

### Frontend

- Shared parser infrastructure: `java-slime`
- Qin parser: `qin-parser`
- Qin lowering: `qin-lang-frontend-adapter`
- Output: `qin-lang-ir`

### IR

IR must stay independent from:

- Java parser details
- JVM bytecode APIs
- browser APIs
- Node platform contracts

### JVM Backend

Responsibilities:

- class generation with Class-File API
- constructor resolution
- public method resolution
- primitive boxing when required for runtime built-ins such as `console.log`

### JS Backend

Responsibilities:

- emit browser-usable JS for the supported Qin subset
- reject unsupported Java interop forms with explicit diagnostics

## Recommended Implementation Order

1. Extend IR for Java constructor and instance call nodes.
2. Extend frontend lowering for:
   - `new`
   - instance method call
   - integer/string call arguments
3. Extend JVM backend generation for:
   - constructor invocation
   - instance method invocation
   - argument loading and primitive boxing
4. Add demos and run entries.
5. Tighten diagnostics and module validation.

## Hard Non-Goals For Now

- full ECMAScript compatibility
- exact prototype/object-model fidelity
- `Proxy`, `eval`, and dynamic runtime code generation as baseline compatibility goals
- Node module semantics
- Node platform compatibility as a product requirement
- universal npm parity without compatibility classification
- Promise-first / JavaScript-event-loop-style async runtime
- Vite dependency, bridge, fallback, or compatibility runtime
- automatic translation from Qin source to Java source

Also not the final product goal:

- exposing frontend/backend layering as the primary long-term end-user mental model
- requiring common Qin app authors to directly think in Spring controller/service wiring first

## Async Direction

Qin backend async direction is:

- default synchronous execution model
- explicit async opt-in
- no Promise/`await`-first programming style for server code
- Java 25 concurrency as the host implementation foundation

Preferred language surface:

- `async expr`
- `async { ... }`
- unified return type `Task<T>`
- explicit waiting such as `task.join()`

This keeps Qin aligned with:

- Spring MVC / JVM backend intuition
- Kotlin-like server ergonomics
- explicit concurrency instead of ambient async contagion
