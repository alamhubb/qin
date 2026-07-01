import { QinWeb } from "java:com.qin.web"

export const app = QinWeb.create()
  .get("/", request => QinWeb.text("hello"))
  .toHttpApp()

"qin-web-hello"
