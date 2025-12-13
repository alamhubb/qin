import { defineConfig } from "qin";

export default defineConfig({
  name: "{{name}}",
  port: 8080,

  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
  },

  client: {
    root: "src/client",
    port: 5173,
  },
});
