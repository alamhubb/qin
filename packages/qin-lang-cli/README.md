# qin-lang-cli

CLI package for Qin language.

## Scope

- Command entrypoints like `run`, `build`, and `pack`
- Coordinates frontend adapter, IR, backend, and runtime
- Hosts native-image integration flow

## Current POC Entry

`QinCompileMain` compiles the current stage-1 Qin subset into `jvm/js/both` targets:

```bash
java -cp "<cli>;<backend>;<frontend>;<ir>" com.qin.lang.cli.QinCompileMain --file test.js --target both --run
```

Current stage-1 focus:

- `console.log(<number>)`
- `console.log(<string>)`
- `console.log(<boolean>)`
- `console.log(null)`
- `const <id> = () => <expr>; console.log(<id>())`
- `const <id> = { <id>: <expr> }`
- `console.log(<id>.<id>)`

## Fullstack Demo Serve (JDK HttpServer)

`QinFullstackServeMain` builds and serves a demo workspace without Spring Boot:

- `shared + server -> .class`
- `shared + web -> app.js`
- serves static files and `/api/result` on `http://localhost:8080`
- static files prefer `<demoRoot>/app` (if present), otherwise `build/web`
- `/index` resolves to `index` or `index.html`

```bash
java -cp "<runtime_cp>" com.qin.lang.cli.QinFullstackServeMain --demo-root qin/examples/qin-fullstack-demo --port 8080
```

Build only mode:

```bash
java -cp "<runtime_cp>" com.qin.lang.cli.QinFullstackServeMain --demo-root qin/examples/qin-fullstack-demo --build-only
```
