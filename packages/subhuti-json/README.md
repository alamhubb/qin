# subhuti-json

Strict JSON module support for Qin's JVM module graph.

## Parser pipeline

`grammar/com/qin/subhutijson/SubhutiJsonGrammarSource.java` authors the JSON
document, value, object, member, and array rules with Subhuti annotations and
its compile-only grammar DSL. The standard `qin-parser` build sends this source
through the shared generator chain:

```text
Subhuti author grammar
  -> AuthoringCompiler
  -> GrammarIr
  -> JavaParserSourceGenerator
  -> generated Chevrotain Java parser
  -> SubhutiCst
```

The JSON grammar adds no JSON-specific branch to the compiler or generator.
The generated `SubhutiJsonParser` validates source before the adapter parses
JSON values and exposes them as an ESM default export. Valid top-level
identifier keys are also exposed as named exports for package metadata such as
`package.json`'s `version` field.

## Qin integration

The ESM resolver recognizes `.json` module files. `QinModuleGraphBuilder`
parses them through `QinJsonModule` and places the generated ESM source into
the ordinary module graph, so later linking and JVM compilation use the same
module path as JavaScript and TypeScript imports.

The parser rejects comments, trailing commas, single-quoted strings, malformed
values, and trailing source after the document.

The end-to-end import example is in `examples/qin-json-module`. Run it with
`qin run` from that directory to exercise parsing, ESM linking, JVM compilation,
and execution.
