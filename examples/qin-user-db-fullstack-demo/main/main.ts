import { QinHttpApp } from "java:com.qin.runtime.core"
import { UserDb } from "java:com.qin.demo.userdb"

export const app = QinHttpApp.create()
    .get("/api/users", request => UserDb.listUsers(request))
    .post("/api/users", request => UserDb.createUser(request))
    .delete("/api/users/{id}", request => UserDb.deleteUser(request))

"qin-user-db-fullstack-demo"
