<template>
  <main :class="pageStyle">
    <section :class="cardStyle">
      <p :class="eyebrowStyle">Qin Vue without Vite</p>
      <h1 :class="titleStyle">{{ title }}</h1>
      <p :class="subtitleStyle">{{ subtitle }}</p>
    </section>
  </main>
</template>

<script setup lang="cssts">
const title = "Hello from Qin Vue Minimal HMR";
const subtitle = "This .vue file is served by Qin dev server from qin.config.json dependencies.";

const pageStyle = css { displayFlex, alignItemsCenter, justifyContentCenter, minHeight100vh, backgroundColorBlue };
const cardStyle = css { backgroundColorWhite, borderRadius16px, padding24px };
const eyebrowStyle = css { colorBlue, fontWeight700 };
const titleStyle = css { colorRed, fontWeight700 };
const subtitleStyle = css { colorGray };
</script>
