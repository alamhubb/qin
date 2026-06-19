import vue from "@vitejs/plugin-vue"
import vitePluginOvs from "vite-plugin-ovs"

export default {
  name: "qin-ovs-cssts-java-slime-demo",
  version: "0.1.0",
  description: "Qin-managed OVS/CSSTS demo with TS parser classes compiled to JVM classes extending Java SlimeParser",
  port: 19112,
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
    "cssts-compiler": "../../../../cssts/cssts/cssts-compiler",
    "cssts-ts": "../../../../cssts/cssts/cssts-runtime",
    "vite-plugin-cssts": "../../../../cssts/vite-plugin-cssts",
    "ovs-compiler": "../../../../ovsjs/ovs/ovs-compiler",
    "ovsjs": "../../../../ovsjs/ovs/ovs-runtime",
    "vite-plugin-ovs": "../../../../ovsjs/vite-plugin-ovs"
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
