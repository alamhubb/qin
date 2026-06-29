export default {
  name: "@qin/generated-qin-parser-ts",
  type: "library",
  entry: "./index.ts",
  generated: {
    source: "java",
    entryBinaryName: "com.qin.parser.QinParser",
    additionalEntryBinaryNames: ["com.slime.parser.cstToAst.SlimeCstToAstUtils", "com.slime.parser.cstToAst.SlimeAstCreateUtils"]
  }
}
