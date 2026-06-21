import { RestController, RequestMapping, GetMapping, PostMapping, DeleteMapping, qonoCall } from "../qono-class.js"

@RestController
@RequestMapping("/api/users")
export class UserController {
    static basePath = "/api/users"

    @GetMapping("")
    static getAll() {
        return qonoCall(UserController, "getAll")
    }

    @PostMapping("")
    static create(input) {
        return qonoCall(UserController, "create", input)
    }

    @DeleteMapping("/{id}")
    static remove(input) {
        return qonoCall(UserController, "remove", input)
    }
}
