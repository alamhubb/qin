import { createApp } from "vue"
import UserRuntimeBadge from "./UserRuntimeBadge.ovs"
import connectedStyle from "./tokens.cssts"
import { UserApi } from "./api/users-api.js"
import { createQonoClient } from "./qono-client.js"
import "./style.css"

const runtimeBadge = document.querySelector("#runtime-badge")
if (runtimeBadge) {
    createApp(UserRuntimeBadge).mount(runtimeBadge)
}

const statusEl = document.querySelector("#status")
const healthDot = document.querySelector("#health-dot")
const usersEl = document.querySelector("#users")
const emptyEl = document.querySelector("#empty")
const form = document.querySelector("#user-form")
const refresh = document.querySelector("#refresh")
const dbReady = document.querySelector("#db-ready")
const UserController = createQonoClient(UserApi)

if (dbReady) {
    dbReady.className = connectedStyle
}

async function requestJson(url, options = {}) {
    const response = await fetch(url, {
        headers: { "Content-Type": "application/json" },
        ...options
    })
    const text = await response.text()
    const payload = text ? JSON.parse(text) : {}
    if (!response.ok) {
        const detail = payload.detail ? `: ${payload.detail}` : ""
        throw new Error(`${payload.error || response.statusText}${detail}`)
    }
    return payload
}

async function checkHealth() {
    const payload = await requestJson("/api/health")
    healthDot.classList.toggle("ok", Boolean(payload.ok))
    dbReady.hidden = !payload.ok
}

async function loadUsers() {
    try {
        statusEl.textContent = "Loading users..."
        await checkHealth()
        const payload = await UserController.getAll()
        renderUsers(payload.users || [])
        statusEl.textContent = "Connected"
    } catch (error) {
        healthDot.classList.remove("ok")
        dbReady.hidden = true
        usersEl.innerHTML = ""
        emptyEl.hidden = false
        emptyEl.textContent = error.message
        statusEl.textContent = "Needs database configuration"
    }
}

function renderUsers(users) {
    usersEl.innerHTML = ""
    emptyEl.hidden = users.length > 0
    emptyEl.textContent = "No users yet."
    for (const user of users) {
        const row = document.createElement("tr")
        row.innerHTML = `
            <td>${escapeHtml(user.id)}</td>
            <td>${escapeHtml(user.name)}</td>
            <td>${escapeHtml(user.email)}</td>
            <td>${escapeHtml(user.createdAt)}</td>
            <td><button class="danger-button" type="button" data-delete="${escapeHtml(user.id)}">Delete</button></td>
        `
        usersEl.append(row)
    }
}

form.addEventListener("submit", async (event) => {
    event.preventDefault()
    const data = new FormData(form)
    try {
        statusEl.textContent = "Adding user..."
        await UserController.create({
            name: data.get("name"),
            email: data.get("email")
        })
        form.reset()
        await loadUsers()
    } catch (error) {
        statusEl.textContent = error.message
    }
})

usersEl.addEventListener("click", async (event) => {
    const button = event.target.closest("[data-delete]")
    if (!button) {
        return
    }
    try {
        statusEl.textContent = "Deleting user..."
        await UserController.delete({ id: button.dataset.delete })
        await loadUsers()
    } catch (error) {
        statusEl.textContent = error.message
    }
})

refresh.addEventListener("click", loadUsers)

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
}

loadUsers()
