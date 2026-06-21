import { RestController, RequestMapping, GetMapping, PostMapping, DeleteMapping } from "../qono-class.js"

@RestController
@RequestMapping("/api/users")
export class UserController {
    static basePath = "/api/users"

    @GetMapping("")
    static getAll() {
    }

    @PostMapping("")
    static create(input) {
    }

    @DeleteMapping("/{id}")
    static remove(input) {
    }
}
