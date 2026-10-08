import { onMounted, ref } from "/@qin-mod/qin-vue-runtime.js?qin-vue=runtime"
const _sfc_main = { setup(__props) {


const title = "Hello from Qin + Vue 3"
const message = "This SFC is transformed by Qin's Vue plugin path."
const backendMessage = ref("Loading Qin backend...")

onMounted(async () => {
  const response = await fetch("/api/message")
  const data = await response.json()
  backendMessage.value = data.message
})

return { onMounted, ref, title, message, backendMessage };
} };
import { h as _h, toDisplayString as _toDisplayString } from "/@qin-mod/qin-vue-runtime.js?qin-vue=runtime"
function render(_ctx, _cache) { return _h("main", null, [_h("h1", null, _toDisplayString(_ctx.title)), _h("p", null, _toDisplayString(_ctx.message)), _h("section", null, [_h("h2", null, "Backend says"), _h("p", null, _toDisplayString(_ctx.backendMessage))])]); }
_sfc_main.render = render;
export default _sfc_main;
