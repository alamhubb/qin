# Qin / OVS / CSSTS LSP Completion Audit

This file tracks the current evidence for the unified Qin / OVS / CSSTS
language-development target. It is an audit map, not a completion claim.

## Target

Qin, OVS, and CSSTS should share one language architecture:

- Java SlimeParser / QinParser remains the authoritative grammar source.
- Java parser output is generated into TypeScript for the Volar LSP runtime.
- Qin, OVS, and CSSTS language servers use the generated parser chain and expose
  diagnostics, completion, definition, references, document symbols, and
  semantic tokens.
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

Run the unified LSP gate from this directory:

```powershell
.\gradlew.bat lspUnifiedMatrix
```

Run the full gate, including Qin JVM `.class` runtime smokes, with:

```powershell
.\gradlew.bat check
```

The `lspUnifiedMatrix` task currently covers the checks below. It also verifies
this audit keeps its target, requirement-audit, known-gaps, and next-hardening
sections so partial evidence cannot silently become a completion claim. OVS
matrix tasks depend on the matching CSSTS tasks so local `file:` dependencies
refresh upstream `dist` outputs before downstream parser-chain tests run.

The task currently covers:

- `qinLanguageTest`
- `ovsLanguageTest`
- `csstsLanguageTest`
- `ovsCompilerTest`
- `csstsCompilerTest`
- `compilerProjectsTest`
- `qinGeneratedParserDryRun`
- `lspRegistrySmoke`
- `lspServerCommandLineSmoke`
- `lspServerDiagnosticsSmoke`
- `lspLanguageCliSmoke`
- `lspVerificationMatrixSmoke`
- `lspPluginDescriptorSmoke`
- `lspNoLocalParserSmoke`
- `lspWorkspaceInventorySmoke`
- `lspPluginPackageSmoke`
- `lspUiFixtureSmoke`

The full `check` task also covers:

- `qinJvmClassTargetSmoke`
- `qinJvmClassDeclarationSmoke`

## Requirement Audit

| Requirement | Current evidence | Status |
| --- | --- | --- |
| Java parser is authoritative for Qin syntax | `qinGeneratedParserDryRun` verifies `com.qin.parser.QinParser` metadata, generated output package, and `generatedParserTarget`; `QinParserObjectDeclarationSmokeTestMain` verifies `object` parses through PEG CST and is not source-string rewritten; `test-generated-parser-parity.ts` compares Java QinParser and generated TypeScript QinParser acceptance on a Qin corpus including `object`, Qin object method bodies with local binding plus early-return `if`, decorated/default-export object, `object` as a type keyword, interface/type exports, generics, namespace and enum exports, import/export variants, type import/export variants, `java:` and local class inheritance, class method expressions, class fields and constructors, decorated classes and methods, control flow, destructuring, async/await, top-level export samples, and invalid import/decorator/class syntax. | Proven for current Qin parser parity corpus |
| Qin generated TypeScript parser is usable by Volar | `qin/packages/qin-language/qin.config.js` points `language.parser` at `generated/qin-parser-ts`; `test-language-plugin.ts` verifies generated Qin parser availability and parser diagnostics; `test-generated-parser-parity.ts` verifies generated parser behavior matches Java parser acceptance on the current corpus. | Proven for current `.qin` LSP scope |
| OVS inherits the shared generated parser chain | `ovs/ovs-compiler/tests/test-generated-parser-chain.ts` and `ovs-language/tests/test-generated-parser-chain.ts` check `OvsParser extends CssTsParser`, `instanceof CssTsParser`, `instanceof SlimeJavascriptParser`, preserves Qin `object` declarations through `QinObjectDeclaration(params)`, and parses Qin object plus CSSTS/OVS syntax through the Qin-managed compiler and language projects. `lspVerificationMatrixSmoke` also reads `OvsParser.ts` and verifies it imports `@qin/generated-qin-parser-ts`, extends `CssTsParser`, preserves Qin declarations, uses generated `Alternative.of`/token normalization, normalizes generated CST in the compiler transform, does not keep a local generated-runtime adapter copy, and requires OVS matrix tasks to run after CSSTS tasks so local `file:` dependency dist outputs are fresh. | Proven for current OVS parser-chain smokes and unified gate source audit |
| CSSTS inherits the shared generated parser chain | `cssts/cssts-compiler/tests/test-generated-parser-chain.ts` and `cssts-language/tests/test-generated-parser-chain.ts` check `CssTsParser extends QinParser`, `instanceof SlimeJavascriptParser`, and parser-chain parsing of Qin object plus CSSTS syntax through the Qin-managed compiler and language projects. `lspVerificationMatrixSmoke` also reads `CssTsParser.ts`, the CSSTS generated runtime adapter, and transform entry to verify the shared generated Qin parser import, generated Qin parser inheritance, token/CST normalization, `Or` semantics, and Java-list bridge. | Proven for current CSSTS parser-chain smokes and unified gate source audit |
| Volar LSP provides diagnostics | `lspServerDiagnosticsSmoke`, `qinLanguageTest`, `ovsLanguageTest`, and `csstsLanguageTest` verify invalid `.qin`, `.ovs`, and `.cssts` diagnostics. | Proven |
| Volar LSP provides completion, definition, references, symbols, and semantic tokens | `lspServerDiagnosticsSmoke` requests and asserts completion, definition, document symbols, and semantic tokens for `.qin`, `.ovs`, and `.cssts` fixtures and verifies each server advertises `referencesProvider`. Each language project's `tests/test-language-server.ts` also asserts completion, definition, references, document symbols, and semantic tokens contain concrete TS/Qin/OVS/CSSTS fixture symbols. `lspVerificationMatrixSmoke` verifies those language tests keep the feature requests and content assertions in place. | Proven for current smoke fixtures |
| IDEA is only an LSP client for these languages | `plugin.xml` registers `.qin`, `.ovs`, `.cssts` file types and one `platform.lsp.serverSupportProvider`; `QinLspNoLocalParserSmokeTestMain` scans plugin sources for local parser, lexer, syntax highlighter, and completion contributor markers; `QinLspLanguageRegistrySmokeTestMain` verifies IDEA LSP registry metadata comes from each language project's `qin.config.js` `language` block and rejects hardcoded production `dist/language-server*` paths; `QinLspWorkspaceInventorySmokeTestMain` rejects legacy `*-intellij-client` projects under OVS/CSSTS language packages; `LSP.md` records the no-local-parser policy. | Proven for current IDEA plugin sources and language-project inventory |
| IDEA plugin packaging and fixture coverage stay in the gate | `lspVerificationMatrixSmoke` verifies the LSP matrix and `check` depend on descriptor, package, and UI fixture smokes; `lspUiFixtureSmoke` verifies valid/invalid `.qin`, `.ovs`, and `.cssts` fixture files and checks that `lspServerDiagnosticsSmoke` opens all three language documents and requests diagnostics, completion, definition, document symbols, and semantic tokens. | Proven |
| Qin-managed project metadata is used | `QinConfigJsParserSmokeTestMain` verifies `workspaces` in `qin.config.js` load into Qin's monorepo package list; `QinLanguageConfigSmokeTestMain` verifies runtime/tooling `language` metadata can omit `language.extension` while parser/compiler/server language projects still require it; `lspVerificationMatrixSmoke` loads each language project's and compiler project's `qin.config.js` and verifies `language.parser`, `language.compiler`, `language.serverBundle`, scripts, dependencies, and compiler-side generated parser chain smoke coverage; `lspLanguageCliSmoke` runs `qin language check` and `build`/`test` dry-runs for Qin, OVS, CSSTS language plus compiler projects, OVS/CSSTS workspace roots, the OVS/CSSTS runtimes, Vite plugins, scaffold CLIs, CSSTS Vue language plugin, and theme package. The workspace-root cases verify each root script dispatches through `qin language ... --root ...` for its owned packages instead of hiding orchestration in external package scripts. The language-server cases also verify `bundle`, `dev`, and `server --dry-run`; `qinLanguageLocalDependencyBuildSmoke` runs `QinCliLanguageLocalDependencyBuildSmokeTestMain`, proving Qin language scripts build local `file:` runtime dependencies before the downstream script and reject circular local Qin dependencies; `compilerProjectsTest` runs `qin language test` for OVS and CSSTS compiler projects; `lspWorkspaceInventorySmoke` verifies the current LSP-critical language/compiler projects, OVS/CSSTS workspace roots, runtimes, Vite plugins, scaffold CLIs, CSSTS theme/Vue language plugin packages, Java Slime/Subhuti parser source, generated Qin parser TypeScript package, parser, CLI, and runtime projects are all owned by `qin.config.js`, and rejects `npm run` forwarding in OVS/CSSTS workspace/tooling scripts. | Proven for the current LSP-critical plus OVS/CSSTS workspace/tooling inventory |
| Qin runtime target remains JVM `.class` | `qinJvmClassTargetSmoke` runs `qin run com.qin.lang.cli.SmokeTestMain`; the smoke compiles and reflects a 5-case `.class` corpus covering top-level function calls, `export const`, object member access, array literals, and `java:` import plus Java instance calls. `qinJvmClassDeclarationSmoke` runs `QinJvmClassDeclarationCorpusSmokeTestMain`, a 14-case class-declaration corpus covering Java `extends`, field annotations, fields, constructors, local member access, `this.field.method()`, runtime function methods, Java static calls, Java constructor returns, direct Java SlimeParser inheritance from parsed Qin source, parsed Qin method-body execution for binary and conditional returns, parsed Qin method-body execution with local binding plus early-return `if` branches, parsed Qin source with multiple local classes plus cross-class member access, parsed Qin local class inheritance with inherited/child method execution, and parsed Qin class fields plus all-args constructor metadata/execution. `lspVerificationMatrixSmoke` also loads the runtime `qin.config.js` files for `qin-lang-cli`, `qin-lang-backend-jvm`, and `qin-runtime-core` and verifies Java entries, UTF-8 source encoding, and `build/classes` output. | Proven for current CLI and class-declaration corpus |
| Node/TypeScript is limited to editor/LSP/tooling | `LSP.md` states the boundary; `qinJvmClassTargetSmoke` and `qinJvmClassDeclarationSmoke` separately verify the runtime path; `lspVerificationMatrixSmoke` asserts those runtime smoke Gradle task blocks invoke `qin run` for JVM `.class` smoke mains and do not invoke `language`, `node`, `tsx`, or `tsdown`, and it rejects runtime project scripts that call editor/LSP tooling commands such as `node`, `tsx`, `tsdown`, `language`, or `server`. | Proven for current checked runtime tasks and runtime Qin configs |
| No fallback success path | Language plugin tests assert invalid input does not fall back to identity source text; transform failures generate visible `Qin/OVS/CSSTS transform failed` code. | Proven for current language-server transform paths |

## Known Gaps

These items are not proven complete yet:

- Full grammar authority across every Slime/Qin/OVS/CSSTS syntax branch is not
  exhaustively audited. Current evidence covers the active generated-parser path,
  a Java/TypeScript parser parity corpus with export, Qin `object`, Qin object
  method bodies with local binding plus early-return `if`, decorator,
  default-export object, `object` as a type keyword, interface/type exports,
  generics, namespace and enum exports, import/export variants, type
  import/export variants, `java:` import/class inheritance, local class
  inheritance, class method expression, class fields and constructors,
  decorated classes and methods, control flow, destructuring, async/await,
  top-level samples, and invalid import/decorator/class samples, plus
  parser-chain inheritance smoke fixtures.
- IDEA has smoke coverage for LSP startup, capabilities, diagnostics, packaging,
  and fixture registration, but manual IDE behavior still relies on
  `runIdeLspFixture` for visual confirmation.
- The `qin.config.js` management guarantee is verified for the current
  LSP-critical inventory plus OVS/CSSTS workspace roots, runtimes, Vite
  plugins, scaffold CLIs, CSSTS theme package, and CSSTS Vue language plugin,
  not every experimental demo or unrelated adjacent repository in the whole
  workspace. Legacy language-local IDEA clients are explicitly rejected for the
  OVS/CSSTS language package roots.
- JVM `.class` execution is proven by the current focused CLI corpus and
  class-declaration corpus, including direct parsed-source class/method
  execution, parsed method bodies with local binding plus early-return `if`,
  parsed multi-class local DTO access, parsed local class inheritance, and
  parsed class field/all-args constructor execution, not by every Qin fullstack example,
  production-style application path, or every class declaration feature variant.
- The Node/TypeScript boundary is proven for the current checked runtime tasks,
  but not yet for every experimental demo or manually run helper outside the
  `check` gate.

## Next Hardening Steps

1. Expand the unified class-declaration `.class` gate toward broader parsed Qin
   source coverage for local class inheritance and richer method bodies.
2. Keep expanding generated parser parity from the current small corpus toward
   broader Slime/Qin syntax coverage.
3. Keep this audit updated whenever a requirement moves from "partly proven" to
   "proven" or when a new gap is found.
