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
- `lspPluginPackageSmoke`
- `lspUiFixtureSmoke`
- `qinJvmClassTargetSmoke`

## Requirement Audit

| Requirement | Current evidence | Status |
| --- | --- | --- |
| Java parser is authoritative for Qin syntax | `qinGeneratedParserDryRun` verifies `com.qin.parser.QinParser` metadata, generated output package, and `generatedParserTarget`; `QinParserObjectDeclarationSmokeTestMain` verifies `object` parses through PEG CST and is not source-string rewritten. | Partly proven |
| Qin generated TypeScript parser is usable by Volar | `qin/packages/qin-language/qin.config.js` points `language.parser` at `generated/qin-parser-ts`; `test-language-plugin.ts` verifies generated Qin parser availability and parser diagnostics. | Proven for current `.qin` LSP scope |
| OVS inherits the shared generated parser chain | `ovs-language/tests/test-generated-parser-chain.ts` checks `OvsParser extends CssTsParser`, `instanceof CssTsParser`, `instanceof SlimeJavascriptParser`, and parser-chain parsing. | Proven for current OVS parser-chain smoke |
| CSSTS inherits the shared generated parser chain | `cssts-language/tests/test-generated-parser-chain.ts` checks `CssTsParser extends SlimeParser`, `instanceof SlimeJavascriptParser`, and parser-chain parsing. | Proven for current CSSTS parser-chain smoke |
| Volar LSP provides diagnostics | `lspServerDiagnosticsSmoke`, `qinLanguageTest`, `ovsLanguageTest`, and `csstsLanguageTest` verify invalid `.qin`, `.ovs`, and `.cssts` diagnostics. | Proven |
| Volar LSP provides completion, definition, symbols, and semantic tokens | `lspServerDiagnosticsSmoke` requests these features for all three languages; language project tests also cover the same feature family. | Proven for current smoke fixtures |
| IDEA is only an LSP client for these languages | `plugin.xml` registers `.qin`, `.ovs`, `.cssts` file types and one `platform.lsp.serverSupportProvider`; `QinLspNoLocalParserSmokeTestMain` scans plugin sources for local parser, lexer, syntax highlighter, and completion contributor markers; `LSP.md` records the no-local-parser policy. | Proven for current IDEA plugin sources |
| IDEA plugin packaging and fixture coverage stay in the gate | `lspVerificationMatrixSmoke` verifies `check` depends on descriptor, package, and UI fixture smokes. | Proven |
| Qin-managed project metadata is used | `lspVerificationMatrixSmoke` loads each language project's `qin.config.js` and verifies `language.parser`, `language.compiler`, `language.serverBundle`, scripts, and dependencies. | Proven for Qin/OVS/CSSTS language projects and OVS/CSSTS compiler projects |
| Qin runtime target remains JVM `.class` | `qinJvmClassTargetSmoke` runs `qin run com.qin.lang.cli.SmokeTestMain` and verifies generated `.class` output. | Proven for current smoke |
| Node/TypeScript is limited to editor/LSP/tooling | `LSP.md` states the boundary; `qinJvmClassTargetSmoke` separately verifies the runtime path. | Partly proven |
| No fallback success path | Language plugin tests assert invalid input does not fall back to identity source text; transform failures generate visible `Qin/OVS/CSSTS transform failed` code. | Proven for current language-server transform paths |

## Known Gaps

These items are not proven complete yet:

- Full grammar authority across every Slime/Qin/OVS/CSSTS syntax branch is not
  exhaustively audited. Current evidence covers the active generated-parser path,
  Qin `object`, and parser-chain inheritance smoke fixtures.
- IDEA has smoke coverage for LSP startup, capabilities, diagnostics, packaging,
  and fixture registration, but manual IDE behavior still relies on
  `runIdeLspFixture` for visual confirmation.
- The `qin.config.js` management guarantee is verified for the language and
  compiler projects in the LSP matrix, not every Qin-adjacent repository in the
  whole workspace.
- JVM `.class` execution is proven by a focused CLI smoke, not by every Qin
  fullstack example or production-style application path.
- The Node/TypeScript boundary is documented and tested indirectly. A future
  smoke could assert that runtime-oriented Qin checks do not call language-server
  Node commands.

## Next Hardening Steps

1. Add a workspace-wide Qin project inventory smoke that lists every relevant
   non-IDEA language/compiler/runtime project and verifies `qin.config.js`
   ownership.
2. Expand generated parser parity checks beyond metadata: run a small corpus
   through Java QinParser and generated TypeScript QinParser and compare success
   or failure classes.
3. Expand `.class` target smoke from one CLI sample to a small Qin backend corpus
   that includes imports, exports, classes, functions, and `object`.
4. Keep this audit updated whenever a requirement moves from "partly proven" to
   "proven" or when a new gap is found.
