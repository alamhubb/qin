# Qin IDEA LSP Integration

The IDEA plugin registers `.qin`, `.ovs`, and `.cssts` as pure LSP-backed file
types. IDEA does not implement Qin, OVS, or CSSTS syntax locally. It starts the
existing Volar language servers and shows diagnostics, completion, and semantic
tokens returned by those servers.

Language server bundles are resolved from each language project's
`qin.config.js` `language.serverBundle` field in the current `qinall`
workspace:

- `qin/packages/qin-language/dist/language-server.cjs`
- `ovsjs/ovs-language/dist/language-server.js`
- `cssts/cssts-language/dist/language-server.cjs`

Runtime environment:

- `QIN_LSP_NODE`: optional Node executable path. Defaults to `node.exe` on
  Windows and `node` elsewhere.
- `QIN_LSP_TYPESCRIPT_TSDK`: optional TypeScript SDK path. If it is not set, the
  plugin resolves TypeScript from the workspace `node_modules`.

Node is only used for the Volar/LSP editor process. Qin syntax diagnostics still
come from generated QinParser TypeScript, which is generated from the Java parser.

## Verification Matrix

The non-interactive verification path is intentionally cross-project:

- `qinGeneratedParserDryRun` verifies the Java `com.qin.parser.QinParser`
  generation metadata before the LSP matrix trusts the generated TypeScript
  package.
- `qinLanguageTest` verifies `.qin` parser diagnostics and confirms invalid
  Qin source produces visible `Qin transform failed` virtual TypeScript instead
  of identity source text.
- `ovsLanguageTest` verifies `.ovs` Volar diagnostics and the OVS compiler
  parser chain. OVS depends on the shared `@qin/generated-qin-parser-ts`
  package and its tests assert transform failures do not fall back to identity
  source text.
- `csstsLanguageTest` verifies `.cssts` Volar diagnostics and the CSSTS compiler
  parser chain. CSSTS also depends on the shared
  `@qin/generated-qin-parser-ts` package and asserts transform failures stay
  visible.
- `lspVerificationMatrixSmoke` reads each language project's `qin.config.js`
  and verifies parser, compiler, generated package, language-server bundle, and
  IDEA client references point at the intended workspace artifacts.
- `qinJvmClassTargetSmoke` verifies the Qin CLI path still compiles and runs JVM
  `.class` output; this keeps editor/LSP TypeScript usage separate from the Qin
  runtime target.

Run the full gate from this directory:

```powershell
.\gradlew.bat check
```

That `check` task depends on the language-project tests, generated-parser dry
run, LSP registry/command/diagnostic smokes, verification matrix, UI fixture
smoke, and JVM `.class` smoke.

## No Fallback Policy

The IDEA plugin must not provide local parser, lexer, or completion fallback for
Qin, OVS, or CSSTS. It registers file types, starts the configured Volar
language server, and displays LSP results.

Language servers must keep parser and transform failures visible:

- Qin failures generate `throw new Error("Qin transform failed: ...")` in the
  virtual TypeScript script.
- OVS failures generate `throw new Error("OVS transform failed: ...")`.
- CSSTS failures generate `throw new Error("CSSTS transform failed: ...")`.

Returning no diagnostics is valid only for non-target documents or successful
parses. It is not an acceptable success path for generated-parser, compiler, or
transform errors.

## UI Fixture

Use the bundled fixture project for a real IDEA UI check:

```powershell
.\gradlew.bat runIdeLspFixture
```

This opens `fixtures/lsp-ui` in the IDE with the plugin loaded. The fixture has
valid and invalid `.qin`, `.ovs`, and `.cssts` files so the editor can verify
file type registration, LSP startup, and diagnostics without adding local IDEA
syntax parsing.

The fixture itself is covered by the non-interactive smoke task:

```powershell
.\gradlew.bat lspUiFixtureSmoke
```
