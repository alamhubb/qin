import DefaultTheme from 'vitepress/theme'
import './custom.css'
import CodeTabs from './components/CodeTabs.vue'
import type { Theme } from 'vitepress'

export default {
  extends: DefaultTheme,
  enhanceApp({ app }) {
    app.component('CodeTabs', CodeTabs)
  },
} satisfies Theme
