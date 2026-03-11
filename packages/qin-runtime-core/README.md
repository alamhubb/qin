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

- Read a `.qin` source file.
- Resolve local `.qin` module imports.
- Parse source via Slime frontend adapter.
- Lower to Qin IR.
- Emit target artifacts:
  - JVM `.class`
  - JS `.js`

Pipeline entry classes:

- `QinRuntimeMain` (CLI entry)
- `QinBuildCoordinator` (orchestration)
- `QinSourceResolver` (source/layout resolution)
- `QinFrontendCompiler` (Slime -> IR)
- `QinIrValidator` + `QinJdkInteropPolicy` (policy checks)
- `QinDependencyService` (dependency boundary, current placeholder)

## Convention Layout

- `shared/`: shared modules and contracts.
- `app/`: frontend static assets root.
- `main/`: backend startup side (`main/Main.java` as preferred entry).

## Run

```bash
java -cp "<cp>" com.qin.runtime.core.QinRuntimeMain --root . --file shared/main.qin --target both --print-ir
```

If `--file` is omitted, it tries:

- `shared/main.qin`
- `shared/shared.qin`
- `main/main.qin`
- `app/main.qin`

## Local Module Demo

Example entry:

`packages/qin-runtime-core/examples/modules/main/main.qin`

```js
import { shared } from "../shared/shared.qin";
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
