import vue from '@vitejs/plugin-vue'
import vitePluginOvs from 'vite-plugin-ovs'
import qinGeneratedTsSlimeParser from './plugins/qinGeneratedTsSlimeParserPlugin.mjs'

export default {
  plugins: [
    ...vitePluginOvs({
      cssts: {
        classPrefix: 'cmp-'
      }
    }),
    qinGeneratedTsSlimeParser({
      packageName: '@qin/generated-slime-parser-ts'
    }),
    vue()
  ]
}
