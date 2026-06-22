import { defineComponent, h, nextTick } from "vue"
import { loading, error, config, report, rows, okCount, errorCount, refresh, statusClass, balanceText } from "./balanceState.js"

function cell(content, props = {}) {
    return h("td", props, content)
}

function metric(label, value, wide = false) {
    return h("div", { class: wide ? "metric wide" : "metric" }, [
        h("span", label),
        h("strong", value)
    ])
}

function renderRows() {
    if (rows.value.length === 0) {
        return [
            h("tr", [
                cell(loading.value ? "Loading..." : "No API key accounts", {
                    colspan: "7",
                    class: "empty"
                })
            ])
        ]
    }

    return rows.value.map(row => h("tr", { key: row.id || row.keyPreview || row.domain }, [
        cell(row.domain || "-"),
        cell(h("div", { class: "account-cell" }, [
            h("strong", row.name || "-"),
            h("span", row.baseUrl || "-")
        ])),
        cell(row.keyPreview || "-", { class: "mono" }),
        cell(balanceText(row), { class: "balance-cell" }),
        cell(h("span", { class: "status-badge " + statusClass(row) }, row.status || "-")),
        cell(row.path || "-", { class: "mono" }),
        cell(row.error || "-", { class: "error-cell" })
    ]))
}

export const BalancePanel = defineComponent({
    name: "BalancePanel",
    setup() {
        nextTick(refresh)
        return () => h("main", [
            h("section", { class: "summary-grid" }, [
                metric("Accounts", String(rows.value.length)),
                metric("OK", String(okCount.value)),
                metric("Errors", String(errorCount.value)),
                metric("Checked at", report.value.checkedAt || "-", true)
            ]),
            error.value ? h("div", { class: "notice" }, [
                h("strong", "Config needed"),
                h("span", error.value)
            ]) : null,
            h("section", { class: "panel" }, [
                h("div", { class: "panel-heading" }, [
                    h("div", [
                        h("h2", "API key balances"),
                        h("p", config.value && config.value.configured
                            ? "Database configured"
                            : "Waiting for xixiapi database configuration")
                    ]),
                    h("button", {
                        type: "button",
                        class: "refresh-button",
                        disabled: loading.value,
                        onClick: refresh
                    }, loading.value ? "Refreshing..." : "Refresh")
                ]),
                h("div", { class: "table-wrap" }, [
                    h("table", [
                        h("thead", [
                            h("tr", [
                                h("th", "Domain"),
                                h("th", "Account"),
                                h("th", "Key"),
                                h("th", "Balance"),
                                h("th", "Status"),
                                h("th", "Path"),
                                h("th", "Error")
                            ])
                        ]),
                        h("tbody", renderRows())
                    ])
                ])
            ])
        ])
    }
})
