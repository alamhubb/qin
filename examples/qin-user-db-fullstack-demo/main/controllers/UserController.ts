import { RestController, RequestMapping, GetMapping, PostMapping, DeleteMapping } from "../qono-class"
import { Qono } from "java:com.qin.runtime.core.qono"
import { db, users } from "../db/schema"

@RestController
@RequestMapping("/api/users")
export class UserController {
    static basePath = "/api/users"

    @GetMapping("")
    static getAll(request) {
        return Qono.jsonRaw(db.selectJson("users", users, "id", "asc"))
    }

    @PostMapping("")
    static create(request) {
        return Qono.jsonRaw(201, db.insertJson("user", users, request.bodyText(), "name"))
    }

    @DeleteMapping("/{id}")
    static remove(request) {
        return Qono.jsonRaw(db.deleteByIdJson(users, request.param("id")))
    }
}
