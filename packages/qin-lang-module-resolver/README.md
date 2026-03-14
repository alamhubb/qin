# qin-lang-module-resolver

ESM module-resolution layer for Qin source files.

## Responsibility

- Resolve local `.js` module specifiers.
- Resolve npm-style bare specifiers from `node_modules`.
  - package entry priority: `exports.import` -> `exports["."]` -> `module` -> `main` -> `index.js`
- Build a module graph from an entry file.
- Keep legal cyclic dependencies in graph (ESM-compatible).
- Emit linked source for current Qin frontend adapter flow.

## Out of Scope

- Import policy checks (handled by `qin-lang-module-policy`).
- ESM semantic checks (handled by `qin-lang-sema-esm`).
- IR lowering or backend emission.
