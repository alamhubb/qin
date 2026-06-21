export const UserApi = {
    getAll: {
        method: "GET",
        path: "/api/users"
    },
    create: {
        method: "POST",
        path: "/api/users"
    },
    delete: {
        method: "DELETE",
        path: "/api/users/{id}"
    }
}
