export default {
  name: "qin-java-source-lsp-demo",
  version: "1.0.0",
  type: "app",
  entry: "src/main/App.qin",
  java: {
    version: "21",
    sourceDir: "src/main",
    outputDir: "build/classes"
  }
}
