import { QinWeb } from "java:com.qin.web"
import { useQinWebController } from "./qin-web-class"
import { UserController } from "./controllers/UserController"

export const app = useQinWebController(
    QinWeb.create().health(),
    UserController
)
    .toHttpApp()

"qin-user-db-fullstack-demo"
