import { RestController, RequestMapping, GetMapping, PostMapping, DeleteMapping } from "../qono-class"
import { listUsers, createUser, deleteUser } from "../db/queries"

@RestController
@RequestMapping("/api/users")
export class UserController {
    static basePath = "/api/users"

    @GetMapping("")
    static getAll(request) {
        return listUsers(request)
    }

    @PostMapping("")
    static create(request) {
        return createUser(request)
    }

    @DeleteMapping("/{id}")
    static remove(request) {
        return deleteUser(request)
    }
}
