import { Qono } from "java:com.qin.runtime.core.qono"
import { getAll, create, remove } from "./controllers/UserController"

export const app = Qono.create()
    .health()
    .query("users.getAll", request => getAll(request))
    .mutation("users.create", request => create(request))
    .mutation("users.delete", request => remove(request))
    .toHttpApp()

"qin-user-db-fullstack-demo"
