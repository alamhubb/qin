# qin-lang-frontend-adapter

Adapter layer from `slime` frontend AST to Qin language IR.

## Scope

- Input: `slime-ast`
- Output: `qin-lang-ir`
- Responsibility: syntax-tree normalization and Qin semantic lowering

## Current Behavior

- Always use Slime Java parser (`CST -> AST -> Qin IR`).
- Slime runtime classes must be present on classpath at compile/run time.
