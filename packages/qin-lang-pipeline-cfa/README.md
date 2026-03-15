# qin-lang-pipeline-cfa

`qin-lang-pipeline-cfa` provides one orchestration entry for Qin JVM compilation:

1. `QinCfaSemanticStage`: ESM module link + semantic checks
2. `QinCfaIrStage`: Slime frontend AST -> Qin language IR -> lowered IR -> CFA IR
3. `QinCfaEmitStage`: CFA IR -> Class-File bytecode

Current bridge state:

- CFA IR is now a dedicated model (`QinCfaProgram`).
- The emitter now consumes `QinCfaProgram` directly and emits Class-File bytecode through
  `QinCfaJvmClassFileBackend`.
- `QinCfaIrToQinIrAdapter` remains in the package only as a transition utility and is no longer on
  the runtime hot path.

This package is orchestration-first. Concrete frontend, sema, lowering, and backend logic stays in their dedicated packages.
