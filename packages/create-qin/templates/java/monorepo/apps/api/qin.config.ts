import { defineConfig } from "qin";

export default defineConfig({
  name: "api",
  port: 8080,

  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
    "common": "*",  // 本地包
  },
});
