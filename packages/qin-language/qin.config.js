export default {
  name: "qin-language",
  version: "0.1.0",
  description: "Qin language support and Volar language server",
  type: "library",
  entry: "qin-language-server/src/index.ts",
  scripts: {
    build: "npm run build",
    dev: "tsx qin-language-server/src/index.ts --stdio",
    test: "npm run test; if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }; npm run build; if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }; npm run test:lsp",
    "test:lsp": "npm run test:lsp"
  },
  dependencies: {
    "@volar/language-core": "^2.4.14",
    "@volar/language-server": "^2.4.14",
    "@volar/language-service": "^2.4.14",
    "@volar/typescript": "^2.4.14",
    "volar-service-typescript": "^0.0.62",
    "vscode-languageserver": "^9.0.1",
    "vscode-languageserver-textdocument": "^1.0.12",
    "vscode-uri": "^3.1.0",
    "typescript": "^5.8.3"
  },
  devDependencies: {
    "tsdown": "^0.20.0-beta.3",
    "tsx": "^4.19.2",
    "@types/node": "^22.15.21"
  },
  language: {
    id: "qin",
    extension: ".qin",
    server: "qin-language-server/src/index.ts",
    serverBundle: "dist/language-server.cjs",
    parser: "generated/qin-parser-ts",
    ideaLspClient: "../qin-idea-plugin-debug"
  },
  generated: {
    source: "java",
    entryBinaryName: "com.qin.parser.QinParser",
    sourceRoots: [
      "../qin-parser/src/java",
      "../../../slime/java-slime/slime-parser/src/main/java",
      "../../../slime/java-slime/slime-token/src/main/java",
      "../../../slime/java-slime/subhuti-java/src/main/java"
    ],
    outputDir: "generated/qin-parser-ts"
  },
  qinLanguage: {
    sourceExtension: ".qin",
    serviceExtension: ".ts",
    parserPackage: "com.qin:qin-parser",
    generatedParserTarget: "@qin/generated-qin-parser-ts"
  }
}
