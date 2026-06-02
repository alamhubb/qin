# qin-runtime-core

Core runtime orchestration package for Qin.

Planning document:

- `QIN_APP_MODEL.md`
- `QIN_DEV_SERVER_STAGE1.md`
- `QIN_MISSION_AND_VALUE.md`
- `QIN_PRODUCT_POSITIONING.md`
- `ROADMAP.md`
- `QIN_LANGUAGE_TARGET_MODEL.md`
- `QIN_JS_COMPATIBILITY_MODEL.md`
- `QIN_CURRENT_SUPPORT_MATRIX.md`
- `QIN_NPM_COMPATIBILITY_POLICY.md`
- `QIN_HOST_CAPABILITY_MODEL.md`
- `QIN_JS_ON_JVM_FEASIBILITY.md`
- `QIN_CSSTS_INTEGRATION_MODEL.md`
- `SPRING_QIN_ARCHITECTURE.md`
- `QIN_BACKEND_MODEL.md`
- `QIN_BUILTINS_STRATEGY.md`
- `QIN_PACKAGE_MANIFEST_MODEL.md`

## Positioning

- Qin app model is defined in `QIN_APP_MODEL.md`.
- Qin Stage-1 dev server design is defined in `QIN_DEV_SERVER_STAGE1.md`.
- Qin mission/value is defined in `QIN_MISSION_AND_VALUE.md`.
- Qin product positioning is defined in `QIN_PRODUCT_POSITIONING.md`.
- Qin keeps JS/ES-style syntax.
- Parsing is based on `java-slime`, through the Qin-owned `qin-parser` layer.
- Runtime semantics are Java-oriented, not Node-compatible.
- Backend foundation is JVM + `.class` ecosystem, not Node compatibility.
- Qin accepts mixed Qin source inputs across `.ts`, `.js`, and `.qin`.
- `.java` remains host/interoperability code, not a Qin source suffix.
- `qin.config.js` is the Qin package manifest, workspace/module-root descriptor, and runtime/build config surface.
- This package is the bootstrap/orchestration layer for Qin-on-JVM runtime workflows.

Product-direction summary:

- Qin is moving toward an AI-native fullstack application language.
- Users should eventually think in application concepts first, not frontend/backend layering first.
- `shared/main/app` remains an important target-zoning model, but not the final primary product mental model.
- See `QIN_APP_MODEL.md` for the intended user-facing application layer above target zoning.
- See `QIN_MISSION_AND_VALUE.md` for why Qin is worth building beyond syntax similarity to JavaScript.
- Stage-1 frontend support uses a single-port Qin dev server plus Qin-owned Vue SFC compilation orchestration; Vite is not a runtime, bridge, fallback, or compatibility layer.
- JS/TS compatibility is defined by Qin's own supported subset, not by full engine parity; see `QIN_JS_COMPATIBILITY_MODEL.md`.
- The current verified support matrix is tracked in `QIN_CURRENT_SUPPORT_MATRIX.md`.
- npm package support is graded by compatibility class rather than treated as universal; see `QIN_NPM_COMPATIBILITY_POLICY.md`.
- Host/runtime capability boundaries are Qin-defined and target-mapped; see `QIN_HOST_CAPABILITY_MODEL.md`.
- JS-on-JVM support priorities versus deferred engine-fidelity work are documented in `QIN_JS_ON_JVM_FEASIBILITY.md`.
- Qin may align with useful dev lifecycle concepts from Vite, but the implementation is Qin-owned and does not invoke Vite.
- CSSTS integration policy is documented in `QIN_CSSTS_INTEGRATION_MODEL.md`.
- Current Stage-1 local development expects sibling `qin/` and `slime/` repositories in one workspace so `qin-parser` can resolve local `java-slime` parser sources; see `QIN_DEV_SERVER_STAGE1.md`.

Backend model summary:

- Qin language surface uses ESM-style syntax.
- Qin standard/runtime APIs are Qin-defined and Java-backed.
- Qin language/target zoning rules are defined in `QIN_LANGUAGE_TARGET_MODEL.md`.
- Heavy backend capability should integrate with normal JVM ecosystem libraries.
- Compatible npm packages are intended to become Qin compile inputs, not just installed artifacts.
- See `QIN_BACKEND_MODEL.md` for the normative backend layering.
- Builtin object strategy is documented in `QIN_BUILTINS_STRATEGY.md`.
- JS/TS surface compatibility is documented in `QIN_JS_COMPATIBILITY_MODEL.md`.
- current support status is documented in `QIN_CURRENT_SUPPORT_MATRIX.md`.
- npm package grading is documented in `QIN_NPM_COMPATIBILITY_POLICY.md`.
- host capability boundaries are documented in `QIN_HOST_CAPABILITY_MODEL.md`.

## Current Scope (Stage 0)

- Read `.ts`, `.js`, and `.qin` source inputs in one Qin workspace graph.
- Resolve local `.js` module imports.
- Parse source via Slime frontend adapter.
- Lower to Qin IR.
- Emit target artifacts:
  - JVM `.class`
  - JS `.js`

Pipeline entry classes:

- `QinRuntimeMain` (CLI entry)
- `QinDevServerMain` (canonical Stage-1 single-process dev/fullstack server entry)
- `QinFullstackMain` (single-entry fullstack serve)
- `QinBuildCoordinator` (orchestration)
- `QinSourceResolver` (source/layout resolution)
- `QinFrontendCompiler` (Qin parser -> IR)
- `QinIrValidator` + `QinJdkInteropPolicy` (policy checks)
- `QinDependencyService` (dependency boundary, current placeholder)

Pipeline layering:

- `qin-lang-module-resolver`: entry file -> module graph -> linked source
- `qin-lang-module-policy`: zone import policy checks
- `qin-lang-sema-esm`: import/export semantic model + link validation
- `qin-parser`: linked source -> Qin parse result / AST
- `qin-lang-frontend-adapter`: Qin parse result / AST -> Qin IR
- `qin-lang-lowering-jvm`: shared strict lowering gate used before target backends
- `qin-lang-backend-jvm` / `qin-lang-backend-js`: target emission

## Convention Layout

- `shared/`: shared modules and contracts.
- `app/`: frontend static assets root.
- `main/`: backend startup side (`main/main.qin` as preferred entry).

Normative target model summary:

- `shared/`: Qin portable zone, intended for both JS and JVM targets
- `main/`: Qin backend zone, JVM `.class` target
- `app/`: Qin frontend zone, JS target
- Mixed-source Qin authoring is allowed; target legality still comes from zoning.
- Java host code may coexist in backend projects, but it is outside the Qin source-suffix set.
- `qin.config.js` is the project-level manifest that binds entry, dependencies, workspace packages, and target/runtime config together.

See `QIN_LANGUAGE_TARGET_MODEL.md` for the full language/target rules.
See `QIN_APP_MODEL.md` for the intended application-first user model that sits above these zones.
See `QIN_PACKAGE_MANIFEST_MODEL.md` for the manifest/module-root model.

## Import Policy (Zone-Based)

Compile-time policy is enforced by `qin-lang-module-policy`:

- `app/` (frontend): JS imports allowed, `java:` imports denied (`QIN1001`)
- `main/` (backend): `java:` and JS imports allowed
- `shared/`: both JS and `java:` imports denied (`QIN1003`)

Smoke test entry:

- `com.qin.runtime.core.QinImportPolicyTestMain`

## ESM Runtime Smoke Test

Example files:

- `examples/esm-runtime/shared/shared.js`
- `examples/esm-runtime/main/main.js`

Runner:

- `com.qin.runtime.core.QinEsmRuntimeTestMain`

This verifies:

- local `.js` ESM import resolution
- semantic link validation
- JVM backend class generation and in-memory execution

## JS-SDK Core (Class-File API Path)

Qin now supports JS-style global built-ins without import:

```js
console.log(1);
console.log(Math.random());
console.log(JSON.stringify({ age: 1 }));
```

Current built-ins:

- `console.log`
- `Math.random/abs/floor/ceil/max/min`
- `JSON.stringify/parse`
- `Number.parseInt/parseFloat/isNaN/isFinite/isInteger/isSafeInteger`
- `Object.keys/values/entries/hasOwn`
- `Date.now`
- first-phase collection runtime behavior for:
  - `Array` (`[]`, `length`, `push`, `pop`, `map`, `forEach`, `at`, `filter`, `join`, `includes`, `indexOf`, `find`, `some`, `every`)
  - `Map` (`new Map()`, `set`, `get`, `has`, `delete`, `clear`, `size`)
  - `Set` (`new Set()`, `add`, `has`, `delete`, `clear`, `size`)
- first-phase string runtime behavior for:
  - `String.length`
  - `includes`, `startsWith`, `endsWith`
  - `trim`, `toUpperCase`, `toLowerCase`
  - `slice`, `substring`, `split`, `charAt`

Compile-time built-in diagnostics:

- `QJS1001`: unknown built-in call
- `QJS1003`: built-in argument mismatch
- `QJS2xxx`: unsupported subset syntax/semantics

## Run

```bash
java -cp "<cp>" com.qin.runtime.core.QinRuntimeMain --root . --file main/main.qin --target both --print-ir
```

If `--file` is omitted, it tries:

- `shared/main.qin`
- `shared/shared.qin`
- `main/main.qin`
- `app/main.qin`
- `shared/main.js`
- `shared/shared.js`
- `main/main.js`
- `app/main.js`

## Local Module Demo

Example entry:

`packages/qin-runtime-core/examples/modules/main/main.js`

```js
import { shared } from "../shared/shared.js";
console.log(shared.age);
```

Imported module:

```js
export const shared = { age: 21 };
```

## Java Standard Library Import Demo

Supported demo syntax:

```js
import { Math } from "java:java.lang";
console.log(Math.random());
```

Supported JVM interop demo:

```js
import { ArrayList } from "java:java.util";

const list = new ArrayList();
list.add("hello");
console.log(list.size());
```

ESM name resolution is case-sensitive.
If you want lowercase usage, use explicit alias:

```js
import { Math as math } from "java:java.lang";
console.log(math.random());
```

Runnable Java demo entry:

`com.qin.runtime.core.QinJavaImportMathDemoMain`

Additional runnable Java demo entry:

`com.qin.runtime.core.QinJavaArrayListDemoMain`

## Fullstack MVP (Single Java Entry)

Run one Java class to build and start backend + frontend on one port:

```bash
java -cp "<cp>" com.qin.runtime.core.QinFullstackMain --root examples/fullstack-mvp --port 8080
```

Stage-1 canonical dev-server entry:

```bash
java -cp "<cp>" com.qin.runtime.core.QinDevServerMain --root examples/fullstack-mvp --port 8080 --dev
```

Current alignment:

- `QinDevServerMain` is the canonical Stage-1 runtime name for Qin's single-process dev/fullstack server.
- `QinFullstackMain` remains available as the current compatibility/build host entry.
- `qin dev` should prefer `QinDevServerMain` when it is present on the runtime classpath.

Example project layout:

- `examples/fullstack-mvp/shared/shared.qin`
- `examples/fullstack-mvp/main/main.qin`
- `examples/fullstack-mvp/app/main.qin`
- `examples/fullstack-mvp/app/index.html`

Endpoints:

- `GET /api/health`
- `GET /api/result`

## Frontend ESM Mode (Qin as TS-like Source)

- Dev mode (`--dev`):
  - no frontend `.js` disk emit is required
  - `/app.js` is served as bootstrap
  - `.js` modules are transformed on request under `/@qin-mod/...`
- Build mode (`--build-only`):
  - emits browser `.js` files for `.js` module graph
  - writes bootstrap `app.js` and module outputs under `@qin-mod/`

## New TestMain Entries (Stage-1)

- `com.qin.runtime.core.EsmStage1TestMain`
- `com.qin.runtime.core.EsmSyntaxMatrixTestMain`
- `com.qin.runtime.core.EsmCycleLiveBindingTestMain`
- `com.qin.runtime.core.EsmBackendParityTestMain`
- `com.qin.runtime.core.QinRunEntryParityTestMain`
- `com.qin.runtime.core.FullstackSinglePortSmokeTestMain`

## New TestMain Entries (JS-SDK + npm bare import)

- `com.qin.runtime.core.JsSdkBuiltinSmokeTestMain`
- `com.qin.runtime.core.JsBuiltinCollectionsSmokeTestMain`
- `com.qin.runtime.core.JsBuiltinStringSmokeTestMain`
- `com.qin.runtime.core.JsSdkBuiltinCompileErrorTestMain`
- `com.qin.runtime.core.JvmClassFileBuiltinEmitTestMain`
- `com.qin.runtime.core.NpmBareImportResolverTestMain`
- `com.qin.runtime.core.QinNpmParitySmokeTestMain`
- `com.qin.runtime.core.NpmSubsetCompileTestMain`

## Verified npm/JS Baseline

The current npm/JS baseline is intentionally small but now verified by parity smoke instead of design intent only.

Verified by `QinNpmParitySmokeTestMain`:

- bare npm ESM package import
- named export / default export access
- function call across package boundary
- `for...of` over array values
- `+=` compound assignment
- `++` update expressions
- `try` / `catch`
- `new Error("...")`
- `error.message`
- template literal rendering
- JS-style numeric stringification inside template interpolation

This is not a claim of broad npm parity.
It is the current proven floor for Qin-compiled npm package execution on JVM.

