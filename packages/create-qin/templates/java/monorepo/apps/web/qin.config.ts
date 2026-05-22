import { defineConfig } from "qin";

/**
 * Web 应用 - Spring Boot + Vite 全栈
 */
export default defineConfig({
  name: "web",
  port: 8080,

  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "4.0.6",
    "java-common": "*",  // 引用本地包
  },

  client: {
    root: "src/client",
    port: 5173,
  },
});
