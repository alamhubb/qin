# Qin npm Compatibility Policy

This document defines how Qin should classify npm package compatibility.

It answers one core question directly:

- If Qin can install or compile npm packages, which kinds of packages are actually within Qin's intended support boundary?

The answer is:

- npm is a package source for Qin, not the definition of Qin's runtime.
- npm compatibility must be graded by source/runtime shape, not treated as universal.

Related documents:

- `QIN_JS_COMPATIBILITY_MODEL.md`
- `QIN_HOST_CAPABILITY_MODEL.md`
- `QIN_BACKEND_MODEL.md`
- `QIN_CURRENT_SUPPORT_MATRIX.md`

## 1. Core Position

Qin should aim to compile and run compatible npm packages where they fit Qin's language/runtime rules.

This means:

- install success does not imply runtime support
- ESM compatibility matters more than registry origin
- host/runtime dependencies decide practical support

So the correct product statement is:

- Qin supports Qin-compatible npm packages
- Qin does not promise blind npm ecosystem parity
- Qin should support as many simple pure ESM npm packages as practical, filtered by target zone
- Qin must not weaken the JVM `.class` subset merely to accept packages that depend on hard dynamic JS semantics

Target-zone clarification:

- `app/` may use frontend npm packages where Qin's frontend pipeline can serve or emit them as JS, but this does not broaden Qin core syntax.
- `main/` may use npm package code only where it compiles cleanly to JVM `.class` or is mediated by explicit Qin host adapters.
- `shared/` MVP should not directly depend on npm packages; reusable cross-target package behavior should be exposed through portable Qin stdlib wrappers or generated `.qin` code.
- Node/browser dual packages often use conditional `exports` to choose different files for different hosts. Qin can borrow that resolver idea, but package conditions are not proof that ordinary `.js` or `.ts` belongs in `shared/`.
- For Qin, the useful Node lesson is "choose the right target entry"; the non-goal is "make any JS/TS file shared-safe because a package can publish both Node and browser builds."

## 2. Compatibility Classes

### Class A: Qin-Compatible By Design

These packages should be the first priority.

Typical properties:

- pure ESM source
- no CommonJS dependency
- no Node builtin dependency
- no DOM dependency
- no native addon dependency
- no reliance on Promise/microtask edge-case semantics
- mostly syntax, AST, text, data, or compile-time logic
- cleanly lowerable to Qin IR and, for backend/shared use, predictable JVM `.class`

Examples of package categories:

- parsers
- lexers
- compiler helpers
- markdown/html/css processors
- config readers
- small utility libraries

These are the best first-wave npm targets for Qin.

### Class B: Host-Adaptable Packages

These packages are not pure-core compatible, but can become Qin-compatible through explicit host adapters.

Typical properties:

- ESM-first or mostly-portable source
- depends on selected Node-like utilities such as path/file/url behavior
- may need filesystem or process-facing abstractions
- may use async patterns that can be mapped into Qin/host behavior

Examples of package categories:

- selected build-tool internals
- selected file-based compiler packages
- selected transform pipelines

Policy:

- support is allowed
- but it should be driven by explicit Qin host adapters
- not by redefining Qin as Node
- adapters must declare whether they are frontend-only, JVM-only, or dual-target-safe

### Class C: Not In Scope For Stage 1

These packages should be rejected or deferred unless Qin's platform definition changes significantly.

Typical properties:

- depends on CommonJS loader behavior
- depends heavily on Node runtime internals
- depends on browser DOM runtime
- depends on native addons
- depends on exact Promise/event-loop behavior
- depends on `Proxy`, `eval`, runtime code generation, or aggressive prototype mutation

Examples of package categories:

- deep Node runtime tooling
- DOM-native UI runtimes on JVM backend
- N-API or `.node` addons
- packages whose correctness assumes full V8/Node semantics
- packages that depend heavily on prototype tricks, `Proxy`, `eval`, or event-loop fidelity
- packages whose useful semantics cannot be compiled to JVM `.class` without an ugly dynamic emulation layer

## 3. Package Intake Rule

When a new npm package is evaluated, the decision order should be:

1. Is it ESM-first?
2. Does it stay inside Qin-supported JS/TS syntax?
3. Does it require Node host APIs?
4. If yes, are those APIs inside Qin's host-capability roadmap?
5. If no, can the package still run as a Class A or Class B package?
6. Which zone wants the package: `app/`, `main/`, or `shared/`?
7. For `main/` or `shared/`, does it compile simply and predictably to JVM `.class`?
8. For `shared/`, can the dependency be represented as portable `.qin` or an approved Qin stdlib wrapper instead of a direct npm import?
9. If the package offers conditional Node/browser entries, is Qin selecting an entry that satisfies the active zone instead of assuming every condition is portable?

This keeps package support intentional.

Do not reintroduce legacy handwritten TypeScript parser packages to make npm
compatibility look better. Parser authority remains the Java Slime/Qin sources
compiled to TypeScript packages, with legacy TS parser references isolated to
explicit legacy tests, migration comparisons, or old demos.

## 4. What Qin Should Prioritize First

The recommended priority order is:

1. Class A packages
2. small Class B packages with clear host adapters
3. strategically important packages such as compiler infrastructure
4. only much later, deeper Node-bound packages

In practice, this means Qin should prioritize packages like:

- `@vue/compiler-sfc`-adjacent compiler flow
- parser/compiler libraries
- syntax transforms
- CSS/template toolchain pieces

before trying to support:

- full dev-server ecosystems
- Node process/runtime tooling
- packages that assume a native JS engine host

## 4.1 Current Verified Runtime Floor

Qin now has a small but real parity-verified npm execution floor on JVM.

Verified by `com.qin.runtime.core.QinNpmParitySmokeTestMain`:

- bare-package ESM import
- named/default export use
- local function execution from imported npm packages
- array iteration with `for...of`
- compound assignment such as `+=`
- update expressions such as `++`
- `try` / `catch` with `Error.message`
- template literals with JS-style numeric string rendering

This section is intentionally narrow.
It records what is already proven, not what Qin may support later.

## 4.2 Current Large-Package Compiler Boundary

The current npm execution pipeline can parse and lower small compatible packages, but large bundled packages expose an
important architecture boundary.

Observed with `@vue/compiler-sfc`:

- the Qin module runner can resolve the package and produce a linked source
- `compiler-sfc.esm-browser.js` is about 1.7 MB
- the parser can now parse that source, but it is slow
- the CFA/JVM backend currently attempts to emit the linked package as one generated JVM class
- Vue's bundled compiler produces more than one thousand top-level declarations and very large runtime function models

This means the current linked-source/single-class path is a Stage-1 bottleneck, not the final npm package architecture.

The long-term package compiler must move toward:

- module-level or file-level compilation
- generated class caching per npm package/module
- explicit package manifests for compiled exports
- function bodies compiled to JVM methods/classes rather than encoded as huge object literals whenever possible
- external function-model artifacts for functions that still use the interpreter path
- bounded literal/function-model emission so compiler optimizations cannot exhaust heap

Until that architecture lands, large compiler packages such as `@vue/compiler-sfc` may fail or take too long even when
they do not use an explicitly unsupported JS feature.

Implementation note:

- `QinRuntimeModuleRegistry` is the runtime boundary for future dynamically loadable module classes
- `QinFunctionModelRegistry` is the runtime boundary for future externalized function-model artifacts
- the remaining compiler task is to emit/register those artifacts from the package compiler instead of embedding every
  model into the generated entry class

## 5. Failure Policy

When a package is outside Qin's current compatibility boundary, failure should be explicit and explainable.

Good failure reasons include:

- CommonJS not supported
- Node builtin not supported
- unsupported dynamic runtime feature
- unsupported async/runtime expectation
- unsupported host capability in current target zone

Bad failure modes are:

- silent miscompilation
- accidental partial execution with undocumented semantics

## 6. Definition Of Success

This policy is successful when:

1. Qin can reuse a meaningful subset of npm without claiming universal compatibility.
2. Package support decisions are predictable and documentable.
3. Node pressure does not redefine Qin's product identity.
4. Compiler/tooling package support grows in a staged, high-signal order.
