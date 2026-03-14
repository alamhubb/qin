# qin-lang-runtime

Runtime library for Qin language programs.

## Scope

- Built-in APIs like JS global `console`, `Math`, `JSON`
- Runtime value model and host bindings
- Shared by interpreter and compiled outputs

## Current Built-ins

- `QinConsole.log(Object)`
- `QinMath.random/abs/floor/ceil/max/min`
- `QinJson.stringify/parse`
