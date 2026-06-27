# Qin Language

Qin-managed language support package for `.qin` files.

This package provides a Volar language server that maps Qin source into a
TypeScript service script so existing TypeScript language features can work in
editors. The project is managed by `qin.config.js`.

Current parser policy:

- Java parser ownership stays in `com.qin:qin-parser`.
- Volar runtime stays TypeScript/Node because editor LSP hosts run Node.
- The language server must not implement Qin syntax with string fallback
  transforms. Qin-only syntax such as `object` belongs in the generated Qin
  TypeScript parser that mirrors the Java `@SubhutiRule` grammar.
- `qin.config.js` records the intended generated parser target:
  `@qin/generated-qin-parser-ts`.
