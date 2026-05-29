# Qin Current Support Matrix

This document records the current Qin-on-JVM support surface.

It is intentionally status-oriented:

- `verified` means covered by current smoke tests or direct runtime paths
- `partial` means implemented as a Qin-owned subset, not full JS/Node parity
- `not current scope` means Qin should fail clearly instead of silently emulating Node/V8

Related documents:

- `QIN_JS_COMPATIBILITY_MODEL.md`
- `QIN_NPM_COMPATIBILITY_POLICY.md`
- `QIN_HOST_CAPABILITY_MODEL.md`
- `QIN_BUILTINS_STRATEGY.md`

## 1. Source And Module Surface

### Verified / Supported

- `.js`, `.ts`, and `.qin` are intended mixed Qin source inputs.
- ESM-first imports and exports.
- Named import/export.
- Default export/import.
- Re-export and export-all smoke coverage.
- Alias import/export patterns.
- Bare npm package import for Qin-compatible packages.
- Scoped package specifiers such as `@scope/pkg`.
- Workspace module graph resolution across mixed source files.
- `import.meta.url` is rewritten to a Qin runtime shim.

### Partial / Target-Specific

- Dynamic `import(...)` is allowed only for browser/frontend module output paths today. The JVM runtime now has a module namespace/registry foundation, but JVM source compilation still rejects `import(...)` with `ESM3001` until module-level/class-cache compilation can register loadable module artifacts.
- Top-level `await` parses through a Qin shim, but it is currently synchronous passthrough, not JS Promise/microtask semantics.
- CommonJS `require` is supported only as a selected compatibility shape for package execution, not as Qin's normative module system.

### Not Current Scope

- Full Node loader parity.
- Full CommonJS module semantics.
- Native addon / N-API loading.
- Runtime code loading through `eval` or `new Function`.

## 2. JS / TS Syntax Surface

### Verified / Supported

- `const`, `let`, `var`.
- Function declarations and function expressions.
- Arrow functions, including lexical `this` smoke coverage.
- Class declarations, class fields, inheritance, `super`, and selected private/static private field paths.
- Object and array literals.
- Object and array destructuring/pattern declarations.
- Spread in arrays and objects.
- Template literals and selected tagged template behavior.
- Member access, optional member access, and nullish/optional member smoke coverage.
- Binary, logical, unary, conditional, assignment, compound assignment, and update expressions.
- `if`, `for...of`, `for...in`, selected `switch`, and `try/catch`.
- `new` expressions and constructor dispatch.
- Decorator-adjacent syntax for Qin/JVM metadata flows, including Spring-style annotation lowering.
- Type annotations, typed fields, interfaces/type aliases as practical Qin/TS surface.

### Partial

- Function runtime execution currently uses a Qin function model/interpreter path for many JS functions.
- Large function-heavy bundled npm packages stress the current function-model/classfile backend.
- Generators and exact iterator protocol semantics are not yet a first-class verified surface.
- TypeScript support is practical syntax compatibility, not a full TypeScript type-checker.

### Not Current Scope

- Full TS type-level programming.
- Exact JS `this`/`arguments` historical edge-case parity.
- Exact `==` coercion parity.
- Exact sparse-array behavior.
- Exact source-map-grade JS stack/debug parity.

## 3. JS Builtin Runtime Surface

### Verified / Supported

- `console.log`.
- `Math`: selected numeric functions such as `random`, `abs`, `floor`, `ceil`, `max`, `min`, `round`, `trunc`, `pow`, `sqrt`, `sin`, `cos`, `tan`, `log`, `exp`.
- `JSON.parse` and `JSON.stringify`, including selected replacer behavior.
- `Number`: `parseInt`, `parseFloat`, `isNaN`, `isFinite`, `isInteger`, `isSafeInteger`.
- `String`: selected member operations including `substr`, `lastIndexOf`, `valueOf`, and template/tagged-template paths.
- `Array`: selected mutation/iteration operations including `concat`, `flat`, `shift`, `unshift`, `forEach`-style callback paths, length assignment, spread, and `for...of`.
- `Object`: `keys`, `values`, `entries`, `hasOwn`, `assign`, `create`, `defineProperty`, descriptors, symbols, and selected prototype calls.
- `Map` and `Set`: Qin runtime object implementations with identity-aware key handling.
- `Date.now`.
- `RegExp`: selected literal and replace/capture paths.
- `Error`, `TypeError`, `RangeError`, and selected error-message behavior.
- `Symbol`: selected creation, registry, key lookup, and property-key support.
- `TextDecoder`.
- Typed arrays: selected `Uint8Array`/typed-array construction, proto/member, and `subarray` paths.
- `Proxy`: selected Qin runtime proxy object support.

### Partial

- Prototypes, descriptors, accessors, and inherited members are Qin-owned implementations with growing smoke coverage, not V8 identity.
- `Proxy` exists for selected object get/set/method scenarios, but it is not a promise of full ECMAScript Proxy trap parity.
- `Promise` is not Qin's normative async abstraction; Qin's design target is explicit `Task<T>`.
- `undefined` is currently mapped through Qin runtime conventions and is not a full V8-value identity claim.

### Not Current Scope

- Full ECMAScript object-model edge-case parity.
- Full property descriptor / Reflect surface.
- Full Symbol-heavy metaprogramming parity.
- Exact Promise/microtask/event-loop behavior.
- `eval`.
- `new Function`.

## 4. Node-Style Host Surface

Node support is a selected host adapter layer, not the Qin platform definition.

### Verified / Supported Subset

- `fs` / `node:fs` namespace marker.
- `fs.existsSync`.
- `fs.writeFileSync`.
- `fs.appendFileSync`.
- `fs.mkdirSync`, including selected `{ recursive: true }`.
- `fs.createWriteStream` with `write`, `end`, and no-op `on`.
- `path` / `node:path`.
- `path.dirname`.
- `path.join`.
- `path.resolve`.
- `url` / `node:url`.
- `url.fileURLToPath`.
- `util` / `node:util`.
- `util.deprecate`.
- `process` / `node:process`.
- `process.cwd`.
- `diagnostics_channel` / `node:diagnostics_channel`: selected `channel`, `tracingChannel`, publish/subscribe no-op shape, `traceSync`, and `tracePromise`.

### Partial

- These APIs are Java-backed compatibility adapters for npm package execution.
- They are synchronous and minimal by design.
- Stream/event behavior is intentionally not Node-equivalent yet.
- `process` is a narrow namespace, not full Node process state.

### Not Current Scope

- `child_process`.
- `worker_threads`.
- `cluster`.
- Full `fs` async API.
- Full Node stream/event behavior.
- Full `process` parity.
- Full package loader / resolution parity.
- Native addons / N-API.

## 5. Current Large npm Package Boundary

Small Qin-compatible npm packages can run through the current pipeline.

Large bundled compiler packages expose the current architecture boundary:

- `@vue/compiler-sfc` can be resolved and parsed.
- The current linked-source/single-class JVM emission path is too heavy for it.
- The blocker is not currently an unsupported syntax such as `Proxy` or `eval`.
- The blocker is package compiler architecture: module-level compilation, class caching, and function-body emission need to replace giant linked class emission.
- The dynamic import runtime registry is the intended loading boundary for those future compiled module artifacts.
- Large interpreted function models now have a runtime registry boundary as well. The remaining compiler work is to emit
  function-model artifacts and register them instead of embedding every large AST model into one generated class.

## 6. Long-Term Direction

The long-term Qin-compatible npm path should be:

- fail fast on unsupported language or host capabilities
- keep Qin ESM as the normative module model
- grow host adapters only when they are explicit and documented
- compile npm packages module-by-module or file-by-file
- cache generated classes per package/module
- compile function bodies to JVM methods/classes where practical
- keep function-model interpretation as a compatibility tool, not the only large-package execution path
