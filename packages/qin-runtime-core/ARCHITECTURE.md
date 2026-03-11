# qin-runtime-core Architecture (Draft v0.1)

This package is the runtime orchestration layer for Qin:

- JS/ES-like syntax
- Java runtime semantics
- JVM `.class` as primary backend target

## 1. Module Boundaries

### Frontend (parser + lowering)

- Source parser: `java-slime` (shared frontend infrastructure)
- Adapter/lowering: `qin-lang-frontend-adapter`
- Output: `qin-lang-ir`

Responsibility:

- Parse `.qin` source
- Enforce language surface rules (ES subset)
- Build IR nodes

### IR (semantic middle layer)

- Package: `qin-lang-ir`

Responsibility:

- Stable, backend-neutral semantic model
- No runtime side effects

### Backend targets

- JVM backend: `qin-lang-backend-jvm`
- JS backend: `qin-lang-backend-js`

Responsibility:

- Convert IR to target artifacts
- JVM backend handles Java interop bytecode generation
- JS backend handles web-side output

### Runtime API layer

- Package: `qin-lang-runtime`

Responsibility:

- Runtime built-ins exposed to Qin programs
- Console/logging and future standard library bridge

### Orchestration layer (this package)

- Package: `qin-runtime-core`
- Entry: `QinRuntimeMain`

Responsibility:

- Project layout detection (`shared/app/main`)
- Input source resolution (`.qin` file)
- Pipeline orchestration (frontend -> IR -> backend targets)
- Build output routing

## 2. Java Interop Rule (Current)

Syntax:

```js
import { Math } from "java:java.lang";
console.log(Math.random());
```

Rules:

- ESM-like syntax, case-sensitive
- No implicit case conversion
- `import { Math as math } ...` is allowed
- Current MVP supports zero-arg static method call in `console.log(...)`

## 3. Recommended Next Split

`QinRuntimeMain` is now split by responsibilities:

1. `QinSourceResolver`
2. `QinFrontendCompiler`
3. `QinIrValidator` + `QinJdkInteropPolicy`
4. `QinBuildCoordinator`
5. `QinDependencyService` (placeholder boundary)

`QinRuntimeMain` only parses CLI options and calls `QinBuildCoordinator`.

## 4. Dependency / JDK Loading Strategy

### Dependency layer

- Reuse Qin workspace/dependency resolver from root CLI (`WorkspaceLoader`, `LocalProjectResolverEnhanced`, `DependencyResolver`)
- Keep resolver logic independent from parser/backend.

### JDK symbol loading

- Interop resolver (future module) should resolve classes/methods in one place:
  - class existence
  - static/instance checks
  - overload resolution
  - primitive boxing strategy

This keeps backend code generation thin and testable.
