export function createQonoRpcClient(controllerName, methods, options = {}) {
    const client = {}
    for (const methodName of Object.keys(methods || {})) {
        client[methodName] = input => qonoCall(controllerName, methodName, input, options)
    }
    return client
}

export async function qonoCall(controllerName, methodName, input = {}, options = {}) {
    const rpcBase = options.rpcBase || "/api/rpc"
    const rpcMethod = `${controllerName}.${methodName}`
    const response = await fetch(`${rpcBase}/${encodeURIComponent(rpcMethod)}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(input ?? {})
    })
    const text = await response.text()
    const payload = text ? JSON.parse(text) : {}
    if (!response.ok) {
        const detail = payload.detail ? `: ${payload.detail}` : ""
        throw new Error(`${payload.error || response.statusText}${detail}`)
    }
    return payload
}
