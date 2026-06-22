import { ref, computed } from "vue"
import okBadge from "./okBadge.cssts"
import errorBadge from "./errorBadge.cssts"

export const loading = ref(false)
export const error = ref("")
export const config = ref(null)
export const report = ref({ configured: false, checkedAt: "", accounts: [] })

export const rows = computed(() => report.value.accounts || [])
export const okCount = computed(() => rows.value.filter(row => row.status === "ok").length)
export const errorCount = computed(() => rows.value.filter(row => row.status !== "ok").length)

export async function refresh() {
    loading.value = true
    error.value = ""
    try {
        config.value = await requestJson("/api/config")
        report.value = await requestJson("/api/balances")
        if (report.value.error) {
            error.value = report.value.error
        }
    } catch (err) {
        error.value = err.message
    } finally {
        loading.value = false
    }
}

export function statusClass(row) {
    return row.status === "ok" ? okBadge : errorBadge
}

export function balanceText(row) {
    return row.balance ? row.balance + " " + (row.currency || "") : "-"
}

async function requestJson(url) {
    const response = await fetch(url)
    const text = await response.text()
    const payload = text ? JSON.parse(text) : {}
    if (!response.ok) {
        throw new Error(payload.detail || payload.error || response.statusText)
    }
    return payload
}
