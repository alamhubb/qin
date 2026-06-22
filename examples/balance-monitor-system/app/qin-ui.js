import { h } from "vue"

function mergeClass(...values) {
    return values.filter(Boolean).join(" ")
}

export function AppShell(children) {
    return h("div", { class: "admin-shell" }, [
        h("aside", { class: "admin-sidebar" }, [
            h("div", { class: "brand-block" }, [
                h("div", { class: "brand-mark" }, "Q"),
                h("div", [
                    h("strong", "Qin UI"),
                    h("span", "Balance admin")
                ])
            ]),
            h("nav", { class: "admin-nav", "aria-label": "Main navigation" }, [
                h("a", { class: "nav-item active", href: "#" }, "Dashboard"),
                h("a", { class: "nav-item", href: "#" }, "Accounts"),
                h("a", { class: "nav-item", href: "#" }, "Providers"),
                h("a", { class: "nav-item", href: "#" }, "Settings")
            ])
        ]),
        h("div", { class: "admin-main" }, children)
    ])
}

export function PageHeader({ eyebrow, title, description, action }) {
    return h("header", { class: "page-header" }, [
        h("div", [
            h("p", { class: "eyebrow" }, eyebrow),
            h("h1", title),
            h("p", { class: "subtitle" }, description)
        ]),
        action ? h("div", { class: "page-actions" }, action) : null
    ])
}

export function Button({ label, disabled = false, onClick, variant = "default" }) {
    return h("button", {
        type: "button",
        class: mergeClass("ui-button", variant === "outline" && "outline"),
        disabled,
        onClick
    }, label)
}

export function Card(children, props = {}) {
    return h("section", { class: mergeClass("ui-card", props.class) }, children)
}

export function StatCard({ label, value, description }) {
    return Card([
        h("div", { class: "stat-label" }, label),
        h("strong", { class: "stat-value" }, value),
        description ? h("p", { class: "stat-description" }, description) : null
    ], { class: "stat-card" })
}

export function Alert({ title, description }) {
    return h("div", { class: "ui-alert", role: "alert" }, [
        h("strong", title),
        h("span", description)
    ])
}

export function Badge({ label, tone = "default" }) {
    return h("span", { class: mergeClass("ui-badge", tone) }, label)
}

export function DataTable({ columns, rows, emptyText }) {
    return h("div", { class: "table-wrap" }, [
        h("table", { class: "ui-table" }, [
            h("thead", [
                h("tr", columns.map(column => h("th", { key: column.key }, column.label)))
            ]),
            h("tbody", rows.length > 0
                ? rows.map(row => h("tr", { key: row.key }, columns.map(column => {
                    const content = column.render ? column.render(row.value) : row.value[column.key]
                    return h("td", { key: column.key }, content)
                })))
                : [h("tr", [h("td", { colspan: String(columns.length), class: "empty" }, emptyText)])])
        ])
    ])
}
