import { defineConfig } from "qin";

export default defineConfig({
  name: "web",
  port: 8080,

  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
    "common": "*",
  },

  client: {
    root: "src/client",
    port: 5173,
  },
});
