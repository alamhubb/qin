export function RestController(target) {
    target.__qonoController = true
}

export function RequestMapping(path) {
    return target => {
        target.basePath = path
    }
}

export function GetMapping(path) {
    return route("GET", path)
}

export function PostMapping(path) {
    return route("POST", path)
}

export function DeleteMapping(path) {
    return route("DELETE", path)
}

function route(method, path) {
    return (target, propertyKey, descriptor) => {
        const original = descriptor.value
        descriptor.value = function (input = {}) {
            return requestRoute(target.basePath || "", { method, path }, input)
        }
        descriptor.value.original = original
        return descriptor
    }
}

async function requestRoute(basePath, route, input) {
    const method = route.method || "GET"
    const path = fillPath(joinPath(basePath, route.path || ""), input)
    const options = { method, headers: { "Content-Type": "application/json" } }
    if (method !== "GET" && method !== "DELETE") {
        options.body = JSON.stringify(input)
    }
    const response = await fetch(path, options)
    const text = await response.text()
    const payload = text ? JSON.parse(text) : {}
    if (!response.ok) {
        const detail = payload.detail ? `: ${payload.detail}` : ""
        throw new Error(`${payload.error || response.statusText}${detail}`)
    }
    return payload
}

function fillPath(path, input) {
    return path.replace(/\{([A-Za-z_][A-Za-z0-9_]*)\}/g, (_, key) => {
        const value = input[key]
        if (value === undefined || value === null || value === "") {
            throw new Error(`Missing route parameter: ${key}`)
        }
        return encodeURIComponent(String(value))
    })
}

function joinPath(basePath, path) {
    const base = normalizePath(basePath)
    const child = normalizePath(path)
    if (child === "/") {
        return base
    }
    if (base === "/") {
        return child
    }
    return `${base}${child}`
}

function normalizePath(path) {
    const value = path || ""
    if (value === "") {
        return "/"
    }
    return value.startsWith("/") ? value : `/${value}`
}
