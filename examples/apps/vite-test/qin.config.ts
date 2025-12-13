/**
 * 测试 qin-plugin-vite 插件
 */
import { defineConfig } from "../../../src/types";
import vite from "../../packages/qin-plugin-vite/src";

export default defineConfig({
  name: "vite-test",
  version: "1.0.0",

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
});
