export default {
  name: "com.qin:subhuti-json",
  type: "library",
  version: "0.1.0",
  description: "Strict JSON parser and ESM module adapter built on Qin's generated Subhuti parser",
  entry: "src/java/com/qin/subhutijson/QinJsonModule.java",
  dependencies: {
    "com.qin:qin-parser": "0.1.0",
    "com.google.code.gson@gson": "2.10.1"
  },
  java: {
    version: "25",
    sourceDir: "src/java",
    outputDir: "build/classes",
    encoding: "UTF-8",
    release: "25",
    source: "25",
    target: "25"
  },
  output: {
    dir: "build",
    jarName: "subhuti-json.jar"
  }
}
