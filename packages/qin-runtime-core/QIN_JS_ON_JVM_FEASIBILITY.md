# Qin JS-on-JVM Feasibility

This document defines which JavaScript/TypeScript-facing language features are good support targets for Qin-on-JVM, and which are intentionally deferred.

It answers one core question directly:

- Given the semantic gap between JVM and native JavaScript runtimes, which JS-facing features should Qin actively support first?

The answer is:

- Qin should actively support the ESM/JVM-friendly surface that strengthens Qin as a language.
- Qin should defer the high-cost features that would mainly drag Qin toward V8/Node reimplementation work.

Related documents:

- `QIN_JS_COMPATIBILITY_MODEL.md`
- `QIN_NPM_COMPATIBILITY_POLICY.md`
- `QIN_HOST_CAPABILITY_MODEL.md`
- `QIN_ASYNC_MODEL.md`
- `QIN_BACKEND_MODEL.md`

## 1. Core Principle

Qin is not trying to become:

- a full JavaScript engine
- a full Node runtime
- a compatibility-first clone of V8 semantics

Qin is trying to become:

- an ESM-style fullstack language
- JVM-native on the backend
- practical for mixed `.js/.ts/.qin` source authoring

So the main support rule is:

- support what strengthens Qin as a language platform
- defer what mostly serves engine-level emulation

## 2. Strong Support Targets

These are good long-term support targets because they fit the JVM well enough and are central to Qin's language promise.

### 2.1 Language/Module Features

- ESM `import` / `export`
- default export
- named export
- alias import/export
- mixed-source module graphs across `.js`, `.ts`, and `.qin`
- lexical scoping
- closures
- functions
- arrow functions
- classes
- inheritance in Qin-defined form
- decorators/annotation mapping

### 2.2 Common Authoring Syntax

- `const` / `let`
- object literals
- array literals
- template strings
- destructuring
- spread syntax
- default parameters
- control flow
- exception handling

### 2.3 Practical TS Surface

- type annotations
- interfaces
- type aliases
- typed class fields/methods
- ordinary generic declarations

### 2.4 Qin-Owned Runtime Surface

- `console`
- `Math`
- `JSON`
- `Array`
- `Map`
- `Set`
- `Object`
- `String`
- `Number`
- `Boolean`
- selected `Date`
- selected `Error`
- selected `RegExp`

### 2.5 Execution Model

- synchronous-by-default execution
- explicit `async`
- `Task<T>` as the Qin-facing async abstraction
- JVM concurrency as host mapping

## 3. Supportable But High-Care Features

These are supportable, but Qin should implement them as Qin-defined semantics, not as a promise of exact Node/V8 identity.

- ordinary dynamic object property read/write
- object-literal-heavy code
- broader builtin coverage
- additional TS syntax layers
- pure ESM compiler/tooling npm packages
- selected host-adapted packages such as parser/compiler infrastructure
- iterator/generator support if Qin later decides the payoff is worth the runtime complexity

Rule:

- acceptable as Qin semantics
- expensive if exact JavaScript parity is demanded

## 4. High-Cost Features To Defer

These features are not "impossible", but they are expensive enough that Qin should explicitly treat them as deferred.

### 4.1 Object-Model Fidelity

- exact prototype-chain semantics
- `delete obj.x`
- full property-descriptor semantics
- exact `this` and `arguments` edge-case behavior
- exact `undefined` / `null` / `NaN` edge-case parity
- `Symbol`-heavy object semantics

Reason:

- these pull Qin toward a much deeper JS object-runtime reimplementation

### 4.2 Coercion Fidelity

- full `==` coercion behavior
- full JavaScript historical conversion edge cases

Reason:

- difficult to make both elegant and faithful

### 4.3 Collection Fidelity

- sparse array exactness
- full Array exotic behavior

Reason:

- JavaScript arrays are not ordinary JVM lists

### 4.4 Runtime/Debug Fidelity

- full generator/iterator semantics with engine-like behavior
- source-map-grade stack trace parity with native JS tooling

Reason:

- possible, but not a first-wave language/platform payoff

## 5. Very High-Cost Features To Treat As Non-Goals For Now

These are the features most likely to push Qin into "rebuild V8/Node" territory.

- `Proxy`
- `eval`
- `new Function()`
- exact Promise/microtask semantics
- exact Node event-loop semantics
- CommonJS full compatibility
- full Node builtin compatibility (`fs`, `path`, `process`, `child_process`, etc.)
- native addon / N-API compatibility

Reason:

- these are mostly engine/host emulation problems, not Qin language-design wins

## 6. Decision Rule

When considering a new JS-facing feature, ask:

1. Does it improve Qin's language usability directly?
2. Can it be implemented cleanly as Qin-defined semantics?
3. Does it help Class-A or strategic Class-B npm packages?
4. Or does it mainly serve exact Node/V8 emulation?

If the answer is mostly the last one, it should be deferred.

## 7. Definition Of Success

This model is successful when:

1. Qin becomes stronger as an ESM-style JVM language without pretending to be a full JS engine.
2. The supported surface is large enough for real application and tooling work.
3. Deferred features are deferred intentionally, not by accident.
4. The project avoids being consumed by V8/Node reimplementation work.
