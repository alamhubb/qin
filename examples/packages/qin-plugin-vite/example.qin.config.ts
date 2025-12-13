/**
 * 示例：使用 qin-plugin-vite 的配置
 */
import { defineConfig } from "../../../src/types";
import vite from "./src";

export default defineConfig({
  name: "fullstack-app",
  version: "1.0.0",

  // Java 后端入口
  entry: "src/Main.java",

  // 使用 Vite 插件
  plugins: [
    vite({
      port: 5173,
      root: "client",
      proxy: {
        "/api": "http://localhost:8080",
      },
    }),
  ],

  // Java 依赖
  dependencies: {
    "com.sparkjava:spark-core": "2.9.4",
  },
});
