<script setup lang="cssts">
import { ref } from 'vue'

const count = ref(0)
const title = 'Rendered from Vue lang=cssts'

const panelStyle = css {
  displayFlex,
  flexDirectionColumn,
  gap12px,
  padding20px,
  borderRadius8px,
  borderStyleSolid,
  borderWidth1px,
  borderColorLightgray,
  backgroundColorWhite
}

const countStyle = css {
  fontSize20px,
  fontWeight700,
  colorBlue
}

const buttonStyle$$hover = css { backgroundColorNavy }

const buttonStyle = css {
  paddingX12px,
  paddingY8px,
  borderNone,
  borderRadius6px,
  backgroundColorBlue,
  colorWhite,
  cursorPointer,
  buttonStyle$$hover
}

function increment() {
  count.value++
}
</script>

<template>
  <article :class="panelStyle">
    <h2>{{ title }}</h2>
    <p :class="countStyle">CSSTS count: {{ count }}</p>
    <button :class="buttonStyle" @click="increment">Increment CSSTS</button>
  </article>
</template>
