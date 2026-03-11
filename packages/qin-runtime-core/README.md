# qin-runtime-core

Core runtime orchestration package for Qin.

## Positioning

- Qin keeps JS/ES-style syntax.
- Parsing is based on `java-slime` (through `qin-lang-frontend-adapter`).
- Runtime semantics are Java-oriented, not Node-compatible.
- This package is the bootstrap layer for future "Java-based Bun/Deno-like" workflow.

## Current Scope (Stage 0)

- Read a `.qin` source file.
- Parse source via Slime frontend adapter.
- Lower to Qin IR.
- Emit target artifacts:
  - JVM `.class`
  - JS `.js`

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

## Java Standard Library Import Demo

Supported demo syntax:

```js
import { Math } from "java:java.lang";
console.log(Math.random());
```

ESM name resolution is case-sensitive.
If you want lowercase usage, use explicit alias:

```js
import { Math as math } from "java:java.lang";
console.log(math.random());
```

Runnable Java demo entry:

`com.qin.runtime.core.QinJavaImportMathDemoMain`
