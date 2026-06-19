<script setup>
import OvsWidget from './OvsWidget.ovs'
import CsstsPanel from './CsstsPanel.vue'
import tokenStyle from './tokens.cssts'
</script>

<template>
  <main class="page-shell">
    <section class="comparison-header">
      <p class="eyebrow">QIN + GENERATED TS JAVA SLIMEPARSER</p>
      <h1>OVS + CSSTS Qin TS comparison</h1>
      <p>
        This project is served by Qin. The OVS and CSSTS parser sources stay in TypeScript,
        while the Slime superclass comes from Qin's Java-to-TypeScript generated package.
      </p>
      <p :class="tokenStyle">Loaded direct .cssts module style.</p>
    </section>

    <div class="demo-grid">
      <OvsWidget />
      <CsstsPanel />
    </div>
  </main>
</template>
