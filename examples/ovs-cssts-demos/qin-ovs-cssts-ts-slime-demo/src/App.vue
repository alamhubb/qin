<script setup>
import OvsWidget from './OvsWidget.ovs'
import CsstsPanel from './CsstsPanel.vue'
import tokenStyle from './tokens.cssts'
</script>

<template>
  <main class="page-shell">
    <section class="comparison-header">
      <p class="eyebrow">Qin + handwritten TypeScript parsers</p>
      <h1>OVS + CSSTS Qin comparison</h1>
      <p>
        This project is served by Qin and uses the handwritten TypeScript OVS/CSSTS parser packages.
      </p>
      <p :class="tokenStyle">Loaded direct .cssts module style.</p>
    </section>

    <div class="demo-grid">
      <OvsWidget />
      <CsstsPanel />
    </div>
  </main>
</template>
