import vue from "@vitejs/plugin-vue"

export default {
  name: "com.qin.smoke:vue3-minimal",
  version: "0.1.0",
  port: 19117,
  frontend: {
    srcDir: "src",
    entry: "src/main.js",
    staticDir: "."
  },
  backend: {
    entry: "main/Main.java"
  },
  plugins: [vue()],
  dependencies: {
    "com.qin:qin-runtime-core": "0.1.0",
    "@vitejs/plugin-vue": "6.0.7",
    "@vue/compiler-sfc": "latest",
    "vue": "latest"
  }
}
