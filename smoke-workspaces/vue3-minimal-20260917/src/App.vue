<template>
  <main>
    <h1>{{ title }}</h1>
    <p>{{ message }}</p>
    <section>
      <h2>Backend says</h2>
      <p>{{ backendMessage }}</p>
    </section>
  </main>
</template>

<script setup>
import { onMounted, ref } from "vue"

const title = "Hello from Qin + Vue 3"
const message = "This SFC is transformed by Qin's Vue plugin path."
const backendMessage = ref("Loading Qin backend...")

onMounted(async () => {
  const response = await fetch("/api/message")
  const data = await response.json()
  backendMessage.value = data.message
})
</script>
