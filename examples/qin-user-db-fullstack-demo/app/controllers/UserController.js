import { RestController, RequestMapping, GetMapping, PostMapping, DeleteMapping, qonoCall } from "../qono-class.js"

export class UserController {
    static basePath = "/api/users"

    static getAll() {
        return qonoCall(UserController, "getAll")
    }

    static create(input) {
        return qonoCall(UserController, "create", input)
    }

    static remove(input) {
        return qonoCall(UserController, "remove", input)
    }
}

RestController(UserController)
RequestMapping("/api/users")(UserController)
GetMapping("")(UserController, "getAll", Object.getOwnPropertyDescriptor(UserController, "getAll"))
PostMapping("")(UserController, "create", Object.getOwnPropertyDescriptor(UserController, "create"))
DeleteMapping("/{id}")(UserController, "remove", Object.getOwnPropertyDescriptor(UserController, "remove"))
