import { createApp } from "vue"
import BalanceDashboard from "./BalanceDashboard.ovs"
import { BalancePanel } from "./BalancePanel.js"
import "./style.css"

createApp(BalanceDashboard).mount("#app")
createApp(BalancePanel).mount("#balance-panel")
