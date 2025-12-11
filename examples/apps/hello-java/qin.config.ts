import type { QinConfig } from "../../../src/types.ts";

/**
 * Hello Java 应用配置
 * Spring Boot 后端 + 原生前端
 */
const config: QinConfig = {
  name: "hello-java",

  // 依赖配置
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
    "java-base": "^0.0.1",  // 本地包
  },
};

export default config;
