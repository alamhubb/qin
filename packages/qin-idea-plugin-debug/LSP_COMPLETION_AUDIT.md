# Qin / OVS / CSSTS LSP Completion Audit

This file tracks the current evidence for the unified Qin / OVS / CSSTS
language-development target. It is an audit map, not a completion claim.

## Target

Qin, OVS, and CSSTS should share one language architecture:

- Java SlimeParser / QinParser remains the authoritative grammar source.
- Java parser output is generated into TypeScript for the Volar LSP runtime.
- Qin, OVS, and CSSTS language servers use the generated parser chain and expose
  diagnostics, completion, definition, document symbols, and semantic tokens.
- IDEA only registers file types and starts LSP servers. It must not implement a
  local syntax parser, lexer, completion engine, or fallback language service.
- Qin-related projects outside the IDEA client are managed through
  `qin.config.js`.
- Qin runtime execution targets JVM `.class`; Node/TypeScript exists only for
  editor/LSP and language-tooling processes.
- Parser, compiler, transform, and LSP failures stay visible. No identity source
  fallback, regex parser, string syntax shim, swallowed error, or degraded broad
  success path is accepted.

## Current Evidence

Run the full gate from this directory:

```powershell
.\gradlew.bat check
```

The check task currently covers:

- `qinLanguageTest`
- `ovsLanguageTest`
- `csstsLanguageTest`
- `qinGeneratedParserDryRun`
- `lspRegistrySmoke`
- `lspServerCommandLineSmoke`
- `lspServerDiagnosticsSmoke`
- `lspVerificationMatrixSmoke`
- `lspPluginDescriptorSmoke`
- `lspNoLocalParserSmoke`
- `lspWorkspaceInventorySmoke`
- `lspPluginPackageSmoke`
- `lspUiFixtureSmoke`
- `qinJvmClassTargetSmoke`
- `qinJvmClassDeclarationSmoke`

## Requirement Audit

| Requirement | Current evidence | Status |
| --- | --- | --- |
| Java parser is authoritative for Qin syntax | `qinGeneratedParserDryRun` verifies `com.qin.parser.QinParser` metadata, generated output package, and `generatedParserTarget`; `QinParserObjectDeclarationSmokeTestMain` verifies `object` parses through PEG CST and is not source-string rewritten; `test-generated-parser-parity.ts` compares Java QinParser and generated TypeScript QinParser acceptance on a small Qin corpus. | Proven for current Qin parser parity corpus |
| Qin generated TypeScript parser is usable by Volar | `qin/packages/qin-language/qin.config.js` points `language.parser` at `generated/qin-parser-ts`; `test-language-plugin.ts` verifies generated Qin parser availability and parser diagnostics; `test-generated-parser-parity.ts` verifies generated parser behavior matches Java parser acceptance on the current corpus. | Proven for current `.qin` LSP scope |
| OVS inherits the shared generated parser chain | `ovs-language/tests/test-generated-parser-chain.ts` checks `OvsParser extends CssTsParser`, `instanceof CssTsParser`, `instanceof SlimeJavascriptParser`, and parser-chain parsing. | Proven for current OVS parser-chain smoke |
| CSSTS inherits the shared generated parser chain | `cssts-language/tests/test-generated-parser-chain.ts` checks `CssTsParser extends SlimeParser`, `instanceof SlimeJavascriptParser`, and parser-chain parsing. | Proven for current CSSTS parser-chain smoke |
| Volar LSP provides diagnostics | `lspServerDiagnosticsSmoke`, `qinLanguageTest`, `ovsLanguageTest`, and `csstsLanguageTest` verify invalid `.qin`, `.ovs`, and `.cssts` diagnostics. | Proven |
| Volar LSP provides completion, definition, symbols, and semantic tokens | `lspServerDiagnosticsSmoke` requests these features for all three languages; language project tests also cover the same feature family. | Proven for current smoke fixtures |
| IDEA is only an LSP client for these languages | `plugin.xml` registers `.qin`, `.ovs`, `.cssts` file types and one `platform.lsp.serverSupportProvider`; `QinLspNoLocalParserSmokeTestMain` scans plugin sources for local parser, lexer, syntax highlighter, and completion contributor markers; `QinLspWorkspaceInventorySmokeTestMain` rejects legacy `*-intellij-client` projects under OVS/CSSTS language packages; `LSP.md` records the no-local-parser policy. | Proven for current IDEA plugin sources and language-project inventory |
| IDEA plugin packaging and fixture coverage stay in the gate | `lspVerificationMatrixSmoke` verifies `check` depends on descriptor, package, and UI fixture smokes. | Proven |
| Qin-managed project metadata is used | `lspVerificationMatrixSmoke` loads each language project's `qin.config.js` and verifies `language.parser`, `language.compiler`, `language.serverBundle`, scripts, and dependencies; `lspWorkspaceInventorySmoke` verifies the current LSP-critical language, compiler, parser, CLI, and runtime projects are all owned by `qin.config.js`. | Proven for the current LSP-critical inventory |
| Qin runtime target remains JVM `.class` | `qinJvmClassTargetSmoke` runs `qin run com.qin.lang.cli.SmokeTestMain`; the smoke compiles and reflects a 5-case `.class` corpus covering top-level function calls, `export const`, object member access, array literals, and `java:` import plus Java instance calls. `qinJvmClassDeclarationSmoke` runs `QinJvmClassDeclarationCorpusSmokeTestMain`, a 10-case class-declaration corpus covering Java `extends`, field annotations, fields, constructors, local member access, `this.field.method()`, runtime function methods, Java static calls, Java constructor returns, direct Java SlimeParser inheritance from parsed Qin source, and parsed Qin method-body execution for binary and conditional returns. | Proven for current CLI and class-declaration corpus |
| Node/TypeScript is limited to editor/LSP/tooling | `LSP.md` states the boundary; `qinJvmClassTargetSmoke` separately verifies the runtime path. | Partly proven |
| No fallback success path | Language plugin tests assert invalid input does not fall back to identity source text; transform failures generate visible `Qin/OVS/CSSTS transform failed` code. | Proven for current language-server transform paths |

## Known Gaps

These items are not proven complete yet:

- Full grammar authority across every Slime/Qin/OVS/CSSTS syntax branch is not
  exhaustively audited. Current evidence covers the active generated-parser path,
  a small Java/TypeScript parser parity corpus, Qin `object`, and parser-chain
  inheritance smoke fixtures.
- IDEA has smoke coverage for LSP startup, capabilities, diagnostics, packaging,
  and fixture registration, but manual IDE behavior still relies on
  `runIdeLspFixture` for visual confirmation.
- The `qin.config.js` management guarantee is verified for the current
  LSP-critical inventory, not every experimental demo or unrelated adjacent
  repository in the whole workspace. Legacy language-local IDEA clients are
  explicitly rejected for the OVS/CSSTS language package roots.
- JVM `.class` execution is proven by the current focused CLI corpus and
  class-declaration corpus, including direct parsed-source class/method
  execution, not by every Qin fullstack example,
  production-style application path, or every class declaration feature variant.
- The Node/TypeScript boundary is documented and tested indirectly. A future
  smoke could assert that runtime-oriented Qin checks do not call language-server
  Node commands.

## Next Hardening Steps

1. Expand the unified class-declaration `.class` gate toward broader parsed Qin
   source coverage for local class inheritance and richer method bodies.
2. Keep expanding generated parser parity from the current small corpus toward
   broader Slime/Qin syntax coverage.
3. Keep this audit updated whenever a requirement moves from "partly proven" to
   "proven" or when a new gap is found.
