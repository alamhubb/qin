# qin-runtime-core

Core runtime orchestration package for Qin.

Planning document:

- `ROADMAP.md`

## Positioning

- Qin keeps JS/ES-style syntax.
- Parsing is based on `java-slime` (through `qin-lang-frontend-adapter`).
- Runtime semantics are Java-oriented, not Node-compatible.
- This package is the bootstrap layer for future "Java-based Bun/Deno-like" workflow.

## Current Scope (Stage 0)

- Read a `.js` source file.
- Resolve local `.js` module imports.
- Parse source via Slime frontend adapter.
- Lower to Qin IR.
- Emit target artifacts:
  - JVM `.class`
  - JS `.js`

Pipeline entry classes:

- `QinRuntimeMain` (CLI entry)
- `QinFullstackMain` (single-entry fullstack serve)
- `QinBuildCoordinator` (orchestration)
- `QinSourceResolver` (source/layout resolution)
- `QinFrontendCompiler` (Slime -> IR)
- `QinIrValidator` + `QinJdkInteropPolicy` (policy checks)
- `QinDependencyService` (dependency boundary, current placeholder)

Pipeline layering:

- `qin-lang-module-resolver`: entry file -> module graph -> linked source
- `qin-lang-module-policy`: zone import policy checks
- `qin-lang-sema-esm`: import/export semantic model + link validation
- `qin-lang-frontend-adapter`: linked source -> Qin IR
- `qin-lang-lowering-jvm`: shared strict lowering gate used before target backends
- `qin-lang-backend-jvm` / `qin-lang-backend-js`: target emission

## Convention Layout

- `shared/`: shared modules and contracts.
- `app/`: frontend static assets root.
- `main/`: backend startup side (`main/Main.java` as preferred entry).

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

## Run

```bash
java -cp "<cp>" com.qin.runtime.core.QinRuntimeMain --root . --file shared/main.js --target both --print-ir
```

If `--file` is omitted, it tries:

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

Example project layout:

- `examples/fullstack-mvp/shared/shared.js`
- `examples/fullstack-mvp/main/main.js`
- `examples/fullstack-mvp/app/main.js`
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
