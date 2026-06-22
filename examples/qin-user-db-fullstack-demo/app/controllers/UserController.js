import { createQonoRpcClient } from "../qono-rpc.js"

export const UserController = createQonoRpcClient("UserController", {
    getAll: { type: "query" },
    create: { type: "mutation" },
    remove: { type: "mutation" }
})
