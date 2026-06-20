import { listUsers, createUser, deleteUser } from "../db/queries"

export function getAll(request) {
    return listUsers(request)
}

export function create(request) {
    return createUser(request)
}

export function remove(request) {
    return deleteUser(request)
}
