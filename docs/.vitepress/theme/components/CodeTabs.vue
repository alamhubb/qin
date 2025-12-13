<script setup lang="ts">
import { ref, useSlots } from 'vue'

const props = defineProps<{
  tabs: string[]
}>()

const activeTab = ref(0)
const slots = useSlots()
</script>

<template>
  <div class="code-tabs">
    <div class="code-tabs-header">
      <button
        v-for="(tab, index) in tabs"
        :key="tab"
        :class="{ active: activeTab === index }"
        @click="activeTab = index"
      >
        {{ tab }}
      </button>
    </div>
    <div class="code-tabs-content">
      <div
        v-for="(tab, index) in tabs"
        :key="tab"
        :class="{ active: activeTab === index }"
      >
        <slot :name="`tab-${index}`" />
      </div>
    </div>
  </div>
</template>
