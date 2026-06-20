export function createQonoClient(basePath = "/api/rpc") {
    return {
        async call(method, input = {}) {
            const response = await fetch(`${basePath}/${encodeURIComponent(method)}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(input)
            })
            const text = await response.text()
            const payload = text ? JSON.parse(text) : {}
            if (!response.ok) {
                const detail = payload.detail ? `: ${payload.detail}` : ""
                throw new Error(`${payload.error || response.statusText}${detail}`)
            }
            return payload
        }
    }
}
