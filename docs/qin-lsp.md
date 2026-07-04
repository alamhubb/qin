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
- IDEA descriptor smoke must verify `com.intellij.modules.java` is declared whenever Qin Java interop uses `JavaPsiFacade`; Java PSI navigation should fail packaging checks if the Java platform dependency is removed.
- Java interop import tables and member qualifiers in IDEA should be derived from Qin PSI/parser adapter tokens, not regex scanning or raw source character walks. Until full Qin AST-backed PSI is available, a shared token-stream adapter is the acceptable transitional boundary.
- Import specifier PSI is a Qin language structure, not a Java-only structure. Java imports and Qin module imports should both use the same `IMPORT_SPECIFIER` PSI node, while `QinJavaImportTable` and `QinModuleImportTable` decide meaning from the module specifier.
- Import binding lookup should parse the current `IMPORT_SPECIFIER` PSI node plus its declaration's module specifier. Do not resolve a specifier element by scanning the whole import declaration for a text match, because aliases can share text with another exported name in the same declaration.
- Import binding lookup should use `QinSourceStructure.importSpecifierAtNameOffset` for both exported import names and local aliases. Do not iterate import declarations or split `exportedNameRange` and `localNameRange` checks inside `QinImportBindings`, because the source-structure adapter owns named-import offset semantics.
- Import binding collection should use `QinSourceStructure.importSpecifierMatches`. Do not flatten `ImportDeclaration.specifiers()` inside `QinImportBindings`, because source-structure owns import declaration/specifier pairing.
- IDEA semantic annotations for Java interop should reuse Qin PSI references and Java PSI resolution. For unresolved `java:` imports or static members, mark the Qin PSI element through an `Annotator`; do not duplicate a separate Java lookup model in the annotator.
- IDEA semantic highlighting for Java interop should reuse the same `QinJavaReference` resolution result. Highlight imported Java classes, static methods, and static fields from their resolved `PsiClass`, `PsiMethod`, or `PsiField`; do not run a second Java lookup path just for colors.
- Production Qin IDEA code may call `JavaPsiFacade` only inside `QinJavaReference`. Smoke tests should fail if annotators, inspections, semantic highlighters, completion contributors, or future Java interop surfaces query Java PSI directly instead of reusing the Qin Java `PsiReference` path.
- IDEA Find Usages and Rename for Java interop should flow through the same Qin `PsiReference`. If a Java class, static method, or static field is renamed, `handleElementRename` should update only the Qin reference token. Do not implement separate string-rewrite refactoring paths for Java interop.
- IDEA Go To Declaration tests for Java interop should exercise the editor path with `myFixture.getElementAtCaret()` for classes, static methods, and static fields. A reference-only `findReferenceAt().resolve()` assertion is not enough to prove Ctrl+Click/navigation targets the real Java PSI element.
- IDEA reference lookup may need a platform bridge from leaf tokens to structured Qin PSI wrappers. That bridge should delegate to `ReferenceProvidersRegistry` and the single Qin reference contributor; it must not add a second Java import/member resolver inside `PsiElement`, `PsiFile`, or an annotator.
- IDEA annotators and semantic highlighters should access platform references through the shared Qin reference helper, currently `QinPsiReferences`, instead of calling `ReferenceProvidersRegistry` directly in each consumer. The helper is the IntelliJ platform boundary; symbol meaning still belongs to the individual `PsiReference`.
- Production Qin IDEA code may call `ReferenceProvidersRegistry.getReferencesFromProviders` only inside `QinPsiReferences`. Smoke tests should fail if annotators, inspections, PSI wrappers, or future editor surfaces call the registry directly, because that would recreate separate reference lookup paths.
- Package smoke must require `QinPsiReferences.class` in the plugin jar, because it is the shared IntelliJ reference lookup boundary used by PSI files, PSI wrappers, annotators, inspections, semantic highlighting, navigation, and refactoring.
- Qin qualifier lookup such as `Counter.next` or `this.next` should use the shared `QinReferenceElements.previousQualifierName` helper. Do not hang generic Qin token facts from `QinJavaReference`, because Java interop is only one consumer of member-access syntax.
- Shared reference token checks such as `REFERENCE_IDENTIFIER` and `IMPORT_ALIAS_NAME` should live in `QinReferenceElements`. Consumers such as unresolved-reference messages and alias references must not own those token mappings.
- Qin-owned symbols should use IDEA named PSI elements plus `PsiReferenceContributor`, not LSP fake definitions. For example, `object Counter` should expose a named `OBJECT_NAME` PSI element, and `Counter.next()` should resolve through a Qin object reference so Go To Declaration, Find Usages, and Rename can use the IntelliJ Platform path.
- Cross-file Qin-owned symbols should use IntelliJ indexes/stubs to locate candidate files and then resolve to real named PSI elements in those files. For example, `import { Counter } from "./Counter.qin"` should resolve `Counter`, `Counter.value`, and `Counter.next()` to the `OBJECT_NAME`, `FIELD_NAME`, and `METHOD_NAME` PSI in `Counter.qin`; do not return virtual LSP definitions or source-string pseudo elements.
- Qin import aliases must preserve JavaScript/TypeScript import semantics during IDEA rename. The exported import specifier name, such as `Counter` in `import { Counter as C }`, should reference the remote `OBJECT_NAME` and update when that object is renamed. The local alias `C` and usages such as `C.next()` should remain local alias text during remote object rename.
- Java interop aliases follow the same rule. Renaming the Java `PsiClass` `demo.Greeter` should update `import { Greeter as G } from "java:demo"` to `import { Welcomer as G } from "java:demo"`, while `G.greet()` remains a local alias usage.
- The local alias inside an import specifier is a local declaration, not a remote reference. In `import { Greeter as G }`, `Greeter` may resolve to the Java `PsiClass`, but `G` inside the import specifier must not resolve to that class. The usage `G.greet()` resolves through the import table.
- Import alias declarations should expose named `IMPORT_ALIAS_NAME` PSI elements. Local alias usages should have a local alias reference so Rename and Find Usages on the alias declaration update `G.greet()` or `C.next()`, while the existing Java/Qin remote references keep Go To Declaration working for the real target.
- Local alias usages should not also be object/class reference candidates for the alias token itself. Semantic highlighting should prefer `QinImportAliasReference` for `C` or `G`, while member references such as `C.value` and `G.greet` still resolve the member through the imported Qin object or Java class.
- Find Usages for a remote Qin object or Java class should include the exported import specifier name even when it has a local alias, for example `Counter` in `import { Counter as C }` and `Greeter` in `import { Greeter as G }`. It should not treat the local alias declaration itself as a remote usage.
- Index and future StubIndex declaration extraction should share the same Qin token/AST adapter logic. Do not keep separate object/method/field scanners inside each IntelliJ index implementation; put declaration scanning behind one adapter so FileBasedIndex, StubIndex, PSI references, and completion consume the same language facts.
- `QinSourceStructure` is the current IDEA-side source-structure adapter for Qin object and member declarations. Stub builders and indexes should consume it instead of ad hoc declaration scanners. As the PSI parser moves closer to Qin parser/AST output, this adapter is the migration boundary rather than a competing semantic model.
- `QinSourceStructure` declarations should carry source ranges for object names, object bodies, and member names. Stub serialization may keep only stable names, but the adapter must preserve positions so future PSI construction and navigation can consume the same structure facts.
- IDEA declaration scanning, PSI building, indexes, references, completion, and highlighting should consume shared structure facts for Qin syntax boundaries such as object declarations, method declarations, field declarations, braces, and assignment operators. Do not duplicate those decisions in each consumer; move reusable checks into the shared token/structure adapter first.
- `QinParserDefinition` should use `QinSourceStructure` offsets for object declarations and object member declarations whenever those ranges are available. The parser still advances through `PsiBuilder` tokens for IntelliJ compatibility, but declaration identity should come from the shared source-structure adapter.
- Qin `OBJECT_DECLARATION` PSI ranges should follow `QinSourceStructure.ObjectDeclaration.bodyRange`, not a second ParserDefinition brace-depth scan. Object metadata between the object name and body, such as future `extends` or decorator-derived clauses, must not make ParserDefinition finish the object PSI before the shared structure body begins.
- Qin `METHOD_DECLARATION` PSI ranges should follow `QinSourceStructure.MemberDeclaration.bodyRange`. Method recognition may skip same-line signature metadata between parameters and `{`, but it must not cross a line terminator to steal the next member declaration body; the shared token-sequence method rule owns that boundary.
- `QinParserDefinition` should consume `QinSourceStructure.SourceRange` predicates such as `startsAfter` and `containsOffset` for object and method body bounds. Do not split `bodyRange().startOffset()` or `bodyRange().endOffset()` in ParserDefinition, because that recreates parser-owned range semantics at the IntelliJ adapter layer.
- `QinParserDefinition` should also use `QinSourceStructure` offsets for named import specifiers and aliases. `IMPORT_SPECIFIER` should begin at the exported import name, while `IMPORT_ALIAS_NAME` remains the local alias declaration inside that specifier.
- Named import structure in `QinSourceStructure` must be parsed inside the `{ ... }` import specifier boundary. Treat `as` and `from` as contextual keywords, consume `as Alias` as part of the current specifier, and stop the import declaration after its module string so newline-separated imports cannot be merged.
- `QinSourceStructure.ImportDeclaration` should carry the import declaration source range. `QinParserDefinition` should stop import PSI construction at that range instead of independently rediscovering `from`, string, semicolon, or newline boundaries.
- `QinParserDefinition` import parsing must use the same contextual keyword helper as `QinSourceStructure` for `from` and `as`. ParserDefinition must not maintain a stricter keyword-only rule that can diverge from the shared token stream and merge newline-separated import declarations.
- Import alias declaration lookup should use the alias source range from `QinSourceStructure.ImportSpecifier`, then bridge that offset back to the `IMPORT_ALIAS_NAME` PSI node. Do not find alias declarations by scanning the whole PSI tree or by matching raw text, because exported names and local aliases can share the same spelling.
- Import alias lookup by local name should use `QinSourceStructure.importAliasSpecifierNamed`. Do not match `ImportSpecifier.localName()` inside `QinImportBindings`; `QinImportBindings` should only bridge the selected alias range back to PSI through `QinPsiTree.importAliasNameElement`.
- Source-structure ranges should bridge back to PSI wrappers through shared PSI tree helpers such as `QinPsiTree.elementAtRangeOrParentOfType`. Keep range/offset-to-wrapper lookup in one platform adapter surface instead of reimplementing `findElementAt` plus parent walking in each resolver.
- `OBJECT_DECLARATION` PSI should bridge back to `QinSourceStructure.ObjectDeclaration` through `QinPsiTree.sourceObjectDeclaration`. Consumers such as `QinObjectSymbols` must not own the object-declaration keyword offset lookup.
- Containing Qin object declaration lookup should use `QinPsiTree.containingObjectDeclaration`. Consumers such as `QinObjectSymbols` must not own the `OBJECT_DECLARATION` token mapping for PSI ancestry walks.
- Qin object and member resolution should also bridge from `QinSourceStructure` name ranges back to `OBJECT_NAME`, `FIELD_NAME`, and `METHOD_NAME` PSI through `QinPsiTree.objectNameElement` and `QinPsiTree.objectMemberNameElement`. Consumers such as `QinObjectSymbols`, references, and completion must not descend PSI trees to rediscover object or member declarations with their own scans.
- Qin object member source ranges should bridge back to `FIELD_NAME` and `METHOD_NAME` PSI through `QinPsiTree.objectMemberNameElement`. Consumers such as `QinObjectSymbols` must not own the member-kind-to-token mapping.
- Qin object declaration lookup by name or keyword offset should use `QinSourceStructure.objectDeclarationNamed` and `QinSourceStructure.objectDeclarationAtKeywordOffset`. Do not iterate `objectDeclarations()` inside `QinObjectSymbols` to match names or keyword ranges; `QinObjectSymbols` should only bridge the selected structure range back to PSI.
- Qin object member declaration lookup by name should use `QinSourceStructure.ObjectDeclaration.memberDeclarationNamed` with `QinSourceStructure.ObjectMemberKind`. Do not match `MemberDeclaration.name()` or derive source-structure member kind from PSI token types inside `QinObjectSymbols`; `QinObjectSymbols` should only bridge the selected member range back to PSI.
- Qin object member collection should use `QinSourceStructure.ObjectDeclaration.memberDeclarations` and carry each member's `QinSourceStructure.ObjectMemberKind` through the PSI bridge. Do not flatten `fields()` and `methods()` or rederive member kind from bridged PSI tokens inside `QinObjectSymbols`; the source-structure adapter owns member order, kind, and duplicate-name handling before consumers bridge ranges back to PSI.
- Qin object member kind should use `QinSourceStructure.ObjectMemberKind`. Consumers such as `QinObjectSymbols` must not define local member-kind enums, because field-vs-method ownership belongs to the shared source-structure adapter.
- When the same Qin structure rule must run against both `PsiBuilder` raw tokens and `QinLexicalToken` lists, implement the rule once behind a shared token-sequence abstraction. Do not keep parallel builder-specific and scanner-specific copies of method/field declaration detection.
- IDEA `Lexer` and `ParserDefinition` are IntelliJ platform entry points, so they should not inherit from each other or duplicate language rules. `QinLexer` should wrap the shared scanner, while `QinParserDefinition` should consume `QinSourceStructure` source ranges for declaration identity and use `PsiBuilder` only to build the platform tree.
- StubIndex migration should be explicit and incremental: first register the Qin `StubIndexKey`/`StringStubIndexExtension`, then add file/object stub producers that feed that key, and only then retire the transitional FileBasedIndex. Once the file stub producer is in place, cross-file Qin object lookup must query `StubIndex`, not the older `FileBasedIndex`.
- Qin object member stub indexes should use object-qualified keys such as `Counter.value` and `Counter.next`, not bare member names. Bare member indexes would mix unrelated objects and recreate a separate semantic lookup layer outside PSI. Imported aliases such as `import { Counter as C }` must still query member indexes with the exported object name (`Counter.next`), then resolve to the real member PSI.
- StubIndex tests should prove both sides of that contract: object-qualified member keys locate the declaration file, while bare member keys such as `value` or `next` return no Qin files. Do not add bare member keys for convenience completion or unresolved-reference checks.
- Object member StubIndex key construction should use `QinSourceStructure.objectMemberKey`, and `QinFileElementType` should index through `QinSourceStructure.ObjectDeclaration.memberIndexEntries`. Do not let the file element type own object-qualified key syntax or flatten fields/methods itself.
- Object member StubIndex key selection should use the shared `QinObjectMemberStubIndexes.keyFor` helper. Consumers such as `QinFileElementType` and `QinObjectSymbols` must not duplicate the field-vs-method `StubIndexKey` mapping.
- Imported Qin object member completion should follow the same path as member references: locate the imported object file through `QinObjectNameStubIndex`, verify members through object-qualified field/method StubIndex keys, and then return the real `FIELD_NAME` or `METHOD_NAME` PSI element. Same-file and `this.` completion can stay PSI-local.
- Qin-owned object methods should follow the same platform path: method declarations expose named `METHOD_NAME` PSI elements, and member usages such as `Counter.next()` resolve the `next` token through a Qin object-method `PsiReferenceContributor`, not through LSP virtual definitions or source-text scans.
- Qin-owned object fields should also expose named `FIELD_NAME` PSI elements. Member usages such as `Counter.value` and `this.value` should resolve through a Qin object-field `PsiReferenceContributor` so Go To Declaration, Find Usages, and Rename stay on the IntelliJ Platform path.
- Qin `this.member` usages must be structured as `MEMBER_ACCESS` PSI with the member token wrapped as `REFERENCE_IDENTIFIER`. This keeps local field/method Go To Declaration, semantic highlighting, Find Usages, Rename, and completion on the same IDEA PSI/reference path instead of special-casing `this.` outside the parser.
- Qin object member references must distinguish calls from field access through shared PSI token facts. `Counter.missing()` is a method call and should be handled by the object-method reference/annotator, while `Counter.missing` is field access and should be handled by the object-field reference/annotator.
- IDEA native completion may be used only for PSI-owned Qin symbols whose declarations are already represented in the Qin PSI tree. For example, `Counter.` and `this.` can complete object `FIELD_NAME` and `METHOD_NAME` entries from same-file or imported object PSI. Do not use IDEA `CompletionContributor` as a broad fallback for TypeScript/Volar completions or Java member completions.
- Qin native completion tests should prove the negative boundary for Java qualifiers. In a file that imports `Greeter` from `java:demo`, completion at `Greeter.g` must not leak Qin object members such as `value` or `next`; Java and TypeScript-compatible member completion belongs to Java PSI/LSP surfaces, not `QinObjectMemberCompletionContributor`.
- Qin native completion tests should also prove the negative boundary for unknown or non-Qin qualifiers, and `this.` completion should be scoped to the containing Qin object only. `QinObjectMemberCompletionContributor` must not offer members from unrelated objects or act as a broad completion fallback when qualifier resolution does not find a Qin object.
- Qin-owned semantic annotations should reuse the same PSI references. For example, unresolved object methods such as `Counter.missing()` should be reported by resolving the Qin object-method reference, not by a separate object/member lookup inside the annotator.
- Qin-owned semantic highlighting should also reuse PSI nodes and PSI references. Declarations such as `OBJECT_NAME`, `METHOD_NAME`, and `FIELD_NAME`, plus resolved Qin object/member references, may receive IntelliJ text attributes through an `Annotator`; do not duplicate lexer/parser rules or create a second symbol model just for colors.
- Aliased import semantic highlighting should preserve the same remote/local split as navigation and rename. The exported name in `import { Counter as C }` or `import { Greeter as G }` should highlight as the remote Qin object or Java class reference, while the alias should highlight as a local import alias symbol/reference.
- Qin local inspections should be IDEA platform surfaces over the same PSI references and unresolved-reference messages used by annotators. Registering an Inspection must not introduce a second object, Java, or alias resolver; it should consume shared helpers such as `QinPsiReferences` and `QinUnresolvedReferenceMessages`.
- Java interop unresolved-reference inspections should be verified separately from annotator highlighting. Missing imported classes and missing static Java members must report through `QinUnresolvedReferenceInspection` with the same messages as `QinJavaInteropAnnotator`, proving both surfaces share `QinUnresolvedReferenceMessages`.
- Qin PSI is an IDEA adapter for Qin parser/AST and symbol model output. Do not grow a separate semantic model inside the IDEA plugin when the parser or shared Qin symbol model should own the language fact.
- IDEA plugin descriptor smoke must verify the native Qin platform entry points: parser definition, syntax highlighter, reference contributors, completion contributor, annotators, inspections, StubIndexes, typed completion trigger, and lookup Enter handler. Package smoke must also verify the corresponding implementation classes are present in the plugin jar, so missing registrations or missing packaged classes fail before a plugin zip is installed.
- IDEA Go To Declaration coverage should exercise the editor path with `myFixture.getElementAtCaret()` for critical references, not only `findReferenceAt().resolve()`. This includes exported import specifier names in aliased imports such as `import { Counter as C }` and `import { Greeter as G }`, because those tokens must navigate to the remote Qin `OBJECT_NAME` or Java `PsiClass` while the alias stays local.
- Qin-owned Go To Declaration coverage should include same-file object/member usages and `this.` member usages through `myFixture.getElementAtCaret()`, not only imported references. This proves the IDEA editor path reaches local `METHOD_NAME` and `FIELD_NAME` PSI nodes. Cover both `this.field` and `this.method()` so local object members stay on the same PSI/reference path as qualified object member access.
- Qin-owned Find Usages and Rename coverage should include both qualified object member usage such as `Counter.next()` and local `this.` usage such as `this.next()`. Method and field refactoring must flow through the same `PsiReference` implementations used by Go To Declaration, not through separate text replacement paths.

The IDEA lexer should be backed by a Qin/Slime/Subhuti token adapter rather than a separate hand-written Qin token model. Map parser token names into IDEA `IElementType` values at this boundary, then let `QinLexer` act only as the IntelliJ `LexerBase` state wrapper. Do not call a whole-file parser directly as a token-by-token lexer hot path.

Do not duplicate Qin/Slime lexical rules in the IDEA plugin. Token creation should use the same Slime/Subhuti token definitions as `QinParser` (`JavaScriptTokens.getTokens()`), and shared token classification such as keyword detection should use Slime token utilities instead of IDEA-local keyword lists. IDEA-owned code may map shared token facts into platform-specific `IElementType` categories, because that mapping is an IntelliJ presentation boundary rather than a language rule.

Base syntax highlighting tests should cover the `QinSyntaxHighlighter` mapping from shared Qin token types to IntelliJ `TextAttributesKey` values. Lexer token classification tests alone are not enough to prove that IDEA will color keywords, identifiers, literals, comments, delimiters, and operators correctly.

Treat import-only contextual keywords such as `as` and `from` as keyword tokens only inside import declarations. If the shared token utility reports them as keywords globally, normalize them back to identifiers first and then upgrade them in the import token-stream pass. Do not highlight ordinary identifiers such as `const from = value` as keywords.

The import token-stream pass must stop at a line terminator after an incomplete import when it is outside named-import braces, unless the current token is the import-closing `from`. This keeps the next statement's identifiers, such as `const as = "local"`, from being highlighted as import contextual keywords while still allowing multiline named imports inside `{ ... }`.

The lexer and `QinSourceStructure` must use the same import new-statement boundary helper for this rule. Do not keep a lexer-only newline stop and a separate source-structure import range scan, because ParserDefinition consumes `QinSourceStructure.ImportDeclaration` ranges to build import PSI nodes.

Keep IDEA token facts normalized behind one adapter surface. `QinLexer`, `QinParserDefinition`, declaration scanning, references, indexes, and completion may consume shared token facts such as trivia detection, reference-token detection, token slicing, next meaningful token lookup, qualifier lookup, and call-vs-field member boundaries. Do not copy those token facts into each consumer. Prefer composition through a shared token adapter/facts class over inheritance between platform adapter classes.

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
Generated TypeScript parser packages must not execute JVM-only Subhuti factory internals. `SubhutiParser.create(...)` is a JVM ByteBuddy enhancement entry point in Java source, but generated TypeScript parser methods already emit `executeRuleWrapper(...)` around `@SubhutiRule` bodies. When lowering `com.subhuti.parser.SubhutiParser.create(...)` for JS/TS output, emit the `@qin/java-sdk-js/tooling` helper `__qin_subhuti_parser_create(...)`, which constructs the parser from Qin class metadata. Do not leave the Java reflection path (`Class.forName("com.subhuti.aop.ByteBuddyParserFactory")`, cached `Method.invoke`) in generated TS and do not add a fallback raw parser path in tests.

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
- Java source `.d.ts` generation for LSP completion must expose both static methods and static fields through `QinSymbolModel`; do not add IDEA-side completion fallbacks for Java members that belong to Volar/TypeScript completion.

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
