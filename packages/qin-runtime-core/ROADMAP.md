# Qin Runtime Roadmap

This document defines the near-term implementation plan for Qin as a Java-based runtime with JS/ESM-style syntax and JVM `.class` as the primary backend target.

## Product Position

- Qin syntax follows a constrained ES module subset.
- Qin runtime semantics are Java-oriented, not Node-oriented.
- `java:` imports are Qin language syntax, not Java source imports.
- JVM backend is generated directly with JDK Class-File API.
- JS backend exists for frontend targets, but Java interop remains JVM-only.

## Near-Term Goal

Build a minimal but credible Qin full-stack foundation:

1. Qin source can be parsed from `.qin`.
2. Qin IR remains backend-neutral enough for JVM and JS targets.
3. JVM target can emit `.class` and call selected Java standard library APIs.
4. Frontend target can emit `.js` for browser-side modules without Node semantics.

## Stage Plan

### Stage 1: JVM Interop Core

Scope:

- `const a = { age: 1 }`
- `console.log(a.age)`
- `import { Math } from "java:java.lang"`
- `console.log(Math.random())`
- `const list = new ArrayList()`
- `list.add("hello")`
- `console.log(list.size())`

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

### Stage 2: Module Graph and Shared Code

Scope:

- local `.qin` module graph
- `shared / app / main` project conventions
- import/export validation
- build target separation:
  - `jvm`
  - `js`
  - `both`

Rules:

- `java:` imports are valid only for JVM-capable modules.
- shared modules must stay inside the cross-target subset unless explicitly split later.

### Stage 3: Minimal Full-Stack Demo

Scope:

- Qin backend compiled to `.class`
- Qin frontend compiled to `.js`
- lightweight Java HTTP server
- static asset serving from `app/`
- backend API route + frontend page demo

This stage is the first credible Qin full-stack showcase.

## Backend Architecture Rules

### Frontend

- Shared parser: `java-slime`
- Qin lowering: `qin-lang-frontend-adapter`
- Output: `qin-lang-ir`

### IR

IR must stay independent from:

- Java parser details
- JVM bytecode APIs
- browser APIs

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
- Node module semantics
- Promise and async runtime
- Vite reimplementation
- automatic translation from Qin source to Java source
