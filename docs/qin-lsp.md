# Qin LSP

## Architecture

Qin LSP is a Volar language server surface for `.qin` files.

- `packages/qin-language/qin-language-server/src/index.ts` starts the Volar server and wires TypeScript language service plugins.
- `packages/qin-language/qin-language-server/src/QinLanguagePlugin.ts` owns Qin virtual code and source mappings.
- `packages/qin-language/qin-language-server/src/QinGeneratedParserProbe.ts` loads the Java Qin parser compiled to TypeScript from `@qin/generated-qin-parser-ts`.
- `packages/qin-idea-plugin-debug` packages the IDEA LSP client integration and editor-side completion behavior.

The parser and AST decide Qin syntax. The virtual TypeScript exists only to ask TypeScript/Volar for editor services such as completion, hover, formatting, inlay hints, and navigation.

## Parser And Virtual TypeScript Boundaries

Qin/Slime parser ASI rules accept user source without explicit semicolons. `SemicolonASI()` is grammar behavior: it consumes an explicit semicolon, or accepts a statement boundary when the token stream satisfies ASI conditions such as line terminator, `}`, or EOF.

Virtual TypeScript must not rely on TypeScript language service ASI guesses. The TypeScript language service can offer edit-context completions that are not identical to final AST parsing. For example, strict TypeScript parsing can treat:

```ts
export { Counter }
Co
```

as an `ExportDeclaration` followed by an `ExpressionStatement`, while completion at `Co` can still prefer the `from` keyword because the edit context after `export { Counter }` can continue as `export { Counter } from ...`.

Generated virtual TypeScript must therefore emit explicit statement boundaries:

```ts
const Counter = new __QinObject_Counter();
export { Counter };
Co
```

Do not modify the user's Qin source to add semicolons. Add semicolons only in generated TypeScript, linked module output, or other compiler-owned emitted code where the emitter knows it is ending a statement.

## Completion Rules

- IDEA typed handlers should trigger completion for Qin/OVS/CSSTS identifier input and member access without consuming typed characters.
- Enter on an active Qin LSP lookup must complete the selected lookup item, not insert a blank line.
- Do not synthesize broad or string-scanned completion items in the IDEA plugin to hide language-server problems.
- When a completion result is wrong, reproduce it at the Qin language-server boundary first, then validate the IDEA editor path.

## Java Types In TypeScript LSP

Qin backend targets JVM `.class`, but the editor-service surface is still TypeScript/Volar. Java library completion should enter TypeScript LSP as generated `.d.ts`, not by replacing Qin LSP with a Java LSP.

Use two type-source frontends that feed one Qin Java semantic model:

- Java source files: use the Subhuti Java parser in `slime/java-slime/slime-java`. It follows the same broad PEG/Subhuti style as Slime/Qin parsers: parser classes extend `SubhutiParser`, grammar rules are `@SubhutiRule` methods, and AST conversion is available through `JavaCstToAst`. Reuse it for source `.java` -> Java AST -> `QinJavaSemanticModel` -> `.d.ts`.
- JDK, dependency jars, and compiled project `.class` files: do not run the source parser on bytecode. Read classpath metadata through a classfile reader such as ASM or the JDK compiler/model APIs, then normalize into the same `QinJavaSemanticModel` shape before emitting `.d.ts`.

The `.d.ts` generator should produce declarations that TypeScript can consume through Qin virtual service scripts, for example:

```ts
declare module "java:java.util" {
  export class ArrayList<T> {
    add(value: T): boolean;
    get(index: number): T;
    size(): number;
  }
}
```

Do not generate broad `any` declarations as a shortcut. Prefer conservative, accurate mappings and make missing Java type shapes visible in tests. Source parser recovery is for editing source Java/Qin; classpath `.class` indexing should be deterministic metadata extraction.

## Logging

Use `glogjs`/`logToFile` for Qin language server diagnostics. Completion logging should include request URI, language id, position, trigger kind, trigger character, item count, and first labels. Logs are diagnostic support only; they are not a fix.

## Validation

For LSP fixes, validate in this order:

1. Rebuild the language server bundle when `qin-language-server/src` changes:

```powershell
npx tsdown
```

2. Run the language-server smoke:

```powershell
npx tsx tests/test-language-server.ts
```

3. Run focused IDEA platform tests from `packages/qin-idea-plugin-debug`:

```powershell
.\gradlew.bat test --tests com.qin.debug.lsp.QinLspCompletionPlatformTest -PqinLocalIdeaHome="D:/Program Files/JetBrains/IntelliJ IDEA 2026.1.3"
```

4. Run package smoke:

```powershell
.\gradlew.bat lspPluginPackageSmoke -PqinLocalIdeaHome="D:/Program Files/JetBrains/IntelliJ IDEA 2026.1.3"
```

If the bug is user-visible in IDEA, do not claim full completion until the editor path is validated or clearly reported as not yet proven.

## Documentation Rule

When a durable Qin LSP rule is discovered, update this document and the `qin-lsp` Codex skill in the same change. Also update broader Qin skills when the rule affects parser/compiler/runtime policy beyond LSP.
