# Qin Builtins Strategy

This document defines how Qin should support JS/ES-style built-in objects
without turning into a Node runtime.

It exists to separate three concepts that are easy to confuse:

- ESM module syntax
- JavaScript language built-ins
- Qin's own standard/builtin surface

## 1. Core Clarification

Supporting ESM does not automatically mean builtin objects already exist.

For example:

- `import` / `export` belong to the module system
- `Array`, `Map`, `Set`, `JSON`, `Math`, `Object` belong to language/runtime built-ins

So Qin must define builtins explicitly.

## 2. Product Direction

Qin should support a JS/ES-style builtin layer, but:

- Qin is not required to replicate the full JavaScript runtime
- Qin is not required to match Node or V8 edge cases
- Qin builtins should be defined by Qin itself

The long-term model is:

- Qin syntax and module model may look ESM-like
- Qin builtin behavior is Qin-defined
- Qin builtin implementation on backend is Java-backed

## 3. Architecture Model

Builtin support should follow this chain:

- Qin source
- Qin frontend / IR
- builtin lowering
- Java-backed runtime implementation
- JVM `.class`

Not this chain:

- Qin source
- pretend Node runtime
- late Java adaptation

## 4. Design Rule

Builtin objects should be treated as a dedicated language/runtime layer.

That means:

- compiler recognizes builtin usage explicitly
- runtime exposes builtin behavior explicitly
- behavior is tested as Qin behavior, not as accidental Java behavior

## 5. Phase-1 Builtins

The first meaningful builtin set should be:

- `console`
- `Math`
- `JSON`
- `Array`
- `Map`
- `Set`
- `Object`
- `Number`
- `Boolean`
- `String` basic shape
- `Date.now`

These are enough to make backend/business code much more natural.

## 6. Phase-1 Scope

### Included now

- `console.log`
- numeric math helpers
- JSON stringify / parse
- array literals
- array basic method subset
- map basic method subset
- set basic method subset
- object key/value/entry helpers
- number parsing / number predicates

### Explicitly not required now

- full ECMAScript parity
- full prototype-chain semantics
- sparse-array semantics
- property descriptors
- `Proxy`
- `Reflect` full surface
- full `Date` compatibility
- `Promise` / async runtime
- Node runtime objects

## 7. Implementation Policy

Qin should not expose raw Java collections/classes as the language definition.

Instead:

- frontend/compiler should preserve Qin builtin intent
- runtime may internally use Java collections and Java standard library
- user-facing behavior should be described as Qin builtin behavior

This allows:

- stable language semantics
- JVM-backed implementation
- future frontend/backend consistency

## 8. Collection Builtins Policy

### Array

Phase-1 array support should focus on:

- `[]`
- index read/write
- `length`
- `push`
- `pop`
- `map`
- `forEach`
- `filter`
- `join`
- `includes`
- `indexOf`
- `find`
- `some`
- `every`

No promise yet for:

- full prototype parity
- sparse-array exactness
- every standard method

### String

Phase-1 string support should focus on:

- `length`
- `includes`
- `startsWith`
- `endsWith`
- `trim`
- `toUpperCase`
- `toLowerCase`
- `slice`
- `substring`
- `split`
- `charAt`

### Map

Phase-1 map support should focus on:

- `new Map()`
- `set`
- `get`
- `has`
- `delete`
- `clear`
- `size`

### Set

Phase-1 set support should focus on:

- `new Set()`
- `add`
- `has`
- `delete`
- `clear`
- `size`

## 9. JSON Policy

Phase-1 JSON support should focus on:

- `JSON.stringify`
- `JSON.parse`

Supported JSON shapes should cover:

- object
- array
- string
- number
- boolean
- `null`

## 10. Testing Rule

Builtin support must be validated as Qin behavior.

That means dedicated smoke/parity tests for:

- `Math`
- `JSON`
- `Array`
- `Map`
- `Set`
- `Object`

The success criterion is:

- Qin code behaves according to Qin's documented builtin subset

The success criterion is not:

- full Node/V8 behavioral identity

## 11. Practical Summary

The intended backend builtin strategy is:

- ESM syntax for module authoring
- Qin-defined builtin layer for language/runtime objects
- Java-backed implementation
- JVM `.class` as backend target

This keeps Qin:

- independent from Node
- expressive enough for JS-like development style
- still rooted in JVM backend architecture

## 12. ESM Capability Boundary

If Qin supports ESM-style syntax, what do we still need to build explicitly?

Answer: a lot of language/runtime behavior still needs explicit design.

What ESM-style syntax gives directly:

- `import` / `export`
- module-shaped source organization
- familiar authoring style

What it does not automatically give:

- builtin objects already behaving like JavaScript
- prototype semantics
- host APIs
- async runtime semantics
- Node package/runtime compatibility

So if we ask whether Qin "loses native ESM abilities", the precise answer is:

- Qin keeps ESM's source-level module model where we implement it
- Qin does not inherit the full JavaScript runtime just because the syntax is ESM-like

In other words:

- module syntax can be ESM-inspired
- builtin/runtime behavior must still be designed and implemented as Qin behavior
