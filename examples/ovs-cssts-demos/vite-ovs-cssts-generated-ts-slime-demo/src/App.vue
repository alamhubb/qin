<script setup>
import OvsWidget from './OvsWidget.ovs'
import CsstsPanel from './CsstsPanel.vue'
import tokenStyle from './tokens.cssts'
import { runtimeEntryPath, runtimeFileCount, runtimeMode, bundleSize, parserClass } from 'virtual:qin-generated-slime-status'
</script>

<template>
  <main class="page-shell">
    <section class="comparison-header">
      <p class="eyebrow">Vite + Qin generated Java-to-TS parser check</p>
      <h1>OVS + CSSTS Vite comparison</h1>
      <p>
        This project uses the same Vite OVS/CSSTS transform chain, then validates transformed
        .ovs and .cssts modules with Qin's per-Java-class TypeScript ESM SlimeParser output.
      </p>
      <p class="runtime-line">{{ parserClass }} loaded via {{ runtimeMode }}, {{ runtimeFileCount }} generated TS files, entry {{ bundleSize }} bytes.</p>
      <p class="runtime-path">{{ runtimeEntryPath }}</p>
      <p :class="tokenStyle">Loaded direct .cssts module style.</p>
    </section>

    <div class="demo-grid">
      <OvsWidget />
      <CsstsPanel />
    </div>
  </main>
</template>
