# Qin IDEA LSP Integration

The IDEA plugin registers `.qin`, `.ovs`, and `.cssts` as pure LSP-backed file
types. IDEA does not implement Qin, OVS, or CSSTS syntax locally. It starts the
existing Volar language servers and shows diagnostics, completion, and semantic
tokens returned by those servers.

Language server bundles are resolved from each language project's
`qin.config.js` `language.serverBundle` field in the current `qinall`
workspace:

- `qin/packages/qin-language/dist/language-server.cjs`
- `ovsjs/ovs-language/dist/language-server.js`
- `cssts/cssts-language/dist/language-server.cjs`

Runtime environment:

- `QIN_LSP_NODE`: optional Node executable path. Defaults to `node.exe` on
  Windows and `node` elsewhere.
- `QIN_LSP_TYPESCRIPT_TSDK`: optional TypeScript SDK path. If it is not set, the
  plugin resolves TypeScript from the workspace `node_modules`.

Node is only used for the Volar/LSP editor process. Qin syntax diagnostics still
come from generated QinParser TypeScript, which is generated from the Java parser.
