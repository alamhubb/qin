import { defineConfig } from "../../../src/types";

/**
 * Hello Java 应用配置
 * Spring Boot 后端 + Vite 前端
 */
export default defineConfig({
  name: "hello-java",

  // 后端端口
  port: 8080,

  // 依赖配置
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
    "java-base": "^0.0.1", // 本地包
  },

  // localRep: true,  // 使用项目本地 ./repository
  // localRep: false, // 默认，使用全局 ~/.qin/repository

  // 前端配置（内置 Vite，自动检测 src/client）
  client: {
    root: "src/client",    // 默认值，可省略
    port: 5173,            // 默认值，可省略
    // proxy 默认为 { "/api": "http://localhost:8080" }
  },
});
