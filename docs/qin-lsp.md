# Qin LSP

## Architecture

Qin LSP is a Volar language server surface for `.qin` files.

- `packages/qin-language/qin-language-server/src/index.ts` starts the Volar server and wires TypeScript language service plugins.
- `packages/qin-language/qin-language-server/src/QinLanguagePlugin.ts` owns Qin virtual code and source mappings.
- `packages/qin-language/qin-language-server/src/QinGeneratedParserProbe.ts` loads the Java Qin parser compiled to TypeScript from `@qin/generated-qin-parser-ts`.
- `packages/qin-idea-plugin-debug` packages the IDEA LSP client integration and editor-side completion behavior.

The parser and AST decide Qin syntax. The virtual TypeScript exists only to ask TypeScript/Volar for editor services such as completion, hover, formatting, inlay hints, and navigation.

IDEA integration should use a hybrid architecture instead of trying to make LSP replace the IntelliJ Platform:

- LSP/Volar remains the TypeScript-compatible completion, hover, formatting, and diagnostics surface.
- IDEA Lexer/ParserDefinition/PSI provide native file structure, syntax highlighting, references, and future refactoring support.
- Java interop navigation resolves through IDEA Java PSI. A Qin expression like `Greeter.greet` imported from `java:demo` should resolve to `demo.Greeter` and its real `PsiMethod` through `JavaPsiFacade`, not to a generated `.d.ts` declaration.
- Java interop import tables and member qualifiers in IDEA should be derived from Qin PSI/parser adapter tokens, not regex scanning or raw source character walks. Until full Qin AST-backed PSI is available, a shared token-stream adapter is the acceptable transitional boundary.
- IDEA semantic annotations for Java interop should reuse Qin PSI references and Java PSI resolution. For unresolved `java:` imports or static members, mark the Qin PSI element through an `Annotator`; do not duplicate a separate Java lookup model in the annotator.
- IDEA Find Usages and Rename for Java interop should flow through the same Qin `PsiReference`. If a Java class or method is renamed, `handleElementRename` should update only the Qin reference token. Do not implement separate string-rewrite refactoring paths for Java interop.
- Qin PSI is an IDEA adapter for Qin parser/AST and symbol model output. Do not grow a separate semantic model inside the IDEA plugin when the parser or shared Qin symbol model should own the language fact.

The IDEA lexer should be backed by a Qin/Slime/Subhuti token adapter rather than a separate hand-written Qin token model. Map parser token names into IDEA `IElementType` values at this boundary, then let `QinLexer` act only as the IntelliJ `LexerBase` state wrapper. Do not call a whole-file parser directly as a token-by-token lexer hot path.

Do not duplicate Qin/Slime lexical rules in the IDEA plugin. Token creation should use the same Slime/Subhuti token definitions as `QinParser` (`JavaScriptTokens.getTokens()`), and shared token classification such as keyword detection should use Slime token utilities instead of IDEA-local keyword lists. IDEA-owned code may map shared token facts into platform-specific `IElementType` categories, because that mapping is an IntelliJ presentation boundary rather than a language rule.

The adapter may own trivia and editor-recovery details required by the IntelliJ lexer contract: whitespace/comment tokens, stable token ranges, fast advancement, and highlightable incomplete literals while the user is typing. That recovery is an editor-tokenization boundary, not a broad parser fallback and not a reason to hide real Qin parser defects.

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

Use multiple type-source frontends that feed one Qin LSP symbol model:

- Java source files: use the Subhuti Java parser in `slime/java-slime/slime-java`. It follows the same broad PEG/Subhuti style as Slime/Qin parsers: parser classes extend `SubhutiParser`, grammar rules are `@SubhutiRule` methods, and AST conversion is available through `JavaCstToAst`. Reuse it for source `.java` -> Java AST -> semantic declarations -> `QinSymbolModel` -> `.d.ts`.
- JDK, dependency jars, and compiled project `.class` files: do not run the source parser on bytecode. Read classpath metadata through a classfile reader such as ASM or the JDK compiler/model APIs, then normalize into `QinSymbolModel` before emitting `.d.ts`.
- Qin and Java compilation output: use `QinIrProgram`/`QinIrClassDeclaration` as the executable backend IR, then derive `QinSymbolModel` entries when LSP declarations need project-local Qin classes.

Do not maintain independent Java semantic and TypeScript declaration models. `QinJavaSemanticModel` is currently the Java-source semantic slice used by `QinJavaAstIrLowerer`; `QinDeclarationIrLowerer` is a Qin AST -> executable IR lowering boundary; `QinIrProgram` is the backend IR consumed by JVM/JS emitters. These are related but not interchangeable. The durable LSP shape is `QinSymbolModel`: one shared symbol/type model with adapters from Java source semantics, classpath metadata, and Qin IR.

`QinIrProgram` and `QinSymbolModel` are peer models, not inheritance parents or children. Share reusable value objects such as `QinIrTypeRef`, annotation refs, qualified names, modifiers, and visibility only when the meaning is truly identical. Convert between models through explicit adapters such as `QinIrProgram -> QinSymbolModel` and `QinJavaSemanticModel -> QinSymbolModel`. Do not add duplicate class/method/field/type shapes with different names unless the semantics are genuinely different.

For editor completion, `src/main/*.java` source symbols are exposed to TypeScript through a generated extra service `.d.ts`, not through IDEA-side completion items. A Qin file importing `java:demo` should see declarations derived from Java source package `demo`, for example `import { Greeter } from "java:demo"` followed by `Greeter.` should complete static methods from `src/main/Greeter.java`.

For IDEA navigation, the same Qin import should resolve through the platform PSI path:

- `import { Greeter } from "java:demo"` resolves `Greeter` to the Java `PsiClass` `demo.Greeter`.
- `Greeter.greet` resolves `greet` to the static Java `PsiMethod` on `demo.Greeter`.
- The `.d.ts` bridge can inform LSP completion, but it must not be the only source of truth for Ctrl+Click, Go To Declaration, Find Usages, or Rename inside IDEA.

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

## Encoding

All Qin LSP source files, docs, configs, fixtures, generated text artifacts, and related skills must be UTF-8 without BOM. Use another encoding only when an external tool or file format absolutely requires it, and document the reason. On Windows, avoid default `Out-File` writes for tracked text; use UTF-8-no-BOM writers and inspect bytes plus git history before repairing mojibake.

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

IDEA platform fixtures can create lightweight files whose URI looks like `/src/...` instead of a real project directory. Do not use those virtual fixture paths to prove language-server behavior that scans the filesystem, such as Java source `.d.ts` generation from `qin.config.js` and `src/main/*.java`. Validate that behavior in `packages/qin-language/tests/test-language-server.ts` or another real-filesystem LSP smoke. Use IDEA platform tests for IDEA-owned behavior such as typed handlers, lookup handling, PSI references, highlighting, and packaging.

## Documentation Rule

When a durable Qin LSP rule is discovered, update this document and the `qin-lsp` Codex skill in the same change. Also update broader Qin skills when the rule affects parser/compiler/runtime policy beyond LSP.
