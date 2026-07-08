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
- target-filtered ESM compatibility, not universal ECMAScript compatibility

The most important filter is target feasibility:

- In both `app/` and `main/`, Qin core ESM syntax is admitted only where it can lower cleanly to Qin IR and predictable JVM `.class`.
- `app/` may accept frontend input surfaces such as `.js`, `.ts`, `.vue`, and `.ovs`, plus browser host capabilities, but those do not broaden Qin core semantics.
- In `shared/`, MVP code should be `.qin` only and must stay inside the intersection supported by both the JS backend and JVM `.class` backend.
- Future `shared/` `.java` must be a Qin-managed portable Java subset, not arbitrary JVM Java.
- A feature is rejected when its clean `.class` mapping would be hard, ugly, or would weaken Qin's static structure.

## 1.1 Static Class-Lowerable Rule

Qin support is defined by static semantics, not by whether a JavaScript engine could execute the source dynamically.

For `.ts`, `.js`, and `.qin` inputs, Qin's goal is to compile the static ESM/Qin/TypeScript-like subset to native JVM `.class` code. Static TypeScript-like source is supported when its runtime meaning can be known at compile time; dynamic TypeScript/JavaScript patterns are unsupported instead of being delegated to a broad JavaScript runtime.

Supported means:

- the parser can produce a stable Qin/Slime AST for the syntax
- lowering can map the AST to Qin IR without guessing runtime object shapes
- JVM lowering can emit predictable `.class` code with fixed fields, fixed methods, normal virtual dispatch, explicit collection/map operations, or Qin-owned builtin APIs
- frontend JS emission can preserve the same semantics without adding a second accepted language path

Unsupported means:

- the feature requires full JavaScript object-model fidelity, prototype-chain fidelity, property descriptor fidelity, `Proxy`, `Reflect` full-surface parity, `eval`, `Function(...)`, `with`, CommonJS loader behavior, exact Node/V8 runtime behavior, or other hard dynamic JS engine semantics
- the compiler would need dynamic member lookup on unknown object shapes, dynamic method invocation by string, runtime prototype walking, generated compatibility adapters for multiple AST/CST shapes, or broad catch-and-continue behavior to make the feature appear successful
- clean `.class` lowering would be hard, ugly, unstable, or would weaken Qin's static structure

`as any` is not itself a dynamic runtime feature because TypeScript erases it. It becomes relevant only when it hides runtime dynamic behavior such as `obj[key]` on an unknown shape, `fn.call/apply/bind`, optional dynamic method calls, prototype walking, or multi-shape adapters. If the member is a real fixed class method or field, fix the Qin type/interface model so the compiler can see it statically instead of treating the call as dynamic JavaScript.

### 1.2 Dynamic Indexing And Map/Dict Rule

`object[name]` is supported only when the compiler can prove the base value is an indexable collection shape.

Accepted `.class`-lowerable cases:

- `const table: Map<K, V> = ...` or another explicit Qin-owned map/dictionary type.
- `const table = new Map<K, V>()` or `new Dict<K, V>()` where the constructor fixes the runtime shape.
- An object literal in a contextual `Dict`/map position, such as `const table: Dict<string, V> = { ... }`.
- A compiler-owned AST/token/field table type whose API explicitly defines dynamic lookup and lowering to `get`/`set`.

Rejected cases:

- `const x = someFunction()` followed by `x[name]` when the return type is unknown.
- `const x = importedValue` followed by `x[name]` when the imported symbol has no Qin-visible map/dictionary type.
- `const x = { a: 1 }` followed by arbitrary `x[name]` without a contextual dictionary type.
- Treating every untyped variable as a map just because Qin cannot infer its type.

Unknown type must remain unknown and fail with a clear compile-time diagnostic that asks the author to add a Qin-visible type, constructor, or dictionary annotation. It must not silently become `Map`, because that would hide mistakes, erase fixed class/member semantics, and reintroduce dynamic JavaScript object-model compatibility through a different name.

Lowering rule:

- Proven map/dictionary indexed reads lower to the target's explicit lookup operation, such as JVM `Map.get(key)`.
- Proven map/dictionary indexed writes lower to the explicit update operation, such as JVM `Map.put(key, value)`.
- Fixed fields and fixed methods use normal field/method access, not map lookup.
- Parser/compiler parameter bags and similar fixed-shape data should be modeled as classes/records/interfaces, not downgraded to maps.

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
- mixed workspace imports across legal zone surfaces

Qin should remain:

- ESM-first
- CommonJS-non-normative
- target-aware: ESM syntax is filtered by the active zone and backend

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
- `.ts` as an ordinary `shared/` source surface

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
- simple pure ESM and compiler/tooling-oriented npm packages that fit the active target

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
- async does not propagate through Qin call chains unless the Qin surface explicitly exposes `Task<T>` or another async boundary

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
- syntax that remains cleanly `.class`-feasible on JVM targets

### Tier B: Valuable but Host-Sensitive

This may be supported incrementally where real package pressure exists:

- selected filesystem/path abstractions
- selected URL/buffer-like host utilities
- selected package-resolution conventions
- selected async interop adapters
- simple npm package shapes that are frontend-safe, JVM-safe, or explicitly wrapped per target zone

These should be host-model-driven, not blindly copied from Node.

### Tier C: Deferred or Rejected

This should not be treated as core language scope:

- V8/Node edge-case fidelity
- exact microtask/event-loop behavior
- native addon support
- highly dynamic metaprogramming as a compatibility baseline
- object-model fidelity work whose main payoff is engine parity rather than Qin language value
- npm packages whose correctness requires JS engine features that do not compile cleanly to JVM `.class`

## 6. Definition Of Success

This model is successful when:

1. Qin can honestly claim mixed-source `.js/.ts/.qin` support without implying full Node parity.
2. The supported subset is stable enough for real compiler/tooling packages.
3. Unsupported capabilities fail for principled reasons, not by accident.
4. Qin gains package reuse without surrendering its own language/runtime identity.
