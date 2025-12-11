import type { QinConfig } from "../../src/types.ts";

/**
 * Qin 全栈示例配置
 * Spring Boot 后端 + 原生前端
 */
const config: QinConfig = {
  // Java 入口文件
  entry: "src/server/Main.java",

  // Maven 依赖
  dependencies: [
    "org.springframework.boot:spring-boot-starter-web:3.2.0",
  ],

  // 输出配置
  output: {
    dir: "dist",
    jarName: "hello-app.jar",
  },

  // 前端配置
  frontend: {
    enabled: true,
    srcDir: "src/client",
    outDir: "dist/static",
    devPort: 5173,
  },

  // Maven 仓库配置（默认使用阿里云镜像）
  repositories: {
    useChinaMirror: true,
  },
};

export default config;
