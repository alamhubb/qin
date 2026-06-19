export default {
  name: "@qin/generated-slime-parser-ts",
  type: "library",
  entry: "./index.ts",
  generated: {
    source: "java",
    entryBinaryName: "com.slime.parser.SlimeParser"
  }
}
