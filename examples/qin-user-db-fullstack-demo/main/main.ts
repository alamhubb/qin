import { Qono } from "java:com.qin.runtime.core.qono"
import { UserApi } from "../app/api/users-api.js"
import { getAll, create, remove } from "./controllers/UserController"

export const app = Qono.create()
    .health()
    .route(UserApi.getAll.method, UserApi.getAll.path, request => getAll(request))
    .route(UserApi.create.method, UserApi.create.path, request => create(request))
    .route(UserApi.delete.method, UserApi.delete.path, request => remove(request))
    .toHttpApp()

"qin-user-db-fullstack-demo"
