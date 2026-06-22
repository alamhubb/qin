import { Qono } from "java:com.qin.qono"
import { useQonoController } from "./qono-class"
import { UserController } from "./controllers/UserController"

export const app = useQonoController(
    Qono.create().health(),
    UserController
)
    .toHttpApp()

"qin-user-db-fullstack-demo"
