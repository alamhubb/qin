# Qin Host Capability Model

This document defines the host-capability boundary for Qin.

It answers one core question directly:

- If Qin is not defined by Node or browser runtime semantics, where do platform capabilities come from?

The answer is:

- from Qin-defined capability layers mapped onto concrete hosts
- with different availability in `shared/`, `main/`, and `app`

Related documents:

- `QIN_LANGUAGE_TARGET_MODEL.md`
- `QIN_BACKEND_MODEL.md`
- `QIN_JS_COMPATIBILITY_MODEL.md`
- `QIN_NPM_COMPATIBILITY_POLICY.md`
- `QIN_ASYNC_MODEL.md`
- `QIN_CURRENT_SUPPORT_MATRIX.md`

## 1. Core Position

Qin should not treat any host as "the language itself".

Instead:

- Qin defines the language
- hosts provide capabilities
- targets map Qin capabilities onto host implementations

In current architecture:

- JVM is the primary backend host
- browser/JS output is the primary frontend host
- Node may appear as an optional compatibility reference point, not as the normative platform
- ESM source syntax is target-filtered by host capability and `.class` feasibility

## 2. Host Layers

Qin capability should be understood in three layers.

### Layer 1: Qin Core Language

Always language-owned:

- syntax
- modules
- declarations
- expressions
- explicit async surface
- Qin builtin layer

### Layer 2: Qin Host-Neutral Capability APIs

These are capabilities Qin may define once and map per target:

- collections
- strings
- JSON
- time
- task/concurrency abstractions
- configuration
- HTTP-facing abstractions
- selected filesystem abstractions when Qin chooses to expose them

### Layer 3: Concrete Host Backing

These are target-specific implementations:

- JVM/JDK and `.class` ecosystem for backend
- browser/web APIs for frontend
- optional compatibility adapters for selected Node-like package expectations

## 3. Zone Capability Matrix

### `shared/`

`shared/` should use only portable Qin capabilities.

MVP source rule:

- `.qin` only
- no ordinary `.js` or `.ts`
- future `.java` only as a Qin-managed portable Java subset

Allowed direction:

- Qin core language
- portable Qin stdlib

Not allowed as normative dependency:

- `java:`
- browser-only globals
- Node-only globals
- bare npm package capabilities unless they are explicitly dual-target-safe or wrapped by portable Qin stdlib

### `main/`

`main/` is the JVM/backend zone.

Allowed direction:

- Qin core language
- Qin builtin/stdlib surface
- `java:` interop
- JVM ecosystem libraries
- ESM-style source that compiles cleanly to JVM `.class`

Not required:

- Node runtime parity
- DOM/browser objects

### `app/`

`app/` is the frontend/browser zone.

Allowed direction:

- Qin core language
- browser/web-capable Qin output
- frontend-target libraries
- frontend input surfaces such as `.js`, `.ts`, `.vue`, and `.ovs`
- browser host capabilities, kept out of `shared/`

Not allowed:

- `java:`

## 4. Node Capability Rule

Node capability should be treated as optional compatibility work, not as the baseline host contract.

That means:

- Qin may add selected Node-adjacent adapters
- Qin should not define correctness by Node behavior
- Qin should not promise `node:*` compatibility as a default language guarantee

Examples of possible adapter candidates later:

- path-like behavior
- url-like behavior
- selected filesystem utilities
- selected package-resolution conventions

Examples of non-baseline Node surfaces:

- `child_process`
- `worker_threads`
- `cluster`
- N-API/native addons
- full `process` parity
- full event-loop compatibility
- exact Promise/microtask host semantics
- CommonJS loader compatibility as a baseline contract

## 5. Browser Capability Rule

Browser capabilities belong to the frontend target, not to Qin as a backend language rule.

So:

- DOM APIs are frontend-host capabilities
- they should not be assumed in backend/JVM execution
- `app/` may target them
- `shared/` should avoid them unless Qin later introduces an explicit portable abstraction

## 6. Async Host Rule

Async behavior is Qin-owned at the language level and host-mapped at runtime.

That means:

- Qin surface should describe `Task<T>` and explicit `async`
- JVM host may use Java 25 concurrency
- JS host may use target-side async machinery
- Node microtask semantics are not the normative definition
- async does not propagate through ordinary Qin call chains by default
- `await`, where supported for frontend or interop, must not redefine Qin core async semantics

## 7. Design Rule For New Capabilities

When adding a new capability, the decision order should be:

1. Is this language-owned?
2. Is this Qin-stdlib-owned?
3. Is this portable enough for `shared/`?
4. If not portable, is it `main/`-only or `app/`-only?
5. Does it need a host adapter, or direct host interop?

This prevents accidental host leakage into the language definition.

## 8. Definition Of Success

This model is successful when:

1. Qin's language identity stays independent from Node and browser host details.
2. `shared/main/app` capability differences remain explicit and predictable.
3. npm compatibility can grow through clear host-adapter work instead of platform confusion.
4. Backend remains JVM-native while frontend remains JS/web-targeted.
