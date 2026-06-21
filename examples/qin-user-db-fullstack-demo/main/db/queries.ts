import { Qono } from "java:com.qin.runtime.core.qono"
import { db, users } from "./schema"

export function listUsers() {
    return Qono.jsonRaw(db.selectJson("users", users, "id", "asc"))
}

export function createUser(request) {
    return Qono.jsonRaw(201, db.insertJson("user", users, request.bodyText(), "name,email"))
}

export function deleteUser(request) {
    return Qono.jsonRaw(db.deleteByIdJson(users, request.param("id")))
}
