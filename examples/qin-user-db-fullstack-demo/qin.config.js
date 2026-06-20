import vue from "@vitejs/plugin-vue"
import vitePluginOvs from "vite-plugin-ovs"

export default {
  name: "qin-user-db-fullstack-demo",
  version: "0.1.0",
  description: "Single-port Qin Vue OVS CSSTS user database demo",
  port: 19116,
  frontend: {
    srcDir: "app",
    entry: "app/main.vue",
    staticDir: "app"
  },
  backend: {
    sourceDir: "main",
    entry: "main/Main.java"
  },
  plugins: [
    ...vitePluginOvs({
      cssts: {
        classPrefix: "user-demo-"
      }
    }),
    vue()
  ],
  packageOverrides: {
    "slime-parser": "../ovs-cssts-demos/qin-ovs-cssts-generated-ts-slime-demo/packages/slime-parser",
    "@qin/java-sdk-js": "../ovs-cssts-demos/qin-ovs-cssts-generated-ts-slime-demo/packages/java-sdk-js",
    "subhuti": "../../../subhuti",
    "ovs-compiler": "../../../ovsjs/ovs/ovs-compiler",
    "cssts-compiler": "../ovs-cssts-demos/qin-ovs-cssts-generated-ts-slime-demo/packages/cssts-compiler"
  },
  dependencies: {
    "com.qin:qin-runtime-core": "0.1.0",
    "org.postgresql:postgresql": "42.7.7",
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
