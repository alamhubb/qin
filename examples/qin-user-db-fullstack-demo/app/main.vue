<script setup>
import { computed, onMounted, ref } from 'vue'
import CsstsBadge from './CsstsBadge.vue'
import UserSummary from './UserSummary.ovs'
import './style.css'

const users = ref([])
const name = ref('')
const email = ref('')
const loading = ref(false)
const saving = ref(false)
const error = ref('')
const status = ref('')

const userCount = computed(() => users.value.length)

async function requestJson(url, options = {}) {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options
  })
  if (!response.ok) {
    const text = await response.text()
    throw new Error(text || `HTTP ${response.status}`)
  }
  if (response.status === 204) return null
  return response.json()
}

async function loadUsers() {
  loading.value = true
  error.value = ''
  try {
    users.value = await requestJson('/api/users')
  } catch (err) {
    error.value = String(err.message || err)
  } finally {
    loading.value = false
  }
}

async function addUser() {
  saving.value = true
  error.value = ''
  status.value = ''
  try {
    const created = await requestJson('/api/users', {
      method: 'POST',
      body: JSON.stringify({ name: name.value, email: email.value })
    })
    users.value = [...users.value, created]
    name.value = ''
    email.value = ''
    status.value = 'User saved to remote PostgreSQL.'
  } catch (err) {
    error.value = String(err.message || err)
  } finally {
    saving.value = false
  }
}

async function deleteUser(id) {
  error.value = ''
  status.value = ''
  try {
    await requestJson(`/api/users/${id}`, { method: 'DELETE' })
    users.value = users.value.filter(user => user.id !== id)
    status.value = 'User deleted.'
  } catch (err) {
    error.value = String(err.message || err)
  }
}

onMounted(loadUsers)
</script>

<template>
  <main class="page">
    <header class="header">
      <p class="muted">Qin single-port fullstack</p>
      <h1>User Database Demo</h1>
      <p class="muted">Vue + OVS + CSSTS and Qin HTTP API share this port. Data is stored in remote PostgreSQL.</p>
      <p class="muted">Current users: {{ userCount }}</p>
      <CsstsBadge />
      <UserSummary />
    </header>

    <section class="panel">
      <form class="form" @submit.prevent="addUser">
        <label class="field">
          Name
          <input v-model.trim="name" required maxlength="120" />
        </label>
        <label class="field">
          Email
          <input v-model.trim="email" type="email" required maxlength="240" />
        </label>
        <button class="button" type="submit" :disabled="saving">{{ saving ? 'Saving...' : 'Add user' }}</button>
        <button class="button" type="button" :disabled="loading" @click="loadUsers">Refresh</button>
      </form>
      <p v-if="error" class="error">{{ error }}</p>
      <p v-if="status" class="ok">{{ status }}</p>
    </section>

    <section class="panel">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="loading">
            <td colspan="4">Loading...</td>
          </tr>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.name }}</td>
            <td>{{ user.email }}</td>
            <td><button class="button danger" @click="deleteUser(user.id)">Delete</button></td>
          </tr>
        </tbody>
      </table>
    </section>
  </main>
</template>
