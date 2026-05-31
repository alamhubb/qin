<template>
  <main :class="pageStyle">
    <section :class="cardStyle">
      <p :class="eyebrowStyle">Qin Native Vue + CSSTS</p>
      <h1 :class="titleStyle">{{ title }}</h1>
      <p :class="subtitleStyle">{{ subtitle }}</p>
      <button :class="buttonStyle">Rendered from lang=cssts</button>
      <section id="ovs-demo"></section>
    </section>
  </main>
</template>

<script setup lang="cssts">
import "./OvsDemo.ovs";

const title = "Hello from .vue";
const subtitle = "Rendered by Qin dev server on one port.";
const pageStyle = css { displayFlex, alignItemsCenter, justifyContentCenter, padding32px };
const cardStyle = css { backgroundColorWhite, borderRadius16px, padding24px };
const eyebrowStyle = css { colorBlue, fontWeight700 };
const titleStyle = css { colorRed, fontWeight700 };
const subtitleStyle = css { colorGray };
const buttonStyle = css { displayInlineFlex, padding16px, backgroundColorBlue, colorWhite, borderRadius8px, cursorPointer };
</script>
