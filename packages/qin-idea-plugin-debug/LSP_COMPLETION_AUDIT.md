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
| Java parser is authoritative for Qin syntax | `qinGeneratedParserDryRun` verifies `com.qin.parser.QinParser` metadata, generated output package, and `generatedParserTarget`; `QinParserObjectDeclarationSmokeTestMain` verifies `object` parses through PEG CST and is not source-string rewritten; `test-generated-parser-parity.ts` compares Java QinParser and generated TypeScript QinParser acceptance on a Qin corpus including `object`, Qin object method bodies with local binding plus early-return `if`, Qin object method bodies with nested `if` branches and branch-local bindings, Qin object method bodies with try/catch/throw, decorated/default-export object, `object` as a type keyword, interface/type exports, generics, namespace and enum exports, import/export variants, type import/export variants, `java:` and local class inheritance, class method expressions, class fields and constructors, decorated classes and methods, `for...of`, `while`, `break`, `continue`, `switch`/`case`/`default`, `import.meta.url`, dynamic `import()`, destructuring, async/await, top-level export samples, and invalid import/decorator/class syntax. `QinParserFacadeUnifiedEntrySmokeTestMain` also verifies switch syntax, `import.meta.url`, and dynamic `import()` stay in the parser input and are not lowered through string rewrites. `lspVerificationMatrixSmoke` now locks the current parity corpus case names so broad coverage cannot be silently narrowed. | Proven for current Qin parser parity corpus |
| Handwritten TS `slime-parser` is deprecated for mainline parser paths | Active Qin/OVS/CSSTS language and compiler projects point parser authority at Java-generated TypeScript parser packages. `lspWorkspaceInventorySmoke` rejects legacy parser dependencies including `slime-parser` from OVS/CSSTS language and language-server packages; `lspVerificationMatrixSmoke` rejects `slime-parser` from OVS/CSSTS compiler `qin.config.js` and scans compiler source so direct legacy TS `slime-parser` imports cannot re-enter the generated CST-to-AST bridge. Remaining `slime-parser` references are treated as legacy tests, migration comparisons, or old demos outside the active Qin/OVS/CSSTS Volar path. | Proven for current LSP-critical mainline inventory |
| Qin generated TypeScript parser is usable by Volar | `qin/packages/qin-language/qin.config.js` points `language.parser` at `generated/qin-parser-ts`; `test-language-plugin.ts` verifies generated Qin parser availability and parser diagnostics; `test-generated-parser-parity.ts` verifies generated parser behavior matches Java parser acceptance on the current corpus. `lspLanguageCliSmoke` now runs `qin language generate-parser --dry-run` for `qin-language` and asserts the `com.qin.parser.QinParser` entry, `@qin/generated-qin-parser-ts` package metadata, `generated/qin-parser-ts` parser target, and additional Slime CST/AST entry metadata. `QinCliLanguageLocalDependencyBuildSmokeTestMain` also verifies `qin language test --dry-run` runs the Java-to-TypeScript parser generation step before the configured language script, so language build/test/dev commands do not rely on a manually refreshed generated parser. | Proven for current `.qin` LSP scope |
| OVS inherits the shared generated parser chain | `ovs/ovs-compiler/tests/test-generated-parser-chain.ts` and `ovs-language/tests/test-generated-parser-chain.ts` check `OvsParser extends CssTsParser`, `instanceof CssTsParser`, `instanceof SlimeJavascriptParser`, preserves Qin `object` declarations through `QinObjectDeclaration(params)`, and parses Qin object method bodies with nested `if` branches, branch-local bindings, Qin object method bodies with try/catch/throw, mutable `while` locals plus assignment, `for`/`break`/`continue`, `do while`, `import.meta.url`, dynamic `import()`, plus CSSTS/OVS syntax through the Qin-managed compiler and language projects. `ovs-language/tests/test-language-server.ts` also opens `qin-rich-valid.ovs` and asserts a Qin object with nested `if`, branch-local bindings, CSSTS `css { displayFlex }`, and OVS render syntax does not produce `OVS transform failed` diagnostics. The OVS language-chain smoke also rejects direct language/package dependencies on legacy parser packages such as `slime-parser`, `slime-token`, `slime-ast`, and `subhuti`; parser authority must flow through `@qin/generated-qin-parser-ts` and `ovs-compiler`. `lspVerificationMatrixSmoke` also reads `OvsParser.ts` and verifies it imports `@qin/generated-qin-parser-ts`, extends `CssTsParser`, preserves Qin declarations, uses generated `Alternative.of`/token normalization, normalizes generated CST in the compiler transform, locks OVS smoke coverage for generated-parser `try`/`catch`/`throw`, mutable `while` local/assignment, `for`/`break`/`continue`, `do while`, `import.meta`, and dynamic import tokens, does not keep a local generated-runtime adapter copy, keeps the OVS language dependency gate in place, verifies the rich Qin diagnostics fixture remains in the OVS LSP smoke, rejects direct legacy TS `slime-parser` imports, confines remaining legacy TS `slime-ast` imports to explicit AST construction boundaries, and requires OVS matrix tasks to run after CSSTS tasks so local `file:` dependency dist outputs are fresh. | Proven for current OVS parser-chain and LSP diagnostics smokes plus unified gate source audit |
| CSSTS inherits the shared generated parser chain | `cssts/cssts-compiler/tests/test-generated-parser-chain.ts` and `cssts-language/tests/test-generated-parser-chain.ts` check `CssTsParser extends QinParser`, `instanceof SlimeJavascriptParser`, and parser-chain parsing of Qin object method bodies with nested `if` branches, branch-local bindings, Qin object method bodies with try/catch/throw, mutable `while` locals plus assignment, `for`/`break`/`continue`, `do while`, `import.meta.url`, dynamic `import()`, plus CSSTS syntax through the Qin-managed compiler and language projects. `cssts-language/tests/test-language-server.ts` also opens `qin-rich-valid.cssts` and asserts a Qin object with nested `if`, branch-local bindings, and CSSTS `css { colorRed, displayFlex }` does not produce `CSSTS transform failed` diagnostics. The CSSTS language-chain smoke also rejects direct `cssts-language` dependencies on legacy parser packages such as `slime-parser`, `slime-token`, `slime-ast`, and `subhuti`; parser authority must flow through `@qin/generated-qin-parser-ts` and `cssts-compiler`. `lspVerificationMatrixSmoke` also reads `CssTsParser.ts`, the CSSTS generated runtime adapter, transform entry, and CST-to-AST extension to verify the shared generated Qin parser import, generated Qin parser inheritance, token/CST normalization, `Or` semantics, Java-list bridge, explicit CST-to-AST registration boundary, rich Qin diagnostics fixture coverage, locks CSSTS smoke coverage for generated-parser `try`/`catch`/`throw`, mutable `while` local/assignment, `for`/`break`/`continue`, `do while`, `import.meta`, and dynamic import tokens, rejects direct legacy TS `slime-parser` imports, confines remaining legacy TS `slime-ast` imports to explicit AST construction boundaries, and the CSSTS language dependency gate. | Proven for current CSSTS parser-chain and LSP diagnostics smokes plus unified gate source audit |
| Volar LSP provides diagnostics | `lspServerDiagnosticsSmoke`, `qinLanguageTest`, `ovsLanguageTest`, and `csstsLanguageTest` verify invalid `.qin`, `.ovs`, and `.cssts` diagnostics. The OVS and CSSTS language tests also request diagnostics for rich Qin object documents to prove valid generated-parser-chain input is not reported as a transform failure. | Proven |
| Volar LSP provides editor features | `lspServerDiagnosticsSmoke` requests and asserts completion plus completion-item resolve, code actions plus code-action resolve for Qin import-policy quickfixes, hover, signature help, definition, declaration, type definition, implementation, call hierarchy, references, document highlight, rename, prepare rename, document symbols, workspace symbols, and full plus range semantic tokens for `.qin`, `.ovs`, and `.cssts` fixtures, including reference declaration/usage source positions returned through the IDEA-facing LSP connection. The Qin fixture also requests and asserts document formatting, range formatting, and on-type formatting through TypeScript formatter edits mapped back to source, declaration/definition from usage back to the source `.qin` declaration, implementation from an interface to a source `.qin` class declaration, call hierarchy prepare plus incoming/outgoing calls through source caller/callee/callsite ranges, inlay hints through source mappings, document links for local imports, folding ranges, linked editing ranges, and selection ranges. Each language project's `tests/test-language-server.ts` also asserts completion, definition, references, document symbols, and semantic tokens contain concrete TS/Qin/OVS/CSSTS fixture symbols; Qin's language-server smoke additionally asserts completion-item resolve detail, import-policy code-action resolve edits, declaration, type definition, implementation, call hierarchy, formatting, range formatting, on-type formatting, semantic token ranges, inlay hint, document link, folding, linked editing, selection range, and workspace symbol behavior. Qin, OVS, and CSSTS now all include `for...of` LSP fixtures; the OVS and CSSTS fixtures assert loop variable completion, definition, declaration/usage references, document symbols, and exact semantic token source positions. `lspVerificationMatrixSmoke` verifies those language tests and the IDEA diagnostics smoke keep the feature requests and content assertions in place, scans Qin's service plugin so import-policy quickfixes keep their own `resolveCodeAction` path, and scans Qin virtual-code mappings so source-mapped Volar features keep `inlayHints: true` alongside completion, formatting, navigation, semantic tokens, and diagnostics. | Proven for current smoke fixtures |
| IDEA is only an LSP client for these languages | `plugin.xml` registers `.qin`, `.ovs`, `.cssts` file types and one `platform.lsp.serverSupportProvider`; `QinLspNoLocalParserSmokeTestMain` scans plugin sources for local parser, lexer, syntax highlighter, and completion contributor markers; `QinLspLanguageRegistrySmokeTestMain` verifies IDEA LSP registry metadata comes from each language project's `qin.config.js` `language` block and rejects hardcoded production `dist/language-server*` paths; `QinLspWorkspaceInventorySmokeTestMain` rejects legacy `*-intellij-client` projects under OVS/CSSTS language packages; `LSP.md` records the no-local-parser policy. | Proven for current IDEA plugin sources and language-project inventory |
| IDEA plugin packaging and fixture coverage stay in the gate | `lspVerificationMatrixSmoke` verifies the LSP matrix and `check` depend on descriptor, package, and UI fixture smokes; `lspUiFixtureSmoke` verifies valid/invalid `.qin`, `.ovs`, and `.cssts` fixture files and checks that `lspServerDiagnosticsSmoke` opens all three language documents and requests diagnostics, completion, definition, references, document symbols, and semantic tokens. | Proven |
| Qin-managed project metadata is used | `QinConfigJsParserSmokeTestMain` verifies `workspaces` in `qin.config.js` load into Qin's monorepo package list; `QinLanguageConfigSmokeTestMain` verifies runtime/tooling `language` metadata can omit `language.extension` while parser/compiler/server language projects still require it; `lspVerificationMatrixSmoke` loads each language project's and compiler project's `qin.config.js` and verifies `language.parser`, `language.compiler`, `language.serverBundle`, scripts, dependencies, and compiler-side generated parser chain smoke coverage; `lspLanguageCliSmoke` runs `qin language check` and `build`/`test` dry-runs for Qin, OVS, CSSTS language plus compiler projects, OVS/CSSTS workspace roots, the OVS/CSSTS runtimes, Vite plugins, scaffold CLIs, CSSTS Vue language plugin, and theme package. The workspace-root cases verify each root script dispatches through `qin language ... --root ...` for its owned packages instead of hiding orchestration in external package scripts. The language-server cases also verify `bundle`, `dev`, and `server --dry-run`; `qinLanguageLocalDependencyBuildSmoke` runs `QinCliLanguageLocalDependencyBuildSmokeTestMain`, proving Qin language scripts build local `file:` runtime dependencies before the downstream script, rebuild changed upstream source into fresh dependency output on the next invocation, run generated Java parser emission before language scripts when `generated` metadata exists, and reject circular local Qin dependencies; `compilerProjectsTest` runs `qin language test` for OVS and CSSTS compiler projects; `lspWorkspaceInventorySmoke` verifies the current LSP-critical language/compiler projects, OVS/CSSTS workspace roots, runtimes, Vite plugins, scaffold CLIs, CSSTS theme/Vue language plugin packages, Java Slime/Subhuti parser source, generated Qin parser TypeScript package, parser, CLI, and runtime projects are all owned by `qin.config.js`, rejects `npm run` forwarding in OVS/CSSTS workspace/tooling scripts and generated scaffold template `qin.config.js` files, reverse-scans `ovsjs/` plus `cssts/` so any new `qin.config.js` must be registered in the inventory or an approved scaffold template, and classifies OVS/CSSTS top-level package-only directories as explicit legacy/external exceptions while demo apps must be Qin-managed so unmanaged package projects cannot appear silently. The approved package-only exceptions must remain without `qin.config.js`, must not claim `.qin`/`.ovs`/`.cssts` or `@qin/generated-qin-parser-ts`, and must stay identifiable as either an external Vue copy, a historical VSCode extension, or the isolated `language-plugin-testts` experiment. | Proven for the current LSP-critical plus OVS/CSSTS workspace/tooling inventory and explicit legacy/external exceptions |
| Qin runtime target remains JVM `.class` | `qinJvmClassTargetSmoke` runs `qin run com.qin.lang.cli.SmokeTestMain`; the smoke compiles and reflects a 5-case `.class` corpus covering top-level function calls, `export const`, object member access, array literals, and `java:` import plus Java instance calls. `qinJvmClassDeclarationSmoke` runs `QinJvmClassDeclarationCorpusSmokeTestMain`, a 24-case class-declaration corpus covering Java `extends`, field annotations, fields, constructors, local member access, `this.field.method()`, runtime function methods, Java static calls, Java constructor returns, direct Java SlimeParser inheritance from parsed Qin source, parsed Qin method-body execution for binary and conditional returns, parsed Qin method-body execution with local binding plus early-return `if` branches, parsed Qin method-body execution with nested `if` branches, branch-local bindings, and fallthrough returns, parsed Qin source with multiple local classes plus cross-class member access, parsed Qin local class inheritance with inherited/child method execution, parsed Qin class fields plus all-args constructor metadata/execution, parsed same-class method invocation through `this.method()`, parsed Qin try/catch/throw exception flow, parsed Qin `while` statement execution through JVM loop control flow, parsed Qin mutable loop locals plus assignment execution through JVM while bytecode, parsed Qin `for`/`break`/`continue` execution through JVM loop bytecode, parsed Qin `do while` execution through JVM loop bytecode, parsed Qin `for...of` execution through JVM iterator bytecode, parsed Qin `switch`/`case`/`default` execution through JVM control flow, and Qin IR sequence expression bytecode with a `console.log` side effect plus returned value. `lspVerificationMatrixSmoke` also loads the runtime `qin.config.js` files for `qin-lang-cli`, `qin-lang-backend-jvm`, and `qin-runtime-core` and verifies Java entries, UTF-8 source encoding, and `build/classes` output. | Proven for current CLI and class-declaration corpus |
| Node/TypeScript is limited to editor/LSP/tooling | `LSP.md` states the boundary; `qinJvmClassTargetSmoke` and `qinJvmClassDeclarationSmoke` separately verify the runtime path; `lspVerificationMatrixSmoke` asserts those runtime smoke Gradle task blocks invoke `qin run` for JVM `.class` smoke mains and do not invoke `language`, `node`, `tsx`, or `tsdown`, and it rejects runtime project scripts that call editor/LSP tooling commands such as `node`, `tsx`, `tsdown`, `language`, or `server`. | Proven for current checked runtime tasks and runtime Qin configs |
| No fallback success path | Language plugin tests assert invalid input does not fall back to identity source text; transform failures generate visible `Qin/OVS/CSSTS transform failed` code. `qinCsstsCompilerNoFallbackSmoke` runs `QinCsstsCompilerNoFallbackSmokeTestMain`, which verifies Qin's CSSTS wrapper uses `RuntimeStore.getUsedStyles()` from the real CSSTS compiler path and rejects the old regex atom extraction bridge. `QinRuntimeImportMetaShimRemovalSmokeTestMain` verifies the old `__qin_import_meta_url__` name is now an ordinary identifier while formal `MetaProperty` AST still lowers `import.meta.url`. `QinEsmRuntimeFeatureParserScanSmokeTestMain` verifies JVM runtime feature rejection for dynamic `import()` and `import.meta` comes from QinParserFacade plus Slime AST nodes, not string/comment regex matches; `lspVerificationMatrixSmoke` also rejects the old QinParserFacade switch, `import.meta.url`, and dynamic `import()` string-lowering paths, the removed adapter import-meta shim, and runtime feature validator regex scanning for dynamic import/import.meta. `lspWorkspaceInventorySmoke` also keeps the legacy `language-plugin-testts` identity fallback visibly isolated as a package-only experiment; if it is ever promoted to active Qin management, that fallback must be removed and the project registered in inventory. | Proven for current language-server transform paths, Qin parser facade syntax-shim gates, Qin frontend adapter import-meta gate, Qin runtime feature parser scan gate, Qin CSSTS compiler wrapper source gate, and active-inventory isolation of the legacy TestTS fallback experiment |

## Known Gaps

These items are not proven complete yet:

- Full grammar authority across every Slime/Qin/OVS/CSSTS syntax branch is not
  exhaustively audited. Current evidence covers the active generated-parser path,
  a Java/TypeScript parser parity corpus with export, Qin `object`, Qin object
  method bodies with local binding plus early-return `if`, nested `if`
  method bodies with branch-local bindings and fallthrough returns, object
  method bodies with try/catch/throw, decorator, default-export object,
  `object` as a type keyword, interface/type exports, generics, namespace and enum exports, import/export variants, type
  import/export variants, `java:` import/class inheritance, local class
  inheritance, class method expression, class fields and constructors,
  decorated classes and methods, `for...of`, `while`, `break`, `continue`,
  `switch`/`case`/`default`, `import.meta.url`, dynamic `import()`,
  destructuring, async/await, top-level samples, and invalid import/decorator/class samples, plus
  parser-chain inheritance smoke fixtures.
- OVS/CSSTS parser classes, language servers, and CST-to-AST extension base now
  use the generated parser chain and `@qin/generated-qin-parser-ts`
  `SlimeCstToAstBridge`; direct legacy TS `slime-parser` imports are rejected by
  `lspVerificationMatrixSmoke`. Compiler AST lowering still uses legacy
  `slime-ast` plus `subhuti` TypeScript packages for AST constants, token
  factories, and CST/token types inside explicit AST construction boundaries.
  Full cleanup still requires exporting those remaining AST/token APIs from the
  Java-generated TypeScript package.
- The handwritten TS `slime-parser` package still exists for legacy tests,
  migration comparisons, and old demos. It is deprecated for active parser
  authority and must not be reintroduced into Qin/OVS/CSSTS language, compiler,
  or Volar LSP mainline dependencies.
- IDEA has smoke coverage for LSP startup, capabilities, diagnostics, packaging,
  and fixture registration, but manual IDE behavior still relies on
  `runIdeLspFixture` for visual confirmation.
- The `qin.config.js` management guarantee is verified for the current
  LSP-critical inventory plus OVS/CSSTS workspace roots, runtimes, Vite
  plugins, scaffold CLIs, CSSTS theme package, and CSSTS Vue language plugin.
  OVS/CSSTS roots are reverse-scanned so a new local `qin.config.js` cannot be
  added without inventory coverage, and top-level package-only directories are
  matched against explicit legacy/external exceptions while demo apps must be
  Qin-managed so unmanaged package projects cannot appear silently. Those
  package-only exceptions are also checked so they cannot claim the active
  Qin/OVS/CSSTS generated-parser chain or promote identity-fallback behavior
  into the mainline by accident. This still does not cover every nested experimental
  demo or unrelated adjacent repository in the whole workspace. Legacy
  language-local IDEA clients are explicitly rejected for the OVS/CSSTS language
  package roots.
- JVM `.class` execution is proven by the current focused CLI corpus and
  class-declaration corpus, including direct parsed-source class/method
  execution, parsed method bodies with local binding plus early-return `if`,
  parsed method bodies with nested `if` branches, branch-local bindings, and
  fallthrough returns, parsed multi-class local DTO access, parsed local class
  inheritance, parsed class field/all-args constructor execution, and parsed
  same-class method invocation through `this.method()`, parsed try/catch/throw,
  parsed `while` execution through JVM loop control flow, parsed mutable
  loop locals plus assignment execution, parsed `for`/`break`/`continue`
  loop execution, parsed `do while` execution, parsed `for...of`
  execution through JVM iterator bytecode, and parsed `switch`/`case`/`default`
  execution through JVM control flow, plus Qin IR sequence expression
  bytecode with a side-effect call and returned value, not by every Qin
  fullstack example, production-style application path, or every class
  declaration feature variant.
- The Node/TypeScript boundary is proven for the current checked runtime tasks,
  but not yet for every experimental demo or manually run helper outside the
  `check` gate.

## Next Hardening Steps

1. Expand the unified class-declaration `.class` gate toward broader parsed Qin
   source coverage for richer class member variants and remaining control-flow
   forms.
2. Keep expanding generated parser parity from the current focused corpus toward
   broader Slime/Qin syntax coverage.
3. Move the remaining OVS/CSSTS AST/token construction boundary off legacy TS
   `slime-ast`/`subhuti` once the Java-generated TS package exposes equivalent
   AST and token APIs.
4. Keep this audit updated whenever a requirement moves from "partly proven" to
   "proven" or when a new gap is found.
