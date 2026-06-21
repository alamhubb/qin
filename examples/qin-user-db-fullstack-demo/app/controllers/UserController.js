import { createQonoControllerClient } from "../qono-class.js"

const client = createQonoControllerClient("/api/users", {
    getAll: {
        method: "GET",
        path: ""
    },
    create: {
        method: "POST",
        path: ""
    },
    remove: {
        method: "DELETE",
        path: "/{id}"
    }
})

export class UserController {
    static getAll() {
        return client.getAll()
    }

    static create(input) {
        return client.create(input)
    }

    static remove(input) {
        return client.remove(input)
    }
}
