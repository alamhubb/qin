<script setup>
import OvsWidget from './OvsWidget.ovs'
import CsstsPanel from './CsstsPanel.vue'
import tokenStyle from './tokens.cssts'
</script>

<template>
  <main class="page-shell">
    <section class="comparison-header">
      <p class="eyebrow">Vite + TypeScript plugins</p>
      <h1>OVS + CSSTS Vite comparison</h1>
      <p>
        This project uses the existing TypeScript vite-plugin-ovs and vite-plugin-cssts chain.
      </p>
      <p :class="tokenStyle">Loaded direct .cssts module style.</p>
    </section>

    <div class="demo-grid">
      <OvsWidget />
      <CsstsPanel />
    </div>
  </main>
</template>
