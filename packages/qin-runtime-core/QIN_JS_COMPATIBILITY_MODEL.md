# Qin JS Compatibility Model

This document defines the normative JavaScript/TypeScript compatibility boundary for Qin.

It answers one core question directly:

- If Qin accepts `.js`, `.ts`, and `.qin`, which JS/TS capabilities are part of Qin's real support target?

The answer is:

- Qin should support a deliberate, documentable JS/TS subset as part of Qin's own language/runtime model.
- Qin should not promise full ECMAScript engine parity or full Node compatibility.

Related documents:

- `QIN_LANGUAGE_TARGET_MODEL.md`
- `QIN_BACKEND_MODEL.md`
- `QIN_BUILTINS_STRATEGY.md`
- `QIN_NPM_COMPATIBILITY_POLICY.md`
- `QIN_HOST_CAPABILITY_MODEL.md`
- `QIN_JS_ON_JVM_FEASIBILITY.md`
- `QIN_CURRENT_SUPPORT_MATRIX.md`

## 1. Core Position

Qin is:

- an ESM-style language platform
- one mixed-source language across `.js`, `.ts`, and `.qin`
- JVM-native on the backend
- JS-emitting on the frontend

Qin is not:

- a full JavaScript engine
- a full TypeScript compiler replacement
- a Node-defined runtime surface

So Qin compatibility should be described as:

- source compatibility where it serves Qin's language goals
- runtime compatibility where it fits Qin's own backend/frontend model

## 2. Supported-by-Design Surface

The following categories are part of Qin's intended mainline compatibility target.

Support-target clarification:

- the items below are the features Qin should actively try to support
- exact Node/V8 identity is not required unless explicitly stated
- see `QIN_JS_ON_JVM_FEASIBILITY.md` for priority and deferred-feature rationale

### 2.1 ESM Module Model

Qin should support:

- `import` / `export`
- named exports
- default exports
- alias imports such as `import { foo as bar } from "..."`
- scoped package specifiers such as `@scope/pkg`
- mixed workspace imports across `.js`, `.ts`, and `.qin`

Qin should remain:

- ESM-first
- CommonJS-non-normative

## 2.2 Core JS Syntax

Qin should support ordinary modern JS authoring patterns such as:

- `const` / `let`
- functions
- classes
- arrow functions
- object literals
- array literals
- template strings
- destructuring
- spread syntax
- default parameters
- `if` / `for` / `while` / `switch`
- `try` / `catch` / `finally`

## 2.3 Core TS Surface

Qin should support the practical TypeScript-like authoring surface that helps mixed-source migration and package reuse.

That includes:

- type annotations
- interface declarations
- type aliases
- typed class fields and methods
- ordinary generic declarations
- decorator-adjacent authoring patterns that Qin maps to its own lowering/runtime path

This does not mean Qin must immediately support:

- the full TypeScript type-checker feature set
- advanced type-level programming
- every TS-only emit edge case

## 2.4 Qin-Owned Builtin Surface

Qin should support a practical JS-style builtin layer, including:

- `console`
- `Math`
- `JSON`
- `Array`
- `Map`
- `Set`
- `Object`
- `Number`
- `String`
- `Boolean`
- `Date` selected surface
- `Error`
- `RegExp` selected surface

These builtins are:

- Qin-defined at the language/runtime level
- Java-backed on the JVM target
- not required to match Node/V8 edge cases exactly

See `QIN_BUILTINS_STRATEGY.md`.

## 2.5 Priority Support Target

The following groups should be treated as active support targets for Qin-on-JVM:

- ESM module authoring
- modern JS syntax used in ordinary application/tooling code
- practical TS surface
- classes/functions/closures
- decorators mapped into Qin/JVM metadata flow
- Qin-owned builtin subset
- pure ESM and compiler/tooling-oriented npm packages

This is the mainline implementation direction.

## 2.6 Current Proven Runtime Subset

The following runtime shapes are already proven by smoke coverage and should be treated as part of Qin's current working JS/npm subset:

- bare ESM package import
- named/default export consumption
- array `for...of`
- template literals
- `+=` compound assignment
- `++` update expressions
- `try` / `catch`
- `new Error(...)`
- member access such as `error.message`

This is still a subset, not a claim of full JS engine parity.

## 3. Explicit Non-Goals

The following are not normative support goals in the current Qin architecture direction:

- full ECMAScript edge-case parity
- exact prototype/object-model fidelity
- `delete obj.x` fidelity
- sparse-array fidelity
- exact `this` / `arguments` edge-case parity
- full coercion parity for `==`
- full `undefined` / `null` / `NaN` edge-case parity
- `Symbol`-heavy runtime semantics
- full prototype-chain fidelity
- full property descriptor semantics
- `Proxy` as a required compatibility baseline
- `Reflect` full-surface parity
- `eval`
- dynamic `Function(...)`
- exact generator/runtime-engine parity
- source-map-grade native-JS debug fidelity as a language baseline
- `with`
- CommonJS semantics
- automatic Node loader parity
- exact Promise/microtask semantics
- exact Node event-loop semantics
- full Node builtin parity
- native addon / N-API compatibility

These are expensive, high-instability areas that would pull Qin toward "rebuild a JS engine" instead of "build a coherent JVM fullstack language".

## 4. Async Compatibility Rule

Qin does not inherit JavaScript's Promise-first execution model as its normative language rule.

Qin's rule is:

- synchronous by default
- `async` is explicit
- `Task<T>` is the Qin-facing async abstraction

So compatibility should not be phrased as:

- "any Promise-based JS code is automatically Qin-native"

Instead it should be phrased as:

- Promise-heavy packages may require adaptation
- Qin-native async semantics remain Qin-owned

See `QIN_ASYNC_MODEL.md`.

## 5. Compatibility Tiers

When deciding whether a JS/TS capability belongs in Qin, use these tiers.

### Tier A: Core Compatibility

This should be supported proactively because it is central to Qin's language promise:

- ESM imports/exports
- ordinary modern JS syntax
- practical TS authoring syntax
- Qin builtin subset
- ordinary classes/functions/closures/object literals

### Tier B: Valuable but Host-Sensitive

This may be supported incrementally where real package pressure exists:

- selected filesystem/path abstractions
- selected URL/buffer-like host utilities
- selected package-resolution conventions
- selected async interop adapters

These should be host-model-driven, not blindly copied from Node.

### Tier C: Deferred or Rejected

This should not be treated as core language scope:

- V8/Node edge-case fidelity
- exact microtask/event-loop behavior
- native addon support
- highly dynamic metaprogramming as a compatibility baseline
- object-model fidelity work whose main payoff is engine parity rather than Qin language value

## 6. Definition Of Success

This model is successful when:

1. Qin can honestly claim mixed-source `.js/.ts/.qin` support without implying full Node parity.
2. The supported subset is stable enough for real compiler/tooling packages.
3. Unsupported capabilities fail for principled reasons, not by accident.
4. Qin gains package reuse without surrendering its own language/runtime identity.
