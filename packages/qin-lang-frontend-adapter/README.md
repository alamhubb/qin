# qin-lang-frontend-adapter

Adapter layer from Qin frontend AST/normalized parse model to Qin language IR.

## Scope

- Input: Qin parser output (currently still Slime-backed AST)
- Output: `qin-lang-ir`
- Responsibility: syntax-tree normalization and Qin semantic lowering

## Current Behavior

- Parsing ownership is moving into `qin-parser`.
- This package should progressively narrow toward lowering only.
- Slime runtime classes are still present underneath through `qin-parser` in the current migration stage.
- `QinFrontendLowerer` is the preferred new entry for source -> Qin IR lowering.
- `QinFrontendLowerer` now goes directly through `QinParserFacade` and then delegates into `QinIrLowerer`.
- `QinIrLowerer` is the Qin-owned `parsed frontend output -> Qin IR` boundary.
- `QinTopLevelIrAssembler` now owns the `Program -> QinIrProgram` top-level dispatch/assembly path.
- `QinDeclarationIrLowerer` now owns the declaration-lowering boundary and is progressively taking over declaration/class/function/field lowering from legacy adapter code.
- `QinRuntimeIrLowerer` now owns the typed-AST runtime statement dispatch boundary, while still reusing legacy adapter helper bodies for deeper runtime-expression semantics during migration.
- `QinJavaAstIrLowerer` owns the Stage 3 Java8 AST -> Qin IR bridge for the Subhuti Java parser path. The current slice lowers package/class/field/method/parameter structure plus `return a + b` style binary return expressions into `Global.__qin_binary__`.
- `QinLegacySlimeIrLowerer` is an internal migration host for the existing Slime-based lowering body.
- `QinSlimeFrontendAdapter` remains as a compatibility entrypoint during migration.
- `QinSlimeFrontendAdapter` no longer owns the live top-level lowering path; it delegates to Qin-owned parser/lowerer entrypoints and keeps legacy helper bodies for migration.
- Top-level runtime statement dispatch now passes through `QinRuntimeIrLowerer`.
- Deeper runtime-expression lowering still temporarily reuses legacy adapter helper bodies.
