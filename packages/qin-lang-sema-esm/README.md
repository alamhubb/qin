# qin-lang-sema-esm

ESM semantic analysis layer for Qin.

## Responsibility

- Build import/export binding model from resolved module graph.
- Validate ESM linkage constraints used in Qin MVP and stage-1 expansion.
- Produce structured diagnostics for compile-time failures.

## Current Checks

- Import kinds: `named/default/namespace/side-effect`.
- Export kinds: `local/export default/re-export named/re-export */re-export * as`.
- Duplicate named exports (`ESM2001`).
- Local import target linking (`ESM2002`, `ESM2003`).
- Ambiguous star re-export resolution (`ESM2004`).
- Runtime feature guards with stable codes:
  - `ESM3004` for unsupported `export let/var/function/class`
  - `ESM3005` for unsupported `export default function/class`
