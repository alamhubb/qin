# qin-lang-lowering-jvm

JVM-specific lowering stage for Qin language pipeline.

## Responsibility

- Convert frontend IR + ESM semantic model into JVM backend-ready IR.
- Keep target-specific transformations out of frontend packages.

## Current State

- Provides no-op lowerer skeleton (`QinNoOpEsmJvmLowerer`).
- Runtime pipeline can call this stage without changing behavior.
