# qin-lang-cli

CLI package for Qin language.

## Scope

- Command entrypoints like `run`, `build`, and `pack`
- Coordinates frontend adapter, IR, backend, and runtime
- Hosts native-image integration flow

## Current POC Entry

`QinCompileMain` compiles a tiny Qin subset into JVM `.class`:

```bash
java -cp "<cli>;<backend>;<frontend>;<ir>" com.qin.lang.cli.QinCompileMain --source "const a = { age: 1 }" --run
```

Supported grammar (POC):

- `const <id> = { <id>: <int> }`
