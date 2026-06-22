import { defineComponent, h, nextTick } from "vue"
import { loading, error, config, report, rows, okCount, errorCount, refresh, statusClass, balanceText } from "./balanceState.js"
import { Alert, AppShell, Badge, Button, Card, DataTable, PageHeader, StatCard } from "./qin-ui.js"

const columns = [
    {
        key: "domain",
        label: "Domain",
        render: row => h("span", { class: "table-strong" }, row.domain || "-")
    },
    {
        key: "account",
        label: "Account",
        render: row => h("div", { class: "account-cell" }, [
            h("strong", row.name || "-"),
            h("span", row.baseUrl || "-")
        ])
    },
    {
        key: "keyPreview",
        label: "Key",
        render: row => h("span", { class: "mono" }, row.keyPreview || "-")
    },
    {
        key: "balance",
        label: "Balance",
        render: row => h("span", { class: "balance-cell" }, balanceText(row))
    },
    {
        key: "status",
        label: "Status",
        render: row => Badge({
            label: row.status || "-",
            tone: row.status === "ok" ? statusClass(row) : "destructive"
        })
    },
    {
        key: "path",
        label: "Path",
        render: row => h("span", { class: "mono" }, row.path || "-")
    },
    {
        key: "error",
        label: "Error",
        render: row => h("span", { class: "error-cell" }, row.error || "-")
    }
]

export const BalancePanel = defineComponent({
    name: "BalancePanel",
    setup() {
        nextTick(refresh)
        return () => AppShell([
            PageHeader({
                eyebrow: "QIN + OVS + QONO",
                title: "Balance Monitor",
                description: "xixiapi API key balance dashboard",
                action: Button({
                    label: loading.value ? "Refreshing..." : "Refresh",
                    disabled: loading.value,
                    onClick: refresh
                })
            }),
            h("section", { class: "summary-grid" }, [
                StatCard({ label: "Accounts", value: String(rows.value.length), description: "API key rows" }),
                StatCard({ label: "OK", value: String(okCount.value), description: "Healthy probes" }),
                StatCard({ label: "Errors", value: String(errorCount.value), description: "Needs attention" }),
                StatCard({ label: "Checked at", value: report.value.checkedAt || "-", description: "Last refresh" })
            ]),
            error.value ? Alert({ title: "Config needed", description: error.value }) : null,
            Card([
                h("div", { class: "card-heading" }, [
                    h("div", [
                        h("h2", "API key balances"),
                        h("p", config.value && config.value.configured
                            ? "Database configured"
                            : "Waiting for xixiapi database configuration")
                    ])
                ]),
                DataTable({
                    columns,
                    rows: rows.value.map(row => ({
                        key: row.id || row.keyPreview || row.domain,
                        value: row
                    })),
                    emptyText: loading.value ? "Loading..." : "No API key accounts"
                })
            ])
        ])
    }
})
