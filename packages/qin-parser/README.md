# qin-parser

Qin language parser frontend package.

## Position

`qin-parser` is the Qin-owned parser boundary.

It exists to keep these concerns separate:

- `java-slime`: shared JS/TS parser infrastructure
- `qin-parser`: Qin language syntax entry, Qin syntax extensions, Qin parse diagnostics
- `qin-lang-frontend-adapter`: Qin AST / normalized frontend model -> Qin IR

## Current Direction

The intended parser inheritance direction is:

- `SubhutiParser`
- `SlimeJavascriptParser`
- `SlimeParser`
- `QinParser`

Where:

- `SlimeJavascriptParser` provides JS/ESM grammar entry
- `SlimeParser` provides TypeScript-oriented grammar extensions
- `QinParser` provides Qin-oriented grammar extensions

## Why This Package Exists

Before this package, parser-facing logic had started to accumulate inside
`qin-lang-frontend-adapter`, including:

- parser input preprocessing
- source rewrites/shims
- import extraction fallback
- direct Slime parser entry wiring

That made the adapter layer responsible for too many frontend concerns.

This package is the architectural correction:

- parser concerns move here
- lowering concerns stay in `qin-lang-frontend-adapter`

## Planned Responsibilities

`qin-parser` should own:

- `.qin` parsing entrypoints
- Stage 3 frontend `.java` parser integration boundaries once Java8 parsing is promoted from `slime-java`
- Qin parser configuration and parse options
- Qin-specific syntax extensions
- Qin parser diagnostics
- normalized parse results / Qin AST boundary
- migration of parser preprocess/rewrite logic out of the adapter
- import extraction fallback for parser-unfriendly top-level import forms during migration

`qin-parser` should not own:

- Qin IR lowering
- backend-specific logic
- JVM bytecode emission

## Migration Plan

Stage 1:

- introduce `qin-parser` package
- define `QinParser` and `QinParserFacade` entry boundaries
- keep implementation backed by `java-slime`

Stage 2:

- move parse/preprocess logic from `QinSlimeFrontendAdapter` into `qin-parser`
- keep adapter focused on AST -> IR

Stage 3:

- add Qin-specific syntax such as:
  - `async expr`
  - `async { ... }`
  - future Qin-only declarations

Stage 4:

- introduce a Qin-owned AST / normalized parse model if needed
- reduce direct dependence of downstream layers on raw Slime node shapes

Stage 5:

- integrate Qin/Slime Java8 `JavaParser` for frontend `.java`
- keep Java parsing in the Subhuti parser family rather than using ad hoc string lowering
- lower Java CST -> Java AST -> Java semantic model -> Qin IR before JS emission
