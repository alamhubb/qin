export function createQonoControllerClient(basePath, routes) {
    const client = {}
    for (const name of Object.keys(routes)) {
        const route = routes[name]
        client[name] = input => requestRoute(basePath, route, input || {})
    }
    return client
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
