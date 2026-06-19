import vue from "@vitejs/plugin-vue"
import vitePluginOvs from "vite-plugin-ovs"

export default {
  name: "qin-ovs-cssts-generated-ts-slime-demo",
  version: "0.1.0",
  description: "Qin-managed OVS/CSSTS demo with Java SlimeParser generated to TypeScript and loaded as a local package",
  port: 19115,
  frontend: {
    srcDir: "src",
    entry: "src/main.js",
    staticDir: "."
  },
  backend: {
    sourceDir: "main",
    entry: "main/parserBridge.ts"
  },
  packageOverrides: {
    "slime-parser": "./packages/slime-parser",
    "@qin/java-sdk-js": "./packages/java-sdk-js",
    "subhuti": "../../../../subhuti",
    "cssts-compiler": "./packages/cssts-compiler",
    "ovs-compiler": "../../../../ovsjs/ovs/ovs-compiler"
  },
  plugins: [
    ...vitePluginOvs({
      cssts: {
        classPrefix: "cmp-"
      }
    }),
    vue()
  ],
  dependencies: {
    "com.qin:qin-runtime-core": "0.1.0",
    "@vitejs/plugin-vue": "6.0.7",
    "@vue/compiler-sfc": "latest",
    "vue": "latest",
    "cssts-compiler": "0.2.87",
    "cssts-ts": "0.2.87",
    "vite-plugin-ovs": "0.2.2",
    "vite-plugin-cssts": "0.2.87",
    "ovs-compiler": "0.2.2",
    "ovsjs": "0.2.2"
  },
  java: {
    version: "21",
    sourceDir: "main",
    outputDir: "build/classes",
    encoding: "UTF-8"
  }
}
