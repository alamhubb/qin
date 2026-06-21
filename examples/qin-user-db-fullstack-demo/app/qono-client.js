export function createQonoClient(api) {
    const client = {}
    for (const name of Object.keys(api)) {
        const route = api[name]
        client[name] = input => requestRoute(route, input || {})
    }
    return client
}

async function requestRoute(route, input) {
    const method = route.method || "GET"
    const path = fillPath(route.path, input)
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
